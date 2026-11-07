# Wave 19 emulator check: test v1.2.33 (6d3a324) vs v1.2.32 (87ef20e)

Rounds a–e of debug/wave19-check (runs in runs.txt), merged. API 33 google_apis x86_64, -gpu swangle_indirect, unless the part name/status says otherwise (fb28/surprise28*: API 28, surprise31: API 31, live36: API 36, live361: API 36.1). A folder prefixed a-/b-/c- is the older round of a part re-run later. Previous release check: [../v132/index.md](../v132/index.md).

| part | round | status |
|---|---|---|
| a-fb28 | a (6585a7a) | COMPLETE; part=fb28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| a-live36 | a (6585a7a) | COMPLETE; part=live36 target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| a-motion19 | a (6585a7a) | COMPLETE; part=motion19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| a-year19 | a (6585a7a) | COMPLETE; part=year19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| abstart-1 | a (6585a7a) | COMPLETE; part=abstart-1 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| abstart-2 | a (6585a7a) | COMPLETE; part=abstart-2 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| abstart-3 | a (6585a7a) | COMPLETE; part=abstart-3 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| article19 | c (cac3a84) | COMPLETE; part=article19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| b-live361 | b (c41d443) | COMPLETE; part=live361 target=google_apis api=36.1 gpu=swangle_indirect emulator-step=success |
| c-surprise28 | c (cac3a84) | COMPLETE; part=surprise28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| cardcrash | b (c41d443) | COMPLETE; part=cardcrash target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| confetti19 | c (cac3a84) | COMPLETE; part=confetti19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| drawn19 | a (6585a7a) | COMPLETE; part=drawn19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fb28 | b (c41d443) | COMPLETE; part=fb28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| func19 | a (6585a7a) | COMPLETE; part=func19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| func19b | b (c41d443) | COMPLETE; part=func19b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| idle19 | a (6585a7a) | COMPLETE; part=idle19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| idlelong | c (cac3a84) | COMPLETE; part=idlelong target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| jank19-1 | a (6585a7a) | incomplete; part=jank19-1 target=google_apis api=33 gpu=swangle_indirect emulator-step=failure |
| jank19-2 | a (6585a7a) | COMPLETE; part=jank19-2 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| live36 | b (c41d443) | COMPLETE; part=live36 target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| live361 | c (cac3a84) | COMPLETE; part=live361 target=google_apis api=36.1 gpu=swangle_indirect emulator-step=success |
| mem | a (6585a7a) | COMPLETE; part=mem target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mem-base | a (6585a7a) | COMPLETE; part=mem-base target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| monkey19 | a (6585a7a) | COMPLETE; part=monkey19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| motion19 | b (c41d443) | COMPLETE; part=motion19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| surprise28 | tip (be14797) | COMPLETE; part=surprise28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| surprise28rm | tip (be14797) | COMPLETE; part=surprise28rm target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| surprise31 | tip (be14797) | COMPLETE; part=surprise31 target=google_apis api=31 gpu=swangle_indirect emulator-step=success |
| surprise33 | c (cac3a84) | COMPLETE; part=surprise33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| year19 | b (c41d443) | COMPLETE; part=year19 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
== new 1.2.33 6d3a324: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4266950 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488546  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.33" (Raw: "1.2.33")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.POST_PROMOTED_NOTIFICATIONS' 'android.permission.USE_BIOMETRIC' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.FOREGROUND_SERVICE' 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
part                    compressed  uncompressed  files
classes*.dex               3497735       6902516      2
res                         468408        832536     57
resources.arsc              235600        235600      1
assets/                      19571         19571      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5488         23940      1
META-INF/                      846          1288      6
== base 1.2.32 87ef20e: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4153198 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488545  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.32" (Raw: "1.2.32")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.USE_BIOMETRIC' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.FOREGROUND_SERVICE' 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
part                    compressed  uncompressed  files
classes*.dex               3395623       6707860      2
res                         468365        832408     57
resources.arsc              224392        224392      1
assets/                      19210         19210      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5463         23760      1
META-INF/                      846          1288      6
```

## sizediff

```
== release-config APKs: new 4266950 B, base 4153198 B (v1.2.32 as released: 4153198 B)
TOTAL compressed entries: new 4242891 B, base 4129142 B, delta +113749 B
ARSC resources by type (new/base/delta): string 857/804/+53, plurals 34/26/+8, color 24/23/+1
ARSC string configurations: new 2, base 2; only in new: []
RES files, top growth by resource-name prefix (compressed B, new/base):
       +37  xml/file_*  (183/146)
        +5  layout/widget_*  (3145/3140)
        +1  xml/shortcuts_*  (553/552)
        +1  drawable/widget_*  (1692/1691)
RES resources with files only in new: 0; sample: []
DEX classes (mapping.txt): new 8606, base 8458; top growth by package:
    +125  org.chyavorec.app  (1122/997)
     +17  androidx.graphics.shapes  (17/0)
      +9  androidx.compose.ui  (967/958)
      +7  org.chyavorec.data  (381/374)
      +1  org.chyavorec.domain  (168/167)
      +1  okhttp3.internal  (148/147)
      +1  kotlin.sequences  (22/21)
      +1  com.google.mlkit  (33/32)
      +1  coil3.RealImageLoader$execute$2  (1/0)
      +1  androidx.work.OneTimeWorkRequestKt  (1/0)
      +1  androidx.work.DataKt  (1/0)
      +1  androidx.navigation.NavGraph$Companion$$ExternalSyntheticLambda0  (1/0)
      +1  androidx.core.view  (95/94)
DEX size by package (depth 2, apkanalyzer, bytes), top growth:
   +170964  <TOTAL>  (5629814/5458850)
   +131729  org.chyavorec  (1345874/1214145)
    +16798  com.google  (773294/756496)
     +6701  androidx.work  (101570/94869)
     +6634  androidx.graphics  (6915/281)
     +5554  okhttp3.internal  (114711/109157)
     +4582  androidx.compose  (1851084/1846502)
     +4520  kotlin.math  (16967/12447)
     +2647  kotlin.jvm  (58033/55386)
     +2189  androidx.core  (96804/94615)
     +2121  androidx.navigation  (48669/46548)
     +1407  kotlin.sequences  (7850/6443)
DEX size by package (depth 3, apkanalyzer, bytes), top growth:
   +170964  <TOTAL>  (5629814/5458850)
   +123861  org.chyavorec.app  (978813/854952)
    +21179  com.google.android  (677770/656591)
     +7946  androidx.compose.foundation  (585458/577512)
     +6765  androidx.work.impl  (77495/70730)
     +6634  androidx.graphics.shapes  (6634/0)
     +6600  org.chyavorec.data  (258467/251867)
     +5171  okhttp3.internal.platform  (24609/19438)
     +4520  kotlin.math  (16967/12447)
     +3239  androidx.compose.material3  (349816/346577)
     +2841  org.chyavorec.domain  (88389/85548)
     +2647  kotlin.jvm.internal  (56947/54300)
## COMPLETE
```

## a-fb28

```
part fb28; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5669 ms
## P1. Home ribbon on API 28 (no AGSL): the still pattern, drawn once
FB frames in 18 s: 255
FB ribbon [0 215 1080 333] mean=208.7 std=67.2 thread-px=27.6%; change 3→10→18 s: 0.44 0.50
## P2. Library card: fallback foil gradients
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
FB card: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P3. 'Изненадай ме'
FB surprise: ; bottom bar hidden
## P4. Book open
REC fb-bookopen: 34 frames @8 fps; motion 0 150 1080 2150: 
## P5. Event: share as image
SHARE fb-event sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE fb-event: chooser OPEN after 1 s (fg android/com.android.internal.app.ChooserActivity); app log: 11-07 16:10:29.765  7421  8129 W BugleDataModel: java.lang.IllegalStateException: Not allowed to start service Intent { cmp=com.google.android.apps.messaging/.s~
SHARE fb-event cache/share: 244429 2026-11-07 16:10 chyavorec-event-1794067829139.png;
SHARE fb-event PNG: chyavorec-event-1794067829139.png → PNG-signature=yes size=244429 B dims=1080x1920 mode=RGBA
SHARE fb-event received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 subject=80 г. от създаването на ЮНЕСКО text=80 г. от създаването на ЮНЕСКО 16 ноември 2026 НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец https://chyavorec.org/events clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-event-1794067829139.png mime=image/png bytes=244429 png=true dims=1080x1920
## P6. Article: toolbar and thread
FB article toolbar after scroll down: visible
## COMPLETE
## logcat findings
app pids seen: ['5569', '6475', '7502']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=33 total=33
== Davey: 1
   davey events=1 max=811ms median=811ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## a-live36

```
part live36; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. Device: Android 16 (SDK 36), 13894323
POST_PROMOTED_NOTIFICATIONS: 
## L1. Update download as a Live Update: 1.2.33-pre (versionCode 1488545) → the staged test 1.2.33 (1488546), throttled
staged: 1488546 1.2.33 4274690 urce/ch-app/emulator-results/v133/update/42388603303/app.apk
installed new-upd: 1.2.33-pre 6d3a324; versionCode 1488545; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v133/update/42388603303/update.json size 4274666;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.33-pre 
POST_PROMOTED_NOTIFICATIONS (pre build): 
THROTTLE on: -P INPUT ACCEPT -A INPUT -p tcp -m tcp --sport 443 -m hashlimit --hashlimit-above 64kb/s --hashlimit-burst 64kb --hashlimit-mode srcip --hashlimit-name pcdl -j DROP -A INPUT -j oem_in 
LIVE: no update dialog: text="Нова версия 1.2.33" text="Новата версия е изтеглена и проверена (контролна сума и подпис)." text="Какво ново" text="• „Моята читателска година“ — анимирана история за прочетеното през годината (
THROTTLE off: 0 rules left
## L2. Event reminder → event Live Update (60 min before; needs the event's start time)
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
EVENT: text="Михаил В. Ломоносов" text="8 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" text="НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец" text="315 г. 
CAP event-reminder (+2650 ms after the tap; capture took 2560 ms): WorkSpec:  WorkName: 
CAP event-reminder: jobs: 0 [ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~Pending queue:~Active jobs:~ Unsatisfied constraints:~ Unsatisfied constraints:~ Current work counts: Config={tot=12 mins={1=4, 2=2, 4=1, 8=1, 16=0, 32=0, 64=0~]; SystemForegroundService: 0 []; notif: ; dialog: 
WORK after the reminder: c96786c7 ENQUEUED attempts=0 PeriodicSyncWorker periods=0 gen=0 enq=1794067886695;158a80d4 SUCCEEDED attempts=1 MessagePollWorker periods=0 gen=0 enq=1794067887286;86e5a4a2 ENQUEUED attempts=0 MessagePollWorker periods=0 gen=0 enq=1794067889972;
EventLiveUpdateWorker scheduled: 0
notifications now: LIVE total for org.chyavorec.app: 0
## L3. API 36 smoke: ribbon and card foil render
API36 ribbon mean=209.5 std=67.1 thread-px=26.8%; change 3→9 s 0.07
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## COMPLETE
## logcat findings
app pids seen: ['4020', '6476', '6972']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=52 total=262
== Davey: 15
   davey events=15 max=1399ms median=817ms
== NativeLink: 3
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## a-motion19

```
part motion19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## R0. Reduced motion: animator/transition/window scale 0 (Developer options / „Премахни анимациите“)
scales: 0 0 0
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2772 ms
## R1. Home: the ribbon is drawn still (no sweep)
RM frames in the first 20 s on Home: 8
RM ribbon [0 280 1080 398] mean=209.5 std=67.0 thread-px=26.8%; change 2→6→12→20 s: 0.00 0.00 0.00 (≈0 = still)
## R2. Library card: no sweep
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
RM card: frames in 6 s 0; change 0.00
## R3. No confetti on 'Искам да прочета'
REC rm-want: 25 frames @8 fps; motion all: frames=25 moving-frames(>1.0)=1 max-step=11.4 max-vs-first=12.0 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.6 11.4 0.0]
RM confetti: confetti-like new saturated points per frame (max 2): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2]
RM want added: checked=true selected=false bounds=[426,738][911,864]
## R4. Nav rosette: static
REC rm-rosette: 41 frames @8 fps; motion 0 2070 1080 2400: 
## R5. Book open: no rotation
REC rm-bookopen: 27 frames @8 fps; motion 0 150 1080 2150: 
RM after book + back: catalogue shown
## R6. 'Изненадай ме' still works
RM surprise: text="Към книгата" 
## R7. Article: plain progress line, toolbar
RM article toolbar after scrolling down: visible
RM article toolbar after scrolling up: visible
## R8. Battery saver (animations ON): the ribbon must be still
battery saver: low_power=1;  mSettingBatterySaverEnabled=true
SAVER frames in 14 s: 16
SAVER ribbon change 3→8→14 s: 0.00 0.00 (≈0 = still)
## R9. Control: animations ON, no saver — the ribbon moves
CONTROL frames in 14 s: 108
CONTROL ribbon change 3→8→14 s: 4.42 4.52
## COMPLETE
## logcat findings
app pids seen: ['5909', '7086', '8522', '8700']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=55 total=181
== Davey: 6
   davey events=6 max=1855ms median=1030ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## a-year19

```
part year19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## Y0. devDebug (org.chyavorec.app.dev, demo data)
installed: devDebug 6d3a324 (org.chyavorec.app.dev, demo sign-in DEMO-0001);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.33-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5037 ms
## Y1. Demo sign-in (DEMO-0001 / demo)
MY after sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, ко
## Y2. The year card on „Моето“
YEAR card: text="Моята читателска година 2026" text="30 книги · виж историята" 
## Y3. Open the story: 8 slides, auto-advance 5.5 s (accessibility events of the slide state)
A11Y service: 11-07 16:10:31.682  7875  7875 I PCA11Y  : connected flags=80
YEAR state at ~35 s: state=Слайд 3 от 8
YEAR state at ~52 s (8 × 5.5 s = 44 s → last slide, stays): state=Слайд 6 от 8
SLIDE changes: 0; gaps (s): 
## Y4. Tap left (previous) ×2, tap right (next)
YEAR after 2 × left tap: state=Слайд 5 от 8
YEAR after a right tap: state=Слайд 7 от 8
## Y5. Hold (press 8 s) pauses the auto-advance
YEAR hold: before 'state=Слайд 8 от 8' → while held (~9 s) 'state=Слайд 8 от 8' → 7 s after release 'state=Слайд 8 от 8' (paused = same while held, advances after)
## Y6. Swipe down closes the story
YEAR after swipe down: story STILL OPEN; screen: content-desc="Благодарим ти, че четеш с нас! Библиотеката на читалището те очаква и през 2027 г." text="Моята читателска година" text="2026" content-desc="Затво
## Y7. Confetti on renewing a demo loan ('Удължи срока')
RENEW: no 'Удължи срока' in the demo loans: content-desc="Благодарим ти, че четеш с нас! Библиотеката на читалището те очаква и през 2027 г." text="Моята читателска година" text="2026" content-desc="Затвори" text="Сподели" 
## Y8. Confetti on 'Искам да прочета' (demo account)
REC confetti-want-dev: 34 frames @8 fps; motion all: frames=34 moving-frames(>1.0)=2 max-step=6.3 max-vs-first=12.0 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.6 0.0 5.2 6.3 0.4 0.4 0.0 0.4]
CONFETTI want (dev): confetti-like new saturated points per frame (max 40): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 10, 40, 36, 36, 36, 30]
## COMPLETE
## logcat findings
app pids seen: []
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-1

```
part abstart-1; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
animations ON for the start A/B (the ribbon runs at start)
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-0 FIRST-INSTALL catalogue results after 4275 ms
new-0 INDEX-READY #1 catalogue results after 4590 ms
new-0 INDEX-READY #2 catalogue results after 5228 ms
new-0 INDEX-READY #3 catalogue results after 4117 ms
START cold new-0         n=10 min=2074ms median=2750ms max=3251ms  all=[3251, 3138, 3052, 2074, 2715, 3096, 2568, 2681, 2785, 2455]  states=['COLD']
START warm new-0         n=8 min=521ms median=660ms max=760ms  all=[645, 676, 760, 521, 680, 711, 635, 576]  states=['HOT', 'UNKNOWN']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 3048 ms
base-1 INDEX-READY #1 catalogue results after 3376 ms
base-1 INDEX-READY #2 catalogue results after 3261 ms
base-1 INDEX-READY #3 catalogue results after 4155 ms
START cold base-1        n=10 min=2110ms median=2570ms max=2978ms  all=[2727, 2247, 2643, 2282, 2912, 2110, 2496, 2978, 2734, 2252]  states=['COLD']
START warm base-1        n=10 min=322ms median=564ms max=992ms  all=[781, 537, 914, 992, 322, 591, 377, 351, 477, 705]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2856 ms
new-1 INDEX-READY #1 catalogue results after 3290 ms
new-1 INDEX-READY #2 catalogue results after 3539 ms
new-1 INDEX-READY #3 catalogue results after 3672 ms
START cold new-1         n=10 min=2124ms median=2664ms max=3251ms  all=[2551, 2635, 3214, 3205, 2669, 3251, 2124, 2592, 2832, 2658]  states=['COLD']
START warm new-1         n=10 min=343ms median=640ms max=1053ms  all=[438, 856, 712, 511, 1053, 695, 755, 585, 343, 542]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 3071 ms
base-2 INDEX-READY #1 catalogue results after 3342 ms
base-2 INDEX-READY #2 catalogue results after 3210 ms
base-2 INDEX-READY #3 catalogue results after 3079 ms
START cold base-2        n=10 min=2132ms median=2513ms max=2825ms  all=[2323, 2819, 2516, 2510, 2532, 2181, 2330, 2825, 2534, 2132]  states=['COLD']
START warm base-2        n=10 min=362ms median=582ms max=973ms  all=[527, 528, 973, 621, 636, 733, 732, 362, 514, 544]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 3276 ms
new-2 INDEX-READY #1 catalogue results after 3451 ms
new-2 INDEX-READY #2 catalogue results after 3373 ms
new-2 INDEX-READY #3 catalogue results after 3365 ms
START cold new-2         n=10 min=2050ms median=2492ms max=3193ms  all=[2546, 2500, 2731, 2363, 2366, 2050, 3193, 2676, 2484, 2136]  states=['COLD']
START warm new-2         n=10 min=313ms median=625ms max=1025ms  all=[775, 313, 668, 827, 389, 582, 694, 1025, 475, 510]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.33    n=20 min=2050ms median=2614ms max=3251ms  all=[2551, 2635, 3214, 3205, 2669, 3251, 2124, 2592, 2832, 2658, 2546, 2500, 2731, 2363, 2366, 2050, 3193, 2676, 2484, 2136]  states=['COLD']
START cold BASE 1.2.32   n=20 min=2110ms median=2513ms max=2978ms  all=[2727, 2247, 2643, 2282, 2912, 2110, 2496, 2978, 2734, 2252, 2323, 2819, 2516, 2510, 2532, 2181, 2330, 2825, 2534, 2132]  states=['COLD']
START warm NEW 1.2.33    n=20 min=313ms median=626ms max=1053ms  all=[438, 856, 712, 511, 1053, 695, 755, 585, 343, 542, 775, 313, 668, 827, 389, 582, 694, 1025, 475, 510]  states=['HOT']
START warm BASE 1.2.32   n=20 min=322ms median=568ms max=992ms  all=[781, 537, 914, 992, 322, 591, 377, 351, 477, 705, 527, 528, 973, 621, 636, 733, 732, 362, 514, 544]  states=['HOT']
START catalogue first-install new n=2 min=2856ms median=3066ms max=3276ms  all=[2856, 3276]  states=[]
START catalogue index-ready new n=6 min=3290ms median=3412ms max=3672ms  all=[3290, 3539, 3672, 3451, 3373, 3365]  states=[]
START catalogue first-install base n=2 min=3048ms median=3060ms max=3071ms  all=[3048, 3071]  states=[]
START catalogue index-ready base n=6 min=3079ms median=3302ms max=4155ms  all=[3376, 3261, 4155, 3342, 3210, 3079]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5780', '6622', '7049', '7660', '8000', '8098', '8219', '8344', '8521', '8627', '8686', '8749', '8805', '8860', '9312', '9579', '9753', '9908', '10255', '10323', '10382', '10438', '10493', '10554', '10608', '10663', '10734', '10794', '11347', '11749', '11904', '12048', '12243', '12306', '12360', '12415', '12470', '12531', '12584', '12639', '12692', '12748', '13257', '13529', '13682', '13820', '14004', '14104', '14158', '14232', '14286', '14342', '14397', '14452', '14507', '14562', '14968', '15257', '15406', '15557', '15721', '15780', '15838', '15893', '15947', '16003', '16060', '16115', '16170', '16224']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 231
   skipped-frames events=231 max=107 total=11761
== Davey: 353
   davey events=353 max=3156ms median=1096ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
animations ON for the start A/B (the ribbon runs at start)
block order (the first, '-0', is a warm-up and is excluded): base-0 new-1 base-1 new-2 base-2
## AB base-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-0: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-0 FIRST-INSTALL catalogue results after 3575 ms
base-0 INDEX-READY #1 catalogue results after 3611 ms
base-0 INDEX-READY #2 catalogue results after 3564 ms
base-0 INDEX-READY #3 catalogue results after 2907 ms
START cold base-0        n=10 min=1232ms median=1514ms max=1733ms  all=[1470, 1733, 1503, 1524, 1586, 1730, 1404, 1232, 1566, 1376]  states=['COLD']
START warm base-0        n=10 min=164ms median=314ms max=569ms  all=[569, 214, 208, 296, 173, 345, 533, 332, 377, 164]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2858 ms
new-1 INDEX-READY #1 catalogue results after 2930 ms
new-1 INDEX-READY #2 catalogue results after 2745 ms
new-1 INDEX-READY #3 catalogue results after 3006 ms
START cold new-1         n=10 min=1549ms median=1780ms max=2061ms  all=[1549, 1838, 1777, 2061, 1746, 1942, 1740, 1684, 1937, 1782]  states=['COLD']
START warm new-1         n=10 min=155ms median=432ms max=650ms  all=[362, 590, 507, 370, 386, 619, 650, 155, 478, 210]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2689 ms
base-1 INDEX-READY #1 catalogue results after 2778 ms
base-1 INDEX-READY #2 catalogue results after 2969 ms
base-1 INDEX-READY #3 catalogue results after 2773 ms
START cold base-1        n=10 min=1347ms median=1640ms max=1916ms  all=[1537, 1901, 1666, 1733, 1916, 1862, 1593, 1465, 1614, 1347]  states=['COLD']
START warm base-1        n=10 min=170ms median=431ms max=520ms  all=[458, 513, 520, 170, 404, 224, 320, 398, 472, 476]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2767 ms
new-2 INDEX-READY #1 catalogue results after 2831 ms
new-2 INDEX-READY #2 catalogue results after 2772 ms
new-2 INDEX-READY #3 catalogue results after 2573 ms
START cold new-2         n=10 min=1622ms median=2066ms max=2229ms  all=[1808, 2171, 2136, 1622, 2136, 2229, 2033, 1993, 1921, 2100]  states=['COLD']
START warm new-2         n=10 min=158ms median=515ms max=729ms  all=[729, 596, 584, 623, 317, 446, 240, 625, 288, 158]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2864 ms
base-2 INDEX-READY #1 catalogue results after 2833 ms
base-2 INDEX-READY #2 catalogue results after 2872 ms
base-2 INDEX-READY #3 catalogue results after 2776 ms
START cold base-2        n=10 min=1526ms median=1750ms max=2088ms  all=[1526, 1757, 2088, 1709, 1778, 1928, 1742, 1667, 1692, 2055]  states=['COLD']
START warm base-2        n=10 min=252ms median=429ms max=674ms  all=[265, 674, 610, 272, 597, 446, 431, 278, 252, 427]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.33    n=20 min=1549ms median=1929ms max=2229ms  all=[1549, 1838, 1777, 2061, 1746, 1942, 1740, 1684, 1937, 1782, 1808, 2171, 2136, 1622, 2136, 2229, 2033, 1993, 1921, 2100]  states=['COLD']
START cold BASE 1.2.32   n=20 min=1347ms median=1721ms max=2088ms  all=[1537, 1901, 1666, 1733, 1916, 1862, 1593, 1465, 1614, 1347, 1526, 1757, 2088, 1709, 1778, 1928, 1742, 1667, 1692, 2055]  states=['COLD']
START warm NEW 1.2.33    n=20 min=155ms median=462ms max=729ms  all=[362, 590, 507, 370, 386, 619, 650, 155, 478, 210, 729, 596, 584, 623, 317, 446, 240, 625, 288, 158]  states=['HOT']
START warm BASE 1.2.32   n=20 min=170ms median=429ms max=674ms  all=[458, 513, 520, 170, 404, 224, 320, 398, 472, 476, 265, 674, 610, 272, 597, 446, 431, 278, 252, 427]  states=['HOT']
START catalogue first-install new n=2 min=2767ms median=2812ms max=2858ms  all=[2858, 2767]  states=[]
START catalogue index-ready new n=6 min=2573ms median=2802ms max=3006ms  all=[2930, 2745, 3006, 2831, 2772, 2573]  states=[]
START catalogue first-install base n=2 min=2689ms median=2776ms max=2864ms  all=[2689, 2864]  states=[]
START catalogue index-ready base n=6 min=2773ms median=2806ms max=2969ms  all=[2778, 2969, 2773, 2833, 2872, 2776]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['6148', '7277', '7598', '7962', '8273', '8339', '8396', '8452', '8508', '8579', '8634', '8689', '8744', '8800', '9228', '9533', '9694', '9847', '10187', '10249', '10304', '10362', '10418', '10474', '10532', '10587', '10644', '10700', '11129', '11392', '11536', '11683', '11828', '11911', '11971', '12027', '12084', '12144', '12200', '12257', '12312', '12369', '12863', '13185', '13330', '13482', '13629', '13698', '13754', '13808', '13865', '13919', '13974', '14042', '14099', '14154', '14628', '14888', '15032', '15218', '15362', '15430', '15487', '15542', '15598', '15651', '15706', '15761', '15814', '15869']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 151
   skipped-frames events=151 max=88 total=6074
