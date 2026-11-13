# Шейдърите на шевицата и фолиото (ВРЕМЕННО, debug/w21-shader; резултати в emulator-results/v135).
# Тестовото v1.2.35 (9295534, e602e63 — ShaderPool, ShaderWarmup, първият кадър с плочката; versionCode 1504706)
# срещу издаденото v1.2.34 (3812d8e, 1504705); и двете prodRelease x86_64 с временен ключ.
# Зарежда се от run.sh след w20final.sh и ползва функциите на по-старите части.
#   start35 — студен старт A/B, n=20 на издание: 8 блока по 5 (base new new base base new new base), всеки
#             блок — нова инсталация; TotalTime на `am start -W` и редът „Fully drawn“
#   card35  — първото отваряне на картата на цял екран след студен старт: картата се добавя на ръка
#             (Моето › „Добави читателска карта“), после ×5 на блок: студен старт, 5 s (ShaderWarmup), Моето ›
#             „Читателска карта“ › „Покажи картата“; framestats (reset точно преди докосването); и без чакане
#             (~1 s след първия кадър) и с изтрит кеш на шейдърите на HWUI; 4 блока (new base base new)
#   reg35   — регресии на 1.2.35: лентата на „Начало“ (0.5 s / 3 s / 70 s / 75 s), картата в покой (и на
#             цял екран), monkey 2000, дневникът на грешките; лентата и на 1.2.34 за сравнение
#   old28s  — API 28 (без AGSL): „Начало“ и картата (плочката), картата на цял екран, monkey 2000, без срив
#   now35   — кръг 2: без чакане (студен старт направо в картата, „Покажи картата“ 0.5/1.0 s след първия кадър)
#   full28  — кръг 2: API 28, картата на цял екран (кръг 1 не стигна до нея)
#   start35b — кръг 3: студеният старт отново, с блок за загряване на емулатора (не се брои) и обратен ред
W21_PARTS=" start35 card35 reg35 old28s now35 full28 start35b "
W20_PARTS="$W20_PARTS $W21_PARTS"
W21=$H_/w21.py
L_ADDCARD='=Добави читателска карта|=Add library card'
L_MYCARD='=Читателска карта|=Library card'
RX_SHADER_DIAG='shevitsa-shader|foil-shader'

shader_diag() {  # $1 — етикет: следите от шейдърите в дневника на грешките и в logcat
  local f=/data/data/$PKG/no_backup/diagnostics/log.jsonl n1 n2
  a "cat $f 2>/dev/null" > $OUT/diag-$1.jsonl 2>/dev/null
  n1=$(grep -cE "$RX_SHADER_DIAG" $OUT/diag-$1.jsonl 2>/dev/null)
  n2=$(grep -E "$RX_SHADER_DIAG" $OUT/logcat-live.txt 2>/dev/null | grep -ciE 'error|exception|fail')
  log "SHADER DIAG $1: diagnostics log entries with shevitsa-shader/foil-shader: ${n1:-0} (of $(wc -l < $OUT/diag-$1.jsonl 2>/dev/null || echo 0)); logcat error lines naming them: ${n2:-0}"
  [ "${n1:-0}" != 0 ] && grep -E "$RX_SHADER_DIAG" $OUT/diag-$1.jsonl | head -3 | cut -c1-300 | tee -a $SUM
  return 0
}
gl_cache() {  # $1 — ls | rm: кешът на шейдърите на HWUI (code_cache/com.android.*shaders_cache)
  case $1 in
    ls) a "ls -l /data/data/$PKG/code_cache/ 2>/dev/null" | tr -d '\r' | grep -iE 'shader|cache' | awk '{print $5, $NF}' | tr '\n' ';';;
    rm) a "rm -f /data/data/$PKG/code_cache/com.android.*shaders_cache* /data/user_de/0/$PKG/code_cache/com.android.*shaders_cache* 2>/dev/null";;
  esac
}

