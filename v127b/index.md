# v1.2.27 full emulator test (released app, origin/main 8dc63c1)

Source: `debug/full-test-3` @ 63931da (app = 8dc63c1, compared with v1.2.26 = 3df7f37), run 40920245109 (attempt 1), 2026-10-29T17:48:26Z.
Emulators: google_apis x86_64 API 33, pixel_6 profile, -gpu swangle_indirect; app locale bg-BG (EN only in look1). APKs: minified prodRelease, x86_64 added, throwaway key, versionCode 1484479.
Wave 13 check: [../v127/index.md](../v127/index.md) and [../v127/run2/index.md](../v127/run2/index.md). v1.2.26 full test: [../v126/index.md](../v126/index.md).

| part | status |
|---|---|
| abstart-1 | COMPLETE; part=abstart-1 api=33 gpu=swangle_indirect emulator-step=success |
| abstart-2 | COMPLETE; part=abstart-2 api=33 gpu=swangle_indirect emulator-step=success |
| func1 | COMPLETE; part=func1 api=33 gpu=swangle_indirect emulator-step=success |
| func2 | COMPLETE; part=func2 api=33 gpu=swangle_indirect emulator-step=success |
| jank-1 | COMPLETE; part=jank-1 api=33 gpu=swangle_indirect emulator-step=success |
| jank-2 | COMPLETE; part=jank-2 api=33 gpu=swangle_indirect emulator-step=success |
| look1 | COMPLETE; part=look1 api=33 gpu=swangle_indirect emulator-step=success |
| look2 | COMPLETE; part=look2 api=33 gpu=swangle_indirect emulator-step=success |
| look3 | COMPLETE; part=look3 api=33 gpu=swangle_indirect emulator-step=success |
| media | COMPLETE; part=media api=33 gpu=swangle_indirect emulator-step=success |
| mem-base | COMPLETE; part=mem-base api=33 gpu=swangle_indirect emulator-step=success |
| mem | COMPLETE; part=mem api=33 gpu=swangle_indirect emulator-step=success |
| monkey | COMPLETE; part=monkey api=33 gpu=swangle_indirect emulator-step=success |
| offline | COMPLETE; part=offline api=33 gpu=swangle_indirect emulator-step=success |
| rot | COMPLETE; part=rot api=33 gpu=swangle_indirect emulator-step=success |

## abstart-1

```
part abstart-1; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2854 ms
new-1 INDEX-READY #1 catalogue results after 3751 ms
new-1 INDEX-READY #2 catalogue results after 2727 ms
new-1 INDEX-READY #3 catalogue results after 2622 ms
START cold new-1         n=10 min=1135ms median=1406ms max=1532ms  all=[1390, 1168, 1464, 1351, 1245, 1135, 1494, 1532, 1501, 1422]  states=['COLD']
START warm new-1         n=10 min=236ms median=349ms max=579ms  all=[309, 579, 516, 329, 337, 352, 236, 346, 416, 370]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4 size 4924210;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.26 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2493 ms
base-1 INDEX-READY #1 catalogue results after 2495 ms
base-1 INDEX-READY #2 catalogue results after 2859 ms
base-1 INDEX-READY #3 catalogue results after 2561 ms
START cold base-1        n=10 min=1041ms median=1344ms max=1573ms  all=[1130, 1109, 1041, 1480, 1413, 1573, 1489, 1293, 1191, 1396]  states=['COLD']
START warm base-1        n=10 min=225ms median=334ms max=486ms  all=[323, 424, 225, 307, 436, 486, 344, 418, 290, 310]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4 size 4924210;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.26 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2553 ms
base-2 INDEX-READY #1 catalogue results after 2680 ms
base-2 INDEX-READY #2 catalogue results after 2439 ms
base-2 INDEX-READY #3 catalogue results after 2684 ms
START cold base-2        n=10 min=1108ms median=1299ms max=1447ms  all=[1424, 1285, 1251, 1447, 1383, 1246, 1197, 1369, 1108, 1313]  states=['COLD']
START warm base-2        n=10 min=227ms median=328ms max=494ms  all=[417, 494, 249, 483, 241, 275, 227, 359, 430, 297]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2492 ms
new-2 INDEX-READY #1 catalogue results after 2577 ms
new-2 INDEX-READY #2 catalogue results after 2635 ms
new-2 INDEX-READY #3 catalogue results after 2712 ms
START cold new-2         n=10 min=1106ms median=1234ms max=1355ms  all=[1322, 1355, 1221, 1341, 1246, 1326, 1177, 1192, 1106, 1193]  states=['COLD']
START warm new-2         n=10 min=227ms median=320ms max=466ms  all=[466, 341, 325, 310, 310, 318, 380, 227, 275, 322]  states=['HOT']
## AB totals
START cold NEW 1.2.27    n=20 min=1106ms median=1324ms max=1532ms  all=[1390, 1168, 1464, 1351, 1245, 1135, 1494, 1532, 1501, 1422, 1322, 1355, 1221, 1341, 1246, 1326, 1177, 1192, 1106, 1193]  states=['COLD']
START cold BASE 1.2.26   n=20 min=1041ms median=1303ms max=1573ms  all=[1130, 1109, 1041, 1480, 1413, 1573, 1489, 1293, 1191, 1396, 1424, 1285, 1251, 1447, 1383, 1246, 1197, 1369, 1108, 1313]  states=['COLD']
START warm NEW 1.2.27    n=20 min=227ms median=333ms max=579ms  all=[309, 579, 516, 329, 337, 352, 236, 346, 416, 370, 466, 341, 325, 310, 310, 318, 380, 227, 275, 322]  states=['HOT']
START warm BASE 1.2.26   n=20 min=225ms median=334ms max=494ms  all=[323, 424, 225, 307, 436, 486, 344, 418, 290, 310, 417, 494, 249, 483, 241, 275, 227, 359, 430, 297]  states=['HOT']
START catalogue first-install new n=2 min=2492ms median=2673ms max=2854ms  all=[2854, 2492]  states=[]
START catalogue index-ready new n=6 min=2577ms median=2674ms max=3751ms  all=[3751, 2727, 2622, 2577, 2635, 2712]  states=[]
START catalogue first-install base n=2 min=2493ms median=2523ms max=2553ms  all=[2493, 2553]  states=[]
START catalogue index-ready base n=6 min=2439ms median=2620ms max=2859ms  all=[2495, 2859, 2561, 2680, 2439, 2684]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5681', '7098', '7510', '7765', '7912', '8076', '8153', '8205', '8274', '8324', '8375', '8435', '8491', '8551', '8966', '9235', '9388', '9538', '9681', '9764', '9821', '9872', '9925', '10088', '10181', '10231', '10309', '10361', '10758', '11013', '11152', '11313', '11459', '11524', '11576', '11627', '11682', '11743', '11793', '11842', '11893', '11946', '12465', '12779', '12918', '13061', '13208', '13274', '13323', '13376', '13430', '13480', '13535', '13585', '13637', '13686']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 34
   skipped-frames events=34 max=61 total=1353
== Davey: 32
   davey events=32 max=1379ms median=837ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4 size 4924210;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.26 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 4007 ms
base-1 INDEX-READY #1 catalogue results after 3298 ms
base-1 INDEX-READY #2 catalogue results after 2908 ms
base-1 INDEX-READY #3 catalogue results after 3270 ms
START cold base-1        n=10 min=1439ms median=1976ms max=4188ms  all=[4188, 2279, 2240, 2022, 2025, 1732, 1599, 1931, 1439, 1718]  states=['COLD']
START warm base-1        n=10 min=196ms median=482ms max=624ms  all=[624, 417, 511, 445, 300, 517, 610, 496, 469, 196]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2709 ms
new-1 INDEX-READY #1 catalogue results after 3078 ms
new-1 INDEX-READY #2 catalogue results after 2736 ms
new-1 INDEX-READY #3 catalogue results after 2930 ms
START cold new-1         n=10 min=1518ms median=1806ms max=2520ms  all=[1941, 1794, 1628, 1723, 1930, 2371, 1710, 2520, 1518, 1818]  states=['COLD']
START warm new-1         n=10 min=256ms median=614ms max=793ms  all=[593, 621, 636, 558, 606, 403, 645, 793, 770, 256]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2616 ms
new-2 INDEX-READY #1 catalogue results after 2881 ms
new-2 INDEX-READY #2 catalogue results after 2587 ms
new-2 INDEX-READY #3 catalogue results after 2888 ms
START cold new-2         n=10 min=1333ms median=1814ms max=2281ms  all=[2067, 1838, 2165, 1774, 1491, 1791, 1333, 1789, 2281, 1991]  states=['COLD']
START warm new-2         n=10 min=218ms median=482ms max=896ms  all=[896, 218, 503, 409, 711, 460, 683, 335, 245, 553]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4 size 4924210;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.26 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2595 ms
base-2 INDEX-READY #1 catalogue results after 2691 ms
base-2 INDEX-READY #2 catalogue results after 2719 ms
base-2 INDEX-READY #3 catalogue results after 2688 ms
START cold base-2        n=10 min=1445ms median=1823ms max=2125ms  all=[1859, 2016, 1665, 2125, 2012, 1787, 1525, 1445, 1954, 1728]  states=['COLD']
START warm base-2        n=10 min=303ms median=490ms max=568ms  all=[546, 568, 566, 365, 509, 470, 303, 522, 341, 469]  states=['HOT']
## AB totals
START cold NEW 1.2.27    n=20 min=1333ms median=1806ms max=2520ms  all=[1941, 1794, 1628, 1723, 1930, 2371, 1710, 2520, 1518, 1818, 2067, 1838, 2165, 1774, 1491, 1791, 1333, 1789, 2281, 1991]  states=['COLD']
START cold BASE 1.2.26   n=20 min=1439ms median=1895ms max=4188ms  all=[4188, 2279, 2240, 2022, 2025, 1732, 1599, 1931, 1439, 1718, 1859, 2016, 1665, 2125, 2012, 1787, 1525, 1445, 1954, 1728]  states=['COLD']
START warm NEW 1.2.27    n=20 min=218ms median=576ms max=896ms  all=[593, 621, 636, 558, 606, 403, 645, 793, 770, 256, 896, 218, 503, 409, 711, 460, 683, 335, 245, 553]  states=['HOT']
START warm BASE 1.2.26   n=20 min=196ms median=483ms max=624ms  all=[624, 417, 511, 445, 300, 517, 610, 496, 469, 196, 546, 568, 566, 365, 509, 470, 303, 522, 341, 469]  states=['HOT']
START catalogue first-install new n=2 min=2616ms median=2662ms max=2709ms  all=[2709, 2616]  states=[]
START catalogue index-ready new n=6 min=2587ms median=2884ms max=3078ms  all=[3078, 2736, 2930, 2881, 2587, 2888]  states=[]
START catalogue first-install base n=2 min=2595ms median=3301ms max=4007ms  all=[4007, 2595]  states=[]
START catalogue index-ready base n=6 min=2688ms median=2814ms max=3298ms  all=[3298, 2908, 3270, 2691, 2719, 2688]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5093', '5935', '6560', '7164', '7738', '7833', '7940', '8050', '8164', '8355', '8406', '8471', '8519', '8565', '8979', '9235', '9372', '9514', '9657', '9716', '9769', '9832', '9879', '9927', '9974', '10160', '10214', '10268', '10653', '10930', '11085', '11223', '11362', '11421', '11472', '11521', '11568', '11617', '11663', '11710', '11800', '11848', '12310', '12639', '12773', '12911', '13098', '13158', '13285', '13350', '13398', '13462', '13520', '13568', '13615', '13663']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 75
   skipped-frames events=75 max=130 total=3498
== Davey: 65
   davey events=65 max=2733ms median=904ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func1

```
part func1; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2653 ms
## F1. Home quick actions; 'Искам да прочета' quick action
QA standard text: Сканирай|=Scan@[167 1542] Читателска карта|=Library card@[415 1563] Искам да прочета|=Want to read@[664 1563] Контакти|=Contacts@[913 1542]; rows=1; quick action 'Събития': absent
QA 'Искам да прочета' → screen: back=yes title=yes; text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" 
## F2. Catalogue '12': other copies on the shelf; notify toggle with undo
SEARCH '12': 108 results; header: text="108 резултата  ·  данни от InvLib към 28.10.2026"
SOLE-OUT book: content-desc="Спомен за здрача" text="Спомен за здрача" text="Веска Пенчева" text="Не е на рафта" text="Наличност към 28.10.2026" text="Искам да прочета" ; AskLibrary=shown Notify=shown
BOOK 'Кажи ми' ON: label now 'text="Ще ти кажем, когато е на рафта"'; snackbar: text="Ще получиш известие, когато книгата е на рафта."
BOOK 'Кажи ми' OFF: snackbar 'text="Няма да получаваш известие за тази книга."' undo=present
BOOK 'Кажи ми' undo → ON again (restored)
COPY-OUT (from '12'): text="Този екземпляр е зает · 5 други на рафта"; title: Времето на горите; AskLibrary=hidden Notify=hidden
COPY-OUT scrolled: copies line: text="7 екземпляра: 5 налични"; shelf marks: 
COPY-OUT search: tried 2 unavailable rows from '12' (found copy-out=1, sole-out=1)
## F3. Want to read: add, 'Кажи ми' on/off, undo, remove with undo
BOOK 399560: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" ; WantToRead [checked=false selected=false bounds=[426,738][911,864]]
WANT add: snackbar shown; chip [checked=true selected=false bounds=[426,738][911,864]]
WANT list opened from the Home quick action
WANT list: Наличност към 28.10.2026|Плъзни книга настрани, за да я махнеш.|12 -те вятъра|12 -те вятъра|Гарфилд, Джудит и др.|налична|Още действия|Спомен за здрача|Спомен за здрача|Веска Пенчева|Не е на рафта|Ще ти кажем, когато е на рафта|
WANT 'Кажи ми' ON: snackbar 'text="Ще получиш известие, когато книгата е на рафта."'; bell icon shown
WANT 'Кажи ми' OFF: snackbar 'text="Няма да получаваш известие за тази книга."'; undo present
WANT 'Кажи ми' undo: bell back (ON restored)
WANT remove: snackbar 'text="„12 -те вятъра“ е махната от списъка"'; undo present
WANT remove undo: book back in list
## F4. /news and /events links
/news (first): News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
Calendar segment chosen: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
/news while on Calendar: News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
/news from Home (News tab left on Calendar): News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
/news cold: News[checked=true selected=false bounds=[42,308][357,434]] Calendar[checked=false selected=false bounds=[357,308][669,434]] subscribe-action=absent
/events: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
/events cold: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
article open: back=yes; content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пирин
/events from an article: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible; back-button none
## F5. Reselecting the News tab on Calendar scrolls to the top
calendar scrolled: common with top: 18/40
RESELECT: News[checked=false selected=false bounds=[42,140][354,266]] Calendar[checked=true selected=false bounds=[354,140][669,266]] subscribe-action=visible; common with top: 38/40
## COMPLETE
## logcat findings
app pids seen: ['6054', '7331', '9621', '9780']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=55 total=98
== Davey: 2
   davey events=2 max=1125ms median=970ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func2

