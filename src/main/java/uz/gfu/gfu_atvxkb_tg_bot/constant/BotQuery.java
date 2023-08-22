package uz.gfu.gfu_atvxkb_tg_bot.constant;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public interface BotQuery {
    String UZ_SELECT = "UZ_SELECT";
    String RU_SELECT = "RU_SELECT";
    String DONE = "DONE";
    String EDIT = "EDIT";
    String ADMIN_IN_PROCESS = "ADMIN_IN_PROCESS";
    String ADMIN_FAILED = "ADMIN_FAILED";
    String ADMIN_DONE = "ADMIN_DONE";
    String GET_SERVICE = "❗️ XIZMAT TURINI TANLANG";
    String OTHER_UZ = "Boshqa";
    String OTHER_RU = "Другой";
    String GET_SERVICE_RU = "️❗️ ВЫБЕРИТЕ ВИД УСЛУГИ";
    String SETTING = "/setting";
    String ADD_BUILDING = "✅ Здание";
    String ADD_FEEDBACK = "✅ Услуга";
    String ADD_SUB_FEEDBACK = "✅ Услуга типа";
    String ADD_ADMIN = "✅ Админ";
    String CRUD_BUILDING = "\uD83C\uDFE2 Здание";
    String CRUD_FEEDBACK = "\uD83D\uDEE0 Услуга";
    String CRUD_SUB_FEEDBACK = "\uD83D\uDD28 Услуга типа";
    String CRUD_ADMIN = "\uD83E\uDDD1\u200D\uD83D\uDCBB Админ";
    String REMOVE_BUILDING = "❌ Здание";
    String UPDATE_BUILDING = "✏️ Здание";
    String ALL_BUILDING = "\uD83E\uDDFE Все здание";
    String MENU = "\uD83D\uDCCC MENU";
    String REMOVE_FEEDBACK = "❌ Услуга";
    String ALL_FEEDBACK = "\uD83E\uDDFE Все Услуга";
    String UPDATE_FEEDBACK = "✏️ Услуга";
    String REMOVE_SUB_FEEDBACK = "❌ Услуга типа";
    String ALL_SUB_FEEDBACK = "\uD83E\uDDFE Услуга типа";
    String UPDATE_SUB_FEEDBACK = "✏️ Услуга типа";
    String REMOVE_ADMIN = "❌ Админ";
    String ALL_ADMIN = "\uD83E\uDDFE Все Админ";
    String UPDATE_ADMIN = "✏️ Админ";
    String BACK = "⬅️ Назад";
    String START_WORK_UZ = "ISHNI BOSHLASH";
    String START_WORK_RU = "НАЧАТЬ РАБОТА";
}
