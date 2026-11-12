# Wave 15 emulator check (7bd2513, test v1.2.29, vs v1.2.28 = 6e2c640)

Source: `debug/wave15-check` @ 781c5f5, last run 41249616504 (attempt 1), 2026-10-31T23:05:54Z. Runs: [runs.txt](runs.txt).
Emulators: google_apis x86_64 API 33 (parts ending in 34: API 34), pixel_6 profile, -gpu swangle_indirect, no camera (scan33cam: emulated back camera); app locale bg-BG unless a part says otherwise. APKs: minified prodRelease, x86_64 added, throwaway key; v1.2.29 versionCode 1488542, v1.2.28 versionCode 1488541.
Earlier: v1.2.28 full test [../v128b/index.md](../v128b/index.md).

## APK size (release configuration: arm64-v8a + armeabi-v7a, no x86_64)

```
== new 1.2.29 7bd2513: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4092878 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488542  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.29" (Raw: "1.2.29")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'android.permission.FOREGROUND_SERVICE' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
part                    compressed  uncompressed  files
classes*.dex               3342856       6610644      2
res                         468368        832408     57
resources.arsc              217040        217040      1
assets/                      19129         19129      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5342         23192      1
META-INF/                      846          1288      6
== base 1.2.28 6e2c640: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4290964 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488541  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.28" (Raw: "1.2.28")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.CAMERA' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.USE_BIOMETRIC' 'android.permission.USE_FINGERPRINT' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'android.permission.FOREGROUND_SERVICE' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
part                    compressed  uncompressed  files
classes*.dex               3510555       6893172      2
res                         468767        833220     58
resources.arsc              213416        213416      1
lib/arm64-v8a                26028         57912      4
lib/armeabi-v7a              22503         41024      4
assets/                      19172         19172      2
AndroidManifest.xml           4890         20880      1
META-INF/                      845          1288      6
```

| part | status |
|---|---|
| a11y | COMPLETE; part=a11y api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| abstart-1 | COMPLETE; part=abstart-1 api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| abstart-2 | COMPLETE; part=abstart-2 api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| gallery | COMPLETE; part=gallery api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| gallery34 | COMPLETE; part=gallery34 target=google_apis api=34 gpu=swangle_indirect camera-back=none emulator-step=success |
| home | COMPLETE; part=home target=google_apis api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| login | COMPLETE; part=login api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| look1 | COMPLETE; part=look1 api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| look2 | COMPLETE; part=look2 api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| look3 | COMPLETE; part=look3 api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| mem-base | COMPLETE; part=mem-base api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| mem | COMPLETE; part=mem api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| monkey | COMPLETE; part=monkey api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| notif | COMPLETE; part=notif target=google_apis api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| offline | COMPLETE; part=offline api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| rot | COMPLETE; part=rot api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| scan33 | COMPLETE; part=scan33 api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| scan33cam | COMPLETE; part=scan33cam api=33 gpu=swangle_indirect camera-back=emulated emulator-step=success |
| scan33keep | COMPLETE; part=scan33keep target=google_apis api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| scan34 | COMPLETE; part=scan34 api=34 gpu=swangle_indirect camera-back=none emulator-step=success |
| scan34ps | COMPLETE; part=scan34ps target=google_apis_playstore api=34 gpu=swangle_indirect camera-back=none emulator-step=success |
| tile33 | COMPLETE; part=tile33 target=google_apis api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| tile34 | COMPLETE; part=tile34 target=google_apis api=34 gpu=swangle_indirect camera-back=none emulator-step=success |
| widget | COMPLETE; part=widget api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| widgetlang | COMPLETE; part=widgetlang api=33 gpu=swangle_indirect camera-back=none emulator-step=success |
| widgetupd | COMPLETE; part=widgetupd api=33 gpu=swangle_indirect camera-back=none emulator-step=success |

## a11y

```
part a11y; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3264 ms
## A. Accessibility (uiautomator): main screens, density 420
A11Y 01-home: targets=16 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
   MISSING: View@[509,630][1080,1253]
A11Y 02-search-expanded: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 03-catalog: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 04-book: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
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
A11Y 13-album: targets=19 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 14-viewer: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 15-contacts: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 16-documents: targets=8 MISSING=0 DOUBLE=0 SAME-LABEL=1 SMALL(<48dp)=0
   SAME-LABEL: 'Отвори с приложение за PDF' x3
A11Y 17-login: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 18-scanner-manual: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
## A2. The same, English, font 1.3 (Home, Catalogue, Book, More)
A11Y 21-home-en-font130: targets=14 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1
   MISSING: View@[509,672][1080,1295]
   SMALL: 'All' 58x42dp
A11Y 22-catalog-en-font130: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 23-book-en-font130: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 24-more-en-font130: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
## A3. Totals over all screens
A11Y TOTAL: SMALL(<48dp)=1 SAME-LABEL=1 MISSING=5 DOUBLE=0 
## COMPLETE
## logcat findings
app pids seen: ['4857', '6891', '9489']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=62 total=229
== Davey: 14
   davey events=14 max=1252ms median=826ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-1

```
part abstart-1; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-0 FIRST-INSTALL catalogue results after 2646 ms
new-0 INDEX-READY #1 catalogue results after 2774 ms
new-0 INDEX-READY #2 catalogue results after 2370 ms
new-0 INDEX-READY #3 catalogue results after 2611 ms
START cold new-0         n=10 min=910ms median=1028ms max=1296ms  all=[1117, 1178, 996, 969, 1035, 914, 1101, 910, 1296, 1022]  states=['COLD']
START warm new-0         n=10 min=160ms median=271ms max=388ms  all=[253, 292, 216, 176, 388, 332, 289, 160, 364, 198]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2467 ms
base-1 INDEX-READY #1 catalogue results after 2384 ms
base-1 INDEX-READY #2 catalogue results after 2466 ms
base-1 INDEX-READY #3 catalogue results after 2455 ms
START cold base-1        n=10 min=873ms median=1040ms max=1317ms  all=[1013, 965, 873, 931, 1027, 1317, 1266, 1249, 1199, 1054]  states=['COLD']
START warm base-1        n=10 min=184ms median=286ms max=374ms  all=[275, 297, 320, 217, 246, 309, 274, 374, 355, 184]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2361 ms
new-1 INDEX-READY #1 catalogue results after 2674 ms
new-1 INDEX-READY #2 catalogue results after 2354 ms
new-1 INDEX-READY #3 catalogue results after 2235 ms
START cold new-1         n=10 min=932ms median=1081ms max=1433ms  all=[1157, 1433, 994, 1314, 1030, 1181, 1108, 932, 1054, 1031]  states=['COLD']
START warm new-1         n=10 min=263ms median=351ms max=501ms  all=[499, 274, 454, 362, 285, 340, 501, 337, 263, 371]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2289 ms
base-2 INDEX-READY #1 catalogue results after 2431 ms
base-2 INDEX-READY #2 catalogue results after 2338 ms
base-2 INDEX-READY #3 catalogue results after 2406 ms
START cold base-2        n=10 min=851ms median=934ms max=1054ms  all=[905, 1054, 1041, 1004, 890, 925, 955, 922, 851, 944]  states=['COLD']
START warm base-2        n=10 min=147ms median=295ms max=370ms  all=[364, 370, 293, 297, 318, 173, 162, 313, 175, 147]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2498 ms
new-2 INDEX-READY #1 catalogue results after 2403 ms
new-2 INDEX-READY #2 catalogue results after 2392 ms
new-2 INDEX-READY #3 catalogue results after 2427 ms
START cold new-2         n=10 min=846ms median=1014ms max=1208ms  all=[854, 1208, 1058, 972, 846, 1018, 983, 1083, 1010, 1022]  states=['COLD']
START warm new-2         n=10 min=151ms median=274ms max=487ms  all=[325, 238, 151, 176, 400, 359, 271, 487, 276, 273]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.29    n=20 min=846ms median=1030ms max=1433ms  all=[1157, 1433, 994, 1314, 1030, 1181, 1108, 932, 1054, 1031, 854, 1208, 1058, 972, 846, 1018, 983, 1083, 1010, 1022]  states=['COLD']
START cold BASE 1.2.28   n=20 min=851ms median=984ms max=1317ms  all=[1013, 965, 873, 931, 1027, 1317, 1266, 1249, 1199, 1054, 905, 1054, 1041, 1004, 890, 925, 955, 922, 851, 944]  states=['COLD']
START warm NEW 1.2.29    n=20 min=151ms median=331ms max=501ms  all=[499, 274, 454, 362, 285, 340, 501, 337, 263, 371, 325, 238, 151, 176, 400, 359, 271, 487, 276, 273]  states=['HOT']
START warm BASE 1.2.28   n=20 min=147ms median=295ms max=374ms  all=[275, 297, 320, 217, 246, 309, 274, 374, 355, 184, 364, 370, 293, 297, 318, 173, 162, 313, 175, 147]  states=['HOT']
START catalogue first-install new n=2 min=2361ms median=2430ms max=2498ms  all=[2361, 2498]  states=[]
START catalogue index-ready new n=6 min=2235ms median=2398ms max=2674ms  all=[2674, 2354, 2235, 2403, 2392, 2427]  states=[]
START catalogue first-install base n=2 min=2289ms median=2378ms max=2467ms  all=[2467, 2289]  states=[]
START catalogue index-ready base n=6 min=2338ms median=2418ms max=2466ms  all=[2384, 2466, 2455, 2431, 2338, 2406]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5866', '7022', '7333', '7483', '7632', '7701', '7848', '7905', '7975', '8041', '8090', '8141', '8201', '8259', '8623', '8884', '9035', '9197', '9347', '9414', '9463', '9511', '9575', '9630', '9677', '9724', '9942', '9995', '10375', '10635', '10772', '10918', '11059', '11127', '11190', '11239', '11291', '11341', '11393', '11442', '11499', '11549', '12016', '12324', '12459', '12598', '12736', '12803', '12852', '12900', '12950', '12999', '13047', '13095', '13144', '13193', '13554', '13815', '13953', '14096', '14236', '14304', '14357', '14407', '14460', '14510', '14560', '14611', '14660', '14709']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 16
   skipped-frames events=16 max=45 total=556
== Davey: 9
   davey events=9 max=1196ms median=810ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): base-0 new-1 base-1 new-2 base-2
## AB base-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-0: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-0 FIRST-INSTALL catalogue results after 2861 ms
base-0 INDEX-READY #1 catalogue results after 3150 ms
base-0 INDEX-READY #2 catalogue results after 3291 ms
base-0 INDEX-READY #3 catalogue results after 3231 ms
START cold base-0        n=10 min=1049ms median=1292ms max=1536ms  all=[1536, 1291, 1474, 1245, 1410, 1413, 1146, 1294, 1049, 1173]  states=['COLD']
START warm base-0        n=10 min=239ms median=426ms max=619ms  all=[455, 619, 425, 344, 286, 428, 239, 317, 518, 487]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2545 ms
new-1 INDEX-READY #1 catalogue results after 2456 ms
new-1 INDEX-READY #2 catalogue results after 2404 ms
new-1 INDEX-READY #3 catalogue results after 2518 ms
START cold new-1         n=10 min=1113ms median=1351ms max=1725ms  all=[1300, 1416, 1309, 1338, 1725, 1658, 1449, 1253, 1113, 1364]  states=['COLD']
START warm new-1         n=10 min=213ms median=399ms max=559ms  all=[459, 440, 366, 213, 391, 441, 285, 407, 559, 295]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2568 ms
base-1 INDEX-READY #1 catalogue results after 2542 ms
base-1 INDEX-READY #2 catalogue results after 2475 ms
base-1 INDEX-READY #3 catalogue results after 2705 ms
START cold base-1        n=10 min=1070ms median=1242ms max=1570ms  all=[1179, 1291, 1236, 1342, 1228, 1332, 1570, 1249, 1077, 1070]  states=['COLD']
START warm base-1        n=10 min=190ms median=382ms max=616ms  all=[393, 284, 616, 375, 388, 429, 494, 269, 307, 190]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2563 ms
new-2 INDEX-READY #1 catalogue results after 2538 ms
new-2 INDEX-READY #2 catalogue results after 2349 ms
new-2 INDEX-READY #3 catalogue results after 2606 ms
START cold new-2         n=10 min=1228ms median=1286ms max=1529ms  all=[1529, 1375, 1413, 1255, 1228, 1313, 1342, 1251, 1258, 1246]  states=['COLD']
START warm new-2         n=10 min=301ms median=437ms max=569ms  all=[443, 486, 375, 569, 356, 539, 301, 535, 431, 307]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2462 ms
base-2 INDEX-READY #1 catalogue results after 2481 ms
base-2 INDEX-READY #2 catalogue results after 2479 ms
base-2 INDEX-READY #3 catalogue results after 2579 ms
START cold base-2        n=10 min=1136ms median=1258ms max=1657ms  all=[1433, 1188, 1515, 1200, 1252, 1657, 1265, 1219, 1136, 1352]  states=['COLD']
START warm base-2        n=10 min=189ms median=364ms max=655ms  all=[197, 233, 366, 189, 456, 353, 479, 531, 362, 655]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.29    n=20 min=1113ms median=1326ms max=1725ms  all=[1300, 1416, 1309, 1338, 1725, 1658, 1449, 1253, 1113, 1364, 1529, 1375, 1413, 1255, 1228, 1313, 1342, 1251, 1258, 1246]  states=['COLD']
START cold BASE 1.2.28   n=20 min=1070ms median=1250ms max=1657ms  all=[1179, 1291, 1236, 1342, 1228, 1332, 1570, 1249, 1077, 1070, 1433, 1188, 1515, 1200, 1252, 1657, 1265, 1219, 1136, 1352]  states=['COLD']
START warm NEW 1.2.29    n=20 min=213ms median=419ms max=569ms  all=[459, 440, 366, 213, 391, 441, 285, 407, 559, 295, 443, 486, 375, 569, 356, 539, 301, 535, 431, 307]  states=['HOT']
START warm BASE 1.2.28   n=20 min=189ms median=370ms max=655ms  all=[393, 284, 616, 375, 388, 429, 494, 269, 307, 190, 197, 233, 366, 189, 456, 353, 479, 531, 362, 655]  states=['HOT']
START catalogue first-install new n=2 min=2545ms median=2554ms max=2563ms  all=[2545, 2563]  states=[]
START catalogue index-ready new n=6 min=2349ms median=2487ms max=2606ms  all=[2456, 2404, 2518, 2538, 2349, 2606]  states=[]
START catalogue first-install base n=2 min=2462ms median=2515ms max=2568ms  all=[2568, 2462]  states=[]
START catalogue index-ready base n=6 min=2475ms median=2512ms max=2705ms  all=[2542, 2475, 2705, 2481, 2479, 2579]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5420', '6382', '7061', '7376', '7703', '7776', '7830', '7894', '7966', '8029', '8088', '8139', '8190', '8247', '8664', '8951', '9095', '9240', '9390', '9462', '9520', '9578', '9638', '9728', '9786', '9840', '9906', '9963', '10360', '10615', '10757', '10902', '11064', '11127', '11178', '11232', '11285', '11339', '11392', '11445', '11498', '11566', '12072', '12390', '12532', '12726', '12875', '12945', '12997', '13049', '13104', '13157', '13212', '13266', '13320', '13376', '13767', '14021', '14160', '14301', '14444', '14515', '14567', '14618', '14672', '14726', '14854', '14911', '14963', '15016']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 41
   skipped-frames events=41 max=55 total=1509
== Davey: 37
   davey events=37 max=1254ms median=773ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## gallery

