# Emulator re-check of the released v1.2.36 (6432ef1) against v1.2.35 (aef8910)

debug/a7-check (runs in runs.txt, latest 43461840130 @ 32a5f45). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU) unless the part says otherwise (parts ending in 28: API 28; in 26: API 26; c36: API 36). Built prodRelease (minified) x86_64 with a throwaway key; diary36: devDebug. In the A/B parts new = 1.2.36 and base = 1.2.35. Screenshots are JPEG, scaled down (sheets/ = contact sheets). Earlier: [../v136/index.md](../v136/index.md) (the candidate 80efffa), [../v135full/index.md](../v135full/index.md).

| part | status |
|---|---|
| c26 | COMPLETE; part=c26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| ng36 | COMPLETE; part=ng36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tr26 | COMPLETE; part=tr26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| tr28 | COMPLETE; part=tr28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| tr36 | COMPLETE; part=tr36 target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| wdg36 | COMPLETE; part=wdg36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| yup36 | COMPLETE; part=yup36 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest)
devDebug 1.2.36 6432ef1 (org.chyavorec.app.dev, demo sign-in DEMO-0001)
1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest)
app.apk 4583950 bytes
app-base.apk 4317894 bytes
app-dev.apk 28595555 bytes
```

## c26

```
part c26; APK: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 1974 ms
## C1. Tour of all screens on API 26
## T-api26-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · С хоро и песен във Водица · 26 септември 2026 · Сканирай · Читателска карта
CAP api26-light/01c-home-3: targets=16 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС
CAP api26-light/01d-home-4: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api26-light/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-light/02b-filters: targets=23 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел
CAP api26-light/02c-filters-2: targets=23 MISSING=2 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP api26-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api26-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api26-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api26-light/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api26-light/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api26-light/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api26-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Измамни игри —  · Още една · Към книгата · Затвори
CAP api26-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CAP api26-light/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт · Моите книги
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api26-light/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api26-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api26-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api26-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-light/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api26-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api26-light/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-light/12-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-light/12b-more-2: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден
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
CAP api26-light/18c-settings-3: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията · Заемания, събития, съобщения
CAP api26-light/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope
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
TOUR api26-light: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=14 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=52
app theme → =Тъмна|=Dark: text="Изглед" text="Тема" text="Тъмна" text="Размер на текста" 
## T-api26-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · Празника „38 читалища. Един  · 27 септември 2026 · Моите книги · Читателска карта
CAP api26-dark/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
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
CAP api26-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Родена в лед —  · Още една · Към книгата · Затвори
CAP api26-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP api26-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги
CAP api26-dark/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api26-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api26-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api26-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api26-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api26-dark/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/12-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-dark/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=25 ellipsized=0 | Галерия · Снимки от сайта · Извези шевица · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник
CAP api26-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api26-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api26-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api26-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api26-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api26-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP api26-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP api26-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP api26-dark/18c-settings-3: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Добави „Книга на деня“ · Всеки ден друга книга от кат · Известия · Избери известията · Заемания, събития, съобщения · Обновления
CAP api26-dark/18d-settings-4: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Провери за нова версия · Версия 1.2.36 · Данни · Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi
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
TOUR api26-dark: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=12 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=55
app theme → =Както системата|=System default: text="Изглед" text="Тема" text="Както системата" text="Размер на текста" 
A11Y AGG: 112 screens analysed, 0 dumps failed; totals MISSING=26 DOUBLE=0 SAME-LABEL=10 SMALL(<48dp)=107
== MISSING: 16 distinct
   View@[0,1906][1080,2032]  [1 screens: 02-catalog]
   View@[53,127][529,190]  [1 screens: 02c-filters-2]
   View@[550,127][929,190]  [1 screens: 02c-filters-2]
   View@[121,2124][340,2274]  [1 screens: 11-event]
   View@[408,2124][597,2274]  [1 screens: 11-event]
   View@[665,2124][959,2274]  [1 screens: 11-event]
   View@[42,1764][524,1938]  [1 screens: 12-more]
   View@[556,1764][1038,1938]  [1 screens: 12-more]
   View@[42,2256][524,2274]  [1 screens: 13-gallery]
   View@[556,2256][1038,2274]  [1 screens: 13-gallery]
   View@[0,231][1080,261]  [1 screens: 18c-settings-3]
   View@[0,231][1080,249]  [1 screens: 18d-settings-4]
   View@[291,0][540,8]  [1 screens: 01c-home-3]
   View@[540,0][789,8]  [1 screens: 01c-home-3]
   View@[870,2029][1069,2064]  [1 screens: 01d-home-4]
   View@[0,2259][1080,2274]  [1 screens: 18c-settings-3]
== SMALL: 27 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   ''  [3 screens: 01d-home-4, 02c-filters-2, 13-gallery]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Далечният морето'  [1 screens: 01c-home-3]
   'Дългият пътя'  [1 screens: 01c-home-3]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
