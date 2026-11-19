interaction | frames | UI>32 | UI>80 | UI max | total max | doFrame max (trace) | path in the worst doFrame | long non-frame main-thread work
---|---|---|---|---|---|---|---|---
first-install-open | 18 | 17 | 17 | 318.3 | 413.4 | 235.0 | traversal 186 > draw 186 > Record View#draw() 0 > AndroidOwner:draw 0 | 
cold-open-1 | 23 | 21 | 21 | 271.5 | 401.2 | 192.8 | traversal 177 > draw 177 > Record View#draw() 45 > AndroidOwner:measureAndLayout 43 > Compose:recompose 8 | 
cold-open-2 | 20 | 20 | 20 | 282.7 | 378.1 | 188.3 | traversal 188 > draw 188 > Record View#draw() 0 > AndroidOwner:draw 0 | 
cold-open-3 | 23 | 21 | 21 | 282.7 | 381.4 | 203.9 | traversal 185 > draw 183 > Record View#draw() 115 > AndroidOwner:measureAndLayout 98 > TextStringSimpleNode::measure 6 > TextLayout:initLayout 0 | 
leave-home-1 | 29 | 22 | 21 | 298.0 | 413.1 | 207.9 | traversal 201 > draw 201 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
warm-open-1 | 10 | 10 | 10 | 367.1 | 476.9 | 230.5 | traversal 230 > draw 230 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
leave-home-2 | 29 | 21 | 18 | 281.7 | 389.9 | 180.6 | traversal 179 > draw 178 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
warm-open-2 | 13 | 11 | 11 | 236.1 | 354.8 | 149.7 | traversal 149 > draw 149 > Record View#draw() 0 > AndroidOwner:draw 0 | 
leave-home-3 | 29 | 26 | 13 | 312.4 | 456.8 | 244.4 | traversal 243 > draw 243 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
warm-open-3 | 11 | 10 | 10 | 261.9 | 360.6 | 181.3 | traversal 181 > draw 180 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
leave-news | 12 | 10 | 9 | 239.2 | 346.5 | 131.7 | traversal 126 > draw 126 > Record View#draw() 22 > AndroidOwner:measureAndLayout 20 > TextStringSimpleNode::measure 5 > TextLayout:initLayout 0 | 
back-from-news | 12 | 7 | 7 | 261.9 | 339.0 | 175.0 | traversal 168 > draw 168 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
leave-more | 14 | 13 | 13 | 229.8 | 326.6 | 134.9 | traversal 133 > draw 133 > Record View#draw() 3 > AndroidOwner:draw 2 | 
back-from-more | 11 | 7 | 7 | 248.2 | 387.3 | 181.2 | traversal 180 > draw 180 > Record View#draw() 4 > AndroidOwner:draw 3 | 
fling-list-1 | 92 | 74 | 73 | 241.0 | 342.7 | 157.9 | traversal 158 > draw 158 > Record View#draw() 0 > AndroidOwner:draw 0 | Recomposer:animation 97
fling-list-2 | 98 | 79 | 77 | 270.3 | 456.0 | 231.6 | traversal 215 > draw 215 > Record View#draw() 57 > AndroidOwner:measureAndLayout 55 > Compose:recompose 25 | 
fling-list-3 | 120 | 103 | 94 | 169.4 | 236.4 | 110.6 | traversal 110 > draw 110 > Record View#draw() 3 > AndroidOwner:draw 3 | 
reselect-top-1 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
reselect-top-2 | 0 | 0 | 0 | 0.0 | 0.0 | 0.0 |  | 
type-1 | 34 | 26 | 25 | 260.8 | 367.3 | 178.8 | traversal 172 > draw 172 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
chip-author | 39 | 37 | 35 | 206.5 | 294.1 | 144.0 | traversal 143 > draw 143 > Record View#draw() 1 > AndroidOwner:draw 0 | 
chip-title | 41 | 39 | 38 | 194.8 | 273.2 | 140.3 | traversal 140 > draw 139 > Record View#draw() 0 > AndroidOwner:draw 0 | 
chip-all | 17 | 9 | 8 | 227.2 | 274.3 | 151.8 | traversal 151 > draw 151 > Record View#draw() 0 > AndroidOwner:draw 0 | 
clear-1 | 14 | 8 | 6 | 230.1 | 359.5 | 192.9 | traversal 192 > draw 191 > Record View#draw() 0 > AndroidOwner:draw 0 | 
type-2 | 25 | 16 | 15 | 252.8 | 347.0 | 169.7 | traversal 164 > draw 164 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
clear-2 | 16 | 9 | 9 | 240.7 | 331.3 | 167.8 | traversal 164 > draw 164 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
type-3 | 31 | 23 | 23 | 278.2 | 386.2 | 164.4 | traversal 163 > draw 163 > Record View#draw() 0 > AndroidOwner:draw 0 | 
erase-3 | 29 | 24 | 23 | 219.8 | 370.9 | 178.8 | traversal 170 > draw 170 > Record View#draw() 4 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
ime-show-1 | 7 | 0 | 0 | 5.4 | 62.4 | 18.1 | traversal 18 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
ime-hide-1 | 6 | 0 | 0 | 2.0 | 54.2 | 22.9 | traversal 23 > measure 0 > AndroidOwner:onMeasure 0 > AndroidOwner:androidViewMeasure 0 | 
ime-show-2 | 7 | 0 | 0 | 3.9 | 41.4 | 18.9 | animation 19 | 
ime-hide-2 | 6 | 0 | 0 | 2.2 | 46.8 | 10.1 | traversal 9 > draw 0 | 
ime-show-3 | 6 | 0 | 0 | 6.8 | 36.5 | 15.8 | traversal 16 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
ime-hide-3 | 7 | 0 | 0 | 1.9 | 32.7 | 8.9 | traversal 9 > layout 0 > AndroidOwner:onLayout 0 > AndroidOwner:viewLayout 0 | 
ime-show-4 | 7 | 0 | 0 | 2.9 | 37.2 | 17.3 | traversal 17 > draw 0 | 
type-digits | 15 | 6 | 6 | 262.8 | 410.0 | 202.5 | traversal 201 > draw 201 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
clear-digits | 16 | 10 | 10 | 306.0 | 411.1 | 208.7 | traversal 208 > draw 208 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
ime-hide-4 | 7 | 0 | 0 | 5.2 | 36.0 | 29.1 | traversal 29 > draw 0 | 
sort-open-1 | 12 | 3 | 3 | 194.7 | 274.2 | 127.8 | traversal 127 > draw 50 > Record View#draw() 0 > AndroidOwner:draw 0 | 
sort-year-1 | 13 | 9 | 7 | 310.1 | 372.8 | 161.2 | traversal 160 > draw 159 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
sort-open-2 | 12 | 4 | 4 | 212.6 | 235.4 | 150.8 | traversal 145 > measure 4 > AndroidOwner:onMeasure 4 > TextStringSimpleNode::measure 0 > TextLayout:initLayout 0 | 
sort-relevance-2 | 15 | 11 | 10 | 254.6 | 346.2 | 182.3 | traversal 182 > draw 182 > Record View#draw() 2 > AndroidOwner:draw 0 | 
sort-open-3 | 11 | 3 | 3 | 197.9 | 235.7 | 116.7 | traversal 116 > draw 7 > Record View#draw() 0 > AndroidOwner:draw 0 | 
sort-year-3 | 15 | 9 | 8 | 292.2 | 357.2 | 191.5 | traversal 191 > draw 191 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-open-1 | 10 | 6 | 6 | 485.1 | 545.0 | 309.6 | traversal 308 > draw 195 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-close-1 | 7 | 0 | 0 | 17.4 | 170.1 | 119.6 | traversal 119 > draw 118 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-open-2 | 11 | 5 | 5 | 571.4 | 629.2 | 296.9 | traversal 294 > draw 190 > Record View#draw() 0 > AndroidOwner:draw 0 | 
filters-close-2 | 7 | 0 | 0 | 15.5 | 139.2 | 102.3 | traversal 101 > draw 101 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-open-3 | 11 | 5 | 5 | 549.5 | 566.5 | 290.5 | traversal 288 > draw 191 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-close-3 | 7 | 0 | 0 | 2.9 | 125.5 | 122.0 | traversal 121 > draw 121 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-open-4 | 12 | 6 | 6 | 521.8 | 573.1 | 311.0 | traversal 310 > draw 178 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
filters-close-4 | 7 | 2 | 0 | 41.9 | 213.7 | 138.0 | traversal 137 > draw 137 > Record View#draw() 3 > AndroidOwner:measureAndLayout 3 > Compose:onPositionedCallbacks 3 | 
shelf-on-1 | 16 | 3 | 3 | 219.9 | 358.6 | 142.2 | traversal 141 > draw 141 > Record View#draw() 0 > AndroidOwner:draw 0 | 
fling-shelf-1 | 109 | 80 | 76 | 251.7 | 435.3 | 206.6 | traversal 203 > draw 203 > Record View#draw() 0 > AndroidOwner:draw 0 | 
shelf-off-1 | 17 | 10 | 10 | 296.2 | 344.4 | 186.3 | traversal 185 > draw 185 > Record View#draw() 0 > AndroidOwner:draw 0 | 
shelf-on-2 | 16 | 5 | 3 | 148.4 | 309.2 | 138.7 | traversal 138 > draw 138 > Record View#draw() 0 > AndroidOwner:draw 0 | 
fling-shelf-2 | 120 | 92 | 89 | 239.3 | 385.2 | 180.6 | traversal 180 > draw 180 > Record View#draw() 0 > AndroidOwner:draw 0 | 
shelf-off-2 | 18 | 10 | 10 | 276.3 | 336.6 | 188.9 | traversal 179 > draw 179 > Record View#draw() 55 > AndroidOwner:measureAndLayout 52 > Compose:applyChanges 10 > FullSuspendCheck 8 | 
shelf-on-3 | 18 | 4 | 2 | 196.4 | 248.4 | 109.6 | traversal 109 > draw 109 > Record View#draw() 0 > AndroidOwner:draw 0 | 
fling-shelf-3 | 118 | 93 | 91 | 285.1 | 379.6 | 408.3 | traversal 408 > draw 408 > Record View#draw() 0 > AndroidOwner:draw 0 | 
shelf-off-3 | 17 | 3 | 2 | 222.8 | 313.4 | 113.5 | traversal 113 > draw 113 > Record View#draw() 0 > AndroidOwner:draw 0 | 
surprise-open-1 | 32 | 22 | 22 | 427.5 | 557.2 | 305.2 | traversal 303 > draw 303 > Record View#draw() 9 > AndroidOwner:measureAndLayout 8 > Compose:applyChanges 2 | 
surprise-again-1 | 32 | 21 | 21 | 342.5 | 486.8 | 264.1 | traversal 261 > draw 261 > Record View#draw() 4 > AndroidOwner:measureAndLayout 3 > TextStringSimpleNode::measure 1 > TextLayout:initLayout 0 | 
surprise-close-1 | 14 | 6 | 4 | 183.9 | 316.9 | 148.1 | traversal 147 > draw 147 > Record View#draw() 0 | 
surprise-open-2 | 35 | 25 | 25 | 365.4 | 653.8 | 325.2 | traversal 324 > draw 324 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
surprise-again-2 | 35 | 25 | 25 | 346.2 | 478.3 | 226.0 | traversal 225 > draw 225 > Record View#draw() 0 | 
surprise-close-2 | 15 | 8 | 7 | 207.0 | 295.6 | 134.9 | traversal 133 > draw 133 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
surprise-open-3 | 31 | 20 | 20 | 395.4 | 533.6 | 255.5 | traversal 255 > draw 255 > Record View#draw() 0 > AndroidOwner:draw 0 | 
surprise-again-3 | 33 | 23 | 23 | 333.7 | 477.6 | 206.6 | traversal 206 > draw 206 > Record View#draw() 0 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
surprise-close-3 | 16 | 9 | 8 | 209.5 | 298.1 | 147.5 | traversal 135 > draw 135 > Record View#draw() 1 > AndroidOwner:measureAndLayout 0 > Compose:onPositionedCallbacks 0 | 
book-open-1 | 18 | 16 | 15 | 339.4 | 422.7 | 223.7 | traversal 219 > draw 217 > Record View#draw() 24 > AndroidOwner:measureAndLayout 23 > TextAnnotatedStringNode:measure 3 > TextLayout:initLayout 0 | 
book-back-1 | 8 | 7 | 7 | 346.7 | 455.1 | 221.5 | traversal 221 > draw 221 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
book-open-2 | 18 | 16 | 15 | 249.6 | 423.4 | 218.3 | traversal 212 > draw 212 > Record View#draw() 6 > AndroidOwner:measureAndLayout 6 > Compose:onPositionedCallbacks 0 | 
book-back-2 | 8 | 7 | 6 | 378.8 | 528.2 | 240.1 | traversal 239 > draw 239 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
book-open-3 | 16 | 15 | 14 | 338.3 | 447.3 | 282.6 | traversal 279 > draw 279 > Record View#draw() 45 > AndroidOwner:measureAndLayout 40 > TextStringSimpleNode::measure 4 > FullSuspendCheck 3 | 
book-back-3 | 8 | 7 | 7 | 354.4 | 487.0 | 256.2 | traversal 248 > draw 248 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
refresh-1 | 18 | 16 | 16 | 218.3 | 342.6 | 159.7 | traversal 151 > draw 150 > Record View#draw() 0 > AndroidOwner:draw 0 | 
refresh-2 | 15 | 13 | 13 | 206.1 | 345.5 | 157.9 | traversal 151 > draw 151 > Record View#draw() 1 > AndroidOwner:measureAndLayout 1 > Compose:onPositionedCallbacks 0 | 
resume-1 | 10 | 3 | 1 | 102.9 | 240.5 | 79.1 | traversal 77 > draw 6 > Record View#draw() 2 > AndroidOwner:draw 2 | 
resume-2 | 10 | 3 | 3 | 175.9 | 265.9 | 148.8 | traversal 148 > draw 48 > Record View#draw() 3 > AndroidOwner:draw 3 | 
slowest UI-thread frames: filters-open-2 571 ms, filters-open-3 550 ms, filters-open-4 522 ms, filters-open-1 485 ms, surprise-open-1 428 ms, surprise-open-3 395 ms, book-back-2 379 ms, warm-open-1 367 ms, surprise-open-2 365 ms, book-back-3 354 ms, book-back-1 347 ms, surprise-again-2 346 ms
