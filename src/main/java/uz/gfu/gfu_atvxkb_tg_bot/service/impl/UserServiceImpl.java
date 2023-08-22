package uz.gfu.gfu_atvxkb_tg_bot.service.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.dto.UserDto;
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
            else {
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
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
            return "<b>Ariza Beruvchi: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                    "<b>Bino: </b>" + building.getName() + "\n" +
                    "<b>Bo'lim: </b>" + department.getName() + "\n" +
                    "<b>Xona: </b>" + department.getRoomNumber() + "\n" +
                    "<b>Ariza turi: </b>" + feedback.getName() + "\n" +
                    "<b>Muammo: </b>" + subFeedback.getName();
        } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
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
        superAdmin.setState(UserState.CRUD_BUILDING);
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
    public void changeStateEditBuilding(BotUser superAdmin) {
        superAdmin.setState(UserState.EDIT_BUILDING_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public void back(BotUser superAdmin, SendMessage sendMessage) {
        sendMessage.setChatId(superAdmin.getChatId());
        switch (superAdmin.getState()) {
            case EDIT_BUILDING_STATE,
                    REMOVE_BUILDING_STATE -> {
                superAdmin.setState(UserState.CRUD_BUILDING);
                sendMessage.setReplyMarkup(generalService.crudBuilding());
            }
            case EDIT_ADMIN_STATE,
                    REMOVE_ADMIN_STATE -> {
                superAdmin.setState(UserState.CRUD_ADMIN);
                sendMessage.setReplyMarkup(generalService.crudAdmin());
            }
            case EDIT_FEEDBACK_STATE,
                    REMOVE_FEEDBACK_STATE -> {
                superAdmin.setState(UserState.CRUD_FEEDBACK);
                sendMessage.setReplyMarkup(generalService.crudFeedback());
            }
        }
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateRemoveBuilding(BotUser superAdmin) {
        superAdmin.setState(UserState.REMOVE_BUILDING_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateAdmin(BotUser superAdmin) {
        superAdmin.setState(UserState.CRUD_ADMIN);
        userRepository.save(superAdmin);
    }
    @Override
    public void editAdmin(String text, BotUser superAdmin, SendMessage sendMessage) {
        sendMessage.setChatId(superAdmin.getChatId());
        BotUser admin = userRepository.findByEditedTrueAndDeletedFalse();
        if (admin == null){
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }else {
            admin.setPhoneNumber(text);
            admin.setEdited(false);
            userRepository.save(admin);
            superAdmin.setState(UserState.CRUD_ADMIN);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.SUCCESS_EDITED);
            else sendMessage.setText(ResMessageRu.SUCCESS_EDITED);
            sendMessage.setReplyMarkup(generalService.crudAdmin());
        }
    }

    @Override
    public void changeStateAddFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.ADD_FEEDBACK_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateRemoveFeedback(BotUser superAdmin) {
    superAdmin.setState(UserState.REMOVE_FEEDBACK_STATE);
    userRepository.save(superAdmin);
    }

    @Override
    public void changeStateUpdateFeedback(BotUser superAdmin) {
    superAdmin.setState(UserState.EDIT_FEEDBACK_STATE);
    userRepository.save(superAdmin);
    }

    @Override
    public void changeStateFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CRUD_FEEDBACK);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateSubFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CRUD_SUB_FEEDBACK);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateGetFeedbackWithSubFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.ADD_SUB_FEEDBACK_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public void createNewAdmin(String text, BotUser superAdmin, SendMessage sendMessage, AbsSender sender) {
        BotUser admin = userRepository.findByPhoneNumberAndDeletedFalse(text);
        if (admin == null) {
            BotUser newAdmin = new BotUser();
            newAdmin.setPhoneNumber(text);
            newAdmin.setRole(Role.ADMIN);
            newAdmin.setState(UserState.SETTING);
            userRepository.save(newAdmin);
            superAdmin.setState(UserState.CRUD_ADMIN);
            userRepository.save(superAdmin);
            sendMessage.setChatId(superAdmin.getChatId());
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) {
                sendMessage.setText(ResMessageUz.CREATED_NEW_ADMIN);
            } else sendMessage.setText(ResMessageRu.CREATED_NEW_ADMIN);
            sendMessage.setReplyMarkup(generalService.crudAdmin());
        } else {
            admin.setRole(Role.ADMIN);
            admin.setState(UserState.SETTING);
            userRepository.save(admin);
            superAdmin.setState(UserState.CRUD_ADMIN);
            userRepository.save(superAdmin);
            sendMessage.setChatId(superAdmin.getChatId());
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.CREATED_NEW_ADMIN);
            else sendMessage.setText(ResMessageRu.CREATED_NEW_ADMIN);
            sendMessage.setReplyMarkup(generalService.crudAdmin());
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            sendMessage.setChatId(admin.getChatId());
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.YOUR_ADMIN);
            else sendMessage.setText(ResMessageRu.YOUR_ADMIN);
            sendMessage.setReplyMarkup(generalService.startWork(admin));
        }
    }

    @Override
    public void changeAddNewAdminState(BotUser superAdmin) {
        superAdmin.setState(UserState.ADD_ADMIN_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public String getDtoAdmin(BotUser superAdmin) {
        List<BotUser> getAllAdmin = userRepository.findAllByRoleAndDeletedFalse(Role.ADMIN);
        List<UserDto> userDtoList = new ArrayList<>();
        for (BotUser botUser : getAllAdmin) {
            UserDto dto = new UserDto();
            dto.setId(botUser.getId());
            dto.setFirstname(botUser.getFirstname());
            dto.setLastname(botUser.getLastname());
            dto.setPhoneNumber(botUser.getPhoneNumber());
            userDtoList.add(dto);
        }
        StringBuilder sb = new StringBuilder();
        short i = 1;
        for (UserDto dto : userDtoList) {
            sb.append(superAdmin.getLanguage().equals(BotQuery.UZ_SELECT) ? "<b>" + i + " \uD83E\uDDD1\u200D\uD83D\uDCBB ADMIN: \n" +
                    "ID: " + dto.getId() + "\n" +
                    "ISM: " + dto.getFirstname() + "\n" +
                    "FAMILIYA: " + dto.getLastname() + "\n" +
                    "TEL RAQAM: " + dto.getPhoneNumber() + "\n\n" +
                    "</b>" : " " +
                    "<b>\uD83E\uDDD1\u200D\uD83D\uDCBB АДМИН: \n" +
                    "ИД: " + dto.getId() + "\n" +
                    "ИМЯ: " + dto.getFirstname() + "\n" +
                    "ФАМИЛИЯ: " + dto.getLastname() + "\n" +
                    "ТЕЛ НОМЕР: " + dto.getPhoneNumber() + "\n" +
                    "</b>");
        }
        return sb.toString();
    }

    @Override
    public void changeStateRemoveAdmin(BotUser superAdmin) {
        superAdmin.setState(UserState.REMOVE_ADMIN_STATE);
        userRepository.save(superAdmin);
    }
    @Override
    public void changeStateEditAdmin(BotUser superAdmin) {
        superAdmin.setState(UserState.EDIT_ADMIN_STATE);
        userRepository.save(superAdmin);
    }



    @Override
    public void getAdminByPhoneNumber(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender) {
        BotUser admin = userRepository.findByPhoneNumberAndDeletedFalse(data);
        sendMessage.enableHtml(true);
        if (admin == null){
            sendMessage.setChatId(superAdmin.getChatId());
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        }
        else if (superAdmin.getState().equals(UserState.REMOVE_ADMIN_STATE)){
            admin.setDeleted(true);
            admin.setDeletedBy(superAdmin.getId());
            userRepository.save(admin);
            superAdmin.setState(UserState.CRUD_ADMIN);
            userRepository.save(superAdmin);
            sendMessage.setChatId(superAdmin.getChatId());
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.DELETED_SUCCESS);
            else sendMessage.setText(ResMessageRu.DELETED_SUCCESS);
            sendMessage.setReplyMarkup(generalService.crudAdmin());
        } else if (superAdmin.getState().equals(UserState.EDIT_ADMIN_STATE)) {
            admin.setEdited(true);
            userRepository.save(admin);
            sendMessage.setChatId(superAdmin.getChatId());
            superAdmin.setState(UserState.EDIT_ADMIN_STATE1);
            userRepository.save(superAdmin);
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ENTER_NEW_ADMIN_PHONE_NUMBER);
            else sendMessage.setText(ResMessageRu.ENTER_NEW_ADMIN_PHONE_NUMBER);
        }
        try {
             sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
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
