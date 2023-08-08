package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface FeedbackService {



    void saveFeedback(String data, BotUser user, SendMessage sendMessage);

    String showFeedback(BotUser currentUser);

}
