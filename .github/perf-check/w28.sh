# TEMPORARY (debug/w27-cat; results in emulator-results/v142perf): main-thread CPU of the frames that are still slow
# after 1.2.40 — the return from a book to the catalogue list, the tab entries, typing in the catalogue search, sort,
# „Покажи още“ and a fling — BASE (org.chyavorec.app.b) against NEW (org.chyavorec.app), and for NEW the variants
# behind launch flags (`--es w28 <flag,flag>`) when NEW is built with an experimental patch (w28-exp.patch).
# A variant label: base | x (NEW without flags) | x-<flag>+<flag>.
# Measured as in part_fix (w27.sh): a Perfetto trace of one interaction, CPU (Running) per doFrame (w28.py meas).
# Loaded by run.sh after w27.sh.
W28_PARTS=" bk33 bk36x ty33 ty36x tb33 tb36x fbk33 fbk36x fty33 fty36x ftb33 ftb36x "
W20_PARTS="$W20_PARTS $W28_PARTS"
W28PY=$H_/w28.py
W28VARS_BK=${W28VARS_BK:-"base x x-bk1 x-bk2 x-one x-nosh x-bk1+one"}
W28VARS_TY=${W28VARS_TY:-"base x x-ta x-tb x-tc x-sug x-ta+sug+one"}
W28VARS_TB=${W28VARS_TB:-"base x"}
[ -f $H_/w28-vars.sh ] && . $H_/w28-vars.sh

