package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.bot.Bot;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.service.AdminService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.UserService;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final GeneralService generalService;

    public AdminServiceImpl(UserService userService, GeneralService generalService) {
        this.userService = userService;
        this.generalService = generalService;
    }

    @Override
    public void adminHasMessage(BotUser admin, String message, SendMessage sendMessage,AbsSender sender) {
        switch (admin.getState()) {
            case ADMIN_FOR_FEEDBACK -> shareAdminMessage(message,admin.getChatId(),sendMessage,sender);
        }
    }

    @Override
    public void adminHasCallBackQuery(BotUser admin, CallbackQuery callbackQuery) {

    }

    @Override
    public void shareAdminMessage(String message,Long chatId,SendMessage sendMessage,AbsSender sender) {
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(generalService.serviceDone());
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void callAdminService(BotUser currentUser, Message message, SendMessage sendMessage, AbsSender sender) {
        adminHasMessage(currentUser,message.getText(),sendMessage,sender);
    }
}
