# Wave 16 emulator check (bf10741, test v1.2.30, vs v1.2.29 = b84650f)

Source: `debug/wave16-check` @ 721eb28, last run 41346234873 (attempt 1), 2026-11-01T20:45:52Z. Runs: [runs.txt](runs.txt).
Emulators: google_apis x86_64 API 33 (parts ending in 34/28/26: that API), pixel_6 profile, -gpu swangle_indirect, no camera; app locale bg-BG unless a part says otherwise. APKs: minified prodRelease, x86_64 added, throwaway key; v1.2.30 versionCode 1488543, v1.2.29 versionCode 1488542.
Earlier: v1.2.29 check [../v129/index.md](../v129/index.md).

## APK size (release configuration: arm64-v8a + armeabi-v7a, no x86_64)

```
== new 1.2.30 bf10741: release-config APK (arm64-v8a + armeabi-v7a, minified, throwaway key)
APK size: 4497269 bytes
manifest:  A: http://schemas.android.com/apk/res/android:versionCode(0x0101021b)=1488543  A: http://schemas.android.com/apk/res/android:versionName(0x0101021c)="1.2.30" (Raw: "1.2.30")  A: http://schemas.android.com/apk/res/android:extractNativeLibs(0x010104ea)=true 
uses-permission: 'android.permission.INTERNET' 'android.permission.ACCESS_NETWORK_STATE' 'android.permission.POST_NOTIFICATIONS' 'android.permission.USE_BIOMETRIC' 'android.permission.REQUEST_INSTALL_PACKAGES' 'android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' 'android.permission.FOREGROUND_SERVICE' 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' 'android.permission.WAKE_LOCK' 'android.permission.RECEIVE_BOOT_COMPLETED' 'org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
foreground services: 
part                    compressed  uncompressed  files
classes*.dex               3524106       6973832      2
res                         589531       1023145    322
resources.arsc              291052        291052      1
assets/                      20429         20429      2
lib/arm64-v8a                 8642         20472      2
lib/armeabi-v7a               6601         13116      2
AndroidManifest.xml           5421         23568      1
META-INF/                      845          1288      6
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
| a11y | COMPLETE; part=a11y target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| a11y16 | COMPLETE; part=a11y16 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| abstart-1 | COMPLETE; part=abstart-1 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| abstart-2 | COMPLETE; part=abstart-2 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| dialogs | COMPLETE; part=dialogs target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| emoji26 | COMPLETE; part=emoji26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success |
| emoji28 | COMPLETE; part=emoji28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| emoji33 | COMPLETE; part=emoji33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| events | COMPLETE; part=events target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| login | COMPLETE; part=login target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| look1 | COMPLETE; part=look1 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| look2 | COMPLETE; part=look2 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| look3 | COMPLETE; part=look3 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mem-base | COMPLETE; part=mem-base target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mem | COMPLETE; part=mem target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| monkey | COMPLETE; part=monkey target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| offline | COMPLETE; part=offline target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| onboard | COMPLETE; part=onboard target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| rot | COMPLETE; part=rot target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tablet | COMPLETE; part=tablet target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| unlock33 | COMPLETE; part=unlock33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| unlock34 | COMPLETE; part=unlock34 target=google_apis api=34 gpu=swangle_indirect emulator-step=success |
| widgetlang | COMPLETE; part=widgetlang target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

## a11y

```
part a11y; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2798 ms
## A. Accessibility (uiautomator): main screens, density 420
A11Y 01-home: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1
   SMALL: 'Ден на народните будители' 280x28dp
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
A11Y 21-home-en-font130: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 22-catalog-en-font130: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 23-book-en-font130: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
A11Y 24-more-en-font130: targets=12 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0
## A3. Totals over all screens
A11Y TOTAL: SMALL(<48dp)=1 SAME-LABEL=1 MISSING=3 DOUBLE=0 
## COMPLETE
## logcat findings
app pids seen: ['5432', '7479', '9888']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 3
   davey events=3 max=820ms median=772ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## a11y16

```
part a11y16; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
TalkBack on the image: 2 package(s) — the test accessibility service stands in for it
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2821 ms
## B. Home top bar: Messages (megaphone) and the unread badge
MESSAGES button: content-desc="Съобщения" ; bell/notification icons: 
carousel card (uiautomator): 35 809 863 1432 | СЪБИТИЕ Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец.
## B2. Unread messages: the stored read list emptied (as if none were read) → the badge and the label
prefs: 485 bytes after adding ['read_messages']
MESSAGES button (unread): content-desc="Съобщения" ; badge texts near it: text="8" 
Messages screen: text="Няма съобщения от читалището" text="Някои съобщения са само за членове. Въведи читателската си карта (или влез) в „Моето“, за да ги виждаш." content-desc="Назад" text="Съобщения" 
## T1. Test accessibility service on (log mode): pane titles on the tabs and detail screens
A11Y service: 11-01 20:00:36.687  7867  7867 I PCA11Y  : connected flags=80
PANE events (13): cls=android.view.View text=[Каталог]|pane=Каталог|cls=android.view.View text=[Моето]|pane=Моето|cls=android.view.View text=[Новини и събития]|pane=Новини и събития|cls=android.view.View text=[Още]|pane=Още|cls=android.view.View text=[Начало]|pane=Начало|cls=android.view.View text=[12 -те вятъра]|pane=12 -те вятъра|cls=android.view.View text=[Фолклорно пътешествие из Пиринска, Родопска и Тракийска област|pane=Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец.|cls=android.view.View text=[Настройки]|pane=Настройки|
## T2. Home as a screen reader sees it: the carousel
TREE home1 (132 lines):
TREE nodes=129 windows: ['11-01 20:01:21.701  7867  7867 I PCDUMP  : thome1 W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-01 20:01:21.703  7867  7867 I PCDUMP  : thome1 W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=16 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Начало']; headings: ['', 'Предстоящи събития']
TREE nodes with labelled (custom) actions: 2: 'Последни новини' [2131361799:Следваща новина] | '' [2131361799:Следваща новина]
TREE carousel nodes 'Последни новини': 1; nodes with Следваща/Предишна новина: 2: 'Последни новини' clk=false b=[0,809][1080,1432] acts=[2131361799:Следваща новина] | '' clk=true b=[35,809][863,1432] acts=[2131361799:Следваща новина]
thome1 W1 d7 cls=android.view.View text= desc=Последни новини pane= heading=false clk=false lclk=false focusable=false checkable=false checked=false enabled=true imp=true sel=false b=[0,809][1080,1432] acts=2131361799:Следваща новина;
thome1 W1 d8 cls=android.view.View text= desc= pane= heading=false clk=true lclk=false focusable=true checkable=false checked=false enabled=true imp=true sel=false b=[35,809][863,1432] acts=2131361799:Следваща новина;
thome1 W1 d8 cls=android.view.View text= desc=Съобщения pane= heading=false clk=false lclk=false focusable=false checkable=false checked=false enabled=true imp=true sel=false b=[965,195][1028,258] acts=
custom action: 11-01 20:01:24.807  7867  7867 I PCA11Y  : ACT 'Следваща новина' id=2131361799 on Последни новини -> true
TREE home2 (135 lines):
TREE nodes=132 windows: ['11-01 20:01:27.840  7867  7867 I PCDUMP  : thome2 W0 type=3 active=false focused=false title= pkg=com.android.systemui b=[0,0][1080,128]', '11-01 20:01:27.844  7867  7867 I PCDUMP  : thome2 W1 type=1 active=true focused=true title=НЧ Яворец pkg=org.chyavorec.app b=[0,0][1080,2400]']
TREE clickable=16 UNLABELLED clickable=0 []
TREE pane titles: ['Status bar', 'Начало']; headings: ['', 'Предстоящи събития']
TREE nodes with labelled (custom) actions: 2: 'Последни новини' [2131361799:Следваща новина;2131361800:Предишна новина] | '' [2131361799:Следваща новина;2131361800:Предишна новина]
TREE carousel nodes 'Последни новини': 1; nodes with Следваща/Предишна новина: 2: 'Последни новини' clk=false b=[0,809][1080,1432] acts=[2131361799:Следваща новина;2131361800:Предишна новина] | '' clk=true b=[126,809][954,1432] acts=[2131361799:Следваща новина;2131361800:Предишна новина]
current card before/after 'Следваща новина': desc=Последни новини pane → desc=Последни новини pane
## T3. Touch exploration (as with TalkBack): the carousel does not auto-advance; control without it
touch exploration true: touchExplorationEnabled=true
dumpsys accessibility: touchExplorationEnabled=true, serviceHandlesDoubleTap=false, requestMultiFingerGestures=false, requestTw Bound services:{Service[label=PC a11y, feedbackType[FEEDBACK_SPOKEN], capabilities=3, eventType Enabled services:{{org.pc.helper/org.pc.helper.A11y 
touch exploration false: touchExplorationEnabled=false
## T3b. Real TalkBack (com.google.android.marvin.talkbackoverlay): the carousel does not auto-advance
TalkBack services: 
TalkBack on: touchExplorationEnabled=false; bound: Bound services:{
## T4. Reduced motion (animator_duration_scale 0): the offline bar and the sync banner
offline: airplane=1
REC reduced0-offline: FRAMES 24 at 10 fps: changed frames [19]; longest run of consecutive changing frames 1 (1-2 = a jump, more = an animation); max diff 1.2
anims 0 offline: app in front yes; offline bar shown; text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и дн
anims 0 news offline: sync banner ?; text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="СЪБИТИЕ  ·  16 октомври 2026" 
REC reduced0-online: FRAMES 59 at 10 fps: changed frames [38, 40]; longest run of consecutive changing frames 1 (1-2 = a jump, more = an animation); max diff 1.5
anims 0 back online: offline bar gone
offline: airplane=1
REC reduced1-offline: FRAMES 117 at 10 fps: changed frames [35, 36, 37, 38, 40, 42, 43, 45, 105, 107, 108, 109, 110, 112, 114]; longest run of consecutive changing frames 4 (1-2 = a jump, more = an animation); max diff 14.4
anims 1 offline: app in front yes; offline bar NO; text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и дн
anims 1 news offline: sync banner shown; text="Календар" text="Новини" text="Търси в новините" text="Всички" text="Запазени" text="Събитие" text="Състави" text="Няма интернет връзка. Показваме последно наличните данни." 
REC reduced1-online: FRAMES 64 at 10 fps: changed frames [45, 46]; longest run of consecutive changing frames 2 (1-2 = a jump, more = an animation); max diff 1.4
anims 1 back online: offline bar gone
## T5. System font 1.3: the carousel title (up to 3 lines)
CAROUSEL text 'СЪБИТИЕ' h=17dp ≈ 0.4 lines of a 36sp×1.3 headline
CAROUSEL text 'Фолклорно пътешествие из Пиринска, Родопска и Трак' h=104dp ≈ 2.2 lines of a 36sp×1.3 headline
CAROUSEL text '16 октомври 2026' h=18dp ≈ 0.4 lines of a 36sp×1.3 headline
CAROUSEL card [35,1013][863,1636] 237dp tall
CAROUSEL text 'Фолклорно пътешествие из Пиринска, Родопска и Трак' h=67dp ≈ 1.9 lines of a 36sp×1.0 headline
CAROUSEL text '16 октомври 2026' h=14dp ≈ 0.4 lines of a 36sp×1.0 headline
CAROUSEL card [35,809][863,1432] 237dp tall
## COMPLETE
## logcat findings
app pids seen: ['5702', '7198', '7447', '8125', '8223', '8363', '8498', '9204', '10086', '10277']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 10
   skipped-frames events=10 max=100 total=540
== Davey: 16
   davey events=16 max=2210ms median=1068ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-1

```
part abstart-1; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): new-0 base-1 new-1 base-2 new-2
## AB new-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-0: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.29" text="Излезе нова версия на приложението." text="Размер: 4,1 МБ" )
new-0 FIRST-INSTALL catalogue results after 2982 ms
new-0 INDEX-READY #1 catalogue results after 2463 ms
new-0 INDEX-READY #2 catalogue results after 2421 ms
new-0 INDEX-READY #3 catalogue results after 2290 ms
START cold new-0         n=10 min=928ms median=1020ms max=1243ms  all=[993, 1048, 1092, 940, 1243, 1119, 928, 950, 1082, 946]  states=['COLD']
START warm new-0         n=10 min=154ms median=302ms max=394ms  all=[303, 339, 324, 255, 394, 154, 281, 299, 370, 302]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2396 ms
base-1 INDEX-READY #1 catalogue results after 2533 ms
base-1 INDEX-READY #2 catalogue results after 2330 ms
base-1 INDEX-READY #3 catalogue results after 2355 ms
START cold base-1        n=10 min=975ms median=1078ms max=1387ms  all=[1319, 1316, 1111, 975, 1063, 1031, 1387, 1031, 1057, 1094]  states=['COLD']
START warm base-1        n=10 min=132ms median=256ms max=465ms  all=[247, 365, 185, 132, 265, 226, 428, 465, 311, 223]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.29" text="Излезе нова версия на приложението." text="Размер: 4,1 МБ" )
new-1 FIRST-INSTALL catalogue results after 2440 ms
new-1 INDEX-READY #1 catalogue results after 2361 ms
new-1 INDEX-READY #2 catalogue results after 2318 ms
new-1 INDEX-READY #3 catalogue results after 2222 ms
START cold new-1         n=10 min=897ms median=1074ms max=1351ms  all=[1059, 1196, 1115, 1269, 1036, 897, 964, 1049, 1351, 1088]  states=['COLD']
START warm new-1         n=10 min=236ms median=288ms max=542ms  all=[287, 387, 276, 277, 542, 236, 290, 237, 449, 335]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2337 ms
base-2 INDEX-READY #1 catalogue results after 2445 ms
base-2 INDEX-READY #2 catalogue results after 2432 ms
base-2 INDEX-READY #3 catalogue results after 2383 ms
START cold base-2        n=10 min=920ms median=1059ms max=1242ms  all=[1047, 1084, 1034, 1124, 920, 1071, 1021, 1134, 1242, 1007]  states=['COLD']
START warm base-2        n=10 min=155ms median=278ms max=426ms  all=[335, 204, 320, 155, 339, 426, 170, 167, 237, 400]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.29" text="Излезе нова версия на приложението." text="Размер: 4,1 МБ" )
new-2 FIRST-INSTALL catalogue results after 2408 ms
new-2 INDEX-READY #1 catalogue results after 2442 ms
new-2 INDEX-READY #2 catalogue results after 2480 ms
new-2 INDEX-READY #3 catalogue results after 2266 ms
START cold new-2         n=10 min=930ms median=1028ms max=1259ms  all=[1259, 1058, 964, 930, 1042, 1230, 1088, 1013, 968, 948]  states=['COLD']
START warm new-2         n=10 min=124ms median=281ms max=321ms  all=[321, 297, 298, 320, 190, 124, 214, 283, 279, 175]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.30    n=20 min=897ms median=1054ms max=1351ms  all=[1059, 1196, 1115, 1269, 1036, 897, 964, 1049, 1351, 1088, 1259, 1058, 964, 930, 1042, 1230, 1088, 1013, 968, 948]  states=['COLD']
START cold BASE 1.2.29   n=20 min=920ms median=1067ms max=1387ms  all=[1319, 1316, 1111, 975, 1063, 1031, 1387, 1031, 1057, 1094, 1047, 1084, 1034, 1124, 920, 1071, 1021, 1134, 1242, 1007]  states=['COLD']
START warm NEW 1.2.30    n=20 min=124ms median=285ms max=542ms  all=[287, 387, 276, 277, 542, 236, 290, 237, 449, 335, 321, 297, 298, 320, 190, 124, 214, 283, 279, 175]  states=['HOT']
START warm BASE 1.2.29   n=20 min=132ms median=256ms max=465ms  all=[247, 365, 185, 132, 265, 226, 428, 465, 311, 223, 335, 204, 320, 155, 339, 426, 170, 167, 237, 400]  states=['HOT']
START catalogue first-install new n=2 min=2408ms median=2424ms max=2440ms  all=[2440, 2408]  states=[]
START catalogue index-ready new n=6 min=2222ms median=2340ms max=2480ms  all=[2361, 2318, 2222, 2442, 2480, 2266]  states=[]
START catalogue first-install base n=2 min=2337ms median=2366ms max=2396ms  all=[2396, 2337]  states=[]
START catalogue index-ready base n=6 min=2330ms median=2408ms max=2533ms  all=[2533, 2330, 2355, 2445, 2432, 2383]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5903', '7240', '7443', '7588', '7731', '7887', '7947', '8012', '8068', '8144', '8198', '8264', '8316', '8381', '8792', '9064', '9209', '9378', '9529', '9602', '9659', '9726', '9791', '9846', '9900', '10099', '10156', '10212', '10636', '10935', '11080', '11228', '11372', '11444', '11506', '11561', '11615', '11674', '11727', '11780', '11835', '11888', '12400', '12761', '12901', '13044', '13192', '13264', '13320', '13376', '13431', '13485', '13539', '13594', '13646', '13700', '14102', '14397', '14537', '14696', '14841', '14921', '14975', '15032', '15084', '15136', '15188', '15241', '15294', '15417']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 14
   skipped-frames events=14 max=65 total=546
