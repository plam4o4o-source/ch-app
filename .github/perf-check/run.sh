#!/usr/bin/env bash
# Пълна проверка на издадената v1.2.27 (origin/main 8dc63c1) на емулатор (ВРЕМЕННО, клон
# debug/full-test-3, резултати в emulator-results/v127b). Приложението е на български (bg-BG),
# освен в матрицата с изгледите (EN варианти).
#   abstart-1/2 — студен и топъл старт A/B срещу v1.2.26 на един емулатор (ABBA / BAAB),
#                 време до резултатите в каталога (първа инсталация и с готов индекс)
#   jank-1/2    — jank при 540x1200: каталог (30 плъзгания, „Покажи още“), „Начало“, „Новини“,
#                 „Календар“, „Искам да прочета“ (16 книги), галерия (албуми, снимки, преглед);
#                 и двете издания, в обратен ред във втората част
#   mem, mem-base — памет при 30 цикъла (разделите + книга + преглед в галерията)
#   monkey      — 5000 събития, seed 4321, без камера
#   media       — галерия (албуми, увеличение над 2×, 20 снимки, памет), корици, PDF документ
#   rot         — завъртане и смърт на процеса: „Начало“, каталог „12“, книга, „Искам да прочета“, Календар
#   offline     — без връзка с кеш и с празен кеш по всички раздели, възстановяване
#   look1/2/3   — матрица от снимки: светла/тъмна BG/EN; шрифт 1.3 + „Много голям“, хоризонтално;
#                 малък екран и таблет
#   func1/func2 — функциите от вълна 13
#   update      — „Какво ново“: v1.2.26 → v1.2.27 с adb install -r и през самообновяването
set -u
PART=${1:-perf}
PKG=org.chyavorec.app
ACT=$PKG/.MainActivity
SITE=https://chyavorec.org
INV=399560
BOOK_URL="$SITE/elektronen-katalog?inv=$INV"
EVENTS_URL="$SITE/events"
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
L_GALLERY='=Gallery|=Галерия'
RX_COPY_OUT='This copy is out|Този екземпляр е зает'
RX_GALLERY_LOADED='text="[0-9]+ (photos?|снимк[аи])"'
RX_WHATSNEW='(What’s new in version|Какво ново във версия)'

