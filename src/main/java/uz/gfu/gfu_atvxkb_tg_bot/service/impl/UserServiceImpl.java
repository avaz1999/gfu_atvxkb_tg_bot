package uz.gfu.gfu_atvxkb_tg_bot.service.impl;


import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Department;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.repository.DepartmentRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.enums.Role;
import uz.gfu.gfu_atvxkb_tg_bot.service.UserService;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public BotUser getCurrentUser(Long chatId, Message message) {
        for (BotUser user : userRepository.findAll()) {
            if (Objects.equals(user.getChatId(), chatId)) {
                return user;
            }
        }
        org.telegram.telegrambots.meta.api.objects.User from = message.getFrom();
        BotUser user = new BotUser();
        user.setCurrentPage(1);
        user.setFirstname(from.getFirstName());
        user.setLastname(from.getLastName() != null ? from.getLastName() : "");
        user.setRole(Role.CLIENT);
        user.setChatId(chatId);
        return userRepository.save(user);
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
        Optional<BotUser> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            user.setLastname(text);
            userRepository.save(user);
        }

    }

    @Override
    public void saveUserFirstname(String text, Long id) {
        Optional<BotUser> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            user.setFirstname(text);
            userRepository.save(user);
        }
    }

    @Override
    public void saveUserDepartmentName(String text, Long userId) {
        Optional<BotUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            Department department = new Department();
            department.setName(text);
            Department saveDepartment = departmentRepository.save(department);
            user.setDepartment(saveDepartment);
            userRepository.save(user);
        }
    }

    @Override
    public void saveUserRoomNum(String text, Long userId) {
        Optional<BotUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            Optional<Department> optionalDepartment = departmentRepository.findById(user.getDepartment().getId());
            if (optionalDepartment.isPresent()) {
                Department department = optionalDepartment.get();
                departmentRepository.save(department);
            }
        }
    }

    @Override
    public void saveUserPhoneNumber(Contact contact, Long userId) {
        Optional<BotUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            Optional<Department> optionalDepartment = departmentRepository.findById(user.getDepartment().getId());
            if (optionalDepartment.isPresent()) {
                Department department = optionalDepartment.get();
                user.setPhoneNumber(department.getInnerPhoneNumber());
                department.setInnerPhoneNumber(contact.getPhoneNumber());
                departmentRepository.save(department);
            }
        }
    }

    @Override
    public String showUserData(Long userId, Long chatId) {
        Optional<BotUser> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            BotUser user = byId.get();
            return "ID: " + chatId + "\n" +
                    "Ism: " + user.getFirstname() + "\n" +
                    "Familya: " + user.getLastname() + "\n" +
                    "Bo'lim: " + user.getDepartment().getName() + "\n" +
//                    "Xona: " + user.getDepartment().getRoomNumber() + "\n" +
                    "Tel Raqam: " + user.getDepartment().getInnerPhoneNumber();
        }
        return null;
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
}
