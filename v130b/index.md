# Wave 16 emulator check, round b (255fdc6 fixes: update re-download, EmojiCompat back; test v1.2.30 vs v1.2.29 = b84650f)

Source: `debug/wave16-check` @ b9ee56a, last run 41353351352 (attempt 1), 2026-11-01T22:13:39Z. Runs: [runs.txt](runs.txt).
Emulators: google_apis x86_64 API 33 (parts ending in 34/28/26: that API), pixel_6 profile, -gpu swangle_indirect, no camera; app locale bg-BG unless a part says otherwise. APKs: minified prodRelease, x86_64 added, throwaway key; v1.2.30 versionCode 1488543, v1.2.29 versionCode 1488542.
Round a (bf10741): [../v130/index.md](../v130/index.md). Earlier: v1.2.29 check [../v129/index.md](../v129/index.md).

## APK size (release configuration: arm64-v8a + armeabi-v7a, no x86_64)

```
== new 1.2.30 255fdc6: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4497441 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488543  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.30" (Raw: "1.2.30")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.USE_BIOMETRIC' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.FOREGROUND_SERVICE' 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
foreground services: 
part                    compressed  uncompressed  files
classes*.dex               3524197       6974060      2
res                         589531       1023145    322
resources.arsc              291052        291052      1
assets/                      20463         20463      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5465         23760      1
META-INF/                      846          1288      6
== base 1.2.29 b84650f: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4096702 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488542  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.29" (Raw: "1.2.29")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'android.permission.FOREGROUND_SERVICE' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
foreground services: 
part                    compressed  uncompressed  files
classes*.dex               3346135       6616220      2
res                         468368        832408     57
resources.arsc              217040        217040      1
assets/                      19674         19674      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5342         23192      1
META-INF/                      846          1288      6
```

| part | status |
|---|---|
| abstart-1 | COMPLETE; part=abstart-1 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| abstart-2 | COMPLETE; part=abstart-2 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| emoji26 | COMPLETE; part=emoji26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| emoji28 | COMPLETE; part=emoji28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| sizediff | COMPLETE; no job status |
| upd33 | COMPLETE; part=upd33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| upd34 | COMPLETE; part=upd34 target=google_apis api=34 gpu=swangle_indirect emulator-step=success |

## abstart-1

