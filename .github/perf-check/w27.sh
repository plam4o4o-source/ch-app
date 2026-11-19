# TEMPORARY (debug/w27-cat; results in emulator-results/v139cat): what runs on the UI thread in the slow frames of the
# „Каталог“ tab of the released 1.2.39 (main ab175e49). Field data: worst UI frame 104 ms on „catalog“ (6 of 3161 janky).
# Builds (prodRelease minified x86_64, throwaway key, both org.chyavorec.app):
#   app.apk       — 1.2.39 as released, no changes
#   app-hook.apk  — the same + w27-hook.patch (DEBUG ONLY): every main-looper message as a trace section,
#                   profileable by shell, androidx.compose.runtime:runtime-tracing (composable names, if it works)
# Parts: cat33 / cat36x / cat26 — the released code; cath33 / cath36x / cath26 — the instrumented build.
# Every interaction: gfxinfo framestats reset before / dumped after + a trace of just that interaction
# (Perfetto on API 29+, text atrace on API 26), analysed on the host by w27.py. No uiautomator dump inside a
# measured window (an accessibility query runs on the app's main thread): the tap positions are found before.
# Loaded by run.sh after w26.sh.
W27_PARTS=" cat33 cath33 cat36x cath36x cat26 cath26 fx33 fx36x "
W20_PARTS="$W20_PARTS $W27_PARTS"
W27=$H_/w27.py
TRDIR=/data/misc/perfetto-traces
TRKEEP_MB=70
TRKEPT=0

