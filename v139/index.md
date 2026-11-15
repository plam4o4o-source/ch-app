# Emulator check of the v1.2.39 candidate (696e57d) against the released v1.2.38 (f57962b)

## Findings

(written after the runs)

## Runs and parts

debug/w26-check (runs in runs.txt, latest 43711037340 @ afb71c5). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU) unless the part name ends in 26 (API 26) or 36x (API 36). prodRelease (minified) x86_64 builds with a throwaway key: new = the 1.2.39 candidate (org.chyavorec.app), base = 1.2.38 as released (org.chyavorec.app.b38, side by side for the A/B), base-upd = 1.2.38 as org.chyavorec.app (the update test). No app-code changes on the debug branch. Screenshots are JPEG, scaled down (sheets/ = contact sheets). The earlier checks: [../v138b/index.md](../v138b/index.md), [../v138/index.md](../v138/index.md).

| part | status |
|---|---|
| fr33a | COMPLETE; part=fr33a target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fr33b | COMPLETE; part=fr33b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fr36x | COMPLETE; part=fr36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| smk26 | incomplete; part=smk26 target=google_apis api=26 gpu=swangle_indirect emulator-step=failure |
| smk33 | incomplete; part=smk33 target=google_apis api=33 gpu=swangle_indirect emulator-step=failure |
| smk36x | incomplete; part=smk36x target=google_apis api=36 gpu=swangle_indirect emulator-step=failure |
| upd33 | COMPLETE; part=upd33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100
1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100
1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200
app.apk 4681342 bytes
app-base.apk 4641014 bytes
app-base-upd.apk 4641014 bytes
```

## fr33a

```
part fr33a; APK: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
base APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100 size 4641014 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
installed:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## F0. The candidate 1.2.39 (org.chyavorec.app) and 1.2.38 (org.chyavorec.app.b38) side by side, both compiled speed-profile
X installed new: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200;  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 3566 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~NxoF6TWc0QlsCq25nh909w==/org.chyavorec.app-_ggznyAD_FsxgWK92lY89g==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 2699 ms
X compile org.chyavorec.app.b38: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~-6DwBZxxfPeobHnqzl-9mw==/org.chyavorec.app.b38-S2uuoqDTEC49mh54jp-fKw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
animations: animator_duration_scale=1 transition=1 window=1; host load: 4.87 2.83 1.20
## F1. Warm-up (rep 0, not counted): every scenario once; the quiz taps are learned
  FS home new rep0: n=86 (+0 flagged) total p50=322 p90=438 p99=492 max=534 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=131.4 (sync 0.1 issue 36.3 swap 87.6) start-delay=93.5 queue=101.7
  FS more new rep0: n=101 (+0 flagged) total p50=297 p90=365 p99=402 max=445 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=111.0 (sync 0.1 issue 38.1 swap 68.1) start-delay=86.3 queue=94.2
  FS timeline new rep0: n=120 (+0 flagged) BUFFER-FULL total p50=241 p90=305 p99=349 max=402 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=93.3 (sync 0.1 issue 35.6 swap 55.8) start-delay=67.5 queue=76.3
QUIZ taps new: 5 questions learned: input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 580; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 580; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
  FS home base rep0: n=77 (+0 flagged) total p50=333 p90=449 p99=538 max=552 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=119.2 (sync 0.5 issue 36.6 swap 79.9) start-delay=101.7 queue=108.0
  FS more base rep0: n=87 (+7 flagged) total p50=321 p90=417 p99=619 max=733 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=121.8 (sync 0.1 issue 45.8 swap 73.2) start-delay=75.6 queue=84.1
  FS timeline base rep0: n=97 (+0 flagged) total p50=310 p90=432 p99=479 max=511 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=127.1 (sync 0.1 issue 40.4 swap 68.3) start-delay=85.6 queue=89.1
QUIZ taps base: 5 questions learned: input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
## F2. Repetition 1, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep1: n=86 (+0 flagged) total p50=285 p90=417 p99=509 max=545 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=109.0 (sync 0.1 issue 33.7 swap 67.4) start-delay=79.3 queue=88.3
  FS more new rep1: n=105 (+0 flagged) total p50=274 p90=337 p99=362 max=382 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=102.4 (sync 0.1 issue 34.2 swap 65.8) start-delay=79.8 queue=91.9
  FS timeline new rep1: n=120 (+0 flagged) BUFFER-FULL total p50=242 p90=302 p99=357 max=379 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=94.6 (sync 0.1 issue 34.8 swap 55.8) start-delay=67.3 queue=74.6
  FS quiz new rep1: n=74 (+43 flagged) total p50=127 p90=245 p99=267 max=269 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=64.1 (sync 0.1 issue 32.5 swap 31.9) start-delay=5.7 queue=50.4
  FS home base rep1: n=96 (+0 flagged) total p50=330 p90=397 p99=480 max=493 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=128.4 (sync 0.1 issue 35.7 swap 78.2) start-delay=90.4 queue=105.5
  FS more base rep1: n=101 (+2 flagged) total p50=298 p90=372 p99=418 max=472 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=116.8 (sync 0.1 issue 36.7 swap 72.7) start-delay=81.7 queue=96.9
  FS timeline base rep1: n=106 (+0 flagged) total p50=261 p90=375 p99=450 max=455 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=105.3 (sync 0.1 issue 37.4 swap 58.1) start-delay=71.2 queue=78.9
  FS quiz base rep1: n=111 (+7 flagged) total p50=344 p90=457 p99=495 max=532 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=148.1 (sync 0.1 issue 41.5 swap 85.4) start-delay=95.1 queue=114.5
## F2. Repetition 2, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep2: n=83 (+0 flagged) total p50=313 p90=377 p99=459 max=501 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=115.2 (sync 0.1 issue 34.5 swap 75.1) start-delay=94.1 queue=98.6
  FS more base rep2: n=93 (+0 flagged) total p50=303 p90=361 p99=383 max=418 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=106.0 (sync 0.1 issue 34.5 swap 70.8) start-delay=84.5 queue=98.0
  FS timeline base rep2: n=105 (+0 flagged) total p50=271 p90=347 p99=416 max=483 ms | medians: ui=0.4 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=101.3 (sync 0.1 issue 37.6 swap 58.3) start-delay=79.3 queue=85.2
  FS quiz base rep2: n=109 (+8 flagged) total p50=347 p90=440 p99=590 max=596 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=146.6 (sync 0.1 issue 40.1 swap 83.8) start-delay=86.9 queue=108.5
  FS home new rep2: n=83 (+0 flagged) total p50=302 p90=434 p99=480 max=493 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=123.0 (sync 0.1 issue 36.5 swap 86.3) start-delay=83.8 queue=92.0
  FS more new rep2: n=112 (+0 flagged) total p50=266 p90=324 p99=413 max=418 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=100.5 (sync 0.1 issue 34.2 swap 63.3) start-delay=75.9 queue=89.7
  FS timeline new rep2: n=120 (+0 flagged) BUFFER-FULL total p50=242 p90=295 p99=366 max=369 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=92.8 (sync 0.1 issue 34.9 swap 56.2) start-delay=67.4 queue=77.2
  FS quiz new rep2: n=79 (+41 flagged) BUFFER-FULL total p50=150 p90=227 p99=292 max=304 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=73.0 (sync 0.1 issue 35.8 swap 35.9) start-delay=5.5 queue=55.9
## F2. Repetition 3, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep3: n=98 (+0 flagged) total p50=285 p90=379 p99=477 max=503 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=102.9 (sync 0.1 issue 35.4 swap 66.6) start-delay=77.6 queue=88.5
  FS more new rep3: n=97 (+0 flagged) total p50=283 p90=342 p99=385 max=421 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=105.7 (sync 0.1 issue 35.7 swap 66.9) start-delay=80.1 queue=94.5
  FS timeline new rep3: n=119 (+0 flagged) total p50=199 p90=301 p99=345 max=405 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=84.1 (sync 0.1 issue 33.4 swap 49.7) start-delay=54.7 queue=60.0
  FS quiz new rep3: n=73 (+47 flagged) BUFFER-FULL total p50=139 p90=233 p99=290 max=300 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=64.8 (sync 0.1 issue 32.3 swap 32.8) start-delay=4.9 queue=47.0
  FS home base rep3: n=59 (+0 flagged) total p50=302 p90=406 p99=505 max=517 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=113.4 (sync 0.1 issue 35.3 swap 76.4) start-delay=83.1 queue=99.3
  FS more base rep3: n=100 (+0 flagged) total p50=316 p90=384 p99=446 max=461 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=120.4 (sync 0.1 issue 45.0 swap 75.8) start-delay=88.2 queue=104.0
  FS timeline base rep3: n=92 (+0 flagged) total p50=317 p90=433 p99=495 max=523 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=134.9 (sync 0.1 issue 37.8 swap 76.7) start-delay=92.1 queue=102.7
  FS quiz base rep3: n=108 (+8 flagged) total p50=373 p90=460 p99=546 max=567 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=154.7 (sync 0.1 issue 40.4 swap 95.5) start-delay=95.1 queue=111.6
