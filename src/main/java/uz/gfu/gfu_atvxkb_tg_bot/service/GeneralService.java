package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface GeneralService {
    ReplyKeyboard getReplyKeyboard(BotUser user);

    ReplyKeyboard getInlineKeyboardButton(BotUser currentUser);

    ReplyKeyboard getInlineKeyboardButtonForService(BotUser currentUser, String text);
}
