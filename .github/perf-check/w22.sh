# Проверка на тестовото v1.2.36 (ВРЕМЕННО, debug/w22-check; резултати в emulator-results/v136).
# Тестовото v1.2.36 (80efffa, versionCode 1507400) срещу издаденото v1.2.35 (aef8910, 1507364); и двете
# prodRelease x86_64 с временен ключ; devDebug от 80efffa за демо входа. Зарежда се от run.sh след a6.sh и
# ползва функциите на по-старите части. „new“ = 1.2.36, „base“ = 1.2.35.
#   upd36 / upd28 — 1.2.35 със състояние (карта на ръка, „Искам да прочета“, размер на текста, тема, уиджет) →
#                   `adb install -r` на 1.2.36: стартира ли, миграцията на Room 2→3, данните, уиджетът, logcat
#   onb36         — нова инсталация: BrandIntro, въведението (3 страници), Начало; шрифт 1.3 + английски; хоризонтално
#   new36         — новите екрани (Още › Извези шевица, Старият вестник, Лента на времето, Познай Яворец, Настройки,
#                   събитие като лист): светло (подробно), тъмно и английски (кратко)
#   new36b        — същото, кратко: шрифт 1.3 / „Много голям“ (bg, en), хоризонтално, 320dp, таблет
#   new28, new26  — new36 на API 28 / 26 (светло подробно, тъмно кратко)
#   mon36         — monkey 2 × 6000 (семена 1357 и 4242)
#   start36a/b    — студен старт A/B, n=20 на издание (две независими изпълнения с обратен ред)
#   gfx36, gfxb36 — gfxinfo по сценарии (1.2.36 / 1.2.35)
#   mem36, memb36 — памет, 30 цикъла (1.2.36 / 1.2.35)
#   perf36        — покой (CPU/кадри) и плавност на новите екрани; памет след обиколка на новите екрани
#   diary36       — devDebug: читателски дневник, печат „Върната“, годишната история „Твоята шевица“
#   widget36      — уиджетите (стар и „Книга на деня“) от бутоните в Настройки, пряк път „Шевица“, икона
W22_PARTS=" upd36 upd28 onb36 new36 new36b new28 new26 mon36 start36a start36b gfx36 gfxb36 mem36 memb36 memL36 membL36 perf36 diary36 widget36 set36 anr36 "
W20_PARTS="$W20_PARTS $W22_PARTS"
W22=$H_/w22.py
L_EMB='Извези шевица|Embroider a pattern'
L_NEWS_PAPER='Старият вестник|The old newspaper'
L_TIMELINE='Лента на времето|Timeline'
L_QUIZ='Познай Яворец|Know Yavorets'

# ---------------------------------------------------------------- общи
more_tile() {  # $1 — заглавие на плочката в „Още“ (bg|en) → отваря екрана
  local i p
  ready >/dev/null; tab more >/dev/null; sleep 2; dims
  for i in 1 2 3 4 5 6; do dump; p=$(xy text "$1" --maxy $((H*97/100))); [ -n "$p" ] && break; sw_up 300; sleep 1; done
  [ -n "$p" ] || { log "MORE: tile '$1' not found: $(texts 16 | cut -c1-300)"; debug_ui more-tile; return 1; }
  a input tap $p; sleep 3
}
tapfrac() {  # L T R B fx fy (в %) — докосване в правоъгълника
  a input tap $(( $1 + ($3 - $1) * $5 / 100 )) $(( $2 + ($4 - $2) * $6 / 100 ))
}
db_pull() {  # $1 — етикет: базата на потребителските данни (+ -wal/-shm) → резюме
  local f d=$OUT/db/$1
  mkdir -p $d
  for f in chitalishte.db chitalishte.db-wal chitalishte.db-shm; do timeout 30 adb exec-out "cat /data/data/$PKG/databases/$f 2>/dev/null" > $d/$f 2>/dev/null; [ -s $d/$f ] || rm -f $d/$f; done
  ls -l $d | tail -n +2 | awk '{print $5, $NF}' | tr '\n' ';' > $d/ls.txt
  log "DB[$1] files: $(cat $d/ls.txt)"
  log "$(python3 $W22 db $d/chitalishte.db)"
}
files_list() {  # $1 — етикет: файловете на приложението (без кеша) с размери
  a "cd /data/data/$PKG 2>/dev/null && find . -type f ! -path './cache/*' ! -path './code_cache/*' ! -path './databases/*-wal' ! -path './databases/*-shm' -exec stat -c '%n %s' {} +" 2>/dev/null | tr -d '\r' | sort > $OUT/files-$1.txt
  log "FILES[$1]: $(wc -l < $OUT/files-$1.txt) files: $(awk '{print $1}' $OUT/files-$1.txt | sed 's|^\./||' | tr '\n' ' ' | cut -c1-500)"
}
wn36() {  # $1 — етикет: „Какво ново във версия …“ след обновяването
  local i got=no
  for i in 1 2 3 4 5 6 7 8; do dump; has "$RX_WHATSNEW" && { got=yes; break; }; sleep 1.5; done
  shot update/$1 >/dev/null
  if [ $got = yes ]; then log "WHATS-NEW $1: SHOWN: $(texts 16 | cut -c1-600)"; else log "WHATS-NEW $1: not shown; screen: $(texts 8 | cut -c1-200)"; fi
  [ $got = yes ]
}
since_fatal() {  # $1 — маркер: ред за срив/ANR на приложението след него
  local n a f
  n=$(since "$1" 'FATAL EXCEPTION' | wc -l); a=$(since "$1" 'ANR in ' | wc -l); f=$(since "$1" 'Process: org\.chyavorec\.app' | wc -l)
  echo "FATAL(any process)=$n of the app=$f ANR=$a"
}