== Davey: 238
   davey events=238 max=2469ms median=949ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-3

```
part abstart-3; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
animations OFF (as in v132)
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-0 FIRST-INSTALL catalogue results after 3262 ms
new-0 INDEX-READY #1 catalogue results after 3137 ms
new-0 INDEX-READY #2 catalogue results after 4018 ms
new-0 INDEX-READY #3 catalogue results after 2551 ms
START cold new-0         n=10 min=1377ms median=1692ms max=2645ms  all=[2402, 2004, 2645, 1480, 2254, 1784, 1377, 1600, 1456, 1434]  states=['COLD']
START warm new-0         n=10 min=383ms median=456ms max=847ms  all=[413, 419, 461, 601, 451, 412, 383, 497, 847, 590]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 3060 ms
base-1 INDEX-READY #1 catalogue results after 3003 ms
base-1 INDEX-READY #2 catalogue results after 2640 ms
base-1 INDEX-READY #3 catalogue results after 2521 ms
START cold base-1        n=10 min=1115ms median=1444ms max=1630ms  all=[1412, 1322, 1441, 1618, 1115, 1630, 1447, 1379, 1460, 1550]  states=['COLD']
START warm base-1        n=10 min=299ms median=498ms max=749ms  all=[749, 504, 405, 299, 300, 493, 639, 603, 609, 415]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2869 ms
new-1 INDEX-READY #1 catalogue results after 2661 ms
new-1 INDEX-READY #2 catalogue results after 2733 ms
new-1 INDEX-READY #3 catalogue results after 2713 ms
START cold new-1         n=10 min=1353ms median=1564ms max=1909ms  all=[1656, 1698, 1666, 1447, 1543, 1567, 1561, 1353, 1909, 1534]  states=['COLD']
START warm new-1         n=10 min=282ms median=424ms max=562ms  all=[562, 475, 413, 450, 396, 420, 428, 323, 554, 282]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2770 ms
base-2 INDEX-READY #1 catalogue results after 2573 ms
base-2 INDEX-READY #2 catalogue results after 2610 ms
base-2 INDEX-READY #3 catalogue results after 2647 ms
START cold base-2        n=10 min=1284ms median=1486ms max=1655ms  all=[1637, 1655, 1284, 1550, 1320, 1543, 1463, 1508, 1387, 1457]  states=['COLD']
START warm base-2        n=10 min=368ms median=542ms max=729ms  all=[479, 633, 490, 661, 578, 716, 729, 368, 506, 424]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2611 ms
new-2 INDEX-READY #1 catalogue results after 2777 ms
new-2 INDEX-READY #2 catalogue results after 2788 ms
new-2 INDEX-READY #3 catalogue results after 2544 ms
START cold new-2         n=10 min=1341ms median=1502ms max=1769ms  all=[1492, 1341, 1512, 1769, 1545, 1545, 1638, 1439, 1390, 1407]  states=['COLD']
START warm new-2         n=10 min=318ms median=501ms max=937ms  all=[707, 318, 488, 498, 426, 937, 649, 668, 504, 481]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.33    n=20 min=1341ms median=1544ms max=1909ms  all=[1656, 1698, 1666, 1447, 1543, 1567, 1561, 1353, 1909, 1534, 1492, 1341, 1512, 1769, 1545, 1545, 1638, 1439, 1390, 1407]  states=['COLD']
START cold BASE 1.2.32   n=20 min=1115ms median=1458ms max=1655ms  all=[1412, 1322, 1441, 1618, 1115, 1630, 1447, 1379, 1460, 1550, 1637, 1655, 1284, 1550, 1320, 1543, 1463, 1508, 1387, 1457]  states=['COLD']
START warm NEW 1.2.33    n=20 min=282ms median=478ms max=937ms  all=[562, 475, 413, 450, 396, 420, 428, 323, 554, 282, 707, 318, 488, 498, 426, 937, 649, 668, 504, 481]  states=['HOT']
START warm BASE 1.2.32   n=20 min=299ms median=505ms max=749ms  all=[749, 504, 405, 299, 300, 493, 639, 603, 609, 415, 479, 633, 490, 661, 578, 716, 729, 368, 506, 424]  states=['HOT']
START catalogue first-install new n=2 min=2611ms median=2740ms max=2869ms  all=[2869, 2611]  states=[]
START catalogue index-ready new n=6 min=2544ms median=2723ms max=2788ms  all=[2661, 2733, 2713, 2777, 2788, 2544]  states=[]
START catalogue first-install base n=2 min=2770ms median=2915ms max=3060ms  all=[3060, 2770]  states=[]
START catalogue index-ready base n=6 min=2521ms median=2625ms max=3003ms  all=[3003, 2640, 2521, 2573, 2610, 2647]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5240', '6114', '6835', '7320', '7756', '7910', '8040', '8150', '8238', '8291', '8428', '8489', '8550', '8604', '9070', '9332', '9479', '9625', '9775', '9846', '9901', '9963', '10019', '10077', '10132', '10186', '10240', '10295', '10850', '11129', '11280', '11438', '11588', '11656', '11712', '11768', '11825', '11878', '11934', '11989', '12045', '12159', '12667', '12973', '13121', '13266', '13417', '13484', '13539', '13596', '13649', '13706', '13762', '13819', '13874', '13931', '14338', '14609', '14757', '14901', '15048', '15115', '15169', '15278', '15355', '15411', '15481', '15536', '15594', '15649']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 72
   skipped-frames events=72 max=70 total=2817
== Davey: 87
   davey events=87 max=1735ms median=879ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## article19

```
part article19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160886 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5534 ms
longest article: https://chyavorec.org/news/digitalen-klub-v-narodno-chitalishte-vasil-levski-1922
## B4. Article: stitched progress thread, hero parallax, floating toolbar (Аа / bookmark / share / browser)
ARTICLE toolbar at open: content-desc="Режим за четене" content-desc="Запази" content-desc="Сподели" content-desc="Отвори в сайта" 
ARTICLE hero parallax: top area diff after a small scroll 28.63
ARTICLE toolbar after a further short scroll down (~480 px): hidden
ARTICLE toolbar after two long scrolls: visible
ARTICLE toolbar after scrolling up: visible
ARTICLE thread (top 260 px) changed with reading: 5.96
## B3b. Reader text size (connected button group in the 'Аа' sheet)
READER sizes: Малък шрифт[checked=false] Нормален шрифт[checked=true] Голям шрифт[checked=false] Много голям шрифт[checked=false] 
READER after 'Голям шрифт': Нормален шрифт[checked=false] Голям шрифт[checked=true] 
ARTICLE bookmark: now content-desc="Премахни от запазените"
## B11c. Article toolbar: share as image
SHARE article sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE article: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 11-07 17:28:24.439  8184  8184 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@fc16; boundsInParent: R~
SHARE article cache/share: 839596 2026-11-07 17:28 chyavorec-news-1794072513145.png;
SHARE article PNG: chyavorec-news-1794072513145.png → PNG-signature=yes size=839596 B dims=1080x1350 mode=RGBA
SHARE article received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-news-1794072513145.png mime=image/png bytes=839596 png=true dims=1080x1350 subject=Дигитален клуб в Народно читалище „Васил Левски–1922“ text=Дигитален клуб в Народно читалище „Васил Левски–1922“ https://chyavorec.org/news/digitalen-klub-v-narodno-chitalishte-vasil-levski-1922
ARTICLE 'Отвори в сайта' → fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
## COMPLETE
## logcat findings
app pids seen: ['5307']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=70 total=730
== Davey: 44
   davey events=44 max=2593ms median=910ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## b-live361

