package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Entity(name = "buildings")
public class Building extends BaseEntity {
    private String name;
    @OneToMany(mappedBy = "building",cascade = CascadeType.ALL)
    private List<BotUser> botUserList;
}
