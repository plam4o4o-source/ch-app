# Emulator check of the v1.2.35 shaders (9295534, versionCode 1504706) vs the released v1.2.34 (3812d8e, 1504705)

debug/w21-shader (runs in runs.txt, latest 43271392580 @ 71c9812). API 33 google_apis x86_64, -gpu swangle_indirect (software GPU); old28s: API 28. Earlier: [../v134/index.md](../v134/index.md), [../v134-card/index.md](../v134-card/index.md).

| part | status |
|---|---|
| card35 | COMPLETE; part=card35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| full28 | COMPLETE; part=full28 target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| now35 | COMPLETE; part=now35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| old28s | COMPLETE; part=old28s target=google_apis api=28 gpu=swangle_indirect emulator-step=success |
| reg35 | COMPLETE; part=reg35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| start35 | COMPLETE; part=start35 target=google_apis api=33 gpu=swangle_indirect emulator-step=success |
| start35b | COMPLETE; part=start35b target=google_apis api=33 gpu=swangle_indirect emulator-step=success |

```
1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705
1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706
app.apk 4318682 bytes
app-base.apk 4316450 bytes
```

## card35

```
part card35; APK: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
installed:  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order: new base base new (each a fresh install; the card added by hand in each)
## C new-1: fresh install, card added by hand, then 5× (cold start, 5 s, Моето › Читателска карта › Покажи картата) + no-wait + cleared GL cache
installed new-1: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
new-1 GL shader cache after the first card: 
TRIAL new-1-1-wait: start TotalTime: 4039; taps after the first frame: My +5352 ms, card +7425 ms, show +10916 ms; fullscreen reached: yes
FS new-1-1-wait-cardscreen: n=16 slowest=599.9ms (frame #12; ui 231.3 rt 193.6 issue 63.2 flags 0) first5=[69, 129, 257, 546, 514] >32ms=16 >64ms=16 >100ms=15 span=2333ms
FS new-1-1-wait-full: n=13 slowest=1199.5ms (frame #5; ui 824.2 rt 268.9 issue 165.9 flags 0) first5=[148, 303, 418, 1164, 1199] >32ms=13 >64ms=13 >100ms=13 span=2067ms
TRIAL new-1-2-wait: start TotalTime: 3756; taps after the first frame: My +5344 ms, card +7405 ms, show +11350 ms; fullscreen reached: yes
FS new-1-2-wait-cardscreen: n=15 slowest=662.0ms (frame #12; ui 219.9 rt 276.7 issue 130.7 flags 0) first5=[79, 130, 260, 529, 491] >32ms=15 >64ms=15 >100ms=14 span=2217ms
FS new-1-2-wait-full: n=13 slowest=1044.2ms (frame #6; ui 618.6 rt 252.7 issue 126.3 flags 0) first5=[289, 464, 526, 626, 1041] >32ms=13 >64ms=13 >100ms=13 span=2333ms
TRIAL new-1-3-wait: start TotalTime: 3204; taps after the first frame: My +5337 ms, card +7400 ms, show +11052 ms; fullscreen reached: yes
FS new-1-3-wait-cardscreen: n=15 slowest=666.9ms (frame #11; ui 237.8 rt 264.4 issue 63.0 flags 0) first5=[78, 141, 355, 581, 596] >32ms=15 >64ms=15 >100ms=14 span=2050ms
FS new-1-3-wait-full: n=13 slowest=935.0ms (frame #6; ui 280.5 rt 603.7 issue 438.6 flags 0) first5=[255, 395, 205, 397, 611] >32ms=13 >64ms=13 >100ms=13 span=2367ms
TRIAL new-1-4-wait: start TotalTime: 3462; taps after the first frame: My +5445 ms, card +7509 ms, show +11480 ms; fullscreen reached: yes
FS new-1-4-wait-cardscreen: n=16 slowest=664.2ms (frame #12; ui 244.0 rt 233.6 issue 62.1 flags 0) first5=[72, 133, 267, 550, 593] >32ms=16 >64ms=16 >100ms=15 span=2200ms
FS new-1-4-wait-full: n=15 slowest=774.2ms (frame #6; ui 388.9 rt 371.9 issue 271.4 flags 0) first5=[191, 252, 473, 376, 659] >32ms=15 >64ms=15 >100ms=15 span=2450ms
TRIAL new-1-5-wait: start TotalTime: 3114; taps after the first frame: My +5474 ms, card +7549 ms, show +11167 ms; fullscreen reached: yes
FS new-1-5-wait-cardscreen: n=16 slowest=609.1ms (frame #4; ui 274.7 rt 222.9 issue 102.7 flags 0) first5=[150, 361, 188, 609, 481] >32ms=16 >64ms=16 >100ms=16 span=2217ms
FS new-1-5-wait-full: n=14 slowest=825.8ms (frame #6; ui 359.3 rt 263.1 issue 157.5 flags 0) first5=[197, 350, 500, 489, 723] >32ms=14 >64ms=14 >100ms=14 span=2217ms
TRIAL new-1-now: start TotalTime: 3114; taps after the first frame: My +903 ms, card +1578 ms, show +2603 ms; fullscreen reached: NO
FS new-1-now-full: n=5 slowest=361.6ms (frame #5; ui 179.2 rt 129.7 issue 78.3 flags 0) first5=[183, 148, 262, 332, 362] >32ms=5 >64ms=5 >100ms=5 span=300ms
TRIAL new-1-coldgl: start TotalTime: 2672; taps after the first frame: My +5676 ms, card +7756 ms, show +11190 ms; fullscreen reached: yes
FS new-1-coldgl-cardscreen: n=14 slowest=614.7ms (frame #5; ui 161.2 rt 292.5 issue 143.7 flags 0) first5=[70, 125, 240, 597, 615] >32ms=14 >64ms=14 >100ms=13 span=2200ms
FS new-1-coldgl-full: n=14 slowest=735.8ms (frame #5; ui 284.4 rt 400.1 issue 301.7 flags 0) first5=[178, 351, 420, 627, 736] >32ms=14 >64ms=14 >100ms=14 span=2317ms
TRIAL new-1-now-coldgl: start TotalTime: 2880; taps after the first frame: My +73 ms, card +618 ms, show +1862 ms; fullscreen reached: NO
FS new-1-now-coldgl-full: n=8 slowest=908.7ms (frame #4; ui 461.2 rt 226.1 issue 87.3 flags 0) first5=[502, 471, 685, 909, 323] >32ms=8 >64ms=8 >100ms=8 span=1467ms
new-1 GL shader cache at the end: 
## C base-2: fresh install, card added by hand, then 5× (cold start, 5 s, Моето › Читателска карта › Покажи картата) + no-wait + cleared GL cache
installed base-2: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
base-2 GL shader cache after the first card: 
TRIAL base-2-1-wait: start TotalTime: 3203; taps after the first frame: My +5387 ms, card +7452 ms, show +11133 ms; fullscreen reached: yes
FS base-2-1-wait-cardscreen: n=16 slowest=615.4ms (frame #9; ui 223.2 rt 251.4 issue 129.5 flags 0) first5=[71, 119, 379, 594, 507] >32ms=16 >64ms=16 >100ms=15 span=2333ms
FS base-2-1-wait-full: n=15 slowest=768.7ms (frame #6; ui 245.3 rt 363.2 issue 247.1 flags 0) first5=[374, 343, 393, 383, 700] >32ms=15 >64ms=15 >100ms=15 span=2683ms
TRIAL base-2-2-wait: start TotalTime: 3184; taps after the first frame: My +5372 ms, card +7445 ms, show +10951 ms; fullscreen reached: yes
FS base-2-2-wait-cardscreen: n=16 slowest=694.9ms (frame #11; ui 221.5 rt 195.9 issue 63.0 flags 0) first5=[77, 137, 367, 635, 593] >32ms=16 >64ms=16 >100ms=15 span=2433ms
FS base-2-2-wait-full: n=13 slowest=826.6ms (frame #5; ui 402.9 rt 336.7 issue 232.4 flags 0) first5=[216, 368, 391, 634, 827] >32ms=13 >64ms=13 >100ms=13 span=2000ms
TRIAL base-2-3-wait: start TotalTime: 3301; taps after the first frame: My +5449 ms, card +7509 ms, show +11293 ms; fullscreen reached: yes
FS base-2-3-wait-cardscreen: n=16 slowest=637.7ms (frame #11; ui 203.3 rt 264.4 issue 135.4 flags 0) first5=[70, 129, 303, 618, 564] >32ms=16 >64ms=16 >100ms=15 span=2367ms
FS base-2-3-wait-full: n=13 slowest=1121.8ms (frame #5; ui 470.3 rt 347.1 issue 244.7 flags 0) first5=[457, 484, 469, 725, 1122] >32ms=13 >64ms=13 >100ms=13 span=2533ms
TRIAL base-2-4-wait: start TotalTime: 3057; taps after the first frame: My +5455 ms, card +7516 ms, show +10785 ms; fullscreen reached: yes
FS base-2-4-wait-cardscreen: n=16 slowest=659.3ms (frame #4; ui 263.2 rt 192.5 issue 147.5 flags 0) first5=[66, 177, 500, 659, 147] >32ms=16 >64ms=16 >100ms=15 span=2050ms
FS base-2-4-wait-full: n=13 slowest=768.2ms (frame #4; ui 204.5 rt 380.7 issue 253.6 flags 0) first5=[200, 326, 504, 768, 661] >32ms=13 >64ms=13 >100ms=13 span=2167ms
TRIAL base-2-5-wait: start TotalTime: 3625; taps after the first frame: My +5515 ms, card +7570 ms, show +11007 ms; fullscreen reached: yes
FS base-2-5-wait-cardscreen: n=16 slowest=610.4ms (frame #11; ui 221.1 rt 195.4 issue 61.4 flags 0) first5=[82, 199, 316, 585, 483] >32ms=16 >64ms=16 >100ms=15 span=2217ms
FS base-2-5-wait-full: n=13 slowest=880.3ms (frame #5; ui 437.5 rt 266.4 issue 164.7 flags 0) first5=[320, 362, 389, 847, 880] >32ms=13 >64ms=13 >100ms=13 span=2317ms
TRIAL base-2-now: start TotalTime: 3055; taps after the first frame: My +85 ms, card +974 ms, show +2014 ms; fullscreen reached: NO
FS base-2-now-full: n=13 slowest=813.4ms (frame #4; ui 321.3 rt 205.1 issue 3.0 flags 0) first5=[453, 334, 455, 813, 670] >32ms=13 >64ms=13 >100ms=12 span=1800ms
TRIAL base-2-coldgl: start TotalTime: 2866; taps after the first frame: My +5344 ms, card +7423 ms, show +10823 ms; fullscreen reached: yes
FS base-2-coldgl-cardscreen: n=16 slowest=622.7ms (frame #4; ui 155.9 rt 267.6 issue 139.2 flags 0) first5=[80, 127, 371, 623, 575] >32ms=16 >64ms=16 >100ms=15 span=2183ms
FS base-2-coldgl-full: n=11 slowest=886.4ms (frame #3; ui 393.3 rt 263.0 issue 156.1 flags 0) first5=[181, 730, 886, 357, 710] >32ms=11 >64ms=11 >100ms=11 span=1833ms
TRIAL base-2-now-coldgl: start TotalTime: 3171; taps after the first frame: My +590 ms, card +1262 ms, show +2422 ms; fullscreen reached: NO
FS base-2-now-coldgl-full: n=8 slowest=637.0ms (frame #4; ui 239.8 rt 200.9 issue 26.3 flags 0) first5=[191, 357, 449, 637, 485] >32ms=8 >64ms=8 >100ms=8 span=950ms
base-2 GL shader cache at the end: 
## C base-3: fresh install, card added by hand, then 5× (cold start, 5 s, Моето › Читателска карта › Покажи картата) + no-wait + cleared GL cache
installed base-3: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
base-3 GL shader cache after the first card: 
TRIAL base-3-1-wait: start TotalTime: 3233; taps after the first frame: My +5573 ms, card +7636 ms, show +11415 ms; fullscreen reached: yes
FS base-3-1-wait-cardscreen: n=16 slowest=616.8ms (frame #7; ui 288.5 rt 243.7 issue 57.5 flags 0) first5=[69, 128, 293, 567, 566] >32ms=16 >64ms=16 >100ms=15 span=2467ms
FS base-3-1-wait-full: n=14 slowest=821.6ms (frame #5; ui 264.4 rt 443.7 issue 311.5 flags 0) first5=[234, 322, 373, 713, 822] >32ms=14 >64ms=14 >100ms=14 span=2383ms
TRIAL base-3-2-wait: start TotalTime: 3241; taps after the first frame: My +5545 ms, card +7619 ms, show +11152 ms; fullscreen reached: yes
FS base-3-2-wait-cardscreen: n=15 slowest=657.4ms (frame #15; ui 172.1 rt 349.2 issue 141.1 flags 0) first5=[70, 124, 383, 651, 598] >32ms=15 >64ms=15 >100ms=14 span=2050ms
FS base-3-2-wait-full: n=13 slowest=713.7ms (frame #4; ui 167.9 rt 369.5 issue 254.7 flags 0) first5=[217, 442, 577, 714, 700] >32ms=13 >64ms=13 >100ms=13 span=2150ms
TRIAL base-3-3-wait: start TotalTime: 3143; taps after the first frame: My +5560 ms, card +7632 ms, show +11300 ms; fullscreen reached: yes
FS base-3-3-wait-cardscreen: n=16 slowest=618.6ms (frame #4; ui 145.2 rt 251.0 issue 86.1 flags 0) first5=[87, 139, 323, 619, 575] >32ms=16 >64ms=16 >100ms=15 span=2333ms
FS base-3-3-wait-full: n=15 slowest=798.9ms (frame #6; ui 286.0 rt 407.4 issue 300.0 flags 0) first5=[242, 312, 469, 385, 635] >32ms=15 >64ms=15 >100ms=15 span=2450ms
TRIAL base-3-4-wait: start TotalTime: 3341; taps after the first frame: My +5619 ms, card +7680 ms, show +11075 ms; fullscreen reached: yes
FS base-3-4-wait-cardscreen: n=16 slowest=605.0ms (frame #11; ui 229.5 rt 183.6 issue 55.6 flags 0) first5=[72, 127, 305, 586, 486] >32ms=16 >64ms=16 >100ms=15 span=2433ms
FS base-3-4-wait-full: n=12 slowest=824.8ms (frame #5; ui 325.7 rt 341.2 issue 242.1 flags 0) first5=[443, 396, 428, 750, 825] >32ms=12 >64ms=12 >100ms=12 span=2167ms
TRIAL base-3-5-wait: start TotalTime: 3025; taps after the first frame: My +5467 ms, card +7538 ms, show +10881 ms; fullscreen reached: yes
FS base-3-5-wait-cardscreen: n=16 slowest=607.4ms (frame #4; ui 152.5 rt 269.0 issue 159.7 flags 0) first5=[80, 136, 291, 607, 560] >32ms=16 >64ms=16 >100ms=15 span=2367ms
FS base-3-5-wait-full: n=12 slowest=1087.2ms (frame #5; ui 456.1 rt 361.7 issue 267.9 flags 0) first5=[314, 448, 403, 736, 1087] >32ms=12 >64ms=12 >100ms=12 span=1967ms
TRIAL base-3-now: start TotalTime: 3461; taps after the first frame: My +126 ms, card +883 ms, show +2475 ms; fullscreen reached: NO
FS base-3-now-full: n=16 slowest=589.1ms (frame #5; ui 310.3 rt 228.1 issue 94.5 flags 0) first5=[551, 286, 346, 502, 589] >32ms=16 >64ms=15 >100ms=15 span=2200ms
TRIAL base-3-coldgl: start TotalTime: 3025; taps after the first frame: My +5317 ms, card +7413 ms, show +10979 ms; fullscreen reached: yes
FS base-3-coldgl-cardscreen: n=15 slowest=712.9ms (frame #11; ui 253.9 rt 256.9 issue 129.5 flags 0) first5=[72, 126, 320, 671, 652] >32ms=15 >64ms=15 >100ms=14 span=2133ms
FS base-3-coldgl-full: n=12 slowest=1014.6ms (frame #4; ui 518.0 rt 357.4 issue 240.1 flags 0) first5=[181, 398, 665, 1015, 719] >32ms=12 >64ms=12 >100ms=12 span=2150ms
TRIAL base-3-now-coldgl: start TotalTime: 2999; taps after the first frame: My +815 ms, card +1320 ms, show +2467 ms; fullscreen reached: NO
FS base-3-now-coldgl-full: n=9 slowest=835.4ms (frame #4; ui 327.8 rt 204.2 issue 8.0 flags 0) first5=[534, 450, 448, 835, 685] >32ms=9 >64ms=9 >100ms=9 span=1500ms
base-3 GL shader cache at the end: 
## C new-4: fresh install, card added by hand, then 5× (cold start, 5 s, Моето › Читателска карта › Покажи картата) + no-wait + cleared GL cache
installed new-4: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
new-4 GL shader cache after the first card: 
TRIAL new-4-1-wait: start TotalTime: 3620; taps after the first frame: My +5533 ms, card +7600 ms, show +10990 ms; fullscreen reached: yes
FS new-4-1-wait-cardscreen: n=16 slowest=682.8ms (frame #7; ui 285.7 rt 158.4 issue 69.1 flags 0) first5=[82, 200, 277, 548, 580] >32ms=16 >64ms=16 >100ms=15 span=2400ms
FS new-4-1-wait-full: n=14 slowest=797.6ms (frame #5; ui 258.6 rt 365.7 issue 174.6 flags 0) first5=[296, 355, 424, 615, 798] >32ms=14 >64ms=14 >100ms=14 span=2467ms
TRIAL new-4-2-wait: start TotalTime: 3322; taps after the first frame: My +5299 ms, card +7363 ms, show +10903 ms; fullscreen reached: yes
FS new-4-2-wait-cardscreen: n=16 slowest=673.7ms (frame #12; ui 234.3 rt 249.3 issue 58.4 flags 0) first5=[87, 129, 254, 542, 497] >32ms=16 >64ms=16 >100ms=15 span=2383ms
FS new-4-2-wait-full: n=15 slowest=805.8ms (frame #6; ui 406.8 rt 217.4 issue 119.0 flags 0) first5=[408, 377, 616, 373, 638] >32ms=15 >64ms=15 >100ms=15 span=2650ms
TRIAL new-4-3-wait: start TotalTime: 2822; taps after the first frame: My +5618 ms, card +7688 ms, show +11236 ms; fullscreen reached: yes
FS new-4-3-wait-cardscreen: n=16 slowest=688.5ms (frame #5; ui 177.6 rt 347.3 issue 239.6 flags 0) first5=[68, 126, 242, 583, 688] >32ms=16 >64ms=16 >100ms=15 span=2200ms
FS new-4-3-wait-full: n=13 slowest=880.0ms (frame #5; ui 455.5 rt 424.4 issue 317.5 flags 0) first5=[262, 378, 383, 851, 880] >32ms=13 >64ms=13 >100ms=13 span=2183ms
TRIAL new-4-4-wait: start TotalTime: 3354; taps after the first frame: My +5373 ms, card +7439 ms, show +11086 ms; fullscreen reached: yes
FS new-4-4-wait-cardscreen: n=16 slowest=674.7ms (frame #4; ui 247.1 rt 240.2 issue 99.9 flags 0) first5=[75, 129, 322, 675, 616] >32ms=16 >64ms=16 >100ms=15 span=2133ms
FS new-4-4-wait-full: n=12 slowest=916.1ms (frame #7; ui 360.5 rt 224.3 issue 134.6 flags 0) first5=[235, 388, 373, 656, 752] >32ms=12 >64ms=12 >100ms=12 span=2133ms
TRIAL new-4-5-wait: start TotalTime: 3647; taps after the first frame: My +5330 ms, card +7392 ms, show +10879 ms; fullscreen reached: yes
FS new-4-5-wait-cardscreen: n=16 slowest=630.8ms (frame #7; ui 262.7 rt 202.1 issue 54.4 flags 0) first5=[70, 126, 269, 533, 572] >32ms=16 >64ms=16 >100ms=15 span=2317ms
FS new-4-5-wait-full: n=12 slowest=748.5ms (frame #5; ui 367.6 rt 327.7 issue 230.1 flags 0) first5=[410, 447, 533, 745, 749] >32ms=12 >64ms=12 >100ms=12 span=2283ms
TRIAL new-4-now: start TotalTime: 3047; taps after the first frame: My +454 ms, card +1196 ms, show +2266 ms; fullscreen reached: NO
FS new-4-now-full: n=6 slowest=382.0ms (frame #6; ui 122.7 rt 165.1 issue 121.6 flags 0) first5=[201, 191, 84, 217, 253] >32ms=6 >64ms=6 >100ms=5 span=433ms
TRIAL new-4-coldgl: start TotalTime: 3108; taps after the first frame: My +5673 ms, card +7740 ms, show +11167 ms; fullscreen reached: yes
FS new-4-coldgl-cardscreen: n=16 slowest=629.9ms (frame #7; ui 259.4 rt 200.2 issue 50.4 flags 0) first5=[80, 133, 275, 531, 554] >32ms=16 >64ms=16 >100ms=15 span=2200ms
FS new-4-coldgl-full: n=14 slowest=765.4ms (frame #5; ui 341.6 rt 360.8 issue 241.3 flags 0) first5=[243, 294, 409, 605, 765] >32ms=14 >64ms=14 >100ms=14 span=2367ms
TRIAL new-4-now-coldgl: start TotalTime: 2929; taps after the first frame: My +572 ms, card +1368 ms, show +2446 ms; fullscreen reached: NO
FS new-4-now-coldgl-full: n=2 slowest=259.9ms (frame #2; ui 10.8 rt 171.0 issue 131.4 flags 0) first5=[222, 260] >32ms=2 >64ms=2 >100ms=2 span=133ms
new-4 GL shader cache at the end: 
## C totals: per build and mode — the slowest frame of each open, the first 5 frames (ms, IntendedVsync → FrameCompleted)
FSAGG full       coldgl      new  opens= 2  slowest frame: median  750.6 ms, mean  750.6, min  735.8, max  765.4  | frame 1 median  210.5 ms | frames 2-5 median 512.5 ms  | slowest per open: [736, 765] at frame # [5, 5]
FSAGG full       coldgl      base opens= 2  slowest frame: median  950.5 ms, mean  950.5, min  886.4, max 1014.6  | frame 1 median  181.0 ms | frames 2-5 median 714.5 ms  | slowest per open: [886, 1015] at frame # [3, 4]
FSAGG full       now         new  opens= 2  slowest frame: median  371.8 ms, mean  371.8, min  361.6, max  382.0  | frame 1 median  192.0 ms | frames 2-5 median 235.0 ms  | slowest per open: [362, 382] at frame # [5, 6]
FSAGG full       now         base opens= 2  slowest frame: median  701.2 ms, mean  701.2, min  589.1, max  813.4  | frame 1 median  502.0 ms | frames 2-5 median 478.5 ms  | slowest per open: [813, 589] at frame # [4, 5]
FSAGG full       now-coldgl  new  opens= 2  slowest frame: median  584.3 ms, mean  584.3, min  259.9, max  908.7  | frame 1 median  362.0 ms | frames 2-5 median 471.0 ms  | slowest per open: [909, 260] at frame # [4, 2]
FSAGG full       now-coldgl  base opens= 2  slowest frame: median  736.2 ms, mean  736.2, min  637.0, max  835.4  | frame 1 median  362.5 ms | frames 2-5 median 467.5 ms  | slowest per open: [637, 835] at frame # [4, 4]
FSAGG full       wait5s      new  opens=10  slowest frame: median  852.9 ms, mean  892.7, min  748.5, max 1199.5  | frame 1 median  258.5 ms | frames 2-5 median 494.5 ms  | slowest per open: [1200, 1044, 935, 774, 826, 798, 806, 880, 916, 748] at frame # [5, 6, 6, 6, 6, 5, 6, 5, 7, 5]
FSAGG full       wait5s      base opens=10  slowest frame: median  823.2 ms, mean  861.2, min  713.7, max 1121.8  | frame 1 median  278.0 ms | frames 2-5 median 494.0 ms  | slowest per open: [769, 827, 1122, 768, 880, 822, 714, 799, 825, 1087] at frame # [6, 5, 5, 4, 5, 5, 4, 6, 5, 5]
FSAGG cardscreen coldgl      new  opens= 2  slowest frame: median  622.3 ms, mean  622.3, min  614.7, max  629.9  | frame 1 median   75.0 ms | frames 2-5 median 403.0 ms  | slowest per open: [615, 630] at frame # [5, 7]
FSAGG cardscreen coldgl      base opens= 2  slowest frame: median  667.8 ms, mean  667.8, min  622.7, max  712.9  | frame 1 median   76.0 ms | frames 2-5 median 473.0 ms  | slowest per open: [623, 713] at frame # [4, 11]
FSAGG cardscreen wait5s      new  opens=10  slowest frame: median  665.5 ms, mean  655.3, min  599.9, max  688.5  | frame 1 median   76.5 ms | frames 2-5 median 421.0 ms  | slowest per open: [600, 662, 667, 664, 609, 683, 674, 688, 675, 631] at frame # [12, 12, 11, 12, 4, 7, 12, 5, 4, 7]
FSAGG cardscreen wait5s      base opens=10  slowest frame: median  617.7 ms, mean  632.3, min  605.0, max  694.9  | frame 1 median   71.5 ms | frames 2-5 median 433.0 ms  | slowest per open: [615, 695, 638, 659, 610, 617, 657, 619, 605, 607] at frame # [9, 11, 11, 4, 11, 7, 15, 4, 11, 4]
FSAGG first-5 frames per open (ms):
   full new-1-coldgl           first5=[178, 351, 420, 627, 736] slowest=736 (#5, rt 400)
   full new-4-coldgl           first5=[243, 294, 409, 605, 765] slowest=765 (#5, rt 361)
   full base-2-coldgl          first5=[181, 730, 886, 357, 710] slowest=886 (#3, rt 263)
   full base-3-coldgl          first5=[181, 398, 665, 1015, 719] slowest=1015 (#4, rt 357)
   full new-1-now              first5=[183, 148, 262, 332, 362] slowest=362 (#5, rt 130)
   full new-4-now              first5=[201, 191, 84, 217, 253] slowest=382 (#6, rt 165)
   full base-2-now             first5=[453, 334, 455, 813, 670] slowest=813 (#4, rt 205)
   full base-3-now             first5=[551, 286, 346, 502, 589] slowest=589 (#5, rt 228)
   full new-1-now-coldgl       first5=[502, 471, 685, 909, 323] slowest=909 (#4, rt 226)
   full new-4-now-coldgl       first5=[222, 260] slowest=260 (#2, rt 171)
   full base-2-now-coldgl      first5=[191, 357, 449, 637, 485] slowest=637 (#4, rt 201)
   full base-3-now-coldgl      first5=[534, 450, 448, 835, 685] slowest=835 (#4, rt 204)
   full new-1-1-wait           first5=[148, 303, 418, 1164, 1199] slowest=1200 (#5, rt 269)
   full new-1-2-wait           first5=[289, 464, 526, 626, 1041] slowest=1044 (#6, rt 253)
   full new-1-3-wait           first5=[255, 395, 205, 397, 611] slowest=935 (#6, rt 604)
   full new-1-4-wait           first5=[191, 252, 473, 376, 659] slowest=774 (#6, rt 372)
   full new-1-5-wait           first5=[197, 350, 500, 489, 723] slowest=826 (#6, rt 263)
   full new-4-1-wait           first5=[296, 355, 424, 615, 798] slowest=798 (#5, rt 366)
   full new-4-2-wait           first5=[408, 377, 616, 373, 638] slowest=806 (#6, rt 217)
   full new-4-3-wait           first5=[262, 378, 383, 851, 880] slowest=880 (#5, rt 424)
   full new-4-4-wait           first5=[235, 388, 373, 656, 752] slowest=916 (#7, rt 224)
   full new-4-5-wait           first5=[410, 447, 533, 745, 749] slowest=748 (#5, rt 328)
   full base-2-1-wait          first5=[374, 343, 393, 383, 700] slowest=769 (#6, rt 363)
   full base-2-2-wait          first5=[216, 368, 391, 634, 827] slowest=827 (#5, rt 337)
   full base-2-3-wait          first5=[457, 484, 469, 725, 1122] slowest=1122 (#5, rt 347)
   full base-2-4-wait          first5=[200, 326, 504, 768, 661] slowest=768 (#4, rt 381)
   full base-2-5-wait          first5=[320, 362, 389, 847, 880] slowest=880 (#5, rt 266)
   full base-3-1-wait          first5=[234, 322, 373, 713, 822] slowest=822 (#5, rt 444)
   full base-3-2-wait          first5=[217, 442, 577, 714, 700] slowest=714 (#4, rt 370)
   full base-3-3-wait          first5=[242, 312, 469, 385, 635] slowest=799 (#6, rt 407)
   full base-3-4-wait          first5=[443, 396, 428, 750, 825] slowest=825 (#5, rt 341)
   full base-3-5-wait          first5=[314, 448, 403, 736, 1087] slowest=1087 (#5, rt 362)
SHADER DIAG card35: diagnostics log entries with shevitsa-shader/foil-shader: 0 (of 0); logcat error lines naming them: 0
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 9 | Първи кадър: медиана 2955 мс, последен 2687 мс | Пълно показване: медиана 3913 мс, последно 3760 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 9" text="Първи кадър: медиана 2955 мс, последен 2687 мс" text="Пълно показване: медиана 3913 мс, последно 3760 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5440', '8382', '8501', '8653', '8782', '8917', '9035', '9158', '9286', '9561', '10504', '10634', '10749', '10868', '10983', '11147', '11268', '11425', '11834', '12580', '12693', '12809', '12923', '13053', '13174', '13295', '13494', '13725', '14508', '14621', '14741', '14862', '14976', '15093', '15221', '15340']): 271 in 13 groups
      36  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      36  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      36  W/Parcel: Expecting binder but got null!
      36  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      36  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      36  W/Gralloc4: allocator #.x is not supported
      36  E/OpenGLRenderer: Unable to match the desired swap behavior.
       5  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       4  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       4  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object g#.s(java.lang.Object)(r
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#c#-d#-#ad-aedf-#cc#dd#ef#, generation=#)
       1  W/g.chyavorec.app: Suspending all threads took: #.#ms
## COMPLETE
## logcat findings
app pids seen: ['5440', '8382', '8501', '8653', '8782', '8917', '9035', '9158', '9286', '9561', '10504', '10634', '10749', '10868', '10983', '11147', '11268', '11425', '11834', '12580', '12693', '12809', '12923', '13053', '13174', '13295', '13494', '13725', '14508', '14621', '14741', '14862', '14976', '15093', '15221', '15340']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 137
   skipped-frames events=137 max=124 total=7686
== Davey: 229
   davey events=229 max=3228ms median=1137ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## full28

```
part full28; APK: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
installed:  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: 
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Skip': text="Skip" text="Chitalishte Yavorets" text="Community Centre “Vasil Levski – 1922”" )
first-install catalogue results after 2599 ms
## F28. API 28: card added, then the fullscreen card ×3 (by the button's position), shots and frames at rest
CARD28 try 1 (field 540 691, save 540 1104): text="LIBRARY CARD" text="“Vasil Levski – 1922”" text="—" text="Reader no. TEST-12345" content-desc="Barcode for card TEST-12345" text="Show card" text="You entered this number; it hasn’t been verified by the library. It is stored encrypted on this device only
CARD28: card shown
show-card at 582 1003
FULL28 1: fullscreen NO; app in front yes
FULL28 2: fullscreen NO; app in front yes
FULL28 3: fullscreen NO; app in front yes
IDLE full28-30-45s (15 s, no input): frames +0; cpu=0.2% (ticks 3 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.2%; pid 5422
FULL28 after a restart: fullscreen NO
FATAL: 0
SHADER DIAG full28: diagnostics log entries with shevitsa-shader/foil-shader: 0 (of 0); logcat error lines naming them: 0
## COMPLETE
## logcat findings
app pids seen: ['5422', '6231']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 3
   skipped-frames events=3 max=39 total=109
== Davey: 8
   davey events=8 max=1142ms median=766ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## now35

```
part now35; APK: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
installed:  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order: new base base new (each a fresh install; the card added by hand in each)
## N new-1: fresh install, card added by hand; cold start straight into the card, „Покажи картата“ 0.5 s / 1.0 s after the first frame
installed new-1: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
TRIAL new-1-1-now05: start (card shortcut) TotalTime: 3302; show tapped +1887 ms after the first frame; fullscreen reached: yes
FS new-1-1-now05-full: n=13 slowest=795.4ms (frame #5; ui 338.6 rt 315.7 issue 204.3 flags 0) first5=[462, 496, 537, 778, 795] >32ms=13 >64ms=13 >100ms=13 span=2650ms
TRIAL new-1-2-now10: start (card shortcut) TotalTime: 3291; show tapped +1833 ms after the first frame; fullscreen reached: yes
FS new-1-2-now10-full: n=12 slowest=944.1ms (frame #5; ui 495.3 rt 382.9 issue 260.7 flags 0) first5=[586, 595, 591, 809, 944] >32ms=12 >64ms=12 >100ms=12 span=2550ms
TRIAL new-1-3-now05: start (card shortcut) TotalTime: 2879; show tapped +1939 ms after the first frame; fullscreen reached: yes
FS new-1-3-now05-full: n=13 slowest=138351947.0ms (frame #13; ui 6.8 rt 138351597.8 issue 61.7 flags 0) first5=[600, 606, 544, 794, 776] >32ms=13 >64ms=13 >100ms=13 span=2700ms
TRIAL new-1-4-now10: start (card shortcut) TotalTime: 3084; show tapped +2072 ms after the first frame; fullscreen reached: yes
FS new-1-4-now10-full: n=12 slowest=945.6ms (frame #5; ui 409.7 rt 373.8 issue 257.3 flags 0) first5=[747, 712, 589, 763, 946] >32ms=12 >64ms=12 >100ms=12 span=2933ms
TRIAL new-1-5-now05-coldgl: start (card shortcut) TotalTime: 2493; show tapped +1734 ms after the first frame; fullscreen reached: yes
FS new-1-5-now05-coldgl-full: n=10 slowest=1090.9ms (frame #5; ui 318.8 rt 708.5 issue 555.1 flags 0) first5=[1053, 411, 581, 844, 1091] >32ms=10 >64ms=10 >100ms=10 span=2750ms
TRIAL new-1-6-now10-coldgl: start (card shortcut) TotalTime: 3108; show tapped +2310 ms after the first frame; fullscreen reached: yes
FS new-1-6-now10-coldgl-full: n=13 slowest=930.3ms (frame #6; ui 555.6 rt 319.5 issue 206.5 flags 0) first5=[530, 671, 893, 838, 796] >32ms=13 >64ms=13 >100ms=13 span=2783ms
TRIAL new-1-7-wait: start TotalTime: 3149; taps after the first frame: My +5657 ms, card +7720 ms, show +12258 ms; fullscreen reached: yes
FS new-1-7-wait-cardscreen: n=15 slowest=756.4ms (frame #14; ui 256.1 rt 290.4 issue 72.7 flags 0) first5=[71, 131, 263, 652, 678] >32ms=15 >64ms=15 >100ms=14 span=2517ms
FS new-1-7-wait-full: n=13 slowest=830.3ms (frame #6; ui 295.8 rt 385.8 issue 265.7 flags 0) first5=[294, 390, 496, 533, 750] >32ms=13 >64ms=13 >100ms=13 span=2467ms
## N base-2: fresh install, card added by hand; cold start straight into the card, „Покажи картата“ 0.5 s / 1.0 s after the first frame
installed base-2: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
TRIAL base-2-1-now05: start (card shortcut) TotalTime: 3764; show tapped +1962 ms after the first frame; fullscreen reached: yes
FS base-2-1-now05-full: n=11 slowest=931.8ms (frame #6; ui 416.0 rt 376.1 issue 219.3 flags 0) first5=[895, 540, 642, 866, 931] >32ms=11 >64ms=11 >100ms=11 span=2650ms
TRIAL base-2-2-now10: start (card shortcut) TotalTime: 3195; show tapped +2266 ms after the first frame; fullscreen reached: yes
FS base-2-2-now10-full: n=13 slowest=999.8ms (frame #6; ui 494.1 rt 292.9 issue 172.9 flags 0) first5=[616, 499, 659, 702, 890] >32ms=13 >64ms=13 >100ms=13 span=2850ms
TRIAL base-2-3-now05: start (card shortcut) TotalTime: 3230; show tapped +2385 ms after the first frame; fullscreen reached: yes
FS base-2-3-now05-full: n=13 slowest=1008.5ms (frame #6; ui 604.5 rt 386.3 issue 287.9 flags 0) first5=[999, 544, 843, 582, 889] >32ms=13 >64ms=13 >100ms=13 span=3233ms
TRIAL base-2-4-now10: start (card shortcut) TotalTime: 3272; show tapped +2047 ms after the first frame; fullscreen reached: yes
FS base-2-4-now10-full: n=13 slowest=72057052969.4ms (frame #13; ui 187.8 rt 72057052626.3 issue 150.6 flags 0) first5=[510, 454, 519, 654, 829] >32ms=13 >64ms=13 >100ms=13 span=2617ms
TRIAL base-2-5-now05-coldgl: start (card shortcut) TotalTime: 3092; show tapped +1904 ms after the first frame; fullscreen reached: yes
FS base-2-5-now05-coldgl-full: n=10 slowest=928.3ms (frame #5; ui 456.8 rt 460.8 issue 354.0 flags 0) first5=[877, 510, 584, 893, 928] >32ms=10 >64ms=10 >100ms=10 span=2550ms
TRIAL base-2-6-now10-coldgl: start (card shortcut) TotalTime: 3189; show tapped +1726 ms after the first frame; fullscreen reached: yes
FS base-2-6-now10-coldgl-full: n=12 slowest=852.5ms (frame #5; ui 390.6 rt 261.2 issue 160.3 flags 0) first5=[642, 424, 617, 825, 852] >32ms=12 >64ms=12 >100ms=12 span=2550ms
TRIAL base-2-7-wait: start TotalTime: 3350; taps after the first frame: My +5416 ms, card +7485 ms, show +11214 ms; fullscreen reached: yes
FS base-2-7-wait-cardscreen: n=15 slowest=72903829103.1ms (frame #15; ui 3.0 rt 72903828927.6 issue 65.8 flags 0) first5=[97, 165, 391, 749, 688] >32ms=15 >64ms=15 >100ms=14 span=2617ms
FS base-2-7-wait-full: n=13 slowest=1066.0ms (frame #5; ui 549.7 rt 399.0 issue 289.5 flags 0) first5=[272, 439, 518, 1058, 1066] >32ms=13 >64ms=13 >100ms=13 span=2333ms
## N base-3: fresh install, card added by hand; cold start straight into the card, „Покажи картата“ 0.5 s / 1.0 s after the first frame
installed base-3: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
TRIAL base-3-1-now05: start (card shortcut) TotalTime: 3370; show tapped +1996 ms after the first frame; fullscreen reached: yes
FS base-3-1-now05-full: n=13 slowest=1040.7ms (frame #5; ui 559.4 rt 272.5 issue 164.5 flags 0) first5=[501, 493, 447, 951, 1041] >32ms=13 >64ms=13 >100ms=13 span=2617ms
TRIAL base-3-2-now10: start (card shortcut) TotalTime: 3103; show tapped +2161 ms after the first frame; fullscreen reached: yes
FS base-3-2-now10-full: n=12 slowest=804.2ms (frame #5; ui 343.4 rt 257.1 issue 150.4 flags 0) first5=[510, 517, 545, 764, 804] >32ms=12 >64ms=12 >100ms=12 span=2550ms
TRIAL base-3-3-now05: start (card shortcut) TotalTime: 3674; show tapped +1611 ms after the first frame; fullscreen reached: yes
FS base-3-3-now05-full: n=13 slowest=693.1ms (frame #1; ui 404.6 rt 288.5 issue 147.4 flags 0) first5=[693, 532, 604, 607, 583] >32ms=13 >64ms=13 >100ms=13 span=2667ms
TRIAL base-3-4-now10: start (card shortcut) TotalTime: 3278; show tapped +2130 ms after the first frame; fullscreen reached: yes
FS base-3-4-now10-full: n=10 slowest=903.2ms (frame #5; ui 421.6 rt 288.2 issue 170.5 flags 0) first5=[766, 550, 630, 832, 903] >32ms=10 >64ms=10 >100ms=10 span=2117ms
TRIAL base-3-5-now05-coldgl: start (card shortcut) TotalTime: 3026; show tapped +1955 ms after the first frame; fullscreen reached: yes
FS base-3-5-now05-coldgl-full: n=11 slowest=935.4ms (frame #4; ui 399.1 rt 385.1 issue 219.8 flags 0) first5=[674, 412, 566, 935, 915] >32ms=11 >64ms=11 >100ms=11 span=2300ms
TRIAL base-3-6-now10-coldgl: start (card shortcut) TotalTime: 3467; show tapped +2146 ms after the first frame; fullscreen reached: yes
FS base-3-6-now10-coldgl-full: n=11 slowest=1072.9ms (frame #5; ui 562.7 rt 275.7 issue 155.2 flags 0) first5=[688, 453, 529, 880, 1073] >32ms=11 >64ms=11 >100ms=11 span=2450ms
TRIAL base-3-7-wait: start TotalTime: 3282; taps after the first frame: My +5321 ms, card +7397 ms, show +11763 ms; fullscreen reached: yes
FS base-3-7-wait-cardscreen: n=14 slowest=744.1ms (frame #11; ui 142.2 rt 306.5 issue 153.5 flags 0) first5=[99, 217, 425, 662, 625] >32ms=14 >64ms=14 >100ms=13 span=2233ms
FS base-3-7-wait-full: n=13 slowest=901.7ms (frame #6; ui 331.8 rt 403.7 issue 258.7 flags 0) first5=[227, 360, 643, 483, 631] >32ms=13 >64ms=13 >100ms=13 span=2517ms
## N new-4: fresh install, card added by hand; cold start straight into the card, „Покажи картата“ 0.5 s / 1.0 s after the first frame
installed new-4: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
COORDS: My 540 2274, card tile 258 1182, show-card 582 1068
COORDS: fullscreen card reached
TRIAL new-4-1-now05: start (card shortcut) TotalTime: 2957; show tapped +1688 ms after the first frame; fullscreen reached: yes
FS new-4-1-now05-full: n=11 slowest=891.0ms (frame #1; ui 624.6 rt 266.4 issue 95.8 flags 0) first5=[891, 437, 727, 846, 870] >32ms=11 >64ms=11 >100ms=11 span=2450ms
TRIAL new-4-2-now10: start (card shortcut) TotalTime: 3296; show tapped +2039 ms after the first frame; fullscreen reached: yes
FS new-4-2-now10-full: n=13 slowest=72056589936.1ms (frame #13; ui 197.9 rt 72056589535.3 issue 140.9 flags 0) first5=[578, 616, 572, 822, 873] >32ms=13 >64ms=13 >100ms=13 span=2817ms
TRIAL new-4-3-now05: start (card shortcut) TotalTime: 3189; show tapped +1696 ms after the first frame; fullscreen reached: yes
FS new-4-3-now05-full: n=13 slowest=961.2ms (frame #6; ui 518.8 rt 386.5 issue 272.5 flags 0) first5=[760, 417, 695, 913, 858] >32ms=13 >64ms=13 >100ms=13 span=3050ms
TRIAL new-4-4-now10: start (card shortcut) TotalTime: 3164; show tapped +2030 ms after the first frame; fullscreen reached: yes
FS new-4-4-now10-full: n=12 slowest=759.3ms (frame #5; ui 344.9 rt 250.9 issue 182.6 flags 0) first5=[605, 587, 519, 692, 759] >32ms=12 >64ms=12 >100ms=12 span=2500ms
TRIAL new-4-5-now05-coldgl: start (card shortcut) TotalTime: 2950; show tapped +1664 ms after the first frame; fullscreen reached: yes
FS new-4-5-now05-coldgl-full: n=10 slowest=950.8ms (frame #4; ui 479.2 rt 272.0 issue 96.3 flags 0) first5=[823, 438, 845, 951, 921] >32ms=10 >64ms=10 >100ms=10 span=2450ms
TRIAL new-4-6-now10-coldgl: start (card shortcut) TotalTime: 3281; show tapped +1790 ms after the first frame; fullscreen reached: yes
FS new-4-6-now10-coldgl-full: n=11 slowest=959.7ms (frame #4; ui 544.4 rt 394.7 issue 283.7 flags 0) first5=[690, 561, 783, 960, 710] >32ms=11 >64ms=11 >100ms=11 span=2467ms
TRIAL new-4-7-wait: start TotalTime: 2961; taps after the first frame: My +5570 ms, card +7634 ms, show +11597 ms; fullscreen reached: yes
FS new-4-7-wait-cardscreen: n=15 slowest=767.4ms (frame #14; ui 265.0 rt 294.3 issue 138.1 flags 0) first5=[74, 127, 310, 650, 170] >32ms=15 >64ms=15 >100ms=14 span=2333ms
FS new-4-7-wait-full: n=12 slowest=833.2ms (frame #5; ui 316.1 rt 363.6 issue 252.7 flags 0) first5=[298, 461, 429, 717, 833] >32ms=12 >64ms=12 >100ms=12 span=2267ms
## N totals
FSAGG full       now05       new  opens= 4  slowest frame: median  926.1 ms, mean 34588648.6, min  795.4, max 138351947.0  | frame 1 median  680.0 ms | frames 2-5 median 751.5 ms  | slowest per open: [795, 138351947, 891, 961] at frame # [5, 13, 1, 6]
FSAGG full       now05       base opens= 4  slowest frame: median  970.1 ms, mean  918.5, min  693.1, max 1040.7  | frame 1 median  794.0 ms | frames 2-5 median 605.5 ms  | slowest per open: [932, 1008, 1041, 693] at frame # [6, 6, 5, 1]
FSAGG full       now05-coldgl new  opens= 2  slowest frame: median 1020.9 ms, mean 1020.9, min  950.8, max 1090.9  | frame 1 median  938.0 ms | frames 2-5 median 844.5 ms  | slowest per open: [1091, 951] at frame # [5, 4]
FSAGG full       now05-coldgl base opens= 2  slowest frame: median  931.8 ms, mean  931.8, min  928.3, max  935.4  | frame 1 median  775.5 ms | frames 2-5 median 738.5 ms  | slowest per open: [928, 935] at frame # [5, 4]
FSAGG full       now10       new  opens= 4  slowest frame: median  944.9 ms, mean 18014148146.3, min  759.3, max 72056589936.1  | frame 1 median  595.5 ms | frames 2-5 median 702.0 ms  | slowest per open: [944, 946, 72056589936, 759] at frame # [5, 5, 13, 5]
FSAGG full       now10       base opens= 4  slowest frame: median  951.5 ms, mean 18014263919.1, min  804.2, max 72057052969.4  | frame 1 median  563.0 ms | frames 2-5 median 656.5 ms  | slowest per open: [1000, 72057052969, 804, 903] at frame # [6, 13, 5, 5]
FSAGG full       now10-coldgl new  opens= 2  slowest frame: median  945.0 ms, mean  945.0, min  930.3, max  959.7  | frame 1 median  610.0 ms | frames 2-5 median 789.5 ms  | slowest per open: [930, 960] at frame # [6, 4]
FSAGG full       now10-coldgl base opens= 2  slowest frame: median  962.7 ms, mean  962.7, min  852.5, max 1072.9  | frame 1 median  665.0 ms | frames 2-5 median 721.0 ms  | slowest per open: [852, 1073] at frame # [5, 5]
FSAGG full       wait5s      new  opens= 2  slowest frame: median  831.8 ms, mean  831.8, min  830.3, max  833.2  | frame 1 median  296.0 ms | frames 2-5 median 514.5 ms  | slowest per open: [830, 833] at frame # [6, 5]
FSAGG full       wait5s      base opens= 2  slowest frame: median  983.9 ms, mean  983.9, min  901.7, max 1066.0  | frame 1 median  249.5 ms | frames 2-5 median 574.5 ms  | slowest per open: [1066, 902] at frame # [5, 6]
FSAGG cardscreen wait5s      new  opens= 2  slowest frame: median  761.9 ms, mean  761.9, min  756.4, max  767.4  | frame 1 median   72.5 ms | frames 2-5 median 286.5 ms  | slowest per open: [756, 767] at frame # [14, 14]
FSAGG cardscreen wait5s      base opens= 2  slowest frame: median 36451914923.6 ms, mean 36451914923.6, min  744.1, max 72903829103.1  | frame 1 median   98.0 ms | frames 2-5 median 525.0 ms  | slowest per open: [72903829103, 744] at frame # [15, 11]
FSAGG first-5 frames per open (ms):
   full new-1-1-now05          first5=[462, 496, 537, 778, 795] slowest=795 (#5, rt 316)
   full new-1-3-now05          first5=[600, 606, 544, 794, 776] slowest=138351947 (#13, rt 138351598)
   full new-4-1-now05          first5=[891, 437, 727, 846, 870] slowest=891 (#1, rt 266)
   full new-4-3-now05          first5=[760, 417, 695, 913, 858] slowest=961 (#6, rt 386)
   full base-2-1-now05         first5=[895, 540, 642, 866, 931] slowest=932 (#6, rt 376)
   full base-2-3-now05         first5=[999, 544, 843, 582, 889] slowest=1008 (#6, rt 386)
   full base-3-1-now05         first5=[501, 493, 447, 951, 1041] slowest=1041 (#5, rt 272)
   full base-3-3-now05         first5=[693, 532, 604, 607, 583] slowest=693 (#1, rt 288)
   full new-1-5-now05-coldgl   first5=[1053, 411, 581, 844, 1091] slowest=1091 (#5, rt 708)
   full new-4-5-now05-coldgl   first5=[823, 438, 845, 951, 921] slowest=951 (#4, rt 272)
   full base-2-5-now05-coldgl  first5=[877, 510, 584, 893, 928] slowest=928 (#5, rt 461)
   full base-3-5-now05-coldgl  first5=[674, 412, 566, 935, 915] slowest=935 (#4, rt 385)
   full new-1-2-now10          first5=[586, 595, 591, 809, 944] slowest=944 (#5, rt 383)
   full new-1-4-now10          first5=[747, 712, 589, 763, 946] slowest=946 (#5, rt 374)
   full new-4-2-now10          first5=[578, 616, 572, 822, 873] slowest=72056589936 (#13, rt 72056589535)
   full new-4-4-now10          first5=[605, 587, 519, 692, 759] slowest=759 (#5, rt 251)
   full base-2-2-now10         first5=[616, 499, 659, 702, 890] slowest=1000 (#6, rt 293)
   full base-2-4-now10         first5=[510, 454, 519, 654, 829] slowest=72057052969 (#13, rt 72057052626)
   full base-3-2-now10         first5=[510, 517, 545, 764, 804] slowest=804 (#5, rt 257)
   full base-3-4-now10         first5=[766, 550, 630, 832, 903] slowest=903 (#5, rt 288)
   full new-1-6-now10-coldgl   first5=[530, 671, 893, 838, 796] slowest=930 (#6, rt 320)
   full new-4-6-now10-coldgl   first5=[690, 561, 783, 960, 710] slowest=960 (#4, rt 395)
   full base-2-6-now10-coldgl  first5=[642, 424, 617, 825, 852] slowest=852 (#5, rt 261)
   full base-3-6-now10-coldgl  first5=[688, 453, 529, 880, 1073] slowest=1073 (#5, rt 276)
   full new-1-7-wait           first5=[294, 390, 496, 533, 750] slowest=830 (#6, rt 386)
   full new-4-7-wait           first5=[298, 461, 429, 717, 833] slowest=833 (#5, rt 364)
   full base-2-7-wait          first5=[272, 439, 518, 1058, 1066] slowest=1066 (#5, rt 399)
   full base-3-7-wait          first5=[227, 360, 643, 483, 631] slowest=902 (#6, rt 404)
SHADER DIAG now35: diagnostics log entries with shevitsa-shader/foil-shader: 0 (of 0); logcat error lines naming them: 0
## COMPLETE
## logcat findings
app pids seen: ['5595', '8275', '8382', '8479', '8599', '8704', '8835', '8934', '9188', '10079', '10175', '10275', '10371', '10473', '10591', '10691', '10942', '11654', '11753', '11850', '11945', '12045', '12145', '12241', '12673', '13403', '13575', '13717', '13823', '13939', '14040', '14138']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 153
   skipped-frames events=153 max=111 total=8193
== Davey: 245
   davey events=245 max=3308ms median=1115ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## old28s

```
part old28s; APK: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450 bytes
host: 4 cpu, 15983 MB RAM
android 9 sdk 28; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
installed:  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: 
camera permission: 
screen 1080x2400
HELPER installed: package:org.pc.helper
DEVICE locale -> bg-BG: data="LOCALE ok bg-BG"; config now: config: mcc310-mnc260-bg-rBG-ldltr-sw411dp-w411dp-h842dp-normal-long-notround-lowdr-nowidecg-port-notnight-420dpi-finger-keysexposed-nokeys-navhidden-nonav-2274x1080-v28
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 2371 ms
## O1. Home ribbon on API 28 (no AGSL): the still tile
HOME28 ribbon [0 215 1080 333]: pattern mean=209.3 std=65.0 thread-px=28.5%; change 1→5→20 s: 0.26 0.25
IDLE home28-20-35s (15 s, no input): frames +25; cpu=11.3% (ticks 170 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=6.5%, Jit thread pool=3.5%, g.chyavorec.app=1.2%, FrameMetricsAgg=0.1%, DefaultExecutor=0.1%; pid 6524
## O2. Card added (API 28), card screen and fullscreen card: the tile, no crash
CARD28 try 1 (field 540 691, save 540 1104): text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотеката. Пази се шифрован само на това устрой
CARD28: card shown
CARD28 screen: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" text="Номерът е въведен от теб и не е проверен от библиотека
IDLE card28-3-18s (15 s, no input): frames +0; cpu=5.5% (ticks 83 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=4.5%, DefaultExecutor=1.0%, Profile Saver=0.1%; pid 6524
CARD28 fullscreen: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" ; app in front yes
IDLE cardfull28-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 6524
FATAL so far: 0
## O3. Monkey 2000 (seed 1357)
## W. First launch and warm-up
first-install catalogue results after 2082 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=7275
MONKEY exit=0 duration=81 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '7275'; airplane=0; camera: 
MEM after-monkey (raw) PSS=85035KB  Java=14068KB  Native=41668KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=15 Activities=1
MEM after-monkey (trimmed) PSS=79653KB  Java=8720KB  Native=41676KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=?KB  Views=15 Activities=1
SHADER DIAG old28s: diagnostics log entries with shevitsa-shader/foil-shader: 0 (of 0); logcat error lines naming them: 0
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: tab bar not reached
WARN: Settings not found
APP W/E lines (pids ['5579', '6524', '7275']): 106 in 38 groups
       9  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
       8  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
       7  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, scanCode=#,
       6  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_DOWN, scanCode=#, m
       4  E/eglCodecCommon: glUtilsParamSize: unknow param #
       4  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       4  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
       4  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
       4  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_RIGHT, scanCode=#, 
       3  W/g.chyavorec.ap: Unexpected CPU variant for X# using defaults: x#_#
       3  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       3  E/eglCodecCommon: GoldfishAddressSpaceHostMemoryAllocator: ioctl_ping failed for device_type=#, ret=-#
       3  W/IInputConnectionWrapper: beginBatchEdit on inactive InputConnection
       3  W/IInputConnectionWrapper: getTextBeforeCursor on inactive InputConnection
       3  W/IInputConnectionWrapper: getTextAfterCursor on inactive InputConnection
       3  W/IInputConnectionWrapper: getSelectedText on inactive InputConnection
       3  W/IInputConnectionWrapper: endBatchEdit on inactive InputConnection
       3  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
       2  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, m
       2  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_UP, scanCode=#, met
       2  W/GooglePlayServicesUtil: Google Play services out of date for org.chyavorec.app.  Requires # but found #
       2  W/GoogleApiManager: The service for ng# is not available: kr#{statusCode=SERVICE_VERSION_UPDATE_REQUIRED, resolution=null, message
       2  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=#, metaSt
       2  W/ViewRootImpl[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanC
       2  W/ViewRootImpl[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCod
       2  W/ViewRootImpl[MainActivity]: Cancelling event due to no window focus: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, metaSt
       2  W/ViewRootImpl[MainActivity]: Dropping event due to no window focus: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[
       1  W/ViewRootImpl[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_MENU, scanCode=#, 
       1  W/ViewRootImpl[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_MULTIPLE, keyCode=KEYCODE_DPAD_RIGHT, 
## COMPLETE
## logcat findings
app pids seen: ['5579', '6524', '7275']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 11
   skipped-frames events=11 max=64 total=435
== Davey: 13
   davey events=13 max=1188ms median=802ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## reg35

```
part reg35; APK: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
installed:  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## W. First launch and warm-up
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
first-install catalogue results after 8358 ms
## R0. Card added by hand (Моето › Добави читателска карта)
ADD by hand: text="ЧИТАТЕЛСКА КАРТА" text="НЧ „Васил Левски – 1922“" text="—" text="Читателски № TEST-12345" content-desc="Баркод на карта TEST-12345" text="Покажи картата" 
## R1. Home ribbon, 1.2.35: animates right after start, stops later
IDLE home-new-a-76-91s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 7134
RIBBON new-a [0 280 1080 398]: diff 0.5→3 s 7.80; 3→6 s 8.28; 70→75 s 0.00 (0 = still); pattern at 0.5 s: mean=209.4 std=65.0 thread-px=28.4%; at 75 s: mean=209.6 std=67.0 thread-px=26.7%
IDLE home-new-b-76-91s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: DefaultDispatch=0.1%; pid 7339
RIBBON new-b [0 280 1080 398]: diff 0.5→3 s 35.41; 3→6 s 7.50; 70→75 s 0.00 (0 = still); pattern at 0.5 s: mean=247.0 std=0.0 thread-px=0.0%; at 75 s: mean=209.6 std=67.0 thread-px=26.7%
## R2. Card screen and fullscreen card at rest: 0-15 s, 30-45 s, 45-60 s after opening
IDLE card-0-15s (15 s, no input): frames +101; cpu=29.2% (ticks 438 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=28.1%, g.chyavorec.app=0.6%, Jit thread pool=0.3%, binder:7736_4=0.1%, Profile Saver=0.1%; pid 7736
SENSORS card-15s: lines with the app 0: 
IDLE card-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 7736
IDLE card-45-60s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 7736
SENSORS card-60s: lines with the app 0: 
IDLE card-full-0-15s (15 s, no input): frames +99; cpu=27.7% (ticks 416 in 15 s; 100 ticks = 1 cpu-s) threads: RenderThread=26.9%, g.chyavorec.app=0.5%, binder:7736_3=0.1%, binder:7736_1=0.1%, Jit thread pool=0.1%; pid 7736
SENSORS card-full-15s: lines with the app 0: 
IDLE card-full-30-45s (15 s, no input): frames +0; cpu=0.1% (ticks 1 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 7736
IDLE card-full-45-60s (15 s, no input): frames +0; cpu=0.1% (ticks 2 in 15 s; 100 ticks = 1 cpu-s) threads: g.chyavorec.app=0.1%; pid 7736
SENSORS card-full-60s: lines with the app 0: 
## R3. Monkey 2000 events (seed 1357)
## W. First launch and warm-up
first-install catalogue results after 3175 ms
## K. Monkey 2000 events (seed 1357, CAMERA revoked)
camera permission before monkey: 
monkey start pid=8419
MONKEY exit=0 duration=121 s; Events injected: 2000 // Monkey finished 
MONKEY markers: CRASH=0 ANR=0 FATAL=0
pid after monkey: '8419'; airplane=0; camera: 
MEM after-monkey (raw) PSS=100660KB  Java=18148KB  Native=49312KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=22)  RSS=192164KB  Views=12 Activities=1
MEM after-monkey (trimmed) PSS=93730KB  Java=12916KB  Native=43360KB  Graphics=0KB (GL=? EGL=? GfxDev=? OtherDev=20)  RSS=186544KB  Views=12 Activities=1
SHADER DIAG reg35: diagnostics log entries with shevitsa-shader/foil-shader: 0 (of 0); logcat error lines naming them: 0
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
WARN: tab bar not reached
WARN: Settings not found
APP W/E lines (pids ['5329', '7134', '7339', '7736', '8419']): 710 in 28 groups
     585  W/InputEventSender: Received 'finished' signal for unknown seq number = #
      22  E/OpenGLRenderer: Unable to match the desired swap behavior.
      19  W/Parcel: Expecting binder but got null!
      13  W/VRI[MainActivity]: Cancelling event (no window focus):KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, metaSta
      10  W/RemoteInputConnectionImpl: getSurroundingText on inactive InputConnection
       7  W/RemoteInputConnectionImpl: getTextBeforeCursor on inactive InputConnection
       6  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
       6  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
       5  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
       5  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
       5  W/Gralloc4: allocator #.x is not supported
       4  W/VRI[MainActivity]: Dropping event (no window focus):KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanCode=#, metaSta
       3  W/RemoteInputConnectionImpl: clearMetaKeyStates on inactive InputConnection
       3  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_LEFT, scanC
       3  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_LEFT, scanCod
       2  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=#.#, y[#]=#.#
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object g#.s(java.lang.Object)(r
       1  E/ImeBackDispatcher: Ime callback not found. Ignoring unregisterReceivedCallback. callbackId: #
       1  W/RemoteInputConnectionImpl: setComposingRegion on inactive InputConnection
       1  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_DPAD_CENTER, sca
       1  W/VRI[MainActivity]: Dropping event due to root view being removed: KeyEvent { action=ACTION_UP, keyCode=KEYCODE_DPAD_CENTER, scanC
       1  W/VRI[MainActivity]: Dropping event (no window focus):MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]=-#.#, y[#]=-#
       1  W/RemoteInputConnectionImpl: setComposingText on inactive InputConnection
       1  W/RemoteInputConnectionImpl: commitText on inactive InputConnection
       1  W/AutofillManager: Session # could not be restored
       1  W/g.chyavorec.app: Cleared Reference was only reachable from finalizer (only reported once)
       1  W/VRI[MainActivity]: Dropping event due to root view being removed: MotionEvent { action=ACTION_MOVE, actionButton=#, id[#]=#, x[#]
       1  W/InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
## R4. Home ribbon on 1.2.34 for comparison
installed base: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
IDLE home-base-a-76-91s (15 s, no input): frames +0; cpu=0.0% (ticks 0 in 15 s; 100 ticks = 1 cpu-s) threads: none; pid 10094
RIBBON base-a [0 280 1080 398]: diff 0.5→3 s 4.93; 3→6 s 8.03; 70→75 s 0.00 (0 = still); pattern at 0.5 s: mean=210.2 std=65.7 thread-px=26.8%; at 75 s: mean=209.6 std=67.0 thread-px=26.7%
## COMPLETE
## logcat findings
app pids seen: ['5329', '7134', '7339', '7736', '8419', '9851', '10094']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 47
   skipped-frames events=47 max=117 total=2333
== Davey: 129
   davey events=129 max=3130ms median=911ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## start35

```
part start35; APK: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450 bytes
host: 4 cpu, 15983 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
installed:  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
block order (ABBA, each a fresh install): base new new base base new new base
## S base-1: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-1: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-1 catalogue after 4810 ms
base-1 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-1: n=5 min=2374ms median=3196ms max=3481ms
## S new-2: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-2: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-2 catalogue after 3187 ms
new-2 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-2: n=5 min=2079ms median=2406ms max=2559ms
## S new-3: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-3: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-3 catalogue after 2971 ms
new-3 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-3: n=5 min=2062ms median=2335ms max=2532ms
## S base-4: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-4: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-4 catalogue after 2987 ms
base-4 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-4: n=5 min=1985ms median=2288ms max=2370ms
## S base-5: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-5: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-5 catalogue after 3261 ms
base-5 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-5: n=5 min=1992ms median=2346ms max=2810ms
## S new-6: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-6: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-6 catalogue after 3189 ms
new-6 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-6: n=5 min=1957ms median=2407ms max=2795ms
## S new-7: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-7: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-7 catalogue after 2653 ms
new-7 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-7: n=5 min=2191ms median=2336ms max=2758ms
## S base-8: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-8: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-8 catalogue after 2898 ms
base-8 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-8: n=5 min=1936ms median=2019ms max=2582ms
## S totals (n=20 per app)
START cold new           n=20 min=1957ms median=2356ms max=2795ms  all=[2406, 2079, 2559, 2419, 2238, 2062, 2532, 2240, 2335, 2376, 2407, 1957, 2795, 2601, 2103, 2316, 2758, 2336, 2191, 2594]  states=['COLD']
DRAWN new: TotalTime n=20 min=1957ms median=2356ms max=2795ms; fully drawn n=20 median=3087ms min=2431 max=3674 all=[3221, 2431, 3530, 2888, 2453, 2586, 3298, 3215, 2620, 3647, 2864, 2632, 3122, 3674, 2615, 3535, 3539, 3052, 2434, 3446]; without line: 0
START cold base          n=20 min=1936ms median=2346ms max=3481ms  all=[3392, 3481, 2374, 3097, 3196, 2346, 1985, 2088, 2370, 2288, 1992, 2036, 2358, 2810, 2346, 2108, 2019, 2582, 2003, 1936]  states=['COLD']
DRAWN base: TotalTime n=20 min=1936ms median=2346ms max=3481ms; fully drawn n=20 median=2975ms min=2263 max=4552 all=[4207, 4552, 3735, 4082, 3797, 2970, 2625, 2512, 2889, 2553, 2263, 2351, 2850, 3059, 3706, 2980, 2465, 3585, 2997, 2273]; without line: 0
AB TotalTime  : 1.2.35 n=20 median 2356 ms (mean 2365) | 1.2.34 n=20 median 2346 ms (mean 2440) | diff of medians +10 ms (+0.4%) | Mann-Whitney p=0.68
AB Fully drawn: 1.2.35 n=20 median 3087 ms (mean 3040) | 1.2.34 n=20 median 2975 ms (mean 3123) | diff of medians +112 ms (+3.8%) | Mann-Whitney p=0.87
SHADER DIAG start35: diagnostics log entries with shevitsa-shader/foil-shader: 0 (of 0); logcat error lines naming them: 0
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 8 | Първи кадър: медиана 1857 мс, последен 1816 мс | Пълно показване: медиана 2749 мс, последно 2158 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 8" text="Първи кадър: медиана 1857 мс, последен 1816 мс" text="Пълно показване: медиана 2749 мс, последно 2158 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['6005', '7299', '7509', '7601', '7771', '7898', '8063', '8252', '8516', '8885', '8966', '9028', '9096', '9157', '9244', '9303', '9482', '9890', '10028', '10088', '10150', '10210', '10272', '10335', '10514', '10841', '10902', '10961', '11024', '11083', '11141', '11200', '11396', '11693', '11798', '11859', '11916', '11973', '12031', '12091', '12386', '12692', '12753', '12813', '12873', '12933', '12995', '13059', '13240', '13553', '13614', '13674', '13735', '13795', '13853', '13918', '14106', '14531', '14589', '14648', '14713', '14771', '14831', '14890']): 457 in 15 groups
      64  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      64  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      64  W/Parcel: Expecting binder but got null!
      64  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      64  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      64  W/Gralloc4: allocator #.x is not supported
      64  E/OpenGLRenderer: Unable to match the desired swap behavior.
       2  W/g.chyavorec.app: Suspending all threads took: #.#ms
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#badfe-ed#-#d#-#a#-#d#b#ab, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=d#b#d-#a-#db-#-#bb#e#b, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#a#-aeae-#a#-bc#c-#f#bcde#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#d#aa#-d#ae-#dc#-#-#ad#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#b#d#-#-#c#-b#-bf#bf#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=b#d#d#c#-#f#-#c#-#-#e#e#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#-#e#-#a#-#bee-#dea#cf#b#, generation=#)
## COMPLETE
## logcat findings
app pids seen: ['6005', '7299', '7509', '7601', '7771', '7898', '8063', '8252', '8516', '8885', '8966', '9028', '9096', '9157', '9244', '9303', '9482', '9890', '10028', '10088', '10150', '10210', '10272', '10335', '10514', '10841', '10902', '10961', '11024', '11083', '11141', '11200', '11396', '11693', '11798', '11859', '11916', '11973', '12031', '12091', '12386', '12692', '12753', '12813', '12873', '12933', '12995', '13059', '13240', '13553', '13614', '13674', '13735', '13795', '13853', '13918', '14106', '14531', '14589', '14648', '14713', '14771', '14831', '14890']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 179
   skipped-frames events=179 max=136 total=8501
== Davey: 308
   davey events=308 max=2664ms median=1080ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## start35b

```
part start35b; APK: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
base APK: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450 bytes
host: 4 cpu, 15982 MB RAM
android 13 sdk 33; Physical size: 1080x2400; Physical density: 420; adb uid 0
RELEASE APK: rel-133.apk missing — built x86_64 app.apk
APK for this part: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682 bytes
installed:  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
app locale: Locales for org.chyavorec.app for user 0 are [bg-BG]
camera permission: 
screen 1080x2400
## S0. Emulator warm-up block (not counted): both apps, 2 installs × 5 cold starts
installed warm-base: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
installed warm-new: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
block order (each a fresh install): new base base new new base base new
## S new-1: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-1: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-1 catalogue after 3117 ms
new-1 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-1: n=5 min=2910ms median=3232ms max=3549ms
## S base-2: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-2: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-2 catalogue after 3474 ms
base-2 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-2: n=5 min=3192ms median=3544ms max=3820ms
## S base-3: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-3: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-3 catalogue after 3033 ms
base-3 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-3: n=5 min=3117ms median=3256ms max=3562ms
## S new-4: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-4: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-4 catalogue after 3186 ms
new-4 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-4: n=5 min=2848ms median=3090ms max=3822ms
## S new-5: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-5: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-5 catalogue after 3004 ms
new-5 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-5: n=5 min=2935ms median=3281ms max=3477ms
## S base-6: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-6: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-6 catalogue after 2882 ms
base-6 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-6: n=5 min=2885ms median=3449ms max=3501ms
## S base-7: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed base-7: 1.2.34 3812d8e built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504705 size 4316450;  versionCode=1504705 minSdk=26 targetSdk=37  versionName=1.2.34 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
base-7 catalogue after 3451 ms
base-7 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
base-7: n=5 min=2821ms median=3254ms max=3435ms
## S new-8: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)
installed new-8: 1.2.35 9295534 built prodRelease (minified), x86_64 added (throwaway key); versionCode 1504706 size 4318682;  versionCode=1504706 minSdk=26 targetSdk=37  versionName=1.2.35 
(dismissed '=Пропусни': text="Пропусни" text="Читалище Яворец" text="НЧ „Васил Левски – 1922“" )
new-8 catalogue after 3198 ms
new-8 dexopt: x86_64: [status=verify] x86_64: [status=verify] 
new-8: n=5 min=2872ms median=3044ms max=3121ms
## S totals (n=20 per app)
START cold new           n=20 min=2848ms median=3143ms max=3822ms  all=[3282, 3165, 2910, 3549, 3232, 3822, 3090, 3451, 2848, 3058, 3248, 3438, 3281, 3477, 2935, 2997, 3095, 3121, 3044, 2872]  states=['COLD']
DRAWN new: TotalTime n=20 min=2848ms median=3143ms max=3822ms; fully drawn n=20 median=4022ms min=3447 max=4750 all=[4487, 4184, 4254, 4060, 4251, 4537, 3447, 4750, 4281, 3782, 3985, 3711, 3981, 4136, 3972, 4108, 3488, 3567, 3735, 3710]; without line: 0
START cold base          n=20 min=2821ms median=3268ms max=3820ms  all=[3820, 3544, 3225, 3192, 3750, 3287, 3117, 3256, 3562, 3143, 3449, 3269, 3501, 2885, 3463, 3254, 3435, 3267, 2821, 2907]  states=['COLD']
DRAWN base: TotalTime n=20 min=2821ms median=3268ms max=3820ms; fully drawn n=20 median=4256ms min=3673 max=4798 all=[4273, 4242, 4528, 3673, 4406, 4031, 3811, 4536, 4586, 4020, 4547, 4028, 4798, 4314, 4185, 4034, 4186, 4471, 3893, 4271]; without line: 0
AB TotalTime  : 1.2.35 n=20 median 3143 ms (mean 3196) | 1.2.34 n=20 median 3268 ms (mean 3307) | diff of medians -125 ms (-3.8%) | Mann-Whitney p=0.15
AB Fully drawn: 1.2.35 n=20 median 4022 ms (mean 4021) | 1.2.34 n=20 median 4256 ms (mean 4242) | diff of medians -234 ms (-5.5%) | Mann-Whitney p=0.04
SHADER DIAG start35: diagnostics log entries with shevitsa-shader/foil-shader: 0 (of 0); logcat error lines naming them: 0
## Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat
DIAGLOG: empty (no entries)
DIAG SCREEN: text="Няма записани грешки."  Бързодействие | Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни. | Студен старт · 8 | Първи кадър: медиана 2682 мс, последен 2547 мс | Пълно показване: медиана 3388 мс, последно 3385 мс | Няма записани грешки. | Диагностика
DIAG SCREEN (scrolled): text="Тук приложението записва техническите грешки (време, място в кода и вид на грешката), за да могат да се открият и поправят. Без лични данни: номерата, ПИН-овете, имената и адресите се премахват. Дневникът остава само на устройството — напуска го единствено ако решиш да го споделиш." text="Сподели" text="Изчисти" text="Бързодействие" text="Времената на стартиране и плавността на екраните, измерени на това устройство — само имената на екраните, без лични данни." text="Студен старт · 8" text="Първи кадър: медиана 2682 мс, последен 2547 мс" text="Пълно показване: медиана 3388 мс, последно 3385 мс" text="Няма записани грешки." content-desc="Назад" text="Диагностика" 
APP W/E lines (pids ['5115', '5710', '5880', '6046', '6532', '6753', '7022', '7827', '7922', '8055', '8221', '8416', '8642', '8989', '9045', '9108', '9165', '9224', '9285', '9349', '9520', '9850', '9909', '10094', '10213', '10319', '10376', '10431', '10597', '10904', '11020', '11079', '11134', '11195', '11252', '11310', '11581', '11900', '11965', '12023', '12082', '12140', '12201', '12257', '12439', '12734', '12792', '12848', '12906', '13016', '13076', '13132', '13303', '13665', '13720', '13775', '13832', '13892', '13951', '14006', '14183', '14484', '14540', '14635', '14691', '14751', '14807', '14864', '15031', '15316', '15374', '15432', '15506', '15565', '15622', '15680']): 539 in 14 groups
      76  W/g.chyavorec.app: Unexpected CPU variant for x#: x#_#.
      76  W/g.chyavorec.app: Known variants: atom, sandybridge, silvermont, kabylake, default
      76  W/Parcel: Expecting binder but got null!
      76  W/OpenGLRenderer: Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...
      76  W/OpenGLRenderer: Failed to initialize #-# format, error = EGL_SUCCESS
      76  W/Gralloc4: allocator #.x is not supported
      76  E/OpenGLRenderer: Unable to match the desired swap behavior.
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#e#fcf-dc#-#eaa-bc#-#bdedbe#a#b, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#e#e-#b#-#cb-#e#a-abeee#e#, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=#d#a#e-a#c-#-#fb-#bbcdc#e, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at java.lang.Object g#.s(java.lang.Object)(r
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=d#c#de-#e#-#be-#ccf-#fb#e#cf, generation=#)
       1  W/WM-Processor: Didn't find WorkSpec for id WorkGenerationalId(workSpecId=f#-#-#ab#-bccf-#b#c#eadc#, generation=#)
       1  W/g.chyavorec.app: Long monitor contention with owner DefaultDispatcher-worker-# (#) at jp# yj#.a(java.lang.Object, is#, jb#)(r#-
## COMPLETE
## logcat findings
app pids seen: ['5115', '5710', '5880', '6046', '6532', '6753', '7022', '7827', '7922', '8055', '8221', '8416', '8642', '8989', '9045', '9108', '9165', '9224', '9285', '9349', '9520', '9850', '9909', '10094', '10213', '10319', '10376', '10431', '10597', '10904', '11020', '11079', '11134', '11195', '11252', '11310', '11581', '11900', '11965', '12023', '12082', '12140', '12201', '12257', '12439', '12734', '12792', '12848', '12906', '13016', '13076', '13132', '13303', '13665', '13720', '13775', '13832', '13892', '13951', '14006', '14183', '14484', '14540', '14635', '14691', '14751', '14807', '14864', '15031', '15316', '15374', '15432', '15506', '15565', '15622', '15680']
== FATAL: 0
== ANR: 0
== StrictMode: 0
== Skipped: 284
   skipped-frames events=284 max=154 total=16053
== Davey: 422
   davey events=422 max=3164ms median=1212ms
== NativeLink: 0
== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)
```

## Files

- [card35/app-we.txt](card35/app-we.txt)
- [card35/debug/1-fs-new-1-now.xml](card35/debug/1-fs-new-1-now.xml)
- [card35/debug/2-fs-new-1-now-coldgl.xml](card35/debug/2-fs-new-1-now-coldgl.xml)
- [card35/debug/3-fs-base-2-now.xml](card35/debug/3-fs-base-2-now.xml)
- [card35/debug/4-fs-base-2-now-coldgl.xml](card35/debug/4-fs-base-2-now-coldgl.xml)
- [card35/debug/5-fs-base-3-now.xml](card35/debug/5-fs-base-3-now.xml)
- [card35/debug/6-fs-base-3-now-coldgl.xml](card35/debug/6-fs-base-3-now-coldgl.xml)
- [card35/debug/7-fs-new-4-now.xml](card35/debug/7-fs-new-4-now.xml)
- [card35/debug/8-fs-new-4-now-coldgl.xml](card35/debug/8-fs-new-4-now-coldgl.xml)
- [card35/diag-card35.jsonl](card35/diag-card35.jsonl)
- [card35/diag-log.jsonl](card35/diag-log.jsonl)
- [card35/diag-screen.xml](card35/diag-screen.xml)
- [card35/fs-agg.txt](card35/fs-agg.txt)
- [card35/fs/base-2-1-wait-card.txt](card35/fs/base-2-1-wait-card.txt)
- [card35/fs/base-2-1-wait-full.txt](card35/fs/base-2-1-wait-full.txt)
- [card35/fs/base-2-2-wait-card.txt](card35/fs/base-2-2-wait-card.txt)
- [card35/fs/base-2-2-wait-full.txt](card35/fs/base-2-2-wait-full.txt)
- [card35/fs/base-2-3-wait-card.txt](card35/fs/base-2-3-wait-card.txt)
- [card35/fs/base-2-3-wait-full.txt](card35/fs/base-2-3-wait-full.txt)
- [card35/fs/base-2-4-wait-card.txt](card35/fs/base-2-4-wait-card.txt)
- [card35/fs/base-2-4-wait-full.txt](card35/fs/base-2-4-wait-full.txt)
- [card35/fs/base-2-5-wait-card.txt](card35/fs/base-2-5-wait-card.txt)
- [card35/fs/base-2-5-wait-full.txt](card35/fs/base-2-5-wait-full.txt)
- [card35/fs/base-2-coldgl-card.txt](card35/fs/base-2-coldgl-card.txt)
- [card35/fs/base-2-coldgl-full.txt](card35/fs/base-2-coldgl-full.txt)
- [card35/fs/base-2-now-coldgl-full.txt](card35/fs/base-2-now-coldgl-full.txt)
- [card35/fs/base-2-now-full.txt](card35/fs/base-2-now-full.txt)
- [card35/fs/base-3-1-wait-card.txt](card35/fs/base-3-1-wait-card.txt)
- [card35/fs/base-3-1-wait-full.txt](card35/fs/base-3-1-wait-full.txt)
- [card35/fs/base-3-2-wait-card.txt](card35/fs/base-3-2-wait-card.txt)
- [card35/fs/base-3-2-wait-full.txt](card35/fs/base-3-2-wait-full.txt)
- [card35/fs/base-3-3-wait-card.txt](card35/fs/base-3-3-wait-card.txt)
- [card35/fs/base-3-3-wait-full.txt](card35/fs/base-3-3-wait-full.txt)
- [card35/fs/base-3-4-wait-card.txt](card35/fs/base-3-4-wait-card.txt)
- [card35/fs/base-3-4-wait-full.txt](card35/fs/base-3-4-wait-full.txt)
- [card35/fs/base-3-5-wait-card.txt](card35/fs/base-3-5-wait-card.txt)
- [card35/fs/base-3-5-wait-full.txt](card35/fs/base-3-5-wait-full.txt)
- [card35/fs/base-3-coldgl-card.txt](card35/fs/base-3-coldgl-card.txt)
- [card35/fs/base-3-coldgl-full.txt](card35/fs/base-3-coldgl-full.txt)
- [card35/fs/base-3-now-coldgl-full.txt](card35/fs/base-3-now-coldgl-full.txt)
- [card35/fs/base-3-now-full.txt](card35/fs/base-3-now-full.txt)
- [card35/fs/new-1-1-wait-card.txt](card35/fs/new-1-1-wait-card.txt)
- [card35/fs/new-1-1-wait-full.txt](card35/fs/new-1-1-wait-full.txt)
- [card35/fs/new-1-2-wait-card.txt](card35/fs/new-1-2-wait-card.txt)
- [card35/fs/new-1-2-wait-full.txt](card35/fs/new-1-2-wait-full.txt)
- [card35/fs/new-1-3-wait-card.txt](card35/fs/new-1-3-wait-card.txt)
- [card35/fs/new-1-3-wait-full.txt](card35/fs/new-1-3-wait-full.txt)
- [card35/fs/new-1-4-wait-card.txt](card35/fs/new-1-4-wait-card.txt)
- [card35/fs/new-1-4-wait-full.txt](card35/fs/new-1-4-wait-full.txt)
- [card35/fs/new-1-5-wait-card.txt](card35/fs/new-1-5-wait-card.txt)
- [card35/fs/new-1-5-wait-full.txt](card35/fs/new-1-5-wait-full.txt)
- [card35/fs/new-1-coldgl-card.txt](card35/fs/new-1-coldgl-card.txt)
- [card35/fs/new-1-coldgl-full.txt](card35/fs/new-1-coldgl-full.txt)
- [card35/fs/new-1-now-coldgl-full.txt](card35/fs/new-1-now-coldgl-full.txt)
- [card35/fs/new-1-now-full.txt](card35/fs/new-1-now-full.txt)
- [card35/fs/new-4-1-wait-card.txt](card35/fs/new-4-1-wait-card.txt)
- [card35/fs/new-4-1-wait-full.txt](card35/fs/new-4-1-wait-full.txt)
- [card35/fs/new-4-2-wait-card.txt](card35/fs/new-4-2-wait-card.txt)
- [card35/fs/new-4-2-wait-full.txt](card35/fs/new-4-2-wait-full.txt)
- [card35/fs/new-4-3-wait-card.txt](card35/fs/new-4-3-wait-card.txt)
- [card35/fs/new-4-3-wait-full.txt](card35/fs/new-4-3-wait-full.txt)
- [card35/fs/new-4-4-wait-card.txt](card35/fs/new-4-4-wait-card.txt)
- [card35/fs/new-4-4-wait-full.txt](card35/fs/new-4-4-wait-full.txt)
- [card35/fs/new-4-5-wait-card.txt](card35/fs/new-4-5-wait-card.txt)
- [card35/fs/new-4-5-wait-full.txt](card35/fs/new-4-5-wait-full.txt)
- [card35/fs/new-4-coldgl-card.txt](card35/fs/new-4-coldgl-card.txt)
- [card35/fs/new-4-coldgl-full.txt](card35/fs/new-4-coldgl-full.txt)
- [card35/fs/new-4-now-coldgl-full.txt](card35/fs/new-4-now-coldgl-full.txt)
- [card35/fs/new-4-now-full.txt](card35/fs/new-4-now-full.txt)
- [card35/job-status.txt](card35/job-status.txt)
- [card35/logcat-all.txt](card35/logcat-all.txt)
- [card35/logcat-dump.txt](card35/logcat-dump.txt)
- [card35/logcat-findings.txt](card35/logcat-findings.txt)
- [card35/logcat-live.txt](card35/logcat-live.txt)
- [card35/shots/debug/1-fs-new-1-now.png](card35/shots/debug/1-fs-new-1-now.png)
- [card35/shots/debug/2-fs-new-1-now-coldgl.png](card35/shots/debug/2-fs-new-1-now-coldgl.png)
- [card35/shots/debug/3-fs-base-2-now.png](card35/shots/debug/3-fs-base-2-now.png)
- [card35/shots/debug/4-fs-base-2-now-coldgl.png](card35/shots/debug/4-fs-base-2-now-coldgl.png)
- [card35/shots/debug/5-fs-base-3-now.png](card35/shots/debug/5-fs-base-3-now.png)
- [card35/shots/debug/6-fs-base-3-now-coldgl.png](card35/shots/debug/6-fs-base-3-now-coldgl.png)
- [card35/shots/debug/7-fs-new-4-now.png](card35/shots/debug/7-fs-new-4-now.png)
- [card35/shots/debug/8-fs-new-4-now-coldgl.png](card35/shots/debug/8-fs-new-4-now-coldgl.png)
- [card35/shots/diag/diagnostics-end-2.png](card35/shots/diag/diagnostics-end-2.png)
- [card35/shots/diag/diagnostics-end.png](card35/shots/diag/diagnostics-end.png)
- [card35/shots/eshot/fs-base-2-1-wait.png](card35/shots/eshot/fs-base-2-1-wait.png)
- [card35/shots/eshot/fs-base-2-coldgl.png](card35/shots/eshot/fs-base-2-coldgl.png)
- [card35/shots/eshot/fs-base-2-now-coldgl.png](card35/shots/eshot/fs-base-2-now-coldgl.png)
- [card35/shots/eshot/fs-base-2-now.png](card35/shots/eshot/fs-base-2-now.png)
- [card35/shots/eshot/fs-base-3-1-wait.png](card35/shots/eshot/fs-base-3-1-wait.png)
- [card35/shots/eshot/fs-base-3-coldgl.png](card35/shots/eshot/fs-base-3-coldgl.png)
- [card35/shots/eshot/fs-base-3-now-coldgl.png](card35/shots/eshot/fs-base-3-now-coldgl.png)
- [card35/shots/eshot/fs-base-3-now.png](card35/shots/eshot/fs-base-3-now.png)
- [card35/shots/eshot/fs-new-1-1-wait.png](card35/shots/eshot/fs-new-1-1-wait.png)
- [card35/shots/eshot/fs-new-1-coldgl.png](card35/shots/eshot/fs-new-1-coldgl.png)
- [card35/shots/eshot/fs-new-1-now-coldgl.png](card35/shots/eshot/fs-new-1-now-coldgl.png)
- [card35/shots/eshot/fs-new-1-now.png](card35/shots/eshot/fs-new-1-now.png)
- [card35/shots/eshot/fs-new-4-1-wait.png](card35/shots/eshot/fs-new-4-1-wait.png)
- [card35/shots/eshot/fs-new-4-coldgl.png](card35/shots/eshot/fs-new-4-coldgl.png)
- [card35/shots/eshot/fs-new-4-now-coldgl.png](card35/shots/eshot/fs-new-4-now-coldgl.png)
- [card35/shots/eshot/fs-new-4-now.png](card35/shots/eshot/fs-new-4-now.png)
- [card35/summary.txt](card35/summary.txt)
- [full28/cpu/full28-30-45s-t0.txt](full28/cpu/full28-30-45s-t0.txt)
- [full28/cpu/full28-30-45s-t1.txt](full28/cpu/full28-30-45s-t1.txt)
- [full28/diag-full28.jsonl](full28/diag-full28.jsonl)
- [full28/gfx-tmp.txt](full28/gfx-tmp.txt)
- [full28/job-status.txt](full28/job-status.txt)
- [full28/logcat-all.txt](full28/logcat-all.txt)
- [full28/logcat-dump.txt](full28/logcat-dump.txt)
- [full28/logcat-findings.txt](full28/logcat-findings.txt)
- [full28/logcat-live.txt](full28/logcat-live.txt)
- [full28/shots/eshot/c28-typed-1.png](full28/shots/eshot/c28-typed-1.png)
- [full28/shots/eshot/full28-1.png](full28/shots/eshot/full28-1.png)
- [full28/shots/eshot/full28-2.png](full28/shots/eshot/full28-2.png)
- [full28/shots/eshot/full28-3.png](full28/shots/eshot/full28-3.png)
- [full28/shots/eshot/full28-after-restart.png](full28/shots/eshot/full28-after-restart.png)
- [full28/summary.txt](full28/summary.txt)
- [now35/diag-now35.jsonl](now35/diag-now35.jsonl)
- [now35/fs-agg.txt](now35/fs-agg.txt)
- [now35/fs/base-2-1-now05-full.txt](now35/fs/base-2-1-now05-full.txt)
- [now35/fs/base-2-2-now10-full.txt](now35/fs/base-2-2-now10-full.txt)
- [now35/fs/base-2-3-now05-full.txt](now35/fs/base-2-3-now05-full.txt)
- [now35/fs/base-2-4-now10-full.txt](now35/fs/base-2-4-now10-full.txt)
- [now35/fs/base-2-5-now05-coldgl-full.txt](now35/fs/base-2-5-now05-coldgl-full.txt)
- [now35/fs/base-2-6-now10-coldgl-full.txt](now35/fs/base-2-6-now10-coldgl-full.txt)
- [now35/fs/base-2-7-wait-card.txt](now35/fs/base-2-7-wait-card.txt)
- [now35/fs/base-2-7-wait-full.txt](now35/fs/base-2-7-wait-full.txt)
- [now35/fs/base-3-1-now05-full.txt](now35/fs/base-3-1-now05-full.txt)
- [now35/fs/base-3-2-now10-full.txt](now35/fs/base-3-2-now10-full.txt)
- [now35/fs/base-3-3-now05-full.txt](now35/fs/base-3-3-now05-full.txt)
- [now35/fs/base-3-4-now10-full.txt](now35/fs/base-3-4-now10-full.txt)
- [now35/fs/base-3-5-now05-coldgl-full.txt](now35/fs/base-3-5-now05-coldgl-full.txt)
- [now35/fs/base-3-6-now10-coldgl-full.txt](now35/fs/base-3-6-now10-coldgl-full.txt)
- [now35/fs/base-3-7-wait-card.txt](now35/fs/base-3-7-wait-card.txt)
- [now35/fs/base-3-7-wait-full.txt](now35/fs/base-3-7-wait-full.txt)
- [now35/fs/new-1-1-now05-full.txt](now35/fs/new-1-1-now05-full.txt)
- [now35/fs/new-1-2-now10-full.txt](now35/fs/new-1-2-now10-full.txt)
- [now35/fs/new-1-3-now05-full.txt](now35/fs/new-1-3-now05-full.txt)
- [now35/fs/new-1-4-now10-full.txt](now35/fs/new-1-4-now10-full.txt)
- [now35/fs/new-1-5-now05-coldgl-full.txt](now35/fs/new-1-5-now05-coldgl-full.txt)
- [now35/fs/new-1-6-now10-coldgl-full.txt](now35/fs/new-1-6-now10-coldgl-full.txt)
- [now35/fs/new-1-7-wait-card.txt](now35/fs/new-1-7-wait-card.txt)
- [now35/fs/new-1-7-wait-full.txt](now35/fs/new-1-7-wait-full.txt)
- [now35/fs/new-4-1-now05-full.txt](now35/fs/new-4-1-now05-full.txt)
- [now35/fs/new-4-2-now10-full.txt](now35/fs/new-4-2-now10-full.txt)
- [now35/fs/new-4-3-now05-full.txt](now35/fs/new-4-3-now05-full.txt)
- [now35/fs/new-4-4-now10-full.txt](now35/fs/new-4-4-now10-full.txt)
- [now35/fs/new-4-5-now05-coldgl-full.txt](now35/fs/new-4-5-now05-coldgl-full.txt)
- [now35/fs/new-4-6-now10-coldgl-full.txt](now35/fs/new-4-6-now10-coldgl-full.txt)
- [now35/fs/new-4-7-wait-card.txt](now35/fs/new-4-7-wait-card.txt)
- [now35/fs/new-4-7-wait-full.txt](now35/fs/new-4-7-wait-full.txt)
- [now35/job-status.txt](now35/job-status.txt)
- [now35/logcat-all.txt](now35/logcat-all.txt)
- [now35/logcat-dump.txt](now35/logcat-dump.txt)
- [now35/logcat-findings.txt](now35/logcat-findings.txt)
- [now35/logcat-live.txt](now35/logcat-live.txt)
- [now35/summary.txt](now35/summary.txt)
- [old28s/app-we.txt](old28s/app-we.txt)
- [old28s/cpu/card28-3-18s-t0.txt](old28s/cpu/card28-3-18s-t0.txt)
- [old28s/cpu/card28-3-18s-t1.txt](old28s/cpu/card28-3-18s-t1.txt)
- [old28s/cpu/cardfull28-30-45s-t0.txt](old28s/cpu/cardfull28-30-45s-t0.txt)
- [old28s/cpu/cardfull28-30-45s-t1.txt](old28s/cpu/cardfull28-30-45s-t1.txt)
- [old28s/cpu/home28-20-35s-t0.txt](old28s/cpu/home28-20-35s-t0.txt)
- [old28s/cpu/home28-20-35s-t1.txt](old28s/cpu/home28-20-35s-t1.txt)
- [old28s/debug/1-ready.xml](old28s/debug/1-ready.xml)
- [old28s/debug/2-ready.xml](old28s/debug/2-ready.xml)
- [old28s/debug/3-ready.xml](old28s/debug/3-ready.xml)
- [old28s/debug/4-ready.xml](old28s/debug/4-ready.xml)
- [old28s/debug/5-ready.xml](old28s/debug/5-ready.xml)
- [old28s/debug/6-ready.xml](old28s/debug/6-ready.xml)
- [old28s/debug/7-ready.xml](old28s/debug/7-ready.xml)
- [old28s/debug/8-tab-more.xml](old28s/debug/8-tab-more.xml)
- [old28s/debug/9-settings.xml](old28s/debug/9-settings.xml)
- [old28s/diag-log.jsonl](old28s/diag-log.jsonl)
- [old28s/diag-old28s.jsonl](old28s/diag-old28s.jsonl)
- [old28s/gfx-tmp.txt](old28s/gfx-tmp.txt)
- [old28s/job-status.txt](old28s/job-status.txt)
- [old28s/logcat-all.txt](old28s/logcat-all.txt)
- [old28s/logcat-dump.txt](old28s/logcat-dump.txt)
- [old28s/logcat-findings.txt](old28s/logcat-findings.txt)
- [old28s/logcat-live.txt](old28s/logcat-live.txt)
- [old28s/mem-after-monkey-raw.txt](old28s/mem-after-monkey-raw.txt)
- [old28s/mem-after-monkey.txt](old28s/mem-after-monkey.txt)
- [old28s/monkey.txt](old28s/monkey.txt)
- [old28s/shots/debug/1-ready.png](old28s/shots/debug/1-ready.png)
- [old28s/shots/debug/2-ready.png](old28s/shots/debug/2-ready.png)
- [old28s/shots/debug/3-ready.png](old28s/shots/debug/3-ready.png)
- [old28s/shots/debug/4-ready.png](old28s/shots/debug/4-ready.png)
- [old28s/shots/debug/5-ready.png](old28s/shots/debug/5-ready.png)
- [old28s/shots/debug/6-ready.png](old28s/shots/debug/6-ready.png)
- [old28s/shots/debug/7-ready.png](old28s/shots/debug/7-ready.png)
- [old28s/shots/debug/8-tab-more.png](old28s/shots/debug/8-tab-more.png)
- [old28s/shots/debug/9-settings.png](old28s/shots/debug/9-settings.png)
- [old28s/shots/eshot/c28-typed-1.png](old28s/shots/eshot/c28-typed-1.png)
- [old28s/shots/eshot/card28-full.png](old28s/shots/eshot/card28-full.png)
- [old28s/shots/eshot/card28.png](old28s/shots/eshot/card28.png)
- [old28s/shots/monkey-end.png](old28s/shots/monkey-end.png)
- [old28s/shots/old28/home-01s.png](old28s/shots/old28/home-01s.png)
- [old28s/shots/old28/home-05s.png](old28s/shots/old28/home-05s.png)
- [old28s/shots/old28/home-20s.png](old28s/shots/old28/home-20s.png)
- [old28s/summary.txt](old28s/summary.txt)
- [reg35/app-we.txt](reg35/app-we.txt)
- [reg35/cpu/card-0-15s-t0.txt](reg35/cpu/card-0-15s-t0.txt)
- [reg35/cpu/card-0-15s-t1.txt](reg35/cpu/card-0-15s-t1.txt)
- [reg35/cpu/card-30-45s-t0.txt](reg35/cpu/card-30-45s-t0.txt)
- [reg35/cpu/card-30-45s-t1.txt](reg35/cpu/card-30-45s-t1.txt)
- [reg35/cpu/card-45-60s-t0.txt](reg35/cpu/card-45-60s-t0.txt)
- [reg35/cpu/card-45-60s-t1.txt](reg35/cpu/card-45-60s-t1.txt)
- [reg35/cpu/card-full-0-15s-t0.txt](reg35/cpu/card-full-0-15s-t0.txt)
- [reg35/cpu/card-full-0-15s-t1.txt](reg35/cpu/card-full-0-15s-t1.txt)
- [reg35/cpu/card-full-30-45s-t0.txt](reg35/cpu/card-full-30-45s-t0.txt)
- [reg35/cpu/card-full-30-45s-t1.txt](reg35/cpu/card-full-30-45s-t1.txt)
- [reg35/cpu/card-full-45-60s-t0.txt](reg35/cpu/card-full-45-60s-t0.txt)
- [reg35/cpu/card-full-45-60s-t1.txt](reg35/cpu/card-full-45-60s-t1.txt)
- [reg35/cpu/home-base-a-76-91s-t0.txt](reg35/cpu/home-base-a-76-91s-t0.txt)
- [reg35/cpu/home-base-a-76-91s-t1.txt](reg35/cpu/home-base-a-76-91s-t1.txt)
- [reg35/cpu/home-new-a-76-91s-t0.txt](reg35/cpu/home-new-a-76-91s-t0.txt)
- [reg35/cpu/home-new-a-76-91s-t1.txt](reg35/cpu/home-new-a-76-91s-t1.txt)
- [reg35/cpu/home-new-b-76-91s-t0.txt](reg35/cpu/home-new-b-76-91s-t0.txt)
- [reg35/cpu/home-new-b-76-91s-t1.txt](reg35/cpu/home-new-b-76-91s-t1.txt)
- [reg35/debug/1-tab-more.xml](reg35/debug/1-tab-more.xml)
- [reg35/debug/2-settings.xml](reg35/debug/2-settings.xml)
- [reg35/diag-log.jsonl](reg35/diag-log.jsonl)
- [reg35/diag-reg35.jsonl](reg35/diag-reg35.jsonl)
- [reg35/gfx-tmp.txt](reg35/gfx-tmp.txt)
- [reg35/job-status.txt](reg35/job-status.txt)
- [reg35/logcat-all.txt](reg35/logcat-all.txt)
- [reg35/logcat-dump.txt](reg35/logcat-dump.txt)
- [reg35/logcat-findings.txt](reg35/logcat-findings.txt)
- [reg35/logcat-live.txt](reg35/logcat-live.txt)
- [reg35/mem-after-monkey-raw.txt](reg35/mem-after-monkey-raw.txt)
- [reg35/mem-after-monkey.txt](reg35/mem-after-monkey.txt)
- [reg35/monkey.txt](reg35/monkey.txt)
- [reg35/rib-base-a.xml](reg35/rib-base-a.xml)
- [reg35/rib-new-a.xml](reg35/rib-new-a.xml)
- [reg35/rib-new-b.xml](reg35/rib-new-b.xml)
- [reg35/sens-card-15s.txt](reg35/sens-card-15s.txt)
- [reg35/sens-card-60s.txt](reg35/sens-card-60s.txt)
- [reg35/sens-card-full-15s.txt](reg35/sens-card-full-15s.txt)
- [reg35/sens-card-full-60s.txt](reg35/sens-card-full-60s.txt)
- [reg35/shots/debug/1-tab-more.png](reg35/shots/debug/1-tab-more.png)
- [reg35/shots/debug/2-settings.png](reg35/shots/debug/2-settings.png)
- [reg35/shots/eshot/card-full-rest.png](reg35/shots/eshot/card-full-rest.png)
- [reg35/shots/eshot/card-rest.png](reg35/shots/eshot/card-rest.png)
- [reg35/shots/monkey-end.png](reg35/shots/monkey-end.png)
- [reg35/shots/rib/base-a-00.5s.png](reg35/shots/rib/base-a-00.5s.png)
- [reg35/shots/rib/base-a-03s.png](reg35/shots/rib/base-a-03s.png)
- [reg35/shots/rib/base-a-06s.png](reg35/shots/rib/base-a-06s.png)
- [reg35/shots/rib/base-a-70s.png](reg35/shots/rib/base-a-70s.png)
- [reg35/shots/rib/base-a-75s.png](reg35/shots/rib/base-a-75s.png)
- [reg35/shots/rib/base-a-strip.png](reg35/shots/rib/base-a-strip.png)
- [reg35/shots/rib/new-a-00.5s.png](reg35/shots/rib/new-a-00.5s.png)
- [reg35/shots/rib/new-a-03s.png](reg35/shots/rib/new-a-03s.png)
- [reg35/shots/rib/new-a-06s.png](reg35/shots/rib/new-a-06s.png)
- [reg35/shots/rib/new-a-70s.png](reg35/shots/rib/new-a-70s.png)
- [reg35/shots/rib/new-a-75s.png](reg35/shots/rib/new-a-75s.png)
- [reg35/shots/rib/new-a-strip.png](reg35/shots/rib/new-a-strip.png)
- [reg35/shots/rib/new-b-00.5s.png](reg35/shots/rib/new-b-00.5s.png)
- [reg35/shots/rib/new-b-03s.png](reg35/shots/rib/new-b-03s.png)
- [reg35/shots/rib/new-b-06s.png](reg35/shots/rib/new-b-06s.png)
- [reg35/shots/rib/new-b-70s.png](reg35/shots/rib/new-b-70s.png)
- [reg35/shots/rib/new-b-75s.png](reg35/shots/rib/new-b-75s.png)
- [reg35/shots/rib/new-b-strip.png](reg35/shots/rib/new-b-strip.png)
- [reg35/summary.txt](reg35/summary.txt)
- [runs.txt](runs.txt)
- [size/apk-base-ref.txt](size/apk-base-ref.txt)
- [size/apk-ref.txt](size/apk-ref.txt)
- [size/apk-size.txt](size/apk-size.txt)
- [start35/app-we.txt](start35/app-we.txt)
- [start35/diag-log.jsonl](start35/diag-log.jsonl)
- [start35/diag-screen.xml](start35/diag-screen.xml)
- [start35/diag-start35.jsonl](start35/diag-start35.jsonl)
- [start35/drawn-base-1.txt](start35/drawn-base-1.txt)
- [start35/drawn-base-4.txt](start35/drawn-base-4.txt)
- [start35/drawn-base-5.txt](start35/drawn-base-5.txt)
- [start35/drawn-base-8.txt](start35/drawn-base-8.txt)
- [start35/drawn-base-lines.txt](start35/drawn-base-lines.txt)
- [start35/drawn-base.txt](start35/drawn-base.txt)
- [start35/drawn-excl-base-lines.txt](start35/drawn-excl-base-lines.txt)
- [start35/drawn-excl-base.txt](start35/drawn-excl-base.txt)
- [start35/drawn-excl-new-lines.txt](start35/drawn-excl-new-lines.txt)
- [start35/drawn-excl-new.txt](start35/drawn-excl-new.txt)
- [start35/drawn-new-2.txt](start35/drawn-new-2.txt)
- [start35/drawn-new-3.txt](start35/drawn-new-3.txt)
- [start35/drawn-new-6.txt](start35/drawn-new-6.txt)
- [start35/drawn-new-7.txt](start35/drawn-new-7.txt)
- [start35/drawn-new-lines.txt](start35/drawn-new-lines.txt)
- [start35/drawn-new.txt](start35/drawn-new.txt)
- [start35/job-status.txt](start35/job-status.txt)
- [start35/logcat-all.txt](start35/logcat-all.txt)
- [start35/logcat-dump.txt](start35/logcat-dump.txt)
- [start35/logcat-findings.txt](start35/logcat-findings.txt)
- [start35/logcat-live.txt](start35/logcat-live.txt)
- [start35/shots/diag/diagnostics-end-2.png](start35/shots/diag/diagnostics-end-2.png)
- [start35/shots/diag/diagnostics-end.png](start35/shots/diag/diagnostics-end.png)
- [start35/summary.txt](start35/summary.txt)
- [start35b/app-we.txt](start35b/app-we.txt)
- [start35b/diag-log.jsonl](start35b/diag-log.jsonl)
- [start35b/diag-screen.xml](start35b/diag-screen.xml)
- [start35b/diag-start35.jsonl](start35b/diag-start35.jsonl)
- [start35b/drawn-base-2.txt](start35b/drawn-base-2.txt)
- [start35b/drawn-base-3.txt](start35b/drawn-base-3.txt)
- [start35b/drawn-base-6.txt](start35b/drawn-base-6.txt)
- [start35b/drawn-base-7.txt](start35b/drawn-base-7.txt)
- [start35b/drawn-base-lines.txt](start35b/drawn-base-lines.txt)
- [start35b/drawn-base.txt](start35b/drawn-base.txt)
- [start35b/drawn-excl-base-lines.txt](start35b/drawn-excl-base-lines.txt)
- [start35b/drawn-excl-base.txt](start35b/drawn-excl-base.txt)
- [start35b/drawn-excl-new-lines.txt](start35b/drawn-excl-new-lines.txt)
- [start35b/drawn-excl-new.txt](start35b/drawn-excl-new.txt)
- [start35b/drawn-new-1.txt](start35b/drawn-new-1.txt)
- [start35b/drawn-new-4.txt](start35b/drawn-new-4.txt)
- [start35b/drawn-new-5.txt](start35b/drawn-new-5.txt)
- [start35b/drawn-new-8.txt](start35b/drawn-new-8.txt)
- [start35b/drawn-new-lines.txt](start35b/drawn-new-lines.txt)
- [start35b/drawn-new.txt](start35b/drawn-new.txt)
- [start35b/drawn-warm-base-lines.txt](start35b/drawn-warm-base-lines.txt)
- [start35b/drawn-warm-base.txt](start35b/drawn-warm-base.txt)
- [start35b/drawn-warm-new-lines.txt](start35b/drawn-warm-new-lines.txt)
- [start35b/drawn-warm-new.txt](start35b/drawn-warm-new.txt)
- [start35b/job-status.txt](start35b/job-status.txt)
- [start35b/logcat-all.txt](start35b/logcat-all.txt)
- [start35b/logcat-dump.txt](start35b/logcat-dump.txt)
- [start35b/logcat-findings.txt](start35b/logcat-findings.txt)
- [start35b/logcat-live.txt](start35b/logcat-live.txt)
- [start35b/shots/diag/diagnostics-end-2.png](start35b/shots/diag/diagnostics-end-2.png)
- [start35b/shots/diag/diagnostics-end.png](start35b/shots/diag/diagnostics-end.png)
- [start35b/summary.txt](start35b/summary.txt)
