package uz.gfu.gfu_atvxkb_tg_bot.payload;

public interface ResMessageUz {
    String START = "<b>Ассалому алейкум\nГеология фанлари университетининг\n" +
            "Ахборота Техникаларига Хизмат Кўрсатиш\n" +
            "Бўтига хуш келибсиз</b>";

    String ERROR_MESSAGE = "<b>Хато хабар киритдингиз</b>";
    String FEEDBACK = "<b>Ўзингизнинг таклифингизни ёзинг</b>";
    String ENTER_NAME = "<b>Илтимос исмингизни киритинг: </b>";
    String ENTER_DEPARTMENT = "<b>Бўлимингизни киритинг:</b>";
    String ENTER_ROOM_NUMBER = "<b>Хона рақамини киритинг:</b>";
    String ENTER_PHONE_NUMBER = "<b>Илтимос ички телефон рақам\n" +
            "Ёки шахсий телефон рақамингизни киритинг</b>";
    String SHOW_DATA = "<b>Ҳаммаси мувофақиятли амалга оширилди\" +\n" +
            "\"Маълумотларингиз тўғри эканлигини текширинг\n\n</b>";
    String ENTER_LASTNAME = "<b>Илтимос фамилянгизни киритинг</b>";
    String DONE = "<b>Рўйҳатдан ўтиш\nМувофақиятли якуналнди ✅\nХизмат турини танланг</b>";
    String EDIT = "<b>Қайтадан киритинг</b>";
    String SUCCESS = "<b>Мувофақиятли амалга ошди ✅\nХодимларимиз сиз билан сиз билан боғланади \uD83D\uDC68\u200D\uD83D\uDCBB\n" +
            "Агар яна сўров юбормоқчи бўлсангиз \nСервиcлардан бирини танланг \uD83D\uDCC3\n</b>";
    String CHOOSE_LANG = "<b>\nБотга хуш келибсиз\nИлтимос тилни танланг</b>";
    String CHOOSE_SERVICE = "<b>Сервислардан бирини танланг\n\nАгар бу ерда муамонгиз кўрсатилмаган бўлса\n\nБошқа: тугмасини босинг</b>";
    String ENTER_BLOCK = "<b>Блокизи танланг</b>";
    String DONE_SERVICE = "<b>Ҳаммаси ўз ўрнида бўлса тасдиқлаш тугмасини босинг\n\n</b>";
    String OTHER_PHONE_NUMBER = "<b>Бошқа фойдаланувчини телефон рақамини кирита олмайсиз</b>";
    String ERROR_CHOOSE_LANG = "<b>Хато хабар киритдингиз! Тилни Танланг\" +\n" +
            "\"Вы ввели неверное сообщение! Выберите язык</b>";
    String ERROR_BUILD_NAME = "<b>Киритилган бино топилмади</b>";
        String ERROR_SERVICE = "Хато хизмат турини танлдиз";
    String ADMIN_CRUD_SERVICE = "<b>Ўзгартирмоқчи бўлган хизмат турини танланг \uD83D\uDDD2</b>";
    String BUILDING_CRUD = "<b>Бинога тегишли ўзгаришларни танланг</b>";
    String FEEDBACK_CRUD = "<b>Сервиcе га тегишли ўзгаришларни танланг</b>";
    String SUB_FEEDBACK_CRUD = "<b>Сервиcе тури га тегишли ўзгаришларни танланг</b>";
    String ADMIN_CRUD = "<b>Админга тегишли хизматни танланг</b>";
    String ADD_BUILDING_NAME = "<b>Янги бино номини киритинг</b>";
    String EXIST_BUILDING_NAME = "<b>Бундай бино номи мавжуд</b>";
    String SUCCESS_ADD_BUILDING = "<b>Мувофақиятли қўшилди\n\n</b>";
    String EDIT_BUILDING = "<b>Ўзгартирмоқчи бўлган бинони танланг\n\n</b>";
    String ENTER_NEW_BLOCK = "<b>\nЯнги бино номини киритинг</b>";
    String SUCCESS_EDITED = "<b>Мувофақиятли ўзгартирилди\n\n</b>";
    String REMOVE_BUILDING = "<b>Ўчирмоқчи бўлган бинони танланг\n\n</b>";
    String DELETED_SUCCESS = "<b>Мувофақиятли ўчирилди</b>";
    String ALL_BUILDING = "<b>Базада мавжуд ҳамма бинолар рўйхати\n\n</b>";
    String BACK = "<b>Сервиcелардан бирини танланг</b>";
    String ENTER_NEW_ADMIN_PHONE_NUMBER = "<b>Янги админнинг телефон рақамини киритинг (9989********) шу кўринишда</b>";
    String ERROR_PHONE_NUMBER = "<b>Хато телефон рақам киритдингиз:\nНамунага эътибор қилинг</b>";
    String CREATED_NEW_ADMIN = "<b>Янги Админ яратилди</b>";
    String YOUR_ADMIN = "<b>Табриклаймиз сиз Админ бўлдингиз:</b>";
    String EDIT_ADMIN = "<b>Ўзгартимоқчи бўлган\nАдминингизни танланг\n</b>";
    String ADD_NEW_FEEDBACK = "<b>Яратмоқчи бўлган сервиcингизни тилини танланг</b>";
    String EXIST_FEEDBACK_NAME = "<b>Бундай Сервиcе мавжуд</b>";
    String PLEASE_ENTER_RUSSIAN = "<b>Мувофақиятли сақланди\nИлтимос русчасини ҳам қўшинг\nРус тилида фойдаланувчилар учун</b>";
    String SUCCESS_ADD_FEEDBACK = "<b>Сервиcе мувофақиятли сақланди</b>";
    String REMOVE_FEEDBACK = "<b>Ўчирмоқчи бўлган сервиcенгизни танланг\n</b>";
    String UPDATE_FEEDBACK = "<b>Ўзгартирмоқчи бўлган сервиcеингизни танланг\n</b>";
    String ENTER_NEW_FEEDBACK = "<b>Янги Сервиcе номини киритинг\n</b>";
    String ALL_FEEDBACK = "<b>Ҳамма сервиcелар рўйхати</b>";
    String SUB_FEEDBACK_WITH_FEEDBACK = "<b>Ўзгартирмоқчи бўлган сервиcе турини танланг</b>";
    String ENTER_NEW_SUB_FEEDBACK = "<b>Янги Сервиcе турини киритинг</b>";
    String SUCCESS_ADD_SUB_FEEDBACK = "<b>Сервиcе тури мувофақиятли қўшилди</b>";
    String ERROR = "<b>ХАТОЛИК ЮЗ БEРДИ!</b>";
    String WAITING = "<b>Сиз бундай ариза йўлладингиз илтимос\nХодимларимизни кутинг\nБошқа турдаги ариза йўллашингиз мумкин</b>";
    String RESEND = "ҚАЙТА ЮБОРИШ";
    String NOT_ADMIN = "<b>Кечирасиз хозирда Админларимиз мавжуд эмас</b>";
    String CONNECT_THIS_MESSAGE = "<b>Бу сўров бириктитилган</b>";
    String TO_CARRY_OUT = "<b>Бу хизмат Амалга оширилган</b>";
    String CONNECT_ADMIN = "<b>Сизнинг Сўровингизни\nХодимимиз: </b>";
    String CONNECT_ADMIN2 = "<b> қабул қилди\nТез орада сиз билан богланади</b>";
    String SERVICE_DONE = "<b> : Сизнинг аризангизни кўриб чиқди ✅\nСифатни яхшилаш учун баҳолашни унутманг \uD83D\uDCDD\nЯна сўровингиз бўлса юборинг</b>";
    String SUCCESS_DONE = "<b>Ҳаммасини мувофақиятли амалга оширган бўлсангиз\nКейинги вазифаларни бажаринг</b>";
    String SUCCESS_ADMIN_DONE = "<b>: Ходим вазифани мувофақиятли якунлади</b>";
    String ERROR_ADMIN_IN_PROSES = "<b>Кечирасиз сиз Бажарилди тугмасини боса олмайсиз\nСиз аввал Бажарилмоwда тугмасини босинг</b>";
    String SORRY_FAILED = "<b>Узур сиз йўллаган ариза мувофақиятсиз якунланди\nБошқа ходимларимиз томонидан кўриб чиқилади</b>";
    String FAILED_SERVICE = "<b>Ушбу сўров </b>";
    String FAILED_SERVICE2 = "<b> томонидан амалга оширилмади\n</b>";
    String ERROR_STATE_SERVICE = "<b>Бажарилмади ни босишдан олдин уриниб кўринг</b>";
    String RATE_SUCCESS = "<b>Баҳолаганингиз учун раҳмат\nСаволларингиз бўлса йўлланг</b>";