# ---------------------------------------------------------------- 1. СТУДЕН СТАРТ A/B
s_block() {  # $1 — new | base, $2 — блок
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
part_start35() {
  local b=0 K order="base new new base base new new base"
  anims 1
  if [ $PART = start35b ]; then
    # В кръг 1 първият блок (base-1) беше бавен за всички: емулаторът още загрява. Сега той не се брои.
    order="new base base new new base base new"
    sec "S0. Emulator warm-up block (not counted): both apps, 2 installs × 5 cold starts"
    for K in base new; do inst $(apk_of $K) warm-$K $(ref_of $K) >/dev/null; launch >/dev/null; sleep 6; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; for b in 1 2 3 4 5; do cold_drawn warm-$K >/dev/null; done; done
    b=0
  fi
  log "block order (each a fresh install): $order"
  for K in $order; do b=$((b + 1)); s_block $K $b; done
  sec "S totals (n=20 per app)"
  for K in new base; do
    python3 $H_/parse.py starts $OUT/drawn-$K.txt "cold $K" | tee -a $SUM
    drawn_sum $K
  done
  python3 $W21 ab $OUT/drawn-new.txt $OUT/drawn-base.txt | tee -a $SUM
  shader_diag start35
  diag_end
}

# ---------------------------------------------------------------- 2. ПЪРВОТО ОТВАРЯНЕ НА КАРТАТА НА ЦЯЛ ЕКРАН
card_add_my() {  # на ръка: Моето › „Добави читателска карта“ (или плочката „Читателска карта“) → номер → „Запази“
  local p i
  for i in 1 2; do
    leave; ready; tab my; sleep 3; dump
    p=$(xy text "$L_ADDCARD"); [ -z "$p" ] && p=$(xy text "$L_MYCARD")
    [ -n "$p" ] || { log "ADD: no add-card button or card tile in My: $(texts 12 | cut -c1-260)"; debug_ui my-add; return 1; }
    a input tap $p; sleep 3; dump
    card_shown && { log "ADD: the card is already there"; return 0; }
    p=$(xy --all class EditText | sed -n 1p)
    [ -n "$p" ] || { log "ADD try $i: no number field: $(texts 10 | cut -c1-260)"; debug_ui add-field; continue; }
    a input tap $p; sleep 1.2; a input text "$CARD_NO"; sleep 1; hide_ime
    tap text '=Запази|=Save' >/dev/null; sleep 3; dump
    card_shown && { log "ADD by hand: $(texts 6 | cut -c1-200)"; return 0; }
    log "ADD try $i: not shown after saving: $(texts 10 | cut -c1-260)"
  done
  return 1
}
card_coords() {  # MY_XY, CARD_XY, SHOW_XY — местата на „Моето“, „Читателска карта“ и „Покажи картата“
  MY_XY=""; CARD_XY=""; SHOW_XY=""
  leave; ready; dump; MY_XY=$(xy --tab "$(tablabel my)" '=More|=Още')
  a input tap $MY_XY; sleep 3; dump; CARD_XY=$(xy text "$L_MYCARD")
  [ -n "$CARD_XY" ] && { a input tap $CARD_XY; sleep 4; dump; SHOW_XY=$(xy text "$L_CARDSHOW"); }
  log "COORDS: My $MY_XY, card tile $CARD_XY, show-card $SHOW_XY"
  [ -n "$SHOW_XY" ] && { a input tap $SHOW_XY; sleep 3; fullscreen_ok && log "COORDS: fullscreen card reached" || log "COORDS: fullscreen NOT confirmed: $(texts 8 | cut -c1-200)"; }
  leave
  [ -n "$MY_XY" ] && [ -n "$CARD_XY" ] && [ -n "$SHOW_XY" ]
}
fullscreen_ok() { dump && has 'content-desc="(Затвори|Close)"' && has 'Баркод на карта|Card barcode' && ! has "text=\"(Покажи картата|Show card)\""; }
fs_trial() {  # $1 — етикет (new-1-3-wait), $2 — пауза след първия кадър (s): wait 5 | now 0; $3 — cold-gl: изтрит кеш на шейдърите
  local L=$1 w=$2 t0 tm tc ts ok r
  a am force-stop $PKG; sleep 2
  [ "${3:-}" = cold-gl ] && gl_cache rm
  sleep 1
  r=$(launch | grep -oE 'TotalTime: [0-9]+'); t0=$(now_ms)
  if [ "$w" = 0 ]; then
    a input tap $MY_XY; tm=$(( $(now_ms) - t0 )); sleep 0.35
    a input tap $CARD_XY; tc=$(( $(now_ms) - t0 )); sleep 0.45
    gfx_reset; a input tap $SHOW_XY; ts=$(( $(now_ms) - t0 ))
  else
    while [ $(( $(now_ms) - t0 )) -lt $((w * 1000)) ]; do sleep 0.2; done
    a input tap $MY_XY; tm=$(( $(now_ms) - t0 )); sleep 2
    gfx_reset; a input tap $CARD_XY; tc=$(( $(now_ms) - t0 )); sleep 2.5
    a dumpsys gfxinfo $PKG framestats > $OUT/fs/$L-card.txt
    gfx_reset; a input tap $SHOW_XY; ts=$(( $(now_ms) - t0 ))
  fi
  sleep 2
  a dumpsys gfxinfo $PKG framestats > $OUT/fs/$L-full.txt
  ok=$(fullscreen_ok && echo yes || echo NO)
  [ "$ok" = NO ] && debug_ui fs-$L
  case $L in *-1-wait|*-now|*-coldgl) eshot fs-$L >/dev/null;; esac
  log "TRIAL $L: start $r; taps after the first frame: My +${tm} ms, card +${tc} ms, show +${ts} ms; fullscreen reached: $ok"
  [ -s $OUT/fs/$L-card.txt ] && python3 $W21 fs $OUT/fs/$L-card.txt $L-cardscreen | tee -a $SUM
  python3 $W21 fs $OUT/fs/$L-full.txt $L-full | tee -a $SUM
}
c_block() {  # $1 — new | base, $2 — блок
  local K=$1 b=$2 i
  sec "C $K-$b: fresh install, card added by hand, then 5× (cold start, 5 s, Моето › Читателска карта › Покажи картата) + no-wait + cleared GL cache"
  inst $(apk_of $K) $K-$b $(ref_of $K) || return
  a input keyevent KEYCODE_HOME; sleep 2
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab home; sleep 3
  card_add_my || { log "$K-$b: card NOT added — block skipped"; return; }
  card_coords || { log "$K-$b: coordinates not found — block skipped"; return; }
  log "$K-$b GL shader cache after the first card: $(gl_cache ls)"
  for i in 1 2 3 4 5; do fs_trial $K-$b-$i-wait 5; done
  fs_trial $K-$b-now 0
  fs_trial $K-$b-coldgl 5 cold-gl
  fs_trial $K-$b-now-coldgl 0 cold-gl
  log "$K-$b GL shader cache at the end: $(gl_cache ls)"
}
part_card35() {
  local b=0 K
  anims 1
  mkdir -p $OUT/fs
  log "block order: new base base new (each a fresh install; the card added by hand in each)"
  for K in new base base new; do b=$((b + 1)); c_block $K $b; done
  sec "C totals: per build and mode — the slowest frame of each open, the first 5 frames (ms, IntendedVsync → FrameCompleted)"
  python3 $W21 fsagg $SUM | tee $OUT/fs-agg.txt | tee -a $SUM
  shader_diag card35
  diag_end
}

