# v1.2.26 full emulator test (round 4: text size on the catalogue, Want to read jank, swangle_indirect)

Source: `debug/full-test-2` @ a8615e9 (app = origin/main 3df7f37), run 40538700043 (attempt 1), 2026-10-27T16:25:09Z.
Emulators: google_apis x86_64 API 33, pixel_6 profile; parts named *-swangle use -gpu swangle_indirect, the rest swiftshader_indirect.
Round 3: [../run3/index.md](../run3/index.md); round 2: [../index.md](../index.md); round 1: [../run1/index.md](../run1/index.md).

| part | status |
|---|---|
| func4-swangle | COMPLETE; part=func4-swangle api=33 gpu=swangle_indirect emulator-step=success |
| wantjank-swangle | COMPLETE; part=wantjank-swangle api=33 gpu=swangle_indirect emulator-step=success |

## func4-swangle

```
part func4-swangle; APK: a8615e9 (Проверка v1.2.26: четвърти кръг — „Много голям“ текст в каталога, jank на „Искам); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 3469 ms
## F4. Settings → Text size → Extra large
text size options: text="Text size" text="Multiplies the font size set on your phone. In articles, the “Aa” button adjusts it further." text="Small" text="Standard" text="Large" text="Extra large" text="Close" 
TEXT SIZE now: text="Extra large" 
xlarge catalogue: text="Search everything" content-desc="Scan barcode" text="Everything" text="Title" text="Author" text="ISBN" text="2446 results  ·  InvLib data as of Oct 17, 2026" text="Джиан, Филип" 
TEXT SIZE reset: text="Standard" 
## COMPLETE
## logcat findings
app pids seen: ['4843']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 4
   skipped-frames events=4 max=108 total=230
== Davey: 16
   davey events=16 max=2558ms median=840ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## wantjank-swangle

```
part wantjank-swangle; APK: a8615e9 (Проверка v1.2.26: четвърти кръг — „Много голям“ текст в каталога, jank на „Искам); x86_64 libs: 4 size 4924210 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420
installed:  versionCode=1 minSdk=26 targetSdk=37  versionName=1.0.0 
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED|USER_SENSITIVE_WHEN_GRANTED|USER_SENSITIVE_WHEN_DENIED]
screen 1080x2400
## 0. First launch and warm-up
first-install catalogue results after 2876 ms
Want to read: added 6 books for the list jank
## 3w. Jank of the Want to read list at 540x1200 (animations ON)
want list: 1 rows visible; --- screen: text="Availability as of Oct 17, 2026" text="Swipe a book aside to remove it." content-desc="Далечният изгрева" text="Далечният изгрева" text="Даниела Георгиева" text="Available" 
GFX want-lowres  frames=    2  janky=2 (100.00%)  p50=40ms p90=40ms p95=40ms p99=40ms  slowUI=0 slowDraw=2 highInput=0
## COMPLETE
## logcat findings
app pids seen: ['4871', '6451']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 7
   skipped-frames events=7 max=62 total=278
== Davey: 28
   davey events=28 max=1761ms median=827ms
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### func4-swangle/shots/func

- [func4-swangle/shots/func/12-text-size-dialog.png](func4-swangle/shots/func/12-text-size-dialog.png)
- [func4-swangle/shots/func/13-settings-xlarge.png](func4-swangle/shots/func/13-settings-xlarge.png)
- [func4-swangle/shots/func/14-home-xlarge.png](func4-swangle/shots/func/14-home-xlarge.png)
- [func4-swangle/shots/func/15-catalog-xlarge.png](func4-swangle/shots/func/15-catalog-xlarge.png)
- [func4-swangle/shots/func/15b-news-xlarge.png](func4-swangle/shots/func/15b-news-xlarge.png)
- [func4-swangle/shots/func/16-settings-reset.png](func4-swangle/shots/func/16-settings-reset.png)

### wantjank-swangle/shots/jank

- [wantjank-swangle/shots/jank/want-lowres.png](wantjank-swangle/shots/jank/want-lowres.png)

## Other files

- [func4-swangle/host-after-run.txt](func4-swangle/host-after-run.txt)
- [func4-swangle/job-status.txt](func4-swangle/job-status.txt)
- [func4-swangle/logcat-all.txt](func4-swangle/logcat-all.txt)
- [func4-swangle/logcat-dump.txt](func4-swangle/logcat-dump.txt)
- [func4-swangle/logcat-findings.txt](func4-swangle/logcat-findings.txt)
- [func4-swangle/logcat-live.txt](func4-swangle/logcat-live.txt)
- [func4-swangle/summary.txt](func4-swangle/summary.txt)
- [wantjank-swangle/gfx-want-lowres.txt](wantjank-swangle/gfx-want-lowres.txt)
- [wantjank-swangle/host-after-run.txt](wantjank-swangle/host-after-run.txt)
- [wantjank-swangle/job-status.txt](wantjank-swangle/job-status.txt)
- [wantjank-swangle/logcat-all.txt](wantjank-swangle/logcat-all.txt)
- [wantjank-swangle/logcat-dump.txt](wantjank-swangle/logcat-dump.txt)
- [wantjank-swangle/logcat-findings.txt](wantjank-swangle/logcat-findings.txt)
- [wantjank-swangle/logcat-live.txt](wantjank-swangle/logcat-live.txt)
- [wantjank-swangle/summary.txt](wantjank-swangle/summary.txt)