## C2. Home ribbon and the card on API 26
IDLE card26-3-18s (15 s, no input): frames +0; cpu=4.2% (ticks 63 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=3.2%, DefaultExecutor=0.9%, Jit thread pool=0.1%; pid 16314
CARD fullscreen on API 26: yes; app in front yes
IDLE cardfull26-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 16314
## C3. Surprise ×2, share a book as an image on API 26
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book26: chooser OPEN after 3 s; cache/share: 2198628 2026-11-13 16:52 chyavorec-book-399560-1794588767359.png;
SHARE book26 file: chyavorec-book-399560-1794588767359.png 2198628 B; PNG (1080, 1920) RGBA
SHARE book26 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794588767359.png mime=image/png bytes=2198628 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C5. Rotation and process death on API 26 (book, catalogue, article)
ROT book: pid 16314→16314 (same); landscape: kept 40% of 25 texts; lost e.g. ['Вид', 'Език', 'Отдел', 'книга']; back to portrait: kept 100% of 25 texts
PROCDEATH book: killed NO (16314); new pid 16314; restored: kept 100% of 25 texts; front app
ROT catalog: pid 16314→16314 (same); landscape: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият дома', 'Белият пътя', 'Елена Русева']; back to portrait: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият пътя', 'Белият дома', 'Мартин Тонев']
PROCDEATH catalog: killed NO (16314); new pid 16314; restored: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият дома', 'Белият пътя', 'Мартин Тонев']; front app
ROT article: pid 16314→16314 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed NO (16314); new pid 16314; restored: kept 100% of 9 texts; front app
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
first-install catalogue results after 1413 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19161
MONKEY exit=0 duration=85 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19161'; airplane=0; camera: 
MEM after-monkey (raw) PSS=81303KB  Java=15392KB  Native=32920KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=12)  RSS=?KB  Views=25 Activities=1
MEM after-monkey (trimmed) PSS=72802KB  Java=8696KB  Native=31316KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=12)  RSS=?KB  Views=15 Activities=1
## C6. Monkey 2000 (seed 4242) on API 26
## W. First launch and warm-up
first-install catalogue results after 1784 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19808
MONKEY exit=0 duration=95 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19808'; airplane=0; camera: 
MEM after-monkey (raw) PSS=82203KB  Java=11120KB  Native=38552KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=24 Activities=1
MEM after-monkey (trimmed) PSS=80862KB  Java=10312KB  Native=38928KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=24 Activities=1
## C6. Monkey 2000 (seed 777) on API 26
## W. First launch and warm-up
first-install catalogue results after 2400 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20317
MONKEY exit=0 duration=84 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20317'; airplane=0; camera: 
MEM after-monkey (raw) PSS=85652KB  Java=16148KB  Native=36464KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=?KB  Views=15 Activities=1
MEM after-monkey (trimmed) PSS=76618KB  Java=7372KB  Native=36428KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=?KB  Views=15 Activities=1
contact sheets: 16 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: Settings not found
APP W/E lines (pids ['5256', '6063', '11352', '16314', '19161', '19808', '20317']): 1171 in 117 groups
     108  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
     100  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      97  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      91  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      87  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      24  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      23  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      20  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      20  W/GoogleApiManager: The service for mu# is not available: ss#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
      18  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      18  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      15  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      14  E/ResourceType: Style contains key with bad entry: #
      14  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      13  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      12  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      12  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      11  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      11  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      10  W/BasicSecureTextField: 	at uk#.d(r#-map-id-b#fff#e#c#add#b#d#ecb#c#f#b#de#b#f#e#d#:#)
      10  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
      10  W/ConnectionStatusConfig: Dynamic lookup for intent failed for action: com.google.android.gms.chimera.container.moduleinstall.ModuleInst
      10  W/GmsClient: unable to connect to service: com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.STAR
      10  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       7  W/zygote64: Unexpected CPU variant for X# using defaults: x#_#
       7  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       7  E/eglCodecCommon: glUtilsParamSize: unknow param #
       6  W/BasicSecureTextField: 	at qw#.b(r#-map-id-b#fff#e#c#add#b#d#ecb#c#f#b#de#b#f#e#d#:#)
       5  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