# ---------------------------------------------------------------- обновяване 1.2.35 → 1.2.36
part_upd36() {
  local p i ids0 r
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || true; }
  anims 1
  sec "U0. Install 1.2.35 ($(cut -c1-90 apk-base-ref.txt)) as a fresh install"
  inst app-base.apk base apk-base-ref.txt || return
  a input keyevent KEYCODE_HOME; sleep 2
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab home; sleep 3
  sec "U1. State on 1.2.35: books in „Искам да прочета“, a card by hand, text size „Голям“, dark theme, the widget, a few screens"
  want_add "$BOOK_URL"; want_add "$SITE/elektronen-katalog?inv=1"; want_add "$SITE/elektronen-katalog?inv=2"
  if [ "$SDK" -ge 33 ]; then card_add_my || log "card NOT added by hand"; else card_add28 || log "card NOT added by hand (28)"; fi
  leave
  set_text_size '=Голям|=Large'; back 1.5; back 1.5
  open_settings && settings_item '=Тема|=Theme' >/dev/null && { sleep 1.5; tap text '=Тъмна|=Dark' --last >/dev/null; sleep 2; back 1.5; }
  leave
  K=b135; a am force-stop $PKG; widget_add b135 && { go_home 3; log "WIDGET on 1.2.35: $(wstate)"; shot update/02-widget-135; }
  wcount before >/dev/null; ids0=$(wids)
  launch >/dev/null; sleep 4; dismiss
  # екрани за „история“ на файловете и кеша
  link_pkg "$SITE/news" >/dev/null; sleep 5; ready >/dev/null; tab more >/dev/null; sleep 2; tab home >/dev/null; sleep 2
  a cmd locale set-app-locales $PKG --locales en-US; sleep 2
  log "locale before the update: $(a cmd locale get-app-locales $PKG | tr -d '\r')"
  upd_state before
  a am force-stop $PKG; sleep 2
  db_pull before; files_list before
  a "ls /data/data/$PKG/shared_prefs /data/data/$PKG/files /data/data/$PKG/datastore 2>/dev/null" | tr -d '\r' | tr '\n' ' ' | cut -c1-400 > $OUT/prefs-before.txt; log "prefs/datastore files before: $(cat $OUT/prefs-before.txt)"
  sec "U2. adb install -r of 1.2.36 over 1.2.35 (same signing key)"
  go_home 2; mark upd-pre
  r=$(timeout 180 adb install -r app.apk 2>&1 | tr -d '\r' | tail -1)
  log "adb install -r 1.2.36: $r; now: $(ver)"
  sleep 6; go_home 3
  log "WIDGET after the update (app not opened): $(wstate)"; shot update/20-widget-after
  wcount after >/dev/null; log "widget ids before/after: [$ids0] / [$(wids)]"
  log "logcat since the install (app not opened): $(since_fatal upd-pre)"
  sec "U3. First launch of 1.2.36 on the old data"
  mark upd-first
  r=$(launch | tr '\n' ' '); log "first launch after the update: $r"
  sleep 4; dump; shot update/30-first-launch; log "first screen: $(texts 14 | cut -c1-300)"
  wn36 31-whats-new && { a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*40/100)) 400; sleep 1.5; shot update/32-whats-new-scrolled; tap text '=Добре|=OK|=Разбрах|=Got it|=Разбрах|=Затвори|=Close' >/dev/null || back 2; }
  sleep 2; dismiss
  log "logcat since the first launch: $(since_fatal upd-first); process $(pidof_app)"
  log "Room/migration lines: $(since upd-pre 'Migration|migrat|IllegalStateException.*Room|SQLiteException|no such table|Room cannot' | head -4 | cut -c1-260 | tr '\n' '|')"
  log "locale after the update: $(a cmd locale get-app-locales $PKG | tr -d '\r') (UI shows: $(texts 4 | cut -c1-100))"
  a cmd locale set-app-locales $PKG --locales bg-BG; sleep 2
  ready >/dev/null
  upd_state after
  sec "U4. Database and files after the update"
  a am force-stop $PKG; sleep 2
  db_pull after; files_list after
  python3 $W22 files $OUT/files-before.txt $OUT/files-after.txt | tee -a $SUM
  # приложението пак — още един студен старт и обиколка на новите екрани върху мигрираните данни
  launch >/dev/null; sleep 5; dismiss; ready >/dev/null
  sec "U5. Widget after opening the app; Diagnostics › Бързодействие after the update; new screens on the migrated data"
  go_home 3; log "WIDGET after opening the app: $(wstate)"; shot update/40-widget-after-app
  K=a136; widget_clicks
  launch >/dev/null; sleep 3
  for i in 1 2 3; do cold_drawn upd >/dev/null; done
  perf_card after-update
  back 1.5; back 1.5
  new_pass after-update brief
  sec "U6. Logcat of the whole part"
  log "FATAL/ANR since the install: $(since_fatal upd-pre)"
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- нова инсталация и въведение
part_onb36() {
  local i p
  helper_install
  anims 1
  sec "O1. Fresh install of 1.2.36: BrandIntro (recorded) and onboarding pages"
  inst app.apk new apk-ref.txt || return
  a input keyevent KEYCODE_HOME; sleep 2
  mark onb1
  rec_start brandintro 7; launch >/dev/null; rec_end; mkdir -p $OUT/shots/first; cp $OUT/rec/brandintro/f0{04,06,08,10,12,16,24,32}.png $OUT/shots/first/ 2>/dev/null
  python3 $W20 strip $OUT/shots/first/brandintro-strip.png all $OUT/rec/brandintro/f0{02,04,06,08,10,12,14,18,24}.png >/dev/null 2>&1
  rec_clean
  dims; DENS=$(dens); CFG=onb-default; : > $OUT/a11y-$CFG.txt
  sleep 1; cap p1-welcome 1
  sw_up 400; cap p1b-welcome-scrolled 1; sw_down 400; sleep 1
  tap text '=Напред|=Next' >/dev/null; cap p2-settings 2
  tap text '=Много голям|=Extra large' >/dev/null; cap p2b-xlarge 1.5
  tap text '=Стандартен|=Standard' >/dev/null; sleep 1
  tap text '=Напред|=Next' >/dev/null; cap p3-signin 2.5
  a input swipe $((W/2)) $((H/2)) $((W/2)) $((H/2+300)) 300; sleep 1; cap p3b-sample-card-tilt 1
  emu sensor set acceleration 4.5:8.7:0 >/dev/null; sleep 2; shot onb/p3-tilt-a >/dev/null; emu sensor set acceleration -4.5:8.7:0 >/dev/null; sleep 2; shot onb/p3-tilt-b >/dev/null; emu sensor set acceleration 0:9.81:0 >/dev/null
  tap text '=Продължи без вход|=Continue without signing in|=Продължи|=Пропусни' >/dev/null; sleep 6
  cap p4-home-first 4
  log "ONB default: FATAL/ANR $(since_fatal onb1); main screen after: $(texts 8 | cut -c1-200)"
  ready >/dev/null; tab catalog >/dev/null; waitfor "$RX_RESULTS" 120 && log "catalogue (first install) after ${WAITED} ms"
  perf_card fresh
  back 1.5; back 1.5
  sec "O2. Onboarding again (data cleared): font 1.3 + English; then landscape; then a skip on page 1"
  a settings put system font_scale 1.3
  a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; a cmd locale set-app-locales $PKG --locales en-US
  mark onb2; launch >/dev/null; sleep 5; CFG=onb-font130-en; : > $OUT/a11y-$CFG.txt
  cap p1-welcome 1.5; sw_up 400; cap p1b-welcome-scrolled 1; sw_down 400; sleep 1
  tap text '=Напред|=Next' >/dev/null; cap p2-settings 2; sw_up 400; cap p2b-scrolled 1; sw_down 400; sleep 1
  tap text '=Напред|=Next' >/dev/null; cap p3-signin 2.5; sw_up 400; cap p3b-scrolled 1
  a settings put system font_scale 1.0
  a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; a cmd locale set-app-locales $PKG --locales bg-BG
  rot 1; sleep 1; launch >/dev/null; sleep 5; CFG=onb-landscape; : > $OUT/a11y-$CFG.txt
  cap p1-welcome 1.5; sw_up 400; cap p1b-welcome-scrolled 1; sw_down 400; sleep 1
  tap text '=Напред|=Next' >/dev/null; cap p2-settings 2; tap text '=Напред|=Next' >/dev/null; cap p3-signin 2.5
  rot 0
  a pm clear $PKG >/dev/null; a pm grant $PKG android.permission.POST_NOTIFICATIONS; a cmd locale set-app-locales $PKG --locales bg-BG
  launch >/dev/null; sleep 5; tap text '=Пропусни|=Skip' >/dev/null; sleep 5; dump; log "after 'Пропусни' on page 1: $(texts 8 | cut -c1-200)"
  log "ONB part: FATAL/ANR $(since_fatal onb1)"
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -30 | tee -a $SUM
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- новите екрани
s_embroidery() {  # $1 — full | brief
  local mode=$1 b l t r bt n0 n1 n2 n3 i
  more_tile "$L_EMB" || return 1
  cap em-01-open 2
  dump; b=$(xy --bounds content-desc 'Платно за шевица|Embroidery canvas')
  if [ -z "$b" ]; then log "EMB: canvas not found: $(texts 16 | cut -c1-260)"; debug_ui emb; leave; return 1; fi
  read l t r bt <<< "$b"
  n0=$(python3 $W22 stitches $OUT/ui.xml)
  for i in "20 20" "35 35" "50 50" "65 35" "80 20" "50 80"; do tapfrac $l $t $r $bt ${i% *} ${i#* }; sleep 0.4; done
  a input swipe $((l + (r-l)*15/100)) $((t + (bt-t)*70/100)) $((l + (r-l)*85/100)) $((t + (bt-t)*70/100)) 500; sleep 0.8
  dump; n1=$(python3 $W22 stitches $OUT/ui.xml); log "EMB stitches: start $n0 → after 6 taps + a stroke $n1"
  cap em-02-stitched 0.5
  tap content-desc '=Върни|=Undo' >/dev/null; sleep 0.8; dump; n2=$(python3 $W22 stitches $OUT/ui.xml); log "EMB undo: $n1 → $n2"
  tap content-desc '=Повтори|=Redo' >/dev/null; sleep 0.8; dump; n3=$(python3 $W22 stitches $OUT/ui.xml); log "EMB redo: $n2 → $n3"
  if [ $mode = full ]; then
    tap text '=Четворно|=Four-way' >/dev/null; sleep 0.8; dump
    tapfrac $l $t $r $bt 30 60; sleep 0.5; tapfrac $l $t $r $bt 40 45; sleep 0.8; dump; n2=$(python3 $W22 stitches $OUT/ui.xml); log "EMB four-way mirror, 2 taps: $n3 → $n2 (+8 expected)"
    cap em-03-mirror 0.5
    tap content-desc '=Бордо|=Burgundy' >/dev/null; sleep 0.5; tapfrac $l $t $r $bt 70 70; sleep 0.8
    sw_up 500; sleep 1; cap em-04-lower 1
    p=""; for i in 1 2 3; do dump; p=$(xy text 'Ромб|Rhombus|Мотиви'); [ -n "$p" ] && break; sw_up 500; sleep 1; done
    tap content-desc 'Ромб|Rhombus' >/dev/null; sleep 1.2; cap em-05-template 0.5
  fi
  sw_up 500; sleep 1
  for i in 1 2 3 4; do dump; p=$(xy text 'Запази шевицата|Save the pattern'); [ -n "$p" ] && break; sw_up 500; sleep 1; done
  if [ -n "$p" ]; then a input tap $p; sleep 1.5; shot em-saved-snack >/dev/null; dump; log "EMB save: $(texts 30 | grep -oE 'text="[^"]*(запазена|saved|Моите|My patterns)[^"]*"' | head -2 | tr '\n' ' ')"; cap em-06-after-save 1; else log "EMB: save button not found: $(texts 14 | cut -c1-240)"; fi
  for i in 1 2 3; do sw_down 500; done; sleep 1
  if tap content-desc '=Сподели като картичка|=Share as a card' >/dev/null; then
    for i in $(seq 1 12); do sleep 1; chooser_up && break; done
    log "EMB share: chooser $(chooser_up && echo OPEN || echo 'NOT open') after $i s; fg $(fg)"; shot new/emb-share >/dev/null
    chooser_up && back 1.5
  else log "EMB share button not found"; fi
  log "EMB end: front $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)")"
  leave
}
s_newspaper() {  # $1 — full | brief
  local mode=$1 p t0 i ok cx cy
  mark np-read-$CFG
  more_tile "$L_NEWS_PAPER" || return 1
  for i in $(seq 1 10); do dump; [ -n "$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*92/100)) 150)" ] && break; sleep 2; done
  cap np-01-list 1.5
  [ $mode = full ] && { sw_up 500; cap np-01b-list-2 1.5; top_of; }
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*92/100)) 150)
  [ -n "$p" ] || { log "NEWSPAPER: no article row: $(texts 12 | cut -c1-240)"; leave; return 1; }
  a input tap $p; t0=$(now_ms)
  waitfor 'Страница [0-9]+ от [0-9]+|Page [0-9]+ of [0-9]+|Статията не се отвори|не може да се покаже|е защитен|твърде голяма|couldn.t open|can.t be shown' 100
  log "NEWSPAPER first open: page label after ${WAITED} ms (-1 = none in 100 s): $(texts 10 | cut -c1-300)"
  sleep 3; cap np-02-reader 1
  [ -f $OUT/shots/tour/$CFG/np-02-reader.png ] && log "NEWSPAPER page content (std of the brightness, ≈0 = blank): $(python3 $W22 blank $OUT/shots/tour/$CFG/np-02-reader.png)"
  if tap content-desc '=Увеличи|=Zoom in' >/dev/null; then sleep 1; tap content-desc '=Увеличи|=Zoom in' >/dev/null; sleep 2; cap np-03-zoom 0.5
    log "NEWSPAPER zoom buttons: diff vs unzoomed $(python3 $W22 shotdiff $OUT/shots/tour/$CFG/np-02-reader.png $OUT/shots/tour/$CFG/np-03-zoom.png)"
    tap content-desc '=Намали|=Zoom out' >/dev/null; sleep 1; tap content-desc '=Намали|=Zoom out' >/dev/null; sleep 1.5
  else log "NEWSPAPER: no zoom button: $(texts 16 | cut -c1-260)"; fi
  if tap content-desc '=Следваща страница|=Next page' >/dev/null; then sleep 3; cap np-04-page2 0.5; log "NEWSPAPER page 2: $(grep -oE 'Страница [0-9]+ от [0-9]+|Page [0-9]+ of [0-9]+' $OUT/ui.xml | head -1); content $(python3 $W22 blank $OUT/shots/tour/$CFG/np-04-page2.png)"
    tap content-desc '=Предишна страница|=Previous page' >/dev/null; sleep 2
  else log "NEWSPAPER: no next-page button (one page?)"; fi
  if [ $mode = full ] && mt_init; then
    cx=$((W/2)); cy=$((H/2))
    dtap $cx $cy; sleep 2.5; shot new/np-dtap >/dev/null
    log "NEWSPAPER double tap: diff vs page $(python3 $W22 shotdiff $OUT/shots/tour/$CFG/np-02-reader.png $OUT/shots/new/np-dtap.png)"
    dtap $cx $cy; sleep 2
    pinch $cx $cy 300 900 16; sleep 3; shot new/np-pinch >/dev/null
    log "NEWSPAPER pinch: diff vs page $(python3 $W22 shotdiff $OUT/shots/tour/$CFG/np-02-reader.png $OUT/shots/new/np-pinch.png)"
    sw_left 300; sleep 1.5; shot new/np-pan >/dev/null
  fi
  log "NEWSPAPER logcat: $(since np-read-$CFG 'OutOfMemory|PdfRenderer|Pdf.*(Exception|error)|newspaper.*(fail|Exception)' | head -3 | cut -c1-240 | tr '\n' '|'); $(since_fatal np-read-$CFG)"
  back 2
  dump
  if [ $mode = full ]; then
    # друга статия, после офлайн: същата статия се чете от кеша
    sw_up 400; sleep 1; dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*60/100)) 150)
    sw_down 400; sleep 0.8
  fi
  offline
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*92/100)) 150)
  if [ -n "$p" ]; then a input tap $p; t0=$(now_ms)
    waitfor 'Страница [0-9]+ от [0-9]+|Page [0-9]+ of [0-9]+|Статията не се отвори|Не успяхме|couldn.t|offline' 40
    log "NEWSPAPER reopened OFFLINE: after ${WAITED} ms: $(texts 8 | cut -c1-240)"; sleep 2; cap np-05-offline 0.5
    [ -f $OUT/shots/tour/$CFG/np-05-offline.png ] && log "NEWSPAPER offline page content: $(python3 $W22 blank $OUT/shots/tour/$CFG/np-05-offline.png)"
    back 2
  fi
  online; sleep 3
  leave
}
s_timeline() {  # $1 — full | brief
  local mode=$1 i
  more_tile "$L_TIMELINE" || return 1
  sleep 3; cap tl-01 2
  for i in 1 2 3 4 5 6; do
    sw_up 600; sleep 0.8
    if [ $mode = full ] || [ $i = 2 ] || [ $i = 5 ]; then cap tl-0$((i + 1)) 0.5; fi
  done
  for i in 1 2 3 4 5 6; do sw_down 700; done; sleep 1
  leave
}
s_quiz() {  # $1 — full | brief
  local mode=$1 i q a n j
  more_tile "$L_QUIZ" || return 1
  waitfor 'Въпрос [0-9]+ от|Question [0-9]+ of|Днес няма викторина|Нови въпроси|No quiz today|come back' 45 && log "QUIZ ready after ${WAITED} ms: $(texts 8 | cut -c1-240)" || log "QUIZ: no question in 45 s: $(texts 8 | cut -c1-240)"
  cap qz-01-q1 1
  for i in 1 2 3 4 5; do
    dump; q=$(python3 $W22 quizans $OUT/ui.xml $H $W)
    a=$(echo "$q" | awk '$1=="A"{print $2, $3; exit}')
    [ -n "$a" ] || { log "QUIZ q$i: no answer buttons: $(texts 12 | cut -c1-260)"; debug_ui quiz-$i; break; }
    a input tap $a; sleep 1.6
    if [ $mode = full ] || [ $i = 1 ]; then cap qz-0$i-answered 0.3; fi
    n=""
    for j in 1 2 3; do dump; n=$(python3 $W22 quizans $OUT/ui.xml $H $W | awk '$1=="N"{print $2, $3}'); [ -n "$n" ] && break; sw_up 300; sleep 1; done
    [ -n "$n" ] || { log "QUIZ q$i: no next button: $(texts 12 | cut -c1-260)"; break; }
    a input tap $n; sleep 1.6
  done
  sleep 2; cap qz-06-result 1.5
  log "QUIZ result: $(texts 12 | cut -c1-300)"
  if tap text '=Сподели резултата|=Share the result' >/dev/null; then
    for i in $(seq 1 15); do sleep 1; chooser_up && break; done
    log "QUIZ share: chooser $(chooser_up && echo OPEN || echo 'NOT open') after $i s; fg $(fg)"; shot new/quiz-share >/dev/null
    chooser_up && back 1.5
  else log "QUIZ: no share button: $(texts 12 | cut -c1-240)"; fi
  if [ $mode = full ]; then
    tap text '=Назад|=Back' >/dev/null || back 2
    sleep 1.5; more_tile "$L_QUIZ" && { sleep 3; cap qz-07-again 1; log "QUIZ again the same day: $(texts 10 | cut -c1-240)"; }
  fi
  leave
}
s_settings_new() {  # $1 — full | brief
  local mode=$1 s0 s1 i
  open_settings || return 1
  cap st-01 1.5; sw_up 600; cap st-02 1.5; sw_up 600; cap st-03 1.5; sw_up 600; cap st-04 1.5
  if settings_find 'Седмичен преглед|Weekly digest'; then
    dump; s0=$(xy --state text 'Седмичен преглед|Weekly digest')
    log "SETTINGS digest row before: $s0; widget buttons: $(has 'Книга на деня|Book of the day' && echo present || echo ABSENT); favourite sections: $(has 'Любими раздели|Favourite sections' && echo present || echo ABSENT)"
    cap st-05-digest 1
    if [ $mode = full ]; then
      tap text 'Седмичен преглед|Weekly digest' >/dev/null; sleep 2; dump; s1=$(xy --state text 'Седмичен преглед|Weekly digest'); log "SETTINGS digest toggled: $s1"; cap st-06-digest-toggled 0.5
      dump; if tap text 'Любими раздели|Favourite sections' >/dev/null; then sleep 1.8; cap st-07-sections 1; back 1.5; fi
      dump; tap text 'Седмичен преглед|Weekly digest' >/dev/null; sleep 1
    fi
  else log "SETTINGS: no digest row"; fi
  leave
}
new_pass() {  # $1 — етикет на конфигурацията, $2 — full | brief
  local C=$1 mode=${2:-full} p
  CFG=$C; restart; dims; DENS=$(dens)
  [ -n "${TOUR_ROT:-}" ] && { rot $TOUR_ROT; sleep 2; ready >/dev/null; dims; }
  sec "NP-$C ($mode). New screens ($(cfg_desc))"
  : > $OUT/a11y-$C.txt
  mark np-$C
  ready; tab home; cap 01-home 4
  if [ $mode = full ]; then sw_up 500; cap 01b-home-2 1.5; sw_up 500; cap 01c-home-3 1.5; sw_up 500; cap 01d-home-4 1.5; sw_up 500; cap 01e-home-5 1.5; top_of; fi
  dump; p=$(xy text 'Честитка|Greeting card'); [ -n "$p" ] && { log "GREETING shown on Home"; a input tap $p; sleep 2; cap 01g-greeting 1; back 1.5; }
  ready; tab more; cap 02-more 2
  if [ $mode = full ]; then sw_up 500; cap 02b-more-2 1.5; sw_up 600; cap 02c-more-3 1.5; top_of; fi
  s_embroidery $mode
  s_newspaper $mode
  s_timeline $mode
  s_quiz $mode
  s_settings_new $mode
  if open_event 1; then cap 11-event 2; [ $mode = full ] && { sw_up 500; cap 11b-event-2 1.5; top_of; }; back 2; fi
  leave
  log "NP $C: $(since_fatal np-$C); app process $(pidof_app); logcat app errors: $(since np-$C "E AndroidRuntime|E $PKG" | wc -l)"
}
part_new36() {
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || log "device locale not changed (labels may be English)"; }
  a am force-stop $PKG
  anims 1
  warmup
  new_pass bg-light full
  if [ "$SDK" -ge 29 ]; then a cmd uimode night yes; sleep 3; new_pass bg-dark brief; a cmd uimode night no; sleep 2
  else app_theme '=Тъмна|=Dark'; new_pass bg-dark brief; app_theme '=Както системата|=System default'; fi
  a cmd locale set-app-locales $PKG --locales en-US; sleep 3
  new_pass en-light brief
  a cmd locale set-app-locales $PKG --locales bg-BG
  sec "NA. Accessibility over all new screens"
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -40 | tee -a $SUM
  sec "NP-D. Diagnostics › Бързодействие"
  perf_card new36; back 1.5; back 1.5
  all_sheets
  diag_end
}
part_new36b() {
  helper_install
  anims 1
  warmup
  if set_text_size '=Много голям|=Extra large'; then
    a settings put system font_scale 1.3; sleep 3; new_pass font130-xlarge brief
    a cmd locale set-app-locales $PKG --locales en-US; sleep 3; new_pass font130-xlarge-en brief
    a cmd locale set-app-locales $PKG --locales bg-BG; a settings put system font_scale 1.0; sleep 2; set_text_size '=Стандартен|=Standard'
  fi
  rot 1; TOUR_ROT=1 new_pass landscape-light brief
  a cmd uimode night yes; sleep 3; TOUR_ROT=1 new_pass landscape-dark brief; a cmd uimode night no; rot 0; TOUR_ROT=""
  a wm density 540; sleep 4; new_pass w320dp brief
  a wm size 2560x1600; a wm density 320; sleep 4; new_pass tablet-1280x800dp brief
  rot 1; TOUR_ROT=1 new_pass tablet-landscape brief; rot 0; TOUR_ROT=""
  a wm size reset; a wm density reset
  sec "NA. Accessibility over all new screens"
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -40 | tee -a $SUM
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- monkey 2 × 6000
part_mon36() {
  anims 1
  MONKEY_N=6000 MONKEY_SEED=1357 part_monkey
  cp $OUT/monkey.txt $OUT/monkey-1357.txt 2>/dev/null
  log "MON36 round 1 (seed 1357): whole log so far: FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
  online; no_camera >/dev/null
  sec "K2. Monkey 6000 events, seed 4242"
  a am force-stop $PKG; sleep 2
  MONKEY_N=6000 MONKEY_SEED=4242 part_monkey
  cp $OUT/monkey.txt $OUT/monkey-4242.txt 2>/dev/null
  log "MON36 round 2 done: whole log FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt) ANR lines=$(grep -c 'ANR in' $OUT/logcat-live.txt)"
  diag_end
}

# ---------------------------------------------------------------- студен старт A/B (n=20 на издание)
s36_block() {  # $1 — new | base, $2 — блок
  local K=$1 b=$2 i
  sec "S $K-$b: fresh install, first launch + catalogue, 2 excluded + 5 cold starts (TotalTime, Fully drawn)"
  inst $(apk_of $K) $K-$b $(ref_of $K) || return
  a input keyevent KEYCODE_HOME; sleep 2
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$K-$b catalogue after ${WAITED} ms" || log "$K-$b: NO catalogue results"
  tab news; sleep 4; tab home; sleep 4
  log "$K-$b dexopt: $(dexopt_state)"
  for i in 1 2; do cold_drawn excl-$K >/dev/null; done
  for i in 1 2 3 4 5; do cold_drawn $K | tee -a $OUT/drawn-$K-$b.txt >/dev/null; done
  log "$K-$b: $(python3 $H_/parse.py starts $OUT/drawn-$K-$b.txt x | grep -oE 'n=.*max=[0-9]+ms')"
}
part_start36() {
  local b=0 K order K2
  anims 1
  case $PART in start36a) order="new base base new new base base new";; *) order="base new new base base new new base";; esac
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

