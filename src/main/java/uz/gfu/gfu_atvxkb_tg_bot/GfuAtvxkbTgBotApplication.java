package uz.gfu.gfu_atvxkb_tg_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.gfu.gfu_atvxkb_tg_bot.bot.Bot;

@SpringBootApplication
public class GfuAtvxkbTgBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(GfuAtvxkbTgBotApplication.class, args);

    }
    public static Bot bot = new Bot("6093951131:AAHD4uFn3oo1rrz9JvkBTN_Bv6lmGbR50OU");

}