contact sheets: 17 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5256', '6063', '11352', '16314', '19161', '19808', '20317']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 20
   skipped-frames events=20 max=52 total=761
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91354008 Nov 13 16:25 app-mapping.txt; crashes: 0 B
```

## ng36

```
part ng36; APK: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5282 ms
## NG1. A quick swipe down that starts on a row at the top of a list: does it open the row? 1.2.36, then 1.2.35
SWIPE new Settings, down from 25% to 75% of the height in 200 ms: nothing opened
SWIPE new Settings, down from 25% to 75% of the height in 80 ms: nothing opened
SWIPE new Settings, down from 25% to 75% of the height in 600 ms: nothing opened
SWIPE new Settings, down from 30% to 60% of the height in 120 ms: nothing opened
SWIPE new Settings, down from 22% to 80% of the height in 300 ms: nothing opened
SWIPE new Още, down from 30% to 75% in 200 ms: nothing opened
SWIPE new Още, down from 30% to 75% in 80 ms: nothing opened
SWIPE new Каталог, down from 45% to 85% in 200 ms: nothing opened
SWIPE new Каталог, down from 45% to 85% in 80 ms: nothing opened
installed base: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894;  versionCode=1507364 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
SWIPE base Settings, down from 25% to 75% of the height in 200 ms: nothing opened
SWIPE base Settings, down from 25% to 75% of the height in 80 ms: nothing opened
SWIPE base Settings, down from 25% to 75% of the height in 600 ms: nothing opened
SWIPE base Settings, down from 30% to 60% of the height in 120 ms: nothing opened
SWIPE base Settings, down from 22% to 80% of the height in 300 ms: nothing opened
SWIPE base Още, down from 30% to 75% in 200 ms: nothing opened
SWIPE base Още, down from 30% to 75% in 80 ms: nothing opened
SWIPE base Каталог, down from 45% to 85% in 200 ms: nothing opened
SWIPE base Каталог, down from 45% to 85% in 80 ms: nothing opened
installed new: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950;  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## NG2. Settings › Избери известията: the weekly digest switch (permission granted)
NOTIF screen: text="Известията се подготвят на самото устройство — без рекламни или проследяващи услуги." text="Съобщения" text="Съобщения, изпратени от читалището до потребителите на приложението" text="Новини" text="Нова публикация в сайта" text="Събития" text="Напомняния за събития" text="Срокове за връщане" text="Наближаващ или изтекъл срок на заета книга" text="Отново на рафта" text="Книги от „Искам да прочета“, които отново са на рафта" text="Седмичен преглед" text="Едно тихо известие в неделя вечер: събития, нови книги и новини — само ако има нещо. Изключено по подразбиране." text="Напомняне „Утре“" text="Ден преди събитие на читалището с начален час — едно кратко известие. Изключено по подразбиран
DIGEST row before: 'Седмичен преглед': checked=false (node itself/ancestor, clickable=true); sections row absent
DIGEST after the tap: 'Седмичен преглед': checked=true (node itself/ancestor, clickable=true); fg org.chyavorec.app/org.chyavorec.app.MainActivity; texts: text="Седмичен преглед" text="Едно тихо известие в неделя вечер: събития, нови книги и новини — само ако има нещо. Изключено по подразбиране." text="Ограничават реда „Нови на рафта“ в прегледа. Без избор — всички нови книги." 
below the digest row: text="Нова публикация в сайта" text="Събития" text="Напомняния за събития" text="Срокове за връщане" text="Наближаващ или изтекъл срок на заета книга" text="Отново на рафта" text="Книги от „Искам да прочета“, които отново са на рафта" text="Седмичен преглед" text="Едно тихо известие в неделя вечер: събития, нови книги и новини — само ако има нещо. Изключено по подразбиране." text="Любими раздели" text="Ограничават реда „Нови на рафта“ в прегледа. Без избор — всички нови книги." text="Общ отдел" 
favourite sections: text="Нова публикация в сайта" text="Събития" text="Напомняния за събития" text="Срокове за връщане" text="Наближаващ или изтекъл срок на заета книга" text="Отново на рафта" text="Книги от „Искам да прочета“, които отново са на рафта" text="Седмичен преглед" text="Едно тихо известие в неделя вечер: събития, нови книги и новини — само ако има нещо. Изключено по подразбиране." text="Любими раздели" 
## NG3. The same switch without the notification permission (revoked, flags cleared): is the system prompt shown?
permission now: android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
NOTIF screen without the permission: text="Известията са изключени за приложението — нищо от изброеното долу няма да се показва." text="Отвори настройките" text="Известията се подготвят на самото устройство — без рекламни или проследяващи услуги." text="Съобщения" text="Съобщения, изпратени от читалището до потребителите на приложението" text="Новини" text="Нова публикация в сайта" text="Събития" text="Напомняния за събития" text="Ср
DIGEST row (no permission): 'Седмичен преглед': checked=true (node itself/ancestor, clickable=true)
DIGEST switched off first: 'Седмичен преглед': checked=false (node itself/ancestor, clickable=true)
after the tap: fg com.google.android.permissioncontroller/com.android.permissioncontroller.permission.ui.GrantPermissionsActivity; text="Allow Chitalishte Yavorets to send you notifications?" text="Allow" text="Don’t allow" 
system prompt → Allow; permission android.permission.POST_NOTIFICATIONS: granted=true, flags=[ USER_SET|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]; DIGEST row: 'Седмичен преглед': checked=true (node itself/ancestor, clickable=true)
DIGEST row before the clock jump: 'Седмичен преглед': checked=true (node itself/ancestor, clickable=true)
## NG4. Digest notification: the clock jumps to Sunday 6 Dec 2026 18:30 (also Никулден), the periodic worker runs
device date before: Fri Nov 13 16:41:45 UTC 2026; tz Etc/UTC
device date now: Sun Dec  6 18:30:09 UTC 2026
jobs: 0 4 
jobscheduler run -f 0: Running job [FORCED]
jobscheduler run -f 4: Running job [FORCED]
NOTIF id=700 channel=updates title=String (Налична е нова версия 1.2.36) text=String (Докосни, за да обновиш приложението.) template=String (android.app.Notification$BigTextStyle) picture=NO largeIcon=yes actions=0 []
NOTIF total for org.chyavorec.app: 1
SHADE digest: 'Тази седмица|This week|седмица|Седмичен' not in the shade: text="On" content-desc="Internet,AndroidWifi" text="Internet" text="On" content-desc="Bluetooth." text="Bluetooth" text="Flashlight" text="Off" content-desc="Do Not Disturb." text="Do Not Disturb" text="Chitalishte Yavorets" text="•" text="now" content-desc="Collapse" 
DIGEST: not in the shade
jobscheduler run -f 0: Running job [FORCED]
NOTIF id=700 channel=updates title=String (Налична е нова версия 1.2.36) text=String (Докосни, за да обновиш приложението.) template=String (android.app.Notification$BigTextStyle) picture=NO largeIcon=yes actions=0 []
NOTIF total for org.chyavorec.app: 1
DIGEST logcat: ; FATAL(any process)=0 of the app=0 ANR=0
## NG5. Никулден (6 Dec): the greeting on Home, „Честитка“, the composer, share as an image
HOME on 6 Dec: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Честит Никулден!" text="Ден на рибарите и моряците. Честит имен ден на всички, които празнуват!" text="*Св. Йоан Златоуст, архиеп. Константинополски (Тип. с. 116)" text="Изпрати честитка" content-desc="Скрий поздрава" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" content-desc="Последни новини" text="СЪБИТИЕ" text="С хоро и песен във Водица" 
GREETING composer: content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="Честитка от Яворец" text="Картичката се създава на телефона и излиза от него само когато я споделите." content-desc="Преглед на картичката: Честит Никулден!. Честит имен ден на Никола, Николай, Николина и всички, които празнуват! Спокойни води и богата трапеза." 
GREETING composer (scrolled): content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="Честитка от Яворец" text="Картичката се създава на телефона и излиза от него само когато я споделите." content-desc="Преглед на картичката: Честит Никулден!. Честит имен ден на Никола, Николай, Николина и всички, които празнуват! Спокойни води и богата трапеза." text="Лично за (по желание)" text="Букви, цифри и обичайна пунктуация" text="0/60" text="Сподели картичката" 
GREETING share: chooser OPEN after 5 s; fg android/com.android.internal.app.ChooserActivity; file chyavorec-greeting-nikulden-1796581973092.png
GREETING file: 50 B; PIL.UnidentifiedImageError: cannot identify image file 'out/ng36/share/greeting-chyavorec-greeting-nikulden-1796581973092.png'
SHARE greet received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-greeting-nikulden-1796581973092.png mime=image/png bytes=676272 png=true dims=1080x1920 subject=Честит Никулден! text=Честит Никулден! Честит имен ден на Никола, Николай, Николина и всички, които празнуват! Спокойни води и богата трапеза. https://chyavorec.org
NG part: FATAL(any process)=0 of the app=0 ANR=0; FATAL lines in the whole log: 0
contact sheets: 4 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: 1 entries, 1 distinct
     1  update.verify: signature mismatch
   keys of an entry: at, count, error, tag
