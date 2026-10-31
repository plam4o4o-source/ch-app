#!/usr/bin/env bash
# Пълна проверка на издадената v1.2.28 (етикет 6e2c640) на емулатор (ВРЕМЕННО, клон
# debug/full-test-4, резултати в emulator-results/v128b), със сравнение с v1.2.27 (8dc63c1).
# Приложението е на български (bg-BG), освен където частта казва друго.
#   abstart-1..4 — студен и топъл старт A/B, редуване на един емулатор; първият блок не се брои;
#                  каталогът — при първа инсталация и с готов индекс
#   jank-1/2     — jank при 540x1200: каталог, „Начало“ (SearchBar), новини, календар, „Искам да
#                  прочета“, галерия (мрежата на албума) и прегледът; и двете издания
#   homey        — „Начало“: screenrecord на първите 5 s след студен старт, y на всеки раздел
#   mem, mem-base — памет при 30 цикъла (разделите, книга, прегледът на галерията, разгънатото
#                  търсене), PSS след trim, наклон (теч)
#   monkey, monkey-base — 6000 събития, seed 2468, без камера
#   media        — липсващи снимки и „Опитай отново“, увеличение над 2×, PDF с име, PDF без връзка
#                  → браузър, корици
#   widget       — уиджетът: добавяне, размерите, светъл/тъмен, без вход, докосвания
#   widgetlang   — езикът на надписите: система BG + приложение EN и обратно
#   widgetupd    — уиджет от v1.2.27 на началния екран → adb install -r v1.2.28
#   func         — търсенето на „Начало“ (разгъване, резултат, назад, жест, смърт на процеса),
#                  входът и Credential Manager (има ли мениджър на пароли / Google Play services)
#   search34     — търсенето на API 34: предиктивното „назад“ по средата на жеста
#   rot, offline, look1/2/3 — завъртане и смърт на процеса, без връзка, матрица от изгледи
#   a11y         — uiautomator: повтарящи се етикети, липсващи етикети, малки цели за докосване
#   update       — „Какво ново“: v1.2.27 → вграденото обновяване към тестовото v1.2.28
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
L_SUBSCRIBE='Subscribe to the calendar|Абонирай се за календара'
L_WANT='=Want to read|=Искам да прочета'
L_NOTIFY='Notify me when|Кажи ми, когато е на рафта'
L_NOTIFY_ON='We’ll notify you|Ще ти кажем, когато е на рафта'
L_ASK='Ask the library|Попитай библиотеката'
L_UNDO='=Undo|=Отмени'
L_MORE_ACTIONS='=More actions|=Още действия'
L_GALLERY='=Gallery|=Галерия'
RX_COPY_OUT='[0-9]+ more on the shelf|Още [0-9]+ на рафта'
RX_GALLERY_LOADED='text="[0-9]+ (photos?|снимк[аи])"'
RX_WHATSNEW='(What’s new in version|Какво ново във версия)'

NEW_V=$(cut -d' ' -f1 apk-ref.txt 2>/dev/null); BASE_V=$(cut -d' ' -f1 apk-base-ref.txt 2>/dev/null)
# ---------------------------------------------------------------- помощни
a() { timeout ${T:-40} adb shell "$@"; }
log() { echo "$*" | tee -a $SUM; }
sec() { echo; echo "################ $* ################"; echo "## $*" >> $SUM; alive "$*"; a log -t PERFCHECK "'section: $(echo "$*" | tr -d "'()")'"; }
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
    home) echo '=Home|=Начало';; catalog) echo '=Catalogue|=Каталог';; my) echo '=My library|=Моето';;
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
  case $PART in homey|func|search34|media|widgetlang) ROOT=1;; *) ROOT=0;; esac
  if [ $ROOT = 1 ]; then timeout 30 adb root 2>&1 | head -2; sleep 4; timeout 60 adb wait-for-device; fi
  log "part $PART; APK: $(cat apk-ref.txt 2>/dev/null) size $(stat -c %s app.apk) bytes"
  log "base APK: $(cat apk-base-ref.txt 2>/dev/null) size $(stat -c %s app-base.apk 2>/dev/null) bytes"
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
apk_of() { case $1 in new*) echo app.apk;; base-upd*) echo app-base-upd.apk;; base*) echo app-base.apk;; esac; }
ref_of() { case $1 in new*) echo apk-ref.txt;; base-upd*) echo apk-base-upd-ref.txt;; base*) echo apk-base-ref.txt;; esac; }
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

mt_init() {
  [ -n "${TOUCH_DEV:-}" ] && return 0
  a getevent -pl 2>&1 | tr -d '\r' > $OUT/getevent.txt
  # Основният дисплей е „virtio_input_multi_touch_1“ (останалите са за допълнителни дисплеи).
  TOUCH_DEV=$(awk '/^add device/{d=$4} /name:/{ if ($2 == "\"virtio_input_multi_touch_1\"") { print d; exit } }' $OUT/getevent.txt)
  [ -z "$TOUCH_DEV" ] && TOUCH_DEV=$(awk '/^add device/{d=$4} /ABS_MT_POSITION_X/{x=d} END{print x}' $OUT/getevent.txt)
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
ab_round() {  # $1 — етикет (new-1, base-2…; блок „-0“ е загряване и не се брои)
  local L=$1 K=${1%-*} apk ref i
  apk=$(apk_of $L); ref=$(ref_of $L)
  [ "${L##*-}" = 0 ] && K=$K-excluded
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
  case $PART in
    abstart-2|abstart-4) order="base-0 new-1 base-1 new-2 base-2";;
    *) order="new-0 base-1 new-1 base-2 new-2";;
  esac
  log "block order (the first, '-0', is a warm-up and is excluded): $order"
  for k in $order; do ab_round $k; done
  sec "AB totals (blocks 1-2 of each app; block 0 excluded)"
  for k in new base; do
    cat $OUT/start-$k-1-cold.txt $OUT/start-$k-2-cold.txt > $OUT/start-$k-cold-all.txt 2>/dev/null
    cat $OUT/start-$k-1-warm.txt $OUT/start-$k-2-warm.txt > $OUT/start-$k-warm-all.txt 2>/dev/null
  done
  python3 $H_/parse.py starts $OUT/start-new-cold-all.txt "cold NEW $NEW_V" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-base-cold-all.txt "cold BASE $BASE_V" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-new-warm-all.txt "warm NEW $NEW_V" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-base-warm-all.txt "warm BASE $BASE_V" | tee -a $SUM
  for k in new base; do
    python3 $H_/parse.py starts $OUT/cat-first-$k.txt "catalogue first-install $k" | tee -a $SUM
    python3 $H_/parse.py starts $OUT/cat-ready-$k.txt "catalogue index-ready $k" | tee -a $SUM
  done
}

# ================================================================= JANK (540x1200, анимациите включени)
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
  # „Начало“ с новия SearchBar (сгънат), после разгънат с „12“ и превъртане на резултатите
  link "$SITE/"; sleep 5; dismiss; ready; tab home; sleep 3; log "jank $K-home screen: $(show 8 | cut -c1-200)"
  jank_swipes $K-home
  for i in 1 2 3; do sw_down 60; sleep 0.3; done
  if tap text 'Търси книга|Search books'; then
    sleep 2; a input text 12; sleep 4; hide_ime; sleep 1; log "jank $K-search screen: $(show 8 | cut -c1-200)"
    jank_swipes $K-search-expanded; back 1.5; back 1.5
  fi
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
  # Галерията: албумите, снимките в албум (мрежата), плъзгане в прегледа
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 40; then
    log "$K gallery albums shown after ${WAITED} ms"
    jank_swipes $K-gallery
    for i in 1 2 3 4; do sw_down 100; sleep 0.4; done
    if open_album 1 && wait_cells 20; then
      log "$K album: $(texts 3 | cut -c1-120); photos on screen $NCELLS"
      jank_swipes $K-album-grid
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
  a wm size reset; a wm density reset; sleep 3; dims; anims 0
  mem $K-jank-end
}
part_jank() {
  if [ "$PART" = jank-2 ]; then jank_app base; jank_app new; else jank_app new; jank_app base; fi
}

