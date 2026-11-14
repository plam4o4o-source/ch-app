# Emulator check of the v1.2.38 candidate (de2e156, wave 23) against the released v1.2.37 (ebf1720)

debug/w24-check (runs in runs.txt, latest 43609408217 @ 590c0f8). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU) unless the part says otherwise (parts ending in 28: API 28; in 26: API 26; c36: API 36). Built prodRelease (minified) x86_64 with a throwaway key; dev38/updd38: devDebug. In the A/B parts new = 1.2.38 and base = 1.2.37. dm33/dm28: the self-update with dex metadata from a local HTTPS server. Screenshots are JPEG, scaled down (sheets/ = contact sheets). Earlier: [../v136r/index.md](../v136r/index.md), [../v136/index.md](../v136/index.md).

| part | status |
|---|---|
| c26 | COMPLETE; part=c26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| c28 | COMPLETE; part=c28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| c36 | COMPLETE; part=c36 target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| dev38 | COMPLETE; part=dev38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| dev38b | COMPLETE; part=dev38b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| dm28 | COMPLETE; part=dm28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| dm33 | COMPLETE; part=dm33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mon38 | COMPLETE; part=mon38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| perf38 | COMPLETE; part=perf38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| perfb38 | COMPLETE; part=perfb38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| start38a | COMPLETE; part=start38a target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| start38b | COMPLETE; part=start38b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tour38 | COMPLETE; part=tour38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tour38b | COMPLETE; part=tour38b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| ui38 | COMPLETE; part=ui38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| ui38b | COMPLETE; part=ui38b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| upd38 | COMPLETE; part=upd38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| updd38 | COMPLETE; part=updd38 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
devDebug 1.2.38 de2e156 (org.chyavorec.app.dev, demo sign-in DEMO-0001, shared debug key)
1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest)
app.apk 4640810 bytes
app-dev.apk 28796723 bytes
```

## c26

```
part c26; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2026 ms
## C1. Tour of all screens on API 26
## T-api26-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · Празника „38 читалища. Един  · 27 септември 2026 · Сканирай · Читателска карта
CAP api26-light/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра
CAP api26-light/01d-home-4: targets=15 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА
CAP api26-light/01e-search-12: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=36 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Търси книга, новина, събитие · Сканирай баркод · 14 ноември 2026 · *Св. ап. Ф · Последни новини · СЪБИТИЕ
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
CAP api26-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Легенда за зима · Още една · Към книгата · Затвори
CAP api26-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api26-light/05b-my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Примерна читателска карта · Баркод на карта 19220001 · Така ще изглежда картата ти · Вход · Добави читателска карта · БИБЛИОТЕКА
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
CAP api26-light/12-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-light/12b-more-2: targets=16 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа · Старият вестник
CAP api26-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api26-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api26-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api26-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api26-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api26-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP api26-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-light/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api26-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP api26-light/18c-settings-3: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=17 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията
CAP api26-light/18d-settings-4: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38 · ДАННИ · Корици от интернет · Корици по ISBN от covers.ope
CAP api26-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api26-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api26-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api26-light/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api26-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api26-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api26-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api26-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api26-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api26-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api26-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api26-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api26-light: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=18 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=57
app theme → =Тъмна|=Dark: text="ИЗГЛЕД" text="Тема" text="Тъмна" text="Размер на текста" 
## T-api26-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api26-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api26-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · С хоро и песен във Водица · 26 септември 2026 · Моите книги · Читателска карта
CAP api26-dark/01c-home-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра
CAP api26-dark/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=21 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api26-dark/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
WARN: tab bar not reached
CAP api26-dark/02-catalog: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Назад · Търси книга, новина, събитие · Сканирай баркод · Всички · Книги
CAP api26-dark/02g-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api26-dark/03-book: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api26-dark/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api26-dark/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api26-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Окото на пламък · Още една · Към книгата · Затвори
CAP api26-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP api26-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP api26-dark/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api26-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api26-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api26-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/08b-news-2: targets=19 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=24 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api26-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api26-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api26-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api26-dark/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api26-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api26-dark/12-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api26-dark/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен
CAP api26-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api26-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api26-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api26-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api26-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api26-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP api26-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api26-dark/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ИЗГЛЕД · Тема · Тъмна · Размер на текста · Стандартен · Палитра
CAP api26-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP api26-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=18 ellipsized=0 | Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията · Заемания, събития, съобщения
CAP api26-dark/18d-settings-4: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38 · ДАННИ · Корици от интернет · Корици по ISBN от covers.ope
CAP api26-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api26-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api26-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api26-dark/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api26-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api26-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api26-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api26-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api26-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api26-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api26-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api26-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api26-dark: A11Y AGG: 52 screens analysed, 0 dumps failed; totals MISSING=8 DOUBLE=0 SAME-LABEL=3 SMALL(<48dp)=14
app theme → =Както системата|=System default: text="ИЗГЛЕД" text="Тема" text="Както системата" text="Размер на текста" 
A11Y AGG: 109 screens analysed, 0 dumps failed; totals MISSING=26 DOUBLE=0 SAME-LABEL=8 SMALL(<48dp)=71
== MISSING: 19 distinct
   View@[0,1925][1080,2051]  [1 screens: 08b-news-2]
   View@[121,2124][340,2274]  [1 screens: 11-event]
   View@[408,2124][597,2274]  [1 screens: 11-event]
   View@[665,2124][959,2274]  [1 screens: 11-event]
   View@[42,1765][524,1938]  [1 screens: 12-more]
   View@[556,1765][1038,1938]  [1 screens: 12-more]
   View@[42,2256][524,2274]  [1 screens: 13-gallery]
   View@[556,2256][1038,2274]  [1 screens: 13-gallery]
   View@[42,0][378,97]  [1 screens: 01d-home-4]
   View@[415,0][751,97]  [1 screens: 01d-home-4]
   View@[788,0][1080,97]  [1 screens: 01d-home-4]
   View@[0,1906][1080,2032]  [1 screens: 02-catalog]
   View@[53,127][529,190]  [1 screens: 02c-filters-2]
   View@[550,127][929,190]  [1 screens: 02c-filters-2]
   View@[0,1934][1080,2060]  [1 screens: 08b-news-2]
   View@[42,231][524,236]  [1 screens: 12b-more-2]
   View@[556,231][1038,236]  [1 screens: 12b-more-2]
   View@[0,1904][1080,2030]  [1 screens: 12b-more-2]
   View@[42,2252][1038,2274]  [1 screens: 18c-settings-3]
== SMALL: 27 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   ''  [4 screens: 02c-filters-2, 12b-more-2, 13-gallery, 18c-settings-3]
   'Всички'  [2 screens: 01d-home-4, 08b-news-2]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Запази: Фолклорно пътешествие '  [1 screens: 08-news]
   'Запазени'  [1 screens: 08b-news-2]
