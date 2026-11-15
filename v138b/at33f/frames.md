| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| timeline | hk | 6 | 68 | **470.8** (449.6..518.1) | 728 | 836 | 0.6 | 216.1 | 0.0 | 0.1 | 0.1 | 70.1 | 97.5 |
| timeline | base | 6 | 74 | **439.1** (383.3..454.0) | 613 | 694 | 0.4 | 173.5 | 0.0 | 0.1 | 0.1 | 66.4 | 106.2 |
| timeline | layer | 6 | 86 | **416.6** (302.1..449.3) | 572 | 655 | 0.5 | 173.0 | 0.0 | 0.1 | 0.1 | 65.7 | 91.2 |
| timeline | medal | 6 | 86 | **419.6** (383.0..429.1) | 597 | 668 | 0.5 | 171.9 | 0.0 | 0.1 | 0.1 | 66.7 | 99.8 |
| timeline | new | 6 | 71 | **507.3** (469.3..536.7) | 713 | 832 | 0.6 | 205.7 | 0.0 | 0.1 | 0.1 | 70.3 | 98.0 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| timeline | hk | **14822** (12626..16836) | 68 (64..79) | **216.1** (172.6..221.2) | 8040 | 6723 |
| timeline | base | **12653** (11407..13831) | 74 (66..78) | **173.5** (169.2..178.4) | 6814 | 5623 |
| timeline | layer | **13642** (11976..14764) | 86 (76..101) | **173.0** (107.9..181.8) | 7618 | 6268 |
| timeline | medal | **14480** (12656..15098) | 86 (76..90) | **171.9** (169.5..177.3) | 7813 | 6584 |
| timeline | new | **14527** (12265..16982) | 71 (59..77) | **205.7** (183.5..220.2) | 8274 | 6179 |

- timeline / base vs hk, sum of RT: 12653.2 vs 14822.0 (-15%); U=3.0, p=0.015; ranges overlap
- timeline / base vs hk, median RT per frame: 173.5 vs 216.1 (-20%); U=3.0, p=0.015; ranges overlap
- timeline / layer vs hk, sum of RT: 13642.2 vs 14822.0 (-8%); U=7.0, p=0.093; ranges overlap
- timeline / layer vs hk, median RT per frame: 173.0 vs 216.1 (-20%); U=3.0, p=0.015; ranges overlap
- timeline / medal vs hk, sum of RT: 14480.4 vs 14822.0 (-2%); U=14.0, p=0.589; ranges overlap
- timeline / medal vs hk, median RT per frame: 171.9 vs 216.1 (-20%); U=3.0, p=0.015; ranges overlap
- timeline / new vs hk, sum of RT: 14526.8 vs 14822.0 (-2%); U=16.0, p=0.818; ranges overlap
- timeline / new vs hk, median RT per frame: 205.7 vs 216.1 (-5%); U=15.0, p=0.699; ranges overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- timeline / hk: [474.6, 467.0, 466.3, 518.1, 478.4, 449.6]
- timeline / base: [383.3, 450.2, 403.1, 449.7, 428.6, 454.0] vs hk: median diff -31.6 ms (UI -0.2, RT -42.5, issue -3.7); Mann-Whitney U=3.0, p=0.015; ranges overlap
- timeline / layer: [432.7, 443.5, 306.4, 449.3, 302.1, 400.4] vs hk: median diff -54.2 ms (UI -0.1, RT -43.1, issue -4.4); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
- timeline / medal: [383.0, 422.6, 429.1, 391.7, 419.6, 419.7] vs hk: median diff -51.1 ms (UI -0.1, RT -44.2, issue -3.4); Mann-Whitney U=0.0, p=0.002; ranges do NOT overlap
- timeline / new: [504.8, 469.3, 509.9, 536.7, 517.3, 479.4] vs hk: median diff +36.6 ms (UI +0.0, RT -10.3, issue +0.2); Mann-Whitney U=29.0, p=0.093; ranges overlap
