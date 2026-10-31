# Emulator results

v1.2.28 full test (released 6e2c640 vs v1.2.27): [v128b/index.md](v128b/index.md)

Wave 14 check before release (52334a7, test v1.2.28 vs v1.2.27): [v128/index.md](v128/index.md)

v1.2.27 full test (released app, 8dc63c1): [v127b/index.md](v127b/index.md)

Wave 13 check before release (305a487): [v127/index.md](v127/index.md)

v1.2.26 full test (3df7f37): [v126/index.md](v126/index.md)

Source: `debug/full-test` @ a49b998, run 39989179842 (attempt 1), 2026-10-23T17:04:07Z.
Emulator: API 35 google_apis x86_64, pixel_6 profile, swiftshader (software rendering).

## Part a summary

```
APK size: 4890407 bytes
APK entries:   8782305                     1038 files
screen 1080x2400
## 1. First install: first launch, time to catalogue results
FIRST-INSTALL catalogue results after 5477 ms (tap→text, uiautomator polling ~1-2 s)
polling floor (text already on screen): 2301 ms — subtract from the times below
INDEX-READY #1 catalogue results after 2785 ms (cold start → tap Catalogue → text)
INDEX-READY #2 catalogue results after 2811 ms (cold start → tap Catalogue → text)
INDEX-READY #3 catalogue results after 2911 ms (cold start → tap Catalogue → text)
INDEX-READY deep-link #1 results 4824 ms after am start
INDEX-READY deep-link #2 results 4816 ms after am start
## 2. Startup
START cold(as installed) n=10 min=1548ms median=1694ms max=1772ms  all=[1719, 1742, 1772, 1668, 1716, 1677, 1584, 1711, 1609, 1548]  states=['COLD']
START warm(HOME→relaunch) n=5 min=271ms median=374ms max=390ms  all=[381, 390, 374, 271, 314]  states=['HOT']
compile: Success 
START cold(speed-profile) n=5 min=1533ms median=1669ms max=1957ms  all=[1957, 1669, 1656, 1779, 1533]  states=['COLD']
## 3. Jank (animations ON for this section)
GFX catalog      frames=  274  janky=270 (98.54%)  p50=150ms p90=150ms p95=200ms p99=400ms  slowUI=147 slowDraw=270 highInput=8
catalog: 'Show more' pages: 2, mis-taps recovered: 0; screen: --- screen: text="ISBN" text="9789549056746" text="Language" text="български" text="Type" text="книга" 
jank home screen: --- screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 23, 2026 · *Св. ап. �
GFX home         frames=   40  janky=40 (100.00%)  p50=150ms p90=350ms p95=650ms p99=700ms  slowUI=23 slowDraw=40 highInput=0
jank news screen: --- screen: text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" 
GFX news         frames=  171  janky=150 (87.72%)  p50=85ms p90=125ms p95=133ms p99=150ms  slowUI=71 slowDraw=150 highInput=33
jank events screen: --- screen: text="All" text="Библиотека" text="Народен обичай" text="Читалище" text="26" text="OCT" 
GFX events       frames=   36  janky=31 (86.11%)  p50=89ms p90=133ms p95=150ms p99=150ms  slowUI=16 slowDraw=31 highInput=9
GFX catalog-lowres frames=  256  janky=210 (82.03%)  p50=69ms p90=97ms p95=113ms p99=200ms  slowUI=117 slowDraw=210 highInput=81
catalog-lowres: 'Show more' pages: 4, mis-taps recovered: 0; screen: --- screen: text="Search by everything" content-desc="Scan barcode" text="Everything" text="Title" text="Author" text="ISBN" 
jank home-lowres screen: --- screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 23, 2026 · *Св. ап. �
GFX home-lowres  frames=  200  janky=166 (83.00%)  p50=81ms p90=121ms p95=150ms p99=200ms  slowUI=94 slowDraw=166 highInput=61
jank news-lowres screen: --- screen: text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" 
GFX news-lowres  frames=  294  janky=180 (61.22%)  p50=53ms p90=73ms p95=77ms p99=93ms  slowUI=91 slowDraw=179 highInput=216
jank events-lowres screen: --- screen: text="All" text="Библиотека" text="Народен обичай" text="Читалище" text="26" text="OCT" 
GFX events-lowres frames=  253  janky=170 (67.19%)  p50=61ms p90=81ms p95=85ms p99=105ms  slowUI=106 slowDraw=170 highInput=157
## 4a. Memory over 30 tab/book cycles
tab home @ 100 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=59161KB  Java=6412KB  Native=24740KB  Graphics=0KB  Views=11 Activities=1
MEM c00 (trimmed)    PSS=65286KB  Java=7556KB  Native=24716KB  Graphics=0KB  Views=11 Activities=1
cycle 1: book screen opened
MEM c10 (raw)        PSS=87290KB  Java=10136KB  Native=40060KB  Graphics=0KB  Views=15 Activities=1
MEM c10 (trimmed)    PSS=86506KB  Java=9672KB  Native=39744KB  Graphics=0KB  Views=15 Activities=1
MEM c20 (raw)        PSS=93216KB  Java=14112KB  Native=41864KB  Graphics=0KB  Views=15 Activities=1
MEM c20 (trimmed)    PSS=82412KB  Java=9736KB  Native=35436KB  Graphics=0KB  Views=15 Activities=1
MEM c30 (raw)        PSS=93775KB  Java=17484KB  Native=38984KB  Graphics=0KB  Views=15 Activities=1
MEM c30 (trimmed)    PSS=82971KB  Java=9800KB  Native=35864KB  Graphics=0KB  Views=15 Activities=1
pid after cycles: 8766
## 4c. Rotation stress (10 toggles each)
ROTATION home: pid 8766 -> 8766 (same) 
ROTATION catalog: pid 8766 -> 8766 (same) 
ROTATION catalog: query '12' kept
ROTATION book: pid 8766 -> 8766 (same) [back button visible]
GFX rotation     frames=   97  janky=95 (97.94%)  p50=48ms p90=150ms p95=200ms p99=400ms  slowUI=23 slowDraw=95 highInput=3
## 4d. Process death and restore
PROCDEATH: after am kill pid='' (killed)
PROCDEATH launcher: LaunchState: COLD first text before=text="" after=text="" → book screen restored
PROCDEATH back → catalogue query kept: no
PROCDEATH#2 pid after kill=''
PROCDEATH from Recents: query not visible 
## 4b. Monkey 4000 events
statusbar disable: 
monkey start pid=11420
MONKEY exit=0 duration=93 s; Events injected: 4000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '11420'; airplane=0
MEM after-monkey (raw) PSS=78415KB  Java=17288KB  Native=30048KB  Graphics=0KB  Views=25 Activities=1
MEM after-monkey (trimmed) PSS=70464KB  Java=10340KB  Native=24268KB  Graphics=0KB  Views=19 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['3693', '4521', '4687', '4804', '4923', '5004', '5128', '5176', '5227', '5280', '5333', '5382', '5430', '5480', '5534', '5586', '5736', '5793', '5859', '5911', '5963', '6080', '7525', '8766', '10858', '11103', '11295', '11420']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 55
   skipped-frames events=55 max=82 total=2713
== Davey: 96
   davey events=96 max=2224ms median=981ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Part b summary

```
APK size: 4890407 bytes
APK entries:   8782305                     1038 files
screen 1080x2400
## 1. First launch and warm-up (online)
first-install catalogue results after 4407 ms
## 6. Collect: package, notifications, jobs
    versionCode=1 minSdk=26 targetSdk=37