## F2. Repetition 4, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep4: n=88 (+0 flagged) total p50=325 p90=403 p99=455 max=489 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=122.4 (sync 0.1 issue 37.1 swap 75.9) start-delay=98.5 queue=104.5
  FS more base rep4: n=95 (+0 flagged) total p50=321 p90=378 p99=440 max=457 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=116.6 (sync 0.1 issue 38.6 swap 73.3) start-delay=86.7 queue=96.5
  FS timeline base rep4: n=104 (+0 flagged) total p50=300 p90=389 p99=477 max=500 ms | medians: ui=0.4 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=121.0 (sync 0.1 issue 36.0 swap 77.4) start-delay=75.6 queue=90.0
  FS quiz base rep4: n=108 (+10 flagged) total p50=353 p90=449 p99=496 max=550 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=149.5 (sync 0.1 issue 40.2 swap 85.3) start-delay=86.2 queue=105.5
  FS home new rep4: n=92 (+0 flagged) total p50=352 p90=437 p99=531 max=536 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=137.9 (sync 0.1 issue 37.8 swap 90.8) start-delay=94.2 queue=101.9
  FS more new rep4: n=104 (+0 flagged) total p50=270 p90=335 p99=388 max=402 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=100.9 (sync 0.1 issue 35.4 swap 63.4) start-delay=71.9 queue=85.8
  FS timeline new rep4: n=120 (+0 flagged) BUFFER-FULL total p50=233 p90=305 p99=348 max=385 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=91.2 (sync 0.1 issue 34.1 swap 52.1) start-delay=56.9 queue=70.3
  FS quiz new rep4: n=74 (+46 flagged) BUFFER-FULL total p50=145 p90=234 p99=266 max=283 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=64.5 (sync 0.1 issue 33.1 swap 32.0) start-delay=9.1 queue=53.8
## F2. Repetition 5, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep5: n=99 (+0 flagged) total p50=272 p90=376 p99=429 max=473 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=109.0 (sync 0.1 issue 35.8 swap 67.3) start-delay=68.8 queue=71.5
  FS more new rep5: n=99 (+0 flagged) total p50=282 p90=343 p99=377 max=382 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=104.2 (sync 0.1 issue 36.2 swap 65.2) start-delay=81.4 queue=91.5
  FS timeline new rep5: n=120 (+0 flagged) BUFFER-FULL total p50=222 p90=298 p99=337 max=341 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=87.6 (sync 0.1 issue 34.5 swap 52.7) start-delay=58.9 queue=72.4
  FS quiz new rep5: n=73 (+46 flagged) total p50=150 p90=231 p99=262 max=272 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=67.8 (sync 0.1 issue 33.2 swap 34.4) start-delay=5.2 queue=52.2
  FS home base rep5: n=80 (+0 flagged) total p50=312 p90=389 p99=442 max=466 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=113.6 (sync 0.1 issue 34.2 swap 71.9) start-delay=89.0 queue=98.9
  FS more base rep5: n=104 (+0 flagged) total p50=302 p90=369 p99=415 max=426 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=114.7 (sync 0.1 issue 36.0 swap 71.6) start-delay=86.2 queue=93.8
  FS timeline base rep5: n=94 (+0 flagged) total p50=262 p90=380 p99=469 max=500 ms | medians: ui=0.6 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=111.8 (sync 0.1 issue 35.1 swap 61.8) start-delay=59.0 queue=77.5
  FS quiz base rep5: n=108 (+8 flagged) total p50=360 p90=445 p99=579 max=590 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=152.3 (sync 0.1 issue 39.3 swap 87.4) start-delay=86.2 queue=112.6
## F2. Repetition 6, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep6: n=92 (+0 flagged) total p50=320 p90=403 p99=461 max=511 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=118.2 (sync 0.1 issue 34.9 swap 74.8) start-delay=94.4 queue=102.9
  FS more base rep6: n=104 (+0 flagged) total p50=306 p90=364 p99=386 max=396 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=117.5 (sync 0.1 issue 36.4 swap 72.5) start-delay=77.1 queue=94.2
  FS timeline base rep6: n=112 (+0 flagged) total p50=282 p90=395 p99=468 max=481 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=113.4 (sync 0.1 issue 35.9 swap 66.3) start-delay=73.0 queue=83.5
  FS quiz base rep6: n=106 (+7 flagged) total p50=362 p90=474 p99=576 max=591 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=142.3 (sync 0.1 issue 41.6 swap 88.2) start-delay=92.5 queue=116.7
  FS home new rep6: n=79 (+0 flagged) total p50=336 p90=416 p99=465 max=490 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=130.0 (sync 0.1 issue 37.2 swap 88.7) start-delay=103.0 queue=121.2
  FS more new rep6: n=99 (+0 flagged) total p50=291 p90=353 p99=388 max=396 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=108.8 (sync 0.1 issue 35.7 swap 67.1) start-delay=81.2 queue=92.9
  FS timeline new rep6: n=119 (+0 flagged) total p50=219 p90=303 p99=331 max=361 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=87.9 (sync 0.1 issue 33.4 swap 52.7) start-delay=54.3 queue=64.4
  FS quiz new rep6: n=79 (+41 flagged) BUFFER-FULL total p50=154 p90=237 p99=273 max=286 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=65.9 (sync 0.1 issue 33.4 swap 33.9) start-delay=4.2 queue=49.8
## F3. Totals (rep 0 is excluded; new = 1.2.39 candidate, base = 1.2.38)
| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 6 | 86 | **316.6** (301.8..330.1) | 400 | 460 | 0.4 | 116.7 | 0.0 | 0.2 | 0.1 | 35.1 | 75.5 |
| home | new | 6 | 89 | **293.6** (271.7..352.4) | 417 | 479 | 0.4 | 116.0 | 0.0 | 0.2 | 0.1 | 36.2 | 76.8 |
| more | base | 6 | 100 | **304.4** (298.0..320.8) | 371 | 417 | 0.2 | 116.7 | 0.0 | 0.1 | 0.1 | 36.5 | 72.6 |
| more | new | 6 | 102 | **277.8** (266.2..291.4) | 340 | 386 | 0.2 | 103.3 | 0.0 | 0.1 | 0.1 | 35.6 | 65.5 |
| quiz | base | 6 | 108 | **356.7** (344.0..373.0) | 453 | 561 | 0.4 | 148.8 | 0.0 | 0.3 | 0.1 | 40.3 | 86.4 |
| quiz | new | 6 | 74 | **147.4** (127.4..153.7) | 234 | 270 | 0.3 | 65.4 | 0.0 | 0.2 | 0.1 | 33.2 | 33.3 |
| timeline | base | 6 | 104 | **276.4** (261.3..316.6) | 384 | 468 | 0.4 | 112.6 | 0.0 | 0.1 | 0.1 | 36.7 | 64.0 |
| timeline | new | 6 | 120 | **227.5** (198.9..242.2) | 302 | 346 | 0.4 | 89.6 | 0.0 | 0.1 | 0.1 | 34.3 | 52.7 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| home | base | **10670** (7186..12311) | 86 (59..96) | **116.7** (113.4..128.4) | 6753 | 3912 |
| home | new | **11020** (10382..13110) | 89 (79..99) | **116.0** (102.9..137.9) | 6730 | 4121 |
| more | base | **12096** (10660..12699) | 100 (93..104) | **116.7** (106.0..120.4) | 7224 | 4845 |
| more | new | **10832** (10389..11437) | 102 (97..112) | **103.3** (100.5..108.8) | 6555 | 4233 |
| quiz | base | **16222** (15953..16749) | 108 (106..111) | **148.8** (142.4..154.7) | 10590 | 5718 |
| quiz | new | **5257** (5181..6125) | 74 (73..79) | **65.4** (64.1..73.0) | 2827 | 2524 |
| timeline | base | **12208** (11042..13373) | 104 (92..112) | **112.6** (101.3..134.9) | 7317 | 4693 |
| timeline | new | **10679** (9893..11391) | 120 (119..120) | **89.6** (84.1..94.6) | 6019 | 4462 |

- home / new vs base, sum of RT: 11019.7 vs 10670.3 (+3%); U=21.0, p=0.699; ranges overlap
- home / new vs base, median RT per frame: 116.0 vs 116.7 (-1%); U=17.0, p=0.937; ranges overlap
- more / new vs base, sum of RT: 10832.5 vs 12096.3 (-10%); U=4.0, p=0.026; ranges overlap
- more / new vs base, median RT per frame: 103.3 vs 116.7 (-11%); U=1.0, p=0.004; ranges overlap
- quiz / new vs base, sum of RT: 5257.5 vs 16222.4 (-68%); U=0.0, p=0.002; ranges do NOT overlap
- quiz / new vs base, median RT per frame: 65.4 vs 148.8 (-56%); U=0.0, p=0.002; ranges do NOT overlap
- timeline / new vs base, sum of RT: 10679.4 vs 12207.6 (-13%); U=3.0, p=0.015; ranges overlap
- timeline / new vs base, median RT per frame: 89.6 vs 112.6 (-20%); U=0.0, p=0.002; ranges do NOT overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [330.1, 313.1, 301.8, 325.2, 312.2, 320.0]
- home / new: [284.8, 302.1, 285.0, 352.4, 271.7, 336.5] vs base: median diff -23.0 ms (UI -0.0, RT -0.7, issue +1.1); Mann-Whitney U=13.0, p=0.485; ranges overlap
- more / base: [298.0, 302.6, 315.7, 320.8, 301.6, 306.1]
- more / new: [273.6, 266.2, 283.4, 269.9, 282.0, 291.4] vs base: median diff -26.6 ms (UI +0.0, RT -13.4, issue -0.9); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
- quiz / base: [344.0, 346.6, 373.0, 353.0, 360.5, 361.8]
- quiz / new: [127.4, 150.1, 139.3, 144.8, 150.1, 153.7] vs base: median diff -209.3 ms (UI -0.1, RT -83.4, issue -7.2); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
- timeline / base: [261.3, 271.2, 316.6, 300.3, 261.8, 281.7]
- timeline / new: [242.2, 241.8, 198.9, 232.9, 222.2, 219.1] vs base: median diff -48.9 ms (UI -0.1, RT -23.0, issue -2.4); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
FR part: FATAL lines 0, ANR 0; host load: 1.94 2.75 2.89
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['6142', '8100', '10816', '14169', '15206', '18417', '19432', '22519']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 24
   skipped-frames events=24 max=77 total=1211
== Davey: 36
   davey events=36 max=2142ms median=1062ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## fr33b