    String ERROR_START = "<b>Хизматдан фойдаланиш учун /start ни босинг</b>";
    String REMOVE_FEEDBACK_LANG = "<b>Ўчирмоқчи бўлган сервиcеингизни тилини танланг</b>";
    String FEEDBACK_IS_EMPTY = "<b>Хозирда сервиcелар мажуд эмас</b>";
    String GET_FEEDBACK_LANG = "<b>Сервиcеларни кўриш учун тилни танланг</b>";
    String GET_ALL_FEEDBACK_BY_LANG = "<b>Танлаган тилингизга тегишли барча сервиcелар\n</b>";
    String EDIT_SERVICE_LANG = "<b>Ўзгартирмоқчи бўлган сервиcингизни тилини танланг\n</b>";
    String ADD_NEW_SUB_FEEDBACK = "<b>Яратмоқчи бўлган муамо нинг тилини танланг</b>";
    String EDIT_NEW_SUB_FEEDBACK = "<b>Ўзгартирмоқчи бўлган муамо нинг тилини танланг</b>";
    String GET_NEW_SUB_FEEDBACK = "<b>Тилни танланг</b>";
    String REMOVE_NEW_SUB_FEEDBACK = "<b>Тилни танланг</b>";
    String GET_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG = "<b>Муамо учун мавжуд бўлган ҳамма сервиcер\n</b>";
    String ADD_ALL_FEEDBACK_FOR_SUB_FEEDBACK_BY_LANG = "<b>Қўшмоқчи бўлган сервиcингизни танланг\n</b>";
    String CHOOSE_EDIT_SUB_FEEDBACK = "<b>Ўзгартирмоқчи бўлган сервиcингизни танланг\n</b>";
    String EXIST_SUB_FEEDBACK = "<b>Бундай муамо тури мавжуд\n</b>";
    String CHOOSE_REMOVE_SUB_FEEDBACK = "<b>Ўчирмоқчи бўлган сервисингизни танланг\n</b>";
    String ADD_SUPER_ADMIN = "ADD SUPER ADMIN";
    String ADD_ADMIN = "ADD ADMIN";
    String STATISTIC_MONTH = "ОЙЛИК СТАТИСТИК";
}
