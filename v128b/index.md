# v1.2.28 full emulator test (released app, tag v1.2.28 = 6e2c640, vs v1.2.27 = 8dc63c1)

Source: `debug/full-test-4` @ bacaa93, last run 41228034302 (attempt 1), 2026-10-31T18:52:02Z. Runs: [runs.txt](runs.txt).
Emulators: google_apis x86_64 API 33 (search34: API 34), pixel_6 profile, -gpu swangle_indirect; app locale bg-BG unless a part says otherwise. APKs: minified prodRelease, x86_64 added, throwaway key; v1.2.28 versionCode 1488541, v1.2.27 versionCode 1484479.
Earlier: wave 14 check [../v128/index.md](../v128/index.md), v1.2.27 full test [../v127b/index.md](../v127b/index.md).

## APK size (release configuration: arm64-v8a + armeabi-v7a, no x86_64)

```
== new 1.2.28 6e2c640: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4290964 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488541  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.28" (Raw: "1.2.28")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
part                    compressed  uncompressed  files
classes*.dex               3510555       6893172      2
res                         468767        833220     58
resources.arsc              213416        213416      1
lib/arm64-v8a                26028         57912      4
lib/armeabi-v7a              22503         41024      4
assets/                      19172         19172      2
AndroidManifest.xml           4890         20880      1
META-INF/                      845          1288      6
== base 1.2.27 8dc63c1: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4759051 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1484479  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.27" (Raw: "1.2.27")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=false 
part                    compressed  uncompressed  files
classes*.dex               3318824       6510820      2
res                         834675       1732326   1006
resources.arsc              314344        314344      1
lib/arm64-v8a                57912         57912      4
lib/armeabi-v7a              41024         41024      4
assets/                      18394         18394      2
AndroidManifest.xml           4768         20304      1
META-INF/                      846          1288      6
```

| part | status |
|---|---|
| a11y | COMPLETE; part=a11y api=33 gpu=swangle_indirect emulator-step=success |
| abstart-1 | COMPLETE; part=abstart-1 api=33 gpu=swangle_indirect emulator-step=success |
| abstart-2 | COMPLETE; part=abstart-2 api=33 gpu=swangle_indirect emulator-step=success |
| abstart-3 | COMPLETE; part=abstart-3 api=33 gpu=swangle_indirect emulator-step=success |
| abstart-4 | COMPLETE; part=abstart-4 api=33 gpu=swangle_indirect emulator-step=success |
| func | COMPLETE; part=func api=33 gpu=swangle_indirect emulator-step=success |
| homey | COMPLETE; part=homey api=33 gpu=swangle_indirect emulator-step=success |
| jank-1 | COMPLETE; part=jank-1 api=33 gpu=swangle_indirect emulator-step=success |
| jank-2 | COMPLETE; part=jank-2 api=33 gpu=swangle_indirect emulator-step=success |
| jankv | COMPLETE; part=jankv api=33 gpu=swangle_indirect emulator-step=success |
| look1 | COMPLETE; part=look1 api=33 gpu=swangle_indirect emulator-step=success |
| look2 | COMPLETE; part=look2 api=33 gpu=swangle_indirect emulator-step=success |
| look3 | COMPLETE; part=look3 api=33 gpu=swangle_indirect emulator-step=success |
| media | COMPLETE; part=media api=33 gpu=swangle_indirect emulator-step=success |
| mem-base | COMPLETE; part=mem-base api=33 gpu=swangle_indirect emulator-step=success |
| mem | COMPLETE; part=mem api=33 gpu=swangle_indirect emulator-step=success |
| monkey-base | COMPLETE; part=monkey-base api=33 gpu=swangle_indirect emulator-step=success |
| monkey | COMPLETE; part=monkey api=33 gpu=swangle_indirect emulator-step=success |
| offline | COMPLETE; part=offline api=33 gpu=swangle_indirect emulator-step=success |
| rot | COMPLETE; part=rot api=33 gpu=swangle_indirect emulator-step=success |
| search34 | COMPLETE; part=search34 api=34 gpu=swangle_indirect emulator-step=success |
| widget | COMPLETE; part=widget api=33 gpu=swangle_indirect emulator-step=success |
| widgetlang | COMPLETE; part=widgetlang api=33 gpu=swangle_indirect emulator-step=success |
| widgetupd | COMPLETE; part=widgetupd api=33 gpu=swangle_indirect emulator-step=success |
| zoom | COMPLETE; part=zoom api=33 gpu=swangle_indirect emulator-step=success |

## a11y

```
part a11y; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2917 ms
## A. Accessibility (uiautomator): main screens, density 420
A11Y 01-home: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 02-search-expanded: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 03-catalog: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 04-book: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2
   SMALL: 'Сигнатура ЦБ/820(73)/Г 23' 371x30dp; 'Инвентарен номер 399560' 371x30dp
A11Y 05-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 06-article: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 07-calendar: targets=18 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 08-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 09-want: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 10-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 11-settings: targets=11 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
   MISSING: View@[0,2317][1080,2400]
A11Y 12-gallery: targets=9 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
   MISSING: View@[42,2321][524,2400]; View@[556,2321][1038,2400]
A11Y 13-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0
   SAME-LABEL: 'Снимката липсва' x10
A11Y 14-viewer: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 15-contacts: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 16-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0
   SAME-LABEL: 'Отвори с приложение за PDF' x3
A11Y 17-login: targets=6 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
   MISSING: CheckBox@[64,1162][190,1288]
A11Y 18-scanner-manual: targets=4 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
   MISSING: View@[0,296][1080,860]
## A2. The same, English, font 1.3 (Home, Catalogue, Book, More)
A11Y 21-home-en-font130: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1
   SMALL: 'All' 58x42dp
A11Y 22-catalog-en-font130: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 23-book-en-font130: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1
   SMALL: 'Call number ЦБ/820(73)/Г 23' 371x36dp
A11Y 24-more-en-font130: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
## A3. Totals over all screens
A11Y TOTAL: SMALL(<48dp)=4 SAME-LABEL=2 MISSING=5 DOUBLE=0 
## COMPLETE
## logcat findings
app pids seen: ['4716', '6581', '9895']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 12
   skipped-frames events=12 max=82 total=585
== Davey: 33
   davey events=33 max=2115ms median=863ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-1

```
part abstart-1; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-0 FIRST-INSTALL catalogue results after 3565 ms
new-0 INDEX-READY #1 catalogue results after 2796 ms
new-0 INDEX-READY #2 catalogue results after 3967 ms
new-0 INDEX-READY #3 catalogue results after 2895 ms
START cold new-0         n=10 min=1354ms median=1860ms max=3096ms  all=[2409, 1849, 3096, 3034, 2836, 1354, 1871, 1461, 1654, 1602]  states=['COLD']
START warm new-0         n=10 min=353ms median=510ms max=858ms  all=[684, 858, 519, 501, 580, 353, 478, 453, 741, 372]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2594 ms
base-1 INDEX-READY #1 catalogue results after 2775 ms
base-1 INDEX-READY #2 catalogue results after 2903 ms
base-1 INDEX-READY #3 catalogue results after 2540 ms
START cold base-1        n=10 min=1326ms median=1578ms max=2044ms  all=[1879, 1741, 1591, 1326, 1600, 1466, 1484, 1538, 2044, 1566]  states=['COLD']
START warm base-1        n=10 min=337ms median=516ms max=863ms  all=[484, 655, 571, 535, 337, 467, 484, 577, 498, 863]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2628 ms
new-1 INDEX-READY #1 catalogue results after 2694 ms
new-1 INDEX-READY #2 catalogue results after 2742 ms
new-1 INDEX-READY #3 catalogue results after 2976 ms
START cold new-1         n=10 min=1390ms median=1470ms max=1904ms  all=[1442, 1904, 1466, 1539, 1880, 1434, 1395, 1473, 1762, 1390]  states=['COLD']
START warm new-1         n=10 min=289ms median=456ms max=731ms  all=[289, 731, 431, 344, 543, 674, 295, 610, 308, 482]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2885 ms
base-2 INDEX-READY #1 catalogue results after 2635 ms
base-2 INDEX-READY #2 catalogue results after 2541 ms
base-2 INDEX-READY #3 catalogue results after 2525 ms
START cold base-2        n=10 min=1327ms median=1544ms max=2016ms  all=[1345, 1560, 1675, 1653, 1492, 1428, 1594, 1527, 1327, 2016]  states=['COLD']
START warm base-2        n=10 min=234ms median=354ms max=737ms  all=[380, 650, 518, 257, 578, 321, 737, 327, 234, 309]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2685 ms
new-2 INDEX-READY #1 catalogue results after 2589 ms
new-2 INDEX-READY #2 catalogue results after 2722 ms
new-2 INDEX-READY #3 catalogue results after 3104 ms
START cold new-2         n=10 min=1451ms median=1626ms max=2204ms  all=[1606, 1495, 1543, 1649, 1645, 2204, 1671, 1451, 1468, 1684]  states=['COLD']
START warm new-2         n=10 min=258ms median=507ms max=718ms  all=[371, 451, 571, 451, 599, 563, 571, 344, 258, 718]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.28    n=20 min=1390ms median=1541ms max=2204ms  all=[1442, 1904, 1466, 1539, 1880, 1434, 1395, 1473, 1762, 1390, 1606, 1495, 1543, 1649, 1645, 2204, 1671, 1451, 1468, 1684]  states=['COLD']
START cold BASE 1.2.27   n=20 min=1326ms median=1563ms max=2044ms  all=[1879, 1741, 1591, 1326, 1600, 1466, 1484, 1538, 2044, 1566, 1345, 1560, 1675, 1653, 1492, 1428, 1594, 1527, 1327, 2016]  states=['COLD']
START warm NEW 1.2.28    n=20 min=258ms median=466ms max=731ms  all=[289, 731, 431, 344, 543, 674, 295, 610, 308, 482, 371, 451, 571, 451, 599, 563, 571, 344, 258, 718]  states=['HOT']
START warm BASE 1.2.27   n=20 min=234ms median=491ms max=863ms  all=[484, 655, 571, 535, 337, 467, 484, 577, 498, 863, 380, 650, 518, 257, 578, 321, 737, 327, 234, 309]  states=['HOT']
START catalogue first-install new n=2 min=2628ms median=2656ms max=2685ms  all=[2628, 2685]  states=[]
START catalogue index-ready new n=6 min=2589ms median=2732ms max=3104ms  all=[2694, 2742, 2976, 2589, 2722, 3104]  states=[]
START catalogue first-install base n=2 min=2594ms median=2740ms max=2885ms  all=[2594, 2885]  states=[]
START catalogue index-ready base n=6 min=2525ms median=2588ms max=2903ms  all=[2775, 2903, 2540, 2635, 2541, 2525]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5416', '6413', '6805', '7405', '7774', '7947', '8090', '8278', '8337', '8395', '8448', '8518', '8570', '8629', '9076', '9351', '9489', '9633', '9778', '9850', '9907', '9962', '10211', '10307', '10357', '10423', '10503', '10555', '10937', '11204', '11360', '11500', '11646', '11712', '11764', '11817', '11868', '11919', '11972', '12022', '12074', '12169', '12680', '13086', '13226', '13367', '13517', '13583', '13634', '13685', '13736', '13787', '13838', '13892', '13941', '13990', '14390', '14651', '14795', '14939', '15125', '15227', '15292', '15348', '15414', '15466', '15520', '15572', '15624', '15675']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 87
   skipped-frames events=87 max=87 total=3543
== Davey: 69
   davey events=69 max=1784ms median=902ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): base-0 new-1 base-1 new-2 base-2
## AB base-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-0: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-0 FIRST-INSTALL catalogue results after 3261 ms
base-0 INDEX-READY #1 catalogue results after 3603 ms
base-0 INDEX-READY #2 catalogue results after 2663 ms
base-0 INDEX-READY #3 catalogue results after 2674 ms
START cold base-0        n=10 min=1081ms median=1408ms max=1819ms  all=[1819, 1081, 1541, 1295, 1808, 1563, 1309, 1318, 1449, 1366]  states=['COLD']
START warm base-0        n=10 min=255ms median=504ms max=685ms  all=[306, 483, 255, 290, 524, 385, 539, 543, 604, 685]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2549 ms
new-1 INDEX-READY #1 catalogue results after 2545 ms
new-1 INDEX-READY #2 catalogue results after 2720 ms
new-1 INDEX-READY #3 catalogue results after 2455 ms
START cold new-1         n=10 min=1160ms median=1406ms max=1784ms  all=[1560, 1250, 1548, 1188, 1563, 1265, 1784, 1160, 1685, 1252]  states=['COLD']
START warm new-1         n=10 min=138ms median=440ms max=616ms  all=[499, 312, 616, 138, 455, 558, 361, 425, 596, 389]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2474 ms
base-1 INDEX-READY #1 catalogue results after 2648 ms
base-1 INDEX-READY #2 catalogue results after 2783 ms
base-1 INDEX-READY #3 catalogue results after 2476 ms
START cold base-1        n=10 min=1150ms median=1360ms max=1987ms  all=[1150, 1185, 1595, 1344, 1375, 1847, 1271, 1412, 1987, 1296]  states=['COLD']
START warm base-1        n=10 min=260ms median=422ms max=731ms  all=[468, 385, 555, 260, 336, 494, 459, 353, 326, 731]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2621 ms
new-2 INDEX-READY #1 catalogue results after 2606 ms
new-2 INDEX-READY #2 catalogue results after 2460 ms
new-2 INDEX-READY #3 catalogue results after 2646 ms
START cold new-2         n=10 min=1137ms median=1419ms max=1602ms  all=[1266, 1602, 1476, 1297, 1559, 1395, 1552, 1281, 1443, 1137]  states=['COLD']
START warm new-2         n=10 min=231ms median=372ms max=611ms  all=[602, 611, 419, 605, 379, 365, 284, 307, 250, 231]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2458 ms
base-2 INDEX-READY #1 catalogue results after 2413 ms
base-2 INDEX-READY #2 catalogue results after 2358 ms
base-2 INDEX-READY #3 catalogue results after 2463 ms
START cold base-2        n=10 min=1124ms median=1339ms max=2123ms  all=[1318, 1124, 1255, 1541, 1253, 1505, 1174, 1511, 2123, 1360]  states=['COLD']
START warm base-2        n=10 min=213ms median=395ms max=845ms  all=[613, 845, 412, 598, 471, 258, 378, 213, 317, 239]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.28    n=20 min=1137ms median=1419ms max=1784ms  all=[1560, 1250, 1548, 1188, 1563, 1265, 1784, 1160, 1685, 1252, 1266, 1602, 1476, 1297, 1559, 1395, 1552, 1281, 1443, 1137]  states=['COLD']
START cold BASE 1.2.27   n=20 min=1124ms median=1352ms max=2123ms  all=[1150, 1185, 1595, 1344, 1375, 1847, 1271, 1412, 1987, 1296, 1318, 1124, 1255, 1541, 1253, 1505, 1174, 1511, 2123, 1360]  states=['COLD']
START warm NEW 1.2.28    n=20 min=138ms median=404ms max=616ms  all=[499, 312, 616, 138, 455, 558, 361, 425, 596, 389, 602, 611, 419, 605, 379, 365, 284, 307, 250, 231]  states=['HOT']
START warm BASE 1.2.27   n=20 min=213ms median=398ms max=845ms  all=[468, 385, 555, 260, 336, 494, 459, 353, 326, 731, 613, 845, 412, 598, 471, 258, 378, 213, 317, 239]  states=['HOT']
START catalogue first-install new n=2 min=2549ms median=2585ms max=2621ms  all=[2549, 2621]  states=[]
START catalogue index-ready new n=6 min=2455ms median=2576ms max=2720ms  all=[2545, 2720, 2455, 2606, 2460, 2646]  states=[]
START catalogue first-install base n=2 min=2458ms median=2466ms max=2474ms  all=[2474, 2458]  states=[]
START catalogue index-ready base n=6 min=2358ms median=2470ms max=2783ms  all=[2648, 2783, 2476, 2413, 2358, 2463]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5295', '6301', '6989', '7301', '7715', '7785', '7864', '7994', '8070', '8122', '8174', '8224', '8276', '8325', '8766', '9061', '9204', '9350', '9493', '9568', '9620', '9689', '9745', '9796', '9849', '9903', '10130', '10204', '10594', '10843', '10986', '11143', '11286', '11355', '11404', '11456', '11505', '11559', '11610', '11660', '11721', '11776', '12293', '12610', '12755', '12893', '13041', '13104', '13155', '13208', '13262', '13315', '13367', '13417', '13467', '13524', '13915', '14182', '14327', '14466', '14606', '14675', '14725', '14775', '14860', '14951', '15002', '15069', '15121', '15173']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 51
   skipped-frames events=51 max=80 total=2011
== Davey: 29
   davey events=29 max=1273ms median=771ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-3

