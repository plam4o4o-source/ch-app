# v1.2.26 full emulator test (round 3: what round 2 lost, and -gpu swangle_indirect)

Source: `debug/full-test-2` @ ac5ea38 (app = origin/main 3df7f37), run 40535799842 (attempt 1), 2026-10-27T16:10:35Z.
Emulators: google_apis x86_64 API 33, pixel_6 profile; parts named *-swangle use -gpu swangle_indirect, the rest swiftshader_indirect.
Round 2: [../index.md](../index.md); round 1: [../run1/index.md](../run1/index.md).

| part | status |
|---|---|
| func3-swangle | COMPLETE; part=func3-swangle api=33 gpu=swangle_indirect emulator-step=success |
| func3 | COMPLETE; part=func3 api=33 gpu=swiftshader_indirect emulator-step=success |
| look3-swangle | COMPLETE; part=look3-swangle api=33 gpu=swangle_indirect emulator-step=success |
| look3 | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: end)); part=look3 api=33 gpu=swiftshader_indirect emulator-step=failure |
| probe33-swangle | COMPLETE; part=probe33-swangle api=33 gpu=swangle_indirect emulator-step=success |
| stress3-swangle | COMPLETE; part=stress3-swangle api=33 gpu=swangle_indirect emulator-step=success |
| stress3 | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: after monkey)); part=stress3 api=33 gpu=swiftshader_indirect emulator-step=failure |
| wantjank | INCOMPLETE (!!! EMULATOR GONE / HUNG (at: 3w. Jank of the Want to read list at 540x1200 (animations ON))); part=wantjank api=33 gpu=swiftshader_indirect emulator-step=failure |

## func3-swangle

```
part func3-swangle; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 3694 ms
## F5. Scanner with CAMERA revoked → manual entry
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
SCAN opened from the Home quick action 'Scan'
SCAN screen: text="Camera access needed" text="The camera is only used to read the barcode on your phone. Nothing is recorded or sent." text="Allow camera" text="Open settings" text="Enter the number manually" content-desc="Back" text="Scan" 
manual field: text="399560"
SCAN result: content-desc="Close sheet" content-desc="Drag handle" content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Available" text="inv. no. 399560" text="Close" text="Open book" 
SCAN → book: opened; content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Available" text="Availability as of Oct 17, 2026" text="Want to read" 
emulator alive after scanner: yes
## F6. Settings → Clear temporary data → catalogue reloads
CLEAR: snackbar shown
CLEAR → catalogue results again after 2905 ms (2446 results)
home after clear: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 27, 2026 · *Св. мчк Нестор. Преп. Димитрий Басарбовски" content-de
news after clear: text="Calendar" text="News" text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" text="СЪБИТИЕ  ·  October 16, 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област
MEM func-end (raw)   PSS=84262KB  Java=16116KB  Native=25816KB  Graphics=0KB  Views=12 Activities=1
MEM func-end (trimmed) PSS=80463KB  Java=11088KB  Native=22024KB  Graphics=0KB  Views=12 Activities=1
## F4. Settings → Text size → Extra large
WARN: settings item =Text size|=Размер на текста not found
## COMPLETE
## logcat findings
app pids seen: ['4871']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=65 total=97
== Davey: 13
   davey events=13 max=1358ms median=845ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## func3

```
part func3; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 4986 ms
## F5. Scanner with CAMERA revoked → manual entry
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
SCAN opened from the Home quick action 'Scan'
SCAN screen: text="Camera access needed" text="The camera is only used to read the barcode on your phone. Nothing is recorded or sent." text="Allow camera" text="Open settings" text="Enter the number manually" content-desc="Back" text="Scan" 
manual field: text="399560"
SCAN result: content-desc="Close sheet" content-desc="Drag handle" content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="2011" text="Available" text="inv. no. 399560" text="Close" text="Open book" 
SCAN → book: opened; content-desc="12 -те вятъра" text="12 -те вятъра" text="Гарфилд, Джудит и др." text="Available" text="Availability as of Oct 17, 2026" text="Want to read" 
emulator alive after scanner: yes
## F6. Settings → Clear temporary data → catalogue reloads
CLEAR: snackbar shown
CLEAR → catalogue results again after 2266 ms (2446 results)
home after clear: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" text="Search books, news, events…" content-desc="Scan barcode" text="October 27, 2026 · *Св. мчк Нестор. Преп. Димитрий Басарбовски" content-de
news after clear: text="Calendar" text="News" text="Search news" text="All" text="Saved" text="Събитие" text="Състави" text="Проекти" text="СЪБИТИЕ  ·  October 16, 2026" text="Фолклорно пътешествие из Пиринска, Родопска и Тракийска област
MEM func-end (raw)   PSS=95590KB  Java=16088KB  Native=31216KB  Graphics=0KB  Views=12 Activities=1
MEM func-end (trimmed) PSS=90254KB  Java=11148KB  Native=25712KB  Graphics=0KB  Views=12 Activities=1
## F4. Settings → Text size → Extra large
WARN: settings item =Text size|=Размер на текста not found
## COMPLETE
## logcat findings
app pids seen: ['5208']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 6
   skipped-frames events=6 max=57 total=233
