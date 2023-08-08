package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.SuperAdminService;
@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    private final GeneralService generalService;

    public SuperAdminServiceImpl(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Override
    public void superAdminHasMessage(BotUser superAdmin, Message message, SendMessage sendMessage, AbsSender sender) {
        sendMessage.enableHtml(true);
        switch (superAdmin.getState()) {
            case SETTING -> superAdminStateSetting(message,superAdmin,sendMessage,sender);
        }
    }

    private void superAdminStateSetting(Message message, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()){
            String text = message.getText();
            if (text.equalsIgnoreCase(BotQuery.SETTING )){
                if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) {
                    sendMessage.setText(ResMessageUz.CHOOSE_SERVICE);
                    sendMessage.setReplyMarkup(generalService.getSettingForSuperAdmin(superAdmin));
                }
            }
        }
    }

    @Override
    public void superAdminHasCallBackQuery(BotUser superAdmin, CallbackQuery callbackQuery) {

    }
}