```
part gallery; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15700 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3000 ms
## G1. Album 1 online (the album data is cached); then the image caches are emptied
ALBUM online: not-loaded=0 missing=1 retry-icons=0 cells=18; 404 tiles: content-desc="Стопанският двор на ТКЗС в Яворец — снимката липсва" content-desc="Техниката на стопанството — снимката липсва" content-desc="Хората на труда — снимката липсва" 
app cache before: 1520	/data/data/org.chyavorec.app/cache/image_cache 744	/data/data/org.chyavorec.app/cache/http 
## G2. Offline: thumbnails show 'Не се зареди' (not 'Снимката липсва')
offline: airplane=1
gallery offline: text="Няма интернет връзка. Показваме последно наличните данни." text="Последна синхронизация: Преди 0 минути" content-desc="Опитай отново" content-desc="Не се зареди" text="29 снимки" text="Фотодокум
ALBUM offline: not-loaded=1 missing=0 retry-icons=1 cells=30; sample: content-desc="Сградата на читалището в Яворец — снимката не се зареди" content-desc="Фолклорен състав при читалището — снимката не се зареди" content-desc="Хорът на сцена — снимката не се зареди" 
ALBUM offline scrolled: not-loaded=1 missing=0 retry-icons=1 cells=30
## G3. Reconnect without touching: the thumbnails recover
ALBUM +5s after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=15
ALBUM +15s after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=15
ALBUM +30s after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=15
ALBUM top after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=18
## G4. A real 404 photo: '<title> — снимката липсва'; the viewer on it
404 tiles: content-desc="Стопанският двор на ТКЗС в Яворец — снимката липсва" content-desc="Техниката на стопанството — снимката липсва" content-desc="Хората на труда — снимката липсва" content-desc="Фермите на стопанството — снимката липсва" ; not-loaded here: 
VIEWER on a 404 photo: text="8 / 29  ·  Стопанският двор на ТКЗС в Яворец"; 'Снимката липсва на сайта' SHOWN; retry absent; text="Снимката липсва на сайта." content-desc="Затвори" text="8 / 29  ·  Стопанският двор на ТКЗС в Яворец" content-desc="Сподели" 
## G5. Viewer: a tap hides and shows the bars (immersive)
VIEWER 20-viewer-start: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
VIEWER 21-after-tap1: counter ''; close button HIDDEN; album grid no; bars: ; BBOX [0,128][1080,1537] 1080x1409 of 1080x2400 (59% of the screen)
VIEWER 22-after-tap2: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
## G6. Vertical drag: a short one returns, a long one dismisses
VIEWER 30-after-short-drag: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
VIEWER 31-after-long-drag: counter ''; close button HIDDEN; album grid VISIBLE; bars: ; BBOX [0,0][1080,2400] 1080x2400 of 1080x2400 (100% of the screen)
## G7. Zoom still works: double tap 2×, pinch, pan; no dismiss while zoomed; reset
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
ZOOM: diff 1x→2x 0.0, 2x→pinched 75.2 (≈0 = no change)
VIEWER 43-vertical-drag-while-zoomed: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,46][1080,2400] 1080x2354 of 1080x2400 (98% of the screen)
swipe while zoomed: text="1 / 29  ·  Сградата на читалището в Яворец" (same photo = panned)
ZOOM reset: diff 1x vs after reset 0.3
## G8. Back gesture (gesture navigation; API 33): the photo scales during the gesture
gesture nav:  navigation_mode=2
VIEWER 60-before-gesture: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
mid-gesture 30%: BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
mid-gesture 50%: BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
VIEWER 63-after-cancelled-gesture: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
full gesture midway: BBOX [0,46][1080,2374] 1080x2328 of 1080x2400 (97% of the screen)
VIEWER 65-after-full-gesture: counter ''; close button HIDDEN; album grid VISIBLE; bars: ; BBOX [0,0][1080,2400] 1080x2400 of 1080x2400 (100% of the screen)
## COMPLETE
## logcat findings
app pids seen: ['5376', '6819', '7610']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=30 total=60
== Davey: 2
   davey events=2 max=1152ms median=1088ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## gallery34

```
part gallery34; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323595 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## G0. API 34: predictive back animations on (developer option) before the framework (re)starts
enable_back_animation=null persist.wm.debug.predictive_back=1
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3790 ms
## G1. Album 1 online (the album data is cached); then the image caches are emptied
ALBUM online: not-loaded=0 missing=1 retry-icons=0 cells=18; 404 tiles: content-desc="Стопанският двор на ТКЗС в Яворец — снимката липсва" content-desc="Техниката на стопанството — снимката липсва" content-desc="Хората на труда — снимката липсва" 
app cache before: 1172	/data/data/org.chyavorec.app/cache/image_cache 744	/data/data/org.chyavorec.app/cache/http 
## G2. Offline: thumbnails show 'Не се зареди' (not 'Снимката липсва')
offline: airplane=1
gallery offline: text="Няма интернет връзка. Показваме последно наличните данни." text="Последна синхронизация: Преди 0 минути" content-desc="Опитай отново" content-desc="Не се зареди" text="29 снимки" text="Фотодокум
ALBUM offline: not-loaded=1 missing=0 retry-icons=1 cells=30; sample: content-desc="Сградата на читалището в Яворец — снимката не се зареди" content-desc="Фолклорен състав при читалището — снимката не се зареди" content-desc="Хорът на сцена — снимката не се зареди" 
ALBUM offline scrolled: not-loaded=1 missing=0 retry-icons=1 cells=27
## G3. Reconnect without touching: the thumbnails recover
ALBUM +5s after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=15
ALBUM +15s after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=15
ALBUM +30s after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=15
ALBUM top after reconnect: not-loaded=0 missing=1 retry-icons=0 cells=18
## G4. A real 404 photo: '<title> — снимката липсва'; the viewer on it
404 tiles: content-desc="Стопанският двор на ТКЗС в Яворец — снимката липсва" content-desc="Техниката на стопанството — снимката липсва" content-desc="Хората на труда — снимката липсва" content-desc="Фермите на стопанството — снимката липсва" ; not-loaded here: 
VIEWER on a 404 photo: text="8 / 29  ·  Стопанският двор на ТКЗС в Яворец"; 'Снимката липсва на сайта' SHOWN; retry absent; text="Снимката липсва на сайта." content-desc="Затвори" text="8 / 29  ·  Стопанският двор на ТКЗС в Яворец" content-desc="Сподели" 
## G5. Viewer: a tap hides and shows the bars (immersive)
VIEWER 20-viewer-start: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,47][1080,2374] 1080x2327 of 1080x2400 (97% of the screen)
VIEWER 21-after-tap1: counter ''; close button HIDDEN; album grid no; bars: ; BBOX [0,128][1080,1537] 1080x1409 of 1080x2400 (59% of the screen)
VIEWER 22-after-tap2: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,47][1080,2374] 1080x2327 of 1080x2400 (97% of the screen)
## G6. Vertical drag: a short one returns, a long one dismisses
VIEWER 30-after-short-drag: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,47][1080,2374] 1080x2327 of 1080x2400 (97% of the screen)
VIEWER 31-after-long-drag: counter ''; close button HIDDEN; album grid VISIBLE; bars: ; BBOX [0,0][1080,2400] 1080x2400 of 1080x2400 (100% of the screen)
## G7. Zoom still works: double tap 2×, pinch, pan; no dismiss while zoomed; reset
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
ZOOM: diff 1x→2x 53.5, 2x→pinched 74.9 (≈0 = no change)
VIEWER 43-vertical-drag-while-zoomed: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,47][1080,2400] 1080x2353 of 1080x2400 (98% of the screen)
swipe while zoomed: text="1 / 29  ·  Сградата на читалището в Яворец" (same photo = panned)
ZOOM reset: diff 1x vs after reset 62.7
## G8. Back gesture (gesture navigation; API 34): the photo scales during the gesture
gesture nav:  navigation_mode=2
predictive back animation setting: 1
VIEWER 60-before-gesture: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,47][1080,2400] 1080x2353 of 1080x2400 (98% of the screen)
PHOTO EDGES 60-before-gesture (row y=1488): HSPAN x 0..1079 (width 1080 of 1080)
PHOTO EDGES 61-gesture-30pct (row y=1488): HSPAN x 0..1079 (width 1080 of 1080)
PHOTO EDGES 62-gesture-50pct (row y=1488): HSPAN x 0..1079 (width 1080 of 1080)
PHOTO EDGES 62b-gesture-70pct (row y=1488): HSPAN x 0..1079 (width 1080 of 1080)
VIEWER 63-after-cancelled-gesture: counter 'text="1 / 29  ·  Сградата на читалището в Яворец"'; close button shown; album grid no; bars: ; BBOX [0,47][1080,2400] 1080x2353 of 1080x2400 (98% of the screen)
full gesture midway: BBOX [0,47][1080,2400] 1080x2353 of 1080x2400 (98% of the screen)
VIEWER 65-after-full-gesture: counter ''; close button HIDDEN; album grid VISIBLE; bars: ; BBOX [0,0][1080,2400] 1080x2400 of 1080x2400 (100% of the screen)
## COMPLETE
## logcat findings
app pids seen: ['8377', '9219', '10415']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=43 total=287
== Davey: 16
   davey events=16 max=1550ms median=916ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## home

```
part home; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323595 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## HM-base. v1.2.28: 'Нови постъпления' and the events section
installed base: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323595;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
NEW ARRIVALS base [0]: SECTION y 1236..2078: 2 texts, repeated: [] TITLES: Към каталога | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS base [1]: SECTION y 1236..2078: 2 texts, repeated: [] TITLES: Към каталога | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS base [2]: SECTION y 1236..2078: 4 texts, repeated: [] TITLES: Към каталога | Дъхът на спомените — том 3 | Анета Ангелова | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS base [3]: SECTION y 1236..2078: 4 texts, repeated: [] TITLES: Към каталога | Дъхът на спомените — том 3 | Анета Ангелова | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS base [4]: SECTION y 1236..2078: 4 texts, repeated: [] TITLES: Към каталога | Дъхът на спомените — том 3 | Анета Ангелова | НАЙ-ЧЕТЕНИ И НОВИ 
EVENTS base cold #1: events section filled after 4582 ms (uiautomator polling, ~1 s resolution); HSECTIONS Последни новини@809:8 Предстоящи събития@1930:12 Нови постъпления@2470:5 За теб@3312:8
EVENTS base cold #2: events section filled after 3437 ms (uiautomator polling, ~1 s resolution); HSECTIONS Последни новини@809:8 Предстоящи събития@1930:12 Нови постъпления@2470:5 За теб@3312:8
EVENTS base cold #3: events section filled after 3625 ms (uiautomator polling, ~1 s resolution); HSECTIONS Последни новини@809:8 Предстоящи събития@1930:12 Нови постъпления@2470:5 За теб@3312:8
## HM-new. v1.2.29: fresh install, the first search, 'Нови постъпления' and the events section
## HS0. Search right after a fresh install (catalogue index not built yet): loading indicator
installed new: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
HS0 dump 1: loading indicator absent; results yes; empty-state no
HS0 dump 2: loading indicator absent; results yes; empty-state no
HS0 dump 3: loading indicator absent; results yes; empty-state no
HS0 dump 4: loading indicator absent; results yes; empty-state no
HS0 dump 5: loading indicator absent; results yes; empty-state no
HS0 dump 6: loading indicator absent; results yes; empty-state no
new catalogue results after 2408 ms
NEW ARRIVALS new [0]: SECTION y 700..1542: 2 texts, repeated: [] TITLES: Към каталога | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS new [1]: SECTION y 700..1542: 2 texts, repeated: [] TITLES: Към каталога | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS new [2]: SECTION y 700..1542: 2 texts, repeated: [] TITLES: Към каталога | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS new [3]: SECTION y 700..1542: 2 texts, repeated: [] TITLES: Към каталога | НАЙ-ЧЕТЕНИ И НОВИ 
NEW ARRIVALS new [4]: SECTION y 700..1542: 2 texts, repeated: [] TITLES: Към каталога | НАЙ-ЧЕТЕНИ И НОВИ 
EVENTS new cold #1: events section filled after 2909 ms (uiautomator polling, ~1 s resolution); HSECTIONS Последни новини@809:8 Предстоящи събития@1930:12 Нови постъпления@2470:5 За теб@3312:8
EVENTS new cold #2: events section filled after 2966 ms (uiautomator polling, ~1 s resolution); HSECTIONS Последни новини@809:8 Предстоящи събития@1930:12 Нови постъпления@2470:5 За теб@3312:8
EVENTS new cold #3: events section filled after 3553 ms (uiautomator polling, ~1 s resolution); HSECTIONS Последни новини@809:8 Предстоящи събития@1930:12 Нови постъпления@2470:5 За теб@3312:8
## HC1. Hero carousel: auto-advance (animations on), swipe
carousel card: 126 809 954 1432 | СЪБИТИЕ Празника „38 читалища. Един ден. Една общност.“, който се проведе в парк
CAROUSEL auto-advance: diff 0→6.5 s 44.8, 6.5→13 s 59.6 (≈0 = no change)
CAROUSEL swipe left: diff 0.0; focal:  СЪСТАВИ 45-ти общински фолклорен събор на народното творчество „От Тимок до Вит
CAROUSEL swipe right: diff 59.6
CAROUSEL after ~60 s (the ~30 s auto-advance budget): diff 0.0
## HC2. Carousel: dark, large font, landscape
TEXT SIZE now: text="Много голям" 
CAROUSEL font 1.3 + Много голям: 
TEXT SIZE now: text="Стандартен" 
CAROUSEL landscape 2400x1080: 380 501 2358 651 | Лента за търсене Търси книга, новина, събитие… Сканирай баркод
## HS1. Open a result and come back: the results without the keyboard
result opened: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
SEARCH back from the result: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false; results visible yes
## HS2. One Back collapses; the collapsed bar shows the placeholder
SEARCH after ONE Back: expanded=no query-12=no hint=yes tabbar=yes app-in-front=yes IME mInputShown=false
## HS3. A result, then another tab, then Home: the bar stays collapsed
result opened: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
on the result: tab bar hidden
on the other tab: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" 
Home tab again: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026"  (the book stays on top of the Home stack)
SEARCH after another tab and Back from the book to Home (should be collapsed): expanded=no query-12=no hint=yes tabbar=yes app-in-front=yes IME mInputShown=false
## HS4. 'Покажи всички (N)'
show-all buttons: text="Покажи всички (20)" 
after 'Покажи всички': chip Книги checked=true selected=false bounds=[281,339][480,465]; show-all left 0; text="12" content-desc="Лента за търсене" content-desc="Назад" content-desc="Изчисти" text="Всички" text="Книги" text="Новини" text="Събития" text="Страници" text="Книги" text="Спомен за здрача" text="Веска Пенчева" 
## COMPLETE
## logcat findings
app pids seen: ['5839', '7156', '7880', '8035', '8132', '8404', '9047', '9518', '9609', '9699', '9837', '10304', '10935', '11434', '11723', '12143', '12492']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 12
   skipped-frames events=12 max=112 total=666
== Davey: 25
   davey events=25 max=2345ms median=857ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## login

```
part login; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4716 ms
## LG1. Sign-in on a fresh install (password-manager flag unset): no credential query, no crash (API 33)
GMS versionName=23.18.18(190800-535401451) ; Play Store 1 ; Google accounts 0; credential providers:     com.google.android.gms/.auth.api.credentials.credman.service.PasswordAndPasskeyService 
LOGIN +2s: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от And
LOGIN +6s: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от And
LOGIN credential traffic after open: []
LOGIN activities started after open: []
## LG2. Card number field
LOGIN card field after typing 'ab-123': FIELD 0: text='ab-123' password=false focused=true hint=''
## LG3. PIN (OutlinedSecureTextField): the last digit shows briefly; the eye toggle
PIN after '1' +2557 ms: FIELD 1: text='1' password=true focused=true hint=''
PIN after '1' +7154 ms: FIELD 1: text='1' password=true focused=true hint=''
PIN after '2' +2165 ms: FIELD 1: text='12' password=true focused=true hint=''
PIN after '2' +6860 ms: FIELD 1: text='12' password=true focused=true hint=''
PIN after '3' +2652 ms: FIELD 1: text='123' password=true focused=true hint=''
PIN after '3' +8020 ms: FIELD 1: text='123' password=true focused=true hint=''
PIN after '4' +2817 ms: FIELD 1: text='1234' password=true focused=true hint=''
PIN after '4' +8218 ms: FIELD 1: text='1234' password=true focused=true hint=''
PIN eye ON: FIELD 1: text='1234' password=true focused=true hint=''; toggle now: content-desc="Скрий ПИН-а"
PIN eye OFF: FIELD 1: text='1234' password=true focused=true hint=''; toggle now: content-desc="Покажи ПИН-а"
## LG4. 'Запомни ме' row: one labelled checkbox
before: CHECKABLE class=android.view.View label='Запомни ме на това устройство' checked=true clickable=true bounds=[63,1161][1017,1287] nested-checkables=0
'Запомни ме' text nodes: 1
after tap on the text: CHECKABLE class=android.view.View label='Запомни ме на това устройство' checked=false clickable=true bounds=[63,1161][1017,1287] nested-checkables=0
A11Y login-15: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
LOGIN after Back: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" t; pid '4985'
LOGIN second open: fg org.chyavorec.app/org.chyavorec.app.MainActivity; credential traffic: []
## COMPLETE
## logcat findings
app pids seen: ['4985']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=52 total=244
== Davey: 7
   davey events=7 max=2376ms median=1076ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look1

