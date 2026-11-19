interaction | frames | UI>32 | UI>80 | UI max | total max | doFrame max (trace) | path in the worst doFrame | long non-frame main-thread work
---|---|---|---|---|---|---|---|---
first-install-open | 16 | 9 | 8 | 1713.9 | 1870.2 | 1167.9 | traversal 1111 > draw 1111 > Record View#draw() 975 > AndroidOwner:measureAndLayout 675 > Lock contention on Jit code cache (owner 154 | msg Handler (android.os.Handler) jd: 0 71
cold-open-1 | 14 | 12 | 12 | 593.3 | 847.1 | 384.8 | traversal 356 > draw 356 > postAndWait 282 | 
cold-open-2 | 20 | 18 | 18 | 496.3 | 673.3 | 388.7 | traversal 386 > draw 386 > postAndWait 367 | msg Handler (android.app.ActivityThread$H) null: 1 197
cold-open-3 | 12 | 11 | 10 | 518.3 | 743.0 | 287.9 | traversal 288 > draw 288 > postAndWait 288 | msg Handler (android.os.Handler) jd: 0 54
leave-home-1 | 10 | 9 | 9 | 376.0 | 576.2 | 228.5 | traversal 228 > draw 228 > postAndWait 228 | 
warm-open-1 | 9 | 8 | 8 | 628.2 | 875.7 | 422.4 | traversal 353 > draw 353 > postAndWait 353 | msg Handler (android.app.ActivityThread$H) null: 1 151
leave-home-2 | 10 | 8 | 7 | 317.3 | 466.9 | 282.5 | traversal 281 > draw 281 > postAndWait 279 | 
warm-open-2 | 9 | 8 | 8 | 490.0 | 790.2 | 385.0 | traversal 382 > draw 382 > postAndWait 382 | msg Handler (android.app.ActivityThread$H) null: 1 248
leave-home-3 | 10 | 9 | 9 | 359.4 | 573.2 | 201.4 | traversal 201 > draw 201 > postAndWait 201 | 
warm-open-3 | 9 | 7 | 7 | 550.1 | 845.6 | 306.9 | traversal 307 > draw 306 > postAndWait 306 | 
leave-news | 15 | 14 | 14 | 420.3 | 652.1 | 244.1 | traversal 243 > draw 243 > postAndWait 242 | 
back-from-news | 9 | 8 | 8 | 529.3 | 727.0 | 288.4 | traversal 288 > draw 288 > postAndWait 287 | 
leave-more | 12 | 11 | 11 | 428.4 | 625.1 | 425.3 | traversal 423 > draw 423 > postAndWait 402 | 
back-from-more | 9 | 8 | 8 | 387.1 | 770.2 | 323.1 | traversal 323 > draw 323 > postAndWait 322 | 
fling-list-1 | 51 | 32 | 31 | 603.3 | 942.7 | 477.9 | traversal 477 > draw 476 > postAndWait 476 | msg Handler (android.view.ViewRootImpl$ViewRootHan 60
fling-list-2 | 53 | 36 | 36 | 666.4 | 919.8 | 393.5 | traversal 393 > draw 393 > postAndWait 393 | 
fling-list-3 | 55 | 46 | 46 | 850.3 | 1235.3 | 545.6 | traversal 527 > draw 527 > postAndWait 511 | msg Handler (android.os.Handler) jd: 0 72
reselect-top-1 | 8 | 6 | 6 | 545.2 | 810.1 | 363.7 | traversal 364 > draw 363 > postAndWait 363 | 
reselect-top-2 | 9 | 7 | 6 | 462.6 | 758.0 | 269.2 | traversal 269 > draw 269 > postAndWait 269 | 
type-1 | 36 | 32 | 28 | 352.8 | 536.0 | 311.3 | traversal 311 > draw 311 > postAndWait 311 | msg Handler (android.app.ActivityThread$H) null: 1 32
chip-author | 16 | 8 | 7 | 280.4 | 420.0 | 190.2 | traversal 190 > draw 190 > postAndWait 190 | 
chip-title | 25 | 23 | 22 | 368.4 | 556.2 | 278.8 | traversal 278 > draw 278 > postAndWait 278 | msg Handler (android.app.ActivityThread$H) null: 1 184
chip-all | 26 | 24 | 22 | 401.3 | 536.8 | 230.4 | traversal 230 > draw 230 > postAndWait 230 | msg Handler (android.app.ActivityThread$H) null: 1 144
clear-1 | 12 | 5 | 3 | 342.7 | 633.6 | 262.7 | traversal 263 > draw 262 > postAndWait 262 | msg Handler (android.app.ActivityThread$H) null: 1 174
type-2 | 15 | 11 | 11 | 785.0 | 1037.5 | 471.4 | traversal 465 > draw 465 > postAndWait 461 | msg Handler (android.os.Handler) jd: 0 100
clear-2 | 11 | 7 | 7 | 537.0 | 824.3 | 369.1 | traversal 368 > draw 368 > postAndWait 367 | 
type-3 | 18 | 15 | 15 | 735.7 | 1037.6 | 446.5 | traversal 443 > draw 443 > postAndWait 436 | 
erase-3 | 16 | 13 | 13 | 909.1 | 1155.1 | 618.4 | traversal 618 > draw 618 > postAndWait 618 | msg Handler (android.view.ViewRootImpl$ViewRootHan 79
ime-show-1 | 8 | 6 | 4 | 511.9 | 786.8 | 309.0 | traversal 307 > draw 307 > postAndWait 307 | 
ime-hide-1 | 8 | 4 | 3 | 933.8 | 1206.8 | 542.6 | traversal 542 > draw 542 > postAndWait 542 | 
ime-show-2 | 9 | 5 | 3 | 621.6 | 970.5 | 391.1 | traversal 391 > draw 391 > postAndWait 391 | 
ime-hide-2 | 9 | 4 | 4 | 620.8 | 842.8 | 402.7 | traversal 402 > draw 402 > postAndWait 402 | 
ime-show-3 | 9 | 4 | 4 | 504.5 | 727.0 | 295.8 | traversal 295 > draw 295 > postAndWait 295 | 
ime-hide-3 | 9 | 4 | 4 | 425.4 | 728.4 | 235.8 | traversal 236 > draw 235 > postAndWait 235 | 
ime-show-4 | 9 | 4 | 4 | 433.6 | 752.6 | 374.0 | traversal 219 > draw 219 > postAndWait 218 | 
type-digits | 11 | 6 | 6 | 950.2 | 1341.0 | 514.4 | traversal 513 > draw 513 > postAndWait 490 | 
clear-digits | 11 | 9 | 9 | 691.3 | 964.0 | 367.5 | traversal 367 > draw 367 > postAndWait 364 | 
ime-hide-4 | 9 | 5 | 4 | 608.0 | 974.1 | 448.2 | traversal 448 > draw 448 > postAndWait 448 | 
sort-open-1 | 8 | 6 | 6 | 894.0 | 973.2 | 676.6 | traversal 672 > draw 391 > postAndWait 235 | 
sort-year-1 | 10 | 8 | 8 | 770.3 | 1067.1 | 358.1 | traversal 358 > draw 358 > postAndWait 357 | 
sort-open-2 | 9 | 6 | 6 | 880.7 | 1025.9 | 656.5 | traversal 641 > draw 460 > postAndWait 230 | 
sort-relevance-2 | 10 | 7 | 7 | 819.3 | 1111.8 | 467.9 | traversal 466 > draw 273 > postAndWait 250 | 
sort-open-3 | 9 | 7 | 7 | 712.5 | 809.9 | 466.9 | traversal 463 > draw 451 > postAndWait 289 | 
sort-year-3 | 10 | 7 | 7 | 678.4 | 994.6 | 535.1 | traversal 535 > draw 535 > postAndWait 534 | 
filters-open-1 | 8 | 6 | 6 | 1199.1 | 1499.7 | 933.0 | traversal 928 > draw 494 > postAndWait 227 | msg Handler (android.app.ActivityThread$H) null: 1 244
filters-available-1 | 8 | 7 | 7 | 1157.8 | 1338.2 | 648.2 | traversal 648 > draw 353 > postAndWait 353 | 
filters-close-1 | 6 | 2 | 1 | 109.8 | 430.8 | 280.2 | animation 280 > Recomposer:recompose 280 > Compose:onForgotten 279 > binder transaction 3 | 
filters-open-2 | 8 | 6 | 6 | 1076.1 | 1377.5 | 957.8 | traversal 953 > draw 539 > postAndWait 240 | 
filters-doctype-2 | 7 | 6 | 5 | 1148.6 | 1316.0 | 649.4 | traversal 649 > draw 360 > postAndWait 360 | 
filters-close-2 | 5 | 2 | 1 | 116.8 | 449.2 | 382.1 | animation 382 > Recomposer:recompose 382 > Compose:onForgotten 381 > binder transaction 4 | 
filters-open-3 | 8 | 6 | 6 | 1014.4 | 1330.1 | 915.3 | traversal 910 > relayoutWindow#first=true/resize=true/vi 456 > binder transaction 6 | msg Handler (android.app.ActivityThread$H) null: 1 231
filters-available-3 | 7 | 6 | 5 | 1306.1 | 1367.0 | 664.7 | traversal 664 > draw 442 > postAndWait 441 | 
filters-close-3 | 5 | 2 | 1 | 231.2 | 512.3 | 406.2 | animation 406 > Recomposer:recompose 406 > Compose:onForgotten 405 > binder transaction 10 | 
filters-open-4 | 7 | 6 | 6 | 953.6 | 1313.8 | 733.1 | traversal 733 > draw 399 > postAndWait 399 | msg Handler (android.app.ActivityThread$H) null: 1 54
filters-clear | 8 | 7 | 6 | 1088.5 | 1139.2 | 755.5 | traversal 755 > draw 424 > postAndWait 423 | 
filters-close-4 | 6 | 2 | 0 | 43.7 | 410.2 | 207.3 | traversal 206 > draw 206 > postAndWait 206 | 
shelf-on-1 | 13 | 9 | 9 | 447.9 | 753.3 | 338.9 | traversal 336 > draw 336 > postAndWait 318 | 
fling-shelf-1 | 63 | 47 | 47 | 522.4 | 833.3 | 343.3 | traversal 343 > draw 343 > postAndWait 343 | 
shelf-off-1 | 12 | 8 | 8 | 611.4 | 823.2 | 421.6 | traversal 422 > draw 422 > postAndWait 421 | 
shelf-on-2 | 14 | 7 | 6 | 336.5 | 567.7 | 201.6 | traversal 202 > draw 202 > postAndWait 201 | 
fling-shelf-2 | 66 | 55 | 53 | 502.5 | 794.6 | 319.0 | traversal 319 > draw 319 > postAndWait 319 | msg Handler (android.os.Handler) jd: 0 65
shelf-off-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
shelf-on-3 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
fling-shelf-3 | 70 | 62 | 59 | 530.5 | 806.1 | 339.5 | traversal 340 > draw 339 > postAndWait 339 | msg Handler (android.os.Handler) jd: 0 52
shelf-off-3 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
surprise-open-1 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
surprise-close-1 | 8 | 7 | 6 | 577.6 | 752.9 | 333.3 | traversal 333 > draw 333 > postAndWait 326 | 
surprise-open-2 | 17 | 15 | 15 | 965.0 | 1294.5 | 546.8 | traversal 546 > draw 546 > postAndWait 535 | 
surprise-close-2 | 5 | 4 | 4 | 384.5 | 697.3 | 257.6 | traversal 257 > draw 257 > postAndWait 254 | 
surprise-open-3 | 15 | 14 | 14 | 777.1 | 1350.5 | 617.6 | traversal 617 > draw 617 > postAndWait 617 | 
surprise-close-3 | 5 | 4 | 4 | 379.8 | 681.9 | 291.4 | traversal 291 > draw 291 > postAndWait 291 | 
book-back-1 | 8 | 7 | 7 | 420.7 | 513.6 | 225.1 | traversal 224 > draw 224 > postAndWait 223 | 
book-back-2 | 8 | 7 | 7 | 437.3 | 603.3 | 242.6 | traversal 242 > draw 242 > postAndWait 242 | 
book-back-3 | 8 | 6 | 6 | 373.8 | 531.8 | 223.1 | traversal 223 > draw 223 > postAndWait 222 | 
refresh-1 | 13 | 11 | 11 | 436.7 | 713.3 | 260.1 | traversal 260 > draw 260 > postAndWait 260 | 
refresh-2 | 13 | 11 | 11 | 441.8 | 716.1 | 260.9 | traversal 260 > draw 260 > postAndWait 259 | 
resume-1 | 1 | 1 | 1 | 488.3 | 926.7 | 812.1 | traversal 812 > draw 205 > postAndWait 19 | 
resume-2 | 1 | 1 | 1 | 279.7 | 713.4 | 551.0 | traversal 551 > draw 156 > postAndWait 145 | 
slowest UI-thread frames: first-install-open 1714 ms, filters-available-3 1306 ms, filters-open-1 1199 ms, filters-available-1 1158 ms, filters-doctype-2 1149 ms, filters-clear 1088 ms, filters-open-2 1076 ms, filters-open-3 1014 ms, surprise-open-2 965 ms, filters-open-4 954 ms, type-digits 950 ms, ime-hide-1 934 ms