# ---------------------------------------------------------------- tracing
tr_init() {
  local want avail c
  mkdir -p $OUT/tr $OUT/fs27
  case $PART in cath*) MAPF=app-hook-mapping.txt;; *) MAPF=app-mapping.txt;; esac
  [ -f "$MAPF" ] || MAPF=""
  python3 -c "import perfetto" 2>/dev/null || python3 -m pip install --user --break-system-packages -q perfetto==0.59.1 >/dev/null 2>&1 || true
  if python3 -c "import perfetto" 2>/dev/null; then TPOK=1; log "trace analysis: $(timeout 300 python3 $W27 warm 2>&1 | tail -1)"; else TPOK=0; log "trace analysis: NO perfetto python package"; fi
  avail=$(a atrace --list_categories 2>/dev/null | tr -d '\r' | awk '{print $1}' | tr '\n' ' ')
  want="gfx view am dalvik input wm res binder_driver aidl sched"
  CATS=""; for c in $want; do case " $avail " in *" $c "*) CATS="$CATS $c";; esac; done
  if [ "$SDK" -ge 29 ] && a "command -v perfetto" >/dev/null 2>&1 && [ $TPOK = 1 ]; then TRMODE=perfetto; else TRMODE=atrace; fi
  if [ $TRMODE = perfetto ]; then
    {
      echo 'buffers { size_kb: 65536 fill_policy: DISCARD }'
      echo 'buffers { size_kb: 4096 fill_policy: DISCARD }'
      echo 'data_sources { config { name: "linux.ftrace" target_buffer: 0 ftrace_config {'
      for c in sched/sched_switch sched/sched_waking sched/sched_blocked_reason power/suspend_resume task/task_newtask task/task_rename; do echo "  ftrace_events: \"$c\""; done
      for c in $CATS; do [ $c = sched ] || echo "  atrace_categories: \"$c\""; done
      for c in ${TR_APPS:-$PKG}; do echo "  atrace_apps: \"$c\""; done
      echo '  buffer_size_kb: 16384 drain_period_ms: 250'
      echo '} } }'
      echo 'data_sources { config { name: "linux.process_stats" target_buffer: 1 process_stats_config { scan_all_processes_on_start: true } } }'
      echo 'data_sources { config { name: "track_event" target_buffer: 0 track_event_config { enabled_categories: "*" } } }'
      echo 'duration_ms: 60000'
    } > $OUT/w27.pbtx
    timeout 20 adb push $OUT/w27.pbtx /data/local/tmp/w27.pbtx >/dev/null 2>&1
  fi
  log "TRACE MODE $TRMODE; atrace categories:$CATS; mapping ${MAPF:-none}; android $SDK"
}
tr_start() {
  TRPID=""
  if [ $TRMODE = perfetto ]; then
    a rm -f $TRDIR/w27.pftrace
    TRPID=$(a "cat /data/local/tmp/w27.pbtx | perfetto --txt -c - -o $TRDIR/w27.pftrace --background" 2>&1 | tr -d '\r' | grep -oE '^[0-9]+$' | tail -1)
    [ -n "$TRPID" ] || TRPID=$(a pidof perfetto | tr -d '\r' | awk '{print $NF}')
    [ -n "$TRPID" ] || log "  (perfetto did not start)"
    sleep 1.5
  else
    a atrace --async_start -b 16384 -a $PKG $CATS >/dev/null 2>&1; sleep 0.8
  fi
}
tr_stop() {  # $1 — name → $OUT/tr/$1.pftrace | .txt
  if [ $TRMODE = perfetto ]; then
    [ -n "$TRPID" ] && a kill -TERM $TRPID >/dev/null 2>&1
    T=40 a "i=0; while [ \$i -lt 120 ] && kill -0 ${TRPID:-0} 2>/dev/null; do sleep 0.25; i=\$((i+1)); done" >/dev/null 2>&1
    timeout 90 adb pull $TRDIR/w27.pftrace $OUT/tr/$1.pftrace >/dev/null 2>&1 || log "  (no trace file for $1)"
  else
    T=150 a atrace --async_stop > $OUT/tr/$1.txt 2>/dev/null
  fi
}
tr_analyse() {  # $1 — name
  local f rc=0 mb
  if [ $TRMODE = perfetto ]; then
    f=$OUT/tr/$1.pftrace; [ -s $f ] || return 0
    timeout 300 python3 $W27 tp $f $PKG $1 $OUT/fs27/$1.json "$MAPF" $OUT/tr.csv 2>&1 | grep -v '^\s*$' | cut -c1-400 | tee -a $SUM; rc=${PIPESTATUS[0]}
  else
    f=$OUT/tr/$1.txt; [ -s $f ] || return 0
    timeout 300 python3 $W27 at $f "$(pidof_app)" $1 $OUT/fs27/$1.json "$MAPF" $OUT/tr.csv 2>&1 | cut -c1-400 | tee -a $SUM; rc=${PIPESTATUS[0]}
  fi
  # Traces with a main-thread slice over 32 ms are kept (gzip), up to TRKEEP_MB per part.
  mb=$(( $(du -sm $OUT/tr | cut -f1) ))
  if [ "$rc" = 3 ] && [ $mb -lt $TRKEEP_MB ]; then gzip -9 -f $f; TRKEPT=$((TRKEPT + 1)); else rm -f $f; fi
}
# Один измерен прозорец: $1 — име, $2 — s след действието, $3… — действието (само adb input / broadcast, без dump).
meas() {
  local name=$1 secs=$2; shift 2
  sleep ${PRE:-1.5}
  tr_start
  a dumpsys gfxinfo $PKG reset >/dev/null 2>&1; sleep 0.3
  a dumpsys gfxinfo $PKG framestats > $OUT/fs27/before.txt 2>/dev/null
  a log -t PERFCHECK "'w27 $name'"
  "$@"
  sleep $secs
  a dumpsys gfxinfo $PKG framestats > $OUT/fs27/$name.txt 2>/dev/null
  tr_stop $name
  python3 $W27 fs $OUT/fs27/before.txt $OUT/fs27/$name.txt $name $OUT/fs.csv $OUT/fs27/$name.json 2>&1 | cut -c1-300 | tee -a $SUM
  tr_analyse $name
  rm -f $OUT/fs27/$name.txt
}
tapxy() { a input tap $1 $2; }
mtap() {  # $1 — име, $2 — s, $3 — "x y" (празно — пропуска се)
  if [ -z "$3" ]; then log "SKIP $1: no position"; return 1; fi
  meas $1 $2 tapxy $3
}

