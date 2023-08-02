package uz.gfu.gfu_atvxkb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;

import java.util.List;

public interface SubFeedbackRepository extends JpaRepository<SubFeedback,Long> {
    List<SubFeedback> findAllByFeedBackIdAndDeletedFalse(Long feedbackId);
    SubFeedback findByNameAndDeletedFalse(String name);
    SubFeedback findByIdAndDeletedFalse(Long id);
}
