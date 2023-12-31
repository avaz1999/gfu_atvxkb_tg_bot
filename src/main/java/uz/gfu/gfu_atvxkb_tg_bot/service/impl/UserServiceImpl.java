package uz.gfu.gfu_atvxkb_tg_bot.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.dto.UserDto;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.*;
import uz.gfu.gfu_atvxkb_tg_bot.enums.State;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.repository.*;
import uz.gfu.gfu_atvxkb_tg_bot.enums.Role;
import uz.gfu.gfu_atvxkb_tg_bot.service.AdminService;
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
    private final ApplicationRepository applicationRepository;
    @Autowired
    @Lazy
    AdminService adminService;
    @Lazy
    private final GeneralService generalService;

    public UserServiceImpl(UserRepository userRepository, FeedBackRepository feedBackRepository, SubFeedbackRepository subFeedbackRepository, DepartmentRepository departmentRepository, BuildingRepository buildingRepository, ApplicationRepository historyRepository, GeneralService generalService) {
        this.userRepository = userRepository;
        this.feedBackRepository = feedBackRepository;
        this.subFeedbackRepository = subFeedbackRepository;
        this.departmentRepository = departmentRepository;
        this.buildingRepository = buildingRepository;
        this.applicationRepository = historyRepository;
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
    public void saveBlock(String text, BotUser client, SendMessage sendMessage) {
        Building byName = buildingRepository.findByNameAndDeletedFalse(text);
        sendMessage.setChatId(client.getChatId());
        if (byName == null) {
            if (client.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ERROR_BUILD_NAME);
            else sendMessage.setText(ResMessageRu.ERROR_BUILD_NAME);
            sendMessage.setReplyMarkup(generalService.getBlock());
        } else {
            client.setState(UserState.GET_DEPARTMENT);
            client.setBuilding(byName);
            userRepository.save(client);
            if (client.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ENTER_DEPARTMENT);
            else if (client.getLanguage().equals(BotQuery.RU_SELECT))
                sendMessage.setText(ResMessageRu.ENTER_DEPARTMENT);
        }
    }

    @Override
    public void changStateFeedback(BotUser client) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(client.getChatId());
        user.setState(UserState.GET_FEEDBACK);
        userRepository.save(user);
    }

    @Override
    public String clientShowFeedback(BotUser client, Application application) {
        Department department = departmentRepository.findByIdAndDeletedFalse(application.getDepartmentId());
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
            return "<b>Ariza Beruvchi: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                    "<b>Bino: </b>" + application.getBuildingName() + "\n" +
                    "<b>Bo'lim: </b>" + department.getName() + "\n" +
                    "<b>Xona: </b>" + department.getRoomNumber() + "\n" +
                    "<b>Ariza turi: </b>" + application.getFeedbackName() + "\n" +
                    "<b>Muammo: </b>" + application.getSubFeedbackName();
        } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
            return "<b>Заявитель: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                    "<b>Здание: </b>" + application.getBuildingName() + "\n" +
                    "<b>Отделение: </b>" + department.getName() + "\n" +
                    "<b>Комната: </b>" + department.getRoomNumber() + "\n" +
                    "<b>Тип Заявка: </b>" + application.getFeedbackName() + "\n" +
                    "<b>Проблема: </b>" + application.getSubFeedbackName();
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
            case EDIT_SUB_FEEDBACK_STATE,
                    REMOVE_SUB_FEEDBACK_STATE-> {
                superAdmin.setState(UserState.CRUD_SUB_FEEDBACK);
                sendMessage.setReplyMarkup(generalService.crudSubFeedback());
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
        if (admin == null) {
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        } else {
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
    public void changeStateAddFeedback(BotUser superAdmin, boolean lang) {
        if (lang) superAdmin.setState(UserState.ADD_FEEDBACK_STATE_RUS);
        else superAdmin.setState(UserState.ADD_FEEDBACK_STATE);
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
        if (admin == null) {
            sendMessage.setChatId(superAdmin.getChatId());
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
            else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
        } else if (superAdmin.getState().equals(UserState.REMOVE_ADMIN_STATE)) {
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
            if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT))
                sendMessage.setText(ResMessageUz.ENTER_NEW_ADMIN_PHONE_NUMBER);
            else sendMessage.setText(ResMessageRu.ENTER_NEW_ADMIN_PHONE_NUMBER);
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void saveUserDepartmentName(String text, BotUser client) {
        Department department = new Department();
        department.setName(text);
        Department saveDepartment = departmentRepository.save(department);
        client.setDepartment(saveDepartment);
        client.setState(UserState.GET_ROOM_NUM);
        userRepository.save(client);
    }

    @Override
    public void saveUserRoomNum(String text, BotUser client) {
        Department department = departmentRepository.
                findByIdAndDeletedFalse(client.getDepartment().getId());
        department.setRoomNumber(text);
        client.setState(UserState.SHARE_PHONE_NUMBER);
        departmentRepository.save(department);
        userRepository.save(client);
    }

    @Override
    public Boolean checkPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumberAndDeletedFalse(phoneNumber);
    }

    @Override
    public void saveAdminPhoneNumber(SendMessage sendMessage, String phoneNumber, BotUser admin, AbsSender sender, BotUser client) {
        if (admin.getRole().equals(Role.ADMIN)) {
            admin.setChatId(client.getChatId());
            admin.setBuilding(client.getBuilding());
            admin.setLanguage(client.getLanguage());
            admin.setDepartment(client.getDepartment());
            admin.setFirstname(client.getFirstname());
            admin.setLastname(client.getLastname());
            admin.setLanguage(client.getLanguage());
            admin.setFeedBacks(client.getFeedBacks());
            admin.setState(UserState.ADMIN_FOR_FEEDBACK);
            BotUser newAdmin = userRepository.save(admin);
            Department department = departmentRepository.findByIdAndDeletedFalse(newAdmin.getDepartment().getId());
            userRepository.delete(client);
            List<Application> allApplications = applicationRepository.findAllByDoneAndDeletedFalse(State.CREATED);
            for (Application application : allApplications) {
                BotUser allClient = userRepository.findByIdAndDeletedFalse(application.getUserId());
                sendMessage.setChatId(newAdmin.getChatId());
                String adminShow = newAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                        ? formUz(allClient,
                        application,
                        department)
                        : formRus(allClient,
                        application,
                        department);
                sendMessage.setText(adminShow);
                sendMessage.setReplyMarkup(generalService.serviceDone(application.getId()));
                try {
                    sender.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            saveUserPhoneNumber(sendMessage, phoneNumber, admin, sender);
        }
    }

    @Override
    public void sendMessageToAdmin(BotUser admins, BotUser client, Application application, SendMessage sendMessage, AbsSender sender) {
        if (admins.getChatId() == null) {
            String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.NOT_ADMIN
                    : ResMessageRu.NOT_ADMIN;
            sendMessage.setText(msg);
            sendMessage.setChatId(client.getChatId());
            sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
        } else {
            Department department = departmentRepository.findByIdAndDeletedFalse(application.getDepartmentId());
            String shareMessage = admins.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? formUz(client, application, department)
                    : formRus(client, application, department);
            sendMessage.setChatId(admins.getChatId());
            sendMessage.setText(shareMessage);
            sendMessage.setReplyMarkup(generalService.serviceDone(application.getId()));
        }
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void adminInProses(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender) {
        Application application = applicationRepository.findByIdAndDeletedFalse(applicationId);
        if (application.getDone().equals(State.CREATED)) {
            if (application.getAdminId() != null) {
                BotUser connectAdmin = userRepository.findByIdAndDeletedFalse(application.getAdminId());
                if (connectAdmin == null) errorMessage(admin, sendMessage);
                else {
                    String msg = admin.getLanguage().equals(BotQuery.UZ_SELECT)
                            ? "<b>Bu so'rov" + connectAdmin.getFirstname() + " " + connectAdmin.getLanguage() + " ga  biriktitilgan</b>"
                            : "<b>Этот запрос прикреплен " + connectAdmin.getFirstname() + " " + connectAdmin.getLanguage() + "</b>";
                    sendMessage.setText(msg);
                    sendMessage.setChatId(admin.getChatId());
                }
                try {
                    sender.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else connectAdminToClient(admin, sendMessage, sender, application);
        } else if (application.getDone().equals(State.FAILED))
            connectAdminToClient(admin, sendMessage, sender, application);
        else {
            String msg = admin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.TO_CARRY_OUT
                    : ResMessageRu.TO_CARRY_OUT;
            sendMessage.setText(msg);
            sendMessage.setChatId(admin.getChatId());
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void connectAdminToClient(BotUser admin, SendMessage sendMessage, AbsSender sender, Application application) {
        BotUser client = userRepository.findByIdAndDeletedFalse(application.getUserId());
        application.setDone(State.IN_PROSES);
        application.setAdminId(admin.getId());
        applicationRepository.save(application);
        String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.CONNECT_ADMIN + admin.getFirstname() + " " + admin.getLastname() + ResMessageUz.CONNECT_ADMIN2
                : ResMessageRu.CONNECT_ADMIN + admin.getFirstname() + " " + admin.getLastname() + ResMessageRu.CONNECT_ADMIN2;
        sendMessage.setChatId(client.getChatId());
        sendMessage.setText(msg);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void adminDone(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender) {
        Application application = applicationRepository.findByIdAndDeletedFalse(applicationId);
        if (application.getDone().equals(State.IN_PROSES) || application.getDone().equals(State.FAILED)) {
            BotUser client = userRepository.findByIdAndDeletedFalse(application.getUserId());
            application.setDone(State.DONE);
            applicationRepository.save(application);
            sendMessage.setChatId(client.getChatId());
            sendMessage.enableHtml(true);
            sendMessage.setReplyMarkup(generalService.rateAdmin(client, admin));
            String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? admin.getFirstname() + " " + admin.getLastname() + ResMessageUz.SERVICE_DONE
                    : admin.getFirstname() + " " + admin.getLastname() + ResMessageRu.SERVICE_DONE;
            sendMessage.setText(msg);

            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            sendMessage.setChatId(admin.getChatId());
            String msgToAdmin = admin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.SUCCESS_DONE
                    : ResMessageRu.SUCCESS_DONE;
            sendMessage.setText(msgToAdmin);
            sendMessage.setReplyMarkup(generalService.forAdmin());

            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            // TODO: 28/08/23 Super adminni developer create qilganda ochiladi
//            for (BotUser superAdmin : userRepository.findAllByRoleAndDeletedFalse(Role.SUPER_ADMIN)) {
//                sendMessage.setChatId(superAdmin.getChatId());
//                String msgToSuperAdmin = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
//                        ? admin.getFirstname() + " " + admin.getLastname()  + ResMessageUz.SUCCESS_ADMIN_DONE
//                        : admin.getFirstname() + " " + admin.getLastname()  + ResMessageRu.SUCCESS_ADMIN_DONE;
//                sendMessage.setChatId(msgToSuperAdmin);
//                sendMessage.setReplyMarkup(generalService.forAdmin());

//                try {
//                    sender.execute(sendMessage);
//                } catch (TelegramApiException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
        } else {
            sendMessage.setChatId(admin.getChatId());
            String msg = admin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ERROR_ADMIN_IN_PROSES
                    : ResMessageRu.ERROR_ADMIN_IN_PROSES;
            sendMessage.setText(msg);
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void adminFailed(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender) {
        Application application = applicationRepository.findByIdAndDeletedFalse(applicationId);
        if (application.getDone().equals(State.DONE) || application.getDone().equals(State.IN_PROSES)) {
            BotUser client = userRepository.findByIdAndDeletedFalse(application.getUserId());
            application.setDone(State.FAILED);
            applicationRepository.save(application);
            sendMessage.setChatId(client.getChatId());
            String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.SORRY_FAILED
                    : ResMessageRu.SORRY_FAILED;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            List<BotUser> superAdmins = userRepository.findAllByRoleAndDeletedFalse(Role.SUPER_ADMIN);
            for (BotUser superAdmin : superAdmins) {
                sendMessageToSuperAdmin(superAdmin, admin, client, application, sendMessage, sender);
            }
            for (BotUser admins : getAllAdmins()) {
                sendMessageToAdmin(admins, client, application, sendMessage, sender);
            }
        } else {
            String msg = admin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? ResMessageUz.ERROR_STATE_SERVICE
                    : ResMessageRu.ERROR_STATE_SERVICE;
            sendMessage.setChatId(admin.getChatId());
            sendMessage.setText(msg);
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void rateAdmin(BotUser client, Byte rate, Long adminId, SendMessage sendMessage, AbsSender sender) {
        BotUser admin = userRepository.findByIdAndDeletedFalse(adminId);
        Application rateApplication = applicationRepository
                .findTopByUserIdAndAdminIdAndDoneAndDeletedFalseOrderByCreatedAtDesc(client.getId(), adminId, State.DONE);
        rateApplication.setRate(rate);
        applicationRepository.save(rateApplication);
        List<Application> applicationOfAdmin = applicationRepository.findAllByAdminIdAndDeletedFalse(adminId);
        int count = 0;
        int sum = 0;
        for (Application application : applicationOfAdmin) {
            count += application.getAdminId();
            sum += application.getRate();
        }
        Float averageRate = (float) sum / count;
        admin.setRateAdmin(averageRate);
        userRepository.save(admin);
        sendMessage.setChatId(client.getChatId());
        String msg = client.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.RATE_SUCCESS
                : ResMessageRu.RATE_SUCCESS;
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.getFeedbacks(client));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        for (BotUser superAdmin : userRepository.findAllByRoleAndDeletedFalse(Role.SUPER_ADMIN)) {
            sendMessage.setChatId(superAdmin.getChatId());
            String msgToSuperAdmin = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                    ? "<b>Ariza beruvchi: </b>" + client.getFirstname() + " " + client.getLastname() +
                    "\n<b>Xodim: </b>" + admin.getFirstname() + " " + admin.getLastname() + "<b> ni\n </b>" + rate + "</b>: Baho bilan baholadi </b>"
                    : "<b>Заявитель: </b>" + client.getFirstname() + " " + client.getLastname() +
                    "<b>Сотрудник: </b>" + admin.getFirstname() + " " + admin.getLastname() + "<b> тот\n</b>" + rate + "<b>: Оценивается оценкой</b>";
            sendMessage.setText(msgToSuperAdmin);
//            sendMessage.setReplyMarkup(generalService.forAdmin());
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void saveSuperAdmin(BotUser superAdmin, Message message) {
        BotUser saveSuperAdmin = userRepository.findByIdAndDeletedFalse(superAdmin.getId());
        Long chatId = message.getChatId();
        saveSuperAdmin.setChatId(chatId);
        userRepository.save(saveSuperAdmin);
    }

    @Override
    public void changeStateChooseLangForAddFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_ADD);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateChooseLangForRemoveFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_REMOVE);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateChooseLangForGetAllFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_GET);
        userRepository.save(superAdmin);
    }

    @Override
    public void crudFeedbackState(BotUser superAdmin) {
        superAdmin.setState(UserState.CRUD_FEEDBACK);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateChooseLangForEditFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_EDIT);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateChooseLangForAddSubFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_SUB_FEEDBACK_ADD);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateChooseLangForEditSubFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_SUB_FEEDBACK_EDIT);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateChooseLangForGetSubFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_SUB_FEEDBACK_GET);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateChooseLangForRemoveSubFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.CHOOSE_LANG_FOR_SUB_FEEDBACK_REMOVE);
        userRepository.save(superAdmin);
    }

    @Override
    public void crudSubFeedbackState(BotUser superAdmin) {
    superAdmin.setState(UserState.CRUD_SUB_FEEDBACK);
    userRepository.save(superAdmin);
    }


    private void sendMessageToSuperAdmin(BotUser superAdmin, BotUser admin, BotUser client, Application application, SendMessage sendMessage, AbsSender sender) {
        Department department = departmentRepository.findByIdAndDeletedFalse(application.getDepartmentId());
        String msg = superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.FAILED_SERVICE + "<b>" + admin.getFirstname() + " " + admin.getLastname() + ResMessageUz.FAILED_SERVICE2 +
                formUz(client, application, department)
                : ResMessageRu.FAILED_SERVICE + "<b>" + admin.getFirstname() + " " + admin.getLastname() + ResMessageRu.FAILED_SERVICE2 +
                formRus(client, application, department);
        sendMessage.setChatId(superAdmin.getChatId());
        sendMessage.setText(msg);
        sendMessage.setReplyMarkup(generalService.serviceDone(application.getId()));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BotUser findAdminByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberAndDeletedFalse(phoneNumber);
    }


    @Override
    public void saveUserPhoneNumber(SendMessage sendMessage, String contact, BotUser client, AbsSender sender) {
        Department department = departmentRepository.findByIdAndDeletedFalse(client.getDepartment().getId());
        savePhoneNumber(sendMessage, contact, client, department, sender);
    }

    private void savePhoneNumber(SendMessage sendMessage, String contact, BotUser client, Department department, AbsSender sender) {
        client.setPhoneNumber(contact);
        client.setState(UserState.REGISTER_DONE);
        department.setInnerPhoneNumber(contact);
        departmentRepository.save(department);
        userRepository.save(client);
        sendMessage.setChatId(client.getChatId());
        if (client.getLanguage().equals(BotQuery.UZ_SELECT))
            sendMessage.setText(ResMessageUz.SHOW_DATA + showUserData(client.getId(), client.getChatId()));
        else sendMessage.setText(ResMessageRu.SHOW_DATA + showUserData(client.getId(), client.getChatId()));
        sendMessage.setReplyMarkup(generalService.getRegisterDone(client, new Application()));
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String formRus(BotUser client, Application application, Department department) {
        return application.getDone().equals(State.CREATED)
                ? "<b>Заявитель: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                "<b>Здание: </b>" + application.getBuildingName() + "\n" +
                "<b>Отделение: </b>" + department.getName() + "\n" +
                "<b>Комната: </b>" + department.getRoomNumber() + "\n" +
                "<b>Номер телефона: </b>" + client.getPhoneNumber() + "\n" +
                "<b>Тип Заявка: </b>" + application.getFeedbackName() + "\n" +
                "<b>Проблема: </b>" + application.getSubFeedbackName()
                : "<b>Эта задача не удалась\nМы просим вас пересмотреть</b>" + "\n" +
                "<b>Заявитель: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                "<b>Здание: </b>" + application.getBuildingName() + "\n" +
                "<b>Отделение: </b>" + department.getName() + "\n" +
                "<b>Комната: </b>" + department.getRoomNumber() + "\n" +
                "<b>Номер телефона: </b>" + client.getPhoneNumber() + "\n" +
                "<b>Тип Заявка: </b>" + application.getFeedbackName() + "\n" +
                "<b>Проблема: </b>" + application.getSubFeedbackName();
    }

    private String formUz(BotUser client, Application application, Department department) {
        return application.getDone().equals(State.CREATED)
                ? "<b>Ariza Beruvchi: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                "<b>Bino: </b>" + application.getBuildingName() + "\n" +
                "<b>Bo'lim: </b>" + department.getName() + "\n" +
                "<b>Xona: </b>" + department.getRoomNumber() + "\n" +
                "<b>Telefon raqam: </b>" + client.getPhoneNumber() + "\n" +
                "<b>Ariza turi: </b>" + application.getFeedbackName() + "\n" +
                "<b>Muammo: </b>" + application.getSubFeedbackName()
                : "<b>Bu vazifa bajarilmadi \nQaytadan ko'rib chiqishizni so'rab qolamiz\n</b>" + "\n" +
                "<b>Ariza Beruvchi: </b>" + client.getFirstname() + " " + client.getLastname() + "\n" +
                "<b>Bino: </b>" + application.getBuildingName() + "\n" +
                "<b>Bo'lim: </b>" + department.getName() + "\n" +
                "<b>Xona: </b>" + department.getRoomNumber() + "\n" +
                "<b>Telefon raqam: </b>" + client.getPhoneNumber() + "\n" +
                "<b>Ariza turi: </b>" + application.getFeedbackName() + "\n" +
                "<b>Muammo: </b>" + application.getSubFeedbackName();
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

    private static void errorMessage(BotUser superAdmin, SendMessage sendMessage) {
        if (superAdmin.getLanguage().equals(BotQuery.UZ_SELECT)) sendMessage.setText(ResMessageUz.ERROR_MESSAGE);
        else sendMessage.setText(ResMessageRu.ERROR_MESSAGE);
    }

    @Override
    public void changeStateAddSubFeedback(BotUser superAdmin) {
        superAdmin.setState(UserState.ADD_SUB_FEEDBACK_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateSubFeedbackEdit(BotUser superAdmin) {
        superAdmin.setState(UserState.EDIT_SUB_FEEDBACK_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateSubFeedbackEdit1(BotUser superAdmin) {
        superAdmin.setState(UserState.EDIT_SUB_FEEDBACK_STATE_1);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateSubFeedbackRemove(BotUser superAdmin) {
        superAdmin.setState(UserState.REMOVE_SUB_FEEDBACK_STATE);
        userRepository.save(superAdmin);
    }

    @Override
    public void changeStateEditData(BotUser client) {
        client.setState(UserState.LAST_NAME);
        userRepository.save(client);
    }
}
