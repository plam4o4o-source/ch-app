# Emulator check of the v1.2.36 candidate (80efffa, versionCode 1507400) against the released v1.2.35 (aef8910, 1507364)

debug/w22-check (runs in runs.txt, latest 43317392091 @ 9791f43). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU) unless the part says otherwise (parts ending in 28: API 28; in 26: API 26; c36: API 36). Built prodRelease (minified) x86_64 with a throwaway key; diary36: devDebug. In the A/B parts new = 1.2.36 and base = 1.2.35. Screenshots are JPEG, scaled down (sheets/ = contact sheets). Earlier: [../v135full/index.md](../v135full/index.md), [../v135/index.md](../v135/index.md).

| part | status |
|---|---|
| c26 | COMPLETE; part=c26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| c28 | COMPLETE; part=c28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| c36 | COMPLETE; part=c36 target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| diary36 | COMPLETE; part=diary36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| gfx36 | COMPLETE; part=gfx36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| gfxb36 | COMPLETE; part=gfxb36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| hi28 | COMPLETE; part=hi28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| idle33 | COMPLETE; part=idle33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mem36 | COMPLETE; part=mem36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| memb36 | COMPLETE; part=memb36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mon36 | COMPLETE; part=mon36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| new26 | COMPLETE; part=new26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| new28 | COMPLETE; part=new28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| new36 | COMPLETE; part=new36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| new36b | COMPLETE; part=new36b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| onb36 | COMPLETE; part=onb36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| start36a | COMPLETE; part=start36a target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| start36b | COMPLETE; part=start36b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| upd28 | COMPLETE; part=upd28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| upd36 | COMPLETE; part=upd36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| widget36 | COMPLETE; part=widget36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest)
devDebug 1.2.36 80efffa (org.chyavorec.app.dev, demo sign-in DEMO-0001)
1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest)
app.apk 4583798 bytes
app-base.apk 4317894 bytes
app-dev.apk 28595559 bytes
```

## c26

```
part c26; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 1771 ms
## C1. Tour of all screens on API 26
## T-api26-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · Празника „38 читалища. Един  · 27 септември 2026 · Сканирай · Читателска карта
CAP api26-light/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС
CAP api26-light/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=21 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api26-light/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
WARN: tab bar not reached
CAP api26-light/02-catalog: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Назад · Търси книга, новина, събитие · Сканирай баркод · Всички · Книги
CAP api26-light/02g-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api26-light/03-book: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api26-light/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api26-light/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
WARN: tab bar not reached
CAP api26-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP api26-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт · Моите книги
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api26-light/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api26-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api26-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api26-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-light/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api26-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api26-light/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-light/12-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-light/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Снимки от сайта · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1
CAP api26-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api26-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api26-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api26-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api26-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api26-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api26-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-light/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api26-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP api26-light/18c-settings-3: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията · Заемания, събития, съобщения
CAP api26-light/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi
CAP api26-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api26-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api26-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api26-light/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api26-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api26-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api26-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.36 (1507400) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api26-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api26-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api26-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api26-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api26-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api26-light: A11Y AGG: 51 screens analysed, 0 dumps failed; totals MISSING=8 DOUBLE=0 SAME-LABEL=3 SMALL(<48dp)=14
app theme → =Тъмна|=Dark: text="Изглед" text="Тема" text="Тъмна" text="Размер на текста" 
## T-api26-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · 27 септември 2026 · Моите книги · Читателска карта · Искам да прочета · Контакти
CAP api26-dark/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP api26-dark/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP api26-dark/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/02b-filters: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел
CAP api26-dark/02c-filters-2: targets=23 MISSING=2 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP api26-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api26-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api26-dark/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api26-dark/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api26-dark/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api26-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Незабравимият п · Още една · Към книгата · Затвори
CAP api26-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api26-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги
CAP api26-dark/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api26-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api26-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api26-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api26-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api26-dark/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/12-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-dark/12b-more-2: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Галерия · Снимки от сайта · Извези шевица · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник
CAP api26-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api26-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api26-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api26-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api26-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api26-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api26-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP api26-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP api26-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията
CAP api26-dark/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
CAP api26-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api26-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api26-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api26-dark/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api26-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api26-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api26-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.36 (1507400) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api26-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api26-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api26-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api26-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api26-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api26-dark: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=12 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=51
app theme → =Както системата|=System default: text="Изглед" text="Тема" text="Както системата" text="Размер на текста" 
A11Y AGG: 107 screens analysed, 0 dumps failed; totals MISSING=20 DOUBLE=0 SAME-LABEL=8 SMALL(<48dp)=65
== MISSING: 12 distinct
   View@[0,1906][1080,2032]  [1 screens: 02-catalog]
   View@[53,127][529,190]  [1 screens: 02c-filters-2]
   View@[550,127][929,190]  [1 screens: 02c-filters-2]
   View@[0,1917][1080,2043]  [1 screens: 08b-news-2]
   View@[121,2124][340,2274]  [1 screens: 11-event]
   View@[408,2124][597,2274]  [1 screens: 11-event]
   View@[665,2124][959,2274]  [1 screens: 11-event]
   View@[42,1764][524,1938]  [1 screens: 12-more]
   View@[556,1764][1038,1938]  [1 screens: 12-more]
   View@[0,1901][1080,2027]  [1 screens: 12b-more-2]
   View@[42,2256][524,2274]  [1 screens: 13-gallery]
   View@[556,2256][1038,2274]  [1 screens: 13-gallery]
== SMALL: 25 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   ''  [2 screens: 02c-filters-2, 13-gallery]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Всички'  [2 screens: 01d-home-4, 08b-news-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
   'Автор'  [1 screens: 02f-shelf-2]
