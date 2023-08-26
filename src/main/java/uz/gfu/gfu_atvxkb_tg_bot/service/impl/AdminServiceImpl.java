package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
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
    public void adminHasMessage(BotUser admin, String message, SendMessage sendMessage, AbsSender sender) {
        switch (admin.getState()) {
            case ADMIN_FOR_FEEDBACK -> adminForFeedbackState(message, admin, sendMessage, sender);

        }
    }


    @Override
    public void adminForFeedbackState(String message, BotUser admin, SendMessage sendMessage, AbsSender sender) {
        String error = admin.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.ERROR_MESSAGE
                : ResMessageRu.ERROR_MESSAGE;
        sendMessage.setChatId(admin.getChatId());
        sendMessage.setText(error);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void callAdminService(BotUser currentUser, Message message, SendMessage sendMessage, AbsSender sender) {
        adminHasMessage(currentUser, message.getText(), sendMessage, sender);
    }

    @Override
    public void callAdminHasCallBackQuery(BotUser admin, CallbackQuery callbackQuery, AbsSender sender) {
        String data = callbackQuery.getData();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        switch (admin.getState()) {
            case ADMIN_FOR_FEEDBACK -> adminInWork(admin, sendMessage, data, sender);
        }

    }

    private void adminInWork(BotUser admin, SendMessage sendMessage, String data, AbsSender sender) {
        String[] split = data.split("#");
        Long applicationId = Long.valueOf(split[1]);
        switch (split[0]) {
            case BotQuery.ADMIN_DONE -> adminDone(admin, applicationId, sendMessage, sender);
            case BotQuery.ADMIN_IN_PROCESS -> adminInProses(admin, applicationId, sendMessage, sender);
            case BotQuery.ADMIN_FAILED -> adminFailed(admin, applicationId, sendMessage, sender);
        }
    }

    private void adminFailed(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender) {
    }

    private void adminInProses(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender) {
        userService.adminInProses(admin, applicationId, sendMessage, sender);
    }

    private void adminDone(BotUser admin, Long applicationId, SendMessage sendMessage, AbsSender sender) {
    }
}
