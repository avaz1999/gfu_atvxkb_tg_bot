package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;
import uz.gfu.gfu_atvxkb_tg_bot.enums.State;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    @Query("select a from application a where a.userId = ?1 and a.done = ?2 and a.deleted = false")
    Application findByUserIdAndDoneAndDeletedFalse(Long userId, State state);
    Application findBySubFeedbackNameAndDoneAndUserIdAndDeletedFalse(String data, State state,Long userId);
    List<Application> findAllByDoneAndDeletedFalse(State state);

    Boolean existsBySubFeedbackNameAndDoneAndUserIdAndDeletedFalse(String data, State state,Long userId);

    @Query("""
            select a from application a
            where a.userId = ?1 and a.done = ?2 and a.deleted = false and a.subFeedbackName is null""")
    Application findByUserIdAndDoneAndDeletedFalseAndSubFeedbackNameIsNull(Long userId, State state);

}
