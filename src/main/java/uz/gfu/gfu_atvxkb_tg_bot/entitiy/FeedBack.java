package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gfu.gfu_atvxkb_tg_bot.base.BaseEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "sub_feed_backs")
public class FeedBack extends BaseEntity {
    private String subTitle;
    private List<String> messages;
}
