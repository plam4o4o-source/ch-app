# v1.2.26 full emulator test

Source: `debug/full-test-2` @ 342758d (app = origin/main 3df7f37), run 40528986167 (attempt 1), 2026-10-27T15:31:40Z.
Emulators: google_apis x86_64, pixel_6 profile, swiftshader (software rendering); start35 on API 35, all other parts on API 34.

| part | status |
|---|---|
| start35 | COMPLETE; part=start35 api=35 emulator-step=success |
| perf | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: end)); part=perf api=34 emulator-step=failure |
| stress | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: home swipe 1)); part=stress api=34 emulator-step=failure |
| func | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: F5. Scanner with CAMERA revoked → manual entry)); part=func api=34 emulator-step=failure |
| look | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: L5. Landscape)); part=look api=34 emulator-step=failure |
| offline | COMPLETE; part=offline api=34 emulator-step=success |

## start35

```
part start35; APK: 342758d (Проверка: пълен тест на v1.2.26 на емулатор (ВРЕМЕННО)); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 15 sdk 35; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## 1. First install: first launch, time to catalogue results
home quick actions: Scan=yes MyBooks=no Contacts=yes; 'Catalogue' labels on screen: 1 (1 = only the tab)
FIRST-INSTALL catalogue results after 5317 ms (tap→text, uiautomator polling)
catalogue hint: 'Search everything' OK
polling floor (text already on screen): 2364 ms — subtract from the times below
INDEX-READY #1 catalogue results after 2958 ms (cold start → tap Catalogue → text)
INDEX-READY #2 catalogue results after 2551 ms (cold start → tap Catalogue → text)
INDEX-READY #3 catalogue results after 2734 ms (cold start → tap Catalogue → text)
INDEX-READY deep-link #1 results 5118 ms after am start
INDEX-READY deep-link #2 results 5864 ms after am start
## 2. Startup
START cold(as installed) n=10 min=1609ms median=1862ms max=2258ms  all=[1763, 1782, 1824, 2258, 1748, 1899, 1609, 2114, 1968, 1948]  states=['COLD']
START warm(HOME→relaunch) n=5 min=196ms median=353ms max=381ms  all=[353, 196, 356, 381, 306]  states=['HOT']
compile: Success 
START cold(speed-profile) n=5 min=1697ms median=1862ms max=2136ms  all=[2136, 1862, 2015, 1697, 1771]  states=['COLD']
## COMPLETE
## logcat findings
app pids seen: ['3792', '4614', '4769', '4895', '5030', '5116', '5203', '5269', '5335', '5393', '5450', '5506', '5566', '5627', '5685', '5742', '5913', '5973', '6033', '6093', '6165']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 42
   skipped-frames events=42 max=82 total=2109
== Davey: 63
   davey events=63 max=1685ms median=1213ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## perf

```
part perf; APK: 342758d (Проверка: пълен тест на v1.2.26 на емулатор (ВРЕМЕННО)); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 1. First install: first launch, time to catalogue results
home quick actions: Scan=yes MyBooks=no Contacts=yes; 'Catalogue' labels on screen: 1 (1 = only the tab)
FIRST-INSTALL catalogue results after 2808 ms (tap→text, uiautomator polling)
catalogue hint: 'Search everything' OK
polling floor (text already on screen): 2224 ms — subtract from the times below
INDEX-READY #1 catalogue results after 2594 ms (cold start → tap Catalogue → text)
INDEX-READY #2 catalogue results after 2196 ms (cold start → tap Catalogue → text)
INDEX-READY #3 catalogue results after 2841 ms (cold start → tap Catalogue → text)
INDEX-READY deep-link #1 results 3333 ms after am start
INDEX-READY deep-link #2 results 3389 ms after am start
## 2. Startup
START cold(as installed) n=10 min=856ms median=1000ms max=1254ms  all=[1121, 1201, 1020, 947, 981, 1254, 918, 980, 1164, 856]  states=['COLD']
START warm(HOME→relaunch) n=5 min=149ms median=235ms max=263ms  all=[263, 223, 235, 247, 149]  states=['HOT']
compile: Success 
START cold(speed-profile) n=5 min=905ms median=997ms max=1059ms  all=[997, 1058, 1059, 967, 905]  states=['COLD']
Want to read: added 1 books for the list jank
## 3. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX catalog-lowres frames=  503  janky=334 (66.40%)  p50=57ms p90=77ms p95=85ms p99=121ms  slowUI=180 slowDraw=333 highInput=319
catalog-lowres: 30 swipes, 'Show more' pages: 6, mis-taps recovered: 0; screen: --- screen: text="Search everything" content-desc="Scan barcode" text="Everything" text="Title" text="Author" text="ISBN" 
jank home-lowres screen: --- screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 27, 2026 · *Св. мчк Нестор. Преп. Димитрий Басарбовски
GFX home-lowres  frames=    ?  janky=? (?%)  p50=?ms p90=?ms p95=?ms p99=?ms  slowUI=? slowDraw=? highInput=?
jank news-lowres screen: (ui dump failed)
GFX news-lowres  frames=    ?  janky=? (?%)  p50=?ms p90=?ms p95=?ms p99=?ms  slowUI=? slowDraw=? highInput=?
jank calendar-lowres screen: (ui dump failed)
GFX calendar-lowres frames=    ?  janky=? (?%)  p50=?ms p90=?ms p95=?ms p99=?ms  slowUI=? slowDraw=? highInput=?
WARN: tab bar not reached
WARN: 'Want to read' tile not found in My
jank want-lowres screen: (ui dump failed)
GFX want-lowres  frames=    ?  janky=? (?%)  p50=?ms p90=?ms p95=?ms p99=?ms  slowUI=? slowDraw=? highInput=?
MEM perf-end (raw)   PSS=?KB  Java=?KB  Native=?KB  Graphics=?KB  Views=? Activities=?
MEM perf-end (trimmed) PSS=?KB  Java=?KB  Native=?KB  Graphics=?KB  Views=? Activities=?
!!! EMULATOR GONE / HUNG (at: end)
## logcat findings
app pids seen: ['5502', '6445', '6597', '6742', '6942', '7024', '7105', '7198', '7262', '7326', '7408', '7473', '7543', '7611', '7677', '7741', '7912', '7979', '8043', '8109', '8187', '8280', '8967']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=103 total=587
== Davey: 21
   davey events=21 max=1773ms median=777ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## stress