# ---------------------------------------------------------------- покой и плавност на новите екрани, памет след обиколка
part_perf36() {
  local i c JCY
  anims 1
  warmup
  sec "P1. CPU and frames at rest on the new screens (30 s after opening, then a 15 s window)"
  a am force-stop $PKG; sleep 3; launch >/dev/null; sleep 2; dismiss; ready; tab home; idle_at home36
  ready; tab more; idle_at more36
  more_tile "$L_EMB" && { idle_at embroidery36; leave; }
  more_tile "$L_TIMELINE" && { idle_at timeline36; leave; }
  more_tile "$L_QUIZ" && { waitfor 'Въпрос [0-9]+ от|Question [0-9]+ of|Днес няма' 40; idle_at quiz36; leave; }
  more_tile "$L_NEWS_PAPER" && { sleep 4; idle_at newspaper-list36; dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*92/100)) 150); [ -n "$p" ] && { a input tap $p; waitfor 'Страница [0-9]+ от [0-9]+|Page [0-9]+ of' 90; sleep 3; idle_at newspaper-reader36; back 2; }; leave; }
  sec "P2. Jank while scrolling (animations ON, ${W}x${H}): Home, Още, Timeline, Newspaper list, News"
  restart; tab home; sleep 20; jscn home-scroll p36
  ready; tab more; sleep 2; jscn more p36
  more_tile "$L_TIMELINE" && { sleep 3; jscn timeline p36; leave; }
  more_tile "$L_NEWS_PAPER" && { sleep 4; jscn newspaper-list p36; leave; }
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; jscn news p36
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 2; jscn catalog p36
  sec "P3. Memory: 10 cycles over the new screens (Още → each tile → back), with the reader open once per cycle"
  mem new-before
  for c in 1 2 3 4 5 6 7 8 9 10; do
    more_tile "$L_EMB" && { dump; b=$(xy --bounds content-desc 'Платно за шевица|Embroidery canvas'); [ -n "$b" ] && { read l t r bt <<< "$b"; tapfrac $l $t $r $bt 40 40; tapfrac $l $t $r $bt 60 60; }; leave; }
    more_tile "$L_TIMELINE" && { sleep 2; sw_up 600; sw_up 600; leave; }
    more_tile "$L_QUIZ" && { sleep 3; leave; }
    more_tile "$L_NEWS_PAPER" && { sleep 3; dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/6)) $((H*92/100)) 150); [ -n "$p" ] && { a input tap $p; waitfor 'Страница [0-9]+ от [0-9]+|Page [0-9]+ of' 60; sleep 2; back 2; }; leave; }
    [ $c = 5 ] && mem new-c05
  done
  mem new-c10
  log "PERF36: FATAL=$(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt); pid $(pidof_app)"
  diag_end
}

