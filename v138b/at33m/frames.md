| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| timeline | base | 5 | 81 | **430.6** (395.3..434.7) | 585 | 655 | 0.4 | 170.5 | 0.0 | 0.1 | 0.1 | 64.7 | 105.4 |
| timeline | flat | 5 | 68 | **472.1** (437.0..506.4) | 749 | 823 | 0.5 | 188.8 | 0.0 | 0.1 | 0.1 | 69.2 | 100.5 |
| timeline | flatnoring | 5 | 80 | **408.6** (375.5..449.8) | 623 | 723 | 0.5 | 177.7 | 0.0 | 0.1 | 0.1 | 66.6 | 74.8 |
| timeline | hk | 5 | 67 | **523.2** (429.5..564.1) | 701 | 836 | 0.6 | 213.3 | 0.0 | 0.1 | 0.1 | 70.1 | 98.2 |
| timeline | medal | 5 | 81 | **371.6** (345.2..406.0) | 575 | 703 | 0.5 | 161.4 | 0.0 | 0.1 | 0.1 | 66.1 | 61.9 |
| timeline | new | 5 | 74 | **472.4** (460.9..538.0) | 687 | 812 | 0.6 | 181.8 | 0.0 | 0.1 | 0.1 | 68.5 | 94.4 |
| timeline | noring | 5 | 79 | **449.7** (381.7..466.1) | 638 | 727 | 0.5 | 179.0 | 0.0 | 0.1 | 0.1 | 67.7 | 80.5 |
| timeline | solidring | 5 | 75 | **449.5** (397.3..456.4) | 674 | 780 | 0.5 | 187.0 | 0.0 | 0.1 | 0.1 | 68.0 | 81.4 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| timeline | base | **13530** (12439..13781) | 81 (76..82) | **170.5** (168.2..173.9) | 7455 | 6002 |
| timeline | flat | **14477** (12170..15671) | 68 (60..82) | **188.8** (163.4..220.4) | 8408 | 5988 |
| timeline | flatnoring | **14779** (13724..15611) | 80 (78..84) | **177.7** (137.1..194.8) | 8015 | 6670 |
| timeline | hk | **13061** (12704..15441) | 67 (62..69) | **213.3** (176.0..230.8) | 7556 | 5586 |
| timeline | medal | **13274** (12201..13912) | 81 (73..93) | **161.4** (139.8..171.0) | 6916 | 6222 |
| timeline | new | **14982** (14864..16295) | 74 (69..80) | **181.8** (165.4..225.2) | 8695 | 6544 |
| timeline | noring | **14125** (12780..15138) | 79 (65..88) | **179.0** (143.2..200.6) | 7809 | 6080 |
| timeline | solidring | **14602** (13495..15959) | 75 (70..83) | **187.0** (155.8..197.9) | 8046 | 6410 |

- timeline / flat vs base, sum of RT: 14476.9 vs 13530.5 (+7%); U=16.0, p=0.548; ranges overlap
- timeline / flat vs base, median RT per frame: 188.8 vs 170.5 (+11%); U=16.0, p=0.548; ranges overlap
- timeline / flatnoring vs base, sum of RT: 14779.0 vs 13530.5 (+9%); U=24.0, p=0.016; ranges overlap
- timeline / flatnoring vs base, median RT per frame: 177.7 vs 170.5 (+4%); U=15.0, p=0.690; ranges overlap
- timeline / hk vs base, sum of RT: 13060.6 vs 13530.5 (-3%); U=13.0, p=1.000; ranges overlap
- timeline / hk vs base, median RT per frame: 213.3 vs 170.5 (+25%); U=25.0, p=0.008; ranges do NOT overlap
- timeline / medal vs base, sum of RT: 13274.0 vs 13530.5 (-2%); U=8.0, p=0.421; ranges overlap
- timeline / medal vs base, median RT per frame: 161.4 vs 170.5 (-5%); U=4.0, p=0.095; ranges overlap
- timeline / new vs base, sum of RT: 14982.1 vs 13530.5 (+11%); U=25.0, p=0.008; ranges do NOT overlap
- timeline / new vs base, median RT per frame: 181.8 vs 170.5 (+7%); U=20.0, p=0.151; ranges overlap
- timeline / noring vs base, sum of RT: 14124.7 vs 13530.5 (+4%); U=20.0, p=0.151; ranges overlap
- timeline / noring vs base, median RT per frame: 179.0 vs 170.5 (+5%); U=15.0, p=0.690; ranges overlap
- timeline / solidring vs base, sum of RT: 14602.2 vs 13530.5 (+8%); U=22.0, p=0.056; ranges overlap
- timeline / solidring vs base, median RT per frame: 187.0 vs 170.5 (+10%); U=16.0, p=0.548; ranges overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- timeline / base: [430.6, 430.6, 396.0, 395.3, 434.7]
- timeline / flat: [437.0, 506.4, 484.4, 444.4, 472.1] vs base: median diff +41.5 ms (UI +0.1, RT +18.3, issue +4.5); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / flatnoring: [408.6, 375.5, 449.8, 445.1, 390.4] vs base: median diff -22.0 ms (UI +0.1, RT +7.2, issue +1.9); Mann-Whitney U=12.0, p=1.000; ranges overlap
- timeline / hk: [523.8, 429.5, 523.2, 564.1, 459.9] vs base: median diff +92.6 ms (UI +0.2, RT +42.8, issue +5.4); Mann-Whitney U=22.0, p=0.056; ranges overlap
- timeline / medal: [345.2, 375.1, 406.0, 368.4, 371.6] vs base: median diff -58.9 ms (UI +0.1, RT -9.1, issue +1.4); Mann-Whitney U=2.0, p=0.032; ranges overlap
- timeline / new: [467.7, 460.9, 497.4, 472.4, 538.0] vs base: median diff +41.8 ms (UI +0.2, RT +11.3, issue +3.8); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / noring: [393.9, 466.1, 460.2, 449.7, 381.7] vs base: median diff +19.1 ms (UI +0.1, RT +8.5, issue +3.0); Mann-Whitney U=15.0, p=0.690; ranges overlap
- timeline / solidring: [456.4, 397.3, 448.4, 449.5, 455.4] vs base: median diff +18.9 ms (UI +0.1, RT +16.5, issue +3.3); Mann-Whitney U=22.0, p=0.056; ranges overlap
