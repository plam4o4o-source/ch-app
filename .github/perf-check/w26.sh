# Проверка на кандидата v1.2.39 (ВРЕМЕННО, debug/w26-check; резултати в emulator-results/v139).
# Кандидатът (claude/charming-rubin-3lzor2 @ 696e57db) срещу издаденото v1.2.38 (main f57962bd); prodRelease x86_64
# с временен ключ, без промени в кода на приложението:
#   new  — org.chyavorec.app      — кандидатът 1.2.39 (versionCode 1990200)
#   base — org.chyavorec.app.b38  — 1.2.38 (1990100), инсталирано ЕДНОВРЕМЕННО с new за A/B на едно пускане
#   app-base-upd.apk — 1.2.38 като org.chyavorec.app (същият ключ) — за обновяването 1.2.38 → 1.2.39
# Зарежда се от run.sh след w25.sh и ползва функциите на по-старите части.
#   smk26 / smk33 / smk36x — срив/ANR: обиколка (Начало, разделите, лентата на времето с плъзгача, викторината до 5 от 5,
#                            събитие и „Добави в календара“, „Дари спомен“ с избора на снимки, споделяне на снимка
#                            към приложението, настройки), после тъмна тема, шрифт 2.0, таблет с два панела.
#                            На API 33 — със записи (конфети, листът на събитието, плъзгачът).
#   fr33a / fr33b / fr36x  — време за кадър: кандидат срещу 1.2.38, редуване, 6 повторения (Начало, Още, лента, викторина)
#   upd33                  — 1.2.38 със състояние → `adb install -r` на кандидата (част upd36 от w22.sh)
W26_PARTS=" smk26 smk33 smk36x fr33a fr33b fr36x upd33 "
W20_PARTS="$W20_PARTS $W26_PARTS"
W26=$H_/w26.py
L_DONATE='=Дари спомен|=Donate a memory'
L_DONATE_PICK='=Избери снимки|=Choose photos|=Добави още|=Add more'
RX_DONATE_PHOTO='Махни снимка 1|Remove photo 1'

# ---------------------------------------------------------------- общи (кандидат / 1.2.38 една до друга)
x_pkg() { case $1 in new) echo org.chyavorec.app;; base) echo org.chyavorec.app.b38;; esac; }
x_stop_all() { local k; for k in new base; do a am force-stop $(x_pkg $k) >/dev/null 2>&1; done; }
REC_FPS=10
# rec_cmd от w25.sh, с REC_FPS кадъра/s
rec_cmd() {
  local name=$1 how=$2 secs=$3 f t0 t1 n; shift 3
  mkdir -p $OUT/rec; [ -n "$REC_FILE" ] && rm -f "$REC_FILE"; REC_FILE=""
  case $how in
    emu)
      f=$(realpath -m $OUT/rec/$name.webm); rm -f $f
      timeout 10 adb emu screenrecord start --time-limit $((secs + 6)) $f >/dev/null 2>&1; t0=$(now_ms); sleep 1.5
      a log -t PERFCHECK "$name"; t1=$(now_ms); "$@"
      sleep $secs; timeout 10 adb emu screenrecord stop >/dev/null 2>&1; sleep 2.5;;
    *)
      t0=$(now_ms); a log -t PERFCHECK "$name"; t1=$(now_ms); "$@"; sleep $secs; f="";;
  esac
  REC_OFF=$(( t1 - t0 ))
  if [ -n "$f" ] && [ -s "$f" ]; then
    REC_SS=$(python3 -c "print(max(0, $REC_OFF/1000.0 - 0.5))"); REC_FILE=$f
    n=$(jframes $f $REC_SS $((secs + 1)) $REC_FPS $OUT/frames/$name)
    log "REC $name ($how): marker ${REC_OFF} ms after the recording start; $n frames at $REC_FPS/s from 0.5 s before the marker (the tap itself needs ~0.3-1 s to arrive)"
    jsheets $OUT/frames/$name
  elif [ $how != none ]; then log "REC $name ($how): no recording file"; fi
}
# Конфетите при 5 от 5: кадрите се ПАЗЯТ (frames/<име>), времето от отговора до първото конфети (w26.py cftime)
cf_scan() {
  # lb (границите на надписа на лентата) и n (y на „Напред“) — локалните на quiz_play (динамичен обхват в bash)
  local cfl_t cfl_b cfl_n; cfl_t=$(echo "${lb:-}" | cut -d' ' -f2); cfl_b=$(echo "${lb:-}" | cut -d' ' -f4); cfl_n=${n:-}
  local name=$1 k d t; shift
  [ -d $OUT/frames/$name ] || return 0
  log "CFTIME $name: $(python3 $W26 cftime $OUT/frames/$name $REC_FPS "${cfl_t:-}" "${cfl_b:-}" "${cfl_n:-}" $H 2>&1 | cut -c1-900 | tr '\n' ' ')"
  log "FRAMES $name: $(python3 $W25 confetti $OUT/frames/$name "$@" 2>&1 | cut -c1-500 | tr '\n' ' ')"
  k=$(cat $OUT/frames/$name/peak.txt 2>/dev/null)
  if [ -n "$k" ] && [ "$k" -gt 0 ] && [ -s "$REC_FILE" ]; then
    mkdir -p $OUT/shots/cf
    for d in 0 3 6; do
      t=$(python3 -c "print(max(0, $REC_SS + ($k - 3 + $d)/$REC_FPS))")
      ffmpeg -loglevel error -ss $t -i "$REC_FILE" -frames:v 1 -vf scale=540:-2 $OUT/shots/cf/$name-peak-$d.png 2>/dev/null
    done
  fi
}
w25_since() { :; }   # кандидатът няма дневник W25
sm_cfg() { CFG=$1; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt; mark sm-$CFG; }
sm_end() { log "SMOKE $CFG: $(since_fatal sm-$CFG); app process $(pidof_app)"; }
night() {  # $1 — yes | no (под API 29 — темата на приложението)
  if [ "$SDK" -ge 29 ]; then a cmd uimode night $1; sleep 3
  elif [ $1 = yes ]; then app_theme '=Тъмна|=Dark'; else app_theme '=Както системата|=System default'; fi
}