```
part abstart-3; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-0 FIRST-INSTALL catalogue results after 3242 ms
new-0 INDEX-READY #1 catalogue results after 3311 ms
new-0 INDEX-READY #2 catalogue results after 2682 ms
new-0 INDEX-READY #3 catalogue results after 2361 ms
START cold new-0         n=10 min=1180ms median=1682ms max=2209ms  all=[1861, 2209, 1750, 1180, 1521, 1615, 1304, 1803, 1839, 1549]  states=['COLD']
START warm new-0         n=10 min=172ms median=340ms max=458ms  all=[383, 391, 351, 330, 235, 211, 399, 172, 458, 214]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2456 ms
base-1 INDEX-READY #1 catalogue results after 2721 ms
base-1 INDEX-READY #2 catalogue results after 2715 ms
base-1 INDEX-READY #3 catalogue results after 2531 ms
START cold base-1        n=10 min=1111ms median=1380ms max=1964ms  all=[1156, 1541, 1445, 1436, 1373, 1368, 1964, 1209, 1111, 1386]  states=['COLD']
START warm base-1        n=10 min=248ms median=356ms max=651ms  all=[473, 387, 248, 417, 255, 324, 318, 651, 291, 497]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2480 ms
new-1 INDEX-READY #1 catalogue results after 2816 ms
new-1 INDEX-READY #2 catalogue results after 2579 ms
new-1 INDEX-READY #3 catalogue results after 2459 ms
START cold new-1         n=10 min=1095ms median=1384ms max=1577ms  all=[1184, 1552, 1317, 1457, 1197, 1541, 1452, 1095, 1577, 1207]  states=['COLD']
START warm new-1         n=10 min=226ms median=303ms max=656ms  all=[389, 656, 470, 296, 291, 310, 388, 226, 248, 292]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2374 ms
base-2 INDEX-READY #1 catalogue results after 2411 ms
base-2 INDEX-READY #2 catalogue results after 2642 ms
base-2 INDEX-READY #3 catalogue results after 2513 ms
START cold base-2        n=10 min=1024ms median=1389ms max=1552ms  all=[1443, 1552, 1241, 1200, 1463, 1024, 1359, 1170, 1419, 1514]  states=['COLD']
START warm base-2        n=10 min=294ms median=460ms max=500ms  all=[473, 467, 469, 480, 332, 436, 391, 452, 500, 294]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2534 ms
new-2 INDEX-READY #1 catalogue results after 2472 ms
new-2 INDEX-READY #2 catalogue results after 2504 ms
new-2 INDEX-READY #3 catalogue results after 2454 ms
START cold new-2         n=10 min=1129ms median=1322ms max=1596ms  all=[1339, 1305, 1277, 1596, 1403, 1129, 1397, 1393, 1290, 1238]  states=['COLD']
START warm new-2         n=10 min=243ms median=378ms max=625ms  all=[403, 369, 286, 444, 243, 497, 388, 625, 362, 246]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.28    n=20 min=1095ms median=1328ms max=1596ms  all=[1184, 1552, 1317, 1457, 1197, 1541, 1452, 1095, 1577, 1207, 1339, 1305, 1277, 1596, 1403, 1129, 1397, 1393, 1290, 1238]  states=['COLD']
START cold BASE 1.2.27   n=20 min=1024ms median=1380ms max=1964ms  all=[1156, 1541, 1445, 1436, 1373, 1368, 1964, 1209, 1111, 1386, 1443, 1552, 1241, 1200, 1463, 1024, 1359, 1170, 1419, 1514]  states=['COLD']
START warm NEW 1.2.28    n=20 min=226ms median=366ms max=656ms  all=[389, 656, 470, 296, 291, 310, 388, 226, 248, 292, 403, 369, 286, 444, 243, 497, 388, 625, 362, 246]  states=['HOT']
START warm BASE 1.2.27   n=20 min=248ms median=426ms max=651ms  all=[473, 387, 248, 417, 255, 324, 318, 651, 291, 497, 473, 467, 469, 480, 332, 436, 391, 452, 500, 294]  states=['HOT']
START catalogue first-install new n=2 min=2480ms median=2507ms max=2534ms  all=[2480, 2534]  states=[]
START catalogue index-ready new n=6 min=2454ms median=2488ms max=2816ms  all=[2816, 2579, 2459, 2472, 2504, 2454]  states=[]
START catalogue first-install base n=2 min=2374ms median=2415ms max=2456ms  all=[2456, 2374]  states=[]
START catalogue index-ready base n=6 min=2411ms median=2586ms max=2721ms  all=[2721, 2715, 2531, 2411, 2642, 2513]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5303', '6362', '6866', '7417', '7912', '8131', '8324', '8375', '8459', '8511', '8571', '8644', '8703', '8754', '9163', '9453', '9607', '9752', '9890', '9962', '10013', '10069', '10129', '10178', '10367', '10435', '10485', '10543', '10941', '11203', '11354', '11496', '11653', '11724', '11777', '11829', '11883', '11934', '11987', '12038', '12091', '12143', '12669', '12977', '13119', '13267', '13443', '13512', '13561', '13627', '13682', '13733', '13783', '13834', '13885', '13936', '14349', '14615', '14756', '14897', '15039', '15110', '15163', '15217', '15272', '15324', '15443', '15503', '15555', '15607']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 49
   skipped-frames events=49 max=68 total=1966
== Davey: 36
   davey events=36 max=1544ms median=904ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-4

```
part abstart-4; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): base-0 new-1 base-1 new-2 base-2
## AB base-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-0: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-0 FIRST-INSTALL catalogue results after 3507 ms
base-0 INDEX-READY #1 catalogue results after 2933 ms
base-0 INDEX-READY #2 catalogue results after 3018 ms
base-0 INDEX-READY #3 catalogue results after 2318 ms
START cold base-0        n=10 min=1126ms median=1361ms max=1636ms  all=[1358, 1518, 1262, 1636, 1364, 1560, 1253, 1399, 1126, 1268]  states=['COLD']
START warm base-0        n=10 min=275ms median=396ms max=859ms  all=[595, 859, 341, 389, 275, 403, 540, 372, 426, 307]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2620 ms
new-1 INDEX-READY #1 catalogue results after 2409 ms
new-1 INDEX-READY #2 catalogue results after 2487 ms
new-1 INDEX-READY #3 catalogue results after 2778 ms
START cold new-1         n=10 min=1088ms median=1268ms max=1557ms  all=[1557, 1224, 1251, 1321, 1276, 1509, 1458, 1259, 1128, 1088]  states=['COLD']
START warm new-1         n=10 min=362ms median=509ms max=593ms  all=[569, 419, 482, 593, 546, 419, 536, 541, 374, 362]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2541 ms
base-1 INDEX-READY #1 catalogue results after 2427 ms
base-1 INDEX-READY #2 catalogue results after 2495 ms
base-1 INDEX-READY #3 catalogue results after 2741 ms
START cold base-1        n=10 min=1053ms median=1338ms max=1533ms  all=[1407, 1533, 1352, 1421, 1323, 1218, 1508, 1053, 1080, 1267]  states=['COLD']
START warm base-1        n=10 min=127ms median=382ms max=524ms  all=[447, 356, 311, 231, 507, 524, 410, 222, 127, 407]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2476 ms
new-2 INDEX-READY #1 catalogue results after 2439 ms
new-2 INDEX-READY #2 catalogue results after 2645 ms
new-2 INDEX-READY #3 catalogue results after 2626 ms
START cold new-2         n=10 min=1140ms median=1333ms max=1706ms  all=[1312, 1706, 1183, 1196, 1434, 1140, 1412, 1230, 1354, 1388]  states=['COLD']
START warm new-2         n=10 min=316ms median=414ms max=507ms  all=[341, 473, 505, 385, 464, 443, 334, 316, 507, 386]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2566 ms
base-2 INDEX-READY #1 catalogue results after 2441 ms
base-2 INDEX-READY #2 catalogue results after 2722 ms
base-2 INDEX-READY #3 catalogue results after 2717 ms
START cold base-2        n=10 min=1077ms median=1324ms max=1455ms  all=[1416, 1255, 1396, 1370, 1093, 1408, 1455, 1277, 1161, 1077]  states=['COLD']
START warm base-2        n=10 min=202ms median=374ms max=531ms  all=[280, 379, 519, 202, 372, 368, 531, 393, 375, 336]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.28    n=20 min=1088ms median=1294ms max=1706ms  all=[1557, 1224, 1251, 1321, 1276, 1509, 1458, 1259, 1128, 1088, 1312, 1706, 1183, 1196, 1434, 1140, 1412, 1230, 1354, 1388]  states=['COLD']
START cold BASE 1.2.27   n=20 min=1053ms median=1338ms max=1533ms  all=[1407, 1533, 1352, 1421, 1323, 1218, 1508, 1053, 1080, 1267, 1416, 1255, 1396, 1370, 1093, 1408, 1455, 1277, 1161, 1077]  states=['COLD']
START warm NEW 1.2.28    n=20 min=316ms median=454ms max=593ms  all=[569, 419, 482, 593, 546, 419, 536, 541, 374, 362, 341, 473, 505, 385, 464, 443, 334, 316, 507, 386]  states=['HOT']
START warm BASE 1.2.27   n=20 min=127ms median=374ms max=531ms  all=[447, 356, 311, 231, 507, 524, 410, 222, 127, 407, 280, 379, 519, 202, 372, 368, 531, 393, 375, 336]  states=['HOT']
START catalogue first-install new n=2 min=2476ms median=2548ms max=2620ms  all=[2620, 2476]  states=[]
START catalogue index-ready new n=6 min=2409ms median=2556ms max=2778ms  all=[2409, 2487, 2778, 2439, 2645, 2626]  states=[]
START catalogue first-install base n=2 min=2541ms median=2554ms max=2566ms  all=[2541, 2566]  states=[]
START catalogue index-ready base n=6 min=2427ms median=2606ms max=2741ms  all=[2427, 2495, 2741, 2441, 2722, 2717]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5077', '5983', '6632', '6969', '7148', '7211', '7267', '7327', '7396', '7459', '7509', '7568', '7619', '7668', '8108', '8602', '8764', '8911', '9059', '9141', '9197', '9268', '9325', '9376', '9429', '9480', '9532', '9583', '10003', '10252', '10399', '10539', '10687', '10793', '10845', '10895', '10962', '11017', '11082', '11133', '11206', '11257', '11809', '12163', '12303', '12443', '12593', '12658', '12712', '12763', '12816', '12872', '12923', '12975', '13026', '13077', '13488', '13742', '13879', '14020', '14167', '14230', '14279', '14329', '14382', '14432', '14483', '14534', '14584', '14635']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 38
   skipped-frames events=38 max=58 total=1528
== Davey: 34
   davey events=34 max=1149ms median=874ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func

```
part func; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3351 ms
## H. Home SearchBar: tap, '12', open a result, back (key)
SEARCHBAR collapsed: field @ 503 393; scanner icon yes
SEARCHBAR tapped: expanded=yes query-12=no hint=yes tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR '12': text="12" content-desc="Лента за търсене" content-desc="Назад" content-desc="Изчисти" text="Всички" text="Книги" text="Новини" text="Събития" text="Страници" text="Книги" text="Спомен за здрача" text="Веска Пенчева" text="1972 · графично издание · за възрастни" text="Не е на рафта" text="12 -те вятъ
SEARCHBAR result opened: back=yes bar-gone=yes; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" 
SEARCHBAR Back from the result: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR Back key on the expanded bar: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
SEARCHBAR 2nd Back key: expanded=no query-12=yes hint=no tabbar=yes app-in-front=yes IME mInputShown=false
## H2. Back GESTURE from the expanded bar (gesture navigation; API 33)
gesture nav:  navigation_mode=2
SEARCHBAR expanded again: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR IME hidden: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
SEARCHBAR after back gesture #1: expanded=no query-12=yes hint=no tabbar=yes app-in-front=yes IME mInputShown=false
SEARCHBAR after a CANCELLED back gesture (should stay expanded): expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
## H3. Process death while the search is expanded with '12'
PD-SEARCH before: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
PD-SEARCH killed: pid ''
PD-SEARCH restored from Recents: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true; text="12" content-desc="Лента за търсене" content-desc="Назад" content-desc="Изчисти" text="Всички" text="Книги" text="Новини" text="Събития" 
PD-SEARCH Back after restore: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
## CM. Sign-in screen; Credential Manager; Google Play services / password manager on the image
image: sdk_gphone64_x86_64 google/sdk_gphone64_x86_64/emu64x:13/TE1A.240213.009/12342917:userdebug/dev-keys
GMS: versionName=23.18.18(190800-535401451) ; Play Store: 1 ; GSF: 1
autofill service: 'com.google.android.gms/.autofill.service.AutofillService'; credential_service: 'null'; credential_service_primary: 'null'
credential providers (dumpsys credential): Can't find service: credential 
CredentialProviderService implementations: 1 services found:   Service #0:     priority=0 preferredOrder=0 match=0x108000 specificIndex=-1 isDefault=false     com.google.android.gms/.auth.api.credentials.credman.service.PasswordAndPasskeyService 
Google accounts on the device: 0
LOGIN +1s: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN +4s: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN +8s: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN logcat (credentials/crash): 
LOGIN card field typed (not submitted: no test account, and a made-up card would hit the real library API): 1 field(s) show it; app in front yes
LOGIN after Back: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="12" 
app pid at the end of CM: '8558'
## COMPLETE
## logcat findings
app pids seen: ['4616', '5549', '7585', '7919', '8311', '8558']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 38
   skipped-frames events=38 max=61 total=1570
== Davey: 87
   davey events=87 max=1640ms median=931ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## homey

```
part homey; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
host tools: /usr/bin/ffmpeg
PIL 12.1.1 numpy 2.3.5
## Y-new. Home: y of every section during the first 5 s of a cold start (tall 1080x5600 screen)
installed new: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue after 3483 ms
HOMEY new-cached-1: 94 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=89 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=782 NEWS=1148 SHELF1:Най търсени=945
HOMEY new-cached-1 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.0s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.2s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.4s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.6s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.2s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.4s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.6s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 4.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.8s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.2s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945
HOMEY new-cached-1 summary (frame px; x4 on screen): SEARCH: first 2.0s y=89, final 89, jumps=0; ACTIONS: first 2.0s y=377, final 380, jumps=1 [(4.0, '+3')]; EVENTS: first 2.0s y=433, final 436, jumps=1 [(4.0, '+3')]; NEW: first 4.0s y=571, final 571, jumps=0; FORYOU: first 4.7s y=782, final 782, jumps=0; NEWS: first 4.0s y=1148, final 1148, jumps=0; SHELF1:Най търсени: first 4.0s y=945, final 945, jumps=0
HOMEY new-cached-1 LARGEST JUMP after a section first appeared: 3 frame px = 12 screen px
HOMEY new-cached-2: 77 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=89 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=782 NEWS=1148 SHELF1:Най търсени=945
HOMEY new-cached-2 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=57,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=57,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=57,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=57,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=57,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=57,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=69,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=57,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=58,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=1371,FORYOU=32,NEWS=32,SHELF1=32 | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 2.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 2.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=752,FORYOU=20,NEWS=20,SHELF1=20 | 2.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 2.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=20,FORYOU=32,NEWS=32,SHELF1=32 | 3.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=120,NEWS=120,SHELF1=120 | 3.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=570,FORYOU=120,NEWS=120,SHELF1=120 | 4.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=120,NEWS=120,SHELF1=120 | 4.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=120,NEWS=120,SHELF1=120
HOMEY new-cached-2 summary (frame px; x4 on screen): SEARCH: first 3.9s y=89, final 89, jumps=0; ACTIONS: first 3.9s y=380, final 380, jumps=0; EVENTS: first 3.9s y=436, final 436, jumps=0; NEW: first 0.0s y=57, final 571, jumps=6 [(0.4, '+12'), (0.5, '-12'), (1.1, '+1313'), (1.2, '-619'), (2.1, '-732')]; FORYOU: first 1.1s y=32, final 120, jumps=3 [(1.2, '-12'), (2.1, '+12'), (3.9, '+88')]; NEWS: first 1.1s y=32, final 120, jumps=3 [(1.2, '-12'), (2.1, '+12'), (3.9, '+88')]; SHELF1:Най търсени: first 1.1s y=32, final 120, jumps=3 [(1.2, '-12'), (2.1, '+12'), (3.9, '+88')]
HOMEY new-cached-2 LARGEST JUMP after a section first appeared: 1313 frame px = 5252 screen px
HOMEY new-cached-3: 98 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=89 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=782 NEWS=1148 SHELF1:Най търсени=945
HOMEY new-cached-3 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.0s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.2s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.4s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.6s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.0s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.2s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.4s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.6s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.8s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.2s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.4s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 5.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945
HOMEY new-cached-3 summary (frame px; x4 on screen): SEARCH: first 2.6s y=89, final 89, jumps=0; ACTIONS: first 2.6s y=377, final 380, jumps=1 [(4.5, '+3')]; EVENTS: first 2.6s y=433, final 436, jumps=1 [(4.5, '+3')]; NEW: first 4.5s y=571, final 571, jumps=0; FORYOU: first 4.5s y=782, final 782, jumps=0; NEWS: first 4.5s y=1148, final 1148, jumps=0; SHELF1:Най търсени: first 4.5s y=945, final 945, jumps=0
HOMEY new-cached-3 LARGEST JUMP after a section first appeared: 3 frame px = 12 screen px
HOMEY new-clearcache-1: 84 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=89 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=782 NEWS=1148 SHELF1:Най търсени=945
HOMEY new-clearcache-1 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=985,SHELF1=782 | 3.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=985,SHELF1=782 | 3.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=985,SHELF1=782 | 3.2s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=985,SHELF1=782 | 3.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=985,SHELF1=782 | 3.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.4s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.6s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.8s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 3.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 3.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.2s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.4s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945
HOMEY new-clearcache-1 summary (frame px; x4 on screen): SEARCH: first 1.7s y=89, final 89, jumps=0; ACTIONS: first 1.7s y=377, final 380, jumps=1 [(3.0, '+3')]; EVENTS: first 1.7s y=433, final 436, jumps=1 [(3.0, '+3')]; NEW: first 3.0s y=571, final 571, jumps=0; FORYOU: first 3.8s y=782, final 782, jumps=0; NEWS: first 3.0s y=985, final 1148, jumps=1 [(3.3, '+163')]; SHELF1:Най търсени: first 3.0s y=782, final 945, jumps=1 [(3.3, '+163')]
HOMEY new-clearcache-1 LARGEST JUMP after a section first appeared: 163 frame px = 652 screen px
HOMEY new-clearcache-2: 86 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=89 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=782 NEWS=1148 SHELF1:Най търсени=945
HOMEY new-clearcache-2 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=89,ACTIONS=377,EVENTS=433,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 2.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.2s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.4s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1148,SHELF1=945 | 3.6s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 3.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 3.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 3.8s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 3.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 3.9s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.0s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.1s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.2s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.3s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.4s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.5s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.6s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945 | 4.7s SEARCH=89,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=782,NEWS=1148,SHELF1=945
HOMEY new-clearcache-2 summary (frame px; x4 on screen): SEARCH: first 1.8s y=89, final 89, jumps=0; ACTIONS: first 1.8s y=377, final 380, jumps=1 [(2.9, '+3')]; EVENTS: first 1.8s y=433, final 436, jumps=1 [(2.9, '+3')]; NEW: first 2.9s y=571, final 571, jumps=0; FORYOU: first 3.6s y=782, final 782, jumps=0; NEWS: first 2.9s y=1148, final 1148, jumps=0; SHELF1:Най търсени: first 2.9s y=945, final 945, jumps=0
HOMEY new-clearcache-2 LARGEST JUMP after a section first appeared: 3 frame px = 12 screen px
## Y-new. Normal screen: 5 s recording at 1080x2400 (what the reader sees)
normal-screen frames every 0.5 s: 10
## Y-base. Home: y of every section during the first 5 s of a cold start (tall 1080x5600 screen)
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue after 2706 ms
HOMEY base-cached-1: 90 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=90 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=781 NEWS=1147 SHELF1:Най търсени=945
HOMEY base-cached-1 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.0s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.2s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.3s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.3s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.4s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.5s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.5s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.6s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.8s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.0s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.2s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.4s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.6s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.8s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945
HOMEY base-cached-1 summary (frame px; x4 on screen): SEARCH: first 1.6s y=90, final 90, jumps=0; ACTIONS: first 4.3s y=380, final 380, jumps=0; EVENTS: first 3.0s y=408, final 436, jumps=1 [(4.3, '+28')]; NEW: first 3.0s y=543, final 571, jumps=1 [(4.3, '+28')]; FORYOU: first 3.8s y=753, final 781, jumps=1 [(4.6, '+28')]; NEWS: first 3.0s y=1119, final 1147, jumps=1 [(4.3, '+28')]; SHELF1:Най търсени: first 3.0s y=916, final 945, jumps=1 [(4.3, '+29')]
HOMEY base-cached-1 LARGEST JUMP after a section first appeared: 29 frame px = 116 screen px
HOMEY base-cached-2: 83 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=90 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=781 NEWS=1147 SHELF1:Най търсени=945
HOMEY base-cached-2 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.0s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.1s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.1s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.2s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.3s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.4s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.6s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.8s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.0s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.1s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.1s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.2s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.4s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945
HOMEY base-cached-2 summary (frame px; x4 on screen): SEARCH: first 1.9s y=90, final 90, jumps=0; ACTIONS: first 1.9s y=377, final 380, jumps=1 [(3.3, '+3')]; EVENTS: first 3.3s y=436, final 436, jumps=0; NEW: first 3.3s y=571, final 571, jumps=0; FORYOU: first 3.3s y=781, final 781, jumps=0; NEWS: first 3.3s y=1147, final 1147, jumps=0; SHELF1:Най търсени: first 3.3s y=945, final 945, jumps=0
HOMEY base-cached-2 LARGEST JUMP after a section first appeared: 3 frame px = 12 screen px
HOMEY base-cached-3: 90 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=90 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=781 NEWS=1147 SHELF1:Най търсени=945
HOMEY base-cached-3 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 2.6s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 2.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 2.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 2.8s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 2.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 2.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.0s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.2s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.3s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.3s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.4s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.5s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.5s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.6s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.8s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 3.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.0s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=753,NEWS=1119,SHELF1=916 | 4.1s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.2s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.4s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=753,NEWS=1147,SHELF1=945 | 4.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.6s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.8s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945
HOMEY base-cached-3 summary (frame px; x4 on screen): SEARCH: first 1.5s y=90, final 90, jumps=0; ACTIONS: first 4.1s y=380, final 380, jumps=0; EVENTS: first 2.5s y=408, final 436, jumps=1 [(4.1, '+28')]; NEW: first 2.5s y=543, final 571, jumps=1 [(4.1, '+28')]; FORYOU: first 3.3s y=753, final 781, jumps=1 [(4.5, '+28')]; NEWS: first 2.5s y=955, final 1147, jumps=2 [(2.8, '+164'), (4.1, '+28')]; SHELF1:Най търсени: first 2.5s y=753, final 945, jumps=2 [(2.8, '+163'), (4.1, '+29')]
HOMEY base-cached-3 LARGEST JUMP after a section first appeared: 164 frame px = 656 screen px
HOMEY base-clearcache-1: 80 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=90 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=781 NEWS=1147 SHELF1:Най търсени=945
HOMEY base-clearcache-1 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=377,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=984,SHELF1=782 | 2.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=984,SHELF1=782 | 2.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=984,SHELF1=782 | 3.0s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=984,SHELF1=782 | 3.1s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1147,SHELF1=945 | 3.1s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1147,SHELF1=945 | 3.2s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1147,SHELF1=945 | 3.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1147,SHELF1=945 | 3.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1147,SHELF1=945 | 3.4s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1147,SHELF1=945 | 3.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=-,NEWS=1147,SHELF1=945 | 3.5s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.6s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.7s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.8s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 3.9s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.0s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.1s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.1s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.2s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945 | 4.3s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945
HOMEY base-clearcache-1 summary (frame px; x4 on screen): SEARCH: first 1.5s y=90, final 90, jumps=0; ACTIONS: first 2.1s y=377, final 380, jumps=1 [(2.8, '+3')]; EVENTS: first 2.8s y=436, final 436, jumps=0; NEW: first 2.8s y=571, final 571, jumps=0; FORYOU: first 3.5s y=781, final 781, jumps=0; NEWS: first 2.8s y=984, final 1147, jumps=1 [(3.1, '+163')]; SHELF1:Най търсени: first 2.8s y=782, final 945, jumps=1 [(3.1, '+163')]
HOMEY base-clearcache-1 LARGEST JUMP after a section first appeared: 163 frame px = 652 screen px
HOMEY base-clearcache-2: 79 frames @ 15 fps; sections found in the final dump: 7; final y (frame px, x0.25 of screen): SEARCH=90 ACTIONS=380 EVENTS=436 NEW=571 FORYOU=781 NEWS=1147 SHELF1:Най търсени=945
HOMEY base-clearcache-2 y by time after am start: 0.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 0.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.0s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.1s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.2s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.3s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.4s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.5s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.6s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.7s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.8s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=-,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 1.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.0s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.1s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.2s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.3s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.4s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.5s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.6s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.7s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.8s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 2.9s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.0s SEARCH=90,ACTIONS=-,EVENTS=-,NEW=-,FORYOU=-,NEWS=-,SHELF1=- | 3.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 3.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 3.2s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 3.3s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 3.3s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=955,SHELF1=753 | 3.4s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.5s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.5s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.6s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.7s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.8s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 3.9s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 4.0s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 4.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 4.1s SEARCH=90,ACTIONS=-,EVENTS=408,NEW=543,FORYOU=-,NEWS=1119,SHELF1=916 | 4.2s SEARCH=90,ACTIONS=380,EVENTS=436,NEW=571,FORYOU=781,NEWS=1147,SHELF1=945
HOMEY base-clearcache-2 summary (frame px; x4 on screen): SEARCH: first 1.9s y=90, final 90, jumps=0; ACTIONS: first 4.2s y=380, final 380, jumps=0; EVENTS: first 3.1s y=408, final 436, jumps=1 [(4.2, '+28')]; NEW: first 3.1s y=543, final 571, jumps=1 [(4.2, '+28')]; FORYOU: first 4.2s y=781, final 781, jumps=0; NEWS: first 3.1s y=955, final 1147, jumps=2 [(3.4, '+164'), (4.2, '+28')]; SHELF1:Най търсени: first 3.1s y=753, final 945, jumps=2 [(3.4, '+163'), (4.2, '+29')]
HOMEY base-clearcache-2 LARGEST JUMP after a section first appeared: 164 frame px = 656 screen px
## Y-base. Normal screen: 5 s recording at 1080x2400 (what the reader sees)
normal-screen frames every 0.5 s: 11
## COMPLETE
## logcat findings
app pids seen: ['5790', '7410', '7911', '8191', '8369', '8536', '8726', '8941', '9263', '9430', '9575', '9723', '9897', '10091']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 37
   skipped-frames events=37 max=92 total=1767
