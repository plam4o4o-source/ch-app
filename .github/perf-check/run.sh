#!/usr/bin/env bash
# Пълна проверка на оптимизирания release на емулатор (ВРЕМЕННО, клон debug/full-test).
#   run.sh a — старт, jank, памет при цикли, завъртане, смърт на процеса, monkey
#   run.sh b — събиране на данни (package/известия/задачи), матрица от екранни снимки,
#              офлайн старт и възстановяване, иконата и „Последни“ (FLAG_SECURE)
set -u
PART=${1:-a}
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
tab() { local p; dump || return 1; p=$(xy text "$(tablabel $1)" --last); [ -n "$p" ] || { echo "tab not found: $1"; return 1; }; echo ">>> tab $1 @ $p"; a input tap $p; }
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
ready() {
  local i
  for i in 1 2 3 4 5; do
    dismiss
    dump && [ -n "$(xy text "$(tablabel home)" --last)" ] && [ -n "$(xy text "$(tablabel more)" --last)" ] && return 0
    echo "(not on a tab screen: $(texts 6)) → back"; back 2
  done
  echo "WARN: tab bar not reached"; return 1
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

# ================================================================= ЧАСТ A
part_a() {
  sec "1. First install: first launch, time to catalogue results"
  launch | tee $OUT/start-first.txt; sleep 5; show; shot 00-first-launch
  dismiss; sleep 2; dismiss; show; shot 01-home-first
  tab catalog; t0=$(now_ms)
  if waitfor "$RX_RESULTS" 150; then log "FIRST-INSTALL catalogue results after ${WAITED} ms (tap→text, uiautomator polling ~1-2 s)"; else log "FIRST-INSTALL: NO catalogue results in 150 s"; fi
  show; shot 02-catalog-first
  waitfor "$RX_RESULTS" 10; log "polling floor (text already on screen): ${WAITED} ms — subtract from the times below"
  for i in 1 2 3; do
    a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 3; dismiss
    tab catalog
    if waitfor "$RX_RESULTS" 60; then log "INDEX-READY #$i catalogue results after ${WAITED} ms (cold start → tap Catalogue → text)"; else log "INDEX-READY #$i: NO results in 60 s"; fi
  done
  # и направо по връзка (студен старт право в каталога)
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
  # Като след инсталиране от Play: компилиране по профила (baseline profile)
  log "compile: $(T=300 a cmd package compile -m speed-profile -f $PKG | tr -d '\r' | tr '\n' ' ')"
  : > $OUT/start-cold-sp.txt
  for i in $(seq 1 5); do
    a am force-stop $PKG; sleep 3
    launch | tee -a $OUT/start-cold-sp.txt | tr '\n' ' '; echo
    sleep 3
  done
  python3 $H_/parse.py starts $OUT/start-cold-sp.txt "cold(speed-profile)" | tee -a $SUM
  dismiss

  sec "3. Jank (animations ON for this section)"
  anims 1
  jank_pass() {  # $1 — наставка за етикетите, $2 — плъзгания в каталога, $3 — на другите екрани (наполовина нагоре/надолу)
    local sfx=$1 n=$2 m=$3 i p p2 pages=0 mis=0
    dims
    restart; link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; sleep 2
    gfx_reset
    for i in $(seq 1 $n); do
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
    gfx_save catalog$sfx; log "catalog$sfx: 'Show more' pages: $pages, mis-taps recovered: $mis; screen: $(show 6 | cut -c1-200)"
    shot jank/catalog$sfx-end
    for scr in home news events; do
      case $scr in home) link "$SITE/";; news) link "$SITE/news";; events) link "$EVENTS_URL";; esac
      sleep 5; dismiss
      if [ $scr = events ] && dump && has 'content-desc="(List view|Изглед списък)"'; then tap content-desc '=List view|=Изглед списък'; sleep 2; fi
      log "jank $scr$sfx screen: $(show 6 | cut -c1-200)"
      gfx_reset
      for i in $(seq 1 $((m/2))); do sw_up 70; sleep 0.25; done
      for i in $(seq 1 $((m/2))); do sw_down 70; sleep 0.25; done
      gfx_save $scr$sfx
      shot jank/$scr$sfx
    done
  }
  jank_pass "" 40 20
  # Второ минаване при 1/4 от пикселите (540x1200): софтуерното рендиране е по-малко тясно място.
  a wm size 540x1200; a wm density 210; sleep 3
  jank_pass "-lowres" 20 20
  a wm size reset; a wm density reset; sleep 3; dims
  anims 0

  sec "4a. Memory over 30 tab/book cycles"
  restart; tab home; sleep 3; dump
  declare -A TXY
  for t in home catalog my news more; do TXY[$t]=$(xy text "$(tablabel $t)" --last); log "tab $t @ ${TXY[$t]}"; done
  mem c00
  for c in $(seq 1 30); do
    for t in home catalog my news more; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 0.4; done
    link "$BOOK_URL" >/dev/null; sleep 1.2
    [ $c = 1 ] && { show 12; has "$RX_BACK" && log "cycle 1: book screen opened" || log "cycle 1: book screen NOT detected"; }
    back 0.6
    case $c in 10|20|30) mem c$c;; esac
  done
  log "pid after cycles: $(a pidof $PKG)"

  sec "4c. Rotation stress (10 toggles each)"
  dims
  rot() {
    local name=$1 pid0 pid1 i
    pid0=$(a pidof $PKG | tr -d '\r'); shot rotation/$name-0-before
    for i in $(seq 1 10); do
      a settings put system user_rotation $((i % 2)); sleep 1.5
      [ $i = 9 ] && shot rotation/$name-1-landscape
    done
    a settings put system user_rotation 0; sleep 2
    pid1=$(a pidof $PKG | tr -d '\r'); shot rotation/$name-2-after; show 15
    log "ROTATION $name: pid $pid0 -> $pid1 ($([ "$pid0" = "$pid1" ] && echo same || echo CHANGED)) $(has "$RX_BACK" && echo '[back button visible]')"
  }
  gfx_reset
  tab home; sleep 2; tab home; sleep 2; rot home
  tab catalog; waitfor "$RX_RESULTS" 30; tap text 'Search by|Търсене|Търси'; sleep 1; a input text 12; sleep 3; show 8; rot catalog
  if dump && grep -q 'text="12"' $OUT/ui.xml; then log "ROTATION catalog: query '12' kept"; else log "ROTATION catalog: query '12' NOT found after rotation"; fi
  back 1; link "$BOOK_URL"; sleep 3; sw_up 300; sleep 1; rot book
  gfx_save rotation
  back

  sec "4d. Process death and restore"
  restart; tab catalog; waitfor "$RX_RESULTS" 30
  tap text 'Search by|Търсене|Търси'; sleep 1; a input text 12; sleep 3
  a input keyevent 111; sleep 1   # ESC: скрива клавиатурата
  dump; p=$(xy content-desc "вятъра"); [ -z "$p" ] && p=$(xy --first-clickable $((H/4)) $((H*3/4)) 120)
  echo ">>> open book @ $p"; a input tap $p; sleep 4; show 12; shot procdeath/1-before
  before_title=$(grep -o 'text="[^"]*"' $OUT/ui.xml | sed -n 1p)
  a input keyevent KEYCODE_HOME; sleep 3
  a am kill $PKG; sleep 3
  pid=$(a pidof $PKG | tr -d '\r'); [ -n "$pid" ] && { echo "still alive ($pid), retry kill"; sleep 5; a am kill $PKG; sleep 3; pid=$(a pidof $PKG | tr -d '\r'); }
  log "PROCDEATH: after am kill pid='${pid}' ($([ -z "$pid" ] && echo killed || echo 'NOT killed'))"
  launch | tee $OUT/start-procdeath.txt | tr '\n' ' '; echo; sleep 4
  show 15; shot procdeath/2-after-launcher
  log "PROCDEATH launcher: $(grep -E 'LaunchState' $OUT/start-procdeath.txt | tr -d '\r') first text before=$before_title after=$(grep -o 'text="[^"]*"' $OUT/ui.xml | sed -n 1p) $(has "$RX_BACK" && echo '→ book screen restored' || echo '→ NOT on book screen')"
  back 2; show 10; shot procdeath/3-after-back
  log "PROCDEATH back → catalogue query kept: $(grep -q 'text="12"' $OUT/ui.xml && echo yes || echo no)"
  # Вариант 2: каталог с търсене „12“ → във фона → убит → от екрана „Последни“
  tap text 'Search by|Търсене|Търси'; sleep 1; a input text 12; sleep 3; a input keyevent 111; sleep 1; show 8; shot procdeath/3b-before-kill-2
  a input keyevent KEYCODE_HOME; sleep 3; a am kill $PKG; sleep 3
  log "PROCDEATH#2 pid after kill='$(a pidof $PKG | tr -d '\r')'"
  a input keyevent KEYCODE_APP_SWITCH; sleep 4; shot procdeath/4-recents
  a input tap $((W/2)) $((H/2)); sleep 6; show 12; shot procdeath/5-from-recents
  log "PROCDEATH from Recents: $(has 'text="12"' && echo 'catalogue query restored' || echo 'query not visible') $(has "$RX_BACK" && echo '[back visible]')"

  sec "4b. Monkey 4000 events"
  no_camera
  # без падащото меню/бързите настройки (иначе monkey включва самолетен режим, DND и т.н.)
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
  sleep 2; log "pid after monkey: '$(a pidof $PKG | tr -d '\r')'; airplane=$(a settings get global airplane_mode_on | tr -d '\r')"; shot monkey-end; show 12
  mem after-monkey
}

