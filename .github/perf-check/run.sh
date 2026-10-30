#!/usr/bin/env bash
# Проверка на сливането от вълна 14 (52334a7; тестово v1.2.28) на емулатор преди изданието
# (ВРЕМЕННО, клон debug/wave14-check, резултати в emulator-results/v128), със сравнение с
# v1.2.27 (8dc63c1). Приложението е на български (bg-BG), освен в EN вариантите (look1, fonts).
#   abstart-1/2 — студен и топъл старт A/B срещу v1.2.27 на един емулатор (ABBA / BAAB)
#   homey       — „Начало“ без скокове: y на „Нови постъпления“/„За теб“ в първите секунди след
#                 студен старт (с кеш и с празен кеш), лентата на състоянието; и двете издания
#   widget      — уиджетът (RemoteViews): добавяне от launcher-а, широк/малък, без вход, тъмен, докосвания
#   widgetupd   — уиджет от v1.2.27 на началния екран → adb install -r v1.2.28 → работи ли;
#                 групите на каналите след обновяването
#   func14      — SearchBar на „Начало“, native библиотеките и скенерът без камера, входът
#                 (Credential Manager), групите на каналите, преките пътища
#   sem         — TalkBack: двойни заглавия, редове 48 dp в диалозите, „Кажи ми“ (и v1.2.27)
#   fonts       — подмножеството на шрифтовете: BG/EN, „“ – — › №, латински имена, курсив
#   media       — „Снимката липсва“ в албум, грешка с „Опитай отново“ в прегледа, PDF с име
#   mem, mem-base — памет при 20 цикъла; monkey — 5000 събития, seed 4321, без камера
#   rot, offline, look1/2/3 — завъртане и смърт на процеса, без връзка, матрица от изгледи
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
  if [ "$PART" = homey ] || [ "$PART" = func14 ]; then timeout 30 adb root 2>&1 | head -2; sleep 4; timeout 60 adb wait-for-device; fi
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
  python3 $H_/parse.py starts $OUT/start-new-cold-all.txt "cold NEW $NEW_V" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-base-cold-all.txt "cold BASE $BASE_V" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-new-warm-all.txt "warm NEW $NEW_V" | tee -a $SUM
  python3 $H_/parse.py starts $OUT/start-base-warm-all.txt "warm BASE $BASE_V" | tee -a $SUM
  for k in new base; do
    python3 $H_/parse.py starts $OUT/cat-first-$k.txt "catalogue first-install $k" | tee -a $SUM
    python3 $H_/parse.py starts $OUT/cat-ready-$k.txt "catalogue index-ready $k" | tee -a $SUM
  done
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
  sec "M. Memory over 20 cycles (all tabs + a book + the gallery viewer), $K"
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
    a input tap ${TXY[more]}; sleep 1.5
    if gallery_cycle; then gok=$((gok+1)); else [ $c -le 2 ] && debug_ui gallery-cycle-$c; ready >/dev/null; fi
    [ $c = 1 ] && shot cycles-after-gallery
    case $c in 10|20) log "cycles done: $c, gallery viewer reached in $gok"; mem c$c;; esac
  done
  log "pid after cycles: $(a pidof $PKG | tr -d '\r'); gallery viewer reached in $gok/20 cycles"
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
  python3 $H_/parse.py block $OUT/notif-$1.txt "AppSettings: $PKG" "AppSettings:" > $OUT/channels-$1.txt
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
  WIDS=$(awk '/^ *\[[0-9]+\] id=/{id=$2} /provider=.*org\.chyavorec\.app/{ if (id != "") { print id; id="" } }' $OUT/appwidget-$1.txt | sort -u | tr '\n' ' ')
  echo $WIDS | wc -w
}
wopts() { grep -A12 "$(echo $WIDS | awk '{print $NF}')" $OUT/appwidget-$1.txt 2>/dev/null | grep -oE 'appWidget(Min|Max)(Width|Height)=[0-9]+' | tr '\n' ' '; }
wstate() {  # уиджетът на началния екран (след dump)
  dump || { echo "(ui dump failed)"; return; }
  echo "views from $PKG: $(grep -c "resource-id=\"$PKG:id/" $OUT/ui.xml) [ids: $(grep -oE "resource-id=\"$PKG:id/[a-zA-Z0-9_]+\"" $OUT/ui.xml | sed -E 's/.*:id\///; s/"//' | sort -u | tr '\n' ' ' | cut -c1-200)]; bounds [$(xy --widget-bounds $PKG)]; texts: $(python3 $H_/find.py $OUT/ui.xml --widget-texts $PKG | tr '\n' '|' | cut -c1-300); error text: $(grep -oE 'text="[^"]*(load widget|loading widget|add widget|Unable to load|не може да се зареди)[^"]*"' $OUT/ui.xml | head -1)"
}
# Отваря избора на уиджети от началния екран; WPX/WPY — визуализацията на уиджета ни.
widget_picker() {  # $1 — етикет
  local p i
  WPX=""; WPY=""
  go_home 2; dims; go_home 2; dump; shot widget/$1-00-home
  lpress $((W/2)) $((H*45/100)) 1500; sleep 2; dump; shot widget/$1-01-longpress
  log "WIDGET $1 long-press menu: $(texts 8 | cut -c1-200)"
  tap text '=Widgets|=Приспособления|=Уиджети' || { log "WIDGET $1: no 'Widgets' in the menu"; debug_ui widgets-menu; go_home; return 1; }
  sleep 3; dump; shot widget/$1-02-picker
  for i in 1 2 3 4 5 6 7 8; do dump; [ -n "$(xy text "$LABEL_W")" ] && break; sw_up 500; sleep 1.5; done
  if [ -z "$(xy text "$LABEL_W")" ]; then
    tap class EditText && { sleep 1; a input text Chitalishte; sleep 3; hide_ime; dump; }
  fi
  shot widget/$1-03-list
  p=$(xy text ' × ' --miny $((H/8)))
  if [ -z "$p" ]; then tap text "$LABEL_W"; sleep 2.5; dump; p=$(xy text ' × ' --miny $((H/8))); fi
  shot widget/$1-04-app
  log "WIDGET $1 picker texts: $(python3 $H_/find.py $OUT/ui.xml --texts | grep -E 'Chitalishte|Читалище|×' | tr '\n' '|' | cut -c1-200)"
  [ -n "$p" ] || { log "WIDGET $1: preview not found in the picker"; debug_ui picker; go_home; return 1; }
  WPX=${p% *}; WPY=$(( ${p#* } - 150 ))
}
widget_add() {  # $1 — етикет; 0 = копие на уиджета е добавено
  local before n
  before=$(wcount $1-before)
  widget_picker $1 || return 1
  mdrag $WPX $WPY $((W/2)) $((H*42/100))
  sleep 3; dump; shot widget/$1-05-dropped
  n=$(wcount $1-after)
  if [ "$n" -le "$before" ]; then
    log "WIDGET $1: drag with motionevent bound nothing ($n) — trying 'input draganddrop'"
    widget_picker $1-retry || return 1
    a input draganddrop $WPX $WPY $((W/2)) $((H*42/100)) 3000 2>&1 | head -2
    sleep 3; dump; shot widget/$1-06-dropped-retry
    n=$(wcount $1-after2)
  fi
  log "WIDGET $1: widgets of ours bound: $before -> $n (ids $WIDS); options: $(wopts $1-after2 2>/dev/null)$(wopts $1-after)"
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
widget_resize_small() {
  local wb l t r b cy n
  go_home 3; dump; wb=$(xy --widget-bounds $PKG)
  [ -n "$wb" ] || { log "RESIZE: widget not on screen"; return 1; }
  read l t r b <<< "$wb"; cy=$(( (t + b) / 2 ))
  lpress $(( (l + r) / 2 )) $cy 1200; sleep 2; dump; shot widget/$K-29-resize-frame
  log "RESIZE frame: $(texts 8 | cut -c1-200)"
  mdrag $((r - 6)) $cy $(( l + (r - l) * 45 / 100 )) $cy 0.3
  sleep 2; shot widget/$K-29b-resized; go_home 3; go_home 3
  n=$(wcount $K-resized)
  log "RESIZE: right edge $r → $(( l + (r - l) * 45 / 100 )); options now: $(wopts $K-resized)"
}
part_widget() {
  K=new
  warmup
  sec "W1. Add the widget from the launcher's widget picker (default 4×2 = wide)"
  log "launcher: $(launcher)"
  widget_add new || { log "WIDGET: NOT added — widget checks skipped"; return; }
  sec "W2. Wide widget, signed out: content"
  go_home 4; log "WIDGET wide: $(wstate)"; shot widget/10-wide-light
  log "WIDGET signed-out: loans block $(has "$PKG:id/widget_loans\"" && echo SHOWN || echo hidden); event $(has "$PKG:id/widget_event_title" && echo shown || echo absent); no-events $(has "$PKG:id/widget_no_events" && echo shown || echo absent)"
  sec "W3. Wide widget: clicks"
  widget_clicks
  sec "W4. Wide widget in dark mode"
  a cmd uimode night yes; sleep 5; go_home 4; go_home 3; log "WIDGET wide dark: $(wstate)"; shot widget/20-wide-dark
  a cmd uimode night no; sleep 5; go_home 3
  sec "W5. Small widget (resized to 2 columns)"
  widget_resize_small
  go_home 3; log "WIDGET small: $(wstate)"; shot widget/30-small-light
  wclick widget_event small-event "$L_SUBSCRIBE"
  wclick "t:$LABEL_W" small-app 'text="(Home|Начало)"'
  a cmd uimode night yes; sleep 5; go_home 4; go_home 3; log "WIDGET small dark: $(wstate)"; shot widget/31-small-dark
  a cmd uimode night no; sleep 4
  sec "W6. After a reboot of the app process (force-stop) and a new day of data"
  a am force-stop $PKG; sleep 3; go_home 3; log "WIDGET after force-stop: $(wstate)"; shot widget/40-after-force-stop
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
  local ids0=$WIDS
  sec "U2. adb install -r v1.2.28 over v1.2.27 with the widget on the home screen (app not opened)"
  go_home 2
  timeout 180 adb install -r app.apk 2>&1 | tail -1
  log "after adb install -r: $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  local t0 t; t0=$(now_ms)
  for t in 3 15 45; do
    while [ $(( $(now_ms) - t0 )) -lt $((t * 1000)) ]; do sleep 0.5; done
    go_home 1; log "WIDGET +${t}s after update: $(wstate)"; shot widget/upd-2$t-after-update-${t}s
  done
  wcount upd-after >/dev/null; log "widget ids before/after the update: [$ids0] / [$WIDS]"
  sec "U3. The kept widget after the update: clicks"
  widget_clicks
  sec "U4. App opened after the update: channel groups; widget"
  restart; channels after-update | tee -a $SUM
  notif_settings_shots widget/upd-40-notif-settings
  go_home 3; log "WIDGET after the app ran: $(wstate)"; shot widget/upd-50-after-app
  a cmd uimode night yes; sleep 5; go_home 4; go_home 3; log "WIDGET dark after update: $(wstate)"; shot widget/upd-51-dark
  a cmd uimode night no; sleep 3
}

# ================================================================= HOMEY (без скокове на „Начало“, лентата на състоянието)
hy_run() {  # $1 — етикет; [$2 = clear — празен кеш на приложението, без въведението]
  local L=$1 d=$OUT/homey/$1 nshots=0
  mkdir -p $OUT/homey; rm -rf $d
  case $L in *-1) nshots=4;; esac
  a am force-stop $PKG; sleep 2
  [ "${2:-}" = clear ] && a "rm -rf /data/data/$PKG/cache/*"
  a "rm -rf /sdcard/hy; mkdir -p /sdcard/hy"
  cat > $OUT/hy.sh <<EOF
read t0 x < /proc/uptime
(i=0; while [ \$i -lt $nshots ]; do screencap -p /sdcard/hy/s\$i.png; read t x < /proc/uptime; echo "\$i \$t" >> /sdcard/hy/stimes.txt; i=\$((i+1)); sleep 0.4; done) &
am start -n $ACT -a android.intent.action.MAIN -c android.intent.category.LAUNCHER >/dev/null 2>&1
i=0
while [ \$i -lt 9 ]; do
  uiautomator dump /sdcard/hy/\$i.xml >/dev/null 2>&1
  read t x < /proc/uptime
  echo "\$i \$t0 \$t" >> /sdcard/hy/times.txt
  i=\$((i+1))
done
wait
EOF
  timeout 20 adb push $OUT/hy.sh /data/local/tmp/hy.sh >/dev/null 2>&1
  T=150 a sh /data/local/tmp/hy.sh
  timeout 120 adb pull /sdcard/hy $d >/dev/null 2>&1
  # Снимките (1080x5600) — смалени, за да не тежат в резултатите
  if command -v convert >/dev/null; then for f in $d/s*.png; do [ -f "$f" ] && convert "$f" -resize 30% "$f"; done; fi
  python3 - $d "$L" <<'EOF' | tee -a $SUM
import os, re, sys, xml.etree.ElementTree as ET
d, label = sys.argv[1], sys.argv[2]
anchors = {'NEW': ('Нови постъпления', 'New arrivals'), 'FORYOU': ('За теб', 'For you'),
           'EVENTS': ('Предстоящи събития', 'Upcoming events'), 'QA': ('Контакти', 'Contacts')}
rows = []
for l in open(os.path.join(d, 'times.txt')) if os.path.exists(os.path.join(d, 'times.txt')) else []:
    i, t0, t = l.split()
    ys = {}
    try:
        root = ET.parse(os.path.join(d, f'{i}.xml')).getroot()
        for k, names in anchors.items():
            for n in root.iter('node'):
                if (n.get('text') or '') in names:
                    m = re.match(r'\[\d+,(\d+)\]', n.get('bounds', ''))
                    ys[k] = int(m.group(1)); break
    except Exception:
        ys = None
    rows.append((float(t) - float(t0), ys))
out = []
for t, ys in rows:
    out.append(f"{t:4.1f}s " + ('(no dump)' if ys is None else ' '.join(f"{k}={ys.get(k, '-')}" for k in anchors)))
moves = []
for k in anchors:
    seen = [ys[k] for t, ys in rows if ys and k in ys]
    if seen:
        moves.append(f"{k}: first y={seen[0]} moved={max(seen) - min(seen)}px over {len(seen)} samples")
    else:
        moves.append(f"{k}: never on screen")
print(f"HOMEY {label}: " + ' | '.join(out))
print(f"HOMEY {label} moves: " + '; '.join(moves))
EOF
}
homey_app() {  # $1 — new | base
  local K=$1
  sec "Y-$K. Home: y of „Нови постъпления“ / „За теб“ during the first seconds of a cold start (tall 1080x5600 screen)"
  inst $(apk_of $K) $K $(ref_of $K) || return
  launch >/dev/null; sleep 6; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$K first-install catalogue after ${WAITED} ms"
  tab home; sleep 8
  a wm size 1080x5600; sleep 3
  hy_run $K-cached-1; hy_run $K-cached-2
  hy_run $K-clearcache-1 clear; sleep 10; hy_run $K-clearcache-2 clear
  a wm size reset; sleep 3; dims
  sec "Y-$K. Status bar scrim on Home (scrolled), light and dark"
  restart; tab home; sleep 4; sw_up 300; sleep 2; shot homey/$K-scrim-light
  a cmd uimode night yes; sleep 3; restart; tab home; sleep 4; sw_up 300; sleep 2; shot homey/$K-scrim-dark
  a cmd uimode night no; sleep 3
}
part_homey() { homey_app new; homey_app base; }

# ================================================================= FUNC14 (търсене, скенер/native, вход, канали, преки пътища)
sbstate() { dump; echo "expanded=$(has 'content-desc="(Назад|Back)"' && echo yes || echo no) query-12=$(grep -q 'text="12"' $OUT/ui.xml && echo yes || echo no) hint=$(has 'Търси книга|Search books' && echo yes || echo no) tabbar=$(has 'text="(Начало|Home)"' && echo yes || echo no) app-in-front=$(has "package=\"$PKG\"" && echo yes || echo NO) IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)')"; }
f_search() {
  sec "H. Home SearchBar (Material 3): tap, '12', open a result, back, back gesture"
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
  sec "H2. Back GESTURE from the expanded bar (gesture navigation; API $SDK — the predictive animation needs Android 14+)"
  nav_gestures
  restart; tab home; sleep 3; p=$(xy text 'Търси книга|Search books')
  if [ -n "$p" ]; then
    a input tap $p; sleep 2.5; a input text 12; sleep 3; log "SEARCHBAR expanded again: $(sbstate)"
    hide_ime; sleep 1; log "SEARCHBAR IME hidden: $(sbstate)"
    bgesture search/10-gesture-midway; shot search/11-after-gesture; log "SEARCHBAR after back gesture #1: $(sbstate)"
    if has 'content-desc="(Назад|Back)"'; then bgesture search/12-gesture2-midway; shot search/13-after-gesture2; log "SEARCHBAR after back gesture #2: $(sbstate)"; fi
    bgesture search/14-gesture3-midway; log "SEARCHBAR back gesture on the collapsed Home: fg $(fg)"
  fi
  nav_buttons
}
f_scan_native() {
  sec "N. Native libraries (compressed in the APK): APK entries, installed files, loaded libraries; scanner with CAMERA denied"
  python3 - app.apk <<'EOF' | tee -a $SUM
import zipfile, sys
z = zipfile.ZipFile(sys.argv[1])
so = [i for i in z.infolist() if i.filename.endswith('.so') and '/x86_64/' in i.filename]
print('APK x86_64 libs: ' + '; '.join(f"{i.filename.split('/')[-1]} type={'deflated' if i.compress_type == 8 else 'STORED' if i.compress_type == 0 else i.compress_type} {i.compress_size}/{i.file_size}" for i in so))
EOF
  local d pid
  d=$(a pm path $PKG | head -1 | tr -d '\r' | sed 's/^package://'); d=${d%/base.apk}
  log "installed: $d; lib/x86_64: $(a "ls -l $d/lib/x86_64/ 2>&1" | tr -d '\r' | awk 'NF>4{print $NF"("$5")"} NF<=4' | tr '\n' ' ' | cut -c1-400)"
  log "package flags: $(a dumpsys package $PKG | tr -d '\r' | grep -E 'primaryCpuAbi|legacyNativeLibraryDir|pkgFlags=' | tr -s ' ' | tr '\n' ' ' | cut -c1-400)"
  f_scan
  pid=$(a pidof $PKG | tr -d '\r')
  log "loaded app .so (pid $pid): $(a "grep -oE '[^ ]+\.so' /proc/$pid/maps" 2>/dev/null | tr -d '\r' | grep -E '/data/app|base.apk' | sort -u | sed -E 's|.*/||' | tr '\n' ' ' | cut -c1-400)"
  log "UnsatisfiedLinkError / dlopen failed in logcat so far: $(grep -cE 'UnsatisfiedLinkError|dlopen failed' $OUT/logcat-live.txt)"
}
f_login() {
  sec "CM. Sign-in screen; the saved-credential query (Credential Manager) on opening"
  log "autofill service: '$(a settings get secure autofill_service | tr -d '\r')'; credential service: '$(a settings get secure credential_service 2>/dev/null | tr -d '\r')'; GMS: $(a dumpsys package com.google.android.gms | grep -m1 versionName | tr -d '\r ')"
  local t p
  ready; tab my; sleep 3; dims; dump
  a log -t PERFCHECK "'login open'"
  tap text '=Вход|=Sign in' || { log "LOGIN: no 'Вход' button: $(texts 10 | cut -c1-200)"; debug_ui login; return; }
  for t in 1 4 8; do sleep $t; dump; shot login/0$t-after-open; log "LOGIN +${t}s: fg $(fg); $(texts 8 | cut -c1-220)"; done
  log "LOGIN logcat (credentials/crash): $(grep -A3000 'login open' $OUT/logcat-live.txt | grep -iE 'credential|CredMan|GetCredential|identity|FATAL|AndroidRuntime' | grep -v PERFCHECK | head -8 | cut -c1-220 | tr '\n' ' ')"
  p=$(xy class EditText)
  if [ -n "$p" ]; then
    a input tap $p; sleep 1; a input text 1234567; sleep 1; hide_ime; dump; shot login/10-typed
    log "LOGIN card field typed (not submitted: no reader test account): $(grep -c 'text="1234567"' $OUT/ui.xml) field(s) show it; app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
  fi
  back 2; back 2; dump; log "LOGIN after Back: $(texts 4 | cut -c1-120)"
  tap text '=Вход|=Sign in' && { sleep 5; dump; shot login/20-second-open; log "LOGIN second open: fg $(fg); $(texts 6 | cut -c1-160)"; back 2; }
}
f_channels() {
  sec "C. Notification channel groups (fresh install of v1.2.28)"
  channels new | tee -a $SUM
  log "cmd notification list_channels: $(a cmd notification list_channels $PKG $(a dumpsys package $PKG | grep -m1 -oE 'userId=[0-9]+' | cut -d= -f2) 2>&1 | tr -d '\r' | head -10 | tr '\n' ' ' | cut -c1-400)"
  notif_settings_shots channels/01-system-settings
}
shortcuts_dump() {  # $1 — етикет
  a dumpsys shortcut 2>/dev/null | tr -d '\r' > $OUT/shortcut-$1.txt
  awk -v p="$PKG" 'index($0, "Package: " p) {f=1; print; next} f && /(Package|Launcher): / {f=0} f' $OUT/shortcut-$1.txt > $OUT/shortcut-$1-app.txt
  log "SHORTCUTS $1: $(grep -oE 'ShortcutInfo \{id=[^,]*, flags=[^,]*' $OUT/shortcut-$1-app.txt | sed 's/ShortcutInfo {//' | tr '\n' ' ' | cut -c1-500)"
  log "SHORTCUTS $1 labels: $(grep -oE '(shortLabel|longLabel)=[^,]*' $OUT/shortcut-$1-app.txt | tr '\n' '|' | cut -c1-400)"
}
sc_popup() {  # $1 — етикет: задържане на иконата в списъка с приложения
  local p i lab='=Yavorets|=НЧ Яворец'
  go_home 2; go_home 2; dims
  a input swipe $((W/2)) $((H*85/100)) $((W/2)) $((H*20/100)) 300; sleep 3; dump
  for i in 1 2 3 4; do p=$(xy text "$lab"); [ -n "$p" ] && break; sw_up 400; sleep 1.5; dump; done
  if [ -z "$p" ]; then tap class EditText && { sleep 1; a input text Yavorets; sleep 3; hide_ime; dump; p=$(xy text "$lab" --last); }; fi
  [ -n "$p" ] || { log "SHORTCUTS: app icon not found in the app list"; debug_ui drawer; go_home; return 1; }
  lpress ${p% *} $(( ${p#* } - 50 )) 1300; sleep 2; dump; shot shortcuts/$1-popup
  log "SHORTCUTS popup $1: $(texts 16 | cut -c1-400)"
}
f_shortcuts() {
  sec "S. App shortcuts: long-press the icon; dynamic 'Искам да прочета' and the last public book"
  shortcuts_dump before
  sc_popup 01-before
  go_home 2
  link "$BOOK_URL"; sleep 5; dump; log "public book opened: $(texts 3 | cut -c1-100)"; back 2
  open_want_list; sleep 2; back 2
  sleep 3; shortcuts_dump after
  sc_popup 02-after
  if tap text '12 -те вятъра'; then sleep 4; dump; shot shortcuts/03-book-shortcut-opened
    log "SHORTCUT '12 -те вятъра' → fg $(fg); $(texts 4 | cut -c1-140)"
  else log "SHORTCUT book: not in the popup"; go_home; fi
}
part_func14() { warmup; f_search; f_scan_native; f_login; f_channels; f_shortcuts; }

# ================================================================= SEM (TalkBack: двойни заглавия, редове 48 dp, „Кажи ми“)
notify_nodes() { dump; echo "desc: $(xy --nodes content-desc 'Кажи ми|Ще ти кажем|Не ми казвай|Notify me|We’ll notify' | tr '\n' ';' | cut -c1-300) || text: $(xy --nodes text 'Кажи ми|Ще ти кажем|Notify me|We’ll notify' | tr '\n' ';' | cut -c1-300)"; }
sem_run() {  # $1 — new | base
  local K=$1 item inv found=no i
  sec "T-$K. TalkBack semantics ($K)"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; search_catalog 12; dims; dump; shot sem/$K-01-catalog-12
  log "SEM $K catalogue '12': content-desc repeating a visible text: $(xy --dupes)"
  cp $OUT/ui.xml $OUT/sem-$K-catalog.xml
  link "$BOOK_URL"; sleep 5; dump; shot sem/$K-02-book; log "SEM $K book page: content-desc repeating a visible text: $(xy --dupes)"; cp $OUT/ui.xml $OUT/sem-$K-book.xml; back 2
  want_add "$BOOK_URL" >/dev/null; open_want_list; dump; shot sem/$K-03-want; log "SEM $K want list: content-desc repeating a visible text: $(xy --dupes)"; back 2
  for item in '=Тема|=Theme' '=Размер на текста|=Text size' '=Език|=Language'; do
    if open_settings && settings_item "$item"; then
      sleep 2; dump; shot sem/$K-10-dialog-$(echo ${item#=} | cut -d'|' -f1 | tr ' ' _)
      log "SEM $K dialog '${item#=}': rows $(xy --radio 420 | tr '\n' ';')"
      cp $OUT/ui.xml "$OUT/sem-$K-dialog-$(echo ${item#=} | cut -d'|' -f1 | tr ' ' _).xml"
      back 2
    fi
  done
  for inv in 136 15 90 20 153 1 624 1872; do
    link "$SITE/elektronen-katalog?inv=$inv"; sleep 4; dims; dump
    has 'Кажи ми|Notify me when' || { sw_up 300; sleep 1.5; dump; }
    if has 'Кажи ми|Notify me when'; then found=yes; break; fi
    back 1.5
  done
  log "SEM $K 'Кажи ми' book inv=$inv found=$found: $(texts 3 | cut -c1-90)"
  [ $found = yes ] || return
  shot sem/$K-20-notify-off; log "SEM $K 'Кажи ми' OFF: $(notify_nodes)"
  tap content-desc 'Кажи ми|Notify me when' || tap text 'Кажи ми|Notify me when'
  sleep 2.5; shot sem/$K-21-notify-on; log "SEM $K 'Кажи ми' after tap (ON): $(notify_nodes)"
  tap content-desc 'Кажи ми|Ще ти кажем|Notify me|We’ll notify' || tap text 'Ще ти кажем|We’ll notify'
  sleep 2; shot sem/$K-22-notify-off-again; log "SEM $K 'Кажи ми' after 2nd tap (OFF): $(notify_nodes); snackbar: $(grep -oE 'text="(Няма да получаваш[^"]*|You won’t[^"]*)"' $OUT/ui.xml | head -1)"
  back 2
}
part_sem() {
  warmup; sem_run new
  inst app-base.apk base apk-base-ref.txt && { launch >/dev/null; sleep 6; ready; dims; tab catalog; waitfor "$RX_RESULTS" 150; sem_run base; }
}

# ================================================================= FONTS (подмножеството на шрифтовете)
chars() { echo "$(grep -oE '„|“|–|—|›|№|«|»|…' $OUT/ui.xml 2>/dev/null | sort | uniq -c | awk '{printf "%s×%s ", $2, $1}')"; }
fonts_lang() {  # $1 — bg | en
  local L=$1 inv i
  sec "F-$L. Fonts: book pages with Latin names, News, an article with italics and quotes, Settings, №, ›"
  restart; dims
  for inv in 399555 399637 399510 399580; do
    link "$SITE/elektronen-katalog?inv=$inv"; sleep 5; dump; shot fonts/$L-book-$inv
    log "FONTS $L book $inv: $(texts 4 | cut -c1-140); chars: $(chars)"; back 1.5
  done
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; search_catalog Sanktus; sleep 2; shot fonts/$L-catalog-sanktus; log "FONTS $L catalogue 'Sanktus': $(rcount) result(s)"
  link_pkg "$SITE/news"; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; sleep 2; dump; shot fonts/$L-news; log "FONTS $L news: chars: $(chars)"
  if [ -n "$ART_URL" ]; then
    link_pkg "$ART_URL"; sleep 6; dump; shot fonts/$L-article-1; log "FONTS $L article: $(texts 3 | cut -c1-140); chars: $(chars)"
    for i in 2 3 4; do sw_up 700; sleep 2; dump; shot fonts/$L-article-$i; log "FONTS $L article page $i chars: $(chars)"; done
    back 2
  fi
  open_settings && { dump; shot fonts/$L-settings-1; log "FONTS $L settings chars: $(chars)"; sw_up 900; sleep 2; dump; shot fonts/$L-settings-2; log "FONTS $L settings (scrolled) chars: $(chars)"; sw_up 900; sleep 2; shot fonts/$L-settings-3; back 2; }
  scanner; sleep 4; dump
  if tap text '=Enter the number manually|=Въведи номера на ръка'; then sleep 2; a input text $INV; sleep 1.5; tap text '=Search|=Търси' --last; sleep 4; dump; shot fonts/$L-scan-result; log "FONTS $L scan result chars: $(chars)"; back 2; fi
  back 2
  link "$SITE/policy"; sleep 5
  for i in 1 2 3 4 5 6 7 8 9 10; do dump; grep -q '›' $OUT/ui.xml && break; sw_up 900; sleep 1.5; done
  shot fonts/$L-privacy-arrow; log "FONTS $L privacy policy: '›' $(grep -q '›' $OUT/ui.xml && echo "on screen after $i pages" || echo 'not found'); chars: $(chars)"; back 2
  link "$SITE/kontakti"; sleep 5; dump; shot fonts/$L-contacts; log "FONTS $L contacts chars: $(chars)"; back 2
}
part_fonts() {
  local r
  r=$(timeout 120 python3 $H_/find_article.py 2>&1 | tail -1)
  ART_URL=$(echo "$r" | cut -f1); log "ARTICLE with italics/quotes (from news.json + rss.xml): $r"
  warmup
  fonts_lang bg
  a cmd locale set-app-locales $PKG --locales en-US; sleep 3; fonts_lang en
  a cmd locale set-app-locales $PKG --locales bg-BG
}

# ================================================================= MEDIA (липсващи снимки, грешка в прегледа, PDF с име)
part_media() {
  local p i n
  sec "P. Host probe: photos of album 1 (Фотодокументална изложба) on the site"
  timeout 400 python3 $H_/probe_images.py /fotodokumentalna-izlozhba 30 384 > $OUT/probe-fotodokumentalna.txt 2>&1
  grep -E '^(PAGE|img tags|PROBE)' $OUT/probe-fotodokumentalna.txt | tee -a $SUM
  log "original 404: $(grep -c 'original 404' $OUT/probe-fotodokumentalna.txt) of $(grep -c '^#' $OUT/probe-fotodokumentalna.txt) photos; first: $(grep -m1 'original 404' $OUT/probe-fotodokumentalna.txt | cut -c1-80)"
  warmup
  sec "G1. Album with missing photos: the 'Снимката липсва' placeholder"
  restart; open_gallery; waitfor "$RX_GALLERY_LOADED" 40
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
      sleep 2; dump; shot media/11-viewer-retry-2s; log "VIEWER retry +2s: $(texts 5 | cut -c1-200)"
      sleep 6; dump; shot media/12-viewer-retry-8s; log "VIEWER retry +8s: message $(has 'Снимката не се зареди|didn.t load' && echo SHOWN || echo absent); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
    fi
    sw_left 150; sleep 4; dump; shot media/13-viewer-next; log "VIEWER next: $(viewer_page); message $(has 'Снимката не се зареди|didn.t load' && echo shown || echo absent)"
    back 1.5
  else log "ALBUM 1: no 'Снимката липсва' tile to open"; debug_ui missing; fi
  for i in 1 2 3 4 5; do sw_down 150; sleep 0.5; done
  if open_photo 1; then sleep 5; dump; shot media/14-viewer-ok-photo1; log "VIEWER photo 1: $(viewer_page); error message $(has 'Снимката не се зареди|didn.t load' && echo SHOWN || echo absent)"; back 1.5; fi
  back 1.5; back 1.5
  sec "D. Documents: PDF opens with its readable file name"
  log "PDF handlers: $(a cmd package query-activities --brief -a android.intent.action.VIEW -t application/pdf 2>&1 | tr -d '\r' | grep -v '^$' | tr '\n' ' ' | cut -c1-200)"
  pdf_try() {  # $1 — етикет
    local p t0; dump; p=$(xy content-desc 'Отвори с приложение за PDF|Open with a PDF app|PDF app')
    [ -n "$p" ] || { log "PDF $1: no 'open with a PDF app' button: $(texts 10 | cut -c1-200)"; return 1; }
    a log -t PERFCHECK "'pdf $1 tap'"; a input tap $p; t0=$(now_ms)
    sleep 2; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg)"
    sleep 4; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg)"
    sleep 5; dump; shot media/pdf-$1; log "PDF $1 +$(( $(now_ms) - t0 )) ms: fg $(fg); $(texts 6 | cut -c1-200)"
    log "PDF $1 FILE NAME shown: $(grep -oE 'text="[^"]*\.pdf"' $OUT/ui.xml | head -1)"
    launch >/dev/null; sleep 3
  }
  link "$SITE/load"; sleep 6; dump; shot media/20-documents; log "DOCUMENTS: $(texts 8 | cut -c1-250)"
  pdf_try document
  link "$SITE/load"; sleep 4; dump
  if tap text '=Исторически публикации|=Historical publications'; then sleep 4; shot media/21-publications; pdf_try publication; fi
}


# ================================================================= main
setup
case $PART in
  abstart-*) part_abstart;;
  homey) part_homey;;
  widget) part_widget;;
  widgetupd) part_widgetupd;;
  func14) part_func14;;
  sem) part_sem;;
  fonts) part_fonts;;
  media) part_media;;
  mem|mem-base) part_mem;;
  monkey) part_monkey;;
  rot) part_rot;;
  offline) part_offline;;
  look*) part_look;;
  *) log "unknown part $PART";;
esac
alive "end"
echo "## COMPLETE" >> $SUM
finish
FAIL=0
grep -q "FATAL EXCEPTION" $OUT/logcat-live.txt && grep -A3 "FATAL EXCEPTION" $OUT/logcat-live.txt | grep -q "$PKG" && FAIL=1
[ $FAIL = 0 ] && echo "RESULT: OK" || echo "RESULT: crash in app"
exit 0
