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
