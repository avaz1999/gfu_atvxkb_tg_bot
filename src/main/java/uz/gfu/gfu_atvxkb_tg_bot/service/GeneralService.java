package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface GeneralService {

    ReplyKeyboard getChooseLang();

    ReplyKeyboard getBlock();

    ReplyKeyboard getPhoneNumber(BotUser client);

    ReplyKeyboard getRegisterDone(BotUser client, Application application);

    ReplyKeyboard getFeedbacks(BotUser client);

    ReplyKeyboard getSubFeedbacks(String text);

    ReplyKeyboard serviceDone(Long applicationId);

    ReplyKeyboard getSettingForSuperAdmin(BotUser superAdmin);

    ReplyKeyboard crudBuilding();

    ReplyKeyboard crudFeedback();

    ReplyKeyboard crudSubFeedback();

    ReplyKeyboard crudAdmin();

    ReplyKeyboard getBuildingNumber();

    ReplyKeyboard getBack(BotUser superAdmin);

    ReplyKeyboard startWork(BotUser superAdmin);

    ReplyKeyboard getAdminNumber();

    ReplyKeyboard getFeedbacksNumber(boolean lang);

    ReplyKeyboard sendFeedback(BotUser client);


    ReplyKeyboard rateAdmin(BotUser admin, BotUser botUser);

    ReplyKeyboard forAdmin();

    ReplyKeyboard getSubFeedbacksNumber(boolean lang);

    ReplyKeyboard crudForDeveloper(BotUser developer);

}
