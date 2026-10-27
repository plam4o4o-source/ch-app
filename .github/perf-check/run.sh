#!/usr/bin/env bash
# Пълна проверка на оптимизирания release на емулатор (ВРЕМЕННО, клон debug/full-test-2, v1.2.26).
#   run.sh start35 — старт и време до резултатите в каталога (API 35, за сравнение с базата)
#   run.sh perf    — старт, време до резултатите, jank при 540x1200 (каталог, Начало, Новини,
#                    Календар, „Искам да прочета“)
#   run.sh stress  — плъзгания по „Начало“ (стабилност на образа), памет при 30 цикъла,
#                    завъртане, смърт на процеса (заявка + сегмент), monkey 4000
#   run.sh func    — новото: търсене „12“, „Искам да прочета“ (+ отмяна), Календар и абонамент,
#                    „Размер на текста“, скенер на ръка (без камера), „Изчисти временните данни“
#   run.sh look    — матрица от снимки: светла EN, тъмна, BG, шрифт 1.3, хоризонтално
#   run.sh offline — студен старт без връзка с кеш, възстановяване (и „Контакти“)
set -u
PART=${1:-perf}
PKG=org.chyavorec.app
ACT=$PKG/.MainActivity
SITE=https://chyavorec.org
INV=399560
BOOK_URL="$SITE/elektronen-katalog?inv=$INV"
EVENTS_URL="$SITE/events"
H_=.github/perf-check
OUT=out/$PART; mkdir -p $OUT/shots
SUM=$OUT/summary.txt; : > $SUM
RX_RESULTS='[0-9]+ (results?|резултата?)'
RX_BACK='content-desc="(Back|Назад)"'
L_SEARCH='Search everything|Търси навсякъде'
L_CAL='=Calendar|=Календар'
L_NEWSSEG='=News|=Новини'
L_SUBSCRIBE='Subscribe in calendar|Абонирай се за календара'
L_WANT='=Want to read|=Искам да прочета'
L_NOTIFY='Notify me when available|Кажи ми, когато е на рафта'

