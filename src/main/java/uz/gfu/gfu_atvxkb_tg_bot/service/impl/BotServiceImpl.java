package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

@Service
public class BotServiceImpl implements BotService {
    private final UserService userService;


    private final SuperAdminService superAdminService;
    private final ClientService clientService;
    private final AdminService adminService;
    private static final String START = "/start";
    private static final String FEEDBACK = "Takliflar";

    public BotServiceImpl(UserServiceImpl userService, SuperAdminService superAdminService, ClientService clientService, AdminService adminService) {
        this.userService = userService;
        this.superAdminService = superAdminService;
        this.clientService = clientService;
        this.adminService = adminService;
    }

    @Override
    public void updateReceived(Update update) {
        if (update.hasMessage()) {
            updateHasMessage(update);
        }
        if (update.hasCallbackQuery()) {
            updateHasCallBackQuery(update);
        }
    }

    @Override
    public void updateHasMessage(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        String text = message.getText();

        BotUser currentUser = userService.getCurrentUser(chatId, message);

        switch (currentUser.getRole()) {
            case CLIENT -> clientService.clientHasMessage(currentUser, message, text, sendMessage);
            case ADMIN -> adminService.adminHasMessage(currentUser, message, text, sendMessage);
            case SUPER_ADMIN -> superAdminService.superAdminHasMessage(currentUser, message, text, sendMessage);
        }
    }

    @Override
    public void updateHasCallBackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        BotUser currentUser = userService.getCurrentUser(chatId, callbackQuery.getMessage());
        clientService.clientHasCallBackQuery(currentUser,callbackQuery);
    }
}
