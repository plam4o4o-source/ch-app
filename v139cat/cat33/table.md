interaction | frames | UI>32 | UI>80 | UI max | total max | doFrame max (trace) | path in the worst doFrame | long non-frame main-thread work
---|---|---|---|---|---|---|---|---
first-install-open | 24 | 13 | 13 | 1407.2 | 1705.8 | 887.5 | traversal 754 > draw 754 > Record View#draw() 444 > AndroidOwner:measureAndLayout 394 > Compose:recompose 85 | binder transaction 107
cold-open-1 | 16 | 15 | 14 | 579.4 | 791.3 | 400.0 | traversal 396 > draw 396 > postAndWait 389 | 
cold-open-2 | 16 | 15 | 13 | 475.2 | 726.2 | 322.1 | traversal 313 > draw 313 > postAndWait 310 | 
cold-open-3 | 17 | 16 | 16 | 472.8 | 749.3 | 283.5 | traversal 277 > draw 277 > postAndWait 273 | binder transaction 58
leave-home-1 | 17 | 16 | 15 | 474.5 | 612.7 | 250.4 | traversal 250 > draw 250 > postAndWait 249 | 
warm-open-1 | 8 | 8 | 7 | 617.0 | 925.1 | 355.6 | traversal 355 > draw 355 > postAndWait 355 | 
leave-home-2 | 10 | 9 | 9 | 341.4 | 520.6 | 216.9 | traversal 213 > draw 213 > postAndWait 208 | 
warm-open-2 | 8 | 8 | 8 | 499.6 | 719.8 | 309.8 | traversal 309 > draw 309 > postAndWait 309 | 
leave-home-3 | 10 | 9 | 9 | 353.4 | 570.3 | 201.8 | traversal 198 > draw 198 > postAndWait 197 | 
warm-open-3 | 9 | 8 | 8 | 540.4 | 760.1 | 309.5 | traversal 309 > draw 309 > postAndWait 309 | 
leave-news | 12 | 11 | 11 | 428.9 | 578.1 | 276.6 | traversal 276 > draw 276 > postAndWait 275 | 
back-from-news | 11 | 10 | 9 | 449.8 | 709.5 | 243.3 | traversal 243 > draw 243 > postAndWait 243 | 
leave-more | 11 | 10 | 10 | 394.4 | 552.8 | 209.7 | traversal 207 > draw 207 > postAndWait 205 | 
back-from-more | 10 | 9 | 9 | 483.3 | 756.9 | 294.4 | traversal 294 > draw 294 > postAndWait 294 | 
fling-list-1 | 52 | 33 | 33 | 815.9 | 1077.6 | 547.5 | traversal 547 > draw 547 > postAndWait 546 | 
fling-list-2 | 51 | 30 | 30 | 594.0 | 837.6 | 425.9 | traversal 426 > draw 426 > postAndWait 426 | 
fling-list-3 | 60 | 49 | 49 | 852.2 | 1332.5 | 601.7 | traversal 599 > draw 599 > postAndWait 597 | 
reselect-top-1 | 9 | 6 | 5 | 532.9 | 821.2 | 323.6 | traversal 324 > draw 324 > postAndWait 323 | 
reselect-top-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
type-1 | 10 | 0 | 0 | 1.4 | 182.6 | 86.5 | traversal 86 > draw 86 > postAndWait 86 | 
chip-author | 12 | 9 | 8 | 589.2 | 826.9 | 348.7 | traversal 349 > draw 349 > postAndWait 348 | 
chip-title | 18 | 16 | 16 | 632.6 | 814.2 | 385.0 | traversal 371 > draw 371 > postAndWait 367 | 
chip-all | 15 | 11 | 11 | 540.7 | 809.4 | 345.3 | traversal 345 > draw 345 > postAndWait 345 | 
clear-1 | 10 | 7 | 6 | 614.4 | 1042.8 | 340.7 | traversal 340 > draw 340 > postAndWait 340 | 
type-2 | 8 | 0 | 0 | 0.7 | 127.7 | 0.5 | traversal 1 > draw 0 > postAndWait 0 | 
type-3 | 8 | 0 | 0 | 0.7 | 130.5 | 0.6 | traversal 1 > draw 1 > postAndWait 0 | 
erase-3 | 9 | 0 | 0 | 0.7 | 132.9 | 0.5 | traversal 0 > draw 0 > postAndWait 0 | 
ime-show-1 | 9 | 4 | 4 | 517.9 | 840.4 | 298.0 | traversal 296 > draw 296 > postAndWait 296 | 
ime-hide-1 | 8 | 5 | 4 | 623.4 | 866.0 | 382.4 | traversal 382 > draw 382 > postAndWait 382 | 
ime-show-2 | 9 | 4 | 3 | 605.8 | 792.6 | 373.6 | traversal 373 > draw 373 > postAndWait 373 | 
ime-hide-2 | 9 | 3 | 3 | 629.0 | 847.7 | 489.4 | traversal 489 > draw 489 > postAndWait 489 | 
ime-show-3 | 9 | 4 | 4 | 417.5 | 750.4 | 225.8 | traversal 225 > draw 225 > postAndWait 225 | 
ime-hide-3 | 8 | 4 | 4 | 662.2 | 875.1 | 428.9 | traversal 429 > draw 429 > postAndWait 429 | 
ime-show-4 | 8 | 3 | 2 | 445.9 | 747.9 | 305.7 | traversal 304 > draw 304 > postAndWait 304 | MSG_SHOW_INSETS 78
type-digits | 11 | 6 | 6 | 603.1 | 1063.4 | 471.3 | traversal 470 > draw 470 > postAndWait 470 | 
clear-digits | 11 | 7 | 7 | 602.8 | 920.0 | 357.5 | traversal 357 > draw 357 > postAndWait 357 | 
ime-hide-4 | 8 | 4 | 3 | 769.7 | 991.0 | 449.7 | traversal 449 > draw 449 > postAndWait 449 | 
sort-open-1 | 8 | 6 | 6 | 921.6 | 1012.0 | 680.6 | traversal 675 > draw 385 > postAndWait 291 | 
sort-year-1 | 10 | 7 | 7 | 551.0 | 759.1 | 221.1 | traversal 217 > draw 217 > postAndWait 217 | 
sort-open-2 | 9 | 6 | 6 | 604.9 | 1062.9 | 466.8 | traversal 462 > draw 219 > postAndWait 155 | 
sort-relevance-2 | 10 | 7 | 7 | 591.0 | 877.2 | 374.3 | traversal 374 > draw 374 > postAndWait 373 | 
sort-open-3 | 10 | 7 | 7 | 757.0 | 884.2 | 599.2 | traversal 593 > relayoutWindow#first=true/resize=false/v 239 > binder transaction 6 | 
sort-year-3 | 11 | 9 | 9 | 699.9 | 1018.1 | 375.6 | traversal 375 > draw 375 > postAndWait 375 | 
filters-open-1 | 8 | 6 | 6 | 1318.9 | 1491.3 | 666.0 | traversal 665 > draw 362 > postAndWait 361 | 
filters-available-1 | 8 | 8 | 7 | 1006.4 | 1178.0 | 645.8 | traversal 646 > draw 343 > postAndWait 343 | 
filters-close-1 | 5 | 2 | 0 | 61.8 | 517.4 | 351.7 | animation 352 > Recomposer:recompose 352 > Compose:onForgotten 351 > binder transaction 67 | 
filters-open-2 | 7 | 5 | 5 | 1276.3 | 1548.4 | 738.9 | traversal 738 > draw 374 > postAndWait 374 | 
filters-doctype-2 | 8 | 7 | 6 | 977.7 | 1153.8 | 617.6 | traversal 618 > draw 317 > postAndWait 317 | 
filters-close-2 | 5 | 2 | 1 | 157.2 | 502.2 | 334.1 | animation 334 > Recomposer:recompose 334 > Compose:onForgotten 333 > binder transaction 18 | 
filters-open-3 | 8 | 6 | 6 | 1259.1 | 1593.7 | 656.7 | traversal 655 > draw 486 > postAndWait 486 | 
filters-available-3 | 8 | 8 | 7 | 977.0 | 1033.1 | 518.7 | traversal 517 > draw 349 > postAndWait 346 | 
filters-close-3 | 6 | 2 | 0 | 46.5 | 463.9 | 259.8 | traversal 259 > draw 259 > postAndWait 259 | 
filters-open-4 | 8 | 5 | 5 | 1299.3 | 1515.3 | 738.3 | traversal 733 > draw 566 > postAndWait 565 | 
filters-clear | 8 | 7 | 6 | 937.9 | 1113.1 | 622.7 | traversal 622 > draw 354 > postAndWait 354 | 
filters-close-4 | 5 | 2 | 0 | 45.6 | 389.7 | 275.3 | animation 275 > Recomposer:recompose 275 > Compose:onForgotten 275 > binder transaction 3 | 
shelf-on-1 | 14 | 10 | 9 | 354.8 | 592.9 | 238.6 | traversal 239 > draw 239 > postAndWait 238 | 
fling-shelf-1 | 67 | 60 | 59 | 530.7 | 831.7 | 322.0 | traversal 322 > draw 322 > postAndWait 322 | 
shelf-off-1 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
shelf-on-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
fling-shelf-2 | 71 | 63 | 62 | 651.4 | 884.5 | 480.3 | traversal 480 > draw 480 > postAndWait 480 | deliverInputEvent src=0x1002 eventTimeNano=1123973 86
shelf-off-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
shelf-on-3 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
fling-shelf-3 | 69 | 61 | 56 | 656.8 | 927.9 | 412.8 | traversal 413 > draw 413 > postAndWait 413 | 
shelf-off-3 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
surprise-open-1 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
surprise-close-1 | 8 | 7 | 5 | 688.4 | 850.3 | 356.5 | traversal 356 > draw 356 > postAndWait 350 | 
surprise-open-2 | 17 | 15 | 15 | 1085.1 | 1543.4 | 711.2 | traversal 711 > draw 711 > postAndWait 711 | 
surprise-close-2 | 5 | 5 | 4 | 509.4 | 766.9 | 281.4 | traversal 281 > draw 281 > postAndWait 281 | 
surprise-open-3 | 15 | 14 | 14 | 834.7 | 1131.7 | 515.3 | traversal 515 > draw 515 > postAndWait 515 | binder transaction 187
surprise-close-3 | 5 | 5 | 5 | 338.6 | 506.8 | 223.6 | traversal 223 > draw 223 > postAndWait 223 | 
book-back-1 | 8 | 7 | 7 | 480.7 | 700.5 | 372.9 | traversal 372 > draw 372 > postAndWait 372 | 
book-back-2 | 8 | 7 | 7 | 389.4 | 532.5 | 238.0 | traversal 237 > draw 237 > postAndWait 236 | 
book-back-3 | 8 | 7 | 7 | 360.8 | 507.8 | 231.3 | traversal 231 > draw 231 > postAndWait 230 | 
refresh-1 | 15 | 13 | 13 | 513.0 | 757.9 | 341.9 | traversal 342 > draw 342 > postAndWait 342 | 
refresh-2 | 14 | 12 | 12 | 447.7 | 717.1 | 291.5 | traversal 291 > draw 291 > postAndWait 291 | 
resume-1 | 1 | 1 | 1 | 593.1 | 905.5 | 851.1 | traversal 851 > draw 456 > postAndWait 136 | 
resume-2 | 1 | 1 | 1 | 325.3 | 1139.8 | 1023.9 | traversal 1024 > draw 288 > postAndWait 152 | 
slowest UI-thread frames: first-install-open 1407 ms, filters-open-1 1319 ms, filters-open-4 1299 ms, filters-open-2 1276 ms, filters-open-3 1259 ms, surprise-open-2 1085 ms, filters-available-1 1006 ms, filters-doctype-2 978 ms, filters-available-3 977 ms, filters-clear 938 ms, sort-open-1 922 ms, fling-list-3 852 ms