# ---------------------------------------------------------------- помощни
a() { timeout ${T:-40} adb shell "$@"; }
log() { echo "$*" | tee -a $SUM; }
sec() { echo; echo "################ $* ################"; echo "## $*" >> $SUM; alive "$*"; a log -t PERFCHECK "'section: $*'"; }
now_ms() { echo $(( $(date +%s%N) / 1000000 )); }
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
sw_left() { a input swipe $((W*85/100)) $((H/2)) $((W*15/100)) $((H/2)) ${1:-200}; }
tablabel() {
  case $1 in
    home) echo '=Home|=Начало';; catalog) echo '=Catalogue|=Каталог';; my) echo '=My|=Моето';;
    news) echo '=News|=Новини';; more) echo '=More|=Още';;
  esac
}
tab() {
  local p ref='=More|=Още'; [ $1 = more ] && ref='=Home|=Начало'
  dump || return 1; p=$(xy --tab "$(tablabel $1)" "$ref")
  [ -n "$p" ] || { echo "tab not found: $1"; debug_ui tab-$1; return 1; }; echo ">>> tab $1 @ $p"; a input tap $p
}
link() { echo ">>> link $1"; a am start -W -a android.intent.action.VIEW -d "\"$1\"" -n $ACT 2>&1 | grep -E "Status|LaunchState|TotalTime|Error" | tr '\n' ' '; echo; }
link_pkg() { echo ">>> link(pkg) $1"; a am start -W -a android.intent.action.VIEW -d "\"$1\"" $PKG 2>&1 | grep -E "Status|LaunchState|TotalTime|Error" | tr '\n' ' '; echo; }
launch() { a am start -W -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $ACT 2>&1 | grep -E "Status|LaunchState|TotalTime|WaitTime|Warning|Error"; }
scanner() { a am start -a org.chyavorec.app.SHORTCUT -d chyavorec://app/scan -n $ACT >/dev/null 2>&1; }
# Прозорецът на фокус: „пакет/активност“ (за външните приложения — браузър, телефон, имейл…).
fg() { a dumpsys window 2>/dev/null | tr -d '\r' | grep -m1 'mCurrentFocus=' | sed -E 's/.* ([^ ]+)\}.*/\1/'; }
# Диалози: системен ANR („Wait“), ново издание („По-късно“), известията („Не сега“), въведението („Пропусни“).
dismiss() {
  local i p l
  for i in 1 2 3; do
    dump || return 0; p=""
    if has "isn.t responding|не отговаря"; then p=$(xy text '=Wait|=Изчакване'); [ -n "$p" ] && { echo ">>> system ANR dialog: $(texts 3) → Wait"; log "(system ANR dialog: $(texts 2) → Wait)"; a input tap $p; sleep 3; continue; }; fi
    for l in '=Later' '=По-късно' '=Not now' '=Не сега' '=Skip' '=Пропусни'; do
      p=$(xy text "$l"); [ -n "$p" ] && { echo ">>> dismiss '$l'"; log "(dismissed '$l': $(texts 3 | cut -c1-120))"; a input tap $p; sleep 2; break; }
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
debug_ui() { NDBG=$((NDBG+1)); [ $NDBG -gt 25 ] && return 0; mkdir -p $OUT/debug; cp $OUT/ui.xml $OUT/debug/$NDBG-$1.xml 2>/dev/null; shot debug/$NDBG-$1; }
restart() { a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 4; ready; }
ready() {
  local i
  for i in 1 2 3 4 5 6; do
    dismiss
    dump && has 'text="(Home|Начало)"' && has 'text="(More|Още|My|Моето)"' && return 0
    if ! has "package=\"$PKG\""; then echo "(app not in front: $(texts 6)) → relaunch"; launch >/dev/null; sleep 5; continue; fi
    if has "$RX_BACK"; then echo "(not on a tab screen: $(texts 6)) → back"; back 2
    else echo "(app in front, tab bar not found: $(texts 20))"; debug_ui ready; sleep 3; fi
  done
  log "WARN: tab bar not reached"; return 1
}
hide_ime() { a input keyevent 111; sleep 1; }
results_top() { local p; p=$(xy text ' results|=1 result| резултат'); [ -n "$p" ] && echo $(( ${p#* } + 30 )) || echo $((H/4)); }
rcount() { grep -oE 'text="[0-9]+ (results?|резултата?)' $OUT/ui.xml 2>/dev/null | grep -oE '[0-9]+' | head -1; }
search_catalog() {
  tap text "$L_SEARCH" || tap class EditText || return 1
  sleep 1; a input text "$1"; sleep 3; hide_ime; sleep 1; dump
}
segment() {
  local l=$L_NEWSSEG; [ $1 = calendar ] && l=$L_CAL
  dims; tap text "$l" --maxy $((H/2)) || { debug_ui seg-$1; return 1; }; sleep 4
}
segstate() { dump; echo "News[$(xy --state text "$L_NEWSSEG" --maxy $((H/2)))] Calendar[$(xy --state text "$L_CAL" --maxy $((H/2)))] subscribe-action=$(has "$L_SUBSCRIBE" && echo visible || echo absent)"; }
cal_checked() { segstate | grep -q 'Calendar\[checked=true'; }
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
rot() {
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
settings_item() {
  local i
  for i in 1 2 3 4; do sw_down 200; sleep 0.5; done
  for i in 1 2 3 4 5 6 7 8; do dump; [ -n "$(xy text "$1")" ] && { tap text "$1"; return 0; }; sw_up 300; sleep 1; done
  log "WARN: settings item $1 not found"; debug_ui settings-item; return 1
}
set_text_size() {
  open_settings && settings_item '=Text size|=Размер на текста' || return 1
  sleep 2; tap text "$1" --last; sleep 3; dump
  log "TEXT SIZE now: $(grep -oE 'text="(Small|Standard|Large|Extra large|Малък|Стандартен|Голям|Много голям)"' $OUT/ui.xml | tr '\n' ' ')"
}
open_want_list() {
  local i
  ready; tab my; sleep 3; dims
  for i in 1 2 3 4; do dump; [ -n "$(xy text 'Want to read|Искам да прочета')" ] && { tap text 'Want to read|Искам да прочета'; sleep 3; return 0; }; sw_up 300; sleep 1; done
  log "WARN: 'Want to read' tile not found in My"; debug_ui want-tile; return 1
}
# „Още“ → „Галерия“ (превърта, ако трябва)
open_gallery() {
  local i p=""
  ready; tab more; sleep 2.5; dims
  for i in 1 2 3 4; do dump; p=$(xy text "$L_GALLERY"); [ -n "$p" ] && break; sw_up 300; sleep 1; done
  [ -n "$p" ] || { log "WARN: Gallery not found in More"; debug_ui gallery; return 1; }
  a input tap $p; sleep 1.5
}
# Първият албум (броят снимки върху корицата е в кликаемата колона на албума).
open_album() {  # [N — поредният албум, от 1]
  local n=${1:-1} p
  dump; p=$(xy --all text 'снимки|снимка|photos|photo' | sed -n ${n}p)
  [ -n "$p" ] || { echo "album $n not found"; return 1; }
  echo ">>> album $n @ $p"; a input tap $p; sleep 2
}
# Снимките в албум: кликаеми квадратни клетки под горната лента.
photo_cells() { xy --cells $((H/8)) $((H*9/10)) $(( W/12 > 60 ? W/12 : 60 )); }
NCELLS=0
wait_cells() {  # лимит (s) → WAITED (ms) / -1; NCELLS — колко клетки се виждат
  local lim=$1 t0; t0=$(now_ms); NCELLS=0
  while :; do
    # (на списъка с албумите има „N снимки“ — там не се броят)
    if dump && ! has "$RX_GALLERY_LOADED"; then NCELLS=$(photo_cells | wc -l); [ $NCELLS -gt 0 ] && { WAITED=$(( $(now_ms) - t0 )); return 0; }; fi
    [ $(( $(now_ms) - t0 )) -ge $((lim * 1000)) ] && { WAITED=-1; return 1; }
    sleep 0.3
  done
}
open_photo() {  # [N — поредна снимка в албума, от 1]
  local n=${1:-1} p
  wait_cells 15 || return 1
  p=$(photo_cells | sed -n ${n}p)
  [ -n "$p" ] || return 1
  echo ">>> photo $n @ $p"; a input tap $p; sleep 2.5
}
viewer_page() { dump; grep -oE 'text="[0-9]+ / [0-9]+[^"]*"' $OUT/ui.xml | head -1; }
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
  # Корен — за сензорния екран (sendevent) и кеша на приложението (само в частта media).
  if [ "$PART" = media ]; then timeout 30 adb root 2>&1 | head -2; sleep 4; timeout 60 adb wait-for-device; fi
  log "part $PART; APK: $(cat apk-ref.txt 2>/dev/null) size $(stat -c %s app.apk) bytes"
  log "base APK: $(cat apk-base-ref.txt 2>/dev/null); update-source APK: $(cat apk-base-upd-ref.txt 2>/dev/null)"
  log "host: $(nproc) cpu, $(free -m | awk 'NR==2{print $2}') MB RAM"
  T=180 a getprop ro.build.version.release >/dev/null
  SDK=$(a getprop ro.build.version.sdk | tr -d '\r')
  log "android $(a getprop ro.build.version.release | tr -d '\r') sdk $SDK; $(a wm size | tr -d '\r' | tail -1); $(a wm density | tr -d '\r' | tail -1); adb uid $(a id -u | tr -d '\r')"
  timeout 180 adb install -r app.apk || { log "RESULT: install failed"; exit 1; }
  log "installed: $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  a cmd locale set-app-locales $PKG --locales bg-BG; log "app locale: $(a cmd locale get-app-locales $PKG | tr -d '\r')"
  a pm grant $PKG android.permission.POST_NOTIFICATIONS || true
  no_camera; log "camera permission: $CAMSTATE"
  a settings put system accelerometer_rotation 0; rot 0
  a settings put system font_scale 1.0; a cmd uimode night no
  adb logcat -G 16M >/dev/null 2>&1; adb logcat -c
  adb logcat -v threadtime > $OUT/logcat-live.txt 2>&1 & LOGPID=$!
  dims; log "screen ${W}x${H}"
  sleep 25; a input keyevent KEYCODE_HOME; sleep 2; dismiss
}
# Чиста инсталация на друго издание: $1 — apk, $2 — етикет, $3 — файл с описанието
inst() {
  a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1
  timeout 180 adb install -r $1 || { log "install $1 failed"; return 1; }
  a cmd locale set-app-locales $PKG --locales bg-BG
  a pm grant $PKG android.permission.POST_NOTIFICATIONS; no_camera >/dev/null
  log "installed $2: $(cat $3 2>/dev/null) size $(stat -c %s $1); $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
}
apk_of() { case $1 in new*) echo app.apk;; base*) echo app-base.apk;; esac; }
ref_of() { case $1 in new*) echo apk-ref.txt;; base*) echo apk-base-ref.txt;; esac; }
warmup() {
  sec "W. First launch and warm-up"
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  tab news; sleep 5; tab home; sleep 5
}
want_add() {  # книга по връзка → „Искам да прочета“ (ако още не е)
  link "$1" >/dev/null; sleep 3; dump
  has "$RX_BACK" && [ -z "$(xy text "$L_WANT")" ] && { a input swipe $((W/2)) $((H*2/3)) $((W/2)) $((H/3)) 300; sleep 1.5; dump; }
  local r=no
  if [ -n "$(xy text "$L_WANT")" ] && xy --state text "$L_WANT" | grep -q 'checked=false'; then tap text "$L_WANT" >/dev/null; r=added; sleep 1.5; fi
  has "$RX_BACK" && back 1.5
  [ $r = added ]
}
fill_want_inv() {
  local n=0 inv
  for inv in $(seq 1 60); do
    want_add "$SITE/elektronen-katalog?inv=$inv" && n=$((n+1))
    [ $n -ge 16 ] && break
  done
  log "Want to read: added $n books by inventory-number links (inv 1..$inv)"
}

# ---------------------------------------------------------------- многопръстово докосване (sendevent)
# Сензорният екран на емулатора (протокол B). Координатите се мащабират към обхвата на устройството.
mt_init() {
  [ -n "${TOUCH_DEV:-}" ] && return 0
  a getevent -pl > $OUT/getevent.txt 2>&1
  TOUCH_DEV=$(awk '/^add device/{d=$4} /ABS_MT_POSITION_X/{print d; exit}' $OUT/getevent.txt)
  MTX=$(grep -m1 'ABS_MT_POSITION_X' $OUT/getevent.txt | grep -oE 'max [0-9]+' | grep -oE '[0-9]+')
  MTY=$(grep -m1 'ABS_MT_POSITION_Y' $OUT/getevent.txt | grep -oE 'max [0-9]+' | grep -oE '[0-9]+')
  MT_PRESS=$(grep -c 'ABS_MT_PRESSURE' $OUT/getevent.txt)
  log "touch device: ${TOUCH_DEV:-NONE} x-max=${MTX:-?} y-max=${MTY:-?} pressure=$MT_PRESS"
  [ -n "$TOUCH_DEV" ] && [ -n "$MTX" ] && [ -n "$MTY" ]
}
# Физическите пиксели (без завъртане) → единиците на устройството.
mtx() { echo $(( $1 * MTX / (W - 1) )); }
mty() { echo $(( $1 * MTY / (H - 1) )); }
mt_run() {  # изпълнява $OUT/mt.sh на устройството (един shell, без закъснението на `input`)
  timeout 20 adb push $OUT/mt.sh /data/local/tmp/mt.sh >/dev/null 2>&1
  T=60 a sh /data/local/tmp/mt.sh
}
# Разтваряне с два пръста около (cx, cy): от разстояние d0 до d1 (px, хоризонтално), на n стъпки.
pinch() {
  local cx=$1 cy=$2 d0=$3 d1=$4 n=${5:-14} i d D=$TOUCH_DEV
  {
    echo "s() { sendevent $D \$1 \$2 \$3; }"
    echo "s 3 47 0; s 3 57 201; s 3 53 $(mtx $((cx - d0/2))); s 3 54 $(mty $cy)"; [ "$MT_PRESS" -gt 0 ] && echo "s 3 58 50"
    echo "s 3 47 1; s 3 57 202; s 3 53 $(mtx $((cx + d0/2))); s 3 54 $(mty $cy)"; [ "$MT_PRESS" -gt 0 ] && echo "s 3 58 50"
    echo "s 1 330 1; s 0 0 0"
    for i in $(seq 1 $n); do
      d=$(( d0 + (d1 - d0) * i / n ))
      echo "s 3 47 0; s 3 53 $(mtx $((cx - d/2))); s 3 47 1; s 3 53 $(mtx $((cx + d/2))); s 0 0 0; sleep 0.03"
    done
    echo "s 3 47 0; s 3 57 -1; s 3 47 1; s 3 57 -1; s 1 330 0; s 0 0 0"
  } > $OUT/mt.sh
  mt_run
}
# Двойно докосване (за 2× в прегледа) — две докосвания през ~120 ms.
dtap() {
  local x=$1 y=$2 D=$TOUCH_DEV
  {
    echo "s() { sendevent $D \$1 \$2 \$3; }"
    for k in 1 2; do
      echo "s 3 47 0; s 3 57 30$k; s 3 53 $(mtx $x); s 3 54 $(mty $y)"; [ "$MT_PRESS" -gt 0 ] && echo "s 3 58 50"
      echo "s 1 330 1; s 0 0 0; sleep 0.05; s 3 57 -1; s 1 330 0; s 0 0 0; sleep 0.12"
    done
  } > $OUT/mt.sh
  mt_run
}

# ================================================================= ABSTART
ab_round() {  # $1 — етикет (new-1, base-2…)
  local L=$1 K=${1%-*} apk ref i
  apk=$(apk_of $L); ref=$(ref_of $L)
  sec "AB $L: fresh install, first launch, catalogue, 10 cold + 10 warm starts"
  inst $apk $L $ref || return
  a input keyevent KEYCODE_HOME; sleep 2
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog
  if waitfor "$RX_RESULTS" 150; then log "$L FIRST-INSTALL catalogue results after ${WAITED} ms"; echo "TotalTime: $WAITED" >> $OUT/cat-first-$K.txt; else log "$L FIRST-INSTALL: NO results"; fi
  for i in 1 2 3; do
    a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 3; dismiss; tab catalog
    if waitfor "$RX_RESULTS" 60; then log "$L INDEX-READY #$i catalogue results after ${WAITED} ms"; echo "TotalTime: $WAITED" >> $OUT/cat-ready-$K.txt; else log "$L INDEX-READY #$i: NO results"; fi
  done
  : > $OUT/start-$L-cold.txt
  for i in $(seq 1 10); do
    a am force-stop $PKG; sleep 3
    launch | tee -a $OUT/start-$L-cold.txt | tr '\n' ' '; echo
    sleep 3
  done
  python3 $H_/parse.py starts $OUT/start-$L-cold.txt "cold $L" | tee -a $SUM
  : > $OUT/start-$L-warm.txt
  for i in $(seq 1 10); do
    a input keyevent KEYCODE_HOME; sleep 3
    launch | tee -a $OUT/start-$L-warm.txt | tr '\n' ' '; echo
    sleep 2
  done
  python3 $H_/parse.py starts $OUT/start-$L-warm.txt "warm $L" | tee -a $SUM
  dismiss; shot ab-$L
}
part_abstart() {
  local order k
  if [ "$PART" = abstart-2 ]; then order="base-1 new-1 new-2 base-2"; else order="new-1 base-1 base-2 new-2"; fi
  for k in $order; do ab_round $k; done
  sec "AB totals"
  for k in new base; do
    cat $OUT/start-$k-1-cold.txt $OUT/start-$k-2-cold.txt > $OUT/start-$k-cold-all.txt 2>/dev/null
    cat $OUT/start-$k-1-warm.txt $OUT/start-$k-2-warm.txt > $OUT/start-$k-warm-all.txt 2>/dev/null
  done
  python3 $H_/parse.py starts $OUT/start-new-cold-all.txt "cold NEW 1.2.27" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-base-cold-all.txt "cold BASE 1.2.26" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-new-warm-all.txt "warm NEW 1.2.27" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-base-warm-all.txt "warm BASE 1.2.26" | tee -a $SUM
  for k in new base; do
    python3 $H_/parse.py starts $OUT/cat-first-$k.txt "catalogue first-install $k" | tee -a $SUM
    python3 $H_/parse.py starts $OUT/cat-ready-$k.txt "catalogue index-ready $k" | tee -a $SUM
  done
}

# ================================================================= JANK
jank_swipes() { local i; gfx_reset; for i in $(seq 1 10); do sw_up 70; sleep 0.25; done; for i in $(seq 1 10); do sw_down 70; sleep 0.25; done; gfx_save $1; shot jank/$1; }
jank_app() {  # $1 — new | base
  local K=$1 i p p2 pages=0 mis=0 scr
  sec "J-$K. Install $K, warm-up, 16 books in Want to read"
  inst $(apk_of $K) $K $(ref_of $K) || return
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$K first-install catalogue results after ${WAITED} ms" || log "$K first-install: NO catalogue results"
  fill_want_inv
  sec "J-$K. Jank at 540x1200 (animations ON)"
  anims 1; a wm size 540x1200; a wm density 210; sleep 3; dims; log "jank screen ${W}x${H}"
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
  gfx_save $K-catalog; log "$K-catalog: 30 swipes, 'Покажи още' pages: $pages, mis-taps recovered: $mis"; shot jank/$K-catalog-end
  for scr in want news calendar; do
    case $scr in
      news) link "$SITE/news"; sleep 5; segstate | grep -q 'News\[checked=true' || segment news;;
      calendar) link "$EVENTS_URL"; sleep 5; dump && has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък'; sleep 2; };;
      want) open_want_list; dump; log "$K want list: $(grep -c 'content-desc="\(More actions\|Още действия\)"' $OUT/ui.xml) rows visible";;
    esac
    dismiss
    log "jank $K-$scr screen: $(show 8 | cut -c1-200)"
    jank_swipes $K-$scr
  done
  # Галерията: албумите, снимките в албум, плъзгане в прегледа
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 40; then
    log "$K gallery albums shown after ${WAITED} ms: $(grep -oE "$RX_GALLERY_LOADED" $OUT/ui.xml | wc -l) on screen"
    jank_swipes $K-gallery
    for i in 1 2 3 4; do sw_down 100; sleep 0.4; done
    if open_album 1 && wait_cells 20; then
      log "$K album: $(texts 3 | cut -c1-120); photos on screen $NCELLS"
      jank_swipes $K-album
      for i in 1 2 3 4; do sw_down 100; sleep 0.4; done
      if open_photo 1; then
        sleep 2; log "$K viewer: $(viewer_page)"
        gfx_reset
        for i in $(seq 1 20); do sw_left 150; sleep 0.6; done
        gfx_save $K-viewer; log "$K viewer after 20 swipes: $(viewer_page)"; shot jank/$K-viewer-end
        back 1.5
      else log "$K viewer: no photo to open"; debug_ui viewer; fi
      back 1.5
    else log "$K album: photos not shown"; debug_ui album; fi
    back 1.5
  else log "$K gallery: albums not shown"; debug_ui gallery; fi
  link "$SITE/"; sleep 5; dismiss; log "jank $K-home screen: $(show 8 | cut -c1-200)"
  jank_swipes $K-home
  a wm size reset; a wm density reset; sleep 3; dims; anims 0
  mem $K-jank-end
}
part_jank() {
  if [ "$PART" = jank-2 ]; then jank_app base; jank_app new; else jank_app new; jank_app base; fi
}

