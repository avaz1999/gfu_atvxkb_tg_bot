package uz.gfu.gfu_atvxkb_tg_bot.payload;

public interface ResMessageUz {
    String START = "Assalomu aleykum Geologiya fanlari universitetining ATM bo'limiga murojaat yuborish bo'limiga hush kelibsiz";

    String ERROR_MESSAGE = "<b>Xato xabar kiritdingiz</b>";
    String FEEDBACK = "<b>O'zingizning taklifingizni yozing</b>";
    String ENTER_NAME = "<b>Iltimos ismingizni kiriting: </b>";
    String ENTER_DEPARTMENT = "<b>Bo'limingizni kiriting:</b>";
    String ENTER_ROOM_NUMBER = "<b>Xona raqamini kiriting:</b>";
    String ENTER_PHONE_NUMBER = "<b>Iltimos ichki telefon raqam\nYoki shaxsiy telefon raqamingizni kiriting</b>";
    String SHOW_DATA = "<b>Hammasi muvofaqiyatli amalga oshirildi\n" +
            "Ma'lumotlaringiz to'g'ri ekanligini tekshiring\n\n</b>";
    String ENTER_LASTNAME = "<b>Iltimos familyangizni kiriting</b>";
    String DONE = "<b>Ro'yhatdan o'tish \nMuvofaqiyatli yakunalndi ✅ \nXizmat turini  tanlang</b>";
    String EDIT = "<b>Qaytadan kiriting</b>";
    String SUCCESS = "<b>Muvofaqiyatli amalga oshdi ✅\nXodimlarimiz siz bilan siz bilan bog'lanadi \uD83D\uDC68\u200D\uD83D\uDCBB\n" +
            "Agar yana so'rov yubormoqchi bo'lsangiz \nServiclardan birini tanlang \uD83D\uDCC3\n</b>";
    String SERVICE = "<b>Servislardan birini tanlang\n\nAgar bu yerda muamongiz ko'rsatilmagan bo'lsa\n\nBoshqa: tugmasini bosing</b>";
    String HELLO = "<b>Assalomu aleykum </b>\n";
    String CHOOSE_LANG = "<b>\nBotga xush kelibsiz\nIltimos tilni tanlang</b>";
    String CHOOSE_SERVICE = "<b>Servislardan birini tanlang\n\nAgar bu yerda muamongiz ko'rsatilmagan bo'lsa\n\nBoshqa: tugmasini bosing</b>";
    String CLICK_START = "<b>Iltimos /start bosing </b>";
    String ENTER_BLOCK = "<b>Blokizi tanlang</b>";
    String DONE_SERVICE = "<b>Hammasi o'z o'rnida bo'lsa tasdiqlash tugmasini bosing\n\n</b>";
    String OTHER_PHONE_NUMBER = "<b>Boshqa foydalanuvchini telefon raqamini kirita olmaysiz</b>";
    String ERROR_CHOOSE_LANG = "<b>Xato xabar kiritdingiz! Tilni Tanlang\n" +
            "Вы ввели неверное сообщение! Выберите язык</b>";
    String ERROR_BUILD_NAME = "<b>Kiritilgan bino  topilmadi</b>";
    String ERROR_SERVICE = "Xato xizmat turini tanldiz";
    String ADMIN_CRUD_SERVICE = "<b>O'zgartirmoqchi bo'lgan xizmat turini tanlang \uD83D\uDDD2</b>";
    String BUILDING_CRUD = "<b>Binoga tegishli o'zgarishlarni tanlang</b>";
    String FEEDBACK_CRUD = "<b>Service ga tegishli o'zgarishlarni tanlang</b>";
    String SUB_FEEDBACK_CRUD = "<b>Service turi ga tegishli o'zgarishlarni tanlang</b>";
    String ADMIN_CRUD = "<b>Adminga tegishli xizmatni tanlang</b>";
    String ADD_BUILDING_NAME = "<b>Yangi bino nomini kiriting</b>";
    String EXIST_BUILDING_NAME = "<b>Bunday bino nomi mavjud</b>";
    String SUCCESS_ADD_BUILDING = "<b>Muvofaqiyatli qo'shildi\n\n</b>";
    String EDIT_BUILDING = "<b>O'zgartirmoqchi bo'lgan binoni tanlang\n\n</b>";
    String ENTER_NEW_BLOCK = "<b>\nYangi bino nomini kiriting</b>";
    String SUCCESS_EDITED = "<b>Muvofaqiyatli o'zgartirildi\n\n</b>";
    String REMOVE_BUILDING = "<b>O'chirmoqchi bo'lgan binoni tanlang\n\n</b>";
    String DELETED_SUCCESS = "<b>Muvofaqiyatli o'chirildi</b>";
    String ALL_BUILDING = "<b>Bazada mavjud hamma binolar ro'yxati\n\n</b>";
    String BACK = "<b>Servicelardan birini tanlang</b>";
    String ENTER_NEW_ADMIN_PHONE_NUMBER = "<b>Yangi adminning telefon raqamini kiriting (9989********) shu ko'rinishda</b>";
    String ERROR_PHONE_NUMBER = "<b>Xato telefon raqam kiritdingiz:\nNamunaga e'tibor qiling</b>";
    String CREATED_NEW_ADMIN = "<b>Yangi Admin yaratildi</b>";
    String YOUR_ADMIN = "<b>Tabriklaymiz siz Admin bo'ldingiz:</b>";
    String START_WORK = "Ishni boshlash";
    String EDIT_ADMIN = "<b>O'zgartimoqchi bo'lgan \nAdminingizni tanlang\n</b>";
    String ADD_NEW_FEEDBACK = "<b>Yangi Service ni kiriting</b>";
    String EXIST_FEEDBACK_NAME = "<b>Bunday Service mavjud</b>";
    String PLEASE_ENTER_RUSSIAN = "<b>Muvofaqiyatli saqlandi \nIltimos ruschasini ham qo'shing \nRus tilida foydalanuvchilar uchun</b>";
    String SUCCESS_ADD_FEEDBACK = "<b>Service muvofaqiyatli saqlandi</b>";
    String REMOVE_FEEDBACK = "<b>O'chirmoqchi bo'lgan servicengizni tanlang\n</b>";
    String UPDATE_FEEDBACK = "<b>O'zgartirmoqchi bo'lgan serviceingizni tanlang</b>";
    String ENTER_NEW_FEEDBACK = "<b>Yangi Service nomini kiriting</b>";
    String ALL_FEEDBACK = "<b>Hamma servicelar ro'yxati</b>";
    String SUB_FEEDBACK_WITH_FEEDBACK = "<b>O'zgartirmoqchi bo'lgan service turini tanlang</b>";
    String ENTER_NEW_SUB_FEEDBACK = "<b>Yangi Service turini kiriting</b>";
    String SUCCESS_ADD_SUB_FEEDBACK = "<b>Service turi muvofaqiyatli qo'shildi</b>";
    String ERROR = "<b>XATOLIK YUZ BERDI!</b>";
    String WAITING = "<b>Siz bunday ariza yo'lladingiz iltimos \nXodimlarimizni kuting\nBoshqa turdagi ariza yo'llashingiz mumkin</b>";
    String RESEND = "QAYTA YUBORISH";
    String NOT_ADMIN = "<b>Kechirasiz xozirda Adminlarimiz mavjud emas</b>";
    String CONNECT_THIS_MESSAGE = "<b>Bu so'rov biriktitilgan</b>";
    String TO_CARRY_OUT = "<b>Bu xizmat Amalga oshirilgan</b>";
    String CONNECT_ADMIN = "<b>Sizning So'rovingizni \nXodimimiz: </b>";
    String CONNECT_ADMIN2 = "<b> qabul qildi\nTez orada siz bilan boglanadi</b>";
    String SERVICE_DONE = "<b> : Sizning arizangizni ko'rib chiqdi ✅\nSifatni yaxshilash uchun baholashni unutmang \uD83D\uDCDD \nYana so'rovingiz bo'lsa yuboring</b>";
    String SUCCESS_DONE = "<b>Hammasini muvofaqiyatli amalga oshirgan bo'lsangiz\nKeyingi vazifalarni bajaring</b>";
    String SUCCESS_ADMIN_DONE = "<b>: Xodim vazifani muvofaqiyatli yakunladi</b>";
    String ERROR_ADMIN_IN_PROSES = "<b>Kechirasiz siz Bajarildi tugmasini bosa olmaysiz\nSiz avval Bajarilmowda tugmasini bosing</b>";
}
