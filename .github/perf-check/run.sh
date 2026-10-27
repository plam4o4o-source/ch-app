#!/usr/bin/env bash
# Проверка на вълна 13 на емулатор преди издание (ВРЕМЕННО, клон debug/wave13-check, v127).
# Приложението е на български (app locale bg-BG), както го виждат читателите.
#   run.sh perf    — студен старт (10 пъти), памет при 20 цикъла по разделите + книга, monkey 4000
#   run.sh func1   — бързите действия на „Начало“, каталог „12“ и книга с други екземпляри на рафта,
#                    „Искам да прочета“ (Кажи ми, отмяна), връзките /news и /events, повторно
#                    докосване на „Новини“ на Календар, „Изчисти временните данни“
#   run.sh func2   — скенер без камера (на ръка, „Назад“ от панела), „Контакти“, „Много голям“
#                    на „Начало“, най-едрият шрифт + „Много голям“ в Календар, хоризонтално „Начало“
#   run.sh offline — „Моето“ без връзка като гост, галерия без връзка с празен кеш и възстановяване
set -u
PART=${1:-perf}
PKG=org.chyavorec.app
ACT=$PKG/.MainActivity
SITE=https://chyavorec.org
INV=399560
BOOK_URL="$SITE/elektronen-katalog?inv=$INV"
EVENTS_URL="$SITE/events"
# Екземпляр, който е зает, докато други от заглавието са на рафта (katalog.json от 17.10.2026):
# резерва, ако такъв не се намери сред резултатите за „12“.
OUT_COPY_INV=1240
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
L_NOTIFY='Notify me when|Кажи ми, когато е на рафта'
L_NOTIFY_ON='We’ll notify you|Ще ти кажем, когато е на рафта'
L_ASK='Ask the library|Попитай библиотеката'
L_UNDO='=Undo|=Отмени'
L_MORE_ACTIONS='=More actions|=Още действия'
RX_COPY_OUT='This copy is out|Този екземпляр е зает'
RX_GALLERY_LOADED='text="[0-9]+ (photos?|снимк[аи])"'

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
  # Приложението на български (етикетите в проверките са българските).
  a cmd locale set-app-locales $PKG --locales bg-BG; log "app locale: $(a cmd locale get-app-locales $PKG | tr -d '\r')"
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


# ---------------------------------------------------------------- помощни (v127)
# Връзка точно както в предишната проверка: VIEW към пакета, без името на активността.
link_pkg() { echo ">>> link(pkg) $1"; a am start -W -a android.intent.action.VIEW -d "\"$1\"" $PKG 2>&1 | grep -E "Status|LaunchState|TotalTime|Error" | tr '\n' ' '; echo; }
ime_shown() { a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)'; }
bounds_of() { python3 $H_/find.py $OUT/ui.xml --bounds "$@"; }
yes_no() { if eval "$1"; then echo yes; else echo NO; fi; }
# Бързите действия на „Начало“: етикет → "x y" (или нищо).
QA_LABELS=('=Сканирай|=Scan' '=Читателска карта|=Library card' '=Искам да прочета|=Want to read' '=Контакти|=Contacts')
qa_report() {  # $1 — етикет за дневника
  local l p ys="" i
  for i in 1 2 3; do
    dump; p=$(xy text '=Контакти|=Contacts'); [ -n "$p" ] && break
    sw_up 250; sleep 1.5
  done
  local out=""
  for l in "${QA_LABELS[@]}"; do
    p=$(xy text "$l")
    out="$out ${l#=}@[${p:-MISSING}]"
    [ -n "$p" ] && ys="$ys ${p#* }"
  done
  # редове: различни y (±40 px)
  local rows; rows=$(echo $ys | tr ' ' '\n' | sort -n | awk 'NR==1{c=1;p=$1;next} {if ($1-p>40) c++; p=$1} END{print c+0}')
  local events; events=$(has 'text="(Събития|Events)"' && echo "PRESENT" || echo absent)
  log "QA $1:$out; rows=$rows; quick action 'Събития': $events"
}

