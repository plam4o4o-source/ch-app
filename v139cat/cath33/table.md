interaction | frames | ui>32 | ui max | doFrames | main CPU/frame >16 | >32 | max CPU in a frame | path in that frame (CPU ms) | worst work outside frames (CPU ms)
---|---|---|---|---|---|---|---|---|---
first-install-open | 23 | 18 | 993.4 | 40 | 3 | 2 | 67.1 |  > traversal 58 > draw 58 > Record View#draw() 58 > AndroidOwner:measureAndLayout 52 > Compose:recompose 3 | unnamed main-thread burst 7
cold-open-1 | 15 | 13 | 355.4 | 41 | 2 | 0 | 23.2 |  > traversal 18 > draw 18 > Record View#draw() 18 > AndroidOwner:measureAndLayout 16 > Compose:recompose 3 > res/oU.webp 2 > ImageDecoder#decodeDrawable 2 > ID#w=241;h=25 | unnamed main-thread burst 8
cold-open-2 | 20 | 17 | 435.4 | 45 | 2 | 0 | 18.9 |  > traversal 17 > draw 17 > Record View#draw() 17 > AndroidOwner:measureAndLayout 14 > Compose:recompose 2 | unnamed main-thread burst 6
cold-open-3 | 15 | 13 | 399.2 | 26 | 3 | 0 | 26.3 |  > traversal 23 > draw 23 > Record View#draw() 23 > AndroidOwner:measureAndLayout 17 > Compose:recompose 4 | unnamed main-thread burst 13
leave-home-1 | 11 | 10 | 468.3 | 11 | 1 | 0 | 20.0 |  > traversal 18 > draw 18 > Record View#draw() 18 > AndroidOwner:measureAndLayout 16 > Compose:recompose 3 > res/oU.webp 2 > ImageDecoder#decodeDrawable 2 > ID#w=241;h=25 | unnamed main-thread burst 4
warm-open-1 | 10 | 9 | 406.7 | 24 | 1 | 0 | 26.2 |  > traversal 24 > draw 24 > Record View#draw() 24 > AndroidOwner:measureAndLayout 22 > Compose:recompose 3 > res/oU.webp 2 > ImageDecoder#decodeDrawable 2 > ID#w=241;h=25 | unnamed main-thread burst 5
leave-home-2 | 11 | 10 | 312.1 | 11 | 1 | 0 | 19.1 |  > traversal 17 > draw 17 > Record View#draw() 17 > AndroidOwner:measureAndLayout 14 | deliverInputEvent src=0x1002 eventTimeNano=380294000000 id=0 1 > ViewPostImeInputStage id=0xc547612e 1 > AndroidOwner:onTouch 1
warm-open-2 | 10 | 9 | 394.3 | 10 | 1 | 1 | 47.0 |  > traversal 45 > draw 45 > Record View#draw() 45 > AndroidOwner:measureAndLayout 42 > TextStringSimpleNode::measure 4 | unnamed main-thread burst 1
leave-home-3 | 11 | 10 | 359.8 | 11 | 1 | 0 | 18.4 |  > traversal 16 > draw 16 > Record View#draw() 16 > AndroidOwner:measureAndLayout 14 > Compose:recompose 2 > res/oU.webp 2 > ImageDecoder#decodeDrawable 2 > ID#w=241;h=25 | deliverInputEvent src=0x1002 eventTimeNano=397529000000 id=0 1 > ViewPostImeInputStage id=0xe045d374 1
warm-open-3 | 10 | 8 | 357.6 | 10 | 1 | 0 | 24.5 |  > traversal 19 > draw 19 > Record View#draw() 19 > AndroidOwner:measureAndLayout 16 | unnamed main-thread burst 1
leave-news | 13 | 12 | 428.2 | 14 | 0 | 0 | 14.5 |  > traversal 12 > draw 12 > Record View#draw() 12 > AndroidOwner:measureAndLayout 11 > Compose:recompose 3 > res/oU.webp 2 > ImageDecoder#decodeDrawable 2 > ID#w=241;h=25 | deliverInputEvent src=0x1002 eventTimeNano=414570000000 id=0 1 > ViewPostImeInputStage id=0xfaf231dc 1 > AndroidOwner:onTouch 1
back-from-news | 11 | 9 | 394.4 | 11 | 1 | 0 | 31.5 |  > traversal 30 > draw 30 > Record View#draw() 30 > AndroidOwner:measureAndLayout 28 > TextStringSimpleNode::measure 5 > TextLayout:initLayout 5 | unnamed main-thread burst 1
leave-more | 13 | 12 | 431.2 | 15 | 0 | 0 | 13.8 |  > traversal 12 > draw 12 > Record View#draw() 12 > AndroidOwner:measureAndLayout 10 | unnamed main-thread burst 2
back-from-more | 11 | 9 | 361.8 | 12 | 1 | 0 | 20.3 |  > traversal 18 > draw 18 > Record View#draw() 18 > AndroidOwner:measureAndLayout 16 > Compose:recompose 3 > res/oU.webp 2 > ImageDecoder#decodeDrawable 2 > ID#w=241;h=25 | deliverInputEvent src=0x1002 eventTimeNano=440802000000 id=0 1
fling-list-1 | 53 | 34 | 451.7 | 58 | 0 | 0 | 7.0 |  > animation 6 > AndroidOwner:measureAndLayout 6 > Compose:applyChanges 4 | compose:lazy:prefetch:idle_frame 5 > compose:lazy:prefetch:execute:urgent 5 > compose:lazy:prefetch:compose 4 > Co
fling-list-2 | 56 | 43 | 630.9 | 65 | 3 | 2 | 38.6 |  > traversal 34 > draw 34 > Record View#draw() 34 > AndroidOwner:measureAndLayout 31 > Compose:recompose 5 > res/oU.webp 4 > ImageDecoder#decodeDrawable 4 > ID#w=241;h=25 | AndroidOwner:measureAndLayout 11
fling-list-3 | 45 | 40 | 459.3 | 63 | 0 | 0 | 3.8 |  > animation 3 > AndroidOwner:measureAndLayout 2 | compose:lazy:prefetch:execute:urgent 5 > compose:lazy:prefetch:apply 3 > PausedComposition:applyChanges 3
reselect-top-1 | 0 | 0 | 0.0 | 2 | 0 | 0 | 0.2 |  | unnamed main-thread burst 3
reselect-top-2 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | unnamed main-thread burst 1
type-1 | 9 | 0 | 1.3 | 387 | 0 | 0 | 3.1 |  > animation 3 > Recomposer:animation 3 > Choreographer#scheduleVsyncLocked 3 | unnamed main-thread burst 1
chip-author | 13 | 9 | 386.6 | 196 | 0 | 0 | 3.0 |  > animation 3 > Recomposer:animation 3 > Choreographer#scheduleVsyncLocked 3 | deliverInputEvent src=0x1002 eventTimeNano=662932000000 id=0 1
chip-title | 24 | 22 | 367.6 | 137 | 1 | 1 | 38.9 |  > traversal 32 > draw 32 > Record View#draw() 32 > AndroidOwner:measureAndLayout 28 > TextStringSimpleNode::measure 10 > TextLayout:initLayout 10 | deliverInputEvent src=0x1002 eventTimeNano=671097000000 id=0 1
chip-all | 17 | 14 | 372.4 | 120 | 1 | 0 | 20.1 |  > traversal 19 > draw 19 > Record View#draw() 19 > AndroidOwner:measureAndLayout 18 > Compose:applyChanges 4 | unnamed main-thread burst 1
clear-1 | 12 | 7 | 520.3 | 126 | 0 | 0 | 14.4 |  > traversal 12 > draw 12 > Record View#draw() 12 > AndroidOwner:measureAndLayout 11 | unnamed main-thread burst 2
type-2 | 8 | 0 | 1.2 | 26 | 0 | 0 | 0.4 |  | unnamed main-thread burst 0
type-3 | 8 | 0 | 0.9 | 25 | 0 | 0 | 0.3 |  | unnamed main-thread burst 0
erase-3 | 8 | 0 | 0.9 | 24 | 0 | 0 | 0.3 |  | unnamed main-thread burst 0
ime-show-1 | 8 | 3 | 358.6 | 24 | 0 | 0 | 0.9 |  | deliverInputEvent src=0x1002 eventTimeNano=748698000000 id=0 1
ime-hide-1 | 9 | 3 | 298.6 | 23 | 0 | 0 | 0.8 |  | unnamed main-thread burst 0
ime-show-2 | 9 | 2 | 427.5 | 24 | 0 | 0 | 0.6 |  | unnamed main-thread burst 1
ime-hide-2 | 9 | 4 | 585.8 | 20 | 0 | 0 | 0.8 |  | unnamed main-thread burst 0
ime-show-3 | 10 | 3 | 317.5 | 25 | 0 | 0 | 0.9 |  | deliverInputEvent src=0x1002 eventTimeNano=781390000000 id=0 1
ime-hide-3 | 9 | 4 | 346.7 | 21 | 0 | 0 | 1.8 |  > insets_animation 2 | unnamed main-thread burst 0
ime-show-4 | 10 | 3 | 409.2 | 23 | 0 | 0 | 0.8 |  | deliverInputEvent src=0x1002 eventTimeNano=797843000000 id=0 1
type-digits | 12 | 5 | 649.6 | 27 | 1 | 0 | 19.7 |  > traversal 17 > draw 17 > Record View#draw() 17 > AndroidOwner:measureAndLayout 15 | unnamed main-thread burst 1
clear-digits | 14 | 10 | 550.8 | 24 | 0 | 0 | 13.9 |  > traversal 11 > draw 11 > Record View#draw() 11 > AndroidOwner:measureAndLayout 10 | unnamed main-thread burst 1
ime-hide-4 | 9 | 4 | 497.4 | 20 | 0 | 0 | 0.9 |  | unnamed main-thread burst 0
sort-open-1 | 9 | 7 | 737.0 | 67 | 0 | 0 | 8.7 |  > traversal 7 > measure 2 > AndroidOwner:onMeasure 2 | MSG_WINDOW_FOCUS_CHANGED 1
sort-year-1 | 11 | 8 | 594.4 | 129 | 1 | 0 | 18.1 |  > traversal 14 > draw 14 > Record View#draw() 14 > AndroidOwner:measureAndLayout 13 > Compose:applyChanges 1 | unnamed main-thread burst 3
sort-open-2 | 11 | 7 | 512.2 | 66 | 0 | 0 | 6.0 |  > traversal 4 | unnamed main-thread burst 1
sort-relevance-2 | 13 | 9 | 555.9 | 128 | 1 | 0 | 27.3 |  > traversal 25 > draw 25 > Record View#draw() 25 > AndroidOwner:measureAndLayout 24 > TextStringSimpleNode::measure 3 | unnamed main-thread burst 3
sort-open-3 | 9 | 7 | 707.2 | 65 | 0 | 0 | 9.4 |  > traversal 7 > draw 3 > Record View#draw() 3 > AndroidOwner:draw 3 | unnamed main-thread burst 1
sort-year-3 | 12 | 8 | 412.8 | 128 | 0 | 0 | 12.0 |  > traversal 10 > draw 10 > Record View#draw() 10 > AndroidOwner:measureAndLayout 10 | unnamed main-thread burst 3
filters-open-1 | 10 | 6 | 826.0 | 16 | 1 | 0 | 30.6 |  > traversal 26 > measure 12 > AndroidOwner:onMeasure 12 | unnamed main-thread burst 4
filters-available-1 | 8 | 8 | 587.7 | 9 | 0 | 0 | 4.0 |  > animation 2 > Recomposer:recompose 2 | deliverInputEvent src=0x1002 eventTimeNano=922812000000 id=0 1
filters-close-1 | 6 | 2 | 68.5 | 20 | 0 | 0 | 3.1 |  > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 2 | MSG_WINDOW_FOCUS_CHANGED 1
filters-open-2 | 10 | 7 | 607.1 | 17 | 1 | 0 | 16.2 |  > traversal 13 > Compose:recompose 4 | unnamed main-thread burst 2
filters-doctype-2 | 8 | 7 | 831.0 | 8 | 0 | 0 | 6.8 |  > traversal 5 > draw 4 > Record View#draw() 4 > AndroidOwner:measureAndLayout 4 > TextStringSimpleNode::measure 3 | unnamed main-thread burst 1
filters-close-2 | 6 | 1 | 158.5 | 20 | 0 | 0 | 2.9 |  > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 2 | MSG_WINDOW_FOCUS_CHANGED 1
filters-open-3 | 10 | 6 | 804.5 | 15 | 1 | 0 | 17.9 |  > traversal 15 > measure 3 > AndroidOwner:onMeasure 3 | unnamed main-thread burst 1
filters-available-3 | 8 | 7 | 809.8 | 9 | 0 | 0 | 3.4 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 1
filters-close-3 | 6 | 1 | 37.1 | 20 | 0 | 0 | 2.9 |  > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 2 | MSG_WINDOW_FOCUS_CHANGED 1
filters-open-4 | 10 | 7 | 935.6 | 15 | 1 | 0 | 16.7 |  > traversal 13 > Compose:recompose 4 | unnamed main-thread burst 3
filters-clear | 8 | 7 | 945.3 | 8 | 0 | 0 | 3.8 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 1
filters-close-4 | 6 | 1 | 77.1 | 20 | 0 | 0 | 3.0 |  > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 2 | unnamed main-thread burst 1
shelf-on-1 | 16 | 6 | 272.8 | 28 | 1 | 0 | 23.2 |  > traversal 21 > draw 21 > Record View#draw() 21 > AndroidOwner:measureAndLayout 18 > TextStringSimpleNode::measure 2 | unnamed main-thread burst 2
fling-shelf-1 | 88 | 74 | 400.9 | 108 | 0 | 0 | 5.6 |  > traversal 4 > draw 4 > postAndWait 3 | unnamed main-thread burst 6
shelf-off-1 | 15 | 9 | 345.0 | 28 | 0 | 0 | 13.3 |  > traversal 10 > draw 10 > Record View#draw() 10 > AndroidOwner:measureAndLayout 8 | unnamed main-thread burst 1
shelf-on-2 | 16 | 7 | 250.0 | 29 | 0 | 0 | 12.5 |  > traversal 11 > draw 11 > Record View#draw() 11 > AndroidOwner:measureAndLayout 9 | unnamed main-thread burst 6
fling-shelf-2 | 89 | 75 | 390.5 | 113 | 0 | 0 | 5.1 |  > traversal 4 > draw 4 > Record View#draw() 4 > AndroidOwner:draw 4 | compose:lazy:prefetch:compose 8 > Compose:recompose 7
shelf-off-2 | 15 | 9 | 336.5 | 30 | 0 | 0 | 12.6 |  > traversal 9 > draw 9 > Record View#draw() 9 > AndroidOwner:measureAndLayout 8 | unnamed main-thread burst 1
shelf-on-3 | 16 | 9 | 261.5 | 27 | 1 | 0 | 17.4 |  > traversal 15 > draw 15 > Record View#draw() 15 > AndroidOwner:measureAndLayout 13 > Compose:recompose 3 | deliverInputEvent src=0x1002 eventTimeNano=1099494000000 id= 1
fling-shelf-3 | 89 | 71 | 346.8 | 110 | 0 | 0 | 3.8 |  > traversal 3 > draw 3 > Record View#draw() 3 > AndroidOwner:draw 3 | compose:lazy:prefetch:execute:urgent 3 > compose:lazy:prefetch:compose 1 > Compose:recompose 1
shelf-off-3 | 15 | 9 | 356.6 | 30 | 1 | 0 | 17.5 |  > traversal 14 > draw 14 > Record View#draw() 14 > AndroidOwner:measureAndLayout 11 | unnamed main-thread burst 1
surprise-open-1 | 25 | 21 | 696.9 | 38 | 0 | 0 | 11.3 |  > traversal 11 > draw 11 > Record View#draw() 11 > AndroidOwner:measureAndLayout 10 > Compose:recompose 4 | unnamed main-thread burst 3
surprise-again-1 | 24 | 18 | 613.9 | 38 | 0 | 0 | 3.8 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 1
surprise-close-1 | 11 | 6 | 396.9 | 23 | 0 | 0 | 11.0 |  > traversal 6 > draw 6 > Record View#draw() 6 > AndroidOwner:draw 4 | unnamed main-thread burst 2
surprise-open-2 | 25 | 19 | 777.7 | 39 | 0 | 0 | 5.8 |  > traversal 6 > draw 5 > Record View#draw() 5 > AndroidOwner:measureAndLayout 5 | unnamed main-thread burst 3
surprise-again-2 | 23 | 15 | 605.3 | 40 | 0 | 0 | 3.3 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 4
surprise-close-2 | 11 | 6 | 454.1 | 23 | 0 | 0 | 4.3 |  > traversal 3 > draw 3 > Record View#draw() 2 > AndroidOwner:measureAndLayout 2 | unnamed main-thread burst 1
surprise-open-3 | 23 | 15 | 654.0 | 39 | 0 | 0 | 4.1 |  > traversal 2 > draw 2 > Record View#draw() 2 > AndroidOwner:draw 1 | unnamed main-thread burst 1
surprise-again-3 | 24 | 17 | 673.2 | 40 | 0 | 0 | 6.5 |  > traversal 3 > draw 3 > Record View#draw() 3 > AndroidOwner:measureAndLayout 3 > TextStringSimpleNode::measure 2 | unnamed main-thread burst 3
surprise-close-3 | 11 | 6 | 349.7 | 24 | 0 | 0 | 4.7 |  > traversal 3 > draw 3 > Record View#draw() 3 > AndroidOwner:measureAndLayout 2 | unnamed main-thread burst 1
book-open-1 | 18 | 16 | 351.6 | 28 | 1 | 0 | 16.3 |  > traversal 11 > draw 10 > Record View#draw() 10 > AndroidOwner:measureAndLayout 9 > Compose:recompose 2 | deliverInputEvent src=0x1002 eventTimeNano=1258127000000 id= 2 > ViewPostImeInputStage id=0xfe2d09f5 2 > AndroidOwner:onTouch 2
book-back-1 | 8 | 7 | 384.2 | 9 | 1 | 1 | 32.9 |  > traversal 31 > draw 31 > Record View#draw() 31 > AndroidOwner:measureAndLayout 29 > Compose:recompose 7 > res/oU.webp 4 > ImageDecoder#decodeDrawable 4 > ID#w=241;h=25 | unnamed main-thread burst 1
book-open-2 | 16 | 14 | 291.8 | 19 | 1 | 0 | 18.8 |  > animation 9 > Recomposer:recompose 9 > Compose:onForgotten 4 > Compose:onForgotten 4 | unnamed main-thread burst 3
book-back-2 | 9 | 8 | 395.1 | 10 | 1 | 0 | 23.8 |  > traversal 22 > draw 22 > Record View#draw() 22 > AndroidOwner:measureAndLayout 19 > Compose:recompose 3 > res/oU.webp 2 > ImageDecoder#decodeDrawable 2 > ID#w=241;h=25 | unnamed main-thread burst 1
book-open-3 | 14 | 13 | 385.9 | 16 | 0 | 0 | 11.4 |  > traversal 9 > draw 9 > Record View#draw() 9 > AndroidOwner:measureAndLayout 5 > Compose:recompose 1 | deliverInputEvent src=0x1002 eventTimeNano=1298512000000 id= 1 > ViewPostImeInputStage id=0xd32c661d 1 > AndroidOwner:onTouch 1
book-back-3 | 8 | 6 | 429.4 | 9 | 1 | 0 | 24.9 |  > traversal 23 > draw 23 > Record View#draw() 23 > AndroidOwner:measureAndLayout 21 | unnamed main-thread burst 1
refresh-1 | 13 | 11 | 430.8 | 14 | 0 | 0 | 3.9 |  > animation 3 > Recomposer:recompose 3 > Compose:applyChanges 2 | deliverInputEvent src=0x1002 eventTimeNano=1320725000000 id= 1
refresh-2 | 13 | 11 | 415.2 | 14 | 0 | 0 | 3.3 |  > animation 2 > Recomposer:recompose 1 | unnamed main-thread burst 2
resume-1 | 1 | 1 | 215.9 | 3 | 0 | 0 | 2.5 |  > traversal 2 > draw 2 > Record View#draw() 2 > AndroidOwner:draw 1 | unnamed main-thread burst 1
resume-2 | 1 | 1 | 271.2 | 3 | 0 | 0 | 3.1 |  > traversal 3 > draw 2 > Record View#draw() 2 > AndroidOwner:draw 2 | unnamed main-thread burst 1
highest main-thread CPU in one frame: first-install-open 67 ms, warm-open-2 47 ms, chip-title 39 ms, fling-list-2 39 ms, book-back-1 33 ms, back-from-news 32 ms, filters-open-1 31 ms, sort-relevance-2 27 ms, cold-open-3 26 ms, warm-open-1 26 ms, book-back-3 25 ms, warm-open-3 24 ms, book-back-2 24 ms, shelf-on-1 23 ms
