# Wave 14 emulator check before release (52334a7, test build v1.2.28 vs v1.2.27)

Source: `debug/wave14-check` @ a1d40ba (app = 52334a7 `claude/charming-rubin-3lzor2`, compared with v1.2.27 = 8dc63c1), last run 41080025588 (attempt 1), 2026-10-30T15:45:51Z. Runs: [runs.txt](runs.txt).
Emulators: google_apis x86_64 API 33, pixel_6 profile, -gpu swangle_indirect; app locale bg-BG (EN in look1 and fonts). APKs: minified prodRelease, x86_64 added, throwaway key; v1.2.28 versionCode 1484480, v1.2.27 versionCode 1484479.
Earlier: v1.2.27 full test [../v127b/index.md](../v127b/index.md).

## APK size (release configuration: arm64-v8a + armeabi-v7a, no x86_64)

```
== new 1.2.28 52334a7: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4279296 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1484480  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.28" (Raw: "1.2.28")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
part                    compressed  uncompressed  files
classes*.dex               3499230       6870364      2
res (other)                 468751        833148     58
resources.arsc              213284        213284      1
lib/arm64-v8a                26028         57912      4
lib/armeabi-v7a              22503         41024      4
assets/                      18984         18984      2
AndroidManifest.xml           4889         20880      1
META-INF/                      846          1288      6
native libraries (compress type 0 = stored, 8 = deflated):
  lib/arm64-v8a/libandroidx.graphics.path.so                   type=8      4386 /     10096
  lib/arm64-v8a/libdatastore_shared_counter.so                 type=8      4256 /     10376
  lib/arm64-v8a/libimage_processing_util_jni.so                type=8     15630 /     32544
  lib/arm64-v8a/libsurface_util_jni.so                         type=8      1756 /      4896
  lib/armeabi-v7a/libandroidx.graphics.path.so                 type=8      4064 /      7252
  lib/armeabi-v7a/libdatastore_shared_counter.so               type=8      2537 /      5864
  lib/armeabi-v7a/libimage_processing_util_jni.so              type=8     14296 /     24448
  lib/armeabi-v7a/libsurface_util_jni.so                       type=8      1606 /      3460
fonts:
== base 1.2.27 8dc63c1: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4759051 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1484479  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.27" (Raw: "1.2.27")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=false 
part                    compressed  uncompressed  files
classes*.dex               3318824       6510820      2
res (other)                 834675       1732326   1006
resources.arsc              314344        314344      1
lib/arm64-v8a                57912         57912      4
lib/armeabi-v7a              41024         41024      4
assets/                      18394         18394      2
AndroidManifest.xml           4768         20304      1
META-INF/                      846          1288      6
native libraries (compress type 0 = stored, 8 = deflated):
  lib/arm64-v8a/libandroidx.graphics.path.so                   type=0     10096 /     10096
  lib/arm64-v8a/libdatastore_shared_counter.so                 type=0     10376 /     10376
  lib/arm64-v8a/libimage_processing_util_jni.so                type=0     32544 /     32544
  lib/arm64-v8a/libsurface_util_jni.so                         type=0      4896 /      4896
  lib/armeabi-v7a/libandroidx.graphics.path.so                 type=0      7252 /      7252
  lib/armeabi-v7a/libdatastore_shared_counter.so               type=0      5864 /      5864
  lib/armeabi-v7a/libimage_processing_util_jni.so              type=0     24448 /     24448
  lib/armeabi-v7a/libsurface_util_jni.so                       type=0      3460 /      3460
fonts:
```

| part | status |
|---|---|
| abstart-1 | COMPLETE; part=abstart-1 api=33 gpu=swangle_indirect emulator-step=success |
| abstart-2 | COMPLETE; part=abstart-2 api=33 gpu=swangle_indirect emulator-step=success |
| abstart-3 | COMPLETE; part=abstart-3 api=33 gpu=swangle_indirect emulator-step=success |
| fonts | COMPLETE; part=fonts api=33 gpu=swangle_indirect emulator-step=success |
| func14 | COMPLETE; part=func14 api=33 gpu=swangle_indirect emulator-step=success |
| homey | COMPLETE; part=homey api=33 gpu=swangle_indirect emulator-step=success |
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
| sem | COMPLETE; part=sem api=33 gpu=swangle_indirect emulator-step=success |
| widget | COMPLETE; part=widget api=33 gpu=swangle_indirect emulator-step=success |
| widgetupd | COMPLETE; part=widgetupd api=33 gpu=swangle_indirect emulator-step=success |

## abstart-1

```
part abstart-1; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931;  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 3947 ms
new-1 INDEX-READY #1 catalogue results after 3376 ms
new-1 INDEX-READY #2 catalogue results after 3176 ms
new-1 INDEX-READY #3 catalogue results after 3919 ms
START cold new-1         n=10 min=1387ms median=2198ms max=3812ms  all=[2993, 3089, 3812, 2206, 2190, 2080, 3003, 1387, 1822, 1854]  states=['COLD']
START warm new-1         n=10 min=317ms median=412ms max=860ms  all=[703, 317, 429, 396, 362, 570, 567, 374, 860, 378]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2731 ms
base-1 INDEX-READY #1 catalogue results after 2661 ms
base-1 INDEX-READY #2 catalogue results after 2927 ms
base-1 INDEX-READY #3 catalogue results after 2733 ms
START cold base-1        n=10 min=1265ms median=1572ms max=2156ms  all=[1265, 1544, 1600, 1423, 1695, 1652, 1419, 2156, 1527, 1683]  states=['COLD']
START warm base-1        n=10 min=342ms median=452ms max=604ms  all=[439, 570, 458, 561, 604, 513, 447, 377, 342, 397]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2679 ms
base-2 INDEX-READY #1 catalogue results after 2648 ms
base-2 INDEX-READY #2 catalogue results after 2630 ms
base-2 INDEX-READY #3 catalogue results after 2961 ms
START cold base-2        n=10 min=1273ms median=1455ms max=1969ms  all=[1478, 1432, 1273, 1490, 1969, 1646, 1337, 1415, 1300, 1483]  states=['COLD']
START warm base-2        n=10 min=337ms median=408ms max=733ms  all=[733, 337, 363, 347, 520, 714, 374, 677, 442, 372]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931;  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2608 ms
new-2 INDEX-READY #1 catalogue results after 2576 ms
new-2 INDEX-READY #2 catalogue results after 2609 ms
new-2 INDEX-READY #3 catalogue results after 2624 ms
START cold new-2         n=10 min=1420ms median=1560ms max=1882ms  all=[1814, 1551, 1882, 1420, 1451, 1763, 1511, 1553, 1792, 1567]  states=['COLD']
START warm new-2         n=10 min=301ms median=498ms max=669ms  all=[669, 585, 556, 364, 515, 482, 301, 629, 416, 468]  states=['HOT']
## AB totals
START cold NEW 1.2.28    n=20 min=1387ms median=1818ms max=3812ms  all=[2993, 3089, 3812, 2206, 2190, 2080, 3003, 1387, 1822, 1854, 1814, 1551, 1882, 1420, 1451, 1763, 1511, 1553, 1792, 1567]  states=['COLD']
START cold BASE 1.2.27   n=20 min=1265ms median=1486ms max=2156ms  all=[1265, 1544, 1600, 1423, 1695, 1652, 1419, 2156, 1527, 1683, 1478, 1432, 1273, 1490, 1969, 1646, 1337, 1415, 1300, 1483]  states=['COLD']
START warm NEW 1.2.28    n=20 min=301ms median=475ms max=860ms  all=[703, 317, 429, 396, 362, 570, 567, 374, 860, 378, 669, 585, 556, 364, 515, 482, 301, 629, 416, 468]  states=['HOT']
START warm BASE 1.2.27   n=20 min=337ms median=444ms max=733ms  all=[439, 570, 458, 561, 604, 513, 447, 377, 342, 397, 733, 337, 363, 347, 520, 714, 374, 677, 442, 372]  states=['HOT']
START catalogue first-install new n=2 min=2608ms median=3278ms max=3947ms  all=[3947, 2608]  states=[]
START catalogue index-ready new n=6 min=2576ms median=2900ms max=3919ms  all=[3376, 3176, 3919, 2576, 2609, 2624]  states=[]
START catalogue first-install base n=2 min=2679ms median=2705ms max=2731ms  all=[2731, 2679]  states=[]
START catalogue index-ready base n=6 min=2630ms median=2697ms max=2961ms  all=[2661, 2927, 2733, 2648, 2630, 2961]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['4340', '5019', '5634', '5959', '6470', '6743', '7094', '7285', '7480', '7592', '7728', '7937', '8000', '8055', '8452', '8757', '8895', '9036', '9177', '9236', '9286', '9352', '9422', '9481', '9534', '9581', '9628', '9674', '10048', '10316', '10451', '10588', '10731', '10791', '10838', '10888', '10934', '11009', '11056', '11104', '11167', '11213', '11664', '12027', '12167', '12303', '12447', '12509', '12556', '12605', '12653', '12702', '12750', '12798', '12846', '12895']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 67
   skipped-frames events=67 max=96 total=2909
== Davey: 66
   davey events=66 max=2258ms median=910ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 3366 ms
base-1 INDEX-READY #1 catalogue results after 3546 ms
base-1 INDEX-READY #2 catalogue results after 3277 ms
base-1 INDEX-READY #3 catalogue results after 3066 ms
START cold base-1        n=10 min=1299ms median=1785ms max=2839ms  all=[2831, 2064, 1760, 1981, 2839, 1497, 1654, 1643, 1299, 1810]  states=['COLD']
START warm base-1        n=10 min=344ms median=468ms max=621ms  all=[554, 429, 621, 563, 344, 420, 434, 367, 502, 509]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931;  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2592 ms
new-1 INDEX-READY #1 catalogue results after 2687 ms
new-1 INDEX-READY #2 catalogue results after 2942 ms
new-1 INDEX-READY #3 catalogue results after 2751 ms
START cold new-1         n=10 min=1516ms median=1686ms max=1825ms  all=[1516, 1714, 1652, 1744, 1704, 1667, 1723, 1608, 1825, 1597]  states=['COLD']
START warm new-1         n=10 min=351ms median=538ms max=735ms  all=[735, 590, 631, 446, 502, 351, 464, 695, 399, 573]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931;  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2525 ms
new-2 INDEX-READY #1 catalogue results after 2549 ms
new-2 INDEX-READY #2 catalogue results after 2618 ms
new-2 INDEX-READY #3 catalogue results after 2704 ms
START cold new-2         n=10 min=1250ms median=1516ms max=1770ms  all=[1250, 1437, 1497, 1544, 1534, 1770, 1545, 1660, 1469, 1447]  states=['COLD']
START warm new-2         n=10 min=247ms median=426ms max=670ms  all=[398, 670, 455, 569, 483, 664, 247, 348, 335, 382]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2801 ms
base-2 INDEX-READY #1 catalogue results after 2605 ms
base-2 INDEX-READY #2 catalogue results after 2726 ms
base-2 INDEX-READY #3 catalogue results after 2565 ms
START cold base-2        n=10 min=1130ms median=1492ms max=2218ms  all=[1458, 1660, 1507, 1821, 1421, 1476, 1130, 2218, 1544, 1411]  states=['COLD']
START warm base-2        n=10 min=263ms median=494ms max=763ms  all=[277, 692, 566, 299, 763, 522, 300, 263, 539, 466]  states=['HOT']
## AB totals
START cold NEW 1.2.28    n=20 min=1250ms median=1602ms max=1825ms  all=[1516, 1714, 1652, 1744, 1704, 1667, 1723, 1608, 1825, 1597, 1250, 1437, 1497, 1544, 1534, 1770, 1545, 1660, 1469, 1447]  states=['COLD']
START cold BASE 1.2.27   n=20 min=1130ms median=1648ms max=2839ms  all=[2831, 2064, 1760, 1981, 2839, 1497, 1654, 1643, 1299, 1810, 1458, 1660, 1507, 1821, 1421, 1476, 1130, 2218, 1544, 1411]  states=['COLD']
START warm NEW 1.2.28    n=20 min=247ms median=474ms max=735ms  all=[735, 590, 631, 446, 502, 351, 464, 695, 399, 573, 398, 670, 455, 569, 483, 664, 247, 348, 335, 382]  states=['HOT']
START warm BASE 1.2.27   n=20 min=263ms median=484ms max=763ms  all=[554, 429, 621, 563, 344, 420, 434, 367, 502, 509, 277, 692, 566, 299, 763, 522, 300, 263, 539, 466]  states=['HOT']
START catalogue first-install new n=2 min=2525ms median=2558ms max=2592ms  all=[2592, 2525]  states=[]
START catalogue index-ready new n=6 min=2549ms median=2696ms max=2942ms  all=[2687, 2942, 2751, 2549, 2618, 2704]  states=[]
START catalogue first-install base n=2 min=2801ms median=3084ms max=3366ms  all=[3366, 2801]  states=[]
START catalogue index-ready base n=6 min=2565ms median=2896ms max=3546ms  all=[3546, 3277, 3066, 2605, 2726, 2565]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5025', '6027', '6595', '6931', '7564', '7678', '7768', '7891', '8099', '8264', '8320', '8389', '8448', '8498', '8917', '9223', '9366', '9510', '9657', '9723', '9776', '9834', '9885', '9936', '10114', '10210', '10263', '10321', '10731', '11009', '11153', '11297', '11457', '11521', '11574', '11626', '11678', '11733', '11785', '11845', '11896', '11948', '12506', '12824', '12969', '13110', '13258', '13323', '13372', '13425', '13477', '13526', '13577', '13628', '13679', '13730']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 54
   skipped-frames events=54 max=111 total=2418
== Davey: 54
   davey events=54 max=2228ms median=794ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-3

```
part abstart-3; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931;  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 4289 ms
new-1 INDEX-READY #1 catalogue results after 3158 ms
new-1 INDEX-READY #2 catalogue results after 3513 ms
new-1 INDEX-READY #3 catalogue results after 3191 ms
START cold new-1         n=10 min=1420ms median=1952ms max=2570ms  all=[2458, 1684, 2570, 2462, 2425, 1912, 1420, 1992, 1528, 1438]  states=['COLD']
START warm new-1         n=10 min=204ms median=503ms max=998ms  all=[648, 418, 469, 537, 773, 309, 674, 204, 998, 388]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2568 ms
base-1 INDEX-READY #1 catalogue results after 2596 ms
base-1 INDEX-READY #2 catalogue results after 2549 ms
base-1 INDEX-READY #3 catalogue results after 2762 ms
START cold base-1        n=10 min=1291ms median=1532ms max=2002ms  all=[1445, 1787, 1876, 1507, 1313, 1291, 2002, 1557, 1331, 1658]  states=['COLD']
START warm base-1        n=10 min=291ms median=518ms max=943ms  all=[657, 350, 705, 531, 943, 429, 423, 291, 704, 504]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2821 ms
base-2 INDEX-READY #1 catalogue results after 2708 ms
base-2 INDEX-READY #2 catalogue results after 2667 ms
base-2 INDEX-READY #3 catalogue results after 2618 ms
START cold base-2        n=10 min=1284ms median=1572ms max=1852ms  all=[1852, 1400, 1776, 1369, 1566, 1578, 1683, 1630, 1352, 1284]  states=['COLD']
START warm base-2        n=10 min=225ms median=442ms max=655ms  all=[318, 524, 386, 402, 481, 225, 523, 454, 429, 655]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931;  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2662 ms
new-2 INDEX-READY #1 catalogue results after 2676 ms
new-2 INDEX-READY #2 catalogue results after 2422 ms
new-2 INDEX-READY #3 catalogue results after 2918 ms
START cold new-2         n=10 min=1226ms median=1483ms max=1767ms  all=[1531, 1767, 1479, 1442, 1226, 1442, 1484, 1590, 1607, 1482]  states=['COLD']
START warm new-2         n=10 min=271ms median=417ms max=700ms  all=[700, 309, 673, 326, 286, 498, 354, 271, 480, 584]  states=['HOT']
## AB totals
START cold NEW 1.2.28    n=20 min=1226ms median=1560ms max=2570ms  all=[2458, 1684, 2570, 2462, 2425, 1912, 1420, 1992, 1528, 1438, 1531, 1767, 1479, 1442, 1226, 1442, 1484, 1590, 1607, 1482]  states=['COLD']
START cold BASE 1.2.27   n=20 min=1284ms median=1562ms max=2002ms  all=[1445, 1787, 1876, 1507, 1313, 1291, 2002, 1557, 1331, 1658, 1852, 1400, 1776, 1369, 1566, 1578, 1683, 1630, 1352, 1284]  states=['COLD']
START warm NEW 1.2.28    n=20 min=204ms median=474ms max=998ms  all=[648, 418, 469, 537, 773, 309, 674, 204, 998, 388, 700, 309, 673, 326, 286, 498, 354, 271, 480, 584]  states=['HOT']
START warm BASE 1.2.27   n=20 min=225ms median=468ms max=943ms  all=[657, 350, 705, 531, 943, 429, 423, 291, 704, 504, 318, 524, 386, 402, 481, 225, 523, 454, 429, 655]  states=['HOT']
START catalogue first-install new n=2 min=2662ms median=3476ms max=4289ms  all=[4289, 2662]  states=[]
START catalogue index-ready new n=6 min=2422ms median=3038ms max=3513ms  all=[3158, 3513, 3191, 2676, 2422, 2918]  states=[]
START catalogue first-install base n=2 min=2568ms median=2694ms max=2821ms  all=[2568, 2821]  states=[]
START catalogue index-ready base n=6 min=2549ms median=2642ms max=2762ms  all=[2596, 2549, 2762, 2708, 2667, 2618]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5204', '6132', '6685', '7055', '7594', '7761', '7905', '8023', '8196', '8257', '8314', '8362', '8427', '8483', '8879', '9146', '9282', '9422', '9561', '9624', '9676', '9723', '9769', '9819', '10011', '10072', '10125', '10173', '10540', '10794', '10945', '11084', '11225', '11285', '11334', '11382', '11428', '11476', '11522', '11571', '11618', '11665', '12184', '12493', '12635', '12775', '12919', '12978', '13024', '13074', '13121', '13170', '13217', '13266', '13317', '13365']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 72
   skipped-frames events=72 max=72 total=2878