DIAG SCREEN: text="Записи: 1"  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 3 | Първи кадър: медиана 3450 мс, последен 3750 мс | Пълно показване: медиана 4220 мс, последно 4449 мс | Топъл старт · 1 | Първи кадър: медиана 387 мс, последен 387 мс | Насечени кадри по екрани | notif
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 3" text="Първи кадър: медиана 3450 мс, последен 3750 мс" text="Пълно показване: медиана 4220 мс, последно 4449 мс" text="Топъл старт · 1" text="Първи кадър: 
APP W/E lines (pids ['4706', '8381', '9350', '10166', '13050']): 52 in 14 groups
       8  W/Parcel: Expecting binder but got null!
       8  E/OpenGLRenderer: Unable to match the desired swap behavior.
       5  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       5  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       5  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       5  W/Gralloc4: allocator #.x is not supported
       4  W/PackageParser: Unknown element under <manifest>: queries at /data/user/#/org.chyavorec.app/no_backup/updates/update-#.apk.par
       2  W/g.chyavorec.app: Suspending all threads took: #.#ms
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=ab#b#b-#d#-#a#d-#-#ca#c#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#-#c-#cc-#c#-#d#cfc#ccf, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=c#c#a-bb#-#d#-#b#-#d#bf, generation=#)
       1  E/FrameEvents: updateAcquireFence: Did not find frame.
       1  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
contact sheets: 5 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4706', '8381', '9350', '10166', '13050']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 46
   skipped-frames events=46 max=127 total=2299
== Davey: 95
   davey events=95 max=3912ms median=1014ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'SIGNAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91354008 Nov 13 16:25 app-mapping.txt; crashes: 174 B