```
part stress; APK: 342758d (Проверка: пълен тест на v1.2.26 на емулатор (ВРЕМЕННО)); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15982 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## S0. First launch and warm-up
first-install catalogue results after 4976 ms
## S1. Home swipes (API stability probe: 20 swipes)
HOME SWIPE #1: emulator not responding
!!! EMULATOR GONE / HUNG (at: home swipe 1)
## logcat findings
app pids seen: ['4985', '6155']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=77 total=349
== Davey: 8
   davey events=8 max=1756ms median=960ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func

```
part func; APK: 342758d (Проверка: пълен тест на v1.2.26 на емулатор (ВРЕМЕННО)); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## F0. First launch and warm-up
first-install catalogue results after 5092 ms
## F1. Catalogue search '12'
SEARCH '12': ? results; header: 
BOOK opened: yes; texts: Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец.|СЪБИТИЕ  ·  October 16, 2026|Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец.|Днес в НЧ „Васил Левски –
BOOK chips: WantToRead=MISSING NotifyWhenAvailable=absent AskLibrary=absent
## F2. Want to read: add, list, remove, undo
WANT tap: snackbar added=no (); chip 
WANT list: book title found in the list: NO MATCH (list texts: Your list is empty|Mark a book with “Want to read” on its catalogue page — here you’ll see whether it’s on the shelf.|Back|Want to read|)
WANT: 'More actions' not found
## F3. News & events: segments and calendar subscription
NEWS tab: title='News & events' yes; segments: News[checked=false selected=false bounds=[141,344][241,397]] Calendar[checked=false selected=false bounds=[402,344][568,397]] subscribe-action=absent
CALENDAR segment: News[checked=false selected=false bounds=[141,344][241,397]] Calendar[checked=false selected=false bounds=[402,344][568,397]] subscribe-action=visible; screen: text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="Calendar view" text="All" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="NOV" text="Ден на народните будители" text="Тържество с ученически рецитал
SUBSCRIBE sheet: content-desc="Close sheet" content-desc="Drag handle" text="Subscribe in calendar" text="Add the centre’s events to your phone’s calendar. New events will appear there on their own." text="Subscribe" text="Google Calendar on Android can’t subscribe directly. Copy the link and add it at calendar.goog
calendar month view: --- screen: text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="List view" content-desc="Previous month" text="October 2026" content-desc="Next month" text="Mo" text="Tu" te
back to News: News[checked=false selected=false bounds=[141,344][241,397]] Calendar[checked=false selected=false bounds=[402,344][568,397]] subscribe-action=absent
## F4. Settings → Text size → Extra large
text size options: text="Text size" text="Multiplies the font size set on your phone. In articles, the “Aa” button adjusts it further." text="Small" text="Standard" text="Large" text="Extra large" text="Close" 
TEXT SIZE now: text="Extra large" 
xlarge catalogue: 
WARN: tab bar not reached
WARN: Settings not found
## F5. Scanner with CAMERA revoked → manual entry
!!! EMULATOR GONE / HUNG (at: F5. Scanner with CAMERA revoked → manual entry)
## logcat findings
app pids seen: ['4945']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=67 total=199
== Davey: 8
   davey events=8 max=1490ms median=1170ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look