# ================================================================= MEMORY
gallery_cycle() {  # от „Още“: галерия → албум → снимка → една снимка нататък → обратно
  local p ok=0
  dump; p=$(xy text "$L_GALLERY")
  [ -z "$p" ] && { sw_up 300; sleep 1; dump; p=$(xy text "$L_GALLERY"); }
  [ -n "$p" ] || return 1
  a input tap $p; sleep 1.5
  waitfor "$RX_GALLERY_LOADED" 15 || { back 1; return 1; }
  open_album 1 || { back 1; return 1; }
  if open_photo 1; then sw_left 150; sleep 1.2; ok=1; back 1; fi
  back 1; back 1
  [ $ok = 1 ]
}
part_mem() {
  local K=new c t gok=0
  [ "$PART" = mem-base ] && { K=base; inst app-base.apk base apk-base-ref.txt; }
  warmup
  sec "M. Memory over 30 cycles (all tabs + a book + the gallery viewer), $K"
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
    [ $c = 1 ] && { show 12; has "$RX_BACK" && log "cycle 1: book screen opened" || log "cycle 1: book screen NOT detected"; shot cycles-book; }
    back 0.6
    a input tap ${TXY[more]}; sleep 1.5
    if gallery_cycle; then gok=$((gok+1)); else [ $c -le 2 ] && debug_ui gallery-cycle-$c; ready >/dev/null; fi
    [ $c = 1 ] && shot cycles-after-gallery
    case $c in 10|20|30) log "cycles done: $c, gallery viewer reached in $gok"; mem c$c;; esac
  done
  log "pid after cycles: $(a pidof $PKG | tr -d '\r'); gallery viewer reached in $gok/30 cycles"
}

