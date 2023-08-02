package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gfu.gfu_atvxkb_tg_bot.base.BaseEntity;
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "histories")
public class History extends BaseEntity {
    private Long userId;
    private Long feedbackId;
    private Long subFeedbackId;
    private Long buildId;
    private Long departmentId;
    private Boolean finished = false;

    public History(Long userId, Long buildId) {
        this.userId = userId;
        this.buildId = buildId;
    }
}
