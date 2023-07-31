package uz.gfu.gfu_atvxkb_tg_bot.bot;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.gfu.gfu_atvxkb_tg_bot.service.BotService;

@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    BotService botService;

    public Bot(@Value("${bot.token}") String token) {
        super(token);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        botService.updateReceived(update,this);
    }

    @Override
    public String getBotUsername() {
        return "myFirstBotAvaz_bot";
    }

}
