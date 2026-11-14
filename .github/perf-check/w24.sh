# Проверка на кандидата v1.2.38 (ВРЕМЕННО, debug/w24-check; резултати в emulator-results/v138).
# Кандидатът v1.2.38 (вълна 23, claude/charming-rubin-3lzor2) срещу издаденото v1.2.37 (main); и двете prodRelease
# x86_64 с временен ключ; devDebug от двете (общ debug ключ) за демо входа. Зарежда се от run.sh след a7.sh и ползва
# функциите на по-старите части. „new“ = 1.2.38, „base“ = 1.2.37.
#   tour38   — обиколка на всички екрани + новите (светло подробно, тъмно)
#   tour38b  — същото: шрифт 1.3, 320 dp, таблет (два панела в „Още“/„Моето“), таблет хоризонтално
#   ui38     — иконите „Бод“ (светло/тъмно), „Моето“ като гост, вход, викторината (5 от 5, грешен отговор),
#              лентата на времето (плъзгачът при превъртане), листът на събитието (откъсването), сезонният конец
#              (3 март, 25 декември, Великден), търсене, настройки
#   dev38    — devDebug: вход и „Картата е издадена“, „Моето“ с вход, удължаване, съобщения, дневник, годината,
#              картата през декември (лентата „Твоята шевица“), два панела на таблет
#   mon38    — monkey 2 × 6000 (1357, 4242)
#   upd38    — 1.2.37 със състояние → `adb install -r` 1.2.38 (както upd36)
#   updd38   — devDebug 1.2.37 с вход, дневник, „Искам да прочета“ → devDebug 1.2.38
#   dm33/dm28 — самообновяването с .dm: контролна сесия (pm), после през приложението от локален HTTPS сървър:
#              верен .dm, грешна сума, повреден .dm (сумата вярна), .dm от 1.2.37; dumpsys package dexopt
#   start38a/b — студен старт A/B (n=20 на издание във всяка)
#   perf38/perfb38 — покой (Начало ~45 s, картата, Още, Моето, викторината, лентата), плавност, памет след обиколки
W24_PARTS=" ui38b dev38b tour38 tour38b ui38 dev38 mon38 upd38 updd38 dm33 dm28 start38a start38b perf38 perfb38 "
W20_PARTS="$W20_PARTS $W24_PARTS"
W24=$H_/w24.py
VC_NEW=${VC_NEW:-1507400}