notification channels: mId='events' mId='library' mId='loans' mId='messages' mId='messages_important' mId='news' mId='updates' 
app jobs: 2; with 'Periodic/period' in JobInfo: 98; latencies: Minimum latency: +3h59m59s997ms Minimum latency: +59m59s102ms 
## 5. Appearance matrix
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
## COMPLETE
## logcat findings
app pids seen: ['4206', '4790', '5449', '6049', '6634']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=35 total=163
== Davey: 6
   davey events=6 max=981ms median=810ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Part c summary

```
APK size: 4890407 bytes
APK entries:   8782305                     1038 files
screen 1080x2400
## 1. First launch and warm-up (online, fills the caches)
first-install catalogue results after 3233 ms
landscape rail labels found: home catalog my news 
landscape: 'More' tab NOT reachable (not in the rail / UI tree)
## Launcher icon
## 4. Network: offline cold start
offline: airplane=1
offline home: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 23, 2026 · *Св. ап. Яков, брат Божий" content-desc="Latest news" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="October 16, 2026" text="Catalogue" text="Library card" 
offline catalogue: results shown (cached)
offline events: text="All" text="Библиотека" text="Народен обичай" text="Читалище" text="26" text="OCT" text="Димитровден — Курбан за здраве" text="Традиционен курбан за здраве — Читалище · Кметство · Църква." text="НАРОДЕН ОБИЧАЙ" text="1" 
offline news: text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" text="СЪБИТИЕ  ·  October 16, 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български народни танци с участието на Николай Корчев от „Плетеница“. В него се включиха танцьорите от Клуба за народни танци в Яворец и ТК „Балкански ритми“ – с. Батошево." text="СЪБИТИЕ  ·  October 16, 2026" 
offline gallery: text="25 photos" text="Новини" content-desc="Back" text="Gallery" text="No internet connection" 
## 4. Network: re-enable, auto-recovery
after reconnect (stayed on gallery, no touch): text="25 photos" text="Новини" content-desc="Back" text="Gallery" 
recovered events: text="All" text="Библиотека" text="Народен обичай" text="Читалище" text="26" text="OCT" text="Димитровден — Курбан за здраве" text="Традиционен курбан за здраве — Читалище · Кметство · Църква." text="НАРОДЕН ОБИЧАЙ" text="1" 
## Recents thumbnail / FLAG_SECURE
secure flag on window:  fl=LAYOUT_IN_SCREEN SECURE LAYOUT_INSET_DECOR SPLIT_TOUCH HARDWARE_ACCELERATED DRAWS_SYSTEM_BAR_BACKGROUNDS 
MEM part-c-end (raw) PSS=50508KB  Java=7780KB  Native=21592KB  Graphics=0KB  Views=12 Activities=1
MEM part-c-end (trimmed) PSS=57131KB  Java=9496KB  Native=21592KB  Graphics=0KB  Views=12 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['3820', '4623', '5103', '5933']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=44 total=258
== Davey: 7
   davey events=7 max=1195ms median=781ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Part d summary

```
APK size: 4890407 bytes
APK entries:   8782305                     1038 files
screen 1080x2400
## D1. Offline first launch; reconnect while on Home (positive control)
airplane=1
home offline (empty cache): text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="You’re offline" text="Check your internet connection and try again." text="Try again" text="Catalogue" text="Library card" text="Events" text="Contacts" 
catalogue offline (empty cache): text="Search by everything" content-desc="Scan barcode" text="Everything" text="Title" text="Author" text="ISBN" text="Keyword" text="You’re offline" text="Check your internet connection and try again." text="Try again" 
home +10s after reconnect (no touch): text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" content-desc="Latest news" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="October 16, 2026" text="Catalogue" 
home +20s after reconnect (no touch): text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" content-desc="Latest news" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="October 16, 2026" text="Catalogue" 
home +30s after reconnect (no touch): text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" content-desc="Latest news" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="October 16, 2026" text="Catalogue" 
news after reconnect: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" content-desc="Latest news" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." 
catalogue after reconnect: results in 2831 ms
## D2. Offline first launch; reconnect while on Gallery / Contacts / Activities
gallery offline (empty cache): text="You’re offline" text="Check your internet connection and try again." text="Try again" content-desc="Back" text="Gallery" text="No internet connection" 
gallery +10s after reconnect (no touch): text="You’re offline" text="Check your internet connection and try again." text="Try again" content-desc="Back" text="Gallery" 
gallery +20s after reconnect (no touch): text="You’re offline" text="Check your internet connection and try again." text="Try again" content-desc="Back" text="Gallery" 
gallery +30s after reconnect (no touch): text="You’re offline" text="Check your internet connection and try again." text="Try again" content-desc="Back" text="Gallery" 
contacts offline: text="You’re offline" text="Check your internet connection and try again." text="Try again" content-desc="Back" text="Contacts" text="No internet connection" 
contacts +25s after reconnect (no touch): text="You’re offline" text="Check your internet connection and try again." text="Try again" content-desc="Back" text="Contacts" 
activities offline: text="The centre’s activities, groups, projects and history — content comes straight from chyavorec.org." text="За нас" text="Клубове и Кръжоци" text="Клуб за народни танци" text="Дарение и дарителите" text="Ансамбъл „Яворски хлапета“" text="Фотодокументална изложба" text="Фолклорна група Явор" 
activities +25s after reconnect (no touch): text="The centre’s activities, groups, projects and history — content comes straight from chyavorec.org." text="За нас" text="Клубове и Кръжоци" text="Клуб за народни танци" text="Дарение и дарителите" text="Ансамбъл „Яворски хлапета“" text="Фотодокументална изложба" text="Фолклорна група Явор" 
## COMPLETE
## logcat findings
app pids seen: ['3904', '5128']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=41 total=113
== Davey: 9
   davey events=9 max=1287ms median=784ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### a/shots

