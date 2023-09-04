package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageRu;
import uz.gfu.gfu_atvxkb_tg_bot.payload.ResMessageUz;
import uz.gfu.gfu_atvxkb_tg_bot.service.DeveloperService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;


@Service
public class DeveloperServiceImpl implements DeveloperService {
    private final GeneralService generalService;

    public DeveloperServiceImpl(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Override
    public void developerService(BotUser developer, Message message, SendMessage sendMessage, AbsSender sender) {
        if (message.hasText()) {
            switch (developer.getState()) {
                case DEVELOPER_STATE -> checkDeveloperState(developer,message,sendMessage,sender);
            }
        }else {
            errorMessage(developer,sendMessage);
        }
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
        String text = message.getText();
        if (text.equalsIgnoreCase("/developer")){
            String msg = developer.getLanguage().equals(BotQuery.UZ_SELECT)
            ? ResMessageUz.CHOOSE_SERVICE
            : ResMessageRu.CHOOSE_SERVICE;
            sendMessage.setText(msg);
            sendMessage.setReplyMarkup(generalService.crudForDeveloper(developer));
        }
    }
    private void errorMessage(BotUser developer, SendMessage sendMessage) {
        String msg = developer.getLanguage().equals(BotQuery.UZ_SELECT)
                ? ResMessageUz.ERROR_MESSAGE
                : ResMessageRu.ERROR_MESSAGE;
        sendMessage.setChatId(developer.getChatId());
        sendMessage.setText(msg);
    }
}
