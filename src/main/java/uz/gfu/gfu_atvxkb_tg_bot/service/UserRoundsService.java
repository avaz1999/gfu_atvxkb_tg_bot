package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface UserRoundsService {
    void getRound1(BotUser user, String data, SendMessage sendMessage);

    void getRound2(BotUser user, String text, SendMessage sendMessage);

    void getRound3(BotUser user, String text, SendMessage sendMessage);

    void getRound4(BotUser user, String text, SendMessage sendMessage);

    void getRound5(BotUser user, String text, SendMessage sendMessage);

    void getRound6(BotUser user, String text, SendMessage sendMessage);

    void getRound7(BotUser user, Message message, SendMessage sendMessage);

    void getRound8(BotUser user, String data, SendMessage sendMessage);

    void getRound9(BotUser user, String text, SendMessage sendMessage);

    void getRound10(BotUser user, String text, SendMessage sendMessage);

    void getRound11(BotUser client, String data, SendMessage sendMessage);
}
