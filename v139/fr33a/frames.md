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
