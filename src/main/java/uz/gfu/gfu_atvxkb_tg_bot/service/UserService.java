package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

import java.util.List;

public interface UserService {
    BotUser register(Long chatId, Message message);

    void saveUserLastname(String text, Long chatId);

    void saveUserFirstname(String text, Long id);

    void saveUserDepartmentName(String text, Long userId);

    void saveUserRoomNum(String text, Long userId);

    void saveUserPhoneNumber(String contact, Long userId);

    String showUserData(Long userId,Long chatId);


    void saveState(BotUser client);

    void saveBlock(String text, Long chatId, SendMessage sendMessage);

    void changStateFeedback(BotUser client);

    String clientShowFeedback(BotUser client);

    List<BotUser> getAllAdmins();

    void changeStateGetFeedback(BotUser client);

    void saveLang(BotUser client, String data);

    void changeStateSuperAdminCRUD(BotUser superAdmin);

    void changeStateBuilding(BotUser superAdmin);

    void changeStateForSetting(BotUser superAdmin);

    void changeStateAddBuilding(BotUser superAdmin);

    void changeStateEditBuilding(BotUser superAdmin);

    void back(BotUser superAdmin);
}
