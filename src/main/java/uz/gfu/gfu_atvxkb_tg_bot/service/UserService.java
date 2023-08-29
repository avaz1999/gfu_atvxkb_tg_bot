package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

import java.util.List;

public interface UserService {
    BotUser register(Long chatId, Message message);

    void saveUserLastname(String text, Long chatId);

    void saveUserFirstname(String text, Long id);

    void saveUserDepartmentName(String text, BotUser userId);

    void saveUserRoomNum(String text, BotUser userId);

    Boolean checkPhoneNumber(String phoneNumber);

    void saveUserPhoneNumber(SendMessage sendMessage, String contact, BotUser userId,AbsSender sender);

    String showUserData(Long userId,Long chatId);


    void saveState(BotUser client);

    void saveBlock(String text, BotUser chatId, SendMessage sendMessage);

    void changStateFeedback(BotUser client);

    String clientShowFeedback(BotUser client, Application saveApplication);

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

    void changeStateAddFeedback(BotUser superAdmin, boolean lang);

    void changeStateRemoveFeedback(BotUser superAdmin);

    void changeStateUpdateFeedback(BotUser superAdmin);

    void changeStateFeedback(BotUser superAdmin);

    void changeStateSubFeedback(BotUser superAdmin);

    void changeStateGetFeedbackWithSubFeedback(BotUser superAdmin);

    void saveAdminPhoneNumber(SendMessage sender, String phoneNumber, BotUser client, AbsSender sender1, BotUser botUser);

    BotUser findAdminByPhoneNumber(String phoneNumber);

    void sendMessageToAdmin(BotUser admins, BotUser client, Application application, SendMessage sendMessage, AbsSender sender);

    void adminInProses(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender);

    void adminDone(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender);

    void adminFailed(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender);

    void rateAdmin(BotUser client, Byte rate, Long adminId, SendMessage sendMessage, AbsSender sender);

    void saveSuperAdmin(BotUser superAdmin, Message message);

    void changeStateChooseLangForAddFeedback(BotUser superAdmin);

    void changeStateChooseLangForRemoveFeedback(BotUser superAdmin);
}
