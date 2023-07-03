package uz.gfu.gfu_atvxkb_tg_bot.service;

import uz.gfu.gfu_atvxkb_tg_bot.dto.FeedbackDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

import java.util.List;

public interface FeedbackService {
    List<FeedbackDto> getAllFeedback();
    List<FeedBack> getAllFeedbacks();

    String addFeedback(FeedbackDto feedbackDto);

    void saveFeedback(String data, BotUser user);

    String showFeedback(BotUser currentUser);
}
