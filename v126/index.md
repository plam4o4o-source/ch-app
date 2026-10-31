# v1.2.26 full emulator test (round 2)

Round 5 (landscape, Want to read jank): [run5/index.md](run5/index.md); round 4 (text size on the catalogue): [run4/index.md](run4/index.md); round 3 (what this round lost, and -gpu swangle_indirect): [run3/index.md](run3/index.md)

Round 4 (text size on the catalogue, Want to read jank): [run4/index.md](run4/index.md); round 3 (what this round lost, and -gpu swangle_indirect): [run3/index.md](run3/index.md)

Round 3 (what this round lost, and -gpu swangle_indirect): [run3/index.md](run3/index.md)

Source: `debug/full-test-2` @ 4ca6dcb (app = origin/main 3df7f37), run 40532592517 (attempt 1), 2026-10-27T15:52:38Z.
Emulators: google_apis x86_64, pixel_6 profile, swiftshader (software rendering); API 33 except probe34 (API 34).
Round 1 (start35 on API 35, the rest on API 34): [run1/index.md](run1/index.md).

| part | status |
|---|---|
| func1 | COMPLETE; part=func1 api=33 emulator-step=success |
| func2 | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: F5. Scanner with CAMERA revoked → manual entry)); part=func2 api=33 emulator-step=failure |
| look1 | COMPLETE; part=look1 api=33 emulator-step=success |
| look2 | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: L5. Landscape)); part=look2 api=33 emulator-step=failure |
| offline | COMPLETE; part=offline api=33 emulator-step=success |
| perf | COMPLETE; part=perf api=33 emulator-step=success |
| probe34 | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: home swipe 1)); part=probe34 api=34 emulator-step=failure |
| stress1 | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: home swipe 1)); part=stress1 api=33 emulator-step=failure |
| stress2 | COMPLETE; part=stress2 api=33 emulator-step=success |
| stress3 | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: after monkey)); part=stress3 api=33 emulator-step=failure |

## func1

```
part func1; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 5010 ms
## F1. Catalogue search '12'
SEARCH '12': 108 results; header: text="108 results  ·  InvLib data as of Oct 17, 2026"
BOOK opened: yes; texts: Спомен за здрача|Спомен за здрача|Веска Пенчева|Not on the shelf|Availability as of Oct 17, 2026|Want to read|
BOOK chips: WantToRead=present [checked=false selected=false bounds=[426,738][793,864]] NotifyWhenAvailable=present AskLibrary=present
## F2. Want to read: add, list, remove, undo
WANT tap: snackbar added=yes; chip checked=true selected=false bounds=[426,738][793,864]
WANT list: book title found in the list: Availability as of Oct 17, 2026 (list texts: Availability as of Oct 17, 2026|Swipe a book aside to remove it.|Спомен за здрача|Спомен за здрача|Веска Пенчева|Not on the shelf|More actions|Back|)
WANT remove: snackbar 'text="“Спомен за здрача” removed from your list"' undo=present; title still listed: no
WANT undo: title back in list: yes
## F2b. A book that is not on the shelf ('Notify me when available')
NOTIFY: book not on the shelf: content-desc="Спомен за здрача" text="Спомен за здрача" text="Веска Пенчева" text="Not on the shelf" text="Availability as of Oct 17, 2026" text="Want to read" text="Ask the library" text="Notify me when available" 
## F3. News & events: segments and calendar subscription
NEWS tab: title='News & events' yes; segments: News[checked=true selected=false bounds=[42,308][339,434]] Calendar[checked=false selected=false bounds=[339,308][633,434]] subscribe-action=absent
CALENDAR segment: News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible; screen: text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="Calendar view" text="All" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="NOV" text="Ден на народните будители" text="Тържество с ученически рецитал
SUBSCRIBE sheet: content-desc="Close sheet" content-desc="Drag handle" text="Subscribe in calendar" text="Add the centre’s events to your phone’s calendar. New events will appear there on their own." text="Subscribe" text="Google Calendar on Android can’t subscribe directly. Copy the link and add it at calendar.goog
calendar month view: --- screen: text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="List view" content-desc="Previous month" text="October 2026" content-desc="Next month" text="Mo" text="Tu" te
back to News: News[checked=true selected=false bounds=[42,308][339,434]] Calendar[checked=false selected=false bounds=[339,308][633,434]] subscribe-action=absent
## COMPLETE
## logcat findings
app pids seen: ['5711']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=57 total=209
== Davey: 7
   davey events=7 max=1471ms median=1000ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func2

```
part func2; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 3036 ms
## F4. Settings → Text size → Extra large
text size options: text="Text size" text="Multiplies the font size set on your phone. In articles, the “Aa” button adjusts it further." text="Small" text="Standard" text="Large" text="Extra large" text="Close" 
TEXT SIZE now: text="Extra large" 
xlarge catalogue: 
WARN: tab bar not reached
WARN: Settings not found
## F5. Scanner with CAMERA revoked → manual entry
!!! EMULATOR GONE / HUNG (at: F5. Scanner with CAMERA revoked → manual entry)
## logcat findings
app pids seen: ['6313']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=54 total=54
== Davey: 2
   davey events=2 max=927ms median=842ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look1