# ================================================================= ЧАСТ B
matrix() {
  local v=$1 p i
  echo "======== matrix $v"
  restart; dims
  tab home; sleep 1; tab home; sleep 3; shot $v/01-home
  tab catalog; waitfor "$RX_RESULTS" 60; sleep 2; shot $v/02-catalog
  link "$BOOK_URL"; sleep 4; show 8; shot $v/03-book; back
  link "$EVENTS_URL"; sleep 5; show 8; shot $v/04-events; back
  tab news; sleep 5; shot $v/05-news
  dump; p=$(xy --first-clickable $((H/6)) $((H*3/4)) 150)
  if [ -n "$p" ]; then echo ">>> open article @ $p"; a input tap $p; sleep 5; show 8; shot $v/06-article; has "$RX_BACK" || echo "WARN: article screen not detected"; back; fi
  tab my; sleep 3; shot $v/07-my
  tab more; sleep 3; shot $v/08-more
  for i in 1 2 3 4; do dump; p=$(xy text '=Settings|=Настройки'); [ -n "$p" ] && break; sw_up 300; sleep 1; done
  if [ -n "$p" ]; then a input tap $p; sleep 3; shot $v/09-settings; sw_up 300; sleep 1; shot $v/09b-settings-scrolled; back; else echo "WARN: Settings not found"; fi
  tab home; sleep 1
}

