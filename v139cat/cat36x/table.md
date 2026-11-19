interaction | frames | ui>32 | ui max | doFrames | main CPU/frame >16 | >32 | max CPU in a frame | path in that frame (CPU ms) | worst work outside frames (CPU ms)
---|---|---|---|---|---|---|---|---|---
first-install-open | 20 | 12 | 1835.3 | 23 | 8 | 8 | 90.8 |  > Choreographer#doFrame - resynced to 12447 in  91 > traversal 73 > draw-VRI[MainActivity] 73 > Record View#draw() 73 > AndroidOwner:measureAndLayout 67 > Compose:recomp | dispatchInputEvent MotionEvent ACTION_UP deviceId=-1 source= 7 > deliverInputEvent src=0x1002 eventTimeNano=17 7 > ViewPostImeInputStage id=0x
cold-open-1 | 15 | 13 | 496.5 | 43 | 3 | 0 | 30.4 |  > Choreographer#doFrame - resynced to 22382 in  30 > traversal 25 > draw-VRI[MainActivity] 25 > Record View#draw() 25 > AndroidOwner:measureAndLayout 20 | unnamed main-thread burst 3
cold-open-2 | 12 | 12 | 423.8 | 45 | 3 | 2 | 41.4 |  > Choreographer#doFrame - resynced to 25822 in  41 > traversal 40 > draw-VRI[MainActivity] 40 > Record View#draw() 40 > AndroidOwner:measureAndLayout 35 > Compose:recomp | unnamed main-thread burst 3
cold-open-3 | 12 | 11 | 490.8 | 24 | 3 | 0 | 19.2 |  > Choreographer#doFrame - resynced to 32214 in  19 > traversal 15 > draw-VRI[MainActivity] 15 > Record View#draw() 15 > AndroidOwner:measureAndLayout 13 > Compose:recomp | unnamed main-thread burst 4
leave-home-1 | 9 | 8 | 439.1 | 13 | 1 | 0 | 20.2 |  > traversal 18 > draw-VRI[MainActivity] 18 > Record View#draw() 18 > AndroidOwner:measureAndLayout 15 > Compose:recompose 3 > res/oU.webp 3 > ImageDecoder#decodeDrawable | unnamed main-thread burst 1
warm-open-1 | 9 | 8 | 546.7 | 33 | 1 | 1 | 38.9 |  > traversal 37 > draw-VRI[MainActivity] 37 > Record View#draw() 37 > AndroidOwner:measureAndLayout 31 > Compose:recompose 4 | unnamed main-thread burst 1
leave-home-2 | 10 | 9 | 464.4 | 16 | 1 | 0 | 29.1 |  > traversal 27 > draw-VRI[MainActivity] 27 > Record View#draw() 27 > AndroidOwner:measureAndLayout 23 > Compose:applyChanges 3 | unnamed main-thread burst 3
warm-open-2 | 9 | 8 | 623.5 | 32 | 1 | 1 | 58.7 |  > traversal 52 > draw-VRI[MainActivity] 52 > Record View#draw() 52 > AndroidOwner:measureAndLayout 43 > Compose:recompose 7 > res/oU.webp 6 > ImageDecoder#decodeDrawable | AndroidOwner:measureAndLayout 2
leave-home-3 | 9 | 8 | 449.7 | 13 | 1 | 0 | 28.9 |  > traversal 27 > draw-VRI[MainActivity] 27 > Record View#draw() 27 > AndroidOwner:measureAndLayout 21 > Compose:applyChanges 3 | unnamed main-thread burst 1
warm-open-3 | 9 | 8 | 494.3 | 14 | 1 | 1 | 44.5 |  > traversal 43 > draw-VRI[MainActivity] 42 > Record View#draw() 42 > AndroidOwner:measureAndLayout 38 > Compose:recompose 4 | unnamed main-thread burst 1
leave-news | 15 | 14 | 583.6 | 30 | 3 | 2 | 33.8 |  > Choreographer#doFrame - resynced to 33897 in  34 > traversal 19 > draw-VRI[MainActivity] 19 > Record View#draw() 19 > AndroidOwner:measureAndLayout 17 > Compose:recomp | unnamed main-thread burst 2
back-from-news | 9 | 8 | 593.6 | 15 | 1 | 1 | 35.0 |  > traversal 32 > draw-VRI[MainActivity] 32 > Record View#draw() 32 > AndroidOwner:measureAndLayout 29 > TextStringSimpleNode::measure 3 | unnamed main-thread burst 3
leave-more | 11 | 10 | 382.3 | 21 | 1 | 0 | 21.4 |  > traversal 19 > draw-VRI[MainActivity] 19 > Record View#draw() 19 > AndroidOwner:measureAndLayout 15 > Compose:recompose 2 | unnamed main-thread burst 2
back-from-more | 9 | 8 | 457.5 | 16 | 1 | 1 | 32.5 |  > traversal 31 > draw-VRI[MainActivity] 31 > Record View#draw() 31 > AndroidOwner:measureAndLayout 26 > Compose:recompose 3 > res/oU.webp 3 > ImageDecoder#decodeDrawable | unnamed main-thread burst 1
fling-list-1 | 48 | 40 | 717.8 | 88 | 2 | 2 | 38.0 |  > Choreographer#doFrame - resynced to 34897 in  38 > animation 28 > Recomposer:animation 28 > AndroidOwner:measureAndLayout 28 | compose:lazy:prefetch:idle_frame 3 > compose:lazy:prefetch:execute:urgent 2 > compose:lazy:prefetch:measure 1 > An
fling-list-2 | 59 | 50 | 988.1 | 109 | 5 | 4 | 42.3 |  > Choreographer#doFrame - resynced to 36576 in  42 > traversal 38 > draw-VRI[MainActivity] 38 > Record View#draw() 38 > AndroidOwner:measureAndLayout 29 > TextStringSimp | AndroidOwner:measureAndLayout 27 > Compose:recompose 3
fling-list-3 | 64 | 58 | 691.2 | 117 | 2 | 0 | 17.5 |  > Choreographer#doFrame - resynced to 36990 in  17 > traversal 11 > draw-VRI[MainActivity] 10 > Record View#draw() 10 > AndroidOwner:measureAndLayout 6 > Compose:applyCh | compose:lazy:prefetch:idle_frame 4 > compose:lazy:prefetch:execute:urgent 4 > compose:lazy:prefetch:compose 2 > Co
reselect-top-1 | 0 | 0 | 0.0 | 1 | 0 | 0 | 0.0 |  | unnamed main-thread burst 1
reselect-top-2 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
type-1 | 35 | 31 | 369.9 | 183 | 0 | 0 | 7.7 |  > traversal 5 > draw-VRI[MainActivity] 5 > Record View#draw() 5 > AndroidOwner:measureAndLayout 5 | unnamed main-thread burst 3
chip-author | 14 | 8 | 302.5 | 31 | 0 | 0 | 1.7 |  > traversal 1 > draw-VRI[MainActivity] 1 > Record View#draw() 1 > AndroidOwner:draw 1 | unnamed main-thread burst 3
chip-title | 25 | 24 | 365.8 | 62 | 0 | 0 | 7.0 |  > Choreographer#doFrame - resynced to 48051 in  7 > traversal 4 > draw-VRI[MainActivity] 4 > Record View#draw() 4 > AndroidOwner:draw 4 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
chip-all | 25 | 25 | 458.2 | 80 | 0 | 0 | 4.5 |  > traversal 2 > draw-VRI[MainActivity] 2 > Record View#draw() 2 > AndroidOwner:draw 1 | unnamed main-thread burst 3
clear-1 | 16 | 10 | 354.6 | 58 | 0 | 0 | 13.0 |  > traversal 12 > draw-VRI[MainActivity] 12 > Record View#draw() 12 > AndroidOwner:measureAndLayout 10 | unnamed main-thread burst 4
type-2 | 15 | 10 | 618.0 | 38 | 4 | 2 | 34.4 |  > Choreographer#doFrame - resynced to 50468 in  34 > traversal 30 > draw-VRI[MainActivity] 30 > Record View#draw() 30 > AndroidOwner:measureAndLayout 26 > TextStringSimp | unnamed main-thread burst 5
clear-2 | 12 | 8 | 650.3 | 30 | 2 | 0 | 16.3 |  > Choreographer#doFrame - resynced to 50967 in  16 > traversal 14 > draw-VRI[MainActivity] 14 > Record View#draw() 14 > AndroidOwner:measureAndLayout 11 | unnamed main-thread burst 1
type-3 | 18 | 16 | 735.2 | 44 | 6 | 2 | 40.7 |  > Choreographer#doFrame - resynced to 51357 in  41 > traversal 34 > draw-VRI[MainActivity] 34 > Record View#draw() 34 > AndroidOwner:measureAndLayout 32 > Compose:recomp | unnamed main-thread burst 5
erase-3 | 16 | 13 | 715.4 | 40 | 4 | 0 | 22.8 |  > Choreographer#doFrame - resynced to 51949 in  22 > traversal 18 > draw-VRI[MainActivity] 18 > Record View#draw() 17 > AndroidOwner:measureAndLayout 15 | unnamed main-thread burst 4
ime-show-1 | 11 | 6 | 784.4 | 22 | 0 | 0 | 1.9 |  > animation 1 | unnamed main-thread burst 3
ime-hide-1 | 8 | 5 | 1046.7 | 25 | 0 | 0 | 4.3 |  > Choreographer#doFrame - resynced to 55305 in  4 > animation 3 > notifyAnimEnd-ValueAnimator 3 | unnamed main-thread burst 1
ime-show-2 | 8 | 5 | 651.4 | 25 | 0 | 0 | 4.1 |  > Choreographer#doFrame - resynced to 56030 in  4 > animation 3 > notifyAnimEnd-ValueAnimator 3 > AIDL::cpp::ISurfaceComposer::removeJankListen 3 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
ime-hide-2 | 9 | 6 | 893.8 | 25 | 0 | 0 | 0.9 |  | unnamed main-thread burst 1
ime-show-3 | 8 | 4 | 658.9 | 24 | 0 | 0 | 1.2 |  > Choreographer#doFrame - resynced to 57160 in  1 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
ime-hide-3 | 8 | 4 | 1022.9 | 24 | 0 | 0 | 1.3 |  > Choreographer#doFrame - resynced to 57472 in  1 | unnamed main-thread burst 3
ime-show-4 | 7 | 4 | 826.1 | 26 | 0 | 0 | 3.8 |  > Choreographer#doFrame - resynced to 58180 in  4 > insets_animation 3 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
type-digits | 11 | 7 | 714.9 | 30 | 1 | 0 | 19.0 |  > traversal 17 > draw-VRI[MainActivity] 17 > Record View#draw() 17 > AndroidOwner:measureAndLayout 14 | unnamed main-thread burst 2
clear-digits | 11 | 7 | 638.9 | 29 | 0 | 0 | 14.9 |  > Choreographer#doFrame - resynced to 59061 in  15 > traversal 13 > draw-VRI[MainActivity] 13 > Record View#draw() 13 > AndroidOwner:measureAndLayout 11 | Choreographer#onVsync 59098 3
ime-hide-4 | 9 | 5 | 1017.7 | 24 | 0 | 0 | 1.1 |  > traversal 1 | unnamed main-thread burst 1
sort-open-1 | 11 | 6 | 1324.3 | 26 | 0 | 0 | 8.8 |  > traversal 7 > measure 2 > AndroidOwner:onMeasure 2 | unnamed main-thread burst 1
sort-year-1 | 9 | 7 | 801.8 | 128 | 2 | 0 | 22.0 |  > Choreographer#doFrame - resynced to 64317 in  22 > traversal 18 > draw-VRI[MainActivity] 18 > Record View#draw() 18 > AndroidOwner:measureAndLayout 12 | unnamed main-thread burst 1
sort-open-2 | 9 | 5 | 1117.6 | 25 | 0 | 0 | 6.4 |  > traversal 5 > measure 1 > AndroidOwner:onMeasure 1 | unnamed main-thread burst 1
sort-relevance-2 | 9 | 7 | 925.7 | 127 | 2 | 0 | 20.8 |  > Choreographer#doFrame - resynced to 68346 in  21 > traversal 18 > draw-VRI[MainActivity] 18 > Record View#draw() 17 > AndroidOwner:measureAndLayout 12 | unnamed main-thread burst 1
sort-open-3 | 9 | 6 | 1121.1 | 27 | 0 | 0 | 6.9 |  > traversal 6 > measure 2 > AndroidOwner:onMeasure 2 | unnamed main-thread burst 2
sort-year-3 | 9 | 8 | 907.8 | 129 | 2 | 2 | 34.0 |  > Choreographer#doFrame - resynced to 72332 in  34 > traversal 28 > draw-VRI[MainActivity] 28 > Record View#draw() 28 > AndroidOwner:measureAndLayout 23 > TextStringSimp | unnamed main-thread burst 1
filters-open-1 | 9 | 6 | 1468.9 | 21 | 1 | 1 | 41.3 |  > traversal 35 > Compose:recompose 15 > Lm96; 1 | unnamed main-thread burst 3
filters-available-1 | 9 | 7 | 1187.8 | 12 | 0 | 0 | 5.6 |  > Choreographer#doFrame - resynced to 74944 in  6 > animation 4 > Recomposer:recompose 4 > Compose:recompose 2 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
filters-close-1 | 5 | 2 | 244.8 | 23 | 0 | 0 | 3.7 |  > Choreographer#doFrame - resynced to 75110 in  4 > animation 4 > Recomposer:recompose 4 > Compose:onForgotten 3 | unnamed main-thread burst 3
filters-open-2 | 9 | 5 | 1953.9 | 17 | 1 | 0 | 17.0 |  > traversal 14 > Compose:recompose 4 | unnamed main-thread burst 1
filters-doctype-2 | 9 | 7 | 1275.9 | 12 | 0 | 0 | 3.5 |  > animation 2 > Recomposer:recompose 2 | unnamed main-thread burst 1
filters-close-2 | 5 | 2 | 65.9 | 24 | 0 | 0 | 3.2 |  > Choreographer#doFrame - resynced to 76153 in  3 > animation 3 > Recomposer:recompose 3 > Compose:onForgotten 2 | unnamed main-thread burst 4
filters-open-3 | 9 | 5 | 1969.3 | 19 | 1 | 0 | 19.1 |  > traversal 16 > measure 3 > AndroidOwner:onMeasure 3 | unnamed main-thread burst 3
filters-available-3 | 8 | 7 | 1152.3 | 13 | 0 | 0 | 4.2 |  > Choreographer#doFrame - resynced to 77043 in  4 > traversal 2 > draw-VRI[MainActivity] 1 > Record View#draw() 1 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
filters-close-3 | 5 | 2 | 284.3 | 23 | 0 | 0 | 7.1 |  > Choreographer#doFrame - resynced to 77215 in  7 > animation 7 > Recomposer:recompose 7 > Compose:onForgotten 6 | unnamed main-thread burst 4
filters-open-4 | 10 | 5 | 1968.2 | 18 | 1 | 0 | 19.7 |  > traversal 17 > Compose:recompose 6 | unnamed main-thread burst 3
filters-clear | 8 | 7 | 1130.7 | 12 | 0 | 0 | 5.5 |  > animation 3 > Recomposer:recompose 3 > Compose:recompose 1 | unnamed main-thread burst 1
filters-close-4 | 5 | 2 | 334.5 | 25 | 0 | 0 | 6.0 |  > AIDL::cpp::IDisplayEventConnection::getLatest 3 | unnamed main-thread burst 4
shelf-on-1 | 13 | 9 | 496.3 | 31 | 0 | 0 | 11.6 |  > traversal 10 > draw-VRI[MainActivity] 10 > Record View#draw() 10 > AndroidOwner:measureAndLayout 8 | unnamed main-thread burst 1
fling-shelf-1 | 68 | 51 | 528.5 | 132 | 0 | 0 | 6.7 |  > Choreographer#doFrame - resynced to 79910 in  7 > traversal 4 > draw-VRI[MainActivity] 3 > Record View#draw() 3 > AndroidOwner:draw 3 | unnamed main-thread burst 3
shelf-off-1 | 12 | 7 | 590.3 | 29 | 0 | 0 | 11.1 |  > traversal 9 > draw-VRI[MainActivity] 9 > Record View#draw() 9 > AndroidOwner:measureAndLayout 7 | unnamed main-thread burst 1
shelf-on-2 | 13 | 7 | 404.3 | 33 | 0 | 0 | 13.6 |  > traversal 12 > draw-VRI[MainActivity] 11 > Record View#draw() 11 > AndroidOwner:measureAndLayout 10 | unnamed main-thread burst 1
fling-shelf-2 | 68 | 47 | 459.9 | 134 | 0 | 0 | 12.0 |  > animation 9 > AndroidOwner:measureAndLayout 9 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 5 > deliverInputEvent src=0x1002 eventTimeNano=11 5 > ViewPostImeInputStage id=0x
shelf-off-2 | 13 | 8 | 654.3 | 30 | 0 | 0 | 10.9 |  > traversal 9 > draw-VRI[MainActivity] 9 > Record View#draw() 9 > AndroidOwner:measureAndLayout 7 | unnamed main-thread burst 1
shelf-on-3 | 14 | 8 | 361.2 | 31 | 0 | 0 | 10.6 |  > traversal 9 > draw-VRI[MainActivity] 9 > Record View#draw() 9 > AndroidOwner:measureAndLayout 7 | unnamed main-thread burst 0
fling-shelf-3 | 60 | 48 | 551.1 | 120 | 5 | 0 | 21.3 |  > animation 15 > Recomposer:recompose 15 > Compose:onRemembered 4 | unnamed main-thread burst 7
shelf-off-3 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | unnamed main-thread burst 0
surprise-open-1 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | unnamed main-thread burst 1
surprise-close-1 | 9 | 8 | 502.5 | 18 | 3 | 1 | 48.7 |  > traversal 46 > draw-VRI[MainActivity] 46 > Record View#draw() 46 > AndroidOwner:measureAndLayout 42 > Compose:recompose 9 > res/oU.webp 3 > ImageDecoder#decodeDrawable | unnamed main-thread burst 2
surprise-open-2 | 16 | 14 | 865.2 | 32 | 2 | 0 | 17.8 |  > Choreographer#doFrame - resynced to 85544 in  18 > traversal 17 > draw-VRI[MainActivity] 17 > Record View#draw() 17 > AndroidOwner:measureAndLayout 17 > Compose:recomp | unnamed main-thread burst 2
surprise-close-2 | 5 | 4 | 604.6 | 13 | 0 | 0 | 8.5 |  > traversal 7 > draw-VRI[MainActivity] 7 > Record View#draw() 7 > AndroidOwner:measureAndLayout 6 > TextAnnotatedStringNode:measure 3 > TextLayout:initLayout 3 | unnamed main-thread burst 3
surprise-open-3 | 15 | 14 | 750.7 | 32 | 0 | 0 | 12.2 |  > Choreographer#doFrame - resynced to 86104 in  12 > traversal 12 > draw-VRI[MainActivity] 12 > Record View#draw() 12 > AndroidOwner:measureAndLayout 11 > Compose:onPosi | unnamed main-thread burst 3
surprise-close-3 | 6 | 4 | 431.9 | 13 | 0 | 0 | 9.7 |  > traversal 8 > draw-VRI[MainActivity] 8 > Record View#draw() 8 > AndroidOwner:measureAndLayout 6 | AndroidOwner:outOfFrameExecutor 1 > Compose:deactivate 1
book-back-1 | 8 | 7 | 359.5 | 15 | 1 | 0 | 25.5 |  > traversal 23 > draw-VRI[MainActivity] 23 > Record View#draw() 23 > AndroidOwner:measureAndLayout 19 > Compose:recompose 3 > res/oU.webp 3 > ImageDecoder#decodeDrawable | unnamed main-thread burst 1
book-back-2 | 8 | 6 | 414.4 | 15 | 1 | 0 | 25.0 |  > traversal 23 > draw-VRI[MainActivity] 23 > Record View#draw() 23 > AndroidOwner:measureAndLayout 19 > Compose:recompose 4 > res/oU.webp 3 > ImageDecoder#decodeDrawable | unnamed main-thread burst 1
book-back-3 | 8 | 7 | 372.6 | 15 | 1 | 1 | 46.0 |  > traversal 44 > draw-VRI[MainActivity] 44 > Record View#draw() 44 > AndroidOwner:measureAndLayout 38 > Run checkpoint function 9 > Marking thread roots 9 | unnamed main-thread burst 1
refresh-1 | 14 | 12 | 407.7 | 26 | 0 | 0 | 1.9 |  > Choreographer#doFrame - resynced to 88964 in  2 > animation 1 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1 > deliverInputEvent src=0x1002 eventTimeNano=14 1 > ViewPostImeInputStage id=0x
refresh-2 | 13 | 11 | 470.8 | 24 | 0 | 0 | 1.9 |  > Choreographer#doFrame - resynced to 89421 in  2 > animation 1 | dispatchInputEvent MotionEvent ACTION_DOWN deviceId=-1 sourc 1
resume-1 | 1 | 1 | 503.4 | 4 | 0 | 0 | 8.7 |  > traversal 9 > draw-VRI[MainActivity] 8 > Record View#draw() 8 > AndroidOwner:draw 8 | clientTransactionExecuted 1
resume-2 | 1 | 0 | 21.0 | 4 | 0 | 0 | 3.2 |  > traversal 3 > draw-VRI[MainActivity] 2 > Record View#draw() 2 > AndroidOwner:draw 2 | unnamed main-thread burst 3
highest main-thread CPU in one frame: first-install-open 91 ms, warm-open-2 59 ms, surprise-close-1 49 ms, book-back-3 46 ms, warm-open-3 44 ms, fling-list-2 42 ms, cold-open-2 41 ms, filters-open-1 41 ms, type-3 41 ms, warm-open-1 39 ms, fling-list-1 38 ms, back-from-news 35 ms, type-2 34 ms, sort-year-3 34 ms
