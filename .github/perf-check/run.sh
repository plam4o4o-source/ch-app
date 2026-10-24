#!/usr/bin/env bash
# Проверка „от край до край“ на release build-а на емулатор (ВРЕМЕННО, клон debug/e2e-check).
#   run.sh api29|api35 — splash/въведение/onboarding, светла/тъмна тема (и смесени),
#   3 бутона / жестове, хоризонтално положение с изрязване; dumpsys window за прозореца.
set -u
PART=${1:-api35}
PKG=org.chyavorec.app
ACT=$PKG/.MainActivity
SITE=https://chyavorec.org
BOOK_URL="$SITE/elektronen-katalog?inv=399560"
EVENTS_URL="$SITE/events"
H_=.github/perf-check
OUT=out/$PART; mkdir -p $OUT/shots
SUM=$OUT/summary.txt; : > $SUM
RX_RESULTS='[0-9]+ (results?|резултата?)'
RX_BACK='content-desc="(Back|Назад)"'

# ---------------------------------------------------------------- помощни
a() { timeout ${T:-40} adb shell "$@"; }
log() { echo "$*" | tee -a $SUM; }
sec() { echo; echo "################ $* ################"; echo "## $*" >> $SUM; alive "$*"; a log -t PERFCHECK "'section: $*'"; }
now_ms() { echo $(( $(date +%s%N) / 1000000 )); }
alive() {
  timeout 15 adb get-state >/dev/null 2>&1 && return 0
  log "!!! EMULATOR GONE (at: $1)"; finish; echo "RESULT: emulator lost"; exit 1
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
tab() { local p; dump || return 1; p=$(xy text "$(tablabel $1)" --last); [ -n "$p" ] || { echo "tab not found: $1"; debug_ui tab-$1; return 1; }; echo ">>> tab $1 @ $p"; a input tap $p; }
link() { echo ">>> link $1"; a am start -W -a android.intent.action.VIEW -d "\"$1\"" -n $ACT 2>&1 | grep -E "Status|LaunchState|TotalTime|Error" | tr '\n' ' '; echo; }
launch() { a am start -W -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $ACT 2>&1 | grep -E "Status|LaunchState|TotalTime|WaitTime|Warning|Error"; }
# Диалози: ново издание („По-късно“), обяснение за известията („Не сега“), въведението („Пропусни“).
dismiss() {
  local i p l
  for i in 1 2 3; do
    dump || return 0; p=""
    # системен диалог (напр. „Pixel Launcher isn't responding“) → „Wait“
    if has "isn.t responding|не отговаря"; then p=$(xy text '=Wait|=Изчакване'); [ -n "$p" ] && { echo ">>> system ANR dialog: $(texts 3) → Wait"; a input tap $p; sleep 3; continue; }; fi
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
restart() { a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 4; ready; }
# Главен екран с долна лента (или страничната в хоризонтално положение): затваря диалози и се връща назад.
# Не натиска „Назад“ на началния екран (иначе приложението се затваря), а връща приложението
# отпред, ако е излязло; при неуспех пази дъмпа и снимка в debug/.
NDBG=0
debug_ui() { NDBG=$((NDBG+1)); [ $NDBG -gt 15 ] && return 0; mkdir -p $OUT/debug; cp $OUT/ui.xml $OUT/debug/$NDBG-$1.xml 2>/dev/null; shot debug/$NDBG-$1; }
ready() {
  local i
  for i in 1 2 3 4 5 6; do
    dismiss
    # Само наличието на етикетите: на Android 10 uiautomator дава на етикетите в лентата празни граници.
    dump && has 'text="(Home|Начало)"' && has 'text="(More|Още)"' && return 0
    if ! has "package=\"$PKG\""; then
      echo "(app not in front: $(texts 6)) → relaunch"; launch >/dev/null; sleep 5; continue
    fi
    debug_ui ready
    if has "$RX_BACK"; then echo "(not on a tab screen: $(texts 6)) → back"; back 2
    else echo "(app in front, tab bar not found: $(texts 40))"; sleep 3; fi
  done
  log "WARN: tab bar not reached"; return 1
}
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
  echo "camera: $(a dumpsys package $PKG | grep -m1 'android.permission.CAMERA' | tr -s ' ')"
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
  log "APK size: $(stat -c %s app.apk) bytes"
  unzip -l app.apk | grep -E "\.so$|classes.*\.dex|resources.arsc" | awk '{print $1, $4}' > $OUT/apk-contents.txt
  unzip -l app.apk | tail -1 >> $OUT/apk-contents.txt
  log "APK entries: $(tail -1 $OUT/apk-contents.txt)"
  T=180 a getprop ro.build.version.release; a wm size; a wm density
  timeout 180 adb install -r app.apk || { echo "RESULT: install failed"; exit 1; }
  a pm grant $PKG android.permission.POST_NOTIFICATIONS || true
  no_camera
  a settings put system accelerometer_rotation 0; a settings put system user_rotation 0
  a settings put system font_scale 1.0; a cmd uimode night no
  adb logcat -G 16M >/dev/null 2>&1; adb logcat -c
  adb logcat -v threadtime > $OUT/logcat-live.txt 2>&1 & LOGPID=$!
  dims; log "screen ${W}x${H}"
  # системата още се успокоява след зареждането (Pixel Launcher понякога дава ANR диалог)
  sleep 25; a input keyevent KEYCODE_HOME; sleep 2; dismiss
}


# ================================================================= ОТ КРАЙ ДО КРАЙ
NAVPFX=com.android.internal.systemui.navbar
CUTPFX=com.android.internal.display.cutout.emulation
ov() { a cmd overlay list | tr -d '\r' | grep -E "$1" | tr -s ' ' | tr '\n' ' '; }
navmode() {  # gestural | threebutton
  a cmd overlay enable-exclusive --category $NAVPFX.$1 >/dev/null 2>&1 || a cmd overlay enable $NAVPFX.$1
  local o; for o in gestural threebutton twobutton; do [ $o = $1 ] || a cmd overlay disable $NAVPFX.$o >/dev/null 2>&1; done
  sleep 8
  log "NAV $1: navigation_mode=$(a settings get secure navigation_mode | tr -d '\r'); overlays: $(ov 'navbar')"
  dismiss
}
cutout() {
  log "cutout before: $(a dumpsys window displays | tr -d '\r' | grep -m2 -oiE 'cutout=[^ ]+( [^ ]+){0,6}' | tr '\n' ' ')"
  log "cutout overlays: $(ov 'cutout')"
  local c
  # Профилът pixel_6 на новите образи вече има изрязване (отгоре) — тогава нищо не се емулира.
  if a dumpsys window displays | tr -d '\r' | grep -m1 -oE 'DisplayCutout\{insets=Rect\([0-9]+, [0-9]+ - [0-9]+, [0-9]+\)' | grep -qvE 'Rect\(0, 0 - 0, 0\)'; then
    log "CUTOUT: native cutout present, no emulation overlay"; return 0
  fi
  for c in tall hole corner double; do a cmd overlay list | tr -d '\r' | grep -qE "$CUTPFX\.$c\$" && break; c=""; done
  if [ -n "$c" ]; then
    a cmd overlay enable-exclusive --category $CUTPFX.$c >/dev/null 2>&1 || a cmd overlay enable $CUTPFX.$c
    sleep 8; log "CUTOUT enabled: $c; overlays: $(ov 'cutout')"
  else log "CUTOUT: no emulation overlay"; fi
  log "cutout after: $(a dumpsys window displays | tr -d '\r' | grep -m2 -oiE 'cutout=[^ ]+( [^ ]+){0,6}' | tr '\n' ' ')"
  a input keyevent KEYCODE_HOME; sleep 3; dismiss
}
# Прозорецът на приложението: флагове, изрязване, светли/тъмни ленти.
wdump() {
  local d=$OUT/window; mkdir -p $d
  a dumpsys window windows | tr -d '\r' > $d/raw-$1.txt
  awk -v pkg="$PKG/" '/^  Window #/{on = index($0, pkg) > 0} on' $d/raw-$1.txt > $d/$1.txt
  { echo "-- dumpsys window | grep -iE navigationBarColor|statusBarColor|layoutInDisplayCutoutMode|LIGHT_STATUS|APPEARANCE"
    grep -iE "navigationBarColor|statusBarColor|layoutInDisplayCutoutMode|LIGHT_STATUS|APPEARANCE" $d/raw-$1.txt | cut -c1-400 | head -30
    echo "-- dumpsys activity top (decor bar backgrounds)"
    a dumpsys activity top | tr -d '\r' | grep -E "statusBarBackground|navigationBarBackground|DecorView" | head -8
  } > $d/$1-grep.txt
  rm -f $d/raw-$1.txt
  log "WIN $1: $(python3 $H_/parse.py win $d/$1.txt)"
}
anr_wait() {
  local p
  dump && has "isn.t responding|не отговаря" && { p=$(xy text '=Wait|=Изчакване'); [ -n "$p" ] && { echo ">>> ANR dialog → Wait"; a input tap $p; sleep 3; }; }
  return 0
}
# Разделите — по връзка (App Link с изричен компонент), не с докосване на лентата: на Android 10
# uiautomator дава на етикетите в лентата празни/изместени граници, а в хоризонтално положение е rail.
home() { link "$SITE/" >/dev/null; sleep 4; dismiss; }
catalog() { link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" ${1:-90} || log "WARN: no catalogue results"; sleep 2; }
# „Още“: /policy отваря „Поверителност“ над раздела „Още“; „Назад“ оставя самия раздел.
more() { link "$SITE/policy" >/dev/null; sleep 4; dismiss; back 2; }
open_settings() {
  local i p=""
  more
  for i in 1 2 3 4 5; do dump; p=$(xy text '=Settings|=Настройки'); [ -n "$p" ] && break; sw_up 300; sleep 1; done
  [ -n "$p" ] || { log "WARN: Settings not found"; debug_ui settings; return 1; }
  a input tap $p; sleep 3
}
set_theme() {  # $1 — етикет ('=Light|=Светла'), $2 — име на снимката на настройките (по избор)
  dims
  open_settings || { ready; return 1; }
  tap text '=Theme|=Тема' || { back; ready; return 1; }; sleep 2; show 8
  tap text "$1" --last || { log "WARN: theme option $1 not found"; back; back; ready; return 1; }
  sleep 4; anr_wait; show 12
  log "THEME -> $1 (settings now shows: $(texts 14 | grep -oE '(System default|Light|Dark|Както системата|Светла|Тъмна)' | head -1))"
  [ -n "${2:-}" ] && shot "$2"
  back 2; ready
}
# Системната тъмна тема. На Android 10 `cmd uimode night` от shell не действа (заключено) —
# там превключвателят „Dark theme“ в Настройки › Дисплей.
night() {  # yes | no
  if [ "$SDK" -lt 30 ]; then
    local cur; cur=$(a settings get secure ui_night_mode | tr -d '\r')
    if { [ $1 = yes ] && [ "$cur" != 2 ]; } || { [ $1 = no ] && [ "$cur" = 2 ]; }; then
      local i want; want=$([ $1 = yes ] && echo yes || echo no)
      a am start -a android.settings.DISPLAY_SETTINGS >/dev/null; sleep 5
      shot debug/night-$1-$NDBG-a-before
      tap text '=Dark theme|=Тъмна тема' || debug_ui darktheme
      sleep 3; shot debug/night-$1-$NDBG-b-after; NDBG=$((NDBG+1))
      # възможен диалог за потвърждение
      dump && { p=$(xy text '=OK|=Turn on|=Turn off|=Включване|=Изключване'); [ -n "$p" ] && { echo ">>> confirm $p"; a input tap $p; sleep 2; }; }
      for i in $(seq 1 15); do a cmd uimode night | grep -qi "night mode: $want" && break; sleep 2; done
      log "NIGHT $1 via Settings: after ${i} polls: $(a cmd uimode night | tr -d '\r')"
      launch >/dev/null; sleep 4
    fi
  else
    a cmd uimode night $1 >/dev/null; sleep 4
  fi
  anr_wait; dismiss
  log "NIGHT $1: $(a cmd uimode night | tr -d '\r' | tr '\n' ' '); ui_night_mode=$(a settings get secure ui_night_mode | tr -d '\r')"
}
# Завъртане: `wm user-rotation lock` (Android 11+), иначе настройката user_rotation.
rot() {
  a settings put system user_rotation $1
  a wm user-rotation lock $1 >/dev/null 2>&1
  sleep 4; dims; anr_wait
  log "ROTATION $1: $(a dumpsys window displays | tr -d '\r' | grep -m1 -oE 'mRotation=[^ ]+|mCurrentRotation=[^ ]+') $(a dumpsys window displays | tr -d '\r' | grep -m1 -oE 'cur=[0-9]+x[0-9]+')"
}
scrolled() {  # $1 — име; съдържанието под лентите
  sw_up 300; sleep 2; shot "$1"; sw_down 300; sleep 1
}

part_e2e() {
  sec "0. Device"
  SDK=$(a getprop ro.build.version.sdk | tr -d '\r')
  log "android $(a getprop ro.build.version.release | tr -d '\r') sdk $SDK; $(a wm size | tr -d '\r' | tail -1); $(a wm density | tr -d '\r' | tail -1)"
  navmode gestural
  cutout
  dims; log "screen ${W}x${H}"

  sec "1. Fresh install: splash, intro, first onboarding page (animations ON)"
  anims 1; sleep 1
  # Видео на първите секунди (screenrecord), от което се вадят кадри; освен това — поредица снимки.
  a rm -f /sdcard/intro.mp4
  timeout 30 adb shell screenrecord --time-limit 15 --size 540x1200 --bit-rate 6000000 /sdcard/intro.mp4 > $OUT/screenrecord.txt 2>&1 & REC=$!
  sleep 1.5
  a am start -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $ACT >/dev/null
  wait $REC; sleep 1
  timeout 60 adb pull /sdcard/intro.mp4 $OUT/intro.mp4 >/dev/null 2>&1
  if [ -s $OUT/intro.mp4 ] && command -v ffmpeg >/dev/null; then
    ffmpeg -loglevel error -i $OUT/intro.mp4 -vf fps=2 $OUT/shots/01-intro-%02d.png
    log "intro video: $(stat -c %s $OUT/intro.mp4) bytes, frames: $(ls $OUT/shots/01-intro-*.png 2>/dev/null | wc -l)"
  else
    log "intro video: size=$(stat -c %s $OUT/intro.mp4 2>/dev/null) ffmpeg=$(command -v ffmpeg) ($(head -c 300 $OUT/screenrecord.txt))"
  fi
  anr_wait
  waitfor 'text="(Skip|Пропусни)"' 40 || log "WARN: onboarding Skip not seen"
  sleep 2; show 12; shot 02-onboarding-1; wdump onboarding
  anims 0

  sec "2. Skip onboarding; Home light"
  tap text '=Skip|=Пропусни'; sleep 4
  dismiss; ready; home; show 10; shot 03-home-light; wdump home-light
  catalog 150; shot 03c-catalog-light; wdump catalog-light

  sec "3. System dark (app theme = System)"
  night yes
  home; shot 04-home-dark; wdump home-dark
  catalog; shot 05-catalog-dark; wdump catalog-dark

  sec "4. App Light in a dark system"
  set_theme '=Light|=Светла' 06a-settings-applight-sysdark
  home; shot 06-home-applight-sysdark; wdump home-applight-sysdark
  catalog; shot 06b-catalog-applight-sysdark

  sec "4b. App Dark in a light system"
  night no
  set_theme '=Dark|=Тъмна' 07a-settings-appdark-syslight
  home; shot 07-home-appdark-syslight; wdump home-appdark-syslight
  catalog; shot 07b-catalog-appdark-syslight
  set_theme '=System default|=Както системата'

  sec "5. 3-button navigation"
  navmode threebutton; dims
  home; shot 08-home-3btn-light; wdump home-3btn-light
  catalog; shot 08c-catalog-3btn-light
  night yes
  home; shot 09-home-3btn-dark; wdump home-3btn-dark
  catalog; shot 09c-catalog-3btn-dark
  night no

  sec "6. Back to gesture navigation"
  navmode gestural; dims
  home; shot 10-home-gesture-light; wdump home-gesture-light

  sec "7. Landscape with 3-button navigation (cutout)"
  navmode threebutton
  rot 1
  home; show 10; shot 11-land90-home; wdump land90-home
  catalog; shot 12-land90-catalog
  rot 3
  catalog 30; shot 13-land270-catalog; wdump land270-catalog
  home; shot 14-land270-home
  night yes
  home; shot 15-land270-home-dark
  rot 1
  home; shot 16-land90-home-dark
  night no; rot 0

  # Накрая: при първите два опита емулаторът с API 35 падна при плъзгане по „Начало“ — тук не губи нищо.
  sec "8. Scrolled content under the bars (3-button, then gestures)"
  home; scrolled 17-home-3btn-light-scrolled
  catalog 30; scrolled 18-catalog-3btn-light-scrolled
  navmode gestural; dims
  home; scrolled 19-home-gesture-light-scrolled
  night yes
  home; scrolled 20-home-gesture-dark-scrolled
  night no
}


# ================================================================= main
setup
part_e2e
alive "end"
echo "## COMPLETE" >> $SUM
finish
FAIL=0
grep -q "FATAL EXCEPTION" $OUT/logcat-live.txt && grep -A3 "FATAL EXCEPTION" $OUT/logcat-live.txt | grep -q "$PKG" && FAIL=1
[ $FAIL = 0 ] && echo "RESULT: OK" || echo "RESULT: crash in app"
exit 0
