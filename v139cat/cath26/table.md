interaction | frames | ui>32 | ui max | doFrames | main CPU/frame >16 | >32 | max CPU in a frame | path in that frame (CPU ms) | worst work outside frames (CPU ms)
---|---|---|---|---|---|---|---|---|---
first-install-open | 18 | 16 | 328.5 | 0 | 0 | 0 | 0.0 |  | 
cold-open-1 | 22 | 21 | 255.1 | 56 | 31 | 30 | 184.4 |  > traversal 184 > draw 184 | AndroidOwner:onTouch 9
cold-open-2 | 22 | 21 | 256.2 | 54 | 30 | 29 | 182.0 |  > traversal 182 > draw 181 | AndroidOwner:onTouch 5
cold-open-3 | 22 | 20 | 432.7 | 55 | 30 | 28 | 238.2 |  > traversal 220 > draw 220 > Record View#draw() 56 > AndroidOwner:measureAndLayout 50 > Compose:onRemembered 16 | AndroidOwner:onTouch 6
leave-home-1 | 28 | 25 | 265.9 | 48 | 27 | 26 | 173.2 |  > traversal 173 > draw 172 | AndroidOwner:onTouch 4
warm-open-1 | 14 | 11 | 284.5 | 37 | 20 | 17 | 200.5 |  > traversal 200 > draw 200 | monitor contention with owner kotlinx.coroutines.DefaultExec 1
leave-home-2 | 29 | 23 | 250.8 | 54 | 26 | 24 | 181.8 |  > traversal 181 > draw 181 > Record View#draw() 2 > AndroidOwner:measureAndLayout 2 | AndroidOwner:onTouch 2
warm-open-2 | 13 | 12 | 254.6 | 43 | 20 | 20 | 193.8 |  > traversal 193 > draw 193 | AndroidOwner:onTouch 1
leave-home-3 | 29 | 22 | 289.3 | 57 | 25 | 23 | 209.5 |  > traversal 207 > draw 207 > Record View#draw() 1 | AndroidOwner:onTouch 3
warm-open-3 | 14 | 13 | 239.0 | 50 | 24 | 23 | 161.8 |  > traversal 150 > draw 150 | AndroidOwner:onTouch 1
leave-news | 13 | 12 | 288.8 | 13 | 12 | 11 | 200.9 |  > traversal 177 > draw 177 > Record View#draw() 30 > AndroidOwner:measureAndLayout 29 > Compose:recompose 7 | AndroidOwner:onTouch 4
back-from-news | 10 | 6 | 274.1 | 10 | 6 | 6 | 170.8 |  > traversal 170 > draw 170 | AndroidOwner:onTouch 1
leave-more | 14 | 13 | 220.6 | 15 | 13 | 13 | 129.8 |  > traversal 121 > draw 121 > Record View#draw() 1 | AndroidOwner:onTouch 2
back-from-more | 10 | 9 | 238.3 | 11 | 9 | 9 | 158.2 |  > traversal 157 > draw 157 | AndroidOwner:onTouch 1
fling-list-1 | 98 | 83 | 347.0 | 106 | 83 | 81 | 214.7 |  > traversal 208 > draw 208 > Record View#draw() 42 > AndroidOwner:measureAndLayout 40 > Compose:recompose 18 | compose:lazy:prefetch:idle_frame 16 > compose:lazy:prefetch:execute:urgent 16 > compose:lazy:prefetch:measure 13 > 
fling-list-2 | 0 | 0 | 0.0 | 71 | 0 | 0 | 1.0 |  | AndroidOwner:onTouch 0
fling-list-3 | 0 | 0 | 0.0 | 71 | 0 | 0 | 1.0 |  | AndroidOwner:onTouch 0
reselect-top-1 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | AndroidOwner:onTouch 0
reselect-top-2 | 0 | 0 | 0.0 | 0 | 0 | 0 | 0.0 |  | AndroidOwner:onTouch 0
type-1 | 31 | 23 | 277.4 | 34 | 25 | 22 | 184.0 |  > traversal 178 > draw 178 | AndroidOwner:measureAndLayout 3
chip-author | 39 | 37 | 213.2 | 45 | 40 | 40 | 128.2 |  > traversal 128 > draw 128 | AndroidOwner:onTouch 1
chip-title | 40 | 38 | 176.7 | 46 | 39 | 39 | 98.3 |  > traversal 97 > draw 97 | monitor contention with owner DefaultDispatcher-worker-1 (68 2 > Lock contention on a monitor lock (owner tid: 1
chip-all | 17 | 8 | 216.1 | 20 | 8 | 8 | 157.4 |  > traversal 149 > draw 149 > Record View#draw() 61 > AndroidOwner:measureAndLayout 54 > TextStringSimpleNode::measure 11 | AndroidOwner:onTouch 0
clear-1 | 16 | 9 | 223.2 | 19 | 9 | 9 | 154.2 |  > traversal 148 > draw 148 > Record View#draw() 2 > AndroidOwner:draw 2 | AndroidOwner:outOfFrameExecutor 1
type-2 | 25 | 15 | 251.7 | 30 | 17 | 15 | 194.4 |  > traversal 185 > draw 185 > Record View#draw() 3 > AndroidOwner:measureAndLayout 2 | AndroidOwner:outOfFrameExecutor 1
clear-2 | 16 | 9 | 211.1 | 20 | 9 | 9 | 172.9 |  > traversal 172 > draw 172 | AndroidOwner:outOfFrameExecutor 1
type-3 | 31 | 23 | 254.1 | 34 | 24 | 22 | 159.7 |  > traversal 153 > draw 153 > Record View#draw() 5 > AndroidOwner:measureAndLayout 5 | AndroidOwner:measureAndLayout 1
erase-3 | 30 | 24 | 262.0 | 35 | 23 | 22 | 177.1 |  > traversal 176 > draw 176 > Record View#draw() 2 > AndroidOwner:measureAndLayout 2 | AndroidOwner:outOfFrameExecutor 1
ime-show-1 | 6 | 0 | 8.8 | 22 | 1 | 0 | 20.5 |  > traversal 20 | AndroidOwner:onTouch 1
ime-hide-1 | 6 | 0 | 3.3 | 23 | 0 | 0 | 9.2 |  > traversal 9 | 
ime-show-2 | 6 | 0 | 7.4 | 22 | 0 | 0 | 6.4 |  > traversal 6 > draw 6 | AndroidOwner:onTouch 0
ime-hide-2 | 6 | 0 | 2.2 | 22 | 0 | 0 | 8.9 |  > traversal 9 | 
ime-show-3 | 7 | 0 | 4.4 | 23 | 1 | 0 | 21.9 |  > traversal 22 | AndroidOwner:onTouch 0
ime-hide-3 | 6 | 0 | 4.7 | 22 | 0 | 0 | 10.3 |  > traversal 10 | 
ime-show-4 | 7 | 0 | 2.5 | 22 | 1 | 0 | 16.2 |  > traversal 16 | AndroidOwner:onTouch 0
type-digits | 17 | 9 | 267.3 | 21 | 10 | 9 | 176.0 |  > traversal 174 > draw 174 > Record View#draw() 9 > AndroidOwner:measureAndLayout 9 > Run checkpoint function 1 | FocusOwnerImpl:dispatchKeyEvent 2
clear-digits | 16 | 12 | 267.7 | 19 | 12 | 11 | 185.3 |  > traversal 183 > draw 183 > Record View#draw() 1 | AndroidOwner:outOfFrameExecutor 1
ime-hide-4 | 7 | 0 | 3.3 | 23 | 1 | 0 | 21.1 |  > traversal 21 | 
sort-open-1 | 11 | 3 | 222.2 | 145 | 4 | 3 | 126.0 |  > traversal 124 > draw 74 | AndroidOwner:onTouch 0
sort-year-1 | 15 | 10 | 261.2 | 96 | 11 | 11 | 188.8 |  > traversal 185 > draw 183 | AndroidOwner:outOfFrameExecutor 1
sort-open-2 | 10 | 3 | 199.8 | 136 | 4 | 4 | 168.1 |  > traversal 166 > draw 166 | AndroidOwner:onTouch 0
sort-relevance-2 | 15 | 11 | 279.6 | 96 | 13 | 13 | 188.1 |  > traversal 187 > draw 187 | AndroidOwner:measureAndLayout 1
sort-open-3 | 12 | 2 | 238.5 | 141 | 4 | 2 | 130.3 |  > traversal 129 > draw 73 | AndroidOwner:onTouch 0
sort-year-3 | 13 | 8 | 205.4 | 97 | 11 | 10 | 182.2 |  > traversal 171 > draw 171 | AndroidOwner:outOfFrameExecutor 1
filters-open-1 | 10 | 6 | 465.3 | 11 | 7 | 7 | 268.2 |  > traversal 264 > draw 199 > Record View#draw() 5 > AndroidOwner:draw 4 | AndroidOwner:onTouch 0
filters-close-1 | 7 | 1 | 45.4 | 15 | 7 | 6 | 107.5 |  > traversal 106 > draw 106 | FocusOwnerImpl:dispatchKeyEvent 0
filters-open-2 | 11 | 7 | 322.4 | 12 | 7 | 7 | 317.8 |  > traversal 314 > draw 200 | AndroidOwner:onTouch 0
filters-close-2 | 7 | 0 | 2.7 | 16 | 5 | 5 | 126.0 |  > animation 126 > Recomposer:recompose 126 > Compose:onForgotten 125 | FocusOwnerImpl:dispatchKeyEvent 0
filters-open-3 | 11 | 7 | 416.5 | 12 | 8 | 8 | 215.5 |  > traversal 214 > draw 193 | AndroidOwner:onTouch 0
filters-close-3 | 6 | 0 | 13.1 | 15 | 5 | 5 | 117.4 |  > animation 117 > Recomposer:recompose 117 > Compose:onForgotten 116 | Lock contention on a monitor lock (owner tid: 9737) 0
filters-open-4 | 13 | 8 | 408.1 | 13 | 8 | 8 | 217.0 |  > traversal 216 > draw 216 > Record View#draw() 3 > AndroidOwner:measureAndLayout 2 > Compose:onPositionedCallbacks 2 | AndroidOwner:onTouch 0
filters-close-4 | 7 | 0 | 4.7 | 16 | 5 | 5 | 126.9 |  > traversal 126 > draw 126 | FocusOwnerImpl:dispatchKeyEvent 0
shelf-on-1 | 16 | 4 | 198.5 | 13 | 4 | 3 | 188.0 |  > traversal 188 > draw 188 | AndroidOwner:onTouch 1
fling-shelf-1 | 114 | 89 | 250.1 | 116 | 86 | 83 | 202.7 |  > traversal 202 > draw 202 | compose:lazy:prefetch:execute:urgent 2
shelf-off-1 | 19 | 5 | 162.6 | 14 | 5 | 5 | 106.1 |  > traversal 106 > draw 106 | AndroidOwner:onTouch 1
shelf-on-2 | 16 | 4 | 207.2 | 14 | 4 | 4 | 112.0 |  > traversal 112 > draw 112 | monitor contention with owner DefaultDispatcher-worker-3 (98 1
fling-shelf-2 | 120 | 95 | 277.9 | 124 | 90 | 88 | 247.3 |  > traversal 247 > draw 247 | compose:lazy:prefetch:idle_frame 3 > compose:lazy:prefetch:execute:urgent 3 > compose:lazy:prefetch:compose 3
shelf-off-2 | 16 | 4 | 152.5 | 12 | 5 | 3 | 147.6 |  > traversal 147 > draw 146 | monitor contention with owner DefaultDispatcher-worker-1 (90 1
shelf-on-3 | 18 | 5 | 217.3 | 14 | 5 | 5 | 115.1 |  > traversal 115 > draw 115 | AndroidOwner:onTouch 1
fling-shelf-3 | 120 | 98 | 282.3 | 115 | 86 | 83 | 242.8 |  > traversal 243 > draw 242 | AndroidOwner:onTouch 4
shelf-off-3 | 17 | 5 | 178.3 | 14 | 5 | 4 | 125.1 |  > traversal 125 > draw 125 | AndroidOwner:onTouch 1
surprise-open-1 | 32 | 23 | 383.7 | 35 | 23 | 22 | 255.6 |  > traversal 251 > draw 251 | AndroidOwner:onTouch 0
surprise-again-1 | 33 | 23 | 348.6 | 36 | 23 | 22 | 220.7 |  > traversal 220 > draw 220 | AndroidOwner:onTouch 1
surprise-close-1 | 12 | 7 | 215.5 | 16 | 7 | 7 | 136.8 |  > traversal 131 > draw 131 | AndroidOwner:onTouch 0
surprise-open-2 | 32 | 21 | 401.5 | 35 | 21 | 19 | 241.4 |  > traversal 239 > draw 239 | monitor contention with owner DefaultDispatcher-worker-1 (90 1
surprise-again-2 | 32 | 23 | 352.3 | 35 | 23 | 21 | 239.5 |  > traversal 237 > draw 237 | AndroidOwner:onTouch 0
surprise-close-2 | 14 | 7 | 230.5 | 17 | 7 | 7 | 175.1 |  > traversal 173 > draw 173 | AndroidOwner:onTouch 0
surprise-open-3 | 34 | 24 | 407.2 | 37 | 24 | 24 | 283.5 |  > traversal 283 > draw 283 > Record View#draw() 12 > AndroidOwner:measureAndLayout 11 | AndroidOwner:onTouch 0
surprise-again-3 | 33 | 23 | 338.3 | 37 | 23 | 23 | 217.6 |  > traversal 217 > draw 217 > Record View#draw() 3 | AndroidOwner:onTouch 0
surprise-close-3 | 15 | 9 | 205.7 | 19 | 9 | 9 | 138.8 |  > traversal 138 > draw 138 | FocusOwnerImpl:dispatchKeyEvent 1
book-open-1 | 19 | 17 | 271.3 | 23 | 17 | 17 | 181.6 |  > traversal 176 > draw 176 > Record View#draw() 25 > AndroidOwner:measureAndLayout 22 > Compose:recompose 5 | monitor contention with owner arch_disk_io_2 (6365) at boole 4 > Lock contention on a monitor lock (owner tid: 4
book-back-1 | 8 | 7 | 322.3 | 9 | 8 | 7 | 286.7 |  > traversal 286 > draw 286 > Record View#draw() 5 > AndroidOwner:measureAndLayout 5 | 
book-open-2 | 18 | 17 | 395.5 | 20 | 17 | 15 | 280.3 |  > traversal 273 > draw 273 > Record View#draw() 24 > AndroidOwner:measureAndLayout 20 > Compose:recompose 3 | AndroidOwner:onTouch 1
book-back-2 | 8 | 7 | 319.8 | 9 | 7 | 6 | 284.9 |  > traversal 284 > draw 284 | 
book-open-3 | 16 | 15 | 366.7 | 17 | 14 | 14 | 203.8 |  > traversal 200 > draw 200 > Record View#draw() 33 > AndroidOwner:measureAndLayout 30 > TextStringSimpleNode::measure 5 | AndroidOwner:onTouch 2
book-back-3 | 8 | 7 | 349.5 | 9 | 6 | 6 | 211.7 |  > traversal 207 > draw 206 | 
refresh-1 | 15 | 13 | 277.7 | 15 | 13 | 13 | 193.5 |  > traversal 182 > draw 182 | AndroidOwner:onTouch 1
refresh-2 | 17 | 15 | 241.4 | 18 | 15 | 14 | 158.0 |  > traversal 147 > draw 147 > Record View#draw() 11 > AndroidOwner:measureAndLayout 10 | AndroidOwner:onTouch 1
resume-1 | 11 | 3 | 220.3 | 26 | 5 | 5 | 198.9 |  > traversal 198 > draw 53 > Record View#draw() 3 > AndroidOwner:draw 2 | activityResume 8
resume-2 | 11 | 2 | 214.5 | 26 | 4 | 2 | 225.0 |  > traversal 224 > draw 80 > Record View#draw() 4 > AndroidOwner:draw 3 | activityResume 18
highest main-thread CPU in one frame: filters-open-2 318 ms, book-back-1 287 ms, book-back-2 285 ms, surprise-open-3 284 ms, book-open-2 280 ms, filters-open-1 268 ms, surprise-open-1 256 ms, fling-shelf-2 247 ms, fling-shelf-3 243 ms, surprise-open-2 241 ms, surprise-again-2 240 ms, cold-open-3 238 ms, resume-2 225 ms, surprise-again-1 221 ms
