package uz.gfu.gfu_atvxkb_tg_bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.gfu.gfu_atvxkb_tg_bot.dto.SubFeedDto;
import uz.gfu.gfu_atvxkb_tg_bot.service.SubFeedbackService;

@RestController
@RequestMapping("/api/sub-feedback")
public class SubFeedbackController {
    private final SubFeedbackService subFeedbackService;

    public SubFeedbackController(SubFeedbackService subFeedbackService) {
        this.subFeedbackService = subFeedbackService;
    }
    @PostMapping
    public ResponseEntity<?> addSubFeedback(@RequestBody SubFeedDto dto){
        return ResponseEntity.ok(subFeedbackService.addFeedback(dto));
    }
}