```
part fr33b; APK: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
base APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100 size 4641014 bytes
host: 4 cpu, 15700 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
installed:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## F0. The candidate 1.2.39 (org.chyavorec.app) and 1.2.38 (org.chyavorec.app.b38) side by side, both compiled speed-profile
X installed new: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200;  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 3521 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~nGVjqtZ0ZCbkWLw5SWxXbw==/org.chyavorec.app-RUDCjPDvy3Lm3ooiElZh5Q==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 3782 ms
X compile org.chyavorec.app.b38: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~VbeKA8FC_eTjZ3h-z_LxSA==/org.chyavorec.app.b38-6LDvb95S2mllLN9WW3gI6Q==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
animations: animator_duration_scale=1 transition=1 window=1; host load: 4.83 3.02 1.29
## F1. Warm-up (rep 0, not counted): every scenario once; the quiz taps are learned
  FS home new rep0: n=77 (+0 flagged) total p50=422 p90=542 p99=621 max=633 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=162.8 (sync 0.1 issue 46.6 swap 110.4) start-delay=121.3 queue=134.1
  FS more new rep0: n=54 (+2 flagged) total p50=323 p90=413 p99=519 max=547 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=125.3 (sync 0.1 issue 46.2 swap 77.7) start-delay=95.6 queue=106.4
  FS timeline new rep0: n=111 (+0 flagged) total p50=279 p90=378 p99=526 max=553 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=111.7 (sync 0.1 issue 44.3 swap 64.3) start-delay=73.4 queue=84.6
QUIZ taps new: 5 questions learned: input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 580; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 580; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
  FS home base rep0: n=68 (+0 flagged) total p50=431 p90=521 p99=624 max=629 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=180.3 (sync 0.1 issue 71.0 swap 94.5) start-delay=100.3 queue=130.8
  FS more base rep0: n=89 (+0 flagged) total p50=355 p90=460 p99=502 max=512 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=143.3 (sync 0.1 issue 58.1 swap 83.4) start-delay=104.9 queue=120.8
  FS timeline base rep0: n=91 (+0 flagged) total p50=332 p90=460 p99=564 max=570 ms | medians: ui=0.4 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=147.0 (sync 0.1 issue 52.9 swap 71.0) start-delay=83.8 queue=98.5
QUIZ taps base: 5 questions learned: input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
## F2. Repetition 1, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep1: n=91 (+1 flagged) total p50=375 p90=506 p99=647 max=689 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=131.0 (sync 0.1 issue 47.1 swap 85.6) start-delay=86.9 queue=95.3
  FS more base rep1: n=87 (+1 flagged) total p50=331 p90=441 p99=500 max=523 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=134.4 (sync 0.1 issue 45.6 swap 82.6) start-delay=85.6 queue=100.8
  FS timeline base rep1: n=95 (+0 flagged) total p50=429 p90=542 p99=596 max=606 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=163.1 (sync 0.1 issue 55.7 swap 103.6) start-delay=119.7 queue=126.2
  FS quiz base rep1: n=101 (+5 flagged) total p50=423 p90=551 p99=600 max=683 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=186.3 (sync 0.1 issue 53.1 swap 109.6) start-delay=108.6 queue=140.6
  FS home new rep1: n=83 (+0 flagged) total p50=407 p90=549 p99=600 max=613 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=162.7 (sync 0.1 issue 57.2 swap 104.4) start-delay=105.5 queue=108.3
  FS more new rep1: n=73 (+0 flagged) total p50=329 p90=407 p99=448 max=457 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=125.2 (sync 0.1 issue 45.1 swap 75.9) start-delay=91.7 queue=100.2
  FS timeline new rep1: n=110 (+0 flagged) total p50=243 p90=359 p99=434 max=452 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=100.9 (sync 0.1 issue 42.2 swap 45.6) start-delay=65.5 queue=79.0
  FS quiz new rep1: n=78 (+40 flagged) total p50=166 p90=292 p99=331 max=333 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=78.6 (sync 0.1 issue 42.5 swap 37.4) start-delay=13.1 queue=65.9
## F2. Repetition 2, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep2: n=80 (+0 flagged) total p50=396 p90=541 p99=586 max=596 ms | medians: ui=0.2 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=159.1 (sync 0.1 issue 47.5 swap 110.0) start-delay=102.8 queue=114.0
  FS more new rep2: n=84 (+3 flagged) total p50=324 p90=414 p99=539 max=598 ms | medians: ui=0.1 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=120.9 (sync 0.0 issue 44.9 swap 75.5) start-delay=88.3 queue=93.8
  FS timeline new rep2: n=102 (+0 flagged) total p50=253 p90=385 p99=443 max=467 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=112.4 (sync 0.1 issue 44.1 swap 63.9) start-delay=69.6 queue=85.3
  FS quiz new rep2: n=82 (+38 flagged) BUFFER-FULL total p50=163 p90=272 p99=344 max=366 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=77.5 (sync 0.1 issue 42.6 swap 36.9) start-delay=5.2 queue=65.2
  FS home base rep2: n=89 (+0 flagged) total p50=403 p90=523 p99=575 max=578 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=165.4 (sync 0.1 issue 46.1 swap 93.2) start-delay=103.2 queue=124.2
  FS more base rep2: n=58 (+3 flagged) total p50=351 p90=457 p99=658 max=730 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=147.5 (sync 0.1 issue 50.5 swap 82.8) start-delay=100.2 queue=90.8
  FS timeline base rep2: n=91 (+0 flagged) total p50=351 p90=471 p99=540 max=626 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=138.2 (sync 0.1 issue 49.2 swap 73.5) start-delay=93.6 queue=107.3
  FS quiz base rep2: n=99 (+5 flagged) total p50=439 p90=567 p99=581 max=583 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=193.8 (sync 0.1 issue 53.3 swap 104.4) start-delay=103.1 queue=135.4
## F2. Repetition 3, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep3: n=81 (+0 flagged) total p50=393 p90=528 p99=635 max=674 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=157.1 (sync 0.1 issue 50.5 swap 92.9) start-delay=98.0 queue=124.8
  FS more base rep3: n=55 (+2 flagged) total p50=350 p90=474 p99=576 max=580 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=147.2 (sync 0.1 issue 50.0 swap 84.8) start-delay=93.4 queue=100.9
  FS timeline base rep3: n=73 (+0 flagged) total p50=428 p90=521 p99=586 max=594 ms | medians: ui=0.4 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=162.1 (sync 0.1 issue 50.4 swap 105.9) start-delay=110.4 queue=113.7
  FS quiz base rep3: n=100 (+6 flagged) total p50=450 p90=566 p99=617 max=633 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=197.4 (sync 0.1 issue 55.6 swap 134.8) start-delay=108.6 queue=131.6
  FS home new rep3: n=79 (+0 flagged) total p50=379 p90=528 p99=623 max=662 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=151.0 (sync 0.1 issue 51.2 swap 96.7) start-delay=92.7 queue=112.7
  FS more new rep3: n=70 (+4 flagged) total p50=347 p90=428 p99=537 max=569 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=128.5 (sync 0.1 issue 45.7 swap 76.6) start-delay=84.6 queue=90.7
  FS timeline new rep3: n=114 (+0 flagged) total p50=275 p90=367 p99=416 max=446 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=106.7 (sync 0.1 issue 45.0 swap 60.8) start-delay=80.8 queue=89.2
  FS quiz new rep3: n=84 (+33 flagged) total p50=175 p90=288 p99=349 max=379 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=84.4 (sync 0.1 issue 43.9 swap 37.7) start-delay=4.7 queue=67.2
## F2. Repetition 4, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep4: n=73 (+0 flagged) total p50=410 p90=539 p99=610 max=652 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=163.0 (sync 0.1 issue 49.4 swap 111.7) start-delay=123.4 queue=134.3
  FS more new rep4: n=93 (+0 flagged) total p50=335 p90=424 p99=483 max=505 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=126.2 (sync 0.1 issue 46.5 swap 78.7) start-delay=98.1 queue=108.2
  FS timeline new rep4: n=101 (+0 flagged) total p50=297 p90=401 p99=497 max=583 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=117.5 (sync 0.1 issue 44.6 swap 69.3) start-delay=70.3 queue=81.3
  FS quiz new rep4: n=80 (+40 flagged) BUFFER-FULL total p50=172 p90=287 p99=336 max=345 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=85.3 (sync 0.1 issue 44.5 swap 38.2) start-delay=3.4 queue=65.4
  FS home base rep4: n=84 (+0 flagged) total p50=394 p90=517 p99=605 max=610 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=145.4 (sync 0.1 issue 45.4 swap 93.2) start-delay=120.7 queue=129.5
  FS more base rep4: n=80 (+0 flagged) total p50=367 p90=459 p99=493 max=519 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=137.3 (sync 0.1 issue 49.4 swap 86.3) start-delay=100.7 queue=109.5
  FS timeline base rep4: n=90 (+0 flagged) total p50=358 p90=462 p99=578 max=695 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=146.8 (sync 0.1 issue 53.4 swap 73.2) start-delay=92.4 queue=103.7
  FS quiz base rep4: n=99 (+4 flagged) total p50=467 p90=550 p99=617 max=668 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=191.5 (sync 0.1 issue 52.5 swap 125.3) start-delay=102.5 queue=135.4
## F2. Repetition 5, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep5: n=68 (+0 flagged) total p50=415 p90=489 p99=619 max=645 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=169.9 (sync 0.1 issue 50.3 swap 95.2) start-delay=122.0 queue=129.7
  FS more base rep5: n=73 (+1 flagged) total p50=375 p90=467 p99=609 max=675 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=137.2 (sync 0.1 issue 45.5 swap 87.2) start-delay=92.1 queue=104.2
  FS timeline base rep5: n=102 (+0 flagged) total p50=343 p90=451 p99=583 max=612 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=140.7 (sync 0.1 issue 51.0 swap 70.1) start-delay=91.3 queue=106.1
  FS quiz base rep5: n=101 (+5 flagged) total p50=437 p90=550 p99=600 max=677 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=191.8 (sync 0.1 issue 50.7 swap 108.3) start-delay=107.5 queue=130.9
  FS home new rep5: n=79 (+0 flagged) total p50=350 p90=467 p99=539 max=626 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=130.6 (sync 0.1 issue 46.6 swap 80.5) start-delay=101.6 queue=105.1
  FS more new rep5: n=88 (+0 flagged) total p50=362 p90=433 p99=472 max=476 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=131.7 (sync 0.1 issue 48.8 swap 79.4) start-delay=99.0 queue=110.7
  FS timeline new rep5: n=110 (+0 flagged) total p50=283 p90=378 p99=480 max=512 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=113.9 (sync 0.1 issue 44.9 swap 63.2) start-delay=82.1 queue=86.6
  FS quiz new rep5: n=81 (+39 flagged) BUFFER-FULL total p50=170 p90=288 p99=354 max=372 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=77.5 (sync 0.0 issue 42.3 swap 35.3) start-delay=9.1 queue=64.4
## F2. Repetition 6, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep6: n=70 (+0 flagged) total p50=394 p90=547 p99=615 max=654 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=167.5 (sync 0.1 issue 53.8 swap 107.4) start-delay=94.0 queue=113.1
  FS more new rep6: n=79 (+0 flagged) total p50=333 p90=416 p99=465 max=481 ms | medians: ui=0.1 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=128.9 (sync 0.1 issue 45.0 swap 75.4) start-delay=85.7 queue=99.1
  FS timeline new rep6: n=114 (+0 flagged) total p50=286 p90=365 p99=494 max=504 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=111.9 (sync 0.1 issue 45.1 swap 64.9) start-delay=68.3 queue=84.4
  FS quiz new rep6: n=82 (+38 flagged) BUFFER-FULL total p50=161 p90=275 p99=342 max=381 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=81.1 (sync 0.1 issue 43.1 swap 37.4) start-delay=4.9 queue=62.2
  FS home base rep6: n=77 (+0 flagged) total p50=393 p90=479 p99=594 max=637 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=142.1 (sync 0.1 issue 46.4 swap 91.0) start-delay=115.2 queue=127.3
  FS more base rep6: n=69 (+3 flagged) total p50=356 p90=472 p99=653 max=691 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=136.8 (sync 0.1 issue 48.1 swap 84.2) start-delay=103.8 queue=106.5
  FS timeline base rep6: n=84 (+0 flagged) total p50=325 p90=503 p99=582 max=632 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=142.5 (sync 0.1 issue 48.3 swap 68.5) start-delay=89.8 queue=106.3
  FS quiz base rep6: n=101 (+4 flagged) total p50=440 p90=581 p99=667 max=704 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=194.4 (sync 0.1 issue 52.4 swap 125.7) start-delay=101.6 queue=133.0
## F3. Totals (rep 0 is excluded; new = 1.2.39 candidate, base = 1.2.38)
| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 6 | 82 | **393.8** (375.4..415.1) | 512 | 612 | 0.5 | 151.2 | 0.0 | 0.2 | 0.1 | 46.7 | 93.0 |
| home | new | 6 | 79 | **394.9** (349.8..410.4) | 540 | 605 | 0.4 | 160.9 | 0.0 | 0.1 | 0.1 | 50.3 | 105.9 |
| more | base | 6 | 71 | **353.5** (331.0..375.4) | 463 | 592 | 0.3 | 137.3 | 0.0 | 0.1 | 0.1 | 48.7 | 84.5 |
| more | new | 6 | 82 | **333.9** (324.1..361.5) | 420 | 477 | 0.2 | 127.4 | 0.0 | 0.1 | 0.1 | 45.4 | 76.3 |
| quiz | base | 6 | 100 | **439.7** (423.4..466.9) | 559 | 609 | 0.4 | 192.8 | 0.0 | 0.3 | 0.1 | 52.8 | 117.4 |
| quiz | new | 6 | 82 | **168.5** (161.1..174.7) | 288 | 343 | 0.2 | 79.8 | 0.0 | 0.2 | 0.1 | 42.9 | 37.4 |
| timeline | base | 6 | 90 | **354.5** (325.0..429.1) | 487 | 583 | 0.5 | 144.7 | 0.0 | 0.1 | 0.1 | 50.7 | 73.3 |
| timeline | new | 6 | 110 | **278.7** (243.2..297.2) | 373 | 462 | 0.4 | 112.1 | 0.0 | 0.1 | 0.1 | 44.8 | 63.5 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| home | base | **13276** (11271..14618) | 82 (68..91) | **151.2** (131.0..169.9) | 7649 | 5249 |
| home | new | **12326** (11025..14080) | 79 (70..83) | **160.9** (130.6..167.5) | 7493 | 4829 |
| more | base | **10150** (8048..11739) | 71 (55..87) | **137.3** (134.4..147.5) | 5693 | 4423 |
| more | new | **10106** (9127..12410) | 82 (70..93) | **127.4** (120.9..131.7) | 6003 | 4130 |
| quiz | base | **18821** (18389..19648) | 100 (99..101) | **192.8** (186.3..197.4) | 12353 | 6550 |
| quiz | new | **7164** (6740..7655) | 82 (78..84) | **79.8** (77.5..85.3) | 3646 | 3499 |
| timeline | base | **13165** (12142..16348) | 90 (73..102) | **144.7** (138.2..163.2) | 7684 | 5602 |
| timeline | new | **12331** (11037..12804) | 110 (101..114) | **112.1** (100.9..117.5) | 6614 | 5564 |

- home / new vs base, sum of RT: 12325.5 vs 13276.4 (-7%); U=12.0, p=0.394; ranges overlap
- home / new vs base, median RT per frame: 160.9 vs 151.2 (+6%); U=20.0, p=0.818; ranges overlap
- more / new vs base, sum of RT: 10105.5 vs 10149.7 (-0%); U=22.0, p=0.589; ranges overlap
- more / new vs base, median RT per frame: 127.4 vs 137.3 (-7%); U=0.0, p=0.002; ranges do NOT overlap
- quiz / new vs base, sum of RT: 7164.1 vs 18821.1 (-62%); U=0.0, p=0.002; ranges do NOT overlap
- quiz / new vs base, median RT per frame: 79.8 vs 192.8 (-59%); U=0.0, p=0.002; ranges do NOT overlap
- timeline / new vs base, sum of RT: 12331.4 vs 13165.2 (-6%); U=4.0, p=0.026; ranges overlap
- timeline / new vs base, median RT per frame: 112.1 vs 144.7 (-22%); U=0.0, p=0.002; ranges do NOT overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [375.4, 403.4, 393.4, 394.2, 415.1, 392.8]
- home / new: [407.0, 395.6, 379.1, 410.4, 349.8, 394.1] vs base: median diff +1.1 ms (UI -0.1, RT +9.7, issue +3.5); Mann-Whitney U=18.0, p=1.000; ranges overlap
- more / base: [331.0, 351.0, 350.1, 366.6, 375.4, 356.1]
- more / new: [329.1, 324.1, 347.1, 335.3, 361.5, 332.6] vs base: median diff -19.6 ms (UI -0.1, RT -9.9, issue -3.3); Mann-Whitney U=7.0, p=0.093; ranges overlap
- quiz / base: [423.4, 439.3, 450.1, 466.9, 436.7, 440.0]
- quiz / new: [166.5, 162.5, 174.7, 171.9, 170.5, 161.1] vs base: median diff -271.2 ms (UI -0.2, RT -113.0, issue -9.9); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
- timeline / base: [429.1, 350.8, 428.5, 358.2, 342.6, 325.0]
- timeline / new: [243.2, 252.5, 274.5, 297.2, 282.8, 286.2] vs base: median diff -75.8 ms (UI -0.1, RT -32.5, issue -6.0); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
FR part: FATAL lines 1, ANR 0; host load: 2.39 3.25 3.33
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5469', '7909', '11876', '12919', '16038', '17045', '20098', '21146']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 29
   skipped-frames events=29 max=92 total=1462
== Davey: 38
   davey events=38 max=2582ms median=1129ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
```