== Davey: 10
   davey events=10 max=1081ms median=824ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (the first, '-0', is a warm-up and is excluded): base-0 new-1 base-1 new-2 base-2
## AB base-0: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-0: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-0 FIRST-INSTALL catalogue results after 2323 ms
base-0 INDEX-READY #1 catalogue results after 2328 ms
base-0 INDEX-READY #2 catalogue results after 2716 ms
base-0 INDEX-READY #3 catalogue results after 2436 ms
START cold base-0        n=10 min=895ms median=1027ms max=1157ms  all=[1026, 1052, 979, 895, 1157, 976, 1020, 1050, 1138, 1028]  states=['COLD']
START warm base-0        n=10 min=224ms median=326ms max=460ms  all=[228, 242, 326, 460, 305, 418, 391, 327, 224, 329]  states=['HOT']
## AB new-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-1: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 FIRST-INSTALL catalogue results after 2491 ms
new-1 INDEX-READY #1 catalogue results after 2506 ms
new-1 INDEX-READY #2 catalogue results after 2427 ms
new-1 INDEX-READY #3 catalogue results after 2487 ms
START cold new-1         n=10 min=1044ms median=1140ms max=1246ms  all=[1246, 1149, 1167, 1063, 1131, 1162, 1044, 1118, 1189, 1110]  states=['COLD']
START warm new-1         n=10 min=133ms median=276ms max=450ms  all=[363, 332, 281, 133, 303, 243, 272, 188, 450, 176]  states=['HOT']
## AB base-1: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-1: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 FIRST-INSTALL catalogue results after 2280 ms
base-1 INDEX-READY #1 catalogue results after 2359 ms
base-1 INDEX-READY #2 catalogue results after 2500 ms
base-1 INDEX-READY #3 catalogue results after 2426 ms
START cold base-1        n=10 min=893ms median=1045ms max=1255ms  all=[960, 934, 1075, 893, 1255, 1133, 930, 1078, 1015, 1121]  states=['COLD']
START warm base-1        n=10 min=244ms median=316ms max=730ms  all=[292, 324, 247, 730, 309, 274, 244, 352, 498, 531]  states=['HOT']
## AB new-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed new-2: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 FIRST-INSTALL catalogue results after 2266 ms
new-2 INDEX-READY #1 catalogue results after 2433 ms
new-2 INDEX-READY #2 catalogue results after 2442 ms
new-2 INDEX-READY #3 catalogue results after 2306 ms
START cold new-2         n=10 min=948ms median=1058ms max=1160ms  all=[1057, 977, 948, 1058, 1102, 1160, 1143, 1092, 1014, 1041]  states=['COLD']
START warm new-2         n=10 min=224ms median=328ms max=504ms  all=[259, 224, 284, 359, 323, 333, 318, 370, 364, 504]  states=['HOT']
## AB base-2: fresh install, first launch, catalogue, 10 cold + 10 warm starts
installed base-2: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 FIRST-INSTALL catalogue results after 2360 ms
base-2 INDEX-READY #1 catalogue results after 2526 ms
base-2 INDEX-READY #2 catalogue results after 2449 ms
base-2 INDEX-READY #3 catalogue results after 2456 ms
START cold base-2        n=10 min=939ms median=1070ms max=1213ms  all=[1153, 1127, 1068, 1213, 1049, 939, 1042, 945, 1175, 1073]  states=['COLD']
START warm base-2        n=10 min=179ms median=264ms max=419ms  all=[239, 372, 278, 190, 276, 253, 419, 233, 179, 282]  states=['HOT']
## AB totals (blocks 1-2 of each app; block 0 excluded)
START cold NEW 1.2.30    n=20 min=948ms median=1106ms max=1246ms  all=[1246, 1149, 1167, 1063, 1131, 1162, 1044, 1118, 1189, 1110, 1057, 977, 948, 1058, 1102, 1160, 1143, 1092, 1014, 1041]  states=['COLD']
START cold BASE 1.2.29   n=20 min=893ms median=1070ms max=1255ms  all=[960, 934, 1075, 893, 1255, 1133, 930, 1078, 1015, 1121, 1153, 1127, 1068, 1213, 1049, 939, 1042, 945, 1175, 1073]  states=['COLD']
START warm NEW 1.2.30    n=20 min=133ms median=310ms max=504ms  all=[363, 332, 281, 133, 303, 243, 272, 188, 450, 176, 259, 224, 284, 359, 323, 333, 318, 370, 364, 504]  states=['HOT']
START warm BASE 1.2.29   n=20 min=179ms median=280ms max=730ms  all=[292, 324, 247, 730, 309, 274, 244, 352, 498, 531, 239, 372, 278, 190, 276, 253, 419, 233, 179, 282]  states=['HOT']
START catalogue first-install new n=2 min=2266ms median=2378ms max=2491ms  all=[2491, 2266]  states=[]
START catalogue index-ready new n=6 min=2306ms median=2438ms max=2506ms  all=[2506, 2427, 2487, 2433, 2442, 2306]  states=[]
START catalogue first-install base n=2 min=2280ms median=2320ms max=2360ms  all=[2280, 2360]  states=[]
START catalogue index-ready base n=6 min=2359ms median=2452ms max=2526ms  all=[2359, 2500, 2426, 2526, 2449, 2456]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5945', '6667', '6827', '7078', '7430', '7499', '7566', '7621', '7675', '7742', '7815', '7887', '7944', '8004', '8423', '8692', '8868', '9029', '9194', '9271', '9335', '9390', '9454', '9507', '9559', '9612', '9694', '9746', '10141', '10396', '10538', '10684', '10831', '10903', '10971', '11025', '11080', '11133', '11187', '11253', '11308', '11362', '11910', '12230', '12372', '12519', '12665', '12738', '12792', '12846', '12902', '12952', '13005', '13057', '13112', '13167', '13558', '13812', '13961', '14105', '14252', '14325', '14379', '14434', '14490', '14543', '14597', '14652', '14719', '14776']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 16
   skipped-frames events=16 max=36 total=518
== Davey: 17
   davey events=17 max=1366ms median=842ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## dialogs