# ================================================================= MONKEY
part_monkey() {
  warmup
  sec "K. Monkey 5000 events (seed 4321, CAMERA revoked)"
  no_camera; log "camera permission before monkey: $CAMSTATE"
  log "statusbar disable: $(a cmd statusbar send-disable-flag statusbar-expansion notification-alerts 2>&1 | tr -d '\r' | head -2)"
  restart
  log "monkey start pid=$(a pidof $PKG | tr -d '\r')"
  a log -t PERFCHECK "monkey start"
  local t0; t0=$(now_ms)
  timeout 1500 adb shell monkey -p $PKG --pct-syskeys 0 --pct-anyevent 0 --throttle 80 -s 4321 -v 5000 > $OUT/monkey.txt 2>&1
  log "MONKEY exit=$? duration=$(( ($(now_ms) - t0) / 1000 )) s; $(grep -E 'Events injected|Monkey finished|aborted' $OUT/monkey.txt | tr '\n' ' ')"
  log "MONKEY markers: CRASH=$(grep -c 'CRASH' $OUT/monkey.txt) ANR=$(grep -c 'NOT RESPONDING\|ANR in' $OUT/monkey.txt) FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/monkey.txt)"
  grep -nE -A20 'CRASH|NOT RESPONDING|ANR in' $OUT/monkey.txt | head -60
  alive "after monkey"
  sleep 2; log "pid after monkey: '$(a pidof $PKG | tr -d '\r')'; airplane=$(a settings get global airplane_mode_on | tr -d '\r'); camera: $(a dumpsys package $PKG | grep -m1 'android.permission.CAMERA: granted' | tr -s ' ' | tr -d '\r')"
  shot monkey-end; show 12
  mem after-monkey
}

