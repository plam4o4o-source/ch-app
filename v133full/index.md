# Full emulator test of the released v1.2.33 (a830b51, versionCode 1500183)

debug/full-test-133 (runs in runs.txt, latest 42683799620 @ 7e3a858). API 33 google_apis x86_64, -gpu swangle_indirect, unless the part says otherwise (live36: API 36, live361: API 36.1, old28: API 28). Each part's summary says which APK it ran (the released ARM APK via ARM translation, or the built x86_64 prodRelease). Previous checks: [../v133/index.md](../v133/index.md).

| part | status |
|---|---|
| demo2 | COMPLETE; part=demo2 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
== released v1.2.33: rel-133.apk — 4269526 bytes; sha256 9300ccd527ff18b4; update.json sha256 9300ccd527ff18b4
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1500183  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.33" (Raw: "1.2.33")  A: http://schemas.android.com/apk/res/android:minSdkVersion(0x0101020c)=26  A: http://schemas.android.com/apk/res/android:targetSdkVersion(0x01010270)=37 
native libs: lib/arm64-v8a/libandroidx.graphics.path.so lib/arm64-v8a/libdatastore_shared_counter.so lib/armeabi-v7a/libandroidx.graphics.path.so lib/armeabi-v7a/libdatastore_shared_counter.so 
baseline profile: assets/dexopt/baseline.prof assets/dexopt/baseline.profm 
apkanalyzer download size: 4067421 B; file size 4269526 B
part                    compressed  uncompressed  files
classes*.dex               3500025       6899732      2
res                         468408        832536     57
resources.arsc              235600        235600      1
assets/                      19856         19856      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5489         23940      1
META-INF/                      846          1288      6
== released v1.2.32: rel-132.apk — 4153198 bytes; sha256 ba4988b065d0618b; update.json sha256 ba4988b065d0618b
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1495920  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.32" (Raw: "1.2.32")  A: http://schemas.android.com/apk/res/android:minSdkVersion(0x0101020c)=26  A: http://schemas.android.com/apk/res/android:targetSdkVersion(0x01010270)=37 
native libs: lib/arm64-v8a/libandroidx.graphics.path.so lib/arm64-v8a/libdatastore_shared_counter.so lib/armeabi-v7a/libandroidx.graphics.path.so lib/armeabi-v7a/libdatastore_shared_counter.so 
baseline profile: assets/dexopt/baseline.prof assets/dexopt/baseline.profm 
apkanalyzer download size: 3959811 B; file size 4153198 B
part                    compressed  uncompressed  files
classes*.dex               3395621       6707860      2
res                         468365        832408     57
resources.arsc              224392        224392      1
assets/                      19213         19213      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5464         23760      1
META-INF/                      846          1288      6
1.2.32 87ef20e built prodRelease x86_64 (throwaway key); versionCode 1495920; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v133full/update/42683799620/update.json
devDebug a830b51 (org.chyavorec.app.dev, demo sign-in DEMO-0001)
1.2.33-pre a830b51 built prodRelease x86_64 (throwaway key); versionCode 1500182; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v133full/update/42683799620/update.json
1.2.33 a830b51 built prodRelease x86_64 (throwaway key); versionCode 1500183; update manifest: the real one (releases/latest)
1.2.33 RELEASED APK (GitHub release asset, arm64-v8a/armeabi-v7a); versionCode 1500183
```

## demo2

```
part demo2; APK: 1.2.33 a830b51 built prodRelease x86_64 (throwaway key); versionCode 1500183; update manifest: the real one (releases/latest) size 4277262 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
abilist: x86_64; native bridge: 0
RELEASE APK install:  → built x86_64 app.apk
APK for this part: 1.2.33 a830b51 built prodRelease x86_64 (throwaway key); versionCode 1500183; update manifest: the real one (releases/latest) size 4277262 bytes
installed:  versionCode=1500183 minSdk=26 targetSdk=37  versionName=1.2.33 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 6436 ms
MY after the demo sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" text="Моите книги" text="Заети книги и срокове за връщане" text="Искам да прочета" text="Книги, ко
## Y2b. Year story: what is on the screen during the first 18 s (screenshot each second)
YEAR STACK fresh +~3s: "main" prio=5 tid=1 Native~"DefaultDispatcher-worker-2" daemon prio=5 tid=17 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-1" daemon prio=5 tid=18 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"RenderThread" daemon prio=7 tid=19 Native~"DefaultDispatcher-worker-3" daemon prio=5 tid=20 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-5" daemon prio=5 tid=21 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~
YEAR STACK fresh +~7s: "main" prio=5 tid=1 Native~"DefaultDispatcher-worker-2" daemon prio=5 tid=17 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-1" daemon prio=5 tid=18 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"RenderThread" daemon prio=7 tid=19 Native~"DefaultDispatcher-worker-3" daemon prio=5 tid=20 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-5" daemon prio=5 tid=21 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~
YEAR STACK fresh +~11s: "main" prio=5 tid=1 Native~"DefaultDispatcher-worker-2" daemon prio=5 tid=17 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-1" daemon prio=5 tid=18 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"RenderThread" daemon prio=7 tid=19 Native~"DefaultDispatcher-worker-3" daemon prio=5 tid=20 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-5" daemon prio=5 tid=21 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~
YEARF fresh frame stats per second: 8.1 12.3 12.7 12.9 12.9 12.4 12.9 12.7 12.9 12.8 12.3 12.9 12.7 12.9 27.6 30.9 31.0 31.0 
YEAR STACK again +~3s: "main" prio=5 tid=1 Native~"DefaultDispatcher-worker-1" daemon prio=5 tid=20 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-2" daemon prio=5 tid=22 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"android.bg" daemon prio=5 tid=23 Native~
YEAR STACK again +~7s: "main" prio=5 tid=1 Native~"DefaultDispatcher-worker-1" daemon prio=5 tid=20 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-2" daemon prio=5 tid=22 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"android.bg" daemon prio=5 tid=23 Native~
YEAR STACK again +~11s: "main" prio=5 tid=1 Runnable~"DefaultDispatcher-worker-1" daemon prio=5 tid=20 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"DefaultDispatcher-worker-2" daemon prio=5 tid=22 TimedWaiting~  at jdk.internal.misc.Unsafe.park(Native method)~  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:357)~  at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.park(CoroutineScheduler.kt:833)~"android.bg" daemon prio=5 tid=23 Native~
YEARF again frame stats per second: 5.6 12.9 12.7 13.0 12.9 12.3 12.8 13.0 12.8 12.4 12.8 12.7 12.9 12.8 25.8 30.9 31.0 29.1 
## Y6b. Short flick variants (the 22% / 90 ms flick stayed open in round a)
YEAR swipe down flick90 (40%→62% = 528 px in 90 ms): STILL OPEN
YEAR swipe down flick150 (40%→62% = 528 px in 150 ms): STILL OPEN
YEAR swipe down flick90b (40%→62% = 528 px in 90 ms): closed
YEAR swipe down short300 (40%→62% = 528 px in 300 ms): STILL OPEN
YEAR swipe down fast200 (30%→85% = 1320 px in 200 ms): closed
## Y4b. Demo card: foil and the sensor
SENSORS on the card screen (demo): active connections mentioning the app: 0; lines: 0x5f676172) Game Rotation Vector Sensor | AOSP | ver: 3 | type: android.sensor.game_rotation_vector(15) | perm: n/a | flags: 0x00000000~Game Rotation Vector Sensor: last 10 events~	 Game Rotation Vector Sensor 0x5f676172 | status: active | pending flush events 0 ~
ESHOT files: demo-0.png demo-0b.png demo-acc-A.png demo-acc-B.png demo-gyro-A.png demo-gyro-B.png demo-gyro-C.png ; size of demo-0: PNG-signature=yes size=1035791 B dims=1080x2400 mode=RGBA
TILT2 demo noise 0→0b (same tilt, 1.5 s): 0.44
TILT2 demo: acc A→B 2.35; 0→accA 3.22; gyro A→B 0.15; gyro B→C 2.41; 0→gyroA 2.55
SENSOR events for the app (recent): 
## Y3b. Hold result
HOLD after the tap: text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Налична" text="Наличност към 09.11.2026" text="Искам да прочета" text="Заяви книгата" text="Библиографски данни" text="Година" text="2011" text="Език" text="български" text="Вид" text="книга" text="Раздел" text="Език. Литература (УДК 8)" text="Сигнатура" text="ЦБ/820(73)/Г 23" text="Инвентарен номер" text="399560" text="Отдел" 
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 2 | Първи кадър: медиана 3506 мс, последен 3186 мс | Пълно показване: медиана 4205 мс, последно 4584 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 2" text="Първи кадър: медиана 3506 мс, последен 3186 мс" text="Пълно показване: медиана 4205 мс, последно 4584 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5077', '7262']): 226 in 108 groups
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       4  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       3  W/ziparchive: Unable to open '/data/app/~~XtVlYrEuFfpFLMXjgetJ#g==/org.chyavorec.app.dev-WCuo#p#TfXForUZ#waaBLQ==/base.dm': 
       2  W/yavorec.app.dev: Unexpected CPU variant for x#: x#_#.
       2  W/yavorec.app.dev: Known variants: atom, sandybridge, silvermont, kabylake, default
       2  W/Parcel: Expecting binder but got null!
       2  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       2  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       2  W/yavorec.app.dev: Accessing hidden method Landroid/os/SystemProperties;->addChangeCallback(Ljava/lang/Runnable;)V (unsupported, 
       2  W/Gralloc4: allocator #.x is not supported
       2  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.PlatformPasswordVisibilitySettingImpl.getSystemShowPasswordSetting(BasicS
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextField_androidKt.rememberPlatformPasswordVisibilitySettings
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-ltb#GB#(BasicSecureTextField.
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-egD#TGM(BasicSecureTextField.
       2  W/BasicSecureTextField: 	at androidx.compose.runtime.CompositionLocalKt.CompositionLocalProvider(CompositionLocal.kt:#)
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

- [demo2/app-we.txt](demo2/app-we.txt)
- [demo2/diag-log.jsonl](demo2/diag-log.jsonl)
- [demo2/diag-screen.xml](demo2/diag-screen.xml)
- [demo2/host-after-run.txt](demo2/host-after-run.txt)
- [demo2/job-status.txt](demo2/job-status.txt)
- [demo2/logcat-all.txt](demo2/logcat-all.txt)
- [demo2/logcat-dump.txt](demo2/logcat-dump.txt)
- [demo2/logcat-findings.txt](demo2/logcat-findings.txt)
- [demo2/logcat-live.txt](demo2/logcat-live.txt)
- [demo2/sensors-demo-after.txt](demo2/sensors-demo-after.txt)
- [demo2/sensors-demo.txt](demo2/sensors-demo.txt)
- [demo2/shots/diag/diagnostics-end-2.png](demo2/shots/diag/diagnostics-end-2.png)
- [demo2/shots/diag/diagnostics-end.png](demo2/shots/diag/diagnostics-end.png)
- [demo2/shots/eshot/demo-0.png](demo2/shots/eshot/demo-0.png)
- [demo2/shots/eshot/demo-0b.png](demo2/shots/eshot/demo-0b.png)
- [demo2/shots/eshot/demo-acc-A.png](demo2/shots/eshot/demo-acc-A.png)
- [demo2/shots/eshot/demo-acc-B.png](demo2/shots/eshot/demo-acc-B.png)
- [demo2/shots/eshot/demo-gyro-A.png](demo2/shots/eshot/demo-gyro-A.png)
- [demo2/shots/eshot/demo-gyro-B.png](demo2/shots/eshot/demo-gyro-B.png)
- [demo2/shots/eshot/demo-gyro-C.png](demo2/shots/eshot/demo-gyro-C.png)
- [demo2/shots/fix/hold-1.png](demo2/shots/fix/hold-1.png)
- [demo2/shots/fix/hold-2.png](demo2/shots/fix/hold-2.png)
- [demo2/shots/fix/hold-3.png](demo2/shots/fix/hold-3.png)
- [demo2/shots/fix/hold-4.png](demo2/shots/fix/hold-4.png)
- [demo2/shots/fix/hold-5.png](demo2/shots/fix/hold-5.png)
- [demo2/shots/fix/hold-6.png](demo2/shots/fix/hold-6.png)
- [demo2/shots/tilt2/demo-0.png](demo2/shots/tilt2/demo-0.png)
- [demo2/shots/tilt2/demo-0b.png](demo2/shots/tilt2/demo-0b.png)
- [demo2/shots/tilt2/demo-acc-A.png](demo2/shots/tilt2/demo-acc-A.png)
- [demo2/shots/tilt2/demo-acc-B.png](demo2/shots/tilt2/demo-acc-B.png)
- [demo2/shots/tilt2/demo-gyro-A.png](demo2/shots/tilt2/demo-gyro-A.png)
- [demo2/shots/tilt2/demo-gyro-B.png](demo2/shots/tilt2/demo-gyro-B.png)
- [demo2/shots/tilt2/demo-gyro-C.png](demo2/shots/tilt2/demo-gyro-C.png)
- [demo2/shots/tilt2/demo-strip.png](demo2/shots/tilt2/demo-strip.png)
- [demo2/shots/year2/again-01s.png](demo2/shots/year2/again-01s.png)
- [demo2/shots/year2/again-02s.png](demo2/shots/year2/again-02s.png)
- [demo2/shots/year2/again-03s.png](demo2/shots/year2/again-03s.png)
- [demo2/shots/year2/again-04s.png](demo2/shots/year2/again-04s.png)
- [demo2/shots/year2/again-05s.png](demo2/shots/year2/again-05s.png)
- [demo2/shots/year2/again-06s.png](demo2/shots/year2/again-06s.png)
- [demo2/shots/year2/again-07s.png](demo2/shots/year2/again-07s.png)
- [demo2/shots/year2/again-08s.png](demo2/shots/year2/again-08s.png)
- [demo2/shots/year2/again-09s.png](demo2/shots/year2/again-09s.png)
- [demo2/shots/year2/again-10s.png](demo2/shots/year2/again-10s.png)
- [demo2/shots/year2/again-11s.png](demo2/shots/year2/again-11s.png)
- [demo2/shots/year2/again-12s.png](demo2/shots/year2/again-12s.png)
- [demo2/shots/year2/again-13s.png](demo2/shots/year2/again-13s.png)
- [demo2/shots/year2/again-14s.png](demo2/shots/year2/again-14s.png)
- [demo2/shots/year2/again-15s.png](demo2/shots/year2/again-15s.png)
- [demo2/shots/year2/again-16s.png](demo2/shots/year2/again-16s.png)
- [demo2/shots/year2/again-17s.png](demo2/shots/year2/again-17s.png)
- [demo2/shots/year2/again-18s.png](demo2/shots/year2/again-18s.png)
- [demo2/shots/year2/again-strip.png](demo2/shots/year2/again-strip.png)
- [demo2/shots/year2/fresh-01s.png](demo2/shots/year2/fresh-01s.png)
- [demo2/shots/year2/fresh-02s.png](demo2/shots/year2/fresh-02s.png)
- [demo2/shots/year2/fresh-03s.png](demo2/shots/year2/fresh-03s.png)
- [demo2/shots/year2/fresh-04s.png](demo2/shots/year2/fresh-04s.png)
- [demo2/shots/year2/fresh-05s.png](demo2/shots/year2/fresh-05s.png)
- [demo2/shots/year2/fresh-06s.png](demo2/shots/year2/fresh-06s.png)
- [demo2/shots/year2/fresh-07s.png](demo2/shots/year2/fresh-07s.png)
- [demo2/shots/year2/fresh-08s.png](demo2/shots/year2/fresh-08s.png)
- [demo2/shots/year2/fresh-09s.png](demo2/shots/year2/fresh-09s.png)
- [demo2/shots/year2/fresh-10s.png](demo2/shots/year2/fresh-10s.png)
- [demo2/shots/year2/fresh-11s.png](demo2/shots/year2/fresh-11s.png)
- [demo2/shots/year2/fresh-12s.png](demo2/shots/year2/fresh-12s.png)
- [demo2/shots/year2/fresh-13s.png](demo2/shots/year2/fresh-13s.png)
- [demo2/shots/year2/fresh-14s.png](demo2/shots/year2/fresh-14s.png)
- [demo2/shots/year2/fresh-15s.png](demo2/shots/year2/fresh-15s.png)
- [demo2/shots/year2/fresh-16s.png](demo2/shots/year2/fresh-16s.png)
- [demo2/shots/year2/fresh-17s.png](demo2/shots/year2/fresh-17s.png)
- [demo2/shots/year2/fresh-18s.png](demo2/shots/year2/fresh-18s.png)
- [demo2/shots/year2/fresh-strip.png](demo2/shots/year2/fresh-strip.png)
- [demo2/shots/year2/swipe-fast200.png](demo2/shots/year2/swipe-fast200.png)
- [demo2/shots/year2/swipe-flick150.png](demo2/shots/year2/swipe-flick150.png)
- [demo2/shots/year2/swipe-flick90.png](demo2/shots/year2/swipe-flick90.png)
- [demo2/shots/year2/swipe-flick90b.png](demo2/shots/year2/swipe-flick90b.png)
- [demo2/shots/year2/swipe-short300.png](demo2/shots/year2/swipe-short300.png)
- [demo2/summary.txt](demo2/summary.txt)
- [demo2/year-stack-again-11.txt](demo2/year-stack-again-11.txt)
- [demo2/year-stack-again-3.txt](demo2/year-stack-again-3.txt)
- [demo2/year-stack-again-7.txt](demo2/year-stack-again-7.txt)
- [demo2/year-stack-fresh-11.txt](demo2/year-stack-fresh-11.txt)
- [demo2/year-stack-fresh-3.txt](demo2/year-stack-fresh-3.txt)
- [demo2/year-stack-fresh-7.txt](demo2/year-stack-fresh-7.txt)
- [runs.txt](runs.txt)
- [size/apk-base-upd-ref.txt](size/apk-base-upd-ref.txt)
- [size/apk-dev-ref.txt](size/apk-dev-ref.txt)
- [size/apk-new-upd-ref.txt](size/apk-new-upd-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/apk-rel-ref.txt](size/apk-rel-ref.txt)
- [size/size-rel.txt](size/size-rel.txt)