```
part func2; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3608 ms
## F6. Scanner (CAMERA revoked): manual panel, Back key
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
manual panel: panel OPEN; IME mInputShown=true; scanner visible; app in front yes
Back key #1: panel OPEN; IME mInputShown=false; scanner visible; app in front yes
Back key #2: panel closed; IME mInputShown=false; scanner visible; app in front yes
SCAN result: content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Налична" text="инв. № 399560" text="Затвори" text="Отвори книг
SCAN → book: opened
## F8. Contacts links (call, email, map, site, cards)
CONTACTS link 'Обади се|=Call': not on screen
CONTACTS link 'Изпрати имейл|=Send email' → fg com.google.android.gm/com.google.android.gm.welcome.WelcomeTourActivity; text="Welcome to Gmail" text="One app for all of your email" text="SKIP" content-desc="Next" 
CONTACTS link 'Отвори карта|=Open map' → fg com.google.android.apps.maps/com.google.android.maps.MapsActivity; text="Make it your map" text="Sign in for quick access to your favorite places and get better recommendations" text="Sign in" text="SKIP" 
CONTACTS link 'Отвори сайта|=Open website' → fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity; text="Welcome to Chrome" text="Add account to device" text="Use without an account" text="By continuing, you agree to the Terms of Service. To help im
CONTACTS link 'chitalishte_yavorec@abv.bg' → fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" 
CONTACTS link 'facebook.com' → fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity; text="Welcome to Chrome" text="Add account to device" text="Use without an account" text="By continuing, you agree to the Terms of Service. To help im
CONTACTS link 'Данни от chyavorec.org|=Data from chyavorec.org' → fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity; text="Welcome to Chrome" text="Add account to device" text="Use without an account" text="By continuing, you agree to the Terms of Service. To help im
CONTACTS clickables past the right edge: 0
## F7. Scanner manual panel with the system back GESTURE (gesture navigation; predictive back)
gesture nav: 
navigation_mode=2 (2 = gestures); predictive-back animations need Android 14+, this is API 33
panel before: panel OPEN; IME mInputShown=true; scanner visible; app in front yes
back gesture #1: panel OPEN; IME mInputShown=false; scanner visible; app in front yes
back gesture #2: panel closed; IME mInputShown=false; scanner visible; app in front yes
panel reopened (IME hidden): panel OPEN; IME mInputShown=true; scanner visible; app in front yes
back swipe (input swipe): panel OPEN; IME mInputShown=false; scanner visible; app in front yes
back swipe #2 (from the scanner): fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и н
3-button nav: 
## COMPLETE
## logcat findings
app pids seen: ['4966']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=77 total=182
== Davey: 8
   davey events=8 max=2241ms median=952ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank-1

```
part jank-1; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## J-new. Install new, warm-up, 16 books in Want to read
installed new: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 2948 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-new. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX new-catalog  frames=  245  janky=225 (91.84%)  p50=117ms p90=150ms p95=200ms p99=250ms  slowUI=110 slowDraw=225 highInput=30
new-catalog: 30 swipes, 'Покажи още' pages: 5, mis-taps recovered: 0
new want list: 1 rows visible
jank new-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="Дъхът на пролетта" text="Дъхът на пролетта" text="Пламен Иванов" text="налична" content-desc="Ощ
GFX new-want     frames=  114  janky=112 (98.25%)  p50=150ms p90=200ms p95=250ms p99=300ms  slowUI=60 slowDraw=112 highInput=3
jank new-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX new-news     frames=   93  janky=74 (79.57%)  p50=105ms p90=150ms p95=150ms p99=200ms  slowUI=40 slowDraw=74 highInput=36
jank new-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX new-calendar frames=  150  janky=123 (82.00%)  p50=89ms p90=125ms p95=150ms p99=150ms  slowUI=76 slowDraw=123 highInput=54
new gallery albums shown after 2466 ms: 6 on screen
GFX new-gallery  frames=  153  janky=126 (82.35%)  p50=117ms p90=150ms p95=150ms p99=150ms  slowUI=73 slowDraw=125 highInput=52
new album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX new-album    frames=  168  janky=134 (79.76%)  p50=93ms p90=133ms p95=150ms p99=150ms  slowUI=51 slowDraw=134 highInput=66
new viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX new-viewer   frames=  312  janky=247 (79.17%)  p50=77ms p90=109ms p95=117ms p99=150ms  slowUI=76 slowDraw=246 highInput=127
new viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
jank new-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="29 октомври 2026 · Св. прпмчца Анастасия
GFX new-home     frames=   46  janky=45 (97.83%)  p50=150ms p90=350ms p95=400ms p99=400ms  slowUI=27 slowDraw=45 highInput=2
MEM new-jank-end (raw) PSS=134610KB  Java=13796KB  Native=64100KB  Graphics=0KB  Views=10 Activities=1
MEM new-jank-end (trimmed) PSS=131080KB  Java=11644KB  Native=56672KB  Graphics=0KB  Views=10 Activities=1
## J-base. Install base, warm-up, 16 books in Want to read
installed base: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4 size 4924210;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.26 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 2536 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-base. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX base-catalog frames=  253  janky=238 (94.07%)  p50=133ms p90=150ms p95=200ms p99=250ms  slowUI=131 slowDraw=238 highInput=22
base-catalog: 30 swipes, 'Покажи още' pages: 5, mis-taps recovered: 0
base want list: 1 rows visible
jank base-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="Дъхът на пролетта" text="Дъхът на пролетта" text="Пламен Иванов" text="Налична" content-desc="Ощ
GFX base-want    frames=  107  janky=101 (94.39%)  p50=150ms p90=200ms p95=200ms p99=250ms  slowUI=57 slowDraw=101 highInput=12
jank base-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX base-news    frames=   85  janky=67 (78.82%)  p50=93ms p90=133ms p95=150ms p99=150ms  slowUI=32 slowDraw=66 highInput=35
jank base-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX base-calendar frames=  151  janky=122 (80.79%)  p50=93ms p90=150ms p95=150ms p99=150ms  slowUI=77 slowDraw=122 highInput=56
base gallery albums shown after 2290 ms: 6 on screen
GFX base-gallery frames=  165  janky=134 (81.21%)  p50=101ms p90=150ms p95=150ms p99=150ms  slowUI=64 slowDraw=134 highInput=62
base album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX base-album   frames=  160  janky=129 (80.62%)  p50=101ms p90=150ms p95=150ms p99=150ms  slowUI=67 slowDraw=128 highInput=59
base viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX base-viewer  frames=  310  janky=251 (80.97%)  p50=81ms p90=109ms p95=121ms p99=133ms  slowUI=85 slowDraw=251 highInput=112
base viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
jank base-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="29 октомври 2026 · Св. прпмчца Анастасия
GFX base-home    frames=  110  janky=98 (89.09%)  p50=150ms p90=250ms p95=250ms p99=400ms  slowUI=57 slowDraw=98 highInput=21
MEM base-jank-end (raw) PSS=138493KB  Java=20840KB  Native=80204KB  Graphics=0KB  Views=10 Activities=1
MEM base-jank-end (trimmed) PSS=133309KB  Java=14860KB  Native=75980KB  Graphics=0KB  Views=10 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5879', '8546', '11537', '13182']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=73 total=382
== Davey: 12
   davey events=12 max=1949ms median=1226ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank-2