```
part look1; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## L0. First launch and warm-up
first-install catalogue results after 5286 ms
## L1. Light EN
matrix light-en: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
## L2. Dark
night: Night mode: yes
matrix dark-en: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
## L3. BG locale
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
matrix bg: news title OK; segments News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
## COMPLETE
## logcat findings
app pids seen: ['4615', '6009', '7526', '7999']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 17
   skipped-frames events=17 max=119 total=922
== Davey: 22
   davey events=22 max=2439ms median=1114ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look2

```
part look2; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
first-install catalogue results after 5061 ms
## L4. Font scale 1.3
WARN: tab bar not reached
matrix font130: news title MISSING; segments News[] Calendar[] subscribe-action=absent
## L5. Landscape
!!! EMULATOR GONE / HUNG (at: L5. Landscape)
## logcat findings
app pids seen: ['4753', '5931']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=110 total=390
== Davey: 11
   davey events=11 max=2452ms median=1333ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## O1. Online warm-up (fills the caches)
first-install catalogue results after 5489 ms
contacts online: text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons"
## O2. Offline cold start with caches
offline: airplane=1
offline home: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 27, 2026 · *Св. мчк Нестор. Преп. Димитрий Басарбовски" content-desc="Latest news" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопск
offline catalogue: results shown (cached, 2812 ms)
offline book: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Available" text="Availability as of Oct 17, 2026" text="Want to read" 
offline news: text="Calendar" text="News" text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" text="СЪБИТИЕ  ·  October 16, 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „В
offline article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  October 16, 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворе
offline calendar: text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="Calendar view" text="All" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="NOV" text="Ден на народните будители" text="Тържество с ученически рецитал
offline gallery: text="29 photos" text="Фотодокументална изложба" text="4 photos" text="Фолклорна група „Явор“" text="6 photos" text="Клуб за народни танци" text="7 photos" text="Ансамбъл „Яворски хлапета“" text="5 ph
offline contacts: text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Email" text="chitalishte
## O3. Reconnect (staying on Contacts, no touch), then the other screens
contacts +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
contacts +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
recovered home: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 27, 2026 · *Св. мчк Нестор. Преп. Димитрий Басарбовски" content-desc="Latest news" text="СЪБИТИЕ
recovered news (link /news): News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible; text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="Calendar view" text="All" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="NOV" 
recovered gallery: text="29 photos" text="Фотодокументална изложба" text="4 photos" text="Фолклорна група „Явор“" text="6 photos" text="Клуб за народни танци" text="7 photos" text="Ансамбъл „Яворски хлапета“" 
## O4. Empty cache: offline first launch on Contacts and Gallery, then reconnect
offline: airplane=1
contacts offline (empty cache): text="Contacts couldn’t be loaded from the website — showing the basic details built into the app. They may be out of date." text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, ПК 5334" text="Email"
contacts (empty cache) +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
contacts (empty cache) +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Send email" text="Open map" text="Open website" text="Address" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Contact persons" text="Председател: Пламен Христов" text="Секретар
offline: airplane=1
gallery offline (empty cache): content-desc="Loading…" content-desc="Back" text="Gallery" 
gallery offline (empty cache) after 25 s: content-desc="Loading…" content-desc="Back" text="Gallery" 
gallery (empty cache) +25s after reconnect (no touch): content-desc="Loading…" content-desc="Back" text="Gallery" 
gallery (empty cache) +60s after reconnect (no touch): text="25 photos" text="News" content-desc="Back" text="Gallery" 
gallery reopened (back + Gallery): text="25 photos" text="News" content-desc="Back" text="Gallery" 
## COMPLETE
## logcat findings
app pids seen: ['4729', '7827', '9116']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 12
   skipped-frames events=12 max=53 total=448
