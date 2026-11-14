| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 7 | 70 | **454.7** (403.5..510.1) | 582 | 672 | 0.4 | 185.7 | 0.0 | 0.2 | 0.1 | 54.4 | 108.4 |
| home | new | 7 | 75 | **473.4** (450.6..486.8) | 594 | 681 | 0.4 | 185.4 | 0.0 | 0.2 | 0.1 | 53.7 | 107.3 |
| more | base | 7 | 70 | **446.8** (347.9..477.2) | 581 | 685 | 0.2 | 177.6 | 0.0 | 0.1 | 0.1 | 55.1 | 105.1 |
| more | new | 7 | 68 | **428.9** (397.2..452.6) | 549 | 597 | 0.2 | 165.4 | 0.0 | 0.1 | 0.1 | 54.9 | 100.8 |
| quiz | base | 6 | 100 | **337.1** (325.2..344.7) | 416 | 480 | 0.8 | 131.6 | 0.0 | 0.6 | 0.1 | 53.5 | 57.9 |
| quiz | new | 6 | 100 | **462.1** (440.8..485.0) | 598 | 657 | 0.4 | 204.5 | 0.0 | 0.3 | 0.1 | 56.8 | 113.5 |
| timeline | base | 7 | 84 | **318.9** (292.7..372.6) | 449 | 514 | 0.3 | 132.1 | 0.0 | 0.1 | 0.1 | 52.0 | 78.4 |
| timeline | new | 7 | 83 | **385.6** (346.0..447.4) | 546 | 637 | 0.5 | 153.5 | 0.0 | 0.1 | 0.1 | 54.3 | 78.2 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [510.1, 425.3, 425.2, 403.5, 460.2, 482.6, 454.7]
- home / new: [486.8, 477.7, 473.4, 473.0, 450.6, 453.3, 476.7] vs base: median diff +18.8 ms (UI -0.1, RT -0.3, issue -0.7); Mann-Whitney U=32.0, p=0.383; ranges overlap
- more / base: [430.4, 450.9, 477.2, 347.9, 446.8, 468.6, 430.5]
- more / new: [439.4, 404.1, 428.1, 435.9, 428.9, 452.6, 397.2] vs base: median diff -17.9 ms (UI -0.0, RT -12.1, issue -0.1); Mann-Whitney U=15.0, p=0.259; ranges overlap
- quiz / base: [337.1, 344.7, 329.7, 325.2, 337.1, 341.6]
- quiz / new: [485.0, 465.1, 459.4, 440.8, 462.5, 461.7] vs base: median diff +125.0 ms (UI -0.3, RT +72.9, issue +3.3); Mann-Whitney U=36.0, p=0.002; ranges do NOT overlap
- timeline / base: [317.4, 372.6, 292.7, 342.6, 304.6, 336.9, 318.9]
- timeline / new: [385.1, 373.6, 385.6, 447.4, 346.0, 406.8, 394.9] vs base: median diff +66.7 ms (UI +0.2, RT +21.4, issue +2.3); Mann-Whitney U=48.0, p=0.001; ranges overlap