- [a/shots/00-first-launch.png](a/shots/00-first-launch.png)
- [a/shots/01-home-first.png](a/shots/01-home-first.png)
- [a/shots/02-catalog-first.png](a/shots/02-catalog-first.png)
- [a/shots/monkey-end.png](a/shots/monkey-end.png)

### a/shots/jank

- [a/shots/jank/catalog-end.png](a/shots/jank/catalog-end.png)
- [a/shots/jank/catalog-lowres-end.png](a/shots/jank/catalog-lowres-end.png)
- [a/shots/jank/events-lowres.png](a/shots/jank/events-lowres.png)
- [a/shots/jank/events.png](a/shots/jank/events.png)
- [a/shots/jank/home-lowres.png](a/shots/jank/home-lowres.png)
- [a/shots/jank/home.png](a/shots/jank/home.png)
- [a/shots/jank/news-lowres.png](a/shots/jank/news-lowres.png)
- [a/shots/jank/news.png](a/shots/jank/news.png)

### a/shots/procdeath

- [a/shots/procdeath/1-before.png](a/shots/procdeath/1-before.png)
- [a/shots/procdeath/2-after-launcher.png](a/shots/procdeath/2-after-launcher.png)
- [a/shots/procdeath/3-after-back.png](a/shots/procdeath/3-after-back.png)
- [a/shots/procdeath/3b-before-kill-2.png](a/shots/procdeath/3b-before-kill-2.png)
- [a/shots/procdeath/4-recents.png](a/shots/procdeath/4-recents.png)
- [a/shots/procdeath/5-from-recents.png](a/shots/procdeath/5-from-recents.png)

