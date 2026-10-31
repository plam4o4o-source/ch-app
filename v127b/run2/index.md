# v1.2.27 full emulator test, round 2: media (zoom, album thumbnails, covers, PDF fallback)

Source: `debug/full-test-3` @ aa1c5a9 (app = 8dc63c1, compared with v1.2.26 = 3df7f37), run 40926099946 (attempt 1), 2026-10-29T18:15:46Z.
Emulators: google_apis x86_64 API 33, pixel_6 profile, -gpu swangle_indirect; app locale bg-BG (EN only in look1). APKs: minified prodRelease, x86_64 added, throwaway key, versionCode 1484479.
Round 1 (all parts): [../index.md](../index.md).

| part | status |
|---|---|
| media2 | COMPLETE; part=media2 api=33 gpu=swangle_indirect emulator-step=success |

## media2

```
part media2; APK: 1.2.27 8dc63c1 (Data safety: кориците от Open Library — „App activity → App interactio); versionCode 1484479; x86_64 libs: 4 size 4875530 bytes
base APK: 1.2.26 3df7f37 (deps: bump the libraries group across 1 directory with 4 updates (#13)); versionCode 1484479; x86_64 libs: 4; update-source APK: 1.2.26 3df7f37; versionCode 1484478; update manifest https://raw.githubusercontent.com/plam4o4o-source/ch-app/emulator-results/v127b/update/update.json
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
installed:  versionCode=1484479 minSdk=26 targetSdk=37  versionName=1.2.27 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission:  android.permission.CAMERA: granted=false, flags=[ USER_FIXED]
screen 1080x2400
## P. Host-side probe of the site photos (status, time, size of the optimizer variants)
PAGE /fotodokumentalna-izlozhba: 200 0.35s 112916 bytes
img tags: 35
PROBE /fotodokumentalna-izlozhba: 30 images, optimizer non-200: 0
#01 '' original 200 0.16s 126KB text/html; charset=utf-8 | w384: not optimizable | w1600: not optimizable
#02 'НЧ Васил Левски – 1922' original 200 0.29s 11KB image/webp | w384 200 0.42s 6KB image/jpeg | w1600 200 0.35s 6KB image/jpeg
#03 'НЧ Васил Левски – 1922' original 200 0.40s 11KB image/webp | w384 200 0.19s 6KB image/jpeg | w1600 200 0.09s 6KB image/jpeg
#04 'Сградата на читалището в Яворец' original 200 0.46s 114KB image/webp | w384 200 0.30s 15KB image/jpeg | w1600 200 0.49s 114KB image/webp
#05 'Фолклорен състав при читалището' original 200 0.18s 65KB image/webp | w384 200 0.40s 15KB image/jpeg | w1600 200 0.63s 65KB image/webp
#06 'Самодейци' original 200 0.33s 75KB image/webp | w384 200 0.25s 15KB image/jpeg | w1600 200 0.38s 75KB image/webp
#07 'Сградата през 70-те години' original 200 0.33s 139KB image/webp | w384 200 0.21s 14KB image/jpeg | w1600 200 0.47s 139KB image/webp
#08 'Сценичен ансамбъл Яворски хлапета' original 200 0.22s 50KB image/webp | w384 200 0.35s 16KB image/jpeg | w1600 200 0.40s 50KB image/webp
#09 'Фолклорна група Явор' original 200 0.41s 151KB image/webp | w384 200 0.25s 26KB image/jpeg | w1600 200 0.49s 145KB image/jpeg
#10 'Клуб за народни танци' original 200 0.30s 120KB image/webp | w384 200 0.31s 16KB image/jpeg | w1600 200 0.40s 92KB image/jpeg
#11 'Стопанският двор на ТКЗС в Яворец' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#12 'Селскостопанска техника' original 404 0.17s 0KB  | w384: not optimizable | w1600: not optimizable
#13 'Трудов ден' original 404 0.09s 0KB  | w384: not optimizable | w1600: not optimizable
#14 'Животновъдна ферма' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#15 'Овчари и стада' original 404 0.09s 0KB  | w384: not optimizable | w1600: not optimizable
#16 'Жътва' original 404 0.10s 0KB  | w384: not optimizable | w1600: not optimizable
#17 'Харман' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#18 'Бригада' original 404 0.10s 0KB  | w384: not optimizable | w1600: not optimizable
#19 'Складове' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#20 'Възпоменателна табела на основоположници' original 200 0.35s 137KB image/webp | w384 200 0.24s 22KB image/jpeg | w1600 200 0.55s 137KB image/webp
#21 'Панорама на село Яворец' original 404 0.09s 0KB  | w384: not optimizable | w1600: not optimizable
#22 'Улиците на Яворец' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#23 'Жителите на Яворец' original 404 0.10s 0KB  | w384: not optimizable | w1600: not optimizable
#24 'Селски празник' original 404 0.10s 0KB  | w384: not optimizable | w1600: not optimizable
#25 'Църквата в Яворец' original 404 0.07s 0KB  | w384: not optimizable | w1600: not optimizable
#26 'Площадът' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#27 'Чешмата' original 404 0.09s 0KB  | w384: not optimizable | w1600: not optimizable
#28 'Природата около Яворец' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#29 'Стари занаяти' original 404 0.08s 0KB  | w384: not optimizable | w1600: not optimizable
#30 'Училището в Яворец' original 404 0.09s 0KB  | w384: not optimizable | w1600: not optimizable
PAGE /folklorna-grupa: 200 0.11s 101762 bytes
img tags: 11
PROBE /folklorna-grupa: 11 images, optimizer non-200: 0
#01 '' original 200 0.11s 126KB text/html; charset=utf-8 | w384: not optimizable | w1600: not optimizable
#02 'НЧ Васил Левски – 1922' original 200 0.09s 11KB image/webp | w384 200 0.08s 6KB image/jpeg | w1600 200 0.08s 6KB image/jpeg
#03 'НЧ Васил Левски – 1922' original 200 0.08s 11KB image/webp | w384 200 0.08s 6KB image/jpeg | w1600 200 0.10s 6KB image/jpeg
#04 'Фолклорна група Явор — групова снимка' original 200 0.10s 151KB image/webp | w384 200 0.08s 26KB image/jpeg | w1600 200 0.12s 145KB image/jpeg
#05 'Станчо Кържин — ръководител' original 200 0.34s 128KB image/webp | w384 200 0.21s 22KB image/jpeg | w1600 200 0.51s 128KB image/webp
#06 'Концертно изпълнение' original 200 0.25s 178KB image/webp | w384 200 0.21s 21KB image/jpeg | w1600 200 0.51s 178KB image/webp
#07 'На сцената' original 200 0.32s 293KB image/jpeg | w384 200 0.21s 23KB image/jpeg | w1600 200 0.60s 271KB image/jpeg
#08 'Фестивал' original 200 0.35s 341KB image/jpeg | w384 200 0.21s 30KB image/jpeg | w1600 200 0.59s 305KB image/jpeg
#09 '' original 200 0.09s 126KB text/html; charset=utf-8 | w384: not optimizable | w1600: not optimizable
#10 'НЧ Васил Левски – 1922' original 200 0.08s 11KB image/webp | w384 200 0.08s 6KB image/jpeg | w1600 200 0.12s 6KB image/jpeg
#11 '' original 200 0.20s 15KB image/webp | w384 200 0.32s 5KB image/jpeg | w1600 200 0.19s 5KB image/jpeg
cover URLs from katalog.json (cv): 302/0B 200/6776B 200/43B 200/43B 200/43B 
Open Library by ISBN (default=false) for catalogue ISBNs: 404 404 404 404 404 
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2475 ms
## G1. Album 1: thumbnails over time (2, 5, 10, 20, 40 s) and after scrolling
## G2. Viewer: load time, double tap 2×, pinch beyond 2×, photo 5
VIEWER: text="1 / 29  ·  Сградата на читалището в Яворец"
touch device: /dev/input/event2 x-max=32767 y-max=32767 pressure=11
after double tap: text="1 / 29  ·  Сградата на читалището в Яворец"
swipe while zoomed: text="1 / 29  ·  Сградата на читалището в Яворец" (page 1 = the swipe panned the zoomed photo)
MEM zoomed (raw)     PSS=82001KB  Java=11440KB  Native=38856KB  Graphics=0KB  Views=10 Activities=1
MEM zoomed (trimmed) PSS=75760KB  Java=9044KB  Native=30004KB  Graphics=0KB  Views=10 Activities=1
after reset double tap: text="1 / 29  ·  Сградата на читалището в Яворец"
swipe after reset: text="2 / 29  ·  Фолклорен състав при читалището"
VIEWER now: text="5 / 29  ·  Сценичен ансамбъл Яворски хлапета"
## C. Covers that have an address in the catalogue (cv) and a book with an ISBN
book 2184: content-desc="Белият дома" text="Белият дома" text="Николай Симеонов" 
book 2091: content-desc="Белият зимата" text="Белият зимата" text="Александър Николов" 
book 259: content-desc="Белият морето" text="Белият морето" text="Радост Георгиева" 
book 958: content-desc="Времето на любовта" text="Времето на любовта" text="Ивана Йорданова" 
book 1894: content-desc="Времето на любовта" text="Времето на любовта" text="Ивана Йорданова" 
search Gatsby:  results
## D1. PDF with the PDF app disabled (Drive PDF viewer)
disable Drive: Package com.google.android.apps.docs new state: disabled-user
PDF handlers now: No activities found 
PDF noapp +1216 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF noapp +2714 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF noapp +3625 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF noapp +4779 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF noapp +5640 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF noapp final: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity; text="Welcome to Chrome" text="By using Chrome, you agree to the Google Terms of Service, and the Google Chrome and ChromeOS Additional Terms of Service." text="Help make Chrome better by sending usag
## D2. PDF download fails (offline while on Documents) → toast + browser
offline: airplane=1
PDF failed +1354 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF failed +2595 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF failed +3447 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF failed +4293 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF failed +5137 ms: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity
PDF failed final: fg com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity; text="Welcome to Chrome" text="By using Chrome, you agree to the Google Terms of Service, and the Google Chrome and ChromeOS Additional Terms of Service." text="Help make Chrome better by sending usag
enable Drive: Package com.google.android.apps.docs new state: enabled
## E. Contacts: the e-mail line in the card
email nodes: clickable="true" clickable="false" bounds="[195,1593][985,1719]" 
email line tap @ 590 1656 → fg com.google.android.gm/com.google.android.gm.welcome.WelcomeTourActivity
## COMPLETE
## logcat findings
app pids seen: ['6422', '7036']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=36 total=36
== Davey: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Screenshots

### media2/shots/media2

- [media2/shots/media2/01-album-10s.png](media2/shots/media2/01-album-10s.png)
- [media2/shots/media2/01-album-20s.png](media2/shots/media2/01-album-20s.png)
- [media2/shots/media2/01-album-2s.png](media2/shots/media2/01-album-2s.png)
- [media2/shots/media2/01-album-40s.png](media2/shots/media2/01-album-40s.png)
- [media2/shots/media2/01-album-5s.png](media2/shots/media2/01-album-5s.png)
- [media2/shots/media2/02-album-scrolled-5s.png](media2/shots/media2/02-album-scrolled-5s.png)
- [media2/shots/media2/03-album-scrolled-15s.png](media2/shots/media2/03-album-scrolled-15s.png)
- [media2/shots/media2/10-viewer-1s.png](media2/shots/media2/10-viewer-1s.png)
- [media2/shots/media2/10-viewer-3s.png](media2/shots/media2/10-viewer-3s.png)
- [media2/shots/media2/10-viewer-8s.png](media2/shots/media2/10-viewer-8s.png)
- [media2/shots/media2/11-viewer-doubletap.png](media2/shots/media2/11-viewer-doubletap.png)
- [media2/shots/media2/12-viewer-pinch-1s.png](media2/shots/media2/12-viewer-pinch-1s.png)
- [media2/shots/media2/13-viewer-pinch-6s.png](media2/shots/media2/13-viewer-pinch-6s.png)
- [media2/shots/media2/14-viewer-zoomed-swipe.png](media2/shots/media2/14-viewer-zoomed-swipe.png)
- [media2/shots/media2/15-viewer-reset.png](media2/shots/media2/15-viewer-reset.png)
- [media2/shots/media2/16-viewer-photo5-2s.png](media2/shots/media2/16-viewer-photo5-2s.png)
- [media2/shots/media2/17-viewer-photo5-10s.png](media2/shots/media2/17-viewer-photo5-10s.png)
- [media2/shots/media2/20-book-1894.png](media2/shots/media2/20-book-1894.png)
- [media2/shots/media2/20-book-2091.png](media2/shots/media2/20-book-2091.png)
- [media2/shots/media2/20-book-2184.png](media2/shots/media2/20-book-2184.png)
- [media2/shots/media2/20-book-259.png](media2/shots/media2/20-book-259.png)
- [media2/shots/media2/20-book-958.png](media2/shots/media2/20-book-958.png)
- [media2/shots/media2/21-catalog-gatsby.png](media2/shots/media2/21-catalog-gatsby.png)
- [media2/shots/media2/30-contacts-email-line.png](media2/shots/media2/30-contacts-email-line.png)
- [media2/shots/media2/pdf-failed-1.png](media2/shots/media2/pdf-failed-1.png)
- [media2/shots/media2/pdf-failed-2.png](media2/shots/media2/pdf-failed-2.png)
- [media2/shots/media2/pdf-failed-3.png](media2/shots/media2/pdf-failed-3.png)
- [media2/shots/media2/pdf-failed-4.png](media2/shots/media2/pdf-failed-4.png)
- [media2/shots/media2/pdf-failed-5.png](media2/shots/media2/pdf-failed-5.png)
- [media2/shots/media2/pdf-failed-final.png](media2/shots/media2/pdf-failed-final.png)
- [media2/shots/media2/pdf-noapp-1.png](media2/shots/media2/pdf-noapp-1.png)
- [media2/shots/media2/pdf-noapp-2.png](media2/shots/media2/pdf-noapp-2.png)
- [media2/shots/media2/pdf-noapp-3.png](media2/shots/media2/pdf-noapp-3.png)
- [media2/shots/media2/pdf-noapp-4.png](media2/shots/media2/pdf-noapp-4.png)
- [media2/shots/media2/pdf-noapp-5.png](media2/shots/media2/pdf-noapp-5.png)
- [media2/shots/media2/pdf-noapp-final.png](media2/shots/media2/pdf-noapp-final.png)

## Other files

- [media2/contacts-ui.xml](media2/contacts-ui.xml)
- [media2/getevent.txt](media2/getevent.txt)
- [media2/host-after-run.txt](media2/host-after-run.txt)
- [media2/job-status.txt](media2/job-status.txt)
- [media2/logcat-all.txt](media2/logcat-all.txt)
- [media2/logcat-dump.txt](media2/logcat-dump.txt)
- [media2/logcat-findings.txt](media2/logcat-findings.txt)
- [media2/logcat-live.txt](media2/logcat-live.txt)
- [media2/mem-zoomed-raw.txt](media2/mem-zoomed-raw.txt)
- [media2/mem-zoomed.txt](media2/mem-zoomed.txt)
- [media2/mt.sh](media2/mt.sh)
- [media2/probe_folklorna-grupa.txt](media2/probe_folklorna-grupa.txt)
- [media2/probe_fotodokumentalna-izlozhba.txt](media2/probe_fotodokumentalna-izlozhba.txt)
- [media2/summary.txt](media2/summary.txt)
