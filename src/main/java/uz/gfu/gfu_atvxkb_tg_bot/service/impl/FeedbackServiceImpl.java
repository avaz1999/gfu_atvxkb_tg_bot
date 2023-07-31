package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.dto.FeedbackDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.FeedbackService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;

    public FeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void saveFeedback(String data, BotUser user) {
        FeedBack feedBackBySubtitle = feedBackRepository.findByNameAndDeletedFalse(data);
        List<FeedBack> feedBackList = new ArrayList<>();
        feedBackList.add(feedBackBySubtitle);
        user.setFeedBacks(feedBackList);
        user.setState(UserState.GET_SUB_FEEDBACK);
        userRepository.save(user);
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