# ---------------------------------------------------------------- помощни
a() { timeout ${T:-40} adb shell "$@"; }
log() { echo "$*" | tee -a $SUM; }
sec() { echo; echo "################ $* ################"; echo "## $*" >> $SUM; alive "$*"; a log -t PERFCHECK "'section: $*'"; }
now_ms() { echo $(( $(date +%s%N) / 1000000 )); }
# Жив ли е емулаторът: не само `get-state` (при „увисване“ казва device), а и команда в него.
alive() {
  timeout 20 adb shell true >/dev/null 2>&1 && return 0
  sleep 10; timeout 30 adb shell true >/dev/null 2>&1 && return 0
  log "!!! EMULATOR GONE / HUNG (at: $1)"; finish; echo "RESULT: emulator lost"; exit 1
}
dump() {
  rm -f $OUT/ui.xml; a rm -f /sdcard/ui.xml
  T=30 a uiautomator dump /sdcard/ui.xml >/dev/null 2>&1
  timeout 15 adb pull /sdcard/ui.xml $OUT/ui.xml >/dev/null 2>&1
  [ -s $OUT/ui.xml ]
}
texts() { grep -o 'text="[^"]*"\|content-desc="[^"]*"' $OUT/ui.xml 2>/dev/null | grep -v '=""' | head -${1:-30} | tr '\n' ' '; }
show() { if dump; then echo "--- screen: $(texts ${1:-30})"; else echo "(ui dump failed)"; fi; }
xy() { python3 $H_/find.py $OUT/ui.xml "$@"; }
has() { grep -qE "$1" $OUT/ui.xml 2>/dev/null; }
tap() { dump || return 1; local p; p=$(xy "$@"); [ -n "$p" ] || { echo "not found: $*"; return 1; }; echo ">>> tap $* @ $p"; a input tap $p; }
back() { a input keyevent KEYCODE_BACK; sleep ${1:-2}; }
shot() {
  mkdir -p "$(dirname "$OUT/shots/$1")"
  a screencap -p /sdcard/s.png && timeout 30 adb pull /sdcard/s.png "$OUT/shots/$1.png" >/dev/null 2>&1 && echo "[shot] $PART/$1.png"
}
dims() {
  local s r t; s=$(a wm size | tail -1 | grep -oE '[0-9]+x[0-9]+'); W=${s%x*}; H=${s#*x}
  r=$(a settings get system user_rotation | tr -d '\r')
  if [ "$r" = 1 ] || [ "$r" = 3 ]; then t=$W; W=$H; H=$t; fi
}
sw_up() { a input swipe $((W/2)) $((H*3/4)) $((W/2)) $((H/4)) ${1:-80}; }
sw_down() { a input swipe $((W/2)) $((H/4)) $((W/2)) $((H*3/4)) ${1:-80}; }
tablabel() {
  case $1 in
    home) echo '=Home|=Начало';; catalog) echo '=Catalogue|=Каталог';; my) echo '=My|=Моето';;
    news) echo '=News|=Новини';; more) echo '=More|=Още';;
  esac
}
# Етикетът в лентата с разделите (не сегментът „Новини“ горе): на един ред/в една колона с „Още“/„Начало“.
tab() {
  local p ref='=More|=Още'; [ $1 = more ] && ref='=Home|=Начало'
  dump || return 1; p=$(xy --tab "$(tablabel $1)" "$ref")
  [ -n "$p" ] || { echo "tab not found: $1"; debug_ui tab-$1; return 1; }; echo ">>> tab $1 @ $p"; a input tap $p
}
link() { echo ">>> link $1"; a am start -W -a android.intent.action.VIEW -d "\"$1\"" -n $ACT 2>&1 | grep -E "Status|LaunchState|TotalTime|Error" | tr '\n' ' '; echo; }
launch() { a am start -W -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $ACT 2>&1 | grep -E "Status|LaunchState|TotalTime|WaitTime|Warning|Error"; }
# Диалози: системен ANR („Wait“), ново издание („По-късно“), известията („Не сега“), въведението („Пропусни“).
dismiss() {
  local i p l
  for i in 1 2 3; do
    dump || return 0; p=""
    if has "isn.t responding|не отговаря"; then p=$(xy text '=Wait|=Изчакване'); [ -n "$p" ] && { echo ">>> system ANR dialog: $(texts 3) → Wait"; log "(system ANR dialog: $(texts 2) → Wait)"; a input tap $p; sleep 3; continue; }; fi
    for l in '=Later' '=По-късно' '=Not now' '=Не сега' '=Skip' '=Пропусни'; do
      p=$(xy text "$l"); [ -n "$p" ] && { echo ">>> dismiss '$l'"; a input tap $p; sleep 2; break; }
    done
    [ -n "$p" ] || return 0
  done
}
waitfor() {  # regex, лимит (s) → WAITED (ms) или -1
  local rx="$1" lim=$2 t0; t0=$(now_ms)
  while :; do
    if dump && has "$rx"; then WAITED=$(( $(now_ms) - t0 )); return 0; fi
    [ $(( $(now_ms) - t0 )) -ge $((lim * 1000)) ] && { WAITED=-1; return 1; }
    sleep 0.3
  done
}
NDBG=0
debug_ui() { NDBG=$((NDBG+1)); [ $NDBG -gt 20 ] && return 0; mkdir -p $OUT/debug; cp $OUT/ui.xml $OUT/debug/$NDBG-$1.xml 2>/dev/null; shot debug/$NDBG-$1; }
restart() { a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 4; ready; }
# Главен екран с лентата на разделите: затваря диалози, връща се назад, пуска приложението отново.
ready() {
  local i
  for i in 1 2 3 4 5 6; do
    dismiss
    # в хоризонтално положение „Още“ може да липсва в страничната лента — тогава стига „Моето“
    dump && has 'text="(Home|Начало)"' && has 'text="(More|Още|My|Моето)"' && return 0
    if ! has "package=\"$PKG\""; then echo "(app not in front: $(texts 6)) → relaunch"; launch >/dev/null; sleep 5; continue; fi
    if has "$RX_BACK"; then echo "(not on a tab screen: $(texts 6)) → back"; back 2
    else echo "(app in front, tab bar not found: $(texts 20))"; debug_ui ready; sleep 3; fi
  done
  log "WARN: tab bar not reached"; return 1
}
# Само ESC (не „Назад“: в първия кръг „Назад“ напускаше каталога). Клавиатурата може да остане —
# резултатите горе пак се докосват.
hide_ime() { a input keyevent 111; sleep 1; }
# y на първия ред под заглавието „N резултата · …“ (за да не се докосват филтрите над него)
results_top() { local p; p=$(xy text ' results|=1 result| резултат'); [ -n "$p" ] && echo $(( ${p#* } + 30 )) || echo $((H/4)); }
rcount() { grep -oE 'text="[0-9]+ (results?|резултата?)' $OUT/ui.xml 2>/dev/null | grep -oE '[0-9]+' | head -1; }
search_catalog() {  # $1 — заявка; каталогът трябва да е отворен
  tap text "$L_SEARCH" || tap class EditText || return 1
  sleep 1; a input text "$1"; sleep 3; hide_ime; sleep 1; dump
}
# Сегментите в „Новини и събития“ (горе, не етикетът в долната лента).
segment() {  # news | calendar
  local l=$L_NEWSSEG; [ $1 = calendar ] && l=$L_CAL
  dims; tap text "$l" --maxy $((H/2)) || { debug_ui seg-$1; return 1; }; sleep 4
}
segstate() { dump; echo "News[$(xy --state text "$L_NEWSSEG" --maxy $((H/2)))] Calendar[$(xy --state text "$L_CAL" --maxy $((H/2)))] subscribe-action=$(has "$L_SUBSCRIBE" && echo visible || echo absent)"; }
gfx_reset() { a dumpsys gfxinfo $PKG reset >/dev/null; }
gfx_save() { a dumpsys gfxinfo $PKG > $OUT/gfx-$1.txt; python3 $H_/parse.py gfx $OUT/gfx-$1.txt "$1" | tee -a $SUM; }
mem() {
  a dumpsys meminfo $PKG > $OUT/mem-$1-raw.txt
  python3 $H_/parse.py mem $OUT/mem-$1-raw.txt "$1 (raw)" | tee -a $SUM
  a am send-trim-memory $PKG RUNNING_CRITICAL >/dev/null 2>&1; sleep 8
  a dumpsys meminfo $PKG > $OUT/mem-$1.txt
  python3 $H_/parse.py mem $OUT/mem-$1.txt "$1 (trimmed)" | tee -a $SUM
}
anims() { for k in window_animation_scale transition_animation_scale animator_duration_scale; do a settings put global $k $1; done; }
no_camera() {
  a pm revoke $PKG android.permission.CAMERA 2>/dev/null
  a pm set-permission-flags $PKG android.permission.CAMERA user-fixed 2>/dev/null
  a appops set $PKG CAMERA deny 2>/dev/null
  CAMSTATE=$(a dumpsys package $PKG | grep -m1 'android.permission.CAMERA: granted' | tr -s ' ' | tr -d '\r')
  echo "camera: $CAMSTATE"
}
offline() { a cmd connectivity airplane-mode enable; a svc wifi disable; a svc data disable; sleep 4; log "offline: airplane=$(a settings get global airplane_mode_on | tr -d '\r')"; }
online() { a cmd connectivity airplane-mode disable; a svc wifi enable; a svc data enable; }
rot() {  # 0 | 1 | 3: `wm user-rotation lock` (Android 11+) и настройката user_rotation
  a settings put system user_rotation $1
  a wm user-rotation lock $1 >/dev/null 2>&1
  sleep 3; dims
}
open_settings() {
  local i p=""
  ready; tab more; sleep 3; dims
  for i in 1 2 3 4 5; do dump; p=$(xy text '=Settings|=Настройки'); [ -n "$p" ] && break; sw_up 300; sleep 1; done
  [ -n "$p" ] || { log "WARN: Settings not found"; debug_ui settings; return 1; }
  a input tap $p; sleep 3
}
settings_item() {  # етикет: превърта настройките, докато го намери, и го докосва
  local i
  # Настройките помнят превъртането — първо най-горе, после надолу.
  for i in 1 2 3 4; do sw_down 200; sleep 0.5; done
  for i in 1 2 3 4 5 6 7 8; do dump; [ -n "$(xy text "$1")" ] && { tap text "$1"; return 0; }; sw_up 300; sleep 1; done
  log "WARN: settings item $1 not found"; debug_ui settings-item; return 1
}
open_want_list() {
  local i
  ready; tab my; sleep 3; dims
  for i in 1 2 3 4; do dump; [ -n "$(xy text 'Want to read|Искам да прочета')" ] && { tap text 'Want to read|Искам да прочета'; sleep 3; return 0; }; sw_up 300; sleep 1; done
  log "WARN: 'Want to read' tile not found in My"; debug_ui want-tile; return 1
}
finish() {
  [ -n "${LOGPID:-}" ] && kill $LOGPID 2>/dev/null
  timeout 60 adb logcat -d -v threadtime > $OUT/logcat-dump.txt 2>/dev/null
  local L=$OUT/logcat-all.txt
  cat $OUT/logcat-live.txt $OUT/logcat-dump.txt 2>/dev/null | awk '!seen[$0]++' > $L
  python3 $H_/parse.py logcat $L $PKG > $OUT/logcat-findings.txt 2>&1
  echo "===== logcat findings ====="; cat $OUT/logcat-findings.txt | cut -c1-260
  { echo "## logcat findings"; grep -E '^== |^app pids|events=' $OUT/logcat-findings.txt; } >> $SUM
  rm -f $OUT/ui.xml
  echo "===== SUMMARY ($PART) ====="; cat $SUM
}
setup() {
  log "part $PART; APK: $(cat apk-ref.txt 2>/dev/null) size $(stat -c %s app.apk) bytes"
  log "host: $(nproc) cpu, $(free -m | awk 'NR==2{print $2}') MB RAM"
  T=180 a getprop ro.build.version.release >/dev/null
  SDK=$(a getprop ro.build.version.sdk | tr -d '\r')
  log "android $(a getprop ro.build.version.release | tr -d '\r') sdk $SDK; $(a wm size | tr -d '\r' | tail -1); $(a wm density | tr -d '\r' | tail -1)"
  timeout 180 adb install -r app.apk || { log "RESULT: install failed"; exit 1; }
  log "installed: $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  a pm grant $PKG android.permission.POST_NOTIFICATIONS || true
  no_camera; log "camera permission: $CAMSTATE"
  a settings put system accelerometer_rotation 0; rot 0
  a settings put system font_scale 1.0; a cmd uimode night no
  adb logcat -G 16M >/dev/null 2>&1; adb logcat -c
  adb logcat -v threadtime > $OUT/logcat-live.txt 2>&1 & LOGPID=$!
  dims; log "screen ${W}x${H}"
  # системата още се успокоява след зареждането (Pixel Launcher понякога дава ANR диалог)
  sleep 25; a input keyevent KEYCODE_HOME; sleep 2; dismiss
}

# ================================================================= СТАРТ
startup() {
  sec "1. First install: first launch, time to catalogue results"
  launch | tee $OUT/start-first.txt; sleep 5; show; shot 00-first-launch
  dismiss; sleep 2; ready; show; shot 01-home-first
  log "home quick actions: Scan=$(has 'text="(Scan|Сканирай)"' && echo yes || echo no) MyBooks=$(has 'text="(My books|Моите книги)"' && echo yes || echo no) Contacts=$(has 'text="(Contacts|Контакти)"' && echo yes || echo no); 'Catalogue' labels on screen: $(grep -oE 'text="(Catalogue|Каталог)"' $OUT/ui.xml | wc -l) (1 = only the tab)"
  tab catalog
  if waitfor "$RX_RESULTS" 150; then log "FIRST-INSTALL catalogue results after ${WAITED} ms (tap→text, uiautomator polling)"; else log "FIRST-INSTALL: NO catalogue results in 150 s"; fi
  show 12; shot 02-catalog-first
  log "catalogue hint: $(has 'text="Search everything"' && echo "'Search everything' OK" || echo "'Search everything' NOT found: $(texts 3)")"
  waitfor "$RX_RESULTS" 10; log "polling floor (text already on screen): ${WAITED} ms — subtract from the times below"
  for i in 1 2 3; do
    a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 3; dismiss
    tab catalog
    if waitfor "$RX_RESULTS" 60; then log "INDEX-READY #$i catalogue results after ${WAITED} ms (cold start → tap Catalogue → text)"; else log "INDEX-READY #$i: NO results in 60 s"; fi
  done
  for i in 1 2; do
    a am force-stop $PKG; sleep 2; t0=$(now_ms); link "$SITE/elektronen-katalog"
    if waitfor "$RX_RESULTS" 60; then log "INDEX-READY deep-link #$i results $(( $(now_ms) - t0 )) ms after am start"; fi
  done

  sec "2. Startup"
  : > $OUT/start-cold.txt
  for i in $(seq 1 10); do
    a am force-stop $PKG; sleep 3
    launch | tee -a $OUT/start-cold.txt | tr '\n' ' '; echo
    sleep 3
  done
  python3 $H_/parse.py starts $OUT/start-cold.txt "cold(as installed)" | tee -a $SUM
  : > $OUT/start-warm.txt
  for i in $(seq 1 5); do
    a input keyevent KEYCODE_HOME; sleep 3
    launch | tee -a $OUT/start-warm.txt | tr '\n' ' '; echo
    sleep 2
  done
  python3 $H_/parse.py starts $OUT/start-warm.txt "warm(HOME→relaunch)" | tee -a $SUM
  log "compile: $(T=300 a cmd package compile -m speed-profile -f $PKG | tr -d '\r' | tr '\n' ' ')"
  : > $OUT/start-cold-sp.txt
  for i in $(seq 1 5); do
    a am force-stop $PKG; sleep 3
    launch | tee -a $OUT/start-cold-sp.txt | tr '\n' ' '; echo
    sleep 3
  done
  python3 $H_/parse.py starts $OUT/start-cold-sp.txt "cold(speed-profile)" | tee -a $SUM
  dismiss
}

part_start35() { startup; }

# ================================================================= PERF
# „Искам да прочета“ с няколко книги (от търсенето „12“) — за jank на списъка.
fill_want() {
  local n=0 k p pts title
  restart; tab catalog; waitfor "$RX_RESULTS" 60; search_catalog 12; dims
  for k in 1 2 3 4 5 6; do
    pts=$(xy --clickables $(results_top) $((H*4/5)) 120)
    while read -r px py; do
      [ -n "$px" ] || continue
      a input tap $px $py; sleep 3; dump
      # чипът може да е под гънката (дълго заглавие) — едно плъзгане
      has "$RX_BACK" && [ -z "$(xy text "$L_WANT")" ] && { a input swipe $((W/2)) $((H*2/3)) $((W/2)) $((H/3)) 300; sleep 1.5; dump; }
      if has "$RX_BACK" && [ -n "$(xy text "$L_WANT")" ]; then
        if xy --state text "$L_WANT" | grep -q 'checked=false'; then tap text "$L_WANT" >/dev/null; n=$((n+1)); sleep 1.5; fi
        back 2
      else
        echo "(fill_want: tap @ $px $py → $(texts 4 | cut -c1-120))"
        has "$RX_BACK" && back 2
      fi
      [ $n -ge 12 ] && break 2
    done <<< "$pts"
    sw_up 400; sleep 2; dump
  done
  log "Want to read: added $n books for the list jank"
}
# Само jank на списъка „Искам да прочета“ (540x1200), след като в него има повече книги.
part_wantjank() {
  warmup
  fill_want
  sec "3w. Jank of the Want to read list at 540x1200 (animations ON)"
  anims 1; a wm size 540x1200; a wm density 210; sleep 3; dims
  local i
  open_want_list; dump; log "want list: $(grep -c 'content-desc="More actions"' $OUT/ui.xml) rows visible; $(show 6 | cut -c1-200)"
  gfx_reset
  for i in $(seq 1 10); do sw_up 70; sleep 0.25; done
  for i in $(seq 1 10); do sw_down 70; sleep 0.25; done
  gfx_save want-lowres; shot jank/want-lowres
  a wm size reset; a wm density reset; anims 0
}
part_perf() {
  startup
  fill_want
  sec "3. Jank at 540x1200 (animations ON)"
  anims 1
  a wm size 540x1200; a wm density 210; sleep 3; dims; log "jank screen ${W}x${H}"
  local i p p2 pages=0 mis=0 scr
  restart; link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; sleep 2
  gfx_reset
  for i in $(seq 1 30); do
    sw_up 60; sleep 0.25
    if [ $((i % 5)) = 0 ] && dump; then
      p=$(xy text '=Show more|=Покажи още')
      if [ -n "$p" ]; then
        sleep 1; dump; p2=$(xy text '=Show more|=Покажи още')
        if [ -n "$p2" ] && [ "$p" = "$p2" ]; then
          a input tap $p2; pages=$((pages+1)); sleep 1.5
          dump && has "$RX_BACK" && { mis=$((mis+1)); back 1.5; }
        fi
      fi
    fi
  done
  gfx_save catalog-lowres; log "catalog-lowres: 30 swipes, 'Show more' pages: $pages, mis-taps recovered: $mis; screen: $(show 6 | cut -c1-200)"
  shot jank/catalog-lowres-end
  # „Начало“ последно: там емулаторът най-често пада при плъзгане.
  for scr in want news calendar home; do
    case $scr in
      home) link "$SITE/"; sleep 5;;
      news) link "$SITE/news"; sleep 5; segstate | grep -q 'News\[checked=true' || segment news;;
      calendar) link "$EVENTS_URL"; sleep 5; dump && has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък'; sleep 2; };;
      want) open_want_list; dump; log "want list: $(grep -c 'content-desc="More actions"' $OUT/ui.xml) rows visible";;
    esac
    dismiss
    log "jank $scr-lowres screen: $(show 8 | cut -c1-220)"
    gfx_reset
    for i in $(seq 1 10); do sw_up 70; sleep 0.25; done
    for i in $(seq 1 10); do sw_down 70; sleep 0.25; done
    gfx_save $scr-lowres
    shot jank/$scr-lowres
  done
  a wm size reset; a wm density reset; sleep 3; dims
  anims 0
  mem perf-end
}

