# Emulator check of v1.2.34 (b06d1ce, versionCode 1500184) vs the released v1.2.33 (a830b51, 1500183)

debug/wave20-final (runs in runs.txt, latest 42899544863 @ 4bcb8d2). API 33 google_apis x86_64, -gpu swangle_indirect; old28: API 28. Earlier: [../v133full/index.md](../v133full/index.md), [../v134-card/index.md](../v134-card/index.md), [../v134-catalog/index.md](../v134-catalog/index.md).

| part | status |
|---|---|
| land34 | COMPLETE; part=land34 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
1.2.33 a830b51 built prodRelease, x86_64 added (throwaway key); versionCode 1500183; update manifest: the real one (releases/latest)
devDebug 1.2.33 a830b51 (org.chyavorec.app.dev, demo sign-in DEMO-0001)
devDebug 1.2.34 b06d1ce (org.chyavorec.app.dev, demo sign-in DEMO-0001)
1.2.34-pre b06d1ce built prodRelease, x86_64 added (throwaway key); versionCode 1500180; update manifest: https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v134/update/42899544863/update.json
1.2.34 b06d1ce built prodRelease, x86_64 added (throwaway key); versionCode 1500184; update manifest: the real one (releases/latest)
app.apk 4300842 bytes
app-base.apk 4277262 bytes
app-new-upd.apk 4300942 bytes
app-dev.apk 27834230 bytes
== size-new.apk: 4293102 bytes; 1.2.34 b06d1ce built prodRelease, release ABIs only (arm64-v8a, armeabi-v7a) (throwaway key); versionCode 1500184; update manifest: the real one (releases/latest)
manifest: =1500184 "1.2.34" "1.2.34" 
apkanalyzer download size: 4088039 B; file size 4293102 B
part                    compressed  uncompressed  files
classes*.dex               3520333       6934884      2
res                         468406        832536     57
resources.arsc              238512        238512      1
assets/                      20217         20217      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5488         23940      1
META-INF/                      846          1288      6
== size-base.apk: 4269526 bytes; 1.2.33 a830b51 built prodRelease, release ABIs only (arm64-v8a, armeabi-v7a) (throwaway key); versionCode 1500183; update manifest: the real one (releases/latest)
manifest: =1500183 "1.2.33" "1.2.33" 
apkanalyzer download size: 4066585 B; file size 4269526 B
part                    compressed  uncompressed  files
classes*.dex               3500025       6899732      2
res                         468408        832536     57
resources.arsc              235600        235600      1
assets/                      19856         19856      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5489         23940      1
META-INF/                      846          1288      6
== size-rel133.apk: 4269526 bytes; released v1.2.33 (GitHub release asset, real key)
manifest: =1500183 "1.2.33" "1.2.33" 
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
```

## land34

```
part land34; APK: 1.2.34 b06d1ce built prodRelease, x86_64 added (throwaway key); versionCode 1500184; update manifest: the real one (releases/latest) size 4300842 bytes
base APK: 1.2.33 a830b51 built prodRelease, x86_64 added (throwaway key); versionCode 1500183; update manifest: the real one (releases/latest) size 4277262 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.34 b06d1ce built prodRelease, x86_64 added (throwaway key); versionCode 1500184; update manifest: the real one (releases/latest) size 4300842 bytes
installed:  versionCode=1500184 minSdk=26 targetSdk=37  versionName=1.2.34 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.34 b06d1ce (org.chyavorec.app.dev, demo sign-in DEMO-0001);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.34-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 8462 ms
Запомни ме before: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
Запомни ме now: CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true)
MY after the demo sign-in: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" 
## L1. More swipe lengths (new, slow and fast): where is the effective close distance?
YEAR2 new swipe d250t600 (656 px ≈ 249 dp in 600 ms): open closed
YEAR2 new swipe d300t600 (787 px ≈ 299 dp in 600 ms): closed closed
YEAR2 new swipe d350t600 (918 px ≈ 349 dp in 600 ms): closed closed
YEAR2 new swipe d300t150 (787 px ≈ 299 dp in 150 ms): open open
YEAR2 new swipe d400t150 (1050 px ≈ 400 dp in 150 ms): open open
## L2. Font 1.3 + „Много голям“, landscape (screen size swapped for the gestures): open the story, every slide
TEXT SIZE now: text="Много голям" 
LANDSCAPE on (method 2): hierarchy rotation="1"; cur=2400x1080 
  screen now 2400x1080 (landscape)
