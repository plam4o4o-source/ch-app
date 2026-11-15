| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 6 | 68 | **433.5** (415.1..476.4) | 550 | 607 | 0.5 | 220.3 | 0.0 | 0.2 | 0.1 | 121.5 | 95.4 |
| home | new | 6 | 76 | **438.6** (424.7..487.7) | 569 | 662 | 0.5 | 229.1 | 0.0 | 0.2 | 0.1 | 128.3 | 96.4 |
| more | base | 6 | 75 | **460.7** (430.5..478.7) | 582 | 656 | 0.3 | 217.4 | 0.0 | 0.2 | 0.1 | 120.6 | 84.4 |
| more | new | 6 | 68 | **443.1** (421.8..450.1) | 533 | 915 | 0.4 | 206.0 | 0.0 | 0.2 | 0.1 | 115.1 | 84.5 |
| quiz | base | 6 | 106 | **330.0** (324.7..340.1) | 374 | 404 | 0.8 | 140.6 | 0.0 | 0.5 | 0.1 | 85.8 | 51.9 |
| quiz | new | 6 | 100 | **475.4** (472.9..483.9) | 571 | 612 | 0.5 | 203.1 | 0.0 | 0.3 | 0.1 | 97.8 | 96.9 |
| timeline | base | 6 | 96 | **318.9** (245.4..341.8) | 447 | 502 | 0.4 | 141.8 | 0.0 | 0.1 | 0.1 | 83.3 | 67.4 |
| timeline | new | 6 | 88 | **401.2** (366.5..414.1) | 494 | 614 | 0.6 | 182.5 | 0.0 | 0.1 | 0.1 | 100.4 | 79.4 |

RenderThread work per repetition (the same scripted gestures): sum of rt over the frames, ms; frames; median rt per frame:

| scenario | version | sum of RT ms, median (min..max) | frames, median (min..max) | median RT per frame (min..max) | sum of swap ms, median | sum of issue ms, median |
|---|---|---|---|---|---|---|
| home | base | **14543** (13008..17192) | 68 (64..90) | **220.3** (201.2..234.4) | 6543 | 7952 |
| home | new | **16178** (14165..18014) | 76 (65..89) | **229.1** (212.3..235.2) | 7379 | 8699 |
| more | base | **15631** (13783..16374) | 75 (71..77) | **217.4** (207.1..224.6) | 6697 | 8699 |
| more | new | **13230** (11564..16204) | 68 (61..82) | **206.0** (200.2..213.3) | 5586 | 7629 |
| quiz | base | **14087** (13878..14305) | 106 (104..111) | **140.6** (135.3..144.8) | 5959 | 8146 |
| quiz | new | **20568** (20188..21131) | 100 (100..102) | **203.1** (196.6..206.6) | 10404 | 10123 |
| timeline | base | **14112** (9818..15824) | 96 (75..105) | **141.8** (107.6..152.7) | 6036 | 8057 |
| timeline | new | **16784** (14422..18510) | 88 (86..101) | **182.5** (175.0..189.8) | 7834 | 8833 |

- home / new vs base, sum of RT: 16177.7 vs 14543.0 (+11%); U=26.0, p=0.240; ranges overlap
- home / new vs base, median RT per frame: 229.1 vs 220.3 (+4%); U=25.0, p=0.310; ranges overlap
- more / new vs base, sum of RT: 13230.2 vs 15630.7 (-15%); U=7.0, p=0.093; ranges overlap
- more / new vs base, median RT per frame: 206.0 vs 217.4 (-5%); U=4.0, p=0.026; ranges overlap
- quiz / new vs base, sum of RT: 20568.3 vs 14087.5 (+46%); U=36.0, p=0.002; ranges do NOT overlap
- quiz / new vs base, median RT per frame: 203.1 vs 140.6 (+44%); U=36.0, p=0.002; ranges do NOT overlap
- timeline / new vs base, sum of RT: 16784.0 vs 14111.8 (+19%); U=32.0, p=0.026; ranges overlap
- timeline / new vs base, median RT per frame: 182.5 vs 141.8 (+29%); U=36.0, p=0.002; ranges do NOT overlap

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [435.4, 415.1, 476.4, 431.6, 439.4, 422.1]
- home / new: [443.3, 429.2, 450.0, 487.7, 424.7, 434.0] vs base: median diff +5.1 ms (UI +0.0, RT +8.9, issue +6.9); Mann-Whitney U=23.0, p=0.485; ranges overlap
- more / base: [447.2, 455.2, 478.7, 430.5, 466.3, 476.1]
- more / new: [450.1, 442.9, 444.3, 441.3, 443.3, 421.8] vs base: median diff -17.7 ms (UI +0.0, RT -11.4, issue -5.5); Mann-Whitney U=6.0, p=0.065; ranges overlap
- quiz / base: [328.1, 330.6, 332.9, 324.7, 340.1, 329.3]
- quiz / new: [478.0, 474.0, 472.9, 483.9, 476.7, 472.9] vs base: median diff +145.4 ms (UI -0.2, RT +62.5, issue +11.9); Mann-Whitney U=36.0, p=0.002; ranges do NOT overlap
- timeline / base: [333.9, 298.1, 245.4, 304.1, 341.8, 333.8]
- timeline / new: [366.5, 401.8, 400.6, 413.3, 394.0, 414.1] vs base: median diff +82.3 ms (UI +0.2, RT +40.7, issue +17.1); Mann-Whitney U=36.0, p=0.002; ranges do NOT overlap
