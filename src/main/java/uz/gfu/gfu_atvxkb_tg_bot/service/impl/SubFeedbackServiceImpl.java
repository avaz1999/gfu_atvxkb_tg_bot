package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.dto.SubFeedDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.UserData;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.SubFeedbackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserDataRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.SubFeedbackService;

import java.util.List;
import java.util.Optional;

@Service
public class SubFeedbackServiceImpl implements SubFeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final SubFeedbackRepository subFeedbackRepository;
    private final UserDataRepository userDataRepository;

    public SubFeedbackServiceImpl(FeedBackRepository feedBackRepository, SubFeedbackRepository subFeedbackRepository, UserDataRepository userDataRepository) {
        this.feedBackRepository = feedBackRepository;
        this.subFeedbackRepository = subFeedbackRepository;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public String addFeedback(SubFeedDto dto) {
        if (dto.getFeedbackId() == null || dto.getName() == null){
            return "Object is null";
        }
        Optional<FeedBack> feedBackById= feedBackRepository.findById(dto.getFeedbackId());
        if (feedBackById.isEmpty()) {
            return "Object not found";
        }
        FeedBack feedBack = feedBackById.get();
        SubFeedback subFeedback = new SubFeedback();
        subFeedback.setName(dto.getName());
        subFeedback.setFeedBack(feedBack);
        subFeedbackRepository.save(subFeedback);
        return "Added Successfully";
    }

    @Override
    public List<SubFeedback> findAllFeedback(String feedback) {
        FeedBack byName = feedBackRepository.findByName(feedback);
        return subFeedbackRepository.findAllByFeedBackId(byName.getId());
    }

    @Override
    public void saveSubFeedback(String data, BotUser client) {
        SubFeedback byName = subFeedbackRepository.findByName(data);
        UserData byUserId = userDataRepository.findByUserId(client.getId());
        byUserId.setSubFeedbackId(byName.getId());
        userDataRepository.save(byUserId);
    }
}