```
part dialogs; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3480 ms
## D-std. Dialogs at 'std' (font_scale 1.0)
DIALOG std-01-text-size: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=7: 'Размер на текста' 28dp; 'Умножава размера на шрифта о' 81dp; 'Малък' 21dp; 'Стандартен' 21dp; 'Голям' 21dp; 'Много голям' 21dp; 'Затвори' 20dp
DIALOG std-02-clear-cache: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=4: 'Изчисти временните данни' 28dp; 'Временните данни (новини, съ' 171dp; 'Отказ' 20dp; 'Изчисти' 20dp
DIALOG std-03-sort-menu: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=6: 'Най-подходящи  ✓' 20dp; 'Заглавие (А–Я)' 20dp; 'Автор (А–Я)' 20dp; 'Година (нови първо)' 20dp; 'Година (стари първо)' 20dp; 'Нови постъпления' 20dp
DIALOG std-04-filters: TEXTH packages=['org.chyavorec.app'] scrollable=1 ['[0,255][1080,2337]'] texts=25: 'Филтри' 28dp; 'Без филтри' 20dp; 'Само налични' 19dp; 'Вид документ' 19dp; 'книга' 17dp; 'видеодокумент' 17dp; 'картографско издание' 17dp; 'друго' 17dp
filters after a swipe: TEXTH packages=['org.chyavorec.app'] scrollable=1 ['[0,255][1080,2337]'] texts=26: 'Вид документ' 19dp; 'книга' 17dp; 'видеодокумент' 17dp; 'картографско издание' 17dp
whats-new record: 1.2.30 (1488543), 6 lines
prefs: 2154 bytes after adding ['pending_whats_new']
DIALOG std-05-whats-new: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=2: 'Какво ново във версия 1.2.30' 27dp; '• По избор: картата, заемани' 368dp
TEXT SIZE now: text="Много голям" 
## D-xl. Dialogs at 'xl' (font_scale 1.0)
DIALOG xl-01-text-size: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=7: 'Размер на текста' 32dp; 'Умножава размера на шрифта о' 136dp; 'Малък' 29dp; 'Стандартен' 29dp; 'Голям' 29dp; 'Много голям' 29dp; 'Затвори' 27dp
DIALOG xl-02-clear-cache: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=4: 'Изчисти временните данни' 63dp; 'Временните данни (новини, съ' 314dp; 'Отказ' 27dp; 'Изчисти' 27dp
DIALOG xl-03-sort-menu: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=6: 'Най-подходящи  ✓' 27dp; 'Заглавие (А–Я)' 27dp; 'Автор (А–Я)' 27dp; 'Година (нови първо)' 27dp; 'Година (стари първо)' 27dp; 'Нови постъпления' 27dp
DIALOG xl-04-filters: TEXTH packages=['org.chyavorec.app'] scrollable=1 ['[0,255][1080,2337]'] texts=21: 'Филтри' 31dp; 'Без филтри' 27dp; 'Само налични' 24dp; 'Вид документ' 24dp; 'книга' 22dp; 'видеодокумент' 22dp; 'картографско издание' 22dp; 'друго' 22dp
filters after a swipe: TEXTH packages=['org.chyavorec.app'] scrollable=1 ['[0,255][1080,2337]'] texts=20: 'Вид документ' 24dp; 'книга' 22dp; 'видеодокумент' 22dp; 'картографско издание' 22dp
whats-new record: 1.2.30 (1488543), 6 lines
prefs: 2178 bytes after adding ['pending_whats_new']
DIALOG xl-05-whats-new: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=2: 'Какво ново във версия 1.2.30' 30dp; '• По избор: картата, заемани' 364dp
## D-xl130. 'Много голям' + system font 1.3 (the stress case)
## D-xl130. Dialogs at 'xl130' (font_scale 1.3)
DIALOG xl130-01-text-size: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=7: 'Размер на текста' 37dp; 'Умножава размера на шрифта о' 240dp; 'Малък' 35dp; 'Стандартен' 35dp; 'Голям' 35dp; 'Много голям' 35dp; 'Затвори' 34dp
DIALOG xl130-02-clear-cache: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=4: 'Изчисти временните данни' 75dp; 'Временните данни (новини, съ' 496dp; 'Отказ' 34dp; 'Изчисти' 34dp
DIALOG xl130-03-sort-menu: TEXTH packages=['org.chyavorec.app'] scrollable=0 [] texts=6: 'Най-подходящи  ✓' 34dp; 'Заглавие (А–Я)' 34dp; 'Автор (А–Я)' 34dp; 'Година (нови първо)' 34dp; 'Година (стари първо)' 68dp; 'Нови постъпления' 34dp
DIALOG xl130-04-filters: TEXTH packages=['org.chyavorec.app'] scrollable=1 ['[0,255][1080,2337]'] texts=18: 'Филтри' 37dp; 'Без филтри' 34dp; 'Само налични' 29dp; 'Вид документ' 29dp; 'книга' 27dp; 'видеодокумент' 27dp; 'картографско издание' 27dp; 'друго' 27dp
filters after a swipe: TEXTH packages=['org.chyavorec.app'] scrollable=1 ['[0,255][1080,2337]'] texts=17: 'електронен документ' 5dp; 'графично издание' 27dp; 'аудиодокумент' 27dp; 'продължаващо издание' 27dp
whats-new record: 1.2.30 (1488543), 6 lines
prefs: 2178 bytes after adding ['pending_whats_new']
DIALOG xl130-05-whats-new: TEXTH packages=['org.chyavorec.app'] scrollable=1 ['[0,1327][1080,2400]'] texts=2: 'Какво ново във версия 1.2.30' 37dp; '• По избор: картата, заемани' 358dp
## D-ratio. Dialog text height ×(Много голям / Стандартен) next to the screen text
RATIO 00-settings-screen xl/std: RATIO 17 common texts: median 1.32 min 1.11 max 2.89; 'Изглед' 1.32; 'Тема' 1.26; 'Както системата' 1.34; 'Размер на текста' 1.26; 'Палитра' 1.26; 'Яворец' 1.32; 'Злато и жито' 2.89; 'Левски' 1.32
RATIO 01-text-size xl/std: RATIO 7 common texts: median 1.34 min 1.12 max 1.69; 'Размер на текста' 1.12; 'Умножава размера на ' 1.69; 'Малък' 1.34; 'Стандартен' 1.34; 'Голям' 1.34; 'Много голям' 1.34; 'Затвори' 1.34
RATIO 02-clear-cache xl/std: RATIO 4 common texts: median 1.59 min 1.34 max 2.24; 'Изчисти временните д' 2.24; 'Временните данни (но' 1.84; 'Отказ' 1.34; 'Изчисти' 1.34
RATIO 03a-catalog-screen xl/std: RATIO 29 common texts: median 1.31 min 1.26 max 2.78; 'Търси навсякъде' 1.26; 'Всичко' 1.32; 'Заглавие' 1.32; 'Автор' 1.32; 'ISBN' 1.32; '2446 резултата  ·  д' 2.78; '"Ох..."' 1.27; 'Джиан, Филип' 1.32
RATIO 03-sort-menu xl/std: RATIO 6 common texts: median 1.34 min 1.34 max 1.34; 'Най-подходящи  ✓' 1.34; 'Заглавие (А–Я)' 1.34; 'Автор (А–Я)' 1.34; 'Година (нови първо)' 1.34; 'Година (стари първо)' 1.34; 'Нови постъпления' 1.34
RATIO 04-filters xl/std: RATIO 21 common texts: median 1.32 min 0.05 max 1.34; 'Филтри' 1.11; 'Без филтри' 1.34; 'Само налични' 1.27; 'Вид документ' 1.27; 'книга' 1.32; 'видеодокумент' 1.32; 'картографско издание' 1.32; 'друго' 1.32
RATIO 05-whats-new xl/std: RATIO 2 common texts: median 1.07 min 0.99 max 1.14; 'Какво ново във верси' 1.14; '• По избор: картата,' 0.99
WARN: tab bar not reached
WARN: Settings not found
## COMPLETE
## logcat findings
app pids seen: ['5198', '8443', '9694', '9869', '10985']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 18
   skipped-frames events=18 max=112 total=755
== Davey: 37
   davey events=37 max=1422ms median=898ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## emoji26

```
part emoji26; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104418 bytes
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
## W. First launch and warm-up
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
first-install catalogue results after 1552 ms
## M1. Texts with emoji on API 26 (no EmojiCompat)
EMOJI 1 (news news-svetoven-den-na-hlyaba 'Световен ден на хляба! 🍞🫓🥖'): Световен ден на хляба! 🍞🫓🥖 | Световен ден на хляба! 🍞🫓🥖 | От сърце ❤️ благодарим на всички, които се включиха в празника със своите домашно приготвени погачи!
EMOJI 2 (news news-obuchenie-po-narodni-tantsi 'Обучение по народни танци'): Когато две села се хванат на едно хоро – Яворец и Батошево заедно в танца 🕺 Онази обява (за 16 май, 15:00 ч., с гост-лек
EMOJI 3: 'Световен ден на хляба! 🍞🫓🥖' — no URL and no Latin word to search for
EMOJI 4 (news news-mt5mv5hvwdzg 'Танцовият клуб участие във VW Fest Узана'): 
EMOJI 5: 'Обучение по народни танци' — no URL and no Latin word to search for
## M2. Cold starts (10), API 26
START cold API 26        n=10 min=640ms median=715ms max=757ms  all=[701, 729, 735, 667, 744, 736, 656, 698, 640, 757]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['4829', '5401', '5445', '5502', '5552', '5596', '5640', '5687', '5730', '5779', '5824']
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
part emoji28; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
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
## W. First launch and warm-up
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
first-install catalogue results after 1349 ms
## M1. Texts with emoji on API 28 (no EmojiCompat)
EMOJI 1 (news news-svetoven-den-na-hlyaba): 
EMOJI 2 (news news-obuchenie-po-narodni-tantsi): 
EMOJI 3 (news news-mvbb0tp9hi2d): 
EMOJI 4 (news news-mt5mv5hvwdzg): 
EMOJI 5 (news news-msuodcr0yrmu): 
## M2. Cold starts (10), API 28
START cold API 28        n=10 min=732ms median=884ms max=1023ms  all=[851, 1023, 846, 904, 865, 1006, 787, 913, 732, 950]  states=[]
## COMPLETE
## logcat findings
app pids seen: ['5372', '5979', '6024', '6073', '6121', '6161', '6216', '6258', '6306', '6346', '6399']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 1
   davey events=1 max=875ms median=875ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## emoji33

```
part emoji33; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
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
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4730 ms
## M1. Texts with emoji on API 33 (no EmojiCompat)
EMOJI 1 (news news-svetoven-den-na-hlyaba 'Световен ден на хляба! 🍞🫓🥖'): Световен ден на хляба! 🍞🫓🥖 | Световен ден на хляба! 🍞🫓🥖 | От сърце ❤️ благодарим на всички, които се включиха в празника със своите домашно приготвени погачи!
EMOJI 2 (news news-obuchenie-po-narodni-tantsi 'Обучение по народни танци'): Когато две села се хванат на едно хоро – Яворец и Батошево заедно в танца 🕺 Онази обява (за 16 май, 15:00 ч., с гост-лек
EMOJI 3: 'Световен ден на хляба! 🍞🫓🥖' — no URL and no Latin word to search for
EMOJI 4 (news news-mt5mv5hvwdzg 'Танцовият клуб участие във VW Fest Узана'): Под ръководството на Ради Мирчев танцьорите представиха български народни танци и зарадваха публиката със своите изпълне
EMOJI 5: 'Обучение по народни танци' — no URL and no Latin word to search for
## M2. Cold starts (10), API 33
START cold API 33        n=10 min=1809ms median=2236ms max=3160ms  all=[2716, 2811, 3160, 1999, 2507, 2055, 2375, 1809, 1924, 2098]  states=['COLD']
## COMPLETE
## logcat findings
app pids seen: ['4124', '7002', '7106', '7214', '7281', '7334', '7384', '7439', '7490', '7541', '7603']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 22
   skipped-frames events=22 max=89 total=1062
== Davey: 32
   davey events=32 max=2642ms median=934ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## events

```
part events; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## E0. Site data (host): events with a place; texts with emoji
SITE news.json: 25 items; keys of the first: ['author', 'category', 'content', 'cover', 'date', 'excerpt', 'id', 'tags', 'title']
SITE events: 75, with a place: 75; places: ['НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец']
EMOJI news id=news-svetoven-den-na-hlyaba url=/news/svetoven-den-na-hlyaba 🍞🥖🫓 'Световен ден на хляба! 🍞🫓🥖'
EMOJI news id=news-obuchenie-po-narodni-tantsi url=/news/obuchenie-po-narodni-tantsi 🕺 'Обучение по народни танци'
EMOJI news id=news-mvbb0tp9hi2d url=None ❤️🍞🥖🫓 'Световен ден на хляба! 🍞🫓🥖'
EMOJI news id=news-mt5mv5hvwdzg url=None 🇧🇬 'Танцовият клуб участие във VW Fest Узана'
EMOJI news id=news-msuodcr0yrmu url=None 🔹🕺 'Обучение по народни танци'
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3105 ms
## E1. Events list: the cards show the time and the description, not the place
CARD ['1', 'НОЕ', 'Ден на народните будители', 'Тържество с ученически рецитали, четения и музикална програма в памет на народните будители.'] place-in-card=no
CARD ['8', 'НОЕ', 'Михаил В. Ломоносов', '315 г. от рождението на Михаил В. Ломоносов — учен и енциклопедист, основател на естествознанието (1711–1765).'] place-in-card=no
CARD ['16', 'НОЕ', '80 г. от създаването на ЮНЕСКО', 'Организацията на ООН за образование, наука и култура е основана на 16 ноември 1946 г.'] place-in-card=no
CARD ['21', 'НОЕ', 'Панайот Пипков · Ден на семейството', '155 г. от рождението на Панайот Пипков — автор на „Върви народе възродени“. Ден на християнското семейство.'] place-in-card=no
CARD ['1', 'ДЕК', 'Световен ден за борба срещу СПИН', 'Информационна инициатива за младежи и ученици.'] place-in-card=no
CARDS 5 (known places: 75)
CARD ['6', 'ДЕК', 'Никулден', 'Традиционен народен и религиозен празник.'] place-in-card=no
CARD ['20', 'Игнажден'] place-in-card=no
CARDS 6 (known places: 75)
## E2. An event with a place: the address line and 'Маршрут' → a geo: intent
EVENT with a place: text="80 г. от създаването на ЮНЕСКО" text="16 ноември 2026" text="Събитие от годишния календар на читалището — повтаря се всяка година на тази дата." text="Библиотека" text="НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец" text="Организацията на ООН за образование, наука и култура е основана на 16 ноември 1946 г." text="Добави в календара" text="Маршрут" text="Напомни ми" text="Виж публикацията" content-desc="Назад" content-desc="Сподели" 
EVENT place line: Събитие от годишния календар на читалището — повтаря се всяка година на тази дата.|НЧ „Васил Левски – 1922“, пл. 9-ти септември 3, 5334 с. Яворец|
ROUTE 11: fg com.google.android.apps.maps/com.google.android.maps.MapsActivity; geo intent: act=android.intent.action.VIEW dat=geo: flg=0x10000000 cmp=com.google.android.apps.maps/com.google.android.maps.MapsActivity}; cmp=com.google.android.apps.maps/com.google.android.maps.MapsActivity 
REMINDER set on this event: text="Ще получиш напомняне на 15 ноември 2026, 18:00."
## E3. The event reminder notification: 'Маршрут'
app jobs: 0 4 5 
jobscheduler run -f 0: Running job [FORCED]
jobscheduler run -f 4: Running job [FORCED]
jobscheduler run -f 5: Running job [FORCED]
NOTIF id=37891 channel=events title=String (Напомняне за събитие) text=String (80 г. от създаването на ЮНЕСКО) template=String (android.app.Notification$BigTextStyle) picture=NO largeIcon=yes actions=2 ['Добави в календара', 'Маршрут']
NOTIF total for org.chyavorec.app: 1
SHADE 20-reminder expanded: buttons [text="Добави в календара" text="Маршрут" ]; big picture view no; text="On" content-desc="Internet,AndroidWifi" text="Internet" text="On" content-desc="Bluetooth." text="Bluetooth" text="Flashlight" text="Off" content-desc="Do Not Disturb." text="Do Not Disturb" text="Chitalishte Yavorets" text="•" text="now" content-desc="Alerted" content-desc="Collapse" text="Напомняне за събитие" 
ACTION 'Маршрут' → fg com.google.android.apps.maps/com.google.android.maps.MapsActivity; text="Make it your map" text="Sign in for quick access to your favorite places and get better recommendations" text="Sign in" text="SKIP" 
ROUTE from the notification: geo intent: dat=geo:
## E4. Texts with emoji (API 33)
EMOJI 1 (news news-svetoven-den-na-hlyaba): Световен ден на хляба! 🍞🫓🥖|Световен ден на хляба! 🍞🫓🥖|
## COMPLETE
## logcat findings
app pids seen: ['4828']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=88 total=228
== Davey: 6
   davey events=6 max=1500ms median=893ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## login