# ---------------------------------------------------------------- 3. РЕГРЕСИИ
ribbon35() {  # $1 — етикет: студен старт; „Начало“ 0.5 s и 3 s след първия кадър (тече ли?), 70/75 s (спряла ли е?)
  local K=$1 t0
  a am force-stop $PKG; sleep 3
  launch >/dev/null; t0=$(now_ms)
  shot_t rib/$K-00.5s 500; shot_t rib/$K-03s 3000; shot_t rib/$K-06s 6000
  sleep 14; dump; ribbon_box; cp $OUT/ui.xml $OUT/rib-$K.xml 2>/dev/null
  shot_t rib/$K-70s 70000; shot_t rib/$K-75s 75000
  cpu_win home-$K-76-91s 15
  if [ -n "$RBOX" ]; then
    log "RIBBON $K [$RBOX]: diff 0.5→3 s $(python3 $W19 rdiff $OUT/shots/rib/$K-00.5s.png $OUT/shots/rib/$K-03s.png "$RBOX"); 3→6 s $(python3 $W19 rdiff $OUT/shots/rib/$K-03s.png $OUT/shots/rib/$K-06s.png "$RBOX"); 70→75 s $(python3 $W19 rdiff $OUT/shots/rib/$K-70s.png $OUT/shots/rib/$K-75s.png "$RBOX") (0 = still); pattern at 0.5 s: $(python3 $W19 rstat $OUT/shots/rib/$K-00.5s.png "$RBOX"); at 75 s: $(python3 $W19 rstat $OUT/shots/rib/$K-75s.png "$RBOX")"
    python3 $W20 strip $OUT/shots/rib/$K-strip.png "$RBOX" $OUT/shots/rib/$K-{00.5s,03s,06s,70s,75s}.png >/dev/null 2>&1
  else log "RIBBON $K: ribbon box not found on Home: $(texts 8 | cut -c1-200)"; debug_ui rib-$K; fi
}
part_reg35() {
  anims 1
  warmup
  sec "R0. Card added by hand (Моето › Добави читателска карта)"
  card_add_my || log "card not added"
  leave
  sec "R1. Home ribbon, 1.2.35: animates right after start, stops later"
  ribbon35 new-a; ribbon35 new-b
  sec "R2. Card screen and fullscreen card at rest: 0-15 s, 30-45 s, 45-60 s after opening"
  restart; ready; tab my; sleep 2; tap text "$L_MYCARD" >/dev/null; sleep 1
  card_idle card
  eshot card-rest >/dev/null
  dump; tap text "$L_CARDSHOW" >/dev/null; sleep 1
  card_idle card-full
  eshot card-full-rest >/dev/null
  leave
  sec "R3. Monkey 2000 events (seed 1357)"
  MONKEY_N=2000 part_monkey
  shader_diag reg35
  diag_end
  sec "R4. Home ribbon on 1.2.34 for comparison"
  inst app-base.apk base apk-base-ref.txt
  launch >/dev/null; sleep 6; dismiss; ready; tab home; sleep 3
  ribbon35 base-a
}