```
part look; APK: 342758d (Проверка: пълен тест на v1.2.26 на емулатор (ВРЕМЕННО)); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## L0. First launch and warm-up
first-install catalogue results after 5095 ms
## L1. Light EN
matrix light-en: news title OK; segments News[checked=false selected=false bounds=[141,344][241,397]] Calendar[checked=false selected=false bounds=[402,344][568,397]] subscribe-action=visible
## L2. Dark
night: Night mode: yes
matrix dark-en: news title OK; segments News[checked=false selected=false bounds=[141,344][241,397]] Calendar[checked=false selected=false bounds=[402,344][568,397]] subscribe-action=visible
## L3. BG locale
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
matrix bg: news title OK; segments News[checked=false selected=false bounds=[129,344][271,397]] Calendar[checked=false selected=false bounds=[420,344][604,397]] subscribe-action=visible
## L4. Font scale 1.3
WARN: tab bar not reached
matrix font130: news title MISSING; segments News[] Calendar[] subscribe-action=absent
## L5. Landscape
!!! EMULATOR GONE / HUNG (at: L5. Landscape)
## logcat findings
app pids seen: ['4751', '5789', '6586', '7062', '7512']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 18
   skipped-frames events=18 max=88 total=828
== Davey: 26
   davey events=26 max=1642ms median=950ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 342758d (Проверка: пълен тест на v1.2.26 на емулатор (ВРЕМЕННО)); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## O1. Online warm-up (fills the caches)
first-install catalogue results after 6068 ms
contacts online: text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons"
## O2. Offline cold start with caches
offline: airplane=1
offline home: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 27, 2026 · *Св. мчк Нестор. Преп. Димитрий Басарбовски" content-desc="Latest news" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопск
offline catalogue: results shown (cached, 2500 ms)
offline book: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Available" text="Availability as of Oct 17, 2026" text="Want to read" 
offline news: text="Calendar" text="News" text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" text="СЪБИТИЕ  ·  October 16, 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „В
offline article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  October 16, 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворе
offline calendar: text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="Calendar view" text="All" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="NOV" text="Ден на народните будители" text="Тържество с ученически рецитал
offline gallery: text="29 photos" text="Фотодокументална изложба" text="4 photos" text="Фолклорна група „Явор“" text="6 photos" text="Клуб за народни танци" text="7 photos" text="Ансамбъл „Яворски хлапета“" text="5 ph
offline contacts: text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Email" text="chitalishte
## O3. Reconnect (staying on Contacts, no touch), then the other screens
contacts +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
contacts +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
recovered home: text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
recovered news: text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons"
## O4. Empty cache: offline first launch on Contacts and Gallery, then reconnect
offline: airplane=1
contacts offline (empty cache): text="Contacts couldn’t be loaded from the website — showing the basic details built into the app. They may be out of date." text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, ПК 5334" text="Email"
contacts (empty cache) +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
contacts (empty cache) +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
offline: airplane=1
gallery offline (empty cache): content-desc="Loading…" content-desc="Back" text="Gallery" text="No internet connection" 
gallery (empty cache) +25s after reconnect (no touch): content-desc="Loading…" content-desc="Back" text="Gallery" 
## COMPLETE
## logcat findings
app pids seen: ['4844', '6554', '7625']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=64 total=331
== Davey: 9
   davey events=9 max=1732ms median=1127ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### func/shots/func

- [func/shots/func/01-catalog-12.png](func/shots/func/01-catalog-12.png)
- [func/shots/func/02-book.png](func/shots/func/02-book.png)
- [func/shots/func/03-book-want-tapped.png](func/shots/func/03-book-want-tapped.png)
- [func/shots/func/04-want-list.png](func/shots/func/04-want-list.png)
- [func/shots/func/08-news-segment.png](func/shots/func/08-news-segment.png)
- [func/shots/func/09-calendar-segment.png](func/shots/func/09-calendar-segment.png)
- [func/shots/func/10-subscribe-sheet.png](func/shots/func/10-subscribe-sheet.png)
- [func/shots/func/11-calendar-month-view.png](func/shots/func/11-calendar-month-view.png)
- [func/shots/func/12-text-size-dialog.png](func/shots/func/12-text-size-dialog.png)
- [func/shots/func/13-settings-xlarge.png](func/shots/func/13-settings-xlarge.png)
- [func/shots/func/14-home-xlarge.png](func/shots/func/14-home-xlarge.png)

### look/shots/bg

- [look/shots/bg/01-home.png](look/shots/bg/01-home.png)
- [look/shots/bg/02-catalog.png](look/shots/bg/02-catalog.png)
- [look/shots/bg/03-news.png](look/shots/bg/03-news.png)
- [look/shots/bg/04-calendar.png](look/shots/bg/04-calendar.png)
- [look/shots/bg/05-book.png](look/shots/bg/05-book.png)

### look/shots/dark-en

- [look/shots/dark-en/01-home.png](look/shots/dark-en/01-home.png)
- [look/shots/dark-en/02-catalog.png](look/shots/dark-en/02-catalog.png)
- [look/shots/dark-en/03-news.png](look/shots/dark-en/03-news.png)
- [look/shots/dark-en/04-calendar.png](look/shots/dark-en/04-calendar.png)
- [look/shots/dark-en/05-book.png](look/shots/dark-en/05-book.png)

### look/shots/light-en

- [look/shots/light-en/01-home.png](look/shots/light-en/01-home.png)
- [look/shots/light-en/02-catalog.png](look/shots/light-en/02-catalog.png)
- [look/shots/light-en/03-news.png](look/shots/light-en/03-news.png)
- [look/shots/light-en/04-calendar.png](look/shots/light-en/04-calendar.png)
- [look/shots/light-en/05-book.png](look/shots/light-en/05-book.png)

### offline/shots/offline

- [offline/shots/offline/00-contacts-online.png](offline/shots/offline/00-contacts-online.png)
- [offline/shots/offline/01-home.png](offline/shots/offline/01-home.png)
- [offline/shots/offline/02-catalog.png](offline/shots/offline/02-catalog.png)
- [offline/shots/offline/03-book.png](offline/shots/offline/03-book.png)
- [offline/shots/offline/04-news.png](offline/shots/offline/04-news.png)
- [offline/shots/offline/05-article.png](offline/shots/offline/05-article.png)
- [offline/shots/offline/06-calendar.png](offline/shots/offline/06-calendar.png)
- [offline/shots/offline/07-gallery.png](offline/shots/offline/07-gallery.png)
- [offline/shots/offline/08-contacts.png](offline/shots/offline/08-contacts.png)
- [offline/shots/offline/09-contacts-after-reconnect.png](offline/shots/offline/09-contacts-after-reconnect.png)

### offline/shots/offline-empty

- [offline/shots/offline-empty/01-contacts-offline.png](offline/shots/offline-empty/01-contacts-offline.png)
- [offline/shots/offline-empty/02-contacts-after-reconnect.png](offline/shots/offline-empty/02-contacts-after-reconnect.png)
- [offline/shots/offline-empty/04-gallery-offline.png](offline/shots/offline-empty/04-gallery-offline.png)
- [offline/shots/offline-empty/05-gallery-after-reconnect.png](offline/shots/offline-empty/05-gallery-after-reconnect.png)

### offline/shots/recovered

- [offline/shots/recovered/01-home.png](offline/shots/recovered/01-home.png)
- [offline/shots/recovered/02-news.png](offline/shots/recovered/02-news.png)
- [offline/shots/recovered/03-calendar.png](offline/shots/recovered/03-calendar.png)
- [offline/shots/recovered/04-catalog.png](offline/shots/recovered/04-catalog.png)

### perf/shots

- [perf/shots/00-first-launch.png](perf/shots/00-first-launch.png)
- [perf/shots/01-home-first.png](perf/shots/01-home-first.png)
- [perf/shots/02-catalog-first.png](perf/shots/02-catalog-first.png)

### perf/shots/jank

- [perf/shots/jank/catalog-lowres-end.png](perf/shots/jank/catalog-lowres-end.png)

### start35/shots

- [start35/shots/00-first-launch.png](start35/shots/00-first-launch.png)
- [start35/shots/01-home-first.png](start35/shots/01-home-first.png)
- [start35/shots/02-catalog-first.png](start35/shots/02-catalog-first.png)

## Other files

- [func/book-texts.txt](func/book-texts.txt)
- [func/job-status.txt](func/job-status.txt)
- [func/logcat-all.txt](func/logcat-all.txt)
- [func/logcat-dump.txt](func/logcat-dump.txt)
- [func/logcat-findings.txt](func/logcat-findings.txt)
- [func/logcat-live.txt](func/logcat-live.txt)
- [func/summary.txt](func/summary.txt)
- [func/want-texts.txt](func/want-texts.txt)
- [look/job-status.txt](look/job-status.txt)
- [look/logcat-all.txt](look/logcat-all.txt)
- [look/logcat-dump.txt](look/logcat-dump.txt)
- [look/logcat-findings.txt](look/logcat-findings.txt)
- [look/logcat-live.txt](look/logcat-live.txt)
- [look/summary.txt](look/summary.txt)
- [offline/job-status.txt](offline/job-status.txt)
- [offline/logcat-all.txt](offline/logcat-all.txt)
- [offline/logcat-dump.txt](offline/logcat-dump.txt)
- [offline/logcat-findings.txt](offline/logcat-findings.txt)
- [offline/logcat-live.txt](offline/logcat-live.txt)
- [offline/summary.txt](offline/summary.txt)
- [perf/gfx-calendar-lowres.txt](perf/gfx-calendar-lowres.txt)
- [perf/gfx-catalog-lowres.txt](perf/gfx-catalog-lowres.txt)
- [perf/gfx-home-lowres.txt](perf/gfx-home-lowres.txt)
- [perf/gfx-news-lowres.txt](perf/gfx-news-lowres.txt)
- [perf/gfx-want-lowres.txt](perf/gfx-want-lowres.txt)
- [perf/job-status.txt](perf/job-status.txt)
- [perf/logcat-all.txt](perf/logcat-all.txt)
- [perf/logcat-dump.txt](perf/logcat-dump.txt)
- [perf/logcat-findings.txt](perf/logcat-findings.txt)
- [perf/logcat-live.txt](perf/logcat-live.txt)
- [perf/mem-perf-end-raw.txt](perf/mem-perf-end-raw.txt)
- [perf/mem-perf-end.txt](perf/mem-perf-end.txt)
- [perf/start-cold-sp.txt](perf/start-cold-sp.txt)
- [perf/start-cold.txt](perf/start-cold.txt)
- [perf/start-first.txt](perf/start-first.txt)
- [perf/start-warm.txt](perf/start-warm.txt)
- [perf/summary.txt](perf/summary.txt)
- [start35/job-status.txt](start35/job-status.txt)
- [start35/logcat-all.txt](start35/logcat-all.txt)
- [start35/logcat-dump.txt](start35/logcat-dump.txt)
- [start35/logcat-findings.txt](start35/logcat-findings.txt)
- [start35/logcat-live.txt](start35/logcat-live.txt)
- [start35/start-cold-sp.txt](start35/start-cold-sp.txt)
- [start35/start-cold.txt](start35/start-cold.txt)
- [start35/start-first.txt](start35/start-first.txt)
- [start35/start-warm.txt](start35/start-warm.txt)
- [start35/summary.txt](start35/summary.txt)
- [stress/job-status.txt](stress/job-status.txt)
- [stress/logcat-all.txt](stress/logcat-all.txt)
- [stress/logcat-dump.txt](stress/logcat-dump.txt)
- [stress/logcat-findings.txt](stress/logcat-findings.txt)
- [stress/logcat-live.txt](stress/logcat-live.txt)
- [stress/summary.txt](stress/summary.txt)
