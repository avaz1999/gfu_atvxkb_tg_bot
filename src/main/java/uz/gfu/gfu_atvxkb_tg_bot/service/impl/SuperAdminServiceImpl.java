package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    private final GeneralService generalService;
    private final UserService userService;
    private final BuildingService buildingService;
    private final FeedbackService feedbackService;
    private final SubFeedbackService subFeedbackService;

    public SuperAdminServiceImpl(GeneralService generalService, UserService userService, BuildingService buildingService, FeedbackService feedbackService, SubFeedbackService subFeedbackService) {
        this.generalService = generalService;
        this.userService = userService;
        this.buildingService = buildingService;
        this.feedbackService = feedbackService;
        this.subFeedbackService = subFeedbackService;
    }

    @Override
    public void superAdminHasMessage(BotUser superAdmin, Message message, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        userService.saveSuperAdmin(superAdmin, message);
        switch (superAdmin.getState()) {
            case SETTING -> superAdminStateSetting(message, superAdmin, sendMessage, sender);
            case SUPER_ADMIN_CRUD -> crudSuperAdminState(message, superAdmin, sendMessage, sender);

            case CRUD_BUILDING -> crudBuilding(message, superAdmin, sendMessage, sender);
            case ADD_BUILDING_STATE -> createNewBuilding(message, superAdmin, sendMessage, sender);
            case EDIT_BUILDING_STATE,
                    EDIT_ADMIN_STATE,
                    REMOVE_FEEDBACK_STATE,
                    EDIT_FEEDBACK_STATE,
                    ADD_SUB_FEEDBACK_STATE,
                    CHOOSE_LANG_FOR_ADD,
                    GET_ALL_SUB_FEEDBACK,
                    REMOVE_SUB_FEEDBACK_STATE-> errorState(message, superAdmin, sendMessage, sender);
            case EDIT_BUILDING_STATE_1 -> editBuilding1(message, superAdmin, sendMessage, sender);

            case CRUD_ADMIN -> crudAdminState(message, superAdmin, sendMessage, sender);
            case ADD_ADMIN_STATE -> createNewAdmin(message, superAdmin, sendMessage, sender);
            case REMOVE_ADMIN_STATE -> removeAdminState(message, superAdmin, sendMessage, sender);
            case EDIT_ADMIN_STATE1 -> editAdmin1State(message, superAdmin, sendMessage, sender);

            case CRUD_FEEDBACK -> crudFeedBackState(message, superAdmin, sendMessage, sender);
            case ADD_FEEDBACK_STATE -> addFeedbackState(message, superAdmin, sendMessage, sender);
            case ADD_FEEDBACK_STATE_RUS -> addFeedbackStateRus(message, superAdmin, sendMessage, sender);
            case EDIT_FEEDBACK_STATE_1 -> editFeedbackState(message, superAdmin, sendMessage, sender);

            case CRUD_SUB_FEEDBACK -> crudSubFeedbackState(message, superAdmin, sendMessage, sender);
            case ADD_SUB_FEEDBACK_STATE_1 -> addSubFeedbackState(message, superAdmin, sendMessage, sender);
            case EDIT_SUB_FEEDBACK_STATE_1 -> editSubFeedbackState(message,superAdmin,sendMessage,sender);
        }
    }

    private void editSubFeedbackState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            subFeedbackService.editSubFeedback(message.getText(),superAdmin,sendMessage);
            userService.changeStateSubFeedback(superAdmin);
        }else {
            errorMessage(superAdmin,sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSubFeedbackState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            subFeedbackService.addSubFeedback(text, superAdmin, sendMessage);
        } else {
            errorMessage(superAdmin, sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void addFeedbackStateRus(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            feedbackService.createNewFeedbackRus(text, superAdmin, sendMessage);
        } else {
            errorMessage(superAdmin, sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void crudSubFeedbackState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            switch (text) {
                case BotQuery.ADD_SUB_FEEDBACK -> addSubFeedback(message,superAdmin, sendMessage, sender);
                case BotQuery.REMOVE_SUB_FEEDBACK -> removeSubFeedback(message,superAdmin, sendMessage, sender);
                case BotQuery.ALL_SUB_FEEDBACK -> getAllSubFeedback(message,superAdmin, sendMessage, sender);
                case BotQuery.UPDATE_SUB_FEEDBACK -> updateSubFeedback(message,superAdmin, sendMessage, sender);
                case BotQuery.MENU -> menu(superAdmin, sendMessage, sender);
            }
        } else {
            errorMessage(superAdmin, sendMessage);
        }
    }

    private void updateSubFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.EDIT_NEW_SUB_FEEDBACK
                    : ResMessageRu.EDIT_NEW_SUB_FEEDBACK;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForEditSubFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void getAllSubFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.GET_NEW_SUB_FEEDBACK
                    : ResMessageRu.GET_NEW_SUB_FEEDBACK;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForGetSubFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeSubFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.REMOVE_NEW_SUB_FEEDBACK
                    : ResMessageRu.REMOVE_NEW_SUB_FEEDBACK;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForRemoveSubFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSubFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ADD_NEW_SUB_FEEDBACK
                    : ResMessageRu.ADD_NEW_SUB_FEEDBACK;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForAddSubFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void editFeedbackState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            feedbackService.editFeedback(text, sendMessage, superAdmin);
        } else {
            errorMessage(superAdmin, sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void addFeedbackState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            feedbackService.createNewFeedback(text, superAdmin, sendMessage);
        } else {
            errorMessage(superAdmin, sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void crudFeedBackState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            switch (text) {
                case BotQuery.ADD_FEEDBACK -> addNewFeedback(message, superAdmin, sendMessage, sender);
                case BotQuery.REMOVE_FEEDBACK -> removeFeedback(message, superAdmin, sendMessage, sender);
                case BotQuery.ALL_FEEDBACK -> allFeedback(message,superAdmin, sendMessage, sender);
                case BotQuery.UPDATE_FEEDBACK -> updateFeedback(message,superAdmin, sendMessage, sender);
                case BotQuery.MENU -> menu(superAdmin, sendMessage, sender);
                default -> {
                    if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                        sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
                    else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT))
                        sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
                    try {
                        sender.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.EDIT_SERVICE_LANG
                    : ResMessageRu.EDIT_SERVICE_LANG;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForEditFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void allFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.GET_FEEDBACK_LANG
                    : ResMessageRu.GET_FEEDBACK_LANG;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForGetAllFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.REMOVE_FEEDBACK_LANG
                    : ResMessageRu.REMOVE_FEEDBACK_LANG;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForRemoveFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNewFeedback(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ADD_NEW_FEEDBACK
                    : ResMessageRu.ADD_NEW_FEEDBACK;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getChooseLang());
            userService.changeStateChooseLangForAddFeedback(superAdmin);
        } else errorMessage(superAdmin, sendMessage);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void editAdmin1State(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            userService.editAdmin(text, superAdmin, sendMessage);
        } else {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void removeAdminState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        if (message.hasText()) {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewAdmin(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            String regexPattern = "^[0-9]+$";
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches() && text.length() == 12) {
                userService.createNewAdmin(text, superAdmin, sendMessage, sender);
            } else if (text.length() == 13) {
                if (text.startsWith("+998")) {
                    userService.createNewAdmin(text, superAdmin, sendMessage, sender);
                } else {
                    if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                        sendMessage.setText(ResMessageUz.ERROR_PHONE_NUMBER);
                    else sendMessage.setText(ResMessageRu.ERROR_PHONE_NUMBER);
                }
            } else {
                if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                    sendMessage.setText(ResMessageUz.ERROR_PHONE_NUMBER);
                else sendMessage.setText(ResMessageRu.ERROR_PHONE_NUMBER);
            }
        } else {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void crudAdminState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            switch (text) {
                case BotQuery.ADD_ADMIN -> addNewAdmin(message, superAdmin, sendMessage, sender);
                case BotQuery.REMOVE_ADMIN -> removeAdmin(superAdmin, sendMessage, sender);
                case BotQuery.ALL_ADMIN -> allAdmin(superAdmin, sendMessage, sender);
                case BotQuery.UPDATE_ADMIN -> updateAdmin(superAdmin, sendMessage, sender);
                case BotQuery.MENU -> menu(superAdmin, sendMessage, sender);
                default -> {
                    if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                        sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
                    else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT))
                        sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
                    try {
                        sender.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateAdmin(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        String getDtoAdmin = userService.getDtoAdmin(superAdmin);
        userService.changeStateEditAdmin(superAdmin);
        sendMessage.setText(ResMessageUz.EDIT_ADMIN + getDtoAdmin);
        sendMessage.setReplyMarkup(generalService.getAdminNumber());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void allAdmin(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        String getAllAdmins = userService.getDtoAdmin(superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
            sendMessage.setText(ResMessageUz.ALL_BUILDING + getAllAdmins);
        else sendMessage.setText(ResMessageRu.ALL_BUILDINGS + getAllAdmins);
        sendMessage.setReplyMarkup(generalService.crudAdmin());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeAdmin(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        String getAdmins = userService.getDtoAdmin(superAdmin);
        userService.changeStateRemoveAdmin(superAdmin);
        sendMessage.setText(ResMessageUz.REMOVE_BUILDING + getAdmins);
        sendMessage.setReplyMarkup(generalService.getAdminNumber());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void addNewAdmin(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ENTER_NEW_ADMIN_PHONE_NUMBER);
            else sendMessage.setText(ResMessageRu.ENTER_NEW_ADMIN_PHONE_NUMBER);
            userService.changeAddNewAdminState(superAdmin);
        } else {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT))
                sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void editBuilding1(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            buildingService.editBuilding(text, superAdmin, sender, sendMessage);
        }
    }


    private void superAdminStateSetting(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            if (text.equalsIgnoreCase(BotQuery.SETTING) ||
                    text.equalsIgnoreCase(BotQuery.CRUD_BUILDING) ||
                    text.equalsIgnoreCase(BotQuery.CRUD_ADMIN) ||
                    text.equalsIgnoreCase(BotQuery.CRUD_FEEDBACK) ||
                    text.equalsIgnoreCase(BotQuery.CRUD_SUB_FEEDBACK)) {

                if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) {
                    sendMessage.setText(ResMessageUz.ADMIN_CRUD_SERVICE);
                } else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) {
                    sendMessage.setText(ResMessageRu.ADMIN_CRUD_SERVICE);
                }

                if (!text.equalsIgnoreCase(BotQuery.SETTING)) {
                    crudSuperAdminState(message, superAdmin, sendMessage, sender);
                } else {
                    userService.changeStateSuperAdminCRUD(superAdmin);
                    sendMessage.setChatId(superAdmin.getChatId());
                    sendMessage.setReplyMarkup(generalService.getSettingForSuperAdmin(superAdmin));
                    try {
                        sender.execute(sendMessage);
                    } catch (
                            TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT))
                sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
            try {
                sender.execute(sendMessage);
            } catch (
                    TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private void crudSuperAdminState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            switch (text) {
                case BotQuery.CRUD_BUILDING -> crudViewBuilding(superAdmin, sendMessage, sender);
                case BotQuery.CRUD_FEEDBACK -> crudFeedBack(superAdmin, sendMessage, sender);
                case BotQuery.CRUD_SUB_FEEDBACK -> crudSubFeedback(superAdmin, sendMessage, sender);
                case BotQuery.CRUD_ADMIN -> crudAdmin(superAdmin, sendMessage, sender);
            }
        }
    }


    private void crudViewBuilding(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.BUILDING_CRUD);
        else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.BUILDING_CRUD);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setReplyMarkup(generalService.crudBuilding());
        userService.changeStateBuilding(superAdmin);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void crudFeedBack(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.FEEDBACK_CRUD);
        else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.FEEDBACK_CRUD);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setReplyMarkup(generalService.crudFeedback());
        userService.changeStateFeedback(superAdmin);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void crudSubFeedback(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.SUB_FEEDBACK_CRUD);
        else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT))
            sendMessage.setText(ResMessageRu.SUB_FEEDBACK_CRUD);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setReplyMarkup(generalService.crudSubFeedback());
        userService.changeStateSubFeedback(superAdmin);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void crudAdmin(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ADMIN_CRUD);
        else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.ADMIN_CRUD);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setReplyMarkup(generalService.crudAdmin());
        userService.changeStateAdmin(superAdmin);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void crudBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            switch (text) {
                case BotQuery.ADD_BUILDING -> addBuilding(superAdmin, sendMessage, sender);
                case BotQuery.REMOVE_BUILDING -> removeBuilding(superAdmin, sendMessage, sender);
                case BotQuery.UPDATE_BUILDING -> updateBuilding(superAdmin, sendMessage, sender);
                case BotQuery.ALL_BUILDING -> allBuilding(superAdmin, sendMessage, sender);
                case BotQuery.MENU -> menu(superAdmin, sendMessage, sender);

                default -> {
                    if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                        sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
                    else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT))
                        sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
                    try {
                        sender.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    private void menu(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) {
            sendMessage.setText(ResMessageUz.ADMIN_CRUD_SERVICE);
        } else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT)) {
            sendMessage.setText(ResMessageRu.ADMIN_CRUD_SERVICE);
        }
        userService.changeStateForSetting(superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setReplyMarkup(generalService.getSettingForSuperAdmin(superAdmin));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void allBuilding(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        String getBuildings = buildingService.getDtoBuildings(superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
            sendMessage.setText(ResMessageUz.ALL_BUILDING + getBuildings);
        else sendMessage.setText(ResMessageRu.ALL_BUILDINGS + getBuildings);
        sendMessage.setReplyMarkup(generalService.crudBuilding());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateBuilding(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        String getBuildings = buildingService.getDtoBuildings(superAdmin);
        userService.changeStateEditBuilding(superAdmin);
        sendMessage.setText(ResMessageUz.EDIT_BUILDING + getBuildings);
        sendMessage.setReplyMarkup(generalService.getBuildingNumber());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeBuilding(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        String getBuildings = buildingService.getDtoBuildings(superAdmin);
        userService.changeStateRemoveBuilding(superAdmin);
        sendMessage.setText(ResMessageUz.REMOVE_BUILDING + getBuildings);
        sendMessage.setReplyMarkup(generalService.getBuildingNumber());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void addBuilding(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ADD_BUILDING_NAME);
        else sendMessage.setText(ResMessageRu.ADD_BUILDING_NAME);
        userService.changeStateAddBuilding(superAdmin);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            String text = message.getText();
            buildingService.createNewBuilding(text, superAdmin, sendMessage);
        } else {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else if (superAdmin.getLanguage().equals(BotQuery.RU_SELECT))
                sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void errorState(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        if (message.hasText()) {
            errorMessage(superAdmin,sendMessage);
        }else {
            errorMessage(superAdmin,sendMessage);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void superAdminHasCallBackQuery(BotUser superAdmin, CallbackQuery callbackQuery, AbsSender sender) {
        String data = callbackQuery.getData();

        SendMessage sendMessage = new SendMessage();
        switch (data) {
            case BotQuery.BACK -> back(superAdmin, sendMessage, sender);
            case BotQuery.UZ_SELECT -> feedbackProsesInChooseLang(superAdmin, sendMessage, sender, false);
            case BotQuery.RU_SELECT -> feedbackProsesInChooseLang(superAdmin, sendMessage, sender, true);
            default -> chooseNumber(superAdmin, data, sendMessage, sender);
        }
    }

    private void feedbackProsesInChooseLang(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        switch (superAdmin.getState()) {
            case CHOOSE_LANG_FOR_ADD -> chooseLangForAdd(superAdmin, sendMessage, sender, lang);
            case CHOOSE_LANG_FOR_REMOVE -> chooseLangForRemove(superAdmin, sendMessage, sender, lang);
            case CHOOSE_LANG_FOR_EDIT -> chooseLangForEdit(superAdmin, sendMessage, sender, lang);
            case CHOOSE_LANG_FOR_GET -> chooseLangForGet(superAdmin, sendMessage, sender, lang);
            case CHOOSE_LANG_FOR_SUB_FEEDBACK_ADD -> chooseLangForSubFeedbackAdd(superAdmin, sendMessage, sender, lang);
            case CHOOSE_LANG_FOR_SUB_FEEDBACK_EDIT -> chooseLangForSubFeedbackEdit(superAdmin, sendMessage, sender, lang);
            case CHOOSE_LANG_FOR_SUB_FEEDBACK_REMOVE -> chooseLangForSubFeedbackRemove(superAdmin, sendMessage, sender, lang);
            case CHOOSE_LANG_FOR_SUB_FEEDBACK_GET -> chooseLangForSubFeedbackGet(superAdmin, sendMessage, sender, lang);
            default -> errorMessage(superAdmin, sendMessage);
        }
    }

    private void chooseLangForSubFeedbackGet(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        String getAllSubFeedback = subFeedbackService.getAllSubFeedbackByFeedbackByLang(lang, superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        if (getAllSubFeedback.isEmpty()) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY
                    : ResMessageRu.FEEDBACK_IS_EMPTY;
        } else {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.GET_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG + getAllSubFeedback
                    : ResMessageRu.GET_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG + getAllSubFeedback;
        }
        userService.crudSubFeedbackState(superAdmin);
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.crudSubFeedback());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseLangForSubFeedbackRemove(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        String allSubFeedback = subFeedbackService.getAllSubFeedbackByFeedbackByLang(lang, superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        if (allSubFeedback.isEmpty()) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY
                    : ResMessageRu.FEEDBACK_IS_EMPTY;
        }else {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.CHOOSE_REMOVE_SUB_FEEDBACK + allSubFeedback
                    : ResMessageRu.CHOOSE_REMOVE_SUB_FEEDBACK + allSubFeedback;
        }
        userService.changeStateSubFeedbackRemove(superAdmin);
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.getSubFeedbacksNumber(lang));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseLangForSubFeedbackEdit(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        String allSubFeedback = subFeedbackService.getAllSubFeedbackByFeedbackByLang(lang, superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        if (allSubFeedback.isEmpty()) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY
                    : ResMessageRu.FEEDBACK_IS_EMPTY;
        }else {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.CHOOSE_EDIT_SUB_FEEDBACK + allSubFeedback
                    : ResMessageRu.CHOOSE_EDIT_SUB_FEEDBACK + allSubFeedback;
        }
        userService.changeStateSubFeedbackEdit(superAdmin);
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.getSubFeedbacksNumber(lang));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseLangForSubFeedbackAdd(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        String getAllFeedbacksByLang = feedbackService.getAllFeedbackByLang(lang, superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        if (getAllFeedbacksByLang.isEmpty()) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY
                    : ResMessageRu.FEEDBACK_IS_EMPTY;
        } else {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ADD_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG + getAllFeedbacksByLang
                    : ResMessageRu.ADD_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG + getAllFeedbacksByLang;
            subFeedbackService.saveHalfSubFeedback(superAdmin,lang);
        }
        userService.changeStateAddSubFeedback(superAdmin);
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.getFeedbacksNumber(lang));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseLangForGet(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        String getAllFeedbacksByLang = feedbackService.getAllFeedbackByLang(lang, superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        if (getAllFeedbacksByLang.isEmpty()) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY
                    : ResMessageRu.FEEDBACK_IS_EMPTY;
        } else {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.GET_ALL_FEEDBACK_BY_LANG + getAllFeedbacksByLang
                    : ResMessageRu.GET_ALL_FEEDBACK_BY_LANG + getAllFeedbacksByLang;
        }
        userService.crudFeedbackState(superAdmin);
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.crudFeedback());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseLangForEdit(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        String getAllFeedbacksByLang = feedbackService.getAllFeedbackByLang(lang, superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        if (getAllFeedbacksByLang.isEmpty()) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY + getAllFeedbacksByLang
                    : ResMessageRu.FEEDBACK_IS_EMPTY + getAllFeedbacksByLang;
        } else {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.UPDATE_FEEDBACK + getAllFeedbacksByLang
                    : ResMessageRu.UPDATE_FEEDBACK + getAllFeedbacksByLang;
        }
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.getFeedbacksNumber(lang));
        userService.changeStateUpdateFeedback(superAdmin);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseLangForRemove(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        String getAllFeedbacksByLang = feedbackService.getAllFeedbackByLang(lang, superAdmin);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        if (getAllFeedbacksByLang.isEmpty()) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY
                    : ResMessageRu.FEEDBACK_IS_EMPTY;
        } else {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.REMOVE_FEEDBACK + getAllFeedbacksByLang
                    : ResMessageRu.REMOVE_FEEDBACK + getAllFeedbacksByLang;
            sendMessage.setReplyMarkup(generalService.getFeedbacksNumber(lang));
            userService.changeStateRemoveFeedback(superAdmin);
        }
        sendMessage.setText(msg);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseLangForAdd(BotUser superAdmin, SendMessage sendMessage, AbsSender sender, boolean lang) {
        userService.changeStateAddFeedback(superAdmin, lang);
        String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.ENTER_NEW_FEEDBACK
                : ResMessageRu.ENTER_NEW_FEEDBACK ;
        sendMessage.enableHtml(true);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setText(msg);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void chooseNumber(BotUser superAdmin, String data, SendMessage sendMessage, AbsSender sender) {
        switch (superAdmin.getState()) {
            case ADD_BUILDING_STATE,
                    EDIT_BUILDING_STATE,
                    REMOVE_BUILDING_STATE -> buildingService.getBuildingByName(superAdmin, sendMessage, data, sender);
            case ADD_ADMIN_STATE,
                    EDIT_ADMIN_STATE,
                    REMOVE_ADMIN_STATE -> userService.getAdminByPhoneNumber(superAdmin, sendMessage, data, sender);
            case REMOVE_FEEDBACK_STATE,
                    EDIT_FEEDBACK_STATE,
                    ADD_SUB_FEEDBACK_STATE-> feedbackService.getFeedbackByName(superAdmin, sendMessage, data, sender);
            case GET_ALL_SUB_FEEDBACK -> subFeedbackService.getAllSubFeedbackByFeedback(superAdmin,sendMessage,data,sender);
            case EDIT_SUB_FEEDBACK_STATE -> subFeedbackService.getSubFeedbackByName(superAdmin,sendMessage,data,sender);
            case REMOVE_SUB_FEEDBACK_STATE -> subFeedbackService.removerSubFeedback(superAdmin,sendMessage,data,sender);
            default -> {
                if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                    sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
                else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
                try {
                    sender.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void back(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        sendMessage.setChatId(superAdmin.getChatId());
        userService.back(superAdmin, sendMessage);
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.BACK);
        else {
            sendMessage.setText(ResMessageRu.BACK);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static void errorMessage(BotUser superAdmin, SendMessage sendMessage) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
        else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
    }
}
