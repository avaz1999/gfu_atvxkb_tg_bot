package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface SuperAdminService {
    void superAdminHasMessage(BotUser superAdmin, Message message, String text, SendMessage sendMessage);
    void superAdminHasCallBackQuery(BotUser superAdmin, CallbackQuery callbackQuery);
}