# ================================================================= MEMORY (30 цикъла)
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
search_cycle() {  # „Начало“: разгъване на търсенето, „12“, назад ×2
  local p
  dump; p=$(xy text 'Търси книга|Search books')
  [ -n "$p" ] || return 1
  a input tap $p; sleep 1.5; a input text 12; sleep 2.5
  dump; local ok=1; grep -q 'text="12"' $OUT/ui.xml || ok=0
  back 1; back 1.2
  [ $ok = 1 ]
}
part_mem() {
  local K=new c t gok=0 sok=0
  [ "$PART" = mem-base ] && { K=base; inst app-base.apk base apk-base-ref.txt; }
  warmup
  sec "M. Memory over 30 cycles (all tabs + a book + the gallery viewer + the expanded search), $K"
  restart; tab home; sleep 3; dump
  declare -A TXY
  for t in home catalog my news more; do
    ref='=More|=Още'; [ $t = more ] && ref='=Home|=Начало'
    TXY[$t]=$(xy --tab "$(tablabel $t)" "$ref"); log "tab $t @ ${TXY[$t]}"
  done
  mem c00
  : > $OUT/mem-trend.txt
  for c in $(seq 1 30); do
    for t in home catalog my news more; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 0.4; done
    link "$BOOK_URL" >/dev/null; sleep 1.2
    [ $c = 1 ] && { show 12; has "$RX_BACK" && log "cycle 1: book screen opened" || log "cycle 1: book screen NOT detected"; shot cycles-book; }
    back 0.6
    a input tap ${TXY[more]}; sleep 1.5
    if gallery_cycle; then gok=$((gok+1)); else [ $c -le 2 ] && debug_ui gallery-cycle-$c; ready >/dev/null; fi
    a input tap ${TXY[home]}; sleep 1.5
    if search_cycle; then sok=$((sok+1)); else [ $c -le 2 ] && debug_ui search-cycle-$c; ready >/dev/null; fi
    [ $c = 1 ] && shot cycles-after-search
    case $c in
      5|15|25) a dumpsys meminfo $PKG > $OUT/mem-c0$c-raw.txt 2>/dev/null; [ $c -ge 10 ] && mv $OUT/mem-c0$c-raw.txt $OUT/mem-c$c-raw.txt
               local cc; cc=$(printf %02d $c); a am send-trim-memory $PKG RUNNING_CRITICAL >/dev/null 2>&1; sleep 6
               a dumpsys meminfo $PKG > $OUT/mem-c$cc.txt; python3 $H_/parse.py mem $OUT/mem-c$cc.txt "c$cc (trimmed)" | tee -a $SUM;;
      10|20|30) log "cycles done: $c, gallery viewer reached in $gok, search expanded in $sok"; mem c$c;;
    esac
  done
  log "pid after cycles: $(a pidof $PKG | tr -d '\r'); gallery viewer reached in $gok/30, expanded search with '12' in $sok/30 cycles"
  python3 - $OUT <<'EOF' | tee -a $SUM
import glob, os, re, sys
pts = []
for f in glob.glob(os.path.join(sys.argv[1], 'mem-c[0-9][0-9].txt')):
    c = int(re.search(r'mem-c(\d+)\.txt', f).group(1))
    m = re.search(r'TOTAL PSS:\s*(\d+)', open(f, errors='replace').read())
    if m: pts.append((c, int(m.group(1))))
pts.sort()
print('PSS after trim by cycle (KB): ' + ' '.join(f'c{c}={v}' for c, v in pts))
late = [p for p in pts if p[0] >= 10]
if len(late) >= 2:
    n = len(late); mx = sum(c for c, _ in late) / n; my = sum(v for _, v in late) / n
    k = sum((c - mx) * (v - my) for c, v in late) / max(1e-9, sum((c - mx) ** 2 for c, _ in late))
    print(f'LEAK TREND (cycles 10-30, least squares): {k:+.0f} KB per cycle ({k * 20 / 1024:+.1f} MB over 20 cycles)')
EOF
}

