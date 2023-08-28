package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Application;
import uz.gfu.gfu_atvxkb_tg_bot.enums.State;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    Application findByIdAndDeletedFalse(Long id);
    List<Application> findAllByDoneAndDeletedFalse(State state);

    Boolean existsBySubFeedbackNameAndDoneAndUserIdAndDeletedFalse(String data, State state,Long userId);


    Application findByUserIdAndDoneAndDeletedFalseAndSubFeedbackName(
            Long userId,
            State state,
            String subFeedbackName);


    Application findTopByUserIdAndDoneAndDeletedFalseOrderByCreatedAtDesc(
            Long userId,
            State done);

    List<Application> findAllByAdminIdAndDeletedFalse(Long adminId);

    Application findByUserIdAndAdminIdAndDoneAndDeletedFalseOrderByCreatedAtDesc(Long clientId,Long adminId,State state);

    Application findTopByUserIdAndDoneAndSubFeedbackNameAndDeletedFalseOrderByCreatedAtDesc(
            Long userId,
            State state,
            String subFeedbackName);
    Application findTopByUserIdAndDoneAndDeletedFalseAndSubFeedbackNameIsNullOrderByCreatedAtDesc(
            Long userId,
            State state
    );
}
