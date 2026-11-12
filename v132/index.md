# Wave 18 emulator check: test v1.2.32 (c1b5ef5) vs v1.2.31 (6f44326)

Emulator run 41884180998 (debug/wave18-check @ f0e3585); API 33 google_apis x86_64, -gpu swangle_indirect. Previous release check: [../v130/index.md](../v130/index.md).

```
== new 1.2.32 c1b5ef5: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4143918 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488545  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.32" (Raw: "1.2.32")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.USE_BIOMETRIC' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.FOREGROUND_SERVICE' 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
foreground services: 
part                    compressed  uncompressed  files
classes*.dex               3386710       6698144      2
res                         468365        832408     57
resources.arsc              224392        224392      1
assets/                      18844         18844      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5463         23760      1
META-INF/                      846          1288      6
== base 1.2.31 6f44326: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4130922 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488544  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.31" (Raw: "1.2.31")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.USE_BIOMETRIC' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.FOREGROUND_SERVICE' 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
foreground services: 
part                    compressed  uncompressed  files
classes*.dex               3374660       6672036      2
res                         468365        832408     57
resources.arsc              222432        222432      1
assets/                      19854         19854      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5464         23760      1
META-INF/                      846          1288      6
```

## abstart-1

```
part abstart-1; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-0 FIRST-INSTALL catalogue results after 3232 ms
new-0 INDEX-READY #1 catalogue results after 4349 ms
new-0 INDEX-READY #2 catalogue results after 4858 ms
new-0 INDEX-READY #3 catalogue results after 4145 ms
START cold new-0         n=10 min=2365ms median=2755ms max=3342ms  all=[2849, 3011, 3342, 3036, 2661, 2365, 2576, 3099, 2640, 2398]  states=['COLD']
START warm new-0         n=10 min=380ms median=938ms max=1232ms  all=[1232, 1122, 627, 897, 1031, 932, 983, 768, 380, 944]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2839 ms
base-1 INDEX-READY #1 catalogue results after 2690 ms
base-1 INDEX-READY #2 catalogue results after 2857 ms
base-1 INDEX-READY #3 catalogue results after 2949 ms
START cold base-1        n=10 min=1987ms median=2244ms max=2410ms  all=[2390, 2237, 2410, 2367, 2085, 2020, 2252, 2342, 2140, 1987]  states=['COLD']
START warm base-1        n=10 min=484ms median=847ms max=1073ms  all=[873, 710, 1073, 821, 729, 484, 698, 1072, 987, 885]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2797 ms
new-1 INDEX-READY #1 catalogue results after 3270 ms
new-1 INDEX-READY #2 catalogue results after 2825 ms
new-1 INDEX-READY #3 catalogue results after 2718 ms
START cold new-1         n=10 min=1988ms median=2283ms max=2750ms  all=[2315, 2072, 2750, 2470, 2516, 2251, 2135, 1988, 2575, 2162]  states=['COLD']
START warm new-1         n=10 min=370ms median=663ms max=1046ms  all=[692, 523, 713, 634, 493, 698, 370, 940, 565, 1046]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2862 ms
base-2 INDEX-READY #1 catalogue results after 3279 ms
base-2 INDEX-READY #2 catalogue results after 2533 ms
base-2 INDEX-READY #3 catalogue results after 2891 ms
START cold base-2        n=10 min=1844ms median=2272ms max=2851ms  all=[2851, 2139, 2355, 1844, 2773, 2108, 2188, 2615, 2400, 2085]  states=['COLD']
START warm base-2        n=10 min=409ms median=897ms max=942ms  all=[925, 942, 409, 586, 641, 929, 888, 819, 906, 913]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2816 ms
new-2 INDEX-READY #1 catalogue results after 2858 ms
new-2 INDEX-READY #2 catalogue results after 2682 ms
new-2 INDEX-READY #3 catalogue results after 3408 ms
START cold new-2         n=10 min=1782ms median=2336ms max=3040ms  all=[2346, 2326, 2312, 2590, 2467, 2078, 2443, 3040, 2120, 1782]  states=['COLD']
START warm new-2         n=10 min=385ms median=729ms max=1248ms  all=[1248, 534, 385, 862, 809, 994, 799, 580, 659, 524]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.32    n=20 min=1782ms median=2320ms max=3040ms  all=[2315, 2072, 2750, 2470, 2516, 2251, 2135, 1988, 2575, 2162, 2346, 2326, 2312, 2590, 2467, 2078, 2443, 3040, 2120, 1782]  states=['COLD']
START cold BASE 1.2.31   n=20 min=1844ms median=2244ms max=2851ms  all=[2390, 2237, 2410, 2367, 2085, 2020, 2252, 2342, 2140, 1987, 2851, 2139, 2355, 1844, 2773, 2108, 2188, 2615, 2400, 2085]  states=['COLD']
START warm NEW 1.2.32    n=20 min=370ms median=676ms max=1248ms  all=[692, 523, 713, 634, 493, 698, 370, 940, 565, 1046, 1248, 534, 385, 862, 809, 994, 799, 580, 659, 524]  states=['HOT']
START warm BASE 1.2.31   n=20 min=409ms median=879ms max=1073ms  all=[873, 710, 1073, 821, 729, 484, 698, 1072, 987, 885, 925, 942, 409, 586, 641, 929, 888, 819, 906, 913]  states=['HOT']
START catalogue first-install new n=2 min=2797ms median=2806ms max=2816ms  all=[2797, 2816]  states=[]
START catalogue index-ready new n=6 min=2682ms median=2842ms max=3408ms  all=[3270, 2825, 2718, 2858, 2682, 3408]  states=[]
START catalogue first-install base n=2 min=2839ms median=2850ms max=2862ms  all=[2839, 2862]  states=[]
START catalogue index-ready base n=6 min=2533ms median=2874ms max=3279ms  all=[2690, 2857, 2949, 3279, 2533, 2891]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['4782', '5787', '6008', '6446', '7069', '7217', '7330', '7655', '7804', '7878', '7932', '8042', '8196', '8386', '8964', '9239', '9396', '9555', '9703', '9774', '9829', '9882', '9937', '9995', '10050', '10116', '10175', '10230', '10855', '11127', '11277', '11430', '11584', '11654', '11710', '11763', '11817', '11872', '11926', '11980', '12036', '12091', '12631', '12957', '13105', '13246', '13395', '13463', '13516', '13571', '13632', '13688', '13743', '13796', '13849', '13902', '14300', '14589', '14730', '14875', '15028', '15111', '15170', '15224', '15278', '15333', '15388', '15442', '15497', '15553']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 152
   skipped-frames events=152 max=113 total=7549
== Davey: 196
   davey events=196 max=2580ms median=904ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): base-0 new-1 base-1 new-2 base-2
## AB base-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-0: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-0 FIRST-INSTALL catalogue results after 2960 ms
base-0 INDEX-READY #1 catalogue results after 3404 ms
base-0 INDEX-READY #2 catalogue results after 3257 ms
base-0 INDEX-READY #3 catalogue results after 2807 ms
START cold base-0        n=10 min=1672ms median=1986ms max=2925ms  all=[2925, 2093, 2351, 2188, 1878, 2585, 1672, 1753, 1813, 1729]  states=['COLD']
START warm base-0        n=10 min=331ms median=583ms max=869ms  all=[752, 553, 564, 869, 434, 602, 617, 331, 455, 627]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2763 ms
new-1 INDEX-READY #1 catalogue results after 2747 ms
new-1 INDEX-READY #2 catalogue results after 2741 ms
new-1 INDEX-READY #3 catalogue results after 2701 ms
START cold new-1         n=10 min=1492ms median=1709ms max=2248ms  all=[1525, 2248, 1898, 1821, 1713, 1599, 1492, 1865, 1705, 1695]  states=['COLD']
START warm new-1         n=10 min=453ms median=574ms max=1042ms  all=[711, 556, 519, 658, 518, 1042, 593, 453, 464, 622]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2724 ms
base-1 INDEX-READY #1 catalogue results after 2544 ms
base-1 INDEX-READY #2 catalogue results after 2695 ms
base-1 INDEX-READY #3 catalogue results after 2694 ms
START cold base-1        n=10 min=1436ms median=1752ms max=1968ms  all=[1881, 1810, 1606, 1467, 1664, 1965, 1968, 1695, 1436, 1861]  states=['COLD']
START warm base-1        n=10 min=242ms median=466ms max=885ms  all=[645, 433, 362, 293, 885, 545, 242, 404, 498, 569]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2776 ms
new-2 INDEX-READY #1 catalogue results after 2744 ms
new-2 INDEX-READY #2 catalogue results after 2661 ms
new-2 INDEX-READY #3 catalogue results after 2552 ms
START cold new-2         n=10 min=1408ms median=1587ms max=2078ms  all=[1477, 1517, 1463, 2078, 1670, 1657, 1746, 1408, 1732, 1515]  states=['COLD']
START warm new-2         n=10 min=280ms median=503ms max=719ms  all=[674, 507, 719, 280, 617, 499, 597, 289, 474, 308]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2593 ms
base-2 INDEX-READY #1 catalogue results after 2610 ms
base-2 INDEX-READY #2 catalogue results after 2493 ms
base-2 INDEX-READY #3 catalogue results after 3029 ms
START cold base-2        n=10 min=1305ms median=1626ms max=2155ms  all=[2155, 1715, 1600, 1651, 1588, 1593, 1821, 1990, 1305, 1489]  states=['COLD']
START warm base-2        n=10 min=230ms median=460ms max=695ms  all=[695, 535, 638, 358, 571, 378, 529, 230, 390, 341]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.32    n=20 min=1408ms median=1682ms max=2248ms  all=[1525, 2248, 1898, 1821, 1713, 1599, 1492, 1865, 1705, 1695, 1477, 1517, 1463, 2078, 1670, 1657, 1746, 1408, 1732, 1515]  states=['COLD']
START cold BASE 1.2.31   n=20 min=1305ms median=1680ms max=2155ms  all=[1881, 1810, 1606, 1467, 1664, 1965, 1968, 1695, 1436, 1861, 2155, 1715, 1600, 1651, 1588, 1593, 1821, 1990, 1305, 1489]  states=['COLD']
START warm NEW 1.2.32    n=20 min=280ms median=538ms max=1042ms  all=[711, 556, 519, 658, 518, 1042, 593, 453, 464, 622, 674, 507, 719, 280, 617, 499, 597, 289, 474, 308]  states=['HOT']
START warm BASE 1.2.31   n=20 min=230ms median=466ms max=885ms  all=[645, 433, 362, 293, 885, 545, 242, 404, 498, 569, 695, 535, 638, 358, 571, 378, 529, 230, 390, 341]  states=['HOT']
START catalogue first-install new n=2 min=2763ms median=2770ms max=2776ms  all=[2763, 2776]  states=[]
START catalogue index-ready new n=6 min=2552ms median=2721ms max=2747ms  all=[2747, 2741, 2701, 2744, 2661, 2552]  states=[]
START catalogue first-install base n=2 min=2593ms median=2658ms max=2724ms  all=[2724, 2593]  states=[]
START catalogue index-ready base n=6 min=2493ms median=2652ms max=3029ms  all=[2544, 2695, 2694, 2610, 2493, 3029]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['4346', '5069', '5636', '6441', '6920', '7137', '7265', '7352', '7488', '7662', '7757', '7862', '7922', '7983', '8426', '8731', '8876', '9031', '9187', '9275', '9332', '9389', '9443', '9500', '9555', '9609', '9664', '9753', '10159', '10435', '10582', '10726', '10876', '10978', '11042', '11098', '11178', '11250', '11305', '11359', '11414', '11466', '12219', '12555', '12698', '12844', '12996', '13063', '13119', '13176', '13230', '13287', '13341', '13395', '13450', '13505', '13899', '14157', '14306', '14449', '14615', '14683', '14740', '14796', '14850', '14905', '14957', '15011', '15064', '15117']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 88
   skipped-frames events=88 max=87 total=3911
== Davey: 80
   davey events=80 max=1805ms median=858ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## drawn

```
part drawn; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## F0. Fresh install, first launch (onboarding: ReportDrawn)
DRAWN fresh-install (onboarding): Fully drawn org.chyavorec.app/.MainActivity: +4s240ms
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
catalogue results after 3273 ms
## F1. Cold starts online ×6 (Home: ReportDrawnWhen)
DRAWN online: TotalTime n=6 min=1412ms median=1767ms max=2276ms; fully drawn n=6 median=2328ms min=1767 max=2812 all=[2812, 2488, 2281, 2375, 1767, 1938]; without line: 0
## F2. Cold starts offline ×6 (cached news)
offline: airplane=1
DRAWN offline: TotalTime n=6 min=1276ms median=1342ms max=1522ms; fully drawn n=6 median=1584ms min=1362 max=1780 all=[1780, 1651, 1510, 1362, 1736, 1518]; without line: 0
## F3. Empty cache (Settings › Изчисти временните данни), then a cold start online ×4
cache cleared: snackbar ''; app cache dir: 96 KB
cache cleared: snackbar ''; app cache dir: 96 KB
cache cleared: snackbar ''; app cache dir: 96 KB
cache cleared: snackbar ''; app cache dir: 100 KB
DRAWN empty-online: TotalTime n=4 min=1307ms median=1402ms max=1643ms; fully drawn n=4 median=2078ms min=2024 max=2579 all=[2105, 2050, 2024, 2579]; without line: 0
## F4. Empty cache, then a cold start offline ×4 (nothing cached: the error/offline state)
cache cleared: snackbar ''; app cache dir: 100 KB
offline: airplane=1
cache cleared: snackbar ''; app cache dir: 100 KB
offline: airplane=1
cache cleared: snackbar ''; app cache dir: 108 KB
offline: airplane=1
cache cleared: snackbar ''; app cache dir: 108 KB
offline: airplane=1
DRAWN empty-offline: TotalTime n=4 min=1268ms median=1311ms max=1380ms; fully drawn n=4 median=1880ms min=1453 max=2294 all=[1873, 1453, 2294, 1888]; without line: 0
empty+offline Home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="Няма връзка" text="Провери интернет връзката си и опитай отн
## F5. After pm clear (onboarding again) ×2
DRAWN cleared: TotalTime n=2 min=1433ms median=1544ms max=1656ms; fully drawn n=2 median=1544ms min=1433 max=1656 all=[1656, 1433]; without line: 0
## F6. All 'Fully drawn' lines of the app in logcat
      1 Fully drawn org.chyavorec.app/.MainActivity: +4s240ms
      1 Fully drawn org.chyavorec.app/.MainActivity: +2s812ms
      1 Fully drawn org.chyavorec.app/.MainActivity: +2s579ms
      1 Fully drawn org.chyavorec.app/.MainActivity: +2s50ms
      1 Fully drawn org.chyavorec.app/.MainActivity: +2s488ms
