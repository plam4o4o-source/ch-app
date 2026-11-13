# Проверка на издаденото v1.2.36 (ВРЕМЕННО, debug/a7-check; резултати в emulator-results/v136r).
# Издаденото v1.2.36 (6432ef1) срещу v1.2.35 (aef8910); и двете prodRelease x86_64 с временен ключ; devDebug от
# 6432ef1 за демо входа. Зарежда се от run.sh след w22.sh и ползва функциите на по-старите части.
#   c26            — (от a6.sh) обиколка, скенерът на ръка ×8, monkey 3 × 2000 на API 26: срив от споделените елементи?
#   tr26/tr28/tr36 — записани преходи между екраните (книга, събитие, статия, картата): има ли споделени елементи;
#                    tr26 — още monkey; tr28 — двойно докосване в „Старият вестник“; tr36 — категориите на уиджетите
#   yup36          — devDebug: годишната история — плъзгане надолу по точни разстояния, input swipe, всеки слайд, край
#   ng36           — Настройки: плъзгане надолу в началото на списъка (1.2.36 срещу 1.2.35), седмичен преглед,
#                    разрешението за известия, честитката за Никулден (датата на устройството)
#   wdg36          — уиджетите от бутоните в Настройки (двата), тъмна тема, докосвания
A7_PARTS=" tr26 tr28 tr36 yup36 ng36 wdg36 wdn36 wdn36b "
W20_PARTS="$W20_PARTS $A7_PARTS"
A7=$H_/a7.py

