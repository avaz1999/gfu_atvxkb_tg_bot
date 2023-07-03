package uz.gfu.gfu_atvxkb_tg_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.gfu.gfu_atvxkb_tg_bot.bot.Bot;

@Configuration
@ComponentScan(basePackages = "uz.gfu.gfu_atvxkb_tg_bot.service.impl")
public class BotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi(Bot bot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        return api;
    }

}