== Davey: 7
   davey events=7 max=1849ms median=1066ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look3-swangle

```
part look3-swangle; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
first-install catalogue results after 2510 ms
## L5. Landscape
landscape: 2400x1080
matrix landscape: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
landscape rail labels: home catalog my news more 
## L4. Font scale 1.3
matrix font130: news title OK; segments News[checked=false selected=false bounds=[42,308][395,434]] Calendar[checked=true selected=false bounds=[395,308][751,434]] subscribe-action=visible
## COMPLETE
## logcat findings
app pids seen: ['5606', '6131', '8008']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=51 total=111
== Davey: 4
   davey events=4 max=1145ms median=822ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## look3

```
part look3; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## L0. First launch and warm-up
first-install catalogue results after 4811 ms
## L5. Landscape
landscape: 2400x1080
matrix landscape: news title OK; segments News[checked=false selected=false bounds=[42,308][336,434]] Calendar[checked=true selected=false bounds=[336,308][633,434]] subscribe-action=visible
landscape rail labels: home catalog my news more 
## L4. Font scale 1.3
WARN: tab bar not reached
matrix font130: news title MISSING; segments News[] Calendar[] subscribe-action=absent
!!! EMULATOR GONE / HUNG (at: end)
## logcat findings
app pids seen: ['4459', '6123', '7744']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 10
   skipped-frames events=10 max=103 total=478
== Davey: 15
   davey events=15 max=2048ms median=1083ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## probe33-swangle

```
part probe33-swangle; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 3064 ms
## S1. Home swipes (API stability probe: 20 swipes)
HOME SWIPES SURVIVED: 20/20
## COMPLETE
## logcat findings
app pids seen: ['5238', '6746']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=44 total=154
== Davey: 6
   davey events=6 max=1119ms median=888ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## stress3-swangle

```
part stress3-swangle; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 2993 ms
## S5. Monkey 4000 events (seed 1234, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
statusbar disable: 
monkey start pid=6693
MONKEY exit=0 duration=176 s; Events injected: 4000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '6693'; airplane=0; camera:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
MEM after-monkey (raw) PSS=91042KB  Java=17636KB  Native=30960KB  Graphics=0KB  Views=26 Activities=1
MEM after-monkey (trimmed) PSS=84800KB  Java=9896KB  Native=28732KB  Graphics=0KB  Views=15 Activities=1
## COMPLETE
## logcat findings
app pids seen: ['5291', '6693']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 21
   skipped-frames events=21 max=100 total=927
== Davey: 74
   davey events=74 max=1445ms median=826ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## stress3

```
part stress3; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 2760 ms
## S5. Monkey 4000 events (seed 1234, CAMERA revoked)
camera permission before monkey:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
statusbar disable: 
monkey start pid=7187
MONKEY exit=255 duration=82 s; 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
!!! EMULATOR GONE / HUNG (at: after monkey)
## logcat findings
app pids seen: ['5387', '7187']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=81 total=195
== Davey: 5
   davey events=5 max=1319ms median=1166ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## wantjank

```
part wantjank; APK: ac5ea38 (Проверка v1.2.26: трети кръг — липсващото от втория, проба с -gpu swangle_indire); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
WARN: tab bar not reached
Want to read: added 0 books for the list jank
## 3w. Jank of the Want to read list at 540x1200 (animations ON)
!!! EMULATOR GONE / HUNG (at: 3w. Jank of the Want to read list at 540x1200 (animations ON))
## logcat findings
app pids seen: ['5197']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=35 total=66
== Davey: 4
   davey events=4 max=1433ms median=1008ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### func3-swangle/shots/debug

- [func3-swangle/shots/debug/1-settings-item.png](func3-swangle/shots/debug/1-settings-item.png)

### func3-swangle/shots/func

