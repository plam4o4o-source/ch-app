# Пълна проверка на издаденото v1.2.35 (ВРЕМЕННО, debug/a6-full; резултати в emulator-results/v135full).
# Издаденото v1.2.35 (aef8910, versionCode 1507364) и v1.2.34 (3812d8e, 1504705) — и двете prodRelease x86_64
# с временен ключ; devDebug от aef8910 за демо входа. Зарежда се от run.sh след w21.sh и ползва функциите на
# по-старите части. От тях направо се пускат и: tour-a/b/c, robust, offline, mem, monkey, a11yt.
#   a6new  — новото от 1.2.33 насам: картата (на ръка), на цял екран, фолиото с наклон; живата шевица (тече,
#            после спира); „Изненадай ме“ (зарче, разклащане, „назад“); споделяне като картинка (статия, книга,
#            събитие); „Настройки › Обновления“; „Само налични“; розетката при дърпане
#   year35 — devDebug: „Моето“ с вход, годината (време до първия слайд, плъзгане надолу, всеки слайд)
#   idle33 — „Начало“ в покой след студен старт (20–35, 45–60, 90–105 s), 1.2.35 и 1.2.34; CPU на всеки екран
#   hi28   — същото на API 28, 1.2.35 и 1.2.34 (с повторение и със снимки: какво се рисува)
#   gfx35  — gfxinfo по сценарии (2 кръга), най-бавните кадри, старт, памет
#   upd35  — 1.2.34 (с уиджета, картата, „Искам да прочета“, размера на текста, тъмна тема) → 1.2.35 през
#            диалога на приложението и системния инсталатор; какво оцелява; „Настройки › Обновления“
#   c26, c28, c36 — съвместимост: обиколка на всички екрани, завъртане и смърт на процеса, monkey 2000
#            (c36: и предвиждащият жест „назад“ върху „Изненадай ме“)
A6_PARTS=" a6new year35 yup35 land35 idle33 hi28 gfx35 upd35 c26 c28 c36 "
W20_PARTS="$W20_PARTS $A6_PARTS"
A6=$H_/a6.py

