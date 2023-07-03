package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Entity(name = "department")
public class Department extends BaseEntity {
        private String name;
        private String innerPhoneNumber;
        private String roomNumber;
        @OneToMany(mappedBy = "department",cascade = CascadeType.ALL)
        private List<BotUser> userList;
        @OneToOne
        private Building building;
}
