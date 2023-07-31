package uz.gfu.gfu_atvxkb_tg_bot.service;

import uz.gfu.gfu_atvxkb_tg_bot.dto.SubFeedDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;

import java.util.List;

public interface SubFeedbackService {
    String addFeedback(SubFeedDto dto);
    List<SubFeedback> findAllFeedback(String feedback);

    void saveSubFeedback(String data, BotUser client);
}
