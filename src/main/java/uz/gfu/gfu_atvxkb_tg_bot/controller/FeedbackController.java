package uz.gfu.gfu_atvxkb_tg_bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.gfu.gfu_atvxkb_tg_bot.dto.FeedbackDto;
import uz.gfu.gfu_atvxkb_tg_bot.service.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody FeedbackDto feedbackDto) {
        return ResponseEntity.ok(feedbackService.addFeedback(feedbackDto));
    }
}
