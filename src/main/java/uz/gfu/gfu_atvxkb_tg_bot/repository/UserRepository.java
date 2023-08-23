package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.enums.Role;

import java.util.List;

public interface UserRepository extends JpaRepository<BotUser,Long> {
     BotUser findByChatIdAndDeletedFalse(Long chatId);
     List<BotUser> findAllByRoleAndDeletedFalse(Role role);
     @Query("select u from users u where u.phoneNumber = ?1 and u.deleted = false")
     BotUser findByPhoneNumberAndDeletedFalse(String phoneNumber);
     BotUser findByEditedTrueAndDeletedFalse();
     Boolean existsByPhoneNumberAndDeletedFalse(String phoneNumber);
}