### a/shots/rotation

- [a/shots/rotation/book-0-before.png](a/shots/rotation/book-0-before.png)
- [a/shots/rotation/book-1-landscape.png](a/shots/rotation/book-1-landscape.png)
- [a/shots/rotation/book-2-after.png](a/shots/rotation/book-2-after.png)
- [a/shots/rotation/catalog-0-before.png](a/shots/rotation/catalog-0-before.png)
- [a/shots/rotation/catalog-1-landscape.png](a/shots/rotation/catalog-1-landscape.png)
- [a/shots/rotation/catalog-2-after.png](a/shots/rotation/catalog-2-after.png)
- [a/shots/rotation/home-0-before.png](a/shots/rotation/home-0-before.png)
- [a/shots/rotation/home-1-landscape.png](a/shots/rotation/home-1-landscape.png)
- [a/shots/rotation/home-2-after.png](a/shots/rotation/home-2-after.png)

### b/shots/bg

- [b/shots/bg/01-home.png](b/shots/bg/01-home.png)
- [b/shots/bg/02-catalog.png](b/shots/bg/02-catalog.png)
- [b/shots/bg/03-book.png](b/shots/bg/03-book.png)
- [b/shots/bg/04-events.png](b/shots/bg/04-events.png)
- [b/shots/bg/05-news.png](b/shots/bg/05-news.png)
- [b/shots/bg/06-article.png](b/shots/bg/06-article.png)
- [b/shots/bg/07-my.png](b/shots/bg/07-my.png)
- [b/shots/bg/08-more.png](b/shots/bg/08-more.png)
- [b/shots/bg/09-settings.png](b/shots/bg/09-settings.png)
- [b/shots/bg/09b-settings-scrolled.png](b/shots/bg/09b-settings-scrolled.png)