sheets_of() {  # $1 — папка под shots/ → контактни листове в sheets/$1
  local d=$OUT/shots/$1
  [ -d $d ] || return 0
  python3 $A6 sheets $OUT/sheets/$1 10 $(ls $d/*.png 2>/dev/null | sort) | tee -a $SUM
}
all_sheets() {  # листове за всяка папка със снимки (без записите)
  local d
  for d in $(cd $OUT/shots 2>/dev/null && find . -type d | sed 's|^\./||' | grep -v '^\.$' | sort); do
    ls $OUT/shots/$d/*.png >/dev/null 2>&1 && python3 $A6 sheets $OUT/sheets/$d 10 $(ls $OUT/shots/$d/*.png | sort) >/dev/null
  done
  log "contact sheets: $(find $OUT/sheets -name '*.jpg' 2>/dev/null | wc -l) in sheets/"
}
uptime_s() { a "cat /proc/uptime" | tr -d '\r' | cut -d' ' -f1; }
jscn() {  # $1 — сценарий, $2 — етикет: jank_scn + най-бавните кадри (framestats, хистограмата)
  jank_scn $1 $2
  a dumpsys gfxinfo $PKG framestats > $OUT/fs-$2-$1.txt 2>/dev/null
  log "  $(python3 $W21 fs $OUT/fs-$2-$1.txt $2-$1 | cut -c1-300)"
  log "  $2-$1 $(python3 $A6 hist $OUT/gfx-$2-$1.txt)"
  rm -f $OUT/fs-$2-$1.txt $OUT/gfx-$2-$1-full.txt
}

# ---------------------------------------------------------------- „НАЧАЛО“ В ПОКОЙ
# Студен старт, после без докосване: прозорци по 15 s от 20, 45 и 90 s след `am start`. Преди и между тях
# няма uiautomator dump (включва достъпността) — само gfxinfo и /proc; режим „dump“ го прави нарочно,
# режим „shots“ снима през ~1 s в прозореца, за да се види какво се мени.
hi_run() {  # $1 — етикет, [$2 — clean | shots | dump]
  local L=$1 mode=${2:-clean} w f0 f1 i u0 u1
  mkdir -p $OUT/hi $OUT/shots/hi
  a am force-stop $PKG; sleep 3; go_home 2
  launch > $OUT/hi/$L-start.txt; t0=$(now_ms)
  log "HI $L ($mode): cold start $(grep -oE 'TotalTime: [0-9]+' $OUT/hi/$L-start.txt)"
  for w in 20 45 90; do
    while [ $(( $(now_ms) - t0 )) -lt $((w * 1000)) ]; do sleep 0.2; done
    [ $mode = dump ] && { dump; sleep 0.5; }
    u0=$(uptime_s)
    if [ $mode = shots ]; then
      f0=$(gfx_total); i=0
      while [ $(( $(now_ms) - t0 )) -lt $(( (w + 15) * 1000 )) ]; do i=$((i + 1)); shot hi/$L-w$w-$(printf %02d $i) >/dev/null; sleep 0.4; done
      f1=$(gfx_total); log "IDLE hi-$L-${w}-$((w + 15))s (15 s, no input, $i screenshots inside): frames +$((f1 - f0))"
      log "  $(python3 $A6 changes $OUT/shots/hi/$L-w$w-changes.png $(ls $OUT/shots/hi/$L-w$w-[0-9][0-9].png | sort))"
      ls $OUT/shots/hi/$L-w$w-[0-9][0-9].png | sort | sed -n '2,$p' | xargs rm -f
    else
      cpu_win hi-$L-${w}-$((w + 15))s 15
    fi
    u1=$(uptime_s)
    a dumpsys gfxinfo $PKG framestats > $OUT/hi/$L-w$w-fs.txt 2>/dev/null
    log "  $L ${w}-$((w + 15))s $(python3 $A6 fstl $OUT/hi/$L-w$w-fs.txt $u0 $u1)"
  done
  shot hi/$L-end >/dev/null
  dump; cp $OUT/ui.xml $OUT/hi/$L-end.xml 2>/dev/null; log "HI $L screen at the end: $(texts 12 | cut -c1-300)"
}
hi_block() {  # $1 — new | base, $2 — блок, $3… — режимите
  local K=$1 b=$2 m n=0; shift 2
  sec "HI $K-$b: fresh install, warm-up, then Home at rest after a cold start (15 s windows at 20, 45 and 90 s): $*"
  inst $(apk_of $K) $K-$b $(ref_of $K) || return
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab news; sleep 4; tab home; sleep 6
  for m in "$@"; do n=$((n + 1)); hi_run $K-$b-$n-$m $m; done
}
hi_totals() {
  sec "HI totals: frames in each 15 s window (label: build-block-run-mode)"
  grep -E '^IDLE hi-' $SUM | sed -E 's/^IDLE hi-([^ ]+) .*frames \+([0-9]+).*/\1 \2/' | awk '{split($1, p, "-"); k=p[1] " " p[4] " " p[length(p)-1] "-" p[length(p)]; a[k]=a[k] " " $2} END {for (k in a) print "HI-TOTAL " k ":" a[k]}' | sort | tee -a $SUM
}
part_hi28() {
  helper_install; [ "$SDK" -lt 33 ] && { dev_locale bg-BG || true; }
  a am force-stop $PKG
  anims 1
  hi_block new 1 clean clean shots dump
  hi_block base 1 clean clean shots dump
  hi_block new 2 clean shots
  hi_block base 2 clean shots
  hi_totals
  all_sheets
  diag_end
}
part_idle33() {
  anims 1
  hi_block new 1 clean clean shots dump
  hi_block base 1 clean clean shots
  hi_block new 2 clean
  hi_totals
  sec "I0. CPU and frames at rest on every screen (1.2.35)"
  part_idle
  all_sheets
}

# ---------------------------------------------------------------- НОВОТО ОТ 1.2.33 НАСАМ
surprise_back() {  # „назад“ (клавишът) затваря „Изненадай ме“ и връща каталога
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 1
  tap content-desc "$L_SURPRISE" >/dev/null || { log "SURPRISE back: no dice button"; return; }
  sleep 4; dump; shot new/surprise-open
  log "SURPRISE open: $(grep -oE 'text="(Предложение|Към книгата|Още една|Избирам книга…)"' $OUT/ui.xml | tr '\n' ' ')"
  tap text '=Още една' >/dev/null && { sleep 4; dump; shot new/surprise-another; log "SURPRISE 'Още една': $(texts 6 | cut -c1-200)"; }
  back 2; dump
  log "SURPRISE after the back key: $(has 'text="(Към книгата|Още една)"' && echo 'STILL OPEN' || echo closed); catalogue $(has "$RX_RESULTS" && echo shown || echo 'NOT shown'); tab bar $(has 'text="(Начало|Home)"' && echo visible || echo HIDDEN)"
}
updates_card() {  # $1 — етикет: „Настройки › Обновления“
  if open_settings && settings_find 'Провери за нова версия|Имаш последната версия|е готова|Налична е версия'; then
    sleep 1.5; dump; shot new/settings-updates-$1
    log "UPDATES card $1: $(grep -oE 'text="[^"]*(Обновления|версия|Автоматично обновяване|е готова|Инсталирай)[^"]*"' $OUT/ui.xml | tr '\n' ' ' | cut -c1-500)"
    if tap text '=Провери за нова версия' >/dev/null; then
      sleep 0.7; shot new/settings-updates-$1-checking; sleep 6; dump; shot new/settings-updates-$1-checked
      log "UPDATES after 'Провери за нова версия': $(grep -oE 'text="[^"]*(версия|Проверка|не успя|готова)[^"]*"' $OUT/ui.xml | tr '\n' ' ' | cut -c1-400)"
    fi
    back 1.5; back 1.5
  else log "UPDATES card $1: not found in Settings"; fi
}
part_a6new() {
  local i
  helper_install
  anims 1
  warmup
  sensors_info
  sec "N1. Card added by hand (Моето › Добави читателска карта)"
  card_add_my || log "card NOT added by hand"
  leave
  sec "N2. Living шевица on Home: animates after a cold start, stops later (0 frames at rest)"
  ribbon35 new-a
  f_ribbon
  sec "N3. Card screen: frames/CPU at rest, tilt (foil)"
  restart; ready; tab my; sleep 2; tap text "$L_MYCARD" >/dev/null; sleep 1
  card_idle card
  card_tilt card
  sec "N4. Fullscreen card: frames/CPU at rest, tilt, flip"
  dump; tap text "$L_CARDSHOW" >/dev/null; sleep 1
  card_idle card-full
  log "fullscreen card reached: $(fullscreen_ok && echo yes || echo NO)"
  card_tilt card-full
  dump; tap content-desc 'Баркод на карта|Card barcode' >/dev/null; sleep 2; eshot zz-full-flipped >/dev/null
  cpu_win card-full-flip-2-17s 15
  back 1.5; dump; log "after back from the fullscreen card: $(texts 5 | cut -c1-160)"
  rot 1; sleep 2; dump; tap text "$L_CARDSHOW" >/dev/null; sleep 3; eshot zz-full-landscape >/dev/null; log "fullscreen card opened in landscape: $(fullscreen_ok && echo yes || echo NO)"; back 1.5; rot 0
  leave
  sec "N5. „Изненадай ме“: dice, shake, back"
  f_surprise
  surprise_back
  sec "N6. Share as image: an article, a book, an event (the files are in share/)"
  if open_article; then share_try art && share_pick_helper art; leave; else log "SHARE art: article not opened"; fi
  link "$BOOK_URL" >/dev/null; sleep 4; share_try book && share_pick_helper book; leave
  link "$SITE/elektronen-katalog?inv=1" >/dev/null; sleep 4; share_try book2 && share_pick_helper book2; leave
  EV_N="" share_event_scan new 4
  a cmd uimode night yes; sleep 3
  if open_article; then share_try art-dark && share_pick_helper art-dark; leave; fi
  link "$BOOK_URL" >/dev/null; sleep 4; share_try book-dark && share_pick_helper book-dark; leave
  a cmd uimode night no; sleep 2
  sec "N7. Settings › Обновления"
  updates_card light
  sec "N8. Catalogue: „Само налични“, sort, shelf"
  f_catalog_ops
  ready; tab catalog; sleep 2; dump; shot new/catalog-after-ops; log "catalogue after the filter (state kept?): $(texts 10 | cut -c1-260)"
  sec "N9. Pull-to-refresh rosette"
  f_refresh_all
  a cmd uimode night yes; sleep 2; link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; pull_refresh news-dark 45; a cmd uimode night no; sleep 2
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- ГОДИНАТА (devDebug, демо вход)
part_year35() {
  local i p
  helper_install
  dev_install
  anims 1
  warmup
  sec "Y0. Demo sign-in (DEMO-0001 / demo, „Запомни ме“)"
  demo_signin_remember
  CFG=demo; DENS=$(dens); dims; : > $OUT/a11y-demo.txt
  sec "Y1. Моето (signed in) and its screens"
  ready; tab my; cap 01-my 2; sw_up 500; cap 01b-my-2 1.5; sw_up 500; cap 01c-my-3 1.5; top_of
  open_link_dev my/loans; cap 02-loans 1; back 1.5
  open_link_dev my/history; cap 03-history 2; sw_up 500; cap 03b-history-2 1.5; back 1.5
  open_link_dev messages; cap 04-messages 2
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/5)) $((H*80/100)) 120); [ -n "$p" ] && { a input tap $p; cap 04b-message 2.5; back 1.5; }
  back 1.5
  sec "Y2. Year story: tap → first slide (raw screencaps), fresh process ×2 and reopen ×2"
  for i in 1 2; do
    a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 5; dismiss; ready; tab my; sleep 3; top_of
    year_tap_raw fresh-$i; sleep 2; [ "$(ystate)" = open ] && back 2
    ready; tab my; sleep 2; top_of; year_tap_raw reopen-$i; sleep 2; [ "$(ystate)" = open ] && back 2
  done
  sec "Y3. Every slide (portrait, standard text): screenshot, scroll inside"
  year_slides_walk port
  sec "Y4. Swipe-down dismiss: each swipe from the first slide of a freshly opened story, twice"
  year_swipes2 new 2
  sec "Y5. Back key and the close button"
  year_open_fresh 25 && { back 2; log "YEAR back key: $(ystate)"; }
  year_open_fresh 25 && { dump; tap content-desc '=Затвори' >/dev/null; sleep 2; log "YEAR close button: $(ystate)"; }
  sec "Y6. Dark theme: the first slides"
  a cmd uimode night yes; sleep 3
  year_slides_walk dark
  a cmd uimode night no; sleep 2
  sec "Y7. Share a slide as an image"
  a "rm -rf /data/data/$PKG/cache/share"
  if year_open_fresh 25; then dump; tap content-desc '=Сподели' >/dev/null || tap text '=Сподели' >/dev/null; sleep 7
    log "YEAR share: fg $(fg); cache/share: $(a "ls -l /data/data/$PKG/cache/share/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';')"
    SH_FILE=$(share_newest); [ -n "$SH_FILE" ] && { mkdir -p $OUT/share; timeout 20 adb pull "/data/data/$PKG/cache/share/$SH_FILE" $OUT/share/year-$SH_FILE >/dev/null 2>&1; }
    chooser_up && back 1.5; [ "$(ystate)" = open ] && back 2
  fi
  sec "Y8. Signed-in card: fullscreen (demo)"
  card_screen; sleep 2; eshot zz-demo-card >/dev/null; dump; tap text "$L_CARDSHOW" >/dev/null; sleep 3; eshot zz-demo-card-full >/dev/null
  log "demo fullscreen card: $(fullscreen_ok && echo yes || echo NO)"; back 1.5; leave
  python3 $W20 a11yagg $OUT/a11y-demo.txt | head -30 | tee -a $SUM
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- GFXINFO ПО СЦЕНАРИИ, СТАРТ, ПАМЕТ
gfx_round35() {  # $1 — кръг
  local L=r$1 i JCY t p
  sec "G$1. gfxinfo per scenario, round $1 (animations ON, ${W}x${H}); slowest frames from framestats and the histogram"
  restart; tab home; sleep 30
  jscn home-scroll $L
  top_of; sleep 1; dump; JCY=$(python3 $W15 big $OUT/ui.xml $PKG 2>/dev/null | awk '{print int(($2+$4)/2)}'); [ -n "$JCY" ] && [ "$JCY" -gt 0 ] && jscn carousel $L
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; jscn news $L
  open_article && { sleep 2; jscn article $L; }
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar; jscn calendar $L
  dump; has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък' >/dev/null; sleep 2; jscn events-list $L; }
  leave; ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 2; jscn catalog $L
  tap content-desc "$L_SHELF" >/dev/null && { sleep 3; jscn shelf $L; tap content-desc "$L_LIST" >/dev/null; sleep 1; }
  ready; tab my; sleep 3; jscn my $L
  ready; tab more; sleep 3; jscn more $L
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30; then jscn gallery $L
    if open_album 1 && wait_cells 15; then jscn album-grid $L
      if open_photo 1; then sleep 2; gfx_reset; for i in $(seq 1 15); do sw_left 150; sleep 0.8; done; gfx_save $L-viewer; log "  $L-viewer $(python3 $A6 hist $OUT/gfx-$L-viewer.txt)"; back 1.5; fi; back 1.5; fi; back 1.5; fi
  if open_settings; then jscn settings $L; back 1.5; fi
  ready; dims; dump; declare -A TXY
  for t in home catalog my news more; do TXY[$t]=$(xy --tab "$(tablabel $t)" "$( [ $t = more ] && echo '=Home|=Начало' || echo '=More|=Още')"); done
  gfx_reset; for i in 1 2 3; do for t in catalog my news more home; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 1.2; done; done; gfx_save $L-tab-switch; log "  $L-tab-switch $(python3 $A6 hist $OUT/gfx-$L-tab-switch.txt)"
  link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" 60; sleep 2; dump
  p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*85/100)) 150)
  [ -n "$p" ] && { gfx_reset; for i in 1 2 3 4 5; do a input tap $p; sleep 2.5; back 2; done; gfx_save $L-book-open-x5; log "  $L-book-open-x5 $(python3 $A6 hist $OUT/gfx-$L-book-open-x5.txt)"; }
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 1
  gfx_reset; for i in 1 2 3; do tap content-desc "$L_SURPRISE" >/dev/null; sleep 4; tap content-desc "$L_CLOSE" >/dev/null || back 1.5; sleep 1.5; done; gfx_save $L-surprise-x3; log "  $L-surprise-x3 $(python3 $A6 hist $OUT/gfx-$L-surprise-x3.txt)"
  leave; card_screen; sleep 1
  dump; if tap text "$L_CARDSHOW" >/dev/null; then gfx_reset; sleep 12; gfx_save $L-cardfull-open-12s; a dumpsys gfxinfo $PKG framestats > $OUT/fs-cardfull.txt; log "  $(python3 $W21 fs $OUT/fs-cardfull.txt $L-cardfull | cut -c1-300)"; rm -f $OUT/fs-cardfull.txt; back 1.5; fi
  leave
}
part_gfx35() {
  local i
  anims 1
  sec "P0. Package: $(cat apk-ref.txt); dexopt: $(dexopt_state); baseline profile in the APK: $(unzip -l app.apk 2>/dev/null | grep -oE 'assets/dexopt/baseline\.prof[m]?' | tr '\n' ' ')"
  warmup
  card_open >/dev/null; leave
  sec "P1. 10 cold (with 'Fully drawn') + 10 warm starts, as installed"
  for i in 1 2; do cold_drawn excluded >/dev/null; done
  : > $OUT/start-new-warm.txt
  for i in $(seq 1 10); do cold_drawn new >/dev/null; done
  for i in $(seq 1 10); do a input keyevent KEYCODE_HOME; sleep 3; launch >> $OUT/start-new-warm.txt; sleep 2; done
  python3 $H_/parse.py starts $OUT/drawn-new.txt "cold new" | tee -a $SUM; drawn_sum new
  python3 $H_/parse.py starts $OUT/start-new-warm.txt "warm new" | tee -a $SUM
  gfx_round35 1
  mem after-round-1
  gfx_round35 2
  mem after-round-2
  sec "P4. Totals"
  grep -E '^GFX ' $SUM | sort -k2 | tee $OUT/gfx-totals.txt >/dev/null
  python3 $WF gfxagg $SUM 2>/dev/null | head -40 | tee -a $SUM
  sec "P5. Diagnostics › „Бързодействие“ (the app's own numbers, JankStats)"
  perf_card filled; back 1.5; back 1.5
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- ОБНОВЯВАНЕ 1.2.34 → 1.2.35
upd_state() {  # $1 — етикет: какво е запазено (размер на текста, тема, карта, „Искам да прочета“)
  local L=$1
  ready; tab home; sleep 3; shot update/$L-home
  open_settings && { settings_find '=Размер на текста|=Text size' && { dump; log "STATE $L settings: $(grep -oE 'text="(Малък|Стандартен|Голям|Много голям|Светла|Тъмна|Системна|Като системата|По системата)[^"]*"' $OUT/ui.xml | tr '\n' ' ')"; shot update/$L-settings; }; back 1.5; }
  log "STATE $L night mode of the app window: $(a dumpsys window windows 2>/dev/null | tr -d '\r' | grep -A40 "$PKG/" | grep -m1 -oE 'mLastReportedConfiguration=\{[^}]*' | grep -oE '(not)?night' | head -1); app locale $(a cmd locale get-app-locales $PKG 2>/dev/null | grep -oE '\[[^]]*\]')"
  card_screen; dump; log "STATE $L card: $(card_shown && echo "shown ($(grep -oE 'TEST-?12345' $OUT/ui.xml | head -1))" || echo "NOT shown: $(texts 6 | cut -c1-160)")"; eshot zz-$L-card >/dev/null; leave
  open_want_list && { dump; log "STATE $L want-to-read: $(want_book_shown && echo 'book listed' || echo 'book MISSING'); $(texts 8 | cut -c1-200)"; shot update/$L-want; back 1.5; }
  leave
}
part_upd35() {
  local p ids0
  helper_install
  anims 1
  sec "U0. v1.2.34 (3812d8e, versionCode 1504705, staged update.json → the built 1.2.35, 1507364)"
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  log "staged manifest: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["size"])' $OUT/update.json 2>&1)"
  inst app-base-upd.apk base-upd apk-base-upd-ref.txt || return
  a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  sec "U1. Using 1.2.34: „По-късно“ on the update dialog, text size „Голям“, dark theme, a card, a book in „Искам да прочета“, the widget"
  launch >/dev/null
  if upd_wait_dialog first; then shot update/00-dialog-134; tap text '=По-късно|=Later' >/dev/null; sleep 2; else log "no update dialog on the first launch of 1.2.34"; fi
  textsize_close; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab home; sleep 3
  want_add "$BOOK_URL"
  card_add $CARD_NO >/dev/null 2>&1; leave
  set_text_size '=Голям|=Large'; back 1.5; back 1.5
  open_settings && settings_item '=Тема|=Theme' >/dev/null && { sleep 1.5; dump; shot update/01-theme-sheet; log "theme options: $(texts 10 | cut -c1-200)"; tap text '=Тъмна|=Dark' --last >/dev/null; sleep 2; back 1.5; }
  leave
  K=b134; a am force-stop $PKG; widget_add b134 && { go_home 3; log "WIDGET v1.2.34: $(wstate)"; shot update/02-widget-134; }
  wcount before >/dev/null; ids0=$(wids)
  launch >/dev/null; sleep 4; dismiss
  upd_state before
  sec "U2. Settings › Обновления on 1.2.34: the ready card → „Инсталирай“ → the system installer"
  log "downloaded files: $(upd_files)"
  a am force-stop $PKG; launch >/dev/null; sleep 6; dump; log "relaunch after 'По-късно': dialog $(has 'text="(Нова версия|New version)' && echo SHOWN || echo 'not shown')"
  dismiss
  if open_settings && settings_find 'е готова|Провери за нова версия|Налична е версия'; then sleep 2; dump; shot update/10-settings-card
    log "SETTINGS › Обновления: $(grep -oE 'text="[^"]*(е готова|Изтеглена и проверена|Инсталирай|Налична е версия|Провери за нова версия|Изтегли и инсталирай|Имаш последната)[^"]*"' $OUT/ui.xml | tr '\n' ' ')"
    p=""; for i in 1 2 3 4 5; do dump; p=$(xy text '=Инсталирай|=Install'); [ -z "$p" ] && p=$(xy text '=Изтегли и инсталирай|=Download and install'); [ -n "$p" ] && break; sw_up 300; sleep 1; done
    shot update/11-settings-card-scrolled
    if [ -n "$p" ]; then a input tap $p; sleep 3; dump; shot update/12-after-install-tap; log "after the tap on Инсталирай: $(texts 8 | cut -c1-240)"
      if installer_confirm 1507364 u; then log "UPDATE via the settings card: installed $(ver)"; else log "UPDATE via the settings card: NOT installed: $(ver); $(texts 8 | cut -c1-200)"; fi
    else log "no install button on the settings card"; fi
  else log "Settings › Обновления: card not found"; fi
  if [ "$(a dumpsys package $PKG | grep -m1 -oE 'versionCode=[0-9]+')" != "versionCode=1507364" ]; then
    sec "U2b. Fallback: adb install -r app.apk over 1.2.34"
    go_home 2; timeout 180 adb install -r app.apk 2>&1 | tail -1; log "after adb install -r: $(ver)"
  fi
  sec "U3. After the update: the widget before the app is opened, „Какво ново“, then what survived"
  sleep 3; go_home 3; log "WIDGET after the update (app not opened): $(wstate)"; shot update/20-widget-after
  wcount after >/dev/null; log "widget ids before/after: [$ids0] / [$(wids)]"
  launch >/dev/null; sleep 3
  whatsnew_check 30-whats-new && { a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*40/100)) 400; sleep 1.5; shot update/31-whats-new-scrolled; tap text '=Добре|=OK|=Разбрах|=Got it' >/dev/null || back 2; }
  sleep 2; a am force-stop $PKG; launch >/dev/null; sleep 5; dump; log "relaunch: what's new again? $(has "$RX_WHATSNEW" && echo 'SHOWN AGAIN' || echo no); update dialog $(has 'text="(Нова версия|New version)' && echo SHOWN || echo 'not shown')"
  dismiss
  upd_state after
  go_home 3; log "WIDGET after opening the app: $(wstate)"; shot update/40-widget-after-app
  K=a135; widget_clicks
  launch >/dev/null; sleep 3
  if open_settings && settings_find 'Имаш последната версия|Провери за нова версия'; then dump; shot update/50-settings-after; log "SETTINGS › Обновления after the update: $(grep -oE 'text="[^"]*(версия|готова)[^"]*"' $OUT/ui.xml | tr '\n' ' ' | cut -c1-300)"; back 1.5; fi
  log "leftover update files: '$(upd_files)'"
  a cmd uimode night no
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- СЪВМЕСТИМОСТ: API 26 / 28 / 36
pback() {  # предвиждащият жест „назад“ (навигация с жестове): снимка по средата, после отпускане
  local L=$1 y=$((H/2))
  a "input motionevent DOWN 3 $y; input motionevent MOVE 80 $y; input motionevent MOVE 220 $y; input motionevent MOVE $((W*40/100)) $y" >/dev/null 2>&1
  sleep 0.6; shot pback/$L-mid >/dev/null
  if [ "${2:-commit}" = cancel ]; then
    a "input motionevent MOVE 120 $y; input motionevent MOVE 20 $y; input motionevent CANCEL 20 $y" >/dev/null 2>&1
  else
    a "input motionevent MOVE $((W*60/100)) $y; input motionevent UP $((W*60/100)) $y" >/dev/null 2>&1
  fi
  sleep 2.5; shot pback/$L-after >/dev/null
}
app_theme() {  # $1 — етикетът на темата в „Настройки › Тема“ (под API 29 няма системна тъмна тема)
  open_settings && settings_item '=Тема|=Theme' >/dev/null && { sleep 1.5; tap text "$1" --last >/dev/null; sleep 2; dump; log "app theme → $1: $(texts 4 | cut -c1-120)"; back 1.5; }
  leave
}
part_compat() {
  local i
  helper_install
  [ "$SDK" -lt 33 ] && { dev_locale bg-BG || log "device locale not changed (labels may be English)"; }
  a am force-stop $PKG
  anims 1
  warmup
  sec "C1. Tour of all screens on API $SDK"
  tour api$SDK-light
  if [ "$SDK" -ge 29 ]; then a cmd uimode night yes; sleep 3; tour api$SDK-dark; a cmd uimode night no; sleep 2
  else app_theme '=Тъмна|=Dark'; tour api$SDK-dark; app_theme '=Както системата|=System default'; fi
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -30 | tee -a $SUM
  sec "C2. Home ribbon and the card on API $SDK"
  a am force-stop $PKG; sleep 3; launch >/dev/null; t0=$(now_ms)
  shot_t compat/home-01s 1000; shot_t compat/home-05s 5000; shot_t compat/home-30s 30000
  if [ "$SDK" -lt 33 ]; then card_add28 || log "CARD: not added"; else card_open >/dev/null || log "CARD: not shown"; fi
  card_screen; sleep 3; eshot zz-card-$SDK >/dev/null
  cpu_win card$SDK-3-18s 15
  dump; tap text "$L_CARDSHOW" >/dev/null || { [ -n "${SHOW_XY:-}" ] && a input tap $SHOW_XY; }
  sleep 3; eshot zz-card-full-$SDK >/dev/null; log "CARD fullscreen on API $SDK: $(fullscreen_ok && echo yes || echo NO); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
  sleep 27; cpu_win cardfull$SDK-30-45s 15
  leave
  sec "C3. Surprise ×2, share a book as an image on API $SDK"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1
  for i in 1 2; do tap content-desc "$L_SURPRISE" >/dev/null; sleep 0.8; shot compat/surprise-$i-mid; sleep 3.5; dump; shot compat/surprise-$i
    log "SURPRISE $i: $(grep -oE 'text="(Предложение|Към книгата|Още една)"' $OUT/ui.xml | tr '\n' ' '); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"; surprise_close; done
  link "$BOOK_URL" >/dev/null; sleep 4; share_try book$SDK && share_pick_helper book$SDK; leave
  if [ "$SDK" -ge 34 ]; then
    sec "C4. Predictive back (gesture navigation) on API $SDK: „Изненадай ме“, a book, an article"
    nav_gestures
    log "enable_back_animation: $(a settings get global enable_back_animation | tr -d '\r')"
    ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 1; dims
    tap content-desc "$L_SURPRISE" >/dev/null; sleep 4.5; dump; shot pback/surprise-0
    log "PBACK surprise open: $(grep -oE 'text="(Предложение|Към книгата|Още една)"' $OUT/ui.xml | tr '\n' ' ')"
    pback surprise-cancel cancel; dump; log "PBACK surprise after a cancelled gesture: $(has 'text="(Към книгата|Още една)"' && echo 'still open (expected)' || echo 'CLOSED')"
    has 'text="(Към книгата|Още една)"' || { tap content-desc "$L_SURPRISE" >/dev/null; sleep 4.5; }
    pback surprise-commit; dump; log "PBACK surprise after the committed gesture: $(has 'text="(Към книгата|Още една)"' && echo 'STILL OPEN' || echo closed); catalogue $(has "$RX_RESULTS" && echo shown || echo 'NOT shown'); in front $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)")"
    log "PBACK surprise mid-gesture vs open: diff $(python3 $W19 rdiff $OUT/shots/pback/surprise-0.png $OUT/shots/pback/surprise-commit-mid.png "0 0 $W $H" 2>&1 | tail -1)"
    link "$BOOK_URL" >/dev/null; sleep 4; shot pback/book-0; pback book-commit; dump; log "PBACK book: after the gesture book screen $(has 'text="12 -те вятъра"' && echo 'STILL OPEN' || echo closed); in front $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)")"
    open_article && { shot pback/article-0; pback article-commit; dump; log "PBACK article: $(has 'Режим за четене' && echo 'STILL OPEN' || echo closed); in front $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)")"; }
    leave
    if open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && open_photo 1; then sleep 2; shot pback/viewer-0; pback viewer-commit; log "PBACK viewer: $( [ -n "$(viewer_page)" ] && echo 'STILL OPEN' || echo closed); in front $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)")"; leave; fi
    launch >/dev/null; sleep 2; ready; tab home; sleep 2; pback home-commit; log "PBACK on Home (root): in front after the gesture: $(fg)"
    launch >/dev/null; sleep 2
    nav_buttons
  fi
  sec "C5. Rotation and process death on API $SDK (book, catalogue, article)"
  robx book; robx catalog; robx article
  if [ "$SDK" -le 28 ]; then
    sec "C5b. Scanner screen: manual entry and back ×2, repeated ×8 (the API 26 crash came right after this in round 1)"
    for i in 1 2 3 4 5 6 7 8; do
      scanner; sleep 3; dump
      if tap text "$L_MANUAL" >/dev/null; then sleep 2; hide_ime; back 1.5; fi
      back 1.5; dump; log "SCAN loop $i: app in front $(has "package=\"$PKG\"" && echo yes || echo NO); $(texts 3 | cut -c1-100)"
      [ -n "$(pidof_app)" ] || { log "SCAN loop $i: the app process is GONE"; launch >/dev/null; sleep 4; }
    done
    ready >/dev/null
  fi
  local sd
  for sd in 1357 4242 777; do
    sec "C6. Monkey 2000 (seed $sd) on API $SDK"
    MONKEY_N=2000 MONKEY_SEED=$sd part_monkey
    cp $OUT/monkey.txt $OUT/monkey-$sd.txt 2>/dev/null
  done
  all_sheets
  diag_end
}

