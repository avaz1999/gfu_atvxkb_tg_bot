package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;

import java.util.List;

public interface SubFeedbackRepository extends JpaRepository<SubFeedback,Long> {
    List<SubFeedback> findAllByFeedBackIdAndDeletedFalse(Long feedbackId);
    SubFeedback findByNameAndDeletedFalse(String name);
    SubFeedback findByNameIsNullAndDeletedFalse();
    Boolean existsByNameAndDeletedFalse(String name);
    List<SubFeedback> findAllByFeedBackAndDeletedFalseOrderByCreatedAt(FeedBack feedBack);
    SubFeedback findByEditedTrueAndDeletedFalse();
    List<SubFeedback> findAllByLangAndDeletedFalse(Boolean lang);
    @Query(nativeQuery = true,
            value = "SELECT * FROM sub_feedback where name is null and deleted = false")
    SubFeedback findNameNull();
}
