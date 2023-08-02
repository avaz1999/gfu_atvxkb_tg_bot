package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface AdminService {
    void adminHasMessage(BotUser admin, Message message, SendMessage sendMessage);
    void adminHasCallBackQuery(BotUser admin, CallbackQuery callbackQuery);
    void shareAdminMessage(String message,Long chatId,SendMessage sendMessage);

    void callAdminService(BotUser currentUser, Message message, SendMessage sendMessage, AbsSender sender);
}
