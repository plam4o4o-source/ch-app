# Частите на вълна 19 (ВРЕМЕННО, debug/wave19-check): тестовото v1.2.33 (6d3a324) срещу v1.2.32 (87ef20e).
# Зарежда се от run.sh след w15.sh, w16.sh и w18.sh и ползва помощните им функции.
#   drawn19    — „Fully drawn“ при студен старт, двете издания поред (анимациите включени)
#   jank19-1/2 — плавност: „Начало“ с лентата (първите 25 s) и след нея, „Новини“, статия, каталог,
#                прегледът в галерията, картата (шевица + фолио, наклон през сензора на емулатора);
#                „Бързодействие“ (1.2.33)
#   idle19     — CPU и кадри без докосване: „Начало“ след лентата, картата, статия, каталог, „Моето“, фон
#   func19     — новото на вълна 19 (1.2.33, API 33): лента, розетка, групи бутони, прегледът, отваряне на
#                книга, дърпане за опресняване, празно състояние, конфети, статия, „Изненадай ме“, споделяне
#   year19     — devDebug (демо вход): „Моята читателска година“, конфети при удължаване
#   motion19   — намалено движение (мащаб на анимациите 0) и режим за пестене на батерията
#   live36     — Android 16: живото известие за изтеглянето (ProgressStyle, чип) и за събитие
#   fb28       — API 28: неподвижната шевица и фолиото без AGSL, „Изненадай ме“, отваряне на книга, споделяне
#   monkey19   — monkey 4000 събития (seed 1357), 1.2.33
W19=$H_/w19.py
CARD_NO=TEST-12345
VC_PRE=1488545

emu() { timeout 12 adb emu "$@" 2>&1 | tr -d '\r' | grep -v '^OK$' | head -2 | tr '\n' ' '; }
pidof_app() { a pidof $PKG | tr -d '\r' | cut -d' ' -f1; }
gfx_total() { a dumpsys gfxinfo $PKG > $OUT/gfx-tmp.txt 2>/dev/null; python3 $W19 frames $OUT/gfx-tmp.txt; }
task_snap() { a "cat /proc/$1/task/*/stat" > $2 2>/dev/null; }
cpu_win() {  # $1 — етикет, $2 — секунди: CPU на процеса (по нишки) и кадрите (gfxinfo), без докосване
  local pid f0 f1
  pid=$(pidof_app); [ -n "$pid" ] || { log "IDLE $1: app not running"; return 1; }
  mkdir -p $OUT/cpu
  f0=$(gfx_total); task_snap $pid $OUT/cpu/$1-t0.txt
  sleep $2
  task_snap $pid $OUT/cpu/$1-t1.txt; f1=$(gfx_total)
  log "IDLE $1 (${2} s, no input): frames +$((f1 - f0)); $(python3 $W19 cpu $OUT/cpu/$1-t0.txt $OUT/cpu/$1-t1.txt $2); pid $pid"
}
# Запис на екрана (кадрите — в пълния размер на екрана, за общите правоъгълници)
rec_start() {  # $1 — име, $2 — секунди
  RECNAME=$1; mkdir -p $OUT/rec
  a rm -f /sdcard/rec.mp4
  adb shell screenrecord --time-limit ${2:-5} --size 540x1200 --bit-rate 1500000 /sdcard/rec.mp4 >/dev/null 2>&1 &
  RECPID=$!; sleep 0.8
}
rec_end() {  # [CROP] — кадрите в $OUT/rec/$RECNAME/; движение и контактен лист
  wait $RECPID 2>/dev/null; sleep 1
  rm -rf $OUT/rec/$RECNAME; mkdir -p $OUT/rec/$RECNAME
  timeout 30 adb pull /sdcard/rec.mp4 $OUT/rec/$RECNAME.mp4 >/dev/null 2>&1
  [ -s $OUT/rec/$RECNAME.mp4 ] && ffmpeg -loglevel error -i $OUT/rec/$RECNAME.mp4 -vf fps=8,scale=1080:2400 $OUT/rec/$RECNAME/f%03d.png
  local n; n=$(ls $OUT/rec/$RECNAME 2>/dev/null | wc -l)
  if [ "$n" = 0 ]; then log "REC $RECNAME: screenrecord gave no frames"; return 1; fi
  log "REC $RECNAME: $n frames @8 fps; motion ${1:-all}: $(python3 $W19 motion ${1:-all} $OUT/rec/$RECNAME/f*.png | cut -c1-420)"
  python3 $W19 sheet $OUT/shots/rec-$RECNAME.png ${1:-all} $OUT/rec/$RECNAME/f*.png >/dev/null
}
rec_clean() { rm -rf $OUT/rec/$RECNAME; }
ribbon_box() { RBOX=$(python3 $W19 ribbon $OUT/ui.xml); }
shot_t() {  # $1 — име, $2 — ms от t0: снимка точно тогава
  while [ $(( $(now_ms) - t0 )) -lt $2 ]; do sleep 0.1; done; shot $1 >/dev/null
}
card_open() {  # картата (добавя номера, ако я няма); 0 — картата е на екрана
  card_screen; dump; card_shown && return 0
  card_add $CARD_NO; card_screen; dump; card_shown
}
sensors_info() {
  log "SENSORS: $(a dumpsys sensorservice 2>/dev/null | tr -d '\r' | grep -iE '^ *0x[0-9a-f]+\) |handle' | grep -ioE '(game rotation vector|rotation vector|accelerometer|gyroscope|magnetic field)[^|]{0,30}' | sort -u | tr '\n' ';' | cut -c1-300)"
  log "emu console: sensor status → $(emu sensor status | cut -c1-200)"
}
tilt_seq() {  # наклон: ускорението на емулатора (гравитацията) по осите, ~1.2 s на положение
  local v
  for v in "4.0:9.0:0" "-4.0:9.0:0" "0:8.0:5.5" "0:9.0:-4" "5:7.5:4" "0:9.81:0"; do
    emu sensor set acceleration $v >/dev/null; sleep 1.2
  done
}
shake() {  # два удара над 2.3 g за < 0.7 s
  emu sensor set acceleration 0:32:0 >/dev/null; sleep 0.2
  emu sensor set acceleration 0:9.81:0 >/dev/null; sleep 0.15
  emu sensor set acceleration 0:-28:4 >/dev/null; sleep 0.2
  emu sensor set acceleration 0:9.81:0 >/dev/null
}
open_event() {  # $1 — поредното събитие в списъка (1…)
  local p
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar
  dump; has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък' >/dev/null; sleep 2; }
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $((H/4)) $((H*85/100)) 150 | sed -n ${1:-1}p)
  [ -n "$p" ] || { log "no event row $1: $(texts 8 | cut -c1-200)"; return 1; }
  a input tap $p; sleep 4; dump; has "$RX_BACK"
}
article_url() { [ -n "${ART_URL:-}" ] || ART_URL=$(python3 $H_/find_article.py 2>/dev/null | cut -f1); echo "$ART_URL"; }
open_article() {
  local u; u=$(article_url)
  if [ -n "$u" ]; then link "$u" >/dev/null; sleep 5
  else link "$SITE/news" >/dev/null; sleep 5; dump; a input tap $(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/4)) $((H*3/4)) 200); sleep 4; fi
  dump; has 'Режим за четене|Reading mode'
}
chooser_up() {  # системният избор „Сподели“ отгоре?
  case "$(fg)" in *[Cc]hooser*|*intentresolver*|*Resolver*) return 0;; esac; return 1
}
share_pick_helper() {  # $1 — етикет: „PC share“ в избора → какво стига до получателя (PCSHARE)
  local i
  for i in 1 2 3; do dump; [ -n "$(xy text '=PC share')" ] && break; sw_up 300; sleep 1.5; done
  if tap text '=PC share' >/dev/null; then
    sleep 3; log "SHARE $1 received by the helper: $(since sh-$1 PCSHARE | tail -1 | sed 's/.*PCSHARE *: //' | cut -c1-460)"
  else log "SHARE $1: 'PC share' not in the chooser: $(texts 12 | cut -c1-200)"; fi
  back 1.5; dump; chooser_up && back 1.5
  has "package=\"$PKG\"" || { launch >/dev/null; sleep 2; }
}
share_image() {  # $1 — етикет: на екран с бутон „Сподели“ → „Сподели като картинка“ → избора
  local i f n
  mark sh-$1
  [ $ROOT = 1 ] && a "rm -rf /data/data/$PKG/cache/share"
  tap content-desc '=Сподели|=Share' >/dev/null || { log "SHARE $1: no share button: $(texts 10 | cut -c1-200)"; debug_ui share-$1; return 1; }
  sleep 2; dump; shot share/$1-0-sheet
  has 'Сподели като картинка' || { log "SHARE $1: FAIL — no choice sheet ($(texts 8 | cut -c1-200))"; return 1; }
  log "SHARE $1 sheet: $(grep -oE 'text="(Сподели като картинка|Сподели връзката|Карта с QR[^"]*|Текст с адреса[^"]*)"' $OUT/ui.xml | tr '\n' ' ')"
  tap text '=Сподели като картинка' >/dev/null
  for i in $(seq 1 20); do sleep 1; chooser_up && break; done
  f=$(fg); shot share/$1-1-chooser
  log "SHARE $1: chooser $(chooser_up && echo OPEN || echo 'NOT open') after ${i} s (fg $f); app log: $(since sh-$1 'chyavorec|ShareImage|share' | grep -iE 'exception|error' | head -2 | cut -c1-160 | tr '\n' '~')"
  if [ $ROOT = 1 ]; then
    log "SHARE $1 cache/share: $(a "ls -l /data/data/$PKG/cache/share/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';')"
    n=$(a "ls -t /data/data/$PKG/cache/share/ 2>/dev/null" | tr -d '\r' | head -1)
    if [ -n "$n" ]; then mkdir -p $OUT/shots/share; timeout 20 adb pull "/data/data/$PKG/cache/share/$n" $OUT/shots/share/$1-image.png >/dev/null 2>&1
      log "SHARE $1 PNG: $n → $(python3 $W19 png $OUT/shots/share/$1-image.png)"; fi
  fi
  chooser_up && share_pick_helper $1
}
share_link() {  # $1 — етикет: „Сподели връзката“
  mark sh-$1
  tap content-desc '=Сподели|=Share' >/dev/null || return 1
  sleep 2; dump; tap text '=Сподели връзката' >/dev/null || { log "SHARE $1: no 'Сподели връзката'"; back 1; return 1; }
  local i; for i in $(seq 1 10); do sleep 1; chooser_up && break; done
  log "SHARE $1 (link): chooser $(chooser_up && echo OPEN || echo 'NOT open') (fg $(fg))"; shot share/$1-chooser
  chooser_up && share_pick_helper $1
}