```

## tr26

```
part tr26; APK: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 1820 ms
## TR1. Transitions recorded at 12 fps (animations ON, API 26): 10 frames from the start of the motion, in sheets/tr
TREC a-book-open: frames=37 motion-start=#23 moving-frames=3 (span 5) kept #22..#31; diffs: 0 7 0 12 1 14 0 0 0 0
TR book: book screen open: text="Джиан, Филип" text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" 
TREC b-book-back: frames=29 motion-start=#25 moving-frames=3 (span 3) kept #19..#28; diffs: 0 0 0 0 0 0 6 19 14 0
TREC c-event-open: frames=28 motion-start=#24 moving-frames=4 (span 4) kept #18..#27; diffs: 0 0 0 0 0 0 3 22 12 8
TR event: event screen open: content-desc="Събота, 21 ноември 2026, след 8 дни" text="Панайот Пипков · Ден на семейството" text="Събитие от годишния 
TREC d-event-back: frames=28 motion-start=#20 moving-frames=5 (span 8) kept #18..#27; diffs: 0 0 8 0 0 20 8 0 20 3
TREC e-article-open: frames=28 motion-start=#23 moving-frames=4 (span 5) kept #18..#27; diffs: 0 0 0 0 0 16 0 32 37 4
TR article: article open: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 20
TREC f-article-back: frames=28 motion-start=#21 moving-frames=5 (span 7) kept #18..#27; diffs: 0 0 0 4 0 0 19 24 42 7
CARD28 try 1 (field 540 691, save 540 1104): text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARD28: card shown
TREC g-card-full: frames=36 motion-start=#0 moving-frames=0 (span 0) kept #0..#9; diffs: 0 0 0 0 0 0 0 0 0 0
TR card fullscreen: yes
TREC h-card-back: frames=35 motion-start=#0 moving-frames=0 (span 0) kept #0..#9; diffs: 0 0 0 0 0 0 0 0 0 0
TR part so far: FATAL(any process)=0 of the app=0 ANR=0
## TR2. Quick open/back ×12 on a book and an event (the shared-element crash on API 26 came during navigation)
after 12 quick book open/back: app in front yes; pid 4966; FATAL(any process)=0 of the app=0 ANR=0
after 6 scanner/manual loops: app in front yes; pid 4966; FATAL(any process)=0 of the app=0 ANR=0
## TR4. Monkey 2000 (seed 31337) on API 26
## W. First launch and warm-up
first-install catalogue results after 1286 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=8009
MONKEY exit=0 duration=56 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '8009'; airplane=0; camera: 
MEM after-monkey (raw) PSS=96538KB  Java=21792KB  Native=41696KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=32 Activities=1
MEM after-monkey (trimmed) PSS=81072KB  Java=9264KB  Native=39156KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=16 Activities=1
## TR4. Monkey 2000 (seed 99) on API 26
## W. First launch and warm-up
first-install catalogue results after 1611 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=8573
MONKEY exit=0 duration=68 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '8573'; airplane=0; camera: 
MEM after-monkey (raw) PSS=96145KB  Java=15284KB  Native=47792KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=12)  RSS=?KB  Views=14 Activities=1
MEM after-monkey (trimmed) PSS=86807KB  Java=6728KB  Native=47892KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=12)  RSS=?KB  Views=14 Activities=1
TR part: FATAL(any process)=0 of the app=0 ANR=0; FATAL lines in the whole log: 0
contact sheets: 9 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: tab bar not reached
WARN: Settings not found
APP W/E lines (pids ['4966', '8009', '8573']): 346 in 39 groups
      37  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
      31  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      31  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      31  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      29  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      13  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      12  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      12  W/GoogleApiManager: The service for mu# is not available: ss#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
      12  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      11  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      11  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      10  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      10  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
       9  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
       9  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
       8  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
       8  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
       6  W/ConnectionStatusConfig: Dynamic lookup for intent failed for action: com.google.android.gms.chimera.container.moduleinstall.ModuleInst
       6  W/GmsClient: unable to connect to service: com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.STAR
       6  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
       5  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
       5  W/InputMethodManager: Unable to send input event to IME: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME
       4  W/ViewRootImpl[MainActivity]: Dropping event due to root view being removed: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]
       3  W/zygote64: Unexpected CPU variant for X# using defaults: x#_#
       3  E/ResourceType: Style contains key with bad entry: #
       3  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       3  E/eglCodecCommon: glUtilsParamSize: unknow param #
       3  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       2  W/LibraryVersion: Failed to get app version for libraryName: play-services-code-scanner
contact sheets: 10 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4966', '8009', '8573']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=41 total=137
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91354008 Nov 13 16:25 app-mapping.txt; crashes: 0 B
```

## tr28

```
part tr28; APK: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15982 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 8802 ms
## TR1. Transitions recorded at 12 fps (animations ON, API 28): 10 frames from the start of the motion, in sheets/tr
TREC a-book-open: frames=46 motion-start=#25 moving-frames=2 (span 7) kept #24..#33; diffs: 0 18 0 0 0 0 0 15 0 0
TR book: book screen open: text="Джиан, Филип" text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" 
TREC b-book-back: frames=38 motion-start=#30 moving-frames=3 (span 4) kept #28..#37; diffs: 0 0 18 8 0 11 0 0 0 0
TREC c-event-open: frames=36 motion-start=#27 moving-frames=4 (span 5) kept #26..#35; diffs: 0 21 16 0 14 11 0 0 0 0
TR event: event screen open: content-desc="Събота, 21 ноември 2026, след 8 дни" text="Панайот Пипков · Ден на семейството" text="Събитие от годишния 
TREC d-event-back: frames=38 motion-start=#20 moving-frames=4 (span 13) kept #19..#28; diffs: 0 8 0 0 0 0 0 37 0 0
TREC e-article-open: frames=36 motion-start=#25 moving-frames=6 (span 8) kept #24..#33; diffs: 0 3 48 0 12 43 0 11 9 0
TR article: article open: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 20
TREC f-article-back: frames=37 motion-start=#21 moving-frames=5 (span 15) kept #20..#29; diffs: 0 4 0 0 0 1 24 0 0 0
CARD28 try 1 (field 540 691, save 540 1104): text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARD28: card shown
TREC g-card-full: frames=46 motion-start=#0 moving-frames=0 (span 0) kept #0..#9; diffs: 0 0 0 0 0 0 0 0 0 0
TR card fullscreen: yes
TREC h-card-back: frames=50 motion-start=#0 moving-frames=0 (span 0) kept #0..#9; diffs: 0 0 0 0 0 0 0 0 0 0
TR part so far: FATAL(any process)=0 of the app=0 ANR=0
## TR2. Quick open/back ×12 on a book and an event (the shared-element crash on API 26 came during navigation)
after 12 quick book open/back: app in front yes; pid 5980; FATAL(any process)=0 of the app=0 ANR=0
after 6 scanner/manual loops: app in front yes; pid 5980; FATAL(any process)=0 of the app=0 ANR=0
## TR3. „Старият вестник“ on API 28: double tap with two overlapping 'input tap' (sendevent was too slow on old images)
NEWSPAPER double tap try 1 (API 28): diff vs the page 28.00
NEWSPAPER double tap try 2 (API 28): diff vs the page 0.47
NEWSPAPER double tap try 3 (API 28): diff vs the page 28.00
TR part: FATAL(any process)=0 of the app=0 ANR=0; FATAL lines in the whole log: 0
contact sheets: 10 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 1 | Първи кадър: медиана 2719 мс, последен 2719 мс | Пълно показване: медиана 2719 мс, последно 2719 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 1" text="Първи кадър: медиана 2719 мс, последен 2719 мс" text="Пълно показване: медиана 2719 мс, последно 2719 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5980']): 43 in 11 groups
      12  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      12  W/GoogleApiManager: The service for mu# is not available: ss#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
       6  W/ConnectionStatusConfig: Dynamic lookup for intent failed for action: com.google.android.gms.chimera.container.moduleinstall.ModuleInst
       6  W/GmsClient: unable to connect to service: com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.STAR
       1  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
       1  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       1  E/eglCodecCommon: glUtilsParamSize: unknow param #
       1  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=d#b#cf-#-#b#-#-d#c#e#e#, generation=#)
       1  W/PackageParser: Unknown element under <manifest>: queries at /data/user/#/org.chyavorec.app/no_backup/updates/update-#.apk.par
       1  W/LibraryVersion: Failed to get app version for libraryName: play-services-code-scanner
