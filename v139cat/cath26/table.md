interaction | frames | UI>32 | UI>80 | UI max | total max | doFrame max (trace) | path in the worst doFrame | long non-frame main-thread work
---|---|---|---|---|---|---|---|---
first-install-open | 23 | 19 | 17 | 588.8 | 684.6 | 331.7 | traversal 280 > draw 280 > Record View#draw() 172 > AndroidOwner:measureAndLayout 158 > Compose:recompose 27 > res/oU.webp 9 > decodeBitmap 9 | 
cold-open-1 | 22 | 21 | 19 | 234.2 | 292.4 | 128.0 | traversal 121 > draw 121 > Record View#draw() 66 > AndroidOwner:measureAndLayout 64 > TextStringSimpleNode::measure 9 > FullSuspendCheck 8 | 
cold-open-2 | 24 | 24 | 21 | 220.6 | 302.0 | 128.0 | traversal 128 > draw 128 > Record View#draw() 0 > AndroidOwner:draw 0 | 
cold-open-3 | 24 | 22 | 22 | 200.6 | 299.5 | 126.0 | traversal 126 > draw 126 > Record View#draw() 0 > AndroidOwner:draw 0 | 
leave-home-1 | 41 | 31 | 11 | 202.9 | 313.8 | 171.6 | traversal 153 > draw 153 > Record View#draw() 6 > AndroidOwner:measureAndLayout 5 > Compose:onPositionedCallbacks 0 | 
warm-open-1 | 16 | 14 | 13 | 235.1 | 318.0 | 179.8 | traversal 178 > draw 178 > Record View#draw() 6 > AndroidOwner:draw 5 | 
leave-home-2 | 41 | 33 | 14 | 198.8 | 296.4 | 135.3 | traversal 135 > draw 134 > Record View#draw() 1 > AndroidOwner:draw 0 | 
warm-open-2 | 17 | 9 | 8 | 181.0 | 241.6 | 114.6 | traversal 114 > draw 114 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
leave-home-3 | 41 | 40 | 12 | 176.5 | 272.0 | 119.5 | traversal 119 > draw 119 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
warm-open-3 | 14 | 11 | 9 | 249.9 | 327.2 | 167.5 | traversal 165 > draw 164 > Record View#draw() 65 > AndroidOwner:measureAndLayout 51 > Compose:recompose 7 | 
leave-news | 14 | 12 | 11 | 190.5 | 258.8 | 104.3 | traversal 104 > draw 103 > Record View#draw() 1 > AndroidOwner:draw 0 | 
back-from-news | 14 | 9 | 7 | 169.5 | 277.3 | 142.4 | traversal 141 > draw 141 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
leave-more | 17 | 16 | 15 | 176.5 | 270.2 | 114.2 | traversal 110 > draw 110 > Record View#draw() 3 > AndroidOwner:measureAndLayout 2 > TextStringSimpleNode::measure 1 > TextLayout:initLayout 0 | 
back-from-more | 15 | 8 | 6 | 175.4 | 267.9 | 117.2 | traversal 114 > draw 114 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
fling-list-1 | 100 | 82 | 69 | 271.6 | 331.6 | 196.0 | traversal 187 > draw 187 > Record View#draw() 26 > AndroidOwner:measureAndLayout 23 > Compose:recompose 13 | 
fling-list-2 | 0 | 0 | 0 | 0.0 | 0.0 | 1.1 | animation 1 > Recomposer:animation 1 | 
fling-list-3 | 0 | 0 | 0 | 0.0 | 0.0 | 1.0 | input 1 > AndroidOwner:onTouch 0 | 
reselect-top-1 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
reselect-top-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
type-1 | 38 | 30 | 30 | 213.2 | 288.2 | 132.3 | traversal 129 > draw 129 > Record View#draw() 2 > AndroidOwner:measureAndLayout 2 > Compose:onPositionedCallbacks 0 | 
chip-author | 51 | 47 | 43 | 149.4 | 239.8 | 108.9 | traversal 108 > draw 108 > Record View#draw() 4 > AndroidOwner:draw 4 | 
chip-title | 52 | 50 | 45 | 166.8 | 213.0 | 105.9 | traversal 95 > draw 95 > Record View#draw() 1 > AndroidOwner:draw 1 | 
chip-all | 20 | 11 | 8 | 149.1 | 215.6 | 99.1 | traversal 98 > draw 98 > Record View#draw() 0 > AndroidOwner:draw 0 | 
clear-1 | 17 | 10 | 9 | 164.2 | 263.4 | 130.7 | traversal 129 > draw 129 > Record View#draw() 0 > AndroidOwner:draw 0 | 
type-2 | 30 | 21 | 19 | 163.0 | 244.4 | 114.0 | traversal 107 > draw 107 > Record View#draw() 4 > AndroidOwner:measureAndLayout 4 > Compose:onPositionedCallbacks 0 | 
clear-2 | 18 | 12 | 12 | 187.0 | 220.8 | 119.5 | traversal 118 > draw 118 > Record View#draw() 2 > AndroidOwner:measureAndLayout 2 > Compose:onPositionedCallbacks 0 | 
type-3 | 39 | 29 | 27 | 179.1 | 273.9 | 112.4 | traversal 112 > draw 112 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
erase-3 | 35 | 28 | 26 | 171.6 | 221.4 | 119.5 | traversal 118 > draw 118 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | msg Handler (android.os.Handler) jd: 0 33
ime-show-1 | 6 | 0 | 0 | 3.0 | 12.8 | 10.0 | traversal 10 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
ime-hide-1 | 7 | 0 | 0 | 1.3 | 13.4 | 11.6 | traversal 11 > measure 1 > AndroidOwner:onMeasure 0 > AndroidOwner:androidViewMeasure 0 | 
ime-show-2 | 6 | 0 | 0 | 6.8 | 33.2 | 9.0 | traversal 9 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
ime-hide-2 | 7 | 0 | 0 | 1.6 | 18.7 | 8.0 | traversal 7 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
ime-show-3 | 7 | 0 | 0 | 2.1 | 10.2 | 8.4 | traversal 8 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
ime-hide-3 | 6 | 0 | 0 | 7.4 | 46.6 | 25.2 | traversal 25 > measure 0 > AndroidOwner:onMeasure 0 > AndroidOwner:androidViewMeasure 0 | 
ime-show-4 | 6 | 0 | 0 | 6.9 | 38.9 | 7.1 | traversal 7 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
type-digits | 17 | 8 | 8 | 191.0 | 270.1 | 122.4 | traversal 121 > draw 121 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
clear-digits | 19 | 12 | 12 | 181.4 | 271.3 | 127.1 | traversal 126 > draw 126 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
ime-hide-4 | 6 | 0 | 0 | 2.6 | 30.7 | 4.7 | traversal 5 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
sort-open-1 | 14 | 3 | 3 | 153.6 | 159.9 | 132.4 | traversal 130 > draw 116 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
sort-year-1 | 16 | 10 | 9 | 203.7 | 271.6 | 148.3 | traversal 145 > draw 145 > Record View#draw() 1 > AndroidOwner:draw 0 | 
sort-open-2 | 15 | 3 | 3 | 666.0 | 690.5 | 465.5 | traversal 464 > draw 145 > Record View#draw() 2 > AndroidOwner:draw 2 | 
sort-relevance-2 | 16 | 10 | 9 | 178.2 | 241.4 | 124.2 | traversal 123 > draw 123 > Record View#draw() 0 > AndroidOwner:draw 0 | 
sort-open-3 | 15 | 4 | 4 | 140.4 | 155.1 | 83.5 | traversal 82 > draw 19 > Record View#draw() 0 > AndroidOwner:draw 0 | 
sort-year-3 | 15 | 9 | 8 | 192.2 | 269.5 | 129.2 | traversal 128 > draw 128 > Record View#draw() 0 > AndroidOwner:draw 0 | 
filters-open-1 | 14 | 8 | 8 | 388.8 | 432.8 | 215.5 | traversal 213 > draw 136 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-close-1 | 7 | 0 | 0 | 8.4 | 86.7 | 85.9 | traversal 85 > draw 85 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-open-2 | 14 | 7 | 7 | 391.4 | 434.4 | 214.6 | traversal 213 > draw 160 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-close-2 | 7 | 0 | 0 | 13.3 | 122.1 | 81.0 | traversal 79 > draw 79 > Record View#draw() 4 > AndroidOwner:measureAndLayout 3 > Compose:onPositionedCallbacks 0 | 
filters-open-3 | 14 | 9 | 9 | 282.5 | 287.9 | 199.4 | traversal 199 > draw 199 > Record View#draw() 0 | 
filters-close-3 | 7 | 0 | 0 | 17.2 | 97.2 | 76.4 | traversal 74 > draw 74 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-open-4 | 15 | 8 | 8 | 285.0 | 290.9 | 226.8 | traversal 226 > draw 86 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-close-4 | 7 | 0 | 0 | 15.7 | 104.5 | 66.6 | traversal 64 > draw 64 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
shelf-on-1 | 22 | 4 | 3 | 180.5 | 247.9 | 117.8 | traversal 117 > draw 117 > Record View#draw() 0 > AndroidOwner:draw 0 | 
fling-shelf-1 | 120 | 97 | 91 | 174.0 | 259.7 | 404.2 | traversal 404 > draw 403 > Record View#draw() 0 > AndroidOwner:draw 0 | 
shelf-off-1 | 22 | 13 | 12 | 156.5 | 232.2 | 123.8 | traversal 119 > draw 119 > Record View#draw() 55 > AndroidOwner:measureAndLayout 54 > TextStringSimpleNode::measure 14 > TextLayout:initLayout 0 | 
shelf-on-2 | 18 | 11 | 11 | 155.1 | 240.8 | 115.5 | traversal 115 > draw 115 > Record View#draw() 0 > AndroidOwner:draw 0 | 
fling-shelf-2 | 120 | 95 | 88 | 183.0 | 271.9 | 138.7 | traversal 138 > draw 138 > Record View#draw() 0 | msg Handler (android.os.Handler) jd: 0 711
shelf-off-2 | 23 | 4 | 3 | 114.9 | 201.1 | 93.4 | traversal 93 > draw 92 > Record View#draw() 0 > AndroidOwner:draw 0 | 
shelf-on-3 | 20 | 12 | 12 | 171.9 | 238.3 | 111.8 | traversal 111 > draw 111 > Record View#draw() 0 > AndroidOwner:draw 0 | 
fling-shelf-3 | 120 | 93 | 83 | 188.1 | 323.7 | 311.9 | traversal 310 > draw 310 > Record View#draw() 0 > AndroidOwner:draw 0 | 
shelf-off-3 | 21 | 14 | 14 | 138.5 | 216.0 | 102.5 | traversal 102 > draw 102 > Record View#draw() 0 > AndroidOwner:draw 0 | 
surprise-open-1 | 37 | 26 | 25 | 264.7 | 375.6 | 174.2 | traversal 173 > draw 173 > Record View#draw() 7 > AndroidOwner:measureAndLayout 6 > TextStringSimpleNode::measure 1 > TextLayout:initLayout 0 | 
surprise-again-1 | 40 | 28 | 28 | 216.5 | 303.1 | 149.8 | traversal 144 > draw 142 > Record View#draw() 0 | 
surprise-close-1 | 17 | 11 | 10 | 126.3 | 189.7 | 72.8 | traversal 72 > draw 72 > Record View#draw() 0 | 
surprise-open-2 | 37 | 25 | 25 | 287.9 | 393.3 | 186.3 | traversal 186 > draw 186 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
surprise-again-2 | 38 | 27 | 26 | 204.8 | 321.6 | 151.1 | traversal 150 > draw 150 > Record View#draw() 0 | 
surprise-close-2 | 17 | 10 | 10 | 142.6 | 214.2 | 98.4 | traversal 97 > draw 97 > Record View#draw() 1 > AndroidOwner:draw 1 | 
surprise-open-3 | 36 | 25 | 25 | 263.2 | 401.4 | 185.1 | traversal 178 > draw 178 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
surprise-again-3 | 40 | 29 | 28 | 221.0 | 315.6 | 153.6 | traversal 141 > draw 141 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
surprise-close-3 | 17 | 10 | 8 | 128.3 | 215.7 | 97.9 | traversal 94 > draw 94 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
book-open-1 | 22 | 20 | 16 | 214.7 | 282.3 | 116.7 | traversal 107 > draw 107 > Record View#draw() 11 > AndroidOwner:measureAndLayout 9 > Compose:recompose 2 | 
book-back-1 | 9 | 8 | 6 | 216.9 | 351.9 | 183.6 | traversal 179 > draw 179 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
book-open-2 | 21 | 20 | 16 | 263.9 | 371.6 | 187.7 | traversal 185 > draw 185 > Record View#draw() 17 > AndroidOwner:measureAndLayout 15 > TextStringSimpleNode::measure 2 > TextLayout:initLayout 0 | 
book-back-2 | 9 | 8 | 6 | 213.4 | 331.6 | 174.3 | traversal 172 > draw 172 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
book-open-3 | 21 | 19 | 14 | 284.7 | 309.2 | 164.7 | traversal 159 > draw 159 > Record View#draw() 16 > AndroidOwner:measureAndLayout 13 > Compose:recompose 1 | 
book-back-3 | 9 | 8 | 6 | 240.6 | 370.0 | 204.2 | traversal 203 > draw 203 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
refresh-1 | 20 | 17 | 16 | 163.0 | 247.1 | 117.1 | traversal 114 > draw 114 > Record View#draw() 0 > AndroidOwner:draw 0 | 
refresh-2 | 19 | 17 | 15 | 152.6 | 236.4 | 102.4 | traversal 102 > draw 102 > Record View#draw() 0 > AndroidOwner:draw 0 | 
resume-1 | 11 | 4 | 2 | 178.7 | 270.5 | 134.7 | traversal 134 > draw 134 > Record View#draw() 1 > AndroidOwner:draw 1 | 
resume-2 | 10 | 3 | 2 | 167.2 | 253.0 | 133.4 | traversal 133 > layout 0 | 
slowest UI-thread frames: sort-open-2 666 ms, first-install-open 589 ms, filters-open-2 391 ms, filters-open-1 389 ms, surprise-open-2 288 ms, filters-open-4 285 ms, book-open-3 285 ms, filters-open-3 282 ms, fling-list-1 272 ms, surprise-open-1 265 ms, book-open-2 264 ms, surprise-open-3 263 ms