```
part look1; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2866 ms
## L bg-light
matrix bg-light: 1080x2400 density 420, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix bg-light: news title OK; segments News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
matrix bg-light scanner: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 и
matrix bg-light tab labels: home catalog my news more 
## L bg-dark
matrix bg-dark: 1080x2400 density 420, font_scale 1.0, night Night mode: yes, locale [bg-BG]
matrix bg-dark: news title OK; segments News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
matrix bg-dark scanner: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 и
matrix bg-dark tab labels: home catalog my news more 
## L en-dark
matrix en-dark: 1080x2400 density 420, font_scale 1.0, night Night mode: yes, locale [en-US]
matrix en-dark: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
matrix en-dark scanner: text="Enter the number manually" text="The barcode scanner comes with Google Play services — they’re missing here, or the scanner is still downloading. Enter the number manually or try again in a mome
matrix en-dark tab labels: home catalog my news more 
## L en-light
matrix en-light: 1080x2400 density 420, font_scale 1.0, night Night mode: no, locale [en-US]
matrix en-light: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
matrix en-light scanner: text="Enter the number manually" text="The barcode scanner comes with Google Play services — they’re missing here, or the scanner is still downloading. Enter the number manually or try again in a mome
matrix en-light tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['4708', '6850', '9509', '11399', '13046']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 24
   skipped-frames events=24 max=66 total=1009
== Davey: 52
   davey events=52 max=1629ms median=944ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look2

```
part look2; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4439 ms
TEXT SIZE now: text="Много голям" 
## L font130-xlarge
matrix font130-xlarge: 1080x2400 density 420, font_scale 1.3, night Night mode: no, locale [bg-BG]
matrix font130-xlarge: news title OK; segments News[checked=false selected=false bounds=[42,307][538,438]] Calendar[checked=true selected=false bounds=[538,307][1038,438]] subscribe-action=visible
matrix font130-xlarge scanner: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 и
matrix font130-xlarge tab labels: home catalog my news more 
TEXT SIZE now: text="Стандартен" 
## L landscape
landscape check: mRotation=ROTATION_90} cur=2400x1080
matrix landscape: 2400x1080 density 420, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix landscape: news title OK; segments News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
matrix landscape scanner: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 и
matrix landscape tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['4496', '7265', '10378']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 36
   skipped-frames events=36 max=119 total=1762
== Davey: 67
   davey events=67 max=3228ms median=1092ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look3

```
part look3; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2547 ms
## L small-320dp
matrix small-320dp: 640x1280 density 320, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix small-320dp: news title OK; segments News[checked=false selected=false bounds=[32,204][231,300]] Calendar[checked=true selected=false bounds=[231,204][432,300]] subscribe-action=visible
matrix small-320dp scanner: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 и
matrix small-320dp tab labels: home catalog my news more 
## L tablet-800dp
matrix tablet-800dp: 1600x2560 density 320, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix tablet-800dp: news title OK; segments News[checked=false selected=false bounds=[192,273][431,369]] Calendar[checked=true selected=false bounds=[431,273][672,369]] subscribe-action=visible
matrix tablet-800dp scanner: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 и
matrix tablet-800dp tab labels: home catalog my news more 
## L tablet-land
landscape check: mRotation=ROTATION_90} cur=2560x1600
matrix tablet-land: 2560x1600 density 320, font_scale 1.0, night Night mode: no, locale [bg-BG]
matrix tablet-land: news title OK; segments News[checked=false selected=false bounds=[466,184][705,280]] Calendar[checked=true selected=false bounds=[705,184][946,280]] subscribe-action=visible
matrix tablet-land scanner: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 и
matrix tablet-land tab labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['6176', '7515', '9552', '11521']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=40 total=78
== Davey: 2
   davey events=2 max=1102ms median=970ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem-base

```
part mem-base; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
installed base: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2835 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=59743KB  Java=7320KB  Native=25716KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=66522KB  Java=8060KB  Native=25716KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=105515KB  Java=11792KB  Native=47096KB  Graphics=0KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=127359KB  Java=17216KB  Native=30164KB  Graphics=0KB  Views=22 Activities=1
MEM c10 (trimmed)    PSS=117854KB  Java=10564KB  Native=26964KB  Graphics=0KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=119064KB  Java=10500KB  Native=27516KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=127262KB  Java=12764KB  Native=32704KB  Graphics=0KB  Views=22 Activities=1
MEM c20 (trimmed)    PSS=123992KB  Java=10512KB  Native=31740KB  Graphics=0KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=123580KB  Java=10620KB  Native=32180KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=129759KB  Java=15172KB  Native=33432KB  Graphics=0KB  Views=22 Activities=1
MEM c30 (trimmed)    PSS=123839KB  Java=10580KB  Native=32124KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 7322; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=66522 c5=105515 c10=117854 c15=119064 c20=123992 c25=123580 c30=123839
LEAK TREND (cycles 10-30, least squares): +330 KB per cycle (+6.4 MB over 20 cycles)
## COMPLETE
## logcat findings
app pids seen: ['5838', '7322']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=39 total=169
== Davey: 26
   davey events=26 max=993ms median=736ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem

```
part mem; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2675 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=61857KB  Java=8528KB  Native=34876KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=60289KB  Java=7224KB  Native=16112KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=101180KB  Java=12008KB  Native=32244KB  Graphics=0KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=127674KB  Java=17960KB  Native=51908KB  Graphics=0KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=117417KB  Java=11496KB  Native=48320KB  Graphics=0KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=119142KB  Java=11424KB  Native=48540KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=139811KB  Java=26732KB  Native=52888KB  Graphics=0KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=121019KB  Java=11544KB  Native=49332KB  Graphics=0KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=120455KB  Java=11564KB  Native=49084KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=122309KB  Java=16292KB  Native=49308KB  Graphics=0KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=108603KB  Java=11548KB  Native=40412KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 6730; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=60289 c5=101180 c10=117417 c15=119142 c20=121019 c25=120455 c30=108603
LEAK TREND (cycles 10-30, least squares): -326 KB per cycle (-6.4 MB over 20 cycles)
## COMPLETE
## logcat findings
app pids seen: ['5224', '6730']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 18
   skipped-frames events=18 max=72 total=646
== Davey: 47
   davey events=47 max=1206ms median=761ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey

```
part monkey; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2997 ms
## K. Monkey 6000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=6359
MONKEY exit=0 duration=328 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6359'; airplane=0; camera: 
MEM after-monkey (raw) PSS=77933KB  Java=14444KB  Native=19812KB  Graphics=0KB  Views=28 Activities=1
MEM after-monkey (trimmed) PSS=78657KB  Java=12172KB  Native=17640KB  Graphics=0KB  Views=26 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4835', '6359']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 66
   skipped-frames events=66 max=114 total=2744
== Davey: 198
   davey events=198 max=2112ms median=883ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## notif

```
part notif; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323595 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## N0. Handlers on the image; settings seeded before the first launch (last_news_id = a stale id)
calendar (INSERT event) handlers: [    com.google.android.calendar/com.android.calendar.event.LaunchInfoActivity ]
geo handlers: [    com.google.android.apps.maps/com.google.android.maps.MapsActivity ]
prefs written: 38 bytes, 1 keys
seeded: -rw------- 1 u0_a174 u0_a174 u:object_r:app_data_file:s0:c174,c256,c512,c768  38 2026-10-31 22:25 settings.preferences_pb
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2735 ms
## N1. New-article notification (PeriodicSyncWorker; the stored newest id is stale)
app jobs: 0 3 
NOTIF total for org.chyavorec.app: 0
jobscheduler run -f 0: Running job [FORCED]
jobscheduler run -f 3: Running job [FORCED]
NOTIF total for org.chyavorec.app: 0
(the periodic work has a flex window: its first run is ~4 h after it is enqueued — moving it back in the WorkManager db)
WorkManager db: -rw-rw---- 1 u0_a174 u0_a174 106496 2026-10-31 22:25 androidx.work.workdb -rw------- 1 u0_a174 u0_a174 32768 2026-10-31 22:26 androidx.work.workdb-shm -rw------- 1 u0_a174 u0_a174 424392 2026-10-31 22:26 androidx.work.workdb-wal 
WORKSPEC PeriodicSyncWorker state 0 periods 0 last_enqueue 1793485526146 interval 21600000 flex 7200000
WORKSPEC MessagePollWorker state 2 periods 0 last_enqueue 1793485527153 interval 0 flex 0
WORKSPEC MessagePollWorker state 2 periods 0 last_enqueue 1793485528404 interval 0 flex 0
WORKSPEC MessagePollWorker state 0 periods 0 last_enqueue 1793485586460 interval 0 flex 0
WORK REWIND: PeriodicSyncWorker rows moved 7 h back: 1
app jobs after the rewind: 6 7 
jobscheduler run -f 6: Running job [FORCED]
jobscheduler run -f 7: Running job [FORCED]
NOTIF id=500 channel=news title=String (Нова публикация) text=String (Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец.) template=String (android.app.Notification$BigPictureStyle) picture=yes Bitmap (746x348) largeIcon=yes actions=1 ['Сподели']
NOTIF total for org.chyavorec.app: 1
settings after the sync: PREF last_news_id = news-mvbb3zcx3c3n PREF bg_step_news = 1793485620756 PREF bg_step_loans = 1793485620756 
## N2. Shade: the new-article notification expanded
SHADE 20-news expanded: buttons [text="Сподели" ]; big picture view yes; text="On" content-desc="Internet,AndroidWifi" text="Internet" text="On" content-desc="Bluetooth." text="Bluetooth" text="Flashlight" text="Off" content-desc="Do Not Disturb." text="Do Not Disturb" text="Silent" content-desc="Clear all silent notifications" text="Chitalishte Yavorets" text="•" text="now" content-desc="C
ACTION 'Сподели' → fg android/com.android.internal.app.ChooserActivity; text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец.&#10;https://chyavorec.org/news/folklorno-pateshestvie-iz-pirinska-rodopska-i-trakiyska-oblast-v-yavor" text="Copy" text="
## N3. Event reminder: 'Напомни ми' on an upcoming event, then the work forced
EVENT candidate 1: text="Ден на народните будители" text="1 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Национален" text="Тържество с ученически рецитали
EVENT candidate 2: text="Михаил В. Ломоносов" text="8 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" text="315 г. от рождението на Михаил В. Лом
REMINDER set on: text="Михаил В. Ломоносов" text="8 ноември 2026" ; snackbar: text="Ще получиш напомняне на 7 ноември 2026, 18:00."; bell now: on; image on the event screen: 0
EVENT candidate 3: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" text="Организацията на ООН за о
REMINDER set on: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" ; snackbar: text="Ще получиш напомняне на 15 ноември 2026, 18:00."; bell now: on; image on the event screen: 0
EVENT candidate 4: text="Панайот Пипков · Ден на семейството" text="21 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Читалище" text="155 г. от рождението н
REMINDER set on: text="Панайот Пипков · Ден на семейството" text="21 ноември 2026" ; snackbar: text="Ще получиш напомняне на 20 ноември 2026, 18:00."; bell now: on; image on the event screen: 0
REMINDER: yes
app jobs: 9 10 11 12 13 
jobscheduler run -f 9: Running job [FORCED]
jobscheduler run -f 10: Running job [FORCED]
jobscheduler run -f 11: Running job [FORCED]
jobscheduler run -f 12: Running job [FORCED]
jobscheduler run -f 13: Running job [FORCED]
NOTIF id=37892 channel=events title=String (Напомняне за събитие) text=String (Панайот Пипков · Ден на семейството) template=String (android.app.Notification$BigTextStyle) picture=NO largeIcon=yes actions=1 ['Добави в календара']
NOTIF id=37891 channel=events title=String (Напомняне за събитие) text=String (80 г. от създаването на ЮНЕСКО) template=String (android.app.Notification$BigTextStyle) picture=NO largeIcon=yes actions=1 ['Добави в календара']
NOTIF id=37890 channel=events title=String (Напомняне за събитие) text=String (Михаил В. Ломоносов) template=String (android.app.Notification$BigTextStyle) picture=NO largeIcon=yes actions=1 ['Добави в календара']
NOTIF id=2147483647 channel=news title=- text=- template=- picture=NO largeIcon=no actions=0 []
NOTIF id=500 channel=news title=String (Нова публикация) text=String (Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец.) template=String (android.app.Notification$BigPictureStyle) picture=yes Bitmap (746x348) largeIcon=yes actions=1 ['Сподели']
NOTIF total for org.chyavorec.app: 5
## N4. Shade: the event reminder expanded; its buttons
SHADE 40-event expanded: buttons []; big picture view no; text="On" content-desc="Internet,AndroidWifi" text="Internet" text="On" content-desc="Bluetooth." text="Bluetooth" text="Flashlight" text="Off" content-desc="Do Not Disturb." text="Do Not Disturb" text="Chitalishte Yavorets" content-desc="Collapse" text="Напомняне за събитие" text="•" text="now" text="Панайот Пипков · 
ACTION 'Маршрут': no such button in the shade
SHADE 42-event-again expanded: buttons [text="Добави в календара" ]; big picture view no; text="On" content-desc="Internet,AndroidWifi" text="Internet" text="On" content-desc="Bluetooth." text="Bluetooth" text="Flashlight" text="Off" content-desc="Do Not Disturb." text="Do Not Disturb" text="Chitalishte Yavorets" content-desc="Collapse" text="now" content-desc="Collapse" text="Напомняне за събитие" text="Па
ACTION 'Добави в календара' → fg com.google.android.calendar/com.google.android.calendar.launch.oobe.WhatsNewFullScreen; text="Google Calendar" content-desc="Welcome to Google Calendar" text="Make the most of every day." content-desc="next page" 
## COMPLETE
## logcat findings
app pids seen: ['5298', '7469']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=50 total=50
== Davey: 2
   davey events=2 max=1084ms median=942ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## O1. Online warm-up (fills the caches)
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2798 ms
online-warm home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Стахий, Амплий и др. Св. мчк Епимах" content-desc="Последни новини" text="СЪБИТИЕ"
online-warm catalogue: results (2446, 3064 ms)
online-warm catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
online-warm book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
online-warm My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
online-warm Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
online-warm news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
online-warm article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
online-warm calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
online-warm More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
online-warm gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
online-warm album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
online-warm contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
online-warm documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O2. Offline cold start with caches: all tabs
offline: airplane=1
offline-cached home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Стахий, Амплий и др. Св. мчк Епимах" content-desc="Последни новини" text="СЪБИТИЕ"
offline-cached catalogue: results (2446, 2764 ms)
offline-cached catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
offline-cached book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
offline-cached My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
offline-cached Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" text="Няма интернет връзка" 
offline-cached news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
offline-cached article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
offline-cached calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
offline-cached More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
offline-cached gallery: text="Няма интернет връзка. Показваме последно наличните данни." text="Последна синхронизация: Преди 2 минути" text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“"
offline-cached album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
offline-cached contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
offline-cached documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O3. Reconnect on Contacts (no touch), then the other screens
contacts +10s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец,
contacts +25s after reconnect (no touch): text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец,
recovered home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="31 октомври 2026 · Св. апли от 70-те: Стахий, Амплий и др. Св. мчк Епимах" content-desc="Последни новини" text="СЪБИТИЕ"
recovered catalogue: results (2446, 2794 ms)
recovered catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
recovered book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
recovered My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="1 книга в спи
recovered Want to read: text="Наличност към 28.10.2026" text="Плъзни книга настрани, за да я премахнеш." text="12 -те вятъра" text="Гарфилд, Джудит и др." text="налична" content-desc="Още действия" content-desc="Назад" text="Искам да прочета" content-desc="Изчисти списъка" 
recovered news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
recovered article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
recovered calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
recovered More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
recovered contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
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
offline-empty contacts: text="Контактите от сайта не се заредиха — показваме основните данни, вградени в приложението. Може да не са актуални." text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читал
offline-empty documents: text="Документи" text="Исторически публикации" text="Няма връзка" text="Провери интернет връзката си и опитай отново." text="Опитай отново" content-desc="Назад" text="Документи и архив" text="Няма интернет връзка" 
## O5. Reconnect with the empty cache: Gallery (no touch), then all tabs
GALLERY (empty cache) reconnect: albums shown after 9953 ms (-1 = not within 60 s); text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" 
recovered-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" content-desc="Последни новини" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." 
recovered-empty catalogue: results (2446, 2989 ms)
recovered-empty catalogue: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 28.10.2026" text="Джиан, Филип" text="налична" text="  ·  " text="2016 · книга · за възрастни" text="12 -те вятъра" text="Гарф
recovered-empty book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" 
recovered-empty My: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
recovered-empty Want to read: text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" 
recovered-empty news: text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално
recovered-empty article: content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="Днес в НЧ „Васил Левски – 1922“ – с. Яворец се проведе специално обучение по български нар
recovered-empty calendar: text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" text="Читалище" text="Народен обичай" text="1" text="НОЕ" text="Ден на народните будители" text="Тържество с ученически рецитали, четения и музикална програма в памет 
recovered-empty More: text="ЧИТАЛИЩЕТО" text="Съобщения" text="Съобщения и покани от читалището" text="Събития" text="Календар на читалището" text="Дейности" text="Групи, клубове, проекти" text="За читалището" text="История и мисия" text="Галерия" text="Снимки от сайта" text="Документи и архив" text="Устав, отчети, юбиле
recovered-empty gallery: text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" text="5 снимки" text="Село Яворец" text="7 снимки" text="Природата край Яворец" content-desc="Назад" text="Гал
recovered-empty album: content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-desc="Сценичен ансамбъл Яворски хлапета" content-desc="Фолклорна група Явор" content-desc="Клуб за народни танци" content-desc="Стопанският 
recovered-empty contacts: text="НЧ „Васил Левски – 1922“" content-desc="Изпрати имейл на читалището" text="Имейл" content-desc="Покажи читалището на картата" text="Карта" content-desc="Отвори сайта на читалището" text="Сайт" text="Адрес" text="пл. 9-ти Септември 3, с. Яворец, общ. Габрово, обл. Габрово · ПК 5334" text="Лица 
recovered-empty documents: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището подават, за да обявят дали имат частни интереси, 
## O6. My offline as guest again (after the online start)
offline: airplane=1
MY offline guest #2: add-card MISSING; note absent; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Добави номера от своята карта" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, които 
## COMPLETE
## logcat findings
app pids seen: ['4735', '9052', '12270', '16171']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=57 total=179
== Davey: 17
   davey events=17 max=1141ms median=823ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## rot

```
part rot; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3175 ms
## R1. Rotation (6 toggles each) on Home, Catalogue '12', a book, Want to read, Calendar
ROT home before: tab bar yes; search field yes
ROT home landscape: tab bar yes; search field yes
ROTATION home: pid 6845 -> 6845 (same); after: tab bar yes; search field yes
ROT catalog before: query '12' kept; results 108
ROT catalog landscape: query '12' kept; results 108
ROTATION catalog: pid 6845 -> 6845 (same); after: query '12' kept; results 108
ROT book before: book title kept; back button yes
ROT book landscape: book title kept; back button yes
ROTATION book: pid 6845 -> 6845 (same); after: book title kept; back button yes
ROT want before: list title yes; book listed
ROT want landscape: list title yes; book listed
ROTATION want: pid 6845 -> 6845 (same); after: list title yes; book listed
ROT calendar before: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
ROT calendar landscape: News[checked=false selected=false bounds=[508,243][820,369]] Calendar[checked=true selected=false bounds=[820,243][1135,369]] subscribe-action=visible
ROTATION calendar: pid 6845 -> 6845 (same); after: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
GFX rotation     frames=  149  janky=148 (99.33%)  p50=300ms p90=700ms p95=850ms p99=1400ms  slowUI=103 slowDraw=148 highInput=1
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
app pids seen: ['4595', '6845', '9883', '10164', '10310', '10866', '11080', '11331', '11515']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 20
   skipped-frames events=20 max=87 total=904