```
part login; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104418 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## LG0. Demo build (devDebug, org.chyavorec.app.dev): demo sign-in — wrong PINs never reach the real server
installed: devDebug 1.2.30-dev bf10741; org.chyavorec.app.dev; demo reader services;  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3650 ms
LOGIN screen: text="ДЕМО ДАННИ — само за разработка, не са реални" text="Вход за читатели" text="Демо вход: DEMO-0001 / demo" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от Android Keystore." content-desc="Назад" text="Вход" 
## LG1. Card number TEST-0042 (fake)
## LG1a. Esc (hardware keyboard) in the focused card field: the field keeps the text, the screen stays?
before Esc: FIELD 0: text='TEST-0042'  focused=true hint=''; IME mInputShown=true
after Esc #1: login screen STILL; FIELD 0: text='TEST-0042'  focused=true hint=''; IME mInputShown=false; TAB selected: none
## LG1b. Two wrong attempts (fake card TEST-0042, PIN 1111)
LOGIN 11-attempt-1: error 'text="Грешен читателски номер или ПИН."' status 'text="Остават 4 опита преди временно блокиране."'; Вход button: clickable="false"; fields: FIELD 0: text='TEST-0042' password=false focused=false hint='' / FIELD 1: text='' password=true focused=true hint=''
LOGIN 12-attempt-2: error 'text="Грешен читателски номер или ПИН."' status 'text="Остават 3 опита преди временно блокиране."'; Вход button: clickable="false"; fields: FIELD 0: text='TEST-0042' password=false focused=false hint='' / FIELD 1: text='' password=true focused=true hint=''
## LG2. Forgot PIN (card TEST-0042, PIN 9876 typed, not sent): the sheet; the email subject
FORGOT 20-demo sheet: content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="Забравен ПИН" text="Нов ПИН се издава само в библиотеката — обърни се към библиотекаря." text="Изпрати имейл · chitalishte_yavorec@abv.bg" 
FORGOT 20-demo email button: text="Изпрати имейл · chitalishte_yavorec@abv.bg"
EMAIL 20-demo: chooser: text="Send using Gmail" text="Just once" text="Always" text="Use a different app" text="PC helper" text="PC catcher" 
EMAIL 20-demo: chooser: text="PC catcher" text="PCCATCH action=android.intent.action.SENDTO data=mailto: | android.intent.extra.SUBJECT=Забравен ПИН — читателска карта TEST-0042 | android.intent.extra.EMAIL=[chitalishte_yavo
EMAIL 20-demo: chooser: text="PC catcher" text="PCCATCH action=android.intent.action.SENDTO data=mailto: | android.intent.extra.SUBJECT=Забравен ПИН — читателска карта TEST-0042 | android.intent.extra.EMAIL=[chitalishte_yavo
EMAIL 20-demo: chooser: text="PC catcher" text="PCCATCH action=android.intent.action.SENDTO data=mailto: | android.intent.extra.SUBJECT=Забравен ПИН — читателска карта TEST-0042 | android.intent.extra.EMAIL=[chitalishte_yavo
EMAIL 20-demo: fg org.pc.helper/org.pc.helper.Catch; catcher: action=android.intent.action.SENDTO data=mailto: | android.intent.extra.SUBJECT=Забравен ПИН — читателска карта TEST-0042 | android.intent.extra.EMAIL=[chitalishte_yavorec@abv.bg]
EMAIL 20-demo: activity starts: act=android.intent.action.SENDTO dat=mailto: flg=0x10000000 cmp=android/com.android.internal.app.ResolverActivity~act=android.intent.action.SENDTO dat=mailto: flg=0x13000000 cmp=org.pc.helper/.Catch~
EMAIL subject has the card number: 1; has a PIN (9876/1111): 0
## LG3. Attempts 3-5: the 5th locks sign-in; the countdown
LOGIN 13-attempt-3: error 'text="Грешен читателски номер или ПИН."' status 'text="Остават 2 опита преди временно блокиране."'; Вход button: clickable="false"; fields: FIELD 0: text='TEST-0042' password=false focused=false hint='' / FIELD 1: text='' password=true focused=true hint=''
LOGIN 14-attempt-4: error 'text="Грешен читателски номер или ПИН."' status 'text="Остава 1 опит преди временно блокиране."'; Вход button: clickable="false"; fields: FIELD 0: text='TEST-0042' password=false focused=false hint='' / FIELD 1: text='' password=true focused=true hint=''
LOGIN 15-attempt-5: error 'text="Опитай отново след 0:55."' status 'text="Твърде много грешни опити — входът е временно блокиран."'; Вход button: clickable="false"; fields: FIELD 0: text='TEST-0042' password=false focused=false hint='' / FIELD 1: text='' password=true focused=true hint=''
LOGIN 16-locked-again-3s: error 'text="Опитай отново след 0:53."' status 'text="Твърде много грешни опити — входът е временно блокиран."'; Вход button: clickable="false"; fields: FIELD 0: text='TEST-0042' password=false focused=false hint='' / FIELD 1: text='' password=true focused=true hint=''
LOGIN 17-locked-after-8s: error 'text="Опитай отново след 0:46."' status 'text="Твърде много грешни опити — входът е временно блокиран."'; Вход button: clickable="false"; fields: FIELD 0: text='TEST-0042' password=false focused=false hint='' / FIELD 1: text='' password=true focused=true hint=''
## LG4. 'Запомни ме' is a CheckBox
CHECKABLE class=android.widget.CheckBox label='Запомни ме на това устройство' checked=true clickable=true bounds=[63,1604][1017,1730] nested-checkables=0
## LG5. Production build: the sign-in screen (no submit), 'Запомни ме', 'Забравен ПИН?' without a card number
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
PROD login: text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от Android Keystore." content-desc="Назад" tex
prod: CHECKABLE class=android.widget.CheckBox label='Запомни ме на това устройство' checked=true clickable=true bounds=[63,1161][1017,1287] nested-checkables=0
FORGOT 31-prod sheet: content-desc="Затваряне на таблицата" content-desc="Манипулатор за преместване с плъзгане" text="Забравен ПИН" text="Нов ПИН се издава само в библиотеката — обърни се към библиотекаря." text="Изпрати имейл · chitalishte_yavorec@abv.bg" 
FORGOT 31-prod email button: text="Изпрати имейл · chitalishte_yavorec@abv.bg"
EMAIL 31-prod: fg android/com.android.internal.app.ResolverActivity; catcher: 
EMAIL 31-prod: activity starts: act=android.intent.action.SENDTO dat=mailto: flg=0x10000000 cmp=android/com.android.internal.app.ResolverActivity~
## COMPLETE
## logcat findings
app pids seen: ['8714']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 0
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look1

```
part look1; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15700 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.29" text="Излезе нова версия на приложението." text="Размер: 4,1 МБ" )
first-install catalogue results after 2859 ms
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
app pids seen: ['5349', '7227', '9117', '10979', '12463']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 9
   skipped-frames events=9 max=44 total=308
== Davey: 22
   davey events=22 max=1201ms median=800ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look2

```
part look2; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2152 ms
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
app pids seen: ['6026', '7902', '10277']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=43 total=111
== Davey: 6
   davey events=6 max=881ms median=750ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look3

```
part look3; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## L0. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3693 ms
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
app pids seen: ['5177', '7242', '9731', '11652']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 22
   skipped-frames events=22 max=205 total=1075
== Davey: 63
   davey events=63 max=3590ms median=878ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem-base

```
part mem-base; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
installed base: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414;  versionCode=1488542 minSdk=26 targetSdk=37  versionName=1.2.29 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2902 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), base
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=59782KB  Java=7836KB  Native=33820KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=66900KB  Java=8428KB  Native=33820KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=95391KB  Java=12228KB  Native=30480KB  Graphics=0KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=113847KB  Java=21672KB  Native=30212KB  Graphics=0KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=99924KB  Java=11156KB  Native=26928KB  Graphics=0KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=102176KB  Java=11128KB  Native=27856KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=121627KB  Java=23892KB  Native=37268KB  Graphics=0KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=103353KB  Java=11252KB  Native=31892KB  Graphics=0KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=105505KB  Java=11164KB  Native=39520KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=117444KB  Java=14684KB  Native=50816KB  Graphics=0KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=109560KB  Java=11076KB  Native=46728KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 7189; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=66900 c5=95391 c10=99924 c15=102176 c20=103353 c25=105505 c30=109560
LEAK TREND (cycles 10-30, least squares): +452 KB per cycle (+8.8 MB over 20 cycles)
## COMPLETE
## logcat findings
app pids seen: ['5854', '7189']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 39
   skipped-frames events=39 max=41 total=1322
== Davey: 131
   davey events=131 max=1377ms median=793ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## mem

```
part mem; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2742 ms
## M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), new
tab home @ 99 2274
tab catalog @ 320 2274
tab my @ 540 2274
tab news @ 760 2274
tab more @ 980 2274
MEM c00 (raw)        PSS=59970KB  Java=6576KB  Native=9672KB  Graphics=0KB  Views=9 Activities=1
MEM c00 (trimmed)    PSS=60448KB  Java=6760KB  Native=5072KB  Graphics=0KB  Views=9 Activities=1
cycle 1: book screen opened
MEM c05 (trimmed)    PSS=107884KB  Java=11016KB  Native=33484KB  Graphics=0KB  Views=13 Activities=1
cycles done: 10, gallery viewer reached in 10, search expanded in 10
MEM c10 (raw)        PSS=131245KB  Java=17072KB  Native=31968KB  Graphics=0KB  Views=20 Activities=1
MEM c10 (trimmed)    PSS=121451KB  Java=10540KB  Native=28440KB  Graphics=0KB  Views=13 Activities=1
MEM c15 (trimmed)    PSS=105996KB  Java=10460KB  Native=27828KB  Graphics=0KB  Views=13 Activities=1
cycles done: 20, gallery viewer reached in 20, search expanded in 20
MEM c20 (raw)        PSS=125511KB  Java=12332KB  Native=44776KB  Graphics=0KB  Views=20 Activities=1
MEM c20 (trimmed)    PSS=118502KB  Java=10408KB  Native=39716KB  Graphics=0KB  Views=13 Activities=1
MEM c25 (trimmed)    PSS=109469KB  Java=10392KB  Native=38768KB  Graphics=0KB  Views=13 Activities=1
cycles done: 30, gallery viewer reached in 30, search expanded in 30
MEM c30 (raw)        PSS=128900KB  Java=25060KB  Native=50360KB  Graphics=0KB  Views=20 Activities=1
MEM c30 (trimmed)    PSS=110752KB  Java=10484KB  Native=46844KB  Graphics=0KB  Views=13 Activities=1
pid after cycles: 6908; gallery viewer reached in 30/30, expanded search with '12' in 30/30 cycles
PSS after trim by cycle (KB): c0=60448 c5=107884 c10=121451 c15=105996 c20=118502 c25=109469 c30=110752
LEAK TREND (cycles 10-30, least squares): -358 KB per cycle (-7.0 MB over 20 cycles)
## COMPLETE
## logcat findings
app pids seen: ['5306', '6908']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 30
   skipped-frames events=30 max=47 total=1077
== Davey: 104
   davey events=104 max=1360ms median=785ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## monkey

```
part monkey; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2786 ms
## K. Monkey 6000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=6299
MONKEY exit=0 duration=271 s; Events injected: 6000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6299'; airplane=0; camera: 
MEM after-monkey (raw) PSS=117419KB  Java=23760KB  Native=39220KB  Graphics=0KB  Views=19 Activities=1
MEM after-monkey (trimmed) PSS=101681KB  Java=11364KB  Native=32156KB  Graphics=0KB  Views=13 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['4798', '6299']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 66
   skipped-frames events=66 max=70 total=2676
== Davey: 169
   davey events=169 max=1727ms median=881ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## offline

```
part offline; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## O1. Online warm-up (fills the caches)
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2846 ms
online-warm home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и днес пазят читалището живо. Честит празник!" text="+ 5 Неделя след Неделя подир Въздвижение. Св. безср. и чудотворци Козма и Дамян. Св. прпмчц
online-warm catalogue: results (2446, 3011 ms)
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
offline-cached home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и днес пазят читалището живо. Честит празник!" text="+ 5 Неделя след Неделя подир Въздвижение. Св. безср. и чудотворци Козма и Дамян. Св. прпмчц
offline-cached catalogue: results (2446, 2358 ms)
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
recovered home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и днес пазят читалището живо. Честит празник!" text="+ 5 Неделя след Неделя подир Въздвижение. Св. безср. и чудотворци Козма и Дамян. Св. прпмчц
recovered catalogue: results (2446, 2392 ms)
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
offline-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и днес пазят читалището живо. Честит празник!" content-desc="Скрий поздрава" content-desc="Лента за търсене" text="Търси книга, новина, събитие…
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
GALLERY (empty cache) reconnect: albums shown after 7712 ms (-1 = not within 60 s); text="29 снимки" text="Фотодокументална изложба" text="4 снимки" text="Фолклорна група „Явор“" text="6 снимки" text="Клуб за народни танци" text="7 снимки" text="Ансамбъл „Яворски хлапета“" 
recovered-empty home: text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и днес пазят читалището живо. Честит празник!" content-desc="Скрий поздрава" content-desc="Лента за търсене" text="Търси книга, новина, събитие…
recovered-empty catalogue: results (2446, 2475 ms)
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
app pids seen: ['5449', '9082', '12198', '15981']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=40 total=40
== Davey: 4
   davey events=4 max=917ms median=771ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## onboard

```
part onboard; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## OB1. Fresh install, no notification permission: page 1 (welcome, three feature rows)
fresh install; POST_NOTIFICATIONS: android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
ONB 01-page1: skip yes; text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" text="Официалното приложение на читалището — винаги актуално от сайта chyavorec.org." text="Новини и събития" text="Концерти, празници и срещи — с напомняне и добавяне в календара." text="Каталог и „Искам да прочета“" text="Търси книгите на библиотеката и си запази желаните — ще ти кажем, щом са на рафта." text="Читателска карта" text="Картата в телефона — с баркод, който се сканира на гишето." text="Напред" 
page 1 features: news yes, catalogue yes, card yes
## OB2. Page 2: text-size chips (apply at once), 'Разреши известията' → the system prompt (API 33)
ONB 02-page2: skip yes; text="Пропусни" text="Удобно за теб" text="Размерът на текста и известията — можеш да ги смениш и по-късно от „Настройки“." text="Размер на текста" text="Малък" text="Стандартен" text="Голям" text="Много голям" text="Известия" text="Сроковете на книгите, нови съобщения и събития." text="Разреши известията" text="Напред" 
chips: Малък[checked=false,selected=false,] Стандартен[checked=true,selected=false,] Голям[checked=false,selected=false,] Много голям[checked=false,selected=false,] 
ONB 03-page2-xlarge: skip yes; text="Пропусни" text="Удобно за теб" text="Размерът на текста и известията — можеш да ги смениш и по-късно от „Настройки“." text="Размер на текста" text="Малък" text="Стандартен" text="Голям" text="Много голям" text="Известия" text="Сроковете на книгите, нови съобщения и събития." text="Разреши известията" text="Напред" 
TEXT SIZE applied at once: title height 69.0 dp → 118.5 dp; chips: Стандартен[checked=false,selected=false,] Много голям[checked=true,selected=false,] 
after 'Разреши известията': system dialog SHOWN; text="Allow Chitalishte Yavorets to send you notifications?" text="Allow" text="Don’t allow" 
ONB 05-page2-after-allow: skip yes; text="Пропусни" text="Удобно за теб" text="Размерът на текста и известията — можеш да ги смениш и по-късно от „Настройки“." text="Размер на текста" text="Малък" text="Стандартен" text="Голям" text="Много голям" text="Известия" text="Сроковете на книгите, нови съобщения и събития." text="Известията са включени" text="Напред" 
status: text="Известията са включени"; android.permission.POST_NOTIFICATIONS: granted=true, flags=[ USER_SET|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
## OB3. Page 3: sign in / add a card; then the main screen does not ask again
ONB 06-page3: skip yes; text="Пропусни" text="Вход и читателска карта" text="Влез с данните си от библиотеката или добави читателската си карта. Можеш и по-късно — от „Моето“." text="Вход с читателски данни" text="Продължи без вход" 
MAIN 07-main-after-allow: system permission dialog no; in-app ask no; android.permission.POST_NOTIFICATIONS: granted=true, flags=[ USER_SET|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]; text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и дн
MAIN 08-main-restart: system permission dialog no; in-app ask no; android.permission.POST_NOTIFICATIONS: granted=true, flags=[ USER_SET|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]; text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и дн
## OB4. Deny in the onboarding: the main screen does not ask again
fresh install; POST_NOTIFICATIONS: android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
ONB 11-page2-after-deny: skip yes; text="Пропусни" text="Удобно за теб" text="Размерът на текста и известията — можеш да ги смениш и по-късно от „Настройки“." text="Размер на текста" text="Малък" text="Стандартен" text="Голям" text="Много голям" text="Известия" text="Сроковете на книгите, нови съобщения и събития." text="Засега без известия — включват се от „Настройки › Известия“." text="Напред" 
status: text="Засега без известия — включват се от „Настройки › Известия“."; android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SET|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
ONB 12-page3: skip yes; text="Пропусни" text="Вход и читателска карта" text="Влез с данните си от библиотеката или добави читателската си карта. Можеш и по-късно — от „Моето“." text="Вход с читателски данни" text="Продължи без вход" 
MAIN 13-main-after-deny: system permission dialog no; in-app ask no; android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SET|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]; text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и дн
MAIN 14-main-after-deny-restart: system permission dialog no; in-app ask no; android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SET|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]; text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Ден на народните будители" text="Почит към будителите — и към всички, които и дн
## OB5. 'Пропусни' on page 1 (the permission not answered)
fresh install; POST_NOTIFICATIONS: android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MAIN 20-main-after-skip: system permission dialog no; in-app ask SHOWN; android.permission.POST_NOTIFICATIONS: granted=false, flags=[ USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]; text="Известия от читалището" text="Искаш ли известия за сроковете на книгите, нови съобщения и събития? Можеш да ги промениш по всяко време от „Настройки › Изв
## COMPLETE
## logcat findings
app pids seen: ['5390', '7904', '8140', '8572', '8752']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=84 total=325
== Davey: 8
   davey events=8 max=2041ms median=854ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## rot

```
part rot; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15700 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3055 ms
## R1. Rotation (6 toggles each) on Home, Catalogue '12', a book, Want to read, Calendar
ROT home before: tab bar yes; search field yes
ROT home landscape: tab bar yes; search field yes
ROTATION home: pid 7235 -> 7235 (same); after: tab bar yes; search field yes
ROT catalog before: query '12' kept; results 108
ROT catalog landscape: query '12' kept; results 108
ROTATION catalog: pid 7235 -> 7235 (same); after: query '12' kept; results 108
ROT book before: book title kept; back button yes
ROT book landscape: book title kept; back button yes
ROTATION book: pid 7235 -> 7235 (same); after: book title kept; back button yes
ROT want before: list title yes; book listed
ROT want landscape: list title yes; book listed
ROTATION want: pid 7235 -> 7235 (same); after: list title yes; book listed
ROT calendar before: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
ROT calendar landscape: News[checked=false selected=false bounds=[508,243][820,369]] Calendar[checked=true selected=false bounds=[820,243][1135,369]] subscribe-action=visible
ROTATION calendar: pid 7235 -> 7235 (same); after: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible
GFX rotation     frames=  144  janky=143 (99.31%)  p50=200ms p90=450ms p95=500ms p99=750ms  slowUI=95 slowDraw=143 highInput=1
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
app pids seen: ['5290', '7235', '9428', '9708', '9860', '10360', '10568', '10813', '10999']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=41 total=246
== Davey: 4
   davey events=4 max=989ms median=834ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## tablet