## fr36x

```
part fr36x; APK: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
base APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100 size 4641014 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
installed:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## F0. The candidate 1.2.39 (org.chyavorec.app) and 1.2.38 (org.chyavorec.app.b38) side by side, both compiled speed-profile
X installed new: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200;  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 9347 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~aHwaKepsAtGDrJbSLZuU_Q==/org.chyavorec.app-oDG8u1S8IE1sd34Cuyb0IQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]|
X installed base: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 4442 ms
X compile org.chyavorec.app.b38: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~HHSHbv35GErP-BEhrn_Xcg==/org.chyavorec.app.b38-YN3jCAxgZt7QSmjd27-yVA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]|
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
animations: animator_duration_scale=1 transition=1 window=1; host load: 6.55 3.99 1.74
## F1. Warm-up (rep 0, not counted): every scenario once; the quiz taps are learned
  FS home new rep0: n=60 (+1 flagged) total p50=613 p90=995 p99=1193 max=1201 ms | medians: ui=2.0 (input 0.0 anim 0.9 layout 0.0 draw 0.3) rt=297.5 (sync 0.2 issue 145.5 swap 134.4) start-delay=180.3 queue=177.8
WARN: tab bar not reached
  FS more new rep0: n=68 (+1 flagged) total p50=566 p90=751 p99=947 max=1071 ms | medians: ui=0.6 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=249.0 (sync 0.1 issue 138.0 swap 124.8) start-delay=137.1 queue=156.9
WARN: tab bar not reached
MORE: tile 'Лента на времето|Timeline' not found: content-desc="Участие в Деветия празник на Кръвенишката баница" text="СЪБИТИЕ  ·  29 август 2026" text="Участие в Деветия празник на Кръвенишката баница" text="С много настроение и усмивки Клубът по народни танци при НЧ „Васил Левски – 1922“ – с. Яворец взе участие в Деветия празник на Кръвенишката 
  scenario timeline new rep0: not run
QUIZ taps new: 5 questions learned: input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 580; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 580; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 654; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
  FS home base rep0: n=62 (+0 flagged) total p50=577 p90=936 p99=1056 max=1068 ms | medians: ui=0.9 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=297.4 (sync 0.1 issue 135.4 swap 128.6) start-delay=147.6 queue=174.8
  FS more base rep0: n=49 (+5 flagged) total p50=643 p90=1258 p99=1739 max=1756 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=276.2 (sync 0.1 issue 158.3 swap 124.3) start-delay=171.3 queue=153.5
  FS timeline base rep0: n=72 (+0 flagged) total p50=570 p90=785 p99=914 max=929 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=256.7 (sync 0.1 issue 152.3 swap 111.6) start-delay=145.4 queue=152.1
QUIZ taps base: 5 questions learned: input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
## F2. Repetition 1, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep1: n=65 (+0 flagged) total p50=578 p90=785 p99=1211 max=1265 ms | medians: ui=0.6 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=306.7 (sync 0.1 issue 130.6 swap 127.9) start-delay=158.4 queue=147.9
WARN: tab bar not reached
  FS more new rep1: n=70 (+0 flagged) total p50=559 p90=742 p99=1058 max=1088 ms | medians: ui=0.6 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=243.0 (sync 0.1 issue 121.4 swap 121.9) start-delay=162.8 queue=149.7
WARN: tab bar not reached
MORE: tile 'Лента на времето|Timeline' not found: content-desc="Участие в Деветия празник на Кръвенишката баница" text="СЪБИТИЕ  ·  29 август 2026" text="Участие в Деветия празник на Кръвенишката баница" text="С много настроение и усмивки Клубът по народни танци при НЧ „Васил Левски – 1922“ – с. Яворец взе участие в Деветия празник на Кръвенишката 
  scenario timeline new rep1: not run
  FS quiz new rep1: n=91 (+16 flagged) total p50=298 p90=457 p99=537 max=583 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=167.6 (sync 0.1 issue 109.9 swap 55.2) start-delay=2.1 queue=108.7
  FS home base rep1: n=58 (+0 flagged) total p50=566 p90=900 p99=1113 max=1119 ms | medians: ui=0.7 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=301.7 (sync 0.1 issue 149.3 swap 141.1) start-delay=149.9 queue=156.8
  FS more base rep1: n=48 (+4 flagged) total p50=673 p90=1331 p99=1447 max=1490 ms | medians: ui=0.6 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=286.7 (sync 0.1 issue 152.8 swap 126.8) start-delay=175.9 queue=175.0
  FS timeline base rep1: n=65 (+0 flagged) total p50=526 p90=676 p99=883 max=919 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=239.8 (sync 0.1 issue 142.6 swap 97.4) start-delay=128.5 queue=142.8
  FS quiz base rep1: n=86 (+1 flagged) total p50=636 p90=792 p99=918 max=970 ms | medians: ui=0.7 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=288.2 (sync 0.1 issue 139.0 swap 136.7) start-delay=135.2 queue=208.8
## F2. Repetition 2, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep2: n=47 (+0 flagged) total p50=670 p90=953 p99=1078 max=1121 ms | medians: ui=1.2 (input 0.2 anim 0.7 layout 0.0 draw 0.2) rt=397.6 (sync 0.1 issue 218.2 swap 181.9) start-delay=86.5 queue=197.2
  FS more base rep2: n=53 (+0 flagged) total p50=590 p90=738 p99=824 max=841 ms | medians: ui=0.5 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=266.0 (sync 0.1 issue 139.9 swap 125.6) start-delay=128.7 queue=156.4
  FS timeline base rep2: n=77 (+0 flagged) total p50=544 p90=624 p99=762 max=798 ms | medians: ui=0.7 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=249.0 (sync 0.1 issue 135.3 swap 102.9) start-delay=136.2 queue=148.0
  FS quiz base rep2: n=90 (+1 flagged) total p50=673 p90=802 p99=886 max=929 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=290.4 (sync 0.1 issue 147.8 swap 134.0) start-delay=139.5 queue=197.5
  FS home new rep2: n=53 (+0 flagged) total p50=773 p90=1071 p99=1296 max=1308 ms | medians: ui=1.0 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=365.9 (sync 0.1 issue 197.8 swap 179.7) start-delay=185.5 queue=198.7
  FS more new rep2: n=48 (+2 flagged) total p50=571 p90=729 p99=1291 max=1342 ms | medians: ui=0.6 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=252.5 (sync 0.1 issue 136.9 swap 112.7) start-delay=119.8 queue=142.3
  FS timeline new rep2: n=79 (+0 flagged) total p50=336 p90=664 p99=682 max=685 ms | medians: ui=0.7 (input 0.0 anim 0.4 layout 0.0 draw 0.2) rt=160.8 (sync 0.1 issue 108.4 swap 76.4) start-delay=85.8 queue=99.2
  FS quiz new rep2: n=90 (+17 flagged) total p50=285 p90=456 p99=476 max=482 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=171.8 (sync 0.1 issue 114.6 swap 53.9) start-delay=1.6 queue=106.7
## F2. Repetition 3, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep3: n=63 (+1 flagged) total p50=586 p90=989 p99=1210 max=1228 ms | medians: ui=0.6 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=303.3 (sync 0.1 issue 177.6 swap 125.8) start-delay=165.4 queue=176.0
  FS more new rep3: n=64 (+0 flagged) total p50=560 p90=717 p99=763 max=781 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=252.1 (sync 0.1 issue 143.2 swap 121.0) start-delay=147.0 queue=163.2
  FS timeline new rep3: n=80 (+0 flagged) total p50=422 p90=652 p99=724 max=769 ms | medians: ui=0.8 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=175.4 (sync 0.1 issue 117.1 swap 91.4) start-delay=100.8 queue=103.9
  FS quiz new rep3: n=89 (+16 flagged) total p50=297 p90=454 p99=479 max=480 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=167.4 (sync 0.1 issue 113.6 swap 53.7) start-delay=2.4 queue=113.2
  FS home base rep3: n=62 (+0 flagged) total p50=602 p90=860 p99=1097 max=1176 ms | medians: ui=0.6 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=312.9 (sync 0.1 issue 146.5 swap 143.7) start-delay=140.3 queue=150.5
  FS more base rep3: n=61 (+0 flagged) total p50=580 p90=758 p99=807 max=822 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=275.9 (sync 0.1 issue 138.4 swap 127.1) start-delay=139.7 queue=161.0
  FS timeline base rep3: n=69 (+0 flagged) total p50=533 p90=613 p99=911 max=920 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=244.3 (sync 0.1 issue 131.2 swap 96.4) start-delay=115.4 queue=141.6
  FS quiz base rep3: n=89 (+1 flagged) total p50=678 p90=851 p99=1013 max=1071 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=309.2 (sync 0.1 issue 144.9 swap 135.8) start-delay=173.6 queue=197.4
## F2. Repetition 4, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep4: n=63 (+0 flagged) total p50=644 p90=963 p99=1140 max=1182 ms | medians: ui=0.8 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=318.7 (sync 0.1 issue 171.5 swap 150.6) start-delay=155.9 queue=163.0
WARN: tab bar not reached
  FS more base rep4: n=71 (+0 flagged) total p50=531 p90=712 p99=756 max=766 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=259.9 (sync 0.1 issue 136.9 swap 117.4) start-delay=126.0 queue=131.7
WARN: tab bar not reached
MORE: tile 'Лента на времето|Timeline' not found: text="45-ти общински фолклорен събор на народното творчество „От Тимок до Вита“, село Черни Вит." text="Този събор е много повече от културно събитие. Той е среща между поколенията, между различни краища на България, между хора, обединени от любовта към родното. Вече десетилетия наред тази сцена съб
  scenario timeline base rep4: not run
  FS quiz base rep4: n=90 (+0 flagged) total p50=671 p90=824 p99=898 max=1013 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=291.5 (sync 0.1 issue 145.6 swap 136.7) start-delay=146.1 queue=193.6
  FS home new rep4: n=65 (+0 flagged) total p50=592 p90=975 p99=1250 max=1268 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=271.1 (sync 0.1 issue 128.5 swap 128.0) start-delay=120.7 queue=141.6
WARN: tab bar not reached
  FS more new rep4: n=74 (+0 flagged) total p50=563 p90=697 p99=813 max=826 ms | medians: ui=0.4 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=254.9 (sync 0.1 issue 122.3 swap 116.1) start-delay=159.2 queue=166.8
  FS timeline new rep4: n=86 (+0 flagged) total p50=471 p90=666 p99=797 max=819 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=200.9 (sync 0.1 issue 119.8 swap 97.3) start-delay=122.3 queue=120.8
  FS quiz new rep4: n=91 (+16 flagged) total p50=295 p90=454 p99=504 max=528 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=167.3 (sync 0.1 issue 111.4 swap 54.3) start-delay=2.4 queue=107.1
## F2. Repetition 5, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep5: n=55 (+3 flagged) total p50=707 p90=1031 p99=1319 max=1378 ms | medians: ui=0.7 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=329.5 (sync 0.1 issue 190.3 swap 144.6) start-delay=186.2 queue=200.9
  FS more new rep5: n=44 (+5 flagged) total p50=566 p90=1131 p99=1297 max=1335 ms | medians: ui=0.6 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=254.2 (sync 0.1 issue 133.7 swap 112.8) start-delay=132.2 queue=137.6
  FS timeline new rep5: n=73 (+0 flagged) total p50=466 p90=700 p99=801 max=863 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=211.5 (sync 0.1 issue 113.9 swap 99.0) start-delay=101.8 queue=112.6
  FS quiz new rep5: n=90 (+15 flagged) total p50=295 p90=462 p99=539 max=551 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=161.9 (sync 0.1 issue 111.4 swap 52.1) start-delay=4.1 queue=109.6
  FS home base rep5: n=57 (+0 flagged) total p50=555 p90=897 p99=1071 max=1093 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=311.0 (sync 0.1 issue 142.4 swap 139.8) start-delay=153.7 queue=171.9
  FS more base rep5: n=56 (+2 flagged) total p50=631 p90=828 p99=1290 max=1325 ms | medians: ui=0.5 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=288.0 (sync 0.1 issue 159.2 swap 132.4) start-delay=135.3 queue=145.7
  FS timeline base rep5: n=67 (+0 flagged) total p50=550 p90=846 p99=905 max=906 ms | medians: ui=0.7 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=269.1 (sync 0.1 issue 146.2 swap 111.2) start-delay=113.0 queue=148.2
  FS quiz base rep5: n=87 (+0 flagged) total p50=643 p90=791 p99=972 max=989 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=284.2 (sync 0.1 issue 136.6 swap 136.3) start-delay=164.4 queue=199.1
## F2. Repetition 6, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep6: n=53 (+2 flagged) total p50=664 p90=955 p99=1106 max=1141 ms | medians: ui=0.9 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=354.8 (sync 0.1 issue 199.1 swap 167.4) start-delay=157.7 queue=188.4
  FS more base rep6: n=63 (+0 flagged) total p50=591 p90=746 p99=813 max=854 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=279.1 (sync 0.1 issue 147.8 swap 116.4) start-delay=167.6 queue=170.5
  FS timeline base rep6: n=70 (+0 flagged) total p50=559 p90=692 p99=828 max=885 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=260.2 (sync 0.1 issue 149.3 swap 102.7) start-delay=144.0 queue=149.3
  FS quiz base rep6: n=90 (+2 flagged) total p50=695 p90=799 p99=840 max=873 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=292.6 (sync 0.1 issue 139.8 swap 138.1) start-delay=182.2 queue=204.9
  FS home new rep6: n=65 (+1 flagged) total p50=552 p90=1072 p99=1650 max=1738 ms | medians: ui=1.2 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=272.2 (sync 0.1 issue 129.1 swap 104.9) start-delay=111.6 queue=122.1
  FS more new rep6: n=61 (+0 flagged) total p50=541 p90=721 p99=766 max=772 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=260.6 (sync 0.1 issue 142.1 swap 118.7) start-delay=150.1 queue=162.8
  FS timeline new rep6: n=80 (+0 flagged) total p50=323 p90=583 p99=727 max=733 ms | medians: ui=1.2 (input 0.0 anim 0.7 layout 0.0 draw 0.3) rt=146.2 (sync 0.1 issue 102.6 swap 39.2) start-delay=53.8 queue=76.8
  FS quiz new rep6: n=90 (+17 flagged) total p50=308 p90=444 p99=501 max=545 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=171.1 (sync 0.1 issue 113.2 swap 56.0) start-delay=1.6 queue=114.1
## F3. Totals (rep 0 is excluded; new = 1.2.39 candidate, base = 1.2.38)
| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 6 | 58 | **623.1** (555.5..670.5) | 926 | 1102 | 0.8 | 315.8 | 0.0 | 0.2 | 0.1 | 160.4 | 147.1 |
| home | new | 6 | 64 | **589.2** (551.7..772.9) | 1010 | 1273 | 0.7 | 305.0 | 0.0 | 0.2 | 0.1 | 154.1 | 128.0 |
| more | base | 6 | 58 | **590.6** (531.2..672.5) | 752 | 819 | 0.5 | 277.5 | 0.0 | 0.2 | 0.1 | 143.9 | 126.2 |
| more | new | 6 | 62 | **561.5** (541.3..571.4) | 725 | 935 | 0.5 | 253.4 | 0.0 | 0.2 | 0.1 | 135.3 | 117.4 |
| quiz | base | 6 | 90 | **672.2** (635.9..695.4) | 801 | 908 | 0.6 | 290.9 | 0.0 | 0.4 | 0.1 | 142.3 | 136.5 |
| quiz | new | 6 | 90 | **296.0** (284.7..307.6) | 455 | 503 | 0.4 | 167.5 | 0.0 | 0.2 | 0.1 | 112.3 | 54.1 |
| timeline | base | 5 | 69 | **544.0** (525.9..559.2) | 676 | 883 | 0.8 | 249.0 | 0.0 | 0.1 | 0.1 | 142.6 | 102.7 |
| timeline | new | 5 | 80 | **422.1** (322.6..471.3) | 664 | 727 | 0.7 | 175.4 | 0.0 | 0.2 | 0.1 | 113.9 | 91.4 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| home | base | **17884** (16743..20027) | 58 (47..63) | **315.8** (301.7..397.6) | 8520 | 9369 |
| home | new | **18850** (17730..19991) | 64 (53..65) | **305.0** (271.1..365.9) | 8960 | 9796 |
| more | base | **16338** (13625..17738) | 58 (48..71) | **277.5** (259.9..288.0) | 6939 | 9286 |
| more | new | **15705** (11337..18416) | 62 (44..74) | **253.4** (243.0..260.6) | 6871 | 8710 |
| quiz | base | **26206** (24702..26940) | 90 (86..90) | **290.9** (284.2..309.2) | 12936 | 13257 |
| quiz | new | **14332** (13817..14741) | 90 (89..91) | **167.5** (161.9..171.8) | 5475 | 8749 |
| timeline | base | **17656** (15606..18631) | 69 (65..77) | **249.0** (239.8..269.1) | 8005 | 9472 |
| timeline | new | **15587** (13029..18462) | 80 (73..86) | **175.4** (146.2..211.5) | 6594 | 8963 |

- home / new vs base, sum of RT: 18850.0 vs 17884.3 (+5%); U=24.0, p=0.394; ranges overlap
- home / new vs base, median RT per frame: 305.0 vs 315.8 (-3%); U=11.0, p=0.310; ranges overlap
- more / new vs base, sum of RT: 15704.9 vs 16337.5 (-4%); U=15.0, p=0.699; ranges overlap
- more / new vs base, median RT per frame: 253.4 vs 277.5 (-9%); U=1.0, p=0.004; ranges overlap
- quiz / new vs base, sum of RT: 14332.5 vs 26206.0 (-45%); U=0.0, p=0.002; ranges do NOT overlap
- quiz / new vs base, median RT per frame: 167.5 vs 290.9 (-42%); U=0.0, p=0.002; ranges do NOT overlap
- timeline / new vs base, sum of RT: 15587.1 vs 17656.0 (-12%); U=5.0, p=0.151; ranges overlap
- timeline / new vs base, median RT per frame: 175.4 vs 249.0 (-30%); U=0.0, p=0.008; ranges do NOT overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [566.2, 670.5, 601.9, 644.3, 555.5, 664.4]
- home / new: [578.1, 772.9, 586.5, 591.9, 707.3, 551.7] vs base: median diff -33.9 ms (UI -0.0, RT -10.8, issue -6.3); Mann-Whitney U=18.0, p=1.000; ranges overlap
- more / base: [672.5, 590.2, 579.7, 531.2, 630.6, 591.0]
- more / new: [559.4, 571.4, 559.8, 563.2, 566.5, 541.3] vs base: median diff -29.1 ms (UI -0.0, RT -24.1, issue -8.6); Mann-Whitney U=6.0, p=0.065; ranges overlap
- quiz / base: [635.9, 673.4, 678.0, 670.9, 642.7, 695.4]
- quiz / new: [298.2, 284.7, 296.7, 295.2, 294.9, 307.6] vs base: median diff -376.2 ms (UI -0.2, RT -123.4, issue -30.0); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
- timeline / base: [525.9, 544.0, 533.2, 550.0, 559.2]
- timeline / new: [335.8, 422.1, 471.3, 465.6, 322.6] vs base: median diff -121.9 ms (UI -0.1, RT -73.7, issue -28.7); Mann-Whitney U=0.0, p=0.008; ranges do NOT overlap
FR part: FATAL lines 0, ANR 0; host load: 3.23 4.00 3.91
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['3801', '6303', '9581', '13491', '14564', '18302', '19599', '22735']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 52
   skipped-frames events=52 max=156 total=2558
== Davey: 198
   davey events=198 max=2723ms median=845ms
== NativeLink: 8
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## smk26

```
part smk26; APK: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
base APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100 size 4641014 bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
installed:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2078 ms
## S1-light. Main screens, timeline (scroll + scrubber drag), quiz to 5/5, event, „Дари спомен“, share, settings (1080x2400 px, density 420, font_scale 1.0, night no, app locale , rotation 0)
CAP light/01-home: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP light/01b-home-2: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · СЪБИТИЕ · · 26 септември 2026 · С хоро и песен във Водица · Сканирай · Читателска карта
CAP light/01c-home-3: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=36 ellipsized=0 | Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра
CAP light/02-catalog: targets=22 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=48 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP light/03-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Примерна читателска карта с  · Така ще изглежда картата ти · Вход · Добави читателска карта · БИБЛИОТЕКА · Читателска карта
CAP light/04-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP light/05-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP light/05b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=24 ellipsized=0 | Стари снимки за фотоархива · Документи и архив · Устав, отчети, юбилейният ве · Контакти · Адрес, телефон, местоположен · ЗАНИМАНИЯ
CAP light/10-timeline: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · 1900. Театрален кръжец и зар · Ученици от Севлиевската гимн
TIMELINE light top: first=1900 min=1900 max=1922 n=13; text="От 1900 г. до днес" content-desc="1900. Театрален кръжец и зараждане на идеята. Ученици от Севлиевската гимназия основават театрална група. Успехът на пиесата „Левски“ ги навежда на мисълта да основат читалище „Васил Левски“. Петко Владов дарява първите книги." content-desc="1900. Театрален кръжец и зараждане на идеята" content-desc="1909. Организирано читалищно дело. Влади Илиев, Христо Лалев и Недко Станчев поставят по-трайно начало. Дарени 50–60 тома, по-късно още 25–30. Библиотеката е в дома на Хр. Лалев." content-desc="1909. Организирано читалищно дело" content-desc="1912–1918. Прек
CAP light/10b-timeline-scrolled: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=22 ellipsized=0 | 1923–1924. Устав и държавно  · 1923–1924 · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств · Въздържателно дружество „Бор
TIMELINE light after 3 swipes: first=1923 min=1920 max=1947 n=17; content-desc="1923–1924. Устав и държавно признание. Уставът е приет на 22 януари 1923 г. Министерството на просветата го утвърждава на 1 май 1924 г. Читалището става член на национален читалищен съюз." text="1923–1924" text="Уставът е приет на 22 януари 1923 г. Министерството на просветата го утвърждава на 1 май 1924 г. Читалището става член на национален читалищен съюз." content-desc="1927. Въздържателно дружество „Борба“. Основано от активни читалищни дейци с председател Илия Христов. Беседи, реферати и театрални постановки." content-desc="1927. Въздържателно дружество „Борба“" content-desc
CRASHES: 0 block(s)  → crashes.txt
```

## smk33

```
part smk33; APK: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
base APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100 size 4641014 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
installed:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5207 ms
## S1-light. Main screens, timeline (scroll + scrubber drag), quiz to 5/5, event, „Дари спомен“, share, settings (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP light/01-home: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP light/01b-home-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=31 ellipsized=0 | Лента за търсене · 15 ноември 2026 · + 8 Неделя · Последни новини · БИБЛИОТЕКА · · 11 ноември 2026 · Четем заедно – мъдростта на 
CAP light/01c-home-3: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · БИБЛИОТЕКА · · 11 ноември 2026 · Четем заедно – мъдростта на  · Сканирай · Читателска карта
CAP light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP light/03-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Примерна читателска карта с  · Така ще изглежда картата ти · Вход · Добави читателска карта · БИБЛИОТЕКА · Читателска карта
CAP light/04-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP light/05-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP light/05b-more-2: targets=15 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=26 ellipsized=0 | Галерия · Снимки от сайта · Дари спомен · Стари снимки за фотоархива · Документи и архив · Устав, отчети, юбилейният ве
CAP light/10-timeline: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · 1900. Театрален кръжец и зар · Ученици от Севлиевската гимн
TIMELINE light top: first=1900 min=1900 max=1922 n=13; text="От 1900 г. до днес" content-desc="1900. Театрален кръжец и зараждане на идеята. Ученици от Севлиевската гимназия основават театрална група. Успехът на пиесата „Левски“ ги навежда на мисълта да основат читалище „Васил Левски“. Петко Владов дарява първите книги." content-desc="1900. Театрален кръжец и зараждане на идеята" content-desc="1909. Организирано читалищно дело. Влади Илиев, Христо Лалев и Недко Станчев поставят по-трайно начало. Дарени 50–60 тома, по-късно още 25–30. Библиотеката е в дома на Хр. Лалев." content-desc="1909. Организирано читалищно дело" content-desc="1912–1918. Прек
CAP light/10b-timeline-scrolled: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | 17 дек. 1922. Възстановяване · Дейността се възобновява под · 1923–1924. Устав и държавно  · 1923–1924 · Устав и държавно признание · Уставът е приет на 22 януари
TIMELINE light after 3 swipes: first=1922 min=1920 max=1936 n=16; content-desc="17 дек. 1922. Възстановяване и официално учредяване. Дейността се възобновява под името НЧ „Васил Левски“. По инициатива на Христо Лалев читалището се отделя в самостоятелна институция, настанена в Общината." content-desc="1923–1924. Устав и държавно признание. Уставът е приет на 22 януари 1923 г. Министерството на просветата го утвърждава на 1 май 1924 г. Читалището става член на национален читалищен съюз." text="1923–1924" text="Уставът е приет на 22 януари 1923 г. Министерството на просветата го утвърждава на 1 май 1924 г. Читалището става член на национален читалищен съюз." c
CRASHES: 0 block(s)  → crashes.txt
```

## smk36x

```
part smk36x; APK: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
base APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100 size 4641014 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
installed:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 5514 ms
## S1-light. Main screens, timeline (scroll + scrubber drag), quiz to 5/5, event, „Дари спомен“, share, settings (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP light/01-home: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP light/01b-home-2: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=33 ellipsized=0 | Последни новини · БИБЛИОТЕКА · · 11 ноември 2026 · Четем заедно – мъдростта на  · Сканирай · Читателска карта
CAP light/01c-home-3: targets=20 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=36 ellipsized=0 | Предстоящи събития · Всички · 16 · НОЕ · 80 г. от създаването на ЮНЕС · Организацията на ООН за обра
CAP light/02-catalog: targets=22 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=49 ellipsized=0 | Търси навсякъде · Сканирай баркод · Всичко · Заглавие · Автор · ISBN
CAP light/03-my: targets=11 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Примерна читателска карта с  · Така ще изглежда картата ти · Вход · Добави читателска карта · БИБЛИОТЕКА · Читателска карта
CAP light/04-news: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Новини · Календар · Търси в новините · Всички · Запазени · Библиотека
CAP light/05-more: targets=15 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP light/05b-more-2: targets=15 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=27 ellipsized=0 | Галерия · Снимки от сайта · Дари спомен · Стари снимки за фотоархива · Документи и архив · Устав, отчети, юбилейният ве
CAP light/10-timeline: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · 1900. Театрален кръжец и зар · Ученици от Севлиевската гимн
TIMELINE light top: first=1900 min=1900 max=1922 n=13; text="От 1900 г. до днес" content-desc="1900. Театрален кръжец и зараждане на идеята. Ученици от Севлиевската гимназия основават театрална група. Успехът на пиесата „Левски“ ги навежда на мисълта да основат читалище „Васил Левски“. Петко Владов дарява първите книги." content-desc="1900. Театрален кръжец и зараждане на идеята" content-desc="1909. Организирано читалищно дело. Влади Илиев, Христо Лалев и Недко Станчев поставят по-трайно начало. Дарени 50–60 тома, по-късно още 25–30. Библиотеката е в дома на Хр. Лалев." content-desc="1909. Организирано читалищно дело" content-desc="1912–1918. Прек
CAP light/10b-timeline-scrolled: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | 1923–1924. Устав и държавно  · 1923–1924 · Устав и държавно признание · Уставът е приет на 22 януари · 1927. Въздържателно дружеств · Въздържателно дружество „Бор
TIMELINE light after 3 swipes: first=1923 min=1920 max=1947 n=17; content-desc="1923–1924. Устав и държавно признание. Уставът е приет на 22 януари 1923 г. Министерството на просветата го утвърждава на 1 май 1924 г. Читалището става член на национален читалищен съюз." text="1923–1924" text="Уставът е приет на 22 януари 1923 г. Министерството на просветата го утвърждава на 1 май 1924 г. Читалището става член на национален читалищен съюз." content-desc="1927. Въздържателно дружество „Борба“. Основано от активни читалищни дейци с председател Илия Христов. Беседи, реферати и театрални постановки." content-desc="1927. Въздържателно дружество „Борба“" content-desc
CRASHES: 0 block(s)  → crashes.txt
```

## upd33

```
part upd33; APK: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
base APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b38, throwaway key, versionCode 1990100 size 4641014 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 size 4681342 bytes
installed:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
UPD: 1.2.38 = 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 → the candidate 1.2.39 696e57d prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990200 (section titles below come from part_upd36: „1.2.35“ = 1.2.38, „1.2.36“ = 1.2.39)
HELPER installed: package:org.pc.helper
## U0. Install 1.2.35 (1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, thro) as a fresh install
installed base: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
## U1. State on 1.2.35: books in „Искам да прочета“, a card by hand, text size „Голям“, dark theme, the widget, a few screens
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
TEXT SIZE now: text="Голям" 
WIDGET b135 long-press menu: content-desc="Wallpaper" content-desc="Bird’s-Eye View, Zion National Park" content-desc="Light the Way, Schwyz" content-desc="White Sands, Sonoran Desert" text="Wallpaper &amp; style" text="Widgets" 
WIDGET b135 picker: our row @ 437 783; size label @ 540 1691; texts: Chitalishte Yavorets|3 × 2|Chitalishte Yavorets|Chitalishte Yavorets|
WIDGET b135 after drop: fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity; 
WIDGET b135: widgets of ours bound: 0 -> 1 (ids id=2 ); options: 
WIDGET on 1.2.35: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_title ]; bounds [309 537 1023 1123]; texts: Книга на деня|Последният пазител на Елис Айлънд|Жос, Гаел|; error text: 
locale before the update: Locales for org.chyavorec.app for user 0 are [en-US]
STATE before settings: 
STATE before night mode of the app window: ; app locale [en-US]
STATE before card: shown (TEST-12345)
STATE before want-to-read: book listed; text="Availability as of Nov 9, 2026" text="Swipe a book aside to remove it." text="Последният мечтите" text="Соня Петрова" text="available" content-desc="More actions" text="Дългият любовта" text="Пе
DB[before] files: 4096 chitalishte.db;32768 chitalishte.db-shm;70072 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[before]: 27 files: databases/catalog-index.db databases/chitalishte.db files/datastore/settings.preferences_pb files/profileInstalled files/profileinstaller_profileWrittenFor_lastUpdateTime.dat no_backup/androidx.work.workdb no_backup/androidx.work.workdb-shm no_backup/androidx.work.workdb-wal no_backup/cache/public/0d38efb6add94db2ad3f1d416cc916a67cd806cb38400fd0603e7690c3c5a3a2.json no_backup/cache/public/1ebfb91784270a823e84c3bd83429c0268dd92904a2dd8e1a4dfd63aad2c1839.json no_backup/cache/public/288782f6475e076
prefs/datastore files before: /data/data/org.chyavorec.app/files: datastore profileInstalled profileinstaller_profileWrittenFor_lastUpdateTime.dat  /data/data/org.chyavorec.app/shared_prefs: app_shortcuts.xml cover_misses.xml stats_anim.xml widget_book.xml 
## U2. adb install -r of 1.2.36 over 1.2.35 (same signing key)
adb install -r 1.2.36: Success; now:  versionCode=1990200 minSdk=26 targetSdk=37  versionName=1.2.39 
WIDGET after the update (app not opened): views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_title ]; bounds [309 537 1023 1123]; texts: Book of the day|Последният пазител на Елис Айлънд|Жос, Гаел|; error text: 
widget ids before/after: [id=2 ] / [id=2 ]
logcat since the install (app not opened): FATAL(any process)=0 of the app=0 ANR=0
## U3. First launch of 1.2.36 on the old data
first launch after the update: Status: ok LaunchState: WARM TotalTime: 3238 WaitTime: 3255 
first screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode" text="November 15, 2026 · + 8 Неделя след Неделя подир Въздвижение. Св. мчци Гурий, Самон и Авив. Гл. 7, утр. ев. 2, ап. Еф. 2:14-22 (с. 2
WHATS-NEW 31-whats-new: not shown; screen: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" text="Search books, news, events…" content-desc="Scan barcode" text="November 15, 2026 · + 8 Неделя с
logcat since the first launch: FATAL(any process)=0 of the app=0 ANR=0; process 10256
Room/migration lines: 11-15 12:10:21.412  7244 10046 I Pay     : Requested check of feature migrations. [CONTEXT service_id=198 ]|11-15 12:10:21.644  7244  7341 E Pay     : [Migration operation] Active tapandpay account not found! [CONTEXT service_id=198 ]|11-15 12:10:21.647  7244  7341 I Pay     : Migration flag not enabled. [CONTEXT service_id=198 ]|11-15 12:10:21.661  7244  7341 I Pay     : Migration already completed. [CONTEXT service_id=198 ]|
locale after the update: Locales for org.chyavorec.app for user 0 are [en-US] (UI shows: text="“Vasil Levski – 1922”" text="Yavorets village" content-desc="Messages" content-desc="Search" )
STATE after settings: text="Тъмна" text="Голям" 
STATE after night mode of the app window: ; app locale [bg-BG]
STATE after card: shown (TEST-12345)
STATE after want-to-read: book listed; text="Наличност към 09.11.2026" text="Плъзни книга настрани, за да я премахнеш." text="Последният мечтите" text="Соня Петрова" text="налична" content-desc="Още действия" text="Дългият любовта" text="П
## U4. Database and files after the update
DB[after] files: 4096 chitalishte.db;32768 chitalishte.db-shm;70072 chitalishte.db-wal;0 ls.txt;
DB chitalishte.db: user_version=3 identity_hash=58c41260b36c41894669690a8febdb1a; tables: event_reminders=0 favorite_news=0 reading_diary=0 room_master_table=1 want_to_read=3; want_to_read: 1:Дългият любовта; 2:Последният мечтите; 399560:12 -те вятъра
FILES[after]: 27 files: databases/catalog-index.db databases/chitalishte.db files/datastore/settings.preferences_pb files/profileInstalled files/profileinstaller_profileWrittenFor_lastUpdateTime.dat no_backup/androidx.work.workdb no_backup/androidx.work.workdb-shm no_backup/androidx.work.workdb-wal no_backup/cache/public/0d38efb6add94db2ad3f1d416cc916a67cd806cb38400fd0603e7690c3c5a3a2.json no_backup/cache/public/1ebfb91784270a823e84c3bd83429c0268dd92904a2dd8e1a4dfd63aad2c1839.json no_backup/cache/public/288782f6475e076
FILES before 27, after 27; removed 0: ; new 0: ; size changed 2: ./no_backup/androidx.work.workdb-wal ./no_backup/diagnostics/perf.json
## U5. Widget after opening the app; Diagnostics › Бързодействие after the update; new screens on the migrated data
WIDGET after opening the app: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_title ]; bounds [309 537 1023 1123]; texts: Книга на деня|Последният пазител на Елис Айлънд|Жос, Гаел|; error text: 
WIDGET click catalog: 'widget_catalog' not on the home screen
WIDGET click scan: 'widget_scan' not on the home screen
WIDGET click event: 'widget_event' not on the home screen
WIDGET click app: 't:Chitalishte Yavorets|Читалище Яворец' not on the home screen
WARN: settings item =Диагностика|=Diagnostics not found
## NP-after-update (brief). New screens (1080x2400 px, density 420, font_scale 1.0, night no, app locale [bg-BG], rotation 0)
CAP after-update/01-home: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=23 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
CAP after-update/02-more: targets=13 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=24 ellipsized=0 | ЧИТАЛИЩЕТО · Съобщения · Съобщения и покани от читали · Събития · Календар на читалището · Дейности
CAP after-update/em-01-open: targets=16 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB stitches: start 0 → after 6 taps + a stroke 13
CAP after-update/em-02-stitched: targets=17 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=25 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB undo: 13 → 6
EMB redo: 6 → 13
EMB save: text="Моите шевици" 
CAP after-update/em-06-after-save: targets=21 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=35 ellipsized=0 | Платно за шевица, 15 на 15 к · Цвят на конеца · Бордо · Злато · Черно · Кремаво
EMB share: chooser NOT open after 12 s; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
EMB end: front app
CAP after-update/np-01-list: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Старият вестник · Юбилеен вестник · 1959 · Двадесет статии от юбилейния · СТАТИЯ 01 · СПОМЕНИ · Баба Дамяна разказва · Спомен от Освобождението — д
NEWSPAPER first open: page label after 2870 ms (-1 = none in 100 s): content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
CAP after-update/np-02-reader: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=5 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели
NEWSPAPER page content (std of the brightness, ≈0 = blank): mean=30 std=25.0
NEWSPAPER: no zoom button: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" 
NEWSPAPER: no next-page button (one page?)
NEWSPAPER logcat: ; FATAL(any process)=0 of the app=0 ANR=0
offline: airplane=1
NEWSPAPER reopened OFFLINE: after 2790 ms: content-desc="Баба Дамяна разказва. Страница 1 от 1. Щипни или докосни двукратно, за да увеличиш." content-desc="Назад" text="Баба Дамяна разказва" content-desc="Отвори във външно приложение" content-desc="Сподели" text="Няма интернет връзк
CAP after-update/np-05-offline: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Баба Дамяна разказва. Страни · Назад · Баба Дамяна разказва · Отвори във външно приложение · Сподели · Няма интернет връзка
NEWSPAPER offline page content: mean=30 std=25.1
CAP after-update/tl-01: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · 1900. Театрален кръжец и зар · Ученици от Севлиевската гимн
CAP after-update/tl-03: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | 1912–1918. Прекъсване от вой · Балканската война (1912) пре · 17 дек. 1922. Възстановяване · 17 дек. 1922 · Възстановяване и официално у · Дейността се възобновява под
CAP after-update/tl-06: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | 1936. Дружество „Воля“. Ново · Ново въздържателно дружество · 1947. Решение за собствена с · Решение за собствена сграда · 1947. Решение за собствена с · Започва отчуждаване на терен
QUIZ ready after 2017 ms: text="Въпрос 1 от 5" text="Какво се случва в читалището през 1947 г.?" text="Решение за собствена сграда" text="Въздържателно дружество „Борба“" text="Общностен и информационен център" text="Трудни години — устояване" content-desc="Избродир
CAP after-update/qz-01-q1: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Въпрос 1 от 5 · Какво се случва в читалището · Решение за собствена сграда · Въздържателно дружество „Бор · Общностен и информационен це · Трудни години — устояване
CAP after-update/qz-01-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=12 ellipsized=0 | Въпрос 1 от 5 · Какво се случва в читалището · Решение за собствена сграда · Въздържателно дружество „Бор · Общностен и информационен це · Трудни години — устояване
CAP after-update/qz-06-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 2/5 · 2 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
QUIZ result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ share: chooser NOT open after 15 s; fg com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity
CAP after-update/st-01: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=21 ellipsized=0 | ИЗГЛЕД · Тема · Тъмна · Размер на текста · Голям · Палитра
CAP after-update/st-02: targets=10 MISSING=3 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=3 | front: app | texts=14 ellipsized=0 | Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български
CAP after-update/st-03: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=15 ellipsized=0 | Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо
CAP after-update/st-04: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=16 ellipsized=0 | Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията · Заемания, събития, съобщения
WARN: settings item Седмичен преглед|Weekly digest not found
SETTINGS: no digest row
CAP after-update/11-event: targets=7 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Понеделник, 16 ноември 2026, · 80 г. от създаването на ЮНЕС · Събитие от годишния календар · НЧ „Васил Левски – 1922“, пл · Организацията на ООН за обра · Добави в календара
NP after-update: FATAL(any process)=0 of the app=0 ANR=0; app process 12272; logcat app errors: 0
## U6. Logcat of the whole part
FATAL/ANR since the install: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 8 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: settings item =Диагностика|=Diagnostics not found
APP W/E lines (pids ['5587', '9190', '10256', '11301', '11577', '11650', '11713', '12272']): 77 in 12 groups
      16  W/Parcel: Expecting binder but got null!
      15  E/OpenGLRenderer: Unable to match the desired swap behavior.
       8  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       8  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       8  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       8  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       8  W/Gralloc4: allocator #.x is not supported
       2  E/FrameEvents: updateAcquireFence: Did not find frame.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=afd#-aa#-#c-ae#-#c#a, generation=#)
       1  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
       1  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object tb.a()(r#-map-id-#f#a#db
contact sheets: 8 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5587', '9190', '10256', '11301', '11577', '11650', '11713', '12272']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 48
   skipped-frames events=48 max=134 total=2685
== Davey: 104
   davey events=104 max=3311ms median=950ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

