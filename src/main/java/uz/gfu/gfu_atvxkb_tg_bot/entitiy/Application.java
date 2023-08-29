package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gfu.gfu_atvxkb_tg_bot.base.BaseEntity;
import uz.gfu.gfu_atvxkb_tg_bot.enums.State;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "application")
public class Application extends BaseEntity {
    private Long userId;
    private Long adminId;
    private String feedbackName;
    private String subFeedbackName;
    private String buildingName;
    private Long departmentId;
    @Enumerated(EnumType.STRING)
    private State done;
    private Byte rate;
    private boolean complete = false;
}
