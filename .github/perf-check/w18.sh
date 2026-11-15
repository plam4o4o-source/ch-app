# Частите на вълна 18 (ВРЕМЕННО, debug/wave18-check): тестовото v1.2.32 срещу v1.2.31.
# Зарежда се от run.sh след w15.sh и w16.sh и ползва помощните им функции.
#   hwprobe    — HARDWARE bitmap-ите на емулатора (помощното приложение) и къде е паметта им; двете издания
#   jank18-1/2 — плавност: въртележката, „Новини“, галерията (албумите, мрежата), каталогът; ~10 бързи
#                плъзгания на сценарий, gfxinfo reset между тях; „Бързодействие“ в „Диагностика“ (1.2.32)
#   drawn      — „Fully drawn“ при студен старт на 1.2.32: онлайн, офлайн, празен кеш (онлайн/офлайн), ново
#   zoom18     — прегледът: разтваряне извън центъра (около пръстите?), двойно докосване, местене, затваряне
#   monkey18   — monkey 3000 събития (seed 1357), 1.2.32
W18=$H_/w18.py

fully_drawn() {  # $1 — маркер: редовете „Fully drawn“ след него (до ~12 s)
  local i l
  for i in $(seq 1 24); do l=$(since "$1" "Fully drawn $PKG" | tail -1); [ -n "$l" ] && break; sleep 0.5; done
  echo "$l" | grep -oE 'Fully drawn [^ ]+ \+[0-9sm]+'
}
cold_drawn() {  # $1 — етикет: студен старт, TotalTime и „Fully drawn“ → $OUT/drawn-$1.txt
  local m=fd-$1-$RANDOM r d
  a am force-stop $PKG; sleep 3; mark $m
  r=$(launch | tr '\n' ' ')
  d=$(fully_drawn $m)
  echo "$r | ${d:-NO FULLY DRAWN}" | tee -a $OUT/drawn-$1.txt
  echo "$d" | grep -q . && echo "$d" >> $OUT/drawn-$1-lines.txt
  sleep 3
}
drawn_sum() { log "DRAWN $1: TotalTime $(python3 $H_/parse.py starts $OUT/drawn-$1.txt x | grep -oE 'n=.*max=[0-9]+ms'); fully drawn $( [ -f $OUT/drawn-$1-lines.txt ] && python3 $W18 drawn $OUT/drawn-$1-lines.txt || echo 'n=0 (no Fully drawn line)'); without line: $(grep -c 'NO FULLY DRAWN' $OUT/drawn-$1.txt)"; }
clear_public_cache() {
  open_settings && settings_item '=Изчисти временните данни|=Clear temporary data' || return 1
  sleep 1.5; dump; tap text '=Изчисти|=Clear' --last; sleep 3; dump
  log "cache cleared: snackbar '$(texts 40 | grep -oE 'text="[^"]*(изтрит|изчист|cleared)[^"]*"' | head -1)'; app cache dir: $(a "du -sk /data/data/$PKG/cache 2>/dev/null" | tr -d '\r' | cut -f1) KB"
}

# ================================================================= C2. FULLY DRAWN (1.2.32)
part_drawn() {
  local i
  sec "F0. Fresh install, first launch (onboarding: ReportDrawn)"
  a input keyevent KEYCODE_HOME; sleep 2
  mark fd-fresh; launch | tr '\n' ' '; echo
  log "DRAWN fresh-install (onboarding): $(fully_drawn fd-fresh)"; sleep 3; shot drawn/00-first-launch
  dismiss; ready; dims; tab catalog; waitfor "$RX_RESULTS" 150 && log "catalogue results after ${WAITED} ms"
  tab news; sleep 5; tab home; sleep 5
  sec "F1. Cold starts online ×6 (Home: ReportDrawnWhen)"
  for i in $(seq 1 6); do cold_drawn online; done; drawn_sum online; shot drawn/01-online
  sec "F2. Cold starts offline ×6 (cached news)"
  offline; for i in $(seq 1 6); do cold_drawn offline; done; drawn_sum offline; shot drawn/02-offline; online; sleep 5
  sec "F3. Empty cache (Settings › Изчисти временните данни), then a cold start online ×4"
  for i in 1 2 3 4; do clear_public_cache; cold_drawn empty-online; done; drawn_sum empty-online; shot drawn/03-empty-online
  sec "F4. Empty cache, then a cold start offline ×4 (nothing cached: the error/offline state)"
  for i in 1 2 3 4; do online; sleep 3; restart >/dev/null; clear_public_cache; offline; cold_drawn empty-offline; done
  drawn_sum empty-offline; shot drawn/04-empty-offline; dump; log "empty+offline Home: $(texts 14 | cut -c1-240)"
  online; sleep 3
  sec "F5. After pm clear (onboarding again) ×2"
  for i in 1 2; do a pm clear $PKG >/dev/null; a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS; cold_drawn cleared; done
  drawn_sum cleared
  sec "F6. All 'Fully drawn' lines of the app in logcat"
  grep -E "Fully drawn $PKG" $OUT/logcat-live.txt | sed -E 's/^.*(Fully drawn)/\1/' | sort | uniq -c | sort -rn | head -5 | tee -a $SUM
}