== Davey: 29
   davey events=29 max=1927ms median=867ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## scan33

```
part scan33; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2735 ms
## S0. Package: permissions (no CAMERA); Google Play services on the image
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.WAKE_LOCK 
CAMERA in dumpsys package: 0 line(s)
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='android.permission.FOREGROUND_SERVICE' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
GMS versionName=23.18.18(190800-535401451) ; Play Store 1 ; Google accounts 0
ML Kit meta-data in the manifest: Raw: "com.google.mlkit.vision.DEPENDENCIES"
## S1. Scan from Home (API 33, camera: Number of camera devices: 0)
SCAN 10-home +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
## S2. Back / cancel
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN after Back/cancel: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S3. Manual entry 399560
SCAN manual panel: open yes; explanation SHOWN
SCAN manual 399560: BOOK SHEET with Отвори книгата; content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Налична" text="инв. № 399560" text="Затвори" text="Отвори книгата" 
SCAN → book: opened; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
## S4. Scan from the launcher shortcut (scan), ~60 s later (the module may have downloaded)
SCAN 40-shortcut +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN (shortcut) after Back: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S5. The button on the screen again
SCAN 50-again +2s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +5s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +10s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +20s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
logcat scanner/module lines: 2243; first: 10-31 21:30:55.263  1108  1897 I ChimeraSrvcProxy: NullBinder for android.net.action.RECOMMEND_NETWORKS triggering remote TransactionTooLargeException due to Service without Chimera impl~10-31 21:30:55.267  1600  3204 I CarrierServices: [52] fbo.b: Sending ProvisioningEvent to Bugle [Send Provisioning Session ID (18)], redacted-pii:[chars:97]~10-31 21:30:55.267  1600  3204 I CarrierServices: [52] ghj.b: EngineBroadcaster: Broadcasting provisioning intent com.google.android.ims.provisioning.engine.provisioningEventAction to Bugle with extra~10-31 21:30:55.306  1976  2658 I DynamiteModule: Considering local module com.google.android.gms.cronet_dynamite:0 and remote module com.google.android.gms.cronet_dynamite:2411~10-31 21:30:55.306  1976  2658 I DynamiteModule: Selected remote version of com.google.android.gms.cronet_dynamite, version >= 2411~10-31 21:30:55.306  1976  2658 V DynamiteModule: Dynamite loader version >= 2, using loadModule2NoCrashUtils~10-31 21:30:55.316  1976  2658 W ziparchive: Unable to open '/data/user_de/0/com.google.android.gms/app_chimera/m/00000001/CronetDynamite.dm': No such file or directory~10-31 21:30:55.316  1976  2658 W ziparchive: Unable to open '/data/user_de/0/com.google.android.gms/app_chimera/m/00000001/CronetDynamite.dm': No such file or directory~10-31 21:30:55.339  1976  2658 D nativeloader: Configuring classloader-namespace for other apk . target_sdk_version=31, uses_libraries=ALL, library_path=/data/user_de/0/com.google.android.gms/app_chim~10-31 21:30:55.431  1600  3204 I CarrierServices: [52] fcw.d: (PEv2-SM-119693049): Start a new provisioning session [b88d8521-c4c9-4983-82c1-d779ac006084]~10-31 21:30:55.461  1600  3291 I CarrierServices: [54] fjp.f: RCS Configuration storage in Bugle is disabled by p/h flag, using ProvisioningEngineStateCacheUtil.getProvisioningConfigurationForGivenSim~10-31 21:30:55.534  1600  3204 I CarrierServices: [52] fhf.b: Config state is not ACCEPTED, provisioning still in progress~
activities started since the first scan:  1 cmp=org.chyavorec.app/.MainActivity 
## SC1. Shortcuts before a book is opened
SHORTCUTS start:  |id=last-book flags=0x85 [DynIc-rStr] shortLabel=12 -те вятъра  |id=catalog flags=0x1a4 [ImManIc-rStr] shortLabel=Catalogue  |id=scan flags=0x1a4 [ImManIc-rStr] shortLabel=Scan  |id=card flags=0x1a4 [ImManIc-rStr] shortLabel=Library card 
## SC2. A book from the public catalogue → 'last book' shortcut; launcher long-press
book: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
SHORTCUTS after the book:  |id=last-book flags=0x85 [DynIc-rStr] shortLabel=12 -те вятъра  |id=catalog flags=0x1a4 [ImManIc-rStr] shortLabel=Catalogue  |id=scan flags=0x1a4 [ImManIc-rStr] shortLabel=Scan  |id=card flags=0x1a4 [ImManIc-rStr] shortLabel=Library card 
LAUNCHER long-press 10-after-book: Scan|Scan a book barcode|My library card|My library card|Catalogue|Search the catalogue|12 -те вятъра|12 -те вятъра — Гарфилд, Джудит и др.|
LAUNCHER 10-after-book: Events absent
## SC3. Settings → 'Изчисти временните данни' → the last-book shortcut is removed
clear dialog: text="Изчисти временните данни" text="Временните данни (новини, събития, каталог, корици и снимки, запазени за четене без интернет, и свалените документи) ще бъдат изтрити и свалени отново при нужда. 
after clearing: text="Известия" text="Избери известията" text="Заемания, събития, съобщения и новини — кои да получаваш" text="Обновления" 
SHORTCUTS after clearing:  |id=catalog flags=0x1a4 [ImManIc-rStr] shortLabel=Catalogue  |id=scan flags=0x1a4 [ImManIc-rStr] shortLabel=Scan  |id=card flags=0x1a4 [ImManIc-rStr] shortLabel=Library card 
LAUNCHER long-press 30-after-clear: Scan|Scan a book barcode|My library card|My library card|Catalogue|Search the catalogue|
LAUNCHER 30-after-clear: Events absent
## CT. Contacts: short action buttons in one row; stacked at a large font
CONTACTS 40-standard: ROWS: 0 matches in 0 row(s) (tops []); ROWS: 0 matches in 0 row(s) (tops []) 
TEXT SIZE now: text="Много голям" 
CONTACTS 41-font130-xlarge: ROWS: 0 matches in 0 row(s) (tops []); ROWS: 0 matches in 0 row(s) (tops []) 
TEXT SIZE now: text="Стандартен" 
## COMPLETE
## logcat findings
app pids seen: ['4724', '6124', '8631', '10595']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=75 total=349
== Davey: 13
   davey events=13 max=1575ms median=870ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## scan33cam

```
part scan33cam; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3573 ms
## S0. Package: permissions (no CAMERA); Google Play services on the image
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.WAKE_LOCK 
CAMERA in dumpsys package: 0 line(s)
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='android.permission.FOREGROUND_SERVICE' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
GMS versionName=23.18.18(190800-535401451) ; Play Store 1 ; Google accounts 0
ML Kit meta-data in the manifest: Raw: "com.google.mlkit.vision.DEPENDENCIES"
## S1. Scan from Home (API 33, camera: Number of camera devices: 1)
SCAN 10-home +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
## S2. Back / cancel
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN after Back/cancel: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S3. Manual entry 399560
SCAN manual panel: open yes; explanation SHOWN
SCAN manual 399560: BOOK SHEET with Отвори книгата; content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Налична" text="инв. № 399560" text="Затвори" text="Отвори книгата" 
SCAN → book: opened; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
## S4. Scan from the launcher shortcut (scan), ~60 s later (the module may have downloaded)
SCAN 40-shortcut +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN (shortcut) after Back: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S5. The button on the screen again
SCAN 50-again +2s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +5s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +10s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +20s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
logcat scanner/module lines: 2043; first: 10-31 21:30:11.398  1278  3372 W ConfigFileUtils: Failed to read asset ChimeraModuleSetList.pb: java.io.FileNotFoundException: ChimeraModuleSetList.pb~10-31 21:30:11.449  1051  2202 I ChimeraSrvcProxy: NullBinder for android.net.action.RECOMMEND_NETWORKS triggering remote TransactionTooLargeException due to Service without Chimera impl~10-31 21:30:12.341  1278  3373 I Checkin : [EventLogChimeraService] Opted in for usage reporting: false~10-31 21:30:12.440  1051  3392 W ChimeraUtils: Module com.google.android.gms.nearby_en missing resource null(0)~10-31 21:30:12.827  1278  3415 W ProviderHelper: Unknown dynamite feature providerinstaller.dynamite~10-31 21:30:12.828  1278  3415 I DynamiteModule: Considering local module com.google.android.gms.providerinstaller.dynamite:1 and remote module com.google.android.gms.providerinstaller.dynamite:0~10-31 21:30:12.831  1278  3415 I DynamiteModule: Selected local version of com.google.android.gms.providerinstaller.dynamite~10-31 21:30:12.895  1278  3415 W ProviderHelper: Unknown dynamite feature providerinstaller.dynamite~10-31 21:30:12.895  1278  3415 I DynamiteModule: Considering local module com.google.android.gms.providerinstaller.dynamite:1 and remote module com.google.android.gms.providerinstaller.dynamite:0~10-31 21:30:12.895  1278  3415 I DynamiteModule: Selected local version of com.google.android.gms.providerinstaller.dynamite~10-31 21:30:13.156  1975  2726 W DynamiteModule: Local module descriptor class for com.google.android.gms.providerinstaller.dynamite not found.~10-31 21:30:13.171  1051  3392 W ChimeraUtils: Module com.google.android.gms.nearby_en missing resource null(0)~
activities started since the first scan:  1 cmp=org.chyavorec.app/.MainActivity 
## COMPLETE
## logcat findings
app pids seen: ['4718', '6279', '8460']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=86 total=301
== Davey: 19
   davey events=19 max=1615ms median=949ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## scan33keep

