package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.dto.FeedbackDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
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
import uz.gfu.gfu_atvxkb_tg_bot.service.FeedbackService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    @Autowired
    @Lazy
    GeneralService generalService;
    private final SubFeedbackRepository subFeedbackRepository;

    public FeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, ApplicationRepository historyRepository, SubFeedbackRepository subFeedbackRepository) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.applicationRepository = historyRepository;

        this.subFeedbackRepository = subFeedbackRepository;
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
            Application application = new Application();
            application.setUserId(user.getId());
            application.setBuildingName(user.getBuilding().getName());
            application.setDepartmentId(user.getDepartment().getId());
            application.setFeedbackName(feedback.getName());
            application.setDone(State.CREATED);
            applicationRepository.save(application);
            sendMessage.setChatId(user.getChatId().toString());
            if (user.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.CHOOSE_SERVICE);
            else if (user.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.CHOOSE_SERVICE);
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

    @Override
    public void createNewFeedback(String text, BotUser superAdmin, SendMessage sendMessage) {
        sendMessage.enableHtml(true);
        sendMessage.setChatId(superAdmin.getChatId());
        if (feedBackRepository.existsByNameAndDeletedFalse(text)) {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.EXIST_FEEDBACK_NAME);
            else sendMessage.setText(ResMessageRu.EXIST_FEEDBACK_NAME);
        } else {
            FeedBack feedBack = new FeedBack();
            feedBack.setName(text);
            feedBackRepository.save(feedBack);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.PLEASE_ENTER_RUSSIAN);
            else sendMessage.setText(ResMessageRu.PLEASE_ENTER_RUSSIAN);
            superAdmin.setState(UserState.ADD_FEEDBACK_STATE_RUS);
            userRepository.save(superAdmin);
        }
    }

    @Override
    public void createNewFeedbackRus(String text, BotUser superAdmin, SendMessage sendMessage) {
        sendMessage.enableHtml(true);
        sendMessage.setChatId(superAdmin.getChatId());
        if (feedBackRepository.existsByNameAndDeletedFalse(text)) {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.EXIST_FEEDBACK_NAME);
            else sendMessage.setText(ResMessageRu.EXIST_FEEDBACK_NAME);
        } else {
            FeedBack feedBack = new FeedBack();
            feedBack.setLang(true);
            feedBack.setName(text);
            feedBackRepository.save(feedBack);
            superAdmin.setState(UserState.CRUD_FEEDBACK);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.SUCCESS_ADD_FEEDBACK);
            else sendMessage.setText(ResMessageRu.SUCCESS_ADD_FEEDBACK);
            sendMessage.setReplyMarkup(generalService.crudFeedback());
        }
    }

    @Override
    public String getDtoFeedback(BotUser superAdmin, SendMessage sendMessage) {
        List<FeedBack> allFeedback = feedBackRepository.findAllByDeletedFalse();
        List<FeedbackDto> feedbackDtoList = new ArrayList<>();
        for (FeedBack feedBack : allFeedback) {
            FeedbackDto dto = new FeedbackDto();
            dto.setName(feedBack.getName());
            dto.setId(feedBack.getId());
            dto.setLang(feedBack.isLang());
            feedbackDtoList.add(dto);
        }
        StringBuilder sb = new StringBuilder();
        String lang = "";
        byte i = 0;
        for (FeedbackDto dto : feedbackDtoList) {
            if (dto.isLang()) lang = "РУССКИЙ";
            else lang = "O'ZBEKCHA";
            sb.append(superAdmin.getLanguage().equals(BotQuery.UZ_SELECT) ? "<b>" + i + 1 + "\n\n \uD83D\uDD28 SERVICE: \n" +
                    "ID: " + dto.getId() + "\n" +
                    "NOMI:  " + dto.getName() + "\n" +
                    "TILI: " + lang + "\n" +
                    "</b>" : " " +
                    "<b>\uD83D\uDD28 SERVICE: \n" +
                    "ИД: " + dto.getId() + "\n" +
                    "ИМЯ: " + dto.getName() + "\n" +
                    "ЯЗЫК: " + lang+ "\n</b>");
            i++;
        }
        return sb.toString();
    }

    @Override
    public List<FeedBack> getAllFeedback() {
        return feedBackRepository.findAllByDeletedFalse();
    }

    @Override
    public void getFeedbackByName(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender) {
        FeedBack feedback = feedBackRepository.findByNameAndDeletedFalse(data);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        if (feedback == null){
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        else if (superAdmin.getState().equals(UserState.REMOVE_FEEDBACK_STATE)){
            feedback.setDeleted(true);
            feedback.setDeletedBy(superAdmin.getId());
            feedBackRepository.save(feedback);
            superAdmin.setState(UserState.CRUD_FEEDBACK);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.DELETED_SUCCESS);
            else sendMessage.setText(ResMessageRu.DELETED_SUCCESS);
            sendMessage.setReplyMarkup(generalService.crudFeedback());
        }else if (superAdmin.getState().equals(UserState.EDIT_FEEDBACK_STATE)){
            feedback.setEdited(true);
            feedBackRepository.save(feedback);
            superAdmin.setState(UserState.EDIT_FEEDBACK_STATE_1);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ENTER_NEW_FEEDBACK);
            else sendMessage.setText(ResMessageRu.ENTER_NEW_FEEDBACK);
        } else if (superAdmin.getState().equals(UserState.ADD_SUB_FEEDBACK_STATE)) {
            SubFeedback subFeedback = new SubFeedback();
            subFeedback.setFeedBack(feedback);
            subFeedbackRepository.save(subFeedback);
            superAdmin.setState(UserState.ADD_SUB_FEEDBACK_STATE_1);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ENTER_NEW_SUB_FEEDBACK);
            else sendMessage.setText(ResMessageRu.ENTER_NEW_SUB_FEEDBACK);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editFeedback(String text, SendMessage sendMessage, BotUser superAdmin) {
        FeedBack feedback = feedBackRepository.findByEditedTrueAndDeletedFalse();
        if (feedback == null) {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }else {
            feedback.setName(text);
            feedback.setDeleted(false);
            feedback.setModifiedBy(superAdmin.getId());
            feedBackRepository.save(feedback);
            superAdmin.setState(UserState.CRUD_FEEDBACK);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.SUCCESS_EDITED);
            else sendMessage.setText(ResMessageRu.SUCCESS_EDITED);
            sendMessage.setReplyMarkup(generalService.crudFeedback());
        }
    }
}