== Davey: 48
   davey events=48 max=1842ms median=885ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## fonts

```
part fonts; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
ARTICLE with italics/quotes (from news.json + rss.xml): https://chyavorec.org/news/grupata-za-narodni-tantsi-pri-chitalishteto-ff-vrabevo-v-ritmi-2026	Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026	0	4
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3057 ms
## F-bg. Fonts: book pages with Latin names, News, an article with italics and quotes, Settings, №, ›
FONTS bg book 399555: text="Sanktus" text="Тойн, Саймън" text="Налична" text="Наличност към 28.10.2026" ; chars: –×1 “×2 „×2 
FONTS bg book 399637: text="Tour de Армения : Една обиколка на Армения с велосипед" text="Юреджиян, Рафи" text="Налична" text="Наличност към 28.10.2026" ; chars: –×1 “×2 „×2 
FONTS bg book 399510: text="Изкуството на щастливия живот на работното място" text="Тензин Гяцо, Далай Лама XIV и др." text="Налична" text="Наличност към 28.10.20; chars: –×1 “×2 „×2 
FONTS bg book 399580: text="Завинаги само твоя" text="О`Нийл, Луис" text="Налична" text="Наличност към 28.10.2026" ; chars: –×1 “×2 „×2 
FONTS bg catalogue 'Sanktus': 1 result(s)
FONTS bg news: chars: –×3 “×3 „×3 
FONTS bg article: content-desc="Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026" text="СЪБИТИЕ  ·  25 юли 2026" text="Групата за народни та; chars: –×3 “×6 „×6 
FONTS bg article page 2 chars: –×3 “×6 „×6 
FONTS bg article page 3 chars: –×3 “×6 „×6 
FONTS bg article page 4 chars: –×3 “×6 „×6 
FONTS bg settings chars: —×1 
FONTS bg settings (scrolled) chars: —×2 
FONTS bg scan result chars: №×1 
FONTS bg privacy policy: '›' on screen after 1 pages; chars: –×1 —×3 “×6 „×6 ›×2 
FONTS bg contacts chars: –×6 “×1 „×1 
## F-en. Fonts: book pages with Latin names, News, an article with italics and quotes, Settings, №, ›
FONTS en book 399555: text="Sanktus" text="Тойн, Саймън" text="Available" text="Availability as of Oct 28, 2026" ; chars: –×1 “×2 
FONTS en book 399637: text="Tour de Армения : Една обиколка на Армения с велосипед" text="Юреджиян, Рафи" text="Available" text="Availability as of Oct 28, 2026" ; chars: –×1 “×2 
FONTS en book 399510: text="Изкуството на щастливия живот на работното място" text="Тензин Гяцо, Далай Лама XIV и др." text="Available" text="Availability as of O; chars: –×1 “×2 
FONTS en book 399580: text="Завинаги само твоя" text="О`Нийл, Луис" text="Available" text="Availability as of Oct 28, 2026" ; chars: –×1 “×2 
FONTS en catalogue 'Sanktus': 1 result(s)
FONTS en news: chars: –×3 “×3 „×3 
FONTS en article: content-desc="Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026" text="СЪБИТИЕ  ·  July 25, 2026" text="Групата за народни ; chars: –×3 “×6 „×6 
FONTS en article page 2 chars: –×3 “×6 „×6 
FONTS en article page 3 chars: –×3 “×6 „×6 
FONTS en article page 4 chars: –×3 “×6 „×6 
FONTS en settings chars: —×1 
FONTS en settings (scrolled) chars: —×2 
FONTS en scan result chars: 
FONTS en privacy policy: '›' on screen after 1 pages; chars: –×1 —×2 “×6 ›×2 
FONTS en contacts chars: –×6 “×1 „×1 
## COMPLETE
## logcat findings
app pids seen: ['5333', '7073', '8832']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=52 total=164
== Davey: 7
   davey events=7 max=1300ms median=874ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func14