# ---------------------------------------------------------------- test IME (no keyboard view) for Cyrillic letters
ime_init() {
  IME_DEF=$(a settings get secure default_input_method | tr -d '\r')
  a ime enable org.pc.helper/.Ime >/dev/null 2>&1
  log "IME: default $IME_DEF; enabled: $(a ime list -s 2>/dev/null | tr -d '\r' | tr '\n' ' ')"
}
ime_use() {
  if [ $1 = pc ]; then a ime set org.pc.helper/.Ime >/dev/null 2>&1; else a ime set "$IME_DEF" >/dev/null 2>&1; fi
  sleep 1.5; log "IME now: $(a settings get secure default_input_method | tr -d '\r')"
}
ime_type() { a am broadcast -a org.pc.helper.TYPE -p org.pc.helper --es b64 "$(printf '%s' "$1" | base64 -w0)" --ei delay ${2:-350} >/dev/null 2>&1; }
ime_del() { a am broadcast -a org.pc.helper.TYPE -p org.pc.helper --es del $1 --ei delay ${2:-300} >/dev/null 2>&1; }
ime_shown() { a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)'; }

# ---------------------------------------------------------------- positions (found before the measured windows)
pos_tabs() {
  dump
  CAT=$(xy --tab "$(tablabel catalog)" '=More|=Още'); HOME_=$(xy --tab "$(tablabel home)" '=More|=Още')
  NEWS=$(xy --tab "$(tablabel news)" '=More|=Още'); MORE_=$(xy --tab "$(tablabel more)" '=Home|=Начало')
  log "tabs: catalog [$CAT] home [$HOME_] news [$NEWS] more [$MORE_]"
}
pos_catalog() {
  dump
  SEARCH=$(xy text "$L_SEARCH"); [ -n "$SEARCH" ] || SEARCH=$(xy class EditText)
  SURP=$(xy content-desc '=Изненадай ме|=Surprise me')
  VIEW=$(xy content-desc '=Полица|=Списък|=Shelf|=List')
  SORT=$(xy content-desc '=Подреждане|=Sort')
  FILT=$(xy content-desc '=Филтри|=Filters')
  log "catalog: search [$SEARCH] surprise [$SURP] view [$VIEW] sort [$SORT] filters [$FILT]; $(texts 14 | cut -c1-300)"
}
on_catalog() { ready >/dev/null; tab catalog >/dev/null; sleep 2.5; dump; }
list_top() { [ -n "$CAT" ] && a input tap $CAT; sleep 2; }  # повторно докосване на раздела — към началото
cold_launch() { a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 8; ready >/dev/null; sleep 2; }
ctrace_on() {  # композиционното проследяване (runtime-tracing) — при всеки старт и в текущия процес
  local r1 r2
  r1=$(a am broadcast -a androidx.tracing.perfetto.action.ENABLE_TRACING_COLD_START --es persistent true $PKG/androidx.tracing.perfetto.TracingReceiver 2>&1 | tr -d '\r' | tail -1 | cut -c1-200)
  r2=$(a am broadcast -a androidx.tracing.perfetto.action.ENABLE_TRACING $PKG/androidx.tracing.perfetto.TracingReceiver 2>&1 | tr -d '\r' | tail -1 | cut -c1-200)
  log "composition tracing: cold start (persistent): $r1 | now: $r2"
}

