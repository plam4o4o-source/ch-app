interaction | frames | ui>32 | ui max | doFrames | main CPU/frame >16 | >32 | max CPU in a frame | path in that frame (CPU ms) | worst work outside frames (CPU ms)
---|---|---|---|---|---|---|---|---|---
first-install-open | 18 | 18 | 265.2 | 56 | 30 | 28 | 169.3 |  > traversal 133 > draw 133 > Record View#draw() 77 > AndroidOwner:measureAndLayout 67 > Compose:recompose 8 > res/oU.webp 2 > decodeBitmap 2 | AndroidOwner:onTouch 17 > Lock contention on Jit code cache (owner tid: 10
cold-open-1 | 23 | 23 | 237.9 | 59 | 34 | 34 | 155.6 |  > traversal 140 > draw 140 > Record View#draw() 63 > AndroidOwner:measureAndLayout 59 > TextStringSimpleNode::measure 5 | AndroidOwner:onTouch 2
cold-open-2 | 23 | 22 | 239.9 | 56 | 33 | 33 | 175.4 |  > traversal 175 > draw 175 | AndroidOwner:onTouch 7
cold-open-3 | 21 | 20 | 254.2 | 57 | 33 | 33 | 149.2 |  > traversal 149 > draw 149 | AndroidOwner:measureAndLayout 5
leave-home-1 | 33 | 24 | 239.9 | 63 | 29 | 28 | 159.1 |  > traversal 159 > draw 159 | AndroidOwner:onTouch 3
warm-open-1 | 14 | 13 | 229.0 | 50 | 24 | 23 | 179.5 |  > traversal 177 > draw 177 > Record View#draw() 15 > AndroidOwner:measureAndLayout 15 | AndroidOwner:onTouch 1
leave-home-2 | 33 | 23 | 270.3 | 58 | 26 | 24 | 167.0 |  > traversal 166 > draw 166 | AndroidOwner:onTouch 2
warm-open-2 | 13 | 11 | 287.5 | 47 | 21 | 21 | 195.9 |  > traversal 195 > draw 195 | AndroidOwner:onTouch 3
leave-home-3 | 33 | 26 | 278.6 | 61 | 29 | 28 | 168.1 |  > traversal 167 > draw 167 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 | AndroidOwner:onTouch 1
warm-open-3 | 13 | 12 | 212.8 | 49 | 21 | 21 | 131.3 |  > traversal 131 > draw 130 | AndroidOwner:onTouch 1
leave-news | 14 | 13 | 195.5 | 14 | 13 | 13 | 113.9 |  > traversal 113 > draw 113 | AndroidOwner:onTouch 3
back-from-news | 11 | 10 | 227.1 | 12 | 10 | 8 | 152.6 |  > traversal 152 > draw 152 | AndroidOwner:onTouch 1
leave-more | 16 | 14 | 186.2 | 17 | 14 | 13 | 110.6 |  > traversal 109 > draw 109 > Record View#draw() 1 | FullSuspendCheck 2
back-from-more | 14 | 8 | 195.4 | 10 | 8 | 8 | 107.8 |  > traversal 107 > draw 107 | AndroidOwner:onTouch 1
fling-list-1 | 78 | 60 | 203.2 | 92 | 60 | 54 | 159.9 |  > traversal 156 > draw 156 > Record View#draw() 22 > AndroidOwner:measureAndLayout 20 > Compose:recompose 10 | compose:lazy:prefetch:idle_frame 12 > compose:lazy:prefetch:execute:urgent 12 > compose:lazy:prefetch:measure 11 > 
fling-list-2 | 0 | 0 | 0.0 | 70 | 0 | 0 | 1.1 |  | AndroidOwner:onTouch 0
fling-list-3 | 0 | 0 | 0.0 | 72 | 0 | 0 | 1.0 |  | AndroidOwner:onTouch 0
reselect-top-1 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | AndroidOwner:onTouch 0
reselect-top-2 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | AndroidOwner:onTouch 0
type-1 | 39 | 32 | 219.7 | 42 | 33 | 32 | 150.0 |  > traversal 146 > draw 146 > Record View#draw() 20 > AndroidOwner:measureAndLayout 18 > TextStringSimpleNode::measure 2 > TextLayout:initLayout 2 | AndroidOwner:outOfFrameExecutor 1
chip-author | 44 | 42 | 174.9 | 49 | 44 | 43 | 121.6 |  > traversal 120 > draw 116 > Record View#draw() 1 | AndroidOwner:onTouch 1
chip-title | 46 | 43 | 178.0 | 53 | 47 | 44 | 121.9 |  > traversal 120 > draw 120 | AndroidOwner:onTouch 1
chip-all | 20 | 8 | 194.1 | 21 | 9 | 8 | 125.8 |  > traversal 125 > draw 125 | AndroidOwner:onTouch 1
clear-1 | 16 | 10 | 191.9 | 18 | 11 | 9 | 158.5 |  > traversal 158 > draw 158 | AndroidOwner:outOfFrameExecutor 1
type-2 | 28 | 19 | 211.9 | 32 | 20 | 15 | 155.2 |  > traversal 154 > draw 154 > Record View#draw() 2 > AndroidOwner:measureAndLayout 1 | AndroidOwner:outOfFrameExecutor 1
clear-2 | 19 | 11 | 219.0 | 21 | 12 | 9 | 176.6 |  > traversal 176 > draw 176 | AndroidOwner:outOfFrameExecutor 1
type-3 | 35 | 27 | 233.8 | 38 | 28 | 23 | 168.2 |  > traversal 163 > draw 163 | AndroidOwner:measureAndLayout 4
erase-3 | 31 | 23 | 230.5 | 37 | 24 | 23 | 158.4 |  > traversal 157 > draw 157 > Record View#draw() 1 | AndroidOwner:outOfFrameExecutor 1
ime-show-1 | 6 | 0 | 2.7 | 21 | 0 | 0 | 11.2 |  > traversal 11 | AndroidOwner:onTouch 1
ime-hide-1 | 6 | 0 | 4.1 | 21 | 0 | 0 | 13.8 |  > traversal 13 | 
ime-show-2 | 6 | 0 | 4.1 | 22 | 0 | 0 | 9.6 |  > traversal 9 | AndroidOwner:onTouch 1
ime-hide-2 | 6 | 0 | 9.2 | 22 | 0 | 0 | 11.4 |  > traversal 11 > draw 4 | monitor contention with owner kx.DefaultExecutor (8231) at b 0
ime-show-3 | 6 | 0 | 3.8 | 21 | 0 | 0 | 9.1 |  > traversal 9 | monitor contention with owner kx.DefaultExecutor (8231) at b 1
ime-hide-3 | 7 | 0 | 1.6 | 23 | 1 | 0 | 18.0 |  > traversal 18 | Lock contention on a monitor lock (owner tid: 7917) 0
ime-show-4 | 6 | 0 | 8.0 | 22 | 0 | 0 | 9.4 |  > traversal 9 | AndroidOwner:onTouch 0
type-digits | 16 | 7 | 210.1 | 21 | 8 | 7 | 183.7 |  > traversal 183 > draw 183 | AndroidOwner:outOfFrameExecutor 1
clear-digits | 17 | 11 | 208.3 | 20 | 10 | 9 | 172.6 |  > traversal 158 > draw 158 > Record View#draw() 36 > AndroidOwner:measureAndLayout 33 > TextStringSimpleNode::measure 3 | AndroidOwner:outOfFrameExecutor 1
ime-hide-4 | 6 | 0 | 2.2 | 23 | 0 | 0 | 6.4 |  > traversal 6 | Lock contention on a monitor lock (owner tid: 8231) 0
sort-open-1 | 14 | 4 | 617.5 | 107 | 3 | 3 | 464.3 |  > traversal 462 > draw 183 | AndroidOwner:onTouch 0
sort-year-1 | 16 | 11 | 255.6 | 96 | 13 | 12 | 177.3 |  > traversal 177 > draw 177 | AndroidOwner:outOfFrameExecutor 16 > Compose:deactivate 15 > Compose:onForgotten 15
sort-open-2 | 13 | 3 | 663.7 | 103 | 4 | 4 | 555.6 |  > traversal 555 > draw 168 | AndroidOwner:onTouch 0
sort-relevance-2 | 16 | 11 | 208.8 | 95 | 13 | 12 | 157.5 |  > traversal 156 > draw 156 | AndroidOwner:outOfFrameExecutor 1
sort-open-3 | 12 | 3 | 651.0 | 100 | 4 | 4 | 398.1 |  > traversal 395 > draw 172 | AndroidOwner:onTouch 0
sort-year-3 | 16 | 10 | 214.3 | 97 | 13 | 12 | 149.6 |  > traversal 149 > draw 149 | AndroidOwner:outOfFrameExecutor 1
filters-open-1 | 11 | 6 | 485.0 | 10 | 7 | 7 | 255.1 |  > traversal 253 > draw 161 | AndroidOwner:onTouch 0
filters-close-1 | 7 | 0 | 3.2 | 15 | 5 | 5 | 116.5 |  > traversal 115 > draw 115 | monitor contention with owner kx.DefaultExecutor (9634) at b 0
filters-open-2 | 11 | 6 | 474.4 | 11 | 7 | 7 | 253.1 |  > traversal 247 > draw 156 | AndroidOwner:onTouch 0
filters-close-2 | 7 | 0 | 27.9 | 15 | 6 | 5 | 109.8 |  > traversal 109 > draw 109 | monitor contention with owner kx.DefaultExecutor (9712) at b 0
filters-open-3 | 11 | 8 | 256.1 | 12 | 8 | 7 | 222.1 |  > traversal 221 > draw 221 | AndroidOwner:onTouch 0
filters-close-3 | 7 | 0 | 12.2 | 15 | 5 | 5 | 97.5 |  > traversal 96 > draw 96 | FocusOwnerImpl:dispatchKeyEvent 0
filters-open-4 | 11 | 6 | 500.2 | 10 | 7 | 6 | 255.5 |  > traversal 254 > draw 164 | AndroidOwner:onTouch 0
filters-close-4 | 7 | 0 | 27.3 | 15 | 6 | 5 | 100.8 |  > traversal 96 > draw 96 | FocusOwnerImpl:dispatchKeyEvent 0
shelf-on-1 | 19 | 3 | 241.9 | 13 | 4 | 3 | 130.3 |  > traversal 126 > draw 126 > Record View#draw() 58 > AndroidOwner:measureAndLayout 52 > Compose:recompose 15 | AndroidOwner:onTouch 1
fling-shelf-1 | 120 | 99 | 261.0 | 127 | 97 | 87 | 940.9 |  > traversal 941 > draw 941 | traversal 226 > draw 136
shelf-off-1 | 20 | 3 | 180.4 | 14 | 5 | 4 | 115.3 |  > traversal 115 > draw 115 | AndroidOwner:onTouch 1
shelf-on-2 | 17 | 4 | 150.7 | 14 | 5 | 4 | 119.7 |  > traversal 119 > draw 119 | AndroidOwner:onTouch 0
fling-shelf-2 | 120 | 92 | 229.5 | 140 | 101 | 83 | 481.5 |  > traversal 478 > draw 478 | AndroidOwner:onTouch 1
shelf-off-2 | 20 | 2 | 203.1 | 14 | 4 | 2 | 136.1 |  > traversal 136 > draw 136 | AndroidOwner:onTouch 0
shelf-on-3 | 18 | 5 | 152.0 | 14 | 5 | 5 | 87.1 |  > traversal 85 > draw 85 | monitor contention with owner kx.DefaultExecutor (9869) at v 1
fling-shelf-3 | 120 | 97 | 220.6 | 129 | 102 | 94 | 174.8 |  > traversal 174 > draw 174 | AndroidOwner:onTouch 3
shelf-off-3 | 19 | 4 | 153.0 | 14 | 4 | 4 | 79.9 |  > traversal 79 > draw 79 | AndroidOwner:onTouch 0
surprise-open-1 | 36 | 26 | 321.7 | 39 | 26 | 25 | 210.5 |  > traversal 210 > draw 210 | AndroidOwner:onTouch 0
surprise-again-1 | 34 | 24 | 281.0 | 38 | 25 | 24 | 198.5 |  > traversal 192 > draw 191 > Record View#draw() 8 > AndroidOwner:draw 5 | AndroidOwner:onTouch 2
surprise-close-1 | 16 | 9 | 158.1 | 19 | 10 | 8 | 120.4 |  > traversal 119 > draw 119 | AndroidOwner:onTouch 0
surprise-open-2 | 36 | 25 | 341.2 | 41 | 25 | 23 | 238.2 |  > traversal 236 > draw 236 > Record View#draw() 5 > AndroidOwner:measureAndLayout 5 | AndroidOwner:onTouch 0
surprise-again-2 | 34 | 23 | 284.8 | 37 | 23 | 23 | 180.8 |  > traversal 174 > draw 174 > Record View#draw() 11 > AndroidOwner:measureAndLayout 7 | AndroidOwner:onTouch 0
surprise-close-2 | 15 | 9 | 179.3 | 19 | 9 | 9 | 118.0 |  > traversal 118 > draw 117 | AndroidOwner:onTouch 0
surprise-open-3 | 35 | 25 | 380.2 | 38 | 25 | 25 | 252.6 |  > traversal 250 > draw 250 | AndroidOwner:onTouch 0
surprise-again-3 | 35 | 25 | 281.1 | 39 | 25 | 25 | 185.8 |  > traversal 185 > draw 185 > Record View#draw() 4 | monitor contention with owner DefaultDispatcher-worker-2 (10 0
surprise-close-3 | 16 | 9 | 175.7 | 19 | 9 | 8 | 118.2 |  > traversal 118 > draw 116 | FocusOwnerImpl:dispatchKeyEvent 1
book-open-1 | 20 | 19 | 322.2 | 25 | 19 | 15 | 247.4 |  > traversal 245 > draw 245 > Record View#draw() 28 > AndroidOwner:measureAndLayout 26 > TextAnnotatedStringNode:measure 7 > TextLayout:initLayout 6 | AndroidOwner:onTouch 2
book-back-1 | 8 | 7 | 280.2 | 10 | 7 | 6 | 246.1 |  > traversal 242 > draw 242 | 
book-open-2 | 19 | 16 | 297.4 | 22 | 18 | 15 | 219.6 |  > traversal 213 > draw 213 > Record View#draw() 23 > AndroidOwner:measureAndLayout 15 > TextStringSimpleNode::measure 2 > TextLayout:initLayout 2 | AndroidOwner:onTouch 2
book-back-2 | 8 | 7 | 268.2 | 9 | 7 | 7 | 236.5 |  > traversal 235 > draw 235 | 
book-open-3 | 19 | 18 | 314.9 | 21 | 18 | 18 | 169.2 |  > traversal 168 > draw 167 | AndroidOwner:onTouch 2
book-back-3 | 9 | 8 | 283.4 | 10 | 7 | 6 | 234.7 |  > traversal 233 > draw 233 | 
refresh-1 | 19 | 16 | 175.3 | 20 | 16 | 16 | 145.6 |  > traversal 145 > draw 145 | AndroidOwner:onTouch 1
refresh-2 | 18 | 16 | 167.1 | 19 | 16 | 14 | 126.1 |  > traversal 125 > draw 125 | AndroidOwner:onTouch 1
resume-1 | 11 | 2 | 193.4 | 26 | 4 | 3 | 187.8 |  > traversal 176 > draw 81 > Record View#draw() 4 > AndroidOwner:draw 4 | activityResume 39 > FocusTransactions:requestFocus 1
resume-2 | 10 | 3 | 136.7 | 26 | 4 | 3 | 138.9 |  > traversal 138 | activityResume 8
highest main-thread CPU in one frame: fling-shelf-1 941 ms, sort-open-2 556 ms, fling-shelf-2 482 ms, sort-open-1 464 ms, sort-open-3 398 ms, filters-open-4 256 ms, filters-open-1 255 ms, filters-open-2 253 ms, surprise-open-3 253 ms, book-open-1 247 ms, book-back-1 246 ms, surprise-open-2 238 ms, book-back-2 236 ms, book-back-3 235 ms