```
part abstart-1; APK: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-0 FIRST-INSTALL catalogue results after 2843 ms
new-0 INDEX-READY #1 catalogue results after 2953 ms
new-0 INDEX-READY #2 catalogue results after 2723 ms
new-0 INDEX-READY #3 catalogue results after 4161 ms
START cold new-0         n=10 min=1593ms median=1968ms max=3352ms  all=[3352, 2609, 2579, 2114, 1852, 1802, 1593, 1688, 1812, 2085]  states=['COLD']
START warm new-0         n=10 min=222ms median=542ms max=941ms  all=[466, 652, 797, 528, 941, 460, 485, 222, 660, 555]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2535 ms
base-1 INDEX-READY #1 catalogue results after 2795 ms
base-1 INDEX-READY #2 catalogue results after 2769 ms
base-1 INDEX-READY #3 catalogue results after 2576 ms
START cold base-1        n=10 min=1397ms median=1661ms max=1887ms  all=[1712, 1875, 1610, 1815, 1460, 1549, 1397, 1887, 1556, 1819]  states=['COLD']
START warm base-1        n=10 min=280ms median=468ms max=925ms  all=[375, 325, 925, 393, 280, 579, 561, 771, 399, 537]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2719 ms
new-1 INDEX-READY #1 catalogue results after 2566 ms
new-1 INDEX-READY #2 catalogue results after 2702 ms
new-1 INDEX-READY #3 catalogue results after 2813 ms
START cold new-1         n=10 min=1456ms median=1782ms max=2143ms  all=[2143, 2040, 1731, 1901, 1456, 1640, 1769, 1698, 1794, 1876]  states=['COLD']
START warm new-1         n=10 min=488ms median=688ms max=879ms  all=[879, 741, 682, 693, 490, 650, 704, 590, 811, 488]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2728 ms
base-2 INDEX-READY #1 catalogue results after 2637 ms
base-2 INDEX-READY #2 catalogue results after 2636 ms
base-2 INDEX-READY #3 catalogue results after 2676 ms
START cold base-2        n=10 min=1560ms median=1762ms max=2365ms  all=[1905, 1932, 1563, 2365, 1688, 1837, 1973, 1654, 1560, 1686]  states=['COLD']
START warm base-2        n=10 min=303ms median=550ms max=1144ms  all=[845, 1144, 518, 599, 573, 543, 455, 476, 558, 303]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2804 ms
new-2 INDEX-READY #1 catalogue results after 2890 ms
new-2 INDEX-READY #2 catalogue results after 2906 ms
new-2 INDEX-READY #3 catalogue results after 2519 ms
START cold new-2         n=10 min=1630ms median=1763ms max=1984ms  all=[1950, 1774, 1984, 1684, 1757, 1769, 1701, 1678, 1630, 1780]  states=['COLD']
START warm new-2         n=10 min=278ms median=472ms max=793ms  all=[578, 470, 373, 713, 378, 732, 793, 474, 278, 382]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.30    n=20 min=1456ms median=1769ms max=2143ms  all=[2143, 2040, 1731, 1901, 1456, 1640, 1769, 1698, 1794, 1876, 1950, 1774, 1984, 1684, 1757, 1769, 1701, 1678, 1630, 1780]  states=['COLD']
START cold BASE 1.2.29   n=20 min=1397ms median=1700ms max=2365ms  all=[1712, 1875, 1610, 1815, 1460, 1549, 1397, 1887, 1556, 1819, 1905, 1932, 1563, 2365, 1688, 1837, 1973, 1654, 1560, 1686]  states=['COLD']
START warm NEW 1.2.30    n=20 min=278ms median=620ms max=879ms  all=[879, 741, 682, 693, 490, 650, 704, 590, 811, 488, 578, 470, 373, 713, 378, 732, 793, 474, 278, 382]  states=['HOT']
START warm BASE 1.2.29   n=20 min=280ms median=540ms max=1144ms  all=[375, 325, 925, 393, 280, 579, 561, 771, 399, 537, 845, 1144, 518, 599, 573, 543, 455, 476, 558, 303]  states=['HOT']
START catalogue first-install new n=2 min=2719ms median=2762ms max=2804ms  all=[2719, 2804]  states=[]
START catalogue index-ready new n=6 min=2519ms median=2758ms max=2906ms  all=[2566, 2702, 2813, 2890, 2906, 2519]  states=[]
START catalogue first-install base n=2 min=2535ms median=2632ms max=2728ms  all=[2535, 2728]  states=[]
START catalogue index-ready base n=6 min=2576ms median=2656ms max=2795ms  all=[2795, 2769, 2576, 2637, 2636, 2676]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['4828', '5417', '5696', '5961', '6687', '7052', '7258', '7439', '7504', '7560', '7613', '7681', '7735', '7797', '8261', '8759', '8899', '9044', '9212', '9289', '9359', '9412', '9466', '9519', '9575', '9629', '9687', '9740', '10164', '10450', '10591', '10736', '10890', '10957', '11032', '11084', '11171', '11236', '11306', '11360', '11416', '11471', '11960', '12480', '12623', '12767', '12919', '12985', '13041', '13098', '13151', '13205', '13258', '13310', '13363', '13415', '13803', '14083', '14225', '14372', '14519', '14585', '14641', '14695', '14750', '14802', '14857', '14911', '14965', '15020']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 98
   skipped-frames events=98 max=88 total=4652
== Davey: 113
   davey events=113 max=2398ms median=889ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): base-0 new-1 base-1 new-2 base-2
## AB base-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-0: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-0 FIRST-INSTALL catalogue results after 2674 ms
base-0 INDEX-READY #1 catalogue results after 2307 ms
base-0 INDEX-READY #2 catalogue results after 2265 ms
base-0 INDEX-READY #3 catalogue results after 2454 ms
START cold base-0        n=10 min=881ms median=1060ms max=1349ms  all=[1299, 881, 1090, 976, 908, 1269, 1029, 1206, 1007, 1349]  states=['COLD']
START warm base-0        n=10 min=159ms median=340ms max=513ms  all=[361, 513, 159, 368, 318, 222, 418, 207, 363, 292]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2338 ms
new-1 INDEX-READY #1 catalogue results after 2581 ms
new-1 INDEX-READY #2 catalogue results after 2386 ms
new-1 INDEX-READY #3 catalogue results after 2357 ms
START cold new-1         n=10 min=1083ms median=1185ms max=1382ms  all=[1171, 1186, 1156, 1083, 1290, 1184, 1142, 1227, 1382, 1238]  states=['COLD']
START warm new-1         n=10 min=160ms median=358ms max=578ms  all=[361, 317, 512, 411, 287, 578, 212, 355, 475, 160]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2439 ms
base-1 INDEX-READY #1 catalogue results after 2564 ms
base-1 INDEX-READY #2 catalogue results after 2524 ms
base-1 INDEX-READY #3 catalogue results after 2307 ms
START cold base-1        n=10 min=1154ms median=1258ms max=1351ms  all=[1338, 1351, 1200, 1298, 1226, 1347, 1166, 1289, 1154, 1187]  states=['COLD']
START warm base-1        n=10 min=136ms median=331ms max=532ms  all=[266, 320, 532, 296, 352, 342, 394, 296, 136, 487]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2352 ms
new-2 INDEX-READY #1 catalogue results after 2585 ms
new-2 INDEX-READY #2 catalogue results after 2410 ms
new-2 INDEX-READY #3 catalogue results after 2440 ms
START cold new-2         n=10 min=916ms median=1156ms max=1402ms  all=[1090, 916, 1147, 1182, 1088, 1166, 1402, 1287, 1044, 1262]  states=['COLD']
START warm new-2         n=10 min=160ms median=284ms max=570ms  all=[279, 319, 249, 395, 288, 160, 218, 570, 167, 368]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2455 ms
base-2 INDEX-READY #1 catalogue results after 2649 ms
base-2 INDEX-READY #2 catalogue results after 2398 ms
base-2 INDEX-READY #3 catalogue results after 2377 ms
START cold base-2        n=10 min=1023ms median=1193ms max=1286ms  all=[1044, 1173, 1213, 1023, 1153, 1223, 1265, 1262, 1286, 1055]  states=['COLD']
START warm base-2        n=10 min=185ms median=318ms max=601ms  all=[228, 601, 453, 328, 307, 251, 438, 367, 185, 229]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.30    n=20 min=916ms median=1176ms max=1402ms  all=[1171, 1186, 1156, 1083, 1290, 1184, 1142, 1227, 1382, 1238, 1090, 916, 1147, 1182, 1088, 1166, 1402, 1287, 1044, 1262]  states=['COLD']
START cold BASE 1.2.29   n=20 min=1023ms median=1218ms max=1351ms  all=[1338, 1351, 1200, 1298, 1226, 1347, 1166, 1289, 1154, 1187, 1044, 1173, 1213, 1023, 1153, 1223, 1265, 1262, 1286, 1055]  states=['COLD']
START warm NEW 1.2.30    n=20 min=160ms median=318ms max=578ms  all=[361, 317, 512, 411, 287, 578, 212, 355, 475, 160, 279, 319, 249, 395, 288, 160, 218, 570, 167, 368]  states=['HOT']
START warm BASE 1.2.29   n=20 min=136ms median=324ms max=601ms  all=[266, 320, 532, 296, 352, 342, 394, 296, 136, 487, 228, 601, 453, 328, 307, 251, 438, 367, 185, 229]  states=['HOT']
START catalogue first-install new n=2 min=2338ms median=2345ms max=2352ms  all=[2338, 2352]  states=[]
START catalogue index-ready new n=6 min=2357ms median=2425ms max=2585ms  all=[2581, 2386, 2357, 2585, 2410, 2440]  states=[]
START catalogue first-install base n=2 min=2439ms median=2447ms max=2455ms  all=[2439, 2455]  states=[]
START catalogue index-ready base n=6 min=2307ms median=2461ms max=2649ms  all=[2564, 2524, 2307, 2649, 2398, 2377]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['6091', '7302', '7605', '7764', '7907', '8074', '8129', '8190', '8246', '8332', '8386', '8453', '8505', '8559', '8984', '9259', '9418', '9581', '9738', '9815', '9870', '9924', '9984', '10040', '10093', '10153', '10298', '10375', '10773', '11038', '11182', '11329', '11473', '11561', '11616', '11669', '11727', '11790', '11841', '11896', '11951', '12003', '12526', '12848', '12988', '13133', '13276', '13350', '13407', '13463', '13569', '13624', '13746', '13818', '13887', '13941', '14339', '14596', '14740', '14885', '15031', '15104', '15156', '15210', '15267', '15320', '15376', '15430', '15485', '15607']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 20
   skipped-frames events=20 max=48 total=704
== Davey: 16
   davey events=16 max=1046ms median=806ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## emoji26

```
part emoji26; APK: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430 bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: 
camera permission: 
screen 1080x2400
## M0. Site data (host): texts with emoji
SITE news.json: 25 items; keys of the first: ['author', 'category', 'content', 'cover', 'date', 'excerpt', 'id', 'tags', 'title']
SITE events: 75, with a place: 75; places: ['НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец']
EMOJI news id=news-svetoven-den-na-hlyaba url=/news/svetoven-den-na-hlyaba 🍞🥖🫓 'Световен ден на хляба! 🍞🫓🥖'
EMOJI news id=news-obuchenie-po-narodni-tantsi url=/news/obuchenie-po-narodni-tantsi 🕺 'Обучение по народни танци'
EMOJI news id=news-mvbb0tp9hi2d url=None ❤️🍞🥖🫓 'Световен ден на хляба! 🍞🫓🥖'
EMOJI news id=news-mt5mv5hvwdzg url=None 🇧🇬 'Танцовият клуб участие във VW Fest Узана'
EMOJI news id=news-msuodcr0yrmu url=None 🔹🕺 'Обучение по народни танци'
GMS on the image: versionName=20.24.14; emoji font provider (androidx.content.action.LOAD_EMOJI_FONT): 0 match(es); app EmojiCompatInitializer in the APK: 1
## W. First launch and warm-up
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
first-install catalogue results after 1815 ms
## M1. Texts with emoji on API 26
EMOJI 1 (news news-svetoven-den-na-hlyaba 'Световен ден на хляба! 🍞🫓🥖'): Световен ден на хляба! 🍞🫓🥖 | Световен ден на хляба! 🍞🫓🥖 | От сърце ❤️ благодарим на всички, които се включиха в празника със своите домашно приготвени погачи!
EMOJI 2 (news news-obuchenie-po-narodni-tantsi 'Обучение по народни танци'): Когато две села се хванат на едно хоро – Яворец и Батошево заедно в танца 🕺 Онази обява (за 16 май, 15:00 ч., с гост-лек
EMOJI 3: 'Световен ден на хляба! 🍞🫓🥖' — no URL and no Latin word to search for
EMOJI 4 (news news-mt5mv5hvwdzg 'Танцовият клуб участие във VW Fest Узана'): Под ръководството на Ради Мирчев танцьорите представиха български народни танци и зарадваха публиката със своите изпълне
EMOJI 5: 'Обучение по народни танци' — no URL and no Latin word to search for
## M1b. The bread article again after 30 s (EmojiCompat loads its font asynchronously)
logcat EmojiCompat/fonts:   5477  5477 I PERFCHECK: section: M1b. The bread article again after 30 s EmojiCompat loads its font asynchronously~
## M2. Cold starts (10), API 26
START cold API 26        n=10 min=759ms median=808ms max=870ms  all=[784, 870, 778, 855, 759, 839, 827, 796, 785, 819]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['4955', '5527', '5617', '5662', '5719', '5764', '5818', '5865', '5970', '6017', '6073', '6120']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## emoji28

```
part emoji28; APK: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: 
camera permission: 
screen 1080x2400
## M0. Site data (host): texts with emoji
SITE news.json: 25 items; keys of the first: ['author', 'category', 'content', 'cover', 'date', 'excerpt', 'id', 'tags', 'title']
SITE events: 75, with a place: 75; places: ['НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец']
EMOJI news id=news-svetoven-den-na-hlyaba url=/news/svetoven-den-na-hlyaba 🍞🥖🫓 'Световен ден на хляба! 🍞🫓🥖'
EMOJI news id=news-obuchenie-po-narodni-tantsi url=/news/obuchenie-po-narodni-tantsi 🕺 'Обучение по народни танци'
EMOJI news id=news-mvbb0tp9hi2d url=None ❤️🍞🥖🫓 'Световен ден на хляба! 🍞🫓🥖'
EMOJI news id=news-mt5mv5hvwdzg url=None 🇧🇬 'Танцовият клуб участие във VW Fest Узана'
EMOJI news id=news-msuodcr0yrmu url=None 🔹🕺 'Обучение по народни танци'
GMS on the image: versionName=20.24.14; emoji font provider (androidx.content.action.LOAD_EMOJI_FONT): 0 match(es); app EmojiCompatInitializer in the APK: 1
## W. First launch and warm-up
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
first-install catalogue results after 2060 ms
## M1. Texts with emoji on API 28
EMOJI 1 (news news-svetoven-den-na-hlyaba 'Световен ден на хляба! 🍞🫓🥖'): Световен ден на хляба! 🍞🫓🥖 | Световен ден на хляба! 🍞🫓🥖 | От сърце ❤️ благодарим на всички, които се включиха в празника със своите домашно приготвени погачи!
EMOJI 2 (news news-obuchenie-po-narodni-tantsi 'Обучение по народни танци'): Когато две села се хванат на едно хоро – Яворец и Батошево заедно в танца 🕺 Онази обява (за 16 май, 15:00 ч., с гост-лек
EMOJI 3: 'Световен ден на хляба! 🍞🫓🥖' — no URL and no Latin word to search for
EMOJI 4 (news news-mt5mv5hvwdzg 'Танцовият клуб участие във VW Fest Узана'): 
EMOJI 5: 'Обучение по народни танци' — no URL and no Latin word to search for
## M1b. The bread article again after 30 s (EmojiCompat loads its font asynchronously)
logcat EmojiCompat/fonts:   6162  6162 I PERFCHECK: section: M1b. The bread article again after 30 s EmojiCompat loads its font asynchronously~
## M2. Cold starts (10), API 28
START cold API 28        n=10 min=1108ms median=1176ms max=1654ms  all=[1152, 1336, 1251, 1206, 1654, 1199, 1129, 1112, 1126, 1108]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5530', '6215', '6302', '6351', '6409', '6453', '6505', '6550', '6601', '6642', '6691', '6733']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=35 total=188
== Davey: 2
   davey events=2 max=780ms median=742ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## sizediff

```
== release-config APKs: new 4497441 B, base 4096702 B
TOTAL compressed entries: new 4446797 B, base 4072648 B, delta +374149 B
ARSC resources by type (new/base/delta): attr 282/22/+260, style 204/25/+179, drawable 117/34/+83, string 826/770/+56, dimen 53/5/+48, color 54/23/+31, layout 35/6/+29, anim 16/0/+16, interpolator 7/0/+7, array 4/0/+4, integer 5/3/+2, bool 4/3/+1
ARSC string configurations: new 2, base 2; only in new: []
RES files, top growth by resource-name prefix (compressed B, new/base):
    +81274  drawable/abc_*  (81607/333)
    +13155  layout/abc_*  (13155/0)
     +8704  drawable/fingerprint_*  (8704/0)
     +6032  anim/btn_*  (6032/0)
     +4261  drawable/btn_*  (4261/0)
     +2466  color/abc_*  (2466/0)
     +1270  anim/abc_*  (1270/0)
     +1237  layout/select_*  (1237/0)
     +1134  interpolator/btn_*  (1134/0)
      +700  layout/fingerprint_*  (700/0)
      +268  layout/support_*  (268/0)
      +227  color/switch_*  (227/0)
      +224  drawable/tooltip_*  (224/0)
      +214  interpolator/fast_*  (214/0)
        +1  xml/widget_*  (396/395)
RES resources with files only in new: 145; sample: ['anim/abc_grow_fade_in_from_bottom', 'anim/abc_popup_enter', 'anim/abc_popup_exit', 'anim/abc_shrink_fade_out_from_bottom', 'anim/btn_checkbox_to_checked_box_inner_merged_animation', 'anim/btn_checkbox_to_checked_box_outer_merged_animation', 'anim/btn_checkbox_to_checked_icon_null_animation', 'anim/btn_checkbox_to_unchecked_box_inner_merged_animation', 'anim/btn_checkbox_to_unchecked_check_path_merged_animation', 'anim/btn_checkbox_to_unchecked_icon_null_animation', 'anim/btn_radio_to_off_mtrl_dot_group_animation', 'anim/btn_radio_to_off_mtrl_ring_outer_animation', 'anim/btn_radio_to_off_mtrl_ring_outer_path_animation', 'anim/btn_radio_to_on_mtrl_dot_group_animation', 'anim/btn_radio_to_on_mtrl_ring_outer_animation', 'anim/btn_radio_to_on_mtrl_ring_outer_path_animation', 'color/abc_color_highlight_material', 'color/abc_primary_text_material_light', 'color/abc_search_url_text', 'color/abc_tint_btn_checkable', 'color/abc_tint_default', 'color/abc_tint_edittext', 'color/abc_tint_seek_thumb', 'color/abc_tint_spinner', 'color/abc_tint_switch_track']
DEX classes (mapping.txt): new 8717, base 8358; top growth by package:
    +132  androidx.appcompat.widget  (135/3)
     +49  androidx.appcompat.view  (49/0)
     +40  org.chyavorec.app  (948/908)
     +32  androidx.appcompat.app  (32/0)
     +28  androidx.core.view  (122/94)
     +16  androidx.core.widget  (16/0)
     +11  androidx.emoji2.viewsintegration  (11/0)
      +5  androidx.activity.compose  (23/18)
      +4  androidx.core.text  (4/0)
      +4  androidx.core.app  (36/32)
      +3  androidx.customview.view  (3/0)
      +2  androidx.emoji2.text  (38/36)
      +2  androidx.activity.result  (15/13)
      +1  org.jsoup.select  (37/36)
      +1  org.jsoup.helper  (7/6)
DEX size by package (depth 2, apkanalyzer, bytes), top growth:
   +299022  <TOTAL>  (5679994/5380972)
   +196420  androidx.appcompat  (197339/919)
    +37394  org.chyavorec  (1182874/1145480)
    +21552  androidx.core  (132908/111356)
    +14547  kotlin.math  (24423/9876)
    +13937  androidx.biometric  (13937/0)
    +12155  kotlin.jvm  (52790/40635)
    +10001  org.jsoup  (147776/137775)
     +9222  androidx.room  (62720/53498)
     +7138  coil3.network  (25246/18108)
     +7087  androidx.emoji2  (15695/8608)
     +5531  _COROUTINE  (6796/1265)
DEX size by package (depth 3, apkanalyzer, bytes), top growth:
   +299022  <TOTAL>  (5679994/5380972)
   +123852  androidx.appcompat.widget  (124771/919)
    +41231  androidx.appcompat.view  (41231/0)
    +35715  org.chyavorec.app  (822624/786909)
    +29116  androidx.appcompat.app  (29116/0)
    +19680  androidx.core.widget  (19680/0)
    +15850  org.jsoup.helper  (18773/2923)
    +14547  kotlin.math  (24423/9876)
    +13937  androidx.biometric  (13937/0)
    +13027  androidx.compose.ui  (594976/581949)
    +12152  kotlin.jvm.internal  (51700/39548)
     +5904  androidx.room.concurrent  (6182/278)
## COMPLETE
```

## upd33

```
part upd33; APK: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430 bytes
host: 4 cpu, 15987 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## R0. The v1.2.30 code as 1.2.30-pre (versionCode 1488542, 255fdc6) → the test v1.2.30; mobile data only, throttled
staged: 1488543 1.2.30 4505177
installed new-upd: 1.2.30-pre 255fdc6 (v1.2.30 code); versionCode 1488542; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v130b/update/41353351352/update.json size 4505145;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.30-pre 
THROTTLE on: -P INPUT ACCEPT -A INPUT -p tcp -m tcp --sport 443 -m hashlimit --hashlimit-above 64kb/s --hashlimit-burst 64kb --hashlimit-mode srcip --hashlimit-name pcdl -j DROP -A INPUT -j bw_INPUT 
## R1. Plain download: 'Изтегли и инсталирай', Home at +1 s → finishes in the background
r1-plain: progress notification at +4275 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 0%) text=null progress=Integer (0)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False actions=['Отказ']
r1-plain +25302 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 21%) text=null progress=Integer (21)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [1000576 2026-11-01 21:59 update-1488543.apk.part;]
r1-plain +50377 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 45%) text=null progress=Integer (45)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [2027621 2026-11-01 21:59 update-1488543.apk.part;]
r1-plain +75465 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 66%) text=null progress=Integer (66)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [3054592 2026-11-01 22:00 update-1488543.apk.part;]
r1-plain +100517 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 89%) text=null progress=Integer (89)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [4016101 2026-11-01 22:00 update-1488543.apk.part;]
r1-plain RESULT: ready after 117080 ms (first progress notification at +4275 ms); app process '5433'; files [4505177 2026-11-01 22:00 update-1488543.apk;]
## R2. Download → 'Отказ' from the notification → 'Изтегли и инсталирай' again, Home at +1 s
r2 shade: 
r2 after 'Отказ': NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +23005 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +47561 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +72010 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +96457 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +120908 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +145353 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +169798 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +194244 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +218689 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +243125 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +267570 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s RESULT: none after 287880 ms (first progress notification at +never ms); app process '7725'; files []
r2 logcat (app/FGS): 
## R3. The same, Home at +17 s
r3 shade: text="Изтегляне на версия 1.2.30 — 10%" text="Отказ" 
r3 after 'Отказ': NOTIF total for org.chyavorec.app: 0; files []
r3 in the app +6014 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3 in the app +12073 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3 in the app +18145 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3 in the app +24251 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3-home-17s +47311 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +71751 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +96193 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +120637 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +145074 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +169514 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +194073 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +218514 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +242967 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +267412 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +291857 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s RESULT: none after 312167 ms (first progress notification at +never ms); app process '8487'; files []
## R4. 'готова за инсталиране' → installer → v1.2.30 and 'Какво ново'
THROTTLE off: 0 rules left
no 'готова за инсталиране' in the shade: text="On" content-desc="Internet" text="Internet" text="On" content-desc="Bluetooth." text="Bluetooth" text="Flashlight" text="Off" content-desc="Do Not Disturb." text="Do Not Disturb" 
## COMPLETE
## logcat findings
app pids seen: ['5433', '7725', '8487']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 21
   skipped-frames events=21 max=93 total=1002
