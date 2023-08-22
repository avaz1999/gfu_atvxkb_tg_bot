package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import uz.gfu.gfu_atvxkb_tg_bot.base.BaseEntity;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "feedbacks")
public class FeedBack extends BaseEntity {
    private String name;
    private boolean lang = false; // default uz
    private boolean edited = false;
    @OneToMany(mappedBy = "feedBack", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<SubFeedback> subFeedbacks;




}
