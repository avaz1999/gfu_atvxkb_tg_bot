package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface UserRepository extends JpaRepository<BotUser,Long> {
     BotUser findByChatIdAndDeletedFalse(Long chatId);
}