- [func3-swangle/shots/func/17-scanner-no-camera.png](func3-swangle/shots/func/17-scanner-no-camera.png)
- [func3-swangle/shots/func/18-manual-entry.png](func3-swangle/shots/func/18-manual-entry.png)
- [func3-swangle/shots/func/19-manual-typed.png](func3-swangle/shots/func/19-manual-typed.png)
- [func3-swangle/shots/func/20-manual-result.png](func3-swangle/shots/func/20-manual-result.png)
- [func3-swangle/shots/func/21-scan-book.png](func3-swangle/shots/func/21-scan-book.png)
- [func3-swangle/shots/func/22-clear-dialog.png](func3-swangle/shots/func/22-clear-dialog.png)
- [func3-swangle/shots/func/23-cleared.png](func3-swangle/shots/func/23-cleared.png)
- [func3-swangle/shots/func/24-catalog-after-clear.png](func3-swangle/shots/func/24-catalog-after-clear.png)
- [func3-swangle/shots/func/25-home-after-clear.png](func3-swangle/shots/func/25-home-after-clear.png)
- [func3-swangle/shots/func/26-news-after-clear.png](func3-swangle/shots/func/26-news-after-clear.png)

### func3/shots/debug

- [func3/shots/debug/1-settings-item.png](func3/shots/debug/1-settings-item.png)

### func3/shots/func

- [func3/shots/func/17-scanner-no-camera.png](func3/shots/func/17-scanner-no-camera.png)
- [func3/shots/func/18-manual-entry.png](func3/shots/func/18-manual-entry.png)
- [func3/shots/func/19-manual-typed.png](func3/shots/func/19-manual-typed.png)
- [func3/shots/func/20-manual-result.png](func3/shots/func/20-manual-result.png)
- [func3/shots/func/21-scan-book.png](func3/shots/func/21-scan-book.png)
- [func3/shots/func/22-clear-dialog.png](func3/shots/func/22-clear-dialog.png)
- [func3/shots/func/23-cleared.png](func3/shots/func/23-cleared.png)
- [func3/shots/func/24-catalog-after-clear.png](func3/shots/func/24-catalog-after-clear.png)
- [func3/shots/func/25-home-after-clear.png](func3/shots/func/25-home-after-clear.png)
- [func3/shots/func/26-news-after-clear.png](func3/shots/func/26-news-after-clear.png)

### look3-swangle/shots/font130

- [look3-swangle/shots/font130/01-home.png](look3-swangle/shots/font130/01-home.png)
- [look3-swangle/shots/font130/02-catalog.png](look3-swangle/shots/font130/02-catalog.png)
- [look3-swangle/shots/font130/03-news.png](look3-swangle/shots/font130/03-news.png)
- [look3-swangle/shots/font130/04-calendar.png](look3-swangle/shots/font130/04-calendar.png)
- [look3-swangle/shots/font130/05-book.png](look3-swangle/shots/font130/05-book.png)

### look3-swangle/shots/landscape

- [look3-swangle/shots/landscape/01-home.png](look3-swangle/shots/landscape/01-home.png)
- [look3-swangle/shots/landscape/03-news.png](look3-swangle/shots/landscape/03-news.png)
- [look3-swangle/shots/landscape/04-calendar.png](look3-swangle/shots/landscape/04-calendar.png)
- [look3-swangle/shots/landscape/05-book.png](look3-swangle/shots/landscape/05-book.png)

### look3/shots/landscape

- [look3/shots/landscape/01-home.png](look3/shots/landscape/01-home.png)
- [look3/shots/landscape/02-catalog.png](look3/shots/landscape/02-catalog.png)
- [look3/shots/landscape/03-news.png](look3/shots/landscape/03-news.png)
- [look3/shots/landscape/04-calendar.png](look3/shots/landscape/04-calendar.png)
- [look3/shots/landscape/05-book.png](look3/shots/landscape/05-book.png)

### probe33-swangle/shots

- [probe33-swangle/shots/swipe-home-end.png](probe33-swangle/shots/swipe-home-end.png)

### stress3-swangle/shots

- [stress3-swangle/shots/monkey-end.png](stress3-swangle/shots/monkey-end.png)

## Other files

