# Wave 13 emulator check before release (v127)

Round 2 (startup A/B against v1.2.26 on the same emulator, /events from an article): [run2/index.md](run2/index.md)

Source: `debug/wave13-check` @ 6efbc80 (app = wave 13 merge 305a487), run 40561018348 (attempt 1), 2026-10-27T18:51:33Z.
Emulators: google_apis x86_64 API 33, pixel_6 profile, -gpu swangle_indirect; app locale bg-BG.
Previous full test (v1.2.26): [../v126/index.md](../v126/index.md).

| part | status |
|---|---|
| func1 | COMPLETE; part=func1 api=33 gpu=swangle_indirect emulator-step=success |
| func2 | COMPLETE; part=func2 api=33 gpu=swangle_indirect emulator-step=success |
| offline | COMPLETE; part=offline api=33 gpu=swangle_indirect emulator-step=success |
| perf | COMPLETE; part=perf api=33 gpu=swangle_indirect emulator-step=success |

## func1

```
part func1; APK: 6efbc80 (Проверка на вълна 13 на емулатор (v127): старт, памет, monkey, функционални пров); x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
first-install catalogue results after 2643 ms
## F1. Home quick actions (Scan, Library card, Want to read, Contacts)
QA standard text: Сканирай|=Scan@[167 1542] Читателска карта|=Library card@[415 1563] Искам да прочета|=Want to read@[664 1563] Контакти|=Contacts@[913 1542]; rows=1; quick action 'Събития': absent
QA 'Искам да прочета' → screen: back=yes title=yes; text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" 
## F2. Catalogue: search '12', a copy that is out while others are on the shelf
SEARCH '12': 108 results; header: text="108 резултата  ·  данни от InvLib към 17.10.2026"
SOLE-OUT book: content-desc="Спомен за здрача" text="Спомен за здрача" text="Веска Пенчева" text="Не е на рафта" text="Наличност към 17.10.2026" text="Искам да прочета" ; AskLibrary=shown Notify=shown
BOOK 'Кажи ми' ON: label now 'text="Ще ти кажем, когато е на рафта"'; snackbar: text="Ще получиш известие, когато книгата е на рафта."
BOOK 'Кажи ми' OFF: snackbar 'text="Няма да получаваш известие за тази книга."' undo=present
BOOK 'Кажи ми' undo → ON again (restored)
COPY-OUT (from '12'): text="Този екземпляр е зает · 5 други на рафта"; title: Времето на горите; AskLibrary=hidden Notify=hidden
COPY-OUT scrolled: AskLibrary=hidden Notify=hidden; copies line: text="7 екземпляра: 5 налични"
COPY-OUT search: tried 2 unavailable rows from '12' (found copy-out=1, sole-out=1)
## F3. Want to read: add, 'Кажи ми' on/off, undo, remove with undo
BOOK 399560: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 17.10.2026" ; WantToRead [checked=false selected=false bounds=[426,738][911,864]]
WANT add: snackbar shown; chip [checked=true selected=false bounds=[426,738][911,864]]
WANT list opened from the Home quick action
WANT list: Наличност към 17.10.2026|Плъзни книга настрани, за да я махнеш.|12 -те вятъра|12 -те вятъра|Гарфилд, Джудит и др.|налична|Още действия|Спомен за здрача|Спомен за здрача|Веска Пенчева|Не е на рафта|Ще ти кажем, когато е на рафта|
WANT status: lowercase 'налична' present; capitalised pill 'Налична' absent; 'Не е на рафта' present
WANT 'Кажи ми' ON: snackbar 'text="Ще получиш известие, когато книгата е на рафта."'; bell icon shown
WANT 'Кажи ми' OFF: snackbar 'text="Няма да получаваш известие за тази книга."'; undo present; bell still-shown
WANT 'Кажи ми' undo: bell back (ON restored)
WANT remove: snackbar 'text="„12 -те вятъра“ е махната от списъка"'; undo present; 'налична' row gone
WANT remove undo: book back in list
## F4. Links: /news opens News after Calendar was used; /events opens Calendar (also from an article)
/news (first): News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
Calendar segment chosen: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
/news while on Calendar: News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
/news from Home (News tab left on Calendar): News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
/news cold: News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
/events: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
article: no card found
## F5. Reselecting the News tab on Calendar scrolls to the top
calendar top: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible; Новини|Календар|Абонирай се за календара|Изглед календар|Всички|Библиотека|Читалище|Народен обичай|1|НОЕ|
calendar scrolled: segments visible yes; common with top: 18/40
RESELECT: News[checked=false selected=false bounds=[42,140][354,266]] Calendar[checked=true selected=false bounds=[354,140][669,266]] subscribe-action=visible; common with top: 38/40; first lines: Новини|Календар|Абонирай се за календара|Изглед календар|Всички|Библиотека|
## F6. Settings → Clear temporary data → catalogue returns
CLEAR: snackbar shown
CLEAR → catalogue results again after 2330 ms (2446 results)
MEM func1-end (raw)  PSS=118165KB  Java=23116KB  Native=65380KB  Graphics=0KB  Views=11 Activities=1
MEM func1-end (trimmed) PSS=95051KB  Java=14232KB  Native=46120KB  Graphics=0KB  Views=11 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5708', '7207', '9496']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=56 total=56
== Davey: 2
   davey events=2 max=1652ms median=1217ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func2

```
part func2; APK: 6efbc80 (Проверка на вълна 13 на емулатор (v127): старт, памет, monkey, функционални пров); x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
first-install catalogue results after 3176 ms
## F7. Scanner with CAMERA revoked: manual entry, Back from the panel
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
SCAN screen: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране
manual panel: field shown; IME mInputShown=true
Back #1: panel still open; IME mInputShown=false; scanner visible; app in front yes
Back #2: panel closed; IME mInputShown=false; scanner visible; app in front yes
SCAN result: content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Налична" text="инв. № 399560" text="Затвори" text="Отвори книг
SCAN → book: opened
emulator alive after scanner: yes
## F8. Contacts at standard text
CONTACTS standard: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital; clickables past the right edge: 0
CONTACTS standard (scrolled): text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chitalishte_yavorec@abv.bg" text="Работно време" 
## F9. Home at 'Много голям': 2x2 quick actions, whole labels, upcoming events
TEXT SIZE now: text="Много голям" 
QA Много голям: Сканирай|=Scan@[291 1573] Читателска карта|=Library card@[789 1573] Искам да прочета|=Want to read@[291 1836] Контакти|=Contacts@[789 1836]; rows=2; quick action 'Събития': absent
upcoming events (xlarge): text="Сканирай" text="Читателска карта" text="Искам да прочета" text="Контакти" text="КАЛЕНДАР" text="Предстоящи събития" text="Всички" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет на народните будители." text="НАЦИО
## F10. Contacts at 'Много голям'
CONTACTS xlarge: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital; clickables past the right edge: 0
CONTACTS xlarge (scrolled): text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chitalishte_yavorec@abv.bg" text="Работно време" text="Понедел
## F11. Largest system font (1.3) + 'Много голям': Календар label, actions on a second row
font_scale=1.3
FONTMAX calendar: News[checked=false selected=false bounds=[42,307][538,438]] Calendar[checked=true selected=false bounds=[538,307][1038,438]] subscribe-action=visible; segment 'Календар' bounds [634 328 943 417], 'Новини' [172 328 412 417], subscribe [839 481 902 544]
FONTMAX actions: on a SECOND row (below the segments)
QA font 1.3 + Много голям: Сканирай|=Scan@[291 1722] Читателска карта|=Library card@[789 1757] Искам да прочета|=Want to read@[291 2083] Контакти|=Contacts@[789 2070]; rows=2; quick action 'Събития': absent
TEXT SIZE now: text="Стандартен" 
## F12. Landscape Home: hero at most about half the height
landscape check: mRotation=ROTATION_90} 2400x1080
HERO landscape: bounds [338 527 2400 1045] height 518 px of 1080 px = 47%
## COMPLETE
## logcat findings
app pids seen: ['5323', '8793', '9220', '9826']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 9
   skipped-frames events=9 max=60 total=388
