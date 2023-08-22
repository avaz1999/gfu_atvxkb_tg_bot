package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.service.DeveloperService;
@Service
public class DeveloperServiceImpl implements DeveloperService {
    @Override
    public void developerService(BotUser developer, Message message, SendMessage sendMessage, AbsSender sender) {

    }

    @Override
    public BotUser addSuperAdmin(Message message, SendMessage sendMessage, AbsSender sender) {
        return null;
    }

    @Override
    public BotUser addAdmin(Message message, SendMessage sendMessage, AbsSender sender) {
        return null;
    }

    @Override
    public BotUser deleteSuperAdmin(Message message, SendMessage sendMessage, AbsSender sender) {
        return null;
    }

    @Override
    public BotUser deleteAdmin(Message message, SendMessage sendMessage, AbsSender sender) {
        return null;
    }

    @Override
    public void checkDeveloperState(BotUser developer, Message message, SendMessage sendMessage, AbsSender sender) {

    }
}