# ================================================================= PERF
warm_first() {
  sec "0. First launch, catalogue index"
  launch | tee $OUT/start-first.txt; sleep 5; dismiss; sleep 2; ready; dims; shot 00-home-first
  tab catalog
  if waitfor "$RX_RESULTS" 150; then log "FIRST-INSTALL catalogue results after ${WAITED} ms"; else log "FIRST-INSTALL: NO catalogue results in 150 s"; fi
  for i in 1 2 3; do
    a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 3; dismiss
    tab catalog
    if waitfor "$RX_RESULTS" 60; then log "INDEX-READY #$i catalogue results after ${WAITED} ms (cold start → tap Catalogue → text)"; else log "INDEX-READY #$i: NO results in 60 s"; fi
  done
}
p_cold() {
  sec "1. Cold start (10 runs, as installed)"
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
  dismiss
}
p_cycles() {
  sec "2. Memory over 20 cycles (all tabs + a book)"
  restart; tab home; sleep 3; dump
  declare -A TXY
  for t in home catalog my news more; do
    ref='=More|=Още'; [ $t = more ] && ref='=Home|=Начало'
    TXY[$t]=$(xy --tab "$(tablabel $t)" "$ref"); log "tab $t @ ${TXY[$t]}"
  done
  mem c00
  for c in $(seq 1 20); do
    for t in home catalog my news more; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 0.4; done
    link "$BOOK_URL" >/dev/null; sleep 1.2
    [ $c = 1 ] && { show 12; has "$RX_BACK" && log "cycle 1: book screen opened" || log "cycle 1: book screen NOT detected"; shot cycles-book; }
    back 0.6
    case $c in 10|20) mem c$c;; esac
  done
  log "pid after cycles: $(a pidof $PKG | tr -d '\r')"
}
p_monkey() {
  sec "3. Monkey 4000 events (seed 1234, CAMERA revoked)"
  no_camera; log "camera permission before monkey: $CAMSTATE"
  log "statusbar disable: $(a cmd statusbar send-disable-flag statusbar-expansion notification-alerts 2>&1 | tr -d '\r' | head -2)"
  restart
  log "monkey start pid=$(a pidof $PKG | tr -d '\r')"
  a log -t PERFCHECK "monkey start"
  t0=$(now_ms)
  timeout 1200 adb shell monkey -p $PKG --pct-syskeys 0 --pct-anyevent 0 --throttle 80 -s 1234 -v 4000 > $OUT/monkey.txt 2>&1
  log "MONKEY exit=$? duration=$(( ($(now_ms) - t0) / 1000 )) s; $(grep -E 'Events injected|Monkey finished|aborted' $OUT/monkey.txt | tr '\n' ' ')"
  log "MONKEY markers: CRASH=$(grep -c 'CRASH' $OUT/monkey.txt) ANR=$(grep -c 'NOT RESPONDING\|ANR in' $OUT/monkey.txt) FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/monkey.txt)"
  grep -nE -A20 'CRASH|NOT RESPONDING|ANR in' $OUT/monkey.txt | head -60
  alive "after monkey"
  sleep 2; log "pid after monkey: '$(a pidof $PKG | tr -d '\r')'; camera: $(a dumpsys package $PKG | grep -m1 'android.permission.CAMERA: granted' | tr -s ' ' | tr -d '\r')"; shot monkey-end; show 12
  mem after-monkey
}
part_perf() { warm_first; p_cold; p_cycles; p_monkey; }