== Davey: 29
   davey events=29 max=2184ms median=922ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## upd34

```
part upd34; APK: 1.2.30 255fdc6 (Обновяване и емоджи: поправки след проверката на емулатор); versionCode 1488543; x86_64 libs: 2 size 4505177 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104430 bytes
host: 4 cpu, 15982 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## R0. The v1.2.30 code as 1.2.30-pre (versionCode 1488542, 255fdc6) → the test v1.2.30; mobile data only, throttled
staged: 1488543 1.2.30 4505177
installed new-upd: 1.2.30-pre 255fdc6 (v1.2.30 code); versionCode 1488542; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v130b/update/41353351352/update.json size 4505145;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.30-pre 
THROTTLE on: -P INPUT ACCEPT -A INPUT -p tcp -m tcp --sport 443 -m hashlimit --hashlimit-above 64kb/s --hashlimit-burst 64kb --hashlimit-mode srcip --hashlimit-name pcdl -j DROP -A INPUT -j bw_INPUT 
## R1. Plain download: 'Изтегли и инсталирай', Home at +1 s → finishes in the background
r1-plain: progress notification at +4716 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 0%) text=null progress=Integer (0)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False actions=['Отказ']
r1-plain +25852 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 23%) text=null progress=Integer (23)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [1064942 2026-11-01 21:58 update-1488543.apk.part;]
r1-plain +50853 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 44%) text=null progress=Integer (44)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [2012581 2026-11-01 21:59 update-1488543.apk.part;]
r1-plain +75426 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 65%) text=null progress=Integer (65)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [2962679 2026-11-01 21:59 update-1488543.apk.part;]
r1-plain +100148 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.30 — 86%) text=null progress=Integer (86)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False; files [3896558 2026-11-01 22:00 update-1488543.apk.part;]
r1-plain RESULT: ready after 116678 ms (first progress notification at +4716 ms); app process '5556'; files [4505177 2026-11-01 22:00 update-1488543.apk;]
## R2. Download → 'Отказ' from the notification → 'Изтегли и инсталирай' again, Home at +1 s
r2 shade: text="Изтегляне на версия 1.2.30 — 11%" text="Отказ" 
r2 after 'Отказ': NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +23516 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +48026 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +72535 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +97050 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +121554 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +146062 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +170580 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +195090 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +219608 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +244112 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s +268622 ms: NOTIF total for org.chyavorec.app: 0; files []
r2-home-1s RESULT: none after 288972 ms (first progress notification at +never ms); app process '6746'; files []
r2 logcat (app/FGS):    399  1099 D audioserver: FGS Logger Transaction failed~   399   508 D audioserver: FGS Logger Transaction failed~   525   542 V WindowManager:     info={id=15 t=OPEN f=0x0 trk=0 r=[0@Point(0, 0)] c=[{WCT{RemoteToken{3050abb Task{93442f #1 type=home}}} m=TO_FRONT f=SHOW_WALLPAPER|MOVE_TO_TOP lea~   525  7139 W ProcessStats: Tracking association SourceState{9927335 com.google.android.configupdater/10129 BFgs #13345} whose proc state 4 is better than process ProcessState{27363~   525  2326 W ProcessStats: Tracking association SourceState{b549ff5 com.google.android.gms.persistent/10131 BFgs #14087} whose proc state 4 is better than process ProcessState{210d~   525   619 D ActivityManager: freezing 6746 org.chyavorec.app~
## R3. The same, Home at +17 s
r3 shade: text="Изтегляне на версия 1.2.30 — 11%" text="Отказ" 
r3 after 'Отказ': NOTIF total for org.chyavorec.app: 0; files []
r3 in the app +6209 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3 in the app +12367 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3 in the app +18609 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3 in the app +24882 ms: text="Проверка за нова версия…" 'Отказ' no; notif: NOTIF total for org.chyavorec.app: 0
r3-home-17s +48667 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +73159 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +97655 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +122155 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +146651 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +171155 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +195654 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +220152 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +244649 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +269144 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s +293634 ms: NOTIF total for org.chyavorec.app: 0; files []
r3-home-17s RESULT: none after 313985 ms (first progress notification at +never ms); app process '7596'; files []
## R4. 'готова за инсталиране' → installer → v1.2.30 and 'Какво ново'
THROTTLE off: 0 rules left
no 'готова за инсталиране' in the shade: text="Internet, T-Mobile, 3G" content-desc="Internet" text="Internet" text="Bluetooth, On" content-desc="Bluetooth." text="Bluetooth" text="Flashlight, Camera in use" text="Flashlight" text="Do Not Di
## COMPLETE
## logcat findings
app pids seen: ['5556', '6746', '7596']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 14
   skipped-frames events=14 max=105 total=693
== Davey: 21
   davey events=21 max=2415ms median=904ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

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

### emoji26/shots/emoji

- [emoji26/shots/emoji/1-news.png](emoji26/shots/emoji/1-news.png)
- [emoji26/shots/emoji/1b-news-after-30s.png](emoji26/shots/emoji/1b-news-after-30s.png)
- [emoji26/shots/emoji/1c-news-second-process.png](emoji26/shots/emoji/1c-news-second-process.png)
- [emoji26/shots/emoji/2-news.png](emoji26/shots/emoji/2-news.png)
- [emoji26/shots/emoji/4-news.png](emoji26/shots/emoji/4-news.png)
- [emoji26/shots/emoji/9-events-list.png](emoji26/shots/emoji/9-events-list.png)

### emoji28/shots/emoji

- [emoji28/shots/emoji/1-news.png](emoji28/shots/emoji/1-news.png)
- [emoji28/shots/emoji/1b-news-after-30s.png](emoji28/shots/emoji/1b-news-after-30s.png)
- [emoji28/shots/emoji/1c-news-second-process.png](emoji28/shots/emoji/1c-news-second-process.png)
- [emoji28/shots/emoji/2-news.png](emoji28/shots/emoji/2-news.png)
- [emoji28/shots/emoji/4-news.png](emoji28/shots/emoji/4-news.png)
- [emoji28/shots/emoji/9-events-list.png](emoji28/shots/emoji/9-events-list.png)

### upd33/shots/update

- [upd33/shots/update/r2-dialog-again.png](upd33/shots/update/r2-dialog-again.png)
- [upd33/shots/update/r2-shade-downloading.png](upd33/shots/update/r2-shade-downloading.png)
- [upd33/shots/update/r3-dialog-again.png](upd33/shots/update/r3-dialog-again.png)
- [upd33/shots/update/r3-home-at-17s.png](upd33/shots/update/r3-home-at-17s.png)
- [upd33/shots/update/r3-shade-downloading.png](upd33/shots/update/r3-shade-downloading.png)
- [upd33/shots/update/r4-shade-ready.png](upd33/shots/update/r4-shade-ready.png)

### upd34/shots/update

- [upd34/shots/update/r2-dialog-again.png](upd34/shots/update/r2-dialog-again.png)
- [upd34/shots/update/r2-shade-downloading.png](upd34/shots/update/r2-shade-downloading.png)
- [upd34/shots/update/r3-dialog-again.png](upd34/shots/update/r3-dialog-again.png)
- [upd34/shots/update/r3-home-at-17s.png](upd34/shots/update/r3-home-at-17s.png)
- [upd34/shots/update/r3-shade-downloading.png](upd34/shots/update/r3-shade-downloading.png)
- [upd34/shots/update/r4-shade-ready.png](upd34/shots/update/r4-shade-ready.png)

## Other files

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
- [emoji26/host-after-run.txt](emoji26/host-after-run.txt)
- [emoji26/job-status.txt](emoji26/job-status.txt)
- [emoji26/logcat-all.txt](emoji26/logcat-all.txt)
- [emoji26/logcat-dump.txt](emoji26/logcat-dump.txt)
- [emoji26/logcat-findings.txt](emoji26/logcat-findings.txt)
- [emoji26/logcat-live.txt](emoji26/logcat-live.txt)
- [emoji26/site/emoji.json](emoji26/site/emoji.json)
- [emoji26/site/emoji.txt](emoji26/site/emoji.txt)
- [emoji26/site/events.json](emoji26/site/events.json)
- [emoji26/site/places.txt](emoji26/site/places.txt)
- [emoji26/start-cold.txt](emoji26/start-cold.txt)
- [emoji26/summary.txt](emoji26/summary.txt)
- [emoji28/host-after-run.txt](emoji28/host-after-run.txt)
- [emoji28/job-status.txt](emoji28/job-status.txt)
- [emoji28/logcat-all.txt](emoji28/logcat-all.txt)
- [emoji28/logcat-dump.txt](emoji28/logcat-dump.txt)
- [emoji28/logcat-findings.txt](emoji28/logcat-findings.txt)
- [emoji28/logcat-live.txt](emoji28/logcat-live.txt)
- [emoji28/site/emoji.json](emoji28/site/emoji.json)
- [emoji28/site/emoji.txt](emoji28/site/emoji.txt)
- [emoji28/site/events.json](emoji28/site/events.json)
- [emoji28/site/places.txt](emoji28/site/places.txt)
- [emoji28/start-cold.txt](emoji28/start-cold.txt)
- [emoji28/summary.txt](emoji28/summary.txt)
- [runs.txt](runs.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-new-upd-ref.txt](size/apk-new-upd-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/size-base.txt](size/size-base.txt)
- [size/size-new.txt](size/size-new.txt)
- [sizediff/dex-packages-base.txt](sizediff/dex-packages-base.txt)
- [sizediff/dex-packages-new.txt](sizediff/dex-packages-new.txt)
- [sizediff/files-base.txt](sizediff/files-base.txt)
- [sizediff/files-new.txt](sizediff/files-new.txt)
- [sizediff/resources-base.txt.gz](sizediff/resources-base.txt.gz)
- [sizediff/resources-new.txt.gz](sizediff/resources-new.txt.gz)
- [sizediff/summary.txt](sizediff/summary.txt)
- [upd33/host-after-run.txt](upd33/host-after-run.txt)
- [upd33/job-status.txt](upd33/job-status.txt)
- [upd33/logcat-all.txt](upd33/logcat-all.txt)
- [upd33/logcat-dump.txt](upd33/logcat-dump.txt)
- [upd33/logcat-findings.txt](upd33/logcat-findings.txt)
- [upd33/logcat-live.txt](upd33/logcat-live.txt)
- [upd33/notif-r1-plain.txt](upd33/notif-r1-plain.txt)
- [upd33/notif-r2-c.txt](upd33/notif-r2-c.txt)
- [upd33/notif-r2-home-1s.txt](upd33/notif-r2-home-1s.txt)
- [upd33/notif-r3-c.txt](upd33/notif-r3-c.txt)
- [upd33/notif-r3-home-17s.txt](upd33/notif-r3-home-17s.txt)
- [upd33/notif-r3-in-1.txt](upd33/notif-r3-in-1.txt)
- [upd33/notif-r3-in-2.txt](upd33/notif-r3-in-2.txt)
- [upd33/notif-r3-in-3.txt](upd33/notif-r3-in-3.txt)
- [upd33/notif-r3-in-4.txt](upd33/notif-r3-in-4.txt)
- [upd33/r1-plain-n.txt](upd33/r1-plain-n.txt)
- [upd33/r2-home-1s-n.txt](upd33/r2-home-1s-n.txt)
- [upd33/r3-home-17s-n.txt](upd33/r3-home-17s-n.txt)
- [upd33/summary.txt](upd33/summary.txt)
- [upd33/update.json](upd33/update.json)
- [upd34/host-after-run.txt](upd34/host-after-run.txt)
- [upd34/job-status.txt](upd34/job-status.txt)
- [upd34/logcat-all.txt](upd34/logcat-all.txt)
- [upd34/logcat-dump.txt](upd34/logcat-dump.txt)
- [upd34/logcat-findings.txt](upd34/logcat-findings.txt)
- [upd34/logcat-live.txt](upd34/logcat-live.txt)
- [upd34/notif-r1-plain.txt](upd34/notif-r1-plain.txt)
- [upd34/notif-r2-c.txt](upd34/notif-r2-c.txt)
- [upd34/notif-r2-home-1s.txt](upd34/notif-r2-home-1s.txt)
- [upd34/notif-r3-c.txt](upd34/notif-r3-c.txt)
- [upd34/notif-r3-home-17s.txt](upd34/notif-r3-home-17s.txt)
- [upd34/notif-r3-in-1.txt](upd34/notif-r3-in-1.txt)
- [upd34/notif-r3-in-2.txt](upd34/notif-r3-in-2.txt)
- [upd34/notif-r3-in-3.txt](upd34/notif-r3-in-3.txt)
- [upd34/notif-r3-in-4.txt](upd34/notif-r3-in-4.txt)
- [upd34/r1-plain-n.txt](upd34/r1-plain-n.txt)
- [upd34/r2-home-1s-n.txt](upd34/r2-home-1s-n.txt)
- [upd34/r3-home-17s-n.txt](upd34/r3-home-17s-n.txt)
- [upd34/summary.txt](upd34/summary.txt)
- [upd34/update.json](upd34/update.json)
- [update/41349572866/none.json](update/41349572866/none.json)
- [update/41349572866/notes.bg.txt](update/41349572866/notes.bg.txt)
- [update/41349572866/notes.en.txt](update/41349572866/notes.en.txt)
- [update/41349572866/update.json](update/41349572866/update.json)
- [update/41353351352/none.json](update/41353351352/none.json)
- [update/41353351352/notes.bg.txt](update/41353351352/notes.bg.txt)
- [update/41353351352/notes.en.txt](update/41353351352/notes.en.txt)
- [update/41353351352/update.json](update/41353351352/update.json)
