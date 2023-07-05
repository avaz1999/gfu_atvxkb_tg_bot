package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.gfu.gfu_atvxkb_tg_bot.constant.BotQuery;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.Building;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.FeedBack;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.SubFeedback;
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.BuildingService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.SubFeedbackService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GeneralServiceImpl implements GeneralService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final FeedbackServiceImpl feedbackService;
    private final BuildingService buildingService;
    private final SubFeedbackService subFeedbackService;

    public GeneralServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, FeedbackServiceImpl feedbackService, BuildingService buildingService, SubFeedbackService subFeedbackService) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.feedbackService = feedbackService;
        this.buildingService = buildingService;
        this.subFeedbackService = subFeedbackService;
    }

    @Override
    public ReplyKeyboard getReplyKeyboard(BotUser user) {
        Optional<BotUser> byId = userRepository.findById(user.getId());
        if (byId.isEmpty()) {
            return null;
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        KeyboardRow row = new KeyboardRow();

        switch (user.getCurrentPage()) {
            case  2 -> {
                KeyboardButton button = new KeyboardButton(BotQuery.BACK);
                row.add(button);
                keyboardRows.add(row);
                return replyKeyboardMarkup;
            }
            case 3 -> {
                List<Building> allBuildings = buildingService.getAllBuildings();
                for (int i = 0; i < allBuildings.size(); i++) {
                    Building building = allBuildings.get(i);
                    KeyboardButton button1 = new KeyboardButton(building.getName());
                    row.add(button1);
                    if (i % 2 == 0) {
                        keyboardRows.add(row);
                    } else {
                        row = new KeyboardRow();
                    }
                }
                return replyKeyboardMarkup;
            }
            case 6 -> {
                KeyboardButton shareContactButton = new KeyboardButton("☎ Share contact");
                shareContactButton.setRequestContact(true);
                row.add(shareContactButton);
                keyboardRows.add(row);
                return replyKeyboardMarkup;
            }
            case 8 -> {
                List<FeedBack> allFeedback = feedbackService.getAllFeedback();
                for (int i = 0; i < allFeedback.size(); i++) {
                    FeedBack feedBack = allFeedback.get(i);
                    KeyboardButton button = new KeyboardButton(feedBack.getName());
                    row.add(button);
                    if (i % 2 == 0) {
                        keyboardRows.add(row);
                    } else {
                        row = new KeyboardRow();
                    }
                }
                return replyKeyboardMarkup;
            }
        }
        return null;
    }

    @Override
    public ReplyKeyboard getInlineKeyboardButton(BotUser currentUser) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(lists);
        switch (currentUser.getCurrentPage()) {
            case 1 -> {
                InlineKeyboardButton inlineKeyboardButtonUz = new InlineKeyboardButton();
                inlineKeyboardButtonUz.setText("\uD83C\uDDFA\uD83C\uDDFF Uz");
                inlineKeyboardButtonUz.setCallbackData(BotQuery.UZ_SELECT);
                inlineKeyboardButtonList1.add(inlineKeyboardButtonUz);
                InlineKeyboardButton inlineKeyboardButtonRus = new InlineKeyboardButton();
                inlineKeyboardButtonRus.setText("\uD83C\uDDF7\uD83C\uDDFA Ru");
                inlineKeyboardButtonRus.setCallbackData(BotQuery.RU_SELECT);
                inlineKeyboardButtonList1.add(inlineKeyboardButtonRus);
                lists.add(inlineKeyboardButtonList1);
            }
            case 7, 10 -> doneAndEdit(inlineKeyboardButtonList1, lists);
        }
        return inlineKeyboardMarkup;
    }

    @Override
    public ReplyKeyboard getInlineKeyboardButtonForService(BotUser currentUser, String text) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(lists);
        if (currentUser.getCurrentPage() == 9) {
            List<SubFeedback> allSubFeedbacks = subFeedbackService.findAllFeedback(text);
            for (int i = 0; i < allSubFeedbacks.size(); i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                SubFeedback subFeedback = allSubFeedbacks.get(i);
                button.setText(subFeedback.getName());
                button.setCallbackData(BotQuery.SUB_FEEDBACK);
                inlineKeyboardButtonList1.add(button);
                if (i % 2 != 0) {
                    lists.add(inlineKeyboardButtonList1);
                    inlineKeyboardButtonList1 = new ArrayList<>();
                }
            }
        }
        return inlineKeyboardMarkup;
    }

    private static void doneAndEdit(List<InlineKeyboardButton> inlineKeyboardButtonList, List<List<InlineKeyboardButton>> lists) {
        InlineKeyboardButton inlineKeyboardButtonSave = new InlineKeyboardButton();
        inlineKeyboardButtonSave.setText("✅ Tasdiqlash");
        inlineKeyboardButtonSave.setCallbackData(BotQuery.DONE);
        inlineKeyboardButtonList.add(inlineKeyboardButtonSave);

        InlineKeyboardButton inlineKeyboardButtonCancel = new InlineKeyboardButton();
        inlineKeyboardButtonCancel.setText("❌ Tahrirlash");
        inlineKeyboardButtonCancel.setCallbackData(BotQuery.EDIT);
        inlineKeyboardButtonList.add(inlineKeyboardButtonCancel);
        lists.add(inlineKeyboardButtonList);
    }

}