# ================================================================= A'. HARDWARE BITMAPS ON THIS EMULATOR
probe_state() {  # $1 — етикет, $2 — пакет: meminfo, дескриптори, gfxinfo, dmabuf, MemAvailable
  local pid; pid=$(a pidof $2 | tr -d '\r' | cut -d' ' -f1)
  a dumpsys meminfo $2 > $OUT/hw/mem-$1.txt
  a dumpsys gfxinfo $2 > $OUT/hw/gfx-$1.txt
  a "ls -l /proc/$pid/fd 2>/dev/null" > $OUT/hw/fd-$1.txt
  a "dmabuf_dump $pid 2>&1" > $OUT/hw/dmabuf-$1.txt
  a "showmap $pid 2>&1" > $OUT/hw/showmap-$1.txt
  log "$(python3 $W18 memrow $OUT/hw/mem-$1.txt $1)"
  log "  $(python3 $W18 fds $OUT/hw/fd-$1.txt | cut -c1-400)"
  log "  $(python3 $W18 gfxmem $OUT/hw/gfx-$1.txt $1 | cut -c1-400)"
  log "  dmabuf_dump: $(grep -ciE 'size|inode' $OUT/hw/dmabuf-$1.txt) lines, $(grep -iE 'total|PROCESS' $OUT/hw/dmabuf-$1.txt | head -3 | tr -s ' ' | tr '\n' ';' | cut -c1-200); $(a grep -E 'MemAvailable' /proc/meminfo | tr -s ' ' | tr -d '\r')"
  log "  showmap (device / dmabuf / ashmem / gralloc mappings): $(grep -E '/dev/|dmabuf|ashmem|gralloc|kgsl|goldfish|gpu' $OUT/hw/showmap-$1.txt | awk '{print $NF, $2, $3}' | sort | head -8 | tr '\n' ';' | cut -c1-300)"
}
hw_probe_mode() {  # $1 — режим на пробата
  a am force-stop org.pc.helper; sleep 1
  mark hw-$1
  # n=0 — същият процес без bitmap-и (база за разликата)
  a am start -W -n org.pc.helper/.HwProbe --es mode $1 --ei n 0 >/dev/null 2>&1; sleep 3
  probe_state helper-$1-before org.pc.helper
  a am force-stop org.pc.helper; sleep 1
  a am start -W -n org.pc.helper/.HwProbe --es mode $1 --ei n 25 --ei size 1024 >/dev/null 2>&1; sleep 6
  log "PROBE $1: $(since hw-$1 'PCHW' | tail -1 | sed -E 's/.*PCHW *: //' | cut -c1-220)"
  probe_state helper-$1-after org.pc.helper
  shot hw/helper-$1
  a am force-stop org.pc.helper
}
thumb_tour() {  # экраните с миниатюри: „Начало“ (въртележката), „Новини“, галерията, каталогът
  local i b cy
  restart; tab home; sleep 4; dims; dump
  b=$(python3 $W15 big $OUT/ui.xml $PKG); read _ y0 _ y1 _ <<< "$b"; cy=$(( (${y0:-500} + ${y1:-900}) / 2 ))
  for i in 1 2 3; do a input swipe $((W*85/100)) $cy $((W*15/100)) $cy 150; sleep 1.5; done
  for i in 1 2 3; do sw_up 300; sleep 1; done
  link "$SITE/news"; sleep 5; for i in 1 2 3 4; do sw_up 300; sleep 1.2; done
  open_gallery && waitfor "$RX_GALLERY_LOADED" 40 && { for i in 1 2; do sw_up 300; sleep 1.2; done; for i in 1 2; do sw_down 300; sleep 0.8; done
    open_album 1 && wait_cells 20 && { for i in 1 2 3; do sw_up 300; sleep 1.5; done; }; back 1.5; back 1.5; }
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; for i in 1 2 3 4 5; do sw_up 300; sleep 1.2; done
  ready; tab home; sleep 4
}
part_hwprobe() {
  local K
  mkdir -p $OUT/hw
  helper_install
  sec "H1. Emulator graphics: renderer, gralloc, memtrack"
  log "GL: $(a dumpsys SurfaceFlinger 2>/dev/null | tr -d '\r' | grep -m2 -E 'GLES:|EGL' | tr '\n' ' ' | cut -c1-240)"
  log "props: $(a getprop | tr -d '\r' | grep -E 'ro.hardware.(gralloc|egl|vulkan)|ro.boot.qemu.gltransport|ro.opengles|debug.hwui.renderer|ro.hardware.hwcomposer|ro.kernel.qemu.gles|ro.boot.hardware.gralloc' | tr '\n' ' ' | cut -c1-500)"
  log "memtrack HAL: $(a 'lshal 2>/dev/null | grep -i memtrack; service list 2>/dev/null | grep -i memtrack' | tr -d '\r' | tr -s ' ' | tr '\n' ';' | cut -c1-200)"
  a dumpsys meminfo > $OUT/hw/meminfo-system.txt 2>&1
  log "system: $(grep -E 'Total RAM|Free RAM|Used RAM|Lost RAM|DMA-BUF|GPU' $OUT/hw/meminfo-system.txt | tr -s ' ' | tr '\n' ';' | cut -c1-400)"
  sec "H2. Helper probe: 25 bitmaps 1024×1024 each (4 MB as ARGB_8888, 2 MB as RGB_565) — config and where the memory shows"
  for m in sw8888 sw565 hw dec8888 dec565 dechw; do hw_probe_mode $m; done
  sec "H3. The app after the same tour of thumbnail screens (Home carousel, News, Gallery, Catalogue): v1.2.31 vs v1.2.32"
  for K in base new; do
    inst $(apk_of $K) $K $(ref_of $K) || continue
    launch >/dev/null; sleep 6; dismiss; ready; dims; tab catalog; waitfor "$RX_RESULTS" 150
    thumb_tour; sleep 3
    probe_state app-$K-raw $PKG
    a am send-trim-memory $PKG RUNNING_CRITICAL >/dev/null 2>&1; sleep 8
    probe_state app-$K-trim $PKG
    thumb_tour; sleep 3
    probe_state app-$K-raw2 $PKG
    shot hw/app-$K
  done
}