# ================================================================= STRESS
warmup() {
  sec "0. First launch and warm-up"
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  tab news; sleep 5; tab home; sleep 5
}
s_swipes() {
  sec "S1. Home swipes (API stability probe: 20 swipes)"
  dims; restart; link "$SITE/"; sleep 8
  local i ok=0
  for i in $(seq 1 20); do
    if [ $((i % 2)) = 1 ]; then sw_up 300; else sw_down 300; fi
    sleep 1.5
    timeout 20 adb shell true >/dev/null 2>&1 || { log "HOME SWIPE #$i: emulator not responding"; alive "home swipe $i"; }
    ok=$i
  done
  log "HOME SWIPES SURVIVED: $ok/20"; shot swipe-home-end
}
s_cycles() {
  sec "S2. Memory over 30 tab/book cycles"
  restart; tab home; sleep 3; dump
  declare -A TXY
  for t in home catalog my news more; do
    ref='=More|=Още'; [ $t = more ] && ref='=Home|=Начало'
    TXY[$t]=$(xy --tab "$(tablabel $t)" "$ref"); log "tab $t @ ${TXY[$t]}"
  done
  mem c00
  for c in $(seq 1 30); do
    for t in home catalog my news more; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 0.4; done
    link "$BOOK_URL" >/dev/null; sleep 1.2
    [ $c = 1 ] && { show 12; has "$RX_BACK" && log "cycle 1: book screen opened" || log "cycle 1: book screen NOT detected"; }
    back 0.6
    case $c in 10|20|30) mem c$c;; esac
  done
  log "pid after cycles: $(a pidof $PKG | tr -d '\r')"
}
s_rotation() {
  sec "S3. Rotation stress (10 toggles each)"
  rotx() {
    local name=$1 pid0 pid1 i
    pid0=$(a pidof $PKG | tr -d '\r'); shot rotation/$name-0-before
    for i in $(seq 1 10); do
      a settings put system user_rotation $((i % 2)); a wm user-rotation lock $((i % 2)) >/dev/null 2>&1; sleep 1.5
      [ $i = 9 ] && shot rotation/$name-1-landscape
    done
    rot 0; sleep 1
    pid1=$(a pidof $PKG | tr -d '\r'); shot rotation/$name-2-after; show 15
    log "ROTATION $name: pid $pid0 -> $pid1 ($([ "$pid0" = "$pid1" ] && echo same || echo CHANGED)) $(has "$RX_BACK" && echo '[back button visible]')"
  }
  restart; gfx_reset
  tab home; sleep 2; tab home; sleep 2; rotx home
  tab catalog; waitfor "$RX_RESULTS" 30; search_catalog 12; log "catalogue '12' before rotation: $(rcount) results"; rotx catalog
  if dump && grep -q 'text="12"' $OUT/ui.xml; then log "ROTATION catalog: query '12' kept ($(rcount) results)"; else log "ROTATION catalog: query '12' NOT found after rotation"; fi
  link "$BOOK_URL"; sleep 3; sw_up 300; sleep 1; rotx book
  gfx_save rotation
  back
}
s_procdeath() {
  sec "S4. Process death and restore (catalogue query + Calendar segment)"
  restart; tab catalog; waitfor "$RX_RESULTS" 30; search_catalog 12
  log "before: catalogue query '12': $(grep -q 'text="12"' $OUT/ui.xml && echo shown || echo missing), $(rcount) results"; shot procdeath/1-catalog-before
  tab news; sleep 4; segment calendar; log "before: $(segstate)"; shot procdeath/2-calendar-before
  a input keyevent KEYCODE_HOME; sleep 3
  a am kill $PKG; sleep 3
  pid=$(a pidof $PKG | tr -d '\r'); [ -n "$pid" ] && { echo "still alive ($pid), retry kill"; sleep 5; a am kill $PKG; sleep 3; pid=$(a pidof $PKG | tr -d '\r'); }
  log "PROCDEATH: after am kill pid='${pid}' ($([ -z "$pid" ] && echo killed || echo 'NOT killed'))"
  launch | tee $OUT/start-procdeath.txt | tr '\n' ' '; echo; sleep 5
  show 15; shot procdeath/3-after-launcher
  log "PROCDEATH launcher: $(grep -E 'LaunchState' $OUT/start-procdeath.txt | tr -d '\r'); title News&events: $(has 'text="(News &amp; events|Новини и събития)"' && echo yes || echo no); $(segstate)"
  tab catalog; sleep 4; dump; shot procdeath/4-catalog-after
  log "PROCDEATH catalogue query kept: $(grep -q 'text="12"' $OUT/ui.xml && echo "yes ($(rcount) results)" || echo "no: $(texts 4)")"
  # Вариант 2: от екрана „Последни“
  tab news; sleep 3; a input keyevent KEYCODE_HOME; sleep 3; a am kill $PKG; sleep 3
  log "PROCDEATH#2 pid after kill='$(a pidof $PKG | tr -d '\r')'"
  a input keyevent KEYCODE_APP_SWITCH; sleep 4; shot procdeath/5-recents
  a input tap $((W/2)) $((H/2)); sleep 6; show 12; shot procdeath/6-from-recents
  log "PROCDEATH from Recents: $(segstate)"
  tab catalog; sleep 3; dump; log "PROCDEATH from Recents: catalogue query $(grep -q 'text="12"' $OUT/ui.xml && echo kept || echo 'not visible')"
}
s_monkey() {
  sec "S5. Monkey 4000 events (seed 1234, CAMERA revoked)"
  no_camera; log "camera permission before monkey: $CAMSTATE"
  log "statusbar disable: $(a cmd statusbar send-disable-flag statusbar-expansion notification-alerts 2>&1 | tr -d '\r' | head -2)"
  restart
  log "monkey start pid=$(a pidof $PKG | tr -d '\r')"
  a log -t PERFCHECK "monkey start"
  t0=$(now_ms)
  timeout 1200 adb shell monkey -p $PKG --pct-syskeys 0 --pct-anyevent 0 --throttle 80 -s 1234 -v 4000 > $OUT/monkey.txt 2>&1
  log "MONKEY exit=$? duration=$(( ($(now_ms) - t0) / 1000 )) s; $(grep -E 'Events injected|Monkey finished|aborted' $OUT/monkey.txt | tr '\n' ' ')"
  log "MONKEY markers: CRASH=$(grep -c 'CRASH' $OUT/monkey.txt) ANR=$(grep -c 'ANR' $OUT/monkey.txt) FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/monkey.txt)"
  grep -nE -A20 'CRASH|NOT RESPONDING|ANR' $OUT/monkey.txt | head -60
  alive "after monkey"
  sleep 2; log "pid after monkey: '$(a pidof $PKG | tr -d '\r')'; airplane=$(a settings get global airplane_mode_on | tr -d '\r'); camera: $(a dumpsys package $PKG | grep -m1 'android.permission.CAMERA: granted' | tr -s ' ' | tr -d '\r')"; shot monkey-end; show 12
  mem after-monkey
}

