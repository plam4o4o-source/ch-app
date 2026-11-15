| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 6 | 120 | **174.9** (171.0..194.1) | 296 | 360 | 0.6 | 62.6 | 0.1 | 0.2 | 0.1 | 1.8 | 59.4 |
| home | new | 6 | 120 | **179.1** (173.4..190.1) | 302 | 352 | 0.6 | 62.2 | 0.1 | 0.2 | 0.1 | 1.8 | 59.9 |
| more | base | 6 | 120 | **165.5** (160.2..168.5) | 227 | 248 | 0.4 | 65.6 | 0.1 | 0.2 | 0.1 | 1.8 | 64.0 |
| more | new | 6 | 120 | **166.8** (125.3..173.6) | 227 | 248 | 0.4 | 62.0 | 0.1 | 0.2 | 0.1 | 1.5 | 59.6 |
| quiz | base | 6 | 120 | **158.1** (149.3..162.9) | 218 | 231 | 0.9 | 43.9 | 0.1 | 0.5 | 0.1 | 5.1 | 36.9 |
| quiz | new | 6 | 104 | **161.6** (156.4..168.0) | 235 | 251 | 0.8 | 41.3 | 0.1 | 0.3 | 0.1 | 8.7 | 28.3 |
| timeline | base | 6 | 120 | **152.6** (147.9..156.4) | 207 | 231 | 0.6 | 58.8 | 0.1 | 0.2 | 0.1 | 2.2 | 57.6 |
| timeline | new | 6 | 120 | **151.4** (147.2..156.2) | 211 | 229 | 0.6 | 57.1 | 0.1 | 0.2 | 0.1 | 3.1 | 52.7 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| home | base | **8237** (7914..8423) | 120 (113..120) | **62.6** (41.9..64.8) | 7448 | 520 |
| home | new | **8030** (7558..8505) | 120 (101..120) | **62.2** (50.8..64.9) | 7349 | 506 |
| more | base | **7573** (7389..7592) | 120 (120..120) | **65.6** (62.3..68.6) | 7061 | 396 |
| more | new | **7553** (5785..8168) | 120 (120..120) | **62.0** (48.4..67.8) | 7117 | 355 |
| quiz | base | **7135** (7022..7245) | 120 (119..120) | **43.9** (38.3..56.8) | 6136 | 891 |
| quiz | new | **6709** (6632..6804) | 104 (103..106) | **41.3** (39.0..55.2) | 5229 | 1409 |
| timeline | base | **6910** (6753..6980) | 120 (120..120) | **58.8** (52.8..60.3) | 6338 | 464 |
| timeline | new | **6892** (6826..7052) | 120 (120..120) | **57.1** (49.1..59.7) | 6168 | 635 |

- home / new vs base, sum of RT: 8030.2 vs 8236.8 (-3%); U=12.0, p=0.394; ranges overlap
- home / new vs base, median RT per frame: 62.2 vs 62.6 (-1%); U=18.0, p=1.000; ranges overlap
- more / new vs base, sum of RT: 7552.9 vs 7573.2 (-0%); U=21.0, p=0.699; ranges overlap
- more / new vs base, median RT per frame: 62.0 vs 65.6 (-6%); U=9.0, p=0.180; ranges overlap
- quiz / new vs base, sum of RT: 6709.3 vs 7135.4 (-6%); U=0.0, p=0.002; ranges do NOT overlap
- quiz / new vs base, median RT per frame: 41.3 vs 43.9 (-6%); U=17.0, p=0.937; ranges overlap
- timeline / new vs base, sum of RT: 6892.1 vs 6909.9 (-0%); U=20.0, p=0.818; ranges overlap
- timeline / new vs base, median RT per frame: 57.1 vs 58.8 (-3%); U=8.0, p=0.132; ranges overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [177.4, 171.0, 172.3, 172.4, 194.1, 178.4]
- home / new: [173.4, 182.5, 189.6, 175.1, 175.7, 190.1] vs base: median diff +4.2 ms (UI -0.0, RT -0.4, issue -0.0); Mann-Whitney U=24.0, p=0.394; ranges overlap
- more / base: [168.5, 168.5, 160.7, 167.8, 160.2, 163.1]
- more / new: [125.3, 167.5, 166.2, 173.6, 171.9, 160.4] vs base: median diff +1.4 ms (UI +0.0, RT -3.6, issue -0.3); Mann-Whitney U=19.0, p=0.937; ranges overlap
- quiz / base: [158.9, 149.3, 153.0, 161.4, 162.9, 157.3]
- quiz / new: [164.1, 160.6, 168.0, 156.4, 160.6, 162.5] vs base: median diff +3.4 ms (UI -0.2, RT -2.6, issue +3.7); Mann-Whitney U=27.0, p=0.180; ranges overlap
- timeline / base: [147.9, 153.6, 149.2, 153.7, 156.4, 151.6]
- timeline / new: [147.2, 150.4, 156.2, 155.5, 152.3, 150.1] vs base: median diff -1.2 ms (UI +0.1, RT -1.7, issue +0.9); Mann-Whitney U=17.0, p=0.937; ranges overlap
