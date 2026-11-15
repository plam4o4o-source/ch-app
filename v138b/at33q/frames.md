| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| quiz | base | 5 | 120 | **247.2** (239.7..250.1) | 301 | 341 | 0.6 | 101.1 | 0.0 | 0.5 | 0.1 | 37.2 | 47.2 |
| quiz | hk | 5 | 109 | **349.8** (332.0..367.5) | 458 | 518 | 0.4 | 147.5 | 0.0 | 0.3 | 0.1 | 40.1 | 86.2 |
| quiz | layer | 5 | 103 | **267.2** (254.8..274.0) | 354 | 409 | 0.4 | 113.8 | 0.0 | 0.3 | 0.1 | 42.2 | 56.0 |
| quiz | new | 5 | 108 | **358.1** (353.9..366.1) | 457 | 544 | 0.4 | 152.4 | 0.0 | 0.3 | 0.1 | 41.7 | 90.0 |
| quiz | noband | 5 | 104 | **249.4** (248.0..275.1) | 309 | 366 | 0.4 | 105.5 | 0.0 | 0.2 | 0.1 | 39.0 | 48.8 |
| quiz | nohem | 5 | 103 | **287.2** (271.6..290.0) | 358 | 409 | 0.4 | 120.2 | 0.0 | 0.3 | 0.1 | 40.6 | 60.5 |
| quiz | nostitch | 5 | 110 | **340.8** (323.2..361.5) | 447 | 482 | 0.4 | 152.3 | 0.0 | 0.3 | 0.1 | 41.1 | 88.1 |
| quiz | solidhem | 5 | 102 | **281.6** (259.6..296.5) | 360 | 403 | 0.4 | 119.5 | 0.0 | 0.3 | 0.1 | 40.4 | 59.9 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| quiz | base | **11835** (11401..11951) | 120 (119..120) | **101.1** (89.7..107.2) | 6855 | 4964 |
| quiz | hk | **16192** (15714..16954) | 109 (106..113) | **147.5** (138.8..159.7) | 10549 | 5609 |
| quiz | layer | **11849** (11373..12084) | 103 (101..105) | **113.8** (110.2..116.7) | 6673 | 5136 |
| quiz | new | **16447** (15979..17408) | 108 (105..110) | **152.4** (143.9..157.6) | 10710 | 5725 |
| quiz | noband | **11048** (10493..11224) | 104 (103..105) | **105.5** (99.4..118.3) | 6396 | 4631 |
| quiz | nohem | **12352** (11982..12776) | 103 (102..105) | **120.2** (117.8..128.2) | 7459 | 4886 |
| quiz | nostitch | **16308** (14624..16877) | 110 (108..111) | **152.3** (134.3..155.1) | 10672 | 5452 |
| quiz | solidhem | **11850** (11424..12989) | 102 (101..104) | **119.5** (114.1..131.5) | 7003 | 4823 |

- quiz / hk vs base, sum of RT: 16191.7 vs 11835.2 (+37%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / hk vs base, median RT per frame: 147.5 vs 101.1 (+46%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / layer vs base, sum of RT: 11848.8 vs 11835.2 (+0%); U=15.0, p=0.690; ranges overlap
- quiz / layer vs base, median RT per frame: 113.8 vs 101.1 (+13%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / new vs base, sum of RT: 16447.2 vs 11835.2 (+39%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / new vs base, median RT per frame: 152.4 vs 101.1 (+51%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / noband vs base, sum of RT: 11047.6 vs 11835.2 (-7%); U=0.0, p=0.008; ranges do NOT overlap
- quiz / noband vs base, median RT per frame: 105.5 vs 101.1 (+4%); U=19.0, p=0.222; ranges overlap
- quiz / nohem vs base, sum of RT: 12352.0 vs 11835.2 (+4%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / nohem vs base, median RT per frame: 120.2 vs 101.1 (+19%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / nostitch vs base, sum of RT: 16308.2 vs 11835.2 (+38%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / nostitch vs base, median RT per frame: 152.3 vs 101.1 (+51%); U=25.0, p=0.008; ranges do NOT overlap
- quiz / solidhem vs base, sum of RT: 11850.0 vs 11835.2 (+0%); U=15.0, p=0.690; ranges overlap
- quiz / solidhem vs base, median RT per frame: 119.5 vs 101.1 (+18%); U=25.0, p=0.008; ranges do NOT overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- quiz / base: [247.8, 239.7, 250.1, 244.1, 247.2]
- quiz / hk: [332.0, 367.5, 346.0, 349.8, 364.0] vs base: median diff +102.6 ms (UI -0.2, RT +46.4, issue +2.9); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- quiz / layer: [267.2, 265.3, 272.4, 274.0, 254.8] vs base: median diff +20.1 ms (UI -0.2, RT +12.7, issue +5.0); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- quiz / new: [366.1, 360.6, 358.1, 357.4, 353.9] vs base: median diff +110.9 ms (UI -0.2, RT +51.3, issue +4.5); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- quiz / noband: [249.4, 249.2, 275.1, 248.0, 250.9] vs base: median diff +2.2 ms (UI -0.2, RT +4.4, issue +1.8); Mann-Whitney U=22.0, p=0.056; ranges overlap
- quiz / nohem: [287.2, 290.0, 289.8, 279.1, 271.6] vs base: median diff +40.0 ms (UI -0.2, RT +19.1, issue +3.4); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- quiz / nostitch: [340.8, 342.6, 361.5, 323.2, 333.7] vs base: median diff +93.6 ms (UI -0.2, RT +51.2, issue +3.9); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- quiz / solidhem: [296.5, 266.0, 282.4, 259.6, 281.6] vs base: median diff +34.4 ms (UI -0.2, RT +18.4, issue +3.2); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