## COMPLETE
## logcat findings
app pids seen: ['5736', '7712', '7862', '7988', '8182', '8273', '8354', '8510', '8578', '8641', '8704', '8766', '8825', '9421', '9828', '10193', '10756', '10870', '11347', '11597', '12087', '12382', '12936', '13227', '13693', '13968', '14138', '14262', '14309']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 18
   skipped-frames events=18 max=53 total=693
== Davey: 18
   davey events=18 max=1327ms median=826ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## hwprobe

```
part hwprobe; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## H1. Emulator graphics: renderer, gralloc, memtrack
GL: Sync configuration: [using: EGL_ANDROID_native_fence_sync] EGL implementation : 1.4 Android META-EGL 
props: [debug.hwui.renderer]: [skiagl] [ro.boot.debug.hwui.renderer]: [skiagl] [ro.boot.qemu.gltransport.drawFlushInterval]: [800] [ro.boot.qemu.gltransport.name]: [pipe] [ro.hardware.egl]: [emulation] [ro.hardware.vulkan]: [ranchu] [ro.opengles.version]: [196609] 
memtrack HAL: 147	memtrack.proxy: [android.hardware.memtrack.IMemtrack];
system: Total RAM: 2,013,352K (status normal); Free RAM: 887,126K ( 530,046K cached pss + 216,340K cached kernel + 140,740K free);DMA-BUF: 0K ( 0K mapped + 0K unmapped);DMA-BUF Heaps: 0K;DMA-BUF Heaps pool: 0K; GPU: 0K; Used RAM: 1,496,702K (1,167,854K used pss + 328,848K kernel); Lost RAM: 58,236K;
## H2. Helper probe: 25 bitmaps 1024×1024 each (4 MB as ARGB_8888, 2 MB as RGB_565) — config and where the memory shows
MEMROW helper-sw8888-before   PSS=23.8  Java=1.2  Native=11.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.1  RSS=92.4
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-sw8888-before: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 527408 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
PROBE sw8888: PCHW mode=sw8888 n=25 size=1024 ok=25 fail=0 config=ARGB_8888 allocBytes=104857600 kept=25 ms=371
MEMROW helper-sw8888-after    PSS=122.1  Java=1.2  Native=111.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.1  RSS=190.1
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-sw8888-after: pipeline: Skia (OpenGL); GPU total: 6406616 bytes, 6.11 MB (64.00 KB is purgeable); textures: 4.00 MB (1 entry) | 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 477280 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW helper-sw565-before    PSS=25.3  Java=1.2  Native=11.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.1  RSS=92.2
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-sw565-before: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 560844 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
PROBE sw565: PCHW mode=sw565 n=25 size=1024 ok=25 fail=0 config=RGB_565 allocBytes=52428800 kept=25 ms=207
MEMROW helper-sw565-after     PSS=73.5  Java=1.2  Native=61.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.1  RSS=138.9
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-sw565-after: pipeline: Skia (OpenGL); GPU total: 4309464 bytes, 4.11 MB (64.00 KB is purgeable); textures: 2.00 MB (1 entry) | 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 499412 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW helper-hw-before       PSS=30.1  Java=1.2  Native=11.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.1  RSS=90.9
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-hw-before: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 591472 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
PROBE hw: PCHW mode=hw n=25 size=1024 ok=25 fail=0 config=HARDWARE allocBytes=104857600 kept=25 ms=555
MEMROW helper-hw-after        PSS=125.3  Java=1.3  Native=115.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.1  RSS=176.4
  FDS total=146: /dev/goldfish_pipe×33; /dev/goldfish_address_space×28; socket×11; /dev/null×10; anon_inode:sync_file×5; anon_inode:eventfd×5; anon_inode:eventpoll×3; /sys/kernel/tracing/trace_marker×2; pipe×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/
  GFXMEM helper-hw-after: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 0.00 bytes (1 entry) | 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 554300 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW helper-dec8888-before  PSS=32.8  Java=1.3  Native=11.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.2  RSS=91.3
  FDS total=94: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×6; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-dec8888-before: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 597236 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
PROBE dec8888: PCHW mode=dec8888 n=25 size=1024 ok=25 fail=0 config=ARGB_8888 allocBytes=104857600 kept=25 ms=912
MEMROW helper-dec8888-after   PSS=235.3  Java=3.2  Native=211.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.2  RSS=294.0
  FDS total=94: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×6; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-dec8888-after: pipeline: Skia (OpenGL); GPU total: 6406616 bytes, 6.11 MB (4.06 MB is purgeable); textures: 4.00 MB (1 entry) | 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 436256 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW helper-dec565-before   PSS=30.6  Java=1.2  Native=11.3  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.2  RSS=91.5
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-dec565-before: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 573844 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
PROBE dec565: PCHW mode=dec565 n=25 size=1024 ok=25 fail=0 config=RGB_565 allocBytes=52428800 kept=25 ms=687
MEMROW helper-dec565-after    PSS=183.7  Java=3.2  Native=161.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.2  RSS=244.8
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-dec565-after: pipeline: Skia (OpenGL); GPU total: 4309464 bytes, 4.11 MB (2.06 MB is purgeable); textures: 2.00 MB (1 entry) | 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 501168 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW helper-dechw-before    PSS=30.7  Java=1.2  Native=11.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.2  RSS=91.6
  FDS total=93: socket×11; /dev/null×10; /dev/goldfish_pipe×7; anon_inode:sync_file×5; anon_inode:eventfd×4; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; pipe×2; anon_inode:eventpoll×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/fra
  GFXMEM helper-dechw-before: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 578320 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
PROBE dechw: PCHW mode=dechw n=25 size=1024 ok=25 fail=0 config=HARDWARE allocBytes=104857600 kept=25 ms=925
MEMROW helper-dechw-after     PSS=137.5  Java=3.2  Native=115.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=1.2  RSS=198.7
  FDS total=146: /dev/goldfish_pipe×33; /dev/goldfish_address_space×28; socket×11; /dev/null×10; anon_inode:sync_file×5; anon_inode:eventfd×5; anon_inode:eventpoll×3; /sys/kernel/tracing/trace_marker×2; pipe×2; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /apex/com.android.art/javalib/apache-xml.jar×1; /system/framework/framework.jar×1; /system/framework/
  GFXMEM helper-dechw-after: pipeline: Skia (OpenGL); GPU total: 2212312 bytes, 2.11 MB (64.00 KB is purgeable); textures: 0.00 bytes (1 entry) | 2.00 MB (2 entries); frames: 2
  dmabuf_dump: 0 lines, ; MemAvailable: 535824 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 0;(deleted) 4 2;(deleted) 516 7;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
## H3. The app after the same tour of thumbnail screens (Home carousel, News, Gallery, Catalogue): v1.2.31 vs v1.2.32
installed base: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
MEMROW app-base-raw           PSS=102.9  Java=11.5  Native=55.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=4.6  RSS=182.1
  FDS total=225: /dev/goldfish_pipe×53; /dev/goldfish_address_space×41; anon_inode:sync_file×24; socket×12; /dev/null×10; /data/data/org.chyavorec.app/databases/…×10; anon_inode:eventfd×8; anon_inode:eventpoll×6; /data/data/org.chyavorec.app/no_backup/…×5; pipe×4; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×3; /data/data/org.chyavorec.app/cache/…×2; /sys/kernel/tracing/trace_marker×2; /apex/com.android.art
  GFXMEM app-base-raw: pipeline: Skia (OpenGL); GPU total: 62407192 bytes, 59.52 MB (34.15 MB is purgeable); textures: 279.82 KB (48 entries) | 10.05 MB (71 entries); frames: 233
  dmabuf_dump: 0 lines, ; MemAvailable: 497000 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 2;(deleted) 516 7;(deleted) 8 0;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW app-base-trim          PSS=107.9  Java=11.5  Native=55.7  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=9.6  RSS=187.6
  FDS total=225: /dev/goldfish_pipe×53; /dev/goldfish_address_space×41; anon_inode:sync_file×24; socket×12; /dev/null×10; /data/data/org.chyavorec.app/databases/…×10; anon_inode:eventfd×8; anon_inode:eventpoll×6; /data/data/org.chyavorec.app/no_backup/…×5; pipe×4; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×3; /data/data/org.chyavorec.app/cache/…×2; /sys/kernel/tracing/trace_marker×2; /apex/com.android.art
  GFXMEM app-base-trim: pipeline: Skia (OpenGL); GPU total: 62407192 bytes, 59.52 MB (34.15 MB is purgeable); textures: 279.82 KB (48 entries) | 10.05 MB (71 entries); frames: 233
  dmabuf_dump: 0 lines, ; MemAvailable: 495796 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 2;(deleted) 516 7;(deleted) 8 0;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW app-base-raw2          PSS=98.6  Java=11.9  Native=51.3  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.5  PrivOther=5.2  RSS=177.7
  FDS total=199: /dev/goldfish_pipe×40; /dev/goldfish_address_space×29; anon_inode:sync_file×24; socket×12; /dev/null×10; /data/data/org.chyavorec.app/databases/…×10; anon_inode:eventfd×8; anon_inode:eventpoll×6; /data/data/org.chyavorec.app/no_backup/…×5; pipe×4; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×3; /sys/kernel/tracing/trace_marker×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /data/data/
  GFXMEM app-base-raw2: pipeline: Skia (OpenGL); GPU total: 60412296 bytes, 57.61 MB (34.75 MB is purgeable); textures: 287.57 KB (39 entries) | 8.86 MB (93 entries); frames: 233
  dmabuf_dump: 0 lines, ; MemAvailable: 502264 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 2;(deleted) 516 7;(deleted) 8 0;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
installed new: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
MEMROW app-new-raw            PSS=87.5  Java=14.2  Native=37.1  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.7  PrivOther=4.9  RSS=165.3
  FDS total=141: anon_inode:sync_file×24; socket×12; /dev/null×10; /data/data/org.chyavorec.app/databases/…×10; anon_inode:eventfd×8; /dev/goldfish_pipe×7; anon_inode:eventpoll×6; /data/data/org.chyavorec.app/no_backup/…×5; pipe×4; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×3; /dev/goldfish_address_space×3; /data/data/org.chyavorec.app/cache/…×2; /sys/kernel/tracing/trace_marker×2; /apex/com.android.art/j
  GFXMEM app-new-raw: pipeline: Skia (OpenGL); GPU total: 73524920 bytes, 70.12 MB (46.38 MB is purgeable); textures: 13.94 MB (53 entries) | 7.02 MB (69 entries); frames: 234
  dmabuf_dump: 0 lines, ; MemAvailable: 531500 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 2;(deleted) 516 7;(deleted) 8 0;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW app-new-trim           PSS=88.8  Java=11.8  Native=35.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.7  PrivOther=10.1  RSS=167.0
  FDS total=141: anon_inode:sync_file×24; socket×12; /dev/null×10; /data/data/org.chyavorec.app/databases/…×10; anon_inode:eventfd×8; /dev/goldfish_pipe×7; anon_inode:eventpoll×6; /data/data/org.chyavorec.app/no_backup/…×5; pipe×4; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×3; /dev/goldfish_address_space×3; /data/data/org.chyavorec.app/cache/…×2; /sys/kernel/tracing/trace_marker×2; /apex/com.android.art/j
  GFXMEM app-new-trim: pipeline: Skia (OpenGL); GPU total: 73524920 bytes, 70.12 MB (46.38 MB is purgeable); textures: 13.94 MB (53 entries) | 7.02 MB (69 entries); frames: 234
  dmabuf_dump: 0 lines, ; MemAvailable: 540792 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 2;(deleted) 516 7;(deleted) 8 0;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s0
MEMROW app-new-raw2           PSS=84.9  Java=14.3  Native=38.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.6  PrivOther=4.6  RSS=165.0
  FDS total=138: anon_inode:sync_file×24; socket×12; /dev/null×10; /data/data/org.chyavorec.app/databases/…×10; anon_inode:eventfd×7; /dev/goldfish_pipe×7; anon_inode:eventpoll×5; /data/data/org.chyavorec.app/no_backup/…×5; pipe×4; /dev/ashmemc7bN-7cc5-N-add2-3c55e0bN×3; /dev/goldfish_address_space×3; /sys/kernel/tracing/trace_marker×2; /apex/com.android.art/javalib/bouncycastle.jar×1; /dev/goldfish
  GFXMEM app-new-raw2: pipeline: Skia (OpenGL); GPU total: 71337120 bytes, 68.03 MB (37.67 MB is purgeable); textures: 287.57 KB (14 entries) | 3.59 MB (11 entries); frames: 219
  dmabuf_dump: 0 lines, ; MemAvailable: 689260 kB
  showmap (device / dmabuf / ashmem / gralloc mappings): (deleted) 4 2;(deleted) 516 16;(deleted) 8 0;/dev/__properties__/properties_serial 8 0;/dev/__properties__/property_info 140 0;/dev/__properties__/u:object_r:binder_cache_system_server_prop:s0 4 0;/dev/__properties__/u:object_r:boot_status_prop:s0 0 0;/dev/__properties__/u:object_r:bootloader_prop:s
## COMPLETE
## logcat findings
app pids seen: ['8846', '9090', '9764', '10653', '10901', '11555']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=62 total=316
== Davey: 18
   davey events=18 max=1249ms median=830ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank18-1

```
part jank18-1; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## J-new. Install new, warm-up
installed new: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 2964 ms
## D0. A few starts for the card: 4 cold + 3 warm
## J-new. Scroll scenarios (animations ON, 1080x2400): ~10 flings each, gfxinfo reset before each
new carousel card: 217 630 1045 1253 | СЪСТАВИ 45-ти общински фолклорен събор на народното творчество „От Тимок до Вита
GFX new-carousel frames=   20  janky=20 (100.00%)  p50=300ms p90=600ms p95=650ms p99=650ms  slowUI=12 slowDraw=20 highInput=0
  GFXMEM new-carousel: pipeline: Skia (OpenGL); GPU total: 24581480 bytes, 23.44 MB (19.34 MB is purgeable); textures: 3.27 MB (7 entries) | 6.56 MB (26 entries) | 4.00 MB (3 entries); frames: 20