```
part scan33keep; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323595 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
DIAGNOSTIC APK: DIAGNOSTIC 1.2.29 7bd2513 + keep ML Kit registrar constructors; versionCode 1488542
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3282 ms
## S0. Package: permissions (no CAMERA); Google Play services on the image
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.WAKE_LOCK 
CAMERA in dumpsys package: 0 line(s)
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='android.permission.FOREGROUND_SERVICE' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
GMS versionName=23.18.18(190800-535401451) ; Play Store 1 ; Google accounts 0
ML Kit meta-data in the manifest: Raw: "com.google.mlkit.vision.DEPENDENCIES"
## S1. Scan from Home (API 33, camera: Number of camera devices: 0)
SCAN 10-home +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
## S2. Back / cancel
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN after Back/cancel: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S3. Manual entry 399560
SCAN manual panel: open yes; explanation SHOWN
SCAN manual 399560: BOOK SHEET with Отвори книгата; content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Налична" text="инв. № 399560" text="Затвори" text="Отвори книгата" 
SCAN → book: opened; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
## S4. Scan from the launcher shortcut (scan), ~60 s later (the module may have downloaded)
SCAN 40-shortcut +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN (shortcut) after Back: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S5. The button on the screen again
SCAN 50-again +2s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +5s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +10s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +20s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
logcat scanner/module lines: 2261; first: 10-31 22:24:44.401  2831  2949 W ziparchive: Unable to open '/data/user_de/0/com.google.android.gms/app_chimera/m/00000005/GoogleCertificates.dm': No such file or directory~10-31 22:24:44.401  2831  2949 W ziparchive: Unable to open '/data/user_de/0/com.google.android.gms/app_chimera/m/00000005/GoogleCertificates.dm': No such file or directory~10-31 22:24:44.826  2091  2446 I RcsProvisioning: Failed to read config: no config found /data/user/0/com.google.android.apps.messaging/files/rcsconfig: open failed: ENOENT (No such file or directory)~10-31 22:24:45.551  2874  2874 W ProviderHelper: Unknown dynamite feature providerinstaller.dynamite~10-31 22:24:45.562  2874  2874 I DynamiteModule: Considering local module com.google.android.gms.providerinstaller.dynamite:1 and remote module com.google.android.gms.providerinstaller.dynamite:0~10-31 22:24:45.562  2874  2874 I DynamiteModule: Selected local version of com.google.android.gms.providerinstaller.dynamite~10-31 22:24:46.118  1048  1338 W bwft    : Succeeded but not registered: com.google.apps.dynamite.device#com.google.android.gm [CONTEXT service_id=51 ]~10-31 22:24:46.240  1626  3173 I CarrierServices: [58] fjp.f: RCS Configuration storage in Bugle is disabled by p/h flag, using ProvisioningEngineStateCacheUtil.getProvisioningConfigurationForGivenSim~10-31 22:24:46.294  2091  2445 W DynamiteModule: Local module descriptor class for com.google.android.gms.providerinstaller.dynamite not found.~10-31 22:24:46.399  1626  3173 I CarrierServices: [58] fjp.f: RCS Configuration storage in Bugle is disabled by p/h flag, using ProvisioningEngineStateCacheUtil.getProvisioningConfigurationForGivenSim~10-31 22:24:46.407  2161  3114 W DynamiteModule: Local module descriptor class for com.google.android.gms.providerinstaller.dynamite not found.~10-31 22:24:46.610  1048  1062 W ProviderHelper: Unknown dynamite feature providerinstaller.dynamite~
ML Kit registrar errors in the app process: 0 line(s): 
Play services module/scanner calls after the first scan: 64 line(s): 10-31 22:26:33.013  1287  6435 I ChimeraConfigurator: Detected pending changes for urgent request: [vision.ocr, mlkit.barcode.ui]. Pending module sets: container:2318189190800+mlkit.barcode.ui+mlkit.o~10-31 22:26:33.043  1287  6435 W ZappDownloader: No successful module downloads for requested features [vision.ocr, mlkit.barcode.ui].~10-31 22:26:33.062  1287  6435 I GmsDebugLogger: [83] 1 [mlkit.barcode.ui, vision.ocr]~10-31 22:26:36.419  1287  1287 D BoundBrokerSvc: onRebind: Intent { act=com.google.android.gms.chimera.container.moduleinstall.ModuleInstallService.START dat=chimera-action:/... cmp=com.google.android~10-31 22:26:37.030  1287  7005 I GmsDebugLogger: [53] +mlkit.barcode.ui:1~10-31 22:26:37.044  1287  3659 I ChimeraConfigurator: Starting update, reason: 4 urgentFeatures: mlkit.barcode.ui:1~
activities started since the first scan:  1 cmp=org.chyavorec.app/.MainActivity 
## COMPLETE
## logcat findings
app pids seen: ['4614', '6263', '8559']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=102 total=402
== Davey: 18
   davey events=18 max=2402ms median=916ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## scan34

```
part scan34; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3266 ms
## S0. Package: permissions (no CAMERA); Google Play services on the image
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.WAKE_LOCK 
CAMERA in dumpsys package: 0 line(s)
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='android.permission.FOREGROUND_SERVICE' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
GMS versionName=23.18.18(190800-535401451) ; Play Store 1 ; Google accounts 0
ML Kit meta-data in the manifest: Raw: "com.google.mlkit.vision.DEPENDENCIES"
## S1. Scan from Home (API 34, camera: Number of camera devices: 0)
SCAN 10-home +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
## S2. Back / cancel
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN after Back/cancel: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S3. Manual entry 399560
SCAN manual panel: open yes; explanation SHOWN
SCAN manual 399560: BOOK SHEET with Отвори книгата; content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Налична" text="инв. № 399560" text="Затвори" text="Отвори книгата" 
SCAN → book: opened; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
## S4. Scan from the launcher shortcut (scan), ~60 s later (the module may have downloaded)
SCAN 40-shortcut +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN (shortcut) after Back: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S5. The button on the screen again
SCAN 50-again +2s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +5s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +10s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +20s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
logcat scanner/module lines: 1901; first: 10-31 21:29:34.999  1468  1703 I Icing   : IndexChimeraService.getServiceInterface callingPackage=com.google.android.googlequicksearchbox componentName=null serviceId=36~10-31 21:29:35.025  1908  3394 W DynamiteModule: Local module descriptor class for com.google.android.gms.providerinstaller.dynamite not found.~10-31 21:29:35.054  1890  2443 I DynamiteModule: Considering local module com.google.android.gms.cronet_dynamite:0 and remote module com.google.android.gms.cronet_dynamite:2411~10-31 21:29:35.054  1890  2443 I DynamiteModule: Selected remote version of com.google.android.gms.cronet_dynamite, version >= 2411~10-31 21:29:35.054  1890  2443 V DynamiteModule: Dynamite loader version >= 2, using loadModule2NoCrashUtils~10-31 21:29:35.119  1890  1890 I DynamiteModule: Considering local module com.google.android.gms.cast.framework.dynamite:0 and remote module com.google.android.gms.cast.framework.dynamite:226~10-31 21:29:35.119  1890  1890 I DynamiteModule: Selected remote version of com.google.android.gms.cast.framework.dynamite, version >= 226~10-31 21:29:35.119  1890  1890 V DynamiteModule: Dynamite loader version >= 2, using loadModule2NoCrashUtils~10-31 21:29:35.212  1423  2866 W ProviderHelper: Unknown dynamite feature providerinstaller.dynamite~10-31 21:29:35.215  1423  3296 W ChimeraUtils: Module com.google.android.gms.backup_base missing resource null(0)~10-31 21:29:35.216  1423  1423 D BoundBrokerSvc: onBind: Intent { act=com.google.android.gms.nearby.sharing.START_SERVICE dat=chimera-action:/... cmp=com.google.android.gms/.chimera.PersistentApiServi~10-31 21:29:35.216  1423  1423 D BoundBrokerSvc: Loading bound service for intent: Intent { act=com.google.android.gms.nearby.sharing.START_SERVICE dat=chimera-action:/... cmp=com.google.android.gms/.~
activities started since the first scan:  1 cmp=org.chyavorec.app/.MainActivity 
## LG1. Sign-in on a fresh install (password-manager flag unset): no credential query, no crash (API 34)
GMS versionName=23.18.18(190800-535401451) ; Play Store 1 ; Google accounts 0; credential providers:     com.google.android.gms/.auth.api.credentials.credman.service.PasswordAndPasskeyService 
LOGIN +2s: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от And
LOGIN +6s: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от And
LOGIN credential traffic after open: []
LOGIN activities started after open: []
## LG2. Card number field
LOGIN card field after typing 'ab-123': FIELD 0: text='ab-123' password=false focused=true hint=''
## LG3. PIN (OutlinedSecureTextField): the last digit shows briefly; the eye toggle
PIN after '1' +2418 ms: FIELD 1: text='1' password=true focused=true hint=''
PIN after '1' +7150 ms: FIELD 1: text='1' password=true focused=true hint=''
PIN after '2' +2182 ms: FIELD 1: text='12' password=true focused=true hint=''
PIN after '2' +6920 ms: FIELD 1: text='12' password=true focused=true hint=''
PIN after '3' +2202 ms: FIELD 1: text='123' password=true focused=true hint=''
PIN after '3' +6806 ms: FIELD 1: text='123' password=true focused=true hint=''
PIN after '4' +2282 ms: FIELD 1: text='1234' password=true focused=true hint=''
PIN after '4' +6998 ms: FIELD 1: text='1234' password=true focused=true hint=''
PIN eye ON: FIELD 1: text='1234' password=true focused=true hint=''; toggle now: content-desc="Скрий ПИН-а"
PIN eye OFF: FIELD 1: text='1234' password=true focused=true hint=''; toggle now: content-desc="Покажи ПИН-а"
## LG4. 'Запомни ме' row: one labelled checkbox
before: CHECKABLE class=android.view.View label='Запомни ме на това устройство' checked=true clickable=true bounds=[63,1161][1017,1287] nested-checkables=0
'Запомни ме' text nodes: 1
after tap on the text: CHECKABLE class=android.view.View label='Запомни ме на това устройство' checked=false clickable=true bounds=[63,1161][1017,1287] nested-checkables=0
A11Y login-15: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
LOGIN after Back: text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" t; pid '7079'
LOGIN second open: fg org.chyavorec.app/org.chyavorec.app.MainActivity; credential traffic: []
## COMPLETE
## logcat findings
app pids seen: ['4739', '5960', '7079']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=60 total=209
== Davey: 9
   davey events=9 max=1372ms median=850ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## scan34ps

```
part scan34ps; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323595 bytes
host: 4 cpu, 15700 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3377 ms
## S0. Package: permissions (no CAMERA); Google Play services on the image
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.WAKE_LOCK 
CAMERA in dumpsys package: 0 line(s)
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='android.permission.FOREGROUND_SERVICE' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
GMS versionName=23.18.18(190800-535401451) ; Play Store 1 ; Google accounts 0
ML Kit meta-data in the manifest: Raw: "com.google.mlkit.vision.DEPENDENCIES"
## S1. Scan from Home (API 34, camera: Number of camera devices: 0)
SCAN 10-home +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 10-home +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
## S2. Back / cancel
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN after Back/cancel: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S3. Manual entry 399560
SCAN manual panel: open yes; explanation SHOWN
SCAN manual 399560: NO book sheet; text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета н
## S4. Scan from the launcher shortcut (scan), ~60 s later (the module may have downloaded)
SCAN 40-shortcut +2s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +5s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +10s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN 40-shortcut +20s: other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #1 → other; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel OPEN; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' no; 'Въведи номера на ръка' yes; texts: text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="ISBN-ът (10 или 13 цифри под баркода) или инвентарният номер от етикета на библиотеката." text="ISBN или инвентар
SCAN Back #2 → APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN (shortcut) after Back: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
## S5. The button on the screen again
SCAN 50-again +2s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +5s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +10s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
SCAN 50-again +20s: APP-UNAVAILABLE; fg org.chyavorec.app/org.chyavorec.app.MainActivity; manual panel closed; explanation SHOWN; download/install text yes; 'Сканирай баркод' no; 'Опитай отново' yes; 'Въведи номера на ръка' yes; texts: text="Скенерът не е достъпен" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак след малко." text="Опитай отново" text="Въведи номера на ръка" content-desc="Назад" text="Сканиране" 
logcat scanner/module lines: 3900; first: 10-31 22:24:29.058  1462  3812 I Icing   : IndexChimeraService.getServiceInterface callingPackage=com.google.android.gms componentName=AppsCorpus serviceId=36~10-31 22:24:29.074  1462  2983 I Auth    : [SupervisedAccountIntentOperation] onHandleIntent(): com.google.android.chimera.IntentOperation.NEW_MODULE~10-31 22:24:29.074  1462  2420 W Auth    : [PubsubPeriodicSyncherRuntimeInit] Initializing periodic tasks, IntentAction=com.google.android.chimera.IntentOperation.NEW_MODULE, InitRuntimeState=12~10-31 22:24:29.081  1462  2983 I Auth    : [SupervisedAccountIntentOperation] onHandleIntent(): com.google.android.gms.chimera.container.CONTAINER_UPDATED~10-31 22:24:29.105  1355  1355 W ChimeraUtils: Module com.google.android.gms.nearby missing resource null(0)~10-31 22:24:29.168   497  1695 I RemoteProvisionSysSvc: getRegistration(android.hardware.security.keymint.IRemotelyProvisionedComponent/default)~10-31 22:24:29.187   497   525 I ActivityManager: Start proc 3842:com.google.android.rkpdapp/u0a172 for service {com.google.android.rkpdapp/com.android.rkpdapp.service.RemoteProvisioningService}~10-31 22:24:29.359  1462  2979 I ChimeraConfigurator: Starting update, reason: 4 urgentFeatures: ~10-31 22:24:29.360  1462  2983 I SyncIntentOperation: Handling the intent: Intent { act=com.google.android.gms.chimera.container.CONTAINER_UPDATED pkg=com.google.android.gms cmp=com.google.android.gms~10-31 22:24:29.445   987  1797 D com.android.ons.ONSProfileActivator: ERR_AUTO_PROVISIONING_DISABLED~10-31 22:24:29.617   497  3869 D SystemServerTimingAsync: ssm.onCompletedEventUser-0_{|Unlocked|}_com.android.server.security.rkp.RemoteProvisioningService~10-31 22:24:29.663  1462  1462 D BoundBrokerSvc: onBind: Intent { act=com.google.android.mdh.service.START dat=chimera-action:/... cmp=com.google.android.gms/.chimera.GmsBoundBrokerService }~
ML Kit registrar errors in the app process: 6 line(s): NoSuchMethodException: com.google.mlkit.common.internal.CommonComponentRegistrar.<init> NoSuchMethodException: com.google.mlkit.vision.common.internal.VisionCommonRegistrar.<init> 
Play services module/scanner calls after the first scan: 52 line(s): 10-31 22:26:10.008  6853  7002 I ChimeraCfgMgr: Updating module config: container:2318189190800 -> container:2318189190800+mlkit.barcode.ui+vision.barcode,tflite_dynamite:2640104055100800+tflite_dynam~10-31 22:26:10.259  6853  7002 I GmsDebugLogger: [83] 0 [mlkit.barcode.ui]~10-31 22:26:10.427  6853  6966 W SyntheticOperationsLogg: SyntheticOperationsLogger unable to log "com.google.android.gms.mlkit.barcode.ui"'s use of "change_component_enabled_state" because installOpe~10-31 22:26:10.429  6853  6966 W SyntheticOperationsLogg: SyntheticOperationsLogger unable to log "com.google.android.gms.mlkit.barcode.ui"'s use of "change_component_enabled_state" because installOpe~10-31 22:26:10.481  6853  6966 W SyntheticOperationsLogg: SyntheticOperationsLogger unable to log "com.google.android.gms.mlkit.barcode.ui"'s use of "change_component_enabled_state" because installOpe~10-31 22:26:10.916  6853  6966 I ChimeraConfigurator: Starting update, reason: 4 urgentFeatures: mlkit.barcode.ui:0~
activities started since the first scan:  1 cmp=org.chyavorec.app/.MainActivity 
## COMPLETE
## logcat findings
app pids seen: ['5254', '6456', '9363']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=39 total=108
== Davey: 8
   davey events=8 max=878ms median=799ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## tile33

```
part tile33; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2434 ms
## T0. The tile service
QS_TILE services:     org.chyavorec.app/.tile.CardTileService 
## T1. No card: added with 'cmd statusbar add-tile' → inactive
sysui_qs_tiles before: internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,dbg:mem,ott
add-tile org.chyavorec.app/.tile.CardTileService: ''
sysui_qs_tiles after: custom(org.chyavorec.app/.tile.CardTileService),internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,dbg:mem,ott
QS no-card: tile @ 285 630 (page 1): MATCH class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[163,579][418,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none  texts: content-desc="Читателска карта" text="Читателска карта" text="Добави картата" 
QS no-card: ours in sysui_qs_tiles: 1
## T2. Tap without a card (unlocked)
after tap (no card): fg org.chyavorec.app/org.chyavorec.app.MainActivity; app IN FRONT; text="Дигитална карта" text="Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите." text="Номер на картата" text="Главни латински букви, цифри и „-“" text="Име
## T3. Card-screen prompt 'Добави в бързите настройки' (API 33; the tile removed first)
remove-tile: ''; ours in sysui_qs_tiles: 0
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARD screen: card shown; prompt SHOWN; dismiss X yes
system dialog: fg TileRequestDialog; text="Chitalishte Yavorets wants to add the following tile to Quick Settings" content-desc="Читателска карта" text="Читателска карта" text="Do not add tile" text="Add tile" 
after 'Add tile': prompt gone; fg org.chyavorec.app/org.chyavorec.app.MainActivity
## T4. With a card: active
after the prompt: ours in sysui_qs_tiles 1
QS with-card: tile @ 793 630 (page 4): MATCH class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[550,525][1037,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[550,525][1037,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[671,579][926,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[550,525][1037,735]" | ACTIONABLE-CHILD none  texts: content-desc="Читателска карта" text="Читателска карта" text="Покажи на гишето" 
QS with-card: ours in sysui_qs_tiles: 1
## T5. Tap (unlocked) → the card screen
after tap (card): fg org.chyavorec.app/org.chyavorec.app.MainActivity; card screen SHOWN; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован
## T6. Locked with a PIN: tap the tile → the unlock prompt first
set-pin: Pin set to '1234'
lock screen: isKeyguardShowing=true ; text="Configure physical keyboard" content-desc="Alerted" text="Tap to select language and layout" content-desc="Expand" text="11:04" content-desc="11:04" 
QS locked: tile @ NOT FOUND (page 4):  texts: 
QS locked: ours in sysui_qs_tiles: 1
tile not on screen — click-tile: ''
after tap while LOCKED: fg NotificationShade; PIN entry SHOWN; app in front no; content-desc="Display brightness" text="Display brightness" content-desc="QR code scanner" text="QR code scanner" text="Updating" content-desc="Battery Saver" text="Battery Saver" text="Unavailable" 
after entering the PIN: fg org.chyavorec.app/org.chyavorec.app.MainActivity; card screen SHOWN; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
clear PIN: Lock credential cleared
## COMPLETE
## logcat findings
app pids seen: ['5503']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=33 total=33
== Davey: 1
   davey events=1 max=824ms median=824ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## tile34

```
part tile34; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2798 ms
## T0. The tile service
QS_TILE services:     org.chyavorec.app/.tile.CardTileService 
## T1. No card: added with 'cmd statusbar add-tile' → inactive
sysui_qs_tiles before: internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,custom(com.google.android.permissioncontroller/com.android.permissioncontroller.permission.service.v33.SafetyCenterQsTileService),dbg:mem,ott
add-tile org.chyavorec.app/.tile.CardTileService: ''
sysui_qs_tiles after: custom(org.chyavorec.app/.tile.CardTileService),internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,custom(com.google.android.permissioncontroller/com.android.permissioncontroller.permission.service.v33.SafetyCenterQsTileService),dbg:mem,ott
QS no-card: tile @ 285 630 (page 1): MATCH class="android.widget.LinearLayout" text="Читателска карта, Добави картата" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Добави картата" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[163,579][418,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Добави картата" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none  texts: text="Читателска карта, Добави картата" content-desc="Читателска карта" text="Читателска карта" text="Добави картата" 
QS no-card: ours in sysui_qs_tiles: 1
## T2. Tap without a card (unlocked)
after tap (no card): fg org.chyavorec.app/org.chyavorec.app.MainActivity; app IN FRONT; text="Дигитална карта" text="Въведи номера от своята читателска карта. Приложението ще покаже баркод в същия формат (Code 39), в който библиотеката печата картите." text="Номер на картата" text="Главни латински букви, цифри и „-“" text="Име
## T3. Card-screen prompt 'Добави в бързите настройки' (API 34; the tile removed first)
remove-tile: ''; ours in sysui_qs_tiles: 0
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARD screen: card shown; prompt SHOWN; dismiss X yes
system dialog: fg TileRequestDialog; text="Chitalishte Yavorets wants to add the following tile to Quick Settings" text="Читателска карта, On" content-desc="Читателска карта" text="Читателска карта" text="Do not add tile" text="Add tile" 
after 'Add tile': prompt gone; fg org.chyavorec.app/org.chyavorec.app.MainActivity
## T4. With a card: active
after the prompt: ours in sysui_qs_tiles 1
QS with-card: tile @ NOT FOUND (page 4):  texts: 
QS with-card: ours in sysui_qs_tiles: 1
## T5. Tap (unlocked) → the card screen
tile not on screen — click-tile: ''
after tap (card): fg org.chyavorec.app/org.chyavorec.app.MainActivity; card screen SHOWN; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован
## T6. Locked with a PIN: tap the tile → the unlock prompt first
set-pin: Pin set to '1234'
lock screen: isKeyguardShowing=true ; text="11&#10;03" content-desc="11:03" text="Sat, Oct 31" content-desc="Sat, Oct 31" text="T-Mobile" content-desc="Wifi signal full." 
QS locked: tile @ NOT FOUND (page 4):  texts: 
QS locked: ours in sysui_qs_tiles: 1
tile not on screen — click-tile: ''
after tap while LOCKED: fg NotificationShade; PIN entry absent; app in front no; content-desc="Display brightness" text="Display brightness" text="QR code scanner, Updating" content-desc="QR code scanner" text="QR code scanner" text="Updating" text="Battery Saver, Unavailable" content-desc="Battery Saver" 
after entering the PIN: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; card screen NO; text="Sat, Oct 31" content-desc="Sat, Oct 31" text="Gmail" content-desc="Gmail" text="Photos" content-desc="Photos" 
clear PIN: Lock credential cleared
## COMPLETE
## logcat findings
app pids seen: ['5064']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=32 total=62
== Davey: 3
   davey events=3 max=783ms median=763ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widget

```
part widget; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3704 ms
## W1. Add the widget from the launcher's widget picker (default 4×2 = wide)
launcher: com.google.android.apps.nexuslauncher
WIDGET new long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET new picker: our row @ 437 999; size label @ 540 1907; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET new after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET new: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET grid from the 4x2 copy: [57 537 1023 1123] cell 241x293 px
## W2. Wide 4×2, signed out: content, light and dark
WIDGET 10-wide-4x2 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 10-wide-4x2 (w15): WIDGET VIEW: title='Читалище Яворец' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=['widget_scan', 'widget_catalog']
WIDGET TARGETS: 5 clickable, <48dp: 1; background('Читалище Яворец Сканирай') 368x223dp; widget_scan('Сканирай') 75x48dp; widget_catalog('Каталог') 66x48dp; widget_event('Предстоящи събития Ден н') 340x50dp; widget_event2('Михаил В. Ломоносов 8 но') 340x36dp
WIDGET 10-wide-4x2 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET signed-out: loans block hidden; event shown; no-events absent
## W3. Wide widget: clicks
WIDGET click catalog (widget_catalog @ 900 600) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 694 600) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак сл
WIDGET click event (widget_event @ 540 786) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 337 599) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
## W4. Sizes: 4×3, 3×2, 2×2, 2×3 (resize handles), light and dark each
RESIZE 4x3 (4x3): bounds [57 537 1023 1437] options 
WIDGET 20-wide-tall-4x3 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1437]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 20-wide-tall-4x3 (w15): WIDGET VIEW: title='Читалище Яворец' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=['widget_scan', 'widget_catalog']
WIDGET TARGETS: 5 clickable, <48dp: 1; background('Читалище Яворец Сканирай') 368x343dp; widget_scan('Сканирай') 75x48dp; widget_catalog('Каталог') 66x48dp; widget_event('Предстоящи събития Ден н') 340x50dp; widget_event2('Михаил В. Ломоносов 8 но') 340x36dp
WIDGET 20-wide-tall-4x3 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1437]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
RESIZE 3x2 (3x2): bounds [57 537 771 1123] options 
WIDGET 30-medium-3x2 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan ]; bounds [57 537 771 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 30-medium-3x2 (w15): WIDGET VIEW: title='-' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=['widget_scan', 'widget_catalog']
WIDGET TARGETS: 5 clickable, <48dp: 1; background('Читалище Яворец Сканирай') 272x223dp; widget_scan('Сканирай') 75x48dp; widget_catalog('Каталог') 66x48dp; widget_event('Предстоящи събития Ден н') 244x50dp; widget_event2('Михаил В. Ломоносов 8 но') 244x36dp
WIDGET 30-medium-3x2 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan ]; bounds [57 537 771 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
RESIZE 2x2 (2x2): bounds [57 537 519 1123] options 
WIDGET 40-small-2x2 light: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1123]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 40-small-2x2 (w15): WIDGET VIEW: title='Читалище' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=[]
WIDGET TARGETS: 3 clickable, <48dp: 1; background('Читалище Предстоящи съби') 176x223dp; widget_event('Предстоящи събития Ден н') 148x69dp; widget_event2('Михаил В. Ломоносов 8 но') 148x36dp
WIDGET 40-small-2x2 dark: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1123]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET click small-event (widget_event @ 288 773) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click small-app: 't:Chitalishte Yavorets|Читалище Яворец' not on the home screen
RESIZE 2x3 (2x3): bounds [57 537 519 1437] options 
WIDGET 50-small-tall-2x3 light: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1437]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 50-small-tall-2x3 (w15): WIDGET VIEW: title='Читалище' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=[]
WIDGET TARGETS: 3 clickable, <48dp: 1; background('Читалище Предстоящи съби') 176x343dp; widget_event('Предстоящи събития Ден н') 148x69dp; widget_event2('Михаил В. Ломоносов 8 но') 148x36dp
WIDGET 50-small-tall-2x3 dark: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1437]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
RESIZE back-4x2 (4x2): bounds [57 537 1023 1123] options 
WIDGET back to 4x2: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
## W5. After force-stop of the app process
WIDGET after force-stop: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
## W6. With a self-entered card (no sign-in: loans need a reader account)
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
WIDGET 80-card-4x2 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 80-card-4x2 (w15): WIDGET VIEW: title='Читалище Яворец' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=['widget_scan', 'widget_catalog']
WIDGET TARGETS: 5 clickable, <48dp: 1; background('Читалище Яворец Сканирай') 368x223dp; widget_scan('Сканирай') 75x48dp; widget_catalog('Каталог') 66x48dp; widget_event('Предстоящи събития Ден н') 340x50dp; widget_event2('Михаил В. Ломоносов 8 но') 340x36dp
WIDGET 80-card-4x2 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
RESIZE card-4x3 (4x3): bounds [57 537 1023 1437] options 
WIDGET 81-card-4x3 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1437]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 81-card-4x3 (w15): WIDGET VIEW: title='Читалище Яворец' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=['widget_scan', 'widget_catalog']
WIDGET TARGETS: 5 clickable, <48dp: 1; background('Читалище Яворец Сканирай') 368x343dp; widget_scan('Сканирай') 75x48dp; widget_catalog('Каталог') 66x48dp; widget_event('Предстоящи събития Ден н') 340x50dp; widget_event2('Михаил В. Ломоносов 8 но') 340x36dp
WIDGET 81-card-4x3 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1437]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
RESIZE card-2x3 (2x3): bounds [57 537 519 1437] options 
WIDGET 82-card-2x3 light: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1437]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 82-card-2x3 (w15): WIDGET VIEW: title='Читалище' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=[]
WIDGET TARGETS: 3 clickable, <48dp: 1; background('Читалище Предстоящи съби') 176x343dp; widget_event('Предстоящи събития Ден н') 148x69dp; widget_event2('Михаил В. Ломоносов 8 но') 148x36dp
WIDGET 82-card-2x3 dark: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1437]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
RESIZE card-2x2 (2x2): bounds [57 537 519 1123] options 
WIDGET 83-card-2x2 light: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1123]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 83-card-2x2 (w15): WIDGET VIEW: title='Читалище' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=[]
WIDGET TARGETS: 3 clickable, <48dp: 1; background('Читалище Предстоящи съби') 176x223dp; widget_event('Предстоящи събития Ден н') 148x69dp; widget_event2('Михаил В. Ломоносов 8 но') 148x36dp
WIDGET 83-card-2x2 dark: views from org.chyavorec.app: 1 [ids: widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_title ]; bounds [57 537 519 1123]; texts: Читалище|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
RESIZE card-3x2 (3x2): bounds [57 537 771 1123] options 
WIDGET 84-card-3x2 light: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan ]; bounds [57 537 771 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET 84-card-3x2 (w15): WIDGET VIEW: title='-' logo=True events=2 [1 ноември 2026: Ден на народните будители] [8 ноември 2026: Михаил В. Ломоносов] no-events=False loans=False due=[] buttons=['widget_scan', 'widget_catalog']
WIDGET TARGETS: 5 clickable, <48dp: 1; background('Читалище Яворец Сканирай') 272x223dp; widget_scan('Сканирай') 75x48dp; widget_catalog('Каталог') 66x48dp; widget_event('Предстоящи събития Ден н') 244x50dp; widget_event2('Михаил В. Ломоносов 8 но') 244x36dp
WIDGET 84-card-3x2 dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan ]; bounds [57 537 771 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
## COMPLETE
## logcat findings
app pids seen: ['4671', '10348', '10601']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 12
   skipped-frames events=12 max=67 total=561
== Davey: 22
   davey events=22 max=1614ms median=938ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widgetlang

```
part widgetlang; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4230 ms
## WL1. System EN, app BG: add the widget
system locale:  / config: mcc310-mnc260-en-rUS-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33; app: [bg-BG]
WIDGET lang long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET lang picker: our row @ 437 809; size label @ 540 1717; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET lang after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET lang: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET-LANG 01-sysEN-appBG: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
## WL2. System EN: the app switched to English in its Settings (process kept)
APP language chosen in Settings: English; get-app-locales: [en]; pid 4328
WIDGET-LANG 02-sysEN-appEN-right-after: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 03-sysEN-appEN-after-15s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 04-sysEN-appEN-after-reopen: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 05-sysEN-appEN-after-restart: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
## WL3. System BG (framework restart), app EN
SYSTEM locale -> bg-BG: 
SYSTEM locale now: persist.sys.locale=bg-BG; config: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33
WIDGET-LANG 10-sysBG-appEN-after-reboot: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
app screen (should be EN): text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode
WIDGET-LANG 11-sysBG-appEN-after-launch: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
## WL4. System BG, app switched to Български in Settings, then restart
APP language chosen in Settings: Български; get-app-locales: [bg]; pid 9827
WIDGET-LANG 20-sysBG-appBG-right-after: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 21-sysBG-appBG-after-restart: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
## WL5. System BG, app EN set with cmd locale (as from the system's per-app language screen), restart
WIDGET-LANG 30-sysBG-appEN-cmd-right-after: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 31-sysBG-appEN-cmd-after-restart: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 32-sysBG-appEN-dark: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
SYSTEM locale -> en-US: 
SYSTEM locale now: persist.sys.locale=en-US; config: config: mcc310-mnc260-en-rUS-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33
## COMPLETE
## logcat findings
app pids seen: ['4328', '8055', '9827', '12630', '12807', '14207']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 11
   skipped-frames events=11 max=91 total=472
== Davey: 18
   davey events=18 max=2461ms median=1064ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widgetupd

```
part widgetupd; APK: 1.2.29 7bd2513 (NotifierTest: тестовете за снимки и действия — в NotifierTes); versionCode 1488542; x86_64 libs: 2 size 4100618 bytes
base APK: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## U0. Install v1.2.28 (RemoteViews widget)
installed base: 1.2.28 6e2c640 (Профил за бърз старт: генериран на емулатор от f6ddc53); versionCode 1488541; x86_64 libs: 4; update manifest: no-update stub size 4323599;  versionCode=1488541 minSdk=26 targetSdk=37  versionName=1.2.28 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2963 ms
groups: group_app=Приложението; group_my_library=Моята библиотека; group_centre=Читалището
channels: events(Събития)->group_centre; messages(Съобщения от читалището)->group_centre; updates(Обновления)->group_app; news(Новини)->group_centre; loans(Срокове за връщане)->group_my_library; library(Библиотека)->group_my_library; messages_important(Важни съобщения)->group_centre
## U1. v1.2.28: add the widget
WIDGET base long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET base picker: our row @ 437 928; size label @ 540 1836; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET base after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET base: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET v1.2.28: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
## U2. adb install -r v1.2.29 over v1.2.28 with the widget on the home screen (app not opened)
after adb install -r:  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
WIDGET +3s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET +15s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET +45s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
widget ids before/after the update: [id=2 ] / [id=2 ]
## U3. The kept widget after the update: clicks
WIDGET click catalog (widget_catalog @ 900 600) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 694 600) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Въведи номера на ръка" text="Скенерът на баркодове идва с Google Play услугите — тук те липсват или скенерът още се изтегля. Въведи номера на ръка или опитай пак сл
WIDGET click event (widget_event @ 540 786) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 337 599) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
## U4. App opened after the update: channel groups; widget
groups: group_app=Приложението; group_my_library=Моята библиотека; group_centre=Читалището
channels: events(Събития)->group_centre; messages(Съобщения)->group_centre; updates(Обновления)->group_app; news(Новини)->group_centre; loans(Срокове за връщане)->group_my_library; library(Отново на рафта)->group_my_library; messages_important(Важни съобщения)->group_centre
SYSTEM notification settings: fg com.android.settings/com.android.settings.Settings$AppNotificationSettingsActivity; content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Chitalishte Yavorets" text="All Chitalishte Yavorets notifications" text="Приложението" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения" content-desc="Съобщения" text="Важни съобщения" content-desc="Важни съобщения" 
SYSTEM notification settings (scrolled): content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Приложението" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения" content-desc="Съобщения" text="Важни съобщения" content-desc="Важни съобщения" text="Новини" content-desc="Новини" text="Моята библиотека" text="Отново на рафта" content-desc="Отново на рафта" text="Срокове за връщане" content-desc="Срокове за връщане" text="Allow notification dot" 
WIDGET after the app ran: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
WIDGET dark after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|; error text: 
## COMPLETE
## logcat findings
app pids seen: ['5205', '7792', '8438']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=47 total=47
== Davey: 3
   davey events=3 max=1249ms median=750ms
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