# ---------------------------------------------------------------- devDebug: читателски дневник, „Върната“, годишната история
part_diary36() {
  local i p b l t r bt mode
  helper_install
  dev_install
  anims 1
  warmup
  sec "D0. Demo sign-in (DEMO-0001 / demo)"
  demo_signin_remember
  CFG=demo36; DENS=$(dens); dims; : > $OUT/a11y-demo36.txt
  mark d36
  sec "D1. Моето and „История“ (these screens are FLAG_SECURE: emulator screenshots)"
  ready; tab my; sleep 2; eshot d-my >/dev/null; sw_up 500; sleep 1; eshot d-my-2 >/dev/null; sw_up 500; sleep 1; eshot d-my-3 >/dev/null; top_of
  open_link_dev my/loans; sleep 1; eshot d-loans >/dev/null; back 1.5
  open_link_dev my/history 5; sleep 1; eshot d-history >/dev/null
  for i in 1 2 3; do sw_up 500; sleep 1; eshot d-history-$((i + 1)) >/dev/null; done; top_of
  dump; log "HISTORY: diary prompts '$(grep -c 'Хареса ли ти' $OUT/ui.xml)', rating nodes '$(grep -c 'Оценка на книгата' $OUT/ui.xml)', texts: $(texts 40 | cut -c1-500)"
  for i in 1 2 3 4; do dump; b=$(xy --bounds content-desc 'Оценка на книгата|Book rating'); [ -n "$b" ] && break; sw_up 400; sleep 1; done
  if [ -n "$b" ]; then
    read l t r bt <<< "$b"; log "DIARY rating node [$b]"
    a input tap $(( l + (r - l) * 70 / 100 )) $(( (t + bt) / 2 )); sleep 2; eshot d-rated >/dev/null; dump
    log "DIARY after rating: $(texts 24 | cut -c1-400)"
  else log "DIARY: no rating node found"; fi
  dump; if tap text '=Добави бележка|=Add a note' >/dev/null; then
    sleep 1.2; dump; p=$(xy --all class EditText | sed -n 1p); [ -n "$p" ] && { a input tap $p; sleep 1; a input text "very_good"; sleep 1; hide_ime; }
    eshot d-note >/dev/null; tap text '=Запази|=Save' >/dev/null; sleep 1.5; eshot d-note-saved >/dev/null
  fi
  dump; if tap text '=Препоръчай|=Recommend' >/dev/null; then
    sleep 2; eshot d-recommend-sheet >/dev/null; tap text '=Сподели като картинка|=Share as an image' >/dev/null
    for i in $(seq 1 15); do sleep 1; chooser_up && break; done
    log "DIARY recommend share: chooser $(chooser_up && echo OPEN || echo 'NOT open') after $i s"; eshot d-recommend-chooser >/dev/null; chooser_up && back 1.5
  else log "DIARY: no 'Препоръчай' after rating: $(texts 14 | cut -c1-260)"; fi
  back 1.5
  log "DIARY: $(since_fatal d36)"
  sec "D2. The diary survives a process death"
  go_home 2; a am kill $PKG; sleep 2; launch >/dev/null; sleep 5
  open_link_dev my/history 5; sleep 1; eshot d-history-after-kill >/dev/null; dump
  log "HISTORY after kill: $(grep -oE 'content-desc="[^"]*Оценка[^"]*"|text="[0-9] от 5"|text="very_good"' $OUT/ui.xml | head -4 | tr '\n' ' ')"
  back 1.5
  sec "D3. Year story, every slide one by one (taps on the right side; emulator screenshots, no drags)"
  for mode in light dark; do
    [ $mode = dark ] && { a cmd uimode night yes; sleep 3; }
    year_open_fresh 25 || { log "YEAR $mode: story did not open"; continue; }
    for i in 1 2 3 4 5 6 7 8 9 10; do
      sleep 1.5; dump; eshot y-$mode-$i >/dev/null
      log "YEAR $mode slide $i: $(texts 8 | cut -c1-260)"
      grep -q 'Затвори' $OUT/ui.xml || { log "YEAR $mode: story no longer open at $i"; break; }
      a input tap $((W*88/100)) $((H/2)); sleep 0.8
    done
    [ "$(ystate)" = open ] && back 2
    [ $mode = dark ] && { a cmd uimode night no; sleep 2; }
  done
  sec "D4. Signed-in card, fullscreen"
  card_screen; sleep 2; eshot zz-demo-card >/dev/null; dump; tap text "$L_CARDSHOW" >/dev/null; sleep 3; eshot zz-demo-card-full >/dev/null
  log "demo fullscreen card: $(fullscreen_ok && echo yes || echo NO)"; back 1.5; leave
  log "DIARY36 part: $(since_fatal d36)"
  python3 $W20 a11yagg $OUT/a11y-demo36.txt | head -30 | tee -a $SUM
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- Настройки: известия (седмичен преглед), палитри, цветове от тапета
part_set36() {
  local i p pal
  helper_install
  anims 1
  warmup
  CFG=set36; DENS=$(dens); dims; : > $OUT/a11y-set36.txt
  mark s36
  sec "S1. Settings › Избери известията: weekly digest, favourite sections, tomorrow reminder"
  open_settings && settings_item 'Избери известията|Choose notifications|Choose which' >/dev/null
  sleep 2; cap n-01 1
  for i in 1 2 3 4 5 6; do dump; [ -n "$(xy text 'Седмичен преглед|Weekly digest')" ] && break; sw_up 500; sleep 1; done
  cap n-02-digest 1
  log "DIGEST row: $(xy --state text 'Седмичен преглед|Weekly digest'); sections row $(has 'Любими раздели|Favourite sections' && echo present || echo ABSENT); tomorrow row $(has 'Напомняне .Утре|Tomorrow' && echo present || echo ABSENT)"
  tap text 'Седмичен преглед|Weekly digest' >/dev/null; sleep 1.2; shot digest/on-snack >/dev/null; dump
  log "DIGEST after the tap: $(xy --state text 'Седмичен преглед|Weekly digest'); snackbar: $(texts 40 | grep -oE 'text="[^"]*(неделя|Sunday)[^"]*"' | head -1)"
  sleep 1.5; cap n-03-digest-on 1
  for i in 1 2 3; do sw_up 400; sleep 0.8; done; cap n-04-sections 1
  tap text 'Любими раздели|Favourite sections' >/dev/null; sleep 1.5; cap n-05-sections-tap 1
  dump; tap text 'Напомняне „Утре“|“Tomorrow” reminder|Утре' >/dev/null; sleep 1.2; cap n-06-tomorrow 1
  back 1.5; back 1.5
  sec "S2. Digest notification: the clock jumps to Sunday 18:30, the periodic worker runs"
  a settings put global auto_time 0; a settings put global auto_time_zone 0
  log "device date before: $(a date | tr -d '\r'); tz $(a getprop persist.sys.timezone | tr -d '\r')"
  a date 111518302026.00 >/dev/null 2>&1; a am broadcast -a android.intent.action.TIME_SET >/dev/null 2>&1; sleep 3
  log "device date now: $(a date | tr -d '\r')"
  log "jobs: $(app_jobs)"; run_jobs; sleep 30
  notifs digest | cut -c1-500 | tee -a $SUM
  shade_expand 'Тази седмица|This week|седмица' digest || log "DIGEST: not in the shade"
  go_home 2; a cmd statusbar collapse
  log "DIGEST: $(since s36 'WeeklyDigest|digest' | head -5 | cut -c1-240 | tr '\n' '|'); $(since_fatal s36)"
  sec "S3. Palettes (Яворец / Левски / Шевица) and colours from the wallpaper: Home and Още"
  for pal in Левски Шевица Яворец; do
    open_settings; settings_find "=$pal" && { tap text "=$pal" >/dev/null; sleep 2; }
    leave; ready; tab home; cap pal-$pal-home 3; ready; tab more; cap pal-$pal-more 1.5
  done
  open_settings; settings_find '=Цветове от тапета|=Colours from the wallpaper|=Colors from the wallpaper' && { tap text 'Цветове от тапета|Colours from the wallpaper|Colors from the wallpaper' >/dev/null; sleep 2; }
  leave; ready; tab home; cap dyn-home 3; ready; tab more; cap dyn-more 1.5
  a cmd uimode night yes; sleep 3; ready; tab home; cap dyn-home-dark 3; a cmd uimode night no; sleep 2
  open_settings; settings_find '=Цветове от тапета|=Colours from the wallpaper|=Colors from the wallpaper' && { tap text 'Цветове от тапета|Colours from the wallpaper|Colors from the wallpaper' >/dev/null; sleep 2; }
  leave
  python3 $W20 a11yagg $OUT/a11y-set36.txt | head -20 | tee -a $SUM
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- API 36: ANR при monkey 777 — 1.2.36 срещу 1.2.35, с проследяванията
part_anr36() {
  local K n r f
  anims 1
  warmup
  for n in 1 2 3; do
    for K in new base; do
      sec "AN $K-$n: fresh install, monkey 2500 events seed 777 (the ANR of c36), /data/anr collected"
      inst $(apk_of $K) anr-$K-$n $(ref_of $K) || continue
      a input keyevent KEYCODE_HOME; sleep 2; launch >/dev/null; sleep 6; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null
      a "rm -rf /data/anr/* 2>/dev/null"; mark anr-$K-$n
      MONKEY_N=2500 MONKEY_SEED=777 part_monkey >/dev/null 2>&1
      r="$(grep -E 'Events injected' $OUT/monkey.txt | tr '\n' ' ') ANR=$(grep -c 'NOT RESPONDING\|ANR in' $OUT/monkey.txt) CRASH=$(grep -c CRASH $OUT/monkey.txt)"
      log "AN $K-$n: $r; logcat ANR lines since: $(since anr-$K-$n 'ANR in ' | wc -l); app FATAL $(since anr-$K-$n 'Process: org\.chyavorec\.app' | wc -l)"
      mkdir -p $OUT/anr/$K-$n; timeout 60 adb pull /data/anr $OUT/anr/$K-$n >/dev/null 2>&1
      cp $OUT/monkey.txt $OUT/monkey-$K-$n.txt
      log "  /data/anr: $(ls $OUT/anr/$K-$n/anr 2>/dev/null | tr '\n' ' ')"
      online
    done
  done
  for f in $(ls $OUT/anr/*/anr/* 2>/dev/null | head -4); do log "ANR TRACE $f: $(grep -A12 '"main"' $f | head -14 | tr '\n' '|' | cut -c1-900)"; done
  diag_end
}

# ---------------------------------------------------------------- уиджети, пряк път, икона
pin_dialog() {  # $1 — етикет: диалогът на лаунчера „Добави на началния екран“ → потвърждава
  local i p
  for i in 1 2 3 4 5 6; do sleep 1.5; dump; has 'Add to home screen|Добави на началния екран|Add automatically|автоматично' && break; done
  shot widget/$1-dialog >/dev/null
  log "PIN $1 dialog: fg $(fg); $(texts 12 | cut -c1-300)"
  p=$(xy text '=Add to home screen|=Добави на началния екран|=Add automatically|=Добави автоматично|=Автоматично добавяне')
  [ -z "$p" ] && p=$(xy text '=Add|=Добави|=ADD')
  if [ -n "$p" ]; then a input tap $p; sleep 3; else log "PIN $1: no confirm button"; fi
}
part_widget36() {
  local p i
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || true; }
  anims 1
  warmup
  card_open >/dev/null; leave
  sec "W1. Launcher icon (all apps) and the widget list"
  go_home 2; dims; shot launcher/home-empty >/dev/null
  a input swipe $((W/2)) $((H*85/100)) $((W/2)) $((H*25/100)) 300; sleep 2
  for i in 1 2 3 4 5 6; do dump; p=$(xy text 'Читалище Яворец|Читалище|Chitalishte'); [ -n "$p" ] && break; sw_up 400; sleep 1; done
  shot launcher/drawer >/dev/null; log "LAUNCHER drawer: label @ ${p:-not found}; $(texts 12 | cut -c1-240)"
  go_home 2
  widget_picker n36 && { shot widget/n36-picker >/dev/null; go_home 2; }
  sec "W2. Widgets added from Settings (the pin buttons)"
  mark w36
  open_settings; settings_find 'Книга на деня|Book of the day' && {
    shot widget/s-buttons >/dev/null
    log "WIDGET button 'Книга на деня' present; now tapping"
    tap text 'Книга на деня|Book of the day' --maxy $((H*95/100)) >/dev/null; pin_dialog book
  }
  go_home 3; dump; log "HOME after the Book-of-the-day pin: $(wstate)"; shot widget/book-on-home >/dev/null
  wcount book >/dev/null; log "widgets of ours bound: $(wids)"; grep -E "provider=.*org.chyavorec.app" $OUT/appwidget-book.txt | sort | uniq -c | head -4 | cut -c1-200
  open_settings; settings_find 'Добави уиджета|Add the' && {
    log "WIDGET button 'Читалище Яворец' tapped"
    tap text 'Добави уиджета|Add the' >/dev/null; pin_dialog main
  }
  go_home 3; dump; log "HOME after both pins: $(wstate)"; shot widget/both-on-home >/dev/null
  wcount both >/dev/null; log "widgets of ours bound: $(wids)"; grep -E "provider=.*org.chyavorec.app" $OUT/appwidget-both.txt | sort | uniq -c | head -4 | cut -c1-200
  a cmd uimode night yes; sleep 5; go_home 4; go_home 3; shot widget/both-dark >/dev/null; log "WIDGET dark: $(wstate)"; a cmd uimode night no; sleep 4; go_home 3
  sec "W3. Widget click-through; the Book-of-the-day widget after the catalogue is loaded"
  ready >/dev/null; tab catalog >/dev/null; waitfor "$RX_RESULTS" 100 >/dev/null; go_home 4; shot widget/after-catalog >/dev/null; log "WIDGET after the catalogue loaded: $(wstate)"
  K=n36; widget_clicks
  dump; p=$(xy text 'Книга на деня|Book of the day|Отвори каталога'); [ -n "$p" ] && { a input tap $p; sleep 4; dump; shot widget/book-click >/dev/null; log "Book widget click → fg $(fg): $(texts 8 | cut -c1-200)"; }
  go_home 2
  sec "W4. Pinned shortcut „Шевица“ from Още › Добави на началния екран"
  ready >/dev/null; tab more >/dev/null; sleep 2
  for i in 1 2 3 4 5; do dump; p=$(xy text 'Добави на началния екран|Add to home screen'); [ -n "$p" ] && break; sw_up 400; sleep 1; done
  if [ -n "$p" ]; then a input tap $p; sleep 2; dump; shot widget/pin-sheet >/dev/null; log "PIN sheet: $(texts 16 | cut -c1-300)"
    tap text 'Шевица|Embroidery' >/dev/null || tap content-desc 'Шевица|Embroidery' >/dev/null; pin_dialog shortcut
    go_home 3; dump; p=$(xy text '=Шевица|=Embroidery'); log "shortcut icon on home: ${p:-not found}"; shot widget/shortcut-home >/dev/null
    [ -n "$p" ] && { a input tap $p; sleep 4; dump; shot widget/shortcut-opened >/dev/null; log "pinned shortcut opened: $(has "$L_EMB" && echo 'embroidery screen' || echo 'OTHER'); $(texts 6 | cut -c1-160)"; }
  else log "PIN: 'Добави на началния екран' not found in Още (launcher without pin support?)"; fi
  log "WIDGET36: $(since_fatal w36)"
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- псевдоними и маршрутизация
part_w22() {
  case "$W22_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    upd36|upd28) part_upd36;;
    onb36) part_onb36;;
    new36|new28|new26) part_new36;;
    new36b) part_new36b;;
    mon36) part_mon36;;
    start36a|start36b) part_start36;;
    gfx36|gfxb36) PART=gfx35; part_gfx35;;
    mem36) PART=mem; part_mem;;
    memb36) PART=mem-base; part_mem;;
    memL36) PART=mem; MEM_CYCLES=60 part_mem;;
    membL36) PART=mem-base; MEM_CYCLES=60 part_mem;;
    set36) part_set36;;
    anr36) part_anr36;;
    perf36) part_perf36;;
    diary36) part_diary36;;
    widget36) part_widget36;;
  esac
  return 0
}
