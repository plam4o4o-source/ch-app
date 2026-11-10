# Крайната проверка на v1.2.34 (ВРЕМЕННО, debug/wave20-final; резултати в emulator-results/v134).
# Тестовото v1.2.34 (b06d1ce, versionCode 1500184) срещу издаденото v1.2.33 (a830b51, 1500183) — и двете
# prodRelease x86_64 с временен ключ; devDebug от b06d1ce за демо входа. Зарежда се от run.sh след w20card.sh.
#   ab34     — студен/топъл старт A/B, n=20 на издание (2 блока по 10, редуват се), „Fully drawn“; и след speed-profile
#   gfx34    — gfxinfo: „Начало“, „Новини“, статия, каталог, картата (първите 15 s), прегледът; 2 кръга на издание
#   idle34   — CPU и кадри 30 s след отварянето на всеки екран (part_idle), новото
#   mem34    — паметта в 30 цикъла (part_mem), новото
#   monkey34 — monkey 6000 събития, seed 1357: новото, после 1.2.33
#   demo34   — devDebug: годината (време до първия слайд, затваряне с плъзгане, без връзка, хоризонтално + шрифт 1.3 +
#              „Много голям“), заявена книга („Заявена“ и след рестарт, „Запомни ме“), споделяне → изход чисти cache/share
#   dev34    — devDebug: StrictMode (само в debug) — обиколка и monkey; стековете на незатворените ресурси
#   misc34   — споделяне (JPEG при снимка, MIME, размер срещу PNG на 1.2.33), споделяне от лентата на статията при
#              превъртане, розетката по средата на жеста, достъпност (отметки, документи, календарът със свита лента,
#              прегледът, „Само налични“, полицата), настройката за разклащане — новото и 1.2.33
#   upd34    — 1.2.34-pre (1500180) → 1.2.34 (1500184): „По-късно“, рестарт, „Настройки › Обновления“ с картата
#   old28    — API 28: картата в покой (плочката), „Изненадай ме“, споделяне на събитие, monkey 2000
FINAL_PARTS=" ab34 gfx34 idle34 mem34 monkey34 demo34 dev34 misc34 upd34 old28 year34b shake34b idlecat mem34b "
W20_PARTS="$W20_PARTS $FINAL_PARTS"
WF=$H_/w20final.py
L_SHAKE='Разклащане за „Изненадай ме“'

# ---------------------------------------------------------------- A1. СТАРТ A/B
ab_block() {  # $1 — new | base, $2 — номер на блока
  local K=$1 b=$2 i
  sec "AB $K-$b: fresh install, warm-up, 2 excluded + 10 cold (Fully drawn) + 10 warm; speed-profile: 2 excluded + 10 cold + 10 warm"
  inst $(apk_of $K) $K-$b $(ref_of $K) || return
  a input keyevent KEYCODE_HOME; sleep 2
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$K-$b first-install catalogue results after ${WAITED} ms" || log "$K-$b: NO catalogue results"
  tab news; sleep 4; tab home; sleep 4
  log "$K-$b dexopt as installed: $(dexopt_state)"
  for i in 1 2; do cold_drawn excl-$K >/dev/null; done
  for i in $(seq 1 10); do cold_drawn $K-inst; done
  for i in $(seq 1 10); do a input keyevent KEYCODE_HOME; sleep 3; launch | tee -a $OUT/start-$K-inst-warm.txt | tr '\n' ' '; echo; sleep 2; done
  log "$K-$b compile: $(a cmd package compile -m speed-profile -f $PKG 2>&1 | tr -d '\r' | tail -1); dexopt now: $(dexopt_state)"
  for i in 1 2; do cold_drawn excl2-$K >/dev/null; done
  for i in $(seq 1 10); do cold_drawn $K-sp; done
  for i in $(seq 1 10); do a input keyevent KEYCODE_HOME; sleep 3; launch | tee -a $OUT/start-$K-sp-warm.txt | tr '\n' ' '; echo; sleep 2; done
}
part_ab34() {
  local b K m
  anims 1
  log "block order: base-1 new-1 base-2 new-2 (each a fresh install)"
  for b in 1 2; do ab_block base $b; ab_block new $b; done
  sec "AB totals (n=20 per app and mode)"
  for m in inst sp; do
    for K in new base; do
      python3 $H_/parse.py starts $OUT/drawn-$K-$m.txt "cold $K $m" | tee -a $SUM
      drawn_sum $K-$m
      python3 $H_/parse.py starts $OUT/start-$K-$m-warm.txt "warm $K $m" | tee -a $SUM
    done
  done
  diag_end
}

# ---------------------------------------------------------------- A2. GFXINFO ПО СЦЕНАРИИ
gfx_round() {  # $1 — new1 | base1 | new2 | base2
  local L=$1 K=${1%[0-9]} i
  sec "G $L: gfxinfo per scenario (animations ON)"
  inst $(apk_of $K) $L $(ref_of $K) || return
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$L catalogue after ${WAITED} ms"
  card_open >/dev/null || log "$L: card not added"; leave
  restart; tab home; sleep 30
  jank_scn home-scroll $L
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; jank_scn news $L
  open_article && { sleep 2; jank_scn article $L; }
  leave; ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 2; jank_scn catalog $L
  leave; gfx_reset; card_screen; sleep 11; gfx_save $L-card-0-15s; shot gfx/$L-card
  dump; if tap text "$L_CARDSHOW" >/dev/null; then gfx_reset; sleep 15; gfx_save $L-cardfull-0-15s; back 1.5; fi
  leave
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && open_photo 1; then
    sleep 2; gfx_reset; for i in $(seq 1 15); do sw_left 150; sleep 0.8; done; gfx_save $L-viewer; back 1.5; back 1.5; back 1.5
  else log "$L: viewer not reached"; fi
}
part_gfx34() {
  anims 1
  local r; for r in new1 base1 new2 base2; do gfx_round $r; done
  sec "G totals (mean of 2 rounds per app; emulator swangle: janky% is high for both — compare frames and p50/p90)"
  grep -E '^GFX ' $SUM | sort -k2 > $OUT/gfx-totals.txt
  python3 $WF gfxagg $SUM | tee $OUT/gfx-agg.txt | tee -a $SUM
  diag_end
}