# ---------------------------------------------------------------- общи
# Запис отвън (емулаторът — и при FLAG_SECURE): $1 — име, $2 — секунди, $3… — командата след началото → shots/erec/
erec() {
  local name=$1 secs=$2 d f n x; shift 2
  d=$(realpath -m $OUT/rec); mkdir -p $d $OUT/shots/erec; f=$d/$name.webm; rm -f $f
  timeout 10 adb emu screenrecord start --time-limit $secs $f >/dev/null 2>&1
  sleep 0.8
  "$@"
  sleep $secs; timeout 10 adb emu screenrecord stop >/dev/null 2>&1; sleep 2.5
  if [ -s $f ]; then
    rm -rf $d/$name $d/pick; mkdir -p $d/$name
    ffmpeg -loglevel error -i $f -vf fps=10 $d/$name/f%03d.png
    n=$(ls $d/$name 2>/dev/null | wc -l)
    log "EREC $name: $n frames; $(python3 $W24 frames $d/$name 2>&1 | tail -1); $(python3 $A7 pick $d/pick 12 $d/$name/f*.png 2>&1 | cut -c1-200)"
    for x in $d/pick/*.png; do [ -f "$x" ] && mv "$x" $OUT/shots/erec/$name-$(basename $x); done
    rm -rf $d/$name $d/pick $f
  else
    log "EREC $name: emulator recording gave no file → emulator screenshots"
    for x in 1 2 3 4; do eshot erec/$name-burst-$x >/dev/null; done
  fi
}
# Запис отвътре (screenrecord), около произволна команда: $1 — име, $2… — командата → shots/tr/<име>-NN
trec_do() {
  local name=$1 n; shift
  mkdir -p $OUT/rec $OUT/shots/tr
  a rm -f /sdcard/rec.mp4
  adb shell screenrecord --time-limit 7 --size 540x1200 --bit-rate 3000000 /sdcard/rec.mp4 >/dev/null 2>&1 &
  RECPID=$!; sleep 1.6
  "$@"
  wait $RECPID 2>/dev/null; sleep 1
  rm -rf $OUT/rec/$name; mkdir -p $OUT/rec/$name
  timeout 30 adb pull /sdcard/rec.mp4 $OUT/rec/$name.mp4 >/dev/null 2>&1
  [ -s $OUT/rec/$name.mp4 ] && ffmpeg -loglevel error -i $OUT/rec/$name.mp4 -vf fps=10 $OUT/rec/$name/f%03d.png
  n=$(ls $OUT/rec/$name 2>/dev/null | wc -l)
  if [ "$n" = 0 ]; then log "TREC $name: screenrecord gave no frames"; rm -f $OUT/rec/$name.mp4; return 1; fi
  rm -rf $OUT/rec/pick; log "TREC $name: $n frames; $(python3 $W24 frames $OUT/rec/$name 2>&1 | tail -1); $(python3 $A7 pick $OUT/rec/pick 12 $OUT/rec/$name/f*.png | cut -c1-200)"
  for f in $OUT/rec/pick/*.png; do [ -f "$f" ] && mv "$f" $OUT/shots/tr/$name-$(basename $f); done
  rm -rf $OUT/rec/$name $OUT/rec/$name.mp4 $OUT/rec/pick
}
swipes3() { local i; for i in 1 2 3; do a input swipe $((W/2)) $((H*75/100)) $((W/2)) $((H*35/100)) 700; sleep 0.6; done; }
dexopt_full() {  # редът на пакета от dumpsys package dexopt (status, reason)
  a dumpsys package dexopt 2>/dev/null | tr -d '\r' | grep -A8 "^ *\[$PKG\]" | grep -E 'status=|path:|reason' | tr -s ' ' | head -4 | tr '\n' '|'
}
profiles() { a "ls -l /data/misc/profiles/ref/$PKG/ /data/misc/profiles/cur/0/$PKG/ 2>&1; ls -l \$(dirname \$(pm path $PKG | head -1 | cut -d: -f2)) 2>&1" | tr -d '\r' | grep -vE '^total|^$' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';' | cut -c1-400; }
set_date() {  # $1 — MMDDhhmmYYYY.ss
  a settings put global auto_time 0; a settings put global auto_time_zone 0
  a date $1 >/dev/null 2>&1; a am broadcast -a android.intent.action.TIME_SET >/dev/null 2>&1; sleep 2
  log "device date now: $(a date | tr -d '\r')"
}
quiz_pass() {  # $1 — 1: първите отговори (научава верните), 2: верните (5 от 5); $2 — префикс на снимките
  local P=$1 X=$2 i o want right n j
  more_tile "$L_QUIZ" || return 1
  waitfor 'Въпрос [0-9]+ от|Question [0-9]+ of|Днес няма викторина|No quiz today|Нови въпроси' 45 && log "QUIZ$P ready after ${WAITED} ms: $(texts 8 | cut -c1-200)" || { log "QUIZ$P: no question in 45 s: $(texts 8 | cut -c1-200)"; debug_ui quiz$P; return 1; }
  cap $X-q0 1
  [ $P = 1 ] && : > $OUT/quiz-answers.txt
  for i in 1 2 3 4 5; do
    dump; python3 $W24 quizopts $OUT/ui.xml $H $W > $OUT/qo.txt
    grep -q '^A' $OUT/qo.txt || { sw_up 400; sleep 1; dump; python3 $W24 quizopts $OUT/ui.xml $H $W > $OUT/qo.txt; }
    log "QUIZ$P q$i options: $(grep '^A' $OUT/qo.txt | cut -f2 | tr '\n' '|' | cut -c1-260)"
    o=""
    if [ $P = 2 ]; then want=$(sed -n ${i}p $OUT/quiz-answers.txt); o=$(awk -F'\t' -v w="$want" '$1 ~ /^A / && $2 == w {print $1; exit}' $OUT/qo.txt); [ -z "$o" ] && o=$(awk -F'\t' -v w="$want" '$1 ~ /^A / && index($2, w) {print $1; exit}' $OUT/qo.txt); fi
    [ -z "$o" ] && o=$(grep -m1 '^A' $OUT/qo.txt | cut -f1)
    [ -n "$o" ] || { log "QUIZ$P q$i: no answer buttons: $(texts 12 | cut -c1-260)"; debug_ui quiz$P-$i; break; }
    o=${o#A }
    if [ $P = 2 ] && [ $i = 5 ] && [ "${QUIZ_REC:-}" = win ]; then recwin quiz-5of5 0.6 2.4 8 adb a input tap $o; elif [ $P = 2 ] && [ $i = 5 ]; then trec_do quiz-band-5of5 a input tap $o; else a input tap $o; sleep 0.5; shot quiz/$X-q$i-a >/dev/null; sleep 1.2; fi
    cap $X-q$i-answered 0.3
    right=$(python3 $W24 quizres $OUT/ui.xml)
    log "QUIZ$P q$i: $right; band: $(grep -oE '(text|content-desc)="[^"]*(Избродирай|Шевицата е цяла|верни от)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' ')"
    if [ $P = 1 ]; then
      case "$right" in RIGHT*) awk -F'\t' -v c="A $o" '$1 == c {print $2; exit}' $OUT/qo.txt >> $OUT/quiz-answers.txt;; *) echo "$right" | cut -f2 >> $OUT/quiz-answers.txt;; esac
    fi
    n=""
    for j in 1 2 3; do dump; n=$(python3 $W24 quizopts $OUT/ui.xml $H $W | awk '$1=="N"{print $2, $3}'); [ -n "$n" ] && break; sw_up 300; sleep 1; done
    [ -n "$n" ] || { log "QUIZ$P q$i: no next button: $(texts 12 | cut -c1-260)"; break; }
    if [ $i = 5 ]; then trec_do quiz-result-$P a input tap $n; else a input tap $n; sleep 1.6; fi
  done
  sleep 1.5; cap $X-result 0.5
  log "QUIZ$P result: $(texts 14 | cut -c1-400)"
  [ $P = 1 ] && log "QUIZ answers learned: $(tr '\n' '|' < $OUT/quiz-answers.txt | cut -c1-300)"
  leave
}

# ---------------------------------------------------------------- обиколки
hub_tablet() {  # $1 — етикет: два панела в „Още“ и „Моето“
  local p t
  ready; tab more; sleep 2.5; cap $1-more 0.5
  for t in "$L_QUIZ" "$L_TIMELINE" '=Галерия|=Gallery' '=Настройки|=Settings'; do
    dump; p=$(xy text "$t"); [ -n "$p" ] || { sw_up 300; sleep 1; dump; p=$(xy text "$t"); }
    [ -n "$p" ] && { a input tap $p; sleep 3; cap $1-more-$(echo "$t" | tr -dc 'a-zA-Z' | cut -c1-12) 0.5; log "HUB $1 after '$t': back arrow $(has "$RX_BACK" && echo yes || echo no); tabs $(has 'text="(Начало|Home)"' && echo visible || echo hidden)"; }
  done
  leave; ready; tab my; sleep 2.5; cap $1-my 0.5
  for t in '=Искам да прочета|=Want to read' '=Читателска карта|=Library card'; do
    dump; p=$(xy text "$t"); [ -n "$p" ] && { a input tap $p; sleep 3; cap $1-my-$(echo "$t" | tr -dc 'a-zA-Z' | cut -c1-12) 0.5; eshot tablet/$1-my-$(echo "$t" | tr -dc 'a-zA-Z' | cut -c1-12) >/dev/null; }
  done
  leave
}
part_tour38() {
  helper_install
  a am force-stop $PKG
  anims 1
  warmup
  if [ $PART = tour38 ]; then
    tour bg-light; new_pass bg-light full
    a cmd uimode night yes; sleep 3; tour bg-dark; new_pass bg-dark brief; a cmd uimode night no; sleep 2
  else
    a settings put system font_scale 1.3; sleep 3; tour font130; new_pass font130 brief; a settings put system font_scale 1.0; sleep 2
    a wm density 540; sleep 4; tour w320dp; new_pass w320dp brief; a wm density reset; sleep 3
    a wm size 1600x2560; a wm density 320; sleep 4; CFG=tablet-portrait; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt; restart; hub_tablet tab-p; new_pass tablet-portrait brief
    a wm size 2560x1600; sleep 4; rot 0; CFG=tablet-landscape; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt; restart; dims; hub_tablet tab-l; new_pass tablet-landscape brief
    a cmd uimode night yes; sleep 3; CFG=tablet-landscape-dark; : > $OUT/a11y-$CFG.txt; restart; dims; hub_tablet tab-ld; a cmd uimode night no
    a wm size reset; a wm density reset; sleep 3
  fi
  sec "TA. Accessibility over the tours"
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -40 | tee -a $SUM
  log "TOUR38 part: FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt), app FATAL $(grep -c 'Process: org\.chyavorec\.app' $OUT/logcat-live.txt), ANR $(grep -c 'ANR in ' $OUT/logcat-live.txt)"
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- новият облик на устройството (prodRelease)
season_shots() {  # $1 — етикет, $2 — дата за `date`
  set_date $2
  a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 7; dismiss; ready >/dev/null
  CFG=season-$1; DENS=$(dens); : > $OUT/a11y-$CFG.txt
  tab home; cap home 3; sw_up 500; cap home-2 1.5; sw_up 500; cap home-3 1.5; top_of
  ready; tab more; cap more 2; ready; tab my; cap my 2
  dump; log "SEASON $1: greeting/feast texts: $(grep -oE '(text|content-desc)="[^"]*(Честит|Рождество|Коледа|Великден|Баба Марта|Христос)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' ')"
}
part_ui38() {
  local mode t i p D0
  helper_install
  a am force-stop $PKG
  anims 1
  warmup
  mark u38
  sec "U1. Tabs with the „Бод“ icons: every tab, light and dark (fresh install, no card)"
  for mode in light dark; do
    [ $mode = dark ] && { a cmd uimode night yes; sleep 3; }
    CFG=tabs-$mode; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt
    for t in home catalog my news more; do ready >/dev/null; tab $t >/dev/null; sleep 2.5; cap $t 0.5; done
    ready; tab more; sw_up 500; cap more-2 1.5; sw_up 500; cap more-3 1.5; top_of
    ready; tab my; sw_up 500; cap my-2 1.5; top_of
    dump; log "MY guest ($mode): sample card hint $(has 'Така ще изглежда картата ти|This is how your card will look' && echo SHOWN || echo absent); $(texts 14 | cut -c1-300)"
    [ $mode = dark ] && { a cmd uimode night no; sleep 2; }
  done
  CFG=ui38; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt
  sec "U2. Login screen (guest), the sample card on it"
  ready; tab my; sleep 2
  if tap text "$L_SIGNIN" >/dev/null; then sleep 3; eshot ui/login-guest >/dev/null; hide_ime; sleep 1; eshot ui/login-guest-noime >/dev/null; dump; log "LOGIN: $(texts 14 | cut -c1-300)"; back 2; fi
  sec "U3. Quiz: pass 1 (first options: right/wrong marks, the band), then data cleared and pass 2 with the learned answers (5/5, confetti)"
  quiz_pass 1 qz1
  a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; a cmd locale set-app-locales $PKG --locales bg-BG; no_camera >/dev/null
  launch >/dev/null; sleep 6; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab news; sleep 4; tab home; sleep 3
  quiz_pass 2 qz2
  sec "U4. Timeline: the decade scrubber while scrolling (recorded), after the scroll stops (0, 1.5, 4 s)"
  if more_tile "$L_TIMELINE"; then
    sleep 3; cap tl-00 0.5
    trec_do timeline-scroll swipes3
    shot ui/tl-after-0s >/dev/null; sleep 1.5; shot ui/tl-after-1.5s >/dev/null; sleep 2.5; shot ui/tl-after-4s >/dev/null
    dump; log "TIMELINE decades: $(grep -oE '(text|content-desc)="[^"]*[0-9]{4}-те[^"]*"' $OUT/ui.xml | head -6 | tr '\n' ' ')"
    a input swipe $((W/2)) $((H*30/100)) $((W/2)) $((H*85/100)) 120; sleep 0.3; shot ui/tl-fling-down >/dev/null
    leave
  fi
  sec "U5. Event leaf: add to calendar (the helper catches the intent), back → the stub tears off (recorded)"
  if open_event 1; then
    cap ev-01 2
    p=""; for i in 1 2 3 4; do dump; p=$(xy text '=Добави в календара|=Add to calendar' --maxy $((H*93/100))); [ -n "$p" ] && break; slow_up; done
    if [ -n "$p" ]; then
      cap ev-02-button 0.5; mark ev-cal; a input tap $p; sleep 3
      chooser_up && { tap text 'PC catcher' >/dev/null; sleep 2; }
      log "CALENDAR: fg $(fg); helper: $(since ev-cal 'PCCATCH' | head -1 | cut -c1-260)"
      shot ui/ev-03-calendar >/dev/null
      top_of
      trec_do event-tear a input keyevent KEYCODE_BACK
      sleep 1; cap ev-04-after 0.5; dump; log "EVENT after return: '$(grep -oE 'text="(Добавено в календара|Added to calendar)"' $OUT/ui.xml | head -1)'; $(texts 10 | cut -c1-240)"
      slow_up; cap ev-05-after-2 0.5
    else log "EVENT: no 'Добави в календара': $(texts 16 | cut -c1-300)"; debug_ui ev-cal; fi
    leave
  fi
  sec "U6. Search (snippets/plates), settings in cards, the end strip"
  ready; tab home; sleep 2
  ready; tab more; sleep 2
  dump; if tap content-desc '=Търсене|=Search' >/dev/null; then
    sleep 2; a input text 2025; sleep 5; hide_ime; cap search-2025 1; sw_up 500; cap search-2025-b 1.5
    dump; log "SEARCH 2025: $(texts 24 | cut -c1-400)"; back 1.5; back 1.5
  else log "SEARCH: no search icon in Още: $(texts 14 | cut -c1-260)"; fi
  ready; tab home; sleep 2
  if tap text "$L_HOMESEARCH" >/dev/null; then sleep 2; a input text 12; sleep 4; hide_ime; cap homesearch-12 1; back 1.5; back 1.5; fi
  if open_settings; then cap set-1 1; for i in 2 3 4 5 6; do sw_up 600; cap set-$i 1.2; done; for i in 1 2 3; do sw_up 900; done; cap set-end 1.5; back 1.5; fi
  ready; tab news; sleep 3; for i in 1 2 3 4 5 6 7 8; do sw_up 120; done; sleep 1.5; cap news-end 0.5
  sec "U7. Feast greeting as an icon (6 Dec), seasonal thread: 3 March (twisted red-white), 25 Dec (Christmas star), 1 May 2027 (Easter, green)"
  D0=$(a date +%m%d%H%M%Y.%S | tr -d '\r')
  season_shots nikulden 120610002026.00
  season_shots christmas 122510002026.00
  season_shots march 030310002027.00
  season_shots easter 050110002027.00
  a cmd uimode night yes; sleep 3; season_shots christmas-dark 122510002026.00; a cmd uimode night no; sleep 2
  set_date $D0; a settings put global auto_time 1
  log "UI38 part: $(since_fatal u38)"
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -30 | tee -a $SUM
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- devDebug: вход, „Моето“, удължаване, съобщения, дневник, картата през декември
part_dev38() {
  local p b l t r bt i D0
  helper_install
  dev_install
  anims 1
  warmup
  CFG=dev38; DENS=$(dens); dims; : > $OUT/a11y-dev38.txt
  mark dv
  sec "DV1. Моето as a guest and the login screen (FLAG_SECURE: emulator screenshots)"
  ready; tab my; sleep 3; eshot dev/01-my-guest >/dev/null; cap my-guest 0
  tap text "$L_SIGNIN" >/dev/null; sleep 3; eshot dev/02-login >/dev/null
  dump; p=$(xy --all class EditText | sed -n 1p)
  if [ -n "$p" ]; then
    a input tap $p; sleep 0.8; a input text DEMO-0001; sleep 0.5
    dump; p=$(xy --all class EditText | sed -n 2p); a input tap $p; sleep 0.8; a input text demo; sleep 0.5; hide_ime
    dump; python3 $WF checkstate $OUT/ui.xml 'Запомни ме' | grep -q 'checked=false' && { tap text 'Запомни ме' >/dev/null; sleep 1; }
    eshot dev/03-login-filled >/dev/null
    dump; p=$(xy text '=Вход' --last)
    sec "DV2. „Картата е издадена“: the sign-in tap recorded from outside (the card lifts, then flies to the hub tile)"
    erec login-fly 5 a input tap $p
    sleep 2; eshot dev/04-after-login >/dev/null; dump; log "after the sign-in: $(texts 12 | cut -c1-300)"
  else log "LOGIN: no fields: $(texts 10 | cut -c1-200)"; fi
  ready; tab my; sleep 3; eshot dev/05-my-signed-in >/dev/null; cap my-signed-in 0; sw_up 500; sleep 1; eshot dev/06-my-signed-in-2 >/dev/null; top_of
  log "MY signed in: $(texts 20 | cut -c1-400)"
  sec "DV3. Loans and renewal (demo: renew → pending, no new due date → no „Нов лист“ expected)"
  open_link_dev my/loans; sleep 1; eshot dev/07-loans >/dev/null; dump; log "LOANS: $(texts 20 | cut -c1-400)"
  p=$(xy text '=Удължи срока|=Renew')
  if [ -n "$p" ]; then a input tap $p; sleep 0.5; eshot dev/08-renew-0.5s >/dev/null; sleep 1.5; eshot dev/09-renew-2s >/dev/null; dump; log "RENEW: $(texts 24 | grep -oE '(Заявено удължаване|Удължено|Нов срок|чака библиотеката|Удължаването)[^"]*' | head -3 | tr '\n' '|'); leaf node $(has 'Нов срок:' && echo PRESENT || echo absent)"
  else log "RENEW: no 'Удължи срока' button"; fi
  back 1.5
  sec "DV4. Messages"
  ready; tab my; sleep 2
  for i in 1 2 3; do dump; p=$(xy text 'Съобщения|Messages|Известия от библиотеката'); [ -n "$p" ] && break; sw_up 400; sleep 1; done
  if [ -n "$p" ]; then a input tap $p; sleep 3; cap messages 0.5; eshot dev/10-messages >/dev/null; log "MESSAGES: $(texts 16 | cut -c1-300)"; back 1.5; else log "MESSAGES: entry not found in Моето: $(texts 16 | cut -c1-260)"; fi
  sec "DV5. Diary: 5 rosettes on the first book, then the year story (favourite slide)"
  open_link_dev my/history 5; sleep 1; eshot dev/11-history >/dev/null
  for i in 1 2 3 4; do dump; b=$(xy --bounds content-desc 'Оценка на книгата|Book rating'); [ -n "$b" ] && break; sw_up 400; sleep 1; done
  if [ -n "$b" ]; then read l t r bt <<< "$b"; a input tap $(( r - (r - l) * 6 / 100 )) $(( (t + bt) / 2 )); sleep 2; eshot dev/12-rated >/dev/null; dump; log "DIARY rated: $(grep -oE 'content-desc="[^"]*Оценка[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  else log "DIARY: no rating node"; fi
  back 1.5
  if year_open_fresh 25; then
    for i in 1 2 3 4 5 6 7 8 9 10 11; do
      sleep 1.5; dump; eshot dev/y-$i >/dev/null
      log "YEAR slide $i: $(texts 8 | cut -c1-240)"
      grep -q 'Затвори' $OUT/ui.xml || break
      a input tap $((W*88/100)) $((H/2)); sleep 0.8
    done
    [ "$(ystate)" = open ] && back 2
  else log "YEAR: story did not open"; fi
  sec "DV6. The card on 10 December (the „Твоята шевица“ strip) and in fullscreen; 20 January; 14 November (no strip)"
  D0=$(a date +%m%d%H%M%Y.%S | tr -d '\r')
  for t in 121010002026.00:dec 012010002027.00:jan; do
    set_date ${t%%:*}; a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 6; dismiss
    card_screen; sleep 3; eshot dev/card-${t##*:} >/dev/null; dump; log "CARD ${t##*:}: band node $(has 'Твоята шевица за' && echo PRESENT || echo absent)"
    tap text "$L_CARDSHOW" >/dev/null && { sleep 3; eshot dev/card-full-${t##*:} >/dev/null; dump; log "CARD full ${t##*:}: band $(has 'Твоята шевица за' && echo PRESENT || echo absent)"; back 1.5; }
    leave
  done
  set_date $D0; a settings put global auto_time 1
  card_screen; sleep 3; eshot dev/card-nov >/dev/null; dump; log "CARD nov: band node $(has 'Твоята шевица за' && echo PRESENT || echo absent)"; leave
  sec "DV7. Tablet landscape: two panes in Моето signed in"
  a wm size 2560x1600; a wm density 320; sleep 4; dims; restart; dims
  ready; tab my; sleep 3; eshot dev/13-tablet-my >/dev/null
  for t in '=Заети книги|=Loans|Заети' '=Читателска карта|=Library card' '=История|=History'; do dump; p=$(xy text "$t"); [ -n "$p" ] && { a input tap $p; sleep 3; eshot dev/14-tablet-$(echo "$t" | tr -dc 'a-zA-Z' | cut -c1-10) >/dev/null; log "TABLET after '$t': $(texts 10 | cut -c1-200)"; }; done
  a wm size reset; a wm density reset; sleep 3
  log "DEV38 part: $(since_fatal dv)"
  python3 $W20 a11yagg $OUT/a11y-dev38.txt | head -20 | tee -a $SUM
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- devDebug 1.2.37 → 1.2.38 с дневник
part_updd38() {
  local b l t r bt i p
  helper_install
  use_pkg org.chyavorec.app.dev
  timeout 60 adb uninstall $PKG >/dev/null 2>&1
  timeout 180 adb install -r app-dev-base.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  log "installed devDebug base: $(cat apk-dev-base-ref.txt 2>/dev/null); $(ver)"
  anims 1
  warmup
  sec "DD1. State on devDebug 1.2.37: demo sign-in (remember me), 5 rosettes + a note in the diary, „Искам да прочета“, text size „Голям“"
  demo_signin_remember
  open_link_dev my/history 5; sleep 1
  for i in 1 2 3 4; do dump; b=$(xy --bounds content-desc 'Оценка на книгата|Book rating'); [ -n "$b" ] && break; sw_up 400; sleep 1; done
  if [ -n "$b" ]; then read l t r bt <<< "$b"; a input tap $(( r - (r - l) * 6 / 100 )) $(( (t + bt) / 2 )); sleep 2; dump; log "DIARY on 1.2.37: $(grep -oE 'content-desc="[^"]*Оценка[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
    dump; if tap text '=Добави бележка|=Add a note' >/dev/null; then sleep 1.2; dump; p=$(xy --all class EditText | sed -n 1p); [ -n "$p" ] && { a input tap $p; sleep 1; a input text "upd_note_137"; sleep 1; hide_ime; }; tap text '=Запази|=Save' >/dev/null; sleep 1.5; fi
    eshot dd/01-history-137 >/dev/null
  else log "DIARY: no rating node on 1.2.37"; fi
  back 1.5
  want_add "$BOOK_URL"; want_add "$SITE/elektronen-katalog?inv=1"
  set_text_size '=Голям|=Large'; back 1.5; back 1.5
  upd_state before
  a am force-stop $PKG; sleep 2
  db_pull before
  sec "DD2. adb install -r devDebug 1.2.38"
  mark dd-pre
  log "adb install -r: $(timeout 180 adb install -r app-dev.apk 2>&1 | tr -d '\r' | tail -1); now $(ver)"
  launch >/dev/null; sleep 6; dump; eshot dd/02-first-launch >/dev/null; log "first screen after the update: $(texts 12 | cut -c1-260)"; dismiss
  log "logcat since the install: $(since_fatal dd-pre); Room: $(since dd-pre 'Migration|SQLiteException|no such table|Room cannot' | head -3 | cut -c1-200 | tr '\n' '|')"
  upd_state after
  ready; tab my; sleep 3; eshot dd/03-my >/dev/null; dump; log "MY after the update (still signed in?): $(texts 12 | cut -c1-260)"
  open_link_dev my/history 5; sleep 1; eshot dd/04-history-138 >/dev/null; dump
  log "DIARY after the update: rating $(grep -oE 'content-desc="[^"]*Оценка[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' '); note $(has 'upd_note_137' && echo KEPT || echo 'not visible')"
  back 1.5
  a am force-stop $PKG; sleep 2; db_pull after; launch >/dev/null; sleep 5; dismiss
  sec "DD3. The year story on the migrated diary (favourite slide)"
  if year_open_fresh 25; then
    for i in 1 2 3 4 5 6 7 8 9 10 11; do sleep 1.5; dump; eshot dd/y-$i >/dev/null; log "YEAR slide $i: $(texts 8 | cut -c1-200)"; grep -q 'Затвори' $OUT/ui.xml || break; a input tap $((W*88/100)) $((H/2)); sleep 0.8; done
    [ "$(ystate)" = open ] && back 2
  else log "YEAR: story did not open"; fi
  log "UPDD38 part: $(since_fatal dd-pre)"
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- самообновяването с dex metadata
dm_for_sdk() {  # $1 — папка, $2 — префикс: .dm за този Android
  if [ "$SDK" -ge 31 ]; then ls $1/$2*.v0_1_5_s.dm 2>/dev/null | head -1; else ls $1/$2*.v0_1_0_p.dm 2>/dev/null | head -1; fi
}
dm_case() {  # $1 — етикет, $2… — DMSPEC за update.json (minSdk:maxSdk:файл[:sha])
  local L=$1 p r st i t0; shift
  sec "DM-$L. In-app update 1.2.38-local (vc $((VC_NEW - 1))) → 1.2.38 (vc $VC_NEW) from https://10.0.2.2:8443; dm: ${*:-none}"
  ( cd $UPC_S/www && rm -f *.dm update.json )
  cp app.apk $UPC_S/www/app.apk; echo app.apk > $UPC_S/www/apkname; echo 0 > $UPC_S/www/rate
  local s; for s in "$@"; do cp "$(echo $s | cut -d: -f3)" $UPC_S/www/ 2>/dev/null; done
  log "$(python3 $W24 manifest $UPC_S/www/app.apk $UPC_S/www/update.json https://10.0.2.2:8443 $VC_NEW 1.2.38 "$@" 2>&1)"
  inst app-new-upd-local.apk upd-local apk-new-upd-local-ref.txt || return 1
  log "install-unknown-apps: $(a appops set $PKG REQUEST_INSTALL_PACKAGES allow 2>&1; a appops get $PKG REQUEST_INSTALL_PACKAGES | tr -d '\r')"
  echo "=== $L" >> $OUT/server.log; mark dm-$L; t0=$(now_ms)
  launch >/dev/null
  upd_dialog 'text="(Инсталирай|Install|Изтегли и инсталирай|Download and install)"' && st=dialog || st=none
  shot dm/$L-1-dialog >/dev/null
  log "DM $L: dialog $st after $(( $(now_ms) - t0 )) ms: $(texts 10 | cut -c1-300); files: $(upd_files)"
  [ $st = dialog ] || { log "DM $L: no update dialog; server: $(sed -n "/=== $L/,\$p" $OUT/server.log | cut -c1-120 | tr '\n' '~')"; return 1; }
  p=$(xy text '=Инсталирай|=Install'); [ -z "$p" ] && p=$(xy text '=Изтегли и инсталирай|=Download and install')
  a input tap $p; t0=$(now_ms)
  if installer_confirm $VC_NEW dm-$L; then r="INSTALLED in $(( ($(now_ms) - t0) / 1000 )) s"; else r="NOT installed ($(ver))"; fi
  sleep 3
  log "DM $L RESULT: $r; $(ver)"
  log "DM $L dexopt: $(dexopt_full)"
  log "DM $L files: $(profiles)"
  log "DM $L server: $(sed -n "/=== $L/,\$p" $OUT/server.log | grep -vE '^===' | cut -c14-120 | tr '\n' '~' | cut -c1-600)"
  log "DM $L PackageManager/installd: $(since dm-$L 'dex.?metadata|\.dm|BAD_DEX|install-dm|INSTALL_FAILED|dexopt|dex2oat.*--profile|PackageInstaller' | grep -vE 'PERFCHECK' | head -8 | cut -c1-230 | tr '\n' '|')"
  log "DM $L app diagnostics (update.*): $(a "cat /data/data/$PKG/no_backup/diagnostics/log.jsonl 2>/dev/null" | tr -d '\r' | grep -E 'update' | tail -4 | cut -c1-200 | tr '\n' '|')"
  log "DM $L: $(since_fatal dm-$L)"
  launch >/dev/null; sleep 4; dismiss; shot dm/$L-2-after >/dev/null
  for i in 1 2 3; do cold_drawn dm-$L >/dev/null; done
  log "DM $L first cold starts after the update: $(cut -c1-200 $OUT/drawn-dm-$L.txt | grep -oE 'TotalTime: [0-9]+|Fully drawn[^|]*' | tr '\n' ' ' | cut -c1-300)"
}
part_dm() {
  local DMP DMS DMB sid sz1 sz2 r
  sec "DM0. Files and the local HTTPS server (runner 10.0.2.2:8443, test CA in the system store)"
  ls -l app.apk app-new-upd-local.apk dm/ 2>&1 | tee -a $SUM
  cat dm/dm.json dm-base/dm.json 2>/dev/null | tr -d '\n ' | cut -c1-600 | tee -a $SUM; echo >> $SUM
  log "updater build: $(cat apk-new-upd-local-ref.txt 2>/dev/null)"
  [ -f app-new-upd-local.apk ] || { log "DM: app-new-upd-local.apk missing — the in-app path cannot be run"; return 1; }
  DMP=$(ls dm/*.v0_1_0_p.dm 2>/dev/null | head -1); DMS=$(ls dm/*.v0_1_5_s.dm 2>/dev/null | head -1)
  [ -n "$DMP$DMS" ] || { log "DM: no .dm built (profgen?) — only the control without dm"; }
  upc_server
  : > $OUT/server.log.keep
  sec "DM1. Control: pm install session with base.apk + base.dm (no app code), then the same without .dm"
  local MY; MY=$(dm_for_sdk dm "")
  for v in with-dm without-dm; do
    a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1
    timeout 60 adb push app.apk /data/local/tmp/b.apk >/dev/null 2>&1
    sz1=$(stat -c %s app.apk); sz2=0
    [ $v = with-dm ] && [ -n "$MY" ] && { timeout 60 adb push $MY /data/local/tmp/b.dm >/dev/null 2>&1; sz2=$(stat -c %s $MY); }
    sid=$(a pm install-create -r -S $((sz1 + sz2)) | tr -d '\r' | grep -oE '[0-9]+' | head -1)
    a pm install-write -S $sz1 $sid base.apk /data/local/tmp/b.apk | tr -d '\r' | tail -1
    [ $sz2 -gt 0 ] && a pm install-write -S $sz2 $sid base.dm /data/local/tmp/b.dm | tr -d '\r' | tail -1
    mark ctl-$v
    r=$(T=240 a pm install-commit $sid | tr -d '\r' | tail -1)
    log "CONTROL $v ($(basename ${MY:-none})): session $sid → $r; $(ver)"
    log "CONTROL $v dexopt: $(dexopt_full)"
    log "CONTROL $v files: $(profiles)"
    log "CONTROL $v installd/PM: $(since ctl-$v 'dex.?metadata|BAD_DEX|install-dm|dex2oat|INSTALL_FAILED' | grep -v PERFCHECK | head -4 | cut -c1-220 | tr '\n' '|')"
    a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
    launch >/dev/null; sleep 6; dismiss
    for i in 1 2 3; do cold_drawn ctl-$v >/dev/null; done
    log "CONTROL $v first cold starts: $(grep -oE 'TotalTime: [0-9]+|Fully drawn[^|]*' $OUT/drawn-ctl-$v.txt | tr '\n' ' ' | cut -c1-300)"
  done
  # Повреден .dm с вярна сума в update.json (системата трябва да откаже сесията, приложението — да опита без него)
  head -c 24000 /dev/urandom > $UPC_S/junk.v0_1_0_p.dm; cp $UPC_S/junk.v0_1_0_p.dm $UPC_S/junk.v0_1_5_s.dm
  DMB=$(dm_for_sdk dm-base "")
  dm_case good ${DMP:+28:30:$DMP} ${DMS:+31::$DMS}
  dm_case badsha ${DMP:+28:30:$DMP:0000000000000000000000000000000000000000000000000000000000000000} ${DMS:+31::$DMS:0000000000000000000000000000000000000000000000000000000000000000}
  dm_case junk 28:30:$UPC_S/junk.v0_1_0_p.dm 31::$UPC_S/junk.v0_1_5_s.dm
  [ -n "$DMB" ] && dm_case old137 $( [ "$SDK" -ge 31 ] && echo "31::$DMB" || echo "28:30:$DMB")
  dm_case none
  kill $SRVPID 2>/dev/null
  cp $OUT/server.log $OUT/server-log.txt
  diag_end
}

# ---------------------------------------------------------------- студен старт A/B
part_start38() {
  local b=0 K order K2
  anims 1
  case $PART in start38a) order="new base base new new base base new";; *) order="base new new base base new new base";; esac
  sec "S0. Emulator warm-up block (not counted): both apps, 2 installs × 5 cold starts"
  for K2 in base new; do inst $(apk_of $K2) warm-$K2 $(ref_of $K2) >/dev/null; launch >/dev/null; sleep 6; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; for b in 1 2 3 4 5; do cold_drawn warm-$K2 >/dev/null; done; done
  b=0
  log "block order (each a fresh install): $order"
  for K in $order; do b=$((b + 1)); s36_block $K $b; done
  sec "S totals (n=20 per app)"
  for K in new base; do
    python3 $H_/parse.py starts $OUT/drawn-$K.txt "cold $K" | tee -a $SUM
    drawn_sum $K
  done
  python3 $W21 ab $OUT/drawn-new.txt $OUT/drawn-base.txt | tee -a $SUM
  diag_end
}

# ---------------------------------------------------------------- покой, плавност, памет (1.2.38 / 1.2.37)
quiz_flow_jank() {  # $1 — етикет: плавност на отговарянето (5 въпроса, първите отговори)
  local i o n
  more_tile "$L_QUIZ" || return 1
  waitfor 'Въпрос [0-9]+ от|Question [0-9]+ of' 40 || { log "QUIZ jank $1: no question"; leave; return 1; }
  gfx_reset
  for i in 1 2 3 4 5; do
    dump; o=$(python3 $W24 quizopts $OUT/ui.xml $H $W | grep -m1 '^A' | cut -f1); o=${o#A }
    [ -n "$o" ] || break; a input tap $o; sleep 1.8
    dump; n=$(python3 $W24 quizopts $OUT/ui.xml $H $W | awk '$1=="N"{print $2, $3}'); [ -n "$n" ] || { sw_up 300; sleep 1; dump; n=$(python3 $W24 quizopts $OUT/ui.xml $H $W | awk '$1=="N"{print $2, $3}'); }
    [ -n "$n" ] || break; a input tap $n; sleep 2.2
  done
  sleep 2; gfx_save $1-quizflow; shot jank/$1-quizflow >/dev/null
  log "  $(python3 $W18 gfxmem $OUT/gfx-$1-quizflow.txt $1-quizflow | cut -c1-300)"
  leave
}
part_perf38() {
  local K=new c p
  [ $PART = perfb38 ] && K=base
  anims 1
  sec "P0. Fresh install of $K ($(cut -c1-80 $(ref_of $K))), first launch, catalogue, a card by hand"
  inst $(apk_of $K) $K $(ref_of $K) || return
  a input keyevent KEYCODE_HOME; sleep 2; launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab news; sleep 4; tab home; sleep 3
  card_open >/dev/null || log "card not added"; leave
  sec "P1. CPU and frames at rest (30 s after opening, then a 15 s window): Home after a cold start (~45 s), the card, fullscreen card, Още, Моето, quiz, timeline"
  a am force-stop $PKG; sleep 3; launch >/dev/null; sleep 2; dismiss; ready; tab home; idle_at home38-$K
  card_screen; idle_at card38-$K; tap text "$L_CARDSHOW" >/dev/null && { idle_at cardfull38-$K; back 1.5; }; leave
  ready; tab more; idle_at more38-$K
  ready; tab my; idle_at my38-$K
  more_tile "$L_QUIZ" && { waitfor 'Въпрос [0-9]+ от|Question [0-9]+ of|Днес няма' 40; idle_at quiz38-$K; leave; }
  more_tile "$L_TIMELINE" && { idle_at timeline38-$K; leave; }
  sec "P2. Jank (animations ON, ${W}x${H}): Home, Още, Моето, timeline, quiz answering"
  restart; tab home; sleep 20; jscn home-scroll p38-$K
  ready; tab more; sleep 2; jscn more p38-$K
  ready; tab my; sleep 2; jscn my p38-$K
  more_tile "$L_TIMELINE" && { sleep 3; jscn timeline p38-$K; leave; }
  quiz_flow_jank p38-$K
  sec "P3. Memory: 12 cycles over Още/Моето/quiz/timeline/card/embroidery, then a full tour"
  restart; mem $K-c00
  for c in $(seq 1 12); do
    more_tile "$L_TIMELINE" && { sleep 2; sw_up 600; sw_up 600; leave; }
    more_tile "$L_QUIZ" && { sleep 3; leave; }
    more_tile "$L_EMB" && { sleep 2; leave; }
    ready >/dev/null; tab my >/dev/null; sleep 1.5; card_screen; sleep 2; leave
    ready >/dev/null; tab more >/dev/null; sleep 1.5; sw_up 500; sleep 1; top_of
    case $c in 4|8|12) mem $K-c$(printf %02d $c);; esac
  done
  tour perf-$K
  mem $K-after-tour
  log "PERF38 $K: FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt); pid $(pidof_app)"
  all_sheets
  diag_end
}

# ================================================================= КРЪГ 2 (ui38b, dev38b)
# eshot с подпапка в името (кръг 1: mv в несъществуваща папка → снимките липсваха)
eshot() {
  local d f; d=$(realpath -m $OUT/shots/eshot); mkdir -p "$(dirname "$d/$1")"; rm -f $d/Screenshot*.png
  timeout 20 adb emu screenrecord screenshot $d >/dev/null 2>&1; sleep 1
  f=$(ls -t $d/Screenshot*.png 2>/dev/null | head -1)
  [ -n "$f" ] && mv "$f" "$d/$1.png" && echo "$d/$1.png"
}
# Прозорец от запис: $1 — име, $2 — от (s след командата), $3 — колко s, $4 — кадри/s, $5 — out (adb | emu), $6… — командата
recwin() {
  local name=$1 from=$2 dur=$3 fps=$4 how=$5 f off n; shift 5
  mkdir -p $OUT/rec $OUT/shots/win/$name
  if [ $how = emu ]; then
    f=$(realpath -m $OUT/rec/$name.webm); rm -f $f
    timeout 10 adb emu screenrecord start --time-limit 7 $f >/dev/null 2>&1; off=1.0; sleep 1.0
    "$@"; sleep 7; timeout 10 adb emu screenrecord stop >/dev/null 2>&1; sleep 2.5
  else
    f=$OUT/rec/$name.mp4; a rm -f /sdcard/rec.mp4
    adb shell screenrecord --time-limit 7 --size 540x1200 --bit-rate 3000000 /sdcard/rec.mp4 >/dev/null 2>&1 &
    RECPID=$!; off=1.6; sleep 1.6
    "$@"; wait $RECPID 2>/dev/null; sleep 1
    timeout 30 adb pull /sdcard/rec.mp4 $f >/dev/null 2>&1
  fi
  if [ -s $f ]; then
    ffmpeg -loglevel error -ss $(python3 -c "print($off+$from)") -t $dur -i $f -vf fps=$fps $OUT/shots/win/$name/f%02d.png
    n=$(ls $OUT/shots/win/$name 2>/dev/null | wc -l)
    log "RECWIN $name ($how, +${from}s for ${dur}s at ${fps} fps): $n frames; $(python3 $W24 frames $OUT/shots/win/$name 2>&1 | tail -1)"
  else log "RECWIN $name: no recording"; fi
  rm -f $f
}
dev_login() {  # вход DEMO-0001 / demo с проверка на полетата; $1 — записва ли се докосването (rec | no)
  local p1 p2 i v
  ready; tab my; sleep 3; tap text "$L_SIGNIN" >/dev/null; sleep 3; eshot dv2/02-login >/dev/null
  for i in 1 2; do
    dump; p1=$(xy --all class EditText | sed -n 1p); p2=$(xy --all class EditText | sed -n 2p)
    [ -n "$p1" ] && [ -n "$p2" ] || { log "LOGIN: fields not found: $(texts 12 | cut -c1-200)"; return 1; }
    a input tap $p1; sleep 1; a input keyevent KEYCODE_MOVE_END; for v in 1 2 3 4 5 6 7 8 9 10 11 12 13 14; do a input keyevent KEYCODE_DEL; done
    a input text DEMO-000; sleep 0.5; a input text 1; sleep 1; hide_ime; sleep 1
    dump; p2=$(xy --all class EditText | sed -n 2p); a input tap $p2; sleep 1; a input text demo; sleep 1; hide_ime; sleep 1
    dump; v=$(grep -oE 'text="DEMO-0001"' $OUT/ui.xml | head -1)
    log "LOGIN try $i: card field $( [ -n "$v" ] && echo OK || echo "WRONG ($(grep -oE 'text="DEMO[^"]*"' $OUT/ui.xml | head -1))")"
    [ -n "$v" ] && break
  done
  python3 $WF checkstate $OUT/ui.xml 'Запомни ме' | grep -q 'checked=false' && { tap text 'Запомни ме' >/dev/null; sleep 1; dump; }
  eshot dv2/03-login-filled >/dev/null
  p1=$(xy text '=Вход' --last)
  if [ "$1" = rec ]; then recwin login-fly 0 2.6 5 emu a input tap $p1; else a input tap $p1; sleep 6; fi
  ready; tab my; sleep 3; dump
  log "MY after the sign-in: $(has 'Демо Читател' && echo 'SIGNED IN' || echo 'NOT signed in'): $(texts 10 | cut -c1-240)"
  has 'Демо Читател'
}
part_ui38b() {
  local p i
  helper_install
  a am force-stop $PKG
  anims 1
  warmup
  CFG=ui38b; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt; mark u38b
  sec "UB1. Event leaf: „Добави в календара“ → the helper („Just once“) → back: the stub tears off"
  if open_event 1; then
    cap ev-01 1.5
    dump; p=$(xy text '=Добави в календара|=Add to calendar' --maxy $((H*93/100)))
    if [ -n "$p" ]; then
      mark ev-cal; a input tap $p; sleep 3; dump
      if has 'PC helper|Complete action|Open with|Отваряне с'; then shot evb/02-chooser >/dev/null; tap text '=PC helper' >/dev/null; sleep 1; tap text '=Just once|=Само веднъж' >/dev/null; sleep 2.5; fi
      log "CALENDAR: fg $(fg); helper: $(since ev-cal 'PCCATCH' | head -1 | cut -c1-300)"
      shot evb/03-helper >/dev/null
      recwin event-tear 0 2.4 8 adb a input keyevent KEYCODE_BACK
      sleep 1; cap ev-04-after 0.5; dump
      log "EVENT after return: '$(grep -oE '(text|content-desc)="[^"]*(Добавено в календара|Added to calendar)[^"]*"' $OUT/ui.xml | head -1)'; on the event screen $(has "$RX_BACK" && echo yes || echo NO)"
      sleep 3; cap ev-05-rest 0.5
    else log "EVENT: no 'Добави в календара'"; fi
    leave
  fi
  sec "UB2. Quiz 5/5: frames 0.8–2.6 s after the last answer (stitch, close, confetti)"
  quiz_pass 1 qz1 >/dev/null
  a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; a cmd locale set-app-locales $PKG --locales bg-BG; no_camera >/dev/null
  launch >/dev/null; sleep 6; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab news; sleep 4; tab home; sleep 3
  QUIZ_REC=win quiz_pass 2 qz2
  sec "UB3. Login screen as a guest (FLAG_SECURE: emulator screenshot)"
  ready; tab my; sleep 2; tap text "$L_SIGNIN" >/dev/null && { sleep 3; hide_ime; sleep 1; eshot ub/login-guest >/dev/null; back 2; }
  log "UI38B part: $(since_fatal u38b)"
  all_sheets
  diag_end
}
part_dev38b() {
  local p b l t r bt i D0 x
  helper_install
  dev_install
  anims 1
  warmup
  CFG=dev38b; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt; mark dvb
  sec "DB1. Sign-in (fields checked), the „Картата е издадена“ tap recorded from outside (5 fps, 0–2.6 s)"
  ready; tab my; sleep 3; eshot dv2/01-my-guest >/dev/null
  dev_login rec || dev_login no || log "DEMO sign-in FAILED twice"
  eshot dv2/04-my-signed-in >/dev/null; sw_up 500; sleep 1; eshot dv2/05-my-signed-in-2 >/dev/null; top_of
  sec "DB2. Loans: renew (demo → pending)"
  open_link_dev my/loans; sleep 2; eshot dv2/06-loans >/dev/null; dump; log "LOANS: $(texts 24 | cut -c1-500)"
  p=$(xy text '=Удължи срока|=Renew')
  if [ -n "$p" ]; then a input tap $p; sleep 0.6; eshot dv2/07-renew-0.6s >/dev/null; sleep 1.5; eshot dv2/08-renew-2s >/dev/null; dump; log "RENEW: $(texts 30 | grep -oE '(Заявено удължаване|Удължено|Нов срок|чака библиотеката|Удължаването|Удължи)[^"]*' | head -4 | tr '\n' '|'); leaf node $(has 'Нов срок:' && echo PRESENT || echo absent)"
  else log "RENEW: no 'Удължи срока' button"; fi
  back 1.5
  sec "DB3. Messages (Още › Съобщения)"
  if more_tile '=Съобщения|=Messages'; then sleep 2; cap messages 0.5; eshot dv2/09-messages >/dev/null; log "MESSAGES: $(texts 16 | cut -c1-300)"
    dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/5)) $((H*85/100)) 120); [ -n "$p" ] && { a input tap $p; sleep 2.5; cap message-open 0.5; back 1.5; }
    leave; fi
  sec "DB4. Diary: 5 rosettes, then the year story slide by slide (content descriptions)"
  open_link_dev my/history 5; sleep 1
  for i in 1 2 3 4; do dump; b=$(xy --bounds content-desc 'Оценка на книгата|Book rating'); [ -n "$b" ] && break; sw_up 400; sleep 1; done
  if [ -n "$b" ]; then read l t r bt <<< "$b"; a input tap $(( r - (r - l) * 6 / 100 )) $(( (t + bt) / 2 )); sleep 2; eshot dv2/10-rated >/dev/null; fi
  back 1.5
  if year_open_fresh 25; then
    for i in $(seq 1 12); do
      dump; x=$(grep -oE 'content-desc="[^"]{12,}"' $OUT/ui.xml | grep -vE 'Затвори|Сподели|Следващ|Предишен' | head -1 | cut -c1-200)
      log "YEAR $i: $x"
      echo "$x" | grep -q 'Най-харесана' && eshot dv2/y-favourite >/dev/null
      grep -q 'Затвори' $OUT/ui.xml || break
      a input tap $((W*88/100)) $((H/2)); sleep 0.7
    done
    [ "$(ystate)" = open ] && back 2
  else log "YEAR: story did not open"; fi
  sec "DB5. The card on 10 December and 20 January (the strip), 14 November (none)"
  D0=$(a date +%m%d%H%M%Y.%S | tr -d '\r')
  for t in 121010002026.00:dec 012010002027.00:jan; do
    set_date ${t%%:*}; a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 6; dismiss
    card_screen; sleep 3; eshot dv2/card-${t##*:} >/dev/null; dump; log "CARD ${t##*:}: band $(has 'Твоята шевица за' && echo PRESENT || echo absent)"
    tap text "$L_CARDSHOW" >/dev/null && { sleep 3; eshot dv2/card-full-${t##*:} >/dev/null; dump; log "CARD full ${t##*:}: band $(has 'Твоята шевица за' && echo PRESENT || echo absent)"; back 1.5; }
    leave
  done
  set_date $D0; a settings put global auto_time 1
  a am force-stop $PKG; launch >/dev/null; sleep 5; card_screen; sleep 3; eshot dv2/card-nov >/dev/null; dump; log "CARD nov: band $(has 'Твоята шевица за' && echo PRESENT || echo absent)"; leave
  sec "DB6. Tablet landscape (1280×800 dp): two panes in Още and Моето, signed in"
  a wm size 2560x1600; a wm density 320; sleep 4; dims; restart; dims
  ready; tab more; sleep 2.5; eshot dv2/t-more >/dev/null
  for t in "$L_QUIZ" "$L_TIMELINE" '=Съобщения|=Messages'; do
    dump; p=$(xy text "$t")
    [ -n "$p" ] || { a input swipe $((W*28/100)) $((H*75/100)) $((W*28/100)) $((H*30/100)) 500; sleep 1.5; dump; p=$(xy text "$t"); }
    [ -n "$p" ] && { a input tap $p; sleep 3; x=$(echo "$t" | tr -dc 'a-zA-Z' | cut -c1-10); eshot dv2/t-more-$x >/dev/null; dump; log "TABLET Още › '$t': tabs $(has 'text="(Начало|Home)"' && echo visible || echo hidden); $(texts 8 | cut -c1-200)"; } || log "TABLET: '$t' not found in the left pane"
  done
  leave; ready; tab my; sleep 3; eshot dv2/t-my >/dev/null
  for t in '=Моите книги|=My books' '=Читателска карта|=Library card' '=Моето членство|=My membership'; do
    dump; p=$(xy text "$t"); [ -n "$p" ] && { a input tap $p; sleep 3; x=$(echo "$t" | tr -dc 'a-zA-Z' | cut -c1-10); eshot dv2/t-my-$x >/dev/null; dump; log "TABLET Моето › '$t': tabs $(has 'text="(Начало|Home)"' && echo visible || echo hidden); $(texts 8 | cut -c1-200)"; }
  done
  a wm size reset; a wm density reset; sleep 3
  log "DEV38B part: $(since_fatal dvb)"
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- маршрутизация
part_w24() {
  case "$W24_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    tour38|tour38b) part_tour38;;
    ui38) part_ui38;;
    ui38b) part_ui38b;;
    dev38b) part_dev38b;;
    dev38) part_dev38;;
    mon38) part_mon36;;
    upd38) part_upd36;;
    updd38) part_updd38;;
    dm33|dm28) part_dm;;
    start38a|start38b) part_start38;;
    perf38|perfb38) part_perf38;;
  esac
  return 0
}
