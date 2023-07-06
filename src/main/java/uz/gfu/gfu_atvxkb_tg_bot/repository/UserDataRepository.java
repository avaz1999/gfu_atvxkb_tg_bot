package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.UserData;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
    UserData findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
