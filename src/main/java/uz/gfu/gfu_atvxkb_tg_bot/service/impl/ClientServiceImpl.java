package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

@Service
public class ClientServiceImpl implements ClientService {
    private static final String START = "/start";
    private final UserService userService;
    private final GeneralService generalService;
    private final FeedbackService feedbackService;
    private final SubFeedbackService subFeedbackService;
    private final AdminService adminService;
    private final BuildingService buildingService;

    public ClientServiceImpl(UserService userService, GeneralService generalService, FeedbackService feedbackService, SubFeedbackService subFeedbackService, AdminService adminService, BuildingService buildingService) {
        this.userService = userService;
        this.generalService = generalService;
        this.feedbackService = feedbackService;
        this.subFeedbackService = subFeedbackService;
        this.adminService = adminService;
        this.buildingService = buildingService;
    }

    @Override
    public void clientHasMessage(BotUser client, Message message, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        switch (client.getState()) {
            case START -> stateStart(message, sendMessage, client);
            case LAST_NAME -> stateLastName(message, sendMessage, client);
            case NAME -> stateName(message, sendMessage, client);
            case BLOCK -> stateBlock(message, sendMessage, client);
            case GET_DEPARTMENT -> stateDepartment(message, sendMessage, client);
            case GET_ROOM_NUM -> stateRoomNumber(message, sendMessage, client);
            case SHARE_PHONE_NUMBER -> statePhoneNumber(message, sendMessage, client);
            case GET_FEEDBACK -> stateFeedback(message, sendMessage, client);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clientHasCallBackQuery(BotUser client, CallbackQuery callbackQuery, AbsSender sender) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(chatId.toString());

        switch (data) {
            case BotQuery.UZ_SELECT -> sendMessage.setText(ResMessageUz.ENTER_LASTNAME);
            case BotQuery.RU_SELECT -> sendMessage.setText(ResMessageRu.ENTER_LASTNAME);
        }
        if (client.getState().equals(UserState.REGISTER_DONE)) {
            switch (data) {
                case BotQuery.DONE -> stateDone(callbackQuery, sendMessage, client);
                case BotQuery.EDIT -> stateEdit(callbackQuery, sendMessage, client);
            }
        }
        if (data != null &&client.getState().equals(UserState.GET_SUB_FEEDBACK)) {
            stateSubFeedback(data, sendMessage, client);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateStart(Message message, SendMessage sendMessage, BotUser client) {
        if (message.getText() != null && message.getText().equalsIgnoreCase(START)) {
            userService.saveState(client);
            sendMessage.setText(ResMessageUz.START);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
        } else {
            sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
        }
    }

    @Override
    public void stateLastName(Message message, SendMessage sendMessage, BotUser client) {
        userService.saveUserLastname(message.getText(), client.getChatId());
        sendMessage.setText(ResMessageUz.ENTER_NAME);
    }

    @Override
    public void stateName(Message message, SendMessage sendMessage, BotUser client) {
        userService.saveUserFirstname(message.getText(), client.getChatId());
        sendMessage.setText(ResMessageUz.ENTER_BLOCK);
        sendMessage.setReplyMarkup(generalService.getBlock());
    }

    @Override
    public void stateBlock(Message message, SendMessage sendMessage, BotUser client) {
        userService.saveBlock(message.getText(), client.getChatId());
        sendMessage.setText(ResMessageUz.ENTER_DEPARTMENT);
    }

    @Override
    public void stateDepartment(Message message, SendMessage sendMessage, BotUser client) {
        userService.saveUserDepartmentName(message.getText(), client.getChatId());
        sendMessage.setText(ResMessageUz.ENTER_ROOM_NUMBER);
    }

    @Override
    public void stateRoomNumber(Message message, SendMessage sendMessage, BotUser client) {
        userService.saveUserRoomNum(message.getText(), client.getChatId());
        sendMessage.setText(ResMessageUz.ENTER_PHONE_NUMBER);
        sendMessage.setReplyMarkup(generalService.getPhoneNumber());
    }

    @Override
    public void statePhoneNumber(Message message, SendMessage sendMessage, BotUser client) {
        Contact contact = message.getContact();
        userService.saveUserPhoneNumber(contact, client.getChatId());
        sendMessage.enableHtml(true);
        sendMessage.setText(ResMessageUz.SHOW_DATA + userService.showUserData(client.getId(), client.getChatId()));
        sendMessage.setReplyMarkup(generalService.getRegisterDone());
        sendMessage.setChatId(client.getChatId());
    }

    @Override
    public void stateDone(CallbackQuery callbackQuery, SendMessage sendMessage, BotUser client) {
        if (client.getState() == UserState.GET_SUB_FEEDBACK){
            sendMessage.setText(ResMessageUz.SUCCESS);
            sendMessage.setReplyMarkup(generalService.getFeedbacks());
            for (BotUser admins : userService.getAllAdmins()) {
                adminService.adminHasMessage(admins,callbackQuery.getMessage(),sendMessage );
            }
        }else {
            sendMessage.setText(ResMessageUz.DONE);
            sendMessage.setReplyMarkup(generalService.getFeedbacks());
            userService.changStateFeedback(client);
        }
    }

    @Override
    public void stateEdit(CallbackQuery callbackQuery, SendMessage sendMessage, BotUser client) {
        sendMessage.setText(ResMessageUz.EDIT);
    }

    @Override
    public void stateFeedback(Message message, SendMessage sendMessage, BotUser client) {
        feedbackService.saveFeedback(message.getText(), client);
        sendMessage.setChatId(client.getChatId().toString());
        sendMessage.setText(ResMessageUz.CHOOSE_SERVICE);
        sendMessage.setReplyMarkup(generalService.getSubFeedbacks(message.getText()));
    }

    @Override
    public void stateSubFeedback(String data, SendMessage sendMessage, BotUser client) {
        subFeedbackService.saveSubFeedback(data, client);
        sendMessage.setText(ResMessageUz.DONE_SERVICE + userService.clientShowFeedback(client));
        sendMessage.setReplyMarkup(generalService.getRegisterDone());
    }

}