# ================================================================= A. FULLY DRAWN, двете издания
part_drawn19() {
  local r K i
  anims 1
  for r in base-1 new-1 base-2 new-2; do
    K=${r%-*}
    sec "F-$r. Install $K, first launch, warm-up; 6 cold starts online with 'Fully drawn' (animations ON)"
    inst $(apk_of $K) $r $(ref_of $K) || continue
    a input keyevent KEYCODE_HOME; sleep 2
    mark fd-fresh-$r; launch >/dev/null; log "DRAWN $r fresh install (onboarding): $(fully_drawn fd-fresh-$r)"; sleep 3
    dismiss; ready; dims; tab catalog; waitfor "$RX_RESULTS" 150; tab news; sleep 5; tab home; sleep 5
    for i in $(seq 1 6); do cold_drawn $K; done
  done
  sec "F. Totals (12 cold starts per build)"
  drawn_sum new; drawn_sum base
}

# ================================================================= A. JANK + „Бързодействие“
jank19_app() {  # $1 — new | base
  local K=$1 i b t0 c0
  sec "J-$K. Install $K, warm-up, library card $CARD_NO"
  inst $(apk_of $K) $K $(ref_of $K) || return
  anims 1
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$K first-install catalogue results after ${WAITED} ms"
  tab news; sleep 4; tab home; sleep 4
  card_open >/dev/null || log "$K: card not shown"
  if [ $K = new ]; then
    sec "D0. A few starts for the card: 4 cold + 3 warm"
    for i in 1 2 3 4; do a am force-stop $PKG; sleep 3; mark perf-cold-$i; launch | tr '\n' ' '; echo " | $(fully_drawn perf-cold-$i)"; sleep 4; done
    for i in 1 2 3; do a input keyevent KEYCODE_HOME; sleep 3; launch | tr '\n' ' '; echo; sleep 3; done
  fi
  sec "J-$K. Home after a cold start: the first 25 s (ribbon weaving, carousel), no input"
  a am force-stop $PKG; sleep 3
  t0=$(now_ms); launch >/dev/null; gfx_reset
  shot_t jank/$K-ribbon-03s 3000; shot_t jank/$K-ribbon-08s 8000; shot_t jank/$K-ribbon-14s 14000; shot_t jank/$K-ribbon-20s 20000
  while [ $(( $(now_ms) - t0 )) -lt 25000 ]; do sleep 0.2; done
  gfx_save $K-home-ribbon
  sec "J-$K. Home 25-40 s after the cold start (ribbon done), no input"
  gfx_reset; sleep 15; gfx_save $K-home-idle; shot jank/$K-ribbon-40s
  dump; ribbon_box
  if [ -n "$RBOX" ]; then
    log "RIBBON $K box [$RBOX]: 3 s $(python3 $W19 rstat $OUT/shots/jank/$K-ribbon-03s.png "$RBOX"); 40 s $(python3 $W19 rstat $OUT/shots/jank/$K-ribbon-40s.png "$RBOX")"
    log "RIBBON $K change 3→8→14→20→40 s: $(python3 $W19 seq "$RBOX" $OUT/shots/jank/$K-ribbon-{03s,08s,14s,20s,40s}.png)"
  else log "RIBBON $K: header/search bounds not found"; fi
  sec "J-$K. Scroll scenarios (animations ON, ${W}x${H}): ~10 flings each, gfxinfo reset before each"
  jank_scn home-scroll $K
  link "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; sleep 2
  jank_scn news $K
  if open_article; then sleep 2; jank_scn article $K; else log "$K article: not opened ($(texts 6 | cut -c1-160))"; fi
  link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" 60; sleep 2; log "$K catalog: $(rcount) results"
  jank_scn catalog $K
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 40 && open_album 1 && open_photo 1; then
    sleep 2; log "$K viewer: $(viewer_page)"; gfx_reset
    for i in $(seq 1 15); do sw_left 150; sleep 0.8; done
    gfx_save $K-viewer; shot jank/$K-viewer; log "$K viewer after 15 swipes: $(viewer_page)"
    back 1.5; back 1.5; back 1.5
  else log "$K viewer: not reached"; debug_ui viewer-$K; fi
  sec "J-$K. Library card screen: shader + foil (1.2.33), idle then emulated tilt"
  sensors_info
  if card_open; then
    c0=$(now_ms); sleep 1; shot jank/$K-card-0; gfx_reset; sleep 12; gfx_save $K-card-idle
    gfx_reset; tilt_seq; shot jank/$K-card-tilted; gfx_save $K-card-tilt
    log "CARD $K diff before/after tilt (upper half): $(python3 $W19 rdiff $OUT/shots/jank/$K-card-0.png $OUT/shots/jank/$K-card-tilted.png '0 200 1080 1300')"
    while [ $(( $(now_ms) - c0 )) -lt 36000 ]; do sleep 0.3; done
    gfx_reset; sleep 10; gfx_save $K-card-after-36s
    back 1.5
  else log "$K card screen: not shown ($(texts 8 | cut -c1-200))"; debug_ui card-$K; fi
  mem $K-after-scroll
  if [ $K = new ]; then
    sec "D. Diagnostics › „Бързодействие“ after the starts and the scenarios"
    a input keyevent KEYCODE_HOME; sleep 3; launch >/dev/null; sleep 4; dismiss
    perf_card $K-filled
    log "DIAG records (shader errors?): $(grep -oE 'text="[^"]*(shader|Shader|шейдър)[^"]*"' $OUT/perf-$K-filled.xml | head -3 | tr '\n' ' ')"
    back 1.5; back 1.5
  fi
  anims 0
}
part_jank19() {
  if [ "$PART" = jank19-2 ]; then jank19_app base; jank19_app new; else jank19_app new; jank19_app base; fi
  sec "J. Totals"
  grep -E '^GFX ' $SUM | sort -k2 | tee $OUT/gfx-totals.txt
}