contact sheets: 11 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5980']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=164 total=369
== Davey: 19
   davey events=19 max=3086ms median=771ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91354008 Nov 13 16:25 app-mapping.txt; crashes: 0 B
```

## tr36

```
part tr36; APK: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5165 ms
## TR1. Transitions recorded at 12 fps (animations ON, API 36): 10 frames from the start of the motion, in sheets/tr
TREC a-book-open: frames=43 motion-start=#26 moving-frames=2 (span 6) kept #25..#34; diffs: 0 18 1 0 0 0 16 0 0 0
TR book: book screen open: text="Джиан, Филип" text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" 
TREC b-book-back: frames=45 motion-start=#32 moving-frames=3 (span 7) kept #31..#40; diffs: 0 19 0 0 0 0 18 3 0 0
TREC c-event-open: frames=39 motion-start=#25 moving-frames=4 (span 9) kept #24..#33; diffs: 0 2 0 0 26 0 0 20 0 8
TR event: event screen open: content-desc="Понеделник, 16 ноември 2026, след 3 дни" text="80 г. от създаването на ЮНЕСКО" text="Събитие от годишния к
TREC d-event-back: frames=37 motion-start=#22 moving-frames=5 (span 13) kept #21..#30; diffs: 0 2 0 0 0 9 0 0 0 31
TREC e-article-open: frames=41 motion-start=#24 moving-frames=8 (span 17) kept #23..#32; diffs: 0 3 50 0 7 0 4 4 0 0
TR article: article open: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 20
TREC f-article-back: frames=36 motion-start=#20 moving-frames=5 (span 12) kept #19..#28; diffs: 0 4 0 0 1 0 0 8 0 57
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
TREC g-card-full: frames=62 motion-start=#0 moving-frames=0 (span 0) kept #0..#9; diffs: 0 0 0 0 0 0 0 0 0 0
TR card fullscreen: yes
TREC h-card-back: frames=71 motion-start=#0 moving-frames=0 (span 0) kept #0..#9; diffs: 0 0 0 0 0 0 0 0 0 0
TR part so far: FATAL(any process)=0 of the app=0 ANR=0
## TR2. Quick open/back ×12 on a book and an event (the shared-element crash on API 26 came during navigation)
after 12 quick book open/back: app in front NO; pid 3943; FATAL(any process)=0 of the app=0 ANR=0
after 6 scanner/manual loops: app in front yes; pid 3943; FATAL(any process)=0 of the app=0 ANR=0
## TR5. Widget providers on API 36 (categories: 1 = home screen, 2 = keyguard)
min=(57345x12801) widgetCategory=1 initialLayout=#7f0e0070 min=(64001x28161) widgetCategory=3 initialLayout=#7f0d0031 min=(46081x28161) widgetCategory=3 initialLayout=#7f0d002d 

