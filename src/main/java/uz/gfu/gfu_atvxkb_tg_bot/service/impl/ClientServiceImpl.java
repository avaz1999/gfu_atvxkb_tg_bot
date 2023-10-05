package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.enums.Role;
import uz.gfu.gfu_atvxkb_tg_bot.enums.State;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.repository.ApplicationRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

import java.awt.print.Pageable;
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {
    private static final String START = "/start";
    private final UserService userService;
    private final GeneralService generalService;
    private final FeedbackService feedbackService;
    private final SubFeedbackService subFeedbackService;
    private final AdminService adminService;
    private final ApplicationRepository applicationRepository;

    public ClientServiceImpl(UserService userService, GeneralService generalService, FeedbackService feedbackService, SubFeedbackService subFeedbackService, AdminService adminService, ApplicationRepository applicationRepository) {
        this.userService = userService;
        this.generalService = generalService;
        this.feedbackService = feedbackService;
        this.subFeedbackService = subFeedbackService;
        this.adminService = adminService;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void clientHasMessage(BotUser client, Message message, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        switch (client.getState()) {
            case START -> stateStart(message, sendMessage, client, sender);
            case CHOOSE_LANG -> stateChooseLang(sendMessage, client, sender);
            case LAST_NAME -> stateLastName(message, sendMessage, client, sender);
            case NAME -> stateName(message, sendMessage, client, sender);
            case BLOCK -> stateBlock(message, sendMessage, client, sender);
            case GET_DEPARTMENT -> stateDepartment(message, sendMessage, client, sender);
            case GET_ROOM_NUM -> stateRoomNumber(message, sendMessage, client, sender);
            case SHARE_PHONE_NUMBER -> statePhoneNumber(message, sendMessage, client, sender);
            case GET_FEEDBACK -> stateFeedback(message, sendMessage, client, sender);
            case GET_SUB_FEEDBACK -> stateTextSubFeedback(client, message, sendMessage, sender);
            case REGISTER_DONE -> stateRegisterDone(sendMessage, client, sender);
        }
    }

    private void stateTextSubFeedback(BotUser client, Message message, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ERROR_SERVICE
                    : ResMessageRu.ERROR_SERVICE;
            sendMessage.setText(msg);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void stateRegisterDone(SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (Objects.equals(client.getLanguage(), BotQuery.UZ_SELECT))
            sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
        else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void stateChooseLang(SendMessage sendMessage, BotUser client, AbsSender sender) {
        sendMessage.setText(ResMessageUz.ERROR_CHOOSE_LANG);
        sendMessage.setChatId(client.getChatId());
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
            case BotQuery.UZ_SELECT -> uzSelect(client, data, sendMessage, sender);
            case BotQuery.RU_SELECT -> ruSelect(client, data, sendMessage, sender);
            default -> {
                if (client.getState().equals(UserState.GET_FEEDBACK)){
                    String[] split = data.split("#");
                    Byte rate = Byte.valueOf(split[0]);
                    Long adminId = Long.valueOf(split[1]);
                    userService.rateAdmin(client,rate,adminId,sendMessage,sender);
                }
            }
        }
        if (client.getState().equals(UserState.REGISTER_DONE) ||
                client.getState().equals(UserState.SAVE_SUB_FEEDBACK)) {
            switch (data) {
                case BotQuery.DONE -> stateDone(callbackQuery, sendMessage, client, sender);
                case BotQuery.EDIT -> stateEdit(callbackQuery, sendMessage, client,sender);
            }
        }
        if (client.getState().equals(UserState.GET_SUB_FEEDBACK)) {
            stateSubFeedback(data, sendMessage, client, sender);
        }
    }

    private void ruSelect(BotUser client, String data, SendMessage sendMessage, AbsSender sender) {
        userService.saveLang(client, data);
        sendMessage.setText(ResMessageRu.ENTER_LASTNAME);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void uzSelect(BotUser client, String data, SendMessage sendMessage, AbsSender sender) {
        userService.saveLang(client, data);
        sendMessage.setText(ResMessageUz.ENTER_LASTNAME);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateStart(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (message.getText() != null && message.getText().equalsIgnoreCase(START)) {
            userService.saveState(client);
            sendMessage.enableHtml(true);
            sendMessage.setText(ResMessageUz.START);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
        } else {
            sendMessage.setText(ResMessageUz.ERROR_START);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateLastName(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
            sendMessage.setText(ResMessageUz.ENTER_NAME);
        } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
            sendMessage.setText(ResMessageRu.ENTER_NAME);
        }
        userService.saveUserLastname(message.getText(), client.getChatId());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateName(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
            sendMessage.setText(ResMessageUz.ENTER_BLOCK);
        } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
            sendMessage.setText(ResMessageRu.ENTER_BLOCK);
        }
        userService.saveUserFirstname(message.getText(), client.getChatId());
        sendMessage.setReplyMarkup(generalService.getBlock());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateBlock(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        userService.saveBlock(message.getText(), client, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateDepartment(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            userService.saveUserDepartmentName(text, client);
            if (client.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ENTER_ROOM_NUMBER);
            else if (client.getLanguage().equals(BotQuery.RU_SELECT))
                sendMessage.setText(ResMessageRu.ENTER_ROOM_NUMBER);

        } else {
            errorMessage(client, sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateRoomNumber(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (message.hasText()) {
            userService.saveUserRoomNum(message.getText(), client);
            if (client.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ENTER_PHONE_NUMBER);
            else if (client.getLanguage().equals(BotQuery.RU_SELECT))
                sendMessage.setText(ResMessageRu.ENTER_PHONE_NUMBER);
            sendMessage.setReplyMarkup(generalService.getPhoneNumber(client));
        } else {
            errorMessage(client, sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void statePhoneNumber(Message message, SendMessage sendMessage,BotUser client, AbsSender sender) {

        sendMessage.enableHtml(true);
        sendMessage.setChatId(client.getChatId());

        if (message.hasContact()) {
            processContactPhoneNumber(message, sendMessage, client, sender);
        } else {
            processTextPhoneNumber(message.getText(), sendMessage, client, sender);
        }
    }

    private void processContactPhoneNumber(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        Contact contact = message.getContact();
        String phoneNumber = contact.getPhoneNumber();

        if (phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.substring(1);
        }

        processPhoneNumberLogic(phoneNumber, sendMessage, client, sender);
    }

    private void processTextPhoneNumber(String phoneNumber, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.substring(1);
        }

        if (isValidPhoneNumber(phoneNumber)) {
            if (checkPhoneNumber(phoneNumber)){
                processPhoneNumberLogic(phoneNumber, sendMessage, client, sender);
            }else handleInvalidPhoneNumberInText(sendMessage,client);
        } else {
            handleInvalidPhoneNumber(sendMessage, client);
        }
    }

    private void handleInvalidPhoneNumberInText(SendMessage sendMessage, BotUser client) {
        String errorMessage = client.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.ERROR_PHONE_NUMBER
                : ResMessageRu.ERROR_PHONE_NUMBER;

        sendMessage.setText(errorMessage);
    }

    private void processPhoneNumberLogic(String phoneNumber, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (userService.checkPhoneNumber(phoneNumber)) {
            BotUser admin = userService.findAdminByPhoneNumber(phoneNumber);
            userService.saveAdminPhoneNumber(sendMessage, phoneNumber, admin, sender, client);
        } else {
            userService.saveUserPhoneNumber(sendMessage, phoneNumber, client, sender);
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("998") && phoneNumber.length() == 12;
    }

    private void handleInvalidPhoneNumber(SendMessage sendMessage, BotUser client) {
        String errorMessage = client.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.ERROR_MESSAGE
                : ResMessageRu.ERROR_MESSAGE;

        sendMessage.setText(errorMessage);
    }

    private boolean checkPhoneNumber(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void stateDone(CallbackQuery callbackQuery, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (client.getState() == UserState.SAVE_SUB_FEEDBACK) {
            if (client.getRole().equals(Role.CLIENT)) {
                String success = client.getLanguage().equals(BotQuery.UZ_SELECT)
                        ? ResMessageUz.SUCCESS
                        : ResMessageRu.SUCCESS;
                sendMessage.setText(success);
                sendMessage.setChatId(client.getChatId());
                sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
                userService.changeStateGetFeedback(client);
                try {
                    sender.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            for (BotUser admins : userService.getAllAdmins()) {
                Application application =
                        applicationRepository
                                .findTopByUserIdAndDoneAndDeletedFalseOrderByCreatedAtDesc(client.getId(), State.CREATED);
                userService.sendMessageToAdmin(admins, client, application, sendMessage, sender);
            }
        } else {
            String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.DONE
                    : ResMessageRu.DONE;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
            userService.changStateFeedback(client);
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stateEdit(CallbackQuery callbackQuery, SendMessage sendMessage, BotUser client,AbsSender sender) {
        if(client.getState().equals(UserState.REGISTER_DONE)){
            String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.EDIT_DATA
                    : ResMessageRu.EDIT_DATA;
            sendMessage.setText(msg);
            sendMessage.setChatId(client.getChatId());
            userService.changeStateEditData(client);
        }else {
            String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.EDIT
                    : ResMessageRu.EDIT;
            sendMessage.setText(msg);
            sendMessage.setChatId(client.getChatId());
            sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
            String data = callbackQuery.getData();
            subFeedbackService.clientEditSubFeedback(data,client);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stateFeedback(Message message, SendMessage sendMessage, BotUser client, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            if (text.equals(BotQuery.GET_SERVICE)) {
                String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                        ? ResMessageUz.CHOOSE_SERVICE
                        : ResMessageRu.CHOOSE_SERVICE;
               sendMessage.setText(msg);
                sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
            } else {
                feedbackService.saveFeedback(text, client, sendMessage);
            }
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorMessage(client, sendMessage);
        }
    }

    @Override
    public void stateSubFeedback(String data, SendMessage sendMessage, BotUser client, AbsSender sender) {
        subFeedbackService.saveSubFeedback(data, client, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static void errorMessage(BotUser client, SendMessage sendMessage) {
        String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.ERROR_MESSAGE
                : ResMessageRu.ERROR_MESSAGE;
        sendMessage.setText(msg);
    }
}
