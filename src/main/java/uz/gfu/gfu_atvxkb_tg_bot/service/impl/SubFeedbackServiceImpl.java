package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import uz.gfu.gfu_atvxkb_tg_bot.dto.SubFeedDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.History;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.HistoryRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.SubFeedbackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.SubFeedbackService;

import java.util.List;
import java.util.Optional;

@Service
public class SubFeedbackServiceImpl implements SubFeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final SubFeedbackRepository subFeedbackRepository;
    private final HistoryRepository historyRepository;

    public SubFeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, SubFeedbackRepository subFeedbackRepository, HistoryRepository historyRepository) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.subFeedbackRepository = subFeedbackRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public String addFeedback(SubFeedDto dto) {
        if (dto.getFeedbackId() == null || dto.getName() == null) {
            return "Object is null";
        }
        Optional<FeedBack> feedBackById = feedBackRepository.findById(dto.getFeedbackId());
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
        FeedBack byName = feedBackRepository.findByNameAndDeletedFalse(feedback);
        if (byName != null) {
            return subFeedbackRepository.findAllByFeedBackIdAndDeletedFalse(byName.getId());
        } else return null;
    }

    @Override
    public void saveSubFeedback(String data, BotUser client) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(client.getChatId());
        if (user != null) {
            SubFeedback subFeedback = subFeedbackRepository.findByNameAndDeletedFalse(data);
            if (subFeedback != null) {
                History history = historyRepository.findByUserIdAndFinishedFalseAndDeletedFalse(client.getId());
                if (history != null) {
                    history.setSubFeedbackId(subFeedback.getId());
                    historyRepository.save(history);
                    user.setState(UserState.SAVE_SUB_FEEDBACK);
                    userRepository.save(user);
                }
            }
        }
    }
}
