package uz.gfu.gfu_atvxkb_tg_bot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack,Long> {
    List<FeedBack> findAllByDeletedFalse();
    FeedBack findByNameAndDeletedFalse(String subTitle);



}