lock-screen widgets feature: config false; communal null
TR part: FATAL(any process)=0 of the app=0 ANR=0; FATAL lines in the whole log: 1
contact sheets: 8 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: 1 entries, 1 distinct
     1  update.verify: signature mismatch
   keys of an entry: at, error, tag
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36 (1507400) | Профил: чака | Студен старт · 1 | Първи кадър: медиана 2771 мс, последен 2771 мс | Пълно показване: медиана 2771 мс, последно 2771 мс | Топъл старт · 1 | Първи кадър: медиана 2485 мс, последен 2485 мс | Пълно показване: медиана 2644 
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36 (1507400)" text="Профил: чака" text="Студен старт · 1" text="Първи кадър: медиана 2771 мс, последен 2771 мс" text="Пълно показване: медиана 2771 мс, последно 2771 мс" text="Топъл старт · 1" text="Първи кадър: 
APP W/E lines (pids ['3943']): 234 in 34 groups
      49  W/Codec2Client: query -- param skipped: index = #.
      36  W/ColorUtils: expected specified color aspects (#:#:#:#)
      23  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
      22  W/HWUI: Image decoding logging dropped!
      21  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
      13  E/MediaCodec: Media Quality Service not found.
      13  W/CCodec: This behavior is subject to change. It is recommended that app developers double check whether the requested m
      13  E/g.chyavorec.app: Failed to query component interface for required system resources: #
       6  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#b#c#f
       4  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=tv#@#f#
       3  W/HWUI: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       3  W/HWUI: Failed to initialize #-# format, error = EGL_SUCCESS
       2  W/JobInfo: Requested important-while-foreground flag for job# is ignored and takes no effect
       2  W/g.chyavorec.app: Suspending all threads took: #.#ms
       2  W/RemoteInputConnectionImpl: requestCursorUpdates on inactive InputConnection
       2  W/FrameTracker: Missed App frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
       2  W/FrameTracker: Missed SF frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
       2  W/g.chyavorec.app: Missing inline cache for void wm#.u()
       1  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       1  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, goldmont, goldmont-plus, goldmont-without-sha-xsaves, tremont, 
       1  E/g.chyavorec.app: Not starting debugger since process cannot load the jdwp agent.
       1  E/ashmem: Pinning is deprecated since Android Q. Please use trim or other methods.
       1  W/HWUI: Unknown dataspace #
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#bf#-#a#-#a#-#a#-#a#e, generation=#)
       1  W/g.chyavorec.app: Missing inline cache for java.lang.Object y#.s(java.lang.Object)
       1  W/InteractionJankMonitor: Initializing without READ_DEVICE_CONFIG permission. enabled=true, interval=#, missedFrameThreshold=#, frameTim
       1  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@e#
       1  W/FrameTracker: Missed App frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
       1  W/FrameTracker: Missed SF frame:UNKNOWN: #, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
contact sheets: 9 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['3943']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 23
   skipped-frames events=23 max=171 total=1160
== Davey: 68
   davey events=68 max=3213ms median=857ms
== NativeLink: 1
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91354008 Nov 13 16:25 app-mapping.txt; crashes: 1520 B
```

## wdg36

```
part wdg36; APK: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5087 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## WD1. The main widget from Settings › Уиджети › „Добави уиджета „Читалище Яворец““
PIN main dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Chitalishte Yavorets" text="Chitalishte Yavorets" text="Scan" text="Catalogue" text="2 books to return" text="in 3 days" text="Oct 15" text="Next event" text="Literary evening" text="Octobe
HOME after the main pin: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 223 1023 809]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; error text: 
widgets of ours bound: id=2 
## WD2. „Книга на деня“ from Settings
PIN book dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Book of the day" text="Book of the day" text="Under the Yoke" text="Ivan Vazov" text="Book of the day" content-desc="Book of the day widget" text="3 × 2" content-desc="3 wide by 2 high" tex
HOME after both pins: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_]; bounds [57 223 1023 809]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|Книга на деня|Легенда за полето|Петър Николов|; error text: 
widgets of ours bound: id=2 id=3 
      1     provider=ProviderId{user:0, app:10174, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver}}
      1     provider=ProviderId{user:0, app:10174, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver}}
widgetCategory=1 widgetCategory=3 

## WD3. After the catalogue is loaded; dark theme (10 s, 40 s and 90 s after the switch); light again
WARN: tab bar not reached
WIDGET after the catalogue loaded: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_]; bounds [57 223 1023 809]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|Книга на деня|Легенда за полето|Петър Николов|; error text: 
WIDGET dark after 10 s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET dark after 40 s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET dark after 90 s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET light again: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
## WD4. Click-through
WIDGET click catalog: 'widget_catalog' not on the home screen
WIDGET click scan: 'widget_scan' not on the home screen
WIDGET click event: 'widget_event' not on the home screen
WIDGET click app: 't:Chitalishte Yavorets|Читалище Яворец' not on the home screen
WDG part: FATAL(any process)=0 of the app=0 ANR=0; FATAL lines in the whole log: 0
contact sheets: 3 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: 1 entries, 1 distinct
     1  update.verify: signature mismatch
   keys of an entry: at, error, tag
WARN: settings item =Диагностика|=Diagnostics not found
APP W/E lines (pids ['5225', '7541', '8361']): 28 in 10 groups
       6  E/OpenGLRenderer: Unable to match the desired swap behavior.
       4  W/Parcel: Expecting binder but got null!
       3  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       3  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       3  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       3  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       3  W/Gralloc4: allocator #.x is not supported
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=bcd#bc-d#-#d#-#-b#a#ea#, generation=#)
       1  W/PackageParser: Unknown element under <manifest>: queries at /data/user/#/org.chyavorec.app/no_backup/updates/update-#.apk.par
       1  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
contact sheets: 3 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5225', '7541', '8361']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 34
   skipped-frames events=34 max=182 total=1908
== Davey: 62
   davey events=62 max=4939ms median=983ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91354008 Nov 13 16:25 app-mapping.txt; crashes: 0 B