```
part func14; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2601 ms
## H. Home SearchBar (Material 3): tap, '12', open a result, back, back gesture
SEARCHBAR collapsed: field @ 503 393; scanner icon yes
SEARCHBAR tapped: expanded=yes query-12=no hint=yes tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR '12': text="12" content-desc="Лента за търсене" content-desc="Назад" content-desc="Изчисти" text="Всички" text="Книги" text="Новини" text="Събития" text="Страници" text="Книги" text="Спомен за здрача" text="Веска Пенчева" text="1972 · графично издание · за възрастни" text="Не е на рафта" text="12 -те вятъ
SEARCHBAR result opened: back=yes bar-gone=yes; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" text="Искам да прочета" 
SEARCHBAR Back from the result: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR Back key on the expanded bar: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
## H2. Back GESTURE from the expanded bar (gesture navigation; API 33 — the predictive animation needs Android 14+)
gesture nav:  navigation_mode=2
SEARCHBAR expanded again: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=true
SEARCHBAR IME hidden: expanded=yes query-12=yes hint=no tabbar=no app-in-front=yes IME mInputShown=false
SEARCHBAR after back gesture #1: expanded=no query-12=yes hint=no tabbar=yes app-in-front=yes IME mInputShown=false
SEARCHBAR back gesture on the collapsed Home: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
## N. Native libraries (compressed in the APK): APK entries, installed files, loaded libraries; scanner with CAMERA denied
APK x86_64 libs: libandroidx.graphics.path.so type=deflated 4614/10760; libdatastore_shared_counter.so type=deflated 2812/7424; libimage_processing_util_jni.so type=deflated 22802/49496; libsurface_util_jni.so type=deflated 1794/4912
installed: /data/app/~~lBPdsMhPORLLxEZcSzZehA==/org.chyavorec.app-W3_m5u0RSzmma1zYRwXX2w==; lib/x86_64: total 96 libandroidx.graphics.path.so(10760) libdatastore_shared_counter.so(7424) libimage_processing_util_jni.so(49496) libsurface_util_jni.so(4912) 
package flags:  legacyNativeLibraryDir=/data/app/~~lBPdsMhPORLLxEZcSzZehA==/org.chyavorec.app-W3_m5u0RSzmma1zYRwXX2w==/lib  primaryCpuAbi=x86_64  pkgFlags=[ HAS_CODE ALLOW_CLEAR_USER_DATA ] 
## F6. Scanner (CAMERA revoked): manual panel, Back key
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
manual panel: panel OPEN; IME mInputShown=true; scanner visible; app in front yes
Back key #1: panel OPEN; IME mInputShown=false; scanner visible; app in front yes
Back key #2: panel closed; IME mInputShown=false; scanner visible; app in front yes
SCAN result: content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Налична" text="инв. № 399560" text="Затвори" text="Отвори книгата" 
SCAN → book: opened
loaded app .so (pid 8005): 
UnsatisfiedLinkError / dlopen failed in logcat so far: 2
## CM. Sign-in screen; the saved-credential query (Credential Manager) on opening
autofill service: 'com.google.android.gms/.autofill.service.AutofillService'; credential service: 'null'; GMS: versionName=23.18.18(190800-535401451)
LOGIN +1s: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN +4s: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN +8s: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството.
LOGIN logcat (credentials/crash): 10-30 14:58:06.319  9163  9163 D AndroidRuntime: >>>>>> START com.android.internal.os.RuntimeInit uid 0 <<<<<< 10-30 14:58:06.321  9163  9163 I AndroidRuntime: Using default boot image 10-30 14:58:06.321  9163  9163 I AndroidRuntime: Leaving lock profiling enabled 10-30 14:58:06.371  9163  9163 D AndroidRuntime: Calling main entry com.android.commands.uiautomator.Launcher 10-30 14:58:07.391  9163  9163 D AndroidRuntime: Shutting down VM 10-30 14:58:08.398  6676  6676 D BoundBrokerSvc: onBind: Intent { act=com.google.android.gms.auth.api.identity.service.signin.START dat=chimera-action:/... cmp=com.google.android.gms/.chimera.GmsApiService } 10-30 14:58:08.398  6676  6676 D BoundBrokerSvc: Loading bound service for intent: Intent { act=com.google.android.gms.auth.api.identity.service.signin.START dat=chimera-action:/... cmp=com.google.android.gms/.chimera.Gm 10-30 14:58:09.333  9192  9192 D AndroidRuntime: >>>>>> START com.android.internal.os.RuntimeInit uid 0 <<<<<< 
LOGIN card field typed (not submitted: no reader test account): 1 field(s) show it; app in front yes
LOGIN after Back: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="12" 
## C. Notification channel groups (fresh install of v1.2.28)
groups: group_app=Приложението; group_my_library=Моята библиотека; group_centre=Читалището
channels: events(Събития)->?; messages(Съобщения от читалището)->?; updates(Обновления)->?; news(Новини)->?; loans(Срокове за връщане)->?; library(Библиотека)->?; messages_important(Важни съобщения)->?
cmd notification list_channels: Unknown command: list_channels 
SYSTEM notification settings: fg com.android.settings/com.android.settings.Settings$AppNotificationSettingsActivity; content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Chitalishte Yavorets" text="All Chitalishte Yavorets notifications" text="Приложението" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения от читалището" content-desc="Съобщения от читалището" content-desc="Важни съобщения" 
SYSTEM notification settings (scrolled): content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения от читалището" content-desc="Съобщения от читалището" text="Важни съобщения" content-desc="Важни съобщения" text="Новини" content-desc="Новини" text="Моята библиотека" text="Библиотека" content-desc="Библиотека" text="Срокове за връщане" content-desc="Срокове за връщане" text="Allow notification dot" 
## S. App shortcuts: long-press the icon; dynamic 'Искам да прочета' and the last public book
SHORTCUTS before: id=last-book, flags=0x85 [DynIc-rStr] id=catalog, flags=0x1a4 [ImManIc-rStr] id=scan, flags=0x1a4 [ImManIc-rStr] id=card, flags=0x1a4 [ImManIc-rStr] id=events, flags=0x1a4 [ImManIc-rStr] 
SHORTCUTS before labels: shortLabel=12 -те вятъра|longLabel=12 -те вятъра — Гарфилд|shortLabel=Catalogue|longLabel=Search the catalogue|shortLabel=Scan|longLabel=Scan a book barcode|shortLabel=Library card|longLabel=My library card|shortLabel=Events|longLabel=Upcoming events|
SHORTCUTS popup 01-before: content-desc="App info" content-desc="Pause app" text="Widgets" text="Catalogue" content-desc="Search the catalogue" text="My library card" content-desc="My library card" text="Upcoming events" content-desc="Upcoming events" text="12 -те вятъра" content-desc="12 -те вятъра — Гарфилд, Джудит и др." 
public book opened: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" 
SHORTCUTS after: id=last-book, flags=0x85 [DynIc-rStr] id=catalog, flags=0x1a4 [ImManIc-rStr] id=want-to-read, flags=0x85 [DynIc-rStr] id=scan, flags=0x1a4 [ImManIc-rStr] id=card, flags=0x1a4 [ImManIc-rStr] id=events, flags=0x1a4 [ImManIc-rStr] 
SHORTCUTS after labels: shortLabel=12 -те вятъра|longLabel=12 -те вятъра — Гарфилд|shortLabel=Catalogue|longLabel=Search the catalogue|shortLabel=Искам да прочета|longLabel=Списък „Искам да прочета“|shortLabel=Scan|longLabel=Scan a book barcode|shortLabel=Library card|longLabel=My library card|shortLabel=Events|longLabel=Upcoming events|
SHORTCUTS popup 02-after: content-desc="App info" content-desc="Pause app" text="Widgets" text="Catalogue" content-desc="Search the catalogue" text="My library card" content-desc="My library card" text="Искам да прочета" content-desc="Списък „Искам да прочета“" text="12 -те вятъра" content-desc="12 -те вятъра — Гарфилд, Джудит и др." 
SHORTCUT '12 -те вятъра' → fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 28.10.2026" 
## COMPLETE
## logcat findings
app pids seen: ['5836', '7341', '8005']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=36 total=36
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## homey

```
part homey; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
host tools: /usr/bin/ffmpeg
PIL 12.1.1
## Y-new. Home: y of „Нови постъпления“ / „За теб“ during the first seconds of a cold start (tall 1080x5600 screen)
installed new: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931;  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue after 3301 ms
HOMEY-FRAMES new-cached-1: 58 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=781 EVENTS=435
HOMEY-FRAMES new-cached-1 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F24,E- | 0.5s N-,F24,E- | 0.6s N-,F20,E- | 0.7s N-,F20,E- | 0.8s N-,F32,E- | 0.9s N-,F32,E- | 1.0s N-,F32,E- | 1.1s N-,F32,E- | 1.2s N-,F32,E- | 1.3s N-,F32,E- | 1.4s N-,F120,E432 | 1.5s N-,F120,E432 | 1.6s N-,F120,E432 | 1.7s N-,F120,E432 | 1.8s N-,F400,E432 | 1.9s N-,F400,E432 | 2.0s N-,F400,E432 | 2.1s N-,F400,E432 | 2.2s N-,F400,E432 | 2.3s N-,F400,E432 | 2.4s N-,F400,E432 | 2.5s N-,F400,E432 | 2.6s N-,F400,E432 | 2.7s N-,F400,E432 | 2.8s N570,F403,E435 | 2.9s N570,F403,E435 | 3.0s N570,F403,E435 | 3.1s N570,F403,E435 | 3.2s N570,F403,E435 | 3.3s N570,F781,E435 | 3.4s N570,F781,E435 | 3.5s N570,F781,E435 | 3.6s N570,F781,E435 | 3.7s N570,F781,E435 | 3.8s N570,F781,E435 | 3.9s N570,F781,E435 | 4.0s N570,F781,E435 | 4.1s N570,F781,E435 | 4.2s N570,F781,E435 | 4.3s N570,F781,E435 | 4.4s N570,F781,E435 | 4.5s N570,F781,E435 | 4.6s N570,F781,E435 | 4.7s N570,F781,E435
HOMEY-FRAMES new-cached-1 summary: NEW: first at 2.8s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.4s y=24, final y=781, range=761px (x4 on screen), jumps=6; EVENTS: first at 1.4s y=432, final y=435, range=3px (x4 on screen), jumps=1
HOMEY-FRAMES new-cached-2: 71 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=781 EVENTS=435
HOMEY-FRAMES new-cached-2 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F-,E- | 0.7s N-,F-,E- | 0.8s N-,F21,E- | 0.9s N-,F21,E- | 1.0s N-,F21,E- | 1.1s N-,F21,E- | 1.2s N-,F21,E- | 1.3s N-,F21,E- | 1.4s N-,F21,E- | 1.5s N-,F24,E- | 1.6s N-,F24,E- | 1.7s N-,F24,E- | 1.8s N-,F24,E- | 1.9s N-,F24,E- | 2.0s N-,F24,E- | 2.1s N-,F24,E- | 2.2s N-,F24,E- | 2.3s N-,F120,E432 | 2.4s N-,F120,E432 | 2.5s N-,F120,E432 | 2.6s N-,F120,E432 | 2.7s N-,F120,E432 | 2.8s N-,F624,E432 | 2.9s N-,F624,E432 | 3.0s N-,F624,E432 | 3.1s N-,F624,E432 | 3.2s N-,F624,E432 | 3.3s N-,F624,E432 | 3.4s N-,F624,E432 | 3.5s N-,F624,E432 | 3.6s N-,F624,E432 | 3.7s N-,F624,E432 | 3.8s N-,F624,E432 | 3.9s N-,F624,E432 | 4.0s N570,F768,E435 | 4.1s N570,F768,E435 | 4.2s N570,F768,E435 | 4.3s N570,F768,E435 | 4.4s N570,F768,E435 | 4.5s N570,F781,E435 | 4.6s N570,F781,E435 | 4.7s N570,F781,E435 | 4.8s N570,F781,E435 | 4.9s N570,F781,E435 | 5.0s N570,F781,E435 | 5.1s N570,F781,E435 | 5.2s N570,F781,E435 | 5.3s N570,F781,E435 | 5.4s N570,F781,E435 | 5.5s N570,F781,E435 | 5.6s N570,F781,E435 | 5.7s N570,F781,E435 | 5.8s N570,F781,E435 | 5.9s N570,F781,E435 | 6.0s N570,F781,E435
HOMEY-FRAMES new-cached-2 summary: NEW: first at 4.0s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.8s y=21, final y=781, range=760px (x4 on screen), jumps=5; EVENTS: first at 2.3s y=432, final y=435, range=3px (x4 on screen), jumps=1
HOMEY-FRAMES new-cached-3: 57 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=781 EVENTS=435
HOMEY-FRAMES new-cached-3 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F-,E- | 0.7s N-,F-,E- | 0.8s N-,F-,E- | 0.9s N-,F20,E- | 1.0s N-,F20,E- | 1.1s N-,F20,E- | 1.2s N-,F20,E- | 1.3s N-,F32,E- | 1.4s N-,F32,E- | 1.5s N-,F32,E- | 1.6s N-,F32,E- | 1.7s N-,F32,E- | 1.8s N-,F32,E- | 1.9s N-,F120,E432 | 2.0s N-,F120,E432 | 2.1s N-,F120,E432 | 2.2s N-,F400,E432 | 2.3s N-,F400,E432 | 2.4s N-,F400,E432 | 2.5s N-,F400,E432 | 2.6s N-,F400,E432 | 2.7s N-,F400,E432 | 2.8s N-,F400,E432 | 2.9s N-,F400,E432 | 3.0s N-,F400,E432 | 3.1s N-,F400,E432 | 3.2s N-,F400,E432 | 3.3s N-,F400,E432 | 3.4s N570,F403,E435 | 3.5s N570,F403,E435 | 3.6s N570,F403,E435 | 3.7s N570,F403,E435 | 3.8s N570,F403,E435 | 3.9s N570,F781,E435 | 4.0s N570,F781,E435 | 4.1s N570,F781,E435 | 4.2s N570,F781,E435 | 4.3s N570,F781,E435 | 4.4s N570,F781,E435 | 4.5s N570,F781,E435 | 4.6s N570,F781,E435
HOMEY-FRAMES new-cached-3 summary: NEW: first at 3.4s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.9s y=20, final y=781, range=761px (x4 on screen), jumps=5; EVENTS: first at 1.9s y=432, final y=435, range=3px (x4 on screen), jumps=1
HOMEY-FRAMES new-clearcache-1: 65 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=781 EVENTS=435
HOMEY-FRAMES new-clearcache-1 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F20,E- | 0.6s N-,F20,E- | 0.7s N-,F20,E- | 0.8s N-,F20,E- | 0.9s N-,F20,E- | 1.0s N-,F20,E- | 1.1s N-,F20,E- | 1.2s N-,F32,E- | 1.3s N-,F32,E- | 1.4s N-,F32,E- | 1.5s N-,F32,E- | 1.6s N-,F32,E- | 1.7s N-,F120,E432 | 1.8s N-,F120,E432 | 1.9s N-,F120,E432 | 2.0s N-,F400,E432 | 2.1s N-,F400,E432 | 2.2s N-,F400,E432 | 2.3s N-,F400,E432 | 2.4s N-,F400,E432 | 2.5s N-,F400,E432 | 2.6s N-,F400,E432 | 2.7s N-,F400,E432 | 2.8s N-,F400,E432 | 2.9s N-,F400,E432 | 3.0s N-,F400,E432 | 3.1s N-,F400,E432 | 3.2s N570,F781,E435 | 3.3s N570,F781,E435 | 3.4s N570,F781,E435 | 3.5s N570,F781,E435 | 3.6s N570,F781,E435 | 3.7s N570,F781,E435 | 3.8s N570,F781,E435 | 3.9s N570,F781,E435 | 4.0s N570,F781,E435 | 4.1s N570,F781,E435 | 4.2s N570,F781,E435 | 4.3s N570,F781,E435 | 4.4s N570,F781,E435 | 4.5s N570,F781,E435 | 4.6s N570,F781,E435 | 4.7s N570,F781,E435 | 4.8s N570,F781,E435 | 4.9s N570,F781,E435 | 5.0s N570,F781,E435 | 5.1s N570,F781,E435 | 5.2s N570,F781,E435 | 5.3s N570,F781,E435 | 5.4s N570,F781,E435
HOMEY-FRAMES new-clearcache-1 summary: NEW: first at 3.2s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.5s y=20, final y=781, range=761px (x4 on screen), jumps=4; EVENTS: first at 1.7s y=432, final y=435, range=3px (x4 on screen), jumps=1
HOMEY-FRAMES new-clearcache-2: 51 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=781 EVENTS=435
HOMEY-FRAMES new-clearcache-2 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F31,E- | 0.7s N-,F31,E- | 0.8s N-,F31,E- | 0.9s N-,F31,E- | 1.0s N-,F32,E- | 1.1s N-,F32,E- | 1.2s N-,F32,E- | 1.3s N-,F32,E- | 1.4s N-,F32,E- | 1.5s N-,F120,E432 | 1.6s N-,F120,E432 | 1.7s N-,F120,E432 | 1.8s N-,F400,E432 | 1.9s N-,F400,E432 | 2.0s N-,F400,E432 | 2.1s N-,F400,E432 | 2.2s N-,F400,E432 | 2.3s N-,F400,E432 | 2.4s N-,F400,E432 | 2.5s N-,F400,E432 | 2.6s N-,F400,E432 | 2.7s N570,F403,E435 | 2.8s N570,F403,E435 | 2.9s N570,F403,E435 | 3.0s N570,F403,E435 | 3.1s N570,F403,E435 | 3.2s N570,F403,E435 | 3.3s N570,F403,E435 | 3.4s N570,F403,E435 | 3.5s N570,F781,E435 | 3.6s N570,F781,E435 | 3.7s N570,F781,E435 | 3.8s N570,F781,E435 | 3.9s N570,F781,E435 | 4.0s N570,F781,E435
HOMEY-FRAMES new-clearcache-2 summary: NEW: first at 2.7s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.6s y=31, final y=781, range=750px (x4 on screen), jumps=4; EVENTS: first at 1.5s y=432, final y=435, range=3px (x4 on screen), jumps=1
HOMEY-FRAMES new-clearcache-3: 83 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=781 EVENTS=435
HOMEY-FRAMES new-clearcache-3 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F-,E- | 0.7s N-,F20,E- | 0.8s N-,F20,E- | 0.9s N-,F20,E- | 1.0s N-,F20,E- | 1.1s N-,F20,E- | 1.2s N-,F24,E- | 1.3s N-,F24,E- | 1.4s N-,F24,E- | 1.5s N-,F24,E- | 1.6s N-,F120,E432 | 1.7s N-,F120,E432 | 1.8s N-,F120,E432 | 1.9s N-,F120,E432 | 2.0s N-,F1310,E432 | 2.1s N-,F1310,E432 | 2.2s N-,F1310,E432 | 2.3s N-,F1310,E432 | 2.4s N-,F1310,E432 | 2.5s N-,F1310,E432 | 2.6s N-,F1310,E432 | 2.7s N-,F1310,E432 | 2.8s N570,F1293,E435 | 2.9s N570,F1293,E435 | 3.0s N570,F1293,E435 | 3.1s N570,F1293,E435 | 3.2s N570,F1293,E435 | 3.3s N570,F1293,E435 | 3.4s N570,F1293,E435 | 3.5s N570,F768,E435 | 3.6s N570,F781,E435 | 3.7s N570,F781,E435 | 3.8s N570,F781,E435 | 3.9s N570,F781,E435 | 4.0s N570,F781,E435 | 4.1s N570,F781,E435 | 4.2s N570,F781,E435 | 4.3s N570,F781,E435 | 4.4s N570,F781,E435 | 4.5s N570,F781,E435 | 4.6s N570,F781,E435 | 4.7s N570,F781,E435 | 4.8s N570,F781,E435 | 4.9s N570,F781,E435 | 5.0s N570,F781,E435 | 5.1s N570,F781,E435 | 5.2s N570,F781,E435 | 5.3s N570,F781,E435 | 5.4s N570,F781,E435 | 5.5s N570,F781,E435 | 5.6s N570,F781,E435 | 5.7s N570,F781,E435 | 5.8s N570,F781,E435 | 5.9s N570,F781,E435 | 6.0s N570,F781,E435 | 6.1s N570,F781,E435 | 6.2s N570,F781,E435 | 6.3s N570,F781,E435 | 6.4s N570,F781,E435 | 6.5s N570,F781,E435 | 6.6s N570,F781,E435 | 6.7s N570,F781,E435 | 6.8s N570,F781,E435 | 6.9s N570,F781,E435 | 7.0s N570,F781,E435 | 7.1s N570,F781,E435 | 7.2s N570,F781,E435
HOMEY-FRAMES new-clearcache-3 summary: NEW: first at 2.8s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.7s y=20, final y=781, range=1290px (x4 on screen), jumps=6; EVENTS: first at 1.6s y=432, final y=435, range=3px (x4 on screen), jumps=1
## Y-new. Status bar scrim on Home (scrolled), light and dark
## Y-base. Home: y of „Нови постъпления“ / „За теб“ during the first seconds of a cold start (tall 1080x5600 screen)
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue after 2749 ms
HOMEY-FRAMES base-cached-1: 61 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=780 EVENTS=435
HOMEY-FRAMES base-cached-1 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F-,E- | 0.7s N-,F20,E- | 0.8s N-,F20,E- | 0.9s N-,F20,E- | 1.0s N-,F20,E- | 1.1s N-,F32,E- | 1.2s N-,F32,E- | 1.3s N-,F32,E- | 1.4s N-,F32,E- | 1.5s N-,F32,E- | 1.6s N-,F464,E- | 1.7s N-,F464,E- | 1.8s N-,F464,E- | 1.9s N-,F464,E- | 2.0s N-,F464,E- | 2.1s N-,F464,E- | 2.2s N-,F464,E- | 2.3s N-,F464,E- | 2.4s N-,F464,E- | 2.5s N-,F464,E- | 2.6s N-,F464,E- | 2.7s N-,F464,E- | 2.8s N-,F464,E- | 2.9s N-,F464,E- | 3.0s N-,F464,E- | 3.1s N542,F1262,E407 | 3.2s N542,F1262,E407 | 3.3s N542,F1262,E407 | 3.4s N542,F1262,E407 | 3.5s N542,F774,E407 | 3.6s N542,F774,E407 | 3.7s N542,F774,E407 | 3.8s N542,F774,E407 | 3.9s N542,F774,E407 | 4.0s N542,F752,E407 | 4.1s N542,F752,E407 | 4.2s N542,F752,E407 | 4.3s N542,F752,E407 | 4.4s N542,F752,E407 | 4.5s N542,F752,E407 | 4.6s N542,F752,E407 | 4.7s N542,F752,E407 | 4.8s N542,F752,E407 | 4.9s N542,F752,E407 | 5.0s N570,F780,E435
HOMEY-FRAMES base-cached-1 summary: NEW: first at 3.1s y=542, final y=570, range=28px (x4 on screen), jumps=1; FORYOU: first at 0.7s y=20, final y=780, range=1242px (x4 on screen), jumps=6; EVENTS: first at 3.1s y=407, final y=435, range=28px (x4 on screen), jumps=1
HOMEY-FRAMES base-cached-2: 50 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=780 EVENTS=435
HOMEY-FRAMES base-cached-2 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F32,E- | 0.7s N-,F32,E- | 0.8s N-,F32,E- | 0.9s N-,F32,E- | 1.0s N-,F32,E- | 1.1s N-,F32,E- | 1.2s N-,F32,E- | 1.3s N-,F32,E- | 1.4s N-,F32,E- | 1.5s N-,F32,E- | 1.6s N-,F32,E- | 1.7s N-,F464,E- | 1.8s N-,F464,E- | 1.9s N-,F464,E- | 2.0s N-,F464,E- | 2.1s N-,F464,E- | 2.2s N-,F464,E- | 2.3s N-,F464,E- | 2.4s N-,F464,E- | 2.5s N-,F464,E- | 2.6s N-,F464,E- | 2.7s N-,F464,E- | 2.8s N-,F464,E- | 2.9s N-,F464,E- | 3.0s N542,F1262,E407 | 3.1s N542,F1262,E407 | 3.2s N570,F831,E435 | 3.3s N570,F831,E435 | 3.4s N570,F831,E435 | 3.5s N570,F831,E435 | 3.6s N570,F831,E435 | 3.7s N570,F831,E435 | 3.8s N570,F831,E435 | 3.9s N570,F780,E435
HOMEY-FRAMES base-cached-2 summary: NEW: first at 3.0s y=542, final y=570, range=28px (x4 on screen), jumps=1; FORYOU: first at 0.6s y=32, final y=780, range=1230px (x4 on screen), jumps=4; EVENTS: first at 3.0s y=407, final y=435, range=28px (x4 on screen), jumps=1
HOMEY-FRAMES base-cached-3: 82 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=780 EVENTS=435
HOMEY-FRAMES base-cached-3 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F32,E- | 0.6s N-,F32,E- | 0.7s N-,F22,E- | 0.8s N-,F22,E- | 0.9s N-,F22,E- | 1.0s N-,F22,E- | 1.1s N-,F22,E- | 1.2s N-,F22,E- | 1.3s N-,F22,E- | 1.4s N-,F22,E- | 1.5s N-,F22,E- | 1.6s N-,F32,E- | 1.7s N-,F32,E- | 1.8s N-,F32,E- | 1.9s N-,F32,E- | 2.0s N-,F464,E- | 2.1s N-,F464,E- | 2.2s N-,F464,E- | 2.3s N-,F464,E- | 2.4s N-,F464,E- | 2.5s N-,F464,E- | 2.6s N-,F464,E- | 2.7s N-,F464,E- | 2.8s N-,F464,E- | 2.9s N-,F464,E- | 3.0s N-,F464,E- | 3.1s N-,F464,E- | 3.2s N-,F464,E- | 3.3s N-,F464,E- | 3.4s N570,F780,E435 | 3.5s N570,F780,E435 | 3.6s N570,F780,E435 | 3.7s N570,F780,E435 | 3.8s N570,F780,E435 | 3.9s N570,F780,E435 | 4.0s N570,F780,E435 | 4.1s N570,F780,E435 | 4.2s N570,F780,E435 | 4.3s N570,F780,E435 | 4.4s N570,F780,E435 | 4.5s N570,F780,E435 | 4.6s N570,F780,E435 | 4.7s N570,F780,E435 | 4.8s N570,F780,E435 | 4.9s N570,F780,E435 | 5.0s N570,F780,E435 | 5.1s N570,F780,E435 | 5.2s N570,F780,E435 | 5.3s N570,F780,E435 | 5.4s N570,F780,E435 | 5.5s N570,F780,E435 | 5.6s N570,F780,E435 | 5.7s N570,F780,E435 | 5.8s N570,F780,E435 | 5.9s N570,F780,E435 | 6.0s N570,F780,E435 | 6.1s N570,F780,E435 | 6.2s N570,F780,E435 | 6.3s N570,F780,E435 | 6.4s N570,F780,E435 | 6.5s N570,F780,E435 | 6.6s N570,F780,E435 | 6.7s N570,F780,E435 | 6.8s N570,F780,E435 | 6.9s N570,F780,E435 | 7.0s N570,F780,E435 | 7.1s N570,F780,E435
HOMEY-FRAMES base-cached-3 summary: NEW: first at 3.4s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.5s y=32, final y=780, range=758px (x4 on screen), jumps=4; EVENTS: first at 3.4s y=435, final y=435, range=0px (x4 on screen), jumps=0
HOMEY-FRAMES base-clearcache-1: 55 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=780 EVENTS=435
HOMEY-FRAMES base-clearcache-1 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F20,E- | 0.7s N-,F20,E- | 0.8s N-,F20,E- | 0.9s N-,F20,E- | 1.0s N-,F20,E- | 1.1s N-,F20,E- | 1.2s N-,F20,E- | 1.3s N-,F32,E- | 1.4s N-,F32,E- | 1.5s N-,F32,E- | 1.6s N-,F32,E- | 1.7s N-,F32,E- | 1.8s N-,F464,E- | 1.9s N-,F464,E- | 2.0s N-,F464,E- | 2.1s N-,F464,E- | 2.2s N-,F464,E- | 2.3s N-,F464,E- | 2.4s N-,F464,E- | 2.5s N-,F464,E- | 2.6s N-,F464,E- | 2.7s N-,F464,E- | 2.8s N-,F464,E- | 2.9s N-,F464,E- | 3.0s N-,F464,E- | 3.1s N-,F464,E- | 3.2s N-,F464,E- | 3.3s N570,F780,E435 | 3.4s N570,F780,E435 | 3.5s N570,F780,E435 | 3.6s N570,F780,E435 | 3.7s N570,F780,E435 | 3.8s N570,F780,E435 | 3.9s N570,F780,E435 | 4.0s N570,F780,E435 | 4.1s N570,F780,E435 | 4.2s N570,F780,E435 | 4.3s N570,F780,E435 | 4.4s N570,F780,E435
HOMEY-FRAMES base-clearcache-1 summary: NEW: first at 3.3s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.6s y=20, final y=780, range=760px (x4 on screen), jumps=3; EVENTS: first at 3.3s y=435, final y=435, range=0px (x4 on screen), jumps=0
HOMEY-FRAMES base-clearcache-2: 69 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=780 EVENTS=435
HOMEY-FRAMES base-clearcache-2 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F-,E- | 0.5s N-,F-,E- | 0.6s N-,F-,E- | 0.7s N-,F-,E- | 0.8s N-,F-,E629 | 0.9s N-,F-,E629 | 1.0s N-,F-,E629 | 1.1s N-,F-,E629 | 1.2s N-,F-,E1370 | 1.3s N-,F-,E1370 | 1.4s N-,F-,E1370 | 1.5s N-,F-,E1370 | 1.6s N-,F-,E1370 | 1.7s N-,F-,E1370 | 1.8s N-,F-,E1370 | 1.9s N-,F-,E1370 | 2.0s N-,F-,E371 | 2.1s N-,F-,E371 | 2.2s N-,F-,E371 | 2.3s N-,F-,E371 | 2.4s N-,F-,E371 | 2.5s N-,F-,E371 | 2.6s N-,F-,E371 | 2.7s N-,F-,E371 | 2.8s N-,F-,E371 | 2.9s N-,F-,E371 | 3.0s N-,F-,E371 | 3.1s N-,F-,E371 | 3.2s N570,F578,E435 | 3.3s N570,F578,E435 | 3.4s N570,F578,E435 | 3.5s N570,F578,E435 | 3.6s N570,F578,E435 | 3.7s N570,F578,E435 | 3.8s N570,F578,E435 | 3.9s N570,F578,E435 | 4.0s N570,F578,E435 | 4.1s N570,F780,E435 | 4.2s N570,F780,E435 | 4.3s N570,F780,E435 | 4.4s N570,F780,E435 | 4.5s N570,F780,E435 | 4.6s N570,F780,E435 | 4.7s N570,F780,E435 | 4.8s N570,F780,E435 | 4.9s N570,F780,E435 | 5.0s N570,F780,E435 | 5.1s N570,F780,E435 | 5.2s N570,F780,E435 | 5.3s N570,F780,E435 | 5.4s N570,F780,E435 | 5.5s N570,F780,E435 | 5.6s N570,F780,E435 | 5.7s N570,F780,E435 | 5.8s N570,F780,E435
HOMEY-FRAMES base-clearcache-2 summary: NEW: first at 3.2s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 3.2s y=578, final y=780, range=202px (x4 on screen), jumps=1; EVENTS: first at 0.8s y=629, final y=435, range=999px (x4 on screen), jumps=3
HOMEY-FRAMES base-clearcache-3: 104 frames @ 10 fps; final y (frame px, x0.25 of screen): NEW=570 FORYOU=780 EVENTS=435
HOMEY-FRAMES base-clearcache-3 y by time after am start: 0.0s N-,F-,E- | 0.1s N-,F-,E- | 0.2s N-,F-,E- | 0.3s N-,F-,E- | 0.4s N-,F32,E- | 0.5s N-,F32,E- | 0.6s N-,F22,E- | 0.7s N-,F22,E- | 0.8s N-,F22,E- | 0.9s N-,F22,E- | 1.0s N-,F31,E- | 1.1s N-,F31,E- | 1.2s N-,F31,E- | 1.3s N-,F31,E- | 1.4s N-,F116,E- | 1.5s N-,F116,E- | 1.6s N-,F400,E- | 1.7s N-,F400,E- | 1.8s N-,F400,E- | 1.9s N-,F400,E- | 2.0s N-,F400,E- | 2.1s N-,F400,E- | 2.2s N-,F400,E- | 2.3s N-,F400,E- | 2.4s N-,F400,E- | 2.5s N-,F400,E- | 2.6s N-,F400,E- | 2.7s N570,F772,E435 | 2.8s N570,F772,E435 | 2.9s N570,F772,E435 | 3.0s N570,F772,E435 | 3.1s N570,F772,E435 | 3.2s N570,F780,E435 | 3.3s N570,F780,E435 | 3.4s N570,F780,E435 | 3.5s N570,F780,E435 | 3.6s N570,F780,E435 | 3.7s N570,F780,E435 | 3.8s N570,F780,E435 | 3.9s N570,F780,E435 | 4.0s N570,F780,E435 | 4.1s N570,F780,E435 | 4.2s N570,F780,E435 | 4.3s N570,F780,E435 | 4.4s N570,F780,E435 | 4.5s N570,F780,E435 | 4.6s N570,F780,E435 | 4.7s N570,F780,E435 | 4.8s N570,F780,E435 | 4.9s N570,F780,E435 | 5.0s N570,F780,E435 | 5.1s N570,F780,E435 | 5.2s N570,F780,E435 | 5.3s N570,F780,E435 | 5.4s N570,F780,E435 | 5.5s N570,F780,E435 | 5.6s N570,F780,E435 | 5.7s N570,F780,E435 | 5.8s N570,F780,E435 | 5.9s N570,F780,E435 | 6.0s N570,F780,E435 | 6.1s N570,F780,E435 | 6.2s N570,F780,E435 | 6.3s N570,F780,E435 | 6.4s N570,F780,E435 | 6.5s N570,F780,E435 | 6.6s N570,F780,E435 | 6.7s N570,F780,E435 | 6.8s N570,F780,E435 | 6.9s N570,F780,E435 | 7.0s N570,F780,E435 | 7.1s N570,F780,E435 | 7.2s N570,F780,E435 | 7.3s N570,F780,E435 | 7.4s N570,F780,E435 | 7.5s N570,F780,E435 | 7.6s N570,F780,E435 | 7.7s N570,F780,E435 | 7.8s N570,F780,E435 | 7.9s N570,F780,E435 | 8.0s N570,F780,E435 | 8.1s N570,F780,E435 | 8.2s N570,F780,E435 | 8.3s N570,F780,E435 | 8.4s N570,F780,E435 | 8.5s N570,F780,E435 | 8.6s N570,F780,E435 | 8.7s N570,F780,E435 | 8.8s N570,F780,E435 | 8.9s N570,F780,E435 | 9.0s N570,F780,E435 | 9.1s N570,F780,E435 | 9.2s N570,F780,E435 | 9.3s N570,F780,E435
HOMEY-FRAMES base-clearcache-3 summary: NEW: first at 2.7s y=570, final y=570, range=0px (x4 on screen), jumps=0; FORYOU: first at 0.4s y=32, final y=780, range=758px (x4 on screen), jumps=6; EVENTS: first at 2.7s y=435, final y=435, range=0px (x4 on screen), jumps=0
## Y-base. Status bar scrim on Home (scrolled), light and dark
## COMPLETE
## logcat findings
app pids seen: ['5988', '7247', '7790', '8126', '8284', '8456', '8614', '8830', '8978', '9249', '9674', '9919', '10066', '10209', '10364', '10550', '10710', '10868']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 32
   skipped-frames events=32 max=99 total=1311
