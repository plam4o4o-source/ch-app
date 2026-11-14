# Targeted emulator check of the released v1.2.38 (f57962b) against v1.2.37 (ebf1720)

Status: run 1 in progress — the findings will be written here when the runs are done. Raw summaries of the parts are below.

## Runs and parts

debug/w25-check (runs in runs.txt, latest 43645027104 @ 1dd4ccb). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU) unless the part name ends in 28 (API 28) or 36x (API 36). prodRelease (minified) x86_64 builds with a throwaway key, installed side by side: new = 1.2.38 as released (org.chyavorec.app), base = 1.2.37 (org.chyavorec.app.b37), hook = the debug branch with log lines and test flags (org.chyavorec.app.hk); dev33: devDebug of the debug branch. Screenshots are JPEG, scaled down (sheets/ = contact sheets). The earlier check: [../v138/index.md](../v138/index.md).

| part | status |
|---|---|
| at33h | COMPLETE; part=at33h target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| at33t | COMPLETE; part=at33t target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| cf28 | COMPLETE; part=cf28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| cf33 | COMPLETE; part=cf33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| cf36x | COMPLETE; part=cf36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| dev33 | COMPLETE; part=dev33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fr33a | COMPLETE; part=fr33a target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fr33b | COMPLETE; part=fr33b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fs33 | COMPLETE; part=fs33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mem33a | COMPLETE; part=mem33a target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| mem33b | COMPLETE; part=mem33b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| wd33 | COMPLETE; part=wd33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| wd36x | COMPLETE; part=wd36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |

```
1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000
devDebug 1.2.38 1dd4ccb (org.chyavorec.app.dev, demo sign-in DEMO-0001, shared debug key; demo-1 renews at once)
1.2.38 1dd4ccb prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.hk, throwaway key, versionCode 1990101
1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100
app.apk 4641014 bytes
app-base.apk 4591454 bytes
app-hook.apk 4642934 bytes
app-dev.apk 28796723 bytes
```

## at33h

```
part at33h; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## AH0. Home and Още: one suspect at a time (hook build flags), with 1.2.38 and 1.2.37 as references
X installed new: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 3826 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~M24F7RGMn0_zg6TyKhMvmw==/org.chyavorec.app-9KoZo6_NCSSqwtam_YY-sQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000;  versionCode=1990000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 3022 ms
X compile org.chyavorec.app.b37: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~o0DFhkp-kGe728EUSKCBJA==/org.chyavorec.app.b37-hvQ4iEjCSHTIPxO0-40H_A==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed hook: 1.2.38 1dd4ccb prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.hk, throwaway key, versionCode 1990101;  versionCode=1990101 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X hook: catalogue after 2839 ms
X compile org.chyavorec.app.hk: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~blSdHWdO0wZXps_Pd4tWcw==/org.chyavorec.app.hk-qm1FdibzIA2ypACGASH4zw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /product/overlay/TelephonyProvider__auto_generated_rro_product.apk| x86_64: [status=run-from-apk] [reason=unknown]|
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
animations: animator_duration_scale=1 transition=1 window=1; host load: 4.79 3.41 1.58
## A. Repetition 0 of: home more (configs rotated by 0)
  FS home new rep0: n=64 (+0 flagged) total p50=436 p90=601 p99=669 max=686 ms | medians: ui=0.6 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=189.9 (sync 0.1 issue 53.1 swap 112.0) start-delay=96.7 queue=121.5
  FS more new rep0: n=50 (+4 flagged) total p50=377 p90=506 p99=624 max=633 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=142.5 (sync 0.1 issue 47.8 swap 87.3) start-delay=87.6 queue=102.9
  FS home base rep0: n=74 (+0 flagged) total p50=465 p90=607 p99=683 max=732 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=192.4 (sync 0.1 issue 53.5 swap 111.4) start-delay=140.5 queue=146.3
  FS more base rep0: n=64 (+0 flagged) total p50=418 p90=564 p99=630 max=639 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=164.0 (sync 0.1 issue 49.1 swap 110.4) start-delay=113.5 queue=125.6
  FS home hk rep0: n=79 (+0 flagged) total p50=450 p90=557 p99=684 max=691 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=158.9 (sync 0.1 issue 47.8 swap 105.6) start-delay=140.1 queue=149.7
  FS more hk rep0: n=42 (+3 flagged) total p50=405 p90=574 p99=665 max=670 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=160.8 (sync 0.1 issue 50.0 swap 105.2) start-delay=100.5 queue=95.5
## A. Repetition 1 of: home more (configs rotated by 1)
  FS home base rep1: n=74 (+0 flagged) total p50=453 p90=562 p99=621 max=625 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=185.9 (sync 0.1 issue 51.2 swap 110.2) start-delay=127.0 queue=139.4
  FS more base rep1: n=68 (+2 flagged) total p50=474 p90=611 p99=985 max=993 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=183.9 (sync 0.1 issue 53.9 swap 114.1) start-delay=129.8 queue=127.1
  FS home hk rep1: n=70 (+0 flagged) total p50=451 p90=591 p99=735 max=813 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=193.8 (sync 0.1 issue 52.6 swap 110.0) start-delay=113.2 queue=134.2
  FS more hk rep1: n=81 (+0 flagged) total p50=409 p90=513 p99=554 max=557 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=159.4 (sync 0.1 issue 53.9 swap 101.1) start-delay=121.3 queue=130.4
  FS home plate rep1: n=78 (+0 flagged) total p50=428 p90=529 p99=647 max=652 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=161.5 (sync 0.1 issue 48.6 swap 104.7) start-delay=125.0 queue=137.8
  FS more plate rep1: n=59 (+3 flagged) total p50=413 p90=502 p99=733 max=801 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=153.0 (sync 0.4 issue 49.7 swap 93.6) start-delay=112.4 queue=120.3
  FS home icon rep1: n=69 (+0 flagged) total p50=451 p90=571 p99=728 max=750 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=159.5 (sync 0.1 issue 48.9 swap 109.0) start-delay=137.9 queue=147.9
  FS more icon rep1: n=64 (+2 flagged) total p50=409 p90=598 p99=725 max=734 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=159.4 (sync 0.1 issue 50.5 swap 104.6) start-delay=110.9 queue=110.5
  FS home both rep1: n=80 (+0 flagged) total p50=446 p90=526 p99=590 max=619 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=155.7 (sync 0.1 issue 48.3 swap 105.4) start-delay=134.7 queue=143.2
  FS more both rep1: n=53 (+4 flagged) total p50=383 p90=464 p99=631 max=667 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=142.7 (sync 0.1 issue 48.5 swap 94.2) start-delay=86.0 queue=115.2
  FS home new rep1: n=69 (+0 flagged) total p50=421 p90=585 p99=676 max=681 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=195.4 (sync 0.1 issue 52.8 swap 112.2) start-delay=105.1 queue=116.5
  FS more new rep1: n=59 (+2 flagged) total p50=415 p90=549 p99=645 max=694 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=155.5 (sync 0.4 issue 53.4 swap 104.0) start-delay=101.1 queue=114.4
## A. Repetition 2 of: home more (configs rotated by 2)
  FS home hk rep2: n=68 (+0 flagged) total p50=467 p90=618 p99=752 max=754 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=181.7 (sync 0.1 issue 49.8 swap 110.1) start-delay=111.1 queue=124.0
  FS more hk rep2: n=85 (+0 flagged) total p50=399 p90=548 p99=633 max=729 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=161.1 (sync 0.1 issue 52.0 swap 104.6) start-delay=98.1 queue=120.9
  FS home plate rep2: n=77 (+0 flagged) total p50=440 p90=559 p99=689 max=706 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=159.8 (sync 0.1 issue 49.4 swap 107.0) start-delay=126.4 queue=136.5
  FS more plate rep2: n=58 (+3 flagged) total p50=366 p90=501 p99=740 max=801 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=144.6 (sync 0.1 issue 47.0 swap 93.7) start-delay=89.9 queue=96.1
  FS home icon rep2: n=82 (+0 flagged) total p50=463 p90=549 p99=619 max=629 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=190.0 (sync 0.1 issue 54.1 swap 109.7) start-delay=129.0 queue=142.5
  FS more icon rep2: n=46 (+3 flagged) total p50=411 p90=546 p99=683 max=709 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=155.7 (sync 0.1 issue 50.1 swap 96.8) start-delay=111.3 queue=120.9
  FS home both rep2: n=73 (+0 flagged) total p50=449 p90=555 p99=684 max=689 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=159.9 (sync 0.1 issue 50.7 swap 106.8) start-delay=141.1 queue=147.8
  FS more both rep2: n=57 (+1 flagged) total p50=416 p90=506 p99=754 max=768 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=146.7 (sync 0.4 issue 47.7 swap 96.3) start-delay=127.1 queue=133.0
  FS home new rep2: n=68 (+0 flagged) total p50=436 p90=534 p99=586 max=592 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=161.0 (sync 0.1 issue 48.5 swap 109.5) start-delay=99.1 queue=112.0
  FS more new rep2: n=83 (+0 flagged) total p50=429 p90=526 p99=571 max=633 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=160.8 (sync 0.1 issue 49.7 swap 105.5) start-delay=106.6 queue=114.6
  FS home base rep2: n=74 (+0 flagged) total p50=446 p90=599 p99=690 max=709 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=172.8 (sync 0.1 issue 50.3 swap 109.9) start-delay=123.9 queue=118.6
  FS more base rep2: n=71 (+0 flagged) total p50=408 p90=582 p99=724 max=778 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=167.5 (sync 0.1 issue 50.0 swap 111.1) start-delay=105.7 queue=123.0
## A. Repetition 3 of: home more (configs rotated by 3)
  FS home plate rep3: n=69 (+0 flagged) total p50=450 p90=536 p99=615 max=651 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=170.4 (sync 0.1 issue 48.8 swap 109.2) start-delay=141.0 queue=148.7
  FS more plate rep3: n=64 (+1 flagged) total p50=385 p90=451 p99=692 max=730 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=144.9 (sync 0.1 issue 47.6 swap 95.5) start-delay=126.5 queue=131.8
  FS home icon rep3: n=63 (+0 flagged) total p50=447 p90=592 p99=680 max=705 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=166.1 (sync 0.1 issue 49.5 swap 108.2) start-delay=138.8 queue=142.7
  FS more icon rep3: n=48 (+1 flagged) total p50=446 p90=560 p99=772 max=775 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=158.2 (sync 0.4 issue 51.2 swap 102.9) start-delay=127.8 queue=131.2
  FS home both rep3: n=75 (+0 flagged) total p50=434 p90=548 p99=627 max=680 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=160.1 (sync 0.1 issue 48.7 swap 107.5) start-delay=131.8 queue=141.0
  FS more both rep3: n=64 (+1 flagged) total p50=419 p90=512 p99=628 max=633 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=150.0 (sync 0.1 issue 49.4 swap 96.0) start-delay=114.1 queue=127.9
  FS home new rep3: n=68 (+0 flagged) total p50=463 p90=614 p99=698 max=750 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=191.6 (sync 0.1 issue 69.7 swap 108.0) start-delay=124.3 queue=112.0
  FS more new rep3: n=76 (+0 flagged) total p50=446 p90=533 p99=604 max=626 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=173.5 (sync 0.1 issue 56.2 swap 103.5) start-delay=119.8 queue=123.5
  FS home base rep3: n=72 (+0 flagged) total p50=407 p90=563 p99=835 max=900 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=158.0 (sync 0.1 issue 48.0 swap 98.6) start-delay=98.3 queue=123.7
  FS more base rep3: n=78 (+0 flagged) total p50=454 p90=581 p99=676 max=702 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=181.4 (sync 0.1 issue 52.4 swap 112.2) start-delay=128.6 queue=144.5
  FS home hk rep3: n=86 (+0 flagged) total p50=401 p90=557 p99=650 max=660 ms | medians: ui=0.4 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=153.1 (sync 0.1 issue 48.1 swap 100.5) start-delay=129.2 queue=138.9
WARN: tab bar not reached
  FS more hk rep3: n=76 (+0 flagged) total p50=352 p90=497 p99=598 max=644 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=141.0 (sync 0.1 issue 49.6 swap 83.4) start-delay=95.4 queue=105.9
## A. Repetition 4 of: home more (configs rotated by 4)
  FS home icon rep4: n=81 (+0 flagged) total p50=478 p90=576 p99=677 max=685 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=164.0 (sync 0.1 issue 49.4 swap 107.0) start-delay=139.9 queue=146.4
  FS more icon rep4: n=64 (+2 flagged) total p50=414 p90=583 p99=706 max=775 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=166.8 (sync 0.1 issue 60.6 swap 100.1) start-delay=103.1 queue=111.1
  FS home both rep4: n=78 (+0 flagged) total p50=452 p90=567 p99=689 max=710 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=156.8 (sync 0.1 issue 50.5 swap 104.8) start-delay=114.0 queue=136.1
  FS more both rep4: n=77 (+2 flagged) total p50=372 p90=513 p99=729 max=767 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=145.8 (sync 0.1 issue 49.0 swap 94.3) start-delay=92.7 queue=101.2
  FS home new rep4: n=73 (+0 flagged) total p50=441 p90=609 p99=670 max=701 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=181.7 (sync 0.1 issue 49.8 swap 108.9) start-delay=137.9 queue=142.6
  FS more new rep4: n=62 (+2 flagged) total p50=440 p90=558 p99=877 max=986 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=169.6 (sync 0.1 issue 49.5 swap 106.8) start-delay=118.9 queue=113.9
  FS home base rep4: n=65 (+0 flagged) total p50=458 p90=616 p99=737 max=744 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=183.5 (sync 0.1 issue 50.4 swap 109.7) start-delay=132.4 queue=136.3
  FS more base rep4: n=64 (+0 flagged) total p50=436 p90=558 p99=642 max=642 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=166.9 (sync 0.1 issue 50.9 swap 108.5) start-delay=105.6 queue=138.0
  FS home hk rep4: n=73 (+0 flagged) total p50=458 p90=571 p99=650 max=660 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=158.1 (sync 0.1 issue 49.3 swap 109.2) start-delay=141.7 queue=144.0
  FS more hk rep4: n=51 (+2 flagged) total p50=420 p90=522 p99=624 max=685 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=159.8 (sync 0.1 issue 50.0 swap 100.8) start-delay=92.5 queue=111.5
  FS home plate rep4: n=75 (+0 flagged) total p50=474 p90=603 p99=711 max=803 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=200.8 (sync 0.2 issue 52.3 swap 112.2) start-delay=130.8 queue=148.9
  FS more plate rep4: n=62 (+2 flagged) total p50=383 p90=540 p99=791 max=850 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=145.4 (sync 0.1 issue 47.5 swap 93.5) start-delay=95.5 queue=107.2
## A. Repetition 5 of: home more (configs rotated by 5)
  FS home both rep5: n=72 (+0 flagged) total p50=456 p90=569 p99=619 max=632 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=166.8 (sync 0.1 issue 50.0 swap 107.9) start-delay=139.0 queue=148.6
  FS more both rep5: n=48 (+2 flagged) total p50=387 p90=579 p99=860 max=917 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=143.5 (sync 0.1 issue 49.0 swap 93.0) start-delay=87.9 queue=107.0
  FS home new rep5: n=77 (+0 flagged) total p50=454 p90=584 p99=675 max=680 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=174.6 (sync 0.1 issue 49.1 swap 111.5) start-delay=134.6 queue=147.1
  FS more new rep5: n=61 (+1 flagged) total p50=398 p90=538 p99=721 max=768 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=166.2 (sync 0.1 issue 48.3 swap 106.3) start-delay=107.6 queue=106.7
  FS home base rep5: n=63 (+0 flagged) total p50=483 p90=560 p99=723 max=725 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=210.8 (sync 0.1 issue 70.2 swap 112.5) start-delay=138.2 queue=129.3
  FS more base rep5: n=60 (+1 flagged) total p50=433 p90=602 p99=827 max=912 ms | medians: ui=0.4 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=177.8 (sync 0.1 issue 59.9 swap 105.4) start-delay=100.7 queue=113.7
  FS home hk rep5: n=67 (+0 flagged) total p50=417 p90=576 p99=621 max=624 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=197.2 (sync 0.1 issue 54.2 swap 109.8) start-delay=102.3 queue=110.6
  FS more hk rep5: n=41 (+2 flagged) total p50=399 p90=561 p99=702 max=708 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=156.9 (sync 0.1 issue 49.7 swap 98.7) start-delay=103.3 queue=111.7
  FS home plate rep5: n=81 (+0 flagged) total p50=424 p90=543 p99=669 max=700 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=155.8 (sync 0.1 issue 48.7 swap 105.0) start-delay=130.2 queue=141.9
  FS more plate rep5: n=74 (+0 flagged) total p50=376 p90=480 p99=546 max=568 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=144.6 (sync 0.1 issue 47.5 swap 96.7) start-delay=95.6 queue=105.2
  FS home icon rep5: n=74 (+0 flagged) total p50=470 p90=593 p99=714 max=758 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=173.2 (sync 0.1 issue 49.1 swap 109.7) start-delay=136.5 queue=144.4
  FS more icon rep5: n=79 (+0 flagged) total p50=417 p90=510 p99=560 max=562 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=154.6 (sync 0.1 issue 48.8 swap 100.6) start-delay=118.8 queue=127.3
## A9. Totals (rep 0 is excluded)
| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 6 | 73 | **455.5** (406.7..482.7) | 581 | 707 | 0.4 | 184.7 | 0.0 | 0.2 | 0.1 | 50.8 | 110.0 |
| home | both | 5 | 75 | **449.4** (434.2..456.5) | 555 | 627 | 0.4 | 159.8 | 0.0 | 0.2 | 0.1 | 50.0 | 106.8 |
| home | hk | 6 | 72 | **450.2** (401.0..466.5) | 574 | 667 | 0.4 | 170.3 | 0.0 | 0.2 | 0.1 | 49.5 | 109.5 |
| home | icon | 5 | 74 | **463.4** (447.0..477.9) | 576 | 680 | 0.3 | 166.1 | 0.0 | 0.2 | 0.1 | 49.4 | 109.0 |
| home | new | 6 | 68 | **438.7** (421.3..463.2) | 593 | 672 | 0.4 | 185.8 | 0.0 | 0.2 | 0.1 | 51.3 | 110.5 |
| home | plate | 5 | 77 | **439.7** (424.1..473.7) | 543 | 669 | 0.3 | 161.5 | 0.0 | 0.2 | 0.1 | 48.8 | 107.0 |
| more | base | 6 | 66 | **434.5** (408.5..474.1) | 582 | 700 | 0.3 | 172.7 | 0.0 | 0.1 | 0.1 | 51.6 | 110.8 |
| more | both | 5 | 57 | **387.1** (371.6..419.2) | 512 | 729 | 0.2 | 145.8 | 0.0 | 0.1 | 0.1 | 49.0 | 94.3 |
| more | hk | 6 | 64 | **402.4** (351.6..419.7) | 535 | 629 | 0.3 | 159.6 | 0.0 | 0.1 | 0.1 | 50.0 | 100.9 |
| more | icon | 5 | 64 | **414.1** (409.1..446.5) | 560 | 706 | 0.3 | 158.2 | 0.0 | 0.1 | 0.1 | 50.5 | 100.6 |
| more | new | 6 | 62 | **422.1** (376.8..445.9) | 535 | 634 | 0.3 | 163.5 | 0.0 | 0.1 | 0.1 | 49.6 | 104.8 |
| more | plate | 5 | 62 | **383.3** (366.0..412.6) | 501 | 733 | 0.3 | 144.9 | 0.0 | 0.1 | 0.1 | 47.5 | 93.7 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [464.8, 453.1, 446.4, 406.7, 457.9, 482.7]
- home / both: [445.5, 449.4, 434.2, 452.0, 456.5] vs base: median diff -6.1 ms (UI -0.0, RT -24.9, issue -0.9); Mann-Whitney U=9.0, p=0.329; ranges overlap
- home / hk: [449.9, 450.6, 466.5, 401.0, 457.7, 416.8] vs base: median diff -5.2 ms (UI +0.0, RT -14.4, issue -1.3); Mann-Whitney U=13.0, p=0.485; ranges overlap
- home / icon: [451.3, 463.4, 447.0, 477.9, 469.8] vs base: median diff +8.0 ms (UI -0.1, RT -18.6, issue -1.4); Mann-Whitney U=18.0, p=0.662; ranges overlap
- home / new: [435.8, 421.3, 436.1, 463.2, 441.3, 453.8] vs base: median diff -16.8 ms (UI +0.0, RT +1.1, issue +0.4); Mann-Whitney U=11.0, p=0.310; ranges overlap
- home / plate: [428.3, 439.7, 449.6, 473.7, 424.1] vs base: median diff -15.8 ms (UI -0.1, RT -23.2, issue -2.1); Mann-Whitney U=10.0, p=0.429; ranges overlap
- more / base: [417.8, 474.1, 408.5, 454.3, 435.6, 433.4]
- more / both: [383.4, 415.6, 419.2, 371.6, 387.1] vs base: median diff -47.4 ms (UI -0.0, RT -26.8, issue -2.6); Mann-Whitney U=3.0, p=0.030; ranges overlap
- more / hk: [405.3, 408.9, 398.6, 351.6, 419.7, 399.5] vs base: median diff -32.1 ms (UI +0.0, RT -13.0, issue -1.6); Mann-Whitney U=3.0, p=0.015; ranges overlap
- more / icon: [409.1, 410.6, 446.5, 414.1, 416.6] vs base: median diff -20.4 ms (UI -0.0, RT -14.5, issue -1.1); Mann-Whitney U=8.0, p=0.247; ranges overlap
- more / new: [376.8, 415.0, 429.1, 445.9, 439.6, 397.9] vs base: median diff -12.4 ms (UI +0.0, RT -9.2, issue -2.1); Mann-Whitney U=11.0, p=0.310; ranges overlap
- more / plate: [412.6, 366.0, 384.9, 383.3, 375.9] vs base: median diff -51.2 ms (UI +0.0, RT -27.8, issue -4.1); Mann-Whitney U=1.0, p=0.009; ranges overlap
AT part: FATAL lines 0; host load: 4.35 4.44 4.41
contact sheets: 1 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5832', '8140', '12680', '15239', '17777', '20646', '23215']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 24
   skipped-frames events=24 max=104 total=1281
== Davey: 38
   davey events=38 max=2495ms median=1183ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## at33t

```
part at33t; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## AT0. Timeline and quiz: one suspect at a time (hook build flags), with 1.2.38 and 1.2.37 as references
X installed new: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 4889 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~3LdFKzQCcNJeM2h9Ii9hkA==/org.chyavorec.app-VHjzanYM2CuCZ7BWlv3c-g==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000;  versionCode=1990000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 3660 ms
X compile org.chyavorec.app.b37: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~k63fb3kJJXnXuGEnmLIBnA==/org.chyavorec.app.b37-A7soGo1G6Byq3As-ofM4fA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed hook: 1.2.38 1dd4ccb prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.hk, throwaway key, versionCode 1990101;  versionCode=1990101 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X hook: catalogue after 3715 ms
X compile org.chyavorec.app.hk: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~AnFe1_jvCfXwsd5aKuhk1A==/org.chyavorec.app.hk-T0YI6nxdiY4wV53fJJpMEQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /product/overlay/TelephonyProvider__auto_generated_rro_product.apk| x86_64: [status=run-from-apk] [reason=unknown]|
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
animations: animator_duration_scale=1 transition=1 window=1; host load: 4.30 3.57 1.74
## A. Repetition 0 of: timeline (configs rotated by 0)
  FS timeline new rep0: n=67 (+0 flagged) total p50=476 p90=657 p99=825 max=920 ms | medians: ui=1.0 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=181.2 (sync 0.3 issue 71.8 swap 96.3) start-delay=134.2 queue=147.4
  FS timeline base rep0: n=83 (+0 flagged) total p50=318 p90=528 p99=665 max=674 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=125.3 (sync 0.2 issue 65.9 swap 44.0) start-delay=91.5 queue=104.9
  FS timeline hk rep0: n=77 (+0 flagged) total p50=469 p90=684 p99=801 max=816 ms | medians: ui=1.0 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=207.8 (sync 0.2 issue 69.9 swap 96.5) start-delay=128.2 queue=147.3