### b/shots/dark-en

- [b/shots/dark-en/00-live-switch.png](b/shots/dark-en/00-live-switch.png)
- [b/shots/dark-en/01-home.png](b/shots/dark-en/01-home.png)
- [b/shots/dark-en/02-catalog.png](b/shots/dark-en/02-catalog.png)
- [b/shots/dark-en/03-book.png](b/shots/dark-en/03-book.png)
- [b/shots/dark-en/04-events.png](b/shots/dark-en/04-events.png)
- [b/shots/dark-en/05-news.png](b/shots/dark-en/05-news.png)
- [b/shots/dark-en/06-article.png](b/shots/dark-en/06-article.png)
- [b/shots/dark-en/07-my.png](b/shots/dark-en/07-my.png)
- [b/shots/dark-en/08-more.png](b/shots/dark-en/08-more.png)
- [b/shots/dark-en/09-settings.png](b/shots/dark-en/09-settings.png)
- [b/shots/dark-en/09b-settings-scrolled.png](b/shots/dark-en/09b-settings-scrolled.png)

### b/shots/font130

- [b/shots/font130/01-home.png](b/shots/font130/01-home.png)
- [b/shots/font130/02-catalog.png](b/shots/font130/02-catalog.png)
- [b/shots/font130/03-book.png](b/shots/font130/03-book.png)
- [b/shots/font130/04-events.png](b/shots/font130/04-events.png)
- [b/shots/font130/05-news.png](b/shots/font130/05-news.png)
- [b/shots/font130/06-article.png](b/shots/font130/06-article.png)
- [b/shots/font130/07-my.png](b/shots/font130/07-my.png)
- [b/shots/font130/08-more.png](b/shots/font130/08-more.png)
- [b/shots/font130/09-settings.png](b/shots/font130/09-settings.png)
- [b/shots/font130/09b-settings-scrolled.png](b/shots/font130/09b-settings-scrolled.png)

### b/shots/light-en

- [b/shots/light-en/01-home.png](b/shots/light-en/01-home.png)
- [b/shots/light-en/02-catalog.png](b/shots/light-en/02-catalog.png)
- [b/shots/light-en/03-book.png](b/shots/light-en/03-book.png)
- [b/shots/light-en/04-events.png](b/shots/light-en/04-events.png)
- [b/shots/light-en/05-news.png](b/shots/light-en/05-news.png)
- [b/shots/light-en/06-article.png](b/shots/light-en/06-article.png)
- [b/shots/light-en/07-my.png](b/shots/light-en/07-my.png)
- [b/shots/light-en/08-more.png](b/shots/light-en/08-more.png)
- [b/shots/light-en/09-settings.png](b/shots/light-en/09-settings.png)
- [b/shots/light-en/09b-settings-scrolled.png](b/shots/light-en/09b-settings-scrolled.png)