== Davey: 67
   davey events=67 max=2300ms median=1058ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank-1

```
part jank-1; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## J-new. Install new, warm-up, 16 books in Want to read
installed new: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 2901 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-new. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX new-catalog  frames=  160  janky=159 (99.38%)  p50=200ms p90=300ms p95=350ms p99=350ms  slowUI=79 slowDraw=159 highInput=1
new-catalog: 30 swipes, 'Покажи още' pages: 1, mis-taps recovered: 0
jank new-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октом
GFX new-home     frames=   84  janky=83 (98.81%)  p50=250ms p90=400ms p95=450ms p99=650ms  slowUI=47 slowDraw=83 highInput=2
new want list: 1 rows visible
jank new-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="Дъхът на пролетта" text="Пламен Иванов" text="налична" content-desc="Още действия" text="Времето на с
GFX new-want     frames=   69  janky=68 (98.55%)  p50=200ms p90=300ms p95=350ms p99=450ms  slowUI=36 slowDraw=68 highInput=2
jank new-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX new-news     frames=   98  janky=79 (80.61%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=38 slowDraw=79 highInput=36
jank new-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX new-calendar frames=   25  janky=19 (76.00%)  p50=150ms p90=200ms p95=250ms p99=250ms  slowUI=15 slowDraw=19 highInput=10
new gallery albums shown after 2991 ms
GFX new-gallery  frames=  108  janky=92 (85.19%)  p50=150ms p90=250ms p95=300ms p99=300ms  slowUI=39 slowDraw=92 highInput=26
new album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX new-album-grid frames=   42  janky=33 (78.57%)  p50=150ms p90=200ms p95=250ms p99=300ms  slowUI=15 slowDraw=33 highInput=14
new viewer: no photo to open
MEM new-jank-end (raw) PSS=108690KB  Java=12528KB  Native=54400KB  Graphics=0KB  Views=10 Activities=1
MEM new-jank-end (trimmed) PSS=107031KB  Java=10088KB  Native=49752KB  Graphics=0KB  Views=10 Activities=1
## J-base. Install base, warm-up, 16 books in Want to read
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 3042 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-base. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX base-catalog frames=  169  janky=167 (98.82%)  p50=200ms p90=350ms p95=400ms p99=500ms  slowUI=88 slowDraw=167 highInput=3
base-catalog: 30 swipes, 'Покажи още' pages: 1, mis-taps recovered: 0
jank base-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Ст
GFX base-home    frames=   78  janky=78 (100.00%)  p50=200ms p90=450ms p95=500ms p99=750ms  slowUI=38 slowDraw=78 highInput=0
base want list: 1 rows visible
jank base-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="Дъхът на пролетта" text="Дъхът на пролетта" text="Пламен Иванов" text="налична" content-desc="Ощ
GFX base-want    frames=   91  janky=89 (97.80%)  p50=200ms p90=300ms p95=350ms p99=450ms  slowUI=54 slowDraw=89 highInput=4
jank base-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX base-news    frames=   91  janky=78 (85.71%)  p50=150ms p90=250ms p95=350ms p99=400ms  slowUI=44 slowDraw=78 highInput=23
jank base-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX base-calendar frames=   28  janky=22 (78.57%)  p50=150ms p90=250ms p95=250ms p99=300ms  slowUI=17 slowDraw=22 highInput=11
base gallery albums shown after 2844 ms
GFX base-gallery frames=  108  janky=95 (87.96%)  p50=150ms p90=250ms p95=250ms p99=300ms  slowUI=36 slowDraw=95 highInput=20
base album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX base-album-grid frames=   53  janky=44 (83.02%)  p50=150ms p90=200ms p95=250ms p99=300ms  slowUI=18 slowDraw=44 highInput=16
base viewer: no photo to open
MEM base-jank-end (raw) PSS=122888KB  Java=18736KB  Native=71688KB  Graphics=0KB  Views=10 Activities=1
MEM base-jank-end (trimmed) PSS=115247KB  Java=11320KB  Native=66428KB  Graphics=0KB  Views=10 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4908', '9081', '12011', '13685']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 12
   skipped-frames events=12 max=99 total=595
== Davey: 33
   davey events=33 max=2358ms median=821ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank-2

```
part jank-2; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## J-base. Install base, warm-up, 16 books in Want to read
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 3498 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-base. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX base-catalog frames=  170  janky=168 (98.82%)  p50=200ms p90=300ms p95=300ms p99=400ms  slowUI=74 slowDraw=168 highInput=3
base-catalog: 30 swipes, 'Покажи още' pages: 1, mis-taps recovered: 0
jank base-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Ст
GFX base-home    frames=   87  janky=84 (96.55%)  p50=250ms p90=400ms p95=450ms p99=650ms  slowUI=58 slowDraw=84 highInput=6
base want list: 1 rows visible
jank base-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я махнеш." content-desc="Дъхът на пролетта" text="Дъхът на пролетта" text="Пламен Иванов" text="налична" content-desc="Ощ
GFX base-want    frames=   87  janky=85 (97.70%)  p50=200ms p90=300ms p95=350ms p99=400ms  slowUI=47 slowDraw=85 highInput=4
jank base-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX base-news    frames=   98  janky=87 (88.78%)  p50=150ms p90=250ms p95=250ms p99=300ms  slowUI=41 slowDraw=87 highInput=17
jank base-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX base-calendar frames=  105  janky=82 (78.10%)  p50=117ms p90=200ms p95=200ms p99=250ms  slowUI=50 slowDraw=82 highInput=38
base gallery albums shown after 2515 ms
GFX base-gallery frames=  108  janky=95 (87.96%)  p50=150ms p90=250ms p95=250ms p99=300ms  slowUI=39 slowDraw=95 highInput=22
base album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX base-album-grid frames=   40  janky=35 (87.50%)  p50=101ms p90=300ms p95=300ms p99=350ms  slowUI=13 slowDraw=35 highInput=10
base viewer: no photo to open
MEM base-jank-end (raw) PSS=111345KB  Java=12820KB  Native=61884KB  Graphics=0KB  Views=10 Activities=1
MEM base-jank-end (trimmed) PSS=108215KB  Java=9952KB  Native=52452KB  Graphics=0KB  Views=10 Activities=1
## J-new. Install new, warm-up, 16 books in Want to read
installed new: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 2833 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## J-new. Jank at 540x1200 (animations ON)
jank screen 540x1200
GFX new-catalog  frames=  153  janky=145 (94.77%)  p50=250ms p90=350ms p95=400ms p99=500ms  slowUI=73 slowDraw=145 highInput=12
new-catalog: 30 swipes, 'Покажи още' pages: 0, mis-taps recovered: 0
jank new-home screen: --- screen: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октом
GFX new-home     frames=   69  janky=69 (100.00%)  p50=300ms p90=500ms p95=550ms p99=950ms  slowUI=43 slowDraw=69 highInput=0
jank new-search screen: --- screen: text="12" content-desc="Лента за търсене" content-desc="Назад" content-desc="Изчисти" text="Всички" text="Книги" text="Новини" text="Събития" 
GFX new-search-expanded frames=   51  janky=44 (86.27%)  p50=150ms p90=350ms p95=450ms p99=600ms  slowUI=17 slowDraw=44 highInput=12
new want list: 1 rows visible
jank new-want screen: --- screen: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="Дъхът на пролетта" text="Пламен Иванов" text="налична" content-desc="Още действия" text="Времето на с
GFX new-want     frames=   87  janky=85 (97.70%)  p50=200ms p90=350ms p95=350ms p99=400ms  slowUI=44 slowDraw=85 highInput=3
jank new-news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
GFX new-news     frames=   63  janky=52 (82.54%)  p50=150ms p90=200ms p95=250ms p99=350ms  slowUI=35 slowDraw=52 highInput=21
jank new-calendar screen: --- screen: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" 
GFX new-calendar frames=   21  janky=17 (80.95%)  p50=150ms p90=200ms p95=250ms p99=250ms  slowUI=12 slowDraw=17 highInput=7
new gallery albums shown after 2572 ms
GFX new-gallery  frames=  111  janky=105 (94.59%)  p50=150ms p90=250ms p95=250ms p99=250ms  slowUI=45 slowDraw=105 highInput=11
new album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сце; photos on screen 15
GFX new-album-grid frames=  105  janky=95 (90.48%)  p50=150ms p90=200ms p95=250ms p99=300ms  slowUI=41 slowDraw=95 highInput=18
new viewer: no photo to open
MEM new-jank-end (raw) PSS=128897KB  Java=26380KB  Native=70732KB  Graphics=0KB  Views=10 Activities=1
MEM new-jank-end (trimmed) PSS=112549KB  Java=12408KB  Native=64464KB  Graphics=0KB  Views=10 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5571', '8981', '11822', '13500']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 13
   skipped-frames events=13 max=76 total=583
== Davey: 30
   davey events=30 max=1830ms median=990ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jankv

```
part jankv; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## JV-new. Viewer jank at 540x1200 (animations ON)
installed new: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX new-viewer   frames=  260  janky=195 (75.00%)  p50=105ms p90=150ms p95=200ms p99=200ms  slowUI=90 slowDraw=192 highInput=113
new viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
GFX new-viewer   frames=  274  janky=207 (75.55%)  p50=97ms p90=150ms p95=150ms p99=200ms  slowUI=109 slowDraw=202 highInput=127
new viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
## JV-base. Viewer jank at 540x1200 (animations ON)
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX base-viewer  frames=  274  janky=228 (83.21%)  p50=89ms p90=150ms p95=150ms p99=150ms  slowUI=95 slowDraw=227 highInput=91
base viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
GFX base-viewer  frames=  274  janky=229 (83.58%)  p50=85ms p90=133ms p95=150ms p99=150ms  slowUI=92 slowDraw=227 highInput=84
base viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
## JV-new. Viewer jank at 540x1200 (animations ON)
installed new: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX new-viewer   frames=  278  janky=210 (75.54%)  p50=101ms p90=150ms p95=150ms p99=150ms  slowUI=101 slowDraw=199 highInput=130
new viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
GFX new-viewer   frames=  278  janky=212 (76.26%)  p50=97ms p90=150ms p95=150ms p99=150ms  slowUI=95 slowDraw=208 highInput=127
new viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
## JV-base. Viewer jank at 540x1200 (animations ON)
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX base-viewer  frames=  278  janky=213 (76.62%)  p50=101ms p90=150ms p95=150ms p99=200ms  slowUI=101 slowDraw=213 highInput=129
base viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
GFX base-viewer  frames=  278  janky=208 (74.82%)  p50=101ms p90=150ms p95=150ms p99=200ms  slowUI=85 slowDraw=207 highInput=136
base viewer after 20 swipes: text="21 / 29  ·  Празници и събори"
## COMPLETE
## logcat findings
app pids seen: ['5271', '5929', '8542', '8767', '9925', '10157', '11312', '11538']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=118 total=816
== Davey: 19
   davey events=19 max=2788ms median=1089ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look1

```
part look1; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3246 ms
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
matrix en-dark: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
matrix en-dark scanner: text="Camera access needed" text="The camera is only used to read the barcode on your phone. Nothing is recorded or sent." text="Allow camera" text="Open settings" text="Enter the number manually" con
matrix en-dark tab labels: home catalog my news more 
## L en-light
matrix en-light: 1080x2400 density 420, font_scale 1.0, night Night mode: no, locale [en-US]
matrix en-light: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
matrix en-light scanner: text="Camera access needed" text="The camera is only used to read the barcode on your phone. Nothing is recorded or sent." text="Allow camera" text="Open settings" text="Enter the number manually" con
matrix en-light tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['4418', '6596', '8855', '10515', '12027']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 22
   skipped-frames events=22 max=67 total=920
== Davey: 49
   davey events=49 max=2087ms median=879ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look2

```
part look2; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3224 ms
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
matrix landscape: news title OK; segments News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
matrix landscape scanner: text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвори настройките" text="Въведи но
matrix landscape tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['4258', '7534', '10017']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 18
   skipped-frames events=18 max=105 total=916
== Davey: 55
   davey events=55 max=1542ms median=929ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look3

```
part look3; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3338 ms
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
app pids seen: ['4959', '6726', '9497', '11338']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 31
   skipped-frames events=31 max=70 total=1318
== Davey: 70
   davey events=70 max=2077ms median=906ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## media