== Davey: 85
   davey events=85 max=2166ms median=916ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look1

```
part look1; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3738 ms
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
app pids seen: ['4359', '6338', '8456', '9910', '11257']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=66 total=376
== Davey: 17
   davey events=17 max=1671ms median=865ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look2

```
part look2; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3483 ms
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
app pids seen: ['5205', '7977', '9822']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=52 total=289
== Davey: 13
   davey events=13 max=1391ms median=930ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look3

```
part look3; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3606 ms
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
app pids seen: ['4854', '7013', '9264', '10869']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 13
   skipped-frames events=13 max=68 total=533
== Davey: 30
   davey events=30 max=1602ms median=818ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## media

```
part media; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## P. Host probe: photos of album 1 (Фотодокументална изложба) on the site
PAGE /fotodokumentalna-izlozhba: 200 0.62s 112916 bytes
img tags: 35
PROBE /fotodokumentalna-izlozhba: 30 images, optimizer non-200: 0
original 404: 19 of 30 photos; first: #11 'Стопанският двор на ТКЗС в Яворец' original 404 0.15s 0KB  | w384: not opti
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2733 ms
## G1. Album with missing photos: the 'Снимката липсва' placeholder
ALBUM 1 top: cells 18; placeholders 1
ALBUM 1 scrolled 1: 'Снимката липсва' tiles on screen: 1; cells 15
ALBUM 1 scrolled 2: 'Снимката липсва' tiles on screen: 1; cells 14
ALBUM 1 scrolled 3: 'Снимката липсва' tiles on screen: 1; cells 14
## G2. Viewer on a missing photo: message and 'Опитай отново'
VIEWER missing photo: text="13 / 29  ·  Жътва"; message SHOWN; retry button SHOWN; text="Снимката не се зареди — може да липсва на сайта или да няма връзка." text="Опитай отново" content-desc="Затвори" text="13 / 29  ·  Жътва" content-desc="Сподели" 
VIEWER retry +2s: text="Снимката не се зареди — може да липсва на сайта или да няма връзка." text="Опитай отново" content-desc="Затвори" text="13 / 29  ·  Жътва" content-desc="Сподели" 
VIEWER retry +8s: message SHOWN; app in front yes
VIEWER next: text="14 / 29  ·  Харманът"; message shown
VIEWER photo 1: text="1 / 29  ·  Сградата на читалището в Яворец"; error message absent
## D. Documents: PDF opens with its readable file name
PDF handlers: 1 activities found:   Activity #0:     priority=0 preferredOrder=0 match=0x608000 specificIndex=-1 isDefault=true     com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity 
DOCUMENTS: text="Документи" text="Исторически публикации" text="Декларации · 14.05.2026 · 4.1 MB" text="Декларация за частни интереси" text="Декларацията за частни интереси по чл. 19а ЗНЧ е официален документ, който членовете на ръководните органи на читалището
PDF document +2018 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF document +6303 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF document +13630 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; content-desc="page 1" content-desc="page 2" content-desc="page 3" text="Декларация за частни интереси.pdf" content-desc="Find" content-desc="Add to Drive" 
PDF document FILE NAME shown: text="Декларация за частни интереси.pdf"
PDF publication +2010 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF publication +6044 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity
PDF publication +13378 ms: fg com.google.android.apps.docs/com.google.android.apps.viewer.PdfViewerActivity; content-desc="page 1" text="Баба Дамяна разказва.pdf" content-desc="Find" content-desc="Add to Drive" content-desc="More options" 
PDF publication FILE NAME shown: text="Баба Дамяна разказва.pdf"
## COMPLETE
## logcat findings
app pids seen: ['5807', '7223']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=33 total=63
== Davey: 4
   davey events=4 max=883ms median=774ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem-base