# ================================================================= MONKEY
part_monkey() {
  [ "$PART" = monkey-base ] && inst app-base.apk base apk-base-ref.txt
  warmup
  sec "K. Monkey 6000 events (seed 2468, CAMERA revoked)"
  no_camera; log "camera permission before monkey: $CAMSTATE"
  restart
  log "monkey start pid=$(a pidof $PKG | tr -d '\r')"
  a log -t PERFCHECK "monkey start"
  local t0; t0=$(now_ms)
  timeout 1800 adb shell monkey -p $PKG --pct-syskeys 0 --pct-anyevent 0 --throttle 80 -s 2468 -v 6000 > $OUT/monkey.txt 2>&1
  log "MONKEY exit=$? duration=$(( ($(now_ms) - t0) / 1000 )) s; $(grep -E 'Events injected|Monkey finished|aborted' $OUT/monkey.txt | tr '\n' ' ')"
  log "MONKEY markers: CRASH=$(grep -c 'CRASH' $OUT/monkey.txt) ANR=$(grep -c 'NOT RESPONDING\|ANR in' $OUT/monkey.txt) FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/monkey.txt)"
  grep -nE -A20 'CRASH|NOT RESPONDING|ANR in' $OUT/monkey.txt | head -60
  alive "after monkey"
  sleep 2; log "pid after monkey: '$(a pidof $PKG | tr -d '\r')'; airplane=$(a settings get global airplane_mode_on | tr -d '\r'); camera: $(a dumpsys package $PKG | grep -m1 'android.permission.CAMERA: granted' | tr -s ' ' | tr -d '\r')"
  online
  shot monkey-end; show 12
  mem after-monkey
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
  sw_down 500; sleep 1; if tap text 'Търси книга|Search books' >/dev/null; then sleep 2; a input text 12; sleep 4; hide_ime; sleep 1; shot $v/01c-search-expanded; back 1.5; back 1.5; fi
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; sleep 3; shot $v/02-catalog
  link "$BOOK_URL"; sleep 5; shot $v/03-book; back
  link_pkg "$SITE/news"; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; sleep 1; shot $v/04-news
  dump; p=$(xy --first-clickable $((H/4)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; sleep 5; shot $v/05-article; back; }
  segment calendar; sleep 1; shot $v/06-calendar
  log "matrix $v: news title $(has 'text="(News and events|Новини и събития)"' && echo OK || echo MISSING); segments $(segstate)"
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
  ready; tab my; sleep 2; tap text '=Вход|=Sign in' >/dev/null && { sleep 4; shot $v/17-login; back 2; }
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

# ================================================================= СКЕНЕР (ръчно въвеждане, без камера)
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

# ================================================================= ОБЩИ (вълна 14)
sw_right() { a input swipe $((W*15/100)) $((H/2)) $((W*85/100)) $((H/2)) ${1:-200}; }
lpress() { a input swipe $1 $2 $1 $2 ${3:-1500}; }   # задържане (дълго докосване)
go_home() { a input keyevent KEYCODE_HOME; sleep ${1:-2}; }
launcher() { a cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME 2>/dev/null | tr -d '\r' | tail -1 | cut -d/ -f1; }
# Плъзгане в един shell на устройството: DOWN, задържане, MOVE на стъпки, задържане, UP.
mdrag() {  # x1 y1 x2 y2 [задържане в началото, s]
  local x1=$1 y1=$2 x2=$3 y2=$4 hold=${5:-1.3} i
  {
    echo "input motionevent DOWN $x1 $y1"
    echo "sleep $hold"
    for i in 1 2 3 4 5 6 7 8 9 10 11 12; do echo "input motionevent MOVE $(( x1 + (x2 - x1) * i / 12 )) $(( y1 + (y2 - y1) * i / 12 ))"; done
    echo "sleep 0.8"; echo "input motionevent MOVE $((x2 + 4)) $((y2 + 4))"; echo "sleep 0.8"
    echo "input motionevent MOVE $x2 $y2"; echo "sleep 0.5"
    echo "input motionevent UP $x2 $y2"
  } > $OUT/drag.sh
  timeout 20 adb push $OUT/drag.sh /data/local/tmp/drag.sh >/dev/null 2>&1
  T=90 a sh /data/local/tmp/drag.sh
}
# Жестът „назад“ от левия ръб (навигация с жестове), със снимка по средата
bgesture() {  # $1 — снимка по средата
  local y=$((H/2))
  a input motionevent DOWN 2 $y; a input motionevent MOVE 60 $y; a input motionevent MOVE 200 $y; a input motionevent MOVE $((W*35/100)) $y
  sleep 0.4; shot $1
  a input motionevent MOVE $((W*55/100)) $y; a input motionevent UP $((W*55/100)) $y; sleep 2.5
}
nav_gestures() { log "gesture nav: $(a cmd overlay enable-exclusive --category com.android.internal.systemui.navbar.gestural 2>&1 | tr -d '\r' | head -2) navigation_mode=$(a settings get secure navigation_mode | tr -d '\r')"; sleep 4; dims; }
nav_buttons() { a cmd overlay enable-exclusive --category com.android.internal.systemui.navbar.threebutton >/dev/null 2>&1; sleep 3; dims; }
# Каналите за известия на приложението: групите и групата на всеки канал
channels() {  # $1 — етикет
  a dumpsys notification --noredact 2>/dev/null | tr -d '\r' > $OUT/notif-$1.txt
  awk -v p="AppSettings: $PKG" 'index($0, p) {f=1; print; next} f && /AppSettings:/ {f=0} f' $OUT/notif-$1.txt > $OUT/channels-$1.txt
  python3 - $OUT/channels-$1.txt <<'EOF'
import re, sys
t = open(sys.argv[1], encoding='utf-8', errors='replace').read()
groups = dict(re.findall(r"NotificationChannelGroup\{mId='([^']*)', mName=([^,]*)", t))
ch = {}
for l in t.splitlines():
    m = re.search(r"NotificationChannel\{mId='([^']*)', mName=([^,]*)", l)
    if m:
        g = re.search(r"mGroup='?([^',}]*)", l)
        ch[m.group(1)] = (m.group(2), g.group(1) if g else '?')
print('groups: ' + ('; '.join(f'{k}={v}' for k, v in groups.items()) or 'NONE'))
print('channels: ' + ('; '.join(f'{k}({v[0]})->{v[1]}' for k, v in ch.items()) or 'NONE'))
EOF
}
notif_settings_shots() {  # $1 — префикс на снимките
  a am start -a android.settings.APP_NOTIFICATION_SETTINGS --es android.provider.extra.APP_PACKAGE $PKG >/dev/null 2>&1
  sleep 4; dims; dump; shot $1-a; log "SYSTEM notification settings: fg $(fg); $(texts 24 | cut -c1-500)"
  sw_up 500; sleep 2; dump; shot $1-b; log "SYSTEM notification settings (scrolled): $(texts 24 | cut -c1-500)"
  sw_up 500; sleep 2; shot $1-c
  back 1; back 1; launch >/dev/null; sleep 2
}

# ================================================================= УИДЖЕТ (RemoteViews)
LABEL_W='Chitalishte Yavorets|Читалище Яворец'
wcount() {  # $1 — етикет; брой копия на уиджета ни в хостовете (dumpsys appwidget → файл)
  a dumpsys appwidget 2>/dev/null | tr -d '\r' > $OUT/appwidget-$1.txt
  awk '/^ *\[[0-9]+\] id=/{id=$2} /provider=.*org\.chyavorec\.app/{ if (id != "") { print id; id="" } }' $OUT/appwidget-$1.txt | sort -u | tr '\n' ' ' > $OUT/wids.txt
  wc -w < $OUT/wids.txt
}
wids() { cat $OUT/wids.txt 2>/dev/null; }
wopts() { grep -A12 "$(wids | awk '{print $NF}')" $OUT/appwidget-$1.txt 2>/dev/null | grep -oE 'appWidget(Min|Max)(Width|Height)=[0-9]+' | tr '\n' ' '; }
wstate() {  # уиджетът на началния екран (след dump)
  dump || { echo "(ui dump failed)"; return; }
  echo "views from $PKG: $(grep -c "resource-id=\"$PKG:id/" $OUT/ui.xml) [ids: $(grep -oE "resource-id=\"$PKG:id/[a-zA-Z0-9_]+\"" $OUT/ui.xml | sed -E 's/.*:id\///; s/"//' | sort -u | tr '\n' ' ' | cut -c1-200)]; bounds [$(xy --widget-bounds $PKG)]; texts: $(python3 $H_/find.py $OUT/ui.xml --widget-texts $PKG | tr '\n' '|' | cut -c1-300); error text: $(grep -oE 'text="[^"]*(load widget|loading widget|add widget|Unable to load|не може да се зареди)[^"]*"' $OUT/ui.xml | head -1)"
}
# Отваря избора на уиджети от началния екран; WPX/WPY — визуализацията на уиджета ни.
widget_picker() {  # $1 — етикет; WPX/WPY — визуализацията на уиджета ни
  local p i hp hy
  WPX=""; WPY=""
  go_home 2; dims; go_home 2; dump; shot widget/$1-00-home
  lpress $((W/2)) $((H*45/100)) 1500; sleep 2; dump; shot widget/$1-01-longpress
  log "WIDGET $1 long-press menu: $(texts 8 | cut -c1-200)"
  tap text '=Widgets|=Приспособления|=Уиджети' || { log "WIDGET $1: no 'Widgets' in the menu"; debug_ui widgets-menu; go_home; return 1; }
  sleep 3; dims
  # Редът на приложението ни (в горната половина на списъка, за да се види разгънат)
  for i in 1 2 3 4 5 6 7 8 9 10; do
    dump; hp=$(xy text "$LABEL_W"); hy=${hp#* }
    [ -n "$hp" ] && [ "$hy" -lt $((H*45/100)) ] && break
    if [ -n "$hp" ]; then a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*35/100)) 600; else sw_up 500; fi
    sleep 1.5
  done
  shot widget/$1-03-list
  [ -n "$hp" ] || { log "WIDGET $1: our app not in the widget list"; debug_ui picker; go_home; return 1; }
  hy=${hp#* }
  p=$(xy text ' × ' --miny $hy)
  if [ -z "$p" ]; then a input tap $hp; sleep 2.5; dump; hp=$(xy text "$LABEL_W"); hy=${hp#* }; p=$(xy text ' × ' --miny ${hy:-0}); fi
  shot widget/$1-04-app
  log "WIDGET $1 picker: our row @ $hp; size label @ ${p:-none}; texts: $(python3 $H_/find.py $OUT/ui.xml --texts | grep -E 'Chitalishte|Читалище|×' | tr '\n' '|' | cut -c1-200)"
  [ -n "$p" ] || { log "WIDGET $1: our preview not found"; debug_ui picker; go_home; return 1; }
  WPX=${p% *}; WPY=$(( ${p#* } - 170 ))
}
widget_add() {  # $1 — етикет; 0 = копие на уиджета е добавено
  local before n
  before=$(wcount $1-before)
  widget_picker $1 || return 1
  a input draganddrop $WPX $WPY $((W/2)) $((H*42/100)) 3000 2>&1 | head -2
  sleep 3; dump; shot widget/$1-05-dropped; log "WIDGET $1 after drop: fg $(fg); $(texts 6 | cut -c1-160)"
  n=$(wcount $1-after)
  if [ "$n" -le "$before" ]; then
    log "WIDGET $1: draganddrop bound nothing ($n) — trying motionevent"
    widget_picker $1-retry || return 1
    mdrag $WPX $WPY $((W/2)) $((H*42/100))
    sleep 3; dump; shot widget/$1-06-dropped-retry
    n=$(wcount $1-after2)
  fi
  log "WIDGET $1: widgets of ours bound: $before -> $n (ids $(wids)); options: $(wopts $1-after2 2>/dev/null)$(wopts $1-after)"
  go_home 3
  [ "$n" -gt "$before" ]
}
# Докосване на част от уиджета: $1 — resource-id или текст (t:…), $2 — етикет, $3 — какво се очаква
wclick() {
  local p wb top bot
  go_home 3; dump
  wb=$(xy --widget-bounds $PKG); top=$(echo $wb | awk '{print $2}'); bot=$(echo $wb | awk '{print $4}')
  case $1 in
    t:*) p=$(xy text "${1#t:}" --miny ${top:-0} --maxy ${bot:-99999});;
    *) p=$(xy resource-id "$PKG:id/$1");;
  esac
  [ -n "$p" ] || { log "WIDGET click $2: '$1' not on the home screen"; return 1; }
  a input tap $p; sleep 4.5; dump; shot widget/$K-click-$2
  log "WIDGET click $2 ($1 @ $p) → fg $(fg); expected screen: $(has "$3" && echo YES || echo NO); $(texts 6 | cut -c1-170)"
}
widget_clicks() {
  wclick widget_catalog catalog "$L_SEARCH"
  wclick widget_scan scan 'Нужен е достъп до камерата|Camera access needed|Въведи номера на ръка|Enter the number manually'
  wclick widget_event event "$L_SUBSCRIBE"
  wclick "t:$LABEL_W" app 'text="(Home|Начало)"'
}
widget_clicks_text() {  # стария (Glance) уиджет — по текст
  wclick 't:=Каталог|=Catalogue' catalog "$L_SEARCH"
  wclick 't:=Сканирай|=Scan' scan 'Нужен е достъп до камерата|Camera access needed|Въведи номера на ръка|Enter the number manually'
}
# Размерът на копието: дръжките на рамката (задържане върху уиджета) — дясната и долната.
WL=""; WT=""; CW=""; CH=""
wgrid() {  # клетките от широкия 4×2 (след добавянето)
  local wb; go_home 3; dump; wb=$(xy --widget-bounds $PKG)
  [ -n "$wb" ] || return 1
  read WL WT WR WB <<< "$wb"; CW=$(( (WR - WL) / 4 )); CH=$(( (WB - WT) / 2 ))
  log "WIDGET grid from the 4x2 copy: [$wb] cell ${CW}x${CH} px"
}
wresize() {  # $1 — колони, $2 — редове, $3 — етикет
  local cols=$1 rows=$2 wb l t r b cx cy tx ty
  go_home 3; dump; wb=$(xy --widget-bounds $PKG)
  [ -n "$wb" ] || { log "RESIZE $3: widget not on screen"; return 1; }
  read l t r b <<< "$wb"; cx=$(( (l + r) / 2 )); cy=$(( (t + b) / 2 ))
  tx=$(( WL + CW * cols - CW / 5 )); ty=$(( WT + CH * rows - CH / 5 ))
  if [ $(( r - tx )) -gt 20 ] || [ $(( tx - r )) -gt $(( CW / 2 )) ]; then
    lpress $cx $cy 1200; sleep 2; dump; shot widget/$K-resize-$3-frame
    a input draganddrop $((r - 6)) $cy $tx $cy 1500 2>&1 | head -2; sleep 2; go_home 3; go_home 2
    dump; wb=$(xy --widget-bounds $PKG); read l t r b <<< "$wb"; cx=$(( (l + r) / 2 ))
  fi
  if [ $(( b - ty )) -gt 20 ] || [ $(( ty - b )) -gt $(( CH / 2 )) ]; then
    lpress $cx $(( (t + b) / 2 )) 1200; sleep 2
    a input draganddrop $cx $((b - 6)) $cx $ty 1500 2>&1 | head -2; sleep 2; go_home 3; go_home 2
  fi
  wcount $K-$3 >/dev/null
  dump; log "RESIZE $3 (${cols}x${rows}): bounds [$(xy --widget-bounds $PKG)] options $(wopts $K-$3)"
}
wsize_check() {  # $1 — етикет: съдържанието и снимки в светъл и тъмен режим
  go_home 3; log "WIDGET $1 light: $(wstate)"; shot widget/$K-$1-light
  a cmd uimode night yes; sleep 5; go_home 4; go_home 3; log "WIDGET $1 dark: $(wstate)"; shot widget/$K-$1-dark
  a cmd uimode night no; sleep 4; go_home 3
}
part_widget() {
  K=new
  warmup
  sec "W1. Add the widget from the launcher's widget picker (default 4×2 = wide)"
  log "launcher: $(launcher)"
  widget_add new || { log "WIDGET: NOT added — widget checks skipped"; return; }
  wgrid
  sec "W2. Wide 4×2, signed out: content, light and dark"
  go_home 4; wsize_check 10-wide-4x2
  dump; log "WIDGET signed-out: loans block $(has "$PKG:id/widget_loans\"" && echo SHOWN || echo hidden); event $(has "$PKG:id/widget_event_title" && echo shown || echo absent); no-events $(has "$PKG:id/widget_no_events" && echo shown || echo absent)"
  sec "W3. Wide widget: clicks"
  widget_clicks
  sec "W4. Sizes: 4×3, 3×2, 2×2, 2×3 (resize handles), light and dark each"
  wresize 4 3 4x3 && wsize_check 20-wide-tall-4x3
  wresize 3 2 3x2 && wsize_check 30-medium-3x2
  wresize 2 2 2x2 && wsize_check 40-small-2x2
  wclick widget_event small-event "$L_SUBSCRIBE"
  wclick "t:$LABEL_W" small-app 'text="(Home|Начало)"'
  wresize 2 3 2x3 && wsize_check 50-small-tall-2x3
  wresize 4 2 back-4x2 && { go_home 3; log "WIDGET back to 4x2: $(wstate)"; shot widget/$K-60-back-4x2; }
  sec "W5. After force-stop of the app process"
  a am force-stop $PKG; sleep 3; go_home 3; log "WIDGET after force-stop: $(wstate)"; shot widget/70-after-force-stop
}
# Системният език (корен): persist.sys.locale и рестарт на рамката (stop/start)
sys_locale() {  # $1 — bg-BG | en-US
  log "SYSTEM locale -> $1: $(a setprop persist.sys.locale $1 2>&1)"
  a stop; sleep 3; a start
  local i; for i in $(seq 1 60); do sleep 3; [ "$(a getprop init.svc.bootanim | tr -d '\r')" = stopped ] && a pm path android >/dev/null 2>&1 && break; done
  sleep 25; a input keyevent KEYCODE_WAKEUP; a wm dismiss-keyguard 2>/dev/null; sleep 2; go_home 3
  log "SYSTEM locale now: persist.sys.locale=$(a getprop persist.sys.locale | tr -d '\r'); config: $(a am get-config 2>/dev/null | tr -d '\r' | grep -m1 -oE '^config: [^ ]+' )"
}
app_locale_ui() {  # $1 — English | Български: смяна от „Настройки“ → „Език“ в приложението
  open_settings && settings_item '=Език|=Language' || return 1
  sleep 2; tap text "=$1" --last; sleep 5; dump
  log "APP language chosen in Settings: $1; get-app-locales: $(a cmd locale get-app-locales $PKG | tr -d '\r' | grep -oE '\[[^]]*\]'); pid $(a pidof $PKG | tr -d '\r')"
}
wlabels() {  # $1 — етикет: надписите на уиджета
  go_home 3; dump; shot widget/lang-$1
  log "WIDGET-LANG $1: $(python3 $H_/find.py $OUT/ui.xml --widget-texts $PKG | tr '\n' '|' | cut -c1-260)"
}
part_widgetlang() {
  K=lang
  warmup
  sec "WL1. System EN, app BG: add the widget"
  log "system locale: $(a getprop persist.sys.locale | tr -d '\r') / $(a am get-config 2>/dev/null | tr -d '\r' | grep -m1 -oE '^config: [^ ]+'); app: $(a cmd locale get-app-locales $PKG | tr -d '\r' | grep -oE '\[[^]]*\]')"
  widget_add lang || { log "WIDGET: NOT added — language checks skipped"; return; }
  wlabels 01-sysEN-appBG
  sec "WL2. System EN: the app switched to English in its Settings (process kept)"
  launch >/dev/null; sleep 3; ready
  app_locale_ui English
  go_home 3; wlabels 02-sysEN-appEN-right-after; sleep 15; wlabels 03-sysEN-appEN-after-15s
  launch >/dev/null; sleep 4; go_home 3; wlabels 04-sysEN-appEN-after-reopen
  a am force-stop $PKG; launch >/dev/null; sleep 6; go_home 4; wlabels 05-sysEN-appEN-after-restart
  sec "WL3. System BG (framework restart), app EN"
  sys_locale bg-BG
  wlabels 10-sysBG-appEN-after-reboot
  launch >/dev/null; sleep 6; dump; log "app screen (should be EN): $(texts 6 | cut -c1-160)"; go_home 4; wlabels 11-sysBG-appEN-after-launch
  sec "WL4. System BG, app switched to Български in Settings, then restart"
  launch >/dev/null; sleep 3; ready; app_locale_ui 'Български'
  go_home 3; wlabels 20-sysBG-appBG-right-after
  a am force-stop $PKG; launch >/dev/null; sleep 6; go_home 4; wlabels 21-sysBG-appBG-after-restart
  sec "WL5. System BG, app EN set with cmd locale (as from the system's per-app language screen), restart"
  a cmd locale set-app-locales $PKG --locales en-US; sleep 3
  go_home 3; wlabels 30-sysBG-appEN-cmd-right-after
  a am force-stop $PKG; launch >/dev/null; sleep 6; go_home 4; wlabels 31-sysBG-appEN-cmd-after-restart
  a cmd uimode night yes; sleep 5; go_home 4; wlabels 32-sysBG-appEN-dark; a cmd uimode night no
  a cmd locale set-app-locales $PKG --locales bg-BG
  sys_locale en-US
}
part_widgetupd() {
  K=upd
  sec "U0. Install v1.2.27 (Glance widget)"
  inst app-base.apk base apk-base-ref.txt || return
  warmup
  channels base-before | tee -a $SUM
  sec "U1. v1.2.27: add the widget"
  widget_add base || { log "WIDGET (base): NOT added — update check skipped"; return; }
  go_home 5; log "WIDGET v1.2.27: $(wstate)"; shot widget/upd-10-base-widget
  local ids0; ids0=$(wids)
  sec "U2. adb install -r v1.2.28 over v1.2.27 with the widget on the home screen (app not opened)"
  go_home 2
  timeout 180 adb install -r app.apk 2>&1 | tail -1
  log "after adb install -r: $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  local t0 t; t0=$(now_ms)
  for t in 3 15 45; do
    while [ $(( $(now_ms) - t0 )) -lt $((t * 1000)) ]; do sleep 0.5; done
    go_home 1; log "WIDGET +${t}s after update: $(wstate)"; shot widget/upd-2$t-after-update-${t}s
  done
  wcount upd-after >/dev/null; log "widget ids before/after the update: [$ids0] / [$(wids)]"
  sec "U3. The kept widget after the update: clicks"
  widget_clicks
  sec "U4. App opened after the update: channel groups; widget"
  restart; channels after-update | tee -a $SUM
  notif_settings_shots widget/upd-40-notif-settings
  go_home 3; log "WIDGET after the app ran: $(wstate)"; shot widget/upd-50-after-app
  a cmd uimode night yes; sleep 5; go_home 4; go_home 3; log "WIDGET dark after update: $(wstate)"; shot widget/upd-51-dark
  a cmd uimode night no; sleep 3
}

# ================================================================= HOMEY (y на всеки раздел в първите 5 s)
hy_run() {  # $1 — етикет; [$2 = clear — празен кеш на приложението]
  local L=$1 d=$OUT/homey/$1 rp
  mkdir -p $d/frames
  a am force-stop $PKG; sleep 2
  [ "${2:-}" = clear ] && a "rm -rf /data/data/$PKG/cache/*"
  a "rm -f /sdcard/hy.mp4"
  # Запис на екрана (1080x5600 → 270x1400), 1 s преди старта + 5 s след него
  T=30 a screenrecord --size 270x1400 --bit-rate 6000000 --time-limit 6 /sdcard/hy.mp4 & rp=$!
  sleep 1
  a am start -n $ACT -a android.intent.action.MAIN -c android.intent.category.LAUNCHER >/dev/null 2>&1
  wait $rp
  sleep 3; dump; cp $OUT/ui.xml $d/final.xml 2>/dev/null
  timeout 60 adb pull /sdcard/hy.mp4 $d/rec.mp4 >/dev/null 2>&1
  if [ -s $d/rec.mp4 ] && command -v ffmpeg >/dev/null; then
    ffmpeg -loglevel error -i $d/rec.mp4 -vf fps=15 $d/frames/f%04d.png
    timeout 600 python3 $H_/homey_sections.py $d/frames $d/final.xml 0.25 15 1.0 "$L" 2>&1 | tee -a $SUM
    # Кадрите около 0.5, 1, 1.5, 2, 3, 5 s след старта
    for f in 0023 0031 0038 0046 0061 0090; do [ -f $d/frames/f$f.png ] && cp $d/frames/f$f.png $d/f$f.png; done
    rm -rf $d/frames
  else log "HOMEY $L: no recording ($(ls -l $d/rec.mp4 2>&1 | cut -c1-80)); ffmpeg $(command -v ffmpeg || echo missing)"; fi
}
homey_app() {  # $1 — new | base
  local K=$1
  sec "Y-$K. Home: y of every section during the first 5 s of a cold start (tall 1080x5600 screen)"
  inst $(apk_of $K) $K $(ref_of $K) || return
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$K first-install catalogue after ${WAITED} ms"
  tab home; sleep 8
  a wm size 1080x5600; sleep 3
  hy_run $K-cached-1; hy_run $K-cached-2; hy_run $K-cached-3
  hy_run $K-clearcache-1 clear; sleep 10; hy_run $K-clearcache-2 clear
  a wm size reset; sleep 3; dims
  sec "Y-$K. Normal screen: 5 s recording at 1080x2400 (what the reader sees)"
  local d=$OUT/homey/$K-normal rp; mkdir -p $d
  a am force-stop $PKG; sleep 2; a "rm -f /sdcard/hy.mp4"
  T=30 a screenrecord --size 540x1200 --time-limit 6 /sdcard/hy.mp4 & rp=$!
  sleep 1; a am start -n $ACT -a android.intent.action.MAIN -c android.intent.category.LAUNCHER >/dev/null 2>&1; wait $rp
  timeout 60 adb pull /sdcard/hy.mp4 $d/rec.mp4 >/dev/null 2>&1
  [ -s $d/rec.mp4 ] && ffmpeg -loglevel error -i $d/rec.mp4 -vf fps=2 $d/n%02d.png && log "normal-screen frames every 0.5 s: $(ls $d/*.png | wc -l)"
}
part_homey() {
  log "host tools: $( (command -v ffmpeg >/dev/null && python3 -c 'import PIL, numpy' 2>/dev/null) || { sudo apt-get update -qq >/dev/null 2>&1; sudo apt-get install -y -qq ffmpeg python3-pil python3-numpy >/dev/null 2>&1; }; command -v ffmpeg; python3 -c 'import PIL, numpy; print("PIL", PIL.__version__, "numpy", numpy.__version__)' 2>&1)"
  homey_app new; homey_app base
}

# ================================================================= FUNC (търсене, вход + Credential Manager)
sbstate() { dump; echo "expanded=$(has 'content-desc="(Назад|Back)"' && echo yes || echo no) query-12=$(grep -q 'text="12"' $OUT/ui.xml && echo yes || echo no) hint=$(has 'Търси книга|Search books' && echo yes || echo no) tabbar=$(has 'text="(Начало|Home)"' && echo yes || echo no) app-in-front=$(has "package=\"$PKG\"" && echo yes || echo NO) IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)')"; }
sb_expand() {  # разгръща полето на „Начало“ и пише „12“
  local p
  restart; tab home; sleep 3; dims; dump; p=$(xy text 'Търси книга|Search books')
  [ -n "$p" ] || { log "SEARCHBAR: field not found"; debug_ui searchbar; return 1; }
  a input tap $p; sleep 2.5; a input text 12; sleep 4
}
f_search() {
  sec "H. Home SearchBar: tap, '12', open a result, back (key)"
  local p
  restart; tab home; sleep 3; dims; dump; shot search/00-home
  p=$(xy text 'Търси книга|Search books')
  log "SEARCHBAR collapsed: field @ ${p:-MISSING}; scanner icon $(has 'content-desc="(Сканирай баркод|Scan barcode)"' && echo yes || echo NO)"
  [ -n "$p" ] || { debug_ui searchbar; return; }
  a input tap $p; sleep 2.5; shot search/01-expanded; log "SEARCHBAR tapped: $(sbstate)"
  a input text 12; sleep 5; hide_ime; sleep 1; dump; shot search/02-typed-12
  log "SEARCHBAR '12': $(texts 16 | cut -c1-300)"
  p=$(xy text '12 -те вятъра' --miny $((H/6)))
  [ -z "$p" ] && p=$(xy --first-clickable $((H/4)) $((H*4/5)) 100)
  if [ -n "$p" ]; then
    a input tap $p; sleep 4; dump; shot search/03-result
    log "SEARCHBAR result opened: back=$(has "$RX_BACK" && echo yes || echo NO) bar-gone=$(has 'Търси книга|Search books' && echo NO || echo yes); $(texts 5 | cut -c1-160)"
    back 3; shot search/04-back-from-result; log "SEARCHBAR Back from the result: $(sbstate)"
  else log "SEARCHBAR: no result row to open"; debug_ui search-result; fi
  back 2; shot search/05-after-back-key; log "SEARCHBAR Back key on the expanded bar: $(sbstate)"
  back 2; log "SEARCHBAR 2nd Back key: $(sbstate)"
  sec "H2. Back GESTURE from the expanded bar (gesture navigation; API $SDK)"
  nav_gestures
  [ "$SDK" -ge 34 ] && log "predictive back developer option: $(a settings put global enable_back_animation 1; a settings get global enable_back_animation | tr -d '\r')"
  if sb_expand; then
    log "SEARCHBAR expanded again: $(sbstate)"
    hide_ime; sleep 1; log "SEARCHBAR IME hidden: $(sbstate)"
    bgesture search/10-gesture-midway; shot search/11-after-gesture; log "SEARCHBAR after back gesture #1: $(sbstate)"
    if has 'content-desc="(Назад|Back)"'; then bgesture search/12-gesture2-midway; shot search/13-after-gesture2; log "SEARCHBAR after back gesture #2: $(sbstate)"; fi
    # Жест, пуснат по средата (отказан): разгънатото поле трябва да остане
    sb_expand && hide_ime && {
      local y=$((H/2))
      a input motionevent DOWN 2 $y; a input motionevent MOVE 60 $y; a input motionevent MOVE 200 $y; a input motionevent MOVE $((W*30/100)) $y
      sleep 0.4; shot search/14-cancel-midway
      a input motionevent MOVE 80 $y; a input motionevent MOVE 10 $y; a input motionevent UP 10 $y; sleep 2
      shot search/15-after-cancelled-gesture; log "SEARCHBAR after a CANCELLED back gesture (should stay expanded): $(sbstate)"
    }
    back 2; back 2
  fi
  nav_buttons
  sec "H3. Process death while the search is expanded with '12'"
  if sb_expand; then
    hide_ime; shot search/20-before-death; log "PD-SEARCH before: $(sbstate)"
    a input keyevent KEYCODE_HOME; sleep 3; a am kill $PKG; sleep 3
    log "PD-SEARCH killed: pid '$(a pidof $PKG | tr -d '\r')'"
    a input keyevent KEYCODE_APP_SWITCH; sleep 4; shot search/21-recents; a input tap $((W/2)) $((H/2)); sleep 6
    shot search/22-restored; log "PD-SEARCH restored from Recents: $(sbstate); $(texts 8 | cut -c1-200)"
    back 2; log "PD-SEARCH Back after restore: $(sbstate)"
  fi
}
f_login() {
  sec "CM. Sign-in screen; Credential Manager; Google Play services / password manager on the image"
  log "image: $(a getprop ro.product.name | tr -d '\r') $(a getprop ro.build.fingerprint | tr -d '\r')"
  log "GMS: $(a dumpsys package com.google.android.gms 2>/dev/null | grep -m1 versionName | tr -d '\r ') ; Play Store: $(a pm list packages 2>/dev/null | grep -c 'com.android.vending') ; GSF: $(a pm list packages 2>/dev/null | grep -c 'com.google.android.gsf')"
  log "autofill service: '$(a settings get secure autofill_service | tr -d '\r')'; credential_service: '$(a settings get secure credential_service 2>/dev/null | tr -d '\r')'; credential_service_primary: '$(a settings get secure credential_service_primary 2>/dev/null | tr -d '\r')'"
  log "credential providers (dumpsys credential): $(a dumpsys credential 2>&1 | tr -d '\r' | grep -iE 'provider|service|enabled' | head -6 | tr '\n' ' ' | cut -c1-300)"
  log "CredentialProviderService implementations: $(a cmd package query-services --brief -a android.service.credentials.CredentialProviderService 2>&1 | tr -d '\r' | grep -v '^$' | tr '\n' ' ' | cut -c1-300)"
  log "Google accounts on the device: $(a dumpsys account 2>/dev/null | tr -d '\r' | grep -cE 'Account \{name=.*type=com.google')"
  local t p
  ready; tab my; sleep 3; dims; dump
  a log -t PERFCHECK "'login open'"
  tap text '=Вход|=Sign in' || { log "LOGIN: no 'Вход' button: $(texts 10 | cut -c1-200)"; debug_ui login; return; }
  for t in 1 4 8; do sleep $t; dump; shot login/0$t-after-open; log "LOGIN +${t}s: fg $(fg); $(texts 8 | cut -c1-220)"; done
  log "LOGIN logcat (credentials/crash): $(grep -A5000 'login open' $OUT/logcat-live.txt | grep -iE 'credential|CredMan|GetCredential|CreateCredential|FATAL|AndroidRuntime: (FATAL|Process)' | grep -v PERFCHECK | head -8 | cut -c1-220 | tr '\n' ' ')"
  p=$(xy class EditText)
  if [ -n "$p" ]; then
    a input tap $p; sleep 1; a input text 1234567; sleep 1; hide_ime; dump; shot login/10-typed
    log "LOGIN card field typed (not submitted: no test account, and a made-up card would hit the real library API): $(grep -c 'text="1234567"' $OUT/ui.xml) field(s) show it; app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
  fi
  back 2; back 2; dump; log "LOGIN after Back: $(texts 4 | cut -c1-120)"
  tap text '=Вход|=Sign in' && { sleep 5; dump; shot login/20-second-open; log "LOGIN second open: fg $(fg); $(texts 6 | cut -c1-160)"; back 2; }
  log "app pid at the end of CM: '$(a pidof $PKG | tr -d '\r')'"
}
part_func() { warmup; f_search; f_login; }
part_search34() {
  warmup
  f_search
  sec "H4. API $SDK: predictive back — several frames during a slow gesture"
  nav_gestures; a settings put global enable_back_animation 1
  if sb_expand; then
    hide_ime; local y=$((H/2)) x
    a input motionevent DOWN 2 $y
    for x in 60 140 220 300 380; do a input motionevent MOVE $x $y; sleep 0.25; shot search/30-slow-gesture-x$x; done
    a input motionevent UP 380 $y; sleep 2.5; shot search/31-after-slow-gesture; log "SEARCHBAR after slow gesture (API $SDK): $(sbstate)"
  fi
  sec "H5. API $SDK: back gesture from a book page (predictive back to Home)"
  link "$BOOK_URL"; sleep 4; dims
  bgesture search/40-book-gesture-midway; dump; log "after back gesture from the book: $(texts 4 | cut -c1-120)"
  nav_buttons
  f_login
}

# ================================================================= MEDIA
pdf_try() {  # $1 — етикет, [$2 — поредният бутон за PDF]: снимки на всеки ~0.6 s, после крайно състояние
  local p i t0
  dump; p=$(xy --all content-desc 'Отвори с приложение за PDF|Open with a PDF app' | sed -n ${2:-1}p)
  [ -n "$p" ] || { log "PDF $1: no 'open with a PDF app' button: $(texts 10 | cut -c1-200)"; return 1; }
  a log -t PERFCHECK "'pdf $1 tap'"; a input tap $p; t0=$(now_ms)
  for i in 1 2 3 4 5; do sleep 0.5; shot media/pdf-$1-$i; dump; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg); $(grep -oE 'text="[^"]*(свали|браузър|download|browser|Сваля|\.pdf)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"; done
  sleep 6; dump; shot media/pdf-$1-final
  log "PDF $1 final +$(( $(now_ms) - t0 )) ms: fg $(fg); $(texts 6 | cut -c1-220)"
  log "PDF $1 FILE NAME shown: $(grep -oE 'text="[^"]*\.pdf"' $OUT/ui.xml | head -1); browser URL: $(grep -oE 'text="[^"]*(chyavorec\.org|\.pdf)[^"]*"' $OUT/ui.xml | head -1 | cut -c1-160)"
  log "PDF $1 logcat (toast/download): $(grep -A3000 "pdf $1 tap" $OUT/logcat-live.txt | grep -iE 'Toast|DownloadManager|documents|pdf' | grep -v PERFCHECK | head -4 | cut -c1-200 | tr '\n' ' ')"
  launch >/dev/null; sleep 3
}
part_media() {
  local p i cx cy n
  python3 -c "import PIL" 2>/dev/null || { sudo apt-get update -qq >/dev/null 2>&1; sudo apt-get install -y -qq python3-pil >/dev/null 2>&1; }
  sec "P. Host probe: photos of album 1 (Фотодокументална изложба) on the site"
  timeout 400 python3 $H_/probe_images.py /fotodokumentalna-izlozhba 30 384 > $OUT/probe-fotodokumentalna.txt 2>&1
  grep -E '^(PAGE|img tags|PROBE)' $OUT/probe-fotodokumentalna.txt | tee -a $SUM
  log "original 404: $(grep -c 'original 404' $OUT/probe-fotodokumentalna.txt) of $(grep -c '^#' $OUT/probe-fotodokumentalna.txt) photos; first: $(grep -m1 'original 404' $OUT/probe-fotodokumentalna.txt | cut -c1-80)"
  # Chrome без първоначалния екран (за резервния вариант с браузъра)
  a "echo 'chrome --disable-fre --no-default-browser-check --no-first-run' > /data/local/tmp/chrome-command-line"; a am set-debug-app --persistent com.android.chrome >/dev/null 2>&1
  warmup
  sec "G1. Album with missing photos: the 'Снимката липсва' placeholder"
  restart; open_gallery; waitfor "$RX_GALLERY_LOADED" 40; sleep 2; shot media/00-gallery
  open_album 1; wait_cells 20; sleep 6; dims; dump; shot media/01-album-top
  log "ALBUM 1 top: cells $(photo_cells | wc -l); placeholders $(grep -cE 'content-desc="(Снимката липсва|Photo unavailable)"' $OUT/ui.xml)"
  for i in 1 2 3; do
    sw_up 500; sleep 5; dump; shot media/02-album-scrolled-$i
    n=$(grep -cE 'content-desc="(Снимката липсва|Photo unavailable)"' $OUT/ui.xml)
    log "ALBUM 1 scrolled $i: 'Снимката липсва' tiles on screen: $n; cells $(photo_cells | wc -l)"
  done
  sec "G2. Viewer on a missing photo: message and 'Опитай отново'"
  p=$(xy content-desc '=Снимката липсва|=Photo unavailable')
  if [ -n "$p" ]; then
    a input tap $p; sleep 6; dump; shot media/10-viewer-missing
    log "VIEWER missing photo: $(viewer_page); message $(has 'Снимката не се зареди|didn.t load' && echo SHOWN || echo ABSENT); retry button $([ -n "$(xy text '=Опитай отново|=Try again')" ] && echo SHOWN || echo ABSENT); $(texts 6 | cut -c1-200)"
    if tap text '=Опитай отново|=Try again'; then
      sleep 1; dump; shot media/11-viewer-retry-1s; log "VIEWER retry +1s: $(texts 5 | cut -c1-200)"
      sleep 7; dump; shot media/12-viewer-retry-8s; log "VIEWER retry +8s: message $(has 'Снимката не се зареди|didn.t load' && echo SHOWN || echo absent); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
    fi
    sw_left 150; sleep 4; dump; shot media/13-viewer-next; log "VIEWER next: $(viewer_page); message $(has 'Снимката не се зареди|didn.t load' && echo shown || echo absent)"
    back 1.5
  else log "ALBUM 1: no 'Снимката липсва' tile to open"; debug_ui missing; fi
  for i in 1 2 3 4 5; do sw_down 150; sleep 0.5; done
  sec "G3. Viewer: double tap 2×, then pinch beyond 2×, pan while zoomed, reset"
  if open_photo 1; then
    sleep 4; dims; cx=$((W/2)); cy=$((H/2)); shot media/20-viewer-1x; log "VIEWER: $(viewer_page); error $(has 'Снимката не се зареди|didn.t load' && echo SHOWN || echo absent)"
    if mt_init; then
      dtap $cx $cy; sleep 2.5; shot media/21-viewer-2x-doubletap; log "after double tap: $(viewer_page)"
      pinch $cx $cy 300 900 16; sleep 1; shot media/22-viewer-pinch-1s
      pinch $cx $cy 300 800 16; sleep 4; shot media/23-viewer-pinch2-4s
      log "after 2 pinches (from 2×: expected up to the cap, beyond 2×): $(viewer_page)"
      python3 - $OUT/shots/media/21-viewer-2x-doubletap.png $OUT/shots/media/23-viewer-pinch2-4s.png <<'EOF' | tee -a $SUM
import sys
try:
    from PIL import Image, ImageChops, ImageStat
    a, b = (Image.open(p).convert('L') for p in sys.argv[1:3])
    print(f"ZOOM check: mean abs difference 2x-shot vs pinched-shot = {ImageStat.Stat(ImageChops.difference(a, b)).mean[0]:.1f} (≈0 = the pinch did not zoom further)")
except Exception as e:
    print(f"ZOOM check: n/a ({e})")
EOF
      sw_left 300; sleep 1.5; shot media/24-viewer-zoomed-pan; log "swipe while zoomed: $(viewer_page) (same page = the swipe panned the photo)"
      mem zoomed
      dtap $cx $cy; sleep 2; shot media/25-viewer-reset; log "after reset double tap: $(viewer_page)"
      sw_left 150; sleep 2; log "swipe after reset: $(viewer_page)"
    else log "PINCH: no multi-touch device — zoom not tested"; fi
    back 1.5
  fi
  back 1.5; back 1.5
  sec "C. Covers: catalogue list and books with an ISBN"
  log "Open Library covers from the host: $(for u in 9780743273565 9789545997827 9789544762942; do curl -s -o /dev/null -w "%{http_code}/%{size_download}B " --max-time 20 "https://covers.openlibrary.org/b/isbn/$u-M.jpg?default=false"; done)"
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; sleep 6; shot media/30-catalog-covers
  for i in 1 2 3; do sw_up 300; sleep 1.5; done; sleep 4; shot media/31-catalog-scrolled
  search_catalog Gatsby; sleep 6; shot media/32-catalog-gatsby; log "search Gatsby: $(rcount) results"
  for i in 2184 958 $INV; do link "$SITE/elektronen-katalog?inv=$i"; sleep 7; shot media/33-book-$i; dump; log "book $i: $(texts 3 | cut -c1-120); cover image nodes: $(grep -cE 'content-desc="(Корица|Cover)' $OUT/ui.xml)"; back 1.5; done
  log "Open Library traffic in logcat: $(grep -c 'openlibrary' $OUT/logcat-live.txt)"
  sec "D1. Documents: PDF download opens with its readable file name (Drive PDF viewer)"
  log "PDF handlers: $(a cmd package query-activities --brief -a android.intent.action.VIEW -t application/pdf 2>&1 | tr -d '\r' | grep -v '^$' | tr '\n' ' ' | cut -c1-200)"
  link "$SITE/load"; sleep 6; dump; shot media/40-documents; log "DOCUMENTS: $(texts 8 | cut -c1-250)"
  pdf_try online 1
  link "$SITE/load"; sleep 4; dump
  if tap text '=Исторически публикации|=Historical publications'; then sleep 4; shot media/41-publications; pdf_try publication 1; fi
  sec "D2. PDF download fails (offline on Documents) → message → browser"
  link "$SITE/load"; sleep 5; offline; sleep 2
  pdf_try offline 2
  online; sleep 5
  sec "D3. PDF with no PDF app at all (Drive disabled) → browser"
  log "disable Drive: $(a pm disable-user --user 0 com.google.android.apps.docs 2>&1 | tr -d '\r')"
  link "$SITE/load"; sleep 5; pdf_try noapp 3
  log "enable Drive: $(a pm enable com.google.android.apps.docs 2>&1 | tr -d '\r')"
}

# ================================================================= UPDATE („Какво ново“)
ver() { a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' '; }
# Колко от редовете на истинските бележки (bg) се виждат в dump-а (по първите 28 знака на реда)
notes_match() {  # $1 — етикет
  python3 - $OUT/ui.xml $OUT/update.json "$1" <<'EOF' | tee -a $SUM
import json, sys, html
ui = html.unescape(open(sys.argv[1], encoding='utf-8', errors='replace').read())
notes = json.load(open(sys.argv[2], encoding='utf-8'))['notes'].split('\n')
hit = [l for l in notes if l.strip()[:28] in ui]
print(f"NOTES {sys.argv[3]}: {len(hit)}/{len(notes)} lines of the real 1.2.28 notes visible" + ('' if len(hit) == len(notes) else f"; missing: {[l[:40] for l in notes if l not in hit]}"))
EOF
}
whatsnew_check() {  # $1 — етикет
  local i got=no
  for i in 1 2 3 4 5 6 7 8; do dump; has "$RX_WHATSNEW" && { got=yes; break; }; sleep 1.5; done
  shot update/$1
  if [ $got = yes ]; then
    log "WHATS-NEW $1: SHOWN: $(grep -oE "text=\"$RX_WHATSNEW[^\"]*\"" $OUT/ui.xml | head -1)"
    cp $OUT/ui.xml $OUT/whatsnew-$1.xml; notes_match "sheet-$1"
  else log "WHATS-NEW $1: not shown; screen: $(texts 8 | cut -c1-200)"; fi
  [ $got = yes ]
}
part_update() {
  local i p st=none t0
  sec "U0. Staged manifest (test v1.2.28 APK + the real 1.2.28 notes from the release's update.json)"
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  log "staged manifest: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["sha256"][:12], d["size"], len(d["notes"].split(chr(10))), "note lines")' $OUT/update.json 2>&1)"
  log "real release manifest (REST asset): $(curl -sS --max-time 30 "$STAGE_URL/real-update-1.2.28.json" | python3 -c 'import json,sys; d=json.load(sys.stdin); print(d["versionCode"], d["versionName"], d["sha256"][:12])' 2>&1)"
  log "notes identical to the release: $(curl -sS --max-time 30 "$STAGE_URL/real-update-1.2.28.json" | python3 -c 'import json,sys; r=json.load(sys.stdin); s=json.load(open(sys.argv[1])); print(r["notes"]==s["notes"] and r["notes_en"]==s["notes_en"])' $OUT/update.json 2>&1)"
  sec "U1. Install v1.2.27 (update source build), add a book to Want to read"
  inst app-base-upd.apk base-upd apk-base-upd-ref.txt || return
  log "install-unknown-apps: $(a appops set $PKG REQUEST_INSTALL_PACKAGES allow 2>&1; a appops get $PKG REQUEST_INSTALL_PACKAGES | tr -d '\r')"
  launch >/dev/null; t0=$(now_ms)
  for i in $(seq 1 40); do
    sleep 2; dump
    if has 'text="(Нова версия|New version)'; then st=dialog; break; fi
    has 'text="(Пропусни|Skip)"' && tap text '=Пропусни|=Skip'
    has 'text="(Не сега|Not now)"' && tap text '=Не сега|=Not now'
  done
  sec "U2. The update dialog: the real list of 1.2.28 notes"
  shot update/10-update-dialog; cp $OUT/ui.xml $OUT/dialog.xml 2>/dev/null
  log "UPDATE dialog: $st after $(( $(now_ms) - t0 )) ms: $(texts 12 | cut -c1-500)"
  [ $st = dialog ] || { log "UPDATE: no dialog — 'What's new' not tested"; return; }
  notes_match dialog-top
  # Бележките може да не се побират: превъртане в диалога
  dims; a input swipe $((W/2)) $((H*60/100)) $((W/2)) $((H*35/100)) 400; sleep 1.5; dump; shot update/11-update-dialog-scrolled; notes_match dialog-scrolled
  for i in $(seq 1 30); do
    dump
    p=$(xy text '=Инсталирай|=Install'); [ -z "$p" ] && p=$(xy text '=Изтегли и инсталирай|=Download and install')
    [ -n "$p" ] && { log "UPDATE: tap '$(python3 $H_/find.py $OUT/ui.xml --texts | grep -m1 -E '^(Инсталирай|Изтегли и инсталирай|Install|Download and install)$')'"; a input tap $p; break; }
    sleep 2
  done
  sec "U3. Install through the system installer"
  for i in $(seq 1 45); do
    sleep 2; dump
    if has 'package="com.(google.)?android.packageinstaller"'; then
      shot update/12-system-confirm; log "UPDATE system dialog: $(texts 6 | cut -c1-200)"
      tap text '=Update|=Обнови|=Install|=Инсталирай|=OK' && { sleep 3; break; }
    fi
    [ "$(a dumpsys package $PKG | grep -m1 -oE 'versionCode=[0-9]+')" = "versionCode=$NEW_VC" ] && break
    [ $((i % 10)) = 0 ] && log "UPDATE waiting: $(texts 6 | cut -c1-160)"
  done
  for i in $(seq 1 30); do [ "$(a dumpsys package $PKG | grep -m1 -oE 'versionCode=[0-9]+')" = "versionCode=$NEW_VC" ] && break; sleep 2; done
  log "UPDATE after install: $(ver); fg $(fg)"
  sleep 3; shot update/13-after-install; dump; log "screen after install: $(texts 8 | cut -c1-200)"
  sec "U4. 'What's new' sheet once after the update"
  has "$RX_WHATSNEW" || { launch >/dev/null; sleep 3; }
  if whatsnew_check 14-whats-new; then
    a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*40/100)) 400; sleep 1.5; dump; shot update/15-whats-new-scrolled; notes_match sheet-scrolled
    tap text '=Добре|=OK|=Разбрах|=Got it' || back 2
    sleep 2; dump; log "after closing the sheet: $(texts 5 | cut -c1-160)"
    a input keyevent KEYCODE_HOME; sleep 2; launch >/dev/null; sleep 5
    whatsnew_check 16-warm-relaunch && log "WHATS-NEW: shown AGAIN on a warm relaunch (should be once)" || log "WHATS-NEW: not shown on a warm relaunch (once)"
    a am force-stop $PKG; launch >/dev/null; sleep 6
    whatsnew_check 17-cold-relaunch && log "WHATS-NEW: shown AGAIN on a cold relaunch (should be once)" || log "WHATS-NEW: not shown on a cold relaunch (once, as designed)"
  fi
  dismiss; ready
  sec "U5. Settings → update status after the update"
  open_settings && { for i in 1 2 3 4 5 6; do dump; has 'Имаш последната версия|You have the latest' && break; sw_up 500; sleep 1; done; shot update/18-settings-update; log "settings update line: $(grep -oE 'text="[^"]*(версия|version)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' ')"; back 2; }
  log "app notifications: $(a dumpsys notification --noredact 2>/dev/null | tr -d '\r' | grep -A3 "pkg=$PKG" | grep -oE 'android.title=[^,]*' | head -4 | tr '\n' ' ')"
}