```
part media; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## P. Host probe: photos of album 1 (Фотодокументална изложба) on the site
PAGE /fotodokumentalna-izlozhba: 200 0.29s 112916 bytes
img tags: 35
PROBE /fotodokumentalna-izlozhba: 30 images, optimizer non-200: 0
original 404: 19 of 30 photos; first: #11 'Стопанският двор на ТКЗС в Яворец' original 404 0.10s 0KB  | w384: not opti
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2470 ms
## G1. Album with missing photos: the 'Снимката липсва' placeholder
ALBUM 1 top: cells 18; placeholders 1
ALBUM 1 scrolled 1: 'Снимката липсва' tiles on screen: 1; cells 15
ALBUM 1 scrolled 2: 'Снимката липсва' tiles on screen: 1; cells 14
ALBUM 1 scrolled 3: 'Снимката липсва' tiles on screen: 1; cells 14
## G2. Viewer on a missing photo: message and 'Опитай отново'
VIEWER missing photo: text="13 / 29  ·  Жътва"; message SHOWN; retry button SHOWN; text="Снимката не се зареди — може да липсва на сайта или да няма връзка." text="Опитай отново" content-desc="Затвори" text="13 / 29  ·  Жътва" content-desc="Сподели" 
VIEWER retry +1s: text="Снимката не се зареди — може да липсва на сайта или да няма връзка." text="Опитай отново" content-desc="Затвори" text="13 / 29  ·  Жътва" content-desc="Сподели" 
VIEWER retry +8s: message SHOWN; app in front yes
VIEWER next: text="14 / 29  ·  Харманът"; message shown
## G3. Viewer: double tap 2×, then pinch beyond 2×, pan while zoomed, reset
VIEWER: text="1 / 29  ·  Сградата на читалището в Яворец"; error absent
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
after double tap: text="1 / 29  ·  Сградата на читалището в Яворец"
after 2 pinches (from 2×: expected up to the cap, beyond 2×): text="1 / 29  ·  Сградата на читалището в Яворец"
ZOOM check: mean abs difference 2x-shot vs pinched-shot = 75.0 (≈0 = the pinch did not zoom further)
swipe while zoomed: text="1 / 29  ·  Сградата на читалището в Яворец" (same page = the swipe panned the photo)
MEM zoomed (raw)     PSS=81361KB  Java=10696KB  Native=40196KB  Graphics=0KB  Views=10 Activities=1
MEM zoomed (trimmed) PSS=82188KB  Java=8664KB  Native=38108KB  Graphics=0KB  Views=10 Activities=1
after reset double tap: text="1 / 29  ·  Сградата на читалището в Яворец"
swipe after reset: text="1 / 29  ·  Сградата на читалището в Яворец"
## C. Covers: catalogue list and books with an ISBN
Open Library covers from the host: 302/0B 404/0B 404/0B 
search Gatsby:  results
book 2184: text="Белият дома" text="Николай Симеонов" text="Налична" ; cover image nodes: 0
book 958: text="Времето на любовта" text="Ивана Йорданова" text="Налична" ; cover image nodes: 0
book 399560: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" ; cover image nodes: 0
Open Library traffic in logcat: 0
## D1. Documents: PDF download opens with its readable file name (Drive PDF viewer)
PDF handlers: 1 activities found:   Activity #0:     priority=0 preferredOrder=0 match=0x608000 specificIndex=-1 isDefault=true     com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity 
DOCUMENTS: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището
PDF online +4633 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Декларация за частни интереси.pdf" 
PDF online +7433 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Декларация за частни интереси.pdf" 
PDF online +10176 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Декларация за частни интереси.pdf" 
PDF online +12969 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Декларация за частни интереси.pdf" 
PDF online +15718 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Декларация за частни интереси.pdf" 
PDF online final +23949 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; content-desc="&#13;&#10;" content-desc="&#13;&#10;" content-desc="&#13;&#10;" text="Декларация за частни интереси.pdf" content-desc="Find" content-desc="Add to Drive" 
PDF online FILE NAME shown: text="Декларация за частни интереси.pdf"; browser URL: text="Декларация за частни интереси.pdf"
PDF online logcat (toast/download): 10-31 17:40:37.577   488   516 I ActivityTaskManager: START u0 {act=android.intent.action.VIEW dat=content://org.chyavorec.app.files/... typ=application/pdf flg=0x10000001 cmp=com.google.android.apps. 10-31 17:40:38.634   488   533 I ActivityManager: Start proc 9162:com.google.android.apps.docs:com.google.android.apps.viewer.pdflib.PdfDocumentService/u0ai9000 for service {com.google.android.apps.do 10-31 17:40:38.648  9162  9162 I DocumentService: Using CollectorTypeCC GC. 10-31 17:40:38.649  9162  9162 W DocumentService: Unexpected CPU variant for x86: x86_64. 
PDF publication +3576 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Баба Дамяна разказва.pdf" 
PDF publication +6333 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Баба Дамяна разказва.pdf" 
PDF publication +9024 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Баба Дамяна разказва.pdf" 
PDF publication +11729 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Баба Дамяна разказва.pdf" 
PDF publication +14484 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; text="Баба Дамяна разказва.pdf" 
PDF publication final +22733 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; content-desc="page 1" text="Баба Дамяна разказва.pdf" content-desc="Find" content-desc="Add to Drive" content-desc="More options" 
PDF publication FILE NAME shown: text="Баба Дамяна разказва.pdf"; browser URL: text="Баба Дамяна разказва.pdf"
PDF publication logcat (toast/download): 10-31 17:41:19.302   488  2352 I ActivityTaskManager: START u0 {act=android.intent.action.VIEW dat=content://org.chyavorec.app.files/... typ=application/pdf flg=0x10000001 cmp=com.google.android.apps. 10-31 17:41:20.147   488  2273 D CoreBackPreview: Window{15550dc u0 com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity}: Setting back callback OnBackInvokedCallbackInfo{mCal 10-31 17:41:20.311  9162  9210 D pdf_document_jni: Creating FPDF_DOCUMENT from fd: 68 10-31 17:41:20.320   488   521 I ActivityTaskManager: Displayed com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity: +990ms 
## D2. PDF download fails (offline on Documents) → message → browser
offline: airplane=1
PDF offline +6128 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; 
PDF offline +9317 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; 
PDF offline +12668 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; 
PDF offline +16212 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; 
PDF offline +18946 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; 
PDF offline final +27253 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; text="chyavorec.org" text="No internet " text="Chrome will let you know when this page is ready. Cancel" text="Dino game. A pixelated dinosaur dodges cacti and pterodactyls as it runs across a desolate landscape. When yo
PDF offline FILE NAME shown: ; browser URL: text="chyavorec.org"
PDF offline logcat (toast/download): 10-31 17:41:59.189   488   516 D CoreBackPreview: Window{a9d35fd u0 Toast}: Setting back callback OnBackInvokedCallbackInfo{mCallback=android.window.IOnBackInvokedCallback$Stub$Proxy@85a1fc0, mPriorit 10-31 17:42:01.507   488  2290 D CoreBackPreview: Window{a9d35fd u0 Toast}: Setting back callback null 10-31 17:42:01.525   488  1858 W InputManager-JNI: Input channel object 'a9d35fd Toast (client)' was disposed without first being removed with the input manager! 10-31 17:42:01.551   488  1858 W NotificationService: Toast already killed. pkg=org.chyavorec.app token=android.os.BinderProxy@d38b7db 
## D3. PDF with no PDF app at all (Drive disabled) → browser
disable Drive: Package com.google.android.apps.docs new state: disabled-user
PDF noapp +5879 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; text="File downloaded" text="f-msupd8ojtgx3-3_Ustav-2019.pdf" 
PDF noapp +8573 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; text="File downloaded" text="f-msupd8ojtgx3-3_Ustav-2019.pdf" 
PDF noapp +11292 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; text="File downloaded" text="f-msupd8ojtgx3-3_Ustav-2019.pdf" 
PDF noapp +13986 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; text="File downloaded" text="f-msupd8ojtgx3-3_Ustav-2019.pdf" 
PDF noapp +16675 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; text="File downloaded" text="f-msupd8ojtgx3-3_Ustav-2019.pdf" 
PDF noapp final +24924 ms: fg com.android.chrome/org.chromium.chrome.browser.customtabs.CustomTabActivity; text="File downloaded" text="f-msupd8ojtgx3-3_Ustav-2019.pdf" text="1.01 MB • chyavorec.org" content-desc="More options" text="Delete" text="Open" 
PDF noapp FILE NAME shown: text="f-msupd8ojtgx3-3_Ustav-2019.pdf"; browser URL: text="f-msupd8ojtgx3-3_Ustav-2019.pdf"
PDF noapp logcat (toast/download): 10-31 17:42:44.893  1657  9505 I MediaProvider: Open with lower FS for /storage/emulated/0/Download/.pending-1794073364-f-msupd8ojtgx3-3_Ustav-2019.pdf. Uid: 10125 10-31 17:42:44.983  1657  9505 I MediaProvider: Open with lower FS for /storage/emulated/0/Download/.pending-1794073364-f-msupd8ojtgx3-3_Ustav-2019.pdf. Uid: 10125 10-31 17:42:45.033  1657  9505 D MediaProvider: Moving /storage/emulated/0/Download/.pending-1794073364-f-msupd8ojtgx3-3_Ustav-2019.pdf to /storage/emulated/0/Download/f-msupd8ojtgx3-3_Ustav-2019.pdf 10-31 17:42:45.079  1657  9505 D MediaProvider: Identical paths /storage/emulated/0/Download/f-msupd8ojtgx3-3_Ustav-2019.pdf; not moving 
enable Drive: Package com.google.android.apps.docs new state: enabled
## COMPLETE
## logcat findings
app pids seen: ['6726', '7287']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=40 total=40
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem-base

```
part mem-base; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2969 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=50811KB  Java=6132KB  Native=8336KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=50274KB  Java=6560KB  Native=4668KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=98322KB  Java=11344KB  Native=36636KB  Graphics=0KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=108357KB  Java=11160KB  Native=28400KB  Graphics=0KB  Views=13 Activities=1
MEM c10 (trimmed)    PSS=106939KB  Java=10328KB  Native=27808KB  Graphics=0KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=108833KB  Java=10400KB  Native=27176KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=124481KB  Java=17620KB  Native=34808KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (trimmed)    PSS=114420KB  Java=10532KB  Native=31856KB  Graphics=0KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=113678KB  Java=10524KB  Native=31080KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=117851KB  Java=11980KB  Native=33452KB  Graphics=0KB  Views=13 Activities=1
MEM c30 (trimmed)    PSS=113973KB  Java=10500KB  Native=31084KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 6907; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=50274 c5=98322 c10=106939 c15=108833 c20=114420 c25=113678 c30=113973
LEAK TREND (cycles 10-30, least squares): +378 KB per cycle (+7.4 MB over 20 cycles)
## COMPLETE
## logcat findings
app pids seen: ['5324', '6907']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=52 total=86
== Davey: 2
   davey events=2 max=942ms median=824ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem

```
part mem; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3096 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=52823KB  Java=7124KB  Native=24912KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=59662KB  Java=8020KB  Native=24892KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=105205KB  Java=11752KB  Native=47180KB  Graphics=0KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=108889KB  Java=17300KB  Native=36236KB  Graphics=0KB  Views=22 Activities=1
MEM c10 (trimmed)    PSS=100464KB  Java=10712KB  Native=33816KB  Graphics=0KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=97143KB  Java=10564KB  Native=33812KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=105976KB  Java=15532KB  Native=37052KB  Graphics=0KB  Views=22 Activities=1
MEM c20 (trimmed)    PSS=97452KB  Java=10580KB  Native=33688KB  Graphics=0KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=104739KB  Java=10720KB  Native=39636KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=114411KB  Java=17508KB  Native=41708KB  Graphics=0KB  Views=22 Activities=1
MEM c30 (trimmed)    PSS=104419KB  Java=10604KB  Native=38664KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 7287; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=59662 c5=105205 c10=100464 c15=97143 c20=97452 c25=104739 c30=104419
LEAK TREND (cycles 10-30, least squares): +310 KB per cycle (+6.1 MB over 20 cycles)
## COMPLETE
## logcat findings
app pids seen: ['5727', '7287']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 46
   skipped-frames events=46 max=53 total=1572
== Davey: 157
   davey events=157 max=1230ms median=816ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey-base

```
part monkey-base; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4061 ms
## K. Monkey 6000 events (seed 2468, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
monkey start pid=6640
MONKEY exit=0 duration=269 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6640'; airplane=0; camera:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MEM after-monkey (raw) PSS=77336KB  Java=15080KB  Native=17456KB  Graphics=0KB  Views=30 Activities=1
MEM after-monkey (trimmed) PSS=73327KB  Java=9892KB  Native=16420KB  Graphics=0KB  Views=14 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5393', '6640']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 21
   skipped-frames events=21 max=81 total=880
== Davey: 64
   davey events=64 max=1751ms median=899ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey

```
part monkey; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3315 ms
## K. Monkey 6000 events (seed 2468, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
monkey start pid=5941
MONKEY exit=0 duration=232 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '5941'; airplane=0; camera:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MEM after-monkey (raw) PSS=78018KB  Java=9312KB  Native=15976KB  Graphics=0KB  Views=13 Activities=1
MEM after-monkey (trimmed) PSS=82791KB  Java=9164KB  Native=15716KB  Graphics=0KB  Views=13 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4646', '5941']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 13
   skipped-frames events=13 max=77 total=513
== Davey: 36
   davey events=36 max=1485ms median=850ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## O1. Online warm-up (fills the caches)
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2878 ms
online-warm home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Стахий, Амплий и др. Св. мчк Епимах" content-desc="Последни новини" text="СЪБИТИЕ"
online-warm catalogue: results (2446, 2586 ms)
online-warm catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
online-warm book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
online-warm My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
online-warm Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
online-warm news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
online-warm article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
online-warm calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
online-warm More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
online-warm gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
online-warm album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Снимката лип
online-warm contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
online-warm documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O2. Offline cold start with caches: all tabs
offline: airplane=1
offline-cached home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Стахий, Амплий и др. Св. мчк Епимах" content-desc="Последни новини" text="СЪБИТИЕ"
offline-cached catalogue: results (2446, 2398 ms)
offline-cached catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
offline-cached book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
offline-cached My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
offline-cached Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" text="Няма интернет връзка" 
offline-cached news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
offline-cached article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
offline-cached calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
offline-cached More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
offline-cached gallery: text="Няма интернет връзка. Показваме последно наличните данни." text="Последна синхронизация: Преди 2 минути" text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“"
offline-cached album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Снимката лип
offline-cached contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
offline-cached documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O3. Reconnect on Contacts (no touch), then the other screens
contacts +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Сек
contacts +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Сек
recovered home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Стахий, Амплий и др. Св. мчк Епимах" content-desc="Последни новини" text="СЪБИТИЕ"
recovered catalogue: results (2446, 2459 ms)
recovered catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
recovered book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
recovered My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
recovered Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
recovered news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
recovered article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
recovered calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
recovered More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Снимката лип
recovered contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
recovered documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O4. Empty cache, offline, guest: all tabs (MyHub)
offline: airplane=1
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
MY offline guest (empty cache): add-card shown; note shown; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Добави читателска карта" text="Не можем да проверим дали онлайн входът е включен." text="Опитай отново" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text
offline-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" text="Сканирай" text="Читателска 
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
GALLERY (empty cache) reconnect: albums shown after 7688 ms (-1 = not within 60 s); text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" 
recovered-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" content-desc="Последни новини" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." 
recovered-empty catalogue: results (2446, 2534 ms)
recovered-empty catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
recovered-empty book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
recovered-empty My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
recovered-empty Want to read: text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" 
recovered-empty news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
recovered-empty article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
recovered-empty calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
recovered-empty More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered-empty gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered-empty album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Снимката лип
recovered-empty contacts: text="НЧ „Васил Левски – 1922“" text="Изпрати имейл" text="Отвори карта" text="Отвори сайта" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица за контакт" text="Председател: Пламен Христов" text="Секретар: Даниела Цвяткова" text="Имейл" text="chital
recovered-empty documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O6. My offline as guest again (after the online start)
offline: airplane=1
MY offline guest #2: add-card MISSING; note absent; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
## COMPLETE
## logcat findings
app pids seen: ['5941', '9096', '12183', '16188']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=32 total=32
== Davey: 2
   davey events=2 max=899ms median=838ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## rot

```
part rot; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4295 ms
## R1. Rotation (6 toggles each) on Home, Catalogue '12', a book, Want to read, Calendar
ROT home before: tab bar yes; search field yes
ROT home landscape: tab bar yes; search field yes
ROTATION home: pid 6981 -> 6981 (same); after: tab bar yes; search field yes
ROT catalog before: query '12' kept; results 108
ROT catalog landscape: query '12' kept; results 108
ROTATION catalog: pid 6981 -> 6981 (same); after: query '12' kept; results 108
ROT book before: book title kept; back button yes
ROT book landscape: book title kept; back button yes
ROTATION book: pid 6981 -> 6981 (same); after: book title kept; back button yes
ROT want before: list title yes; book listed
ROT want landscape: list title yes; book listed
ROTATION want: pid 6981 -> 6981 (same); after: list title yes; book listed
ROT calendar before: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
ROT calendar landscape: News[checked=false selected=false bounds=[508,243][820,369]] Calendar[checked=true selected=false bounds=[820,243][1135,369]] subscribe-action=visible
ROTATION calendar: pid 6981 -> 6981 (same); after: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
GFX rotation     frames=  153  janky=151 (98.69%)  p50=300ms p90=700ms p95=850ms p99=1000ms  slowUI=111 slowDraw=151 highInput=3
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
app pids seen: ['4597', '6981', '9965', '10250', '10394', '10901', '11110', '11351', '11573']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 19
   skipped-frames events=19 max=71 total=803
== Davey: 33
   davey events=33 max=1779ms median=887ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## search34