# ================================================================= FUNC1
warmup() {
  sec "W. First launch and warm-up"
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  tab news; sleep 5; tab home; sleep 5
}
f_home_qa() {
  sec "F1. Home quick actions (Scan, Library card, Want to read, Contacts)"
  restart; link "$SITE/"; sleep 5; dismiss
  qa_report "standard text"; shot func/01-home-quick-actions
  local p cy
  cy=$(xy text '=Контакти|=Contacts'); cy=${cy#* }
  if [ -n "$cy" ] && tap text "$L_WANT" --miny $((cy - 80)) --maxy $((cy + 80)); then
    sleep 3; dump; shot func/02-qa-want-to-read
    log "QA 'Искам да прочета' → screen: back=$(has "$RX_BACK" && echo yes || echo NO) title=$(has 'text="(Want to read|Искам да прочета)"' && echo yes || echo NO); $(texts 8 | cut -c1-220)"
    back 2
  else log "QA 'Искам да прочета': not found next to 'Контакти'"; debug_ui qa-want; fi
}
f_catalog() {
  sec "F2. Catalogue: search '12', a copy that is out while others are on the shelf"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; search_catalog 12; dims; shot func/03-catalog-12
  log "SEARCH '12': $(rcount) results; header: $(grep -oE 'text="[0-9]+ (results?|резултата?)[^"]*"' $OUT/ui.xml | head -1)"
  local found=0 sole=0 k pts px py tried=0
  for k in 1 2 3 4; do
    dump; pts=$(xy --all text '=Не е на рафта|=Заета|=Not on the shelf|=On loan' --miny $(results_top) --maxy $((H*17/20)))
    while read -r px py; do
      [ -n "$px" ] || continue
      tried=$((tried+1)); [ $tried -gt 14 ] && break 2
      a input tap $px $py; sleep 3; dump
      if ! has "$RX_BACK"; then echo "(tap @ $px $py did not open a book)"; continue; fi
      if [ $found = 0 ] && has "$RX_COPY_OUT"; then
        found=1; shot func/04-book-copy-out-others-on-shelf
        log "COPY-OUT (from '12'): $(grep -oE "text=\"($RX_COPY_OUT)[^\"]*\"" $OUT/ui.xml | head -1); title: $(python3 $H_/find.py $OUT/ui.xml --texts | sed -n 2p); AskLibrary=$(has "$L_ASK" && echo SHOWN || echo hidden) Notify=$(has "$L_NOTIFY" && echo SHOWN || echo hidden)"
        sw_up 400; sleep 1.5; dump; shot func/04b-book-copy-out-scrolled
        log "COPY-OUT scrolled: AskLibrary=$(has "$L_ASK" && echo SHOWN || echo hidden) Notify=$(has "$L_NOTIFY" && echo SHOWN || echo hidden); copies line: $(grep -oE 'text="[0-9]+ екземпляр[^"]*"' $OUT/ui.xml | head -1)"
      elif [ $sole = 0 ] && has "$L_ASK"; then
        sole=1; f_book_notify
      fi
      back 2
      [ $found = 1 ] && [ $sole = 1 ] && break 2
    done <<< "$pts"
    sw_up 500; sleep 2
  done
  log "COPY-OUT search: tried $tried unavailable rows from '12' (found copy-out=$found, sole-out=$sole)"
  if [ $found = 0 ]; then
    link "$SITE/elektronen-katalog?inv=$OUT_COPY_INV"; sleep 4; dump; shot func/04-book-copy-out-others-on-shelf
    log "COPY-OUT (link inv=$OUT_COPY_INV): $(grep -oE "text=\"($RX_COPY_OUT)[^\"]*\"" $OUT/ui.xml | head -1 || true); AskLibrary=$(has "$L_ASK" && echo SHOWN || echo hidden) Notify=$(has "$L_NOTIFY" && echo SHOWN || echo hidden); $(texts 8 | cut -c1-200)"
    sw_up 400; sleep 1.5; dump; shot func/04b-book-copy-out-scrolled
    log "COPY-OUT scrolled: AskLibrary=$(has "$L_ASK" && echo SHOWN || echo hidden) Notify=$(has "$L_NOTIFY" && echo SHOWN || echo hidden)"
    back 2
  fi
}
# На страницата на книга, която не е на рафта (и няма други екземпляри): „Кажи ми“ вкл./изкл./отмяна.
f_book_notify() {
  shot func/05-book-sole-out
  log "SOLE-OUT book: $(texts 6 | cut -c1-200); AskLibrary=$(has "$L_ASK" && echo shown || echo hidden) Notify=$(has "$L_NOTIFY" && echo shown || echo hidden)"
  if [ -z "$(xy text "$L_NOTIFY")" ]; then sw_up 300; sleep 1; dump; fi
  if tap text "$L_NOTIFY"; then
    sleep 1.5; dump; shot func/06-book-notify-on
    log "BOOK 'Кажи ми' ON: label now '$(grep -oE "text=\"($L_NOTIFY_ON)[^\"]*\"" $OUT/ui.xml | head -1)'; snackbar: $(grep -oE 'text="(Ще получиш[^"]*|You’ll get[^"]*|Известията[^"]*)"' $OUT/ui.xml | head -1)"
    if tap text "$L_NOTIFY_ON"; then
      sleep 1.5; dump; shot func/07-book-notify-off-snackbar
      log "BOOK 'Кажи ми' OFF: snackbar '$(grep -oE 'text="(Няма да получаваш[^"]*|You won’t[^"]*)"' $OUT/ui.xml | head -1)' undo=$([ -n "$(xy text "$L_UNDO")" ] && echo present || echo MISSING)"
      if tap text "$L_UNDO"; then sleep 1.5; dump; shot func/08-book-notify-undo
        log "BOOK 'Кажи ми' undo → $(has "$L_NOTIFY_ON" && echo 'ON again (restored)' || echo "not restored: $(texts 8 | cut -c1-160)")"
      fi
    fi
  else log "BOOK: 'Кажи ми' button not found"; fi
}
f_want() {
  sec "F3. Want to read: add, 'Кажи ми' on/off, undo, remove with undo"
  ready; link "$BOOK_URL"; sleep 4; dump
  has "$RX_BACK" && [ -z "$(xy text "$L_WANT")" ] && { sw_up 300; sleep 1; dump; }
  log "BOOK $INV: $(texts 5 | cut -c1-160); WantToRead [$(xy --state text "$L_WANT")]"
  if xy --state text "$L_WANT" | grep -q 'checked=false'; then tap text "$L_WANT"; sleep 1; dump; shot func/09-book-want-added
    log "WANT add: snackbar $(has 'Добавена в|Added to' && echo shown || echo 'not seen'); chip [$(xy --state text "$L_WANT")]"
  fi
  sleep 2; back 2
  # Списъкът — от бързото действие на „Начало“.
  ready; link "$SITE/"; sleep 4; dismiss; dump
  local cy; cy=$(xy text '=Контакти|=Contacts'); cy=${cy#* }
  if [ -n "$cy" ] && tap text "$L_WANT" --miny $((cy - 80)) --maxy $((cy + 80)); then log "WANT list opened from the Home quick action"
  else log "WANT: quick action not found, via My"; open_want_list; fi
  sleep 3; dump; shot func/10-want-list
  python3 $H_/find.py $OUT/ui.xml --texts > $OUT/want-texts.txt
  log "WANT list: $(head -12 $OUT/want-texts.txt | tr '\n' '|' | cut -c1-300)"
  log "WANT status: lowercase 'налична' $(has 'text="(налична|available)"' && echo present || echo MISSING); capitalised pill 'Налична' $(has 'text="(Налична|Available)"' && echo PRESENT || echo absent); 'Не е на рафта' $(has 'text="Не е на рафта"' && echo present || echo absent)"
  # Редът на налична книга: менюто „Още действия“ в същия ред като „налична“.
  local ry mp
  ry=$(xy text '=налична|=available'); ry=${ry#* }
  menu_row() { dump; if [ -n "$ry" ]; then mp=$(xy content-desc "$L_MORE_ACTIONS" --miny $((ry - 150)) --maxy $((ry + 150))); else mp=$(xy content-desc "$L_MORE_ACTIONS"); fi
    [ -n "$mp" ] || { echo "menu not found"; return 1; }; a input tap $mp; sleep 1.5; }
  if menu_row; then
    shot func/11-want-menu
    tap text "$L_NOTIFY"; sleep 1.5; dump; shot func/12-want-notify-on
    log "WANT 'Кажи ми' ON: snackbar '$(grep -oE 'text="(Ще получиш[^"]*|You’ll get[^"]*|Известията[^"]*)"' $OUT/ui.xml | head -1)'; bell icon $(has "content-desc=\"($L_NOTIFY_ON)" && echo shown || echo MISSING)"
    sleep 2
    if menu_row; then
      tap text '=Не ми казвай|=Stop notifying me'; sleep 1.5; dump; shot func/13-want-notify-off-snackbar
      log "WANT 'Кажи ми' OFF: snackbar '$(grep -oE 'text="(Няма да получаваш[^"]*|You won’t[^"]*)"' $OUT/ui.xml | head -1)'; undo $([ -n "$(xy text "$L_UNDO")" ] && echo present || echo MISSING); bell $(has "content-desc=\"($L_NOTIFY_ON)" && echo still-shown || echo gone)"
      if tap text "$L_UNDO"; then sleep 1.5; dump; shot func/14-want-notify-undo; log "WANT 'Кажи ми' undo: bell $(has "content-desc=\"($L_NOTIFY_ON)" && echo 'back (ON restored)' || echo 'NOT back')"; fi
    fi
    sleep 2
    if menu_row; then
      tap text '=Махни от списъка|=Remove from list'; sleep 1.2; dump; shot func/15-want-removed-snackbar
      log "WANT remove: snackbar '$(grep -oE 'text="[^"]*(е махната от списъка|removed from your list)"' $OUT/ui.xml | head -1)'; undo $([ -n "$(xy text "$L_UNDO")" ] && echo present || echo MISSING); 'налична' row $(has 'text="налична"' && echo 'still listed' || echo gone)"
      if tap text "$L_UNDO"; then sleep 2; dump; shot func/16-want-after-undo; log "WANT remove undo: book $(has 'text="12 -те вятъра"' && echo 'back in list' || echo "NOT back: $(texts 8 | cut -c1-160)")"; fi
    fi
  else log "WANT: 'Още действия' not found"; debug_ui want-menu; fi
  back 2
}
f_news() {
  sec "F4. Links: /news opens News after Calendar was used; /events opens Calendar (also from an article)"
  ready; link_pkg "$SITE/news"; sleep 5; dims; log "/news (first): $(segstate)"; shot func/17-news-link
  segment calendar; log "Calendar segment chosen: $(segstate)"; shot func/18-calendar-chosen
  link_pkg "$SITE/news"; sleep 4; log "/news while on Calendar: $(segstate)"; shot func/19-news-link-after-calendar
  segment calendar; tab home; sleep 3
  link_pkg "$SITE/news"; sleep 4; log "/news from Home (News tab left on Calendar): $(segstate)"; shot func/20-news-link-from-home
  a am force-stop $PKG; sleep 2
  link_pkg "$SITE/news"; sleep 6; dismiss; log "/news cold: $(segstate)"
  link_pkg "$EVENTS_URL"; sleep 4; log "/events: $(segstate)"; shot func/21-events-link
  segment news; dump; local p; p=$(xy --first-clickable $((H/3)) $((H*3/4)) 150)
  if [ -n "$p" ]; then a input tap $p; sleep 5; dump; shot func/22-article
    log "article open: back=$(has "$RX_BACK" && echo yes || echo NO); $(texts 4 | cut -c1-160)"
    link_pkg "$EVENTS_URL"; sleep 4; log "/events from an article: $(segstate); back-button $(has "$RX_BACK" && echo 'visible (still on the article?)' || echo none)"; shot func/23-events-from-article
  else log "article: no card found"; fi
}
f_reselect() {
  sec "F5. Reselecting the News tab on Calendar scrolls to the top"
  ready; link_pkg "$EVENTS_URL"; sleep 5; dims; dump
  has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък'; sleep 2; log "switched to list view"; }
  dump; python3 $H_/find.py $OUT/ui.xml --texts > $OUT/reselect-top.txt; shot func/24-calendar-top
  log "calendar top: $(segstate); $(head -10 $OUT/reselect-top.txt | tr '\n' '|' | cut -c1-200)"
  local i; for i in 1 2 3 4; do sw_up 300; sleep 1; done
  sleep 1; dump; python3 $H_/find.py $OUT/ui.xml --texts > $OUT/reselect-scrolled.txt; shot func/25-calendar-scrolled
  log "calendar scrolled: segments visible $([ -n "$(xy text "$L_CAL" --maxy $((H/2)))" ] && echo yes || echo no); common with top: $(grep -Fxc -f $OUT/reselect-top.txt $OUT/reselect-scrolled.txt)/$(wc -l < $OUT/reselect-top.txt)"
  tab news; sleep 3; dump; python3 $H_/find.py $OUT/ui.xml --texts > $OUT/reselect-after.txt; shot func/26-calendar-after-reselect
  log "RESELECT: $(segstate); common with top: $(grep -Fxc -f $OUT/reselect-top.txt $OUT/reselect-after.txt)/$(wc -l < $OUT/reselect-top.txt); first lines: $(head -6 $OUT/reselect-after.txt | tr '\n' '|' | cut -c1-200)"
}
f_clear() {
  sec "F6. Settings → Clear temporary data → catalogue returns"
  if open_settings && settings_item '=Clear temporary data|=Изчисти временните данни'; then
    sleep 2; shot func/27-clear-dialog
    tap text '=Clear|=Изчисти' --last; sleep 1; dump; shot func/28-cleared-snackbar
    log "CLEAR: snackbar $(has 'Temporary data cleared|Временните данни са изтрити' && echo shown || echo 'not seen')"
    t0=$(now_ms); link "$SITE/elektronen-katalog"
    if waitfor "$RX_RESULTS" 120; then log "CLEAR → catalogue results again after $(( $(now_ms) - t0 )) ms ($(rcount) results)"; else log "CLEAR → catalogue: NO results in 120 s: $(texts 8)"; fi
    shot func/29-catalog-after-clear
  fi
  mem func1-end
}
part_func1() { warmup; f_home_qa; f_catalog; f_want; f_news; f_reselect; f_clear; }

# ================================================================= FUNC2
f_scan() {
  sec "F7. Scanner with CAMERA revoked: manual entry, Back from the panel"
  no_camera; log "camera permission: $CAMSTATE"
  if echo "$CAMSTATE" | grep -q 'granted=true'; then log "SCAN: SKIPPED — camera permission still granted"; return; fi
  ready; link "$SITE/"; sleep 4; dismiss; dump
  tap text '=Сканирай|=Scan' || { log "SCAN: no quick action"; return; }
  sleep 4; dump; shot func2/01-scanner-no-camera
  if has 'package="com.(google.)?android.permissioncontroller"'; then log "SCAN: system permission dialog: $(texts 6)"; tap text "t allow|=Deny|=Не разрешавам"; sleep 2; dump; fi
  log "SCAN screen: $(texts 10 | cut -c1-250)"
  if tap text '=Enter the number manually|=Въведи номера на ръка'; then
    sleep 2; dump; shot func2/02-manual-panel
    log "manual panel: field $(has 'ISBN или инвентарен номер|ISBN or inventory number' && echo shown || echo MISSING); IME $(ime_shown)"
    local n=0
    for n in 1 2; do
      back 2; dump
      log "Back #$n: panel $(has 'text="(ISBN или инвентарен номер|ISBN or inventory number)"' && echo 'still open' || echo closed); IME $(ime_shown); scanner $(has 'text="(Въведи номера на ръка|Enter the number manually)"' && echo visible || echo gone); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
      has 'text="(ISBN или инвентарен номер|ISBN or inventory number)"' || break
    done
    shot func2/03-after-back
    if has 'text="(Въведи номера на ръка|Enter the number manually)"' && tap text '=Enter the number manually|=Въведи номера на ръка'; then
      sleep 2; a input text $INV; sleep 1.5
      tap text '=Search|=Търси' --last; sleep 4; dump; shot func2/04-manual-result
      log "SCAN result: $(texts 10 | cut -c1-250)"
      if tap text '=Open book|=Отвори книгата'; then sleep 4; dump; log "SCAN → book: $(has "$RX_BACK" && echo opened || echo 'NOT opened')"; fi
    fi
  else log "SCAN: manual entry button NOT found"; fi
  log "emulator alive after scanner: $(timeout 20 adb shell echo yes 2>/dev/null | tr -d '\r')"
}
contacts_check() {  # $1 — етикет
  link "$SITE/kontakti"; sleep 6; dims; dump; shot func2/contacts-$1-1
  # кликаемите редове/връзки: изрязани ли са вдясно
  local over; over=$(grep -oE 'clickable="true"[^>]*bounds="\[[0-9]+,[0-9]+\]\[[0-9]+,[0-9]+\]"' $OUT/ui.xml | grep -oE '\]\[[0-9]+' | tr -d '][' | awk -v w=$W '$1>w' | wc -l)
  log "CONTACTS $1: $(texts 14 | cut -c1-300); clickables past the right edge: $over"
  sw_up 500; sleep 1.5; dump; shot func2/contacts-$1-2
  log "CONTACTS $1 (scrolled): $(texts 12 | cut -c1-250)"
  sw_up 500; sleep 1.5; shot func2/contacts-$1-3
  back 2
}
set_text_size() {  # етикет: '=Very large|=Много голям' и др.
  open_settings && settings_item '=Text size|=Размер на текста' || return 1
  sleep 2; tap text "$1" --last; sleep 3; dump
  log "TEXT SIZE now: $(grep -oE 'text="(Small|Standard|Large|Extra large|Малък|Стандартен|Голям|Много голям)"' $OUT/ui.xml | tr '\n' ' ')"
}
f_bigtext() {
  sec "F8. Contacts at standard text"
  contacts_check standard
  sec "F9. Home at 'Много голям': 2x2 quick actions, whole labels, upcoming events"
  if set_text_size '=Extra large|=Много голям'; then
    shot func2/05-settings-xlarge
    restart; link "$SITE/"; sleep 5; dismiss
    qa_report "Много голям"; shot func2/06-home-xlarge-quick-actions
    local i; for i in 1 2 3 4 5 6; do dump; [ -n "$(xy text '=Предстоящи събития|=Upcoming events')" ] && break; sw_up 300; sleep 1.2; done
    local p; p=$(xy text '=Предстоящи събития|=Upcoming events')
    if [ -n "$p" ]; then
      # заглавието горе, картите под него
      a input swipe $((W/2)) ${p#* } $((W/2)) $((H/5)) 600; sleep 2; dump; shot func2/07-home-xlarge-upcoming
      log "upcoming events (xlarge): $(texts 14 | cut -c1-300)"
    else log "upcoming events section NOT found (no upcoming events?)"; shot func2/07-home-xlarge-upcoming; fi
    sec "F10. Contacts at 'Много голям'"
    contacts_check xlarge
    sec "F11. Largest system font (1.3) + 'Много голям': Календар label, actions on a second row"
    a settings put system font_scale 1.3; sleep 3; log "font_scale=$(a settings get system font_scale | tr -d '\r')"
    restart; link_pkg "$EVENTS_URL"; sleep 5; dims; dump; shot func2/08-calendar-font130-xlarge
    local cal sub news
    cal=$(bounds_of text "$L_CAL" --maxy $((H/2))); news=$(bounds_of text "$L_NEWSSEG" --maxy $((H/2))); sub=$(bounds_of content-desc "$L_SUBSCRIBE")
    log "FONTMAX calendar: $(segstate); segment 'Календар' bounds [$cal], 'Новини' [$news], subscribe [$sub]"
    if [ -n "$cal" ] && [ -n "$sub" ]; then set -- $cal; local cb=$4; set -- $sub
      log "FONTMAX actions: $([ $2 -ge $cb ] && echo 'on a SECOND row (below the segments)' || echo 'on the SAME row as the segments')"; fi
    link "$SITE/"; sleep 5; dismiss; qa_report "font 1.3 + Много голям"; shot func2/09-home-font130-xlarge
    a settings put system font_scale 1.0; sleep 2
    set_text_size '=Standard|=Стандартен'; shot func2/10-settings-reset
  fi
}
f_landscape() {
  sec "F12. Landscape Home: hero at most about half the height"
  restart; rot 0; sleep 1; rot 1; sleep 2
  log "landscape check: $(a dumpsys window displays | tr -d '\r' | grep -m1 -oE 'mCurrentRotation=[^ ]+|mRotation=[^ ]+') ${W}x${H}"
  link "$SITE/"; sleep 4; dismiss
  waitfor 'content-desc="(Последни новини|Latest news)"' 25
  dump; shot func2/11-home-landscape
  local hb; hb=$(bounds_of content-desc '=Последни новини|=Latest news')
  if [ -n "$hb" ]; then set -- $hb; log "HERO landscape: bounds [$hb] height $(( $4 - $2 )) px of $H px = $(( ($4 - $2) * 100 / H ))%"
  else log "HERO landscape: carousel not found: $(texts 8 | cut -c1-200)"; fi
  sw_up 300; sleep 1.5; shot func2/12-home-landscape-scrolled
  rot 0; sleep 2
}
part_func2() { warmup; f_scan; f_bigtext; f_landscape; }

# ================================================================= OFFLINE
part_offline() {
  sec "O1. Empty cache, offline, guest: My"
  a am force-stop $PKG; a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; no_camera >/dev/null
  a cmd locale set-app-locales $PKG --locales bg-BG
  offline
  launch >/dev/null; sleep 6; ready; dims; shot offline/00-home-offline
  tab my; sleep 4; dump; shot offline/01-my-guest-offline
  log "MY offline guest: add-card $(has 'text="(Добави читателска карта|Add library card)"' && echo shown || echo MISSING); note $(has 'Не можем да проверим|t check whether' && echo shown || echo absent); $(texts 14 | cut -c1-300)"
  sleep 8; dump; shot offline/02-my-guest-offline-later
  log "MY offline guest (+8 s): add-card $(has 'text="(Добави читателска карта|Add library card)"' && echo shown || echo MISSING); note $(has 'Не можем да проверим|t check whether' && echo shown || echo absent)"

  sec "O2. Gallery offline with an empty cache"
  tab more; sleep 3; dims
  local i p
  for i in 1 2 3 4; do dump; p=$(xy text '=Галерия|=Gallery'); [ -n "$p" ] && break; sw_up 300; sleep 1; done
  a input tap $p; t0=$(now_ms); sleep 3; dump; shot offline/03-gallery-offline-3s
  log "gallery offline 3 s: $(texts 8 | cut -c1-200)"
  if waitfor 'text="(Опитай отново|Try again)"' 30; then log "GALLERY offline: 'Опитай отново' after $(( $(now_ms) - t0 )) ms; $(texts 8 | cut -c1-200)"
  else log "GALLERY offline: NO 'Опитай отново' within ~33 s (skeleton?): $(texts 8 | cut -c1-200)"; fi
  shot offline/04-gallery-offline-error
  sleep 15; dump; shot offline/05-gallery-offline-later; log "gallery offline (+15 s): $(texts 8 | cut -c1-200)"

  sec "O3. Reconnect on the Gallery (no touch)"
  online; t0=$(now_ms)
  local net=-1 got=-1 now
  for i in $(seq 1 60); do
    now=$(( $(now_ms) - t0 ))
    [ $net = -1 ] && a dumpsys connectivity | tr -d '\r' | grep -qE 'Active default network: [0-9]+' && net=$now
    if dump && has "$RX_GALLERY_LOADED"; then got=$(( $(now_ms) - t0 )); break; fi
    [ $now -ge 60000 ] && break
    sleep 0.5
  done
  shot offline/06-gallery-after-reconnect
  log "GALLERY reconnect: default network back after ${net} ms; albums shown after ${got} ms (-1 = not within 60 s); $(texts 8 | cut -c1-200)"
  if [ $got = -1 ] && [ -n "$(xy text '=Опитай отново|=Try again')" ]; then tap text '=Опитай отново|=Try again'; sleep 8; dump; shot offline/07-gallery-after-retry; log "gallery after 'Опитай отново': $(texts 8 | cut -c1-200)"; fi
  back 2

  sec "O4. My offline as guest again (after the online start)"
  tab my; sleep 4; shot offline/08-my-online
  a am force-stop $PKG; offline; launch >/dev/null; sleep 6; ready; tab my; sleep 4; dump; shot offline/09-my-guest-offline-2
  log "MY offline guest #2: add-card $(has 'text="(Добави читателска карта|Add library card)"' && echo shown || echo MISSING); note $(has 'Не можем да проверим|t check whether' && echo shown || echo absent); $(texts 14 | cut -c1-300)"
  online
}

# ================================================================= main
setup
case ${PART%%-*} in
  perf) part_perf;;
  func1) part_func1;;
  func2) part_func2;;
  offline) part_offline;;
  *) log "unknown part $PART";;
esac
alive "end"
echo "## COMPLETE" >> $SUM
finish
FAIL=0
grep -q "FATAL EXCEPTION" $OUT/logcat-live.txt && grep -A3 "FATAL EXCEPTION" $OUT/logcat-live.txt | grep -q "$PKG" && FAIL=1
[ $FAIL = 0 ] && echo "RESULT: OK" || echo "RESULT: crash in app"
exit 0
