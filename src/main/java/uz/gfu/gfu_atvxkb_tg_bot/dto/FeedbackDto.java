package uz.gfu.gfu_atvxkb_tg_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackDto {
    private Long id;
    private String title;
    private List<String> message;
}