My in landscape: year card found
YEAR land13 slide 1: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Моята читателска година · 2026 · Затвори · Сподели
  slide 1 after a slow drag up inside: diff 2.64
YEAR land13 slide 2: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Най-четен раздел: Език. Литература — 2 к · Моята читателска година · 2026 · Затвори · Сподели
  slide 2 after a slow drag up inside: diff 11.40
YEAR land13 slide 3: YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Най-дълга поредица: 11 месеца подред с п · Моята читателска година · 2026 · Затвори · Сподели
  slide 3 after a slow drag up inside: diff 18.89
YEAR land13 slide 4: YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Начало · Каталог · Моето · Новини · Още · Известия · Кои да получаваш: срокове, събития, нови · МОЕТО ЧЕТЕНЕ
  slide 4 after a slow drag up inside: diff 0.00
  story closed at slide 4
## L3. Story opened in portrait, then rotated to landscape while open
LANDSCAPE on (method 1): hierarchy rotation="1"; cur=2400x1080 
  screen now 2400x1080 (landscape)
YEAR after rotating while open: open (SLIDE m=48 s=29); YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Любим автор: Иван Вазов — 6 книги през г · Моята читателска година · 2026 · Затвори · Сподели
  land slide +1: open; YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Рекорден месец: Септември — 6 книги. · Моята читателска година · 2026 · Затвори · Сподели
  land slide +2: open; YS state=- error=no scrollable=0 ellipsized=0 beyond-screen=0 [] | Твоят читателски тип: Книжен дракон. Пог · Моята читателска година · 2026 · Затвори · Сподели
  land slide +3: closed; YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Начало · Каталог · Моето · Новини · Още · Историята на четенето и подобни книги · Читателски данни · Известия
  land slide +4: closed; YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Начало · Каталог · Моето · Новини · Още · Историята на четенето и подобни книги · Читателски данни · Известия
  land slide +5: closed; YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Начало · Каталог · Моето · Новини · Още · Историята на четенето и подобни книги · Читателски данни · Известия
  land slide +6: closed; YS state=- error=no scrollable=1 ellipsized=0 beyond-screen=0 [] | Начало · Каталог · Моето · Новини · Още · Историята на четенето и подобни книги · Читателски данни · Известия
TEXT SIZE now: text="Стандартен" 
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 1 | Първи кадър: медиана 5095 мс, последен 5095 мс | Пълно показване: медиана 5095 мс, последно 5095 мс | Насечени кадри по екрани | login: 18,7 % от 91 кадъра · най-бавен 1244 мс | settings: 16,1 % от 31 кадъра · най-бавен 580 мс | home: 4,9 %
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 1" text="Първи кадър: медиана 5095 мс, последен 5095 мс" text="Пълно показване: медиана 5095 мс, последно 5095 мс" text="Насечени кадри по екрани" text="login: 18,7 % от 91 кадъра · най-бавен 1244 мс" text="s
APP W/E lines (pids ['4993']): 240 in 106 groups
       9  E/OpenGLRenderer: Unable to match the desired swap behavior.
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       5  W/Parcel: Expecting binder but got null!
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.InnerNodeCoordinator$LookaheadDelegateImpl.measure-BRTryo#(InnerNodeCoordinator.k
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasureBlock$lambda$#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate$$ExternalSyntheticLambda#.invoke(D#$$SyntheticClass:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.performMeasure-BRTryo#$ui(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutNodeLayoutDelegate.performLookaheadMeasure-BRTryo#$ui(LayoutNodeLayoutDeleg
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.remeasure-BRTryo#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LookaheadPassDelegate.measure-BRTryo#(LookaheadPassDelegate.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.ui.node.LayoutModifierNodeCoordinator$LookaheadDelegateForLayoutModifierNode.measure-BRTr
       4  W/BasicSecureTextField: 	at androidx.compose.ui.graphics.BlockGraphicsLayerModifier.measure-#p#s#s(GraphicsLayerModifier.kt:#)
       4  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
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
app pids seen: ['4993']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 76
   skipped-frames events=76 max=265 total=3318
== Davey: 340
   davey events=340 max=5281ms median=850ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Files

