# Wave 13 emulator check before release (v127), round 2: startup A/B and follow-ups

Source: `debug/wave13-check` @ 3274c6e (app = wave 13 merge 305a487), run 40565019874 (attempt 1), 2026-10-27T19:19:28Z.
Emulators: google_apis x86_64 API 33, pixel_6 profile, -gpu swangle_indirect; app locale bg-BG.
Round 1: [../index.md](../index.md). Previous full test (v1.2.26): [../../v126/index.md](../../v126/index.md).

| part | status |
|---|---|
| abstart-1 | COMPLETE; part=abstart-1 api=33 gpu=swangle_indirect emulator-step=success |
| abstart-2 | COMPLETE; part=abstart-2 api=33 gpu=swangle_indirect emulator-step=success |
| func3 | COMPLETE; part=func3 api=33 gpu=swangle_indirect emulator-step=success |

## abstart-1

```
part abstart-1; APK: 3274c6e (Проверка на вълна 13 (v127), кръг 2: старт A/B срещу v1.2.26 на един емулатор, /); x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## AB new-1: fresh install, first launch, catalogue index, 10 cold starts
installed new-1: 3274c6e (Проверка на вълна 13 (v127), кръг 2: старт A/B срещу v1.2.26 на един емулатор, /); x86_64 libs: 4 size 4875530
new-1 first-install catalogue results after 2406 ms
START cold new-1         n=10 min=768ms median=1003ms max=1214ms  all=[1056, 768, 854, 1068, 1214, 806, 1014, 992, 925, 1077]  states=['COLD']
## AB base-1: fresh install, first launch, catalogue index, 10 cold starts
installed base-1: 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); x86_64 libs: 4 size 4924210
base-1 first-install catalogue results after 2442 ms
START cold base-1        n=10 min=848ms median=1038ms max=1161ms  all=[1095, 1065, 1034, 1041, 886, 1047, 1161, 1021, 848, 908]  states=['COLD']
## AB base-2: fresh install, first launch, catalogue index, 10 cold starts
installed base-2: 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); x86_64 libs: 4 size 4924210
base-2 first-install catalogue results after 2387 ms
START cold base-2        n=10 min=805ms median=1154ms max=1376ms  all=[1140, 1376, 1215, 1168, 1012, 805, 1047, 1065, 1199, 1230]  states=['COLD']
## AB new-2: fresh install, first launch, catalogue index, 10 cold starts
installed new-2: 3274c6e (Проверка на вълна 13 (v127), кръг 2: старт A/B срещу v1.2.26 на един емулатор, /); x86_64 libs: 4 size 4875530
new-2 first-install catalogue results after 2338 ms
START cold new-2         n=10 min=787ms median=1044ms max=1107ms  all=[965, 949, 1024, 1107, 1046, 1083, 787, 1042, 1059, 1081]  states=['COLD']
## AB totals
START cold NEW 305a487   n=20 min=768ms median=1033ms max=1214ms  all=[1056, 768, 854, 1068, 1214, 806, 1014, 992, 925, 1077, 965, 949, 1024, 1107, 1046, 1083, 787, 1042, 1059, 1081]  states=['COLD']
START cold BASE 3df7f37  n=20 min=805ms median=1056ms max=1376ms  all=[1095, 1065, 1034, 1041, 886, 1047, 1161, 1021, 848, 908, 1140, 1376, 1215, 1168, 1012, 805, 1047, 1065, 1199, 1230]  states=['COLD']
## COMPLETE
## logcat findings
app pids seen: ['6556', '7297', '7492', '7704', '7785', '7846', '7900', '7968', '8023', '8075', '8145', '8196', '8260', '8443', '8706', '8853', '8996', '9068', '9123', '9173', '9238', '9291', '9342', '9396', '9448', '9514', '9717', '9974', '10118', '10408', '10480', '10545', '10599', '10671', '10725', '10777', '10827', '10881', '10932', '11112', '11367', '11511', '11653', '11723', '11777', '11827', '11895', '11947', '12000', '12052', '12108', '12159']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 12
   skipped-frames events=12 max=52 total=440
== Davey: 10
   davey events=10 max=1195ms median=862ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## abstart-2

```
part abstart-2; APK: 3274c6e (Проверка на вълна 13 (v127), кръг 2: старт A/B срещу v1.2.26 на един емулатор, /); x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## AB new-1: fresh install, first launch, catalogue index, 10 cold starts
installed new-1: 3274c6e (Проверка на вълна 13 (v127), кръг 2: старт A/B срещу v1.2.26 на един емулатор, /); x86_64 libs: 4 size 4875530
new-1 first-install catalogue results after 3456 ms
START cold new-1         n=10 min=1936ms median=2658ms max=2934ms  all=[2822, 2690, 2934, 2774, 2321, 2694, 1936, 2059, 2405, 2625]  states=['COLD']
## AB base-1: fresh install, first launch, catalogue index, 10 cold starts
installed base-1: 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); x86_64 libs: 4 size 4924210
base-1 first-install catalogue results after 2766 ms
START cold base-1        n=10 min=1626ms median=1859ms max=2110ms  all=[2042, 2085, 1781, 1626, 1800, 1781, 1817, 1983, 1901, 2110]  states=['COLD']
## AB base-2: fresh install, first launch, catalogue index, 10 cold starts
installed base-2: 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); x86_64 libs: 4 size 4924210
base-2 first-install catalogue results after 2725 ms
START cold base-2        n=10 min=1692ms median=1776ms max=2043ms  all=[2025, 1827, 1779, 1774, 1760, 1936, 1692, 1767, 2043, 1725]  states=['COLD']
## AB new-2: fresh install, first launch, catalogue index, 10 cold starts
installed new-2: 3274c6e (Проверка на вълна 13 (v127), кръг 2: старт A/B срещу v1.2.26 на един емулатор, /); x86_64 libs: 4 size 4875530
new-2 first-install catalogue results after 2710 ms
START cold new-2         n=10 min=1457ms median=1814ms max=2113ms  all=[1638, 1876, 1551, 2069, 1941, 1753, 1457, 1654, 1930, 2113]  states=['COLD']
## AB totals
START cold NEW 305a487   n=20 min=1457ms median=2064ms max=2934ms  all=[2822, 2690, 2934, 2774, 2321, 2694, 1936, 2059, 2405, 2625, 1638, 1876, 1551, 2069, 1941, 1753, 1457, 1654, 1930, 2113]  states=['COLD']
START cold BASE 3df7f37  n=20 min=1626ms median=1808ms max=2110ms  all=[2042, 2085, 1781, 1626, 1800, 1781, 1817, 1983, 1901, 2110, 2025, 1827, 1779, 1774, 1760, 1936, 1692, 1767, 2043, 1725]  states=['COLD']
## COMPLETE
## logcat findings
app pids seen: ['4893', '5805', '6050', '6787', '6918', '7008', '7163', '7449', '7647', '7756', '7824', '7932', '8096', '8520', '8820', '8957', '9129', '9194', '9256', '9304', '9352', '9399', '9446', '9496', '9556', '9601', '9773', '10027', '10168', '10319', '10380', '10441', '10495', '10541', '10650', '10696', '10743', '10792', '10838', '11002', '11264', '11411', '11550', '11613', '11662', '11708', '11757', '11803', '11853', '11900', '11949', '11994']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 68
   skipped-frames events=68 max=83 total=3127
