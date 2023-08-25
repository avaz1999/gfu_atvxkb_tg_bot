package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface AdminService {
    void adminHasMessage(BotUser admin, String message, SendMessage sendMessage,AbsSender sender);
    void shareAdminMessage(String message,BotUser admin,SendMessage sendMessage,AbsSender sender);

    void callAdminService(BotUser currentUser, Message message, SendMessage sendMessage, AbsSender sender);

    void callAdminHasCallBackQuery(BotUser currentUser, CallbackQuery callbackQuery, AbsSender sender);
}
