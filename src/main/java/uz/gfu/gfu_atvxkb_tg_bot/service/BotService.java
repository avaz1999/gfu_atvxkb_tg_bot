package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface BotService {
    void updateReceived(Update update, AbsSender sender);
    void updateHasMessage(Update update,AbsSender sender);

    void updateHasCallBackQuery(Update update,AbsSender sender);

}
