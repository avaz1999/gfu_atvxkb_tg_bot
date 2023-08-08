package uz.gfu.gfu_atvxkb_tg_bot.service.impl;

import org.springframework.context.annotation.Lazy;
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
import uz.gfu.gfu_atvxkb_tg_bot.service.BuildingService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;
import uz.gfu.gfu_atvxkb_tg_bot.service.SubFeedbackService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralServiceImpl implements GeneralService {

    private final FeedbackServiceImpl feedbackService;
    private final BuildingService buildingService;
    @Lazy
    private final SubFeedbackService subFeedbackService;

    public GeneralServiceImpl(FeedbackServiceImpl feedbackService, BuildingService buildingService, SubFeedbackService subFeedbackService) {
        this.feedbackService = feedbackService;
        this.buildingService = buildingService;
        this.subFeedbackService = subFeedbackService;
    }

    @Override
    public ReplyKeyboard getChooseLang() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(lists);

        InlineKeyboardButton uz = new InlineKeyboardButton();
        uz.setText("\uD83C\uDDFA\uD83C\uDDFF Uz");
        uz.setCallbackData(BotQuery.UZ_SELECT);
        row.add(uz);

        InlineKeyboardButton rus = new InlineKeyboardButton();
        rus.setText("\uD83C\uDDF7\uD83C\uDDFA Ru");
        rus.setCallbackData(BotQuery.RU_SELECT);
        row.add(rus);
        lists.add(row);
        return inlineKeyboardMarkup;
    }

    @Override
    public ReplyKeyboard getBlock() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        KeyboardRow row = new KeyboardRow();

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

    @Override
    public ReplyKeyboard getPhoneNumber(BotUser client) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = getSharePhoneKeyBoardButton(client);

        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    private static KeyboardRow getSharePhoneKeyBoardButton(BotUser client) {
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton sharePhoneNumberButton = new KeyboardButton();
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
            sharePhoneNumberButton.setText("☎️ Telefon raqam ulashish");
        } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
            sharePhoneNumberButton.setText("☎️ Поделитесь номером телефона");
        }
        sharePhoneNumberButton.setRequestContact(true);
        keyboardRow.add(sharePhoneNumberButton);
        return keyboardRow;
    }

    @Override
    public ReplyKeyboard getRegisterDone(BotUser client) {
        Result result = getResult();
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
            result.inlineKeyboardButtonSave().setText("✅ Tasdiqlash");
            result.inlineKeyboardButtonSave().setCallbackData(BotQuery.DONE);
            result.inlineKeyboardButtonList().add(result.inlineKeyboardButtonSave());

            InlineKeyboardButton inlineKeyboardButtonCancel = new InlineKeyboardButton();
            inlineKeyboardButtonCancel.setText("❌ Tahrirlash");
            inlineKeyboardButtonCancel.setCallbackData(BotQuery.EDIT);
            result.inlineKeyboardButtonList().add(inlineKeyboardButtonCancel);
            result.lists().add(result.inlineKeyboardButtonList());
        } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
            result.inlineKeyboardButtonSave().setText("✅ Подтверждение");
            result.inlineKeyboardButtonSave().setCallbackData(BotQuery.DONE);
            result.inlineKeyboardButtonList().add(result.inlineKeyboardButtonSave());

            InlineKeyboardButton inlineKeyboardButtonCancel = new InlineKeyboardButton();
            inlineKeyboardButtonCancel.setText("❌ Редактирование");
            inlineKeyboardButtonCancel.setCallbackData(BotQuery.EDIT);
            result.inlineKeyboardButtonList().add(inlineKeyboardButtonCancel);
            result.lists().add(result.inlineKeyboardButtonList());
        }

        return result.inlineKeyboardMarkup;
    }

    @Override
    public ReplyKeyboard getFeedbacks(BotUser client) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        KeyboardRow menu = new KeyboardRow();
        KeyboardButton menuButton = new KeyboardButton();
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)){
            menuButton.setText(BotQuery.GET_SERVICE);
        } else if (client.getLanguage().equals(BotQuery.RU_SELECT)) {
            menuButton.setText(BotQuery.GET_SERVICE_RU);
        }
        menu.add(menuButton);
        keyboardRows.add(menu);
        KeyboardRow row = new KeyboardRow();
        List<FeedBack> allFeedbackUz = feedbackService.getAllFeedbackUZ();
        List<FeedBack> allFeedbackRu = feedbackService.getAllFeedbackRU();
        KeyboardButton button;
        if (client.getLanguage().equals(BotQuery.UZ_SELECT)) {
            button = new KeyboardButton(BotQuery.OTHER_UZ);
            row = getKeyboardButtonsByLang(keyboardRows, row, allFeedbackUz);
        } else {
            button = new KeyboardButton(BotQuery.OTHER_RU);
            row = getKeyboardButtonsByLang(keyboardRows, row, allFeedbackRu);
        }
        row.add(button);
        return replyKeyboardMarkup;
    }

    private KeyboardRow getKeyboardButtonsByLang(List<KeyboardRow> keyboardRows, KeyboardRow row, List<FeedBack> allFeedbackUz) {
        for (int i = 0; i < allFeedbackUz.size(); i++) {
            FeedBack feedBack = allFeedbackUz.get(i);
            KeyboardButton button = new KeyboardButton(feedBack.getName());
            row.add(button);
            if (i % 2 == 0) {
                keyboardRows.add(row);
            } else {
                row = new KeyboardRow();
            }
        }
        return row;
    }

    @Override
    public ReplyKeyboard getSubFeedbacks(String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(lists);
        List<SubFeedback> allSubFeedbacks = subFeedbackService.findAllFeedback(text);
        for (int i = 0; i < allSubFeedbacks.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            SubFeedback subFeedback = allSubFeedbacks.get(i);
            button.setText(subFeedback.getName());
            button.setCallbackData(subFeedback.getName());
            inlineKeyboardButtonList1.add(button);
            if (i % 2 != 0) {
                lists.add(inlineKeyboardButtonList1);
                inlineKeyboardButtonList1 = new ArrayList<>();
            }
        }
        return inlineKeyboardMarkup;
    }

    @Override
    public ReplyKeyboard serviceDone() {
        Result result = getResult();

        // "Bajarildi" tugmasini yaratish
        InlineKeyboardButton inlineKeyboardButtonSave = new InlineKeyboardButton();
        inlineKeyboardButtonSave.setText("✅ Bajarildi");
        inlineKeyboardButtonSave.setCallbackData(BotQuery.ADMIN_DONE);
        result.inlineKeyboardButtonList().add(inlineKeyboardButtonSave);

        // "Bajarilmadi" tugmasini yaratish
        InlineKeyboardButton inlineKeyboardButtonCancel = new InlineKeyboardButton();
        inlineKeyboardButtonCancel.setText("❌ Bajarilmadi");
        inlineKeyboardButtonCancel.setCallbackData(BotQuery.ADMIN_FAILED);
        result.inlineKeyboardButtonList().add(inlineKeyboardButtonCancel);

        // Buttonlar ro'yxatini "lists" ga qo'shish
        result.lists().add(result.inlineKeyboardButtonList());

        // "Bajarilmoqda" tugmasini yangi qator (pastga) qo'shish
        InlineKeyboardButton inlineKeyboardButtonInProcess = new InlineKeyboardButton();
        inlineKeyboardButtonInProcess.setText("⚠️ Bajarilmoqda");
        inlineKeyboardButtonInProcess.setCallbackData(BotQuery.ADMIN_IN_PROCESS);

        // "Bajarilmoqda" tugmasini alohida qator (pastga) qo'shish
        List<InlineKeyboardButton> inProcessRow = new ArrayList<>();
        inProcessRow.add(inlineKeyboardButtonInProcess);
        result.lists().add(inProcessRow);

        // "Bajarildi" tugmasini "Save" deb yangilab olish
        result.inlineKeyboardButtonSave().setText("✅ Bajarildi");
        result.inlineKeyboardButtonSave().setCallbackData(BotQuery.ADMIN_DONE);

        return result.inlineKeyboardMarkup();
    }

    @Override
    public ReplyKeyboard getSettingForSuperAdmin(BotUser superAdmin) {
        return null;
    }

// getResult() va Result klasslarini oldin bir xil qoldirish mumkin

// ...

    private record Result(InlineKeyboardMarkup inlineKeyboardMarkup,
                          List<InlineKeyboardButton> inlineKeyboardButtonList, List<List<InlineKeyboardButton>> lists,
                          InlineKeyboardButton inlineKeyboardButtonSave) {
    }


    private static Result getResult() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(lists);
        InlineKeyboardButton inlineKeyboardButtonSave = new InlineKeyboardButton();
        return new Result(inlineKeyboardMarkup, inlineKeyboardButtonList, lists, inlineKeyboardButtonSave);
    }


}