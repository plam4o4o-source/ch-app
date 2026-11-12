# wave20/catalog: Surprise memory on old Android (run 42861478506 @ 33e130b)

## surprise26

```
part=surprise26 target=google_apis api=26 gpu=swangle_indirect emulator-step=success
part surprise26; APK: wave20/catalog 33e130b built prodRelease x86_64 (throwaway key); versionCode 1500190 size 4286414 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 8.0.0 sdk 26; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: wave20/catalog 33e130b built prodRelease x86_64 (throwaway key); versionCode 1500190 size 4286414 bytes
installed:  versionCode=1500190 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-v26
animator scale: 1 (0 = reduced motion: no fan/flip animation)
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2012 ms
## SM0. Device memory: MemTotal: 2046988 kB; API 26
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0; TOTAL: 79279 TOTAL SWAP PSS: 0;
MEMPROBE dice-1: samples 49; app RSS 164→max 169 MB at +3200 ms; min MemAvailable 1216 MB at +3530 ms
after 10 s:  Graphics: 0; TOTAL: 73334 TOTAL SWAP PSS: 0;
SM1 after 10 s: app in front; pid '4928'; scene shown; deaths: 0 ()
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0; TOTAL: 73710 TOTAL SWAP PSS: 0;
MEMPROBE dice-2: samples 44; app RSS 165→max 167 MB at +2260 ms; min MemAvailable 1233 MB at +4439 ms
after 10 s:  Graphics: 0; TOTAL: 73198 TOTAL SWAP PSS: 0;
SM2 after 10 s: app in front; pid '4928'; scene shown; deaths: 0 ()
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0; TOTAL: 73497 TOTAL SWAP PSS: 0;
MEMPROBE dice-3: samples 45; app RSS 165→max 169 MB at +9150 ms; min MemAvailable 1233 MB at +2140 ms
after 10 s:  Graphics: 0; TOTAL: 74681 TOTAL SWAP PSS: 0;
SM3 after 10 s: app in front; pid '4928'; scene shown; deaths: 0 ()
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 36; app RSS 167→max 172 MB at +2189 ms; min MemAvailable 1234 MB at +4120 ms
## COMPLETE
## logcat findings
app pids seen: ['4928']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=39 total=39
== Davey: 0
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## surprise28

```
part=surprise28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success
part surprise28; APK: wave20/catalog 33e130b built prodRelease x86_64 (throwaway key); versionCode 1500190 size 4286414 bytes
base APK:  size  bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: wave20/catalog 33e130b built prodRelease x86_64 (throwaway key); versionCode 1500190 size 4286414 bytes
installed:  versionCode=1500190 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
animator scale: 1 (0 = reduced motion: no fan/flip animation)
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2206 ms
## SM0. Device memory: MemTotal: 2046996 kB; API 28
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0; TOTAL: 76331 TOTAL SWAP PSS: 0;
MEMPROBE dice-1: samples 47; app RSS 176→max 180 MB at +3519 ms; min MemAvailable 1108 MB at +3519 ms
after 10 s:  Graphics: 0; TOTAL: 73667 TOTAL SWAP PSS: 0;
SM1 after 10 s: app in front; pid '5683'; scene shown; deaths: 1 ()
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0; TOTAL: 74518 TOTAL SWAP PSS: 0;
MEMPROBE dice-2: samples 45; app RSS 179→max 181 MB at +3959 ms; min MemAvailable 1145 MB at +3959 ms
after 10 s:  Graphics: 0; TOTAL: 73797 TOTAL SWAP PSS: 0;
SM2 after 10 s: app in front; pid '5683'; scene shown; deaths: 0 ()
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0; TOTAL: 73413 TOTAL SWAP PSS: 0;
MEMPROBE dice-3: samples 45; app RSS 179→max 182 MB at +3939 ms; min MemAvailable 1143 MB at +4240 ms
after 10 s:  Graphics: 0; TOTAL: 73701 TOTAL SWAP PSS: 0;
SM3 after 10 s: app in front; pid '5683'; scene shown; deaths: 0 ()
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 36; app RSS 181→max 184 MB at +2289 ms; min MemAvailable 1147 MB at +5279 ms
## COMPLETE
## logcat findings
app pids seen: ['5683']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 2
   skipped-frames events=2 max=51 total=82
== Davey: 3
   davey events=3 max=901ms median=787ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## surprise30

```
part=surprise30 target=google_apis api=30 gpu=swangle_indirect emulator-step=success
part surprise30; APK: wave20/catalog 33e130b built prodRelease x86_64 (throwaway key); versionCode 1500190 size 4286414 bytes
base APK:  size  bytes
host: 4 cpu, 15982 MB RAM
android 11 sdk 30; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: wave20/catalog 33e130b built prodRelease x86_64 (throwaway key); versionCode 1500190 size 4286414 bytes
installed:  versionCode=1500190 minSdk=26 targetSdk=37  versionName=1.0.0 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-nowidecg-lowdr-port-notnight-420dpi-finger-keysexposed-qwerty-navhidden-nonav-2274x1080-v30
animator scale: 1 (0 = reduced motion: no fan/flip animation)
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2106 ms
## SM0. Device memory: MemTotal: 2028632 kB; API 30
## SM1. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0 0; TOTAL PSS: 87438 TOTAL RSS: 202596 TOTAL SWAP PSS: 292;
MEMPROBE dice-1: samples 45; app RSS 179→max 186 MB at +3629 ms; min MemAvailable 939 MB at +2230 ms
after 10 s:  Graphics: 0 0; TOTAL PSS: 77799 TOTAL RSS: 193056 TOTAL SWAP PSS: 220;
SM1 after 10 s: app in front; pid '5924'; scene shown; deaths: 1 ()
## SM2. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0 0; TOTAL PSS: 78035 TOTAL RSS: 193320 TOTAL SWAP PSS: 220;
MEMPROBE dice-2: samples 44; app RSS 182→max 187 MB at +3220 ms; min MemAvailable 946 MB at +2210 ms
after 10 s:  Graphics: 0 0; TOTAL PSS: 77314 TOTAL RSS: 192772 TOTAL SWAP PSS: 220;
SM2 after 10 s: app in front; pid '5924'; scene shown; deaths: 0 ()
## SM3. 'Изненадай ме' (dice): app RSS and MemAvailable every 0.25 s, 10 s
before:  Graphics: 0 0; TOTAL PSS: 79085 TOTAL RSS: 194568 TOTAL SWAP PSS: 220;
MEMPROBE dice-3: samples 44; app RSS 183→max 189 MB at +3889 ms; min MemAvailable 953 MB at +2219 ms
after 10 s:  Graphics: 0 0; TOTAL PSS: 77228 TOTAL RSS: 192396 TOTAL SWAP PSS: 227;
SM3 after 10 s: app in front; pid '5924'; scene shown; deaths: 2 ()
## SM9. For comparison: open a book from the catalogue list (same memory probe)
MEMPROBE book: samples 36; app RSS 186→max 190 MB at +2740 ms; min MemAvailable 961 MB at +1689 ms
## COMPLETE
## logcat findings
app pids seen: ['5924']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 1
   skipped-frames events=1 max=38 total=38
== Davey: 1
   davey events=1 max=876ms median=876ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