== Davey: 12
   davey events=12 max=1583ms median=1004ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## perf

```
part perf; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 1. First install: first launch, time to catalogue results
home quick actions: Scan=yes MyBooks=no Contacts=yes; 'Catalogue' labels on screen: 1 (1 = only the tab)
FIRST-INSTALL catalogue results after 5927 ms (tap→text, uiautomator polling)
catalogue hint: 'Search everything' OK
polling floor (text already on screen): 2496 ms — subtract from the times below
INDEX-READY #1 catalogue results after 2927 ms (cold start → tap Catalogue → text)
INDEX-READY #2 catalogue results after 2677 ms (cold start → tap Catalogue → text)
INDEX-READY #3 catalogue results after 2470 ms (cold start → tap Catalogue → text)
INDEX-READY deep-link #1 results 6710 ms after am start
INDEX-READY deep-link #2 results 4807 ms after am start
## 2. Startup
START cold(as installed) n=10 min=1693ms median=1798ms max=1985ms  all=[1795, 1738, 1807, 1802, 1693, 1787, 1701, 1876, 1865, 1985]  states=['COLD']
START warm(HOME→relaunch) n=5 min=285ms median=309ms max=366ms  all=[285, 292, 318, 366, 309]  states=['HOT']
compile: Success 
START cold(speed-profile) n=5 min=1630ms median=1735ms max=1814ms  all=[1713, 1814, 1735, 1742, 1630]  states=['COLD']
Want to read: added 3 books for the list jank
## 3. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX catalog-lowres frames=  372  janky=306 (82.26%)  p50=73ms p90=105ms p95=121ms p99=200ms  slowUI=168 slowDraw=306 highInput=126
catalog-lowres: 30 swipes, 'Show more' pages: 6, mis-taps recovered: 0; screen: --- screen: text="Search everything" content-desc="Scan barcode" text="Everything" text="Title" text="Author" text="ISBN" 
want list: 1 rows visible
jank want-lowres screen: --- screen: text="Availability as of Oct 17, 2026" text="Swipe a book aside to remove it." content-desc="Гласът на горите" text="Гласът на горите" text="Катя Тонева" text="Available" content-desc="More actions" content-d
GFX want-lowres  frames=    1  janky=1 (100.00%)  p50=16ms p90=16ms p95=16ms p99=16ms  slowUI=0 slowDraw=1 highInput=0
jank news-lowres screen: --- screen: text="Calendar" text="News" text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" 
GFX news-lowres  frames=  176  janky=133 (75.57%)  p50=65ms p90=81ms p95=89ms p99=101ms  slowUI=79 slowDraw=133 highInput=86
jank calendar-lowres screen: --- screen: text="News" text="Calendar" content-desc="Subscribe in calendar" content-desc="Calendar view" text="All" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX calendar-lowres frames=  254  janky=176 (69.29%)  p50=57ms p90=81ms p95=85ms p99=97ms  slowUI=98 slowDraw=176 highInput=153
jank home-lowres screen: --- screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 27, 2026 · *Св. мчк Нестор. Преп. Димитрий Басарбовски
GFX home-lowres  frames=  199  janky=179 (89.95%)  p50=85ms p90=113ms p95=125ms p99=200ms  slowUI=109 slowDraw=179 highInput=40
MEM perf-end (raw)   PSS=116609KB  Java=15116KB  Native=62184KB  Graphics=0KB  Views=10 Activities=1
MEM perf-end (trimmed) PSS=118668KB  Java=15848KB  Native=58504KB  Graphics=0KB  Views=10 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5116', '7098', '7475', '7621', '7768', '7901', '7987', '8052', '8103', '8161', '8217', '8269', '8319', '8372', '8423', '8474', '8674', '8734', '8786', '8839', '8893', '8976', '9780']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 54
   skipped-frames events=54 max=120 total=2623
== Davey: 83
   davey events=83 max=2607ms median=1168ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## probe34

```
part probe34; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 5147 ms
## S1. Home swipes (API stability probe: 20 swipes)
HOME SWIPE #1: emulator not responding
!!! EMULATOR GONE / HUNG (at: home swipe 1)
## logcat findings
app pids seen: ['4734', '5985']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=77 total=299
== Davey: 11
   davey events=11 max=1674ms median=1017ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## stress1