new news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" 
GFX new-news     frames=   76  janky=76 (100.00%)  p50=250ms p90=400ms p95=450ms p99=650ms  slowUI=34 slowDraw=76 highInput=0
  GFXMEM new-news: pipeline: Skia (OpenGL); GPU total: 77068904 bytes, 73.50 MB (68.88 MB is purgeable); textures: 3.27 MB (7 entries) | 9.63 MB (50 entries) | 4.00 MB (3 entries); frames: 76
GFX new-gallery  frames=   62  janky=62 (100.00%)  p50=250ms p90=400ms p95=450ms p99=500ms  slowUI=29 slowDraw=62 highInput=0
  GFXMEM new-gallery: pipeline: Skia (OpenGL); GPU total: 83601696 bytes, 79.73 MB (74.89 MB is purgeable); textures: 7.97 MB (39 entries) | 7.63 MB (45 entries); frames: 62
new album: photos on screen 18
GFX new-album-grid frames=   75  janky=74 (98.67%)  p50=250ms p90=450ms p95=550ms p99=650ms  slowUI=31 slowDraw=74 highInput=1
  GFXMEM new-album-grid: pipeline: Skia (OpenGL); GPU total: 87402456 bytes, 83.35 MB (80.45 MB is purgeable); textures: 7.96 MB (69 entries) | 7.97 MB (66 entries); frames: 75
new catalog: 2446 results
GFX new-catalog  frames=   61  janky=61 (100.00%)  p50=450ms p90=700ms p95=750ms p99=850ms  slowUI=35 slowDraw=61 highInput=0
  GFXMEM new-catalog: pipeline: Skia (OpenGL); GPU total: 84637384 bytes, 80.72 MB (48.58 MB is purgeable); textures: 295.97 KB (18 entries) | 2.25 MB (3 entries); frames: 61
