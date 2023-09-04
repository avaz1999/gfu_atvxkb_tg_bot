package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.dto.SubFeedDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;

import java.util.List;

public interface SubFeedbackService {
    String addFeedback(SubFeedDto dto);
    List<SubFeedback> findAllFeedback(String feedback);

    void saveSubFeedback(String data, BotUser client, SendMessage sendMessage);

    void addSubFeedback(String text, BotUser superAdmin, SendMessage sendMessage);

    void getAllSubFeedbackByFeedback(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender);

    String getAllSubFeedbackByFeedbackByLang(boolean lang, BotUser superAdmin);
}