part_b() {
  sec "1. First launch and warm-up (online)"
  launch; sleep 5; ready; show 10
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  tab news; sleep 6; link "$EVENTS_URL"; sleep 6; back; tab home; sleep 4

  sec "6. Collect: package, notifications, jobs"
  a dumpsys package $PKG > $OUT/dumpsys-package.txt
  { grep -E "versionCode|versionName|targetSdk|minSdk|flags=|pkgFlags|privateFlags" $OUT/dumpsys-package.txt | head -12
    echo "-- requested permissions:"; sed -n '/requested permissions:/,/install permissions:/p' $OUT/dumpsys-package.txt | head -30
    echo "-- install permissions:"; sed -n '/install permissions:/,/User 0:/p' $OUT/dumpsys-package.txt | head -30
    echo "-- runtime permissions:"; sed -n '/runtime permissions:/,/enabledComponents\|disabledComponents\|^ *$/p' $OUT/dumpsys-package.txt | head -20
  } > $OUT/package-summary.txt
  cat $OUT/package-summary.txt; grep -E "targetSdk|versionCode" $OUT/package-summary.txt | head -3 >> $SUM
  a dumpsys notification --noredact > $OUT/dumpsys-notification.txt
  { echo "-- channels / settings for $PKG"; grep -n "$PKG" $OUT/dumpsys-notification.txt | head -20
    python3 $H_/parse.py block $OUT/dumpsys-notification.txt "AppSettings: $PKG" "AppSettings:" | head -80
    grep -E "NotificationChannel\{" $OUT/dumpsys-notification.txt | grep -iE "chyavorec|mId=(news|events|loans|messages|updates|general)" | head -20; } > $OUT/notification-channels.txt
  cat $OUT/notification-channels.txt | cut -c1-300
  log "notification channels: $(grep -o 'mId=[^,]*' $OUT/notification-channels.txt | sort -u | tr '\n' ' ')"
  a dumpsys jobscheduler $PKG > $OUT/dumpsys-jobscheduler.txt
  awk '/^  JOB /{on=($0 ~ /chyavorec/)} on' $OUT/dumpsys-jobscheduler.txt > $OUT/jobs-app.txt
  grep -E "^  JOB |Minimum latency|Periodic|period|Deadline|Requires:|Run time|Enqueue time" $OUT/jobs-app.txt | tee $OUT/jobs-summary.txt
  log "app jobs: $(grep -c '^  JOB ' $OUT/jobs-app.txt); with 'Periodic/period' in JobInfo: $(grep -ciE 'periodic|period=' $OUT/jobs-app.txt); latencies: $(grep -o 'Minimum latency: [^ ]*' $OUT/jobs-app.txt | tr '\n' ' ')"
  a dumpsys activity service org.chyavorec.app/androidx.work.impl.background.systemjob.SystemJobService > /dev/null 2>&1

  sec "5. Appearance matrix"
  matrix light-en
  a cmd uimode night yes; sleep 3; show 5; shot dark-en/00-live-switch   # без рестарт: смяна на живо
  matrix dark-en
  a cmd uimode night no; sleep 2
  a cmd locale set-app-locales $PKG --locales bg-BG; sleep 3
  log "app locale: $(a cmd locale get-app-locales $PKG | tr -d '\r')"
  matrix bg
  a cmd locale set-app-locales $PKG --locales en-US; sleep 2
  a settings put system font_scale 1.3; sleep 2
  matrix font130
  a settings put system font_scale 1.0; sleep 2
}