```
part mem-base; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2847 ms
## M. Memory over 20 cycles (all tabs + a book + the gallery viewer), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=61429KB  Java=7728KB  Native=25952KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=67541KB  Java=8308KB  Native=25948KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
cycles done: 10, gallery viewer reached in 10
MEM c10 (raw)        PSS=118970KB  Java=19320KB  Native=54204KB  Graphics=0KB  Views=13 Activities=1
MEM c10 (trimmed)    PSS=105781KB  Java=9684KB  Native=47892KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20
MEM c20 (raw)        PSS=123050KB  Java=14900KB  Native=31884KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (trimmed)    PSS=115798KB  Java=9068KB  Native=29856KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 7249; gallery viewer reached in 20/20 cycles
## COMPLETE
## logcat findings
app pids seen: ['5857', '7249']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=34 total=130
== Davey: 3
   davey events=3 max=1414ms median=854ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem

```
part mem; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3410 ms
## M. Memory over 20 cycles (all tabs + a book + the gallery viewer), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=52011KB  Java=7644KB  Native=26152KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=53501KB  Java=6948KB  Native=14816KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
cycles done: 10, gallery viewer reached in 10
MEM c10 (raw)        PSS=99685KB  Java=18232KB  Native=38372KB  Graphics=0KB  Views=13 Activities=1
MEM c10 (trimmed)    PSS=91899KB  Java=9488KB  Native=34144KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20
MEM c20 (raw)        PSS=111768KB  Java=13364KB  Native=36860KB  Graphics=0KB  Views=13 Activities=1
MEM c20 (trimmed)    PSS=104903KB  Java=8744KB  Native=35096KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 6623; gallery viewer reached in 20/20 cycles
## COMPLETE
## logcat findings
app pids seen: ['5096', '6623']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=51 total=212
== Davey: 12
   davey events=12 max=1664ms median=760ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey-base

```
part monkey-base; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2829 ms
## K. Monkey 5000 events (seed 4321, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
statusbar disable: 
monkey start pid=7073
MONKEY exit=0 duration=133 s; Events injected: 5000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '7073'; airplane=0; camera:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MEM after-monkey (raw) PSS=100638KB  Java=16352KB  Native=45244KB  Graphics=0KB  Views=15 Activities=1
MEM after-monkey (trimmed) PSS=93914KB  Java=10092KB  Native=39804KB  Graphics=0KB  Views=15 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5668', '7073']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=41 total=146
== Davey: 3
   davey events=3 max=870ms median=803ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey

```
part monkey; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3255 ms
## K. Monkey 5000 events (seed 4321, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
statusbar disable: 
monkey start pid=6219
MONKEY exit=0 duration=335 s; Events injected: 5000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6219'; airplane=0; camera:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MEM after-monkey (raw) PSS=83338KB  Java=14872KB  Native=21672KB  Graphics=0KB  Views=17 Activities=1
MEM after-monkey (trimmed) PSS=77489KB  Java=10696KB  Native=16564KB  Graphics=0KB  Views=17 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4741', '6219']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 125
   skipped-frames events=125 max=83 total=5234