### gallery/shots/gallery

- [gallery/shots/gallery/01-album-online.png](gallery/shots/gallery/01-album-online.png)
- [gallery/shots/gallery/02-album-offline.png](gallery/shots/gallery/02-album-offline.png)
- [gallery/shots/gallery/03-album-offline-scrolled.png](gallery/shots/gallery/03-album-offline-scrolled.png)
- [gallery/shots/gallery/04-album-recovered.png](gallery/shots/gallery/04-album-recovered.png)
- [gallery/shots/gallery/05-album-top-recovered.png](gallery/shots/gallery/05-album-top-recovered.png)
- [gallery/shots/gallery/10-404-tile.png](gallery/shots/gallery/10-404-tile.png)
- [gallery/shots/gallery/11-viewer-404.png](gallery/shots/gallery/11-viewer-404.png)
- [gallery/shots/gallery/20-viewer-start.png](gallery/shots/gallery/20-viewer-start.png)
- [gallery/shots/gallery/21-after-tap1.png](gallery/shots/gallery/21-after-tap1.png)
- [gallery/shots/gallery/22-after-tap2.png](gallery/shots/gallery/22-after-tap2.png)
- [gallery/shots/gallery/30-after-short-drag.png](gallery/shots/gallery/30-after-short-drag.png)
- [gallery/shots/gallery/31-after-long-drag.png](gallery/shots/gallery/31-after-long-drag.png)
- [gallery/shots/gallery/40-1x.png](gallery/shots/gallery/40-1x.png)
- [gallery/shots/gallery/41-2x-doubletap.png](gallery/shots/gallery/41-2x-doubletap.png)
- [gallery/shots/gallery/42-pinched.png](gallery/shots/gallery/42-pinched.png)
- [gallery/shots/gallery/43-vertical-drag-while-zoomed.png](gallery/shots/gallery/43-vertical-drag-while-zoomed.png)
- [gallery/shots/gallery/44-reset.png](gallery/shots/gallery/44-reset.png)
- [gallery/shots/gallery/60-before-gesture.png](gallery/shots/gallery/60-before-gesture.png)
- [gallery/shots/gallery/61-gesture-30pct.png](gallery/shots/gallery/61-gesture-30pct.png)
- [gallery/shots/gallery/62-gesture-50pct.png](gallery/shots/gallery/62-gesture-50pct.png)
- [gallery/shots/gallery/63-after-cancelled-gesture.png](gallery/shots/gallery/63-after-cancelled-gesture.png)
- [gallery/shots/gallery/64-full-gesture-midway.png](gallery/shots/gallery/64-full-gesture-midway.png)
- [gallery/shots/gallery/65-after-full-gesture.png](gallery/shots/gallery/65-after-full-gesture.png)

### gallery34/shots/gallery