# ================================================================= B. JANK (+ D: „Бързодействие“)
fling_v() { local i; for i in 1 2 3 4 5 6; do sw_up 60; sleep 0.9; done; for i in 1 2 3 4; do sw_down 60; sleep 0.9; done; }
jank_scn() {  # $1 — сценарий, $2 — K: gfx reset, ~10 бързи плъзгания, gfxinfo, снимка
  gfx_reset; sleep 0.3
  case $1 in
    carousel) local i; for i in 1 2 3 4 5; do a input swipe $((W*85/100)) $JCY $((W*15/100)) $JCY 90; sleep 1.1; done
              for i in 1 2 3 4 5; do a input swipe $((W*15/100)) $JCY $((W*85/100)) $JCY 90; sleep 1.1; done;;
    *) fling_v;;
  esac
  sleep 1; gfx_save $2-$1; cp $OUT/gfx-$2-$1.txt $OUT/gfx-$2-$1-full.txt 2>/dev/null
  log "  $(python3 $W18 gfxmem $OUT/gfx-$2-$1.txt $2-$1 | cut -c1-300)"
  shot jank/$2-$1
}
perf_card() {  # $1 — етикет: „Настройки › Информация › Диагностика“, картата „Бързодействие“
  open_settings && settings_item '=Диагностика|=Diagnostics' || return 1
  sleep 2.5; dump; shot perf/$1-1
  log "PERF CARD $1: $(python3 $W18 perfcard $OUT/ui.xml | cut -c1-900)"
  cp $OUT/ui.xml $OUT/perf-$1.xml
  sw_up 400; sleep 1.5; dump; shot perf/$1-2
  log "PERF CARD $1 (scrolled): $(python3 $W18 perfcard $OUT/ui.xml | cut -c1-700)"
  sw_down 400; sw_down 400; sleep 1
}
jank18_app() {  # $1 — new | base
  local K=$1 i b
  sec "J-$K. Install $K, warm-up"
  inst $(apk_of $K) $K $(ref_of $K) || return
  launch >/dev/null; sleep 6; dismiss; ready; dims
  tab catalog; waitfor "$RX_RESULTS" 150 && log "$K first-install catalogue results after ${WAITED} ms"
  tab news; sleep 4; tab home; sleep 4
  if [ $K = new ]; then
    sec "D0. A few starts for the card: 4 cold + 3 warm"
    for i in 1 2 3 4; do a am force-stop $PKG; sleep 3; mark perf-cold-$i; launch | tr '\n' ' '; echo " | $(fully_drawn perf-cold-$i)"; sleep 4; done
    for i in 1 2 3; do a input keyevent KEYCODE_HOME; sleep 3; launch | tr '\n' ' '; echo; sleep 3; done
  fi
  sec "J-$K. Scroll scenarios (animations ON, ${W}x${H}): ~10 flings each, gfxinfo reset before each"
  anims 1
  restart; tab home; sleep 5; dump; b=$(python3 $W15 big $OUT/ui.xml $PKG); log "$K carousel card: $(echo "$b" | cut -c1-120)"
  read _ y0 _ y1 _ <<< "$b"; JCY=$(( (${y0:-500} + ${y1:-900}) / 2 ))
  jank_scn carousel $K
  link "$SITE/news"; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; sleep 2; log "$K news screen: $(show 6 | cut -c1-160)"
  jank_scn news $K
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 40; then
    sleep 2; jank_scn gallery $K
    for i in 1 2 3 4; do sw_down 100; sleep 0.4; done
    if open_album 1 && wait_cells 20; then sleep 2; log "$K album: photos on screen $NCELLS"; jank_scn album-grid $K; back 1.5; else log "$K album: photos not shown"; debug_ui album; fi
    back 1.5
  else log "$K gallery: albums not shown"; debug_ui gallery; fi
  link "$SITE/elektronen-katalog"; waitfor "$RX_RESULTS" 60; sleep 2; log "$K catalog: $(rcount) results"
  jank_scn catalog $K
  mem $K-after-scroll
  anims 0
  if [ $K = new ]; then
    sec "D. Diagnostics › „Бързодействие“ after the starts and the scroll scenarios"
    a input keyevent KEYCODE_HOME; sleep 3; launch >/dev/null; sleep 4; dismiss
    perf_card $K-filled
    sec "D2. „Изчисти“ clears it"
    dump; tap text '=Изчисти|=Clear'; sleep 2.5; dump; shot perf/$K-cleared
    log "PERF CARD after Изчисти: $(python3 $W18 perfcard $OUT/ui.xml | cut -c1-400); empty text $(has 'Още няма измервания|No measurements yet' && echo SHOWN || echo absent); snackbar $(has 'изчистен|cleared' && echo yes || echo no)"
    back 1.5; back 1.5
    a input keyevent KEYCODE_HOME; sleep 3; launch >/dev/null; sleep 4
    perf_card $K-after-clear-and-restart
  fi
}
part_jank18() {
  if [ "$PART" = jank18-2 ]; then jank18_app base; jank18_app new; else jank18_app new; jank18_app base; fi
  sec "J. Totals"
  grep -E '^GFX ' $SUM | sort -k2 | tee $OUT/gfx-totals.txt
}