## C2. Home ribbon and the card on API 26
IDLE card26-3-18s (15 s, no input): frames +0; cpu=1.5% (ticks 23 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=1.3%, DefaultExecutor=0.2%; pid 17147
CARD fullscreen on API 26: yes; app in front yes
IDLE cardfull26-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 17147
## C3. Surprise ×2, share a book as an image on API 26
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book26: chooser OPEN after 4 s; cache/share: 2214407 2026-11-14 14:32 chyavorec-book-399560-1794666753973.png;
SHARE book26 file: chyavorec-book-399560-1794666753973.png 2214407 B; PNG (1080, 1920) RGBA
SHARE book26 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794666753973.png mime=image/png bytes=2214407 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C5. Rotation and process death on API 26 (book, catalogue, article)
ROT book: pid 17147→17147 (same); landscape: kept 40% of 25 texts; lost e.g. ['Вид', 'Език', 'Отдел', 'книга']; back to portrait: kept 100% of 25 texts
PROCDEATH book: killed NO (17147); new pid 17147; restored: kept 100% of 25 texts; front app
ROT catalog: pid 17147→17147 (same); landscape: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият пътя', 'Белият дома', 'Мартин Тонев']; back to portrait: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият пътя', 'Белият дома', 'Елена Русева']
PROCDEATH catalog: killed NO (17147); new pid 17147; restored: kept 47% of 34 texts; lost e.g. ['Изчисти', 'Белият пътя', 'Белият дома', 'Мартин Тонев']; front app
ROT article: pid 17147→17147 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed NO (17147); new pid 17147; restored: kept 100% of 9 texts; front app
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
first-install catalogue results after 1423 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19941
MONKEY exit=0 duration=92 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19941'; airplane=0; camera: 
MEM after-monkey (raw) PSS=85288KB  Java=14508KB  Native=37644KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=21)  RSS=?KB  Views=16 Activities=1
MEM after-monkey (trimmed) PSS=79162KB  Java=8652KB  Native=37556KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=21)  RSS=?KB  Views=16 Activities=1
## C6. Monkey 2000 (seed 4242) on API 26
## W. First launch and warm-up
first-install catalogue results after 1342 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20442
MONKEY exit=0 duration=90 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20442'; airplane=0; camera: 
MEM after-monkey (raw) PSS=85284KB  Java=15164KB  Native=35544KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=23 Activities=1
MEM after-monkey (trimmed) PSS=81620KB  Java=11432KB  Native=35420KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=23 Activities=1
## C6. Monkey 2000 (seed 777) on API 26
## W. First launch and warm-up
first-install catalogue results after 2101 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20896
MONKEY exit=0 duration=75 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20896'; airplane=0; camera: 
MEM after-monkey (raw) PSS=85041KB  Java=13112KB  Native=38256KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=14 Activities=1
MEM after-monkey (trimmed) PSS=80395KB  Java=8596KB  Native=38256KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=?KB  Views=14 Activities=1
contact sheets: 17 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: няма | Студен старт · 7 | Първи кадър: медиана 1215 мс, последен 1141 мс | Пълно показване: медиана 1648 мс, последно 1640 мс | Топъл старт · 5 | Първи кадър: медиана 189 мс, последен 161 мс | Пълно показване: медиана 723 мс,
DIAG SCREEN (scrolled): text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38 (1510100)" text="Профил: няма" text="Студен старт · 7" text="Първи кадър: медиана 1215 мс, последен 1141 мс" text="Пълно показване: медиана 1648 мс, последно 1640 мс" text="Топъл старт · 5" text="Първи кадър: медиана 189 мс, последен 161 мс" text="Пълно показване: медиана 723 мс, последно 723 мс" text="Насечени кадри по екрани" text="card: 76,5 % от 34 кадъра · най-бавен 400 мс" text="CPU време на кадъра (без видеокартата): средно 314 мс, най-бавен 605 мс" text="book: 69,1 % от 81 кадъра · най-бавен 325 мс" text="CPU време на кадъра (без видеок
APP W/E lines (pids ['4949', '5902', '11454', '17147', '19941', '20442', '20896']): 1542 in 124 groups
      75  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
      73  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      68  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      57  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      56  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      28  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      26  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      26  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      24  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
      23  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      22  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
      21  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      20  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      20  W/GoogleApiManager: The service for sx# is not available: ft#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
      20  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
      18  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      18  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      15  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      15  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      15  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      14  E/ResourceType: Style contains key with bad entry: #
      12  W/BasicSecureTextField: 	at b#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      12  W/BasicSecureTextField: 	at yo#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      12  W/BasicSecureTextField: 	at hd#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      12  W/BasicSecureTextField: 	at fz#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      12  W/BasicSecureTextField: 	at id#.H#(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      12  W/BasicSecureTextField: 	at id#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      12  W/BasicSecureTextField: 	at ty.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      12  W/BasicSecureTextField: 	at uy#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
contact sheets: 18 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4949', '5902', '11454', '17147', '19941', '20442', '20896']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 21
   skipped-frames events=21 max=59 total=792
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:03 app-mapping.txt; crashes: 0 B
```

## c28

```
part c28; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2175 ms
## C1. Tour of all screens on API 28
## T-api28-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api28-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api28-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · СЪБИТИЕ · Празника „38 читалища. Един  · 27 септември 2026 · Сканирай · Читателска карта
CAP api28-light/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP api28-light/01d-home-4: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api28-light/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api28-light/02-catalog: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api28-light/02g-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api28-light/03-book: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api28-light/03b-book-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api28-light/03c-book-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | 12 -те вятъра · Гарфилд, Джудит и др. · Налична · Наличност към 09.11.2026 · Искам да прочета · Библиографски данни
CAP api28-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Въображаема Евр · Още една · Към книгата · Затвори
CAP api28-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api28-light/05b-my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Примерна читателска карта · Баркод на карта 19220001 · Така ще изглежда картата ти · Вход · Добави читателска карта · БИБЛИОТЕКА
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api28-light/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api28-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api28-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api28-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-light/08b-news-2: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api28-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api28-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api28-light/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-light/12-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api28-light/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=24 ellipsized=0 | Снимки от сайта · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица
CAP api28-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api28-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api28-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api28-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api28-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api28-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP api28-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-light/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api28-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP api28-light/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=18 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията
CAP api28-light/18d-settings-4: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38 · ДАННИ · Корици от интернет · Корици по ISBN от covers.ope
CAP api28-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api28-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api28-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api28-light/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api28-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api28-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api28-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api28-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api28-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api28-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api28-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api28-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api28-light: A11Y AGG: 52 screens analysed, 0 dumps failed; totals MISSING=7 DOUBLE=0 SAME-LABEL=3 SMALL(<48dp)=15
WARN: Settings not found
## T-api28-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP api28-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api28-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · Празника „38 читалища. Един  · 27 септември 2026 · Моите книги · Читателска карта · Искам да прочета
CAP api28-dark/01c-home-3: targets=16 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP api28-dark/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Пътят на пътя · Огънят на здрача · Дъхът на любовта · ВИТРИНА · Най търсени · Далечният морето
CAP api28-dark/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api28-dark/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api28-dark/02c-filters-2: targets=23 MISSING=2 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP api28-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api28-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=44 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api28-dark/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api28-dark/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-dark/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api28-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Сред лимоновите · Още една · Към книгата · Затвори
CAP api28-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP api28-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP api28-dark/06-card: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api28-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api28-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api28-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-dark/08b-news-2: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=27 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api28-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api28-dark/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP api28-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api28-dark/12-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api28-dark/12b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа
CAP api28-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api28-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api28-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · Сподели · Отвори в сайта · За снимката
CAP api28-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | 70-те години · Затвори · 4 / 29  ·  70-те години · 70-те години · Сподели · Отвори в сайта
CAP api28-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api28-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP api28-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api28-dark/18-settings: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api28-dark/18b-settings-2: targets=11 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=19 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Език · Както системата · Сезонни украси · Празници, сезони и обичаи: п
CAP api28-dark/18c-settings-3: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=17 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията
CAP api28-dark/18d-settings-4: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38 · ДАННИ · Корици от интернет · Корици по ISBN от covers.ope
CAP api28-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api28-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api28-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api28-dark/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api28-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api28-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api28-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api28-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api28-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api28-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api28-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api28-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api28-dark: A11Y AGG: 53 screens analysed, 0 dumps failed; totals MISSING=18 DOUBLE=0 SAME-LABEL=3 SMALL(<48dp)=56
app theme → =Както системата|=System default: text="ИЗГЛЕД" text="Тема" text="Както системата" text="Размер на текста" 
A11Y AGG: 105 screens analysed, 0 dumps failed; totals MISSING=25 DOUBLE=0 SAME-LABEL=6 SMALL(<48dp)=71
== MISSING: 18 distinct
   View@[42,0][777,31]  [1 screens: 01c-home-3]
   View@[809,0][1080,31]  [1 screens: 01c-home-3]
   View@[0,1906][1080,2032]  [1 screens: 02-catalog]
   View@[53,127][529,196]  [1 screens: 02c-filters-2]
   View@[550,127][929,196]  [1 screens: 02c-filters-2]
   View@[913,1936][1039,1981]  [1 screens: 08b-news-2]
   View@[121,2124][340,2274]  [1 screens: 11-event]
   View@[408,2124][597,2274]  [1 screens: 11-event]
   View@[665,2124][959,2274]  [1 screens: 11-event]
   View@[42,1765][524,1938]  [1 screens: 12-more]
   View@[556,1765][1038,1938]  [1 screens: 12-more]
   View@[42,231][524,312]  [1 screens: 12b-more-2]
   View@[42,2256][524,2274]  [1 screens: 13-gallery]
   View@[556,2256][1038,2274]  [1 screens: 13-gallery]
   View@[84,231][370,236]  [1 screens: 18b-settings-2]
   View@[396,231][683,236]  [1 screens: 18b-settings-2]
   View@[709,231][996,236]  [1 screens: 18b-settings-2]
   View@[42,2248][1038,2274]  [1 screens: 18c-settings-3]
== SMALL: 27 distinct
   ''  [6 screens: 02c-filters-2, 08b-news-2, 12b-more-2, 13-gallery, 18b-settings-2, 18c-settings-3]
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
## C2. Home ribbon and the card on API 28
IDLE card28-3-18s (15 s, no input): frames +0; cpu=3.4% (ticks 51 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=2.7%, DefaultExecutor=0.6%, Profile Saver=0.1%; pid 17414
CARD fullscreen on API 28: yes; app in front yes
IDLE cardfull28-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 17414
## C3. Surprise ×2, share a book as an image on API 28
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book28: chooser OPEN after 1 s; cache/share: 433360 2026-11-14 14:30 chyavorec-book-399560-1794666617991.png;
SHARE book28 file: chyavorec-book-399560-1794666617991.png 433360 B; PNG (1080, 1920) RGBA
SHARE book28 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794666617991.png mime=image/png bytes=433360 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C5. Rotation and process death on API 28 (book, catalogue, article)
ROT book: pid 17414→17414 (same); landscape: kept 40% of 25 texts; lost e.g. ['Вид', 'Език', 'Отдел', 'книга']; back to portrait: kept 100% of 25 texts
PROCDEATH book: killed yes; new pid 18459; restored: kept 96% of 25 texts; lost e.g. ['Последна синхронизация: Преди ']; front app
ROT catalog: pid 18459→18459 (same); landscape: kept 63% of 35 texts; lost e.g. ['·', 'налична', 'Белият пътя', 'Белият дома']; back to portrait: kept 91% of 35 texts; lost e.g. ['ISBN', 'Автор', 'Ключова дума']
PROCDEATH catalog: killed yes; new pid 18868; restored: kept 100% of 35 texts; front app
ROT article: pid 18868→18868 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed yes; new pid 19122; restored: kept 100% of 9 texts; front app
## C5b. Scanner screen: manual entry and back ×2, repeated ×8 (the API 26 crash came right after this in round 1)
SCAN loop 1: app in front yes; text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсва
SCAN loop 2: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 3: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 4: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 5: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 6: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 7: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
SCAN loop 8: app in front yes; text="12" content-desc="Изчисти" text="Всичко" 
## C6. Monkey 2000 (seed 1357) on API 28
## W. First launch and warm-up
first-install catalogue results after 1389 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=20235
MONKEY exit=0 duration=73 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '20235'; airplane=0; camera: 
MEM after-monkey (raw) PSS=91768KB  Java=16772KB  Native=45180KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=22 Activities=1
MEM after-monkey (trimmed) PSS=85267KB  Java=11936KB  Native=43520KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=22 Activities=1
## C6. Monkey 2000 (seed 4242) on API 28
## W. First launch and warm-up
WARN: tab bar not reached
first-install: NO catalogue results
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=22398
MONKEY exit=0 duration=64 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '22398'; airplane=0; camera: 
MEM after-monkey (raw) PSS=89760KB  Java=17492KB  Native=43252KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=14 Activities=1
MEM after-monkey (trimmed) PSS=79378KB  Java=8372KB  Native=41864KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=16)  RSS=?KB  Views=14 Activities=1
## C6. Monkey 2000 (seed 777) on API 28
## W. First launch and warm-up
first-install catalogue results after 1552 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=22921
MONKEY exit=0 duration=62 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '22921'; airplane=0; camera: 
MEM after-monkey (raw) PSS=87214KB  Java=11532KB  Native=45728KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=14 Activities=1
MEM after-monkey (trimmed) PSS=83529KB  Java=7816KB  Native=45852KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=14 Activities=1
contact sheets: 15 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: tab bar not reached
WARN: Settings not found
APP W/E lines (pids ['5547', '6544', '12359', '17414', '18459', '18868', '19122', '20235', '22398', '22921']): 764 in 119 groups
      38  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
      34  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
      32  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
      31  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
      29  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
      20  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
      20  W/GoogleApiManager: The service for sx# is not available: ft#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
      19  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      19  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      18  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      16  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
      15  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
      14  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
      13  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[
      12  E/eglCodecCommon: glUtilsParamSize: unknow param #
      11  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
      10  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
      10  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      10  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
      10  W/ConnectionStatusConfig: Dynamic lookup for intent failed for action: com.google.android.gms.chimera.container.moduleinstall.ModuleInst
      10  W/GmsClient: unable to connect to service: com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.STAR
       9  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       9  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=-#.#, y
       9  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
       8  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       7  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
       6  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
       5  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       5  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
contact sheets: 15 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5547', '6544', '12359', '17414', '18459', '18868', '19122', '20235', '22398', '22921']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=60 total=226
== Davey: 8
   davey events=8 max=1394ms median=812ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 0 B
```

## c36

```
part c36; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4046 ms
## C1. Tour of all screens on API 36
## T-api36-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP api36-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api36-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | 14 ноември 2026 · *Св. ап. Ф · Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай
CAP api36-light/01c-home-3: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Сканирай · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP api36-light/01d-home-4: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api36-light/01e-search-12: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=2 SMALL(<48dp)=0 | front: app | texts=42 ellipsized=0 | 2 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP api36-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api36-light/02c-filters-2: targets=24 MISSING=1 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел
CAP api36-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api36-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api36-light/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api36-light/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-light/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Победа на сърце · Още една · Към книгата · Затвори
CAP api36-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api36-light/05b-my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Примерна читателска карта · Читателски № 19220001 · Баркод на карта 19220001 · Така ще изглежда картата ти · Вход · Добави читателска карта
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP api36-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api36-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api36-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api36-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-light/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=26 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api36-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api36-light/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-light/12-more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api36-light/12b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен
CAP api36-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api36-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api36-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP api36-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP api36-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api36-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP api36-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-light/18-settings: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api36-light/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP api36-light/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“
CAP api36-light/18d-settings-4: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=18 ellipsized=0 | Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията · Заемания, събития, съобщения · ОБНОВЛЕНИЯ · Автоматично обновяване
CAP api36-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api36-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api36-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api36-light/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api36-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api36-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api36-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api36-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api36-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api36-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api36-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api36-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api36-light: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=4 DOUBLE=0 SAME-LABEL=7 SMALL(<48dp)=46
## T-api36-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP api36-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP api36-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги · Читателска карта
CAP api36-dark/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP api36-dark/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=21 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP api36-dark/01e-search-12: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=36 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Търси книга, новина, събитие · Сканирай баркод · 14 ноември 2026 · *Св. ап. Ф · Последни новини · БИБЛИОТЕКА
CAP api36-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP api36-dark/02c-filters-2: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP api36-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP api36-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP api36-dark/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP api36-dark/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-dark/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP api36-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Тишината на спо · Още една · Към книгата · Затвори
CAP api36-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP api36-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP api36-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP api36-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP api36-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP api36-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-dark/08b-news-2: targets=19 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=24 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP api36-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP api36-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP api36-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP api36-dark/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP api36-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP api36-dark/12-more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP api36-dark/12b-more-2: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен
CAP api36-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP api36-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP api36-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP api36-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP api36-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP api36-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP api36-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP api36-dark/18-settings: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP api36-dark/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP api36-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=19 ellipsized=0 | Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP api36-dark/18d-settings-4: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38 · ДАННИ
CAP api36-dark/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP api36-dark/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP api36-dark/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP api36-dark/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP api36-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP api36-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP api36-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP api36-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api36-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP api36-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP api36-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP api36-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR api36-dark: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=5 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=48
A11Y AGG: 114 screens analysed, 0 dumps failed; totals MISSING=9 DOUBLE=0 SAME-LABEL=12 SMALL(<48dp)=94
== MISSING: 6 distinct
   View@[0,2012][1080,2127]  [1 screens: 08b-news-2]
   View@[0,2032][1080,2127]  [1 screens: 12b-more-2]
   View@[42,2321][524,2400]  [1 screens: 13-gallery]
   View@[556,2321][1038,2400]  [1 screens: 13-gallery]
   View@[42,2320][1038,2400]  [1 screens: 18-settings]
   View@[53,2320][1027,2400]  [1 screens: 02c-filters-2]
== SMALL: 24 distinct
   'Манипулатор за преместване с п'  [5 screens: 02b-filters, 02c-filters-2, 09c-reader-sheet, 09d-share-sheet, 11c-event-share]
   'Всички'  [2 screens: 01d-home-4, 08b-news-2]
   'Бевълстоук : Кн. 1 - 2, Куин, '  [2 screens: 02e-shelf, 02f-shelf-2]
   'Безсрамна добродетел, Робъртс,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Белият дома, Николай Симеонов,'  [2 screens: 02e-shelf, 02f-shelf-2]
   'Дъхът на спомените — том 3'  [1 screens: 01e-search-12]
   'Гласът на реката'  [1 screens: 01e-search-12]
   '12 -те вятъра, Гарфилд, Джудит'  [1 screens: 02e-shelf]
   'Аз обичам Ню Йорк, Келк, Линдз'  [1 screens: 02e-shelf]
   'Ако върнеш времето назад, Потъ'  [1 screens: 02e-shelf]
   'Алтъна, Никова, Ивелина'  [1 screens: 02e-shelf]
   'Анатомът, Андахази, Федерико'  [1 screens: 02e-shelf]
   'Белият звездите, Диана Христов'  [1 screens: 02e-shelf]
   'Всичко'  [1 screens: 02f-shelf-2]
   'Заглавие'  [1 screens: 02f-shelf-2]
   'Автор'  [1 screens: 02f-shelf-2]
   'ISBN'  [1 screens: 02f-shelf-2]
   'Белият вятъра, Петър Здравков'  [1 screens: 02f-shelf-2]
   'Белият детството, Симеон Колев'  [1 screens: 02f-shelf-2]
   'Запазени'  [1 screens: 08b-news-2]
   'Библиотека'  [1 screens: 08b-news-2]
## C2. Home ribbon and the card on API 36
IDLE card36-3-18s (15 s, no input): frames +96; cpu=48.3% (ticks 724 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=47.7%, g.chyavorec.app=0.3%, Jit thread pool=0.1%, binder:15715_7=0.1%, DefaultExecutor=0.1%; pid 15715
CARD fullscreen on API 36: yes; app in front yes
IDLE cardfull36-30-45s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 15715
## C3. Surprise ×2, share a book as an image on API 36
SURPRISE 1: text="Още една" text="Към книгата" ; app in front yes
SURPRISE 2: text="Още една" text="Към книгата" ; app in front yes
SHARE book36: chooser OPEN after 4 s; cache/share: 918813 2026-11-14 14:38 chyavorec-book-399560-1794667077848.png;
SHARE book36 file: chyavorec-book-399560-1794667077848.png 918813 B; PNG (1080, 1920) RGBA
SHARE book36 received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794667077848.png mime=image/png bytes=918813 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## C4. Predictive back (gesture navigation) on API 36: „Изненадай ме“, a book, an article
gesture nav:  navigation_mode=2
enable_back_animation: null
PBACK surprise open: text="Още една" text="Към книгата" 
PBACK surprise after a cancelled gesture: still open (expected)
PBACK surprise after the committed gesture: closed; catalogue shown; in front app
PBACK surprise mid-gesture vs open: diff 2.34
PBACK book: after the gesture book screen STILL OPEN; in front app
PBACK article: closed; in front app
PBACK viewer: closed; in front app
PBACK on Home (root): in front after the gesture: com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
## C5. Rotation and process death on API 36 (book, catalogue, article)
ROT book: pid 15715→15715 (same); landscape: kept 40% of 25 texts; lost e.g. ['Вид', 'Език', 'книга', 'Отдел']; back to portrait: kept 100% of 25 texts
PROCDEATH book: killed yes; new pid 18047; restored: kept 100% of 25 texts; front app
ROT catalog: pid 18047→18047 (same); landscape: kept 71% of 34 texts; lost e.g. ['Белият пътя', 'Белият дома', 'Ключова дума', 'Мартин Тонев']; back to portrait: kept 100% of 34 texts
PROCDEATH catalog: killed yes; new pid 18456; restored: kept 100% of 34 texts; front app
ROT article: pid 18456→18456 (same); landscape: kept 78% of 9 texts; lost e.g. ['СЪБИТИЕ  ·  25 юли 2026', 'Групата за народни танци при Н']; back to portrait: kept 100% of 9 texts
PROCDEATH article: killed yes; new pid 18743; restored: kept 100% of 9 texts; front app
## C6. Monkey 2000 (seed 1357) on API 36
## W. First launch and warm-up
first-install catalogue results after 3139 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19088
MONKEY exit=0 duration=140 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19088'; airplane=0; camera: 
MEM after-monkey (raw) PSS=74033KB  Java=12244KB  Native=34396KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=53)  RSS=191432KB  Views=19 Activities=1
MEM after-monkey (trimmed) PSS=75798KB  Java=11560KB  Native=31916KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=21)  RSS=193864KB  Views=16 Activities=1
## C6. Monkey 2000 (seed 4242) on API 36
## W. First launch and warm-up
first-install catalogue results after 2722 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=19972
MONKEY exit=0 duration=154 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '19972'; airplane=0; camera: 
MEM after-monkey (raw) PSS=76768KB  Java=15544KB  Native=36440KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=73)  RSS=191936KB  Views=29 Activities=1
MEM after-monkey (trimmed) PSS=81109KB  Java=14964KB  Native=36324KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=73)  RSS=196916KB  Views=29 Activities=1
## C6. Monkey 2000 (seed 777) on API 36
## W. First launch and warm-up
WARN: tab bar not reached
first-install: NO catalogue results
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
WARN: tab bar not reached
monkey start pid=21878
MONKEY exit=0 duration=127 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '21878'; airplane=0; camera: 
MEM after-monkey (raw) PSS=101634KB  Java=17916KB  Native=52900KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=61)  RSS=216776KB  Views=27 Activities=1
MEM after-monkey (trimmed) PSS=100558KB  Java=13792KB  Native=51008KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=61)  RSS=216280KB  Views=15 Activities=1
contact sheets: 19 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: Settings not found
APP W/E lines (pids ['4173', '5200', '10845', '15715', '18047', '18456', '18743', '19088', '19972', '21878']): 2595 in 214 groups
     639  W/Codec2Client: query -- param skipped: index = #.
     458  W/ColorUtils: expected specified color aspects (#:#:#:#)
     224  W/HWUI: Image decoding logging dropped!
     160  E/MediaCodec: Media Quality Service not found.
     160  W/CCodec: This behavior is subject to change. It is recommended that app developers double check whether the requested m
     160  E/g.chyavorec.app: Failed to query component interface for required system resources: #
      55  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_HIDE_ANIMATION::#@#@org.chyavorec.app>
      50  W/FrameTracker: Missed SF frame:JANK_COMPOSER, #, #, CUJ=J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
      30  W/RemoteInputConnectionImpl: requestCursorUpdates on inactive InputConnection
      17  W/HWUI: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      17  W/HWUI: Failed to initialize #-# format, error = EGL_SUCCESS
      16  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#b#a#
      14  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
      14  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#ea#f
      10  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      10  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, goldmont, goldmont-plus, goldmont-without-sha-xsaves, tremont, 
      10  E/g.chyavorec.app: Not starting debugger since process cannot load the jdwp agent.
      10  E/ashmem: Pinning is deprecated since Android Q. Please use trim or other methods.
      10  W/HWUI: Unknown dataspace #
      10  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#b#
      10  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       9  W/AMessage: failed to deliver message as target handler # is gone.
       8  W/WindowOnBackDispatcher: sendCancelIfRunning: isInProgress=false callback=android.view.ImeBackAnimationController@#e#
       8  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
       8  E/FrameTracker: force finish cuj, time out: J<IME_INSETS_SHOW_ANIMATION::#@#@org.chyavorec.app>
       7  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       7  E/ImeBackDispatcher: Ime callback not found. Ignoring unregisterReceivedCallback. callbackId: #
       7  W/System: A resource failed to call release. 
       6  W/InteractionJankMonitor: Initializing without READ_DEVICE_CONFIG permission. enabled=true, interval=#, missedFrameThreshold=#, frameTim
contact sheets: 19 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4173', '5200', '10845', '15715', '18047', '18456', '18743', '19088', '19972', '21878']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 252
   skipped-frames events=252 max=202 total=12986
== Davey: 709
   davey events=709 max=4480ms median=992ms
== NativeLink: 9
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 0 B
```

## dev38

```
part dev38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.38 de2e156 (org.chyavorec.app.dev, demo sign-in DEMO-0001, shared debug key);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.38-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4092 ms
## DV1. Моето as a guest and the login screen (FLAG_SECURE: emulator screenshots)
CAP dev38/my-guest: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001
## DV2. „Картата е издадена“: the sign-in tap recorded from outside (the card lifts, then flies to the hub tile)
EREC login-fly: 50 frames; frames 50; changed between consecutive frames: 1; max diff 0.9; frames=50 motion-start=#5 moving-frames=0 (span 0) kept #4..#15; diffs: 0 1 0 0 0 0 0 0 0 0 0 0
after the sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" text="Така ще изглежда картата ти" text="Вход за читатели" t
CAP dev38/my-signed-in: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | ДЕМО ДАННИ — само за разрабо · Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001
MY signed in: text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" text="Така ще изглежда картата ти" text="Вход" text="Добави читателска карта" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" tex
## DV3. Loans and renewal (demo: renew → pending, no new due date → no „Нов лист“ expected)
LOANS: text="ДЕМО ДАННИ — само за разработка, не са реални" text="Нещо се обърка" text="Сесията е изтекла. Моля, влез отново." text="Вход" content-desc="Назад" text="Моите книги" 
RENEW: no 'Удължи срока' button
## DV4. Messages
MESSAGES: entry not found in Моето: content-desc="Примерна читателска карта" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" text="Така ще изглежда картата ти" text="Вход" text="Добави читателска карта" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята 
## DV5. Diary: 5 rosettes on the first book, then the year story (favourite slide)
DIARY: no rating node
YEAR: story did not open
## DV6. The card on 10 December (the „Твоята шевица“ strip) and in fullscreen; 20 January; 14 November (no strip)
device date now: Thu Dec 10 10:00:02 UTC 2026
CARD dec: band node absent
device date now: Wed Jan 20 10:00:07 UTC 2027
CARD jan: band node absent
device date now: Sat Nov 14 14:11:35 UTC 2026
CARD nov: band node absent
## DV7. Tablet landscape: two panes in Моето signed in
TABLET after '=Заети книги|=Loans|Заети': text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил
TABLET after '=Читателска карта|=Library card': text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил
DEV38 part: FATAL(any process)=0 of the app=0 ANR=0
A11Y AGG: 2 screens analysed, 0 dumps failed; totals MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
== MISSING: 0 distinct
== SMALL: 0 distinct
== SAME-LABEL: 0 distinct
== DOUBLE: 0 distinct
contact sheets: 5 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38-dev (1) | Профил: чака | Студен старт · 4 | Първи кадър: медиана 4284 мс, последен 2157 мс | Пълно показване: медиана 4877 мс, последно 2723 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38-dev (1)" text="Профил: чака" text="Студен старт · 4" text="Първи кадър: медиана 4284 мс, последен 2157 мс" text="Пълно показване: медиана 4877 мс, последно 2723 мс" text="Няма записани грешки." content-desc="Н
APP W/E lines (pids ['5984', '8611', '9167', '9953']): 252 in 107 groups
       8  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       6  E/OpenGLRenderer: Unable to match the desired swap behavior.
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       6  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       5  W/ziparchive: Unable to open '/data/app/~~vTrd#YOchL#IY#vjL#X#KA==/org.chyavorec.app.dev-#u-#r#CMr#LPMk#e_Gg==/base.dm': No 
       4  W/yavorec.app.dev: Unexpected CPU variant for x#: x#_#.
       4  W/yavorec.app.dev: Known variants: atom, sandybridge, silvermont, kabylake, default
       4  W/Parcel: Expecting binder but got null!
       4  W/yavorec.app.dev: Accessing hidden method Landroid/os/SystemProperties;->addChangeCallback(Ljava/lang/Runnable;)V (unsupported, 
       4  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       4  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       4  W/Gralloc4: allocator #.x is not supported
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       4  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       4  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.PlatformPasswordVisibilitySettingImpl.getSystemShowPasswordSetting(BasicS
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextField_androidKt.rememberPlatformPasswordVisibilitySettings
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-ltb#GB#(BasicSecureTextField.
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-egD#TGM(BasicSecureTextField.
       2  W/BasicSecureTextField: 	at androidx.compose.runtime.CompositionLocalKt.CompositionLocalProvider(CompositionLocal.kt:#)
contact sheets: 6 in sheets/
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
CRASHES: 1 block(s) {'SIGNAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:03 app-mapping.txt; crashes: 174 B
```

## dev38b

```
part dev38b; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.38 de2e156 (org.chyavorec.app.dev, demo sign-in DEMO-0001, shared debug key);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.38-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 7466 ms
## DB1. Sign-in (fields checked), the „Картата е издадена“ tap recorded from outside (5 fps, 0–2.6 s)
LOGIN try 1: card field OK
RECWIN login-fly (emu, +0s for 2.6s at 5 fps): 13 frames; frames 13; changed between consecutive frames: 0; max diff 0.2
MY after the sign-in: NOT signed in: text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" 
LOGIN try 1: card field OK
MY after the sign-in: NOT signed in: text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" 
DEMO sign-in FAILED twice
## DB2. Loans: renew (demo → pending)
LOANS: text="ДЕМО ДАННИ — само за разработка, не са реални" text="Нещо се обърка" text="Сесията е изтекла. Моля, влез отново." text="Вход" content-desc="Назад" text="Моите книги" 
RENEW: no 'Удължи срока' button
## DB3. Messages (Още › Съобщения)
CAP dev38b/messages: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Няма съобщения от читалището · Някои съобщения са само за ч · Назад · Съобщения
MESSAGES: text="Няма съобщения от читалището" text="Някои съобщения са само за членове. Въведи читателската си карта (или влез) в „Моето“, за да ги виждаш." content-desc="Назад" text="Съобщения" 
## DB4. Diary: 5 rosettes, then the year story slide by slide (content descriptions)
YEAR: story did not open
## DB5. The card on 10 December and 20 January (the strip), 14 November (none)
device date now: Thu Dec 10 10:00:02 UTC 2026
CARD dec: band absent
device date now: Wed Jan 20 10:00:07 UTC 2027
CARD jan: band absent
device date now: Sat Nov 14 15:35:50 UTC 2026
CARD nov: band absent
## DB6. Tablet landscape (1280×800 dp): two panes in Още and Моето, signed in
TABLET Още › 'Познай Яворец|Know Yavorets': tabs visible; text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="Галерия" text="Снимки от сайта" text="Документи и архив" 
TABLET Още › 'Лента на времето|Timeline': tabs visible; text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="Галерия" text="Снимки от сайта" text="Документи и архив" 
TABLET: '=Съобщения|=Messages' not found in the left pane
TABLET Моето › '=Моите книги|=My books': tabs visible; text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" 
TABLET Моето › '=Читателска карта|=Library card': tabs visible; text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" 
TABLET Моето › '=Моето членство|=My membership': tabs visible; text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ДЕМО ДАННИ — само за разработка, не са реални" content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" 
DEV38B part: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 6 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38-dev (1) | Профил: чака | Студен старт · 5 | Първи кадър: медиана 6099 мс, последен 4012 мс | Пълно показване: медиана 6705 мс, последно 4320 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38-dev (1)" text="Профил: чака" text="Студен старт · 5" text="Първи кадър: медиана 6099 мс, последен 4012 мс" text="Пълно показване: медиана 6705 мс, последно 4320 мс" text="Няма записани грешки." content-desc="Н
APP W/E lines (pids ['5245', '9663', '10297', '10874', '11238']): 489 in 127 groups
      12  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
      12  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
      12  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
      10  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
      10  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       8  E/OpenGLRenderer: Unable to match the desired swap behavior.
       8  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       8  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       6  W/Parcel: Expecting binder but got null!
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.InnerNodeCoordinator$LookaheadDelegateImpl.measure-BRTryo#(InnerNodeCoordinator.k
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasureBlock$lambda$#(LookaheadPassDelegate.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate$$ExternalSyntheticLambda#.invoke(D#$$SyntheticClass:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasure-BRTryo#$ui(LookaheadPassDelegate.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performLookaheadMeasure-BRTryo#$ui(LayoutNodeLayoutDeleg
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.remeasure-BRTryo#(LookaheadPassDelegate.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.measure-BRTryo#(LookaheadPassDelegate.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutModifierNodeCoordinator$LookaheadDelegateForLayoutModifierNode.measure-BRTr
       6  W/BasicSecureTextField: 	at androidx.compose.ui.graphics.BlockGraphicsLayerModifier.measure-#p#s#s(GraphicsLayerModifier.kt:#)
       6  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       6  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       5  W/yavorec.app.dev: Unexpected CPU variant for x#: x#_#.
       5  W/yavorec.app.dev: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/ziparchive: Unable to open '/data/app/~~cRZMBfN_#RZ#BQ#_mhImuw==/org.chyavorec.app.dev-qmFuALLyNjdX#pn#jzM#Qg==/base.dm': 
       5  W/yavorec.app.dev: Accessing hidden method Landroid/os/SystemProperties;->addChangeCallback(Ljava/lang/Runnable;)V (unsupported, 
       5  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       5  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       5  W/Gralloc4: allocator #.x is not supported
       4  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       4  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
contact sheets: 7 in sheets/
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
CRASHES: 1 block(s) {'SIGNAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642132 Nov 14 15:25 app-mapping.txt; crashes: 174 B
```

## dm28

```
part dm28; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: 
camera permission: 
screen 1080x2400
## DM0. Files and the local HTTPS server (runner 10.0.2.2:8443, test CA in the system store)
-rw-r--r-- 1 runner runner 4640582 Nov 14 14:04 app-new-upd-local.apk
-rw-r--r-- 1 runner runner 4640810 Nov 14 14:04 app.apk

dm/:
total 52
-rw-r--r-- 1 runner runner 21550 Nov 14 14:04 app.v0_1_0_p.dm
-rw-r--r-- 1 runner runner 21188 Nov 14 14:04 app.v0_1_5_s.dm
-rw-r--r-- 1 runner runner   409 Nov 14 14:04 dm.json
[{"format":"v0_1_0_p","minSdk":28,"maxSdk":30,"file":"app.v0_1_0_p.dm","sha256":"155d23acd08de5ac5171fc750bd6e3e87b03b35afe62538a354412ab1e971ebb","size":21550},{"format":"v0_1_5_s","minSdk":31,"maxSdk":null,"file":"app.v0_1_5_s.dm","sha256":"eb62490b8422f97bc9e9061cd26f722f2144707fc9638d0c0e93a3045403b752","size":21188}][{"format":"v0_1_0_p","minSdk":28,"maxSdk":30,"file":"app-base.v0_1_0_p.dm","sha256":"e65bd590bb8291e097ca897543ccb5b652c39353d08608ebd4526008a05ba17b","size":21125},{"format":"v0_1_5_s","minSdk":31,"maxSdk":null,"file":"app-base.v0_1_5_s.dm","sha256":"5e6f4bcffe1116e433caabd9

updater build: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server)
test CA in the system store: -rw-r--r-- 1 root root 1159 2026-11-14 14:07 /system/etc/security/cacerts/e84129e0.0; certs: 138
## DM1. Control: pm install session with base.apk + base.dm (no app code), then the same without .dm
CONTROL with-dm (app.v0_1_0_p.dm): session 483557777 → Success;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
CONTROL with-dm dexopt:  path: /data/app/org.chyavorec.app-vqa9TCRt3GEWsDf4H7DByA==/base.apk| x86_64: [status=speed-profile] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=quicken] [reason=prebuilt]|
CONTROL with-dm files: /data/misc/profiles/cur/0/org.chyavorec.app/:;0 2026-11-14 14:07 primary.prof;/data/misc/profiles/ref/org.chyavorec.app/:;20086 2026-11-14 14:07 primary.prof;4640810 2026-11-14 14:07 base.apk;21550 2026-11-14 14:07 base.dm;4096 2026-11-14 14:07 lib;4096 2026-11-14 14:07 oat;
CONTROL with-dm installd/PM: 11-14 14:07:39.603  5536  5536 W dex2oat : Unexpected CPU variant for X86 using defaults: x86_64|11-14 14:07:39.607  5536  5536 I dex2oat : No primary.vdex file in DexMetadata archive. Not doing fast verification.|11-14 14:07:39.607  5536  5536 I dex2oat : /system/bin/dex2oat --input-vdex-fd=-1 --output-vdex-fd=10 --compiler-filter=speed-profile --profile-file-fd=13 --classpath-dir=/data/app/org.chyavorec.app-vqa9TCRt3GEWsDf4H7DBy|11-14 14:07:40.340  5536  5536 W dex2oat : Accessing hidden method Landroid/graphics/Canvas;->drawPatch(Landroid/graphics/NinePatch;Landroid/graphics/Rect;Landroid/graphics/Paint;)V (dark greylist, linking)|
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
CONTROL with-dm first cold starts: TotalTime: 1059 Fully drawn org.chyavorec.app/.MainActivity: +1s78ms TotalTime: 863 Fully drawn org.chyavorec.app/.MainActivity: +889ms TotalTime: 857 Fully drawn org.chyavorec.app/.MainActivity: +902ms 
CONTROL without-dm (app.v0_1_0_p.dm): session 1008825706 → Success;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
CONTROL without-dm dexopt:  path: /data/app/org.chyavorec.app-xxSZptbDUrtj5RopNUa8Vw==/base.apk| x86_64: [status=speed-profile] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=quicken] [reason=prebuilt]|
CONTROL without-dm files: /data/misc/profiles/cur/0/org.chyavorec.app/:;0 2026-11-14 14:08 primary.prof;/data/misc/profiles/ref/org.chyavorec.app/:;4640810 2026-11-14 14:08 base.apk;4096 2026-11-14 14:08 lib;4096 2026-11-14 14:08 oat;
CONTROL without-dm installd/PM: 11-14 14:08:20.819  6094  6094 W dex2oat : Unexpected CPU variant for X86 using defaults: x86_64|11-14 14:08:20.819  6094  6094 I dex2oat : /system/bin/dex2oat --input-vdex-fd=-1 --output-vdex-fd=10 --compiler-filter=speed-profile --classpath-dir=/data/app/org.chyavorec.app-xxSZptbDUrtj5RopNUa8Vw== --class-loader-co|11-14 14:08:21.345  6094  6096 W dex2oat : Accessing hidden method Landroid/graphics/Canvas;->drawPatch(Landroid/graphics/NinePatch;Landroid/graphics/Rect;Landroid/graphics/Paint;)V (dark greylist, linking)|11-14 14:08:21.345  6094  6096 W dex2oat : Accessing hidden method Landroid/graphics/Canvas;->drawPatch(Landroid/graphics/NinePatch;Landroid/graphics/RectF;Landroid/graphics/Paint;)V (dark greylist, linking)|
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
CONTROL without-dm first cold starts: TotalTime: 1080 Fully drawn org.chyavorec.app/.MainActivity: +1s389ms TotalTime: 1122 Fully drawn org.chyavorec.app/.MainActivity: +1s316ms TotalTime: 1091 Fully drawn org.chyavorec.app/.MainActivity: +1s414ms 
## DM-good. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 28:30:dm/app.v0_1_0_p.dm 31::dm/app.v0_1_5_s.dm
manifest: vc 1510100 app.apk 4640810 B; dm: 28-30 app.v0_1_0_p.dm 21550 B sha 155d23acd0, 31-None app.v0_1_5_s.dm 21188 B sha eb62490b84
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM good: dialog dialog after 10130 ms: text="New version 1.2.38" text="The new version has been downloaded and verified (checksum and signature)." text="What’s new" text="• Test update (emulator)" text="Later" text="Install" ; files: 4640810 2026-11-14 14:09 update-1510100.apk;21550 2026-11-14 14:09 update-1510100.dm;
INSTALLER: text="Chitalishte Yavorets" text="Do you want to install an update to this existing application? Your existing data will not be lost. It does not require any special access." text="CANCEL" text="INSTA
DM good RESULT: INSTALLED in 8 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM good dexopt:  path: /data/app/org.chyavorec.app-4L316Xcz88daEQDmmfpVFg==/base.apk| x86_64: [status=speed-profile] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=quicken] [reason=prebuilt]|
DM good files: /data/misc/profiles/cur/0/org.chyavorec.app/:;0 2026-11-14 14:09 primary.prof;/data/misc/profiles/ref/org.chyavorec.app/:;20086 2026-11-14 14:09 primary.prof;4640810 2026-11-14 14:09 base.apk;21550 2026-11-14 14:09 base.dm;4096 2026-11-14 14:09 lib;4096 2026-11-14 14:09 oat;
DM good server: GET /update.json Range=None If-Range=None -> 200 sent 722/722 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.4 s (rate 0 B/s)~GET /app.v0_1_0_p.dm Range=None If-Range=None -> 200 START (21550 B)~GET /app.v0_1_0_p.dm Range=None If-Range=None -> 200 sent 21550/21550 B in 0.0 s (rate 0 B/s)~
DM good PackageManager/installd: 11-14 14:09:12.965  1985  2590 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x10000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstal|11-14 14:09:13.264  1985  2003 I ActivityManager: Start proc 6680:com.google.android.packageinstaller/u0a28 for activity com.google.android.packageinstaller/com.android.packageinstaller.InstallStart|11-14 14:09:13.573  1985  2774 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x2000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstall|11-14 14:09:14.800  1985  2009 I ActivityManager: Displayed com.google.android.packageinstaller/com.android.packageinstaller.PackageInstallerActivity: +1s126ms (total +1s512ms)|11-14 14:09:14.847  1936  2409 W SurfaceFlinger: Attempting to set client state on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:09:14.847  1936  2409 W SurfaceFlinger: Attempting to destroy on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:09:16.157  6727  6727 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@9dc7; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packag|11-14 14:09:17.579  6751  6751 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.access
DM good app diagnostics (update.*): 
DM good: FATAL(any process)=0 of the app=0 ANR=0
DM good first cold starts after the update: TotalTime: 1011 Fully drawn org.chyavorec.app/.MainActivity: +1s108ms TotalTime: 1033 Fully drawn org.chyavorec.app/.MainActivity: +1s99ms TotalTime: 844 Fully drawn org.chyavorec.app/.MainActivity: +944ms 
## DM-badsha. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 28:30:dm/app.v0_1_0_p.dm:0000000000000000000000000000000000000000000000000000000000000000 31::dm/app.v0_1_5_s.dm:0000000000000000000000000000000000000000000000000000000000000000
manifest: vc 1510100 app.apk 4640810 B; dm: 28-30 app.v0_1_0_p.dm 21550 B sha 0000000000, 31-None app.v0_1_5_s.dm 21188 B sha 0000000000
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM badsha: dialog dialog after 9696 ms: text="New version 1.2.38" text="The new version has been downloaded and verified (checksum and signature)." text="What’s new" text="• Test update (emulator)" text="Later" text="Install" ; files: 4640810 2026-11-14 14:10 update-1510100.apk;
INSTALLER: text="Chitalishte Yavorets" text="Do you want to install an update to this existing application? Your existing data will not be lost. It does not require any special access." text="CANCEL" text="INSTA
DM badsha RESULT: INSTALLED in 7 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM badsha dexopt:  path: /data/app/org.chyavorec.app-HuqpXqQnEwmm-F7-k8NTCA==/base.apk| x86_64: [status=speed-profile] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=quicken] [reason=prebuilt]|
DM badsha files: /data/misc/profiles/cur/0/org.chyavorec.app/:;0 2026-11-14 14:10 primary.prof;/data/misc/profiles/ref/org.chyavorec.app/:;4640810 2026-11-14 14:10 base.apk;4096 2026-11-14 14:10 lib;4096 2026-11-14 14:10 oat;
DM badsha server: GET /update.json Range=None If-Range=None -> 200 sent 722/722 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.3 s (rate 0 B/s)~GET /app.v0_1_0_p.dm Range=None If-Range=None -> 200 START (21550 B)~GET /app.v0_1_0_p.dm Range=None If-Range=None -> 200 sent 21550/21550 B in 0.0 s (rate 0 B/s)~
DM badsha PackageManager/installd: 11-14 14:10:08.923  1985  2352 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x10000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstal|11-14 14:10:09.137  1985  4661 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x2000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstall|11-14 14:10:09.917  1936  1940 W SurfaceFlinger: Attempting to set client state on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:10:09.917  1936  1940 W SurfaceFlinger: Attempting to destroy on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:10:09.918  1985  2009 I ActivityManager: Displayed com.google.android.packageinstaller/com.android.packageinstaller.PackageInstallerActivity: +631ms (total +726ms)|11-14 14:10:11.694  7417  7417 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@d283; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packag|11-14 14:10:13.103  7441  7441 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@d28a; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packag|11-14 14:10:13.626  1985  2014 I PackageManager.DexOptimizer: Running dexopt 
DM badsha app diagnostics (update.*): 
DM badsha: FATAL(any process)=0 of the app=0 ANR=0
DM badsha first cold starts after the update: TotalTime: 1112 Fully drawn org.chyavorec.app/.MainActivity: +1s388ms TotalTime: 1104 Fully drawn org.chyavorec.app/.MainActivity: +1s420ms TotalTime: 1163 Fully drawn org.chyavorec.app/.MainActivity: +1s404ms 
## DM-junk. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 28:30:/home/runner/upc-srv/junk.v0_1_0_p.dm 31::/home/runner/upc-srv/junk.v0_1_5_s.dm
manifest: vc 1510100 app.apk 4640810 B; dm: 28-30 junk.v0_1_0_p.dm 24000 B sha 619672dcb5, 31-None junk.v0_1_5_s.dm 24000 B sha 619672dcb5
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM junk: dialog dialog after 10069 ms: text="New version 1.2.38" text="The new version has been downloaded and verified (checksum and signature)." text="What’s new" text="• Test update (emulator)" text="Later" text="Install" ; files: 4640810 2026-11-14 14:11 update-1510100.apk;24000 2026-11-14 14:11 update-1510100.dm;
INSTALLER: text="Chitalishte Yavorets" text="Do you want to install an update to this existing application? Your existing data will not be lost. It does not require any special access." text="CANCEL" text="INSTA
INSTALLER: text="Chitalishte Yavorets" text="Do you want to install an update to this existing application? Your existing data will not be lost. It does not require any special access." text="CANCEL" text="INSTA
DM junk RESULT: INSTALLED in 15 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM junk dexopt:  path: /data/app/org.chyavorec.app-sTPRSh0jUSJBQ-nEe4YM-Q==/base.apk| x86_64: [status=speed-profile] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=quicken] [reason=prebuilt]|
DM junk files: /data/misc/profiles/cur/0/org.chyavorec.app/:;0 2026-11-14 14:11 primary.prof;/data/misc/profiles/ref/org.chyavorec.app/:;4640810 2026-11-14 14:11 base.apk;4096 2026-11-14 14:11 lib;4096 2026-11-14 14:11 oat;
DM junk server: GET /update.json Range=None If-Range=None -> 200 sent 724/724 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.3 s (rate 0 B/s)~GET /junk.v0_1_0_p.dm Range=None If-Range=None -> 200 START (24000 B)~GET /junk.v0_1_0_p.dm Range=None If-Range=None -> 200 sent 24000/24000 B in 0.0 s (rate 0 B/s)~
DM junk PackageManager/installd: 11-14 14:11:05.775  1985  2481 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x10000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstal|11-14 14:11:06.107  1985  4661 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x2000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstall|11-14 14:11:06.807  1985  2009 I ActivityManager: Displayed com.google.android.packageinstaller/com.android.packageinstaller.PackageInstallerActivity: +599ms (total +723ms)|11-14 14:11:08.635  8084  8084 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@10741; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packa|11-14 14:11:10.053  8108  8108 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@10748; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packa|11-14 14:11:10.321  1985  2014 D ziparchive: Zip: EOCD not found, /data/app/vmdl1566216490.tmp/base.dm is not zip|11-14 14:11:10.334  1985  2014 W PackageManager: android.content.pm.PackageParser$PackageParserException: Error opening /data/app/vmdl1566216490.tmp/base.dm|11-14 14:11:10.334  1985  2014 W PackageManager: 	at android.content.pm.dex.DexMetadataHelper.valid
DM junk app diagnostics (update.*): 
DM junk: FATAL(any process)=0 of the app=0 ANR=0
DM junk first cold starts after the update: TotalTime: 1197 Fully drawn org.chyavorec.app/.MainActivity: +1s427ms TotalTime: 1231 Fully drawn org.chyavorec.app/.MainActivity: +1s522ms TotalTime: 1107 Fully drawn org.chyavorec.app/.MainActivity: +1s359ms 
## DM-old137. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 28:30:dm-base/app-base.v0_1_0_p.dm
manifest: vc 1510100 app.apk 4640810 B; dm: 28-30 app-base.v0_1_0_p.dm 21125 B sha e65bd590bb
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM old137: dialog dialog after 10051 ms: text="New version 1.2.38" text="The new version has been downloaded and verified (checksum and signature)." text="What’s new" text="• Test update (emulator)" text="Later" text="Install" ; files: 4640810 2026-11-14 14:12 update-1510100.apk;21125 2026-11-14 14:12 update-1510100.dm;
INSTALLER: text="Chitalishte Yavorets" text="Do you want to install an update to this existing application? Your existing data will not be lost. It does not require any special access." text="CANCEL" text="INSTA
DM old137 RESULT: INSTALLED in 7 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM old137 dexopt:  path: /data/app/org.chyavorec.app-Cj-w0OswaWfV9KGzvcsISg==/base.apk| x86_64: [status=speed-profile] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=quicken] [reason=prebuilt]|
DM old137 files: /data/misc/profiles/cur/0/org.chyavorec.app/:;0 2026-11-14 14:12 primary.prof;/data/misc/profiles/ref/org.chyavorec.app/:;19685 2026-11-14 14:12 primary.prof;4640810 2026-11-14 14:12 base.apk;21125 2026-11-14 14:12 base.dm;4096 2026-11-14 14:12 lib;4096 2026-11-14 14:12 oat;
DM old137 server: GET /update.json Range=None If-Range=None -> 200 sent 533/533 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.2 s (rate 0 B/s)~GET /app-base.v0_1_0_p.dm Range=None If-Range=None -> 200 START (21125 B)~GET /app-base.v0_1_0_p.dm Range=None If-Range=None -> 200 sent 21125/21125 B in 0.0 s (rate 0 B/s)~
DM old137 PackageManager/installd: 11-14 14:12:10.958  1985  2773 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x10000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstal|11-14 14:12:11.053  1985  2589 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x2000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstall|11-14 14:12:12.180  1985  2009 I ActivityManager: Displayed com.google.android.packageinstaller/com.android.packageinstaller.PackageInstallerActivity: +739ms (total +1s101ms)|11-14 14:12:12.345  1936  2411 W SurfaceFlinger: Attempting to set client state on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:12:12.345  1936  2411 W SurfaceFlinger: Attempting to destroy on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:12:13.743  8863  8863 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@170a1; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packa|11-14 14:12:15.152  8887  8887 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@170a6; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packa|11-14 14:12:16.481  1936  2411 W SurfaceFlinger: Attempting to destroy on r
DM old137 app diagnostics (update.*): 
DM old137: FATAL(any process)=0 of the app=0 ANR=0
DM old137 first cold starts after the update: TotalTime: 1234 Fully drawn org.chyavorec.app/.MainActivity: +1s419ms TotalTime: 1241 Fully drawn org.chyavorec.app/.MainActivity: +1s525ms TotalTime: 1389 Fully drawn org.chyavorec.app/.MainActivity: +1s561ms 
## DM-none. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: none
manifest: vc 1510100 app.apk 4640810 B; dm: 
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM none: dialog dialog after 10216 ms: text="New version 1.2.38" text="The new version has been downloaded and verified (checksum and signature)." text="What’s new" text="• Test update (emulator)" text="Later" text="Install" ; files: 4640810 2026-11-14 14:13 update-1510100.apk;
INSTALLER: text="Chitalishte Yavorets" text="Do you want to install an update to this existing application? Your existing data will not be lost. It does not require any special access." text="CANCEL" text="INSTA
DM none RESULT: INSTALLED in 7 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM none dexopt:  path: /data/app/org.chyavorec.app-J5PmGi8vyBwlHn4NnGrEFw==/base.apk| x86_64: [status=speed-profile] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=quicken] [reason=prebuilt]|
DM none files: /data/misc/profiles/cur/0/org.chyavorec.app/:;0 2026-11-14 14:13 primary.prof;/data/misc/profiles/ref/org.chyavorec.app/:;4640810 2026-11-14 14:13 base.apk;4096 2026-11-14 14:13 lib;4096 2026-11-14 14:13 oat;
DM none server: GET /update.json Range=None If-Range=None -> 200 sent 324/324 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.2 s (rate 0 B/s)~
DM none PackageManager/installd: 11-14 14:13:08.094  1985  4370 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x10000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstal|11-14 14:13:08.484  1985  3572 I ActivityManager: START u0 {act=android.content.pm.action.CONFIRM_PERMISSIONS flg=0x2000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstall|11-14 14:13:09.438  1936  2056 W SurfaceFlinger: Attempting to set client state on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:13:09.438  1936  2056 W SurfaceFlinger: Attempting to destroy on removed layer: Splash Screen com.google.android.packageinstaller#0|11-14 14:13:09.461  1985  2009 I ActivityManager: Displayed com.google.android.packageinstaller/com.android.packageinstaller.PackageInstallerActivity: +673ms (total +919ms)|11-14 14:13:11.020  9530  9530 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@1a55b; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packa|11-14 14:13:12.429  9557  9557 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@1a568; boundsInParent: Rect(0, 0 - 631, 0); boundsInScreen: Rect(21, 2111 - 652, 2111); packa|11-14 14:13:12.886  1985  2014 I PackageManager.DexOptimizer: Running dexopt (d
DM none app diagnostics (update.*): 
DM none: FATAL(any process)=0 of the app=0 ANR=0
DM none first cold starts after the update: TotalTime: 967 Fully drawn org.chyavorec.app/.MainActivity: +1s196ms TotalTime: 1387 Fully drawn org.chyavorec.app/.MainActivity: +1s644ms TotalTime: 1113 Fully drawn org.chyavorec.app/.MainActivity: +1s400ms 
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="No errors recorded."  Performance | Start-up times and screen smoothness measured on this device — screen names only, no personal data. | Version 1.2.38 (1510100) | Profile: waiting | First start after the update | First frame 984 ms, fully drawn 1272 ms | Cold start · 3 | First frame: median 1084 ms, last 1084 ms | Fully drawn: median 1373 ms, last 1373 ms | Earlier · Version 1.2.38 (1510099) | Cold start · 1 | First 
DIAG SCREEN (scrolled): text="The app records technical errors here (time, place in the code and type of error) so they can be found and fixed. No personal data: numbers, PINs, names and addresses are removed. The log stays on this device only — it leaves it only if you choose to share it." text="Share" text="Clear" text="Performance" text="Start-up times and screen smoothness measured on this device — screen names only, no personal data." text="Version 1.2.38 (1510100)" text="Profile: waiting" text="First start after the update" text="First frame 984 ms, fully drawn 1272 ms" text="Cold start · 3" text="First frame: median 1084 ms, last 1084 ms" text="Fully drawn: median 1373 ms, last 1373 ms" text="Earlier · Versi
APP W/E lines (pids ['5585', '5771', '5923', '5985', '6130', '6286', '6333', '6392', '6541', '6882', '6980', '7036', '7094', '7247', '7486', '7659', '7711', '7767', '7920', '8211', '8387', '8449', '8499', '8655', '9035', '9117', '9167', '9219', '9367', '9690', '9776', '9827', '9886']): 757 in 36 groups
      60  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->getScaleX()F (dark greylist, linking)
      41  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setPivotX(F)Z (dark greylist, linking)
      38  E/eglCodecCommon: glUtilsParamSize: unknow param #
      35  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setClipToOutline(Z)Z (dark greylist, linking)
      34  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setLayerType(I)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
      33  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      33  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setScaleY(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->getMatrix(Landroid/graphics/Matrix;)V (dark greylist, linki
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setCameraDistance(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setRotationX(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setRotationY(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setAlpha(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setLayerPaint(Landroid/graphics/Paint;)Z (dark greylist, li
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setElevation(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setRotation(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setTranslationY(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setOutline(Landroid/graphics/Outline;)Z (dark greylist, lin
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setScaleX(F)Z (dark greylist, linking)
      33  W/g.chyavorec.ap: Accessing hidden method Landroid/view/RenderNode;->setTranslationX(F)Z (dark greylist, linking)
       5  W/PackageParser: Unknown element under <manifest>: queries at /data/user/#/org.chyavorec.app/no_backup/updates/update-#.apk.par
       2  W/g.chyavorec.ap: Suspending all threads took: #.#ms
       2  W/ActivityThread: handleWindowVisibility: no activity for token android.os.BinderProxy@#d#
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=c#e#-#e#-#-b#e#-#a#e#a#f, generation=#)
       1  W/g.chyavorec.ap: Long monitor contention with owner DefaultDispatcher-worker-# (#) at android.content.SharedPreferences android
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#dd#c-#bd#-#d#a-a#-#fde, generation=#)
       1  W/g.chyavorec.ap: Verification of tj# f#.f(javax.net.ssl.SSLException) took #.#ms
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=c#eb#f#-#e#-#bc-a#ba-fa#f#e#, generation=#)
contact sheets: 3 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5585', '5771', '5923', '5985', '6130', '6286', '6333', '6392', '6541', '6882', '6980', '7036', '7094', '7247', '7486', '7659', '7711', '7767', '7920', '8211', '8387', '8449', '8499', '8655', '9035', '9117', '9167', '9219', '9367', '9690', '9776', '9827', '9886']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 10
   skipped-frames events=10 max=44 total=362
== Davey: 20
   davey events=20 max=1171ms median=786ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 0 B
```

## dm33

```
part dm33; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## DM0. Files and the local HTTPS server (runner 10.0.2.2:8443, test CA in the system store)
-rw-r--r-- 1 runner runner 4640582 Nov 14 14:03 app-new-upd-local.apk
-rw-r--r-- 1 runner runner 4640810 Nov 14 14:03 app.apk

dm/:
total 52
-rw-r--r-- 1 runner runner 21550 Nov 14 14:03 app.v0_1_0_p.dm
-rw-r--r-- 1 runner runner 21188 Nov 14 14:03 app.v0_1_5_s.dm
-rw-r--r-- 1 runner runner   409 Nov 14 14:03 dm.json
[{"format":"v0_1_0_p","minSdk":28,"maxSdk":30,"file":"app.v0_1_0_p.dm","sha256":"155d23acd08de5ac5171fc750bd6e3e87b03b35afe62538a354412ab1e971ebb","size":21550},{"format":"v0_1_5_s","minSdk":31,"maxSdk":null,"file":"app.v0_1_5_s.dm","sha256":"eb62490b8422f97bc9e9061cd26f722f2144707fc9638d0c0e93a3045403b752","size":21188}][{"format":"v0_1_0_p","minSdk":28,"maxSdk":30,"file":"app-base.v0_1_0_p.dm","sha256":"e65bd590bb8291e097ca897543ccb5b652c39353d08608ebd4526008a05ba17b","size":21125},{"format":"v0_1_5_s","minSdk":31,"maxSdk":null,"file":"app-base.v0_1_5_s.dm","sha256":"5e6f4bcffe1116e433caabd9

updater build: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server)
test CA in the system store: -rw-r--r-- 1 root root 1159 2026-11-14 14:07 /system/etc/security/cacerts/e84129e0.0; certs: 126
## DM1. Control: pm install session with base.apk + base.dm (no app code), then the same without .dm
CONTROL with-dm (app.v0_1_5_s.dm): session 1933175819 → Success;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
CONTROL with-dm dexopt:  path: /data/app/~~uG5aJjC1RdIPB4n1yDJ5Yw==/org.chyavorec.app-d2KE1IKDmhcJdTX5cOoeiA==/base.apk| x86_64: [status=speed-profile] [reason=install-dm]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
CONTROL with-dm files: /data/misc/profiles/cur/0/org.chyavorec.app:;0 2026-11-14 14:07 primary.prof;/data/misc/profiles/ref/org.chyavorec.app:;19549 2026-11-14 14:07 primary.prof;4640810 2026-11-14 14:07 base.apk;21188 2026-11-14 14:07 base.dm;4096 2026-11-14 14:07 lib;4096 2026-11-14 14:07 oat;
CONTROL with-dm installd/PM: 11-14 14:07:57.022   485   554 I system_server: No primary.vdex file in DexMetadata archive. Not doing fast verification.|11-14 14:07:57.023   421   585 V installd: Running /apex/com.android.art/bin/dex2oat64 in=base.apk out=/data/app/~~uG5aJjC1RdIPB4n1yDJ5Yw==/org.chyavorec.app-d2KE1IKDmhcJdTX5cOoeiA==/oat/x86_64/base.odex|11-14 14:07:57.083  5554  5554 W dex2oat64: Unexpected CPU variant for x86: x86_64.|11-14 14:07:57.083  5554  5554 W dex2oat64: Known variants: atom, sandybridge, silvermont, kabylake, default|
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CONTROL with-dm first cold starts: TotalTime: 2032 Fully drawn org.chyavorec.app/.MainActivity: +2s202ms TotalTime: 2174 Fully drawn org.chyavorec.app/.MainActivity: +2s434ms TotalTime: 1548 Fully drawn org.chyavorec.app/.MainActivity: +1s732ms 
CONTROL without-dm (app.v0_1_5_s.dm): session 847644466 → Success;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
CONTROL without-dm dexopt:  path: /data/app/~~zzMZvVXOEgpXIcigDgH4PQ==/org.chyavorec.app-QCYc412LJIVbRef-G3sHKA==/base.apk| x86_64: [status=verify] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
CONTROL without-dm files: /data/misc/profiles/cur/0/org.chyavorec.app:;0 2026-11-14 14:08 primary.prof;/data/misc/profiles/ref/org.chyavorec.app:;4640810 2026-11-14 14:08 base.apk;4096 2026-11-14 14:08 lib;4096 2026-11-14 14:08 oat;
CONTROL without-dm installd/PM: 11-14 14:08:51.687   421   585 V installd: Running /apex/com.android.art/bin/dex2oat64 in=base.apk out=/data/app/~~zzMZvVXOEgpXIcigDgH4PQ==/org.chyavorec.app-QCYc412LJIVbRef-G3sHKA==/oat/x86_64/base.odex|11-14 14:08:51.745  7357  7357 W dex2oat64: Unexpected CPU variant for x86: x86_64.|11-14 14:08:51.745  7357  7357 W dex2oat64: Known variants: atom, sandybridge, silvermont, kabylake, default|11-14 14:08:51.746  7357  7357 W dex2oat64: Mismatch between dex2oat instruction set features to use (ISA: X86_64 Feature string: -ssse3,-sse4.1,-sse4.2,-avx,-avx2,-popcnt) and those from CPP defines (ISA: X86_64 Feature|
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CONTROL without-dm first cold starts: TotalTime: 1856 Fully drawn org.chyavorec.app/.MainActivity: +2s229ms TotalTime: 1440 Fully drawn org.chyavorec.app/.MainActivity: +1s656ms TotalTime: 1300 Fully drawn org.chyavorec.app/.MainActivity: +1s512ms 
## DM-good. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 28:30:dm/app.v0_1_0_p.dm 31::dm/app.v0_1_5_s.dm
manifest: vc 1510100 app.apk 4640810 B; dm: 28-30 app.v0_1_0_p.dm 21550 B sha 155d23acd0, 31-None app.v0_1_5_s.dm 21188 B sha eb62490b84
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM good: dialog dialog after 12824 ms: text="Нова версия 1.2.38" text="Новата версия е изтеглена и проверена (контролна сума и подпис)." text="Какво ново" text="• Тестово обновяване (емулатор)" text="По-късно" text="Инсталирай" ; files: 4640810 2026-11-14 14:09 update-1510100.apk;21188 2026-11-14 14:09 update-1510100.dm;
DM good RESULT: INSTALLED in 4 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM good dexopt:  path: /data/app/~~J3a41bPvk0q30zKHLnILBQ==/org.chyavorec.app-MDX7346ub9anj7TNIun_ew==/base.apk| x86_64: [status=speed-profile] [reason=install-dm]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
DM good files: /data/misc/profiles/cur/0/org.chyavorec.app:;0 2026-11-14 14:09 primary.prof;/data/misc/profiles/ref/org.chyavorec.app:;19549 2026-11-14 14:09 primary.prof;4640810 2026-11-14 14:09 base.apk;21188 2026-11-14 14:09 base.dm;4096 2026-11-14 14:09 lib;4096 2026-11-14 14:09 oat;
DM good server: GET /update.json Range=None If-Range=None -> 200 sent 722/722 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.1 s (rate 0 B/s)~GET /app.v0_1_5_s.dm Range=None If-Range=None -> 200 START (21188 B)~GET /app.v0_1_5_s.dm Range=None If-Range=None -> 200 sent 21188/21188 B in 0.0 s (rate 0 B/s)~
DM good PackageManager/installd: 11-14 14:09:53.593   485   554 I system_server: No primary.vdex file in DexMetadata archive. Not doing fast verification.|11-14 14:09:53.594   485   554 I PackageDexOptimizer: Running dexopt (dexoptNeeded=1) on: /data/app/~~J3a41bPvk0q30zKHLnILBQ==/org.chyavorec.app-MDX7346ub9anj7TNIun_ew==/base.apk pkg=org.chyavorec.app isa=x86_64 dexoptFlags=boot_c|11-14 14:09:53.661  8308  8308 W dex2oat64: /apex/com.android.art/bin/dex2oat64 --zip-fd=8 --zip-location=base.apk --oat-fd=9 --oat-location=/data/app/~~J3a41bPvk0q30zKHLnILBQ==/org.chyavorec.app-MDX7346ub9anj7TNIun_ew==/oat/x86_6|11-14 14:09:53.665  8308  8308 I dex2oat64: No primary.vdex file in DexMetadata archive. Not doing fast verification.|11-14 14:09:53.665  8308  8308 I dex2oat64: /apex/com.android.art/bin/dex2oat64 --input-vdex-fd=-1 --output-vdex-fd=10 --dm-fd=14 --profile-file-fd=12 --classpath-dir=/data/app/~~J3a41bPvk0q30zKHLnILBQ==/org.chyavorec.app-MDX7346u|11-14 14:09:56.315   485   554 D PackageInstallerSession: Marking session 357494098 as applied|11-14 14:09:56.432   485   531 W BroadcastQueue: Background execution not allowed: receiving Intent { act=android.intent.action.PACKAGE_ADDED dat=package: flg=0x4000010 (has extras) } to com.google.android.packageinstaller/com.and|11-14 14:10:00.012   485  1714 W ziparchive: Unable to open '/product/overlay/ManagedProvisioning__auto_generated_rro_product.dm': No such file or directory|
DM good app diagnostics (update.*): 
DM good: FATAL(any process)=0 of the app=0 ANR=0
DM good first cold starts after the update: TotalTime: 1361 Fully drawn org.chyavorec.app/.MainActivity: +1s414ms TotalTime: 1423 Fully drawn org.chyavorec.app/.MainActivity: +1s438ms TotalTime: 1285 Fully drawn org.chyavorec.app/.MainActivity: +1s469ms 
## DM-badsha. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 28:30:dm/app.v0_1_0_p.dm:0000000000000000000000000000000000000000000000000000000000000000 31::dm/app.v0_1_5_s.dm:0000000000000000000000000000000000000000000000000000000000000000
manifest: vc 1510100 app.apk 4640810 B; dm: 28-30 app.v0_1_0_p.dm 21550 B sha 0000000000, 31-None app.v0_1_5_s.dm 21188 B sha 0000000000
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM badsha: dialog dialog after 12415 ms: text="Нова версия 1.2.38" text="Новата версия е изтеглена и проверена (контролна сума и подпис)." text="Какво ново" text="• Тестово обновяване (емулатор)" text="По-късно" text="Инсталирай" ; files: 4640810 2026-11-14 14:10 update-1510100.apk;
DM badsha RESULT: INSTALLED in 4 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM badsha dexopt:  path: /data/app/~~F51YLanrOOQMI6J_rexrOw==/org.chyavorec.app-ku7EgHFeXuq4kT6wdA4W8A==/base.apk| x86_64: [status=verify] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
DM badsha files: /data/misc/profiles/cur/0/org.chyavorec.app:;19549 2026-11-14 14:10 primary.prof;/data/misc/profiles/ref/org.chyavorec.app:;4640810 2026-11-14 14:10 base.apk;4096 2026-11-14 14:10 lib;4096 2026-11-14 14:10 oat;
DM badsha server: GET /update.json Range=None If-Range=None -> 200 sent 722/722 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.0 s (rate 0 B/s)~GET /app.v0_1_5_s.dm Range=None If-Range=None -> 200 START (21188 B)~GET /app.v0_1_5_s.dm Range=None If-Range=None -> 200 sent 21188/21188 B in 0.0 s (rate 0 B/s)~
DM badsha PackageManager/installd: 11-14 14:10:50.167   485   839 W system_server: Long monitor contention with owner android.io (522) at java.lang.Boolean com.android.server.pm.PackageInstallerService.lambda$new$0()(PackageInstallerService.java:250) waiters=1 in a|11-14 14:10:50.445   485   554 W ziparchive: Unable to open '/data/app/~~F51YLanrOOQMI6J_rexrOw==/org.chyavorec.app-ku7EgHFeXuq4kT6wdA4W8A==/base.dm': No such file or directory|11-14 14:10:50.450   485   554 I PackageDexOptimizer: Running dexopt (dexoptNeeded=1) on: /data/app/~~F51YLanrOOQMI6J_rexrOw==/org.chyavorec.app-ku7EgHFeXuq4kT6wdA4W8A==/base.apk pkg=org.chyavorec.app isa=x86_64 dexoptFlags=boot_c|11-14 14:10:51.332   485   554 D PackageInstallerSession: Marking session 1865349627 as applied|11-14 14:10:51.420   485   531 W BroadcastQueue: Background execution not allowed: receiving Intent { act=android.intent.action.PACKAGE_ADDED dat=package: flg=0x4000010 (has extras) } to com.google.android.packageinstaller/com.and|11-14 14:10:57.258   485  1711 W ziparchive: Unable to open '/product/overlay/ManagedProvisioning__auto_generated_rro_product.dm': No such file or directory|11-14 14:10:57.259   485  1711 W ziparchive: Unable to open '/product/overlay/ManagedProvisioning__auto_generated_rro_product.dm': No such file or directory|11-14 14:10:57.259   485  1711 W ziparchive: Unable to open '/vendor/overlay/CarrierConfig__auto_generated_rro_vendor.dm': No such file or directory|
DM badsha app diagnostics (update.*): {"at":1794665446505,"tag":"update.dm","error":"dm checksum mismatch"}|
DM badsha: FATAL(any process)=0 of the app=0 ANR=0
DM badsha first cold starts after the update: TotalTime: 1431 Fully drawn org.chyavorec.app/.MainActivity: +1s646ms TotalTime: 1179 Fully drawn org.chyavorec.app/.MainActivity: +1s422ms TotalTime: 1399 Fully drawn org.chyavorec.app/.MainActivity: +1s630ms 
## DM-junk. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 28:30:/home/runner/upc-srv/junk.v0_1_0_p.dm 31::/home/runner/upc-srv/junk.v0_1_5_s.dm
manifest: vc 1510100 app.apk 4640810 B; dm: 28-30 junk.v0_1_0_p.dm 24000 B sha acc37f44d7, 31-None junk.v0_1_5_s.dm 24000 B sha acc37f44d7
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM junk: dialog dialog after 12629 ms: text="Нова версия 1.2.38" text="Новата версия е изтеглена и проверена (контролна сума и подпис)." text="Какво ново" text="• Тестово обновяване (емулатор)" text="По-късно" text="Инсталирай" ; files: 4640810 2026-11-14 14:11 update-1510100.apk;24000 2026-11-14 14:11 update-1510100.dm;
INSTALLER: text="Chitalishte Yavorets" text="Do you want to update this app?" text="Cancel" text="Update" 
DM junk RESULT: INSTALLED in 9 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM junk dexopt:  path: /data/app/~~XpArmCPjfgjZ_57pDASvYQ==/org.chyavorec.app-sPNpuQIPRpI1AEAhUzdhfg==/base.apk| x86_64: [status=verify] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
DM junk files: /data/misc/profiles/cur/0/org.chyavorec.app:;0 2026-11-14 14:11 primary.prof;/data/misc/profiles/ref/org.chyavorec.app:;4640810 2026-11-14 14:11 base.apk;4096 2026-11-14 14:11 lib;4096 2026-11-14 14:11 oat;
DM junk server: GET /update.json Range=None If-Range=None -> 200 sent 724/724 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.5 s (rate 0 B/s)~GET /junk.v0_1_5_s.dm Range=None If-Range=None -> 200 START (24000 B)~GET /junk.v0_1_5_s.dm Range=None If-Range=None -> 200 sent 24000/24000 B in 0.0 s (rate 0 B/s)~
DM junk PackageManager/installd: 11-14 14:11:47.386   485   554 D ziparchive: Zip: EOCD not found, /data/app/vmdl1598422546.tmp/base.dm is not zip|11-14 14:11:47.390   485   554 W PackageManager: Failed parse during installPackageLI: Error opening /data/app/vmdl1598422546.tmp/base.dm: Invalid file|11-14 14:11:47.391   485   554 D PackageInstallerSession: Marking session 1598422546 as failed: INSTALL_FAILED_BAD_DEX_METADATA: Failed parse during installPackageLI: Error opening /data/app/vmdl1598422546.tmp/base.dm: Invalid fil|11-14 14:11:47.442   485  2036 I ActivityTaskManager: START u0 {act=android.content.pm.action.CONFIRM_INSTALL flg=0x10000000 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstal|11-14 14:11:47.480   485  2036 V SplashScreenExceptionList: SplashScreen checking exception for package com.google.android.packageinstaller (target sdk:33) -> false|11-14 14:11:47.612   485  2036 I ActivityTaskManager: START u0 {act=android.content.pm.action.CONFIRM_INSTALL flg=0x2000001 pkg=com.google.android.packageinstaller cmp=com.google.android.packageinstaller/com.android.packageinstall|11-14 14:11:47.991   485  2036 D CoreBackPreview: Window{6f4c292 u0 com.google.android.packageinstaller/com.android.packageinstaller.PackageInstallerActivity}: Setting back callback OnBackInvokedCallbackInfo{mCallback=android.wind|11-14 14:11:48.275   485   520 I ActivityTaskManager: Displayed com.google.android.packageinstaller/com.android.
DM junk app diagnostics (update.*): {"at":1794665507394,"tag":"update.install-status","error":"PackageInstaller status 4"}|{"at":1794665507399,"tag":"update.dm","error":"install with dm failed (4), retrying without it"}|
DM junk: FATAL(any process)=0 of the app=0 ANR=0
DM junk first cold starts after the update: TotalTime: 1548 Fully drawn org.chyavorec.app/.MainActivity: +1s689ms TotalTime: 1132 Fully drawn org.chyavorec.app/.MainActivity: +1s390ms TotalTime: 1264 Fully drawn org.chyavorec.app/.MainActivity: +1s464ms 
## DM-old137. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: 31::dm-base/app-base.v0_1_5_s.dm
manifest: vc 1510100 app.apk 4640810 B; dm: 31-None app-base.v0_1_5_s.dm 20743 B sha 5e6f4bcffe
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM old137: dialog dialog after 12301 ms: text="Нова версия 1.2.38" text="Новата версия е изтеглена и проверена (контролна сума и подпис)." text="Какво ново" text="• Тестово обновяване (емулатор)" text="По-късно" text="Инсталирай" ; files: 4640810 2026-11-14 14:12 update-1510100.apk;20743 2026-11-14 14:12 update-1510100.dm;
DM old137 RESULT: INSTALLED in 4 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM old137 dexopt:  path: /data/app/~~k32AvsdzDlnQ--mZorCitQ==/org.chyavorec.app-Dl0ML_WI-ncdtZ2-yfGn_A==/base.apk| x86_64: [status=speed-profile] [reason=install-dm]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
DM old137 files: /data/misc/profiles/cur/0/org.chyavorec.app:;19549 2026-11-14 14:12 primary.prof;/data/misc/profiles/ref/org.chyavorec.app:;19098 2026-11-14 14:12 primary.prof;4640810 2026-11-14 14:12 base.apk;20743 2026-11-14 14:12 base.dm;4096 2026-11-14 14:12 lib;4096 2026-11-14 14:12 oat;
DM old137 server: GET /update.json Range=None If-Range=None -> 200 sent 535/535 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.5 s (rate 0 B/s)~GET /app-base.v0_1_5_s.dm Range=None If-Range=None -> 200 START (20743 B)~GET /app-base.v0_1_5_s.dm Range=None If-Range=None -> 200 sent 20743/20743 B in 0.0 s (rate 0 B/s)~
DM old137 PackageManager/installd: 11-14 14:12:49.665   485   554 I system_server: No primary.vdex file in DexMetadata archive. Not doing fast verification.|11-14 14:12:49.665   485   554 I PackageDexOptimizer: Running dexopt (dexoptNeeded=1) on: /data/app/~~k32AvsdzDlnQ--mZorCitQ==/org.chyavorec.app-Dl0ML_WI-ncdtZ2-yfGn_A==/base.apk pkg=org.chyavorec.app isa=x86_64 dexoptFlags=boot_c|11-14 14:12:49.725 10556 10556 W dex2oat64: /apex/com.android.art/bin/dex2oat64 --zip-fd=8 --zip-location=base.apk --oat-fd=9 --oat-location=/data/app/~~k32AvsdzDlnQ--mZorCitQ==/org.chyavorec.app-Dl0ML_WI-ncdtZ2-yfGn_A==/oat/x86_6|11-14 14:12:49.739 10556 10556 I dex2oat64: No primary.vdex file in DexMetadata archive. Not doing fast verification.|11-14 14:12:49.739 10556 10556 I dex2oat64: /apex/com.android.art/bin/dex2oat64 --input-vdex-fd=-1 --output-vdex-fd=10 --dm-fd=14 --profile-file-fd=12 --classpath-dir=/data/app/~~k32AvsdzDlnQ--mZorCitQ==/org.chyavorec.app-Dl0ML_WI|11-14 14:12:50.448   485   531 W BroadcastQueue: Background execution not allowed: receiving Intent { act=android.intent.action.PACKAGE_ADDED dat=package: flg=0x4000010 (has extras) } to com.google.android.packageinstaller/com.and|11-14 14:12:50.482   485   554 D PackageInstallerSession: Marking session 403966220 as applied|11-14 14:12:56.459   485  2046 W ziparchive: Unable to open '/product/overlay/ManagedProvisioning__auto_generated_rro_product.dm': No such file or directory|
DM old137 app diagnostics (update.*): 
DM old137: FATAL(any process)=0 of the app=0 ANR=0
DM old137 first cold starts after the update: TotalTime: 1704 Fully drawn org.chyavorec.app/.MainActivity: +1s790ms TotalTime: 1261 Fully drawn org.chyavorec.app/.MainActivity: +1s528ms TotalTime: 1339 Fully drawn org.chyavorec.app/.MainActivity: +1s560ms 
## DM-none. In-app update 1.2.38-local (vc 1510099) → 1.2.38 (vc 1510100) from https://10.0.2.2:8443; dm: none
manifest: vc 1510100 app.apk 4640810 B; dm: 
installed upd-local: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510099; update manifest: https://10.0.2.2:8443/update.json (local test server) size 4640582;  versionCode=1510099 minSdk=26 targetSdk=37  versionName=1.2.38 
install-unknown-apps: REQUEST_INSTALL_PACKAGES: allow
DM none: dialog dialog after 12656 ms: text="Нова версия 1.2.38" text="Новата версия е изтеглена и проверена (контролна сума и подпис)." text="Какво ново" text="• Тестово обновяване (емулатор)" text="По-късно" text="Инсталирай" ; files: 4640810 2026-11-14 14:13 update-1510100.apk;
DM none RESULT: INSTALLED in 4 s;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
DM none dexopt:  path: /data/app/~~vjiEgExfygNMqjFj8gqoig==/org.chyavorec.app-zJeHj1oJMtmG0VuSF4c1xg==/base.apk| x86_64: [status=verify] [reason=install]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
DM none files: /data/misc/profiles/cur/0/org.chyavorec.app:;19549 2026-11-14 14:13 primary.prof;/data/misc/profiles/ref/org.chyavorec.app:;4640810 2026-11-14 14:13 base.apk;4096 2026-11-14 14:13 lib;4096 2026-11-14 14:13 oat;
DM none server: GET /update.json Range=None If-Range=None -> 200 sent 324/324 B in 0.0 s (rate 0 B/s)~GET /app.apk Range=None If-Range=None -> 200 START (4640810 B)~GET /app.apk Range=None If-Range=None -> 200 sent 4640810/4640810 B in 0.1 s (rate 0 B/s)~
DM none PackageManager/installd: 11-14 14:13:47.355   485   554 W ziparchive: Unable to open '/data/app/~~vjiEgExfygNMqjFj8gqoig==/org.chyavorec.app-zJeHj1oJMtmG0VuSF4c1xg==/base.dm': No such file or directory|11-14 14:13:47.355   485   554 I PackageDexOptimizer: Running dexopt (dexoptNeeded=1) on: /data/app/~~vjiEgExfygNMqjFj8gqoig==/org.chyavorec.app-zJeHj1oJMtmG0VuSF4c1xg==/base.apk pkg=org.chyavorec.app isa=x86_64 dexoptFlags=boot_c|11-14 14:13:48.355   485   531 W BroadcastQueue: Background execution not allowed: receiving Intent { act=android.intent.action.PACKAGE_ADDED dat=package: flg=0x4000010 (has extras) } to com.google.android.packageinstaller/com.and|11-14 14:13:48.384   485   554 D PackageInstallerSession: Marking session 451845118 as applied|11-14 14:13:54.493   485   517 W ziparchive: Unable to open '/product/overlay/ManagedProvisioning__auto_generated_rro_product.dm': No such file or directory|11-14 14:13:54.493   485   517 W ziparchive: Unable to open '/product/overlay/ManagedProvisioning__auto_generated_rro_product.dm': No such file or directory|11-14 14:13:54.493   485   517 W ziparchive: Unable to open '/vendor/overlay/CarrierConfig__auto_generated_rro_vendor.dm': No such file or directory|11-14 14:13:54.493   485   517 W ziparchive: Unable to open '/vendor/overlay/CarrierConfig__auto_generated_rro_vendor.dm': No such file or directory|
DM none app diagnostics (update.*): 
DM none: FATAL(any process)=0 of the app=0 ANR=0
DM none first cold starts after the update: TotalTime: 1279 Fully drawn org.chyavorec.app/.MainActivity: +1s540ms TotalTime: 1263 Fully drawn org.chyavorec.app/.MainActivity: +1s538ms TotalTime: 1541 Fully drawn org.chyavorec.app/.MainActivity: +1s751ms 
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 3 | Първи кадър: медиана 1219 мс, последен 1418 мс | Пълно показване: медиана 1483 мс, последно 1637 мс | Топъл старт · 1 | Първи кадър: медиана 761 мс, последен 761 мс | Пълно показване: медиана 1210 мс
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 3" text="Първи кадър: медиана 1219 мс, последен 1418 мс" text="Пълно показване: медиана 1483 мс, последно 1637 мс" text="Топъл старт · 1" text="Първи кадър: 
APP W/E lines (pids ['5822', '6825', '6970', '7188', '7438', '7705', '7852', '7932', '8136', '8342', '8554', '8624', '8686', '8884', '9072', '9275', '9340', '9419', '9614', '9912', '10078', '10144', '10204', '10387', '10576', '10902', '10966', '11031', '11241', '11430', '11632', '11694', '11756']): 269 in 16 groups
      48  E/OpenGLRenderer: Unable to match the desired swap behavior.
      43  W/Parcel: Expecting binder but got null!
      33  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      33  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      33  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      33  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      33  W/Gralloc4: allocator #.x is not supported
       5  W/PackageParser: Unknown element under <manifest>: queries at /data/user/#/org.chyavorec.app/no_backup/updates/update-#.apk.par
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#e#-#c#c-#-#a#-bedfc#ccac#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ad#cc#-c#e#-#e#-#b-b#d#f#b, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=b#a#-d#-#b#e-b#a-e#f#ab#af#d, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object xv#.getValue()(r#-map-id
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=e#e#f-cf#-#bb#-#a#-#fdc#b#c#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=ee#a-e#c-#d#-#df#-#fb#ee#de, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#d#c#c-#a-#efe-a#f-d#e#db#b, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#c#-#b#-#c#-af#e-f#ae#dd#d#, generation=#)
contact sheets: 3 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5822', '6825', '6970', '7188', '7438', '7705', '7852', '7932', '8136', '8342', '8554', '8624', '8686', '8884', '9072', '9275', '9340', '9419', '9614', '9912', '10078', '10144', '10204', '10387', '10576', '10902', '10966', '11031', '11241', '11430', '11632', '11694', '11756']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 24
   skipped-frames events=24 max=58 total=881
== Davey: 47
   davey events=47 max=1356ms median=804ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:03 app-mapping.txt; crashes: 0 B
```

## mon38

```
part mon38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4100 ms
## K. Monkey 6000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=6955
MONKEY exit=0 duration=365 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6955'; airplane=0; camera: 
MEM after-monkey (raw) PSS=102693KB  Java=22348KB  Native=22792KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=12)  RSS=129152KB  Views=19 Activities=1
MEM after-monkey (trimmed) PSS=83778KB  Java=9524KB  Native=21240KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=12)  RSS=121004KB  Views=15 Activities=1
MON36 round 1 (seed 1357): whole log so far: FATAL=0
## K2. Monkey 6000 events, seed 4242
## W. First launch and warm-up
first-install catalogue results after 3578 ms
## K. Monkey 6000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=9120
MONKEY exit=0 duration=298 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '9120'; airplane=0; camera: 
MEM after-monkey (raw) PSS=162605KB  Java=22640KB  Native=98092KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=29)  RSS=251896KB  Views=15 Activities=1
MEM after-monkey (trimmed) PSS=136700KB  Java=12044KB  Native=79096KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=27)  RSS=227740KB  Views=15 Activities=1
MON36 round 2 done: whole log FATAL=0 ANR lines=0
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: tab bar not reached
WARN: Settings not found
APP W/E lines (pids ['5395', '6955', '8857', '9120']): 3952 in 53 groups
    3625  W/InputEventSender: Received 'finished' signal for unknown seq number = #
      63  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
      57  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
      33  W/Parcel: Expecting binder but got null!
      33  E/OpenGLRenderer: Unable to match the desired swap behavior.
      15  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      15  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      12  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
       7  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
       6  E/ViewRootImpl: Calling showInsets(#,true) on window that no longer has views.
       6  E/ImeBackDispatcher: Ime callback not found. Ignoring unregisterReceivedCallback. callbackId: #
       5  W/System: A resource failed to call release. 
       4  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       4  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       4  W/Gralloc4: allocator #.x is not supported
       4  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, metaSta
       4  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=
       4  W/VRI[Изскачащ прозорец]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanC
       4  W/VRI[Изскачащ прозорец]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCod
       3  W/RemoteInputConnectionImpl: setComposingText on inactive InputConnection
       3  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=-#.#, y[#]=#.
       3  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCod
       3  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanC
       2  W/ViewRootImpl: Not reporting timeline because gpuCompletedTime is #.#E#ms ahead of presentTime. FRAME_TIMELINE_VSYNC_ID=#, IN
       2  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       2  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=-#.#, y[#]=-#
       2  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanC
       2  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCod
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#df-#db#-#b#-bb#-fe#abf#dffd#, generation=#)
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5395', '6955', '8857', '9120']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 99
   skipped-frames events=99 max=120 total=4175
== Davey: 273
   davey events=273 max=3018ms median=850ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 0 B
```

## perf38

```
part perf38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## P0. Fresh install of new (1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versi), first launch, catalogue, a card by hand
installed new: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## P1. CPU and frames at rest (30 s after opening, then a 15 s window): Home after a cold start (~45 s), the card, fullscreen card, Още, Моето, quiz, timeline
IDLE home38-new (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7760
IDLE card38-new (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 7760
IDLE cardfull38-new (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 7760
IDLE more38-new (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7760
IDLE my38-new (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7760
IDLE quiz38-new (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7760
IDLE timeline38-new (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7760
## P2. Jank (animations ON, 1080x2400): Home, Още, Моето, timeline, quiz answering
GFX p38-new-home-scroll frames=   64  janky=63 (98.44%)  p50=400ms p90=600ms p95=700ms p99=750ms  slowUI=47 slowDraw=63 highInput=1
  GFXMEM p38-new-home-scroll: pipeline: Skia (OpenGL); GPU total: 43920416 bytes, 41.89 MB (35.57 MB is purgeable); textures: 2.83 MB (6 entries) | 7.14 MB (40 entries) | 5.42 MB (12 entries); frames: 64
  FS p38-new-home-scroll: n=63 slowest=967.8ms (frame #51; ui 330.7 rt 441.8 issue 331.1 flags 0) first5=[479, 736, 883, 815, 828] >32ms=63 >64ms=62 >100ms=62 span=12533ms
  p38-new-home-scroll HIST: frames=64 slowest-bucket=750ms ≥50ms=63 ≥100ms=63 ≥200ms=59 ≥500ms=14
WARN: tab bar not reached
GFX p38-new-more frames=   72  janky=72 (100.00%)  p50=250ms p90=350ms p95=350ms p99=500ms  slowUI=30 slowDraw=72 highInput=0
  GFXMEM p38-new-more: pipeline: Skia (OpenGL); GPU total: 78761168 bytes, 75.11 MB (70.76 MB is purgeable); textures: 13.85 KB (1 entry) | 6.85 MB (25 entries) | 8.57 MB (39 entries); frames: 72
  FS p38-new-more: n=72 slowest=644.8ms (frame #37; ui 295.0 rt 178.4 issue 63.8 flags 0) first5=[69, 102, 254, 288, 341] >32ms=72 >64ms=71 >100ms=69 span=11833ms
  p38-new-more HIST: frames=72 slowest-bucket=500ms ≥50ms=71 ≥100ms=69 ≥200ms=52 ≥500ms=1
GFX p38-new-my   frames=    0  janky=0 (0.00%)  p50=4950ms p90=4950ms p95=4950ms p99=4950ms  slowUI=0 slowDraw=0 highInput=0
  GFXMEM p38-new-my: pipeline: Skia (OpenGL); GPU total: 82949432 bytes, 79.11 MB (74.92 MB is purgeable); textures: 13.85 KB (1 entry) | 1.82 MB (33 entries) | 7.81 MB (26 entries); frames: 0
  FS p38-new-my: no frames
  p38-new-my HIST: frames=0 slowest-bucket=0ms ≥50ms=0 ≥100ms=0 ≥200ms=0 ≥500ms=0
GFX p38-new-timeline frames=   79  janky=79 (100.00%)  p50=300ms p90=400ms p95=500ms p99=550ms  slowUI=42 slowDraw=79 highInput=0
  GFXMEM p38-new-timeline: pipeline: Skia (OpenGL); GPU total: 88207616 bytes, 84.12 MB (70.79 MB is purgeable); textures: 693.48 KB (35 entries) | 2.80 MB (20 entries); frames: 79
  FS p38-new-timeline: n=81 slowest=771.4ms (frame #73; ui 176.0 rt 376.3 issue 242.6 flags 0) first5=[104, 176, 381, 563, 574] >32ms=81 >64ms=81 >100ms=79 span=13233ms
  p38-new-timeline HIST: frames=79 slowest-bucket=550ms ≥50ms=79 ≥100ms=77 ≥200ms=63 ≥500ms=5
GFX p38-new-quizflow frames=   97  janky=97 (100.00%)  p50=300ms p90=500ms p95=600ms p99=650ms  slowUI=39 slowDraw=97 highInput=0
  GFXMEM p38-new-quizflow: pipeline: Skia (OpenGL); GPU total: 87397960 bytes, 83.35 MB (68.81 MB is purgeable); textures: 371.13 KB (37 entries) | 2.02 MB (7 entries); frames: 97
## P3. Memory: 12 cycles over Още/Моето/quiz/timeline/card/embroidery, then a full tour
MEM new-c00 (raw)    PSS=63204KB  Java=10416KB  Native=24612KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=25)  RSS=149316KB  Views=7 Activities=1
MEM new-c00 (trimmed) PSS=68109KB  Java=9808KB  Native=24612KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=25)  RSS=155000KB  Views=7 Activities=1
MEM new-c04 (raw)    PSS=77750KB  Java=15024KB  Native=28280KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=7)  RSS=168800KB  Views=10 Activities=1
MEM new-c04 (trimmed) PSS=70422KB  Java=9712KB  Native=26280KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=7)  RSS=161472KB  Views=10 Activities=1
MEM new-c08 (raw)    PSS=79325KB  Java=14916KB  Native=28928KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=7)  RSS=171136KB  Views=10 Activities=1
MEM new-c08 (trimmed) PSS=72177KB  Java=9780KB  Native=26936KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=7)  RSS=163988KB  Views=10 Activities=1
MEM new-c12 (raw)    PSS=83335KB  Java=17488KB  Native=29144KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=7)  RSS=175452KB  Views=10 Activities=1
MEM new-c12 (trimmed) PSS=73095KB  Java=9916KB  Native=26652KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=7)  RSS=165212KB  Views=10 Activities=1
## T-perf-new. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP perf-new/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP perf-new/01b-home-2: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | 14 ноември 2026 · *Св. ап. Ф · Последни новини · Моите книги · Читателска карта · Искам да прочета · Контакти
CAP perf-new/01c-home-3: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Моите книги · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP perf-new/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP perf-new/01e-search-12: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=36 ellipsized=1 [Търси книга, новина, събитие…] | Лента за търсене · Търси книга, новина, събитие · Сканирай баркод · 14 ноември 2026 · *Св. ап. Ф · Последни новини · БИБЛИОТЕКА
CAP perf-new/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP perf-new/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP perf-new/02c-filters-2: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=0 | Манипулатор за преместване с · Раздел (жанр) · Общ отдел · Философия. Психология · Религия · Обществени науки
CAP perf-new/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP perf-new/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP perf-new/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP perf-new/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP perf-new/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP perf-new/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP perf-new/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP perf-new/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Дългият войната · Още една · Към книгата · Затвори
CAP perf-new/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP perf-new/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP perf-new/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP perf-new/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP perf-new/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP perf-new/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP perf-new/08b-news-2: targets=19 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=24 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP perf-new/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP perf-new/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP perf-new/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP perf-new/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP perf-new/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP perf-new/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP perf-new/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP perf-new/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP perf-new/12-more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP perf-new/12b-more-2: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=24 ellipsized=0 | Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа
CAP perf-new/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP perf-new/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP perf-new/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP perf-new/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP perf-new/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP perf-new/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP perf-new/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP perf-new/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP perf-new/18-settings: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP perf-new/18b-settings-2: targets=12 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=19 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP perf-new/18c-settings-3: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“
CAP perf-new/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Избери известията · Заемания, събития, съобщения · ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия
CAP perf-new/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP perf-new/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP perf-new/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP perf-new/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP perf-new/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP perf-new/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP perf-new/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP perf-new/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP perf-new/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP perf-new/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP perf-new/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP perf-new/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR perf-new: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=10 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=51
MEM new-after-tour (raw) PSS=96729KB  Java=11416KB  Native=51636KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=19)  RSS=190756KB  Views=13 Activities=1
MEM new-after-tour (trimmed) PSS=99274KB  Java=11044KB  Native=51636KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=19)  RSS=196852KB  Views=13 Activities=1
PERF38 new: FATAL=0; pid 19582
contact sheets: 9 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 5 | Първи кадър: медиана 3819 мс, последен 3819 мс | Пълно показване: медиана 4534 мс, последно 4874 мс | Топъл старт · 1 | Първи кадър: медиана 962 мс, последен 962 мс | Насечени кадри по екрани | home:
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 5" text="Първи кадър: медиана 3819 мс, последен 3819 мс" text="Пълно показване: медиана 4534 мс, последно 4874 мс" text="Топъл старт · 1" text="Първи кадър: 
APP W/E lines (pids ['5316', '7760', '9187', '10845', '19582']): 237 in 90 groups
      15  E/OpenGLRenderer: Unable to match the desired swap behavior.
      14  W/Parcel: Expecting binder but got null!
       7  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       7  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       6  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       5  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       5  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/Gralloc4: allocator #.x is not supported
       5  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       3  W/BasicSecureTextField: 	at yo#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       3  W/BasicSecureTextField: 	at hd#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       3  W/BasicSecureTextField: 	at fz#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       3  W/BasicSecureTextField: 	at id#.H#(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       3  W/BasicSecureTextField: 	at id#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       3  W/BasicSecureTextField: 	at ty.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       3  W/BasicSecureTextField: 	at uy#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at da#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at b#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at b#.b(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at a#.invoke(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
contact sheets: 10 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5316', '7760', '9187', '10845', '19582']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 48
   skipped-frames events=48 max=127 total=2517
== Davey: 138
   davey events=138 max=3664ms median=888ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'ANR': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 9097 B
```

## perfb38

```
part perfb38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## P0. Fresh install of base (1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versi), first launch, catalogue, a card by hand
installed base: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## P1. CPU and frames at rest (30 s after opening, then a 15 s window): Home after a cold start (~45 s), the card, fullscreen card, Още, Моето, quiz, timeline
IDLE home38-base (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8053
IDLE card38-base (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8053
IDLE cardfull38-base (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 8053
IDLE more38-base (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8053
IDLE my38-base (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8053
IDLE quiz38-base (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8053
IDLE timeline38-base (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 8053
## P2. Jank (animations ON, 1080x2400): Home, Още, Моето, timeline, quiz answering
GFX p38-base-home-scroll frames=   86  janky=85 (98.84%)  p50=250ms p90=450ms p95=550ms p99=600ms  slowUI=47 slowDraw=85 highInput=1
  GFXMEM p38-base-home-scroll: pipeline: Skia (OpenGL); GPU total: 54790908 bytes, 52.25 MB (47.26 MB is purgeable); textures: 2.37 MB (6 entries) | 7.03 MB (36 entries) | 9.59 MB (23 entries); frames: 86
  FS p38-base-home-scroll: n=85 slowest=808.7ms (frame #24; ui 348.7 rt 285.3 issue 187.9 flags 0) first5=[233, 505, 698, 747, 621] >32ms=85 >64ms=83 >100ms=80 span=12700ms
  p38-base-home-scroll HIST: frames=86 slowest-bucket=600ms ≥50ms=85 ≥100ms=80 ≥200ms=63 ≥500ms=6
WARN: tab bar not reached
GFX p38-base-more frames=   76  janky=76 (100.00%)  p50=200ms p90=350ms p95=350ms p99=450ms  slowUI=35 slowDraw=76 highInput=0
  GFXMEM p38-base-more: pipeline: Skia (OpenGL); GPU total: 66472292 bytes, 63.39 MB (60.39 MB is purgeable); textures: 2.38 MB (7 entries) | 7.05 MB (40 entries) | 9.59 MB (24 entries); frames: 76
  FS p38-base-more: n=76 slowest=615.0ms (frame #16; ui 235.1 rt 223.7 issue 133.3 flags 0) first5=[80, 98, 165, 300, 220] >32ms=76 >64ms=75 >100ms=70 span=11033ms
  p38-base-more HIST: frames=76 slowest-bucket=450ms ≥50ms=75 ≥100ms=69 ≥200ms=49 ≥500ms=0
GFX p38-base-my  frames=   69  janky=69 (100.00%)  p50=300ms p90=450ms p95=450ms p99=500ms  slowUI=31 slowDraw=69 highInput=0
  GFXMEM p38-base-my: pipeline: Skia (OpenGL); GPU total: 82758480 bytes, 78.92 MB (72.67 MB is purgeable); textures: 13.85 KB (1 entry) | 1.61 MB (25 entries) | 6.94 MB (34 entries); frames: 69
  FS p38-base-my: n=69 slowest=690.5ms (frame #32; ui 309.4 rt 220.9 issue 77.0 flags 0) first5=[144, 226, 372, 600, 562] >32ms=69 >64ms=69 >100ms=69 span=11933ms
  p38-base-my HIST: frames=69 slowest-bucket=500ms ≥50ms=69 ≥100ms=69 ≥200ms=54 ≥500ms=1
GFX p38-base-timeline frames=   89  janky=85 (95.51%)  p50=150ms p90=300ms p95=300ms p99=450ms  slowUI=41 slowDraw=85 highInput=5
  GFXMEM p38-base-timeline: pipeline: Skia (OpenGL); GPU total: 87737144 bytes, 83.67 MB (77.24 MB is purgeable); textures: 541.12 KB (47 entries) | 4.51 MB (11 entries); frames: 89
  FS p38-base-timeline: n=89 slowest=526.7ms (frame #73; ui 150.7 rt 305.4 issue 145.3 flags 0) first5=[48, 123, 139, 215, 265] >32ms=88 >64ms=83 >100ms=82 span=11017ms
  p38-base-timeline HIST: frames=89 slowest-bucket=450ms ≥50ms=83 ≥100ms=76 ≥200ms=37 ≥500ms=0
GFX p38-base-quizflow frames=  101  janky=99 (98.02%)  p50=150ms p90=200ms p95=200ms p99=250ms  slowUI=42 slowDraw=99 highInput=2
  GFXMEM p38-base-quizflow: pipeline: Skia (OpenGL); GPU total: 83868656 bytes, 79.98 MB (73.48 MB is purgeable); textures: 367.93 KB (37 entries) | 2.32 MB (30 entries); frames: 101
## P3. Memory: 12 cycles over Още/Моето/quiz/timeline/card/embroidery, then a full tour
MEM base-c00 (raw)   PSS=63712KB  Java=10292KB  Native=24596KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=28)  RSS=147592KB  Views=7 Activities=1
MEM base-c00 (trimmed) PSS=68658KB  Java=9912KB  Native=24612KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=28)  RSS=153456KB  Views=7 Activities=1
MEM base-c04 (raw)   PSS=72612KB  Java=12116KB  Native=26468KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=11)  RSS=161960KB  Views=10 Activities=1
MEM base-c04 (trimmed) PSS=69588KB  Java=9576KB  Native=25996KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=11)  RSS=158936KB  Views=10 Activities=1
MEM base-c08 (raw)   PSS=79123KB  Java=17596KB  Native=26004KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=15)  RSS=168692KB  Views=10 Activities=1
MEM base-c08 (trimmed) PSS=69707KB  Java=9788KB  Native=24416KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=15)  RSS=159276KB  Views=10 Activities=1
MEM base-c12 (raw)   PSS=71715KB  Java=11144KB  Native=24856KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=162236KB  Views=10 Activities=1
MEM base-c12 (trimmed) PSS=70211KB  Java=9676KB  Native=24824KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=160732KB  Views=10 Activities=1
## T-perf-base. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP perf-base/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP perf-base/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги · Читателска карта · Искам да прочета
CAP perf-base/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · 21
CAP perf-base/01d-home-4: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP perf-base/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP perf-base/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP perf-base/02c-filters-2: targets=24 MISSING=1 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=2 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел · Философия. Психология
CAP perf-base/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP perf-base/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP perf-base/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP perf-base/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP perf-base/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP perf-base/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP perf-base/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP perf-base/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Сянката на река · Още една · Към книгата · Затвори
CAP perf-base/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP perf-base/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Здравей! · Новините, събитията, каталог · Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта
CAP perf-base/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP perf-base/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP perf-base/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP perf-base/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP perf-base/08b-news-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=27 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP perf-base/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP perf-base/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP perf-base/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP perf-base/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP perf-base/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP perf-base/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP perf-base/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP perf-base/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP perf-base/12-more: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP perf-base/12b-more-2: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Снимки от сайта · Бод по бод, като на кърпа · Старият вестник · Статии от юбилейния вестник · Лента на времето · Историята на читалището от 1
CAP perf-base/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP perf-base/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP perf-base/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP perf-base/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP perf-base/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP perf-base/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Адрес · пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл
CAP perf-base/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP perf-base/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP perf-base/18-settings: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Изглед · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP perf-base/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
CAP perf-base/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Разклати телефона в каталога · Уиджети · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP perf-base/18d-settings-4: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Обновления · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.37 · Данни
CAP perf-base/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP perf-base/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP perf-base/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP perf-base/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP perf-base/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP perf-base/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP perf-base/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.37 (1510000) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP perf-base/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.37 (1510000)
CAP perf-base/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.37 (1510000)
CAP perf-base/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Вход за читатели · Читателски номер · ПИН (от библиотеката) · Покажи ПИН-а · Запомни ме на това устройств · Вход
CAP perf-base/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP perf-base/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR perf-base: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=4 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=47
MEM base-after-tour (raw) PSS=104079KB  Java=16220KB  Native=53964KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=25)  RSS=196064KB  Views=13 Activities=1
MEM base-after-tour (trimmed) PSS=98153KB  Java=11108KB  Native=50432KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=25)  RSS=193776KB  Views=13 Activities=1
PERF38 base: FATAL=0; pid 19241
contact sheets: 9 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.37 (1510000) | Профил: чака | Студен старт · 5 | Първи кадър: медиана 3124 мс, последен 3538 мс | Пълно показване: медиана 3455 мс, последно 3858 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.37 (1510000)" text="Профил: чака" text="Студен старт · 5" text="Първи кадър: медиана 3124 мс, последен 3538 мс" text="Пълно показване: медиана 3455 мс, последно 3858 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5735', '8053', '9537', '11092', '19241']): 230 in 89 groups
      13  W/Parcel: Expecting binder but got null!
      13  E/OpenGLRenderer: Unable to match the desired swap behavior.
       7  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       7  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       5  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       5  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/Gralloc4: allocator #.x is not supported
       5  W/BasicSecureTextField: 	at nl#.d(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       3  W/BasicSecureTextField: 	at pn#.d(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       3  W/BasicSecureTextField: 	at ub#.a(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       3  W/BasicSecureTextField: 	at xx#.c(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       3  W/BasicSecureTextField: 	at vb#.I#(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       3  W/BasicSecureTextField: 	at vb#.d(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       3  W/BasicSecureTextField: 	at sy.c(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       3  W/BasicSecureTextField: 	at mx#.d(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at h#.a(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       2  W/BasicSecureTextField: 	at mx#.b(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       2  W/BasicSecureTextField: 	at mx#.a(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
       2  W/BasicSecureTextField: 	at x#.invoke(r#-map-id-c#cc#d#b#db#bee#e#fb#feb#bb#ecd#c#aab#f#bb#b#:#)
contact sheets: 10 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5735', '8053', '9537', '11092', '19241']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 30
   skipped-frames events=30 max=118 total=1578
== Davey: 56
   davey events=56 max=3415ms median=960ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:03 app-mapping.txt; crashes: 0 B
```

## start38a

```
part start38a; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## S0. Emulator warm-up block (not counted): both apps, 2 installs × 5 cold starts
installed warm-base: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
installed warm-new: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
block order (each a fresh install): new base base new new base base new
## S new-1: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-1: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 catalogue after 3020 ms
new-1 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-1: n=5 min=2704ms median=2863ms max=3133ms
## S base-2: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-2: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 catalogue after 3179 ms
base-2 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-2: n=5 min=2349ms median=2499ms max=3245ms
## S base-3: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-3: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-3 catalogue after 2785 ms
base-3 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-3: n=5 min=2399ms median=2532ms max=2978ms
## S new-4: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-4: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-4 catalogue after 3221 ms
new-4 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-4: n=5 min=2249ms median=2603ms max=2888ms
## S new-5: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-5: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-5 catalogue after 3042 ms
new-5 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-5: n=5 min=2287ms median=2474ms max=2530ms
## S base-6: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-6: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-6 catalogue after 3016 ms
base-6 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-6: n=5 min=2352ms median=2469ms max=2797ms
## S base-7: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-7: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-7 catalogue after 3149 ms
base-7 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-7: n=5 min=2249ms median=2438ms max=2920ms
## S new-8: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-8: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-8 catalogue after 3056 ms
new-8 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-8: n=5 min=2310ms median=2597ms max=2734ms
## S totals (n=20 per app)
START cold new           n=20 min=2249ms median=2600ms max=3133ms  all=[2815, 3133, 2863, 2704, 2982, 2603, 2853, 2482, 2249, 2888, 2287, 2449, 2530, 2474, 2527, 2310, 2597, 2354, 2734, 2663]  states=['COLD']
DRAWN new: TotalTime n=20 min=2249ms median=2600ms max=3133ms; fully drawn n=20 median=2969ms min=2554 max=3987 all=[3233, 3987, 3172, 3103, 3471, 2741, 3153, 2763, 3063, 3528, 2634, 2758, 2831, 2554, 2915, 2590, 3023, 2622, 3031, 2798]; without line: 0
START cold base          n=20 min=2249ms median=2504ms max=3245ms  all=[2499, 2688, 3245, 2484, 2349, 2978, 2532, 2508, 2399, 2572, 2469, 2674, 2352, 2395, 2797, 2438, 2920, 2249, 2411, 2707]  states=['COLD']
DRAWN base: TotalTime n=20 min=2249ms median=2504ms max=3245ms; fully drawn n=20 median=2789ms min=2517 max=4047 all=[2623, 2975, 4047, 2673, 2899, 3138, 3001, 2781, 2777, 2999, 2777, 3038, 2663, 2654, 2797, 2769, 3161, 2517, 2665, 2866]; without line: 0
AB TotalTime  : 1.2.35 n=20 median 2600 ms (mean 2625) | 1.2.34 n=20 median 2504 ms (mean 2583) | diff of medians +96 ms (+3.9%) | Mann-Whitney p=0.47
AB Fully drawn: 1.2.35 n=20 median 2969 ms (mean 2998) | 1.2.34 n=20 median 2789 ms (mean 2891) | diff of medians +180 ms (+6.5%) | Mann-Whitney p=0.32
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 8 | Първи кадър: медиана 2360 мс, последен 2488 мс | Пълно показване: медиана 2565 мс, последно 2629 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 8" text="Първи кадър: медиана 2360 мс, последен 2488 мс" text="Пълно показване: медиана 2565 мс, последно 2629 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5945', '6654', '6851', '7122', '7439', '7640', '7942', '8582', '8836', '8958', '9157', '9220', '9427', '9737', '9792', '9847', '9904', '10002', '10057', '10120', '10297', '10595', '10657', '10714', '10771', '10827', '10909', '10969', '11141', '11436', '11493', '11555', '11613', '11672', '11729', '11784', '12104', '12410', '12469', '12524', '12579', '12636', '12747', '12802', '12985', '13275', '13332', '13389', '13445', '13499', '13556', '13611', '13779', '14090', '14151', '14277', '14333', '14406', '14462', '14517', '14697', '14996', '15054', '15110', '15168', '15225', '15282', '15343', '15515', '15825', '15879', '15934', '15991', '16067', '16124', '16186']): 546 in 20 groups
      76  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      76  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      76  W/Parcel: Expecting binder but got null!
      76  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      76  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      76  W/Gralloc4: allocator #.x is not supported
      76  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object f#.s(java.lang.Object)(r
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#fe#-cc#-#b#-#e-aa#a#fab#a#, generation=#)
       1  W/g.chyavorec.app: Suspending all threads took: #.#ms
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#d#f#f#-e#af-#e-b#-#d#cc#, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner OkHttp https://chyavorec.org/... (#) at mo# ys#.d()(r#-map-id-#f#a#cd#e#b#d
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#f#-#ad-#ee-#-f#f#db#e, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#bee#efc-b#c-#c#-#f#-ecd#afbe#b, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=c#cf-#a#-#-b#-#db#fbc#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=dc#b#f-#afb-#a#d-#a#-#e#ae#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#fb#-e#-#d#-#fb-f#c#eb#b, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#d#-#cc-#c#d-a#d#-#b#ca#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ffca#aa-#df#-#-b#e-c#c#d#ecf#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=f#ac#-#d#-#b#-#ad-caac#cf#a, generation=#)
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5945', '6654', '6851', '7122', '7439', '7640', '7942', '8582', '8836', '8958', '9157', '9220', '9427', '9737', '9792', '9847', '9904', '10002', '10057', '10120', '10297', '10595', '10657', '10714', '10771', '10827', '10909', '10969', '11141', '11436', '11493', '11555', '11613', '11672', '11729', '11784', '12104', '12410', '12469', '12524', '12579', '12636', '12747', '12802', '12985', '13275', '13332', '13389', '13445', '13499', '13556', '13611', '13779', '14090', '14151', '14277', '14333', '14406', '14462', '14517', '14697', '14996', '15054', '15110', '15168', '15225', '15282', '15343', '15515', '15825', '15879', '15934', '15991', '16067', '16124', '16186']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 248
   skipped-frames events=248 max=111 total=12597
== Davey: 363
   davey events=363 max=3097ms median=1213ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:03 app-mapping.txt; crashes: 3634 B
```

## start38b

```
part start38b; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## S0. Emulator warm-up block (not counted): both apps, 2 installs × 5 cold starts
installed warm-base: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
installed warm-new: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
block order (each a fresh install): base new new base base new new base
## S base-1: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-1: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 catalogue after 3145 ms
base-1 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-1: n=5 min=2452ms median=2755ms max=2883ms
## S new-2: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-2: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 catalogue after 2865 ms
new-2 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-2: n=5 min=2555ms median=2684ms max=3055ms
## S new-3: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-3: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-3 catalogue after 3012 ms
new-3 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-3: n=5 min=2206ms median=2631ms max=2885ms
## S base-4: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-4: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-4 catalogue after 2877 ms
base-4 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-4: n=5 min=2515ms median=2573ms max=2804ms
## S base-5: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-5: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-5 catalogue after 2861 ms
base-5 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-5: n=5 min=2575ms median=2922ms max=3203ms
## S new-6: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-6: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-6 catalogue after 3015 ms
new-6 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-6: n=5 min=2122ms median=2492ms max=2824ms
## S new-7: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-7: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810;  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-7 catalogue after 2923 ms
new-7 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-7: n=5 min=2254ms median=2378ms max=2707ms
## S base-8: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-8: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-8 catalogue after 2884 ms
base-8 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-8: n=5 min=2136ms median=2293ms max=2615ms
## S totals (n=20 per app)
START cold new           n=20 min=2122ms median=2566ms max=3055ms  all=[2746, 3055, 2555, 2577, 2684, 2885, 2409, 2631, 2882, 2206, 2387, 2824, 2492, 2122, 2820, 2428, 2707, 2254, 2378, 2288]  states=['COLD']
DRAWN new: TotalTime n=20 min=2122ms median=2566ms max=3055ms; fully drawn n=20 median=2814ms min=2375 max=3414 all=[3178, 3414, 2859, 2769, 3028, 3300, 2513, 2688, 3274, 2772, 2553, 3277, 2856, 2375, 3179, 2739, 3195, 2629, 2595, 2712]; without line: 0
START cold base          n=20 min=2136ms median=2613ms max=3203ms  all=[2686, 2452, 2883, 2755, 2828, 2516, 2573, 2515, 2804, 2625, 2611, 3203, 2575, 2922, 3127, 2136, 2615, 2547, 2293, 2255]  states=['COLD']
DRAWN base: TotalTime n=20 min=2136ms median=2613ms max=3203ms; fully drawn n=20 median=2898ms min=2373 max=3456 all=[3233, 2777, 3416, 3098, 3199, 2768, 2795, 2693, 3135, 3227, 2823, 3333, 2837, 3370, 3456, 2373, 2958, 2781, 2512, 2592]; without line: 0
AB TotalTime  : 1.2.35 n=20 median 2566 ms (mean 2566) | 1.2.34 n=20 median 2613 ms (mean 2646) | diff of medians -47 ms (-1.8%) | Mann-Whitney p=0.37
AB Fully drawn: 1.2.35 n=20 median 2814 ms (mean 2895) | 1.2.34 n=20 median 2898 ms (mean 2969) | diff of medians -84 ms (-2.9%) | Mann-Whitney p=0.39
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.37 (1510000) | Профил: чака | Студен старт · 8 | Първи кадър: медиана 2146 мс, последен 2139 мс | Пълно показване: медиана 2433 мс, последно 2463 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.37 (1510000)" text="Профил: чака" text="Студен старт · 8" text="Първи кадър: медиана 2146 мс, последен 2139 мс" text="Пълно показване: медиана 2433 мс, последно 2463 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5739', '6812', '7003', '7372', '7522', '7712', '7920', '8531', '8703', '8817', '8883', '8955', '9176', '9492', '9561', '9625', '9688', '9746', '9812', '9900', '10090', '10409', '10470', '10538', '10598', '10674', '10733', '10795', '10977', '11278', '11339', '11398', '11458', '11517', '11580', '11688', '11970', '12278', '12342', '12402', '12464', '12524', '12585', '12645', '12878', '13185', '13248', '13310', '13370', '13430', '13489', '13551', '13747', '14130', '14215', '14274', '14332', '14392', '14453', '14516', '14686', '14986', '15047', '15108', '15167', '15229', '15288', '15352', '15529', '15845', '15909', '15969', '16029', '16088', '16148', '16212']): 542 in 17 groups
      76  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      76  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      76  W/Parcel: Expecting binder but got null!
      76  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      76  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      76  W/Gralloc4: allocator #.x is not supported
      76  E/OpenGLRenderer: Unable to match the desired swap behavior.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=fb#e-fc#f-#d#-#ee#-f#ad#e#aa#bd, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#c#d#b#-a#f-#f#-bfd#-f#f#ccefc, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#ffb#-#-#fbf-b#a#-#ffa#a#ef#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#f#abc#-b#-#d#e-ad#-#cc#d#da#f#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ee#b-f#d#-#-#b-#e#c#f#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#f#d#-aa#a-#eb#-#e-#c#ea#c, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object qt#.getValue()(r#-map-id
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#d#dd-d#a-#-#ccc-#c#e#cb#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=aab#b#f-#b#-#f#-#-cae#f#ca, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#ad#ddf#-#b#-#dd-be#e-#c#c#cacab#, generation=#)
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5739', '6812', '7003', '7372', '7522', '7712', '7920', '8531', '8703', '8817', '8883', '8955', '9176', '9492', '9561', '9625', '9688', '9746', '9812', '9900', '10090', '10409', '10470', '10538', '10598', '10674', '10733', '10795', '10977', '11278', '11339', '11398', '11458', '11517', '11580', '11688', '11970', '12278', '12342', '12402', '12464', '12524', '12585', '12645', '12878', '13185', '13248', '13310', '13370', '13430', '13489', '13551', '13747', '14130', '14215', '14274', '14332', '14392', '14453', '14516', '14686', '14986', '15047', '15108', '15167', '15229', '15288', '15352', '15529', '15845', '15909', '15969', '16029', '16088', '16148', '16212']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 258
   skipped-frames events=258 max=109 total=13051
== Davey: 384
   davey events=384 max=2625ms median=1208ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 1815 B
```

## tour38

```
part tour38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5269 ms
## T-bg-light. All screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP bg-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта
CAP bg-light/01c-home-3: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=33 ellipsized=0 | Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра
CAP bg-light/01d-home-4: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP bg-light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-light/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP bg-light/02c-filters-2: targets=26 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=28 ellipsized=0 | Манипулатор за преместване с · електронен документ · графично издание · аудиодокумент · продължаващо издание · патент/стандарт
CAP bg-light/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP bg-light/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-light/02f-shelf-2: targets=44 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=12 | front: app | texts=46 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-light/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP bg-light/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP bg-light/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-light/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-light/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Историята на по · Още една · Към книгата · Затвори
CAP bg-light/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP bg-light/05b-my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Примерна читателска карта · Читателски № 19220001 · Баркод на карта 19220001 · Така ще изглежда картата ти · Вход · Добави читателска карта
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP bg-light/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP bg-light/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP bg-light/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP bg-light/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-light/08b-news-2: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=24 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-light/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-light/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-light/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP bg-light/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-light/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP bg-light/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP bg-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP bg-light/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-light/12-more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-light/12b-more-2: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен
CAP bg-light/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP bg-light/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP bg-light/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP bg-light/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP bg-light/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP bg-light/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP bg-light/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-light/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-light/18-settings: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-light/18b-settings-2: targets=11 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=18 ellipsized=0 | Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език
CAP bg-light/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=18 ellipsized=0 | Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо
CAP bg-light/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | ИЗВЕСТИЯ · Избери известията · Заемания, събития, съобщения · ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два 
CAP bg-light/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP bg-light/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP bg-light/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP bg-light/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP bg-light/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP bg-light/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP bg-light/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP bg-light/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP bg-light/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP bg-light/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP bg-light/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP bg-light/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR bg-light: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=5 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=40
## NP-bg-light (full). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP bg-light/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-light/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги · Читателска карта
CAP bg-light/01c-home-3: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Моите книги · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP bg-light/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ
CAP bg-light/01e-home-5: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=21 ellipsized=0 | Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3 · НАЙ-ЧЕТЕНИ И НОВИ · За теб · Пътят на пътя
CAP bg-light/02-more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-light/02b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен
CAP bg-light/02c-more-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=0 | Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа · Старият вестник
CAP bg-light/em-01-open: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 16
CAP bg-light/em-02-stitched: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 16 → 6
EMB redo: 6 → 16
EMB four-way mirror, 2 taps: 16 → 18 (+8 expected)
CAP bg-light/em-03-mirror: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-04-lower: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
CAP bg-light/em-05-template: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB save: text="Моите шевици" 
CAP bg-light/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser NOT open after 12 s; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
EMB end: front app
CAP bg-light/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
CAP bg-light/np-01b-list-2: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | СТАТИЯ 02 · СПОМЕНИ · Баба Дамяна разказва (продъл · Възстановяването на селото с · СТАТИЯ 03 · СПОМЕНИ · Читалището — покровител и за · Спомени за читалището в запа
NEWSPAPER first open: page label after 2972 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-light/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.4
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
NEWSPAPER double tap: diff vs page 24.69
NEWSPAPER pinch: diff vs page 25.39
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2701 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP bg-light/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=225 std=29.4
CAP bg-light/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-light/tl-02: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP bg-light/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств
CAP bg-light/tl-04: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | 1927. Въздържателно дружеств · Въздържателно дружество „Бор · Основано от активни читалищн · 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр
CAP bg-light/tl-05: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | 1936. Дружество „Воля“. Ново · Ново въздържателно дружество · 1947. Решение за собствена с · Решение за собствена сграда · Започва отчуждаване на терен · 17 март 1958. Първа копка. С
CAP bg-light/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
CAP bg-light/tl-07: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 1988 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Коя от тези книги е написана от Максим Якубовски?" text="Колекционерът" text="Робинята" text="Един ден в древен Рим" text="Прокудените" text="Избродирай отгов
CAP bg-light/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
CAP bg-light/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
CAP bg-light/qz-02-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Устав и държавно признание · Дружество „Воля“ · Трудни години — устояване
CAP bg-light/qz-03-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 3 отговорени, общ · Въпрос 3 от 5 · Коя от тези книги е написана · Очи с цвят на вода · Загадката на Мари Роже · Ритуал в смъртта
CAP bg-light/qz-04-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 0 верни от 4 отговорени, общ · Въпрос 4 от 5 · Снимка от фотодокументалната · Какво е на снимката? · Фолклорна група Явор · Хората на труда
CAP bg-light/qz-05-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 5 отговорени, общ · Въпрос 5 от 5 · Какво се случва в читалището · Трудни години — устояване · Първият радиоапарат в селото · Първа копка
CAP bg-light/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser NOT open after 15 s; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
CAP bg-light/qz-07-again: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ again the same day: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text
CAP bg-light/st-01: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-light/st-02: targets=12 MISSING=4 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=18 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP bg-light/st-03: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“
CAP bg-light/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Избери известията · Заемания, събития, съобщения · ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-light/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP bg-light/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP bg-light: FATAL(any process)=0 of the app=0 ANR=0; app process 13240; logcat app errors: 0
## T-bg-dark. All screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP bg-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-dark/01b-home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги · Читателска карта · Искам да прочета
CAP bg-dark/01c-home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | БИБЛИОТЕКА · ЧИТАЛИЩЕ · БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3
CAP bg-dark/01d-home-4: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | БИБЛИОТЕКА · Нови постъпления · Към каталога · Дъхът на спомените — том 3 · Гласът на реката · Забравеният морето — том 3
CAP bg-dark/01e-search-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP bg-dark/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/02b-filters: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=26 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP bg-dark/02c-filters-2: targets=26 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Манипулатор за преместване с · електронен документ · графично издание · аудиодокумент · продължаващо издание · патент/стандарт
CAP bg-dark/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP bg-dark/02e-shelf: targets=48 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=51 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/02f-shelf-2: targets=45 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=22 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/02g-search-12: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP bg-dark/03-book: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP bg-dark/03b-book-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-dark/03c-book-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката · Кажи ми, когато е на рафта · Библиографски данни · Издателство
CAP bg-dark/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Небе и земя — Р · Още една · Към книгата · Затвори
CAP bg-dark/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP bg-dark/05b-my-2: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP bg-dark/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP bg-dark/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP bg-dark/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP bg-dark/08-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-dark/08b-news-2: targets=19 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=24 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP bg-dark/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-dark/09b-article-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP bg-dark/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP bg-dark/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-dark/10-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP bg-dark/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP bg-dark/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP bg-dark/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP bg-dark/12-more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-dark/12b-more-2: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | Дейности · Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта
CAP bg-dark/13-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP bg-dark/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP bg-dark/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP bg-dark/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP bg-dark/16-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP bg-dark/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP bg-dark/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-dark/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP bg-dark/18-settings: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-dark/18b-settings-2: targets=12 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=19 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP bg-dark/18c-settings-3: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP bg-dark/18d-settings-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=16 ellipsized=0 | Заемания, събития, съобщения · ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38
WARN: settings item =Размер на текста|=Text size not found
WARN: settings item =Език|=Language not found
WARN: settings item =Тема|=Theme not found
CAP bg-dark/18h-notifications: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP bg-dark/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP bg-dark/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP bg-dark/18k-about: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP bg-dark/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP bg-dark/18m-diagnostics-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP bg-dark/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP bg-dark/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP bg-dark/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR bg-dark: A11Y AGG: 54 screens analysed, 0 dumps failed; totals MISSING=7 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=48
## NP-bg-dark (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night yes, app locale [bg-BG], rotation 0)
CAP bg-dark/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP bg-dark/02-more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP bg-dark/em-01-open: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 47 → after 6 taps + a stroke 54
CAP bg-dark/em-02-stitched: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 54 → 48
EMB redo: 48 → 54
EMB save: text="Моите шевици" 
CAP bg-dark/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser NOT open after 12 s; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
EMB end: front app
CAP bg-dark/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2873 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-dark/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=30 std=25.0
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2482 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP bg-dark/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=30 std=25.0
CAP bg-dark/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP bg-dark/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 1912–1918. Прекъсване от вой · Балканската война (1912) пре · 17 дек. 1922. Възстановяване · Възстановяване и официално у · Дейността се възобновява под · 1923–1924. Устав и държавно 
CAP bg-dark/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 17 март 1958. Първа копка. С · Самооблагане 3% по Закона за · 1961. Открита читалищната сг · Открита читалищната сграда · Нова сграда с библиотека, го · След 1989. Трудни години — у
QUIZ ready after 2000 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" 
CAP bg-dark/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
CAP bg-dark/qz-01-answered: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен
QUIZ q1: no next button: text="Контакти" text="Адрес, телефон, местоположение" text="ЗАНИМАНИЯ" text="Извези шевица" text="Бод по бод, като на кърпа" text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Позна
CAP bg-dark/qz-06-result: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=0 | Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа · Старият вестник
QUIZ result: text="Контакти" text="Адрес, телефон, местоположение" text="ЗАНИМАНИЯ" text="Извези шевица" text="Бод по бод, като на кърпа" text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" t
QUIZ: no share button: text="Контакти" text="Адрес, телефон, местоположение" text="ЗАНИМАНИЯ" text="Извези шевица" text="Бод по бод, като на кърпа" text="Старият вестник" text="Статии от юбилейния вестник" text="Лента на времето" text="Историята на читалището от 
CAP bg-dark/st-01: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP bg-dark/st-02: targets=11 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=18 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP bg-dark/st-03: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=19 ellipsized=0 | Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога
CAP bg-dark/st-04: targets=8 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията · Заемания, събития, съобщения · ОБНОВЛЕНИЯ
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP bg-dark/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP bg-dark: FATAL(any process)=0 of the app=0 ANR=0; app process 23811; logcat app errors: 0
## TA. Accessibility over the tours
A11Y AGG: 58 screens analysed, 0 dumps failed; totals MISSING=11 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=10
== MISSING: 10 distinct
   View@[42,2320][1038,2400]  [1 screens: st-01]
   View@[84,296][370,350]  [1 screens: st-02]
   View@[396,296][683,350]  [1 screens: st-02]
   View@[709,296][996,350]  [1 screens: st-02]
   View@[42,2319][1038,2400]  [1 screens: st-04]
   View@[42,2318][1038,2400]  [1 screens: np-01b-list-2]
   View@[84,296][370,340]  [1 screens: st-02]
   View@[396,296][683,340]  [1 screens: st-02]
   View@[709,296][996,340]  [1 screens: st-02]
   View@[42,2334][1038,2400]  [1 screens: st-02]
== SMALL: 4 distinct
   'Контакти Адрес, телефон, место'  [2 screens: 02c-more-3, qz-06-result]
   ''  [1 screens: st-02]
   'Език Български'  [1 screens: st-03]
   'Всички'  [1 screens: 01e-home-5]
== SAME-LABEL: 0 distinct
== DOUBLE: 0 distinct
TOUR38 part: FATAL lines 0, app FATAL 0, ANR 0
contact sheets: 19 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 5 | Първи кадър: медиана 2755 мс, последен 1623 мс | Пълно показване: медиана 2755 мс, последно 2225 мс | Топъл старт · 1 | Първи кадър: медиана 919 мс, последен 919 мс | Насечени кадри по екрани | home:
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 5" text="Първи кадър: медиана 2755 мс, последен 1623 мс" text="Пълно показване: медиана 2755 мс, последно 2225 мс" text="Топъл старт · 1" text="Първи кадър: 
APP W/E lines (pids ['5585', '6790', '13240', '18439', '23811']): 417 in 89 groups
      22  W/Parcel: Expecting binder but got null!
      20  E/OpenGLRenderer: Unable to match the desired swap behavior.
      10  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
      10  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       8  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       7  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       7  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       6  W/BasicSecureTextField: 	at yo#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       6  W/BasicSecureTextField: 	at hd#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       6  W/BasicSecureTextField: 	at fz#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       6  W/BasicSecureTextField: 	at id#.H#(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       6  W/BasicSecureTextField: 	at id#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       6  W/BasicSecureTextField: 	at ty.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       6  W/BasicSecureTextField: 	at uy#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       6  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       6  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       5  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       5  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/Gralloc4: allocator #.x is not supported
       4  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       4  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       4  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       4  W/BasicSecureTextField: 	at da#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/BasicSecureTextField: 	at b#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/BasicSecureTextField: 	at b#.b(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/BasicSecureTextField: 	at a#.invoke(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
contact sheets: 20 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5585', '6790', '13240', '18439', '23811']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 92
   skipped-frames events=92 max=142 total=4136
== Davey: 277
   davey events=277 max=4675ms median=884ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 0 B
```

## tour38b

```
part tour38b; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4040 ms
## T-font130. All screens (1080x2400 px, density 420, font_scale 1.3, night no, app locale [bg-BG], rotation 0)
CAP font130/01-home: targets=13 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP font130/01b-home-2: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP font130/01c-home-3: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Последни новини · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта · Искам да прочета
CAP font130/01d-home-4: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития · Всички · 16
CAP font130/01e-search-12: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=38 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP font130/02-catalog: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=47 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP font130/02b-filters: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP font130/02c-filters-2: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Манипулатор за преместване с · продължаващо издание · патент/стандарт · нотно издание · Раздел (жанр) · Общ отдел
CAP font130/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP font130/02e-shelf: targets=47 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=14 | front: app | texts=50 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP font130/02f-shelf-2: targets=44 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=21 | front: app | texts=46 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP font130/02g-search-12: targets=21 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=42 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · ISBN
CAP font130/03-book: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP font130/03b-book-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | Библиографски данни · Издателство · Сиела, Русе · Година · 1972 · ISBN
CAP font130/03c-book-3: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | 1972 · ISBN · 9789546551135 · Език · български · Вид
CAP font130/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Златният полето · Още една · Към книгата · Затвори
CAP font130/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP font130/05b-my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Така ще изглежда картата ти · Вход · Добави читателска карта · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CAP font130/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP font130/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP font130/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP font130/08-news: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP font130/08b-news-2: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=23 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP font130/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP font130/09b-article-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=3 ellipsized=0 | Групата за народни танци при · Групата за народни танци при · Назад
CAP font130/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP font130/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP font130/10-calendar: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP font130/11-event: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP font130/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP font130/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP font130/12-more: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP font130/12b-more-2: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=22 ellipsized=0 | Групи, клубове, проекти · История и мисия · Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве
CAP font130/13-gallery: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 29 снимки · Фотодокументална изложба · 4 снимки · Фолклорна група „Явор“ · 6 снимки · Клуб за народни танци
CAP font130/14-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP font130/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP font130/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP font130/16-contacts: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP font130/16b-contacts-2: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg · Работно време · Понеделник: 08:00 – 16:30
CAP font130/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP font130/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP font130/18-settings: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP font130/18b-settings-2: targets=9 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=12 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP font130/18c-settings-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога
CAP font130/18d-settings-4: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ
CAP font130/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP font130/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP font130/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP font130/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP font130/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP font130/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP font130/18k-about: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP font130/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP font130/18m-diagnostics-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100) · Профил: чака · Студен старт · 2 · Първи кадър: медиана 3355 мс
CAP font130/19-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP font130/20-scanner: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
CAP font130/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR font130: A11Y AGG: 57 screens analysed, 0 dumps failed; totals MISSING=11 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=51
## NP-font130 (brief). New screens (1080x2400 px, density 420, font_scale 1.3, night no, app locale [bg-BG], rotation 0)
CAP font130/01-home: targets=13 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=22 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP font130/02-more: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP font130/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 14
CAP font130/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 14 → 6
EMB redo: 6 → 14
EMB save: text="Моите шевици" 
CAP font130/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP font130/np-01-list: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2701 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP font130/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.4
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2790 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP font130/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=225 std=29.4
CAP font130/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP font130/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1912–1918. Прекъсване от вой · Балканската война (1912) пре · 17 дек. 1922. Възстановяване · Възстановяване и официално у · Дейността се възобновява под · 1923–1924. Устав и държавно 
CAP font130/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 1934. Първият радиоапарат в  · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново · Дружество „Воля“ · Ново въздържателно дружество · 1947. Решение за собствена с
QUIZ ready after 2077 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Коя от тези книги е написана от Максим Якубовски?" text="Колекционерът" text="Робинята" text="Един ден в древен Рим" text="Прокудените" text="Избродирай отгов
CAP font130/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
CAP font130/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
CAP font130/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 2 s; fg android/com.android.internal.app.ChooserActivity
CAP font130/st-01: targets=9 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP font130/st-02: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език
CAP font130/st-03: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=14 ellipsized=0 | Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ
CAP font130/st-04: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP font130/11-event: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP font130: FATAL(any process)=0 of the app=0 ANR=0; app process 13421; logcat app errors: 0
## T-w320dp. All screens (1080x2400 px, density 540, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP w320dp/01-home: targets=12 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=18 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP w320dp/01b-home-2: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | 14 ноември 2026 · *Св. ап. Ф · Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Моите книги
CAP w320dp/01c-home-3: targets=15 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития · Всички · 16
CAP w320dp/01d-home-4: targets=13 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра · БИБЛИОТЕКА · БИБЛИОТЕКА
CAP w320dp/02-catalog: targets=18 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=36 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · 2446 резултата  ·  данни от 
CAP w320dp/02b-filters: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=16 ellipsized=0 | Манипулатор за преместване с · Филтри · Без филтри · Само налични · Вид документ · книга
CAP w320dp/02c-filters-2: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=2 | front: app | texts=15 ellipsized=0 | Манипулатор за преместване с · електронен документ · графично издание · аудиодокумент · продължаващо издание · патент/стандарт
CAP w320dp/02d-sort: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Най-подходящи  ✓ · Заглавие (А–Я) · Автор (А–Я) · Година (нови първо) · Година (стари първо) · Нови постъпления
CAP w320dp/02e-shelf: targets=30 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=7 | front: app | texts=32 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · 2446 резултата  ·  данни от 
CAP w320dp/02f-shelf-2: targets=34 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=13 | front: app | texts=35 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · Алтъна, Никова, Ивелина
CAP w320dp/02g-search-12: targets=19 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | 12 · Изчисти · Всичко · Заглавие · Автор · 12 -те вятъра
CAP w320dp/03-book: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Спомен за здрача · Веска Пенчева · Не е на рафта · Наличност към 09.11.2026 · Искам да прочета · Попитай библиотеката
CAP w320dp/03b-book-2: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | Кажи ми, когато е на рафта · Библиографски данни · Издателство · Сиела, Русе · Година · 1972
CAP w320dp/03c-book-3: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Година · 1972 · ISBN · 9789546551135 · Език · български
CAP w320dp/04-surprise: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Предложение: Пътят на мечтит · Още една · Към книгата · Затвори
CAP w320dp/05-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Вход · БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ
CAP w320dp/05b-my-2: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | БИБЛИОТЕКА · Читателска карта · Въведена от теб карта · Моите книги · Заети книги и срокове за връ · Искам да прочета
CAP w320dp/06-card: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=14 ellipsized=0 | ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345 · Покажи картата
CAP w320dp/06b-card-full: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Затвори · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · — · Читателски № TEST-12345 · Баркод на карта TEST-12345
CAP w320dp/07-want: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
CAP w320dp/08-news: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP w320dp/08b-news-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP w320dp/09-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Групата за народни танци при · СЪБИТИЕ  ·  25 юли 2026 · Групата за народни танци при · Групата за народни танци при · Режим за четене · Аа
CAP w320dp/09b-article-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=3 ellipsized=0 | Групата за народни танци при · Групата за народни танци при · Назад
CAP w320dp/09c-reader-sheet: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Затваряне на таблицата · Манипулатор за преместване с · Режим за четене · Размер на шрифта · Аа · Малък шрифт
CAP w320dp/09d-share-sheet: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP w320dp/10-calendar: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
CAP w320dp/11-event: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Назад · Сподели
CAP w320dp/11b-event-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Панайот Пипков · Ден на семе · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · 155 г. от рождението на Пана · Добави в календара
CAP w320dp/11c-event-share: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=1 | front: app | texts=7 ellipsized=1 [Карта с QR код — за Instagram,] | Затваряне на таблицата · Манипулатор за преместване с · Сподели · Сподели като картинка · Карта с QR код — за Instagra · Сподели връзката
CAP w320dp/12-more: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP w320dp/12b-more-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Групи, клубове, проекти · За читалището · История и мисия · Галерия · Снимки от сайта · Документи и архив
CAP w320dp/13-gallery: targets=3 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | 29 снимки · Фотодокументална изложба · Назад · Галерия
CAP w320dp/14-album: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Сградата на читалището в Яво · Фолклорен състав при читалищ · Хорът на сцена · 70-те години · Сценичен ансамбъл Яворски хл · Фолклорна група Явор
CAP w320dp/15-viewer: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сподели · Отвори в сайта · За снимката
CAP w320dp/15b-viewer-info: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Сградата на читалището в Яво · Затвори · 1 / 29  ·  Сградата на читал · Сградата на читалището в Яво · Сподели · Отвори в сайта
CAP w320dp/16-contacts: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | НЧ „Васил Левски – 1922“ · Изпрати имейл на читалището · Имейл · Покажи читалището на картата · Карта · Отвори сайта на читалището
CAP w320dp/16b-contacts-2: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | пл. 9-ти Септември 3, с. Яво · Лица за контакт · Председател: Пламен Христов · Секретар: Даниела Цвяткова · Имейл · chitalishte_yavorec@abv.bg
CAP w320dp/17-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP w320dp/17b-documents-2: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Документи · Исторически публикации · Декларации · 14.05.2026 · 4. · Декларация за частни интерес · Декларацията за частни интер · Отвори с приложение за PDF: 
CAP w320dp/18-settings: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP w320dp/18b-settings-2: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Злато и жито · Зелено и сребро · Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета
CAP w320dp/18c-settings-3: targets=7 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=12 ellipsized=0 | Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP w320dp/18d-settings-4: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
CAP w320dp/18e-text-size: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Размер на текста · Умножава размера на шрифта о · Малък · Стандартен · Голям · Много голям
CAP w320dp/18f-language: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Език · Както системата · Български · English · Затвори
CAP w320dp/18g-theme: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Тема · Както системата · Светла · Тъмна · Затвори
CAP w320dp/18h-notifications: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Известията се подготвят на с · Съобщения · Съобщения, изпратени от чита · Новини · Нова публикация в сайта · Събития
CAP w320dp/18i-privacy-policy: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Виж и: Политика за поверител · Последна актуализация: 12 но · Кой обработва данните · Приложението „Читалище Яворе · Принцип · Приложението събира възможно
CAP w320dp/18j-terms: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Предназначение · Приложението предоставя инфо · Съдържание · Новините, събитията, снимкит · Наличност на книгите · Наличността в каталога е инф
CAP w320dp/18k-about: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Читалище Яворец · НЧ „Васил Левски – 1922“ · Версия 1.2.38 (1510100) · Официалното мобилно приложен · Откъде идват данните · Новини, събития, снимки, док
CAP w320dp/18l-diagnostics: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Тук приложението записва тех · Сподели · Изчисти · Бързодействие · Времената на стартиране и пл · Версия 1.2.38 (1510100)
CAP w320dp/18m-diagnostics-2: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Времената на стартиране и пл · Версия 1.2.38 (1510100) · Профил: чака · Студен старт · 4 · Първи кадър: медиана 3443 мс · Пълно показване: медиана 388
CAP w320dp/19-login: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP w320dp/20-scanner: targets=3 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Назад · Сканиране
CAP w320dp/20b-scan-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Въведи номера на ръка · Скенерът на баркодове идва с · ISBN-ът (10 или 13 цифри под · ISBN или инвентарен номер · Отказ · Търси
TOUR w320dp: A11Y AGG: 56 screens analysed, 0 dumps failed; totals MISSING=16 DOUBLE=0 SAME-LABEL=5 SMALL(<48dp)=32
## NP-w320dp (brief). New screens (1080x2400 px, density 540, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP w320dp/01-home: targets=12 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=18 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP w320dp/02-more: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP w320dp/em-01-open: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 13 → after 6 taps + a stroke 1
CAP w320dp/em-02-stitched: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 1 → 9
EMB redo: 9 → 1
EMB save: text="Моите шевици" text="Шевицата е запазена" 
CAP w320dp/em-06-after-save: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser NOT open after 12 s; fg org.chyavorec.app/org.chyavorec.app.MainActivity
EMB end: front app
CAP w320dp/np-01-list: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2704 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP w320dp/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=225 std=29.2
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2506 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP w320dp/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=225 std=29.2
CAP w320dp/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP w320dp/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP w320dp/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 1923–1924. Устав и държавно  · Уставът е приет на 22 януари · 1927. Въздържателно дружеств · Въздържателно дружество „Бор · Основано от активни читалищн · 1934. Първият радиоапарат в 
QUIZ ready after 2022 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" 
CAP w320dp/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
CAP w320dp/qz-01-answered: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=19 ellipsized=0 | Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа
QUIZ q1: no next button: text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="ПРИЛОЖЕНИЕТО" text="Настройки" text="Политика за поверителност" text="Добави на началния екран" text="За приложението" text="Още" content-
CAP w320dp/qz-06-result: targets=13 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=16 ellipsized=0 | Лента на времето · Историята на читалището от 1 · Познай Яворец · Пет въпроса всеки ден · ПРИЛОЖЕНИЕТО · Настройки
QUIZ result: text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="ПРИЛОЖЕНИЕТО" text="Настройки" text="Политика за поверителност" text="Добави на началния екран" text="За приложението" text="Още" content-desc="Търсене" text="Начало" 
QUIZ: no share button: text="Лента на времето" text="Историята на читалището от 1900 г." text="Познай Яворец" text="Пет въпроса всеки ден" text="ПРИЛОЖЕНИЕТО" text="Настройки" text="Политика за поверителност" text="Добави на началния екран" text="За приложението"
CAP w320dp/st-01: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP w320dp/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Злато и жито · Зелено и сребро · Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета
CAP w320dp/st-03: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
CAP w320dp/st-04: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP w320dp/11-event: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=7 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Назад · Сподели
NP w320dp: FATAL(any process)=0 of the app=0 ANR=0; app process 22273; logcat app errors: 0
CAP tablet-portrait/tab-p-more: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=36 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-portrait/tab-p-more-KnowYavorets: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
HUB tab-p after 'Познай Яворец|Know Yavorets': back arrow yes; tabs hidden
CAP tablet-portrait/tab-p-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Вход
CAP tablet-portrait/tab-p-my-Wanttoread: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
## NP-tablet-portrait (brief). New screens (1600x2560 px, density 320, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP tablet-portrait/01-home: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=46 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP tablet-portrait/02-more: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=36 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-portrait/em-01-open: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 14
CAP tablet-portrait/em-02-stitched: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 14 → 6
EMB redo: 6 → 14
EMB save: text="Моите шевици" 
CAP tablet-portrait/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP tablet-portrait/np-01-list: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=28 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2666 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP tablet-portrait/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=227 std=36.8
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2565 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP tablet-portrait/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER offline page content: mean=227 std=36.8
CAP tablet-portrait/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP tablet-portrait/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново · Дружество „Воля“ · Ново въздържателно дружество
CAP tablet-portrait/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | 1936. Дружество „Воля“. Ново · Ново въздържателно дружество · 1947. Решение за собствена с · Решение за собствена сграда · Започва отчуждаване на терен · 17 март 1958. Първа копка. С
QUIZ ready after 2012 ms: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" 
CAP tablet-portrait/qz-01-q1: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
CAP tablet-portrait/qz-01-answered: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=36 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
QUIZ q1: no next button: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" 
CAP tablet-portrait/qz-06-result: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=36 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
QUIZ result: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" 
QUIZ: no share button: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" 
CAP tablet-portrait/st-01: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP tablet-portrait/st-02: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=0 | Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво
CAP tablet-portrait/st-03: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38 · ДАННИ
CAP tablet-portrait/st-04: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия · Версия 1.2.38 · ДАННИ
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP tablet-portrait/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Събота, 21 ноември 2026, сле · Добави в календара · Напомни ми · Маршрут · Виж публикацията · Панайот Пипков · Ден на семе
NP tablet-portrait: FATAL(any process)=0 of the app=0 ANR=0; app process 26544; logcat app errors: 0
CAP tablet-landscape/tab-l-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-landscape/tab-l-more-Gallery: targets=24 MISSING=5 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=37 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
HUB tab-l after '=Галерия|=Gallery': back arrow yes; tabs visible
CAP tablet-landscape/tab-l-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Вход
CAP tablet-landscape/tab-l-my-Wanttoread: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
## NP-tablet-landscape (brief). New screens (2560x1600 px, density 320, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP tablet-landscape/01-home: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=1 [Търси книга, новина, събитие…] | Начало · Каталог · Моето · Новини · Още · НЧ „Васил Левски – 1922“
CAP tablet-landscape/02-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
MORE: tile 'Извези шевица|Embroider a pattern' not found: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="С
MORE: tile 'Старият вестник|The old newspaper' not found: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="С
MORE: tile 'Лента на времето|Timeline' not found: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="С
MORE: tile 'Познай Яворец|Know Yavorets' not found: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="С
WARN: Settings not found
CAP tablet-landscape/11-event: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · Добави в календара · Напомни ми · Маршрут · Виж публикацията · 80 г. от създаването на ЮНЕС
NP tablet-landscape: FATAL(any process)=0 of the app=0 ANR=0; app process 30424; logcat app errors: 0
CAP tablet-landscape-dark/tab-ld-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
CAP tablet-landscape-dark/tab-ld-more-Gallery: targets=24 MISSING=5 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=37 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · ЧИТАЛИЩЕТО
HUB tab-ld after '=Галерия|=Gallery': back arrow yes; tabs visible
CAP tablet-landscape-dark/tab-ld-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Начало · Каталог · Моето · Новини · Още · Вход
CAP tablet-landscape-dark/tab-ld-my-Wanttoread: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=4 ellipsized=0 | Списъкът е празен · Отбележи книга с „Искам да п · Назад · Искам да прочета
## TA. Accessibility over the tours
A11Y AGG: 64 screens analysed, 0 dumps failed; totals MISSING=19 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=9
== MISSING: 15 distinct
   View@[192,1379][647,1480]  [3 screens: 02-more, tab-ld-more, tab-ld-more-Gallery]
   View@[671,1379][1127,1480]  [3 screens: 02-more, tab-ld-more, tab-ld-more-Gallery]
   View@[823,2100][1069,2127]  [1 screens: 01-home]
   View@[42,2324][1038,2400]  [1 screens: st-01]
   View@[84,296][370,473]  [1 screens: st-02]
   View@[396,296][683,473]  [1 screens: st-02]
   View@[1193,1385][1622,1481]  [1 screens: tab-ld-more-Gallery]
   View@[1646,1385][2075,1481]  [1 screens: tab-ld-more-Gallery]
   View@[2099,1385][2528,1481]  [1 screens: tab-ld-more-Gallery]
   View@[32,265][1408,280]  [1 screens: st-02]
   View@[54,1958][540,2049]  [1 screens: 01-home]
   View@[540,1958][1026,2049]  [1 screens: 01-home]
   View@[54,344][519,464]  [1 screens: qz-01-answered]
   View@[54,344][519,353]  [1 screens: qz-06-result]
   View@[560,344][1026,353]  [1 screens: qz-06-result]
== SMALL: 3 distinct
   ''  [4 screens: 01-home, qz-01-answered, qz-06-result, st-02]
   'Български'  [1 screens: st-03]
   'Устав, отчети, юбилейният вест'  [1 screens: qz-01-answered]
== SAME-LABEL: 0 distinct
== DOUBLE: 0 distinct
TOUR38 part: FATAL lines 0, app FATAL 0, ANR 0
contact sheets: 24 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 10 | Първи кадър: медиана 1868 мс, последен 893 мс | Пълно показване: медиана 2327 мс, последно 1410 мс | Топъл старт · 1 | Първи кадър: медиана 502 мс, последен 502 мс | Насечени кадри по екрани | embro
DIAG SCREEN (scrolled): text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 10" text="Първи кадър: медиана 1868 мс, последен 893 мс" text="Пълно показване: медиана 2327 мс, последно 1410 мс" text="Топъл старт · 1" text="Първи кадър: медиана 502 мс, последен 502 мс" text="Насечени кадри по екрани" text="embroidery: 5,4 % от 93 кадъра · най-бавен 796 мс" text="CPU време на кадъра (без видеокартата): средно 330 мс, най-бавен 1078 мс" text="more: 5,4 % от 149 кадъра · най-бавен 316 мс" text="CPU време на кадъра (без видеокартата): средно 253 мс, най-бавен 646 мс" text="quiz: 2,2 % от 135 кадъра · най-бавен 643 мс" text="CPU време на кадъра (без видеокартата): средно 326 мс, най-бавен 664 мс" text="h
APP W/E lines (pids ['5344', '6352', '13421', '17304', '22273', '25893', '26544', '29740', '30424', '32129']): 458 in 91 groups
      33  E/OpenGLRenderer: Unable to match the desired swap behavior.
      32  W/Parcel: Expecting binder but got null!
      12  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      12  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      10  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      10  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      10  W/Gralloc4: allocator #.x is not supported
       9  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       8  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       8  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       5  W/BasicSecureTextField: 	at yo#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       5  W/BasicSecureTextField: 	at hd#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       5  W/BasicSecureTextField: 	at fz#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       5  W/BasicSecureTextField: 	at id#.H#(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       5  W/BasicSecureTextField: 	at id#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       5  W/BasicSecureTextField: 	at ty.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       5  W/BasicSecureTextField: 	at uy#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       4  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       4  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       4  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       4  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       4  W/BasicSecureTextField: 	at da#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/BasicSecureTextField: 	at b#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/BasicSecureTextField: 	at b#.b(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/BasicSecureTextField: 	at a#.invoke(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
contact sheets: 25 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5344', '6352', '13421', '17304', '22273', '25893', '26544', '29740', '30424', '32129']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 40
   skipped-frames events=40 max=167 total=1962
== Davey: 145
   davey events=145 max=3584ms median=825ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 0 B
```

## ui38

```
part ui38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4131 ms
## U1. Tabs with the „Бод“ icons: every tab, light and dark (fresh install, no card)
CAP tabs-light/home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP tabs-light/catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP tabs-light/my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP tabs-light/news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP tabs-light/more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP tabs-light/more-2: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа · Старият вестник
CAP tabs-light/more-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=0 | Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа · Старият вестник
CAP tabs-light/my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Примерна читателска карта · Читателски № 19220001 · Баркод на карта 19220001 · Така ще изглежда картата ти · Вход · Добави читателска карта
MY guest (light): sample card hint SHOWN; content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" text="Така ще изглежда картата ти" text="Вход" text="Добави читателска карта" text="БИБЛИОТЕКА" text="Читателска 
CAP tabs-dark/home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP tabs-dark/catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP tabs-dark/my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
CAP tabs-dark/news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP tabs-dark/more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP tabs-dark/more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Галерия · Снимки от сайта · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен
CAP tabs-dark/more-3: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=0 | Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ · Извези шевица · Бод по бод, като на кърпа · Старият вестник
CAP tabs-dark/my-2: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | Примерна читателска карта · Читателски № 19220001 · Баркод на карта 19220001 · Така ще изглежда картата ти · Вход · Добави читателска карта
MY guest (dark): sample card hint SHOWN; content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" text="Така ще изглежда картата ти" text="Вход" text="Добави читателска карта" text="БИБЛИОТЕКА" text="Читателска 
## U2. Login screen (guest), the sample card on it
LOGIN: content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" text="Така ще изглежда картата ти" text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" c
## U3. Quiz: pass 1 (first options: right/wrong marks, the band), then data cleared and pass 2 with the learned answers (5/5, confetti)
QUIZ1 ready after 2039 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Коя от тези книги е написана от Максим Якубовски?" text="Колекционерът" text="Робинята" text="Един ден в древен Рим" t
CAP ui38/qz1-q0: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ1 q1 options: Колекционерът|Робинята|Един ден в древен Рим|Прокудените|
CAP ui38/qz1-q1-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ1 q1: WRONG	Робинята; band: content-desc="0 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q2 options: Проект „Глобални библиотеки“|Решение за собствена сграда|Възстановяване и официално учредяване|Устав и държавно признание|
CAP ui38/qz1-q2-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Проект „Глобални библиотеки“ · Решение за собствена сграда · Възстановяване и официално у
QUIZ1 q2: WRONG	Решение за собствена сграда; band: content-desc="0 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q3 options: Трудни години — устояване|Първият радиоапарат в селото|Първа копка|Проект „Глобални библиотеки“|
CAP ui38/qz1-q3-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 3 отговорени, общ · Въпрос 3 от 5 · Какво се случва в читалището · Трудни години — устояване · Първият радиоапарат в селото · Първа копка
QUIZ1 q3: WRONG	Първа копка; band: content-desc="0 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q4 options: Устав и държавно признание|Дружество „Воля“|Трудни години — устояване|Прекъсване от войните|
CAP ui38/qz1-q4-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 4 отговорени, общ · Въпрос 4 от 5 · Какво се случва в читалището · Устав и държавно признание · Дружество „Воля“ · Трудни години — устояване
QUIZ1 q4: WRONG	Дружество „Воля“; band: content-desc="0 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q5 options: Очи с цвят на вода|Загадката на Мари Роже|Ритуал в смъртта|Дързост и мечти|
CAP ui38/qz1-q5-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 5 отговорени, общ · Въпрос 5 от 5 · Коя от тези книги е написана · Очи с цвят на вода · Загадката на Мари Роже · Ритуал в смъртта
QUIZ1 q5: WRONG	Загадката на Мари Роже; band: content-desc="0 верни от 5 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
TREC quiz-result-1: 31 frames; frames 31; changed between consecutive frames: 1; max diff 24.6; frames=31 motion-start=#15 moving-frames=1 (span 1) kept #14..#25; diffs: 0 24 0 0 0 0 0 0 0 0 0 0
CAP ui38/qz1-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ1 result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ answers learned: Робинята|Решение за собствена сграда|Първа копка|Дружество „Воля“|Загадката на Мари Роже|
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
QUIZ2 ready after 1993 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Коя от тези книги е написана от Максим Якубовски?" text="Колекционерът" text="Робинята" text="Един ден в древен Рим" t
CAP ui38/qz2-q0: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ2 q1 options: Колекционерът|Робинята|Един ден в древен Рим|Прокудените|
CAP ui38/qz2-q1-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 1 верни от 1 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ2 q1: RIGHT; band: content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q2 options: Проект „Глобални библиотеки“|Решение за собствена сграда|Възстановяване и официално учредяване|Устав и държавно признание|
CAP ui38/qz2-q2-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 2 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Проект „Глобални библиотеки“ · Решение за собствена сграда · Възстановяване и официално у
QUIZ2 q2: RIGHT; band: content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q3 options: Трудни години — устояване|Първият радиоапарат в селото|Първа копка|Проект „Глобални библиотеки“|
CAP ui38/qz2-q3-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 3 верни от 3 отговорени, общ · Въпрос 3 от 5 · Какво се случва в читалището · Трудни години — устояване · Първият радиоапарат в селото · Първа копка
QUIZ2 q3: RIGHT; band: content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q4 options: Устав и държавно признание|Дружество „Воля“|Трудни години — устояване|Прекъсване от войните|
CAP ui38/qz2-q4-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 4 верни от 4 отговорени, общ · Въпрос 4 от 5 · Какво се случва в читалището · Устав и държавно признание · Дружество „Воля“ · Трудни години — устояване
QUIZ2 q4: RIGHT; band: content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q5 options: Очи с цвят на вода|Загадката на Мари Роже|Ритуал в смъртта|Дързост и мечти|
TREC quiz-band-5of5: 55 frames; frames 55; changed between consecutive frames: 1; max diff 9.6; frames=55 motion-start=#18 moving-frames=1 (span 1) kept #17..#28; diffs: 0 10 0 0 0 0 0 0 0 0 0 0
CAP ui38/qz2-q5-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 5 верни от 5 отговорени, общ · Въпрос 5 от 5 · Коя от тези книги е написана · Очи с цвят на вода · Загадката на Мари Роже · Ритуал в смъртта
QUIZ2 q5: RIGHT; band: content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
TREC quiz-result-2: 21 frames; frames 21; changed between consecutive frames: 1; max diff 23.2; frames=21 motion-start=#18 moving-frames=1 (span 1) kept #9..#20; diffs: 0 0 0 0 0 0 0 0 0 23 0 0
CAP ui38/qz2-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Отлично! Познаваш Яворец! · 5/5 · 5 верни от 5 · Шевицата е цяла! · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ2 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## U4. Timeline: the decade scrubber while scrolling (recorded), after the scroll stops (0, 1.5, 4 s)
CAP ui38/tl-00: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
TREC timeline-scroll: 58 frames; frames 58; changed between consecutive frames: 20; max diff 15.8; frames=58 motion-start=#17 moving-frames=17 (span 39) kept #16..#27; diffs: 0 14 14 13 0 0 14 0 14 14 0 1
TIMELINE decades: 
## U5. Event leaf: add to calendar (the helper catches the intent), back → the stub tears off (recorded)
CAP ui38/ev-01: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CAP ui38/ev-02-button: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CALENDAR: fg android/com.android.internal.app.ResolverActivity; helper: 
TREC event-tear: 26 frames; frames 26; changed between consecutive frames: 4; max diff 47.4; frames=26 motion-start=#14 moving-frames=4 (span 8) kept #13..#24; diffs: 0 2 0 0 0 46 14 0 46 0 0 0
CAP ui38/ev-04-after: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
EVENT after return: ''; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="16" text="НОЕ" 
CAP ui38/ev-05-after-2: targets=19 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=4 | front: app | texts=43 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
## U6. Search (snippets/plates), settings in cards, the end strip
CAP ui38/search-2025: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=2 […561 Жители (2025) 10 км от Се; Отчетът за дейността през 2025] | Всички · Книги · Новини · Събития · Страници · КНИГИ
CAP ui38/search-2025-b: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=2 […561 Жители (2025) 10 км от Се; Отчетът за дейността през 2025] | Всички · Книги · Новини · Събития · Страници · КНИГИ
SEARCH 2025: text="Всички" text="Книги" text="Новини" text="Събития" text="Страници" text="КНИГИ" text="Изгубеният изгрева" text="Красимир Маринов" text="налична" text="  ·  " text="2025 · книга · справочен" text="СТРАНИЦИ" text="село Яворец - история" text="…561 Жители (2025) 10 км от Севлиево 18 км от Габрово 702 м Витата стена Въведение Сърцето на Централния Балкан Село Яворец е на 10 км…" text="Годишни отч
CAP ui38/homesearch-12: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=40 ellipsized=0 | 12 · Лента за търсене · Назад · Изчисти · Всички · Книги
CAP ui38/set-1: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
CAP ui38/set-2: targets=12 MISSING=4 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=18 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP ui38/set-3: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=18 ellipsized=0 | Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“
CAP ui38/set-4: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Избери известията · Заемания, събития, съобщения · ОБНОВЛЕНИЯ · Автоматично обновяване · Проверка при отваряне и два  · Провери за нова версия
CAP ui38/set-5: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Корици от интернет · Корици по ISBN от covers.ope · Корици само по Wi-Fi · По мобилни данни — само вече · Изчисти временните данни · Новините, събитията, каталог
CAP ui38/set-6: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Новините, събитията, каталог · ПОВЕРИТЕЛНОСТ · Искай отключване за личните  · Не може да се включи: телефо · ИНФОРМАЦИЯ · Политика за поверителност
CAP ui38/set-end: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Новините, събитията, каталог · ПОВЕРИТЕЛНОСТ · Искай отключване за личните  · Не може да се включи: телефо · ИНФОРМАЦИЯ · Политика за поверителност
CAP ui38/news-end: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=44 ellipsized=0 | Новини · Календар · Абонирай се за календара · Изглед календар · Всички · Библиотека
## U7. Feast greeting as an icon (6 Dec), seasonal thread: 3 March (twisted red-white), 25 Dec (Christmas star), 1 May 2027 (Easter, green)
device date now: Sun Dec  6 10:00:06 UTC 2026
CAP season-nikulden/home: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=30 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Честит Никулден! · Ден на рибарите и моряците.  · Изпрати честитка
CAP season-nikulden/home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта
CAP season-nikulden/home-3: targets=17 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=31 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 6 · ДЕК · Никулден
CAP season-nikulden/more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP season-nikulden/my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
SEASON nikulden: greeting/feast texts: 
device date now: Fri Dec 25 10:00:15 UTC 2026
CAP season-christmas/home: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Честито Рождество Христово! · Весела Коледа на всички от ч · Изпрати честитка
CAP season-christmas/home-2: targets=18 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Лента за търсене · Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай
CAP season-christmas/home-3: targets=16 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=30 ellipsized=0 | КАЛЕНДАР · Предстоящи събития · Всички · 6 · ЯНУ · Богоявление
CAP season-christmas/more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP season-christmas/my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
SEASON christmas: greeting/feast texts: 
device date now: Wed Mar  3 10:00:10 UTC 2027
CAP season-march/home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · 3 март – Ден на Освобождение · Национален празник на Българ · Към книгите
CAP season-march/home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=34 ellipsized=0 | Няма интернет връзка. Показв · Последна синхронизация: 25.1 · Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026
CAP season-march/home-3: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Сканирай · Читателска карта · Искам да прочета · Контакти · КАЛЕНДАР · Предстоящи събития
CAP season-march/more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP season-march/my: targets=11 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
SEASON march: greeting/feast texts: 
device date now: Sat May  1 10:00:08 UTC 2027
CAP season-easter/home: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=27 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · 1 май – Ден на труда · Честит празник на всички, ко · Изпрати честитка
CAP season-easter/home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта
CAP season-easter/home-3: targets=18 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Читателска карта · Искам да прочета · КАЛЕНДАР · Предстоящи събития · Всички · 6
CAP season-easter/more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP season-easter/my: targets=11 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
SEASON easter: greeting/feast texts: 
device date now: Fri Dec 25 10:00:07 UTC 2026
CAP season-christmas-dark/home: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=29 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Честито Рождество Христово! · Весела Коледа на всички от ч · Изпрати честитка
CAP season-christmas-dark/home-2: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=32 ellipsized=0 | Последни новини · БИБЛИОТЕКА · Четем заедно – мъдростта на  · 11 ноември 2026 · Сканирай · Читателска карта
CAP season-christmas-dark/home-3: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=31 ellipsized=0 | 6 · ЯНУ · Богоявление · Традиционен народен и религи · НАРОДЕН ОБИЧАЙ · 7
CAP season-christmas-dark/more: targets=14 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP season-christmas-dark/my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
SEASON christmas-dark: greeting/feast texts: 
device date now: Sat Nov 14 14:21:29 UTC 2026
UI38 part: FATAL(any process)=0 of the app=0 ANR=0
A11Y AGG: 71 screens analysed, 0 dumps failed; totals MISSING=19 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=20
== MISSING: 17 distinct
   View@[903,0][1029,28]  [1 screens: home-2]
   View@[291,0][540,1]  [1 screens: home-3]
   View@[540,0][789,1]  [1 screens: home-3]
   View@[871,2101][1069,2127]  [1 screens: home]
   View@[42,0][291,23]  [1 screens: home-3]
   View@[789,0][1038,23]  [1 screens: home-3]
   View@[42,1929][524,2064]  [1 screens: my]
   View@[556,1929][1038,2064]  [1 screens: my]
   View@[42,2107][378,2127]  [1 screens: home-3]
   View@[415,2107][751,2127]  [1 screens: home-3]
   View@[788,2107][1080,2127]  [1 screens: home-3]
   View@[42,2062][1038,2127]  [1 screens: ev-05-after-2]
   View@[42,2320][1038,2400]  [1 screens: set-1]
   View@[84,296][370,320]  [1 screens: set-2]
   View@[396,296][683,320]  [1 screens: set-2]
   View@[709,296][996,320]  [1 screens: set-2]
   View@[42,2314][1038,2400]  [1 screens: set-2]
== SMALL: 9 distinct
   ''  [4 screens: ev-05-after-2, home, home-3, set-2]
   'Всички'  [2 screens: ev-05-after-2, news-end]
   'Библиотека'  [2 screens: ev-05-after-2, news-end]
   'Читалище'  [2 screens: ev-05-after-2, news-end]
   '6 Богоявление'  [1 screens: home]
   'Далечният морето'  [1 screens: home-3]
   'Дългият пътя'  [1 screens: home-3]
   '6 Никулден'  [1 screens: home]
   'Контакти Адрес, телефон, место'  [1 screens: more-3]
== SAME-LABEL: 0 distinct
contact sheets: 19 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 6 | Първи кадър: медиана 3287 мс, последен 1790 мс | Пълно показване: медиана 3653 мс, последно 2933 мс | Топъл старт · 1 | Първи кадър: медиана 907 мс, последен 907 мс | Насечени кадри по екрани | event
DIAG SCREEN (scrolled): text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 6" text="Първи кадър: медиана 3287 мс, последен 1790 мс" text="Пълно показване: медиана 3653 мс, последно 2933 мс" text="Топъл старт · 1" text="Първи кадър: медиана 907 мс, последен 907 мс" text="Насечени кадри по екрани" text="event: 5,6 % от 36 кадъра · най-бавен 280 мс" text="CPU време на кадъра (без видеокартата): средно 326 мс, най-бавен 648 мс" text="more: 4,1 % от 74 кадъра · най-бавен 241 мс" text="CPU време на кадъра (без видеокартата): средно 233 мс, най-бавен 528 мс" text="quiz: 3,7 % от 135 кадъра · най-бавен 363 мс" text="CPU време на кадъра (без видеокартата): средно 348 мс, най-бавен 819 мс" text="search: 
APP W/E lines (pids ['5626', '10469', '14414', '16003', '16891', '17786', '18634']): 224 in 93 groups
       9  E/OpenGLRenderer: Unable to match the desired swap behavior.
       8  W/Parcel: Expecting binder but got null!
       7  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       7  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       7  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       7  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       7  W/Gralloc4: allocator #.x is not supported
       4  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       4  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at da#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at b#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at b#.b(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at a#.invoke(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at mp#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at mp#.invoke(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at py#.r(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at yr#.b(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at b#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at lb#.f(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at mp#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at mp#.f(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
contact sheets: 20 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5626', '10469', '14414', '16003', '16891', '17786', '18634']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 32
   skipped-frames events=32 max=113 total=1728
== Davey: 53
   davey events=53 max=3836ms median=1006ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'SIGNAL': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 174 B
```

## ui38b

```
part ui38b; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install: NO catalogue results
## UB1. Event leaf: „Добави в календара“ → the helper („Just once“) → back: the stub tears off
CAP ui38b/ev-01: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
CALENDAR: fg org.pc.helper/org.pc.helper.Catch; helper: 11-14 15:32:47.569  8896  8896 I PCCATCH : action=android.intent.action.INSERT data=content://com.android.calendar/events | description=Организацията на ООН за образование, наука и култура е основана на 16 ноември 1946 г.
RECWIN event-tear (adb, +0s for 2.4s at 8 fps): 17 frames; frames 17; changed between consecutive frames: 1; max diff 0.8
CAP ui38b/ev-04-after: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
EVENT after return: 'content-desc="Понеделник, 16 ноември 2026, след 2 дни, Добавено в календара"'; on the event screen yes
CAP ui38b/ev-05-rest: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
## UB2. Quiz 5/5: frames 0.8–2.6 s after the last answer (stitch, close, confetti)
QUIZ1 ready after 3122 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Коя от тези книги е написана от Максим Якубовски?" text="Колекционерът" text="Робинята" text="Един ден в древен Рим" t
CAP ui38b/qz1-q0: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ1 q1 options: Колекционерът|Робинята|Един ден в древен Рим|Прокудените|
CAP ui38b/qz1-q1-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ1 q1: WRONG	Робинята; band: content-desc="0 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q2 options: Проект „Глобални библиотеки“|Решение за собствена сграда|Възстановяване и официално учредяване|Устав и държавно признание|
CAP ui38b/qz1-q2-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Проект „Глобални библиотеки“ · Решение за собствена сграда · Възстановяване и официално у
QUIZ1 q2: WRONG	Решение за собствена сграда; band: content-desc="0 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q3 options: Трудни години — устояване|Първият радиоапарат в селото|Първа копка|Проект „Глобални библиотеки“|
CAP ui38b/qz1-q3-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 3 отговорени, общ · Въпрос 3 от 5 · Какво се случва в читалището · Трудни години — устояване · Първият радиоапарат в селото · Първа копка
QUIZ1 q3: WRONG	Първа копка; band: content-desc="0 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q4 options: Устав и държавно признание|Дружество „Воля“|Трудни години — устояване|Прекъсване от войните|
CAP ui38b/qz1-q4-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=14 ellipsized=0 | 0 верни от 4 отговорени, общ · Въпрос 4 от 5 · Какво се случва в читалището · Устав и държавно признание · Дружество „Воля“ · Трудни години — устояване
QUIZ1 q4: WRONG	Дружество „Воля“; band: content-desc="0 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ1 q5 options: Очи с цвят на вода|Загадката на Мари Роже|Ритуал в смъртта|Дързост и мечти|
CAP ui38b/qz1-q5-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 5 отговорени, общ · Въпрос 5 от 5 · Коя от тези книги е написана · Очи с цвят на вода · Загадката на Мари Роже · Ритуал в смъртта
QUIZ1 q5: WRONG	Загадката на Мари Роже; band: content-desc="0 верни от 5 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
TREC quiz-result-1: 32 frames; frames 32; changed between consecutive frames: 1; max diff 24.6; frames=32 motion-start=#16 moving-frames=1 (span 1) kept #15..#26; diffs: 0 24 0 0 0 0 0 0 0 0 0 0
CAP ui38b/qz1-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ1 result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ answers learned: Робинята|Решение за собствена сграда|Първа копка|Дружество „Воля“|Загадката на Мари Роже|
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
QUIZ2 ready after 1996 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Коя от тези книги е написана от Максим Якубовски?" text="Колекционерът" text="Робинята" text="Един ден в древен Рим" t
CAP ui38b/qz2-q0: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ2 q1 options: Колекционерът|Робинята|Един ден в древен Рим|Прокудените|
CAP ui38b/qz2-q1-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 1 верни от 1 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
QUIZ2 q1: RIGHT; band: content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q2 options: Проект „Глобални библиотеки“|Решение за собствена сграда|Възстановяване и официално учредяване|Устав и държавно признание|
CAP ui38b/qz2-q2-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 2 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Проект „Глобални библиотеки“ · Решение за собствена сграда · Възстановяване и официално у
QUIZ2 q2: RIGHT; band: content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q3 options: Трудни години — устояване|Първият радиоапарат в селото|Първа копка|Проект „Глобални библиотеки“|
CAP ui38b/qz2-q3-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 3 верни от 3 отговорени, общ · Въпрос 3 от 5 · Какво се случва в читалището · Трудни години — устояване · Първият радиоапарат в селото · Първа копка
QUIZ2 q3: RIGHT; band: content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q4 options: Устав и държавно признание|Дружество „Воля“|Трудни години — устояване|Прекъсване от войните|
CAP ui38b/qz2-q4-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 4 верни от 4 отговорени, общ · Въпрос 4 от 5 · Какво се случва в читалището · Устав и държавно признание · Дружество „Воля“ · Трудни години — устояване
QUIZ2 q4: RIGHT; band: content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ2 q5 options: Очи с цвят на вода|Загадката на Мари Роже|Ритуал в смъртта|Дързост и мечти|
RECWIN quiz-5of5 (adb, +0.6s for 2.4s at 8 fps): 20 frames; frames 20; changed between consecutive frames: 0; max diff 0.5
CAP ui38b/qz2-q5-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | 5 верни от 5 отговорени, общ · Въпрос 5 от 5 · Коя от тези книги е написана · Очи с цвят на вода · Загадката на Мари Роже · Ритуал в смъртта
QUIZ2 q5: RIGHT; band: content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
TREC quiz-result-2: 107 frames; frames 107; changed between consecutive frames: 1; max diff 23.2; frames=107 motion-start=#16 moving-frames=1 (span 1) kept #15..#26; diffs: 0 23 0 0 0 0 0 0 0 0 0 0
CAP ui38b/qz2-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Отлично! Познаваш Яворец! · 5/5 · 5 верни от 5 · Шевицата е цяла! · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ2 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## UB3. Login screen as a guest (FLAG_SECURE: emulator screenshot)
UI38B part: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 12 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 1 | Първи кадър: медиана 3510 мс, последен 3510 мс | Пълно показване: медиана 3510 мс, последно 3510 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 1" text="Първи кадър: медиана 3510 мс, последен 3510 мс" text="Пълно показване: медиана 3510 мс, последно 3510 мс" text="Няма записани грешки." content-desc=
APP W/E lines (pids ['5139', '10183']): 103 in 89 groups
       3  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       2  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       2  W/Parcel: Expecting binder but got null!
       2  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       2  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       2  W/Gralloc4: allocator #.x is not supported
       2  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#bcfec#-a#ca-#e#c-#-#b#f#, generation=#)
       2  W/BasicSecureTextField: 	at b#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at tn#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       2  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       2  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       2  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#c#fac#-#-#adb-b#a-ed#da#fa#, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object s#.s(java.lang.Object)(r
       1  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       1  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       1  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       1  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       1  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       1  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       1  W/BasicSecureTextField: 	at da#.a(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       1  W/BasicSecureTextField: 	at da#.d(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       1  W/BasicSecureTextField: 	at b#.b(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       1  W/BasicSecureTextField: 	at a#.invoke(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       1  W/BasicSecureTextField: 	at mp#.c(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       1  W/BasicSecureTextField: 	at mp#.invoke(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       1  W/BasicSecureTextField: 	at py#.r(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
       1  W/BasicSecureTextField: 	at yr#.b(r#-map-id-#f#a#cd#e#b#d#c#c#dcc#a#e#f#e#a#:#)
contact sheets: 13 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5139', '10183']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 26
   skipped-frames events=26 max=147 total=1449
== Davey: 74
   davey events=74 max=3231ms median=936ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642132 Nov 14 15:25 app-mapping.txt; crashes: 0 B
```

## upd38

```
part upd38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## U0. Install 1.2.35 (1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 151) as a fresh install
installed base: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454;  versionCode=1510000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## U1. State on 1.2.35: books in „Искам да прочета“, a card by hand, text size „Голям“, dark theme, the widget, a few screens
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
TEXT SIZE now: text="Голям" 
WIDGET b135 long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET b135 picker: our row @ 437 1077; size label @ 540 1985; texts: Chitalishte Yavorets|3 × 2|Chitalishte Yavorets|Chitalishte Yavorets|
WIDGET b135 after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET b135: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET on 1.2.35: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_title ]; bounds [309 537 1023 1123]; texts: Книга на деня|Безсрамна добродетел|Робъртс, Нора|; error text: 
locale before the update: Locales for org.chyavorec.app for user 0 are [en-US]
STATE before settings: 
STATE before night mode of the app window: ; app locale [en-US]
STATE before card: shown (TEST-12345)
STATE before want-to-read: book listed; text="Availability as of Nov 9, 2026" text="Swipe a book aside to remove it." text="Последният мечтите" text="Соня Петрова" text="available" content-desc="More actions" text="Дългият любовта" text="Пе
DB[before] files: 4096 chitalishte.db;32768 chitalishte.db-shm;70072 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[before]: 27 files: databases/catalog-index.db databases/chitalishte.db files/datastore/settings.preferences_pb files/profileInstalled files/profileinstaller_profileWrittenFor_lastUpdateTime.dat no_backup/androidx.work.workdb no_backup/androidx.work.workdb-shm no_backup/androidx.work.workdb-wal no_backup/cache/public/1ebfb91784270a823e84c3bd83429c0268dd92904a2dd8e1a4dfd63aad2c1839.json no_backup/cache/public/288782f6475e076286d714aeb0dda24f43f1298026ee7a45902c0407942f5c8d.json no_backup/cache/public/3111e84832a5450
prefs/datastore files before: /data/data/org.chyavorec.app/files: datastore profileInstalled profileinstaller_profileWrittenFor_lastUpdateTime.dat  /data/data/org.chyavorec.app/shared_prefs: app_shortcuts.xml cover_misses.xml stats_anim.xml widget_book.xml 
## U2. adb install -r of 1.2.36 over 1.2.35 (same signing key)
adb install -r 1.2.36: Success; now:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
WIDGET after the update (app not opened): views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_title ]; bounds [309 537 1023 1123]; texts: Book of the day|Безсрамна добродетел|Робъртс, Нора|; error text: 
widget ids before/after: [id=2 ] / [id=2 ]
logcat since the install (app not opened): FATAL(any process)=0 of the app=0 ANR=0
## U3. First launch of 1.2.36 on the old data
first launch after the update: Status: ok LaunchState: WARM TotalTime: 4076 WaitTime: 4219 
first screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode" text="November 14, 2026 · *Св. ап. Филип. Св. благоверни цар Управда-Юстиниан. Св. мчк Константин от Идра (Тип. с. 118) (Рождественски заг
WHATS-NEW 31-whats-new: not shown; screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode" text="November 14, 2026 · *Св. ап. Фил
logcat since the first launch: FATAL(any process)=0 of the app=0 ANR=0; process 10016
Room/migration lines: 
locale after the update: Locales for org.chyavorec.app for user 0 are [en-US] (UI shows: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" )
STATE after settings: text="Тъмна" text="Голям" 
STATE after night mode of the app window: ; app locale [bg-BG]
STATE after card: shown (TEST-12345)
STATE after want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="Последният мечтите" text="Соня Петрова" text="налична" content-desc="Още действия" text="Дългият любовта" text="П
## U4. Database and files after the update
DB[after] files: 4096 chitalishte.db;32768 chitalishte.db-shm;70072 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[after]: 27 files: databases/catalog-index.db databases/chitalishte.db files/datastore/settings.preferences_pb files/profileInstalled files/profileinstaller_profileWrittenFor_lastUpdateTime.dat no_backup/androidx.work.workdb no_backup/androidx.work.workdb-shm no_backup/androidx.work.workdb-wal no_backup/cache/public/1ebfb91784270a823e84c3bd83429c0268dd92904a2dd8e1a4dfd63aad2c1839.json no_backup/cache/public/288782f6475e076286d714aeb0dda24f43f1298026ee7a45902c0407942f5c8d.json no_backup/cache/public/3111e84832a5450
FILES before 27, after 27; removed 0: ; new 0: ; size changed 1: ./no_backup/diagnostics/perf.json
## U5. Widget after opening the app; Diagnostics › Бързодействие after the update; new screens on the migrated data
WIDGET after opening the app: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_title ]; bounds [309 537 1023 1123]; texts: Книга на деня|Безсрамна добродетел|Робъртс, Нора|; error text: 
WIDGET click catalog: 'widget_catalog' not on the home screen
WIDGET click scan: 'widget_scan' not on the home screen
WIDGET click event: 'widget_event' not on the home screen
WIDGET click app: 't:Chitalishte Yavorets|Читалище Яворец' not on the home screen
WARN: Settings not found
## NP-after-update (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP after-update/01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP after-update/02-more: targets=14 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=23 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP after-update/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 17
CAP after-update/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 17 → 6
EMB redo: 6 → 17
EMB save: text="Моите шевици" 
CAP after-update/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser OPEN after 4 s; fg android/com.android.internal.app.ChooserActivity
EMB end: front app
CAP after-update/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 3387 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP after-update/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=30 std=25.0
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 3374 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP after-update/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=30 std=25.1
CAP after-update/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно
CAP after-update/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 1909. Организирано читалищно · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре · 17 дек. 1922. Възстановяване
CAP after-update/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 1927. Въздържателно дружеств · Основано от активни читалищн · 1934. Първият радиоапарат в  · Първият радиоапарат в селото · Закупен от читалището. По вр · 1936. Дружество „Воля“. Ново
QUIZ ready after 2107 ms: content-desc="0 верни от 0 отговорени, общо 5 въпроса" text="Въпрос 1 от 5" text="Коя от тези книги е написана от Максим Якубовски?" text="Колекционерът" text="Робинята" text="Един ден в древен Рим" text="Прокудените" text="Избродирай отгов
CAP after-update/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
CAP after-update/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=13 ellipsized=0 | 0 верни от 1 отговорени, общ · Въпрос 1 от 5 · Коя от тези книги е написана · Колекционерът · Робинята · Един ден в древен Рим
CAP after-update/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 0/5 · 0 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="0/5" text="0 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser OPEN after 3 s; fg android/com.android.internal.app.ChooserActivity
CAP after-update/st-01: targets=10 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | ИЗГЛЕД · Тема · Тъмна · Размер на текста · Голям · Палитра
CAP after-update/st-02: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=19 ellipsized=0 | Яворец · Злато и жито · Левски · Зелено и сребро · Шевица · Червено, черно и слонова кос
CAP after-update/st-03: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога
CAP after-update/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=16 ellipsized=0 | Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP after-update/11-event: targets=8 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP after-update: FATAL(any process)=0 of the app=0 ANR=0; app process 11993; logcat app errors: 0
## U6. Logcat of the whole part
FATAL/ANR since the install: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 8 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN:  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38 (1510100) | Профил: чака | Студен старт · 5 | Първи кадър: медиана 3689 мс, последен 4014 мс | Пълно показване: медиана 4394 мс, последно 4685 мс | Топъл старт · 2 | Първи кадър: медиана 2532 мс, последен 1347 мс | Пълно показване: медиана 4470 
DIAG SCREEN (scrolled): text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38 (1510100)" text="Профил: чака" text="Студен старт · 5" text="Първи кадър: медиана 3689 мс, последен 4014 мс" text="Пълно показване: медиана 4394 мс, последно 4685 мс" text="Топъл старт · 2" text="Първи кадър: медиана 2532 мс, последен 1347 мс" text="Пълно показване: медиана 4470 мс, последно 4470 мс" text="Насечени кадри по екрани" text="home: 0,5 % от 215 кадъра · най-бавен 2847 мс" text="CPU време на кадъра (без видеокартата): средно 706 мс, най-бавен 4451 мс" text="Преди това · Версия 1.2.37 (1510000)" text="Студен старт
APP W/E lines (pids ['5068', '8999', '10016', '11198', '11489', '11553', '11670', '11993']): 79 in 14 groups
      16  W/Parcel: Expecting binder but got null!
      15  E/OpenGLRenderer: Unable to match the desired swap behavior.
       8  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       8  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       8  W/Gralloc4: allocator #.x is not supported
       2  E/FrameEvents: updateAcquireFence: Did not find frame.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#d#c#-#ea-#-bde#-#bf#e#, generation=#)
       1  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       1  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
       1  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object s#.s(java.lang.Object)(r
       1  W/g.chyavorec.app: No such thread id for suspend: #
contact sheets: 9 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5068', '8999', '10016', '11198', '11489', '11553', '11670', '11993']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 74
   skipped-frames events=74 max=172 total=4744
== Davey: 284
   davey events=284 max=4401ms median=816ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'ANR': 1} → crashes.txt
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 8565 B
```

## updd38

```
part updd38; APK: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
base APK: 1.2.37 ebf1720 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510000; update manifest: the real one (releases/latest) size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 de2e156 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1510100; update manifest: the real one (releases/latest) size 4640810 bytes
installed:  versionCode=1510100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed devDebug base: devDebug 1.2.37 ebf1720 (org.chyavorec.app.dev, demo sign-in DEMO-0001, shared debug key);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.37-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 7437 ms
## DD1. State on devDebug 1.2.37: demo sign-in (remember me), 5 rosettes + a note in the diary, „Искам да прочета“, text size „Голям“
Запомни ме before: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
Запомни ме now: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
MY after the demo sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" 
DIARY on 1.2.37: content-desc="Оценка на книгата" 
TEXT SIZE now: text="Голям" 
STATE before settings: text="Голям" 
STATE before night mode of the app window: ; app locale [bg-BG]
STATE before card: shown ()
STATE before want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="Дългият любовта" text="Пенка Христова" text="Не е на рафта" content-desc="Още действия" text="12 -те вятъра" text
DB[before] files: 4096 chitalishte.db;32768 chitalishte.db-shm;78312 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=1 room_master_table=1 want_to_read=2; want_to_read: 1:Дългият любовта; 399560:12 -те вятъра
## DD2. adb install -r devDebug 1.2.38
adb install -r: Success; now  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.38-dev 
first screen after the update: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения (1 непрочетено)" text="1" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="Просрочено" text="1 книга е просрочена" text="Демо загл
logcat since the install: FATAL(any process)=0 of the app=0 ANR=0; Room: 
STATE after settings: text="Голям" 
STATE after night mode of the app window: ; app locale [bg-BG]
STATE after card: shown ()
STATE after want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="Дългият любовта" text="Пенка Христова" text="Не е на рафта" content-desc="Още действия" text="12 -те вятъра" text
MY after the update (still signed in?): text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" text="Моите книги" text="1 книга е просрочена" text="Искам
DIARY after the update: rating content-desc="Оценка на книгата" ; note KEPT
DB[after] files: 4096 chitalishte.db;32768 chitalishte.db-shm;78312 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=1 room_master_table=1 want_to_read=2; want_to_read: 1:Дългият любовта; 399560:12 -те вятъра
## DD3. The year story on the migrated diary (favourite slide)
YEAR slide 1: text="Моята читателска година" text="2026" content-desc="Затвори" text="Сподели" 
YEAR slide 2: content-desc="Любим автор: Иван Вазов — 6 книги през годината." text="Моята читателска година" text="2026" content-desc="Затвори" text="Сподели" 
YEAR slide 3: content-desc="Най-четен раздел: Език. Литература — 2 книги." text="Моята читателска година" text="2026" content-desc="Затвори" text="Сподели" 
YEAR slide 4: content-desc="Най-дълга поредица: 11 месеца подред с поне една книга." text="Моята читателска година" text="2026" content-desc="Затвори" text="Сподели" 
YEAR slide 5: content-desc="Благодарим ти, че четеш с нас! Библиотеката на читалището те очаква и през 2027 г. Твоята шевица: лента, избродирана от 30 книги през годината." text="Моята читателска година" text="2026
YEAR slide 6: text="Читателска карта" text="Покажи картата на гишето" text="Моите книги" text="1 книга е просрочена" text="Искам да прочета" text="2 книги в списъка" text="Моето членство" text="Статус и валидност" 
UPDD38 part: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 3 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38-dev (1) | Профил: чака | Студен старт · 3 | Първи кадър: медиана 3752 мс, последен 2979 мс | Пълно показване: медиана 4700 мс, последно 3919 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38-dev (1)" text="Профил: чака" text="Студен старт · 3" text="Първи кадър: медиана 3752 мс, последен 2979 мс" text="Пълно показване: медиана 4700 мс, последно 3919 мс" text="Няма записани грешки." content-desc="Н
APP W/E lines (pids ['5637', '9464', '10462']): 242 in 113 groups
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       6  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       5  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/Parcel: Expecting binder but got null!
       4  E/OpenGLRenderer: Unable to match the desired swap behavior.
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       3  W/yavorec.app.dev: Unexpected CPU variant for x#: x#_#.
       3  W/yavorec.app.dev: Known variants: atom, sandybridge, silvermont, kabylake, default
       3  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       3  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       3  W/Gralloc4: allocator #.x is not supported
       3  W/yavorec.app.dev: Accessing hidden method Landroid/os/SystemProperties;->addChangeCallback(Ljava/lang/Runnable;)V (unsupported, 
       3  W/BasicSecureTextField: 	at android.view.ViewGroup.recreateChildDisplayList(ViewGroup.java:#)
       3  W/BasicSecureTextField: 	at android.view.ViewGroup.dispatchGetDisplayList(ViewGroup.java:#)
       3  W/yavorec.app.dev: Accessing hidden method Landroid/view/ViewTreeObserver;->dispatchOnScrollChanged()V (unsupported, reflection, 
       3  W/ziparchive: Unable to open '/data/app/~~Q#_s#SDhkJ#JQovBP_dq_g==/org.chyavorec.app.dev-ZjWT#whkMldrz#f#UtodA==/base.dm': N
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.PlatformPasswordVisibilitySettingImpl.getSystemShowPasswordSetting(BasicS
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextField_androidKt.rememberPlatformPasswordVisibilitySettings
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-ltb#GB#(BasicSecureTextField.
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-egD#TGM(BasicSecureTextField.
contact sheets: 4 in sheets/
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
retrace tool: /usr/local/lib/android/sdk/cmdline-tools/latest/bin/retrace; mapping: -rw-r--r-- 1 runner runner 92642141 Nov 14 14:04 app-mapping.txt; crashes: 0 B
```

