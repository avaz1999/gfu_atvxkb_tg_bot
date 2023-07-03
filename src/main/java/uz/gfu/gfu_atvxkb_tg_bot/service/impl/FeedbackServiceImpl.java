package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.dto.FeedbackDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
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
    public  List<FeedbackDto> getAllFeedback() {
        List<FeedbackDto> feedbackDtoList = new ArrayList<>();
        FeedbackDto feedbackDto = new FeedbackDto();
        List<FeedBack> allFeedbacks = feedBackRepository.findAll();
        for (int i = 0; i < allFeedbacks.size(); i++) {
            FeedBack feedBack = allFeedbacks.get(i);
            feedbackDto.setId(feedBack.getId());
            feedbackDto.setTitle(feedBack.getSubTitle());
            feedbackDto.setMessage(feedBack.getMessages());
            feedbackDtoList.add(feedbackDto);
        }
        return feedbackDtoList;
    }
    @Override
    public List<FeedBack> getAllFeedbacks(){
        return feedBackRepository.findAll();
    }

    @Override
    public String addFeedback(FeedbackDto feedbackDto) {
        FeedBack feedBack = new FeedBack();
        feedBack.setSubTitle(feedbackDto.getTitle());
        feedBack.setMessages(feedbackDto.getMessage());
        feedBackRepository.save(feedBack);
        return "Successfully added";
    }

    @Override
    public void saveFeedback(String data, BotUser user) {
        FeedBack feedBackBySubtitle = feedBackRepository.findBySubTitle(data);
        FeedBack feedBack = new FeedBack();
        List<FeedBack> feedBackList = new ArrayList<>();
        feedBack.setSubTitle(feedBackBySubtitle.getSubTitle());
        feedBack.setMessages(feedBackBySubtitle.getMessages());
        feedBackList.add(feedBack);
        user.setFeedBacks(feedBackList);
        userRepository.save(user);
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
