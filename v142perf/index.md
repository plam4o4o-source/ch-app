# Slow frames after 1.2.40: NEW (7abd0937) against BASE (5a145786), emulator A/B

TEMPORARY A/B on debug/w27-cat: the frames that are still slow after 1.2.40, main-thread CPU (Running time) per
doFrame from a Perfetto trace of each interaction (as in [../v139fix/index.md](../v139fix/index.md)).

- BASE = org.chyavorec.app.b, NEW = org.chyavorec.app; both prodRelease (minified) x86_64, throwaway key,
  speed-profile; each variant in a new process, rounds alternate the order.
- When NEW is built with `w28-exp.patch` (DEBUG ONLY, never released), variants are chosen by launch flags
  (`--es w28 <flags>`); a variant label is `base`, `x` (NEW without flags) or `x-<flag>+<flag>`:
  `bk1` — the first frame after the return from a book composes only the row of that book, the other visible rows are
  spacers of their last height; `bk2` — the other rows without their generated cover (its colour only) in that frame;
  `one` — only the row of the last opened book is a shared element; `nosh` — list covers without the shadow layer;
  `ta` — rows `animateItem` without fade in/out; `tb` — no `animateItem`; `tc` — without fade out; `sug` —
  suggestions without AnimatedVisibility.
- Parts: `bk33`/`bk36x` — Back from a book (rows 1–3, key Back); `ty33`/`ty36x` — typing 3 words in the catalogue
  search (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“ (found by its text after
  slow swipes), a fling (6 up, 4 down) and a slow drag; `tb33`/`tb36x` — entering each main tab (first entry in the
  process, then warm). API 33 unless the part name ends in 36x (API 36).
- Columns: `worst` — the highest CPU of one doFrame in the window; `entry frame` — the first doFrame with ≥ 8 ms,
  `next` — the one after it; frames over 25/30 ms in total; `rows` — catalogue rows composed in the worst frame
  (experimental build only).
- Wall times on this software-GPU emulator are mostly waits for the RenderThread and are not used.
- Run 1 (parts `bk*`, `ty*`, `tb*`): BASE 5a145786 against NEW = 5a145786 + `w28-exp.patch` (the variants).
  Run 2, the final A/B (parts `fbk*`, `fty*`, `ftb*`): BASE 5a145786 against NEW = wave25/perf (no patch, no flags),
  more rounds; recordings at 8 frames/s over the whole transition (the emulator draws a frame in 0.2–0.5 s).
- Host CPUs of the runners differ (a part can be 2× slower than another): compare BASE and NEW within one part.
- Run 3 (parts `gtb*`): the tab entries again with wave25/perf 7abd0937 (Home: the carousel moved into the first frame).
- Run 1 parts `tb33`/`tb36x` stopped at the recording step (a fractional recording length) before the table: their
  numbers are the `M` lines in summary.txt.

## Runs and parts

debug/w27-cat (runs in runs.txt, latest 45058371922 @ 51cf0dc). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU) unless the part name ends in 36x (API 36). Per-part table: <part>/table.md; screen recordings as frames in <part>/frames/<name>/ and contact sheets in <part>/sheets/. The previous A/B: [../v139fix/index.md](../v139fix/index.md).

| part | status |
|---|---|
| bk33 | COMPLETE; part=bk33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| bk36x | COMPLETE; part=bk36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| fbk33 | COMPLETE; part=fbk33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fbk36x | COMPLETE; part=fbk36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| ftb33 | COMPLETE; part=ftb33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| ftb36x | COMPLETE; part=ftb36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| fty33 | COMPLETE; part=fty33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| fty36x | COMPLETE; part=fty36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| gtb33 | COMPLETE; part=gtb33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| gtb36x | COMPLETE; part=gtb36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| tb33 | COMPLETE; part=tb33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| tb36x | COMPLETE; part=tb36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |
| ty33 | COMPLETE; part=ty33 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| ty36x | COMPLETE; part=ty36x target=google_apis api=36 gpu=swangle_indirect emulator-step=success |

```
1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE)
1.2.40 7abd093 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW)
app.apk 4687410 bytes
app-base.apk 4682814 bytes
```

## bk33

```
part bk33; APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 33
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 5673 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~0sj-Jgbsf_7v8Ef-kWCO9g==/org.chyavorec.app-An5_cOQRMTBGkQ2QRBAfqg==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 4373 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~fMNkRMEUbBNGkQwwvei8lg==/org.chyavorec.app.b-KZAX8JYOFe0qUgJZvh9ojQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/LiveWallpapersPicker/LiveWallpapersPicker.apk| x86_64: [status=verify] [reason=prebuilt]|
tabs: catalog [320 2274] home [99 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## B1. Round 1: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r1-1: worst 33.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 33.9 + next 5.1 (from the entry: 33.9, 5.1, 3.7, 4.3); rows composed per frame max 0
M book-back@base@r1-2: worst 44.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 44.1 + next 4.1 (from the entry: 44.1, 4.1, 9.7, 5.0); rows composed per frame max 0
M book-back@base@r1-3: worst 50.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 50.6 + next 6.7 (from the entry: 50.6, 6.7, 6.0, 9.9); rows composed per frame max 0
M book-back@x@r1-1: worst 32.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 32.6 + next 6.6 (from the entry: 32.6, 6.6, 3.1, 6.2); rows composed per frame max 7
M book-back@x@r1-2: worst 64.2 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 64.2 + next 6.7 (from the entry: 64.2, 6.7, 3.2, 6.7); rows composed per frame max 7
M book-back@x@r1-3: worst 33.4 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (6 with >= 3 ms); entry frame 8.1 + next 33.4 (from the entry: 8.1, 33.4, 5.0, 6.7); rows composed per frame max 7
M book-back@x-bk1@r1-1: worst 26.0 ms CPU (rows composed in it 6); frames >16 3, >25 1, >30 0 of 8 (6 with >= 3 ms); entry frame 8.3 + next 20.2 (from the entry: 8.3, 20.2, 6.8, 26.0); rows composed per frame max 6
M book-back@x-bk1@r1-2: worst 31.9 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 21.0 + next 10.3 (from the entry: 21.0, 10.3, 31.9, 4.2); rows composed per frame max 6
M book-back@x-bk1@r1-3: worst 49.6 ms CPU (rows composed in it 1); frames >16 1, >25 1, >30 1 of 9 (8 with >= 3 ms); entry frame 49.6 + next 3.7 (from the entry: 49.6, 3.7, 15.2, 9.8); rows composed per frame max 6
M book-back@x-bk2@r1-1: worst 39.2 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 39.2 + next 5.7 (from the entry: 39.2, 5.7, 15.4, 11.5); rows composed per frame max 7
M book-back@x-bk2@r1-2: worst 39.9 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 9 (8 with >= 3 ms); entry frame 39.9 + next 4.2 (from the entry: 39.9, 4.2, 24.3, 11.2); rows composed per frame max 7
M book-back@x-bk2@r1-3: worst 36.4 ms CPU (rows composed in it 6); frames >16 2, >25 2, >30 1 of 9 (7 with >= 3 ms); entry frame 28.5 + next 9.6 (from the entry: 28.5, 9.6, 36.4, 3.8); rows composed per frame max 7
M book-back@x-one@r1-1: worst 29.0 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 8 (7 with >= 3 ms); entry frame 29.0 + next 4.5 (from the entry: 29.0, 4.5, 6.4, 8.4); rows composed per frame max 7
M book-back@x-one@r1-2: worst 49.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (7 with >= 3 ms); entry frame 49.6 + next 4.9 (from the entry: 49.6, 4.9, 8.7, 7.0); rows composed per frame max 7
M book-back@x-one@r1-3: worst 29.9 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 9 (7 with >= 3 ms); entry frame 29.9 + next 4.0 (from the entry: 29.9, 4.0, 6.5, 6.9); rows composed per frame max 7
M book-back@x-nosh@r1-1: worst 63.9 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (7 with >= 3 ms); entry frame 63.9 + next 6.7 (from the entry: 63.9, 6.7, 3.6, 7.4); rows composed per frame max 7
M book-back@x-nosh@r1-2: worst 31.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 31.6 + next 11.1 (from the entry: 31.6, 11.1, 6.6, 4.0); rows composed per frame max 7
M book-back@x-nosh@r1-3: worst 64.3 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 64.3 + next 5.6 (from the entry: 64.3, 5.6, 3.0, 4.0); rows composed per frame max 7
M book-back@x-bk1+one@r1-1: worst 40.2 ms CPU (rows composed in it 1); frames >16 2, >25 1, >30 1 of 9 (8 with >= 3 ms); entry frame 9.2 + next 40.2 (from the entry: 9.2, 40.2, 8.0, 17.0); rows composed per frame max 6
M book-back@x-bk1+one@r1-2: worst 43.7 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 1 of 9 (8 with >= 3 ms); entry frame 21.8 + next 9.9 (from the entry: 21.8, 9.9, 43.7, 6.8); rows composed per frame max 6
M book-back@x-bk1+one@r1-3: worst 19.8 ms CPU (rows composed in it 1); frames >16 1, >25 0, >30 0 of 9 (7 with >= 3 ms); entry frame 19.8 + next 5.0 (from the entry: 19.8, 5.0, 13.7, 5.2); rows composed per frame max 6
## B2. Round 2: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@x-bk1+one@r2-1: worst 27.1 ms CPU (rows composed in it 1); frames >16 2, >25 2, >30 0 of 9 (7 with >= 3 ms); entry frame 27.1 + next 5.6 (from the entry: 27.1, 5.6, 25.2, 4.6); rows composed per frame max 6
M book-back@x-bk1+one@r2-2: worst 29.9 ms CPU (rows composed in it 1); frames >16 1, >25 1, >30 0 of 9 (6 with >= 3 ms); entry frame 29.9 + next 4.5 (from the entry: 29.9, 4.5, 14.8, 3.7); rows composed per frame max 6
M book-back@x-bk1+one@r2-3: worst 25.2 ms CPU (rows composed in it 1); frames >16 3, >25 1, >30 0 of 9 (8 with >= 3 ms); entry frame 25.2 + next 7.5 (from the entry: 25.2, 7.5, 17.2, 16.6); rows composed per frame max 6
M book-back@x-nosh@r2-1: worst 60.3 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 60.3 + next 4.2 (from the entry: 60.3, 4.2, 4.0, 2.8); rows composed per frame max 7
M book-back@x-nosh@r2-2: worst 48.1 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (7 with >= 3 ms); entry frame 48.1 + next 10.0 (from the entry: 48.1, 10.0, 3.4, 6.3); rows composed per frame max 7
M book-back@x-nosh@r2-3: worst 31.3 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 31.3 + next 9.2 (from the entry: 31.3, 9.2, 4.6, 8.8); rows composed per frame max 7
M book-back@x-one@r2-1: worst 36.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (6 with >= 3 ms); entry frame 36.6 + next 15.1 (from the entry: 36.6, 15.1, 4.5, 3.3); rows composed per frame max 7
M book-back@x-one@r2-2: worst 55.3 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 55.3 + next 4.2 (from the entry: 55.3, 4.2, 7.3, 2.4); rows composed per frame max 7
M book-back@x-one@r2-3: worst 57.5 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 57.5 + next 6.9 (from the entry: 57.5, 6.9, 3.3, 18.8); rows composed per frame max 7
M book-back@x-bk2@r2-1: worst 46.2 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 46.2 + next 5.0 (from the entry: 46.2, 5.0, 24.1, 10.5); rows composed per frame max 7
M book-back@x-bk2@r2-2: worst 49.5 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 49.5 + next 10.2 (from the entry: 49.5, 10.2, 21.5, 4.7); rows composed per frame max 7
M book-back@x-bk2@r2-3: worst 47.3 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 9 (8 with >= 3 ms); entry frame 47.3 + next 5.2 (from the entry: 47.3, 5.2, 19.1, 3.5); rows composed per frame max 7
M book-back@x-bk1@r2-1: worst 32.3 ms CPU (rows composed in it 1); frames >16 2, >25 2, >30 2 of 8 (6 with >= 3 ms); entry frame 32.3 + next 3.8 (from the entry: 32.3, 3.8, 30.9, 5.2); rows composed per frame max 6
M book-back@x-bk1@r2-2: worst 31.7 ms CPU (rows composed in it 6); frames >16 2, >25 2, >30 1 of 9 (8 with >= 3 ms); entry frame 29.5 + next 6.9 (from the entry: 29.5, 6.9, 31.7, 4.2); rows composed per frame max 6
M book-back@x-bk1@r2-3: worst 26.3 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 0 of 9 (6 with >= 3 ms); entry frame 21.5 + next 12.7 (from the entry: 21.5, 12.7, 26.3, 4.7); rows composed per frame max 6
M book-back@x@r2-1: worst 59.4 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 59.4 + next 4.9 (from the entry: 59.4, 4.9, 2.8, 8.0); rows composed per frame max 7
M book-back@x@r2-2: worst 54.3 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (5 with >= 3 ms); entry frame 54.3 + next 4.6 (from the entry: 54.3, 4.6, 2.8, 4.7); rows composed per frame max 7
M book-back@x@r2-3: worst 51.5 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 51.5 + next 4.6 (from the entry: 51.5, 4.6, 3.6, 4.6); rows composed per frame max 7
M book-back@base@r2-1: worst 45.2 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 45.2 + next 20.8 (from the entry: 45.2, 20.8, 4.1, 4.0); rows composed per frame max 0
M book-back@base@r2-2: worst 37.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 8 (7 with >= 3 ms); entry frame 37.6 + next 11.4 (from the entry: 37.6, 11.4, 5.6, 6.8); rows composed per frame max 0
M book-back@base@r2-3: worst 54.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 8 (7 with >= 3 ms); entry frame 54.4 + next 7.5 (from the entry: 54.4, 7.5, 3.7, 7.6); rows composed per frame max 0
## B3. Round 3: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r3-1: worst 52.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 8 (7 with >= 3 ms); entry frame 52.9 + next 6.8 (from the entry: 52.9, 6.8, 5.5, 6.7); rows composed per frame max 0
M book-back@base@r3-2: worst 43.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 8 (7 with >= 3 ms); entry frame 43.3 + next 5.9 (from the entry: 43.3, 5.9, 3.5, 7.2); rows composed per frame max 0
M book-back@base@r3-3: worst 39.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 39.6 + next 4.0 (from the entry: 39.6, 4.0, 5.6, 7.2); rows composed per frame max 0
M book-back@x@r3-1: worst 44.7 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (6 with >= 3 ms); entry frame 44.7 + next 6.1 (from the entry: 44.7, 6.1, 5.7, 3.3); rows composed per frame max 7
M book-back@x@r3-2: worst 53.4 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (5 with >= 3 ms); entry frame 53.4 + next 4.6 (from the entry: 53.4, 4.6, 3.0, 3.7); rows composed per frame max 7
M book-back@x@r3-3: worst 63.8 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (5 with >= 3 ms); entry frame 63.8 + next 4.0 (from the entry: 63.8, 4.0, 3.0, 4.5); rows composed per frame max 7
M book-back@x-bk1@r3-1: worst 31.8 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 23.4 + next 4.5 (from the entry: 23.4, 4.5, 31.8, 13.4); rows composed per frame max 6
M book-back@x-bk1@r3-2: worst 39.8 ms CPU (rows composed in it 1); frames >16 2, >25 2, >30 1 of 9 (7 with >= 3 ms); entry frame 39.8 + next 6.2 (from the entry: 39.8, 6.2, 28.7, 4.4); rows composed per frame max 6
M book-back@x-bk1@r3-3: worst 23.9 ms CPU (rows composed in it 6); frames >16 2, >25 0, >30 0 of 9 (8 with >= 3 ms); entry frame 20.7 + next 4.2 (from the entry: 20.7, 4.2, 23.9, 6.4); rows composed per frame max 6
M book-back@x-bk2@r3-1: worst 46.6 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 46.6 + next 4.2 (from the entry: 46.6, 4.2, 24.9, 10.4); rows composed per frame max 7
M book-back@x-bk2@r3-2: worst 36.6 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 36.6 + next 5.9 (from the entry: 36.6, 5.9, 22.4, 7.2); rows composed per frame max 7
M book-back@x-bk2@r3-3: worst 27.4 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 9 (7 with >= 3 ms); entry frame 27.4 + next 8.0 (from the entry: 27.4, 8.0, 14.9, 4.7); rows composed per frame max 7
M book-back@x-one@r3-1: worst 36.2 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 8 (6 with >= 3 ms); entry frame 36.2 + next 4.2 (from the entry: 36.2, 4.2, 6.5, 5.8); rows composed per frame max 7
M book-back@x-one@r3-2: worst 44.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (8 with >= 3 ms); entry frame 44.6 + next 5.6 (from the entry: 44.6, 5.6, 3.4, 3.1); rows composed per frame max 7
M book-back@x-one@r3-3: worst 46.7 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 46.7 + next 5.0 (from the entry: 46.7, 5.0, 4.6, 6.7); rows composed per frame max 7
M book-back@x-nosh@r3-1: worst 55.1 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 55.1 + next 7.4 (from the entry: 55.1, 7.4, 3.5, 3.3); rows composed per frame max 7
M book-back@x-nosh@r3-2: worst 39.1 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 39.1 + next 4.1 (from the entry: 39.1, 4.1, 7.6, 4.6); rows composed per frame max 7
M book-back@x-nosh@r3-3: worst 44.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 44.6 + next 4.3 (from the entry: 44.6, 4.3, 7.2, 7.2); rows composed per frame max 7
M book-back@x-bk1+one@r3-1: worst 41.1 ms CPU (rows composed in it 1); frames >16 2, >25 2, >30 1 of 9 (7 with >= 3 ms); entry frame 41.1 + next 3.1 (from the entry: 41.1, 3.1, 25.5, 3.1); rows composed per frame max 6
M book-back@x-bk1+one@r3-2: worst 27.4 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 0 of 10 (7 with >= 3 ms); entry frame 20.4 + next 5.6 (from the entry: 20.4, 5.6, 27.4, 4.3); rows composed per frame max 6
M book-back@x-bk1+one@r3-3: worst 34.5 ms CPU (rows composed in it 1); frames >16 2, >25 2, >30 1 of 9 (7 with >= 3 ms); entry frame 34.5 + next 5.8 (from the entry: 34.5, 5.8, 25.3, 10.4); rows composed per frame max 6
## BV. Visual: open a book and Back, screen recording at 25 frames/s
REC open-base (emu): marker 1547 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-base (emu): marker 1552 ms after the recording start; 61 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS base: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x (emu): marker 1549 ms after the recording start; 61 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x (emu): marker 1567 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-bk1 (emu): marker 1552 ms after the recording start; 61 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-bk1 (emu): marker 1552 ms after the recording start; 61 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-bk1: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-bk2 (emu): marker 1554 ms after the recording start; 59 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-bk2 (emu): marker 1554 ms after the recording start; 60 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-bk2: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-one (emu): marker 1548 ms after the recording start; 61 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-one (emu): marker 1588 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-one: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-nosh (emu): marker 1551 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-nosh (emu): marker 1560 ms after the recording start; 61 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-nosh: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-bk1+one (emu): marker 1549 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-bk1+one (emu): marker 1548 ms after the recording start; 59 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-bk1+one: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
book-back | base | 9 | 44.1 / 44.6 / 54.4 | 44.1 | 6.7 | 9 | 9 | 0 | 83
book-back | x | 9 | 53.4 / 50.8 / 64.2 | 53.4 | 4.9 | 9 | 9 | 7 | 88
book-back | x-bk1 | 9 | 31.8 / 32.6 / 49.6 | 23.4 | 6.2 | 11 | 7 | 6 | 93
book-back | x-bk1+one | 9 | 29.9 / 32.1 / 43.7 | 25.2 | 5.6 | 11 | 4 | 1 | 83
book-back | x-bk2 | 9 | 39.9 / 41.0 / 49.5 | 39.9 | 5.7 | 10 | 8 | 7 | 100
book-back | x-nosh | 9 | 48.1 / 48.7 / 64.3 | 48.1 | 6.7 | 9 | 9 | 7 | 86
book-back | x-one | 9 | 44.6 / 42.8 / 57.5 | 44.6 | 4.9 | 9 | 7 | 7 | 79
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 61
contact sheets: 69 in sheets/
contact sheets: 69 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4982', '8221', '8752', '9152', '9762', '10327', '10734', '11170', '11544', '11931', '12354', '12734', '13132', '13497', '14642', '15018', '15381', '15745', '16115', '16499', '17186', '17415', '17632', '17861', '18087', '18301']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 125
   skipped-frames events=125 max=172 total=8531
== Davey: 280
   davey events=280 max=4280ms median=926ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## bk36x

```
part bk36x; APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 36
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 4416 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~Ai1GUJUb3IpNOwYDLDU9Mg==/org.chyavorec.app-tY5QhDoae3nEJg2MIRaBeA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~YszGhreOdAVqZbisYp7w7w==/org.chyavorec.app.b-dpob7hlcdasgE0u6We7Psw==/base.apk| x86_64: [status=verify] [reason=install] [primary-abi]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 2854 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~YszGhreOdAVqZbisYp7w7w==/org.chyavorec.app.b-dpob7hlcdasgE0u6We7Psw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]|
tabs: catalog [320 2274] home [100 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## B1. Round 1: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r1-1: worst 17.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (10 with >= 3 ms); entry frame 17.4 + next 3.3 (from the entry: 17.4, 3.3, 3.3, 2.6); rows composed per frame max 0
M book-back@base@r1-2: worst 26.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 17 (9 with >= 3 ms); entry frame 26.5 + next 3.8 (from the entry: 26.5, 3.8, 3.8, 4.7); rows composed per frame max 0
M book-back@base@r1-3: worst 14.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 14.0 + next 2.2 (from the entry: 14.0, 2.2, 3.0, 2.9); rows composed per frame max 0
M book-back@x@r1-1: worst 23.7 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (8 with >= 3 ms); entry frame 23.7 + next 3.5 (from the entry: 23.7, 3.5, 3.4, 1.3); rows composed per frame max 7
M book-back@x@r1-2: worst 26.7 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 16 (5 with >= 3 ms); entry frame 26.7 + next 3.9 (from the entry: 26.7, 3.9, 3.8, 1.9); rows composed per frame max 7
M book-back@x@r1-3: worst 21.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (10 with >= 3 ms); entry frame 21.8 + next 3.6 (from the entry: 21.8, 3.6, 3.5, 1.4); rows composed per frame max 7
M book-back@x-bk1@r1-1: worst 11.3 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 17 (9 with >= 3 ms); entry frame 11.3 + next 3.0 (from the entry: 11.3, 3.0, 3.0, 6.7); rows composed per frame max 6
M book-back@x-bk1@r1-2: worst 12.6 ms CPU (rows composed in it 6); frames >16 0, >25 0, >30 0 of 16 (7 with >= 3 ms); entry frame 11.0 + next 2.7 (from the entry: 11.0, 2.7, 12.6, 5.0); rows composed per frame max 6
M book-back@x-bk1@r1-3: worst 15.9 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 15.9 + next 2.8 (from the entry: 15.9, 2.8, 9.0, 8.9); rows composed per frame max 6
M book-back@x-bk2@r1-1: worst 25.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 17 (9 with >= 3 ms); entry frame 25.6 + next 2.7 (from the entry: 25.6, 2.7, 2.6, 5.3); rows composed per frame max 7
M book-back@x-bk2@r1-2: worst 13.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (10 with >= 3 ms); entry frame 13.4 + next 4.5 (from the entry: 13.4, 4.5, 4.4, 9.6); rows composed per frame max 7
M book-back@x-bk2@r1-3: worst 20.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 19 (10 with >= 3 ms); entry frame 20.3 + next 3.3 (from the entry: 20.3, 3.3, 6.3, 6.3); rows composed per frame max 7
M book-back@x-one@r1-1: worst 23.0 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (7 with >= 3 ms); entry frame 23.0 + next 2.6 (from the entry: 23.0, 2.6, 2.6, 1.5); rows composed per frame max 7
M book-back@x-one@r1-2: worst 30.0 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 17 (7 with >= 3 ms); entry frame 30.0 + next 4.2 (from the entry: 30.0, 4.2, 4.1, 1.6); rows composed per frame max 7
M book-back@x-one@r1-3: worst 16.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 16.4 + next 5.3 (from the entry: 16.4, 5.3, 5.3, 2.4); rows composed per frame max 7
M book-back@x-nosh@r1-1: worst 22.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 22.4 + next 3.6 (from the entry: 22.4, 3.6, 3.5, 1.2); rows composed per frame max 7
M book-back@x-nosh@r1-2: worst 17.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (9 with >= 3 ms); entry frame 17.4 + next 3.9 (from the entry: 17.4, 3.9, 3.9, 1.7); rows composed per frame max 7
M book-back@x-nosh@r1-3: worst 15.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 15 (10 with >= 3 ms); entry frame 15.2 + next 3.0 (from the entry: 15.2, 3.0, 5.4, 2.5); rows composed per frame max 7
M book-back@x-bk1+one@r1-1: worst 21.8 ms CPU (rows composed in it 1); frames >16 1, >25 0, >30 0 of 17 (11 with >= 3 ms); entry frame 21.8 + next 3.6 (from the entry: 21.8, 3.6, 3.5, 5.8); rows composed per frame max 6
M book-back@x-bk1+one@r1-2: worst 16.4 ms CPU (rows composed in it 1); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 16.4 + next 3.2 (from the entry: 16.4, 3.2, 8.7, 8.6); rows composed per frame max 6
M book-back@x-bk1+one@r1-3: worst 14.1 ms CPU (rows composed in it 6); frames >16 0, >25 0, >30 0 of 19 (4 with >= 3 ms); entry frame 10.5 + next 3.0 (from the entry: 10.5, 3.0, 2.9, 14.1); rows composed per frame max 6
## B2. Round 2: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@x-bk1+one@r2-1: worst 16.7 ms CPU (rows composed in it 1); frames >16 1, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 16.7 + next 3.6 (from the entry: 16.7, 3.6, 3.6, 5.5); rows composed per frame max 6
M book-back@x-bk1+one@r2-2: worst 17.0 ms CPU (rows composed in it 1); frames >16 1, >25 0, >30 0 of 17 (10 with >= 3 ms); entry frame 17.0 + next 3.6 (from the entry: 17.0, 3.6, 3.5, 5.2); rows composed per frame max 6
M book-back@x-bk1+one@r2-3: worst 10.3 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 19 (4 with >= 3 ms); entry frame 10.3 + next 2.4 (from the entry: 10.3, 2.4, 7.8, 7.7); rows composed per frame max 6
M book-back@x-nosh@r2-1: worst 21.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (9 with >= 3 ms); entry frame 21.3 + next 3.5 (from the entry: 21.3, 3.5, 3.5, 4.3); rows composed per frame max 7
M book-back@x-nosh@r2-2: worst 20.9 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 20.9 + next 4.2 (from the entry: 20.9, 4.2, 4.1, 1.7); rows composed per frame max 7
M book-back@x-nosh@r2-3: worst 28.7 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 17 (5 with >= 3 ms); entry frame 28.7 + next 8.3 (from the entry: 28.7, 8.3, 8.2, 2.3); rows composed per frame max 7
M book-back@x-one@r2-1: worst 30.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 17 (5 with >= 3 ms); entry frame 30.6 + next 3.5 (from the entry: 30.6, 3.5, 3.4, 1.6); rows composed per frame max 7
M book-back@x-one@r2-2: worst 20.7 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 20.7 + next 3.9 (from the entry: 20.7, 3.9, 3.8, 1.8); rows composed per frame max 7
M book-back@x-one@r2-3: worst 17.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 17.5 + next 3.8 (from the entry: 17.5, 3.8, 3.8, 1.7); rows composed per frame max 7
M book-back@x-bk2@r2-1: worst 16.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 16.0 + next 3.0 (from the entry: 16.0, 3.0, 2.9, 5.3); rows composed per frame max 7
M book-back@x-bk2@r2-2: worst 18.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (10 with >= 3 ms); entry frame 18.8 + next 2.7 (from the entry: 18.8, 2.7, 6.8, 6.8); rows composed per frame max 7
M book-back@x-bk2@r2-3: worst 37.8 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 19 (9 with >= 3 ms); entry frame 37.8 + next 3.3 (from the entry: 37.8, 3.3, 3.3, 11.2); rows composed per frame max 7
M book-back@x-bk1@r2-1: worst 14.9 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 14.9 + next 3.3 (from the entry: 14.9, 3.3, 3.3, 14.3); rows composed per frame max 6
M book-back@x-bk1@r2-2: worst 16.1 ms CPU (rows composed in it 1); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 16.1 + next 3.5 (from the entry: 16.1, 3.5, 3.5, 12.7); rows composed per frame max 6
M book-back@x-bk1@r2-3: worst 18.0 ms CPU (rows composed in it 1); frames >16 1, >25 0, >30 0 of 19 (13 with >= 3 ms); entry frame 18.0 + next 3.9 (from the entry: 18.0, 3.9, 3.8, 7.7); rows composed per frame max 6
M book-back@x@r2-1: worst 14.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 14.7 + next 4.0 (from the entry: 14.7, 4.0, 3.9, 2.2); rows composed per frame max 7
M book-back@x@r2-2: worst 17.9 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 18 (7 with >= 3 ms); entry frame 17.9 + next 3.1 (from the entry: 17.9, 3.1, 3.1, 2.3); rows composed per frame max 7
M book-back@x@r2-3: worst 18.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 19 (9 with >= 3 ms); entry frame 18.5 + next 7.9 (from the entry: 18.5, 7.9, 7.9, 1.6); rows composed per frame max 7
M book-back@base@r2-1: worst 24.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (10 with >= 3 ms); entry frame 24.4 + next 3.8 (from the entry: 24.4, 3.8, 3.8, 2.1); rows composed per frame max 0
M book-back@base@r2-2: worst 24.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 24.9 + next 6.6 (from the entry: 24.9, 6.6, 2.7, 2.6); rows composed per frame max 0
M book-back@base@r2-3: worst 23.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 23.8 + next 3.6 (from the entry: 23.8, 3.6, 3.5, 2.6); rows composed per frame max 0
## B3. Round 3: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r3-1: worst 19.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 19.5 + next 3.5 (from the entry: 19.5, 3.5, 3.5, 1.1); rows composed per frame max 0
M book-back@base@r3-2: worst 18.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 18.1 + next 3.6 (from the entry: 18.1, 3.6, 3.6, 2.1); rows composed per frame max 0
M book-back@base@r3-3: worst 24.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 24.5 + next 5.4 (from the entry: 24.5, 5.4, 2.3, 2.2); rows composed per frame max 0
M book-back@x@r3-1: worst 16.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (8 with >= 3 ms); entry frame 16.3 + next 4.7 (from the entry: 16.3, 4.7, 4.6, 2.0); rows composed per frame max 7
M book-back@x@r3-2: worst 29.0 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 17 (10 with >= 3 ms); entry frame 29.0 + next 3.2 (from the entry: 29.0, 3.2, 3.1, 2.8); rows composed per frame max 7
M book-back@x@r3-3: worst 23.2 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 23.2 + next 4.6 (from the entry: 23.2, 4.6, 4.0, 4.0); rows composed per frame max 7
M book-back@x-bk1@r3-1: worst 12.7 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 12.7 + next 3.1 (from the entry: 12.7, 3.1, 8.8, 8.8); rows composed per frame max 6
M book-back@x-bk1@r3-2: worst 13.2 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 19 (8 with >= 3 ms); entry frame 13.2 + next 3.3 (from the entry: 13.2, 3.3, 3.2, 6.2); rows composed per frame max 6
M book-back@x-bk1@r3-3: worst 10.0 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 19 (6 with >= 3 ms); entry frame 10.0 + next 6.4 (from the entry: 10.0, 6.4, 8.7, 8.7); rows composed per frame max 6
M book-back@x-bk2@r3-1: worst 13.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (8 with >= 3 ms); entry frame 13.7 + next 3.0 (from the entry: 13.7, 3.0, 3.0, 6.6); rows composed per frame max 7
M book-back@x-bk2@r3-2: worst 16.6 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (12 with >= 3 ms); entry frame 16.6 + next 3.6 (from the entry: 16.6, 3.6, 3.6, 8.1); rows composed per frame max 7
M book-back@x-bk2@r3-3: worst 16.9 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 19 (12 with >= 3 ms); entry frame 16.9 + next 6.3 (from the entry: 16.9, 6.3, 5.5, 5.5); rows composed per frame max 7
M book-back@x-one@r3-1: worst 25.0 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 25.0 + next 2.5 (from the entry: 25.0, 2.5, 2.5, 3.5); rows composed per frame max 7
M book-back@x-one@r3-2: worst 19.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 19.4 + next 3.6 (from the entry: 19.4, 3.6, 3.5, 5.5); rows composed per frame max 7
M book-back@x-one@r3-3: worst 14.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 14.4 + next 2.3 (from the entry: 14.4, 2.3, 3.0, 2.9); rows composed per frame max 7
M book-back@x-nosh@r3-1: worst 26.0 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 15 (8 with >= 3 ms); entry frame 26.0 + next 3.6 (from the entry: 26.0, 3.6, 3.6, 1.8); rows composed per frame max 7
M book-back@x-nosh@r3-2: worst 18.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 18.8 + next 7.7 (from the entry: 18.8, 7.7, 2.3, 2.3); rows composed per frame max 7
M book-back@x-nosh@r3-3: worst 24.2 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 19 (10 with >= 3 ms); entry frame 24.2 + next 5.9 (from the entry: 24.2, 5.9, 5.8, 1.7); rows composed per frame max 7
M book-back@x-bk1+one@r3-1: worst 13.9 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 16 (8 with >= 3 ms); entry frame 13.9 + next 5.2 (from the entry: 13.9, 5.2, 11.9, 11.8); rows composed per frame max 6
M book-back@x-bk1+one@r3-2: worst 14.1 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 15 (7 with >= 3 ms); entry frame 14.1 + next 2.2 (from the entry: 14.1, 2.2, 12.0, 3.0); rows composed per frame max 6
M book-back@x-bk1+one@r3-3: worst 13.4 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 18 (4 with >= 3 ms); entry frame 13.4 + next 2.6 (from the entry: 13.4, 2.6, 6.9, 2.5); rows composed per frame max 6
## BV. Visual: open a book and Back, screen recording at 25 frames/s
REC open-base (emu): marker 1535 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-base (emu): marker 1569 ms after the recording start; 64 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS base: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x (emu): marker 1537 ms after the recording start; 64 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x (emu): marker 1541 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-bk1 (emu): marker 1542 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-bk1 (emu): marker 1542 ms after the recording start; 64 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-bk1: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-bk2 (emu): marker 1538 ms after the recording start; 63 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-bk2 (emu): marker 1532 ms after the recording start; 64 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-bk2: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-one (emu): marker 1533 ms after the recording start; 64 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-one (emu): marker 1531 ms after the recording start; 63 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-one: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-nosh (emu): marker 1531 ms after the recording start; 61 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-nosh (emu): marker 1536 ms after the recording start; 64 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-nosh: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x-bk1+one (emu): marker 1532 ms after the recording start; 62 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x-bk1+one (emu): marker 1535 ms after the recording start; 64 frames at 25/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x-bk1+one: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
book-back | base | 9 | 23.8 / 21.5 / 26.5 | 23.8 | 3.6 | 1 | 0 | 0 | 63
book-back | x | 9 | 21.8 / 21.3 / 29.0 | 21.8 | 3.9 | 2 | 0 | 7 | 68
book-back | x-bk1 | 9 | 13.2 / 13.9 / 18.0 | 13.2 | 3.3 | 0 | 0 | 1 | 68
book-back | x-bk1+one | 9 | 14.1 / 15.3 / 21.8 | 14.1 | 3.2 | 0 | 0 | 1 | 62
book-back | x-bk2 | 9 | 16.9 / 19.9 / 37.8 | 16.9 | 3.3 | 2 | 1 | 7 | 72
book-back | x-nosh | 9 | 21.3 / 21.7 / 28.7 | 21.3 | 3.9 | 2 | 0 | 7 | 72
book-back | x-one | 9 | 20.7 / 21.9 / 30.6 | 20.7 | 3.6 | 2 | 1 | 7 | 66
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 9
contact sheets: 72 in sheets/
contact sheets: 72 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4253', '5885', '6481', '6931', '7312', '7678', '8103', '8470', '8852', '9216', '9581', '9960', '10338', '10725', '11863', '12237', '12598', '12974', '13343', '13735', '14362', '14594', '14816', '15052', '15296', '15537']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 42
   skipped-frames events=42 max=85 total=2385
== Davey: 80
   davey events=80 max=2211ms median=1089ms
== NativeLink: 26
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## fbk33

```
part fbk33; APK: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 33
host cpu:  AMD EPYC 7763 64-Core Processor
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 3807 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~Fnz-P9lAA_kIvnUh7bzHGw==/org.chyavorec.app-mgl201Utrgn3WgYckZQu4Q==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 3639 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~no1RTp2In0KS60q_6Bc_JA==/org.chyavorec.app.b-xiSxvPY-T3p4FVhNrxI4Sw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/LiveWallpapersPicker/LiveWallpapersPicker.apk| x86_64: [status=verify] [reason=prebuilt]|
tabs: catalog [320 2274] home [99 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## B1. Round 1: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r1-1: worst 40.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 8 (5 with >= 3 ms); entry frame 40.2 + next 4.8 (from the entry: 40.2, 4.8, 5.9, 2.4); rows composed per frame max 0
M book-back@base@r1-2: worst 37.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 10 (6 with >= 3 ms); entry frame 37.9 + next 4.2 (from the entry: 37.9, 4.2, 3.0, 6.2); rows composed per frame max 0
M book-back@base@r1-3: worst 46.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (5 with >= 3 ms); entry frame 46.3 + next 5.8 (from the entry: 46.3, 5.8, 2.5, 3.0); rows composed per frame max 0
M book-back@x@r1-1: worst 26.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (6 with >= 3 ms); entry frame 26.0 + next 13.8 (from the entry: 26.0, 13.8, 8.0, 3.6); rows composed per frame max 0
M book-back@x@r1-2: worst 28.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (8 with >= 3 ms); entry frame 28.0 + next 11.5 (from the entry: 28.0, 11.5, 15.9, 3.8); rows composed per frame max 0
M book-back@x@r1-3: worst 36.9 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 36.9 + next 10.6 (from the entry: 36.9, 10.6, 16.1, 4.2); rows composed per frame max 0
## B2. Round 2: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@x@r2-1: worst 28.3 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 9 (8 with >= 3 ms); entry frame 28.3 + next 10.2 (from the entry: 28.3, 10.2, 27.6, 6.3); rows composed per frame max 0
M book-back@x@r2-2: worst 34.2 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 34.2 + next 13.3 (from the entry: 34.2, 13.3, 17.3, 4.8); rows composed per frame max 0
M book-back@x@r2-3: worst 27.4 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 9 (8 with >= 3 ms); entry frame 27.4 + next 12.0 (from the entry: 27.4, 12.0, 19.9, 6.8); rows composed per frame max 0
M book-back@base@r2-1: worst 49.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (5 with >= 3 ms); entry frame 49.2 + next 10.0 (from the entry: 49.2, 10.0, 1.9, 2.3); rows composed per frame max 0
M book-back@base@r2-2: worst 41.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 41.5 + next 7.6 (from the entry: 41.5, 7.6, 2.6, 6.1); rows composed per frame max 0
M book-back@base@r2-3: worst 67.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 67.9 + next 4.7 (from the entry: 67.9, 4.7, 2.3, 6.1); rows composed per frame max 0
## B3. Round 3: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r3-1: worst 52.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (5 with >= 3 ms); entry frame 52.4 + next 3.2 (from the entry: 52.4, 3.2, 2.7, 5.8); rows composed per frame max 0
M book-back@base@r3-2: worst 37.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 37.7 + next 4.3 (from the entry: 37.7, 4.3, 8.1, 3.2); rows composed per frame max 0
M book-back@base@r3-3: worst 37.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 37.1 + next 3.4 (from the entry: 37.1, 3.4, 4.0, 2.9); rows composed per frame max 0
M book-back@x@r3-1: worst 37.5 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 37.5 + next 9.7 (from the entry: 37.5, 9.7, 20.4, 6.1); rows composed per frame max 0
M book-back@x@r3-2: worst 29.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (6 with >= 3 ms); entry frame 29.8 + next 12.2 (from the entry: 29.8, 12.2, 9.0, 7.7); rows composed per frame max 0
M book-back@x@r3-3: worst 25.2 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 9 (7 with >= 3 ms); entry frame 25.2 + next 9.0 (from the entry: 25.2, 9.0, 18.7, 9.1); rows composed per frame max 0
## B4. Round 4: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@x@r4-1: worst 31.7 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 31.7 + next 9.4 (from the entry: 31.7, 9.4, 21.3, 3.9); rows composed per frame max 0
M book-back@x@r4-2: worst 25.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 9 (7 with >= 3 ms); entry frame 25.0 + next 11.6 (from the entry: 25.0, 11.6, 8.2, 3.1); rows composed per frame max 0
M book-back@x@r4-3: worst 29.1 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 9 (7 with >= 3 ms); entry frame 29.1 + next 23.2 (from the entry: 29.1, 23.2, 11.1, 8.3); rows composed per frame max 0
M book-back@base@r4-1: worst 45.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 45.9 + next 4.1 (from the entry: 45.9, 4.1, 2.7, 6.0); rows composed per frame max 0
M book-back@base@r4-2: worst 40.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (7 with >= 3 ms); entry frame 40.2 + next 4.0 (from the entry: 40.2, 4.0, 6.3, 6.0); rows composed per frame max 0
M book-back@base@r4-3: worst 28.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (5 with >= 3 ms); entry frame 28.3 + next 5.3 (from the entry: 28.3, 5.3, 2.4, 5.1); rows composed per frame max 0
## B5. Round 5: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r5-1: worst 55.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (4 with >= 3 ms); entry frame 55.6 + next 3.4 (from the entry: 55.6, 3.4, 1.8, 2.8); rows composed per frame max 0
M book-back@base@r5-2: worst 36.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (6 with >= 3 ms); entry frame 36.0 + next 3.3 (from the entry: 36.0, 3.3, 4.2, 8.9); rows composed per frame max 0
M book-back@base@r5-3: worst 29.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (7 with >= 3 ms); entry frame 29.3 + next 5.0 (from the entry: 29.3, 5.0, 5.9, 5.9); rows composed per frame max 0
M book-back@x@r5-1: worst 33.4 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 9 (5 with >= 3 ms); entry frame 33.4 + next 9.5 (from the entry: 33.4, 9.5, 20.1, 6.3); rows composed per frame max 0
M book-back@x@r5-2: worst 16.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 9 (7 with >= 3 ms); entry frame 16.4 + next 10.2 (from the entry: 16.4, 10.2, 14.4, 3.2); rows composed per frame max 0
M book-back@x@r5-3: worst 28.1 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 9 (8 with >= 3 ms); entry frame 28.1 + next 9.9 (from the entry: 28.1, 9.9, 17.2, 6.0); rows composed per frame max 0
## BV. Visual: open a book and Back, screen recording at 8 frames/s
REC open-base (emu): marker 1541 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-base (emu): marker 1545 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS base: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x (emu): marker 1547 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x (emu): marker 1551 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
book-back | base | 15 | 40.2 / 43.0 / 67.9 | 40.2 | 4.3 | 15 | 13 | 0 | 72
book-back | x | 15 | 28.3 / 29.1 / 37.5 | 28.3 | 10.6 | 14 | 5 | 0 | 81
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 28
contact sheets: 17 in sheets/
contact sheets: 17 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5354', '8146', '8721', '9105', '10642', '11080', '12293', '12933']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 37
   skipped-frames events=37 max=141 total=2343
== Davey: 63
   davey events=63 max=3397ms median=1195ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## fbk36x

```
part fbk36x; APK: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15982 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 36
host cpu:  INTEL(R) XEON(R) PLATINUM 8573C
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: NO catalogue results
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~HHdPT0nNgqXPqDI1QGeXjA==/org.chyavorec.app-UyQWfS-fUE1anBMTrs6vyw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~bJVo7ee8HEkmvnoU_WP9Gg==/org.chyavorec.app.b-evs3e9hquC8ghZGvrrpNnA==/base.apk| x86_64: [status=verify] [reason=install] [primary-abi]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 3356 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~bJVo7ee8HEkmvnoU_WP9Gg==/org.chyavorec.app.b-evs3e9hquC8ghZGvrrpNnA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]|
tabs: catalog [320 2274] home [100 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## B1. Round 1: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r1-1: worst 47.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (9 with >= 3 ms); entry frame 47.8 + next 7.7 (from the entry: 47.8, 7.7, 7.4, 9.5); rows composed per frame max 0
M book-back@base@r1-2: worst 61.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 17 (14 with >= 3 ms); entry frame 61.8 + next 4.6 (from the entry: 61.8, 4.6, 4.6, 4.4); rows composed per frame max 0
M book-back@base@r1-3: worst 55.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 55.4 + next 5.0 (from the entry: 55.4, 5.0, 4.9, 7.2); rows composed per frame max 0
M book-back@x@r1-1: worst 35.1 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 15 (11 with >= 3 ms); entry frame 35.1 + next 13.9 (from the entry: 35.1, 13.9, 13.7, 22.0); rows composed per frame max 0
M book-back@x@r1-2: worst 35.2 ms CPU (rows composed in it 0); frames >16 3, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 35.2 + next 20.9 (from the entry: 35.2, 20.9, 20.9, 9.5); rows composed per frame max 0
M book-back@x@r1-3: worst 20.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 15 (11 with >= 3 ms); entry frame 20.0 + next 9.5 (from the entry: 20.0, 9.5, 11.3, 11.2); rows composed per frame max 0
## B2. Round 2: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@x@r2-1: worst 26.4 ms CPU (rows composed in it 0); frames >16 5, >25 1, >30 0 of 16 (12 with >= 3 ms); entry frame 26.4 + next 21.3 (from the entry: 26.4, 21.3, 21.1, 21.9); rows composed per frame max 0
M book-back@x@r2-2: worst 36.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 17 (13 with >= 3 ms); entry frame 36.4 + next 13.2 (from the entry: 36.4, 13.2, 13.1, 8.1); rows composed per frame max 0
M book-back@x@r2-3: worst 31.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 31.7 + next 10.9 (from the entry: 31.7, 10.9, 10.9, 10.6); rows composed per frame max 0
M book-back@base@r2-1: worst 45.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (10 with >= 3 ms); entry frame 45.2 + next 6.4 (from the entry: 45.2, 6.4, 6.3, 2.9); rows composed per frame max 0
M book-back@base@r2-2: worst 47.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (14 with >= 3 ms); entry frame 47.2 + next 4.5 (from the entry: 47.2, 4.5, 4.5, 4.2); rows composed per frame max 0
M book-back@base@r2-3: worst 29.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 16 (10 with >= 3 ms); entry frame 29.4 + next 4.2 (from the entry: 29.4, 4.2, 4.1, 6.7); rows composed per frame max 0
## B3. Round 3: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r3-1: worst 56.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (9 with >= 3 ms); entry frame 56.8 + next 5.3 (from the entry: 56.8, 5.3, 5.2, 3.1); rows composed per frame max 0
M book-back@base@r3-2: worst 49.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 17 (13 with >= 3 ms); entry frame 49.2 + next 5.9 (from the entry: 49.2, 5.9, 5.7, 8.5); rows composed per frame max 0
M book-back@base@r3-3: worst 57.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (13 with >= 3 ms); entry frame 57.8 + next 12.9 (from the entry: 57.8, 12.9, 12.8, 3.4); rows composed per frame max 0
M book-back@x@r3-1: worst 25.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 17 (11 with >= 3 ms); entry frame 25.2 + next 10.4 (from the entry: 25.2, 10.4, 10.3, 9.4); rows composed per frame max 0
M book-back@x@r3-2: worst 32.9 ms CPU (rows composed in it 0); frames >16 3, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 32.9 + next 21.6 (from the entry: 32.9, 21.6, 21.5, 9.3); rows composed per frame max 0
M book-back@x@r3-3: worst 23.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 15 (11 with >= 3 ms); entry frame 23.1 + next 8.8 (from the entry: 23.1, 8.8, 11.7, 11.6); rows composed per frame max 0
## B4. Round 4: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@x@r4-1: worst 23.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (12 with >= 3 ms); entry frame 23.7 + next 11.5 (from the entry: 23.7, 11.5, 11.4, 11.7); rows composed per frame max 0
M book-back@x@r4-2: worst 28.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 15 (11 with >= 3 ms); entry frame 28.4 + next 13.0 (from the entry: 28.4, 13.0, 13.0, 8.3); rows composed per frame max 0
M book-back@x@r4-3: worst 49.3 ms CPU (rows composed in it 0); frames >16 3, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 49.3 + next 16.5 (from the entry: 49.3, 16.5, 16.5, 13.2); rows composed per frame max 0
M book-back@base@r4-1: worst 45.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (11 with >= 3 ms); entry frame 45.9 + next 5.0 (from the entry: 45.9, 5.0, 4.9, 9.3); rows composed per frame max 0
M book-back@base@r4-2: worst 49.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 49.7 + next 4.8 (from the entry: 49.7, 4.8, 4.7, 3.6); rows composed per frame max 0
M book-back@base@r4-3: worst 32.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 32.8 + next 10.8 (from the entry: 32.8, 10.8, 10.8, 3.5); rows composed per frame max 0
## B5. Round 5: per variant a new process, Каталог, then rows 1-3: open, Back (measured)
M book-back@base@r5-1: worst 43.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (11 with >= 3 ms); entry frame 43.9 + next 6.2 (from the entry: 43.9, 6.2, 6.0, 3.1); rows composed per frame max 0
M book-back@base@r5-2: worst 42.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (11 with >= 3 ms); entry frame 42.5 + next 9.9 (from the entry: 42.5, 9.9, 9.7, 3.7); rows composed per frame max 0
M book-back@base@r5-3: worst 35.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (10 with >= 3 ms); entry frame 35.3 + next 4.5 (from the entry: 35.3, 4.5, 4.4, 11.8); rows composed per frame max 0
M book-back@x@r5-1: worst 32.7 ms CPU (rows composed in it 0); frames >16 5, >25 1, >30 1 of 16 (14 with >= 3 ms); entry frame 32.7 + next 17.3 (from the entry: 32.7, 17.3, 17.3, 23.8); rows composed per frame max 0
M book-back@x@r5-2: worst 32.1 ms CPU (rows composed in it 0); frames >16 3, >25 3, >30 1 of 16 (14 with >= 3 ms); entry frame 32.1 + next 28.9 (from the entry: 32.1, 28.9, 28.9, 9.0); rows composed per frame max 0
M book-back@x@r5-3: worst 31.4 ms CPU (rows composed in it 0); frames >16 5, >25 1, >30 1 of 16 (12 with >= 3 ms); entry frame 31.4 + next 18.5 (from the entry: 31.4, 18.5, 18.4, 17.9); rows composed per frame max 0
## BV. Visual: open a book and Back, screen recording at 8 frames/s
REC open-base (emu): marker 1553 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-base (emu): marker 1570 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS base: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
REC open-x (emu): marker 1578 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC back-x (emu): marker 1558 ms after the recording start; 60 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
VIS x: back on the list yes; text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" text="Ключова дума" text="2446 резултата  ·  данни от InvLib към 09.11.2026" 
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
book-back | base | 15 | 47.2 / 46.7 / 61.8 | 47.2 | 5.3 | 15 | 14 | 0 | 109
book-back | x | 15 | 31.7 / 30.9 / 49.3 | 31.7 | 13.9 | 14 | 9 | 0 | 142
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 27
contact sheets: 17 in sheets/
contact sheets: 17 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['3813', '7198', '7742', '8140', '9296', '9681', '10842', '11484']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 30
   skipped-frames events=30 max=204 total=2034
== Davey: 64
   davey events=64 max=2880ms median=1003ms
== NativeLink: 8
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## ftb33

```
part ftb33; APK: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 33
host cpu:  AMD EPYC 9V74 80-Core Processor
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 3378 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~ESiN0fFIn9A-bGcfYosvYQ==/org.chyavorec.app-1VQb09FgQPW_GVreuvfddQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 2701 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~593Le3AmXzxo6b8jqxa1Mw==/org.chyavorec.app.b-JaHnKWxEAgd-z6eVUx5IGQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/LiveWallpapersPicker/LiveWallpapersPicker.apk| x86_64: [status=verify] [reason=prebuilt]|
tabs: catalog [320 2274] home [99 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## E1. Round 1: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r1: worst 34.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 14 (3 with >= 3 ms); entry frame 15.7 + next 1.9 (from the entry: 15.7, 1.9, 0.8, 1.1); rows composed per frame max 0
M tab-my-cold@base@r1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (4 with >= 3 ms); entry frame 10.3 + next 6.3 (from the entry: 10.3, 6.3, 1.3, 1.1); rows composed per frame max 0
M tab-more-cold@base@r1: worst 11.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (2 with >= 3 ms); entry frame 11.4 + next 1.2 (from the entry: 11.4, 1.2, 1.8, 0.9); rows composed per frame max 0
M tab-catalog-cold@base@r1: worst 24.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (5 with >= 3 ms); entry frame 10.6 + next 3.0 (from the entry: 10.6, 3.0, 2.9, 24.0); rows composed per frame max 0
M tab-home@base@r1-1: worst 17.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 37 (3 with >= 3 ms); entry frame 17.5 + next 2.0 (from the entry: 17.5, 2.0, 1.0, 0.6); rows composed per frame max 0
M tab-more@base@r1-1: worst 9.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 9.9 + next 5.0 (from the entry: 9.9, 5.0, 0.8, 2.6); rows composed per frame max 0
M tab-my@base@r1-1: worst 11.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 11.1 + next 1.3 (from the entry: 11.1, 1.3, 1.2, 1.0); rows composed per frame max 0
M tab-news@base@r1-1: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 11.5 + next 1.7 (from the entry: 11.5, 1.7, 3.4, 0.8); rows composed per frame max 0
M tab-home@base@r1-2: worst 23.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 37 (4 with >= 3 ms); entry frame 23.9 + next 4.8 (from the entry: 23.9, 4.8, 1.0, 0.7); rows composed per frame max 0
M tab-news@base@r1-2: worst 12.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 12.1 + next 1.9 (from the entry: 12.1, 1.9, 1.8, 0.7); rows composed per frame max 0
M tab-my@base@r1-2: worst 8.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 8.7 + next 1.2 (from the entry: 8.7, 1.2, 1.3, 1.2); rows composed per frame max 0
M tab-more@base@r1-2: worst 19.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (1 with >= 3 ms); entry frame 19.1 + next 2.3 (from the entry: 19.1, 2.3, 1.2, 0.8); rows composed per frame max 0
M tab-catalog@base@r1: worst 18.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 12.1 + next 5.1 (from the entry: 12.1, 5.1, 18.4, 1.7); rows composed per frame max 0
M tab-home@base@r1-3: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 41 (2 with >= 3 ms); entry frame 13.3 + next 1.3 (from the entry: 13.3, 1.3, 2.1, 1.0); rows composed per frame max 0
M tab-news-cold@x@r1: worst 22.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 21 (6 with >= 3 ms); entry frame 15.2 + next 6.1 (from the entry: 15.2, 6.1, 1.4, 22.7); rows composed per frame max 0
M tab-my-cold@x@r1: worst 9.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 34 (3 with >= 3 ms); entry frame 9.4 + next 3.1 (from the entry: 9.4, 3.1, 1.6, 1.0); rows composed per frame max 0
M tab-more-cold@x@r1: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 12.0 + next 1.0 (from the entry: 12.0, 1.0, 2.7, 4.1); rows composed per frame max 0
M tab-catalog-cold@x@r1: worst 23.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 13 (6 with >= 3 ms); entry frame 23.1 + next 2.4 (from the entry: 23.1, 2.4, 6.0, 11.4); rows composed per frame max 0
M tab-home@x@r1-1: worst 16.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 32 (3 with >= 3 ms); entry frame 8.9 + next 1.3 (from the entry: 8.9, 1.3, 16.4, 1.1); rows composed per frame max 0
M tab-more@x@r1-1: worst 9.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (4 with >= 3 ms); entry frame 9.9 + next 0.8 (from the entry: 9.9, 0.8, 8.3, 0.9); rows composed per frame max 0
M tab-my@x@r1-1: worst 8.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 8.9 + next 1.3 (from the entry: 8.9, 1.3, 3.2, 1.2); rows composed per frame max 0
M tab-news@x@r1-1: worst 15.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 15.2 + next 4.3 (from the entry: 15.2, 4.3, 10.7, 1.2); rows composed per frame max 0
M tab-home@x@r1-2: worst 26.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 43 (2 with >= 3 ms); entry frame 9.3 + next 1.3 (from the entry: 9.3, 1.3, 26.7, 1.2); rows composed per frame max 0
M tab-news@x@r1-2: worst 21.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 21.2 + next 2.0 (from the entry: 21.2, 2.0, 8.5, 0.8); rows composed per frame max 0
M tab-my@x@r1-2: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-more@x@r1-2: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 12.7 + next 1.0 (from the entry: 12.7, 1.0, 6.3, 0.6); rows composed per frame max 0
M tab-catalog@x@r1: worst 18.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 18.2 + next 1.7 (from the entry: 18.2, 1.7, 16.4, 1.3); rows composed per frame max 0
M tab-home@x@r1-3: worst 10.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 40 (3 with >= 3 ms); entry frame 10.8 + next 1.0 (from the entry: 10.8, 1.0, 5.8, 2.1); rows composed per frame max 0
## E2. Round 2: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r2: worst 16.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 16.0 + next 1.8 (from the entry: 16.0, 1.8, 1.5, 15.9); rows composed per frame max 0
M tab-my-cold@x@r2: worst 12.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (4 with >= 3 ms); entry frame 12.1 + next 3.2 (from the entry: 12.1, 3.2, 1.2, 0.6); rows composed per frame max 0
M tab-more-cold@x@r2: worst 14.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 14.1 + next 8.7 (from the entry: 14.1, 8.7, 1.5, 9.7); rows composed per frame max 0
M tab-catalog-cold@x@r2: worst 24.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 13 (3 with >= 3 ms); entry frame 13.2 + next 2.3 (from the entry: 13.2, 2.3, 24.0, 2.6); rows composed per frame max 0
M tab-home@x@r2-1: worst 14.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (3 with >= 3 ms); entry frame 8.8 + next 2.0 (from the entry: 8.8, 2.0, 14.1, 1.2); rows composed per frame max 0
M tab-more@x@r2-1: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 12.2 + next 1.7 (from the entry: 12.2, 1.7, 10.6, 0.8); rows composed per frame max 0
M tab-my@x@r2-1: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 10.7 + next 2.6 (from the entry: 10.7, 2.6, 1.2, 0.7); rows composed per frame max 0
M tab-news@x@r2-1: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 13.8 + next 1.7 (from the entry: 13.8, 1.7, 7.8, 1.1); rows composed per frame max 0
M tab-home@x@r2-2: worst 27.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 39 (2 with >= 3 ms); entry frame 27.2 + next 1.6 (from the entry: 27.2, 1.6, 0.7, 0.5); rows composed per frame max 0
M tab-news@x@r2-2: worst 15.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 15.2 + next 1.6 (from the entry: 15.2, 1.6, 8.0, 0.8); rows composed per frame max 0
M tab-my@x@r2-2: worst 10.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 10.0 + next 1.1 (from the entry: 10.0, 1.1, 1.4, 1.1); rows composed per frame max 0
M tab-more@x@r2-2: worst 14.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 12.9 + next 1.9 (from the entry: 12.9, 1.9, 14.8, 1.0); rows composed per frame max 0
M tab-catalog@x@r2: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 15.1 + next 1.8 (from the entry: 15.1, 1.8, 8.3, 1.2); rows composed per frame max 0
M tab-home@x@r2-3: worst 24.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 39 (4 with >= 3 ms); entry frame 8.9 + next 1.4 (from the entry: 8.9, 1.4, 24.1, 0.9); rows composed per frame max 0
M tab-news-cold@base@r2: worst 21.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 13 (4 with >= 3 ms); entry frame 19.0 + next 1.7 (from the entry: 19.0, 1.7, 1.4, 21.2); rows composed per frame max 0
M tab-my-cold@base@r2: worst 19.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 36 (4 with >= 3 ms); entry frame 19.3 + next 5.2 (from the entry: 19.3, 5.2, 1.5, 1.1); rows composed per frame max 0
M tab-more-cold@base@r2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 15.6 + next 1.8 (from the entry: 15.6, 1.8, 1.9, 0.9); rows composed per frame max 0
M tab-catalog-cold@base@r2: worst 26.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 13 (4 with >= 3 ms); entry frame 26.1 + next 1.7 (from the entry: 26.1, 1.7, 5.2, 14.0); rows composed per frame max 0
M tab-home@base@r2-1: worst 16.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 41 (3 with >= 3 ms); entry frame 16.1 + next 4.8 (from the entry: 16.1, 4.8, 0.7, 0.8); rows composed per frame max 0
M tab-more@base@r2-1: worst 11.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (4 with >= 3 ms); entry frame 11.1 + next 4.7 (from the entry: 11.1, 4.7, 1.2, 0.7); rows composed per frame max 0
M tab-my@base@r2-1: worst 13.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 13.5 + next 1.4 (from the entry: 13.5, 1.4, 1.3, 3.2); rows composed per frame max 0
M tab-news@base@r2-1: worst 16.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 16.8 + next 1.3 (from the entry: 16.8, 1.3, 1.3, 3.9); rows composed per frame max 0
M tab-home@base@r2-2: worst 29.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 41 (1 with >= 3 ms); entry frame 29.3 + next 2.1 (from the entry: 29.3, 2.1, 1.2, 0.8); rows composed per frame max 0
M tab-news@base@r2-2: worst 20.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 20.6 + next 1.8 (from the entry: 20.6, 1.8, 0.7, 0.7); rows composed per frame max 0
M tab-my@base@r2-2: worst 17.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 13 (2 with >= 3 ms); entry frame 17.2 + next 1.6 (from the entry: 17.2, 1.6, 1.2, 0.6); rows composed per frame max 0
M tab-more@base@r2-2: worst 17.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 17.4 + next 1.7 (from the entry: 17.4, 1.7, 0.8, 0.8); rows composed per frame max 0
M tab-catalog@base@r2: worst 31.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 12 (2 with >= 3 ms); entry frame 13.1 + next 1.9 (from the entry: 13.1, 1.9, 31.3, 1.1); rows composed per frame max 0
M tab-home@base@r2-3: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 38 (2 with >= 3 ms); entry frame 12.7 + next 1.0 (from the entry: 12.7, 1.0, 1.5, 0.8); rows composed per frame max 0
## E3. Round 3: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r3: worst 48.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (3 with >= 3 ms); entry frame 8.9 + next 1.7 (from the entry: 8.9, 1.7, 0.9, 1.0); rows composed per frame max 0
M tab-my-cold@base@r3: worst 16.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 35 (4 with >= 3 ms); entry frame 16.5 + next 5.2 (from the entry: 16.5, 5.2, 1.7, 0.9); rows composed per frame max 0
M tab-more-cold@base@r3: worst 11.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (1 with >= 3 ms); entry frame 11.1 + next 1.9 (from the entry: 11.1, 1.9, 1.9, 0.8); rows composed per frame max 0
M tab-catalog-cold@base@r3: worst 42.0 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 16 (4 with >= 3 ms); entry frame 16.6 + next 2.6 (from the entry: 16.6, 2.6, 3.2, 1.6); rows composed per frame max 0
M tab-home@base@r3-1: worst 19.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 38 (4 with >= 3 ms); entry frame 19.2 + next 1.9 (from the entry: 19.2, 1.9, 1.1, 0.8); rows composed per frame max 0
M tab-more@base@r3-1: worst 20.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 20.0 + next 1.8 (from the entry: 20.0, 1.8, 1.3, 0.8); rows composed per frame max 0
M tab-my@base@r3-1: worst 9.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 9.6 + next 1.2 (from the entry: 9.6, 1.2, 1.2, 0.8); rows composed per frame max 0
M tab-news@base@r3-1: worst 22.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (1 with >= 3 ms); entry frame 22.4 + next 1.7 (from the entry: 22.4, 1.7, 0.6, 0.7); rows composed per frame max 0
M tab-home@base@r3-2: worst 15.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 43 (4 with >= 3 ms); entry frame 15.8 + next 1.4 (from the entry: 15.8, 1.4, 1.4, 0.8); rows composed per frame max 0
M tab-news@base@r3-2: worst 22.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 22.7 + next 1.9 (from the entry: 22.7, 1.9, 0.6, 0.9); rows composed per frame max 0
M tab-my@base@r3-2: worst 8.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 8.2 + next 1.7 (from the entry: 8.2, 1.7, 1.3, 0.6); rows composed per frame max 0
M tab-more@base@r3-2: worst 18.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 18.2 + next 1.4 (from the entry: 18.2, 1.4, 3.3, 1.0); rows composed per frame max 0
M tab-catalog@base@r3: worst 16.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 14.8 + next 1.3 (from the entry: 14.8, 1.3, 16.2, 1.3); rows composed per frame max 0
M tab-home@base@r3-3: worst 18.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 38 (4 with >= 3 ms); entry frame 18.9 + next 1.2 (from the entry: 18.9, 1.2, 1.5, 0.6); rows composed per frame max 0
M tab-news-cold@x@r3: worst 14.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 12.2 + next 2.4 (from the entry: 12.2, 2.4, 1.8, 14.7); rows composed per frame max 0
M tab-my-cold@x@r3: worst 14.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (2 with >= 3 ms); entry frame 14.5 + next 2.4 (from the entry: 14.5, 2.4, 1.2, 1.1); rows composed per frame max 0
M tab-more-cold@x@r3: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 12.9 + next 1.4 (from the entry: 12.9, 1.4, 5.9, 0.8); rows composed per frame max 0
M tab-catalog-cold@x@r3: worst 12.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 12.8 + next 2.4 (from the entry: 12.8, 2.4, 3.4, 12.4); rows composed per frame max 0
M tab-home@x@r3-1: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 34 (3 with >= 3 ms); entry frame 12.2 + next 1.1 (from the entry: 12.2, 1.1, 4.9, 2.2); rows composed per frame max 0
M tab-more@x@r3-1: worst 12.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 12.8 + next 0.6 (from the entry: 12.8, 0.6, 4.9, 0.7); rows composed per frame max 0
M tab-my@x@r3-1: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 13.0 + next 1.1 (from the entry: 13.0, 1.1, 2.0, 0.5); rows composed per frame max 0
M tab-news@x@r3-1: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 13.0 + next 1.2 (from the entry: 13.0, 1.2, 6.7, 1.0); rows composed per frame max 0
M tab-home@x@r3-2: worst 17.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 41 (3 with >= 3 ms); entry frame 10.4 + next 4.8 (from the entry: 10.4, 4.8, 17.9, 1.6); rows composed per frame max 0
M tab-news@x@r3-2: worst 18.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 18.2 + next 1.8 (from the entry: 18.2, 1.8, 7.9, 0.9); rows composed per frame max 0
M tab-my@x@r3-2: worst 9.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 9.8 + next 1.4 (from the entry: 9.8, 1.4, 1.3, 1.0); rows composed per frame max 0
M tab-more@x@r3-2: worst 14.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 10.7 + next 1.6 (from the entry: 10.7, 1.6, 14.3, 1.1); rows composed per frame max 0
M tab-catalog@x@r3: worst 16.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 11.2 + next 1.6 (from the entry: 11.2, 1.6, 16.4, 4.0); rows composed per frame max 0
M tab-home@x@r3-3: worst 21.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 41 (3 with >= 3 ms); entry frame 21.5 + next 1.0 (from the entry: 21.5, 1.0, 3.9, 2.9); rows composed per frame max 0
## E4. Round 4: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r4: worst 20.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 13 (4 with >= 3 ms); entry frame 20.5 + next 1.9 (from the entry: 20.5, 1.9, 1.6, 9.2); rows composed per frame max 0
M tab-my-cold@x@r4: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 36 (5 with >= 3 ms); entry frame 12.9 + next 4.7 (from the entry: 12.9, 4.7, 4.4, 1.2); rows composed per frame max 0
M tab-more-cold@x@r4: worst 16.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (5 with >= 3 ms); entry frame 16.9 + next 3.7 (from the entry: 16.9, 3.7, 7.1, 4.8); rows composed per frame max 0
M tab-catalog-cold@x@r4: worst 28.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 13 (5 with >= 3 ms); entry frame 28.0 + next 7.2 (from the entry: 28.0, 7.2, 14.5, 4.4); rows composed per frame max 0
M tab-home@x@r4-1: worst 21.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 21.6 + next 0.7 (from the entry: 21.6, 0.7, 9.1, 1.8); rows composed per frame max 0
M tab-more@x@r4-1: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (5 with >= 3 ms); entry frame 9.9 + next 13.8 (from the entry: 9.9, 13.8, 2.3, 11.9); rows composed per frame max 0
M tab-my@x@r4-1: worst 8.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 8.4 + next 1.5 (from the entry: 8.4, 1.5, 6.4, 1.2); rows composed per frame max 0
M tab-news@x@r4-1: worst 13.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 13.9 + next 1.2 (from the entry: 13.9, 1.2, 10.5, 1.1); rows composed per frame max 0
M tab-home@x@r4-2: worst 28.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 13 (3 with >= 3 ms); entry frame 28.2 + next 1.5 (from the entry: 28.2, 1.5, 0.6, 0.6); rows composed per frame max 0
M tab-news@x@r4-2: worst 9.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (6 with >= 3 ms); entry frame 8.8 + next 5.0 (from the entry: 8.8, 5.0, 9.0, 1.0); rows composed per frame max 0
M tab-my@x@r4-2: worst 12.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 12.3 + next 1.8 (from the entry: 12.3, 1.8, 1.5, 1.1); rows composed per frame max 0
M tab-more@x@r4-2: worst 11.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 11.8 + next 0.7 (from the entry: 11.8, 0.7, 1.9, 0.7); rows composed per frame max 0
M tab-catalog@x@r4: worst 18.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 14.7 + next 1.7 (from the entry: 14.7, 1.7, 18.9, 1.2); rows composed per frame max 0
M tab-home@x@r4-3: worst 21.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 21.7 + next 1.2 (from the entry: 21.7, 1.2, 6.1, 1.9); rows composed per frame max 0
M tab-news-cold@base@r4: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 11.5 + next 2.1 (from the entry: 11.5, 2.1, 9.9, 1.2); rows composed per frame max 0
M tab-my-cold@base@r4: worst 24.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 33 (3 with >= 3 ms); entry frame 24.0 + next 11.0 (from the entry: 24.0, 11.0, 1.4, 1.8); rows composed per frame max 0
M tab-more-cold@base@r4: worst 23.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (6 with >= 3 ms); entry frame 23.4 + next 1.8 (from the entry: 23.4, 1.8, 5.5, 1.0); rows composed per frame max 0
M tab-catalog-cold@base@r4: worst 15.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 10.6 + next 2.5 (from the entry: 10.6, 2.5, 4.5, 15.8); rows composed per frame max 0
M tab-home@base@r4-1: worst 13.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (5 with >= 3 ms); entry frame 13.6 + next 4.8 (from the entry: 13.6, 4.8, 1.1, 0.7); rows composed per frame max 0
M tab-more@base@r4-1: worst 15.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 15.7 + next 2.1 (from the entry: 15.7, 2.1, 0.9, 0.8); rows composed per frame max 0
M tab-my@base@r4-1: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 13.3 + next 1.1 (from the entry: 13.3, 1.1, 1.6, 0.6); rows composed per frame max 0
M tab-news@base@r4-1: worst 21.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (4 with >= 3 ms); entry frame 21.5 + next 1.6 (from the entry: 21.5, 1.6, 4.5, 4.1); rows composed per frame max 0
M tab-home@base@r4-2: worst 24.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (2 with >= 3 ms); entry frame 24.1 + next 1.5 (from the entry: 24.1, 1.5, 1.3, 0.9); rows composed per frame max 0
M tab-news@base@r4-2: worst 22.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (4 with >= 3 ms); entry frame 22.7 + next 1.9 (from the entry: 22.7, 1.9, 0.8, 0.7); rows composed per frame max 0
M tab-my@base@r4-2: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (2 with >= 3 ms); entry frame 11.5 + next 1.5 (from the entry: 11.5, 1.5, 1.8, 0.6); rows composed per frame max 0
M tab-more@base@r4-2: worst 24.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 24.7 + next 2.4 (from the entry: 24.7, 2.4, 0.7, 1.1); rows composed per frame max 0
M tab-catalog@base@r4: worst 15.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 15.4 + next 1.4 (from the entry: 15.4, 1.4, 12.6, 1.3); rows composed per frame max 0
M tab-home@base@r4-3: worst 13.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 13.1 + next 1.3 (from the entry: 13.1, 1.3, 1.4, 0.9); rows composed per frame max 0
## E5. Round 5: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r5: worst 15.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (5 with >= 3 ms); entry frame 15.4 + next 8.4 (from the entry: 15.4, 8.4, 1.9, 11.4); rows composed per frame max 0
M tab-my-cold@base@r5: worst 18.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 36 (2 with >= 3 ms); entry frame 18.4 + next 2.6 (from the entry: 18.4, 2.6, 1.3, 0.6); rows composed per frame max 0
M tab-more-cold@base@r5: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 11.5 + next 2.0 (from the entry: 11.5, 2.0, 2.7, 2.1); rows composed per frame max 0
M tab-catalog-cold@base@r5: worst 39.7 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 1 of 13 (3 with >= 3 ms); entry frame 27.7 + next 2.9 (from the entry: 27.7, 2.9, 39.7, 1.7); rows composed per frame max 0
M tab-home@base@r5-1: worst 15.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 40 (2 with >= 3 ms); entry frame 15.4 + next 1.8 (from the entry: 15.4, 1.8, 1.2, 3.0); rows composed per frame max 0
M tab-more@base@r5-1: worst 19.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 11.7 + next 1.7 (from the entry: 11.7, 1.7, 1.1, 0.8); rows composed per frame max 0
M tab-my@base@r5-1: worst 9.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 9.3 + next 1.2 (from the entry: 9.3, 1.2, 1.2, 0.6); rows composed per frame max 0
M tab-news@base@r5-1: worst 20.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (1 with >= 3 ms); entry frame 20.2 + next 2.1 (from the entry: 20.2, 2.1, 1.2, 1.0); rows composed per frame max 0
M tab-home@base@r5-2: worst 26.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 43 (3 with >= 3 ms); entry frame 26.9 + next 4.8 (from the entry: 26.9, 4.8, 0.9, 0.8); rows composed per frame max 0
M tab-news@base@r5-2: worst 19.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 19.9 + next 1.4 (from the entry: 19.9, 1.4, 0.8, 0.8); rows composed per frame max 0
M tab-my@base@r5-2: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 11.2 + next 2.0 (from the entry: 11.2, 2.0, 1.3, 1.0); rows composed per frame max 0
M tab-more@base@r5-2: worst 9.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 9.6 + next 1.7 (from the entry: 9.6, 1.7, 1.3, 0.8); rows composed per frame max 0
M tab-catalog@base@r5: worst 27.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 12 (3 with >= 3 ms); entry frame 12.4 + next 1.8 (from the entry: 12.4, 1.8, 27.9, 1.2); rows composed per frame max 0
M tab-home@base@r5-3: worst 20.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 39 (2 with >= 3 ms); entry frame 20.7 + next 1.9 (from the entry: 20.7, 1.9, 1.2, 0.8); rows composed per frame max 0
M tab-news-cold@x@r5: worst 9.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (6 with >= 3 ms); entry frame 8.6 + next 9.8 (from the entry: 8.6, 9.8, 2.0, 4.7); rows composed per frame max 0
M tab-my-cold@x@r5: worst 10.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (3 with >= 3 ms); entry frame 10.8 + next 3.4 (from the entry: 10.8, 3.4, 1.1, 1.0); rows composed per frame max 0
M tab-more-cold@x@r5: worst 7.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-catalog-cold@x@r5: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 10.2 + next 2.6 (from the entry: 10.2, 2.6, 7.7, 10.3); rows composed per frame max 0
M tab-home@x@r5-1: worst 10.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 36 (5 with >= 3 ms); entry frame 10.8 + next 3.7 (from the entry: 10.8, 3.7, 3.5, 3.9); rows composed per frame max 0
M tab-more@x@r5-1: worst 13.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (4 with >= 3 ms); entry frame 8.1 + next 1.2 (from the entry: 8.1, 1.2, 13.7, 0.8); rows composed per frame max 0
M tab-my@x@r5-1: worst 7.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@x@r5-1: worst 12.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 12.8 + next 1.7 (from the entry: 12.8, 1.7, 4.7, 1.0); rows composed per frame max 0
M tab-home@x@r5-2: worst 21.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 41 (4 with >= 3 ms); entry frame 21.5 + next 1.5 (from the entry: 21.5, 1.5, 0.5, 0.6); rows composed per frame max 0
M tab-news@x@r5-2: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 11.5 + next 1.5 (from the entry: 11.5, 1.5, 7.7, 0.8); rows composed per frame max 0
M tab-my@x@r5-2: worst 9.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 9.8 + next 1.0 (from the entry: 9.8, 1.0, 1.9, 0.6); rows composed per frame max 0
M tab-more@x@r5-2: worst 18.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 18.6 + next 1.5 (from the entry: 18.6, 1.5, 7.4, 0.9); rows composed per frame max 0
M tab-catalog@x@r5: worst 9.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (2 with >= 3 ms); entry frame 9.6 + next 0.7 (from the entry: 9.6, 0.7, 1.1, 0.6); rows composed per frame max 0
M tab-home@x@r5-3: worst 19.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 40 (5 with >= 3 ms); entry frame 19.4 + next 1.0 (from the entry: 19.4, 1.0, 10.5, 4.1); rows composed per frame max 0
## EV. Visual: the tab entries recorded at 8 frames/s
REC tab-news-base (emu): marker 1648 ms after the recording start; 47 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-base (emu): marker 1543 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-base (emu): marker 1536 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-news-x (emu): marker 1627 ms after the recording start; 46 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-x (emu): marker 1555 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-x (emu): marker 1545 ms after the recording start; 43 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
tab-news-cold | base | 5 | 21.2 / 26.4 / 48.9 | 15.4 | 1.9 | 2 | 2 | 0 | 61
tab-news-cold | x | 5 | 16.0 / 16.7 / 22.7 | 15.2 | 2.4 | 0 | 0 | 0 | 48
tab-my-cold | base | 5 | 18.4 / 17.7 / 24.0 | 18.4 | 5.2 | 0 | 0 | 0 | 42
tab-my-cold | x | 5 | 12.1 / 11.9 / 14.5 | 12.1 | 3.2 | 0 | 0 | 0 | 34
tab-more-cold | base | 5 | 11.5 / 14.6 / 23.4 | 11.5 | 1.8 | 0 | 0 | 0 | 30
tab-more-cold | x | 5 | 12.9 / 12.7 / 16.9 | 12.9 | 1.4 | 0 | 0 | 0 | 42
tab-catalog-cold | base | 5 | 26.1 / 29.5 / 42.0 | 16.6 | 2.6 | 4 | 2 | 0 | 64
tab-catalog-cold | x | 5 | 23.1 / 19.6 / 28.0 | 13.2 | 2.4 | 1 | 0 | 0 | 56
tab-home | base | 15 | 17.5 / 18.7 / 29.3 | 17.5 | 1.9 | 2 | 0 | 0 | 40
tab-home | x | 15 | 21.5 / 19.6 / 28.2 | 12.2 | 1.3 | 3 | 0 | 0 | 44
tab-more | base | 10 | 17.8 / 16.5 / 24.7 | 16.5 | 2.0 | 0 | 0 | 0 | 31
tab-more | x | 10 | 13.2 / 13.5 / 18.6 | 12.0 | 1.4 | 0 | 0 | 0 | 36
tab-my | base | 10 | 11.1 / 11.4 / 17.2 | 11.1 | 1.4 | 0 | 0 | 0 | 25
tab-my | x | 10 | 9.8 / 9.8 / 13.0 | 9.8 | 1.2 | 0 | 0 | 0 | 23
tab-news | base | 10 | 20.4 / 19.0 / 22.7 | 20.4 | 1.8 | 0 | 0 | 0 | 32
tab-news | x | 10 | 13.9 / 14.4 / 21.2 | 13.9 | 1.7 | 0 | 0 | 0 | 35
tab-catalog | base | 5 | 18.4 / 21.8 / 31.3 | 13.1 | 1.8 | 2 | 1 | 0 | 44
tab-catalog | x | 5 | 16.4 / 15.6 / 18.9 | 14.7 | 1.7 | 0 | 0 | 0 | 40
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 13
contact sheets: 20 in sheets/
contact sheets: 20 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5643', '7954', '8622', '9136', '10668', '11179', '12602', '13216']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 31
   skipped-frames events=31 max=115 total=1720
== Davey: 44
   davey events=44 max=3030ms median=1248ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## ftb36x

```
part ftb36x; APK: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 36
host cpu:  AMD EPYC 9V45 96-Core Processor
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 4035 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~IGCESSfrgl_Z6e6RUTafuA==/org.chyavorec.app-53hkGw1eN-fFJXJrC4utHg==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~XtY1YCObh8NkpAPOhzvllA==/org.chyavorec.app.b-55fel6u9h5r8viX7_LtHtw==/base.apk| x86_64: [status=verify] [reason=install] [primary-abi]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 2772 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~XtY1YCObh8NkpAPOhzvllA==/org.chyavorec.app.b-55fel6u9h5r8viX7_LtHtw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]|
tabs: catalog [320 2274] home [100 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## E1. Round 1: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r1: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 30 (8 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-my-cold@base@r1: worst 19.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 55 (6 with >= 3 ms); entry frame 19.2 + next 2.6 (from the entry: 19.2, 2.6, 2.5, 4.4); rows composed per frame max 0
M tab-more-cold@base@r1: worst 18.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 32 (6 with >= 3 ms); entry frame 18.1 + next 18.0 (from the entry: 18.1, 18.0, 1.8, 1.7); rows composed per frame max 0
M tab-catalog-cold@base@r1: worst 17.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 26 (4 with >= 3 ms); entry frame 8.8 + next 2.6 (from the entry: 8.8, 2.6, 2.5, 17.9); rows composed per frame max 0
M tab-home@base@r1-1: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 41 (3 with >= 3 ms); entry frame 11.2 + next 1.9 (from the entry: 11.2, 1.9, 1.8, 0.3); rows composed per frame max 0
M tab-more@base@r1-1: worst 14.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (6 with >= 3 ms); entry frame 14.0 + next 13.9 (from the entry: 14.0, 13.9, 1.8, 1.7); rows composed per frame max 0
M tab-my@base@r1-1: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 10.7 + next 3.7 (from the entry: 10.7, 3.7, 3.7, 1.3); rows composed per frame max 0
M tab-news@base@r1-1: worst 10.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 10.6 + next 1.6 (from the entry: 10.6, 1.6, 1.5, 0.8); rows composed per frame max 0
M tab-home@base@r1-2: worst 18.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 20 (5 with >= 3 ms); entry frame 18.4 + next 3.3 (from the entry: 18.4, 3.3, 3.2, 1.2); rows composed per frame max 0
M tab-news@base@r1-2: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (7 with >= 3 ms); entry frame 10.7 + next 10.7 (from the entry: 10.7, 10.7, 3.1, 3.0); rows composed per frame max 0
M tab-my@base@r1-2: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 10.7 + next 1.4 (from the entry: 10.7, 1.4, 1.2, 1.1); rows composed per frame max 0
M tab-more@base@r1-2: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (1 with >= 3 ms); entry frame 15.1 + next 0.9 (from the entry: 15.1, 0.9, 1.3, 1.2); rows composed per frame max 0
M tab-catalog@base@r1: worst 7.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (4 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-home@base@r1-3: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 10.5 + next 0.8 (from the entry: 10.5, 0.8, 3.5, 3.5); rows composed per frame max 0
M tab-news-cold@x@r1: worst 7.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 28 (7 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-my-cold@x@r1: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 57 (1 with >= 3 ms); entry frame 12.0 + next 2.4 (from the entry: 12.0, 2.4, 2.3, 1.5); rows composed per frame max 0
M tab-more-cold@x@r1: worst 11.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 36 (8 with >= 3 ms); entry frame 10.0 + next 10.0 (from the entry: 10.0, 10.0, 1.8, 1.7); rows composed per frame max 0
M tab-catalog-cold@x@r1: worst 17.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 26 (6 with >= 3 ms); entry frame 17.2 + next 2.7 (from the entry: 17.2, 2.7, 2.7, 12.5); rows composed per frame max 0
M tab-home@x@r1-1: worst 8.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 64 (4 with >= 3 ms); entry frame 8.4 + next 1.7 (from the entry: 8.4, 1.7, 1.6, 5.2); rows composed per frame max 0
M tab-more@x@r1-1: worst 9.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 9.4 + next 9.4 (from the entry: 9.4, 9.4, 0.8, 0.7); rows composed per frame max 0
M tab-my@x@r1-1: worst 6.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@x@r1-1: worst 11.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 11.6 + next 1.7 (from the entry: 11.6, 1.7, 1.7, 10.3); rows composed per frame max 0
M tab-home@x@r1-2: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 67 (5 with >= 3 ms); entry frame 10.5 + next 10.5 (from the entry: 10.5, 10.5, 1.8, 1.7); rows composed per frame max 0
M tab-news@x@r1-2: worst 17.6 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 17.6 + next 17.6 (from the entry: 17.6, 17.6, 2.0, 2.0); rows composed per frame max 0
M tab-my@x@r1-2: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-more@x@r1-2: worst 13.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 13.2 + next 1.6 (from the entry: 13.2, 1.6, 1.5, 7.7); rows composed per frame max 0
M tab-catalog@x@r1: worst 13.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (5 with >= 3 ms); entry frame 13.2 + next 1.6 (from the entry: 13.2, 1.6, 7.7, 7.6); rows composed per frame max 0
M tab-home@x@r1-3: worst 18.4 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 59 (7 with >= 3 ms); entry frame 18.4 + next 18.3 (from the entry: 18.4, 18.3, 1.2, 1.1); rows composed per frame max 0
## E2. Round 2: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r2: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (10 with >= 3 ms); entry frame 11.2 + next 11.1 (from the entry: 11.2, 11.1, 4.3, 4.2); rows composed per frame max 0
M tab-my-cold@x@r2: worst 13.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 58 (3 with >= 3 ms); entry frame 13.2 + next 3.0 (from the entry: 13.2, 3.0, 2.9, 1.5); rows composed per frame max 0
M tab-more-cold@x@r2: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 38 (3 with >= 3 ms); entry frame 9.5 + next 9.4 (from the entry: 9.5, 9.4, 1.4, 1.3); rows composed per frame max 0
M tab-catalog-cold@x@r2: worst 18.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 25 (6 with >= 3 ms); entry frame 18.5 + next 5.3 (from the entry: 18.5, 5.3, 5.2, 12.3); rows composed per frame max 0
M tab-home@x@r2-1: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 59 (4 with >= 3 ms); entry frame 10.5 + next 1.9 (from the entry: 10.5, 1.9, 1.8, 7.9); rows composed per frame max 0
M tab-more@x@r2-1: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-my@x@r2-1: worst 10.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (1 with >= 3 ms); entry frame 10.0 + next 1.1 (from the entry: 10.0, 1.1, 1.1, 1.1); rows composed per frame max 0
M tab-news@x@r2-1: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 10.5 + next 4.6 (from the entry: 10.5, 4.6, 4.5, 4.7); rows composed per frame max 0
M tab-home@x@r2-2: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 67 (6 with >= 3 ms); entry frame 10.1 + next 2.0 (from the entry: 10.1, 2.0, 2.0, 10.7); rows composed per frame max 0
M tab-news@x@r2-2: worst 8.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 8.5 + next 8.4 (from the entry: 8.5, 8.4, 2.7, 2.6); rows composed per frame max 0
M tab-my@x@r2-2: worst 8.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (3 with >= 3 ms); entry frame 8.2 + next 7.0 (from the entry: 8.2, 7.0, 7.0, 1.6); rows composed per frame max 0
M tab-more@x@r2-2: worst 8.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 8.6 + next 0.9 (from the entry: 8.6, 0.9, 5.3, 5.3); rows composed per frame max 0
M tab-catalog@x@r2: worst 16.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 16.6 + next 1.8 (from the entry: 16.6, 1.8, 10.9, 10.8); rows composed per frame max 0
M tab-home@x@r2-3: worst 8.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 65 (7 with >= 3 ms); entry frame 8.4 + next 8.3 (from the entry: 8.4, 8.3, 1.0, 0.9); rows composed per frame max 0
M tab-news-cold@base@r2: worst 9.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (9 with >= 3 ms); entry frame 9.2 + next 9.1 (from the entry: 9.2, 9.1, 4.3, 4.2); rows composed per frame max 0
M tab-my-cold@base@r2: worst 14.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 60 (3 with >= 3 ms); entry frame 14.8 + next 2.9 (from the entry: 14.8, 2.9, 2.8, 1.6); rows composed per frame max 0
M tab-more-cold@base@r2: worst 17.4 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 31 (3 with >= 3 ms); entry frame 17.4 + next 17.4 (from the entry: 17.4, 17.4, 1.9, 1.8); rows composed per frame max 0
M tab-catalog-cold@base@r2: worst 12.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 29 (5 with >= 3 ms); entry frame 9.3 + next 2.6 (from the entry: 9.3, 2.6, 2.6, 2.8); rows composed per frame max 0
M tab-home@base@r2-1: worst 12.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 65 (3 with >= 3 ms); entry frame 12.3 + next 2.7 (from the entry: 12.3, 2.7, 2.7, 1.5); rows composed per frame max 0
M tab-more@base@r2-1: worst 10.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (6 with >= 3 ms); entry frame 10.1 + next 10.0 (from the entry: 10.1, 10.0, 5.1, 5.1); rows composed per frame max 0
M tab-my@base@r2-1: worst 5.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 29 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@base@r2-1: worst 8.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 8.5 + next 4.6 (from the entry: 8.5, 4.6, 4.6, 1.1); rows composed per frame max 0
M tab-home@base@r2-2: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 68 (3 with >= 3 ms); entry frame 11.2 + next 1.6 (from the entry: 11.2, 1.6, 1.5, 0.9); rows composed per frame max 0
M tab-news@base@r2-2: worst 11.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (5 with >= 3 ms); entry frame 10.3 + next 10.2 (from the entry: 10.3, 10.2, 1.9, 1.8); rows composed per frame max 0
M tab-my@base@r2-2: worst 11.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 11.4 + next 1.9 (from the entry: 11.4, 1.9, 1.9, 0.6); rows composed per frame max 0
M tab-more@base@r2-2: worst 24.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 24.5 + next 1.6 (from the entry: 24.5, 1.6, 1.6, 1.1); rows composed per frame max 0
M tab-catalog@base@r2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (8 with >= 3 ms); entry frame 11.1 + next 2.6 (from the entry: 11.1, 2.6, 15.6, 15.4); rows composed per frame max 0
M tab-home@base@r2-3: worst 10.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 61 (4 with >= 3 ms); entry frame 10.9 + next 1.1 (from the entry: 10.9, 1.1, 1.5, 1.4); rows composed per frame max 0
## E3. Round 3: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r3: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 34 (8 with >= 3 ms); entry frame 12.3 + next 12.2 (from the entry: 12.3, 12.2, 2.2, 2.1); rows composed per frame max 0
M tab-my-cold@base@r3: worst 20.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 56 (1 with >= 3 ms); entry frame 20.4 + next 2.8 (from the entry: 20.4, 2.8, 2.7, 1.6); rows composed per frame max 0
M tab-more-cold@base@r3: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 34 (5 with >= 3 ms); entry frame 15.5 + next 15.4 (from the entry: 15.5, 15.4, 1.8, 1.7); rows composed per frame max 0
M tab-catalog-cold@base@r3: worst 17.5 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 27 (5 with >= 3 ms); entry frame 9.1 + next 2.5 (from the entry: 9.1, 2.5, 2.4, 17.5); rows composed per frame max 0
M tab-home@base@r3-1: worst 11.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 60 (3 with >= 3 ms); entry frame 11.8 + next 2.0 (from the entry: 11.8, 2.0, 2.0, 1.1); rows composed per frame max 0
M tab-more@base@r3-1: worst 23.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 19 (7 with >= 3 ms); entry frame 23.0 + next 22.9 (from the entry: 23.0, 22.9, 3.9, 3.8); rows composed per frame max 0
M tab-my@base@r3-1: worst 8.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 8.2 + next 1.3 (from the entry: 8.2, 1.3, 1.0, 1.0); rows composed per frame max 0
M tab-news@base@r3-1: worst 13.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 13.9 + next 1.6 (from the entry: 13.9, 1.6, 1.6, 1.0); rows composed per frame max 0
M tab-home@base@r3-2: worst 17.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 62 (1 with >= 3 ms); entry frame 17.7 + next 1.8 (from the entry: 17.7, 1.8, 1.8, 1.2); rows composed per frame max 0
M tab-news@base@r3-2: worst 11.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (6 with >= 3 ms); entry frame 11.0 + next 10.9 (from the entry: 11.0, 10.9, 2.1, 2.0); rows composed per frame max 0
M tab-my@base@r3-2: worst 6.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (5 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-more@base@r3-2: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 12.7 + next 0.8 (from the entry: 12.7, 0.8, 1.3, 1.3); rows composed per frame max 0
M tab-catalog@base@r3: worst 14.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (4 with >= 3 ms); entry frame 14.7 + next 1.9 (from the entry: 14.7, 1.9, 11.0, 11.0); rows composed per frame max 0
M tab-home@base@r3-3: worst 12.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 64 (3 with >= 3 ms); entry frame 12.1 + next 1.1 (from the entry: 12.1, 1.1, 1.6, 1.6); rows composed per frame max 0
M tab-news-cold@x@r3: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (8 with >= 3 ms); entry frame 15.1 + next 15.1 (from the entry: 15.1, 15.1, 1.5, 1.5); rows composed per frame max 0
M tab-my-cold@x@r3: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 52 (1 with >= 3 ms); entry frame 13.8 + next 2.8 (from the entry: 13.8, 2.8, 2.8, 1.8); rows composed per frame max 0
M tab-more-cold@x@r3: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 37 (7 with >= 3 ms); entry frame 10.7 + next 10.6 (from the entry: 10.7, 10.6, 1.0, 0.9); rows composed per frame max 0
M tab-catalog-cold@x@r3: worst 19.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 26 (6 with >= 3 ms); entry frame 10.5 + next 2.2 (from the entry: 10.5, 2.2, 2.2, 19.2); rows composed per frame max 0
M tab-home@x@r3-1: worst 8.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 58 (3 with >= 3 ms); entry frame 8.5 + next 1.8 (from the entry: 8.5, 1.8, 1.7, 8.5); rows composed per frame max 0
M tab-more@x@r3-1: worst 19.7 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 19 (7 with >= 3 ms); entry frame 19.7 + next 19.7 (from the entry: 19.7, 19.7, 1.6, 1.5); rows composed per frame max 0
M tab-my@x@r3-1: worst 6.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@x@r3-1: worst 8.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 8.9 + next 1.6 (from the entry: 8.9, 1.6, 1.5, 4.4); rows composed per frame max 0
M tab-home@x@r3-2: worst 15.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 70 (4 with >= 3 ms); entry frame 15.0 + next 1.3 (from the entry: 15.0, 1.3, 1.2, 0.5); rows composed per frame max 0
M tab-news@x@r3-2: worst 7.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-my@x@r3-2: worst 7.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-more@x@r3-2: worst 8.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 8.4 + next 8.3 (from the entry: 8.4, 8.3, 0.9, 0.8); rows composed per frame max 0
M tab-catalog@x@r3: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 9.5 + next 2.0 (from the entry: 9.5, 2.0, 8.1, 8.1); rows composed per frame max 0
M tab-home@x@r3-3: worst 9.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 68 (5 with >= 3 ms); entry frame 9.9 + next 9.9 (from the entry: 9.9, 9.9, 1.3, 1.2); rows composed per frame max 0
## E4. Round 4: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r4: worst 23.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 25 (5 with >= 3 ms); entry frame 9.6 + next 9.5 (from the entry: 9.6, 9.5, 2.0, 1.9); rows composed per frame max 0
M tab-my-cold@x@r4: worst 14.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 58 (1 with >= 3 ms); entry frame 14.1 + next 2.4 (from the entry: 14.1, 2.4, 2.4, 1.4); rows composed per frame max 0
M tab-more-cold@x@r4: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 36 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-catalog-cold@x@r4: worst 45.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 26 (4 with >= 3 ms); entry frame 14.2 + next 2.7 (from the entry: 14.2, 2.7, 2.7, 45.2); rows composed per frame max 0
M tab-home@x@r4-1: worst 8.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 55 (4 with >= 3 ms); entry frame 8.3 + next 1.4 (from the entry: 8.3, 1.4, 1.4, 6.8); rows composed per frame max 0
M tab-more@x@r4-1: worst 7.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (8 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-my@x@r4-1: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 13.3 + next 1.1 (from the entry: 13.3, 1.1, 1.1, 1.1); rows composed per frame max 0
M tab-news@x@r4-1: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (4 with >= 3 ms); entry frame 10.4 + next 1.8 (from the entry: 10.4, 1.8, 1.7, 11.9); rows composed per frame max 0
M tab-home@x@r4-2: worst 20.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 64 (8 with >= 3 ms); entry frame 9.3 + next 1.6 (from the entry: 9.3, 1.6, 1.5, 20.6); rows composed per frame max 0
M tab-news@x@r4-2: worst 8.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (7 with >= 3 ms); entry frame 8.6 + next 8.5 (from the entry: 8.6, 8.5, 1.9, 1.8); rows composed per frame max 0
M tab-my@x@r4-2: worst 12.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 12.4 + next 1.2 (from the entry: 12.4, 1.2, 1.0, 0.9); rows composed per frame max 0
M tab-more@x@r4-2: worst 11.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 11.7 + next 0.9 (from the entry: 11.7, 0.9, 6.0, 6.0); rows composed per frame max 0
M tab-catalog@x@r4: worst 9.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (3 with >= 3 ms); entry frame 8.7 + next 2.1 (from the entry: 8.7, 2.1, 2.1, 9.4); rows composed per frame max 0
M tab-home@x@r4-3: worst 8.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 60 (5 with >= 3 ms); entry frame 8.4 + next 8.3 (from the entry: 8.4, 8.3, 1.2, 1.1); rows composed per frame max 0
M tab-news-cold@base@r4: worst 15.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 34 (7 with >= 3 ms); entry frame 15.7 + next 15.7 (from the entry: 15.7, 15.7, 1.8, 1.7); rows composed per frame max 0
M tab-my-cold@base@r4: worst 7.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 56 (4 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-more-cold@base@r4: worst 10.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (7 with >= 3 ms); entry frame 10.9 + next 10.8 (from the entry: 10.9, 10.8, 1.8, 1.7); rows composed per frame max 0
M tab-catalog-cold@base@r4: worst 20.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 31 (8 with >= 3 ms); entry frame 10.0 + next 2.7 (from the entry: 10.0, 2.7, 2.6, 3.3); rows composed per frame max 0
M tab-home@base@r4-1: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 62 (3 with >= 3 ms); entry frame 11.5 + next 1.8 (from the entry: 11.5, 1.8, 1.7, 0.7); rows composed per frame max 0
M tab-more@base@r4-1: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 11.2 + next 11.1 (from the entry: 11.2, 11.1, 1.9, 1.8); rows composed per frame max 0
M tab-my@base@r4-1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (1 with >= 3 ms); entry frame 10.3 + next 2.4 (from the entry: 10.3, 2.4, 2.3, 0.5); rows composed per frame max 0
M tab-news@base@r4-1: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 15.6 + next 3.0 (from the entry: 15.6, 3.0, 2.9, 1.1); rows composed per frame max 0
M tab-home@base@r4-2: worst 17.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 67 (5 with >= 3 ms); entry frame 17.1 + next 1.1 (from the entry: 17.1, 1.1, 1.8, 1.8); rows composed per frame max 0
M tab-news@base@r4-2: worst 9.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (6 with >= 3 ms); entry frame 9.7 + next 9.5 (from the entry: 9.7, 9.5, 1.8, 1.7); rows composed per frame max 0
M tab-my@base@r4-2: worst 10.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (3 with >= 3 ms); entry frame 10.1 + next 1.0 (from the entry: 10.1, 1.0, 1.6, 1.6); rows composed per frame max 0
M tab-more@base@r4-2: worst 17.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 20 (2 with >= 3 ms); entry frame 17.2 + next 5.1 (from the entry: 17.2, 5.1, 1.4, 1.3); rows composed per frame max 0
M tab-catalog@base@r4: worst 13.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (4 with >= 3 ms); entry frame 13.1 + next 1.7 (from the entry: 13.1, 1.7, 7.8, 1.9); rows composed per frame max 0
M tab-home@base@r4-3: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 59 (4 with >= 3 ms); entry frame 11.9 + next 1.1 (from the entry: 11.9, 1.1, 1.4, 1.3); rows composed per frame max 0
## E5. Round 5: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r5: worst 20.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 31 (13 with >= 3 ms); entry frame 10.2 + next 10.2 (from the entry: 10.2, 10.2, 20.1, 20.0); rows composed per frame max 0
M tab-my-cold@base@r5: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 55 (5 with >= 3 ms); entry frame 15.5 + next 2.4 (from the entry: 15.5, 2.4, 2.4, 1.4); rows composed per frame max 0
M tab-more-cold@base@r5: worst 9.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 32 (2 with >= 3 ms); entry frame 9.6 + next 9.5 (from the entry: 9.6, 9.5, 1.7, 1.7); rows composed per frame max 0
M tab-catalog-cold@base@r5: worst 27.4 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 28 (5 with >= 3 ms); entry frame 9.3 + next 3.0 (from the entry: 9.3, 3.0, 2.9, 27.4); rows composed per frame max 0
M tab-home@base@r5-1: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 62 (5 with >= 3 ms); entry frame 13.8 + next 2.1 (from the entry: 13.8, 2.1, 2.0, 0.8); rows composed per frame max 0
M tab-more@base@r5-1: worst 13.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (5 with >= 3 ms); entry frame 13.5 + next 13.4 (from the entry: 13.5, 13.4, 2.1, 2.1); rows composed per frame max 0
M tab-my@base@r5-1: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (3 with >= 3 ms); entry frame 9.5 + next 1.8 (from the entry: 9.5, 1.8, 1.8, 0.7); rows composed per frame max 0
M tab-news@base@r5-1: worst 14.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 14.5 + next 2.3 (from the entry: 14.5, 2.3, 2.2, 0.8); rows composed per frame max 0
M tab-home@base@r5-2: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 66 (3 with >= 3 ms); entry frame 13.0 + next 1.1 (from the entry: 13.0, 1.1, 1.3, 1.2); rows composed per frame max 0
M tab-news@base@r5-2: worst 14.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (6 with >= 3 ms); entry frame 14.6 + next 14.5 (from the entry: 14.6, 14.5, 1.8, 1.7); rows composed per frame max 0
M tab-my@base@r5-2: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-more@base@r5-2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (1 with >= 3 ms); entry frame 15.6 + next 1.4 (from the entry: 15.6, 1.4, 1.4, 1.2); rows composed per frame max 0
M tab-catalog@base@r5: worst 11.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 11.8 + next 11.8 (from the entry: 11.8, 11.8, 1.5, 1.4); rows composed per frame max 0
M tab-home@base@r5-3: worst 11.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 56 (5 with >= 3 ms); entry frame 11.7 + next 4.1 (from the entry: 11.7, 4.1, 4.0, 1.5); rows composed per frame max 0
M tab-news-cold@x@r5: worst 18.6 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 23 (6 with >= 3 ms); entry frame 18.6 + next 18.5 (from the entry: 18.6, 18.5, 1.7, 1.5); rows composed per frame max 0
M tab-my-cold@x@r5: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 55 (3 with >= 3 ms); entry frame 15.3 + next 3.3 (from the entry: 15.3, 3.3, 3.3, 1.5); rows composed per frame max 0
M tab-more-cold@x@r5: worst 8.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (3 with >= 3 ms); entry frame 8.1 + next 8.1 (from the entry: 8.1, 8.1, 1.8, 1.7); rows composed per frame max 0
M tab-catalog-cold@x@r5: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 29 (7 with >= 3 ms); entry frame 11.2 + next 2.0 (from the entry: 11.2, 2.0, 1.9, 3.1); rows composed per frame max 0
M tab-home@x@r5-1: worst 16.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 60 (3 with >= 3 ms); entry frame 16.7 + next 1.5 (from the entry: 16.7, 1.5, 1.4, 3.3); rows composed per frame max 0
M tab-more@x@r5-1: worst 8.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (11 with >= 3 ms); entry frame 8.3 + next 8.2 (from the entry: 8.3, 8.2, 4.9, 4.9); rows composed per frame max 0
M tab-my@x@r5-1: worst 6.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 32 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@x@r5-1: worst 16.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 20 (7 with >= 3 ms); entry frame 16.7 + next 1.7 (from the entry: 16.7, 1.7, 1.7, 4.0); rows composed per frame max 0
M tab-home@x@r5-2: worst 11.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 60 (5 with >= 3 ms); entry frame 8.7 + next 1.3 (from the entry: 8.7, 1.3, 11.3, 11.2); rows composed per frame max 0
M tab-news@x@r5-2: worst 12.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 12.8 + next 12.8 (from the entry: 12.8, 12.8, 1.6, 1.5); rows composed per frame max 0
M tab-my@x@r5-2: worst 10.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 10.2 + next 1.2 (from the entry: 10.2, 1.2, 1.2, 1.1); rows composed per frame max 0
M tab-more@x@r5-2: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 11.9 + next 1.8 (from the entry: 11.9, 1.8, 5.3, 5.3); rows composed per frame max 0
M tab-catalog@x@r5: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 14.2 + next 2.4 (from the entry: 14.2, 2.4, 2.3, 14.9); rows composed per frame max 0
M tab-home@x@r5-3: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 62 (6 with >= 3 ms); entry frame 9.6 + next 1.7 (from the entry: 9.6, 1.7, 1.6, 15.5); rows composed per frame max 0
## EV. Visual: the tab entries recorded at 8 frames/s
REC tab-news-base (emu): marker 1959 ms after the recording start; 48 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-base (emu): marker 1547 ms after the recording start; 45 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-base (emu): marker 1536 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-news-x (emu): marker 1573 ms after the recording start; 47 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-x (emu): marker 1538 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-x (emu): marker 1537 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
tab-news-cold | base | 5 | 13.8 / 13.3 / 20.1 | 10.2 | 10.2 | 0 | 0 | 0 | 89
tab-news-cold | x | 5 | 15.1 / 15.2 / 23.1 | 11.2 | 11.1 | 0 | 0 | 0 | 78
tab-my-cold | base | 5 | 15.5 / 15.6 / 20.4 | 15.5 | 2.6 | 0 | 0 | 0 | 57
tab-my-cold | x | 5 | 13.8 / 13.7 / 15.3 | 13.8 | 2.8 | 0 | 0 | 0 | 55
tab-more-cold | base | 5 | 15.5 / 14.3 / 18.1 | 15.5 | 15.4 | 0 | 0 | 0 | 75
tab-more-cold | x | 5 | 9.5 / 9.5 / 11.4 | 9.5 | 9.4 | 0 | 0 | 0 | 68
tab-catalog-cold | base | 5 | 17.9 / 19.0 / 27.4 | 9.3 | 2.6 | 2 | 0 | 0 | 84
tab-catalog-cold | x | 5 | 18.5 / 22.6 / 45.2 | 14.2 | 2.7 | 1 | 1 | 0 | 80
tab-home | base | 15 | 11.9 / 13.0 / 18.4 | 11.9 | 1.8 | 0 | 0 | 0 | 48
tab-home | x | 15 | 10.5 / 12.1 / 20.6 | 9.6 | 1.8 | 0 | 0 | 0 | 58
tab-more | base | 10 | 14.6 / 15.7 / 24.5 | 14.6 | 7.5 | 0 | 0 | 0 | 47
tab-more | x | 10 | 9.0 / 10.6 / 19.7 | 9.0 | 1.7 | 0 | 0 | 0 | 49
tab-my | base | 10 | 9.8 / 9.1 / 11.4 | 9.8 | 1.4 | 0 | 0 | 0 | 33
tab-my | x | 10 | 7.8 / 8.8 / 13.3 | 4.1 | 0.6 | 0 | 0 | 0 | 35
tab-news | base | 10 | 11.3 / 12.1 / 15.6 | 10.8 | 7.0 | 0 | 0 | 0 | 43
tab-news | x | 10 | 11.1 / 11.4 / 17.6 | 10.4 | 3.2 | 0 | 0 | 0 | 50
tab-catalog | base | 5 | 13.1 / 12.5 / 15.6 | 11.8 | 1.9 | 0 | 0 | 0 | 46
tab-catalog | x | 5 | 13.2 / 12.7 / 16.6 | 13.2 | 2.0 | 0 | 0 | 0 | 58
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 2
contact sheets: 20 in sheets/
contact sheets: 20 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4366', '5946', '6802', '7330', '8950', '9506', '11067', '11760']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=72 total=769
== Davey: 32
   davey events=32 max=1655ms median=938ms
== NativeLink: 8
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## fty33

```
part fty33; APK: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
IME: default com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME; enabled: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME com.google.android.tts/com.google.android.apps.speech.tts.googletts.settings.asr.voiceime.VoiceInputMethodService org.pc.helper/.Ime 
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 33
host cpu:  AMD EPYC 7763 64-Core Processor
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 6774 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~OHpeo_MzqrotSmAYBgsPeg==/org.chyavorec.app-GIMguktHgbeg6Gn1VnPsuw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 3590 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~g4lzowcV5xfNvUqWXUSPGw==/org.chyavorec.app.b-l9t5WlXqnuVpmNJyUsfqMw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/LiveWallpapersPicker/LiveWallpapersPicker.apk| x86_64: [status=verify] [reason=prebuilt]|
tabs: catalog [320 2274] home [99 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
IME now: org.pc.helper/.Ime
## T1. Round 1: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@base@r1-1: worst 20.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 28 (8 with >= 3 ms); entry frame 20.7 + next 0.7 (from the entry: 20.7, 0.7, 1.2, 4.2); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r1-1: worst 22.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 22.4 + next 0.3 (from the entry: 22.4, 0.3, 0.3, 0.3); rows composed per frame max 0
M type@base@r1-2: worst 25.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 24 (7 with >= 3 ms); entry frame 25.0 + next 0.3 (from the entry: 25.0, 0.3, 6.7, 12.8); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r1-2: worst 26.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 14 (2 with >= 3 ms); entry frame 26.5 + next 1.1 (from the entry: 26.5, 1.1, 2.9, 1.1); rows composed per frame max 0
M type@base@r1-3: worst 30.0 ms CPU (rows composed in it 0); frames >16 4, >25 1, >30 1 of 27 (6 with >= 3 ms); entry frame 21.3 + next 0.2 (from the entry: 21.3, 0.2, 1.4, 9.0); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r1-3: worst 22.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 22.2 + next 1.2 (from the entry: 22.2, 1.2, 0.8, 1.2); rows composed per frame max 0
M sort-open@base@r1: worst 10.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (1 with >= 3 ms); entry frame 10.9 + next 1.4 (from the entry: 10.9, 1.4, 1.5, 0.3); rows composed per frame max 0
M sort-year@base@r1: worst 24.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 24.9 + next 0.3 (from the entry: 24.9, 0.3, 0.5, 5.3); rows composed per frame max 0
M sort-open@base@r1-2: worst 5.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 30 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@base@r1: worst 11.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (3 with >= 3 ms); entry frame 11.0 + next 0.2 (from the entry: 11.0, 0.2, 0.9, 3.5); rows composed per frame max 0
  load-more button at [540 1896] after 9 swipes (base)
M loadmore@base@r1: worst 4.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r1: worst 65.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 68 (5 with >= 3 ms); entry frame 13.5 + next 7.8 (from the entry: 13.5, 7.8, 2.1, 0.2); rows composed per frame max 0
M drag@base@r1: worst 0.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 154 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x@r1-1: worst 33.4 ms CPU (rows composed in it 0); frames >16 3, >25 1, >30 1 of 34 (11 with >= 3 ms); entry frame 20.9 + next 0.4 (from the entry: 20.9, 0.4, 0.3, 3.5); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r1-1: worst 23.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 23.2 + next 0.4 (from the entry: 23.2, 0.4, 0.3, 0.4); rows composed per frame max 0
M type@x@r1-2: worst 30.7 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 1 of 31 (6 with >= 3 ms); entry frame 26.2 + next 0.3 (from the entry: 26.2, 0.3, 0.3, 8.1); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r1-2: worst 29.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 17 (2 with >= 3 ms); entry frame 29.0 + next 1.5 (from the entry: 29.0, 1.5, 0.9, 0.7); rows composed per frame max 0
M type@x@r1-3: worst 28.2 ms CPU (rows composed in it 0); frames >16 3, >25 1, >30 0 of 30 (10 with >= 3 ms); entry frame 28.2 + next 0.4 (from the entry: 28.2, 0.4, 7.1, 0.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r1-3: worst 35.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 17 (2 with >= 3 ms); entry frame 35.1 + next 0.4 (from the entry: 35.1, 0.4, 0.3, 0.4); rows composed per frame max 0
M sort-open@x@r1: worst 8.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (1 with >= 3 ms); entry frame 8.4 + next 1.1 (from the entry: 8.4, 1.1, 1.8, 0.6); rows composed per frame max 0
M sort-year@x@r1: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 15.5 + next 0.2 (from the entry: 15.5, 0.2, 0.3, 4.0); rows composed per frame max 0
M sort-open@x@r1-2: worst 5.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 38 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r1: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 13.3 + next 0.3 (from the entry: 13.3, 0.3, 0.3, 0.9); rows composed per frame max 0
  load-more button at [540 1896] after 12 swipes (x)
M loadmore@x@r1: worst 4.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r1: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 79 (15 with >= 3 ms); entry frame 11.2 + next 5.0 (from the entry: 11.2, 5.0, 6.2, 0.2); rows composed per frame max 0
M drag@x@r1: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (2 with >= 3 ms); entry frame 9.5 + next 1.5 (from the entry: 9.5, 1.5, 1.9, 1.2); rows composed per frame max 0
## T2. Round 2: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@x@r2-1: worst 41.9 ms CPU (rows composed in it 0); frames >16 4, >25 3, >30 2 of 34 (9 with >= 3 ms); entry frame 37.4 + next 0.4 (from the entry: 37.4, 0.4, 0.4, 7.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r2-1: worst 31.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (2 with >= 3 ms); entry frame 31.8 + next 0.4 (from the entry: 31.8, 0.4, 0.3, 0.3); rows composed per frame max 0
M type@x@r2-2: worst 25.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 31 (9 with >= 3 ms); entry frame 13.2 + next 0.3 (from the entry: 13.2, 0.3, 12.0, 0.5); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r2-2: worst 26.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 16 (2 with >= 3 ms); entry frame 26.6 + next 1.4 (from the entry: 26.6, 1.4, 0.8, 2.7); rows composed per frame max 0
M type@x@r2-3: worst 34.6 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 29 (10 with >= 3 ms); entry frame 24.2 + next 2.8 (from the entry: 24.2, 2.8, 6.7, 3.6); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r2-3: worst 45.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 17 (2 with >= 3 ms); entry frame 45.1 + next 1.4 (from the entry: 45.1, 1.4, 0.9, 1.3); rows composed per frame max 0
M sort-open@x@r2: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 12.0 + next 1.4 (from the entry: 12.0, 1.4, 2.8, 0.8); rows composed per frame max 0
M sort-year@x@r2: worst 22.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 22.2 + next 0.6 (from the entry: 22.2, 0.6, 0.3, 0.8); rows composed per frame max 0
M sort-open@x@r2-2: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r2: worst 15.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 15.9 + next 0.5 (from the entry: 15.9, 0.5, 0.6, 0.3); rows composed per frame max 0
  load-more button at [540 1896] after 9 swipes (x)
M loadmore@x@r2: worst 6.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r2: worst 17.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 78 (14 with >= 3 ms); entry frame 10.1 + next 3.7 (from the entry: 10.1, 3.7, 6.0, 0.2); rows composed per frame max 0
M drag@x@r2: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@base@r2-1: worst 39.5 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 1 of 25 (8 with >= 3 ms); entry frame 22.2 + next 0.3 (from the entry: 22.2, 0.3, 0.5, 11.7); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r2-1: worst 31.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (2 with >= 3 ms); entry frame 31.5 + next 0.3 (from the entry: 31.5, 0.3, 0.5, 0.5); rows composed per frame max 0
M type@base@r2-2: worst 36.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 25 (8 with >= 3 ms); entry frame 14.0 + next 0.2 (from the entry: 14.0, 0.2, 3.3, 36.2); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r2-2: worst 19.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (2 with >= 3 ms); entry frame 19.4 + next 0.4 (from the entry: 19.4, 0.4, 0.3, 0.3); rows composed per frame max 0
M type@base@r2-3: worst 32.1 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 2 of 26 (7 with >= 3 ms); entry frame 12.3 + next 0.3 (from the entry: 12.3, 0.3, 0.4, 6.6); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r2-3: worst 16.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (3 with >= 3 ms); entry frame 16.2 + next 3.5 (from the entry: 16.2, 3.5, 1.0, 1.4); rows composed per frame max 0
M sort-open@base@r2: worst 23.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (1 with >= 3 ms); entry frame 23.3 + next 1.4 (from the entry: 23.3, 1.4, 1.6, 0.4); rows composed per frame max 0
M sort-year@base@r2: worst 30.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 15 (2 with >= 3 ms); entry frame 30.0 + next 0.4 (from the entry: 30.0, 0.4, 0.4, 0.4); rows composed per frame max 0
M sort-open@base@r2-2: worst 13.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (1 with >= 3 ms); entry frame 13.2 + next 1.7 (from the entry: 13.2, 1.7, 1.8, 0.4); rows composed per frame max 0
M sort-rel@base@r2: worst 23.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 23.2 + next 0.3 (from the entry: 23.2, 0.3, 0.4, 1.0); rows composed per frame max 0
  load-more button at [540 1896] after 11 swipes (base)
M loadmore@base@r2: worst 4.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r2: worst 33.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 79 (8 with >= 3 ms); entry frame 12.9 + next 5.6 (from the entry: 12.9, 5.6, 8.0, 0.2); rows composed per frame max 0
M drag@base@r2: worst 3.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
## T3. Round 3: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@base@r3-1: worst 35.3 ms CPU (rows composed in it 0); frames >16 4, >25 2, >30 1 of 28 (9 with >= 3 ms); entry frame 35.3 + next 0.4 (from the entry: 35.3, 0.4, 0.4, 7.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r3-1: worst 14.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 14.8 + next 0.3 (from the entry: 14.8, 0.3, 0.4, 0.4); rows composed per frame max 0
M type@base@r3-2: worst 36.6 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 26 (7 with >= 3 ms); entry frame 16.2 + next 0.3 (from the entry: 16.2, 0.3, 5.9, 0.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r3-2: worst 24.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (3 with >= 3 ms); entry frame 24.8 + next 7.6 (from the entry: 24.8, 7.6, 0.9, 1.4); rows composed per frame max 0
M type@base@r3-3: worst 39.1 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 27 (8 with >= 3 ms); entry frame 19.3 + next 0.4 (from the entry: 19.3, 0.4, 5.8, 0.5); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r3-3: worst 29.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 15 (3 with >= 3 ms); entry frame 29.8 + next 3.5 (from the entry: 29.8, 3.5, 1.2, 1.5); rows composed per frame max 0
M sort-open@base@r3: worst 10.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 29 (1 with >= 3 ms); entry frame 10.8 + next 1.2 (from the entry: 10.8, 1.2, 1.8, 0.6); rows composed per frame max 0
M sort-year@base@r3: worst 18.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 18.8 + next 0.4 (from the entry: 18.8, 0.4, 0.4, 0.9); rows composed per frame max 0
M sort-open@base@r3-2: worst 7.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@base@r3: worst 17.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (2 with >= 3 ms); entry frame 10.0 + next 1.1 (from the entry: 10.0, 1.1, 17.8, 0.3); rows composed per frame max 0
  load-more button at [540 1896] after 10 swipes (base)
M loadmore@base@r3: worst 4.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r3: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 83 (17 with >= 3 ms); entry frame 11.7 + next 7.0 (from the entry: 11.7, 7.0, 5.4, 0.2); rows composed per frame max 0
M drag@base@r3: worst 7.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (4 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x@r3-1: worst 32.2 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 2 of 33 (11 with >= 3 ms); entry frame 31.9 + next 0.4 (from the entry: 31.9, 0.4, 0.5, 7.2); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r3-1: worst 34.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (3 with >= 3 ms); entry frame 34.0 + next 0.4 (from the entry: 34.0, 0.4, 0.3, 0.5); rows composed per frame max 0
M type@x@r3-2: worst 29.3 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 28 (9 with >= 3 ms); entry frame 29.3 + next 0.5 (from the entry: 29.3, 0.5, 4.8, 0.6); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r3-2: worst 28.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 18 (2 with >= 3 ms); entry frame 28.9 + next 1.1 (from the entry: 28.9, 1.1, 1.0, 0.8); rows composed per frame max 0
M type@x@r3-3: worst 43.0 ms CPU (rows composed in it 0); frames >16 3, >25 3, >30 2 of 27 (9 with >= 3 ms); entry frame 43.0 + next 6.1 (from the entry: 43.0, 6.1, 3.4, 11.2); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r3-3: worst 39.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (3 with >= 3 ms); entry frame 39.6 + next 3.1 (from the entry: 39.6, 3.1, 1.0, 1.2); rows composed per frame max 0
M sort-open@x@r3: worst 8.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (1 with >= 3 ms); entry frame 8.8 + next 2.2 (from the entry: 8.8, 2.2, 2.8, 0.7); rows composed per frame max 0
M sort-year@x@r3: worst 14.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 14.4 + next 0.4 (from the entry: 14.4, 0.4, 0.4, 0.3); rows composed per frame max 0
M sort-open@x@r3-2: worst 7.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 37 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r3: worst 22.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 13.2 + next 1.3 (from the entry: 13.2, 1.3, 22.4, 0.8); rows composed per frame max 0
  load-more button at [540 1896] after 10 swipes (x)
M loadmore@x@r3: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 12.0 + next 0.4 (from the entry: 12.0, 0.4, 0.4, 0.3); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r3: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 91 (15 with >= 3 ms); entry frame 12.2 + next 9.3 (from the entry: 12.2, 9.3, 3.5, 0.5); rows composed per frame max 0
M drag@x@r3: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
## TV. Visual: typing a word, recorded
REC type-base (emu): marker 1556 ms after the recording start; 53 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC type-x (emu): marker 1557 ms after the recording start; 52 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
IME now: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
type | base | 9 | 35.3 / 32.7 / 39.5 | 20.7 | 0.3 | 11 | 8 | 0 | 110
type | x | 9 | 32.2 / 33.2 / 43.0 | 28.2 | 0.4 | 16 | 9 | 0 | 126
clear | base | 9 | 22.4 / 23.1 / 31.5 | 22.4 | 1.1 | 3 | 1 | 0 | 34
clear | x | 9 | 31.8 / 32.6 / 45.1 | 31.8 | 1.1 | 8 | 5 | 0 | 42
sort-open | base | 6 | 10.9 / 11.9 / 23.3 | 10.9 | 1.3 | 0 | 0 | 0 | 19
sort-open | x | 6 | 8.1 / 8.3 / 12.0 | 4.2 | 0.6 | 0 | 0 | 0 | 16
sort-year | base | 3 | 24.9 / 24.6 / 30.0 | 24.9 | 0.4 | 1 | 0 | 0 | 39
sort-year | x | 3 | 15.5 / 17.4 / 22.2 | 15.5 | 0.4 | 0 | 0 | 0 | 29
sort-rel | base | 3 | 17.8 / 17.3 / 23.2 | 11.0 | 0.3 | 0 | 0 | 0 | 33
sort-rel | x | 3 | 15.9 / 17.2 / 22.4 | 13.3 | 0.5 | 0 | 0 | 0 | 27
loadmore | base | 3 | 4.1 / 4.2 / 4.4 | 0.0 | 0.0 | 0 | 0 | 0 | 8
loadmore | x | 3 | 6.2 / 7.7 / 12.0 | 0.0 | 0.0 | 0 | 0 | 0 | 10
fling | base | 3 | 33.8 / 38.1 / 65.0 | 12.9 | 7.0 | 2 | 2 | 0 | 130
fling | x | 3 | 12.2 / 13.6 / 17.5 | 11.2 | 5.0 | 0 | 0 | 0 | 123
drag | base | 3 | 3.6 / 3.7 / 7.1 | 0.0 | 0.0 | 0 | 0 | 0 | 30
drag | x | 3 | 7.7 / 8.0 / 9.5 | 0.0 | 0.0 | 0 | 0 | 0 | 31
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 31
contact sheets: 8 in sheets/
contact sheets: 8 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5518', '8182', '9608', '10733', '13853', '14962']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 38
   skipped-frames events=38 max=193 total=2191
== Davey: 95
   davey events=95 max=4801ms median=948ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 1 block(s) {'FATAL': 1} → crashes.txt
```

## fty36x

```
part fty36x; APK: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 ed441ba prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687414 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
IME: default com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME; enabled: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME com.google.android.tts/com.google.android.apps.speech.tts.googletts.settings.asr.voiceime.VoiceInputMethodService org.pc.helper/.Ime 
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 36
host cpu:  AMD EPYC 9V74 80-Core Processor
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 3892 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~8KdOeC3NT5x-1YzJO11xzg==/org.chyavorec.app-5-X-SKTuEMq-9sQzPFJiIg==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~WUbbxw1UGyg-AKwMAm-DPQ==/org.chyavorec.app.b-DutKQb3BPMPDwU5mMiuAPQ==/base.apk| x86_64: [status=verify] [reason=install] [primary-abi]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 2658 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~WUbbxw1UGyg-AKwMAm-DPQ==/org.chyavorec.app.b-DutKQb3BPMPDwU5mMiuAPQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~8it9u4Z9DsDmik7pnivGzA==/org.pc.helper-IO6LOl7EvLxoK54NOUemXw==/base.apk| x86_64: [status=run-from-apk] [reason=unknown] [primary-abi]|
tabs: catalog [320 2274] home [100 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
IME now: org.pc.helper/.Ime
## T1. Round 1: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@base@r1-1: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 46 (12 with >= 3 ms); entry frame 12.0 + next 11.9 (from the entry: 12.0, 11.9, 3.0, 2.9); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r1-1: worst 8.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (1 with >= 3 ms); entry frame 8.6 + next 0.4 (from the entry: 8.6, 0.4, 0.3, 0.5); rows composed per frame max 0
M type@base@r1-2: worst 21.5 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 39 (9 with >= 3 ms); entry frame 21.5 + next 21.2 (from the entry: 21.5, 21.2, 1.2, 1.1); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r1-2: worst 11.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 11.6 + next 11.5 (from the entry: 11.6, 11.5, 1.3, 1.2); rows composed per frame max 0
M type@base@r1-3: worst 21.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 42 (7 with >= 3 ms); entry frame 21.0 + next 20.6 (from the entry: 21.0, 20.6, 2.7, 2.6); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r1-3: worst 25.7 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 26 (3 with >= 3 ms); entry frame 25.7 + next 25.5 (from the entry: 25.7, 25.5, 0.9, 0.9); rows composed per frame max 0
M sort-open@base@r1: worst 8.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 38 (1 with >= 3 ms); entry frame 8.3 + next 1.4 (from the entry: 8.3, 1.4, 1.3, 1.7); rows composed per frame max 0
M sort-year@base@r1: worst 26.7 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 23 (3 with >= 3 ms); entry frame 26.7 + next 26.5 (from the entry: 26.7, 26.5, 0.3, 0.2); rows composed per frame max 0
M sort-open@base@r1-2: worst 7.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 30 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@base@r1: worst 10.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (3 with >= 3 ms); entry frame 10.0 + next 1.9 (from the entry: 10.0, 1.9, 1.7, 1.3); rows composed per frame max 0
  load-more button at [540 1896] after 11 swipes (base)
M loadmore@base@r1: worst 4.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r1: worst 9.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 122 (11 with >= 3 ms); entry frame 8.1 + next 1.6 (from the entry: 8.1, 1.6, 9.2, 9.1); rows composed per frame max 0
M drag@base@r1: worst 6.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 41 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x@r1-1: worst 13.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 54 (11 with >= 3 ms); entry frame 8.7 + next 0.6 (from the entry: 8.7, 0.6, 0.7, 1.0); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r1-1: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (4 with >= 3 ms); entry frame 11.2 + next 0.5 (from the entry: 11.2, 0.5, 0.4, 0.5); rows composed per frame max 0
M type@x@r1-2: worst 32.2 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 2 of 43 (10 with >= 3 ms); entry frame 18.4 + next 0.3 (from the entry: 18.4, 0.3, 0.4, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r1-2: worst 12.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 12.6 + next 12.4 (from the entry: 12.6, 12.4, 0.5, 0.5); rows composed per frame max 0
M type@x@r1-3: worst 26.7 ms CPU (rows composed in it 0); frames >16 3, >25 1, >30 0 of 51 (12 with >= 3 ms); entry frame 26.7 + next 0.4 (from the entry: 26.7, 0.4, 0.3, 3.7); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r1-3: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 10.3 + next 10.2 (from the entry: 10.3, 10.2, 0.5, 0.4); rows composed per frame max 0
M sort-open@x@r1: worst 7.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x@r1: worst 13.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 28 (3 with >= 3 ms); entry frame 13.4 + next 13.3 (from the entry: 13.4, 13.3, 0.5, 0.4); rows composed per frame max 0
M sort-open@x@r1-2: worst 6.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r1: worst 16.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 16.1 + next 16.1 (from the entry: 16.1, 16.1, 0.5, 0.3); rows composed per frame max 0
  load-more button at [540 1896] after 12 swipes (x)
M loadmore@x@r1: worst 3.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r1: worst 22.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 127 (11 with >= 3 ms); entry frame 22.4 + next 3.6 (from the entry: 22.4, 3.6, 3.6, 0.5); rows composed per frame max 0
M drag@x@r1: worst 3.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 41 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
## T2. Round 2: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@x@r2-1: worst 18.6 ms CPU (rows composed in it 0); frames >16 4, >25 0, >30 0 of 57 (12 with >= 3 ms); entry frame 13.5 + next 0.3 (from the entry: 13.5, 0.3, 0.4, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r2-1: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (1 with >= 3 ms); entry frame 10.7 + next 0.5 (from the entry: 10.7, 0.5, 0.3, 0.5); rows composed per frame max 0
M type@x@r2-2: worst 25.3 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 47 (7 with >= 3 ms); entry frame 13.2 + next 0.3 (from the entry: 13.2, 0.3, 0.5, 0.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r2-2: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (5 with >= 3 ms); entry frame 9.9 + next 9.8 (from the entry: 9.9, 9.8, 10.7, 9.1); rows composed per frame max 0
M type@x@r2-3: worst 29.2 ms CPU (rows composed in it 0); frames >16 5, >25 1, >30 0 of 52 (13 with >= 3 ms); entry frame 29.2 + next 0.5 (from the entry: 29.2, 0.5, 0.4, 0.5); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r2-3: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (3 with >= 3 ms); entry frame 12.9 + next 12.7 (from the entry: 12.9, 12.7, 1.4, 1.2); rows composed per frame max 0
M sort-open@x@r2: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 44 (1 with >= 3 ms); entry frame 10.3 + next 1.4 (from the entry: 10.3, 1.4, 1.3, 1.9); rows composed per frame max 0
M sort-year@x@r2: worst 21.9 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 21.9 + next 21.8 (from the entry: 21.9, 21.8, 0.4, 0.3); rows composed per frame max 0
M sort-open@x@r2-2: worst 5.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r2: worst 17.5 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 17.5 + next 17.4 (from the entry: 17.5, 17.4, 0.4, 0.3); rows composed per frame max 0
  load-more button at [540 1896] after 10 swipes (x)
M loadmore@x@r2: worst 7.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r2: worst 10.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 121 (12 with >= 3 ms); entry frame 10.4 + next 2.1 (from the entry: 10.4, 2.1, 3.0, 2.8); rows composed per frame max 0
M drag@x@r2: worst 1.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 43 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@base@r2-1: worst 31.3 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 2 of 49 (11 with >= 3 ms); entry frame 13.2 + next 0.2 (from the entry: 13.2, 0.2, 0.4, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r2-1: worst 10.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (2 with >= 3 ms); entry frame 10.6 + next 0.5 (from the entry: 10.6, 0.5, 0.3, 0.4); rows composed per frame max 0
M type@base@r2-2: worst 13.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 42 (6 with >= 3 ms); entry frame 13.1 + next 0.4 (from the entry: 13.1, 0.4, 0.4, 0.6); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r2-2: worst 13.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 13.7 + next 13.6 (from the entry: 13.7, 13.6, 1.0, 1.0); rows composed per frame max 0
M type@base@r2-3: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 44 (11 with >= 3 ms); entry frame 9.9 + next 2.3 (from the entry: 9.9, 2.3, 0.4, 0.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r2-3: worst 16.3 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 16.3 + next 16.2 (from the entry: 16.3, 16.2, 1.4, 1.3); rows composed per frame max 0
M sort-open@base@r2: worst 10.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 10.8 + next 1.2 (from the entry: 10.8, 1.2, 1.1, 1.8); rows composed per frame max 0
M sort-year@base@r2: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 15.5 + next 15.4 (from the entry: 15.5, 15.4, 0.4, 0.3); rows composed per frame max 0
M sort-open@base@r2-2: worst 5.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@base@r2: worst 12.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 12.6 + next 12.5 (from the entry: 12.6, 12.5, 0.4, 0.3); rows composed per frame max 0
  load-more button at [540 2019] after 10 swipes (base)
M loadmore@base@r2: worst 2.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r2: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 121 (5 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M drag@base@r2: worst 3.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
## T3. Round 3: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@base@r3-1: worst 28.4 ms CPU (rows composed in it 0); frames >16 4, >25 2, >30 0 of 44 (9 with >= 3 ms); entry frame 14.6 + next 0.2 (from the entry: 14.6, 0.2, 0.4, 0.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r3-1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (1 with >= 3 ms); entry frame 10.3 + next 0.2 (from the entry: 10.3, 0.2, 0.6, 0.4); rows composed per frame max 0
M type@base@r3-2: worst 31.1 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 2 of 39 (7 with >= 3 ms); entry frame 18.2 + next 0.4 (from the entry: 18.2, 0.4, 0.3, 2.8); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r3-2: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (5 with >= 3 ms); entry frame 11.5 + next 11.4 (from the entry: 11.5, 11.4, 4.4, 4.3); rows composed per frame max 0
M type@base@r3-3: worst 20.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 45 (11 with >= 3 ms); entry frame 20.0 + next 0.2 (from the entry: 20.0, 0.2, 0.4, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r3-3: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 9.4 + next 1.3 (from the entry: 9.4, 1.3, 1.0, 0.9); rows composed per frame max 0
M sort-open@base@r3: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 53 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@base@r3: worst 18.9 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 21 (4 with >= 3 ms); entry frame 18.9 + next 18.8 (from the entry: 18.9, 18.8, 0.3, 0.3); rows composed per frame max 0
M sort-open@base@r3-2: worst 9.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (4 with >= 3 ms); entry frame 9.2 + next 3.3 (from the entry: 9.2, 3.3, 2.9, 4.0); rows composed per frame max 0
M sort-rel@base@r3: worst 19.8 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 22 (3 with >= 3 ms); entry frame 19.8 + next 19.6 (from the entry: 19.8, 19.6, 0.4, 0.3); rows composed per frame max 0
  load-more button at [540 1896] after 9 swipes (base)
M loadmore@base@r3: worst 3.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r3: worst 6.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 115 (10 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M drag@base@r3: worst 5.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 40 (4 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x@r3-1: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 60 (14 with >= 3 ms); entry frame 11.4 + next 0.7 (from the entry: 11.4, 0.7, 0.5, 3.6); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r3-1: worst 10.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (3 with >= 3 ms); entry frame 10.1 + next 0.5 (from the entry: 10.1, 0.5, 0.4, 0.5); rows composed per frame max 0
M type@x@r3-2: worst 17.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 49 (6 with >= 3 ms); entry frame 17.2 + next 0.2 (from the entry: 17.2, 0.2, 0.4, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r3-2: worst 17.8 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 17.8 + next 17.7 (from the entry: 17.8, 17.7, 1.0, 0.9); rows composed per frame max 0
M type@x@r3-3: worst 16.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 51 (13 with >= 3 ms); entry frame 13.2 + next 0.3 (from the entry: 13.2, 0.3, 0.5, 0.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r3-3: worst 20.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 20.1 + next 19.9 (from the entry: 20.1, 19.9, 1.2, 1.1); rows composed per frame max 0
M sort-open@x@r3: worst 13.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 40 (1 with >= 3 ms); entry frame 13.1 + next 1.4 (from the entry: 13.1, 1.4, 1.2, 1.8); rows composed per frame max 0
M sort-year@x@r3: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 12.2 + next 12.1 (from the entry: 12.2, 12.1, 0.4, 0.3); rows composed per frame max 0
M sort-open@x@r3-2: worst 6.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r3: worst 16.4 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 16.4 + next 16.3 (from the entry: 16.4, 16.3, 0.4, 0.3); rows composed per frame max 0
  load-more button at [540 1896] after 10 swipes (x)
M loadmore@x@r3: worst 3.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r3: worst 26.5 ms CPU (rows composed in it 0); frames >16 8, >25 4, >30 0 of 124 (25 with >= 3 ms); entry frame 8.6 + next 1.2 (from the entry: 8.6, 1.2, 3.1, 3.1); rows composed per frame max 0
M drag@x@r3: worst 3.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 66 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
## TV. Visual: typing a word, recorded
REC type-base (emu): marker 1540 ms after the recording start; 53 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC type-x (emu): marker 1540 ms after the recording start; 52 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
IME now: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
type | base | 9 | 21.0 / 21.1 / 31.3 | 14.6 | 0.4 | 6 | 4 | 0 | 118
type | x | 9 | 18.6 / 21.6 / 32.2 | 13.5 | 0.3 | 6 | 2 | 0 | 135
clear | base | 9 | 11.6 / 13.4 / 25.7 | 11.5 | 11.4 | 2 | 0 | 0 | 44
clear | x | 9 | 11.2 / 12.9 / 20.1 | 11.2 | 10.2 | 0 | 0 | 0 | 40
sort-open | base | 6 | 7.8 / 8.0 / 10.8 | 4.2 | 0.6 | 0 | 0 | 0 | 21
sort-open | x | 6 | 7.2 / 8.3 / 13.1 | 0.0 | 0.0 | 0 | 0 | 0 | 20
sort-year | base | 3 | 18.9 / 20.4 / 26.7 | 18.9 | 18.8 | 2 | 0 | 0 | 55
sort-year | x | 3 | 13.4 / 15.8 / 21.9 | 13.4 | 13.3 | 0 | 0 | 0 | 42
sort-rel | base | 3 | 12.6 / 14.1 / 19.8 | 12.6 | 12.5 | 0 | 0 | 0 | 37
sort-rel | x | 3 | 16.4 / 16.7 / 17.5 | 16.4 | 16.3 | 0 | 0 | 0 | 45
loadmore | base | 3 | 3.3 / 3.3 / 4.4 | 0.0 | 0.0 | 0 | 0 | 0 | 9
loadmore | x | 3 | 3.7 / 4.8 / 7.1 | 0.0 | 0.0 | 0 | 0 | 0 | 13
fling | base | 3 | 7.7 / 7.8 / 9.2 | 0.0 | 0.0 | 0 | 0 | 0 | 126
fling | x | 3 | 22.4 / 19.8 / 26.5 | 10.4 | 2.1 | 4 | 0 | 0 | 145
drag | base | 3 | 5.9 / 5.2 / 6.4 | 0.0 | 0.0 | 0 | 0 | 0 | 49
drag | x | 3 | 3.7 / 3.1 / 3.8 | 0.0 | 0.0 | 0 | 0 | 0 | 44
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 10
contact sheets: 8 in sheets/
contact sheets: 8 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4134', '6065', '7449', '8525', '11330', '12466']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 29
   skipped-frames events=29 max=116 total=1510
== Davey: 61
   davey events=61 max=2383ms median=945ms
== NativeLink: 6
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## gtb33

```
part gtb33; APK: 1.2.40 7abd093 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687410 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 7abd093 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687410 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 33
host cpu:  AMD EPYC 7763 64-Core Processor
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 4026 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~vZk-aovzMslhcjj32cchYg==/org.chyavorec.app--HK30FZ414Qqro4FwyAQOQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 3836 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~3o1vfZdWFGvWZoDkhnc8yQ==/org.chyavorec.app.b-vjU5csCcHkdUlm3PjJXhkQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/LiveWallpapersPicker/LiveWallpapersPicker.apk| x86_64: [status=verify] [reason=prebuilt]|
tabs: catalog [320 2274] home [99 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## E1. Round 1: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r1: worst 19.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (4 with >= 3 ms); entry frame 8.8 + next 14.7 (from the entry: 8.8, 14.7, 1.9, 1.1); rows composed per frame max 0
M tab-my-cold@base@r1: worst 33.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 23 (4 with >= 3 ms); entry frame 33.3 + next 5.0 (from the entry: 33.3, 5.0, 1.1, 0.9); rows composed per frame max 0
M tab-more-cold@base@r1: worst 17.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (5 with >= 3 ms); entry frame 17.3 + next 1.9 (from the entry: 17.3, 1.9, 5.4, 3.6); rows composed per frame max 0
M tab-catalog-cold@base@r1: worst 25.5 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 12 (4 with >= 3 ms); entry frame 19.4 + next 2.3 (from the entry: 19.4, 2.3, 9.4, 25.5); rows composed per frame max 0
M tab-home@base@r1-1: worst 18.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 28 (3 with >= 3 ms); entry frame 18.6 + next 1.5 (from the entry: 18.6, 1.5, 6.4, 2.7); rows composed per frame max 0
M tab-more@base@r1-1: worst 16.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 10.7 + next 16.9 (from the entry: 10.7, 16.9, 5.3, 1.0); rows composed per frame max 0
M tab-my@base@r1-1: worst 12.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (3 with >= 3 ms); entry frame 12.5 + next 1.6 (from the entry: 12.5, 1.6, 1.5, 3.2); rows composed per frame max 0
M tab-news@base@r1-1: worst 52.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (1 with >= 3 ms); entry frame 52.4 + next 1.8 (from the entry: 52.4, 1.8, 0.8, 2.4); rows composed per frame max 0
M tab-home@base@r1-2: worst 32.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 11 (4 with >= 3 ms); entry frame 32.6 + next 1.6 (from the entry: 32.6, 1.6, 4.6, 0.8); rows composed per frame max 0
M tab-news@base@r1-2: worst 15.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 15.8 + next 1.5 (from the entry: 15.8, 1.5, 1.0, 0.8); rows composed per frame max 0
M tab-my@base@r1-2: worst 25.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 10 (2 with >= 3 ms); entry frame 25.4 + next 1.4 (from the entry: 25.4, 1.4, 0.8, 0.6); rows composed per frame max 0
M tab-more@base@r1-2: worst 38.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 10 (2 with >= 3 ms); entry frame 38.5 + next 1.7 (from the entry: 38.5, 1.7, 0.8, 0.8); rows composed per frame max 0
M tab-catalog@base@r1: worst 22.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 14.8 + next 2.1 (from the entry: 14.8, 2.1, 22.4, 1.3); rows composed per frame max 0
M tab-home@base@r1-3: worst 17.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (4 with >= 3 ms); entry frame 17.9 + next 4.0 (from the entry: 17.9, 4.0, 1.5, 0.4); rows composed per frame max 0
M tab-news-cold@x@r1: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (6 with >= 3 ms); entry frame 11.9 + next 1.9 (from the entry: 11.9, 1.9, 1.4, 9.0); rows composed per frame max 0
M tab-my-cold@x@r1: worst 25.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 23 (3 with >= 3 ms); entry frame 25.8 + next 6.1 (from the entry: 25.8, 6.1, 0.9, 0.9); rows composed per frame max 0
M tab-more-cold@x@r1: worst 8.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 8.0 + next 2.5 (from the entry: 8.0, 2.5, 8.6, 3.1); rows composed per frame max 0
M tab-catalog-cold@x@r1: worst 21.6 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 13 (6 with >= 3 ms); entry frame 21.6 + next 2.5 (from the entry: 21.6, 2.5, 9.7, 16.9); rows composed per frame max 0
M tab-home@x@r1-1: worst 19.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 31 (4 with >= 3 ms); entry frame 19.4 + next 2.4 (from the entry: 19.4, 2.4, 8.8, 1.0); rows composed per frame max 0
M tab-more@x@r1-1: worst 10.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (5 with >= 3 ms); entry frame 10.2 + next 2.1 (from the entry: 10.2, 2.1, 7.3, 4.4); rows composed per frame max 0
M tab-my@x@r1-1: worst 18.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 18.5 + next 2.0 (from the entry: 18.5, 2.0, 0.9, 0.8); rows composed per frame max 0
M tab-news@x@r1-1: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 15.1 + next 2.1 (from the entry: 15.1, 2.1, 10.5, 0.9); rows composed per frame max 0
M tab-home@x@r1-2: worst 26.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 31 (3 with >= 3 ms); entry frame 26.6 + next 2.5 (from the entry: 26.6, 2.5, 15.1, 1.0); rows composed per frame max 0
M tab-news@x@r1-2: worst 11.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 11.4 + next 2.2 (from the entry: 11.4, 2.2, 10.7, 0.7); rows composed per frame max 0
M tab-my@x@r1-2: worst 14.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 14.4 + next 1.8 (from the entry: 14.4, 1.8, 1.3, 1.1); rows composed per frame max 0
M tab-more@x@r1-2: worst 17.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (2 with >= 3 ms); entry frame 17.8 + next 1.4 (from the entry: 17.8, 1.4, 13.2, 0.8); rows composed per frame max 0
M tab-catalog@x@r1: worst 28.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 10 (2 with >= 3 ms); entry frame 14.8 + next 1.7 (from the entry: 14.8, 1.7, 28.0, 0.8); rows composed per frame max 0
M tab-home@x@r1-3: worst 25.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 31 (3 with >= 3 ms); entry frame 16.3 + next 2.4 (from the entry: 16.3, 2.4, 25.0, 1.1); rows composed per frame max 0
## E2. Round 2: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r2: worst 19.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 14 (5 with >= 3 ms); entry frame 9.5 + next 19.0 (from the entry: 9.5, 19.0, 3.1, 1.7); rows composed per frame max 0
M tab-my-cold@x@r2: worst 26.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 25 (4 with >= 3 ms); entry frame 26.5 + next 3.3 (from the entry: 26.5, 3.3, 1.1, 0.7); rows composed per frame max 0
M tab-more-cold@x@r2: worst 9.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (4 with >= 3 ms); entry frame 9.1 + next 1.7 (from the entry: 9.1, 1.7, 8.8, 1.0); rows composed per frame max 0
M tab-catalog-cold@x@r2: worst 25.7 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 12 (5 with >= 3 ms); entry frame 25.7 + next 6.2 (from the entry: 25.7, 6.2, 20.1, 5.9); rows composed per frame max 0
M tab-home@x@r2-1: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (4 with >= 3 ms); entry frame 14.5 + next 1.4 (from the entry: 14.5, 1.4, 14.9, 0.9); rows composed per frame max 0
M tab-more@x@r2-1: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 9.8 + next 2.0 (from the entry: 9.8, 2.0, 13.8, 0.9); rows composed per frame max 0
M tab-my@x@r2-1: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (2 with >= 3 ms); entry frame 15.5 + next 2.0 (from the entry: 15.5, 2.0, 1.6, 0.7); rows composed per frame max 0
M tab-news@x@r2-1: worst 25.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (4 with >= 3 ms); entry frame 25.0 + next 3.7 (from the entry: 25.0, 3.7, 8.5, 2.3); rows composed per frame max 0
M tab-home@x@r2-2: worst 23.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (5 with >= 3 ms); entry frame 23.8 + next 2.3 (from the entry: 23.8, 2.3, 14.4, 4.1); rows composed per frame max 0
M tab-news@x@r2-2: worst 21.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 21.7 + next 2.0 (from the entry: 21.7, 2.0, 14.6, 0.5); rows composed per frame max 0
M tab-my@x@r2-2: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 15.3 + next 3.4 (from the entry: 15.3, 3.4, 1.4, 1.1); rows composed per frame max 0
M tab-more@x@r2-2: worst 25.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (2 with >= 3 ms); entry frame 25.0 + next 1.7 (from the entry: 25.0, 1.7, 15.3, 2.4); rows composed per frame max 0
M tab-catalog@x@r2: worst 17.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 17.7 + next 4.0 (from the entry: 17.7, 4.0, 13.3, 0.9); rows composed per frame max 0
M tab-home@x@r2-3: worst 19.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 19.7 + next 2.1 (from the entry: 19.7, 2.1, 8.3, 0.9); rows composed per frame max 0
M tab-news-cold@base@r2: worst 32.8 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 13 (4 with >= 3 ms); entry frame 10.4 + next 32.8 (from the entry: 10.4, 32.8, 2.4, 1.1); rows composed per frame max 0
M tab-my-cold@base@r2: worst 30.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 23 (3 with >= 3 ms); entry frame 30.7 + next 1.7 (from the entry: 30.7, 1.7, 5.6, 0.7); rows composed per frame max 0
M tab-more-cold@base@r2: worst 17.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 17.3 + next 1.5 (from the entry: 17.3, 1.5, 2.1, 0.7); rows composed per frame max 0
M tab-catalog-cold@base@r2: worst 27.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 13 (7 with >= 3 ms); entry frame 10.6 + next 3.4 (from the entry: 10.6, 3.4, 3.8, 27.0); rows composed per frame max 0
M tab-home@base@r2-1: worst 24.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 24.2 + next 2.4 (from the entry: 24.2, 2.4, 5.8, 2.7); rows composed per frame max 0
M tab-more@base@r2-1: worst 26.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 9 (3 with >= 3 ms); entry frame 26.0 + next 2.1 (from the entry: 26.0, 2.1, 0.5, 0.9); rows composed per frame max 0
M tab-my@base@r2-1: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 13.8 + next 2.4 (from the entry: 13.8, 2.4, 0.8, 0.6); rows composed per frame max 0
M tab-news@base@r2-1: worst 26.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 10 (2 with >= 3 ms); entry frame 26.7 + next 1.9 (from the entry: 26.7, 1.9, 0.8, 0.5); rows composed per frame max 0
M tab-home@base@r2-2: worst 33.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 11 (2 with >= 3 ms); entry frame 33.3 + next 2.2 (from the entry: 33.3, 2.2, 1.1, 0.8); rows composed per frame max 0
M tab-news@base@r2-2: worst 13.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 8.8 + next 5.8 (from the entry: 8.8, 5.8, 13.5, 2.1); rows composed per frame max 0
M tab-my@base@r2-2: worst 9.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 9.8 + next 1.6 (from the entry: 9.8, 1.6, 1.4, 4.1); rows composed per frame max 0
M tab-more@base@r2-2: worst 25.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 10 (2 with >= 3 ms); entry frame 25.3 + next 1.9 (from the entry: 25.3, 1.9, 0.4, 0.5); rows composed per frame max 0
M tab-catalog@base@r2: worst 22.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 10.5 + next 2.2 (from the entry: 10.5, 2.2, 22.8, 0.8); rows composed per frame max 0
M tab-home@base@r2-3: worst 22.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 22.6 + next 1.3 (from the entry: 22.6, 1.3, 1.4, 0.5); rows composed per frame max 0
## E3. Round 3: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r3: worst 18.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 15 (5 with >= 3 ms); entry frame 18.0 + next 2.4 (from the entry: 18.0, 2.4, 1.6, 10.8); rows composed per frame max 0
M tab-my-cold@base@r3: worst 24.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 26 (2 with >= 3 ms); entry frame 24.4 + next 2.8 (from the entry: 24.4, 2.8, 0.9, 0.8); rows composed per frame max 0
M tab-more-cold@base@r3: worst 18.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 18.1 + next 2.0 (from the entry: 18.1, 2.0, 5.0, 0.7); rows composed per frame max 0
M tab-catalog-cold@base@r3: worst 37.6 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 2 of 12 (4 with >= 3 ms); entry frame 33.9 + next 3.4 (from the entry: 33.9, 3.4, 37.6, 2.1); rows composed per frame max 0
M tab-home@base@r3-1: worst 22.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 22.0 + next 1.5 (from the entry: 22.0, 1.5, 4.1, 0.5); rows composed per frame max 0
M tab-more@base@r3-1: worst 30.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 10 (4 with >= 3 ms); entry frame 30.1 + next 2.2 (from the entry: 30.1, 2.2, 0.5, 7.6); rows composed per frame max 0
M tab-my@base@r3-1: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (2 with >= 3 ms); entry frame 9.5 + next 1.5 (from the entry: 9.5, 1.5, 1.3, 0.8); rows composed per frame max 0
M tab-news@base@r3-1: worst 44.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 9 (1 with >= 3 ms); entry frame 44.9 + next 1.9 (from the entry: 44.9, 1.9, 0.7, 2.7); rows composed per frame max 0
M tab-home@base@r3-2: worst 36.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 13 (2 with >= 3 ms); entry frame 36.8 + next 1.6 (from the entry: 36.8, 1.6, 1.5, 1.0); rows composed per frame max 0
M tab-news@base@r3-2: worst 17.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 9 (3 with >= 3 ms); entry frame 17.4 + next 2.3 (from the entry: 17.4, 2.3, 2.7, 8.2); rows composed per frame max 0
M tab-my@base@r3-2: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (1 with >= 3 ms); entry frame 12.2 + next 1.5 (from the entry: 12.2, 1.5, 1.3, 0.9); rows composed per frame max 0
M tab-more@base@r3-2: worst 33.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 10 (2 with >= 3 ms); entry frame 33.2 + next 2.0 (from the entry: 33.2, 2.0, 0.8, 0.5); rows composed per frame max 0
M tab-catalog@base@r3: worst 20.7 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 20.7 + next 1.7 (from the entry: 20.7, 1.7, 19.3, 2.1); rows composed per frame max 0
M tab-home@base@r3-3: worst 18.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 18.6 + next 3.1 (from the entry: 18.6, 3.1, 1.4, 0.5); rows composed per frame max 0
M tab-news-cold@x@r3: worst 21.3 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 13 (5 with >= 3 ms); entry frame 16.3 + next 1.8 (from the entry: 16.3, 1.8, 4.7, 21.3); rows composed per frame max 0
M tab-my-cold@x@r3: worst 14.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (3 with >= 3 ms); entry frame 14.4 + next 2.6 (from the entry: 14.4, 2.6, 1.2, 1.1); rows composed per frame max 0
M tab-more-cold@x@r3: worst 22.4 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 15 (3 with >= 3 ms); entry frame 22.4 + next 1.3 (from the entry: 22.4, 1.3, 18.1, 1.2); rows composed per frame max 0
M tab-catalog-cold@x@r3: worst 27.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 12 (5 with >= 3 ms); entry frame 12.5 + next 3.4 (from the entry: 12.5, 3.4, 27.4, 3.5); rows composed per frame max 0
M tab-home@x@r3-1: worst 25.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 21 (3 with >= 3 ms); entry frame 25.9 + next 2.4 (from the entry: 25.9, 2.4, 14.1, 2.9); rows composed per frame max 0
M tab-more@x@r3-1: worst 18.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 9 (4 with >= 3 ms); entry frame 18.8 + next 2.3 (from the entry: 18.8, 2.3, 14.0, 7.9); rows composed per frame max 0
M tab-my@x@r3-1: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 12.0 + next 1.7 (from the entry: 12.0, 1.7, 0.8, 0.7); rows composed per frame max 0
M tab-news@x@r3-1: worst 18.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (2 with >= 3 ms); entry frame 18.2 + next 1.9 (from the entry: 18.2, 1.9, 12.3, 1.0); rows composed per frame max 0
M tab-home@x@r3-2: worst 30.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 29 (5 with >= 3 ms); entry frame 30.4 + next 2.5 (from the entry: 30.4, 2.5, 10.1, 1.1); rows composed per frame max 0
M tab-news@x@r3-2: worst 23.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 9 (4 with >= 3 ms); entry frame 8.2 + next 23.0 (from the entry: 8.2, 23.0, 2.2, 16.1); rows composed per frame max 0
M tab-my@x@r3-2: worst 17.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 17.2 + next 1.5 (from the entry: 17.2, 1.5, 1.3, 1.1); rows composed per frame max 0
M tab-more@x@r3-2: worst 20.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 19.3 + next 1.3 (from the entry: 19.3, 1.3, 20.2, 1.0); rows composed per frame max 0
M tab-catalog@x@r3: worst 27.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 11 (2 with >= 3 ms); entry frame 9.9 + next 2.7 (from the entry: 9.9, 2.7, 27.3, 1.2); rows composed per frame max 0
M tab-home@x@r3-3: worst 18.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 28 (4 with >= 3 ms); entry frame 17.9 + next 1.5 (from the entry: 17.9, 1.5, 18.1, 0.9); rows composed per frame max 0
## E4. Round 4: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r4: worst 14.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 8.8 + next 14.3 (from the entry: 8.8, 14.3, 2.0, 1.3); rows composed per frame max 0
M tab-my-cold@x@r4: worst 21.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 21.7 + next 3.0 (from the entry: 21.7, 3.0, 1.6, 0.8); rows composed per frame max 0
M tab-more-cold@x@r4: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (3 with >= 3 ms); entry frame 12.7 + next 2.1 (from the entry: 12.7, 2.1, 9.1, 3.4); rows composed per frame max 0
M tab-catalog-cold@x@r4: worst 40.7 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 12 (7 with >= 3 ms); entry frame 17.1 + next 3.2 (from the entry: 17.1, 3.2, 40.7, 8.1); rows composed per frame max 0
M tab-home@x@r4-1: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 28 (4 with >= 3 ms); entry frame 15.6 + next 1.3 (from the entry: 15.6, 1.3, 12.7, 0.9); rows composed per frame max 0
M tab-more@x@r4-1: worst 17.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 17.6 + next 1.5 (from the entry: 17.6, 1.5, 12.9, 0.8); rows composed per frame max 0
M tab-my@x@r4-1: worst 14.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (2 with >= 3 ms); entry frame 14.7 + next 1.7 (from the entry: 14.7, 1.7, 1.4, 0.9); rows composed per frame max 0
M tab-news@x@r4-1: worst 16.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 10.1 + next 2.5 (from the entry: 10.1, 2.5, 16.9, 0.7); rows composed per frame max 0
M tab-home@x@r4-2: worst 35.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 33 (4 with >= 3 ms); entry frame 35.2 + next 2.4 (from the entry: 35.2, 2.4, 10.6, 1.4); rows composed per frame max 0
M tab-news@x@r4-2: worst 15.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 15.8 + next 2.0 (from the entry: 15.8, 2.0, 6.5, 0.6); rows composed per frame max 0
M tab-my@x@r4-2: worst 10.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 10.1 + next 1.6 (from the entry: 10.1, 1.6, 1.3, 0.9); rows composed per frame max 0
M tab-more@x@r4-2: worst 21.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (2 with >= 3 ms); entry frame 21.1 + next 2.0 (from the entry: 21.1, 2.0, 15.0, 0.8); rows composed per frame max 0
M tab-catalog@x@r4: worst 19.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 14.8 + next 2.5 (from the entry: 14.8, 2.5, 19.6, 1.3); rows composed per frame max 0
M tab-home@x@r4-3: worst 23.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 31 (4 with >= 3 ms); entry frame 14.6 + next 1.4 (from the entry: 14.6, 1.4, 23.2, 1.1); rows composed per frame max 0
M tab-news-cold@base@r4: worst 27.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 12 (5 with >= 3 ms); entry frame 27.0 + next 1.9 (from the entry: 27.0, 1.9, 10.0, 1.9); rows composed per frame max 0
M tab-my-cold@base@r4: worst 22.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 26 (2 with >= 3 ms); entry frame 22.6 + next 2.7 (from the entry: 22.6, 2.7, 1.3, 0.9); rows composed per frame max 0
M tab-more-cold@base@r4: worst 35.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 13 (3 with >= 3 ms); entry frame 35.4 + next 1.9 (from the entry: 35.4, 1.9, 2.1, 1.1); rows composed per frame max 0
M tab-catalog-cold@base@r4: worst 33.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 12 (7 with >= 3 ms); entry frame 13.7 + next 3.4 (from the entry: 13.7, 3.4, 33.3, 5.7); rows composed per frame max 0
M tab-home@base@r4-1: worst 22.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 29 (3 with >= 3 ms); entry frame 22.6 + next 2.1 (from the entry: 22.6, 2.1, 5.9, 3.6); rows composed per frame max 0
M tab-more@base@r4-1: worst 22.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 22.5 + next 1.5 (from the entry: 22.5, 1.5, 0.6, 0.7); rows composed per frame max 0
M tab-my@base@r4-1: worst 15.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 15.9 + next 1.7 (from the entry: 15.9, 1.7, 0.8, 0.7); rows composed per frame max 0
M tab-news@base@r4-1: worst 15.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (2 with >= 3 ms); entry frame 15.2 + next 1.3 (from the entry: 15.2, 1.3, 4.3, 0.8); rows composed per frame max 0
M tab-home@base@r4-2: worst 40.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 32 (2 with >= 3 ms); entry frame 40.1 + next 2.3 (from the entry: 40.1, 2.3, 0.9, 0.9); rows composed per frame max 0
M tab-news@base@r4-2: worst 18.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 18.1 + next 4.6 (from the entry: 18.1, 4.6, 0.8, 0.7); rows composed per frame max 0
M tab-my@base@r4-2: worst 12.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (1 with >= 3 ms); entry frame 12.8 + next 2.0 (from the entry: 12.8, 2.0, 1.3, 1.1); rows composed per frame max 0
M tab-more@base@r4-2: worst 28.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 10 (3 with >= 3 ms); entry frame 28.9 + next 5.6 (from the entry: 28.9, 5.6, 1.9, 0.9); rows composed per frame max 0
M tab-catalog@base@r4: worst 13.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 13.9 + next 1.6 (from the entry: 13.9, 1.6, 13.4, 1.0); rows composed per frame max 0
M tab-home@base@r4-3: worst 17.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 33 (2 with >= 3 ms); entry frame 17.3 + next 1.3 (from the entry: 17.3, 1.3, 1.4, 0.6); rows composed per frame max 0
## E5. Round 5: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r5: worst 26.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 13 (4 with >= 3 ms); entry frame 13.2 + next 16.0 (from the entry: 13.2, 16.0, 2.7, 1.5); rows composed per frame max 0
M tab-my-cold@base@r5: worst 29.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 21 (2 with >= 3 ms); entry frame 29.1 + next 2.9 (from the entry: 29.1, 2.9, 0.9, 0.7); rows composed per frame max 0
M tab-more-cold@base@r5: worst 16.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 16.3 + next 1.7 (from the entry: 16.3, 1.7, 2.2, 0.9); rows composed per frame max 0
M tab-catalog-cold@base@r5: worst 39.5 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 1 of 12 (6 with >= 3 ms); entry frame 16.1 + next 3.4 (from the entry: 16.1, 3.4, 39.5, 2.7); rows composed per frame max 0
M tab-home@base@r5-1: worst 29.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 29 (3 with >= 3 ms); entry frame 29.0 + next 2.4 (from the entry: 29.0, 2.4, 1.0, 0.6); rows composed per frame max 0
M tab-more@base@r5-1: worst 15.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (5 with >= 3 ms); entry frame 15.0 + next 1.3 (from the entry: 15.0, 1.3, 6.6, 0.9); rows composed per frame max 0
M tab-my@base@r5-1: worst 10.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 10.0 + next 1.5 (from the entry: 10.0, 1.5, 1.4, 0.9); rows composed per frame max 0
M tab-news@base@r5-1: worst 29.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 10 (1 with >= 3 ms); entry frame 29.9 + next 1.5 (from the entry: 29.9, 1.5, 0.7, 0.5); rows composed per frame max 0
M tab-home@base@r5-2: worst 30.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 11 (3 with >= 3 ms); entry frame 30.1 + next 2.7 (from the entry: 30.1, 2.7, 1.0, 0.9); rows composed per frame max 0
M tab-news@base@r5-2: worst 17.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (5 with >= 3 ms); entry frame 17.9 + next 4.9 (from the entry: 17.9, 4.9, 1.5, 0.6); rows composed per frame max 0
M tab-my@base@r5-2: worst 10.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 10.1 + next 2.0 (from the entry: 10.1, 2.0, 1.4, 0.9); rows composed per frame max 0
M tab-more@base@r5-2: worst 28.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 10 (2 with >= 3 ms); entry frame 28.7 + next 1.3 (from the entry: 28.7, 1.3, 0.4, 0.7); rows composed per frame max 0
M tab-catalog@base@r5: worst 23.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 11.8 + next 3.6 (from the entry: 11.8, 3.6, 23.3, 0.8); rows composed per frame max 0
M tab-home@base@r5-3: worst 18.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 18.1 + next 1.5 (from the entry: 18.1, 1.5, 1.0, 0.5); rows composed per frame max 0
M tab-news-cold@x@r5: worst 25.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 12 (4 with >= 3 ms); entry frame 10.5 + next 25.4 (from the entry: 10.5, 25.4, 1.8, 1.8); rows composed per frame max 0
M tab-my-cold@x@r5: worst 15.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (5 with >= 3 ms); entry frame 15.0 + next 5.6 (from the entry: 15.0, 5.6, 1.5, 1.0); rows composed per frame max 0
M tab-more-cold@x@r5: worst 18.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (5 with >= 3 ms); entry frame 15.2 + next 1.5 (from the entry: 15.2, 1.5, 18.5, 3.6); rows composed per frame max 0
M tab-catalog-cold@x@r5: worst 15.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (6 with >= 3 ms); entry frame 14.0 + next 2.7 (from the entry: 14.0, 2.7, 15.4, 5.0); rows composed per frame max 0
M tab-home@x@r5-1: worst 19.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 29 (4 with >= 3 ms); entry frame 19.2 + next 2.1 (from the entry: 19.2, 2.1, 9.0, 0.9); rows composed per frame max 0
M tab-more@x@r5-1: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (4 with >= 3 ms); entry frame 9.6 + next 2.0 (from the entry: 9.6, 2.0, 12.7, 1.0); rows composed per frame max 0
M tab-my@x@r5-1: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 10.7 + next 1.2 (from the entry: 10.7, 1.2, 4.2, 0.8); rows composed per frame max 0
M tab-news@x@r5-1: worst 16.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (2 with >= 3 ms); entry frame 16.5 + next 1.1 (from the entry: 16.5, 1.1, 11.4, 1.0); rows composed per frame max 0
M tab-home@x@r5-2: worst 29.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 11 (4 with >= 3 ms); entry frame 29.2 + next 4.8 (from the entry: 29.2, 4.8, 15.5, 1.1); rows composed per frame max 0
M tab-news@x@r5-2: worst 20.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (5 with >= 3 ms); entry frame 20.5 + next 3.9 (from the entry: 20.5, 3.9, 4.9, 9.1); rows composed per frame max 0
M tab-my@x@r5-2: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 10 (1 with >= 3 ms); entry frame 11.5 + next 1.7 (from the entry: 11.5, 1.7, 0.5, 0.8); rows composed per frame max 0
M tab-more@x@r5-2: worst 18.3 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 18.3 + next 3.5 (from the entry: 18.3, 3.5, 16.1, 0.7); rows composed per frame max 0
M tab-catalog@x@r5: worst 17.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 17.5 + next 2.3 (from the entry: 17.5, 2.3, 13.2, 1.3); rows composed per frame max 0
M tab-home@x@r5-3: worst 19.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (4 with >= 3 ms); entry frame 19.7 + next 1.9 (from the entry: 19.7, 1.9, 15.6, 0.7); rows composed per frame max 0
## EV. Visual: the tab entries recorded at 8 frames/s
REC tab-news-base (emu): marker 1657 ms after the recording start; 46 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-base (emu): marker 1546 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-base (emu): marker 1546 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-news-x (emu): marker 1580 ms after the recording start; 46 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-x (emu): marker 1559 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-x (emu): marker 1553 ms after the recording start; 43 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
tab-news-cold | base | 5 | 26.9 / 24.8 / 32.8 | 13.2 | 14.7 | 3 | 1 | 0 | 60
tab-news-cold | x | 5 | 19.0 / 18.4 / 25.4 | 10.5 | 14.3 | 1 | 0 | 0 | 58
tab-my-cold | base | 5 | 29.1 / 28.0 / 33.3 | 29.1 | 2.8 | 3 | 2 | 0 | 48
tab-my-cold | x | 5 | 21.7 / 20.7 / 26.5 | 21.7 | 3.3 | 2 | 0 | 0 | 46
tab-more-cold | base | 5 | 17.3 / 20.9 / 35.4 | 17.3 | 1.9 | 1 | 1 | 0 | 45
tab-more-cold | x | 5 | 12.7 / 14.3 / 22.4 | 12.7 | 1.7 | 0 | 0 | 0 | 41
tab-catalog-cold | base | 5 | 33.3 / 32.6 / 39.5 | 16.1 | 3.4 | 6 | 4 | 0 | 73
tab-catalog-cold | x | 5 | 25.7 / 26.2 / 40.7 | 17.1 | 3.2 | 3 | 1 | 0 | 70
tab-home | base | 15 | 22.6 / 25.6 / 40.1 | 22.6 | 2.1 | 6 | 5 | 0 | 42
tab-home | x | 15 | 23.2 / 23.1 / 35.2 | 19.7 | 2.3 | 5 | 2 | 0 | 54
tab-more | base | 10 | 27.4 / 26.5 / 38.5 | 27.4 | 1.9 | 7 | 3 | 0 | 44
tab-more | x | 10 | 18.1 / 17.6 / 25.0 | 18.1 | 2.0 | 1 | 0 | 0 | 44
tab-my | base | 10 | 12.3 / 13.2 / 25.4 | 12.3 | 1.6 | 1 | 0 | 0 | 25
tab-my | x | 10 | 14.6 / 14.0 / 18.5 | 14.6 | 1.7 | 0 | 0 | 0 | 27
tab-news | base | 10 | 18.0 / 25.2 / 52.4 | 18.0 | 1.9 | 4 | 2 | 0 | 38
tab-news | x | 10 | 17.5 / 18.4 / 25.0 | 16.1 | 2.2 | 1 | 0 | 0 | 40
tab-catalog | base | 5 | 22.4 / 20.6 / 23.3 | 13.9 | 2.1 | 0 | 0 | 0 | 45
tab-catalog | x | 5 | 19.6 / 22.0 / 28.0 | 14.8 | 2.5 | 2 | 0 | 0 | 47
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 45
contact sheets: 20 in sheets/
contact sheets: 20 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5450', '8159', '8813', '9495', '10992', '11600', '13009', '13764']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 41
   skipped-frames events=41 max=104 total=2290
== Davey: 68
   davey events=68 max=2728ms median=1252ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## gtb36x

```
part gtb36x; APK: 1.2.40 7abd093 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687410 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 7abd093 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW) size 4687410 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 36
host cpu:  AMD EPYC 9V74 80-Core Processor
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 4415 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~Q42_wBksES0W4E6XHrXU8Q==/org.chyavorec.app-8VHddvLnZaZqOHxz2cj6ZA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~nfqtheUq167Oro3ASxHLZA==/org.chyavorec.app.b-A-XJ4l42tjm1se7gOtVlpg==/base.apk| x86_64: [status=verify] [reason=install] [primary-abi]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 2876 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~nfqtheUq167Oro3ASxHLZA==/org.chyavorec.app.b-A-XJ4l42tjm1se7gOtVlpg==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]|
tabs: catalog [320 2274] home [100 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## E1. Round 1: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r1: worst 17.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 24 (7 with >= 3 ms); entry frame 17.1 + next 16.9 (from the entry: 17.1, 16.9, 1.6, 1.5); rows composed per frame max 0
M tab-my-cold@base@r1: worst 17.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 43 (3 with >= 3 ms); entry frame 17.2 + next 5.9 (from the entry: 17.2, 5.9, 5.8, 1.7); rows composed per frame max 0
M tab-more-cold@base@r1: worst 16.5 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 16.5 + next 16.4 (from the entry: 16.5, 16.4, 1.9, 1.8); rows composed per frame max 0
M tab-catalog-cold@base@r1: worst 19.6 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 27 (6 with >= 3 ms); entry frame 15.1 + next 3.0 (from the entry: 15.1, 3.0, 2.9, 3.7); rows composed per frame max 0
M tab-home@base@r1-1: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (5 with >= 3 ms); entry frame 14.9 + next 1.5 (from the entry: 14.9, 1.5, 1.4, 0.7); rows composed per frame max 0
M tab-more@base@r1-1: worst 15.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 15.4 + next 1.9 (from the entry: 15.4, 1.9, 1.8, 0.8); rows composed per frame max 0
M tab-my@base@r1-1: worst 19.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 27 (1 with >= 3 ms); entry frame 19.2 + next 1.4 (from the entry: 19.2, 1.4, 1.3, 0.8); rows composed per frame max 0
M tab-news@base@r1-1: worst 19.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 19.6 + next 1.5 (from the entry: 19.6, 1.5, 1.5, 0.9); rows composed per frame max 0
M tab-home@base@r1-2: worst 27.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 22 (3 with >= 3 ms); entry frame 27.9 + next 1.6 (from the entry: 27.9, 1.6, 1.5, 1.4); rows composed per frame max 0
M tab-news@base@r1-2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (4 with >= 3 ms); entry frame 15.6 + next 15.5 (from the entry: 15.6, 15.5, 1.8, 1.7); rows composed per frame max 0
M tab-my@base@r1-2: worst 8.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 8.1 + next 1.5 (from the entry: 8.1, 1.5, 1.5, 1.4); rows composed per frame max 0
M tab-more@base@r1-2: worst 22.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 22.9 + next 1.9 (from the entry: 22.9, 1.9, 1.7, 1.2); rows composed per frame max 0
M tab-catalog@base@r1: worst 13.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 13.4 + next 1.7 (from the entry: 13.4, 1.7, 11.1, 11.0); rows composed per frame max 0
M tab-home@base@r1-3: worst 20.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 20.1 + next 1.2 (from the entry: 20.1, 1.2, 4.0, 3.9); rows composed per frame max 0
M tab-news-cold@x@r1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (7 with >= 3 ms); entry frame 10.3 + next 10.2 (from the entry: 10.3, 10.2, 2.2, 2.1); rows composed per frame max 0
M tab-my-cold@x@r1: worst 19.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 42 (1 with >= 3 ms); entry frame 19.8 + next 3.0 (from the entry: 19.8, 3.0, 2.9, 1.9); rows composed per frame max 0
M tab-more-cold@x@r1: worst 15.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 33 (5 with >= 3 ms); entry frame 14.8 + next 14.7 (from the entry: 14.8, 14.7, 1.8, 1.7); rows composed per frame max 0
M tab-catalog-cold@x@r1: worst 30.6 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 2 of 29 (7 with >= 3 ms); entry frame 23.4 + next 2.5 (from the entry: 23.4, 2.5, 2.4, 6.8); rows composed per frame max 0
M tab-home@x@r1-1: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (3 with >= 3 ms); entry frame 11.2 + next 2.4 (from the entry: 11.2, 2.4, 2.3, 7.1); rows composed per frame max 0
M tab-more@x@r1-1: worst 16.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 16.1 + next 1.7 (from the entry: 16.1, 1.7, 1.6, 11.5); rows composed per frame max 0
M tab-my@x@r1-1: worst 13.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 13.7 + next 1.4 (from the entry: 13.7, 1.4, 1.4, 1.4); rows composed per frame max 0
M tab-news@x@r1-1: worst 17.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 17.0 + next 2.0 (from the entry: 17.0, 2.0, 2.0, 4.6); rows composed per frame max 0
M tab-home@x@r1-2: worst 17.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 17.1 + next 1.9 (from the entry: 17.1, 1.9, 1.8, 4.8); rows composed per frame max 0
M tab-news@x@r1-2: worst 20.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 20 (7 with >= 3 ms); entry frame 20.0 + next 19.9 (from the entry: 20.0, 19.9, 2.3, 2.2); rows composed per frame max 0
M tab-my@x@r1-2: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 13.3 + next 1.3 (from the entry: 13.3, 1.3, 1.4, 1.3); rows composed per frame max 0
M tab-more@x@r1-2: worst 12.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 12.5 + next 1.1 (from the entry: 12.5, 1.1, 6.1, 6.1); rows composed per frame max 0
M tab-catalog@x@r1: worst 23.6 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 19 (6 with >= 3 ms); entry frame 13.5 + next 3.7 (from the entry: 13.5, 3.7, 23.6, 23.5); rows composed per frame max 0
M tab-home@x@r1-3: worst 14.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (4 with >= 3 ms); entry frame 11.1 + next 2.3 (from the entry: 11.1, 2.3, 2.2, 14.8); rows composed per frame max 0
## E2. Round 2: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r2: worst 11.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (7 with >= 3 ms); entry frame 11.8 + next 11.7 (from the entry: 11.8, 11.7, 1.6, 1.4); rows composed per frame max 0
M tab-my-cold@x@r2: worst 23.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 39 (5 with >= 3 ms); entry frame 23.5 + next 5.3 (from the entry: 23.5, 5.3, 5.2, 1.2); rows composed per frame max 0
M tab-more-cold@x@r2: worst 17.8 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 32 (4 with >= 3 ms); entry frame 17.8 + next 17.7 (from the entry: 17.8, 17.7, 2.2, 2.1); rows composed per frame max 0
M tab-catalog-cold@x@r2: worst 32.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 26 (9 with >= 3 ms); entry frame 32.9 + next 3.2 (from the entry: 32.9, 3.2, 3.1, 14.6); rows composed per frame max 0
M tab-home@x@r2-1: worst 16.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 44 (7 with >= 3 ms); entry frame 16.5 + next 1.2 (from the entry: 16.5, 1.2, 6.8, 6.7); rows composed per frame max 0
M tab-more@x@r2-1: worst 11.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 11.0 + next 2.3 (from the entry: 11.0, 2.3, 2.1, 6.0); rows composed per frame max 0
M tab-my@x@r2-1: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (5 with >= 3 ms); entry frame 13.3 + next 4.8 (from the entry: 13.3, 4.8, 4.8, 1.1); rows composed per frame max 0
M tab-news@x@r2-1: worst 12.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 12.1 + next 1.6 (from the entry: 12.1, 1.6, 1.5, 6.3); rows composed per frame max 0
M tab-home@x@r2-2: worst 21.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 19 (4 with >= 3 ms); entry frame 21.1 + next 1.8 (from the entry: 21.1, 1.8, 1.8, 5.9); rows composed per frame max 0
M tab-news@x@r2-2: worst 11.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (8 with >= 3 ms); entry frame 11.7 + next 11.6 (from the entry: 11.7, 11.6, 1.9, 1.8); rows composed per frame max 0
M tab-my@x@r2-2: worst 8.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 8.8 + next 1.3 (from the entry: 8.8, 1.3, 1.5, 1.4); rows composed per frame max 0
M tab-more@x@r2-2: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 12.0 + next 1.0 (from the entry: 12.0, 1.0, 7.3, 7.2); rows composed per frame max 0
M tab-catalog@x@r2: worst 14.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 14.4 + next 1.6 (from the entry: 14.4, 1.6, 9.5, 9.4); rows composed per frame max 0
M tab-home@x@r2-3: worst 8.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 8.8 + next 1.7 (from the entry: 8.8, 1.7, 1.6, 5.2); rows composed per frame max 0
M tab-news-cold@base@r2: worst 17.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 29 (11 with >= 3 ms); entry frame 17.1 + next 17.0 (from the entry: 17.1, 17.0, 7.4, 1.8); rows composed per frame max 0
M tab-my-cold@base@r2: worst 18.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 43 (3 with >= 3 ms); entry frame 18.9 + next 2.6 (from the entry: 18.9, 2.6, 1.7, 1.5); rows composed per frame max 0
M tab-more-cold@base@r2: worst 32.1 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 2 of 24 (3 with >= 3 ms); entry frame 32.1 + next 32.0 (from the entry: 32.1, 32.0, 1.5, 1.4); rows composed per frame max 0
M tab-catalog-cold@base@r2: worst 26.0 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 25 (7 with >= 3 ms); entry frame 10.4 + next 2.9 (from the entry: 10.4, 2.9, 2.9, 2.8); rows composed per frame max 0
M tab-home@base@r2-1: worst 15.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 46 (1 with >= 3 ms); entry frame 15.8 + next 2.1 (from the entry: 15.8, 2.1, 2.0, 0.8); rows composed per frame max 0
M tab-more@base@r2-1: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (5 with >= 3 ms); entry frame 15.1 + next 15.0 (from the entry: 15.1, 15.0, 2.1, 2.0); rows composed per frame max 0
M tab-my@base@r2-1: worst 13.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 13.1 + next 1.8 (from the entry: 13.1, 1.8, 1.4, 1.3); rows composed per frame max 0
M tab-news@base@r2-1: worst 22.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 22.4 + next 2.4 (from the entry: 22.4, 2.4, 2.4, 1.1); rows composed per frame max 0
M tab-home@base@r2-2: worst 20.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 51 (3 with >= 3 ms); entry frame 20.5 + next 2.0 (from the entry: 20.5, 2.0, 1.9, 1.1); rows composed per frame max 0
M tab-news@base@r2-2: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 13.0 + next 12.8 (from the entry: 13.0, 12.8, 1.9, 1.8); rows composed per frame max 0
M tab-my@base@r2-2: worst 16.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 16.9 + next 1.1 (from the entry: 16.9, 1.1, 1.2, 1.2); rows composed per frame max 0
M tab-more@base@r2-2: worst 24.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (5 with >= 3 ms); entry frame 24.9 + next 4.9 (from the entry: 24.9, 4.9, 4.8, 1.1); rows composed per frame max 0
M tab-catalog@base@r2: worst 20.7 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 9.3 + next 1.9 (from the entry: 9.3, 1.9, 20.7, 20.6); rows composed per frame max 0
M tab-home@base@r2-3: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 48 (4 with >= 3 ms); entry frame 15.5 + next 1.0 (from the entry: 15.5, 1.0, 1.5, 1.4); rows composed per frame max 0
## E3. Round 3: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r3: worst 12.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (7 with >= 3 ms); entry frame 12.4 + next 12.2 (from the entry: 12.4, 12.2, 2.0, 1.9); rows composed per frame max 0
M tab-my-cold@base@r3: worst 18.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 42 (3 with >= 3 ms); entry frame 18.0 + next 2.8 (from the entry: 18.0, 2.8, 2.8, 0.8); rows composed per frame max 0
M tab-more-cold@base@r3: worst 23.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 28 (4 with >= 3 ms); entry frame 23.1 + next 23.0 (from the entry: 23.1, 23.0, 2.3, 2.2); rows composed per frame max 0
M tab-catalog-cold@base@r3: worst 17.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 22 (7 with >= 3 ms); entry frame 17.1 + next 3.0 (from the entry: 17.1, 3.0, 3.0, 14.6); rows composed per frame max 0
M tab-home@base@r3-1: worst 15.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 46 (5 with >= 3 ms); entry frame 15.4 + next 4.9 (from the entry: 15.4, 4.9, 4.8, 1.1); rows composed per frame max 0
M tab-more@base@r3-1: worst 28.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 16 (4 with >= 3 ms); entry frame 28.4 + next 1.6 (from the entry: 28.4, 1.6, 1.5, 1.7); rows composed per frame max 0
M tab-my@base@r3-1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 10.3 + next 1.4 (from the entry: 10.3, 1.4, 2.7, 2.7); rows composed per frame max 0
M tab-news@base@r3-1: worst 20.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 20.4 + next 1.7 (from the entry: 20.4, 1.7, 1.6, 0.6); rows composed per frame max 0
M tab-home@base@r3-2: worst 30.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 18 (3 with >= 3 ms); entry frame 30.4 + next 2.2 (from the entry: 30.4, 2.2, 2.1, 0.6); rows composed per frame max 0
M tab-news@base@r3-2: worst 25.4 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 20 (4 with >= 3 ms); entry frame 25.4 + next 25.4 (from the entry: 25.4, 25.4, 2.1, 2.0); rows composed per frame max 0
M tab-my@base@r3-2: worst 15.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 15.0 + next 2.0 (from the entry: 15.0, 2.0, 1.9, 1.4); rows composed per frame max 0
M tab-more@base@r3-2: worst 19.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 19.1 + next 1.8 (from the entry: 19.1, 1.8, 1.8, 1.2); rows composed per frame max 0
M tab-catalog@base@r3: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 14.9 + next 5.3 (from the entry: 14.9, 5.3, 12.2, 12.1); rows composed per frame max 0
M tab-home@base@r3-3: worst 22.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 22.2 + next 2.0 (from the entry: 22.2, 2.0, 1.9, 0.6); rows composed per frame max 0
M tab-news-cold@x@r3: worst 8.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (6 with >= 3 ms); entry frame 8.8 + next 8.7 (from the entry: 8.8, 8.7, 1.5, 1.4); rows composed per frame max 0
M tab-my-cold@x@r3: worst 13.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 44 (4 with >= 3 ms); entry frame 13.5 + next 6.2 (from the entry: 13.5, 6.2, 1.2, 1.2); rows composed per frame max 0
M tab-more-cold@x@r3: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (5 with >= 3 ms); entry frame 12.2 + next 12.2 (from the entry: 12.2, 12.2, 1.7, 1.6); rows composed per frame max 0
M tab-catalog-cold@x@r3: worst 31.1 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 2 of 28 (9 with >= 3 ms); entry frame 22.9 + next 2.8 (from the entry: 22.9, 2.8, 2.7, 4.1); rows composed per frame max 0
M tab-home@x@r3-1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 47 (4 with >= 3 ms); entry frame 10.3 + next 3.4 (from the entry: 10.3, 3.4, 3.3, 5.9); rows composed per frame max 0
M tab-more@x@r3-1: worst 8.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (4 with >= 3 ms); entry frame 8.9 + next 1.9 (from the entry: 8.9, 1.9, 1.8, 7.3); rows composed per frame max 0
M tab-my@x@r3-1: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 15.1 + next 2.2 (from the entry: 15.1, 2.2, 1.2, 1.2); rows composed per frame max 0
M tab-news@x@r3-1: worst 19.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 19.1 + next 1.1 (from the entry: 19.1, 1.1, 8.9, 8.8); rows composed per frame max 0
M tab-home@x@r3-2: worst 16.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (4 with >= 3 ms); entry frame 16.3 + next 1.6 (from the entry: 16.3, 1.6, 1.5, 5.6); rows composed per frame max 0
M tab-news@x@r3-2: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (8 with >= 3 ms); entry frame 9.5 + next 9.5 (from the entry: 9.5, 9.5, 2.9, 1.6); rows composed per frame max 0
M tab-my@x@r3-2: worst 9.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 9.8 + next 1.1 (from the entry: 9.8, 1.1, 1.2, 1.4); rows composed per frame max 0
M tab-more@x@r3-2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 15.6 + next 1.1 (from the entry: 15.6, 1.1, 6.9, 6.8); rows composed per frame max 0
M tab-catalog@x@r3: worst 13.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 8.5 + next 2.1 (from the entry: 8.5, 2.1, 13.4, 13.4); rows composed per frame max 0
M tab-home@x@r3-3: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 13.8 + next 1.7 (from the entry: 13.8, 1.7, 8.9, 8.8); rows composed per frame max 0
## E4. Round 4: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r4: worst 21.1 ms CPU (rows composed in it 0); frames >16 4, >25 0, >30 0 of 26 (8 with >= 3 ms); entry frame 20.8 + next 20.7 (from the entry: 20.8, 20.7, 2.1, 2.0); rows composed per frame max 0
M tab-my-cold@x@r4: worst 22.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 43 (5 with >= 3 ms); entry frame 22.3 + next 3.2 (from the entry: 22.3, 3.2, 3.2, 1.6); rows composed per frame max 0
M tab-more-cold@x@r4: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 33 (6 with >= 3 ms); entry frame 11.6 + next 11.4 (from the entry: 11.6, 11.4, 1.9, 1.8); rows composed per frame max 0
M tab-catalog-cold@x@r4: worst 33.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 25 (8 with >= 3 ms); entry frame 15.6 + next 3.6 (from the entry: 15.6, 3.6, 3.5, 33.9); rows composed per frame max 0
M tab-home@x@r4-1: worst 17.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 44 (7 with >= 3 ms); entry frame 17.0 + next 4.3 (from the entry: 17.0, 4.3, 4.2, 7.5); rows composed per frame max 0
M tab-more@x@r4-1: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-my@x@r4-1: worst 10.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 10.0 + next 1.5 (from the entry: 10.0, 1.5, 1.4, 1.3); rows composed per frame max 0
M tab-news@x@r4-1: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (7 with >= 3 ms); entry frame 15.3 + next 1.1 (from the entry: 15.3, 1.1, 8.0, 8.0); rows composed per frame max 0
M tab-home@x@r4-2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 49 (8 with >= 3 ms); entry frame 15.6 + next 2.2 (from the entry: 15.6, 2.2, 2.2, 7.0); rows composed per frame max 0
M tab-news@x@r4-2: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (7 with >= 3 ms); entry frame 12.2 + next 12.1 (from the entry: 12.2, 12.1, 2.1, 2.0); rows composed per frame max 0
M tab-my@x@r4-2: worst 14.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 14.5 + next 1.3 (from the entry: 14.5, 1.3, 1.6, 1.5); rows composed per frame max 0
M tab-more@x@r4-2: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 12.0 + next 1.0 (from the entry: 12.0, 1.0, 6.2, 6.2); rows composed per frame max 0
M tab-catalog@x@r4: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 10.5 + next 2.4 (from the entry: 10.5, 2.4, 2.3, 10.0); rows composed per frame max 0
M tab-home@x@r4-3: worst 14.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (6 with >= 3 ms); entry frame 14.6 + next 1.5 (from the entry: 14.6, 1.5, 5.4, 5.3); rows composed per frame max 0
M tab-news-cold@base@r4: worst 20.5 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 28 (8 with >= 3 ms); entry frame 20.5 + next 20.4 (from the entry: 20.5, 20.4, 1.5, 1.4); rows composed per frame max 0
M tab-my-cold@base@r4: worst 22.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 41 (7 with >= 3 ms); entry frame 22.8 + next 3.5 (from the entry: 22.8, 3.5, 3.3, 0.7); rows composed per frame max 0
M tab-more-cold@base@r4: worst 20.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 28 (2 with >= 3 ms); entry frame 20.2 + next 20.0 (from the entry: 20.2, 20.0, 1.9, 1.8); rows composed per frame max 0
M tab-catalog-cold@base@r4: worst 26.2 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 0 of 22 (8 with >= 3 ms); entry frame 20.7 + next 5.7 (from the entry: 20.7, 5.7, 3.8, 3.7); rows composed per frame max 0
M tab-home@base@r4-1: worst 16.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 43 (4 with >= 3 ms); entry frame 16.2 + next 1.8 (from the entry: 16.2, 1.8, 1.6, 0.6); rows composed per frame max 0
M tab-more@base@r4-1: worst 22.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 14 (5 with >= 3 ms); entry frame 22.5 + next 3.3 (from the entry: 22.5, 3.3, 3.2, 1.0); rows composed per frame max 0
M tab-my@base@r4-1: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 12.0 + next 1.2 (from the entry: 12.0, 1.2, 1.5, 1.5); rows composed per frame max 0
M tab-news@base@r4-1: worst 25.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 16 (3 with >= 3 ms); entry frame 25.7 + next 2.1 (from the entry: 25.7, 2.1, 1.9, 0.6); rows composed per frame max 0
M tab-home@base@r4-2: worst 26.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 19 (5 with >= 3 ms); entry frame 26.3 + next 1.5 (from the entry: 26.3, 1.5, 1.5, 0.6); rows composed per frame max 0
M tab-news@base@r4-2: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (7 with >= 3 ms); entry frame 12.9 + next 12.8 (from the entry: 12.9, 12.8, 2.1, 2.0); rows composed per frame max 0
M tab-my@base@r4-2: worst 13.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 13.2 + next 1.3 (from the entry: 13.2, 1.3, 1.4, 1.4); rows composed per frame max 0
M tab-more@base@r4-2: worst 23.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 23.7 + next 1.7 (from the entry: 23.7, 1.7, 1.7, 0.9); rows composed per frame max 0
M tab-catalog@base@r4: worst 14.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 14.8 + next 1.6 (from the entry: 14.8, 1.6, 10.4, 10.4); rows composed per frame max 0
M tab-home@base@r4-3: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 12.7 + next 1.3 (from the entry: 12.7, 1.3, 1.6, 1.5); rows composed per frame max 0
## E5. Round 5: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r5: worst 20.9 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 26 (8 with >= 3 ms); entry frame 20.9 + next 20.7 (from the entry: 20.9, 20.7, 1.7, 1.6); rows composed per frame max 0
M tab-my-cold@base@r5: worst 15.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (7 with >= 3 ms); entry frame 15.7 + next 3.3 (from the entry: 15.7, 3.3, 3.3, 1.5); rows composed per frame max 0
M tab-more-cold@base@r5: worst 15.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 28 (4 with >= 3 ms); entry frame 15.4 + next 15.3 (from the entry: 15.4, 15.3, 1.9, 1.8); rows composed per frame max 0
M tab-catalog-cold@base@r5: worst 28.1 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 26 (7 with >= 3 ms); entry frame 12.2 + next 3.5 (from the entry: 12.2, 3.5, 3.4, 28.1); rows composed per frame max 0
M tab-home@base@r5-1: worst 23.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 44 (6 with >= 3 ms); entry frame 23.6 + next 3.9 (from the entry: 23.6, 3.9, 3.8, 1.3); rows composed per frame max 0
M tab-more@base@r5-1: worst 11.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (4 with >= 3 ms); entry frame 11.3 + next 11.3 (from the entry: 11.3, 11.3, 1.9, 1.8); rows composed per frame max 0
M tab-my@base@r5-1: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 12.2 + next 1.3 (from the entry: 12.2, 1.3, 4.3, 4.3); rows composed per frame max 0
M tab-news@base@r5-1: worst 22.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 22.5 + next 2.8 (from the entry: 22.5, 2.8, 2.8, 1.7); rows composed per frame max 0
M tab-home@base@r5-2: worst 22.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 21 (2 with >= 3 ms); entry frame 22.1 + next 1.5 (from the entry: 22.1, 1.5, 1.5, 0.8); rows composed per frame max 0
M tab-news@base@r5-2: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (4 with >= 3 ms); entry frame 11.9 + next 11.9 (from the entry: 11.9, 11.9, 1.7, 1.6); rows composed per frame max 0
M tab-my@base@r5-2: worst 11.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 11.6 + next 1.2 (from the entry: 11.6, 1.2, 1.6, 1.5); rows composed per frame max 0
M tab-more@base@r5-2: worst 22.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 22.7 + next 1.7 (from the entry: 22.7, 1.7, 1.7, 1.3); rows composed per frame max 0
M tab-catalog@base@r5: worst 20.3 ms CPU (rows composed in it 0); frames >16 3, >25 0, >30 0 of 16 (5 with >= 3 ms); entry frame 20.3 + next 2.6 (from the entry: 20.3, 2.6, 2.6, 16.6); rows composed per frame max 0
M tab-home@base@r5-3: worst 14.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 14.0 + next 1.3 (from the entry: 14.0, 1.3, 1.8, 1.7); rows composed per frame max 0
M tab-news-cold@x@r5: worst 9.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 30 (10 with >= 3 ms); entry frame 9.0 + next 8.9 (from the entry: 9.0, 8.9, 1.7, 1.6); rows composed per frame max 0
M tab-my-cold@x@r5: worst 9.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 43 (2 with >= 3 ms); entry frame 9.3 + next 2.9 (from the entry: 9.3, 2.9, 2.9, 0.9); rows composed per frame max 0
M tab-more-cold@x@r5: worst 11.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 36 (10 with >= 3 ms); entry frame 11.0 + next 10.9 (from the entry: 11.0, 10.9, 5.1, 5.0); rows composed per frame max 0
M tab-catalog-cold@x@r5: worst 25.9 ms CPU (rows composed in it 0); frames >16 3, >25 2, >30 0 of 28 (9 with >= 3 ms); entry frame 20.4 + next 3.3 (from the entry: 20.4, 3.3, 3.2, 4.5); rows composed per frame max 0
M tab-home@x@r5-1: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 46 (6 with >= 3 ms); entry frame 15.5 + next 1.8 (from the entry: 15.5, 1.8, 1.8, 5.7); rows composed per frame max 0
M tab-more@x@r5-1: worst 8.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (4 with >= 3 ms); entry frame 8.2 + next 1.8 (from the entry: 8.2, 1.8, 1.7, 8.3); rows composed per frame max 0
M tab-my@x@r5-1: worst 11.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 21 (1 with >= 3 ms); entry frame 11.7 + next 1.1 (from the entry: 11.7, 1.1, 1.3, 1.2); rows composed per frame max 0
M tab-news@x@r5-1: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (3 with >= 3 ms); entry frame 12.2 + next 1.2 (from the entry: 12.2, 1.2, 10.0, 9.9); rows composed per frame max 0
M tab-home@x@r5-2: worst 23.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 19 (4 with >= 3 ms); entry frame 23.6 + next 1.8 (from the entry: 23.6, 1.8, 1.7, 5.5); rows composed per frame max 0
M tab-news@x@r5-2: worst 13.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (8 with >= 3 ms); entry frame 13.1 + next 13.0 (from the entry: 13.1, 13.0, 1.9, 1.8); rows composed per frame max 0
M tab-my@x@r5-2: worst 17.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 20 (4 with >= 3 ms); entry frame 17.7 + next 1.2 (from the entry: 17.7, 1.2, 3.4, 3.4); rows composed per frame max 0
M tab-more@x@r5-2: worst 13.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 13.4 + next 1.1 (from the entry: 13.4, 1.1, 7.5, 7.4); rows composed per frame max 0
M tab-catalog@x@r5: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 10.2 + next 1.9 (from the entry: 10.2, 1.9, 15.1, 15.1); rows composed per frame max 0
M tab-home@x@r5-3: worst 18.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (7 with >= 3 ms); entry frame 18.1 + next 2.0 (from the entry: 18.1, 2.0, 2.0, 6.4); rows composed per frame max 0
## EV. Visual: the tab entries recorded at 8 frames/s
REC tab-news-base (emu): marker 1702 ms after the recording start; 46 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-base (emu): marker 1544 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-base (emu): marker 1537 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-news-x (emu): marker 1624 ms after the recording start; 47 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-more-x (emu): marker 1546 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
REC tab-home-x (emu): marker 1543 ms after the recording start; 44 frames at 8/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
tab-news-cold | base | 5 | 17.1 / 17.6 / 20.9 | 17.1 | 17.0 | 0 | 0 | 0 | 94
tab-news-cold | x | 5 | 10.3 / 12.2 / 21.1 | 10.3 | 10.2 | 0 | 0 | 0 | 71
tab-my-cold | base | 5 | 18.0 / 18.5 / 22.8 | 18.0 | 3.3 | 0 | 0 | 0 | 56
tab-my-cold | x | 5 | 19.8 / 17.7 / 23.5 | 19.8 | 3.2 | 0 | 0 | 0 | 53
tab-more-cold | base | 5 | 20.2 / 21.5 / 32.1 | 20.2 | 20.0 | 2 | 2 | 0 | 72
tab-more-cold | x | 5 | 12.9 / 13.9 / 17.8 | 12.2 | 12.2 | 0 | 0 | 0 | 85
tab-catalog-cold | base | 5 | 26.0 / 23.4 / 28.1 | 15.1 | 3.0 | 6 | 0 | 0 | 105
tab-catalog-cold | x | 5 | 31.1 / 30.9 / 33.9 | 22.9 | 3.2 | 8 | 6 | 0 | 120
tab-home | base | 15 | 20.1 / 19.8 / 30.4 | 20.1 | 1.6 | 3 | 1 | 0 | 51
tab-home | x | 15 | 15.6 / 15.6 / 23.6 | 15.6 | 1.8 | 0 | 0 | 0 | 57
tab-more | base | 10 | 22.6 / 20.6 / 28.4 | 22.6 | 1.9 | 1 | 0 | 0 | 48
tab-more | x | 10 | 12.0 / 11.8 / 16.1 | 12.0 | 1.1 | 0 | 0 | 0 | 41
tab-my | base | 10 | 12.6 / 13.2 / 19.2 | 12.6 | 1.4 | 0 | 0 | 0 | 36
tab-my | x | 10 | 13.3 / 12.8 / 17.7 | 13.3 | 1.3 | 0 | 0 | 0 | 37
tab-news | base | 10 | 20.0 / 18.9 / 25.7 | 20.0 | 7.3 | 3 | 0 | 0 | 46
tab-news | x | 10 | 12.6 / 14.2 / 20.0 | 12.6 | 5.8 | 0 | 0 | 0 | 57
tab-catalog | base | 5 | 14.9 / 16.8 / 20.7 | 14.8 | 1.9 | 0 | 0 | 0 | 57
tab-catalog | x | 5 | 14.4 / 15.4 / 23.6 | 10.5 | 2.1 | 0 | 0 | 0 | 53
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 15
contact sheets: 20 in sheets/
contact sheets: 20 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['3778', '5911', '6723', '7287', '8866', '9424', '10983', '11696']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 24
   skipped-frames events=24 max=150 total=1613
== Davey: 35
   davey events=35 max=2690ms median=1302ms
== NativeLink: 8
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## tb33

```
part tb33; APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 33
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 3365 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~oonuBpAZcokVPnzGpKWV_w==/org.chyavorec.app-wDFYSH70vcgQ2pQscQaRGg==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 2714 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~MRyL5eOaBMasNzH6u9gsZw==/org.chyavorec.app.b-5KuEwja-venfqeTSczMFDQ==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/LiveWallpapersPicker/LiveWallpapersPicker.apk| x86_64: [status=verify] [reason=prebuilt]|
tabs: catalog [320 2274] home [99 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## E1. Round 1: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r1: worst 14.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (7 with >= 3 ms); entry frame 13.2 + next 4.6 (from the entry: 13.2, 4.6, 1.6, 14.4); rows composed per frame max 0
M tab-my-cold@base@r1: worst 17.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 45 (5 with >= 3 ms); entry frame 17.4 + next 6.3 (from the entry: 17.4, 6.3, 1.4, 3.7); rows composed per frame max 0
M tab-more-cold@base@r1: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (6 with >= 3 ms); entry frame 12.2 + next 5.1 (from the entry: 12.2, 5.1, 3.2, 1.7); rows composed per frame max 0
M tab-catalog-cold@base@r1: worst 25.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 14 (6 with >= 3 ms); entry frame 12.5 + next 3.1 (from the entry: 12.5, 3.1, 25.4, 5.2); rows composed per frame max 0
M tab-home@base@r1-1: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 55 (3 with >= 3 ms); entry frame 12.7 + next 1.7 (from the entry: 12.7, 1.7, 1.7, 0.9); rows composed per frame max 0
M tab-more@base@r1-1: worst 21.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 21.1 + next 2.4 (from the entry: 21.1, 2.4, 0.8, 1.2); rows composed per frame max 0
M tab-my@base@r1-1: worst 8.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@base@r1-1: worst 27.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 11 (2 with >= 3 ms); entry frame 27.6 + next 1.7 (from the entry: 27.6, 1.7, 1.0, 0.8); rows composed per frame max 0
M tab-home@base@r1-2: worst 13.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (5 with >= 3 ms); entry frame 13.9 + next 1.6 (from the entry: 13.9, 1.6, 1.4, 5.0); rows composed per frame max 0
M tab-news@base@r1-2: worst 24.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 8.2 + next 24.3 (from the entry: 8.2, 24.3, 1.8, 1.4); rows composed per frame max 0
M tab-my@base@r1-2: worst 14.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 14.1 + next 1.5 (from the entry: 14.1, 1.5, 1.3, 1.4); rows composed per frame max 0
M tab-more@base@r1-2: worst 11.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (1 with >= 3 ms); entry frame 11.3 + next 1.7 (from the entry: 11.3, 1.7, 1.5, 2.5); rows composed per frame max 0
M tab-catalog@base@r1: worst 15.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 15.5 + next 1.7 (from the entry: 15.5, 1.7, 15.8, 1.7); rows composed per frame max 0
M tab-home@base@r1-3: worst 16.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 13 (2 with >= 3 ms); entry frame 16.7 + next 1.3 (from the entry: 16.7, 1.3, 1.3, 0.9); rows composed per frame max 0
M tab-news-cold@x@r1: worst 19.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 26 (6 with >= 3 ms); entry frame 19.3 + next 2.0 (from the entry: 19.3, 2.0, 1.4, 13.7); rows composed per frame max 0
M tab-my-cold@x@r1: worst 11.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 42 (5 with >= 3 ms); entry frame 11.1 + next 9.4 (from the entry: 11.1, 9.4, 1.8, 3.4); rows composed per frame max 0
M tab-more-cold@x@r1: worst 9.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 9.4 + next 1.6 (from the entry: 9.4, 1.6, 1.7, 0.9); rows composed per frame max 0
M tab-catalog-cold@x@r1: worst 22.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 22.8 + next 7.5 (from the entry: 22.8, 7.5, 5.5, 15.1); rows composed per frame max 7
M tab-home@x@r1-1: worst 11.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 59 (4 with >= 3 ms); entry frame 11.1 + next 1.6 (from the entry: 11.1, 1.6, 1.2, 1.1); rows composed per frame max 0
M tab-more@x@r1-1: worst 12.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 12.7 + next 1.4 (from the entry: 12.7, 1.4, 0.6, 1.0); rows composed per frame max 0
M tab-my@x@r1-1: worst 8.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 8.7 + next 0.9 (from the entry: 8.7, 0.9, 0.2, 0.0); rows composed per frame max 0
M tab-news@x@r1-1: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (2 with >= 3 ms); entry frame 15.6 + next 1.9 (from the entry: 15.6, 1.9, 0.6, 1.3); rows composed per frame max 0
M tab-home@x@r1-2: worst 29.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 13 (3 with >= 3 ms); entry frame 29.8 + next 1.8 (from the entry: 29.8, 1.8, 1.3, 0.9); rows composed per frame max 0
M tab-news@x@r1-2: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (4 with >= 3 ms); entry frame 13.0 + next 3.0 (from the entry: 13.0, 3.0, 1.2, 0.7); rows composed per frame max 0
M tab-my@x@r1-2: worst 11.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (2 with >= 3 ms); entry frame 11.2 + next 1.0 (from the entry: 11.2, 1.0, 1.2, 4.4); rows composed per frame max 0
M tab-more@x@r1-2: worst 11.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 11.5 + next 1.6 (from the entry: 11.5, 1.6, 1.3, 1.3); rows composed per frame max 0
M tab-catalog@x@r1: worst 17.0 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 17.0 + next 1.6 (from the entry: 17.0, 1.6, 0.8, 0.9); rows composed per frame max 7
M tab-home@x@r1-3: worst 11.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 11.7 + next 1.9 (from the entry: 11.7, 1.9, 1.0, 0.8); rows composed per frame max 0
## E2. Round 2: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r2: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 9.7 + next 2.9 (from the entry: 9.7, 2.9, 15.3, 1.9); rows composed per frame max 0
M tab-my-cold@x@r2: worst 15.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 47 (3 with >= 3 ms); entry frame 15.0 + next 3.8 (from the entry: 15.0, 3.8, 1.5, 1.2); rows composed per frame max 0
M tab-more-cold@x@r2: worst 9.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 9.0 + next 1.6 (from the entry: 9.0, 1.6, 5.8, 1.0); rows composed per frame max 0
M tab-catalog-cold@x@r2: worst 12.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 8.5 + next 1.7 (from the entry: 8.5, 1.7, 12.8, 2.2); rows composed per frame max 7
M tab-home@x@r2-1: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 56 (3 with >= 3 ms); entry frame 13.3 + next 1.5 (from the entry: 13.3, 1.5, 1.7, 1.0); rows composed per frame max 0
M tab-more@x@r2-1: worst 19.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (5 with >= 3 ms); entry frame 19.9 + next 1.8 (from the entry: 19.9, 1.8, 4.2, 0.8); rows composed per frame max 0
M tab-my@x@r2-1: worst 7.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@x@r2-1: worst 14.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 14.3 + next 1.3 (from the entry: 14.3, 1.3, 1.3, 1.2); rows composed per frame max 0
M tab-home@x@r2-2: worst 11.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 62 (2 with >= 3 ms); entry frame 11.1 + next 0.9 (from the entry: 11.1, 0.9, 1.3, 1.8); rows composed per frame max 0
M tab-news@x@r2-2: worst 18.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (5 with >= 3 ms); entry frame 18.5 + next 1.6 (from the entry: 18.5, 1.6, 4.3, 0.9); rows composed per frame max 0
M tab-my@x@r2-2: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (2 with >= 3 ms); entry frame 9.5 + next 1.0 (from the entry: 9.5, 1.0, 1.2, 1.2); rows composed per frame max 0
M tab-more@x@r2-2: worst 12.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 12.1 + next 1.6 (from the entry: 12.1, 1.6, 1.3, 1.3); rows composed per frame max 0
M tab-catalog@x@r2: worst 12.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 9.7 + next 2.2 (from the entry: 9.7, 2.2, 12.5, 1.3); rows composed per frame max 7
M tab-home@x@r2-3: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 62 (2 with >= 3 ms); entry frame 13.0 + next 1.6 (from the entry: 13.0, 1.6, 0.9, 0.7); rows composed per frame max 0
M tab-news-cold@base@r2: worst 14.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (6 with >= 3 ms); entry frame 14.1 + next 1.5 (from the entry: 14.1, 1.5, 5.7, 7.5); rows composed per frame max 0
M tab-my-cold@base@r2: worst 18.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 50 (1 with >= 3 ms); entry frame 18.7 + next 1.2 (from the entry: 18.7, 1.2, 2.4, 1.4); rows composed per frame max 0
M tab-more-cold@base@r2: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 15.3 + next 1.4 (from the entry: 15.3, 1.4, 4.4, 1.1); rows composed per frame max 0
M tab-catalog-cold@base@r2: worst 17.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (4 with >= 3 ms); entry frame 9.5 + next 1.9 (from the entry: 9.5, 1.9, 9.7, 17.1); rows composed per frame max 0
M tab-home@base@r2-1: worst 12.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 57 (3 with >= 3 ms); entry frame 12.1 + next 3.9 (from the entry: 12.1, 3.9, 0.6, 0.8); rows composed per frame max 0
M tab-more@base@r2-1: worst 18.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 18.6 + next 2.1 (from the entry: 18.6, 2.1, 1.5, 0.8); rows composed per frame max 0
M tab-my@base@r2-1: worst 9.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 9.0 + next 1.5 (from the entry: 9.0, 1.5, 1.3, 1.4); rows composed per frame max 0
M tab-news@base@r2-1: worst 18.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (1 with >= 3 ms); entry frame 18.1 + next 1.8 (from the entry: 18.1, 1.8, 1.3, 1.4); rows composed per frame max 0
M tab-home@base@r2-2: worst 19.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 57 (2 with >= 3 ms); entry frame 19.1 + next 2.0 (from the entry: 19.1, 2.0, 1.1, 1.4); rows composed per frame max 0
M tab-news@base@r2-2: worst 25.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 11 (3 with >= 3 ms); entry frame 8.3 + next 25.7 (from the entry: 8.3, 25.7, 2.0, 1.2); rows composed per frame max 0
M tab-my@base@r2-2: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (1 with >= 3 ms); entry frame 14.9 + next 2.0 (from the entry: 14.9, 2.0, 1.4, 1.1); rows composed per frame max 0
M tab-more@base@r2-2: worst 14.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (1 with >= 3 ms); entry frame 14.0 + next 1.7 (from the entry: 14.0, 1.7, 1.3, 0.7); rows composed per frame max 0
M tab-catalog@base@r2: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 10.7 + next 5.2 (from the entry: 10.7, 5.2, 8.7, 1.1); rows composed per frame max 0
M tab-home@base@r2-3: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 54 (3 with >= 3 ms); entry frame 11.9 + next 1.3 (from the entry: 11.9, 1.3, 4.8, 0.9); rows composed per frame max 0
## E3. Round 3: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r3: worst 10.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (3 with >= 3 ms); entry frame 10.1 + next 1.6 (from the entry: 10.1, 1.6, 2.0, 8.3); rows composed per frame max 0
M tab-my-cold@base@r3: worst 14.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 53 (3 with >= 3 ms); entry frame 14.7 + next 3.5 (from the entry: 14.7, 3.5, 0.7, 1.3); rows composed per frame max 0
M tab-more-cold@base@r3: worst 8.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 8.9 + next 1.5 (from the entry: 8.9, 1.5, 2.1, 1.2); rows composed per frame max 0
M tab-catalog-cold@base@r3: worst 28.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 15 (5 with >= 3 ms); entry frame 10.0 + next 4.2 (from the entry: 10.0, 4.2, 28.1, 2.4); rows composed per frame max 0
M tab-home@base@r3-1: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 12.0 + next 1.8 (from the entry: 12.0, 1.8, 0.8, 1.1); rows composed per frame max 0
M tab-more@base@r3-1: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 13.3 + next 1.8 (from the entry: 13.3, 1.8, 1.3, 0.7); rows composed per frame max 0
M tab-my@base@r3-1: worst 9.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 9.6 + next 1.4 (from the entry: 9.6, 1.4, 1.2, 1.0); rows composed per frame max 0
M tab-news@base@r3-1: worst 13.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 13.3 + next 1.3 (from the entry: 13.3, 1.3, 1.6, 1.2); rows composed per frame max 0
M tab-home@base@r3-2: worst 22.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 13 (3 with >= 3 ms); entry frame 22.6 + next 1.9 (from the entry: 22.6, 1.9, 1.6, 1.2); rows composed per frame max 0
M tab-news@base@r3-2: worst 20.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 20.0 + next 1.4 (from the entry: 20.0, 1.4, 1.0, 0.8); rows composed per frame max 0
M tab-my@base@r3-2: worst 19.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (4 with >= 3 ms); entry frame 19.3 + next 2.4 (from the entry: 19.3, 2.4, 7.4, 1.1); rows composed per frame max 0
M tab-more@base@r3-2: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (4 with >= 3 ms); entry frame 10.3 + next 2.2 (from the entry: 10.3, 2.2, 1.3, 4.4); rows composed per frame max 0
M tab-catalog@base@r3: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (4 with >= 3 ms); entry frame 8.0 + next 1.2 (from the entry: 8.0, 1.2, 11.9, 1.2); rows composed per frame max 0
M tab-home@base@r3-3: worst 10.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 10.7 + next 1.0 (from the entry: 10.7, 1.0, 1.3, 0.8); rows composed per frame max 0
M tab-news-cold@x@r3: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 10.5 + next 2.0 (from the entry: 10.5, 2.0, 1.1, 1.1); rows composed per frame max 0
M tab-my-cold@x@r3: worst 14.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 49 (1 with >= 3 ms); entry frame 14.3 + next 2.7 (from the entry: 14.3, 2.7, 1.3, 1.1); rows composed per frame max 0
M tab-more-cold@x@r3: worst 18.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 18.2 + next 1.5 (from the entry: 18.2, 1.5, 3.2, 1.2); rows composed per frame max 0
M tab-catalog-cold@x@r3: worst 19.0 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 14 (6 with >= 3 ms); entry frame 15.1 + next 3.3 (from the entry: 15.1, 3.3, 19.0, 5.8); rows composed per frame max 7
M tab-home@x@r3-1: worst 9.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (3 with >= 3 ms); entry frame 9.7 + next 1.8 (from the entry: 9.7, 1.8, 1.8, 0.9); rows composed per frame max 0
M tab-more@x@r3-1: worst 16.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 16.2 + next 2.0 (from the entry: 16.2, 2.0, 1.3, 0.9); rows composed per frame max 0
M tab-my@x@r3-1: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@x@r3-1: worst 13.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (1 with >= 3 ms); entry frame 13.5 + next 1.7 (from the entry: 13.5, 1.7, 1.3, 1.2); rows composed per frame max 0
M tab-home@x@r3-2: worst 18.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 65 (1 with >= 3 ms); entry frame 18.5 + next 1.1 (from the entry: 18.5, 1.1, 1.3, 1.4); rows composed per frame max 0
M tab-news@x@r3-2: worst 19.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (2 with >= 3 ms); entry frame 19.9 + next 1.4 (from the entry: 19.9, 1.4, 1.0, 0.7); rows composed per frame max 0
M tab-my@x@r3-2: worst 8.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 8.2 + next 1.7 (from the entry: 8.2, 1.7, 0.7, 1.2); rows composed per frame max 0
M tab-more@x@r3-2: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 15.3 + next 1.7 (from the entry: 15.3, 1.7, 1.2, 1.0); rows composed per frame max 0
M tab-catalog@x@r3: worst 8.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 13 (3 with >= 3 ms); entry frame 8.2 + next 1.3 (from the entry: 8.2, 1.3, 1.1, 0.7); rows composed per frame max 7
M tab-home@x@r3-3: worst 11.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 59 (2 with >= 3 ms); entry frame 11.1 + next 1.1 (from the entry: 11.1, 1.1, 1.4, 0.9); rows composed per frame max 0
## E4. Round 4: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r4: worst 12.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (4 with >= 3 ms); entry frame 10.8 + next 1.8 (from the entry: 10.8, 1.8, 1.1, 0.8); rows composed per frame max 0
M tab-my-cold@x@r4: worst 13.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 48 (2 with >= 3 ms); entry frame 13.8 + next 1.9 (from the entry: 13.8, 1.9, 1.4, 1.2); rows composed per frame max 0
M tab-more-cold@x@r4: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 13.0 + next 1.8 (from the entry: 13.0, 1.8, 1.8, 0.8); rows composed per frame max 0
M tab-catalog-cold@x@r4: worst 18.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 15 (4 with >= 3 ms); entry frame 18.5 + next 1.9 (from the entry: 18.5, 1.9, 15.1, 4.5); rows composed per frame max 7
M tab-home@x@r4-1: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 60 (1 with >= 3 ms); entry frame 12.2 + next 2.4 (from the entry: 12.2, 2.4, 0.9, 1.2); rows composed per frame max 0
M tab-more@x@r4-1: worst 22.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 10 (3 with >= 3 ms); entry frame 22.4 + next 2.4 (from the entry: 22.4, 2.4, 0.8, 0.9); rows composed per frame max 0
M tab-my@x@r4-1: worst 10.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 10.6 + next 2.8 (from the entry: 10.6, 2.8, 0.1, 0.0); rows composed per frame max 0
M tab-news@x@r4-1: worst 13.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (1 with >= 3 ms); entry frame 13.6 + next 2.0 (from the entry: 13.6, 2.0, 1.0, 1.2); rows composed per frame max 0
M tab-home@x@r4-2: worst 21.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 66 (3 with >= 3 ms); entry frame 21.9 + next 3.7 (from the entry: 21.9, 3.7, 1.3, 5.7); rows composed per frame max 0
M tab-news@x@r4-2: worst 12.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (4 with >= 3 ms); entry frame 12.0 + next 1.9 (from the entry: 12.0, 1.9, 1.1, 3.4); rows composed per frame max 0
M tab-my@x@r4-2: worst 11.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 11.0 + next 1.5 (from the entry: 11.0, 1.5, 0.7, 1.3); rows composed per frame max 0
M tab-more@x@r4-2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 15.6 + next 1.4 (from the entry: 15.6, 1.4, 0.9, 1.3); rows composed per frame max 0
M tab-catalog@x@r4: worst 8.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 8.3 + next 1.9 (from the entry: 8.3, 1.9, 8.7, 1.3); rows composed per frame max 7
M tab-home@x@r4-3: worst 14.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 63 (3 with >= 3 ms); entry frame 14.8 + next 1.9 (from the entry: 14.8, 1.9, 1.4, 1.0); rows composed per frame max 0
M tab-news-cold@base@r4: worst 12.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 14 (3 with >= 3 ms); entry frame 11.0 + next 1.9 (from the entry: 11.0, 1.9, 12.5, 1.2); rows composed per frame max 0
M tab-my-cold@base@r4: worst 8.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 55 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-more-cold@base@r4: worst 20.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 20.9 + next 1.4 (from the entry: 20.9, 1.4, 5.7, 1.1); rows composed per frame max 0
M tab-catalog-cold@base@r4: worst 10.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 8.7 + next 1.9 (from the entry: 8.7, 1.9, 10.4, 1.5); rows composed per frame max 0
M tab-home@base@r4-1: worst 9.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 51 (3 with >= 3 ms); entry frame 9.5 + next 1.8 (from the entry: 9.5, 1.8, 1.3, 0.9); rows composed per frame max 0
M tab-more@base@r4-1: worst 16.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 11 (3 with >= 3 ms); entry frame 16.2 + next 2.0 (from the entry: 16.2, 2.0, 0.9, 0.8); rows composed per frame max 0
M tab-my@base@r4-1: worst 7.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M tab-news@base@r4-1: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 15.6 + next 1.4 (from the entry: 15.6, 1.4, 1.3, 5.6); rows composed per frame max 0
M tab-home@base@r4-2: worst 23.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 56 (2 with >= 3 ms); entry frame 23.4 + next 1.0 (from the entry: 23.4, 1.0, 1.4, 1.5); rows composed per frame max 0
M tab-news@base@r4-2: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (3 with >= 3 ms); entry frame 10.5 + next 1.4 (from the entry: 10.5, 1.4, 1.1, 0.8); rows composed per frame max 0
M tab-my@base@r4-2: worst 11.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 11.6 + next 1.3 (from the entry: 11.6, 1.3, 0.9, 1.5); rows composed per frame max 0
M tab-more@base@r4-2: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 12 (2 with >= 3 ms); entry frame 14.9 + next 1.6 (from the entry: 14.9, 1.6, 1.3, 1.2); rows composed per frame max 0
M tab-catalog@base@r4: worst 12.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 13 (3 with >= 3 ms); entry frame 12.2 + next 1.4 (from the entry: 12.2, 1.4, 11.0, 1.3); rows composed per frame max 0
M tab-home@base@r4-3: worst 12.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 61 (3 with >= 3 ms); entry frame 12.3 + next 0.8 (from the entry: 12.3, 0.8, 1.4, 0.9); rows composed per frame max 0
## EV. Visual: the tab entries recorded at 25 frames/s
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['7417', '8067', '9759', '10355', '11997', '12480']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 16
   skipped-frames events=16 max=80 total=813
== Davey: 26
   davey events=26 max=2033ms median=1086ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## tb36x

```
part tb36x; APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15982 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 36
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 3693 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~WuTN3aMRWRVY3Hej0mV_vw==/org.chyavorec.app-IIxRfiY7m34O9xOWJagc9g==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~JLgdiKDK7hBvNNgWdfz-Ew==/org.chyavorec.app.b-kJfXGd62Jo7K6fF3z5I8qA==/base.apk| x86_64: [status=verify] [reason=install] [primary-abi]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 3162 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~JLgdiKDK7hBvNNgWdfz-Ew==/org.chyavorec.app.b-kJfXGd62Jo7K6fF3z5I8qA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]|
tabs: catalog [320 2274] home [100 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
## E1. Round 1: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r1: worst 14.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (9 with >= 3 ms); entry frame 14.7 + next 14.5 (from the entry: 14.7, 14.5, 2.1, 2.0); rows composed per frame max 0
M tab-my-cold@base@r1: worst 26.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 42 (5 with >= 3 ms); entry frame 26.3 + next 7.7 (from the entry: 26.3, 7.7, 7.6, 1.3); rows composed per frame max 0
M tab-more-cold@base@r1: worst 34.9 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 2 of 22 (5 with >= 3 ms); entry frame 34.9 + next 34.8 (from the entry: 34.9, 34.8, 1.8, 1.7); rows composed per frame max 0
M tab-catalog-cold@base@r1: worst 20.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 23 (12 with >= 3 ms); entry frame 13.5 + next 3.5 (from the entry: 13.5, 3.5, 3.5, 3.9); rows composed per frame max 0
M tab-home@base@r1-1: worst 23.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 37 (1 with >= 3 ms); entry frame 23.1 + next 2.3 (from the entry: 23.1, 2.3, 2.1, 2.9); rows composed per frame max 0
M tab-more@base@r1-1: worst 19.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 15 (6 with >= 3 ms); entry frame 19.7 + next 3.2 (from the entry: 19.7, 3.2, 3.1, 0.7); rows composed per frame max 0
M tab-my@base@r1-1: worst 15.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (7 with >= 3 ms); entry frame 15.0 + next 3.6 (from the entry: 15.0, 3.6, 3.5, 4.7); rows composed per frame max 0
M tab-news@base@r1-1: worst 34.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 14 (1 with >= 3 ms); entry frame 34.9 + next 2.5 (from the entry: 34.9, 2.5, 2.4, 1.1); rows composed per frame max 0
M tab-home@base@r1-2: worst 40.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 18 (3 with >= 3 ms); entry frame 40.8 + next 2.4 (from the entry: 40.8, 2.4, 2.3, 1.2); rows composed per frame max 0
M tab-news@base@r1-2: worst 16.3 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 18 (6 with >= 3 ms); entry frame 16.3 + next 16.3 (from the entry: 16.3, 16.3, 2.3, 2.2); rows composed per frame max 0
M tab-my@base@r1-2: worst 26.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 16 (5 with >= 3 ms); entry frame 26.3 + next 6.5 (from the entry: 26.3, 6.5, 6.4, 1.6); rows composed per frame max 0
M tab-more@base@r1-2: worst 30.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (5 with >= 3 ms); entry frame 30.5 + next 3.7 (from the entry: 30.5, 3.7, 3.7, 1.0); rows composed per frame max 0
M tab-catalog@base@r1: worst 18.6 ms CPU (rows composed in it 0); frames >16 3, >25 0, >30 0 of 16 (5 with >= 3 ms); entry frame 17.2 + next 4.4 (from the entry: 17.2, 4.4, 4.3, 18.6); rows composed per frame max 0
M tab-home@base@r1-3: worst 30.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (5 with >= 3 ms); entry frame 30.2 + next 3.0 (from the entry: 30.2, 3.0, 2.9, 1.1); rows composed per frame max 0
M tab-news-cold@x@r1: worst 33.1 ms CPU (rows composed in it 0); frames >16 4, >25 4, >30 2 of 24 (7 with >= 3 ms); entry frame 25.7 + next 25.6 (from the entry: 25.7, 25.6, 3.2, 3.0); rows composed per frame max 0
M tab-my-cold@x@r1: worst 29.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 37 (5 with >= 3 ms); entry frame 29.1 + next 10.7 (from the entry: 29.1, 10.7, 10.6, 1.5); rows composed per frame max 0
M tab-more-cold@x@r1: worst 21.7 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 26 (6 with >= 3 ms); entry frame 21.7 + next 21.6 (from the entry: 21.7, 21.6, 2.4, 2.3); rows composed per frame max 0
M tab-catalog-cold@x@r1: worst 33.2 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 23 (9 with >= 3 ms); entry frame 21.9 + next 3.8 (from the entry: 21.9, 3.8, 3.8, 33.2); rows composed per frame max 7
M tab-home@x@r1-1: worst 21.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 39 (6 with >= 3 ms); entry frame 21.5 + next 2.8 (from the entry: 21.5, 2.8, 2.6, 3.9); rows composed per frame max 0
M tab-more@x@r1-1: worst 17.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 16 (5 with >= 3 ms); entry frame 17.2 + next 17.1 (from the entry: 17.2, 17.1, 2.7, 2.5); rows composed per frame max 0
M tab-my@x@r1-1: worst 19.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 24 (11 with >= 3 ms); entry frame 19.7 + next 4.5 (from the entry: 19.7, 4.5, 4.4, 5.6); rows composed per frame max 0
M tab-news@x@r1-1: worst 24.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (8 with >= 3 ms); entry frame 24.0 + next 3.1 (from the entry: 24.0, 3.1, 3.0, 6.7); rows composed per frame max 0
M tab-home@x@r1-2: worst 42.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 40 (7 with >= 3 ms); entry frame 42.8 + next 5.1 (from the entry: 42.8, 5.1, 4.9, 0.6); rows composed per frame max 0
M tab-news@x@r1-2: worst 17.9 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 18 (7 with >= 3 ms); entry frame 17.9 + next 17.8 (from the entry: 17.9, 17.8, 3.1, 3.1); rows composed per frame max 0
M tab-my@x@r1-2: worst 19.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 19.4 + next 1.4 (from the entry: 19.4, 1.4, 1.6, 1.5); rows composed per frame max 0
M tab-more@x@r1-2: worst 31.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (3 with >= 3 ms); entry frame 31.6 + next 4.2 (from the entry: 31.6, 4.2, 4.1, 1.4); rows composed per frame max 0
M tab-catalog@x@r1: worst 19.5 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 16 (5 with >= 3 ms); entry frame 11.3 + next 4.3 (from the entry: 11.3, 4.3, 4.2, 19.5); rows composed per frame max 7
M tab-home@x@r1-3: worst 18.1 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 43 (3 with >= 3 ms); entry frame 18.1 + next 1.3 (from the entry: 18.1, 1.3, 1.8, 1.7); rows composed per frame max 0
## E2. Round 2: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r2: worst 18.3 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 25 (11 with >= 3 ms); entry frame 18.3 + next 18.2 (from the entry: 18.3, 18.2, 2.0, 1.9); rows composed per frame max 0
M tab-my-cold@x@r2: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 41 (7 with >= 3 ms); entry frame 15.6 + next 7.0 (from the entry: 15.6, 7.0, 6.8, 1.3); rows composed per frame max 0
M tab-more-cold@x@r2: worst 18.8 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 18.8 + next 17.7 (from the entry: 18.8, 17.7, 2.2, 2.1); rows composed per frame max 0
M tab-catalog-cold@x@r2: worst 25.4 ms CPU (rows composed in it 7); frames >16 2, >25 2, >30 0 of 28 (10 with >= 3 ms); entry frame 14.0 + next 2.9 (from the entry: 14.0, 2.9, 2.8, 4.2); rows composed per frame max 7
M tab-home@x@r2-1: worst 22.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 40 (5 with >= 3 ms); entry frame 22.2 + next 2.4 (from the entry: 22.2, 2.4, 2.2, 6.5); rows composed per frame max 0
M tab-more@x@r2-1: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (4 with >= 3 ms); entry frame 15.1 + next 2.9 (from the entry: 15.1, 2.9, 2.8, 1.6); rows composed per frame max 0
M tab-my@x@r2-1: worst 11.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (2 with >= 3 ms); entry frame 11.3 + next 11.2 (from the entry: 11.3, 11.2, 2.3, 2.3); rows composed per frame max 0
M tab-news@x@r2-1: worst 26.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 15 (3 with >= 3 ms); entry frame 26.3 + next 1.8 (from the entry: 26.3, 1.8, 1.6, 0.7); rows composed per frame max 0
M tab-home@x@r2-2: worst 34.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 18 (5 with >= 3 ms); entry frame 34.7 + next 5.4 (from the entry: 34.7, 5.4, 5.2, 1.2); rows composed per frame max 0
M tab-news@x@r2-2: worst 28.5 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 18 (8 with >= 3 ms); entry frame 28.5 + next 28.4 (from the entry: 28.5, 28.4, 5.0, 4.9); rows composed per frame max 0
M tab-my@x@r2-2: worst 33.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 18 (5 with >= 3 ms); entry frame 33.2 + next 4.4 (from the entry: 33.2, 4.4, 4.3, 1.9); rows composed per frame max 0
M tab-more@x@r2-2: worst 40.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (3 with >= 3 ms); entry frame 40.5 + next 1.6 (from the entry: 40.5, 1.6, 1.5, 1.6); rows composed per frame max 0
M tab-catalog@x@r2: worst 17.9 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 17 (6 with >= 3 ms); entry frame 10.6 + next 3.5 (from the entry: 10.6, 3.5, 17.9, 17.8); rows composed per frame max 7
M tab-home@x@r2-3: worst 23.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 23.9 + next 2.4 (from the entry: 23.9, 2.4, 2.2, 1.1); rows composed per frame max 0
M tab-news-cold@base@r2: worst 23.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 26 (7 with >= 3 ms); entry frame 14.8 + next 14.7 (from the entry: 14.8, 14.7, 2.4, 2.3); rows composed per frame max 0
M tab-my-cold@base@r2: worst 20.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 36 (8 with >= 3 ms); entry frame 20.3 + next 2.2 (from the entry: 20.3, 2.2, 2.1, 2.4); rows composed per frame max 0
M tab-more-cold@base@r2: worst 33.0 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 2 of 27 (3 with >= 3 ms); entry frame 9.3 + next 33.0 (from the entry: 9.3, 33.0, 32.9, 2.2); rows composed per frame max 0
M tab-catalog-cold@base@r2: worst 21.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 23 (5 with >= 3 ms); entry frame 13.9 + next 2.8 (from the entry: 13.9, 2.8, 2.7, 21.3); rows composed per frame max 0
M tab-home@base@r2-1: worst 25.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 13 (4 with >= 3 ms); entry frame 25.3 + next 2.2 (from the entry: 25.3, 2.2, 2.1, 3.0); rows composed per frame max 0
M tab-more@base@r2-1: worst 15.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (6 with >= 3 ms); entry frame 15.6 + next 15.5 (from the entry: 15.6, 15.5, 2.2, 2.1); rows composed per frame max 0
M tab-my@base@r2-1: worst 23.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 20 (3 with >= 3 ms); entry frame 23.5 + next 2.1 (from the entry: 23.5, 2.1, 2.0, 1.2); rows composed per frame max 0
M tab-news@base@r2-1: worst 54.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (1 with >= 3 ms); entry frame 54.6 + next 2.9 (from the entry: 54.6, 2.9, 2.8, 1.5); rows composed per frame max 0
M tab-home@base@r2-2: worst 19.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 20 (6 with >= 3 ms); entry frame 19.6 + next 2.3 (from the entry: 19.6, 2.3, 2.2, 1.2); rows composed per frame max 0
M tab-news@base@r2-2: worst 24.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 18 (14 with >= 3 ms); entry frame 24.0 + next 23.9 (from the entry: 24.0, 23.9, 6.1, 5.9); rows composed per frame max 0
M tab-my@base@r2-2: worst 23.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (5 with >= 3 ms); entry frame 23.7 + next 2.5 (from the entry: 23.7, 2.5, 2.4, 1.7); rows composed per frame max 0
M tab-more@base@r2-2: worst 42.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (3 with >= 3 ms); entry frame 42.6 + next 1.9 (from the entry: 42.6, 1.9, 1.8, 1.5); rows composed per frame max 0
M tab-catalog@base@r2: worst 26.1 ms CPU (rows composed in it 0); frames >16 3, >25 1, >30 0 of 16 (5 with >= 3 ms); entry frame 26.1 + next 2.6 (from the entry: 26.1, 2.6, 2.5, 24.2); rows composed per frame max 0
M tab-home@base@r2-3: worst 20.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (4 with >= 3 ms); entry frame 20.8 + next 2.5 (from the entry: 20.8, 2.5, 2.1, 1.2); rows composed per frame max 0
## E3. Round 3: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@base@r3: worst 22.8 ms CPU (rows composed in it 0); frames >16 4, >25 0, >30 0 of 31 (8 with >= 3 ms); entry frame 22.8 + next 22.7 (from the entry: 22.8, 22.7, 2.3, 2.2); rows composed per frame max 0
M tab-my-cold@base@r3: worst 32.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 38 (5 with >= 3 ms); entry frame 32.5 + next 2.4 (from the entry: 32.5, 2.4, 2.3, 4.4); rows composed per frame max 0
M tab-more-cold@base@r3: worst 19.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 25 (5 with >= 3 ms); entry frame 19.2 + next 19.0 (from the entry: 19.2, 19.0, 1.9, 1.8); rows composed per frame max 0
M tab-catalog-cold@base@r3: worst 24.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 24 (11 with >= 3 ms); entry frame 14.2 + next 3.5 (from the entry: 14.2, 3.5, 3.4, 24.0); rows composed per frame max 0
M tab-home@base@r3-1: worst 23.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 40 (5 with >= 3 ms); entry frame 23.9 + next 2.4 (from the entry: 23.9, 2.4, 2.3, 3.5); rows composed per frame max 0
M tab-more@base@r3-1: worst 33.2 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (4 with >= 3 ms); entry frame 33.2 + next 2.3 (from the entry: 33.2, 2.3, 2.2, 1.0); rows composed per frame max 0
M tab-my@base@r3-1: worst 20.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 22 (3 with >= 3 ms); entry frame 20.9 + next 2.4 (from the entry: 20.9, 2.4, 2.3, 1.2); rows composed per frame max 0
M tab-news@base@r3-1: worst 22.0 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 16 (4 with >= 3 ms); entry frame 22.0 + next 22.0 (from the entry: 22.0, 22.0, 2.3, 2.2); rows composed per frame max 0
M tab-home@base@r3-2: worst 28.1 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 17 (4 with >= 3 ms); entry frame 28.1 + next 2.9 (from the entry: 28.1, 2.9, 2.7, 1.2); rows composed per frame max 0
M tab-news@base@r3-2: worst 15.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (6 with >= 3 ms); entry frame 15.0 + next 14.9 (from the entry: 15.0, 14.9, 2.4, 2.3); rows composed per frame max 0
M tab-my@base@r3-2: worst 15.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (4 with >= 3 ms); entry frame 15.1 + next 1.3 (from the entry: 15.1, 1.3, 1.6, 1.6); rows composed per frame max 0
M tab-more@base@r3-2: worst 33.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (5 with >= 3 ms); entry frame 33.5 + next 5.4 (from the entry: 33.5, 5.4, 5.3, 1.6); rows composed per frame max 0
M tab-catalog@base@r3: worst 20.9 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 20.9 + next 2.0 (from the entry: 20.9, 2.0, 16.0, 16.0); rows composed per frame max 0
M tab-home@base@r3-3: worst 18.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 18.5 + next 2.5 (from the entry: 18.5, 2.5, 2.3, 0.6); rows composed per frame max 0
M tab-news-cold@x@r3: worst 19.8 ms CPU (rows composed in it 0); frames >16 4, >25 0, >30 0 of 28 (7 with >= 3 ms); entry frame 17.7 + next 17.7 (from the entry: 17.7, 17.7, 1.9, 1.8); rows composed per frame max 0
M tab-my-cold@x@r3: worst 16.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 38 (8 with >= 3 ms); entry frame 16.7 + next 4.1 (from the entry: 16.7, 4.1, 4.0, 1.4); rows composed per frame max 0
M tab-more-cold@x@r3: worst 17.8 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 26 (9 with >= 3 ms); entry frame 9.0 + next 17.8 (from the entry: 9.0, 17.8, 17.6, 2.3); rows composed per frame max 0
M tab-catalog-cold@x@r3: worst 26.6 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 23 (8 with >= 3 ms); entry frame 26.6 + next 3.5 (from the entry: 26.6, 3.5, 3.4, 23.1); rows composed per frame max 7
M tab-home@x@r3-1: worst 31.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 17 (2 with >= 3 ms); entry frame 31.5 + next 2.5 (from the entry: 31.5, 2.5, 2.4, 3.2); rows composed per frame max 0
M tab-more@x@r3-1: worst 16.6 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 18 (8 with >= 3 ms); entry frame 16.6 + next 16.5 (from the entry: 16.6, 16.5, 2.4, 2.3); rows composed per frame max 0
M tab-my@x@r3-1: worst 14.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 14.7 + next 2.2 (from the entry: 14.7, 2.2, 2.2, 1.1); rows composed per frame max 0
M tab-news@x@r3-1: worst 51.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 15 (3 with >= 3 ms); entry frame 51.9 + next 1.7 (from the entry: 51.9, 1.7, 1.6, 0.6); rows composed per frame max 0
M tab-home@x@r3-2: worst 37.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 19 (4 with >= 3 ms); entry frame 37.6 + next 2.0 (from the entry: 37.6, 2.0, 1.8, 0.9); rows composed per frame max 0
M tab-news@x@r3-2: worst 30.2 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 2 of 17 (5 with >= 3 ms); entry frame 8.9 + next 8.8 (from the entry: 8.9, 8.8, 30.2, 30.0); rows composed per frame max 0
M tab-my@x@r3-2: worst 16.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 16.4 + next 1.3 (from the entry: 16.4, 1.3, 2.1, 2.1); rows composed per frame max 0
M tab-more@x@r3-2: worst 26.7 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 16 (5 with >= 3 ms); entry frame 26.7 + next 4.1 (from the entry: 26.7, 4.1, 4.0, 1.2); rows composed per frame max 0
M tab-catalog@x@r3: worst 13.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 10.3 + next 2.4 (from the entry: 10.3, 2.4, 2.3, 13.5); rows composed per frame max 7
M tab-home@x@r3-3: worst 23.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (8 with >= 3 ms); entry frame 23.7 + next 2.3 (from the entry: 23.7, 2.3, 2.2, 2.3); rows composed per frame max 0
## E4. Round 4: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries
M tab-news-cold@x@r4: worst 17.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 23 (9 with >= 3 ms); entry frame 17.1 + next 16.9 (from the entry: 17.1, 16.9, 2.4, 2.3); rows composed per frame max 0
M tab-my-cold@x@r4: worst 16.3 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 38 (3 with >= 3 ms); entry frame 16.3 + next 2.4 (from the entry: 16.3, 2.4, 2.3, 0.9); rows composed per frame max 0
M tab-more-cold@x@r4: worst 16.9 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 24 (7 with >= 3 ms); entry frame 16.9 + next 16.8 (from the entry: 16.9, 16.8, 4.2, 4.0); rows composed per frame max 0
M tab-catalog-cold@x@r4: worst 22.6 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 25 (12 with >= 3 ms); entry frame 14.5 + next 4.0 (from the entry: 14.5, 4.0, 3.9, 3.8); rows composed per frame max 7
M tab-home@x@r4-1: worst 17.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 43 (5 with >= 3 ms); entry frame 17.9 + next 1.8 (from the entry: 17.9, 1.8, 1.7, 0.8); rows composed per frame max 0
M tab-more@x@r4-1: worst 16.7 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 16.7 + next 2.3 (from the entry: 16.7, 2.3, 2.2, 1.1); rows composed per frame max 0
M tab-my@x@r4-1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (4 with >= 3 ms); entry frame 10.3 + next 2.2 (from the entry: 10.3, 2.2, 2.2, 1.4); rows composed per frame max 0
M tab-news@x@r4-1: worst 50.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 14 (5 with >= 3 ms); entry frame 50.0 + next 2.3 (from the entry: 50.0, 2.3, 2.2, 1.3); rows composed per frame max 0
M tab-home@x@r4-2: worst 22.2 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 20 (6 with >= 3 ms); entry frame 22.2 + next 6.0 (from the entry: 22.2, 6.0, 5.9, 2.6); rows composed per frame max 0
M tab-news@x@r4-2: worst 14.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (8 with >= 3 ms); entry frame 14.3 + next 14.2 (from the entry: 14.3, 14.2, 3.8, 3.7); rows composed per frame max 0
M tab-my@x@r4-2: worst 23.5 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 23.5 + next 1.7 (from the entry: 23.5, 1.7, 1.3, 1.2); rows composed per frame max 0
M tab-more@x@r4-2: worst 36.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (1 with >= 3 ms); entry frame 36.9 + next 2.2 (from the entry: 36.9, 2.2, 2.1, 1.1); rows composed per frame max 0
M tab-catalog@x@r4: worst 15.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (5 with >= 3 ms); entry frame 15.5 + next 4.2 (from the entry: 15.5, 4.2, 14.8, 14.8); rows composed per frame max 7
M tab-home@x@r4-3: worst 29.5 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 16 (6 with >= 3 ms); entry frame 29.5 + next 2.4 (from the entry: 29.5, 2.4, 2.3, 1.2); rows composed per frame max 0
M tab-news-cold@base@r4: worst 19.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 27 (7 with >= 3 ms); entry frame 15.7 + next 15.4 (from the entry: 15.7, 15.4, 2.5, 2.4); rows composed per frame max 0
M tab-my-cold@base@r4: worst 27.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 39 (5 with >= 3 ms); entry frame 27.0 + next 4.2 (from the entry: 27.0, 4.2, 4.1, 1.4); rows composed per frame max 0
M tab-more-cold@base@r4: worst 29.4 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 25 (7 with >= 3 ms); entry frame 29.4 + next 29.2 (from the entry: 29.4, 29.2, 4.2, 4.1); rows composed per frame max 0
M tab-catalog-cold@base@r4: worst 23.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 26 (9 with >= 3 ms); entry frame 15.2 + next 3.9 (from the entry: 15.2, 3.9, 3.8, 4.4); rows composed per frame max 0
M tab-home@base@r4-1: worst 26.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 39 (4 with >= 3 ms); entry frame 26.0 + next 2.2 (from the entry: 26.0, 2.2, 2.1, 3.0); rows composed per frame max 0
M tab-more@base@r4-1: worst 19.9 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 18 (6 with >= 3 ms); entry frame 19.9 + next 1.8 (from the entry: 19.9, 1.8, 1.8, 5.0); rows composed per frame max 0
M tab-my@base@r4-1: worst 12.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 20 (5 with >= 3 ms); entry frame 12.8 + next 4.0 (from the entry: 12.8, 4.0, 3.9, 1.7); rows composed per frame max 0
M tab-news@base@r4-1: worst 41.0 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (7 with >= 3 ms); entry frame 41.0 + next 4.0 (from the entry: 41.0, 4.0, 3.9, 4.7); rows composed per frame max 0
M tab-home@base@r4-2: worst 27.8 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 17 (6 with >= 3 ms); entry frame 27.8 + next 6.0 (from the entry: 27.8, 6.0, 5.2, 1.6); rows composed per frame max 0
M tab-news@base@r4-2: worst 15.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (6 with >= 3 ms); entry frame 9.9 + next 9.8 (from the entry: 9.9, 9.8, 15.2, 15.1); rows composed per frame max 0
M tab-my@base@r4-2: worst 14.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (5 with >= 3 ms); entry frame 14.1 + next 4.7 (from the entry: 14.1, 4.7, 1.5, 1.5); rows composed per frame max 0
M tab-more@base@r4-2: worst 32.3 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (5 with >= 3 ms); entry frame 32.3 + next 5.2 (from the entry: 32.3, 5.2, 5.1, 2.2); rows composed per frame max 0
M tab-catalog@base@r4: worst 18.1 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 14 (4 with >= 3 ms); entry frame 12.5 + next 3.0 (from the entry: 12.5, 3.0, 2.9, 18.1); rows composed per frame max 0
M tab-home@base@r4-3: worst 33.9 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 16 (5 with >= 3 ms); entry frame 33.9 + next 1.8 (from the entry: 33.9, 1.8, 1.8, 1.4); rows composed per frame max 0
## EV. Visual: the tab entries recorded at 25 frames/s
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['5227', '6195', '7917', '8428', '9969', '10514']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 15
   skipped-frames events=15 max=153 total=1033
== Davey: 28
   davey events=28 max=2001ms median=1224ms
== NativeLink: 6
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## ty33

```
part ty33; APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
IME: default com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME; enabled: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME com.google.android.tts/com.google.android.apps.speech.tts.googletts.settings.asr.voiceime.VoiceInputMethodService org.pc.helper/.Ime 
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 33
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 3436 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~TNutnSrqCr-Cl2B8SmaWoA==/org.chyavorec.app-rEoKEX0WA5WBH-F_imkNfA==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/ProxyHandler/ProxyHandler.apk| x86_64: [status=verify] [reason=prebuilt]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 3236 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~zSbNK73fzwQgiUYVTFqRCQ==/org.chyavorec.app.b-bgtyXiUCC_6oDcTRI8EULw==/base.apk| x86_64: [status=speed-profile] [reason=cmdline]| path: /system/priv-app/LiveWallpapersPicker/LiveWallpapersPicker.apk| x86_64: [status=verify] [reason=prebuilt]|
tabs: catalog [320 2274] home [99 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
IME now: org.pc.helper/.Ime
## T1. Round 1: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@base@r1-1: worst 24.8 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 31 (4 with >= 3 ms); entry frame 8.9 + next 0.5 (from the entry: 8.9, 0.5, 1.2, 1.0); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r1-1: worst 8.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 8.9 + next 0.2 (from the entry: 8.9, 0.2, 0.3, 0.3); rows composed per frame max 0
M type@base@r1-2: worst 21.5 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 29 (5 with >= 3 ms); entry frame 19.3 + next 0.2 (from the entry: 19.3, 0.2, 0.3, 2.2); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r1-2: worst 10.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 10.8 + next 0.9 (from the entry: 10.8, 0.9, 0.7, 1.2); rows composed per frame max 0
M type@base@r1-3: worst 19.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 31 (6 with >= 3 ms); entry frame 19.6 + next 0.3 (from the entry: 19.6, 0.3, 0.3, 3.1); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r1-3: worst 17.0 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 17.0 + next 1.0 (from the entry: 17.0, 1.0, 4.1, 1.2); rows composed per frame max 0
M sort-open@base@r1: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 53 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@base@r1: worst 39.4 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 17 (3 with >= 3 ms); entry frame 39.4 + next 0.2 (from the entry: 39.4, 0.2, 0.4, 1.0); rows composed per frame max 0
M sort-open@base@r1-2: worst 5.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 66 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@base@r1: worst 16.8 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 16.8 + next 0.3 (from the entry: 16.8, 0.3, 0.3, 0.9); rows composed per frame max 0
  load-more button at [540 2045] after 11 swipes (base)
M loadmore@base@r1: worst 3.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r1: worst 11.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 97 (13 with >= 3 ms); entry frame 11.7 + next 1.8 (from the entry: 11.7, 1.8, 1.6, 0.2); rows composed per frame max 0
M drag@base@r1: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x@r1-1: worst 35.3 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 1 of 31 (10 with >= 3 ms); entry frame 18.5 + next 0.2 (from the entry: 18.5, 0.2, 0.3, 4.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r1-1: worst 20.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 20.8 + next 0.2 (from the entry: 20.8, 0.2, 0.6, 1.1); rows composed per frame max 7
M type@x@r1-2: worst 44.2 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 30 (6 with >= 3 ms); entry frame 15.2 + next 0.3 (from the entry: 15.2, 0.3, 0.4, 7.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r1-2: worst 31.3 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 16 (3 with >= 3 ms); entry frame 31.3 + next 3.0 (from the entry: 31.3, 3.0, 1.0, 1.3); rows composed per frame max 7
M type@x@r1-3: worst 25.9 ms CPU (rows composed in it 3); frames >16 3, >25 1, >30 0 of 29 (6 with >= 3 ms); entry frame 16.9 + next 0.4 (from the entry: 16.9, 0.4, 0.4, 3.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r1-3: worst 21.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 21.5 + next 0.5 (from the entry: 21.5, 0.5, 0.3, 0.3); rows composed per frame max 7
M sort-open@x@r1: worst 9.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 34 (2 with >= 3 ms); entry frame 9.0 + next 1.1 (from the entry: 9.0, 1.1, 1.8, 0.6); rows composed per frame max 0
M sort-year@x@r1: worst 15.6 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 10.2 + next 15.6 (from the entry: 10.2, 15.6, 2.1, 0.2); rows composed per frame max 7
M sort-open@x@r1-2: worst 6.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 80 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r1: worst 10.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (4 with >= 3 ms); entry frame 10.5 + next 0.2 (from the entry: 10.5, 0.2, 0.4, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 11 swipes (x)
M loadmore@x@r1: worst 3.9 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r1: worst 30.9 ms CPU (rows composed in it 21); frames >16 3, >25 3, >30 1 of 94 (17 with >= 3 ms); entry frame 8.9 + next 29.1 (from the entry: 8.9, 29.1, 30.9, 0.4); rows composed per frame max 21
M drag@x@r1: worst 6.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x-ta@r1-1: worst 21.0 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 38 (5 with >= 3 ms); entry frame 21.0 + next 0.2 (from the entry: 21.0, 0.2, 0.0, 2.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta): 250 results
M clear@x-ta@r1-1: worst 21.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (1 with >= 3 ms); entry frame 21.4 + next 0.2 (from the entry: 21.4, 0.2, 0.2, 0.0); rows composed per frame max 7
M type@x-ta@r1-2: worst 23.0 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 36 (2 with >= 3 ms); entry frame 18.0 + next 0.2 (from the entry: 18.0, 0.2, 0.0, 2.5); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta): 196 results
M clear@x-ta@r1-2: worst 13.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 13.0 + next 1.1 (from the entry: 13.0, 1.1, 1.0, 1.2); rows composed per frame max 7
M type@x-ta@r1-3: worst 22.8 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 36 (6 with >= 3 ms); entry frame 16.5 + next 0.1 (from the entry: 16.5, 0.1, 0.0, 2.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta): 44 results
M clear@x-ta@r1-3: worst 20.2 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 20.2 + next 0.9 (from the entry: 20.2, 0.9, 1.0, 1.1); rows composed per frame max 7
M sort-open@x-ta@r1: worst 12.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 33 (1 with >= 3 ms); entry frame 12.6 + next 1.7 (from the entry: 12.6, 1.7, 1.6, 0.6); rows composed per frame max 0
M sort-year@x-ta@r1: worst 9.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 9.7 + next 0.1 (from the entry: 9.7, 0.1, 0.5, 0.2); rows composed per frame max 7
M sort-open@x-ta@r1-2: worst 5.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 64 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta@r1: worst 16.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 16.8 + next 0.2 (from the entry: 16.8, 0.2, 0.0, 0.7); rows composed per frame max 7
  load-more button at [540 2013] after 11 swipes (x-ta)
M loadmore@x-ta@r1: worst 2.7 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 17 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 1
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta@r1: worst 41.6 ms CPU (rows composed in it 25); frames >16 1, >25 1, >30 1 of 88 (19 with >= 3 ms); entry frame 9.9 + next 41.6 (from the entry: 9.9, 41.6, 11.1, 6.8); rows composed per frame max 25
M drag@x-ta@r1: worst 19.2 ms CPU (rows composed in it 8); frames >16 1, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 19.2 + next 2.8 (from the entry: 19.2, 2.8, 0.8, 4.2); rows composed per frame max 8
M type@x-tb@r1-1: worst 23.8 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 38 (6 with >= 3 ms); entry frame 17.0 + next 0.2 (from the entry: 17.0, 0.2, 0.0, 2.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tb): 250 results
M clear@x-tb@r1-1: worst 10.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 15 (1 with >= 3 ms); entry frame 10.0 + next 0.2 (from the entry: 10.0, 0.2, 0.2, 0.0); rows composed per frame max 7
M type@x-tb@r1-2: worst 16.6 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 35 (6 with >= 3 ms); entry frame 8.9 + next 0.2 (from the entry: 8.9, 0.2, 0.0, 2.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tb): 196 results
M clear@x-tb@r1-2: worst 25.3 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 17 (2 with >= 3 ms); entry frame 25.3 + next 0.8 (from the entry: 25.3, 0.8, 1.0, 1.4); rows composed per frame max 7
M type@x-tb@r1-3: worst 19.8 ms CPU (rows composed in it 6); frames >16 1, >25 0, >30 0 of 33 (8 with >= 3 ms); entry frame 9.7 + next 0.3 (from the entry: 9.7, 0.3, 0.0, 3.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tb): 44 results
M clear@x-tb@r1-3: worst 16.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 16.3 + next 0.9 (from the entry: 16.3, 0.9, 0.7, 1.2); rows composed per frame max 7
M sort-open@x-tb@r1: worst 8.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 37 (1 with >= 3 ms); entry frame 8.7 + next 1.2 (from the entry: 8.7, 1.2, 2.2, 0.7); rows composed per frame max 0
M sort-year@x-tb@r1: worst 10.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 10.9 + next 0.3 (from the entry: 10.9, 0.3, 0.0, 0.8); rows composed per frame max 7
M sort-open@x-tb@r1-2: worst 5.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 72 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tb@r1: worst 11.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (3 with >= 3 ms); entry frame 11.4 + next 0.2 (from the entry: 11.4, 0.2, 0.7, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 11 swipes (x-tb)
M loadmore@x-tb@r1: worst 3.8 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tb@r1: worst 41.5 ms CPU (rows composed in it 13); frames >16 5, >25 3, >30 2 of 91 (15 with >= 3 ms); entry frame 21.0 + next 23.0 (from the entry: 21.0, 23.0, 40.1, 15.6); rows composed per frame max 15
M drag@x-tb@r1: worst 5.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (4 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x-tc@r1-1: worst 17.0 ms CPU (rows composed in it 4); frames >16 1, >25 0, >30 0 of 41 (5 with >= 3 ms); entry frame 11.3 + next 0.2 (from the entry: 11.3, 0.2, 0.3, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tc): 250 results
M clear@x-tc@r1-1: worst 16.6 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 16.6 + next 0.2 (from the entry: 16.6, 0.2, 0.3, 0.3); rows composed per frame max 7
M type@x-tc@r1-2: worst 28.7 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 34 (6 with >= 3 ms); entry frame 10.9 + next 0.2 (from the entry: 10.9, 0.2, 0.3, 14.5); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tc): 196 results
M clear@x-tc@r1-2: worst 18.6 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 18.6 + next 1.9 (from the entry: 18.6, 1.9, 1.1, 0.7); rows composed per frame max 7
M type@x-tc@r1-3: worst 22.5 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 35 (7 with >= 3 ms); entry frame 22.5 + next 0.3 (from the entry: 22.5, 0.3, 0.3, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tc): 44 results
M clear@x-tc@r1-3: worst 11.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 11.0 + next 1.0 (from the entry: 11.0, 1.0, 0.8, 1.2); rows composed per frame max 7
M sort-open@x-tc@r1: worst 10.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (1 with >= 3 ms); entry frame 10.0 + next 1.0 (from the entry: 10.0, 1.0, 1.4, 0.3); rows composed per frame max 0
M sort-year@x-tc@r1: worst 7.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 7
M sort-open@x-tc@r1-2: worst 5.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 66 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tc@r1: worst 9.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 9.7 + next 0.2 (from the entry: 9.7, 0.2, 0.3, 0.6); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-tc)
M loadmore@x-tc@r1: worst 3.6 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tc@r1: worst 15.1 ms CPU (rows composed in it 8); frames >16 0, >25 0, >30 0 of 91 (10 with >= 3 ms); entry frame 15.1 + next 4.0 (from the entry: 15.1, 4.0, 3.4, 0.4); rows composed per frame max 8
M drag@x-tc@r1: worst 6.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 3
M type@x-sug@r1-1: worst 25.2 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 0 of 33 (6 with >= 3 ms); entry frame 8.7 + next 0.3 (from the entry: 8.7, 0.3, 0.3, 2.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-sug): 250 results
M clear@x-sug@r1-1: worst 9.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 9.4 + next 0.3 (from the entry: 9.4, 0.3, 0.3, 0.4); rows composed per frame max 7
M type@x-sug@r1-2: worst 18.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 27 (6 with >= 3 ms); entry frame 18.3 + next 0.2 (from the entry: 18.3, 0.2, 0.3, 5.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-sug): 196 results
M clear@x-sug@r1-2: worst 17.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 17.5 + next 0.2 (from the entry: 17.5, 0.2, 0.3, 0.3); rows composed per frame max 7
M type@x-sug@r1-3: worst 19.5 ms CPU (rows composed in it 4); frames >16 1, >25 0, >30 0 of 32 (6 with >= 3 ms); entry frame 14.1 + next 0.2 (from the entry: 14.1, 0.2, 0.4, 2.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-sug): 44 results
M clear@x-sug@r1-3: worst 15.6 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 15.6 + next 0.3 (from the entry: 15.6, 0.3, 0.3, 0.3); rows composed per frame max 7
M sort-open@x-sug@r1: worst 7.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 68 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x-sug@r1: worst 10.3 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 10.3 + next 0.2 (from the entry: 10.3, 0.2, 0.4, 0.7); rows composed per frame max 7
M sort-open@x-sug@r1-2: worst 8.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 82 (1 with >= 3 ms); entry frame 8.0 + next 1.0 (from the entry: 8.0, 1.0, 1.9, 0.3); rows composed per frame max 0
M sort-rel@x-sug@r1: worst 13.1 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 13.1 + next 0.2 (from the entry: 13.1, 0.2, 0.5, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-sug)
M loadmore@x-sug@r1: worst 2.8 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 19 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-sug@r1: worst 11.4 ms CPU (rows composed in it 9); frames >16 0, >25 0, >30 0 of 94 (12 with >= 3 ms); entry frame 11.4 + next 1.8 (from the entry: 11.4, 1.8, 4.4, 0.4); rows composed per frame max 9
M drag@x-sug@r1: worst 5.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 4
M type@x-ta+sug+one@r1-1: worst 22.9 ms CPU (rows composed in it 6); frames >16 2, >25 0, >30 0 of 38 (6 with >= 3 ms); entry frame 22.7 + next 0.2 (from the entry: 22.7, 0.2, 2.6, 0.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta+sug+one): 250 results
M clear@x-ta+sug+one@r1-1: worst 19.0 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 19.0 + next 0.2 (from the entry: 19.0, 0.2, 0.0, 0.2); rows composed per frame max 7
M type@x-ta+sug+one@r1-2: worst 23.7 ms CPU (rows composed in it 6); frames >16 1, >25 0, >30 0 of 33 (6 with >= 3 ms); entry frame 23.7 + next 0.2 (from the entry: 23.7, 0.2, 2.7, 0.5); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta+sug+one): 196 results
M clear@x-ta+sug+one@r1-2: worst 19.7 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 19.7 + next 0.2 (from the entry: 19.7, 0.2, 0.2, 0.0); rows composed per frame max 7
M type@x-ta+sug+one@r1-3: worst 25.2 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 0 of 35 (6 with >= 3 ms); entry frame 24.6 + next 0.2 (from the entry: 24.6, 0.2, 0.0, 3.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta+sug+one): 44 results
M clear@x-ta+sug+one@r1-3: worst 9.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 9.2 + next 0.2 (from the entry: 9.2, 0.2, 0.3, 0.0); rows composed per frame max 7
M sort-open@x-ta+sug+one@r1: worst 7.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 38 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x-ta+sug+one@r1: worst 9.6 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 9.6 + next 0.1 (from the entry: 9.6, 0.1, 0.0, 1.1); rows composed per frame max 7
M sort-open@x-ta+sug+one@r1-2: worst 5.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 64 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta+sug+one@r1: worst 8.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 8.2 + next 0.2 (from the entry: 8.2, 0.2, 0.7, 0.3); rows composed per frame max 7
  load-more button at [540 1948] after 10 swipes (x-ta+sug+one)
M loadmore@x-ta+sug+one@r1: worst 3.2 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta+sug+one@r1: worst 11.3 ms CPU (rows composed in it 4); frames >16 0, >25 0, >30 0 of 98 (10 with >= 3 ms); entry frame 11.3 + next 0.4 (from the entry: 11.3, 0.4, 0.3, 0.0); rows composed per frame max 10
M drag@x-ta+sug+one@r1: worst 20.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 25 (6 with >= 3 ms); entry frame 20.4 + next 1.5 (from the entry: 20.4, 1.5, 0.7, 6.7); rows composed per frame max 7
## T2. Round 2: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@x-ta+sug+one@r2-1: worst 29.8 ms CPU (rows composed in it 6); frames >16 3, >25 1, >30 0 of 39 (6 with >= 3 ms); entry frame 21.2 + next 0.2 (from the entry: 21.2, 0.2, 0.0, 2.6); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta+sug+one): 250 results
M clear@x-ta+sug+one@r2-1: worst 17.7 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 17.7 + next 0.2 (from the entry: 17.7, 0.2, 0.0, 0.2); rows composed per frame max 7
M type@x-ta+sug+one@r2-2: worst 12.5 ms CPU (rows composed in it 6); frames >16 0, >25 0, >30 0 of 35 (5 with >= 3 ms); entry frame 12.5 + next 0.2 (from the entry: 12.5, 0.2, 6.1, 0.6); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta+sug+one): 196 results
M clear@x-ta+sug+one@r2-2: worst 17.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 17.8 + next 0.2 (from the entry: 17.8, 0.2, 0.2, 0.0); rows composed per frame max 7
M type@x-ta+sug+one@r2-3: worst 19.3 ms CPU (rows composed in it 6); frames >16 1, >25 0, >30 0 of 37 (6 with >= 3 ms); entry frame 15.4 + next 0.2 (from the entry: 15.4, 0.2, 0.0, 2.5); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta+sug+one): 44 results
M clear@x-ta+sug+one@r2-3: worst 19.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 19.3 + next 0.2 (from the entry: 19.3, 0.2, 0.3, 0.0); rows composed per frame max 7
M sort-open@x-ta+sug+one@r2: worst 7.2 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 65 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x-ta+sug+one@r2: worst 13.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 13.7 + next 0.3 (from the entry: 13.7, 0.3, 0.0, 0.8); rows composed per frame max 7
M sort-open@x-ta+sug+one@r2-2: worst 5.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 71 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta+sug+one@r2: worst 10.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 10.7 + next 0.2 (from the entry: 10.7, 0.2, 0.0, 0.6); rows composed per frame max 7
  load-more button at [540 1896] after 11 swipes (x-ta+sug+one)
M loadmore@x-ta+sug+one@r2: worst 3.0 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta+sug+one@r2: worst 20.6 ms CPU (rows composed in it 13); frames >16 1, >25 0, >30 0 of 88 (15 with >= 3 ms); entry frame 12.8 + next 1.9 (from the entry: 12.8, 1.9, 8.8, 0.2); rows composed per frame max 13
M drag@x-ta+sug+one@r2: worst 5.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x-sug@r2-1: worst 18.1 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 33 (7 with >= 3 ms); entry frame 18.1 + next 0.1 (from the entry: 18.1, 0.1, 0.3, 2.7); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-sug): 250 results
M clear@x-sug@r2-1: worst 19.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (1 with >= 3 ms); entry frame 19.4 + next 0.2 (from the entry: 19.4, 0.2, 0.4, 0.3); rows composed per frame max 7
M type@x-sug@r2-2: worst 31.8 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 1 of 31 (6 with >= 3 ms); entry frame 8.5 + next 0.3 (from the entry: 8.5, 0.3, 0.0, 13.6); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-sug): 196 results
M clear@x-sug@r2-2: worst 25.2 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 16 (2 with >= 3 ms); entry frame 25.2 + next 0.3 (from the entry: 25.2, 0.3, 0.4, 0.4); rows composed per frame max 7
M type@x-sug@r2-3: worst 26.7 ms CPU (rows composed in it 6); frames >16 2, >25 1, >30 0 of 31 (6 with >= 3 ms); entry frame 15.8 + next 1.2 (from the entry: 15.8, 1.2, 0.3, 2.1); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-sug): 44 results
M clear@x-sug@r2-3: worst 18.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 18.5 + next 1.1 (from the entry: 18.5, 1.1, 0.3, 0.4); rows composed per frame max 7
M sort-open@x-sug@r2: worst 7.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 42 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x-sug@r2: worst 23.6 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 23.6 + next 0.3 (from the entry: 23.6, 0.3, 0.9, 0.4); rows composed per frame max 7
M sort-open@x-sug@r2-2: worst 6.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 56 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-sug@r2: worst 9.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 9.8 + next 0.2 (from the entry: 9.8, 0.2, 0.4, 0.7); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-sug)
M loadmore@x-sug@r2: worst 4.4 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-sug@r2: worst 24.0 ms CPU (rows composed in it 9); frames >16 1, >25 0, >30 0 of 91 (11 with >= 3 ms); entry frame 24.0 + next 3.5 (from the entry: 24.0, 3.5, 0.2, 0.4); rows composed per frame max 9
M drag@x-sug@r2: worst 4.4 ms CPU (rows composed in it 5); frames >16 0, >25 0, >30 0 of 26 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 5
M type@x-tc@r2-1: worst 17.2 ms CPU (rows composed in it 6); frames >16 3, >25 0, >30 0 of 42 (7 with >= 3 ms); entry frame 16.1 + next 0.2 (from the entry: 16.1, 0.2, 0.3, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tc): 250 results
M clear@x-tc@r2-1: worst 14.6 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 14.6 + next 0.2 (from the entry: 14.6, 0.2, 0.3, 0.3); rows composed per frame max 7
M type@x-tc@r2-2: worst 22.6 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 39 (4 with >= 3 ms); entry frame 22.6 + next 0.2 (from the entry: 22.6, 0.2, 0.3, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tc): 196 results
M clear@x-tc@r2-2: worst 22.1 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 22.1 + next 1.1 (from the entry: 22.1, 1.1, 0.8, 0.7); rows composed per frame max 7
M type@x-tc@r2-3: worst 22.2 ms CPU (rows composed in it 6); frames >16 1, >25 0, >30 0 of 33 (7 with >= 3 ms); entry frame 10.8 + next 0.3 (from the entry: 10.8, 0.3, 0.9, 2.9); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tc): 44 results
M clear@x-tc@r2-3: worst 23.1 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 23.1 + next 1.4 (from the entry: 23.1, 1.4, 1.0, 0.8); rows composed per frame max 7
M sort-open@x-tc@r2: worst 9.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (1 with >= 3 ms); entry frame 9.7 + next 1.3 (from the entry: 9.7, 1.3, 1.9, 0.5); rows composed per frame max 0
M sort-year@x-tc@r2: worst 18.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 18.5 + next 0.4 (from the entry: 18.5, 0.4, 0.3, 0.6); rows composed per frame max 7
M sort-open@x-tc@r2-2: worst 6.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 62 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tc@r2: worst 9.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 9.7 + next 0.2 (from the entry: 9.7, 0.2, 0.3, 0.6); rows composed per frame max 7
  load-more button at [540 1896] after 11 swipes (x-tc)
M loadmore@x-tc@r2: worst 2.8 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tc@r2: worst 30.7 ms CPU (rows composed in it 0); frames >16 4, >25 2, >30 1 of 86 (16 with >= 3 ms); entry frame 9.1 + next 3.3 (from the entry: 9.1, 3.3, 0.3, 3.6); rows composed per frame max 18
M drag@x-tc@r2: worst 1.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x-tb@r2-1: worst 15.7 ms CPU (rows composed in it 6); frames >16 0, >25 0, >30 0 of 39 (10 with >= 3 ms); entry frame 10.0 + next 0.2 (from the entry: 10.0, 0.2, 6.0, 0.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tb): 250 results
M clear@x-tb@r2-1: worst 16.9 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (1 with >= 3 ms); entry frame 16.9 + next 0.2 (from the entry: 16.9, 0.2, 0.3, 0.0); rows composed per frame max 7
M type@x-tb@r2-2: worst 19.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 37 (4 with >= 3 ms); entry frame 19.3 + next 0.2 (from the entry: 19.3, 0.2, 0.0, 2.9); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tb): 196 results
M clear@x-tb@r2-2: worst 10.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 10.2 + next 1.0 (from the entry: 10.2, 1.0, 1.0, 0.7); rows composed per frame max 7
M type@x-tb@r2-3: worst 20.6 ms CPU (rows composed in it 5); frames >16 1, >25 0, >30 0 of 35 (6 with >= 3 ms); entry frame 14.1 + next 0.2 (from the entry: 14.1, 0.2, 0.0, 2.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tb): 44 results
M clear@x-tb@r2-3: worst 19.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 19.8 + next 0.9 (from the entry: 19.8, 0.9, 1.0, 3.9); rows composed per frame max 7
M sort-open@x-tb@r2: worst 7.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 48 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x-tb@r2: worst 10.1 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 10.1 + next 0.2 (from the entry: 10.1, 0.2, 0.0, 0.8); rows composed per frame max 7
M sort-open@x-tb@r2-2: worst 5.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 58 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tb@r2: worst 13.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 13.8 + next 0.2 (from the entry: 13.8, 0.2, 0.0, 0.7); rows composed per frame max 7
  load-more button at [540 1896] after 12 swipes (x-tb)
M loadmore@x-tb@r2: worst 2.8 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tb@r2: worst 25.0 ms CPU (rows composed in it 14); frames >16 2, >25 0, >30 0 of 86 (13 with >= 3 ms); entry frame 25.0 + next 14.7 (from the entry: 25.0, 14.7, 18.2, 6.0); rows composed per frame max 15
M drag@x-tb@r2: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x-ta@r2-1: worst 19.5 ms CPU (rows composed in it 5); frames >16 2, >25 0, >30 0 of 39 (7 with >= 3 ms); entry frame 9.8 + next 0.2 (from the entry: 9.8, 0.2, 0.0, 4.1); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta): 250 results
M clear@x-ta@r2-1: worst 18.1 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 15 (2 with >= 3 ms); entry frame 18.1 + next 0.2 (from the entry: 18.1, 0.2, 0.2, 0.0); rows composed per frame max 7
M type@x-ta@r2-2: worst 22.6 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 33 (6 with >= 3 ms); entry frame 8.7 + next 0.2 (from the entry: 8.7, 0.2, 0.0, 22.6); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta): 196 results
M clear@x-ta@r2-2: worst 32.9 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 17 (2 with >= 3 ms); entry frame 32.9 + next 1.1 (from the entry: 32.9, 1.1, 0.7, 1.2); rows composed per frame max 7
M type@x-ta@r2-3: worst 25.5 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 0 of 35 (6 with >= 3 ms); entry frame 25.5 + next 1.5 (from the entry: 25.5, 1.5, 0.0, 4.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta): 44 results
M clear@x-ta@r2-3: worst 17.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 17.5 + next 1.9 (from the entry: 17.5, 1.9, 0.9, 1.3); rows composed per frame max 7
M sort-open@x-ta@r2: worst 7.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 42 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x-ta@r2: worst 29.5 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 18 (2 with >= 3 ms); entry frame 29.5 + next 0.2 (from the entry: 29.5, 0.2, 0.0, 1.0); rows composed per frame max 7
M sort-open@x-ta@r2-2: worst 6.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 54 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta@r2: worst 9.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 9.8 + next 0.2 (from the entry: 9.8, 0.2, 0.0, 0.8); rows composed per frame max 7
  load-more button at [540 2007] after 11 swipes (x-ta)
M loadmore@x-ta@r2: worst 2.9 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 17 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 1
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta@r2: worst 7.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 93 (11 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 7
M drag@x-ta@r2: worst 5.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 7
M type@x@r2-1: worst 23.5 ms CPU (rows composed in it 5); frames >16 4, >25 0, >30 0 of 33 (5 with >= 3 ms); entry frame 17.2 + next 0.2 (from the entry: 17.2, 0.2, 0.3, 2.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r2-1: worst 21.9 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (1 with >= 3 ms); entry frame 21.9 + next 0.2 (from the entry: 21.9, 0.2, 0.4, 0.4); rows composed per frame max 7
M type@x@r2-2: worst 35.4 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 1 of 28 (6 with >= 3 ms); entry frame 11.4 + next 0.2 (from the entry: 11.4, 0.2, 0.3, 3.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r2-2: worst 10.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 10.9 + next 1.5 (from the entry: 10.9, 1.5, 0.7, 2.2); rows composed per frame max 7
M type@x@r2-3: worst 14.7 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 29 (7 with >= 3 ms); entry frame 9.0 + next 0.3 (from the entry: 9.0, 0.3, 0.0, 12.5); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r2-3: worst 23.8 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 16 (3 with >= 3 ms); entry frame 23.8 + next 1.0 (from the entry: 23.8, 1.0, 1.1, 0.8); rows composed per frame max 7
M sort-open@x@r2: worst 7.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-year@x@r2: worst 7.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 7
M sort-open@x@r2-2: worst 5.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 66 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r2: worst 27.5 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 18 (2 with >= 3 ms); entry frame 27.5 + next 0.3 (from the entry: 27.5, 0.3, 0.3, 0.9); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x)
M loadmore@x@r2: worst 3.2 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r2: worst 13.6 ms CPU (rows composed in it 10); frames >16 0, >25 0, >30 0 of 95 (9 with >= 3 ms); entry frame 13.6 + next 2.2 (from the entry: 13.6, 2.2, 1.9, 0.4); rows composed per frame max 10
M drag@x@r2: worst 6.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (4 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 7
M type@base@r2-1: worst 23.6 ms CPU (rows composed in it 0); frames >16 3, >25 0, >30 0 of 31 (8 with >= 3 ms); entry frame 17.8 + next 3.6 (from the entry: 17.8, 3.6, 0.3, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r2-1: worst 10.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 15 (1 with >= 3 ms); entry frame 10.1 + next 0.2 (from the entry: 10.1, 0.2, 0.3, 0.3); rows composed per frame max 0
M type@base@r2-2: worst 26.8 ms CPU (rows composed in it 0); frames >16 2, >25 1, >30 0 of 30 (6 with >= 3 ms); entry frame 17.3 + next 0.3 (from the entry: 17.3, 0.3, 0.4, 3.8); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r2-2: worst 9.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (2 with >= 3 ms); entry frame 9.9 + next 1.1 (from the entry: 9.9, 1.1, 1.0, 1.2); rows composed per frame max 0
M type@base@r2-3: worst 20.2 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 30 (7 with >= 3 ms); entry frame 20.2 + next 0.2 (from the entry: 20.2, 0.2, 0.8, 2.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r2-3: worst 9.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 16 (2 with >= 3 ms); entry frame 9.8 + next 0.9 (from the entry: 9.8, 0.9, 1.0, 1.5); rows composed per frame max 0
M sort-open@base@r2: worst 12.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (1 with >= 3 ms); entry frame 12.9 + next 1.2 (from the entry: 12.9, 1.2, 1.6, 0.3); rows composed per frame max 0
M sort-year@base@r2: worst 14.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (3 with >= 3 ms); entry frame 14.7 + next 0.3 (from the entry: 14.7, 0.3, 0.2, 0.3); rows composed per frame max 0
M sort-open@base@r2-2: worst 6.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 60 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@base@r2: worst 11.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 11.0 + next 0.2 (from the entry: 11.0, 0.2, 0.7, 2.4); rows composed per frame max 0
  load-more button at [540 1896] after 11 swipes (base)
M loadmore@base@r2: worst 3.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r2: worst 30.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 1 of 86 (10 with >= 3 ms); entry frame 8.5 + next 0.6 (from the entry: 8.5, 0.6, 0.2, 0.4); rows composed per frame max 0
M drag@base@r2: worst 4.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
IME now: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
type | base | 6 | 22.6 / 22.8 / 26.8 | 18.6 | 0.3 | 1 | 0 | 0 | 78
type | x | 6 | 30.6 / 29.8 / 44.2 | 16.0 | 0.2 | 4 | 3 | 6 | 98
type | x-sug | 6 | 22.4 / 23.3 / 31.8 | 14.9 | 0.2 | 3 | 1 | 6 | 80
type | x-ta | 6 | 22.7 / 22.4 / 25.5 | 17.2 | 0.2 | 1 | 0 | 7 | 83
type | x-ta+sug+one | 6 | 23.3 / 22.2 / 29.8 | 21.9 | 0.2 | 2 | 0 | 6 | 83
type | x-tb | 6 | 19.6 / 19.3 / 23.8 | 12.1 | 0.2 | 0 | 0 | 6 | 77
type | x-tc | 6 | 22.4 / 21.7 / 28.7 | 13.7 | 0.2 | 1 | 0 | 6 | 83
clear | base | 6 | 10.0 / 11.1 / 17.0 | 10.0 | 0.9 | 0 | 0 | 0 | 21
clear | x | 6 | 21.7 / 21.7 / 31.3 | 21.7 | 0.8 | 1 | 1 | 7 | 32
clear | x-sug | 6 | 18.0 / 17.6 / 25.2 | 18.0 | 0.3 | 1 | 0 | 7 | 26
clear | x-ta | 6 | 19.1 / 20.5 / 32.9 | 19.1 | 1.0 | 1 | 1 | 7 | 28
clear | x-ta+sug+one | 6 | 18.4 / 17.1 / 19.7 | 18.4 | 0.2 | 0 | 0 | 7 | 24
clear | x-tb | 6 | 16.6 / 16.4 / 25.3 | 16.6 | 0.9 | 1 | 0 | 7 | 25
clear | x-tc | 6 | 17.6 / 17.7 / 23.1 | 17.6 | 1.1 | 0 | 0 | 7 | 28
sort-open | base | 4 | 6.7 / 8.0 / 12.9 | 0.0 | 0.0 | 0 | 0 | 0 | 16
sort-open | x | 4 | 6.6 / 7.0 / 9.0 | 0.0 | 0.0 | 0 | 0 | 0 | 18
sort-open | x-sug | 4 | 7.5 / 7.3 / 8.0 | 0.0 | 0.0 | 0 | 0 | 0 | 20
sort-open | x-ta | 4 | 7.2 / 8.0 / 12.6 | 0.0 | 0.0 | 0 | 0 | 0 | 18
sort-open | x-ta+sug+one | 4 | 6.4 / 6.3 / 7.5 | 0.0 | 0.0 | 0 | 0 | 0 | 14
sort-open | x-tb | 4 | 6.7 / 6.9 / 8.7 | 0.0 | 0.0 | 0 | 0 | 0 | 17
sort-open | x-tc | 4 | 8.1 / 8.0 / 10.0 | 4.8 | 0.5 | 0 | 0 | 0 | 19
sort-year | base | 2 | 27.0 / 27.0 / 39.4 | 27.0 | 0.2 | 1 | 1 | 0 | 39
sort-year | x | 2 | 11.7 / 11.7 / 15.6 | 5.1 | 7.8 | 0 | 0 | 7 | 26
sort-year | x-sug | 2 | 17.0 / 17.0 / 23.6 | 17.0 | 0.2 | 0 | 0 | 7 | 25
sort-year | x-ta | 2 | 19.6 / 19.6 / 29.5 | 19.6 | 0.2 | 1 | 0 | 7 | 28
sort-year | x-ta+sug+one | 2 | 11.6 / 11.6 / 13.7 | 11.6 | 0.2 | 0 | 0 | 7 | 22
sort-year | x-tb | 2 | 10.5 / 10.5 / 10.9 | 10.5 | 0.2 | 0 | 0 | 7 | 21
sort-year | x-tc | 2 | 13.1 / 13.1 / 18.5 | 9.2 | 0.2 | 0 | 0 | 7 | 22
sort-rel | base | 2 | 13.9 / 13.9 / 16.8 | 13.9 | 0.2 | 0 | 0 | 0 | 23
sort-rel | x | 2 | 19.0 / 19.0 / 27.5 | 19.0 | 0.2 | 1 | 0 | 7 | 32
sort-rel | x-sug | 2 | 11.4 / 11.4 / 13.1 | 11.4 | 0.2 | 0 | 0 | 7 | 20
sort-rel | x-ta | 2 | 13.3 / 13.3 / 16.8 | 13.3 | 0.2 | 0 | 0 | 7 | 22
sort-rel | x-ta+sug+one | 2 | 9.4 / 9.4 / 10.7 | 9.4 | 0.2 | 0 | 0 | 7 | 18
sort-rel | x-tb | 2 | 12.6 / 12.6 / 13.8 | 12.6 | 0.2 | 0 | 0 | 7 | 22
sort-rel | x-tc | 2 | 9.7 / 9.7 / 9.7 | 9.7 | 0.2 | 0 | 0 | 7 | 17
loadmore | base | 2 | 3.2 / 3.2 / 3.3 | 0.0 | 0.0 | 0 | 0 | 0 | 7
loadmore | x | 2 | 3.5 / 3.5 / 3.9 | 0.0 | 0.0 | 0 | 0 | 2 | 7
loadmore | x-sug | 2 | 3.6 / 3.6 / 4.4 | 0.0 | 0.0 | 0 | 0 | 2 | 7
loadmore | x-ta | 2 | 2.8 / 2.8 / 2.9 | 0.0 | 0.0 | 0 | 0 | 1 | 5
loadmore | x-ta+sug+one | 2 | 3.1 / 3.1 / 3.2 | 0.0 | 0.0 | 0 | 0 | 2 | 5
loadmore | x-tb | 2 | 3.3 / 3.3 / 3.8 | 0.0 | 0.0 | 0 | 0 | 2 | 6
loadmore | x-tc | 2 | 3.2 / 3.2 / 3.6 | 0.0 | 0.0 | 0 | 0 | 2 | 7
fling | base | 2 | 21.1 / 21.1 / 30.6 | 10.1 | 1.2 | 1 | 1 | 0 | 124
fling | x | 2 | 22.2 / 22.2 / 30.9 | 11.2 | 15.7 | 3 | 1 | 16 | 158
fling | x-sug | 2 | 17.7 / 17.7 / 24.0 | 17.7 | 2.6 | 0 | 0 | 9 | 115
fling | x-ta | 2 | 24.3 / 24.3 / 41.6 | 5.0 | 20.8 | 1 | 1 | 16 | 141
fling | x-ta+sug+one | 2 | 16.0 / 16.0 / 20.6 | 12.1 | 1.1 | 0 | 0 | 8 | 134
fling | x-tb | 2 | 33.2 / 33.2 / 41.5 | 23.0 | 18.9 | 3 | 2 | 14 | 210
fling | x-tc | 2 | 22.9 / 22.9 / 30.7 | 12.1 | 3.6 | 2 | 1 | 4 | 158
drag | base | 2 | 5.8 / 5.8 / 6.9 | 0.0 | 0.0 | 0 | 0 | 0 | 31
drag | x | 2 | 6.4 / 6.4 / 6.5 | 0.0 | 0.0 | 0 | 0 | 0 | 33
drag | x-sug | 2 | 5.2 / 5.2 / 5.9 | 0.0 | 0.0 | 0 | 0 | 2 | 27
drag | x-ta | 2 | 12.3 / 12.3 / 19.2 | 9.6 | 1.4 | 0 | 0 | 8 | 43
drag | x-ta+sug+one | 2 | 12.8 / 12.8 / 20.4 | 10.2 | 0.8 | 0 | 0 | 4 | 45
drag | x-tb | 2 | 6.1 / 6.1 / 6.9 | 0.0 | 0.0 | 0 | 0 | 0 | 40
drag | x-tc | 2 | 4.0 / 4.0 / 6.5 | 0.0 | 0.0 | 0 | 0 | 0 | 24
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 24
contact sheets: 0 in sheets/
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['6045', '8063', '9533', '10774', '11790', '12770', '13769', '14843', '15823', '16786', '17708', '18708', '19672', '20615']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 53
   skipped-frames events=53 max=125 total=2953
== Davey: 95
   davey events=95 max=2809ms median=1091ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

## ty36x

```
part ty36x; APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
base APK: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app.b, throwaway key, versionCode 2000200 (BASE) size 4682814 bytes
host: 4 cpu, 15700 MB RAM
android 16 sdk 36; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.40 5a14578 prodRelease (minified), x86_64 added, applicationId org.chyavorec.app, throwaway key, versionCode 2000200 (NEW + w28-exp.patch, DEBUG ONLY: variants behind launch flags) size 4687546 bytes
installed:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
IME: default com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME; enabled: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME com.google.android.tts/com.google.android.apps.speech.tts.googletts.settings.asr.voiceime.VoiceInputMethodService org.pc.helper/.Ime 
trace analysis: trace_processor_shell: /home/runner/.local/share/perfetto/prebuilts/trace_processor_shell-2434129a3af4dc9e
TRACE MODE perfetto; atrace categories: gfx view am dalvik input wm res binder_driver aidl sched; mapping app-mapping.txt; android 36
## S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile
S x:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S x: catalogue after 3841 ms
X compile org.chyavorec.app: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~ETZ9U7o1DwRxZgBN40aJ-Q==/org.chyavorec.app-fjrRnQR9s2h6iFHQtCo63A==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~kuySGAuVQxWW-FeeDZDSNw==/org.chyavorec.app.b-D5r2yB_YARrsAlY3OJSB-g==/base.apk| x86_64: [status=verify] [reason=install] [primary-abi]|
S base:  versionCode=2000200 minSdk=26 targetSdk=37  versionName=1.2.40 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
S base: catalogue after 3299 ms
X compile org.chyavorec.app.b: profile broadcast result=1; compile: Success; dexopt:  path: /data/app/~~kuySGAuVQxWW-FeeDZDSNw==/org.chyavorec.app.b-D5r2yB_YARrsAlY3OJSB-g==/base.apk| x86_64: [status=speed-profile] [reason=cmdline] [primary-abi]| path: /data/app/~~aoR_aeSnjLRX77gs3m_J7A==/org.pc.helper-dCQGAgzB2ixq0VTDROWNIg==/base.apk| x86_64: [status=run-from-apk] [reason=unknown] [primary-abi]|
tabs: catalog [320 2274] home [100 2274] news [760 2274] more [980 2274]
tab my [540 2274]
gestures: 6 flings up (540,1800 → 540,600 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device
IME now: org.pc.helper/.Ime
## T1. Round 1: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@base@r1-1: worst 19.4 ms CPU (rows composed in it 0); frames >16 1, >25 0, >30 0 of 53 (9 with >= 3 ms); entry frame 11.3 + next 0.3 (from the entry: 11.3, 0.3, 0.5, 0.5); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r1-1: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 22 (1 with >= 3 ms); entry frame 15.3 + next 0.5 (from the entry: 15.3, 0.5, 0.3, 0.6); rows composed per frame max 0
M type@base@r1-2: worst 25.6 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 43 (8 with >= 3 ms); entry frame 12.2 + next 0.3 (from the entry: 12.2, 0.3, 0.4, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r1-2: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (3 with >= 3 ms); entry frame 13.0 + next 12.8 (from the entry: 13.0, 12.8, 1.5, 1.3); rows composed per frame max 0
M type@base@r1-3: worst 25.6 ms CPU (rows composed in it 0); frames >16 1, >25 1, >30 0 of 48 (16 with >= 3 ms); entry frame 25.6 + next 0.7 (from the entry: 25.6, 0.7, 0.5, 0.5); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r1-3: worst 12.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (4 with >= 3 ms); entry frame 12.6 + next 12.5 (from the entry: 12.6, 12.5, 4.7, 1.5); rows composed per frame max 0
M sort-open@base@r1: worst 10.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 63 (1 with >= 3 ms); entry frame 10.3 + next 1.6 (from the entry: 10.3, 1.6, 1.5, 2.2); rows composed per frame max 0
M sort-year@base@r1: worst 14.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 14.9 + next 14.8 (from the entry: 14.9, 14.8, 0.5, 0.3); rows composed per frame max 0
M sort-open@base@r1-2: worst 14.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (1 with >= 3 ms); entry frame 14.6 + next 1.6 (from the entry: 14.6, 1.6, 1.5, 2.0); rows composed per frame max 0
M sort-rel@base@r1: worst 12.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (5 with >= 3 ms); entry frame 12.5 + next 12.3 (from the entry: 12.5, 12.3, 0.4, 0.3); rows composed per frame max 0
  load-more button at [540 1896] after 11 swipes (base)
M loadmore@base@r1: worst 4.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r1: worst 9.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 126 (15 with >= 3 ms); entry frame 9.7 + next 9.6 (from the entry: 9.7, 9.6, 0.5, 0.4); rows composed per frame max 0
M drag@base@r1: worst 7.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (4 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x@r1-1: worst 47.0 ms CPU (rows composed in it 7); frames >16 3, >25 2, >30 1 of 54 (10 with >= 3 ms); entry frame 47.0 + next 0.2 (from the entry: 47.0, 0.2, 0.2, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r1-1: worst 16.3 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 24 (1 with >= 3 ms); entry frame 16.3 + next 0.4 (from the entry: 16.3, 0.4, 0.3, 0.4); rows composed per frame max 7
M type@x@r1-2: worst 18.1 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 42 (8 with >= 3 ms); entry frame 18.1 + next 0.3 (from the entry: 18.1, 0.3, 0.4, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r1-2: worst 12.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 12.2 + next 12.0 (from the entry: 12.2, 12.0, 1.4, 1.3); rows composed per frame max 7
M type@x@r1-3: worst 15.7 ms CPU (rows composed in it 4); frames >16 0, >25 0, >30 0 of 47 (13 with >= 3 ms); entry frame 12.7 + next 0.2 (from the entry: 12.7, 0.2, 0.4, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r1-3: worst 22.6 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 22.6 + next 22.5 (from the entry: 22.6, 22.5, 0.4, 0.3); rows composed per frame max 7
M sort-open@x@r1: worst 8.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 71 (3 with >= 3 ms); entry frame 8.6 + next 1.6 (from the entry: 8.6, 1.6, 1.5, 5.2); rows composed per frame max 0
M sort-year@x@r1: worst 12.3 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 26 (3 with >= 3 ms); entry frame 12.3 + next 12.2 (from the entry: 12.3, 12.2, 0.5, 0.4); rows composed per frame max 7
M sort-open@x@r1-2: worst 6.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 64 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r1: worst 13.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 13.2 + next 13.1 (from the entry: 13.2, 13.1, 0.5, 0.4); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x)
M loadmore@x@r1: worst 10.4 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 24 (1 with >= 3 ms); entry frame 10.4 + next 0.7 (from the entry: 10.4, 0.7, 0.5, 0.4); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r1: worst 13.3 ms CPU (rows composed in it 9); frames >16 0, >25 0, >30 0 of 116 (9 with >= 3 ms); entry frame 13.3 + next 1.4 (from the entry: 13.3, 1.4, 3.9, 3.8); rows composed per frame max 9
M drag@x@r1: worst 3.2 ms CPU (rows composed in it 4); frames >16 0, >25 0, >30 0 of 42 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 4
M type@x-ta@r1-1: worst 19.7 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 57 (11 with >= 3 ms); entry frame 19.7 + next 0.2 (from the entry: 19.7, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta): 250 results
M clear@x-ta@r1-1: worst 11.6 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 11.6 + next 0.4 (from the entry: 11.6, 0.4, 0.3, 0.5); rows composed per frame max 7
M type@x-ta@r1-2: worst 19.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 51 (9 with >= 3 ms); entry frame 19.4 + next 0.2 (from the entry: 19.4, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta): 196 results
M clear@x-ta@r1-2: worst 12.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 12.5 + next 12.4 (from the entry: 12.5, 12.4, 1.4, 1.3); rows composed per frame max 7
M type@x-ta@r1-3: worst 18.1 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 51 (12 with >= 3 ms); entry frame 18.1 + next 0.2 (from the entry: 18.1, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta): 44 results
M clear@x-ta@r1-3: worst 12.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 12.8 + next 12.6 (from the entry: 12.8, 12.6, 0.8, 0.8); rows composed per frame max 7
M sort-open@x-ta@r1: worst 8.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 54 (1 with >= 3 ms); entry frame 8.8 + next 1.5 (from the entry: 8.8, 1.5, 2.3, 2.1); rows composed per frame max 0
M sort-year@x-ta@r1: worst 15.6 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 15.6 + next 15.5 (from the entry: 15.6, 15.5, 0.3, 0.2); rows composed per frame max 7
M sort-open@x-ta@r1-2: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 46 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta@r1: worst 26.3 ms CPU (rows composed in it 7); frames >16 2, >25 2, >30 0 of 22 (3 with >= 3 ms); entry frame 26.3 + next 26.2 (from the entry: 26.3, 26.2, 0.4, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 11 swipes (x-ta)
M loadmore@x-ta@r1: worst 3.7 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 18 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta@r1: worst 29.1 ms CPU (rows composed in it 17); frames >16 4, >25 2, >30 0 of 123 (26 with >= 3 ms); entry frame 13.9 + next 3.7 (from the entry: 13.9, 3.7, 3.6, 3.5); rows composed per frame max 17
M drag@x-ta@r1: worst 11.2 ms CPU (rows composed in it 8); frames >16 0, >25 0, >30 0 of 43 (5 with >= 3 ms); entry frame 11.2 + next 11.1 (from the entry: 11.2, 11.1, 1.6, 1.5); rows composed per frame max 8
M type@x-tb@r1-1: worst 28.6 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 57 (8 with >= 3 ms); entry frame 10.6 + next 0.2 (from the entry: 10.6, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tb): 250 results
M clear@x-tb@r1-1: worst 15.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 15.5 + next 0.4 (from the entry: 15.5, 0.4, 0.3, 0.5); rows composed per frame max 7
M type@x-tb@r1-2: worst 14.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 49 (10 with >= 3 ms); entry frame 11.6 + next 0.2 (from the entry: 11.6, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tb): 196 results
M clear@x-tb@r1-2: worst 13.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (5 with >= 3 ms); entry frame 13.0 + next 12.8 (from the entry: 13.0, 12.8, 1.6, 1.4); rows composed per frame max 7
M type@x-tb@r1-3: worst 20.1 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 48 (11 with >= 3 ms); entry frame 20.1 + next 0.2 (from the entry: 20.1, 0.2, 0.0, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tb): 44 results
M clear@x-tb@r1-3: worst 13.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 13.7 + next 13.6 (from the entry: 13.7, 13.6, 1.1, 1.0); rows composed per frame max 7
M sort-open@x-tb@r1: worst 8.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 27 (1 with >= 3 ms); entry frame 8.8 + next 1.9 (from the entry: 8.8, 1.9, 1.7, 2.2); rows composed per frame max 0
M sort-year@x-tb@r1: worst 19.8 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 19.8 + next 19.7 (from the entry: 19.8, 19.7, 0.4, 0.3); rows composed per frame max 7
M sort-open@x-tb@r1-2: worst 6.7 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 36 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tb@r1: worst 21.7 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 21.7 + next 21.6 (from the entry: 21.7, 21.6, 0.4, 0.2); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-tb)
M loadmore@x-tb@r1: worst 4.2 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tb@r1: worst 9.1 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 122 (17 with >= 3 ms); entry frame 9.1 + next 9.0 (from the entry: 9.1, 9.0, 0.8, 0.8); rows composed per frame max 1
M drag@x-tb@r1: worst 4.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 41 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x-tc@r1-1: worst 17.5 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 61 (7 with >= 3 ms); entry frame 14.2 + next 0.7 (from the entry: 14.2, 0.7, 0.8, 1.7); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tc): 250 results
M clear@x-tc@r1-1: worst 12.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (1 with >= 3 ms); entry frame 12.7 + next 0.5 (from the entry: 12.7, 0.5, 0.4, 0.5); rows composed per frame max 7
M type@x-tc@r1-2: worst 16.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 50 (12 with >= 3 ms); entry frame 11.8 + next 0.4 (from the entry: 11.8, 0.4, 0.5, 0.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tc): 196 results
M clear@x-tc@r1-2: worst 12.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 12.7 + next 12.5 (from the entry: 12.7, 12.5, 1.3, 1.2); rows composed per frame max 7
M type@x-tc@r1-3: worst 12.6 ms CPU (rows composed in it 6); frames >16 0, >25 0, >30 0 of 57 (14 with >= 3 ms); entry frame 9.7 + next 0.3 (from the entry: 9.7, 0.3, 0.4, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tc): 44 results
M clear@x-tc@r1-3: worst 12.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 27 (5 with >= 3 ms); entry frame 12.2 + next 12.1 (from the entry: 12.2, 12.1, 1.4, 1.2); rows composed per frame max 7
M sort-open@x-tc@r1: worst 9.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 32 (1 with >= 3 ms); entry frame 9.3 + next 1.5 (from the entry: 9.3, 1.5, 1.4, 2.8); rows composed per frame max 0
M sort-year@x-tc@r1: worst 13.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (4 with >= 3 ms); entry frame 13.5 + next 13.4 (from the entry: 13.5, 13.4, 3.6, 0.4); rows composed per frame max 7
M sort-open@x-tc@r1-2: worst 7.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 30 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tc@r1: worst 13.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 13.9 + next 13.8 (from the entry: 13.9, 13.8, 0.5, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-tc)
M loadmore@x-tc@r1: worst 3.9 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 25 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tc@r1: worst 8.5 ms CPU (rows composed in it 9); frames >16 0, >25 0, >30 0 of 123 (11 with >= 3 ms); entry frame 8.5 + next 1.3 (from the entry: 8.5, 1.3, 1.3, 3.2); rows composed per frame max 9
M drag@x-tc@r1: worst 9.0 ms CPU (rows composed in it 8); frames >16 0, >25 0, >30 0 of 44 (4 with >= 3 ms); entry frame 9.0 + next 9.0 (from the entry: 9.0, 9.0, 2.4, 2.3); rows composed per frame max 8
M type@x-sug@r1-1: worst 19.7 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 52 (12 with >= 3 ms); entry frame 19.7 + next 2.1 (from the entry: 19.7, 2.1, 0.6, 0.5); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-sug): 250 results
M clear@x-sug@r1-1: worst 9.1 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 23 (2 with >= 3 ms); entry frame 9.1 + next 0.4 (from the entry: 9.1, 0.4, 0.4, 0.5); rows composed per frame max 7
M type@x-sug@r1-2: worst 26.9 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 45 (9 with >= 3 ms); entry frame 26.9 + next 0.3 (from the entry: 26.9, 0.3, 0.5, 0.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-sug): 196 results
M clear@x-sug@r1-2: worst 11.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (2 with >= 3 ms); entry frame 11.5 + next 0.4 (from the entry: 11.5, 0.4, 0.3, 0.5); rows composed per frame max 7
M type@x-sug@r1-3: worst 20.4 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 51 (10 with >= 3 ms); entry frame 20.4 + next 0.5 (from the entry: 20.4, 0.5, 0.4, 2.8); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-sug): 44 results
M clear@x-sug@r1-3: worst 11.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 23 (2 with >= 3 ms); entry frame 11.5 + next 0.4 (from the entry: 11.5, 0.4, 0.3, 0.7); rows composed per frame max 7
M sort-open@x-sug@r1: worst 8.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 35 (1 with >= 3 ms); entry frame 8.5 + next 1.8 (from the entry: 8.5, 1.8, 1.7, 2.3); rows composed per frame max 0
M sort-year@x-sug@r1: worst 16.1 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 16.1 + next 16.0 (from the entry: 16.1, 16.0, 0.3, 0.2); rows composed per frame max 7
M sort-open@x-sug@r1-2: worst 6.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 55 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-sug@r1: worst 12.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 12.8 + next 12.7 (from the entry: 12.8, 12.7, 0.4, 0.3); rows composed per frame max 7
  load-more button at [540 1987] after 9 swipes (x-sug)
M loadmore@x-sug@r1: worst 2.9 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 22 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 1
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-sug@r1: worst 9.4 ms CPU (rows composed in it 8); frames >16 0, >25 0, >30 0 of 128 (12 with >= 3 ms); entry frame 9.4 + next 2.8 (from the entry: 9.4, 2.8, 3.5, 3.4); rows composed per frame max 8
M drag@x-sug@r1: worst 5.0 ms CPU (rows composed in it 5); frames >16 0, >25 0, >30 0 of 45 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 5
M type@x-ta+sug+one@r1-1: worst 22.6 ms CPU (rows composed in it 5); frames >16 2, >25 0, >30 0 of 63 (9 with >= 3 ms); entry frame 11.7 + next 0.2 (from the entry: 11.7, 0.2, 0.0, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta+sug+one): 250 results
M clear@x-ta+sug+one@r1-1: worst 14.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 14.2 + next 0.5 (from the entry: 14.2, 0.5, 0.4, 0.3); rows composed per frame max 7
M type@x-ta+sug+one@r1-2: worst 17.1 ms CPU (rows composed in it 6); frames >16 1, >25 0, >30 0 of 50 (5 with >= 3 ms); entry frame 12.0 + next 0.2 (from the entry: 12.0, 0.2, 0.2, 0.1); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta+sug+one): 196 results
M clear@x-ta+sug+one@r1-2: worst 12.1 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 12.1 + next 0.3 (from the entry: 12.1, 0.3, 0.2, 0.1); rows composed per frame max 7
M type@x-ta+sug+one@r1-3: worst 19.7 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 55 (9 with >= 3 ms); entry frame 19.7 + next 0.2 (from the entry: 19.7, 0.2, 0.0, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta+sug+one): 44 results
M clear@x-ta+sug+one@r1-3: worst 12.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (3 with >= 3 ms); entry frame 12.2 + next 3.5 (from the entry: 12.2, 3.5, 2.3, 0.0); rows composed per frame max 7
M sort-open@x-ta+sug+one@r1: worst 8.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 42 (1 with >= 3 ms); entry frame 8.5 + next 1.7 (from the entry: 8.5, 1.7, 1.6, 2.9); rows composed per frame max 0
M sort-year@x-ta+sug+one@r1: worst 16.6 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 16.6 + next 16.5 (from the entry: 16.6, 16.5, 0.6, 0.6); rows composed per frame max 7
M sort-open@x-ta+sug+one@r1-2: worst 6.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 53 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta+sug+one@r1: worst 12.6 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 12.6 + next 12.5 (from the entry: 12.6, 12.5, 0.3, 0.2); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-ta+sug+one)
M loadmore@x-ta+sug+one@r1: worst 4.2 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta+sug+one@r1: worst 8.6 ms CPU (rows composed in it 9); frames >16 0, >25 0, >30 0 of 123 (12 with >= 3 ms); entry frame 8.6 + next 1.3 (from the entry: 8.6, 1.3, 4.5, 1.7); rows composed per frame max 9
M drag@x-ta+sug+one@r1: worst 10.3 ms CPU (rows composed in it 6); frames >16 0, >25 0, >30 0 of 48 (3 with >= 3 ms); entry frame 10.3 + next 10.2 (from the entry: 10.3, 10.2, 1.4, 1.2); rows composed per frame max 6
## T2. Round 2: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag
M type@x-ta+sug+one@r2-1: worst 20.4 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 64 (12 with >= 3 ms); entry frame 20.4 + next 0.3 (from the entry: 20.4, 0.3, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta+sug+one): 250 results
M clear@x-ta+sug+one@r2-1: worst 10.7 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 10.7 + next 0.3 (from the entry: 10.7, 0.3, 0.2, 0.1); rows composed per frame max 7
M type@x-ta+sug+one@r2-2: worst 20.6 ms CPU (rows composed in it 6); frames >16 2, >25 0, >30 0 of 51 (4 with >= 3 ms); entry frame 19.2 + next 0.2 (from the entry: 19.2, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta+sug+one): 196 results
M clear@x-ta+sug+one@r2-2: worst 10.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 21 (3 with >= 3 ms); entry frame 10.9 + next 10.9 (from the entry: 10.9, 10.9, 0.4, 0.3); rows composed per frame max 7
M type@x-ta+sug+one@r2-3: worst 21.0 ms CPU (rows composed in it 7); frames >16 5, >25 0, >30 0 of 54 (8 with >= 3 ms); entry frame 21.0 + next 0.2 (from the entry: 21.0, 0.2, 0.0, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta+sug+one): 44 results
M clear@x-ta+sug+one@r2-3: worst 12.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 12.5 + next 0.4 (from the entry: 12.5, 0.4, 0.3, 0.4); rows composed per frame max 7
M sort-open@x-ta+sug+one@r2: worst 8.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 31 (1 with >= 3 ms); entry frame 8.6 + next 1.7 (from the entry: 8.6, 1.7, 1.6, 2.8); rows composed per frame max 0
M sort-year@x-ta+sug+one@r2: worst 13.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 13.4 + next 13.4 (from the entry: 13.4, 13.4, 0.3, 0.2); rows composed per frame max 7
M sort-open@x-ta+sug+one@r2-2: worst 5.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 72 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta+sug+one@r2: worst 12.1 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 12.1 + next 12.0 (from the entry: 12.1, 12.0, 0.3, 0.2); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-ta+sug+one)
M loadmore@x-ta+sug+one@r2: worst 3.7 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 15 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta+sug+one@r2: worst 10.6 ms CPU (rows composed in it 9); frames >16 0, >25 0, >30 0 of 124 (9 with >= 3 ms); entry frame 10.6 + next 2.2 (from the entry: 10.6, 2.2, 8.3, 8.1); rows composed per frame max 9
M drag@x-ta+sug+one@r2: worst 8.2 ms CPU (rows composed in it 4); frames >16 0, >25 0, >30 0 of 44 (3 with >= 3 ms); entry frame 8.2 + next 8.1 (from the entry: 8.2, 8.1, 0.6, 0.9); rows composed per frame max 4
M type@x-sug@r2-1: worst 20.8 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 54 (7 with >= 3 ms); entry frame 20.8 + next 0.2 (from the entry: 20.8, 0.2, 0.3, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-sug): 250 results
M clear@x-sug@r2-1: worst 11.3 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 22 (1 with >= 3 ms); entry frame 11.3 + next 0.4 (from the entry: 11.3, 0.4, 0.3, 0.6); rows composed per frame max 7
M type@x-sug@r2-2: worst 23.8 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 45 (8 with >= 3 ms); entry frame 23.8 + next 0.3 (from the entry: 23.8, 0.3, 0.4, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-sug): 196 results
M clear@x-sug@r2-2: worst 10.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 22 (2 with >= 3 ms); entry frame 10.9 + next 0.5 (from the entry: 10.9, 0.5, 0.3, 0.6); rows composed per frame max 7
M type@x-sug@r2-3: worst 11.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 48 (10 with >= 3 ms); entry frame 11.9 + next 0.2 (from the entry: 11.9, 0.2, 0.5, 0.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-sug): 44 results
M clear@x-sug@r2-3: worst 14.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (2 with >= 3 ms); entry frame 14.8 + next 0.4 (from the entry: 14.8, 0.4, 0.3, 0.5); rows composed per frame max 7
M sort-open@x-sug@r2: worst 11.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 19 (1 with >= 3 ms); entry frame 11.6 + next 1.7 (from the entry: 11.6, 1.7, 1.6, 2.5); rows composed per frame max 0
M sort-year@x-sug@r2: worst 13.2 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 22 (3 with >= 3 ms); entry frame 13.2 + next 13.1 (from the entry: 13.2, 13.1, 0.5, 0.4); rows composed per frame max 7
M sort-open@x-sug@r2-2: worst 6.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 48 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-sug@r2: worst 18.0 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 18.0 + next 17.8 (from the entry: 18.0, 17.8, 2.4, 2.3); rows composed per frame max 7
  load-more button at [540 1896] after 11 swipes (x-sug)
M loadmore@x-sug@r2: worst 4.6 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 25 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-sug@r2: worst 9.3 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 124 (16 with >= 3 ms); entry frame 8.4 + next 1.3 (from the entry: 8.4, 1.3, 9.3, 3.6); rows composed per frame max 9
M drag@x-sug@r2: worst 5.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 43 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 7
M type@x-tc@r2-1: worst 16.0 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 63 (11 with >= 3 ms); entry frame 16.0 + next 0.2 (from the entry: 16.0, 0.2, 0.4, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tc): 250 results
M clear@x-tc@r2-1: worst 11.1 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 22 (2 with >= 3 ms); entry frame 9.2 + next 1.2 (from the entry: 9.2, 1.2, 0.1, 11.1); rows composed per frame max 7
M type@x-tc@r2-2: worst 11.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 50 (10 with >= 3 ms); entry frame 11.2 + next 0.3 (from the entry: 11.2, 0.3, 0.4, 0.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tc): 196 results
M clear@x-tc@r2-2: worst 11.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 11.8 + next 11.7 (from the entry: 11.8, 11.7, 1.5, 1.3); rows composed per frame max 7
M type@x-tc@r2-3: worst 18.0 ms CPU (rows composed in it 5); frames >16 2, >25 0, >30 0 of 56 (15 with >= 3 ms); entry frame 13.2 + next 13.1 (from the entry: 13.2, 13.1, 0.4, 0.3); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tc): 44 results
M clear@x-tc@r2-3: worst 14.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 27 (3 with >= 3 ms); entry frame 14.0 + next 13.9 (from the entry: 14.0, 13.9, 1.1, 1.0); rows composed per frame max 7
M sort-open@x-tc@r2: worst 8.8 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 39 (1 with >= 3 ms); entry frame 8.8 + next 1.8 (from the entry: 8.8, 1.8, 1.7, 2.4); rows composed per frame max 0
M sort-year@x-tc@r2: worst 13.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 13.4 + next 13.3 (from the entry: 13.4, 13.3, 0.4, 0.3); rows composed per frame max 7
M sort-open@x-tc@r2-2: worst 7.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 52 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tc@r2: worst 24.5 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 24.5 + next 24.4 (from the entry: 24.5, 24.4, 0.4, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-tc)
M loadmore@x-tc@r2: worst 4.0 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 25 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tc@r2: worst 13.4 ms CPU (rows composed in it 9); frames >16 0, >25 0, >30 0 of 126 (14 with >= 3 ms); entry frame 13.4 + next 3.3 (from the entry: 13.4, 3.3, 3.0, 3.0); rows composed per frame max 9
M drag@x-tc@r2: worst 12.5 ms CPU (rows composed in it 8); frames >16 0, >25 0, >30 0 of 47 (2 with >= 3 ms); entry frame 12.5 + next 12.4 (from the entry: 12.5, 12.4, 2.5, 2.3); rows composed per frame max 8
M type@x-tb@r2-1: worst 17.2 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 58 (9 with >= 3 ms); entry frame 16.2 + next 0.2 (from the entry: 16.2, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-tb): 250 results
M clear@x-tb@r2-1: worst 10.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 18 (2 with >= 3 ms); entry frame 10.9 + next 0.6 (from the entry: 10.9, 0.6, 0.2, 0.5); rows composed per frame max 7
M type@x-tb@r2-2: worst 28.0 ms CPU (rows composed in it 7); frames >16 1, >25 1, >30 0 of 48 (3 with >= 3 ms); entry frame 12.3 + next 0.3 (from the entry: 12.3, 0.3, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-tb): 196 results
M clear@x-tb@r2-2: worst 12.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (3 with >= 3 ms); entry frame 12.4 + next 12.3 (from the entry: 12.4, 12.3, 1.1, 1.1); rows composed per frame max 7
M type@x-tb@r2-3: worst 21.6 ms CPU (rows composed in it 7); frames >16 1, >25 0, >30 0 of 50 (11 with >= 3 ms); entry frame 21.6 + next 0.3 (from the entry: 21.6, 0.3, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-tb): 44 results
M clear@x-tb@r2-3: worst 12.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (3 with >= 3 ms); entry frame 12.0 + next 11.9 (from the entry: 12.0, 11.9, 2.1, 2.0); rows composed per frame max 7
M sort-open@x-tb@r2: worst 9.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (3 with >= 3 ms); entry frame 9.3 + next 1.5 (from the entry: 9.3, 1.5, 1.4, 5.4); rows composed per frame max 0
M sort-year@x-tb@r2: worst 13.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 13.0 + next 12.8 (from the entry: 13.0, 12.8, 0.4, 0.2); rows composed per frame max 7
M sort-open@x-tb@r2-2: worst 6.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 67 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-tb@r2: worst 16.3 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 16.3 + next 16.2 (from the entry: 16.3, 16.2, 0.4, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 10 swipes (x-tb)
M loadmore@x-tb@r2: worst 3.9 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 17 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-tb@r2: worst 6.9 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 120 (19 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
M drag@x-tb@r2: worst 5.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 44 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x-ta@r2-1: worst 26.8 ms CPU (rows composed in it 7); frames >16 3, >25 2, >30 0 of 59 (14 with >= 3 ms); entry frame 23.3 + next 0.2 (from the entry: 23.3, 0.2, 0.0, 2.7); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x-ta): 250 results
M clear@x-ta@r2-1: worst 12.4 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 19 (2 with >= 3 ms); entry frame 12.4 + next 0.4 (from the entry: 12.4, 0.4, 0.3, 0.1); rows composed per frame max 7
M type@x-ta@r2-2: worst 20.3 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 49 (8 with >= 3 ms); entry frame 20.3 + next 0.2 (from the entry: 20.3, 0.2, 0.1, 0.0); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x-ta): 196 results
M clear@x-ta@r2-2: worst 12.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (5 with >= 3 ms); entry frame 12.8 + next 12.7 (from the entry: 12.8, 12.7, 1.3, 1.2); rows composed per frame max 7
M type@x-ta@r2-3: worst 13.1 ms CPU (rows composed in it 6); frames >16 0, >25 0, >30 0 of 54 (12 with >= 3 ms); entry frame 11.9 + next 0.3 (from the entry: 11.9, 0.3, 0.2, 0.2); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x-ta): 44 results
M clear@x-ta@r2-3: worst 12.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (4 with >= 3 ms); entry frame 12.0 + next 11.9 (from the entry: 12.0, 11.9, 1.5, 1.5); rows composed per frame max 7
M sort-open@x-ta@r2: worst 8.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 52 (1 with >= 3 ms); entry frame 8.9 + next 1.5 (from the entry: 8.9, 1.5, 1.4, 2.3); rows composed per frame max 0
M sort-year@x-ta@r2: worst 13.9 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 22 (5 with >= 3 ms); entry frame 13.9 + next 13.8 (from the entry: 13.9, 13.8, 0.5, 0.4); rows composed per frame max 7
M sort-open@x-ta@r2-2: worst 6.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 49 (2 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x-ta@r2: worst 11.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 22 (3 with >= 3 ms); entry frame 11.8 + next 11.7 (from the entry: 11.8, 11.7, 0.5, 0.3); rows composed per frame max 7
  load-more button at [540 1983] after 10 swipes (x-ta)
M loadmore@x-ta@r2: worst 3.0 ms CPU (rows composed in it 1); frames >16 0, >25 0, >30 0 of 17 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 1
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x-ta@r2: worst 16.0 ms CPU (rows composed in it 14); frames >16 1, >25 0, >30 0 of 141 (19 with >= 3 ms); entry frame 13.2 + next 3.4 (from the entry: 13.2, 3.4, 3.6, 3.6); rows composed per frame max 14
M drag@x-ta@r2: worst 3.4 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 44 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@x@r2-1: worst 27.2 ms CPU (rows composed in it 7); frames >16 2, >25 1, >30 0 of 51 (13 with >= 3 ms); entry frame 27.2 + next 0.3 (from the entry: 27.2, 0.3, 0.3, 0.6); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'приказ' (x): 250 results
M clear@x@r2-1: worst 14.0 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 24 (2 with >= 3 ms); entry frame 14.0 + next 0.5 (from the entry: 14.0, 0.5, 0.4, 0.4); rows composed per frame max 7
M type@x@r2-2: worst 20.2 ms CPU (rows composed in it 7); frames >16 3, >25 0, >30 0 of 41 (10 with >= 3 ms); entry frame 20.2 + next 0.3 (from the entry: 20.2, 0.3, 0.4, 0.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'любов' (x): 196 results
M clear@x@r2-2: worst 27.4 ms CPU (rows composed in it 7); frames >16 2, >25 2, >30 0 of 24 (3 with >= 3 ms); entry frame 27.4 + next 27.2 (from the entry: 27.4, 27.2, 1.3, 1.2); rows composed per frame max 7
M type@x@r2-3: worst 23.2 ms CPU (rows composed in it 4); frames >16 2, >25 0, >30 0 of 45 (8 with >= 3 ms); entry frame 9.8 + next 0.3 (from the entry: 9.8, 0.3, 0.5, 0.4); rows composed per frame max 7 [no input slice: from the trace start]
  after type 'мария' (x): 44 results
M clear@x@r2-3: worst 14.5 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (6 with >= 3 ms); entry frame 14.5 + next 14.4 (from the entry: 14.5, 14.4, 1.3, 1.2); rows composed per frame max 7
M sort-open@x@r2: worst 8.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 53 (1 with >= 3 ms); entry frame 8.3 + next 1.7 (from the entry: 8.3, 1.7, 1.6, 2.2); rows composed per frame max 0
M sort-year@x@r2: worst 19.7 ms CPU (rows composed in it 7); frames >16 2, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 19.7 + next 19.6 (from the entry: 19.7, 19.6, 0.4, 0.3); rows composed per frame max 7
M sort-open@x@r2-2: worst 7.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 58 (3 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M sort-rel@x@r2: worst 13.8 ms CPU (rows composed in it 7); frames >16 0, >25 0, >30 0 of 25 (5 with >= 3 ms); entry frame 13.8 + next 13.5 (from the entry: 13.8, 13.5, 0.4, 0.3); rows composed per frame max 7
  load-more button at [540 1896] after 11 swipes (x)
M loadmore@x@r2: worst 4.5 ms CPU (rows composed in it 2); frames >16 0, >25 0, >30 0 of 23 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 2
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@x@r2: worst 16.3 ms CPU (rows composed in it 9); frames >16 2, >25 0, >30 0 of 125 (17 with >= 3 ms); entry frame 16.3 + next 3.5 (from the entry: 16.3, 3.5, 3.5, 3.3); rows composed per frame max 11
M drag@x@r2: worst 2.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 46 (0 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
M type@base@r2-1: worst 22.8 ms CPU (rows composed in it 0); frames >16 2, >25 0, >30 0 of 47 (15 with >= 3 ms); entry frame 14.0 + next 0.2 (from the entry: 14.0, 0.2, 0.4, 0.3); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'приказ' (base): 250 results
M clear@base@r2-1: worst 11.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 24 (1 with >= 3 ms); entry frame 11.9 + next 0.4 (from the entry: 11.9, 0.4, 0.3, 0.6); rows composed per frame max 0
M type@base@r2-2: worst 29.8 ms CPU (rows composed in it 0); frames >16 2, >25 2, >30 0 of 43 (13 with >= 3 ms); entry frame 12.2 + next 0.3 (from the entry: 12.2, 0.3, 3.4, 0.4); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'любов' (base): 196 results
M clear@base@r2-2: worst 13.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 13.0 + next 12.9 (from the entry: 13.0, 12.9, 1.2, 1.2); rows composed per frame max 0
M type@base@r2-3: worst 15.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 50 (13 with >= 3 ms); entry frame 15.3 + next 0.4 (from the entry: 15.3, 0.4, 0.4, 0.5); rows composed per frame max 0 [no input slice: from the trace start]
  after type 'мария' (base): 44 results
M clear@base@r2-3: worst 12.3 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (5 with >= 3 ms); entry frame 12.3 + next 12.2 (from the entry: 12.3, 12.2, 1.2, 1.1); rows composed per frame max 0
M sort-open@base@r2: worst 11.6 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 37 (1 with >= 3 ms); entry frame 11.6 + next 2.0 (from the entry: 11.6, 2.0, 1.8, 2.2); rows composed per frame max 0
M sort-year@base@r2: worst 14.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 23 (3 with >= 3 ms); entry frame 14.5 + next 14.3 (from the entry: 14.5, 14.3, 0.5, 0.4); rows composed per frame max 0
M sort-open@base@r2-2: worst 9.0 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 48 (3 with >= 3 ms); entry frame 9.0 + next 1.5 (from the entry: 9.0, 1.5, 1.4, 3.5); rows composed per frame max 0
M sort-rel@base@r2: worst 13.9 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 26 (3 with >= 3 ms); entry frame 8.8 + next 1.7 (from the entry: 8.8, 1.7, 1.6, 13.9); rows composed per frame max 0
  load-more button at [540 1896] after 9 swipes (base)
M loadmore@base@r2: worst 4.1 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 25 (1 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
  after load more: text="Търси навсякъде" content-desc="Сканирай баркод" text="Всичко" text="Заглавие" text="Автор" text="ISBN" 
M fling@base@r2: worst 10.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 115 (16 with >= 3 ms); entry frame 10.5 + next 1.5 (from the entry: 10.5, 1.5, 4.2, 4.1); rows composed per frame max 0
M drag@base@r2: worst 5.5 ms CPU (rows composed in it 0); frames >16 0, >25 0, >30 0 of 45 (6 with >= 3 ms); entry frame 0.0 + next 0.0 (from the entry: ); rows composed per frame max 0
IME now: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME
## Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant
kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median
---|---|---|---|---|---|---|---|---|---
type | base | 6 | 24.2 / 23.1 / 29.8 | 13.1 | 0.3 | 5 | 0 | 0 | 130
type | x | 6 | 21.7 / 25.2 / 47.0 | 19.1 | 0.3 | 3 | 1 | 7 | 118
type | x-sug | 6 | 20.6 / 20.6 / 26.9 | 20.6 | 0.3 | 1 | 0 | 7 | 115
type | x-ta | 6 | 19.5 / 19.6 / 26.8 | 19.5 | 0.2 | 2 | 0 | 7 | 127
type | x-ta+sug+one | 6 | 20.5 / 20.2 / 22.6 | 19.4 | 0.2 | 0 | 0 | 6 | 125
type | x-tb | 6 | 20.9 / 21.7 / 28.6 | 14.2 | 0.2 | 2 | 0 | 7 | 103
type | x-tc | 6 | 16.0 / 15.3 / 18.0 | 12.5 | 0.3 | 0 | 0 | 7 | 119
clear | base | 6 | 12.8 / 13.0 / 15.3 | 12.8 | 12.3 | 0 | 0 | 0 | 47
clear | x | 6 | 15.4 / 17.8 / 27.4 | 15.4 | 13.2 | 2 | 0 | 7 | 53
clear | x-sug | 6 | 11.4 / 11.5 / 14.8 | 11.4 | 0.4 | 0 | 0 | 7 | 24
clear | x-ta | 6 | 12.4 / 12.3 / 12.8 | 12.4 | 12.2 | 0 | 0 | 7 | 44
clear | x-ta+sug+one | 6 | 12.1 / 12.1 / 14.2 | 12.1 | 0.5 | 0 | 0 | 7 | 23
clear | x-tb | 6 | 12.7 / 12.9 / 15.5 | 12.7 | 12.1 | 0 | 0 | 7 | 43
clear | x-tc | 6 | 12.4 / 12.4 / 14.0 | 12.4 | 11.9 | 0 | 0 | 7 | 43
sort-open | base | 4 | 10.9 / 11.4 / 14.6 | 10.9 | 1.6 | 0 | 0 | 0 | 26
sort-open | x | 4 | 7.7 / 7.6 / 8.6 | 4.2 | 0.8 | 0 | 0 | 0 | 26
sort-open | x-sug | 4 | 7.7 / 8.4 / 11.6 | 4.2 | 0.8 | 0 | 0 | 0 | 22
sort-open | x-ta | 4 | 7.9 / 7.7 / 8.9 | 4.4 | 0.8 | 0 | 0 | 0 | 26
sort-open | x-ta+sug+one | 4 | 7.5 / 7.4 / 8.6 | 4.2 | 0.8 | 0 | 0 | 0 | 21
sort-open | x-tb | 4 | 7.8 / 7.9 / 9.3 | 4.4 | 0.8 | 0 | 0 | 0 | 22
sort-open | x-tc | 4 | 8.1 / 8.2 / 9.3 | 4.4 | 0.8 | 0 | 0 | 0 | 23
sort-year | base | 2 | 14.7 / 14.7 / 14.9 | 14.7 | 14.6 | 0 | 0 | 0 | 45
sort-year | x | 2 | 16.0 / 16.0 / 19.7 | 16.0 | 15.9 | 0 | 0 | 7 | 50
sort-year | x-sug | 2 | 14.7 / 14.7 / 16.1 | 14.7 | 14.6 | 0 | 0 | 7 | 43
sort-year | x-ta | 2 | 14.8 / 14.8 / 15.6 | 14.8 | 14.7 | 0 | 0 | 7 | 45
sort-year | x-ta+sug+one | 2 | 15.0 / 15.0 / 16.6 | 15.0 | 14.9 | 0 | 0 | 7 | 44
sort-year | x-tb | 2 | 16.4 / 16.4 / 19.8 | 16.4 | 16.2 | 0 | 0 | 7 | 45
sort-year | x-tc | 2 | 13.4 / 13.4 / 13.5 | 13.4 | 13.4 | 0 | 0 | 7 | 42
sort-rel | base | 2 | 13.2 / 13.2 / 13.9 | 10.7 | 7.0 | 0 | 0 | 0 | 47
sort-rel | x | 2 | 13.5 / 13.5 / 13.8 | 13.5 | 13.3 | 0 | 0 | 7 | 46
sort-rel | x-sug | 2 | 15.4 / 15.4 / 18.0 | 15.4 | 15.2 | 0 | 0 | 7 | 48
sort-rel | x-ta | 2 | 19.1 / 19.1 / 26.3 | 19.1 | 18.9 | 2 | 0 | 7 | 52
sort-rel | x-ta+sug+one | 2 | 12.3 / 12.3 / 12.6 | 12.3 | 12.2 | 0 | 0 | 7 | 36
sort-rel | x-tb | 2 | 19.0 / 19.0 / 21.7 | 19.0 | 18.9 | 0 | 0 | 7 | 52
sort-rel | x-tc | 2 | 19.2 / 19.2 / 24.5 | 19.2 | 19.1 | 0 | 0 | 7 | 57
loadmore | base | 2 | 4.1 / 4.1 / 4.1 | 0.0 | 0.0 | 0 | 0 | 0 | 11
loadmore | x | 2 | 7.5 / 7.5 / 10.4 | 5.2 | 0.3 | 0 | 0 | 2 | 15
loadmore | x-sug | 2 | 3.8 / 3.8 / 4.6 | 0.0 | 0.0 | 0 | 0 | 2 | 10
loadmore | x-ta | 2 | 3.4 / 3.4 / 3.7 | 0.0 | 0.0 | 0 | 0 | 2 | 6
loadmore | x-ta+sug+one | 2 | 4.0 / 4.0 / 4.2 | 0.0 | 0.0 | 0 | 0 | 2 | 6
loadmore | x-tb | 2 | 4.0 / 4.0 / 4.2 | 0.0 | 0.0 | 0 | 0 | 2 | 7
loadmore | x-tc | 2 | 4.0 / 4.0 / 4.0 | 0.0 | 0.0 | 0 | 0 | 2 | 11
fling | base | 2 | 10.1 / 10.1 / 10.5 | 10.1 | 5.5 | 0 | 0 | 0 | 150
fling | x | 2 | 14.8 / 14.8 / 16.3 | 14.8 | 2.5 | 0 | 0 | 9 | 158
fling | x-sug | 2 | 9.4 / 9.4 / 9.4 | 8.9 | 2.0 | 0 | 0 | 4 | 144
fling | x-ta | 2 | 22.6 / 22.6 / 29.1 | 13.6 | 3.5 | 2 | 0 | 16 | 255
fling | x-ta+sug+one | 2 | 9.6 / 9.6 / 10.6 | 9.6 | 1.8 | 0 | 0 | 9 | 134
fling | x-tb | 2 | 8.0 / 8.0 / 9.1 | 4.5 | 4.5 | 0 | 0 | 1 | 144
fling | x-tc | 2 | 10.9 / 10.9 / 13.4 | 10.9 | 2.3 | 0 | 0 | 9 | 145
drag | base | 2 | 6.2 / 6.2 / 7.0 | 0.0 | 0.0 | 0 | 0 | 0 | 69
drag | x | 2 | 3.0 / 3.0 / 3.2 | 0.0 | 0.0 | 0 | 0 | 2 | 52
drag | x-sug | 2 | 5.2 / 5.2 / 5.5 | 0.0 | 0.0 | 0 | 0 | 6 | 65
drag | x-ta | 2 | 7.3 / 7.3 / 11.2 | 5.6 | 5.5 | 0 | 0 | 4 | 65
drag | x-ta+sug+one | 2 | 9.2 / 9.2 / 10.3 | 9.2 | 9.1 | 0 | 0 | 5 | 68
drag | x-tb | 2 | 5.2 / 5.2 / 5.4 | 0.0 | 0.0 | 0 | 0 | 0 | 55
drag | x-tc | 2 | 10.8 / 10.8 / 12.5 | 10.8 | 10.7 | 0 | 0 | 8 | 75
CRASH new: FATAL 0, base: FATAL 0; ANR 0; traces kept: 12
contact sheets: 0 in sheets/
contact sheets: 0 in sheets/
## COMPLETE
## logcat findings
app pids seen: ['4266', '5971', '7286', '8367', '9305', '10232', '11200', '12085', '12999', '13915', '14877', '15840', '16743', '17660']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 43
   skipped-frames events=43 max=141 total=2646
== Davey: 125
   davey events=125 max=2489ms median=870ms
== NativeLink: 14
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
CRASHES: 0 block(s)  → crashes.txt
```

