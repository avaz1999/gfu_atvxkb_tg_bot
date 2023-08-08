package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.History;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.HistoryRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.FeedbackService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    @Autowired @Lazy
    GeneralService generalService;

    public FeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, HistoryRepository historyRepository) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;

    }


    @Override
    @Transactional
    public void saveFeedback(String data, BotUser user, SendMessage sendMessage) {
        FeedBack feedback = feedBackRepository.findByNameAndDeletedFalse(data);
        if (feedback == null) {
            if (user.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_SERVICE);
            else if (user.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.ERROR_SERVICE);
            sendMessage.setReplyMarkup(generalService.getFeedbacks(user));
        } else {
            List<FeedBack> feedBackList = new ArrayList<>();
            feedBackList.add(feedback);
            user.setFeedBacks(feedBackList);
            user.setState(UserState.GET_SUB_FEEDBACK);
            userRepository.save(user);
            History history = historyRepository.findByUserIdAndFinishedFalseAndDeletedFalse(user.getId());
            history.setFeedbackId(feedback.getId());
            historyRepository.save(history);
            sendMessage.setChatId(user.getChatId().toString());
            if (user.getLanguage().equals(BotQuery.UZ_SELECT))sendMessage.setText(ResMessageUz.CHOOSE_SERVICE);
            else if (user.getLanguage().equals(BotQuery.RU_SELECT))sendMessage.setText(ResMessageRu.CHOOSE_SERVICE);
            sendMessage.setReplyMarkup(generalService.getSubFeedbacks(data));
        }
    }

    public List<FeedBack> getAllFeedbackUZ() {
        return feedBackRepository.findAllByLangFalseAndDeletedFalse();
    }

    public List<FeedBack> getAllFeedbackRU() {
        return feedBackRepository.findAllByLangTrueAndDeletedFalse();
    }

    @Override
    public String showFeedback(BotUser currentUser) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(currentUser.getChatId());
        for (FeedBack feedBack : feedBackRepository.findAll()) {
//                if (Objects.equals(feedBack.getId(),))
        }
        return null;
    }
}
