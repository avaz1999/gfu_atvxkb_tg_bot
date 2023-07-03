package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface BotService {
    void updateReceived(Update update);
    void updateHasMessage(Update update);

    void updateHasCallBackQuery(Update update);

}