MEM new-after-scroll (raw) PSS=95111KB  Java=21320KB  Native=44048KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=19)  RSS=173228KB  Views=10 Activities=1
MEM new-after-scroll (trimmed) PSS=83962KB  Java=13716KB  Native=33132KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=15)  RSS=147140KB  Views=10 Activities=1
## D. Diagnostics › „Бързодействие“ after the starts and the scroll scenarios
PERF CARD new-filled: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 2568 мс, последен 4744 мс | Пълно показване: медиана 3223 мс, последно 6470 мс | Насечени кадри по екрани | home: 5,2 % от 155 кадъра · най-бавен 2465 мс | news: 2,9 % от 34 кадъра · най-бавен 340 мс | catalog: 1,8 % от 55 кадъра · най-бавен 522 мс | album: 1,4 % от 69 кадъра · най-бавен 391 мс | article: 1,4 % от 72 кадъра · най-бавен 404 мс | Записи: 1 | 04.11.2026 20:25:20  ·  update.verify | signature mismatch | Диагностика
PERF CARD new-filled (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 2568 мс, последен 4744 мс | Пълно показване: медиана 3223 мс, последно 6470 мс | Насечени кадри по екрани | home: 5,2 % от 155 кадъра · най-бавен 2465 мс | news: 2,9 % от 34 кадъра · най-бавен 340 мс | catalog: 1,8 % от 55 кадъра · най-бавен 522 мс | album: 1,4 % от 69 кадъра · най-бавен 391 мс | article: 1,4 % от 72 кадъра · най-бавен 404 мс | Записи: 1 | 04.11.2026 20:25:20  ·  update.verify | signature mismatch | Диагностика
## D2. „Изчисти“ clears it
PERF CARD after Изчисти: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Още няма измервания. | Няма записани грешки. | Диагностика | Дневникът е изчистен; empty text SHOWN; snackbar yes
PERF CARD new-after-clear-and-restart: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Още няма измервания. | Няма записани грешки. | Диагностика
PERF CARD new-after-clear-and-restart (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Още няма измервания. | Няма записани грешки. | Диагностика
## J-base. Install base, warm-up
installed base: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 2812 ms
## J-base. Scroll scenarios (animations ON, 1080x2400): ~10 flings each, gfxinfo reset before each
base carousel card: 126 630 954 1253 | СЪБИТИЕ С хоро и песен във Водица 26 септември 2026
GFX base-carousel frames=   20  janky=20 (100.00%)  p50=300ms p90=500ms p95=550ms p99=550ms  slowUI=15 slowDraw=20 highInput=0
  GFXMEM base-carousel: pipeline: Skia (OpenGL); GPU total: 15326013 bytes, 14.62 MB (10.51 MB is purgeable); textures: 2.83 MB (6 entries) | 311.24 KB (26 entries) | 4.00 MB (3 entries); frames: 20
base news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" 
GFX base-news    frames=   84  janky=84 (100.00%)  p50=250ms p90=400ms p95=450ms p99=600ms  slowUI=40 slowDraw=84 highInput=1
  GFXMEM base-news: pipeline: Skia (OpenGL); GPU total: 62149916 bytes, 59.27 MB (54.64 MB is purgeable); textures: 336.24 KB (41 entries) | 7.12 MB (23 entries); frames: 84
GFX base-gallery frames=   68  janky=68 (100.00%)  p50=300ms p90=400ms p95=400ms p99=400ms  slowUI=26 slowDraw=68 highInput=0
  GFXMEM base-gallery: pipeline: Skia (OpenGL); GPU total: 74197272 bytes, 70.76 MB (67.60 MB is purgeable); textures: 310.65 KB (48 entries) | 4.36 MB (39 entries); frames: 68
base album: photos on screen 18
GFX base-album-grid frames=   72  janky=72 (100.00%)  p50=200ms p90=450ms p95=450ms p99=500ms  slowUI=39 slowDraw=72 highInput=0
  GFXMEM base-album-grid: pipeline: Skia (OpenGL); GPU total: 87783672 bytes, 83.72 MB (80.55 MB is purgeable); textures: 605.02 KB (108 entries) | 4.34 MB (33 entries); frames: 72
base catalog: 2446 results
GFX base-catalog frames=   52  janky=52 (100.00%)  p50=450ms p90=600ms p95=650ms p99=950ms  slowUI=33 slowDraw=52 highInput=0
  GFXMEM base-catalog: pipeline: Skia (OpenGL); GPU total: 83031800 bytes, 79.19 MB (49.05 MB is purgeable); textures: 279.82 KB (42 entries) | 2.00 MB (2 entries); frames: 52
MEM base-after-scroll (raw) PSS=114752KB  Java=14128KB  Native=66104KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=30)  RSS=199696KB  Views=10 Activities=1
MEM base-after-scroll (trimmed) PSS=113696KB  Java=12824KB  Native=61240KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=30)  RSS=199032KB  Views=10 Activities=1
## J. Totals
## COMPLETE
## logcat findings
app pids seen: ['4509', '5826', '6327', '6633', '6967', '7415', '9965', '10318']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 21
   skipped-frames events=21 max=137 total=1209
== Davey: 41
   davey events=41 max=3575ms median=872ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank18-2

```
part jank18-2; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## J-base. Install base, warm-up
installed base: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 3241 ms
## J-base. Scroll scenarios (animations ON, 1080x2400): ~10 flings each, gfxinfo reset before each
base carousel card: 126 630 954 1253 | СЪБИТИЕ С хоро и песен във Водица 26 септември 2026
GFX base-carousel frames=   16  janky=15 (93.75%)  p50=250ms p90=400ms p95=450ms p99=450ms  slowUI=10 slowDraw=15 highInput=1
  GFXMEM base-carousel: pipeline: Skia (OpenGL); GPU total: 12213053 bytes, 11.65 MB (7.54 MB is purgeable); textures: 2.83 MB (6 entries) | 311.24 KB (26 entries) | 4.00 MB (3 entries); frames: 16
base news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" 
GFX base-news    frames=   38  janky=38 (100.00%)  p50=150ms p90=250ms p95=350ms p99=400ms  slowUI=25 slowDraw=38 highInput=0
  GFXMEM base-news: pipeline: Skia (OpenGL); GPU total: 48173124 bytes, 45.94 MB (41.34 MB is purgeable); textures: 2.83 MB (6 entries) | 631.91 KB (57 entries) | 4.00 MB (3 entries); frames: 38
GFX base-gallery frames=   79  janky=79 (100.00%)  p50=150ms p90=350ms p95=400ms p99=450ms  slowUI=38 slowDraw=79 highInput=1
  GFXMEM base-gallery: pipeline: Skia (OpenGL); GPU total: 75810584 bytes, 72.30 MB (69.46 MB is purgeable); textures: 310.65 KB (42 entries) | 7.22 MB (50 entries); frames: 79
base album: photos on screen 18
GFX base-album-grid frames=   82  janky=78 (95.12%)  p50=200ms p90=350ms p95=400ms p99=450ms  slowUI=42 slowDraw=78 highInput=5
  GFXMEM base-album-grid: pipeline: Skia (OpenGL); GPU total: 41145468 bytes, 39.24 MB (36.85 MB is purgeable); textures: 355.66 KB (93 entries) | 4.27 MB (16 entries); frames: 82
base catalog: 2446 results
GFX base-catalog frames=   56  janky=56 (100.00%)  p50=200ms p90=500ms p95=500ms p99=600ms  slowUI=22 slowDraw=56 highInput=0
  GFXMEM base-catalog: pipeline: Skia (OpenGL); GPU total: 84934464 bytes, 81.00 MB (46.86 MB is purgeable); textures: 279.82 KB (42 entries) | 2.00 MB (2 entries); frames: 56
MEM base-after-scroll (raw) PSS=131581KB  Java=14032KB  Native=63800KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=182076KB  Views=10 Activities=1
MEM base-after-scroll (trimmed) PSS=131689KB  Java=10904KB  Native=62128KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=9)  RSS=182748KB  Views=10 Activities=1
## J-new. Install new, warm-up
installed new: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 2395 ms
## D0. A few starts for the card: 4 cold + 3 warm
## J-new. Scroll scenarios (animations ON, 1080x2400): ~10 flings each, gfxinfo reset before each
new carousel card: 126 630 954 1253 | СЪБИТИЕ С хоро и песен във Водица 26 септември 2026
GFX new-carousel frames=   22  janky=22 (100.00%)  p50=250ms p90=350ms p95=350ms p99=400ms  slowUI=15 slowDraw=22 highInput=0
  GFXMEM new-carousel: pipeline: Skia (OpenGL); GPU total: 31360272 bytes, 29.91 MB (25.80 MB is purgeable); textures: 2.83 MB (6 entries) | 6.56 MB (26 entries) | 4.00 MB (3 entries); frames: 22
new news screen: --- screen: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" 
GFX new-news     frames=   59  janky=58 (98.31%)  p50=200ms p90=300ms p95=350ms p99=400ms  slowUI=27 slowDraw=58 highInput=1
  GFXMEM new-news: pipeline: Skia (OpenGL); GPU total: 66613688 bytes, 63.53 MB (58.88 MB is purgeable); textures: 9.73 MB (61 entries) | 7.13 MB (27 entries); frames: 59
GFX new-gallery  frames=   86  janky=85 (98.84%)  p50=200ms p90=300ms p95=350ms p99=500ms  slowUI=42 slowDraw=85 highInput=1
  GFXMEM new-gallery: pipeline: Skia (OpenGL); GPU total: 88002080 bytes, 83.93 MB (80.77 MB is purgeable); textures: 8.03 MB (38 entries) | 7.49 MB (62 entries); frames: 86
new album: photos on screen 18
GFX new-album-grid frames=   84  janky=84 (100.00%)  p50=200ms p90=350ms p95=350ms p99=450ms  slowUI=36 slowDraw=84 highInput=0
  GFXMEM new-album-grid: pipeline: Skia (OpenGL); GPU total: 46557172 bytes, 44.40 MB (41.69 MB is purgeable); textures: 9.63 MB (70 entries) | 2.62 MB (47 entries); frames: 84
new catalog: 2446 results
GFX new-catalog  frames=   58  janky=58 (100.00%)  p50=300ms p90=500ms p95=550ms p99=700ms  slowUI=29 slowDraw=58 highInput=0
  GFXMEM new-catalog: pipeline: Skia (OpenGL); GPU total: 53430856 bytes, 50.96 MB (30.80 MB is purgeable); textures: 279.82 KB (12 entries) | 2.00 MB (2 entries); frames: 58
MEM new-after-scroll (raw) PSS=91992KB  Java=17056KB  Native=41924KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=170604KB  Views=10 Activities=1
MEM new-after-scroll (trimmed) PSS=93200KB  Java=14224KB  Native=40852KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=172148KB  Views=10 Activities=1
## D. Diagnostics › „Бързодействие“ after the starts and the scroll scenarios
PERF CARD new-filled: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 1143 мс, последен 3025 мс | Пълно показване: медиана 1417 мс, последно 4138 мс | Насечени кадри по екрани | home: 1,1 % от 182 кадъра · най-бавен 1565 мс | album: 1,1 % от 91 кадъра · най-бавен 236 мс | gallery: 0,0 % от 140 кадъра · най-бавен 311 мс | news: 0,0 % от 71 кадъра · най-бавен 302 мс | catalog: 0,0 % от 57 кадъра · най-бавен 439 мс | Записи: 1 | 04.11.2026 20:26:03  ·  update.verify | signature mismatch | Диагностика
PERF CARD new-filled (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 1143 мс, последен 3025 мс | Пълно показване: медиана 1417 мс, последно 4138 мс | Насечени кадри по екрани | home: 1,1 % от 182 кадъра · най-бавен 1565 мс | album: 1,1 % от 91 кадъра · най-бавен 236 мс | gallery: 0,0 % от 140 кадъра · най-бавен 311 мс | news: 0,0 % от 71 кадъра · най-бавен 302 мс | catalog: 0,0 % от 57 кадъра · най-бавен 439 мс | Записи: 1 | 04.11.2026 20:26:03  ·  update.verify | signature mismatch | Диагностика
## D2. „Изчисти“ clears it
PERF CARD after Изчисти: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Още няма измервания. | Няма записани грешки. | Диагностика | Дневникът е изчистен; empty text SHOWN; snackbar yes
PERF CARD new-after-clear-and-restart: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Още няма измервания. | Няма записани грешки. | Диагностика
PERF CARD new-after-clear-and-restart (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Още няма измервания. | Няма записани грешки. | Диагностика
## J. Totals
## COMPLETE
## logcat findings
app pids seen: ['5802', '7391', '8985', '9340', '9414', '9473', '9541', '9684']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=120 total=475
== Davey: 15
   davey events=15 max=3433ms median=897ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem-base

```
part mem-base; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15700 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
installed base: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3300 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=55206KB  Java=6536KB  Native=9736KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=5)  RSS=97048KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=56262KB  Java=7084KB  Native=4888KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=1)  RSS=80336KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=101442KB  Java=11912KB  Native=30992KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=150128KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=129229KB  Java=19108KB  Native=39860KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=165016KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=109309KB  Java=11368KB  Native=35620KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=152788KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=107605KB  Java=11396KB  Native=39492KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=14)  RSS=158984KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=118722KB  Java=13152KB  Native=48128KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=18)  RSS=171044KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=116194KB  Java=11324KB  Native=47472KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=18)  RSS=168516KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=115999KB  Java=11380KB  Native=47624KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=18)  RSS=169744KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=121906KB  Java=15956KB  Native=48552KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=18)  RSS=176052KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=116698KB  Java=11432KB  Native=47892KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=18)  RSS=170844KB  Views=13 Activities=1
pid after cycles: 6771; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=56262 c5=101442 c10=109309 c15=107605 c20=116194 c25=115999 c30=116698
LEAK TREND (cycles 10-30, least squares): +463 KB per cycle (+9.1 MB over 20 cycles)
MEMTABLE base 1.2.31 (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)
  c00 raw  PSS=53.9  Java=6.4  Native=9.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=2.7  RSS=94.8
  c00 trim PSS=54.9  Java=6.9  Native=4.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=7.9  RSS=78.5
  c10 raw  PSS=126.2  Java=18.7  Native=38.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.6  RSS=161.1
  c10 trim PSS=106.7  Java=11.1  Native=34.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.4  RSS=149.2
  c20 raw  PSS=115.9  Java=12.8  Native=47.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.5  RSS=167.0
  c20 trim PSS=113.5  Java=11.1  Native=46.4  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.5  RSS=164.6
  c30 raw  PSS=119.0  Java=15.6  Native=47.4  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.4  RSS=171.9
  c30 trim PSS=114.0  Java=11.2  Native=46.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.4  RSS=166.8