```
part jank-2; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## J-base. Install base, warm-up, 16 books in Want to read
installed base: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4 size 4924210;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.26 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 2935 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-base. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX base-catalog frames=  168  janky=166 (98.81%)  p50=200ms p90=350ms p95=400ms p99=450ms  slowUI=98 slowDraw=166 highInput=2
base-catalog: 30 swipes, 'Покажи още' pages: 1, mis-taps recovered: 0
base want list: 1 rows visible
jank base-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="Дъхът на пролетта" text="Дъхът на пролетта" text="Пламен Иванов" text="Налична" content-desc="Ощ
GFX base-want    frames=   97  janky=95 (97.94%)  p50=200ms p90=300ms p95=350ms p99=400ms  slowUI=49 slowDraw=95 highInput=3
jank base-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX base-news    frames=   77  janky=61 (79.22%)  p50=150ms p90=250ms p95=300ms p99=450ms  slowUI=33 slowDraw=61 highInput=28
jank base-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX base-calendar frames=  109  janky=85 (77.98%)  p50=125ms p90=200ms p95=250ms p99=300ms  slowUI=45 slowDraw=85 highInput=35
base gallery albums shown after 3003 ms: 6 on screen
GFX base-gallery frames=  110  janky=103 (93.64%)  p50=150ms p90=200ms p95=250ms p99=250ms  slowUI=56 slowDraw=103 highInput=14
base album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX base-album   frames=   35  janky=29 (82.86%)  p50=121ms p90=200ms p95=250ms p99=350ms  slowUI=13 slowDraw=29 highInput=11
base viewer: no photo to open
jank base-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="29 октомври 2026 · Св. прпмчца Анастасия
GFX base-home    frames=   69  janky=66 (95.65%)  p50=200ms p90=350ms p95=400ms p99=550ms  slowUI=36 slowDraw=66 highInput=5
MEM base-jank-end (raw) PSS=117524KB  Java=22028KB  Native=58276KB  Graphics=0KB  Views=10 Activities=1
MEM base-jank-end (trimmed) PSS=102587KB  Java=15356KB  Native=45212KB  Graphics=0KB  Views=10 Activities=1
## J-new. Install new, warm-up, 16 books in Want to read
installed new: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 2664 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-new. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX new-catalog  frames=  176  janky=175 (99.43%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=99 slowDraw=175 highInput=2
new-catalog: 30 swipes, 'Покажи още' pages: 2, mis-taps recovered: 0
new want list: 1 rows visible
jank new-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="Дъхът на пролетта" text="Дъхът на пролетта" text="Пламен Иванов" text="налична" content-desc="Ощ
GFX new-want     frames=  101  janky=91 (90.10%)  p50=150ms p90=300ms p95=350ms p99=450ms  slowUI=57 slowDraw=91 highInput=19
jank new-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX new-news     frames=  112  janky=102 (91.07%)  p50=150ms p90=200ms p95=250ms p99=300ms  slowUI=56 slowDraw=102 highInput=16
jank new-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX new-calendar frames=  119  janky=93 (78.15%)  p50=150ms p90=200ms p95=250ms p99=250ms  slowUI=56 slowDraw=93 highInput=45
new gallery albums shown after 2828 ms: 6 on screen
GFX new-gallery  frames=  115  janky=104 (90.43%)  p50=150ms p90=200ms p95=250ms p99=300ms  slowUI=41 slowDraw=104 highInput=20
new album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX new-album    frames=   46  janky=36 (78.26%)  p50=150ms p90=200ms p95=250ms p99=250ms  slowUI=13 slowDraw=36 highInput=15
new viewer: no photo to open
jank new-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="29 октомври 2026 · Св. прпмчца Анастасия
GFX new-home     frames=   73  janky=70 (95.89%)  p50=200ms p90=300ms p95=350ms p99=450ms  slowUI=37 slowDraw=70 highInput=5
MEM new-jank-end (raw) PSS=125271KB  Java=19444KB  Native=71276KB  Graphics=0KB  Views=10 Activities=1
MEM new-jank-end (trimmed) PSS=125008KB  Java=14564KB  Native=71004KB  Graphics=0KB  Views=10 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4701', '8497', '11303', '13097']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 13
   skipped-frames events=13 max=119 total=655
== Davey: 22
   davey events=22 max=2728ms median=936ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look1

```
part look1; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3242 ms
## L bg-light
matrix bg-light: 1080x2400 density 420, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix bg-light: news title OK; segments News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
matrix bg-light scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи но
matrix bg-light tab labels: home catalog my news more 
## L bg-dark
matrix bg-dark: 1080x2400 density 420, font_scale 1.0, night Night mode: yes, locale [bg-BG]
matrix bg-dark: news title OK; segments News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
matrix bg-dark scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи но
matrix bg-dark tab labels: home catalog my news more 
## L en-dark
matrix en-dark: 1080x2400 density 420, font_scale 1.0, night Night mode: yes, locale [en-US]
matrix en-dark: news title MISSING; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=absent
matrix en-dark scanner: text="Camera access needed" text="The camera is only used to read the barcode on your phone. Nothing is recorded or sent." text="Allow camera" text="Open settings" text="Enter the number manually" con
matrix en-dark tab labels: home catalog my news more 
## L en-light
matrix en-light: 1080x2400 density 420, font_scale 1.0, night Night mode: no, locale [en-US]
matrix en-light: news title MISSING; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=absent
matrix en-light scanner: text="Camera access needed" text="The camera is only used to read the barcode on your phone. Nothing is recorded or sent." text="Allow camera" text="Open settings" text="Enter the number manually" con
matrix en-light tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['4547', '6498', '8929', '10459', '11782']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=57 total=270
== Davey: 21
   davey events=21 max=1921ms median=915ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look2

```
part look2; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3079 ms
TEXT SIZE now: text="Много голям" 
## L font130-xlarge
matrix font130-xlarge: 1080x2400 density 420, font_scale 1.3, night Night mode: no, locale [bg-BG]
matrix font130-xlarge: news title OK; segments News[checked=false selected=false bounds=[42,307][538,438]] Calendar[checked=true selected=false bounds=[538,307][1038,438]] subscribe-action=visible
matrix font130-xlarge scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи но
matrix font130-xlarge tab labels: home catalog my news more 
TEXT SIZE now: text="Стандартен" 
## L landscape
landscape check: mRotation=ROTATION_90} cur=2400x1080
matrix landscape: 2400x1080 density 420, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix landscape: news title OK; segments News[checked=false selected=false bounds=[508,243][820,369]] Calendar[checked=true selected=false bounds=[820,243][1135,369]] subscribe-action=visible
matrix landscape scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" content-desc="Н
matrix landscape tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['5241', '7733', '9570']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 1
   davey events=1 max=1099ms median=1099ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look3

```
part look3; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2775 ms
## L small-320dp
matrix small-320dp: 640x1280 density 320, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix small-320dp: news title OK; segments News[checked=false selected=false bounds=[32,204][231,300]] Calendar[checked=true selected=false bounds=[231,204][432,300]] subscribe-action=visible
matrix small-320dp scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи но
matrix small-320dp tab labels: home catalog my news more 
## L tablet-800dp
matrix tablet-800dp: 1600x2560 density 320, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix tablet-800dp: news title OK; segments News[checked=false selected=false bounds=[192,273][431,369]] Calendar[checked=true selected=false bounds=[431,273][672,369]] subscribe-action=visible
matrix tablet-800dp scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи но
matrix tablet-800dp tab labels: home catalog my news more 
## L tablet-land
landscape check: mRotation=ROTATION_90} cur=2560x1600
matrix tablet-land: 2560x1600 density 320, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix tablet-land: news title OK; segments News[checked=false selected=false bounds=[466,184][705,280]] Calendar[checked=true selected=false bounds=[705,184][946,280]] subscribe-action=visible
matrix tablet-land scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи но
matrix tablet-land tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['5319', '7369', '9034', '10507']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=42 total=83
== Davey: 5
   davey events=5 max=1003ms median=820ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## media

```
part media; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3060 ms
## G1. Gallery: albums
MEM gal-before (raw) PSS=53640KB  Java=6884KB  Native=26116KB  Graphics=0KB  Views=7 Activities=1
MEM gal-before (trimmed) PSS=55740KB  Java=7988KB  Native=25564KB  Graphics=0KB  Views=7 Activities=1
GALLERY albums after 1994 ms (tap → '… снимки')
GALLERY albums: 29 снимки|Фотодокументална изложба|4 снимки|Фолклорна група „Явор“|6 снимки|Клуб за народни танци|7 снимки|Ансамбъл „Яворски хлапета“|5 снимки|Село Яворец|7 снимки|Природата край Яворец|
ALBUM 1: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" ; photos on screen 18 after 2033 ms
## G2. Photo viewer: 2× (double tap), beyond 2× (pinch), 20 photos
VIEWER: text="1 / 29  ·  Сградата на читалището в Яворец"; fg org.chyavorec.app/org.chyavorec.app.MainActivity
MEM gal-viewer (raw) PSS=93283KB  Java=12300KB  Native=37320KB  Graphics=0KB  Views=10 Activities=1
MEM gal-viewer (trimmed) PSS=92423KB  Java=7544KB  Native=36412KB  Graphics=0KB  Views=10 Activities=1
touch device: /dev/input/event12 x-max=32767 y-max=32767 pressure=11
VIEWER after double tap: text="1 / 29  ·  Сградата на читалището в Яворец"
VIEWER after 2 pinches (from 2× → expected about 2× · 3 · 2.7, capped at 5×): text="1 / 29  ·  Сградата на читалището в Яворец"
VIEWER swipe while zoomed: text="2 / 29  ·  Фолклорен състав при читалището" (same page = the swipe panned the photo)
MEM gal-zoomed (raw) PSS=96231KB  Java=8268KB  Native=40620KB  Graphics=0KB  Views=10 Activities=1
MEM gal-zoomed (trimmed) PSS=91364KB  Java=7532KB  Native=36504KB  Graphics=0KB  Views=10 Activities=1
VIEWER after reset double tap: text="2 / 29  ·  Фолклорен състав при читалището"
GFX viewer-20    frames=  221  janky=211 (95.48%)  p50=150ms p90=200ms p95=200ms p99=250ms  slowUI=88 slowDraw=211 highInput=11
VIEWER after 20 swipes: text="22 / 29  ·  Църквата"
MEM gal-after-20 (raw) PSS=67416KB  Java=11864KB  Native=16376KB  Graphics=0KB  Views=10 Activities=1
MEM gal-after-20 (trimmed) PSS=63190KB  Java=7648KB  Native=16372KB  Graphics=0KB  Views=10 Activities=1
MEM gal-back-home (raw) PSS=98187KB  Java=11568KB  Native=46764KB  Graphics=0KB  Views=10 Activities=1
MEM gal-back-home (trimmed) PSS=91258KB  Java=9260KB  Native=42152KB  Graphics=0KB  Views=10 Activities=1
## C. Covers in the catalogue (Open Library)
CACHE before-covers: cache/catalog-index.db.lck=4KB cache/chitalishte.db.lck=4KB cache/data=44KB cache/http=744KB cache/image_cache=2636KB files=40KB 
CACHE after-catalogue: cache/catalog-index.db.lck=4KB cache/chitalishte.db.lck=4KB cache/data=44KB cache/http=744KB cache/image_cache=2676KB files=40KB 
book: text="12" content-desc="Изчисти" text="Всичко" text="Заглавие" text="Автор" 
CACHE after-books: cache/catalog-index.db.lck=4KB cache/chitalishte.db.lck=4KB cache/data=44KB cache/http=744KB cache/image_cache=2760KB files=40KB 
Open Library traffic in logcat: 0
## D. Documents: PDF download (no PDF app?)
PDF handlers: 1 activities found:   Activity #0:     priority=0 preferredOrder=0 match=0x608000 specificIndex=-1 isDefault=true     com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity 
browsers (https): 1 activities found:   Activity #0:     priority=0 preferredOrder=0 match=0x208000 specificIndex=-1 isDefault=true     com.android.chrome/com.google.android.apps.chrome.IntentDispatcher 
DOCUMENTS: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, които могат да повлияят на тяхната безпристрастност при вземане на решения. Тя е аналог на деклараци; PDF buttons: 1
PDF online +1559 ms: fg   mCurrentFocus=null
PDF online +3381 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF online +6223 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF online +11561 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; content-desc="page 1" content-desc="page 2" content-desc="page 3" text="3bf689707cc697d836d6e3bf.pdf" content-desc="Find" content-desc="Add to Drive" 
PDF online toasts in logcat: 
PDF online back in app: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" 
offline: airplane=1
PDF offline +1130 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF offline +2471 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF offline +5305 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF offline +10643 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; content-desc="page 1" content-desc="page 2" content-desc="page 3" text="3bf689707cc697d836d6e3bf.pdf" content-desc="Find" content-desc="Add to Drive" 
PDF offline toasts in logcat: 
PDF offline back in app: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" 
PUBLICATIONS: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
PDF publication +662 ms: fg org.chyavorec.app/org.chyavorec.app.MainActivity
PDF publication +2433 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF publication +5236 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF publication +10532 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; content-desc="page 1" text="a1fe4e661cc48879ad89589f.pdf" content-desc="Find" content-desc="Add to Drive" content-desc="More options" 
PDF publication toasts in logcat: 
PDF publication back in app: text="Документи" text="Исторически публикации" text="Статия 01 · Спомени" 
## COMPLETE
## logcat findings
app pids seen: ['5380', '6803']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=51 total=122
== Davey: 5
   davey events=5 max=1090ms median=854ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem-base

```
part mem-base; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15987 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
installed base: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4 size 4924210;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.26 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3475 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=55841KB  Java=8200KB  Native=25692KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=54274KB  Java=6952KB  Native=15684KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
cycles done: 10, gallery viewer reached in 10
MEM c10 (raw)        PSS=89987KB  Java=18152KB  Native=29292KB  Graphics=0KB  Views=13 Activities=1
MEM c10 (trimmed)    PSS=83403KB  Java=9460KB  Native=26220KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20
MEM c20 (raw)        PSS=110713KB  Java=18820KB  Native=41608KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (trimmed)    PSS=96996KB  Java=9332KB  Native=38396KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30
MEM c30 (raw)        PSS=117924KB  Java=17996KB  Native=49764KB  Graphics=0KB  Views=13 Activities=1
MEM c30 (trimmed)    PSS=106444KB  Java=9348KB  Native=46968KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 7052; gallery viewer reached in 30/30 cycles
## COMPLETE
## logcat findings
app pids seen: ['5423', '7052']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=84 total=268
== Davey: 24
   davey events=24 max=1884ms median=844ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem

