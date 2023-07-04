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
import uz.gfu.gfu_atvxkb_tg_bot.repository.FeedBackRepository;
import uz.gfu.gfu_atvxkb_tg_bot.repository.UserRepository;
import uz.gfu.gfu_atvxkb_tg_bot.service.BuildingService;
import uz.gfu.gfu_atvxkb_tg_bot.service.GeneralService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GeneralServiceImpl implements GeneralService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final FeedbackServiceImpl feedbackService;
    private final BuildingService buildingService;

    public GeneralServiceImpl(FeedBackRepository feedBackRepository, UserRepository userRepository, FeedbackServiceImpl feedbackService, BuildingService buildingService) {
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
        this.feedbackService = feedbackService;

        this.buildingService = buildingService;
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
            case 0 -> {
                KeyboardButton start = new KeyboardButton("/start");
                row.add(start);
                keyboardRows.add(row);
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
                    if (i % 2 == 0){
                        keyboardRows.add(row);
                    }else {
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
        List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(lists);
        if (currentUser.getCurrentPage() == 8) {
            InlineKeyboardButton other = new InlineKeyboardButton();
            InlineKeyboardButton back = new InlineKeyboardButton();
            back.setText("◀\uFE0F Back");
            back.setCallbackData(BotQuery.BACK);
            inlineKeyboardButtonList2.add(back);
            switch (text) {
                case "\uD83C\uDF10 Internet":
                    InlineKeyboardButton internetSpeed = new InlineKeyboardButton();
                    internetSpeed.setText("⚡\uFE0F Tezlik past");
                    internetSpeed.setCallbackData(BotQuery.SPEED);
                    inlineKeyboardButtonList1.add(internetSpeed);

                    InlineKeyboardButton settingInternet = new InlineKeyboardButton();
                    settingInternet.setText("\uD83D\uDD27 Ishlamayabti");
                    settingInternet.setCallbackData(BotQuery.SETTING);
                    inlineKeyboardButtonList1.add(settingInternet);

                    InlineKeyboardButton wifi = new InlineKeyboardButton();
                    wifi.setText("\uD83D\uDCF6 Wifi");
                    wifi.setCallbackData(BotQuery.WIFI);
                    inlineKeyboardButtonList2.add(wifi);

                    other.setText("\uD83D\uDCCE Boshqa");
                    other.setCallbackData(BotQuery.OTHER);
                    inlineKeyboardButtonList2.add(other);

                    lists.add(inlineKeyboardButtonList1);
                    lists.add(inlineKeyboardButtonList2);
                    break;
                case "\uD83D\uDDA8 Printer":
                    InlineKeyboardButton toner = new InlineKeyboardButton();
                    toner.setText("\uD83D\uDEE2 Zapravka");
                    toner.setCallbackData(BotQuery.ZAPRAVKA);
                    inlineKeyboardButtonList1.add(toner);

                    InlineKeyboardButton set = new InlineKeyboardButton();
                    set.setText("\uD83D\uDCE1 Ulash");
                    set.setCallbackData(BotQuery.CONNECT);
                    inlineKeyboardButtonList1.add(set);

                    InlineKeyboardButton setting = new InlineKeyboardButton();
                    setting.setText("\uD83D\uDEE0 Tuzatish");
                    setting.setCallbackData(BotQuery.SETTING);
                    inlineKeyboardButtonList2.add(setting);

                    other.setText("\uD83D\uDCCE Boshqa");
                    other.setCallbackData(BotQuery.OTHER);
                    inlineKeyboardButtonList2.add(other);

                    lists.add(inlineKeyboardButtonList1);
                    lists.add(inlineKeyboardButtonList2);
                    break;
                case "\uD83D\uDDA5 Kompyuter":
                    InlineKeyboardButton reinstall = new InlineKeyboardButton();
                    reinstall.setText("\uD83D\uDCC0 Pereustanovka");
                    reinstall.setCallbackData(BotQuery.REINSTALL);
                    inlineKeyboardButtonList1.add(reinstall);

                    InlineKeyboardButton diagnostic = new InlineKeyboardButton();
                    diagnostic.setText("⚙\uFE0F Diagnostika");
                    diagnostic.setCallbackData(BotQuery.DIAGNOSTIC);
                    inlineKeyboardButtonList1.add(diagnostic);

                    InlineKeyboardButton activationWindows = new InlineKeyboardButton();
                    activationWindows.setText("\uD83D\uDCBB Activatsiya Windows");
                    activationWindows.setCallbackData(BotQuery.ACTIVATION_WINDOWS);
                    inlineKeyboardButtonList2.add(activationWindows);

                    InlineKeyboardButton activationOffice = new InlineKeyboardButton();
                    activationOffice.setText("\uD83D\uDCC2 Activatsiya Office");
                    activationOffice.setCallbackData(BotQuery.ACTIVATION_OFFICE);
                    inlineKeyboardButtonList2.add(activationOffice);

                    other.setText("\uD83D\uDCCE Boshqa");
                    other.setCallbackData(BotQuery.OTHER);
                    inlineKeyboardButtonList2.add(other);


                    lists.add(inlineKeyboardButtonList1);
                    lists.add(inlineKeyboardButtonList2);
                    break;
                case "☎\uFE0F Telefon":

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