## COMPLETE
## logcat findings
app pids seen: ['5238', '6771']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=44 total=540
== Davey: 56
   davey events=56 max=1212ms median=778ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem

```
part mem; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4059 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=44323KB  Java=6772KB  Native=5116KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=6)  RSS=87932KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=51336KB  Java=7052KB  Native=5176KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=6)  RSS=94376KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=93687KB  Java=11224KB  Native=35224KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=151600KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=111514KB  Java=20132KB  Native=33536KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=155692KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=97846KB  Java=10780KB  Native=29272KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=142068KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=104040KB  Java=10800KB  Native=33600KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=149044KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=107368KB  Java=12608KB  Native=34732KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=153996KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=104943KB  Java=10736KB  Native=34208KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=151572KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=107737KB  Java=10892KB  Native=39692KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=158260KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=123907KB  Java=20536KB  Native=50048KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=179688KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=106251KB  Java=10904KB  Native=42232KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=162032KB  Views=13 Activities=1
pid after cycles: 6329; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=51336 c5=93687 c10=97846 c15=104040 c20=104943 c25=107737 c30=106251
LEAK TREND (cycles 10-30, least squares): +410 KB per cycle (+8.0 MB over 20 cycles)
MEMTABLE new 1.2.32 (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)
  c00 raw  PSS=43.3  Java=6.6  Native=5.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=2.5  RSS=85.9
  c00 trim PSS=50.1  Java=6.9  Native=5.1  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=8.4  RSS=92.2
  c10 raw  PSS=108.9  Java=19.7  Native=32.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.6  RSS=152.0
  c10 trim PSS=95.6  Java=10.5  Native=28.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.6  RSS=138.7
  c20 raw  PSS=104.9  Java=12.3  Native=33.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.6  RSS=150.4
  c20 trim PSS=102.5  Java=10.5  Native=33.4  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.6  RSS=148.0
  c30 raw  PSS=121.0  Java=20.1  Native=48.9  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.8  RSS=175.5
  c30 trim PSS=103.8  Java=10.6  Native=41.2  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.3  PrivOther=9.6  RSS=158.2
## COMPLETE
## logcat findings
app pids seen: ['4711', '6329']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 87
   skipped-frames events=87 max=97 total=3342
== Davey: 202
   davey events=202 max=2015ms median=828ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey18

```
part monkey18; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4105 ms
## K. Monkey 3000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=5419
MONKEY exit=0 duration=273 s; Events injected: 3000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '5419'; airplane=0; camera: 
MEM after-monkey (raw) PSS=63585KB  Java=11804KB  Native=10948KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=101656KB  Views=29 Activities=1
MEM after-monkey (trimmed) PSS=67098KB  Java=11260KB  Native=10972KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=106772KB  Views=27 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4211', '5419']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 158
   skipped-frames events=158 max=150 total=6365
== Davey: 327
   davey events=327 max=2808ms median=929ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## sizediff

```
== release-config APKs: new 4143918 B, base 4130922 B
TOTAL compressed entries: new 4119863 B, base 4106864 B, delta +12999 B
ARSC resources by type (new/base/delta): string 804/794/+10, id 259/257/+2
ARSC string configurations: new 2, base 2; only in new: []
RES files, top growth by resource-name prefix (compressed B, new/base):
RES resources with files only in new: 0; sample: []
DEX classes (mapping.txt): new 8462, base 8417; top growth by package:
     +21  org.chyavorec.app  (986/965)
     +19  androidx.metrics.performance  (19/0)
      +3  org.chyavorec.data  (376/373)
      +3  androidx.activity.compose  (26/23)
      +2  androidx.compose.foundation  (974/972)
      +1  okio.internal  (11/10)
      +1  kotlinx.serialization.json  (73/72)
      +1  androidx.emoji2.text  (37/36)
      +1  androidx.activity.OnBackPressedDispatcher$$ExternalSyntheticLambda0  (1/0)
      +1  androidx.activity.FullyDrawnReporterOwner  (1/0)
DEX size by package (depth 2, apkanalyzer, bytes), top growth:
    +23591  <TOTAL>  (5453806/5430215)
    +14672  org.chyavorec  (1197392/1182720)
     +4343  androidx.activity  (29276/24933)
     +4280  androidx.metrics  (4280/0)
     +2984  com.google  (760570/757586)
     +1225  okio.internal  (9504/8279)
     +1098  okhttp3.internal  (112585/111487)
      +778  kotlinx.serialization  (79825/79047)
      +173  androidx.emoji2  (8784/8611)
      +144  androidx.compose  (1816227/1816083)
       +76  androidx.datastore  (126132/126056)
       +64  android.view  (7583/7519)
DEX size by package (depth 3, apkanalyzer, bytes), top growth:
    +23591  <TOTAL>  (5453806/5430215)
    +12022  org.chyavorec.app  (841887/829865)
     +4280  androidx.metrics.performance  (4280/0)
     +4108  androidx.compose.foundation  (562846/558738)
     +2684  org.chyavorec.data  (250563/247879)
     +2078  com.google.android  (665132/663054)
     +1225  okio.internal  (9504/8279)
     +1100  okhttp3.internal.publicsuffix  (3535/2435)
      +976  androidx.activity.compose  (8643/7667)
      +907  com.google.mlkit  (56231/55324)
      +798  kotlinx.serialization.json  (44189/43391)
      +210  androidx.work.impl  (72241/72031)
## COMPLETE
```

## zoom18