v_use() {  # $1 — variant label → PKG/ACT and VFLAGS
  case $1 in
    base) use_pkg org.chyavorec.app.b; VFLAGS="";;
    x) use_pkg org.chyavorec.app; VFLAGS="";;
    x-*) use_pkg org.chyavorec.app; VFLAGS=$(echo "${1#x-}" | tr '+' ',');;
  esac
}
v_stop() { a am force-stop org.chyavorec.app; a am force-stop org.chyavorec.app.b; }
v_launch() {  # a new process of the current variant, on Home
  v_stop; sleep 1
  a am start -W -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $ACT ${VFLAGS:+--es w28 $VFLAGS} >/dev/null 2>&1
  sleep 7; ready >/dev/null; sleep 1.5
}
# One measured interaction: $1 — name (<kind>@<variant>@r<round>[-<i>]), $2 — s after the action, $3… — the action.
wm() {
  local name=$1 secs=$2 rc mb f; shift 2
  sleep 1.5
  tr_start
  a log -t PERFCHECK "'w28 $name'"
  "$@"
  sleep $secs
  tr_stop "$name"
  f="$OUT/tr/$name.pftrace"
  [ -s "$f" ] || { log "M $name: no trace"; return 0; }
  timeout 300 python3 $W28PY meas "$f" $PKG "$name" $OUT/m.csv 2>&1 | cut -c1-400 | tee -a $SUM; rc=${PIPESTATUS[0]}
  mb=$(( $(du -sm $OUT/tr | cut -f1) ))
  if [ "$rc" = 3 ] && [ $mb -lt $TRKEEP_MB ]; then gzip -9 -f "$f"; TRKEPT=$((TRKEPT + 1)); else rm -f "$f"; fi
}
wt() { if [ -z "$3" ]; then log "SKIP $1: no position"; return 1; fi; wm "$1" $2 tapxy $3; }
w_setup() {  # both builds: BASE installed next to NEW, the app language, first launch to the catalogue, speed-profile
  local K
  TR_APPS="org.chyavorec.app org.chyavorec.app.b"
  anims 1
  tr_init; MAPF=""
  log "host cpu: $(grep -m1 'model name' /proc/cpuinfo | cut -d: -f2)"
  sec "S0. NEW (org.chyavorec.app) and BASE (org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile"
  [ -f app-base.apk ] && timeout 240 adb install -r app-base.apk 2>&1 | tail -1
  for K in x base; do
    v_use $K
    a cmd locale set-app-locales $PKG --locales bg-BG >/dev/null 2>&1; a pm grant $PKG android.permission.POST_NOTIFICATIONS >/dev/null 2>&1
    log "S $K: $(ver)"
    launch >/dev/null; sleep 8; ready >/dev/null; dims
    tab catalog >/dev/null; waitfor "$RX_RESULTS" 150 && log "S $K: catalogue after ${WAITED} ms" || log "S $K: NO catalogue results"
    tab news >/dev/null; sleep 4; tab my >/dev/null; sleep 3; tab more >/dev/null; sleep 3; tab home >/dev/null; sleep 3
    x_compile
  done
  v_use x; launch >/dev/null; sleep 6; ready >/dev/null; pos_tabs
  MY_=$(xy --tab "$(tablabel my)" '=More|=Още'); log "tab my [$MY_]"
  gest_init
}
w_order() {  # $1 — round, $2… — variants: odd rounds as given, even rounds reversed
  local r=$1; shift
  if [ $((r % 2)) = 1 ]; then echo "$@"; else echo "$@" | tr ' ' '\n' | tac | tr '\n' ' '; fi
}
w_catalog() {  # tap Каталог, wait for the results
  a input tap $CAT; waitfor "$RX_RESULTS" 30 || log "  (no catalogue results: $(texts 6 | cut -c1-160))"; sleep 2
}
w_end() {
  sec "Z. The table: main-thread CPU (Running) per doFrame, by interaction and variant"
  python3 $W28PY agg $OUT/m.csv 2>&1 | tee $OUT/table.md | tee -a $SUM
  log "CRASH new: FATAL $(grep -A3 'FATAL EXCEPTION' $OUT/logcat-live.txt | grep -c 'Process: org.chyavorec.app,'), base: FATAL $(grep -A3 'FATAL EXCEPTION' $OUT/logcat-live.txt | grep -c 'Process: org.chyavorec.app.b,'); ANR $(grep -c 'ANR in org.chyavorec' $OUT/logcat-live.txt); traces kept: $TRKEPT"
  all_sheets >/dev/null 2>&1 || true
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- task 6: Back from a book to the catalogue list
bk_round() {  # $1 — variant, $2 — round
  local v=$1 r=$2 i p
  v_use $v; v_launch
  w_catalog; dump
  for i in 1 2 3; do
    dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $(results_top) $((H * 8 / 10)) 100 | sed -n ${i}p)
    [ -n "$p" ] || { log "  no row $i ($v)"; continue; }
    a input tap $p; sleep 4.5
    wm "book-back@$v@r$r-$i" 3.5 a input keyevent 4
    dump; has "$RX_RESULTS" || { log "  (not back on the list: $(texts 6 | cut -c1-160))"; ready >/dev/null; w_catalog; }
  done
}
bk_visual() {  # $1 — variant: Back from a book and the open of the next one, recorded (REC_FPS frames/s)
  local v=$1 p
  v_use $v; v_launch; w_catalog; dump
  p=$(python3 $H_/find.py $OUT/ui.xml --clickables $(results_top) $((H * 8 / 10)) 100 | sed -n 2p)
  [ -n "$p" ] || { log "VIS $v: no row"; return 0; }
  # the software-GPU emulator draws a frame in ~0.2-0.5 s: the whole transition takes several seconds
  REC_FPS=8 rec_cmd open-$v emu 7 a input tap $p
  sleep 2; shot bk/$v-book >/dev/null
  REC_FPS=8 rec_cmd back-$v emu 7 a input keyevent 4
  sleep 1; dump; log "VIS $v: back on the list $(has "$RX_RESULTS" && echo yes || echo NO); $(texts 8 | cut -c1-200)"; shot bk/$v-after >/dev/null
}
part_bk() {
  local r v
  w_setup
  for r in $(seq 1 ${W28R_BK:-3}); do
    sec "B$r. Round $r: per variant a new process, Каталог, then rows 1-3: open, Back (measured)"
    for v in $(w_order $r $W28VARS_BK); do bk_round $v $r; done
  done
  sec "BV. Visual: open a book and Back, screen recording at 8 frames/s"
  for v in $W28VARS_BK; do bk_visual $v; done
  w_end
}