# Бавно плъзгане (не хвърляне) — превърта списъка, без да тръгва от ред в горната му част.
slow_up() { a input swipe $((W/2)) $((H*72/100)) $((W/2)) $((H*40/100)) ${1:-450}; sleep 1; }
slow_down() { a input swipe $((W/2)) $((H*45/100)) $((W/2)) $((H*78/100)) ${1:-450}; sleep 1; }
find_slow() {  # $1 — текст: превърта бавно надолу, докато се види → 0; P_FOUND — мястото
  local i
  P_FOUND=""
  for i in 1 2 3 4 5 6 7 8 9 10 11 12 13 14; do dump; P_FOUND=$(xy text "$1" --maxy $((H*93/100))); [ -n "$P_FOUND" ] && return 0; slow_up; done
  return 1
}
# Запис около едно докосване: $1 — име, $2 $3 — къде; кадрите около началото на движението → shots/tr/<име>-NN.png
trec() {
  local name=$1 n
  mkdir -p $OUT/rec $OUT/shots/tr
  a rm -f /sdcard/rec.mp4
  adb shell screenrecord --time-limit 6 --size 540x1200 --bit-rate 3000000 /sdcard/rec.mp4 >/dev/null 2>&1 &
  RECPID=$!; sleep 1.6
  if [ "$2" = back ]; then a input keyevent KEYCODE_BACK; else a input tap $2 $3; fi
  wait $RECPID 2>/dev/null; sleep 1
  rm -rf $OUT/rec/$name; mkdir -p $OUT/rec/$name
  timeout 30 adb pull /sdcard/rec.mp4 $OUT/rec/$name.mp4 >/dev/null 2>&1
  [ -s $OUT/rec/$name.mp4 ] && ffmpeg -loglevel error -i $OUT/rec/$name.mp4 -vf fps=12 $OUT/rec/$name/f%03d.png
  n=$(ls $OUT/rec/$name 2>/dev/null | wc -l)
  if [ "$n" = 0 ]; then log "TREC $name: screenrecord gave no frames"; rm -f $OUT/rec/$name.mp4; return 1; fi
  rm -rf $OUT/rec/pick; log "TREC $name: $(python3 $A7 pick $OUT/rec/pick 10 $OUT/rec/$name/f*.png | cut -c1-300)"
  for f in $OUT/rec/pick/*.png; do [ -f "$f" ] && mv "$f" $OUT/shots/tr/$name-$(basename $f); done
  rm -rf $OUT/rec/$name $OUT/rec/$name.mp4 $OUT/rec/pick
}

# ---------------------------------------------------------------- ПРЕХОДИТЕ (API 26 / 28 / 36)
part_tr() {
  local p i sd cx cy
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || log "device locale not changed (labels may be English)"; }
  a am force-stop $PKG
  anims 1
  warmup
  mark tr
  sec "TR1. Transitions recorded at 12 fps (animations ON, API $SDK): 10 frames from the start of the motion, in sheets/tr"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 2; dims; dump
  p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*80/100)) 150)
  if [ -n "$p" ]; then trec a-book-open $p; sleep 1.5; dump; log "TR book: $(has "$RX_BACK" && echo 'book screen open' || echo 'NOT open'): $(texts 4 | cut -c1-120)"
    trec b-book-back back; sleep 1.5
  else log "TR: no catalogue row"; fi
  leave
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar
  dump; has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък' >/dev/null; sleep 2; }
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $((H/4)) $((H*85/100)) 150 | sed -n 1p)
  if [ -n "$p" ]; then trec c-event-open $p; sleep 1.5; dump; log "TR event: $(has "$RX_BACK" && echo 'event screen open' || echo 'NOT open'): $(texts 4 | cut -c1-120)"
    trec d-event-back back; sleep 1.5
  else log "TR: no event row"; fi
  leave
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news
  sleep 2; dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H*45/100)) $((H*85/100)) 150)
  if [ -n "$p" ]; then trec e-article-open $p; sleep 2; dump; log "TR article: $(has 'Режим за четене|Reading mode' && echo 'article open' || echo 'NOT open'): $(texts 3 | cut -c1-120)"
    trec f-article-back back; sleep 1.5
  else log "TR: no news row"; fi
  leave
  if [ "$SDK" -lt 33 ]; then card_add28 || log "CARD: not added"; else card_open >/dev/null || log "CARD: not shown"; fi
  card_screen; sleep 3; dump; p=$(xy text "$L_CARDSHOW")
  if [ -n "$p" ]; then trec g-card-full $p; sleep 1.5; log "TR card fullscreen: $(fullscreen_ok && echo yes || echo NO)"; trec h-card-back back; else log "TR: no '$L_CARDSHOW'"; fi
  leave
  log "TR part so far: $(since_fatal tr)"
  sec "TR2. Quick open/back ×12 on a book and an event (the shared-element crash on API 26 came during navigation)"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1; dump
  p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*80/100)) 150)
  if [ -n "$p" ]; then for i in 1 2 3 4 5 6 7 8 9 10 11 12; do a input tap $p; sleep 0.7; a input keyevent KEYCODE_BACK; sleep 0.6; done; fi
  sleep 2; dump; log "after 12 quick book open/back: app in front $(has "package=\"$PKG\"" && echo yes || echo NO); pid $(pidof_app); $(since_fatal tr)"
  for i in 1 2 3 4 5 6; do
    scanner; sleep 2.5; dump
    if tap text "$L_MANUAL" >/dev/null; then sleep 1.5; hide_ime; back 1; fi
    back 1
  done
  dump; log "after 6 scanner/manual loops: app in front $(has "package=\"$PKG\"" && echo yes || echo NO); pid $(pidof_app); $(since_fatal tr)"
  leave
  if [ "$SDK" = 28 ]; then
    sec "TR3. „Старият вестник“ on API 28: double tap with two overlapping 'input tap' (sendevent was too slow on old images)"
    if more_tile "$L_NEWS_PAPER"; then
      for i in $(seq 1 10); do dump; [ -n "$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*92/100)) 150)" ] && break; sleep 2; done
      dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*92/100)) 150)
      if [ -n "$p" ]; then a input tap $p; waitfor 'Страница [0-9]+ от [0-9]+|Page [0-9]+ of [0-9]+' 90; sleep 3; shot np/reader-0 >/dev/null
        cx=$((W/2)); cy=$((H/2))
        for i in 1 2 3; do
          a "input tap $cx $cy & sleep 0.12; input tap $cx $cy; wait"; sleep 2.5; shot np/reader-dtap-$i >/dev/null
          log "NEWSPAPER double tap try $i (API $SDK): diff vs the page $(python3 $A7 same $OUT/shots/np/reader-0.png $OUT/shots/np/reader-dtap-$i.png)"
        done
        back 2
      else log "NEWSPAPER: no article row"; fi
      leave
    fi
  fi
  if [ "$SDK" = 26 ]; then
    for sd in 31337 99; do
      sec "TR4. Monkey 2000 (seed $sd) on API $SDK"
      MONKEY_N=2000 MONKEY_SEED=$sd part_monkey
      cp $OUT/monkey.txt $OUT/monkey-$sd.txt 2>/dev/null
    done
  fi
  if [ "$SDK" -ge 34 ]; then
    sec "TR5. Widget providers on API $SDK (categories: 1 = home screen, 2 = keyguard)"
    a dumpsys appwidget 2>/dev/null | tr -d '\r' > $OUT/appwidget-providers.txt
    grep -B2 -A6 -E 'provider.*org\.chyavorec\.app' $OUT/appwidget-providers.txt | grep -oE 'cmp=[^ }]+|widgetCategory=[0-9]+|min=\([^)]*\)|initialLayout=[^ ]+' | tr '\n' ' ' | cut -c1-600 | tee -a $SUM; echo >> $SUM
    log "lock-screen widgets feature: config $(a cmd overlay lookup android android:bool/config_glanceableHubEnabled 2>&1 | tr -d '\r' | head -1); communal $(a settings get secure glanceable_hub_enabled | tr -d '\r')"
  fi
  log "TR part: $(since_fatal tr); FATAL lines in the whole log: $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- ГОДИШНАТА ИСТОРИЯ (devDebug)
part_yup36() {
  local i r v lbl up mv
  helper_install
  dev_install
  anims 1
  warmup
  demo_signin_remember
  mark yup
  sec "YU1. Swipe-down dismiss with exact distances (motionevent, ×3): the rule is ≥140 dp at any speed; the finger-up position must count"
  for v in "A-moves-to-183-up-183:183:23 46 69 92 115 138 183" "B-last-move-114-up-183:183:23 46 69 92 114" "C-moves-to-160-up-160:160:40 80 120 160" "D-one-move-20-up-183:183:20" "E-moves-to-100-up-100:100:25 50 75 100" "F-moves-to-139-up-139:139:35 70 105 139" "G-last-move-114-up-150:150:30 60 90 114" "H-last-move-130-up-136:136:40 80 130"; do
    IFS=: read lbl up mv <<< "$v"
    r=""
    for i in 1 2 3; do
      year_open_fresh 25 || { r="$r no-open"; continue; }
      sleep 1.2
      r="$r $(ydrag $lbl-$i $up $mv | cut -d' ' -f1)"
    done
    log "YEAR3 $lbl (UP at $up dp; MOVE at: $mv dp):$r"
  done
  [ "$(ystate)" = open ] && back 2
  sec "YU2. 'input swipe' (×3 each): 200 dp in 300 / 150 / 600 ms, 120 dp slow, 150 dp in 300 ms"
  YSPECS="d200t300:200:300 d200t150:200:150 d200t600:200:600 d120slow:120:900 d150t300:150:300 d135t300:135:300" year_swipes2 new 3
  sec "YU3. Every slide by auto-advance (no touches; 5.5 s per slide): an emulator screenshot every ~2.5 s"
  if year_open_fresh 25; then
    for i in $(seq -w 1 22); do eshot ya-$i >/dev/null; sleep 1.2; done
    dump; log "YEAR after ~55 s of auto-advance: $(ystate); $(texts 10 | cut -c1-300)"
    [ "$(ystate)" = open ] && back 2
  else log "YEAR: story did not open"; fi
  sec "YU4. The last slide by 7 quick taps on the right, its texts, share as an image"
  a "rm -rf /data/data/$PKG/cache/share"
  if year_open_fresh 25; then
    for i in 1 2 3 4 5 6 7; do a input tap $((W*88/100)) $((H/2)); sleep 0.5; done
    sleep 2.5; eshot yb-last >/dev/null; dump; log "YEAR last slide: $(texts 14 | cut -c1-500)"
    a input tap $((W*88/100)) $((H/2)); sleep 2; log "YEAR one more tap on the last slide: $(ystate)"
    [ "$(ystate)" = open ] || { year_open_fresh 25; for i in 1 2 3 4 5 6 7; do a input tap $((W*88/100)) $((H/2)); sleep 0.5; done; sleep 2; }
    mark sh-year
    dump; tap text '=Сподели' >/dev/null || tap content-desc '=Сподели' >/dev/null; sleep 2; eshot yc-share-1 >/dev/null; dump
    has 'Сподели като картинка' && { tap text '=Сподели като картинка' >/dev/null; }
    for i in $(seq 1 15); do sleep 1; chooser_up && break; done
    eshot yc-share-2 >/dev/null
    SH_FILE=$(share_newest)
    log "YEAR share from the last slide: chooser $(chooser_up && echo OPEN || echo 'NOT open') after $i s; cache/share: $(a "ls -l /data/data/$PKG/cache/share/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';')"
    if [ -n "$SH_FILE" ]; then mkdir -p $OUT/share; timeout 20 adb pull "/data/data/$PKG/cache/share/$SH_FILE" $OUT/share/year-last-$SH_FILE >/dev/null 2>&1
      log "YEAR share file: $SH_FILE $(stat -c %s $OUT/share/year-last-$SH_FILE 2>/dev/null) B; $(python3 -c 'import sys; from PIL import Image; i=Image.open(sys.argv[1]); print(i.format, i.size, i.mode)' $OUT/share/year-last-$SH_FILE 2>&1 | tail -1)"; fi
    chooser_up && share_pick_helper year
    [ "$(ystate)" = open ] && back 2
  fi
  sec "YU5. Dark theme: the last slide"
  a cmd uimode night yes; sleep 3
  if year_open_fresh 25; then for i in 1 2 3 4 5 6 7; do a input tap $((W*88/100)) $((H/2)); sleep 0.5; done; sleep 2.5; eshot yd-last-dark >/dev/null
    for i in 1 2 3 4 5 6; do a input tap $((W*12/100)) $((H/2)); sleep 0.5; done; sleep 2.5; eshot yd-second-dark >/dev/null; back 2; fi
  a cmd uimode night no; sleep 2
  log "YUP part: $(since_fatal yup)"
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- НАСТРОЙКИ: ПЛЪЗГАНЕ, СЕДМИЧЕН ПРЕГЛЕД, РАЗРЕШЕНИЕ, ЧЕСТИТКА
swipe_probe() {  # $1 — new | base: бързо плъзгане надолу от ред в началото на списъка — отваря ли нещо?
  local K=$1 s x1 y1 y2 d r
  open_settings || return 1
  sleep 2; dump; shot swipe/$K-settings-0 >/dev/null
  for s in "25:75:200" "25:75:80" "25:75:600" "30:60:120" "22:80:300"; do
    IFS=: read y1 y2 d <<< "$s"
    a input swipe $((W/2)) $((H*y1/100)) $((W/2)) $((H*y2/100)) $d; sleep 1.8; dump
    if has "$RX_BACK" && has 'text="(Настройки|Settings)"' && ! has 'text="(Затвори|Close|Отказ|Cancel)"'; then r="nothing opened"; else r="OPENED: $(texts 4 | cut -c1-140)"; shot swipe/$K-settings-$y1-$y2-$d >/dev/null; back 1.5; dump; has 'text="(Настройки|Settings)"' || open_settings; fi
    log "SWIPE $K Settings, down from ${y1}% to ${y2}% of the height in $d ms: $r"
  done
  leave
  ready; tab more; sleep 2
  for s in "30:75:200" "30:75:80"; do
    IFS=: read y1 y2 d <<< "$s"
    a input swipe $((W/4)) $((H*y1/100)) $((W/4)) $((H*y2/100)) $d; sleep 1.8; dump
    if has "$RX_BACK"; then r="OPENED: $(texts 4 | cut -c1-140)"; shot swipe/$K-more-$d >/dev/null; back 1.5; else r="nothing opened"; fi
    log "SWIPE $K Още, down from ${y1}% to ${y2}% in $d ms: $r"
  done
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1
  for s in "45:85:200" "45:85:80"; do
    IFS=: read y1 y2 d <<< "$s"
    a input swipe $((W/2)) $((H*y1/100)) $((W/2)) $((H*y2/100)) $d; sleep 2.5; dump
    if has "$RX_BACK"; then r="OPENED: $(texts 4 | cut -c1-140)"; shot swipe/$K-catalog-$d >/dev/null; back 1.5; else r="nothing opened"; fi
    log "SWIPE $K Каталог, down from ${y1}% to ${y2}% in $d ms: $r"
  done
  ready; tab home; sleep 1
}
notif_screen() {  # Настройки › Избери известията → 0, ако екранът е отворен
  open_settings || return 1
  sleep 1.5
  find_slow 'Избери известията|Choose notifications|Choose which' || { log "NOTIF: row 'Избери известията' not found: $(texts 14 | cut -c1-260)"; debug_ui notif-row; return 1; }
  a input tap $P_FOUND; sleep 2.5; dump
}
digest_row() { dump; python3 $WF checkstate $OUT/ui.xml 'Седмичен преглед' | sed 's/^CHECKSTATE //' | cut -c1-90; }
part_ng36() {
  local K p i
  helper_install
  anims 1
  warmup
  dims; mark ng
  sec "NG1. A quick swipe down that starts on a row at the top of a list: does it open the row? 1.2.36, then 1.2.35"
  swipe_probe new
  inst app-base.apk base apk-base-ref.txt && { launch >/dev/null; sleep 6; dismiss; ready; swipe_probe base; }
  inst app.apk new apk-ref.txt; launch >/dev/null; sleep 6; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab home; sleep 3
  sec "NG2. Settings › Избери известията: the weekly digest switch (permission granted)"
  mark ng2
  if notif_screen; then
    shot digest/n-01 >/dev/null; log "NOTIF screen: $(texts 30 | cut -c1-700)"
    if find_slow 'Седмичен преглед|Weekly digest'; then
      shot digest/n-02-row >/dev/null; log "DIGEST row before: $(digest_row); sections row $(has 'Любими раздели|Favourite sections' && echo present || echo absent)"
      tap text 'Седмичен преглед|Weekly digest' >/dev/null; sleep 1.2; shot digest/n-03-tapped >/dev/null; dump
      log "DIGEST after the tap: $(digest_row); fg $(fg); texts: $(texts 40 | grep -oE 'text="[^"]*(недел|Sunday|преглед|digest)[^"]*"' | head -3 | tr '\n' ' ')"
      slow_up; shot digest/n-04-below >/dev/null; dump; log "below the digest row: $(texts 30 | cut -c1-500)"
      if tap text 'Любими раздели|Favourite sections' >/dev/null; then sleep 1.8; shot digest/n-05-sections >/dev/null; dump; log "favourite sections: $(texts 20 | cut -c1-400)"; back 1.5; fi
    else log "DIGEST: row not found on the notifications screen: $(texts 30 | cut -c1-500)"; debug_ui digest-row; fi
    leave
  fi
  sec "NG3. The same switch without the notification permission (revoked, flags cleared): is the system prompt shown?"
  a pm revoke $PKG android.permission.POST_NOTIFICATIONS; a pm clear-permission-flags $PKG android.permission.POST_NOTIFICATIONS user-set user-fixed 2>&1 | head -1
  log "permission now: $(perm_notif)"
  launch >/dev/null; sleep 5; dismiss
  if notif_screen; then
    shot digest/p-01 >/dev/null; log "NOTIF screen without the permission: $(texts 12 | cut -c1-400)"
    if find_slow 'Седмичен преглед|Weekly digest'; then
      log "DIGEST row (no permission): $(digest_row)"
      digest_row | grep -q 'checked=true' && { tap text 'Седмичен преглед|Weekly digest' >/dev/null; sleep 1.5; log "DIGEST switched off first: $(digest_row)"; }
      tap text 'Седмичен преглед|Weekly digest' >/dev/null; sleep 2.5; shot digest/p-02-tapped >/dev/null; dump
      log "after the tap: fg $(fg); $(texts 8 | cut -c1-260)"
      if tap text '=Allow|=Разрешаване|=Разреши' >/dev/null; then sleep 2; dump; log "system prompt → Allow; permission $(perm_notif); DIGEST row: $(digest_row)"; shot digest/p-03-allowed >/dev/null
      else log "NO system permission prompt after the tap; permission $(perm_notif); DIGEST row: $(digest_row)"; fi
    fi
    leave
  fi
  a pm grant $PKG android.permission.POST_NOTIFICATIONS
  if notif_screen && find_slow 'Седмичен преглед|Weekly digest'; then digest_row | grep -q 'checked=true' || { tap text 'Седмичен преглед|Weekly digest' >/dev/null; sleep 1.5; }; log "DIGEST row before the clock jump: $(digest_row)"; fi
  leave
  sec "NG4. Digest notification: the clock jumps to Sunday 6 Dec 2026 18:30 (also Никулден), the periodic worker runs"
  a settings put global auto_time 0; a settings put global auto_time_zone 0
  log "device date before: $(a date | tr -d '\r'); tz $(a getprop persist.sys.timezone | tr -d '\r')"
  a date 120618302026.00 >/dev/null 2>&1; a am broadcast -a android.intent.action.TIME_SET >/dev/null 2>&1; sleep 3
  log "device date now: $(a date | tr -d '\r')"
  mark ng4
  go_home 2
  log "jobs: $(app_jobs)"; run_jobs; sleep 40
  notifs digest | cut -c1-700 | tee -a $SUM
  shade_expand 'Тази седмица|This week|седмица|Седмичен' digest || { log "DIGEST: not in the shade"; run_jobs; sleep 30; notifs digest2 | cut -c1-500 | tee -a $SUM; a cmd statusbar expand-notifications; sleep 2; shot notif/digest-shade-2 >/dev/null; }
  a cmd statusbar collapse; go_home 2
  log "DIGEST logcat: $(since ng4 'WeeklyDigest|digest|PeriodicSync' | head -6 | cut -c1-220 | tr '\n' '|'); $(since_fatal ng4)"
  sec "NG5. Никулден (6 Dec): the greeting on Home, „Честитка“, the composer, share as an image"
  mark ng5
  a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 8; dismiss; ready; tab home; sleep 4
  shot greet/01-home >/dev/null; dump; log "HOME on 6 Dec: $(texts 14 | cut -c1-500)"
  sw_up 500; sleep 1.5; shot greet/02-home-2 >/dev/null; top_of; sleep 1; dump
  p=$(xy text 'Честитка|Greeting card')
  if [ -n "$p" ]; then
    a input tap $p; sleep 3; shot greet/03-composer >/dev/null; dump; log "GREETING composer: $(texts 30 | cut -c1-700)"
    p=$(xy --all class EditText | sed -n 1p); [ -n "$p" ] && { a input tap $p; sleep 1; a input text "Maria"; sleep 1; hide_ime; sleep 1; shot greet/04-name >/dev/null; }
    slow_up; shot greet/05-composer-2 >/dev/null; dump; log "GREETING composer (scrolled): $(texts 30 | cut -c1-500)"
    a "rm -rf /data/data/$PKG/cache/share"; mark sh-greet
    if tap text 'Сподели картичката|Share the card' >/dev/null; then
      for i in $(seq 1 20); do sleep 1; chooser_up && break; done
      shot greet/06-chooser >/dev/null
      SH_FILE=$(a "ls -t /data/data/$PKG/cache/share/ 2>/dev/null" | tr -d '\r' | head -1)
      log "GREETING share: chooser $(chooser_up && echo OPEN || echo 'NOT open') after $i s; fg $(fg); file ${SH_FILE:-none}"
      [ -n "$SH_FILE" ] && { mkdir -p $OUT/share; timeout 20 adb exec-out "run-as $PKG cat cache/share/$SH_FILE" > $OUT/share/greeting-$SH_FILE 2>/dev/null; [ -s $OUT/share/greeting-$SH_FILE ] || timeout 20 adb pull "/data/data/$PKG/cache/share/$SH_FILE" $OUT/share/greeting-$SH_FILE >/dev/null 2>&1; log "GREETING file: $(stat -c %s $OUT/share/greeting-$SH_FILE 2>/dev/null) B; $(python3 -c 'import sys; from PIL import Image; i=Image.open(sys.argv[1]); print(i.format, i.size, i.mode)' $OUT/share/greeting-$SH_FILE 2>&1 | tail -1)"; }
      chooser_up && share_pick_helper greet
    else log "GREETING: no share button: $(texts 20 | cut -c1-300)"; fi
    leave
  else log "GREETING: no „Честитка“ on Home on 6 Dec: $(texts 20 | cut -c1-400)"; debug_ui greet; fi
  a cmd uimode night yes; sleep 4; ready; tab home; sleep 3; shot greet/07-home-dark >/dev/null; a cmd uimode night no; sleep 2
  log "NG part: $(since_fatal ng); FATAL lines in the whole log: $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- УИДЖЕТИТЕ ОТ НАСТРОЙКИ
pin_from_settings() {  # $1 — текст на бутона, $2 — етикет
  a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 5; dismiss
  open_settings || return 1
  sleep 1.5
  find_slow "$1" || { log "PIN $2: button '$1' not found in Settings: $(texts 14 | cut -c1-240)"; debug_ui pin-$2; return 1; }
  shot widget/s-$2-button >/dev/null
  a input tap $P_FOUND; pin_dialog $2
}
part_wdg36() {
  local p i t
  helper_install
  anims 1
  warmup
  card_open >/dev/null; leave
  mark wdg
  sec "WD1. The main widget from Settings › Уиджети › „Добави уиджета „Читалище Яворец““"
  pin_from_settings 'Добави уиджета|Add the .* widget|Add the widget' main
  go_home 3; log "HOME after the main pin: $(wstate)"; shot widget/10-main-on-home >/dev/null
  wcount main >/dev/null; log "widgets of ours bound: $(wids)"
  sec "WD2. „Книга на деня“ from Settings"
  pin_from_settings 'Добави „Книга на деня“|Книга на деня|Book of the day' book
  go_home 3; log "HOME after both pins: $(wstate)"; shot widget/20-both-on-home >/dev/null
  wcount both >/dev/null; log "widgets of ours bound: $(wids)"; grep -E "provider=.*org.chyavorec.app" $OUT/appwidget-both.txt | sort | uniq -c | head -4 | cut -c1-200 | tee -a $SUM
  grep -B2 -A6 -E 'provider.*org\.chyavorec\.app' $OUT/appwidget-both.txt | grep -oE 'cmp=[^ }]+|widgetCategory=[0-9]+' | sort -u | tr '\n' ' ' | cut -c1-400 | tee -a $SUM; echo >> $SUM
  sec "WD3. After the catalogue is loaded; dark theme (10 s, 40 s and 90 s after the switch); light again"
  ready >/dev/null; tab catalog >/dev/null; waitfor "$RX_RESULTS" 100 >/dev/null; go_home 4; shot widget/30-after-catalog >/dev/null; log "WIDGET after the catalogue loaded: $(wstate)"
  a cmd uimode night yes; t0=$(now_ms)
  for t in 10 40 90; do while [ $(( $(now_ms) - t0 )) -lt $((t * 1000)) ]; do sleep 0.5; done; go_home 2; shot widget/31-dark-${t}s >/dev/null; log "WIDGET dark after $t s: $(wstate)"; done
  a cmd uimode night no; sleep 12; go_home 3; shot widget/32-light-again >/dev/null; log "WIDGET light again: $(wstate)"
  sec "WD4. Click-through"
  K=r36; widget_clicks
  go_home 3; dump; p=$(xy resource-id "$PKG:id/widget_book_title"); [ -z "$p" ] && p=$(xy text '=Книга на деня|=Book of the day')
  [ -n "$p" ] && { a input tap $p; sleep 4.5; dump; shot widget/40-book-click >/dev/null; log "Book widget click → fg $(fg): $(texts 8 | cut -c1-220)"; }
  go_home 2
  log "WDG part: $(since_fatal wdg); FATAL lines in the whole log: $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- УИДЖЕТЪТ СЛЕД СМЯНА НА НОЩНИЯ РЕЖИМ (1.2.36 срещу 1.2.35)
wd_night() {  # $1 — new | base
  local K=$1 i t
  inst $(apk_of $K) $K $(ref_of $K) || return 1
  a input keyevent KEYCODE_HOME; sleep 2; launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog >/dev/null; waitfor "$RX_RESULTS" 120 >/dev/null; tab home >/dev/null; sleep 3
  a am force-stop $PKG
  widget_add n-$K || { log "WDN $K: widget not added"; return 1; }
  go_home 3; log "WDN $K home before: $(wstate)"; shot wdn/$K-0-before >/dev/null
  wcount $K-b >/dev/null; log "WDN $K bound before: [$(wids)]"
  mark wdn-$K
  a cmd uimode night yes; t0=$(now_ms)
  for t in 3 10 30; do while [ $(( $(now_ms) - t0 )) -lt $((t * 1000)) ]; do sleep 0.3; done; go_home 1; dump; shot wdn/$K-1-dark-${t}s >/dev/null; log "WDN $K dark +${t}s: widget views $(grep -c "resource-id=\"$PKG:id/" $OUT/ui.xml); bound [$(wcount $K-d$t >/dev/null; wids)]"; done
  log "WDN $K launcher/appwidget log after the switch: $(since wdn-$K 'AppWidget|appwidget|Launcher.*widget|removeAppWidget|deleteAppWidget|onDeleted|unbind' | grep -v PERFCHECK | head -8 | cut -c1-220 | tr '\n' '|')"
  a cmd uimode night no; sleep 12; go_home 3; dump; shot wdn/$K-2-light-again >/dev/null; log "WDN $K light again: widget views $(grep -c "resource-id=\"$PKG:id/" $OUT/ui.xml); bound [$(wcount $K-l >/dev/null; wids)]"
  a dumpsys appwidget 2>/dev/null | tr -d '\r' | grep -E 'provider=.*org\.chyavorec|id=' | head -6 | cut -c1-200 | tee -a $SUM
}
part_wdn36() {
  helper_install
  anims 1
  wd_night base
  wd_night new
  log "WDN part: $(since_fatal wdn); FATAL lines: $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
  all_sheets
  diag_end
}

part_a7() {
  case "$A7_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    tr26|tr28|tr36) part_tr;;
    yup36) part_yup36;;
    ng36) part_ng36;;
    wdg36) part_wdg36;;
    wdn36|wdn36b) part_wdn36;;
  esac
  return 0
}