```
part mem; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4834 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=51365KB  Java=6588KB  Native=16868KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=55176KB  Java=6696KB  Native=8264KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
cycles done: 10, gallery viewer reached in 10
MEM c10 (raw)        PSS=110553KB  Java=16216KB  Native=25164KB  Graphics=0KB  Views=13 Activities=1
MEM c10 (trimmed)    PSS=106002KB  Java=8692KB  Native=22680KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20
MEM c20 (raw)        PSS=103360KB  Java=18040KB  Native=29828KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (trimmed)    PSS=90879KB  Java=8704KB  Native=27024KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30
MEM c30 (raw)        PSS=112042KB  Java=17556KB  Native=38052KB  Graphics=0KB  Views=13 Activities=1
MEM c30 (trimmed)    PSS=99918KB  Java=8728KB  Native=34792KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 6353; gallery viewer reached in 30/30 cycles
## COMPLETE
## logcat findings
app pids seen: ['5102', '6353']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=64 total=312
== Davey: 30
   davey events=30 max=1899ms median=776ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey

```
part monkey; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3650 ms
## K. Monkey 5000 events (seed 4321, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
statusbar disable: 
monkey start pid=6539
MONKEY exit=0 duration=229 s; Events injected: 5000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6539'; airplane=0; camera:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MEM after-monkey (raw) PSS=94320KB  Java=21468KB  Native=23600KB  Graphics=0KB  Views=17 Activities=1
MEM after-monkey (trimmed) PSS=86161KB  Java=11608KB  Native=22088KB  Graphics=0KB  Views=15 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5209', '6539']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 23
   skipped-frames events=23 max=76 total=968
== Davey: 74
   davey events=74 max=1646ms median=814ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## O1. Online warm-up (fills the caches)
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3267 ms
online-warm home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="29 октомври 2026 · Св. прпмчца Анастасия Римлянка. Преп. Серапион" content-desc="Последни новини" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска
online-warm catalogue: results (2446, 2514 ms)
online-warm catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" content-desc="12 -те вятъра" te
online-warm book: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" 
online-warm My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
online-warm Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
online-warm news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
online-warm article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
online-warm calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
online-warm More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
online-warm gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
online-warm album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
online-warm contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
online-warm documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O2. Offline cold start with caches: all tabs
offline: airplane=1
offline-cached home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="29 октомври 2026 · Св. прпмчца Анастасия Римлянка. Преп. Серапион" content-desc="Последни новини" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска
offline-cached catalogue: results (2446, 2543 ms)
offline-cached catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" content-desc="12 -те вятъра" te
offline-cached book: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" 
offline-cached My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
offline-cached Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" text="Няма интернет връз
offline-cached news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
offline-cached article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
offline-cached calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
offline-cached More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
offline-cached gallery: text="Няма интернет връзка. Показваме последно наличните данни." text="Последна синхронизация: Преди 2 минути" text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“"
offline-cached album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
offline-cached contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
offline-cached documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O3. Reconnect on Contacts (no touch), then the other screens
contacts +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Сек
contacts +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Сек
recovered home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="29 октомври 2026 · Св. прпмчца Анастасия Римлянка. Преп. Серапион" content-desc="Последни новини" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска
recovered catalogue: results (2446, 2593 ms)
recovered catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" content-desc="12 -те вятъра" te
recovered book: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" 
recovered My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
recovered Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
recovered news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
recovered article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
recovered calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
recovered More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
recovered contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
recovered documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O4. Empty cache, offline, guest: all tabs (MyHub)
offline: airplane=1
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
MY offline guest (empty cache): add-card shown; note shown; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Добави читателска карта" text="Не можем да проверим дали онлайн входът е включен." text="Опитай отново" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text
offline-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Сканирай" text="Читателска карта" text="Искам да прочета" t
offline-empty catalogue: NO results
offline-empty catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Каталог" content-desc="Полица" content-desc="Подреждане" content-desc="Ф
offline-empty book: text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Няма интернет връзка" 
offline-empty My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Добави читателска карта" text="Не можем да проверим дали онлайн входът е включен." text="Опитай отново" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text
offline-empty Want to read: text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" text="Няма интернет връзка" 
offline-empty news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Новини и събития" content-desc="Търсене" text="Няма интернет връзка" text="Начало" text="Каталог" text="Моето" 
offline-empty calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Новини и събития" content-desc="Търсене" text="Няма интернет връзка" text="Начало" tex
offline-empty More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
offline-empty gallery: text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Галерия" text="Няма интернет връзка" 
offline-empty contacts: text="Контактите от сайта не се заредиха — показваме основните данни, вградени в приложението. Може да не са актуални." text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, ПК 5334" text="Имейл"
offline-empty documents: text="Документи" text="Исторически публикации" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Документи и архив" text="Няма интернет връзка" 
## O5. Reconnect with the empty cache: Gallery (no touch), then all tabs
GALLERY (empty cache) reconnect: albums shown after 8747 ms (-1 = not within 60 s); text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" 
recovered-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" content-desc="Последни новини" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="16 октомври 2026" text="Ск
recovered-empty catalogue: results (2446, 2822 ms)
recovered-empty catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" content-desc="12 -те вятъра" te
recovered-empty book: content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" 
recovered-empty My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
recovered-empty Want to read: text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" 
recovered-empty news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
recovered-empty article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
recovered-empty calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
recovered-empty More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered-empty gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered-empty album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
recovered-empty contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
recovered-empty documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O6. My offline as guest again (after the online start)
offline: airplane=1
MY offline guest #2: add-card MISSING; note absent; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
## COMPLETE
## logcat findings
app pids seen: ['5616', '9076', '12271', '15967']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=76 total=257
== Davey: 6
   davey events=6 max=1280ms median=898ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## rot