== Davey: 295
   davey events=295 max=2220ms median=899ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## O1. Online warm-up (fills the caches)
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3820 ms
online-warm home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="30 октомври 2026 · *Св. крал Стефан Милутин. Св. свщмчк Зиновий, еп. Егейски и св. мчца Зиновия." content-desc="Последни
online-warm catalogue: results (2446, 3241 ms)
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
offline-cached home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="30 октомври 2026 · *Св. крал Стефан Милутин. Св. свщмчк Зиновий, еп. Егейски и св. мчца Зиновия." content-desc="Последни
offline-cached catalogue: results (2446, 2526 ms)
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
recovered home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" text="30 октомври 2026 · *Св. крал Стефан Милутин. Св. свщмчк Зиновий, еп. Егейски и св. мчца Зиновия." content-desc="Последни
recovered catalogue: results (2446, 2835 ms)
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
GALLERY (empty cache) reconnect: albums shown after 9439 ms (-1 = not within 60 s); text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" 
recovered-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" content-desc="Лента за търсене" text="Търси книга, новина, събитие…" content-desc="Сканирай баркод" content-desc="Последни новини" text="СЪБИТИЕ" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." 
recovered-empty catalogue: results (2446, 3106 ms)
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
app pids seen: ['4962', '8954', '12117', '16027']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=38 total=178
== Davey: 10
   davey events=10 max=1282ms median=830ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## rot

```
part rot; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15987 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2902 ms
## R1. Rotation (6 toggles each) on Home, Catalogue '12', a book, Want to read, Calendar
ROT home before: tab bar yes; search field yes
ROT home landscape: tab bar yes; search field yes
ROTATION home: pid 7122 -> 7122 (same); after: tab bar yes; search field yes
ROT catalog before: query '12' kept; results 108
ROT catalog landscape: query '12' kept; results 108
ROTATION catalog: pid 7122 -> 7122 (same); after: query '12' kept; results 108
ROT book before: book title kept; back button yes
ROT book landscape: book title kept; back button yes
ROTATION book: pid 7122 -> 7122 (same); after: book title kept; back button yes
ROT want before: list title yes; book listed
ROT want landscape: list title yes; book listed
ROTATION want: pid 7122 -> 7122 (same); after: list title yes; book listed
ROT calendar before: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
ROT calendar landscape: News[checked=false selected=false bounds=[508,243][820,369]] Calendar[checked=true selected=false bounds=[820,243][1135,369]] subscribe-action=visible
ROTATION calendar: pid 7122 -> 7122 (same); after: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
GFX rotation     frames=  142  janky=141 (99.30%)  p50=250ms p90=600ms p95=800ms p99=1000ms  slowUI=98 slowDraw=141 highInput=1
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
app pids seen: ['4537', '7122', '10051', '10331', '10511', '10978', '11234', '11476', '11659']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=76 total=604
== Davey: 22
   davey events=22 max=1559ms median=848ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## sem

```
part sem; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3849 ms
## T-new. TalkBack semantics (new)
SEM new catalogue '12': content-desc repeating a visible text: 0 
SEM new book page: content-desc repeating a visible text: 0 
SEM new want list: content-desc repeating a visible text: 0 
SEM new dialog 'Тема|=Theme': rows 126px=48dp clickable=false '';126px=48dp clickable=false '';126px=48dp clickable=false '';
SEM new dialog 'Размер на текста|=Text size': rows 126px=48dp clickable=false '';126px=48dp clickable=false '';126px=48dp clickable=false '';126px=48dp clickable=false '';
SEM new dialog 'Език|=Language': rows 126px=48dp clickable=false '';126px=48dp clickable=false '';126px=48dp clickable=false '';
SEM new 'Кажи ми' book inv=136 found=yes: text="Белият детството" text="Симеон Колев" text="Не е на рафта" 
SEM new 'Кажи ми' OFF: toggle: MATCH class="android.view.View" text="" content-desc="Кажи ми, когато е на рафта" checkable="false" checked="false" clickable="false" bounds="[53,1151][1027,1277]" | ACTIONABLE-ANCESTOR class="android.view.View" text="" content-desc="" checkable="true" checked="false" clickable="true" bounds="[53,1151][1027,1277]" | ACTIONABLE-CHILD none; || desc: class="android.view.View" text="" content-desc="Кажи ми, когато е на рафта" checkable="false" checked="false" clickable="false" selected="false" bounds="[53,1151][1027,1277]"; || text: 
SEM new 'Кажи ми' after tap (ON): toggle: MATCH class="android.view.View" text="" content-desc="Кажи ми, когато е на рафта" checkable="false" checked="false" clickable="false" bounds="[53,1151][1027,1277]" | ACTIONABLE-ANCESTOR class="android.view.View" text="" content-desc="" checkable="true" checked="true" clickable="true" bounds="[53,1151][1027,1277]" | ACTIONABLE-CHILD none; || desc: class="android.view.View" text="" content-desc="Кажи ми, когато е на рафта" checkable="false" checked="false" clickable="false" selected="false" bounds="[53,1151][1027,1277]"; || text: 
SEM new 'Кажи ми' after 2nd tap (OFF): toggle: MATCH class="android.view.View" text="" content-desc="Кажи ми, когато е на рафта" checkable="false" checked="false" clickable="false" bounds="[53,1151][1027,1277]" | ACTIONABLE-ANCESTOR class="android.view.View" text="" content-desc="" checkable="true" checked="false" clickable="true" bounds="[53,1151][1027,1277]" | ACTIONABLE-CHILD none; || desc: class="android.view.View" text="" content-desc="Кажи ми, когато е на рафта" checkable="false" checked="false" clickable="false" selected="false" bounds="[53,1151][1027,1277]"; || text: ; snackbar: text="Няма да получаваш известие за тази книга."
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## T-base. TalkBack semantics (base)
SEM base catalogue '12': content-desc repeating a visible text: 6 Спомен за здрача | 12 -те вятъра | Белият дома | Белият здрача | Белият пътя | Времето на войната
SEM base book page: content-desc repeating a visible text: 1 12 -те вятъра
SEM base want list: content-desc repeating a visible text: 1 12 -те вятъра
SEM base dialog 'Тема|=Theme': rows 115px=44dp clickable=false '';115px=44dp clickable=false '';115px=44dp clickable=false '';
SEM base dialog 'Размер на текста|=Text size': rows 115px=44dp clickable=false '';115px=44dp clickable=false '';115px=44dp clickable=false '';115px=44dp clickable=false '';
SEM base dialog 'Език|=Language': rows 115px=44dp clickable=false '';115px=44dp clickable=false '';115px=44dp clickable=false '';
SEM base 'Кажи ми' book inv=136 found=yes: content-desc="Белият детството" text="Белият детството" text="Симеон Колев" 
SEM base 'Кажи ми' OFF: toggle: MATCH class="android.widget.TextView" text="Кажи ми, когато е на рафта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[300,1193][848,1237]" | ACTIONABLE-ANCESTOR class="android.view.View" text="" content-desc="" checkable="true" checked="false" clickable="true" bounds="[53,1151][1027,1277]" | ACTIONABLE-CHILD none; || desc:  || text: class="android.widget.TextView" text="Кажи ми, когато е на рафта" content-desc="" checkable="false" checked="false" clickable="false" selected="false" bounds="[300,1193][848,1237]";
SEM base 'Кажи ми' after tap (ON): toggle: MATCH class="android.widget.TextView" text="Ще ти кажем, когато е на рафта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[250,1193][899,1237]" | ACTIONABLE-ANCESTOR class="android.view.View" text="" content-desc="" checkable="true" checked="true" clickable="true" bounds="[53,1151][1027,1277]" | ACTIONABLE-CHILD none; || desc:  || text: class="android.widget.TextView" text="Ще ти кажем, когато е на рафта" content-desc="" checkable="false" checked="false" clickable="false" selected="false" bounds="[250,1193][899,1237]";
SEM base 'Кажи ми' after 2nd tap (OFF): toggle: MATCH class="android.widget.TextView" text="Кажи ми, когато е на рафта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[300,1193][848,1237]" | ACTIONABLE-ANCESTOR class="android.view.View" text="" content-desc="" checkable="true" checked="false" clickable="true" bounds="[53,1151][1027,1277]" | ACTIONABLE-CHILD none; || desc:  || text: class="android.widget.TextView" text="Кажи ми, когато е на рафта" content-desc="" checkable="false" checked="false" clickable="false" selected="false" bounds="[300,1193][848,1237]";; snackbar: text="Няма да получаваш известие за тази книга."
## COMPLETE
## logcat findings
app pids seen: ['5102', '9355']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 9
   skipped-frames events=9 max=45 total=331
== Davey: 15
   davey events=15 max=1703ms median=958ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widget

```
part widget; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3022 ms
## W1. Add the widget from the launcher's widget picker (default 4×2 = wide)
launcher: com.google.android.apps.nexuslauncher
WIDGET new long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET new picker: our row @ 437 923; size label @ 540 1831; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET new after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET new: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
## W2. Wide widget, signed out: content
WIDGET wide: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_scan ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET signed-out: loans block hidden; event shown; no-events absent
## W3. Wide widget: clicks
WIDGET click catalog (widget_catalog @ 887 595) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 711 595) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвор
WIDGET click event (widget_event @ 540 714) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 342 595) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
## W4. Wide widget in dark mode
WIDGET wide dark: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_scan ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|1 ноември 2026|; error text: 
## W5. Small widget (resized to 2 columns)
RESIZE frame: 
RESIZE: right edge 1023 → 491; options now: 
WIDGET small: views from org.chyavorec.app: 1 [ids: widget_event widget_event_column widget_event_date widget_event_title ]; bounds [57 537 519 1123]; texts: Chitalishte Yavorets|Next event|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET click small-event (widget_event @ 288 721) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click small-app (t:Chitalishte Yavorets|Читалище Яворец @ 319 588) → fg org.chyavorec.app/org.chyavorec.app.MainActivity; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET small dark: views from org.chyavorec.app: 1 [ids: widget_event widget_event_column widget_event_date widget_event_title ]; bounds [57 537 519 1123]; texts: Chitalishte Yavorets|Next event|Ден на народните будители|1 ноември 2026|; error text: 
## W6. After a reboot of the app process (force-stop) and a new day of data
WIDGET after force-stop: views from org.chyavorec.app: 1 [ids: widget_event widget_event_column widget_event_date widget_event_title ]; bounds [57 537 519 1123]; texts: Chitalishte Yavorets|Next event|Ден на народните будители|1 ноември 2026|; error text: 
## COMPLETE
## logcat findings
app pids seen: ['5000']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 9
   skipped-frames events=9 max=81 total=376
== Davey: 13
   davey events=13 max=2085ms median=897ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widgetupd

```
part widgetupd; APK: 1.2.28 52334a7 (Merge remote-tracking branch 'origin/wave14/modern' into claude/charmi); versionCode 1484480; x86_64 libs: 4 size 4311931 bytes
base APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## U0. Install v1.2.27 (Glance widget)
installed base: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530;  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3606 ms
groups: NONE
channels: events(Събития)->null; messages(Съобщения от читалището)->null; updates(Обновления)->null; news(Новини)->null; loans(Срокове за връщане)->null; library(Библиотека)->null; messages_important(Важни съобщения)->null
## U1. v1.2.27: add the widget
WIDGET base long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET base picker: our row @ 437 772; size label @ 540 1680; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET base after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET base: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET v1.2.27: views from org.chyavorec.app: 1 [ids: rootView ]; bounds [57 537 1023 1123]; texts: Читалище Яворец|Сканирай|Каталог|Следващо събитие|Ден на народните будители|1 ноември 2026|; error text: 
## U2. adb install -r v1.2.28 over v1.2.27 with the widget on the home screen (app not opened)
after adb install -r:  versionCode=1484480 minSdk=26 targetSdk=37  versionName=1.2.28 
WIDGET +3s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_scan ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET +15s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_scan ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET +45s after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_scan ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|1 ноември 2026|; error text: 
widget ids before/after the update: [id=2 ] / [id=2 ]
## U3. The kept widget after the update: clicks
WIDGET click catalog (widget_catalog @ 887 595) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
WIDGET click scan (widget_scan @ 711 595) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Нужен е достъп до камерата" text="Камерата се използва само за четене на баркода на телефона. Нищо не се снима и не се изпраща." text="Разреши камерата" text="Отвор
WIDGET click event (widget_event @ 540 714) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
WIDGET click app (t:Chitalishte Yavorets|Читалище Яворец @ 342 595) → fg org.chyavorec.app/org.chyavorec.app.OpenLink; expected screen: YES; text="Новини" text="Календар" content-desc="Абонирай се за календара" content-desc="Изглед календар" text="Всички" text="Библиотека" 
## U4. App opened after the update: channel groups; widget
groups: group_app=Приложението; group_my_library=Моята библиотека; group_centre=Читалището
channels: events(Събития)->group_centre; messages(Съобщения от читалището)->group_centre; updates(Обновления)->group_app; news(Новини)->group_centre; loans(Срокове за връщане)->group_my_library; library(Библиотека)->group_my_library; messages_important(Важни съобщения)->group_centre
SYSTEM notification settings: fg com.android.settings/com.android.settings.Settings$AppNotificationSettingsActivity; content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Chitalishte Yavorets" text="All Chitalishte Yavorets notifications" text="Приложението" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения от читалището" content-desc="Съобщения от читалището" content-desc="Важни съобщения" 
SYSTEM notification settings (scrolled): content-desc="Chitalishte Yavorets" content-desc="Navigate up" text="Приложението" text="Обновления" content-desc="Обновления" text="Читалището" text="Събития" content-desc="Събития" text="Съобщения от читалището" content-desc="Съобщения от читалището" text="Важни съобщения" content-desc="Важни съобщения" text="Новини" content-desc="Новини" text="Моята библиотека" text="Библиотека" content-desc="Библиотека" text="Срокове за връщане" content-desc="Срокове за връщане" text="Allow notification dot"
WIDGET after the app ran: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_scan ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|1 ноември 2026|; error text: 
WIDGET dark after update: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event_column widget_event_date widget_event_title widget_scan ]; bounds [57 537 1023 1123]; texts: Chitalishte Yavorets|Scan|Catalogue|Next event|Ден на народните будители|1 ноември 2026|; error text: 
## COMPLETE
## logcat findings
app pids seen: ['5563', '8124', '8709']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=74 total=269
== Davey: 6
   davey events=6 max=1291ms median=928ms