```

## yup36

```
part yup36; APK: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
base APK: 1.2.35 aef8910 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507364; update manifest: the real one (releases/latest) size 4317894 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.36 6432ef1 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1507400; update manifest: the real one (releases/latest) size 4583950 bytes
installed:  versionCode=1507400 minSdk=26 targetSdk=37  versionName=1.2.36 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.36 6432ef1 (org.chyavorec.app.dev, demo sign-in DEMO-0001);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.36-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3297 ms
Запомни ме before: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
Запомни ме now: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
MY after the demo sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" 
## YU1. Swipe-down dismiss with exact distances (motionevent, ×3): the rule is ≥140 dp at any speed; the finger-up position must count
YEAR3 A-moves-to-183-up-183 (UP at 183 dp; MOVE at: 23 46 69 92 115 138 183 dp): closed closed closed
YEAR3 B-last-move-114-up-183 (UP at 183 dp; MOVE at: 23 46 69 92 114 dp): closed closed closed
YEAR3 C-moves-to-160-up-160 (UP at 160 dp; MOVE at: 40 80 120 160 dp): closed closed closed
YEAR3 D-one-move-20-up-183 (UP at 183 dp; MOVE at: 20 dp): closed closed closed
YEAR3 E-moves-to-100-up-100 (UP at 100 dp; MOVE at: 25 50 75 100 dp): open open open
YEAR3 F-moves-to-139-up-139 (UP at 139 dp; MOVE at: 35 70 105 139 dp): open open open
YEAR3 G-last-move-114-up-150 (UP at 150 dp; MOVE at: 30 60 90 114 dp): closed closed closed
YEAR3 H-last-move-130-up-136 (UP at 136 dp; MOVE at: 40 80 130 dp): open open open
## YU2. 'input swipe' (×3 each): 200 dp in 300 / 150 / 600 ms, 120 dp slow, 150 dp in 300 ms
YEAR2 new swipe d200t300 (525 px ≈ 200 dp in 300 ms): closed closed closed
YEAR2 new swipe d200t150 (525 px ≈ 200 dp in 150 ms): closed open open
YEAR2 new swipe d200t600 (525 px ≈ 200 dp in 600 ms): closed closed closed
YEAR2 new swipe d120slow (315 px ≈ 120 dp in 900 ms): open open open
YEAR2 new swipe d150t300 (393 px ≈ 149 dp in 300 ms): closed closed closed
YEAR2 new swipe d135t300 (354 px ≈ 134 dp in 300 ms): open open open
## YU3. Every slide by auto-advance (no touches; 5.5 s per slide): an emulator screenshot every ~2.5 s
YEAR after ~55 s of auto-advance: open; content-desc="Благодарим ти, че четеш с нас! Библиотеката на читалището те очаква и през 2027 г. Твоята шевица: лента, избродирана от 30 книги през годината." text="Моята читателска година" text="2026" content-desc="Затвори" text="Сподели" 
## YU4. The last slide by 7 quick taps on the right, its texts, share as an image
YEAR last slide: content-desc="Благодарим ти, че четеш с нас! Библиотеката на читалището те очаква и през 2027 г. Твоята шевица: лента, избродирана от 30 книги през годината." text="Моята читателска година" text="2026" content-desc="Затвори" text="Сподели" 
YEAR one more tap on the last slide: closed
YEAR share from the last slide: chooser OPEN after 1 s; cache/share: 811439 2026-11-13 16:54 chyavorec-year-2026-outro-1794588858631.png;
YEAR share file: chyavorec-year-2026-outro-1794588858631.png 811439 B; PNG (1080, 1920) RGBA
SHARE year received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.dev.files/share/chyavorec-year-2026-outro-1794588858631.png mime=image/png bytes=811439 png=true dims=1080x1920 subject=Моята читателска година 2026 text=Моята читателска година 2026 в библиотеката на НЧ „Васил Левски – 1922“ — https://chyavorec.org
## YU5. Dark theme: the last slide
YUP part: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 8 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.36-dev (1) | Профил: няма | Студен старт · 1 | Първи кадър: медиана 3903 мс, последен 3903 мс | Пълно показване: медиана 3903 мс, последно 3903 мс | Топъл старт · 1 | Първи кадър: медиана 357 мс, последен 357 мс | Насечени кадри по екрани | login: 
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.36-dev (1)" text="Профил: няма" text="Студен старт · 1" text="Първи кадър: медиана 3903 мс, последен 3903 мс" text="Пълно показване: медиана 3903 мс, последно 3903 мс" text="Топъл старт · 1" text="Първи кадър: медиана 357 мс, последен 357 мс" text="Насечени кадри по екрани" text="login: 5,1 % от 78 кадъра · най-бавен 289 мс" text="общо с видеокартата: средно 126 мс, най-бавен 394 мс" text="my: 4,6 % от 2418 кадъра · най-бавен 662 мс" text="общо с видеокартата: средно 286 мс, най-бавен 923 мс" text="onboarding: 2,0 % от 49 кадъра · най-бавен 654 мс
APP W/E lines (pids ['6216']): 239 in 104 groups
      12  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
      11  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       5  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.InnerNodeCoordinator$LookaheadDelegateImpl.measure-BRTryo#(InnerNodeCoordinator.k
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasureBlock$lambda$#(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate$$ExternalSyntheticLambda#.invoke(D#$$SyntheticClass:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasure-BRTryo#$ui(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performLookaheadMeasure-BRTryo#$ui(LayoutNodeLayoutDeleg
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.remeasure-BRTryo#(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.measure-BRTryo#(LookaheadPassDelegate.kt:#)
       3  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutModifierNodeCoordinator$LookaheadDelegateForLayoutModifierNode.measure-BRTr
       3  W/BasicSecureTextField: 	at androidx.compose.ui.graphics.BlockGraphicsLayerModifier.measure-#p#s#s(GraphicsLayerModifier.kt:#)
       2  W/Parcel: Expecting binder but got null!
       2  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.PlatformPasswordVisibilitySettingImpl.getSystemShowPasswordSetting(BasicS
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextField_androidKt.rememberPlatformPasswordVisibilitySettings
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-ltb#GB#(BasicSecureTextField.
contact sheets: 9 in sheets/
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
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 91354008 Nov 13 16:25 app-mapping.txt; crashes: 0 B
```

