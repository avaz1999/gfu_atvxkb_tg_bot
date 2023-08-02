package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.History;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.HistoryRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.FeedbackService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    public FeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, HistoryRepository historyRepository) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }


    @Override
    @Transactional
    public void saveFeedback(String data, BotUser user) {
        FeedBack feedback = feedBackRepository.findByNameAndDeletedFalse(data);
        List<FeedBack> feedBackList = new ArrayList<>();
        feedBackList.add(feedback);
        user.setFeedBacks(feedBackList);
        user.setState(UserState.GET_SUB_FEEDBACK);
        userRepository.save(user);
        History history = historyRepository.findByUserIdAndFinishedFalseAndDeletedFalse(user.getId());
        history.setFeedbackId(feedback.getId());
        historyRepository.save(history);
    }

    public List<FeedBack> getAllFeedback(){
        return feedBackRepository.findAllByDeletedFalse();
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
