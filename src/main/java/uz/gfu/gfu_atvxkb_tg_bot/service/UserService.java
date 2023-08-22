package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

import java.util.List;

public interface UserService {
    BotUser register(Long chatId, Message message);

    void saveUserLastname(String text, Long chatId);

    void saveUserFirstname(String text, Long id);

    void saveUserDepartmentName(String text, BotUser userId);

    void saveUserRoomNum(String text, BotUser userId);

    void saveUserPhoneNumber(SendMessage sendMessage, String contact, BotUser userId,AbsSender sender);

    String showUserData(Long userId,Long chatId);


    void saveState(BotUser client);

    void saveBlock(String text, BotUser chatId, SendMessage sendMessage);

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

    void back(BotUser superAdmin, SendMessage sendMessage);

    void changeStateRemoveBuilding(BotUser superAdmin);

    void changeStateAdmin(BotUser superAdmin);

    void createNewAdmin(String text, BotUser superAdmin, SendMessage sendMessage, AbsSender sender);

    void changeAddNewAdminState(BotUser superAdmin);


    String getDtoAdmin(BotUser superAdmin);

    void changeStateRemoveAdmin(BotUser superAdmin);

    void getAdminByPhoneNumber(BotUser superAdmin, SendMessage sendMessage, String data, AbsSender sender);

    void changeStateEditAdmin(BotUser superAdmin);

    void editAdmin(String text, BotUser superAdmin, SendMessage sendMessage);

    void changeStateAddFeedback(BotUser superAdmin);

    void changeStateRemoveFeedback(BotUser superAdmin);

    void changeStateUpdateFeedback(BotUser superAdmin);

    void changeStateFeedback(BotUser superAdmin);

    void changeStateSubFeedback(BotUser superAdmin);

    void changeStateGetFeedbackWithSubFeedback(BotUser superAdmin);
}