### c/shots/landscape

- [c/shots/landscape/01-home.png](c/shots/landscape/01-home.png)
- [c/shots/landscape/02-catalog.png](c/shots/landscape/02-catalog.png)
- [c/shots/landscape/03-book.png](c/shots/landscape/03-book.png)

### c/shots/launcher

- [c/shots/launcher/01-home-screen.png](c/shots/launcher/01-home-screen.png)
- [c/shots/launcher/02-app-drawer.png](c/shots/launcher/02-app-drawer.png)

### c/shots/offline

- [c/shots/offline/01-home.png](c/shots/offline/01-home.png)
- [c/shots/offline/02-catalog.png](c/shots/offline/02-catalog.png)
- [c/shots/offline/03-book.png](c/shots/offline/03-book.png)
- [c/shots/offline/04-events.png](c/shots/offline/04-events.png)
- [c/shots/offline/05-news.png](c/shots/offline/05-news.png)
- [c/shots/offline/06-article.png](c/shots/offline/06-article.png)
- [c/shots/offline/07-my.png](c/shots/offline/07-my.png)
- [c/shots/offline/08-more.png](c/shots/offline/08-more.png)
- [c/shots/offline/09-gallery.png](c/shots/offline/09-gallery.png)

### c/shots/recents

- [c/shots/recents/01-my-signed-out.png](c/shots/recents/01-my-signed-out.png)
- [c/shots/recents/02-recents-from-my-signed-out.png](c/shots/recents/02-recents-from-my-signed-out.png)
- [c/shots/recents/03-card-screen.png](c/shots/recents/03-card-screen.png)
- [c/shots/recents/04-recents-from-card.png](c/shots/recents/04-recents-from-card.png)
- [c/shots/recents/05-my-with-card.png](c/shots/recents/05-my-with-card.png)
- [c/shots/recents/06-recents-from-my-with-card.png](c/shots/recents/06-recents-from-my-with-card.png)

### c/shots/recovered

- [c/shots/recovered/01-home.png](c/shots/recovered/01-home.png)
- [c/shots/recovered/04-events.png](c/shots/recovered/04-events.png)
- [c/shots/recovered/05-news.png](c/shots/recovered/05-news.png)
- [c/shots/recovered/09-gallery-auto.png](c/shots/recovered/09-gallery-auto.png)

### d/shots/d1

- [d/shots/d1/01-home-offline.png](d/shots/d1/01-home-offline.png)
- [d/shots/d1/02-catalog-offline.png](d/shots/d1/02-catalog-offline.png)
- [d/shots/d1/03-news-offline.png](d/shots/d1/03-news-offline.png)
- [d/shots/d1/04-home-after-reconnect-10s.png](d/shots/d1/04-home-after-reconnect-10s.png)
- [d/shots/d1/04-home-after-reconnect-20s.png](d/shots/d1/04-home-after-reconnect-20s.png)
- [d/shots/d1/04-home-after-reconnect-30s.png](d/shots/d1/04-home-after-reconnect-30s.png)
- [d/shots/d1/05-news-after.png](d/shots/d1/05-news-after.png)
- [d/shots/d1/06-catalog-after.png](d/shots/d1/06-catalog-after.png)

### d/shots/d2

- [d/shots/d2/01-gallery-offline.png](d/shots/d2/01-gallery-offline.png)
- [d/shots/d2/02-gallery-after-reconnect-30s.png](d/shots/d2/02-gallery-after-reconnect-30s.png)
- [d/shots/d2/03-activities-offline.png](d/shots/d2/03-activities-offline.png)
- [d/shots/d2/03-contacts-offline.png](d/shots/d2/03-contacts-offline.png)
- [d/shots/d2/04-activities-after-reconnect-25s.png](d/shots/d2/04-activities-after-reconnect-25s.png)
- [d/shots/d2/04-contacts-after-reconnect-25s.png](d/shots/d2/04-contacts-after-reconnect-25s.png)

