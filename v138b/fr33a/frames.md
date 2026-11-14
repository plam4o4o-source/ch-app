| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 7 | 59 | **590.1** (498.4..634.2) | 734 | 886 | 0.4 | 222.6 | 0.0 | 0.2 | 0.1 | 67.9 | 140.4 |
| home | new | 7 | 59 | **583.5** (526.7..597.6) | 756 | 921 | 0.4 | 215.8 | 0.0 | 0.2 | 0.1 | 67.1 | 137.8 |
| more | base | 7 | 61 | **530.1** (436.1..568.8) | 717 | 921 | 0.2 | 207.7 | 0.0 | 0.1 | 0.1 | 68.9 | 132.4 |
| more | new | 7 | 67 | **511.9** (406.0..549.6) | 662 | 749 | 0.2 | 198.0 | 0.0 | 0.1 | 0.1 | 67.8 | 126.8 |
| quiz | base | 6 | 85 | **421.1** (410.6..432.7) | 537 | 604 | 0.8 | 183.1 | 0.0 | 0.7 | 0.1 | 67.8 | 76.0 |
| quiz | new | 6 | 88 | **599.8** (592.2..648.8) | 786 | 886 | 0.4 | 274.9 | 0.0 | 0.3 | 0.1 | 72.2 | 185.6 |
| timeline | base | 7 | 75 | **431.8** (358.5..461.9) | 597 | 683 | 0.4 | 173.1 | 0.0 | 0.1 | 0.1 | 66.9 | 105.1 |
| timeline | new | 6 | 70 | **494.2** (459.2..548.6) | 723 | 850 | 0.5 | 201.9 | 0.0 | 0.1 | 0.1 | 68.9 | 102.6 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [591.9, 634.2, 593.0, 534.3, 498.4, 590.1, 502.2]
- home / new: [592.8, 526.7, 596.5, 583.5, 597.6, 534.5, 550.0] vs base: median diff -6.6 ms (UI +0.0, RT -6.8, issue -0.8); Mann-Whitney U=28.0, p=0.710; ranges overlap
- more / base: [521.4, 554.5, 530.1, 568.8, 436.1, 534.0, 518.9]
- more / new: [511.9, 468.9, 549.6, 406.0, 522.6, 539.6, 458.3] vs base: median diff -18.2 ms (UI +0.0, RT -9.7, issue -1.1); Mann-Whitney U=16.0, p=0.318; ranges overlap
- quiz / base: [420.3, 415.9, 422.0, 410.6, 432.7, 425.3]
- quiz / new: [648.8, 602.2, 596.4, 624.1, 592.2, 597.4] vs base: median diff +178.7 ms (UI -0.4, RT +91.8, issue +4.4); Mann-Whitney U=36.0, p=0.002; ranges do NOT overlap
- timeline / base: [461.9, 436.1, 415.3, 358.5, 368.4, 444.7, 431.8]
- timeline / new: [548.6, 459.2, 502.1, 486.3, 482.9, 527.1] vs base: median diff +62.4 ms (UI +0.1, RT +28.8, issue +2.0); Mann-Whitney U=41.0, p=0.002; ranges overlap