# ---------------------------------------------------------------- 4. API 28
part_old28s() {
  local t0
  helper_install; dev_locale bg-BG || true; a am force-stop $PKG
  anims 1
  warmup
  sec "O1. Home ribbon on API $SDK (no AGSL): the still tile"
  a am force-stop $PKG; sleep 3; launch >/dev/null; t0=$(now_ms)
  shot_t old28/home-01s 1000; shot_t old28/home-05s 5000; shot_t old28/home-20s 20000
  dump; ribbon_box
  [ -n "$RBOX" ] && log "HOME28 ribbon [$RBOX]: pattern $(python3 $W19 rstat $OUT/shots/old28/home-01s.png "$RBOX"); change 1→5→20 s: $(python3 $W19 seq "$RBOX" $OUT/shots/old28/home-{01s,05s,20s}.png)" || log "HOME28: ribbon box not found: $(texts 8 | cut -c1-200)"
  cpu_win home28-20-35s 15
  sec "O2. Card added (API 28), card screen and fullscreen card: the tile, no crash"
  card_add28 || log "CARD28: not added"
  leave; ready; tab my; sleep 2; tap text "$L_MYCARD" >/dev/null; sleep 3
  eshot card28 >/dev/null; dump; log "CARD28 screen: $(texts 8 | cut -c1-220)"
  cpu_win card28-3-18s 15
  dump; tap text "$L_CARDSHOW" >/dev/null || { [ -n "${SHOW_XY:-}" ] && a input tap $SHOW_XY; }
  sleep 3; eshot card28-full >/dev/null; dump; log "CARD28 fullscreen: $(texts 6 | cut -c1-200); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
  sleep 27; cpu_win cardfull28-30-45s 15
  leave
  log "FATAL so far: $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
  sec "O3. Monkey 2000 (seed 1357)"
  MONKEY_N=2000 part_monkey
  shader_diag old28s
  diag_end
}