# ---------------------------------------------------------------- the part
part_cat() {
  local r w p n words secs ok
  helper_install; ime_init
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || log "device locale not changed (labels may be English)"; }
  anims 1
  tr_init
  sec "C0. First launch after install: Home → „Каталог“ (the catalogue is fetched and indexed; not representative), then speed-profile"
  launch >/dev/null; sleep 8; ready; dims; pos_tabs; gest_init
  meas first-install-open 12 tapxy $CAT
  waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms (+12 s)" || log "first-install: NO catalogue results"
  tab news >/dev/null; sleep 4; tab home >/dev/null; sleep 4
  x_compile
  launch >/dev/null; sleep 6; ready >/dev/null
  case $PART in cath*) ctrace_on;; esac

  sec "C1. Cold process: Home, then tap „Каталог“ (first composition of the tab, the view models, the index) ×3"
  for r in 1 2 3; do
    cold_launch
    mtap cold-open-$r 6 "$CAT"
    dump; log "  after cold-open-$r: $(rcount) results; $(texts 5 | cut -c1-160)"
  done
  pos_catalog

  sec "C2. Warm: leave the tab and come back ×3; to News / More and back"
  for r in 1 2 3; do mtap leave-home-$r 3 "$HOME_"; mtap warm-open-$r 4 "$CAT"; done
  mtap leave-news 4 "$NEWS"; mtap back-from-news 4 "$CAT"
  mtap leave-more 3 "$MORE_"; mtap back-from-more 4 "$CAT"

  sec "C3. Fling the list (6 up, 4 down) ×3; tab reselect → top ×2"
  for r in 1 2 3; do meas fling-list-$r 1.5 gest g-scroll; done
  for r in 1 2; do sw_up 80; sleep 0.6; sw_up 80; sleep 0.6; sw_up 80; sleep 2; mtap reselect-top-$r 3 "$CAT"; done

  sec "C4. „Покажи още“ (load more) ×2"
  for r in 1 2; do
    p=""; for n in $(seq 1 14); do dump; p=$(xy text '=Покажи още|=Show more' --maxy $((H * 92 / 100))); [ -n "$p" ] && break; sw_up 80; sleep 0.8; done
    mtap loadmore-$r 3 "$p"
  done
  list_top; sleep 1

  sec "C5. Typing with a test IME (Cyrillic, one letter every 350 ms), field chips, clear (×), erasing letter by letter"
  ime_use pc
  on_catalog; pos_catalog
  a input tap $SEARCH; sleep 2
  words="приказ любов мария"; r=0
  for w in $words; do
    r=$((r + 1)); n=$(python3 -c "import sys; print(len(sys.argv[1]))" "$w"); secs=$(python3 -c "print(2.5 + $n * 0.35)")
    meas type-$r $secs ime_type "$w" 350
    dump; log "  after type-$r '$w': $(rcount) results; text: $(grep -oE 'class="android.widget.EditText"[^>]*text="[^"]*"' $OUT/ui.xml | grep -oE 'text="[^"]*"' | head -1)"
    CLR=$(xy content-desc '=Изчисти|=Clear')
    if [ $r = 1 ]; then
      CH_A=$(xy text '=Автор|=Author' --maxy $((H / 2))); CH_T=$(xy text '=Заглавие|=Title' --maxy $((H / 2))); CH_ALL=$(xy text '=Всичко|=All' --maxy $((H / 2)))
      mtap chip-author 3 "$CH_A"; mtap chip-title 3 "$CH_T"; mtap chip-all 3 "$CH_ALL"
    fi
    if [ $r = 3 ]; then meas erase-3 $(python3 -c "print(2.5 + $n * 0.3)") ime_del $n 300
    else mtap clear-$r 3 "$CLR"; fi
  done
  dump; log "  after the typing: $(rcount) results; IME $(ime_shown)"
  ime_use default

  sec "C6. The system keyboard ($IME_DEF): show by tapping the field / hide with Back ×3, digits with it"
  back 1; on_catalog; pos_catalog
  for r in 1 2 3; do
    mtap ime-show-$r 3 "$SEARCH"
    if ime_shown | grep -q true; then meas ime-hide-$r 3 a input keyevent 4; else log "  ime-show-$r: keyboard NOT shown — no Back"; fi
  done
  mtap ime-show-4 3 "$SEARCH"
  if ime_shown | grep -q true; then
    meas type-digits 3.5 a input text 19
    dump; CLR=$(xy content-desc '=Изчисти|=Clear'); mtap clear-digits 3 "$CLR"
    ime_shown | grep -q true && meas ime-hide-4 3 a input keyevent 4
  fi
  on_catalog; pos_catalog

  sec "C7. Sort menu ×3: open, „Година (нови първо)“ / „Най-подходящи“"
  for r in 1 2 3; do
    mtap sort-open-$r 2.5 "$SORT"
    dump
    if [ $((r % 2)) = 1 ]; then p=$(xy text 'Година (нови първо)|Year (newest'); mtap sort-year-$r 3.5 "$p"
    else p=$(xy text 'Най-подходящи|Most relevant'); mtap sort-relevance-$r 3.5 "$p"; fi
    [ -n "$p" ] || back 1.5
  done
  tap content-desc '=Подреждане|=Sort' >/dev/null; sleep 1.5; tap text 'Най-подходящи|Most relevant' >/dev/null; sleep 3; list_top

  sec "C8. Filters sheet ×3: open, a filter („Само налични“ / „книга“), close with Back; then „Без филтри“"
  for r in 1 2 3; do
    mtap filters-open-$r 3 "$FILT"
    dump; log "  sheet: $(texts 12 | cut -c1-260)"
    if [ $r = 2 ]; then p=$(xy text '=книга'); mtap filters-doctype-$r 3.5 "$p"
    else p=$(xy text '=Само налични|=Only available'); mtap filters-available-$r 3.5 "$p"; fi
    meas filters-close-$r 3 a input keyevent 4
  done
  mtap filters-open-4 3 "$FILT"
  dump; p=$(xy text '=Без филтри|=Clear filters'); mtap filters-clear 3.5 "$p"
  meas filters-close-4 3 a input keyevent 4
  dump; log "  after the filters: $(rcount) results; $(texts 6 | cut -c1-160)"
  list_top

  sec "C9. Shelf view ×3: list → shelf, fling, shelf → list"
  for r in 1 2 3; do
    mtap shelf-on-$r 3.5 "$VIEW"
    meas fling-shelf-$r 1.5 gest g-scroll
    mtap shelf-off-$r 3.5 "$VIEW"
    list_top
  done

  sec "C10. „Изненадай ме“ (top-bar button) ×3: open (shuffle), „Още една“, close"
  for r in 1 2 3; do
    mtap surprise-open-$r 7 "$SURP"
    if [ $r = 1 ]; then
      dump; AGAIN=$(xy text '=Още една|=Another one'); CLOSE=$(xy content-desc '=Затвори|=Close')
      log "  surprise: again [$AGAIN] close [$CLOSE]; $(texts 10 | cut -c1-260)"
    fi
    mtap surprise-again-$r 7 "$AGAIN"
    if [ $r = 3 ] || [ -z "$CLOSE" ]; then meas surprise-close-$r 3 a input keyevent 4; else mtap surprise-close-$r 3 "$CLOSE"; fi
    dump; has "$RX_RESULTS" || { log "  (surprise not closed? $(texts 6 | cut -c1-160))"; on_catalog; }
  done

  sec "C11. Open a book from the list and come back ×3 (rows 1–3)"
  on_catalog; list_top; dump
  for r in 1 2 3; do
    p=$(python3 $H_/find.py $OUT/ui.xml --clickables $(results_top) $((H * 8 / 10)) 100 | sed -n ${r}p)
    mtap book-open-$r 4.5 "$p"
    meas book-back-$r 3.5 a input keyevent 4
    dump; has "$RX_RESULTS" || { log "  (not back on the list: $(texts 6 | cut -c1-160))"; on_catalog; }
  done

  sec "C12. Pull to refresh ×2"
  for r in 1 2; do list_top; meas refresh-$r 7 a input swipe $((W / 2)) $((H * 38 / 100)) $((W / 2)) $((H * 80 / 100)) 500; done

  sec "C13. Back to the app from the launcher (catalogue in front) ×2"
  for r in 1 2; do a input keyevent KEYCODE_HOME; sleep 3; meas resume-$r 4 launch; done

  sec "C14. All interactions (ui = as JankStats frameDurationUiNanos: vsync → end of the sync; doFrame from the trace)"
  python3 $W27 agg $OUT/fs.csv $OUT/tr.csv 2>&1 | tee $OUT/table.md | cut -c1-400 | tee -a $SUM
  log "traces kept: $TRKEPT (gzip) in tr/, $(du -sh $OUT/tr | cut -f1); FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt), ANR $(grep -c 'ANR in ' $OUT/logcat-live.txt)"
  log "composition tracing / test IME in logcat: $(grep -cE 'PerfettoSdk|androidx.tracing' $OUT/logcat-live.txt) tracing lines; $(grep -E 'PCIME' $OUT/logcat-live.txt | tail -2 | cut -c1-200 | tr '\n' ' ')"
}