part_a6() {
  case "$A6_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    a6new) part_a6new;;
    year35) part_year35;;
    yup35) part_yup35;;
    land35) part_land35;;
    idle33) part_idle33;;
    hi28) part_hi28;;
    gfx35) part_gfx35;;
    upd35) part_upd35;;
    c26|c28|c36) part_compat;;
  esac
  return 0
}

# ---------------------------------------------------------------- ХОРИЗОНТАЛНО (API 33)
part_land35() {
  helper_install
  anims 1
  warmup
  card_open >/dev/null; leave
  sec "LD1. Landscape (rotation checked in the uiautomator hierarchy): the whole tour, light and dark"
  if land_on; then
    tour landscape-light
    a cmd uimode night yes; sleep 3; tour landscape-dark; a cmd uimode night no; sleep 2
    sec "LD2. The card and the fullscreen card in landscape (scrolled to the button)"
    card_screen; sleep 2; dump; shot land/card-screen
    for i in 1 2 3; do dump; p=$(xy text "$L_CARDSHOW"); [ -n "$p" ] && break; sw_up 300; sleep 1; done
    shot land/card-screen-scrolled
    if [ -n "$p" ]; then a input tap $p; sleep 3; eshot zz-land-fullscreen >/dev/null; log "fullscreen card in landscape: $(fullscreen_ok && echo yes || echo NO)"; back 1.5; else log "landscape: no 'Покажи картата' on the card screen: $(texts 8 | cut -c1-200)"; fi
    sec "LD3. Year-less screens at 2400x1080: Home at rest after 30 s (frames)"
    leave; ready; tab home; sleep 30; cpu_win land-home-30s 15
    land_off
  else log "LANDSCAPE: not achieved"; fi
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -30 | tee -a $SUM
  all_sheets
  diag_end
}

