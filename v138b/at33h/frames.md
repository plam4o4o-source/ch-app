| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| home | base | 6 | 73 | **455.5** (406.7..482.7) | 581 | 707 | 0.4 | 184.7 | 0.0 | 0.2 | 0.1 | 50.8 | 110.0 |
| home | both | 5 | 75 | **449.4** (434.2..456.5) | 555 | 627 | 0.4 | 159.8 | 0.0 | 0.2 | 0.1 | 50.0 | 106.8 |
| home | hk | 6 | 72 | **450.2** (401.0..466.5) | 574 | 667 | 0.4 | 170.3 | 0.0 | 0.2 | 0.1 | 49.5 | 109.5 |
| home | icon | 5 | 74 | **463.4** (447.0..477.9) | 576 | 680 | 0.3 | 166.1 | 0.0 | 0.2 | 0.1 | 49.4 | 109.0 |
| home | new | 6 | 68 | **438.7** (421.3..463.2) | 593 | 672 | 0.4 | 185.8 | 0.0 | 0.2 | 0.1 | 51.3 | 110.5 |
| home | plate | 5 | 77 | **439.7** (424.1..473.7) | 543 | 669 | 0.3 | 161.5 | 0.0 | 0.2 | 0.1 | 48.8 | 107.0 |
| more | base | 6 | 66 | **434.5** (408.5..474.1) | 582 | 700 | 0.3 | 172.7 | 0.0 | 0.1 | 0.1 | 51.6 | 110.8 |
| more | both | 5 | 57 | **387.1** (371.6..419.2) | 512 | 729 | 0.2 | 145.8 | 0.0 | 0.1 | 0.1 | 49.0 | 94.3 |
| more | hk | 6 | 64 | **402.4** (351.6..419.7) | 535 | 629 | 0.3 | 159.6 | 0.0 | 0.1 | 0.1 | 50.0 | 100.9 |
| more | icon | 5 | 64 | **414.1** (409.1..446.5) | 560 | 706 | 0.3 | 158.2 | 0.0 | 0.1 | 0.1 | 50.5 | 100.6 |
| more | new | 6 | 62 | **422.1** (376.8..445.9) | 535 | 634 | 0.3 | 163.5 | 0.0 | 0.1 | 0.1 | 49.6 | 104.8 |
| more | plate | 5 | 62 | **383.3** (366.0..412.6) | 501 | 733 | 0.3 | 144.9 | 0.0 | 0.1 | 0.1 | 47.5 | 93.7 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- home / base: [464.8, 453.1, 446.4, 406.7, 457.9, 482.7]
- home / both: [445.5, 449.4, 434.2, 452.0, 456.5] vs base: median diff -6.1 ms (UI -0.0, RT -24.9, issue -0.9); Mann-Whitney U=9.0, p=0.329; ranges overlap
- home / hk: [449.9, 450.6, 466.5, 401.0, 457.7, 416.8] vs base: median diff -5.2 ms (UI +0.0, RT -14.4, issue -1.3); Mann-Whitney U=13.0, p=0.485; ranges overlap
- home / icon: [451.3, 463.4, 447.0, 477.9, 469.8] vs base: median diff +8.0 ms (UI -0.1, RT -18.6, issue -1.4); Mann-Whitney U=18.0, p=0.662; ranges overlap
- home / new: [435.8, 421.3, 436.1, 463.2, 441.3, 453.8] vs base: median diff -16.8 ms (UI +0.0, RT +1.1, issue +0.4); Mann-Whitney U=11.0, p=0.310; ranges overlap
- home / plate: [428.3, 439.7, 449.6, 473.7, 424.1] vs base: median diff -15.8 ms (UI -0.1, RT -23.2, issue -2.1); Mann-Whitney U=10.0, p=0.429; ranges overlap
- more / base: [417.8, 474.1, 408.5, 454.3, 435.6, 433.4]
- more / both: [383.4, 415.6, 419.2, 371.6, 387.1] vs base: median diff -47.4 ms (UI -0.0, RT -26.8, issue -2.6); Mann-Whitney U=3.0, p=0.030; ranges overlap
- more / hk: [405.3, 408.9, 398.6, 351.6, 419.7, 399.5] vs base: median diff -32.1 ms (UI +0.0, RT -13.0, issue -1.6); Mann-Whitney U=3.0, p=0.015; ranges overlap
- more / icon: [409.1, 410.6, 446.5, 414.1, 416.6] vs base: median diff -20.4 ms (UI -0.0, RT -14.5, issue -1.1); Mann-Whitney U=8.0, p=0.247; ranges overlap
- more / new: [376.8, 415.0, 429.1, 445.9, 439.6, 397.9] vs base: median diff -12.4 ms (UI +0.0, RT -9.2, issue -2.1); Mann-Whitney U=11.0, p=0.310; ranges overlap
- more / plate: [412.6, 366.0, 384.9, 383.3, 375.9] vs base: median diff -51.2 ms (UI +0.0, RT -27.8, issue -4.1); Mann-Whitney U=1.0, p=0.009; ranges overlap
