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
            "Если вы хотите отправить запрос повторно, \nВыберите один из Сервисов \uD83D\uDCC3\n</b>";
    String ADMIN_CRUD_SERVICE = "<b>Выберите тип услуги, которую вы хотите изменить \uD83D\uDDD2</b>";
    String BUILDING_CRUD = "<b>Выберите изменения, связанные со зданием</b>";
    String FEEDBACK_CRUD = "<b>Выберите изменения, связанные с Сервисом</b>";
    String SUB_FEEDBACK_CRUD = "<b>Выберите изменения, связанные с типом услуги</b>";
    String ADMIN_CRUD = "<b>Выберите сервис, принадлежащий администратору</b>";
    String ADD_BUILDING_NAME = "<b>Введите название здание</b>";
    String EXIST_BUILDING_NAME = "<b>У такого здания есть имя</b>";
    String SUCCESS_ADD_BUILDING = "<b>Добавлено успешно\n\n</b>";
    String ENTER_NEW_BLOCK = "\nВведите название блок";
    String SUCCESS_EDITED = "Изменено успешно\n\n";
    String DELETED_SUCCESS = "<b>Удалено успешно</b>";
    String ALL_BUILDINGS = "<b>Список всех зданий, имеющихся в базе данных\n\n</b>";
    String BACK = "<b>Выберите одну из услуг</b>";
    String ENTER_NEW_ADMIN_PHONE_NUMBER = "<b>Введите номер телефона нового администратора (9989********) такой образом.</b>";
    String ERROR_PHONE_NUMBER = "<b>Вы ввели неверный номер телефона:\nОбратите внимание на шаблон</b>";
    String CREATED_NEW_ADMIN = "<b>Создан новый админ</b>";
    String YOUR_ADMIN = "<b>Поздравляем, вы стали администратором</b>";
    String START_WORK = "Начать работа";
    String ADD_NEW_FEEDBACK = "<b>Выберите язык сервиса, который вы хотите создать.</b>";
    String EXIST_FEEDBACK_NAME = "<b>такая услуга есть</b>";
    String PLEASE_ENTER_RUSSIAN = "<b>Успешно сохранено\nПожалуйста, добавьте русский язык тоже\nДля пользователей на русском</b>";
    String SUCCESS_ADD_FEEDBACK = "<b>Услуга успешно сохранена</b>";
    String REMOVE_FEEDBACK = "<b>Выберите услугу, которую хотите отключить\n</b>";
    String UPDATE_FEEDBACK = "<b>Выберите услугу, которую хотите изменить\n</b>";
    String ENTER_NEW_FEEDBACK = "<b>Введите новое имя службы\n</b>";
    String ALL_FEEDBACK = "<b>Список всех услуг</b>";
    String SUB_FEEDBACK_WITH_FEEDBACK = "<b>Выберите тип услуги, которую вы хотите изменить</b>";
    String ENTER_NEW_SUB_FEEDBACK = "<b>Введите новый тип услуги</b>";
    String SUCCESS_ADD_SUB_FEEDBACK = "<b>Тип службы успешно добавлен</b>";
    String ERROR = "<b>ПРОИЗОШЛА ОШИБКА!</b>";
    String WAITING = "<b>Вы отправили такую заявку, пожалуйста\nЖдите наших сотрудников\nВы можете отправить другой тип заявки</b>";
    String RESEND = "ОТПРАВИТЬ";
    String NOT_ADMIN = "<b>К сожалению, на данный момент свободных администраторов нет.</b>";
    String TO_CARRY_OUT = "<b>Данная услуга реализована</b>";
    String CONNECT_ADMIN = "<b>Ваш запрос принят нашими сотрудниками\n" +
            "Скоро свяжусь с вами</b>";
    String CONNECT_ADMIN2 = "<b> принял\nСвяжемся с вами в ближайшее время</b>";
    String SERVICE_DONE = "<b> Ваша заявка рассмотрена\nОцените для улучшения качества \uD83D\uDCDD\nЕсли у вас есть дополнительный запрос, отправьте его</b>";
    String SUCCESS_DONE = "<b>Если вы все сделали успешно\nВыполните следующие задачи</b>";
    String SUCCESS_ADMIN_DONE = "<b>: Сотрудник успешно выполнил задание</b>";
    String ERROR_ADMIN_IN_PROSES = "<b>Извините, вы не можете нажать Готово\nСначала необходимо нажать кнопку «Готово».</b>";
    String SORRY_FAILED = "<bИзвините, ваша заявка не удалась\nОно будет рассмотрено другими нашими сотрудниками.></b>";
    String FAILED_SERVICE = "<b>Этот запрос</b>";
    String FAILED_SERVICE2 = "<b> провалился\n</b>";
    String ERROR_STATE_SERVICE = "<b>Пожалуйста, попробуйте, прежде чем нажать «Не удалось».</b>";
    String RATE_SUCCESS = "<b>Thank you for your rating\nIf you have any questions, send them</b>";
    String REMOVE_FEEDBACK_LANG = "<b>Выберите язык услуги, которую хотите изменить.</b>";
    String FEEDBACK_IS_EMPTY = "<b>Услуги в настоящее время недоступны</b>";
    String GET_FEEDBACK_LANG = "<b>Выберите язык для просмотра услуг</b>";
    String GET_ALL_FEEDBACK_BY_LANG = "<b>Все услуги на выбранном вами языке\n</b>";
    String EDIT_SERVICE_LANG = "<b>Выберите язык услуги, которую хотите изменить\n</b>";
    String ADD_NEW_SUB_FEEDBACK = "<b>Выберите язык проблемы, которую вы хотите создать.</b>";
    String EDIT_NEW_SUB_FEEDBACK = "<b>Выберите язык проблемы, которую вы хотите изменить.</b>";
    String GET_NEW_SUB_FEEDBACK = "<b>Выберите язык</b>";
    String REMOVE_NEW_SUB_FEEDBACK = "<b>Выберите язык</b>";
    String GET_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG = "<b>Все сервисы доступны для решения проблемы\n</b>";
    String ADD_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG = "<b>Выберите услугу, которую хотите добавить\n</b>";
    String CHOOSE_EDIT_SUB_FEEDBACK = "<b>Ўзгартирмоқчи бўлган сервиcингизни танланг\n</b>";
    String EXIST_SUB_FEEDBACK = "<b>Такая проблема существует\n</b>";
    String CHOOSE_REMOVE_SUB_FEEDBACK = "<b>Выберите услугу, которую хотите удалить\n</b>";
    String ADD_SUPER_ADMIN = "ADD SUPER ADMIN";
    String ADD_ADMIN = "ADD ADMIN";
    String STATISTIC_MONTH = "ЕЖЕМЕСЯЧНАЯ СТАТИСТИКА";
}