# ---------------------------------------------------------------- tasks 8, 9: typing, sort, „Покажи още“, fling
ty_round() {  # $1 — variant, $2 — round
  local v=$1 r=$2 k w n secs p i CLR SEARCH SORT
  v_use $v; v_launch
  w_catalog; dump
  SEARCH=$(xy text "$L_SEARCH"); [ -n "$SEARCH" ] || SEARCH=$(xy class EditText)
  SORT=$(xy content-desc '=Подреждане|=Sort')
  a input tap $SEARCH; sleep 2
  k=0
  for w in приказ любов мария; do
    k=$((k + 1)); n=$(python3 -c "import sys; print(len(sys.argv[1]))" "$w"); secs=$(python3 -c "print(2.5 + $n * 0.35)")
    wm "type@$v@r$r-$k" $secs ime_type "$w" 350
    dump; log "  after type '$w' ($v): $(rcount) results"
    CLR=$(xy content-desc '=Изчисти|=Clear')
    wt "clear@$v@r$r-$k" 3 "$CLR"
  done
  wt "sort-open@$v@r$r" 2.5 "$SORT"; dump; p=$(xy text 'Година (нови първо)|Year (newest'); wt "sort-year@$v@r$r" 3.5 "$p"
  wt "sort-open@$v@r$r-2" 2.5 "$SORT"; dump; p=$(xy text 'Най-подходящи|Most relevant'); wt "sort-rel@$v@r$r" 3.5 "$p"
  [ -n "$p" ] || { back 1.5; }
  # „Покажи още“: down the list with slow swipes until the button is on screen (uiautomator text)
  p=""
  for i in $(seq 1 16); do
    a input swipe $((W / 2)) $((H * 75 / 100)) $((W / 2)) $((H * 30 / 100)) 400; sleep 1.2
    dump; p=$(xy text 'Покажи още|Show more' --maxy $((H * 88 / 100)))
    [ -n "$p" ] && break
  done
  if [ -n "$p" ]; then
    log "  load-more button at [$p] after $i swipes ($v)"
    wt "loadmore@$v@r$r" 3.5 "$p"
    dump; log "  after load more: $(texts 6 | cut -c1-160)"
  else
    log "  load-more button NOT found after $i swipes ($v): $(texts 12 | cut -c1-300)"; debug_ui loadmore-$v
  fi
  a input tap $CAT; sleep 2.5   # tab reselect → top
  wm "fling@$v@r$r" 1.5 gest g-scroll
  a input tap $CAT; sleep 2.5
  wm "drag@$v@r$r" 1.5 a input swipe $((W / 2)) $((H * 80 / 100)) $((W / 2)) $((H * 25 / 100)) 2500
}
part_ty() {
  local r v
  helper_install; ime_init
  w_setup
  ime_use pc
  for r in $(seq 1 ${W28R_TY:-2}); do
    sec "T$r. Round $r: per variant a new process; typing 3 words (test IME, a letter every 350 ms), × (clear), sort by year / relevance, „Покажи още“, a fling, a slow drag"
    for v in $(w_order $r $W28VARS_TY); do ty_round $v $r; done
  done
  sec "TV. Visual: typing a word, recorded"
  for v in $W28VARS_TY; do
    v_use $v; v_launch; w_catalog; dump
    p=$(xy text "$L_SEARCH"); [ -n "$p" ] || p=$(xy class EditText)
    a input tap $p; sleep 2
    REC_FPS=8 rec_cmd type-$v emu 6 ime_type любов 350
  done
  ime_use default
  w_end
}

# ---------------------------------------------------------------- task 7: the entry frame of each main tab
tb_round() {  # $1 — variant, $2 — round
  local v=$1 r=$2
  v_use $v; v_launch
  # the first entry of each tab in the process, then warm entries (the composition is new, the view models are kept)
  wt "tab-news-cold@$v@r$r" 3 "$NEWS"; wt "tab-my-cold@$v@r$r" 3 "$MY_"; wt "tab-more-cold@$v@r$r" 3 "$MORE_"
  wt "tab-catalog-cold@$v@r$r" 4 "$CAT"; wt "tab-home@$v@r$r-1" 3 "$HOME_"
  wt "tab-more@$v@r$r-1" 3 "$MORE_"; wt "tab-my@$v@r$r-1" 3 "$MY_"; wt "tab-news@$v@r$r-1" 3 "$NEWS"; wt "tab-home@$v@r$r-2" 3 "$HOME_"
  wt "tab-news@$v@r$r-2" 3 "$NEWS"; wt "tab-my@$v@r$r-2" 3 "$MY_"; wt "tab-more@$v@r$r-2" 3 "$MORE_"; wt "tab-catalog@$v@r$r" 4 "$CAT"
  wt "tab-home@$v@r$r-3" 3 "$HOME_"
}
tb_visual() {  # $1 — variant: the tab entries recorded
  local v=$1
  v_use $v; v_launch
  REC_FPS=8 rec_cmd tab-news-$v emu 5 a input tap $NEWS
  REC_FPS=8 rec_cmd tab-more-$v emu 5 a input tap $MORE_
  REC_FPS=8 rec_cmd tab-home-$v emu 5 a input tap $HOME_
}
part_tb() {
  local r v
  w_setup
  for r in $(seq 1 ${W28R_TB:-4}); do
    sec "E$r. Round $r: per variant a new process on Home; News, Моето, Още, Каталог, Home (first entries), then warm entries"
    for v in $(w_order $r $W28VARS_TB); do tb_round $v $r; done
  done
  sec "EV. Visual: the tab entries recorded at 8 frames/s"
  for v in $W28VARS_TB; do tb_visual $v; done
  w_end
}

part_w28() {
  case "$W28_PARTS" in *" $PART "*) ;; *) return 1;; esac
  # f* — the final A/B: BASE against NEW without flags, more rounds
  case $PART in f*) W28VARS_BK="base x"; W28VARS_TY="base x"; W28VARS_TB="base x"; W28R_BK=5; W28R_TY=3; W28R_TB=5;; esac
  case $PART in bk*|fbk*) part_bk;; ty*|fty*) part_ty;; tb*|ftb*) part_tb;; esac
  return 0
}
