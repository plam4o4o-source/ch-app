# Целева проверка на издаденото v1.2.38 (ВРЕМЕННО, debug/w25-check; резултати в emulator-results/v138b).
# Три prodRelease x86_64 с временен ключ, инсталирани ЕДНОВРЕМЕННО (различни applicationId), за да се редуват
# на едно и също пускане на емулатора:
#   new  — org.chyavorec.app      — издаденото 1.2.38 (f57962bd), без промени
#   base — org.chyavorec.app.b37  — 1.2.37 (ebf1720)
#   hook — org.chyavorec.app.hk   — клонът debug/w25-check: дневник (Log.w „W25“) и тестови флагове (--es w25 …)
# и devDebug от клона (org.chyavorec.app.dev): демо вход, „demo-1“ се удължава веднага.
#   cf33 / cf28 / cf36x — конфети при 5 от 5 във викторината (дневник + записи), контрола „Искам да прочета“
#   dev33      — вход с БУТОНА, „Картата е издадена“, „Нов лист“, годината (любима книга), таблет, картата през дек/яну
#   fr33a/b    — време за кадър, 1.2.38 срещу 1.2.37, редуване, 6 повторения (framestats)
#   at33t/h    — приписване: един заподозрян наведнъж (флагове в hook)
#   mem33a/b   — памет: 30 цикъла, meminfo след trim + GC на всеки 5
#   wd33/wd36x — уиджетите от „Настройки“ след тъмна тема (на приложението и на системата)
#   fs33       — шрифт 2.0: викторина, лента на времето, настройки, вход
W25_PARTS=" cf33 cf28 cf36x dev33 fr33a fr33b at33t at33h mem33a mem33b wd33 wd36x fs33 "
W20_PARTS="$W20_PARTS $W25_PARTS"
W25=$H_/w25.py
RX_QUESTION='Въпрос [0-9]+ от|Question [0-9]+ of'

