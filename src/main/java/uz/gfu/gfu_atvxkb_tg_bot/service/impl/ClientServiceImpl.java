package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.service.*;

import static uz.gfu.gfu_atvxkb_tg_bot.GfuAtvxkbTgBotApplication.bot;

@Service
public class ClientServiceImpl implements ClientService {
    private final UserService userService;
    private final UserRoundsService userRoundsService;

    public ClientServiceImpl(UserService userService, UserRoundsService userRoundsService) {
        this.userService = userService;
        this.userRoundsService = userRoundsService;
    }

    @Override
    public void clientHasMessage(BotUser client, Message message, String text, SendMessage sendMessage) {
        try {
            BotUser currentUser = userService.getCurrentUser(message);
            userRoundsService.getRound7(currentUser, message, sendMessage);
            checkCurrentUserPages(currentUser, sendMessage, text);
            if (sendMessage.getText().isEmpty())
                return;
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void checkCurrentUserPages(BotUser currentUser, SendMessage sendMessage, String text) {
        switch (currentUser.getCurrentPage()) {
            case 2 -> userRoundsService.getRound2(currentUser, text, sendMessage);
            case 3 -> userRoundsService.getRound3(currentUser, text, sendMessage);
            case 4 -> userRoundsService.getRound4(currentUser, text, sendMessage);
            case 5 -> userRoundsService.getRound5(currentUser, text, sendMessage);
            case 6 -> userRoundsService.getRound6(currentUser, text, sendMessage);
            case 9 -> userRoundsService.getRound9(currentUser, text, sendMessage);
        }
    }

    @Override
    public void clientHasCallBackQuery(BotUser client, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        switch (client.getCurrentPage()) {
            case 1 -> userRoundsService.getRound1(client, data, sendMessage);
            case 8 -> userRoundsService.getRound8(client, data, sendMessage);
            case 10 -> userRoundsService.getRound10(client, data, sendMessage);
            case 11 -> userRoundsService.getRound11(client, data, sendMessage);
        }
        try {
            sendMessage.setChatId(chatId);
            bot.execute(sendMessage);
            userService.nextPage(client);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