## C2. Home ribbon and the card on API 26
IDLE card26-3-18s (15 s, no input): frames +0; cpu=4.1% (ticks 61 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=3.4%, DefaultExecutor=0.7%; pid 17300
CARD fullscreen on API 26: yes; app in front yes
IDLE cardfull26-30-45s (15 s, no input): frames +0; cpu=0.2% (ticks 3 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.2%; pid 17300
## C3. Surprise ×2, share a book as an image on API 26
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book26: chooser OPEN after 3 s; cache/share: 2198628 2026-11-12 22:49 chyavorec-book-399560-1794523771167.png;
SHARE book26 file: chyavorec-book-399560-1794523771167.png 2198628 B; PNG (1080, 1920) RGBA
SHARE book26 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794523771167.png mime=image/png bytes=2198628 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C5. Rotation and process death on API 26 (book, catalogue, article)
ROT book: pid 17300→17300 (same); landscape: kept 40% of 25 texts; lost e.g. ['Вид', 'Език', 'Отдел', 'книга']; back to portrait: kept 100% of 25 texts
PROCDEATH book: killed NO (17300); new pid 17300; restored: kept 100% of 25 texts; front app
ROT catalog: pid 17300→17300 (same); landscape: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият пътя', 'Белият дома', 'Мартин Тонев']; back to portrait: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият дома', 'Белият пътя', 'Елена Русева']
PROCDEATH catalog: killed NO (17300); new pid 17300; restored: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият дома', 'Белият пътя', 'Елена Русева']; front app
ROT article: pid 17300→17300 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed NO (17300); new pid 17300; restored: kept 100% of 9 texts; front app
## C5b. Scanner screen: manual entry and back ×2, repeated ×8 (the API 26 crash came right after this in round 1)
SCAN loop 1: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 2: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 3: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 4: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 5: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 6: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 7: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
SCAN loop 8: app in front yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" 
## C6. Monkey 2000 (seed 1357) on API 26
## W. First launch and warm-up
first-install catalogue results after 1411 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20041
MONKEY exit=0 duration=88 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20041'; airplane=0; camera: 
MEM after-monkey (raw) PSS=86794KB  Java=16880KB  Native=36796KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=?KB  Views=26 Activities=1
MEM after-monkey (trimmed) PSS=80082KB  Java=10944KB  Native=36232KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=?KB  Views=24 Activities=1
## C6. Monkey 2000 (seed 4242) on API 26
## W. First launch and warm-up
first-install catalogue results after 2471 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20602
MONKEY exit=0 duration=120 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20602'; airplane=0; camera: 
MEM after-monkey (raw) PSS=73871KB  Java=12928KB  Native=31056KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=?KB  Views=24 Activities=1
MEM after-monkey (trimmed) PSS=70767KB  Java=9648KB  Native=31092KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=?KB  Views=24 Activities=1
## C6. Monkey 2000 (seed 777) on API 26
## W. First launch and warm-up
first-install catalogue results after 2399 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=21115
MONKEY exit=0 duration=90 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '21115'; airplane=0; camera: 
MEM after-monkey (raw) PSS=85010KB  Java=14472KB  Native=36692KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=?KB  Views=15 Activities=1
MEM after-monkey (trimmed) PSS=78911KB  Java=8564KB  Native=36704KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=?KB  Views=15 Activities=1
contact sheets: 17 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: няма | Студен старт · 8 | Първи кадър: медиана 1086 мс, последен 1072 мс | Пълно показване: медиана 1486 мс, последно 1521 мс | Топъл старт · 4 | Първи кадър: медиана 145 мс, последен 194 мс | Насечени кадри по екрани | book:
DIAG SCREEN (scrolled): text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: няма" text="Студен старт · 8" text="Първи кадър: медиана 1086 мс, последен 1072 мс" text="Пълно показване: медиана 1486 мс, последно 1521 мс" text="Топъл старт · 4" text="Първи кадър: медиана 145 мс, последен 194 мс" text="Насечени кадри по екрани" text="book: 78,7 % от 89 кадъра · най-бавен 233 мс" text="общо с видеокартата: средно 184 мс, най-бавен 356 мс" text="card: 72,2 % от 36 кадъра · най-бавен 353 мс" text="общо с видеокартата: средно 271 мс, най-бавен 644 мс" text="catalog: 65,1 % от 175 ка
APP W/E lines (pids ['5162', '6171', '8335', '12353', '17300', '20041', '20602', '21115']): 1087 in 191 groups
      98  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
      91  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      89  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      86  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      82  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      20  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      20  W/GoogleApiManager: The service for mu# is not available: ss#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
      18  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      16  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      14  E/ResourceType: Style contains key with bad entry: #
      14  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      13  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      12  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      12  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      12  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      11  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      10  W/ConnectionStatusConfig: Dynamic lookup for intent failed for action: com.google.android.gms.chimera.container.moduleinstall.ModuleInst
      10  W/GmsClient: unable to connect to service: com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.STAR
       9  E/eglCodecCommon: glUtilsParamSize: unknow param #
       9  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
       9  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       9  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
       8  W/zygote64: Unexpected CPU variant for X# using defaults: x#_#
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
       7  W/BasicSecureTextField: 	at uk#.d(r#-map-id-ccf#fe#c#ee#d#b#f#db#cfd#ad#fa#c#f#ae#b#fc#:#)
       7  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       7  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       6  E/AndroidRuntime: 	at ld#.a(r#-map-id-ccf#fe#c#ee#d#b#f#db#cfd#ad#fa#c#f#ae#b#fc#:#)
contact sheets: 18 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5162', '6171', '8335', '12353', '17300', '20041', '20602', '21115']
== FATAL: 2
== ANR: 0
== StrictMode: 0
== Skipped: 14
   skipped-frames events=14 max=51 total=518
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 13710 B
```

## c28

```
part c28; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15982 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2090 ms
## C1. Tour of all screens on API 28
## T-api28-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api28-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api28-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · Празника „38 читалища. Един  · 27 септември 2026 · Сканирай · Читателска карта · Искам да прочета
CAP api28-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | 16 · НОЕ · Организацията на ООН за обра · БИБЛИОТЕКА · 21 · НОЕ
CAP api28-light/01d-home-4: targets=15 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP api28-light/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api28-light/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел · Философия. Психология
CAP api28-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api28-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api28-light/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api28-light/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-light/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Камъкът на смър · Още една · Към книгата · Затвори
CAP api28-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP api28-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт · Моите книги
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api28-light/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api28-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api28-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api28-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-light/08b-news-2: targets=19 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=24 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api28-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api28-light/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-light/12-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api28-light/12b-more-2: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
CAP api28-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api28-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api28-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api28-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api28-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api28-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api28-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-light/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api28-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP api28-light/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията
CAP api28-light/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
CAP api28-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api28-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api28-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api28-light/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api28-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api28-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api28-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.36 (1507400) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api28-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api28-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api28-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api28-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api28-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api28-light: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=13 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=49
app theme → =Тъмна|=Dark: text="Изглед" text="Тема" text="Тъмна" text="Размер на текста" 
## T-api28-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api28-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api28-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · Празника „38 читалища. Един  · 27 септември 2026 · Моите книги · Читателска карта
CAP api28-dark/01c-home-3: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Моите книги · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP api28-dark/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP api28-dark/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api28-dark/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api28-dark/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=2 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел · Философия. Психология
CAP api28-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api28-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api28-dark/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-dark/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Море от обърнат · Още една · Към книгата · Затвори
CAP api28-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api28-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги
CAP api28-dark/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api28-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api28-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api28-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-dark/08b-news-2: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api28-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api28-dark/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-dark/12-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api28-dark/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Снимки от сайта · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1
CAP api28-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api28-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api28-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api28-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api28-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api28-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api28-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-dark/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP api28-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP api28-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията
CAP api28-dark/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
CAP api28-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api28-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api28-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api28-dark/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api28-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api28-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api28-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.36 (1507400) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api28-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api28-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.36 (1507400)
CAP api28-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api28-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api28-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api28-dark: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=9 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=52
app theme → =Както системата|=System default: text="Изглед" text="Тема" text="Както системата" text="Размер на текста" 
A11Y AGG: 113 screens analysed, 0 dumps failed; totals MISSING=22 DOUBLE=0 SAME-LABEL=10 SMALL(<48dp)=101
== MISSING: 14 distinct
   View@[0,1906][1080,2032]  [1 screens: 02-catalog]
   View@[0,1934][1080,2060]  [1 screens: 08b-news-2]
   View@[121,2124][340,2274]  [1 screens: 11-event]
   View@[408,2124][597,2274]  [1 screens: 11-event]
   View@[665,2124][959,2274]  [1 screens: 11-event]
   View@[42,1764][524,1938]  [1 screens: 12-more]
   View@[556,1764][1038,1938]  [1 screens: 12-more]
   View@[42,2256][524,2274]  [1 screens: 13-gallery]
   View@[556,2256][1038,2274]  [1 screens: 13-gallery]
   View@[42,0][378,160]  [1 screens: 01d-home-4]
   View@[415,0][751,160]  [1 screens: 01d-home-4]
   View@[788,0][1080,160]  [1 screens: 01d-home-4]
   View@[0,1870][1080,1996]  [1 screens: 08b-news-2]
   View@[0,1898][1080,2024]  [1 screens: 12b-more-2]
== SMALL: 26 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Покажи 2446 резултата'  [1 screens: 02c-filters-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
## C2. Home ribbon and the card on API 28
IDLE card28-3-18s (15 s, no input): frames +0; cpu=1.5% (ticks 23 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=1.3%, DefaultExecutor=0.3%; pid 16769
CARD fullscreen on API 28: yes; app in front yes
IDLE cardfull28-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 16769
## C3. Surprise ×2, share a book as an image on API 28
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book28: chooser OPEN after 2 s; cache/share: 267515 2026-11-12 22:43 chyavorec-book-399560-1794523414157.png;
SHARE book28 file: chyavorec-book-399560-1794523414157.png 267515 B; PNG (1080, 1920) RGBA
SHARE book28 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794523414157.png mime=image/png bytes=267515 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C5. Rotation and process death on API 28 (book, catalogue, article)
ROT book: pid 16769→16769 (same); landscape: kept 40% of 25 texts; lost e.g. ['Вид', 'Език', 'книга', 'Отдел']; back to portrait: kept 100% of 25 texts
PROCDEATH book: killed yes; new pid 17769; restored: kept 100% of 25 texts; front app
ROT catalog: pid 17769→17769 (same); landscape: kept 63% of 35 texts; lost e.g. ['·', 'налична', 'Белият дома', 'Белият пътя']; back to portrait: kept 100% of 35 texts
PROCDEATH catalog: killed yes; new pid 18163; restored: kept 100% of 35 texts; front app
ROT article: pid 18163→18163 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed yes; new pid 18416; restored: kept 100% of 9 texts; front app
## C5b. Scanner screen: manual entry and back ×2, repeated ×8 (the API 26 crash came right after this in round 1)
SCAN loop 1: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 2: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 3: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 4: app in front NO; 
SCAN loop 5: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 6: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 7: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 8: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
## C6. Monkey 2000 (seed 1357) on API 28
## W. First launch and warm-up
first-install catalogue results after 1599 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19562
MONKEY exit=0 duration=105 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19562'; airplane=0; camera: 
MEM after-monkey (raw) PSS=79205KB  Java=13272KB  Native=38228KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=26 Activities=1
MEM after-monkey (trimmed) PSS=77300KB  Java=10664KB  Native=38852KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=26 Activities=1
## C6. Monkey 2000 (seed 4242) on API 28
## W. First launch and warm-up
first-install catalogue results after 1740 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20030
MONKEY exit=0 duration=91 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20030'; airplane=0; camera: 
MEM after-monkey (raw) PSS=86132KB  Java=15648KB  Native=41912KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=20 Activities=1
MEM after-monkey (trimmed) PSS=81521KB  Java=11464KB  Native=41608KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=20 Activities=1
## C6. Monkey 2000 (seed 777) on API 28
## W. First launch and warm-up
first-install catalogue results after 1947 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20481
MONKEY exit=0 duration=74 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20481'; airplane=0; camera: 
MEM after-monkey (raw) PSS=81650KB  Java=14024KB  Native=38588KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=19 Activities=1
MEM after-monkey (trimmed) PSS=76430KB  Java=9016KB  Native=38480KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=19 Activities=1
contact sheets: 16 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 10 | Първи кадър: медиана 1464 мс, последен 1513 мс | Пълно показване: медиана 1959 мс, последно 1947 мс | Топъл старт · 2 | Първи кадър: медиана 596 мс, последен 125 мс | Пълно показване: медиана 1464 м
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 10" text="Първи кадър: медиана 1464 мс, последен 1513 мс" text="Пълно показване: медиана 1959 мс, последно 1947 мс" text="Топъл старт · 2" text="Първи кадър: медиана 596 мс, последен 125 мс" text="Пълно показване: медиана 1464 мс, последно 1464 мс" text="Насечени кадри по екрани" text="card: 69,4 % от 36 кадъра · най-бавен 405 мс" text="общо с видеокартата: средно 310 мс, най-бавен 677 мс" text="book: 61,5 % от 65 кадъра · най-бавен 403 мс" text="общо с видеокартата: средно 197 мс, най-бавен 5
APP W/E lines (pids ['5480', '6228', '11577', '16769', '17769', '18163', '18416', '19562', '20030', '20481']): 783 in 117 groups
      38  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      35  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      31  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      27  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      26  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
      25  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      24  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      24  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      23  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      23  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      22  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      22  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      21  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      19  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      19  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      19  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      18  W/GoogleApiManager: The service for mu# is not available: ss#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
      15  E/eglCodecCommon: glUtilsParamSize: unknow param #
      14  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[
      13  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      13  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      12  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=-#.#, y
      10  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
      10  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      10  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
      10  W/ConnectionStatusConfig: Dynamic lookup for intent failed for action: com.google.android.gms.chimera.container.moduleinstall.ModuleInst
      10  W/GmsClient: unable to connect to service: com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.STAR
       6  W/BasicSecureTextField: 	at uk#.d(r#-map-id-ccf#fe#c#ee#d#b#f#db#cfd#ad#fa#c#f#ae#b#fc#:#)
       6  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
contact sheets: 17 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5480', '6228', '11577', '16769', '17769', '18163', '18416', '19562', '20030', '20481']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 21
   skipped-frames events=21 max=64 total=790
== Davey: 23
   davey events=23 max=1442ms median=890ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## c36

```
part c36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 6536 ms
## C1. Tour of all screens on API 36
## T-api36-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP api36-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api36-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта
CAP api36-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP api36-light/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=21 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api36-light/01e-search-12: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=36 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Търси книга, новина, събитие · Сканирай баркод · 13 ноември 2026 · *Св. Йоан  · Последни новини · БИБЛИОТЕКА
CAP api36-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api36-light/02c-filters-2: targets=28 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=30 ellipsized=0 | Манипулатор за преместване с · картографско издание · друго · електронен документ · графично издание · аудиодокумент
CAP api36-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api36-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api36-light/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api36-light/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-light/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Огънят на зимат · Още една · Към книгата · Затвори
CAP api36-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP api36-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api36-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api36-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api36-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api36-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-light/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api36-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api36-light/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-light/12-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api36-light/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=27 ellipsized=0 | Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта
CAP api36-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api36-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api36-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP api36-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP api36-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api36-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api36-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-light/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api36-light/18b-settings-2: targets=12 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=19 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP api36-light/18c-settings-3: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“
CAP api36-light/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP api36-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api36-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api36-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
WARN: settings item Избери известията|Choose notifications|Choose which not found
WARN: settings item =Политика за поверителност|=Privacy policy not found
WARN: settings item =Условия за използване|=Terms of use not found
WARN: settings item =За приложението|=About the app not found
WARN: settings item =Диагностика|=Diagnostics not found
CAP api36-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api36-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api36-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api36-light: A11Y AGG: 51 screens analysed, 0 dumps failed; totals MISSING=7 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=54
## T-api36-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP api36-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api36-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги · Читателска карта · Искам да прочета
CAP api36-dark/01c-home-3: targets=16 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP api36-dark/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА · Най търсени
CAP api36-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api36-dark/02c-filters-2: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел · Философия. Психология
CAP api36-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api36-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api36-dark/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-dark/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Забравеният спо · Още една · Към книгата · Затвори
CAP api36-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api36-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api36-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api36-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api36-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api36-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-dark/08b-news-2: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=28 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api36-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api36-dark/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-dark/12-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api36-dark/12b-more-2: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Галерия · Снимки от сайта · Извези шевица · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник
CAP api36-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api36-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api36-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP api36-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP api36-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api36-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api36-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-dark/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api36-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP api36-dark/18c-settings-3: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия
CAP api36-dark/18d-settings-4: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
CAP api36-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api36-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api36-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
WARN: settings item Избери известията|Choose notifications|Choose which not found
WARN: settings item =Политика за поверителност|=Privacy policy not found
WARN: settings item =Условия за използване|=Terms of use not found
WARN: settings item =За приложението|=About the app not found
WARN: settings item =Диагностика|=Diagnostics not found
CAP api36-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP api36-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api36-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api36-dark: A11Y AGG: 50 screens analysed, 0 dumps failed; totals MISSING=8 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=45
A11Y AGG: 101 screens analysed, 0 dumps failed; totals MISSING=15 DOUBLE=0 SAME-LABEL=10 SMALL(<48dp)=99
== MISSING: 13 distinct
   View@[42,0][777,3]  [1 screens: 01c-home-3]
   View@[809,0][1080,3]  [1 screens: 01c-home-3]
   View@[913,2051][1039,2096]  [1 screens: 08b-news-2]
   View@[0,2017][1080,2127]  [1 screens: 12b-more-2]
   View@[42,2321][524,2400]  [1 screens: 13-gallery]
   View@[556,2321][1038,2400]  [1 screens: 13-gallery]
   View@[0,2322][1080,2400]  [1 screens: 18c-settings-3]
   View@[0,2298][1080,2400]  [1 screens: 18d-settings-4]
   View@[42,296][356,325]  [1 screens: 18b-settings-2]
   View@[382,296][697,325]  [1 screens: 18b-settings-2]
   View@[723,296][1038,325]  [1 screens: 18b-settings-2]
   View@[0,296][1080,325]  [1 screens: 18c-settings-3]
   View@[0,2319][1080,2400]  [1 screens: 18c-settings-3]
== SMALL: 26 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Всички'  [2 screens: 01d-home-4, 08b-news-2]
   ''  [2 screens: 08b-news-2, 18b-settings-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
## C2. Home ribbon and the card on API 36
IDLE card36-3-18s (15 s, no input): frames +79; cpu=47.2% (ticks 708 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=46.6%, g.chyavorec.app=0.4%, binder:16017_3=0.1%, FrameMetricsAgg=0.1%, DefaultExecutor=0.1%; pid 16017
CARD fullscreen on API 36: yes; app in front yes
IDLE cardfull36-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 16017
## C3. Surprise ×2, share a book as an image on API 36
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book36: chooser OPEN after 4 s; cache/share: 921023 2026-11-12 22:51 chyavorec-book-399560-1794523907422.png;
SHARE book36 file: chyavorec-book-399560-1794523907422.png 921023 B; PNG (1080, 1920) RGBA
SHARE book36 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794523907422.png mime=image/png bytes=921023 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C4. Predictive back (gesture navigation) on API 36: „Изненадай ме“, a book, an article
gesture nav:  navigation_mode=2
enable_back_animation: null
PBACK surprise open: text="Още една" text="Към книгата" 
PBACK surprise after a cancelled gesture: still open (expected)
PBACK surprise after the committed gesture: closed; catalogue shown; in front app
PBACK surprise mid-gesture vs open: diff 2.27
PBACK book: after the gesture book screen STILL OPEN; in front app
PBACK article: closed; in front app
PBACK viewer: closed; in front app
PBACK on Home (root): in front after the gesture: com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
## C5. Rotation and process death on API 36 (book, catalogue, article)
ROT book: pid 16017→16017 (same); landscape: kept 40% of 25 texts; lost e.g. ['Вид', 'Език', 'книга', 'Отдел']; back to portrait: kept 96% of 25 texts; lost e.g. ['Последна синхронизация: Преди ']
PROCDEATH book: killed yes; new pid 18396; restored: kept 96% of 25 texts; lost e.g. ['Последна синхронизация: Преди ']; front app
ROT catalog: pid 18396→18396 (same); landscape: kept 71% of 34 texts; lost e.g. ['Белият пътя', 'Белият дома', 'Мартин Тонев', 'Ключова дума']; back to portrait: kept 100% of 34 texts
PROCDEATH catalog: killed yes; new pid 18811; restored: kept 100% of 34 texts; front app
ROT article: pid 18811→18811 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed yes; new pid 19097; restored: kept 100% of 9 texts; front app
## C6. Monkey 2000 (seed 1357) on API 36
## W. First launch and warm-up
first-install catalogue results after 3281 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19464
MONKEY exit=0 duration=153 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19464'; airplane=0; camera: 
MEM after-monkey (raw) PSS=80869KB  Java=14332KB  Native=42648KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=21)  RSS=198664KB  Views=26 Activities=1
MEM after-monkey (trimmed) PSS=81282KB  Java=12948KB  Native=40004KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=21)  RSS=198920KB  Views=15 Activities=1
## C6. Monkey 2000 (seed 4242) on API 36
## W. First launch and warm-up
first-install catalogue results after 2824 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20483
MONKEY exit=0 duration=151 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20483'; airplane=0; camera: 
MEM after-monkey (raw) PSS=76134KB  Java=16112KB  Native=32176KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=57)  RSS=194040KB  Views=27 Activities=1
MEM after-monkey (trimmed) PSS=78885KB  Java=15436KB  Native=30676KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=57)  RSS=197412KB  Views=27 Activities=1
## C6. Monkey 2000 (seed 777) on API 36
## W. First launch and warm-up
WARN: tab bar not reached
first-install: NO catalogue results
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
WARN: tab bar not reached
monkey start pid=22414
MONKEY exit=236 duration=75 s; ** Monkey aborted due to error. Events injected: 748 
MONKEY markers: CRASH=0 ANR=2 FATAL=0
pid after monkey: '22414'; airplane=0; camera: 
MEM after-monkey (raw) PSS=75438KB  Java=19768KB  Native=26564KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=29)  RSS=192840KB  Views=26 Activities=1
MEM after-monkey (trimmed) PSS=76042KB  Java=15880KB  Native=26220KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=29)  RSS=193800KB  Views=23 Activities=1
contact sheets: 19 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 10 | Първи кадър: медиана 2537 мс, последен 1690 мс | Пълно показване: медиана 3924 мс, последно 2193 мс | Топъл старт · 10 | Първи кадър: медиана 922 мс, последен 706 мс | Пълно показване: медиана 3141 
DIAG SCREEN (scrolled): text="Студен старт · 10" text="Първи кадър: медиана 2537 мс, последен 1690 мс" text="Пълно показване: медиана 3924 мс, последно 2193 мс" text="Топъл старт · 10" text="Първи кадър: медиана 922 мс, последен 706 мс" text="Пълно показване: медиана 3141 мс, последно 3021 мс" text="Насечени кадри по екрани" text="article: 20,6 % от 136 кадъра · най-бавен 2577 мс" text="общо с видеокартата: средно 599 мс, най-бавен 2811 мс" text="book: 5,8 % от 137 кадъра · най-бавен 2267 мс" text="общо с видеокартата: средно 617 мс, най-бавен 3571 мс" text="search: 4,2 % от 95 кадъра · най-бавен 692 мс" text="общо с видеокартата: средно 607 мс, най-бавен 1708 мс" text="catalog: 4,1 % от 242 кадъра · най-бавен 2444
APP W/E lines (pids ['3844', '5346', '11155', '16017', '18396', '18811', '19097', '19464', '20483', '22414']): 2525 in 216 groups
     640  W/Codec2Client: query -- param skipped: index = #.
     469  W/ColorUtils: expected specified color aspects (#:#:#:#)
     217  W/HWUI: Image decoding logging dropped!
     162  E/MediaCodec: Media Quality Service not found.
     162  W/CCodec: This behavior is subject to change. It is recommended that app developers double check whether the requested m
     162  E/g.chyavorec.app: Failed to query component interface for required system resources: #
      49  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
      41  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
      20  W/RemoteInputConnectionImpl: requestCursorUpdates on inactive InputConnection
      17  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@c#bbad#
      16  W/HWUI: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      16  W/HWUI: Failed to initialize #-# format, error = EGL_SUCCESS
      12  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#fa#b#d
      10  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      10  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, goldmont, goldmont-plus, goldmont-without-sha-xsaves, tremont, 
      10  E/g.chyavorec.app: Not starting debugger since process cannot load the jdwp agent.
      10  E/ashmem: Pinning is deprecated since Android Q. Please use trim or other methods.
      10  W/HWUI: Unknown dataspace #
       8  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=tv#@#e#
       8  W/BasicSecureTextField: 	at uk#.d(r#-map-id-ccf#fe#c#ee#d#b#f#db#cfd#ad#fa#c#f#ae#b#fc#:#)
       8  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       8  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, metaSt
       6  W/g.chyavorec.app: Suspending all threads took: #.#ms
       6  W/InteractionJankMonitor: Initializing without READ_DEVICE_CONFIG permission. enabled=true, interval=#, missedFrameThreshold=#, frameTim
       6  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       6  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@bfd#e#c
       5  W/g.chyavorec.app: Missing inline cache for java.lang.Object rn.s(java.lang.Object)
       5  W/FrameTracker: Missed App frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
       5  W/FrameTracker: Missed SF frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
contact sheets: 20 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['3844', '5346', '11155', '16017', '18396', '18811', '19097', '19464', '20483', '22414']
== FATAL: 0
== ANR: 2
== StrictMode: 0
== Skipped: 274
   skipped-frames events=274 max=396 total=14581
== Davey: 808
   davey events=808 max=7187ms median=1004ms
== NativeLink: 9
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 4 block(s) {'ANR': 4} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 11637 B
```

## diary36

```
part diary36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.36 80efffa (org.chyavorec.app.dev, demo sign-in DEMO-0001);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.36-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5057 ms
## D0. Demo sign-in (DEMO-0001 / demo)
Запомни ме before: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
Запомни ме now: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
MY after the demo sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" 
## D1. Моето and „История“ (reading diary prompts, the „Върната“ stamp)
CAP demo36/01-my: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=25 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · ДЧ · Демо Читател · Читателски № DEMO-0001 · Активно · БИБЛИОТЕКА
CAP demo36/01b-my-2: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=24 ellipsized=0 | Искам да прочета · Книги, които искаш да вземеш · Моето членство · Статус и валидност · Прочетени книги · Историята на четенето и подо
CAP demo36/01c-my-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=24 ellipsized=0 | Кои да получаваш: срокове, с · МОЕТО ЧЕТЕНЕ · 2026 · Моята читателска година 2026 · 30 книги · виж историята · Книги по месеци
CAP demo36/02-loans: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Последна синхронизация: Пред · Демо заглавие (просрочено) · Демо автор · Взета на 04.10.2026 · Срокът изтече на 10.11.2026
CAP demo36/03-history: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Всички години · 2026 · 2025 · 2026 · 30 книги
CAP demo36/03b-history-2: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · взета 10.11.2026 · върната 1 · Хареса ли ти? · Не сега · Оценка на книгата · Чичовци
CAP demo36/03c-history-3: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Железният светилник · Талев, Димитър · взета 28.10.2026 · върната 1 · Хареса ли ти? · Не сега
HISTORY: diary prompts '1', rating marks '0', stamp 'Върната' '0'
DIARY: no 'Препоръчай': text="ДЕМО ДАННИ — само за разработка, не са реални" text="Всички години" text="2026" text="2025" text="2026" text="30 книги" text="Под игото" text="Иван Вазов" text="взета 14.09.2026 · върната 14.10.2026" text="Демо заглавие (прочетено)" text="Демо автор" tex
DIARY: FATAL(any process)=0 of the app=0 ANR=0
## D2. The diary survives a process death; a book screen shows the diary section
CAP demo36/06-history-after-kill: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Всички години · 2026 · 2025 · 2026 · 30 книги
HISTORY after kill: rating state 'content-desc="Оценка на книгата" content-desc="Оценка на книгата" '
## D3. Year story: „Твоята шевица“ and the embroidered charts (portrait; dark)
YEAR port slide 1: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | През 2026 г. взе 30 книги от библиотекат · Моята читателска година · 2026 · Затвори · Сподели
  slide 1 after a slow drag up inside: diff 8.91
YEAR port slide 2: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Най-четен раздел: Език. Литература — 2 к · Моята читателска година · 2026 · Затвори · Сподели
  slide 2 after a slow drag up inside: diff 13.08
YEAR port slide 3: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Най-дълга поредица: 11 месеца подред с п · Моята читателска година · 2026 · Затвори · Сподели
  slide 3 after a slow drag up inside: diff 23.16
YEAR port slide 4: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Благодарим ти, че четеш с нас! Библиотек · Моята читателска година · 2026 · Затвори · Сподели
  slide 4 after a slow drag up inside: diff 0.14
YEAR port slide 5: YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Искам да прочета · Книги, които искаш да вземеш · Моето членство · Статус и валидност · Прочетени книги · Историята на четенето и подобни книги · Моят профил · Читателски данни
  slide 5 after a slow drag up inside: diff 0.00
  story closed at slide 5
YEAR dark slide 1: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | 2026 — твоята година с книгите · Моята читателска година · 2026 · Затвори · Сподели
  slide 1 after a slow drag up inside: diff 10.17
YEAR dark slide 2: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Твоят читателски тип: Книжен дракон. Пог · Моята читателска година · 2026 · Затвори · Сподели
  slide 2 after a slow drag up inside: diff 0.76
YEAR dark slide 3: YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Покажи картата на гишето · Искам да прочета · Книги, които искаш да вземеш · Моето членство · Статус и валидност · Прочетени книги · Историята на четенето и подобни книги · Моят профил
  slide 3 after a slow drag up inside: diff 0.01
YEAR dark slide 4: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | 2026 — твоята година с книгите · Моята читателска година · 2026 · Затвори · Сподели
  slide 4 after a slow drag up inside: diff 15.14
YEAR dark slide 5: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Любим автор: Иван Вазов — 6 книги през г · Моята читателска година · 2026 · Затвори · Сподели
  slide 5 after a slow drag up inside: diff 7.93
YEAR dark slide 6: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Рекорден месец: Септември — 5 книги. · Моята читателска година · 2026 · Затвори · Сподели
  slide 6 after a slow drag up inside: diff 15.52
YEAR dark slide 7: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Твоят читателски тип: Книжен дракон. Пог · Моята читателска година · 2026 · Затвори · Сподели
  slide 7 after a slow drag up inside: diff 29.10
YEAR dark slide 8: YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Прочетени книги · Историята на четенето и подобни книги · Моят профил · Читателски данни · Известия · Кои да получаваш: срокове, събития, нови · МОЕТО ЧЕТЕНЕ · 2026
  slide 8 after a slow drag up inside: diff 0.00
YEAR dark slide 9: YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Известия · Кои да получаваш: срокове, събития, нови · МОЕТО ЧЕТЕНЕ · 2026 · Моята читателска година 2026 · 30 книги · виж историята · Книги по месеци · Цялата история
  slide 9 after a slow drag up inside: diff 0.00
## D4. Signed-in card, fullscreen
demo fullscreen card: yes
## NP-demo-new (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP demo-new/01-home: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения (1 непрочетено) · 1 · Лента за търсене · Търси книга, новина, събитие
CAP demo-new/02-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP demo-new/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 16
CAP demo-new/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 16 → 6
EMB redo: 6 → 16
EMB save: text="Моите шевици" 
CAP demo-new/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP demo-new/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2579 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP demo-new/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.4
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2478 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP demo-new/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=225 std=29.4
CAP demo-new/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP demo-new/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP demo-new/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1983 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Първа копка" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Решение за
CAP demo-new/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP demo-new/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP demo-new/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
CAP demo-new/st-01: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP demo-new/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP demo-new/st-03: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP demo-new/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Обновления · Автоматичното обновяване раб · Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP demo-new/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP demo-new: FATAL(any process)=0 of the app=0 ANR=0; app process 10295; logcat app errors: 0
DIARY36 part: FATAL(any process)=0 of the app=0 ANR=0
A11Y AGG: 8 screens analysed, 0 dumps failed; totals MISSING=3 DOUBLE=0 SAME-LABEL=4 SMALL(<48dp)=4
== MISSING: 3 distinct
   View@[42,2010][524,2127]  [1 screens: 01-my]
   View@[42,296][524,302]  [1 screens: 01b-my-2]
   View@[556,296][1038,302]  [1 screens: 01b-my-2]
== SMALL: 2 distinct
   ''  [2 screens: 01-my, 01b-my-2]
   'Кои да получаваш: срокове, съб'  [1 screens: 01c-my-3]
== SAME-LABEL: 3 distinct
   'Не сега' x2  [2 screens: 03-history, 06-history-after-kill]
   'Удължи срока' x2  [1 screens: 02-loans]
   'Не сега' x3  [1 screens: 03b-history-2]
== DOUBLE: 0 distinct
contact sheets: 9 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36-dev (1) | Профил: чака | Студен старт · 3 | Първи кадър: медиана 4088 мс, последен 4088 мс | Пълно показване: медиана 5145 мс, последно 5145 мс | Насечени кадри по екрани | login: 16,7 % от 84 кадъра · най-бавен 504 мс | общо с видеокартата: сре
DIAG SCREEN (scrolled): text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36-dev (1)" text="Профил: чака" text="Студен старт · 3" text="Първи кадър: медиана 4088 мс, последен 4088 мс" text="Пълно показване: медиана 5145 мс, последно 5145 мс" text="Насечени кадри по екрани" text="login: 16,7 % от 84 кадъра · най-бавен 504 мс" text="общо с видеокартата: средно 181 мс, най-бавен 687 мс" text="my: 14,0 % от 107 кадъра · най-бавен 1109 мс" text="общо с видеокартата: средно 298 мс, най-бавен 1239 мс" text="onboarding: 2,6 % от 39 кадъра · най-бавен 857 мс" text="общо с видеокартата: средно 576 мс, най-бав
APP W/E lines (pids ['5570', '8558', '10295']): 251 in 110 groups
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       6  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       5  W/Parcel: Expecting binder but got null!
       5  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       5  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/ziparchive: Unable to open '/data/app/~~#JrFTam#qM#Cee#wNAERhw==/org.chyavorec.app.dev-#gCI#hPIAD#K#K#IqAMJbg==/base.dm': 
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       3  W/yavorec.app.dev: Unexpected CPU variant for x#: x#_#.
       3  W/yavorec.app.dev: Known variants: atom, sandybridge, silvermont, kabylake, default
       3  W/yavorec.app.dev: Accessing hidden method Landroid/os/SystemProperties;->addChangeCallback(Ljava/lang/Runnable;)V (unsupported, 
       3  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       3  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       3  W/Gralloc4: allocator #.x is not supported
       3  E/OpenGLRenderer: Unable to match the desired swap behavior.
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.InnerNodeCoordinator$LookaheadDelegateImpl.measure-BRTryo#(InnerNodeCoordinator.k
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasureBlock$lambda$#(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate$$ExternalSyntheticLambda#.invoke(D#$$SyntheticClass:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasure-BRTryo#$ui(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performLookaheadMeasure-BRTryo#$ui(LayoutNodeLayoutDeleg
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.remeasure-BRTryo#(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.measure-BRTryo#(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutModifierNodeCoordinator$LookaheadDelegateForLayoutModifierNode.measure-BRTr
       3  W/BasicSecureTextField: 	at androidx.compose.ui.graphics.BlockGraphicsLayerModifier.measure-#p#s#s(GraphicsLayerModifier.kt:#)
       3  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       3  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       3  W/yavorec.app.dev: Accessing hidden method Landroid/view/ViewTreeObserver;->dispatchOnScrollChanged()V (unsupported, reflection, 
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
contact sheets: 10 in sheets/
## COMPLETE
## logcat findings
app pids seen: []
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## gfx36

```
part gfx36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## P0. Package: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest); dexopt: x86_64: [status=verify] x86_64: [status=verify] ; baseline profile in the APK: assets/dexopt/baseline.prof assets/dexopt/baseline.profm 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3838 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## P1. 10 cold (with 'Fully drawn') + 10 warm starts, as installed
START cold new           n=10 min=2101ms median=2604ms max=3463ms  all=[2988, 3172, 3463, 2565, 2403, 2422, 2803, 2643, 2239, 2101]  states=['COLD']
DRAWN new: TotalTime n=10 min=2101ms median=2604ms max=3463ms; fully drawn n=10 median=2919ms min=2210 max=3840 all=[3273, 3806, 3840, 2708, 2694, 2818, 3110, 3020, 2560, 2210]; without line: 0
START warm new           n=10 min=273ms median=712ms max=915ms  all=[748, 911, 273, 451, 772, 526, 819, 677, 915, 397]  states=['HOT']
## G1. gfxinfo per scenario, round 1 (animations ON, 1080x2400); slowest frames from framestats and the histogram
GFX r1-home-scroll frames=   78  janky=77 (98.72%)  p50=250ms p90=400ms p95=450ms p99=550ms  slowUI=55 slowDraw=77 highInput=1
  GFXMEM r1-home-scroll: pipeline: Skia (OpenGL); GPU total: 44459928 bytes, 42.40 MB (36.84 MB is purgeable); textures: 1.36 MB (3 entries) | 6.20 MB (36 entries) | 10.95 MB (26 entries); frames: 78
  FS r1-home-scroll: n=77 slowest=650.0ms (frame #8; ui 330.7 rt 248.5 issue 139.5 flags 0) first5=[351, 540, 629, 617, 484] >32ms=77 >64ms=77 >100ms=75 span=11733ms
  r1-home-scroll HIST: frames=78 slowest-bucket=550ms ≥50ms=78 ≥100ms=75 ≥200ms=62 ≥500ms=3
GFX r1-carousel  frames=  113  janky=112 (99.12%)  p50=250ms p90=350ms p95=400ms p99=400ms  slowUI=80 slowDraw=112 highInput=1
  GFXMEM r1-carousel: pipeline: Skia (OpenGL); GPU total: 59972780 bytes, 57.19 MB (51.95 MB is purgeable); textures: 4.19 MB (9 entries) | 7.48 MB (42 entries) | 8.56 MB (21 entries); frames: 113
  FS r1-carousel: n=113 slowest=564.0ms (frame #38; ui 235.8 rt 212.3 issue 81.9 flags 0) first5=[96, 168, 330, 390, 350] >32ms=113 >64ms=113 >100ms=112 span=14600ms
  r1-carousel HIST: frames=113 slowest-bucket=400ms ≥50ms=113 ≥100ms=112 ≥200ms=94 ≥500ms=0
GFX r1-news      frames=   41  janky=41 (100.00%)  p50=150ms p90=200ms p95=250ms p99=400ms  slowUI=26 slowDraw=41 highInput=0
  GFXMEM r1-news: pipeline: Skia (OpenGL); GPU total: 70285576 bytes, 67.03 MB (62.71 MB is purgeable); textures: 1.91 MB (4 entries) | 9.35 MB (70 entries) | 6.28 MB (8 entries); frames: 41
  FS r1-news: n=41 slowest=470.3ms (frame #36; ui 150.6 rt 49.2 issue 6.8 flags 0) first5=[49, 135, 168, 226, 242] >32ms=41 >64ms=37 >100ms=37 span=6650ms
  r1-news HIST: frames=41 slowest-bucket=400ms ≥50ms=38 ≥100ms=35 ≥200ms=13 ≥500ms=0
GFX r1-article   frames=   94  janky=93 (98.94%)  p50=200ms p90=300ms p95=300ms p99=350ms  slowUI=36 slowDraw=93 highInput=2
  GFXMEM r1-article: pipeline: Skia (OpenGL); GPU total: 88826656 bytes, 84.71 MB (82.38 MB is purgeable); textures: 13.85 KB (1 entry) | 4.32 MB (14 entries) | 7.59 MB (54 entries); frames: 94
  FS r1-article: n=94 slowest=460.5ms (frame #23; ui 91.7 rt 202.7 issue 118.7 flags 0) first5=[48, 183, 299, 377, 368] >32ms=94 >64ms=92 >100ms=85 span=11483ms
  r1-article HIST: frames=94 slowest-bucket=350ms ≥50ms=92 ≥100ms=85 ≥200ms=54 ≥500ms=0
GFX r1-calendar  frames=   81  janky=80 (98.77%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=55 slowDraw=80 highInput=2
  GFXMEM r1-calendar: pipeline: Skia (OpenGL); GPU total: 81298640 bytes, 77.53 MB (75.29 MB is purgeable); textures: 1.52 MB (12 entries) | 5.07 MB (45 entries); frames: 81
  FS r1-calendar: n=81 slowest=458.5ms (frame #53; ui 170.8 rt 201.0 issue 45.8 flags 0) first5=[49, 118, 200, 266, 242] >32ms=81 >64ms=76 >100ms=73 span=10400ms
  r1-calendar HIST: frames=81 slowest-bucket=350ms ≥50ms=79 ≥100ms=70 ≥200ms=39 ≥500ms=0
GFX r1-catalog   frames=   54  janky=54 (100.00%)  p50=200ms p90=400ms p95=450ms p99=550ms  slowUI=28 slowDraw=54 highInput=0
  GFXMEM r1-catalog: pipeline: Skia (OpenGL); GPU total: 88507760 bytes, 84.41 MB (55.88 MB is purgeable); textures: 363.75 KB (16 entries) | 2.74 MB (14 entries); frames: 54
  FS r1-catalog: n=54 slowest=620.0ms (frame #20; ui 211.4 rt 276.4 issue 127.7 flags 0) first5=[105, 196, 326, 549, 491] >32ms=54 >64ms=54 >100ms=54 span=11467ms
  r1-catalog HIST: frames=54 slowest-bucket=550ms ≥50ms=54 ≥100ms=54 ≥200ms=34 ≥500ms=2
GFX r1-shelf     frames=   80  janky=80 (100.00%)  p50=250ms p90=350ms p95=350ms p99=400ms  slowUI=37 slowDraw=80 highInput=0
  GFXMEM r1-shelf: pipeline: Skia (OpenGL); GPU total: 86546152 bytes, 82.54 MB (54.02 MB is purgeable); textures: 367.63 KB (17 entries) | 2.74 MB (13 entries); frames: 80
  FS r1-shelf: n=80 slowest=557.6ms (frame #8; ui 181.7 rt 196.5 issue 49.1 flags 0) first5=[84, 127, 216, 181, 269] >32ms=80 >64ms=80 >100ms=77 span=10650ms
  r1-shelf HIST: frames=80 slowest-bucket=400ms ≥50ms=80 ≥100ms=76 ≥200ms=60 ≥500ms=0
GFX r1-my        frames=   84  janky=84 (100.00%)  p50=250ms p90=350ms p95=350ms p99=450ms  slowUI=27 slowDraw=84 highInput=0
  GFXMEM r1-my: pipeline: Skia (OpenGL); GPU total: 62171592 bytes, 59.29 MB (45.03 MB is purgeable); textures: 577.67 KB (26 entries) | 2.44 MB (5 entries); frames: 84
  FS r1-my: n=84 slowest=586.4ms (frame #73; ui 239.9 rt 248.9 issue 138.3 flags 0) first5=[55, 151, 266, 385, 393] >32ms=84 >64ms=82 >100ms=78 span=11850ms
  r1-my HIST: frames=84 slowest-bucket=450ms ≥50ms=84 ≥100ms=78 ≥200ms=61 ≥500ms=0
GFX r1-more      frames=   76  janky=76 (100.00%)  p50=250ms p90=350ms p95=400ms p99=500ms  slowUI=29 slowDraw=76 highInput=0
  GFXMEM r1-more: pipeline: Skia (OpenGL); GPU total: 64601224 bytes, 61.61 MB (47.30 MB is purgeable); textures: 891.48 KB (49 entries) | 2.44 MB (5 entries); frames: 76
  FS r1-more: n=76 slowest=621.6ms (frame #71; ui 215.8 rt 320.5 issue 160.9 flags 0) first5=[76, 124, 201, 182, 281] >32ms=76 >64ms=75 >100ms=71 span=11583ms
  r1-more HIST: frames=76 slowest-bucket=500ms ≥50ms=76 ≥100ms=70 ≥200ms=52 ≥500ms=1
GFX r1-gallery   frames=  102  janky=99 (97.06%)  p50=150ms p90=250ms p95=300ms p99=300ms  slowUI=50 slowDraw=99 highInput=4
  GFXMEM r1-gallery: pipeline: Skia (OpenGL); GPU total: 84212904 bytes, 80.31 MB (65.45 MB is purgeable); textures: 5.08 MB (15 entries) | 2.86 MB (50 entries); frames: 102
  FS r1-gallery: n=102 slowest=480.2ms (frame #64; ui 135.0 rt 186.2 issue 42.6 flags 0) first5=[39, 64, 183, 245, 133] >32ms=102 >64ms=97 >100ms=94 span=11167ms
  r1-gallery HIST: frames=102 slowest-bucket=350ms ≥50ms=98 ≥100ms=89 ≥200ms=48 ≥500ms=0
GFX r1-album-grid frames=   93  janky=89 (95.70%)  p50=150ms p90=250ms p95=250ms p99=250ms  slowUI=38 slowDraw=89 highInput=7
  GFXMEM r1-album-grid: pipeline: Skia (OpenGL); GPU total: 88150888 bytes, 84.07 MB (77.15 MB is purgeable); textures: 6.68 MB (49 entries) | 2.86 MB (49 entries); frames: 93
  FS r1-album-grid: n=93 slowest=392.2ms (frame #82; ui 159.7 rt 106.5 issue 25.9 flags 0) first5=[42, 61, 120, 179, 209] >32ms=91 >64ms=87 >100ms=83 span=11250ms
  r1-album-grid HIST: frames=93 slowest-bucket=250ms ≥50ms=89 ≥100ms=79 ≥200ms=43 ≥500ms=0
GFX r1-viewer    frames=  185  janky=177 (95.68%)  p50=129ms p90=150ms p95=200ms p99=200ms  slowUI=75 slowDraw=177 highInput=9
  r1-viewer HIST: frames=185 slowest-bucket=200ms ≥50ms=158 ≥100ms=125 ≥200ms=11 ≥500ms=0
GFX r1-settings  frames=   98  janky=92 (93.88%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=40 slowDraw=92 highInput=10
  GFXMEM r1-settings: pipeline: Skia (OpenGL); GPU total: 69806952 bytes, 66.57 MB (62.34 MB is purgeable); textures: 7.18 MB (121 entries) | 2.09 MB (8 entries); frames: 98
  FS r1-settings: n=97 slowest=445.4ms (frame #85; ui 193.4 rt 167.9 issue 90.3 flags 0) first5=[39, 83, 167, 216, 212] >32ms=94 >64ms=93 >100ms=88 span=10500ms
  r1-settings HIST: frames=98 slowest-bucket=350ms ≥50ms=92 ≥100ms=83 ≥200ms=41 ≥500ms=0
GFX r1-tab-switch frames=   70  janky=69 (98.57%)  p50=200ms p90=450ms p95=500ms p99=650ms  slowUI=40 slowDraw=69 highInput=2
  r1-tab-switch HIST: frames=70 slowest-bucket=650ms ≥50ms=68 ≥100ms=56 ≥200ms=36 ≥500ms=6
GFX r1-book-open-x5 frames=  124  janky=121 (97.58%)  p50=200ms p90=350ms p95=350ms p99=400ms  slowUI=70 slowDraw=121 highInput=5
  r1-book-open-x5 HIST: frames=124 slowest-bucket=450ms ≥50ms=120 ≥100ms=112 ≥200ms=70 ≥500ms=0
GFX r1-surprise-x3 frames=   69  janky=69 (100.00%)  p50=450ms p90=900ms p95=1000ms p99=1150ms  slowUI=34 slowDraw=69 highInput=0
  r1-surprise-x3 HIST: frames=69 slowest-bucket=1150ms ≥50ms=69 ≥100ms=69 ≥200ms=59 ≥500ms=31
GFX r1-cardfull-open-12s frames=   99  janky=99 (100.00%)  p50=150ms p90=200ms p95=250ms p99=750ms  slowUI=27 slowDraw=99 highInput=0
  FS r1-cardfull: n=99 slowest=846.0ms (frame #1; ui 568.4 rt 216.4 issue 148.8 flags 0) first5=[846, 161, 381, 375, 376] >32ms=99 >64ms=99 >100ms=99 span=12933ms
MEM after-round-1 (raw) PSS=83202KB  Java=14076KB  Native=33300KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=21)  RSS=171536KB  Views=13 Activities=1
MEM after-round-1 (trimmed) PSS=83364KB  Java=10764KB  Native=31740KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=21)  RSS=172156KB  Views=13 Activities=1
## G2. gfxinfo per scenario, round 2 (animations ON, 1080x2400); slowest frames from framestats and the histogram
GFX r2-home-scroll frames=   90  janky=88 (97.78%)  p50=200ms p90=350ms p95=350ms p99=450ms  slowUI=46 slowDraw=88 highInput=4
  GFXMEM r2-home-scroll: pipeline: Skia (OpenGL); GPU total: 51886076 bytes, 49.48 MB (39.47 MB is purgeable); textures: 2.85 MB (7 entries) | 7.03 MB (35 entries) | 13.36 MB (21 entries); frames: 90
  FS r2-home-scroll: n=89 slowest=550.2ms (frame #11; ui 149.2 rt 227.3 issue 50.9 flags 0) first5=[191, 374, 540, 537, 484] >32ms=89 >64ms=87 >100ms=85 span=11633ms
  r2-home-scroll HIST: frames=90 slowest-bucket=450ms ≥50ms=89 ≥100ms=85 ≥200ms=51 ≥500ms=0
GFX r2-carousel  frames=   11  janky=11 (100.00%)  p50=150ms p90=350ms p95=400ms p99=400ms  slowUI=4 slowDraw=11 highInput=0
  GFXMEM r2-carousel: pipeline: Skia (OpenGL); GPU total: 62481788 bytes, 59.59 MB (53.58 MB is purgeable); textures: 2.85 MB (7 entries) | 7.05 MB (39 entries) | 13.36 MB (21 entries); frames: 11
  FS r2-carousel: n=11 slowest=533.3ms (frame #3; ui 146.8 rt 260.2 issue 125.7 flags 0) first5=[73, 114, 533, 438, 377] >32ms=11 >64ms=11 >100ms=9 span=1300ms
  r2-carousel HIST: frames=11 slowest-bucket=400ms ≥50ms=11 ≥100ms=9 ≥200ms=5 ≥500ms=0
GFX r2-news      frames=   63  janky=62 (98.41%)  p50=150ms p90=250ms p95=300ms p99=300ms  slowUI=33 slowDraw=62 highInput=1
  GFXMEM r2-news: pipeline: Skia (OpenGL); GPU total: 100108440 bytes, 95.47 MB (85.42 MB is purgeable); textures: 10.18 MB (69 entries) | 16.46 MB (34 entries); frames: 63
  FS r2-news: n=63 slowest=429.3ms (frame #34; ui 91.2 rt 185.4 issue 111.6 flags 0) first5=[53, 79, 148, 197, 216] >32ms=63 >64ms=60 >100ms=56 span=7017ms
  r2-news HIST: frames=63 slowest-bucket=300ms ≥50ms=61 ≥100ms=53 ≥200ms=31 ≥500ms=0
GFX r2-article   frames=  102  janky=100 (98.04%)  p50=200ms p90=250ms p95=300ms p99=300ms  slowUI=42 slowDraw=100 highInput=3
  GFXMEM r2-article: pipeline: Skia (OpenGL); GPU total: 101585840 bytes, 96.88 MB (90.80 MB is purgeable); textures: 13.85 KB (1 entry) | 11.42 MB (69 entries) | 16.47 MB (40 entries); frames: 102
  FS r2-article: n=102 slowest=493.9ms (frame #87; ui 200.8 rt 163.5 issue 84.1 flags 0) first5=[42, 106, 180, 278, 361] >32ms=102 >64ms=101 >100ms=94 span=11783ms
  r2-article HIST: frames=102 slowest-bucket=350ms ≥50ms=101 ≥100ms=93 ≥200ms=57 ≥500ms=0
GFX r2-calendar  frames=   83  janky=83 (100.00%)  p50=150ms p90=250ms p95=250ms p99=300ms  slowUI=58 slowDraw=83 highInput=0
  GFXMEM r2-calendar: pipeline: Skia (OpenGL); GPU total: 113877064 bytes, 108.60 MB (104.26 MB is purgeable); textures: 5.80 MB (36 entries) | 12.95 MB (55 entries); frames: 83
  FS r2-calendar: n=83 slowest=443.1ms (frame #41; ui 187.8 rt 137.7 issue 41.0 flags 0) first5=[53, 88, 253, 214, 298] >32ms=83 >64ms=77 >100ms=70 span=9983ms
  r2-calendar HIST: frames=83 slowest-bucket=300ms ≥50ms=79 ≥100ms=65 ≥200ms=27 ≥500ms=0
GFX r2-catalog   frames=   56  janky=56 (100.00%)  p50=200ms p90=400ms p95=450ms p99=500ms  slowUI=21 slowDraw=56 highInput=0
  GFXMEM r2-catalog: pipeline: Skia (OpenGL); GPU total: 107497920 bytes, 102.52 MB (69.98 MB is purgeable); textures: 620.26 KB (22 entries) | 4.49 MB (9 entries); frames: 56
  FS r2-catalog: n=56 slowest=632.5ms (frame #55; ui 218.7 rt 202.2 issue 73.6 flags 0) first5=[133, 300, 465, 605, 455] >32ms=56 >64ms=56 >100ms=56 span=11683ms
  r2-catalog HIST: frames=56 slowest-bucket=500ms ≥50ms=56 ≥100ms=56 ≥200ms=35 ≥500ms=1
GFX r2-shelf     frames=   89  janky=89 (100.00%)  p50=250ms p90=300ms p95=350ms p99=400ms  slowUI=38 slowDraw=89 highInput=0
  GFXMEM r2-shelf: pipeline: Skia (OpenGL); GPU total: 113012504 bytes, 107.78 MB (74.99 MB is purgeable); textures: 624.13 KB (23 entries) | 4.49 MB (8 entries); frames: 89
  FS r2-shelf: n=89 slowest=567.6ms (frame #15; ui 204.0 rt 219.6 issue 110.5 flags 0) first5=[70, 121, 307, 387, 497] >32ms=89 >64ms=87 >100ms=83 span=11667ms
  r2-shelf HIST: frames=89 slowest-bucket=400ms ≥50ms=89 ≥100ms=83 ≥200ms=64 ≥500ms=0
GFX r2-my        frames=   82  janky=82 (100.00%)  p50=250ms p90=350ms p95=400ms p99=400ms  slowUI=33 slowDraw=82 highInput=0
  GFXMEM r2-my: pipeline: Skia (OpenGL); GPU total: 110589280 bytes, 105.47 MB (71.21 MB is purgeable); textures: 628.59 KB (28 entries) | 4.48 MB (7 entries); frames: 82
  FS r2-my: n=82 slowest=561.4ms (frame #44; ui 216.9 rt 220.8 issue 75.8 flags 0) first5=[58, 136, 252, 373, 413] >32ms=82 >64ms=80 >100ms=74 span=11933ms
  r2-my HIST: frames=82 slowest-bucket=400ms ≥50ms=82 ≥100ms=74 ≥200ms=55 ≥500ms=0
GFX r2-more      frames=   84  janky=84 (100.00%)  p50=250ms p90=350ms p95=400ms p99=450ms  slowUI=33 slowDraw=84 highInput=0
  GFXMEM r2-more: pipeline: Skia (OpenGL); GPU total: 108498760 bytes, 103.47 MB (75.16 MB is purgeable); textures: 701.40 KB (50 entries) | 4.48 MB (7 entries); frames: 84
  FS r2-more: n=84 slowest=597.3ms (frame #29; ui 229.4 rt 209.2 issue 93.0 flags 0) first5=[79, 214, 272, 403, 63] >32ms=84 >64ms=83 >100ms=78 span=12500ms
  r2-more HIST: frames=84 slowest-bucket=450ms ≥50ms=84 ≥100ms=78 ≥200ms=61 ≥500ms=0
GFX r2-gallery   frames=  105  janky=103 (98.10%)  p50=200ms p90=250ms p95=300ms p99=350ms  slowUI=55 slowDraw=103 highInput=3
  GFXMEM r2-gallery: pipeline: Skia (OpenGL); GPU total: 100723112 bytes, 96.06 MB (79.18 MB is purgeable); textures: 5.08 MB (16 entries) | 4.76 MB (49 entries); frames: 105
  FS r2-gallery: n=105 slowest=499.2ms (frame #86; ui 181.7 rt 170.1 issue 82.5 flags 0) first5=[42, 73, 136, 233, 266] >32ms=105 >64ms=101 >100ms=96 span=11750ms
  r2-gallery HIST: frames=105 slowest-bucket=350ms ≥50ms=101 ≥100ms=93 ≥200ms=59 ≥500ms=0
GFX r2-album-grid frames=   94  janky=92 (97.87%)  p50=200ms p90=250ms p95=300ms p99=350ms  slowUI=45 slowDraw=92 highInput=3
  GFXMEM r2-album-grid: pipeline: Skia (OpenGL); GPU total: 105365592 bytes, 100.48 MB (91.55 MB is purgeable); textures: 6.68 MB (50 entries) | 4.61 MB (46 entries); frames: 94
  FS r2-album-grid: n=93 slowest=451.9ms (frame #90; ui 101.4 rt 168.1 issue 80.9 flags 0) first5=[65, 170, 201, 273, 167] >32ms=93 >64ms=90 >100ms=83 span=11617ms
  r2-album-grid HIST: frames=94 slowest-bucket=350ms ≥50ms=89 ≥100ms=81 ≥200ms=48 ≥500ms=0
GFX r2-viewer    frames=  181  janky=169 (93.37%)  p50=125ms p90=150ms p95=150ms p99=200ms  slowUI=87 slowDraw=169 highInput=18
  r2-viewer HIST: frames=181 slowest-bucket=250ms ≥50ms=158 ≥100ms=125 ≥200ms=9 ≥500ms=0
GFX r2-settings  frames=   41  janky=37 (90.24%)  p50=121ms p90=200ms p95=200ms p99=300ms  slowUI=17 slowDraw=37 highInput=7
  GFXMEM r2-settings: pipeline: Skia (OpenGL); GPU total: 86944304 bytes, 82.92 MB (75.78 MB is purgeable); textures: 6.23 MB (121 entries) | 4.09 MB (8 entries); frames: 41
  FS r2-settings: n=41 slowest=353.0ms (frame #16; ui 129.7 rt 109.7 issue 36.1 flags 0) first5=[30, 102, 120, 183, 22] >32ms=38 >64ms=38 >100ms=33 span=3800ms
  r2-settings HIST: frames=41 slowest-bucket=300ms ≥50ms=38 ≥100ms=25 ≥200ms=7 ≥500ms=0
GFX r2-tab-switch frames=  133  janky=125 (93.98%)  p50=150ms p90=300ms p95=350ms p99=400ms  slowUI=62 slowDraw=125 highInput=12
  r2-tab-switch HIST: frames=133 slowest-bucket=450ms ≥50ms=125 ≥100ms=108 ≥200ms=58 ≥500ms=0
GFX r2-book-open-x5 frames=  125  janky=124 (99.20%)  p50=200ms p90=350ms p95=350ms p99=450ms  slowUI=69 slowDraw=124 highInput=2
  r2-book-open-x5 HIST: frames=125 slowest-bucket=450ms ≥50ms=121 ≥100ms=114 ≥200ms=64 ≥500ms=0
GFX r2-surprise-x3 frames=   67  janky=66 (98.51%)  p50=500ms p90=750ms p95=850ms p99=1050ms  slowUI=32 slowDraw=66 highInput=1
  r2-surprise-x3 HIST: frames=67 slowest-bucket=1050ms ≥50ms=67 ≥100ms=67 ≥200ms=57 ≥500ms=34
GFX r2-cardfull-open-12s frames=  100  janky=100 (100.00%)  p50=150ms p90=200ms p95=200ms p99=500ms  slowUI=20 slowDraw=100 highInput=0
  FS r2-cardfull: n=100 slowest=330.5ms (frame #5; ui 96.0 rt 127.7 issue 41.7 flags 0) first5=[259, 212, 307, 219, 331] >32ms=100 >64ms=100 >100ms=99 span=12283ms
MEM after-round-2 (raw) PSS=86401KB  Java=19312KB  Native=34476KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=177800KB  Views=13 Activities=1
MEM after-round-2 (trimmed) PSS=79839KB  Java=10616KB  Native=31628KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=171748KB  Views=13 Activities=1
## P4. Totals
scenario          app     n   frames  janky%   p50   p90   p99
## P5. Diagnostics › „Бързодействие“ (the app's own numbers, JankStats)
PERF CARD filled: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 15 | Първи кадър: медиана 2543 мс, последен 2527 мс | Пълно показване: медиана 2973 мс, последно 2983 мс | Топъл старт · 10 | Първи кадър: медиана 660 мс, последен 382 мс | Насечени кадри по екрани | home: 0,6 % от 173 кадъра · най-бавен 998 мс | общо с видеокартата: средно 327 мс, най-бавен 1168 мс | Диагностика
PERF CARD filled (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 15 | Първи кадър: медиана 2543 мс, последен 2527 мс | Пълно показване: медиана 2973 мс, последно 2983 мс | Топъл старт · 10 | Първи кадър: медиана 660 мс, последен 382 мс | Насечени кадри по екрани | home: 0,6 % от 173 кадъра · най-бавен 998 мс | общо с видеокартата: средно 327 мс, най-бавен 1168 мс | Няма записани грешки. | Диагностика
contact sheets: 4 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 15 | Първи кадър: медиана 2543 мс, последен 2527 мс | Пълно показване: медиана 2973 мс, последно 2983 мс | Топъл старт · 10 | Първи кадър: медиана 660 мс, последен 382 мс | Насечени кадри по екрани | hom
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 15" text="Първи кадър: медиана 2543 мс, последен 2527 мс" text="Пълно показване: медиана 2973 мс, последно 2983 мс" text="Топъл старт · 10" text="Първи кадър
APP W/E lines (pids ['5406', '7749', '7839', '8006', '8137', '8293', '8410', '8470', '8529', '8589', '8656', '8719', '8780', '9084', '12438']): 141 in 13 groups
      26  E/OpenGLRenderer: Unable to match the desired swap behavior.
      21  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      21  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      16  W/Parcel: Expecting binder but got null!
      15  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      15  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      15  W/Gralloc4: allocator #.x is not supported
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=ededad#-bd#d-#db-#-d#e#a#d#, generation=#)
       1  W/g.chyavorec.app: Suspending all threads took: #.#ms
       1  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
       1  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
contact sheets: 5 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5406', '7749', '7839', '8006', '8137', '8293', '8410', '8470', '8529', '8589', '8656', '8719', '8780', '9084', '12438']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 62
   skipped-frames events=62 max=104 total=2925
== Davey: 122
   davey events=122 max=2561ms median=920ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## gfxb36

```
part gfxb36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
BASE APK as the tested app: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest)
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
installed:  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## P0. Package: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest); dexopt: x86_64: [status=verify] x86_64: [status=verify] ; baseline profile in the APK: assets/dexopt/baseline.prof assets/dexopt/baseline.profm 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4797 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## P1. 10 cold (with 'Fully drawn') + 10 warm starts, as installed
START cold new           n=10 min=3048ms median=3431ms max=3896ms  all=[3701, 3650, 3663, 3896, 3048, 3440, 3104, 3320, 3243, 3422]  states=['COLD']
DRAWN new: TotalTime n=10 min=3048ms median=3431ms max=3896ms; fully drawn n=10 median=4436ms min=3567 max=5727 all=[5061, 4211, 5727, 5469, 4471, 3567, 4392, 4828, 3769, 4401]; without line: 0
START warm new           n=10 min=608ms median=962ms max=1231ms  all=[1231, 1060, 789, 941, 986, 608, 1193, 749, 984, 621]  states=['HOT']
## G1. gfxinfo per scenario, round 1 (animations ON, 1080x2400); slowest frames from framestats and the histogram
GFX r1-home-scroll frames=   52  janky=52 (100.00%)  p50=400ms p90=650ms p95=700ms p99=900ms  slowUI=31 slowDraw=52 highInput=0
  GFXMEM r1-home-scroll: pipeline: Skia (OpenGL); GPU total: 48992008 bytes, 46.72 MB (37.42 MB is purgeable); textures: 3.27 MB (7 entries) | 6.17 MB (34 entries) | 12.38 MB (24 entries); frames: 52
  FS r1-home-scroll: n=51 slowest=1090.7ms (frame #48; ui 321.5 rt 565.1 issue 298.7 flags 0) first5=[206, 632, 877, 842, 841] >32ms=51 >64ms=51 >100ms=51 span=11733ms
  r1-home-scroll HIST: frames=52 slowest-bucket=900ms ≥50ms=52 ≥100ms=52 ≥200ms=45 ≥500ms=20
GFX r1-carousel  frames=   16  janky=16 (100.00%)  p50=350ms p90=400ms p95=700ms p99=700ms  slowUI=12 slowDraw=16 highInput=0
  GFXMEM r1-carousel: pipeline: Skia (OpenGL); GPU total: 50407900 bytes, 48.07 MB (39.09 MB is purgeable); textures: 1.84 MB (4 entries) | 6.97 MB (31 entries) | 14.07 MB (33 entries); frames: 16
  FS r1-carousel: n=16 slowest=717.0ms (frame #11; ui 274.6 rt 425.9 issue 193.3 flags 0) first5=[131, 227, 392, 582, 568] >32ms=16 >64ms=16 >100ms=16 span=2417ms
  r1-carousel HIST: frames=16 slowest-bucket=700ms ≥50ms=16 ≥100ms=16 ≥200ms=13 ≥500ms=1
GFX r1-news      frames=   83  janky=82 (98.80%)  p50=200ms p90=350ms p95=400ms p99=450ms  slowUI=54 slowDraw=82 highInput=1
  GFXMEM r1-news: pipeline: Skia (OpenGL); GPU total: 99194248 bytes, 94.60 MB (85.60 MB is purgeable); textures: 1.84 MB (4 entries) | 10.44 MB (92 entries) | 13.84 MB (32 entries); frames: 83
  FS r1-news: n=84 slowest=691.9ms (frame #69; ui 190.8 rt 253.4 issue 137.8 flags 0) first5=[60, 109, 303, 341, 437] >32ms=84 >64ms=77 >100ms=76 span=13433ms
  r1-news HIST: frames=83 slowest-bucket=450ms ≥50ms=78 ≥100ms=74 ≥200ms=54 ≥500ms=0
GFX r1-article   frames=   67  janky=67 (100.00%)  p50=300ms p90=400ms p95=450ms p99=550ms  slowUI=24 slowDraw=67 highInput=0
  GFXMEM r1-article: pipeline: Skia (OpenGL); GPU total: 113686544 bytes, 108.42 MB (103.41 MB is purgeable); textures: 13.85 KB (1 entry) | 11.34 MB (63 entries) | 16.04 MB (72 entries); frames: 67
  FS r1-article: n=67 slowest=702.7ms (frame #49; ui 252.0 rt 312.6 issue 180.7 flags 0) first5=[54, 254, 326, 479, 644] >32ms=67 >64ms=66 >100ms=63 span=11867ms
  r1-article HIST: frames=67 slowest-bucket=550ms ≥50ms=67 ≥100ms=63 ≥200ms=55 ≥500ms=3
GFX r1-calendar  frames=   76  janky=76 (100.00%)  p50=250ms p90=400ms p95=450ms p99=500ms  slowUI=49 slowDraw=76 highInput=0
  GFXMEM r1-calendar: pipeline: Skia (OpenGL); GPU total: 113344536 bytes, 108.09 MB (103.84 MB is purgeable); textures: 9.44 MB (51 entries) | 10.27 MB (57 entries); frames: 76
  FS r1-calendar: n=76 slowest=615.6ms (frame #35; ui 178.2 rt 259.8 issue 72.9 flags 0) first5=[66, 169, 280, 347, 385] >32ms=76 >64ms=74 >100ms=73 span=11383ms
  r1-calendar HIST: frames=76 slowest-bucket=500ms ≥50ms=75 ≥100ms=73 ≥200ms=60 ≥500ms=1
GFX r1-catalog   frames=   57  janky=57 (100.00%)  p50=400ms p90=550ms p95=650ms p99=700ms  slowUI=33 slowDraw=57 highInput=0
  GFXMEM r1-catalog: pipeline: Skia (OpenGL); GPU total: 113659816 bytes, 108.39 MB (72.10 MB is purgeable); textures: 303.72 KB (20 entries) | 4.32 MB (4 entries); frames: 57
  FS r1-catalog: n=57 slowest=1005.1ms (frame #21; ui 359.4 rt 384.0 issue 191.3 flags 0) first5=[147, 410, 552, 865, 870] >32ms=57 >64ms=57 >100ms=57 span=12750ms
  r1-catalog HIST: frames=57 slowest-bucket=700ms ≥50ms=57 ≥100ms=57 ≥200ms=54 ≥500ms=17
GFX r1-my        frames=   72  janky=71 (98.61%)  p50=300ms p90=400ms p95=450ms p99=500ms  slowUI=29 slowDraw=71 highInput=1
  GFXMEM r1-my: pipeline: Skia (OpenGL); GPU total: 109759816 bytes, 104.68 MB (86.34 MB is purgeable); textures: 570.06 KB (29 entries) | 4.67 MB (44 entries); frames: 72
  FS r1-my: n=72 slowest=693.1ms (frame #5; ui 255.4 rt 256.5 issue 138.2 flags 0) first5=[49, 287, 388, 553, 693] >32ms=72 >64ms=71 >100ms=71 span=12183ms
  r1-my HIST: frames=72 slowest-bucket=500ms ≥50ms=71 ≥100ms=71 ≥200ms=60 ≥500ms=2
GFX r1-more      frames=   69  janky=69 (100.00%)  p50=250ms p90=450ms p95=500ms p99=550ms  slowUI=27 slowDraw=69 highInput=0
  GFXMEM r1-more: pipeline: Skia (OpenGL); GPU total: 111685224 bytes, 106.51 MB (87.90 MB is purgeable); textures: 624.15 KB (45 entries) | 4.64 MB (36 entries); frames: 69
  FS r1-more: n=69 slowest=757.5ms (frame #42; ui 199.7 rt 385.2 issue 163.4 flags 0) first5=[52, 156, 335, 617, 677] >32ms=69 >64ms=67 >100ms=67 span=12217ms
  r1-more HIST: frames=69 slowest-bucket=550ms ≥50ms=67 ≥100ms=67 ≥200ms=57 ≥500ms=4
GFX r1-gallery   frames=   73  janky=73 (100.00%)  p50=250ms p90=400ms p95=450ms p99=550ms  slowUI=36 slowDraw=73 highInput=0
  GFXMEM r1-gallery: pipeline: Skia (OpenGL); GPU total: 106967496 bytes, 102.01 MB (89.34 MB is purgeable); textures: 5.36 MB (33 entries) | 4.71 MB (57 entries); frames: 73
  FS r1-gallery: n=74 slowest=694.4ms (frame #42; ui 188.0 rt 169.8 issue 55.4 flags 0) first5=[62, 110, 207, 311, 343] >32ms=74 >64ms=72 >100ms=70 span=12817ms
  r1-gallery HIST: frames=73 slowest-bucket=550ms ≥50ms=73 ≥100ms=69 ≥200ms=59 ≥500ms=2
GFX r1-album-grid frames=   77  janky=76 (98.70%)  p50=250ms p90=450ms p95=500ms p99=650ms  slowUI=42 slowDraw=76 highInput=1
  GFXMEM r1-album-grid: pipeline: Skia (OpenGL); GPU total: 110100440 bytes, 105.00 MB (100.27 MB is purgeable); textures: 7.12 MB (92 entries) | 4.29 MB (21 entries); frames: 77
  FS r1-album-grid: n=76 slowest=883.7ms (frame #31; ui 365.1 rt 128.1 issue 83.9 flags 0) first5=[174, 205, 367, 308, 180] >32ms=76 >64ms=73 >100ms=72 span=11883ms
  r1-album-grid HIST: frames=77 slowest-bucket=650ms ≥50ms=73 ≥100ms=71 ≥200ms=49 ≥500ms=4
GFX r1-viewer    frames=  145  janky=144 (99.31%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=63 slowDraw=144 highInput=1
  r1-viewer HIST: frames=145 slowest-bucket=350ms ≥50ms=135 ≥100ms=119 ≥200ms=63 ≥500ms=0
GFX r1-settings  frames=   88  janky=86 (97.73%)  p50=250ms p90=400ms p95=400ms p99=500ms  slowUI=33 slowDraw=86 highInput=3
  GFXMEM r1-settings: pipeline: Skia (OpenGL); GPU total: 86001512 bytes, 82.02 MB (77.60 MB is purgeable); textures: 7.14 MB (111 entries) | 4.10 MB (10 entries); frames: 88
  FS r1-settings: n=87 slowest=655.5ms (frame #59; ui 168.4 rt 245.2 issue 137.7 flags 0) first5=[117, 211, 276, 209, 379] >32ms=86 >64ms=85 >100ms=83 span=12650ms
  r1-settings HIST: frames=88 slowest-bucket=500ms ≥50ms=85 ≥100ms=82 ≥200ms=66 ≥500ms=1
GFX r1-tab-switch frames=   71  janky=71 (100.00%)  p50=200ms p90=600ms p95=750ms p99=1050ms  slowUI=40 slowDraw=71 highInput=0
  r1-tab-switch HIST: frames=71 slowest-bucket=1050ms ≥50ms=70 ≥100ms=65 ≥200ms=47 ≥500ms=13
GFX r1-book-open-x5 frames=   78  janky=77 (98.72%)  p50=300ms p90=500ms p95=600ms p99=750ms  slowUI=57 slowDraw=77 highInput=2
  r1-book-open-x5 HIST: frames=78 slowest-bucket=750ms ≥50ms=78 ≥100ms=72 ≥200ms=63 ≥500ms=14
GFX r1-surprise-x3 frames=   64  janky=64 (100.00%)  p50=850ms p90=1200ms p95=1300ms p99=1350ms  slowUI=46 slowDraw=64 highInput=0
  r1-surprise-x3 HIST: frames=64 slowest-bucket=1350ms ≥50ms=64 ≥100ms=64 ≥200ms=60 ≥500ms=46
GFX r1-cardfull-open-12s frames=   54  janky=53 (98.15%)  p50=300ms p90=450ms p95=550ms p99=850ms  slowUI=8 slowDraw=53 highInput=2
  FS r1-cardfull: n=55 slowest=799.1ms (frame #3; ui 268.8 rt 257.8 issue 74.8 flags 0) first5=[515, 775, 799, 479, 540] >32ms=55 >64ms=55 >100ms=55 span=12550ms
MEM after-round-1 (raw) PSS=76220KB  Java=16232KB  Native=25572KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=5)  RSS=159792KB  Views=13 Activities=1
MEM after-round-1 (trimmed) PSS=72551KB  Java=9996KB  Native=23072KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=5)  RSS=156656KB  Views=13 Activities=1
## G2. gfxinfo per scenario, round 2 (animations ON, 1080x2400); slowest frames from framestats and the histogram
GFX r2-home-scroll frames=   68  janky=68 (100.00%)  p50=350ms p90=500ms p95=550ms p99=600ms  slowUI=47 slowDraw=68 highInput=0
  GFXMEM r2-home-scroll: pipeline: Skia (OpenGL); GPU total: 70534488 bytes, 67.27 MB (62.51 MB is purgeable); textures: 2.35 MB (5 entries) | 7.12 MB (39 entries) | 5.90 MB (13 entries); frames: 68
  FS r2-home-scroll: n=67 slowest=792.8ms (frame #9; ui 270.6 rt 307.9 issue 104.3 flags 0) first5=[348, 620, 736, 752, 626] >32ms=67 >64ms=67 >100ms=67 span=12300ms
  r2-home-scroll HIST: frames=68 slowest-bucket=600ms ≥50ms=68 ≥100ms=68 ≥200ms=61 ≥500ms=10
GFX r2-news      frames=   84  janky=84 (100.00%)  p50=250ms p90=400ms p95=450ms p99=500ms  slowUI=43 slowDraw=84 highInput=0
  GFXMEM r2-news: pipeline: Skia (OpenGL); GPU total: 88670720 bytes, 84.56 MB (80.31 MB is purgeable); textures: 13.85 KB (1 entry) | 6.25 MB (31 entries) | 7.60 MB (35 entries); frames: 84
  FS r2-news: n=84 slowest=641.2ms (frame #56; ui 214.5 rt 248.5 issue 68.0 flags 0) first5=[52, 171, 202, 372, 417] >32ms=84 >64ms=83 >100ms=82 span=12267ms
  r2-news HIST: frames=84 slowest-bucket=500ms ≥50ms=84 ≥100ms=82 ≥200ms=65 ≥500ms=2
GFX r2-article   frames=   62  janky=62 (100.00%)  p50=250ms p90=450ms p95=450ms p99=500ms  slowUI=23 slowDraw=62 highInput=0
  GFXMEM r2-article: pipeline: Skia (OpenGL); GPU total: 87815152 bytes, 83.75 MB (79.49 MB is purgeable); textures: 27.70 KB (2 entries) | 7.51 MB (36 entries) | 6.84 MB (29 entries); frames: 62
  FS r2-article: n=62 slowest=721.0ms (frame #44; ui 119.8 rt 279.1 issue 149.5 flags 0) first5=[49, 231, 400, 562, 603] >32ms=62 >64ms=61 >100ms=61 span=10683ms
  r2-article HIST: frames=62 slowest-bucket=500ms ≥50ms=61 ≥100ms=61 ≥200ms=47 ≥500ms=2
GFX r2-calendar  frames=   49  janky=49 (100.00%)  p50=250ms p90=400ms p95=400ms p99=500ms  slowUI=33 slowDraw=49 highInput=0
  GFXMEM r2-calendar: pipeline: Skia (OpenGL); GPU total: 89164344 bytes, 85.03 MB (82.78 MB is purgeable); textures: 4.24 MB (24 entries) | 4.32 MB (21 entries); frames: 49
  FS r2-calendar: n=49 slowest=646.6ms (frame #35; ui 176.8 rt 239.8 issue 131.0 flags 0) first5=[68, 93, 188, 299, 414] >32ms=49 >64ms=48 >100ms=45 span=7100ms
  r2-calendar HIST: frames=49 slowest-bucket=500ms ≥50ms=49 ≥100ms=45 ≥200ms=36 ≥500ms=1
GFX r2-catalog   frames=   60  janky=60 (100.00%)  p50=400ms p90=700ms p95=800ms p99=900ms  slowUI=41 slowDraw=60 highInput=0
  GFXMEM r2-catalog: pipeline: Skia (OpenGL); GPU total: 80225880 bytes, 76.51 MB (50.35 MB is purgeable); textures: 299.84 KB (20 entries) | 2.42 MB (32 entries); frames: 60
  FS r2-catalog: n=61 slowest=1320.4ms (frame #58; ui 546.1 rt 375.0 issue 143.9 flags 0) first5=[153, 288, 589, 752, 819] >32ms=61 >64ms=61 >100ms=61 span=13617ms
  r2-catalog HIST: frames=60 slowest-bucket=900ms ≥50ms=60 ≥100ms=60 ≥200ms=56 ≥500ms=24
GFX r2-shelf     frames=    0  janky=0 (0.00%)  p50=4950ms p90=4950ms p95=4950ms p99=4950ms  slowUI=0 slowDraw=0 highInput=0
  GFXMEM r2-shelf: pipeline: Skia (OpenGL); GPU total: 80523928 bytes, 76.79 MB (50.54 MB is purgeable); textures: 303.72 KB (21 entries) | 2.42 MB (32 entries); frames: 0
  FS r2-shelf: no frames
  r2-shelf HIST: frames=0 slowest-bucket=0ms ≥50ms=0 ≥100ms=0 ≥200ms=0 ≥500ms=0
GFX r2-my        frames=   65  janky=65 (100.00%)  p50=300ms p90=400ms p95=400ms p99=500ms  slowUI=27 slowDraw=65 highInput=0
  GFXMEM r2-my: pipeline: Skia (OpenGL); GPU total: 84227784 bytes, 80.33 MB (54.07 MB is purgeable); textures: 573.94 KB (31 entries) | 2.25 MB (3 entries); frames: 65
  FS r2-my: n=65 slowest=737.2ms (frame #61; ui 249.9 rt 269.5 issue 88.2 flags 0) first5=[120, 224, 390, 568, 564] >32ms=65 >64ms=65 >100ms=65 span=11533ms
  r2-my HIST: frames=65 slowest-bucket=500ms ≥50ms=65 ≥100ms=65 ≥200ms=54 ≥500ms=1
GFX r2-more      frames=   80  janky=80 (100.00%)  p50=300ms p90=450ms p95=450ms p99=500ms  slowUI=29 slowDraw=80 highInput=0
  GFXMEM r2-more: pipeline: Skia (OpenGL); GPU total: 86186056 bytes, 82.19 MB (55.66 MB is purgeable); textures: 628.03 KB (47 entries) | 2.25 MB (3 entries); frames: 80
  FS r2-more: n=80 slowest=683.3ms (frame #29; ui 308.6 rt 192.2 issue 65.2 flags 0) first5=[56, 89, 324, 434, 545] >32ms=80 >64ms=79 >100ms=77 span=13067ms
  r2-more HIST: frames=80 slowest-bucket=500ms ≥50ms=80 ≥100ms=77 ≥200ms=65 ≥500ms=3
GFX r2-gallery   frames=   62  janky=62 (100.00%)  p50=300ms p90=450ms p95=450ms p99=700ms  slowUI=31 slowDraw=62 highInput=0
  GFXMEM r2-gallery: pipeline: Skia (OpenGL); GPU total: 73046592 bytes, 69.66 MB (59.12 MB is purgeable); textures: 5.06 MB (14 entries) | 2.60 MB (41 entries); frames: 62
  FS r2-gallery: n=62 slowest=783.8ms (frame #11; ui 260.8 rt 191.6 issue 1.6 flags 0) first5=[52, 91, 250, 381, 434] >32ms=62 >64ms=58 >100ms=55 span=12367ms
  r2-gallery HIST: frames=62 slowest-bucket=700ms ≥50ms=59 ≥100ms=55 ≥200ms=44 ≥500ms=3
GFX r2-album-grid frames=   78  janky=77 (98.72%)  p50=250ms p90=400ms p95=450ms p99=450ms  slowUI=34 slowDraw=77 highInput=2
  GFXMEM r2-album-grid: pipeline: Skia (OpenGL); GPU total: 85324024 bytes, 81.37 MB (70.77 MB is purgeable); textures: 6.67 MB (49 entries) | 2.59 MB (40 entries); frames: 78
  FS r2-album-grid: n=77 slowest=630.4ms (frame #73; ui 190.0 rt 264.2 issue 66.9 flags 0) first5=[167, 266, 382, 415, 345] >32ms=77 >64ms=74 >100ms=73 span=11967ms
  r2-album-grid HIST: frames=78 slowest-bucket=450ms ≥50ms=75 ≥100ms=72 ≥200ms=59 ≥500ms=0
GFX r2-viewer    frames=  154  janky=153 (99.35%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=71 slowDraw=153 highInput=3
  r2-viewer HIST: frames=154 slowest-bucket=350ms ≥50ms=145 ≥100ms=133 ≥200ms=73 ≥500ms=0
GFX r2-settings  frames=   74  janky=72 (97.30%)  p50=250ms p90=400ms p95=400ms p99=450ms  slowUI=28 slowDraw=72 highInput=4
  GFXMEM r2-settings: pipeline: Skia (OpenGL); GPU total: 70111624 bytes, 66.86 MB (64.45 MB is purgeable); textures: 6.18 MB (110 entries) | 2.09 MB (8 entries); frames: 74
  FS r2-settings: n=73 slowest=641.5ms (frame #41; ui 172.9 rt 228.6 issue 54.3 flags 0) first5=[130, 234, 317, 25, 44] >32ms=70 >64ms=67 >100ms=67 span=11633ms
  r2-settings HIST: frames=74 slowest-bucket=450ms ≥50ms=67 ≥100ms=67 ≥200ms=50 ≥500ms=0
GFX r2-tab-switch frames=  116  janky=113 (97.41%)  p50=200ms p90=450ms p95=500ms p99=550ms  slowUI=64 slowDraw=113 highInput=4
  r2-tab-switch HIST: frames=116 slowest-bucket=600ms ≥50ms=115 ≥100ms=109 ≥200ms=71 ≥500ms=6
GFX r2-book-open-x5 frames=   78  janky=78 (100.00%)  p50=300ms p90=550ms p95=550ms p99=700ms  slowUI=61 slowDraw=78 highInput=0
  r2-book-open-x5 HIST: frames=78 slowest-bucket=700ms ≥50ms=78 ≥100ms=73 ≥200ms=64 ≥500ms=15
GFX r2-surprise-x3 frames=   66  janky=66 (100.00%)  p50=600ms p90=1000ms p95=1050ms p99=1250ms  slowUI=31 slowDraw=66 highInput=0
  r2-surprise-x3 HIST: frames=66 slowest-bucket=1250ms ≥50ms=66 ≥100ms=66 ≥200ms=58 ≥500ms=41
GFX r2-cardfull-open-12s frames=   56  janky=56 (100.00%)  p50=250ms p90=400ms p95=650ms p99=750ms  slowUI=7 slowDraw=56 highInput=0
  FS r2-cardfull: n=57 slowest=853.8ms (frame #3; ui 296.5 rt 416.5 issue 178.2 flags 0) first5=[553, 781, 854, 623, 746] >32ms=57 >64ms=57 >100ms=57 span=12417ms
MEM after-round-2 (raw) PSS=88792KB  Java=17496KB  Native=41372KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=183420KB  Views=13 Activities=1
MEM after-round-2 (trimmed) PSS=82457KB  Java=10396KB  Native=37348KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=177584KB  Views=13 Activities=1
## P4. Totals
scenario          app     n   frames  janky%   p50   p90   p99
## P5. Diagnostics › „Бързодействие“ (the app's own numbers, JankStats)
WARN: settings item =Диагностика|=Diagnostics not found
contact sheets: 4 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: settings item =Диагностика|=Diagnostics not found
APP W/E lines (pids ['4992', '7287', '7448', '7650', '7769', '7937', '8039', '8116', '8175', '8233', '8348', '8427', '8485', '8733', '12643']): 138 in 11 groups
      29  E/OpenGLRenderer: Unable to match the desired swap behavior.
      21  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      21  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      18  W/Parcel: Expecting binder but got null!
      15  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      15  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      15  W/Gralloc4: allocator #.x is not supported
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=ff#e#ff-a#e-#e#-#f#f-d#eb#d#, generation=#)
       1  W/g.chyavorec.app: Suspending all threads took: #.#ms
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object g#.s(java.lang.Object)(r
       1  W/g.chyavorec.app: Long monitor contention with owner OkHttp https://chyavorec.org/... (#) at android.security.net.config.Network
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4992', '7287', '7448', '7650', '7769', '7937', '8039', '8116', '8175', '8233', '8348', '8427', '8485', '8733', '12643']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 104
   skipped-frames events=104 max=126 total=5041
== Davey: 236
   davey events=236 max=3142ms median=966ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:18 app-mapping.txt; crashes: 0 B
```

## hi28

```
part hi28; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## HI new-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots dump
installed new-1: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-1-1-clean (clean): cold start TotalTime: 1725
IDLE hi-new-1-1-clean-20-35s (15 s, no input): frames +38; cpu=14.7% (ticks 220 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.3%, Jit thread pool=3.4%, g.chyavorec.app=1.7%, DefaultExecutor=0.2%, FrameMetricsAgg=0.1%; pid 6961
  new-1-1-clean 20-35s FSTL: 42 frames in the window, 3 group(s) [start×frames(length)]: +0.18s×16(1477ms); +7.66s×13(1073ms); +14.73s×13(1099ms); gaps between group starts (s): 7.48 7.07; slowest 423 ms
IDLE hi-new-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6961
  new-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6961
  new-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI new-1-2-clean (clean): cold start TotalTime: 1401
IDLE hi-new-1-2-clean-20-35s (15 s, no input): frames +36; cpu=14.3% (ticks 215 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=8.3%, Jit thread pool=4.2%, g.chyavorec.app=1.5%, DefaultExecutor=0.2%, FrameMetricsAgg=0.1%; pid 7155
  new-1-2-clean 20-35s FSTL: 39 frames in the window, 3 group(s) [start×frames(length)]: +0.22s×14(1427ms); +7.66s×12(897ms); +14.55s×13(1064ms); gaps between group starts (s): 7.44 6.89; slowest 438 ms
IDLE hi-new-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7155
  new-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7155
  new-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-2-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI new-1-3-shots (shots): cold start TotalTime: 1590
IDLE hi-new-1-3-shots-20-35s (15 s, no input, 16 screenshots inside): frames +38
  CHANGES: 3 of 15 consecutive pairs differ: 02:[0,112][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,648][1080,1328] 10:[0,648][1044,1328] 11:same 12:same 13:same 14:same 15:same 16:same
  new-1-3-shots 20-35s FSTL: 38 frames in the window, 6 group(s) [start×frames(length)]: +0.10s×2(37ms); +0.40s×7(464ms); +1.12s×5(408ms); +7.54s×13(1012ms); +14.57s×8(526ms); +15.36s×3(229ms); gaps between group starts (s): 0.30 0.72 6.42 7.03 0.79; slowest 470 ms
IDLE hi-new-1-3-shots-45-60s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  new-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.9 s)
IDLE hi-new-1-3-shots-90-105s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  new-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.8 s)
HI new-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI new-1-4-dump (dump): cold start TotalTime: 1537
IDLE hi-new-1-4-dump-20-35s (15 s, no input): frames +26; cpu=10.6% (ticks 159 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=6.9%, Jit thread pool=2.3%, g.chyavorec.app=1.3%, DefaultExecutor=0.1%; pid 7998
  new-1-4-dump 20-35s FSTL: 26 frames in the window, 2 group(s) [start×frames(length)]: +5.05s×13(1068ms); +12.10s×13(1085ms); gaps between group starts (s): 7.05; slowest 385 ms
IDLE hi-new-1-4-dump-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7998
  new-1-4-dump 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-4-dump-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7998
  new-1-4-dump 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-4-dump screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
## HI base-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots dump
installed base-1: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI base-1-1-clean (clean): cold start TotalTime: 1337
IDLE hi-base-1-1-clean-20-35s (15 s, no input): frames +36; cpu=14.9% (ticks 223 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=8.5%, Jit thread pool=4.0%, g.chyavorec.app=1.8%, HeapTaskDaemon=0.3%, DefaultExecutor=0.2%; pid 8647
  base-1-1-clean 20-35s FSTL: 39 frames in the window, 3 group(s) [start×frames(length)]: +0.03s×16(1643ms); +7.69s×12(983ms); +14.64s×11(1012ms); gaps between group starts (s): 7.66 6.95; slowest 477 ms
IDLE hi-base-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8647
  base-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8647
  base-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI base-1-2-clean (clean): cold start TotalTime: 1483
IDLE hi-base-1-2-clean-20-35s (15 s, no input): frames +37; cpu=14.4% (ticks 216 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.1%, Jit thread pool=3.4%, g.chyavorec.app=1.5%, DefaultExecutor=0.3%, HeapTaskDaemon=0.1%; pid 8829
  base-1-2-clean 20-35s FSTL: 43 frames in the window, 3 group(s) [start×frames(length)]: +0.01s×20(1969ms); +7.98s×12(931ms); +14.92s×11(737ms); gaps between group starts (s): 7.97 6.94; slowest 430 ms
IDLE hi-base-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8829
  base-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8829
  base-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-2-clean screen at the end: 
HI base-1-3-shots (shots): cold start TotalTime: 1566
IDLE hi-base-1-3-shots-20-35s (15 s, no input, 15 screenshots inside): frames +32
  CHANGES: 2 of 14 consecutive pairs differ: 02:[0,116][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,648][1080,1328] 10:same 11:same 12:same 13:same 14:same 15:same
  base-1-3-shots 20-35s FSTL: 41 frames in the window, 3 group(s) [start×frames(length)]: +0.06s×17(1663ms); +7.73s×12(1055ms); +14.78s×12(1014ms); gaps between group starts (s): 7.68 7.04; slowest 475 ms
IDLE hi-base-1-3-shots-45-60s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  base-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 17.0 s)
IDLE hi-base-1-3-shots-90-105s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  base-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.9 s)
HI base-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI base-1-4-dump (dump): cold start TotalTime: 1589
IDLE hi-base-1-4-dump-20-35s (15 s, no input): frames +26; cpu=11.0% (ticks 165 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=6.8%, Jit thread pool=2.8%, g.chyavorec.app=1.1%, HeapTaskDaemon=0.2%, FrameMetricsAgg=0.1%; pid 9628
  base-1-4-dump 20-35s FSTL: 26 frames in the window, 2 group(s) [start×frames(length)]: +5.11s×13(1070ms); +12.15s×13(1097ms); gaps between group starts (s): 7.05; slowest 400 ms
IDLE hi-base-1-4-dump-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9628
  base-1-4-dump 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-4-dump-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9628
  base-1-4-dump 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-4-dump screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
## HI new-2: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean shots
installed new-2: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-2-1-clean (clean): cold start TotalTime: 1651
IDLE hi-new-2-1-clean-20-35s (15 s, no input): frames +38; cpu=14.8% (ticks 222 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.7%, Jit thread pool=3.1%, g.chyavorec.app=1.7%, DefaultExecutor=0.2%, FrameMetricsAgg=0.1%; pid 10238
  new-2-1-clean 20-35s FSTL: 44 frames in the window, 3 group(s) [start×frames(length)]: +0.03s×18(1728ms); +7.77s×13(1042ms); +14.81s×13(1099ms); gaps between group starts (s): 7.74 7.04; slowest 408 ms
IDLE hi-new-2-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 10238
  new-2-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-2-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 10238
  new-2-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-2-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI new-2-2-shots (shots): cold start TotalTime: 1472
IDLE hi-new-2-2-shots-20-35s (15 s, no input, 16 screenshots inside): frames +34
  CHANGES: 3 of 15 consecutive pairs differ: 02:[0,116][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,648][1080,1328] 10:same 11:same 12:same 13:same 14:same 15:same 16:[0,648][1080,1328]
  new-2-2-shots 20-35s FSTL: 36 frames in the window, 4 group(s) [start×frames(length)]: +0.01s×12(1098ms); +1.36s×1(0ms); +7.37s×11(1004ms); +14.37s×12(957ms); gaps between group starts (s): 1.35 6.01 7.00; slowest 461 ms
IDLE hi-new-2-2-shots-45-60s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  new-2-2-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.8 s)
IDLE hi-new-2-2-shots-90-105s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  new-2-2-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.8 s)
HI new-2-2-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
## HI base-2: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean shots
installed base-2: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI base-2-1-clean (clean): cold start TotalTime: 1560
IDLE hi-base-2-1-clean-20-35s (15 s, no input): frames +36; cpu=15.6% (ticks 234 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.3%, Jit thread pool=4.5%, g.chyavorec.app=1.5%, DefaultExecutor=0.2%, FrameMetricsAgg=0.1%; pid 11440
  base-2-1-clean 20-35s FSTL: 41 frames in the window, 3 group(s) [start×frames(length)]: +0.18s×16(1507ms); +7.70s×13(1107ms); +14.81s×12(867ms); gaps between group starts (s): 7.52 7.11; slowest 452 ms
IDLE hi-base-2-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11440
  base-2-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-2-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11440
  base-2-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-2-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI base-2-2-shots (shots): cold start TotalTime: 1359
IDLE hi-base-2-2-shots-20-35s (15 s, no input, 16 screenshots inside): frames +38
  CHANGES: 3 of 15 consecutive pairs differ: 02:[0,116][1080,1328] 03:same 04:same 05:same 06:same 07:same 08:same 09:[0,648][1080,1328] 10:same 11:same 12:same 13:same 14:same 15:same 16:[0,648][1080,1328]
  base-2-2-shots 20-35s FSTL: 38 frames in the window, 4 group(s) [start×frames(length)]: +0.11s×16(1566ms); +7.69s×12(1085ms); +14.77s×9(765ms); +15.84s×1(0ms); gaps between group starts (s): 7.58 7.08 1.07; slowest 517 ms
IDLE hi-base-2-2-shots-45-60s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  base-2-2-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.8 s)
IDLE hi-base-2-2-shots-90-105s (15 s, no input, 18 screenshots inside): frames +0
  CHANGES: 0 of 17 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same 17:same 18:same
  base-2-2-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.9 s)
HI base-2-2-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
## HI totals: frames in each 15 s window (label: build-block-run-mode)
HI-TOTAL base clean 20-35s: 36 37 36
HI-TOTAL base clean 45-60s: 0 0 0
HI-TOTAL base clean 90-105s: 0 0 0
HI-TOTAL base dump 20-35s: 26
HI-TOTAL base dump 45-60s: 0
HI-TOTAL base dump 90-105s: 0
HI-TOTAL base shots 20-35s: 32 38
HI-TOTAL base shots 45-60s: 0 0
HI-TOTAL base shots 90-105s: 0 0
HI-TOTAL new clean 20-35s: 38 36 38
HI-TOTAL new clean 45-60s: 0 0 0
HI-TOTAL new clean 90-105s: 0 0 0
HI-TOTAL new dump 20-35s: 26
HI-TOTAL new dump 45-60s: 0
HI-TOTAL new dump 90-105s: 0
HI-TOTAL new shots 20-35s: 38 34
HI-TOTAL new shots 45-60s: 0 0
HI-TOTAL new shots 90-105s: 0 0
contact sheets: 3 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 3 | Първи кадър: медиана 1186 мс, последен 1186 мс | Пълно показване: медиана 1768 мс, последно 1768 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 3" text="Първи кадър: медиана 1186 мс, последен 1186 мс" text="Пълно показване: медиана 1768 мс, последно 1768 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['6536', '6961', '7155', '7359', '7998', '8349', '8647', '8829', '9009', '9628', '9941', '10238', '10426', '11135', '11440', '11618']): 70 in 9 groups
      17  E/eglCodecCommon: glUtilsParamSize: unknow param #
      16  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
      16  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      16  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#f#c#f-caa#-#e#-#fd#-fbb#e#, generation=#)
       1  W/g.chyavorec.ap: Long monitor contention with owner DefaultDispatcher-worker-# (#) at void java.lang.Object.wait()(Object.java:
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#-#ec-#f#b-#e#-eaf#b#e#a, generation=#)
       1  W/g.chyavorec.ap: No such thread id for suspend: #
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=ad#a#f#-#d-#-b#cf-b#e#f#, generation=#)
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['6536', '6961', '7155', '7359', '7998', '8349', '8647', '8829', '9009', '9628', '9941', '10238', '10426', '11135', '11440', '11618']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 17
   skipped-frames events=17 max=43 total=577
== Davey: 18
   davey events=18 max=925ms median=806ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## idle33

```
part idle33; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## HI new-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots dump
installed new-1: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-1-1-clean (clean): cold start TotalTime: 5098
IDLE hi-new-1-1-clean-20-35s (15 s, no input): frames +33; cpu=10.5% (ticks 158 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.3%, g.chyavorec.app=0.9%, Jit thread pool=0.3%; pid 6131
  new-1-1-clean 20-35s FSTL: 38 frames in the window, 11 group(s) [start×frames(length)]: +0.12s×3(367ms); +1.10s×4(417ms); +1.83s×7(917ms); +3.02s×1(0ms); +3.42s×5(733ms); +4.47s×3(417ms); +5.18s×1(0ms); +5.45s×1(0ms); +9.22s×6(350ms); +9.85s×3(383ms); +16.47s×4(67ms); gaps between group starts (s): 0.98 0.73 1.18 0.40 1.05 0.72 0.27 3.77 0.63 6.62; slowest 805 ms
IDLE hi-new-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 6131
  new-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.3 s)
IDLE hi-new-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 6131
  new-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI new-1-2-clean (clean): cold start TotalTime: 3982
IDLE hi-new-1-2-clean-20-35s (15 s, no input): frames +34; cpu=11.4% (ticks 171 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.8%, g.chyavorec.app=0.8%, Jit thread pool=0.7%, binder:7826_5=0.1%; pid 7826
  new-1-2-clean 20-35s FSTL: 35 frames in the window, 8 group(s) [start×frames(length)]: +0.13s×1(0ms); +0.50s×5(483ms); +1.30s×1(0ms); +1.66s×7(767ms); +2.76s×2(67ms); +3.31s×1(0ms); +3.60s×8(1133ms); +8.85s×10(1183ms); gaps between group starts (s): 0.37 0.80 0.37 1.10 0.55 0.28 5.25; slowest 665 ms
IDLE hi-new-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7826
  new-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7826
  new-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-2-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI new-1-3-shots (shots): cold start TotalTime: 3510
IDLE hi-new-1-3-shots-20-35s (15 s, no input, 11 screenshots inside): frames +33
  CHANGES: 4 of 10 consecutive pairs differ: 02:[0,108][1080,1392] 03:[8,312][1072,384] 04:same 05:same 06:same 07:[0,712][1080,1392] 08:[0,712][1048,1392] 09:same 10:same 11:same
  new-1-3-shots 20-35s FSTL: 41 frames in the window, 11 group(s) [start×frames(length)]: +0.15s×1(0ms); +0.52s×1(0ms); +0.84s×11(1683ms); +2.97s×7(1067ms); +4.30s×3(317ms); +8.69s×6(417ms); +9.44s×3(200ms); +15.95s×4(50ms); +16.27s×3(400ms); +16.94s×1(0ms); +17.22s×1(0ms); gaps between group starts (s): 0.37 0.32 2.13 1.33 4.38 0.75 6.52 0.32 0.67 0.28; slowest 798 ms
IDLE hi-new-1-3-shots-45-60s (15 s, no input, 15 screenshots inside): frames +0
  CHANGES: 0 of 14 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same
  new-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.1 s)
IDLE hi-new-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
  CHANGES: 0 of 14 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same
  new-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.2 s)
HI new-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI new-1-4-dump (dump): cold start TotalTime: 3675
IDLE hi-new-1-4-dump-20-35s (15 s, no input): frames +18; cpu=7.3% (ticks 109 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=5.7%, g.chyavorec.app=0.8%, Jit thread pool=0.7%, Profile Saver=0.1%; pid 8743
  new-1-4-dump 20-35s FSTL: 19 frames in the window, 3 group(s) [start×frames(length)]: +2.68s×5(233ms); +3.21s×4(417ms); +9.80s×10(1167ms); gaps between group starts (s): 0.53 6.58; slowest 635 ms
IDLE hi-new-1-4-dump-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8743
  new-1-4-dump 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-1-4-dump-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8743
  new-1-4-dump 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-1-4-dump screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
## HI base-1: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean clean shots
installed base-1: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI base-1-1-clean (clean): cold start TotalTime: 3011
IDLE hi-base-1-1-clean-20-35s (15 s, no input): frames +38; cpu=15.5% (ticks 233 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=13.7%, g.chyavorec.app=0.9%, Jit thread pool=0.7%, DefaultExecutor=0.2%; pid 9629
  base-1-1-clean 20-35s FSTL: 41 frames in the window, 5 group(s) [start×frames(length)]: +0.09s×2(217ms); +0.64s×23(3767ms); +8.76s×9(933ms); +15.81s×5(250ms); +16.34s×2(17ms); gaps between group starts (s): 0.55 8.12 7.05 0.53; slowest 621 ms
IDLE hi-base-1-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9629
  base-1-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9629
  base-1-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI base-1-2-clean (clean): cold start TotalTime: 3864
IDLE hi-base-1-2-clean-20-35s (15 s, no input): frames +35; cpu=13.1% (ticks 197 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=11.4%, g.chyavorec.app=0.9%, Jit thread pool=0.9%; pid 9800
  base-1-2-clean 20-35s FSTL: 37 frames in the window, 6 group(s) [start×frames(length)]: +0.01s×3(367ms); +0.68s×4(483ms); +1.46s×12(1833ms); +3.59s×8(1183ms); +9.24s×4(50ms); +9.56s×6(900ms); gaps between group starts (s): 0.67 0.78 2.13 5.65 0.32; slowest 697 ms
IDLE hi-base-1-2-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9800
  base-1-2-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-base-1-2-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9800
  base-1-2-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI base-1-2-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
HI base-1-3-shots (shots): cold start TotalTime: 3065
IDLE hi-base-1-3-shots-20-35s (15 s, no input, 11 screenshots inside): frames +35
  CHANGES: 4 of 10 consecutive pairs differ: 02:[0,108][1080,1392] 03:[0,312][1068,1392] 04:same 05:same 06:same 07:[0,716][1080,1340] 08:[0,712][1080,1392] 09:same 10:same 11:same
  base-1-3-shots 20-35s FSTL: 45 frames in the window, 9 group(s) [start×frames(length)]: +0.27s×16(2417ms); +2.95s×1(0ms); +3.32s×5(600ms); +4.32s×4(550ms); +9.05s×8(833ms); +10.15s×1(0ms); +16.27s×5(283ms); +16.85s×2(83ms); +17.23s×3(450ms); gaps between group starts (s): 2.68 0.37 1.00 4.73 1.10 6.12 0.58 0.38; slowest 887 ms
IDLE hi-base-1-3-shots-45-60s (15 s, no input, 16 screenshots inside): frames +0
  CHANGES: 0 of 15 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same 16:same
  base-1-3-shots 45-60s FSTL: 0 frames in the window (120 in the buffer; window 16.9 s)
IDLE hi-base-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
  CHANGES: 0 of 14 consecutive pairs differ: 02:same 03:same 04:same 05:same 06:same 07:same 08:same 09:same 10:same 11:same 12:same 13:same 14:same 15:same
  base-1-3-shots 90-105s FSTL: 0 frames in the window (120 in the buffer; window 16.2 s)
HI base-1-3-shots screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
## HI new-2: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): clean
installed new-2: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HI new-2-1-clean (clean): cold start TotalTime: 3396
IDLE hi-new-2-1-clean-20-35s (15 s, no input): frames +39; cpu=14.9% (ticks 223 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=12.9%, g.chyavorec.app=0.9%, Jit thread pool=0.9%, binder:11174_2=0.1%, DefaultExecutor=0.1%; pid 11174
  new-2-1-clean 20-35s FSTL: 43 frames in the window, 7 group(s) [start×frames(length)]: +0.02s×1(0ms); +0.40s×11(1383ms); +2.09s×4(500ms); +2.94s×11(1733ms); +8.42s×9(950ms); +15.54s×5(250ms); +16.15s×2(17ms); gaps between group starts (s): 0.38 1.68 0.85 5.48 7.12 0.62; slowest 688 ms
IDLE hi-new-2-1-clean-45-60s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11174
  new-2-1-clean 45-60s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
IDLE hi-new-2-1-clean-90-105s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11174
  new-2-1-clean 90-105s FSTL: 0 frames in the window (120 in the buffer; window 15.2 s)
HI new-2-1-clean screen at the end: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
## HI totals: frames in each 15 s window (label: build-block-run-mode)
HI-TOTAL base clean 20-35s: 38 35
HI-TOTAL base clean 45-60s: 0 0
HI-TOTAL base clean 90-105s: 0 0
HI-TOTAL base shots 20-35s: 35
HI-TOTAL base shots 45-60s: 0
HI-TOTAL base shots 90-105s: 0
HI-TOTAL new clean 20-35s: 33 34 39
HI-TOTAL new clean 45-60s: 0 0 0
HI-TOTAL new clean 90-105s: 0 0 0
HI-TOTAL new dump 20-35s: 18
HI-TOTAL new dump 45-60s: 0
HI-TOTAL new dump 90-105s: 0
HI-TOTAL new shots 20-35s: 33
HI-TOTAL new shots 45-60s: 0
HI-TOTAL new shots 90-105s: 0
## I0. CPU and frames at rest on every screen (1.2.35)
## W. First launch and warm-up
first-install catalogue results after 3750 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## I. CPU and frames with no input: 30 s after opening each screen, then a 15 s window (should be ~0 frames, ~0% CPU)
IDLE home (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE catalog (15 s, no input): frames +30; cpu=1.7% (ticks 25 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.9%, g.chyavorec.app=0.5%, binder:11770_2=0.1%, FrameMetricsAgg=0.1%, DefaultExecutor=0.1%; pid 11770
IDLE catalog-shelf (15 s, no input): frames +31; cpu=1.7% (ticks 25 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.9%, g.chyavorec.app=0.5%, binder:11770_5=0.1%, binder:11770_2=0.1%, FrameMetricsAgg=0.1%; pid 11770
IDLE book (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE surprise-result (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE home-search-expanded (15 s, no input): frames +30; cpu=1.7% (ticks 25 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=1.0%, g.chyavorec.app=0.4%, binder:11770_4=0.1%, binder:11770_4=0.1%, Jit thread pool=0.1%; pid 11770
IDLE my (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE card (15 s, no input): frames +0; cpu=0.2% (ticks 3 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.2%; pid 11770
IDLE card-full (15 s, no input): frames +0; cpu=0.3% (ticks 4 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.3%; pid 11770
IDLE want (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE news (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE article (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE calendar (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE event (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: 3 chyavorec.org=0.1%; pid 11770
IDLE more (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE gallery (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE album (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE viewer (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE settings (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE diagnostics (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE contacts (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE documents (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 11770
IDLE background (30 s, no input): frames +0; cpu=0.2% (ticks 6 in 30 s; 100 ticks = 1 cpu-s) threads: HeapTaskDaemon=0.1%, ReferenceQueueD=0.1%; pid 11770
## I2. Screens that kept drawing (frames > 0 in the 15 s window)
IDLE hi-new-1-1-clean-20-35s (15 s, no input): frames +33; cpu=10.5% (ticks 158 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.3%, g.chyavorec.app=0.9%, Jit thread pool=0.3%; pid 6131
IDLE hi-new-1-2-clean-20-35s (15 s, no input): frames +34; cpu=11.4% (ticks 171 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=9.8%, g.chyavorec.app=0.8%, Jit thread pool=0.7%, binder:7826_5=0.1%
IDLE hi-new-1-3-shots-20-35s (15 s, no input, 11 screenshots inside): frames +33
IDLE hi-new-1-3-shots-45-60s (15 s, no input, 15 screenshots inside): frames +0
IDLE hi-new-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
IDLE hi-new-1-4-dump-20-35s (15 s, no input): frames +18; cpu=7.3% (ticks 109 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=5.7%, g.chyavorec.app=0.8%, Jit thread pool=0.7%, Profile Saver=0.1%; 
IDLE hi-base-1-1-clean-20-35s (15 s, no input): frames +38; cpu=15.5% (ticks 233 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=13.7%, g.chyavorec.app=0.9%, Jit thread pool=0.7%, DefaultExecutor=
IDLE hi-base-1-2-clean-20-35s (15 s, no input): frames +35; cpu=13.1% (ticks 197 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=11.4%, g.chyavorec.app=0.9%, Jit thread pool=0.9%; pid 9800
IDLE hi-base-1-3-shots-20-35s (15 s, no input, 11 screenshots inside): frames +35
IDLE hi-base-1-3-shots-45-60s (15 s, no input, 16 screenshots inside): frames +0
IDLE hi-base-1-3-shots-90-105s (15 s, no input, 15 screenshots inside): frames +0
IDLE hi-new-2-1-clean-20-35s (15 s, no input): frames +39; cpu=14.9% (ticks 223 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=12.9%, g.chyavorec.app=0.9%, Jit thread pool=0.9%, binder:11174_2=0.
IDLE catalog (15 s, no input): frames +30; cpu=1.7% (ticks 25 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.9%, g.chyavorec.app=0.5%, binder:11770_2=0.1%, FrameMetricsAgg=0.1%, DefaultExecutor
IDLE catalog-shelf (15 s, no input): frames +31; cpu=1.7% (ticks 25 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=0.9%, g.chyavorec.app=0.5%, binder:11770_5=0.1%, binder:11770_2=0.1%, FrameMetri
IDLE home-search-expanded (15 s, no input): frames +30; cpu=1.7% (ticks 25 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=1.0%, g.chyavorec.app=0.4%, binder:11770_4=0.1%, binder:11770_4=0.1%, Jit
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 3 | Първи кадър: медиана 2923 мс, последен 2923 мс | Пълно показване: медиана 3629 мс, последно 3629 мс | Топъл старт · 1 | Първи кадър: медиана 1028 мс, последен 1028 мс | Насечени кадри по екрани | my:
DIAG SCREEN (scrolled): text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 3" text="Първи кадър: медиана 2923 мс, последен 2923 мс" text="Пълно показване: медиана 3629 мс, последно 3629 мс" text="Топъл старт · 1" text="Първи кадър: медиана 1028 мс, последен 1028 мс" text="Насечени кадри по екрани" text="my: 2,8 % от 36 кадъра · най-бавен 434 мс" text="общо с видеокартата: средно 397 мс, най-бавен 779 мс" text="more: 1,7 % от 117 кадъра · най-бавен 386 мс" text="общо с видеокартата: средно 358 мс, най-бавен 715 мс" text="home: 1,5 % от 202 кадъра · най-бавен 1307 мс" text="общо с видеокартата: средно 478 мс, най-бавен 1552 мс" text="card: 0,6 % от 165 кадъра · най-бавен 376 мс" text="общо с виде
APP W/E lines (pids ['4929', '6131', '7826', '8086', '8743', '9315', '9629', '9800', '10054', '10853', '11174', '11770']): 95 in 13 groups
      14  E/OpenGLRenderer: Unable to match the desired swap behavior.
      13  W/Parcel: Expecting binder but got null!
      13  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      13  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      12  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      12  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      12  W/Gralloc4: allocator #.x is not supported
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=f#b-#b#-#-b#-#dd#b#, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner OkHttp https://chyavorec.org/... (#) at android.security.net.config.Network
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#fa#-fb#-#-#a-#c#ed#b#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=b#fae#c-#c#-#f#b-abb#-#af#b#d#, generation=#)
       1  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object d#.s(java.lang.Object)(r
contact sheets: 6 in sheets/
contact sheets: 6 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4929', '6131', '7826', '8086', '8743', '9315', '9629', '9800', '10054', '10853', '11174', '11770']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 72
   skipped-frames events=72 max=106 total=3707
== Davey: 137
   davey events=137 max=3040ms median=952ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## mem36

```
part mem36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3188 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=47417KB  Java=7672KB  Native=22692KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=114496KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=55106KB  Java=8500KB  Native=22920KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=124160KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=88873KB  Java=11504KB  Native=37380KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=160724KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=120402KB  Java=17836KB  Native=52888KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=184288KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=104812KB  Java=10980KB  Native=44372KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=168696KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=117072KB  Java=11060KB  Native=54400KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=180892KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=127914KB  Java=15852KB  Native=59592KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=192472KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=122018KB  Java=11016KB  Native=58568KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=186576KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=126452KB  Java=11132KB  Native=62752KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=192128KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=133316KB  Java=12824KB  Native=67320KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=199520KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=130712KB  Java=11064KB  Native=66520KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=196916KB  Views=13 Activities=1
pid after cycles: 7185; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=55106 c5=88873 c10=104812 c15=117072 c20=122018 c25=126452 c30=130712
LEAK TREND (cycles 10-30, least squares): +1224 KB per cycle (+23.9 MB over 20 cycles)
MEMTABLE new 1.2.36 (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)
  c00 raw  PSS=46.3  Java=7.5  Native=22.2  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=2.8  RSS=111.8
  c00 trim PSS=53.8  Java=8.3  Native=22.4  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.5  RSS=121.2
  c10 raw  PSS=117.6  Java=17.4  Native=51.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.3  RSS=180.0
  c10 trim PSS=102.4  Java=10.7  Native=43.3  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.1  RSS=164.7
  c20 raw  PSS=124.9  Java=15.5  Native=58.2  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.2  RSS=188.0
  c20 trim PSS=119.2  Java=10.8  Native=57.2  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.2  RSS=182.2
  c30 raw  PSS=130.2  Java=12.5  Native=65.7  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.2  RSS=194.8
  c30 trim PSS=127.6  Java=10.8  Native=65.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.2  RSS=192.3
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5490', '7185']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 48
   skipped-frames events=48 max=77 total=1741
== Davey: 162
   davey events=162 max=1702ms median=812ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## memb36

```
part memb36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
installed base: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3247 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=51610KB  Java=7240KB  Native=12520KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=8)  RSS=110388KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=52837KB  Java=7168KB  Native=12608KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=3)  RSS=107796KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=98518KB  Java=11960KB  Native=44756KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=168316KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=123257KB  Java=18096KB  Native=63500KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=189940KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=113975KB  Java=11356KB  Native=61012KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=180668KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=120128KB  Java=11432KB  Native=63740KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=185500KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=138666KB  Java=22892KB  Native=70668KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=205764KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=120758KB  Java=11456KB  Native=64412KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=187856KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=125305KB  Java=11532KB  Native=69228KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=193792KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=137731KB  Java=18228KB  Native=74392KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=25)  RSS=206596KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=124427KB  Java=11536KB  Native=67996KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=25)  RSS=193292KB  Views=13 Activities=1
pid after cycles: 6768; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=52837 c5=98518 c10=113975 c15=120128 c20=120758 c25=125305 c30=124427
LEAK TREND (cycles 10-30, least squares): +522 KB per cycle (+10.2 MB over 20 cycles)
MEMTABLE base 1.2.35 (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)
  c00 raw  PSS=50.4  Java=7.1  Native=12.2  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=2.3  RSS=107.8
  c00 trim PSS=51.6  Java=7.0  Native=12.3  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=8.3  RSS=105.3
  c10 raw  PSS=120.4  Java=17.7  Native=62.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.4  PrivOther=9.5  RSS=185.5
  c10 trim PSS=111.3  Java=11.1  Native=59.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.4  PrivOther=9.4  RSS=176.4
  c20 raw  PSS=135.4  Java=22.4  Native=69.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.4  PrivOther=9.7  RSS=200.9
  c20 trim PSS=117.9  Java=11.2  Native=62.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.4  PrivOther=9.5  RSS=183.5
  c30 raw  PSS=134.5  Java=17.8  Native=72.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.4  PrivOther=9.7  RSS=201.8
  c30 trim PSS=121.5  Java=11.3  Native=66.4  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.4  PrivOther=9.5  RSS=188.8
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5370', '6768']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 61
   skipped-frames events=61 max=58 total=2172
== Davey: 158
   davey events=158 max=1176ms median=803ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## mon36

```
part mon36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3889 ms
## K. Monkey 6000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=7236
MONKEY exit=0 duration=304 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '7236'; airplane=0; camera: 
MEM after-monkey (raw) PSS=95465KB  Java=23768KB  Native=23304KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=137300KB  Views=19 Activities=1
MEM after-monkey (trimmed) PSS=75146KB  Java=9240KB  Native=21836KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=128572KB  Views=15 Activities=1
MON36 round 1 (seed 1357): whole log so far: FATAL=0
## K2. Monkey 6000 events, seed 4242
## W. First launch and warm-up
first-install catalogue results after 2874 ms
## K. Monkey 6000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=8842
MONKEY exit=0 duration=332 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '8842'; airplane=0; camera: 
MEM after-monkey (raw) PSS=110894KB  Java=20536KB  Native=49228KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=26)  RSS=196452KB  Views=30 Activities=1
MEM after-monkey (trimmed) PSS=103506KB  Java=13100KB  Native=48068KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=22)  RSS=194360KB  Views=24 Activities=1
MON36 round 2 done: whole log FATAL=0 ANR lines=0
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: tab bar not reached
WARN: Settings not found
APP W/E lines (pids ['5871', '7236', '8596', '8842']): 3925 in 43 groups
    3556  W/InputEventSender: Received 'finished' signal for unknown seq number = #
      94  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
      75  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
      41  W/Parcel: Expecting binder but got null!
      41  E/OpenGLRenderer: Unable to match the desired swap behavior.
      16  W/System: A resource failed to call release. 
       9  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       9  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       9  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
       8  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
       5  W/RemoteInputConnectionImpl: setComposingText on inactive InputConnection
       5  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, metaSt
       4  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       4  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       4  W/Gralloc4: allocator #.x is not supported
       4  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, metaSta
       4  W/InputMethodManager: Timeout waiting for IME to handle input event after # ms: com.google.android.inputmethod.latin/com.android.inp
       4  E/ImeBackDispatcher: Ime callback not found. Ignoring unregisterReceivedCallback. callbackId: #
       3  W/VRI[MainActivity]: Dropping event due to root view being removed: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]
       3  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[#]=-#.
       2  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       2  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCod
       2  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scan
       2  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCo
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#c#d#-c#ca-#b-#b#-#a#f#f, generation=#)
       1  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, 
       1  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[#]=#.#
       1  W/InputMethodManager: Ignoring showSoftInput() as view=k#{e#c# VFED..... ......ID #,#-#,# aid=#} is not served.
       1  W/VRI[MainActivity]: Dropping event due to root view being removed: MotionEvent { action=ACTION_DOWN, actionButton=#, id[#]=#, x[#]
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5871', '7236', '8596', '8842']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 67
   skipped-frames events=67 max=106 total=2574
== Davey: 173
   davey events=173 max=2510ms median=817ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## new26

```
part new26; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 1848 ms
## NP-bg-light (full). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP bg-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · 26 септември 2026 · Сканирай · Читателска карта · Искам да прочета · Контакти
CAP bg-light/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP bg-light/01d-home-4: targets=16 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP bg-light/01e-home-5: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Далечният морето · Дългият пътя · Дъхът на планините · НОВИНИ · Още новини · Всички
CAP bg-light/02-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-light/02b-more-2: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
CAP bg-light/02c-more-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
CAP bg-light/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 15
CAP bg-light/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 15 → 6
EMB redo: 6 → 15
EMB four-way mirror, 2 taps: 15 → 23 (+8 expected)
CAP bg-light/em-03-mirror: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-04-lower: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-05-template: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB save: text="Моите шевици" 
CAP bg-light/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP bg-light/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
CAP bg-light/np-01b-list-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=18 ellipsized=0 | СТАТИЯ 02 · СПОМЕНИ · Баба Дамяна разказва (продъл · Възстановяването на селото с · СТАТИЯ 03 · СПОМЕНИ · Читалището — покровител и за · Спомени за читалището в запа
NEWSPAPER first open: page label after 1733 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=238 std=34.2
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
touch device: /dev/input/event1 x-max=32767 y-max=32767 pressure=1
NEWSPAPER double tap: diff vs page 0.01
NEWSPAPER pinch: diff vs page 30.98
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=0
NEWSPAPER reopened OFFLINE: after 1675 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP bg-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=240 std=33.8
CAP bg-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-light/tl-02: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 1909. Организирано читалищно · Организирано читалищно дело · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре
CAP bg-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP bg-light/tl-04: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | 1927. Въздържателно дружеств · Основано от активни читалищн · 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново
CAP bg-light/tl-05: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | 1947. Решение за собствена с · Решение за собствена сграда · Започва отчуждаване на терен · 17 март 1958. Първа копка. С · Първа копка · Самооблагане 3% по Закона за
CAP bg-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
CAP bg-light/tl-07: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1223 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Първа копка" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Решение за
CAP bg-light/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP bg-light/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP bg-light/qz-02-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Въздържателно дружество „Бор · Театрален кръжец и зараждане · Решение за собствена сграда
CAP bg-light/qz-03-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 1 верни от 3 отговорени, общ · Въпрос 3 от 5 · Коя от тези книги е написана · Черната кутия: Ниско прелита · Sanktus · Площад Диамант
CAP bg-light/qz-04-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 1 верни от 4 отговорени, общ · Въпрос 4 от 5 · Коя от тези книги е написана · Конспирацията · Крадците на мрака · Търсачите
CAP bg-light/qz-05-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 1 верни от 5 отговорени, общ · Въпрос 5 от 5 · Какво се случва в читалището · Театрален кръжец и зараждане · Дружество „Воля“ · Първият радиоапарат в селото
CAP bg-light/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
CAP bg-light/qz-07-again: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ again the same day: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
CAP bg-light/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-light/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP bg-light/st-03: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията · Заемания, събития, съобщения · Обновления
CAP bg-light/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-light/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP bg-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
NP bg-light: FATAL(any process)=0 of the app=0 ANR=0; app process 6130; logcat app errors: 0
app theme → =Тъмна|=Dark: text="Изглед" text="Тема" text="Тъмна" text="Размер на текста" 
## NP-bg-dark (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP bg-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-dark/02-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-dark/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 49 → after 6 taps + a stroke 53
CAP bg-dark/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 53 → 48
EMB redo: 48 → 53
EMB save: text="Моите шевици" 
CAP bg-dark/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP bg-dark/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 1749 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-dark/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=29 std=25.2
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=0
NEWSPAPER reopened OFFLINE: after 1761 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP bg-dark/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=30 std=25.1
CAP bg-dark/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-dark/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP bg-dark/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1252 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP bg-dark/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP bg-dark/qz-01-answered: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Галерия · Снимки от сайта · Извези шевица · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник
QUIZ q1: no next button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text
CAP bg-dark/qz-06-result: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
QUIZ result: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="
QUIZ: no share button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" 
CAP bg-dark/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP bg-dark/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP bg-dark/st-03: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията · Заемания, събития, съобщения
CAP bg-dark/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-dark/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
NP bg-dark: FATAL(any process)=0 of the app=0 ANR=0; app process 11172; logcat app errors: 0
app theme → =Както системата|=System default: text="Изглед" text="Тема" text="Както системата" text="Размер на текста" 
## NP-en-light (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP en-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP en-light/02-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP en-light/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 52 → after 6 taps + a stroke 43
CAP en-light/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 43 → 52
EMB redo: 52 → 43
EMB save: text="Моите шевици" 
CAP en-light/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP en-light/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 1665 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP en-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=238 std=34.2
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=0
NEWSPAPER reopened OFFLINE: after 1808 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP en-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=240 std=33.8
CAP en-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP en-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP en-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1230 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP en-light/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP en-light/qz-01-answered: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Галерия · Снимки от сайта · Извези шевица · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник
QUIZ q1: no next button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text
CAP en-light/qz-06-result: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
QUIZ result: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="
QUIZ: no share button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" 
CAP en-light/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP en-light/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP en-light/st-03: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията · Заемания, събития, съобщения
CAP en-light/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP en-light/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
NP en-light: FATAL(any process)=0 of the app=0 ANR=0; app process 14352; logcat app errors: 0
## NA. Accessibility over all new screens
A11Y AGG: 77 screens analysed, 0 dumps failed; totals MISSING=21 DOUBLE=0 SAME-LABEL=4 SMALL(<48dp)=8
== MISSING: 11 distinct
   View@[42,1764][524,1938]  [1 screens: 02-more]
   View@[556,1764][1038,1938]  [1 screens: 02-more]
   View@[0,2249][1080,2274]  [1 screens: st-03]
   View@[121,2124][340,2274]  [1 screens: 11-event]
   View@[408,2124][597,2274]  [1 screens: 11-event]
   View@[665,2124][959,2274]  [1 screens: 11-event]
   View@[42,0][378,13]  [1 screens: 01d-home-4]
   View@[415,0][751,13]  [1 screens: 01d-home-4]
   View@[788,0][1080,13]  [1 screens: 01d-home-4]
   View@[0,1936][1080,2062]  [1 screens: 02b-more-2]
   View@[0,231][1080,235]  [1 screens: st-03]
== SMALL: 2 distinct
   'Изчисти платното'  [3 screens: em-01-open, em-02-stitched, em-03-mirror]
   'СТАТИЯ 07 · ПУБЛИЦИСТИКА'  [1 screens: np-01b-list-2]
== SAME-LABEL: 1 distinct
   'Назад' x2  [3 screens: qz-01-q1, qz-06-result, qz-07-again]
== DOUBLE: 0 distinct
## NP-D. Diagnostics › Бързодействие
PERF CARD new36: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: няма | Студен старт · 4 | Първи кадър: медиана 1138 мс, последен 1320 мс | Пълно показване: медиана 1463 мс, последно 1564 мс | Няма записани грешки. | Диагностика
PERF CARD new36 (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: няма | Студен старт · 4 | Първи кадър: медиана 1138 мс, последен 1320 мс | Пълно показване: медиана 1463 мс, последно 1564 мс | Няма записани грешки. | Диагностика
contact sheets: 11 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: няма | Студен старт · 4 | Първи кадър: медиана 1138 мс, последен 1320 мс | Пълно показване: медиана 1463 мс, последно 1564 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: няма" text="Студен старт · 4" text="Първи кадър: медиана 1138 мс, последен 1320 мс" text="Пълно показване: медиана 1463 мс, последно 1564 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5347', '6130', '11172', '14352']): 21 in 6 groups
       4  W/zygote64: Unexpected CPU variant for X# using defaults: x#_#
       4  E/ResourceType: Style contains key with bad entry: #
       4  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       4  E/eglCodecCommon: glUtilsParamSize: unknow param #
       4  W/ImageReader_JNI: Unable to acquire a buffer item, very likely client tried to acquire more than maxImages buffers
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ee#c-#d#b-#f#-bbdc-#fc#b#b#, generation=#)
contact sheets: 12 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5347', '6130', '11172', '14352']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=41 total=177
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## new28

```
part new28; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15982 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 10671 ms
## NP-bg-light (full). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP bg-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Последни новини · С хоро и песен във Водица · 26 септември 2026 · Сканирай · Читателска карта · Искам да прочета
CAP bg-light/01c-home-3: targets=16 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP bg-light/01d-home-4: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА · Най търсени · Далечният морето
CAP bg-light/01e-home-5: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Далечният морето · Дългият пътя · Дъхът на планините · НОВИНИ · Още новини · Всички
CAP bg-light/02-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-light/02b-more-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
CAP bg-light/02c-more-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
CAP bg-light/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 16
CAP bg-light/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 16 → 6
EMB redo: 6 → 16
EMB four-way mirror, 2 taps: 16 → 24 (+8 expected)
CAP bg-light/em-03-mirror: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-04-lower: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-05-template: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB save: text="Моите шевици" 
CAP bg-light/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP bg-light/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
CAP bg-light/np-01b-list-2: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=17 ellipsized=0 | СТАТИЯ 02 · СПОМЕНИ · Баба Дамяна разказва (продъл · Възстановяването на селото с · СТАТИЯ 03 · СПОМЕНИ · Читалището — покровител и за · Спомени за читалището в запа
NEWSPAPER first open: page label after 2436 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=238 std=33.8
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
touch device: /dev/input/event1 x-max=32767 y-max=32767 pressure=1
NEWSPAPER double tap: diff vs page 0.02
NEWSPAPER pinch: diff vs page 25.58
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 1939 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=238 std=33.8
CAP bg-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-light/tl-02: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP bg-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP bg-light/tl-04: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | 1927. Въздържателно дружеств · 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново · Дружество „Воля“
CAP bg-light/tl-05: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | 1936. Дружество „Воля“. Ново · 1947. Решение за собствена с · Решение за собствена сграда · Започва отчуждаване на терен · 17 март 1958. Първа копка. С · Първа копка
CAP bg-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
CAP bg-light/tl-07: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1318 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Първа копка" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Решение за
CAP bg-light/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP bg-light/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP bg-light/qz-02-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Въздържателно дружество „Бор · Театрален кръжец и зараждане · Решение за собствена сграда
CAP bg-light/qz-03-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 1 верни от 3 отговорени, общ · Въпрос 3 от 5 · Коя от тези книги е написана · Черната кутия: Ниско прелита · Sanktus · Площад Диамант
CAP bg-light/qz-04-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 1 верни от 4 отговорени, общ · Въпрос 4 от 5 · Коя от тези книги е написана · Конспирацията · Крадците на мрака · Търсачите
CAP bg-light/qz-05-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 1 верни от 5 отговорени, общ · Въпрос 5 от 5 · Какво се случва в читалището · Театрален кръжец и зараждане · Дружество „Воля“ · Първият радиоапарат в селото
CAP bg-light/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
CAP bg-light/qz-07-again: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ again the same day: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
CAP bg-light/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-light/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP bg-light/st-03: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията
CAP bg-light/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-light/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP bg-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
NP bg-light: FATAL(any process)=0 of the app=0 ANR=0; app process 6822; logcat app errors: 0
app theme → =Тъмна|=Dark: text="Изглед" text="Тема" text="Тъмна" text="Размер на текста" 
## NP-bg-dark (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP bg-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-dark/02-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-dark/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 49 → after 6 taps + a stroke 53
CAP bg-dark/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 53 → 48
EMB redo: 48 → 53
EMB save: text="Моите шевици" 
CAP bg-dark/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP bg-dark/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 1896 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-dark/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=30 std=25.1
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2136 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP bg-dark/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=30 std=25.0
CAP bg-dark/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-dark/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP bg-dark/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1294 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP bg-dark/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP bg-dark/qz-01-answered: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=26 ellipsized=0 | Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта
QUIZ q1: no next button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text
CAP bg-dark/qz-06-result: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
QUIZ result: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="
QUIZ: no share button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" 
CAP bg-dark/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP bg-dark/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP bg-dark/st-03: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията
CAP bg-dark/st-04: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-dark/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
NP bg-dark: FATAL(any process)=0 of the app=0 ANR=0; app process 11662; logcat app errors: 0
app theme → =Както системата|=System default: text="Изглед" text="Тема" text="Както системата" text="Размер на текста" 
## NP-en-light (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP en-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP en-light/02-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP en-light/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 52 → after 6 taps + a stroke 44
CAP en-light/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 44 → 52
EMB redo: 52 → 44
EMB save: 
CAP en-light/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 1 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP en-light/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 1996 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP en-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=238 std=33.8
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2060 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP en-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=240 std=33.4
CAP en-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP en-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP en-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1282 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP en-light/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP en-light/qz-01-answered: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Галерия · Снимки от сайта · Извези шевица · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник
QUIZ q1: no next button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text
CAP en-light/qz-06-result: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
QUIZ result: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="
QUIZ: no share button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" 
CAP en-light/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP en-light/st-02: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP en-light/st-03: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия
CAP en-light/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
no event row 1: 
NP en-light: FATAL(any process)=0 of the app=0 ANR=0; app process 14708; logcat app errors: 0
## NA. Accessibility over all new screens
A11Y AGG: 76 screens analysed, 0 dumps failed; totals MISSING=16 DOUBLE=0 SAME-LABEL=4 SMALL(<48dp)=10
== MISSING: 11 distinct
   View@[121,2124][340,2274]  [1 screens: 11-event]
   View@[408,2124][597,2274]  [1 screens: 11-event]
   View@[665,2124][959,2274]  [1 screens: 11-event]
   View@[42,0][777,36]  [1 screens: 01c-home-3]
   View@[809,0][1080,36]  [1 screens: 01c-home-3]
   View@[0,1997][1080,2064]  [1 screens: 01d-home-4]
   View@[42,1764][524,1938]  [1 screens: 02-more]
   View@[556,1764][1038,1938]  [1 screens: 02-more]
   View@[42,2247][1038,2274]  [1 screens: np-01b-list-2]
   View@[0,2254][1080,2274]  [1 screens: st-02]
   View@[0,2264][1080,2274]  [1 screens: st-03]
== SMALL: 4 distinct
   'Изчисти платното'  [3 screens: em-01-open, em-02-stitched, em-03-mirror]
   'Дейности Групи, клубове, проек'  [1 screens: qz-01-answered]
   'За читалището История и мисия'  [1 screens: qz-01-answered]
   ''  [1 screens: np-01b-list-2]
== SAME-LABEL: 1 distinct
   'Назад' x2  [3 screens: qz-01-q1, qz-06-result, qz-07-again]
== DOUBLE: 0 distinct
## NP-D. Diagnostics › Бързодействие
PERF CARD new36: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 4 | Първи кадър: медиана 1884 мс, последен 1861 мс | Пълно показване: медиана 2600 мс, последно 2503 мс | Няма записани грешки. | Диагностика
PERF CARD new36 (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 4 | Първи кадър: медиана 1884 мс, последен 1861 мс | Пълно показване: медиана 2600 мс, последно 2503 мс | Няма записани грешки. | Диагностика
contact sheets: 11 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 4" text="Първи кадър: медиана 1884 мс, последен 1861 мс" text="Пълно показване: медиана 2600 мс, последно 2503 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5772', '6822', '11662', '14708']): 21 in 8 groups
       5  E/eglCodecCommon: glUtilsParamSize: unknow param #
       4  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
       4  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       4  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
       1  W/g.chyavorec.ap: Verification of void om#.onBlockedStatusChanged(android.net.Network, boolean) took #.#ms
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=f#e#e#c#-#-#d#d-a#d-#b#f#e#c#da#, generation=#)
       1  W/g.chyavorec.ap: Long monitor contention with owner OkHttp https://chyavorec.org/... (#) at void ra#.<init>(p#, java.lang.Strin
       1  W/g.chyavorec.ap: Long monitor contention with owner DefaultDispatcher-worker-# (#) at boolean android.os.BinderProxy.transactNa
contact sheets: 12 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5772', '6822', '11662', '14708']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 16
   skipped-frames events=16 max=114 total=764
== Davey: 31
   davey events=31 max=2025ms median=856ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## new36

```
part new36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15700 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4184 ms
## NP-bg-light (full). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP bg-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта · Искам да прочета
CAP bg-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НОЕ · Организацията на ООН за обра · БИБЛИОТЕКА · НОЕ · 155 г. от рождението на Пана · ЧИТАЛИЩЕ
CAP bg-light/01d-home-4: targets=16 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP bg-light/01e-home-5: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Далечният морето · Дългият пътя · Дъхът на планините · НОВИНИ · Още новини · Всички
CAP bg-light/02-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-light/02b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=25 ellipsized=0 | Галерия · Снимки от сайта · Извези шевица · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник
CAP bg-light/02c-more-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
CAP bg-light/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 16
CAP bg-light/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 16 → 6
EMB redo: 6 → 16
EMB four-way mirror, 2 taps: 16 → 24 (+8 expected)
CAP bg-light/em-03-mirror: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-04-lower: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-05-template: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB save: text="Моите шевици" 
CAP bg-light/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP bg-light/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
CAP bg-light/np-01b-list-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Баба Дамяна разказва (продъл · Възстановяването на селото с · СТАТИЯ 03 · СПОМЕНИ · Читалището — покровител и за · Спомени за читалището в запа · СТАТИЯ 04 · ИСТОРИЯ
NEWSPAPER first open: page label after 2771 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.4
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
NEWSPAPER double tap: diff vs page 24.69
NEWSPAPER pinch: diff vs page 25.40
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2676 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP bg-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=225 std=29.4
CAP bg-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-light/tl-02: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP bg-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP bg-light/tl-04: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | 1927. Въздържателно дружеств · 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново · Дружество „Воля“
CAP bg-light/tl-05: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | 1947. Решение за собствена с · Започва отчуждаване на терен · 17 март 1958. Първа копка. С · Първа копка · Самооблагане 3% по Закона за · 1961. Открита читалищната сг
CAP bg-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
CAP bg-light/tl-07: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1998 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Първа копка" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Решение за
CAP bg-light/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP bg-light/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP bg-light/qz-02-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Въздържателно дружество „Бор · Театрален кръжец и зараждане · Решение за собствена сграда
CAP bg-light/qz-03-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 1 верни от 3 отговорени, общ · Въпрос 3 от 5 · Коя от тези книги е написана · Черната кутия: Ниско прелита · Sanktus · Площад Диамант
CAP bg-light/qz-04-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 1 верни от 4 отговорени, общ · Въпрос 4 от 5 · Коя от тези книги е написана · Конспирацията · Крадците на мрака · Търсачите
CAP bg-light/qz-05-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 1 верни от 5 отговорени, общ · Въпрос 5 от 5 · Какво се случва в читалището · Театрален кръжец и зараждане · Дружество „Воля“ · Първият радиоапарат в селото
CAP bg-light/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
CAP bg-light/qz-07-again: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ again the same day: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
CAP bg-light/st-01: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-light/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP bg-light/st-03: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP bg-light/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-light/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP bg-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP bg-light: FATAL(any process)=0 of the app=0 ANR=0; app process 6903; logcat app errors: 0
## NP-bg-dark (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP bg-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-dark/02-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-dark/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 51 → after 6 taps + a stroke 57
CAP bg-dark/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 57 → 50
EMB redo: 50 → 57
EMB save: text="Моите шевици" 
CAP bg-dark/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP bg-dark/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2523 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-dark/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=30 std=25.1
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2512 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP bg-dark/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=30 std=25.1
CAP bg-dark/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-dark/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP bg-dark/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 2021 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP bg-dark/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP bg-dark/qz-01-answered: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
QUIZ q1: no next button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text
CAP bg-dark/qz-06-result: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
QUIZ result: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="
QUIZ: no share button: text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="Документи и архив" text="Устав, отчети, юбилейният вестник" 
CAP bg-dark/st-01: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-dark/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP bg-dark/st-03: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP bg-dark/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-dark/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP bg-dark: FATAL(any process)=0 of the app=0 ANR=0; app process 11995; logcat app errors: 0
## NP-en-light (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [en-US], rotation 0)
CAP en-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=1 [Search books, news, events…] | “Vasil Levski – 1922” · Yavorets village · Messages · Search · Search books, news, events… · Scan barcode
CAP en-light/02-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | THE CENTRE · Messages · Announcements and invitation · Events · Community centre calendar · Activities
CAP en-light/em-01-open: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | Embroidery canvas, 15 by 15  · Thread colour · Burgundy · Gold · Black · Cream
EMB stitches: start 55 → after 6 taps + a stroke 48
CAP en-light/em-02-stitched: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | Embroidery canvas, 15 by 15  · Thread colour · Burgundy · Gold · Black · Cream
EMB undo: 48 → 55
EMB redo: 55 → 48
EMB save: text="My patterns" 
CAP en-light/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Embroidery canvas, 15 by 15  · Thread colour · Burgundy · Gold · Black · Cream
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP en-light/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | The old newspaper · Jubilee newspaper · 1959 · Twenty articles from the jub · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2630 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Page 1 of 1. Pinch or double-tap to zoom." content-desc="Back" text="Баба Дамяна разказва" content-desc="Open in another app" content-desc="Share" 
CAP en-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Page 1 · Back · Баба Дамяна разказва · Open in another app · Share
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.4
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Page 1 of 1. Pinch or double-tap to zoom." content-desc="Back" text="Баба Дамяна разказва" content-desc="Open in another app" content-desc="Share" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2704 ms: content-desc="Баба Дамяна разказва. Page 1 of 1. Pinch or double-tap to zoom." content-desc="Back" text="Баба Дамяна разказва" content-desc="Open in another app" content-desc="Share" text="No internet connection" 
CAP en-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Page 1 · Back · Баба Дамяна разказва · Open in another app · Share · No internet connection
NEWSPAPER offline page content: mean=225 std=29.4
CAP en-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Timeline · From 1900 to today · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP en-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP en-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ: no question in 45 s: text="Tomorrow will be better — Yavorets has more to tell." text="1/5" text="1 correct out of 5" text="Streak: 1 day in a row" text="New questions tomorrow." text="Share the result" text="Back" content-desc="Back" 
CAP en-light/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Tomorrow will be better — Ya · 1/5 · 1 correct out of 5 · Streak: 1 day in a row · New questions tomorrow. · Share the result
CAP en-light/qz-01-answered: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | THE CENTRE · Messages · Announcements and invitation · Events · Community centre calendar · Activities
QUIZ q1: no next button: text="The old newspaper" text="Articles from the jubilee newspaper" text="Timeline" text="The history of the centre since 1900" text="Know Yavorets" text="Five questions every day" text="Documents &amp; archive" text="Statute, reports, jubilee newspaper" text=
CAP en-light/qz-06-result: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | The old newspaper · Articles from the jubilee ne · Timeline · The history of the centre si · Know Yavorets · Five questions every day
QUIZ result: text="The old newspaper" text="Articles from the jubilee newspaper" text="Timeline" text="The history of the centre since 1900" text="Know Yavorets" text="Five questions every day" text="Documents &amp; archive" text="Statute, reports, jubilee newspaper" text="Contacts" text="Address, phone, locatio
QUIZ: no share button: text="The old newspaper" text="Articles from the jubilee newspaper" text="Timeline" text="The history of the centre since 1900" text="Know Yavorets" text="Five questions every day" text="Documents &amp; archive" text="Statute, reports, jubi
CAP en-light/st-01: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Appearance · Theme · System default · Text size · Standard · Palette
CAP en-light/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Black (OLED) · Pure black background in the · Colours from wallpaper · Material You colours from yo · Language · English
CAP en-light/st-03: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Widgets · Add the “Chitalishte Yavoret · Due dates, the next event an · Add “Book of the day” · A different catalogue book o · Notifications
CAP en-light/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Checked on launch and twice  · Check for updates · Version 1.2.36 · Data · Covers from the internet · Covers by ISBN from covers.o
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP en-light/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Monday, November 16, 2026, i · 80 г. от създаването на ЮНЕС · An event from the centre’s a · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Add to calendar
NP en-light: FATAL(any process)=0 of the app=0 ANR=0; app process 15241; logcat app errors: 0
## NA. Accessibility over all new screens
A11Y AGG: 77 screens analysed, 0 dumps failed; totals MISSING=4 DOUBLE=0 SAME-LABEL=4 SMALL(<48dp)=2
== MISSING: 4 distinct
   View@[42,0][378,33]  [1 screens: 01d-home-4]
   View@[415,0][751,33]  [1 screens: 01d-home-4]
   View@[788,0][1080,33]  [1 screens: 01d-home-4]
   View@[0,296][1080,314]  [1 screens: st-03]
== SMALL: 2 distinct
   'Галерия Снимки от сайта'  [1 screens: 02b-more-2]
   'Извези шевица Бод по бод, като'  [1 screens: 02b-more-2]
== SAME-LABEL: 2 distinct
   'Назад' x2  [3 screens: qz-01-q1, qz-06-result, qz-07-again]
   'Back' x2  [1 screens: qz-01-q1]
== DOUBLE: 0 distinct
## NP-D. Diagnostics › Бързодействие
PERF CARD new36: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 4 | Първи кадър: медиана 2584 мс, последен 2735 мс | Пълно показване: медиана 2892 мс, последно 3350 мс | Насечени кадри по екрани | embroidery: 5,9 % от 170 кадъра · най-бавен 1027 мс | общо с видеокартата: средно 308 мс, най-бавен 1464 мс | more: 3,3 % от 272 кадъра · най-бавен 600 мс | общо с видеокартата: средно 315 мс, най-бавен 970 мс | news: 1,7 % от 59 кадъра · най-бавен 285 мс | общо с видеокартата: средно 296 мс, най-бавен 547 мс | home: 1,1 % от 445 кадъра · най-бавен 1703 мс | общо с видеокартата: средно 293 мс, най-бавен 2074 мс | timeline: 0,0 % от 413 кадъра · най-бавен 375 мс | общо с видеокартата: средно 230 мс, най-бавен 583 мс | Диагностика
PERF CARD new36 (scrolled): (card not on screen) Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 4 | Първи кадър: медиана 2584 мс, последен 2735 мс | Пълно показване: медиана 2892 мс, последно 3350 мс | Насечени кадри по екрани | embroidery: 5,9 % от 170 кадъра · най-бавен 1027 мс | общо с видеокартата: средно 308 мс, най-бавен 1464 мс | more: 3,3 % от 272 кадъра · най-бавен 600 мс | общо с видеокартата: средно 315 мс, най-бавен 970 мс | news: 1,7 % от 59 кадъра · най-бавен 285 мс
contact sheets: 11 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 4 | Първи кадър: медиана 2584 мс, последен 2735 мс | Пълно показване: медиана 2892 мс, последно 3350 мс | Насечени кадри по екрани | embroidery: 5,9 % от 170 кадъра · най-бавен 1027 мс | общо с видеокарт
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 4" text="Първи кадър: медиана 2584 мс, последен 2735 мс" text="Пълно показване: медиана 2892 мс, последно 3350 мс" text="Насечени кадри по екрани" text="embroidery: 5,9 % от 170 кадъра · най-бавен 1027 мс" text="общо с видеокартата: средно 308 мс, най-бавен 1464 мс" text="more: 3,3 % от 272 кадъра · най-бавен 600 мс" text="общо с видеокартата: средно 315 мс, най-бавен 970 мс" text="news: 1,7 % от 59 кадъра · най-бавен 285 мс" text="общо с видеокартата: средно 296 мс, най-бавен 547 мс" text="ho
APP W/E lines (pids ['5458', '6903', '11995', '15241']): 43 in 10 groups
      10  W/Parcel: Expecting binder but got null!
       6  E/OpenGLRenderer: Unable to match the desired swap behavior.
       4  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       4  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       4  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       4  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       4  W/Gralloc4: allocator #.x is not supported
       4  E/FrameEvents: updateAcquireFence: Did not find frame.
       2  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#bcb#c-#c-#e#-bdd#-#a#aa#f#, generation=#)
contact sheets: 12 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5458', '6903', '11995', '15241']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 22
   skipped-frames events=22 max=93 total=1072
== Davey: 31
   davey events=31 max=2394ms median=1115ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## new36b

```
part new36b; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 6169 ms
TEXT SIZE now: text="Много голям" 
## NP-font130-xlarge (brief). New screens (1080x2400 px, density 420, font_scale 1.3, night no, app locale [bg-BG], rotation 0)
CAP font130-xlarge/01-home: targets=12 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP font130-xlarge/02-more: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP font130-xlarge/em-01-open: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 17
CAP font130-xlarge/em-02-stitched: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 17 → 6
EMB redo: 6 → 17
EMB save: text="Моите шевици" 
CAP font130-xlarge/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Цвят на конеца · Бордо · Злато · Черно · Кремаво · Зелено
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP font130-xlarge/np-01-list: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 4316 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP font130-xlarge/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.4
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2596 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP font130-xlarge/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=225 std=29.4
CAP font130-xlarge/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP font130-xlarge/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP font130-xlarge/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств · Въздържателно дружество „Бор · Основано от активни читалищн
QUIZ ready after 2001 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Първа копка" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Решение за
CAP font130-xlarge/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP font130-xlarge/qz-01-answered: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP font130-xlarge/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
CAP font130-xlarge/st-01: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Много голям · Палитра
CAP font130-xlarge/st-02: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Яворец · Злато и жито · Левски · Зелено и сребро · Шевица · Червено, черно и слонова кос
CAP font130-xlarge/st-03: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP font130-xlarge/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP font130-xlarge/11-event: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Назад
NP font130-xlarge: FATAL(any process)=0 of the app=0 ANR=0; app process 7253; logcat app errors: 0
## NP-font130-xlarge-en (brief). New screens (1080x2400 px, density 420, font_scale 1.3, night no, app locale [en-US], rotation 0)
CAP font130-xlarge-en/01-home: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=1 [Search books, news, events…] | “Vasil Levski – 1922” · Yavorets village · Messages · Search · Search books, news, events… · Scan barcode
CAP font130-xlarge-en/02-more: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | THE CENTRE · Messages · Announcements and invitation · Events · Community centre calendar · Activities
CAP font130-xlarge-en/em-01-open: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Embroidery canvas, 15 by 15  · Thread colour · Burgundy · Gold · Black · Cream
EMB stitches: start 14 → after 6 taps + a stroke 2
CAP font130-xlarge-en/em-02-stitched: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Embroidery canvas, 15 by 15  · Thread colour · Burgundy · Gold · Black · Cream
EMB undo: 2 → 12
EMB redo: 12 → 2
EMB save: text="My patterns" 
CAP font130-xlarge-en/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Embroidery canvas, 15 by 15  · Thread colour · Burgundy · Gold · Black · Cream
EMB share: chooser NOT open after 12 s; fg org.chyavorec.app/org.chyavorec.app.MainActivity
EMB end: front app
CAP font130-xlarge-en/np-01-list: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | The old newspaper · Jubilee newspaper · 1959 · Twenty articles from the jub · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2598 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Page 1 of 1. Pinch or double-tap to zoom." content-desc="Back" text="Баба Дамяна разказва" content-desc="Open in another app" content-desc="Share" 
CAP font130-xlarge-en/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Page 1 · Back · Баба Дамяна разказва · Open in another app · Share
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.4
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Page 1 of 1. Pinch or double-tap to zoom." content-desc="Back" text="Баба Дамяна разказва" content-desc="Open in another app" content-desc="Share" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2586 ms: content-desc="Баба Дамяна разказва. Page 1 of 1. Pinch or double-tap to zoom." content-desc="Back" text="Баба Дамяна разказва" content-desc="Open in another app" content-desc="Share" 
CAP font130-xlarge-en/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Page 1 · Back · Баба Дамяна разказва · Open in another app · Share
NEWSPAPER offline page content: mean=225 std=29.4
CAP font130-xlarge-en/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Timeline · From 1900 to today · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP font130-xlarge-en/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP font130-xlarge-en/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1923–1924. Устав и държавно  · Уставът е приет на 22 януари · 1927. Въздържателно дружеств · Въздържателно дружество „Бор · Основано от активни читалищн · 1934. Първият радиоапарат в 
QUIZ: no question in 45 s: text="Tomorrow will be better — Yavorets has more to tell." text="1/5" text="1 correct out of 5" text="Streak: 1 day in a row" text="New questions tomorrow." text="Share the result" text="Back" content-desc="Back" 
CAP font130-xlarge-en/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Tomorrow will be better — Ya · 1/5 · 1 correct out of 5 · Streak: 1 day in a row · New questions tomorrow. · Share the result
CAP font130-xlarge-en/qz-01-answered: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Activities · Groups, clubs, projects · About us · History and mission · Gallery · Photos from the website
QUIZ q1: no next button: text="The history of the centre since 1900" text="Know Yavorets" text="Five questions every day" text="Documents &amp; archive" text="Statute, reports, jubilee newspaper" text="Contacts" text="Address, phone, location" text="THE APP" text="Settings" text="Priv
CAP font130-xlarge-en/qz-06-result: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | The history of the centre si · Know Yavorets · Five questions every day · Documents & archive · Statute, reports, jubilee ne · Contacts
QUIZ result: text="The history of the centre since 1900" text="Know Yavorets" text="Five questions every day" text="Documents &amp; archive" text="Statute, reports, jubilee newspaper" text="Contacts" text="Address, phone, location" text="THE APP" text="Settings" text="Privacy policy" text="Add to home screen" te
QUIZ: no share button: text="The history of the centre since 1900" text="Know Yavorets" text="Five questions every day" text="Documents &amp; archive" text="Statute, reports, jubilee newspaper" text="Contacts" text="Address, phone, location" text="THE APP" text="
CAP font130-xlarge-en/st-01: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Appearance · Theme · System default · Text size · Extra large · Palette
CAP font130-xlarge-en/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Gold and wheat · Green and silver · Red, black and ivory · Black (OLED) · Pure black background in the · Colours from wallpaper
CAP font130-xlarge-en/st-03: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Language · English · Seasonal decorations · Holidays, seasons and tradit · Shake for “Surprise me” · Shake the phone in the catal
CAP font130-xlarge-en/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Shake the phone in the catal · Widgets · Add the “Chitalishte Yavoret · Due dates, the next event an · Add “Book of the day” · A different catalogue book o
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP font130-xlarge-en/11-event: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Monday, November 16, 2026, i · 80 г. от създаването на ЮНЕС · An event from the centre’s a · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Back
NP font130-xlarge-en: FATAL(any process)=0 of the app=0 ANR=0; app process 11009; logcat app errors: 0
TEXT SIZE now: text="Стандартен" 
## NP-landscape-light (brief). New screens (2400x1080 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 1)
CAP landscape-light/01-home: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP landscape-light/02-more: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP landscape-light/em-01-open: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 15
CAP landscape-light/em-02-stitched: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 15 → 6
EMB redo: 6 → 15
EMB save: text="Шевицата е запазена" 
CAP landscape-light/em-06-after-save: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP landscape-light/np-01-list: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 3001 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP landscape-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=234 std=20.3
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2824 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP landscape-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=235 std=15.9
CAP landscape-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP landscape-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 1909. Организирано читалищно · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване · Възстановяване и официално у
CAP landscape-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново · Дружество „Воля“ · Ново въздържателно дружество
QUIZ ready after 1967 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
CAP landscape-light/qz-01-q1: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Назад
QUIZ q1: no answer buttons: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
CAP landscape-light/qz-06-result: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Назад
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
QUIZ: no share button: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
CAP landscape-light/st-01: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP landscape-light/st-02: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=14 ellipsized=0 | Палитра · Яворец · Яворец · Злато и жито · Левски · Зелено и сребро
CAP landscape-light/st-03: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=10 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP landscape-light/st-04: targets=6 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=10 ellipsized=0 | Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP landscape-light/11-event: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Назад
NP landscape-light: FATAL(any process)=0 of the app=0 ANR=0; app process 15312; logcat app errors: 0
## NP-landscape-dark (brief). New screens (2400x1080 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 1)
CAP landscape-dark/01-home: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP landscape-dark/02-more: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP landscape-dark/em-01-open: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 19 → after 6 taps + a stroke 4
CAP landscape-dark/em-02-stitched: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 4 → 13
EMB redo: 13 → 4
EMB save: 
CAP landscape-dark/em-06-after-save: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP landscape-dark/np-01-list: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2763 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP landscape-dark/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=28 std=17.5
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2334 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP landscape-dark/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=28 std=17.5
CAP landscape-dark/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP landscape-dark/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 1909. Организирано читалищно · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване · Възстановяване и официално у
CAP landscape-dark/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1934. Първият радиоапарат в  · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново · Дружество „Воля“ · Ново въздържателно дружество · 1947. Решение за собствена с
QUIZ ready after 1963 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
CAP landscape-dark/qz-01-q1: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Назад
QUIZ q1: no answer buttons: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
CAP landscape-dark/qz-06-result: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Назад
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
QUIZ: no share button: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." content-desc="Назад" text="Познай Яворец" 
CAP landscape-dark/st-01: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP landscape-dark/st-02: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=14 ellipsized=0 | Палитра · Яворец · Яворец · Злато и жито · Левски · Зелено и сребро
CAP landscape-dark/st-03: targets=6 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP landscape-dark/st-04: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP landscape-dark/11-event: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Назад
NP landscape-dark: FATAL(any process)=0 of the app=0 ANR=0; app process 18871; logcat app errors: 0
## NP-w320dp (brief). New screens (1080x2400 px, density 540, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP w320dp/01-home: targets=12 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=18 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP w320dp/02-more: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP w320dp/em-01-open: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 8 → after 6 taps + a stroke 25
CAP w320dp/em-02-stitched: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 25 → 14
EMB redo: 14 → 25
EMB save: text="Моите шевици" text="Шевицата е запазена" 
CAP w320dp/em-06-after-save: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=5 | front: app | texts=27 ellipsized=0 | Бордо · Злато · Черно · Кремаво · Зелено · Бод
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP w320dp/np-01-list: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2589 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP w320dp/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.2
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2291 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP w320dp/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=225 std=29.2
CAP w320dp/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP w320dp/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP w320dp/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1927. Въздържателно дружеств · Основано от активни читалищн · 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново
QUIZ ready after 1960 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP w320dp/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP w320dp/qz-01-answered: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=17 ellipsized=0 | Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец
QUIZ q1: no next button: text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="ПРИЛОЖЕНИЕТО" text="Настройки" text="Политика за поверителност" text="Добави на началния екран" text="За приложението" text="Още" content-desc="Търсене" text="
CAP w320dp/qz-06-result: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=15 ellipsized=0 | Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен · ПРИЛОЖЕНИЕТО · Настройки · Политика за поверителност
QUIZ result: text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="ПРИЛОЖЕНИЕТО" text="Настройки" text="Политика за поверителност" text="Добави на началния екран" text="За приложението" text="Още" content-desc="Търсене" text="Начало" text="Каталог" 
QUIZ: no share button: text="Устав, отчети, юбилейният вестник" text="Контакти" text="Адрес, телефон, местоположение" text="ПРИЛОЖЕНИЕТО" text="Настройки" text="Политика за поверителност" text="Добави на началния екран" text="За приложението" text="Още" content-d
CAP w320dp/st-01: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP w320dp/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Злато и жито · Зелено и сребро · Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета
CAP w320dp/st-03: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP w320dp/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP w320dp/11-event: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Назад · Сподели
NP w320dp: FATAL(any process)=0 of the app=0 ANR=0; app process 22105; logcat app errors: 0
## NP-tablet-1280x800dp (brief). New screens (2560x1600 px, density 320, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP tablet-1280x800dp/01-home: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP tablet-1280x800dp/02-more: targets=20 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=34 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-1280x800dp/em-01-open: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 25 → after 6 taps + a stroke 18
CAP tablet-1280x800dp/em-02-stitched: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 18 → 25
EMB redo: 25 → 18
EMB save: text="Моите шевици" 
CAP tablet-1280x800dp/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP tablet-1280x800dp/np-01-list: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2694 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP tablet-1280x800dp/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=229 std=32.1
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2453 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP tablet-1280x800dp/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=229 std=32.1
CAP tablet-1280x800dp/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP tablet-1280x800dp/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | 1927. Въздържателно дружеств · 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново · Дружество „Воля“
CAP tablet-1280x800dp/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | 1936. Дружество „Воля“. Ново · 1947. Решение за собствена с · Решение за собствена сграда · Започва отчуждаване на терен · 17 март 1958. Първа копка. С · Първа копка
QUIZ ready after 1958 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP tablet-1280x800dp/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP tablet-1280x800dp/qz-01-answered: targets=20 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=34 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
QUIZ q1: no next button: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" 
CAP tablet-1280x800dp/qz-06-result: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=34 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Съобщения
QUIZ result: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" 
QUIZ: no share button: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" 
CAP tablet-1280x800dp/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP tablet-1280x800dp/st-02: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP tablet-1280x800dp/st-03: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP tablet-1280x800dp/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP tablet-1280x800dp/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · Добави в календара · Напомни ми · Маршрут · Виж публикацията · 80 г. от създаването на ЮНЕС
NP tablet-1280x800dp: FATAL(any process)=0 of the app=0 ANR=0; app process 25628; logcat app errors: 0
## NP-tablet-landscape (brief). New screens (1600x2560 px, density 320, font_scale 1.0, night no, app locale [bg-BG], rotation 1)
CAP tablet-landscape/01-home: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=43 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP tablet-landscape/02-more: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-landscape/em-01-open: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 18 → after 6 taps + a stroke 25
CAP tablet-landscape/em-02-stitched: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 25 → 18
EMB redo: 18 → 25
EMB save: text="Моите шевици" 
CAP tablet-landscape/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP tablet-landscape/np-01-list: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2439 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP tablet-landscape/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=228 std=34.8
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2581 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP tablet-landscape/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=229 std=35.1
CAP tablet-landscape/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP tablet-landscape/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | 1923–1924. Устав и държавно  · 1927. Въздържателно дружеств · Въздържателно дружество „Бор · Основано от активни читалищн · 1934. Първият радиоапарат в  · Първият радиоапарат в селото
CAP tablet-landscape/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | 1923–1924. Устав и държавно  · 1927. Въздържателно дружеств · Въздържателно дружество „Бор · Основано от активни читалищн · 1934. Първият радиоапарат в  · Първият радиоапарат в селото
QUIZ ready after 1997 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" 
CAP tablet-landscape/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
CAP tablet-landscape/qz-01-answered: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
QUIZ q1: no next button: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" 
CAP tablet-landscape/qz-06-result: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
QUIZ result: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" 
QUIZ: no share button: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" 
CAP tablet-landscape/st-01: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP tablet-landscape/st-02: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=31 ellipsized=0 | Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво
CAP tablet-landscape/st-03: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
CAP tablet-landscape/st-04: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Известия · Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два 
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP tablet-landscape/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Добави в календара · Напомни ми · Маршрут · Виж публикацията · Панайот Пипков · Ден на семе
NP tablet-landscape: FATAL(any process)=0 of the app=0 ANR=0; app process 28799; logcat app errors: 0
## NA. Accessibility over all new screens
A11Y AGG: 131 screens analysed, 0 dumps failed; totals MISSING=17 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=18
== MISSING: 16 distinct
   View@[160,1455][2560,1551]  [2 screens: 02-more, qz-01-answered]
   View@[0,296][1080,312]  [1 screens: st-03]
   View@[42,1974][540,2127]  [1 screens: 01-home]
   View@[540,1974][1038,2127]  [1 screens: 01-home]
   View@[128,231][2018,233]  [1 screens: st-02]
   View@[128,1009][2018,1080]  [1 screens: st-03]
   View@[128,231][2018,239]  [1 screens: st-02]
   View@[170,231][754,238]  [1 screens: st-03]
   View@[780,231][1365,238]  [1 screens: st-03]
   View@[1391,231][1976,238]  [1 screens: st-03]
   View@[128,994][2018,1080]  [1 screens: st-04]
   View@[0,1454][1440,1550]  [1 screens: st-02]
   View@[54,1958][540,2049]  [1 screens: 01-home]
   View@[540,1958][1026,2049]  [1 screens: 01-home]
   View@[54,344][519,438]  [1 screens: qz-01-answered]
   View@[54,344][519,479]  [1 screens: qz-06-result]
== SMALL: 10 distinct
   ''  [5 screens: 01-home, qz-01-answered, qz-06-result, st-02, st-03]
   'Цветовете на телефона, взети о'  [1 screens: st-04]
   'Сезонни украси Празници, сезон'  [1 screens: st-02]
   'Бордо'  [1 screens: em-06-after-save]
   'Злато'  [1 screens: em-06-after-save]
   'Черно'  [1 screens: em-06-after-save]
   'Кремаво'  [1 screens: em-06-after-save]
   'Зелено'  [1 screens: em-06-after-save]
   'Бод по бод, като на кърпа'  [1 screens: qz-01-answered]
   'Устав, отчети, юбилейният вест'  [1 screens: qz-06-result]
== SAME-LABEL: 2 distinct
   'Назад' x2  [2 screens: qz-01-q1, qz-06-result]
   'Back' x2  [1 screens: qz-01-q1]
== DOUBLE: 0 distinct
contact sheets: 16 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 8 | Първи кадър: медиана 2734 мс, последен 1143 мс | Пълно показване: медиана 2975 мс, последно 1836 мс | Насечени кадри по екрани | embroidery: 9,1 % от 197 кадъра · най-бавен 692 мс | общо с видеокарта
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 8" text="Първи кадър: медиана 2734 мс, последен 1143 мс" text="Пълно показване: медиана 2975 мс, последно 1836 мс" text="Насечени кадри по екрани" text="embroidery: 9,1 % от 197 кадъра · най-бавен 692 мс" text="общо с видеокартата: средно 249 мс, най-бавен 733 мс" text="more: 5,2 % от 305 кадъра · най-бавен 585 мс" text="общо с видеокартата: средно 290 мс, най-бавен 787 мс" text="newspaper/read: 1,9 % от 52 кадъра · най-бавен 310 мс" text="общо с видеокартата: средно 335 мс, най-бавен 715 мс" 
APP W/E lines (pids ['5375', '7253', '11009', '15312', '18871', '22105', '25628', '28799']): 95 in 12 groups
      22  E/OpenGLRenderer: Unable to match the desired swap behavior.
      21  W/Parcel: Expecting binder but got null!
       8  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       8  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       8  W/Gralloc4: allocator #.x is not supported
       7  E/FrameEvents: updateAcquireFence: Did not find frame.
       2  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=ab#da-c#-#e-#b#-#a#a#bd#e, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner main (#) at void ed.Y()(r#-map-id-ccf#fe#c#ee#d#b#f#db#cfd#ad#fa#c#f#ae#b#f
       1  W/g.chyavorec.app: No such thread id for suspend: #
contact sheets: 17 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5375', '7253', '11009', '15312', '18871', '22105', '25628', '28799']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 44
   skipped-frames events=44 max=112 total=2184
== Davey: 78
   davey events=78 max=2532ms median=933ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## onb36

```
part onb36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## O1. Fresh install of 1.2.36: BrandIntro (recorded) and onboarding pages
installed new: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
REC brandintro: 36 frames @8 fps; motion all: frames=36 moving-frames(>1.0)=2 max-step=103.7 max-vs-first=161.8 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.1 0.0 0.0 0.0 0.2 0.0 0.0 0.0 0.0 0.0 0.0 103.7 62.5 0.0 0.0 0.0 0.2 0.0 0.0 0.0 0.0 0.0 0.0 0.0]
CAP onb-default/p1-welcome: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Пропусни · Читалище Яворец · НЧ „Васил Левски – 1922“ · Официалното приложение на чи · Новини и събития · Концерти, празници и срещи —
CAP onb-default/p1b-welcome-scrolled: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Пропусни · Читалище Яворец · НЧ „Васил Левски – 1922“ · Официалното приложение на чи · Новини и събития · Концерти, празници и срещи —
CAP onb-default/p2-settings: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Пропусни · Удобно за теб · Размерът на текста и извести · Размер на текста · Малък · Стандартен
CAP onb-default/p2b-xlarge: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Пропусни · Удобно за теб · Размерът на текста и извести · Размер на текста · Малък · Стандартен
CAP onb-default/p3-signin: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Пропусни · Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001
CAP onb-default/p3b-sample-card-tilt: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Пропусни · Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001
CAP onb-default/p4-home-first: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
ONB default: FATAL/ANR FATAL(any process)=0 of the app=0 ANR=0; main screen after: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *С
catalogue (first install) after 3025 ms
PERF CARD fresh: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 1 | Първи кадър: медиана 2926 мс, последен 2926 мс | Пълно показване: медиана 2926 мс, последно 2926 мс | Няма записани грешки. | Диагностика
PERF CARD fresh (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 1 | Първи кадър: медиана 2926 мс, последен 2926 мс | Пълно показване: медиана 2926 мс, последно 2926 мс | Няма записани грешки. | Диагностика
## O2. Onboarding again (data cleared): font 1.3 + English; then landscape; then a skip on page 1
CAP onb-font130-en/p1-welcome: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Skip · Chitalishte Yavorets · Community Centre “Vasil Levs · The centre’s official app —  · News and events · Concerts, celebrations and m
CAP onb-font130-en/p1b-welcome-scrolled: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Skip · Chitalishte Yavorets · Community Centre “Vasil Levs · The centre’s official app —  · News and events · Concerts, celebrations and m
CAP onb-font130-en/p2-settings: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Skip · Make it yours · Text size and notifications  · Text size · Small · Standard
CAP onb-font130-en/p2b-scrolled: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Skip · Make it yours · Text size and notifications  · Text size · Small · Standard
CAP onb-font130-en/p3-signin: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Skip · Sample library card · LIBRARY CARD · “Vasil Levski – 1922” · Maria Ivanova · Reader no. 19220001
CAP onb-font130-en/p3b-scrolled: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Skip · Sample library card · LIBRARY CARD · “Vasil Levski – 1922” · Maria Ivanova · Reader no. 19220001
CAP onb-landscape/p1-welcome: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Пропусни · Читалище Яворец · НЧ „Васил Левски – 1922“ · Официалното приложение на чи · Новини и събития · Концерти, празници и срещи —
CAP onb-landscape/p1b-welcome-scrolled: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Пропусни · Читалище Яворец · НЧ „Васил Левски – 1922“ · Официалното приложение на чи · Новини и събития · Концерти, празници и срещи —
CAP onb-landscape/p2-settings: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Пропусни · Удобно за теб · Размерът на текста и извести · Размер на текста · Малък · Стандартен
CAP onb-landscape/p3-signin: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Пропусни · Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001
after 'Пропусни' on page 1: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *С
ONB part: FATAL/ANR FATAL(any process)=0 of the app=0 ANR=0
A11Y AGG: 17 screens analysed, 0 dumps failed; totals MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
== MISSING: 0 distinct
== SMALL: 0 distinct
== SAME-LABEL: 0 distinct
== DOUBLE: 0 distinct
contact sheets: 6 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 1 | Първи кадър: медиана 1833 мс, последен 1833 мс | Пълно показване: медиана 1833 мс, последно 1833 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 1" text="Първи кадър: медиана 1833 мс, последен 1833 мс" text="Пълно показване: медиана 1833 мс, последно 1833 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['6134', '8660', '9123', '9458']): 30 in 9 groups
       4  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       4  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       4  W/Parcel: Expecting binder but got null!
       4  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       4  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       4  W/Gralloc4: allocator #.x is not supported
       4  E/OpenGLRenderer: Unable to match the desired swap behavior.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#bff#c#e-c#-#cf#-#d-#dbc#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=b#b#bc-#bdf-#fe#-#c-#a#, generation=#)
contact sheets: 7 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['6134', '8660', '9123', '9458']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 19
   skipped-frames events=19 max=130 total=1017
== Davey: 44
   davey events=44 max=3894ms median=1096ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## start36a

```
part start36a; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## S0. Emulator warm-up block (not counted): both apps, 2 installs × 5 cold starts
installed warm-base: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
installed warm-new: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
block order (each a fresh install): new base base new new base base new
## S new-1: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-1: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 catalogue after 2770 ms
new-1 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-1: n=5 min=1698ms median=1831ms max=2352ms
## S base-2: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-2: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 catalogue after 2560 ms
base-2 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-2: n=5 min=1646ms median=1878ms max=2208ms
## S base-3: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-3: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-3 catalogue after 2830 ms
base-3 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-3: n=5 min=1562ms median=1812ms max=1891ms
## S new-4: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-4: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-4 catalogue after 2759 ms
new-4 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-4: n=5 min=1562ms median=2106ms max=2298ms
## S new-5: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-5: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-5 catalogue after 2682 ms
new-5 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-5: n=5 min=1608ms median=1837ms max=1947ms
## S base-6: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-6: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-6 catalogue after 2603 ms
base-6 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-6: n=5 min=1783ms median=1975ms max=2197ms
## S base-7: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-7: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-7 catalogue after 2764 ms
base-7 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-7: n=5 min=1580ms median=1852ms max=2178ms
## S new-8: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-8: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-8 catalogue after 2647 ms
new-8 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-8: n=5 min=1837ms median=1955ms max=2087ms
## S totals (n=20 per app)
START cold new           n=20 min=1562ms median=1891ms max=2352ms  all=[2352, 1727, 1698, 2028, 1831, 2298, 2179, 2106, 1871, 1562, 1782, 1608, 1947, 1837, 1879, 2087, 1903, 1837, 1955, 2014]  states=['COLD']
DRAWN new: TotalTime n=20 min=1562ms median=1891ms max=2352ms; fully drawn n=20 median=2126ms min=1777 max=2487 all=[2487, 1828, 1842, 2228, 2142, 2449, 2394, 2292, 1973, 1777, 1993, 1810, 2149, 2205, 2026, 2454, 2095, 2077, 2225, 2111]; without line: 0
START cold base          n=20 min=1562ms median=1865ms max=2208ms  all=[1878, 1879, 1646, 2208, 1748, 1562, 1812, 1815, 1891, 1759, 1984, 2197, 1783, 1975, 1945, 1580, 2178, 1852, 1962, 1780]  states=['COLD']
DRAWN base: TotalTime n=20 min=1562ms median=1865ms max=2208ms; fully drawn n=20 median=2651ms min=2069 max=3201 all=[2663, 3009, 2198, 3201, 2767, 2341, 2786, 2146, 2823, 2124, 3015, 2487, 2834, 2936, 2290, 2639, 3125, 2069, 2448, 2257]; without line: 0
AB TotalTime  : 1.2.35 n=20 median 1891 ms (mean 1925) | 1.2.34 n=20 median 1865 ms (mean 1872) | diff of medians +26 ms (+1.4%) | Mann-Whitney p=0.42
AB Fully drawn: 1.2.35 n=20 median 2126 ms (mean 2128) | 1.2.34 n=20 median 2651 ms (mean 2608) | diff of medians -524 ms (-19.8%) | Mann-Whitney p=0.00
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 8 | Първи кадър: медиана 1838 мс, последен 1796 мс | Пълно показване: медиана 2102 мс, последно 1931 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 8" text="Първи кадър: медиана 1838 мс, последен 1796 мс" text="Пълно показване: медиана 2102 мс, последно 1931 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5803', '6800', '7186', '7420', '7564', '7825', '8062', '8499', '8559', '8626', '8685', '8744', '8928', '9269', '9334', '9396', '9456', '9516', '9575', '9637', '9815', '10281', '10344', '10404', '10468', '10530', '10590', '10652', '10835', '11144', '11205', '11264', '11326', '11387', '11446', '11507', '11691', '12058', '12120', '12181', '12240', '12300', '12363', '12424', '12715', '13023', '13082', '13141', '13204', '13263', '13324', '13385', '13563', '13871', '13932', '13994', '14056', '14116', '14176', '14237', '14420', '14834', '14895', '14970', '15029', '15090', '15150', '15211', '15392', '15685', '15745', '15804', '15863', '15924', '15983', '16041']): 547 in 20 groups
      76  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      76  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      76  W/Parcel: Expecting binder but got null!
      76  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      76  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      76  W/Gralloc4: allocator #.x is not supported
      76  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object d#.s(java.lang.Object)(r
       2  W/g.chyavorec.app: Suspending all threads took: #.#ms
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#a#-#b#-#d#-#fa-ed#f#b#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#e#d-bf#-#a#f-#d#-#d#ad#be#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=a#b#d#f-a#ee-#d#-#d#-f#cb#cfb#dd#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ce#-d#c#-#a-ab#c-a#fe#cbd, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=ee#d#c-#f-#-aacf-#ce#d#ef#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#fbfa-ff#-#-be#-#e#bd#d#c, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#f#fb#-#a#-#d#-abc#-e#a#b#, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object xs#.getValue()(r#-map-id
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=bcbfaaa#-#-#b-#d#-#eb#ef#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#a#a#-#-#df#-ba#-b#b#ad#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#d-#af#-#f-#f-c#af#d#, generation=#)
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5803', '6800', '7186', '7420', '7564', '7825', '8062', '8499', '8559', '8626', '8685', '8744', '8928', '9269', '9334', '9396', '9456', '9516', '9575', '9637', '9815', '10281', '10344', '10404', '10468', '10530', '10590', '10652', '10835', '11144', '11205', '11264', '11326', '11387', '11446', '11507', '11691', '12058', '12120', '12181', '12240', '12300', '12363', '12424', '12715', '13023', '13082', '13141', '13204', '13263', '13324', '13385', '13563', '13871', '13932', '13994', '14056', '14116', '14176', '14237', '14420', '14834', '14895', '14970', '15029', '15090', '15150', '15211', '15392', '15685', '15745', '15804', '15863', '15924', '15983', '16041']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 178
   skipped-frames events=178 max=93 total=7736
== Davey: 303
   davey events=303 max=2640ms median=982ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## start36b

```
part start36b; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## S0. Emulator warm-up block (not counted): both apps, 2 installs × 5 cold starts
installed warm-base: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
installed warm-new: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
block order (each a fresh install): base new new base base new new base
## S base-1: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-1: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 catalogue after 2882 ms
base-1 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-1: n=5 min=2448ms median=2555ms max=3473ms
## S new-2: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-2: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 catalogue after 3073 ms
new-2 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-2: n=5 min=2267ms median=2613ms max=2707ms
## S new-3: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-3: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-3 catalogue after 3090 ms
new-3 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-3: n=5 min=2452ms median=2577ms max=2961ms
## S base-4: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-4: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-4 catalogue after 3076 ms
base-4 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-4: n=5 min=2284ms median=2589ms max=2756ms
## S base-5: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-5: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-5 catalogue after 2656 ms
base-5 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-5: n=5 min=2365ms median=2624ms max=2749ms
## S new-6: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-6: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-6 catalogue after 2887 ms
new-6 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-6: n=5 min=2422ms median=2650ms max=3113ms
## S new-7: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-7: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-7 catalogue after 2838 ms
new-7 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-7: n=5 min=2349ms median=2834ms max=2950ms
## S base-8: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-8: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-8 catalogue after 3257 ms
base-8 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-8: n=5 min=2187ms median=2454ms max=2644ms
## S totals (n=20 per app)
START cold new           n=20 min=2267ms median=2632ms max=3113ms  all=[2613, 2658, 2469, 2267, 2707, 2961, 2564, 2452, 2886, 2577, 2896, 2650, 3113, 2422, 2569, 2349, 2428, 2834, 2950, 2921]  states=['COLD']
DRAWN new: TotalTime n=20 min=2267ms median=2632ms max=3113ms; fully drawn n=20 median=2884ms min=2578 max=3855 all=[3274, 2885, 2736, 2615, 2882, 3281, 2762, 2751, 3216, 2760, 3416, 2867, 3360, 2654, 2824, 2578, 2915, 3212, 3534, 3855]; without line: 0
START cold base          n=20 min=2187ms median=2577ms max=3473ms  all=[2555, 2553, 3473, 3060, 2448, 2509, 2589, 2284, 2718, 2756, 2624, 2565, 2365, 2749, 2717, 2454, 2644, 2440, 2187, 2616]  states=['COLD']
DRAWN base: TotalTime n=20 min=2187ms median=2577ms max=3473ms; fully drawn n=20 median=3625ms min=2446 max=4470 all=[3856, 3773, 4470, 4119, 3978, 3224, 3981, 3341, 3134, 3685, 3503, 3294, 3871, 3250, 3687, 3737, 3565, 2868, 2446, 3543]; without line: 0
AB TotalTime  : 1.2.35 n=20 median 2632 ms (mean 2664) | 1.2.34 n=20 median 2577 ms (mean 2615) | diff of medians +54 ms (+2.1%) | Mann-Whitney p=0.43
AB Fully drawn: 1.2.35 n=20 median 2884 ms (mean 3019) | 1.2.34 n=20 median 3625 ms (mean 3566) | diff of medians -742 ms (-20.5%) | Mann-Whitney p=0.00
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 8 | Първи кадър: медиана 2245 мс, последен 2313 мс | Пълно показване: медиана 2686 мс, последно 3311 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 8" text="Първи кадър: медиана 2245 мс, последен 2313 мс" text="Пълно показване: медиана 2686 мс, последно 3311 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5658', '6725', '6972', '7261', '7417', '7826', '8058', '8703', '8768', '8830', '8890', '8951', '9172', '9489', '9558', '9620', '9681', '9820', '9965', '10036', '10219', '10555', '10617', '10677', '10741', '10802', '10881', '10939', '11124', '11439', '11497', '11558', '11620', '11679', '11776', '11860', '12141', '12453', '12514', '12576', '12639', '12702', '12761', '12821', '13069', '13372', '13433', '13494', '13555', '13616', '13677', '13738', '13929', '14309', '14376', '14456', '14517', '14574', '14639', '14697', '14877', '15191', '15250', '15311', '15370', '15429', '15546', '15674', '15882', '16204', '16267', '16327', '16389', '16449', '16510', '16577']): 539 in 14 groups
      76  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      76  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      76  W/Parcel: Expecting binder but got null!
      76  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      76  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      76  W/Gralloc4: allocator #.x is not supported
      76  E/OpenGLRenderer: Unable to match the desired swap behavior.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ca#-#f#-#fc#-#cfd-caa#c#aeaa#a, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ea#c#-#-#f#-#-a#ba#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#df#-#cc#-#a#b-b#a-de#f#ada, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#c#adf#-#fd#-#b#-#ab-#a#ca#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=af#a#f#-#d-#ba-a#b-ee#f#c, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#f#fa#-#d#e-#-#aa#-e#d#b#a#, generation=#)
       1  W/g.chyavorec.app: Suspending all threads took: #.#ms
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5658', '6725', '6972', '7261', '7417', '7826', '8058', '8703', '8768', '8830', '8890', '8951', '9172', '9489', '9558', '9620', '9681', '9820', '9965', '10036', '10219', '10555', '10617', '10677', '10741', '10802', '10881', '10939', '11124', '11439', '11497', '11558', '11620', '11679', '11776', '11860', '12141', '12453', '12514', '12576', '12639', '12702', '12761', '12821', '13069', '13372', '13433', '13494', '13555', '13616', '13677', '13738', '13929', '14309', '14376', '14456', '14517', '14574', '14639', '14697', '14877', '15191', '15250', '15311', '15370', '15429', '15546', '15674', '15882', '16204', '16267', '16327', '16389', '16449', '16510', '16577']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 254
   skipped-frames events=254 max=109 total=12877
== Davey: 379
   davey events=379 max=2795ms median=1193ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## upd28

```
part upd28; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## U0. Install 1.2.35 (1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 150) as a fresh install
installed base: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## U1. State on 1.2.35: books in „Искам да прочета“, a card by hand, text size „Голям“, dark theme, the widget, a few screens
CARD28 try 1 (field 540 691, save 540 1104): text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARD28: card shown
WARN: settings item =Text size|=Размер на текста not found
WIDGET b135 long-press menu: text="Настройки за началния екран" text="Приспособления" text="Тапети" 
WIDGET b135 picker: our row @ 540 854; size label @ 346 974; texts: 2 × 2|3 × 2|Читалище Яворец|Читалище Яворец|Читалище Яворец|4 × 2|3 × 3|4 × 1|1 × 1|3 × 3|
WIDGET b135 after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Часовник" content-desc="Часовник" text="Аналогов часовник" text="2 × 2" content-desc="Ширина 2 и височина 2" text="Цифров часовник" 
WIDGET b135: draganddrop bound nothing (0) — trying motionevent
WIDGET b135-retry long-press menu: text="Настройки за началния екран" text="Приспособления" text="Тапети" 
WIDGET b135-retry picker: our row @ 540 974; size label @ 346 1094; texts: 2 × 2|3 × 2|Читалище Яворец|Читалище Яворец|Читалище Яворец|4 × 2|3 × 3|4 × 1|1 × 1|3 × 3|
WIDGET b135: widgets of ours bound: 0 -> 0 (ids ); options: 
locale before the update: 
STATE before settings: text="Тъмна" text="Стандартен" 
STATE before night mode of the app window: ; app locale 
STATE before card: shown (TEST-12345)
STATE before want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="Последният мечтите" text="Соня Петрова" text="налична" content-desc="Още действия" text="Дългият любовта" text="П
DB[before] files: 4096 chitalishte.db;32768 chitalishte.db-shm;61832 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=2 identity_hash=de965857dbc324c1f39366be6acfbef1; tables: event_reminders=0 favorite_news=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[before]: 58 files: cache/data/user/0/org.chyavorec.app/no_backup/androidx.work.workdb.lck cache/http/1acbd2607480a51d9f81644f293bca3d.0 cache/http/1acbd2607480a51d9f81644f293bca3d.1 cache/http/2f0f1461ff8b70ed7e88ad35ebc33dc5.0 cache/http/2f0f1461ff8b70ed7e88ad35ebc33dc5.1 cache/http/5af185b2674a5c7ddb433af5348f407c.0 cache/http/5af185b2674a5c7ddb433af5348f407c.1 cache/http/6a2a3b66eebe8dbb29620040c35b3b4a.0 cache/http/6a2a3b66eebe8dbb29620040c35b3b4a.1 cache/http/81fd6a9abde05dabe2e215e9f8fb2818.0 cache/http/81fd
prefs/datastore files before: /data/data/org.chyavorec.app/files: datastore profileInstalled profileinstaller_profileWrittenFor_lastUpdateTime.dat  /data/data/org.chyavorec.app/shared_prefs: app_language.xml app_shortcuts.xml cover_misses.xml stats_anim.xml 
## U2. adb install -r of 1.2.36 over 1.2.35 (same signing key)
adb install -r 1.2.36: Success; now:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
WIDGET after the update (app not opened): views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
widget ids before/after: [] / []
logcat since the install (app not opened): FATAL(any process)=0 of the app=0 ANR=0
## U3. First launch of 1.2.36 on the old data
first launch after the update: Status: ok TotalTime: 1723 WaitTime: 1797 
first screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Последни новини" text="БИБЛ
WHATS-NEW 31-whats-new: not shown; screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="13 ноември 2026 · *С
logcat since the first launch: FATAL(any process)=0 of the app=0 ANR=0; process 9299
Room/migration lines: 11-12 22:27:32.840  4206  4206 I CarrierServices: [2] aws.a: Migration binding to RcsMigrationService|11-12 22:27:32.869  4206  4206 I CarrierServices: [2] aws.a: Migration binding to RcsMigrationService|11-12 22:27:32.900  3302  6481 W RcsProvisioning: 	at com.google.android.apps.messaging.rcsmigration.RcsStateProvider.buildRcsState(SourceFile:81)|11-12 22:27:32.900  3302  6481 W RcsProvisioning: 	at com.google.android.apps.messaging.rcsmigration.RcsStateProvider.getRcsState(SourceFile:6)|
locale after the update:  (UI shows: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за тър)
STATE after settings: text="Тъмна" text="Стандартен" 
STATE after night mode of the app window: ; app locale 
STATE after card: shown (TEST-12345)
STATE after want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="Последният мечтите" text="Соня Петрова" text="налична" content-desc="Още действия" text="Дългият любовта" text="П
## U4. Database and files after the update
DB[after] files: 45056 chitalishte.db;32768 chitalishte.db-shm;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[after]: 58 files: cache/data/user/0/org.chyavorec.app/no_backup/androidx.work.workdb.lck cache/http/1acbd2607480a51d9f81644f293bca3d.0 cache/http/1acbd2607480a51d9f81644f293bca3d.1 cache/http/2f0f1461ff8b70ed7e88ad35ebc33dc5.0 cache/http/2f0f1461ff8b70ed7e88ad35ebc33dc5.1 cache/http/5af185b2674a5c7ddb433af5348f407c.0 cache/http/5af185b2674a5c7ddb433af5348f407c.1 cache/http/6a2a3b66eebe8dbb29620040c35b3b4a.0 cache/http/6a2a3b66eebe8dbb29620040c35b3b4a.1 cache/http/81fd6a9abde05dabe2e215e9f8fb2818.0 cache/http/81fd
FILES before 58, after 58; removed 0: ; new 0: ; size changed 5: ./cache/http/2f0f1461ff8b70ed7e88ad35ebc33dc5.0 ./cache/http/journal ./cache/image_cache/journal ./databases/chitalishte.db ./no_backup/diagnostics/perf.json
## U5. Widget after opening the app; Diagnostics › Бързодействие after the update; new screens on the migrated data
WIDGET after opening the app: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET click catalog: 'widget_catalog' not on the home screen
WIDGET click scan: 'widget_scan' not on the home screen
WIDGET click event: 'widget_event' not on the home screen
WIDGET click app: 't:Chitalishte Yavorets|Читалище Яворец' not on the home screen
PERF CARD after-update: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Първи старт след обновяването | Първи кадър 1544 мс, пълно показване 2073 мс | Студен старт · 4 | Първи кадър: медиана 1538 мс, последен 1595 мс | Пълно показване: медиана 1984 мс, последно 1982 мс | Топъл старт · 1 | Първи кадър: медиана 709 мс, последен 709 мс | Насечени кадри по екрани | home: 29,8 % от 121 кадъра · най-бавен 649 мс | общо с видеокартата: средно 254 мс, най-бавен 770 мс | Преди това · Версия: неизвестна (по-стари измервания) | Студен старт · 2 | Първи кадър: медиана 1454 мс, последен 1386 мс | Пълно показване: медиана 1942 мс, последно 2361 мс | Диагностика
PERF CARD after-update (scrolled): (card not on screen) Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Първи старт след обновяването | Първи кадър 1544 мс, пълно показване 2073 мс | Студен старт · 4 | Първи кадър: медиана 1538 мс, последен 1595 мс | Пълно показване: медиана 1984 мс, последно 1982 мс | Топъл старт · 1 | Първи кадър: медиана 709 мс, последен 709 мс | Насечени кадри по екрани | home: 29,8 % от 121 кадъра · най-бавен 649 мс
## NP-after-update (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP after-update/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP after-update/02-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP after-update/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 17
CAP after-update/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 17 → 6
EMB redo: 6 → 17
EMB save: text="Моите шевици" 
CAP after-update/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP after-update/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2036 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP after-update/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=30 std=25.1
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 1947 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP after-update/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=30 std=25.0
CAP after-update/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP after-update/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP after-update/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1213 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Първа копка" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Решение за
CAP after-update/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP after-update/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP after-update/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 1 s; fg android/com.android.internal.app.ChooserActivity
CAP after-update/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP after-update/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP after-update/st-03: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията · Заемания, събития, съобщения
CAP after-update/st-04: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP after-update/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
NP after-update: FATAL(any process)=0 of the app=0 ANR=0; app process 10972; logcat app errors: 0
## U6. Logcat of the whole part
FATAL/ANR since the install: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 8 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Първи старт след обновяването | Първи кадър 1544 мс, пълно показване 2073 мс | Студен старт · 5 | Първи кадър: медиана 1595 мс, последен 1782 мс | Пълно показване: медиана 1987 мс, последно 2290 мс | Топъл старт · 1 | 
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Първи старт след обновяването" text="Първи кадър 1544 мс, пълно показване 2073 мс" text="Студен старт · 5" text="Първи кадър: медиана 1595 мс, последен 1782 мс" text="Пълно показване: медиана 1987 мс, последно 2290 мс" text="Топъл старт · 1" text="Първи кадър: медиана 709 мс, последен 709 мс" text="Насечени кадри по екрани" text="home: 29,8 % от 121 кадъра · най-бавен 649 мс" text="общо с видеокартата: средно 254 мс, най-бавен 770 мс" text="Преди това · Версия: неизвестна (по-стари измервания)" text="Студен 
APP W/E lines (pids ['6428', '8450', '9299', '10122', '10398', '10448', '10506', '10972']): 36 in 8 groups
       8  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  E/eglCodecCommon: glUtilsParamSize: unknow param #
       8  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=fc#be#-#a#e-#aca-#daf-#adf#d#, generation=#)
       1  W/g.chyavorec.ap: Long monitor contention with owner DefaultDispatcher-worker-# (#) at void java.lang.Object.wait()(Object.java:
       1  W/g.chyavorec.ap: Verification of void org.chyavorec.app.MainActivity.l(android.content.Intent) took #.#ms
       1  W/g.chyavorec.ap: Long monitor contention with owner DefaultDispatcher-worker-# (#) at boolean android.os.BinderProxy.transactNa
contact sheets: 9 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['6428', '8450', '9299', '10122', '10398', '10448', '10506', '10972']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 11
   skipped-frames events=11 max=50 total=410
== Davey: 13
   davey events=13 max=1373ms median=864ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

## upd36

```
part upd36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## U0. Install 1.2.35 (1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 150) as a fresh install
installed base: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## U1. State on 1.2.35: books in „Искам да прочета“, a card by hand, text size „Голям“, dark theme, the widget, a few screens
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
TEXT SIZE now: text="Голям" 
WIDGET b135 long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET b135 picker: our row @ 437 893; size label @ 540 1801; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET b135 after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET b135: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET on 1.2.35: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; error text: 
locale before the update: Locales for org.chyavorec.app for user 0 are [en-US]
STATE before settings: 
STATE before night mode of the app window: ; app locale [en-US]
STATE before card: shown (TEST-12345)
STATE before want-to-read: book listed; text="Availability as of Nov 9, 2026" text="Swipe a book aside to remove it." text="Последният мечтите" text="Соня Петрова" text="available" content-desc="More actions" text="Дългият любовта" text="Пе
DB[before] files: 4096 chitalishte.db;32768 chitalishte.db-shm;61832 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=2 identity_hash=de965857dbc324c1f39366be6acfbef1; tables: event_reminders=0 favorite_news=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[before]: 27 files: databases/catalog-index.db databases/chitalishte.db files/datastore/settings.preferences_pb files/profileInstalled files/profileinstaller_profileWrittenFor_lastUpdateTime.dat no_backup/androidx.work.workdb no_backup/androidx.work.workdb-shm no_backup/androidx.work.workdb-wal no_backup/cache/public/1ebfb91784270a823e84c3bd83429c0268dd92904a2dd8e1a4dfd63aad2c1839.json no_backup/cache/public/288782f6475e076286d714aeb0dda24f43f1298026ee7a45902c0407942f5c8d.json no_backup/cache/public/3111e84832a5450
prefs/datastore files before: /data/data/org.chyavorec.app/files: datastore profileInstalled profileinstaller_profileWrittenFor_lastUpdateTime.dat  /data/data/org.chyavorec.app/shared_prefs: app_shortcuts.xml cover_misses.xml stats_anim.xml widget.xml 
## U2. adb install -r of 1.2.36 over 1.2.35 (same signing key)
adb install -r 1.2.36: Success; now:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
WIDGET after the update (app not opened): views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|80 г. от създаването на ЮНЕСКО|November 16, 2026|Панайот Пипков · Ден на семейството|November 21, 2026|; error text: 
widget ids before/after: [id=2 ] / [id=2 ]
logcat since the install (app not opened): FATAL(any process)=0 of the app=0 ANR=0
## U3. First launch of 1.2.36 on the old data
first launch after the update: Status: ok LaunchState: WARM TotalTime: 2646 WaitTime: 2652 
first screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode" text="November 13, 2026 · *Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" content-desc="Latest news" text="БИБЛИОТЕКА" text="
WHATS-NEW 31-whats-new: not shown; screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode" text="November 13, 2026 · *Св. Йоан Зл
logcat since the first launch: FATAL(any process)=0 of the app=0 ANR=0; process 9921
Room/migration lines: 
locale after the update: Locales for org.chyavorec.app for user 0 are [en-US] (UI shows: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" )
STATE after settings: text="Тъмна" text="Голям" 
STATE after night mode of the app window: ; app locale [bg-BG]
STATE after card: shown (TEST-12345)
STATE after want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="Последният мечтите" text="Соня Петрова" text="налична" content-desc="Още действия" text="Дългият любовта" text="П
## U4. Database and files after the update
DB[after] files: 45056 chitalishte.db;32768 chitalishte.db-shm;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[after]: 27 files: databases/catalog-index.db databases/chitalishte.db files/datastore/settings.preferences_pb files/profileInstalled files/profileinstaller_profileWrittenFor_lastUpdateTime.dat no_backup/androidx.work.workdb no_backup/androidx.work.workdb-shm no_backup/androidx.work.workdb-wal no_backup/cache/public/1ebfb91784270a823e84c3bd83429c0268dd92904a2dd8e1a4dfd63aad2c1839.json no_backup/cache/public/288782f6475e076286d714aeb0dda24f43f1298026ee7a45902c0407942f5c8d.json no_backup/cache/public/3111e84832a5450
FILES before 27, after 27; removed 0: ; new 0: ; size changed 3: ./databases/chitalishte.db ./no_backup/androidx.work.workdb-wal ./no_backup/diagnostics/perf.json
## U5. Widget after opening the app; Diagnostics › Бързодействие after the update; new screens on the migrated data
WIDGET after opening the app: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; error text: 
WIDGET click catalog (widget_catalog @ 900 600) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 694 600) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак сл
WIDGET click event (widget_event @ 540 786) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 349 599) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
PERF CARD after-update: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 4 | Първи кадър: медиана 1904 мс, последен 1984 мс | Пълно показване: медиана 2126 мс, последно 2219 мс | Топъл старт · 5 | Първи кадър: медиана 160 мс, последен 519 мс | Пълно показване: медиана 2616 мс, последно 2616 мс | Насечени кадри по екрани | home: 0,3 % от 388 кадъра · най-бавен 1723 мс | общо с видеокартата: средно 282 мс, най-бавен 2478 мс | Диагностика
PERF CARD after-update (scrolled): (card not on screen) Студен старт · 4 | Първи кадър: медиана 1904 мс, последен 1984 мс | Пълно показване: медиана 2126 мс, последно 2219 мс | Топъл старт · 5 | Първи кадър: медиана 160 мс, последен 519 мс | Пълно показване: медиана 2616 мс, последно 2616 мс | Насечени кадри по екрани | home: 0,3 % от 388 кадъра · най-бавен 1723 мс | общо с видеокартата: средно 282 мс, най-бавен 2478 мс | scan: 0,0 % от 32 кадъра · най-бавен 373 мс | общо с видеокартата: средно 228 мс, най-бавен 909 мс | Преди това · Версия: неизвестна (по-стари измервания)
## NP-after-update (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP after-update/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP after-update/02-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP after-update/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 16
CAP after-update/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 16 → 6
EMB redo: 6 → 16
EMB save: text="Моите шевици" 
CAP after-update/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP after-update/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2325 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP after-update/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=30 std=25.1
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2610 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP after-update/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=30 std=25.1
CAP after-update/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP after-update/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 1912–1918. Прекъсване от вой · 17 дек. 1922. Възстановяване · Възстановяване и официално у · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание
CAP after-update/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | 1947. Решение за собствена с · Решение за собствена сграда · Започва отчуждаване на терен · 17 март 1958. Първа копка. С · Първа копка · Самооблагане 3% по Закона за
QUIZ ready after 1948 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Първа копка" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Решение за
CAP after-update/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP after-update/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Първа копка · Въздържателно дружество „Бор · Общностен и информационен це
CAP after-update/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Утре ще е по-добре — Яворец  · 1/5 · 1 верни от 5 · Серия: 1 ден поред · Нови въпроси — утре. · Сподели резултата
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="1/5" text="1 верни от 5" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Назад" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
CAP after-update/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Голям · Палитра
CAP after-update/st-02: targets=11 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=17 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP after-update/st-03: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“
CAP after-update/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Избери известията · Заемания, събития, съобщения · Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP after-update/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP after-update: FATAL(any process)=0 of the app=0 ANR=0; app process 12180; logcat app errors: 0
## U6. Logcat of the whole part
FATAL/ANR since the install: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 9 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 5 | Първи кадър: медиана 1824 мс, последен 1323 мс | Пълно показване: медиана 2034 мс, последно 1403 мс | Топъл старт · 5 | Първи кадър: медиана 160 мс, последен 519 мс | Пълно показване: медиана 2616 мс
DIAG SCREEN (scrolled): text="Студен старт · 5" text="Първи кадър: медиана 1824 мс, последен 1323 мс" text="Пълно показване: медиана 2034 мс, последно 1403 мс" text="Топъл старт · 5" text="Първи кадър: медиана 160 мс, последен 519 мс" text="Пълно показване: медиана 2616 мс, последно 2616 мс" text="Насечени кадри по екрани" text="home: 0,3 % от 388 кадъра · най-бавен 1723 мс" text="общо с видеокартата: средно 282 мс, най-бавен 2478 мс" text="scan: 0,0 % от 32 кадъра · най-бавен 373 мс" text="общо с видеокартата: средно 228 мс, най-бавен 909 мс" text="Преди това · Версия: неизвестна (по-стари измервания)" text="Студен старт · 1" text="Първи кадър: медиана 1507 мс, последен 1507 мс" text="Пълно показване: медиана 1507
APP W/E lines (pids ['6000', '8777', '9921', '10994', '11471', '11529', '11687', '12180']): 79 in 12 groups
      16  E/OpenGLRenderer: Unable to match the desired swap behavior.
      14  W/Parcel: Expecting binder but got null!
       8  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       8  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       8  W/Gralloc4: allocator #.x is not supported
       3  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       2  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       2  E/FrameEvents: updateAcquireFence: Did not find frame.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#c#cf-#a#-#ba#-#-bf#e#e, generation=#)
       1  W/LibraryVersion: Failed to get app version for libraryName: play-services-code-scanner
contact sheets: 10 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['6000', '8777', '9921', '10994', '11471', '11529', '11687', '12180']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 27
   skipped-frames events=27 max=88 total=1282
== Davey: 44
   davey events=44 max=2550ms median=1105ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:27 app-mapping.txt; crashes: 0 B
```

## widget36

```
part widget36; APK: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 80efffa built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583798 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3507 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## W1. Launcher icon (all apps) and the widget list
LAUNCHER drawer: label @ not found; text="Calendar" content-desc="Calendar" text="Camera" content-desc="Camera" text="Chrome" content-desc="Chrome" text="Clock" content-desc="Clock" text="Contacts" content-desc="Contacts" text="Drive" content-desc="Drive" 
WIDGET n36 long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET n36 picker: our row @ 437 927; size label @ 540 1835; texts: Chitalishte Yavorets|3 × 2|Chitalishte Yavorets|Chitalishte Yavorets|
## W2. Widgets added from Settings (the pin buttons)
WIDGET button 'Книга на деня' present; now tapping
PIN book dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Book of the day" text="Book of the day" text="Under the Yoke" text="Ivan Vazov" text="Book of the day" content-desc="Book of the day widget" text="3 × 2" content-desc="3 wide by 2 high" tex
PIN book: no confirm button
HOME after the Book-of-the-day pin: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
widgets of ours bound: 
WIDGET button 'Читалище Яворец' tapped
PIN main dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Chitalishte Yavorets" text="Chitalishte Yavorets" text="Scan" text="Catalogue" text="2 books to return" text="in 3 days" text="Oct 15" text="Next event" text="Literary evening" text="Octobe
PIN main: no confirm button
HOME after both pins: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
widgets of ours bound: 
WIDGET dark: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
## W3. Widget click-through; the Book-of-the-day widget after the catalogue is loaded
WIDGET after the catalogue loaded: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET click catalog: 'widget_catalog' not on the home screen
WIDGET click scan: 'widget_scan' not on the home screen
WIDGET click event: 'widget_event' not on the home screen
WIDGET click app: 't:Chitalishte Yavorets|Читалище Яворец' not on the home screen
## W4. Pinned shortcut „Шевица“ from Още › Добави на началния екран
PIN sheet: content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="Добави на началния екран" text="Пряк път на началния екран на телефона, който отваря избраното с един допир. Етикетът не показва лични данни." text="Моите книги" text="Събития" text="Новини" text="Читате
PIN shortcut dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" text="Шевица" content-desc="Шевица widget" text="1 × 1" content-desc="1 wide by 1 high" text="Cancel" text="Add to home screen" 
PIN shortcut: no confirm button
shortcut icon on home: not found
WIDGET36: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 3 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 1 | Първи кадър: медиана 1542 мс, последен 1542 мс | Пълно показване: медиана 1542 мс, последно 1542 мс | Топъл старт · 5 | Първи кадър: медиана 359 мс, последен 404 мс | Насечени кадри по екрани | more:
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 1" text="Първи кадър: медиана 1542 мс, последен 1542 мс" text="Пълно показване: медиана 1542 мс, последно 1542 мс" text="Топъл старт · 5" text="Първи кадър: медиана 359 мс, последен 404 мс" text="Насечени кадри по екрани" text="more: 7,5 % от 80 кадъра · най-бавен 421 мс" text="общо с видеокартата: средно 222 мс, най-бавен 561 мс" text="onboarding: 4,3 % от 47 кадъра · най-бавен 682 мс" text="общо с видеокартата: средно 441 мс, най-бавен 1426 мс" text="settings: 3,0 % от 134 кадъра · най-бавен
APP W/E lines (pids ['5984']): 17 in 9 groups
       7  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/Parcel: Expecting binder but got null!
       2  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       1  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       1  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       1  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       1  W/Gralloc4: allocator #.x is not supported
       1  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5984']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=100 total=209
== Davey: 11
   davey events=11 max=2124ms median=992ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91350883 Nov 12 22:16 app-mapping.txt; crashes: 0 B
```

