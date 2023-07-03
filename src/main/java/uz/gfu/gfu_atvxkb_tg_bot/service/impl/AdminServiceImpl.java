package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.service.AdminService;
@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public void adminHasMessage(BotUser admin, Message message, String text, SendMessage sendMessage) {

    }

    @Override
    public void adminHasCallBackQuery(BotUser admin, CallbackQuery callbackQuery) {

    }
}
