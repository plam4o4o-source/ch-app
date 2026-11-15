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