# ================================================================= FUNC
f_want() {
  local p title n i
  sec "F1. Catalogue search '12'"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; search_catalog 12; shot func/01-catalog-12
  n=$(rcount); log "SEARCH '12': ${n:-?} results; header: $(grep -oE 'text="[0-9]+ (results?|резултата?)[^"]*"' $OUT/ui.xml | head -1)"
  p=$(xy --first-clickable $(results_top) $((H*3/4)) 120)
  echo ">>> open first result @ $p"; a input tap $p; sleep 4; show 25; shot func/02-book
  python3 $H_/find.py $OUT/ui.xml --texts > $OUT/book-texts.txt
  log "BOOK opened: $(has "$RX_BACK" && echo yes || echo NO); texts: $(head -6 $OUT/book-texts.txt | tr '\n' '|' | cut -c1-200)"
  log "BOOK chips: WantToRead=$([ -n "$(xy text "$L_WANT")" ] && echo "present [$(xy --state text "$L_WANT")]" || echo MISSING) NotifyWhenAvailable=$(has "$L_NOTIFY" && echo present || echo absent) AskLibrary=$(has 'Ask the library|Попитай библиотеката' && echo present || echo absent)"

  sec "F2. Want to read: add, list, remove, undo"
  tap text "$L_WANT"; sleep 1; dump; shot func/03-book-want-tapped
  log "WANT tap: snackbar added=$(has 'Added to “Want to read”|Добавена' && echo yes || echo "no ($(texts 40 | grep -oE 'text="[^"]*(Want|Искам)[^"]*"' | tr '\n' ' '))"); chip $(xy --state text "$L_WANT")"
  sleep 3; ready
  if open_want_list; then
    dump; shot func/04-want-list
    python3 $H_/find.py $OUT/ui.xml --texts > $OUT/want-texts.txt
    title=$(grep -Fxf $OUT/book-texts.txt $OUT/want-texts.txt | grep -vE '^(Want to read|Искам да прочета|Back|Назад|On the shelf|На рафта|Available|Borrowed|.{0,3})$' | head -1)
    log "WANT list: book title found in the list: ${title:-NO MATCH} (list texts: $(head -8 $OUT/want-texts.txt | tr '\n' '|' | cut -c1-220))"
    if tap content-desc '=More actions|=Още действия'; then
      sleep 1.5; shot func/05-want-menu
      tap text '=Remove from list|=Махни от списъка'; sleep 1; dump; shot func/06-want-removed-snackbar
      log "WANT remove: snackbar '$(grep -oE 'text="[^"]*(removed from your list|махната от списъка)[^"]*"' $OUT/ui.xml | head -1)' undo=$([ -n "$(xy text '=Undo|=Отмени')" ] && echo present || echo MISSING); title still listed: $([ -n "$title" ] && grep -qF "text=\"$title\"" $OUT/ui.xml && echo yes || echo no)"
      if tap text '=Undo|=Отмени'; then sleep 2; dump; shot func/07-want-after-undo
        log "WANT undo: title back in list: $([ -n "$title" ] && grep -qF "text=\"$title\"" $OUT/ui.xml && echo yes || echo "no ($(texts 8))")"
      else log "WANT undo: Undo action not found"; fi
    else log "WANT: 'More actions' not found"; fi
  fi

  sec "F2b. A book that is not on the shelf ('Notify me when available')"
  local found=0 pts px py
  ready; tab catalog; sleep 3; dump
  grep -q 'text="12"' $OUT/ui.xml || search_catalog 12
  for k in 1 2 3; do
    pts=$(xy --clickables $(results_top) $((H*4/5)) 120)
    while read -r px py; do
      [ -n "$px" ] || continue
      a input tap $px $py; sleep 3; dump
      if has "$L_NOTIFY"; then found=1; shot func/07b-book-not-on-shelf; log "NOTIFY: book not on the shelf: $(texts 8 | cut -c1-250)"; back 2; break 2; fi
      has "$RX_BACK" && back 2
    done <<< "$pts"
    sw_up 400; sleep 2; dump
  done
  [ $found = 1 ] || log "NOTIFY: no book with 'Notify me when available' among the first results"
}
f_events() {
  sec "F3. News & events: segments and calendar subscription"
  ready; tab news; sleep 5; dump; shot func/08-news-segment
  log "NEWS tab: title='News & events' $(has 'text="News &amp; events"' && echo yes || echo NO); segments: $(segstate)"
  segment calendar; dump; shot func/09-calendar-segment
  log "CALENDAR segment: $(segstate); screen: $(texts 14 | cut -c1-250)"
  if tap content-desc "$L_SUBSCRIBE"; then sleep 2.5; dump; shot func/10-subscribe-sheet
    log "SUBSCRIBE sheet: $(texts 12 | cut -c1-300)"
    back 2
  else log "SUBSCRIBE: action not found"; fi
  if tap content-desc '=Calendar view|=Изглед календар'; then sleep 3; shot func/11-calendar-month-view; log "calendar month view: $(show 12 | cut -c1-200)"; tap content-desc '=List view|=Изглед списък'; sleep 2; fi
  segment news; log "back to News: $(segstate)"
}
f_textsize() {
  sec "F4. Settings → Text size → Extra large"
  if open_settings && settings_item '=Text size|=Размер на текста'; then
    sleep 2; dump; shot func/12-text-size-dialog; log "text size options: $(texts 12 | cut -c1-250)"
    tap text '=Extra large|=Много голям' --last; sleep 3; dump; shot func/13-settings-xlarge
    log "TEXT SIZE now: $(grep -oE 'text="(Small|Standard|Large|Extra large)"' $OUT/ui.xml | tr '\n' ' ')"
    link "$SITE/"; sleep 5; dismiss; shot func/14-home-xlarge
    link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 30; sleep 2; shot func/15-catalog-xlarge
    log "xlarge catalogue: $(texts 8 | cut -c1-200)"
    link "$SITE/news"; sleep 4; shot func/15b-news-xlarge
    open_settings && settings_item '=Text size|=Размер на текста' && { sleep 2; tap text '=Standard|=Стандартен' --last; sleep 3; dump; log "TEXT SIZE reset: $(grep -oE 'text="(Small|Standard|Large|Extra large)"' $OUT/ui.xml | tr '\n' ' ')"; shot func/16-settings-reset; }
  fi
}
f_scan() {
  sec "F5. Scanner with CAMERA revoked → manual entry"
  no_camera; log "camera permission: $CAMSTATE"
  if echo "$CAMSTATE" | grep -q 'granted=true'; then
    log "SCAN: SKIPPED — camera permission still granted (the emulator would die if the camera opened)"
  else
    ready; link "$SITE/"; sleep 4; dismiss; dump
    if [ -n "$(xy text '=Scan|=Сканирай')" ]; then tap text '=Scan|=Сканирай'; log "SCAN opened from the Home quick action 'Scan'"
    else a am start -a org.chyavorec.app.SHORTCUT -d chyavorec://app/scan -n $ACT >/dev/null; log "SCAN opened through the 'scan' shortcut (no 'Scan' quick action on Home)"; fi
    sleep 4; dump; shot func/17-scanner-no-camera
    if has 'package="com.(google.)?android.permissioncontroller"'; then log "SCAN: system permission dialog shown: $(texts 6)"; tap text "t allow|=Deny"; sleep 2; dump; fi
    log "SCAN screen: $(texts 10 | cut -c1-250)"
    if tap text '=Enter the number manually|=Въведи номера на ръка'; then
      sleep 2; shot func/18-manual-entry
      a input text $INV; sleep 1.5; dump; shot func/19-manual-typed
      log "manual field: $(grep -oE "text=\"$INV\"" $OUT/ui.xml | head -1)"
      tap text '=Search|=Търси' --last; sleep 4; dump; shot func/20-manual-result
      log "SCAN result: $(texts 12 | cut -c1-300)"
      if tap text '=Open book|=Отвори книгата'; then sleep 4; dump; shot func/21-scan-book
        log "SCAN → book: $(has "$RX_BACK" && echo opened || echo 'NOT opened'); $(texts 6 | cut -c1-200)"
      else log "SCAN: 'Open book' not found"; fi
    else log "SCAN: manual entry button NOT found"; fi
    log "emulator alive after scanner: $(timeout 20 adb shell echo yes 2>/dev/null | tr -d '\r')"
  fi
}
f_clear() {
  sec "F6. Settings → Clear temporary data → catalogue reloads"
  if open_settings && settings_item '=Clear temporary data|=Изчисти временните данни'; then
    sleep 2; shot func/22-clear-dialog
    tap text '=Clear|=Изчисти' --last; sleep 1; dump; shot func/23-cleared
    log "CLEAR: snackbar $(has 'Temporary data cleared|Временните данни' && echo shown || echo 'not seen')"
    t0=$(now_ms); link "$SITE/elektronen-katalog"
    if waitfor "$RX_RESULTS" 120; then log "CLEAR → catalogue results again after $(( $(now_ms) - t0 )) ms ($(rcount) results)"; else log "CLEAR → catalogue: NO results in 120 s: $(texts 8)"; fi
    shot func/24-catalog-after-clear
    link "$SITE/"; sleep 6; dismiss; dump; shot func/25-home-after-clear; log "home after clear: $(texts 10 | cut -c1-220)"
    link "$SITE/news"; sleep 6; dump; shot func/26-news-after-clear; log "news after clear: $(texts 10 | cut -c1-220)"
  fi
  mem func-end
}