# ================================================================= A11Y (uiautomator)
ax() {  # $1 — етикет на екрана
  sleep ${2:-0}; dump || { log "A11Y $1: dump failed"; return; }
  mkdir -p $OUT/a11y; cp $OUT/ui.xml $OUT/a11y/$1.xml; shot a11y/$1
  python3 $H_/a11y.py $OUT/ui.xml $DENS $W $H "$1" | tee -a $SUM
}
part_a11y() {
  local p
  warmup
  want_add "$BOOK_URL"
  DENS=$(a wm density | tail -1 | grep -oE '[0-9]+$'); dims
  sec "A. Accessibility (uiautomator): main screens, density $DENS"
  restart; tab home; ax 01-home 2
  p=$(xy text 'Търси книга|Search books'); [ -n "$p" ] && { a input tap $p; sleep 2; a input text 12; sleep 4; hide_ime; ax 02-search-expanded 1; back 1.5; back 1.5; }
  ready; tab catalog; waitfor "$RX_RESULTS" 60; ax 03-catalog 1
  link "$BOOK_URL"; ax 04-book 4; back
  link_pkg "$SITE/news"; sleep 4; segstate | grep -q 'News\[checked=true' || segment news; ax 05-news 1
  dump; p=$(xy --first-clickable $((H/4)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; ax 06-article 4; back; }
  segment calendar; ax 07-calendar 1
  ready; tab my; ax 08-my 3
  open_want_list && { ax 09-want 1; back; }
  ready; tab more; ax 10-more 2
  open_settings && { ax 11-settings 1; back; }
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30; then ax 12-gallery 2
    open_album 1 && { wait_cells 15; ax 13-album 2; open_photo 1 && { ax 14-viewer 2; back 1.5; }; back 1.5; }
  fi
  back 1.5
  link "$SITE/kontakti"; ax 15-contacts 4; back
  link "$SITE/load"; ax 16-documents 4; back
  ready; tab my; sleep 2; tap text '=Вход|=Sign in' && { ax 17-login 3; back 2; }
  scanner; sleep 4; tap text '=Enter the number manually|=Въведи номера на ръка' && { sleep 2; hide_ime; ax 18-scanner-manual 1; back 2; }; back 2
  sec "A2. The same, English, font 1.3 (Home, Catalogue, Book, More)"
  a cmd locale set-app-locales $PKG --locales en-US; a settings put system font_scale 1.3; sleep 3
  restart; tab home; ax 21-home-en-font130 2
  tab catalog; waitfor "$RX_RESULTS" 60; ax 22-catalog-en-font130 1
  link "$BOOK_URL"; ax 23-book-en-font130 4; back
  ready; tab more; ax 24-more-en-font130 2
  a cmd locale set-app-locales $PKG --locales bg-BG; a settings put system font_scale 1.0
  sec "A3. Totals over all screens"
  grep -hE '^A11Y ' $SUM | awk '{for(i=1;i<=NF;i++){split($i,kv,"="); if(kv[1]~/^(MISSING|DOUBLE|SAME-LABEL|SMALL\(<48dp\))$/) s[kv[1]]+=kv[2]}} END{for(k in s) printf "%s=%d ", k, s[k]; print ""}' | sed 's/^/A11Y TOTAL: /' | tee -a $SUM
}

# ================================================================= main
NEW_VC=1488541
setup
case $PART in
  abstart-*) part_abstart;;
  jank-*) part_jank;;
  homey) part_homey;;
  mem|mem-base) part_mem;;
  monkey|monkey-base) part_monkey;;
  media) part_media;;
  widget) part_widget;;
  widgetlang) part_widgetlang;;
  widgetupd) part_widgetupd;;
  func) part_func;;
  search34) part_search34;;
  rot) part_rot;;
  offline) part_offline;;
  look*) part_look;;
  a11y) part_a11y;;
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