# ================================================================= MEDIA
cache_sizes() {  # само с корен: размерът на кеш папките на приложението
  log "CACHE $1: $(a "du -sk /data/data/$PKG/cache/* /data/data/$PKG/files 2>/dev/null" | tr -d '\r' | awk '{printf "%s=%sKB ", $2, $1}' | sed "s|/data/data/$PKG/||g")"
}
part_media() {
  local p i cx cy n
  warmup
  sec "G1. Gallery: albums"
  restart; mem gal-before
  open_gallery; local t0; t0=$(now_ms)
  if waitfor "$RX_GALLERY_LOADED" 40; then log "GALLERY albums after ${WAITED} ms (tap → '… снимки')"; else log "GALLERY: albums NOT shown in 40 s: $(texts 10 | cut -c1-200)"; fi
  sleep 3; shot media/01-gallery; dump
  python3 $H_/find.py $OUT/ui.xml --texts > $OUT/gallery-texts.txt
  log "GALLERY albums: $(grep -vE '^(Назад|Back|Галерия|Gallery)$' $OUT/gallery-texts.txt | head -24 | tr '\n' '|' | cut -c1-500)"
  n=$(grep -cE "$RX_GALLERY_LOADED" $OUT/ui.xml)
  for i in $(seq 1 $(( n < 6 ? n : 6 ))); do
    dump; open_album $i || break
    if wait_cells 20; then log "ALBUM $i: $(texts 2 | cut -c1-100); photos on screen $NCELLS after ${WAITED} ms"; else log "ALBUM $i: photos NOT shown: $(texts 6 | cut -c1-160)"; fi
    sleep 2; shot media/02-album-$i
    back 2
  done
  sw_down 100; sleep 1
  sec "G2. Photo viewer: 2× (double tap), beyond 2× (pinch), 20 photos"
  open_album 1 && open_photo 1
  sleep 3; dims; shot media/03-viewer-1x; log "VIEWER: $(viewer_page); fg $(fg)"
  mem gal-viewer
  cx=$((W/2)); cy=$((H/2))
  if mt_init; then
    dtap $cx $cy; sleep 2.5; shot media/04-viewer-2x-doubletap
    log "VIEWER after double tap: $(viewer_page)"
    pinch $cx $cy 300 900 16; sleep 1; shot media/05-viewer-pinch-1s
    pinch $cx $cy 300 800 16; sleep 5; shot media/06-viewer-pinch-more-5s
    log "VIEWER after 2 pinches (from 2× → expected about 2× · 3 · 2.7, capped at 5×): $(viewer_page)"
    sw_left 300; sleep 1.5; shot media/07-viewer-zoomed-pan
    log "VIEWER swipe while zoomed: $(viewer_page) (same page = the swipe panned the photo)"
    mem gal-zoomed
    dtap $cx $cy; sleep 2; shot media/08-viewer-reset
    log "VIEWER after reset double tap: $(viewer_page)"
  else log "PINCH: no multi-touch device — zoom not tested"; fi
  gfx_reset
  for i in $(seq 1 20); do sw_left 150; sleep 1; done
  gfx_save viewer-20; log "VIEWER after 20 swipes: $(viewer_page)"; shot media/09-viewer-after-20
  mem gal-after-20
  back 1.5; back 1.5; back 1.5; ready; tab home; sleep 3
  mem gal-back-home

  sec "C. Covers in the catalogue (Open Library)"
  cache_sizes before-covers
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; shot media/10-catalog-0s; sleep 3; shot media/11-catalog-3s; sleep 6; shot media/12-catalog-9s
  for i in 1 2 3; do sw_up 300; sleep 1.5; done; sleep 4; shot media/13-catalog-scrolled
  cache_sizes after-catalogue
  search_catalog 12; sleep 6; shot media/14-catalog-12
  link "$BOOK_URL"; sleep 6; shot media/15-book; log "book: $(texts 5 | cut -c1-160)"; back
  link "$SITE/elektronen-katalog?inv=$OUT_COPY_INV"; sleep 6; shot media/16-book-2; back
  cache_sizes after-books
  log "Open Library traffic in logcat: $(grep -c 'openlibrary' $OUT/logcat-live.txt)"

  sec "D. Documents: PDF download (no PDF app?)"
  log "PDF handlers: $(a cmd package query-activities --brief -a android.intent.action.VIEW -t application/pdf 2>&1 | tr -d '\r' | grep -v '^$' | tr '\n' ' ' | cut -c1-300)"
  log "browsers (https): $(a cmd package query-activities --brief -a android.intent.action.VIEW -c android.intent.category.BROWSABLE -d https://example.com 2>&1 | tr -d '\r' | grep -v '^$' | tr '\n' ' ' | cut -c1-300)"
  link "$SITE/load"; sleep 6; dump; shot media/20-documents
  log "DOCUMENTS: $(texts 14 | cut -c1-400); PDF buttons: $(grep -cE 'content-desc="(Отвори с приложение за PDF|Open with a PDF app)' $OUT/ui.xml)"
  pdf_try() {  # $1 — етикет
    local p; dump; p=$(xy content-desc 'Отвори с приложение за PDF|Open with a PDF app|PDF app')
    [ -n "$p" ] || { log "PDF $1: no 'open with a PDF app' button: $(texts 10 | cut -c1-200)"; return 1; }
    a log -t PERFCHECK "pdf $1 tap"; a input tap $p; local t0; t0=$(now_ms)
    sleep 0.4; shot media/pdf-$1-a; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg)"
    sleep 1; shot media/pdf-$1-b; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg)"
    sleep 2.5; shot media/pdf-$1-c; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg)"
    sleep 5; shot media/pdf-$1-d; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg); $(dump && texts 6 | cut -c1-200)"
    log "PDF $1 toasts in logcat: $(grep -A200 "pdf $1 tap" $OUT/logcat-live.txt | grep -iE 'toast' | grep -v PERFCHECK | head -3 | cut -c1-200 | tr '\n' ' ')"
    launch >/dev/null; sleep 3; dump; log "PDF $1 back in app: $(texts 3 | cut -c1-120)"
  }
  pdf_try online
  link "$SITE/load"; sleep 4
  offline
  pdf_try offline
  online; sleep 5
  link "$SITE/load"; sleep 4; dump
  if tap text '=Исторически публикации|=Historical publications'; then sleep 4; shot media/21-publications; log "PUBLICATIONS: $(texts 10 | cut -c1-300)"; pdf_try publication; fi
}

# ================================================================= ROTATION + PROCESS DEATH
want_book_shown() { has 'text="12 -те вятъра"'; }
rcheck() {  # $1 — екран: проверка на състоянието след завъртане / възстановяване
  dump
  case $1 in
    home) echo "tab bar $(has 'text="(Home|Начало)"' && echo yes || echo NO); search field $(has 'Търси книга|Search books' && echo yes || echo NO)";;
    catalog) echo "query '12' $(grep -q 'text="12"' $OUT/ui.xml && echo kept || echo LOST); results $(rcount)";;
    book) echo "book title $(has 'text="12 -те вятъра"' && echo kept || echo LOST); back button $(has "$RX_BACK" && echo yes || echo NO)";;
    want) echo "list title $(has 'text="(Want to read|Искам да прочета)"' && echo yes || echo NO); book $(want_book_shown && echo listed || echo MISSING)";;
    calendar) echo "$(segstate)";;
  esac
}
goto() {  # $1 — екран
  case $1 in
    home) ready; tab home; sleep 3;;
    catalog) ready; tab catalog; waitfor "$RX_RESULTS" 30; dump; grep -q 'text="12"' $OUT/ui.xml || search_catalog 12;;
    book) link "$BOOK_URL"; sleep 4;;
    want) open_want_list; sleep 1;;
    calendar) link_pkg "$EVENTS_URL"; sleep 5; cal_checked || segment calendar;;
  esac
}
rotx() {
  local name=$1 pid0 pid1 i
  pid0=$(a pidof $PKG | tr -d '\r'); log "ROT $name before: $(rcheck $name)"; shot rotation/$name-0-before
  for i in $(seq 1 6); do
    a settings put system user_rotation $((i % 2)); a wm user-rotation lock $((i % 2)) >/dev/null 2>&1; sleep 1.5
    [ $i = 5 ] && { sleep 1; dims; shot rotation/$name-1-landscape; log "ROT $name landscape: $(rcheck $name)"; }
  done
  rot 0; sleep 1
  pid1=$(a pidof $PKG | tr -d '\r'); shot rotation/$name-2-after
  log "ROTATION $name: pid $pid0 -> $pid1 ($([ "$pid0" = "$pid1" ] && echo same || echo CHANGED)); after: $(rcheck $name)"
}
pdeath() {
  local name=$1 pid
  goto $name; log "PD $name before: $(rcheck $name)"; shot procdeath/$name-1-before
  a input keyevent KEYCODE_HOME; sleep 3
  a am kill $PKG; sleep 3
  pid=$(a pidof $PKG | tr -d '\r'); [ -n "$pid" ] && { sleep 5; a am kill $PKG; sleep 3; pid=$(a pidof $PKG | tr -d '\r'); }
  if [ "${2:-}" = recents ]; then
    a input keyevent KEYCODE_APP_SWITCH; sleep 4; shot procdeath/$name-2-recents
    a input tap $((W/2)) $((H/2)); sleep 6
  else
    launch > $OUT/start-procdeath-$name.txt; sleep 5
  fi
  shot procdeath/$name-3-restored
  log "PROCDEATH $name${2:+ ($2)}: killed $([ -z "$pid" ] && echo yes || echo "NO (pid $pid)"); $(grep -oE 'LaunchState: \w+' $OUT/start-procdeath-$name.txt 2>/dev/null); restored: $(rcheck $name)"
}
part_rot() {
  warmup
  want_add "$BOOK_URL"; want_add "$SITE/elektronen-katalog?inv=1"
  sec "R1. Rotation (6 toggles each) on Home, Catalogue '12', a book, Want to read, Calendar"
  restart; gfx_reset
  local s
  for s in home catalog book want calendar; do goto $s; rotx $s; done
  gfx_save rotation
  sec "R2. Process death (HOME → am kill → launcher) on the same screens"
  for s in home catalog book want calendar; do pdeath $s; done
  sec "R3. Process death, back from Recents (Calendar, book)"
  pdeath calendar recents; pdeath book recents
}

# ================================================================= OFFLINE
otab() {  # $1 — етикет на снимката, $2 — описание; снимка и текстовете
  sleep ${3:-4}; dump; shot $1; log "$2: $(texts 14 | cut -c1-300)"
}
all_screens_offline() {  # $1 — папка за снимките
  local d=$1 p
  ready; tab home; otab $d/01-home "$d home"
  tab catalog; if waitfor "$RX_RESULTS" 30; then log "$d catalogue: results ($(rcount), ${WAITED} ms)"; else log "$d catalogue: NO results"; fi; otab $d/02-catalog "$d catalogue" 1
  link "$BOOK_URL"; otab $d/03-book "$d book"; back
  ready; tab my; otab $d/04-my "$d My"
  open_want_list; otab $d/05-want "$d Want to read" 2; back
  link_pkg "$SITE/news"; sleep 2; segstate | grep -q 'News\[checked=true' || segment news; otab $d/06-news "$d news" 2
  p=$(xy --first-clickable $((H/4)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; otab $d/07-article "$d article"; back; }
  segment calendar; otab $d/08-calendar "$d calendar" 1
  ready; tab more; otab $d/09-more "$d More" 2
  open_gallery; otab $d/10-gallery "$d gallery" 5
  open_album 1 >/dev/null && otab $d/10b-album "$d album" 3 && back
  back
  link "$SITE/kontakti"; otab $d/11-contacts "$d contacts"; back
  link "$SITE/load"; otab $d/12-documents "$d documents"; back
}
part_offline() {
  local t
  sec "O1. Online warm-up (fills the caches)"
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  want_add "$BOOK_URL"
  all_screens_offline online-warm
  sec "O2. Offline cold start with caches: all tabs"
  a am force-stop $PKG; offline
  launch | tr '\n' ' '; echo; sleep 6; dismiss
  all_screens_offline offline-cached
  sec "O3. Reconnect on Contacts (no touch), then the other screens"
  link "$SITE/kontakti"; sleep 3
  online
  for t in 10 25; do sleep $((t == 10 ? 10 : 15)); dump; log "contacts +${t}s after reconnect (no touch): $(texts 10 | cut -c1-250)"; done
  shot recovered/00-contacts-after-reconnect; back
  all_screens_offline recovered
  sec "O4. Empty cache, offline, guest: all tabs (MyHub)"
  a am force-stop $PKG; a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; no_camera >/dev/null
  a cmd locale set-app-locales $PKG --locales bg-BG
  offline
  launch >/dev/null; sleep 6; ready; dims
  tab my; sleep 4; dump; shot offline-empty/00-my-guest
  log "MY offline guest (empty cache): add-card $(has 'text="(Добави читателска карта|Add library card)"' && echo shown || echo MISSING); note $(has 'Не можем да проверим|t check whether' && echo shown || echo absent); $(texts 14 | cut -c1-300)"
  all_screens_offline offline-empty
  sec "O5. Reconnect with the empty cache: Gallery (no touch), then all tabs"
  open_gallery; sleep 3
  online; t0=$(now_ms)
  local got=-1 i
  for i in $(seq 1 60); do
    if dump && has "$RX_GALLERY_LOADED"; then got=$(( $(now_ms) - t0 )); break; fi
    [ $(( $(now_ms) - t0 )) -ge 60000 ] && break
    sleep 0.5
  done
  shot recovered-empty/00-gallery-after-reconnect
  log "GALLERY (empty cache) reconnect: albums shown after ${got} ms (-1 = not within 60 s); $(texts 8 | cut -c1-200)"
  if [ $got = -1 ] && [ -n "$(xy text '=Опитай отново|=Try again')" ]; then tap text '=Опитай отново|=Try again'; sleep 8; dump; shot recovered-empty/00b-gallery-after-retry; log "gallery after 'Опитай отново': $(texts 8 | cut -c1-200)"; fi
  back
  all_screens_offline recovered-empty
  sec "O6. My offline as guest again (after the online start)"
  a am force-stop $PKG; offline; launch >/dev/null; sleep 6; ready; tab my; sleep 4; dump; shot offline-empty/20-my-guest-2
  log "MY offline guest #2: add-card $(has 'text="(Добави читателска карта|Add library card)"' && echo shown || echo MISSING); note $(has 'Не можем да проверим|t check whether' && echo shown || echo absent); $(texts 14 | cut -c1-300)"
  online
}

# ================================================================= LOOK
matrix() {
  local v=$1 p
  sec "L $v"
  restart
  if [ "${2:-}" = land ]; then
    rot 0; sleep 1; rot 1; sleep 2
    log "landscape check: $(a dumpsys window displays | tr -d '\r' | grep -m1 -oE 'mCurrentRotation=[^ ]+|mRotation=[^ ]+') $(a dumpsys window displays | tr -d '\r' | grep -m1 -oE 'cur=[0-9]+x[0-9]+')"
  fi
  dims; log "matrix $v: ${W}x${H} density $(a wm density | tail -1 | grep -oE '[0-9]+$'), font_scale $(a settings get system font_scale | tr -d '\r'), night $(a cmd uimode night | tr -d '\r'), locale $(a cmd locale get-app-locales $PKG | grep -oE '\[[^]]*\]')"
  link "$SITE/"; sleep 5; dismiss; shot $v/01-home; sw_up 500; sleep 1.5; shot $v/01b-home-scrolled
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; sleep 3; shot $v/02-catalog
  link "$BOOK_URL"; sleep 5; shot $v/03-book; back
  link_pkg "$SITE/news"; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; sleep 1; shot $v/04-news
  dump; p=$(xy --first-clickable $((H/4)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; sleep 5; shot $v/05-article; back; }
  segment calendar; sleep 1; shot $v/06-calendar
  log "matrix $v: news title $(has 'text="(News &amp; events|Новини и събития)"' && echo OK || echo MISSING); segments $(segstate)"
  ready; tab my; sleep 3; shot $v/07-my
  open_want_list && { shot $v/08-want; back; }
  ready; tab more; sleep 3; shot $v/09-more
  open_settings && { shot $v/10-settings; back; }
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30; then sleep 3; shot $v/11-gallery
    open_album 1 && { wait_cells 15; sleep 2; shot $v/12-album; open_photo 1 && { sleep 3; shot $v/13-viewer; back 1.5; }; back 1.5; }
  fi
  back 1.5
  link "$SITE/kontakti"; sleep 5; shot $v/14-contacts; back
  link "$SITE/load"; sleep 5; shot $v/15-documents; back
  scanner; sleep 4; shot $v/16-scanner; dump; log "matrix $v scanner: $(texts 6 | cut -c1-200)"; back
  dump; log "matrix $v tab labels: $(for t in home catalog my news more; do [ -n "$(xy text "$(tablabel $t)")" ] && printf '%s ' $t; done)"
  [ "${2:-}" = land ] && rot 0
}
look_warm() {
  sec "L0. First launch and warm-up"
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  want_add "$BOOK_URL"; want_add "$SITE/elektronen-katalog?inv=1"
  link "$EVENTS_URL"; sleep 5; link "$SITE/news"; sleep 5
}
part_look() {
  look_warm
  case $PART in
    look1)
      matrix bg-light
      a cmd uimode night yes; sleep 3; matrix bg-dark
      a cmd locale set-app-locales $PKG --locales en-US; sleep 3
      matrix en-dark
      a cmd uimode night no; sleep 3; matrix en-light
      a cmd locale set-app-locales $PKG --locales bg-BG;;
    look2)
      if set_text_size '=Extra large|=Много голям'; then
        a settings put system font_scale 1.3; sleep 3; matrix font130-xlarge
        a settings put system font_scale 1.0; sleep 2; set_text_size '=Standard|=Стандартен'
      fi
      matrix landscape land;;
    look3)
      a wm size 640x1280; a wm density 320; sleep 4; matrix small-320dp
      a wm size 1600x2560; a wm density 320; sleep 4; matrix tablet-800dp
      matrix tablet-land land
      a wm size reset; a wm density reset;;
  esac
}

# ================================================================= FUNC1 (вълна 13)
qa_report() {
  local l p ys="" i out="" rows events
  for i in 1 2 3; do dump; p=$(xy text '=Контакти|=Contacts'); [ -n "$p" ] && break; sw_up 250; sleep 1.5; done
  for l in '=Сканирай|=Scan' '=Читателска карта|=Library card' '=Искам да прочета|=Want to read' '=Контакти|=Contacts'; do
    p=$(xy text "$l"); out="$out ${l#=}@[${p:-MISSING}]"; [ -n "$p" ] && ys="$ys ${p#* }"
  done
  rows=$(echo $ys | tr ' ' '\n' | sort -n | awk 'NR==1{c=1;p=$1;next} {if ($1-p>40) c++; p=$1} END{print c+0}')
  events=$(has 'text="(Събития|Events)"' && echo "PRESENT" || echo absent)
  log "QA $1:$out; rows=$rows; quick action 'Събития': $events"
}
f_home_qa() {
  sec "F1. Home quick actions; 'Искам да прочета' quick action"
  restart; link "$SITE/"; sleep 5; dismiss
  qa_report "standard text"; shot func/01-home-quick-actions
  local cy; cy=$(xy text '=Контакти|=Contacts'); cy=${cy#* }
  if [ -n "$cy" ] && tap text "$L_WANT" --miny $((cy - 80)) --maxy $((cy + 80)); then
    sleep 3; dump; shot func/02-qa-want-to-read
    log "QA 'Искам да прочета' → screen: back=$(has "$RX_BACK" && echo yes || echo NO) title=$(has 'text="(Want to read|Искам да прочета)"' && echo yes || echo NO); $(texts 8 | cut -c1-220)"
    back 2
  else log "QA 'Искам да прочета': not found next to 'Контакти'"; debug_ui qa-want; fi
}
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
f_catalog() {
  sec "F2. Catalogue '12': other copies on the shelf; notify toggle with undo"
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
        log "COPY-OUT scrolled: copies line: $(grep -oE 'text="[0-9]+ екземпляр[^"]*"' $OUT/ui.xml | head -1); shelf marks: $(grep -oE 'text="(На рафта|Зает[аи]?|On the shelf|On loan)[^"]*"' $OUT/ui.xml | head -8 | tr '\n' ' ')"
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
    log "COPY-OUT (link inv=$OUT_COPY_INV): $(grep -oE "text=\"($RX_COPY_OUT)[^\"]*\"" $OUT/ui.xml | head -1 || true); $(texts 8 | cut -c1-200)"
    back 2
  fi
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
  ready; link "$SITE/"; sleep 4; dismiss; dump
  local cy; cy=$(xy text '=Контакти|=Contacts'); cy=${cy#* }
  if [ -n "$cy" ] && tap text "$L_WANT" --miny $((cy - 80)) --maxy $((cy + 80)); then log "WANT list opened from the Home quick action"
  else log "WANT: quick action not found, via My"; open_want_list; fi
  sleep 3; dump; shot func/10-want-list
  python3 $H_/find.py $OUT/ui.xml --texts > $OUT/want-texts.txt
  log "WANT list: $(head -12 $OUT/want-texts.txt | tr '\n' '|' | cut -c1-300)"
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
      log "WANT 'Кажи ми' OFF: snackbar '$(grep -oE 'text="(Няма да получаваш[^"]*|You won’t[^"]*)"' $OUT/ui.xml | head -1)'; undo $([ -n "$(xy text "$L_UNDO")" ] && echo present || echo MISSING)"
      if tap text "$L_UNDO"; then sleep 1.5; dump; shot func/14-want-notify-undo; log "WANT 'Кажи ми' undo: bell $(has "content-desc=\"($L_NOTIFY_ON)" && echo 'back (ON restored)' || echo 'NOT back')"; fi
    fi
    sleep 2
    if menu_row; then
      tap text '=Махни от списъка|=Remove from list'; sleep 1.2; dump; shot func/15-want-removed-snackbar
      log "WANT remove: snackbar '$(grep -oE 'text="[^"]*(е махната от списъка|removed from your list)"' $OUT/ui.xml | head -1)'; undo $([ -n "$(xy text "$L_UNDO")" ] && echo present || echo MISSING)"
      if tap text "$L_UNDO"; then sleep 2; dump; shot func/16-want-after-undo; log "WANT remove undo: book $(has 'text="12 -те вятъра"' && echo 'back in list' || echo "NOT back: $(texts 8 | cut -c1-160)")"; fi
    fi
  else log "WANT: 'Още действия' not found"; debug_ui want-menu; fi
  back 2
}
f_news() {
  sec "F4. /news and /events links"
  ready; link_pkg "$SITE/news"; sleep 5; dims; log "/news (first): $(segstate)"; shot func/17-news-link
  segment calendar; log "Calendar segment chosen: $(segstate)"
  link_pkg "$SITE/news"; sleep 4; log "/news while on Calendar: $(segstate)"; shot func/19-news-link-after-calendar
  segment calendar; tab home; sleep 3
  link_pkg "$SITE/news"; sleep 4; log "/news from Home (News tab left on Calendar): $(segstate)"
  a am force-stop $PKG; sleep 2
  link_pkg "$SITE/news"; sleep 6; dismiss; log "/news cold: $(segstate)"
  link_pkg "$EVENTS_URL"; sleep 4; log "/events: $(segstate)"; shot func/21-events-link
  a am force-stop $PKG; sleep 2
  link_pkg "$EVENTS_URL"; sleep 6; dismiss; log "/events cold: $(segstate)"
  segment news; dump; local p; p=$(xy --first-clickable $((H/5)) $((H*4/5)) 150)
  if [ -n "$p" ]; then a input tap $p; sleep 5; dump; shot func/22-article
    log "article open: back=$(has "$RX_BACK" && echo yes || echo NO); $(texts 4 | cut -c1-160)"
    link_pkg "$EVENTS_URL"; sleep 4; log "/events from an article: $(segstate); back-button $(has "$RX_BACK" && echo 'VISIBLE (still on the article)' || echo none)"; shot func/23-events-from-article
  else log "article: no card found"; fi
}
f_reselect() {
  sec "F5. Reselecting the News tab on Calendar scrolls to the top"
  ready; link_pkg "$EVENTS_URL"; sleep 5; dims; dump
  has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък'; sleep 2; log "switched to list view"; }
  dump; python3 $H_/find.py $OUT/ui.xml --texts > $OUT/reselect-top.txt; shot func/24-calendar-top
  local i; for i in 1 2 3 4; do sw_up 300; sleep 1; done
  sleep 1; dump; python3 $H_/find.py $OUT/ui.xml --texts > $OUT/reselect-scrolled.txt; shot func/25-calendar-scrolled
  log "calendar scrolled: common with top: $(grep -Fxc -f $OUT/reselect-top.txt $OUT/reselect-scrolled.txt)/$(wc -l < $OUT/reselect-top.txt)"
  tab news; sleep 3; dump; python3 $H_/find.py $OUT/ui.xml --texts > $OUT/reselect-after.txt; shot func/26-calendar-after-reselect
  log "RESELECT: $(segstate); common with top: $(grep -Fxc -f $OUT/reselect-top.txt $OUT/reselect-after.txt)/$(wc -l < $OUT/reselect-top.txt)"
}
part_func1() { warmup; f_home_qa; f_catalog; f_want; f_news; f_reselect; }

# ================================================================= FUNC2
scan_open_manual() {
  ready; link "$SITE/"; sleep 4; dismiss; dump
  tap text '=Сканирай|=Scan' || { scanner; log "(scanner via shortcut)"; }
  sleep 4; dump
  if has 'package="com.(google.)?android.permissioncontroller"'; then log "SCAN: system permission dialog: $(texts 6)"; tap text "t allow|=Deny|=Не разрешавам"; sleep 2; dump; fi
  tap text '=Enter the number manually|=Въведи номера на ръка' || return 1
  sleep 2; dump
}
panel_state() { dump; echo "panel $(has 'text="(ISBN или инвентарен номер|ISBN or inventory number)"' && echo OPEN || echo closed); IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)'); scanner $(has 'text="(Въведи номера на ръка|Enter the number manually)"' && echo visible || echo gone); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"; }
f_scan() {
  sec "F6. Scanner (CAMERA revoked): manual panel, Back key"
  no_camera; log "camera permission: $CAMSTATE"
  if echo "$CAMSTATE" | grep -q 'granted=true'; then log "SCAN: SKIPPED — camera permission still granted"; return; fi
  if scan_open_manual; then
    shot func2/01-manual-panel; log "manual panel: $(panel_state)"
    local n; for n in 1 2; do back 2; log "Back key #$n: $(panel_state)"; has 'text="(ISBN или инвентарен номер|ISBN or inventory number)"' || break; done
    shot func2/02-after-back
    if tap text '=Enter the number manually|=Въведи номера на ръка'; then
      sleep 2; a input text $INV; sleep 1.5; tap text '=Search|=Търси' --last; sleep 4; dump; shot func2/03-manual-result
      log "SCAN result: $(texts 10 | cut -c1-250)"
      tap text '=Open book|=Отвори книгата' && { sleep 4; dump; log "SCAN → book: $(has "$RX_BACK" && echo opened || echo 'NOT opened')"; }
    fi
  else log "SCAN: manual entry button NOT found"; debug_ui scan; fi
}
f_scan_gesture() {
  sec "F7. Scanner manual panel with the system back GESTURE (gesture navigation; predictive back)"
  log "gesture nav: $(a cmd overlay enable-exclusive --category com.android.internal.systemui.navbar.gestural 2>&1 | tr -d '\r' | head -2)"; sleep 4
  log "navigation_mode=$(a settings get secure navigation_mode | tr -d '\r') (2 = gestures); predictive-back animations need Android 14+, this is API $SDK"
  dims
  if scan_open_manual; then
    shot func2/10-manual-panel-gesture; log "panel before: $(panel_state)"
    local y=$((H/2)) n
    for n in 1 2; do
      # Жестът „назад“ от левия ръб, на части — снимка по средата (предварителен изглед)
      a input motionevent DOWN 2 $y; a input motionevent MOVE 60 $y; a input motionevent MOVE 200 $y; a input motionevent MOVE $((W*35/100)) $y
      sleep 0.4; shot func2/11-gesture-$n-midway
      a input motionevent MOVE $((W*55/100)) $y; a input motionevent UP $((W*55/100)) $y; sleep 2
      log "back gesture #$n: $(panel_state)"; shot func2/12-gesture-$n-after
      has 'text="(ISBN или инвентарен номер|ISBN or inventory number)"' || break
    done
    # Отделно: бързото плъзгане (input swipe) от ръба
    if has 'text="(Въведи номера на ръка|Enter the number manually)"' && tap text '=Enter the number manually|=Въведи номера на ръка'; then
      sleep 2; hide_ime; log "panel reopened (IME hidden): $(panel_state)"
      a input swipe 2 $y $((W*6/10)) $y 250; sleep 2
      log "back swipe (input swipe): $(panel_state)"; shot func2/13-swipe-after
      a input swipe 2 $y $((W*6/10)) $y 250; sleep 2
      log "back swipe #2 (from the scanner): fg $(fg); $(dump && texts 4 | cut -c1-120)"
    fi
  else log "SCAN (gesture): manual entry button NOT found"; debug_ui scan-gesture; fi
  log "3-button nav: $(a cmd overlay enable-exclusive --category com.android.internal.systemui.navbar.threebutton 2>&1 | tr -d '\r' | head -1)"; sleep 3
}
f_contacts() {
  sec "F8. Contacts links (call, email, map, site, cards)"
  local l p f k=0
  for l in '=Обади се|=Call' '=Изпрати имейл|=Send email' '=Отвори карта|=Open map' '=Отвори сайта|=Open website' 'chitalishte_yavorec@abv.bg' 'facebook.com' '=Данни от chyavorec.org|=Data from chyavorec.org'; do
    link "$SITE/kontakti"; sleep 5; dims; dump
    p=$(xy text "$l")
    if [ -z "$p" ]; then sw_up 500; sleep 1.5; dump; p=$(xy text "$l"); fi
    [ -z "$p" ] && { log "CONTACTS link '${l#=}': not on screen"; continue; }
    k=$((k+1)); a input tap $p; sleep 4; f=$(fg); shot func2/contacts-link-$k
    log "CONTACTS link '${l#=}' → fg $f; $(dump && texts 4 | cut -c1-150)"
    a input keyevent KEYCODE_BACK; sleep 1.5; [ "$(fg | cut -d/ -f1)" != $PKG ] && { a input keyevent KEYCODE_BACK; sleep 1.5; }
    [ "$(fg | cut -d/ -f1)" != $PKG ] && { launch >/dev/null; sleep 2; }
  done
  link "$SITE/kontakti"; sleep 5; dump
  log "CONTACTS clickables past the right edge: $(grep -oE 'clickable="true"[^>]*bounds="\[[0-9]+,[0-9]+\]\[[0-9]+,[0-9]+\]"' $OUT/ui.xml | grep -oE '\]\[[0-9]+' | tr -d '][' | awk -v w=$W '$1>w' | wc -l)"
}
part_func2() { warmup; f_scan; f_contacts; f_scan_gesture; }

# ================================================================= UPDATE („Какво ново“)
whatsnew_check() {  # $1 — етикет
  local i got=no
  for i in 1 2 3 4 5 6 7 8; do dump; has "$RX_WHATSNEW" && { got=yes; break; }; sleep 1.5; done
  shot update/$1
  if [ $got = yes ]; then
    log "WHATS-NEW $1: SHOWN: $(grep -oE "text=\"$RX_WHATSNEW[^\"]*\"" $OUT/ui.xml | head -1); $(texts 6 | cut -c1-400)"
  else log "WHATS-NEW $1: not shown; screen: $(texts 8 | cut -c1-200)"; fi
  [ $got = yes ]
}
ver() { a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' '; }
part_update() {
  sec "U1. v1.2.26 (update source build) → adb install -r v1.2.27"
  inst app-base-upd.apk base-upd apk-base-upd-ref.txt || return
  launch >/dev/null; sleep 8; dump; shot update/00-base-first; log "base first launch: $(texts 8 | cut -c1-200)"
  dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 120 && log "base catalogue after ${WAITED} ms"
  want_add "$BOOK_URL" && log "base: book $INV added to Want to read"
  a am force-stop $PKG
  timeout 180 adb install -r app.apk 2>&1 | tail -1
  log "after adb install -r: $(ver)"
  launch >/dev/null; sleep 4
  whatsnew_check 01-after-adb-install || log "(expected by design: the sheet only follows an in-app update, which saves the notes before installing)"
  dismiss; ready; open_want_list; dump; shot update/02-want-after-adb-update
  log "after adb update: Want to read keeps $INV: $(has 'text="12 -те вятъра"' && echo yes || echo NO)"

  sec "U2. v1.2.26 → v1.2.27 through the in-app updater (staged update.json, same throwaway key)"
  inst app-base-upd.apk base-upd apk-base-upd-ref.txt || return
  log "manifest: $(curl -sS --max-time 20 https://raw.githubusercontent.com/${GITHUB_REPOSITORY:-plam4o4o-source/ch-app}/emulator-results/v127b/update/update.json | python3 -c 'import json,sys; d=json.load(sys.stdin); print(d["versionCode"], d["versionName"], d["sha256"][:12], d["size"])' 2>&1)"
  log "install-unknown-apps: $(a appops set $PKG REQUEST_INSTALL_PACKAGES allow 2>&1; a appops get $PKG REQUEST_INSTALL_PACKAGES | tr -d '\r')"
  launch >/dev/null
  local i p st=none t0; t0=$(now_ms)
  for i in $(seq 1 40); do
    sleep 2; dump
    if has 'text="(Нова версия|New version)'; then st=dialog; break; fi
    has 'text="(Пропусни|Skip)"' && tap text '=Пропусни|=Skip'
    has 'text="(Не сега|Not now)"' && tap text '=Не сега|=Not now'
  done
  shot update/10-update-dialog
  log "UPDATE dialog: $st after $(( $(now_ms) - t0 )) ms: $(texts 10 | cut -c1-400)"
  if [ $st = dialog ]; then
    # Изтеглено вече (Wi-Fi) → „Инсталирай“; иначе „Изтегли и инсталирай“
    for i in $(seq 1 30); do
      dump
      p=$(xy text '=Инсталирай|=Install'); [ -z "$p" ] && p=$(xy text '=Изтегли и инсталирай|=Download and install')
      [ -n "$p" ] && { log "UPDATE: tap '$(xy --texts 2>/dev/null | grep -m1 -E '^(Инсталирай|Изтегли и инсталирай|Install|Download and install)$')'"; a input tap $p; break; }
      sleep 2
    done
    for i in $(seq 1 45); do
      sleep 2; dump
      if has 'package="com.(google.)?android.packageinstaller"'; then
        shot update/11-system-confirm; log "UPDATE system dialog: $(texts 6 | cut -c1-200)"
        tap text '=Update|=Обнови|=Install|=Инсталирай|=OK' && { sleep 3; break; }
      fi
      [ "$(a dumpsys package $PKG | grep -m1 -oE 'versionCode=[0-9]+')" = "versionCode=1484479" ] && break
      [ $((i % 10)) = 0 ] && log "UPDATE waiting: $(texts 6 | cut -c1-160)"
    done
    for i in $(seq 1 30); do [ "$(a dumpsys package $PKG | grep -m1 -oE 'versionCode=[0-9]+')" = "versionCode=1484479" ] && break; sleep 2; done
    log "UPDATE after install: $(ver); fg $(fg)"
    sleep 3; shot update/12-after-install; dump; log "screen after install: $(texts 8 | cut -c1-200)"
    if ! has "$RX_WHATSNEW"; then launch >/dev/null; sleep 3; fi
    if whatsnew_check 13-whats-new; then
      tap text '=Добре|=OK'; sleep 2
      a am force-stop $PKG; launch >/dev/null; sleep 5
      whatsnew_check 14-second-launch && log "WHATS-NEW: shown AGAIN on the second launch (should be once)" || log "WHATS-NEW: not shown on the second launch (once, as designed)"
    fi
    dismiss; ready; open_want_list; dump; shot update/15-want-after-in-app-update
    log "after in-app update: Want to read keeps $INV: $(has 'text="12 -те вятъра"' && echo yes || echo NO)"
  fi
  log "app notifications: $(a dumpsys notification --noredact 2>/dev/null | tr -d '\r' | grep -A3 "pkg=$PKG" | grep -oE 'android.title=[^,]*' | head -4 | tr '\n' ' ')"
}

# ================================================================= main
setup
case $PART in
  abstart-*) part_abstart;;
  jank-*) part_jank;;
  mem|mem-base) part_mem;;
  monkey) part_monkey;;
  media) part_media;;
  rot) part_rot;;
  offline) part_offline;;
  look*) part_look;;
  func1) part_func1;;
  func2) part_func2;;
  update) part_update;;
  *) log "unknown part $PART";;
esac
alive "end"
echo "## COMPLETE" >> $SUM
finish
FAIL=0
grep -q "FATAL EXCEPTION" $OUT/logcat-live.txt && grep -A3 "FATAL EXCEPTION" $OUT/logcat-live.txt | grep -q "$PKG" && FAIL=1
[ $FAIL = 0 ] && echo "RESULT: OK" || echo "RESULT: crash in app"
exit 0