# ================================================================= LOOK
matrix() {
  local v=$1
  echo "======== matrix $v"
  restart; dims
  link "$SITE/"; sleep 5; dismiss; shot $v/01-home
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; sleep 2; shot $v/02-catalog
  link "$SITE/news"; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; sleep 1; shot $v/03-news
  segment calendar; sleep 1; shot $v/04-calendar
  log "matrix $v: news title $(has 'text="(News &amp; events|Новини и събития)"' && echo OK || echo MISSING); segments $(segstate)"
  segment news
  link "$BOOK_URL"; sleep 4; shot $v/05-book; back
}
look_warm() {
  sec "L0. First launch and warm-up"
  launch >/dev/null; sleep 6; ready
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  link "$EVENTS_URL"; sleep 6; link "$SITE/news"; sleep 5
}
look1() {
  sec "L1. Light EN"; matrix light-en
  sec "L2. Dark"; a cmd uimode night yes; sleep 3; log "night: $(a cmd uimode night | tr -d '\r')"; matrix dark-en
  a cmd uimode night no; sleep 2
  sec "L3. BG locale"
  a cmd locale set-app-locales $PKG --locales bg-BG; sleep 3
  log "app locale: $(a cmd locale get-app-locales $PKG | tr -d '\r')"
  matrix bg
  a cmd locale set-app-locales $PKG --locales en-US; sleep 2
}
look_font() {
  sec "L4. Font scale 1.3"
  a settings put system font_scale 1.3; sleep 2; matrix font130
  a settings put system font_scale 1.0; sleep 2
}
look_land() {
  sec "L5. Landscape"
  rot 1; log "landscape: ${W}x${H}"; matrix landscape
  dump; log "landscape rail labels: $(for t in home catalog my news more; do [ -n "$(xy text "$(tablabel $t)")" ] && printf '%s ' $t; done)"
  rot 0
}
look2() { look_font; look_land; }

