# Вълна 20, картата (ВРЕМЕННО, debug/wave20-card; резултати в emulator-results/v134-card).
# Новото (wave20/card) срещу 1.2.33 (a830b51). Зарежда се от run.sh след w20.sh и ползва функциите им.
#   card33 — API 33: картата и картата на цял екран без докосване (0–15 s, 30–45 s, 45–60 s): кадри, CPU,
#            сензорите; наклон след покоя (фолиото се мени ли?), покой след наклона; обръщане и покой след него
#   card28 — API 28: „Моето“ → картата → „Покажи картата“ (общите граници) ×5 — gfxinfo и GC в logcat,
#            1.2.33 и новото поред (два кръга)
CARD_PARTS=" card33 card28 "
W20_PARTS="$W20_PARTS card33 card28 "

sens_app() {  # $1 — етикет: връзките на приложението в sensorservice
  a dumpsys sensorservice 2>/dev/null | tr -d '\r' > $OUT/sens-$1.txt
  log "SENSORS $1: lines with the app $(grep -c "$PKG" $OUT/sens-$1.txt): $(grep -E "$PKG" -A4 $OUT/sens-$1.txt | grep -iE "$PKG|rotation|accelerometer|0x[0-9a-f]+ " | tr -s ' ' | head -8 | cut -c1-150 | tr '\n' '~')"
}
card_idle() {  # $1 — етикет: без докосване — веднага, 30–45 s и 45–60 s след отварянето
  local t0; t0=$(now_ms)
  cpu_win $1-0-15s 15
  sens_app $1-15s
  while [ $(( $(now_ms) - t0 )) -lt 30000 ]; do sleep 0.5; done
  cpu_win $1-30-45s 15
  cpu_win $1-45-60s 15
  sens_app $1-60s
}
card_tilt() {  # $1 — етикет: наклон през емулатора след покоя — събужда ли се сензорът, мени ли се фолиото?
  local E=$OUT/shots/eshot f
  eshot $1-rest >/dev/null; sleep 1.5; eshot $1-rest-b >/dev/null
  emu sensor set acceleration 4.5:8.7:0 >/dev/null; sleep 2.5; eshot $1-acc-A >/dev/null
  sens_app $1-woken
  emu sensor set acceleration -4.5:8.7:0 >/dev/null; sleep 2.5; eshot $1-acc-B >/dev/null
  emu sensor set acceleration 0:9.81:0 >/dev/null; sleep 2.5; eshot $1-back >/dev/null
  mkdir -p $OUT/shots/tilt; for f in rest rest-b acc-A acc-B back; do [ -f $E/$1-$f.png ] && python3 -c "from PIL import Image; import sys; Image.open(sys.argv[1]).convert('RGB').resize(($W,$H)).save(sys.argv[2])" $E/$1-$f.png $OUT/shots/tilt/$1-$f.png 2>/dev/null; done
  local R="0 250 $W 1250"
  log "TILT $1: noise rest→rest-b $(python3 $W19 rdiff $OUT/shots/tilt/$1-rest.png $OUT/shots/tilt/$1-rest-b.png "$R" 2>&1 | tail -1); rest→accA $(python3 $W19 rdiff $OUT/shots/tilt/$1-rest.png $OUT/shots/tilt/$1-acc-A.png "$R" 2>&1 | tail -1); accA→accB $(python3 $W19 rdiff $OUT/shots/tilt/$1-acc-A.png $OUT/shots/tilt/$1-acc-B.png "$R" 2>&1 | tail -1); accB→back $(python3 $W19 rdiff $OUT/shots/tilt/$1-acc-B.png $OUT/shots/tilt/$1-back.png "$R" 2>&1 | tail -1)"
  python3 $W20 strip $OUT/shots/tilt/$1-strip.png "$R" $OUT/shots/tilt/$1-{rest,acc-A,acc-B,back}.png >/dev/null
  rm -f $E/$1-*.png
  # След наклона — пак покой: бързият сензор заспива след ~15 s.
  sleep 20; cpu_win $1-after-tilt-20-35s 15; sens_app $1-after-tilt
}
part_card33() {
  anims 1
  warmup
  sensors_info
  sec "C1. Card screen: no input (frames, CPU, sensors)"
  card_open >/dev/null || log "CARD: not shown: $(texts 10 | cut -c1-200)"
  card_idle card
  sec "C2. Card screen: tilt after the rest"
  card_tilt card
  sec "C3. Fullscreen card: no input"
  dump; tap text "$L_CARDSHOW" >/dev/null; sleep 1
  card_idle card-full
  sec "C4. Fullscreen card: tilt after the rest"
  card_tilt card-full
  sec "C5. Fullscreen card: flip (tap) and rest after it"
  dump; tap content-desc 'Баркод на карта|Card barcode' >/dev/null; sleep 2; eshot card-full-flipped >/dev/null
  cpu_win card-full-flip-2-17s 15
  sleep 5; cpu_win card-full-flip-22-37s 15; sens_app card-full-flip
  dump; tap text '=TEST-12345' >/dev/null; sleep 2
  sec "C6. Power saver switched on 3 s after opening the card (the weave stops at once?)"
  leave; card_screen; sleep 3
  a dumpsys battery unplug; log "power saver: $(a cmd power set-mode 1 2>&1 | tr -d '\r' | head -1) → $(a settings get global low_power | tr -d '\r')"
  sleep 1; cpu_win card-powersave-4-19s 15
  a cmd power set-mode 0 >/dev/null 2>&1; a dumpsys battery reset
  leave
  diag_end
}
c28_round() {  # $1 — new | base, $2 — кръг
  local K=$1 i pid gc p
  ready; tab my; sleep 3; dump
  pid=$(pidof_app); adb logcat -c
  gfx_reset
  for i in 1 2 3 4 5; do
    ready; tab my; sleep 2; dump
    p=$(xy text '=Читателска карта|=Library card'); [ -n "$p" ] && a input tap $p || card_screen
    sleep 3; dump
    [ $i = 1 ] && eshot c28-$K-$2-card >/dev/null
    tap text "$L_CARDSHOW" >/dev/null; sleep 3
    [ $i = 1 ] && eshot c28-$K-$2-full >/dev/null
    back 2.5; back 2
  done
  gfx_save c28-$K-$2
  adb logcat -d -v threadtime > $OUT/logcat-c28-$K-$2.txt 2>/dev/null
  gc=$(grep -E " $pid +[0-9]+ " $OUT/logcat-c28-$K-$2.txt | grep -cE 'GC freed|concurrent copying GC|WaitForGcToComplete')
  log "C28 $K round $2: GC lines in logcat for pid $pid: $gc; $(grep -E " $pid +[0-9]+ " $OUT/logcat-c28-$K-$2.txt | grep -E 'GC freed' | tail -3 | cut -c1-200 | tr '\n' '~')"
}
card_add28() {  # картата на API 28: полето, после текстът (с повторение — клавиатурата идва бавно)
  local i p s
  for i in 1 2 3; do
    card_screen; dump; card_shown && return 0
    p=$(xy --all class EditText | sed -n 1p); s=$(xy text '=Запази|=Save')
    [ -n "$p" ] || { log "CARD28: no field: $(texts 8 | cut -c1-200)"; continue; }
    # С мигащ курсор uiautomator на API 28 не дочаква покой — „Запази“ се натиска по мястото отпреди.
    a input tap $p; sleep 2.5; a input text "$CARD_NO"; sleep 1.5; eshot c28-typed-$i >/dev/null
    hide_ime; sleep 1.5; [ -n "$s" ] && a input tap $s; sleep 3; dump
    log "CARD28 try $i (field $p, save $s): $(texts 12 | cut -c1-260)"
    card_shown && { log "CARD28: card shown"; return 0; }
    back 1.5
  done
  log "CARD28: NOT added"; return 1
}
part_card28() {
  local K r
  for r in 1 2; do
    for K in base new; do
      sec "C28-$K-$r. API $SDK: Моето → картата → „Покажи картата“ ×5 (gfxinfo, GC)"
      inst $(apk_of $K) $K $(ref_of $K) || continue
      anims 1
      launch >/dev/null; sleep 6; dismiss; ready
      card_add28 || { shot c28/$K-$r-no-card; continue; }
      leave
      c28_round $K $r
    done
  done
  sec "C28-sum. Totals"
  grep -E '^GFX ' $SUM | tee -a $OUT/gfx-totals.txt
  diag_end
}
part_card() {
  case "$CARD_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    card33) part_card33;;
    card28) part_card28;;
  esac
  return 0
}