# ---------------------------------------------------------------- общи
x_apk() { case $1 in new) echo app.apk;; base) echo app-base.apk;; hook) echo app-hook.apk;; esac; }
x_ref() { case $1 in new) echo apk-ref.txt;; base) echo apk-base-ref.txt;; hook) echo apk-hook-ref.txt;; esac; }
x_pkg() { case $1 in new) echo org.chyavorec.app;; base) echo org.chyavorec.app.b37;; hook) echo org.chyavorec.app.hk;; esac; }
x_use() { use_pkg $(x_pkg $1); }
x_stop_all() { local k; for k in new base hook; do a am force-stop $(x_pkg $k) >/dev/null 2>&1; done; }
launch_f() { a am start -W -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $ACT ${1:+--es w25 $1} >/dev/null 2>&1; }
x_install() {  # $1 — new | base | hook: инсталира (new вече е от setup), език, разрешения, първо пускане до каталога
  local K=$1
  x_use $K
  if [ $K != new ]; then
    [ -f $(x_apk $K) ] || { log "X $K: $(x_apk $K) missing"; return 1; }
    timeout 240 adb install -r $(x_apk $K) 2>&1 | tail -1
  fi
  a cmd locale set-app-locales $PKG --locales bg-BG >/dev/null 2>&1; a pm grant $PKG android.permission.POST_NOTIFICATIONS >/dev/null 2>&1; no_camera >/dev/null
  log "X installed $K: $(cat $(x_ref $K) 2>/dev/null); $(ver)"
  launch >/dev/null; sleep 7; ready >/dev/null; dims
  tab catalog >/dev/null; waitfor "$RX_RESULTS" 150 && log "X $K: catalogue after ${WAITED} ms" || log "X $K: NO catalogue results"
  tab news >/dev/null; sleep 5; tab home >/dev/null; sleep 5
}
x_compile() {  # профилът от APK-а (ProfileInstaller) → speed-profile, както след инсталиране с .dm / фоновия dexopt
  local r c
  r=$(a am broadcast -a androidx.profileinstaller.action.INSTALL_PROFILE $PKG/androidx.profileinstaller.ProfileInstallReceiver 2>&1 | tr -d '\r' | grep -oE 'result=[0-9-]+' | head -1)
  sleep 3; a am force-stop $PKG; sleep 1
  c=$(T=400 a cmd package compile -m speed-profile -f $PKG 2>&1 | tr -d '\r' | tail -1)
  log "X compile $PKG: profile broadcast ${r:-none}; compile: $c; dexopt: $(dexopt_full)"
}
quiz_file_rm() { a rm -f /data/data/$PKG/files/datastore/quiz.preferences_pb >/dev/null 2>&1; }
jframes() {  # $1 — видео, $2 — от (s), $3 — колко s, $4 — кадри/s, $5 — папка → JPEG 432 px (за да не тежат)
  mkdir -p $5; rm -f $5/*.jpg
  ffmpeg -loglevel error -ss $2 -t $3 -i $1 -vf fps=$4,scale=${REC_W:-432}:-2 -q:v 5 $5/f%03d.jpg
  ls $5 2>/dev/null | wc -l
}
jsheets() {  # $1 — папка с кадри → контактни листове в sheets/<име>
  ls $1/*.jpg >/dev/null 2>&1 && python3 $A6 sheets $OUT/sheets/$(basename $1) 15 $(ls $1/*.jpg | sort) >/dev/null
}
# Запис около една команда. $1 — име, $2 — emu | adb | none, $3 — s след командата, $4… — командата.
# Маркер PERFCHECK „<име>“ в logcat точно преди командата. Кадрите: $OUT/frames/<име>/ (10/s, от 0.5 s преди командата).
REC_OFF=0; REC_FILE=""; REC_SS=0
rec_cmd() {
  local name=$1 how=$2 secs=$3 f t0 t1 n; shift 3
  mkdir -p $OUT/rec; [ -n "$REC_FILE" ] && rm -f "$REC_FILE"; REC_FILE=""
  case $how in
    emu)
      f=$(realpath -m $OUT/rec/$name.webm); rm -f $f
      timeout 10 adb emu screenrecord start --time-limit $((secs + 6)) $f >/dev/null 2>&1; t0=$(now_ms); sleep 1.5
      a log -t PERFCHECK "$name"; t1=$(now_ms); "$@"
      sleep $secs; timeout 10 adb emu screenrecord stop >/dev/null 2>&1; sleep 2.5;;
    adb)
      f=$OUT/rec/$name.mp4; rm -f $f; a rm -f /sdcard/rec.mp4
      adb shell screenrecord --time-limit $((secs + 4)) --size 540x1200 --bit-rate 4000000 /sdcard/rec.mp4 >/dev/null 2>&1 &
      RECPID=$!; t0=$(now_ms); sleep 2
      a log -t PERFCHECK "$name"; t1=$(now_ms); "$@"
      wait $RECPID 2>/dev/null; sleep 1
      timeout 40 adb pull /sdcard/rec.mp4 $f >/dev/null 2>&1;;
    *)
      t0=$(now_ms); a log -t PERFCHECK "$name"; t1=$(now_ms); "$@"; sleep $secs; f="";;
  esac
  REC_OFF=$(( t1 - t0 ))
  if [ -n "$f" ] && [ -s "$f" ]; then
    REC_SS=$(python3 -c "print(max(0, $REC_OFF/1000.0 - 0.5))"); REC_FILE=$f
    n=$(jframes $f $REC_SS $((secs + 1)) 10 $OUT/frames/$name)
    log "REC $name ($how): marker ${REC_OFF} ms after the recording start; $n frames at 10/s from 0.5 s before the marker (frame k ≈ marker + (k-6)*100 ms; the command itself needs ~0.5-1 s to deliver the tap)"
    jsheets $OUT/frames/$name
  elif [ $how != none ]; then log "REC $name ($how): no recording file"; fi
}
cf_scan() {  # $1 — име на записа, $2… — правоъгълникът на лентата: брои конфетите, вади 4 кадъра около върха в пълен размер
  local name=$1 k d t; shift
  [ -d $OUT/frames/$name ] || return 0
  log "FRAMES $name: $(python3 $W25 confetti $OUT/frames/$name "$@" 2>&1 | cut -c1-700 | tr '\n' ' ')"
  k=$(cat $OUT/frames/$name/peak.txt 2>/dev/null)
  if [ -n "$k" ] && [ "$k" -gt 0 ] && [ -s "$REC_FILE" ]; then
    mkdir -p $OUT/shots/cf
    for d in 0 2 4 6; do
      t=$(python3 -c "print(max(0, $REC_SS + ($k - 3 + $d)/10.0))")
      ffmpeg -loglevel error -ss $t -i "$REC_FILE" -frames:v 1 $OUT/shots/cf/$name-peak-$d.png 2>/dev/null
    done
  fi
  rm -rf $OUT/frames/$name
}
w25_since() { sleep 1; python3 $W25 w25log $OUT/logcat-live.txt "$1" | head -${2:-260} | cut -c1-260 | tee -a $SUM; }
anim_state() { echo "animator_duration_scale=$(a settings get global animator_duration_scale | tr -d '\r') transition=$(a settings get global transition_animation_scale | tr -d '\r') window=$(a settings get global window_animation_scale | tr -d '\r')"; }

# ================================================================= 1. КОНФЕТИ ПРИ 5 ОТ 5
quiz_open() {  # → 0, ако първият (или текущият) въпрос е на екрана
  more_tile "$L_QUIZ" || return 1
  waitfor "$RX_QUESTION|Днес няма викторина|No quiz today|Нови въпроси" 45 || { log "QUIZ: nothing in 45 s: $(texts 8 | cut -c1-200)"; debug_ui quiz; return 1; }
  has "$RX_QUESTION" || { log "QUIZ: no question: $(texts 10 | cut -c1-260)"; return 1; }
}
quiz_next_xy() {  # „Напред“ / „Виж резултата“ (превърта, ако трябва) → "x y" или нищо
  local j n=""
  for j in 1 2 3; do dump; n=$(python3 $W24 quizopts $OUT/ui.xml $H $W | awk '$1=="N"{print $2, $3}'); [ -n "$n" ] && break; sw_up 300; sleep 1; done
  echo "$n"
}
# Един цял ход през викторината. $1 — learn (първите отговори; учи верните) | right (верните → 5 от 5);
# $2 — етикет; $3 — запис на петия отговор (emu | adb | none); $4 — s изчакване след петия отговор
quiz_play() {
  local mode=$1 L=$2 how=${3:-none} wait5=${4:-7} i o want right n lb box
  quiz_open || return 1
  [ $mode = learn ] && : > $OUT/quiz-answers.txt
  for i in 1 2 3 4 5; do
    dump; python3 $W24 quizopts $OUT/ui.xml $H $W > $OUT/qo.txt
    grep -q '^A' $OUT/qo.txt || { sw_up 400; sleep 1; dump; python3 $W24 quizopts $OUT/ui.xml $H $W > $OUT/qo.txt; }
    o=""
    if [ $mode = right ]; then
      want=$(sed -n ${i}p $OUT/quiz-answers.txt)
      o=$(awk -F'\t' -v w="$want" '$1 ~ /^A / && $2 == w {print $1; exit}' $OUT/qo.txt)
      [ -z "$o" ] && o=$(awk -F'\t' -v w="$want" '$1 ~ /^A / && index($2, w) {print $1; exit}' $OUT/qo.txt)
      [ -z "$o" ] && log "QUIZ $L q$i: the learned answer '$want' is not among: $(grep '^A' $OUT/qo.txt | cut -f2 | tr '\n' '|' | cut -c1-200)"
    fi
    [ -z "$o" ] && o=$(grep -m1 '^A' $OUT/qo.txt | cut -f1)
    [ -n "$o" ] || { log "QUIZ $L q$i: no answer buttons: $(texts 12 | cut -c1-260)"; debug_ui quiz-$L-$i; return 1; }
    o=${o#A }
    if [ $mode = right ] && [ $i = 5 ]; then
      # Лентата: от надписа ѝ (минус мястото на „Вярно!“) до бутона — за броенето на конфетите извън нея
      lb=$(xy --bounds text 'Избродирай отговорите|Embroider the answers|Шевицата е цяла'); n=$(python3 $W24 quizopts $OUT/ui.xml $H $W | awk '$1=="N"{print $3}')
      box=""; [ -n "$lb" ] && [ -n "$n" ] && box=$(python3 -c "print(0, max(0, $(echo $lb | cut -d' ' -f2)/$H - 0.12), 1, ($n - 60)/$H)")
      log "QUIZ $L q5: answer at [$o]; band label bounds [$lb]; next button y ${n:-?}; $(anim_state)"
      shot quiz/$L-5-before >/dev/null
      rec_cmd q5-$L $how $wait5 a input tap $o
      shot quiz/$L-5-after-${wait5}s >/dev/null
      log "W25 log after the 5th answer ($L; times from the marker just before 'input tap'):"; w25_since "q5-$L"
      cf_scan q5-$L $box
    else
      a input tap $o; sleep 2.4
    fi
    dump; right=$(python3 $W24 quizres $OUT/ui.xml)
    log "QUIZ $L q$i: $right; $(grep -oE '(text|content-desc)="[^"]*(Избродирай|Шевицата е цяла|верни от)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
    if [ $mode = learn ]; then
      case "$right" in RIGHT*) awk -F'\t' -v c="A $o" '$1 == c {print $2; exit}' $OUT/qo.txt >> $OUT/quiz-answers.txt;; *) echo "$right" | cut -f2 >> $OUT/quiz-answers.txt;; esac
    fi
    n=$(quiz_next_xy); [ -n "$n" ] || { log "QUIZ $L q$i: no next button: $(texts 12 | cut -c1-260)"; return 1; }
    a input tap $n; sleep 1.8
  done
  sleep 1.5; dump; shot quiz/$L-result >/dev/null
  log "QUIZ $L result: $(texts 10 | cut -c1-300)"
  [ $mode = learn ] && log "QUIZ answers learned: $(tr '\n' '|' < $OUT/quiz-answers.txt | cut -c1-300)"
  leave
}
quiz_fresh() {  # $1 — флагове: чисто състояние на викторината (файлът ѝ се трие), приложението наново
  a am force-stop $PKG; sleep 1; quiz_file_rm; launch_f "$1"; sleep 7; dismiss; ready >/dev/null
}
cf_control() {  # $1 — етикет: „Искам да прочета“ на страницата на книга (конфети от докосването)
  local pw L=$1
  link "$BOOK_URL" >/dev/null; sleep 5; dump
  [ -z "$(xy text "$L_WANT")" ] && { sw_up 300; sleep 1.5; dump; }
  pw=$(xy text "$L_WANT")
  [ -n "$pw" ] || { log "CONTROL $L: no 'Искам да прочета' on the book screen: $(texts 12 | cut -c1-240)"; back 1.5; return 1; }
  if xy --state text "$L_WANT" | grep -q 'checked=true'; then a input tap $pw; sleep 2.5; dump; pw=$(xy text "$L_WANT"); fi
  log "CONTROL $L: want-to-read before: $(xy --state text "$L_WANT")"
  rec_cmd want-$L emu 5 a input tap $pw
  log "W25 log after the control tap ($L):"; w25_since "want-$L"
  cf_scan want-$L
  dump; log "CONTROL $L: want-to-read after: $(xy --state text "$L_WANT")"
  back 1.5
}
part_cf() {
  local i
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || true; }
  anims 1
  log "animations: $(anim_state)"
  sec "CF0. The hook build (same code as 1.2.38 + Log.w lines), first launch"
  x_install hook || return
  log "anim after the first launch: $(anim_state)"
  mark cf
  sec "CF1. Control: confetti on „Искам да прочета“ (host recording), hook build"
  cf_control hook
  sec "CF2. Quiz pass 1 (hook): the first options, the right answers are learned"
  quiz_fresh ""; quiz_play learn learn
  sec "CF3. Quiz 5/5 on the hook build, no recording (pure timing from the log)"
  quiz_fresh ""; quiz_play right hk-none none 8
  sec "CF4. Quiz 5/5 on the hook build, recorded from the host (adb emu screenrecord)"
  quiz_fresh ""; quiz_play right hk-emu emu 8
  sec "CF5. Quiz 5/5 on the hook build, recorded in the guest (screenrecord 540x1200 — loads the software GPU)"
  quiz_fresh ""; quiz_play right hk-adb adb 8
  sec "CF6. Quiz 5/5 on the hook build, „Виж резултата“ NOT tapped for 12 s, host recording (second sample)"
  quiz_fresh ""; quiz_play right hk-long emu 12
  log "CF hook: $(since_fatal cf)"
  sec "CF7. The released 1.2.38 itself (no log lines): control and quiz 5/5, host recording"
  a am force-stop $PKG
  x_use new
  launch >/dev/null; sleep 7; dismiss; ready >/dev/null; dims
  tab catalog >/dev/null; waitfor "$RX_RESULTS" 150 >/dev/null; tab home >/dev/null; sleep 3
  cf_control new
  quiz_fresh ""; quiz_play right new-e1 emu 9
  quiz_fresh ""; quiz_play right new-e2 emu 9
  log "CF part: $(since_fatal cf); Davey lines of the apps: $(grep -c 'Davey!' $OUT/logcat-live.txt)"
  all_sheets
}

# ================================================================= 2. devDebug: вход, карта, „Нов лист“, годината, таблет
dev_signin() {  # вход DEMO-0001 / demo с БУТОНА (по класа/границите, не по първия текст „Вход“); $1 — rec | no
  local p1 p2 i v b bx by
  ready >/dev/null; tab my >/dev/null; sleep 3
  tap text "$L_SIGNIN" >/dev/null || { log "LOGIN: no 'Вход' on Моето: $(texts 12 | cut -c1-240)"; return 1; }
  sleep 3; eshot dv/02-login >/dev/null
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
  # „Запомни ме“ и бутонът са под картата — превърта се, докато бутонът (кликаем, широк, под горната лента) се види
  for i in 1 2 3 4 5; do
    dump
    python3 $WF checkstate $OUT/ui.xml 'Запомни ме' | grep -q 'checked=false' && { tap text 'Запомни ме' >/dev/null; sleep 1; dump; }
    b=$(python3 $W25 loginbtn $OUT/ui.xml $W $H)
    [ -n "$b" ] && python3 $WF checkstate $OUT/ui.xml 'Запомни ме' | grep -q 'checked=true' && break
    a input swipe $((W/2)) $((H*62/100)) $((W/2)) $((H*38/100)) 500; sleep 1.2
  done
  log "LOGIN: remember-me $(python3 $WF checkstate $OUT/ui.xml 'Запомни ме' | cut -c1-80); the sign-in BUTTON: ${b:-NOT FOUND}; all 'Вход' nodes: $(python3 $H_/find.py $OUT/ui.xml --nodes text '=Вход' | cut -c1-400 | tr '\n' '|')"
  [ -n "$b" ] || { debug_ui login-button; return 1; }
  eshot dv/03-login-filled >/dev/null
  bx=$(echo $b | cut -d' ' -f1); by=$(echo $b | cut -d' ' -f2)
  if [ "$1" = rec ]; then REC_W=648 rec_cmd login-fly emu 6 a input tap $bx $by; else a input tap $bx $by; sleep 6; fi
  eshot dv/04-after-login >/dev/null
  ready >/dev/null; tab my >/dev/null; sleep 3; dump
  log "MY after the sign-in: $(has 'Демо Читател' && echo 'SIGNED IN' || echo 'NOT signed in'): $(texts 12 | cut -c1-300)"
  has 'Демо Читател'
}
year_walk() {  # без докосвания: слайдовете се сменят сами (5.5 s); снимка и описание при всеки нов
  local t0 i=0 last="" d n=0 k=0 second=0
  year_open_fresh 25 || { log "YEAR: story did not open: $(texts 10 | cut -c1-200)"; return 1; }
  t0=$(now_ms)
  while [ $(( $(now_ms) - t0 )) -lt 80000 ]; do
    if dump; then
      d=$(python3 $W25 slidedesc $OUT/ui.xml)
      grep -qE 'Затвори|Close|Слайд|Slide' $OUT/ui.xml || { log "YEAR: the story is no longer on screen after $(( ($(now_ms) - t0) / 1000 )) s: $(texts 6 | cut -c1-160)"; break; }
      if [ "$d" != "$last" ]; then
        i=$((i + 1)); last="$d"; second=0
        eshot dv/y-$(printf %02d $i)-a >/dev/null; log "YEAR slide #$i at +$(( ($(now_ms) - t0) / 1000 )) s: $d"
        echo "$d" | grep -q 'Най-харесана' && { cp $OUT/ui.xml $OUT/year-favourite.xml; log "YEAR: the favourite-book slide is #$i"; }
      elif [ $second = 0 ]; then
        second=1; eshot dv/y-$(printf %02d $i)-b >/dev/null
      fi
    else
      k=$((k + 1)); [ $k -le 20 ] && eshot dv/yt-$(printf %02d $k) >/dev/null
      sleep 1.5
    fi
    n=$((n + 1))
  done
  log "YEAR walk: $i distinct slides in $(( ($(now_ms) - t0) / 1000 )) s ($n polls, $k failed dumps)"
  [ "$(ystate)" = open ] && back 2
}
card_at() {  # $1 — етикет: дневникът се отваря (историята в кеша), после картата; лентата „Твоята шевица“
  a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 6; dismiss
  ready >/dev/null; tab my >/dev/null; sleep 3; dump; log "CARD $1: Моето: $(has 'Демо Читател' && echo 'signed in' || echo 'NOT signed in')"
  open_link_dev my/history 6; back 1.5
  card_screen; sleep 3; eshot dv/card-$1 >/dev/null; dump; cp $OUT/ui.xml $OUT/card-$1.xml
  log "CARD $1 ($(a date +%Y-%m-%d | tr -d '\r')): band $(has 'Твоята шевица за' && echo "PRESENT ($(grep -oE 'content-desc="Твоята шевица за[^"]*"' $OUT/ui.xml | head -1))" || echo absent); $(texts 10 | cut -c1-240)"
  tap text "$L_CARDSHOW" >/dev/null && { sleep 3; eshot dv/card-full-$1 >/dev/null; dump; log "CARD full $1: band $(has 'Твоята шевица за' && echo PRESENT || echo absent)"; back 1.5; }
  leave
}
part_dev() {
  local p b l t r bt i D0 x
  helper_install
  dev_install
  anims 1
  warmup
  CFG=dev33; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt; mark dv
  sec "D1. Sign-in with the BUTTON; „Картата е издадена“ recorded from the host (10 frames/s)"
  ready >/dev/null; tab my >/dev/null; sleep 3; eshot dv/01-my-guest >/dev/null
  dev_signin rec || { log "first sign-in attempt failed, once more without the recording"; dev_signin no || log "DEMO sign-in FAILED twice"; }
  [ -d $OUT/frames/login-fly ] && log "FRAMES login-fly: $(python3 $W25 motion $OUT/frames/login-fly 2>&1 | tail -1 | cut -c1-600)"
  eshot dv/05-my-signed-in >/dev/null; sw_up 500; sleep 1; eshot dv/06-my-signed-in-2 >/dev/null; top_of
  sec "D2. Loans: „Удължи срока“ on demo-1 (debug branch: accepted at once, new due date) → „Нов лист“, host recording"
  open_link_dev my/loans; sleep 2; eshot dv/07-loans >/dev/null; dump; log "LOANS: $(texts 24 | cut -c1-500)"
  p=$(xy text '=Удължи срока|=Renew')
  if [ -n "$p" ]; then
    REC_W=648 rec_cmd renew-leaf emu 5 a input tap $p
    eshot dv/08-after-renew >/dev/null; dump; cp $OUT/ui.xml $OUT/loans-after-renew.xml
    log "RENEW: leaf node $(has 'Нов срок:' && echo "PRESENT: $(grep -oE 'content-desc="Нов срок:[^"]*"' $OUT/ui.xml | head -1)" || echo absent); $(texts 30 | grep -oE '(Заявено удължаване|Удължено|Срокът е удължен|чака библиотеката|Удължаването)[^"]*' | head -3 | tr '\n' '|')"
    [ -d $OUT/frames/renew-leaf ] && log "FRAMES renew-leaf: $(python3 $W25 motion $OUT/frames/renew-leaf 2>&1 | tail -1 | cut -c1-600)"
    # втори път: останалото заемане с „Удължи срока“ (demo-2) остава „чака библиотеката“ — без лист
    p=$(xy text '=Удължи срока|=Renew'); [ -n "$p" ] && { a input tap $p; sleep 3; dump; eshot dv/09-renew-pending >/dev/null; log "RENEW second loan (pending): leaf nodes $(grep -c 'Нов срок:' $OUT/ui.xml); $(texts 30 | grep -oE '(Заявено удължаване|чака библиотеката)[^"]*' | head -2 | tr '\n' '|')"; }
  else log "RENEW: no 'Удължи срока' button"; fi
  back 1.5
  sec "D3. Diary: 5 rosettes on the first book, then the year story with no taps (slides change by themselves)"
  open_link_dev my/history 5; sleep 1
  for i in 1 2 3 4; do dump; b=$(xy --bounds content-desc 'Оценка на книгата|Book rating'); [ -n "$b" ] && break; sw_up 400; sleep 1; done
  if [ -n "$b" ]; then read l t r bt <<< "$b"; a input tap $(( r - (r - l) * 6 / 100 )) $(( (t + bt) / 2 )); sleep 2; eshot dv/10-rated >/dev/null; dump; log "DIARY rated: $(grep -oE 'content-desc="[^"]*Оценка[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  else log "DIARY: no rating node: $(texts 16 | cut -c1-300)"; fi
  back 1.5
  year_walk
  sec "D4. Tablet landscape (1280x800 dp), signed in: two panes in Моето (uiautomator, the card pane is FLAG_SECURE)"
  a wm size 2560x1600; a wm density 320; sleep 4; dims; restart; dims
  ready >/dev/null; tab my >/dev/null; sleep 3; eshot dv/t-my >/dev/null; dump; cp $OUT/ui.xml $OUT/tablet-my.xml
  log "TABLET Моето ($W x $H): $(has 'Демо Читател' && echo 'signed in' || echo 'NOT signed in')"; python3 $W25 panes $OUT/ui.xml $W | cut -c1-600 | tee -a $SUM
  for t in '=Моите книги|=My books' '=Читателска карта|=Library card' '=Моето членство|=My membership' '=История|=Reading history|История на заемане' '=Искам да прочета|=Want to read'; do
    dump; p=$(xy text "$t" --maxy $((H*97/100)))
    [ -n "$p" ] || { a input swipe $((W*20/100)) $((H*75/100)) $((W*20/100)) $((H*35/100)) 500; sleep 1.5; dump; p=$(xy text "$t" --maxy $((H*97/100))); }
    if [ -n "$p" ]; then
      a input tap $p; sleep 3.5; x=$(echo "$t" | tr -dc 'a-zA-Z' | cut -c1-10); eshot dv/t-my-$x >/dev/null; dump; cp $OUT/ui.xml $OUT/tablet-my-$x.xml
      log "TABLET Моето › '$t' (tapped at $p): tabs $(has 'text="(Начало|Home)"' && echo visible || echo hidden); back arrow $(has "$RX_BACK" && echo yes || echo no)"; python3 $W25 panes $OUT/ui.xml $W | cut -c1-600 | tee -a $SUM
    else log "TABLET: '$t' not found in Моето: $(texts 20 | cut -c1-300)"; fi
  done
  a wm size reset; a wm density reset; sleep 3; dims; restart
  sec "D5. The card: 14 November (no band), 15 December and 15 January (the „Твоята шевица“ band)"
  D0=$(a date +%m%d%H%M%Y.%S | tr -d '\r')
  card_at nov
  for t in 121510002026.00:dec 011510002027.00:jan; do
    set_date ${t%%:*}; card_at ${t##*:}
  done
  set_date $D0; a settings put global auto_time 1
  log "DEV part: $(since_fatal dv)"
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}

# ================================================================= 3. ВРЕМЕ ЗА КАДЪР (framestats)
gest_init() {  # жестовете — един скрипт на устройството (без закъсненията на adb между тях)
  local x=$((W/2)) y1=$((H*75/100)) y2=$((H*25/100))
  cat > $OUT/g-scroll.sh <<EOF
i=0; while [ \$i -lt 6 ]; do input swipe $x $y1 $x $y2 60; sleep 0.9; i=\$((i+1)); done
i=0; while [ \$i -lt 4 ]; do input swipe $x $y2 $x $y1 60; sleep 0.9; i=\$((i+1)); done
EOF
  timeout 20 adb push $OUT/g-scroll.sh /data/local/tmp/g-scroll.sh >/dev/null 2>&1
  log "gestures: 6 flings up ($x,$y1 → $x,$y2 in 60 ms, 0.9 s apart), then 4 down; one shell script on the device"
}
gest() { T=${2:-150} a sh /data/local/tmp/$1.sh >/dev/null 2>&1; }
fs_before() { mkdir -p $OUT/fs; a dumpsys gfxinfo $PKG reset >/dev/null 2>&1; sleep 0.3; a dumpsys gfxinfo $PKG framestats > $OUT/fs/before.txt 2>/dev/null; }
fs_take() {  # $1 — сценарий, $2 — етикет (издание / конфигурация), $3 — повторение
  local f=$OUT/fs/$1-$2-$3.txt
  a dumpsys gfxinfo $PKG framestats > $f 2>/dev/null
  log "  $(python3 $W25 fs $OUT/fs/before.txt $f $1 $2 $3 $OUT/frames.csv | cut -c1-430)"
  echo "$1 $2 rep$3 $(python3 $H_/parse.py gfx $f - | tr -s ' ' | cut -c1-220)" >> $OUT/gfx-hist.txt
  rm -f $f
}
sc_home() { ready >/dev/null; tab home >/dev/null; sleep 3; top_of; sleep 2.5; fs_before; gest g-scroll; sleep 1.5; fs_take home $1 $2; top_of; }
sc_more() { ready >/dev/null; tab more >/dev/null; sleep 3; top_of; sleep 2.5; fs_before; gest g-scroll; sleep 1.5; fs_take more $1 $2; top_of; }
sc_timeline() {
  more_tile "$L_TIMELINE" || return 1
  sleep 4; fs_before; gest g-scroll; sleep 1.5; fs_take timeline $1 $2
  [ "$2" = 1 ] && shot fr/timeline-$1 >/dev/null
  leave
}
quiz_learn_taps() {  # $1 — етикет: първият отговор на всеки въпрос и „Напред“ → скрипт за повторение qz-<етикет>.sh
  local L=$1 i o n s
  : > $OUT/qz-$L.sh
  quiz_open || return 1
  for i in 1 2 3 4 5; do
    dump; o=$(python3 $W24 quizopts $OUT/ui.xml $H $W | grep -m1 '^A' | cut -f1); o=${o#A }
    [ -n "$o" ] || { log "QUIZ taps $L q$i: no answers: $(texts 10 | cut -c1-200)"; break; }
    a input tap $o; sleep 2
    s=""; dump; n=$(python3 $W24 quizopts $OUT/ui.xml $H $W | awk '$1=="N"{print $2, $3}')
    [ -n "$n" ] || { sw_up 300; sleep 1; s="input swipe $((W/2)) $((H*3/4)) $((W/2)) $((H/4)) 300; sleep 1; "; dump; n=$(python3 $W24 quizopts $OUT/ui.xml $H $W | awk '$1=="N"{print $2, $3}'); }
    [ -n "$n" ] || { log "QUIZ taps $L q$i: no next: $(texts 10 | cut -c1-200)"; break; }
    echo "input tap $o; sleep 2; ${s}input tap $n; sleep 2.2" >> $OUT/qz-$L.sh
    a input tap $n; sleep 2.2
  done
  log "QUIZ taps $L: $(wc -l < $OUT/qz-$L.sh) questions learned: $(tr '\n' '~' < $OUT/qz-$L.sh | cut -c1-300); result: $(dump; python3 $W24 quizres $OUT/ui.xml | head -1) $(texts 4 | cut -c1-120)"
  leave
  [ "$(wc -l < $OUT/qz-$L.sh)" = 5 ]
}
sc_quiz() {
  [ -s $OUT/qz-$1.sh ] || return 1
  timeout 20 adb push $OUT/qz-$1.sh /data/local/tmp/qz.sh >/dev/null 2>&1
  quiz_open || return 1
  sleep 3; fs_before; gest qz 200; sleep 2; fs_take quiz $1 $2
  [ "$2" = 1 ] && shot fr/quiz-$1 >/dev/null
  leave
}
# Едно повторение в нов процес: $1 — new | base | hook, $2 — етикет, $3 — повторение (0 — загряване: учи викторината),
# $4 — флагове (само hook), $5 — сценариите
fr_rep() {
  local K=$1 L=$2 r=$3 fl=$4 s
  x_use $K
  x_stop_all; sleep 1; quiz_file_rm
  launch_f "$fl"; sleep 8; dismiss; ready >/dev/null
  for s in $5; do
    if [ $s = quiz ] && [ $r = 0 ]; then quiz_learn_taps $L || log "QUIZ taps $L: not learned"; else sc_$s $L $r || log "  scenario $s $L rep$r: not run"; fi
  done
  a am force-stop $PKG
}
fr_setup() {  # $1… — изданията: инсталиране, първо пускане, speed-profile
  local K
  anims 1
  for K in "$@"; do x_install $K && x_compile; done
  x_use new; dims; gest_init
  log "animations: $(anim_state); host load: $(cut -d' ' -f1-3 /proc/loadavg)"
}
part_fr() {
  local r K order
  sec "F0. 1.2.38 (org.chyavorec.app) and 1.2.37 (org.chyavorec.app.b37) side by side, both compiled speed-profile"
  fr_setup new base
  sec "F1. Warm-up (rep 0, not counted): every scenario once; the quiz taps are learned"
  for K in new base; do fr_rep $K $K 0 "" "home more timeline quiz"; done
  for r in 1 2 3 4 5 6; do
    case "$PART-$((r % 2))" in fr33a-1|fr33b-0) order="new base";; *) order="base new";; esac
    sec "F2. Repetition $r, order: $order (a fresh process for each; Home, Още, timeline, quiz)"
    for K in $order; do fr_rep $K $K $r "" "home more timeline quiz"; done
  done
  sec "F3. Totals (rep 0 is excluded)"
  python3 $W25 fsagg $OUT/frames.csv $OUT/frames.md | tee -a $SUM
  x_use new
  log "FR part: FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt); host load: $(cut -d' ' -f1-3 /proc/loadavg)"
}
# Приписване: конфигурациите се въртят във всяко повторение. Всяка: етикет:издание:флагове
at_round() {  # $1 — сценариите, $2 — повторения, $3… — конфигурациите
  local scen=$1 reps=$2 r c n i L K fl; shift 2
  n=$#
  for r in $(seq 0 $reps); do
    sec "A. Repetition $r of: $scen (configs rotated by $r)"
    for i in $(seq 0 $((n - 1))); do
      c=$(eval echo \${$(( (i + r) % n + 1 ))})
      L=$(echo $c | cut -d: -f1); K=$(echo $c | cut -d: -f2); fl=$(echo $c | cut -d: -f3)
      # rep 0: загряване; за викторината — и научаване на докосванията за тази конфигурация
      case "$scen" in *quiz*) fr_rep $K $L $r "$fl" "$scen";; *) [ $r = 0 ] && [ $i -ge 3 ] && continue; fr_rep $K $L $r "$fl" "$scen";; esac
    done
  done
}
part_at() {
  if [ $PART = at33t ]; then
    sec "AT0. Timeline and quiz: one suspect at a time (hook build flags), with 1.2.38 and 1.2.37 as references"
    fr_setup new base hook
    at_round "timeline" 5 new:new: base:base: hk:hook: scrub:hook:noscrub medal:hook:plainmedal thread:hook:solidthread card:hook:plaincard enter:hook:noenter all:hook:noscrub,plainmedal,nothread,plaincard,noenter
    at_round "quiz" 5 hk:hook: noband:hook:noband
  else
    sec "AH0. Home and Още: one suspect at a time (hook build flags), with 1.2.38 and 1.2.37 as references"
    fr_setup new base hook
    at_round "home more" 5 new:new: base:base: hk:hook: plate:hook:plainplate icon:hook:noicon both:hook:plainplate,noicon
  fi
  sec "A9. Totals (rep 0 is excluded)"
  python3 $W25 fsagg $OUT/frames.csv $OUT/frames.md | tee -a $SUM
  x_use new
  log "AT part: FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt); host load: $(cut -d' ' -f1-3 /proc/loadavg)"
}

# ================================================================= 4. ПАМЕТ: 30 цикъла
mem_take() {  # $1 — издание, $2 — цикъл: trim RUNNING_CRITICAL + GC (SIGUSR1), после meminfo
  local pid
  pid=$(pidof_app)
  a am send-trim-memory $PKG RUNNING_CRITICAL >/dev/null 2>&1; sleep 2
  [ -n "$pid" ] && a kill -10 $pid >/dev/null 2>&1; sleep 3
  [ -n "$pid" ] && a kill -10 $pid >/dev/null 2>&1; sleep 4
  mkdir -p $OUT/meminfo; a dumpsys meminfo $PKG > $OUT/meminfo/$1-c$(printf %02d $2).txt 2>/dev/null
  log "$(python3 $W25 mem $OUT/meminfo/$1-c$(printf %02d $2).txt $1 $2 $OUT/mem.csv | cut -c1-360) pid ${pid:-?}"
}
mem_run() {  # $1 — new | base
  local K=$1 c
  x_use $K; x_stop_all; sleep 1
  launch >/dev/null; sleep 8; dismiss; ready >/dev/null; dims; tab home >/dev/null; sleep 6
  mem_take $K 0
  for c in $(seq 1 30); do
    more_tile "$L_TIMELINE" && { sleep 2; sw_up 600; sleep 0.6; sw_up 600; sleep 1; leave; }
    more_tile "$L_QUIZ" && { sleep 3; leave; }
    ready >/dev/null; tab more >/dev/null; sleep 1.5; sw_up 500; sleep 1; top_of
    link "$BOOK_URL" >/dev/null; sleep 4; leave
    ready >/dev/null; tab home >/dev/null; sleep 1.5
    case $c in 5|10|15|20|25|30) mem_take $K $c;; esac
  done
  log "MEM $K: pid at the end $(pidof_app); process deaths during the run: $(grep -cE "Process $PKG \(pid [0-9]+\) has died" $OUT/logcat-live.txt)"
  a am force-stop $PKG
}
part_memx() {
  local K order
  anims 1
  sec "M0. Both versions installed side by side (speed-profile)"
  for K in new base; do x_install $K && x_compile; done
  case $PART in mem33a) order="new base";; *) order="base new";; esac
  for K in $order; do
    sec "M1. $K: 30 cycles (timeline + 2 scrolls, quiz, Още + scroll, book details), meminfo after trim + GC every 5"
    mem_run $K
  done
  sec "M2. Totals"
  python3 $W25 memagg $OUT/mem.csv | tee -a $SUM
  x_use new
  log "MEM part: FATAL lines $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
}

# ================================================================= 5a. УИДЖЕТИТЕ СЛЕД ТЪМНА ТЕМА
wd_state() {  # $1 — етикет
  go_home 3; shot widget/$1 >/dev/null
  log "WIDGET $1: $(wstate | cut -c1-420)"
  log "WIDGET $1: bound ids [$(wcount $1 >/dev/null; wids)]; providers: $(grep -oE 'ComponentInfo\{org\.chyavorec[^}]*' $OUT/appwidget-$1.txt | sort | uniq -c | tr -s ' ' | tr '\n' ' ' | cut -c1-260)"
}
part_wd() {
  local t t0
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || true; }
  anims 1
  x_use new
  warmup
  card_open >/dev/null; leave
  mark wd
  log "launcher: $(launcher)"
  sec "WD1. Both widgets pinned from Settings › Уиджети (the launcher's pin dialog)"
  pin_from_settings 'Добави уиджета|Add the .* widget|Add the widget' main; wd_state 10-main
  pin_from_settings 'Добави „Книга на деня“|Книга на деня|Book of the day' book; wd_state 20-both
  ready >/dev/null; tab catalog >/dev/null; waitfor "$RX_RESULTS" 100 >/dev/null; wd_state 21-after-catalog
  sec "WD2. App theme → „Тъмна“ (Settings › Тема), system still light: 5 s, 40 s"
  app_theme '=Тъмна|=Dark'; sleep 3; wd_state 30-app-dark-5s; sleep 33; wd_state 31-app-dark-40s
  sec "WD3. System dark mode on as well: 10 s, 40 s, 90 s"
  a cmd uimode night yes; t0=$(now_ms)
  for t in 10 40 90; do while [ $(( $(now_ms) - t0 )) -lt $((t * 1000)) ]; do sleep 0.5; done; wd_state 40-sys-dark-${t}s; done
  log "launcher/appwidget log since the pins: $(since wd 'removeAppWidget|deleteAppWidget|onDeleted|deleteHost|unbind|Widget.*(remov|delet|restor)|AppWidgetServiceImpl' | grep -v PERFCHECK | head -10 | cut -c1-230 | tr '\n' '|')"
  sec "WD4. Launcher restarted in dark (force-stop of the launcher), then system light and app theme „Както системата“"
  a am force-stop $(launcher | cut -d/ -f1) >/dev/null 2>&1; sleep 2; wd_state 50-launcher-restarted
  a cmd uimode night no; sleep 12; wd_state 60-sys-light
  app_theme '=Както системата|=Same as the system|=System'; sleep 3; wd_state 61-app-system
  a dumpsys appwidget 2>/dev/null | tr -d '\r' | grep -E 'provider=.*org\.chyavorec|^ *\[[0-9]+\] id=|host=' | head -12 | cut -c1-200 | tee -a $SUM
  log "WD part: $(since_fatal wd)"
  all_sheets
}

# ================================================================= 5b. ШРИФТ 2.0
capx() { cap $1 ${2:-1}; log "    $(python3 $W25 clipped $OUT/ui.xml $W $H | cut -c1-420)"; }
part_fs() {
  local o n i p
  helper_install
  anims 1
  x_use new
  warmup
  a settings put system font_scale 2.0; sleep 3; restart
  CFG=font200; DENS=$(dens); dims; : > $OUT/a11y-$CFG.txt; mark fs
  log "font_scale $(a settings get system font_scale | tr -d '\r'); $(cfg_desc)"
  sec "FS1. Quiz at font scale 2.0: question, answered (feedback + band + Напред), result"
  quiz_file_rm
  if quiz_open; then
    capx quiz-q1 1
    dump; python3 $W24 quizopts $OUT/ui.xml $H $W > $OUT/qo.txt; log "QUIZ options visible without scrolling: $(grep -c '^A' $OUT/qo.txt) of 4; next button: $(grep -c '^N' $OUT/qo.txt)"
    sw_up 400; capx quiz-q1-scrolled 1.2; top_of
    for i in 1 2 3 4 5; do
      dump; o=$(python3 $W24 quizopts $OUT/ui.xml $H $W | grep -m1 '^A' | cut -f1); o=${o#A }
      [ -n "$o" ] || { sw_up 400; sleep 1; dump; o=$(python3 $W24 quizopts $OUT/ui.xml $H $W | grep -m1 '^A' | cut -f1); o=${o#A }; }
      [ -n "$o" ] || { log "FS quiz q$i: no reachable answer: $(texts 10 | cut -c1-200)"; break; }
      a input tap $o; sleep 2.2; [ $i -le 2 ] && capx quiz-q$i-answered 0.3
      n=$(quiz_next_xy); [ -n "$n" ] || { log "FS quiz q$i: 'Напред' NOT reachable: $(texts 12 | cut -c1-240)"; break; }
      a input tap $n; sleep 2
    done
    capx quiz-result 1.5; sw_up 400; capx quiz-result-2 1.2
    leave
  fi
  sec "FS2. Timeline at font scale 2.0: top, while scrolling (the decade scrubber), further down"
  if more_tile "$L_TIMELINE"; then
    capx tl-1 3
    a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*45/100)) 900 & p=$!; sleep 0.6; shot tour/$CFG/tl-2-while-scrolling >/dev/null; wait $p
    capx tl-3 0.3; sw_up 500; capx tl-4 1.5; sw_up 500; sleep 0.3; shot tour/$CFG/tl-5-scrubber >/dev/null; capx tl-6 1.5
    leave
  fi
  sec "FS3. Settings at font scale 2.0 (every screenful)"
  if open_settings; then capx set-1 1; for i in 2 3 4 5 6 7 8 9 10; do slow_up; capx set-$i 0.5; done; back 1.5; fi
  sec "FS4. Login at font scale 2.0 (FLAG_SECURE: host screenshots + uiautomator)"
  ready >/dev/null; tab my >/dev/null; sleep 2; capx my-1 0.5; sw_up 400; capx my-2 1.2; top_of
  if tap text "$L_SIGNIN" >/dev/null; then
    sleep 3; hide_ime; sleep 1
    for i in 1 2 3 4; do
      eshot fs/login-$i >/dev/null; dump; cp $OUT/ui.xml $OUT/login-fs-$i.xml
      log "LOGIN font 2.0, screenful $i: $(python3 $W25 clipped $OUT/ui.xml $W $H | cut -c1-300); sign-in button: $(python3 $W25 loginbtn $OUT/ui.xml $W $H | cut -c1-120); texts: $(texts 16 | cut -c1-360)"
      a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*40/100)) 500; sleep 1.2
    done
    back 2
  fi
  sec "FS5. The same four at „Много голям“ in the app + system 2.0 is capped at 2.0 — only Home for reference"
  ready >/dev/null; tab home >/dev/null; capx home-1 2
  a settings put system font_scale 1.0; sleep 2
  python3 $W20 a11yagg $OUT/a11y-$CFG.txt | head -30 | tee -a $SUM
  log "FS part: $(since_fatal fs)"
  all_sheets
}

# ---------------------------------------------------------------- маршрутизация
part_w25() {
  case "$W25_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    cf33|cf28|cf36x) part_cf;;
    dev33) part_dev;;
    fr33a|fr33b) part_fr;;
    at33t|at33h) part_at;;
    mem33a|mem33b) part_memx;;
    wd33|wd36x) part_wd;;
    fs33) part_fs;;
  esac
  return 0
}