- [gallery34/shots/gallery/01-album-online.png](gallery34/shots/gallery/01-album-online.png)
- [gallery34/shots/gallery/02-album-offline.png](gallery34/shots/gallery/02-album-offline.png)
- [gallery34/shots/gallery/03-album-offline-scrolled.png](gallery34/shots/gallery/03-album-offline-scrolled.png)
- [gallery34/shots/gallery/04-album-recovered.png](gallery34/shots/gallery/04-album-recovered.png)
- [gallery34/shots/gallery/05-album-top-recovered.png](gallery34/shots/gallery/05-album-top-recovered.png)
- [gallery34/shots/gallery/10-404-tile.png](gallery34/shots/gallery/10-404-tile.png)
- [gallery34/shots/gallery/11-viewer-404.png](gallery34/shots/gallery/11-viewer-404.png)
- [gallery34/shots/gallery/20-viewer-start.png](gallery34/shots/gallery/20-viewer-start.png)
- [gallery34/shots/gallery/21-after-tap1.png](gallery34/shots/gallery/21-after-tap1.png)
- [gallery34/shots/gallery/22-after-tap2.png](gallery34/shots/gallery/22-after-tap2.png)
- [gallery34/shots/gallery/30-after-short-drag.png](gallery34/shots/gallery/30-after-short-drag.png)
- [gallery34/shots/gallery/31-after-long-drag.png](gallery34/shots/gallery/31-after-long-drag.png)
- [gallery34/shots/gallery/40-1x.png](gallery34/shots/gallery/40-1x.png)
- [gallery34/shots/gallery/41-2x-doubletap.png](gallery34/shots/gallery/41-2x-doubletap.png)
- [gallery34/shots/gallery/42-pinched.png](gallery34/shots/gallery/42-pinched.png)
- [gallery34/shots/gallery/43-vertical-drag-while-zoomed.png](gallery34/shots/gallery/43-vertical-drag-while-zoomed.png)
- [gallery34/shots/gallery/44-reset.png](gallery34/shots/gallery/44-reset.png)
- [gallery34/shots/gallery/60-before-gesture.png](gallery34/shots/gallery/60-before-gesture.png)
- [gallery34/shots/gallery/61-gesture-30pct.png](gallery34/shots/gallery/61-gesture-30pct.png)
- [gallery34/shots/gallery/62-gesture-50pct.png](gallery34/shots/gallery/62-gesture-50pct.png)
- [gallery34/shots/gallery/62b-gesture-70pct.png](gallery34/shots/gallery/62b-gesture-70pct.png)
- [gallery34/shots/gallery/63-after-cancelled-gesture.png](gallery34/shots/gallery/63-after-cancelled-gesture.png)
- [gallery34/shots/gallery/64-full-gesture-midway.png](gallery34/shots/gallery/64-full-gesture-midway.png)
- [gallery34/shots/gallery/65-after-full-gesture.png](gallery34/shots/gallery/65-after-full-gesture.png)

### home/shots/home

- [home/shots/home/base-events-1.png](home/shots/home/base-events-1.png)
- [home/shots/home/base-events-2.png](home/shots/home/base-events-2.png)
- [home/shots/home/base-events-3.png](home/shots/home/base-events-3.png)
- [home/shots/home/base-new-arrivals-0.png](home/shots/home/base-new-arrivals-0.png)
- [home/shots/home/base-new-arrivals-end.png](home/shots/home/base-new-arrivals-end.png)
- [home/shots/home/c-00.png](home/shots/home/c-00.png)
- [home/shots/home/c-01.png](home/shots/home/c-01.png)
- [home/shots/home/c-02.png](home/shots/home/c-02.png)
- [home/shots/home/c-03-swiped-left.png](home/shots/home/c-03-swiped-left.png)
- [home/shots/home/c-04-swiped-right.png](home/shots/home/c-04-swiped-right.png)
- [home/shots/home/c-05-after-60s.png](home/shots/home/c-05-after-60s.png)
- [home/shots/home/c-06-after-67s.png](home/shots/home/c-06-after-67s.png)
- [home/shots/home/c-10-dark.png](home/shots/home/c-10-dark.png)
- [home/shots/home/c-11-font130-xlarge.png](home/shots/home/c-11-font130-xlarge.png)
- [home/shots/home/c-12-landscape.png](home/shots/home/c-12-landscape.png)
- [home/shots/home/new-events-1.png](home/shots/home/new-events-1.png)
- [home/shots/home/new-events-2.png](home/shots/home/new-events-2.png)
- [home/shots/home/new-events-3.png](home/shots/home/new-events-3.png)
- [home/shots/home/new-new-arrivals-0.png](home/shots/home/new-new-arrivals-0.png)
- [home/shots/home/new-new-arrivals-end.png](home/shots/home/new-new-arrivals-end.png)
- [home/shots/home/s-00-first-search.png](home/shots/home/s-00-first-search.png)
- [home/shots/home/s-01-results.png](home/shots/home/s-01-results.png)
- [home/shots/home/s-02-back-from-result.png](home/shots/home/s-02-back-from-result.png)
- [home/shots/home/s-03-after-one-back.png](home/shots/home/s-03-after-one-back.png)
- [home/shots/home/s-04-home-tab-again.png](home/shots/home/s-04-home-tab-again.png)
- [home/shots/home/s-05-back-from-book-after-tab-switch.png](home/shots/home/s-05-back-from-book-after-tab-switch.png)
- [home/shots/home/s-06-show-all.png](home/shots/home/s-06-show-all.png)

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

### mem-base/shots

- [mem-base/shots/cycles-after-search.png](mem-base/shots/cycles-after-search.png)
- [mem-base/shots/cycles-book.png](mem-base/shots/cycles-book.png)

### mem/shots

- [mem/shots/cycles-after-search.png](mem/shots/cycles-after-search.png)
- [mem/shots/cycles-book.png](mem/shots/cycles-book.png)

### monkey/shots

- [monkey/shots/monkey-end.png](monkey/shots/monkey-end.png)

### notif/shots/notif

- [notif/shots/notif/20-news-0-shade.png](notif/shots/notif/20-news-0-shade.png)
- [notif/shots/notif/20-news-1-expanded.png](notif/shots/notif/20-news-1-expanded.png)
- [notif/shots/notif/21-news-share.png](notif/shots/notif/21-news-share.png)
- [notif/shots/notif/30-events.png](notif/shots/notif/30-events.png)
- [notif/shots/notif/31-reminder-set-2.png](notif/shots/notif/31-reminder-set-2.png)
- [notif/shots/notif/31-reminder-set-3.png](notif/shots/notif/31-reminder-set-3.png)
- [notif/shots/notif/31-reminder-set-4.png](notif/shots/notif/31-reminder-set-4.png)
- [notif/shots/notif/40-event-0-shade.png](notif/shots/notif/40-event-0-shade.png)
- [notif/shots/notif/40-event-1-expanded.png](notif/shots/notif/40-event-1-expanded.png)
- [notif/shots/notif/42-event-again-0-shade.png](notif/shots/notif/42-event-again-0-shade.png)
- [notif/shots/notif/42-event-again-1-expanded.png](notif/shots/notif/42-event-again-1-expanded.png)
- [notif/shots/notif/43-calendar.png](notif/shots/notif/43-calendar.png)

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

### scan33/shots/contacts

- [scan33/shots/contacts/40-standard.png](scan33/shots/contacts/40-standard.png)
- [scan33/shots/contacts/41-font130-xlarge.png](scan33/shots/contacts/41-font130-xlarge.png)

### scan33/shots/scan

- [scan33/shots/scan/10-home-10s.png](scan33/shots/scan/10-home-10s.png)
- [scan33/shots/scan/10-home-20s.png](scan33/shots/scan/10-home-20s.png)
- [scan33/shots/scan/10-home-2s.png](scan33/shots/scan/10-home-2s.png)
- [scan33/shots/scan/10-home-5s.png](scan33/shots/scan/10-home-5s.png)
- [scan33/shots/scan/20-after-back.png](scan33/shots/scan/20-after-back.png)
- [scan33/shots/scan/30-book.png](scan33/shots/scan/30-book.png)
- [scan33/shots/scan/30-manual-result.png](scan33/shots/scan/30-manual-result.png)
- [scan33/shots/scan/40-shortcut-10s.png](scan33/shots/scan/40-shortcut-10s.png)
- [scan33/shots/scan/40-shortcut-20s.png](scan33/shots/scan/40-shortcut-20s.png)
- [scan33/shots/scan/40-shortcut-2s.png](scan33/shots/scan/40-shortcut-2s.png)
- [scan33/shots/scan/40-shortcut-5s.png](scan33/shots/scan/40-shortcut-5s.png)
- [scan33/shots/scan/45-shortcut-after-back.png](scan33/shots/scan/45-shortcut-after-back.png)
- [scan33/shots/scan/50-again-10s.png](scan33/shots/scan/50-again-10s.png)
- [scan33/shots/scan/50-again-20s.png](scan33/shots/scan/50-again-20s.png)
- [scan33/shots/scan/50-again-2s.png](scan33/shots/scan/50-again-2s.png)
- [scan33/shots/scan/50-again-5s.png](scan33/shots/scan/50-again-5s.png)

### scan33/shots/shortcuts

- [scan33/shots/shortcuts/10-after-book-popup.png](scan33/shots/shortcuts/10-after-book-popup.png)
- [scan33/shots/shortcuts/20-clear-dialog.png](scan33/shots/shortcuts/20-clear-dialog.png)
- [scan33/shots/shortcuts/30-after-clear-popup.png](scan33/shots/shortcuts/30-after-clear-popup.png)

### scan33cam/shots/scan

- [scan33cam/shots/scan/10-home-10s.png](scan33cam/shots/scan/10-home-10s.png)
- [scan33cam/shots/scan/10-home-20s.png](scan33cam/shots/scan/10-home-20s.png)
- [scan33cam/shots/scan/10-home-2s.png](scan33cam/shots/scan/10-home-2s.png)
- [scan33cam/shots/scan/10-home-5s.png](scan33cam/shots/scan/10-home-5s.png)
- [scan33cam/shots/scan/20-after-back.png](scan33cam/shots/scan/20-after-back.png)
- [scan33cam/shots/scan/30-book.png](scan33cam/shots/scan/30-book.png)
- [scan33cam/shots/scan/30-manual-result.png](scan33cam/shots/scan/30-manual-result.png)
- [scan33cam/shots/scan/40-shortcut-10s.png](scan33cam/shots/scan/40-shortcut-10s.png)
- [scan33cam/shots/scan/40-shortcut-20s.png](scan33cam/shots/scan/40-shortcut-20s.png)
- [scan33cam/shots/scan/40-shortcut-2s.png](scan33cam/shots/scan/40-shortcut-2s.png)
- [scan33cam/shots/scan/40-shortcut-5s.png](scan33cam/shots/scan/40-shortcut-5s.png)
- [scan33cam/shots/scan/45-shortcut-after-back.png](scan33cam/shots/scan/45-shortcut-after-back.png)
- [scan33cam/shots/scan/50-again-10s.png](scan33cam/shots/scan/50-again-10s.png)
- [scan33cam/shots/scan/50-again-20s.png](scan33cam/shots/scan/50-again-20s.png)
- [scan33cam/shots/scan/50-again-2s.png](scan33cam/shots/scan/50-again-2s.png)
- [scan33cam/shots/scan/50-again-5s.png](scan33cam/shots/scan/50-again-5s.png)

### scan33keep/shots/scan

- [scan33keep/shots/scan/10-home-10s.png](scan33keep/shots/scan/10-home-10s.png)
- [scan33keep/shots/scan/10-home-20s.png](scan33keep/shots/scan/10-home-20s.png)
- [scan33keep/shots/scan/10-home-2s.png](scan33keep/shots/scan/10-home-2s.png)
- [scan33keep/shots/scan/10-home-5s.png](scan33keep/shots/scan/10-home-5s.png)
- [scan33keep/shots/scan/20-after-back.png](scan33keep/shots/scan/20-after-back.png)
- [scan33keep/shots/scan/30-book.png](scan33keep/shots/scan/30-book.png)
- [scan33keep/shots/scan/30-manual-result.png](scan33keep/shots/scan/30-manual-result.png)
- [scan33keep/shots/scan/40-shortcut-10s.png](scan33keep/shots/scan/40-shortcut-10s.png)
- [scan33keep/shots/scan/40-shortcut-20s.png](scan33keep/shots/scan/40-shortcut-20s.png)
- [scan33keep/shots/scan/40-shortcut-2s.png](scan33keep/shots/scan/40-shortcut-2s.png)
- [scan33keep/shots/scan/40-shortcut-5s.png](scan33keep/shots/scan/40-shortcut-5s.png)
- [scan33keep/shots/scan/45-shortcut-after-back.png](scan33keep/shots/scan/45-shortcut-after-back.png)
- [scan33keep/shots/scan/50-again-10s.png](scan33keep/shots/scan/50-again-10s.png)
- [scan33keep/shots/scan/50-again-20s.png](scan33keep/shots/scan/50-again-20s.png)
- [scan33keep/shots/scan/50-again-2s.png](scan33keep/shots/scan/50-again-2s.png)
- [scan33keep/shots/scan/50-again-5s.png](scan33keep/shots/scan/50-again-5s.png)

### scan34/shots/scan

- [scan34/shots/scan/10-home-10s.png](scan34/shots/scan/10-home-10s.png)
- [scan34/shots/scan/10-home-20s.png](scan34/shots/scan/10-home-20s.png)
- [scan34/shots/scan/10-home-2s.png](scan34/shots/scan/10-home-2s.png)
- [scan34/shots/scan/10-home-5s.png](scan34/shots/scan/10-home-5s.png)
- [scan34/shots/scan/20-after-back.png](scan34/shots/scan/20-after-back.png)
- [scan34/shots/scan/30-book.png](scan34/shots/scan/30-book.png)
- [scan34/shots/scan/30-manual-result.png](scan34/shots/scan/30-manual-result.png)
- [scan34/shots/scan/40-shortcut-10s.png](scan34/shots/scan/40-shortcut-10s.png)
- [scan34/shots/scan/40-shortcut-20s.png](scan34/shots/scan/40-shortcut-20s.png)
- [scan34/shots/scan/40-shortcut-2s.png](scan34/shots/scan/40-shortcut-2s.png)
- [scan34/shots/scan/40-shortcut-5s.png](scan34/shots/scan/40-shortcut-5s.png)
- [scan34/shots/scan/45-shortcut-after-back.png](scan34/shots/scan/45-shortcut-after-back.png)
- [scan34/shots/scan/50-again-10s.png](scan34/shots/scan/50-again-10s.png)
- [scan34/shots/scan/50-again-20s.png](scan34/shots/scan/50-again-20s.png)
- [scan34/shots/scan/50-again-2s.png](scan34/shots/scan/50-again-2s.png)
- [scan34/shots/scan/50-again-5s.png](scan34/shots/scan/50-again-5s.png)

### scan34ps/shots/scan

- [scan34ps/shots/scan/10-home-10s.png](scan34ps/shots/scan/10-home-10s.png)
- [scan34ps/shots/scan/10-home-20s.png](scan34ps/shots/scan/10-home-20s.png)
- [scan34ps/shots/scan/10-home-2s.png](scan34ps/shots/scan/10-home-2s.png)
- [scan34ps/shots/scan/10-home-5s.png](scan34ps/shots/scan/10-home-5s.png)
- [scan34ps/shots/scan/20-after-back.png](scan34ps/shots/scan/20-after-back.png)
- [scan34ps/shots/scan/30-manual-result.png](scan34ps/shots/scan/30-manual-result.png)
- [scan34ps/shots/scan/40-shortcut-10s.png](scan34ps/shots/scan/40-shortcut-10s.png)
- [scan34ps/shots/scan/40-shortcut-20s.png](scan34ps/shots/scan/40-shortcut-20s.png)
- [scan34ps/shots/scan/40-shortcut-2s.png](scan34ps/shots/scan/40-shortcut-2s.png)
- [scan34ps/shots/scan/40-shortcut-5s.png](scan34ps/shots/scan/40-shortcut-5s.png)
- [scan34ps/shots/scan/45-shortcut-after-back.png](scan34ps/shots/scan/45-shortcut-after-back.png)
- [scan34ps/shots/scan/50-again-10s.png](scan34ps/shots/scan/50-again-10s.png)
- [scan34ps/shots/scan/50-again-20s.png](scan34ps/shots/scan/50-again-20s.png)
- [scan34ps/shots/scan/50-again-2s.png](scan34ps/shots/scan/50-again-2s.png)
- [scan34ps/shots/scan/50-again-5s.png](scan34ps/shots/scan/50-again-5s.png)

### tile33/shots/tile

