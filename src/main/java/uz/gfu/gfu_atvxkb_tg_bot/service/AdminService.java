package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface AdminService {
    void adminHasMessage(BotUser admin, Message message, String text, SendMessage sendMessage);
    void adminHasCallBackQuery(BotUser admin, CallbackQuery callbackQuery);
}