## Other files

- [a/apk-contents.txt](a/apk-contents.txt)
- [a/gfx-catalog-lowres.txt](a/gfx-catalog-lowres.txt)
- [a/gfx-catalog.txt](a/gfx-catalog.txt)
- [a/gfx-events-lowres.txt](a/gfx-events-lowres.txt)
- [a/gfx-events.txt](a/gfx-events.txt)
- [a/gfx-home-lowres.txt](a/gfx-home-lowres.txt)
- [a/gfx-home.txt](a/gfx-home.txt)
- [a/gfx-news-lowres.txt](a/gfx-news-lowres.txt)
- [a/gfx-news.txt](a/gfx-news.txt)
- [a/gfx-rotation.txt](a/gfx-rotation.txt)
- [a/logcat-all.txt](a/logcat-all.txt)
- [a/logcat-dump.txt](a/logcat-dump.txt)
- [a/logcat-findings.txt](a/logcat-findings.txt)
- [a/logcat-live.txt](a/logcat-live.txt)
- [a/mem-after-monkey-raw.txt](a/mem-after-monkey-raw.txt)
- [a/mem-after-monkey.txt](a/mem-after-monkey.txt)
- [a/mem-c00-raw.txt](a/mem-c00-raw.txt)
- [a/mem-c00.txt](a/mem-c00.txt)
- [a/mem-c10-raw.txt](a/mem-c10-raw.txt)
- [a/mem-c10.txt](a/mem-c10.txt)
- [a/mem-c20-raw.txt](a/mem-c20-raw.txt)
- [a/mem-c20.txt](a/mem-c20.txt)
- [a/mem-c30-raw.txt](a/mem-c30-raw.txt)
- [a/mem-c30.txt](a/mem-c30.txt)
- [a/monkey.txt](a/monkey.txt)
- [a/start-cold-sp.txt](a/start-cold-sp.txt)
- [a/start-cold.txt](a/start-cold.txt)
- [a/start-first.txt](a/start-first.txt)
- [a/start-procdeath.txt](a/start-procdeath.txt)
- [a/start-warm.txt](a/start-warm.txt)
- [a/summary.txt](a/summary.txt)
- [b/apk-contents.txt](b/apk-contents.txt)
- [b/dumpsys-jobscheduler.txt](b/dumpsys-jobscheduler.txt)
- [b/dumpsys-notification.txt](b/dumpsys-notification.txt)
- [b/dumpsys-package.txt](b/dumpsys-package.txt)
- [b/jobs-app.txt](b/jobs-app.txt)
- [b/jobs-summary.txt](b/jobs-summary.txt)
- [b/logcat-all.txt](b/logcat-all.txt)
- [b/logcat-dump.txt](b/logcat-dump.txt)
- [b/logcat-findings.txt](b/logcat-findings.txt)
- [b/logcat-live.txt](b/logcat-live.txt)
- [b/notification-channels.txt](b/notification-channels.txt)
- [b/package-summary.txt](b/package-summary.txt)
- [b/summary.txt](b/summary.txt)
- [c/apk-contents.txt](c/apk-contents.txt)
- [c/logcat-all.txt](c/logcat-all.txt)
- [c/logcat-dump.txt](c/logcat-dump.txt)
- [c/logcat-findings.txt](c/logcat-findings.txt)
- [c/logcat-live.txt](c/logcat-live.txt)
- [c/mem-part-c-end-raw.txt](c/mem-part-c-end-raw.txt)
- [c/mem-part-c-end.txt](c/mem-part-c-end.txt)
- [c/summary.txt](c/summary.txt)
- [d/apk-contents.txt](d/apk-contents.txt)
- [d/logcat-all.txt](d/logcat-all.txt)
- [d/logcat-dump.txt](d/logcat-dump.txt)
- [d/logcat-findings.txt](d/logcat-findings.txt)
- [d/logcat-live.txt](d/logcat-live.txt)
- [d/summary.txt](d/summary.txt)
