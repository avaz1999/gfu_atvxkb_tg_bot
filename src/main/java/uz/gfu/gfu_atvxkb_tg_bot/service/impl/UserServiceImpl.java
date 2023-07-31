package uz.gfu.gfu_atvxkb_tg_bot.service.impl;


import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Department;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.History;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;
import uz.gfu.gfu_atvxkb_tg_bot.repository.BuildingRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.DepartmentRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.HistoryRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.enums.Role;
import uz.gfu.gfu_atvxkb_tg_bot.service.UserService;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final BuildingRepository buildingRepository;
    private final HistoryRepository historiyRepository;

    public UserServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository, BuildingRepository buildingRepository, HistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.buildingRepository = buildingRepository;
        this.historiyRepository = historyRepository;
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
    public void nextPage(BotUser currentUser) {
        Optional<BotUser> byId = userRepository.findById(currentUser.getId());
        if (byId.isPresent()) {
            BotUser user = byId.get();
            user.setCurrentPage(currentUser.getCurrentPage() + 1);
            userRepository.save(user);
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
    public void saveBlock(String text, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        Building byName = buildingRepository.findByName(text);
        if (byName!= null) {
            user.setState(UserState.GET_DEPARTMENT);
            userRepository.save(user);
        }
        assert byName != null;
        historiyRepository.save(new History(user.getId(),byName.getId()));
    }

    @Override
    public void changStateFeedback(BotUser client) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(client.getChatId());
        user.setState(UserState.GET_FEEDBACK);
        userRepository.save(user);
    }

    @Override
    public void saveUserDepartmentName(String text, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        Department department = new Department();
        department.setName(text);
        Department saveDepartment = departmentRepository.save(department);
        user.setDepartment(saveDepartment);
        user.setState(UserState.GET_ROOM_NUM);
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
    public void saveUserPhoneNumber(Contact contact, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        Department department = departmentRepository.findByIdAndDeletedFalse(user.getDepartment().getId());
        user.setPhoneNumber(contact.getPhoneNumber());
        user.setState(UserState.REGISTER_DONE);
        department.setInnerPhoneNumber(contact.getPhoneNumber());
        departmentRepository.save(department);
        userRepository.save(user);
    }

    @Override
    public String showUserData(Long userId, Long chatId) {
        BotUser user = userRepository.findByChatIdAndDeletedFalse(chatId);
        return "ID: " + chatId + "\n" +
                "Ism: " + user.getFirstname() + "\n" +
                "Familya: " + user.getLastname() + "\n" +
                "Bo'lim: " + user.getDepartment().getName() + "\n" +
//                    "Xona: " + user.getDepartment().getRoomNumber() + "\n" +
                "Tel Raqam: " + user.getDepartment().getInnerPhoneNumber();

    }

    @Override
    public void editData(Long userId) {
        Optional<BotUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            user.setCurrentPage(1);
            userRepository.save(user);
        }
    }

    @Override
    public void saveUserMessages(String text, Long chatId) {
        LinkedList<String> messages = new LinkedList<>();
        messages.add(text);
        Optional<BotUser> byId = userRepository.findById(chatId);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            user.setMessages(messages);
            userRepository.save(user);
        }
    }

    @Override
    public BotUser back(BotUser currentUser) {
        Optional<BotUser> byId = userRepository.findById(currentUser.getId());
        if (byId.isPresent()) {
            BotUser user = byId.get();
            user.setCurrentPage(currentUser.getCurrentPage() - 2);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void prev(BotUser currentUser) {
        currentUser.setCurrentPage(currentUser.getCurrentPage() - 1);
        userRepository.save(currentUser);
    }

    @Override
    public void saveState(BotUser client) {
        client.setState(UserState.LAST_NAME);
        userRepository.save(client);
    }

}
