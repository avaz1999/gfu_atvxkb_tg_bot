package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.dto.SubFeedDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;
import uz.gfu.gfu_atvxkb_tg_bot.enums.State;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.ApplicationRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.SubFeedbackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.SubFeedbackService;
import uz.gfu.gfu_atvxkb_tg_bot.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class SubFeedbackServiceImpl implements SubFeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final SubFeedbackRepository subFeedbackRepository;
    private final ApplicationRepository applicationRepository;
    @Autowired
    @Lazy
    UserService userService;
    @Autowired
    @Lazy
    GeneralService generalService;

    public SubFeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, SubFeedbackRepository subFeedbackRepository, ApplicationRepository applicationRepository) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.subFeedbackRepository = subFeedbackRepository;
        this.applicationRepository = applicationRepository;
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
    public void saveSubFeedback(String data, BotUser client, SendMessage sendMessage) {
        SubFeedback subFeedback = subFeedbackRepository.findByNameAndDeletedFalse(data);
        if (subFeedback == null) {
            if (client.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else if (client.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        else {
            Application application = applicationRepository.findByUserIdAndDoneAndDeletedFalse(client.getId(),State.CREATED);
            if (application != null) {
                application.setSubFeedbackName(subFeedback.getName());
                application.setDone(State.CREATED);
                applicationRepository.save(application);
                client.setState(UserState.SAVE_SUB_FEEDBACK);
                userRepository.save(client);
                if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
                    sendMessage.setText(ResMessageUz.DONE_SERVICE + userService.clientShowFeedback(client));
                    sendMessage.setReplyMarkup(generalService.getRegisterDone(client));
                } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
                    sendMessage.setText(ResMessageRu.DONE_SERVICE + userService.clientShowFeedback(client));
                    sendMessage.setReplyMarkup(generalService.getRegisterDone(client));
                }
            }
        }
    }

    @Override
    public void addSubFeedback(String text, BotUser superAdmin, SendMessage sendMessage) {
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        SubFeedback subFeedback = subFeedbackRepository.findNameNull();
        subFeedback.setName(text);
        subFeedback.setCreatedBy(superAdmin.getId());
        subFeedback.setLang(false);
        subFeedbackRepository.save(subFeedback);
        superAdmin.setState(UserState.CRUD_SUB_FEEDBACK);
        sendMessage.setReplyMarkup(generalService.crudSubFeedback());
        userRepository.save(superAdmin);
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.SUCCESS_ADD_SUB_FEEDBACK);
            else sendMessage.setText(ResMessageRu.SUCCESS_ADD_SUB_FEEDBACK);
    }
}