```
part tablet; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
(dismissed '=По-късно': text="Нова версия 1.2.29" text="Излезе нова версия на приложението." text="Размер: 4,1 МБ" )
first-install catalogue results after 3279 ms
## TB1. Tablet: wm size 2560x1600, density 320 (1280x800 dp) — Catalogue → the book beside the list
window 2560x1600 px = 1280x800 dp; tab labels: home catalog my news more 
CATALOGUE + book: list yes; detail back/title yes; PANES left=38 right=24; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', '12']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']
## TB2. Back closes the detail first
after Back: list yes; detail back/title no; PANES left=37 right=2; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', 'Всичко']; right: ['12', 'Инв. номер']
## TB3. News → the article beside the list
NEWS + article: list yes; detail back/title yes; PANES left=16 right=5; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', 'Календар']; right: ['Проекти', 'СЪБИТИЕ  ·  16 октомври ', 'Фолклорно пътешествие из', 'Днес в НЧ „Васил Левски ', 'Аа']
after Back: list yes; detail back/title no; PANES left=13 right=1; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', 'Календар']; right: ['Проекти']
## TB4. Моето › Искам да прочета → the book beside the list
WANT + book: list yes; detail back/title yes; PANES left=9 right=24; left: ['Наличност към 28.10.2026', 'Плъзни книга настрани, з', 'Дългият любовта', 'Пенка Христова', 'Не е на рафта', '12 -те вятъра']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да про
after Back: list yes; detail back/title yes; PANES left=9 right=0; left: ['Наличност към 28.10.2026', 'Плъзни книга настрани, з', 'Дългият любовта', 'Пенка Христова', 'Не е на рафта', '12 -те вятъра']; right: []
## TB5. Rotation with a book open (density 320: portrait is 800 dp wide)
before rotation: list yes; detail back/title yes; PANES left=38 right=24; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', '12']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']
rotated (1600x2560 px = 800 dp wide): list yes; detail back/title yes; PANES left=15 right=9; left: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']; right: ['2011', 'български', 'книга', 'Език. Литература (УДК 8)', 'ЦБ/820(73)/Г 23', '399560']
rotated back: list yes; detail back/title yes; PANES left=38 right=24; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', '12']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']
## TB6. Rotation at density 240 (1706x1066 dp; portrait 1066 dp wide — two panes both ways)
density 240 landscape: list yes; detail back/title yes; PANES left=53 right=24; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', '12']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']
density 240 portrait (selection kept?): list yes; detail back/title yes; PANES left=89 right=26; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', '12']; right: ['Автор', 'ISBN', '12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026']
density 240 back: list yes; detail back/title yes; PANES left=53 right=24; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', '12']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']
## TB7. Narrow the window (phone size) with the book open → the detail goes full screen
narrowed: list yes; detail back/title yes; PANES left=9 right=15; left: ['Библиографски данни', 'Година', 'Език', 'Вид', 'Раздел', 'Сигнатура']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', '2011']
Back on the narrowed window: list yes; detail back/title no; PANES left=29 right=14; left: ['Всичко', 'Заглавие', '12 -те вятъра', 'Спомен за здрача', 'Веска Пенчева', 'Не е на рафта']; right: ['12', 'Автор', 'ISBN', 'Ключова дума', '108 резултата  ·  данни ', '1972 · графично издание ']
## TB8. Keyboard (tablet size): Ctrl+1..5, Ctrl+F, '/', Esc
Ctrl+2 → TAB selected: ['Каталог']
Ctrl+3 → TAB selected: ['Моето']
Ctrl+4 → TAB selected: ['Новини']
Ctrl+5 → TAB selected: ['Още']
Ctrl+1 → TAB selected: ['Начало']
Ctrl+F on Home: expanded=yes query-12=no hint=yes tabbar=no app-in-front=yes IME mInputShown=true
Esc: expanded=yes query-12=no hint=yes tabbar=no app-in-front=yes IME mInputShown=false
Esc again: expanded=no query-12=no hint=yes tabbar=yes app-in-front=yes IME mInputShown=false
'/' on Catalogue: focused field 0 IME mInputShown=true; focused="true"
book open: list yes; detail back/title yes; PANES left=38 right=24; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', '12']; right: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']
Esc on the book: list yes; detail back/title no; PANES left=37 right=2; left: ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', 'Всичко']; right: ['12', 'Инв. номер']
Ctrl+F on News: fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Всички" text="Книги" text="Новини" text="Събития" text="Страници" text="Какво търсиш?" 
## TB9. Phone in landscape: unchanged (single pane)
phone landscape 2400x1080: list yes; detail back/title yes; PANES left=7 right=1; left: ['12 -те вятъра', 'Гарфилд, Джудит и др.', 'Налична', 'Наличност към 28.10.2026', 'Искам да прочета', 'Библиографски данни']; right: ['2011']
## COMPLETE
## logcat findings
app pids seen: ['5234', '7606', '9203', '9953', '10905']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 13
   skipped-frames events=13 max=64 total=494
== Davey: 20
   davey events=20 max=1272ms median=944ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## unlock33

```
part unlock33; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2981 ms
## P0. Package (GitHub build): requested permissions
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.FOREGROUND_SERVICE_DATA_SYNC android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.USE_BIOMETRIC android.permission.WAKE_LOCK 
PERM android.permission.USE_BIOMETRIC: 1
PERM android.permission.USE_FINGERPRINT: 0
PERM android.permission.CAMERA: 0
PERM android.permission.FOREGROUND_SERVICE: 1
PERM android.permission.FOREGROUND_SERVICE_DATA_SYNC: 1
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.USE_BIOMETRIC' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.FOREGROUND_SERVICE' name='android.permission.FOREGROUND_SERVICE_DATA_SYNC' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
APK SystemForegroundService: foregroundServiceType(0x01010599)=0x00000001
## P1. No screen lock: Settings › Поверителност — the switch is disabled with an explanation
device secure:  lock: true
SWITCH (no lock): SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1682][1080,1979] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']
explanation 'Не може да се включи…': SHOWN; group title 'Поверителност' yes
tap on the disabled switch: SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1682][1080,1979] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']; prompt absent; fg org.chyavorec.app/org.chyavorec.app.MainActivity; PROMPT packages=['org.chyavorec.app'] password-fields=0 texts=['Новата версия е подписана с различен ключ и не може да замени инстал
## P2. A self-entered card (before the switch is on)
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
card screen: card shown
## P3. Device PIN 1234; the switch → the system prompt → PIN
set-pin: Pin set to '1234'
SWITCH (PIN set): SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,1610][1080,1963] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
SWITCH tapped → prompt SHOWN; fg BiometricPrompt; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH after the PIN: SWITCH class=android.view.View checkable=true checked=true enabled=true clickable=true bounds=[0,1868][1080,2221] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']; fg org.chyavorec.app/org.chyavorec.app.MainActivity
## P4. New process: Моето › Читателска карта → prompt; Back (cancel) → Моето; PIN → the card
GATE 20-card-prompt: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 21-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 22-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P5. Background 20 s (< 60 s): no prompt; background 70 s (> 60 s): prompt
GATE 30-after-20s-background: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
GATE 31-after-70s-background: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 32-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P6. Screen off and on (keyguard with the PIN): prompt again
keyguard: prompt absent; fg NotificationShade; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Configure physical keyboard', 'Alerted', 'Tap to select language and layout', 'Expand', '8:05', 'Sun, Nov 1', 'T-Mobile', 'Wifi signal full.', 'Phone signal
GATE 40-after-screen-off: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
biometric/keyguard log after screen off:    481   481 D BiometricService: Sensor ID(0), oemStrength: 15, updatedStrength: 15, modality 2, state: 0, cookie: 0, sensorId 0, hasEnrollments: false cannot participate in Keystore~   750   750 E KeyguardViewMediator: mHideAnimationFinishedRunnable#run~   481   481 D BiometricService/PreAuthInfo: Package: org.chyavorec.app Sensor ID: 0 Modality: 2 Status: 7~   481   481 D BiometricService/PreAuthInfo: getCanAuthenticateInternal Modality: 1 AuthenticatorStatus: 1~   481   481 D BiometricService: handleAuthenticate: modality(1), status(0), preAuthInfo: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, In~   481   481 D BiometricService: Creating authSession with authRequest: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, Ineligible:{ID(0), o~   481   481 D BiometricService/AuthSession: Creating AuthSession with: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, Ineligible:{ID(0), o~   750   750 D AuthController: showAuthenticationDialog, authenticators: 32768, sensorIds: , credentialAllowed: true, requireConfirmation: false, operationId: 0, requestId: 5, multiS~   750   750 D AuthController: userId: 0 savedState: null mCurrentDialog: null newDialog: com.android.systemui.biometrics.AuthContainerView{fa9197f VFE...... .F....I. 0,0-0,0}~   481   836 D CoreBackPreview: Window{e3356d3 u0 BiometricPrompt}: Setting back callback OnBackInvokedCallbackInfo{mCallback=android.window.IOnBackInvokedCallback$Stub$Proxy@aba109,~   750   750 W AuthController: Task stack changed, current client: org.chyavorec.app~   750   750 D KeyguardViewMediator: onKeyguardExitRemoteAnimationFinished#hideKeyguardViewAfterRemoteAnimation~   481   481 D BiometricService: Sensor ID(0), oemStrength: 15, updatedStrength: 15, modality 2, state: 0, cookie: 0, sensorId 0, hasEnrollments: false cannot participate in Keystore~   750   750 D KeyguardViewMediator: onKeyguardExitRemoteAnimationFinished#hideKeyguardViewAfterRemoteAnimation~
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 41-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.MainActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P7. Quick Settings tile → the card: the gate
sysui_qs_tiles before: internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,dbg:mem,ott
add-tile org.chyavorec.app/.tile.CardTileService: ''
sysui_qs_tiles after: custom(org.chyavorec.app/.tile.CardTileService),internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,dbg:mem,ott
QS gate: tile @ NOT FOUND (page 4):  texts: 
QS gate: ours in sysui_qs_tiles: 1
click-tile: 
GATE 50-tile-tap: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg BiometricPrompt; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 51-tile-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
QS gate2: tile @ 285 630 (page 1): MATCH class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[163,579][418,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none  texts: content-desc="Читателска карта" text="Читателска карта" text="Покажи на гишето" 
QS gate2: ours in sysui_qs_tiles: 1
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 52-tile-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg org.chyavorec.app/org.chyavorec.app.OpenLink; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P8. Turning the switch off also asks for the PIN
switch OFF tapped → prompt SHOWN; fg BiometricPrompt; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH now: SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,1354][1080,1707] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
clear PIN: Lock credential cleared
## COMPLETE
## logcat findings
app pids seen: ['5654', '8588', '9994']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=47 total=124
== Davey: 5
   davey events=5 max=1612ms median=952ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## unlock34

