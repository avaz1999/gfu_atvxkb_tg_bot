package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;
import uz.gfu.gfu_atvxkb_tg_bot.enums.State;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    Application findByUserIdAndDoneAndDeletedFalse(Long userId, State state);
    List<Application> findAllByDoneAndDeletedFalse(State state);
}
