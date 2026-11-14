| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| quiz | hk | 5 | 87 | **597.5** (586.5..619.2) | 767 | 841 | 0.7 | 269.4 | 0.0 | 0.4 | 0.1 | 73.8 | 168.6 |
| quiz | noband | 5 | 103 | **427.4** (414.1..441.9) | 549 | 586 | 0.6 | 200.0 | 0.0 | 0.4 | 0.1 | 69.1 | 120.3 |
| timeline | all | 5 | 79 | **425.9** (382.1..431.5) | 574 | 687 | 0.5 | 172.0 | 0.0 | 0.2 | 0.2 | 64.4 | 102.1 |
| timeline | base | 6 | 82 | **361.7** (317.9..464.9) | 556 | 685 | 0.5 | 158.5 | 0.0 | 0.2 | 0.2 | 65.9 | 65.0 |
| timeline | card | 5 | 73 | **451.6** (449.1..501.8) | 621 | 732 | 1.0 | 186.0 | 0.0 | 0.2 | 0.2 | 70.9 | 92.6 |
| timeline | enter | 5 | 69 | **494.8** (468.0..511.4) | 702 | 796 | 1.2 | 215.9 | 0.0 | 0.2 | 0.1 | 72.5 | 99.9 |
| timeline | hk | 6 | 72 | **488.0** (460.0..518.4) | 660 | 784 | 1.0 | 211.8 | 0.0 | 0.2 | 0.2 | 69.6 | 96.6 |
| timeline | medal | 5 | 84 | **345.3** (320.9..464.1) | 539 | 700 | 0.8 | 157.0 | 0.0 | 0.2 | 0.2 | 65.1 | 51.1 |
| timeline | new | 6 | 72 | **469.7** (451.6..494.4) | 647 | 793 | 1.0 | 194.2 | 0.0 | 0.2 | 0.2 | 70.8 | 93.9 |
| timeline | scrub | 5 | 58 | **498.3** (449.9..594.5) | 711 | 782 | 0.7 | 216.3 | 0.0 | 0.2 | 0.2 | 71.1 | 98.0 |
| timeline | thread | 5 | 77 | **435.7** (399.4..477.9) | 662 | 756 | 0.8 | 180.3 | 0.0 | 0.2 | 0.2 | 72.9 | 84.5 |

Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:

- quiz / hk: [586.5, 612.1, 597.5, 619.2, 588.6]
- quiz / noband: [441.9, 417.2, 414.1, 439.9, 427.4] vs hk: median diff -170.1 ms (UI -0.0, RT -69.4, issue -4.7); Mann-Whitney U=0.0, p=0.008; ranges do NOT overlap
- timeline / all: [395.7, 431.5, 382.1, 428.6, 425.9]
- timeline / base: [317.9, 355.8, 367.6, 321.9, 390.3, 464.9] vs all: median diff -64.1 ms (UI -0.0, RT -13.5, issue +1.5); Mann-Whitney U=6.0, p=0.126; ranges overlap
- timeline / card: [449.1, 501.8, 462.6, 450.1, 451.6] vs all: median diff +25.7 ms (UI +0.5, RT +14.0, issue +6.5); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / enter: [468.0, 511.4, 496.6, 494.8, 480.7] vs all: median diff +69.0 ms (UI +0.7, RT +43.9, issue +8.1); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / hk: [469.3, 460.0, 506.7, 462.3, 507.8, 518.4] vs all: median diff +62.1 ms (UI +0.4, RT +39.8, issue +5.2); Mann-Whitney U=30.0, p=0.004; ranges do NOT overlap
- timeline / medal: [345.3, 347.8, 337.4, 464.1, 320.9] vs all: median diff -80.5 ms (UI +0.2, RT -15.0, issue +0.7); Mann-Whitney U=5.0, p=0.151; ranges overlap
- timeline / new: [476.2, 487.9, 463.3, 494.4, 454.4, 451.6] vs all: median diff +43.9 ms (UI +0.5, RT +22.2, issue +6.4); Mann-Whitney U=30.0, p=0.004; ranges do NOT overlap
- timeline / scrub: [479.4, 498.3, 594.5, 510.8, 449.9] vs all: median diff +72.4 ms (UI +0.2, RT +44.4, issue +6.7); Mann-Whitney U=25.0, p=0.008; ranges do NOT overlap
- timeline / thread: [477.9, 419.1, 473.1, 399.4, 435.7] vs all: median diff +9.8 ms (UI +0.3, RT +8.3, issue +8.5); Mann-Whitney U=19.0, p=0.222; ranges overlap