# ================================================================= OFFLINE
part_offline() {
  local p t
  sec "O1. Online warm-up (fills the caches)"
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  link "$BOOK_URL"; sleep 4; back
  link "$SITE/news"; sleep 6; dump; p=$(xy --first-clickable $((H/4)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; sleep 5; back; }
  link "$EVENTS_URL"; sleep 6
  link "$SITE/kontakti"; sleep 6; show 8; log "contacts online: $(texts 8 | cut -c1-200)"; shot offline/00-contacts-online; back
  tab more; sleep 3; tap text '=Gallery|=Галерия'; sleep 6; back
  tab home; sleep 4

  sec "O2. Offline cold start with caches"
  a am force-stop $PKG; offline
  launch | tr '\n' ' '; echo; sleep 6; dismiss
  show 12; shot offline/01-home; log "offline home: $(texts 14 | cut -c1-300)"
  tab catalog; waitfor "$RX_RESULTS" 30 && log "offline catalogue: results shown (cached, ${WAITED} ms)" || log "offline catalogue: NO results"; shot offline/02-catalog
  link "$BOOK_URL"; sleep 4; show 10; shot offline/03-book; log "offline book: $(texts 6 | cut -c1-200)"; back
  link "$SITE/news"; sleep 5; show 10; shot offline/04-news; log "offline news: $(texts 12 | cut -c1-250)"
  dump; p=$(xy --first-clickable $((H/4)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; sleep 5; show 10; shot offline/05-article; log "offline article: $(texts 6 | cut -c1-200)"; back; }
  link "$EVENTS_URL"; sleep 5; show 10; shot offline/06-calendar; log "offline calendar: $(texts 12 | cut -c1-250)"
  tab more; sleep 3; tap text '=Gallery|=Галерия'; sleep 5; show 10; shot offline/07-gallery; log "offline gallery: $(texts 10 | cut -c1-200)"; back
  link "$SITE/kontakti"; sleep 5; show 10; shot offline/08-contacts; log "offline contacts: $(texts 12 | cut -c1-300)"

  sec "O3. Reconnect (staying on Contacts, no touch), then the other screens"
  online
  for t in 10 25; do sleep $((t == 10 ? 10 : 15)); dump; log "contacts +${t}s after reconnect (no touch): $(texts 10 | cut -c1-250)"; done
  shot offline/09-contacts-after-reconnect
  back; link "$SITE/"; sleep 5; dump; shot recovered/01-home; log "recovered home: $(texts 12 | cut -c1-250)"
  link "$SITE/news"; sleep 5; dump; shot recovered/02-news; log "recovered news (link /news): $(segstate); $(texts 10 | cut -c1-200)"
  link "$EVENTS_URL"; sleep 5; shot recovered/03-calendar
  t0=$(now_ms); link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; shot recovered/04-catalog
  tab more; sleep 3; tap text '=Gallery|=Галерия'; sleep 6; dump; shot recovered/05-gallery; log "recovered gallery: $(texts 8 | cut -c1-200)"; back

  sec "O4. Empty cache: offline first launch on Contacts and Gallery, then reconnect"
  a am force-stop $PKG; a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; no_camera >/dev/null
  offline
  launch >/dev/null; sleep 6; ready
  link "$SITE/kontakti"; sleep 5; show 10; shot offline-empty/01-contacts-offline; log "contacts offline (empty cache): $(texts 12 | cut -c1-300)"
  online
  for t in 10 25; do sleep $((t == 10 ? 10 : 15)); dump; log "contacts (empty cache) +${t}s after reconnect (no touch): $(texts 10 | cut -c1-250)"; done
  shot offline-empty/02-contacts-after-reconnect
  if dump && [ -n "$(xy text '=Try again|=Опитай отново')" ]; then tap text '=Try again|=Опитай отново'; sleep 6; dump; shot offline-empty/03-contacts-after-retry; log "contacts after 'Try again': $(texts 10 | cut -c1-250)"; fi
  back; offline
  tab more; sleep 3; tap text '=Gallery|=Галерия'; sleep 5; show 10; shot offline-empty/04-gallery-offline; log "gallery offline (empty cache): $(texts 8 | cut -c1-200)"
  sleep 20; dump; shot offline-empty/04b-gallery-offline-25s; log "gallery offline (empty cache) after 25 s: $(texts 8 | cut -c1-200)"
  online; sleep 25; dump; shot offline-empty/05-gallery-after-reconnect; log "gallery (empty cache) +25s after reconnect (no touch): $(texts 8 | cut -c1-200)"
  sleep 35; dump; shot offline-empty/05b-gallery-after-reconnect-60s; log "gallery (empty cache) +60s after reconnect (no touch): $(texts 8 | cut -c1-200)"
  if [ -n "$(xy text '=Try again|=Опитай отново')" ]; then tap text '=Try again|=Опитай отново'; sleep 6; dump; shot offline-empty/06-gallery-after-retry; log "gallery after 'Try again': $(texts 8 | cut -c1-200)"; fi
  back; sleep 2; tap text '=Gallery|=Галерия'; sleep 8; dump; shot offline-empty/07-gallery-reopened; log "gallery reopened (back + Gallery): $(texts 8 | cut -c1-200)"
}

# ================================================================= main
setup
# Наставката след „-“ (напр. stress3-swangle) е само за режима на GPU в workflow-а.
case ${PART%%-*} in
  start35) part_start35;; perf) part_perf;; offline) part_offline;; wantjank) part_wantjank;;
  # Трети кръг: стъпките, след които емулаторът падаше (голям текст), са последни.
  func3) warmup; f_scan; f_clear; f_textsize;;
  func4) warmup; f_textsize;;
  look3) look_warm; look_land; look_font;;
  probe*) warmup; s_swipes;;
  stress1) warmup; s_cycles; s_swipes;;
  stress2) warmup; s_rotation; s_procdeath;;
  stress3) warmup; s_monkey;;
  func1) warmup; f_want; f_events;;
  func2) warmup; f_textsize; f_scan; f_clear;;
  look1) look_warm; look1;;
  look2) look_warm; look2;;
  *) log "unknown part $PART";;
esac
alive "end"
echo "## COMPLETE" >> $SUM
finish
FAIL=0
grep -q "FATAL EXCEPTION" $OUT/logcat-live.txt && grep -A3 "FATAL EXCEPTION" $OUT/logcat-live.txt | grep -q "$PKG" && FAIL=1
[ $FAIL = 0 ] && echo "RESULT: OK" || echo "RESULT: crash in app"
exit 0