== Davey: 16
   davey events=16 max=1324ms median=908ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 6efbc80 (Проверка на вълна 13 на емулатор (v127): старт, памет, monkey, функционални пров); x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15987 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## O1. Empty cache, offline, guest: My
offline: airplane=1
MY offline guest: add-card shown; note shown; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Добави читателска карта" text="Не можем да проверим дали онлайн входът е включен." text="Опитай отново" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text
MY offline guest (+8 s): add-card shown; note shown
## O2. Gallery offline with an empty cache
gallery offline 3 s: text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Галерия" text="Няма интернет връзка" 
GALLERY offline: 'Опитай отново' after 7585 ms; text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Галерия" text="Няма интернет връзка" 
gallery offline (+15 s): text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Галерия" text="Няма интернет връзка" 
## O3. Reconnect on the Gallery (no touch)
GALLERY reconnect: default network back after 6748 ms; albums shown after 14064 ms (-1 = not within 60 s); text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" 
## O4. My offline as guest again (after the online start)
offline: airplane=1
MY offline guest #2: add-card MISSING; note absent; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
## COMPLETE
## logcat findings
app pids seen: ['5114', '7895']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=82 total=163
== Davey: 2
   davey events=2 max=1222ms median=1134ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## perf

```
part perf; APK: 6efbc80 (Проверка на вълна 13 на емулатор (v127): старт, памет, monkey, функционални пров); x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch, catalogue index
FIRST-INSTALL catalogue results after 3530 ms
INDEX-READY #1 catalogue results after 2725 ms (cold start → tap Catalogue → text)
INDEX-READY #2 catalogue results after 3500 ms (cold start → tap Catalogue → text)
INDEX-READY #3 catalogue results after 2835 ms (cold start → tap Catalogue → text)
## 1. Cold start (10 runs, as installed)
START cold(as installed) n=10 min=1796ms median=2160ms max=2988ms  all=[2833, 2988, 2159, 2191, 1951, 2161, 2131, 2195, 1867, 1796]  states=['COLD']
START warm(HOME→relaunch) n=5 min=329ms median=586ms max=744ms  all=[461, 586, 744, 701, 329]  states=['HOT']
## 2. Memory over 20 cycles (all tabs + a book)
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=58188KB  Java=7328KB  Native=26252KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=64485KB  Java=8208KB  Native=26248KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c10 (raw)        PSS=99664KB  Java=21180KB  Native=43524KB  Graphics=0KB  Views=13 Activities=1
MEM c10 (trimmed)    PSS=84944KB  Java=11108KB  Native=39152KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (raw)        PSS=97349KB  Java=19256KB  Native=42384KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (trimmed)    PSS=85154KB  Java=11104KB  Native=39152KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 8403
## 3. Monkey 4000 events (seed 1234, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
statusbar disable: 
monkey start pid=9524
MONKEY exit=0 duration=166 s; Events injected: 4000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '9524'; camera:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MEM after-monkey (raw) PSS=93980KB  Java=13884KB  Native=45616KB  Graphics=0KB  Views=13 Activities=1
MEM after-monkey (trimmed) PSS=94715KB  Java=10864KB  Native=42784KB  Graphics=0KB  Views=13 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4507', '5874', '6527', '6888', '7251', '7482', '7628', '7703', '7863', '7984', '8049', '8100', '8152', '8202', '8403', '9524']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 35
   skipped-frames events=35 max=74 total=1484
== Davey: 67
   davey events=67 max=1646ms median=843ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### func1/shots/func

- [func1/shots/func/01-home-quick-actions.png](func1/shots/func/01-home-quick-actions.png)
- [func1/shots/func/02-qa-want-to-read.png](func1/shots/func/02-qa-want-to-read.png)
- [func1/shots/func/03-catalog-12.png](func1/shots/func/03-catalog-12.png)
- [func1/shots/func/04-book-copy-out-others-on-shelf.png](func1/shots/func/04-book-copy-out-others-on-shelf.png)
- [func1/shots/func/04b-book-copy-out-scrolled.png](func1/shots/func/04b-book-copy-out-scrolled.png)
- [func1/shots/func/05-book-sole-out.png](func1/shots/func/05-book-sole-out.png)
- [func1/shots/func/06-book-notify-on.png](func1/shots/func/06-book-notify-on.png)
- [func1/shots/func/07-book-notify-off-snackbar.png](func1/shots/func/07-book-notify-off-snackbar.png)
- [func1/shots/func/08-book-notify-undo.png](func1/shots/func/08-book-notify-undo.png)
- [func1/shots/func/09-book-want-added.png](func1/shots/func/09-book-want-added.png)
- [func1/shots/func/10-want-list.png](func1/shots/func/10-want-list.png)
- [func1/shots/func/11-want-menu.png](func1/shots/func/11-want-menu.png)
- [func1/shots/func/12-want-notify-on.png](func1/shots/func/12-want-notify-on.png)
- [func1/shots/func/13-want-notify-off-snackbar.png](func1/shots/func/13-want-notify-off-snackbar.png)
- [func1/shots/func/14-want-notify-undo.png](func1/shots/func/14-want-notify-undo.png)
- [func1/shots/func/15-want-removed-snackbar.png](func1/shots/func/15-want-removed-snackbar.png)
- [func1/shots/func/16-want-after-undo.png](func1/shots/func/16-want-after-undo.png)
- [func1/shots/func/17-news-link.png](func1/shots/func/17-news-link.png)
- [func1/shots/func/18-calendar-chosen.png](func1/shots/func/18-calendar-chosen.png)
- [func1/shots/func/19-news-link-after-calendar.png](func1/shots/func/19-news-link-after-calendar.png)
- [func1/shots/func/20-news-link-from-home.png](func1/shots/func/20-news-link-from-home.png)
- [func1/shots/func/21-events-link.png](func1/shots/func/21-events-link.png)
- [func1/shots/func/24-calendar-top.png](func1/shots/func/24-calendar-top.png)
- [func1/shots/func/25-calendar-scrolled.png](func1/shots/func/25-calendar-scrolled.png)
- [func1/shots/func/26-calendar-after-reselect.png](func1/shots/func/26-calendar-after-reselect.png)
- [func1/shots/func/27-clear-dialog.png](func1/shots/func/27-clear-dialog.png)
- [func1/shots/func/28-cleared-snackbar.png](func1/shots/func/28-cleared-snackbar.png)
- [func1/shots/func/29-catalog-after-clear.png](func1/shots/func/29-catalog-after-clear.png)

### func2/shots/func2

- [func2/shots/func2/01-scanner-no-camera.png](func2/shots/func2/01-scanner-no-camera.png)
- [func2/shots/func2/02-manual-panel.png](func2/shots/func2/02-manual-panel.png)
- [func2/shots/func2/03-after-back.png](func2/shots/func2/03-after-back.png)
- [func2/shots/func2/04-manual-result.png](func2/shots/func2/04-manual-result.png)
- [func2/shots/func2/05-settings-xlarge.png](func2/shots/func2/05-settings-xlarge.png)
- [func2/shots/func2/06-home-xlarge-quick-actions.png](func2/shots/func2/06-home-xlarge-quick-actions.png)
- [func2/shots/func2/07-home-xlarge-upcoming.png](func2/shots/func2/07-home-xlarge-upcoming.png)
- [func2/shots/func2/08-calendar-font130-xlarge.png](func2/shots/func2/08-calendar-font130-xlarge.png)
- [func2/shots/func2/09-home-font130-xlarge.png](func2/shots/func2/09-home-font130-xlarge.png)
- [func2/shots/func2/10-settings-reset.png](func2/shots/func2/10-settings-reset.png)
- [func2/shots/func2/11-home-landscape.png](func2/shots/func2/11-home-landscape.png)
- [func2/shots/func2/12-home-landscape-scrolled.png](func2/shots/func2/12-home-landscape-scrolled.png)
- [func2/shots/func2/contacts-standard-1.png](func2/shots/func2/contacts-standard-1.png)
- [func2/shots/func2/contacts-standard-2.png](func2/shots/func2/contacts-standard-2.png)
- [func2/shots/func2/contacts-standard-3.png](func2/shots/func2/contacts-standard-3.png)
- [func2/shots/func2/contacts-xlarge-1.png](func2/shots/func2/contacts-xlarge-1.png)
- [func2/shots/func2/contacts-xlarge-2.png](func2/shots/func2/contacts-xlarge-2.png)
- [func2/shots/func2/contacts-xlarge-3.png](func2/shots/func2/contacts-xlarge-3.png)

### offline/shots/offline

- [offline/shots/offline/00-home-offline.png](offline/shots/offline/00-home-offline.png)
- [offline/shots/offline/01-my-guest-offline.png](offline/shots/offline/01-my-guest-offline.png)
- [offline/shots/offline/02-my-guest-offline-later.png](offline/shots/offline/02-my-guest-offline-later.png)
- [offline/shots/offline/03-gallery-offline-3s.png](offline/shots/offline/03-gallery-offline-3s.png)
- [offline/shots/offline/04-gallery-offline-error.png](offline/shots/offline/04-gallery-offline-error.png)
- [offline/shots/offline/05-gallery-offline-later.png](offline/shots/offline/05-gallery-offline-later.png)
- [offline/shots/offline/06-gallery-after-reconnect.png](offline/shots/offline/06-gallery-after-reconnect.png)
- [offline/shots/offline/08-my-online.png](offline/shots/offline/08-my-online.png)
- [offline/shots/offline/09-my-guest-offline-2.png](offline/shots/offline/09-my-guest-offline-2.png)

### perf/shots

- [perf/shots/00-home-first.png](perf/shots/00-home-first.png)
- [perf/shots/cycles-book.png](perf/shots/cycles-book.png)
- [perf/shots/monkey-end.png](perf/shots/monkey-end.png)

## Other files

- [func1/host-after-run.txt](func1/host-after-run.txt)
- [func1/job-status.txt](func1/job-status.txt)
- [func1/logcat-all.txt](func1/logcat-all.txt)
- [func1/logcat-dump.txt](func1/logcat-dump.txt)
- [func1/logcat-findings.txt](func1/logcat-findings.txt)
- [func1/logcat-live.txt](func1/logcat-live.txt)
- [func1/mem-func1-end-raw.txt](func1/mem-func1-end-raw.txt)
- [func1/mem-func1-end.txt](func1/mem-func1-end.txt)
- [func1/reselect-after.txt](func1/reselect-after.txt)
- [func1/reselect-scrolled.txt](func1/reselect-scrolled.txt)
- [func1/reselect-top.txt](func1/reselect-top.txt)
- [func1/summary.txt](func1/summary.txt)
- [func1/want-texts.txt](func1/want-texts.txt)
- [func2/host-after-run.txt](func2/host-after-run.txt)
- [func2/job-status.txt](func2/job-status.txt)
- [func2/logcat-all.txt](func2/logcat-all.txt)
- [func2/logcat-dump.txt](func2/logcat-dump.txt)
- [func2/logcat-findings.txt](func2/logcat-findings.txt)
- [func2/logcat-live.txt](func2/logcat-live.txt)
- [func2/summary.txt](func2/summary.txt)
- [offline/host-after-run.txt](offline/host-after-run.txt)
- [offline/job-status.txt](offline/job-status.txt)
- [offline/logcat-all.txt](offline/logcat-all.txt)
- [offline/logcat-dump.txt](offline/logcat-dump.txt)
- [offline/logcat-findings.txt](offline/logcat-findings.txt)
- [offline/logcat-live.txt](offline/logcat-live.txt)
- [offline/summary.txt](offline/summary.txt)
- [perf/host-after-run.txt](perf/host-after-run.txt)
- [perf/job-status.txt](perf/job-status.txt)
- [perf/logcat-all.txt](perf/logcat-all.txt)
- [perf/logcat-dump.txt](perf/logcat-dump.txt)
- [perf/logcat-findings.txt](perf/logcat-findings.txt)
- [perf/logcat-live.txt](perf/logcat-live.txt)
- [perf/mem-after-monkey-raw.txt](perf/mem-after-monkey-raw.txt)
- [perf/mem-after-monkey.txt](perf/mem-after-monkey.txt)
- [perf/mem-c00-raw.txt](perf/mem-c00-raw.txt)
- [perf/mem-c00.txt](perf/mem-c00.txt)
- [perf/mem-c10-raw.txt](perf/mem-c10-raw.txt)
- [perf/mem-c10.txt](perf/mem-c10.txt)
- [perf/mem-c20-raw.txt](perf/mem-c20-raw.txt)
- [perf/mem-c20.txt](perf/mem-c20.txt)
- [perf/monkey.txt](perf/monkey.txt)
- [perf/start-cold.txt](perf/start-cold.txt)
- [perf/start-first.txt](perf/start-first.txt)
- [perf/start-warm.txt](perf/start-warm.txt)
- [perf/summary.txt](perf/summary.txt)
