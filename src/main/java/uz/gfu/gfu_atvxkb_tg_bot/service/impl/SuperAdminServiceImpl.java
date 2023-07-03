package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.service.SuperAdminService;
@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    @Override
    public void superAdminHasMessage(BotUser superAdmin, Message message, String text, SendMessage sendMessage) {

    }

    @Override
    public void superAdminHasCallBackQuery(BotUser superAdmin, CallbackQuery callbackQuery) {

    }
}