# ================================================================= A. CPU БЕЗ ДОКОСВАНЕ
idle19_app() {  # $1 — new | base
  local K=$1 jp
  sec "I-$K. Install $K, warm-up, library card"
  inst $(apk_of $K) $K $(ref_of $K) || return
  anims 1
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150; tab news; sleep 4; tab home; sleep 4
  card_open >/dev/null || log "$K: card not shown"
  sec "I-$K. Home: cold start, 0-24 s (ribbon + carousel) and 37-67 s (should be ~0)"
  a am force-stop $PKG; sleep 3; launch >/dev/null; sleep 1
  cpu_win $K-home-0-25s 24
  sleep 12; cpu_win $K-home-37-67s 30
  a dumpsys cpuinfo 2>/dev/null | tr -d '\r' | grep -E "$PKG|TOTAL" | head -3 | sed 's/^/  cpuinfo: /' | tee -a $SUM
  sec "I-$K. Library card: first 30 s (shader budget), then 35-65 s, then with sensor jitter"
  if card_open; then
    cpu_win $K-card-0-30s 30
    sleep 5; cpu_win $K-card-35-65s 30
    ( for jp in $(seq 1 40); do emu sensor set acceleration "0.$((jp % 3)):9.$((70 + jp % 9)):0.$((jp % 4))" >/dev/null; sleep 0.5; done ) & jp=$!
    cpu_win $K-card-jitter 20; wait $jp 2>/dev/null
    emu sensor set acceleration 0:9.81:0 >/dev/null
    back 1.5
  fi
  sec "I-$K. Other screens, 8 s after opening, 20 s each"
  if open_article; then sleep 8; cpu_win $K-article 20; fi
  link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" 60; sleep 8; cpu_win $K-catalog 20
  ready; tab my; sleep 8; cpu_win $K-my 20
  link "$SITE/news" >/dev/null; sleep 8; cpu_win $K-news 20
  ready; tab home; sleep 8; cpu_win $K-home-return 20
  sec "I-$K. In the background (Home key)"
  go_home 5; cpu_win $K-background 30
}
part_idle19() { idle19_app new; idle19_app base; anims 0; }