# ================================================================= ЧАСТ C
part_c() {
  sec "1. First launch and warm-up (online, fills the caches)"
  launch; sleep 5; ready
  tab catalog; waitfor "$RX_RESULTS" 150 && log "first-install catalogue results after ${WAITED} ms" || log "first-install: NO catalogue results"
  link "$BOOK_URL"; sleep 4; back
  tab news; sleep 6; dims; dump; p=$(xy --first-clickable $((H/6)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; sleep 5; back; }
  link "$EVENTS_URL"; sleep 6; back
  tab my; sleep 3; tab more; sleep 3; tab home; sleep 4
  echo "======== landscape"
  restart; a settings put system user_rotation 1; sleep 3; dims
  tab home; sleep 3; shot landscape/01-home
  tab catalog; waitfor "$RX_RESULTS" 60; sleep 2; shot landscape/02-catalog
  link "$BOOK_URL"; sleep 4; shot landscape/03-book; back
  show 40; log "landscape rail labels found: $(for t in home catalog my news more; do [ -n "$(xy text "$(tablabel $t)" --last)" ] && printf '%s ' $t; done)"
  if tab more; then sleep 3; shot landscape/08-more; else log "landscape: 'More' tab NOT reachable (not in the rail / UI tree)"; fi
  a settings put system user_rotation 0; sleep 3; dims

  sec "Launcher icon"
  a input keyevent KEYCODE_HOME; sleep 3; shot launcher/01-home-screen
  a input swipe $((W/2)) $((H*9/10)) $((W/2)) $((H/5)) 300; sleep 3; shot launcher/02-app-drawer; show 40
  a input keyevent KEYCODE_HOME; sleep 2

  sec "4. Network: offline cold start"
  a am force-stop $PKG
  a cmd connectivity airplane-mode enable; a svc wifi disable; a svc data disable; sleep 4
  log "offline: airplane=$(a settings get global airplane_mode_on | tr -d '\r')"
  launch >/dev/null; sleep 6; dismiss
  show 10; shot offline/01-home; log "offline home: $(texts 12)"
  tab catalog; waitfor "$RX_RESULTS" 30 && log "offline catalogue: results shown (cached)" || log "offline catalogue: NO results"; shot offline/02-catalog
  link "$BOOK_URL"; sleep 4; show 10; shot offline/03-book; back
  link "$EVENTS_URL"; sleep 5; show 10; shot offline/04-events; log "offline events: $(texts 10)"; back
  tab news; sleep 5; show 10; shot offline/05-news; log "offline news: $(texts 10)"
  dump; p=$(xy --first-clickable $((H/6)) $((H*3/4)) 150); [ -n "$p" ] && { a input tap $p; sleep 5; show 10; shot offline/06-article; back; }
  tab my; sleep 3; shot offline/07-my
  tab more; sleep 3; shot offline/08-more
  tap text '=Gallery|=Галерия'; sleep 5; show 10; shot offline/09-gallery; log "offline gallery: $(texts 10)"
  sec "4. Network: re-enable, auto-recovery"
  a cmd connectivity airplane-mode disable; a svc wifi enable; a svc data enable
  sleep 20; show 10; shot recovered/09-gallery-auto; log "after reconnect (stayed on gallery, no touch): $(texts 10)"
  back; tab home; sleep 5; shot recovered/01-home
  link "$EVENTS_URL"; sleep 5; show 10; shot recovered/04-events; log "recovered events: $(texts 10)"; back
  tab news; sleep 5; shot recovered/05-news

  sec "Recents thumbnail / FLAG_SECURE"
  restart; tab my; sleep 3; shot recents/01-my-signed-out
  a input keyevent KEYCODE_APP_SWITCH; sleep 4; shot recents/02-recents-from-my-signed-out
  a input keyevent KEYCODE_BACK; sleep 2; launch >/dev/null; sleep 3
  tab my; sleep 2; tap text '=Library card|=Читателска карта'; sleep 3; show 15; shot recents/03-card-screen
  a input keyevent KEYCODE_APP_SWITCH; sleep 4; shot recents/04-recents-from-card
  a input keyevent KEYCODE_BACK; sleep 2; launch >/dev/null; sleep 3; show 15
  # самостоятелна карта → „Моето“ става защитено
  if tap text '=Card number|=Номер на картата'; then sleep 1; a input text 12345; sleep 1; a input keyevent 111; sleep 1; tap text '=Save|=Запази'; sleep 3; show 12; fi
  back; tab my; sleep 3; show 12; shot recents/05-my-with-card
  a input keyevent KEYCODE_APP_SWITCH; sleep 4; shot recents/06-recents-from-my-with-card
  log "secure flag on window: $(a dumpsys window windows | grep -E 'mCurrentFocus|FLAG_SECURE|SECURE' | head -3 | tr -s ' ' | tr '\n' ' ')"
  a input keyevent KEYCODE_HOME; sleep 2

  mem part-c-end
}

# ================================================================= ЧАСТ D
# Първо стартиране без връзка (празен кеш): екраните с грешка — възстановяват ли се сами при връзка?
part_d() {
  sec "D1. Offline first launch; reconnect while on Home (positive control)"
  a cmd connectivity airplane-mode enable; a svc wifi disable; a svc data disable; sleep 4
  log "airplane=$(a settings get global airplane_mode_on | tr -d '\r')"
  launch >/dev/null; sleep 6; ready
  show 12; shot d1/01-home-offline; log "home offline (empty cache): $(texts 12)"
  tab catalog; sleep 8; show 10; shot d1/02-catalog-offline; log "catalogue offline (empty cache): $(texts 10)"
  tab news; sleep 4; show 10; shot d1/03-news-offline
  tab home; sleep 3
  a cmd connectivity airplane-mode disable; a svc wifi enable; a svc data enable
  for t in 10 20 30; do sleep 10; dump; log "home +${t}s after reconnect (no touch): $(texts 10)"; shot d1/04-home-after-reconnect-${t}s; done
  tab news; sleep 5; shot d1/05-news-after; log "news after reconnect: $(texts 8)"
  tab catalog; waitfor "$RX_RESULTS" 90 && log "catalogue after reconnect: results in ${WAITED} ms" || log "catalogue after reconnect: NO results in 90 s"; shot d1/06-catalog-after

  sec "D2. Offline first launch; reconnect while on Gallery / Contacts / Activities"
  a am force-stop $PKG; a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; no_camera >/dev/null
  a cmd connectivity airplane-mode enable; a svc wifi disable; a svc data disable; sleep 4
  launch >/dev/null; sleep 6; ready
  tab more; sleep 3; tap text '=Gallery|=Галерия'; sleep 5; show 10; shot d2/01-gallery-offline; log "gallery offline (empty cache): $(texts 10)"
  a cmd connectivity airplane-mode disable; a svc wifi enable; a svc data enable
  for t in 10 20 30; do sleep 10; dump; log "gallery +${t}s after reconnect (no touch): $(texts 10)"; done
  shot d2/02-gallery-after-reconnect-30s
  back; for item in '=Contacts|=Контакти' '=Activities|=Дейности'; do
    a cmd connectivity airplane-mode enable; a svc wifi disable; sleep 4
    tap text "$item"; sleep 5; n=$(echo "$item" | cut -d'|' -f1 | tr -d '=' | tr 'A-Z' 'a-z')
    show 8; shot d2/03-$n-offline; log "$n offline: $(texts 8)"
    a cmd connectivity airplane-mode disable; a svc wifi enable; sleep 25
    dump; shot d2/04-$n-after-reconnect-25s; log "$n +25s after reconnect (no touch): $(texts 8)"
    back
  done
}

# ================================================================= main
setup
case $PART in a) part_a;; b) part_b;; c) part_c;; d) part_d;; esac
alive "end"
echo "## COMPLETE" >> $SUM
finish
FAIL=0
grep -q "FATAL EXCEPTION" $OUT/logcat-live.txt && grep -A3 "FATAL EXCEPTION" $OUT/logcat-live.txt | grep -q "$PKG" && FAIL=1
[ $FAIL = 0 ] && echo "RESULT: OK" || echo "RESULT: crash in app"
exit 0
