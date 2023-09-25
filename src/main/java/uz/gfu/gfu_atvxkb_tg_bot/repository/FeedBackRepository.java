package uz.gfu.gfu_atvxkb_tg_bot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack,Long> {
    List<FeedBack> findAllByLangFalseAndDeletedFalse();
    List<FeedBack> findAllByLangTrueAndDeletedFalse();
    FeedBack findByNameAndDeletedFalse(String subTitle);
    FeedBack findTopByNameAndDeletedFalse(String subTitle);

    FeedBack findByIdAndDeletedFalse(Long id);
    Boolean existsByNameAndDeletedFalse(String name);
    List<FeedBack> findAllByDeletedFalse();
    List<FeedBack> findAllByLangAndDeletedFalseOrderByCreatedAtDesc(boolean lang);
    FeedBack findByEditedTrueAndDeletedFalse();



}