```
part unlock34; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104414 bytes
host: 4 cpu, 15983 MB RAM
android 14 sdk 34; Physical size: 1080x2400; Physical density: 420; adb uid 2000
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2772 ms
## P0. Package (GitHub build): requested permissions
requested permissions: android.permission.ACCESS_NETWORK_STATE android.permission.FOREGROUND_SERVICE android.permission.FOREGROUND_SERVICE_DATA_SYNC android.permission.INTERNET android.permission.POST_NOTIFICATIONS android.permission.RECEIVE_BOOT_COMPLETED android.permission.REQUEST_INSTALL_PACKAGES android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION android.permission.USE_BIOMETRIC android.permission.WAKE_LOCK 
PERM android.permission.USE_BIOMETRIC: 1
PERM android.permission.USE_FINGERPRINT: 0
PERM android.permission.CAMERA: 0
PERM android.permission.FOREGROUND_SERVICE: 1
PERM android.permission.FOREGROUND_SERVICE_DATA_SYNC: 1
APK uses-permission (aapt2): name='android.permission.INTERNET' name='android.permission.ACCESS_NETWORK_STATE' name='android.permission.POST_NOTIFICATIONS' name='android.permission.USE_BIOMETRIC' name='android.permission.REQUEST_INSTALL_PACKAGES' name='android.permission.UPDATE_PACKAGES_WITHOUT_USER_ACTION' name='android.permission.FOREGROUND_SERVICE' name='android.permission.FOREGROUND_SERVICE_DATA_SYNC' name='android.permission.WAKE_LOCK' name='android.permission.RECEIVE_BOOT_COMPLETED' name='org.chyavorec.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION' 
APK SystemForegroundService: foregroundServiceType(0x01010599)=0x00000001
## P1. No screen lock: Settings › Поверителност — the switch is disabled with an explanation
device secure:  lock: true
SWITCH (no lock): SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1886][1080,2183] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']
explanation 'Не може да се включи…': SHOWN; group title 'Поверителност' yes
tap on the disabled switch: SWITCH class=android.view.View checkable=true checked=false enabled=false clickable=true bounds=[0,1886][1080,2183] texts=['Искай отключване за личните екрани', 'Не може да се включи: телефонът няма заключване на екрана. Задай ПИН, фигура или парола в настройките на телефона.']; prompt absent; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; PROMPT packages=['org.chyavorec.app'] password-fields=0 texts=['Провери за нова вер
## P2. A self-entered card (before the switch is on)
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
card screen: card shown
## P3. Device PIN 1234; the switch → the system prompt → PIN
set-pin: Pin set to '1234'
SWITCH (PIN set): SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,2285][1080,2400] texts=['Искай отключване за личните екрани']
SWITCH tapped → prompt SHOWN; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH after the PIN: SWITCH 'Искай отключване за личните екрани': not on screen; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
## P4. New process: Моето › Читателска карта → prompt; Back (cancel) → Моето; PIN → the card
GATE 20-card-prompt: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 21-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 22-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P5. Background 20 s (< 60 s): no prompt; background 70 s (> 60 s): prompt
GATE 30-after-20s-background: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
GATE 31-after-70s-background: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 32-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P6. Screen off and on (keyguard with the PIN): prompt again
keyguard: prompt absent; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['08\n04', 'Sun, Nov 1', 'T-Mobile', 'Wifi signal full.', 'Phone signal full.'
GATE 40-after-screen-off: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
biometric/keyguard log after screen off:    524   524 D BiometricService: Sensor ID(4), oemStrength: 15, updatedStrength: 15, modality 8, state: 0, cookie: 0, sensorId 4, hasEnrollments: false cannot participate in Keystore~   524   524 D BiometricService: Sensor ID(0), oemStrength: 15, updatedStrength: 15, modality 2, state: 0, cookie: 0, sensorId 0, hasEnrollments: false cannot participate in Keystore~   524   748 D BiometricService: resetLockout(userId=0, hat=present)~   837   837 E KeyguardViewMediator: mHideAnimationFinishedRunnable#run~   524   524 D BiometricService/PreAuthInfo: Package: org.chyavorec.app Sensor ID: 4 Modality: 8 Status: 7~   524   524 D BiometricService/PreAuthInfo: Package: org.chyavorec.app Sensor ID: 0 Modality: 2 Status: 7~   524   524 D BiometricService/PreAuthInfo: getCanAuthenticateInternal Modality: 1 AuthenticatorStatus: 1~   524   524 D BiometricService: handleAuthenticate: modality(1), status(0), preAuthInfo: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, In~   524   524 D BiometricService: Creating authSession with authRequest: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, Ineligible:{ID(4), o~   524   524 D BiometricService/AuthSession: Creating AuthSession with: BiometricRequested: true, StrengthRequested: 255, CredentialRequested: true, Eligible:{}, Ineligible:{ID(4), o~   837   837 D AuthController: showAuthenticationDialog, authenticators: 32768, sensorIds: , credentialAllowed: true, requireConfirmation: false, operationId: 0, requestId: 5~   837   837 D AuthController: userId: 0 savedState: null mCurrentDialog: null newDialog: com.android.systemui.biometrics.AuthContainerView{fd7885d VFE...... .F....I. 0,0-0,0}~   524  2587 D CoreBackPreview: Window{d1ce8b4 u0 BiometricPrompt}: Setting back callback OnBackInvokedCallbackInfo{mCallback=android.window.IOnBackInvokedCallback$Stub$Proxy@73cdf23~   837   837 W AuthController: Task stack changed, current client: org.chyavorec.app~
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 41-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P7. Quick Settings tile → the card: the gate
sysui_qs_tiles before: internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,custom(com.google.android.permissioncontroller/com.android.permissioncontroller.permission.service.v33.SafetyCenterQsTileService),dbg:mem,ott
add-tile org.chyavorec.app/.tile.CardTileService: ''
sysui_qs_tiles after: custom(org.chyavorec.app/.tile.CardTileService),internet,bt,flashlight,dnd,alarm,airplane,controls,rotation,qr_code_scanner,battery,cast,screenrecord,custom(com.google.android.gms/.nearby.sharing.SharingTileService),mictoggle,cameratoggle,custom(com.google.android.permissioncontroller/com.android.permissioncontroller.permission.service.v33.SafetyCenterQsTileService),dbg:mem,ott
QS gate: tile @ NOT FOUND (page 4):  texts: 
QS gate: ours in sysui_qs_tiles: 1
click-tile: 
GATE 50-tile-tap: prompt SHOWN; locked view no; card no; TAB selected: none; back arrow no; app in front NO; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; content-desc="Tap to cancel authentication" text="Отключи личните екрани" text="Читателска карта, заемания, история и съобщения" 
cancel: Back #1 → prompt still; IME mInputShown=false
cancel: Back #2 → prompt gone; IME mInputShown=false
GATE 51-tile-after-cancel: prompt absent; locked view no; card no; TAB selected: ['Моето']; back arrow no; app in front yes; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="Здравей!" text="Новините, събитията, каталогът и информацията за читалището са достъпни и без вход." text="Вход" text="БИБЛИОТЕКА" text="Читателска карта" text="Въведена от теб карта" 
QS gate2: tile @ 285 630 (page 1): MATCH class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none MATCH class="android.widget.TextView" text="Читателска карта" content-desc="" checkable="false" checked="false" clickable="false" bounds="[163,579][418,630]" | ACTIONABLE-ANCESTOR class="android.widget.LinearLayout" text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" checkable="false" checked="false" clickable="true" bounds="[42,525][529,735]" | ACTIONABLE-CHILD none  texts: text="Читателска карта, Покажи на гишето" content-desc="Читателска карта" text="Читателска карта" text="Покажи на гишето" 
QS gate2: ours in sysui_qs_tiles: 1
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Читателска карта, заемания, история и съобщения']
GATE 52-tile-after-pin: prompt absent; locked view no; card SHOWN; TAB selected: none; back arrow yes; app in front yes; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## P8. Turning the switch off also asks for the PIN
switch OFF tapped → prompt SHOWN; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel aut
PIN screen: PROMPT packages=['com.android.systemui'] password-fields=1 texts=['Tap to cancel authentication', 'Отключи личните екрани', 'Искай отключване за личните екрани']
SWITCH now: SWITCH class=android.view.View checkable=true checked=false enabled=true clickable=true bounds=[0,2173][1080,2337] texts=['Искай отключване за личните екрани', 'Картата, заеманията, историята и личните съобщения се отварят след пръстов отпечатък, лице или ПИН-а на телефона.']
clear PIN: Lock credential cleared
## COMPLETE
## logcat findings
app pids seen: ['5340', '7329', '8411']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=53 total=53
== Davey: 2
   davey events=2 max=995ms median=932ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## widgetlang

```
part widgetlang; APK: 1.2.30 bf10741 (Какво ново: версия 1.2.30); versionCode 1488543; x86_64 libs: 2 size 4505009 bytes
base APK: 1.2.29 b84650f (Профил за бърз старт: генериран на емулатор от 34b56df); versionCode 1488542; x86_64 libs: 2; update manifest: no-update stub size 4104418 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1488543 minSdk=26 targetSdk=37  versionName=1.2.30 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2863 ms
## WL1. The widget (system en-US, app bg-BG)
system: config: mcc310-mnc260-en-rUS-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33; app: [bg-BG]
WIDGET lang long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET lang picker: our row @ 437 983; size label @ 540 1891; texts: Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets|Chitalishte Yavorets widget|4 × 2|
WIDGET lang after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET lang: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET-LANG 01-appBG: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 01-appBG: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET event column 340x90dp clickable=true label='Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов '
WIDGET TARGETS: 4 clickable, <48dp: 0; background('Читалище Яворец Сканирай') 368x223dp; widget_scan('Сканирай') 75x48dp; widget_catalog('Каталог') 66x48dp; widget_event_column('Предстоящи събития Ден н') 340x90dp
## WL2. App language → English from the system per-app setting (cmd locale), app not opened (process killed, not force-stopped)
app process before: ''
WIDGET-LANG 10-appEN-2s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 10-appEN-2s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 10-appEN-5s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 10-appEN-5s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 10-appEN-10s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 10-appEN-10s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 10-appEN-20s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 10-appEN-20s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 10-appEN-40s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 10-appEN-40s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
app process after: '7912'
receiver/broadcast lines: 11-01 20:38:49.783   483   578 I ActivityManager: Start proc 7912:org.chyavorec.app/u0a174 for broadcast {org.chyavorec.app/org.chyavorec.app.widget.Chitalishte~11-01 20:38:49.789  7912  7912 I g.chyavorec.app: Using CollectorTypeCC GC.~11-01 20:38:49.790  7912  7912 W g.chyavorec.app: Unexpected CPU variant for x86: x86_64.~11-01 20:38:49.790  7912  7912 W g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default~
## WL3. App language → Български (cmd locale), app not opened
WIDGET-LANG 20-appBG-2s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 20-appBG-2s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 20-appBG-5s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 20-appBG-5s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 20-appBG-10s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 20-appBG-10s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 20-appBG-20s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 20-appBG-20s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 20-appBG-40s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 20-appBG-40s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
app process after: '8209'
## WL4. Device language (app follows the system): en-US → bg-BG → en-US, without a reboot
app locales cleared: Locales for org.chyavorec.app for user 0 are []
WIDGET-LANG 30-follow-system-en: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 30-follow-system-en: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33
WIDGET-LANG 31-deviceBG-2s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 31-deviceBG-2s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 31-deviceBG-5s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 31-deviceBG-5s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 31-deviceBG-10s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 31-deviceBG-10s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 31-deviceBG-20s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 31-deviceBG-20s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
WIDGET-LANG 31-deviceBG-40s: Читалище Яворец|Сканирай|Каталог|Предстоящи събития|Ден на народните будители|1 ноември 2026|Михаил В. Ломоносов|8 ноември 2026|
WIDGET-LANG 31-deviceBG-40s: LANG cyrillic=98 latin=0 -> BG | Читалище Яворец Сканирай Каталог Предстоящи събития Ден на народните будители 1 ноември 2026 Михаил В. Ломоносов 8 ноември 2026 
app process after: '8761'
DEVICE locale -> en-US: data="LOCALE ok en-US"; config now: config: mcc310-mnc260-en-rUS-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2209x1080-v33
WIDGET-LANG 32-deviceEN-2s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 32-deviceEN-2s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 32-deviceEN-5s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 32-deviceEN-5s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 32-deviceEN-10s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 32-deviceEN-10s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 32-deviceEN-20s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 32-deviceEN-20s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
WIDGET-LANG 32-deviceEN-40s: Chitalishte Yavorets|Scan|Catalogue|Upcoming events|Ден на народните будители|November 1, 2026|Михаил В. Ломоносов|November 8, 2026|
WIDGET-LANG 32-deviceEN-40s: LANG cyrillic=38 latin=62 -> EN | Chitalishte Yavorets Scan Catalogue Upcoming events Ден на народните будители November 1, 2026 Михаил В. Ломоносов November 8, 2026 
app process after: '9706'
## COMPLETE
## logcat findings
app pids seen: ['5752', '7912', '8209', '8761', '9706']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=44 total=44
== Davey: 2
   davey events=2 max=1363ms median=1048ms
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

### a11y16/shots/a11y16

- [a11y16/shots/a11y16/01-home-top.png](a11y16/shots/a11y16/01-home-top.png)
- [a11y16/shots/a11y16/02-home-unread.png](a11y16/shots/a11y16/02-home-unread.png)
- [a11y16/shots/a11y16/03-messages.png](a11y16/shots/a11y16/03-messages.png)
- [a11y16/shots/a11y16/10-tx-0s.png](a11y16/shots/a11y16/10-tx-0s.png)
- [a11y16/shots/a11y16/11-tx-6s.png](a11y16/shots/a11y16/11-tx-6s.png)
- [a11y16/shots/a11y16/12-tx-13s.png](a11y16/shots/a11y16/12-tx-13s.png)
- [a11y16/shots/a11y16/13-ctl-0s.png](a11y16/shots/a11y16/13-ctl-0s.png)
- [a11y16/shots/a11y16/14-ctl-6s.png](a11y16/shots/a11y16/14-ctl-6s.png)
- [a11y16/shots/a11y16/15-ctl-13s.png](a11y16/shots/a11y16/15-ctl-13s.png)
- [a11y16/shots/a11y16/16-talkback-0s.png](a11y16/shots/a11y16/16-talkback-0s.png)
- [a11y16/shots/a11y16/17-talkback-6s.png](a11y16/shots/a11y16/17-talkback-6s.png)
- [a11y16/shots/a11y16/18-talkback-13s.png](a11y16/shots/a11y16/18-talkback-13s.png)
- [a11y16/shots/a11y16/20-news-offline.png](a11y16/shots/a11y16/20-news-offline.png)
- [a11y16/shots/a11y16/20-offline.png](a11y16/shots/a11y16/20-offline.png)
- [a11y16/shots/a11y16/21-news-offline.png](a11y16/shots/a11y16/21-news-offline.png)
- [a11y16/shots/a11y16/21-offline.png](a11y16/shots/a11y16/21-offline.png)
- [a11y16/shots/a11y16/30-font130-home.png](a11y16/shots/a11y16/30-font130-home.png)

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

### dialogs/shots/debug

- [dialogs/shots/debug/1-ready.png](dialogs/shots/debug/1-ready.png)
- [dialogs/shots/debug/2-ready.png](dialogs/shots/debug/2-ready.png)
- [dialogs/shots/debug/3-ready.png](dialogs/shots/debug/3-ready.png)
- [dialogs/shots/debug/4-ready.png](dialogs/shots/debug/4-ready.png)
- [dialogs/shots/debug/5-ready.png](dialogs/shots/debug/5-ready.png)
- [dialogs/shots/debug/6-ready.png](dialogs/shots/debug/6-ready.png)
- [dialogs/shots/debug/7-tab-more.png](dialogs/shots/debug/7-tab-more.png)
- [dialogs/shots/debug/8-settings.png](dialogs/shots/debug/8-settings.png)

### dialogs/shots/dialogs

- [dialogs/shots/dialogs/std-00-settings-screen.png](dialogs/shots/dialogs/std-00-settings-screen.png)
- [dialogs/shots/dialogs/std-01-text-size.png](dialogs/shots/dialogs/std-01-text-size.png)
- [dialogs/shots/dialogs/std-02-clear-cache.png](dialogs/shots/dialogs/std-02-clear-cache.png)
- [dialogs/shots/dialogs/std-03-sort-menu.png](dialogs/shots/dialogs/std-03-sort-menu.png)
- [dialogs/shots/dialogs/std-04-filters.png](dialogs/shots/dialogs/std-04-filters.png)
- [dialogs/shots/dialogs/std-04b-filters-swiped.png](dialogs/shots/dialogs/std-04b-filters-swiped.png)
- [dialogs/shots/dialogs/std-05-whats-new.png](dialogs/shots/dialogs/std-05-whats-new.png)
- [dialogs/shots/dialogs/std-05b-whats-new-swiped.png](dialogs/shots/dialogs/std-05b-whats-new-swiped.png)
- [dialogs/shots/dialogs/xl-00-settings-screen.png](dialogs/shots/dialogs/xl-00-settings-screen.png)
- [dialogs/shots/dialogs/xl-01-text-size.png](dialogs/shots/dialogs/xl-01-text-size.png)
- [dialogs/shots/dialogs/xl-02-clear-cache.png](dialogs/shots/dialogs/xl-02-clear-cache.png)
- [dialogs/shots/dialogs/xl-03-sort-menu.png](dialogs/shots/dialogs/xl-03-sort-menu.png)
- [dialogs/shots/dialogs/xl-04-filters.png](dialogs/shots/dialogs/xl-04-filters.png)
- [dialogs/shots/dialogs/xl-04b-filters-swiped.png](dialogs/shots/dialogs/xl-04b-filters-swiped.png)
- [dialogs/shots/dialogs/xl-05-whats-new.png](dialogs/shots/dialogs/xl-05-whats-new.png)
- [dialogs/shots/dialogs/xl-05b-whats-new-swiped.png](dialogs/shots/dialogs/xl-05b-whats-new-swiped.png)
- [dialogs/shots/dialogs/xl130-00-settings-screen.png](dialogs/shots/dialogs/xl130-00-settings-screen.png)
- [dialogs/shots/dialogs/xl130-01-text-size.png](dialogs/shots/dialogs/xl130-01-text-size.png)
- [dialogs/shots/dialogs/xl130-02-clear-cache.png](dialogs/shots/dialogs/xl130-02-clear-cache.png)
- [dialogs/shots/dialogs/xl130-03-sort-menu.png](dialogs/shots/dialogs/xl130-03-sort-menu.png)
- [dialogs/shots/dialogs/xl130-04-filters.png](dialogs/shots/dialogs/xl130-04-filters.png)
- [dialogs/shots/dialogs/xl130-04b-filters-swiped.png](dialogs/shots/dialogs/xl130-04b-filters-swiped.png)
- [dialogs/shots/dialogs/xl130-05-whats-new.png](dialogs/shots/dialogs/xl130-05-whats-new.png)
- [dialogs/shots/dialogs/xl130-05b-whats-new-swiped.png](dialogs/shots/dialogs/xl130-05b-whats-new-swiped.png)

### emoji26/shots/emoji

- [emoji26/shots/emoji/1-news.png](emoji26/shots/emoji/1-news.png)
- [emoji26/shots/emoji/2-news.png](emoji26/shots/emoji/2-news.png)
- [emoji26/shots/emoji/4-news.png](emoji26/shots/emoji/4-news.png)
- [emoji26/shots/emoji/9-events-list.png](emoji26/shots/emoji/9-events-list.png)

### emoji28/shots/emoji

- [emoji28/shots/emoji/1-news.png](emoji28/shots/emoji/1-news.png)
- [emoji28/shots/emoji/2-news.png](emoji28/shots/emoji/2-news.png)
- [emoji28/shots/emoji/3-news.png](emoji28/shots/emoji/3-news.png)
- [emoji28/shots/emoji/4-news.png](emoji28/shots/emoji/4-news.png)
- [emoji28/shots/emoji/5-news.png](emoji28/shots/emoji/5-news.png)
- [emoji28/shots/emoji/9-events-list.png](emoji28/shots/emoji/9-events-list.png)

### emoji33/shots/emoji

- [emoji33/shots/emoji/1-news.png](emoji33/shots/emoji/1-news.png)
- [emoji33/shots/emoji/2-news.png](emoji33/shots/emoji/2-news.png)
- [emoji33/shots/emoji/4-news.png](emoji33/shots/emoji/4-news.png)
- [emoji33/shots/emoji/9-events-list.png](emoji33/shots/emoji/9-events-list.png)

### events/shots/emoji

- [events/shots/emoji/1-news.png](events/shots/emoji/1-news.png)
- [events/shots/emoji/9-events-list.png](events/shots/emoji/9-events-list.png)

### events/shots/events

- [events/shots/events/01-list.png](events/shots/events/01-list.png)
- [events/shots/events/02-list-scrolled.png](events/shots/events/02-list-scrolled.png)
- [events/shots/events/10-detail-with-place.png](events/shots/events/10-detail-with-place.png)
- [events/shots/events/11-maps.png](events/shots/events/11-maps.png)

### events/shots/notif

- [events/shots/notif/20-reminder-0-shade.png](events/shots/notif/20-reminder-0-shade.png)
- [events/shots/notif/20-reminder-1-expanded.png](events/shots/notif/20-reminder-1-expanded.png)
- [events/shots/notif/21-reminder-route.png](events/shots/notif/21-reminder-route.png)

### login/shots/login

- [login/shots/login/20-demo-intent.png](login/shots/login/20-demo-intent.png)
- [login/shots/login/20-demo-resolver.png](login/shots/login/20-demo-resolver.png)

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

### onboard/shots/onboard

- [onboard/shots/onboard/01-page1.png](onboard/shots/onboard/01-page1.png)
- [onboard/shots/onboard/02-page2.png](onboard/shots/onboard/02-page2.png)
- [onboard/shots/onboard/03-page2-xlarge.png](onboard/shots/onboard/03-page2-xlarge.png)
- [onboard/shots/onboard/04-system-prompt.png](onboard/shots/onboard/04-system-prompt.png)
- [onboard/shots/onboard/05-page2-after-allow.png](onboard/shots/onboard/05-page2-after-allow.png)
- [onboard/shots/onboard/06-page3.png](onboard/shots/onboard/06-page3.png)
- [onboard/shots/onboard/07-main-after-allow.png](onboard/shots/onboard/07-main-after-allow.png)
- [onboard/shots/onboard/08-main-restart.png](onboard/shots/onboard/08-main-restart.png)
- [onboard/shots/onboard/10-system-prompt.png](onboard/shots/onboard/10-system-prompt.png)
- [onboard/shots/onboard/11-page2-after-deny.png](onboard/shots/onboard/11-page2-after-deny.png)
- [onboard/shots/onboard/12-page3.png](onboard/shots/onboard/12-page3.png)
- [onboard/shots/onboard/13-main-after-deny.png](onboard/shots/onboard/13-main-after-deny.png)
- [onboard/shots/onboard/14-main-after-deny-restart.png](onboard/shots/onboard/14-main-after-deny-restart.png)
- [onboard/shots/onboard/20-main-after-skip.png](onboard/shots/onboard/20-main-after-skip.png)

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

### tablet/shots/tablet

- [tablet/shots/tablet/00-home.png](tablet/shots/tablet/00-home.png)
- [tablet/shots/tablet/01-catalog-book.png](tablet/shots/tablet/01-catalog-book.png)
- [tablet/shots/tablet/02-after-back.png](tablet/shots/tablet/02-after-back.png)
- [tablet/shots/tablet/03-news-article.png](tablet/shots/tablet/03-news-article.png)
- [tablet/shots/tablet/04-want-book.png](tablet/shots/tablet/04-want-book.png)
- [tablet/shots/tablet/05-rotated.png](tablet/shots/tablet/05-rotated.png)
- [tablet/shots/tablet/06-rotated-back.png](tablet/shots/tablet/06-rotated-back.png)
- [tablet/shots/tablet/07-d240-land.png](tablet/shots/tablet/07-d240-land.png)
- [tablet/shots/tablet/08-d240-portrait.png](tablet/shots/tablet/08-d240-portrait.png)
- [tablet/shots/tablet/09-narrowed.png](tablet/shots/tablet/09-narrowed.png)
- [tablet/shots/tablet/10-ctrl-f-home.png](tablet/shots/tablet/10-ctrl-f-home.png)
- [tablet/shots/tablet/11-slash-catalog.png](tablet/shots/tablet/11-slash-catalog.png)
- [tablet/shots/tablet/12-ctrl-f-news.png](tablet/shots/tablet/12-ctrl-f-news.png)
- [tablet/shots/tablet/13-phone-landscape-book.png](tablet/shots/tablet/13-phone-landscape-book.png)

### unlock33/shots/unlock

- [unlock33/shots/unlock/01-no-screen-lock.png](unlock33/shots/unlock/01-no-screen-lock.png)
- [unlock33/shots/unlock/10-with-screen-lock.png](unlock33/shots/unlock/10-with-screen-lock.png)
- [unlock33/shots/unlock/12-after-pin.png](unlock33/shots/unlock/12-after-pin.png)
- [unlock33/shots/unlock/39-keyguard.png](unlock33/shots/unlock/39-keyguard.png)

### unlock34/shots/unlock

- [unlock34/shots/unlock/01-no-screen-lock.png](unlock34/shots/unlock/01-no-screen-lock.png)
- [unlock34/shots/unlock/10-with-screen-lock.png](unlock34/shots/unlock/10-with-screen-lock.png)
- [unlock34/shots/unlock/12-after-pin.png](unlock34/shots/unlock/12-after-pin.png)
- [unlock34/shots/unlock/39-keyguard.png](unlock34/shots/unlock/39-keyguard.png)

### update/shots/update

- [update/shots/update/10-v129-dialog.png](update/shots/update/10-v129-dialog.png)
- [update/shots/update/12-whats-new-1.2.30.png](update/shots/update/12-whats-new-1.2.30.png)
- [update/shots/update/13-whats-new-scrolled.png](update/shots/update/13-whats-new-scrolled.png)
- [update/shots/update/20-dialog.png](update/shots/update/20-dialog.png)
- [update/shots/update/21-dialog-downloading.png](update/shots/update/21-dialog-downloading.png)
- [update/shots/update/22-shade-downloading.png](update/shots/update/22-shade-downloading.png)
- [update/shots/update/30-dialog-again.png](update/shots/update/30-dialog-again.png)
- [update/shots/update/30b-dialog-after-second-tap.png](update/shots/update/30b-dialog-after-second-tap.png)
- [update/shots/update/31-shade-ready.png](update/shots/update/31-shade-ready.png)
- [update/shots/update/32-after-tap.png](update/shots/update/32-after-tap.png)
- [update/shots/update/34-whats-new-after-b.png](update/shots/update/34-whats-new-after-b.png)

### widgetlang/shots/widget

- [widgetlang/shots/widget/lang-00-home.png](widgetlang/shots/widget/lang-00-home.png)
- [widgetlang/shots/widget/lang-01-appBG.png](widgetlang/shots/widget/lang-01-appBG.png)
- [widgetlang/shots/widget/lang-01-longpress.png](widgetlang/shots/widget/lang-01-longpress.png)
- [widgetlang/shots/widget/lang-03-list.png](widgetlang/shots/widget/lang-03-list.png)
- [widgetlang/shots/widget/lang-04-app.png](widgetlang/shots/widget/lang-04-app.png)
- [widgetlang/shots/widget/lang-05-dropped.png](widgetlang/shots/widget/lang-05-dropped.png)
- [widgetlang/shots/widget/lang-10-appEN-10s.png](widgetlang/shots/widget/lang-10-appEN-10s.png)
- [widgetlang/shots/widget/lang-10-appEN-20s.png](widgetlang/shots/widget/lang-10-appEN-20s.png)
- [widgetlang/shots/widget/lang-10-appEN-2s.png](widgetlang/shots/widget/lang-10-appEN-2s.png)
- [widgetlang/shots/widget/lang-10-appEN-40s.png](widgetlang/shots/widget/lang-10-appEN-40s.png)
- [widgetlang/shots/widget/lang-10-appEN-5s.png](widgetlang/shots/widget/lang-10-appEN-5s.png)
- [widgetlang/shots/widget/lang-20-appBG-10s.png](widgetlang/shots/widget/lang-20-appBG-10s.png)
- [widgetlang/shots/widget/lang-20-appBG-20s.png](widgetlang/shots/widget/lang-20-appBG-20s.png)
- [widgetlang/shots/widget/lang-20-appBG-2s.png](widgetlang/shots/widget/lang-20-appBG-2s.png)
- [widgetlang/shots/widget/lang-20-appBG-40s.png](widgetlang/shots/widget/lang-20-appBG-40s.png)
- [widgetlang/shots/widget/lang-20-appBG-5s.png](widgetlang/shots/widget/lang-20-appBG-5s.png)
- [widgetlang/shots/widget/lang-30-follow-system-en.png](widgetlang/shots/widget/lang-30-follow-system-en.png)
- [widgetlang/shots/widget/lang-31-deviceBG-10s.png](widgetlang/shots/widget/lang-31-deviceBG-10s.png)
- [widgetlang/shots/widget/lang-31-deviceBG-20s.png](widgetlang/shots/widget/lang-31-deviceBG-20s.png)
- [widgetlang/shots/widget/lang-31-deviceBG-2s.png](widgetlang/shots/widget/lang-31-deviceBG-2s.png)
- [widgetlang/shots/widget/lang-31-deviceBG-40s.png](widgetlang/shots/widget/lang-31-deviceBG-40s.png)
- [widgetlang/shots/widget/lang-31-deviceBG-5s.png](widgetlang/shots/widget/lang-31-deviceBG-5s.png)
- [widgetlang/shots/widget/lang-32-deviceEN-10s.png](widgetlang/shots/widget/lang-32-deviceEN-10s.png)
- [widgetlang/shots/widget/lang-32-deviceEN-20s.png](widgetlang/shots/widget/lang-32-deviceEN-20s.png)
- [widgetlang/shots/widget/lang-32-deviceEN-2s.png](widgetlang/shots/widget/lang-32-deviceEN-2s.png)
- [widgetlang/shots/widget/lang-32-deviceEN-40s.png](widgetlang/shots/widget/lang-32-deviceEN-40s.png)
- [widgetlang/shots/widget/lang-32-deviceEN-5s.png](widgetlang/shots/widget/lang-32-deviceEN-5s.png)
- [widgetlang/shots/widget/lang-40-dark.png](widgetlang/shots/widget/lang-40-dark.png)

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
- [a11y16/a11y-events-all.txt](a11y16/a11y-events-all.txt)
- [a11y16/a11ytree-home1.txt](a11y16/a11ytree-home1.txt)
- [a11y16/a11ytree-home2.txt](a11y16/a11ytree-home2.txt)
- [a11y16/dumpsys-accessibility.txt](a11y16/dumpsys-accessibility.txt)
- [a11y16/home.xml](a11y16/home.xml)
- [a11y16/host-after-run.txt](a11y16/host-after-run.txt)
- [a11y16/job-status.txt](a11y16/job-status.txt)
- [a11y16/logcat-all.txt](a11y16/logcat-all.txt)
- [a11y16/logcat-dump.txt](a11y16/logcat-dump.txt)
- [a11y16/logcat-findings.txt](a11y16/logcat-findings.txt)
- [a11y16/logcat-live.txt](a11y16/logcat-live.txt)
- [a11y16/pane-events.txt](a11y16/pane-events.txt)
- [a11y16/rec/reduced0-offline/rec.mp4](a11y16/rec/reduced0-offline/rec.mp4)
- [a11y16/rec/reduced0-online/rec.mp4](a11y16/rec/reduced0-online/rec.mp4)
- [a11y16/rec/reduced1-offline/rec.mp4](a11y16/rec/reduced1-offline/rec.mp4)
- [a11y16/rec/reduced1-online/rec.mp4](a11y16/rec/reduced1-online/rec.mp4)
- [a11y16/settings.pb](a11y16/settings.pb)
- [a11y16/summary.txt](a11y16/summary.txt)
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
- [dialogs/debug/1-ready.xml](dialogs/debug/1-ready.xml)
- [dialogs/debug/2-ready.xml](dialogs/debug/2-ready.xml)
- [dialogs/debug/3-ready.xml](dialogs/debug/3-ready.xml)
- [dialogs/debug/4-ready.xml](dialogs/debug/4-ready.xml)
- [dialogs/debug/5-ready.xml](dialogs/debug/5-ready.xml)
- [dialogs/debug/6-ready.xml](dialogs/debug/6-ready.xml)
- [dialogs/debug/7-tab-more.xml](dialogs/debug/7-tab-more.xml)
- [dialogs/debug/8-settings.xml](dialogs/debug/8-settings.xml)
- [dialogs/dlg-std-00-settings-screen.xml](dialogs/dlg-std-00-settings-screen.xml)
- [dialogs/dlg-std-01-text-size.xml](dialogs/dlg-std-01-text-size.xml)
- [dialogs/dlg-std-02-clear-cache.xml](dialogs/dlg-std-02-clear-cache.xml)
- [dialogs/dlg-std-03-sort-menu.xml](dialogs/dlg-std-03-sort-menu.xml)
- [dialogs/dlg-std-03a-catalog-screen.xml](dialogs/dlg-std-03a-catalog-screen.xml)
- [dialogs/dlg-std-04-filters.xml](dialogs/dlg-std-04-filters.xml)
- [dialogs/dlg-std-05-whats-new.xml](dialogs/dlg-std-05-whats-new.xml)
- [dialogs/dlg-xl-00-settings-screen.xml](dialogs/dlg-xl-00-settings-screen.xml)
- [dialogs/dlg-xl-01-text-size.xml](dialogs/dlg-xl-01-text-size.xml)
- [dialogs/dlg-xl-02-clear-cache.xml](dialogs/dlg-xl-02-clear-cache.xml)
- [dialogs/dlg-xl-03-sort-menu.xml](dialogs/dlg-xl-03-sort-menu.xml)
- [dialogs/dlg-xl-03a-catalog-screen.xml](dialogs/dlg-xl-03a-catalog-screen.xml)
- [dialogs/dlg-xl-04-filters.xml](dialogs/dlg-xl-04-filters.xml)
- [dialogs/dlg-xl-05-whats-new.xml](dialogs/dlg-xl-05-whats-new.xml)
- [dialogs/dlg-xl130-00-settings-screen.xml](dialogs/dlg-xl130-00-settings-screen.xml)
- [dialogs/dlg-xl130-01-text-size.xml](dialogs/dlg-xl130-01-text-size.xml)
- [dialogs/dlg-xl130-02-clear-cache.xml](dialogs/dlg-xl130-02-clear-cache.xml)
- [dialogs/dlg-xl130-03-sort-menu.xml](dialogs/dlg-xl130-03-sort-menu.xml)
- [dialogs/dlg-xl130-03a-catalog-screen.xml](dialogs/dlg-xl130-03a-catalog-screen.xml)
- [dialogs/dlg-xl130-04-filters.xml](dialogs/dlg-xl130-04-filters.xml)
- [dialogs/dlg-xl130-05-whats-new.xml](dialogs/dlg-xl130-05-whats-new.xml)
- [dialogs/host-after-run.txt](dialogs/host-after-run.txt)
- [dialogs/job-status.txt](dialogs/job-status.txt)
- [dialogs/logcat-all.txt](dialogs/logcat-all.txt)
- [dialogs/logcat-dump.txt](dialogs/logcat-dump.txt)
- [dialogs/logcat-findings.txt](dialogs/logcat-findings.txt)
- [dialogs/logcat-live.txt](dialogs/logcat-live.txt)
- [dialogs/settings.pb](dialogs/settings.pb)
- [dialogs/summary.txt](dialogs/summary.txt)
- [dialogs/update.json](dialogs/update.json)
- [dialogs/whatsnew-record.json](dialogs/whatsnew-record.json)
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
- [emoji33/host-after-run.txt](emoji33/host-after-run.txt)
- [emoji33/job-status.txt](emoji33/job-status.txt)
- [emoji33/logcat-all.txt](emoji33/logcat-all.txt)
- [emoji33/logcat-dump.txt](emoji33/logcat-dump.txt)
- [emoji33/logcat-findings.txt](emoji33/logcat-findings.txt)
- [emoji33/logcat-live.txt](emoji33/logcat-live.txt)
- [emoji33/site/emoji.json](emoji33/site/emoji.json)
- [emoji33/site/emoji.txt](emoji33/site/emoji.txt)
- [emoji33/site/events.json](emoji33/site/events.json)
- [emoji33/site/places.txt](emoji33/site/places.txt)
- [emoji33/start-cold.txt](emoji33/start-cold.txt)
- [emoji33/summary.txt](emoji33/summary.txt)
- [events/event-detail.xml](events/event-detail.xml)
- [events/events-list.xml](events/events-list.xml)
- [events/host-after-run.txt](events/host-after-run.txt)
- [events/job-status.txt](events/job-status.txt)
- [events/logcat-all.txt](events/logcat-all.txt)
- [events/logcat-dump.txt](events/logcat-dump.txt)
- [events/logcat-findings.txt](events/logcat-findings.txt)
- [events/logcat-live.txt](events/logcat-live.txt)
- [events/notif-e3.txt](events/notif-e3.txt)
- [events/shade-20-reminder.xml](events/shade-20-reminder.xml)
- [events/site/emoji.json](events/site/emoji.json)
- [events/site/emoji.txt](events/site/emoji.txt)
- [events/site/events.json](events/site/events.json)
- [events/site/places.txt](events/site/places.txt)
- [events/summary.txt](events/summary.txt)
- [login/forgot-20-demo.xml](login/forgot-20-demo.xml)
- [login/forgot-31-prod.xml](login/forgot-31-prod.xml)
- [login/host-after-run.txt](login/host-after-run.txt)
- [login/job-status.txt](login/job-status.txt)
- [login/logcat-all.txt](login/logcat-all.txt)
- [login/logcat-dump.txt](login/logcat-dump.txt)
- [login/logcat-findings.txt](login/logcat-findings.txt)
- [login/logcat-live.txt](login/logcat-live.txt)
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
- [offline/host-after-run.txt](offline/host-after-run.txt)
- [offline/job-status.txt](offline/job-status.txt)
- [offline/logcat-all.txt](offline/logcat-all.txt)
- [offline/logcat-dump.txt](offline/logcat-dump.txt)
- [offline/logcat-findings.txt](offline/logcat-findings.txt)
- [offline/logcat-live.txt](offline/logcat-live.txt)
- [offline/summary.txt](offline/summary.txt)
- [onboard/host-after-run.txt](onboard/host-after-run.txt)
- [onboard/job-status.txt](onboard/job-status.txt)
- [onboard/logcat-all.txt](onboard/logcat-all.txt)
- [onboard/logcat-dump.txt](onboard/logcat-dump.txt)
- [onboard/logcat-findings.txt](onboard/logcat-findings.txt)
- [onboard/logcat-live.txt](onboard/logcat-live.txt)
- [onboard/onb-01-page1.xml](onboard/onb-01-page1.xml)
- [onboard/onb-02-page2.xml](onboard/onb-02-page2.xml)
- [onboard/onb-03-page2-xlarge.xml](onboard/onb-03-page2-xlarge.xml)
- [onboard/onb-05-page2-after-allow.xml](onboard/onb-05-page2-after-allow.xml)
- [onboard/onb-06-page3.xml](onboard/onb-06-page3.xml)
- [onboard/onb-11-page2-after-deny.xml](onboard/onb-11-page2-after-deny.xml)
- [onboard/onb-12-page3.xml](onboard/onb-12-page3.xml)
- [onboard/summary.txt](onboard/summary.txt)
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
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-base-upd-ref.txt](size/apk-base-upd-ref.txt)
- [size/apk-dev-ref.txt](size/apk-dev-ref.txt)
- [size/apk-new-upd-ref.txt](size/apk-new-upd-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/size-base.txt](size/size-base.txt)
- [size/size-new.txt](size/size-new.txt)
- [tablet/host-after-run.txt](tablet/host-after-run.txt)
- [tablet/job-status.txt](tablet/job-status.txt)
- [tablet/logcat-all.txt](tablet/logcat-all.txt)
- [tablet/logcat-dump.txt](tablet/logcat-dump.txt)
- [tablet/logcat-findings.txt](tablet/logcat-findings.txt)
- [tablet/logcat-live.txt](tablet/logcat-live.txt)
- [tablet/summary.txt](tablet/summary.txt)
- [unlock33/dumpsys-package.txt](unlock33/dumpsys-package.txt)
- [unlock33/host-after-run.txt](unlock33/host-after-run.txt)
- [unlock33/job-status.txt](unlock33/job-status.txt)
- [unlock33/logcat-all.txt](unlock33/logcat-all.txt)
- [unlock33/logcat-dump.txt](unlock33/logcat-dump.txt)
- [unlock33/logcat-findings.txt](unlock33/logcat-findings.txt)
- [unlock33/logcat-live.txt](unlock33/logcat-live.txt)
- [unlock33/prompt-enable.xml](unlock33/prompt-enable.xml)
- [unlock33/qs-gate-page1.xml](unlock33/qs-gate-page1.xml)
- [unlock33/qs-gate-page2.xml](unlock33/qs-gate-page2.xml)
- [unlock33/qs-gate-page3.xml](unlock33/qs-gate-page3.xml)
- [unlock33/qs-gate-page4.xml](unlock33/qs-gate-page4.xml)
- [unlock33/qs-gate2-page1.xml](unlock33/qs-gate2-page1.xml)
- [unlock33/summary.txt](unlock33/summary.txt)
- [unlock34/dumpsys-package.txt](unlock34/dumpsys-package.txt)
- [unlock34/host-after-run.txt](unlock34/host-after-run.txt)
- [unlock34/job-status.txt](unlock34/job-status.txt)
- [unlock34/logcat-all.txt](unlock34/logcat-all.txt)
- [unlock34/logcat-dump.txt](unlock34/logcat-dump.txt)
- [unlock34/logcat-findings.txt](unlock34/logcat-findings.txt)
- [unlock34/logcat-live.txt](unlock34/logcat-live.txt)
- [unlock34/prompt-enable.xml](unlock34/prompt-enable.xml)
- [unlock34/qs-gate-page1.xml](unlock34/qs-gate-page1.xml)
- [unlock34/qs-gate-page2.xml](unlock34/qs-gate-page2.xml)
- [unlock34/qs-gate-page3.xml](unlock34/qs-gate-page3.xml)
- [unlock34/qs-gate-page4.xml](unlock34/qs-gate-page4.xml)
- [unlock34/qs-gate2-page1.xml](unlock34/qs-gate2-page1.xml)
- [unlock34/summary.txt](unlock34/summary.txt)
- [update/b4.txt](update/b4.txt)
- [update/dialog-a.xml](update/dialog-a.xml)
- [update/dialog-b.xml](update/dialog-b.xml)
- [update/host-after-run.txt](update/host-after-run.txt)
- [update/job-status.txt](update/job-status.txt)
- [update/jobscheduler-after-2nd-tap.txt](update/jobscheduler-after-2nd-tap.txt)
- [update/logcat-all.txt](update/logcat-all.txt)
- [update/logcat-dump.txt](update/logcat-dump.txt)
- [update/logcat-findings.txt](update/logcat-findings.txt)
- [update/logcat-live.txt](update/logcat-live.txt)
- [update/notif-b2-1.txt](update/notif-b2-1.txt)
- [update/notif-b2-2.txt](update/notif-b2-2.txt)
- [update/notif-b2-3.txt](update/notif-b2-3.txt)
- [update/notif-b3.txt](update/notif-b3.txt)
- [update/notif-b3b.txt](update/notif-b3b.txt)
- [update/notif-b4-check.txt](update/notif-b4-check.txt)
- [update/notif-b4.txt](update/notif-b4.txt)
- [update/notif-b4t-1.txt](update/notif-b4t-1.txt)
- [update/notif-b4t-2.txt](update/notif-b4t-2.txt)
- [update/notif-b4t-3.txt](update/notif-b4t-3.txt)
- [update/notif-b4t-4.txt](update/notif-b4t-4.txt)
- [update/shade-downloading.xml](update/shade-downloading.xml)
- [update/summary.txt](update/summary.txt)
- [update/update.json](update/update.json)
- [update/whatsnew-12-whats-new-1.2.30.xml](update/whatsnew-12-whats-new-1.2.30.xml)
- [update/whatsnew-34-whats-new-after-b.xml](update/whatsnew-34-whats-new-after-b.xml)
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