# ---------------------------------------------------------------- run 3: the entry frames, A/B of the cover text
launch_w() {  # $1 — flags for the hook build (W27Trace.flags), empty — none
  a am force-stop $PKG; sleep 1
  a am start -W -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $ACT ${1:+--es w27 $1} >/dev/null 2>&1
  sleep 8; ready >/dev/null; sleep 2
}
entry_round() {  # $1 — label (round + flags), $2 — flags
  local L=$1 r p
  launch_w "$2"
  mtap cold-open-$L 6 "$CAT"
  for r in 1 2 3; do mtap leave-home-$L-$r 3 "$HOME_"; mtap warm-open-$L-$r 4 "$CAT"; done
  mtap leave-news-$L 4 "$NEWS"; mtap back-from-news-$L 4 "$CAT"
  dump
  for r in 1 2; do
    p=$(python3 $H_/find.py $OUT/ui.xml --clickables $(results_top) $((H * 8 / 10)) 100 | sed -n ${r}p)
    mtap book-open-$L-$r 4.5 "$p"
    meas book-back-$L-$r 3.5 a input keyevent 4
    dump; has "$RX_RESULTS" || { log "  (not back on the list: $(texts 6 | cut -c1-160))"; on_catalog; }
  done
  meas fling-list-$L 1.5 gest g-scroll
  list_top
  mtap sort-open-$L 2.5 "$SORT"; dump; p=$(xy text 'Година (нови първо)|Year (newest'); mtap sort-year-$L 3.5 "$p"
  tap content-desc '=Подреждане|=Sort' >/dev/null; sleep 1.5; tap text 'Най-подходящи|Most relevant' >/dev/null; sleep 3; list_top
}
part_entry() {
  local k fl
  helper_install; ime_init
  log "IME debug: enable → $(a ime enable org.pc.helper/.Ime 2>&1 | tr -d '\r' | tr '\n' ' ' | cut -c1-200); all: $(a ime list -a -s 2>&1 | tr -d '\r' | tr '\n' ' '); package: $(a dumpsys package org.pc.helper | tr -d '\r' | grep -m3 -E 'Ime|InputMethod' | tr '\n' ' ' | cut -c1-300)"
  anims 1
  tr_init
  sec "E0. First launch after install: Home → „Каталог“ (fetch + index), then speed-profile"
  launch >/dev/null; sleep 8; ready; dims; pos_tabs; gest_init
  meas first-install-open 12 tapxy $CAT
  waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms (+12 s)" || log "first-install: NO catalogue results"
  tab news >/dev/null; sleep 4; tab home >/dev/null; sleep 4
  x_compile
  launch >/dev/null; sleep 6; ready >/dev/null; tab catalog >/dev/null; sleep 3; pos_catalog; tab home >/dev/null; sleep 2
  case $PART in
    cath*) for k in 1 2; do
             sec "E$k. Round $k, the cover title as released (hyphenation + LineBreak.Heading)"; entry_round r$k-asis ""
             sec "E$k. Round $k, flag plaincover (cover title without hyphenation, LineBreak.Simple)"; entry_round r$k-plain plaincover
           done;;
    *) for k in 1 2 3; do sec "E$k. Round $k (released code)"; entry_round r$k ""; done;;
  esac
  sec "E9. All interactions (cpu = main-thread Running time in one doFrame)"
  python3 $W27 agg $OUT/fs.csv $OUT/tr.csv 2>&1 | tee $OUT/table.md | cut -c1-400 | tee -a $SUM
  python3 $W27 ab $OUT/tr.csv 2>&1 | tee -a $SUM
  log "traces kept: $TRKEPT (gzip) in tr/, $(du -sh $OUT/tr | cut -f1); FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt), ANR $(grep -c 'ANR in ' $OUT/logcat-live.txt)"
}