```
part zoom18; APK: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654 bytes
base APK: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## Z-new. Viewer: pinch at off-centre points (zoom around the fingers?), double tap, pan, drag to dismiss
installed new: 1.2.32 c1b5ef5 (Какво ново: версия 1.2.32); versionCode 1488545; x86_64 libs: 2 size 4151654;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
new viewer: text="1 / 29  ·  Сградата на читалището в Яворец" screen 1080x2400
ZOOMFIX new pinch #1 at (302,1200): scale≈1.77 fixed point≈(303,1204) fingers=(302,1200) |f-fingers|=4px |f-centre(540,1200)|=237px match=0.993 → AROUND THE FINGERS
  after double tap on the zoomed photo: diff vs 1x 0.0 (≈0 = back at 1x); text="1 / 29  ·  Сградата на читалището в Яворец"
ZOOMFIX new pinch #2 at (777,1056): scale≈1.77 fixed point≈(779,1057) fingers=(777,1056) |f-fingers|=3px |f-centre(540,1200)|=279px match=0.998 → AROUND THE FINGERS
  after double tap on the zoomed photo: diff vs 1x 0.0 (≈0 = back at 1x); text="1 / 29  ·  Сградата на читалището в Яворец"
ZOOMFIX new pinch #3 at (540,1200): scale≈1.77 fixed point≈(541,1204) fingers=(540,1200) |f-fingers|=4px |f-centre(540,1200)|=4px match=0.993 → unclear
  after double tap on the zoomed photo: diff vs 1x 0.0 (≈0 = back at 1x); text="1 / 29  ·  Сградата на читалището в Яворец"
## Z-new. Double tap at an off-centre point, pan while zoomed, double tap back
ZOOMFIX new double tap at (324,1104): scale≈2.00 fixed point≈(326,1104) fingers=(324,1104) |f-fingers|=2px |f-centre(540,1200)|=235px match=0.995 → AROUND THE FINGERS
PAN new: SHIFT pan: dx=-194px dy=-0px (response 0.74) (drag −216 px); page before 'text="1 / 29  ·  Сградата на читалището в Яворец"' after 'text="1 / 29  ·  Сградата на читалището в Яворец"'
PAN-V new (zoomed, vertical drag must pan, not dismiss): SHIFT pan: dx=-0px dy=+325px (response 0.85); still in viewer: yes
  after double tap back: diff vs 1x 0.0 (≈0 = 1x)
## Z-new. Swipe to the next photo, then drag down to dismiss
after swipe left: text="2 / 29  ·  Фолклорен състав при читалището"
DISMISS new: viewer page text ''; photo cells 18; content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-
## Z-base. Viewer: pinch at off-centre points (zoom around the fingers?), double tap, pan, drag to dismiss
installed base: 1.2.31 6f44326 (Профил за бърз старт: генериран на емулатор от e20a974); versionCode 1488544; x86_64 libs: 2; update manifest: no-update stub size 4138646;  versionCode=1488544 minSdk=26 targetSdk=37  versionName=1.2.31 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base viewer: text="1 / 29  ·  Сградата на читалището в Яворец" screen 1080x2400
ZOOMFIX base pinch #1 at (302,1200): scale≈1.77 fixed point≈(541,1199) fingers=(302,1200) |f-fingers|=239px |f-centre(540,1200)|=2px match=0.991 → AROUND THE CENTRE
  after double tap on the zoomed photo: diff vs 1x 0.0 (≈0 = back at 1x); text="1 / 29  ·  Сградата на читалището в Яворец"
ZOOMFIX base pinch #2 at (777,1056): scale≈1.77 fixed point≈(541,1199) fingers=(777,1056) |f-fingers|=276px |f-centre(540,1200)|=2px match=0.991 → AROUND THE CENTRE
  after double tap on the zoomed photo: diff vs 1x 0.0 (≈0 = back at 1x); text="1 / 29  ·  Сградата на читалището в Яворец"
ZOOMFIX base pinch #3 at (540,1200): scale≈1.77 fixed point≈(541,1199) fingers=(540,1200) |f-fingers|=2px |f-centre(540,1200)|=2px match=0.991 → unclear
  after double tap on the zoomed photo: diff vs 1x 0.0 (≈0 = back at 1x); text="1 / 29  ·  Сградата на читалището в Яворец"
## Z-base. Double tap at an off-centre point, pan while zoomed, double tap back
ZOOMFIX base double tap at (324,1104): scale≈2.00 fixed point≈(326,1104) fingers=(324,1104) |f-fingers|=2px |f-centre(540,1200)|=235px match=0.995 → AROUND THE FINGERS
PAN base: SHIFT pan: dx=-192px dy=-0px (response 0.74) (drag −216 px); page before 'text="1 / 29  ·  Сградата на читалището в Яворец"' after 'text="1 / 29  ·  Сградата на читалището в Яворец"'
PAN-V base (zoomed, vertical drag must pan, not dismiss): SHIFT pan: dx=+0px dy=+339px (response 0.85); still in viewer: yes
  after double tap back: diff vs 1x 0.0 (≈0 = 1x)
## Z-base. Swipe to the next photo, then drag down to dismiss
after swipe left: text="2 / 29  ·  Фолклорен състав при читалището"
DISMISS base: viewer page text ''; photo cells 18; content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-
## COMPLETE
## logcat findings
app pids seen: ['5665', '6563', '8412', '8592']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=37 total=37
== Davey: 2
   davey events=2 max=1030ms median=938ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Files

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
- [abstart-1/shots/ab-base-1.png](abstart-1/shots/ab-base-1.png)
- [abstart-1/shots/ab-base-2.png](abstart-1/shots/ab-base-2.png)
- [abstart-1/shots/ab-new-0.png](abstart-1/shots/ab-new-0.png)
- [abstart-1/shots/ab-new-1.png](abstart-1/shots/ab-new-1.png)
- [abstart-1/shots/ab-new-2.png](abstart-1/shots/ab-new-2.png)
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
- [abstart-2/shots/ab-base-0.png](abstart-2/shots/ab-base-0.png)
- [abstart-2/shots/ab-base-1.png](abstart-2/shots/ab-base-1.png)
- [abstart-2/shots/ab-base-2.png](abstart-2/shots/ab-base-2.png)
- [abstart-2/shots/ab-new-1.png](abstart-2/shots/ab-new-1.png)
- [abstart-2/shots/ab-new-2.png](abstart-2/shots/ab-new-2.png)
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
- [drawn/drawn-cleared-lines.txt](drawn/drawn-cleared-lines.txt)
- [drawn/drawn-cleared.txt](drawn/drawn-cleared.txt)
- [drawn/drawn-empty-offline-lines.txt](drawn/drawn-empty-offline-lines.txt)
- [drawn/drawn-empty-offline.txt](drawn/drawn-empty-offline.txt)
- [drawn/drawn-empty-online-lines.txt](drawn/drawn-empty-online-lines.txt)
- [drawn/drawn-empty-online.txt](drawn/drawn-empty-online.txt)
- [drawn/drawn-offline-lines.txt](drawn/drawn-offline-lines.txt)
- [drawn/drawn-offline.txt](drawn/drawn-offline.txt)
- [drawn/drawn-online-lines.txt](drawn/drawn-online-lines.txt)
- [drawn/drawn-online.txt](drawn/drawn-online.txt)
- [drawn/host-after-run.txt](drawn/host-after-run.txt)
- [drawn/job-status.txt](drawn/job-status.txt)
- [drawn/logcat-all.txt](drawn/logcat-all.txt)
- [drawn/logcat-dump.txt](drawn/logcat-dump.txt)
- [drawn/logcat-findings.txt](drawn/logcat-findings.txt)
- [drawn/logcat-live.txt](drawn/logcat-live.txt)
- [drawn/shots/drawn/00-first-launch.png](drawn/shots/drawn/00-first-launch.png)
- [drawn/shots/drawn/01-online.png](drawn/shots/drawn/01-online.png)
- [drawn/shots/drawn/02-offline.png](drawn/shots/drawn/02-offline.png)
- [drawn/shots/drawn/03-empty-online.png](drawn/shots/drawn/03-empty-online.png)
- [drawn/shots/drawn/04-empty-offline.png](drawn/shots/drawn/04-empty-offline.png)
- [drawn/summary.txt](drawn/summary.txt)
- [hwprobe/host-after-run.txt](hwprobe/host-after-run.txt)
- [hwprobe/hw/dmabuf-app-base-raw.txt](hwprobe/hw/dmabuf-app-base-raw.txt)
- [hwprobe/hw/dmabuf-app-base-raw2.txt](hwprobe/hw/dmabuf-app-base-raw2.txt)
- [hwprobe/hw/dmabuf-app-base-trim.txt](hwprobe/hw/dmabuf-app-base-trim.txt)
- [hwprobe/hw/dmabuf-app-new-raw.txt](hwprobe/hw/dmabuf-app-new-raw.txt)
- [hwprobe/hw/dmabuf-app-new-raw2.txt](hwprobe/hw/dmabuf-app-new-raw2.txt)
- [hwprobe/hw/dmabuf-app-new-trim.txt](hwprobe/hw/dmabuf-app-new-trim.txt)
- [hwprobe/hw/dmabuf-helper-dec565-after.txt](hwprobe/hw/dmabuf-helper-dec565-after.txt)
- [hwprobe/hw/dmabuf-helper-dec565-before.txt](hwprobe/hw/dmabuf-helper-dec565-before.txt)
- [hwprobe/hw/dmabuf-helper-dec8888-after.txt](hwprobe/hw/dmabuf-helper-dec8888-after.txt)
- [hwprobe/hw/dmabuf-helper-dec8888-before.txt](hwprobe/hw/dmabuf-helper-dec8888-before.txt)
- [hwprobe/hw/dmabuf-helper-dechw-after.txt](hwprobe/hw/dmabuf-helper-dechw-after.txt)
- [hwprobe/hw/dmabuf-helper-dechw-before.txt](hwprobe/hw/dmabuf-helper-dechw-before.txt)
- [hwprobe/hw/dmabuf-helper-hw-after.txt](hwprobe/hw/dmabuf-helper-hw-after.txt)
- [hwprobe/hw/dmabuf-helper-hw-before.txt](hwprobe/hw/dmabuf-helper-hw-before.txt)
- [hwprobe/hw/dmabuf-helper-sw565-after.txt](hwprobe/hw/dmabuf-helper-sw565-after.txt)
- [hwprobe/hw/dmabuf-helper-sw565-before.txt](hwprobe/hw/dmabuf-helper-sw565-before.txt)
- [hwprobe/hw/dmabuf-helper-sw8888-after.txt](hwprobe/hw/dmabuf-helper-sw8888-after.txt)
- [hwprobe/hw/dmabuf-helper-sw8888-before.txt](hwprobe/hw/dmabuf-helper-sw8888-before.txt)
- [hwprobe/hw/fd-app-base-raw.txt](hwprobe/hw/fd-app-base-raw.txt)
- [hwprobe/hw/fd-app-base-raw2.txt](hwprobe/hw/fd-app-base-raw2.txt)
- [hwprobe/hw/fd-app-base-trim.txt](hwprobe/hw/fd-app-base-trim.txt)
- [hwprobe/hw/fd-app-new-raw.txt](hwprobe/hw/fd-app-new-raw.txt)
- [hwprobe/hw/fd-app-new-raw2.txt](hwprobe/hw/fd-app-new-raw2.txt)
- [hwprobe/hw/fd-app-new-trim.txt](hwprobe/hw/fd-app-new-trim.txt)
- [hwprobe/hw/fd-helper-dec565-after.txt](hwprobe/hw/fd-helper-dec565-after.txt)
- [hwprobe/hw/fd-helper-dec565-before.txt](hwprobe/hw/fd-helper-dec565-before.txt)
- [hwprobe/hw/fd-helper-dec8888-after.txt](hwprobe/hw/fd-helper-dec8888-after.txt)
- [hwprobe/hw/fd-helper-dec8888-before.txt](hwprobe/hw/fd-helper-dec8888-before.txt)
- [hwprobe/hw/fd-helper-dechw-after.txt](hwprobe/hw/fd-helper-dechw-after.txt)
- [hwprobe/hw/fd-helper-dechw-before.txt](hwprobe/hw/fd-helper-dechw-before.txt)
- [hwprobe/hw/fd-helper-hw-after.txt](hwprobe/hw/fd-helper-hw-after.txt)
- [hwprobe/hw/fd-helper-hw-before.txt](hwprobe/hw/fd-helper-hw-before.txt)
- [hwprobe/hw/fd-helper-sw565-after.txt](hwprobe/hw/fd-helper-sw565-after.txt)
- [hwprobe/hw/fd-helper-sw565-before.txt](hwprobe/hw/fd-helper-sw565-before.txt)
- [hwprobe/hw/fd-helper-sw8888-after.txt](hwprobe/hw/fd-helper-sw8888-after.txt)
- [hwprobe/hw/fd-helper-sw8888-before.txt](hwprobe/hw/fd-helper-sw8888-before.txt)
- [hwprobe/hw/gfx-app-base-raw.txt](hwprobe/hw/gfx-app-base-raw.txt)
- [hwprobe/hw/gfx-app-base-raw2.txt](hwprobe/hw/gfx-app-base-raw2.txt)
- [hwprobe/hw/gfx-app-base-trim.txt](hwprobe/hw/gfx-app-base-trim.txt)
- [hwprobe/hw/gfx-app-new-raw.txt](hwprobe/hw/gfx-app-new-raw.txt)
- [hwprobe/hw/gfx-app-new-raw2.txt](hwprobe/hw/gfx-app-new-raw2.txt)
- [hwprobe/hw/gfx-app-new-trim.txt](hwprobe/hw/gfx-app-new-trim.txt)
- [hwprobe/hw/gfx-helper-dec565-after.txt](hwprobe/hw/gfx-helper-dec565-after.txt)
- [hwprobe/hw/gfx-helper-dec565-before.txt](hwprobe/hw/gfx-helper-dec565-before.txt)
- [hwprobe/hw/gfx-helper-dec8888-after.txt](hwprobe/hw/gfx-helper-dec8888-after.txt)
- [hwprobe/hw/gfx-helper-dec8888-before.txt](hwprobe/hw/gfx-helper-dec8888-before.txt)
- [hwprobe/hw/gfx-helper-dechw-after.txt](hwprobe/hw/gfx-helper-dechw-after.txt)
- [hwprobe/hw/gfx-helper-dechw-before.txt](hwprobe/hw/gfx-helper-dechw-before.txt)
- [hwprobe/hw/gfx-helper-hw-after.txt](hwprobe/hw/gfx-helper-hw-after.txt)
- [hwprobe/hw/gfx-helper-hw-before.txt](hwprobe/hw/gfx-helper-hw-before.txt)
- [hwprobe/hw/gfx-helper-sw565-after.txt](hwprobe/hw/gfx-helper-sw565-after.txt)
- [hwprobe/hw/gfx-helper-sw565-before.txt](hwprobe/hw/gfx-helper-sw565-before.txt)
- [hwprobe/hw/gfx-helper-sw8888-after.txt](hwprobe/hw/gfx-helper-sw8888-after.txt)
- [hwprobe/hw/gfx-helper-sw8888-before.txt](hwprobe/hw/gfx-helper-sw8888-before.txt)
- [hwprobe/hw/mem-app-base-raw.txt](hwprobe/hw/mem-app-base-raw.txt)
- [hwprobe/hw/mem-app-base-raw2.txt](hwprobe/hw/mem-app-base-raw2.txt)
- [hwprobe/hw/mem-app-base-trim.txt](hwprobe/hw/mem-app-base-trim.txt)
- [hwprobe/hw/mem-app-new-raw.txt](hwprobe/hw/mem-app-new-raw.txt)
- [hwprobe/hw/mem-app-new-raw2.txt](hwprobe/hw/mem-app-new-raw2.txt)
- [hwprobe/hw/mem-app-new-trim.txt](hwprobe/hw/mem-app-new-trim.txt)
- [hwprobe/hw/mem-helper-dec565-after.txt](hwprobe/hw/mem-helper-dec565-after.txt)
- [hwprobe/hw/mem-helper-dec565-before.txt](hwprobe/hw/mem-helper-dec565-before.txt)
- [hwprobe/hw/mem-helper-dec8888-after.txt](hwprobe/hw/mem-helper-dec8888-after.txt)
- [hwprobe/hw/mem-helper-dec8888-before.txt](hwprobe/hw/mem-helper-dec8888-before.txt)
- [hwprobe/hw/mem-helper-dechw-after.txt](hwprobe/hw/mem-helper-dechw-after.txt)
- [hwprobe/hw/mem-helper-dechw-before.txt](hwprobe/hw/mem-helper-dechw-before.txt)
- [hwprobe/hw/mem-helper-hw-after.txt](hwprobe/hw/mem-helper-hw-after.txt)
- [hwprobe/hw/mem-helper-hw-before.txt](hwprobe/hw/mem-helper-hw-before.txt)
- [hwprobe/hw/mem-helper-sw565-after.txt](hwprobe/hw/mem-helper-sw565-after.txt)
- [hwprobe/hw/mem-helper-sw565-before.txt](hwprobe/hw/mem-helper-sw565-before.txt)
- [hwprobe/hw/mem-helper-sw8888-after.txt](hwprobe/hw/mem-helper-sw8888-after.txt)
- [hwprobe/hw/mem-helper-sw8888-before.txt](hwprobe/hw/mem-helper-sw8888-before.txt)
- [hwprobe/hw/meminfo-system.txt](hwprobe/hw/meminfo-system.txt)
- [hwprobe/hw/showmap-app-base-raw.txt](hwprobe/hw/showmap-app-base-raw.txt)
- [hwprobe/hw/showmap-app-base-raw2.txt](hwprobe/hw/showmap-app-base-raw2.txt)
- [hwprobe/hw/showmap-app-base-trim.txt](hwprobe/hw/showmap-app-base-trim.txt)
- [hwprobe/hw/showmap-app-new-raw.txt](hwprobe/hw/showmap-app-new-raw.txt)
- [hwprobe/hw/showmap-app-new-raw2.txt](hwprobe/hw/showmap-app-new-raw2.txt)
- [hwprobe/hw/showmap-app-new-trim.txt](hwprobe/hw/showmap-app-new-trim.txt)
- [hwprobe/hw/showmap-helper-dec565-after.txt](hwprobe/hw/showmap-helper-dec565-after.txt)
- [hwprobe/hw/showmap-helper-dec565-before.txt](hwprobe/hw/showmap-helper-dec565-before.txt)
- [hwprobe/hw/showmap-helper-dec8888-after.txt](hwprobe/hw/showmap-helper-dec8888-after.txt)
- [hwprobe/hw/showmap-helper-dec8888-before.txt](hwprobe/hw/showmap-helper-dec8888-before.txt)
- [hwprobe/hw/showmap-helper-dechw-after.txt](hwprobe/hw/showmap-helper-dechw-after.txt)
- [hwprobe/hw/showmap-helper-dechw-before.txt](hwprobe/hw/showmap-helper-dechw-before.txt)
- [hwprobe/hw/showmap-helper-hw-after.txt](hwprobe/hw/showmap-helper-hw-after.txt)
- [hwprobe/hw/showmap-helper-hw-before.txt](hwprobe/hw/showmap-helper-hw-before.txt)
- [hwprobe/hw/showmap-helper-sw565-after.txt](hwprobe/hw/showmap-helper-sw565-after.txt)
- [hwprobe/hw/showmap-helper-sw565-before.txt](hwprobe/hw/showmap-helper-sw565-before.txt)
- [hwprobe/hw/showmap-helper-sw8888-after.txt](hwprobe/hw/showmap-helper-sw8888-after.txt)
- [hwprobe/hw/showmap-helper-sw8888-before.txt](hwprobe/hw/showmap-helper-sw8888-before.txt)
- [hwprobe/job-status.txt](hwprobe/job-status.txt)
- [hwprobe/logcat-all.txt](hwprobe/logcat-all.txt)
- [hwprobe/logcat-dump.txt](hwprobe/logcat-dump.txt)
- [hwprobe/logcat-findings.txt](hwprobe/logcat-findings.txt)
- [hwprobe/logcat-live.txt](hwprobe/logcat-live.txt)
- [hwprobe/shots/hw/app-base.png](hwprobe/shots/hw/app-base.png)
- [hwprobe/shots/hw/app-new.png](hwprobe/shots/hw/app-new.png)
- [hwprobe/shots/hw/helper-dec565.png](hwprobe/shots/hw/helper-dec565.png)
- [hwprobe/shots/hw/helper-dec8888.png](hwprobe/shots/hw/helper-dec8888.png)
- [hwprobe/shots/hw/helper-dechw.png](hwprobe/shots/hw/helper-dechw.png)
- [hwprobe/shots/hw/helper-hw.png](hwprobe/shots/hw/helper-hw.png)
- [hwprobe/shots/hw/helper-sw565.png](hwprobe/shots/hw/helper-sw565.png)
- [hwprobe/shots/hw/helper-sw8888.png](hwprobe/shots/hw/helper-sw8888.png)
- [hwprobe/summary.txt](hwprobe/summary.txt)
- [jank18-1/gfx-base-album-grid-full.txt](jank18-1/gfx-base-album-grid-full.txt)
- [jank18-1/gfx-base-album-grid.txt](jank18-1/gfx-base-album-grid.txt)
- [jank18-1/gfx-base-carousel-full.txt](jank18-1/gfx-base-carousel-full.txt)
- [jank18-1/gfx-base-carousel.txt](jank18-1/gfx-base-carousel.txt)
- [jank18-1/gfx-base-catalog-full.txt](jank18-1/gfx-base-catalog-full.txt)
- [jank18-1/gfx-base-catalog.txt](jank18-1/gfx-base-catalog.txt)
- [jank18-1/gfx-base-gallery-full.txt](jank18-1/gfx-base-gallery-full.txt)
- [jank18-1/gfx-base-gallery.txt](jank18-1/gfx-base-gallery.txt)
- [jank18-1/gfx-base-news-full.txt](jank18-1/gfx-base-news-full.txt)
- [jank18-1/gfx-base-news.txt](jank18-1/gfx-base-news.txt)
- [jank18-1/gfx-new-album-grid-full.txt](jank18-1/gfx-new-album-grid-full.txt)
- [jank18-1/gfx-new-album-grid.txt](jank18-1/gfx-new-album-grid.txt)
- [jank18-1/gfx-new-carousel-full.txt](jank18-1/gfx-new-carousel-full.txt)
- [jank18-1/gfx-new-carousel.txt](jank18-1/gfx-new-carousel.txt)
- [jank18-1/gfx-new-catalog-full.txt](jank18-1/gfx-new-catalog-full.txt)
- [jank18-1/gfx-new-catalog.txt](jank18-1/gfx-new-catalog.txt)
- [jank18-1/gfx-new-gallery-full.txt](jank18-1/gfx-new-gallery-full.txt)
- [jank18-1/gfx-new-gallery.txt](jank18-1/gfx-new-gallery.txt)
- [jank18-1/gfx-new-news-full.txt](jank18-1/gfx-new-news-full.txt)
- [jank18-1/gfx-new-news.txt](jank18-1/gfx-new-news.txt)
- [jank18-1/gfx-totals.txt](jank18-1/gfx-totals.txt)
- [jank18-1/host-after-run.txt](jank18-1/host-after-run.txt)
- [jank18-1/job-status.txt](jank18-1/job-status.txt)
- [jank18-1/logcat-all.txt](jank18-1/logcat-all.txt)
- [jank18-1/logcat-dump.txt](jank18-1/logcat-dump.txt)
- [jank18-1/logcat-findings.txt](jank18-1/logcat-findings.txt)
- [jank18-1/logcat-live.txt](jank18-1/logcat-live.txt)
- [jank18-1/mem-base-after-scroll-raw.txt](jank18-1/mem-base-after-scroll-raw.txt)
- [jank18-1/mem-base-after-scroll.txt](jank18-1/mem-base-after-scroll.txt)
- [jank18-1/mem-new-after-scroll-raw.txt](jank18-1/mem-new-after-scroll-raw.txt)
- [jank18-1/mem-new-after-scroll.txt](jank18-1/mem-new-after-scroll.txt)
- [jank18-1/perf-new-after-clear-and-restart.xml](jank18-1/perf-new-after-clear-and-restart.xml)
- [jank18-1/perf-new-filled.xml](jank18-1/perf-new-filled.xml)
- [jank18-1/shots/jank/base-album-grid.png](jank18-1/shots/jank/base-album-grid.png)
- [jank18-1/shots/jank/base-carousel.png](jank18-1/shots/jank/base-carousel.png)
- [jank18-1/shots/jank/base-catalog.png](jank18-1/shots/jank/base-catalog.png)
- [jank18-1/shots/jank/base-gallery.png](jank18-1/shots/jank/base-gallery.png)
- [jank18-1/shots/jank/base-news.png](jank18-1/shots/jank/base-news.png)
- [jank18-1/shots/jank/new-album-grid.png](jank18-1/shots/jank/new-album-grid.png)
- [jank18-1/shots/jank/new-carousel.png](jank18-1/shots/jank/new-carousel.png)
- [jank18-1/shots/jank/new-catalog.png](jank18-1/shots/jank/new-catalog.png)
- [jank18-1/shots/jank/new-gallery.png](jank18-1/shots/jank/new-gallery.png)
- [jank18-1/shots/jank/new-news.png](jank18-1/shots/jank/new-news.png)
- [jank18-1/shots/perf/new-after-clear-and-restart-1.png](jank18-1/shots/perf/new-after-clear-and-restart-1.png)
- [jank18-1/shots/perf/new-after-clear-and-restart-2.png](jank18-1/shots/perf/new-after-clear-and-restart-2.png)
- [jank18-1/shots/perf/new-cleared.png](jank18-1/shots/perf/new-cleared.png)
- [jank18-1/shots/perf/new-filled-1.png](jank18-1/shots/perf/new-filled-1.png)
- [jank18-1/shots/perf/new-filled-2.png](jank18-1/shots/perf/new-filled-2.png)
- [jank18-1/summary.txt](jank18-1/summary.txt)
- [jank18-2/gfx-base-album-grid-full.txt](jank18-2/gfx-base-album-grid-full.txt)
- [jank18-2/gfx-base-album-grid.txt](jank18-2/gfx-base-album-grid.txt)
- [jank18-2/gfx-base-carousel-full.txt](jank18-2/gfx-base-carousel-full.txt)
- [jank18-2/gfx-base-carousel.txt](jank18-2/gfx-base-carousel.txt)
- [jank18-2/gfx-base-catalog-full.txt](jank18-2/gfx-base-catalog-full.txt)
- [jank18-2/gfx-base-catalog.txt](jank18-2/gfx-base-catalog.txt)
- [jank18-2/gfx-base-gallery-full.txt](jank18-2/gfx-base-gallery-full.txt)
- [jank18-2/gfx-base-gallery.txt](jank18-2/gfx-base-gallery.txt)
- [jank18-2/gfx-base-news-full.txt](jank18-2/gfx-base-news-full.txt)
- [jank18-2/gfx-base-news.txt](jank18-2/gfx-base-news.txt)
- [jank18-2/gfx-new-album-grid-full.txt](jank18-2/gfx-new-album-grid-full.txt)
- [jank18-2/gfx-new-album-grid.txt](jank18-2/gfx-new-album-grid.txt)
- [jank18-2/gfx-new-carousel-full.txt](jank18-2/gfx-new-carousel-full.txt)
- [jank18-2/gfx-new-carousel.txt](jank18-2/gfx-new-carousel.txt)
- [jank18-2/gfx-new-catalog-full.txt](jank18-2/gfx-new-catalog-full.txt)
- [jank18-2/gfx-new-catalog.txt](jank18-2/gfx-new-catalog.txt)
- [jank18-2/gfx-new-gallery-full.txt](jank18-2/gfx-new-gallery-full.txt)
- [jank18-2/gfx-new-gallery.txt](jank18-2/gfx-new-gallery.txt)
- [jank18-2/gfx-new-news-full.txt](jank18-2/gfx-new-news-full.txt)
- [jank18-2/gfx-new-news.txt](jank18-2/gfx-new-news.txt)
- [jank18-2/gfx-totals.txt](jank18-2/gfx-totals.txt)
- [jank18-2/host-after-run.txt](jank18-2/host-after-run.txt)
- [jank18-2/job-status.txt](jank18-2/job-status.txt)
- [jank18-2/logcat-all.txt](jank18-2/logcat-all.txt)
- [jank18-2/logcat-dump.txt](jank18-2/logcat-dump.txt)
- [jank18-2/logcat-findings.txt](jank18-2/logcat-findings.txt)
- [jank18-2/logcat-live.txt](jank18-2/logcat-live.txt)
- [jank18-2/mem-base-after-scroll-raw.txt](jank18-2/mem-base-after-scroll-raw.txt)
- [jank18-2/mem-base-after-scroll.txt](jank18-2/mem-base-after-scroll.txt)
- [jank18-2/mem-new-after-scroll-raw.txt](jank18-2/mem-new-after-scroll-raw.txt)
- [jank18-2/mem-new-after-scroll.txt](jank18-2/mem-new-after-scroll.txt)
- [jank18-2/perf-new-after-clear-and-restart.xml](jank18-2/perf-new-after-clear-and-restart.xml)
- [jank18-2/perf-new-filled.xml](jank18-2/perf-new-filled.xml)
- [jank18-2/shots/jank/base-album-grid.png](jank18-2/shots/jank/base-album-grid.png)
- [jank18-2/shots/jank/base-carousel.png](jank18-2/shots/jank/base-carousel.png)
- [jank18-2/shots/jank/base-catalog.png](jank18-2/shots/jank/base-catalog.png)
- [jank18-2/shots/jank/base-gallery.png](jank18-2/shots/jank/base-gallery.png)
- [jank18-2/shots/jank/base-news.png](jank18-2/shots/jank/base-news.png)
- [jank18-2/shots/jank/new-album-grid.png](jank18-2/shots/jank/new-album-grid.png)
- [jank18-2/shots/jank/new-carousel.png](jank18-2/shots/jank/new-carousel.png)
- [jank18-2/shots/jank/new-catalog.png](jank18-2/shots/jank/new-catalog.png)
- [jank18-2/shots/jank/new-gallery.png](jank18-2/shots/jank/new-gallery.png)
- [jank18-2/shots/jank/new-news.png](jank18-2/shots/jank/new-news.png)
- [jank18-2/shots/perf/new-after-clear-and-restart-1.png](jank18-2/shots/perf/new-after-clear-and-restart-1.png)
- [jank18-2/shots/perf/new-after-clear-and-restart-2.png](jank18-2/shots/perf/new-after-clear-and-restart-2.png)
- [jank18-2/shots/perf/new-cleared.png](jank18-2/shots/perf/new-cleared.png)
- [jank18-2/shots/perf/new-filled-1.png](jank18-2/shots/perf/new-filled-1.png)
- [jank18-2/shots/perf/new-filled-2.png](jank18-2/shots/perf/new-filled-2.png)
- [jank18-2/summary.txt](jank18-2/summary.txt)
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
- [mem-base/shots/cycles-after-search.png](mem-base/shots/cycles-after-search.png)
- [mem-base/shots/cycles-book.png](mem-base/shots/cycles-book.png)
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
- [mem/shots/cycles-after-search.png](mem/shots/cycles-after-search.png)
- [mem/shots/cycles-book.png](mem/shots/cycles-book.png)
- [mem/summary.txt](mem/summary.txt)
- [monkey18/host-after-run.txt](monkey18/host-after-run.txt)
- [monkey18/job-status.txt](monkey18/job-status.txt)
- [monkey18/logcat-all.txt](monkey18/logcat-all.txt)
- [monkey18/logcat-dump.txt](monkey18/logcat-dump.txt)
- [monkey18/logcat-findings.txt](monkey18/logcat-findings.txt)
- [monkey18/logcat-live.txt](monkey18/logcat-live.txt)
- [monkey18/mem-after-monkey-raw.txt](monkey18/mem-after-monkey-raw.txt)
- [monkey18/mem-after-monkey.txt](monkey18/mem-after-monkey.txt)
- [monkey18/monkey.txt](monkey18/monkey.txt)
- [monkey18/shots/monkey-end.png](monkey18/shots/monkey-end.png)
- [monkey18/summary.txt](monkey18/summary.txt)
- [runs.txt](runs.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
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
- [zoom18/getevent.txt](zoom18/getevent.txt)
- [zoom18/host-after-run.txt](zoom18/host-after-run.txt)
- [zoom18/job-status.txt](zoom18/job-status.txt)
- [zoom18/logcat-all.txt](zoom18/logcat-all.txt)
- [zoom18/logcat-dump.txt](zoom18/logcat-dump.txt)
- [zoom18/logcat-findings.txt](zoom18/logcat-findings.txt)
- [zoom18/logcat-live.txt](zoom18/logcat-live.txt)
- [zoom18/mt.sh](zoom18/mt.sh)
- [zoom18/shots/zoom/base-d-0-1x.png](zoom18/shots/zoom/base-d-0-1x.png)
- [zoom18/shots/zoom/base-d-1-2x.png](zoom18/shots/zoom/base-d-1-2x.png)
- [zoom18/shots/zoom/base-d-2-panned.png](zoom18/shots/zoom/base-d-2-panned.png)
- [zoom18/shots/zoom/base-d-3-panned-down.png](zoom18/shots/zoom/base-d-3-panned-down.png)
- [zoom18/shots/zoom/base-d-4-reset.png](zoom18/shots/zoom/base-d-4-reset.png)
- [zoom18/shots/zoom/base-e-dismissed.png](zoom18/shots/zoom/base-e-dismissed.png)
- [zoom18/shots/zoom/base-p1-0-1x.png](zoom18/shots/zoom/base-p1-0-1x.png)
- [zoom18/shots/zoom/base-p1-1-pinched.png](zoom18/shots/zoom/base-p1-1-pinched.png)
- [zoom18/shots/zoom/base-p1-2-reset.png](zoom18/shots/zoom/base-p1-2-reset.png)
- [zoom18/shots/zoom/base-p2-0-1x.png](zoom18/shots/zoom/base-p2-0-1x.png)
- [zoom18/shots/zoom/base-p2-1-pinched.png](zoom18/shots/zoom/base-p2-1-pinched.png)
- [zoom18/shots/zoom/base-p2-2-reset.png](zoom18/shots/zoom/base-p2-2-reset.png)
- [zoom18/shots/zoom/base-p3-0-1x.png](zoom18/shots/zoom/base-p3-0-1x.png)
- [zoom18/shots/zoom/base-p3-1-pinched.png](zoom18/shots/zoom/base-p3-1-pinched.png)
- [zoom18/shots/zoom/base-p3-2-reset.png](zoom18/shots/zoom/base-p3-2-reset.png)
- [zoom18/shots/zoom/new-d-0-1x.png](zoom18/shots/zoom/new-d-0-1x.png)
- [zoom18/shots/zoom/new-d-1-2x.png](zoom18/shots/zoom/new-d-1-2x.png)
- [zoom18/shots/zoom/new-d-2-panned.png](zoom18/shots/zoom/new-d-2-panned.png)
- [zoom18/shots/zoom/new-d-3-panned-down.png](zoom18/shots/zoom/new-d-3-panned-down.png)
- [zoom18/shots/zoom/new-d-4-reset.png](zoom18/shots/zoom/new-d-4-reset.png)
- [zoom18/shots/zoom/new-e-dismissed.png](zoom18/shots/zoom/new-e-dismissed.png)
- [zoom18/shots/zoom/new-p1-0-1x.png](zoom18/shots/zoom/new-p1-0-1x.png)
- [zoom18/shots/zoom/new-p1-1-pinched.png](zoom18/shots/zoom/new-p1-1-pinched.png)
- [zoom18/shots/zoom/new-p1-2-reset.png](zoom18/shots/zoom/new-p1-2-reset.png)
- [zoom18/shots/zoom/new-p2-0-1x.png](zoom18/shots/zoom/new-p2-0-1x.png)
- [zoom18/shots/zoom/new-p2-1-pinched.png](zoom18/shots/zoom/new-p2-1-pinched.png)
- [zoom18/shots/zoom/new-p2-2-reset.png](zoom18/shots/zoom/new-p2-2-reset.png)
- [zoom18/shots/zoom/new-p3-0-1x.png](zoom18/shots/zoom/new-p3-0-1x.png)
- [zoom18/shots/zoom/new-p3-1-pinched.png](zoom18/shots/zoom/new-p3-1-pinched.png)
- [zoom18/shots/zoom/new-p3-2-reset.png](zoom18/shots/zoom/new-p3-2-reset.png)
- [zoom18/summary.txt](zoom18/summary.txt)
