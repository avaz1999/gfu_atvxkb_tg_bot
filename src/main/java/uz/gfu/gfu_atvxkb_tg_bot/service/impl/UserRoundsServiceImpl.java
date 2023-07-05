package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
public class UserRoundsServiceImpl implements UserRoundsService {
    private final UserService userService;
    private final GeneralService generalService;
    private final BuildingService buildingService;
    private final FeedbackService feedbackService;

    public UserRoundsServiceImpl(UserService userService, GeneralService generalService, BuildingService buildingService, FeedbackService feedbackService) {
        this.userService = userService;
        this.generalService = generalService;
        this.buildingService = buildingService;
        this.feedbackService = feedbackService;
    }

    @Override
    public void getRound1(BotUser client, String data, SendMessage sendMessage) {
        switch (data) {
            case BotQuery.UZ_SELECT -> {
                sendMessage.setText(ResMessageUz.ENTER_LASTNAME);
            }
            case BotQuery.RU_SELECT -> sendMessage.setText(ResMessageRu.ENTER_LASTNAME);
        }
    }

    @Override
    //Familyasini kiritganda ishlaydi
    public void getRound2(BotUser currentUser, String text, SendMessage sendMessage) {
        Long userId = currentUser.getId();
        userService.saveUserLastname(text, userId);
        userService.nextPage(currentUser);
        sendMessage.setText(ResMessageUz.ENTER_NAME);
        sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
    }

    @Override
    public void getRound3(BotUser currentUser, String text, SendMessage sendMessage) {
        Long userId = currentUser.getId();
        if (text.equalsIgnoreCase(BotQuery.BACK)) {
            back(currentUser, ResMessageUz.ENTER_LASTNAME, sendMessage);
        } else {
            userService.saveUserFirstname(text, userId);
            userService.nextPage(currentUser);
            sendMessage.setText(ResMessageUz.ENTER_BLOCK);
            sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
        }
    }

    @Override
    public void getRound4(BotUser currentUser, String text, SendMessage sendMessage) {
        Long userId = currentUser.getId();
        if (text.equalsIgnoreCase(BotQuery.BACK)) {
            back(currentUser, ResMessageUz.ENTER_NAME, sendMessage);
        } else if (buildingService.checkBuilding(text)) {
            buildingService.saveBuilding(text, userId);
            userService.nextPage(currentUser);
            sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
            sendMessage.setText(ResMessageUz.ENTER_DEPARTMENT);
        } else {
            userService.prev(currentUser);
        }
    }

    @Override
    public void getRound5(BotUser currentUser, String text, SendMessage sendMessage) {
        Long userId = currentUser.getId();
        if (text.equalsIgnoreCase(BotQuery.BACK)) {
            back(currentUser, ResMessageUz.ENTER_BLOCK, sendMessage);
            sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
        } else {
            userService.saveUserDepartmentName(text, userId);
            userService.nextPage(currentUser);
            sendMessage.setText(ResMessageUz.ENTER_ROOM_NUMBER);
            sendMessage.setReplyMarkup(generalService.getBack(currentUser));
        }
    }

    @Override
    public void getRound6(BotUser currentUser, String text, SendMessage sendMessage) {
        Long userId = currentUser.getId();
        if (text.equalsIgnoreCase(BotQuery.BACK)) {
            back(currentUser, ResMessageUz.ENTER_DEPARTMENT, sendMessage);
        } else {
            userService.saveUserRoomNum(text, userId);
            userService.nextPage(currentUser);
            sendMessage.setText(ResMessageUz.ENTER_PHONE_NUMBER);
            sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
        }
    }

    @Override
    public void getRound7(BotUser currentUser, Message message, SendMessage sendMessage) {
        if (message.getText() != null && message.getText().equalsIgnoreCase(BotQuery.BACK)) {
            back(currentUser, ResMessageUz.ENTER_ROOM_NUMBER, sendMessage);
        }
        if (message.hasContact() && currentUser.getCurrentPage() == 7) {
            Long chatId = message.getChatId();
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
    }

    @Override
    public void getRound8(BotUser client, String data, SendMessage sendMessage) {
        switch (data) {
            case BotQuery.DONE -> {
                sendMessage.setText(ResMessageUz.DONE);
                sendMessage.setReplyMarkup(generalService.getReplyKeyboard(client));
            }
            case BotQuery.EDIT -> sendMessage.setText(ResMessageUz.EDIT);
        }
    }

    @Override
    public void getRound9(BotUser currentUser, String text, SendMessage sendMessage) {
        feedbackService.saveFeedback(text, currentUser);
        userService.nextPage(currentUser);
        sendMessage.setText(ResMessageUz.SERVICE);
        sendMessage.setReplyMarkup(generalService.getInlineKeyboardButtonForService(currentUser, text));
    }

    @Override
    public void getRound10(BotUser client, String data, SendMessage sendMessage) {
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
    }

    private void back(BotUser currentUser, String msg, SendMessage sendMessage) {
        userService.back(currentUser);
        sendMessage.setText(msg);
    }
}