```
part rot; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2909 ms
## R1. Rotation (6 toggles each) on Home, Catalogue '12', a book, Want to read, Calendar
ROT home before: tab bar yes; search field yes
ROT home landscape: tab bar yes; search field yes
ROTATION home: pid 7977 -> 7977 (same); after: tab bar yes; search field yes
ROT catalog before: query '12' kept; results 108
ROT catalog landscape: query '12' kept; results 108
ROTATION catalog: pid 7977 -> 7977 (same); after: query '12' kept; results 108
ROT book before: book title kept; back button yes
ROT book landscape: book title kept; back button yes
ROTATION book: pid 7977 -> 7977 (same); after: book title kept; back button yes
ROT want before: list title yes; book listed
ROT want landscape: list title yes; book listed
ROTATION want: pid 7977 -> 7977 (same); after: list title yes; book listed
ROT calendar before: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
ROT calendar landscape: News[checked=false selected=false bounds=[508,243][820,369]] Calendar[checked=true selected=false bounds=[820,243][1135,369]] subscribe-action=visible
ROTATION calendar: pid 7977 -> 7977 (same); after: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
GFX rotation     frames=  145  janky=144 (99.31%)  p50=200ms p90=500ms p95=600ms p99=700ms  slowUI=90 slowDraw=144 highInput=1
## R2. Process death (HOME → am kill → launcher) on the same screens
PD home before: tab bar yes; search field yes
PROCDEATH home: killed yes; LaunchState: COLD; restored: tab bar yes; search field yes
PD catalog before: query '12' kept; results 108
PROCDEATH catalog: killed yes; LaunchState: COLD; restored: query '12' kept; results 108
PD book before: book title kept; back button yes
PROCDEATH book: killed yes; LaunchState: COLD; restored: book title kept; back button yes
PD want before: list title yes; book listed
PROCDEATH want: killed yes; LaunchState: COLD; restored: list title yes; book listed
PD calendar before: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
PROCDEATH calendar: killed yes; LaunchState: COLD; restored: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
## R3. Process death, back from Recents (Calendar, book)
PD calendar before: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
PROCDEATH calendar (recents): killed yes; LaunchState: COLD; restored: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
PD book before: book title kept; back button yes
PROCDEATH book (recents): killed yes; LaunchState: COLD; restored: book title kept; back button yes
## COMPLETE
## logcat findings
app pids seen: ['5442', '7977', '10235', '10511', '10889', '11228', '11437', '11677', '11860']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=38 total=138
== Davey: 8
   davey events=8 max=1016ms median=756ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### abstart-1/shots

- [abstart-1/shots/ab-base-1.png](abstart-1/shots/ab-base-1.png)
- [abstart-1/shots/ab-base-2.png](abstart-1/shots/ab-base-2.png)
- [abstart-1/shots/ab-new-1.png](abstart-1/shots/ab-new-1.png)
- [abstart-1/shots/ab-new-2.png](abstart-1/shots/ab-new-2.png)

### abstart-2/shots

- [abstart-2/shots/ab-base-1.png](abstart-2/shots/ab-base-1.png)
- [abstart-2/shots/ab-base-2.png](abstart-2/shots/ab-base-2.png)
- [abstart-2/shots/ab-new-1.png](abstart-2/shots/ab-new-1.png)
- [abstart-2/shots/ab-new-2.png](abstart-2/shots/ab-new-2.png)

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
- [func1/shots/func/19-news-link-after-calendar.png](func1/shots/func/19-news-link-after-calendar.png)
- [func1/shots/func/21-events-link.png](func1/shots/func/21-events-link.png)
- [func1/shots/func/22-article.png](func1/shots/func/22-article.png)
- [func1/shots/func/23-events-from-article.png](func1/shots/func/23-events-from-article.png)
- [func1/shots/func/24-calendar-top.png](func1/shots/func/24-calendar-top.png)
- [func1/shots/func/25-calendar-scrolled.png](func1/shots/func/25-calendar-scrolled.png)
- [func1/shots/func/26-calendar-after-reselect.png](func1/shots/func/26-calendar-after-reselect.png)

### func2/shots/func2

- [func2/shots/func2/01-manual-panel.png](func2/shots/func2/01-manual-panel.png)
- [func2/shots/func2/02-after-back.png](func2/shots/func2/02-after-back.png)
- [func2/shots/func2/03-manual-result.png](func2/shots/func2/03-manual-result.png)
- [func2/shots/func2/10-manual-panel-gesture.png](func2/shots/func2/10-manual-panel-gesture.png)
- [func2/shots/func2/11-gesture-1-midway.png](func2/shots/func2/11-gesture-1-midway.png)
- [func2/shots/func2/11-gesture-2-midway.png](func2/shots/func2/11-gesture-2-midway.png)
- [func2/shots/func2/12-gesture-1-after.png](func2/shots/func2/12-gesture-1-after.png)
- [func2/shots/func2/12-gesture-2-after.png](func2/shots/func2/12-gesture-2-after.png)
- [func2/shots/func2/13-swipe-after.png](func2/shots/func2/13-swipe-after.png)
- [func2/shots/func2/contacts-link-1.png](func2/shots/func2/contacts-link-1.png)
- [func2/shots/func2/contacts-link-2.png](func2/shots/func2/contacts-link-2.png)
- [func2/shots/func2/contacts-link-3.png](func2/shots/func2/contacts-link-3.png)
- [func2/shots/func2/contacts-link-4.png](func2/shots/func2/contacts-link-4.png)
- [func2/shots/func2/contacts-link-5.png](func2/shots/func2/contacts-link-5.png)
- [func2/shots/func2/contacts-link-6.png](func2/shots/func2/contacts-link-6.png)

### jank-1/shots/jank

- [jank-1/shots/jank/base-album.png](jank-1/shots/jank/base-album.png)
- [jank-1/shots/jank/base-calendar.png](jank-1/shots/jank/base-calendar.png)
- [jank-1/shots/jank/base-catalog-end.png](jank-1/shots/jank/base-catalog-end.png)
- [jank-1/shots/jank/base-gallery.png](jank-1/shots/jank/base-gallery.png)
- [jank-1/shots/jank/base-home.png](jank-1/shots/jank/base-home.png)
- [jank-1/shots/jank/base-news.png](jank-1/shots/jank/base-news.png)
- [jank-1/shots/jank/base-viewer-end.png](jank-1/shots/jank/base-viewer-end.png)
- [jank-1/shots/jank/base-want.png](jank-1/shots/jank/base-want.png)
- [jank-1/shots/jank/new-album.png](jank-1/shots/jank/new-album.png)
- [jank-1/shots/jank/new-calendar.png](jank-1/shots/jank/new-calendar.png)
- [jank-1/shots/jank/new-catalog-end.png](jank-1/shots/jank/new-catalog-end.png)
- [jank-1/shots/jank/new-gallery.png](jank-1/shots/jank/new-gallery.png)
- [jank-1/shots/jank/new-home.png](jank-1/shots/jank/new-home.png)
- [jank-1/shots/jank/new-news.png](jank-1/shots/jank/new-news.png)
- [jank-1/shots/jank/new-viewer-end.png](jank-1/shots/jank/new-viewer-end.png)
- [jank-1/shots/jank/new-want.png](jank-1/shots/jank/new-want.png)

### jank-2/shots/debug

- [jank-2/shots/debug/1-viewer.png](jank-2/shots/debug/1-viewer.png)
- [jank-2/shots/debug/2-viewer.png](jank-2/shots/debug/2-viewer.png)

### jank-2/shots/jank

- [jank-2/shots/jank/base-album.png](jank-2/shots/jank/base-album.png)
- [jank-2/shots/jank/base-calendar.png](jank-2/shots/jank/base-calendar.png)
- [jank-2/shots/jank/base-catalog-end.png](jank-2/shots/jank/base-catalog-end.png)
- [jank-2/shots/jank/base-gallery.png](jank-2/shots/jank/base-gallery.png)
- [jank-2/shots/jank/base-home.png](jank-2/shots/jank/base-home.png)
- [jank-2/shots/jank/base-news.png](jank-2/shots/jank/base-news.png)
- [jank-2/shots/jank/base-want.png](jank-2/shots/jank/base-want.png)
- [jank-2/shots/jank/new-album.png](jank-2/shots/jank/new-album.png)
- [jank-2/shots/jank/new-calendar.png](jank-2/shots/jank/new-calendar.png)
- [jank-2/shots/jank/new-catalog-end.png](jank-2/shots/jank/new-catalog-end.png)
- [jank-2/shots/jank/new-gallery.png](jank-2/shots/jank/new-gallery.png)
- [jank-2/shots/jank/new-home.png](jank-2/shots/jank/new-home.png)
- [jank-2/shots/jank/new-news.png](jank-2/shots/jank/new-news.png)
- [jank-2/shots/jank/new-want.png](jank-2/shots/jank/new-want.png)

### look1/shots/bg-dark

- [look1/shots/bg-dark/01-home.png](look1/shots/bg-dark/01-home.png)
- [look1/shots/bg-dark/01b-home-scrolled.png](look1/shots/bg-dark/01b-home-scrolled.png)
- [look1/shots/bg-dark/02-catalog.png](look1/shots/bg-dark/02-catalog.png)
- [look1/shots/bg-dark/03-book.png](look1/shots/bg-dark/03-book.png)
- [look1/shots/bg-dark/04-news.png](look1/shots/bg-dark/04-news.png)
- [look1/shots/bg-dark/05-article.png](look1/shots/bg-dark/05-article.png)
- [look1/shots/bg-dark/06-calendar.png](look1/shots/bg-dark/06-calendar.png)
- [look1/shots/bg-dark/07-my.png](look1/shots/bg-dark/07-my.png)
- [look1/shots/bg-dark/08-want.png](look1/shots/bg-dark/08-want.png)
- [look1/shots/bg-dark/09-more.png](look1/shots/bg-dark/09-more.png)
- [look1/shots/bg-dark/10-settings.png](look1/shots/bg-dark/10-settings.png)
- [look1/shots/bg-dark/11-gallery.png](look1/shots/bg-dark/11-gallery.png)
- [look1/shots/bg-dark/12-album.png](look1/shots/bg-dark/12-album.png)
- [look1/shots/bg-dark/13-viewer.png](look1/shots/bg-dark/13-viewer.png)
- [look1/shots/bg-dark/14-contacts.png](look1/shots/bg-dark/14-contacts.png)
- [look1/shots/bg-dark/15-documents.png](look1/shots/bg-dark/15-documents.png)
- [look1/shots/bg-dark/16-scanner.png](look1/shots/bg-dark/16-scanner.png)

### look1/shots/bg-light

- [look1/shots/bg-light/01-home.png](look1/shots/bg-light/01-home.png)
- [look1/shots/bg-light/01b-home-scrolled.png](look1/shots/bg-light/01b-home-scrolled.png)
- [look1/shots/bg-light/02-catalog.png](look1/shots/bg-light/02-catalog.png)
- [look1/shots/bg-light/03-book.png](look1/shots/bg-light/03-book.png)
- [look1/shots/bg-light/04-news.png](look1/shots/bg-light/04-news.png)
- [look1/shots/bg-light/05-article.png](look1/shots/bg-light/05-article.png)
- [look1/shots/bg-light/06-calendar.png](look1/shots/bg-light/06-calendar.png)
- [look1/shots/bg-light/07-my.png](look1/shots/bg-light/07-my.png)
- [look1/shots/bg-light/08-want.png](look1/shots/bg-light/08-want.png)
- [look1/shots/bg-light/09-more.png](look1/shots/bg-light/09-more.png)
- [look1/shots/bg-light/10-settings.png](look1/shots/bg-light/10-settings.png)
- [look1/shots/bg-light/11-gallery.png](look1/shots/bg-light/11-gallery.png)
- [look1/shots/bg-light/12-album.png](look1/shots/bg-light/12-album.png)
- [look1/shots/bg-light/13-viewer.png](look1/shots/bg-light/13-viewer.png)
- [look1/shots/bg-light/14-contacts.png](look1/shots/bg-light/14-contacts.png)
- [look1/shots/bg-light/15-documents.png](look1/shots/bg-light/15-documents.png)
- [look1/shots/bg-light/16-scanner.png](look1/shots/bg-light/16-scanner.png)

### look1/shots/en-dark

- [look1/shots/en-dark/01-home.png](look1/shots/en-dark/01-home.png)
- [look1/shots/en-dark/01b-home-scrolled.png](look1/shots/en-dark/01b-home-scrolled.png)
- [look1/shots/en-dark/02-catalog.png](look1/shots/en-dark/02-catalog.png)
- [look1/shots/en-dark/03-book.png](look1/shots/en-dark/03-book.png)
- [look1/shots/en-dark/04-news.png](look1/shots/en-dark/04-news.png)
- [look1/shots/en-dark/05-article.png](look1/shots/en-dark/05-article.png)
- [look1/shots/en-dark/06-calendar.png](look1/shots/en-dark/06-calendar.png)
- [look1/shots/en-dark/07-my.png](look1/shots/en-dark/07-my.png)
- [look1/shots/en-dark/08-want.png](look1/shots/en-dark/08-want.png)
- [look1/shots/en-dark/09-more.png](look1/shots/en-dark/09-more.png)
- [look1/shots/en-dark/10-settings.png](look1/shots/en-dark/10-settings.png)
- [look1/shots/en-dark/11-gallery.png](look1/shots/en-dark/11-gallery.png)
- [look1/shots/en-dark/12-album.png](look1/shots/en-dark/12-album.png)
- [look1/shots/en-dark/13-viewer.png](look1/shots/en-dark/13-viewer.png)
- [look1/shots/en-dark/14-contacts.png](look1/shots/en-dark/14-contacts.png)
- [look1/shots/en-dark/15-documents.png](look1/shots/en-dark/15-documents.png)
- [look1/shots/en-dark/16-scanner.png](look1/shots/en-dark/16-scanner.png)

### look1/shots/en-light

- [look1/shots/en-light/01-home.png](look1/shots/en-light/01-home.png)
- [look1/shots/en-light/01b-home-scrolled.png](look1/shots/en-light/01b-home-scrolled.png)
- [look1/shots/en-light/02-catalog.png](look1/shots/en-light/02-catalog.png)
- [look1/shots/en-light/03-book.png](look1/shots/en-light/03-book.png)
- [look1/shots/en-light/04-news.png](look1/shots/en-light/04-news.png)
- [look1/shots/en-light/05-article.png](look1/shots/en-light/05-article.png)
- [look1/shots/en-light/06-calendar.png](look1/shots/en-light/06-calendar.png)
- [look1/shots/en-light/07-my.png](look1/shots/en-light/07-my.png)
- [look1/shots/en-light/08-want.png](look1/shots/en-light/08-want.png)
- [look1/shots/en-light/09-more.png](look1/shots/en-light/09-more.png)
- [look1/shots/en-light/10-settings.png](look1/shots/en-light/10-settings.png)
- [look1/shots/en-light/11-gallery.png](look1/shots/en-light/11-gallery.png)
- [look1/shots/en-light/12-album.png](look1/shots/en-light/12-album.png)
- [look1/shots/en-light/13-viewer.png](look1/shots/en-light/13-viewer.png)
- [look1/shots/en-light/14-contacts.png](look1/shots/en-light/14-contacts.png)
- [look1/shots/en-light/15-documents.png](look1/shots/en-light/15-documents.png)
- [look1/shots/en-light/16-scanner.png](look1/shots/en-light/16-scanner.png)

### look2/shots/font130-xlarge

- [look2/shots/font130-xlarge/01-home.png](look2/shots/font130-xlarge/01-home.png)
- [look2/shots/font130-xlarge/01b-home-scrolled.png](look2/shots/font130-xlarge/01b-home-scrolled.png)
- [look2/shots/font130-xlarge/02-catalog.png](look2/shots/font130-xlarge/02-catalog.png)
- [look2/shots/font130-xlarge/03-book.png](look2/shots/font130-xlarge/03-book.png)
- [look2/shots/font130-xlarge/04-news.png](look2/shots/font130-xlarge/04-news.png)
- [look2/shots/font130-xlarge/05-article.png](look2/shots/font130-xlarge/05-article.png)
- [look2/shots/font130-xlarge/06-calendar.png](look2/shots/font130-xlarge/06-calendar.png)
- [look2/shots/font130-xlarge/07-my.png](look2/shots/font130-xlarge/07-my.png)
- [look2/shots/font130-xlarge/08-want.png](look2/shots/font130-xlarge/08-want.png)
- [look2/shots/font130-xlarge/09-more.png](look2/shots/font130-xlarge/09-more.png)
- [look2/shots/font130-xlarge/10-settings.png](look2/shots/font130-xlarge/10-settings.png)
- [look2/shots/font130-xlarge/11-gallery.png](look2/shots/font130-xlarge/11-gallery.png)
- [look2/shots/font130-xlarge/12-album.png](look2/shots/font130-xlarge/12-album.png)
- [look2/shots/font130-xlarge/13-viewer.png](look2/shots/font130-xlarge/13-viewer.png)
- [look2/shots/font130-xlarge/14-contacts.png](look2/shots/font130-xlarge/14-contacts.png)
- [look2/shots/font130-xlarge/15-documents.png](look2/shots/font130-xlarge/15-documents.png)
- [look2/shots/font130-xlarge/16-scanner.png](look2/shots/font130-xlarge/16-scanner.png)

### look2/shots/landscape

- [look2/shots/landscape/01-home.png](look2/shots/landscape/01-home.png)
- [look2/shots/landscape/01b-home-scrolled.png](look2/shots/landscape/01b-home-scrolled.png)
- [look2/shots/landscape/02-catalog.png](look2/shots/landscape/02-catalog.png)
- [look2/shots/landscape/03-book.png](look2/shots/landscape/03-book.png)
- [look2/shots/landscape/04-news.png](look2/shots/landscape/04-news.png)
- [look2/shots/landscape/05-article.png](look2/shots/landscape/05-article.png)
- [look2/shots/landscape/06-calendar.png](look2/shots/landscape/06-calendar.png)
- [look2/shots/landscape/07-my.png](look2/shots/landscape/07-my.png)
- [look2/shots/landscape/08-want.png](look2/shots/landscape/08-want.png)
- [look2/shots/landscape/09-more.png](look2/shots/landscape/09-more.png)
- [look2/shots/landscape/10-settings.png](look2/shots/landscape/10-settings.png)
- [look2/shots/landscape/11-gallery.png](look2/shots/landscape/11-gallery.png)
- [look2/shots/landscape/12-album.png](look2/shots/landscape/12-album.png)
- [look2/shots/landscape/13-viewer.png](look2/shots/landscape/13-viewer.png)
- [look2/shots/landscape/14-contacts.png](look2/shots/landscape/14-contacts.png)
- [look2/shots/landscape/15-documents.png](look2/shots/landscape/15-documents.png)
- [look2/shots/landscape/16-scanner.png](look2/shots/landscape/16-scanner.png)

### look3/shots/small-320dp

- [look3/shots/small-320dp/01-home.png](look3/shots/small-320dp/01-home.png)
- [look3/shots/small-320dp/01b-home-scrolled.png](look3/shots/small-320dp/01b-home-scrolled.png)
- [look3/shots/small-320dp/02-catalog.png](look3/shots/small-320dp/02-catalog.png)
- [look3/shots/small-320dp/03-book.png](look3/shots/small-320dp/03-book.png)
- [look3/shots/small-320dp/04-news.png](look3/shots/small-320dp/04-news.png)
- [look3/shots/small-320dp/05-article.png](look3/shots/small-320dp/05-article.png)
- [look3/shots/small-320dp/06-calendar.png](look3/shots/small-320dp/06-calendar.png)
- [look3/shots/small-320dp/07-my.png](look3/shots/small-320dp/07-my.png)
- [look3/shots/small-320dp/08-want.png](look3/shots/small-320dp/08-want.png)
- [look3/shots/small-320dp/09-more.png](look3/shots/small-320dp/09-more.png)
- [look3/shots/small-320dp/10-settings.png](look3/shots/small-320dp/10-settings.png)
- [look3/shots/small-320dp/11-gallery.png](look3/shots/small-320dp/11-gallery.png)
- [look3/shots/small-320dp/12-album.png](look3/shots/small-320dp/12-album.png)
- [look3/shots/small-320dp/13-viewer.png](look3/shots/small-320dp/13-viewer.png)
- [look3/shots/small-320dp/14-contacts.png](look3/shots/small-320dp/14-contacts.png)
- [look3/shots/small-320dp/15-documents.png](look3/shots/small-320dp/15-documents.png)
- [look3/shots/small-320dp/16-scanner.png](look3/shots/small-320dp/16-scanner.png)

### look3/shots/tablet-800dp

- [look3/shots/tablet-800dp/01-home.png](look3/shots/tablet-800dp/01-home.png)
- [look3/shots/tablet-800dp/01b-home-scrolled.png](look3/shots/tablet-800dp/01b-home-scrolled.png)
- [look3/shots/tablet-800dp/02-catalog.png](look3/shots/tablet-800dp/02-catalog.png)
- [look3/shots/tablet-800dp/03-book.png](look3/shots/tablet-800dp/03-book.png)
- [look3/shots/tablet-800dp/04-news.png](look3/shots/tablet-800dp/04-news.png)
- [look3/shots/tablet-800dp/05-article.png](look3/shots/tablet-800dp/05-article.png)
- [look3/shots/tablet-800dp/06-calendar.png](look3/shots/tablet-800dp/06-calendar.png)
- [look3/shots/tablet-800dp/07-my.png](look3/shots/tablet-800dp/07-my.png)
- [look3/shots/tablet-800dp/08-want.png](look3/shots/tablet-800dp/08-want.png)
- [look3/shots/tablet-800dp/09-more.png](look3/shots/tablet-800dp/09-more.png)
- [look3/shots/tablet-800dp/10-settings.png](look3/shots/tablet-800dp/10-settings.png)
- [look3/shots/tablet-800dp/11-gallery.png](look3/shots/tablet-800dp/11-gallery.png)
- [look3/shots/tablet-800dp/12-album.png](look3/shots/tablet-800dp/12-album.png)
- [look3/shots/tablet-800dp/13-viewer.png](look3/shots/tablet-800dp/13-viewer.png)
- [look3/shots/tablet-800dp/14-contacts.png](look3/shots/tablet-800dp/14-contacts.png)
- [look3/shots/tablet-800dp/15-documents.png](look3/shots/tablet-800dp/15-documents.png)
- [look3/shots/tablet-800dp/16-scanner.png](look3/shots/tablet-800dp/16-scanner.png)

### look3/shots/tablet-land

- [look3/shots/tablet-land/01-home.png](look3/shots/tablet-land/01-home.png)
- [look3/shots/tablet-land/01b-home-scrolled.png](look3/shots/tablet-land/01b-home-scrolled.png)
- [look3/shots/tablet-land/02-catalog.png](look3/shots/tablet-land/02-catalog.png)
- [look3/shots/tablet-land/03-book.png](look3/shots/tablet-land/03-book.png)
- [look3/shots/tablet-land/04-news.png](look3/shots/tablet-land/04-news.png)
- [look3/shots/tablet-land/05-article.png](look3/shots/tablet-land/05-article.png)
- [look3/shots/tablet-land/06-calendar.png](look3/shots/tablet-land/06-calendar.png)
- [look3/shots/tablet-land/07-my.png](look3/shots/tablet-land/07-my.png)
- [look3/shots/tablet-land/08-want.png](look3/shots/tablet-land/08-want.png)
- [look3/shots/tablet-land/09-more.png](look3/shots/tablet-land/09-more.png)
- [look3/shots/tablet-land/10-settings.png](look3/shots/tablet-land/10-settings.png)
- [look3/shots/tablet-land/11-gallery.png](look3/shots/tablet-land/11-gallery.png)
- [look3/shots/tablet-land/12-album.png](look3/shots/tablet-land/12-album.png)
- [look3/shots/tablet-land/13-viewer.png](look3/shots/tablet-land/13-viewer.png)
- [look3/shots/tablet-land/14-contacts.png](look3/shots/tablet-land/14-contacts.png)
- [look3/shots/tablet-land/15-documents.png](look3/shots/tablet-land/15-documents.png)
- [look3/shots/tablet-land/16-scanner.png](look3/shots/tablet-land/16-scanner.png)

### media/shots/media

- [media/shots/media/01-gallery.png](media/shots/media/01-gallery.png)
- [media/shots/media/02-album-1.png](media/shots/media/02-album-1.png)
- [media/shots/media/03-viewer-1x.png](media/shots/media/03-viewer-1x.png)
- [media/shots/media/04-viewer-2x-doubletap.png](media/shots/media/04-viewer-2x-doubletap.png)
- [media/shots/media/05-viewer-pinch-1s.png](media/shots/media/05-viewer-pinch-1s.png)
- [media/shots/media/06-viewer-pinch-more-5s.png](media/shots/media/06-viewer-pinch-more-5s.png)
- [media/shots/media/07-viewer-zoomed-pan.png](media/shots/media/07-viewer-zoomed-pan.png)
- [media/shots/media/08-viewer-reset.png](media/shots/media/08-viewer-reset.png)
- [media/shots/media/09-viewer-after-20.png](media/shots/media/09-viewer-after-20.png)
- [media/shots/media/10-catalog-0s.png](media/shots/media/10-catalog-0s.png)
- [media/shots/media/11-catalog-3s.png](media/shots/media/11-catalog-3s.png)
- [media/shots/media/12-catalog-9s.png](media/shots/media/12-catalog-9s.png)
- [media/shots/media/13-catalog-scrolled.png](media/shots/media/13-catalog-scrolled.png)
- [media/shots/media/14-catalog-12.png](media/shots/media/14-catalog-12.png)
- [media/shots/media/15-book.png](media/shots/media/15-book.png)
- [media/shots/media/16-book-2.png](media/shots/media/16-book-2.png)
- [media/shots/media/20-documents.png](media/shots/media/20-documents.png)
- [media/shots/media/21-publications.png](media/shots/media/21-publications.png)
- [media/shots/media/pdf-offline-a.png](media/shots/media/pdf-offline-a.png)
- [media/shots/media/pdf-offline-b.png](media/shots/media/pdf-offline-b.png)
- [media/shots/media/pdf-offline-c.png](media/shots/media/pdf-offline-c.png)
- [media/shots/media/pdf-offline-d.png](media/shots/media/pdf-offline-d.png)
- [media/shots/media/pdf-online-a.png](media/shots/media/pdf-online-a.png)
- [media/shots/media/pdf-online-b.png](media/shots/media/pdf-online-b.png)
- [media/shots/media/pdf-online-c.png](media/shots/media/pdf-online-c.png)
- [media/shots/media/pdf-online-d.png](media/shots/media/pdf-online-d.png)
- [media/shots/media/pdf-publication-a.png](media/shots/media/pdf-publication-a.png)
- [media/shots/media/pdf-publication-b.png](media/shots/media/pdf-publication-b.png)
- [media/shots/media/pdf-publication-c.png](media/shots/media/pdf-publication-c.png)
- [media/shots/media/pdf-publication-d.png](media/shots/media/pdf-publication-d.png)

### mem-base/shots

- [mem-base/shots/cycles-after-gallery.png](mem-base/shots/cycles-after-gallery.png)
- [mem-base/shots/cycles-book.png](mem-base/shots/cycles-book.png)

### mem/shots

- [mem/shots/cycles-after-gallery.png](mem/shots/cycles-after-gallery.png)
- [mem/shots/cycles-book.png](mem/shots/cycles-book.png)

### monkey/shots

- [monkey/shots/monkey-end.png](monkey/shots/monkey-end.png)

### offline/shots/offline-cached

- [offline/shots/offline-cached/01-home.png](offline/shots/offline-cached/01-home.png)
- [offline/shots/offline-cached/02-catalog.png](offline/shots/offline-cached/02-catalog.png)
- [offline/shots/offline-cached/03-book.png](offline/shots/offline-cached/03-book.png)
- [offline/shots/offline-cached/04-my.png](offline/shots/offline-cached/04-my.png)
- [offline/shots/offline-cached/05-want.png](offline/shots/offline-cached/05-want.png)
- [offline/shots/offline-cached/06-news.png](offline/shots/offline-cached/06-news.png)
- [offline/shots/offline-cached/07-article.png](offline/shots/offline-cached/07-article.png)
- [offline/shots/offline-cached/08-calendar.png](offline/shots/offline-cached/08-calendar.png)
- [offline/shots/offline-cached/09-more.png](offline/shots/offline-cached/09-more.png)
- [offline/shots/offline-cached/10-gallery.png](offline/shots/offline-cached/10-gallery.png)
- [offline/shots/offline-cached/10b-album.png](offline/shots/offline-cached/10b-album.png)
- [offline/shots/offline-cached/11-contacts.png](offline/shots/offline-cached/11-contacts.png)
- [offline/shots/offline-cached/12-documents.png](offline/shots/offline-cached/12-documents.png)

### offline/shots/offline-empty

- [offline/shots/offline-empty/00-my-guest.png](offline/shots/offline-empty/00-my-guest.png)
- [offline/shots/offline-empty/01-home.png](offline/shots/offline-empty/01-home.png)
- [offline/shots/offline-empty/02-catalog.png](offline/shots/offline-empty/02-catalog.png)
- [offline/shots/offline-empty/03-book.png](offline/shots/offline-empty/03-book.png)
- [offline/shots/offline-empty/04-my.png](offline/shots/offline-empty/04-my.png)
- [offline/shots/offline-empty/05-want.png](offline/shots/offline-empty/05-want.png)
- [offline/shots/offline-empty/06-news.png](offline/shots/offline-empty/06-news.png)
- [offline/shots/offline-empty/08-calendar.png](offline/shots/offline-empty/08-calendar.png)
- [offline/shots/offline-empty/09-more.png](offline/shots/offline-empty/09-more.png)
- [offline/shots/offline-empty/10-gallery.png](offline/shots/offline-empty/10-gallery.png)
- [offline/shots/offline-empty/11-contacts.png](offline/shots/offline-empty/11-contacts.png)
- [offline/shots/offline-empty/12-documents.png](offline/shots/offline-empty/12-documents.png)
- [offline/shots/offline-empty/20-my-guest-2.png](offline/shots/offline-empty/20-my-guest-2.png)

### offline/shots/online-warm

- [offline/shots/online-warm/01-home.png](offline/shots/online-warm/01-home.png)
- [offline/shots/online-warm/02-catalog.png](offline/shots/online-warm/02-catalog.png)
- [offline/shots/online-warm/03-book.png](offline/shots/online-warm/03-book.png)
- [offline/shots/online-warm/04-my.png](offline/shots/online-warm/04-my.png)
- [offline/shots/online-warm/05-want.png](offline/shots/online-warm/05-want.png)
- [offline/shots/online-warm/06-news.png](offline/shots/online-warm/06-news.png)
- [offline/shots/online-warm/07-article.png](offline/shots/online-warm/07-article.png)
- [offline/shots/online-warm/08-calendar.png](offline/shots/online-warm/08-calendar.png)
- [offline/shots/online-warm/09-more.png](offline/shots/online-warm/09-more.png)
- [offline/shots/online-warm/10-gallery.png](offline/shots/online-warm/10-gallery.png)
- [offline/shots/online-warm/10b-album.png](offline/shots/online-warm/10b-album.png)
- [offline/shots/online-warm/11-contacts.png](offline/shots/online-warm/11-contacts.png)
- [offline/shots/online-warm/12-documents.png](offline/shots/online-warm/12-documents.png)

### offline/shots/recovered

- [offline/shots/recovered/00-contacts-after-reconnect.png](offline/shots/recovered/00-contacts-after-reconnect.png)
- [offline/shots/recovered/01-home.png](offline/shots/recovered/01-home.png)
- [offline/shots/recovered/02-catalog.png](offline/shots/recovered/02-catalog.png)
- [offline/shots/recovered/03-book.png](offline/shots/recovered/03-book.png)
- [offline/shots/recovered/04-my.png](offline/shots/recovered/04-my.png)
- [offline/shots/recovered/05-want.png](offline/shots/recovered/05-want.png)
- [offline/shots/recovered/06-news.png](offline/shots/recovered/06-news.png)
- [offline/shots/recovered/07-article.png](offline/shots/recovered/07-article.png)
- [offline/shots/recovered/08-calendar.png](offline/shots/recovered/08-calendar.png)
- [offline/shots/recovered/09-more.png](offline/shots/recovered/09-more.png)
- [offline/shots/recovered/10-gallery.png](offline/shots/recovered/10-gallery.png)
- [offline/shots/recovered/10b-album.png](offline/shots/recovered/10b-album.png)
- [offline/shots/recovered/11-contacts.png](offline/shots/recovered/11-contacts.png)
- [offline/shots/recovered/12-documents.png](offline/shots/recovered/12-documents.png)

### offline/shots/recovered-empty

- [offline/shots/recovered-empty/00-gallery-after-reconnect.png](offline/shots/recovered-empty/00-gallery-after-reconnect.png)
- [offline/shots/recovered-empty/01-home.png](offline/shots/recovered-empty/01-home.png)
- [offline/shots/recovered-empty/02-catalog.png](offline/shots/recovered-empty/02-catalog.png)
- [offline/shots/recovered-empty/03-book.png](offline/shots/recovered-empty/03-book.png)
- [offline/shots/recovered-empty/04-my.png](offline/shots/recovered-empty/04-my.png)
- [offline/shots/recovered-empty/05-want.png](offline/shots/recovered-empty/05-want.png)
- [offline/shots/recovered-empty/06-news.png](offline/shots/recovered-empty/06-news.png)
- [offline/shots/recovered-empty/07-article.png](offline/shots/recovered-empty/07-article.png)
- [offline/shots/recovered-empty/08-calendar.png](offline/shots/recovered-empty/08-calendar.png)
- [offline/shots/recovered-empty/09-more.png](offline/shots/recovered-empty/09-more.png)
- [offline/shots/recovered-empty/10-gallery.png](offline/shots/recovered-empty/10-gallery.png)
- [offline/shots/recovered-empty/10b-album.png](offline/shots/recovered-empty/10b-album.png)
- [offline/shots/recovered-empty/11-contacts.png](offline/shots/recovered-empty/11-contacts.png)
- [offline/shots/recovered-empty/12-documents.png](offline/shots/recovered-empty/12-documents.png)

### rot/shots/procdeath

- [rot/shots/procdeath/book-1-before.png](rot/shots/procdeath/book-1-before.png)
- [rot/shots/procdeath/book-2-recents.png](rot/shots/procdeath/book-2-recents.png)
- [rot/shots/procdeath/book-3-restored.png](rot/shots/procdeath/book-3-restored.png)
- [rot/shots/procdeath/calendar-1-before.png](rot/shots/procdeath/calendar-1-before.png)
- [rot/shots/procdeath/calendar-2-recents.png](rot/shots/procdeath/calendar-2-recents.png)
- [rot/shots/procdeath/calendar-3-restored.png](rot/shots/procdeath/calendar-3-restored.png)
- [rot/shots/procdeath/catalog-1-before.png](rot/shots/procdeath/catalog-1-before.png)
- [rot/shots/procdeath/catalog-3-restored.png](rot/shots/procdeath/catalog-3-restored.png)
- [rot/shots/procdeath/home-1-before.png](rot/shots/procdeath/home-1-before.png)
- [rot/shots/procdeath/home-3-restored.png](rot/shots/procdeath/home-3-restored.png)
- [rot/shots/procdeath/want-1-before.png](rot/shots/procdeath/want-1-before.png)
- [rot/shots/procdeath/want-3-restored.png](rot/shots/procdeath/want-3-restored.png)

### rot/shots/rotation

- [rot/shots/rotation/book-0-before.png](rot/shots/rotation/book-0-before.png)
- [rot/shots/rotation/book-1-landscape.png](rot/shots/rotation/book-1-landscape.png)
- [rot/shots/rotation/book-2-after.png](rot/shots/rotation/book-2-after.png)
- [rot/shots/rotation/calendar-0-before.png](rot/shots/rotation/calendar-0-before.png)
- [rot/shots/rotation/calendar-1-landscape.png](rot/shots/rotation/calendar-1-landscape.png)
- [rot/shots/rotation/calendar-2-after.png](rot/shots/rotation/calendar-2-after.png)
- [rot/shots/rotation/catalog-0-before.png](rot/shots/rotation/catalog-0-before.png)
- [rot/shots/rotation/catalog-1-landscape.png](rot/shots/rotation/catalog-1-landscape.png)
- [rot/shots/rotation/catalog-2-after.png](rot/shots/rotation/catalog-2-after.png)
- [rot/shots/rotation/home-0-before.png](rot/shots/rotation/home-0-before.png)
- [rot/shots/rotation/home-1-landscape.png](rot/shots/rotation/home-1-landscape.png)
- [rot/shots/rotation/home-2-after.png](rot/shots/rotation/home-2-after.png)
- [rot/shots/rotation/want-0-before.png](rot/shots/rotation/want-0-before.png)
- [rot/shots/rotation/want-1-landscape.png](rot/shots/rotation/want-1-landscape.png)
- [rot/shots/rotation/want-2-after.png](rot/shots/rotation/want-2-after.png)

### update/shots/update

- [update/shots/update/00-base-first.png](update/shots/update/00-base-first.png)
- [update/shots/update/01-after-adb-install.png](update/shots/update/01-after-adb-install.png)
- [update/shots/update/02-want-after-adb-update.png](update/shots/update/02-want-after-adb-update.png)
- [update/shots/update/10-update-dialog.png](update/shots/update/10-update-dialog.png)
- [update/shots/update/12-after-install.png](update/shots/update/12-after-install.png)
- [update/shots/update/13-whats-new.png](update/shots/update/13-whats-new.png)
- [update/shots/update/14-second-launch.png](update/shots/update/14-second-launch.png)
- [update/shots/update/15-want-after-in-app-update.png](update/shots/update/15-want-after-in-app-update.png)

## Other files

- [abstart-1/cat-first-base.txt](abstart-1/cat-first-base.txt)
- [abstart-1/cat-first-new.txt](abstart-1/cat-first-new.txt)
- [abstart-1/cat-ready-base.txt](abstart-1/cat-ready-base.txt)
- [abstart-1/cat-ready-new.txt](abstart-1/cat-ready-new.txt)
- [abstart-1/host-after-run.txt](abstart-1/host-after-run.txt)
- [abstart-1/job-status.txt](abstart-1/job-status.txt)
- [abstart-1/logcat-all.txt](abstart-1/logcat-all.txt)
- [abstart-1/logcat-dump.txt](abstart-1/logcat-dump.txt)
- [abstart-1/logcat-findings.txt](abstart-1/logcat-findings.txt)
- [abstart-1/logcat-live.txt](abstart-1/logcat-live.txt)
- [abstart-1/start-base-1-cold.txt](abstart-1/start-base-1-cold.txt)
- [abstart-1/start-base-1-warm.txt](abstart-1/start-base-1-warm.txt)
- [abstart-1/start-base-2-cold.txt](abstart-1/start-base-2-cold.txt)
- [abstart-1/start-base-2-warm.txt](abstart-1/start-base-2-warm.txt)
- [abstart-1/start-base-cold-all.txt](abstart-1/start-base-cold-all.txt)
- [abstart-1/start-base-warm-all.txt](abstart-1/start-base-warm-all.txt)
- [abstart-1/start-new-1-cold.txt](abstart-1/start-new-1-cold.txt)
- [abstart-1/start-new-1-warm.txt](abstart-1/start-new-1-warm.txt)
- [abstart-1/start-new-2-cold.txt](abstart-1/start-new-2-cold.txt)
- [abstart-1/start-new-2-warm.txt](abstart-1/start-new-2-warm.txt)
- [abstart-1/start-new-cold-all.txt](abstart-1/start-new-cold-all.txt)
- [abstart-1/start-new-warm-all.txt](abstart-1/start-new-warm-all.txt)
- [abstart-1/summary.txt](abstart-1/summary.txt)
- [abstart-2/cat-first-base.txt](abstart-2/cat-first-base.txt)
- [abstart-2/cat-first-new.txt](abstart-2/cat-first-new.txt)
- [abstart-2/cat-ready-base.txt](abstart-2/cat-ready-base.txt)
- [abstart-2/cat-ready-new.txt](abstart-2/cat-ready-new.txt)
- [abstart-2/host-after-run.txt](abstart-2/host-after-run.txt)
- [abstart-2/job-status.txt](abstart-2/job-status.txt)
- [abstart-2/logcat-all.txt](abstart-2/logcat-all.txt)
- [abstart-2/logcat-dump.txt](abstart-2/logcat-dump.txt)
- [abstart-2/logcat-findings.txt](abstart-2/logcat-findings.txt)
- [abstart-2/logcat-live.txt](abstart-2/logcat-live.txt)
- [abstart-2/start-base-1-cold.txt](abstart-2/start-base-1-cold.txt)
- [abstart-2/start-base-1-warm.txt](abstart-2/start-base-1-warm.txt)
- [abstart-2/start-base-2-cold.txt](abstart-2/start-base-2-cold.txt)
- [abstart-2/start-base-2-warm.txt](abstart-2/start-base-2-warm.txt)
- [abstart-2/start-base-cold-all.txt](abstart-2/start-base-cold-all.txt)
- [abstart-2/start-base-warm-all.txt](abstart-2/start-base-warm-all.txt)
- [abstart-2/start-new-1-cold.txt](abstart-2/start-new-1-cold.txt)
- [abstart-2/start-new-1-warm.txt](abstart-2/start-new-1-warm.txt)
- [abstart-2/start-new-2-cold.txt](abstart-2/start-new-2-cold.txt)
- [abstart-2/start-new-2-warm.txt](abstart-2/start-new-2-warm.txt)
- [abstart-2/start-new-cold-all.txt](abstart-2/start-new-cold-all.txt)
- [abstart-2/start-new-warm-all.txt](abstart-2/start-new-warm-all.txt)
- [abstart-2/summary.txt](abstart-2/summary.txt)
- [func1/host-after-run.txt](func1/host-after-run.txt)
- [func1/job-status.txt](func1/job-status.txt)
- [func1/logcat-all.txt](func1/logcat-all.txt)
- [func1/logcat-dump.txt](func1/logcat-dump.txt)
- [func1/logcat-findings.txt](func1/logcat-findings.txt)
- [func1/logcat-live.txt](func1/logcat-live.txt)
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
- [jank-1/gfx-base-album.txt](jank-1/gfx-base-album.txt)
- [jank-1/gfx-base-calendar.txt](jank-1/gfx-base-calendar.txt)
- [jank-1/gfx-base-catalog.txt](jank-1/gfx-base-catalog.txt)
- [jank-1/gfx-base-gallery.txt](jank-1/gfx-base-gallery.txt)
- [jank-1/gfx-base-home.txt](jank-1/gfx-base-home.txt)
- [jank-1/gfx-base-news.txt](jank-1/gfx-base-news.txt)
- [jank-1/gfx-base-viewer.txt](jank-1/gfx-base-viewer.txt)
- [jank-1/gfx-base-want.txt](jank-1/gfx-base-want.txt)
- [jank-1/gfx-new-album.txt](jank-1/gfx-new-album.txt)
- [jank-1/gfx-new-calendar.txt](jank-1/gfx-new-calendar.txt)
- [jank-1/gfx-new-catalog.txt](jank-1/gfx-new-catalog.txt)
- [jank-1/gfx-new-gallery.txt](jank-1/gfx-new-gallery.txt)
- [jank-1/gfx-new-home.txt](jank-1/gfx-new-home.txt)
- [jank-1/gfx-new-news.txt](jank-1/gfx-new-news.txt)
- [jank-1/gfx-new-viewer.txt](jank-1/gfx-new-viewer.txt)
- [jank-1/gfx-new-want.txt](jank-1/gfx-new-want.txt)
- [jank-1/host-after-run.txt](jank-1/host-after-run.txt)
- [jank-1/job-status.txt](jank-1/job-status.txt)
- [jank-1/logcat-all.txt](jank-1/logcat-all.txt)
- [jank-1/logcat-dump.txt](jank-1/logcat-dump.txt)
- [jank-1/logcat-findings.txt](jank-1/logcat-findings.txt)
- [jank-1/logcat-live.txt](jank-1/logcat-live.txt)
- [jank-1/mem-base-jank-end-raw.txt](jank-1/mem-base-jank-end-raw.txt)
- [jank-1/mem-base-jank-end.txt](jank-1/mem-base-jank-end.txt)
- [jank-1/mem-new-jank-end-raw.txt](jank-1/mem-new-jank-end-raw.txt)
- [jank-1/mem-new-jank-end.txt](jank-1/mem-new-jank-end.txt)
- [jank-1/summary.txt](jank-1/summary.txt)
- [jank-2/debug/1-viewer.xml](jank-2/debug/1-viewer.xml)
- [jank-2/debug/2-viewer.xml](jank-2/debug/2-viewer.xml)
- [jank-2/gfx-base-album.txt](jank-2/gfx-base-album.txt)
- [jank-2/gfx-base-calendar.txt](jank-2/gfx-base-calendar.txt)
- [jank-2/gfx-base-catalog.txt](jank-2/gfx-base-catalog.txt)
- [jank-2/gfx-base-gallery.txt](jank-2/gfx-base-gallery.txt)
- [jank-2/gfx-base-home.txt](jank-2/gfx-base-home.txt)
- [jank-2/gfx-base-news.txt](jank-2/gfx-base-news.txt)
- [jank-2/gfx-base-want.txt](jank-2/gfx-base-want.txt)
- [jank-2/gfx-new-album.txt](jank-2/gfx-new-album.txt)
- [jank-2/gfx-new-calendar.txt](jank-2/gfx-new-calendar.txt)
- [jank-2/gfx-new-catalog.txt](jank-2/gfx-new-catalog.txt)
- [jank-2/gfx-new-gallery.txt](jank-2/gfx-new-gallery.txt)
- [jank-2/gfx-new-home.txt](jank-2/gfx-new-home.txt)
- [jank-2/gfx-new-news.txt](jank-2/gfx-new-news.txt)
- [jank-2/gfx-new-want.txt](jank-2/gfx-new-want.txt)
- [jank-2/host-after-run.txt](jank-2/host-after-run.txt)
- [jank-2/job-status.txt](jank-2/job-status.txt)
- [jank-2/logcat-all.txt](jank-2/logcat-all.txt)
- [jank-2/logcat-dump.txt](jank-2/logcat-dump.txt)
- [jank-2/logcat-findings.txt](jank-2/logcat-findings.txt)
- [jank-2/logcat-live.txt](jank-2/logcat-live.txt)
- [jank-2/mem-base-jank-end-raw.txt](jank-2/mem-base-jank-end-raw.txt)
- [jank-2/mem-base-jank-end.txt](jank-2/mem-base-jank-end.txt)
- [jank-2/mem-new-jank-end-raw.txt](jank-2/mem-new-jank-end-raw.txt)
- [jank-2/mem-new-jank-end.txt](jank-2/mem-new-jank-end.txt)
- [jank-2/summary.txt](jank-2/summary.txt)
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
- [look3/host-after-run.txt](look3/host-after-run.txt)
- [look3/job-status.txt](look3/job-status.txt)
- [look3/logcat-all.txt](look3/logcat-all.txt)
- [look3/logcat-dump.txt](look3/logcat-dump.txt)
- [look3/logcat-findings.txt](look3/logcat-findings.txt)
- [look3/logcat-live.txt](look3/logcat-live.txt)
- [look3/summary.txt](look3/summary.txt)
- [media/gallery-texts.txt](media/gallery-texts.txt)
- [media/getevent.txt](media/getevent.txt)
- [media/gfx-viewer-20.txt](media/gfx-viewer-20.txt)
- [media/host-after-run.txt](media/host-after-run.txt)
- [media/job-status.txt](media/job-status.txt)
- [media/logcat-all.txt](media/logcat-all.txt)
- [media/logcat-dump.txt](media/logcat-dump.txt)
- [media/logcat-findings.txt](media/logcat-findings.txt)
- [media/logcat-live.txt](media/logcat-live.txt)
- [media/mem-gal-after-20-raw.txt](media/mem-gal-after-20-raw.txt)
- [media/mem-gal-after-20.txt](media/mem-gal-after-20.txt)
- [media/mem-gal-back-home-raw.txt](media/mem-gal-back-home-raw.txt)
- [media/mem-gal-back-home.txt](media/mem-gal-back-home.txt)
- [media/mem-gal-before-raw.txt](media/mem-gal-before-raw.txt)
- [media/mem-gal-before.txt](media/mem-gal-before.txt)
- [media/mem-gal-viewer-raw.txt](media/mem-gal-viewer-raw.txt)
- [media/mem-gal-viewer.txt](media/mem-gal-viewer.txt)
- [media/mem-gal-zoomed-raw.txt](media/mem-gal-zoomed-raw.txt)
- [media/mem-gal-zoomed.txt](media/mem-gal-zoomed.txt)
- [media/mt.sh](media/mt.sh)
- [media/summary.txt](media/summary.txt)
- [mem-base/host-after-run.txt](mem-base/host-after-run.txt)
- [mem-base/job-status.txt](mem-base/job-status.txt)
- [mem-base/logcat-all.txt](mem-base/logcat-all.txt)
- [mem-base/logcat-dump.txt](mem-base/logcat-dump.txt)
- [mem-base/logcat-findings.txt](mem-base/logcat-findings.txt)
- [mem-base/logcat-live.txt](mem-base/logcat-live.txt)
- [mem-base/mem-c00-raw.txt](mem-base/mem-c00-raw.txt)
- [mem-base/mem-c00.txt](mem-base/mem-c00.txt)
- [mem-base/mem-c10-raw.txt](mem-base/mem-c10-raw.txt)
- [mem-base/mem-c10.txt](mem-base/mem-c10.txt)
- [mem-base/mem-c20-raw.txt](mem-base/mem-c20-raw.txt)
- [mem-base/mem-c20.txt](mem-base/mem-c20.txt)
- [mem-base/mem-c30-raw.txt](mem-base/mem-c30-raw.txt)
- [mem-base/mem-c30.txt](mem-base/mem-c30.txt)
- [mem-base/summary.txt](mem-base/summary.txt)
- [mem/host-after-run.txt](mem/host-after-run.txt)
- [mem/job-status.txt](mem/job-status.txt)
- [mem/logcat-all.txt](mem/logcat-all.txt)
- [mem/logcat-dump.txt](mem/logcat-dump.txt)
- [mem/logcat-findings.txt](mem/logcat-findings.txt)
- [mem/logcat-live.txt](mem/logcat-live.txt)
- [mem/mem-c00-raw.txt](mem/mem-c00-raw.txt)
- [mem/mem-c00.txt](mem/mem-c00.txt)
- [mem/mem-c10-raw.txt](mem/mem-c10-raw.txt)
- [mem/mem-c10.txt](mem/mem-c10.txt)
- [mem/mem-c20-raw.txt](mem/mem-c20-raw.txt)
- [mem/mem-c20.txt](mem/mem-c20.txt)
- [mem/mem-c30-raw.txt](mem/mem-c30-raw.txt)
- [mem/mem-c30.txt](mem/mem-c30.txt)
- [mem/summary.txt](mem/summary.txt)
- [monkey/host-after-run.txt](monkey/host-after-run.txt)
- [monkey/job-status.txt](monkey/job-status.txt)
- [monkey/logcat-all.txt](monkey/logcat-all.txt)
- [monkey/logcat-dump.txt](monkey/logcat-dump.txt)
- [monkey/logcat-findings.txt](monkey/logcat-findings.txt)
- [monkey/logcat-live.txt](monkey/logcat-live.txt)
- [monkey/mem-after-monkey-raw.txt](monkey/mem-after-monkey-raw.txt)
- [monkey/mem-after-monkey.txt](monkey/mem-after-monkey.txt)
- [monkey/monkey.txt](monkey/monkey.txt)
- [monkey/summary.txt](monkey/summary.txt)
- [offline/host-after-run.txt](offline/host-after-run.txt)
- [offline/job-status.txt](offline/job-status.txt)
- [offline/logcat-all.txt](offline/logcat-all.txt)
- [offline/logcat-dump.txt](offline/logcat-dump.txt)
- [offline/logcat-findings.txt](offline/logcat-findings.txt)
- [offline/logcat-live.txt](offline/logcat-live.txt)
- [offline/summary.txt](offline/summary.txt)
- [rot/gfx-rotation.txt](rot/gfx-rotation.txt)
- [rot/host-after-run.txt](rot/host-after-run.txt)
- [rot/job-status.txt](rot/job-status.txt)
- [rot/logcat-all.txt](rot/logcat-all.txt)
- [rot/logcat-dump.txt](rot/logcat-dump.txt)
- [rot/logcat-findings.txt](rot/logcat-findings.txt)
- [rot/logcat-live.txt](rot/logcat-live.txt)
- [rot/start-procdeath-book.txt](rot/start-procdeath-book.txt)
- [rot/start-procdeath-calendar.txt](rot/start-procdeath-calendar.txt)
- [rot/start-procdeath-catalog.txt](rot/start-procdeath-catalog.txt)
- [rot/start-procdeath-home.txt](rot/start-procdeath-home.txt)
- [rot/start-procdeath-want.txt](rot/start-procdeath-want.txt)
- [rot/summary.txt](rot/summary.txt)
- [update/host-after-run.txt](update/host-after-run.txt)
- [update/job-status.txt](update/job-status.txt)
- [update/logcat-all.txt](update/logcat-all.txt)
- [update/logcat-dump.txt](update/logcat-dump.txt)
- [update/logcat-findings.txt](update/logcat-findings.txt)
- [update/logcat-live.txt](update/logcat-live.txt)
- [update/summary.txt](update/summary.txt)
- [update/update.json](update/update.json)