## A. Repetition 1 of: timeline (configs rotated by 1)
  FS timeline base rep1: n=84 (+0 flagged) total p50=356 p90=542 p99=611 max=617 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=149.4 (sync 0.2 issue 64.3 swap 71.7) start-delay=95.8 queue=103.4
  FS timeline hk rep1: n=65 (+0 flagged) total p50=460 p90=622 p99=716 max=719 ms | medians: ui=1.2 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=211.3 (sync 0.2 issue 69.3 swap 93.5) start-delay=126.8 queue=141.7
  FS timeline scrub rep1: n=59 (+0 flagged) total p50=479 p90=662 p99=758 max=771 ms | medians: ui=0.7 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=216.3 (sync 0.2 issue 71.1 swap 120.4) start-delay=135.9 queue=142.7
  FS timeline medal rep1: n=84 (+0 flagged) total p50=345 p90=522 p99=727 max=734 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=123.2 (sync 0.2 issue 64.3 swap 51.0) start-delay=102.7 queue=110.4
  FS timeline thread rep1: n=77 (+0 flagged) total p50=478 p90=662 p99=756 max=762 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=210.1 (sync 0.3 issue 76.7 swap 86.7) start-delay=120.5 queue=137.9
  FS timeline card rep1: n=74 (+0 flagged) total p50=449 p90=619 p99=689 max=703 ms | medians: ui=1.0 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=179.5 (sync 0.2 issue 67.2 swap 92.6) start-delay=119.2 queue=142.3
  FS timeline enter rep1: n=71 (+0 flagged) total p50=468 p90=686 p99=780 max=791 ms | medians: ui=1.2 (input 0.0 anim 0.7 layout 0.0 draw 0.3) rt=215.9 (sync 0.2 issue 69.3 swap 99.6) start-delay=125.4 queue=142.8
  FS timeline all rep1: n=80 (+0 flagged) total p50=396 p90=554 p99=634 max=693 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.3) rt=167.6 (sync 0.2 issue 63.9 swap 102.6) start-delay=96.4 queue=108.9
  FS timeline new rep1: n=75 (+0 flagged) total p50=488 p90=665 p99=781 max=841 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=201.9 (sync 0.2 issue 70.5 swap 96.9) start-delay=130.2 queue=147.3
## A. Repetition 2 of: timeline (configs rotated by 2)
  FS timeline hk rep2: n=66 (+0 flagged) total p50=507 p90=669 p99=767 max=779 ms | medians: ui=0.9 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=212.4 (sync 0.2 issue 68.7 swap 104.3) start-delay=140.4 queue=149.1
  FS timeline scrub rep2: n=59 (+0 flagged) total p50=498 p90=714 p99=782 max=787 ms | medians: ui=0.8 (input 0.0 anim 0.4 layout 0.0 draw 0.2) rt=213.3 (sync 0.2 issue 74.3 swap 95.0) start-delay=135.1 queue=143.8
  FS timeline medal rep2: n=91 (+0 flagged) total p50=348 p90=546 p99=599 max=603 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=167.1 (sync 0.2 issue 65.5 swap 61.0) start-delay=101.7 queue=104.0
  FS timeline thread rep2: n=77 (+0 flagged) total p50=419 p90=610 p99=806 max=822 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=180.3 (sync 0.2 issue 67.3 swap 84.0) start-delay=114.3 queue=129.8
  FS timeline card rep2: n=73 (+0 flagged) total p50=502 p90=688 p99=812 max=819 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=217.6 (sync 0.2 issue 93.6 swap 95.3) start-delay=126.9 queue=149.2
  FS timeline enter rep2: n=76 (+0 flagged) total p50=511 p90=675 p99=757 max=779 ms | medians: ui=0.7 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=229.5 (sync 0.2 issue 80.1 swap 107.8) start-delay=138.1 queue=149.0
  FS timeline all rep2: n=71 (+0 flagged) total p50=431 p90=574 p99=697 max=733 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=172.0 (sync 0.2 issue 64.4 swap 102.1) start-delay=101.6 queue=121.9
  FS timeline new rep2: n=73 (+0 flagged) total p50=463 p90=606 p99=664 max=670 ms | medians: ui=1.1 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=207.2 (sync 0.2 issue 71.2 swap 91.4) start-delay=130.0 queue=142.8
  FS timeline base rep2: n=81 (+0 flagged) total p50=368 p90=536 p99=751 max=783 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=167.7 (sync 0.2 issue 65.8 swap 58.2) start-delay=95.4 queue=101.1
## A. Repetition 3 of: timeline (configs rotated by 3)
  FS timeline scrub rep3: n=56 (+0 flagged) total p50=594 p90=751 p99=797 max=798 ms | medians: ui=0.7 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=255.5 (sync 0.2 issue 77.1 swap 150.0) start-delay=174.8 queue=176.3
  FS timeline medal rep3: n=97 (+0 flagged) total p50=337 p90=539 p99=591 max=608 ms | medians: ui=0.6 (input 0.0 anim 0.4 layout 0.0 draw 0.1) rt=157.0 (sync 0.2 issue 64.7 swap 51.1) start-delay=90.3 queue=102.7
  FS timeline thread rep3: n=71 (+0 flagged) total p50=473 p90=687 p99=777 max=788 ms | medians: ui=0.7 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=211.0 (sync 0.2 issue 77.1 swap 88.5) start-delay=135.0 queue=147.1
  FS timeline card rep3: n=65 (+0 flagged) total p50=463 p90=679 p99=765 max=774 ms | medians: ui=1.0 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=186.0 (sync 0.2 issue 70.9 swap 91.4) start-delay=130.1 queue=140.9
  FS timeline enter rep3: n=69 (+0 flagged) total p50=497 p90=702 p99=796 max=819 ms | medians: ui=1.2 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=215.2 (sync 0.1 issue 68.6 swap 101.1) start-delay=128.2 queue=141.4
  FS timeline all rep3: n=79 (+0 flagged) total p50=382 p90=547 p99=587 max=592 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=162.3 (sync 0.1 issue 64.3 swap 96.7) start-delay=92.7 queue=97.0
  FS timeline new rep3: n=73 (+0 flagged) total p50=494 p90=637 p99=744 max=811 ms | medians: ui=1.1 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=206.1 (sync 0.3 issue 69.7 swap 92.0) start-delay=129.7 queue=137.9
  FS timeline base rep3: n=79 (+0 flagged) total p50=322 p90=570 p99=704 max=733 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=147.0 (sync 0.2 issue 64.4 swap 49.7) start-delay=89.1 queue=99.6
  FS timeline hk rep3: n=72 (+0 flagged) total p50=462 p90=554 p99=631 max=667 ms | medians: ui=1.1 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=175.9 (sync 0.2 issue 67.4 swap 92.2) start-delay=123.0 queue=132.1
## A. Repetition 4 of: timeline (configs rotated by 4)
  FS timeline medal rep4: n=70 (+0 flagged) total p50=464 p90=631 p99=757 max=795 ms | medians: ui=0.7 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=176.6 (sync 0.2 issue 72.1 swap 104.1) start-delay=130.5 queue=138.5
  FS timeline thread rep4: n=82 (+0 flagged) total p50=399 p90=546 p99=657 max=672 ms | medians: ui=1.1 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=156.6 (sync 0.1 issue 68.0 swap 80.6) start-delay=122.5 queue=131.9
  FS timeline card rep4: n=67 (+0 flagged) total p50=450 p90=621 p99=731 max=753 ms | medians: ui=1.0 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=198.5 (sync 0.2 issue 67.8 swap 93.1) start-delay=128.5 queue=139.4
  FS timeline enter rep4: n=66 (+0 flagged) total p50=495 p90=765 p99=854 max=867 ms | medians: ui=1.2 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=209.6 (sync 0.1 issue 74.1 swap 93.3) start-delay=134.3 queue=148.5
  FS timeline all rep4: n=79 (+0 flagged) total p50=429 p90=601 p99=687 max=700 ms | medians: ui=0.7 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=177.6 (sync 0.2 issue 65.6 swap 104.9) start-delay=105.2 queue=110.2
  FS timeline new rep4: n=69 (+0 flagged) total p50=454 p90=609 p99=806 max=921 ms | medians: ui=1.0 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=165.2 (sync 0.2 issue 70.1 swap 89.6) start-delay=131.6 queue=140.9
  FS timeline base rep4: n=85 (+0 flagged) total p50=390 p90=595 p99=659 max=679 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=171.0 (sync 0.2 issue 69.5 swap 104.2) start-delay=110.6 queue=128.3
  FS timeline hk rep4: n=72 (+0 flagged) total p50=508 p90=651 p99=820 max=826 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=221.3 (sync 0.2 issue 71.9 swap 104.9) start-delay=142.5 queue=150.5
  FS timeline scrub rep4: n=58 (+0 flagged) total p50=511 p90=711 p99=833 max=844 ms | medians: ui=0.6 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=222.1 (sync 0.2 issue 68.3 swap 98.0) start-delay=137.8 queue=153.5
## A. Repetition 5 of: timeline (configs rotated by 5)
  FS timeline thread rep5: n=67 (+0 flagged) total p50=436 p90=675 p99=736 max=745 ms | medians: ui=0.9 (input 0.0 anim 0.6 layout 0.0 draw 0.2) rt=166.5 (sync 0.2 issue 72.9 swap 84.5) start-delay=128.5 queue=138.8
  FS timeline card rep5: n=79 (+0 flagged) total p50=452 p90=551 p99=732 max=783 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.1) rt=171.5 (sync 0.2 issue 74.3 swap 88.0) start-delay=127.8 queue=141.9
  FS timeline enter rep5: n=59 (+0 flagged) total p50=481 p90=774 p99=845 max=855 ms | medians: ui=1.3 (input 0.0 anim 0.8 layout 0.0 draw 0.2) rt=220.9 (sync 0.1 issue 72.5 swap 99.9) start-delay=125.0 queue=145.6
  FS timeline all rep5: n=79 (+0 flagged) total p50=426 p90=620 p99=704 max=749 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=179.3 (sync 0.2 issue 68.9 swap 95.9) start-delay=108.7 queue=128.3
  FS timeline new rep5: n=70 (+0 flagged) total p50=452 p90=724 p99=805 max=814 ms | medians: ui=1.0 (input 0.0 anim 0.7 layout 0.0 draw 0.2) rt=186.5 (sync 0.2 issue 71.5 swap 95.8) start-delay=134.6 queue=146.0
  FS timeline base rep5: n=68 (+0 flagged) total p50=465 p90=610 p99=737 max=738 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=186.3 (sync 0.2 issue 70.6 swap 108.9) start-delay=114.7 queue=142.3
  FS timeline hk rep5: n=74 (+0 flagged) total p50=518 p90=684 p99=805 max=823 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=218.0 (sync 0.2 issue 72.2 swap 96.6) start-delay=137.7 queue=149.8
  FS timeline scrub rep5: n=58 (+0 flagged) total p50=450 p90=571 p99=719 max=733 ms | medians: ui=0.7 (input 0.0 anim 0.4 layout 0.0 draw 0.1) rt=172.0 (sync 0.2 issue 70.1 swap 89.8) start-delay=122.6 queue=141.3
  FS timeline medal rep5: n=80 (+0 flagged) total p50=321 p90=521 p99=700 max=718 ms | medians: ui=0.8 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=134.5 (sync 0.2 issue 65.1 swap 49.5) start-delay=79.4 queue=94.3
## A. Repetition 0 of: quiz (configs rotated by 0)
QUIZ taps hk: 5 questions learned: input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
QUIZ taps noband: 5 questions learned: input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
## A. Repetition 1 of: quiz (configs rotated by 1)
  FS quiz noband rep1: n=102 (+6 flagged) total p50=442 p90=550 p99=617 max=618 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=204.5 (sync 0.1 issue 71.2 swap 133.8) start-delay=87.4 queue=120.1
  FS quiz hk rep1: n=82 (+6 flagged) total p50=587 p90=784 p99=846 max=900 ms | medians: ui=0.7 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=274.7 (sync 0.2 issue 74.0 swap 161.3) start-delay=124.6 queue=188.8
## A. Repetition 2 of: quiz (configs rotated by 2)
  FS quiz hk rep2: n=90 (+0 flagged) total p50=612 p90=756 p99=835 max=850 ms | medians: ui=0.7 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=263.7 (sync 0.2 issue 71.8 swap 144.2) start-delay=155.7 queue=197.6
  FS quiz noband rep2: n=104 (+7 flagged) total p50=417 p90=534 p99=586 max=629 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=197.1 (sync 0.1 issue 69.0 swap 84.6) start-delay=91.5 queue=127.9
## A. Repetition 3 of: quiz (configs rotated by 3)
  FS quiz noband rep3: n=105 (+6 flagged) total p50=414 p90=549 p99=568 max=579 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=200.0 (sync 0.1 issue 68.5 swap 81.4) start-delay=95.8 queue=127.6
  FS quiz hk rep3: n=87 (+2 flagged) total p50=598 p90=761 p99=853 max=883 ms | medians: ui=0.7 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=260.1 (sync 0.1 issue 69.7 swap 189.1) start-delay=132.6 queue=185.3
## A. Repetition 4 of: quiz (configs rotated by 4)
  FS quiz hk rep4: n=88 (+2 flagged) total p50=619 p90=788 p99=841 max=893 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=275.2 (sync 0.1 issue 73.9 swap 173.5) start-delay=160.9 queue=195.0
  FS quiz noband rep4: n=103 (+6 flagged) total p50=440 p90=550 p99=585 max=586 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=198.9 (sync 0.1 issue 64.8 swap 122.1) start-delay=99.4 queue=123.6
## A. Repetition 5 of: quiz (configs rotated by 5)
  FS quiz noband rep5: n=101 (+7 flagged) total p50=427 p90=535 p99=589 max=600 ms | medians: ui=0.6 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=201.0 (sync 0.1 issue 70.2 swap 120.4) start-delay=101.1 queue=128.3
  FS quiz hk rep5: n=84 (+5 flagged) total p50=589 p90=767 p99=823 max=851 ms | medians: ui=0.7 (input 0.0 anim 0.2 layout 0.0 draw 0.4) rt=269.4 (sync 0.1 issue 73.8 swap 168.6) start-delay=127.7 queue=193.9
## A9. Totals (rep 0 is excluded)
| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| quiz | hk | 5 | 87 | **597.5** (586.5..619.2) | 767 | 841 | 0.7 | 269.4 | 0.0 | 0.4 | 0.1 | 73.8 | 168.6 |
| quiz | noband | 5 | 103 | **427.4** (414.1..441.9) | 549 | 586 | 0.6 | 200.0 | 0.0 | 0.4 | 0.1 | 69.1 | 120.3 |
| timeline | all | 5 | 79 | **425.9** (382.1..431.5) | 574 | 687 | 0.5 | 172.0 | 0.0 | 0.2 | 0.2 | 64.4 | 102.1 |
| timeline | base | 6 | 82 | **361.7** (317.9..464.9) | 556 | 685 | 0.5 | 158.5 | 0.0 | 0.2 | 0.2 | 65.9 | 65.0 |
| timeline | card | 5 | 73 | **451.6** (449.1..501.8) | 621 | 732 | 1.0 | 186.0 | 0.0 | 0.2 | 0.2 | 70.9 | 92.6 |
| timeline | enter | 5 | 69 | **494.8** (468.0..511.4) | 702 | 796 | 1.2 | 215.9 | 0.0 | 0.2 | 0.1 | 72.5 | 99.9 |
| timeline | hk | 6 | 72 | **488.0** (460.0..518.4) | 660 | 784 | 1.0 | 211.8 | 0.0 | 0.2 | 0.2 | 69.6 | 96.6 |
| timeline | medal | 5 | 84 | **345.3** (320.9..464.1) | 539 | 700 | 0.8 | 157.0 | 0.0 | 0.2 | 0.2 | 65.1 | 51.1 |
| timeline | new | 6 | 72 | **469.7** (451.6..494.4) | 647 | 793 | 1.0 | 194.2 | 0.0 | 0.2 | 0.2 | 70.8 | 93.9 |
| timeline | scrub | 5 | 58 | **498.3** (449.9..594.5) | 711 | 782 | 0.7 | 216.3 | 0.0 | 0.2 | 0.2 | 71.1 | 98.0 |
| timeline | thread | 5 | 77 | **435.7** (399.4..477.9) | 662 | 756 | 0.8 | 180.3 | 0.0 | 0.2 | 0.2 | 72.9 | 84.5 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- quiz / hk: [586.5, 612.1, 597.5, 619.2, 588.6]
- quiz / noband: [441.9, 417.2, 414.1, 439.9, 427.4] vs hk: median diff -170.1 ms (UI -0.0, RT -69.4, issue -4.7); Mann-Whitney U=0.0, p=0.008; ranges do NOT overlap
- timeline / all: [395.7, 431.5, 382.1, 428.6, 425.9]
- timeline / base: [317.9, 355.8, 367.6, 321.9, 390.3, 464.9] vs all: median diff -64.1 ms (UI -0.0, RT -13.5, issue +1.5); Mann-Whitney U=6.0, p=0.126; ranges overlap
- timeline / card: [449.1, 501.8, 462.6, 450.1, 451.6] vs all: median diff +25.7 ms (UI +0.5, RT +14.0, issue +6.5); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / enter: [468.0, 511.4, 496.6, 494.8, 480.7] vs all: median diff +69.0 ms (UI +0.7, RT +43.9, issue +8.1); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / hk: [469.3, 460.0, 506.7, 462.3, 507.8, 518.4] vs all: median diff +62.1 ms (UI +0.4, RT +39.8, issue +5.2); Mann-Whitney U=30.0, p=0.004; ranges do NOT overlap
- timeline / medal: [345.3, 347.8, 337.4, 464.1, 320.9] vs all: median diff -80.5 ms (UI +0.2, RT -15.0, issue +0.7); Mann-Whitney U=5.0, p=0.151; ranges overlap
- timeline / new: [476.2, 487.9, 463.3, 494.4, 454.4, 451.6] vs all: median diff +43.9 ms (UI +0.5, RT +22.2, issue +6.4); Mann-Whitney U=30.0, p=0.004; ranges do NOT overlap
- timeline / scrub: [479.4, 498.3, 594.5, 510.8, 449.9] vs all: median diff +72.4 ms (UI +0.2, RT +44.4, issue +6.7); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / thread: [477.9, 419.1, 473.1, 399.4, 435.7] vs all: median diff +9.8 ms (UI +0.3, RT +8.3, issue +8.5); Mann-Whitney U=19.0, p=0.222; ranges overlap
AT part: FATAL lines 3; host load: 3.84 4.25 4.35
contact sheets: 2 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5081', '8543', '13022', '16283', '19344', '22334', '25348']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 35
   skipped-frames events=35 max=119 total=2080
== Davey: 61
   davey events=61 max=3248ms median=991ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 4 block(s) {'ANR': 1, 'FATAL': 3} → crashes.txt