== NativeLink: 0
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

### abstart-3/shots

- [abstart-3/shots/ab-base-1.png](abstart-3/shots/ab-base-1.png)
- [abstart-3/shots/ab-base-2.png](abstart-3/shots/ab-base-2.png)
- [abstart-3/shots/ab-new-1.png](abstart-3/shots/ab-new-1.png)
- [abstart-3/shots/ab-new-2.png](abstart-3/shots/ab-new-2.png)

### fonts/shots/fonts

- [fonts/shots/fonts/bg-article-1.png](fonts/shots/fonts/bg-article-1.png)
- [fonts/shots/fonts/bg-article-2.png](fonts/shots/fonts/bg-article-2.png)
- [fonts/shots/fonts/bg-article-3.png](fonts/shots/fonts/bg-article-3.png)
- [fonts/shots/fonts/bg-article-4.png](fonts/shots/fonts/bg-article-4.png)
- [fonts/shots/fonts/bg-book-399510.png](fonts/shots/fonts/bg-book-399510.png)
- [fonts/shots/fonts/bg-book-399555.png](fonts/shots/fonts/bg-book-399555.png)
- [fonts/shots/fonts/bg-book-399580.png](fonts/shots/fonts/bg-book-399580.png)
- [fonts/shots/fonts/bg-book-399637.png](fonts/shots/fonts/bg-book-399637.png)
- [fonts/shots/fonts/bg-catalog-sanktus.png](fonts/shots/fonts/bg-catalog-sanktus.png)
- [fonts/shots/fonts/bg-contacts.png](fonts/shots/fonts/bg-contacts.png)
- [fonts/shots/fonts/bg-news.png](fonts/shots/fonts/bg-news.png)
- [fonts/shots/fonts/bg-privacy-arrow.png](fonts/shots/fonts/bg-privacy-arrow.png)
- [fonts/shots/fonts/bg-scan-result.png](fonts/shots/fonts/bg-scan-result.png)
- [fonts/shots/fonts/bg-settings-1.png](fonts/shots/fonts/bg-settings-1.png)
- [fonts/shots/fonts/bg-settings-2.png](fonts/shots/fonts/bg-settings-2.png)
- [fonts/shots/fonts/bg-settings-3.png](fonts/shots/fonts/bg-settings-3.png)
- [fonts/shots/fonts/en-article-1.png](fonts/shots/fonts/en-article-1.png)
- [fonts/shots/fonts/en-article-2.png](fonts/shots/fonts/en-article-2.png)
- [fonts/shots/fonts/en-article-3.png](fonts/shots/fonts/en-article-3.png)
- [fonts/shots/fonts/en-article-4.png](fonts/shots/fonts/en-article-4.png)
- [fonts/shots/fonts/en-book-399510.png](fonts/shots/fonts/en-book-399510.png)
- [fonts/shots/fonts/en-book-399555.png](fonts/shots/fonts/en-book-399555.png)
- [fonts/shots/fonts/en-book-399580.png](fonts/shots/fonts/en-book-399580.png)
- [fonts/shots/fonts/en-book-399637.png](fonts/shots/fonts/en-book-399637.png)
- [fonts/shots/fonts/en-catalog-sanktus.png](fonts/shots/fonts/en-catalog-sanktus.png)
- [fonts/shots/fonts/en-contacts.png](fonts/shots/fonts/en-contacts.png)
- [fonts/shots/fonts/en-news.png](fonts/shots/fonts/en-news.png)
- [fonts/shots/fonts/en-privacy-arrow.png](fonts/shots/fonts/en-privacy-arrow.png)
- [fonts/shots/fonts/en-scan-result.png](fonts/shots/fonts/en-scan-result.png)
- [fonts/shots/fonts/en-settings-1.png](fonts/shots/fonts/en-settings-1.png)
- [fonts/shots/fonts/en-settings-2.png](fonts/shots/fonts/en-settings-2.png)
- [fonts/shots/fonts/en-settings-3.png](fonts/shots/fonts/en-settings-3.png)

### func14/shots/channels

- [func14/shots/channels/01-system-settings-a.png](func14/shots/channels/01-system-settings-a.png)
- [func14/shots/channels/01-system-settings-b.png](func14/shots/channels/01-system-settings-b.png)
- [func14/shots/channels/01-system-settings-c.png](func14/shots/channels/01-system-settings-c.png)

### func14/shots/func2

- [func14/shots/func2/01-manual-panel.png](func14/shots/func2/01-manual-panel.png)
- [func14/shots/func2/02-after-back.png](func14/shots/func2/02-after-back.png)
- [func14/shots/func2/03-manual-result.png](func14/shots/func2/03-manual-result.png)

### func14/shots/login

- [func14/shots/login/01-after-open.png](func14/shots/login/01-after-open.png)
- [func14/shots/login/04-after-open.png](func14/shots/login/04-after-open.png)
- [func14/shots/login/08-after-open.png](func14/shots/login/08-after-open.png)
- [func14/shots/login/10-typed.png](func14/shots/login/10-typed.png)

### func14/shots/search

- [func14/shots/search/00-home.png](func14/shots/search/00-home.png)
- [func14/shots/search/01-expanded.png](func14/shots/search/01-expanded.png)
- [func14/shots/search/02-typed-12.png](func14/shots/search/02-typed-12.png)
- [func14/shots/search/03-result.png](func14/shots/search/03-result.png)
- [func14/shots/search/04-back-from-result.png](func14/shots/search/04-back-from-result.png)
- [func14/shots/search/05-after-back-key.png](func14/shots/search/05-after-back-key.png)
- [func14/shots/search/10-gesture-midway.png](func14/shots/search/10-gesture-midway.png)
- [func14/shots/search/11-after-gesture.png](func14/shots/search/11-after-gesture.png)
- [func14/shots/search/14-gesture3-midway.png](func14/shots/search/14-gesture3-midway.png)

### func14/shots/shortcuts

- [func14/shots/shortcuts/01-before-popup.png](func14/shots/shortcuts/01-before-popup.png)
- [func14/shots/shortcuts/02-after-popup.png](func14/shots/shortcuts/02-after-popup.png)
- [func14/shots/shortcuts/03-book-shortcut-opened.png](func14/shots/shortcuts/03-book-shortcut-opened.png)

### homey/homey/base-cached-1

- [homey/homey/base-cached-1/t1s.png](homey/homey/base-cached-1/t1s.png)
- [homey/homey/base-cached-1/t2s.png](homey/homey/base-cached-1/t2s.png)
- [homey/homey/base-cached-1/t3s.png](homey/homey/base-cached-1/t3s.png)
- [homey/homey/base-cached-1/t4s.png](homey/homey/base-cached-1/t4s.png)

### homey/homey/base-cached-2

- [homey/homey/base-cached-2/t1s.png](homey/homey/base-cached-2/t1s.png)
- [homey/homey/base-cached-2/t2s.png](homey/homey/base-cached-2/t2s.png)
- [homey/homey/base-cached-2/t3s.png](homey/homey/base-cached-2/t3s.png)

### homey/homey/base-cached-3

- [homey/homey/base-cached-3/t1s.png](homey/homey/base-cached-3/t1s.png)
- [homey/homey/base-cached-3/t2s.png](homey/homey/base-cached-3/t2s.png)
- [homey/homey/base-cached-3/t3s.png](homey/homey/base-cached-3/t3s.png)
- [homey/homey/base-cached-3/t4s.png](homey/homey/base-cached-3/t4s.png)
- [homey/homey/base-cached-3/t6s.png](homey/homey/base-cached-3/t6s.png)

### homey/homey/base-clearcache-1

- [homey/homey/base-clearcache-1/t1s.png](homey/homey/base-clearcache-1/t1s.png)
- [homey/homey/base-clearcache-1/t2s.png](homey/homey/base-clearcache-1/t2s.png)
- [homey/homey/base-clearcache-1/t3s.png](homey/homey/base-clearcache-1/t3s.png)
- [homey/homey/base-clearcache-1/t4s.png](homey/homey/base-clearcache-1/t4s.png)

### homey/homey/base-clearcache-2

- [homey/homey/base-clearcache-2/t1s.png](homey/homey/base-clearcache-2/t1s.png)
- [homey/homey/base-clearcache-2/t2s.png](homey/homey/base-clearcache-2/t2s.png)
- [homey/homey/base-clearcache-2/t3s.png](homey/homey/base-clearcache-2/t3s.png)
- [homey/homey/base-clearcache-2/t4s.png](homey/homey/base-clearcache-2/t4s.png)

### homey/homey/base-clearcache-3

- [homey/homey/base-clearcache-3/t1s.png](homey/homey/base-clearcache-3/t1s.png)
- [homey/homey/base-clearcache-3/t2s.png](homey/homey/base-clearcache-3/t2s.png)
- [homey/homey/base-clearcache-3/t3s.png](homey/homey/base-clearcache-3/t3s.png)
- [homey/homey/base-clearcache-3/t4s.png](homey/homey/base-clearcache-3/t4s.png)
- [homey/homey/base-clearcache-3/t6s.png](homey/homey/base-clearcache-3/t6s.png)
- [homey/homey/base-clearcache-3/t8s.png](homey/homey/base-clearcache-3/t8s.png)

### homey/homey/new-cached-1

- [homey/homey/new-cached-1/t1s.png](homey/homey/new-cached-1/t1s.png)
- [homey/homey/new-cached-1/t2s.png](homey/homey/new-cached-1/t2s.png)
- [homey/homey/new-cached-1/t3s.png](homey/homey/new-cached-1/t3s.png)
- [homey/homey/new-cached-1/t4s.png](homey/homey/new-cached-1/t4s.png)

### homey/homey/new-cached-2

- [homey/homey/new-cached-2/t1s.png](homey/homey/new-cached-2/t1s.png)
- [homey/homey/new-cached-2/t2s.png](homey/homey/new-cached-2/t2s.png)
- [homey/homey/new-cached-2/t3s.png](homey/homey/new-cached-2/t3s.png)
- [homey/homey/new-cached-2/t4s.png](homey/homey/new-cached-2/t4s.png)
- [homey/homey/new-cached-2/t6s.png](homey/homey/new-cached-2/t6s.png)

### homey/homey/new-cached-3

- [homey/homey/new-cached-3/t1s.png](homey/homey/new-cached-3/t1s.png)
- [homey/homey/new-cached-3/t2s.png](homey/homey/new-cached-3/t2s.png)
- [homey/homey/new-cached-3/t3s.png](homey/homey/new-cached-3/t3s.png)
- [homey/homey/new-cached-3/t4s.png](homey/homey/new-cached-3/t4s.png)

### homey/homey/new-clearcache-1

- [homey/homey/new-clearcache-1/t1s.png](homey/homey/new-clearcache-1/t1s.png)
- [homey/homey/new-clearcache-1/t2s.png](homey/homey/new-clearcache-1/t2s.png)
- [homey/homey/new-clearcache-1/t3s.png](homey/homey/new-clearcache-1/t3s.png)
- [homey/homey/new-clearcache-1/t4s.png](homey/homey/new-clearcache-1/t4s.png)

### homey/homey/new-clearcache-2

- [homey/homey/new-clearcache-2/t1s.png](homey/homey/new-clearcache-2/t1s.png)
- [homey/homey/new-clearcache-2/t2s.png](homey/homey/new-clearcache-2/t2s.png)
- [homey/homey/new-clearcache-2/t3s.png](homey/homey/new-clearcache-2/t3s.png)
- [homey/homey/new-clearcache-2/t4s.png](homey/homey/new-clearcache-2/t4s.png)

