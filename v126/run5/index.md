# v1.2.26 full emulator test (round 5: landscape matrix, Want to read jank with more books, swangle_indirect)

Source: `debug/full-test-2` @ 82e0251 (app = origin/main 3df7f37), run 40541070551 (attempt 1), 2026-10-27T16:40:27Z.
Emulators: google_apis x86_64 API 33, pixel_6 profile; parts named *-swangle use -gpu swangle_indirect, the rest swiftshader_indirect.
Round 4: [../run4/index.md](../run4/index.md); round 3: [../run3/index.md](../run3/index.md); round 2: [../index.md](../index.md); round 1: [../run1/index.md](../run1/index.md).

| part | status |
|---|---|
| look5-swangle | COMPLETE; part=look5-swangle api=33 gpu=swangle_indirect emulator-step=success |
| wantjank5-swangle | COMPLETE; part=wantjank5-swangle api=33 gpu=swangle_indirect emulator-step=success |

## look5-swangle

```
part look5-swangle; APK: 82e0251 (Проверка v1.2.26: пети кръг — хоризонтална матрица, jank на „Искам да прочета“ с); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## L0. First launch and warm-up
first-install catalogue results after 2661 ms
## L5. Landscape
landscape: 2400x1080
landscape check: mRotation=ROTATION_90} cur=2400x1080
matrix landscape: news title OK; segments News[checked=false selected=false bounds=[508,243][802,369]] Calendar[checked=true selected=false bounds=[802,243][1099,369]] subscribe-action=visible
landscape rail labels: home catalog my news more 
## COMPLETE
## logcat findings
app pids seen: ['5388', '7136']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=51 total=99
== Davey: 1
   davey events=1 max=1157ms median=1157ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## wantjank5-swangle

```
part wantjank5-swangle; APK: 82e0251 (Проверка v1.2.26: пети кръг — хоризонтална матрица, jank на „Искам да прочета“ с); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 3783 ms
Want to read: added 16 books by inventory-number links (inv 1..16)
## 3w. Jank of the Want to read list at 540x1200 (animations ON)
want list: 1 rows visible; --- screen: text="Availability as of Oct 17, 2026" text="Swipe a book aside to remove it." content-desc="Дъхът на пролетта" text="Дъхът на пролетта" text="Пламен Иванов" text="Available" 
GFX want-lowres  frames=   69  janky=65 (94.20%)  p50=200ms p90=250ms p95=300ms p99=300ms  slowUI=37 slowDraw=65 highInput=6
## COMPLETE
## logcat findings
app pids seen: ['5106']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=58 total=137
== Davey: 3
   davey events=3 max=1429ms median=1016ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### look5-swangle/shots/landscape

- [look5-swangle/shots/landscape/01-home.png](look5-swangle/shots/landscape/01-home.png)
- [look5-swangle/shots/landscape/02-catalog.png](look5-swangle/shots/landscape/02-catalog.png)
- [look5-swangle/shots/landscape/03-news.png](look5-swangle/shots/landscape/03-news.png)
- [look5-swangle/shots/landscape/04-calendar.png](look5-swangle/shots/landscape/04-calendar.png)
- [look5-swangle/shots/landscape/05-book.png](look5-swangle/shots/landscape/05-book.png)

### wantjank5-swangle/shots/jank

- [wantjank5-swangle/shots/jank/want-lowres.png](wantjank5-swangle/shots/jank/want-lowres.png)

## Other files

- [look5-swangle/host-after-run.txt](look5-swangle/host-after-run.txt)
- [look5-swangle/job-status.txt](look5-swangle/job-status.txt)
- [look5-swangle/logcat-all.txt](look5-swangle/logcat-all.txt)
- [look5-swangle/logcat-dump.txt](look5-swangle/logcat-dump.txt)
- [look5-swangle/logcat-findings.txt](look5-swangle/logcat-findings.txt)
- [look5-swangle/logcat-live.txt](look5-swangle/logcat-live.txt)
- [look5-swangle/summary.txt](look5-swangle/summary.txt)
- [wantjank5-swangle/gfx-want-lowres.txt](wantjank5-swangle/gfx-want-lowres.txt)
- [wantjank5-swangle/host-after-run.txt](wantjank5-swangle/host-after-run.txt)
- [wantjank5-swangle/job-status.txt](wantjank5-swangle/job-status.txt)
- [wantjank5-swangle/logcat-all.txt](wantjank5-swangle/logcat-all.txt)
- [wantjank5-swangle/logcat-dump.txt](wantjank5-swangle/logcat-dump.txt)
- [wantjank5-swangle/logcat-findings.txt](wantjank5-swangle/logcat-findings.txt)
- [wantjank5-swangle/logcat-live.txt](wantjank5-swangle/logcat-live.txt)
- [wantjank5-swangle/summary.txt](wantjank5-swangle/summary.txt)
