package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;

import java.util.List;

public interface FeedbackService {



    void saveFeedback(String data, BotUser user, SendMessage sendMessage);

    String showFeedback(BotUser currentUser);

    void createNewFeedback(String text, BotUser superAdmin, SendMessage sendMessage);

    void createNewFeedbackRus(String text, BotUser superAdmin, SendMessage sendMessage);

    String getDtoFeedback(BotUser superAdmin, SendMessage sendMessage);

    List<FeedBack> getAllFeedback();

    void getFeedbackByName(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender);

    void editFeedback(String text, SendMessage sendMessage, BotUser superAdmin);
}