# ---------------------------------------------------------------- части на обиколката
sm_tabs() {  # $1 — full | brief
  ready; tab home; cap 01-home 3
  [ $1 = full ] && eshot home/$CFG-hero >/dev/null
  sw_up 500; cap 01b-home-2 1.5; [ $1 = full ] && { sw_up 500; cap 01c-home-3 1.5; }; top_of
  ready; tab catalog; waitfor "$RX_RESULTS" 60 >/dev/null; cap 02-catalog 1
  ready; tab my; cap 03-my 2
  ready; tab news; cap 04-news 3
  ready; tab more; cap 05-more 2; sw_up 500; cap 05b-more-2 1.5; top_of
}
tl_years() { dump; python3 $W26 years $OUT/ui.xml; }
# Плъзгачът: кратко превъртане (дръжката се показва), после влачене на дръжката до левия ръб надолу.
# Хванат е, ако първата видима година скочи напред (обикновеното плъзгане надолу връща списъка нагоре).
tl_drag() {
  local b top bot x y off pre post y0 y1 caught=no
  dump; b=$(python3 $W26 scrollable $OUT/ui.xml)
  top=$(echo $b | cut -d' ' -f2); bot=$(echo $b | cut -d' ' -f4)
  [ -n "$top" ] || { log "TL-DRAG $CFG: no scrollable node"; return 1; }
  x=$(( 26 * DENS / 160 ))
  for off in 44 32 58; do
    top_of; sleep 1.6; pre=$(tl_years)
    y=$(( top + off * DENS / 160 ))
    a input swipe $((W/2)) $((H*62/100)) $((W/2)) $((H*54/100)) 200
    ( sleep 1.3; timeout 15 adb shell screencap -p /sdcard/mid.png ) &
    a input swipe $x $y $x $((y + (bot - top) * 45 / 100)) 2400
    wait
    mkdir -p $OUT/shots/tour/$CFG; timeout 20 adb pull /sdcard/mid.png $OUT/shots/tour/$CFG/10c-drag-mid-${off}dp.png >/dev/null 2>&1
    sleep 0.4; post=$(tl_years); shot tour/$CFG/10d-drag-after-${off}dp >/dev/null
    y0=$(echo "$pre" | grep -oE 'first=[0-9]+' | cut -d= -f2); y1=$(echo "$post" | grep -oE 'first=[0-9]+' | cut -d= -f2)
    log "TL-DRAG $CFG thumb at ${off} dp below the list top (x=$x y=$y, list $top..$bot): before [$pre] after [$post]"
    if [ "${y1:-0}" -gt $(( ${y0:-0} + 12 )) ]; then caught=yes; break; fi
  done
  log "TL-DRAG $CFG: scrubber $( [ $caught = yes ] && echo "CAUGHT (list jumped ${y0} → ${y1})" || echo 'not caught by the scripted drag') ; $(since_fatal sm-$CFG)"
}
sm_timeline() {  # $1 — full | brief
  more_tile "$L_TIMELINE" || return 1
  sleep 3; cap 10-timeline 0.5; log "TIMELINE $CFG top: $(tl_years); $(grep -oE '(text|content-desc)="[^"]*"' $OUT/ui.xml | grep -E '[0-9]{4}' | head -8 | tr '\n' ' ' | cut -c1-600)"
  [ $1 = full ] && eshot timeline/$CFG-top >/dev/null
  swipes3; sleep 0.2; shot tour/$CFG/10a-scrolling >/dev/null; cap 10b-timeline-scrolled 1.5
  dump; log "TIMELINE $CFG after 3 swipes: $(python3 $W26 years $OUT/ui.xml); $(grep -oE '(text|content-desc)="[^"]*"' $OUT/ui.xml | grep -E '[0-9]{4}' | head -8 | tr '\n' ' ' | cut -c1-600)"
  [ $1 = full ] && { sw_up 500; sleep 1.5; eshot timeline/$CFG-mid >/dev/null; }
  tl_drag
  leave
}
sm_quiz() {  # $1 — full (до 5 от 5) | brief (един отговор); $2 — запис на петия отговор (emu | none)
  local o
  if [ $1 = full ]; then
    quiz_fresh ""; quiz_play learn learn-$CFG || log "QUIZ $CFG: learn pass incomplete"
    quiz_fresh ""; REC_FPS=20 quiz_play right r5-$CFG ${2:-none} 6 || log "QUIZ $CFG: 5/5 pass incomplete"
    dump; log "QUIZ $CFG after the 5/5 pass: $(texts 10 | cut -c1-300)"
  else
    quiz_open || return 1
    cap 15-quiz 1; dump; o=$(python3 $W24 quizopts $OUT/ui.xml $H $W | grep -m1 '^A' | cut -f1); o=${o#A }
    [ -n "$o" ] && { a input tap $o; cap 15b-quiz-answered 2; }
    leave
  fi
}
open_event_rec() {  # като open_event 1, но докосването се записва отвън (листът „пада“ при отваряне)
  local p
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar
  dump; has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък' >/dev/null; sleep 2; }
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $((H/4)) $((H*85/100)) 150 | sed -n 1p)
  [ -n "$p" ] || { log "no event row: $(texts 8 | cut -c1-200)"; return 1; }
  REC_FPS=20 rec_cmd ev-open-$CFG emu 2 a input tap $p
  sleep 1; dump; has "$RX_BACK"
}
sm_event() {  # $1 — rec | no
  local p i
  if [ "$1" = rec ]; then open_event_rec || return 1; else open_event 1 || return 1; fi
  cap 20-event 1.5
  back 2; dump; log "EVENT $CFG back from the leaf: $(texts 6 | cut -c1-200)"
  open_event 1 || return 1
  p=""; for i in 1 2 3 4; do dump; p=$(xy text '=Добави в календара|=Add to calendar' --maxy $((H*93/100))); [ -n "$p" ] && break; slow_up; done
  if [ -n "$p" ]; then
    mark ev-cal-$CFG; a input tap $p; sleep 3
    chooser_up && { tap text 'PC catcher' >/dev/null; sleep 2; }
    log "CALENDAR $CFG: fg $(fg); helper: $(since ev-cal-$CFG 'PCCATCH' | head -1 | cut -c1-200)"
    shot tour/$CFG/20b-calendar >/dev/null
    back 2; sleep 1; dump
    has "package=\"$PKG\"" || { log "CALENDAR $CFG: app not in front after back: $(fg)"; launch >/dev/null; sleep 3; }
    cap 20c-event-after-calendar 1
    log "EVENT $CFG after the calendar: '$(grep -oE 'text="(Добавено в календара|Отворено в календара|Added to calendar|Opened in calendar)[^"]*"' $OUT/ui.xml | head -1)'; $(texts 10 | cut -c1-240)"
  else log "EVENT $CFG: no 'Добави в календара': $(texts 16 | cut -c1-300)"; debug_ui ev-cal; fi
  leave
}
donate_pick_xy() { local i p=""; for i in 1 2 3 4; do dump; p=$(xy text "$L_DONATE_PICK" --maxy $((H*95/100))); [ -n "$p" ] && break; sw_up 400; sleep 1; done; echo "$p"; }
sm_donate() {  # $1 — full | brief: „Още“ › „Дари спомен“, бутонът отваря избора на снимки, „Назад“
  local p
  more_tile "$L_DONATE" || return 1
  sleep 1.5; cap 30-donate 1
  [ $1 = full ] && { eshot donate/$CFG-empty >/dev/null; sw_up 600; cap 30b-donate-2 1.5; sw_up 600; cap 30c-donate-3 1.5; top_of; }
  dump; log "DONATE $CFG: $(texts 16 | cut -c1-500)"
  p=$(donate_pick_xy)
  if [ -n "$p" ]; then
    mark dn-$CFG; a input tap $p; sleep 4
    log "DONATE $CFG picker: fg $(fg); $(dump; texts 6 | cut -c1-200)"
    shot tour/$CFG/30d-picker >/dev/null
    back 2; sleep 1.5; dump
    has "package=\"$PKG\"" || { back 2; dump; }
    log "DONATE $CFG after cancelling the picker: front $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)"); donate screen $(has 'Дари спомен' && echo yes || echo no); photo $(has "$RX_DONATE_PHOTO" && echo YES || echo none); $(since_fatal dn-$CFG)"
    cap 30e-after-cancel 0.5
  else log "DONATE $CFG: no 'Избери снимки' button"; debug_ui donate; fi
  leave
}
SHARE_URI=""
share_prep() {  # тестова снимка в MediaStore → SHARE_URI (content://media/external/images/media/<id>)
  local i id
  python3 $W26 jpeg $OUT/w26-spomen.jpg >/dev/null 2>&1 || { log "SHARE: no test JPEG (PIL?)"; return 1; }
  a mkdir -p /sdcard/Pictures; timeout 30 adb push $OUT/w26-spomen.jpg /sdcard/Pictures/w26-spomen.jpg >/dev/null 2>&1
  a am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///sdcard/Pictures/w26-spomen.jpg >/dev/null 2>&1
  for i in 1 2 3 4 5 6 7 8; do
    id=$(a content query --uri content://media/external/images/media --projection _id:_display_name 2>/dev/null | tr -d '\r' | grep 'w26-spomen' | grep -oE '_id=[0-9]+' | head -1 | cut -d= -f2)
    [ -n "$id" ] && break; sleep 2
  done
  [ -n "$id" ] || { log "SHARE: the test photo is not in MediaStore: $(a content query --uri content://media/external/images/media --projection _id:_display_name 2>&1 | tr -d '\r' | tail -3 | tr '\n' ' ' | cut -c1-300)"; return 1; }
  SHARE_URI=content://media/external/images/media/$id
  log "SHARE: test photo $(stat -c %s $OUT/w26-spomen.jpg) B → $SHARE_URI"
}
share_check() {  # $1 — етикет: на екрана ли е „Дари спомен“ със снимката
  local i
  for i in 1 2 3 4 5 6; do dump; has "$RX_DONATE_PHOTO" && break; sleep 1.5; done
  cap 31-shared-$1 0.5
  log "SHARE $1: front $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)"); donate screen $(has 'Дари спомен' && echo yes || echo no); PHOTO $(has "$RX_DONATE_PHOTO" && echo SHOWN || echo none); count '$(grep -oE 'text="[0-9]+ от [0-9]+"' $OUT/ui.xml | head -1)'; $(texts 12 | cut -c1-400); $(since_fatal sh-$1)"
}
sm_share() {  # $1 — full | brief: ACTION_SEND image/jpeg към приложението (студено и топло)
  local r
  [ -n "$SHARE_URI" ] || share_prep || return 1
  sec "SH-$CFG. Sharing a photo to the app (ACTION_SEND image/jpeg, EXTRA_STREAM $SHARE_URI)"
  # 1) adb: am start с право за четене (студено — процесът спрян)
  a am force-stop $PKG; sleep 1; mark sh-$CFG-am
  r=$(a am start -W -a android.intent.action.SEND -t image/jpeg --eu android.intent.extra.STREAM "$SHARE_URI" --grant-read-uri-permission -n $ACT 2>&1 | tr -d '\r' | grep -E 'Status|Error|Exception|Denial|TotalTime' | tr '\n' ' ' | cut -c1-300)
  log "SHARE am start (cold, --grant-read-uri-permission): $r"
  sleep 4; share_check $CFG-am
  [ $1 = full ] && eshot donate/$CFG-shared-am >/dev/null
  leave
  # 2) помощното приложение като галерия (ClipData + FLAG_GRANT_READ_URI_PERMISSION), приложението вече отворено
  ready >/dev/null; tab home >/dev/null; sleep 2; mark sh-$CFG-helper
  a am start -n org.pc.helper/.Send --es uri "$SHARE_URI" --es pkg $PKG >/dev/null 2>&1; sleep 5
  log "SHARE helper: $(since sh-$CFG-helper PCSEND | tail -1 | sed 's/.*PCSEND *: //' | cut -c1-300)"
  share_check $CFG-helper
  [ $1 = full ] && { eshot donate/$CFG-shared-helper >/dev/null; sw_up 600; cap 31b-shared-scrolled 1.5; }
  # 3) същото без право за четене — приложението не бива да падне
  if [ $1 = full ]; then
    leave; mark sh-$CFG-nogrant
    r=$(a am start -W -a android.intent.action.SEND -t image/jpeg --eu android.intent.extra.STREAM "$SHARE_URI" -n $ACT 2>&1 | tr -d '\r' | grep -E 'Status|Error|Exception' | tr '\n' ' ' | cut -c1-200)
    log "SHARE am start WITHOUT a read grant (warm): $r"; sleep 4; share_check $CFG-nogrant
  fi
  leave
}
sm_settings() {
  if open_settings; then cap 40-settings 1; sw_up 600; cap 40b-settings-2 1.5; sw_up 600; cap 40c-settings-3 1.5; back 1.5; fi
  leave
}
sm_tablet() {  # два панела в „Още“/„Моето“ (wm size 2560x1600, 320 dpi), „Дари спомен“ и лентата в панела
  a wm size 2560x1600; a wm density 320; sleep 4
  sm_cfg tablet-landscape; restart; dims
  sec "TB-$CFG. Tablet two-pane ($(cfg_desc))"
  hub_tablet tab-l
  sm_cfg tablet-landscape-b
  more_tile "$L_DONATE" && { sleep 2; cap 50-tablet-donate 1; dump; log "TABLET donate pane: back arrow $(has "$RX_BACK" && echo yes || echo no); tabs $(has 'text="(Начало|Home)"' && echo visible || echo hidden); $(texts 10 | cut -c1-240)"; leave; }
  more_tile "$L_TIMELINE" && { sleep 3; cap 51-tablet-timeline 1; swipes3; cap 51b-tablet-timeline-scrolled 1.5; leave; }
  sm_end
  a wm size reset; a wm density reset; sleep 4; dims
}

# ---------------------------------------------------------------- 1. СРИВ / ANR (обиколка)
part_smk() {
  local how=none
  [ $PART = smk33 ] && how=emu
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || log "device locale not changed (labels may be English)"; }
  a am force-stop $PKG
  anims 1
  mark smk
  warmup
  sm_cfg light; restart; dims
  sec "S1-$CFG. Main screens, timeline (scroll + scrubber drag), quiz to 5/5, event, „Дари спомен“, share, settings ($(cfg_desc))"
  sm_tabs full
  sm_timeline full
  sm_quiz full $how
  sm_event $( [ $how = emu ] && echo rec || echo no )
  sm_donate full
  sm_share full
  sm_settings
  sm_end
  sm_cfg dark; night yes; restart; dims
  sec "S2-$CFG. Dark theme ($(cfg_desc))"
  sm_tabs brief; [ $PART = smk33 ] && { ready; tab home; sleep 2; eshot home/dark-hero >/dev/null; }
  sm_timeline brief; sm_quiz brief; sm_donate brief; sm_settings
  sm_end
  night no
  sm_cfg font200; a settings put system font_scale 2.0; sleep 3; restart; dims
  sec "S3-$CFG. Font scale 2.0 ($(cfg_desc))"
  sm_tabs brief; sm_timeline brief; sm_quiz brief; sm_donate brief; sm_share brief; sm_settings
  sm_end
  a settings put system font_scale 1.0; sleep 2
  if [ $PART = smk33 ]; then
    sm_cfg font130; a settings put system font_scale 1.3; sleep 3; restart; dims
    sec "S3b-$CFG. Font scale 1.3: the timeline medallions ($(cfg_desc))"
    more_tile "$L_TIMELINE" && { sleep 3; cap 10-timeline 0.5; eshot timeline/font130-top >/dev/null; swipes3; cap 10b-timeline-scrolled 1.5; eshot timeline/font130-scrolled >/dev/null; leave; }
    sm_end
    a settings put system font_scale 1.0; sleep 2
  fi
  sm_tablet
  sec "S9. Crashes and ANRs of the whole part"
  log "SMOKE TOTAL ($PART, API $SDK): $(since_fatal smk); FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt), app FATAL $(grep -c 'Process: org\.chyavorec\.app' $OUT/logcat-live.txt), ANR $(grep -c 'ANR in ' $OUT/logcat-live.txt)"
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -30 | tee -a $SUM
  all_sheets
}

# ---------------------------------------------------------------- 2. ВРЕМЕ ЗА КАДЪР (кандидат срещу 1.2.38)
part_fr26() {
  local r K order
  sec "F0. The candidate 1.2.39 (org.chyavorec.app) and 1.2.38 (org.chyavorec.app.b38) side by side, both compiled speed-profile"
  fr_setup new base
  sec "F1. Warm-up (rep 0, not counted): every scenario once; the quiz taps are learned"
  for K in new base; do fr_rep $K $K 0 "" "home more timeline quiz"; done
  for r in 1 2 3 4 5 6; do
    if [ $(( r % 2 )) = 1 ]; then order="new base"; else order="base new"; fi
    [ $PART = fr33b ] && { [ "$order" = "new base" ] && order="base new" || order="new base"; }
    sec "F2. Repetition $r, order: $order (a fresh process for each; Home, Още, timeline, quiz)"
    for K in $order; do fr_rep $K $K $r "" "home more timeline quiz"; done
  done
  sec "F3. Totals (rep 0 is excluded; new = 1.2.39 candidate, base = 1.2.38)"
  python3 $W25 fsagg $OUT/frames.csv $OUT/frames.md base | tee -a $SUM
  x_use new
  log "FR part: FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt), ANR $(grep -c 'ANR in ' $OUT/logcat-live.txt); host load: $(cut -d' ' -f1-3 /proc/loadavg)"
}

# ---------------------------------------------------------------- 3. ОБНОВЯВАНЕ 1.2.38 → кандидата
part_upd26() {
  [ -f app-base-upd.apk ] || { log "UPD: app-base-upd.apk missing"; return 1; }
  cp app-base-upd.apk app-base.apk; cp apk-base-upd-ref.txt apk-base-ref.txt
  log "UPD: 1.2.38 = $(cat apk-base-ref.txt) → the candidate $(cat apk-ref.txt) (section titles below come from part_upd36: „1.2.35“ = 1.2.38, „1.2.36“ = 1.2.39)"
  part_upd36
}

# ---------------------------------------------------------------- маршрутизация
part_w26() {
  case "$W26_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    smk26|smk33|smk36x) part_smk;;
    fr33a|fr33b|fr36x) part_fr26;;
    upd33) part_upd26;;
  esac
  return 0
}