```
part live361; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. Device: Android 16 (SDK 36), 14574095
POST_PROMOTED_NOTIFICATIONS: android.permission.POST_PROMOTED_NOTIFICATIONS: granted=true
## L1. Update download as a Live Update: 1.2.33-pre (versionCode 1488545) → the staged test 1.2.33 (1488546), throttled
staged: 1488546 1.2.33 4274690 urce/ch-app/emulator-results/v133/update/42392073324/app.apk
installed new-upd: 1.2.33-pre 6d3a324; versionCode 1488545; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v133/update/42392073324/update.json size 4274666;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.33-pre 
POST_PROMOTED_NOTIFICATIONS (pre build): android.permission.POST_PROMOTED_NOTIFICATIONS: granted=true
network: wifi 0; Active default network: 101; 2 PROMOTED lines in dumpsys package
hashlimit rule failed (hashlimit: Could not determine whether revision 3 is supported, assuming it is.
iptables v1.8.11 (legacy): can't initialize iptables table `filter': Permission denied (you must be root)
Perhaps iptables or your kernel needs to be upgraded.) — packet limit instead
THROTTLE on: iptables v1.8.11 (legacy): can't initialize iptables table `filter': Permission denied (you must be root) Perhaps iptables or your kernel needs to be upgraded. 
LIVE update notification at +3762 ms:
LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 86%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (86) requestPromotedOngoing=Boolean (true) shortCriticalText=String (86%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
   android.requestPromotedOngoing=Boolean (true)
   android.template=String (android.app.Notification$ProgressStyle)
   android.shortCriticalText=String (86%)
LIVE total for org.chyavorec.app: 1
SHADE: text="Обновено до версия 1.2.33" 
LIVE at +29721 ms: LIVE id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) template=String (android.app.Notification$BigTextStyle) progress=Integer (0) requestPromotedOngoing=- shortCriticalText=null chronometer=Boolean (false) flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
LIVE last capture: LIVE id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) template=String (android.app.Notification$BigTextStyle) progress=Integer (0) requestPromotedOngoing=- shortCriticalText=null chronometer=Boolean (false) flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
  dumpsys:      28                 android.shortCriticalText=null
  dumpsys:       1     NotificationRecord(0x02d42266: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
  dumpsys:       1       originalFlags=ONGOING_EVENT|ONLY_ALERT_ONCE|FOREGROUND_SERVICE|PROMOTED_ONGOING
  dumpsys:       1       flags=ONGOING_EVENT|ONLY_ALERT_ONCE|NO_CLEAR|FOREGROUND_SERVICE|PROMOTED_ONGOING
  dumpsys:       1                 android.template=String (android.app.Notification$ProgressStyle)
  dumpsys:       1                 android.shortCriticalText=String (86%)
  dumpsys:       1                 android.requestPromotedOngoing=Boolean (true)
THROTTLE off: 0 rules left
l-finish +121262 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +145639 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +170013 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +194397 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +218770 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +243154 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +267561 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +291931 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +316302 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +340671 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish +365035 ms: NOTIF id=700 title=String (Обновено до версия 1.2.33) text=String (Докосни, за да отвориш приложението.) progress=Integer (0)/Integer (0) indeterminate=Boolean ; files [;]
l-finish RESULT: none after 385292 ms (first progress notification at +never ms); app process '4926'; files [;]
## L1b. Install the downloaded update (system installer) → 1.2.33
no 'готова за инсталиране' in the shade: text="Обновено до версия 1.2.33" text="Докосни, за да отвориш приложението." text="Chitalishte Yavorets" text="•" text="6 minutes ago" content-desc="Collapse" text="Silent" content-desc="Clear all sil
## L2. Event reminder → event Live Update (60 min before; needs the event's start time)
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.32" text="Излезе нова версия на приложението." text="Размер: 4,2 MB" )
EVENT: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" text="НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец" t
REMINDER set: text="Ще получиш напомняне на 15 ноември 2026, 18:00."; bell: on
CAP event-reminder (+2489 ms after the tap; capture took 2346 ms): WorkSpec:  WorkName: 
CAP event-reminder: jobs: 0 [ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~Pending queue:~Active jobs:~ Unsatisfied constraints:~ Unsatisfied constraints:~]; SystemForegroundService: 0 []; notif: ; dialog: 
WORK after the reminder: 
EventReminderWorker scheduled: 0; EventLiveUpdateWorker scheduled: 0
notifications now: LIVE total for org.chyavorec.app: 0
## L3. API 36 smoke: ribbon and card foil render
API36 ribbon mean=209.5 std=67.0 thread-px=26.8%; change 3→9 s 0.00
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## COMPLETE
## logcat findings
app pids seen: ['4447', '4926', '6226', '6837']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=33 total=33
== Davey: 5
   davey events=5 max=804ms median=765ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## c-surprise28

```
part surprise28; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160886 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: 
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
first-install catalogue results after 2733 ms
## SM0. Device memory: MemTotal: 2046996 kB; API 28
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
no dice button
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
no dice button
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
no dice button
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 36; app RSS 182→max 186 MB at +2539 ms; min MemAvailable 1146 MB at +1579 ms
## COMPLETE
## logcat findings
app pids seen: ['5366']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=37 total=74
== Davey: 2
   davey events=2 max=930ms median=858ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## cardcrash

```
part cardcrash; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
## CC-new. 5 cycles: cleared data, first launch, empty card screen, add the number, full-screen card
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-1: FATAL 0; card shown yes; app in front yes; 5 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~5 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-2: FATAL 0; card shown yes; app in front yes; 9 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~9 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-3: FATAL 0; card shown yes; app in front yes; 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-4: FATAL 0; card shown yes; app in front yes; 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-5: FATAL 0; card shown yes; app in front yes; 4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
installed base: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
## CC-base. 5 cycles: cleared data, first launch, empty card screen, add the number, full-screen card
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-1: FATAL 0; card shown yes; app in front yes; 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-2: FATAL 0; card shown yes; app in front yes; 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-3: FATAL 0; card shown yes; app in front yes; 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-4: FATAL 0; card shown yes; app in front yes; 0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-5: FATAL 0; card shown yes; app in front yes; 
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
## CC-new. 5 cycles: cleared data, first launch, empty card screen, add the number, full-screen card
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-1: FATAL 0; card shown yes; app in front yes; 5 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~5 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-2: FATAL 0; card shown yes; app in front yes; 9 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~9 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-3: FATAL 0; card shown yes; app in front yes; 4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-4: FATAL 0; card shown yes; app in front yes; 4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH new-5: FATAL 0; card shown yes; app in front yes; 4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~4 W PeoplePU: java.lang.IllegalArgumentException: Unknown component: ComponentInfo{com.google.android.syncadapt~
installed base: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
## CC-base. 5 cycles: cleared data, first launch, empty card screen, add the number, full-screen card
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-1: FATAL 0; card shown yes; app in front yes; 0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-2: FATAL 0; card shown yes; app in front yes; 0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-3: FATAL 0; card shown yes; app in front yes; 0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-4: FATAL 0; card shown yes; app in front yes; 0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~0 W err     : java.lang.IllegalArgumentException: Component class com.google.android.apps.docs.sync.syncadapter~
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARDCRASH base-5: FATAL 0; card shown yes; app in front yes; 
## CC. Totals
CARDCRASH total new: 0 of 10 cycles with a crash
CARDCRASH total base: 0 of 10 cycles with a crash
All FATAL in logcat: 0
## COMPLETE
## logcat findings
app pids seen: ['5810', '7755', '8654', '9102', '9594', '10379', '10808', '11253', '11690', '12231', '12934', '13386', '13828', '14266', '14805', '15424', '15860', '16281', '16713', '17148']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 75
   skipped-frames events=75 max=105 total=3577
== Davey: 149
   davey events=149 max=2633ms median=950ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## confetti19

```
part confetti19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160886 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3526 ms
## B9. Confetti + haptics on 'Искам да прочета' (book screen)
WANT before: checked=false selected=false bounds=[426,738][911,864]
REC confetti-want: 18 frames @8 fps; motion all: frames=18 moving-frames(>1.0)=1 max-step=11.4 max-vs-first=12.3 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.6 11.4 0.3 0.0 0.4 0.4 0.5 0.4 0.2]
CONFETTI want-to-read: confetti-like new saturated points per frame (max 48): [0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 12, 48, 48, 38, 39, 37, 7, 2]
WANT after: checked=true selected=false bounds=[426,738][911,864]; snackbar: text="Искам да прочета" 
HAPTIC after the add:     createTime: 11-07 17:26:59.312, startTime: 11-07 17:26:59.453, endTime: 11-07 17:26:59.591, durationMs: 279, status: finished, effect: Mono{mEffect=Composed{segments=[Prebaked{effect=CLICK, streng~
## B11b. Book: share as image, then share the link
SHARE book sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE book: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 11-07 17:27:10.633  6243  7447 I AppLinksHostsVerifierV2: Verification org.chyavorec.app complete. Successful hosts: . Failed hosts: chyavorec.org. Error hosts:~
SHARE book cache/share: 774403 2026-11-07 17:27 chyavorec-book-399560-1794072439666.png;
SHARE book PNG: chyavorec-book-399560-1794072439666.png → PNG-signature=yes size=774403 B dims=1080x1920 mode=RGBA
SHARE book received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794072439666.png mime=image/png bytes=774403 png=true dims=1080x1920 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
SHARE book-link (link): chooser OPEN (fg android/com.android.internal.app.ChooserActivity)
SHARE book-link received by the helper: action=android.intent.action.SEND type=text/plain flags=0x1b080001 clipItems=1 stream=null subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560
## B10. Event: reminder set (confetti) and share as image
EVENT: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Биб
REC confetti-reminder: 18 frames @8 fps; motion all: frames=18 moving-frames(>1.0)=3 max-step=7.9 max-vs-first=11.9 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.3 7.9 2.8 1.6 0.5 0.4 0.5 0.5 0.4 0.2 0.1]
CONFETTI reminder: confetti-like new saturated points per frame (max 37): [0, 0, 0, 0, 0, 0, 0, 0, 8, 37, 37, 32, 30, 37, 28, 8, 0, 0]
REMINDER: ; bell: on
SHARE event sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE event: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 
SHARE event cache/share: 654869 2026-11-07 17:28 chyavorec-event-1794072528813.png;
SHARE event PNG: chyavorec-event-1794072528813.png → PNG-signature=yes size=654869 B dims=1080x1920 mode=RGBA
SHARE event received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-event-1794072528813.png mime=image/png bytes=654869 png=true dims=1080x1920 subject=80 г. от създаването на ЮНЕСКО text=80 г. от създаването на ЮНЕСКО 16 ноември 2026 НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец https://chyavorec.org/events
## COMPLETE
## logcat findings
app pids seen: ['5967']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 8
   skipped-frames events=8 max=86 total=365
== Davey: 10
   davey events=10 max=2032ms median=966ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## drawn19

```
part drawn19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## F-base-1. Install base, first launch, warm-up; 6 cold starts online with 'Fully drawn' (animations ON)
installed base-1: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
DRAWN base-1 fresh install (onboarding): Fully drawn org.chyavorec.app/.MainActivity: +4s497ms
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## F-new-1. Install new, first launch, warm-up; 6 cold starts online with 'Fully drawn' (animations ON)
installed new-1: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
DRAWN new-1 fresh install (onboarding): Fully drawn org.chyavorec.app/.MainActivity: +3s726ms
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## F-base-2. Install base, first launch, warm-up; 6 cold starts online with 'Fully drawn' (animations ON)
installed base-2: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
DRAWN base-2 fresh install (onboarding): Fully drawn org.chyavorec.app/.MainActivity: +2s312ms
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## F-new-2. Install new, first launch, warm-up; 6 cold starts online with 'Fully drawn' (animations ON)
installed new-2: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
DRAWN new-2 fresh install (onboarding): Fully drawn org.chyavorec.app/.MainActivity: +2s629ms
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## F. Totals (12 cold starts per build)
DRAWN new: TotalTime n=12 min=1995ms median=2626ms max=2953ms; fully drawn n=12 median=3262ms min=2470 max=3872 all=[3207, 2696, 2996, 3447, 3303, 2470, 2696, 3425, 3227, 3296, 3872, 3609]; without line: 0
DRAWN base: TotalTime n=12 min=2416ms median=2914ms max=4155ms; fully drawn n=12 median=3696ms min=2684 max=5372 all=[5372, 4112, 3748, 5205, 5117, 4054, 3059, 2684, 3080, 3137, 3644, 3044]; without line: 0
## COMPLETE
## logcat findings
app pids seen: ['5621', '7238', '7396', '7557', '7712', '7937', '8026', '8320', '8993', '9051', '9104', '9190', '9251', '9311', '9489', '9850', '9906', '9964', '10022', '10079', '10171', '10366', '10728', '10794', '10851', '10922', '10979', '11035']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 99
   skipped-frames events=99 max=119 total=5098
== Davey: 149
   davey events=149 max=2765ms median=1098ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## fb28

```
part fb28; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2727 ms
## P1. Home ribbon on API 28 (no AGSL): the still pattern, drawn once
FB frames in 18 s: 151
FB ribbon [0 215 1080 333] mean=208.8 std=67.2 thread-px=27.4%; change 3→10→18 s: 0.38 0.50
## P2. Library card: fallback foil gradients
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
FB card: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P3. 'Изненадай ме'
FB surprise: ; bottom bar hidden
## P4. Book open
REC fb-bookopen: 38 frames @8 fps; motion 0 150 1080 2150: frames=38 moving-frames(>1.0)=5 max-step=21.4 max-vs-first=30.4 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.6 0.4 20.0 0.1 0.4 0.0 18.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 1.3 0.0 0.0 0.0 21.4 0.0 0.0 14.6 0.0]
## P5. Event: share as image
SHARE fb-event sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE fb-event: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 
SHARE fb-event cache/share: 244427 2026-11-07 16:51 chyavorec-event-1794070315599.png;
SHARE fb-event PNG: chyavorec-event-1794070315599.png → PNG-signature=yes size=244427 B dims=1080x1920 mode=RGBA
SHARE fb-event received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-event-1794070315599.png mime=image/png bytes=244427 png=true dims=1080x1920 subject=80 г. от създаването на ЮНЕСКО text=80 г. от създаването на ЮНЕСКО 16 ноември 2026 НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец https://chyavorec.org/events
## P6. Article: toolbar and thread
FB article toolbar after scroll down: visible
## COMPLETE
## logcat findings
app pids seen: ['5680', '6562', '7673']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=58 total=224
== Davey: 6
   davey events=6 max=1133ms median=896ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func19

```
part func19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4221 ms
## B1. Home ribbon „жива шевица“ (AGSL, API 33): renders, sweeps ~24 s, then stops
RIBBON frames in the first 34 s: 273
RIBBON box [0 280 1080 398] at 02 s: 
RIBBON box [0 280 1080 398] at 12 s: mean=209.5 std=67.0 thread-px=26.7%
RIBBON box [0 280 1080 398] at 34 s: mean=209.5 std=67.0 thread-px=26.7%
RIBBON change between shots (2,5,8,12,16,20,24,28,34 s): 0.56 0.00 0.00 0.00 0.00 0.00 0.00
RIBBON still after the budget (34 s → 40 s): diff 0.00
## B1b. The first scroll stops the ribbon (new process)
RIBBON after the first scroll: change a→b→c 0.00 0.00 (≈0 = stopped)
## B1c. Once per process: back to Home from another tab — no new sweep
RIBBON after a tab round trip: change 43.26
RIBBON shader errors in logcat: 1
## B2. Bottom-nav rosette morph (recording while switching tabs)
REC rosette: 47 frames @8 fps; motion 0 2070 1080 2400: 
ROSETTE: tab selected after the round: TAB selected: ['Начало']
## B3. Connected button groups: News / Calendar
GROUP news/calendar before: News[checked=true selected=false bounds=[42,307][537,433]] Calendar[checked=false selected=false bounds=[542,307][1038,433]] subscribe-action=absent
GROUP after tapping 'Календар': News[checked=false selected=false bounds=[42,307][422,433]] Calendar[checked=true selected=false bounds=[427,307][807,433]] subscribe-action=visible
GROUP after tapping 'Новини': News[checked=true selected=false bounds=[42,307][537,433]] Calendar[checked=false selected=false bounds=[542,307][1038,433]] subscribe-action=absent
GROUP a11y: checkable="false" checked="false" clickable="false" selected="false" checkable="false" checked="false" clickable="false" selected="false" 
## B7. Pull-to-refresh rosette (News list), shots during a slow pull
REFRESH: top area diff pull vs after: 0.00
## B8. Empty state with the stitched band: Want to read (empty on a fresh install)
EMPTY want: text="Списъкът е празен" text="Отбележи книга с „Искам да прочета“ на страницата ѝ в каталога — тук ще виждаш дали е на рафта." content-desc="Назад" text="Искам да прочета" 
## B4. Article: stitched progress thread, hero parallax, floating toolbar (Аа / bookmark / share / browser)
ARTICLE toolbar at open: content-desc="Режим за четене" content-desc="Запази" content-desc="Сподели" content-desc="Отвори в сайта" 
ARTICLE hero parallax: top area diff after a small scroll 92.01
ARTICLE toolbar after scrolling down: VISIBLE (expected hidden)
ARTICLE toolbar after scrolling up: visible
ARTICLE thread (top 260 px) changed with reading: 5.96
## B3b. Reader text size (connected button group in the 'Аа' sheet)
READER sizes: Малък шрифт[checked=false] Нормален шрифт[checked=true] Голям шрифт[checked=false] Много голям шрифт[checked=false] 
READER after 'Голям шрифт': Нормален шрифт[checked=false] Голям шрифт[checked=true] 
ARTICLE bookmark: now content-desc="Премахни от запазените"
## B11c. Article toolbar: share as image
SHARE article sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE article: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 11-07 16:14:29.811  9200  9200 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@19729; boundsInParent: ~
SHARE article cache/share: 1169798 2026-11-07 16:14 chyavorec-news-1794068078931.png;
SHARE article PNG: chyavorec-news-1794068078931.png → PNG-signature=yes size=1169798 B dims=1080x1350 mode=RGBA
SHARE article received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 subject=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 text=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 https://chyavorec.org/news/grupata-za-narodni-tantsi-pri-chitalishteto-ff-vrabevo-v-ritmi-2026 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-news-1794068078931.png mime=image/png bytes=1169798 png=true dims=1080x1350
ARTICLE 'Отвори в сайта' → fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
## B5. Gallery viewer floating toolbar: share / open on site / photo info
VIEWER toolbar: content-desc="Сподели" content-desc="Отвори в сайта" content-desc="За снимката" 
VIEWER 'За снимката': content-desc="Сградата на читалището в Яворец" content-desc="Затвори" text="1 / 29  ·  Сградата на читалището в Яворец" text="Сградата на читалището в Яворец" content-desc="Сподели" content-desc="Отвори в сайта" content-desc="За снимката" 
VIEWER closed after share? content-desc="Сградата на читалището в Яворец" content-desc="Фолклорен състав при читалището" content-desc="Хорът на сцена" content-desc="70-те години" content-
## B6. Book open: catalogue → book (cover rotates around the spine) and back (recording)
REC bookopen: 44 frames @8 fps; motion 0 150 1080 2150: 
BOOKOPEN after back: catalogue shown
REC bookopen-home: 44 frames @8 fps; motion all: frames=44 moving-frames(>1.0)=0 max-step=0.1 max-vs-first=0.1 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0]
## B9. Confetti + haptics on 'Искам да прочета' (book screen)
WANT before: checked=false selected=false bounds=[426,738][911,864]
REC confetti-want: 31 frames @8 fps; motion all: frames=31 moving-frames(>1.0)=1 max-step=11.5 max-vs-first=12.3 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.6 11.5 0.0 0.2]
CONFETTI want-to-read: confetti-like new saturated points per frame (max 43): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 12, 12, 43]
WANT after: checked=true selected=false bounds=[426,738][911,864]; snackbar: text="Искам да прочета" 
HAPTIC after the add:     createTime: 11-07 16:14:15.029, startTime: 11-07 16:14:15.030, endTime: 11-07 16:14:15.130, durationMs: 101, status: finished, effect: Mono{mEffect=Composed{segments=[Prebaked{effect=TICK, strengt~    createTime: 11-07 16:17:06.961, startTime: 11-07 16:17:06.965, endTime: 11-07 16:17:07.076, durationMs: 115, status: finished, effect: Mono{mEffect=Composed{segments=[Prebaked{effect=CLICK, streng~
## B11b. Book: share as image, then share the link
SHARE book sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE book: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 
SHARE book cache/share: 774403 2026-11-07 16:17 chyavorec-book-399560-1794068249804.png;
SHARE book PNG: chyavorec-book-399560-1794068249804.png → PNG-signature=yes size=774403 B dims=1080x1920 mode=RGBA
SHARE book received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-book-399560-1794068249804.png mime=image/png bytes=774403 png=true dims=1080x1920
SHARE book-link (link): chooser OPEN (fg android/com.android.internal.app.ChooserActivity)
SHARE book-link received by the helper: action=android.intent.action.SEND type=text/plain flags=0x1b080001 subject=12 -те вятъра text=12 -те вятъра Гарфилд, Джудит и др. В електронния каталог: https://chyavorec.org/elektronen-katalog#zapis=399560 clipItems=1 stream=null
## B10. Event: reminder set (confetti) and share as image
EVENT: text="Михаил В. Ломоносов" text="8 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" 
EVENT: no 'Напомни ми' (past event or none)
SHARE event sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE event: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 
SHARE event cache/share: 636560 2026-11-07 16:19 chyavorec-event-1794068339896.png;
SHARE event PNG: chyavorec-event-1794068339896.png → PNG-signature=yes size=636560 B dims=1080x1920 mode=RGBA
SHARE event received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 subject=Михаил В. Ломоносов text=Михаил В. Ломоносов 8 ноември 2026 НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец https://chyavorec.org/events clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-event-1794068339896.png mime=image/png bytes=636560 png=true dims=1080x1920
## B12. 'Изненадай ме': the dice button in the catalogue
REC surprise-dice: 46 frames @8 fps; motion all: frames=46 moving-frames(>1.0)=3 max-step=173.6 max-vs-first=200.8 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.0 0.0 4.6 0.0 0.0 0.0 0.0 173.6 0.1 0.0 0.0 0.0 0.0 0.0 28.8 0.0]
SURPRISE dice: text="Още една" text="Към книгата" ; bottom bar hidden
SURPRISE 'Към книгата' → book screen yes: text="Дъхът на планините" text="разказ за дома" text="Ангел Василев" 
## B13. 'Изненадай ме' by shaking (emulator accelerometer: two peaks > 2.3 g within 0.7 s)
accelerometer: 0000000000) Goldfish 3-axis Accelerometer | The Android Open Source Project | ver: 1 | type: android.sensor.acceleromete
SHAKE one short peak (info; a peak held > 120 ms counts twice by design): not triggered
SHAKE double peak: TRIGGERED; text="Към книгата" 
## B13b. Shake on another tab (catalogue not resumed) must not trigger
WARN: tab bar not reached
SHAKE on Home: TRIGGERED (unexpected)
## COMPLETE
## logcat findings
app pids seen: ['5244', '6157', '7836']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 47
   skipped-frames events=47 max=110 total=2215
== Davey: 92
   davey events=92 max=3395ms median=974ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func19b

```
part func19b; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4520 ms
## B1. Home ribbon „жива шевица“ (AGSL, API 33): renders, sweeps ~24 s, then stops
RIBBON frames in the first 34 s: 309
RIBBON box [0 280 1080 398] at 03 s: mean=209.4 std=67.0 thread-px=26.8%
RIBBON box [0 280 1080 398] at 12 s: mean=212.2 std=63.4 thread-px=26.4%
RIBBON box [0 280 1080 398] at 34 s: mean=209.5 std=67.0 thread-px=26.7%
RIBBON change between shots (3,5,8,12,16,20,24,28,34 s): 4.43 3.86 6.86 3.16 0.83 0.00 0.00 0.00
RIBBON still after the budget (34 s → 40 s): diff 0.00
## B1b. The first scroll stops the ribbon (new process)
RIBBON after the first scroll: change a→b→c 0.00 0.00 (≈0 = stopped)
## B1c. Once per process: back to Home from another tab — no new sweep
RIBBON after a tab round trip: change 43.26
RIBBON shader errors in logcat: 1
## B2. Bottom-nav rosette morph (recording while switching tabs)
REC rosette: 51 frames @8 fps; motion 0 2070 1080 2400: frames=51 moving-frames(>1.0)=5 max-step=12.6 max-vs-first=13.3 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.4 0.0 0.0 0.0 0.0 12.6 0.0 0.0 0.0 0.0 0.0 0.5 0.0 2.4 0.2 0.0 0.0 0.0 0.8 0.0 0.0 2.2 0.1 0.0 0.1 0.0 0.0 0.0 0.2 1.8 0.0 0.1 0.1 0.0 0.1 0.0 0.0 0.7 0.0 0.0 0.0 5.4 0.0 0.0]
ROSETTE: tab selected after the round: TAB selected: ['Начало']
## B7. Pull-to-refresh rosette (News list), shots during a slow pull
REFRESH: top area diff before vs pull 0.37, pull vs after 0.37
## B4. Article: stitched progress thread, hero parallax, floating toolbar (Аа / bookmark / share / browser)
ARTICLE toolbar at open: content-desc="Режим за четене" content-desc="Запази" content-desc="Сподели" content-desc="Отвори в сайта" 
ARTICLE hero parallax: top area diff after a small scroll 92.01
ARTICLE toolbar after a further short scroll down (~480 px): VISIBLE (expected hidden)
ARTICLE toolbar after two long scrolls: visible
ARTICLE toolbar after scrolling up: visible
ARTICLE thread (top 260 px) changed with reading: 5.96
## B3b. Reader text size (connected button group in the 'Аа' sheet)
READER sizes: Малък шрифт[checked=false] Нормален шрифт[checked=true] Голям шрифт[checked=false] Много голям шрифт[checked=false] 
READER after 'Голям шрифт': Нормален шрифт[checked=false] Голям шрифт[checked=true] 
ARTICLE bookmark: now content-desc="Премахни от запазените"
## B11c. Article toolbar: share as image
SHARE article sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE article: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 11-07 16:53:16.838  9111  9111 I AccessibilityNodeInfoDumper: Skipping invisible child: android.view.accessibility.AccessibilityNodeInfo@1660e; boundsInParent: ~
SHARE article received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-news-1794070405071.png mime=image/png bytes=1169798 png=true dims=1080x1350 subject=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 text=Групата за народни танци при читалището, ФФ „Врабево в ритми“ 2026 https://chyavorec.org/news/grupata-za-narodni-tantsi-pri-chitalishteto-ff-vrabevo-v-ritmi-2026
ARTICLE 'Отвори в сайта' → fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
## B5. Gallery viewer floating toolbar: share / open on site / photo info
VIEWER toolbar: content-desc="Сподели" content-desc="Отвори в сайта" content-desc="За снимката" 
VIEWER 'За снимката': content-desc="Сградата на читалището в Яворец" content-desc="Затвори" text="1 / 29  ·  Сградата на читалището в Яворец" text="Сградата на читалището в Яворец" content-desc="Сподели" content-desc="Отвори в сайта" content-desc="За снимката" 
VIEWER after the 2nd 'За снимката' tap: viewer open
VIEWER 'Сподели' → fg android/com.android.internal.app.ChooserActivity; sheet: 
SHARE viewer received by the helper: action=android.intent.action.SEND type=text/plain flags=0x1b080001 clipItems=1 stream=null subject=Сградата на читалището в Яворец text=Сградата на читалището в Яворец https://chyavorec.org/fotodokumentalna-izlozhba
VIEWER 'Отвори в сайта' → fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
## B6. Book open: catalogue → book (cover rotates around the spine) and back (recording)
REC bookopen: 40 frames @8 fps; motion 0 150 1080 2150: frames=40 moving-frames(>1.0)=6 max-step=24.1 max-vs-first=31.5 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 20.0 17.7 0.0 0.4 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 1.1 0.0 24.1 0.0 0.0 19.1 0.0 13.6 0.0 0.0 0.0]
BOOKOPEN after back: catalogue shown
REC bookopen-home: 48 frames @8 fps; motion all: frames=48 moving-frames(>1.0)=0 max-step=0.1 max-vs-first=0.2 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0]
## B10. Event: reminder set (confetti) and share as image
EVENT: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Биб
REC confetti-reminder: 31 frames @8 fps; motion all: frames=31 moving-frames(>1.0)=1 max-step=11.5 max-vs-first=12.0 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.4 0.2 11.5 0.4 0.5 0.5 0.5 0.5]
CONFETTI reminder: confetti-like new saturated points per frame (max 40): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 34, 27, 40, 31, 33, 22]
REMINDER: ; bell: on
SHARE event sheet: text="Сподели като картинка" text="Карта с QR код — за Instagram, Facebook, Viber…" text="Сподели връзката" text="Текст с адреса в сайта" 
SHARE event: chooser OPEN after 2 s (fg android/com.android.internal.app.ChooserActivity); app log: 11-07 16:56:54.120  1712  1712 E A       : com.google.android.apps.gsa.shared.speech.a.m: errorCode: 65561, engine: 2~11-07 16:56:54.120  1712  1712 E A       : Caused by: com.google.android.apps.gsa.shared.speech.a.o: errorCode: 65586, engine: 2~
SHARE event received by the helper: action=android.intent.action.SEND type=image/png flags=0x1b080001 clipItems=1 stream=content://org.chyavorec.app.files/share/chyavorec-event-1794070617589.png mime=image/png bytes=654870 png=true dims=1080x1920 subject=80 г. от създаването на ЮНЕСКО text=80 г. от създаването на ЮНЕСКО 16 ноември 2026 НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец https://chyavorec.org/events
## B12. 'Изненадай ме': the dice button in the catalogue
REC surprise-dice: 49 frames @8 fps; motion all: frames=49 moving-frames(>1.0)=5 max-step=173.6 max-vs-first=201.6 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.0 4.6 0.0 0.0 0.0 173.6 0.3 0.0 0.0 26.3 0.0 0.0 0.0 0.0 1.9 1.3 0.0 0.0 0.0 0.0]
SURPRISE dice: text="Още една" text="Към книгата" ; bottom bar hidden
SURPRISE 'Към книгата' → book screen yes: text="Дъхът на дома" text="Красимир Маринов" text="Налична" 
## B13. 'Изненадай ме' by shaking (emulator accelerometer: two peaks > 2.3 g within 0.7 s)
accelerometer: 0000000000) Goldfish 3-axis Accelerometer | The Android Open Source Project | ver: 1 | type: android.sensor.acceleromete
SHAKE one short peak (info; a peak held > 120 ms counts twice by design): not triggered
SHAKE double peak: TRIGGERED; text="Към книгата" 
## B13b. Shake on another tab (catalogue not resumed) must not trigger
on Home before the shake: TAB selected: ['Начало']
SHAKE on Home: not triggered
## COMPLETE
## logcat findings
app pids seen: ['5416', '6803', '7959']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 23
   skipped-frames events=23 max=77 total=1165
== Davey: 49
   davey events=49 max=2110ms median=952ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## idle19

```
part idle19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## I-new. Install new, warm-up, library card
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## I-new. Home: cold start, 0-24 s (ribbon + carousel) and 37-67 s (should be ~0)
IDLE new-home-0-25s (24 s, no input): frames +113; cpu=25.1% (ticks 603 in 24 s; 100 ticks = 1 cpu-s) threads: RenderThread=23.0%, g.chyavorec.app=1.3%, Jit thread pool=0.6%, HeapTaskDaemon=0.1%, DefaultExecutor=0.0%; pid 7378
IDLE new-home-37-67s (30 s, no input): frames +94; cpu=15.8% (ticks 475 in 30 s; 100 ticks = 1 cpu-s) threads: RenderThread=15.4%, g.chyavorec.app=0.3%, Jit thread pool=0.1%, DefaultExecutor=0.0%; pid 7378
  cpuinfo: 100% TOTAL: 51% user + 46% kernel + 2.2% softirq
## I-new. Library card: first 30 s (shader budget), then 35-65 s, then with sensor jitter
IDLE new-card-0-30s (30 s, no input): frames +140; cpu=26.4% (ticks 792 in 30 s; 100 ticks = 1 cpu-s) threads: RenderThread=25.7%, g.chyavorec.app=0.5%, Jit thread pool=0.2%, binder:7378_4=0.0%; pid 7378
IDLE new-card-35-65s (30 s, no input): frames +128; cpu=20.0% (ticks 600 in 30 s; 100 ticks = 1 cpu-s) threads: RenderThread=19.3%, g.chyavorec.app=0.4%, Jit thread pool=0.2%, binder:7378_2=0.1%, DefaultExecutor=0.0%; pid 7378
IDLE new-card-jitter (20 s, no input): frames +94; cpu=24.9% (ticks 497 in 20 s; 100 ticks = 1 cpu-s) threads: RenderThread=24.1%, g.chyavorec.app=0.4%, Profile Saver=0.1%, binder:7378_5=0.1%, binder:7378_4=0.1%; pid 7378
## I-new. Other screens, 8 s after opening, 20 s each
IDLE new-article (20 s, no input): frames +0; cpu=0.0% (ticks 0 in 20 s; 100 ticks = 1 cpu-s) threads: none; pid 7378
IDLE new-catalog (20 s, no input): frames +0; cpu=0.1% (ticks 3 in 20 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%, Profile Saver=0.1%; pid 7378
IDLE new-my (20 s, no input): frames +0; cpu=0.0% (ticks 0 in 20 s; 100 ticks = 1 cpu-s) threads: none; pid 7378
IDLE new-news (20 s, no input): frames +0; cpu=0.0% (ticks 0 in 20 s; 100 ticks = 1 cpu-s) threads: none; pid 7378
IDLE new-home-return (20 s, no input): frames +0; cpu=0.0% (ticks 0 in 20 s; 100 ticks = 1 cpu-s) threads: none; pid 7378
## I-new. In the background (Home key)
IDLE new-background (30 s, no input): frames +0; cpu=0.1% (ticks 2 in 30 s; 100 ticks = 1 cpu-s) threads: HeapTaskDaemon=0.1%; pid 7378
## I-base. Install base, warm-up, library card
installed base: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="Sat, Nov 7" content-desc="Sat, Nov 7" text="Gmail" content-desc="Gmail" text="Photos" content-desc="Photos" text="YouTube" content-desc="YouTube" content-desc="Home" text="Phone" 
base: card not shown
## I-base. Home: cold start, 0-24 s (ribbon + carousel) and 37-67 s (should be ~0)
IDLE base-home-0-25s (24 s, no input): frames +128; cpu=30.5% (ticks 733 in 24 s; 100 ticks = 1 cpu-s) threads: RenderThread=28.2%, g.chyavorec.app=1.0%, Jit thread pool=1.0%, HeapTaskDaemon=0.1%, DefaultDispatch=0.1%; pid 9163
IDLE base-home-37-67s (30 s, no input): frames +0; cpu=0.0% (ticks 0 in 30 s; 100 ticks = 1 cpu-s) threads: none; pid 9163
  cpuinfo: 29% TOTAL: 4.8% user + 24% kernel + 0.4% iowait + 0.2% softirq
## I-base. Library card: first 30 s (shader budget), then 35-65 s, then with sensor jitter
CARD: no card-number field: text="Sat, Nov 7" content-desc="Sat, Nov 7" text="Gmail" content-desc="Gmail" text="Photos" content-desc="Photos" text="YouTube" content-desc="YouTube" 
## I-base. Other screens, 8 s after opening, 20 s each
IDLE base-article (20 s, no input): frames +0; cpu=0.0% (ticks 0 in 20 s; 100 ticks = 1 cpu-s) threads: none; pid 9538
IDLE base-catalog (20 s, no input): frames +0; cpu=0.1% (ticks 1 in 20 s; 100 ticks = 1 cpu-s) threads: Jit thread pool=0.1%; pid 9538
IDLE base-my (20 s, no input): frames +0; cpu=0.0% (ticks 0 in 20 s; 100 ticks = 1 cpu-s) threads: none; pid 9538
IDLE base-news (20 s, no input): frames +0; cpu=0.0% (ticks 0 in 20 s; 100 ticks = 1 cpu-s) threads: none; pid 9538
IDLE base-home-return (20 s, no input): frames +91; cpu=22.4% (ticks 447 in 20 s; 100 ticks = 1 cpu-s) threads: RenderThread=20.9%, g.chyavorec.app=0.6%, Jit thread pool=0.6%, binder:9538_1=0.1%, Profile Saver=0.1%; pid 9538
## I-base. In the background (Home key)
IDLE base-background (30 s, no input): frames +0; cpu=0.1% (ticks 2 in 30 s; 100 ticks = 1 cpu-s) threads: HeapTaskDaemon=0.1%; pid 9538
## COMPLETE
## logcat findings
app pids seen: ['5246', '7378', '8654', '9077', '9163', '9337', '9418', '9538']
== FATAL: 10
== ANR: 0
== StrictMode: 0
== Skipped: 41
   skipped-frames events=41 max=110 total=2182
== Davey: 76
   davey events=76 max=2715ms median=894ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## idlelong

```
part idlelong; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160886 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## IL-new. Install new, warm-up, library card
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## IL-new. Home after a cold start: frames/CPU in 15 s windows, 0-120 s, no input
REC ribbon-new: 95 frames @8 fps; motion 0 280 1080 398: frames=95 moving-frames(>1.0)=28 max-step=1.9 max-vs-first=5.5 steps=[0.0 0.0 0.0 0.1 0.0 0.0 0.0 0.2 0.1 0.5 0.5 0.2 0.0 0.8 0.0 1.2 0.0 1.0 0.5 0.0 1.5 0.0 0.6 1.0 1.0 0.0 1.1 0.0 1.4 0.1 0.0 1.5 0.8 1.1 0.7 1.3 0.4 1.1 0.9 0.8 1.1 0.4 1.5 0.4 1.2 0.2 1.2 1.0 1.1 0.4 1.3 0.0 0.2 0.3 0.6 0.3 0.0 0.5 0.0 1.4 0.0 1.7 0.0 1.2 1.0 0.7 0.8 1.9 0.0 0.9 1.9 0.0 0.8 0.0 1.0 1.2 1.2 0.2 0.0 1.2 0.0 1.1 0.0 1.1 0.9 0.0 0.0 1.
IDLE new-home-w1 (15 s, no input): frames +99; cpu=23.1% (ticks 347 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=21.7%, g.chyavorec.app=0.9%, Jit thread pool=0.4%, binder:7771_2=0.1%, DefaultExecutor=0.1%; pid 7771
IDLE new-home-w2 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7771
IDLE new-home-w3 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7771
IDLE new-home-w4 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7771
IDLE new-home-w5 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7771
IDLE new-home-w6 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7771
IDLE new-home-w7 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7771
## IL-new. Library card: 15 s windows, 0-105 s, no input
IDLE new-card-w1 (15 s, no input): frames +147; cpu=27.3% (ticks 410 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=25.7%, g.chyavorec.app=0.9%, Jit thread pool=0.5%, DefaultExecutor=0.1%, binder:7771_2=0.1%; pid 7771
IDLE new-card-w2 (15 s, no input): frames +149; cpu=29.4% (ticks 441 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=28.0%, g.chyavorec.app=0.7%, Jit thread pool=0.4%, binder:7771_2=0.1%, binder:7771_4=0.1%; pid 7771
IDLE new-card-w3 (15 s, no input): frames +144; cpu=28.3% (ticks 425 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=27.3%, g.chyavorec.app=0.9%, Jit thread pool=0.1%, binder:7771_1=0.1%; pid 7771
IDLE new-card-w4 (15 s, no input): frames +145; cpu=28.1% (ticks 421 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=27.2%, g.chyavorec.app=0.7%, binder:7771_1=0.1%, FrameMetricsAgg=0.1%; pid 7771
IDLE new-card-w5 (15 s, no input): frames +147; cpu=29.4% (ticks 441 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=28.3%, g.chyavorec.app=0.7%, HeapTaskDaemon=0.1%, binder:7771_2=0.1%, binder:7771_1=0.1%; pid 7771
IDLE new-card-w6 (15 s, no input): frames +128; cpu=22.9% (ticks 344 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=22.1%, g.chyavorec.app=0.7%, binder:7771_2=0.1%, FrameMetricsAgg=0.1%; pid 7771
IDLE new-card-w7 (15 s, no input): frames +131; cpu=23.0% (ticks 345 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=22.1%, g.chyavorec.app=0.6%, binder:7771_4=0.1%, binder:7771_4=0.1%, binder:7771_1=0.1%; pid 7771
## IL-base. Install base, warm-up, library card
installed base: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160886;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## IL-base. Home after a cold start: frames/CPU in 15 s windows, 0-120 s, no input
IDLE base-home-w1 (15 s, no input): frames +99; cpu=19.7% (ticks 295 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=17.5%, g.chyavorec.app=1.1%, Jit thread pool=0.9%, binder:9207_3=0.1%, binder:9207_1=0.1%; pid 9207
IDLE base-home-w2 (15 s, no input): frames +13; cpu=3.0% (ticks 45 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=2.4%, g.chyavorec.app=0.3%, Jit thread pool=0.3%; pid 9207
IDLE base-home-w3 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9207
IDLE base-home-w4 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9207
IDLE base-home-w5 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9207
IDLE base-home-w6 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9207
IDLE base-home-w7 (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 9207
## IL-base. Library card: 15 s windows, 0-105 s, no input
IDLE base-card-w1 (15 s, no input): frames +170; cpu=30.6% (ticks 459 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=29.4%, g.chyavorec.app=0.9%, Jit thread pool=0.2%, binder:9207_5=0.1%; pid 9207
IDLE base-card-w2 (15 s, no input): frames +168; cpu=30.8% (ticks 462 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=29.7%, g.chyavorec.app=0.8%, Jit thread pool=0.2%, binder:9207_5=0.1%, FrameMetricsAgg=0.1%; pid 9207
IDLE base-card-w3 (15 s, no input): frames +172; cpu=29.6% (ticks 444 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=28.4%, g.chyavorec.app=0.9%, binder:9207_3=0.2%, Jit thread pool=0.1%, FrameMetricsAgg=0.1%; pid 9207
IDLE base-card-w4 (15 s, no input): frames +175; cpu=32.1% (ticks 481 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=31.0%, g.chyavorec.app=0.8%, binder:9207_5=0.1%, FrameMetricsAgg=0.1%; pid 9207
IDLE base-card-w5 (15 s, no input): frames +176; cpu=30.0% (ticks 450 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=28.9%, g.chyavorec.app=0.8%, binder:9207_3=0.1%, binder:9207_1=0.1%, Profile Saver=0.1%; pid 9207
IDLE base-card-w6 (15 s, no input): frames +176; cpu=32.3% (ticks 484 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=31.1%, g.chyavorec.app=0.8%, binder:9207_5=0.2%, binder:9207_1=0.1%, Jit thread pool=0.1%; pid 9207
IDLE base-card-w7 (15 s, no input): frames +177; cpu=30.4% (ticks 456 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=29.4%, g.chyavorec.app=0.8%, binder:9207_5=0.1%, binder:9207_2=0.1%, FrameMetricsAgg=0.1%; pid 9207
## COMPLETE
## logcat findings
app pids seen: ['6143', '7771', '8772', '9207']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 19
   skipped-frames events=19 max=78 total=782
== Davey: 32
   davey events=32 max=1860ms median=950ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank19-1

```
part jank19-1; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## J-new. Install new, warm-up, library card TEST-12345
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 4351 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## D0. A few starts for the card: 4 cold + 3 warm
## J-new. Home after a cold start: the first 25 s (ribbon weaving, carousel), no input
GFX new-home-ribbon frames=  148  janky=143 (96.62%)  p50=150ms p90=300ms p95=350ms p99=700ms  slowUI=37 slowDraw=143 highInput=8
## J-new. Home 25-40 s after the cold start (ribbon done), no input
GFX new-home-idle frames=  110  janky=107 (97.27%)  p50=150ms p90=300ms p95=350ms p99=400ms  slowUI=17 slowDraw=107 highInput=4
RIBBON new box [0 280 1080 398]: 3 s mean=209.4 std=67.0 thread-px=26.9%; 40 s mean=212.1 std=63.5 thread-px=26.4%
RIBBON new change 3→8→14→20→40 s: 4.04 7.05 5.03 4.60
## J-new. Scroll scenarios (animations ON, 1080x2400): ~10 flings each, gfxinfo reset before each
GFX new-home-scroll frames=   77  janky=76 (98.70%)  p50=250ms p90=400ms p95=450ms p99=500ms  slowUI=41 slowDraw=76 highInput=1
  GFXMEM new-home-scroll: pipeline: Skia (OpenGL); GPU total: 37469368 bytes, 35.73 MB (30.98 MB is purgeable); textures: 2.83 MB (6 entries) | 7.18 MB (30 entries) | 7.60 MB (18 entries); frames: 77
GFX new-news     frames=   69  janky=68 (98.55%)  p50=150ms p90=200ms p95=200ms p99=250ms  slowUI=38 slowDraw=68 highInput=1
  GFXMEM new-news: pipeline: Skia (OpenGL); GPU total: 73726888 bytes, 70.31 MB (65.53 MB is purgeable); textures: 9.89 MB (77 entries) | 10.44 MB (24 entries); frames: 69
GFX new-article  frames=   81  janky=79 (97.53%)  p50=200ms p90=300ms p95=350ms p99=400ms  slowUI=30 slowDraw=79 highInput=3
  GFXMEM new-article: pipeline: Skia (OpenGL); GPU total: 77843552 bytes, 74.24 MB (71.42 MB is purgeable); textures: 13.85 KB (1 entry) | 11.11 MB (71 entries) | 10.48 MB (36 entries); frames: 81
new catalog: 2446 results
GFX new-catalog  frames=   56  janky=56 (100.00%)  p50=250ms p90=450ms p95=500ms p99=500ms  slowUI=24 slowDraw=56 highInput=0
  GFXMEM new-catalog: pipeline: Skia (OpenGL); GPU total: 85288104 bytes, 81.34 MB (47.07 MB is purgeable); textures: 334.61 KB (14 entries) | 2.00 MB (2 entries); frames: 56
new viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX new-viewer   frames=  168  janky=165 (98.21%)  p50=150ms p90=200ms p95=200ms p99=250ms  slowUI=69 slowDraw=165 highInput=5
new viewer after 15 swipes: text="16 / 29  ·  Складове и хамбари"
## J-new. Library card screen: shader + foil (1.2.33), idle then emulated tilt
SENSORS: Accelerometer (handle=0x00000000, connectio;Accelerometer Uncalibrated ;Game Rotation Vector Sensor ;Gyroscope ;Gyroscope (uncalibrated) ;Gyroscope Bias (debug)    ;Gyroscope Sensor ;Magnetic field sensor ;Magnetic field sensor (uncalibrated) ;Rotation Vector Sensor ;Rotation Vector Sensor    ;accel
emu console: sensor status → acceleration: enabled. gyroscope: enabled. 
GFX new-card-idle frames=   80  janky=79 (98.75%)  p50=200ms p90=350ms p95=400ms p99=500ms  slowUI=25 slowDraw=78 highInput=1
GFX new-card-tilt frames=   50  janky=49 (98.00%)  p50=250ms p90=350ms p95=350ms p99=450ms  slowUI=11 slowDraw=49 highInput=1
CARD new diff before/after tilt (upper half): 
GFX new-card-after-36s frames=   68  janky=67 (98.53%)  p50=150ms p90=250ms p95=250ms p99=300ms  slowUI=1 slowDraw=67 highInput=2
MEM new-after-scroll (raw) PSS=89891KB  Java=14004KB  Native=42116KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=15)  RSS=166916KB  Views=10 Activities=1
MEM new-after-scroll (trimmed) PSS=76637KB  Java=10272KB  Native=28036KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=15)  RSS=154688KB  Views=10 Activities=1
## D. Diagnostics › „Бързодействие“ after the starts and the scenarios
PERF CARD new-filled: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 2839 мс, последен 2696 мс | Пълно показване: медиана 3887 мс, последно 3841 мс | Топъл старт · 4 | Първи кадър: медиана 491 мс, последен 150 мс | Насечени кадри по екрани | gallery: 3,1 % от 32 кадъра · най-бавен 297 мс | article: 0,7 % от 141 кадъра · най-бавен 312 мс | card: 0,3 % от 342 кадъра · най-бавен 295 мс | home: 0,0 % от 405 кадъра · най-бавен 1717 мс | viewer: 0,0 % от 178 кадъра · най-бавен 281 мс | Записи: 1 | 07.11.2026 18:08:45  ·  update.verify | signature mismatch | Диагностика
PERF CARD new-filled (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 2839 мс, последен 2696 мс | Пълно показване: медиана 3887 мс, последно 3841 мс | Топъл старт · 4 | Първи кадър: медиана 491 мс, последен 150 мс | Насечени кадри по екрани | gallery: 3,1 % от 32 кадъра · най-бавен 297 мс | article: 0,7 % от 141 кадъра · най-бавен 312 мс | card: 0,3 % от 342 кадъра · най-бавен 295 мс | home: 0,0 % от 405 кадъра · най-бавен 1717 мс | viewer: 0,0 % от 178 кадъра · най-бавен 281 мс | Записи: 1 | 07.11.2026 18:08:45  ·  update.verify | signature mismatch | Диагностика
DIAG records (shader errors?): 
## J-base. Install base, warm-up, library card TEST-12345
installed base: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 2745 ms
CARD added 'TEST-12345': 
base: card not shown
## J-base. Home after a cold start: the first 25 s (ribbon weaving, carousel), no input
!!! EMULATOR GONE / HUNG (at: J-base. Home after a cold start: the first 25 s (ribbon weaving, carousel), no input)
## logcat findings
app pids seen: ['5173', '7683', '7850', '8090', '8224', '8354', '10358']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 31
   skipped-frames events=31 max=90 total=1543
== Davey: 41
   davey events=41 max=2207ms median=1036ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## jank19-2

```
part jank19-2; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## J-base. Install base, warm-up, library card TEST-12345
installed base: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base first-install catalogue results after 6154 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## J-base. Home after a cold start: the first 25 s (ribbon weaving, carousel), no input
GFX base-home-ribbon frames=  122  janky=120 (98.36%)  p50=200ms p90=350ms p95=500ms p99=750ms  slowUI=40 slowDraw=120 highInput=2
## J-base. Home 25-40 s after the cold start (ribbon done), no input
GFX base-home-idle frames=   28  janky=27 (96.43%)  p50=250ms p90=350ms p95=350ms p99=450ms  slowUI=16 slowDraw=27 highInput=1
RIBBON base box [0 280 1080 314]: 3 s mean=246.3 std=7.4 thread-px=0.6%; 40 s mean=246.5 std=6.5 thread-px=0.3%
RIBBON base change 3→8→14→20→40 s: 0.28 0.34 1.76 1.51
## J-base. Scroll scenarios (animations ON, 1080x2400): ~10 flings each, gfxinfo reset before each
GFX base-home-scroll frames=   28  janky=27 (96.43%)  p50=400ms p90=750ms p95=800ms p99=900ms  slowUI=17 slowDraw=27 highInput=1
  GFXMEM base-home-scroll: pipeline: Skia (OpenGL); GPU total: 27854084 bytes, 26.56 MB (21.59 MB is purgeable); textures: 2.35 MB (5 entries) | 6.82 MB (29 entries) | 7.45 MB (21 entries); frames: 28
GFX base-news    frames=   96  janky=95 (98.96%)  p50=200ms p90=300ms p95=300ms p99=400ms  slowUI=51 slowDraw=95 highInput=2
  GFXMEM base-news: pipeline: Skia (OpenGL); GPU total: 77433816 bytes, 73.85 MB (68.86 MB is purgeable); textures: 9.85 MB (41 entries) | 10.07 MB (36 entries); frames: 96
GFX base-article frames=   86  janky=86 (100.00%)  p50=200ms p90=300ms p95=300ms p99=400ms  slowUI=34 slowDraw=86 highInput=0
  GFXMEM base-article: pipeline: Skia (OpenGL); GPU total: 87840800 bytes, 83.77 MB (80.78 MB is purgeable); textures: 6.01 MB (41 entries) | 10.05 MB (31 entries); frames: 86
base catalog: 2446 results
GFX base-catalog frames=   56  janky=56 (100.00%)  p50=300ms p90=450ms p95=550ms p99=650ms  slowUI=31 slowDraw=56 highInput=0
  GFXMEM base-catalog: pipeline: Skia (OpenGL); GPU total: 83820352 bytes, 79.94 MB (45.80 MB is purgeable); textures: 279.82 KB (12 entries) | 2.00 MB (2 entries); frames: 56
base viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX base-viewer  frames=  163  janky=157 (96.32%)  p50=150ms p90=200ms p95=200ms p99=250ms  slowUI=82 slowDraw=157 highInput=9
base viewer after 15 swipes: text="16 / 29  ·  Складове и хамбари"
## J-base. Library card screen: shader + foil (1.2.33), idle then emulated tilt
SENSORS: Accelerometer (handle=0x00000000, connectio;Accelerometer Uncalibrated ;Game Rotation Vector Sensor ;Gyroscope ;Gyroscope (uncalibrated) ;Gyroscope Bias (debug)    ;Gyroscope Sensor ;Magnetic field sensor ;Magnetic field sensor (uncalibrated) ;Rotation Vector Sensor ;Rotation Vector Sensor    ;accel
emu console: sensor status → acceleration: enabled. gyroscope: enabled. 
GFX base-card-idle frames=  105  janky=104 (99.05%)  p50=125ms p90=150ms p95=200ms p99=200ms  slowUI=13 slowDraw=104 highInput=2
GFX base-card-tilt frames=   65  janky=62 (95.38%)  p50=129ms p90=150ms p95=200ms p99=200ms  slowUI=11 slowDraw=62 highInput=5
CARD base diff before/after tilt (upper half): 
GFX base-card-after-36s frames=    0  janky=0 (0.00%)  p50=4950ms p90=4950ms p95=4950ms p99=4950ms  slowUI=0 slowDraw=0 highInput=0
MEM base-after-scroll (raw) PSS=90724KB  Java=13216KB  Native=40208KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=19)  RSS=162608KB  Views=10 Activities=1
MEM base-after-scroll (trimmed) PSS=67413KB  Java=9776KB  Native=15416KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=19)  RSS=139920KB  Views=10 Activities=1
## J-new. Install new, warm-up, library card TEST-12345
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new first-install catalogue results after 2921 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## D0. A few starts for the card: 4 cold + 3 warm
## J-new. Home after a cold start: the first 25 s (ribbon weaving, carousel), no input
GFX new-home-ribbon frames=  151  janky=150 (99.34%)  p50=150ms p90=300ms p95=350ms p99=600ms  slowUI=44 slowDraw=150 highInput=1
## J-new. Home 25-40 s after the cold start (ribbon done), no input
GFX new-home-idle frames=  108  janky=106 (98.15%)  p50=150ms p90=250ms p95=300ms p99=350ms  slowUI=20 slowDraw=106 highInput=3
RIBBON new box [0 280 1080 398]: 3 s mean=209.5 std=67.1 thread-px=26.8%; 40 s mean=212.3 std=63.2 thread-px=26.4%
RIBBON new change 3→8→14→20→40 s: 4.42 6.91 5.08 5.17
## J-new. Scroll scenarios (animations ON, 1080x2400): ~10 flings each, gfxinfo reset before each
GFX new-home-scroll frames=   73  janky=73 (100.00%)  p50=300ms p90=450ms p95=600ms p99=650ms  slowUI=36 slowDraw=73 highInput=0
  GFXMEM new-home-scroll: pipeline: Skia (OpenGL); GPU total: 39207544 bytes, 37.39 MB (32.61 MB is purgeable); textures: 3.27 MB (7 entries) | 5.97 MB (33 entries) | 8.34 MB (23 entries); frames: 73
GFX new-news     frames=   61  janky=61 (100.00%)  p50=200ms p90=300ms p95=300ms p99=350ms  slowUI=32 slowDraw=61 highInput=0
  GFXMEM new-news: pipeline: Skia (OpenGL); GPU total: 72102952 bytes, 68.76 MB (63.74 MB is purgeable); textures: 941.57 KB (2 entries) | 9.10 MB (77 entries) | 10.69 MB (28 entries); frames: 61
GFX new-article  frames=   76  janky=76 (100.00%)  p50=200ms p90=350ms p95=350ms p99=450ms  slowUI=34 slowDraw=76 highInput=0
  GFXMEM new-article: pipeline: Skia (OpenGL); GPU total: 88118640 bytes, 84.04 MB (80.98 MB is purgeable); textures: 955.42 KB (3 entries) | 8.50 MB (72 entries) | 9.17 MB (24 entries); frames: 76
new catalog: 2446 results
GFX new-catalog  frames=   55  janky=55 (100.00%)  p50=250ms p90=500ms p95=500ms p99=550ms  slowUI=32 slowDraw=55 highInput=0
  GFXMEM new-catalog: pipeline: Skia (OpenGL); GPU total: 84753456 bytes, 80.83 MB (52.56 MB is purgeable); textures: 283.70 KB (13 entries) | 2.03 MB (9 entries); frames: 55
new viewer: text="1 / 29  ·  Сградата на читалището в Яворец"
GFX new-viewer   frames=  173  janky=167 (96.53%)  p50=150ms p90=200ms p95=250ms p99=300ms  slowUI=78 slowDraw=167 highInput=9
new viewer after 15 swipes: text="16 / 29  ·  Складове и хамбари"
## J-new. Library card screen: shader + foil (1.2.33), idle then emulated tilt
SENSORS: Accelerometer (handle=0x00000000, connectio;Accelerometer Uncalibrated ;Game Rotation Vector Sensor ;Gyroscope ;Gyroscope (uncalibrated) ;Gyroscope Bias (debug)    ;Gyroscope Sensor ;Magnetic field sensor ;Magnetic field sensor (uncalibrated) ;Rotation Vector Sensor ;Rotation Vector Sensor    ;accel
emu console: sensor status → acceleration: enabled. gyroscope: enabled. 
GFX new-card-idle frames=   80  janky=76 (95.00%)  p50=200ms p90=300ms p95=350ms p99=400ms  slowUI=24 slowDraw=76 highInput=7
GFX new-card-tilt frames=   52  janky=52 (100.00%)  p50=200ms p90=300ms p95=350ms p99=400ms  slowUI=15 slowDraw=52 highInput=0
CARD new diff before/after tilt (upper half): 
GFX new-card-after-36s frames=   67  janky=67 (100.00%)  p50=200ms p90=350ms p95=350ms p99=500ms  slowUI=22 slowDraw=67 highInput=0
MEM new-after-scroll (raw) PSS=92945KB  Java=12324KB  Native=38564KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=41)  RSS=170288KB  Views=10 Activities=1
MEM new-after-scroll (trimmed) PSS=83398KB  Java=9580KB  Native=29000KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=41)  RSS=164372KB  Views=10 Activities=1
## D. Diagnostics › „Бързодействие“ after the starts and the scenarios
PERF CARD new-filled: Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 2488 мс, последен 2490 мс | Пълно показване: медиана 3367 мс, последно 3544 мс | Топъл старт · 4 | Първи кадър: медиана 403 мс, последен 231 мс | Насечени кадри по екрани | home: 0,0 % от 452 кадъра · най-бавен 1308 мс | card: 0,0 % от 348 кадъра · най-бавен 302 мс | viewer: 0,0 % от 184 кадъра · най-бавен 264 мс | article: 0,0 % от 84 кадъра · най-бавен 245 мс | news: 0,0 % от 73 кадъра · най-бавен 215 мс | Записи: 1 | 07.11.2026 18:14:26  ·  update.verify | signature mismatch | Диагностика
PERF CARD new-filled (scrolled): Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 6 | Първи кадър: медиана 2488 мс, последен 2490 мс | Пълно показване: медиана 3367 мс, последно 3544 мс | Топъл старт · 4 | Първи кадър: медиана 403 мс, последен 231 мс | Насечени кадри по екрани | home: 0,0 % от 452 кадъра · най-бавен 1308 мс | card: 0,0 % от 348 кадъра · най-бавен 302 мс | viewer: 0,0 % от 184 кадъра · най-бавен 264 мс | article: 0,0 % от 84 кадъра · най-бавен 245 мс | news: 0,0 % от 73 кадъра · най-бавен 215 мс | Записи: 1 | 07.11.2026 18:14:26  ·  update.verify | signature mismatch | Диагностика
DIAG records (shader errors?): 
## J. Totals
## COMPLETE
## logcat findings
app pids seen: ['5324', '7630', '9290', '9953', '10021', '10086', '10147', '10281']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 29
   skipped-frames events=29 max=109 total=1373
== Davey: 56
   davey events=56 max=2945ms median=989ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## live36

```
part live36; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. Device: Android 16 (SDK 36), 13894323
POST_PROMOTED_NOTIFICATIONS: 
## L1. Update download as a Live Update: 1.2.33-pre (versionCode 1488545) → the staged test 1.2.33 (1488546), throttled
staged: 1488546 1.2.33 4274690 urce/ch-app/emulator-results/v133/update/42392073324/app.apk
installed new-upd: 1.2.33-pre 6d3a324; versionCode 1488545; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v133/update/42392073324/update.json size 4274666;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.33-pre 
POST_PROMOTED_NOTIFICATIONS (pre build): 
network: wifi 0; Active default network: 101; 1 PROMOTED lines in dumpsys package
THROTTLE on: -P INPUT ACCEPT -A INPUT -p tcp -m tcp --sport 443 -m hashlimit --hashlimit-above 64kb/s --hashlimit-burst 64kb --hashlimit-mode srcip --hashlimit-name pcdl -j DROP -A INPUT -j oem_in 
LIVE update notification at +4845 ms:
LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 3%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (3) requestPromotedOngoing=Boolean (true) shortCriticalText=String (3%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
   android.requestPromotedOngoing=Boolean (true)
   android.template=String (android.app.Notification$ProgressStyle)
   android.shortCriticalText=String (3%)
LIVE total for org.chyavorec.app: 1
SHADE: text="Изтегляне на версия 1.2.33 — 21%" text="Отказ" text="100%" 
LIVE at +35037 ms: LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 30%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (30) requestPromotedOngoing=Boolean (true) shortCriticalText=String (30%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
LIVE last capture: LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 95%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (95) requestPromotedOngoing=Boolean (true) shortCriticalText=String (95%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
  dumpsys:      33                 android.template=String (android.app.Notification$ProgressStyle)
  dumpsys:      33                 android.requestPromotedOngoing=Boolean (true)
  dumpsys:       1                 android.shortCriticalText=String (95%)
  dumpsys:       1                 android.shortCriticalText=String (92%)
  dumpsys:       1                 android.shortCriticalText=String (89%)
  dumpsys:       1                 android.shortCriticalText=String (87%)
  dumpsys:       1                 android.shortCriticalText=String (85%)
  dumpsys:       1                 android.shortCriticalText=String (84%)
  dumpsys:       1                 android.shortCriticalText=String (82%)
  dumpsys:       1                 android.shortCriticalText=String (81%)
  dumpsys:       1                 android.shortCriticalText=String (79%)
  dumpsys:       1                 android.shortCriticalText=String (77%)
THROTTLE off: 0 rules left
l-finish: progress notification at +106374 ms: NOTIF id=701 title=String (Изтегляне на версия 1.2.33 — 98%) text=- progress=Integer (98)/Integer (100) indeterminate=Boolean (false) ongoing=False fgs=False actions=['Отказ']
l-finish RESULT: ready after 110467 ms (first progress notification at +106374 ms); app process '4414'; files [4274690 2026-11-07 16:50 update-1488546.apk;]
## L1b. Install the downloaded update (system installer) → 1.2.33
UPDATE: installed  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
## L2. Event reminder → event Live Update (60 min before; needs the event's start time)
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.32" text="Излезе нова версия на приложението." text="Размер: 4,2 MB" )
EVENT: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" text="НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец" t
REMINDER set: text="Ще получиш напомняне на 15 ноември 2026, 18:00."; bell: on
CAP event-reminder (+2686 ms after the tap; capture took 2576 ms): WorkSpec:  WorkName: 
CAP event-reminder: jobs: 0 [ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~Pending queue:~Active jobs:~ Unsatisfied constraints:~ Current work counts: Config={tot=12 mins={1=4, 2=2, 4=1, 8=1, 16=0, 32=0, 64=0~]; SystemForegroundService: 0 []; notif: ; dialog: 
WORK after the reminder: 92601177 ENQUEUED attempts=0 PeriodicSyncWorker periods=0 gen=0 enq=1794070275465;54416058 SUCCEEDED attempts=1 MessagePollWorker periods=0 gen=0 enq=1794070275723;5f4d13da ENQUEUED attempts=0 MessagePollWorker periods=0 gen=0 enq=1794070277738;acb4ebe0 ENQUEUED attempts=0 EventReminderWorker periods=0 gen=0 enq=1794070323918;
EventReminderWorker scheduled: 1; EventLiveUpdateWorker scheduled: 0
notifications now: LIVE total for org.chyavorec.app: 0
## L3. API 36 smoke: ribbon and card foil render
API36 ribbon mean=209.5 std=67.0 thread-px=26.8%; change 3→9 s 0.00
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## COMPLETE
## logcat findings
app pids seen: ['4414', '5896', '6125', '6716']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=60 total=198
== Davey: 18
   davey events=18 max=1559ms median=836ms
== NativeLink: 3
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## live361

```
part live361; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160886 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. Device: Android 16 (SDK 36), 14574095
POST_PROMOTED_NOTIFICATIONS: android.permission.POST_PROMOTED_NOTIFICATIONS: granted=true
## L1. Update download as a Live Update: 1.2.33-pre (versionCode 1488545) → the staged test 1.2.33 (1488546), throttled
staged: 1488546 1.2.33 4274690 urce/ch-app/emulator-results/v133/update/42395220039/app.apk
installed new-upd: 1.2.33-pre 6d3a324; versionCode 1488545; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v133/update/42395220039/update.json size 4274666;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.33-pre 
POST_PROMOTED_NOTIFICATIONS (pre build): android.permission.POST_PROMOTED_NOTIFICATIONS: granted=true
network: wifi 0; Active default network: 101; 2 PROMOTED lines in dumpsys package
THROTTLE on: -P INPUT ACCEPT -A INPUT -p tcp -m tcp --sport 443 -m hashlimit --hashlimit-above 64kb/s --hashlimit-burst 64kb --hashlimit-mode srcip --hashlimit-name pcdl -j DROP -A INPUT -j oem_in 
LIVE update notification at +3973 ms:
LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 0%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (0) requestPromotedOngoing=Boolean (true) shortCriticalText=String (0%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
   raw: 0x04880deb: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate=null sound=null defaults=0 flags=ONGOING_EVENT|ONL
   raw: flags=ONGOING_EVENT|ONLY_ALERT_ONCE|NO_CLEAR|FOREGROUND_SERVICE|PROMOTED_ONGOING
   raw: mAttributes= AudioAttributes: usage=USAGE_NOTIFICATION content=CONTENT_TYPE_SONIFICATION flags=0x800(FLAG_MUTE_HAPTIC)  tags= bundle=null
   android.requestPromotedOngoing=Boolean (true)
   android.template=String (android.app.Notification$ProgressStyle)
   android.shortCriticalText=String (0%)
LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 3%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (3) requestPromotedOngoing=Boolean (true) shortCriticalText=String (3%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
SHADE: text="Изтегляне на версия 1.2.33 — 24%" text="Отказ" 
LIVE at +40124 ms: LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 37%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (37) requestPromotedOngoing=Boolean (true) shortCriticalText=String (37%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
LIVE last capture: LIVE id=701 title=String (Изтегляне на версия 1.2.33 — 99%) text=- template=String (android.app.Notification$ProgressStyle) progress=Integer (99) requestPromotedOngoing=Boolean (true) shortCriticalText=String (99%) chronometer=- flags=0x800 FLAG_PROMOTED_ONGOING=no promotable=False ongoing=False
  dumpsys:      31       originalFlags=ONGOING_EVENT|ONLY_ALERT_ONCE|FOREGROUND_SERVICE|PROMOTED_ONGOING
  dumpsys:      31                 android.template=String (android.app.Notification$ProgressStyle)
  dumpsys:      31                 android.requestPromotedOngoing=Boolean (true)
  dumpsys:      29       flags=ONGOING_EVENT|ONLY_ALERT_ONCE|NO_CLEAR|FOREGROUND_SERVICE|PROMOTED_ONGOING
  dumpsys:       2       flags=ONGOING_EVENT|ONLY_ALERT_ONCE|FOREGROUND_SERVICE|PROMOTED_ONGOING
  dumpsys:       1     NotificationRecord(0x0fd79dc7: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
  dumpsys:       1     NotificationRecord(0x0f4b9678: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
  dumpsys:       1     NotificationRecord(0x0f18fbad: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
  dumpsys:       1     NotificationRecord(0x0ed32d8c: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
  dumpsys:       1     NotificationRecord(0x0e045d4b: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
  dumpsys:       1     NotificationRecord(0x0e02f704: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
  dumpsys:       1     NotificationRecord(0x0ca03f31: pkg=org.chyavorec.app user=UserHandle{0} id=701 tag=null importance=3 key=0|org.chyavorec.app|701|null|10227: Notification(channel=updates shortcut=null contentView=null vibrate
THROTTLE off: 0 rules left
l-finish RESULT: ready after 111874 ms (first progress notification at +never ms); app process '4113'; files [4274690 2026-11-07 17:28 update-1488546.apk;]
## L1b. Install the downloaded update (system installer) → 1.2.33
UPDATE: installed  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
## L2. Event reminder → event Live Update (60 min before; needs the event's start time)
installed new: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690;  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.32" text="Излезе нова версия на приложението." text="Размер: 4,2 MB" )
EVENT: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" text="НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец" t
REMINDER set: text="Ще получиш напомняне на 15 ноември 2026, 18:00."; bell: on
CAP event-reminder (+2690 ms after the tap; capture took 2492 ms): WorkSpec:  WorkName: 
CAP event-reminder: jobs: 0 [ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~ Unsatisfied constraints: TIMING_DELAY [0x80000000]~ Ready: false (job=false user=true !restricted=true !pending=true !active=tru~Pending queue:~Active jobs:~ Unsatisfied constraints:~ Unsatisfied constraints:~]; SystemForegroundService: 0 []; notif: ; dialog: 
WORK after the reminder: 06a6da59 ENQUEUED attempts=0 PeriodicSyncWorker periods=0 gen=0 enq=1794072525885;25365f98 SUCCEEDED attempts=1 MessagePollWorker periods=0 gen=0 enq=1794072526588;79c1e849 ENQUEUED attempts=0 MessagePollWorker periods=0 gen=0 enq=1794072527856;174f7944 ENQUEUED attempts=0 EventReminderWorker periods=0 gen=0 enq=1794072574262;
EventReminderWorker scheduled: 1; EventLiveUpdateWorker scheduled: 0
notifications now: LIVE total for org.chyavorec.app: 0
## L3. API 36 smoke: ribbon and card foil render
API36 ribbon mean=209.5 std=67.1 thread-px=26.8%; change 3→9 s 0.07
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
## COMPLETE
## logcat findings
app pids seen: ['4113', '5882', '6082', '6794']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=84 total=706
== Davey: 27
   davey events=27 max=2202ms median=956ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem

```
part mem; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3510 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=47259KB  Java=6868KB  Native=5216KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=5)  RSS=94948KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=47920KB  Java=7128KB  Native=4728KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=1)  RSS=93376KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=91825KB  Java=10836KB  Native=39200KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=149292KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=98343KB  Java=12172KB  Native=44744KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=157124KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=95723KB  Java=10772KB  Native=43556KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=154504KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=102124KB  Java=10764KB  Native=47820KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=161620KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=108493KB  Java=12736KB  Native=51420KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=169020KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=106133KB  Java=10868KB  Native=50952KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=166660KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=110643KB  Java=10936KB  Native=57116KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=171860KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=117848KB  Java=19664KB  Native=55076KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=179708KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=100947KB  Java=10896KB  Native=47144KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=162816KB  Views=13 Activities=1
pid after cycles: 6223; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=47920 c5=91825 c10=95723 c15=102124 c20=106133 c25=110643 c30=100947
LEAK TREND (cycles 10-30, least squares): +379 KB per cycle (+7.4 MB over 20 cycles)
MEMTABLE new 1.2.33 (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)
  c00 raw  PSS=46.2  Java=6.7  Native=5.1  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=2.5  RSS=92.7
  c00 trim PSS=46.8  Java=7.0  Native=4.6  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.1  PrivOther=8.1  RSS=91.2
  c10 raw  PSS=96.0  Java=11.9  Native=43.7  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.9  RSS=153.4
  c10 trim PSS=93.5  Java=10.5  Native=42.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.8  RSS=150.9
  c20 raw  PSS=106.0  Java=12.4  Native=50.2  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.8  RSS=165.1
  c20 trim PSS=103.6  Java=10.6  Native=49.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.8  RSS=162.8
  c30 raw  PSS=115.1  Java=19.2  Native=53.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.7  RSS=175.5
  c30 trim PSS=98.6  Java=10.6  Native=46.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.5  RSS=159.0
## COMPLETE
## logcat findings
app pids seen: ['4675', '6223']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 146
   skipped-frames events=146 max=69 total=5691
== Davey: 398
   davey events=398 max=1819ms median=891ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem-base

```
part mem-base; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
installed base: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882;  versionCode=1488545 minSdk=26 targetSdk=37  versionName=1.2.32 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3588 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=48101KB  Java=6836KB  Native=11532KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=6)  RSS=107132KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=50521KB  Java=7100KB  Native=11632KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=6)  RSS=100668KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=93988KB  Java=11216KB  Native=41896KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=13)  RSS=154452KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=112525KB  Java=18188KB  Native=31548KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=146772KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=102320KB  Java=10664KB  Native=28688KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=136628KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=117139KB  Java=12492KB  Native=40684KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=154932KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=137534KB  Java=23368KB  Native=49412KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=176696KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=122033KB  Java=10708KB  Native=46624KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=161200KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=116123KB  Java=10680KB  Native=42100KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=157392KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=130225KB  Java=21436KB  Native=44736KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=172164KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=111712KB  Java=10728KB  Native=37172KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=17)  RSS=153652KB  Views=13 Activities=1
pid after cycles: 6488; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=50521 c5=93988 c10=102320 c15=117139 c20=122033 c25=116123 c30=111712
LEAK TREND (cycles 10-30, least squares): +355 KB per cycle (+6.9 MB over 20 cycles)
MEMTABLE base 1.2.32 (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)
  c00 raw  PSS=47.0  Java=6.7  Native=11.3  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=2.3  RSS=104.6
  c00 trim PSS=49.3  Java=6.9  Native=11.4  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=8.2  RSS=98.3
  c10 raw  PSS=109.9  Java=17.8  Native=30.8  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.7  RSS=143.3
  c10 trim PSS=99.9  Java=10.4  Native=28.0  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.9  RSS=133.4
  c20 raw  PSS=134.3  Java=22.8  Native=48.3  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.9  RSS=172.6
  c20 trim PSS=119.2  Java=10.5  Native=45.5  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.9  RSS=157.4
  c30 raw  PSS=127.2  Java=20.9  Native=43.7  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.9  RSS=168.1
  c30 trim PSS=109.1  Java=10.5  Native=36.3  Graphics=0.0  GL=?  EGL=?  GfxDev=?  OtherDev=0.0  Unknown=0.2  PrivOther=9.7  RSS=150.1
## COMPLETE
## logcat findings
app pids seen: ['5301', '6488']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 129
   skipped-frames events=129 max=84 total=4956
== Davey: 317
   davey events=317 max=1864ms median=865ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey19

```
part monkey19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4164 ms
## K. Monkey 4000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=6613
MONKEY exit=0 duration=207 s; Events injected: 4000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6613'; airplane=0; camera: 
MEM after-monkey (raw) PSS=107721KB  Java=13508KB  Native=41236KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=8)  RSS=143568KB  Views=15 Activities=1
MEM after-monkey (trimmed) PSS=85899KB  Java=10304KB  Native=34188KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=8)  RSS=139364KB  Views=15 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5216', '6613']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 51
   skipped-frames events=51 max=134 total=2374
== Davey: 148
   davey events=148 max=2639ms median=887ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## motion19

```
part motion19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## R0. Reduced motion: animator/transition/window scale 0 (Developer options / „Премахни анимациите“)
scales: 0 0 0
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3849 ms
## R1. Home: the ribbon is drawn still (no sweep)
RM frames in the first 20 s on Home: 10
RM ribbon [0 280 1080 398] mean=209.5 std=67.0 thread-px=26.8%; change 2→6→12→20 s: 0.00 0.00 0.00 (≈0 = still)
## R2. Library card: no sweep
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
RM card: frames in 6 s 0; change 0.00
## R3. No confetti on 'Искам да прочета'
REC rm-want: 29 frames @8 fps; motion all: frames=29 moving-frames(>1.0)=1 max-step=12.1 max-vs-first=12.1 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 12.1 0.0 0.0 0.0 0.1]
RM confetti: confetti-like new saturated points per frame (max 2): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 1]
RM want added: checked=true selected=false bounds=[426,738][911,864]
## R4. Nav rosette: static
REC rm-rosette: 48 frames @8 fps; motion 0 2070 1080 2400: frames=48 moving-frames(>1.0)=4 max-step=232.0 max-vs-first=232.3 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.0 0.2 0.2 0.0 0.0 232.0 0.0 0.0 0.0 0.0 0.0 0.0 231.8 0.0 0.8 2.2 0.0 0.0 0.0 0.0 0.0 0.0 0.2 0.0 0.0 2.1 0.0 0.0 0.0 0.5 0.0]
## R5. Book open: no rotation
REC rm-bookopen: 29 frames @8 fps; motion 0 150 1080 2150: frames=29 moving-frames(>1.0)=2 max-step=20.1 max-vs-first=31.5 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 20.1 0.0 18.2 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.1 0.0 0.0 0.4]
RM after book + back: catalogue shown
## R6. 'Изненадай ме' still works
RM surprise: text="Към книгата" 
## R7. Article: plain progress line, toolbar
RM article toolbar after scrolling down: visible
RM article toolbar after scrolling up: visible
## R8. Battery saver (animations ON): the ribbon must be still
battery saver: low_power=1;  mSettingBatterySaverEnabled=true
SAVER frames in 14 s: 17
SAVER ribbon change 3→8→14 s: 0.06 0.00 (≈0 = still)
## R9. Control: animations ON, no saver — the ribbon moves
CONTROL frames in 14 s: 54
CONTROL ribbon change 3→8→14 s: 1.57 4.80
## COMPLETE
## logcat findings
app pids seen: ['5346', '6675', '8927', '9102']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 14
   skipped-frames events=14 max=140 total=777
== Davey: 32
   davey events=32 max=3824ms median=984ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## surprise28

```
part surprise28; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2547 ms
## SM0. Device memory: MemTotal: 2046996 kB; API 28
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL: 76212 TOTAL SWAP PSS: 0
MEMPROBE dice-1: samples 17; app RSS 177→max 1101 MB at +4240 ms; min MemAvailable 246 MB at +4240 ms
SM1 after 10 s: app NOT in front; pid ''; scene no; deaths: 35 (4 I ActivityManager: Process org.chyavorec.app (pid 5469) has died: fore TOP )
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL: 65062 TOTAL SWAP PSS: 0
MEMPROBE dice-2: samples 17; app RSS 160→max 1478 MB at +5360 ms; min MemAvailable 42 MB at +5360 ms
SM2 after 10 s: app NOT in front; pid ''; scene no; deaths: 28 (7 I ActivityManager: Process org.chyavorec.app (pid 7213) has died: fore TOP )
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL: 66109 TOTAL SWAP PSS: 0
MEMPROBE dice-3: samples 18; app RSS 163→max 1568 MB at +5010 ms; min MemAvailable 247 MB at +5010 ms
SM3 after 10 s: app NOT in front; pid ''; scene no; deaths: 32 (1 I ActivityManager: Process org.chyavorec.app (pid 8510) has died: fore TOP )
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 36; app RSS 162→max 168 MB at +2529 ms; min MemAvailable 1342 MB at +9360 ms
## COMPLETE
## logcat findings
app pids seen: ['5469', '7213', '8510', '9658']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=80 total=190
== Davey: 4
   davey events=4 max=1594ms median=1134ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## surprise28rm

```
part surprise28rm; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
animator scale: 0 (0 = reduced motion: no fan/flip animation)
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 1484 ms
## SM0. Device memory: MemTotal: 2046996 kB; API 28
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL: 73651 TOTAL SWAP PSS: 0
MEMPROBE dice-1: samples 46; app RSS 176→max 179 MB at +6479 ms; min MemAvailable 1114 MB at +2120 ms
SM1 after 10 s: app in front; pid '5708'; scene shown; deaths: 2 ()
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL: 72993 TOTAL SWAP PSS: 0
MEMPROBE dice-2: samples 46; app RSS 176→max 178 MB at +5359 ms; min MemAvailable 1145 MB at +3780 ms
SM2 after 10 s: app in front; pid '5708'; scene shown; deaths: 0 ()
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL: 74039 TOTAL SWAP PSS: 0
MEMPROBE dice-3: samples 46; app RSS 177→max 178 MB at +2420 ms; min MemAvailable 1144 MB at +2100 ms
SM3 after 10 s: app in front; pid '5708'; scene shown; deaths: 0 ()
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 37; app RSS 179→max 182 MB at +1939 ms; min MemAvailable 1144 MB at +7980 ms
## COMPLETE
## logcat findings
app pids seen: ['5708']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## surprise31

```
part surprise31; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15982 MB RAM
android 12 sdk 31; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2274x1080-v31
animator scale: 1 (0 = reduced motion: no fan/flip animation)
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.32" text="Излезе нова версия на приложението." text="Размер: 4,2 МБ" )
first-install catalogue results after 2501 ms
## SM0. Device memory: MemTotal: 2015572 kB; API 31
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL PSS: 84023 TOTAL RSS: 184296 TOTAL SWAP PSS: 454
MEMPROBE dice-1: samples 42; app RSS 167→max 174 MB at +4520 ms; min MemAvailable 657 MB at +2859 ms
SM1 after 10 s: app in front; pid '5896'; scene shown; deaths: 4 ()
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL PSS: 81019 TOTAL RSS: 181792 TOTAL SWAP PSS: 299
MEMPROBE dice-2: samples 42; app RSS 169→max 175 MB at +4129 ms; min MemAvailable 692 MB at +2810 ms
SM2 after 10 s: app in front; pid '5896'; scene shown; deaths: 0 ()
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL PSS: 81820 TOTAL RSS: 182784 TOTAL SWAP PSS: 295
MEMPROBE dice-3: samples 42; app RSS 170→max 177 MB at +3860 ms; min MemAvailable 677 MB at +11840 ms
SM3 after 10 s: app in front; pid '5896'; scene shown; deaths: 0 ()
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 34; app RSS 175→max 178 MB at +2610 ms; min MemAvailable 700 MB at +8740 ms
## COMPLETE
## logcat findings
app pids seen: ['5896']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=56 total=90
== Davey: 2
   davey events=2 max=1184ms median=1102ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## surprise33

```
part surprise33; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160886 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5535 ms
## SM0. Device memory: MemTotal: 2013352 kB; API 33
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL PSS: 74497 TOTAL RSS: 111492 TOTAL SWAP PSS: 32385
MEMPROBE dice-1: samples 33; app RSS 111→max 115 MB at +5520 ms; min MemAvailable 546 MB at +11330 ms
SM1 after 10 s: app in front; pid '5486'; scene shown; deaths: 24 ()
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL PSS: 73934 TOTAL RSS: 114132 TOTAL SWAP PSS: 24141
MEMPROBE dice-2: samples 40; app RSS 104→max 112 MB at +4750 ms; min MemAvailable 486 MB at +10120 ms
SM2 after 10 s: app in front; pid '5486'; scene shown; deaths: 3 ()
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  TOTAL PSS: 75128 TOTAL RSS: 116664 TOTAL SWAP PSS: 23385
MEMPROBE dice-3: samples 40; app RSS 106→max 112 MB at +4860 ms; min MemAvailable 516 MB at +3509 ms
SM3 after 10 s: app in front; pid '5486'; scene shown; deaths: 2 ()
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 34; app RSS 112→max 118 MB at +2670 ms; min MemAvailable 514 MB at +1379 ms
## COMPLETE
## logcat findings
app pids seen: ['5486']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 14
   skipped-frames events=14 max=67 total=688
== Davey: 40
   davey events=40 max=1849ms median=910ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## year19

```
part year19; APK: 1.2.33 6d3a324 (Какво ново: версия 1.2.33; политика за поверителност — 7 ное); versionCode 1488546; x86_64 libs: 2 size 4274690 bytes
base APK: 1.2.32 87ef20e (Профил за бърз старт: генериран на емулатор от ad28878); versionCode 1488545; x86_64 libs: 2; update manifest: no-update stub size 4160882 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488546 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## Y0. devDebug (org.chyavorec.app.dev, demo data)
installed: devDebug 6d3a324 (org.chyavorec.app.dev, demo sign-in DEMO-0001);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.33-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 7176 ms
## Y1. Demo sign-in (DEMO-0001 / demo)
MY after sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, ко
## Y2. The year card on „Моето“
YEAR card: text="Моята читателска година 2026" text="30 книги · виж историята" 
## Y3. Open the story: 8 slides, auto-advance 5.5 s (slide state polled through the test accessibility service)
A11Y service: 11-07 16:51:01.907  8208  8208 I PCA11Y  : connected flags=80
YEAR timeline (state every ~3 s from the tap): +5640 no-slide-state;+21524 state=Слайд1;
## Y4. Tap left (previous) ×2, tap right (next)
YEAR after 2 × left tap: state=Слайд 6 от 8
YEAR after a right tap: state=Слайд 8 от 8
## Y5. Hold pauses the auto-advance (fresh story, press 12 s on slide 1-2)
YEAR reopened: state=Слайд 1 от 8 at +15621 ms
YEAR while held: +7083 ms: 'state=Слайд 1 от 8' → 'state=Слайд 1 от 8' (start 'state=Слайд 1 от 8'; paused = unchanged over > 5.5 s)
YEAR 8 s after release: 'state=Слайд 3 от 8' (advanced = resumed)
## Y6. Swipe down closes the story
YEAR after a fast swipe down (350 ms): STILL OPEN
YEAR after a slow drag down (1.2 s): closed
## Y7. Confetti on renewing a demo loan ('Удължи срока')
REC confetti-renew: 33 frames @8 fps; motion all: frames=33 moving-frames(>1.0)=0 max-step=0.0 max-vs-first=0.0 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0]
CONFETTI renew: confetti-like new saturated points per frame (max 0): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
RENEW: text="Заявено удължаване · чака библиотеката"
## Y8. Confetti on 'Искам да прочета' (demo account)
REC confetti-want-dev: 32 frames @8 fps; motion all: frames=32 moving-frames(>1.0)=1 max-step=11.1 max-vs-first=12.0 steps=[0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.6 11.1 0.0 0.3 0.0 0.5 0.4 0.0]
CONFETTI want (dev): confetti-like new saturated points per frame (max 49): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 43, 43, 49, 33, 33]
## COMPLETE
## logcat findings
app pids seen: []
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Files

- [a-fb28/gfx-tmp.txt](a-fb28/gfx-tmp.txt)
- [a-fb28/host-after-run.txt](a-fb28/host-after-run.txt)
- [a-fb28/job-status.txt](a-fb28/job-status.txt)
- [a-fb28/logcat-all.txt](a-fb28/logcat-all.txt)
- [a-fb28/logcat-dump.txt](a-fb28/logcat-dump.txt)
- [a-fb28/logcat-findings.txt](a-fb28/logcat-findings.txt)
- [a-fb28/logcat-live.txt](a-fb28/logcat-live.txt)
- [a-fb28/rec/fb-bookopen.mp4](a-fb28/rec/fb-bookopen.mp4)
- [a-fb28/shots/fb/article.png](a-fb28/shots/fb/article.png)
- [a-fb28/shots/fb/ribbon-03s.png](a-fb28/shots/fb/ribbon-03s.png)
- [a-fb28/shots/fb/ribbon-10s.png](a-fb28/shots/fb/ribbon-10s.png)
- [a-fb28/shots/fb/ribbon-18s.png](a-fb28/shots/fb/ribbon-18s.png)
- [a-fb28/shots/fb/surprise.png](a-fb28/shots/fb/surprise.png)
- [a-fb28/shots/share/fb-event-0-sheet.png](a-fb28/shots/share/fb-event-0-sheet.png)
- [a-fb28/shots/share/fb-event-1-chooser.png](a-fb28/shots/share/fb-event-1-chooser.png)
- [a-fb28/shots/share/fb-event-image.png](a-fb28/shots/share/fb-event-image.png)
- [a-fb28/summary.txt](a-fb28/summary.txt)
- [a-live36/cap/event-reminder/androidx.work.workdb](a-live36/cap/event-reminder/androidx.work.workdb)
- [a-live36/cap/event-reminder/androidx.work.workdb-shm](a-live36/cap/event-reminder/androidx.work.workdb-shm)
- [a-live36/cap/event-reminder/androidx.work.workdb-wal](a-live36/cap/event-reminder/androidx.work.workdb-wal)
- [a-live36/cap/event-reminder/jobscheduler.txt](a-live36/cap/event-reminder/jobscheduler.txt)
- [a-live36/cap/event-reminder/notif.txt](a-live36/cap/event-reminder/notif.txt)
- [a-live36/cap/event-reminder/services.txt](a-live36/cap/event-reminder/services.txt)
- [a-live36/cap/event-reminder/ui.xml](a-live36/cap/event-reminder/ui.xml)
- [a-live36/cap/event-reminder/workdb.txt](a-live36/cap/event-reminder/workdb.txt)
- [a-live36/host-after-run.txt](a-live36/host-after-run.txt)
- [a-live36/job-status.txt](a-live36/job-status.txt)
- [a-live36/live/ev.txt](a-live36/live/ev.txt)
- [a-live36/live/notif-ev.txt](a-live36/live/notif-ev.txt)
- [a-live36/logcat-all.txt](a-live36/logcat-all.txt)
- [a-live36/logcat-dump.txt](a-live36/logcat-dump.txt)
- [a-live36/logcat-findings.txt](a-live36/logcat-findings.txt)
- [a-live36/logcat-live.txt](a-live36/logcat-live.txt)
- [a-live36/shots/live/10-ribbon-03s.png](a-live36/shots/live/10-ribbon-03s.png)
- [a-live36/shots/live/11-ribbon-09s.png](a-live36/shots/live/11-ribbon-09s.png)
- [a-live36/shots/live/12-card.png](a-live36/shots/live/12-card.png)
- [a-live36/shots/upc/event-reminder.png](a-live36/shots/upc/event-reminder.png)
- [a-live36/summary.txt](a-live36/summary.txt)
- [a-live36/update.json](a-live36/update.json)
- [a-motion19/gfx-tmp.txt](a-motion19/gfx-tmp.txt)
- [a-motion19/host-after-run.txt](a-motion19/host-after-run.txt)
- [a-motion19/job-status.txt](a-motion19/job-status.txt)
- [a-motion19/logcat-all.txt](a-motion19/logcat-all.txt)
- [a-motion19/logcat-dump.txt](a-motion19/logcat-dump.txt)
- [a-motion19/logcat-findings.txt](a-motion19/logcat-findings.txt)
- [a-motion19/logcat-live.txt](a-motion19/logcat-live.txt)
- [a-motion19/rec/rm-bookopen.mp4](a-motion19/rec/rm-bookopen.mp4)
- [a-motion19/rec/rm-rosette.mp4](a-motion19/rec/rm-rosette.mp4)
- [a-motion19/rec/rm-want.mp4](a-motion19/rec/rm-want.mp4)
- [a-motion19/shots/motion/article.png](a-motion19/shots/motion/article.png)
- [a-motion19/shots/motion/card-a.png](a-motion19/shots/motion/card-a.png)
- [a-motion19/shots/motion/card-b.png](a-motion19/shots/motion/card-b.png)
- [a-motion19/shots/motion/ctl-03s.png](a-motion19/shots/motion/ctl-03s.png)
- [a-motion19/shots/motion/ctl-08s.png](a-motion19/shots/motion/ctl-08s.png)
- [a-motion19/shots/motion/ctl-14s.png](a-motion19/shots/motion/ctl-14s.png)
- [a-motion19/shots/motion/ribbon-02s.png](a-motion19/shots/motion/ribbon-02s.png)
- [a-motion19/shots/motion/ribbon-06s.png](a-motion19/shots/motion/ribbon-06s.png)
- [a-motion19/shots/motion/ribbon-12s.png](a-motion19/shots/motion/ribbon-12s.png)
- [a-motion19/shots/motion/ribbon-20s.png](a-motion19/shots/motion/ribbon-20s.png)
- [a-motion19/shots/motion/saver-03s.png](a-motion19/shots/motion/saver-03s.png)
- [a-motion19/shots/motion/saver-08s.png](a-motion19/shots/motion/saver-08s.png)
- [a-motion19/shots/motion/saver-14s.png](a-motion19/shots/motion/saver-14s.png)
- [a-motion19/shots/motion/surprise.png](a-motion19/shots/motion/surprise.png)
- [a-motion19/shots/rec-rm-want.png](a-motion19/shots/rec-rm-want.png)
- [a-motion19/summary.txt](a-motion19/summary.txt)
- [a-year19/a11ytree-y-at-35s.txt](a-year19/a11ytree-y-at-35s.txt)
- [a-year19/a11ytree-y-at-52s.txt](a-year19/a11ytree-y-at-52s.txt)
- [a-year19/a11ytree-y-closed.txt](a-year19/a11ytree-y-closed.txt)
- [a-year19/a11ytree-y-hold-0.txt](a-year19/a11ytree-y-hold-0.txt)
- [a-year19/a11ytree-y-hold-after.txt](a-year19/a11ytree-y-hold-after.txt)
- [a-year19/a11ytree-y-hold-during.txt](a-year19/a11ytree-y-hold-during.txt)
- [a-year19/a11ytree-y-left2.txt](a-year19/a11ytree-y-left2.txt)
- [a-year19/a11ytree-y-right.txt](a-year19/a11ytree-y-right.txt)
- [a-year19/host-after-run.txt](a-year19/host-after-run.txt)
- [a-year19/job-status.txt](a-year19/job-status.txt)
- [a-year19/logcat-all.txt](a-year19/logcat-all.txt)
- [a-year19/logcat-dump.txt](a-year19/logcat-dump.txt)
- [a-year19/logcat-findings.txt](a-year19/logcat-findings.txt)
- [a-year19/logcat-live.txt](a-year19/logcat-live.txt)
- [a-year19/rec/confetti-want-dev.mp4](a-year19/rec/confetti-want-dev.mp4)
- [a-year19/shots/rec-confetti-want-dev.png](a-year19/shots/rec-confetti-want-dev.png)
- [a-year19/shots/year/10-story-07s.png](a-year19/shots/year/10-story-07s.png)
- [a-year19/shots/year/10-story-13s.png](a-year19/shots/year/10-story-13s.png)
- [a-year19/shots/year/10-story-19s.png](a-year19/shots/year/10-story-19s.png)
- [a-year19/shots/year/10-story-30s.png](a-year19/shots/year/10-story-30s.png)
- [a-year19/shots/year/11-story-end.png](a-year19/shots/year/11-story-end.png)
- [a-year19/shots/year/12-after-hold.png](a-year19/shots/year/12-after-hold.png)
- [a-year19/shots/year/13-after-swipe-down.png](a-year19/shots/year/13-after-swipe-down.png)
- [a-year19/shots/year/20-loans.png](a-year19/shots/year/20-loans.png)
- [a-year19/summary.txt](a-year19/summary.txt)
- [a-year19/year-live.txt](a-year19/year-live.txt)
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
- [abstart-3/shots/ab-base-1.png](abstart-3/shots/ab-base-1.png)
- [abstart-3/shots/ab-base-2.png](abstart-3/shots/ab-base-2.png)
- [abstart-3/shots/ab-new-0.png](abstart-3/shots/ab-new-0.png)
- [abstart-3/shots/ab-new-1.png](abstart-3/shots/ab-new-1.png)
- [abstart-3/shots/ab-new-2.png](abstart-3/shots/ab-new-2.png)
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
- [article19/host-after-run.txt](article19/host-after-run.txt)
- [article19/job-status.txt](article19/job-status.txt)
- [article19/logcat-all.txt](article19/logcat-all.txt)
- [article19/logcat-dump.txt](article19/logcat-dump.txt)
- [article19/logcat-findings.txt](article19/logcat-findings.txt)
- [article19/logcat-live.txt](article19/logcat-live.txt)
- [article19/shots/func/article-0.png](article19/shots/func/article-0.png)
- [article19/shots/func/article-1-small-scroll.png](article19/shots/func/article-1-small-scroll.png)
- [article19/shots/func/article-2-down.png](article19/shots/func/article-2-down.png)
- [article19/shots/func/article-2b-down2.png](article19/shots/func/article-2b-down2.png)
- [article19/shots/func/article-3-up.png](article19/shots/func/article-3-up.png)
- [article19/shots/func/article-browser.png](article19/shots/func/article-browser.png)
- [article19/shots/func/reader-sheet-0.png](article19/shots/func/reader-sheet-0.png)
- [article19/shots/func/reader-sheet-1-large.png](article19/shots/func/reader-sheet-1-large.png)
- [article19/shots/share/article-0-sheet.png](article19/shots/share/article-0-sheet.png)
- [article19/shots/share/article-1-chooser.png](article19/shots/share/article-1-chooser.png)
- [article19/shots/share/article-image.png](article19/shots/share/article-image.png)
- [article19/summary.txt](article19/summary.txt)
- [b-live361/cap/event-reminder/jobscheduler.txt](b-live361/cap/event-reminder/jobscheduler.txt)
- [b-live361/cap/event-reminder/notif.txt](b-live361/cap/event-reminder/notif.txt)
- [b-live361/cap/event-reminder/services.txt](b-live361/cap/event-reminder/services.txt)
- [b-live361/cap/event-reminder/ui.xml](b-live361/cap/event-reminder/ui.xml)
- [b-live361/cap/event-reminder/workdb.txt](b-live361/cap/event-reminder/workdb.txt)
- [b-live361/host-after-run.txt](b-live361/host-after-run.txt)
- [b-live361/job-status.txt](b-live361/job-status.txt)
- [b-live361/l-finish-n.txt](b-live361/l-finish-n.txt)
- [b-live361/live/ev.txt](b-live361/live/ev.txt)
- [b-live361/live/l1.txt](b-live361/live/l1.txt)
- [b-live361/live/l10.txt](b-live361/live/l10.txt)
- [b-live361/live/l11.txt](b-live361/live/l11.txt)
- [b-live361/live/l12.txt](b-live361/live/l12.txt)
- [b-live361/live/l13.txt](b-live361/live/l13.txt)
- [b-live361/live/l14.txt](b-live361/live/l14.txt)
- [b-live361/live/l15.txt](b-live361/live/l15.txt)
- [b-live361/live/l16.txt](b-live361/live/l16.txt)
- [b-live361/live/l17.txt](b-live361/live/l17.txt)
- [b-live361/live/l18.txt](b-live361/live/l18.txt)
- [b-live361/live/l19.txt](b-live361/live/l19.txt)
- [b-live361/live/l2.txt](b-live361/live/l2.txt)
- [b-live361/live/l20.txt](b-live361/live/l20.txt)
- [b-live361/live/l21.txt](b-live361/live/l21.txt)
- [b-live361/live/l22.txt](b-live361/live/l22.txt)
- [b-live361/live/l23.txt](b-live361/live/l23.txt)
- [b-live361/live/l24.txt](b-live361/live/l24.txt)
- [b-live361/live/l25.txt](b-live361/live/l25.txt)
- [b-live361/live/l26.txt](b-live361/live/l26.txt)
- [b-live361/live/l27.txt](b-live361/live/l27.txt)
- [b-live361/live/l28.txt](b-live361/live/l28.txt)
- [b-live361/live/l29.txt](b-live361/live/l29.txt)
- [b-live361/live/l3.txt](b-live361/live/l3.txt)
- [b-live361/live/l30.txt](b-live361/live/l30.txt)
- [b-live361/live/l4.txt](b-live361/live/l4.txt)
- [b-live361/live/l5.txt](b-live361/live/l5.txt)
- [b-live361/live/l6.txt](b-live361/live/l6.txt)
- [b-live361/live/l7.txt](b-live361/live/l7.txt)
- [b-live361/live/l8.txt](b-live361/live/l8.txt)
- [b-live361/live/l9.txt](b-live361/live/l9.txt)
- [b-live361/live/notif-ev.txt](b-live361/live/notif-ev.txt)
- [b-live361/live/notif-l1.txt](b-live361/live/notif-l1.txt)
- [b-live361/live/notif-l10.txt](b-live361/live/notif-l10.txt)
- [b-live361/live/notif-l11.txt](b-live361/live/notif-l11.txt)
- [b-live361/live/notif-l12.txt](b-live361/live/notif-l12.txt)
- [b-live361/live/notif-l13.txt](b-live361/live/notif-l13.txt)
- [b-live361/live/notif-l14.txt](b-live361/live/notif-l14.txt)
- [b-live361/live/notif-l15.txt](b-live361/live/notif-l15.txt)
- [b-live361/live/notif-l16.txt](b-live361/live/notif-l16.txt)
- [b-live361/live/notif-l17.txt](b-live361/live/notif-l17.txt)
- [b-live361/live/notif-l18.txt](b-live361/live/notif-l18.txt)
- [b-live361/live/notif-l19.txt](b-live361/live/notif-l19.txt)
- [b-live361/live/notif-l2.txt](b-live361/live/notif-l2.txt)
- [b-live361/live/notif-l20.txt](b-live361/live/notif-l20.txt)
- [b-live361/live/notif-l21.txt](b-live361/live/notif-l21.txt)
- [b-live361/live/notif-l22.txt](b-live361/live/notif-l22.txt)
- [b-live361/live/notif-l23.txt](b-live361/live/notif-l23.txt)
- [b-live361/live/notif-l24.txt](b-live361/live/notif-l24.txt)
- [b-live361/live/notif-l25.txt](b-live361/live/notif-l25.txt)
- [b-live361/live/notif-l26.txt](b-live361/live/notif-l26.txt)
- [b-live361/live/notif-l27.txt](b-live361/live/notif-l27.txt)
- [b-live361/live/notif-l28.txt](b-live361/live/notif-l28.txt)
- [b-live361/live/notif-l29.txt](b-live361/live/notif-l29.txt)
- [b-live361/live/notif-l3.txt](b-live361/live/notif-l3.txt)
- [b-live361/live/notif-l30.txt](b-live361/live/notif-l30.txt)
- [b-live361/live/notif-l4.txt](b-live361/live/notif-l4.txt)
- [b-live361/live/notif-l5.txt](b-live361/live/notif-l5.txt)
- [b-live361/live/notif-l6.txt](b-live361/live/notif-l6.txt)
- [b-live361/live/notif-l7.txt](b-live361/live/notif-l7.txt)
- [b-live361/live/notif-l8.txt](b-live361/live/notif-l8.txt)
- [b-live361/live/notif-l9.txt](b-live361/live/notif-l9.txt)
- [b-live361/live/shade.xml](b-live361/live/shade.xml)
- [b-live361/logcat-all.txt](b-live361/logcat-all.txt)
- [b-live361/logcat-dump.txt](b-live361/logcat-dump.txt)
- [b-live361/logcat-findings.txt](b-live361/logcat-findings.txt)
- [b-live361/logcat-live.txt](b-live361/logcat-live.txt)
- [b-live361/notif-l-finish.txt](b-live361/notif-l-finish.txt)
- [b-live361/shots/live/00-dialog.png](b-live361/shots/live/00-dialog.png)
- [b-live361/shots/live/01-statusbar-chip.png](b-live361/shots/live/01-statusbar-chip.png)
- [b-live361/shots/live/02-shade.png](b-live361/shots/live/02-shade.png)
- [b-live361/shots/live/03-statusbar-later.png](b-live361/shots/live/03-statusbar-later.png)
- [b-live361/shots/live/04-ready.png](b-live361/shots/live/04-ready.png)
- [b-live361/shots/live/10-ribbon-03s.png](b-live361/shots/live/10-ribbon-03s.png)
- [b-live361/shots/live/11-ribbon-09s.png](b-live361/shots/live/11-ribbon-09s.png)
- [b-live361/shots/live/12-card.png](b-live361/shots/live/12-card.png)
- [b-live361/shots/upc/event-reminder.png](b-live361/shots/upc/event-reminder.png)
- [b-live361/summary.txt](b-live361/summary.txt)
- [b-live361/update.json](b-live361/update.json)
- [c-surprise28/host-after-run.txt](c-surprise28/host-after-run.txt)
- [c-surprise28/job-status.txt](c-surprise28/job-status.txt)
- [c-surprise28/logcat-all.txt](c-surprise28/logcat-all.txt)
- [c-surprise28/logcat-dump.txt](c-surprise28/logcat-dump.txt)
- [c-surprise28/logcat-findings.txt](c-surprise28/logcat-findings.txt)
- [c-surprise28/logcat-live.txt](c-surprise28/logcat-live.txt)
- [c-surprise28/memprobe-book.txt](c-surprise28/memprobe-book.txt)
- [c-surprise28/mp.sh](c-surprise28/mp.sh)
- [c-surprise28/summary.txt](c-surprise28/summary.txt)
- [cardcrash/host-after-run.txt](cardcrash/host-after-run.txt)
- [cardcrash/job-status.txt](cardcrash/job-status.txt)
- [cardcrash/logcat-all.txt](cardcrash/logcat-all.txt)
- [cardcrash/logcat-dump.txt](cardcrash/logcat-dump.txt)
- [cardcrash/logcat-findings.txt](cardcrash/logcat-findings.txt)
- [cardcrash/logcat-live.txt](cardcrash/logcat-live.txt)
- [cardcrash/summary.txt](cardcrash/summary.txt)
- [confetti19/host-after-run.txt](confetti19/host-after-run.txt)
- [confetti19/job-status.txt](confetti19/job-status.txt)
- [confetti19/logcat-all.txt](confetti19/logcat-all.txt)
- [confetti19/logcat-dump.txt](confetti19/logcat-dump.txt)
- [confetti19/logcat-findings.txt](confetti19/logcat-findings.txt)
- [confetti19/logcat-live.txt](confetti19/logcat-live.txt)
- [confetti19/rec/confetti-reminder.mp4](confetti19/rec/confetti-reminder.mp4)
- [confetti19/rec/confetti-want.mp4](confetti19/rec/confetti-want.mp4)
- [confetti19/shots/rec-confetti-reminder.png](confetti19/shots/rec-confetti-reminder.png)
- [confetti19/shots/rec-confetti-want.png](confetti19/shots/rec-confetti-want.png)
- [confetti19/shots/share/book-0-sheet.png](confetti19/shots/share/book-0-sheet.png)
- [confetti19/shots/share/book-1-chooser.png](confetti19/shots/share/book-1-chooser.png)
- [confetti19/shots/share/book-image.png](confetti19/shots/share/book-image.png)
- [confetti19/shots/share/book-link-chooser.png](confetti19/shots/share/book-link-chooser.png)
- [confetti19/shots/share/event-0-sheet.png](confetti19/shots/share/event-0-sheet.png)
- [confetti19/shots/share/event-1-chooser.png](confetti19/shots/share/event-1-chooser.png)
- [confetti19/shots/share/event-image.png](confetti19/shots/share/event-image.png)
- [confetti19/summary.txt](confetti19/summary.txt)
- [drawn19/drawn-base-lines.txt](drawn19/drawn-base-lines.txt)
- [drawn19/drawn-base.txt](drawn19/drawn-base.txt)
- [drawn19/drawn-new-lines.txt](drawn19/drawn-new-lines.txt)
- [drawn19/drawn-new.txt](drawn19/drawn-new.txt)
- [drawn19/host-after-run.txt](drawn19/host-after-run.txt)
- [drawn19/job-status.txt](drawn19/job-status.txt)
- [drawn19/logcat-all.txt](drawn19/logcat-all.txt)
- [drawn19/logcat-dump.txt](drawn19/logcat-dump.txt)
- [drawn19/logcat-findings.txt](drawn19/logcat-findings.txt)
- [drawn19/logcat-live.txt](drawn19/logcat-live.txt)
- [drawn19/summary.txt](drawn19/summary.txt)
- [fb28/gfx-tmp.txt](fb28/gfx-tmp.txt)
- [fb28/host-after-run.txt](fb28/host-after-run.txt)
- [fb28/job-status.txt](fb28/job-status.txt)
- [fb28/logcat-all.txt](fb28/logcat-all.txt)
- [fb28/logcat-dump.txt](fb28/logcat-dump.txt)
- [fb28/logcat-findings.txt](fb28/logcat-findings.txt)
- [fb28/logcat-live.txt](fb28/logcat-live.txt)
- [fb28/rec/fb-bookopen.mp4](fb28/rec/fb-bookopen.mp4)
- [fb28/shots/fb/article.png](fb28/shots/fb/article.png)
- [fb28/shots/fb/ribbon-03s.png](fb28/shots/fb/ribbon-03s.png)
- [fb28/shots/fb/ribbon-10s.png](fb28/shots/fb/ribbon-10s.png)
- [fb28/shots/fb/ribbon-18s.png](fb28/shots/fb/ribbon-18s.png)
- [fb28/shots/fb/surprise.png](fb28/shots/fb/surprise.png)
- [fb28/shots/rec-fb-bookopen.png](fb28/shots/rec-fb-bookopen.png)
- [fb28/shots/share/fb-event-0-sheet.png](fb28/shots/share/fb-event-0-sheet.png)
- [fb28/shots/share/fb-event-1-chooser.png](fb28/shots/share/fb-event-1-chooser.png)
- [fb28/shots/share/fb-event-image.png](fb28/shots/share/fb-event-image.png)
- [fb28/summary.txt](fb28/summary.txt)
- [func19/debug/1-ready.xml](func19/debug/1-ready.xml)
- [func19/debug/2-ready.xml](func19/debug/2-ready.xml)
- [func19/debug/3-ready.xml](func19/debug/3-ready.xml)
- [func19/debug/4-ready.xml](func19/debug/4-ready.xml)
- [func19/debug/5-ready.xml](func19/debug/5-ready.xml)
- [func19/debug/6-ready.xml](func19/debug/6-ready.xml)
- [func19/debug/7-tab-home.xml](func19/debug/7-tab-home.xml)
- [func19/gfx-tmp.txt](func19/gfx-tmp.txt)
- [func19/host-after-run.txt](func19/host-after-run.txt)
- [func19/job-status.txt](func19/job-status.txt)
- [func19/logcat-all.txt](func19/logcat-all.txt)
- [func19/logcat-dump.txt](func19/logcat-dump.txt)
- [func19/logcat-findings.txt](func19/logcat-findings.txt)
- [func19/logcat-live.txt](func19/logcat-live.txt)
- [func19/rec/bookopen-home.mp4](func19/rec/bookopen-home.mp4)
- [func19/rec/bookopen.mp4](func19/rec/bookopen.mp4)
- [func19/rec/confetti-want.mp4](func19/rec/confetti-want.mp4)
- [func19/rec/rosette.mp4](func19/rec/rosette.mp4)
- [func19/rec/surprise-dice.mp4](func19/rec/surprise-dice.mp4)
- [func19/shots/debug/1-ready.png](func19/shots/debug/1-ready.png)
- [func19/shots/debug/2-ready.png](func19/shots/debug/2-ready.png)
- [func19/shots/debug/3-ready.png](func19/shots/debug/3-ready.png)
- [func19/shots/debug/4-ready.png](func19/shots/debug/4-ready.png)
- [func19/shots/debug/5-ready.png](func19/shots/debug/5-ready.png)
- [func19/shots/debug/6-ready.png](func19/shots/debug/6-ready.png)
- [func19/shots/debug/7-tab-home.png](func19/shots/debug/7-tab-home.png)
- [func19/shots/func/article-0.png](func19/shots/func/article-0.png)
- [func19/shots/func/article-1-small-scroll.png](func19/shots/func/article-1-small-scroll.png)
- [func19/shots/func/article-2-down.png](func19/shots/func/article-2-down.png)
- [func19/shots/func/article-3-up.png](func19/shots/func/article-3-up.png)
- [func19/shots/func/article-browser.png](func19/shots/func/article-browser.png)
- [func19/shots/func/empty-want.png](func19/shots/func/empty-want.png)
- [func19/shots/func/group-0.png](func19/shots/func/group-0.png)
- [func19/shots/func/group-1-calendar.png](func19/shots/func/group-1-calendar.png)
- [func19/shots/func/reader-sheet-0.png](func19/shots/func/reader-sheet-0.png)
- [func19/shots/func/reader-sheet-1-large.png](func19/shots/func/reader-sheet-1-large.png)
- [func19/shots/func/refresh-1-pull.png](func19/shots/func/refresh-1-pull.png)
- [func19/shots/func/refresh-2-pull.png](func19/shots/func/refresh-2-pull.png)
- [func19/shots/func/refresh-3-released.png](func19/shots/func/refresh-3-released.png)
- [func19/shots/func/refresh-4-after.png](func19/shots/func/refresh-4-after.png)
- [func19/shots/func/ribbon-05s.png](func19/shots/func/ribbon-05s.png)
- [func19/shots/func/ribbon-08s.png](func19/shots/func/ribbon-08s.png)
- [func19/shots/func/ribbon-12s.png](func19/shots/func/ribbon-12s.png)
- [func19/shots/func/ribbon-16s.png](func19/shots/func/ribbon-16s.png)
- [func19/shots/func/ribbon-20s.png](func19/shots/func/ribbon-20s.png)
- [func19/shots/func/ribbon-24s.png](func19/shots/func/ribbon-24s.png)
- [func19/shots/func/ribbon-28s.png](func19/shots/func/ribbon-28s.png)
- [func19/shots/func/ribbon-34s.png](func19/shots/func/ribbon-34s.png)
- [func19/shots/func/ribbon-40s.png](func19/shots/func/ribbon-40s.png)
- [func19/shots/func/ribbon-again-a.png](func19/shots/func/ribbon-again-a.png)
- [func19/shots/func/ribbon-again-b.png](func19/shots/func/ribbon-again-b.png)
- [func19/shots/func/ribbon-scroll-03s.png](func19/shots/func/ribbon-scroll-03s.png)
- [func19/shots/func/ribbon-scroll-a.png](func19/shots/func/ribbon-scroll-a.png)
- [func19/shots/func/ribbon-scroll-b.png](func19/shots/func/ribbon-scroll-b.png)
- [func19/shots/func/ribbon-scroll-c.png](func19/shots/func/ribbon-scroll-c.png)
- [func19/shots/func/shake-1.png](func19/shots/func/shake-1.png)
- [func19/shots/func/shake-2.png](func19/shots/func/shake-2.png)
- [func19/shots/func/surprise-result.png](func19/shots/func/surprise-result.png)
- [func19/shots/func/surprise-shuffle.png](func19/shots/func/surprise-shuffle.png)
- [func19/shots/func/viewer-0.png](func19/shots/func/viewer-0.png)
- [func19/shots/func/viewer-info.png](func19/shots/func/viewer-info.png)
- [func19/shots/rec-bookopen-home.png](func19/shots/rec-bookopen-home.png)
- [func19/shots/rec-confetti-want.png](func19/shots/rec-confetti-want.png)
- [func19/shots/rec-surprise-dice.png](func19/shots/rec-surprise-dice.png)
- [func19/shots/share/article-0-sheet.png](func19/shots/share/article-0-sheet.png)
- [func19/shots/share/article-1-chooser.png](func19/shots/share/article-1-chooser.png)
- [func19/shots/share/article-image.png](func19/shots/share/article-image.png)
- [func19/shots/share/book-0-sheet.png](func19/shots/share/book-0-sheet.png)
- [func19/shots/share/book-1-chooser.png](func19/shots/share/book-1-chooser.png)
- [func19/shots/share/book-image.png](func19/shots/share/book-image.png)
- [func19/shots/share/book-link-chooser.png](func19/shots/share/book-link-chooser.png)
- [func19/shots/share/event-0-sheet.png](func19/shots/share/event-0-sheet.png)
- [func19/shots/share/event-1-chooser.png](func19/shots/share/event-1-chooser.png)
- [func19/shots/share/event-image.png](func19/shots/share/event-image.png)
- [func19/summary.txt](func19/summary.txt)
- [func19b/gfx-tmp.txt](func19b/gfx-tmp.txt)
- [func19b/host-after-run.txt](func19b/host-after-run.txt)
- [func19b/job-status.txt](func19b/job-status.txt)
- [func19b/logcat-all.txt](func19b/logcat-all.txt)
- [func19b/logcat-dump.txt](func19b/logcat-dump.txt)
- [func19b/logcat-findings.txt](func19b/logcat-findings.txt)
- [func19b/logcat-live.txt](func19b/logcat-live.txt)
- [func19b/rec/bookopen-home.mp4](func19b/rec/bookopen-home.mp4)
- [func19b/rec/bookopen.mp4](func19b/rec/bookopen.mp4)
- [func19b/rec/confetti-reminder.mp4](func19b/rec/confetti-reminder.mp4)
- [func19b/rec/rosette.mp4](func19b/rec/rosette.mp4)
- [func19b/rec/surprise-dice.mp4](func19b/rec/surprise-dice.mp4)
- [func19b/shots/func/article-0.png](func19b/shots/func/article-0.png)
- [func19b/shots/func/article-1-small-scroll.png](func19b/shots/func/article-1-small-scroll.png)
- [func19b/shots/func/article-2-down.png](func19b/shots/func/article-2-down.png)
- [func19b/shots/func/article-2b-down2.png](func19b/shots/func/article-2b-down2.png)
- [func19b/shots/func/article-3-up.png](func19b/shots/func/article-3-up.png)
- [func19b/shots/func/article-browser.png](func19b/shots/func/article-browser.png)
- [func19b/shots/func/reader-sheet-0.png](func19b/shots/func/reader-sheet-0.png)
- [func19b/shots/func/reader-sheet-1-large.png](func19b/shots/func/reader-sheet-1-large.png)
- [func19b/shots/func/refresh-0-before.png](func19b/shots/func/refresh-0-before.png)
- [func19b/shots/func/refresh-1-pull.png](func19b/shots/func/refresh-1-pull.png)
- [func19b/shots/func/refresh-2-pull.png](func19b/shots/func/refresh-2-pull.png)
- [func19b/shots/func/refresh-3-released.png](func19b/shots/func/refresh-3-released.png)
- [func19b/shots/func/refresh-4-after.png](func19b/shots/func/refresh-4-after.png)
- [func19b/shots/func/ribbon-03s.png](func19b/shots/func/ribbon-03s.png)
- [func19b/shots/func/ribbon-05s.png](func19b/shots/func/ribbon-05s.png)
- [func19b/shots/func/ribbon-08s.png](func19b/shots/func/ribbon-08s.png)
- [func19b/shots/func/ribbon-12s.png](func19b/shots/func/ribbon-12s.png)
- [func19b/shots/func/ribbon-16s.png](func19b/shots/func/ribbon-16s.png)
- [func19b/shots/func/ribbon-20s.png](func19b/shots/func/ribbon-20s.png)
- [func19b/shots/func/ribbon-24s.png](func19b/shots/func/ribbon-24s.png)
- [func19b/shots/func/ribbon-28s.png](func19b/shots/func/ribbon-28s.png)
- [func19b/shots/func/ribbon-34s.png](func19b/shots/func/ribbon-34s.png)
- [func19b/shots/func/ribbon-40s.png](func19b/shots/func/ribbon-40s.png)
- [func19b/shots/func/ribbon-again-a.png](func19b/shots/func/ribbon-again-a.png)
- [func19b/shots/func/ribbon-again-b.png](func19b/shots/func/ribbon-again-b.png)
- [func19b/shots/func/ribbon-scroll-03s.png](func19b/shots/func/ribbon-scroll-03s.png)
- [func19b/shots/func/ribbon-scroll-a.png](func19b/shots/func/ribbon-scroll-a.png)
- [func19b/shots/func/ribbon-scroll-b.png](func19b/shots/func/ribbon-scroll-b.png)
- [func19b/shots/func/ribbon-scroll-c.png](func19b/shots/func/ribbon-scroll-c.png)
- [func19b/shots/func/shake-1.png](func19b/shots/func/shake-1.png)
- [func19b/shots/func/shake-2.png](func19b/shots/func/shake-2.png)
- [func19b/shots/func/surprise-result.png](func19b/shots/func/surprise-result.png)
- [func19b/shots/func/surprise-shuffle.png](func19b/shots/func/surprise-shuffle.png)
- [func19b/shots/func/viewer-0.png](func19b/shots/func/viewer-0.png)
- [func19b/shots/func/viewer-info.png](func19b/shots/func/viewer-info.png)
- [func19b/shots/func/viewer-share.png](func19b/shots/func/viewer-share.png)
- [func19b/shots/func/viewer-site.png](func19b/shots/func/viewer-site.png)
- [func19b/shots/rec-bookopen-home.png](func19b/shots/rec-bookopen-home.png)
- [func19b/shots/rec-bookopen.png](func19b/shots/rec-bookopen.png)
- [func19b/shots/rec-confetti-reminder.png](func19b/shots/rec-confetti-reminder.png)
- [func19b/shots/rec-rosette.png](func19b/shots/rec-rosette.png)
- [func19b/shots/rec-surprise-dice.png](func19b/shots/rec-surprise-dice.png)
- [func19b/shots/share/article-0-sheet.png](func19b/shots/share/article-0-sheet.png)
- [func19b/shots/share/article-1-chooser.png](func19b/shots/share/article-1-chooser.png)
- [func19b/shots/share/event-0-sheet.png](func19b/shots/share/event-0-sheet.png)
- [func19b/shots/share/event-1-chooser.png](func19b/shots/share/event-1-chooser.png)
- [func19b/summary.txt](func19b/summary.txt)
- [idle19/cpu/base-article-t0.txt](idle19/cpu/base-article-t0.txt)
- [idle19/cpu/base-article-t1.txt](idle19/cpu/base-article-t1.txt)
- [idle19/cpu/base-background-t0.txt](idle19/cpu/base-background-t0.txt)
- [idle19/cpu/base-background-t1.txt](idle19/cpu/base-background-t1.txt)
- [idle19/cpu/base-catalog-t0.txt](idle19/cpu/base-catalog-t0.txt)
- [idle19/cpu/base-catalog-t1.txt](idle19/cpu/base-catalog-t1.txt)
- [idle19/cpu/base-home-0-25s-t0.txt](idle19/cpu/base-home-0-25s-t0.txt)
- [idle19/cpu/base-home-0-25s-t1.txt](idle19/cpu/base-home-0-25s-t1.txt)
- [idle19/cpu/base-home-37-67s-t0.txt](idle19/cpu/base-home-37-67s-t0.txt)
- [idle19/cpu/base-home-37-67s-t1.txt](idle19/cpu/base-home-37-67s-t1.txt)
- [idle19/cpu/base-home-return-t0.txt](idle19/cpu/base-home-return-t0.txt)
- [idle19/cpu/base-home-return-t1.txt](idle19/cpu/base-home-return-t1.txt)
- [idle19/cpu/base-my-t0.txt](idle19/cpu/base-my-t0.txt)
- [idle19/cpu/base-my-t1.txt](idle19/cpu/base-my-t1.txt)
- [idle19/cpu/base-news-t0.txt](idle19/cpu/base-news-t0.txt)
- [idle19/cpu/base-news-t1.txt](idle19/cpu/base-news-t1.txt)
- [idle19/cpu/new-article-t0.txt](idle19/cpu/new-article-t0.txt)
- [idle19/cpu/new-article-t1.txt](idle19/cpu/new-article-t1.txt)
- [idle19/cpu/new-background-t0.txt](idle19/cpu/new-background-t0.txt)
- [idle19/cpu/new-background-t1.txt](idle19/cpu/new-background-t1.txt)
- [idle19/cpu/new-card-0-30s-t0.txt](idle19/cpu/new-card-0-30s-t0.txt)
- [idle19/cpu/new-card-0-30s-t1.txt](idle19/cpu/new-card-0-30s-t1.txt)
- [idle19/cpu/new-card-35-65s-t0.txt](idle19/cpu/new-card-35-65s-t0.txt)
- [idle19/cpu/new-card-35-65s-t1.txt](idle19/cpu/new-card-35-65s-t1.txt)
- [idle19/cpu/new-card-jitter-t0.txt](idle19/cpu/new-card-jitter-t0.txt)
- [idle19/cpu/new-card-jitter-t1.txt](idle19/cpu/new-card-jitter-t1.txt)
- [idle19/cpu/new-catalog-t0.txt](idle19/cpu/new-catalog-t0.txt)
- [idle19/cpu/new-catalog-t1.txt](idle19/cpu/new-catalog-t1.txt)
- [idle19/cpu/new-home-0-25s-t0.txt](idle19/cpu/new-home-0-25s-t0.txt)
- [idle19/cpu/new-home-0-25s-t1.txt](idle19/cpu/new-home-0-25s-t1.txt)
- [idle19/cpu/new-home-37-67s-t0.txt](idle19/cpu/new-home-37-67s-t0.txt)
- [idle19/cpu/new-home-37-67s-t1.txt](idle19/cpu/new-home-37-67s-t1.txt)
- [idle19/cpu/new-home-return-t0.txt](idle19/cpu/new-home-return-t0.txt)
- [idle19/cpu/new-home-return-t1.txt](idle19/cpu/new-home-return-t1.txt)
- [idle19/cpu/new-my-t0.txt](idle19/cpu/new-my-t0.txt)
- [idle19/cpu/new-my-t1.txt](idle19/cpu/new-my-t1.txt)
- [idle19/cpu/new-news-t0.txt](idle19/cpu/new-news-t0.txt)
- [idle19/cpu/new-news-t1.txt](idle19/cpu/new-news-t1.txt)
- [idle19/debug/1-card-add.xml](idle19/debug/1-card-add.xml)
- [idle19/gfx-tmp.txt](idle19/gfx-tmp.txt)
- [idle19/host-after-run.txt](idle19/host-after-run.txt)
- [idle19/job-status.txt](idle19/job-status.txt)
- [idle19/logcat-all.txt](idle19/logcat-all.txt)
- [idle19/logcat-dump.txt](idle19/logcat-dump.txt)
- [idle19/logcat-findings.txt](idle19/logcat-findings.txt)
- [idle19/logcat-live.txt](idle19/logcat-live.txt)
- [idle19/shots/debug/1-card-add.png](idle19/shots/debug/1-card-add.png)
- [idle19/summary.txt](idle19/summary.txt)
- [idlelong/cpu/base-card-w1-t0.txt](idlelong/cpu/base-card-w1-t0.txt)
- [idlelong/cpu/base-card-w1-t1.txt](idlelong/cpu/base-card-w1-t1.txt)
- [idlelong/cpu/base-card-w2-t0.txt](idlelong/cpu/base-card-w2-t0.txt)
- [idlelong/cpu/base-card-w2-t1.txt](idlelong/cpu/base-card-w2-t1.txt)
- [idlelong/cpu/base-card-w3-t0.txt](idlelong/cpu/base-card-w3-t0.txt)
- [idlelong/cpu/base-card-w3-t1.txt](idlelong/cpu/base-card-w3-t1.txt)
- [idlelong/cpu/base-card-w4-t0.txt](idlelong/cpu/base-card-w4-t0.txt)
- [idlelong/cpu/base-card-w4-t1.txt](idlelong/cpu/base-card-w4-t1.txt)
- [idlelong/cpu/base-card-w5-t0.txt](idlelong/cpu/base-card-w5-t0.txt)
- [idlelong/cpu/base-card-w5-t1.txt](idlelong/cpu/base-card-w5-t1.txt)
- [idlelong/cpu/base-card-w6-t0.txt](idlelong/cpu/base-card-w6-t0.txt)
- [idlelong/cpu/base-card-w6-t1.txt](idlelong/cpu/base-card-w6-t1.txt)
- [idlelong/cpu/base-card-w7-t0.txt](idlelong/cpu/base-card-w7-t0.txt)
- [idlelong/cpu/base-card-w7-t1.txt](idlelong/cpu/base-card-w7-t1.txt)
- [idlelong/cpu/base-home-w1-t0.txt](idlelong/cpu/base-home-w1-t0.txt)
- [idlelong/cpu/base-home-w1-t1.txt](idlelong/cpu/base-home-w1-t1.txt)
- [idlelong/cpu/base-home-w2-t0.txt](idlelong/cpu/base-home-w2-t0.txt)
- [idlelong/cpu/base-home-w2-t1.txt](idlelong/cpu/base-home-w2-t1.txt)
- [idlelong/cpu/base-home-w3-t0.txt](idlelong/cpu/base-home-w3-t0.txt)
- [idlelong/cpu/base-home-w3-t1.txt](idlelong/cpu/base-home-w3-t1.txt)
- [idlelong/cpu/base-home-w4-t0.txt](idlelong/cpu/base-home-w4-t0.txt)
- [idlelong/cpu/base-home-w4-t1.txt](idlelong/cpu/base-home-w4-t1.txt)
- [idlelong/cpu/base-home-w5-t0.txt](idlelong/cpu/base-home-w5-t0.txt)
- [idlelong/cpu/base-home-w5-t1.txt](idlelong/cpu/base-home-w5-t1.txt)
- [idlelong/cpu/base-home-w6-t0.txt](idlelong/cpu/base-home-w6-t0.txt)
- [idlelong/cpu/base-home-w6-t1.txt](idlelong/cpu/base-home-w6-t1.txt)
- [idlelong/cpu/base-home-w7-t0.txt](idlelong/cpu/base-home-w7-t0.txt)
- [idlelong/cpu/base-home-w7-t1.txt](idlelong/cpu/base-home-w7-t1.txt)
- [idlelong/cpu/new-card-w1-t0.txt](idlelong/cpu/new-card-w1-t0.txt)
- [idlelong/cpu/new-card-w1-t1.txt](idlelong/cpu/new-card-w1-t1.txt)
- [idlelong/cpu/new-card-w2-t0.txt](idlelong/cpu/new-card-w2-t0.txt)
- [idlelong/cpu/new-card-w2-t1.txt](idlelong/cpu/new-card-w2-t1.txt)
- [idlelong/cpu/new-card-w3-t0.txt](idlelong/cpu/new-card-w3-t0.txt)
- [idlelong/cpu/new-card-w3-t1.txt](idlelong/cpu/new-card-w3-t1.txt)
- [idlelong/cpu/new-card-w4-t0.txt](idlelong/cpu/new-card-w4-t0.txt)
- [idlelong/cpu/new-card-w4-t1.txt](idlelong/cpu/new-card-w4-t1.txt)
- [idlelong/cpu/new-card-w5-t0.txt](idlelong/cpu/new-card-w5-t0.txt)
- [idlelong/cpu/new-card-w5-t1.txt](idlelong/cpu/new-card-w5-t1.txt)
- [idlelong/cpu/new-card-w6-t0.txt](idlelong/cpu/new-card-w6-t0.txt)
- [idlelong/cpu/new-card-w6-t1.txt](idlelong/cpu/new-card-w6-t1.txt)
- [idlelong/cpu/new-card-w7-t0.txt](idlelong/cpu/new-card-w7-t0.txt)
- [idlelong/cpu/new-card-w7-t1.txt](idlelong/cpu/new-card-w7-t1.txt)
- [idlelong/cpu/new-home-w1-t0.txt](idlelong/cpu/new-home-w1-t0.txt)
- [idlelong/cpu/new-home-w1-t1.txt](idlelong/cpu/new-home-w1-t1.txt)
- [idlelong/cpu/new-home-w2-t0.txt](idlelong/cpu/new-home-w2-t0.txt)
- [idlelong/cpu/new-home-w2-t1.txt](idlelong/cpu/new-home-w2-t1.txt)
- [idlelong/cpu/new-home-w3-t0.txt](idlelong/cpu/new-home-w3-t0.txt)
- [idlelong/cpu/new-home-w3-t1.txt](idlelong/cpu/new-home-w3-t1.txt)
- [idlelong/cpu/new-home-w4-t0.txt](idlelong/cpu/new-home-w4-t0.txt)
- [idlelong/cpu/new-home-w4-t1.txt](idlelong/cpu/new-home-w4-t1.txt)
- [idlelong/cpu/new-home-w5-t0.txt](idlelong/cpu/new-home-w5-t0.txt)
- [idlelong/cpu/new-home-w5-t1.txt](idlelong/cpu/new-home-w5-t1.txt)
- [idlelong/cpu/new-home-w6-t0.txt](idlelong/cpu/new-home-w6-t0.txt)
- [idlelong/cpu/new-home-w6-t1.txt](idlelong/cpu/new-home-w6-t1.txt)
- [idlelong/cpu/new-home-w7-t0.txt](idlelong/cpu/new-home-w7-t0.txt)
- [idlelong/cpu/new-home-w7-t1.txt](idlelong/cpu/new-home-w7-t1.txt)
- [idlelong/gfx-tmp.txt](idlelong/gfx-tmp.txt)
- [idlelong/host-after-run.txt](idlelong/host-after-run.txt)
- [idlelong/job-status.txt](idlelong/job-status.txt)
- [idlelong/logcat-all.txt](idlelong/logcat-all.txt)
- [idlelong/logcat-dump.txt](idlelong/logcat-dump.txt)
- [idlelong/logcat-findings.txt](idlelong/logcat-findings.txt)
- [idlelong/logcat-live.txt](idlelong/logcat-live.txt)
- [idlelong/rec/ribbon-new.mp4](idlelong/rec/ribbon-new.mp4)
- [idlelong/shots/rec-ribbon-new.png](idlelong/shots/rec-ribbon-new.png)
- [idlelong/summary.txt](idlelong/summary.txt)
- [jank19-1/gfx-new-article-full.txt](jank19-1/gfx-new-article-full.txt)
- [jank19-1/gfx-new-article.txt](jank19-1/gfx-new-article.txt)
- [jank19-1/gfx-new-card-after-36s.txt](jank19-1/gfx-new-card-after-36s.txt)
- [jank19-1/gfx-new-card-idle.txt](jank19-1/gfx-new-card-idle.txt)
- [jank19-1/gfx-new-card-tilt.txt](jank19-1/gfx-new-card-tilt.txt)
- [jank19-1/gfx-new-catalog-full.txt](jank19-1/gfx-new-catalog-full.txt)
- [jank19-1/gfx-new-catalog.txt](jank19-1/gfx-new-catalog.txt)
- [jank19-1/gfx-new-home-idle.txt](jank19-1/gfx-new-home-idle.txt)
- [jank19-1/gfx-new-home-ribbon.txt](jank19-1/gfx-new-home-ribbon.txt)
- [jank19-1/gfx-new-home-scroll-full.txt](jank19-1/gfx-new-home-scroll-full.txt)
- [jank19-1/gfx-new-home-scroll.txt](jank19-1/gfx-new-home-scroll.txt)
- [jank19-1/gfx-new-news-full.txt](jank19-1/gfx-new-news-full.txt)
- [jank19-1/gfx-new-news.txt](jank19-1/gfx-new-news.txt)
- [jank19-1/gfx-new-viewer.txt](jank19-1/gfx-new-viewer.txt)
- [jank19-1/host-after-run.txt](jank19-1/host-after-run.txt)
- [jank19-1/job-status.txt](jank19-1/job-status.txt)
- [jank19-1/logcat-all.txt](jank19-1/logcat-all.txt)
- [jank19-1/logcat-dump.txt](jank19-1/logcat-dump.txt)
- [jank19-1/logcat-findings.txt](jank19-1/logcat-findings.txt)
- [jank19-1/logcat-live.txt](jank19-1/logcat-live.txt)
- [jank19-1/mem-new-after-scroll-raw.txt](jank19-1/mem-new-after-scroll-raw.txt)
- [jank19-1/mem-new-after-scroll.txt](jank19-1/mem-new-after-scroll.txt)
- [jank19-1/perf-new-filled.xml](jank19-1/perf-new-filled.xml)
- [jank19-1/shots/jank/new-article.png](jank19-1/shots/jank/new-article.png)
- [jank19-1/shots/jank/new-catalog.png](jank19-1/shots/jank/new-catalog.png)
- [jank19-1/shots/jank/new-home-scroll.png](jank19-1/shots/jank/new-home-scroll.png)
- [jank19-1/shots/jank/new-news.png](jank19-1/shots/jank/new-news.png)
- [jank19-1/shots/jank/new-ribbon-03s.png](jank19-1/shots/jank/new-ribbon-03s.png)
- [jank19-1/shots/jank/new-ribbon-08s.png](jank19-1/shots/jank/new-ribbon-08s.png)
- [jank19-1/shots/jank/new-ribbon-14s.png](jank19-1/shots/jank/new-ribbon-14s.png)
- [jank19-1/shots/jank/new-ribbon-20s.png](jank19-1/shots/jank/new-ribbon-20s.png)
- [jank19-1/shots/jank/new-ribbon-40s.png](jank19-1/shots/jank/new-ribbon-40s.png)
- [jank19-1/shots/jank/new-viewer.png](jank19-1/shots/jank/new-viewer.png)
- [jank19-1/shots/perf/new-filled-1.png](jank19-1/shots/perf/new-filled-1.png)
- [jank19-1/shots/perf/new-filled-2.png](jank19-1/shots/perf/new-filled-2.png)
- [jank19-1/summary.txt](jank19-1/summary.txt)
- [jank19-2/gfx-base-article-full.txt](jank19-2/gfx-base-article-full.txt)
- [jank19-2/gfx-base-article.txt](jank19-2/gfx-base-article.txt)
- [jank19-2/gfx-base-card-after-36s.txt](jank19-2/gfx-base-card-after-36s.txt)
- [jank19-2/gfx-base-card-idle.txt](jank19-2/gfx-base-card-idle.txt)
- [jank19-2/gfx-base-card-tilt.txt](jank19-2/gfx-base-card-tilt.txt)
- [jank19-2/gfx-base-catalog-full.txt](jank19-2/gfx-base-catalog-full.txt)
- [jank19-2/gfx-base-catalog.txt](jank19-2/gfx-base-catalog.txt)
- [jank19-2/gfx-base-home-idle.txt](jank19-2/gfx-base-home-idle.txt)
- [jank19-2/gfx-base-home-ribbon.txt](jank19-2/gfx-base-home-ribbon.txt)
- [jank19-2/gfx-base-home-scroll-full.txt](jank19-2/gfx-base-home-scroll-full.txt)
- [jank19-2/gfx-base-home-scroll.txt](jank19-2/gfx-base-home-scroll.txt)
- [jank19-2/gfx-base-news-full.txt](jank19-2/gfx-base-news-full.txt)
- [jank19-2/gfx-base-news.txt](jank19-2/gfx-base-news.txt)
- [jank19-2/gfx-base-viewer.txt](jank19-2/gfx-base-viewer.txt)
- [jank19-2/gfx-new-article-full.txt](jank19-2/gfx-new-article-full.txt)
- [jank19-2/gfx-new-article.txt](jank19-2/gfx-new-article.txt)
- [jank19-2/gfx-new-card-after-36s.txt](jank19-2/gfx-new-card-after-36s.txt)
- [jank19-2/gfx-new-card-idle.txt](jank19-2/gfx-new-card-idle.txt)
- [jank19-2/gfx-new-card-tilt.txt](jank19-2/gfx-new-card-tilt.txt)
- [jank19-2/gfx-new-catalog-full.txt](jank19-2/gfx-new-catalog-full.txt)
- [jank19-2/gfx-new-catalog.txt](jank19-2/gfx-new-catalog.txt)
- [jank19-2/gfx-new-home-idle.txt](jank19-2/gfx-new-home-idle.txt)
- [jank19-2/gfx-new-home-ribbon.txt](jank19-2/gfx-new-home-ribbon.txt)
- [jank19-2/gfx-new-home-scroll-full.txt](jank19-2/gfx-new-home-scroll-full.txt)
- [jank19-2/gfx-new-home-scroll.txt](jank19-2/gfx-new-home-scroll.txt)
- [jank19-2/gfx-new-news-full.txt](jank19-2/gfx-new-news-full.txt)
- [jank19-2/gfx-new-news.txt](jank19-2/gfx-new-news.txt)
- [jank19-2/gfx-new-viewer.txt](jank19-2/gfx-new-viewer.txt)
- [jank19-2/gfx-totals.txt](jank19-2/gfx-totals.txt)
- [jank19-2/host-after-run.txt](jank19-2/host-after-run.txt)
- [jank19-2/job-status.txt](jank19-2/job-status.txt)
- [jank19-2/logcat-all.txt](jank19-2/logcat-all.txt)
- [jank19-2/logcat-dump.txt](jank19-2/logcat-dump.txt)
- [jank19-2/logcat-findings.txt](jank19-2/logcat-findings.txt)
- [jank19-2/logcat-live.txt](jank19-2/logcat-live.txt)
- [jank19-2/mem-base-after-scroll-raw.txt](jank19-2/mem-base-after-scroll-raw.txt)
- [jank19-2/mem-base-after-scroll.txt](jank19-2/mem-base-after-scroll.txt)
- [jank19-2/mem-new-after-scroll-raw.txt](jank19-2/mem-new-after-scroll-raw.txt)
- [jank19-2/mem-new-after-scroll.txt](jank19-2/mem-new-after-scroll.txt)
- [jank19-2/perf-new-filled.xml](jank19-2/perf-new-filled.xml)
- [jank19-2/shots/jank/base-article.png](jank19-2/shots/jank/base-article.png)
- [jank19-2/shots/jank/base-catalog.png](jank19-2/shots/jank/base-catalog.png)
- [jank19-2/shots/jank/base-home-scroll.png](jank19-2/shots/jank/base-home-scroll.png)
- [jank19-2/shots/jank/base-news.png](jank19-2/shots/jank/base-news.png)
- [jank19-2/shots/jank/base-ribbon-03s.png](jank19-2/shots/jank/base-ribbon-03s.png)
- [jank19-2/shots/jank/base-ribbon-08s.png](jank19-2/shots/jank/base-ribbon-08s.png)
- [jank19-2/shots/jank/base-ribbon-14s.png](jank19-2/shots/jank/base-ribbon-14s.png)
- [jank19-2/shots/jank/base-ribbon-20s.png](jank19-2/shots/jank/base-ribbon-20s.png)
- [jank19-2/shots/jank/base-ribbon-40s.png](jank19-2/shots/jank/base-ribbon-40s.png)
- [jank19-2/shots/jank/base-viewer.png](jank19-2/shots/jank/base-viewer.png)
- [jank19-2/shots/jank/new-article.png](jank19-2/shots/jank/new-article.png)
- [jank19-2/shots/jank/new-catalog.png](jank19-2/shots/jank/new-catalog.png)
- [jank19-2/shots/jank/new-home-scroll.png](jank19-2/shots/jank/new-home-scroll.png)
- [jank19-2/shots/jank/new-news.png](jank19-2/shots/jank/new-news.png)
- [jank19-2/shots/jank/new-ribbon-03s.png](jank19-2/shots/jank/new-ribbon-03s.png)
- [jank19-2/shots/jank/new-ribbon-08s.png](jank19-2/shots/jank/new-ribbon-08s.png)
- [jank19-2/shots/jank/new-ribbon-14s.png](jank19-2/shots/jank/new-ribbon-14s.png)
- [jank19-2/shots/jank/new-ribbon-20s.png](jank19-2/shots/jank/new-ribbon-20s.png)
- [jank19-2/shots/jank/new-ribbon-40s.png](jank19-2/shots/jank/new-ribbon-40s.png)
- [jank19-2/shots/jank/new-viewer.png](jank19-2/shots/jank/new-viewer.png)
- [jank19-2/shots/perf/new-filled-1.png](jank19-2/shots/perf/new-filled-1.png)
- [jank19-2/shots/perf/new-filled-2.png](jank19-2/shots/perf/new-filled-2.png)
- [jank19-2/summary.txt](jank19-2/summary.txt)
- [live36/cap/event-reminder/androidx.work.workdb](live36/cap/event-reminder/androidx.work.workdb)
- [live36/cap/event-reminder/androidx.work.workdb-shm](live36/cap/event-reminder/androidx.work.workdb-shm)
- [live36/cap/event-reminder/androidx.work.workdb-wal](live36/cap/event-reminder/androidx.work.workdb-wal)
- [live36/cap/event-reminder/jobscheduler.txt](live36/cap/event-reminder/jobscheduler.txt)
- [live36/cap/event-reminder/notif.txt](live36/cap/event-reminder/notif.txt)
- [live36/cap/event-reminder/services.txt](live36/cap/event-reminder/services.txt)
- [live36/cap/event-reminder/ui.xml](live36/cap/event-reminder/ui.xml)
- [live36/cap/event-reminder/workdb.txt](live36/cap/event-reminder/workdb.txt)
- [live36/host-after-run.txt](live36/host-after-run.txt)
- [live36/job-status.txt](live36/job-status.txt)
- [live36/l-finish-n.txt](live36/l-finish-n.txt)
- [live36/live/ev.txt](live36/live/ev.txt)
- [live36/live/l1.txt](live36/live/l1.txt)
- [live36/live/l10.txt](live36/live/l10.txt)
- [live36/live/l11.txt](live36/live/l11.txt)
- [live36/live/l12.txt](live36/live/l12.txt)
- [live36/live/l13.txt](live36/live/l13.txt)
- [live36/live/l14.txt](live36/live/l14.txt)
- [live36/live/l15.txt](live36/live/l15.txt)
- [live36/live/l16.txt](live36/live/l16.txt)
- [live36/live/l17.txt](live36/live/l17.txt)
- [live36/live/l18.txt](live36/live/l18.txt)
- [live36/live/l19.txt](live36/live/l19.txt)
- [live36/live/l2.txt](live36/live/l2.txt)
- [live36/live/l20.txt](live36/live/l20.txt)
- [live36/live/l21.txt](live36/live/l21.txt)
- [live36/live/l22.txt](live36/live/l22.txt)
- [live36/live/l23.txt](live36/live/l23.txt)
- [live36/live/l24.txt](live36/live/l24.txt)
- [live36/live/l25.txt](live36/live/l25.txt)
- [live36/live/l26.txt](live36/live/l26.txt)
- [live36/live/l27.txt](live36/live/l27.txt)
- [live36/live/l28.txt](live36/live/l28.txt)
- [live36/live/l29.txt](live36/live/l29.txt)
- [live36/live/l3.txt](live36/live/l3.txt)
- [live36/live/l30.txt](live36/live/l30.txt)
- [live36/live/l4.txt](live36/live/l4.txt)
- [live36/live/l5.txt](live36/live/l5.txt)
- [live36/live/l6.txt](live36/live/l6.txt)
- [live36/live/l7.txt](live36/live/l7.txt)
- [live36/live/l8.txt](live36/live/l8.txt)
- [live36/live/l9.txt](live36/live/l9.txt)
- [live36/live/notif-ev.txt](live36/live/notif-ev.txt)
- [live36/live/notif-l1.txt](live36/live/notif-l1.txt)
- [live36/live/notif-l10.txt](live36/live/notif-l10.txt)
- [live36/live/notif-l11.txt](live36/live/notif-l11.txt)
- [live36/live/notif-l12.txt](live36/live/notif-l12.txt)
- [live36/live/notif-l13.txt](live36/live/notif-l13.txt)
- [live36/live/notif-l14.txt](live36/live/notif-l14.txt)
- [live36/live/notif-l15.txt](live36/live/notif-l15.txt)
- [live36/live/notif-l16.txt](live36/live/notif-l16.txt)
- [live36/live/notif-l17.txt](live36/live/notif-l17.txt)
- [live36/live/notif-l18.txt](live36/live/notif-l18.txt)
- [live36/live/notif-l19.txt](live36/live/notif-l19.txt)
- [live36/live/notif-l2.txt](live36/live/notif-l2.txt)
- [live36/live/notif-l20.txt](live36/live/notif-l20.txt)
- [live36/live/notif-l21.txt](live36/live/notif-l21.txt)
- [live36/live/notif-l22.txt](live36/live/notif-l22.txt)
- [live36/live/notif-l23.txt](live36/live/notif-l23.txt)
- [live36/live/notif-l24.txt](live36/live/notif-l24.txt)
- [live36/live/notif-l25.txt](live36/live/notif-l25.txt)
- [live36/live/notif-l26.txt](live36/live/notif-l26.txt)
- [live36/live/notif-l27.txt](live36/live/notif-l27.txt)
- [live36/live/notif-l28.txt](live36/live/notif-l28.txt)
- [live36/live/notif-l29.txt](live36/live/notif-l29.txt)
- [live36/live/notif-l3.txt](live36/live/notif-l3.txt)
- [live36/live/notif-l30.txt](live36/live/notif-l30.txt)
- [live36/live/notif-l4.txt](live36/live/notif-l4.txt)
- [live36/live/notif-l5.txt](live36/live/notif-l5.txt)
- [live36/live/notif-l6.txt](live36/live/notif-l6.txt)
- [live36/live/notif-l7.txt](live36/live/notif-l7.txt)
- [live36/live/notif-l8.txt](live36/live/notif-l8.txt)
- [live36/live/notif-l9.txt](live36/live/notif-l9.txt)
- [live36/live/shade.xml](live36/live/shade.xml)
- [live36/logcat-all.txt](live36/logcat-all.txt)
- [live36/logcat-dump.txt](live36/logcat-dump.txt)
- [live36/logcat-findings.txt](live36/logcat-findings.txt)
- [live36/logcat-live.txt](live36/logcat-live.txt)
- [live36/notif-l-finish.txt](live36/notif-l-finish.txt)
- [live36/shots/live/00-dialog.png](live36/shots/live/00-dialog.png)
- [live36/shots/live/01-statusbar-chip.png](live36/shots/live/01-statusbar-chip.png)
- [live36/shots/live/02-shade.png](live36/shots/live/02-shade.png)
- [live36/shots/live/03-statusbar-later.png](live36/shots/live/03-statusbar-later.png)
- [live36/shots/live/04-ready.png](live36/shots/live/04-ready.png)
- [live36/shots/live/10-ribbon-03s.png](live36/shots/live/10-ribbon-03s.png)
- [live36/shots/live/11-ribbon-09s.png](live36/shots/live/11-ribbon-09s.png)
- [live36/shots/live/12-card.png](live36/shots/live/12-card.png)
- [live36/shots/upc/event-reminder.png](live36/shots/upc/event-reminder.png)
- [live36/summary.txt](live36/summary.txt)
- [live36/update.json](live36/update.json)
- [live361/cap/event-reminder/androidx.work.workdb](live361/cap/event-reminder/androidx.work.workdb)
- [live361/cap/event-reminder/androidx.work.workdb-shm](live361/cap/event-reminder/androidx.work.workdb-shm)
- [live361/cap/event-reminder/androidx.work.workdb-wal](live361/cap/event-reminder/androidx.work.workdb-wal)
- [live361/cap/event-reminder/jobscheduler.txt](live361/cap/event-reminder/jobscheduler.txt)
- [live361/cap/event-reminder/notif.txt](live361/cap/event-reminder/notif.txt)
- [live361/cap/event-reminder/services.txt](live361/cap/event-reminder/services.txt)
- [live361/cap/event-reminder/ui.xml](live361/cap/event-reminder/ui.xml)
- [live361/cap/event-reminder/workdb.txt](live361/cap/event-reminder/workdb.txt)
- [live361/host-after-run.txt](live361/host-after-run.txt)
- [live361/job-status.txt](live361/job-status.txt)
- [live361/l-finish-n.txt](live361/l-finish-n.txt)
- [live361/live/ev.txt](live361/live/ev.txt)
- [live361/live/l1.txt](live361/live/l1.txt)
- [live361/live/l10.txt](live361/live/l10.txt)
- [live361/live/l11.txt](live361/live/l11.txt)
- [live361/live/l12.txt](live361/live/l12.txt)
- [live361/live/l13.txt](live361/live/l13.txt)
- [live361/live/l14.txt](live361/live/l14.txt)
- [live361/live/l15.txt](live361/live/l15.txt)
- [live361/live/l16.txt](live361/live/l16.txt)
- [live361/live/l17.txt](live361/live/l17.txt)
- [live361/live/l18.txt](live361/live/l18.txt)
- [live361/live/l19.txt](live361/live/l19.txt)
- [live361/live/l2.txt](live361/live/l2.txt)
- [live361/live/l20.txt](live361/live/l20.txt)
- [live361/live/l21.txt](live361/live/l21.txt)
- [live361/live/l22.txt](live361/live/l22.txt)
- [live361/live/l23.txt](live361/live/l23.txt)
- [live361/live/l24.txt](live361/live/l24.txt)
- [live361/live/l25.txt](live361/live/l25.txt)
- [live361/live/l26.txt](live361/live/l26.txt)
- [live361/live/l27.txt](live361/live/l27.txt)
- [live361/live/l28.txt](live361/live/l28.txt)
- [live361/live/l29.txt](live361/live/l29.txt)
- [live361/live/l3.txt](live361/live/l3.txt)
- [live361/live/l30.txt](live361/live/l30.txt)
- [live361/live/l4.txt](live361/live/l4.txt)
- [live361/live/l5.txt](live361/live/l5.txt)
- [live361/live/l6.txt](live361/live/l6.txt)
- [live361/live/l7.txt](live361/live/l7.txt)
- [live361/live/l8.txt](live361/live/l8.txt)
- [live361/live/l9.txt](live361/live/l9.txt)
- [live361/live/notif-ev.txt](live361/live/notif-ev.txt)
- [live361/live/notif-l1.txt](live361/live/notif-l1.txt)
- [live361/live/notif-l10.txt](live361/live/notif-l10.txt)
- [live361/live/notif-l11.txt](live361/live/notif-l11.txt)
- [live361/live/notif-l12.txt](live361/live/notif-l12.txt)
- [live361/live/notif-l13.txt](live361/live/notif-l13.txt)
- [live361/live/notif-l14.txt](live361/live/notif-l14.txt)
- [live361/live/notif-l15.txt](live361/live/notif-l15.txt)
- [live361/live/notif-l16.txt](live361/live/notif-l16.txt)
- [live361/live/notif-l17.txt](live361/live/notif-l17.txt)
- [live361/live/notif-l18.txt](live361/live/notif-l18.txt)
- [live361/live/notif-l19.txt](live361/live/notif-l19.txt)
- [live361/live/notif-l2.txt](live361/live/notif-l2.txt)
- [live361/live/notif-l20.txt](live361/live/notif-l20.txt)
- [live361/live/notif-l21.txt](live361/live/notif-l21.txt)
- [live361/live/notif-l22.txt](live361/live/notif-l22.txt)
- [live361/live/notif-l23.txt](live361/live/notif-l23.txt)
- [live361/live/notif-l24.txt](live361/live/notif-l24.txt)
- [live361/live/notif-l25.txt](live361/live/notif-l25.txt)
- [live361/live/notif-l26.txt](live361/live/notif-l26.txt)
- [live361/live/notif-l27.txt](live361/live/notif-l27.txt)
- [live361/live/notif-l28.txt](live361/live/notif-l28.txt)
- [live361/live/notif-l29.txt](live361/live/notif-l29.txt)
- [live361/live/notif-l3.txt](live361/live/notif-l3.txt)
- [live361/live/notif-l30.txt](live361/live/notif-l30.txt)
- [live361/live/notif-l4.txt](live361/live/notif-l4.txt)
- [live361/live/notif-l5.txt](live361/live/notif-l5.txt)
- [live361/live/notif-l6.txt](live361/live/notif-l6.txt)
- [live361/live/notif-l7.txt](live361/live/notif-l7.txt)
- [live361/live/notif-l8.txt](live361/live/notif-l8.txt)
- [live361/live/notif-l9.txt](live361/live/notif-l9.txt)
- [live361/live/shade.xml](live361/live/shade.xml)
- [live361/logcat-all.txt](live361/logcat-all.txt)
- [live361/logcat-dump.txt](live361/logcat-dump.txt)
- [live361/logcat-findings.txt](live361/logcat-findings.txt)
- [live361/logcat-live.txt](live361/logcat-live.txt)
- [live361/notif-l-finish.txt](live361/notif-l-finish.txt)
- [live361/shots/live/00-dialog.png](live361/shots/live/00-dialog.png)
- [live361/shots/live/01-statusbar-chip.png](live361/shots/live/01-statusbar-chip.png)
- [live361/shots/live/02-shade.png](live361/shots/live/02-shade.png)
- [live361/shots/live/03-statusbar-later.png](live361/shots/live/03-statusbar-later.png)
- [live361/shots/live/04-ready.png](live361/shots/live/04-ready.png)
- [live361/shots/live/10-ribbon-03s.png](live361/shots/live/10-ribbon-03s.png)
- [live361/shots/live/11-ribbon-09s.png](live361/shots/live/11-ribbon-09s.png)
- [live361/shots/live/12-card.png](live361/shots/live/12-card.png)
- [live361/shots/upc/event-reminder.png](live361/shots/upc/event-reminder.png)
- [live361/summary.txt](live361/summary.txt)
- [live361/update.json](live361/update.json)
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
- [monkey19/host-after-run.txt](monkey19/host-after-run.txt)
- [monkey19/job-status.txt](monkey19/job-status.txt)
- [monkey19/logcat-all.txt](monkey19/logcat-all.txt)
- [monkey19/logcat-dump.txt](monkey19/logcat-dump.txt)
- [monkey19/logcat-findings.txt](monkey19/logcat-findings.txt)
- [monkey19/logcat-live.txt](monkey19/logcat-live.txt)
- [monkey19/mem-after-monkey-raw.txt](monkey19/mem-after-monkey-raw.txt)
- [monkey19/mem-after-monkey.txt](monkey19/mem-after-monkey.txt)
- [monkey19/monkey.txt](monkey19/monkey.txt)
- [monkey19/shots/monkey-end.png](monkey19/shots/monkey-end.png)
- [monkey19/summary.txt](monkey19/summary.txt)
- [motion19/gfx-tmp.txt](motion19/gfx-tmp.txt)
- [motion19/host-after-run.txt](motion19/host-after-run.txt)
- [motion19/job-status.txt](motion19/job-status.txt)
- [motion19/logcat-all.txt](motion19/logcat-all.txt)
- [motion19/logcat-dump.txt](motion19/logcat-dump.txt)
- [motion19/logcat-findings.txt](motion19/logcat-findings.txt)
- [motion19/logcat-live.txt](motion19/logcat-live.txt)
- [motion19/rec/rm-bookopen.mp4](motion19/rec/rm-bookopen.mp4)
- [motion19/rec/rm-rosette.mp4](motion19/rec/rm-rosette.mp4)
- [motion19/rec/rm-want.mp4](motion19/rec/rm-want.mp4)
- [motion19/shots/motion/article.png](motion19/shots/motion/article.png)
- [motion19/shots/motion/card-a.png](motion19/shots/motion/card-a.png)
- [motion19/shots/motion/card-b.png](motion19/shots/motion/card-b.png)
- [motion19/shots/motion/ctl-03s.png](motion19/shots/motion/ctl-03s.png)
- [motion19/shots/motion/ctl-08s.png](motion19/shots/motion/ctl-08s.png)
- [motion19/shots/motion/ctl-14s.png](motion19/shots/motion/ctl-14s.png)
- [motion19/shots/motion/ribbon-02s.png](motion19/shots/motion/ribbon-02s.png)
- [motion19/shots/motion/ribbon-06s.png](motion19/shots/motion/ribbon-06s.png)
- [motion19/shots/motion/ribbon-12s.png](motion19/shots/motion/ribbon-12s.png)
- [motion19/shots/motion/ribbon-20s.png](motion19/shots/motion/ribbon-20s.png)
- [motion19/shots/motion/saver-03s.png](motion19/shots/motion/saver-03s.png)
- [motion19/shots/motion/saver-08s.png](motion19/shots/motion/saver-08s.png)
- [motion19/shots/motion/saver-14s.png](motion19/shots/motion/saver-14s.png)
- [motion19/shots/motion/surprise.png](motion19/shots/motion/surprise.png)
- [motion19/shots/rec-rm-bookopen.png](motion19/shots/rec-rm-bookopen.png)
- [motion19/shots/rec-rm-rosette.png](motion19/shots/rec-rm-rosette.png)
- [motion19/shots/rec-rm-want.png](motion19/shots/rec-rm-want.png)
- [motion19/summary.txt](motion19/summary.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-dev-ref.txt](size/apk-dev-ref.txt)
- [size/apk-new-upd-ref.txt](size/apk-new-upd-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/size-base.txt](size/size-base.txt)
- [size/size-new.txt](size/size-new.txt)
- [sizediff/dex-packages-base.txt](sizediff/dex-packages-base.txt)
- [sizediff/dex-packages-new.txt](sizediff/dex-packages-new.txt)
- [sizediff/files-base.txt](sizediff/files-base.txt)
- [sizediff/files-new.txt](sizediff/files-new.txt)
- [sizediff/retrace.txt](sizediff/retrace.txt)
- [sizediff/summary.txt](sizediff/summary.txt)
- [surprise28/host-after-run.txt](surprise28/host-after-run.txt)
- [surprise28/job-status.txt](surprise28/job-status.txt)
- [surprise28/logcat-all.txt](surprise28/logcat-all.txt)
- [surprise28/logcat-dump.txt](surprise28/logcat-dump.txt)
- [surprise28/logcat-findings.txt](surprise28/logcat-findings.txt)
- [surprise28/logcat-live.txt](surprise28/logcat-live.txt)
- [surprise28/memprobe-book.txt](surprise28/memprobe-book.txt)
- [surprise28/memprobe-dice-1.txt](surprise28/memprobe-dice-1.txt)
- [surprise28/memprobe-dice-2.txt](surprise28/memprobe-dice-2.txt)
- [surprise28/memprobe-dice-3.txt](surprise28/memprobe-dice-3.txt)
- [surprise28/mp.sh](surprise28/mp.sh)
- [surprise28/shots/surprise/after-1.png](surprise28/shots/surprise/after-1.png)
- [surprise28/shots/surprise/after-2.png](surprise28/shots/surprise/after-2.png)
- [surprise28/shots/surprise/after-3.png](surprise28/shots/surprise/after-3.png)
- [surprise28/summary.txt](surprise28/summary.txt)
- [surprise28rm/host-after-run.txt](surprise28rm/host-after-run.txt)
- [surprise28rm/job-status.txt](surprise28rm/job-status.txt)
- [surprise28rm/logcat-all.txt](surprise28rm/logcat-all.txt)
- [surprise28rm/logcat-dump.txt](surprise28rm/logcat-dump.txt)
- [surprise28rm/logcat-findings.txt](surprise28rm/logcat-findings.txt)
- [surprise28rm/logcat-live.txt](surprise28rm/logcat-live.txt)
- [surprise28rm/memprobe-book.txt](surprise28rm/memprobe-book.txt)
- [surprise28rm/memprobe-dice-1.txt](surprise28rm/memprobe-dice-1.txt)
- [surprise28rm/memprobe-dice-2.txt](surprise28rm/memprobe-dice-2.txt)
- [surprise28rm/memprobe-dice-3.txt](surprise28rm/memprobe-dice-3.txt)
- [surprise28rm/mp.sh](surprise28rm/mp.sh)
- [surprise28rm/shots/surprise/after-1.png](surprise28rm/shots/surprise/after-1.png)
- [surprise28rm/shots/surprise/after-2.png](surprise28rm/shots/surprise/after-2.png)
- [surprise28rm/shots/surprise/after-3.png](surprise28rm/shots/surprise/after-3.png)
- [surprise28rm/summary.txt](surprise28rm/summary.txt)
- [surprise31/host-after-run.txt](surprise31/host-after-run.txt)
- [surprise31/job-status.txt](surprise31/job-status.txt)
- [surprise31/logcat-all.txt](surprise31/logcat-all.txt)
- [surprise31/logcat-dump.txt](surprise31/logcat-dump.txt)
- [surprise31/logcat-findings.txt](surprise31/logcat-findings.txt)
- [surprise31/logcat-live.txt](surprise31/logcat-live.txt)
- [surprise31/memprobe-book.txt](surprise31/memprobe-book.txt)
- [surprise31/memprobe-dice-1.txt](surprise31/memprobe-dice-1.txt)
- [surprise31/memprobe-dice-2.txt](surprise31/memprobe-dice-2.txt)
- [surprise31/memprobe-dice-3.txt](surprise31/memprobe-dice-3.txt)
- [surprise31/mp.sh](surprise31/mp.sh)
- [surprise31/shots/surprise/after-1.png](surprise31/shots/surprise/after-1.png)
- [surprise31/shots/surprise/after-2.png](surprise31/shots/surprise/after-2.png)
- [surprise31/shots/surprise/after-3.png](surprise31/shots/surprise/after-3.png)
- [surprise31/summary.txt](surprise31/summary.txt)
- [surprise33/host-after-run.txt](surprise33/host-after-run.txt)
- [surprise33/job-status.txt](surprise33/job-status.txt)
- [surprise33/logcat-all.txt](surprise33/logcat-all.txt)
- [surprise33/logcat-dump.txt](surprise33/logcat-dump.txt)
- [surprise33/logcat-findings.txt](surprise33/logcat-findings.txt)
- [surprise33/logcat-live.txt](surprise33/logcat-live.txt)
- [surprise33/memprobe-book.txt](surprise33/memprobe-book.txt)
- [surprise33/memprobe-dice-1.txt](surprise33/memprobe-dice-1.txt)
- [surprise33/memprobe-dice-2.txt](surprise33/memprobe-dice-2.txt)
- [surprise33/memprobe-dice-3.txt](surprise33/memprobe-dice-3.txt)
- [surprise33/mp.sh](surprise33/mp.sh)
- [surprise33/shots/surprise/after-1.png](surprise33/shots/surprise/after-1.png)
- [surprise33/shots/surprise/after-2.png](surprise33/shots/surprise/after-2.png)
- [surprise33/shots/surprise/after-3.png](surprise33/shots/surprise/after-3.png)
- [surprise33/summary.txt](surprise33/summary.txt)
- [update/42398878195/update.json](update/42398878195/update.json)
- [year19/a11ytree-y-closed.txt](year19/a11ytree-y-closed.txt)
- [year19/a11ytree-y-closed2.txt](year19/a11ytree-y-closed2.txt)
- [year19/a11ytree-y-closed3.txt](year19/a11ytree-y-closed3.txt)
- [year19/a11ytree-y-hold-0.txt](year19/a11ytree-y-hold-0.txt)
- [year19/a11ytree-y-hold-a.txt](year19/a11ytree-y-hold-a.txt)
- [year19/a11ytree-y-hold-after.txt](year19/a11ytree-y-hold-after.txt)
- [year19/a11ytree-y-hold-b.txt](year19/a11ytree-y-hold-b.txt)
- [year19/a11ytree-y-left2.txt](year19/a11ytree-y-left2.txt)
- [year19/a11ytree-y-poll.txt](year19/a11ytree-y-poll.txt)
- [year19/a11ytree-y-right.txt](year19/a11ytree-y-right.txt)
- [year19/host-after-run.txt](year19/host-after-run.txt)
- [year19/job-status.txt](year19/job-status.txt)
- [year19/logcat-all.txt](year19/logcat-all.txt)
- [year19/logcat-dump.txt](year19/logcat-dump.txt)
- [year19/logcat-findings.txt](year19/logcat-findings.txt)
- [year19/logcat-live.txt](year19/logcat-live.txt)
- [year19/rec/confetti-renew.mp4](year19/rec/confetti-renew.mp4)
- [year19/rec/confetti-want-dev.mp4](year19/rec/confetti-want-dev.mp4)
- [year19/shots/rec-confetti-renew.png](year19/shots/rec-confetti-renew.png)
- [year19/shots/rec-confetti-want-dev.png](year19/shots/rec-confetti-want-dev.png)
- [year19/shots/year/11-story-end.png](year19/shots/year/11-story-end.png)
- [year19/shots/year/13-after-swipe-down.png](year19/shots/year/13-after-swipe-down.png)
- [year19/summary.txt](year19/summary.txt)
- [year19/year-timeline.txt](year19/year-timeline.txt)
