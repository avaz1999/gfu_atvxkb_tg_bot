package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

import static uz.gfu.gfu_atvxkb_tg_bot.GfuAtvxkbTgBotApplication.bot;

@Service
public class ClientServiceImpl implements ClientService {
    private static final String START = "/start";
    private final UserService userService;
    private final GeneralService generalService;
    private final FeedbackService feedbackService;
    private final BuildingService buildingService;

    public ClientServiceImpl(UserService userService, GeneralService generalService, FeedbackService feedbackService, BuildingService buildingService) {
        this.userService = userService;
        this.generalService = generalService;
        this.feedbackService = feedbackService;
        this.buildingService = buildingService;
    }

    @Override
    public void clientHasMessage(BotUser client, Message message, String text, SendMessage sendMessage) {
        Long chatId = message.getChatId();
        BotUser currentUser = userService.getCurrentUser(chatId, message);
        if (message.hasContact() && currentUser.getCurrentPage() == 7) {
            try {
                Contact contact = message.getContact();
                userService.saveUserPhoneNumber(contact, currentUser.getId());
                userService.nextPage(currentUser);
                sendMessage.setText(ResMessageUz.SHOW_DATA + userService.showUserData(currentUser.getId(), chatId));
                sendMessage.setReplyMarkup(generalService.getInlineKeyboardButton(currentUser));
                sendMessage.setChatId(chatId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        sendMessage.setChatId(chatId);
        checkCurrentUserPages(currentUser, message, sendMessage, text, chatId);

        try {
            if (sendMessage.getText().isEmpty())
                return;
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void checkCurrentUserPages(BotUser currentUser, Message message, SendMessage sendMessage, String text, Long chatId) {
        Long userId = currentUser.getId();
        switch (currentUser.getCurrentPage()) {
            case 2 -> {
                userService.saveUserLastname(text, userId);
                userService.nextPage(currentUser);
                sendMessage.setText(ResMessageUz.ENTER_NAME);
            }
            case 3 -> {
                userService.saveUserFirstname(text, userId);
                userService.nextPage(currentUser);
                sendMessage.setText(ResMessageUz.ENTER_BLOCK);
                sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
            }
            case 4 -> {
                if (buildingService.checkBuilding(text)) {
                    buildingService.saveBuilding(text,userId);
                    userService.nextPage(currentUser);
                    sendMessage.setText(ResMessageUz.ENTER_DEPARTMENT);
                }else {
                    userService.prev(currentUser);
                }
            }
            case 5 -> {
                userService.saveUserDepartmentName(text, userId);
                userService.nextPage(currentUser);
                sendMessage.setText(ResMessageUz.ENTER_ROOM_NUMBER);
            }
            case 6 -> {
                userService.saveUserRoomNum(text, userId);
                userService.nextPage(currentUser);
                sendMessage.setText(ResMessageUz.ENTER_PHONE_NUMBER);
                sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
            }
            case 9 -> {
                feedbackService.saveFeedback(text,currentUser);
                userService.nextPage(currentUser);
                sendMessage.setText(ResMessageUz.SERVICE);
                sendMessage.setReplyMarkup(generalService.getInlineKeyboardButtonForService(currentUser,text));
            }
        }
    }

    @Override
    public void clientHasCallBackQuery(BotUser client, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        switch (client.getCurrentPage()) {
            case 1 -> {
                switch (data) {
                    case BotQuery.UZ_SELECT -> sendMessage.setText(ResMessageUz.ENTER_LASTNAME);
                    case BotQuery.RU_SELECT -> sendMessage.setText(ResMessageRu.ENTER_LASTNAME);
                }
                try {
                    sendMessage.setChatId(chatId);
                    bot.execute(sendMessage);
                    userService.nextPage(client);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            case 8 -> {
                switch (data) {
                    case BotQuery.DONE -> {
                        sendMessage.setText(ResMessageUz.DONE);
                        sendMessage.setReplyMarkup(generalService.getReplyKeyboard(client));
                    }
                    case BotQuery.EDIT -> sendMessage.setText(ResMessageUz.EDIT);
                }
                try {
                    sendMessage.setChatId(chatId);
                    bot.execute(sendMessage);
                    userService.nextPage(client);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            case 10 -> {
                switch (data) {
                    case BotQuery.ACTIVATION_WINDOWS -> {
                        userService.nextPage(client);
                        sendMessage.setText(ResMessageUz.SUCCESS);
                    }
                    case BotQuery.BACK -> {
                        userService.back(client);
                        sendMessage.setText(ResMessageUz.SERVICE);
                        sendMessage.setReplyMarkup(generalService.getReplyKeyboard(client));
                    }
                }
                try {
                    sendMessage.setChatId(chatId);
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
