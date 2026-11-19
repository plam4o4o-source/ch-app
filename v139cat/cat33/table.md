interaction | frames | ui>32 | ui max | doFrames | main CPU/frame >16 | >32 | max CPU in a frame | path in that frame (CPU ms) | worst work outside frames (CPU ms)
---|---|---|---|---|---|---|---|---|---
first-install-open | 18 | 10 | 1591.8 | 17 | 4 | 3 | 89.7 |  > traversal 74 > draw 74 > Record View#draw() 74 > AndroidOwner:measureAndLayout 66 > Compose:recompose 10 | deliverInputEvent src=0x1002 eventTimeNano=161881000000 id=0 12 > ViewPostImeInputStage id=0xc4a2a22e 12 > AndroidOwner:onTouch 12
cold-open-1 | 20 | 16 | 648.6 | 37 | 3 | 1 | 43.9 |  > traversal 32 > draw 32 > Record View#draw() 32 > AndroidOwner:measureAndLayout 29 > Compose:recompose 8 > res/oU.webp 5 > ImageDecoder#decodeDrawable 5 > ID#w=241;h=25 | deliverInputEvent src=0x1002 eventTimeNano=272729000000 id=0 6 > ViewPostImeInputStage id=0xd32e2563 6 > AndroidOwner:onTouch 6
cold-open-2 | 18 | 17 | 529.9 | 44 | 2 | 0 | 28.6 |  > traversal 27 > draw 27 > Record View#draw() 27 > AndroidOwner:measureAndLayout 22 > Compose:recompose 3 | unnamed main-thread burst 8
cold-open-3 | 11 | 11 | 588.6 | 16 | 2 | 0 | 24.3 |  > traversal 22 > draw 22 > Record View#draw() 22 > AndroidOwner:measureAndLayout 18 > Compose:recompose 2 | unnamed main-thread burst 4
leave-home-1 | 10 | 9 | 446.5 | 10 | 1 | 0 | 27.9 |  > traversal 25 > draw 25 > Record View#draw() 25 > AndroidOwner:measureAndLayout 22 > Compose:recompose 4 > res/oU.webp 4 > ImageDecoder#decodeDrawable 4 > ID#w=241;h=25 | unnamed main-thread burst 2
warm-open-1 | 8 | 8 | 645.6 | 20 | 1 | 1 | 58.6 |  > traversal 54 > draw 54 > Record View#draw() 54 > AndroidOwner:measureAndLayout 47 > Compose:recompose 5 | AndroidOwner:measureAndLayout 2
leave-home-2 | 9 | 8 | 513.3 | 9 | 1 | 1 | 32.8 |  > traversal 31 > draw 31 > Record View#draw() 31 > AndroidOwner:measureAndLayout 24 > Compose:recompose 4 > res/oU.webp 3 > ImageDecoder#decodeDrawable 3 > ID#w=241;h=25 | unnamed main-thread burst 3
warm-open-2 | 9 | 8 | 501.9 | 9 | 1 | 0 | 26.4 |  > traversal 24 > draw 24 > Record View#draw() 24 > AndroidOwner:measureAndLayout 22 > Compose:recompose 5 | unnamed main-thread burst 4
leave-home-3 | 11 | 10 | 432.3 | 11 | 1 | 0 | 20.4 |  > traversal 18 > draw 18 > Record View#draw() 18 > AndroidOwner:measureAndLayout 15 | deliverInputEvent src=0x1002 eventTimeNano=411471000000 id=0 1 > ViewPostImeInputStage id=0xfb042c70 1 > AndroidOwner:onTouch 1
warm-open-3 | 9 | 8 | 471.6 | 10 | 1 | 1 | 50.7 |  > traversal 49 > draw 49 > Record View#draw() 49 > AndroidOwner:measureAndLayout 42 > Compose:recompose 4 | unnamed main-thread burst 1
leave-news | 12 | 11 | 557.3 | 12 | 2 | 0 | 23.8 |  > traversal 21 > draw 21 > Record View#draw() 21 > AndroidOwner:measureAndLayout 19 > Compose:recompose 6 > res/oU.webp 5 > ImageDecoder#decodeDrawable 5 > ID#w=241;h=25 | unnamed main-thread burst 1
back-from-news | 9 | 8 | 506.1 | 10 | 1 | 1 | 45.1 |  > traversal 43 > draw 43 > Record View#draw() 43 > AndroidOwner:measureAndLayout 39 > Compose:recompose 4 | binder transaction 2
leave-more | 12 | 11 | 452.1 | 15 | 1 | 0 | 23.8 |  > traversal 22 > draw 22 > Record View#draw() 22 > AndroidOwner:measureAndLayout 17 > Compose:recompose 3 > res/oU.webp 3 > ImageDecoder#decodeDrawable 3 > ID#w=241;h=25 | unnamed main-thread burst 4
back-from-more | 10 | 9 | 498.3 | 10 | 1 | 1 | 35.5 |  > traversal 33 > draw 33 > Record View#draw() 30 > AndroidOwner:measureAndLayout 27 > Compose:recompose 7 > res/oU.webp 6 > ImageDecoder#decodeDrawable 6 > ID#w=241;h=25 | deliverInputEvent src=0x1002 eventTimeNano=456188000000 id=0 1 > ViewPostImeInputStage id=0xe59a6c2f 1 > AndroidOwner:onTouch 1
fling-list-1 | 59 | 51 | 561.3 | 62 | 1 | 1 | 32.5 |  > traversal 31 > draw 31 > Record View#draw() 31 > AndroidOwner:measureAndLayout 29 > Compose:recompose 9 | compose:lazy:prefetch:idle_frame 5 > compose:lazy:prefetch:execute:urgent 5 > compose:lazy:prefetch:measure 4 > An
fling-list-2 | 60 | 52 | 1144.6 | 64 | 1 | 1 | 62.2 |  > traversal 59 > draw 59 > Record View#draw() 59 > AndroidOwner:measureAndLayout 53 > Compose:recompose 4 | AndroidOwner:measureAndLayout 13
fling-list-3 | 0 | 0 | 0.0 | 50 | 0 | 0 | 0.3 |  | unnamed main-thread burst 0
reselect-top-1 | 10 | 4 | 287.9 | 8 | 0 | 0 | 1.1 |  | unnamed main-thread burst 1
reselect-top-2 | 10 | 7 | 286.2 | 10 | 0 | 0 | 1.0 |  | unnamed main-thread burst 2
type-1 | 39 | 34 | 352.2 | 164 | 0 | 0 | 6.1 |  > traversal 5 > draw 5 > Record View#draw() 5 > AndroidOwner:draw 4 | unnamed main-thread burst 2
chip-author | 16 | 9 | 263.0 | 28 | 0 | 0 | 3.0 |  > traversal 3 > draw 3 > Record View#draw() 3 > AndroidOwner:draw 3 | unnamed main-thread burst 1
chip-title | 25 | 23 | 360.4 | 36 | 0 | 0 | 3.5 |  > traversal 2 > draw 2 > Record View#draw() 2 | unnamed main-thread burst 2
chip-all | 24 | 24 | 328.0 | 44 | 0 | 0 | 4.7 |  > traversal 3 > draw 2 > Record View#draw() 2 > AndroidOwner:draw 1 | unnamed main-thread burst 1
clear-1 | 14 | 9 | 365.5 | 34 | 0 | 0 | 13.2 |  > traversal 12 > draw 12 > Record View#draw() 12 > AndroidOwner:measureAndLayout 11 | unnamed main-thread burst 1
type-2 | 15 | 10 | 720.2 | 28 | 2 | 0 | 30.6 |  > traversal 28 > draw 28 > Record View#draw() 28 > AndroidOwner:measureAndLayout 23 > Compose:recompose 4 | unnamed main-thread burst 4
clear-2 | 12 | 7 | 538.0 | 24 | 1 | 1 | 34.2 |  > traversal 29 > draw 29 > Record View#draw() 29 > AndroidOwner:measureAndLayout 27 > Compose:onPositionedCallbacks 4 | unnamed main-thread burst 1
type-3 | 17 | 14 | 653.5 | 26 | 3 | 1 | 34.6 |  > traversal 29 > draw 29 > Record View#draw() 29 > AndroidOwner:measureAndLayout 27 > Compose:recompose 3 | unnamed main-thread burst 4
erase-3 | 16 | 13 | 673.1 | 29 | 2 | 0 | 29.8 |  > traversal 24 > draw 24 > Record View#draw() 24 > AndroidOwner:measureAndLayout 22 > TextStringSimpleNode::measure 5 | unnamed main-thread burst 2
ime-show-1 | 9 | 5 | 562.0 | 23 | 0 | 0 | 0.9 |  | unnamed main-thread burst 5
ime-hide-1 | 8 | 3 | 606.7 | 19 | 0 | 0 | 3.9 |  > traversal 3 > draw 3 > Record View#draw() 3 | unnamed main-thread burst 2
ime-show-2 | 9 | 4 | 592.9 | 24 | 0 | 0 | 0.7 |  | deliverInputEvent src=0x1002 eventTimeNano=786919000000 id=0 1
ime-hide-2 | 8 | 2 | 783.7 | 21 | 0 | 0 | 0.8 |  | unnamed main-thread burst 0
ime-show-3 | 9 | 4 | 615.1 | 22 | 0 | 0 | 3.4 |  > traversal 3 > draw 3 | deliverInputEvent src=0x1002 eventTimeNano=804050000000 id=0 1
ime-hide-3 | 9 | 6 | 533.6 | 21 | 0 | 0 | 2.8 |  > traversal 3 | unnamed main-thread burst 0
ime-show-4 | 9 | 4 | 608.0 | 23 | 0 | 0 | 0.8 |  | deliverInputEvent src=0x1002 eventTimeNano=821251000000 id=0 1
type-digits | 11 | 7 | 952.1 | 24 | 1 | 0 | 30.7 |  > traversal 28 > draw 28 > Record View#draw() 28 > AndroidOwner:measureAndLayout 24 > Compose:recompose 3 | unnamed main-thread burst 1
clear-digits | 10 | 7 | 708.7 | 22 | 1 | 1 | 44.4 |  > traversal 38 > draw 38 > Record View#draw() 38 > AndroidOwner:measureAndLayout 33 > TextStringSimpleNode::measure 4 | unnamed main-thread burst 2
ime-hide-4 | 9 | 4 | 700.0 | 23 | 0 | 0 | 0.7 |  | unnamed main-thread burst 0
sort-open-1 | 9 | 6 | 1066.7 | 24 | 0 | 0 | 14.2 |  > traversal 12 > draw 2 > Record View#draw() 2 > AndroidOwner:draw 2 | unnamed main-thread burst 1
sort-year-1 | 10 | 8 | 895.1 | 126 | 1 | 0 | 29.5 |  > traversal 25 > draw 25 > Record View#draw() 25 > AndroidOwner:measureAndLayout 24 > Compose:recompose 3 | unnamed main-thread burst 2
sort-open-2 | 11 | 7 | 912.1 | 37 | 0 | 0 | 8.8 |  > traversal 7 > draw 2 > Record View#draw() 2 > AndroidOwner:draw 1 | unnamed main-thread burst 1
sort-relevance-2 | 10 | 8 | 768.0 | 126 | 1 | 0 | 25.3 |  > traversal 22 > draw 22 > Record View#draw() 22 > AndroidOwner:measureAndLayout 20 > Compose:applyChanges 3 | unnamed main-thread burst 2
sort-open-3 | 9 | 7 | 749.4 | 37 | 0 | 0 | 6.7 |  > traversal 5 > measure 1 > AndroidOwner:onMeasure 1 | unnamed main-thread burst 3
sort-year-3 | 10 | 8 | 897.5 | 127 | 1 | 0 | 28.7 |  > traversal 25 > draw 25 > Record View#draw() 25 > AndroidOwner:measureAndLayout 20 | unnamed main-thread burst 1
filters-open-1 | 10 | 6 | 1157.4 | 16 | 1 | 0 | 23.6 |  > traversal 20 > Compose:recompose 6 | unnamed main-thread burst 1
filters-available-1 | 8 | 8 | 1028.5 | 8 | 0 | 0 | 3.7 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 1
filters-close-1 | 5 | 2 | 110.4 | 19 | 0 | 0 | 3.4 |  > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 3 | unnamed main-thread burst 2
filters-open-2 | 7 | 5 | 1180.1 | 14 | 1 | 0 | 18.4 |  > traversal 15 > Compose:recompose 3 | unnamed main-thread burst 1
filters-doctype-2 | 7 | 7 | 1175.9 | 7 | 0 | 0 | 4.1 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 1
filters-close-2 | 6 | 2 | 71.9 | 19 | 0 | 0 | 4.2 |  > animation 4 > Recomposer:recompose 4 > Compose:onForgotten 3 | unnamed main-thread burst 3
filters-open-3 | 9 | 6 | 1155.7 | 15 | 1 | 0 | 28.9 |  > traversal 26 > Compose:recompose 8 | unnamed main-thread burst 4
filters-available-3 | 8 | 7 | 945.2 | 7 | 0 | 0 | 4.1 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 1
filters-close-3 | 6 | 2 | 51.8 | 19 | 0 | 0 | 3.3 |  > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 2 | unnamed main-thread burst 1
filters-open-4 | 9 | 6 | 960.9 | 15 | 1 | 0 | 26.4 |  > traversal 23 > Compose:recompose 6 | deliverInputEvent src=0x1002 eventTimeNano=1026774000000 id= 1 > ViewPostImeInputStage id=0xf7ad8365 1
filters-clear | 8 | 7 | 1088.6 | 7 | 0 | 0 | 4.7 |  > animation 3 > Recomposer:recompose 3 | unnamed main-thread burst 1
filters-close-4 | 5 | 2 | 45.2 | 20 | 0 | 0 | 3.2 |  > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 2 | MSG_WINDOW_FOCUS_CHANGED 1
shelf-on-1 | 14 | 10 | 390.5 | 27 | 1 | 0 | 20.4 |  > traversal 18 > draw 18 > Record View#draw() 18 > AndroidOwner:measureAndLayout 16 > Compose:recompose 1 | AndroidOwner:measureAndLayout 1
fling-shelf-1 | 64 | 50 | 535.6 | 84 | 0 | 0 | 5.5 |  > traversal 4 > draw 4 > Record View#draw() 4 > AndroidOwner:draw 4 | compose:lazy:prefetch:compose 4 > Compose:recompose 4
shelf-off-1 | 12 | 8 | 524.9 | 23 | 1 | 1 | 51.2 |  > traversal 40 > draw 40 > Record View#draw() 40 > AndroidOwner:measureAndLayout 33 > TextStringSimpleNode::measure 9 | unnamed main-thread burst 1
shelf-on-2 | 14 | 8 | 386.6 | 25 | 1 | 0 | 19.3 |  > traversal 17 > draw 17 > Record View#draw() 17 > AndroidOwner:measureAndLayout 15 > Compose:recompose 1 | unnamed main-thread burst 1
fling-shelf-2 | 63 | 55 | 577.6 | 78 | 1 | 1 | 42.4 |  > traversal 40 > draw 40 > Record View#draw() 39 > AndroidOwner:measureAndLayout 35 > Compose:recompose 3 | unnamed main-thread burst 3
shelf-off-2 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | unnamed main-thread burst 0
shelf-on-3 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | unnamed main-thread burst 0
fling-shelf-3 | 59 | 53 | 659.2 | 64 | 0 | 0 | 3.4 |  > traversal 3 | deliverInputEvent src=0x1002 eventTimeNano=1149722000000 id= 1
shelf-off-3 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | unnamed main-thread burst 0
surprise-open-1 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | unnamed main-thread burst 0
surprise-close-1 | 9 | 8 | 509.8 | 10 | 2 | 2 | 60.5 |  > traversal 56 > draw 56 > Record View#draw() 56 > AndroidOwner:measureAndLayout 53 > Compose:recompose 7 | unnamed main-thread burst 2
surprise-open-2 | 17 | 16 | 1589.9 | 19 | 0 | 0 | 7.2 |  > traversal 7 > draw 7 > Record View#draw() 7 > AndroidOwner:measureAndLayout 6 > Compose:recompose 1 | unnamed main-thread burst 12
surprise-close-2 | 5 | 5 | 333.8 | 8 | 0 | 0 | 14.4 |  > traversal 8 > draw 8 > Record View#draw() 8 > AndroidOwner:draw 6 | unnamed main-thread burst 3
surprise-open-3 | 15 | 15 | 788.0 | 20 | 0 | 0 | 7.1 |  > traversal 7 > draw 7 > Record View#draw() 7 > AndroidOwner:measureAndLayout 6 | unnamed main-thread burst 4
surprise-close-3 | 5 | 5 | 356.3 | 8 | 0 | 0 | 13.1 |  > traversal 11 > draw 11 > Record View#draw() 11 > AndroidOwner:measureAndLayout 6 > TextAnnotatedStringNode:measure 4 | AndroidOwner:outOfFrameExecutor 4 > Compose:deactivate 4 > Compose:onForgotten 3
book-back-1 | 8 | 7 | 296.8 | 9 | 1 | 1 | 61.3 |  > traversal 58 > draw 57 > Record View#draw() 57 > AndroidOwner:measureAndLayout 51 > TextStringSimpleNode::measure 24 | unnamed main-thread burst 1
book-back-2 | 8 | 7 | 335.9 | 9 | 1 | 1 | 36.7 |  > traversal 32 > draw 32 > Record View#draw() 32 > AndroidOwner:measureAndLayout 23 > TextStringSimpleNode::measure 3 > TextLayout:initLayout 3 | unnamed main-thread burst 1
book-back-3 | 8 | 7 | 354.3 | 9 | 1 | 1 | 40.5 |  > traversal 39 > draw 39 > Record View#draw() 39 > AndroidOwner:measureAndLayout 36 > Compose:recompose 4 | unnamed main-thread burst 3
refresh-1 | 15 | 13 | 457.5 | 16 | 0 | 0 | 2.4 |  > animation 2 > Recomposer:animation 2 | unnamed main-thread burst 1
refresh-2 | 14 | 12 | 417.5 | 16 | 0 | 0 | 4.7 |  > traversal 3 | unnamed main-thread burst 1
resume-1 | 1 | 1 | 459.2 | 3 | 0 | 0 | 2.5 |  > traversal 2 > draw 2 > Record View#draw() 1 > AndroidOwner:draw 1 | unnamed main-thread burst 3
resume-2 | 1 | 1 | 386.6 | 3 | 0 | 0 | 6.4 |  > traversal 6 > draw 6 > Record View#draw() 6 > AndroidOwner:draw 5 | unnamed main-thread burst 1
highest main-thread CPU in one frame: first-install-open 90 ms, fling-list-2 62 ms, book-back-1 61 ms, surprise-close-1 60 ms, warm-open-1 59 ms, shelf-off-1 51 ms, warm-open-3 51 ms, back-from-news 45 ms, clear-digits 44 ms, cold-open-1 44 ms, fling-shelf-2 42 ms, book-back-3 40 ms, book-back-2 37 ms, back-from-more 36 ms