- [func3-swangle/debug/1-settings-item.xml](func3-swangle/debug/1-settings-item.xml)
- [func3-swangle/host-after-run.txt](func3-swangle/host-after-run.txt)
- [func3-swangle/job-status.txt](func3-swangle/job-status.txt)
- [func3-swangle/logcat-all.txt](func3-swangle/logcat-all.txt)
- [func3-swangle/logcat-dump.txt](func3-swangle/logcat-dump.txt)
- [func3-swangle/logcat-findings.txt](func3-swangle/logcat-findings.txt)
- [func3-swangle/logcat-live.txt](func3-swangle/logcat-live.txt)
- [func3-swangle/mem-func-end-raw.txt](func3-swangle/mem-func-end-raw.txt)
- [func3-swangle/mem-func-end.txt](func3-swangle/mem-func-end.txt)
- [func3-swangle/summary.txt](func3-swangle/summary.txt)
- [func3/debug/1-settings-item.xml](func3/debug/1-settings-item.xml)
- [func3/host-after-run.txt](func3/host-after-run.txt)
- [func3/job-status.txt](func3/job-status.txt)
- [func3/logcat-all.txt](func3/logcat-all.txt)
- [func3/logcat-dump.txt](func3/logcat-dump.txt)
- [func3/logcat-findings.txt](func3/logcat-findings.txt)
- [func3/logcat-live.txt](func3/logcat-live.txt)
- [func3/mem-func-end-raw.txt](func3/mem-func-end-raw.txt)
- [func3/mem-func-end.txt](func3/mem-func-end.txt)
- [func3/summary.txt](func3/summary.txt)
- [look3-swangle/host-after-run.txt](look3-swangle/host-after-run.txt)
- [look3-swangle/job-status.txt](look3-swangle/job-status.txt)
- [look3-swangle/logcat-all.txt](look3-swangle/logcat-all.txt)
- [look3-swangle/logcat-dump.txt](look3-swangle/logcat-dump.txt)
- [look3-swangle/logcat-findings.txt](look3-swangle/logcat-findings.txt)
- [look3-swangle/logcat-live.txt](look3-swangle/logcat-live.txt)
- [look3-swangle/summary.txt](look3-swangle/summary.txt)
- [look3/host-after-run.txt](look3/host-after-run.txt)
- [look3/job-status.txt](look3/job-status.txt)
- [look3/logcat-all.txt](look3/logcat-all.txt)
- [look3/logcat-dump.txt](look3/logcat-dump.txt)
- [look3/logcat-findings.txt](look3/logcat-findings.txt)
- [look3/logcat-live.txt](look3/logcat-live.txt)
- [look3/summary.txt](look3/summary.txt)
- [probe33-swangle/host-after-run.txt](probe33-swangle/host-after-run.txt)
- [probe33-swangle/job-status.txt](probe33-swangle/job-status.txt)
- [probe33-swangle/logcat-all.txt](probe33-swangle/logcat-all.txt)
- [probe33-swangle/logcat-dump.txt](probe33-swangle/logcat-dump.txt)
- [probe33-swangle/logcat-findings.txt](probe33-swangle/logcat-findings.txt)
- [probe33-swangle/logcat-live.txt](probe33-swangle/logcat-live.txt)
- [probe33-swangle/summary.txt](probe33-swangle/summary.txt)
- [stress3-swangle/host-after-run.txt](stress3-swangle/host-after-run.txt)
- [stress3-swangle/job-status.txt](stress3-swangle/job-status.txt)
- [stress3-swangle/logcat-all.txt](stress3-swangle/logcat-all.txt)
- [stress3-swangle/logcat-dump.txt](stress3-swangle/logcat-dump.txt)
- [stress3-swangle/logcat-findings.txt](stress3-swangle/logcat-findings.txt)
- [stress3-swangle/logcat-live.txt](stress3-swangle/logcat-live.txt)
- [stress3-swangle/mem-after-monkey-raw.txt](stress3-swangle/mem-after-monkey-raw.txt)
- [stress3-swangle/mem-after-monkey.txt](stress3-swangle/mem-after-monkey.txt)
- [stress3-swangle/monkey.txt](stress3-swangle/monkey.txt)
- [stress3-swangle/summary.txt](stress3-swangle/summary.txt)
- [stress3/host-after-run.txt](stress3/host-after-run.txt)
- [stress3/job-status.txt](stress3/job-status.txt)
- [stress3/logcat-all.txt](stress3/logcat-all.txt)
- [stress3/logcat-dump.txt](stress3/logcat-dump.txt)
- [stress3/logcat-findings.txt](stress3/logcat-findings.txt)
- [stress3/logcat-live.txt](stress3/logcat-live.txt)
- [stress3/monkey.txt](stress3/monkey.txt)
- [stress3/summary.txt](stress3/summary.txt)
- [wantjank/host-after-run.txt](wantjank/host-after-run.txt)
- [wantjank/job-status.txt](wantjank/job-status.txt)
- [wantjank/logcat-all.txt](wantjank/logcat-all.txt)
- [wantjank/logcat-dump.txt](wantjank/logcat-dump.txt)
- [wantjank/logcat-findings.txt](wantjank/logcat-findings.txt)
- [wantjank/logcat-live.txt](wantjank/logcat-live.txt)
- [wantjank/summary.txt](wantjank/summary.txt)
