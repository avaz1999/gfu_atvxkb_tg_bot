package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface DeveloperService {
    void developerService(BotUser currentUser, Message message, SendMessage sendMessage, AbsSender sender);
    BotUser addSuperAdmin(Message message,SendMessage sendMessage,AbsSender sender);
    BotUser addAdmin(Message message,SendMessage sendMessage,AbsSender sender);
    BotUser deleteSuperAdmin(Message message,SendMessage sendMessage,AbsSender sender);
    BotUser deleteAdmin(Message message,SendMessage sendMessage,AbsSender sender);
    void checkDeveloperState(BotUser developer,Message message,SendMessage sendMessage,AbsSender sender);
}
