package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.UserData;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserDataRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.FeedbackService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;

    public FeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, UserDataRepository userDataRepository) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public void saveFeedback(String data, BotUser user) {
        UserData userData = new UserData();
        FeedBack feedBackBySubtitle = feedBackRepository.findByName(data);
        userData.setFeedbackId(feedBackBySubtitle.getId());
        userData.setUserId(user.getId());
        List<FeedBack> feedBackList = new ArrayList<>();
        feedBackList.add(feedBackBySubtitle);
        user.setFeedBacks(feedBackList);
        userDataRepository.save(userData);
        userRepository.save(user);
    }

    public List<FeedBack> getAllFeedback(){
        return feedBackRepository.findAll();
    }
    @Override
    public String showFeedback(BotUser currentUser) {
        Optional<BotUser> byId = userRepository.findById(currentUser.getId());
        if (byId.isPresent()) {
            BotUser user = byId.get();
            for (FeedBack feedBack : feedBackRepository.findAll()) {
//                if (Objects.equals(feedBack.getId(),))
            }
        }
        return null;
    }
}
