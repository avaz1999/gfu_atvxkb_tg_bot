package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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
import uz.gfu.gfu_atvxkb_tg_bot.service.FeedbackService;
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
    private final FeedbackService feedbackService;
    @Autowired
    @Lazy
    UserService userService;
    @Autowired
    @Lazy
    GeneralService generalService;

    public SubFeedbackServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, SubFeedbackRepository subFeedbackRepository, ApplicationRepository applicationRepository, FeedbackService feedbackService) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.subFeedbackRepository = subFeedbackRepository;
        this.applicationRepository = applicationRepository;
        this.feedbackService = feedbackService;
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
        SubFeedback subFeedback = subFeedbackRepository.findTopByNameAndDeletedFalseOrderByCreatedAt(data);
        if (subFeedback == null) {
            errorMessage(client, sendMessage);
        } else {
            if (applicationRepository.
                    existsBySubFeedbackNameAndDoneAndUserIdAndDeletedFalse(data, State.CREATED, client.getId())) {
                String waiting = client.getLanguage().equals(BotQuery.UZ_SELECT)
                        ? ResMessageUz.WAITING
                        : ResMessageRu.WAITING;
                client.setState(UserState.GET_FEEDBACK);
                userRepository.save(client);

                Application application = applicationRepository.
                        findTopByUserIdAndDoneAndDeletedFalseAndSubFeedbackNameIsNullOrderByCreatedAtDesc(client.getId(), State.CREATED);
                applicationRepository.delete(application);
                sendMessage.setText(waiting);
                sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
            } else {
                Application application =
                        applicationRepository.
                                findByUserIdAndDoneAndDeletedFalseAndSubFeedbackName(client.getId(), State.CREATED, null);
                if (application == null) {
                    errorMessage(client, sendMessage);
                } else {
                    saveApplication(client, sendMessage, application, subFeedback);
                }
            }
        }
    }

    private void saveApplication(BotUser client, SendMessage sendMessage, Application application, SubFeedback subFeedback) {
        application.setSubFeedbackName(subFeedback.getName());
        Application saveApplication = applicationRepository.save(application);
        client.setState(UserState.SAVE_SUB_FEEDBACK);
        userRepository.save(client);
        String doneService = client.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.DONE_SERVICE + userService.clientShowFeedback(client, saveApplication)
                : ResMessageRu.DONE_SERVICE + userService.clientShowFeedback(client, saveApplication);
        sendMessage.setText(doneService);
        sendMessage.setReplyMarkup(generalService.getRegisterDone(client,application));
    }

    @Override
    public void addSubFeedback(String text, BotUser superAdmin, SendMessage sendMessage) {
        SubFeedback dbSubFeedback = subFeedbackRepository.findByNameAndDeletedFalse(text);
        if (dbSubFeedback != null) {
            String msg = "";
             msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.WRONG_SUB_FEEDBACK
                    : ResMessageRu.WRONG_SUB_FEEDBACK;
            sendMessage.setChatId(superAdmin.getChatId());
            sendMessage.setText(msg);
        }else {
            SubFeedback subFeedback = subFeedbackRepository.findNameNull();
            sendMessage.enableHtml(true);
            sendMessage.setChatId(superAdmin.getChatId());
            if(subFeedback == null){
                String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                        ? ResMessageUz.ERROR_MESSAGE
                        : ResMessageRu.ERROR_MESSAGE;
                sendMessage.setText(msg);
            }else {
                subFeedback.setName(text);
                subFeedback.setCreatedBy(superAdmin.getId());
                subFeedbackRepository.save(subFeedback);
                superAdmin.setState(UserState.CRUD_SUB_FEEDBACK);
                sendMessage.setReplyMarkup(generalService.crudSubFeedback());
                userRepository.save(superAdmin);
                String successAddSubFeedback = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                        ? ResMessageUz.SUCCESS_ADD_SUB_FEEDBACK
                        : ResMessageRu.SUCCESS_ADD_SUB_FEEDBACK;
                sendMessage.setText(successAddSubFeedback);
            }
        }
    }

    @Override
    public void getAllSubFeedbackByFeedback(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender) {
        FeedBack feedback = feedBackRepository.findByNameAndDeletedFalse(data);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        String msg = "";
        for (SubFeedback subFeedback : subFeedbackRepository.findAllByFeedBackAndDeletedFalseOrderByCreatedAt(feedback)) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? "<b>ID: </b>" + subFeedback.getId() +
                    "<b>NOMI: </b>" + subFeedback.getName() +
                    "<b>SERVICE TURI: </b>" + feedback.getName() +
                    "________________________________________________________"
                    : "<b>ИД: </b>" + subFeedback.getId() +
                    "<b>ИМЯ: </b>" + subFeedback.getName() +
                    "<b>ТИП ОБСЛУЖИВАНИЯ: </b>" + feedback.getName() +
                    "________________________________________________________";
            sendMessage.setText(msg);
        }
    }

    @Override
    public String getAllSubFeedbackByFeedbackByLang(boolean lang, BotUser superAdmin) {
        StringBuilder msg = new StringBuilder();
        for (SubFeedback subFeedback : subFeedbackRepository.findAllByLangAndDeletedFalse(lang)) {
            msg.append(superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? "ID: " + subFeedback.getId() + "\n" +
                    "НОМИ: " + subFeedback.getName() + "\n" +
                    "Сервис Тури: " + subFeedback.getFeedBack().getName() + "\n" +
                    "_______________________________________________\n"
                    : "ID: " + subFeedback.getId() + "\n" +
                    "ИМЯ: " + subFeedback.getName() + "\n" +
                    "Тип Обслуживания" + subFeedback.getFeedBack().getName() + "\n" +
                    "_______________________________________________\n"
            );
        }
        return msg.toString();
    }

    @Override
    public List<SubFeedback> getAllSubFeedbackByLang(boolean lang) {
        return subFeedbackRepository.findAllByLangAndDeletedFalse(lang);
    }

    @Override
    public void getSubFeedbackByName(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender) {
        SubFeedback subFeedback = subFeedbackRepository.findByNameAndDeletedFalse(data);
        sendMessage.enableHtml(true);
        String msg = "";
        if (subFeedback == null) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.FEEDBACK_IS_EMPTY
                    : ResMessageRu.FEEDBACK_IS_EMPTY;
        } else {
            subFeedback.setEdited(true);
            subFeedbackRepository.save(subFeedback);
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ENTER_NEW_SUB_FEEDBACK
                    : ResMessageRu.ENTER_NEW_SUB_FEEDBACK;
        }
        userService.changeStateSubFeedbackEdit1(superAdmin);
        sendMessage.setText(msg);
        sendMessage.setChatId(superAdmin.getChatId());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static void errorMessage(BotUser superAdmin, SendMessage sendMessage) {
        String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.ERROR_MESSAGE
                : ResMessageRu.ERROR_MESSAGE;
        sendMessage.setText(msg);
    }

    @Override
    public void editSubFeedback(String text, BotUser superAdmin, SendMessage sendMessage) {
        SubFeedback subFeedback = subFeedbackRepository.findByEditedTrueAndDeletedFalse();
        String msg = "";
        if (subFeedback == null) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ERROR_MESSAGE
                    : ResMessageRu.ERROR_MESSAGE;
        } else if (subFeedbackRepository.existsByNameAndDeletedFalse(text)) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.EXIST_SUB_FEEDBACK
                    : ResMessageRu.EXIST_SUB_FEEDBACK;
        } else {
            subFeedback.setName(text);
            subFeedback.setModifiedBy(superAdmin.getId());
            subFeedbackRepository.save(subFeedback);
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.SUCCESS_EDITED
                    : ResMessageRu.SUCCESS_EDITED;
            sendMessage.setChatId(superAdmin.getChatId());
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.crudSubFeedback());
        }
    }

    @Override
    public void saveHalfSubFeedback(BotUser superAdmin, boolean lang) {
        SubFeedback subFeedback = new SubFeedback();
        subFeedback.setLang(lang);
        subFeedback.setCreatedBy(superAdmin.getId());
        subFeedbackRepository.save(subFeedback);
    }

    @Override
    public void removerSubFeedback(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender) {
        SubFeedback subFeedback = subFeedbackRepository.findByNameAndDeletedFalse(data);
        String msg = "";
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.enableHtml(true);
        if (subFeedback == null) {
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ERROR_MESSAGE
                    : ResMessageRu.ERROR_MESSAGE;
        } else {
            subFeedback.setDeleted(true);
            subFeedback.setDeletedBy(superAdmin.getId());
            subFeedbackRepository.save(subFeedback);
            userService.changeStateSubFeedback(superAdmin);
            msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.DELETED_SUCCESS
                    : ResMessageRu.DELETED_SUCCESS;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.crudSubFeedback());
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clientEditSubFeedback(String data, BotUser client) {
        SubFeedback subFeedback = subFeedbackRepository.findByNameAndDeletedFalse(data);
        subFeedbackRepository.delete(subFeedback);
        client.setState(UserState.GET_FEEDBACK);
        userRepository.save(client);
    }
}