- [tile33/shots/tile/10-qs-no-card.png](tile33/shots/tile/10-qs-no-card.png)
- [tile33/shots/tile/30-qs-with-card.png](tile33/shots/tile/30-qs-with-card.png)
- [tile33/shots/tile/40-lockscreen.png](tile33/shots/tile/40-lockscreen.png)
- [tile33/shots/tile/41-qs-locked.png](tile33/shots/tile/41-qs-locked.png)
- [tile33/shots/tile/42-after-tap-locked.png](tile33/shots/tile/42-after-tap-locked.png)

### tile34/shots/tile

- [tile34/shots/tile/10-qs-no-card.png](tile34/shots/tile/10-qs-no-card.png)
- [tile34/shots/tile/30-qs-with-card.png](tile34/shots/tile/30-qs-with-card.png)
- [tile34/shots/tile/40-lockscreen.png](tile34/shots/tile/40-lockscreen.png)
- [tile34/shots/tile/41-qs-locked.png](tile34/shots/tile/41-qs-locked.png)
- [tile34/shots/tile/42-after-tap-locked.png](tile34/shots/tile/42-after-tap-locked.png)
- [tile34/shots/tile/43-after-unlock.png](tile34/shots/tile/43-after-unlock.png)

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
- [widget/shots/widget/card-80-card-4x2-dark.png](widget/shots/widget/card-80-card-4x2-dark.png)
- [widget/shots/widget/card-80-card-4x2-light.png](widget/shots/widget/card-80-card-4x2-light.png)
- [widget/shots/widget/card-81-card-4x3-dark.png](widget/shots/widget/card-81-card-4x3-dark.png)
- [widget/shots/widget/card-81-card-4x3-light.png](widget/shots/widget/card-81-card-4x3-light.png)
- [widget/shots/widget/card-82-card-2x3-dark.png](widget/shots/widget/card-82-card-2x3-dark.png)
- [widget/shots/widget/card-82-card-2x3-light.png](widget/shots/widget/card-82-card-2x3-light.png)
- [widget/shots/widget/card-83-card-2x2-dark.png](widget/shots/widget/card-83-card-2x2-dark.png)
- [widget/shots/widget/card-83-card-2x2-light.png](widget/shots/widget/card-83-card-2x2-light.png)
- [widget/shots/widget/card-84-card-3x2-dark.png](widget/shots/widget/card-84-card-3x2-dark.png)
- [widget/shots/widget/card-84-card-3x2-light.png](widget/shots/widget/card-84-card-3x2-light.png)
- [widget/shots/widget/card-resize-card-2x2-frame.png](widget/shots/widget/card-resize-card-2x2-frame.png)
- [widget/shots/widget/card-resize-card-2x3-frame.png](widget/shots/widget/card-resize-card-2x3-frame.png)
- [widget/shots/widget/card-resize-card-3x2-frame.png](widget/shots/widget/card-resize-card-3x2-frame.png)
- [widget/shots/widget/card-resize-card-4x3-frame.png](widget/shots/widget/card-resize-card-4x3-frame.png)
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
- [gallery/album-offline.xml](gallery/album-offline.xml)
- [gallery/drag.sh](gallery/drag.sh)
- [gallery/getevent.txt](gallery/getevent.txt)
- [gallery/host-after-run.txt](gallery/host-after-run.txt)
- [gallery/job-status.txt](gallery/job-status.txt)
- [gallery/logcat-all.txt](gallery/logcat-all.txt)
- [gallery/logcat-dump.txt](gallery/logcat-dump.txt)
- [gallery/logcat-findings.txt](gallery/logcat-findings.txt)
- [gallery/logcat-live.txt](gallery/logcat-live.txt)
- [gallery/mt.sh](gallery/mt.sh)
- [gallery/summary.txt](gallery/summary.txt)
- [gallery34/album-offline.xml](gallery34/album-offline.xml)
- [gallery34/drag.sh](gallery34/drag.sh)
- [gallery34/getevent.txt](gallery34/getevent.txt)
- [gallery34/host-after-run.txt](gallery34/host-after-run.txt)
- [gallery34/job-status.txt](gallery34/job-status.txt)
- [gallery34/logcat-all.txt](gallery34/logcat-all.txt)
- [gallery34/logcat-dump.txt](gallery34/logcat-dump.txt)
- [gallery34/logcat-findings.txt](gallery34/logcat-findings.txt)
- [gallery34/logcat-live.txt](gallery34/logcat-live.txt)
- [gallery34/mt.sh](gallery34/mt.sh)
- [gallery34/summary.txt](gallery34/summary.txt)
- [home/host-after-run.txt](home/host-after-run.txt)
- [home/job-status.txt](home/job-status.txt)
- [home/logcat-all.txt](home/logcat-all.txt)
- [home/logcat-dump.txt](home/logcat-dump.txt)
- [home/logcat-findings.txt](home/logcat-findings.txt)
- [home/logcat-live.txt](home/logcat-live.txt)
- [home/newarr-base-0.xml](home/newarr-base-0.xml)
- [home/newarr-base-1.xml](home/newarr-base-1.xml)
- [home/newarr-base-2.xml](home/newarr-base-2.xml)
- [home/newarr-base-3.xml](home/newarr-base-3.xml)
- [home/newarr-base-4.xml](home/newarr-base-4.xml)
- [home/newarr-new-0.xml](home/newarr-new-0.xml)
- [home/newarr-new-1.xml](home/newarr-new-1.xml)
- [home/newarr-new-2.xml](home/newarr-new-2.xml)
- [home/newarr-new-3.xml](home/newarr-new-3.xml)
- [home/newarr-new-4.xml](home/newarr-new-4.xml)
- [home/summary.txt](home/summary.txt)
- [login/host-after-run.txt](login/host-after-run.txt)
- [login/job-status.txt](login/job-status.txt)
- [login/logcat-all.txt](login/logcat-all.txt)
- [login/logcat-dump.txt](login/logcat-dump.txt)
- [login/logcat-findings.txt](login/logcat-findings.txt)
- [login/logcat-live.txt](login/logcat-live.txt)
- [login/login-open.xml](login/login-open.xml)
- [login/login-remember-before.xml](login/login-remember-before.xml)
- [login/summary.txt](login/summary.txt)
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
- [notif/androidx.work.workdb](notif/androidx.work.workdb)
- [notif/host-after-run.txt](notif/host-after-run.txt)
- [notif/job-status.txt](notif/job-status.txt)
- [notif/logcat-all.txt](notif/logcat-all.txt)
- [notif/logcat-dump.txt](notif/logcat-dump.txt)
- [notif/logcat-findings.txt](notif/logcat-findings.txt)
- [notif/logcat-live.txt](notif/logcat-live.txt)
- [notif/notif-n-end.txt](notif/notif-n-end.txt)
- [notif/notif-n1-forced.txt](notif/notif-n1-forced.txt)
- [notif/notif-n1-natural.txt](notif/notif-n1-natural.txt)
- [notif/notif-n1-rewound.txt](notif/notif-n1-rewound.txt)
- [notif/notif-n3.txt](notif/notif-n3.txt)
- [notif/settings-after.pb](notif/settings-after.pb)
- [notif/settings.preferences_pb](notif/settings.preferences_pb)
- [notif/shade-20-news.xml](notif/shade-20-news.xml)
- [notif/shade-40-event.xml](notif/shade-40-event.xml)
- [notif/shade-42-event-again.xml](notif/shade-42-event-again.xml)
- [notif/summary.txt](notif/summary.txt)
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
- [scan33/contacts-40-standard.txt](scan33/contacts-40-standard.txt)
- [scan33/contacts-40-standard.xml](scan33/contacts-40-standard.xml)
- [scan33/contacts-41-font130-xlarge.txt](scan33/contacts-41-font130-xlarge.txt)
- [scan33/contacts-41-font130-xlarge.xml](scan33/contacts-41-font130-xlarge.xml)
- [scan33/dumpsys-package.txt](scan33/dumpsys-package.txt)
- [scan33/host-after-run.txt](scan33/host-after-run.txt)
- [scan33/job-status.txt](scan33/job-status.txt)
- [scan33/launcher-10-after-book.xml](scan33/launcher-10-after-book.xml)
- [scan33/launcher-30-after-clear.xml](scan33/launcher-30-after-clear.xml)
- [scan33/logcat-all.txt](scan33/logcat-all.txt)
- [scan33/logcat-dump.txt](scan33/logcat-dump.txt)
- [scan33/logcat-findings.txt](scan33/logcat-findings.txt)
- [scan33/logcat-live.txt](scan33/logcat-live.txt)
- [scan33/logcat-scanner.txt](scan33/logcat-scanner.txt)
- [scan33/shortcut-0-app.txt](scan33/shortcut-0-app.txt)
- [scan33/shortcut-0.txt](scan33/shortcut-0.txt)
- [scan33/shortcut-1-app.txt](scan33/shortcut-1-app.txt)
- [scan33/shortcut-1.txt](scan33/shortcut-1.txt)
- [scan33/shortcut-2-app.txt](scan33/shortcut-2-app.txt)
- [scan33/shortcut-2.txt](scan33/shortcut-2.txt)
- [scan33/summary.txt](scan33/summary.txt)
- [scan33cam/dumpsys-package.txt](scan33cam/dumpsys-package.txt)
- [scan33cam/host-after-run.txt](scan33cam/host-after-run.txt)
- [scan33cam/job-status.txt](scan33cam/job-status.txt)
- [scan33cam/logcat-all.txt](scan33cam/logcat-all.txt)
- [scan33cam/logcat-dump.txt](scan33cam/logcat-dump.txt)
- [scan33cam/logcat-findings.txt](scan33cam/logcat-findings.txt)
- [scan33cam/logcat-live.txt](scan33cam/logcat-live.txt)
- [scan33cam/logcat-scanner.txt](scan33cam/logcat-scanner.txt)
- [scan33cam/summary.txt](scan33cam/summary.txt)
- [scan33keep/dumpsys-package.txt](scan33keep/dumpsys-package.txt)
- [scan33keep/host-after-run.txt](scan33keep/host-after-run.txt)
- [scan33keep/job-status.txt](scan33keep/job-status.txt)
- [scan33keep/logcat-all.txt](scan33keep/logcat-all.txt)
- [scan33keep/logcat-dump.txt](scan33keep/logcat-dump.txt)
- [scan33keep/logcat-findings.txt](scan33keep/logcat-findings.txt)
- [scan33keep/logcat-live.txt](scan33keep/logcat-live.txt)
- [scan33keep/logcat-scanner.txt](scan33keep/logcat-scanner.txt)
- [scan33keep/summary.txt](scan33keep/summary.txt)
- [scan34/dumpsys-package.txt](scan34/dumpsys-package.txt)
- [scan34/host-after-run.txt](scan34/host-after-run.txt)
- [scan34/job-status.txt](scan34/job-status.txt)
- [scan34/logcat-all.txt](scan34/logcat-all.txt)
- [scan34/logcat-dump.txt](scan34/logcat-dump.txt)
- [scan34/logcat-findings.txt](scan34/logcat-findings.txt)
- [scan34/logcat-live.txt](scan34/logcat-live.txt)
- [scan34/logcat-scanner.txt](scan34/logcat-scanner.txt)
- [scan34/login-open.xml](scan34/login-open.xml)
- [scan34/login-remember-before.xml](scan34/login-remember-before.xml)
- [scan34/summary.txt](scan34/summary.txt)
- [scan34ps/dumpsys-package.txt](scan34ps/dumpsys-package.txt)
- [scan34ps/host-after-run.txt](scan34ps/host-after-run.txt)
- [scan34ps/job-status.txt](scan34ps/job-status.txt)
- [scan34ps/logcat-all.txt](scan34ps/logcat-all.txt)
- [scan34ps/logcat-dump.txt](scan34ps/logcat-dump.txt)
- [scan34ps/logcat-findings.txt](scan34ps/logcat-findings.txt)
- [scan34ps/logcat-live.txt](scan34ps/logcat-live.txt)
- [scan34ps/logcat-scanner.txt](scan34ps/logcat-scanner.txt)
- [scan34ps/summary.txt](scan34ps/summary.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-base-upd-ref.txt](size/apk-base-upd-ref.txt)
- [size/apk-keep-ref.txt](size/apk-keep-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/size-base.txt](size/size-base.txt)
- [size/size-new.txt](size/size-new.txt)
- [tile33/host-after-run.txt](tile33/host-after-run.txt)
- [tile33/job-status.txt](tile33/job-status.txt)
- [tile33/logcat-all.txt](tile33/logcat-all.txt)
- [tile33/logcat-dump.txt](tile33/logcat-dump.txt)
- [tile33/logcat-findings.txt](tile33/logcat-findings.txt)
- [tile33/logcat-live.txt](tile33/logcat-live.txt)
- [tile33/qs-locked-page1.xml](tile33/qs-locked-page1.xml)
- [tile33/qs-locked-page2.xml](tile33/qs-locked-page2.xml)
- [tile33/qs-locked-page3.xml](tile33/qs-locked-page3.xml)
- [tile33/qs-locked-page4.xml](tile33/qs-locked-page4.xml)
- [tile33/qs-no-card-page1.xml](tile33/qs-no-card-page1.xml)
- [tile33/qs-with-card-page1.xml](tile33/qs-with-card-page1.xml)
- [tile33/qs-with-card-page2.xml](tile33/qs-with-card-page2.xml)
- [tile33/qs-with-card-page3.xml](tile33/qs-with-card-page3.xml)
- [tile33/qs-with-card-page4.xml](tile33/qs-with-card-page4.xml)
- [tile33/summary.txt](tile33/summary.txt)
- [tile33/tile-add-dialog.xml](tile33/tile-add-dialog.xml)
- [tile33/tile-locked-tap.xml](tile33/tile-locked-tap.xml)
- [tile34/host-after-run.txt](tile34/host-after-run.txt)
- [tile34/job-status.txt](tile34/job-status.txt)
- [tile34/logcat-all.txt](tile34/logcat-all.txt)
- [tile34/logcat-dump.txt](tile34/logcat-dump.txt)
- [tile34/logcat-findings.txt](tile34/logcat-findings.txt)
- [tile34/logcat-live.txt](tile34/logcat-live.txt)
- [tile34/qs-locked-page1.xml](tile34/qs-locked-page1.xml)
- [tile34/qs-locked-page2.xml](tile34/qs-locked-page2.xml)
- [tile34/qs-locked-page3.xml](tile34/qs-locked-page3.xml)
- [tile34/qs-locked-page4.xml](tile34/qs-locked-page4.xml)
- [tile34/qs-no-card-page1.xml](tile34/qs-no-card-page1.xml)
- [tile34/qs-with-card-page1.xml](tile34/qs-with-card-page1.xml)
- [tile34/qs-with-card-page2.xml](tile34/qs-with-card-page2.xml)
- [tile34/qs-with-card-page3.xml](tile34/qs-with-card-page3.xml)
- [tile34/qs-with-card-page4.xml](tile34/qs-with-card-page4.xml)
- [tile34/summary.txt](tile34/summary.txt)
- [tile34/tile-add-dialog.xml](tile34/tile-add-dialog.xml)
- [tile34/tile-locked-tap.xml](tile34/tile-locked-tap.xml)
- [update/41249616504/none.json](update/41249616504/none.json)
- [update/41249616504/notes.bg.txt](update/41249616504/notes.bg.txt)
- [update/41249616504/notes.en.txt](update/41249616504/notes.en.txt)
- [update/41249616504/update.json](update/41249616504/update.json)
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
- [widget/appwidget-card-card-2x2.txt](widget/appwidget-card-card-2x2.txt)
- [widget/appwidget-card-card-2x3.txt](widget/appwidget-card-card-2x3.txt)
- [widget/appwidget-card-card-3x2.txt](widget/appwidget-card-card-3x2.txt)
- [widget/appwidget-card-card-4x3.txt](widget/appwidget-card-card-4x3.txt)
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
- [widget/widget-card-80-card-4x2.xml](widget/widget-card-80-card-4x2.xml)
- [widget/widget-card-81-card-4x3.xml](widget/widget-card-81-card-4x3.xml)
- [widget/widget-card-82-card-2x3.xml](widget/widget-card-82-card-2x3.xml)
- [widget/widget-card-83-card-2x2.xml](widget/widget-card-83-card-2x2.xml)
- [widget/widget-card-84-card-3x2.xml](widget/widget-card-84-card-3x2.xml)
- [widget/widget-new-10-wide-4x2.xml](widget/widget-new-10-wide-4x2.xml)
- [widget/widget-new-20-wide-tall-4x3.xml](widget/widget-new-20-wide-tall-4x3.xml)
- [widget/widget-new-30-medium-3x2.xml](widget/widget-new-30-medium-3x2.xml)
- [widget/widget-new-40-small-2x2.xml](widget/widget-new-40-small-2x2.xml)
- [widget/widget-new-50-small-tall-2x3.xml](widget/widget-new-50-small-tall-2x3.xml)
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
