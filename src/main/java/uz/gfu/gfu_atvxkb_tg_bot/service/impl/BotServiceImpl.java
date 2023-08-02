package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

@Service
public class BotServiceImpl implements BotService {
    private final UserService userService;


    private final SuperAdminService superAdminService;
    private final ClientService clientService;
    private final AdminService adminService;
    private static final String START = "/start";
    private final GeneralService generalService;

    public BotServiceImpl(UserServiceImpl userService, SuperAdminService superAdminService, ClientService clientService, AdminService adminService, GeneralService generalService) {
        this.userService = userService;
        this.superAdminService = superAdminService;
        this.clientService = clientService;
        this.adminService = adminService;
        this.generalService = generalService;
    }

    @Override
    public void updateReceived(Update update, AbsSender sender) {
        if (update.hasMessage()) {
            updateHasMessage(update, sender);
        }
        if (update.hasCallbackQuery()) {
            updateHasCallBackQuery(update,sender);
        }
    }

    @SneakyThrows
    @Override
    public void updateHasMessage(Update update,AbsSender sender) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        String text = message.getText();

        BotUser currentUser = userService.register(chatId, message);

        switch (currentUser.getRole()) {
            case CLIENT -> clientService.clientHasMessage(currentUser, message, sendMessage,sender);
            case ADMIN -> adminService.callAdminService(currentUser, message, sendMessage,sender);
            case SUPER_ADMIN -> superAdminService.superAdminHasMessage(currentUser, message, text, sendMessage);
        }
    }

    @Override
    public void updateHasCallBackQuery(Update update, AbsSender sender) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        BotUser currentUser = userService.register(chatId, callbackQuery.getMessage());
        clientService.clientHasCallBackQuery(currentUser, callbackQuery,sender);
    }
}
