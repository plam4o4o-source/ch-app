interaction | frames | UI>32 | UI>80 | UI max | total max | doFrame max (trace) | path in the worst doFrame | long non-frame main-thread work
---|---|---|---|---|---|---|---|---
first-install-open | 14 | 9 | 9 | 1114.3 | 1246.5 | 572.6 | Choreographer#doFrame - resynced to 1004 572 > traversal 442 > draw-VRI[MainActivity] 441 > Record View#draw() 278 > AndroidOwner:measureAndLayout 248 | AIDL::cpp::IVirtualDeviceManagerNative::getDeviceI 61
cold-open-1 | 21 | 19 | 19 | 627.9 | 796.4 | 378.2 | Choreographer#doFrame - resynced to 2241 378 > traversal 358 > draw-VRI[MainActivity] 358 > postAndWait 296 | 
cold-open-2 | 20 | 18 | 17 | 735.6 | 879.8 | 413.6 | Choreographer#doFrame - resynced to 2895 414 > traversal 402 > draw-VRI[MainActivity] 402 > postAndWait 400 | 
cold-open-3 | 19 | 18 | 18 | 580.6 | 827.6 | 303.1 | Choreographer#doFrame - resynced to 3549 300 > traversal 298 > draw-VRI[MainActivity] 298 > postAndWait 245 | dispatchInputEvent MotionEvent ACTION_UP deviceId= 62
leave-home-1 | 16 | 15 | 15 | 435.8 | 736.4 | 435.8 | traversal 434 > draw-VRI[MainActivity] 434 > postAndWait 403 | 
warm-open-1 | 8 | 7 | 7 | 523.5 | 929.7 | 302.6 | traversal 300 > draw-VRI[MainActivity] 300 > postAndWait 259 | 
leave-home-2 | 9 | 8 | 8 | 478.1 | 814.7 | 255.2 | Choreographer#doFrame - resynced to 3645 255 > traversal 255 > draw-VRI[MainActivity] 255 > postAndWait 254 | 
warm-open-2 | 9 | 8 | 8 | 570.7 | 1013.1 | 341.5 | Choreographer#doFrame - resynced to 3676 341 > traversal 341 > draw-VRI[MainActivity] 341 > postAndWait 340 | 
leave-home-3 | 10 | 8 | 8 | 407.7 | 654.3 | 243.0 | Choreographer#doFrame - resynced to 3691 240 > traversal 234 > draw-VRI[MainActivity] 234 > postAndWait 160 | 
warm-open-3 | 9 | 8 | 8 | 539.6 | 899.7 | 382.3 | Choreographer#doFrame - resynced to 3716 382 > traversal 382 > draw-VRI[MainActivity] 382 > postAndWait 381 | 
leave-news | 11 | 10 | 10 | 447.8 | 704.2 | 249.5 | Choreographer#doFrame - resynced to 3740 249 > traversal 243 > draw-VRI[MainActivity] 243 > postAndWait 242 | 
back-from-news | 9 | 8 | 8 | 438.4 | 819.1 | 377.8 | traversal 378 > draw-VRI[MainActivity] 378 > postAndWait 378 | 
leave-more | 12 | 11 | 11 | 367.7 | 545.9 | 364.0 | traversal 363 > draw-VRI[MainActivity] 363 > postAndWait 361 | 
back-from-more | 11 | 9 | 9 | 606.3 | 936.4 | 389.1 | Choreographer#doFrame - resynced to 3801 389 > traversal 389 > draw-VRI[MainActivity] 389 > postAndWait 388 | 
fling-list-1 | 52 | 45 | 43 | 769.9 | 1211.0 | 444.3 | Choreographer#doFrame - resynced to 3849 444 > traversal 444 > draw-VRI[MainActivity] 441 > postAndWait 441 | 
fling-list-2 | 66 | 57 | 55 | 702.6 | 1048.3 | 446.1 | Choreographer#doFrame - resynced to 3966 446 > traversal 442 > draw-VRI[MainActivity] 442 > postAndWait 441 | 
fling-list-3 | 60 | 49 | 47 | 566.7 | 1031.4 | 372.0 | Choreographer#doFrame - resynced to 4042 370 > traversal 363 > draw-VRI[MainActivity] 362 > postAndWait 355 | 
reselect-top-1 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
reselect-top-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
type-1 | 19 | 18 | 15 | 645.1 | 1109.0 | 368.6 | Choreographer#doFrame - resynced to 4859 368 > traversal 364 > draw-VRI[MainActivity] 364 > postAndWait 356 | 
chip-author | 22 | 20 | 20 | 531.9 | 797.4 | 289.0 | Choreographer#doFrame - resynced to 4909 286 > traversal 279 > draw-VRI[MainActivity] 279 > postAndWait 278 | 
chip-title | 24 | 23 | 23 | 428.4 | 668.7 | 238.8 | Choreographer#doFrame - resynced to 5038 239 > traversal 238 > draw-VRI[MainActivity] 238 > postAndWait 237 | 
chip-all | 18 | 15 | 14 | 289.6 | 645.3 | 263.2 | traversal 263 > draw-VRI[MainActivity] 263 > postAndWait 263 | 
clear-1 | 12 | 8 | 7 | 748.2 | 1049.3 | 467.0 | Choreographer#doFrame - resynced to 5170 466 > traversal 466 > draw-VRI[MainActivity] 466 > postAndWait 466 | 
type-2 | 15 | 10 | 10 | 615.2 | 1054.8 | 391.5 | Choreographer#doFrame - resynced to 5205 390 > traversal 387 > draw-VRI[MainActivity] 387 > postAndWait 386 | 
clear-2 | 12 | 9 | 8 | 613.3 | 1020.6 | 342.9 | Choreographer#doFrame - resynced to 5250 343 > traversal 342 > draw-VRI[MainActivity] 342 > postAndWait 342 | 
type-3 | 18 | 14 | 14 | 767.1 | 1153.2 | 438.4 | Choreographer#doFrame - resynced to 5300 438 > traversal 437 > draw-VRI[MainActivity] 437 > postAndWait 437 | Choreographer#onVsync 52920 65
erase-3 | 17 | 13 | 13 | 752.6 | 1119.5 | 531.4 | Choreographer#doFrame - resynced to 5353 529 > traversal 529 > draw-VRI[MainActivity] 529 > postAndWait 528 | 
ime-show-1 | 6 | 3 | 3 | 651.9 | 1254.5 | 513.3 | Choreographer#doFrame - resynced to 5705 513 > traversal 512 > draw-VRI[MainActivity] 512 > postAndWait 512 | 
ime-hide-1 | 8 | 4 | 4 | 1003.3 | 1485.1 | 683.2 | Choreographer#doFrame - resynced to 5780 683 > traversal 682 > draw-VRI[MainActivity] 682 > postAndWait 682 | FrameTracker#onJankDataAvailable 95
ime-show-2 | 8 | 4 | 4 | 777.9 | 1086.2 | 394.8 | Choreographer#doFrame - resynced to 5833 395 > traversal 390 > draw-VRI[MainActivity] 390 > postAndWait 390 | clientTransactionExecuted 54
ime-hide-2 | 8 | 5 | 5 | 1296.4 | 1759.5 | 859.3 | Choreographer#doFrame - resynced to 5894 859 > traversal 855 > draw-VRI[MainActivity] 855 > postAndWait 855 | 
ime-show-3 | 8 | 6 | 4 | 708.6 | 1190.0 | 425.4 | Choreographer#doFrame - resynced to 5945 425 > traversal 425 > draw-VRI[MainActivity] 425 > postAndWait 424 | 
ime-hide-3 | 8 | 4 | 4 | 1113.8 | 1478.2 | 897.1 | Choreographer#doFrame - resynced to 6002 897 > traversal 896 > draw-VRI[MainActivity] 895 > postAndWait 895 | 
ime-show-4 | 8 | 4 | 2 | 609.3 | 896.6 | 360.3 | Choreographer#doFrame - resynced to 6051 360 > traversal 360 > draw-VRI[MainActivity] 360 > postAndWait 359 | 
type-digits | 11 | 6 | 5 | 848.2 | 1337.0 | 535.4 | Choreographer#doFrame - resynced to 6106 535 > traversal 534 > draw-VRI[MainActivity] 534 > postAndWait 533 | 
clear-digits | 10 | 8 | 8 | 722.0 | 1160.7 | 488.9 | Choreographer#doFrame - resynced to 6153 489 > traversal 489 > draw-VRI[MainActivity] 489 > postAndWait 488 | 
ime-hide-4 | 9 | 5 | 5 | 996.7 | 1426.7 | 731.8 | Choreographer#doFrame - resynced to 6195 732 > traversal 724 > draw-VRI[MainActivity] 724 > postAndWait 724 | 
sort-open-1 | 9 | 5 | 5 | 1276.4 | 1620.6 | 785.6 | traversal 780 > draw-VRI[Изскачащ прозорец] 359 > postAndWait 231 | 
sort-year-1 | 9 | 7 | 7 | 852.1 | 1263.2 | 527.9 | Choreographer#doFrame - resynced to 6689 528 > traversal 527 > draw-VRI[MainActivity] 527 > postAndWait 527 | 
sort-open-2 | 10 | 3 | 3 | 1926.7 | 2023.1 | 1121.7 | traversal 1113 > draw-VRI[Изскачащ прозорец] 658 > postAndWait 237 | 
sort-relevance-2 | 8 | 8 | 8 | 1371.2 | 1687.8 | 934.1 | Choreographer#doFrame - resynced to 7081 934 > traversal 905 > draw-VRI[MainActivity] 491 > Record View#draw() 320 > AndroidOwner:measureAndLayout 316 | 
sort-open-3 | 10 | 6 | 6 | 777.2 | 1221.2 | 646.3 | traversal 643 > draw-VRI[Изскачащ прозорец] 537 > postAndWait 349 | 
sort-year-3 | 9 | 8 | 8 | 869.3 | 1272.1 | 504.2 | Choreographer#doFrame - resynced to 7507 499 > traversal 498 > draw-VRI[Изскачащ прозорец] 495 > postAndWait 495 | 
filters-open-1 | 8 | 5 | 5 | 1375.6 | 1699.9 | 857.2 | traversal 845 > draw-VRI[MainActivity] 492 > postAndWait 326 | 
filters-available-1 | 8 | 7 | 6 | 1109.9 | 1294.1 | 852.5 | Choreographer#doFrame - resynced to 7763 852 > traversal 852 > draw-VRI[MainActivity] 441 > postAndWait 441 | 
filters-close-1 | 5 | 0 | 0 | 27.5 | 524.7 | 195.5 | Choreographer#doFrame - resynced to 7777 195 > animation 195 > Recomposer:recompose 195 > Compose:onForgotten 195 > binder transaction 11 | 
filters-open-2 | 8 | 5 | 5 | 1803.6 | 2035.6 | 1017.5 | Choreographer#doFrame - resynced to 7841 1017 > traversal 1016 > draw-VRI[MainActivity] 557 > postAndWait 557 | 
filters-doctype-2 | 7 | 7 | 6 | 1108.4 | 1289.5 | 644.2 | Choreographer#doFrame - resynced to 7866 644 > traversal 644 > draw-VRI[MainActivity] 342 > postAndWait 342 | 
filters-close-2 | 5 | 2 | 2 | 229.3 | 638.4 | 304.0 | Choreographer#doFrame - resynced to 7879 304 > animation 304 > Recomposer:recompose 304 > Compose:onForgotten 303 > binder transaction 6 | 
filters-open-3 | 8 | 6 | 6 | 1223.9 | 1526.0 | 747.6 | Choreographer#doFrame - resynced to 7936 747 > traversal 746 > draw-VRI[MainActivity] 568 > postAndWait 567 | 
filters-available-3 | 8 | 7 | 6 | 1198.7 | 1372.6 | 830.8 | Choreographer#doFrame - resynced to 7961 831 > traversal 830 > draw-VRI[MainActivity] 472 > postAndWait 472 | 
filters-close-3 | 4 | 2 | 2 | 264.3 | 670.3 | 560.7 | Choreographer#doFrame - resynced to 7975 561 > animation 561 > Recomposer:recompose 561 > Compose:onForgotten 549 > binder transaction 262 | 
filters-open-4 | 8 | 5 | 5 | 1602.9 | 2036.6 | 853.1 | Choreographer#doFrame - resynced to 8027 853 > traversal 852 > draw-VRI[MainActivity] 666 > postAndWait 666 | 
filters-clear | 8 | 7 | 6 | 1170.3 | 1344.1 | 837.0 | Choreographer#doFrame - resynced to 8054 837 > traversal 837 > draw-VRI[MainActivity] 510 > postAndWait 510 | 
filters-close-4 | 5 | 1 | 1 | 296.8 | 628.4 | 321.4 | traversal 321 > draw-VRI[MainActivity] 321 > postAndWait 321 | 
shelf-on-1 | 14 | 9 | 9 | 404.9 | 676.7 | 239.6 | Choreographer#doFrame - resynced to 8135 239 > traversal 239 > draw-VRI[MainActivity] 239 > postAndWait 238 | 
fling-shelf-1 | 68 | 52 | 51 | 504.3 | 855.5 | 301.4 | traversal 301 > draw-VRI[MainActivity] 301 > postAndWait 301 | 
shelf-off-1 | 12 | 7 | 7 | 510.2 | 860.3 | 312.8 | Choreographer#doFrame - resynced to 8318 313 > traversal 312 > draw-VRI[MainActivity] 312 > postAndWait 312 | 
shelf-on-2 | 14 | 8 | 8 | 360.8 | 645.4 | 187.0 | Choreographer#doFrame - resynced to 8368 187 > traversal 187 > draw-VRI[MainActivity] 187 > postAndWait 179 | 
fling-shelf-2 | 66 | 47 | 46 | 469.7 | 814.3 | 272.8 | Choreographer#doFrame - resynced to 8428 273 > traversal 272 > draw-VRI[MainActivity] 272 > postAndWait 272 | 
shelf-off-2 | 13 | 8 | 8 | 550.4 | 817.4 | 334.8 | Choreographer#doFrame - resynced to 8544 334 > traversal 334 > draw-VRI[MainActivity] 334 > postAndWait 333 | 
shelf-on-3 | 14 | 9 | 9 | 341.0 | 636.3 | 189.4 | traversal 189 > draw-VRI[MainActivity] 189 > postAndWait 189 | 
fling-shelf-3 | 64 | 42 | 42 | 550.7 | 995.5 | 313.2 | Choreographer#doFrame - resynced to 8744 312 > traversal 312 > draw-VRI[MainActivity] 312 > postAndWait 279 | 
shelf-off-3 | 13 | 9 | 9 | 639.5 | 929.7 | 358.4 | Choreographer#doFrame - resynced to 8781 358 > traversal 358 > draw-VRI[MainActivity] 358 > postAndWait 357 | 
surprise-open-1 | 18 | 16 | 16 | 1111.9 | 1741.4 | 608.7 | Choreographer#doFrame - resynced to 8855 600 > traversal 597 > draw-VRI[MainActivity] 597 > postAndWait 589 | 
surprise-again-1 | 19 | 13 | 13 | 909.5 | 1497.1 | 666.5 | Choreographer#doFrame - resynced to 8919 664 > traversal 662 > draw-VRI[MainActivity] 662 > postAndWait 660 | 
surprise-close-1 | 9 | 4 | 4 | 463.3 | 935.9 | 306.6 | Choreographer#doFrame - resynced to 9028 306 > traversal 306 > draw-VRI[MainActivity] 306 > postAndWait 306 | 
surprise-open-2 | 19 | 17 | 16 | 1076.2 | 1764.1 | 634.7 | Choreographer#doFrame - resynced to 9096 634 > traversal 632 > draw-VRI[MainActivity] 632 > postAndWait 630 | 
surprise-again-2 | 19 | 17 | 17 | 1015.0 | 1491.5 | 587.2 | Choreographer#doFrame - resynced to 9147 587 > traversal 584 > draw-VRI[MainActivity] 584 > postAndWait 582 | 
surprise-close-2 | 9 | 5 | 5 | 783.2 | 1163.1 | 515.9 | Choreographer#doFrame - resynced to 9190 516 > traversal 515 > draw-VRI[MainActivity] 515 > postAndWait 515 | 
surprise-open-3 | 18 | 14 | 14 | 1081.6 | 1764.0 | 700.3 | Choreographer#doFrame - resynced to 9256 700 > traversal 698 > draw-VRI[MainActivity] 698 > postAndWait 690 | 
surprise-again-3 | 19 | 18 | 17 | 1110.3 | 1602.2 | 775.3 | Choreographer#doFrame - resynced to 9291 775 > traversal 774 > draw-VRI[MainActivity] 774 > postAndWait 774 | 
surprise-close-3 | 8 | 4 | 4 | 815.9 | 1046.4 | 450.6 | Choreographer#doFrame - resynced to 9344 450 > traversal 450 > draw-VRI[MainActivity] 450 > postAndWait 449 | 
book-open-1 | 13 | 11 | 11 | 417.5 | 680.1 | 359.6 | traversal 354 > draw-VRI[MainActivity] 354 > postAndWait 347 | 
book-back-1 | 7 | 7 | 7 | 691.4 | 1018.5 | 421.2 | Choreographer#doFrame - resynced to 9716 421 > traversal 420 > draw-VRI[MainActivity] 420 > postAndWait 418 | 
book-open-2 | 12 | 11 | 10 | 636.8 | 869.6 | 349.3 | Choreographer#doFrame - resynced to 9735 349 > traversal 348 > draw-VRI[MainActivity] 348 > postAndWait 347 | 
book-back-2 | 9 | 8 | 7 | 579.8 | 1021.6 | 437.2 | Choreographer#doFrame - resynced to 9761 437 > traversal 436 > draw-VRI[MainActivity] 436 > postAndWait 433 | 
book-open-3 | 12 | 10 | 10 | 450.3 | 724.6 | 292.4 | traversal 288 > draw-VRI[MainActivity] 288 > postAndWait 285 | 
book-back-3 | 8 | 7 | 7 | 618.6 | 908.0 | 359.0 | Choreographer#doFrame - resynced to 9801 359 > traversal 358 > draw-VRI[MainActivity] 358 > postAndWait 355 | 
refresh-1 | 12 | 10 | 10 | 626.6 | 913.2 | 394.7 | Choreographer#doFrame - resynced to 9841 395 > traversal 393 > draw-VRI[MainActivity] 393 > postAndWait 392 | 
refresh-2 | 11 | 9 | 9 | 500.9 | 873.5 | 289.3 | Choreographer#doFrame - resynced to 9873 289 > traversal 287 > draw-VRI[MainActivity] 287 > postAndWait 287 | 
resume-1 | 1 | 1 | 1 | 681.8 | 2013.7 | 1975.6 | Choreographer#doFrame - resynced to 1005 1976 > traversal 1974 > draw-VRI[MainActivity] 525 > postAndWait 271 | MSG_WINDOW_FOCUS_CHANGED 142
resume-2 | 1 | 1 | 1 | 548.7 | 1266.4 | 1251.2 | traversal 1251 > draw-VRI[MainActivity] 386 > postAndWait 127 | 
slowest UI-thread frames: sort-open-2 1927 ms, filters-open-2 1804 ms, filters-open-4 1603 ms, filters-open-1 1376 ms, sort-relevance-2 1371 ms, ime-hide-2 1296 ms, sort-open-1 1276 ms, filters-open-3 1224 ms, filters-available-3 1199 ms, filters-clear 1170 ms, first-install-open 1114 ms, ime-hide-3 1114 ms