### homey/homey/new-clearcache-3

- [homey/homey/new-clearcache-3/t1s.png](homey/homey/new-clearcache-3/t1s.png)
- [homey/homey/new-clearcache-3/t2s.png](homey/homey/new-clearcache-3/t2s.png)
- [homey/homey/new-clearcache-3/t3s.png](homey/homey/new-clearcache-3/t3s.png)
- [homey/homey/new-clearcache-3/t4s.png](homey/homey/new-clearcache-3/t4s.png)
- [homey/homey/new-clearcache-3/t6s.png](homey/homey/new-clearcache-3/t6s.png)

### homey/shots/homey

- [homey/shots/homey/base-scrim-dark.png](homey/shots/homey/base-scrim-dark.png)
- [homey/shots/homey/base-scrim-light.png](homey/shots/homey/base-scrim-light.png)
- [homey/shots/homey/new-scrim-dark.png](homey/shots/homey/new-scrim-dark.png)
- [homey/shots/homey/new-scrim-light.png](homey/shots/homey/new-scrim-light.png)

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

- [media/shots/media/01-album-top.png](media/shots/media/01-album-top.png)
- [media/shots/media/02-album-scrolled-1.png](media/shots/media/02-album-scrolled-1.png)
- [media/shots/media/02-album-scrolled-2.png](media/shots/media/02-album-scrolled-2.png)
- [media/shots/media/02-album-scrolled-3.png](media/shots/media/02-album-scrolled-3.png)
- [media/shots/media/10-viewer-missing.png](media/shots/media/10-viewer-missing.png)
- [media/shots/media/11-viewer-retry-2s.png](media/shots/media/11-viewer-retry-2s.png)
- [media/shots/media/12-viewer-retry-8s.png](media/shots/media/12-viewer-retry-8s.png)
- [media/shots/media/13-viewer-next.png](media/shots/media/13-viewer-next.png)
- [media/shots/media/14-viewer-ok-photo1.png](media/shots/media/14-viewer-ok-photo1.png)
- [media/shots/media/20-documents.png](media/shots/media/20-documents.png)
- [media/shots/media/21-publications.png](media/shots/media/21-publications.png)
- [media/shots/media/pdf-document.png](media/shots/media/pdf-document.png)
- [media/shots/media/pdf-publication.png](media/shots/media/pdf-publication.png)

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

### sem/shots/sem

- [sem/shots/sem/base-01-catalog-12.png](sem/shots/sem/base-01-catalog-12.png)
- [sem/shots/sem/base-02-book.png](sem/shots/sem/base-02-book.png)
- [sem/shots/sem/base-03-want.png](sem/shots/sem/base-03-want.png)
- [sem/shots/sem/base-10-dialog-Език.png](sem/shots/sem/base-10-dialog-Език.png)
- [sem/shots/sem/base-10-dialog-Размер_на_текста.png](sem/shots/sem/base-10-dialog-Размер_на_текста.png)
- [sem/shots/sem/base-10-dialog-Тема.png](sem/shots/sem/base-10-dialog-Тема.png)
- [sem/shots/sem/base-20-notify-off.png](sem/shots/sem/base-20-notify-off.png)
- [sem/shots/sem/base-21-notify-on.png](sem/shots/sem/base-21-notify-on.png)
- [sem/shots/sem/base-22-notify-off-again.png](sem/shots/sem/base-22-notify-off-again.png)
- [sem/shots/sem/new-01-catalog-12.png](sem/shots/sem/new-01-catalog-12.png)
- [sem/shots/sem/new-02-book.png](sem/shots/sem/new-02-book.png)
- [sem/shots/sem/new-03-want.png](sem/shots/sem/new-03-want.png)
- [sem/shots/sem/new-10-dialog-Език.png](sem/shots/sem/new-10-dialog-Език.png)
- [sem/shots/sem/new-10-dialog-Размер_на_текста.png](sem/shots/sem/new-10-dialog-Размер_на_текста.png)
- [sem/shots/sem/new-10-dialog-Тема.png](sem/shots/sem/new-10-dialog-Тема.png)
- [sem/shots/sem/new-20-notify-off.png](sem/shots/sem/new-20-notify-off.png)
- [sem/shots/sem/new-21-notify-on.png](sem/shots/sem/new-21-notify-on.png)
- [sem/shots/sem/new-22-notify-off-again.png](sem/shots/sem/new-22-notify-off-again.png)

### widget/shots/widget

- [widget/shots/widget/10-wide-light.png](widget/shots/widget/10-wide-light.png)
- [widget/shots/widget/20-wide-dark.png](widget/shots/widget/20-wide-dark.png)
- [widget/shots/widget/30-small-light.png](widget/shots/widget/30-small-light.png)
- [widget/shots/widget/31-small-dark.png](widget/shots/widget/31-small-dark.png)
- [widget/shots/widget/40-after-force-stop.png](widget/shots/widget/40-after-force-stop.png)
- [widget/shots/widget/new-00-home.png](widget/shots/widget/new-00-home.png)
- [widget/shots/widget/new-01-longpress.png](widget/shots/widget/new-01-longpress.png)
- [widget/shots/widget/new-03-list.png](widget/shots/widget/new-03-list.png)
- [widget/shots/widget/new-04-app.png](widget/shots/widget/new-04-app.png)
- [widget/shots/widget/new-05-dropped.png](widget/shots/widget/new-05-dropped.png)
- [widget/shots/widget/new-29-resize-frame.png](widget/shots/widget/new-29-resize-frame.png)
- [widget/shots/widget/new-29b-resized.png](widget/shots/widget/new-29b-resized.png)
- [widget/shots/widget/new-click-app.png](widget/shots/widget/new-click-app.png)
- [widget/shots/widget/new-click-catalog.png](widget/shots/widget/new-click-catalog.png)
- [widget/shots/widget/new-click-event.png](widget/shots/widget/new-click-event.png)
- [widget/shots/widget/new-click-scan.png](widget/shots/widget/new-click-scan.png)
- [widget/shots/widget/new-click-small-app.png](widget/shots/widget/new-click-small-app.png)
- [widget/shots/widget/new-click-small-event.png](widget/shots/widget/new-click-small-event.png)

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
- [abstart-3/cat-first-base.txt](abstart-3/cat-first-base.txt)
- [abstart-3/cat-first-new.txt](abstart-3/cat-first-new.txt)
- [abstart-3/cat-ready-base.txt](abstart-3/cat-ready-base.txt)
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
- [abstart-3/start-new-1-cold.txt](abstart-3/start-new-1-cold.txt)
- [abstart-3/start-new-1-warm.txt](abstart-3/start-new-1-warm.txt)
- [abstart-3/start-new-2-cold.txt](abstart-3/start-new-2-cold.txt)
- [abstart-3/start-new-2-warm.txt](abstart-3/start-new-2-warm.txt)
- [abstart-3/start-new-cold-all.txt](abstart-3/start-new-cold-all.txt)
- [abstart-3/start-new-warm-all.txt](abstart-3/start-new-warm-all.txt)
- [abstart-3/summary.txt](abstart-3/summary.txt)
- [fonts/host-after-run.txt](fonts/host-after-run.txt)
- [fonts/job-status.txt](fonts/job-status.txt)
- [fonts/logcat-all.txt](fonts/logcat-all.txt)
- [fonts/logcat-dump.txt](fonts/logcat-dump.txt)
- [fonts/logcat-findings.txt](fonts/logcat-findings.txt)
- [fonts/logcat-live.txt](fonts/logcat-live.txt)
- [fonts/summary.txt](fonts/summary.txt)
- [func14/channels-new.txt](func14/channels-new.txt)
- [func14/host-after-run.txt](func14/host-after-run.txt)
- [func14/job-status.txt](func14/job-status.txt)
- [func14/logcat-all.txt](func14/logcat-all.txt)
- [func14/logcat-dump.txt](func14/logcat-dump.txt)
- [func14/logcat-findings.txt](func14/logcat-findings.txt)
- [func14/logcat-live.txt](func14/logcat-live.txt)
- [func14/notif-new.txt](func14/notif-new.txt)
- [func14/shortcut-after-app.txt](func14/shortcut-after-app.txt)
- [func14/shortcut-after.txt](func14/shortcut-after.txt)
- [func14/shortcut-before-app.txt](func14/shortcut-before-app.txt)
- [func14/shortcut-before.txt](func14/shortcut-before.txt)
- [func14/summary.txt](func14/summary.txt)
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
- [homey/homey/base-clearcache-3/final.xml](homey/homey/base-clearcache-3/final.xml)
- [homey/homey/base-clearcache-3/rec.mp4](homey/homey/base-clearcache-3/rec.mp4)
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
- [homey/homey/new-clearcache-3/final.xml](homey/homey/new-clearcache-3/final.xml)
- [homey/homey/new-clearcache-3/rec.mp4](homey/homey/new-clearcache-3/rec.mp4)
- [homey/host-after-run.txt](homey/host-after-run.txt)
- [homey/job-status.txt](homey/job-status.txt)
- [homey/logcat-all.txt](homey/logcat-all.txt)
- [homey/logcat-dump.txt](homey/logcat-dump.txt)
- [homey/logcat-findings.txt](homey/logcat-findings.txt)
- [homey/logcat-live.txt](homey/logcat-live.txt)
- [homey/summary.txt](homey/summary.txt)
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
- [media/host-after-run.txt](media/host-after-run.txt)
- [media/job-status.txt](media/job-status.txt)
- [media/logcat-all.txt](media/logcat-all.txt)
- [media/logcat-dump.txt](media/logcat-dump.txt)
- [media/logcat-findings.txt](media/logcat-findings.txt)
- [media/logcat-live.txt](media/logcat-live.txt)
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
- [mem-base/mem-c10-raw.txt](mem-base/mem-c10-raw.txt)
- [mem-base/mem-c10.txt](mem-base/mem-c10.txt)
- [mem-base/mem-c20-raw.txt](mem-base/mem-c20-raw.txt)
- [mem-base/mem-c20.txt](mem-base/mem-c20.txt)
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
- [sem/host-after-run.txt](sem/host-after-run.txt)
- [sem/job-status.txt](sem/job-status.txt)
- [sem/logcat-all.txt](sem/logcat-all.txt)
- [sem/logcat-dump.txt](sem/logcat-dump.txt)
- [sem/logcat-findings.txt](sem/logcat-findings.txt)
- [sem/logcat-live.txt](sem/logcat-live.txt)
- [sem/sem-base-book.xml](sem/sem-base-book.xml)
- [sem/sem-base-catalog.xml](sem/sem-base-catalog.xml)
- [sem/sem-base-dialog-Език.xml](sem/sem-base-dialog-Език.xml)
- [sem/sem-base-dialog-Размер_на_текста.xml](sem/sem-base-dialog-Размер_на_текста.xml)
- [sem/sem-base-dialog-Тема.xml](sem/sem-base-dialog-Тема.xml)
- [sem/sem-base-notify-1793374694.xml](sem/sem-base-notify-1793374694.xml)
- [sem/sem-base-notify-1793374703.xml](sem/sem-base-notify-1793374703.xml)
- [sem/sem-base-notify-1793374712.xml](sem/sem-base-notify-1793374712.xml)
- [sem/sem-new-book.xml](sem/sem-new-book.xml)
- [sem/sem-new-catalog.xml](sem/sem-new-catalog.xml)
- [sem/sem-new-dialog-Език.xml](sem/sem-new-dialog-Език.xml)
- [sem/sem-new-dialog-Размер_на_текста.xml](sem/sem-new-dialog-Размер_на_текста.xml)
- [sem/sem-new-dialog-Тема.xml](sem/sem-new-dialog-Тема.xml)
- [sem/sem-new-notify-1793374476.xml](sem/sem-new-notify-1793374476.xml)
- [sem/sem-new-notify-1793374483.xml](sem/sem-new-notify-1793374483.xml)
- [sem/sem-new-notify-1793374490.xml](sem/sem-new-notify-1793374490.xml)
- [sem/summary.txt](sem/summary.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/size-base.txt](size/size-base.txt)
- [size/size-new.txt](size/size-new.txt)
- [widget/appwidget-new-after.txt](widget/appwidget-new-after.txt)
- [widget/appwidget-new-before.txt](widget/appwidget-new-before.txt)
- [widget/appwidget-new-resized.txt](widget/appwidget-new-resized.txt)
- [widget/host-after-run.txt](widget/host-after-run.txt)
- [widget/job-status.txt](widget/job-status.txt)
- [widget/logcat-all.txt](widget/logcat-all.txt)
- [widget/logcat-dump.txt](widget/logcat-dump.txt)
- [widget/logcat-findings.txt](widget/logcat-findings.txt)
- [widget/logcat-live.txt](widget/logcat-live.txt)
- [widget/summary.txt](widget/summary.txt)
- [widget/wids.txt](widget/wids.txt)
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
