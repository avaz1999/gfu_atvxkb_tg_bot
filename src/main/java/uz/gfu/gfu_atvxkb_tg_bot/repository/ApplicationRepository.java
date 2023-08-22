package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    Application findByUserIdAndFinishedFalseAndDeletedFalse(Long userId);
    List<Application> findAllByFinishedFalseAndDeletedFalse();
}
