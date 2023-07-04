package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gfu.gfu_atvxkb_tg_bot.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "sub_feedback")
public class SubFeedback extends BaseEntity {
    private String name;
    @ManyToOne
    private FeedBack feedBack;
}