# ---------------------------------------------------------------- КРЪГ 2
# Без чакане: кръг 1 не стигна до картата (докосванията по „Моето“ още при зареждането на „Начало“ отвориха
# статия). Сега студеният старт е направо в екрана на картата (прекия път chyavorec://app/my/card), а
# „Покажи картата“ се докосва 0.5 s / 1.0 s след първия кадър — преди ShaderWarmup (2 s след старта).
now_trial() {  # $1 — етикет, $2 — пауза (ms) след първия кадър, $3 — cold-gl
  local L=$1 r t0 ts ok
  a am force-stop $PKG; sleep 2
  [ "${3:-}" = cold-gl ] && gl_cache rm
  sleep 1
  r=$(a am start -W -a org.chyavorec.app.SHORTCUT -d chyavorec://app/my/card -n $ACT 2>&1 | grep -oE 'TotalTime: [0-9]+'); t0=$(now_ms)
  while [ $(( $(now_ms) - t0 )) -lt $2 ]; do sleep 0.05; done
  gfx_reset; a input tap $SHOW_XY; ts=$(( $(now_ms) - t0 ))
  sleep 2
  a dumpsys gfxinfo $PKG framestats > $OUT/fs/$L-full.txt
  ok=$(fullscreen_ok && echo yes || echo NO)
  [ "$ok" = NO ] && { debug_ui now-$L; eshot now-$L >/dev/null; }
  log "TRIAL $L: start (card shortcut) $r; show tapped +${ts} ms after the first frame; fullscreen reached: $ok"
  [ "$ok" = yes ] && python3 $W21 fs $OUT/fs/$L-full.txt $L-full | tee -a $SUM
}
n_block() {  # $1 — new | base, $2 — блок
  local K=$1 b=$2
  sec "N $K-$b: fresh install, card added by hand; cold start straight into the card, „Покажи картата“ 0.5 s / 1.0 s after the first frame"
  inst $(apk_of $K) $K-$b $(ref_of $K) || return
  a input keyevent KEYCODE_HOME; sleep 2
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 >/dev/null; tab home; sleep 3
  card_add_my || { log "$K-$b: card NOT added — block skipped"; return; }
  card_coords || { log "$K-$b: coordinates not found — block skipped"; return; }
  now_trial $K-$b-1-now05 500; now_trial $K-$b-2-now10 1000; now_trial $K-$b-3-now05 500; now_trial $K-$b-4-now10 1000
  now_trial $K-$b-5-now05-coldgl 500 cold-gl; now_trial $K-$b-6-now10-coldgl 1000 cold-gl
  fs_trial $K-$b-7-wait 5
}
part_now35() {
  local b=0 K
  anims 1
  mkdir -p $OUT/fs
  log "block order: new base base new (each a fresh install; the card added by hand in each)"
  for K in new base base new; do b=$((b + 1)); n_block $K $b; done
  sec "N totals"
  python3 $W21 fsagg $SUM | tee $OUT/fs-agg.txt | tee -a $SUM
  shader_diag now35
}
# API 28: кръг 1 не отвори картата на цял екран (докосването по текста); сега — по мястото на бутона.
part_full28() {
  local i p
  anims 1
  warmup
  sec "F28. API $SDK: card added, then the fullscreen card ×3 (by the button's position), shots and frames at rest"
  card_add28 || log "CARD28: not added"
  leave; ready; tab my; sleep 2; tap text "$L_MYCARD" >/dev/null; sleep 3
  dump; p=$(xy text "$L_CARDSHOW"); [ -z "$p" ] && p="582 1068"
  log "show-card at $p"
  for i in 1 2 3; do
    a input tap $p; sleep 3; eshot full28-$i >/dev/null
    log "FULL28 $i: fullscreen $(fullscreen_ok && echo yes || echo NO); app in front $(has "package=\"$PKG\"" && echo yes || echo NO)"
    [ $i = 3 ] && { sleep 25; cpu_win full28-30-45s 15; }
    back 2; dump
  done
  restart; card_screen; dump; p=$(xy text "$L_CARDSHOW"); [ -n "$p" ] && { a input tap $p; sleep 3; eshot full28-after-restart >/dev/null; log "FULL28 after a restart: fullscreen $(fullscreen_ok && echo yes || echo NO)"; }
  leave
  log "FATAL: $(grep -c 'FATAL EXCEPTION' $OUT/logcat-live.txt)"
  shader_diag full28
}

part_w21() {
  case "$W21_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    now35) part_now35;;
    full28) part_full28;;
    start35|start35b) part_start35;;
    card35) part_card35;;
    reg35) part_reg35;;
    old28s) part_old28s;;
  esac
  return 0
}
