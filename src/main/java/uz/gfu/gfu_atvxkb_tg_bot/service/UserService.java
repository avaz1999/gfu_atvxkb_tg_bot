package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface UserService {
    BotUser getCurrentUser(Long chatId, Message message);

    void nextPage(BotUser currentUser);

    void saveUserLastname(String text, Long chatId);

    void saveUserFirstname(String text, Long id);

    void saveUserDepartmentName(String text, Long userId);

    void saveUserRoomNum(String text, Long userId);

    void saveUserPhoneNumber(Contact contact, Long userId);

    String showUserData(Long userId,Long chatId);

    void editData(Long userId);

    void saveUserMessages(String text, Long chatId);

    BotUser back(BotUser currentUser);

    void prev(BotUser currentUser);
}
