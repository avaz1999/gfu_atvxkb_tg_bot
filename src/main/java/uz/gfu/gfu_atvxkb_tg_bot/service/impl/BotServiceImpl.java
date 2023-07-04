package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

import static uz.gfu.gfu_atvxkb_tg_bot.GfuAtvxkbTgBotApplication.bot;

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
    public void updateReceived(Update update) {
        if (update.hasMessage()) {
            updateHasMessage(update);
        }
        if (update.hasCallbackQuery()) {
            updateHasCallBackQuery(update);
        }
    }

    @SneakyThrows
    @Override
    public void updateHasMessage(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        String text = message.getText();

        BotUser currentUser = userService.getCurrentUser(chatId, message);

         if (text != null && text.equalsIgnoreCase(START)) {
            sendMessage.setText(ResMessageUz.HELLO + message.getFrom().getFirstName() + ResMessageUz.CHOOSE_LANG);
            sendMessage.setReplyMarkup(generalService.getInlineKeyboardButton(currentUser));
        } else {
            sendMessage.setText(ResMessageUz.CLICK_START);
            sendMessage.setReplyMarkup(generalService.getReplyKeyboard(currentUser));
        }

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
        clientService.clientHasCallBackQuery(currentUser, callbackQuery);
    }
}