== Davey: 68
   davey events=68 max=1683ms median=996ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func3

```
part func3; APK: 3274c6e (Проверка на вълна 13 (v127), кръг 2: старт A/B срещу v1.2.26 на един емулатор, /); x86_64 libs: 4 size 4875530 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## W. First launch and warm-up
first-install catalogue results after 3115 ms
## F4b. /events from an open article
article open: back=yes; content-desc="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област в Яворец." text="СЪБИТИЕ  ·  16 октомври 2026" text="Фолклорно пътешествие из Пирин
/events from an article: News[checked=false selected=false bounds=[42,308][354,434]] Calendar[checked=true selected=false bounds=[354,308][669,434]] subscribe-action=visible; back-button none
Back after that: News[] Calendar[] subscribe-action=absent; text="НЧ „Васил Левски – 1922“" text="с. Яворец" content-desc="Съобщения" text="Търси книга, новина, събитие…" 
## F11b. Home quick-action labels at font 1.3 + 'Много голям' (scrolled into view)
TEXT SIZE now: text="Много голям" 
QA font 1.3 + Много голям, scrolled: Сканирай|=Scan@[291 981] Читателска карта|=Library card@[789 1016] Искам да прочета|=Want to read@[291 1364] Контакти|=Contacts@[789 1329]; rows=2; quick action 'Събития': absent
TEXT SIZE now: text="Стандартен" 
## COMPLETE
## logcat findings
app pids seen: ['4948', '8071']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=65 total=177
== Davey: 5
   davey events=5 max=1389ms median=1315ms
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

### func3/shots/func3

- [func3/shots/func3/01-article.png](func3/shots/func3/01-article.png)
- [func3/shots/func3/02-events-from-article.png](func3/shots/func3/02-events-from-article.png)
- [func3/shots/func3/03-home-font130-xlarge-qa.png](func3/shots/func3/03-home-font130-xlarge-qa.png)

## Other files

- [abstart-1/host-after-run.txt](abstart-1/host-after-run.txt)
- [abstart-1/job-status.txt](abstart-1/job-status.txt)
- [abstart-1/logcat-all.txt](abstart-1/logcat-all.txt)
- [abstart-1/logcat-dump.txt](abstart-1/logcat-dump.txt)
- [abstart-1/logcat-findings.txt](abstart-1/logcat-findings.txt)
- [abstart-1/logcat-live.txt](abstart-1/logcat-live.txt)
- [abstart-1/start-base-1.txt](abstart-1/start-base-1.txt)
- [abstart-1/start-base-2.txt](abstart-1/start-base-2.txt)
- [abstart-1/start-base-all.txt](abstart-1/start-base-all.txt)
- [abstart-1/start-new-1.txt](abstart-1/start-new-1.txt)
- [abstart-1/start-new-2.txt](abstart-1/start-new-2.txt)
- [abstart-1/start-new-all.txt](abstart-1/start-new-all.txt)
- [abstart-1/summary.txt](abstart-1/summary.txt)
- [abstart-2/host-after-run.txt](abstart-2/host-after-run.txt)
- [abstart-2/job-status.txt](abstart-2/job-status.txt)
- [abstart-2/logcat-all.txt](abstart-2/logcat-all.txt)
- [abstart-2/logcat-dump.txt](abstart-2/logcat-dump.txt)
- [abstart-2/logcat-findings.txt](abstart-2/logcat-findings.txt)
- [abstart-2/logcat-live.txt](abstart-2/logcat-live.txt)
- [abstart-2/start-base-1.txt](abstart-2/start-base-1.txt)
- [abstart-2/start-base-2.txt](abstart-2/start-base-2.txt)
- [abstart-2/start-base-all.txt](abstart-2/start-base-all.txt)
- [abstart-2/start-new-1.txt](abstart-2/start-new-1.txt)
- [abstart-2/start-new-2.txt](abstart-2/start-new-2.txt)
- [abstart-2/start-new-all.txt](abstart-2/start-new-all.txt)
- [abstart-2/summary.txt](abstart-2/summary.txt)
- [func3/host-after-run.txt](func3/host-after-run.txt)
- [func3/job-status.txt](func3/job-status.txt)
- [func3/logcat-all.txt](func3/logcat-all.txt)
- [func3/logcat-dump.txt](func3/logcat-dump.txt)
- [func3/logcat-findings.txt](func3/logcat-findings.txt)
- [func3/logcat-live.txt](func3/logcat-live.txt)
- [func3/summary.txt](func3/summary.txt)