```

## cf28

```
part cf28; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
animations: animator_duration_scale=1 transition=1 window=1
## CF0. The hook build (same code as 1.2.38 + Log.w lines), first launch
X installed hook: 1.2.38 1dd4ccb prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.hk, throwaway key, versionCode 1990101;  versionCode=1990101 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X hook: catalogue after 2115 ms
anim after the first launch: animator_duration_scale=1 transition=1 window=1
## CF1. Control: confetti on „Искам да прочета“ (host recording), hook build
CONTROL hook: want-to-read before: checked=false selected=false bounds=[426,673][926,799]
REC want-hook (emu): marker 1529 ms after the recording start; 57 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the control tap (hook):
+   162 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(705.0, 736.0) lastTap=Offset(705.0, 736.0)
+   171 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.0 alpha=1.0
+   434 ms  W25     : CH frame 1 progress=0.090914525 +272ms draws=1
+   435 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.090914525 alpha=1.0
+   468 ms  W25     : CH frame 2 progress=0.24450614 +306ms draws=2
+   469 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.24450614 alpha=1.0
+   648 ms  W25     : CH frame 3 progress=0.2767965 +486ms draws=3
+   649 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.2767965 alpha=1.0
+   761 ms  W25     : CH frame 4 progress=0.44388375 +599ms draws=4
+   764 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.44388375 alpha=1.0
+   808 ms  W25     : CH frame 5 progress=0.53702027 +646ms draws=5
+   810 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.53702027 alpha=1.0
+   965 ms  W25     : CH frame 6 progress=0.5840636 +803ms draws=6
+   966 ms  W25     : CL draw #7 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.5840636 alpha=1.0
+   966 ms  W25     : CH frame 7 progress=0.7206398 +805ms draws=7
+   972 ms  W25     : CL draw #8 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.7206398 alpha=0.79817194
+  1052 ms  W25     : CH frame 8 progress=0.73764867 +891ms draws=8
+  1053 ms  W25     : CL draw #9 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.73764867 alpha=0.74957526
+  1170 ms  W25     : CH frame 9 progress=0.7984312 +1009ms draws=9
+  1171 ms  W25     : CL draw #10 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.7984312 alpha=0.5759108
+  1287 ms  W25     : CH frame 10 progress=0.9053359 +1125ms draws=10
+  1287 ms  W25     : CL draw #11 size=Size(1080.0, 2400.0) at=Offset(705.0, 736.0) progress=0.9053359 alpha=0.27046886
+  1336 ms  W25     : CH frame 11 progress=1.0 +1174ms draws=11
+  1336 ms  W25     : CH effect end bursts=1 frames=11 draws=11 +1174ms progress=1.0
FRAMES want-hook: confetti-like new saturated points outside the band, per frame (level at the end 98; peak #16 = 246; frames with >=6: 47, first #11, last #57): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 25, 98, 245, 230, 246, 192, 182, 124, 122, 101, 101, 102, 102, 101, 97, 98, 98, 98, 98, 101, 100, 100, 100, 100, 95, 99, 100, 100, 99, 97, 97, 97, 97, 97, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98] vertical span of those points: 30-31% 30-31% 30-31% 25-31% 20-32% 20-33% 25-40% 27-48% 30-56% 30-57% 30-31% 30-31% 
CONTROL hook: want-to-read after: checked=true selected=false bounds=[426,673][926,799]
## CF2. Quiz pass 1 (hook): the first options, the right answers are learned
QUIZ learn q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q3: WRONG	Катрин Уеб; content-desc="2 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q4: WRONG	Даниъл Дж. Бурстин; content-desc="2 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q5: WRONG	1922; content-desc="2 верни от 5 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ answers learned: Решение за собствена сграда|Първият радиоапарат в селото|Катрин Уеб|Даниъл Дж. Бурстин|1922|
## CF3. Quiz 5/5 on the hook build, no recording (pure timing from the log)
QUIZ hk-none q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q5: answer at [540 910]; band label bounds [327 1808 753 1845]; next button y 2163; animator_duration_scale=1 transition=1 window=1
W25 log after the 5th answer (hk-none; times from the marker just before 'input tap'):
+    96 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1073 ms  W25     : QB after stitch i=4 +977ms
+  1468 ms  W25     : QB after close +1371ms
+  1468 ms  W25     : QB after haptics +1372ms
+  1469 ms  W25     : QB burst called=true origin=Offset(540.5, 1959.5) bounds=Rect.fromLTRB(68.0, 1856.0, 1013.0, 2063.0) bursts=1 +1372ms
+  1469 ms  W25     : QB effect end size=5 +1372ms active=true
+  1574 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 1959.5) lastTap=Offset(540.0, 910.0)
+  1576 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.0 alpha=1.0
+  1612 ms  W25     : CH frame 1 progress=0.03165625 +37ms draws=1
+  1616 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.03165625 alpha=1.0
+  1714 ms  W25     : CH frame 2 progress=0.047147032 +139ms draws=2
+  1723 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.047147032 alpha=1.0
+  1742 ms  W25     : CH frame 3 progress=0.13829194 +167ms draws=3
+  1742 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.13829194 alpha=1.0
+  1848 ms  W25     : CH frame 4 progress=0.15479775 +273ms draws=4
+  1849 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.15479775 alpha=1.0
+  1877 ms  W25     : CH frame 5 progress=0.24594268 +302ms draws=5
+  1877 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.24594268 alpha=1.0
+  1988 ms  W25     : CH frame 6 progress=0.27764127 +413ms draws=6
+  1988 ms  W25     : CL draw #7 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.27764127 alpha=1.0
+  2011 ms  W25     : CH frame 7 progress=0.38413176 +436ms draws=7
+  2011 ms  W25     : CL draw #8 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.38413176 alpha=1.0
+  2114 ms  W25     : CH frame 8 progress=0.40088767 +539ms draws=8
+  2115 ms  W25     : CL draw #9 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.40088767 alpha=1.0
+  2141 ms  W25     : CH frame 9 progress=0.49203125 +567ms draws=9
+  2142 ms  W25     : CL draw #10 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.49203125 alpha=1.0
+  2257 ms  W25     : CH frame 10 progress=0.5237091 +682ms draws=10
+  2257 ms  W25     : CL draw #11 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.5237091 alpha=1.0
+  2271 ms  W25     : CH frame 11 progress=0.6302223 +697ms draws=11
+  2273 ms  W25     : CL draw #12 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.6302223 alpha=1.0
+  2372 ms  W25     : CH frame 12 progress=0.64700156 +797ms draws=12
+  2377 ms  W25     : CL draw #13 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.64700156 alpha=1.0
+  2399 ms  W25     : CH frame 13 progress=0.72299206 +824ms draws=13
+  2400 ms  W25     : CL draw #14 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.72299206 alpha=0.7914513
+  2503 ms  W25     : CH frame 14 progress=0.75468796 +928ms draws=14
+  2503 ms  W25     : CL draw #15 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.75468796 alpha=0.70089155
+  2529 ms  W25     : CH frame 15 progress=0.8458282 +955ms draws=15
+  2530 ms  W25     : CL draw #16 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.8458282 alpha=0.44049093
+  2638 ms  W25     : CH frame 16 progress=0.87773985 +1063ms draws=16
+  2646 ms  W25     : CL draw #17 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.87773985 alpha=0.34931472
+  2667 ms  W25     : CH frame 17 progress=0.9840312 +1092ms draws=17
+  2668 ms  W25     : CL draw #18 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.9840312 alpha=0.045625143
+  2766 ms  W25     : CH frame 18 progress=1.0 +1192ms draws=18
+  2766 ms  W25     : CH effect end bursts=1 frames=18 draws=18 +1192ms progress=1.0
QUIZ hk-none q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-none result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF4. Quiz 5/5 on the hook build, recorded from the host (adb emu screenrecord)
QUIZ hk-emu q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q5: answer at [540 910]; band label bounds [327 1808 753 1845]; next button y 2163; animator_duration_scale=1 transition=1 window=1
REC q5-hk-emu (emu): marker 1532 ms after the recording start; 86 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-emu; times from the marker just before 'input tap'):
+   137 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1289 ms  W25     : QB after stitch i=4 +1152ms
+  1792 ms  W25     : QB after close +1656ms
+  1794 ms  W25     : QB after haptics +1657ms
+  1794 ms  W25     : QB burst called=true origin=Offset(540.5, 1959.5) bounds=Rect.fromLTRB(68.0, 1856.0, 1013.0, 2063.0) bursts=1 +1657ms
+  1794 ms  W25     : QB effect end size=5 +1658ms active=true
+  1796 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 1959.5) lastTap=Offset(540.0, 910.0)
+  1818 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.0 alpha=1.0
+  1994 ms  W25     : CH frame 1 progress=0.16786312 +198ms draws=1
+  2009 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.16786312 alpha=1.0
+  2071 ms  W25     : CH frame 2 progress=0.1796776 +274ms draws=2
+  2071 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.1796776 alpha=1.0
+  2274 ms  W25     : CH frame 3 progress=0.22632647 +478ms draws=3
+  2275 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.22632647 alpha=1.0
+  2429 ms  W25     : CH frame 4 progress=0.41168043 +633ms draws=4
+  2430 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.41168043 alpha=1.0
+  2508 ms  W25     : CH frame 5 progress=0.5610528 +712ms draws=5
+  2509 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.5610528 alpha=1.0
+  2755 ms  W25     : CH frame 6 progress=0.63230664 +959ms draws=6
+  2756 ms  W25     : CL draw #7 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.63230664 alpha=1.0
+  2861 ms  W25     : CH frame 7 progress=0.8491575 +1064ms draws=7
+  2861 ms  W25     : CL draw #8 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.8491575 alpha=0.43097854
+  3050 ms  W25     : CH frame 8 progress=0.9577409 +1254ms draws=8
+  3051 ms  W25     : CL draw #9 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.9577409 alpha=0.12074028
+  3198 ms  W25     : CH frame 9 progress=1.0 +1401ms draws=9
+  3198 ms  W25     : CH effect end bursts=1 frames=9 draws=9 +1402ms progress=1.0
FRAMES q5-hk-emu: confetti-like new saturated points outside the band, per frame (level at the end 4699; peak #38 = 4719; frames with >=6: 78, first #9, last #86): [0, 0, 0, 0, 0, 0, 0, 0, 4697, 4695, 4699, 4697, 4697, 4695, 4698, 4697, 4695, 4696, 4702, 4697, 4695, 4695, 4701, 4700, 4699, 4698, 4698, 4699, 4699, 4698, 4697, 4697, 4698, 4697, 4698, 4699, 4714, 4719, 4701, 4701, 4698, 4699, 4698, 4699, 4699, 4699, 4698, 4698, 4699, 4699, 4699, 4698, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699] vertical span of those points: 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 37-92% 38-92% 38-92% 38-92% 38-92% 38-92% 
QUIZ hk-emu q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-emu result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF5. Quiz 5/5 on the hook build, recorded in the guest (screenrecord 540x1200 — loads the software GPU)
QUIZ hk-adb q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q5: answer at [540 910]; band label bounds [327 1808 753 1845]; next button y 2163; animator_duration_scale=1 transition=1 window=1
REC q5-hk-adb (adb): marker 2017 ms after the recording start; 29 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-adb; times from the marker just before 'input tap'):
+    93 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1146 ms  W25     : QB after stitch i=4 +1052ms
+  1512 ms  W25     : QB after close +1419ms
+  1516 ms  W25     : QB after haptics +1422ms
+  1516 ms  W25     : QB burst called=true origin=Offset(540.5, 1959.5) bounds=Rect.fromLTRB(68.0, 1856.0, 1013.0, 2063.0) bursts=1 +1422ms
+  1516 ms  W25     : QB effect end size=5 +1422ms active=true
+  1605 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 1959.5) lastTap=Offset(540.0, 910.0)
+  1629 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.0 alpha=1.0
+  1697 ms  W25     : CH frame 1 progress=0.07751444 +91ms draws=1
+  1708 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.07751444 alpha=1.0
+  1745 ms  W25     : CH frame 2 progress=0.09483724 +140ms draws=2
+  1746 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.09483724 alpha=1.0
+  1889 ms  W25     : CH frame 3 progress=0.12558016 +284ms draws=3
+  1890 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.12558016 alpha=1.0
+  1992 ms  W25     : CH frame 4 progress=0.26282474 +387ms draws=4
+  1993 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.26282474 alpha=1.0
+  2078 ms  W25     : CH frame 5 progress=0.35765338 +473ms draws=5
+  2079 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.35765338 alpha=1.0
+  2146 ms  W25     : CH frame 6 progress=0.4360418 +541ms draws=6
+  2147 ms  W25     : CL draw #7 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.4360418 alpha=1.0
+  2253 ms  W25     : CH frame 7 progress=0.4988393 +648ms draws=7
+  2306 ms  W25     : CL draw #8 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.4988393 alpha=1.0
+  2386 ms  W25     : CH frame 8 progress=0.6369086 +781ms draws=8
+  2387 ms  W25     : CL draw #9 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.6369086 alpha=1.0
+  2496 ms  W25     : CH frame 9 progress=0.7166151 +890ms draws=9
+  2499 ms  W25     : CL draw #10 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.7166151 alpha=0.8096712
+  2529 ms  W25     : CH frame 10 progress=0.809716 +924ms draws=10
+  2530 ms  W25     : CL draw #11 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.809716 alpha=0.5436686
+  2716 ms  W25     : CH frame 11 progress=0.84261936 +1110ms draws=11
+  2726 ms  W25     : CL draw #12 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.84261936 alpha=0.449659
+  2798 ms  W25     : CH frame 12 progress=1.0 +1192ms draws=12
+  2798 ms  W25     : CH effect end bursts=1 frames=12 draws=12 +1192ms progress=1.0
FRAMES q5-hk-adb: confetti-like new saturated points outside the band, per frame (level at the end 32; peak #26 = 51; frames with >=6: 26, first #4, last #29): [0, 0, 2, 8, 10, 15, 15, 18, 22, 22, 23, 23, 22, 26, 25, 31, 31, 31, 31, 31, 31, 31, 37, 44, 44, 51, 32, 31, 31] vertical span of those points: 60-62% 60-62% 60-62% 60-62% 60-62% 60-62% 60-62% 60-62% 60-62% 60-62% 60-62% 60-62% 
QUIZ hk-adb q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-adb result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF6. Quiz 5/5 on the hook build, „Виж резултата“ NOT tapped for 12 s, host recording (second sample)
QUIZ hk-long q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q5: answer at [540 910]; band label bounds [327 1808 753 1845]; next button y 2163; animator_duration_scale=1 transition=1 window=1
REC q5-hk-long (emu): marker 1543 ms after the recording start; 127 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-long; times from the marker just before 'input tap'):
+   149 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1293 ms  W25     : QB after stitch i=4 +1143ms
+  1882 ms  W25     : QB after close +1732ms
+  1884 ms  W25     : QB after haptics +1735ms
+  1884 ms  W25     : QB burst called=true origin=Offset(540.5, 1959.5) bounds=Rect.fromLTRB(68.0, 1856.0, 1013.0, 2063.0) bursts=1 +1735ms
+  1885 ms  W25     : QB effect end size=5 +1735ms active=true
+  1886 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 1959.5) lastTap=Offset(540.0, 910.0)
+  1903 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.0 alpha=1.0
+  1933 ms  W25     : CH frame 1 progress=0.030645603 +47ms draws=1
+  1950 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.030645603 alpha=1.0
+  2074 ms  W25     : CH frame 2 progress=0.04669974 +188ms draws=2
+  2075 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.04669974 alpha=1.0
+  2291 ms  W25     : CH frame 3 progress=0.15321118 +405ms draws=3
+  2291 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.15321118 alpha=1.0
+  2394 ms  W25     : CH frame 4 progress=0.35330394 +508ms draws=4
+  2394 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.35330394 alpha=1.0
+  2614 ms  W25     : CH frame 5 progress=0.44983664 +728ms draws=5
+  2614 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.44983664 alpha=1.0
+  2753 ms  W25     : CH frame 6 progress=0.64944464 +867ms draws=6
+  2753 ms  W25     : CL draw #7 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.64944464 alpha=1.0
+  2885 ms  W25     : CH frame 7 progress=0.7763598 +999ms draws=7
+  2886 ms  W25     : CL draw #8 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.7763598 alpha=0.63897204
+  2931 ms  W25     : CH frame 8 progress=0.8859165 +1046ms draws=8
+  2932 ms  W25     : CL draw #9 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.8859165 alpha=0.32595295
+  3157 ms  W25     : CH frame 9 progress=0.9346278 +1271ms draws=9
+  3160 ms  W25     : CL draw #10 size=Size(1080.0, 2400.0) at=Offset(540.5, 1959.5) progress=0.9346278 alpha=0.1867778
+  3271 ms  W25     : CH frame 10 progress=1.0 +1386ms draws=10
+  3271 ms  W25     : CH effect end bursts=1 frames=10 draws=10 +1386ms progress=1.0
FRAMES q5-hk-long: confetti-like new saturated points outside the band, per frame (level at the end 4699; peak #38 = 4704; frames with >=6: 118, first #10, last #127): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4694, 4696, 4695, 4694, 4695, 4695, 4698, 4697, 4699, 4697, 4698, 4698, 4699, 4701, 4701, 4700, 4699, 4700, 4699, 4698, 4698, 4698, 4697, 4697, 4697, 4698, 4696, 4698, 4704, 4699, 4699, 4697, 4698, 4697, 4697, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4698, 4698, 4698, 4698, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4698, 4698, 4 vertical span of those points: 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 
QUIZ hk-long q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-long result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
CF hook: FATAL(any process)=0 of the app=0 ANR=0
## CF7. The released 1.2.38 itself (no log lines): control and quiz 5/5, host recording
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CONTROL new: want-to-read before: checked=false selected=false bounds=[426,673][926,799]
REC want-new (emu): marker 1526 ms after the recording start; 57 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the control tap (new):
(no W25 lines after the marker want-new)
FRAMES want-new: confetti-like new saturated points outside the band, per frame (level at the end 98; peak #15 = 240; frames with >=6: 49, first #9, last #57): [0, 0, 0, 0, 0, 0, 0, 0, 24, 23, 25, 188, 235, 235, 240, 172, 195, 212, 211, 97, 101, 101, 102, 102, 102, 97, 98, 99, 99, 99, 101, 100, 101, 101, 101, 95, 99, 100, 100, 99, 97, 97, 97, 97, 95, 98, 98, 98, 98, 98, 98, 98, 104, 98, 98, 98, 98] vertical span of those points: 30-31% 30-31% 30-31% 29-31% 21-31% 21-31% 22-36% 23-38% 25-41% 29-49% 29-49% 30-31% 
CONTROL new: want-to-read after: checked=true selected=false bounds=[426,673][926,799]
QUIZ new-e1 q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q5: answer at [540 910]; band label bounds [327 1808 753 1845]; next button y 2163; animator_duration_scale=1 transition=1 window=1
REC q5-new-e1 (emu): marker 1533 ms after the recording start; 97 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (new-e1; times from the marker just before 'input tap'):
(no W25 lines after the marker q5-new-e1)
FRAMES q5-new-e1: confetti-like new saturated points outside the band, per frame (level at the end 4699; peak #37 = 4716; frames with >=6: 88, first #10, last #97): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4696, 4696, 4695, 4695, 4696, 4697, 4698, 4699, 4700, 4700, 4701, 4701, 4699, 4698, 4699, 4698, 4700, 4699, 4698, 4698, 4698, 4698, 4698, 4698, 4699, 4698, 4715, 4716, 4701, 4698, 4698, 4697, 4698, 4696, 4696, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4698, 4698, 4698, 4699, 4699, 4699, 4699, 4699, 4699, 4699, 4698, 4698, 4698, 4698, 4699, 4699, 4699, 4699, 4699, 4699, 4698, 4698, 4698, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 469 vertical span of those points: 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 37-92% 38-92% 38-92% 38-92% 38-92% 38-92% 
QUIZ new-e1 q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ new-e1 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ new-e2 q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q3: UNKNOWN; 
QUIZ new-e2 q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q5: answer at [540 910]; band label bounds [327 1808 753 1845]; next button y 2163; animator_duration_scale=1 transition=1 window=1
REC q5-new-e2 (emu): marker 1529 ms after the recording start; 97 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (new-e2; times from the marker just before 'input tap'):
(no W25 lines after the marker q5-new-e2)
FRAMES q5-new-e2: confetti-like new saturated points outside the band, per frame (level at the end 4699; peak #37 = 4715; frames with >=6: 88, first #10, last #97): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4696, 4696, 4697, 4695, 4695, 4697, 4697, 4699, 4698, 4701, 4700, 4702, 4698, 4698, 4699, 4700, 4699, 4699, 4698, 4699, 4698, 4698, 4698, 4698, 4698, 4712, 4711, 4715, 4699, 4698, 4698, 4696, 4696, 4696, 4696, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4699, 4699, 4698, 4699, 4699, 4698, 4699, 4698, 4699, 4699, 4698, 4698, 4698, 4698, 4699, 4698, 4698, 4698, 4698, 4699, 4698, 4698, 4699, 4699, 4699, 4698, 4698, 4699, 4699, 4699, 4699, 4699, 469 vertical span of those points: 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 38-92% 
QUIZ new-e2 q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ new-e2 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
CF part: FATAL(any process)=0 of the app=0 ANR=0; Davey lines of the apps: 7
contact sheets: 44 in sheets/
contact sheets: 44 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['10265', '10584', '11276']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=32 total=32
== Davey: 2
   davey events=2 max=842ms median=800ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## cf33

```
part cf33; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
animations: animator_duration_scale=1 transition=1 window=1
## CF0. The hook build (same code as 1.2.38 + Log.w lines), first launch
X installed hook: 1.2.38 1dd4ccb prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.hk, throwaway key, versionCode 1990101;  versionCode=1990101 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X hook: catalogue after 7641 ms
anim after the first launch: animator_duration_scale=1 transition=1 window=1
## CF1. Control: confetti on „Искам да прочета“ (host recording), hook build
CONTROL hook: want-to-read before: checked=false selected=false bounds=[426,738][922,864]
REC want-hook (emu): marker 1583 ms after the recording start; 56 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the control tap (hook):
+  1063 ms  Choreographer: Skipped 46 frames!  The application may be doing too much work on its main thread.
+  1487 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(703.0, 801.0) lastTap=Offset(703.0, 801.0)
+  1681 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(703.0, 801.0) progress=0.0 alpha=1.0
+  2057 ms  OpenGLRenderer: Davey! duration=1695ms
+  2267 ms  W25     : CH frame 1 progress=0.530303 +779ms draws=1
+  2269 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(703.0, 801.0) progress=0.530303 alpha=1.0
+  2561 ms  OpenGLRenderer: Davey! duration=950ms
+  2638 ms  W25     : CH frame 2 progress=0.6969697 +1150ms draws=2
+  2640 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(703.0, 801.0) progress=0.6969697 alpha=0.8658009
+  3025 ms  OpenGLRenderer: Davey! duration=943ms
+  3027 ms  W25     : CH frame 3 progress=1.0 +1539ms draws=3
+  3028 ms  W25     : CH effect end bursts=1 frames=3 draws=3 +1540ms progress=1.0
+  3681 ms  OpenGLRenderer: Davey! duration=831ms
+  4064 ms  OpenGLRenderer: Davey! duration=786ms
FRAMES want-hook: confetti-like new saturated points outside the band, per frame (level at the end 120; peak #40 = 235; frames with >=6: 27, first #30, last #56): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 21, 21, 21, 21, 21, 21, 21, 230, 233, 235, 234, 157, 156, 152, 152, 159, 117, 118, 118, 117, 119, 119, 120, 121, 120, 120] vertical span of those points: 32-33% 32-33% 32-33% 32-33% 32-33% 32-33% 32-33% 32-33% 26-46% 26-46% 26-46% 26-46% 
CONTROL hook: want-to-read after: checked=true selected=false bounds=[426,738][922,864]
## CF2. Quiz pass 1 (hook): the first options, the right answers are learned
QUIZ learn q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q3: WRONG	Катрин Уеб; content-desc="2 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q4: WRONG	Даниъл Дж. Бурстин; content-desc="2 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q5: WRONG	1922; content-desc="2 верни от 5 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ answers learned: Решение за собствена сграда|Първият радиоапарат в селото|Катрин Уеб|Даниъл Дж. Бурстин|1922|
## CF3. Quiz 5/5 on the hook build, no recording (pure timing from the log)
QUIZ hk-none q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q5: answer at [540 975]; band label bounds [324 1871 756 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
W25 log after the 5th answer (hk-none; times from the marker just before 'input tap'):
+    60 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1203 ms  OpenGLRenderer: Davey! duration=792ms
+  1207 ms  W25     : QB after stitch i=4 +1146ms
+  1684 ms  OpenGLRenderer: Davey! duration=708ms
+  1869 ms  W25     : QB after close +1808ms
+  1976 ms  W25     : QB after haptics +1915ms
+  1976 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +1916ms
+  1977 ms  W25     : QB effect end size=5 +1916ms active=true
+  1979 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  2073 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  2186 ms  W25     : CH frame 1 progress=0.18181817 +206ms draws=1
+  2195 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.18181817 alpha=1.0
+  2356 ms  OpenGLRenderer: Davey! duration=753ms
+  2359 ms  W25     : CH frame 2 progress=0.19696465 +379ms draws=2
+  2359 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.19696465 alpha=1.0
+  2596 ms  W25     : CH frame 3 progress=0.3333283 +616ms draws=3
+  2597 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.3333283 alpha=1.0
+  3006 ms  W25     : CH frame 4 progress=0.56060106 +1026ms draws=4
+  3006 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.56060106 alpha=1.0
+  3272 ms  W25     : CH frame 5 progress=0.9242373 +1292ms draws=5
+  3273 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.9242373 alpha=0.21646483
+  3537 ms  W25     : CH frame 6 progress=1.0 +1557ms draws=6
+  3537 ms  W25     : CH effect end bursts=1 frames=6 draws=6 +1558ms progress=1.0
+  3626 ms  OpenGLRenderer: Davey! duration=713ms
QUIZ hk-none q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-none result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF4. Quiz 5/5 on the hook build, recorded from the host (adb emu screenrecord)
QUIZ hk-emu q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q5: answer at [540 975]; band label bounds [324 1871 756 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-hk-emu (emu): marker 1555 ms after the recording start; 85 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-emu; times from the marker just before 'input tap'):
+   112 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1412 ms  Choreographer: Skipped 34 frames!  The application may be doing too much work on its main thread.
+  1681 ms  OpenGLRenderer: Davey! duration=859ms
+  1922 ms  W25     : QB after stitch i=4 +1810ms
+  1923 ms  Choreographer: Skipped 30 frames!  The application may be doing too much work on its main thread.
+  2220 ms  OpenGLRenderer: Davey! duration=1047ms
+  2746 ms  OpenGLRenderer: Davey! duration=1177ms
+  2907 ms  W25     : QB after close +2795ms
+  2914 ms  W25     : QB after haptics +2802ms
+  2917 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +2805ms
+  2918 ms  W25     : QB effect end size=5 +2806ms active=true
+  2920 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  2935 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  3229 ms  OpenGLRenderer: Davey! duration=1091ms
+  3466 ms  Choreographer: Skipped 31 frames!  The application may be doing too much work on its main thread.
+  3466 ms  W25     : CH frame 1 progress=0.48484847 +547ms draws=1
+  3484 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.48484847 alpha=1.0
+  3736 ms  OpenGLRenderer: Davey! duration=1077ms
+  3970 ms  W25     : CH frame 2 progress=0.500005 +1050ms draws=2
+  3971 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.500005 alpha=1.0
+  4435 ms  W25     : CH frame 3 progress=0.93939894 +1516ms draws=3
+  4436 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.93939894 alpha=0.17314588
+  4927 ms  W25     : CH frame 4 progress=1.0 +2007ms draws=4
+  4928 ms  W25     : CH effect end bursts=1 frames=4 draws=4 +2008ms progress=1.0
+  5199 ms  OpenGLRenderer: Davey! duration=1036ms
+  5695 ms  OpenGLRenderer: Davey! duration=975ms
+  5960 ms  OpenGLRenderer: Davey! duration=1025ms
FRAMES q5-hk-emu: confetti-like new saturated points outside the band, per frame (level at the end 4685; peak #12 = 4688; frames with >=6: 74, first #12, last #85): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4688, 4688, 4688, 4687, 4687, 4687, 4687, 4686, 4686, 4685, 4685, 4685, 4685, 4685, 4684, 4684, 4685, 4685, 4685, 4685, 4688, 4687, 4687, 4685, 4685, 4685, 4685, 4685, 4682, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4684, 4683, 4682, 4683, 4682, 4683, 4680, 4685, 4685, 4684, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685] vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ hk-emu q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-emu result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF5. Quiz 5/5 on the hook build, recorded in the guest (screenrecord 540x1200 — loads the software GPU)
QUIZ hk-adb q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q5: answer at [540 975]; band label bounds [324 1871 756 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-hk-adb (adb): marker 2026 ms after the recording start; 49 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-adb; times from the marker just before 'input tap'):
+    48 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1167 ms  OpenGLRenderer: Davey! duration=809ms
+  1282 ms  W25     : QB after stitch i=4 +1234ms
+  1914 ms  OpenGLRenderer: Davey! duration=743ms
+  1994 ms  W25     : QB after close +1945ms
+  1997 ms  W25     : QB after haptics +1948ms
+  1998 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +1949ms
+  1998 ms  W25     : QB effect end size=5 +1949ms active=true
+  2000 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  2002 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  2131 ms  W25     : CH frame 1 progress=0.1060606 +130ms draws=1
+  2134 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.1060606 alpha=1.0
+  2574 ms  W25     : CH frame 2 progress=0.12121212 +573ms draws=2
+  2574 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.12121212 alpha=1.0
+  2606 ms  W25     : CH frame 3 progress=0.5151515 +606ms draws=3
+  2607 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.5151515 alpha=1.0
+  3027 ms  W25     : CH frame 4 progress=0.54545456 +1027ms draws=4
+  3028 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.54545456 alpha=1.0
+  3341 ms  OpenGLRenderer: Davey! duration=897ms
+  3418 ms  W25     : CH frame 5 progress=0.9242424 +1418ms draws=5
+  3419 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.9242424 alpha=0.21645035
+  3595 ms  W25     : CH frame 6 progress=1.0 +1595ms draws=6
+  3595 ms  W25     : CH effect end bursts=1 frames=6 draws=6 +1595ms progress=1.0
+  4107 ms  OpenGLRenderer: Davey! duration=765ms
FRAMES q5-hk-adb: confetti-like new saturated points outside the band, per frame (level at the end 16; peak #43 = 65; frames with >=6: 35, first #15, last #49): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 8, 6, 6, 6, 6, 6, 8, 12, 12, 12, 12, 19, 19, 19, 19, 19, 19, 19, 19, 19, 18, 63, 63, 63, 63, 63, 65, 15, 15, 15, 16, 16, 16] vertical span of those points: 63-65% 63-65% 63-65% 63-65% 63-65% 63-65% 63-65% 63-65% 63-65% 63-65% 63-65% 63-65% 
QUIZ hk-adb q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-adb result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF6. Quiz 5/5 on the hook build, „Виж резултата“ NOT tapped for 12 s, host recording (second sample)
QUIZ hk-long q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q5: answer at [540 975]; band label bounds [324 1871 756 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-hk-long (emu): marker 1549 ms after the recording start; 125 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-long; times from the marker just before 'input tap'):
+   105 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+   828 ms  Choreographer: Skipped 31 frames!  The application may be doing too much work on its main thread.
+  1536 ms  OpenGLRenderer: Davey! duration=747ms
+  1838 ms  W25     : QB after stitch i=4 +1733ms
+  1839 ms  Choreographer: Skipped 30 frames!  The application may be doing too much work on its main thread.
+  2194 ms  OpenGLRenderer: Davey! duration=1083ms
+  2514 ms  OpenGLRenderer: Davey! duration=953ms
+  2706 ms  W25     : QB after close +2602ms
+  2711 ms  W25     : QB after haptics +2606ms
+  2711 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +2607ms
+  2712 ms  W25     : QB effect end size=5 +2607ms active=true
+  2717 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  2737 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  2964 ms  OpenGLRenderer: Davey! duration=1072ms
+  2980 ms  W25     : CH frame 1 progress=0.2121212 +262ms draws=1
+  2998 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.2121212 alpha=1.0
+  3381 ms  OpenGLRenderer: Davey! duration=905ms
+  3729 ms  W25     : CH frame 2 progress=0.24242423 +1011ms draws=2
+  3732 ms  Choreographer: Skipped 43 frames!  The application may be doing too much work on its main thread.
+  3732 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.24242423 alpha=1.0
+  3977 ms  OpenGLRenderer: Davey! duration=796ms
+  4195 ms  W25     : CH frame 3 progress=0.9090909 +1478ms draws=3
+  4199 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.9090909 alpha=0.25974035
+  4744 ms  W25     : CH frame 4 progress=1.0 +2026ms draws=4
+  4744 ms  W25     : CH effect end bursts=1 frames=4 draws=4 +2026ms progress=1.0
+  4747 ms  Choreographer: Skipped 32 frames!  The application may be doing too much work on its main thread.
+  5048 ms  OpenGLRenderer: Davey! duration=1299ms
+  5350 ms  OpenGLRenderer: Davey! duration=858ms
+  5649 ms  OpenGLRenderer: Davey! duration=996ms
FRAMES q5-hk-long: confetti-like new saturated points outside the band, per frame (level at the end 4685; peak #11 = 4687; frames with >=6: 115, first #11, last #125): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4687, 4685, 4685, 4685, 4684, 4685, 4683, 4685, 4684, 4687, 4683, 4683, 4684, 4685, 4685, 4685, 4687, 4685, 4683, 4685, 4687, 4687, 4684, 4687, 4684, 4685, 4685, 4683, 4684, 4686, 4684, 4685, 4685, 4682, 4684, 4685, 4684, 4685, 4684, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4682, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4682, 4685, 4685 vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ hk-long q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-long result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
CF hook: FATAL(any process)=0 of the app=0 ANR=0
## CF7. The released 1.2.38 itself (no log lines): control and quiz 5/5, host recording
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CONTROL new: want-to-read before: checked=false selected=false bounds=[426,738][922,864]
REC want-new (emu): marker 1553 ms after the recording start; 55 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the control tap (new):
+  5411 ms  OpenGLRenderer: Davey! duration=736ms
FRAMES want-new: confetti-like new saturated points outside the band, per frame (level at the end 118; peak #23 = 271; frames with >=6: 45, first #11, last #55): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 25, 25, 24, 28, 28, 28, 28, 261, 261, 261, 265, 271, 120, 120, 117, 117, 117, 118, 122, 118, 118, 117, 118, 117, 118, 118, 117, 117, 118, 118, 118, 117, 117, 118, 118, 118, 118, 117, 117, 118, 118, 118, 119, 121] vertical span of those points: 32-33% 32-33% 32-33% 32-33% 32-33% 32-33% 32-33% 32-33% 24-35% 23-35% 23-35% 23-35% 
CONTROL new: want-to-read after: checked=true selected=false bounds=[426,738][922,864]
QUIZ new-e1 q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q5: answer at [540 975]; band label bounds [324 1871 756 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-new-e1 (emu): marker 1550 ms after the recording start; 96 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (new-e1; times from the marker just before 'input tap'):
+  1595 ms  OpenGLRenderer: Davey! duration=834ms
+  2051 ms  OpenGLRenderer: Davey! duration=977ms
+  2262 ms  Choreographer: Skipped 30 frames!  The application may be doing too much work on its main thread.
+  2610 ms  OpenGLRenderer: Davey! duration=1099ms
+  3008 ms  OpenGLRenderer: Davey! duration=966ms
+  3575 ms  OpenGLRenderer: Davey! duration=1196ms
+  3781 ms  Choreographer: Skipped 30 frames!  The application may be doing too much work on its main thread.
+  4664 ms  Choreographer: Skipped 35 frames!  The application may be doing too much work on its main thread.
+  4933 ms  OpenGLRenderer: Davey! duration=1177ms
+  5490 ms  OpenGLRenderer: Davey! duration=857ms
+  5942 ms  OpenGLRenderer: Davey! duration=1257ms
FRAMES q5-new-e1: confetti-like new saturated points outside the band, per frame (level at the end 4685; peak #34 = 4687; frames with >=6: 86, first #11, last #96): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4682, 4682, 4682, 4685, 4680, 4680, 4681, 4681, 4681, 4682, 4682, 4686, 4685, 4685, 4684, 4684, 4684, 4685, 4684, 4684, 4685, 4683, 4686, 4687, 4687, 4686, 4686, 4686, 4687, 4687, 4682, 4687, 4684, 4685, 4684, 4685, 4685, 4685, 4684, 4685, 4685, 4682, 4685, 4685, 4685, 4685, 4684, 4685, 4686, 4685, 4685, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4684, 4685, 4685, 4685, 4685, 4685] vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ new-e1 q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ new-e1 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ new-e2 q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q5: answer at [540 975]; band label bounds [324 1871 756 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-new-e2 (emu): marker 1554 ms after the recording start; 95 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (new-e2; times from the marker just before 'input tap'):
+   758 ms  Choreographer: Skipped 36 frames!  The application may be doing too much work on its main thread.
+  1043 ms  OpenGLRenderer: Davey! duration=701ms
+  1735 ms  Choreographer: Skipped 30 frames!  The application may be doing too much work on its main thread.
+  1998 ms  OpenGLRenderer: Davey! duration=1213ms
+  2490 ms  OpenGLRenderer: Davey! duration=993ms
+  3004 ms  OpenGLRenderer: Davey! duration=1107ms
+  3272 ms  Choreographer: Skipped 34 frames!  The application may be doing too much work on its main thread.
+  3585 ms  OpenGLRenderer: Davey! duration=1075ms
+  4304 ms  Choreographer: Skipped 31 frames!  The application may be doing too much work on its main thread.
+  4820 ms  Choreographer: Skipped 30 frames!  The application may be doing too much work on its main thread.
+  5131 ms  OpenGLRenderer: Davey! duration=1060ms
+  5375 ms  Choreographer: Skipped 32 frames!  The application may be doing too much work on its main thread.
+  5672 ms  OpenGLRenderer: Davey! duration=1127ms
+  6136 ms  OpenGLRenderer: Davey! duration=1089ms
FRAMES q5-new-e2: confetti-like new saturated points outside the band, per frame (level at the end 4685; peak #56 = 4709; frames with >=6: 86, first #10, last #95): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4686, 4684, 4685, 4685, 4683, 4688, 4688, 4688, 4685, 4686, 4686, 4687, 4687, 4688, 4687, 4687, 4681, 4681, 4681, 4685, 4689, 4686, 4684, 4685, 4684, 4684, 4686, 4686, 4685, 4685, 4684, 4682, 4683, 4684, 4685, 4685, 4684, 4685, 4684, 4685, 4697, 4697, 4697, 4699, 4698, 4699, 4709, 4707, 4707, 4707, 4709, 4707, 4685, 4685, 4684, 4685, 4685, 4684, 4682, 4685, 4685, 4685, 4685, 4682, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4685, 4682, 4685, 4685, 4685, 4685, 4685, 4685, 4685] vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ new-e2 q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ new-e2 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
CF part: FATAL(any process)=1 of the app=0 ANR=0; Davey lines of the apps: 213
contact sheets: 45 in sheets/
contact sheets: 45 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['12138', '12515', '13322']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 25
   skipped-frames events=25 max=191 total=1386
== Davey: 52
   davey events=52 max=4431ms median=1064ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
```

## cf36x

```
part cf36x; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15700 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
animations: animator_duration_scale=1 transition=1 window=1
## CF0. The hook build (same code as 1.2.38 + Log.w lines), first launch
X installed hook: 1.2.38 1dd4ccb prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.hk, throwaway key, versionCode 1990101;  versionCode=1990101 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X hook: catalogue after 4130 ms
anim after the first launch: animator_duration_scale=1 transition=1 window=1
## CF1. Control: confetti on „Искам да прочета“ (host recording), hook build
CONTROL hook: want-to-read before: checked=false selected=false bounds=[426,738][921,864]
REC want-hook (emu): marker 1548 ms after the recording start; 55 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the control tap (hook):
+   294 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(702.0, 801.0) lastTap=Offset(702.0, 801.0)
+   301 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(702.0, 801.0) progress=0.0 alpha=1.0
+   729 ms  W25     : CH frame 1 progress=0.30303028 +435ms draws=1
+   731 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(702.0, 801.0) progress=0.30303028 alpha=1.0
+   999 ms  W25     : CH frame 2 progress=0.39393938 +704ms draws=2
+  1000 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(702.0, 801.0) progress=0.39393938 alpha=1.0
+  1192 ms  W25     : CH frame 3 progress=0.6363636 +898ms draws=3
+  1197 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(702.0, 801.0) progress=0.6363636 alpha=1.0
+  1510 ms  W25     : CH frame 4 progress=0.81818175 +1215ms draws=4
+  1510 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(702.0, 801.0) progress=0.81818175 alpha=0.5194807
+  1663 ms  W25     : CH frame 5 progress=1.0 +1369ms draws=5
+  1663 ms  W25     : CH effect end bursts=1 frames=5 draws=5 +1369ms progress=1.0
FRAMES want-hook: confetti-like new saturated points outside the band, per frame (level at the end 122; peak #23 = 255; frames with >=6: 42, first #14, last #55): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 31, 30, 28, 28, 231, 231, 231, 248, 255, 237, 237, 240, 121, 123, 123, 123, 121, 123, 121, 123, 123, 123, 123, 123, 123, 123, 123, 123, 123, 122, 122, 122, 122, 121, 121, 122, 122, 122, 122, 122, 122] vertical span of those points: 32-33% 32-33% 32-33% 32-33% 32-33% 22-37% 22-37% 22-37% 23-40% 23-40% 30-53% 30-53% 
CONTROL hook: want-to-read after: checked=true selected=false bounds=[426,738][921,864]
## CF2. Quiz pass 1 (hook): the first options, the right answers are learned
QUIZ learn q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q3: WRONG	Катрин Уеб; content-desc="2 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q4: WRONG	Даниъл Дж. Бурстин; content-desc="2 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn q5: WRONG	1922; content-desc="2 верни от 5 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ learn result: text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ answers learned: Решение за собствена сграда|Първият радиоапарат в селото|Катрин Уеб|Даниъл Дж. Бурстин|1922|
## CF3. Quiz 5/5 on the hook build, no recording (pure timing from the log)
QUIZ hk-none q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-none q5: answer at [540 975]; band label bounds [325 1871 755 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
W25 log after the 5th answer (hk-none; times from the marker just before 'input tap'):
+    65 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1330 ms  W25     : QB after stitch i=4 +1265ms
+  1831 ms  W25     : QB after close +1766ms
+  1837 ms  W25     : QB after haptics +1772ms
+  1838 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +1772ms
+  1838 ms  W25     : QB effect end size=5 +1772ms active=true
+  1840 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  1851 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  2033 ms  W25     : CH frame 1 progress=0.15151514 +193ms draws=1
+  2034 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.15151514 alpha=1.0
+  2296 ms  W25     : CH frame 2 progress=0.16666666 +456ms draws=2
+  2297 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.16666666 alpha=1.0
+  2428 ms  W25     : CH frame 3 progress=0.39393938 +588ms draws=3
+  2429 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.39393938 alpha=1.0
+  2641 ms  W25     : CH frame 4 progress=0.5151515 +801ms draws=4
+  2642 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.5151515 alpha=1.0
+  2833 ms  W25     : CH frame 5 progress=0.7121212 +993ms draws=5
+  2834 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.7121212 alpha=0.8225109
+  3122 ms  W25     : CH frame 6 progress=0.8939394 +1282ms draws=6
+  3123 ms  W25     : CL draw #7 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.8939394 alpha=0.30303037
+  3236 ms  W25     : CH frame 7 progress=1.0 +1395ms draws=7
+  3236 ms  W25     : CH effect end bursts=1 frames=7 draws=7 +1395ms progress=1.0
QUIZ hk-none q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-none result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF4. Quiz 5/5 on the hook build, recorded from the host (adb emu screenrecord)
QUIZ hk-emu q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-emu q5: answer at [540 975]; band label bounds [325 1871 755 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-hk-emu (emu): marker 1551 ms after the recording start; 86 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-emu; times from the marker just before 'input tap'):
+    64 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1458 ms  HWUI    : Davey! duration=872ms
+  1579 ms  W25     : QB after stitch i=4 +1515ms
+  2019 ms  HWUI    : Davey! duration=868ms
+  2309 ms  W25     : QB after close +2245ms
+  2312 ms  W25     : QB after haptics +2248ms
+  2312 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +2248ms
+  2312 ms  W25     : QB effect end size=5 +2248ms active=true
+  2314 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  2337 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  2453 ms  HWUI    : Davey! duration=717ms
+  2678 ms  W25     : CH frame 1 progress=0.3181818 +364ms draws=1
+  2696 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.3181818 alpha=1.0
+  2826 ms  HWUI    : Davey! duration=768ms
+  3018 ms  W25     : CH frame 2 progress=0.3333333 +704ms draws=2
+  3019 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.3333333 alpha=1.0
+  3369 ms  W25     : CH frame 3 progress=0.6212121 +1055ms draws=3
+  3371 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.6212121 alpha=1.0
+  3742 ms  W25     : CH frame 4 progress=0.9393939 +1428ms draws=4
+  3743 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.9393939 alpha=0.17316034
+  3895 ms  HWUI    : Davey! duration=707ms
+  4064 ms  W25     : CH frame 5 progress=1.0 +1749ms draws=5
+  4065 ms  W25     : CH effect end bursts=1 frames=5 draws=5 +1750ms progress=1.0
+  4248 ms  HWUI    : Davey! duration=720ms
+  4606 ms  HWUI    : Davey! duration=796ms
+  4923 ms  HWUI    : Davey! duration=702ms
FRAMES q5-hk-emu: confetti-like new saturated points outside the band, per frame (level at the end 4692; peak #49 = 4730; frames with >=6: 77, first #10, last #86): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4689, 4689, 4690, 4688, 4688, 4688, 4687, 4690, 4690, 4690, 4687, 4687, 4689, 4687, 4690, 4691, 4689, 4689, 4689, 4692, 4688, 4690, 4690, 4692, 4688, 4688, 4690, 4688, 4687, 4687, 4692, 4693, 4693, 4692, 4692, 4692, 4692, 4692, 4729, 4730, 4730, 4730, 4691, 4691, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692] vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ hk-emu q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-emu result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF5. Quiz 5/5 on the hook build, recorded in the guest (screenrecord 540x1200 — loads the software GPU)
QUIZ hk-adb q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-adb q5: answer at [540 975]; band label bounds [325 1871 755 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-hk-adb (adb): marker 2024 ms after the recording start; 46 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-adb; times from the marker just before 'input tap'):
+    45 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1290 ms  W25     : QB after stitch i=4 +1245ms
+  1799 ms  W25     : QB after close +1754ms
+  1821 ms  W25     : QB after haptics +1775ms
+  1821 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +1776ms
+  1821 ms  W25     : QB effect end size=5 +1776ms active=true
+  2083 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  2085 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  2232 ms  W25     : CH frame 1 progress=0.13636364 +149ms draws=1
+  2236 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.13636364 alpha=1.0
+  2534 ms  W25     : CH frame 2 progress=0.15151514 +450ms draws=2
+  2535 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.15151514 alpha=1.0
+  2605 ms  W25     : CH frame 3 progress=0.40909088 +521ms draws=3
+  2605 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.40909088 alpha=1.0
+  3004 ms  W25     : CH frame 4 progress=0.48484847 +921ms draws=4
+  3005 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.48484847 alpha=1.0
+  3030 ms  W25     : CH frame 5 progress=0.8484848 +946ms draws=5
+  3030 ms  W25     : CL draw #6 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.8484848 alpha=0.43290055
+  3379 ms  W25     : CH frame 6 progress=0.8636363 +1296ms draws=6
+  3380 ms  W25     : CL draw #7 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.8636363 alpha=0.38961053
+  3424 ms  HWUI    : Davey! duration=707ms
+  3519 ms  W25     : CH frame 7 progress=1.0 +1435ms draws=7
+  3519 ms  W25     : CH effect end bursts=1 frames=7 draws=7 +1435ms progress=1.0
FRAMES q5-hk-adb: confetti-like new saturated points outside the band, per frame (level at the end 14; peak #37 = 34; frames with >=6: 35, first #12, last #46): [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 8, 16, 16, 16, 16, 15, 15, 15, 15, 15, 15, 15, 15, 15, 12, 12, 12, 34, 29, 29, 29, 29, 14, 14, 14, 14, 14] vertical span of those points: 64-65% 64-65% 64-65% 64-65% 63-65% 64-65% 64-65% 64-65% 63-65% 63-65% 63-65% 63-65% 
QUIZ hk-adb q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-adb result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
## CF6. Quiz 5/5 on the hook build, „Виж резултата“ NOT tapped for 12 s, host recording (second sample)
QUIZ hk-long q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ hk-long q5: answer at [540 975]; band label bounds [325 1871 755 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-hk-long (emu): marker 1548 ms after the recording start; 126 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (hk-long; times from the marker just before 'input tap'):
+    86 ms  W25     : QB effect start size=5 total=5 seen=4 reduced=false closed=true confetti=true
+  1361 ms  W25     : QB after stitch i=4 +1276ms
+  1443 ms  HWUI    : Davey! duration=854ms
+  1923 ms  HWUI    : Davey! duration=879ms
+  2086 ms  W25     : QB after close +2000ms
+  2091 ms  W25     : QB after haptics +2006ms
+  2092 ms  W25     : QB burst called=true origin=Offset(540.5, 2022.5) bounds=Rect.fromLTRB(68.0, 1919.0, 1013.0, 2126.0) bursts=1 +2006ms
+  2092 ms  W25     : QB effect end size=5 +2006ms active=true
+  2093 ms  W25     : CH effect bursts=1 reduced=false isPlaying=false host=1080 x 2400 origin=Offset(540.5, 2022.5) lastTap=Offset(540.0, 975.0)
+  2096 ms  W25     : CL draw #1 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.0 alpha=1.0
+  2280 ms  HWUI    : Davey! duration=750ms
+  2457 ms  W25     : CH frame 1 progress=0.3181818 +364ms draws=1
+  2478 ms  W25     : CL draw #2 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.3181818 alpha=1.0
+  2641 ms  HWUI    : Davey! duration=812ms
+  2803 ms  W25     : CH frame 2 progress=0.34848484 +710ms draws=2
+  2804 ms  W25     : CL draw #3 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.34848484 alpha=1.0
+  3184 ms  W25     : CH frame 3 progress=0.6363636 +1090ms draws=3
+  3185 ms  W25     : CL draw #4 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.6363636 alpha=1.0
+  3494 ms  W25     : CH frame 4 progress=0.98484844 +1400ms draws=4
+  3495 ms  W25     : CL draw #5 size=Size(1080.0, 2400.0) at=Offset(540.5, 2022.5) progress=0.98484844 alpha=0.04329017
+  3943 ms  W25     : CH frame 5 progress=1.0 +1849ms draws=5
+  3944 ms  W25     : CH effect end bursts=1 frames=5 draws=5 +1850ms progress=1.0
+  4034 ms  HWUI    : Davey! duration=886ms
+  4479 ms  HWUI    : Davey! duration=729ms
+  4735 ms  HWUI    : Davey! duration=811ms
FRAMES q5-hk-long: confetti-like new saturated points outside the band, per frame (level at the end 4692; peak #46 = 4731; frames with >=6: 117, first #10, last #126): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4689, 4690, 4689, 4685, 4686, 4686, 4687, 4688, 4686, 4690, 4688, 4687, 4688, 4690, 4691, 4688, 4687, 4690, 4690, 4690, 4690, 4694, 4693, 4693, 4692, 4691, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4691, 4692, 4731, 4731, 4730, 4731, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4 vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ hk-long q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ hk-long result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
CF hook: FATAL(any process)=0 of the app=0 ANR=0
## CF7. The released 1.2.38 itself (no log lines): control and quiz 5/5, host recording
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
CONTROL new: want-to-read before: checked=false selected=false bounds=[426,738][921,864]
REC want-new (emu): marker 1551 ms after the recording start; 55 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the control tap (new):
(no W25 lines after the marker want-new)
FRAMES want-new: confetti-like new saturated points outside the band, per frame (level at the end 122; peak #16 = 288; frames with >=6: 46, first #10, last #55): [0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 28, 28, 29, 197, 200, 288, 287, 268, 268, 275, 270, 200, 198, 126, 123, 123, 123, 120, 120, 121, 121, 121, 119, 120, 120, 120, 121, 122, 123, 123, 123, 123, 123, 123, 123, 123, 123, 122, 122, 122, 119, 122, 122, 122, 124] vertical span of those points: 32-33% 32-33% 32-33% 32-33% 31-34% 31-34% 24-34% 24-34% 23-37% 23-37% 27-48% 27-48% 
CONTROL new: want-to-read after: checked=true selected=false bounds=[426,738][921,864]
QUIZ new-e1 q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e1 q5: answer at [540 975]; band label bounds [325 1871 755 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-new-e1 (emu): marker 1541 ms after the recording start; 95 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (new-e1; times from the marker just before 'input tap'):
+  1480 ms  HWUI    : Davey! duration=892ms
+  2185 ms  HWUI    : Davey! duration=949ms
+  2625 ms  HWUI    : Davey! duration=743ms
+  2993 ms  HWUI    : Davey! duration=920ms
+  3993 ms  HWUI    : Davey! duration=721ms
+  4753 ms  HWUI    : Davey! duration=897ms
+  5112 ms  HWUI    : Davey! duration=734ms
FRAMES q5-new-e1: confetti-like new saturated points outside the band, per frame (level at the end 4692; peak #50 = 4695; frames with >=6: 86, first #10, last #95): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4686, 4686, 4688, 4688, 4688, 4694, 4691, 4691, 4691, 4694, 4687, 4690, 4690, 4690, 4689, 4690, 4688, 4689, 4694, 4694, 4691, 4692, 4688, 4687, 4687, 4691, 4689, 4692, 4692, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4692, 4692, 4695, 4695, 4695, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692] vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ new-e1 q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ new-e1 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
QUIZ new-e2 q1: RIGHT; content-desc="1 верни от 1 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q2: RIGHT; content-desc="2 верни от 2 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q3: RIGHT; content-desc="3 верни от 3 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q4: RIGHT; content-desc="4 верни от 4 отговорени, общо 5 въпроса" text="Избродирай отговорите" 
QUIZ new-e2 q5: answer at [540 975]; band label bounds [325 1871 755 1908]; next button y 2226; animator_duration_scale=1 transition=1 window=1
REC q5-new-e2 (emu): marker 1551 ms after the recording start; 96 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
W25 log after the 5th answer (new-e2; times from the marker just before 'input tap'):
+  1462 ms  HWUI    : Davey! duration=790ms
+  1764 ms  HWUI    : Davey! duration=745ms
+  2039 ms  HWUI    : Davey! duration=727ms
+  2489 ms  HWUI    : Davey! duration=814ms
+  2884 ms  HWUI    : Davey! duration=746ms
+  3964 ms  HWUI    : Davey! duration=795ms
+  4265 ms  HWUI    : Davey! duration=718ms
+  4611 ms  HWUI    : Davey! duration=818ms
+  5127 ms  HWUI    : Davey! duration=901ms
FRAMES q5-new-e2: confetti-like new saturated points outside the band, per frame (level at the end 4692; peak #48 = 4705; frames with >=6: 87, first #10, last #96): [0, 0, 0, 0, 0, 0, 0, 0, 0, 4686, 4686, 4690, 4691, 4691, 4691, 4688, 4688, 4690, 4690, 4690, 4691, 4690, 4687, 4691, 4691, 4691, 4686, 4690, 4689, 4692, 4691, 4691, 4693, 4692, 4692, 4690, 4691, 4691, 4691, 4692, 4693, 4692, 4692, 4691, 4691, 4691, 4691, 4705, 4705, 4703, 4703, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692, 4692, 4692, 4691, 4692, 4692] vertical span of those points: 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 40-95% 
QUIZ new-e2 q5: RIGHT; content-desc="5 верни от 5 отговорени, общо 5 въпроса" text="Шевицата е цяла!" 
QUIZ new-e2 result: text="Отлично! Познаваш Яворец!" text="5/5" text="5 верни от 5" text="Шевицата е цяла!" text="Серия: 1 ден поред" text="Нови въпроси — утре." text="Сподели резултата" text="Затвори" content-desc="Назад" text="Познай Яворец" 
CF part: FATAL(any process)=0 of the app=0 ANR=0; Davey lines of the apps: 135
contact sheets: 46 in sheets/
contact sheets: 46 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['9451', '9803', '10526']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 5
   skipped-frames events=5 max=91 total=323
== Davey: 32
   davey events=32 max=1781ms median=864ms
== NativeLink: 3
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'ANR': 1} → crashes.txt
```

## dev33

```
part dev33; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
installed: devDebug 1.2.38 1dd4ccb (org.chyavorec.app.dev, demo sign-in DEMO-0001, shared debug key; demo-1 renews at once);  versionCode=1 minSdk=26 targetSdk=37  versionName=1.2.38-dev 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4355 ms
## D1. Sign-in with the BUTTON; „Картата е издадена“ recorded from the host (10 frames/s)
LOGIN try 1: card field OK
LOGIN: remember-me CHECKSTATE 'Запомни ме': checked=true (node itself/ancestor, clickable=true); the sign-in BUTTON: 540 1776 enabled=true class=android.view.View bounds=(63, 1708, 1017, 1845); all 'Вход' nodes: class="android.widget.TextView" text="Вход" content-desc="" checkable="false" checked="false" clickable="false" selected="false" bounds="[494,1750][586,1803]"|class="android.widget.TextView" text="Вход" content-desc="" checkable="false" checked="false" clickable="false" selected="false" bounds="[148,177][261,247]"|
REC login-fly (emu): marker 1531 ms after the recording start; 66 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
MY after the sign-in: SIGNED IN: text="ДЕМО ДАННИ — само за разработка, не са реални" text="ДЧ" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" text="БИБЛИОТЕКА" text="Читателска карта" text="Покажи картата на гишето" text="Моите книги" text="1 книга е просрочена" text="Искам да прочета" text="Книги, които искаш да
FRAMES login-fly: frames 66; frames that differ from the previous one (>0.35): 6, first #9, last #43; diffs: 0.1 0.3 0.3 0.1 0.2 0.0 0.1 6.5 0.1 0.1 0.1 0.2 0.1 0.1 0.2 0.2 0.1 0.2 0.1 0.0 0.1 0.0 0.0 0.1 0.1 0.1 0.1 0.1 0.1 0.6 0.1 0.1 0.0 0.0 11.6 0.0 27.0 0.0 0.0 13.0 0.0 0.7 0.0 0.3 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
## D2. Loans: „Удължи срока“ on demo-1 (debug branch: accepted at once, new due date) → „Нов лист“, host recording
LOANS: text="ДЕМО ДАННИ — само за разработка, не са реални" text="Последна синхронизация: Преди 0 минути" text="Демо заглавие (просрочено)" text="Демо автор" text="Взета на 06.10.2026" text="Срокът изтече на 12.11.2026" text="просрочена с 3 дни" text="Удължавана 2 пъти" text="Удължаването е отказано: Книгата е заявена от друг читател." text="Демо заглавие (наближава срок)" text="Демо автор" text="Взета на 18.10.2026" text="Връщане: 17.11.2026" text="остават 2 дни" text="Удължавана 1 път" text="Удължи с
REC renew-leaf (emu): marker 1543 ms after the recording start; 55 frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)
RENEW: leaf node absent; Удължаването е отказано: Книгата е заявена от друг читател.|Заявено удължаване · чака библиотеката|
FRAMES renew-leaf: frames 55; frames that differ from the previous one (>0.35): 6, first #8, last #23; diffs: 0.1 0.1 0.1 0.2 0.0 0.0 2.6 0.0 0.1 0.1 0.1 0.1 0.1 18.7 0.0 0.4 0.0 0.6 0.0 0.7 0.1 0.5 0.0 0.2 0.0 0.1 0.1 0.0 0.0 0.0 0.1 0.1 0.0 0.0 0.0 0.1 0.1 0.0 0.0 0.0 0.1 0.1 0.0 0.1 0.0 0.0 0.0 0.1 0.0 0.0 0.0 0.0 0.0 0.0
RENEW second loan (pending): leaf nodes 1; Заявено удължаване · чака библиотеката|
## D3. Diary: 5 rosettes on the first book, then the year story with no taps (slides change by themselves)
DIARY rated: content-desc="Оценка на книгата" 
YEAR slide #1 at +4 s: 2026 — твоята година с книгите
YEAR slide #2 at +8 s: През 2026 г. взе 30 книги от библиотеката. в 11 различни месеца
YEAR slide #3 at +13 s: Любим автор: Иван Вазов — 6 книги през годината.
YEAR slide #4 at +22 s: Най-харесана книга: Немили-недраги, Иван Вазов — 5 от 5 розетки в дневника.
YEAR: the favourite-book slide is #4
YEAR slide #5 at +27 s: Най-четен раздел: Език. Литература — 2 книги.
YEAR slide #6 at +32 s: Рекорден месец: Септември — 5 книги.
YEAR slide #7 at +36 s: Най-дълга поредица: 11 месеца подред с поне една книга.
YEAR slide #8 at +45 s: Твоят читателски тип: Книжен дракон. Поглъщаш книга след книга — рафтовете едва смогват.
YEAR slide #9 at +50 s: Благодарим ти, че четеш с нас! Библиотеката на читалището те очаква и през 2027 г. Твоята шевица: лента, избродирана от 30 книги през годината.
YEAR walk: 9 distinct slides in 81 s (26 polls, 0 failed dumps)
## D4. Tablet landscape (1280x800 dp), signed in: two panes in Моето (uiautomator, the card pane is FLAG_SECURE)
TABLET Моето (2560 x 1600): signed in
PANES: left 24 texts, right 2 texts; likely pane border x=696 (27% of 2560)
  LEFT: Начало | Каталог | Моето | Новини | Още | ДЕМО ДАННИ — само за разработка, не са р | ДЧ | Демо Читател | Читателски № DEMO-0001 | Активно | БИБЛИОТЕКА | Читателска карта | Покажи картата на гишето | Моите книги | 1 книга е просрочена | Искам да прочета | Книги, които искаш да вземеш | Моето членство | Статус и валидност | Прочетени книги | Историята на четенето и подобни книги | Моят профил
  RIGHT: Търсене | Избери раздел вляво — той ще се отвори т
TABLET Моето › '=Моите книги|=My books' (tapped at 793 797): tabs visible; back arrow yes
PANES: left 24 texts, right 26 texts; likely pane border x=1376 (53% of 2560)
  LEFT: Начало | Каталог | Моето | Новини | Още | ДЕМО ДАННИ — само за разработка, не са р | ДЧ | Демо Читател | Читателски № DEMO-0001 | Активно | БИБЛИОТЕКА | Читателска карта | Покажи картата на гишето | Моите книги | 1 книга е просрочена | Искам да прочета | Книги, които искаш да вземеш | Моето членство | Статус и валидност | Прочетени книги | Историята на четенето и подобни книги | Моят профил
  RIGHT: Търсене | ДЕМО ДАННИ — само за разработка, не са р | Последна синхронизация: Преди 2 минути | Демо заглавие (просрочено) | Демо автор | Взета на 06.10.2026 | Срокът изтече на 12.11.2026 | просрочена с 3 дни | Удължавана 2 пъти | Удължаването е отказано: Книгата е заяве | Демо заглавие (наближава срок) | Демо автор | Взета на 18.10.2026 | Връщане: 17.11.2026 | остават 2 дни | Удължавана 1 път | Заявено удължаване · чака библиотеката | Под игото | Иван Вазов | Взета на 01.11.2026 | Връщане: 06.12.2026 | остават 21 дни
TABLET Моето › '=Читателска карта|=Library card' (tapped at 360 797): tabs visible; back arrow yes
PANES: left 24 texts, right 13 texts; likely pane border x=696 (27% of 2560)
  LEFT: Начало | Каталог | Моето | Новини | Още | ДЕМО ДАННИ — само за разработка, не са р | ДЧ | Демо Читател | Читателски № DEMO-0001 | Активно | БИБЛИОТЕКА | Читателска карта | Покажи картата на гишето | Моите книги | 1 книга е просрочена | Искам да прочета | Книги, които искаш да вземеш | Моето членство | Статус и валидност | Прочетени книги | Историята на четенето и подобни книги | Моят профил
  RIGHT: Търсене | ДЕМО ДАННИ — само за разработка, не са р | ЧИТАТЕЛСКА КАРТА | НЧ „Васил Левски – 1922“ | Демо Читател | Читателски № DEMO-0001 | Активно | Баркод на карта DEMO-0001 | Покажи картата | Последна синхронизация: Преди 3 минути | Покажи картата на библиотекаря; баркодът | Назад | Читателска карта
TABLET Моето › '=Моето членство|=My membership' (tapped at 823 1055): tabs visible; back arrow yes
PANES: left 24 texts, right 14 texts; likely pane border x=696 (27% of 2560)
  LEFT: Начало | Каталог | Моето | Новини | Още | ДЕМО ДАННИ — само за разработка, не са р | ДЧ | Демо Читател | Читателски № DEMO-0001 | Активно | БИБЛИОТЕКА | Читателска карта | Покажи картата на гишето | Моите книги | 1 книга е просрочена | Искам да прочета | Книги, които искаш да вземеш | Моето членство | Статус и валидност | Прочетени книги | Историята на четенето и подобни книги | Моят профил
  RIGHT: Търсене | ДЕМО ДАННИ — само за разработка, не са р | Демо Читател | Активно | Членски номер | DEMO-0001 | Член от | 15 ноември 2024 | Валидно до | 31 декември 2026 | QR код на картата | Последна синхронизация: Преди 3 минути | Назад | Моето членство
TABLET: '=История|=Reading history|История на заемане' not found in Моето: text="Начало" text="Каталог" text="Моето" text="Новини" text="Още" text="Покажи картата на гишето" text="1 книга е просрочена" text="Искам да прочета" text="Книги, които искаш да вземеш" text="Моето членство" text="Статус и валидност" text="Прочетени книги" text="Историята на четенето и подобни книг
TABLET Моето › '=Искам да прочета|=Want to read' (tapped at 351 440): tabs hidden; back arrow yes
PANES: left 2 texts, right 2 texts; likely pane border x=1096 (42% of 2560)
  LEFT: Назад | Искам да прочета
  RIGHT: Списъкът е празен | Отбележи книга с „Искам да прочета“ на с
## D5. The card: 14 November (no band), 15 December and 15 January (the „Твоята шевица“ band)
CARD nov: Моето: signed in
CARD nov (2026-11-14): band absent; text="ДЕМО ДАННИ — само за разработка, не са реални" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" content-desc="Баркод на карта DEMO-0001" text="Покажи картата" tex
CARD full nov: band absent
device date now: Tue Dec 15 10:00:02 UTC 2026
CARD dec: Моето: signed in
CARD dec (2026-12-15): band PRESENT (content-desc="Твоята шевица за 2026"); text="ДЕМО ДАННИ — само за разработка, не са реални" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" content-desc="Твоята шевица за 2026" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" content-desc="Баркод на карта
CARD full dec: band PRESENT
device date now: Fri Jan 15 10:00:02 UTC 2027
CARD jan: Моето: signed in
CARD jan (2027-01-15): band PRESENT (content-desc="Твоята шевица за 2026"); text="ДЕМО ДАННИ — само за разработка, не са реални" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" content-desc="Твоята шевица за 2026" text="Демо Читател" text="Читателски № DEMO-0001" text="Активно" content-desc="Баркод на карта
CARD full jan: band PRESENT
device date now: Sat Nov 14 22:08:36 UTC 2026
DEV part: FATAL(any process)=1 of the app=0 ANR=0
contact sheets: 14 in sheets/
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Версия 1.2.38-dev (1) | Профил: чака | Студен старт · 6 | Първи кадър: медиана 3190 мс, последен 3086 мс | Пълно показване: медиана 3814 мс, последно 3981 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Версия 1.2.38-dev (1)" text="Профил: чака" text="Студен старт · 6" text="Първи кадър: медиана 3190 мс, последен 3086 мс" text="Пълно показване: медиана 3814 мс, последно 3981 мс" text="Няма записани грешки." content-desc="Н
APP W/E lines (pids ['6162', '9193', '9698', '9862', '10255', '10951']): 313 in 125 groups
      15  W/W25: CL draw ## size=Size(#.#, #.#) at=Offset(#.#, #.#) progress=#.# alpha=#.#
      15  W/W25: CH frame # progress=#.# +#ms draws=#
       8  W/ziparchive: Unable to open '/data/app/~~YdktRVIuS#ewPYYRcZSjg==/org.chyavorec.app.dev-bfk_ifYh#tjuRPRrT#tPbw==/base.dm': N
       8  E/OpenGLRenderer: Unable to match the desired swap behavior.
       6  W/yavorec.app.dev: Unexpected CPU variant for x#: x#_#.
       6  W/yavorec.app.dev: Known variants: atom, sandybridge, silvermont, kabylake, default
       6  W/Parcel: Expecting binder but got null!
       6  W/yavorec.app.dev: Accessing hidden method Landroid/os/SystemProperties;->addChangeCallback(Ljava/lang/Runnable;)V (unsupported, 
       6  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       6  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       6  W/Gralloc4: allocator #.x is not supported
       6  W/W25: CH effect bursts=# reduced=false isPlaying=false host=# x # origin=Offset.Unspecified lastTap=Offset.Unspecifi
       6  W/BasicSecureTextField: 	at androidx.compose.runtime.internal.ComposableLambdaImpl.invoke(ComposableLambda.kt:#)
       6  W/BasicSecureTextField: 	at androidx.compose.ui.layout.LayoutNodeSubcompositionsState.subcompose(SubcomposeLayout.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.material#.SecureTextFieldKt$OutlinedSecureTextField$#.invoke(SecureTextField.kt:#)
       4  W/BasicSecureTextField: 	at androidx.compose.runtime.snapshots.SnapshotStateObserver.observeReads(SnapshotStateObserver.kt:#)
       4  W/BasicSecureTextField: 	at android.view.View.draw(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.View.updateDisplayListIfDirty(View.java:#)
       4  W/BasicSecureTextField: 	at android.view.Choreographer$CallbackRecord.run(Choreographer.java:#)
       3  W/yavorec.app.dev: Accessing hidden method Landroid/view/ViewTreeObserver;->dispatchOnScrollChanged()V (unsupported, reflection, 
       2  W/BasicSecureTextField: Failed to fetch show password setting, using value: true
       2  W/BasicSecureTextField: android.provider.Settings$SettingNotFoundException: show_password
       2  W/BasicSecureTextField: 	at android.provider.Settings.parseIntSetting(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings.-$$Nest$smparseIntSetting(Unknown Source:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getIntForUser(Settings.java:#)
       2  W/BasicSecureTextField: 	at android.provider.Settings$System.getInt(Settings.java:#)
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.PlatformPasswordVisibilitySettingImpl.getSystemShowPasswordSetting(BasicS
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextField_androidKt.rememberPlatformPasswordVisibilitySettings
       2  W/BasicSecureTextField: 	at androidx.compose.foundation.text.BasicSecureTextFieldKt.BasicSecureTextField-ltb#GB#(BasicSecureTextField.
contact sheets: 15 in sheets/
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
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
```

## fr33a

```
part fr33a; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## F0. 1.2.38 (org.chyavorec.app) and 1.2.37 (org.chyavorec.app.b37) side by side, both compiled speed-profile
X installed new: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 5274 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~ZRsXCx4kbfgzVgvdJOcLtg==/org.chyavorec.app-qMGHDxzGDeIkREDyAElkhw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000;  versionCode=1990000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 5582 ms
X compile org.chyavorec.app.b37: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~cvfv3wNCMP7BhxGEuA1vKg==/org.chyavorec.app.b37-qCyDED0XMpcDi8O0tnLS3w==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
animations: animator_duration_scale=1 transition=1 window=1; host load: 6.66 4.05 1.75
## F1. Warm-up (rep 0, not counted): every scenario once; the quiz taps are learned
  FS home new rep0: n=49 (+0 flagged) total p50=593 p90=797 p99=921 max=924 ms | medians: ui=0.7 (input 0.0 anim 0.5 layout 0.0 draw 0.2) rt=256.7 (sync 0.1 issue 67.0 swap 167.6) start-delay=111.0 queue=158.2
  FS more new rep0: n=67 (+0 flagged) total p50=512 p90=683 p99=749 max=810 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=204.6 (sync 0.1 issue 70.6 swap 126.8) start-delay=146.9 queue=151.9
  FS timeline new rep0: n=68 (+0 flagged) total p50=549 p90=781 p99=866 max=929 ms | medians: ui=0.6 (input 0.0 anim 0.4 layout 0.0 draw 0.1) rt=226.8 (sync 0.3 issue 83.0 swap 126.1) start-delay=147.3 queue=157.0
QUIZ taps new: 5 questions learned: input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
  FS home base rep0: n=59 (+0 flagged) total p50=592 p90=734 p99=766 max=768 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=222.6 (sync 0.1 issue 67.2 swap 143.9) start-delay=188.0 queue=203.2
  FS more base rep0: n=61 (+2 flagged) total p50=521 p90=717 p99=921 max=1049 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=204.2 (sync 0.1 issue 67.5 swap 123.6) start-delay=141.8 queue=153.2
  FS timeline base rep0: n=71 (+0 flagged) total p50=462 p90=597 p99=653 max=659 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=178.8 (sync 0.1 issue 66.0 swap 108.8) start-delay=116.6 queue=121.4
QUIZ taps base: 5 questions learned: input tap 540 780; sleep 2; input tap 540 1863; sleep 2.2~input tap 540 780; sleep 2; input tap 540 1863; sleep 2.2~input tap 540 706; sleep 2; input tap 540 1612; sleep 2.2~input tap 540 706; sleep 2; input tap 540 1612; sleep 2.2~input tap 540 780; sleep 2; input tap 540 1919; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Серия: 1 ден поред" 
## F2. Repetition 1, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep1: n=64 (+0 flagged) total p50=527 p90=756 p99=851 max=866 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=201.1 (sync 0.1 issue 74.1 swap 127.2) start-delay=148.8 queue=147.2
WARN: tab bar not reached
  FS more new rep1: n=65 (+0 flagged) total p50=469 p90=578 p99=681 max=704 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=178.5 (sync 0.1 issue 65.6 swap 108.8) start-delay=117.8 queue=138.0
  FS timeline new rep1: n=76 (+0 flagged) total p50=459 p90=669 p99=834 max=837 ms | medians: ui=0.5 (input 0.0 anim 0.4 layout 0.0 draw 0.1) rt=196.9 (sync 0.1 issue 68.7 swap 90.6) start-delay=124.5 queue=138.1
  FS quiz new rep1: n=86 (+1 flagged) total p50=649 p90=767 p99=857 max=899 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=282.5 (sync 0.1 issue 72.2 swap 195.9) start-delay=165.1 queue=196.0
  FS home base rep1: n=46 (+0 flagged) total p50=634 p90=736 p99=905 max=971 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=261.9 (sync 0.1 issue 67.9 swap 143.7) start-delay=158.2 queue=177.1
  FS more base rep1: n=58 (+2 flagged) total p50=555 p90=757 p99=946 max=1029 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=220.3 (sync 0.1 issue 77.7 swap 132.4) start-delay=142.3 queue=161.3
  FS timeline base rep1: n=70 (+0 flagged) total p50=436 p90=547 p99=611 max=622 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=171.1 (sync 0.2 issue 66.9 swap 100.8) start-delay=117.8 queue=128.7
  FS quiz base rep1: n=85 (+0 flagged) total p50=420 p90=555 p99=603 max=617 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.7) rt=187.6 (sync 0.1 issue 67.0 swap 76.5) start-delay=106.0 queue=123.4
## F2. Repetition 2, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep2: n=56 (+0 flagged) total p50=593 p90=772 p99=919 max=1014 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=247.4 (sync 0.1 issue 68.1 swap 140.4) start-delay=172.0 queue=188.5
  FS more base rep2: n=61 (+2 flagged) total p50=530 p90=739 p99=1008 max=1277 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=205.0 (sync 0.1 issue 68.9 swap 136.3) start-delay=146.7 queue=141.5
  FS timeline base rep2: n=75 (+0 flagged) total p50=415 p90=579 p99=696 max=726 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=173.1 (sync 0.1 issue 66.6 swap 103.6) start-delay=119.7 queue=130.3
  FS quiz base rep2: n=87 (+0 flagged) total p50=416 p90=534 p99=605 max=607 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=167.3 (sync 0.1 issue 68.3 swap 74.4) start-delay=109.7 queue=121.9
  FS home new rep2: n=56 (+0 flagged) total p50=597 p90=768 p99=946 max=994 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=223.1 (sync 0.5 issue 67.3 swap 142.3) start-delay=153.1 queue=193.5
  FS more new rep2: n=58 (+2 flagged) total p50=550 p90=732 p99=997 max=1100 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=202.5 (sync 0.1 issue 70.2 swap 130.2) start-delay=149.2 queue=154.3
  FS timeline new rep2: n=63 (+0 flagged) total p50=502 p90=703 p99=801 max=806 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=205.7 (sync 0.1 issue 69.0 swap 108.0) start-delay=139.2 queue=144.5
  FS quiz new rep2: n=91 (+2 flagged) total p50=602 p90=750 p99=877 max=934 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=262.3 (sync 0.1 issue 72.2 swap 156.2) start-delay=174.0 queue=190.4
## F2. Repetition 3, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep3: n=59 (+0 flagged) total p50=584 p90=725 p99=947 max=959 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=215.8 (sync 0.1 issue 66.7 swap 143.1) start-delay=153.2 queue=158.9
WARN: tab bar not reached
  FS more new rep3: n=71 (+0 flagged) total p50=406 p90=559 p99=634 max=674 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=165.7 (sync 0.3 issue 65.2 swap 94.2) start-delay=120.8 queue=133.7
WARN: tab bar not reached
MORE: tile 'Лента на времето|Timeline' not found: text="НЧ „Васил Левски – 1922“ – с. Яворец се включва в инициативите, посветени на Деня на четенето и Деня на европейските автори с посланието #ReadForReal.&#10;На 25 ноември 2026 г. от 10:30 ч. в Народно читалище „Васил Левски – 1922“ – с. Яворец ще се проведе литературна среща с участието на хора 
  scenario timeline new rep3: not run
  FS quiz new rep3: n=89 (+1 flagged) total p50=596 p90=800 p99=1047 max=1131 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=277.7 (sync 0.1 issue 76.0 swap 183.3) start-delay=138.2 queue=188.2
  FS home base rep3: n=65 (+0 flagged) total p50=534 p90=715 p99=965 max=1000 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=188.5 (sync 0.3 issue 70.0 swap 125.3) start-delay=146.3 queue=139.2
  FS more base rep3: n=50 (+2 flagged) total p50=569 p90=807 p99=1087 max=1184 ms | medians: ui=0.2 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=231.6 (sync 0.1 issue 70.2 swap 146.7) start-delay=174.0 queue=149.9
  FS timeline base rep3: n=85 (+0 flagged) total p50=359 p90=581 p99=680 max=732 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=156.7 (sync 0.1 issue 67.3 swap 69.5) start-delay=89.5 queue=103.4
  FS quiz base rep3: n=85 (+0 flagged) total p50=422 p90=540 p99=697 max=698 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.7) rt=167.2 (sync 0.1 issue 67.3 swap 79.5) start-delay=107.2 queue=119.7
## F2. Repetition 4, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep4: n=67 (+0 flagged) total p50=498 p90=686 p99=757 max=805 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=191.9 (sync 0.1 issue 66.8 swap 124.4) start-delay=145.5 queue=148.4
WARN: tab bar not reached
  FS more base rep4: n=61 (+0 flagged) total p50=436 p90=613 p99=689 max=718 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=174.3 (sync 0.1 issue 66.5 swap 107.5) start-delay=86.8 queue=124.8
  FS timeline base rep4: n=71 (+0 flagged) total p50=368 p90=615 p99=683 max=720 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=169.0 (sync 0.1 issue 65.6 swap 106.0) start-delay=102.3 queue=117.3
  FS quiz base rep4: n=84 (+0 flagged) total p50=411 p90=534 p99=580 max=584 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=179.6 (sync 0.1 issue 68.3 swap 73.6) start-delay=93.2 queue=123.8
  FS home new rep4: n=61 (+0 flagged) total p50=598 p90=774 p99=967 max=1003 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=228.9 (sync 0.1 issue 67.1 swap 137.8) start-delay=162.4 queue=190.4
  FS more new rep4: n=65 (+0 flagged) total p50=523 p90=662 p99=784 max=784 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=200.4 (sync 0.1 issue 67.8 swap 128.6) start-delay=134.6 queue=144.9
  FS timeline new rep4: n=76 (+0 flagged) total p50=486 p90=688 p99=777 max=829 ms | medians: ui=0.5 (input 0.0 anim 0.4 layout 0.0 draw 0.1) rt=179.3 (sync 0.2 issue 67.1 swap 93.0) start-delay=133.9 queue=144.0
  FS quiz new rep4: n=88 (+4 flagged) total p50=624 p90=796 p99=895 max=919 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=273.1 (sync 0.1 issue 73.9 swap 190.1) start-delay=161.3 queue=182.6
## F2. Repetition 5, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep5: n=58 (+0 flagged) total p50=534 p90=711 p99=798 max=817 ms | medians: ui=1.0 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=203.8 (sync 0.1 issue 66.3 swap 134.1) start-delay=144.7 queue=144.5
  FS more new rep5: n=68 (+0 flagged) total p50=540 p90=685 p99=733 max=753 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=198.0 (sync 0.1 issue 68.8 swap 131.7) start-delay=129.5 queue=136.3
  FS timeline new rep5: n=65 (+0 flagged) total p50=483 p90=804 p99=873 max=883 ms | medians: ui=0.4 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=198.1 (sync 0.4 issue 67.6 swap 97.2) start-delay=139.9 queue=149.4
  FS quiz new rep5: n=87 (+2 flagged) total p50=592 p90=777 p99=855 max=884 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=276.6 (sync 0.1 issue 71.4 swap 184.6) start-delay=154.7 queue=185.8
  FS home base rep5: n=53 (+0 flagged) total p50=590 p90=742 p99=886 max=897 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=256.6 (sync 0.2 issue 74.6 swap 144.7) start-delay=167.2 queue=181.4
  FS more base rep5: n=51 (+3 flagged) total p50=534 p90=695 p99=808 max=815 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=214.6 (sync 0.1 issue 72.0 swap 132.0) start-delay=129.9 queue=151.0
  FS timeline base rep5: n=78 (+0 flagged) total p50=445 p90=632 p99=731 max=779 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=186.5 (sync 0.1 issue 68.5 swap 108.2) start-delay=128.7 queue=136.9
  FS quiz base rep5: n=85 (+0 flagged) total p50=433 p90=530 p99=593 max=650 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.7) rt=186.9 (sync 0.1 issue 67.4 swap 75.5) start-delay=110.7 queue=122.9
## F2. Repetition 6, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep6: n=69 (+0 flagged) total p50=502 p90=713 p99=774 max=794 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=199.4 (sync 0.2 issue 67.6 swap 119.1) start-delay=140.2 queue=147.5
WARN: tab bar not reached
  FS more base rep6: n=62 (+0 flagged) total p50=519 p90=705 p99=817 max=847 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=207.7 (sync 0.3 issue 68.4 swap 133.4) start-delay=148.1 queue=156.1
  FS timeline base rep6: n=76 (+0 flagged) total p50=432 p90=615 p99=743 max=766 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=178.7 (sync 0.1 issue 67.4 swap 105.1) start-delay=126.5 queue=135.1
  FS quiz base rep6: n=83 (+0 flagged) total p50=425 p90=568 p99=704 max=728 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=186.6 (sync 0.1 issue 68.3 swap 81.1) start-delay=96.8 queue=117.3
  FS home new rep6: n=65 (+0 flagged) total p50=550 p90=742 p99=808 max=812 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=200.9 (sync 0.1 issue 68.3 swap 131.4) start-delay=170.0 queue=173.8
WARN: tab bar not reached
  FS more new rep6: n=74 (+0 flagged) total p50=458 p90=618 p99=813 max=851 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=183.4 (sync 0.1 issue 67.6 swap 111.0) start-delay=132.6 queue=137.3
  FS timeline new rep6: n=71 (+0 flagged) total p50=527 p90=743 p99=895 max=934 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=228.3 (sync 0.1 issue 73.1 swap 149.8) start-delay=142.9 queue=149.3
  FS quiz new rep6: n=87 (+3 flagged) total p50=597 p90=807 p99=914 max=1000 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=267.1 (sync 0.1 issue 70.8 swap 186.7) start-delay=164.4 queue=187.8
## F3. Totals (rep 0 is excluded)
| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 7 | 59 | **590.1** (498.4..634.2) | 734 | 886 | 0.4 | 222.6 | 0.0 | 0.2 | 0.1 | 67.9 | 140.4 |
| home | new | 7 | 59 | **583.5** (526.7..597.6) | 756 | 921 | 0.4 | 215.8 | 0.0 | 0.2 | 0.1 | 67.1 | 137.8 |
| more | base | 7 | 61 | **530.1** (436.1..568.8) | 717 | 921 | 0.2 | 207.7 | 0.0 | 0.1 | 0.1 | 68.9 | 132.4 |
| more | new | 7 | 67 | **511.9** (406.0..549.6) | 662 | 749 | 0.2 | 198.0 | 0.0 | 0.1 | 0.1 | 67.8 | 126.8 |
| quiz | base | 6 | 85 | **421.1** (410.6..432.7) | 537 | 604 | 0.8 | 183.1 | 0.0 | 0.7 | 0.1 | 67.8 | 76.0 |
| quiz | new | 6 | 88 | **599.8** (592.2..648.8) | 786 | 886 | 0.4 | 274.9 | 0.0 | 0.3 | 0.1 | 72.2 | 185.6 |
| timeline | base | 7 | 75 | **431.8** (358.5..461.9) | 597 | 683 | 0.4 | 173.1 | 0.0 | 0.1 | 0.1 | 66.9 | 105.1 |
| timeline | new | 6 | 70 | **494.2** (459.2..548.6) | 723 | 850 | 0.5 | 201.9 | 0.0 | 0.1 | 0.1 | 68.9 | 102.6 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [591.9, 634.2, 593.0, 534.3, 498.4, 590.1, 502.2]
- home / new: [592.8, 526.7, 596.5, 583.5, 597.6, 534.5, 550.0] vs base: median diff -6.6 ms (UI +0.0, RT -6.8, issue -0.8); Mann-Whitney U=28.0, p=0.710; ranges overlap
- more / base: [521.4, 554.5, 530.1, 568.8, 436.1, 534.0, 518.9]
- more / new: [511.9, 468.9, 549.6, 406.0, 522.6, 539.6, 458.3] vs base: median diff -18.2 ms (UI +0.0, RT -9.7, issue -1.1); Mann-Whitney U=16.0, p=0.318; ranges overlap
- quiz / base: [420.3, 415.9, 422.0, 410.6, 432.7, 425.3]
- quiz / new: [648.8, 602.2, 596.4, 624.1, 592.2, 597.4] vs base: median diff +178.7 ms (UI -0.4, RT +91.8, issue +4.4); Mann-Whitney U=36.0, p=0.002; ranges do NOT overlap
- timeline / base: [461.9, 436.1, 415.3, 358.5, 368.4, 444.7, 431.8]
- timeline / new: [548.6, 459.2, 502.1, 486.3, 482.9, 527.1] vs base: median diff +62.4 ms (UI +0.1, RT +28.8, issue +2.0); Mann-Whitney U=41.0, p=0.002; ranges overlap
FR part: FATAL lines 0; host load: 3.02 3.26 3.53
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4835', '8003', '10531', '13953', '14974', '18788', '19818', '23096']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 40
   skipped-frames events=40 max=110 total=2320
== Davey: 116
   davey events=116 max=3542ms median=822ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## fr33b

```
part fr33b; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## F0. 1.2.38 (org.chyavorec.app) and 1.2.37 (org.chyavorec.app.b37) side by side, both compiled speed-profile
X installed new: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 4324 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~5f0bjwAIefCEkcdWYxL0Kg==/org.chyavorec.app-2FH1tWmZpsPfZHHFJBYZEQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000;  versionCode=1990000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 3982 ms
X compile org.chyavorec.app.b37: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~ffC4nNYSMXmssLvbKhPsXw==/org.chyavorec.app.b37-lSZfKoPWKBNQ9mvKwY_ROg==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
animations: animator_duration_scale=1 transition=1 window=1; host load: 5.01 3.28 1.42
## F1. Warm-up (rep 0, not counted): every scenario once; the quiz taps are learned
  FS home new rep0: n=70 (+0 flagged) total p50=487 p90=647 p99=756 max=762 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=186.5 (sync 0.5 issue 53.7 swap 111.0) start-delay=147.6 queue=156.3
  FS more new rep0: n=55 (+2 flagged) total p50=439 p90=685 p99=950 max=978 ms | medians: ui=0.3 (input 0.0 anim 0.0 layout 0.0 draw 0.2) rt=172.3 (sync 0.1 issue 54.9 swap 102.4) start-delay=100.4 queue=99.6
  FS timeline new rep0: n=92 (+0 flagged) total p50=385 p90=513 p99=604 max=672 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=153.5 (sync 0.1 issue 56.2 swap 76.6) start-delay=106.0 queue=114.8
QUIZ taps new: 5 questions learned: input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 706; sleep 2; input tap 540 2226; sleep 2.2~input tap 540 780; sleep 2; input tap 540 2226; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Избродирай отговорите"
  FS home base rep0: n=71 (+0 flagged) total p50=510 p90=636 p99=697 max=731 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=210.8 (sync 0.2 issue 58.5 swap 127.5) start-delay=137.1 queue=146.6
  FS more base rep0: n=72 (+0 flagged) total p50=430 p90=562 p99=613 max=641 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=177.7 (sync 0.2 issue 53.4 swap 106.3) start-delay=119.8 queue=140.6
  FS timeline base rep0: n=81 (+0 flagged) total p50=317 p90=436 p99=480 max=481 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=126.6 (sync 0.1 issue 52.4 swap 56.5) start-delay=83.8 queue=93.0
QUIZ taps base: 5 questions learned: input tap 540 780; sleep 2; input tap 540 1863; sleep 2.2~input tap 540 780; sleep 2; input tap 540 1863; sleep 2.2~input tap 540 706; sleep 2; input tap 540 1612; sleep 2.2~input tap 540 706; sleep 2; input tap 540 1612; sleep 2.2~input tap 540 780; sleep 2; input tap 540 1919; sleep 2.2~; result: UNKNOWN text="Утре ще е по-добре — Яворец има още какво да разкаже." text="2/5" text="2 верни от 5" text="Серия: 1 ден поред" 
## F2. Repetition 1, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep1: n=57 (+0 flagged) total p50=425 p90=572 p99=672 max=678 ms | medians: ui=0.5 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=194.3 (sync 0.1 issue 63.9 swap 107.5) start-delay=106.4 queue=126.0
  FS more base rep1: n=69 (+5 flagged) total p50=451 p90=581 p99=795 max=979 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=177.9 (sync 0.4 issue 56.0 swap 104.6) start-delay=125.9 queue=137.7
  FS timeline base rep1: n=88 (+0 flagged) total p50=373 p90=449 p99=512 max=531 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=143.3 (sync 0.1 issue 53.4 swap 84.9) start-delay=104.6 queue=110.2
  FS quiz base rep1: n=100 (+0 flagged) total p50=337 p90=416 p99=481 max=513 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=126.5 (sync 0.1 issue 52.7 swap 57.9) start-delay=92.9 queue=98.7
  FS home new rep1: n=75 (+0 flagged) total p50=478 p90=619 p99=758 max=788 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=185.4 (sync 0.1 issue 53.5 swap 107.4) start-delay=142.2 queue=147.8
  FS more new rep1: n=80 (+0 flagged) total p50=404 p90=526 p99=597 max=622 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=158.7 (sync 0.1 issue 51.7 swap 100.2) start-delay=117.1 queue=126.6
  FS timeline new rep1: n=75 (+0 flagged) total p50=374 p90=559 p99=643 max=683 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=135.8 (sync 0.1 issue 53.1 swap 70.3) start-delay=101.4 queue=111.4
  FS quiz new rep1: n=94 (+6 flagged) total p50=485 p90=613 p99=692 max=711 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=210.6 (sync 0.1 issue 58.7 swap 121.7) start-delay=95.8 queue=140.8
## F2. Repetition 2, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep2: n=76 (+0 flagged) total p50=473 p90=569 p99=681 max=723 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=193.2 (sync 0.1 issue 55.4 swap 108.6) start-delay=142.5 queue=154.0
  FS more new rep2: n=78 (+1 flagged) total p50=428 p90=559 p99=597 max=608 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=165.4 (sync 0.1 issue 67.4 swap 100.8) start-delay=112.3 queue=133.7
  FS timeline new rep2: n=92 (+0 flagged) total p50=386 p90=532 p99=637 max=697 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=165.5 (sync 0.1 issue 54.5 swap 78.2) start-delay=100.7 queue=108.2
  FS quiz new rep2: n=102 (+3 flagged) total p50=465 p90=602 p99=647 max=683 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=204.8 (sync 0.2 issue 57.0 swap 115.5) start-delay=126.3 queue=150.4
  FS home base rep2: n=79 (+2 flagged) total p50=425 p90=549 p99=614 max=627 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=162.9 (sync 0.1 issue 53.2 swap 105.2) start-delay=133.9 queue=144.7
  FS more base rep2: n=72 (+0 flagged) total p50=477 p90=615 p99=737 max=746 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=183.9 (sync 0.1 issue 71.2 swap 105.1) start-delay=129.3 queue=143.7
  FS timeline base rep2: n=90 (+0 flagged) total p50=293 p90=414 p99=481 max=518 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=124.5 (sync 0.1 issue 51.5 swap 46.5) start-delay=77.0 queue=84.7
  FS quiz base rep2: n=99 (+0 flagged) total p50=345 p90=417 p99=478 max=479 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=121.5 (sync 0.1 issue 53.2 swap 59.1) start-delay=88.0 queue=100.4
## F2. Repetition 3, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep3: n=79 (+0 flagged) total p50=403 p90=583 p99=647 max=661 ms | medians: ui=0.7 (input 0.0 anim 0.3 layout 0.0 draw 0.2) rt=151.0 (sync 0.4 issue 52.4 swap 93.0) start-delay=100.0 queue=122.2
WARN: tab bar not reached
  FS more base rep3: n=81 (+0 flagged) total p50=348 p90=493 p99=685 max=729 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=136.8 (sync 0.1 issue 50.9 swap 85.6) start-delay=91.0 queue=104.6
  FS timeline base rep3: n=82 (+0 flagged) total p50=343 p90=468 p99=538 max=590 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=137.5 (sync 0.1 issue 51.7 swap 78.8) start-delay=103.8 queue=117.2
  FS quiz base rep3: n=102 (+0 flagged) total p50=330 p90=407 p99=467 max=468 ms | medians: ui=0.7 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=147.2 (sync 0.1 issue 53.9 swap 61.7) start-delay=87.4 queue=98.7
  FS home new rep3: n=80 (+0 flagged) total p50=473 p90=569 p99=630 max=631 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=168.2 (sync 0.1 issue 52.9 swap 106.8) start-delay=146.0 queue=152.3
  FS more new rep3: n=59 (+2 flagged) total p50=436 p90=586 p99=764 max=852 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=168.5 (sync 0.1 issue 53.5 swap 99.0) start-delay=127.1 queue=122.9
  FS timeline new rep3: n=83 (+0 flagged) total p50=447 p90=588 p99=652 max=704 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=182.0 (sync 0.3 issue 61.0 swap 117.1) start-delay=111.0 queue=119.6
  FS quiz new rep3: n=101 (+4 flagged) total p50=459 p90=593 p99=634 max=668 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=198.6 (sync 0.1 issue 56.6 swap 106.7) start-delay=103.6 queue=141.1
## F2. Repetition 4, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep4: n=64 (+0 flagged) total p50=451 p90=561 p99=660 max=682 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=176.5 (sync 0.1 issue 54.3 swap 106.0) start-delay=144.7 queue=146.4
  FS more new rep4: n=54 (+1 flagged) total p50=429 p90=548 p99=760 max=763 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=158.7 (sync 0.1 issue 55.2 swap 101.8) start-delay=128.1 queue=117.0
  FS timeline new rep4: n=91 (+0 flagged) total p50=346 p90=467 p99=570 max=572 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=131.0 (sync 0.1 issue 53.0 swap 71.9) start-delay=96.0 queue=109.7
  FS quiz new rep4: n=97 (+5 flagged) total p50=441 p90=590 p99=646 max=647 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=203.5 (sync 0.1 issue 56.0 swap 111.5) start-delay=105.6 queue=151.3
  FS home base rep4: n=70 (+0 flagged) total p50=460 p90=582 p99=694 max=698 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=185.7 (sync 0.1 issue 55.5 swap 108.4) start-delay=132.0 queue=139.8
  FS more base rep4: n=60 (+2 flagged) total p50=447 p90=645 p99=729 max=769 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=175.8 (sync 0.1 issue 59.7 swap 106.4) start-delay=121.3 queue=125.8
  FS timeline base rep4: n=81 (+0 flagged) total p50=305 p90=470 p99=555 max=592 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=132.1 (sync 0.1 issue 51.0 swap 76.7) start-delay=89.7 queue=102.3
  FS quiz base rep4: n=100 (+0 flagged) total p50=325 p90=401 p99=440 max=453 ms | medians: ui=0.7 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=130.6 (sync 0.1 issue 51.7 swap 57.6) start-delay=87.6 queue=97.6
## F2. Repetition 5, order: base new (a fresh process for each; Home, Още, timeline, quiz)
  FS home base rep5: n=61 (+0 flagged) total p50=483 p90=634 p99=700 max=732 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=185.6 (sync 0.1 issue 54.4 swap 113.6) start-delay=134.0 queue=155.5
  FS more base rep5: n=70 (+0 flagged) total p50=469 p90=584 p99=639 max=656 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=172.5 (sync 0.1 issue 54.4 swap 106.8) start-delay=115.4 queue=142.9
  FS timeline base rep5: n=84 (+0 flagged) total p50=337 p90=445 p99=514 max=522 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.1) rt=134.3 (sync 0.1 issue 52.0 swap 81.2) start-delay=94.3 queue=104.8
  FS quiz base rep5: n=99 (+0 flagged) total p50=337 p90=421 p99=518 max=565 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=142.5 (sync 0.1 issue 54.0 swap 57.8) start-delay=89.9 queue=99.7
  FS home new rep5: n=81 (+0 flagged) total p50=453 p90=608 p99=713 max=719 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=169.5 (sync 0.1 issue 53.0 swap 104.7) start-delay=133.9 queue=146.3
  FS more new rep5: n=76 (+0 flagged) total p50=453 p90=549 p99=586 max=595 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=180.5 (sync 0.1 issue 64.2 swap 101.1) start-delay=126.1 queue=139.4
  FS timeline new rep5: n=74 (+0 flagged) total p50=407 p90=580 p99=625 max=660 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=175.2 (sync 0.1 issue 54.3 swap 87.1) start-delay=99.3 queue=115.3
  FS quiz new rep5: n=103 (+2 flagged) total p50=462 p90=572 p99=727 max=740 ms | medians: ui=0.5 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=204.2 (sync 0.1 issue 57.8 swap 111.3) start-delay=109.3 queue=145.1
## F2. Repetition 6, order: new base (a fresh process for each; Home, Още, timeline, quiz)
  FS home new rep6: n=71 (+0 flagged) total p50=477 p90=594 p99=663 max=668 ms | medians: ui=0.3 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=187.1 (sync 0.1 issue 54.5 swap 107.3) start-delay=145.2 queue=149.4
  FS more new rep6: n=68 (+0 flagged) total p50=397 p90=531 p99=567 max=567 ms | medians: ui=0.2 (input 0.0 anim 0.0 layout 0.0 draw 0.1) rt=154.5 (sync 0.1 issue 53.4 swap 98.3) start-delay=114.1 queue=120.6
  FS timeline new rep6: n=77 (+0 flagged) total p50=395 p90=546 p99=645 max=669 ms | medians: ui=0.5 (input 0.0 anim 0.3 layout 0.0 draw 0.1) rt=151.9 (sync 0.1 issue 53.4 swap 82.4) start-delay=109.9 queue=120.8
  FS quiz new rep6: n=98 (+6 flagged) total p50=462 p90=603 p99=667 max=688 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.3) rt=205.2 (sync 0.1 issue 54.8 swap 116.8) start-delay=106.9 queue=148.7
  FS home base rep6: n=70 (+0 flagged) total p50=455 p90=572 p99=618 max=627 ms | medians: ui=0.4 (input 0.0 anim 0.2 layout 0.0 draw 0.2) rt=186.7 (sync 0.1 issue 52.9 swap 110.2) start-delay=114.3 queue=139.0
  FS more base rep6: n=39 (+1 flagged) total p50=430 p90=563 p99=644 max=660 ms | medians: ui=0.4 (input 0.0 anim 0.1 layout 0.0 draw 0.2) rt=177.6 (sync 0.5 issue 55.1 swap 99.0) start-delay=101.2 queue=118.6
  FS timeline base rep6: n=98 (+0 flagged) total p50=319 p90=455 p99=541 max=573 ms | medians: ui=0.3 (input 0.0 anim 0.2 layout 0.0 draw 0.1) rt=130.2 (sync 0.1 issue 52.7 swap 78.4) start-delay=92.6 queue=102.1
  FS quiz base rep6: n=97 (+0 flagged) total p50=342 p90=452 p99=501 max=525 ms | medians: ui=0.8 (input 0.0 anim 0.1 layout 0.0 draw 0.6) rt=132.5 (sync 0.1 issue 54.6 swap 58.0) start-delay=93.7 queue=100.5
## F3. Totals (rep 0 is excluded)
| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 7 | 70 | **454.7** (403.5..510.1) | 582 | 672 | 0.4 | 185.7 | 0.0 | 0.2 | 0.1 | 54.4 | 108.4 |
| home | new | 7 | 75 | **473.4** (450.6..486.8) | 594 | 681 | 0.4 | 185.4 | 0.0 | 0.2 | 0.1 | 53.7 | 107.3 |
| more | base | 7 | 70 | **446.8** (347.9..477.2) | 581 | 685 | 0.2 | 177.6 | 0.0 | 0.1 | 0.1 | 55.1 | 105.1 |
| more | new | 7 | 68 | **428.9** (397.2..452.6) | 549 | 597 | 0.2 | 165.4 | 0.0 | 0.1 | 0.1 | 54.9 | 100.8 |
| quiz | base | 6 | 100 | **337.1** (325.2..344.7) | 416 | 480 | 0.8 | 131.6 | 0.0 | 0.6 | 0.1 | 53.5 | 57.9 |
| quiz | new | 6 | 100 | **462.1** (440.8..485.0) | 598 | 657 | 0.4 | 204.5 | 0.0 | 0.3 | 0.1 | 56.8 | 113.5 |
| timeline | base | 7 | 84 | **318.9** (292.7..372.6) | 449 | 514 | 0.3 | 132.1 | 0.0 | 0.1 | 0.1 | 52.0 | 78.4 |
| timeline | new | 7 | 83 | **385.6** (346.0..447.4) | 546 | 637 | 0.5 | 153.5 | 0.0 | 0.1 | 0.1 | 54.3 | 78.2 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [510.1, 425.3, 425.2, 403.5, 460.2, 482.6, 454.7]
- home / new: [486.8, 477.7, 473.4, 473.0, 450.6, 453.3, 476.7] vs base: median diff +18.8 ms (UI -0.1, RT -0.3, issue -0.7); Mann-Whitney U=32.0, p=0.383; ranges overlap
- more / base: [430.4, 450.9, 477.2, 347.9, 446.8, 468.6, 430.5]
- more / new: [439.4, 404.1, 428.1, 435.9, 428.9, 452.6, 397.2] vs base: median diff -17.9 ms (UI -0.0, RT -12.1, issue -0.1); Mann-Whitney U=15.0, p=0.259; ranges overlap
- quiz / base: [337.1, 344.7, 329.7, 325.2, 337.1, 341.6]
- quiz / new: [485.0, 465.1, 459.4, 440.8, 462.5, 461.7] vs base: median diff +125.0 ms (UI -0.3, RT +72.9, issue +3.3); Mann-Whitney U=36.0, p=0.002; ranges do NOT overlap
- timeline / base: [317.4, 372.6, 292.7, 342.6, 304.6, 336.9, 318.9]
- timeline / new: [385.1, 373.6, 385.6, 447.4, 346.0, 406.8, 394.9] vs base: median diff +66.7 ms (UI +0.2, RT +21.4, issue +2.3); Mann-Whitney U=48.0, p=0.001; ranges overlap
FR part: FATAL lines 0; host load: 4.22 3.99 3.78
contact sheets: 2 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5155', '7644', '11294', '12326', '15655', '16662', '19618', '20611']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 27
   skipped-frames events=27 max=89 total=1581
== Davey: 48
   davey events=48 max=4203ms median=1240ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## fs33

```
part fs33; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 4950 ms
font_scale 2.0; 1080x2400 px, density 420, font_scale 2.0, night no, app locale [bg-BG], rotation 0
## FS1. Quiz at font scale 2.0: question, answered (feedback + band + Напред), result
CAP font200/quiz-q1: targets=5 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 0 верни от 0 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Решение за собствена сграда · Въздържателно дружество „Бор · Общностен и информационен це
    CLIP: cut 0, squeezed 0, overlapping clickables 0
QUIZ options visible without scrolling: 4 of 4; next button: 1
CAP font200/quiz-q1-scrolled: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Какво се случва в читалището · Решение за собствена сграда · Въздържателно дружество „Бор · Общностен и информационен це · Трудни години — устояване · Избродирай отговорите
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/quiz-q1-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 1 верни от 1 отговорени, общ · Въпрос 1 от 5 · Какво се случва в читалището · Решение за собствена сграда · Вярно! · Започва отчуждаване на терен
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/quiz-q2-answered: targets=2 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | 2 верни от 2 отговорени, общ · Въпрос 2 от 5 · Какво се случва в читалището · Първият радиоапарат в селото · Вярно! · Закупен от читалището. По вр
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/quiz-result: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 2/5 · 2 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/quiz-result-2: targets=3 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Утре ще е по-добре — Яворец  · 2/5 · 2 верни от 5 · Избродирай отговорите · Серия: 1 ден поред · Нови въпроси — утре.
    CLIP: cut 0, squeezed 0, overlapping clickables 0
## FS2. Timeline at font scale 2.0: top, while scrolling (the decade scrubber), further down
CAP font200/tl-1: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=6 ellipsized=0 | Лента на времето · От 1900 г. до днес · 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · Назад
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/tl-3: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | 1900. Театрален кръжец и зар · Театрален кръжец и зараждане · Ученици от Севлиевската гимн · 1909. Организирано читалищно · Организирано читалищно дело · Влади Илиев, Христо Лалев и 
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/tl-4: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | 1900. Театрален кръжец и зар · Ученици от Севлиевската гимн · 1909. Организирано читалищно · Организирано читалищно дело · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/tl-6: targets=1 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | 1909. Организирано читалищно · Организирано читалищно дело · Влади Илиев, Христо Лалев и  · 1912–1918. Прекъсване от вой · Прекъсване от войните · Балканската война (1912) пре
    CLIP: cut 0, squeezed 0, overlapping clickables 0
## FS3. Settings at font scale 2.0 (every screenful)
CAP font200/set-1: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=17 ellipsized=0 | ИЗГЛЕД · Тема · Както системата · Размер на текста · Стандартен · Палитра
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/set-2: targets=7 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=15 ellipsized=0 | Размер на текста · Стандартен · Палитра · Яворец · Яворец · Злато и жито
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/set-3: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Злато и жито · Зелено и сребро · Червено, черно и слонова кос · Черно (OLED) · Чисто черен фон в тъмната те · Цветове от тапета
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/set-4: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Чисто черен фон в тъмната те · Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/set-5: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | Цветове от тапета · Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/set-6: targets=6 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=1 | front: app | texts=11 ellipsized=0 | Цветовете на телефона, взети · Език · Български · Сезонни украси · Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“
    CLIP: cut 0, squeezed 1, overlapping clickables 0: 'Добави уиджета „Читалище' only 13px high
CAP font200/set-7: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=9 ellipsized=0 | Празници, сезони и обичаи: п · Разклащане за „Изненадай ме“ · Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо
    CLIP: cut 0, squeezed 1, overlapping clickables 0: 'Добави „Книга на деня“' only 23px high
CAP font200/set-8: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=8 ellipsized=0 | Разклати телефона в каталога · УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/set-9: targets=4 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=10 ellipsized=0 | УИДЖЕТИ · Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/set-10: targets=5 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=11 ellipsized=0 | Добави уиджета „Читалище Яво · Срокове за връщане, следващо · Добави „Книга на деня“ · Всеки ден друга книга от кат · ИЗВЕСТИЯ · Избери известията
    CLIP: cut 0, squeezed 0, overlapping clickables 0
## FS4. Login at font scale 2.0 (FLAG_SECURE: host screenshots + uiautomator)
CAP font200/my-1: targets=9 MISSING=0 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=0 | front: app | texts=20 ellipsized=0 | Примерна читателска карта · ЧИТАТЕЛСКА КАРТА · НЧ „Васил Левски – 1922“ · Мария Иванова · Читателски № 19220001 · Баркод на карта 19220001
    CLIP: cut 0, squeezed 0, overlapping clickables 0
CAP font200/my-2: targets=12 MISSING=1 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=18 ellipsized=0 | Вход · Добави читателска карта · БИБЛИОТЕКА · Читателска карта · Добави номера от своята карт · Моите книги
    CLIP: cut 0, squeezed 1, overlapping clickables 0: 'Вход' only 5px high
LOGIN font 2.0, screenful 1: CLIP: cut 0, squeezed 0, overlapping clickables 0; sign-in button: ; texts: content-desc="Примерна читателска карта" text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="Мария Иванова" text="Читателски № 19220001" content-desc="Баркод на карта 19220001" text="Така ще изглежда картата ти" text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройст
LOGIN font 2.0, screenful 2: CLIP: cut 0, squeezed 0, overlapping clickables 0; sign-in button: 540 1570 enabled=false class=android.view.View bounds=(63, 1500, 1017, 1640); texts: text="Така ще изглежда картата ти" text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от Android Keystore." content-desc="Назад" text="Вход" 
LOGIN font 2.0, screenful 3: CLIP: cut 0, squeezed 0, overlapping clickables 0; sign-in button: 540 1456 enabled=false class=android.view.View bounds=(63, 1386, 1017, 1526); texts: text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от Android Keystore." content-desc="Назад" text="Вход" 
LOGIN font 2.0, screenful 4: CLIP: cut 0, squeezed 0, overlapping clickables 0; sign-in button: 540 1456 enabled=false class=android.view.View bounds=(63, 1386, 1017, 1526); texts: text="Вход за читатели" text="Читателски номер" text="ПИН (от библиотеката)" content-desc="Покажи ПИН-а" text="Запомни ме на това устройство" text="Вход" text="Забравен ПИН?" text="ПИН-ът не се съхранява на устройството. Сесията се пази шифрована с ключ от Android Keystore." content-desc="Назад" text="Вход" 
## FS5. The same four at „Много голям“ in the app + system 2.0 is capped at 2.0 — only Home for reference
CAP font200/home-1: targets=12 MISSING=2 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=2 | front: app | texts=18 ellipsized=1 [Търси книга, новина, събитие…] | НЧ „Васил Левски – 1922“ · с. Яворец · Съобщения · Лента за търсене · Търси книга, новина, събитие · Сканирай баркод
    CLIP: cut 0, squeezed 0, overlapping clickables 0
A11Y AGG: 23 screens analysed, 0 dumps failed; totals MISSING=4 DOUBLE=0 SAME-LABEL=0 SMALL(<48dp)=5
== MISSING: 4 distinct
   View@[42,1799][1038,1925]  [1 screens: quiz-q1]
   View@[42,2032][1038,2127]  [1 screens: my-2]
   View@[42,2080][540,2127]  [1 screens: home-1]
   View@[540,2080][1038,2127]  [1 screens: home-1]
== SMALL: 3 distinct
   ''  [2 screens: home-1, my-2]
   'Цветовете на телефона, взети о'  [1 screens: set-6]
   'Вход'  [1 screens: my-2]
== SAME-LABEL: 0 distinct
== DOUBLE: 0 distinct
FS part: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 4 in sheets/
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4861', '6110']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 22
   skipped-frames events=22 max=112 total=1127
== Davey: 48
   davey events=48 max=4042ms median=886ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'ANR': 1} → crashes.txt
```

## mem33a

```
part mem33a; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## M0. Both versions installed side by side (speed-profile)
X installed new: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 4779 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~uPviQnFJY4kTbyDlbt6u6Q==/org.chyavorec.app-wROQu-Tp1K-v5WcanE0jxw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000;  versionCode=1990000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 3588 ms
X compile org.chyavorec.app.b37: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~x2ksKr92EAEc3pY0IrGKyw==/org.chyavorec.app.b37-gprN6ldOapoedlYCK8tsHA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
## M1. new: 30 cycles (timeline + 2 scrolls, quiz, Още + scroll, book details), meminfo after trim + GC every 5
MEM new c0: PSS=77650 Java=10980 Native=23984 Graphics=0 Code=30436 PrivOther=2416 System=9110 | DalvikAlloc=6368 NativeAlloc=26351 | Views=9 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=152080 (KB) pid 8014
MEM new c5: PSS=93658 Java=14548 Native=32700 Graphics=0 Code=26076 PrivOther=8108 System=11458 | DalvikAlloc=8187 NativeAlloc=25431 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=171532 (KB) pid 8014
MEM new c10: PSS=93251 Java=14580 Native=33132 Graphics=0 Code=25696 PrivOther=7984 System=11107 | DalvikAlloc=8234 NativeAlloc=19707 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=172596 (KB) pid 8014
MEM new c15: PSS=92820 Java=14644 Native=33360 Graphics=0 Code=25724 PrivOther=7988 System=10320 | DalvikAlloc=8336 NativeAlloc=13513 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=173520 (KB) pid 8014
MEM new c20: PSS=93384 Java=14648 Native=33688 Graphics=0 Code=25916 PrivOther=7988 System=10328 | DalvikAlloc=8343 NativeAlloc=7286 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=174220 (KB) pid 8014
MEM new c25: PSS=92550 Java=14756 Native=33336 Graphics=0 Code=25500 PrivOther=7916 System=10262 | DalvikAlloc=8488 NativeAlloc=662 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=174196 (KB) pid 8014
MEM new c30: PSS=92979 Java=14768 Native=33660 Graphics=0 Code=25552 PrivOther=7920 System=10299 | DalvikAlloc=8477 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=174676 (KB) pid 8014
MEM new: pid at the end 8014; process deaths during the run: 0
## M1. base: 30 cycles (timeline + 2 scrolls, quiz, Още + scroll, book details), meminfo after trim + GC every 5
MEM base c0: PSS=80083 Java=12200 Native=25008 Graphics=0 Code=28376 PrivOther=3068 System=10055 | DalvikAlloc=7375 NativeAlloc=26974 | Views=9 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=161380 (KB) pid 25356
MEM base c5: PSS=97574 Java=14420 Native=33220 Graphics=0 Code=30000 PrivOther=8324 System=10850 | DalvikAlloc=8309 NativeAlloc=17946 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=181488 (KB) pid 25356
MEM base c10: PSS=97739 Java=14412 Native=33532 Graphics=0 Code=29816 PrivOther=8332 System=10899 | DalvikAlloc=8306 NativeAlloc=4470 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=182052 (KB) pid 25356
MEM base c15: PSS=97737 Java=14528 Native=33732 Graphics=0 Code=29804 PrivOther=8364 System=10533 | DalvikAlloc=8413 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=182584 (KB) pid 25356
MEM base c20: PSS=98398 Java=14536 Native=34224 Graphics=0 Code=29820 PrivOther=8364 System=10674 | DalvikAlloc=8418 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=183608 (KB) pid 25356
MEM base c25: PSS=97588 Java=14532 Native=34100 Graphics=0 Code=29748 PrivOther=8316 System=10112 | DalvikAlloc=8436 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=183692 (KB) pid 25356
MEM base c30: PSS=96969 Java=14672 Native=34060 Graphics=0 Code=28992 PrivOther=8264 System=10189 | DalvikAlloc=8566 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=183112 (KB) pid 25356
MEM base: pid at the end 25356; process deaths during the run: 0
## M2. Totals

new: after trim + GC, KB (cycle: 0 5 10 15 20 25 30)
| bucket | c0 | c5 | c10 | c15 | c20 | c25 | c30 | c0→last | last third slope, KB/cycle | whole slope |
|---|---|---|---|---|---|---|---|---|---|---|
| TOTAL PSS | 77650 | 93658 | 93251 | 92820 | 93384 | 92550 | 92979 | +15329 | -40.5 | +313.6 |
| Java Heap | 10980 | 14548 | 14580 | 14644 | 14648 | 14756 | 14768 | +3788 | +12.0 | +84.6 |
| Native Heap | 23984 | 32700 | 33132 | 33360 | 33688 | 33336 | 33660 | +9676 | -2.8 | +220.4 |
| Graphics | 0 | 0 | 0 | 0 | 0 | 0 | 0 | +0 | +0.0 | +0.0 |
| Code | 30436 | 26076 | 25696 | 25724 | 25916 | 25500 | 25552 | -4884 | -36.4 | -111.3 |
| Private Other | 2416 | 8108 | 7984 | 7988 | 7988 | 7916 | 7920 | +5504 | -6.8 | +115.2 |
| System | 9110 | 11458 | 11107 | 10320 | 10328 | 10262 | 10299 | +1189 | -2.9 | +2.8 |
| Dalvik Heap alloc | 6368 | 8187 | 8234 | 8336 | 8343 | 8488 | 8477 | +2109 | +13.4 | +50.3 |
| Native Heap alloc | 26351 | 25431 | 19707 | 13513 | 7286 | 662 | 0 | -26351 | -728.6 | -1007.2 |
| Views | 9 | 11 | 11 | 11 | 11 | 11 | 11 | +2 | +0.0 | +0.0 |
| ViewRootImpl | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| Activities | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| AppContexts | 5 | 5 | 5 | 5 | 5 | 5 | 5 | +0 | +0.0 | +0.0 |

base: after trim + GC, KB (cycle: 0 5 10 15 20 25 30)
| bucket | c0 | c5 | c10 | c15 | c20 | c25 | c30 | c0→last | last third slope, KB/cycle | whole slope |
|---|---|---|---|---|---|---|---|---|---|---|
| TOTAL PSS | 80083 | 97574 | 97739 | 97737 | 98398 | 97588 | 96969 | +16886 | -142.9 | +366.8 |
| Java Heap | 12200 | 14420 | 14412 | 14528 | 14536 | 14532 | 14672 | +2472 | +13.6 | +55.5 |
| Native Heap | 25008 | 33220 | 33532 | 33732 | 34224 | 34100 | 34060 | +9052 | -16.4 | +211.5 |
| Graphics | 0 | 0 | 0 | 0 | 0 | 0 | 0 | +0 | +0.0 | +0.0 |
| Code | 28376 | 30000 | 29816 | 29804 | 29820 | 29748 | 28992 | +616 | -82.8 | +9.6 |
| Private Other | 3068 | 8324 | 8332 | 8364 | 8364 | 8316 | 8264 | +5196 | -10.0 | +111.5 |
| System | 10055 | 10850 | 10899 | 10533 | 10674 | 10112 | 10189 | +134 | -48.5 | -9.3 |
| Dalvik Heap alloc | 7375 | 8309 | 8306 | 8413 | 8418 | 8436 | 8566 | +1191 | +14.8 | +28.1 |
| Native Heap alloc | 26974 | 17946 | 4470 | 0 | 0 | 0 | 0 | -26974 | +0.0 | -866.3 |
| Views | 9 | 11 | 11 | 11 | 11 | 11 | 11 | +2 | +0.0 | +0.0 |
| ViewRootImpl | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| Activities | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| AppContexts | 5 | 5 | 5 | 5 | 5 | 5 | 5 | +0 | +0.0 | +0.0 |
MEM part: FATAL lines 0
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4984', '8014']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 20
   skipped-frames events=20 max=109 total=1058
== Davey: 66
   davey events=66 max=3297ms median=774ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## mem33b

```
part mem33b; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## M0. Both versions installed side by side (speed-profile)
X installed new: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100;  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X new: catalogue after 3516 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~7MvDg9IXEzI6j0viTvpoMQ==/org.chyavorec.app-hSREAsgW5jg2ZEvQGzARig==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
X installed base: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000;  versionCode=1990000 minSdk=26 targetSdk=37  versionName=1.2.37 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
X base: catalogue after 3291 ms
X compile org.chyavorec.app.b37: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~VOJaGY7g_7DUkNXh-ZIYxg==/org.chyavorec.app.b37-g9XqYVCpCHfeXEnmg4HiGw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/VpnDialogs/VpnDialogs.apk| x86_64: [status=verify] [reason=prebuilt]|
## M1. base: 30 cycles (timeline + 2 scrolls, quiz, Още + scroll, book details), meminfo after trim + GC every 5
MEM base c0: PSS=80922 Java=10896 Native=24208 Graphics=0 Code=32580 PrivOther=2496 System=9986 | DalvikAlloc=6397 NativeAlloc=26390 | Views=9 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=154928 (KB) pid 8032
MEM base c5: PSS=101604 Java=14468 Native=32788 Graphics=0 Code=32740 PrivOther=8500 System=12184 | DalvikAlloc=8302 NativeAlloc=15973 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=179428 (KB) pid 8032
MEM base c10: PSS=100520 Java=14380 Native=33448 Graphics=0 Code=29664 PrivOther=8224 System=14064 | DalvikAlloc=8303 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=180420 (KB) pid 8032
MEM base c15: PSS=98413 Java=14456 Native=33668 Graphics=0 Code=29232 PrivOther=8208 System=12065 | DalvikAlloc=8388 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=181012 (KB) pid 8032
MEM base c20: PSS=98536 Java=14444 Native=33652 Graphics=0 Code=29244 PrivOther=8208 System=12180 | DalvikAlloc=8378 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=181452 (KB) pid 8032
MEM base c25: PSS=99150 Java=14464 Native=33520 Graphics=0 Code=29288 PrivOther=8216 System=12862 | DalvikAlloc=8403 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=181572 (KB) pid 8032
MEM base c30: PSS=96486 Java=14532 Native=33976 Graphics=0 Code=28704 PrivOther=7920 System=10526 | DalvikAlloc=8544 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=182312 (KB) pid 8032
MEM base: pid at the end 8032; process deaths during the run: 0
## M1. new: 30 cycles (timeline + 2 scrolls, quiz, Още + scroll, book details), meminfo after trim + GC every 5
MEM new c0: PSS=77150 Java=12340 Native=24884 Graphics=0 Code=26024 PrivOther=2844 System=9702 | DalvikAlloc=7422 NativeAlloc=26890 | Views=9 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=160544 (KB) pid 23828
MEM new c5: PSS=95861 Java=14372 Native=32904 Graphics=0 Code=29260 PrivOther=7984 System=10541 | DalvikAlloc=8241 NativeAlloc=25025 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=180880 (KB) pid 23828
MEM new c10: PSS=96612 Java=14436 Native=33480 Graphics=0 Code=29256 PrivOther=7996 System=10684 | DalvikAlloc=8298 NativeAlloc=18890 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=182004 (KB) pid 23828
MEM new c15: PSS=96517 Java=14536 Native=33848 Graphics=0 Code=29272 PrivOther=7864 System=10033 | DalvikAlloc=8467 NativeAlloc=10717 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=183180 (KB) pid 23828
MEM new c20: PSS=97866 Java=14540 Native=33876 Graphics=0 Code=29452 PrivOther=8184 System=11058 | DalvikAlloc=8383 NativeAlloc=3159 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=183188 (KB) pid 23828
MEM new c25: PSS=98100 Java=14712 Native=33952 Graphics=0 Code=29392 PrivOther=8180 System=11112 | DalvikAlloc=8547 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=183548 (KB) pid 23828
MEM new c30: PSS=97421 Java=14716 Native=33812 Graphics=0 Code=29408 PrivOther=8140 System=10573 | DalvikAlloc=8555 NativeAlloc=0 | Views=11 ViewRootImpl=1 Activities=1 AppContexts=5 RSS=183520 (KB) pid 23828
MEM new: pid at the end 23828; process deaths during the run: 0
## M2. Totals

base: after trim + GC, KB (cycle: 0 5 10 15 20 25 30)
| bucket | c0 | c5 | c10 | c15 | c20 | c25 | c30 | c0→last | last third slope, KB/cycle | whole slope |
|---|---|---|---|---|---|---|---|---|---|---|
| TOTAL PSS | 80922 | 101604 | 100520 | 98413 | 98536 | 99150 | 96486 | +15564 | -205.0 | +284.3 |
| Java Heap | 10896 | 14468 | 14380 | 14456 | 14444 | 14464 | 14532 | +3636 | +8.8 | +78.3 |
| Native Heap | 24208 | 32788 | 33448 | 33668 | 33652 | 33520 | 33976 | +9768 | +32.4 | +221.2 |
| Graphics | 0 | 0 | 0 | 0 | 0 | 0 | 0 | +0 | +0.0 | +0.0 |
| Code | 32580 | 32740 | 29664 | 29232 | 29244 | 29288 | 28704 | -3876 | -54.0 | -135.4 |
| Private Other | 2496 | 8500 | 8224 | 8208 | 8208 | 8216 | 7920 | +5424 | -28.8 | +112.1 |
| System | 9986 | 12184 | 14064 | 12065 | 12180 | 12862 | 10526 | +540 | -165.4 | +7.8 |
| Dalvik Heap alloc | 6397 | 8302 | 8303 | 8388 | 8378 | 8403 | 8544 | +2147 | +16.6 | +48.0 |
| Native Heap alloc | 26390 | 15973 | 0 | 0 | 0 | 0 | 0 | -26390 | +0.0 | -793.7 |
| Views | 9 | 11 | 11 | 11 | 11 | 11 | 11 | +2 | +0.0 | +0.0 |
| ViewRootImpl | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| Activities | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| AppContexts | 5 | 5 | 5 | 5 | 5 | 5 | 5 | +0 | +0.0 | +0.0 |

new: after trim + GC, KB (cycle: 0 5 10 15 20 25 30)
| bucket | c0 | c5 | c10 | c15 | c20 | c25 | c30 | c0→last | last third slope, KB/cycle | whole slope |
|---|---|---|---|---|---|---|---|---|---|---|
| TOTAL PSS | 77150 | 95861 | 96612 | 96517 | 97866 | 98100 | 97421 | +20271 | -44.5 | +475.3 |
| Java Heap | 12340 | 14372 | 14436 | 14536 | 14540 | 14712 | 14716 | +2376 | +17.6 | +56.5 |
| Native Heap | 24884 | 32904 | 33480 | 33848 | 33876 | 33952 | 33812 | +8928 | -6.4 | +209.1 |
| Graphics | 0 | 0 | 0 | 0 | 0 | 0 | 0 | +0 | +0.0 | +0.0 |
| Code | 26024 | 29260 | 29256 | 29272 | 29452 | 29392 | 29408 | +3384 | -4.4 | +75.8 |
| Private Other | 2844 | 7984 | 7996 | 7864 | 8184 | 8180 | 8140 | +5296 | -4.4 | +117.6 |
| System | 9702 | 10541 | 10684 | 10033 | 11058 | 11112 | 10573 | +871 | -48.5 | +29.5 |
| Dalvik Heap alloc | 7422 | 8241 | 8298 | 8467 | 8383 | 8547 | 8555 | +1133 | +17.2 | +29.3 |
| Native Heap alloc | 26890 | 25025 | 18890 | 10717 | 3159 | 0 | 0 | -26890 | -315.9 | -1046.1 |
| Views | 9 | 11 | 11 | 11 | 11 | 11 | 11 | +2 | +0.0 | +0.0 |
| ViewRootImpl | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| Activities | 1 | 1 | 1 | 1 | 1 | 1 | 1 | +0 | +0.0 | +0.0 |
| AppContexts | 5 | 5 | 5 | 5 | 5 | 5 | 5 | +0 | +0.0 | +0.0 |
MEM part: FATAL lines 0
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5444', '23828']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 11
   skipped-frames events=11 max=71 total=568
== Davey: 16
   davey events=16 max=2471ms median=1110ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## wd33

```
part wd33; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 3942 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
launcher: com.google.android.apps.nexuslauncher
## WD1. Both widgets pinned from Settings › Уиджети (the launcher's pin dialog)
PIN main dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Chitalishte Yavorets" text="Chitalishte Yavorets" text="Scan" text="Catalogue" text="2 books to return" text="in 3 days" text="Oct 15" text="Next event" text="Literary evening" text="Octobe
WIDGET 10-main: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [57 223 1023 809]; texts: Читалище|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; e
WIDGET 10-main: bound ids [id=2 ]; providers:  1 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
PIN book dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Book of the day" text="Book of the day" text="Under the Yoke" text="Ivan Vazov" text="Book of the day" content-desc="Book of the day widget" text="3 × 2" content-desc="3 wide by 2 high" tex
WIDGET 20-both: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_]; bounds [57 223 1023 809]; texts: Читалище|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|Кн
WIDGET 20-both: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WARN: tab bar not reached
WIDGET 21-after-catalog: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_]; bounds [57 223 1023 809]; texts: Читалище|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|Кн
WIDGET 21-after-catalog: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
## WD2. App theme → „Тъмна“ (Settings › Тема), system still light: 5 s, 40 s
WARN: tab bar not reached
WARN: settings item =Тема|=Theme not found
WARN: tab bar not reached
WIDGET 30-app-dark-5s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 30-app-dark-5s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 31-app-dark-40s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 31-app-dark-40s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
## WD3. System dark mode on as well: 10 s, 40 s, 90 s
WIDGET 40-sys-dark-10s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 40-sys-dark-10s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 40-sys-dark-40s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 40-sys-dark-40s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 40-sys-dark-90s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 40-sys-dark-90s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
launcher/appwidget log since the pins: 11-14 22:03:13.187  4609  4609 D BoundBrokerSvc: onUnbind: Intent { act=com.google.android.gms.common.BIND_SHARED_PREFS pkg=com.google.android.gms }|11-14 22:04:03.576   485  1471 I AppWidgetServiceImpl: Bound widget 2 to provider ProviderId{user:0, app:10174, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver}}|11-14 22:05:07.910   485  2562 I ExplicitHealthCheckController: No more health check requests, unbinding...|11-14 22:05:07.918   485  2562 I ExplicitHealthCheckController: No more health check requests, unbinding...|11-14 22:05:07.927   485  2562 I ExplicitHealthCheckController: No more health check requests, unbinding...|11-14 22:05:07.970   485  1621 I ExplicitHealthCheckController: No more health check requests, unbinding...|11-14 22:05:07.987   485  1621 I ExplicitHealthCheckController: No more health check requests, unbinding...|11-14 22:05:08.005   485  2025 I ExplicitHealthCheckController: No more health check requests, unbinding...|11-14 22:05:08.013   485  2562 I ExplicitHealthCheckController: No more health check requests, unbinding...|11-14 22:05:08.034   485  2025 I ExplicitHealthCheckController: No more health check requests, unbinding...|
## WD4. Launcher restarted in dark (force-stop of the launcher), then system light and app theme „Както системата“
WIDGET 50-launcher-restarted: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 50-launcher-restarted: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 60-sys-light: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 60-sys-light: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
app theme → =Както системата|=Same as the system|=System: text="ИЗГЛЕД" text="Тема" text="Както системата" text="Размер на текста" 
WIDGET 61-app-system: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 61-app-system: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
  [0] id=2
    host=HostId{user:0, app:10151, hostId:1024, pkg:com.google.android.apps.nexuslauncher}
    provider=ProviderId{user:0, app:10174, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver}}
  [1] id=3
    host=HostId{user:0, app:10151, hostId:1024, pkg:com.google.android.apps.nexuslauncher}
    provider=ProviderId{user:0, app:10174, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver}}
WD part: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 4 in sheets/
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5326', '6954', '7339']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 22
   skipped-frames events=22 max=75 total=988
== Davey: 37
   davey events=37 max=2221ms median=1061ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## wd36x

```
part wd36x; APK: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
base APK: 1.2.37 ebf1720 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b37, throwaway key, versionCode 1990000 size 4591454 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.38 f57962b prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 1990100 size 4641014 bytes
installed:  versionCode=1990100 minSdk=26 targetSdk=37  versionName=1.2.38 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 6557 ms
CARD added 'TEST-12345': text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
launcher: com.google.android.apps.nexuslauncher
## WD1. Both widgets pinned from Settings › Уиджети (the launcher's pin dialog)
PIN main dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Chitalishte Yavorets widget, 4 wide by 2 high Due dates, the next event and a quick scan" content-desc="Chitalishte Yavorets" text="Читалище Яворец" text="Сканирай" text="Каталог" text="2 к
WIDGET 10-main: views from org.chyavorec.app: 1 [ids: widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_logo widget_next_event_label widget_scan widget_title ]; bounds [67 223 1013 811]; texts: Читалище|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|; e
WIDGET 10-main: bound ids [id=2 ]; providers:  1 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
PIN book dialog: fg com.google.android.apps.nexuslauncher/com.android.launcher3.dragndrop.AddItemActivity; text="Chitalishte Yavorets" text="Touch &amp; hold the widget to move it around the home screen" content-desc="Book of the day widget, 3 wide by 2 high A different book from the library catalogue every day, with its cover, title and author" content-desc="Book of the day" text="Книга на деня" text="П
WIDGET 20-both: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_]; bounds [67 223 1013 811]; texts: Читалище|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|Кн
WIDGET 20-both: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WARN: tab bar not reached
WIDGET 21-after-catalog: views from org.chyavorec.app: 1 [ids: widget_book_author widget_book_cover widget_book_title widget_catalog widget_event widget_event2 widget_event2_date widget_event2_title widget_event_column widget_event_date widget_event_title widget_]; bounds [67 223 1013 811]; texts: Читалище|Сканирай|Каталог|Предстоящи събития|80 г. от създаването на ЮНЕСКО|16 ноември 2026|Панайот Пипков · Ден на семейството|21 ноември 2026|Кн
WIDGET 21-after-catalog: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
## WD2. App theme → „Тъмна“ (Settings › Тема), system still light: 5 s, 40 s
WARN: tab bar not reached
WARN: settings item =Тема|=Theme not found
WARN: tab bar not reached
WIDGET 30-app-dark-5s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 30-app-dark-5s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 31-app-dark-40s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 31-app-dark-40s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
## WD3. System dark mode on as well: 10 s, 40 s, 90 s
WIDGET 40-sys-dark-10s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 40-sys-dark-10s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 40-sys-dark-40s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 40-sys-dark-40s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 40-sys-dark-90s: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 40-sys-dark-90s: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
launcher/appwidget log since the pins: 11-14 22:03:15.300   642  2102 I AppWidgetServiceImpl: startListening() 0|11-14 22:03:15.308   642  2102 I AppWidgetServiceImpl: startListening() 0|11-14 22:03:15.380   642   953 I AppWidgetServiceImpl: Received broadcast: android.intent.action.PACKAGE_RESTARTED on user 0|11-14 22:03:15.381   642   953 I AppWidgetServiceImpl: Updating package stopped masked state for uid 10216 package org.chyavorec.app isStopped true|11-14 22:03:15.381   642   953 I AppWidgetServiceImpl: cancelBroadcastsLocked() for Provider{ProviderId{user:0, app:10216, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver}}}|11-14 22:03:15.381   642   953 I AppWidgetServiceImpl: cancelBroadcastsLocked() for Provider{ProviderId{user:0, app:10216, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver}}}|11-14 22:03:19.147   642   953 I AppWidgetServiceImpl: Received broadcast: android.intent.action.PACKAGE_UNSTOPPED on user 0|11-14 22:03:19.147   642   953 I AppWidgetServiceImpl: Updating package stopped masked state for uid 10216 package org.chyavorec.app isStopped false|11-14 22:03:21.693   642  2079 I AppWidgetServiceImpl: getAppWidgetIds() 0|11-14 22:03:21.694   642  2079 I AppWidgetServiceImpl: getAppWidgetIds() 0|
## WD4. Launcher restarted in dark (force-stop of the launcher), then system light and app theme „Както системата“
WIDGET 50-launcher-restarted: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 50-launcher-restarted: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
WIDGET 60-sys-light: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 60-sys-light: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
app theme → =Както системата|=Same as the system|=System: text="ИЗГЛЕД" text="Тема" text="Както системата" text="Размер на текста" 
WIDGET 61-app-system: views from org.chyavorec.app: 0 [ids: ]; bounds []; texts: |; error text: 
WIDGET 61-app-system: bound ids [id=2 id=3 ]; providers:  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver  2 ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver 
  [0] id=2
    host=HostId{user:0, app:10184, hostId:1024, pkg:com.google.android.apps.nexuslauncher}
    provider=ProviderId{user:0, app:10216, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.ChitalishteWidgetReceiver}}
  [1] id=3
    host=HostId{user:0, app:10184, hostId:1024, pkg:com.google.android.apps.nexuslauncher}
    provider=ProviderId{user:0, app:10216, cmp:ComponentInfo{org.chyavorec.app/org.chyavorec.app.widget.BookOfTheDayWidgetReceiver}}
WD part: FATAL(any process)=0 of the app=0 ANR=0
contact sheets: 4 in sheets/
contact sheets: 4 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4154', '5807', '6356']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 38
   skipped-frames events=38 max=237 total=2181
== Davey: 102
   davey events=102 max=3633ms median=930ms
== NativeLink: 3
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