# ---------------------------------------------------------------- run 4: the fix ae598c45 (NEW) against ab175e49 (BASE)
# Both plain prodRelease side by side: NEW = org.chyavorec.app (app.apk), BASE = org.chyavorec.app.b (app-base.apk).
fx_pkg() { case $1 in new) echo org.chyavorec.app;; base) echo org.chyavorec.app.b;; esac; }
fx_use() { use_pkg $(fx_pkg $1); }
fx_stop() { a am force-stop org.chyavorec.app; a am force-stop org.chyavorec.app.b; }
fxm() {  # $1 — name, $2 — s, $3… — action: framestats + trace, analysed per entry (w27.py entry)
  local name=$1 secs=$2 rc mb; shift 2
  sleep 1.5
  tr_start
  a dumpsys gfxinfo $PKG reset >/dev/null 2>&1; sleep 0.3
  a dumpsys gfxinfo $PKG framestats > $OUT/fs27/before.txt 2>/dev/null
  a log -t PERFCHECK "'w27 $name'"
  "$@"
  sleep $secs
  a dumpsys gfxinfo $PKG framestats > $OUT/fs27/$name.txt 2>/dev/null
  tr_stop $name
  python3 $W27 fs $OUT/fs27/before.txt $OUT/fs27/$name.txt $name $OUT/fs.csv $OUT/fs27/$name.json 2>&1 | head -1 | cut -c1-260 >> $OUT/fs-lines.txt
  rm -f $OUT/fs27/$name.txt
  local f=$OUT/tr/$name.pftrace
  [ -s $f ] || { log "ENTRY $name: no trace"; return 0; }
  timeout 300 python3 $W27 entry $f $PKG $name $OUT/entry.csv 2>&1 | cut -c1-400 | tee -a $SUM; rc=${PIPESTATUS[0]}
  mb=$(( $(du -sm $OUT/tr | cut -f1) ))
  if [ "$rc" = 3 ] && [ $mb -lt $TRKEEP_MB ]; then gzip -9 -f $f; TRKEPT=$((TRKEPT + 1)); else rm -f $f; fi
}
fxt() { if [ -z "$3" ]; then log "SKIP $1: no position"; return 1; fi; fxm $1 $2 tapxy $3; }
first_row_title() { dump; python3 - "$OUT/ui.xml" "$(results_top)" <<'EOF'
import re, sys, xml.etree.ElementTree as ET
root = ET.parse(sys.argv[1]).getroot(); top = int(sys.argv[2])
best = None
for n in root.iter('node'):
    t = n.get('text') or ''
    m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
    if not t or not m: continue
    x0, y0 = int(m.group(1)), int(m.group(2))
    # the row title: right of the cover (x > 150 px), below the results header
    if y0 > top and x0 > 150 and len(t) > 2 and not re.search(r'налична|резултат|·', t):
        if best is None or y0 < best[0]: best = (y0, t)
print(best[1][:60] if best else '')
EOF
}
fx_visual() {  # $1 — base | new: rows after a tab switch, scroll position kept, Back from a book (recorded)
  local K=$1 t1 t2 p
  sec "V-$K. Visual checks ($K): rows after a tab switch, the scroll position after Home → Каталог, Back from a book (screen recording)"
  fx_stop; sleep 1; launch >/dev/null; sleep 8; ready >/dev/null; sleep 1
  a input tap $CAT; sleep 0.35; shot fx/$K-tab-0.35s >/dev/null; sleep 0.5; shot fx/$K-tab-0.85s >/dev/null; sleep 2
  dump; log "VIS $K after the tab switch: $(rcount) results; rows: $(first_row_title)"; shot fx/$K-tab-3s >/dev/null
  a input tap $HOME_; sleep 2; a input tap $CAT; sleep 0.25; shot fx/$K-warm-0.25s >/dev/null; sleep 0.4; shot fx/$K-warm-0.65s >/dev/null; sleep 2
  dump; log "VIS $K warm switch: $(rcount) results; first row: $(first_row_title)"; shot fx/$K-warm-3s >/dev/null
  sw_up 400; sleep 1; sw_up 400; sleep 2; t1=$(first_row_title); shot fx/$K-scrolled >/dev/null
  a input tap $HOME_; sleep 3; a input tap $CAT; sleep 3; t2=$(first_row_title); shot fx/$K-scrolled-back >/dev/null
  log "VIS $K scroll position: before Home '$t1', after Home → Каталог '$t2' → $([ -n "$t1" ] && [ "$t1" = "$t2" ] && echo KEPT || echo 'CHANGED/unknown')"
  a input tap $NEWS; sleep 3; a input tap $CAT; sleep 3; t2=$(first_row_title)
  log "VIS $K News → Каталог: first row '$t2' ($([ "$t1" = "$t2" ] && echo kept || echo changed)); $(rcount) results"; shot fx/$K-back-from-news >/dev/null
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $(results_top) $((H * 8 / 10)) 100 | sed -n 1p)
  if [ -n "$p" ]; then
    a input tap $p; sleep 4; shot fx/$K-book >/dev/null
    REC_FPS=20 rec_cmd fxback-$K emu 3 a input keyevent 4
    sleep 1; dump; log "VIS $K Back from a book: on the list $(has "$RX_RESULTS" && echo yes || echo NO); first row '$(first_row_title)'"
    shot fx/$K-after-book >/dev/null
  else log "VIS $K: no row to open"; fi
}
fx_round() {  # $1 — base | new, $2 — round
  local K=$1 r=$2 i p
  fx_use $K; fx_stop; sleep 1
  launch >/dev/null; sleep 8; ready >/dev/null; sleep 2
  fxt cold-open-$K-r$r 6 "$CAT"
  for i in 1 2; do fxt leave-home-$K-r$r-$i 3 "$HOME_"; fxt warm-open-$K-r$r-$i 4 "$CAT"; done
  fxt leave-news-$K-r$r 4 "$NEWS"; fxt from-news-$K-r$r 4 "$CAT"   # Back from a tab goes to Home (start destination): News → tap Каталог
  dump; has "$RX_RESULTS" || { log "  (not on the catalogue after News → Каталог: $(texts 6 | cut -c1-160))"; on_catalog; }
  for i in 1 2; do
    dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $(results_top) $((H * 8 / 10)) 100 | sed -n ${i}p)
    [ -n "$p" ] || { log "  no row $i"; continue; }
    a input tap $p; sleep 4.5
    fxm book-back-$K-r$r-$i 3.5 a input keyevent 4
    dump; has "$RX_RESULTS" || { log "  (not back on the list: $(texts 6 | cut -c1-160))"; on_catalog; }
  done
}
part_fix() {
  local K r
  TR_APPS="org.chyavorec.app org.chyavorec.app.b"
  anims 1
  tr_init; MAPF=""
  sec "X0. NEW (ae598c45, org.chyavorec.app) and BASE (ab175e49, org.chyavorec.app.b) side by side; first launch to the catalogue, speed-profile"
  [ -f app-base.apk ] && timeout 240 adb install -r app-base.apk 2>&1 | tail -1
  for K in new base; do
    fx_use $K
    a cmd locale set-app-locales $PKG --locales bg-BG >/dev/null 2>&1; a pm grant $PKG android.permission.POST_NOTIFICATIONS >/dev/null 2>&1
    log "X $K: $(ver)"
    launch >/dev/null; sleep 8; ready >/dev/null; dims
    tab catalog >/dev/null; waitfor "$RX_RESULTS" 150 && log "X $K: catalogue after ${WAITED} ms" || log "X $K: NO catalogue results"
    tab news >/dev/null; sleep 4; tab home >/dev/null; sleep 3
    x_compile
  done
  fx_use new; launch >/dev/null; sleep 6; ready >/dev/null; pos_tabs; gest_init
  for r in 1 2 3 4 5; do
    if [ $((r % 2)) = 1 ]; then K="base new"; else K="new base"; fi
    sec "X$r. Round $r ($K): cold first entry, 2 × warm tab switch Home → Каталог, News → Каталог, 2 × Back from a book"
    for k in $K; do fx_round $k $r; done
  done
  sec "X8. Visual checks"
  for K in new base; do fx_use $K; fx_visual $K; done
  sec "X9. BASE (ab175e49) vs NEW (ae598c45): main-thread CPU (Running) per entry"
  python3 $W27 fxagg $OUT/entry.csv 2>&1 | tee $OUT/table.md | tee -a $SUM
  for K in new base; do log "CRASH $K ($(fx_pkg $K)): FATAL $(grep -A3 'FATAL EXCEPTION' $OUT/logcat-live.txt | grep -c "Process: $(fx_pkg $K),"), ANR $(grep -c "ANR in $(fx_pkg $K) " $OUT/logcat-live.txt)"; done
  log "all FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt); traces kept: $TRKEPT"
  all_sheets >/dev/null 2>&1 || true
  fx_use new
}

part_w27() {
  case "$W27_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in fx*) part_fix;; *) part_entry;; esac
  return 0
}
