interaction | frames | UI>32 | UI>80 | UI max | total max | doFrame max (trace) | path in the worst doFrame | long non-frame main-thread work
---|---|---|---|---|---|---|---|---
first-install-open | 14 | 8 | 8 | 1149.7 | 1272.2 | 657.4 | Choreographer#doFrame - resynced to 1311 644 > traversal 572 > draw-VRI[MainActivity] 571 > Record View#draw() 357 > AndroidOwner:measureAndLayout 309 | 
cold-open-1 | 17 | 13 | 13 | 581.1 | 880.2 | 457.5 | Choreographer#doFrame - resynced to 2633 456 > traversal 442 > draw-VRI[MainActivity] 442 > Record View#draw() 247 > AndroidOwner:measureAndLayout 236 | msg Handler (android.app.ActivityThread$H) null: 1 188
cold-open-2 | 16 | 13 | 13 | 658.0 | 852.2 | 515.0 | Choreographer#doFrame - resynced to 3315 506 > traversal 503 > draw-VRI[MainActivity] 503 > Record View#draw() 287 > AndroidOwner:measureAndLayout 276 | 
cold-open-3 | 14 | 12 | 12 | 555.7 | 728.2 | 325.7 | Choreographer#doFrame - resynced to 4005 326 > traversal 262 > draw-VRI[MainActivity] 262 > postAndWait 231 | msg Handler (android.app.ActivityThread$H) null: 1 123
leave-home-1 | 9 | 8 | 8 | 437.7 | 752.3 | 438.0 | traversal 436 > draw-VRI[MainActivity] 436 > postAndWait 409 | 
warm-open-1 | 9 | 8 | 8 | 582.2 | 873.5 | 308.4 | Choreographer#doFrame - resynced to 4068 308 > traversal 308 > draw-VRI[MainActivity] 308 > postAndWait 307 | msg Handler (android.app.ActivityThread$H) null: 1 129
leave-home-2 | 10 | 9 | 9 | 419.7 | 663.5 | 239.8 | traversal 240 > draw-VRI[MainActivity] 239 > postAndWait 239 | 
warm-open-2 | 9 | 8 | 8 | 538.8 | 811.5 | 348.6 | Choreographer#doFrame - resynced to 4121 348 > traversal 348 > draw-VRI[MainActivity] 348 > postAndWait 348 | msg Handler (android.app.ActivityThread$H) null: 1 139
leave-home-3 | 10 | 9 | 9 | 377.7 | 589.4 | 235.4 | Choreographer#doFrame - resynced to 4142 235 > traversal 235 > draw-VRI[MainActivity] 235 > postAndWait 234 | 
warm-open-3 | 9 | 8 | 8 | 447.2 | 736.5 | 287.3 | Choreographer#doFrame - resynced to 4167 287 > traversal 287 > draw-VRI[MainActivity] 287 > postAndWait 287 | 
leave-news | 17 | 16 | 16 | 555.9 | 724.8 | 379.4 | Choreographer#doFrame - resynced to 4187 378 > traversal 378 > draw-VRI[MainActivity] 378 > postAndWait 365 | 
back-from-news | 9 | 8 | 8 | 447.5 | 799.7 | 270.2 | traversal 270 > draw-VRI[MainActivity] 270 > postAndWait 270 | 
leave-more | 12 | 11 | 11 | 356.9 | 547.0 | 280.4 | traversal 278 > draw-VRI[MainActivity] 278 > postAndWait 273 | 
back-from-more | 9 | 8 | 8 | 465.2 | 745.7 | 247.5 | traversal 244 > draw-VRI[MainActivity] 244 > postAndWait 217 | 
fling-list-1 | 55 | 37 | 37 | 594.9 | 874.3 | 376.4 | Choreographer#doFrame - resynced to 4279 372 > traversal 366 > draw-VRI[MainActivity] 366 > postAndWait 365 | 
fling-list-2 | 54 | 35 | 35 | 579.2 | 878.7 | 369.4 | Choreographer#doFrame - resynced to 4370 369 > traversal 369 > draw-VRI[MainActivity] 369 > postAndWait 369 | 
fling-list-3 | 53 | 39 | 38 | 576.1 | 940.4 | 369.6 | Choreographer#doFrame - resynced to 4539 369 > traversal 365 > draw-VRI[MainActivity] 365 > postAndWait 348 | 
reselect-top-1 | 9 | 7 | 7 | 533.4 | 815.1 | 469.3 | traversal 469 > draw-VRI[MainActivity] 469 > postAndWait 469 | msg Handler (android.os.Handler) android.graphics. 74
reselect-top-2 | 10 | 7 | 7 | 529.5 | 783.8 | 299.5 | traversal 298 > draw-VRI[MainActivity] 298 > postAndWait 297 | 
loadmore-1 | 6 | 5 | 5 | 553.7 | 831.4 | 314.8 | Choreographer#doFrame - resynced to 4813 314 > traversal 314 > draw-VRI[MainActivity] 314 > postAndWait 314 | 
loadmore-2 | 6 | 5 | 5 | 489.2 | 797.5 | 286.9 | Choreographer#doFrame - resynced to 5229 284 > traversal 284 > draw-VRI[MainActivity] 284 > postAndWait 284 | 
type-1 | 18 | 13 | 13 | 683.1 | 960.4 | 447.1 | Choreographer#doFrame - resynced to 5337 447 > traversal 446 > draw-VRI[MainActivity] 446 > postAndWait 445 | 
chip-author | 23 | 21 | 21 | 546.5 | 723.6 | 283.4 | Choreographer#doFrame - resynced to 5386 283 > traversal 280 > draw-VRI[MainActivity] 280 > postAndWait 279 | msg Handler (android.app.ActivityThread$H) null: 1 163
chip-title | 27 | 27 | 27 | 309.5 | 507.3 | 202.9 | Choreographer#doFrame - resynced to 5477 203 > traversal 202 > draw-VRI[MainActivity] 202 > postAndWait 202 | msg Handler (android.app.ActivityThread$H) null: 1 193
chip-all | 16 | 14 | 14 | 365.0 | 602.8 | 264.5 | Choreographer#doFrame - resynced to 5583 264 > traversal 264 > draw-VRI[MainActivity] 264 > postAndWait 263 | msg Handler (android.app.ActivityThread$H) null: 1 159
clear-1 | 12 | 8 | 8 | 557.2 | 826.4 | 331.9 | Choreographer#doFrame - resynced to 5652 332 > traversal 331 > draw-VRI[MainActivity] 331 > postAndWait 331 | 
type-2 | 16 | 11 | 10 | 645.6 | 1033.2 | 359.3 | Choreographer#doFrame - resynced to 5683 356 > traversal 354 > draw-VRI[MainActivity] 354 > postAndWait 353 | 
clear-2 | 12 | 8 | 7 | 564.5 | 913.4 | 372.4 | Choreographer#doFrame - resynced to 5737 372 > traversal 372 > draw-VRI[MainActivity] 372 > postAndWait 371 | 
type-3 | 17 | 13 | 12 | 604.3 | 964.9 | 409.0 | Choreographer#doFrame - resynced to 5781 409 > traversal 408 > draw-VRI[MainActivity] 408 > postAndWait 408 | 
erase-3 | 17 | 13 | 13 | 780.9 | 1231.2 | 557.8 | Choreographer#doFrame - resynced to 5834 558 > traversal 556 > draw-VRI[MainActivity] 556 > postAndWait 556 | 
ime-show-1 | 10 | 7 | 6 | 631.3 | 1042.2 | 344.9 | Choreographer#doFrame - resynced to 6039 344 > traversal 344 > draw-VRI[MainActivity] 344 > postAndWait 344 | 
ime-hide-1 | 9 | 5 | 5 | 1195.6 | 1556.9 | 598.7 | Choreographer#doFrame - resynced to 6123 598 > traversal 597 > draw-VRI[MainActivity] 597 > postAndWait 597 | 
ime-show-2 | 8 | 4 | 4 | 634.4 | 981.0 | 373.4 | Choreographer#doFrame - resynced to 6165 373 > traversal 372 > draw-VRI[MainActivity] 372 > postAndWait 368 | 
ime-hide-2 | 9 | 5 | 5 | 1037.5 | 1384.5 | 549.7 | Choreographer#doFrame - resynced to 6233 550 > traversal 550 > draw-VRI[MainActivity] 549 > postAndWait 549 | 
ime-show-3 | 7 | 3 | 3 | 787.4 | 1050.6 | 497.7 | Choreographer#doFrame - resynced to 6278 498 > traversal 497 > draw-VRI[MainActivity] 497 > postAndWait 497 | 
ime-hide-3 | 9 | 5 | 5 | 945.9 | 1320.0 | 653.1 | Choreographer#doFrame - resynced to 6337 653 > traversal 651 > draw-VRI[MainActivity] 651 > postAndWait 651 | 
ime-show-4 | 8 | 4 | 3 | 536.9 | 968.3 | 313.1 | traversal 313 > draw-VRI[MainActivity] 313 > postAndWait 313 | 
type-digits | 11 | 6 | 6 | 714.4 | 1174.0 | 458.8 | Choreographer#doFrame - resynced to 6442 458 > traversal 458 > draw-VRI[MainActivity] 458 > postAndWait 457 | 
clear-digits | 11 | 7 | 7 | 698.3 | 1055.6 | 357.7 | Choreographer#doFrame - resynced to 6490 357 > traversal 357 > draw-VRI[MainActivity] 357 > postAndWait 356 | 
ime-hide-4 | 9 | 5 | 5 | 1066.8 | 1382.7 | 562.9 | Choreographer#doFrame - resynced to 6530 560 > traversal 558 > draw-VRI[MainActivity] 558 > postAndWait 556 | 
sort-open-1 | 9 | 3 | 3 | 1549.0 | 1717.2 | 788.3 | traversal 780 > draw-VRI[Изскачащ прозорец] 352 > postAndWait 270 | msg Handler (android.app.ActivityThread$H) null: 1 96
sort-year-1 | 9 | 7 | 7 | 1201.0 | 1489.8 | 799.6 | Choreographer#doFrame - resynced to 7001 797 > traversal 794 > draw-VRI[Изскачащ прозорец] 586 > postAndWait 586 | 
sort-open-2 | 10 | 5 | 5 | 1158.6 | 1579.7 | 643.9 | Choreographer#doFrame - resynced to 7059 636 > traversal 635 > draw-VRI[Изскачащ прозорец] 462 > postAndWait 120 | msg Handler (android.app.ActivityThread$H) null: 1 221
sort-relevance-2 | 10 | 7 | 7 | 751.6 | 1179.8 | 511.5 | Choreographer#doFrame - resynced to 7414 511 > traversal 505 > draw-VRI[Изскачащ прозорец] 261 > postAndWait 261 | 
sort-open-3 | 11 | 5 | 5 | 1262.0 | 1783.4 | 803.5 | Choreographer#doFrame - resynced to 7473 801 > traversal 800 > draw-VRI[Изскачащ прозорец] 459 > postAndWait 163 | msg Handler (android.app.ActivityThread$H) null: 1 238
sort-year-3 | 9 | 7 | 7 | 982.6 | 1264.0 | 629.2 | Choreographer#doFrame - resynced to 7825 629 > traversal 627 > draw-VRI[Изскачащ прозорец] 626 > postAndWait 626 | 
filters-open-1 | 11 | 6 | 6 | 1636.2 | 2058.5 | 1000.1 | traversal 994 > draw-VRI[MainActivity] 496 > postAndWait 141 | msg Handler (android.app.ActivityThread$H) null: 1 132
filters-available-1 | 8 | 7 | 6 | 963.1 | 1141.5 | 667.6 | Choreographer#doFrame - resynced to 8092 667 > traversal 663 > draw-VRI[MainActivity] 411 > postAndWait 411 | 
filters-close-1 | 6 | 2 | 1 | 245.2 | 659.1 | 199.0 | Choreographer#doFrame - resynced to 8110 199 > traversal 199 > draw-VRI[MainActivity] 199 > postAndWait 198 | 
filters-open-2 | 10 | 5 | 5 | 1736.0 | 2220.3 | 1021.7 | Choreographer#doFrame - resynced to 8169 1016 > traversal 1014 > draw-VRI[MainActivity] 760 > postAndWait 759 | msg Handler (android.app.ActivityThread$H) null: 1 140
filters-doctype-2 | 8 | 7 | 6 | 1048.9 | 1258.4 | 735.5 | Choreographer#doFrame - resynced to 8194 733 > traversal 732 > draw-VRI[MainActivity] 477 > postAndWait 477 | 
filters-close-2 | 6 | 1 | 1 | 305.5 | 601.4 | 294.7 | Choreographer#doFrame - resynced to 8212 295 > traversal 295 > draw-VRI[MainActivity] 295 > postAndWait 288 | 
filters-open-3 | 10 | 5 | 5 | 1816.6 | 2479.3 | 974.5 | traversal 969 > draw-VRI[MainActivity] 779 > postAndWait 138 | msg Handler (android.app.ActivityThread$H) null: 1 140
filters-available-3 | 8 | 7 | 6 | 1040.3 | 1199.6 | 650.5 | Choreographer#doFrame - resynced to 8300 644 > traversal 644 > draw-VRI[MainActivity] 388 > postAndWait 388 | 
filters-close-3 | 6 | 2 | 1 | 252.2 | 645.1 | 300.9 | Choreographer#doFrame - resynced to 8312 301 > animation 301 > Recomposer:recompose 301 > Compose:onForgotten 297 > binder transaction 22 | 
filters-open-4 | 10 | 5 | 5 | 2006.9 | 2304.6 | 1139.6 | traversal 1134 > draw-VRI[MainActivity] 715 > postAndWait 257 | msg Handler (android.app.ActivityThread$H) null: 1 661
filters-clear | 8 | 7 | 6 | 1126.0 | 1299.8 | 864.9 | Choreographer#doFrame - resynced to 8405 865 > traversal 864 > draw-VRI[MainActivity] 516 > postAndWait 516 | 
filters-close-4 | 6 | 2 | 2 | 253.8 | 702.3 | 243.8 | traversal 244 > draw-VRI[MainActivity] 244 > postAndWait 243 | 
shelf-on-1 | 14 | 8 | 8 | 398.8 | 666.6 | 294.6 | traversal 292 > draw-VRI[MainActivity] 292 > Record View#draw() 152 > AndroidOwner:measureAndLayout 150 > Compose:applyChanges 63 | 
fling-shelf-1 | 69 | 54 | 52 | 504.0 | 822.2 | 270.1 | Choreographer#doFrame - resynced to 8531 270 > traversal 266 > draw-VRI[MainActivity] 266 > postAndWait 266 | 
shelf-off-1 | 14 | 8 | 8 | 649.8 | 900.9 | 352.5 | Choreographer#doFrame - resynced to 8665 352 > traversal 352 > draw-VRI[MainActivity] 352 > postAndWait 352 | 
shelf-on-2 | 16 | 8 | 7 | 327.1 | 567.5 | 185.7 | traversal 186 > draw-VRI[MainActivity] 183 > postAndWait 183 | 
fling-shelf-2 | 67 | 56 | 55 | 491.0 | 766.3 | 318.8 | Choreographer#doFrame - resynced to 8773 315 > traversal 315 > draw-VRI[MainActivity] 315 > postAndWait 314 | 
shelf-off-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
shelf-on-3 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
fling-shelf-3 | 65 | 56 | 56 | 500.3 | 881.1 | 296.5 | Choreographer#doFrame - resynced to 8905 296 > traversal 294 > draw-VRI[MainActivity] 294 > postAndWait 276 | 
shelf-off-3 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
surprise-open-1 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
surprise-close-1 | 8 | 7 | 6 | 667.1 | 1013.5 | 413.8 | Choreographer#doFrame - resynced to 9001 414 > traversal 413 > draw-VRI[MainActivity] 413 > postAndWait 409 | 
surprise-open-2 | 15 | 14 | 14 | 748.0 | 1206.6 | 541.1 | Choreographer#doFrame - resynced to 9085 540 > traversal 540 > draw-VRI[MainActivity] 540 > postAndWait 540 | 
surprise-close-2 | 5 | 3 | 3 | 485.1 | 796.2 | 351.4 | Choreographer#doFrame - resynced to 9110 350 > traversal 347 > draw-VRI[MainActivity] 347 > postAndWait 347 | 
surprise-open-3 | 13 | 13 | 13 | 612.1 | 1054.1 | 377.4 | Choreographer#doFrame - resynced to 9139 377 > traversal 377 > draw-VRI[MainActivity] 377 > postAndWait 377 | 
surprise-close-3 | 4 | 3 | 3 | 574.6 | 800.6 | 300.9 | Choreographer#doFrame - resynced to 9161 300 > traversal 299 > draw-VRI[MainActivity] 299 > postAndWait 299 | 
book-back-1 | 9 | 8 | 8 | 377.9 | 564.1 | 235.6 | Choreographer#doFrame - resynced to 9233 235 > traversal 235 > draw-VRI[MainActivity] 235 > postAndWait 234 | 
book-back-2 | 9 | 8 | 8 | 342.2 | 592.7 | 271.4 | Choreographer#doFrame - resynced to 9288 271 > traversal 271 > draw-VRI[MainActivity] 271 > postAndWait 270 | 
book-back-3 | 9 | 8 | 8 | 356.9 | 602.3 | 216.5 | Choreographer#doFrame - resynced to 9345 216 > traversal 215 > draw-VRI[MainActivity] 215 > postAndWait 215 | 
refresh-1 | 16 | 14 | 14 | 342.1 | 607.9 | 171.8 | Choreographer#doFrame - resynced to 9431 172 > traversal 171 > draw-VRI[MainActivity] 171 > postAndWait 171 | 
refresh-2 | 14 | 12 | 12 | 427.1 | 694.4 | 259.5 | Choreographer#doFrame - resynced to 9475 259 > traversal 258 > draw-VRI[MainActivity] 258 > postAndWait 258 | 
resume-1 | 1 | 1 | 1 | 472.6 | 1247.9 | 1221.2 | traversal 1218 > draw-VRI[MainActivity] 321 > postAndWait 112 | 
resume-2 | 1 | 1 | 1 | 388.9 | 1177.5 | 1074.5 | traversal 1072 > draw-VRI[MainActivity] 369 > postAndWait 99 | 
slowest UI-thread frames: filters-open-4 2007 ms, filters-open-3 1817 ms, filters-open-2 1736 ms, filters-open-1 1636 ms, sort-open-1 1549 ms, sort-open-3 1262 ms, sort-year-1 1201 ms, ime-hide-1 1196 ms, sort-open-2 1159 ms, first-install-open 1150 ms, filters-clear 1126 ms, ime-hide-4 1067 ms