# ---------------------------------------------------------------- A3/A4. CPU БЕЗ ДОКОСВАНЕ, ПАМЕТ, MONKEY
part_idle34() { part_idle; }
part_mem34() { part_mem; diag_end; }
part_monkey34() {
  part_monkey; mv $OUT/monkey.txt $OUT/monkey-new.txt
  sec "K-base. The same monkey on 1.2.33"
  inst app-base.apk base apk-base-ref.txt
  part_monkey; mv $OUT/monkey.txt $OUT/monkey-base.txt
  diag_end
}

# ---------------------------------------------------------------- B. ГОДИНАТА (devDebug)
dev_install() {
  use_pkg org.chyavorec.app.dev
  timeout 180 adb install -r app-dev.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  log "installed: $(cat apk-dev-ref.txt 2>/dev/null); $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
}
demo_signin_remember() {
  local p
  ready; tab my; sleep 3; tap text "$L_SIGNIN" >/dev/null || log "no 'Вход' in My: $(texts 10 | cut -c1-200)"
  sleep 3; dump; p=$(xy --all class EditText | sed -n 1p)
  if [ -n "$p" ]; then
    a input tap $p; sleep 0.8; a input text DEMO-0001; sleep 0.5
    dump; p=$(xy --all class EditText | sed -n 2p); a input tap $p; sleep 0.8; a input text demo; sleep 0.5; hide_ime
    dump; log "Запомни ме before: $(python3 $WF checkstate $OUT/ui.xml 'Запомни ме')"
    python3 $WF checkstate $OUT/ui.xml 'Запомни ме' | grep -q 'checked=false' && { tap text 'Запомни ме' >/dev/null; sleep 1; dump; }
    log "Запомни ме now: $(python3 $WF checkstate $OUT/ui.xml 'Запомни ме')"
    tap text '=Вход' >/dev/null; sleep 6
  fi
  ready; tab my; sleep 3; dump
  log "MY after the demo sign-in: $(texts 8 | cut -c1-240)"
}
ys_open_check() {  # опитва се да види историята отворена
  dump && grep -qE 'content-desc="[^"]*(Затвори|Следващ слайд)"|Слайд [0-9]+ от' $OUT/ui.xml
}
year_reopen() {
  local i
  year_story_closed || { back 2; }
  ready; tab my; sleep 1.5; top_of; year_card_find || return 1
  tap text "$L_YEAR" >/dev/null
  for i in $(seq 1 25); do sleep 1; ys_open_check && return 0; done
  return 1
}
year_tap_shots() {  # $1 — етикет: докосване на картата на годината от устройството и 12 снимки поред с времената
  local p d=/sdcard/ys
  year_card_find || { log "YT $1: year card not found"; return 1; }
  p=$(xy text "$L_YEAR")
  a "rm -rf $d; mkdir -p $d"
  cat > $OUT/ys.sh <<EOF
read u r < /proc/uptime; echo "tap \$u" > $d/t.txt
input tap $p
read u r < /proc/uptime; echo "tapdone \$u" >> $d/t.txt
for i in 1 2 3 4 5 6 7 8 9 10 11 12; do screencap -p $d/\$i.png; read u r < /proc/uptime; echo "\$i \$u" >> $d/t.txt; done
EOF
  timeout 20 adb push $OUT/ys.sh /data/local/tmp/ys.sh >/dev/null 2>&1
  T=90 a sh /data/local/tmp/ys.sh
  mkdir -p $OUT/shots/yt/$1; timeout 60 adb pull $d/. $OUT/shots/yt/$1/ >/dev/null 2>&1
  log "YT $1 shots: $(python3 $WF shots $OUT/shots/yt/$1/t.txt $OUT/shots/yt/$1/*.png | head -1 | cut -c1-900)"
  log "YT $1: $(python3 $WF shots $OUT/shots/yt/$1/t.txt $OUT/shots/yt/$1/*.png | tail -1)"
  python3 $W20 strip $OUT/shots/yt/$1-strip.png all $(ls $OUT/shots/yt/$1/*.png | sort -V | head -8) >/dev/null
  sleep 1; dump; log "YT $1 after the shots (uiautomator): $(python3 $WF ys $OUT/ui.xml $H)"
}
year_swipes() {
  local spec y1 px dur lbl dp i
  dp=$(dens | tr -dc 0-9); [ -n "$dp" ] || dp=420
  # px за dp при текущата плътност
  for spec in "d200t150:200:150" "d200t300:200:300" "d200t600:200:600" "d120fast:120:70" "d120slow:120:900" "d40fast:40:45" "d20fast:20:45" "d160slow:160:1500"; do
    IFS=: read lbl px dur <<< "$spec"
    local dist=$(( px * dp / 160 ))
    ys_open_check || year_reopen || { log "YEAR swipe $lbl: story not open"; continue; }
    sleep 1.5
    y1=$((H*35/100))
    a input swipe $((W/2)) $y1 $((W/2)) $((y1 + dist)) $dur; sleep 2.5; shot year/swipe-$lbl
    log "YEAR swipe down $lbl (${px} dp = $dist px in ${dur} ms, ~$(( px * 1000 / dur )) dp/s avg): $(year_story_closed && echo closed || echo 'STILL OPEN')"
  done
  year_story_closed || back 2
}
part_demo34() {
  local i p inv
  helper_install
  dev_install
  anims 1
  warmup
  sec "B0. Demo sign-in with „Запомни ме“"
  demo_signin_remember
  sec "B1. Year story: time from the tap to the first slide (device-side tap + screencaps with uptime)"
  a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 5; dismiss; ready; tab my; sleep 3; top_of
  year_tap_shots fresh
  back 2; sleep 1; year_story_closed || back 2; ready; tab my; sleep 2; top_of
  year_tap_shots reopen
  back 2; sleep 1; year_story_closed || back 2
  sec "B1b. The same with a screen recording (8 fps) for the frames (fresh process)"
  a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 5; dismiss; ready; tab my; sleep 3; top_of
  year_timed rec-fresh
  back 2; sleep 1; year_story_closed || back 2
  sec "B2. Swipe-down dismiss: ≥140 dp at any speed, or a fast flick (≥700 dp/s) after ≥32 dp"
  year_reopen; year_swipes
  sec "B3. Offline: the story with airplane mode on (fresh process)"
  offline; a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 5; dismiss; ready; tab my; sleep 3; top_of
  if year_card_find; then tap text "$L_YEAR" >/dev/null; sleep 4; dump; shot year/offline-1; log "YEAR offline: $(python3 $WF ys $OUT/ui.xml $H)"
    if has 'text="Опитай отново"'; then tap text '=Опитай отново' >/dev/null; sleep 3; dump; shot year/offline-retry; log "YEAR offline after retry (still offline): $(python3 $WF ys $OUT/ui.xml $H)"; fi
    online; sleep 8; dump; shot year/offline-reconnect; log "YEAR after reconnect (8 s): $(python3 $WF ys $OUT/ui.xml $H)"
    has 'text="Опитай отново"' && { tap text '=Опитай отново' >/dev/null; sleep 4; dump; log "YEAR after reconnect + retry: $(python3 $WF ys $OUT/ui.xml $H)"; }
  else log "YEAR offline: no year card: $(texts 8 | cut -c1-200)"; online; fi
  log "YEAR offline: demo history cache: $(a "ls -l /data/data/$PKG/files /data/data/$PKG/no_backup 2>/dev/null" | tr -d '\r' | grep -iE 'hist|reader|cache' | head -3 | tr -s ' ' | tr '\n' ';')"
  year_story_closed || back 2; sleep 3
  sec "B4. Landscape + font 1.3 + „Много голям“: each slide (scrollable? clipped? ellipsized?)"
  set_text_size '=Много голям' >/dev/null; back 1.5; back 1.5
  a settings put system font_scale 1.3; rot 1; sleep 3; dims
  ready; tab my; sleep 2; top_of
  if year_card_find; then tap text "$L_YEAR" >/dev/null; sleep 4
    for i in 1 2 3 4 5 6 7 8; do
      dump; shot year/land-slide-$i; log "YEAR land+1.3+XL slide $i: $(python3 $WF ys $OUT/ui.xml $H)"
      cp $OUT/ui.xml $OUT/year-land-$i.xml
      a input swipe $((W/2)) $((H*75/100)) $((W/2)) $((H*35/100)) 500; sleep 1.5; shot year/land-slide-$i-scrolled
      log "  scroll inside slide $i: diff $(python3 $W19 rdiff $OUT/shots/year/land-slide-$i.png $OUT/shots/year/land-slide-$i-scrolled.png "0 0 $W $H" 2>&1 | tail -1)"
      p=$(xy content-desc '=Следващ слайд'); [ -n "$p" ] || break
      a input tap $p; sleep 2
    done
    back 2
  else log "YEAR landscape: no year card"; fi
  rot 0; a settings put system font_scale 1.0; sleep 2
  year_story_closed || back 2
  set_text_size '=Стандартен' >/dev/null; back 1.5; back 1.5
  sec "B5. Portrait, font 1.3 + „Много голям“: the first slides"
  set_text_size '=Много голям' >/dev/null; back 1.5; back 1.5; a settings put system font_scale 1.3; sleep 2
  ready; tab my; sleep 2; top_of
  if year_card_find; then tap text "$L_YEAR" >/dev/null; sleep 4
    for i in 1 2 3 4 5 6 7 8; do dump; shot year/port13-slide-$i; log "YEAR port+1.3+XL slide $i: $(python3 $WF ys $OUT/ui.xml $H)"
      p=$(xy content-desc '=Следващ слайд'); [ -n "$p" ] || break; a input tap $p; sleep 2; done
    back 2
  fi
  a settings put system font_scale 1.0; year_story_closed || back 2
  set_text_size '=Стандартен' >/dev/null; back 1.5; back 1.5
  sec "B6. Hold: „Заяви книгата“ → „Заявена“; again after an app restart („Запомни ме“)"
  for inv in $INV 1 2 3 5 8 13; do
    link "$SITE/elektronen-katalog?inv=$inv" >/dev/null; sleep 4; dump
    for i in 1 2; do [ -n "$(xy text '=Заяви книгата|=Заявена')" ] && break; sw_up 400; sleep 1; dump; done
    [ -n "$(xy text '=Заяви книгата|=Заявена')" ] && break
    back 1.5
  done
  if [ -n "$(xy text '=Заяви книгата')" ]; then
    shot hold/0-before; tap text '=Заяви книгата' >/dev/null; sleep 3; dump; shot hold/1-after
    log "HOLD inv $inv after the tap: $(grep -oE '(text|content-desc)="[^"]*(Заявена|Заяви книгата|Заявката)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' ')"
    back 1.5; link "$SITE/elektronen-katalog?inv=$inv" >/dev/null; sleep 4; dump; for i in 1 2; do [ -n "$(xy text '=Заявена|=Заяви книгата')" ] && break; sw_up 400; sleep 1; dump; done
    shot hold/2-reopened; log "HOLD reopened (same process): $(grep -oE 'text="[^"]*(Заявена|Заяви книгата)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
    a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 6; dismiss
    link "$SITE/elektronen-katalog?inv=$inv" >/dev/null; sleep 5; dump; for i in 1 2; do [ -n "$(xy text '=Заявена|=Заяви книгата')" ] && break; sw_up 400; sleep 1; dump; done
    shot hold/3-after-restart; log "HOLD after an app restart: $(grep -oE 'text="[^"]*(Заявена|Заяви книгата)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
    ready >/dev/null; tab my >/dev/null; sleep 3; dump; log "HOLD: still signed in after the restart: $(has 'DEMO-0001' && echo yes || echo NO)"
  elif [ -n "$(xy text '=Заявена')" ]; then log "HOLD: inv $inv already „Заявена“ (from an earlier tap?)"
  else log "HOLD: no 'Заяви книгата' on the tried books"; fi
  sec "B7. Share files (event card, year slide) → logout → cache/share"
  share_event_scan dev 6
  year_reopen && { tap content-desc '=Сподели' >/dev/null; sleep 6; log "YEAR share: fg $(fg)"; chooser_up && back 1.5; back 2; }
  log "cache/share before logout: $(a "ls -l /data/data/$PKG/cache/share/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';')"
  ready; tab my; sleep 2
  for i in 1 2 3 4 5 6 7 8; do dump; [ -n "$(xy text '=Изход')" ] && break; sw_up 500; sleep 1; done
  if tap text '=Изход' >/dev/null; then sleep 2; dump; shot logout/confirm; tap text '=Изход' --last >/dev/null; sleep 4; dump; shot logout/after
    log "LOGOUT: $(texts 6 | cut -c1-200)"
    log "cache/share after logout: '$(a "ls -l /data/data/$PKG/cache/share/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';')'"
  else log "LOGOUT: no 'Изход' in My"; fi
  diag_end
}

# ---------------------------------------------------------------- C. СПОДЕЛЯНЕ
share_newest() { a "ls -t /data/data/$PKG/cache/share/ 2>/dev/null" | tr -d '\r' | head -1; }
share_try() {  # $1 — етикет: на екран с „Сподели“ → „Сподели като картинка“ → избора; SH_FILE — новият файл
  local i
  SH_FILE=""
  a "rm -rf /data/data/$PKG/cache/share"
  mark sh-$1
  tap content-desc '=Сподели|=Share' >/dev/null || { log "SHARE $1: no share button"; return 1; }
  sleep 2; dump
  has 'Сподели като картинка' || { log "SHARE $1: no choice sheet ($(texts 6 | cut -c1-160))"; back 1.5; return 1; }
  tap text '=Сподели като картинка' >/dev/null
  for i in $(seq 1 20); do sleep 1; chooser_up && break; done
  SH_FILE=$(share_newest)
  log "SHARE $1: chooser $(chooser_up && echo OPEN || echo 'NOT open') after $i s; cache/share: $(a "ls -l /data/data/$PKG/cache/share/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';')"
  if [ -n "$SH_FILE" ]; then mkdir -p $OUT/share; timeout 20 adb pull "/data/data/$PKG/cache/share/$SH_FILE" $OUT/share/$1-$SH_FILE >/dev/null 2>&1
    log "SHARE $1 file: $SH_FILE $(stat -c %s $OUT/share/$1-$SH_FILE 2>/dev/null) B; $(python3 -c 'import sys; from PIL import Image; i=Image.open(sys.argv[1]); print(i.format, i.size, i.mode)' $OUT/share/$1-$SH_FILE 2>&1 | tail -1)"; fi
  chooser_up
}
share_event_scan() {  # $1 — етикет, $2 — колко събития: първото с .jpg (новото) / същото N (EV_N зададено)
  local n found=""
  for n in ${EV_N:-$(seq 1 ${2:-8})}; do
    open_event $n || continue
    dump; local title; title=$(texts 2 | cut -c1-90)
    if share_try ev$n-$1; then
      case "$SH_FILE" in
        *.jpg) found=$n; log "SHARE ev$n-$1: JPEG (event with a photo): $title"; share_pick_helper ev$n-$1;;
        *) [ -n "${EV_N:-}" ] && { found=$n; share_pick_helper ev$n-$1; } || { log "SHARE ev$n-$1: $SH_FILE (no photo?) $title"; back 1.5; dump; chooser_up && back 1.5; };;
      esac
    fi
    leave
    [ -n "$found" ] && break
  done
  EV_FOUND=$found
  log "SHARE $1: event with a photo: ${found:-none of the first ${2:-8}}"
}
article_share_scroll() {  # $1 — етикет: „Сподели като картинка“ в лентата на статията и веднага превъртане
  local i
  open_article || { log "ARTSHARE $1: article not opened"; return; }
  a "rm -rf /data/data/$PKG/cache/share"; mark sh-as-$1
  tap content-desc '=Сподели' >/dev/null || { log "ARTSHARE $1: no share in the toolbar"; return; }
  sleep 1.5; dump; local p; p=$(xy text '=Сподели като картинка')
  [ -n "$p" ] || { log "ARTSHARE $1: no sheet"; back 1.5; return; }
  a "input tap $p; sleep 0.15; input swipe $((W/2)) $((H*75/100)) $((W/2)) $((H*30/100)) 120; input swipe $((W/2)) $((H*75/100)) $((W/2)) $((H*30/100)) 120; input swipe $((W/2)) $((H*75/100)) $((W/2)) $((H*30/100)) 120"
  shot artshare/$1-scrolled
  for i in $(seq 1 20); do sleep 1; chooser_up && break; done
  log "ARTSHARE $1 (scrolled 3× right after 'Сподели като картинка'): chooser $(chooser_up && echo OPEN || echo 'NOT open') after $i s; toolbar now $(dump; has 'content-desc="Режим за четене"' && echo visible || echo hidden); cache/share: $(a "ls -l /data/data/$PKG/cache/share/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';')"
  chooser_up && share_pick_helper as-$1
  leave
}

# ---------------------------------------------------------------- D. ДОСТЪПНОСТ И ДРУГИ
a11y34() {  # $1 — new | base
  local K=$1 i
  CFG=a11y-$K; DENS=$(dens)
  a11y_on
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; sleep 2; dump
  log "A-$K NEWS bookmark buttons (uiautomator): named $(grep -cE 'content-desc="Запази: ' $OUT/ui.xml), bare 'Запази' $(grep -c 'content-desc="Запази"' $OUT/ui.xml); e.g. $(grep -oE 'content-desc="Запази[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  adump news-$K >/dev/null; python3 $WF nodes $OUT/a11ytree-news-$K.txt '^Запази' | tail -3 | tee -a $SUM
  link "$SITE/load" >/dev/null; sleep 6; dump; shot a11y/$K-documents
  log "A-$K DOCUMENTS PDF buttons: named $(grep -cE 'content-desc="Отвори с приложение за PDF: ' $OUT/ui.xml), bare $(grep -c 'content-desc="Отвори с приложение за PDF"' $OUT/ui.xml); e.g. $(grep -oE 'content-desc="Отвори с приложение за PDF[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  adump documents-$K >/dev/null; python3 $WF nodes $OUT/a11ytree-documents-$K.txt 'PDF' | tail -2 | tee -a $SUM
  leave
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar; sleep 2
  adump cal-top-$K >/dev/null; python3 $WF topbar $OUT/a11ytree-cal-top-$K.txt 420 $DENS | sed "s/^/A-$K cal top: /" | tee -a $SUM
  for i in 1 2 3; do sw_up 250; sleep 0.8; done; sleep 1.5; shot a11y/$K-calendar-collapsed; dump; cp $OUT/ui.xml $OUT/a11y-cal-collapsed-$K.xml
  adump cal-collapsed-$K >/dev/null; python3 $WF topbar $OUT/a11ytree-cal-collapsed-$K.txt 420 $DENS | sed "s/^/A-$K cal collapsed: /" | tee -a $SUM
  log "A-$K cal collapsed (uiautomator): top-bar actions present: $(grep -oE 'content-desc="(Абонирай се за календара|Изглед [^"]*|Търси[^"]*)"[^>]*bounds="[^"]*"' $OUT/ui.xml | sed -E 's/" .*bounds=/ /' | tr '\n' ';')"
  leave
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && open_photo 1; then sleep 2
    adump viewer-$K >/dev/null; log "A-$K VIEWER pane titles: $(python3 $W16 tree $OUT/a11ytree-viewer-$K.txt | grep -oE 'pane titles: \[[^]]*\]')"
    back 1.5; back 1.5; back 1.5; fi
  leave; ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1
  if tap content-desc "$L_FILTERS" >/dev/null; then sleep 2; dump; cap filters 0
    adump filters-$K >/dev/null; python3 $WF nodes $OUT/a11ytree-filters-$K.txt 'Само налични' | tee -a $SUM
    log "A-$K filters (uiautomator) 'Само налични': $(python3 $WF checkstate $OUT/ui.xml 'Само налични'); checkable nodes on the sheet: $(grep -c 'checkable="true"' $OUT/ui.xml)"
    back 1.5; fi
  dump; has 'text="(Начало|Home)"' || back 1.5
  if tap content-desc "$L_SHELF" >/dev/null; then sleep 3; cap shelf 0
    adump shelf-$K >/dev/null; python3 $WF spines $OUT/a11ytree-shelf-$K.txt $DENS | sed "s/^/A-$K /" | tee -a $SUM
    tap content-desc "$L_LIST" >/dev/null; sleep 1; fi
  a11y_off
}
shake3() {  # три удара над 2.5 g за < 1 s, после покой ~1 g (новото изискване)
  emu sensor set acceleration 0:30:0 >/dev/null; sleep 0.13; emu sensor set acceleration 0:9.81:0 >/dev/null; sleep 0.08
  emu sensor set acceleration 0:-28:4 >/dev/null; sleep 0.13; emu sensor set acceleration 0:9.81:0 >/dev/null; sleep 0.08
  emu sensor set acceleration 0:30:0 >/dev/null; sleep 0.13; emu sensor set acceleration 0:9.81:0 >/dev/null; sleep 1.6
}
surprise_shown() { dump; has 'text="(Предложение|Към книгата|Още една|Избирам книга…)"'; }
surprise_close() { surprise_shown && { tap content-desc '=Затвори' >/dev/null || back 1.5; sleep 1.5; }; }
shake_round() {  # $1 — етикет, $2 — функцията за разклащане
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 2
  $2; sleep 1; local r; surprise_shown && r=TRIGGERED || r='not triggered'
  log "SHAKE $1: $r"; shot shake/$1; surprise_close
}
shake34() {
  sec "SH1. Shake with the setting at its default: 3 peaks + stillness (opens), the old 2-peak pattern"
  shake_round default-3peaks shake3
  shake_round default-2peaks shake
  shake_round default-3peaks-again shake3
  sec "SH2. Settings › Изглед › „$L_SHAKE“ off → shake does nothing"
  if open_settings && settings_find "=$L_SHAKE"; then dump; log "setting before: $(python3 $WF checkstate $OUT/ui.xml "$L_SHAKE")"
    tap text "=$L_SHAKE" >/dev/null; sleep 1.5; dump; shot shake/setting-off; log "setting after the tap: $(python3 $WF checkstate $OUT/ui.xml "$L_SHAKE")"
    back 1.5; back 1.5
    shake_round off-3peaks shake3
    shake_round off-3peaks-b shake3
    open_settings && settings_find "=$L_SHAKE" && { tap text "=$L_SHAKE" >/dev/null; sleep 1.5; dump; log "setting back on: $(python3 $WF checkstate $OUT/ui.xml "$L_SHAKE")"; back 1.5; back 1.5; }
    shake_round on-again-3peaks shake3
  else log "SHAKE setting not found in Settings"; fi
  emu sensor set acceleration 0:9.81:0 >/dev/null
}
part_misc34() {
  local K
  helper_install
  anims 1
  for K in new base; do
    [ $K = base ] && inst app-base.apk base apk-base-ref.txt
    warmup
    sec "C1-$K. Event with a photo: share as image (file in cache/share, MIME in the chooser)"
    if [ $K = new ]; then EV_N=""; share_event_scan $K 8; EV_NEW=$EV_FOUND
    else EV_N=${EV_NEW:-1} share_event_scan $K 1; fi
    sec "C1b-$K. Article: share as image (photo card)"
    open_article && { share_try art-$K && share_pick_helper art-$K; leave; }
    sec "C2-$K. Article toolbar share while scrolling during the render"
    article_share_scroll $K-1; article_share_scroll $K-2
    sec "D1-$K. Pull-to-refresh rosette mid-gesture (slow pull, screencaps on the device)"
    link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; pull_refresh news-$K 45
    pull_refresh news-top-$K 22
    link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar; pull_refresh calendar-$K 45
    [ $K = new ] && shake34
    sec "D2-$K. Accessibility: news bookmarks, documents, calendar with the collapsed bar, viewer pane title, „Само налични“, shelf spines"
    a11y34 $K
    a cmd uimode night yes; sleep 2; link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; pull_refresh news-dark-$K 45; a cmd uimode night no; sleep 2
  done
  python3 $W20 a11yagg $OUT/a11y-a11y-*.txt | head -30 | tee -a $SUM
  diag_end
}

# ---------------------------------------------------------------- D. КАРТАТА ЗА ОБНОВЯВАНЕ В НАСТРОЙКИТЕ
# Листът „Размер на текста“ от въведението (първото пускане, прекъснато от диалога за обновяване) — „Затвори“.
textsize_close() {
  dump; if has 'text="Размер на текста"' && has 'text="Затвори"'; then log "(onboarding sheet 'Размер на текста' shown after the relaunch → Затвори)"; tap text '=Затвори' >/dev/null; sleep 2; fi
}
part_upd34() {
  local p
  helper_install
  anims 1
  sec "U0. 1.2.34-pre (b06d1ce, versionCode 1500180) with the staged update.json → 1.2.34 (1500184)"
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  log "staged manifest: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["size"])' $OUT/update.json 2>&1)"
  inst app-new-upd.apk new-upd apk-new-upd-ref.txt || return
  a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  launch >/dev/null
  if upd_wait_dialog u; then shot update/u-10-dialog
    tap text '=По-късно|=Later' >/dev/null; sleep 2
    log "downloaded files: $(upd_files)"
    sec "U1. „По-късно“ + relaunch: no dialog; Settings › Обновления shows the ready card"
    a am force-stop $PKG; launch >/dev/null; sleep 8; dump; shot update/u-15-relaunch; log "after 'По-късно' + relaunch: dialog $(has 'text="(Нова версия|New version)' && echo SHOWN || echo 'not shown'); on screen: $(texts 6 | cut -c1-200)"
    textsize_close; dismiss
    if open_settings && settings_find 'е готова|Провери за нова версия|Налична е версия'; then sleep 2; dump; shot update/u-20-settings
      log "SETTINGS › Обновления: $(grep -oE 'text="[^"]*(е готова|Изтеглена и проверена|Инсталирай|Налична е версия|Провери за нова версия|Изтегли и инсталирай)[^"]*"' $OUT/ui.xml | tr '\n' ' ')"
      sec "U2. Process death, then Settings again (the card restored)"
      a am force-stop $PKG; launch >/dev/null; sleep 5; textsize_close; dismiss
      if open_settings && settings_find 'е готова|Провери за нова версия|Налична е версия'; then sleep 2; dump; shot update/u-30-settings-again
        log "SETTINGS after a 2nd relaunch: $(grep -oE 'text="[^"]*(е готова|Изтеглена и проверена|Инсталирай|Налична е версия|Провери за нова версия)[^"]*"' $OUT/ui.xml | tr '\n' ' ')"
        cp $OUT/ui.xml $OUT/settings-update.xml
        sec "U3. „Инсталирай“ on the card"
        p=$(xy text '=Инсталирай')
        if [ -n "$p" ]; then a input tap $p
          if installer_confirm 1500184 u; then log "UPDATE via the settings card: installed $(ver)"; else log "UPDATE via the settings card: NOT installed: $(ver); $(texts 8 | cut -c1-200)"; fi
        else log "no 'Инсталирай' on the settings screen"; fi
      fi
    fi
  fi
  diag_end
}

# ---------------------------------------------------------------- E. StrictMode (devDebug)
dev_tour() {
  local i
  ready; tab home; sleep 4; sw_up 400; sleep 1; sw_up 400; sleep 1; top_of
  ready; tab catalog; waitfor "$RX_RESULTS" 60; search_catalog 12 >/dev/null; sleep 2
  tap content-desc "$L_SHELF" >/dev/null && { sleep 3; fling_v; tap content-desc "$L_LIST" >/dev/null; sleep 1; }
  tap content-desc "$L_SURPRISE" >/dev/null && { sleep 4; surprise_close; }
  link "$BOOK_URL" >/dev/null; sleep 4; sw_up 400; sleep 1; back 1.5
  open_article && { sleep 2; sw_up 300; sleep 1; share_try art-dev && { back 1.5; chooser_up && back 1.5; }; leave; }
  open_event 1 && { share_try ev1-dev && { back 1.5; chooser_up && back 1.5; }; leave; }
  link_pkg "$SITE/news" >/dev/null; sleep 4; segstate | grep -q 'News\[checked=true' || segment news; pull_refresh dev-news 45
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && open_photo 1; then for i in 1 2 3 4; do sw_left 150; sleep 1; done; back 1.5; back 1.5; back 1.5; fi
  link "$SITE/load" >/dev/null; sleep 5; pdf_try devdoc 1 >/dev/null 2>&1; dump; has "package=\"$PKG\"" || launch >/dev/null
  leave; card_open >/dev/null && { sleep 3; tap text "$L_CARDSHOW" >/dev/null; sleep 3; back 1.5; }
  leave; ready; tab my; sleep 3; top_of; year_card_find && { tap text "$L_YEAR" >/dev/null; sleep 6; back 2; }
  open_settings && { sw_up 600; sleep 1; back 1.5; }
  ready; tab more; sleep 2
  a am send-trim-memory $PKG RUNNING_CRITICAL >/dev/null 2>&1; sleep 2
  go_home 2; launch >/dev/null; sleep 3
  log "tour done; StrictMode lines so far: $(grep -c ' StrictMode' $OUT/logcat-live.txt)"
  # CloseGuard съобщава при финализиране — събиране на боклука
  local pid; pid=$(pidof_app); [ -n "$pid" ] && { a "kill -10 $pid" >/dev/null 2>&1; sleep 3; }
}
part_dev34() {
  helper_install
  dev_install
  anims 1
  warmup
  sec "E0. Demo sign-in"
  demo_signin
  sec "E1. Short tour (devDebug, StrictMode VM policy: leaked closables/registrations/SQLite)"
  dev_tour
  python3 $WF strict $OUT/logcat-live.txt $PKG > $OUT/strictmode-tour.txt; head -14 $OUT/strictmode-tour.txt | tee -a $SUM
  sec "E2. Monkey on devDebug (seed 1357, 6000 events)"
  part_monkey; mv $OUT/monkey.txt $OUT/monkey-dev-1357.txt
  sec "E3. Monkey on devDebug (seed 24680, 4000 events)"
  restart; a log -t PERFCHECK "monkey2 start"
  timeout 1500 adb shell monkey -p $PKG --pct-syskeys 0 --pct-anyevent 0 --throttle 80 -s 24680 -v 4000 > $OUT/monkey-dev-24680.txt 2>&1
  log "MONKEY 24680: $(grep -E 'Events injected|Monkey finished|aborted' $OUT/monkey-dev-24680.txt | tr '\n' ' '); CRASH=$(grep -c CRASH $OUT/monkey-dev-24680.txt) ANR=$(grep -c 'NOT RESPONDING\|ANR in' $OUT/monkey-dev-24680.txt)"
  online; a am send-trim-memory $PKG RUNNING_CRITICAL >/dev/null 2>&1; sleep 5
  sec "E4. StrictMode / CloseGuard over the whole run (devDebug only; stacks show where the resource was opened)"
  adb logcat -d -v threadtime > $OUT/logcat-sm-src.txt 2>/dev/null
  cat $OUT/logcat-live.txt $OUT/logcat-sm-src.txt | awk '!s[$0]++' > $OUT/logcat-sm.txt
  python3 $WF strict $OUT/logcat-sm.txt $PKG > $OUT/strictmode.txt; head -16 $OUT/strictmode.txt | tee -a $SUM
  log "W/System 'resource failed' lines (any pid): $(grep -c 'A resource failed to call' $OUT/logcat-sm.txt)"
  rm -f $OUT/logcat-sm-src.txt $OUT/logcat-sm.txt
  diag_end
}

# ---------------------------------------------------------------- API 28
part_old28f() {
  helper_install; dev_locale bg-BG || true; a am force-stop $PKG
  anims 1
  warmup
  sec "O1. Card screen on API $SDK (tiled шевица fallback): no input 0-15 s and 30-45 s"
  card_open >/dev/null || log "card not shown"; local t0; t0=$(now_ms)
  cpu_win card28-0-15s 15; while [ $(( $(now_ms) - t0 )) -lt 30000 ]; do sleep 0.5; done; cpu_win card28-30-45s 15; shot old28/card
  dump; tap text "$L_CARDSHOW" >/dev/null && { sleep 30; cpu_win cardfull28-30-45s 15; shot old28/card-full; back 1.5; }
  leave
  sec "O2. Surprise (lite shuffle on API 26-30) ×3"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1
  local i; for i in 1 2 3; do tap content-desc "$L_SURPRISE" >/dev/null; sleep 0.8; shot old28/surprise-$i-mid; sleep 3.5; dump; shot old28/surprise-$i
    log "SURPRISE $i: $(grep -oE 'text="(Предложение|Към книгата|Още една)"' $OUT/ui.xml | tr '\n' ' '); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"; surprise_close; done
  sec "O3. Share an event as image on API $SDK"
  a "rm -rf /data/data/$PKG/cache/share" 2>/dev/null
  EV_N="1 2" share_event_scan old28 2
  sec "O4. Monkey 2000 (seed 1357)"
  MONKEY_N=2000 part_monkey
  diag_end
}

# ================================================================= КРЪГ 2
# Годината: по-точно време (raw снимки ~0.2 s), затваряне с плъзгане (всеки път от първия слайд, по снимка),
# хоризонтално (с проверка, че екранът наистина е завъртян) — новото и devDebug на 1.2.33 (a830b51).
year_tap_raw() {  # $1 — етикет: докосване на картата и 24 raw снимки с uptime
  local p d=/data/local/tmp/ysr
  year_card_find || { log "YR $1: year card not found"; return 1; }
  p=$(xy text "$L_YEAR")
  a "rm -rf $d; mkdir -p $d"
  cat > $OUT/ysr.sh <<EOF
read u r < /proc/uptime; echo "tap \$u" > $d/t.txt
input tap $p
read u r < /proc/uptime; echo "tapdone \$u" >> $d/t.txt
i=1; while [ \$i -le 24 ]; do screencap $d/\$i.raw; read u r < /proc/uptime; echo "\$i \$u" >> $d/t.txt; i=\$((i+1)); done
EOF
  timeout 20 adb push $OUT/ysr.sh /data/local/tmp/ysr.sh >/dev/null 2>&1
  T=120 a sh /data/local/tmp/ysr.sh
  local o=$OUT/yr/$1; mkdir -p $o; timeout 120 adb pull $d/. $o/ >/dev/null 2>&1
  python3 $WF rawshots $o/t.txt $o/*.raw > $o/result.txt 2>&1
  log "YR $1: $(tail -1 $o/result.txt)"
  log "YR $1 shots: $(head -1 $o/result.txt | cut -c1-700)"
  rm -f $o/*.raw; a "rm -rf $d"
}
ystate() {  # снимка → open | closed
  shot year/state >/dev/null; python3 $WF state $OUT/shots/year/state.png | cut -d' ' -f1
}
year_open_fresh() {  # затваря (ако е отворена), отваря отначало и чака първия слайд (до $1 s)
  local i
  [ "$(ystate)" = open ] && back 2
  ready >/dev/null; tab my >/dev/null; sleep 1.5; top_of; year_card_find || return 1
  tap text "$L_YEAR" >/dev/null
  for i in $(seq 1 ${1:-25}); do sleep 1; shot year/wait >/dev/null; python3 $WF state $OUT/shots/year/wait.png | grep -q 'SLIDE' && return 0; done
  return 1
}
year_swipes2() {  # $1 — етикет (new | base), $2 — колко пъти всяко
  local spec px dur lbl dp dist y1 r k n=0 c=0
  dp=$(dens | tr -dc 0-9); [ -n "$dp" ] || dp=420
  for spec in "d200t150:200:150" "d200t300:200:300" "d200t600:200:600" "d120fast:120:70" "d120slow:120:900" "d40fast:40:45" "d20fast:20:45" "v133fast200:525:200"; do
    IFS=: read lbl px dur <<< "$spec"
    dist=$(( px * dp / 160 )); [ $lbl = v133fast200 ] && dist=$((H*55/100))
    r=""
    for k in $(seq 1 ${2:-2}); do
      year_open_fresh ${YWAIT:-25} || { r="$r no-open"; continue; }
      sleep 1.2; y1=$((H*30/100))
      a input swipe $((W/2)) $y1 $((W/2)) $((y1 + dist)) $dur; sleep 2.5
      shot year/sw-$1-$lbl-$k >/dev/null
      r="$r $(python3 $WF state $OUT/shots/year/sw-$1-$lbl-$k.png | cut -d' ' -f1)"
    done
    log "YEAR2 $1 swipe $lbl ($dist px ≈ $((dist * 160 / dp)) dp in $dur ms):$r"
  done
  [ "$(ystate)" = open ] && back 2
}
land_on() {  # хоризонтално; проверка по йерархията на uiautomator (rotation="1|3")
  local m
  for m in 1 2 3; do
    case $m in
      1) a settings put system accelerometer_rotation 0; a settings put system user_rotation 1; a wm user-rotation lock 1 >/dev/null 2>&1;;
      2) a cmd window user-rotation lock 1 >/dev/null 2>&1; a content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1 >/dev/null 2>&1;;
      3) a settings put system accelerometer_rotation 1; emu rotate >/dev/null; sleep 1; emu sensor set acceleration 9.81:0:0 >/dev/null;;
    esac
    sleep 3; dump
    if grep -qE 'hierarchy rotation="(1|3)"' $OUT/ui.xml; then log "LANDSCAPE on (method $m): $(grep -oE 'hierarchy rotation="[0-9]"' $OUT/ui.xml); $(a dumpsys window displays 2>/dev/null | tr -d '\r' | grep -m1 -oE 'mRotation=[0-9]|cur=[0-9x]+' | tr '\n' ' ')"; LAND_M=$m; return 0; fi
  done
  log "LANDSCAPE: NOT achieved with any method ($(grep -oE 'hierarchy rotation="[0-9]"' $OUT/ui.xml); user_rotation=$(a settings get system user_rotation | tr -d '\r'); $(a dumpsys window displays 2>/dev/null | tr -d '\r' | grep -m2 -oE 'mRotation=[0-9]|mUserRotation=[A-Z_0-9]+' | tr '\n' ' '))"
  return 1
}
land_off() {
  emu sensor set acceleration 0:9.81:0 >/dev/null; a settings put system accelerometer_rotation 0; a settings put system user_rotation 0; a wm user-rotation lock 0 >/dev/null 2>&1; sleep 2
}
year_slides_walk() {  # $1 — етикет: всеки слайд (докосване вдясно → следващ), снимка, ys, превъртане вътре
  local i
  year_open_fresh 25 || { log "YEAR $1: story did not open"; return 1; }
  for i in 1 2 3 4 5 6 7 8 9; do
    sleep 1.2; dump; shot year/$1-slide-$i >/dev/null; cp $OUT/ui.xml $OUT/year-$1-$i.xml 2>/dev/null
    log "YEAR $1 slide $i: $(python3 $WF ys $OUT/ui.xml $H | cut -c1-260)"
    a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*45/100)) 600; sleep 1; shot year/$1-slide-$i-scrolled >/dev/null
    log "  slide $i after a slow drag up inside: diff $(python3 $W19 rdiff $OUT/shots/year/$1-slide-$i.png $OUT/shots/year/$1-slide-$i-scrolled.png "0 0 $W $H" 2>&1 | tail -1)"
    [ "$(python3 $WF state $OUT/shots/year/$1-slide-$i-scrolled.png | cut -d' ' -f1)" = open ] || { log "  story closed at slide $i"; return 0; }
    a input tap $((W*88/100)) $((H/2)); sleep 0.6
  done
  [ "$(ystate)" = open ] && back 2
}
part_year34b() {
  local i
  helper_install
  dev_install
  anims 1
  warmup
  demo_signin_remember
  sec "Y1. Year story, new (b06d1ce devDebug): tap → raw screencaps (~0.2 s each), fresh process ×2 and reopen ×2"
  for i in 1 2; do
    a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 5; dismiss; ready; tab my; sleep 3; top_of
    year_tap_raw fresh-$i; sleep 2; [ "$(ystate)" = open ] && back 2
    ready; tab my; sleep 2; top_of; year_tap_raw reopen-$i; sleep 2; [ "$(ystate)" = open ] && back 2
  done
  sec "Y2. Swipe-down dismiss, new: every swipe from the first slide of a freshly opened story, twice; open/closed by screenshot"
  year_swipes2 new 2
  sec "Y3. Landscape + font 1.3 + „Много голям“, new: every slide"
  set_text_size '=Много голям' >/dev/null; back 1.5; back 1.5; a settings put system font_scale 1.3; sleep 2
  if land_on; then year_slides_walk land13; fi
  land_off
  sec "Y4. Portrait + font 1.3 + „Много голям“, new: every slide"
  year_slides_walk port13
  a settings put system font_scale 1.0; sleep 1; set_text_size '=Стандартен' >/dev/null; back 1.5; back 1.5
  sec "Y5. The same swipes on 1.2.33 devDebug (a830b51), for comparison (the story takes ~17 s to load there)"
  a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1
  timeout 180 adb install -r app-dev-base.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  log "installed: $(cat apk-dev-base-ref.txt 2>/dev/null); $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  warmup; demo_signin_remember
  YWAIT=40 year_swipes2 base 1
  diag_end
}
part_shake34b() {
  anims 1
  warmup
  sec "SK1. Default setting: 3 peaks + stillness, ×4 (positive control)"
  local i; for i in 1 2 3 4; do shake_round def-$i shake3; sleep 1; done
  sec "SK2. Settings › Изглед: off; shake ×3 must do nothing"
  restart
  if open_settings && settings_find "=$L_SHAKE"; then tap text "=$L_SHAKE" >/dev/null; sleep 1.5; dump; shot shake/b-setting-off; log "setting now: $(python3 $WF checkstate $OUT/ui.xml "$L_SHAKE")"; fi
  restart
  for i in 1 2 3; do shake_round off-$i shake3; sleep 1; done
  sec "SK3. Setting back on (after an app restart); shake ×2 opens again"
  restart
  if open_settings && settings_find "=$L_SHAKE"; then tap text "=$L_SHAKE" >/dev/null; sleep 1.5; dump; log "setting now: $(python3 $WF checkstate $OUT/ui.xml "$L_SHAKE")"; fi
  restart
  for i in 1 2; do shake_round on-$i shake3; sleep 1; done
  diag_end
}
part_idlecat() {
  anims 1
  warmup
  sec "IC. Catalogue at rest without a focused search field: 30 s after opening, then 15 s"
  a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 3; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 60
  idle_at catalog-plain; log "  focused field: $(grep -c 'focused="true"' $OUT/ui.xml) node(s) focused; IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)')"
  tap content-desc "$L_SHELF" >/dev/null && { idle_at catalog-shelf-plain; tap content-desc "$L_LIST" >/dev/null; sleep 1; }
  search_catalog 12 >/dev/null; hide_ime
  idle_at catalog-search-typed; log "  focused field: $(grep -c 'focused="true"' $OUT/ui.xml) node(s) focused; IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)')"
  diag_end
}

part_final() {
  case "$FINAL_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    ab34) part_ab34;;
    gfx34) part_gfx34;;
    idle34) part_idle34;;
    mem34|mem34b) part_mem34;;
    monkey34) part_monkey34;;
    demo34) part_demo34;;
    dev34) part_dev34;;
    misc34) part_misc34;;
    upd34) part_upd34;;
    old28) part_old28f;;
    year34b) part_year34b;;
    shake34b) part_shake34b;;
    idlecat) part_idlecat;;
  esac
  return 0
}