```
part stress1; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 2473 ms
## S2. Memory over 30 tab/book cycles
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=64279KB  Java=7600KB  Native=26072KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=68714KB  Java=8244KB  Native=26056KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c10 (raw)        PSS=87489KB  Java=16908KB  Native=33252KB  Graphics=0KB  Views=13 Activities=1
MEM c10 (trimmed)    PSS=78284KB  Java=10812KB  Native=30440KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (raw)        PSS=94882KB  Java=17652KB  Native=40020KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (trimmed)    PSS=83799KB  Java=10880KB  Native=35884KB  Graphics=0KB  Views=13 Activities=1
MEM c30 (raw)        PSS=84658KB  Java=11488KB  Native=35708KB  Graphics=0KB  Views=13 Activities=1
MEM c30 (trimmed)    PSS=83423KB  Java=10808KB  Native=35180KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 7174
## S1. Home swipes (API stability probe: 20 swipes)
HOME SWIPE #1: emulator not responding
!!! EMULATOR GONE / HUNG (at: home swipe 1)
## logcat findings
app pids seen: ['6161', '7174', '8737']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=37 total=164
== Davey: 9
   davey events=9 max=1126ms median=783ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## stress2

```
part stress2; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 5323 ms
## S3. Rotation stress (10 toggles each)
ROTATION home: pid 7223 -> 7223 (same) 
catalogue '12' before rotation: 108 results
ROTATION catalog: pid 7223 -> 7223 (same) 
ROTATION catalog: query '12' kept (108 results)
ROTATION book: pid 7223 -> 7223 (same) [back button visible]
GFX rotation     frames=  122  janky=118 (96.72%)  p50=150ms p90=300ms p95=400ms p99=550ms  slowUI=83 slowDraw=118 highInput=7
## S4. Process death and restore (catalogue query + Calendar segment)
before: catalogue query '12': shown, 108 results
before: News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
PROCDEATH: after am kill pid='' (killed)
PROCDEATH launcher: LaunchState: COLD; title News&events: yes; News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
PROCDEATH catalogue query kept: yes (108 results)
PROCDEATH#2 pid after kill=''
PROCDEATH from Recents: News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
PROCDEATH from Recents: catalogue query kept
## COMPLETE
## logcat findings
app pids seen: ['5232', '7223', '8752', '9130', '9568']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 9
   skipped-frames events=9 max=77 total=428
== Davey: 16
   davey events=16 max=1630ms median=1024ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## stress3

