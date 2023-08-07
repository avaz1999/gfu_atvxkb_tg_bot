package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface GeneralService {

    ReplyKeyboard getChooseLang();

    ReplyKeyboard getBlock();

    ReplyKeyboard getPhoneNumber();

    ReplyKeyboard getRegisterDone(BotUser client);

    ReplyKeyboard getFeedbacks(BotUser client);

    ReplyKeyboard getSubFeedbacks(String text);

    ReplyKeyboard serviceDone();

}
