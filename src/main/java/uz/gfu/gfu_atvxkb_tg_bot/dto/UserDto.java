package uz.gfu.gfu_atvxkb_tg_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private Long chatId;

}
