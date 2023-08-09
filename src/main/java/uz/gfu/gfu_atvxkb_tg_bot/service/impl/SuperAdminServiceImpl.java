package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.SuperAdminService;
import uz.gfu.gfu_atvxkb_tg_bot.service.UserService;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    private final GeneralService generalService;
    private final UserService userService;

    public SuperAdminServiceImpl(GeneralService generalService, UserService userService) {
        this.generalService = generalService;
        this.userService = userService;
    }

    @Override
    public void superAdminHasMessage(BotUser superAdmin, Message message, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        switch (superAdmin.getState()) {
            case SETTING -> superAdminStateSetting(message, superAdmin, sendMessage, sender);
            case SUPER_ADMIN_CRUD -> crudSuperAdminState(message, superAdmin, sendMessage, sender);
            case SUPER_ADMIN_BUILDING -> crudBuilding(message, superAdmin, sendMessage, sender);
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
                if (!text.equalsIgnoreCase(BotQuery.SETTING)){
                    crudSuperAdminState(message,superAdmin,sendMessage,sender);
                }else {
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
        userService.changeStateBuilding(superAdmin);
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
        userService.changeStateBuilding(superAdmin);
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
        userService.changeStateBuilding(superAdmin);
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
                case BotQuery.ADD_BUILDING -> addBuilding(message, superAdmin, sendMessage, sender);
                case BotQuery.REMOVE_BUILDING -> removeBuilding(message, superAdmin, sendMessage, sender);
                case BotQuery.UPDATE_BUILDING -> updateBuilding(message, superAdmin, sendMessage, sender);
                case BotQuery.ALL_BUILDING -> allBuilding(message, superAdmin, sendMessage, sender);
                case BotQuery.MENU -> menu(message, superAdmin, sendMessage, sender);

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

    private void menu(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
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

    private void allBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
    }

    private void updateBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
    }

    private void removeBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
    }

    private void addBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {

    }

    @Override
    public void superAdminHasCallBackQuery(BotUser superAdmin, CallbackQuery callbackQuery) {

    }
}
