package uz.gfu.gfu_atvxkb_tg_bot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack,Long> {
    FeedBack findBySubTitle(String subTitle);

    @Query(nativeQuery = true,value = "select sb.sub_title\n" +
            "from sub_feed_backs sb join users_feedbacks uf on sb.id = uf.feedback_id\n" +
            "join users u on u.id = uf.user_id where u.id=:userId")
    List<String> getSubTitleFromUser(Long userId);

}