```
part stress3; APK: 4ca6dcb (Проверка v1.2.26: втори кръг на API 33, по-малки части, поправки в скрипта); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 4966 ms
## S5. Monkey 4000 events (seed 1234, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
statusbar disable: 
monkey start pid=6956
MONKEY exit=255 duration=45 s; 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
!!! EMULATOR GONE / HUNG (at: after monkey)
## logcat findings
app pids seen: ['5187', '6956']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 10
   skipped-frames events=10 max=78 total=516
== Davey: 11
   davey events=11 max=1906ms median=1200ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### func1/shots/func

- [func1/shots/func/01-catalog-12.png](func1/shots/func/01-catalog-12.png)
- [func1/shots/func/02-book.png](func1/shots/func/02-book.png)
- [func1/shots/func/03-book-want-tapped.png](func1/shots/func/03-book-want-tapped.png)
- [func1/shots/func/04-want-list.png](func1/shots/func/04-want-list.png)
- [func1/shots/func/05-want-menu.png](func1/shots/func/05-want-menu.png)
- [func1/shots/func/06-want-removed-snackbar.png](func1/shots/func/06-want-removed-snackbar.png)
- [func1/shots/func/07-want-after-undo.png](func1/shots/func/07-want-after-undo.png)
- [func1/shots/func/07b-book-not-on-shelf.png](func1/shots/func/07b-book-not-on-shelf.png)
- [func1/shots/func/08-news-segment.png](func1/shots/func/08-news-segment.png)
- [func1/shots/func/09-calendar-segment.png](func1/shots/func/09-calendar-segment.png)
- [func1/shots/func/10-subscribe-sheet.png](func1/shots/func/10-subscribe-sheet.png)
- [func1/shots/func/11-calendar-month-view.png](func1/shots/func/11-calendar-month-view.png)

### func2/shots/func

- [func2/shots/func/12-text-size-dialog.png](func2/shots/func/12-text-size-dialog.png)
- [func2/shots/func/13-settings-xlarge.png](func2/shots/func/13-settings-xlarge.png)

### look1/shots/bg

- [look1/shots/bg/01-home.png](look1/shots/bg/01-home.png)
- [look1/shots/bg/02-catalog.png](look1/shots/bg/02-catalog.png)
- [look1/shots/bg/03-news.png](look1/shots/bg/03-news.png)
- [look1/shots/bg/04-calendar.png](look1/shots/bg/04-calendar.png)
- [look1/shots/bg/05-book.png](look1/shots/bg/05-book.png)

### look1/shots/dark-en

- [look1/shots/dark-en/01-home.png](look1/shots/dark-en/01-home.png)
- [look1/shots/dark-en/02-catalog.png](look1/shots/dark-en/02-catalog.png)
- [look1/shots/dark-en/03-news.png](look1/shots/dark-en/03-news.png)
- [look1/shots/dark-en/04-calendar.png](look1/shots/dark-en/04-calendar.png)
- [look1/shots/dark-en/05-book.png](look1/shots/dark-en/05-book.png)

### look1/shots/light-en

- [look1/shots/light-en/01-home.png](look1/shots/light-en/01-home.png)
- [look1/shots/light-en/02-catalog.png](look1/shots/light-en/02-catalog.png)
- [look1/shots/light-en/03-news.png](look1/shots/light-en/03-news.png)
- [look1/shots/light-en/04-calendar.png](look1/shots/light-en/04-calendar.png)
- [look1/shots/light-en/05-book.png](look1/shots/light-en/05-book.png)

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
- [offline/shots/offline-empty/04b-gallery-offline-25s.png](offline/shots/offline-empty/04b-gallery-offline-25s.png)
- [offline/shots/offline-empty/05-gallery-after-reconnect.png](offline/shots/offline-empty/05-gallery-after-reconnect.png)
- [offline/shots/offline-empty/05b-gallery-after-reconnect-60s.png](offline/shots/offline-empty/05b-gallery-after-reconnect-60s.png)
- [offline/shots/offline-empty/07-gallery-reopened.png](offline/shots/offline-empty/07-gallery-reopened.png)

### offline/shots/recovered

- [offline/shots/recovered/01-home.png](offline/shots/recovered/01-home.png)
- [offline/shots/recovered/02-news.png](offline/shots/recovered/02-news.png)
- [offline/shots/recovered/03-calendar.png](offline/shots/recovered/03-calendar.png)
- [offline/shots/recovered/04-catalog.png](offline/shots/recovered/04-catalog.png)
- [offline/shots/recovered/05-gallery.png](offline/shots/recovered/05-gallery.png)

### perf/shots

- [perf/shots/00-first-launch.png](perf/shots/00-first-launch.png)
- [perf/shots/01-home-first.png](perf/shots/01-home-first.png)
- [perf/shots/02-catalog-first.png](perf/shots/02-catalog-first.png)

### perf/shots/jank

- [perf/shots/jank/calendar-lowres.png](perf/shots/jank/calendar-lowres.png)
- [perf/shots/jank/catalog-lowres-end.png](perf/shots/jank/catalog-lowres-end.png)
- [perf/shots/jank/home-lowres.png](perf/shots/jank/home-lowres.png)
- [perf/shots/jank/news-lowres.png](perf/shots/jank/news-lowres.png)
- [perf/shots/jank/want-lowres.png](perf/shots/jank/want-lowres.png)

### stress2/shots/procdeath

- [stress2/shots/procdeath/1-catalog-before.png](stress2/shots/procdeath/1-catalog-before.png)
- [stress2/shots/procdeath/2-calendar-before.png](stress2/shots/procdeath/2-calendar-before.png)
- [stress2/shots/procdeath/3-after-launcher.png](stress2/shots/procdeath/3-after-launcher.png)
- [stress2/shots/procdeath/4-catalog-after.png](stress2/shots/procdeath/4-catalog-after.png)
- [stress2/shots/procdeath/5-recents.png](stress2/shots/procdeath/5-recents.png)
- [stress2/shots/procdeath/6-from-recents.png](stress2/shots/procdeath/6-from-recents.png)

### stress2/shots/rotation

- [stress2/shots/rotation/book-0-before.png](stress2/shots/rotation/book-0-before.png)
- [stress2/shots/rotation/book-1-landscape.png](stress2/shots/rotation/book-1-landscape.png)
- [stress2/shots/rotation/book-2-after.png](stress2/shots/rotation/book-2-after.png)
- [stress2/shots/rotation/catalog-0-before.png](stress2/shots/rotation/catalog-0-before.png)
- [stress2/shots/rotation/catalog-1-landscape.png](stress2/shots/rotation/catalog-1-landscape.png)
- [stress2/shots/rotation/catalog-2-after.png](stress2/shots/rotation/catalog-2-after.png)
- [stress2/shots/rotation/home-0-before.png](stress2/shots/rotation/home-0-before.png)
- [stress2/shots/rotation/home-1-landscape.png](stress2/shots/rotation/home-1-landscape.png)
- [stress2/shots/rotation/home-2-after.png](stress2/shots/rotation/home-2-after.png)

## Other files

- [func1/book-texts.txt](func1/book-texts.txt)
- [func1/host-after-run.txt](func1/host-after-run.txt)
- [func1/job-status.txt](func1/job-status.txt)
- [func1/logcat-all.txt](func1/logcat-all.txt)
- [func1/logcat-dump.txt](func1/logcat-dump.txt)
- [func1/logcat-findings.txt](func1/logcat-findings.txt)
- [func1/logcat-live.txt](func1/logcat-live.txt)
- [func1/summary.txt](func1/summary.txt)
- [func1/want-texts.txt](func1/want-texts.txt)
- [func2/host-after-run.txt](func2/host-after-run.txt)
- [func2/job-status.txt](func2/job-status.txt)
- [func2/logcat-all.txt](func2/logcat-all.txt)
- [func2/logcat-dump.txt](func2/logcat-dump.txt)
- [func2/logcat-findings.txt](func2/logcat-findings.txt)
- [func2/logcat-live.txt](func2/logcat-live.txt)
- [func2/summary.txt](func2/summary.txt)
- [look1/host-after-run.txt](look1/host-after-run.txt)
- [look1/job-status.txt](look1/job-status.txt)
- [look1/logcat-all.txt](look1/logcat-all.txt)
- [look1/logcat-dump.txt](look1/logcat-dump.txt)
- [look1/logcat-findings.txt](look1/logcat-findings.txt)
- [look1/logcat-live.txt](look1/logcat-live.txt)
- [look1/summary.txt](look1/summary.txt)
- [look2/host-after-run.txt](look2/host-after-run.txt)
- [look2/job-status.txt](look2/job-status.txt)
- [look2/logcat-all.txt](look2/logcat-all.txt)
- [look2/logcat-dump.txt](look2/logcat-dump.txt)
- [look2/logcat-findings.txt](look2/logcat-findings.txt)
- [look2/logcat-live.txt](look2/logcat-live.txt)
- [look2/summary.txt](look2/summary.txt)
- [offline/host-after-run.txt](offline/host-after-run.txt)
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
- [perf/host-after-run.txt](perf/host-after-run.txt)
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
- [probe34/host-after-run.txt](probe34/host-after-run.txt)
- [probe34/job-status.txt](probe34/job-status.txt)
- [probe34/logcat-all.txt](probe34/logcat-all.txt)
- [probe34/logcat-dump.txt](probe34/logcat-dump.txt)
- [probe34/logcat-findings.txt](probe34/logcat-findings.txt)
- [probe34/logcat-live.txt](probe34/logcat-live.txt)
- [probe34/summary.txt](probe34/summary.txt)
- [stress1/host-after-run.txt](stress1/host-after-run.txt)
- [stress1/job-status.txt](stress1/job-status.txt)
- [stress1/logcat-all.txt](stress1/logcat-all.txt)
- [stress1/logcat-dump.txt](stress1/logcat-dump.txt)
- [stress1/logcat-findings.txt](stress1/logcat-findings.txt)
- [stress1/logcat-live.txt](stress1/logcat-live.txt)
- [stress1/mem-c00-raw.txt](stress1/mem-c00-raw.txt)
- [stress1/mem-c00.txt](stress1/mem-c00.txt)
- [stress1/mem-c10-raw.txt](stress1/mem-c10-raw.txt)
- [stress1/mem-c10.txt](stress1/mem-c10.txt)
- [stress1/mem-c20-raw.txt](stress1/mem-c20-raw.txt)
- [stress1/mem-c20.txt](stress1/mem-c20.txt)
- [stress1/mem-c30-raw.txt](stress1/mem-c30-raw.txt)
- [stress1/mem-c30.txt](stress1/mem-c30.txt)
- [stress1/summary.txt](stress1/summary.txt)
- [stress2/gfx-rotation.txt](stress2/gfx-rotation.txt)
- [stress2/host-after-run.txt](stress2/host-after-run.txt)
- [stress2/job-status.txt](stress2/job-status.txt)
- [stress2/logcat-all.txt](stress2/logcat-all.txt)
- [stress2/logcat-dump.txt](stress2/logcat-dump.txt)
- [stress2/logcat-findings.txt](stress2/logcat-findings.txt)
- [stress2/logcat-live.txt](stress2/logcat-live.txt)
- [stress2/start-procdeath.txt](stress2/start-procdeath.txt)
- [stress2/summary.txt](stress2/summary.txt)
- [stress3/host-after-run.txt](stress3/host-after-run.txt)
- [stress3/job-status.txt](stress3/job-status.txt)
- [stress3/logcat-all.txt](stress3/logcat-all.txt)
- [stress3/logcat-dump.txt](stress3/logcat-dump.txt)
- [stress3/logcat-findings.txt](stress3/logcat-findings.txt)
- [stress3/logcat-live.txt](stress3/logcat-live.txt)
- [stress3/monkey.txt](stress3/monkey.txt)
- [stress3/summary.txt](stress3/summary.txt)
