package uz.gfu.gfu_atvxkb_tg_bot.service.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.*;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.repository.*;
import uz.gfu.gfu_atvxkb_tg_bot.enums.Role;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.UserService;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FeedBackRepository feedBackRepository;
    private final SubFeedbackRepository subFeedbackRepository;
    private final DepartmentRepository departmentRepository;
    private final BuildingRepository buildingRepository;
    private final HistoryRepository historyRepository;
    @Lazy
    private final GeneralService generalService;

    public UserServiceImpl(UserRepository userRepository, FeedBackRepository feedBackRepository, SubFeedbackRepository subFeedbackRepository, DepartmentRepository departmentRepository, BuildingRepository buildingRepository, HistoryRepository historyRepository, GeneralService generalService) {
        this.userRepository = userRepository;
        this.feedBackRepository = feedBackRepository;
        this.subFeedbackRepository = subFeedbackRepository;
        this.departmentRepository = departmentRepository;
        this.buildingRepository = buildingRepository;
        this.historyRepository = historyRepository;
        this.generalService = generalService;
    }

    @Override
    public BotUser register(Long chatId, Message message) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        if (user != null) return user;
        else {
            String firstName = message.getFrom().getFirstName();
            String lastName = message.getFrom().getLastName() != null ? message.getFrom().getLastName() : " ";
            BotUser newUser = new BotUser(firstName, lastName, chatId, UserState.START, Role.CLIENT);
            return userRepository.save(newUser);
        }
    }


    @Override
    public void saveUserLastname(String text, Long id) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(id);
        if (user != null) {
            user.setLastname(text);
            user.setState(UserState.NAME);
            userRepository.save(user);
        }
    }

    @Override
    public void saveUserFirstname(String text, Long id) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(id);
        user.setFirstname(text);
        user.setState(UserState.BLOCK);
        userRepository.save(user);
    }

    @Override
    public void saveBlock(String text, Long chatId, SendMessage sendMessage) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        Building byName = buildingRepository.findByNameAndDeletedFalse(text);
        sendMessage.setChatId(user.getChatId());
        if (byName == null) {
            if (user.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ERROR_BUILD_NAME);
            else{
                sendMessage.setText(ResMessageRu.ERROR_BUILD_NAME);
            }
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(generalService.getBlock());
        } else {
            user.setState(UserState.GET_DEPARTMENT);
            userRepository.save(user);
            if (user.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ENTER_DEPARTMENT);
            else if (user.getLanguage().equals(BotQuery.RU_SELECT)) sendMessage.setText(ResMessageRu.ENTER_DEPARTMENT);
            historyRepository.save(new History(user.getId(), byName.getId()));
        }
    }

    @Override
    public void changStateFeedback(BotUser client) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(client.getChatId());
        user.setState(UserState.GET_FEEDBACK);
        userRepository.save(user);
    }

    @Override
    public String clientShowFeedback(BotUser client) {
        History history = historyRepository.findByUserIdAndFinishedFalseAndDeletedFalse(client.getId());
        FeedBack feedback = feedBackRepository.findByIdAndDeletedFalse(history.getFeedbackId());
        SubFeedback subFeedback = subFeedbackRepository.findByIdAndDeletedFalse(history.getSubFeedbackId());
        Building building = buildingRepository.findByIdAndDeletedFalse(history.getBuildId());
        Department department = departmentRepository.findByIdAndDeletedFalse(history.getDepartmentId());
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)){
            return "<b>Ariza Beruvchi: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                    "<b>Bino: </b>" + building.getName() + "\n" +
                    "<b>Bo'lim: </b>" + department.getName() + "\n" +
                    "<b>Xona: </b>" + department.getRoomNumber() + "\n" +
                    "<b>Ariza turi: </b>" + feedback.getName() + "\n" +
                    "<b>Muammo: </b>" + subFeedback.getName();
        }else if (client.getLanguage().equals(BotQuery.RU_SELECT)){
            return "<b>Заявитель: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                    "<b>Здание: </b>" + building.getName() + "\n" +
                    "<b>Отделение: </b>" + department.getName() + "\n" +
                    "<b>Комната: </b>" + department.getRoomNumber() + "\n" +
                    "<b>Тип Заявка: </b>" + feedback.getName() + "\n" +
                    "<b>Проблема: </b>" + subFeedback.getName();
        }
        return "ERROR";
    }

    @Override
    public List<BotUser> getAllAdmins() {
        return userRepository.findAllByRoleAndDeletedFalse(Role.ADMIN);
    }

    @Override
    public void changeStateGetFeedback(BotUser client) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(client.getChatId());
        user.setState(UserState.GET_FEEDBACK);
        userRepository.save(user);
    }

    @Override
    public void saveLang(BotUser client, String data) {
        client.setState(UserState.LAST_NAME);
        client.setLanguage(data);
        userRepository.save(client);
    }

    @Override
    public void changeStateSuperAdminCRUD(BotUser superAdmin) {
        superAdmin.setState(UserState.SUPER_ADMIN_CRUD);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateBuilding(BotUser superAdmin) {
        superAdmin.setState(UserState.SUPER_ADMIN_BUILDING);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateForSetting(BotUser superAdmin) {
        superAdmin.setState(UserState.SETTING);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateAddBuilding(BotUser superAdmin) {
    superAdmin.setState(UserState.ADD_BUILDING_STATE);
    userRepository.save(superAdmin);
    }

    @Override
    public void saveUserDepartmentName(String text, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        Department department = new Department();
        department.setName(text);
        Department saveDepartment = departmentRepository.save(department);
        user.setDepartment(saveDepartment);
        user.setState(UserState.GET_ROOM_NUM);
        History history = historyRepository.findByUserIdAndFinishedFalseAndDeletedFalse(user.getId());
        history.setDepartmentId(saveDepartment.getId());
        historyRepository.save(history);
        userRepository.save(user);
    }

    @Override
    public void saveUserRoomNum(String text, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        Department department = departmentRepository.
                findByIdAndDeletedFalse(user.getDepartment().getId());
        department.setRoomNumber(text);
        user.setState(UserState.SHARE_PHONE_NUMBER);
        departmentRepository.save(department);
        userRepository.save(user);
    }

    @Override
    public void saveUserPhoneNumber(String contact, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        Department department = departmentRepository.findByIdAndDeletedFalse(user.getDepartment().getId());
        user.setPhoneNumber(contact);
        user.setState(UserState.REGISTER_DONE);
        department.setInnerPhoneNumber(contact);
        departmentRepository.save(department);
        userRepository.save(user);
    }

    @Override
    public String showUserData(Long userId, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        if (user.getLanguage().equals(BotQuery.UZ_SELECT)) {
            return "<b>ID</b>: " + chatId + "\n" +
                    "<b>Ism:</b> " + user.getFirstname() + "\n" +
                    "<b>Familya:</b> " + user.getLastname() + "\n" +
                    "<b>Bo'lim: </b>" + user.getDepartment().getName() + "\n" +
                    "<b>Xona: </b>" + user.getDepartment().getRoomNumber() + "\n" +
                    "<b>Tel Raqam: </b>" + user.getDepartment().getInnerPhoneNumber();
        } else
            return "<b>ID</b>: " + chatId + "\n" +
                    "<b>Имя:</b> " + user.getFirstname() + "\n" +
                    "<b>Фамиля:</b> " + user.getLastname() + "\n" +
                    "<b>Отдель: </b>" + user.getDepartment().getName() + "\n" +
                    "<b>Комната: </b>" + user.getDepartment().getRoomNumber() + "\n" +
                    "<b>Тел. номер: </b>" + user.getDepartment().getInnerPhoneNumber();

    }

    @Override
    public void saveState(BotUser client) {
        client.setState(UserState.CHOOSE_LANG);
        userRepository.save(client);
    }

}