# ================================================================= E. GALLERY VIEWER
zshot() { sleep ${2:-3}; shot zoom/$1; }
zf() { python3 $W18 zoomfix $OUT/shots/zoom/$1.png $OUT/shots/zoom/$2.png $3 $4 "$5" | tee -a $SUM; }
part_zoom18() {
  local K cx cy r
  for K in new base; do
    sec "Z-$K. Viewer: pinch at off-centre points (zoom around the fingers?), double tap, pan, drag to dismiss"
    inst $(apk_of $K) $K $(ref_of $K) || continue
    launch >/dev/null; sleep 6; dismiss; ready; dims
    restart; open_gallery && waitfor "$RX_GALLERY_LOADED" 40 && open_album 1 && wait_cells 20 && open_photo 1 || { log "$K: viewer not reached"; debug_ui viewer-$K; continue; }
    sleep 5; dims; mt_init || { log "no multi-touch"; return; }
    log "$K viewer: $(viewer_page) screen ${W}x${H}"
    r=0
    for pt in "$((W*28/100)) $((H/2))" "$((W*72/100)) $((H*44/100))" "$((W/2)) $((H/2))"; do
      r=$((r+1)); read cx cy <<< "$pt"
      zshot $K-p$r-0-1x 1
      pinch $cx $cy 240 480 16; zshot $K-p$r-1-pinched 3.5
      zf $K-p$r-0-1x $K-p$r-1-pinched $cx $cy "$K pinch #$r at ($cx,$cy)"
      dtap $((W/2)) $((H/2)); zshot $K-p$r-2-reset 2.5
      dd=$(python3 $W18 diff $OUT/shots/zoom/$K-p$r-0-1x.png $OUT/shots/zoom/$K-p$r-2-reset.png)
      log "  after double tap on the zoomed photo: diff vs 1x $dd (≈0 = back at 1x); $(viewer_page)"
      # Разтварянето не е увеличило → двойното докосване е увеличило 2×: още едно връща към 1×.
      if [ "${dd%.*}" -ge 8 ] 2>/dev/null; then dtap $((W/2)) $((H/2)); zshot $K-p$r-3-reset2 2.5
        log "  second double tap: diff vs 1x $(python3 $W18 diff $OUT/shots/zoom/$K-p$r-0-1x.png $OUT/shots/zoom/$K-p$r-3-reset2.png)"; fi
    done
    sec "Z-$K. Double tap at an off-centre point, pan while zoomed, double tap back"
    cx=$((W*30/100)); cy=$((H*46/100))
    zshot $K-d-0-1x 1
    dtap $cx $cy; zshot $K-d-1-2x 2.5
    zf $K-d-0-1x $K-d-1-2x $cx $cy "$K double tap at ($cx,$cy)"
    local pg0; pg0=$(viewer_page)
    a input swipe $((W*65/100)) $((H/2)) $((W*45/100)) $((H/2)) 900; zshot $K-d-2-panned 2
    log "PAN $K: $(python3 $W18 shift $OUT/shots/zoom/$K-d-1-2x.png $OUT/shots/zoom/$K-d-2-panned.png pan | cut -c1-120) (drag −$((W*20/100)) px); page before '$pg0' after '$(viewer_page)'"
    a input swipe $((W/2)) $((H*40/100)) $((W/2)) $((H*55/100)) 900; zshot $K-d-3-panned-down 2
    log "PAN-V $K (zoomed, vertical drag must pan, not dismiss): $(python3 $W18 shift $OUT/shots/zoom/$K-d-2-panned.png $OUT/shots/zoom/$K-d-3-panned-down.png pan | cut -c1-120); still in viewer: $(viewer_page | grep -q / && echo yes || echo NO)"
    dtap $((W/2)) $((H/2)); zshot $K-d-4-reset 2.5
    log "  after double tap back: diff vs 1x $(python3 $W18 diff $OUT/shots/zoom/$K-d-0-1x.png $OUT/shots/zoom/$K-d-4-reset.png) (≈0 = 1x)"
    sec "Z-$K. Swipe to the next photo, then drag down to dismiss"
    sw_left 150; sleep 2; log "after swipe left: $(viewer_page)"
    a input swipe $((W/2)) $((H*35/100)) $((W/2)) $((H*85/100)) 250; sleep 2.5; dump; shot zoom/$K-e-dismissed
    log "DISMISS $K: viewer page text '$(viewer_page)'; photo cells $(photo_cells | wc -l); $(texts 6 | cut -c1-160)"
    back 1.5; back 1.5; back 1.5
  done
}

# ================================================================= F. MONKEY 3000
part_monkey18() { MONKEY_N=3000; part_monkey; }

part_w18() {
  case $PART in
    hwprobe) part_hwprobe;;
    jank18-1|jank18-2) part_jank18;;
    drawn) part_drawn;;
    zoom18) part_zoom18;;
    monkey18) part_monkey18;;
    *) return 1;;
  esac
  return 0
}
