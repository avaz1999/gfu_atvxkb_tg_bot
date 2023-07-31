package uz.gfu.gfu_atvxkb_tg_bot.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gfu.gfu_atvxkb_tg_bot.base.BaseEntity;
import uz.gfu.gfu_atvxkb_tg_bot.enums.Role;
import uz.gfu.gfu_atvxkb_tg_bot.enums.UserState;

import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "users")
public class BotUser extends BaseEntity {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private Long chatId;
    private Long editMessageId;
    @Enumerated(EnumType.STRING)
    private UserState state;
    private int currentPage = 0;
    @ManyToMany
    @JoinTable(name = "users_feedbacks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "feedback_id"))
    private List<FeedBack> feedBacks;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    private Department department;
    @ManyToOne
    private Building building;

    public BotUser(String firstName, String lastName, Long chatId, UserState userState, Role role) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.chatId = chatId;
        this.state = userState;
        this.role = role;
    }
}