# ================================================================= B. НОВОТО НА ВЪЛНА 19
f_ribbon() {
  local i
  sec "B1. Home ribbon „жива шевица“ (AGSL, API $SDK): renders, sweeps ~24 s, then stops"
  a am force-stop $PKG; sleep 3
  t0=$(now_ms); launch >/dev/null; gfx_reset
  for i in 02 05 08 12 16 20 24 28 34; do shot_t func/ribbon-${i}s $((10#$i * 1000)); done
  log "RIBBON frames in the first 34 s: $(gfx_total)"
  dump; ribbon_box
  if [ -n "$RBOX" ]; then
    for i in 02 12 34; do log "RIBBON box [$RBOX] at ${i} s: $(python3 $W19 rstat $OUT/shots/func/ribbon-${i}s.png "$RBOX")"; done
    log "RIBBON change between shots (2,5,8,12,16,20,24,28,34 s): $(python3 $W19 seq "$RBOX" $OUT/shots/func/ribbon-{02,05,08,12,16,20,24,28,34}s.png)"
    sleep 6; shot func/ribbon-40s
    log "RIBBON still after the budget (34 s → 40 s): diff $(python3 $W19 rdiff $OUT/shots/func/ribbon-34s.png $OUT/shots/func/ribbon-40s.png "$RBOX")"
  else log "RIBBON: header/search bounds not found ($(texts 8 | cut -c1-200))"; debug_ui ribbon; fi
  sec "B1b. The first scroll stops the ribbon (new process)"
  a am force-stop $PKG; sleep 3; t0=$(now_ms); launch >/dev/null
  shot_t func/ribbon-scroll-03s 3000
  sw_up 200; sleep 1; sw_down 200; sleep 1.5
  t0=$(now_ms); shot func/ribbon-scroll-a; shot_t func/ribbon-scroll-b 3000; shot_t func/ribbon-scroll-c 6000
  [ -n "$RBOX" ] && log "RIBBON after the first scroll: change a→b→c $(python3 $W19 seq "$RBOX" $OUT/shots/func/ribbon-scroll-{a,b,c}.png) (≈0 = stopped)"
  sec "B1c. Once per process: back to Home from another tab — no new sweep"
  tab catalog; sleep 3; tab home; t0=$(now_ms); shot func/ribbon-again-a; shot_t func/ribbon-again-b 3000
  [ -n "$RBOX" ] && log "RIBBON after a tab round trip: change $(python3 $W19 seq "$RBOX" $OUT/shots/func/ribbon-again-{a,b}.png)"
  log "RIBBON shader errors in logcat: $(grep -ciE 'RuntimeShader|AGSL|shevitsa-shader|SkRuntimeEffect' $OUT/logcat-live.txt)"
}
f_rosette() {
  sec "B2. Bottom-nav rosette morph (recording while switching tabs)"
  ready; dims; dump
  declare -A TXY; local t
  for t in catalog my news more home; do TXY[$t]=$(xy --tab "$(tablabel $t)" "$( [ $t = more ] && echo '=Home|=Начало' || echo '=More|=Още')"); done
  rec_start rosette 6
  for t in catalog my news more home; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 0.9; done
  rec_end "0 $((H - 330)) $W $H"; rec_clean
  dump; log "ROSETTE: tab selected after the round: $(python3 $W16 tabsel $OUT/ui.xml)"
}
f_groups() {
  sec "B3. Connected button groups: News / Calendar"
  link "$SITE/news" >/dev/null; sleep 5
  log "GROUP news/calendar before: $(segstate)"; shot func/group-0
  segment calendar; sleep 1; log "GROUP after tapping 'Календар': $(segstate)"; shot func/group-1-calendar
  segment news; sleep 1; log "GROUP after tapping 'Новини': $(segstate)"
  dump; log "GROUP a11y: $(python3 $H_/find.py $OUT/ui.xml --nodes text '=Новини' 2>/dev/null | grep -oE '(checkable|checked|selected|clickable)="[a-z]+"' | tr '\n' ' ')"
}
f_article() {
  local p s
  sec "B4. Article: stitched progress thread, hero parallax, floating toolbar (Аа / bookmark / share / browser)"
  open_article || { log "ARTICLE: not opened ($(texts 8 | cut -c1-200))"; debug_ui article; return 1; }
  shot func/article-0; dump
  log "ARTICLE toolbar at open: $(grep -oE 'content-desc="(Режим за четене|Запази|Премахни[^"]*|Сподели|Отвори в сайта)"' $OUT/ui.xml | tr '\n' ' ')"
  a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*45/100)) 500; sleep 1.5; shot func/article-1-small-scroll
  log "ARTICLE hero parallax: top area diff after a small scroll $(python3 $W19 rdiff $OUT/shots/func/article-0.png $OUT/shots/func/article-1-small-scroll.png "0 200 $W 900")"
  sw_up 300; sleep 0.6; sw_up 300; sleep 1.5; dump; shot func/article-2-down
  log "ARTICLE toolbar after scrolling down: $(has 'content-desc="Режим за четене"' && echo 'VISIBLE (expected hidden)' || echo hidden)"
  sw_down 400; sleep 1.5; dump; shot func/article-3-up
  log "ARTICLE toolbar after scrolling up: $(has 'content-desc="Режим за четене"' && echo visible || echo 'HIDDEN (expected visible)')"
  log "ARTICLE thread (top 260 px) changed with reading: $(python3 $W19 rdiff $OUT/shots/func/article-0.png $OUT/shots/func/article-2-down.png "0 0 $W 260")"
  sec "B3b. Reader text size (connected button group in the 'Аа' sheet)"
  if tap content-desc '=Режим за четене' >/dev/null; then
    sleep 2; dump; shot func/reader-sheet-0
    log "READER sizes: $(for s in 'Малък шрифт' 'Нормален шрифт' 'Голям шрифт' 'Много голям шрифт'; do echo -n "$s[$(xy --state content-desc "=$s" | grep -oE 'checked=[a-z]+')] "; done)"
    tap content-desc '=Голям шрифт' >/dev/null; sleep 1.5; dump; shot func/reader-sheet-1-large
    log "READER after 'Голям шрифт': $(for s in 'Нормален шрифт' 'Голям шрифт'; do echo -n "$s[$(xy --state content-desc "=$s" | grep -oE 'checked=[a-z]+')] "; done)"
    tap content-desc '=Нормален шрифт' >/dev/null; sleep 1; back 1.5
  else log "READER: no 'Режим за четене' button"; fi
  dump; p=$(xy content-desc '=Запази|=Премахни от запазените')
  if [ -n "$p" ]; then tap content-desc '=Запази' >/dev/null; sleep 1.5; dump; log "ARTICLE bookmark: now $(grep -oE 'content-desc="(Запази|Премахни[^"]*)"' $OUT/ui.xml | head -1)"; fi
  sec "B11c. Article toolbar: share as image"
  dump; has 'content-desc="Сподели"' || { sw_down 300; sleep 1; dump; }
  share_image article
  dump; has 'Режим за четене' || open_article >/dev/null
  dump; if tap content-desc '=Отвори в сайта' >/dev/null; then sleep 4; log "ARTICLE 'Отвори в сайта' → fg $(fg)"; shot func/article-browser; back 2; fi
}
f_gallery() {
  sec "B5. Gallery viewer floating toolbar: share / open on site / photo info"
  if ! { open_gallery && waitfor "$RX_GALLERY_LOADED" 40 && open_album 1 && open_photo 1; }; then log "VIEWER: not reached"; debug_ui viewer; return 1; fi
  sleep 2; dump; shot func/viewer-0
  log "VIEWER toolbar: $(grep -oE 'content-desc="(Сподели|Отвори в сайта|За снимката)"' $OUT/ui.xml | tr '\n' ' ')"
  if tap content-desc '=За снимката' >/dev/null; then sleep 2; dump; shot func/viewer-info; log "VIEWER 'За снимката': $(texts 10 | cut -c1-300)"; back 1.5; fi
  dump; viewer_page >/dev/null
  mark sh-viewer
  if tap content-desc '=Сподели' >/dev/null; then
    sleep 3; dump; log "VIEWER 'Сподели' → fg $(fg); sheet: $(grep -oE 'text="(Сподели като картинка|Сподели връзката)"' $OUT/ui.xml | tr '\n' ' ')"; shot func/viewer-share
    chooser_up && share_pick_helper viewer
    dump; has 'Сподели като картинка' && back 1.5
  fi
  dump; [ -n "$(viewer_page)" ] || { log "VIEWER closed after share? $(texts 6 | cut -c1-160)"; }
  if tap content-desc '=Отвори в сайта' >/dev/null; then sleep 4; log "VIEWER 'Отвори в сайта' → fg $(fg)"; shot func/viewer-site; back 2; fi
  back 1.5; back 1.5; back 1.5
}
f_bookopen() {
  local p
  sec "B6. Book open: catalogue → book (cover rotates around the spine) and back (recording)"
  link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" 60; sleep 2; dump
  p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*85/100)) 150)
  [ -n "$p" ] || { log "BOOKOPEN: no result row"; return 1; }
  rec_start bookopen 6
  a input tap $p; sleep 2.6; back 0.1; sleep 2.2
  rec_end "0 150 $W $((H - 250))"; rec_clean
  dump; log "BOOKOPEN after back: catalogue $(has "$RX_RESULTS" && echo shown || echo NOT shown)"
  rec_start bookopen-home 5
  ready; tab home >/dev/null; sleep 1.5; dump
  p=$(xy --all content-desc 'Корица|корица' | head -1)
  [ -n "$p" ] && { a input tap $p; sleep 2.2; back 1; }
  rec_end; rec_clean
}
f_refresh() {
  sec "B7. Pull-to-refresh rosette (News list), shots during a slow pull"
  link "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news
  adb shell input swipe $((W/2)) $((H*28/100)) $((W/2)) $((H*72/100)) 2600 & local sp=$!
  sleep 1.0; shot func/refresh-1-pull; sleep 0.9; shot func/refresh-2-pull; wait $sp
  sleep 0.4; shot func/refresh-3-released; sleep 2.5; shot func/refresh-4-after
  log "REFRESH: top area diff pull vs after: $(python3 $W19 rdiff $OUT/shots/func/refresh-2-pull.png $OUT/shots/func/refresh-4-after.png "0 200 $W 900")"
}
f_empty() {
  sec "B8. Empty state with the stitched band: Want to read (empty on a fresh install)"
  open_want_list && { sleep 2; dump; shot func/empty-want; log "EMPTY want: $(texts 8 | cut -c1-240)"; back 1.5; }
}
f_confetti() {
  sec "B9. Confetti + haptics on 'Искам да прочета' (book screen)"
  link "$BOOK_URL" >/dev/null; sleep 4; dump
  [ -z "$(xy text "$L_WANT")" ] && { sw_up 300; sleep 1; dump; }
  if [ -n "$(xy text "$L_WANT")" ]; then
    log "WANT before: $(xy --state text "$L_WANT")"
    rec_start confetti-want 4; tap text "$L_WANT" >/dev/null; rec_end
    log "CONFETTI want-to-read: $(python3 $W19 confetti all $OUT/rec/confetti-want/f001.png $OUT/rec/confetti-want/f0*.png | cut -c1-300)"; rec_clean
    dump; log "WANT after: $(xy --state text "$L_WANT"); snackbar: $(grep -oE 'text="[^"]*(Искам да прочета|добав)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
    log "HAPTIC after the add: $(a dumpsys vibrator_manager 2>/dev/null | tr -d '\r' | grep -E "$PKG" | tail -2 | cut -c1-200 | tr '\n' '~')"
  else log "WANT: no 'Искам да прочета' on the book screen"; fi
  sec "B11b. Book: share as image, then share the link"
  share_image book
  dump; has "$RX_BACK" || link "$BOOK_URL" >/dev/null
  sleep 2; dump; share_link book-link
  back 1.5
}
f_event() {
  sec "B10. Event: reminder set (confetti) and share as image"
  open_event 1 || { log "EVENT: not opened"; debug_ui event; return 1; }
  log "EVENT: $(texts 4 | cut -c1-160)"
  local i; for i in 1 2 3; do [ -n "$(xy text '=Напомни ми')" ] && break; sw_up 400; sleep 1; dump; done
  if [ -n "$(xy text '=Напомни ми')" ]; then
    rec_start confetti-reminder 4; tap text '=Напомни ми' >/dev/null; rec_end
    log "CONFETTI reminder: $(python3 $W19 confetti all $OUT/rec/confetti-reminder/f001.png $OUT/rec/confetti-reminder/f0*.png | cut -c1-300)"; rec_clean
    dump; log "REMINDER: $(grep -oE 'text="Ще получиш напомняне[^"]*"' $OUT/ui.xml | head -1); bell: $(has 'Премахни напомнянето' && echo on || echo '?')"
  else log "EVENT: no 'Напомни ми' (past event or none)"; fi
  for i in 1 2 3; do sw_down 400; sleep 0.5; done; dump
  share_image event
}
f_surprise() {
  sec "B12. 'Изненадай ме': the dice button in the catalogue"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1; dump
  if [ -z "$(xy content-desc '=Изненадай ме')" ]; then log "SURPRISE: no dice button: $(grep -oE 'content-desc="[^"]+"' $OUT/ui.xml | head -12 | tr '\n' ' ')"; debug_ui dice; else
    rec_start surprise-dice 6; tap content-desc '=Изненадай ме' >/dev/null; sleep 1.2; shot func/surprise-shuffle; rec_end; rec_clean
    sleep 1; dump; shot func/surprise-result
    log "SURPRISE dice: $(grep -oE 'text="(Предложение|Към книгата|Още една|Избирам книга…|Няма налична книга[^"]*)"' $OUT/ui.xml | tr '\n' ' '); bottom bar $(has 'text="(Начало|Каталог)"' && echo 'VISIBLE (expected hidden)' || echo hidden)"
    if tap text '=Към книгата' >/dev/null; then sleep 3; dump; log "SURPRISE 'Към книгата' → book screen $(has "$RX_BACK" && echo yes || echo no): $(texts 3 | cut -c1-120)"; back 1.5; fi
    dump; has 'text="Предложение"' && { tap content-desc '=Затвори' >/dev/null || back 1.5; sleep 1; }
  fi
  sec "B13. 'Изненадай ме' by shaking (emulator accelerometer: two peaks > 2.3 g within 0.7 s)"
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 1.5
  log "accelerometer: $(a dumpsys sensorservice 2>/dev/null | tr -d '\r' | grep -m1 -iE 'accelerometer' | cut -c1-120)"
  mark shake1
  emu sensor set acceleration 0:30:0 >/dev/null; emu sensor set acceleration 0:9.81:0 >/dev/null
  sleep 2.5; dump; log "SHAKE one short peak (info; a peak held > 120 ms counts twice by design): $(has 'text="(Предложение|Избирам книга…)"' && echo TRIGGERED || echo 'not triggered')"
  has 'text="Предложение"' && { tap content-desc '=Затвори' >/dev/null || back 1.5; sleep 1.5; }
  shake; sleep 0.8; shot func/shake-1; sleep 2; dump; shot func/shake-2
  log "SHAKE double peak: $(has 'text="(Предложение|Избирам книга…|Към книгата)"' && echo TRIGGERED || echo 'not triggered'); $(grep -oE 'text="(Предложение|Към книгата)"' $OUT/ui.xml | tr '\n' ' ')"
  if ! has 'text="(Предложение|Избирам книга…|Към книгата)"'; then
    log "  emu output: '$(emu sensor get acceleration)'"; shake; sleep 0.3; shake; sleep 3; dump
    log "SHAKE retry (2 × double peak): $(has 'text="(Предложение|Към книгата)"' && echo TRIGGERED || echo 'not triggered')"
  fi
  has 'text="Предложение"' && { tap content-desc '=Затвори' >/dev/null || back 1.5; sleep 1; }
  emu sensor set acceleration 0:9.81:0 >/dev/null
  sec "B13b. Shake on another tab (catalogue not resumed) must not trigger"
  ready; tab home; sleep 2; shake; sleep 2.5; dump
  log "SHAKE on Home: $(has 'text="(Предложение|Към книгата)"' && echo 'TRIGGERED (unexpected)' || echo 'not triggered')"
}
part_func19() {
  helper_install
  anims 1
  warmup
  f_ribbon; f_rosette; f_groups; f_refresh; f_empty; f_article; f_gallery; f_bookopen; f_confetti; f_event; f_surprise
}

# ================================================================= B. ГОДИНАТА (devDebug, демо вход)
adump_q() { a am broadcast -a org.pc.helper.DUMP --es id t$1 >/dev/null; sleep 2.5; grep -E "PCDUMP *: t$1 " $OUT/logcat-live.txt > $OUT/a11ytree-$1.txt 2>/dev/null; }
year_state() { adump_q $1; grep -oE 'state=(Слайд|Slide) [0-9]+ [^ ]+ [0-9]+' $OUT/a11ytree-$1.txt | head -1; }
part_year19() {
  local p i s0 s1 s2
  helper_install
  sec "Y0. devDebug (org.chyavorec.app.dev, demo data)"
  use_pkg org.chyavorec.app.dev
  timeout 180 adb install -r app-dev.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  log "installed: $(cat apk-dev-ref.txt 2>/dev/null); $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  anims 1
  warmup
  sec "Y1. Demo sign-in (DEMO-0001 / demo)"
  ready; tab my; sleep 3; tap text '=Вход' >/dev/null || log "no 'Вход' in My: $(texts 10 | cut -c1-200)"
  sleep 3; dump; p=$(xy --all class EditText | sed -n 1p)
  if [ -n "$p" ]; then
    a input tap $p; sleep 0.8; a input text DEMO-0001; sleep 0.5
    dump; p=$(xy --all class EditText | sed -n 2p); a input tap $p; sleep 0.8; a input text demo; sleep 0.5; hide_ime
    tap text '=Вход' >/dev/null; sleep 6
  fi
  ready; tab my; sleep 3; dump; shot year/00-my-signed-in
  log "MY after sign-in: $(texts 12 | cut -c1-300)"
  sec "Y2. The year card on „Моето“"
  for i in 1 2 3 4 5 6; do dump; [ -n "$(xy text 'Моята читателска година')" ] && break; sw_up 350; sleep 1.2; done
  shot year/01-year-card
  log "YEAR card: $(grep -oE 'text="(Моята читателска година[^"]*|[0-9]+ книг[^"]*виж историята|Книгите ти[^"]*)"' $OUT/ui.xml | head -4 | tr '\n' ' ')"
  [ -n "$(xy text 'Моята читателска година')" ] || { log "YEAR: FAIL — no year card on My"; debug_ui year-card; return; }
  sec "Y3. Open the story: 8 slides, auto-advance 5.5 s (accessibility events of the slide state)"
  a11y_on
  mark y-open; t0=$(now_ms)
  tap text 'Моята читателска година' >/dev/null
  for i in 02 07 13 19 30; do shot_t year/10-story-${i}s $((10#$i * 1000)); done
  s0=$(year_state y-at-35s); log "YEAR state at ~35 s: $s0"
  while [ $(( $(now_ms) - t0 )) -lt 52000 ]; do sleep 0.5; done
  s1=$(year_state y-at-52s); log "YEAR state at ~52 s (8 × 5.5 s = 44 s → last slide, stays): $s1"; shot year/11-story-end
  since y-open 'PCLIVE' > $OUT/year-live.txt
  python3 $W19 slides $OUT/year-live.txt | tee -a $SUM
  sec "Y4. Tap left (previous) ×2, tap right (next)"
  a input tap $((W*12/100)) $((H/2)); sleep 1.2; a input tap $((W*12/100)) $((H/2)); sleep 1.2
  s0=$(year_state y-left2); log "YEAR after 2 × left tap: $s0"
  a input tap $((W*80/100)) $((H/2)); sleep 1.2; s1=$(year_state y-right); log "YEAR after a right tap: $s1"
  sec "Y5. Hold (press 8 s) pauses the auto-advance"
  s0=$(year_state y-hold-0)
  adb shell input swipe $((W/2)) $((H*60/100)) $((W/2)) $((H*60/100)) 8000 & p=$!
  sleep 6.5; s1=$(year_state y-hold-during); wait $p; shot year/12-after-hold
  sleep 7; s2=$(year_state y-hold-after)
  log "YEAR hold: before '$s0' → while held (~9 s) '$s1' → 7 s after release '$s2' (paused = same while held, advances after)"
  sec "Y6. Swipe down closes the story"
  a input swipe $((W/2)) $((H*30/100)) $((W/2)) $((H*85/100)) 350; sleep 2.5; dump; shot year/13-after-swipe-down
  adump_q y-closed
  log "YEAR after swipe down: story $(grep -q 'state=Слайд' $OUT/a11ytree-y-closed.txt && echo 'STILL OPEN' || echo closed); screen: $(texts 6 | cut -c1-160)"
  a11y_off
  sec "Y7. Confetti on renewing a demo loan ('Удължи срока')"
  a am start -a org.chyavorec.app.SHORTCUT -d chyavorec://app/my/loans -n $ACT >/dev/null 2>&1; sleep 4; dump; shot year/20-loans
  if [ -n "$(xy text '=Удължи срока')" ]; then
    rec_start confetti-renew 4; tap text '=Удължи срока' >/dev/null; rec_end
    log "CONFETTI renew: $(python3 $W19 confetti all $OUT/rec/confetti-renew/f001.png $OUT/rec/confetti-renew/f0*.png | cut -c1-300)"; rec_clean
    sleep 1; dump; log "RENEW: $(grep -oE 'text="(Удължено ✓|Заявено удължаване[^"]*)"' $OUT/ui.xml | head -1)"
  else log "RENEW: no 'Удължи срока' in the demo loans: $(texts 10 | cut -c1-200)"; fi
  sec "Y8. Confetti on 'Искам да прочета' (demo account)"
  link "https://chyavorec.org/elektronen-katalog?inv=$INV" >/dev/null; sleep 4; dump
  [ -z "$(xy text "$L_WANT")" ] && { sw_up 300; sleep 1; dump; }
  if [ -n "$(xy text "$L_WANT")" ]; then
    rec_start confetti-want-dev 4; tap text "$L_WANT" >/dev/null; rec_end
    log "CONFETTI want (dev): $(python3 $W19 confetti all $OUT/rec/confetti-want-dev/f001.png $OUT/rec/confetti-want-dev/f0*.png | cut -c1-300)"; rec_clean
  fi
  use_pkg org.chyavorec.app
}

# ================================================================= D. НАМАЛЕНО ДВИЖЕНИЕ
part_motion19() {
  local i
  helper_install
  sec "R0. Reduced motion: animator/transition/window scale 0 (Developer options / „Премахни анимациите“)"
  anims 0; log "scales: $(a settings get global animator_duration_scale | tr -d '\r') $(a settings get global transition_animation_scale | tr -d '\r') $(a settings get global window_animation_scale | tr -d '\r')"
  warmup
  sec "R1. Home: the ribbon is drawn still (no sweep)"
  a am force-stop $PKG; sleep 3; t0=$(now_ms); launch >/dev/null; gfx_reset
  for i in 02 06 12 20; do shot_t motion/ribbon-${i}s $((10#$i * 1000)); done
  log "RM frames in the first 20 s on Home: $(gfx_total)"
  dump; ribbon_box
  [ -n "$RBOX" ] && { log "RM ribbon [$RBOX] $(python3 $W19 rstat $OUT/shots/motion/ribbon-06s.png "$RBOX"); change 2→6→12→20 s: $(python3 $W19 seq "$RBOX" $OUT/shots/motion/ribbon-{02,06,12,20}s.png) (≈0 = still)"; }
  sec "R2. Library card: no sweep"
  if card_open; then sleep 1; shot motion/card-a; gfx_reset; sleep 6; shot motion/card-b
    log "RM card: frames in 6 s $(gfx_total); change $(python3 $W19 rdiff $OUT/shots/motion/card-a.png $OUT/shots/motion/card-b.png '0 200 1080 1300')"; back 1.5; fi
  sec "R3. No confetti on 'Искам да прочета'"
  link "$BOOK_URL" >/dev/null; sleep 4; dump; [ -z "$(xy text "$L_WANT")" ] && { sw_up 300; sleep 1; dump; }
  if [ -n "$(xy text "$L_WANT")" ]; then rec_start rm-want 4; tap text "$L_WANT" >/dev/null; rec_end
    log "RM confetti: $(python3 $W19 confetti all $OUT/rec/rm-want/f001.png $OUT/rec/rm-want/f0*.png | cut -c1-300)"; rec_clean
    dump; log "RM want added: $(xy --state text "$L_WANT")"; fi
  back 1.5
  sec "R4. Nav rosette: static"
  ready; f_rosette_rm
  sec "R5. Book open: no rotation"
  link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" 60; sleep 2; dump
  local p; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*85/100)) 150)
  if [ -n "$p" ]; then rec_start rm-bookopen 5; a input tap $p; sleep 2.2; back 0.1; sleep 1.8; rec_end "0 150 $W $((H - 250))"; rec_clean
    dump; log "RM after book + back: catalogue $(has "$RX_RESULTS" && echo shown || echo NOT shown)"; fi
  sec "R6. 'Изненадай ме' still works"
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 1
  if tap content-desc '=Изненадай ме' >/dev/null; then sleep 2.5; dump; shot motion/surprise
    log "RM surprise: $(grep -oE 'text="(Предложение|Към книгата|Избирам книга…)"' $OUT/ui.xml | tr '\n' ' ')"; tap content-desc '=Затвори' >/dev/null || back 1.5; sleep 1; fi
  sec "R7. Article: plain progress line, toolbar"
  if open_article; then sw_up 300; sleep 1.5; dump; shot motion/article
    log "RM article toolbar after scrolling down: $(has 'content-desc="Режим за четене"' && echo visible || echo hidden)"; sw_down 300; sleep 1.2; dump
    log "RM article toolbar after scrolling up: $(has 'content-desc="Режим за четене"' && echo visible || echo hidden)"; fi
  sec "R8. Battery saver (animations ON): the ribbon must be still"
  anims 1
  a dumpsys battery unplug; a cmd power set-mode 1 >/dev/null 2>&1; a settings put global low_power 1; sleep 2
  log "battery saver: low_power=$(a settings get global low_power | tr -d '\r'); $(a dumpsys power 2>/dev/null | tr -d '\r' | grep -m1 -E 'mSettingBatterySaverEnabled|mBatterySaverEnabled|batterySaver' | tr -s ' ')"
  a am force-stop $PKG; sleep 3; t0=$(now_ms); launch >/dev/null; gfx_reset
  for i in 03 08 14; do shot_t motion/saver-${i}s $((10#$i * 1000)); done
  log "SAVER frames in 14 s: $(gfx_total)"
  [ -n "$RBOX" ] && log "SAVER ribbon change 3→8→14 s: $(python3 $W19 seq "$RBOX" $OUT/shots/motion/saver-{03,08,14}s.png) (≈0 = still)"
  a cmd power set-mode 0 >/dev/null 2>&1; a settings put global low_power 0; a dumpsys battery reset
  sec "R9. Control: animations ON, no saver — the ribbon moves"
  a am force-stop $PKG; sleep 3; t0=$(now_ms); launch >/dev/null; gfx_reset
  for i in 03 08 14; do shot_t motion/ctl-${i}s $((10#$i * 1000)); done
  log "CONTROL frames in 14 s: $(gfx_total)"
  [ -n "$RBOX" ] && log "CONTROL ribbon change 3→8→14 s: $(python3 $W19 seq "$RBOX" $OUT/shots/motion/ctl-{03,08,14}s.png)"
  anims 0
}
f_rosette_rm() {
  dims; dump
  declare -A TXY; local t
  for t in catalog my news more home; do TXY[$t]=$(xy --tab "$(tablabel $t)" "$( [ $t = more ] && echo '=Home|=Начало' || echo '=More|=Още')"); done
  rec_start rm-rosette 6
  for t in catalog my news more home; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 0.9; done
  rec_end "0 $((H - 330)) $W $H"; rec_clean
}

# ================================================================= C. ЖИВИ ИЗВЕСТИЯ (Android 16)
live_notifs() { a dumpsys notification --noredact 2>/dev/null | tr -d '\r' > $OUT/live/notif-$1.txt; python3 $W19 notif $OUT/live/notif-$1.txt $PKG; }
part_live36() {
  local i st t0 first=""
  mkdir -p $OUT/live
  sec "L0. Device: Android $(a getprop ro.build.version.release | tr -d '\r') (SDK $SDK), $(a getprop ro.build.version.incremental | tr -d '\r')"
  log "POST_PROMOTED_NOTIFICATIONS: $(a dumpsys package $PKG | tr -d '\r' | grep -m1 -oE 'android.permission.POST_PROMOTED_NOTIFICATIONS: granted=[a-z]+')"
  sec "L1. Update download as a Live Update: 1.2.33-pre (versionCode $VC_PRE) → the staged test 1.2.33 ($NEW_VC), throttled"
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  log "staged: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["size"], d["apkUrl"][-60:])' $OUT/update.json 2>&1)"
  inst app-new-upd.apk new-upd apk-new-upd-ref.txt || { log "no app-new-upd.apk"; return; }
  log "POST_PROMOTED_NOTIFICATIONS (pre build): $(a dumpsys package $PKG | tr -d '\r' | grep -m1 -oE 'android.permission.POST_PROMOTED_NOTIFICATIONS: granted=[a-z]+')"
  a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  throttle_on
  launch >/dev/null
  if upd_dialog 'text="Изтегли и инсталирай"'; then
    shot live/00-dialog
    tap text '=Изтегли и инсталирай' >/dev/null; t0=$(now_ms)
    for i in $(seq 1 30); do
      live_notifs l$i > $OUT/live/l$i.txt
      [ -z "$first" ] && grep -q 'LIVE id=' $OUT/live/l$i.txt && { first=$(( $(now_ms) - t0 )); log "LIVE update notification at +${first} ms:"; cat $OUT/live/l$i.txt | head -8 | tee -a $SUM; }
      case $i in
        3) go_home 1; shot live/01-statusbar-chip;;
        5) a cmd statusbar expand-notifications; sleep 2.5; shot live/02-shade; dump; cp $OUT/ui.xml $OUT/live/shade.xml
           log "SHADE: $(grep -oE 'text="[^"]*(Изтегляне|версия|%|Отказ)[^"]*"' $OUT/ui.xml | head -5 | tr '\n' ' ')"; a cmd statusbar collapse;;
        8) log "LIVE at +$(( $(now_ms) - t0 )) ms: $(grep -m1 'LIVE id=' $OUT/live/l$i.txt | cut -c1-400)"; shot live/03-statusbar-later;;
      esac
      grep -q 'готова за инсталиране' $OUT/live/notif-l$i.txt && break
      sleep 3
    done
    log "LIVE last capture: $(grep -m1 'LIVE id=' $OUT/live/l$i.txt | cut -c1-400)"
    grep -h -iE 'promot|ProgressStyle|shortCritical|requestPromoted' $OUT/live/notif-l*.txt | sort | uniq -c | sort -rn | head -12 | cut -c1-220 | sed 's/^/  dumpsys: /' | tee -a $SUM
    throttle_off
    upd_wait_bg l-finish || true
    sec "L1b. Install the downloaded update (system installer) → 1.2.33"
    a cmd statusbar expand-notifications; sleep 2.5; dump; shot live/04-ready
    if tap text 'готова за инсталиране' >/dev/null; then
      sleep 4; dump; has 'text="Инсталирай"' && tap text '=Инсталирай' >/dev/null
      installer_confirm $NEW_VC l1b && log "UPDATE: installed $(ver)" || log "UPDATE: NOT installed: $(ver)"
    else log "no 'готова за инсталиране' in the shade: $(texts 10 | cut -c1-200)"; a cmd statusbar collapse; fi
  else log "LIVE: no update dialog: $(texts 10 | cut -c1-200)"; throttle_off; fi
  sec "L2. Event reminder → event Live Update (60 min before; needs the event's start time)"
  inst app.apk new apk-ref.txt
  launch >/dev/null; sleep 6; dismiss; ready; dims
  if open_event 1; then
    local i2; for i2 in 1 2 3; do [ -n "$(xy text '=Напомни ми')" ] && break; sw_up 400; sleep 1; dump; done
    log "EVENT: $(texts 6 | cut -c1-240)"
    tap text '=Напомни ми' >/dev/null && { sleep 2; dump; log "REMINDER: $(grep -oE 'text="Ще получиш напомняне[^"]*"' $OUT/ui.xml | head -1)"; }
    t1=$(now_ms); upc_cap event-reminder >/dev/null 2>&1
    log "WORK after the reminder: $(grep -E 'Worker' $OUT/cap/event-reminder/workdb.txt 2>/dev/null | cut -c4- | tr '\n' ';' | cut -c1-400)"
    log "EventLiveUpdateWorker scheduled: $(grep -c 'EventLiveUpdateWorker' $OUT/cap/event-reminder/workdb.txt 2>/dev/null)"
    live_notifs ev > $OUT/live/ev.txt; log "notifications now: $(tail -1 $OUT/live/ev.txt)"
  fi
  sec "L3. API $SDK smoke: ribbon and card foil render"
  a am force-stop $PKG; sleep 3; t0=$(now_ms); launch >/dev/null
  shot_t live/10-ribbon-03s 3000; shot_t live/11-ribbon-09s 9000; dump; ribbon_box
  [ -n "$RBOX" ] && log "API$SDK ribbon $(python3 $W19 rstat $OUT/shots/live/10-ribbon-03s.png "$RBOX"); change 3→9 s $(python3 $W19 rdiff $OUT/shots/live/10-ribbon-03s.png $OUT/shots/live/11-ribbon-09s.png "$RBOX")"
  card_open && { sleep 2; shot live/12-card; back 1.5; }
}

# ================================================================= B. API 28: БЕЗ AGSL
part_fb28() {
  local p
  helper_install
  dev_locale bg-BG || log "device locale not changed (labels may be English)"
  a am force-stop $PKG
  anims 1
  warmup
  sec "P1. Home ribbon on API $SDK (no AGSL): the still pattern, drawn once"
  a am force-stop $PKG; sleep 3; t0=$(now_ms); launch >/dev/null; gfx_reset
  shot_t fb/ribbon-03s 3000; shot_t fb/ribbon-10s 10000; shot_t fb/ribbon-18s 18000
  log "FB frames in 18 s: $(gfx_total)"
  dump; ribbon_box
  [ -n "$RBOX" ] && log "FB ribbon [$RBOX] $(python3 $W19 rstat $OUT/shots/fb/ribbon-03s.png "$RBOX"); change 3→10→18 s: $(python3 $W19 seq "$RBOX" $OUT/shots/fb/ribbon-{03,10,18}s.png)"
  sec "P2. Library card: fallback foil gradients"
  card_open && { sleep 2; shot fb/card; dump; log "FB card: $(texts 6 | cut -c1-200)"; back 1.5; } || log "FB card: not shown"
  sec "P3. 'Изненадай ме'"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1
  if tap content-desc '=Изненадай ме' >/dev/null; then sleep 3; dump; shot fb/surprise
    log "FB surprise: $(grep -oE 'text="(Предложение|Към книгата|Избирам книга…)"' $OUT/ui.xml | tr '\n' ' '); bottom bar $(has 'text="(Начало|Каталог)"' && echo visible || echo hidden)"; tap content-desc '=Затвори' >/dev/null || back 1.5; sleep 1; fi
  sec "P4. Book open"
  link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" 60; sleep 2; dump
  p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*85/100)) 150)
  [ -n "$p" ] && { rec_start fb-bookopen 5; a input tap $p; sleep 2.4; back 0.1; sleep 1.8; rec_end "0 150 $W $((H - 250))"; rec_clean; }
  sec "P5. Event: share as image"
  open_event 1 && share_image fb-event
  sec "P6. Article: toolbar and thread"
  open_article && { sw_up 300; sleep 1.5; shot fb/article; dump; log "FB article toolbar after scroll down: $(has 'content-desc="Режим за четене"' && echo visible || echo hidden)"; }
}

part_monkey19() { MONKEY_N=4000; part_monkey; }

part_w19() {
  case $PART in
    drawn19) part_drawn19;;
    jank19-1|jank19-2) part_jank19;;
    idle19) part_idle19;;
    func19) part_func19;;
    year19) part_year19;;
    motion19) part_motion19;;
    live36) part_live36;;
    fb28) part_fb28;;
    monkey19) part_monkey19;;
    abstart-1|abstart-2) anims 1; log "animations ON for the start A/B (the ribbon runs at start)"; part_abstart;;
    abstart-3) anims 0; log "animations OFF (as in v132)"; part_abstart;;
    *) return 1;;
  esac
  return 0
}