```
part search34; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3310 ms
## H. Home SearchBar: tap, '12', open a result, back (key)
SEARCHBAR collapsed: field @ 503 393; scanner icon yes
SEARCHBAR tapped: expanded=yes query-12=no hint=yes tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR '12': text="12" content-desc="Лента за търсене" content-desc="Назад" content-desc="Изчисти" text="Всички" text="Книги" text="Новини" text="Събития" text="Страници" text="Книги" text="Спомен за здрача" text="Веска Пенчева" text="1972 · графично издание · за възрастни" text="Не е на рафта" text="12 -те вятъ
SEARCHBAR result opened: back=yes bar-gone=yes; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" 
SEARCHBAR Back from the result: expanded=yes query-12=no hint=no tabbar=no app-in-front=yes IME mInputShown=false
SEARCHBAR Back key on the expanded bar: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR 2nd Back key: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
## H2. Back GESTURE from the expanded bar (gesture navigation; API 34)
gesture nav:  navigation_mode=2
predictive back developer option: 1
SEARCHBAR expanded again: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR IME hidden: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
SEARCHBAR after back gesture #1: expanded=no query-12=yes hint=no tabbar=yes app-in-front=yes IME mInputShown=false
SEARCHBAR after a CANCELLED back gesture (should stay expanded): expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
## H3. Process death while the search is expanded with '12'
PD-SEARCH before: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
PD-SEARCH killed: pid ''
PD-SEARCH restored from Recents: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true; text="12" content-desc="Лента за търсене" content-desc="Назад" content-desc="Изчисти" text="Всички" text="Книги" text="Новини" text="Събития" 
PD-SEARCH Back after restore: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
## H4. API 34: predictive back — several frames during a slow gesture
gesture nav:  navigation_mode=0
SEARCHBAR after slow gesture (API 34): expanded=no query-12=yes hint=no tabbar=yes app-in-front=yes IME mInputShown=false
## H5. API 34: back gesture from a book page (predictive back to Home)
after back gesture from the book: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" 
## CM. Sign-in screen; Credential Manager; Google Play services / password manager on the image
image: sdk_gphone64_x86_64 google/sdk_gphone64_x86_64/emu64xa:14/UE1A.230829.050/12077443:userdebug/dev-keys
GMS: versionName=23.18.18(190800-535401451) ; Play Store: 1 ; GSF: 1
autofill service: 'com.google.android.gms/.autofill.service.AutofillService'; credential_service: 'com.google.android.gms/.auth.api.credentials.credman.service.PasswordAndPasskeyService'; credential_service_primary: 'com.google.android.gms/.auth.api.credentials.credman.service.PasswordAndPasskeyService'
credential providers (dumpsys credential): 
CredentialProviderService implementations: 1 services found:   Service #0:     priority=0 preferredOrder=0 match=0x108000 specificIndex=-1 isDefault=false     com.google.android.gms/.auth.api.credentials.credman.service.PasswordAndPasskeyService 
Google accounts on the device: 0
LOGIN +1s: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN +4s: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN +8s: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN logcat (credentials/crash): 10-31 17:40:41.515   527   908 I CredManSysService: starting executeGetCredential with callingPackage: org.chyavorec.app 10-31 17:40:41.530   527   908 I ProviderGetSession: Filtering request options for: ComponentInfo{com.google.android.gms/com.google.android.gms.auth.api.credentials.credman.service.PasswordAndPasskeyService} 10-31 17:40:41.530   527   908 I ProviderGetSession: Option of type: android.credentials.TYPE_PASSWORD_CREDENTIAL meets all filteringconditions 10-31 17:40:41.530   527   908 I GetRequestSession: Provider session created and being added for: ComponentInfo{com.google.android.gms/com.google.android.gms.auth.api.credentials.credman.service.PasswordAndPasskeyService 10-31 17:40:41.530   527   908 I CredManSysServiceImpl: Service does not have the required capabilities 10-31 17:40:41.530   527   908 I CredManSysServiceImpl: Service does not have the required capabilities 10-31 17:40:41.567  6231  6322 W Auth.Api.Credentials: [BaseCredentialProviderService] Operation execution failed. [CONTEXT service_id=68 ] 10-31 17:40:41.567  6231  6322 W Auth.Api.Credentials: cgi: No matching credentials. 
LOGIN card field typed (not submitted: no test account, and a made-up card would hit the real library API): 1 field(s) show it; app in front yes
LOGIN after Back: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="12" 
app pid at the end of CM: '8324'
## COMPLETE
## logcat findings
app pids seen: ['5204', '5992', '7110', '7442', '7834', '8091', '8324']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 40
   skipped-frames events=40 max=72 total=1613
== Davey: 113
   davey events=113 max=1511ms median=863ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widget

```
part widget; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3681 ms
## W1. Add the widget from the launcher's widget picker (default 4×2 = wide)
launcher: com.google.android.apps.nexuslauncher
WIDGET new long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET new picker: our row @ 437 543; size label @ 540 1451; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET new after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET new: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET grid from the 4x2 copy: [57 537 1023 1123] cell 241x293 px
## W2. Wide 4×2, signed out: content, light and dark
WIDGET 10-wide-4x2 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET 10-wide-4x2 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET signed-out: loans block hidden; event shown; no-events absent
## W3. Wide widget: clicks
WIDGET click catalog (widget_catalog @ 900 595) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 699 595) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвор
WIDGET click event (widget_event @ 540 714) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 337 595) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
## W4. Sizes: 4×3, 3×2, 2×2, 2×3 (resize handles), light and dark each
RESIZE 4x3 (4x3): bounds [57 537 1023 1437] options 
WIDGET 20-wide-tall-4x3 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1437]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET 20-wide-tall-4x3 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1437]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
RESIZE 3x2 (3x2): bounds [57 537 771 1123] options 
WIDGET 30-medium-3x2 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 771 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET 30-medium-3x2 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 771 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
RESIZE 2x2 (2x2): bounds [57 537 519 1123] options 
WIDGET 40-small-2x2 light: views from org.chyavorec.app: 1 [ids: widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_title ]; bounds [57 537 519 1123]; texts: Читалище Яворец|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET 40-small-2x2 dark: views from org.chyavorec.app: 1 [ids: widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_title ]; bounds [57 537 519 1123]; texts: Читалище Яворец|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET click small-event (widget_event @ 288 721) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click small-app (t:Chitalishte Yavorets|Читалище Яворец @ 319 588) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
RESIZE 2x3 (2x3): bounds [57 537 519 1437] options 
WIDGET 50-small-tall-2x3 light: views from org.chyavorec.app: 1 [ids: widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_title ]; bounds [57 537 519 1437]; texts: Читалище Яворец|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET 50-small-tall-2x3 dark: views from org.chyavorec.app: 1 [ids: widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_title ]; bounds [57 537 519 1437]; texts: Читалище Яворец|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
RESIZE back-4x2 (4x2): bounds [57 537 1023 1123] options 
WIDGET back to 4x2: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
## W5. After force-stop of the app process
WIDGET after force-stop: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
## COMPLETE
## logcat findings
app pids seen: ['5161']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=69 total=281
== Davey: 10
   davey events=10 max=1405ms median=936ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widgetlang

```
part widgetlang; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3151 ms
## WL1. System EN, app BG: add the widget
system locale:  / config: mcc310-mnc260-en-rUS-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33; app: [bg-BG]
WIDGET lang long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET lang picker: our row @ 437 1021; size label @ 540 1929; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET lang after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET lang: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET-LANG 01-sysEN-appBG: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|
## WL2. System EN: the app switched to English in its Settings (process kept)
APP language chosen in Settings: English; get-app-locales: [en]; pid 5614
WIDGET-LANG 02-sysEN-appEN-right-after: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|
WIDGET-LANG 03-sysEN-appEN-after-15s: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|
WIDGET-LANG 04-sysEN-appEN-after-reopen: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|
WIDGET-LANG 05-sysEN-appEN-after-restart: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|November 1, 2026|
## WL3. System BG (framework restart), app EN
SYSTEM locale -> bg-BG: 
SYSTEM locale now: persist.sys.locale=bg-BG; config: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33
WIDGET-LANG 10-sysBG-appEN-after-reboot: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|November 1, 2026|
app screen (should be EN): text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode
WIDGET-LANG 11-sysBG-appEN-after-launch: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|November 1, 2026|
## WL4. System BG, app switched to Български in Settings, then restart
APP language chosen in Settings: Български; get-app-locales: [bg]; pid 10618
WIDGET-LANG 20-sysBG-appBG-right-after: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|November 1, 2026|
WIDGET-LANG 21-sysBG-appBG-after-restart: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|
## WL5. System BG, app EN set with cmd locale (as from the system's per-app language screen), restart
WIDGET-LANG 30-sysBG-appEN-cmd-right-after: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|
WIDGET-LANG 31-sysBG-appEN-cmd-after-restart: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|November 1, 2026|
WIDGET-LANG 32-sysBG-appEN-dark: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|November 1, 2026|
SYSTEM locale -> en-US: 
SYSTEM locale now: persist.sys.locale=en-US; config: config: mcc310-mnc260-en-rUS-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33
## COMPLETE
## logcat findings
app pids seen: ['5614', '8304', '10618', '12801', '12966', '14211']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=72 total=301
== Davey: 10
   davey events=10 max=1816ms median=913ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widgetupd

```
part widgetupd; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## U0. Install v1.2.27 (Glance widget)
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3027 ms
groups: NONE
channels: events(Събития)->null; messages(Съобщения от читалището)->null; updates(Обновления)->null; news(Новини)->null; loans(Срокове за връщане)->null; library(Библиотека)->null; messages_important(Важни съобщения)->null
## U1. v1.2.27: add the widget
WIDGET base long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET base picker: our row @ 437 884; size label @ 540 1792; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET base after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET base: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET v1.2.27: views from org.chyavorec.app: 1 [ids: rootView ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
## U2. adb install -r v1.2.28 over v1.2.27 with the widget on the home screen (app not opened)
after adb install -r:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
WIDGET +3s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET +15s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET +45s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
widget ids before/after the update: [id=2 ] / [id=2 ]
## U3. The kept widget after the update: clicks
WIDGET click catalog (widget_catalog @ 900 595) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 699 595) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвор
WIDGET click event (widget_event @ 540 714) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 337 595) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
## U4. App opened after the update: channel groups; widget
groups: group_app=Приложението; group_my_library=Моята библиотека; group_centre=Читалището
channels: events(Събития)->group_centre; messages(Съобщения от читалището)->group_centre; updates(Обновления)->group_app; news(Новини)->group_centre; loans(Срокове за връщане)->group_my_library; library(Библиотека)->group_my_library; messages_important(Важни съобщения)->group_centre
SYSTEM notification settings: fg com.android.settings/com.android.settings.Settings$AppNotificationSettingsActivity; content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Chitalishte Yavorets" text="All Chitalishte Yavorets notifications" text="Приложението" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения от читалището" content-desc="Съобщения от читалището" content-desc="Важни съобщения" 
SYSTEM notification settings (scrolled): content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Приложението" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения от читалището" content-desc="Съобщения от читалището" text="Важни съобщения" content-desc="Важни съобщения" text="Новини" content-desc="Новини" text="Моята библиотека" text="Библиотека" content-desc="Библиотека" text="Срокове за връщане" content-desc="Срокове за връщане" text="Allow notification dot"
WIDGET after the app ran: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET dark after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
## COMPLETE
## logcat findings
app pids seen: ['4916', '7971', '8544']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=104 total=325
== Davey: 16
   davey events=16 max=1254ms median=786ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## zoom