# ---------------------------------------------------------------- ГОДИНАТА: плъзгане надолу по точни разстояния (devDebug)
ydrag() {  # $1 — етикет, $2 — dp на последното UP, $3… — dp на MOVE по ред; DOWN на 30 % от височината
  local lbl=$1 up=$2 d i dens px; shift 2
  dens=$(dens | tr -dc 0-9); [ -n "$dens" ] || dens=420
  local x=$((W / 2)) y0=$((H * 30 / 100))
  { echo "input motionevent DOWN $x $y0"
    for d in "$@"; do px=$((d * dens / 160)); echo "sleep 0.06; input motionevent MOVE $x $((y0 + px))"; done
    px=$((up * dens / 160)); echo "sleep 0.06; input motionevent UP $x $((y0 + px))"
  } > $OUT/yd.sh
  timeout 20 adb push $OUT/yd.sh /data/local/tmp/yd.sh >/dev/null 2>&1
  T=60 a sh /data/local/tmp/yd.sh
  sleep 2.5; shot year/yd-$lbl >/dev/null
  echo "$(ystate)"
}
part_yup35() {
  local i r v
  helper_install
  dev_install
  anims 1
  warmup
  demo_signin_remember
  sec "YU1. Swipe-down dismiss with exact distances (motionevent): is the finger-up position counted? (the rule: ≥140 dp at any speed)"
  for v in "A-moves-to-183-up-183:183:23 46 69 92 115 138 183" "B-last-move-114-up-183:183:23 46 69 92 114" "C-moves-to-160-up-160:160:40 80 120 160" "D-one-move-20-up-183:183:20" "E-moves-to-100-up-100:100:25 50 75 100" "F-moves-to-139-up-139:139:35 70 105 139"; do
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
  all_sheets
  diag_end
  use_pkg org.chyavorec.app
}
