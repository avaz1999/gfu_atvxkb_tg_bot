package uz.gfu.gfu_atvxkb_tg_bot.payload;

public interface ResMessageRu {
    String ENTER_LASTNAME = "<b>Пожалуйста, введите свою фамилию:</b>";
    String ERROR_MESSAGE = "<b>Вы ввели неверное сообщение</b>";
    String OTHER_PHONE_NUMBER = "<b>Вы не можете ввести номер телефона другого пользователя</b>";
    String ERROR_BUILD_NAME = "<b>Введенное здание не найдено</b>";
    String ENTER_NAME = "<b>Пожалуйста, введите Ваше имя</b>";
    String ENTER_BLOCK = "<b>Пожалуйста, выберите свой блок</b>";
    String ENTER_DEPARTMENT = "<b>Введите свой отдел</b>";
    String ENTER_ROOM_NUMBER = "\"<b>Введите номер комнаты\"</b>";
    String ENTER_PHONE_NUMBER = "\"<b>Введите свой номер телефона\"</b>";
    String SHOW_DATA = "<b>Все было сделано успешно\n" +
            "Проверьте правильность вашей информации\"</b>\n\n";
    String CHOOSE_SERVICE = "<b>Выберите одну из услуг\n"+
            "Если вашей проблемы нет в списке\n" +
            "Щелкните Другое:</b>";
    String DONE_SERVICE = "<b>Если все на месте, нажмите кнопку подтверждения</b>\n\n";
    String ERROR_SERVICE ="Вы выбрали неверный тип услуги" ;
    String DONE = "<b>Постановка на учет\n" +
            "Успешно завершено ✅\n" +
            "Выберите тип услуги</b>";
    String SUCCESS = "<b>Успешное завершение ✅\n Наши сотрудники свяжутся с вами \uD83D\uDC68\u200D\uD83D\uDCBB\n" +
            "Если вы хотите отправить запрос повторно, \nВыберите один из Сервисов \uD83D\uDCC3</b>";
}

