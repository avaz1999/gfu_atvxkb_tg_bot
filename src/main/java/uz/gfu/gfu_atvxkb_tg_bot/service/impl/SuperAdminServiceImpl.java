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
import uz.gfu.gfu_atvxkb_tg_bot.service.BuildingService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.SuperAdminService;
import uz.gfu.gfu_atvxkb_tg_bot.service.UserService;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    private final GeneralService generalService;
    private final UserService userService;
    private final BuildingService buildingService;

    public SuperAdminServiceImpl(GeneralService generalService, UserService userService, BuildingService buildingService) {
        this.generalService = generalService;
        this.userService = userService;
        this.buildingService = buildingService;
    }

    @Override
    public void superAdminHasMessage(BotUser superAdmin, Message message, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        switch (superAdmin.getState()) {
            case SETTING -> superAdminStateSetting(message, superAdmin, sendMessage, sender);
            case SUPER_ADMIN_CRUD -> crudSuperAdminState(message, superAdmin, sendMessage, sender);
            case SUPER_ADMIN_BUILDING -> crudBuilding(message, superAdmin, sendMessage, sender);
            case ADD_BUILDING_STATE -> createNewBuilding(message, superAdmin, sendMessage, sender);
            case EDIT_BUILDING_STATE -> editBuilding(message,superAdmin, sendMessage, sender);
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
                case BotQuery.ADD_BUILDING -> addBuilding(superAdmin, sendMessage, sender);
                case BotQuery.REMOVE_BUILDING -> removeBuilding(message, superAdmin, sendMessage, sender);
                case BotQuery.UPDATE_BUILDING -> updateBuilding( superAdmin, sendMessage, sender);
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

    }

    private void updateBuilding( BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
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

    private void removeBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
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
            crudBuilding(message,superAdmin,sendMessage,sender);
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

    private void editBuilding(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()){
            String text = message.getText();
            if (text.equals(BotQuery.BACK)){
                userService.back(superAdmin);
                sendMessage.setText(ResMessageUz.BUILDING_CRUD);
                sendMessage.setReplyMarkup(generalService.crudBuilding());
            } else if (text.equals(BotQuery.UPDATE_BUILDING)) {
                String getBuildings = buildingService.getDtoBuildings(superAdmin);
                userService.changeStateEditBuilding(superAdmin);
                sendMessage.setText(ResMessageUz.EDIT_BUILDING + getBuildings);
                sendMessage.setReplyMarkup(generalService.getBuildingNumber());
            }
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
        switch (data){
            case BotQuery.BACK -> back(superAdmin,sendMessage,sender);
        }
    }

    private void back(BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        userService.back(superAdmin);
        sendMessage.setText(ResMessageUz.SUCCESS_ADD_BUILDING);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setReplyMarkup(generalService.getBack(superAdmin));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