- [land34/app-we.txt](land34/app-we.txt)
- [land34/diag-log.jsonl](land34/diag-log.jsonl)
- [land34/diag-screen.xml](land34/diag-screen.xml)
- [land34/host-after-run.txt](land34/host-after-run.txt)
- [land34/job-status.txt](land34/job-status.txt)
- [land34/logcat-all.txt](land34/logcat-all.txt)
- [land34/logcat-dump.txt](land34/logcat-dump.txt)
- [land34/logcat-findings.txt](land34/logcat-findings.txt)
- [land34/logcat-live.txt](land34/logcat-live.txt)
- [land34/shots/diag/diagnostics-end-2.png](land34/shots/diag/diagnostics-end-2.png)
- [land34/shots/diag/diagnostics-end.png](land34/shots/diag/diagnostics-end.png)
- [land34/shots/land/my-landscape.png](land34/shots/land/my-landscape.png)
- [land34/shots/year/land13-slide-1-scrolled.png](land34/shots/year/land13-slide-1-scrolled.png)
- [land34/shots/year/land13-slide-1.png](land34/shots/year/land13-slide-1.png)
- [land34/shots/year/land13-slide-2-scrolled.png](land34/shots/year/land13-slide-2-scrolled.png)
- [land34/shots/year/land13-slide-2.png](land34/shots/year/land13-slide-2.png)
- [land34/shots/year/land13-slide-3-scrolled.png](land34/shots/year/land13-slide-3-scrolled.png)
- [land34/shots/year/land13-slide-3.png](land34/shots/year/land13-slide-3.png)
- [land34/shots/year/land13-slide-4-scrolled.png](land34/shots/year/land13-slide-4-scrolled.png)
- [land34/shots/year/land13-slide-4.png](land34/shots/year/land13-slide-4.png)
- [land34/shots/year/rot-0-portrait.png](land34/shots/year/rot-0-portrait.png)
- [land34/shots/year/rot-1-landscape.png](land34/shots/year/rot-1-landscape.png)
- [land34/shots/year/rot-land-slide-1.png](land34/shots/year/rot-land-slide-1.png)
- [land34/shots/year/rot-land-slide-2.png](land34/shots/year/rot-land-slide-2.png)
- [land34/shots/year/rot-land-slide-3.png](land34/shots/year/rot-land-slide-3.png)
- [land34/shots/year/rot-land-slide-4.png](land34/shots/year/rot-land-slide-4.png)
- [land34/shots/year/rot-land-slide-5.png](land34/shots/year/rot-land-slide-5.png)
- [land34/shots/year/rot-land-slide-6.png](land34/shots/year/rot-land-slide-6.png)
- [land34/shots/year/state.png](land34/shots/year/state.png)
- [land34/shots/year/sw-new-d250t600-1.png](land34/shots/year/sw-new-d250t600-1.png)
- [land34/shots/year/sw-new-d250t600-2.png](land34/shots/year/sw-new-d250t600-2.png)
- [land34/shots/year/sw-new-d300t150-1.png](land34/shots/year/sw-new-d300t150-1.png)
- [land34/shots/year/sw-new-d300t150-2.png](land34/shots/year/sw-new-d300t150-2.png)
- [land34/shots/year/sw-new-d300t600-1.png](land34/shots/year/sw-new-d300t600-1.png)
- [land34/shots/year/sw-new-d300t600-2.png](land34/shots/year/sw-new-d300t600-2.png)
- [land34/shots/year/sw-new-d350t600-1.png](land34/shots/year/sw-new-d350t600-1.png)
- [land34/shots/year/sw-new-d350t600-2.png](land34/shots/year/sw-new-d350t600-2.png)
- [land34/shots/year/sw-new-d400t150-1.png](land34/shots/year/sw-new-d400t150-1.png)
- [land34/shots/year/sw-new-d400t150-2.png](land34/shots/year/sw-new-d400t150-2.png)
- [land34/shots/year/wait.png](land34/shots/year/wait.png)
- [land34/summary.txt](land34/summary.txt)
- [land34/year-land13-1.xml](land34/year-land13-1.xml)
- [land34/year-land13-2.xml](land34/year-land13-2.xml)
- [land34/year-land13-3.xml](land34/year-land13-3.xml)
- [land34/year-land13-4.xml](land34/year-land13-4.xml)
- [runs.txt](runs.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-dev-base-ref.txt](size/apk-dev-base-ref.txt)
- [size/apk-dev-ref.txt](size/apk-dev-ref.txt)
- [size/apk-new-upd-ref.txt](size/apk-new-upd-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/apk-size.txt](size/apk-size.txt)
- [size/size-base-ref.txt](size/size-base-ref.txt)
- [size/size-new-ref.txt](size/size-new-ref.txt)
- [size/size.txt](size/size.txt)
- [size/sizediff.txt](size/sizediff.txt)
