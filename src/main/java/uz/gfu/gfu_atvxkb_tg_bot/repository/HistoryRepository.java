package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.History;

public interface HistoryRepository extends JpaRepository<History,Long> {
    History findByUserIdAndFinishedFalseAndDeletedFalse(Long userId);
}