```
part zoom; APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## Z-new. Viewer: double tap at 2× resets? pinch beyond 2× then double tap resets?
installed new: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4 size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
Z-new round 1: after dtap on the pinched photo + swipe: text="2 / 29  ·  Фолклорен състав при читалището"
ZOOM diff vs 1x: after dtap at 2x=0.0  pinched=87.0  after dtap on pinched=0.4  (≈0 = back at 1x)
Z-new round 2: after dtap on the pinched photo + swipe: text="2 / 29  ·  Фолклорен състав при читалището"
ZOOM diff vs 1x: after dtap at 2x=0.0  pinched=87.0  after dtap on pinched=0.4  (≈0 = back at 1x)
Z-new round 3: after dtap on the pinched photo + swipe: text="2 / 29  ·  Фолклорен състав при читалището"
ZOOM diff vs 1x: after dtap at 2x=71.4  pinched=99.8  after dtap on pinched=0.4  (≈0 = back at 1x)
## Z-base. Viewer: double tap at 2× resets? pinch beyond 2× then double tap resets?
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App ); versionCode 1484479; x86_64 libs: 4; update manifest: no-update stub size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
Z-base round 1: after dtap on the pinched photo + swipe: text="2 / 29  ·  Фолклорен състав при читалището"
ZOOM diff vs 1x: after dtap at 2x=0.0  pinched=87.0  after dtap on pinched=0.4  (≈0 = back at 1x)
Z-base round 2: after dtap on the pinched photo + swipe: text="2 / 29  ·  Фолклорен състав при читалището"
ZOOM diff vs 1x: after dtap at 2x=0.0  pinched=87.0  after dtap on pinched=0.4  (≈0 = back at 1x)
Z-base round 3: after dtap on the pinched photo + swipe: text="2 / 29  ·  Фолклорен състав при читалището"
ZOOM diff vs 1x: after dtap at 2x=71.4  pinched=99.8  after dtap on pinched=0.4  (≈0 = back at 1x)
## COMPLETE
## logcat findings
app pids seen: ['5541', '6140', '9034', '9200']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=85 total=361
== Davey: 7
   davey events=7 max=2005ms median=854ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### a11y/shots/a11y

- [a11y/shots/a11y/01-home.png](a11y/shots/a11y/01-home.png)
- [a11y/shots/a11y/02-search-expanded.png](a11y/shots/a11y/02-search-expanded.png)
- [a11y/shots/a11y/03-catalog.png](a11y/shots/a11y/03-catalog.png)
- [a11y/shots/a11y/04-book.png](a11y/shots/a11y/04-book.png)
- [a11y/shots/a11y/05-news.png](a11y/shots/a11y/05-news.png)
- [a11y/shots/a11y/06-article.png](a11y/shots/a11y/06-article.png)
- [a11y/shots/a11y/07-calendar.png](a11y/shots/a11y/07-calendar.png)
- [a11y/shots/a11y/08-my.png](a11y/shots/a11y/08-my.png)
- [a11y/shots/a11y/09-want.png](a11y/shots/a11y/09-want.png)
- [a11y/shots/a11y/10-more.png](a11y/shots/a11y/10-more.png)
- [a11y/shots/a11y/11-settings.png](a11y/shots/a11y/11-settings.png)
- [a11y/shots/a11y/12-gallery.png](a11y/shots/a11y/12-gallery.png)
- [a11y/shots/a11y/13-album.png](a11y/shots/a11y/13-album.png)
- [a11y/shots/a11y/14-viewer.png](a11y/shots/a11y/14-viewer.png)
- [a11y/shots/a11y/15-contacts.png](a11y/shots/a11y/15-contacts.png)
- [a11y/shots/a11y/16-documents.png](a11y/shots/a11y/16-documents.png)
- [a11y/shots/a11y/18-scanner-manual.png](a11y/shots/a11y/18-scanner-manual.png)
- [a11y/shots/a11y/21-home-en-font130.png](a11y/shots/a11y/21-home-en-font130.png)
- [a11y/shots/a11y/22-catalog-en-font130.png](a11y/shots/a11y/22-catalog-en-font130.png)
- [a11y/shots/a11y/23-book-en-font130.png](a11y/shots/a11y/23-book-en-font130.png)
- [a11y/shots/a11y/24-more-en-font130.png](a11y/shots/a11y/24-more-en-font130.png)

### abstart-1/shots

- [abstart-1/shots/ab-base-1.png](abstart-1/shots/ab-base-1.png)
- [abstart-1/shots/ab-base-2.png](abstart-1/shots/ab-base-2.png)
- [abstart-1/shots/ab-new-0.png](abstart-1/shots/ab-new-0.png)
- [abstart-1/shots/ab-new-1.png](abstart-1/shots/ab-new-1.png)
- [abstart-1/shots/ab-new-2.png](abstart-1/shots/ab-new-2.png)

### abstart-2/shots

- [abstart-2/shots/ab-base-0.png](abstart-2/shots/ab-base-0.png)
- [abstart-2/shots/ab-base-1.png](abstart-2/shots/ab-base-1.png)
- [abstart-2/shots/ab-base-2.png](abstart-2/shots/ab-base-2.png)
- [abstart-2/shots/ab-new-1.png](abstart-2/shots/ab-new-1.png)
- [abstart-2/shots/ab-new-2.png](abstart-2/shots/ab-new-2.png)

### abstart-3/shots

- [abstart-3/shots/ab-base-1.png](abstart-3/shots/ab-base-1.png)
- [abstart-3/shots/ab-base-2.png](abstart-3/shots/ab-base-2.png)
- [abstart-3/shots/ab-new-0.png](abstart-3/shots/ab-new-0.png)
- [abstart-3/shots/ab-new-1.png](abstart-3/shots/ab-new-1.png)
- [abstart-3/shots/ab-new-2.png](abstart-3/shots/ab-new-2.png)

### abstart-4/shots

- [abstart-4/shots/ab-base-0.png](abstart-4/shots/ab-base-0.png)
- [abstart-4/shots/ab-base-1.png](abstart-4/shots/ab-base-1.png)
- [abstart-4/shots/ab-base-2.png](abstart-4/shots/ab-base-2.png)
- [abstart-4/shots/ab-new-1.png](abstart-4/shots/ab-new-1.png)
- [abstart-4/shots/ab-new-2.png](abstart-4/shots/ab-new-2.png)

### func/shots/login

- [func/shots/login/01-after-open.png](func/shots/login/01-after-open.png)
- [func/shots/login/04-after-open.png](func/shots/login/04-after-open.png)
- [func/shots/login/08-after-open.png](func/shots/login/08-after-open.png)
- [func/shots/login/10-typed.png](func/shots/login/10-typed.png)

### func/shots/search

- [func/shots/search/00-home.png](func/shots/search/00-home.png)
- [func/shots/search/01-expanded.png](func/shots/search/01-expanded.png)
- [func/shots/search/02-typed-12.png](func/shots/search/02-typed-12.png)
- [func/shots/search/03-result.png](func/shots/search/03-result.png)
- [func/shots/search/04-back-from-result.png](func/shots/search/04-back-from-result.png)
- [func/shots/search/05-after-back-key.png](func/shots/search/05-after-back-key.png)
- [func/shots/search/10-gesture-midway.png](func/shots/search/10-gesture-midway.png)
- [func/shots/search/11-after-gesture.png](func/shots/search/11-after-gesture.png)
- [func/shots/search/14-cancel-midway.png](func/shots/search/14-cancel-midway.png)
- [func/shots/search/15-after-cancelled-gesture.png](func/shots/search/15-after-cancelled-gesture.png)
- [func/shots/search/20-before-death.png](func/shots/search/20-before-death.png)
- [func/shots/search/21-recents.png](func/shots/search/21-recents.png)
- [func/shots/search/22-restored.png](func/shots/search/22-restored.png)

### homey/homey/base-cached-1

- [homey/homey/base-cached-1/f0023.png](homey/homey/base-cached-1/f0023.png)
- [homey/homey/base-cached-1/f0031.png](homey/homey/base-cached-1/f0031.png)
- [homey/homey/base-cached-1/f0038.png](homey/homey/base-cached-1/f0038.png)
- [homey/homey/base-cached-1/f0046.png](homey/homey/base-cached-1/f0046.png)
- [homey/homey/base-cached-1/f0061.png](homey/homey/base-cached-1/f0061.png)
- [homey/homey/base-cached-1/f0090.png](homey/homey/base-cached-1/f0090.png)

### homey/homey/base-cached-2

- [homey/homey/base-cached-2/f0023.png](homey/homey/base-cached-2/f0023.png)
- [homey/homey/base-cached-2/f0031.png](homey/homey/base-cached-2/f0031.png)
- [homey/homey/base-cached-2/f0038.png](homey/homey/base-cached-2/f0038.png)
- [homey/homey/base-cached-2/f0046.png](homey/homey/base-cached-2/f0046.png)
- [homey/homey/base-cached-2/f0061.png](homey/homey/base-cached-2/f0061.png)

### homey/homey/base-cached-3

- [homey/homey/base-cached-3/f0023.png](homey/homey/base-cached-3/f0023.png)
- [homey/homey/base-cached-3/f0031.png](homey/homey/base-cached-3/f0031.png)
- [homey/homey/base-cached-3/f0038.png](homey/homey/base-cached-3/f0038.png)
- [homey/homey/base-cached-3/f0046.png](homey/homey/base-cached-3/f0046.png)
- [homey/homey/base-cached-3/f0061.png](homey/homey/base-cached-3/f0061.png)
- [homey/homey/base-cached-3/f0090.png](homey/homey/base-cached-3/f0090.png)

### homey/homey/base-clearcache-1

- [homey/homey/base-clearcache-1/f0023.png](homey/homey/base-clearcache-1/f0023.png)
- [homey/homey/base-clearcache-1/f0031.png](homey/homey/base-clearcache-1/f0031.png)
- [homey/homey/base-clearcache-1/f0038.png](homey/homey/base-clearcache-1/f0038.png)
- [homey/homey/base-clearcache-1/f0046.png](homey/homey/base-clearcache-1/f0046.png)
- [homey/homey/base-clearcache-1/f0061.png](homey/homey/base-clearcache-1/f0061.png)

### homey/homey/base-clearcache-2

- [homey/homey/base-clearcache-2/f0023.png](homey/homey/base-clearcache-2/f0023.png)
- [homey/homey/base-clearcache-2/f0031.png](homey/homey/base-clearcache-2/f0031.png)
- [homey/homey/base-clearcache-2/f0038.png](homey/homey/base-clearcache-2/f0038.png)
- [homey/homey/base-clearcache-2/f0046.png](homey/homey/base-clearcache-2/f0046.png)
- [homey/homey/base-clearcache-2/f0061.png](homey/homey/base-clearcache-2/f0061.png)

### homey/homey/base-normal

- [homey/homey/base-normal/n01.png](homey/homey/base-normal/n01.png)
- [homey/homey/base-normal/n02.png](homey/homey/base-normal/n02.png)
- [homey/homey/base-normal/n03.png](homey/homey/base-normal/n03.png)
- [homey/homey/base-normal/n04.png](homey/homey/base-normal/n04.png)
- [homey/homey/base-normal/n05.png](homey/homey/base-normal/n05.png)
- [homey/homey/base-normal/n06.png](homey/homey/base-normal/n06.png)
- [homey/homey/base-normal/n07.png](homey/homey/base-normal/n07.png)
- [homey/homey/base-normal/n08.png](homey/homey/base-normal/n08.png)
- [homey/homey/base-normal/n09.png](homey/homey/base-normal/n09.png)
- [homey/homey/base-normal/n10.png](homey/homey/base-normal/n10.png)
- [homey/homey/base-normal/n11.png](homey/homey/base-normal/n11.png)

### homey/homey/new-cached-1

- [homey/homey/new-cached-1/f0023.png](homey/homey/new-cached-1/f0023.png)
- [homey/homey/new-cached-1/f0031.png](homey/homey/new-cached-1/f0031.png)
- [homey/homey/new-cached-1/f0038.png](homey/homey/new-cached-1/f0038.png)
- [homey/homey/new-cached-1/f0046.png](homey/homey/new-cached-1/f0046.png)
- [homey/homey/new-cached-1/f0061.png](homey/homey/new-cached-1/f0061.png)
- [homey/homey/new-cached-1/f0090.png](homey/homey/new-cached-1/f0090.png)

### homey/homey/new-cached-2

- [homey/homey/new-cached-2/f0023.png](homey/homey/new-cached-2/f0023.png)
- [homey/homey/new-cached-2/f0031.png](homey/homey/new-cached-2/f0031.png)
- [homey/homey/new-cached-2/f0038.png](homey/homey/new-cached-2/f0038.png)
- [homey/homey/new-cached-2/f0046.png](homey/homey/new-cached-2/f0046.png)
- [homey/homey/new-cached-2/f0061.png](homey/homey/new-cached-2/f0061.png)

### homey/homey/new-cached-3

- [homey/homey/new-cached-3/f0023.png](homey/homey/new-cached-3/f0023.png)
- [homey/homey/new-cached-3/f0031.png](homey/homey/new-cached-3/f0031.png)
- [homey/homey/new-cached-3/f0038.png](homey/homey/new-cached-3/f0038.png)
- [homey/homey/new-cached-3/f0046.png](homey/homey/new-cached-3/f0046.png)
- [homey/homey/new-cached-3/f0061.png](homey/homey/new-cached-3/f0061.png)
- [homey/homey/new-cached-3/f0090.png](homey/homey/new-cached-3/f0090.png)

### homey/homey/new-clearcache-1

- [homey/homey/new-clearcache-1/f0023.png](homey/homey/new-clearcache-1/f0023.png)
- [homey/homey/new-clearcache-1/f0031.png](homey/homey/new-clearcache-1/f0031.png)
- [homey/homey/new-clearcache-1/f0038.png](homey/homey/new-clearcache-1/f0038.png)
- [homey/homey/new-clearcache-1/f0046.png](homey/homey/new-clearcache-1/f0046.png)
- [homey/homey/new-clearcache-1/f0061.png](homey/homey/new-clearcache-1/f0061.png)

### homey/homey/new-clearcache-2

- [homey/homey/new-clearcache-2/f0023.png](homey/homey/new-clearcache-2/f0023.png)
- [homey/homey/new-clearcache-2/f0031.png](homey/homey/new-clearcache-2/f0031.png)
- [homey/homey/new-clearcache-2/f0038.png](homey/homey/new-clearcache-2/f0038.png)
- [homey/homey/new-clearcache-2/f0046.png](homey/homey/new-clearcache-2/f0046.png)
- [homey/homey/new-clearcache-2/f0061.png](homey/homey/new-clearcache-2/f0061.png)

### homey/homey/new-normal

- [homey/homey/new-normal/n01.png](homey/homey/new-normal/n01.png)
- [homey/homey/new-normal/n02.png](homey/homey/new-normal/n02.png)
- [homey/homey/new-normal/n03.png](homey/homey/new-normal/n03.png)
- [homey/homey/new-normal/n04.png](homey/homey/new-normal/n04.png)
- [homey/homey/new-normal/n05.png](homey/homey/new-normal/n05.png)
- [homey/homey/new-normal/n06.png](homey/homey/new-normal/n06.png)
- [homey/homey/new-normal/n07.png](homey/homey/new-normal/n07.png)
- [homey/homey/new-normal/n08.png](homey/homey/new-normal/n08.png)
- [homey/homey/new-normal/n09.png](homey/homey/new-normal/n09.png)
- [homey/homey/new-normal/n10.png](homey/homey/new-normal/n10.png)

### jank-1/shots/debug

- [jank-1/shots/debug/1-viewer.png](jank-1/shots/debug/1-viewer.png)
- [jank-1/shots/debug/2-viewer.png](jank-1/shots/debug/2-viewer.png)

### jank-1/shots/jank

- [jank-1/shots/jank/base-album-grid.png](jank-1/shots/jank/base-album-grid.png)
- [jank-1/shots/jank/base-calendar.png](jank-1/shots/jank/base-calendar.png)
- [jank-1/shots/jank/base-catalog-end.png](jank-1/shots/jank/base-catalog-end.png)
- [jank-1/shots/jank/base-gallery.png](jank-1/shots/jank/base-gallery.png)
- [jank-1/shots/jank/base-home.png](jank-1/shots/jank/base-home.png)
- [jank-1/shots/jank/base-news.png](jank-1/shots/jank/base-news.png)
- [jank-1/shots/jank/base-want.png](jank-1/shots/jank/base-want.png)
- [jank-1/shots/jank/new-album-grid.png](jank-1/shots/jank/new-album-grid.png)
- [jank-1/shots/jank/new-calendar.png](jank-1/shots/jank/new-calendar.png)
- [jank-1/shots/jank/new-catalog-end.png](jank-1/shots/jank/new-catalog-end.png)
- [jank-1/shots/jank/new-gallery.png](jank-1/shots/jank/new-gallery.png)
- [jank-1/shots/jank/new-home.png](jank-1/shots/jank/new-home.png)
- [jank-1/shots/jank/new-news.png](jank-1/shots/jank/new-news.png)
- [jank-1/shots/jank/new-want.png](jank-1/shots/jank/new-want.png)

### jank-2/shots/debug

- [jank-2/shots/debug/1-viewer.png](jank-2/shots/debug/1-viewer.png)
- [jank-2/shots/debug/2-viewer.png](jank-2/shots/debug/2-viewer.png)

### jank-2/shots/jank

- [jank-2/shots/jank/base-album-grid.png](jank-2/shots/jank/base-album-grid.png)
- [jank-2/shots/jank/base-calendar.png](jank-2/shots/jank/base-calendar.png)
- [jank-2/shots/jank/base-catalog-end.png](jank-2/shots/jank/base-catalog-end.png)
- [jank-2/shots/jank/base-gallery.png](jank-2/shots/jank/base-gallery.png)
- [jank-2/shots/jank/base-home.png](jank-2/shots/jank/base-home.png)
- [jank-2/shots/jank/base-news.png](jank-2/shots/jank/base-news.png)
- [jank-2/shots/jank/base-want.png](jank-2/shots/jank/base-want.png)
- [jank-2/shots/jank/new-album-grid.png](jank-2/shots/jank/new-album-grid.png)
- [jank-2/shots/jank/new-calendar.png](jank-2/shots/jank/new-calendar.png)
- [jank-2/shots/jank/new-catalog-end.png](jank-2/shots/jank/new-catalog-end.png)
- [jank-2/shots/jank/new-gallery.png](jank-2/shots/jank/new-gallery.png)
- [jank-2/shots/jank/new-home.png](jank-2/shots/jank/new-home.png)
- [jank-2/shots/jank/new-news.png](jank-2/shots/jank/new-news.png)
- [jank-2/shots/jank/new-search-expanded.png](jank-2/shots/jank/new-search-expanded.png)
- [jank-2/shots/jank/new-want.png](jank-2/shots/jank/new-want.png)

### jankv/shots/jankv

- [jankv/shots/jankv/base-viewer-end.png](jankv/shots/jankv/base-viewer-end.png)
- [jankv/shots/jankv/base-viewer-start.png](jankv/shots/jankv/base-viewer-start.png)
- [jankv/shots/jankv/new-viewer-end.png](jankv/shots/jankv/new-viewer-end.png)
- [jankv/shots/jankv/new-viewer-start.png](jankv/shots/jankv/new-viewer-start.png)

### look1/shots/bg-dark

- [look1/shots/bg-dark/01-home.png](look1/shots/bg-dark/01-home.png)
- [look1/shots/bg-dark/01b-home-scrolled.png](look1/shots/bg-dark/01b-home-scrolled.png)
- [look1/shots/bg-dark/01c-search-expanded.png](look1/shots/bg-dark/01c-search-expanded.png)
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
- [look1/shots/bg-light/01c-search-expanded.png](look1/shots/bg-light/01c-search-expanded.png)
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
- [look1/shots/en-dark/01c-search-expanded.png](look1/shots/en-dark/01c-search-expanded.png)
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
- [look1/shots/en-light/01c-search-expanded.png](look1/shots/en-light/01c-search-expanded.png)
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
- [look2/shots/font130-xlarge/01c-search-expanded.png](look2/shots/font130-xlarge/01c-search-expanded.png)
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
- [look2/shots/landscape/01c-search-expanded.png](look2/shots/landscape/01c-search-expanded.png)
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
- [look3/shots/small-320dp/01c-search-expanded.png](look3/shots/small-320dp/01c-search-expanded.png)
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
- [look3/shots/tablet-800dp/01c-search-expanded.png](look3/shots/tablet-800dp/01c-search-expanded.png)
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
- [look3/shots/tablet-land/01c-search-expanded.png](look3/shots/tablet-land/01c-search-expanded.png)
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

- [media/shots/media/00-gallery.png](media/shots/media/00-gallery.png)
- [media/shots/media/01-album-top.png](media/shots/media/01-album-top.png)
- [media/shots/media/02-album-scrolled-1.png](media/shots/media/02-album-scrolled-1.png)
- [media/shots/media/02-album-scrolled-2.png](media/shots/media/02-album-scrolled-2.png)
- [media/shots/media/02-album-scrolled-3.png](media/shots/media/02-album-scrolled-3.png)
- [media/shots/media/10-viewer-missing.png](media/shots/media/10-viewer-missing.png)
- [media/shots/media/11-viewer-retry-1s.png](media/shots/media/11-viewer-retry-1s.png)
- [media/shots/media/12-viewer-retry-8s.png](media/shots/media/12-viewer-retry-8s.png)
- [media/shots/media/13-viewer-next.png](media/shots/media/13-viewer-next.png)
- [media/shots/media/20-viewer-1x.png](media/shots/media/20-viewer-1x.png)
- [media/shots/media/21-viewer-2x-doubletap.png](media/shots/media/21-viewer-2x-doubletap.png)
- [media/shots/media/22-viewer-pinch-1s.png](media/shots/media/22-viewer-pinch-1s.png)
- [media/shots/media/23-viewer-pinch2-4s.png](media/shots/media/23-viewer-pinch2-4s.png)
- [media/shots/media/24-viewer-zoomed-pan.png](media/shots/media/24-viewer-zoomed-pan.png)
- [media/shots/media/25-viewer-reset.png](media/shots/media/25-viewer-reset.png)
- [media/shots/media/30-catalog-covers.png](media/shots/media/30-catalog-covers.png)
- [media/shots/media/31-catalog-scrolled.png](media/shots/media/31-catalog-scrolled.png)
- [media/shots/media/32-catalog-gatsby.png](media/shots/media/32-catalog-gatsby.png)
- [media/shots/media/33-book-2184.png](media/shots/media/33-book-2184.png)
- [media/shots/media/33-book-399560.png](media/shots/media/33-book-399560.png)
- [media/shots/media/33-book-958.png](media/shots/media/33-book-958.png)
- [media/shots/media/40-documents.png](media/shots/media/40-documents.png)
- [media/shots/media/41-publications.png](media/shots/media/41-publications.png)
- [media/shots/media/pdf-noapp-1.png](media/shots/media/pdf-noapp-1.png)
- [media/shots/media/pdf-noapp-2.png](media/shots/media/pdf-noapp-2.png)
- [media/shots/media/pdf-noapp-3.png](media/shots/media/pdf-noapp-3.png)
- [media/shots/media/pdf-noapp-4.png](media/shots/media/pdf-noapp-4.png)
- [media/shots/media/pdf-noapp-5.png](media/shots/media/pdf-noapp-5.png)
- [media/shots/media/pdf-noapp-final.png](media/shots/media/pdf-noapp-final.png)
- [media/shots/media/pdf-offline-1.png](media/shots/media/pdf-offline-1.png)
- [media/shots/media/pdf-offline-2.png](media/shots/media/pdf-offline-2.png)
- [media/shots/media/pdf-offline-3.png](media/shots/media/pdf-offline-3.png)
- [media/shots/media/pdf-offline-4.png](media/shots/media/pdf-offline-4.png)
- [media/shots/media/pdf-offline-5.png](media/shots/media/pdf-offline-5.png)
- [media/shots/media/pdf-offline-final.png](media/shots/media/pdf-offline-final.png)
- [media/shots/media/pdf-online-1.png](media/shots/media/pdf-online-1.png)
- [media/shots/media/pdf-online-2.png](media/shots/media/pdf-online-2.png)
- [media/shots/media/pdf-online-3.png](media/shots/media/pdf-online-3.png)
- [media/shots/media/pdf-online-4.png](media/shots/media/pdf-online-4.png)
- [media/shots/media/pdf-online-5.png](media/shots/media/pdf-online-5.png)
- [media/shots/media/pdf-online-final.png](media/shots/media/pdf-online-final.png)
- [media/shots/media/pdf-publication-1.png](media/shots/media/pdf-publication-1.png)
- [media/shots/media/pdf-publication-2.png](media/shots/media/pdf-publication-2.png)
- [media/shots/media/pdf-publication-3.png](media/shots/media/pdf-publication-3.png)
- [media/shots/media/pdf-publication-4.png](media/shots/media/pdf-publication-4.png)
- [media/shots/media/pdf-publication-5.png](media/shots/media/pdf-publication-5.png)
- [media/shots/media/pdf-publication-final.png](media/shots/media/pdf-publication-final.png)

### mem-base/shots

- [mem-base/shots/cycles-after-search.png](mem-base/shots/cycles-after-search.png)
- [mem-base/shots/cycles-book.png](mem-base/shots/cycles-book.png)

### mem/shots

- [mem/shots/cycles-after-search.png](mem/shots/cycles-after-search.png)
- [mem/shots/cycles-book.png](mem/shots/cycles-book.png)

### monkey-base/shots

- [monkey-base/shots/monkey-end.png](monkey-base/shots/monkey-end.png)

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

### search34/shots/login

- [search34/shots/login/01-after-open.png](search34/shots/login/01-after-open.png)
- [search34/shots/login/04-after-open.png](search34/shots/login/04-after-open.png)
- [search34/shots/login/08-after-open.png](search34/shots/login/08-after-open.png)
- [search34/shots/login/10-typed.png](search34/shots/login/10-typed.png)

### search34/shots/search

- [search34/shots/search/00-home.png](search34/shots/search/00-home.png)
- [search34/shots/search/01-expanded.png](search34/shots/search/01-expanded.png)
- [search34/shots/search/02-typed-12.png](search34/shots/search/02-typed-12.png)
- [search34/shots/search/03-result.png](search34/shots/search/03-result.png)
- [search34/shots/search/04-back-from-result.png](search34/shots/search/04-back-from-result.png)
- [search34/shots/search/05-after-back-key.png](search34/shots/search/05-after-back-key.png)
- [search34/shots/search/10-gesture-midway.png](search34/shots/search/10-gesture-midway.png)
- [search34/shots/search/11-after-gesture.png](search34/shots/search/11-after-gesture.png)
- [search34/shots/search/14-cancel-midway.png](search34/shots/search/14-cancel-midway.png)
- [search34/shots/search/15-after-cancelled-gesture.png](search34/shots/search/15-after-cancelled-gesture.png)
- [search34/shots/search/20-before-death.png](search34/shots/search/20-before-death.png)
- [search34/shots/search/21-recents.png](search34/shots/search/21-recents.png)
- [search34/shots/search/22-restored.png](search34/shots/search/22-restored.png)
- [search34/shots/search/30-slow-gesture-x140.png](search34/shots/search/30-slow-gesture-x140.png)
- [search34/shots/search/30-slow-gesture-x220.png](search34/shots/search/30-slow-gesture-x220.png)
- [search34/shots/search/30-slow-gesture-x300.png](search34/shots/search/30-slow-gesture-x300.png)
- [search34/shots/search/30-slow-gesture-x380.png](search34/shots/search/30-slow-gesture-x380.png)
- [search34/shots/search/30-slow-gesture-x60.png](search34/shots/search/30-slow-gesture-x60.png)
- [search34/shots/search/31-after-slow-gesture.png](search34/shots/search/31-after-slow-gesture.png)
- [search34/shots/search/40-book-gesture-midway.png](search34/shots/search/40-book-gesture-midway.png)

### update/shots/update

- [update/shots/update/10-update-dialog.png](update/shots/update/10-update-dialog.png)
- [update/shots/update/11-update-dialog-scrolled.png](update/shots/update/11-update-dialog-scrolled.png)
- [update/shots/update/13-after-install.png](update/shots/update/13-after-install.png)
- [update/shots/update/14-whats-new.png](update/shots/update/14-whats-new.png)
- [update/shots/update/15-whats-new-scrolled.png](update/shots/update/15-whats-new-scrolled.png)
- [update/shots/update/16-warm-relaunch.png](update/shots/update/16-warm-relaunch.png)
- [update/shots/update/17-cold-relaunch.png](update/shots/update/17-cold-relaunch.png)
- [update/shots/update/18-settings-update.png](update/shots/update/18-settings-update.png)

### widget/shots/widget

- [widget/shots/widget/70-after-force-stop.png](widget/shots/widget/70-after-force-stop.png)
- [widget/shots/widget/new-00-home.png](widget/shots/widget/new-00-home.png)
- [widget/shots/widget/new-01-longpress.png](widget/shots/widget/new-01-longpress.png)
- [widget/shots/widget/new-03-list.png](widget/shots/widget/new-03-list.png)
- [widget/shots/widget/new-04-app.png](widget/shots/widget/new-04-app.png)
- [widget/shots/widget/new-05-dropped.png](widget/shots/widget/new-05-dropped.png)
- [widget/shots/widget/new-10-wide-4x2-dark.png](widget/shots/widget/new-10-wide-4x2-dark.png)
- [widget/shots/widget/new-10-wide-4x2-light.png](widget/shots/widget/new-10-wide-4x2-light.png)
- [widget/shots/widget/new-20-wide-tall-4x3-dark.png](widget/shots/widget/new-20-wide-tall-4x3-dark.png)
- [widget/shots/widget/new-20-wide-tall-4x3-light.png](widget/shots/widget/new-20-wide-tall-4x3-light.png)
- [widget/shots/widget/new-30-medium-3x2-dark.png](widget/shots/widget/new-30-medium-3x2-dark.png)
- [widget/shots/widget/new-30-medium-3x2-light.png](widget/shots/widget/new-30-medium-3x2-light.png)
- [widget/shots/widget/new-40-small-2x2-dark.png](widget/shots/widget/new-40-small-2x2-dark.png)
- [widget/shots/widget/new-40-small-2x2-light.png](widget/shots/widget/new-40-small-2x2-light.png)
- [widget/shots/widget/new-50-small-tall-2x3-dark.png](widget/shots/widget/new-50-small-tall-2x3-dark.png)
- [widget/shots/widget/new-50-small-tall-2x3-light.png](widget/shots/widget/new-50-small-tall-2x3-light.png)
- [widget/shots/widget/new-60-back-4x2.png](widget/shots/widget/new-60-back-4x2.png)
- [widget/shots/widget/new-click-app.png](widget/shots/widget/new-click-app.png)
- [widget/shots/widget/new-click-catalog.png](widget/shots/widget/new-click-catalog.png)
- [widget/shots/widget/new-click-event.png](widget/shots/widget/new-click-event.png)
- [widget/shots/widget/new-click-scan.png](widget/shots/widget/new-click-scan.png)
- [widget/shots/widget/new-click-small-app.png](widget/shots/widget/new-click-small-app.png)
- [widget/shots/widget/new-click-small-event.png](widget/shots/widget/new-click-small-event.png)
- [widget/shots/widget/new-resize-2x2-frame.png](widget/shots/widget/new-resize-2x2-frame.png)
- [widget/shots/widget/new-resize-2x3-frame.png](widget/shots/widget/new-resize-2x3-frame.png)
- [widget/shots/widget/new-resize-3x2-frame.png](widget/shots/widget/new-resize-3x2-frame.png)
- [widget/shots/widget/new-resize-4x3-frame.png](widget/shots/widget/new-resize-4x3-frame.png)
- [widget/shots/widget/new-resize-back-4x2-frame.png](widget/shots/widget/new-resize-back-4x2-frame.png)

### widgetlang/shots/widget

- [widgetlang/shots/widget/lang-00-home.png](widgetlang/shots/widget/lang-00-home.png)
- [widgetlang/shots/widget/lang-01-longpress.png](widgetlang/shots/widget/lang-01-longpress.png)
- [widgetlang/shots/widget/lang-01-sysEN-appBG.png](widgetlang/shots/widget/lang-01-sysEN-appBG.png)
- [widgetlang/shots/widget/lang-02-sysEN-appEN-right-after.png](widgetlang/shots/widget/lang-02-sysEN-appEN-right-after.png)
- [widgetlang/shots/widget/lang-03-list.png](widgetlang/shots/widget/lang-03-list.png)
- [widgetlang/shots/widget/lang-03-sysEN-appEN-after-15s.png](widgetlang/shots/widget/lang-03-sysEN-appEN-after-15s.png)
- [widgetlang/shots/widget/lang-04-app.png](widgetlang/shots/widget/lang-04-app.png)
- [widgetlang/shots/widget/lang-04-sysEN-appEN-after-reopen.png](widgetlang/shots/widget/lang-04-sysEN-appEN-after-reopen.png)
- [widgetlang/shots/widget/lang-05-dropped.png](widgetlang/shots/widget/lang-05-dropped.png)
- [widgetlang/shots/widget/lang-05-sysEN-appEN-after-restart.png](widgetlang/shots/widget/lang-05-sysEN-appEN-after-restart.png)
- [widgetlang/shots/widget/lang-10-sysBG-appEN-after-reboot.png](widgetlang/shots/widget/lang-10-sysBG-appEN-after-reboot.png)
- [widgetlang/shots/widget/lang-11-sysBG-appEN-after-launch.png](widgetlang/shots/widget/lang-11-sysBG-appEN-after-launch.png)
- [widgetlang/shots/widget/lang-20-sysBG-appBG-right-after.png](widgetlang/shots/widget/lang-20-sysBG-appBG-right-after.png)
- [widgetlang/shots/widget/lang-21-sysBG-appBG-after-restart.png](widgetlang/shots/widget/lang-21-sysBG-appBG-after-restart.png)
- [widgetlang/shots/widget/lang-30-sysBG-appEN-cmd-right-after.png](widgetlang/shots/widget/lang-30-sysBG-appEN-cmd-right-after.png)
- [widgetlang/shots/widget/lang-31-sysBG-appEN-cmd-after-restart.png](widgetlang/shots/widget/lang-31-sysBG-appEN-cmd-after-restart.png)
- [widgetlang/shots/widget/lang-32-sysBG-appEN-dark.png](widgetlang/shots/widget/lang-32-sysBG-appEN-dark.png)

### widgetupd/shots/widget

- [widgetupd/shots/widget/base-00-home.png](widgetupd/shots/widget/base-00-home.png)
- [widgetupd/shots/widget/base-01-longpress.png](widgetupd/shots/widget/base-01-longpress.png)
- [widgetupd/shots/widget/base-03-list.png](widgetupd/shots/widget/base-03-list.png)
- [widgetupd/shots/widget/base-04-app.png](widgetupd/shots/widget/base-04-app.png)
- [widgetupd/shots/widget/base-05-dropped.png](widgetupd/shots/widget/base-05-dropped.png)
- [widgetupd/shots/widget/upd-10-base-widget.png](widgetupd/shots/widget/upd-10-base-widget.png)
- [widgetupd/shots/widget/upd-215-after-update-15s.png](widgetupd/shots/widget/upd-215-after-update-15s.png)
- [widgetupd/shots/widget/upd-23-after-update-3s.png](widgetupd/shots/widget/upd-23-after-update-3s.png)
- [widgetupd/shots/widget/upd-245-after-update-45s.png](widgetupd/shots/widget/upd-245-after-update-45s.png)
- [widgetupd/shots/widget/upd-40-notif-settings-a.png](widgetupd/shots/widget/upd-40-notif-settings-a.png)
- [widgetupd/shots/widget/upd-40-notif-settings-b.png](widgetupd/shots/widget/upd-40-notif-settings-b.png)
- [widgetupd/shots/widget/upd-40-notif-settings-c.png](widgetupd/shots/widget/upd-40-notif-settings-c.png)
- [widgetupd/shots/widget/upd-50-after-app.png](widgetupd/shots/widget/upd-50-after-app.png)
- [widgetupd/shots/widget/upd-51-dark.png](widgetupd/shots/widget/upd-51-dark.png)
- [widgetupd/shots/widget/upd-click-app.png](widgetupd/shots/widget/upd-click-app.png)
- [widgetupd/shots/widget/upd-click-catalog.png](widgetupd/shots/widget/upd-click-catalog.png)
- [widgetupd/shots/widget/upd-click-event.png](widgetupd/shots/widget/upd-click-event.png)
- [widgetupd/shots/widget/upd-click-scan.png](widgetupd/shots/widget/upd-click-scan.png)

### zoom/shots/zoom

- [zoom/shots/zoom/base-1-0-1x.png](zoom/shots/zoom/base-1-0-1x.png)
- [zoom/shots/zoom/base-1-1-2x.png](zoom/shots/zoom/base-1-1-2x.png)
- [zoom/shots/zoom/base-1-2-after-dtap-at-2x.png](zoom/shots/zoom/base-1-2-after-dtap-at-2x.png)
- [zoom/shots/zoom/base-1-3-pinched.png](zoom/shots/zoom/base-1-3-pinched.png)
- [zoom/shots/zoom/base-1-4-after-dtap-pinched.png](zoom/shots/zoom/base-1-4-after-dtap-pinched.png)
- [zoom/shots/zoom/base-2-0-1x.png](zoom/shots/zoom/base-2-0-1x.png)
- [zoom/shots/zoom/base-2-1-2x.png](zoom/shots/zoom/base-2-1-2x.png)
- [zoom/shots/zoom/base-2-2-after-dtap-at-2x.png](zoom/shots/zoom/base-2-2-after-dtap-at-2x.png)
- [zoom/shots/zoom/base-2-3-pinched.png](zoom/shots/zoom/base-2-3-pinched.png)
- [zoom/shots/zoom/base-2-4-after-dtap-pinched.png](zoom/shots/zoom/base-2-4-after-dtap-pinched.png)
- [zoom/shots/zoom/base-3-0-1x.png](zoom/shots/zoom/base-3-0-1x.png)
- [zoom/shots/zoom/base-3-1-2x.png](zoom/shots/zoom/base-3-1-2x.png)
- [zoom/shots/zoom/base-3-2-after-dtap-at-2x.png](zoom/shots/zoom/base-3-2-after-dtap-at-2x.png)
- [zoom/shots/zoom/base-3-3-pinched.png](zoom/shots/zoom/base-3-3-pinched.png)
- [zoom/shots/zoom/base-3-4-after-dtap-pinched.png](zoom/shots/zoom/base-3-4-after-dtap-pinched.png)
- [zoom/shots/zoom/new-1-0-1x.png](zoom/shots/zoom/new-1-0-1x.png)
- [zoom/shots/zoom/new-1-1-2x.png](zoom/shots/zoom/new-1-1-2x.png)
- [zoom/shots/zoom/new-1-2-after-dtap-at-2x.png](zoom/shots/zoom/new-1-2-after-dtap-at-2x.png)
- [zoom/shots/zoom/new-1-3-pinched.png](zoom/shots/zoom/new-1-3-pinched.png)
- [zoom/shots/zoom/new-1-4-after-dtap-pinched.png](zoom/shots/zoom/new-1-4-after-dtap-pinched.png)
- [zoom/shots/zoom/new-2-0-1x.png](zoom/shots/zoom/new-2-0-1x.png)
- [zoom/shots/zoom/new-2-1-2x.png](zoom/shots/zoom/new-2-1-2x.png)
- [zoom/shots/zoom/new-2-2-after-dtap-at-2x.png](zoom/shots/zoom/new-2-2-after-dtap-at-2x.png)
- [zoom/shots/zoom/new-2-3-pinched.png](zoom/shots/zoom/new-2-3-pinched.png)
- [zoom/shots/zoom/new-2-4-after-dtap-pinched.png](zoom/shots/zoom/new-2-4-after-dtap-pinched.png)
- [zoom/shots/zoom/new-3-0-1x.png](zoom/shots/zoom/new-3-0-1x.png)
- [zoom/shots/zoom/new-3-1-2x.png](zoom/shots/zoom/new-3-1-2x.png)
- [zoom/shots/zoom/new-3-2-after-dtap-at-2x.png](zoom/shots/zoom/new-3-2-after-dtap-at-2x.png)
- [zoom/shots/zoom/new-3-3-pinched.png](zoom/shots/zoom/new-3-3-pinched.png)
- [zoom/shots/zoom/new-3-4-after-dtap-pinched.png](zoom/shots/zoom/new-3-4-after-dtap-pinched.png)

## Other files

- [a11y/a11y/01-home.xml](a11y/a11y/01-home.xml)
- [a11y/a11y/02-search-expanded.xml](a11y/a11y/02-search-expanded.xml)
- [a11y/a11y/03-catalog.xml](a11y/a11y/03-catalog.xml)
- [a11y/a11y/04-book.xml](a11y/a11y/04-book.xml)
- [a11y/a11y/05-news.xml](a11y/a11y/05-news.xml)
- [a11y/a11y/06-article.xml](a11y/a11y/06-article.xml)
- [a11y/a11y/07-calendar.xml](a11y/a11y/07-calendar.xml)
- [a11y/a11y/08-my.xml](a11y/a11y/08-my.xml)
- [a11y/a11y/09-want.xml](a11y/a11y/09-want.xml)
- [a11y/a11y/10-more.xml](a11y/a11y/10-more.xml)
- [a11y/a11y/11-settings.xml](a11y/a11y/11-settings.xml)
- [a11y/a11y/12-gallery.xml](a11y/a11y/12-gallery.xml)
- [a11y/a11y/13-album.xml](a11y/a11y/13-album.xml)
- [a11y/a11y/14-viewer.xml](a11y/a11y/14-viewer.xml)
- [a11y/a11y/15-contacts.xml](a11y/a11y/15-contacts.xml)
- [a11y/a11y/16-documents.xml](a11y/a11y/16-documents.xml)
- [a11y/a11y/17-login.xml](a11y/a11y/17-login.xml)
- [a11y/a11y/18-scanner-manual.xml](a11y/a11y/18-scanner-manual.xml)
- [a11y/a11y/21-home-en-font130.xml](a11y/a11y/21-home-en-font130.xml)
- [a11y/a11y/22-catalog-en-font130.xml](a11y/a11y/22-catalog-en-font130.xml)
- [a11y/a11y/23-book-en-font130.xml](a11y/a11y/23-book-en-font130.xml)
- [a11y/a11y/24-more-en-font130.xml](a11y/a11y/24-more-en-font130.xml)
- [a11y/host-after-run.txt](a11y/host-after-run.txt)
- [a11y/job-status.txt](a11y/job-status.txt)
- [a11y/logcat-all.txt](a11y/logcat-all.txt)
- [a11y/logcat-dump.txt](a11y/logcat-dump.txt)
- [a11y/logcat-findings.txt](a11y/logcat-findings.txt)
- [a11y/logcat-live.txt](a11y/logcat-live.txt)
- [a11y/summary.txt](a11y/summary.txt)
- [abstart-1/cat-first-base.txt](abstart-1/cat-first-base.txt)
- [abstart-1/cat-first-new-excluded.txt](abstart-1/cat-first-new-excluded.txt)
- [abstart-1/cat-first-new.txt](abstart-1/cat-first-new.txt)
- [abstart-1/cat-ready-base.txt](abstart-1/cat-ready-base.txt)
- [abstart-1/cat-ready-new-excluded.txt](abstart-1/cat-ready-new-excluded.txt)
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
- [abstart-1/start-new-0-cold.txt](abstart-1/start-new-0-cold.txt)
- [abstart-1/start-new-0-warm.txt](abstart-1/start-new-0-warm.txt)
- [abstart-1/start-new-1-cold.txt](abstart-1/start-new-1-cold.txt)
- [abstart-1/start-new-1-warm.txt](abstart-1/start-new-1-warm.txt)
- [abstart-1/start-new-2-cold.txt](abstart-1/start-new-2-cold.txt)
- [abstart-1/start-new-2-warm.txt](abstart-1/start-new-2-warm.txt)
- [abstart-1/start-new-cold-all.txt](abstart-1/start-new-cold-all.txt)
- [abstart-1/start-new-warm-all.txt](abstart-1/start-new-warm-all.txt)
- [abstart-1/summary.txt](abstart-1/summary.txt)
- [abstart-2/cat-first-base-excluded.txt](abstart-2/cat-first-base-excluded.txt)
- [abstart-2/cat-first-base.txt](abstart-2/cat-first-base.txt)
- [abstart-2/cat-first-new.txt](abstart-2/cat-first-new.txt)
- [abstart-2/cat-ready-base-excluded.txt](abstart-2/cat-ready-base-excluded.txt)
- [abstart-2/cat-ready-base.txt](abstart-2/cat-ready-base.txt)
- [abstart-2/cat-ready-new.txt](abstart-2/cat-ready-new.txt)
- [abstart-2/host-after-run.txt](abstart-2/host-after-run.txt)
- [abstart-2/job-status.txt](abstart-2/job-status.txt)
- [abstart-2/logcat-all.txt](abstart-2/logcat-all.txt)
- [abstart-2/logcat-dump.txt](abstart-2/logcat-dump.txt)
- [abstart-2/logcat-findings.txt](abstart-2/logcat-findings.txt)
- [abstart-2/logcat-live.txt](abstart-2/logcat-live.txt)
- [abstart-2/start-base-0-cold.txt](abstart-2/start-base-0-cold.txt)
- [abstart-2/start-base-0-warm.txt](abstart-2/start-base-0-warm.txt)
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
- [abstart-3/cat-first-base.txt](abstart-3/cat-first-base.txt)
- [abstart-3/cat-first-new-excluded.txt](abstart-3/cat-first-new-excluded.txt)
- [abstart-3/cat-first-new.txt](abstart-3/cat-first-new.txt)
- [abstart-3/cat-ready-base.txt](abstart-3/cat-ready-base.txt)
- [abstart-3/cat-ready-new-excluded.txt](abstart-3/cat-ready-new-excluded.txt)
- [abstart-3/cat-ready-new.txt](abstart-3/cat-ready-new.txt)
- [abstart-3/host-after-run.txt](abstart-3/host-after-run.txt)
- [abstart-3/job-status.txt](abstart-3/job-status.txt)
- [abstart-3/logcat-all.txt](abstart-3/logcat-all.txt)
- [abstart-3/logcat-dump.txt](abstart-3/logcat-dump.txt)
- [abstart-3/logcat-findings.txt](abstart-3/logcat-findings.txt)
- [abstart-3/logcat-live.txt](abstart-3/logcat-live.txt)
- [abstart-3/start-base-1-cold.txt](abstart-3/start-base-1-cold.txt)
- [abstart-3/start-base-1-warm.txt](abstart-3/start-base-1-warm.txt)
- [abstart-3/start-base-2-cold.txt](abstart-3/start-base-2-cold.txt)
- [abstart-3/start-base-2-warm.txt](abstart-3/start-base-2-warm.txt)
- [abstart-3/start-base-cold-all.txt](abstart-3/start-base-cold-all.txt)
- [abstart-3/start-base-warm-all.txt](abstart-3/start-base-warm-all.txt)
- [abstart-3/start-new-0-cold.txt](abstart-3/start-new-0-cold.txt)
- [abstart-3/start-new-0-warm.txt](abstart-3/start-new-0-warm.txt)
- [abstart-3/start-new-1-cold.txt](abstart-3/start-new-1-cold.txt)
- [abstart-3/start-new-1-warm.txt](abstart-3/start-new-1-warm.txt)
- [abstart-3/start-new-2-cold.txt](abstart-3/start-new-2-cold.txt)
- [abstart-3/start-new-2-warm.txt](abstart-3/start-new-2-warm.txt)
- [abstart-3/start-new-cold-all.txt](abstart-3/start-new-cold-all.txt)
- [abstart-3/start-new-warm-all.txt](abstart-3/start-new-warm-all.txt)
- [abstart-3/summary.txt](abstart-3/summary.txt)
- [abstart-4/cat-first-base-excluded.txt](abstart-4/cat-first-base-excluded.txt)
- [abstart-4/cat-first-base.txt](abstart-4/cat-first-base.txt)
- [abstart-4/cat-first-new.txt](abstart-4/cat-first-new.txt)
- [abstart-4/cat-ready-base-excluded.txt](abstart-4/cat-ready-base-excluded.txt)
- [abstart-4/cat-ready-base.txt](abstart-4/cat-ready-base.txt)
- [abstart-4/cat-ready-new.txt](abstart-4/cat-ready-new.txt)
- [abstart-4/host-after-run.txt](abstart-4/host-after-run.txt)
- [abstart-4/job-status.txt](abstart-4/job-status.txt)
- [abstart-4/logcat-all.txt](abstart-4/logcat-all.txt)
- [abstart-4/logcat-dump.txt](abstart-4/logcat-dump.txt)
- [abstart-4/logcat-findings.txt](abstart-4/logcat-findings.txt)
- [abstart-4/logcat-live.txt](abstart-4/logcat-live.txt)
- [abstart-4/start-base-0-cold.txt](abstart-4/start-base-0-cold.txt)
- [abstart-4/start-base-0-warm.txt](abstart-4/start-base-0-warm.txt)
- [abstart-4/start-base-1-cold.txt](abstart-4/start-base-1-cold.txt)
- [abstart-4/start-base-1-warm.txt](abstart-4/start-base-1-warm.txt)
- [abstart-4/start-base-2-cold.txt](abstart-4/start-base-2-cold.txt)
- [abstart-4/start-base-2-warm.txt](abstart-4/start-base-2-warm.txt)
- [abstart-4/start-base-cold-all.txt](abstart-4/start-base-cold-all.txt)
- [abstart-4/start-base-warm-all.txt](abstart-4/start-base-warm-all.txt)
- [abstart-4/start-new-1-cold.txt](abstart-4/start-new-1-cold.txt)
- [abstart-4/start-new-1-warm.txt](abstart-4/start-new-1-warm.txt)
- [abstart-4/start-new-2-cold.txt](abstart-4/start-new-2-cold.txt)
- [abstart-4/start-new-2-warm.txt](abstart-4/start-new-2-warm.txt)
- [abstart-4/start-new-cold-all.txt](abstart-4/start-new-cold-all.txt)
- [abstart-4/start-new-warm-all.txt](abstart-4/start-new-warm-all.txt)
- [abstart-4/summary.txt](abstart-4/summary.txt)
- [func/host-after-run.txt](func/host-after-run.txt)
- [func/job-status.txt](func/job-status.txt)
- [func/logcat-all.txt](func/logcat-all.txt)
- [func/logcat-dump.txt](func/logcat-dump.txt)
- [func/logcat-findings.txt](func/logcat-findings.txt)
- [func/logcat-live.txt](func/logcat-live.txt)
- [func/summary.txt](func/summary.txt)
- [homey/homey/base-cached-1/final.xml](homey/homey/base-cached-1/final.xml)
- [homey/homey/base-cached-1/rec.mp4](homey/homey/base-cached-1/rec.mp4)
- [homey/homey/base-cached-2/final.xml](homey/homey/base-cached-2/final.xml)
- [homey/homey/base-cached-2/rec.mp4](homey/homey/base-cached-2/rec.mp4)
- [homey/homey/base-cached-3/final.xml](homey/homey/base-cached-3/final.xml)
- [homey/homey/base-cached-3/rec.mp4](homey/homey/base-cached-3/rec.mp4)
- [homey/homey/base-clearcache-1/final.xml](homey/homey/base-clearcache-1/final.xml)
- [homey/homey/base-clearcache-1/rec.mp4](homey/homey/base-clearcache-1/rec.mp4)
- [homey/homey/base-clearcache-2/final.xml](homey/homey/base-clearcache-2/final.xml)
- [homey/homey/base-clearcache-2/rec.mp4](homey/homey/base-clearcache-2/rec.mp4)
- [homey/homey/base-normal/rec.mp4](homey/homey/base-normal/rec.mp4)
- [homey/homey/new-cached-1/final.xml](homey/homey/new-cached-1/final.xml)
- [homey/homey/new-cached-1/rec.mp4](homey/homey/new-cached-1/rec.mp4)
- [homey/homey/new-cached-2/final.xml](homey/homey/new-cached-2/final.xml)
- [homey/homey/new-cached-2/rec.mp4](homey/homey/new-cached-2/rec.mp4)
- [homey/homey/new-cached-3/final.xml](homey/homey/new-cached-3/final.xml)
- [homey/homey/new-cached-3/rec.mp4](homey/homey/new-cached-3/rec.mp4)
- [homey/homey/new-clearcache-1/final.xml](homey/homey/new-clearcache-1/final.xml)
- [homey/homey/new-clearcache-1/rec.mp4](homey/homey/new-clearcache-1/rec.mp4)
- [homey/homey/new-clearcache-2/final.xml](homey/homey/new-clearcache-2/final.xml)
- [homey/homey/new-clearcache-2/rec.mp4](homey/homey/new-clearcache-2/rec.mp4)
- [homey/homey/new-normal/rec.mp4](homey/homey/new-normal/rec.mp4)
- [homey/host-after-run.txt](homey/host-after-run.txt)
- [homey/job-status.txt](homey/job-status.txt)
- [homey/logcat-all.txt](homey/logcat-all.txt)
- [homey/logcat-dump.txt](homey/logcat-dump.txt)
- [homey/logcat-findings.txt](homey/logcat-findings.txt)
- [homey/logcat-live.txt](homey/logcat-live.txt)
- [homey/summary.txt](homey/summary.txt)
- [jank-1/debug/1-viewer.xml](jank-1/debug/1-viewer.xml)
- [jank-1/debug/2-viewer.xml](jank-1/debug/2-viewer.xml)
- [jank-1/gfx-base-album-grid.txt](jank-1/gfx-base-album-grid.txt)
- [jank-1/gfx-base-calendar.txt](jank-1/gfx-base-calendar.txt)
- [jank-1/gfx-base-catalog.txt](jank-1/gfx-base-catalog.txt)
- [jank-1/gfx-base-gallery.txt](jank-1/gfx-base-gallery.txt)
- [jank-1/gfx-base-home.txt](jank-1/gfx-base-home.txt)
- [jank-1/gfx-base-news.txt](jank-1/gfx-base-news.txt)
- [jank-1/gfx-base-want.txt](jank-1/gfx-base-want.txt)
- [jank-1/gfx-new-album-grid.txt](jank-1/gfx-new-album-grid.txt)
- [jank-1/gfx-new-calendar.txt](jank-1/gfx-new-calendar.txt)
- [jank-1/gfx-new-catalog.txt](jank-1/gfx-new-catalog.txt)
- [jank-1/gfx-new-gallery.txt](jank-1/gfx-new-gallery.txt)
- [jank-1/gfx-new-home.txt](jank-1/gfx-new-home.txt)
- [jank-1/gfx-new-news.txt](jank-1/gfx-new-news.txt)
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
- [jank-2/gfx-base-album-grid.txt](jank-2/gfx-base-album-grid.txt)
- [jank-2/gfx-base-calendar.txt](jank-2/gfx-base-calendar.txt)
- [jank-2/gfx-base-catalog.txt](jank-2/gfx-base-catalog.txt)
- [jank-2/gfx-base-gallery.txt](jank-2/gfx-base-gallery.txt)
- [jank-2/gfx-base-home.txt](jank-2/gfx-base-home.txt)
- [jank-2/gfx-base-news.txt](jank-2/gfx-base-news.txt)
- [jank-2/gfx-base-want.txt](jank-2/gfx-base-want.txt)
- [jank-2/gfx-new-album-grid.txt](jank-2/gfx-new-album-grid.txt)
- [jank-2/gfx-new-calendar.txt](jank-2/gfx-new-calendar.txt)
- [jank-2/gfx-new-catalog.txt](jank-2/gfx-new-catalog.txt)
- [jank-2/gfx-new-gallery.txt](jank-2/gfx-new-gallery.txt)
- [jank-2/gfx-new-home.txt](jank-2/gfx-new-home.txt)
- [jank-2/gfx-new-news.txt](jank-2/gfx-new-news.txt)
- [jank-2/gfx-new-search-expanded.txt](jank-2/gfx-new-search-expanded.txt)
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
- [jankv/gfx-base-viewer.txt](jankv/gfx-base-viewer.txt)
- [jankv/gfx-new-viewer.txt](jankv/gfx-new-viewer.txt)
- [jankv/host-after-run.txt](jankv/host-after-run.txt)
- [jankv/job-status.txt](jankv/job-status.txt)
- [jankv/logcat-all.txt](jankv/logcat-all.txt)
- [jankv/logcat-dump.txt](jankv/logcat-dump.txt)
- [jankv/logcat-findings.txt](jankv/logcat-findings.txt)
- [jankv/logcat-live.txt](jankv/logcat-live.txt)
- [jankv/summary.txt](jankv/summary.txt)
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
- [media/getevent.txt](media/getevent.txt)
- [media/host-after-run.txt](media/host-after-run.txt)
- [media/job-status.txt](media/job-status.txt)
- [media/logcat-all.txt](media/logcat-all.txt)
- [media/logcat-dump.txt](media/logcat-dump.txt)
- [media/logcat-findings.txt](media/logcat-findings.txt)
- [media/logcat-live.txt](media/logcat-live.txt)
- [media/mem-zoomed-raw.txt](media/mem-zoomed-raw.txt)
- [media/mem-zoomed.txt](media/mem-zoomed.txt)
- [media/mt.sh](media/mt.sh)
- [media/probe-fotodokumentalna.txt](media/probe-fotodokumentalna.txt)
- [media/summary.txt](media/summary.txt)
- [mem-base/host-after-run.txt](mem-base/host-after-run.txt)
- [mem-base/job-status.txt](mem-base/job-status.txt)
- [mem-base/logcat-all.txt](mem-base/logcat-all.txt)
- [mem-base/logcat-dump.txt](mem-base/logcat-dump.txt)
- [mem-base/logcat-findings.txt](mem-base/logcat-findings.txt)
- [mem-base/logcat-live.txt](mem-base/logcat-live.txt)
- [mem-base/mem-c00-raw.txt](mem-base/mem-c00-raw.txt)
- [mem-base/mem-c00.txt](mem-base/mem-c00.txt)
- [mem-base/mem-c05-raw.txt](mem-base/mem-c05-raw.txt)
- [mem-base/mem-c05.txt](mem-base/mem-c05.txt)
- [mem-base/mem-c10-raw.txt](mem-base/mem-c10-raw.txt)
- [mem-base/mem-c10.txt](mem-base/mem-c10.txt)
- [mem-base/mem-c15-raw.txt](mem-base/mem-c15-raw.txt)
- [mem-base/mem-c15.txt](mem-base/mem-c15.txt)
- [mem-base/mem-c20-raw.txt](mem-base/mem-c20-raw.txt)
- [mem-base/mem-c20.txt](mem-base/mem-c20.txt)
- [mem-base/mem-c25-raw.txt](mem-base/mem-c25-raw.txt)
- [mem-base/mem-c25.txt](mem-base/mem-c25.txt)
- [mem-base/mem-c30-raw.txt](mem-base/mem-c30-raw.txt)
- [mem-base/mem-c30.txt](mem-base/mem-c30.txt)
- [mem-base/mem-trend.txt](mem-base/mem-trend.txt)
- [mem-base/summary.txt](mem-base/summary.txt)
- [mem/host-after-run.txt](mem/host-after-run.txt)
- [mem/job-status.txt](mem/job-status.txt)
- [mem/logcat-all.txt](mem/logcat-all.txt)
- [mem/logcat-dump.txt](mem/logcat-dump.txt)
- [mem/logcat-findings.txt](mem/logcat-findings.txt)
- [mem/logcat-live.txt](mem/logcat-live.txt)
- [mem/mem-c00-raw.txt](mem/mem-c00-raw.txt)
- [mem/mem-c00.txt](mem/mem-c00.txt)
- [mem/mem-c05-raw.txt](mem/mem-c05-raw.txt)
- [mem/mem-c05.txt](mem/mem-c05.txt)
- [mem/mem-c10-raw.txt](mem/mem-c10-raw.txt)
- [mem/mem-c10.txt](mem/mem-c10.txt)
- [mem/mem-c15-raw.txt](mem/mem-c15-raw.txt)
- [mem/mem-c15.txt](mem/mem-c15.txt)
- [mem/mem-c20-raw.txt](mem/mem-c20-raw.txt)
- [mem/mem-c20.txt](mem/mem-c20.txt)
- [mem/mem-c25-raw.txt](mem/mem-c25-raw.txt)
- [mem/mem-c25.txt](mem/mem-c25.txt)
- [mem/mem-c30-raw.txt](mem/mem-c30-raw.txt)
- [mem/mem-c30.txt](mem/mem-c30.txt)
- [mem/mem-trend.txt](mem/mem-trend.txt)
- [mem/summary.txt](mem/summary.txt)
- [monkey-base/host-after-run.txt](monkey-base/host-after-run.txt)
- [monkey-base/job-status.txt](monkey-base/job-status.txt)
- [monkey-base/logcat-all.txt](monkey-base/logcat-all.txt)
- [monkey-base/logcat-dump.txt](monkey-base/logcat-dump.txt)
- [monkey-base/logcat-findings.txt](monkey-base/logcat-findings.txt)
- [monkey-base/logcat-live.txt](monkey-base/logcat-live.txt)
- [monkey-base/mem-after-monkey-raw.txt](monkey-base/mem-after-monkey-raw.txt)
- [monkey-base/mem-after-monkey.txt](monkey-base/mem-after-monkey.txt)
- [monkey-base/monkey.txt](monkey-base/monkey.txt)
- [monkey-base/summary.txt](monkey-base/summary.txt)
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
- [runs.txt](runs.txt)
- [search34/host-after-run.txt](search34/host-after-run.txt)
- [search34/job-status.txt](search34/job-status.txt)
- [search34/logcat-all.txt](search34/logcat-all.txt)
- [search34/logcat-dump.txt](search34/logcat-dump.txt)
- [search34/logcat-findings.txt](search34/logcat-findings.txt)
- [search34/logcat-live.txt](search34/logcat-live.txt)
- [search34/summary.txt](search34/summary.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-base-upd-ref.txt](size/apk-base-upd-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/size-base.txt](size/size-base.txt)
- [size/size-new.txt](size/size-new.txt)
- [update/41228034302/none.json](update/41228034302/none.json)
- [update/41228034302/real-update-1.2.28.json](update/41228034302/real-update-1.2.28.json)
- [update/41228034302/update.json](update/41228034302/update.json)
- [update/dialog.xml](update/dialog.xml)
- [update/host-after-run.txt](update/host-after-run.txt)
- [update/job-status.txt](update/job-status.txt)
- [update/logcat-all.txt](update/logcat-all.txt)
- [update/logcat-dump.txt](update/logcat-dump.txt)
- [update/logcat-findings.txt](update/logcat-findings.txt)
- [update/logcat-live.txt](update/logcat-live.txt)
- [update/summary.txt](update/summary.txt)
- [update/update.json](update/update.json)
- [update/whatsnew-14-whats-new.xml](update/whatsnew-14-whats-new.xml)
- [widget/appwidget-new-2x2.txt](widget/appwidget-new-2x2.txt)
- [widget/appwidget-new-2x3.txt](widget/appwidget-new-2x3.txt)
- [widget/appwidget-new-3x2.txt](widget/appwidget-new-3x2.txt)
- [widget/appwidget-new-4x3.txt](widget/appwidget-new-4x3.txt)
- [widget/appwidget-new-after.txt](widget/appwidget-new-after.txt)
- [widget/appwidget-new-back-4x2.txt](widget/appwidget-new-back-4x2.txt)
- [widget/appwidget-new-before.txt](widget/appwidget-new-before.txt)
- [widget/host-after-run.txt](widget/host-after-run.txt)
- [widget/job-status.txt](widget/job-status.txt)
- [widget/logcat-all.txt](widget/logcat-all.txt)
- [widget/logcat-dump.txt](widget/logcat-dump.txt)
- [widget/logcat-findings.txt](widget/logcat-findings.txt)
- [widget/logcat-live.txt](widget/logcat-live.txt)
- [widget/summary.txt](widget/summary.txt)
- [widget/wids.txt](widget/wids.txt)
- [widgetlang/appwidget-lang-after.txt](widgetlang/appwidget-lang-after.txt)
- [widgetlang/appwidget-lang-before.txt](widgetlang/appwidget-lang-before.txt)
- [widgetlang/host-after-run.txt](widgetlang/host-after-run.txt)
- [widgetlang/job-status.txt](widgetlang/job-status.txt)
- [widgetlang/logcat-all.txt](widgetlang/logcat-all.txt)
- [widgetlang/logcat-dump.txt](widgetlang/logcat-dump.txt)
- [widgetlang/logcat-findings.txt](widgetlang/logcat-findings.txt)
- [widgetlang/logcat-live.txt](widgetlang/logcat-live.txt)
- [widgetlang/summary.txt](widgetlang/summary.txt)
- [widgetlang/wids.txt](widgetlang/wids.txt)
- [widgetupd/appwidget-base-after.txt](widgetupd/appwidget-base-after.txt)
- [widgetupd/appwidget-base-before.txt](widgetupd/appwidget-base-before.txt)
- [widgetupd/appwidget-upd-after.txt](widgetupd/appwidget-upd-after.txt)
- [widgetupd/channels-after-update.txt](widgetupd/channels-after-update.txt)
- [widgetupd/channels-base-before.txt](widgetupd/channels-base-before.txt)
- [widgetupd/host-after-run.txt](widgetupd/host-after-run.txt)
- [widgetupd/job-status.txt](widgetupd/job-status.txt)
- [widgetupd/logcat-all.txt](widgetupd/logcat-all.txt)
- [widgetupd/logcat-dump.txt](widgetupd/logcat-dump.txt)
- [widgetupd/logcat-findings.txt](widgetupd/logcat-findings.txt)
- [widgetupd/logcat-live.txt](widgetupd/logcat-live.txt)
- [widgetupd/notif-after-update.txt](widgetupd/notif-after-update.txt)
- [widgetupd/notif-base-before.txt](widgetupd/notif-base-before.txt)
- [widgetupd/summary.txt](widgetupd/summary.txt)
- [widgetupd/wids.txt](widgetupd/wids.txt)
- [zoom/getevent.txt](zoom/getevent.txt)
- [zoom/host-after-run.txt](zoom/host-after-run.txt)
- [zoom/job-status.txt](zoom/job-status.txt)
- [zoom/logcat-all.txt](zoom/logcat-all.txt)
- [zoom/logcat-dump.txt](zoom/logcat-dump.txt)
- [zoom/logcat-findings.txt](zoom/logcat-findings.txt)
- [zoom/logcat-live.txt](zoom/logcat-live.txt)
- [zoom/mt.sh](zoom/mt.sh)
- [zoom/summary.txt](zoom/summary.txt)
