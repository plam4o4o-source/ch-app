# Пълната проверка на изданието v1.2.33 (ВРЕМЕННО, debug/full-test-133; резултати в emulator-results/v133full).
# Издаденото v1.2.33 (a830b51, versionCode 1500183): APK-ът от GitHub Release (само ARM — на x86_64 образите
# с превод на ARM, API 30+), иначе prodRelease x86_64 от a830b51 със същия versionCode и временен ключ.
# Зарежда се от run.sh след w15/w16/w18/w19 и ползва функциите им.
#   first      — първо пускане: въведението (3 стр., размер на текста, известия), BrandIntro, „Начало“
#   tour-a     — всички екрани (снимка + uiautomator + a11y): светло/тъмно × BG/EN
#   tour-b     — шрифт 1.3 + „Много голям“; хоризонтално (светло и тъмно)
#   tour-c     — 320 dp ширина; таблет 1280x800 dp (хоризонтално и вертикално)
#   demo       — devDebug (демо вход): „Моето“, заемания, история, съобщения, заявка, картата с наклон,
#                „Моята читателска година“ (зареждане, затваряне с бързо плъзгане)
#   func       — дърпане за опресняване (розетката по средата на жеста), „Изненадай ме“ (зарче и тръскане),
#                отваряне на книга, статия, преглед (лента, мащаб), събитие (напомняне, „Маршрут“, картинка),
#                документи/PDF, контакти, филтри/подреждане/полица, картата с наклон
#   func2      — „Поверителност“ (отключване), скенер на ръка, „Диагностика/Бързодействие“, изчистване на кеша
#   robust     — завъртане и смърт на процеса на всеки раздел и на дълбоките екрани
#   offline    — офлайн студен старт с и без кеш, всички раздели (part_offline)
#   monkey     — monkey 8000 събития, seed 1357 и seed 24680
#   update     — 1.2.32 → 1.2.33 с уиджета на началния екран и „Какво ново“ (истинските APK-та, иначе
#                построените с update.json от emulator-results)
#   widget     — уиджетът във всички размери; език на приложението и на устройството
#   perf       — студен/топъл старт n=20 (както е инсталирано и след speed-profile), Fully drawn, gfxinfo по
#                сценарии, „Бързодействие“, размер
#   idle       — CPU и кадри без докосване 30 s след отваряне на всеки екран
#   mem        — паметта в 30 цикъла (part_mem)
#   a11yt      — тестовата услуга за достъпност с „изследване чрез докосване“ (вместо TalkBack)
#   live36/361 — живото известие за изтеглянето (чипът) на Android 16 / 16 QPR (36.1)
#   old28      — API 28 (най-старият път, без AGSL и без език по приложение)
W20=$H_/w20.py
VC_PRE=1500182
L_FILTERS='=Филтри|=Filters'; L_SORT='=Подреждане|=Sort'; L_SHELF='=Полица|=Shelf'; L_LIST='=Списък|=List'
L_SURPRISE='=Изненадай ме|=Surprise me'; L_CLOSE='=Затвори|=Close'
L_SHARE='=Сподели|=Share'; L_SHARE_IMG='=Сподели като картинка|=Share as an image'
L_READER='=Режим за четене|=Reading mode'; L_PHOTOINFO='=За снимката|=About this photo'
L_HOMESEARCH='Търси книга|Search books'; L_SIGNIN='=Вход|=Sign in'; L_CARDSHOW='=Покажи картата|=Show card'
L_REMIND='=Напомни ми|=Remind me'; L_ROUTE='=Маршрут|=Directions'; L_MANUAL='=Въведи номера на ръка|=Enter the number manually'
L_YEAR='Моята читателска година|My reading year'
W20_PARTS=" live361b first tour-a tour-b tour-c demo func func2 robust offline monkey update widget perf idle mem a11yt live36 live361 old28 upd2 fix demo2 old28b "

cfg_desc() { echo "${W}x${H} px, density $(dens), font_scale $(a settings get system font_scale | tr -d '\r'), night $(a cmd uimode night | tr -d '\r' | grep -oE '(yes|no|auto)'), app locale $(a cmd locale get-app-locales $PKG 2>/dev/null | grep -oE '\[[^]]*\]'), rotation $(a settings get system user_rotation | tr -d '\r')"; }

# ---------------------------------------------------------------- снимка + uiautomator + a11y на един екран
cap() {  # $1 — име, [$2 — изчакване, s] → $OUT/shots/tour/$CFG/$1.png, $OUT/a11y/$CFG/$1.xml, ред в $SUM
  local n=$1 r ok=0
  sleep ${2:-0}
  mkdir -p $OUT/a11y/$CFG
  shot tour/$CFG/$n >/dev/null
  dump && ok=1 || { sleep 3; dump && ok=1; }
  if [ $ok = 1 ]; then
    cp $OUT/ui.xml $OUT/a11y/$CFG/$n.xml
    r=$(A11Y_PKG=$PKG python3 $H_/a11y.py $OUT/ui.xml ${DENS:-420} $W $H "$CFG/$n")
    echo "$r" >> $OUT/a11y-$CFG.txt
    log "CAP $CFG/$n: $(echo "$r" | head -1 | sed 's/^A11Y [^:]*: //') | front: $(has "package=\"$PKG\"" && echo app || echo "OTHER $(fg)") | $(python3 $W20 brief $OUT/ui.xml | cut -c1-230)"
  else
    echo "A11Y $CFG/$n: dump failed" >> $OUT/a11y-$CFG.txt
    log "CAP $CFG/$n: uiautomator dump FAILED twice (never idle?); fg $(fg)"
  fi
}
top_of() { local i; for i in 1 2 3 4; do sw_down 150; sleep 0.35; done; }

# ---------------------------------------------------------------- обиколка на всички екрани
tour() {  # $1 — етикет на конфигурацията
  CFG=$1; local p i
  restart; dims; DENS=$(dens)
  sec "T-$CFG. All screens ($(cfg_desc))"
  : > $OUT/a11y-$CFG.txt
  # Начало
  ready; tab home; cap 01-home 3
  sw_up 500; cap 01b-home-2 1.5; sw_up 500; cap 01c-home-3 1.5; sw_up 500; cap 01d-home-4 1.5; top_of
  if tap text "$L_HOMESEARCH" >/dev/null; then sleep 2; a input text 12; sleep 4; hide_ime; cap 01e-search-12 1; back 1.5; back 1.5; fi
  # Каталог: списък, филтри, подреждане, полица, търсене, книга, „Изненадай ме“
  ready; tab catalog; waitfor "$RX_RESULTS" 60; cap 02-catalog 1
  if tap content-desc "$L_FILTERS" >/dev/null; then cap 02b-filters 2; sw_up 500; cap 02c-filters-2 1.5; back 1.5; fi
  if tap content-desc "$L_SORT" >/dev/null; then cap 02d-sort 1.5; back 1.5; fi
  if tap content-desc "$L_SHELF" >/dev/null; then cap 02e-shelf 3; sw_up 500; cap 02f-shelf-2 1.5; top_of; tap content-desc "$L_LIST" >/dev/null; sleep 2; fi
  search_catalog 12 >/dev/null; cap 02g-search-12 1
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*85/100)) 120)
  if [ -n "$p" ]; then a input tap $p; cap 03-book 3; sw_up 500; cap 03b-book-2 1.5; sw_up 500; cap 03c-book-3 1.5; back 2; fi
  ready; tab catalog; sleep 1.5
  if tap content-desc "$L_SURPRISE" >/dev/null; then cap 04-surprise 4; tap content-desc "$L_CLOSE" >/dev/null || back 1.5; sleep 1.5; fi
  # Моето
  ready; tab my; cap 05-my 3; sw_up 500; cap 05b-my-2 1.5; top_of
  if card_open >/dev/null 2>&1; then cap 06-card 2.5; if tap text "$L_CARDSHOW" >/dev/null; then cap 06b-card-full 3; back 1.5; fi; back 1.5; fi
  if open_want_list; then cap 07-want 1; back 1.5; fi
  # Новини: списък, статия (лента, „Аа“, споделяне), календар, събитие
  link_pkg "$SITE/news" >/dev/null; sleep 4; segstate | grep -q 'News\[checked=true' || segment news; cap 08-news 1
  sw_up 500; cap 08b-news-2 1.5; top_of
  if open_article; then cap 09-article 1; sw_up 400; cap 09b-article-2 1.5; sw_down 400; sleep 1.5
    if tap content-desc "$L_READER" >/dev/null; then cap 09c-reader-sheet 2; back 1.5; fi
    dump; if tap content-desc "$L_SHARE" >/dev/null; then cap 09d-share-sheet 2; back 1.5; fi
    back 2; fi
  link_pkg "$EVENTS_URL" >/dev/null; sleep 4; cal_checked || segment calendar; cap 10-calendar 1
  if open_event 1; then cap 11-event 1; sw_up 500; cap 11b-event-2 1.5; top_of
    dump; if tap content-desc "$L_SHARE" >/dev/null; then cap 11c-event-share 2; back 1.5; fi
    back 2; fi
  # Още: галерия, албум, преглед, контакти, документи, настройки
  ready; tab more; cap 12-more 2; sw_up 500; cap 12b-more-2 1.5
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30; then cap 13-gallery 2
    if open_album 1; then wait_cells 15; cap 14-album 1.5
      if open_photo 1; then cap 15-viewer 2.5; if tap content-desc "$L_PHOTOINFO" >/dev/null; then cap 15b-viewer-info 2; back 1.5; fi; back 1.5; fi
      back 1.5; fi
    back 1.5; fi
  link "$SITE/kontakti" >/dev/null; cap 16-contacts 4; sw_up 500; cap 16b-contacts-2 1.5; back 2
  link "$SITE/load" >/dev/null; cap 17-documents 4; sw_up 500; cap 17b-documents-2 1.5; back 2
  if open_settings; then cap 18-settings 1; sw_up 600; cap 18b-settings-2 1.5; sw_up 600; cap 18c-settings-3 1.5; sw_up 600; cap 18d-settings-4 1.5
    settings_item '=Размер на текста|=Text size' >/dev/null && { cap 18e-text-size 2; back 1.5; }
    settings_item '=Език|=Language' >/dev/null && { cap 18f-language 2; back 1.5; }
    settings_item '=Тема|=Theme' >/dev/null && { cap 18g-theme 2; back 1.5; }
    settings_item 'Избери известията|Choose notifications|Choose which' >/dev/null && { cap 18h-notifications 2.5; back 1.5; }
    settings_item '=Политика за поверителност|=Privacy policy' >/dev/null && { cap 18i-privacy-policy 2.5; back 1.5; }
    settings_item '=Условия за използване|=Terms of use' >/dev/null && { cap 18j-terms 2.5; back 1.5; }
    settings_item '=За приложението|=About the app' >/dev/null && { cap 18k-about 2.5; back 1.5; }
    settings_item '=Диагностика|=Diagnostics' >/dev/null && { cap 18l-diagnostics 2.5; sw_up 500; cap 18m-diagnostics-2 1.5; back 1.5; }
    back 1.5; fi
  # Вход и скенер (на ръка)
  ready; tab my; sleep 2
  if tap text "$L_SIGNIN" >/dev/null; then cap 19-login 3; hide_ime; back 2; fi
  scanner; cap 20-scanner 4
  if tap text "$L_MANUAL" >/dev/null; then sleep 2; hide_ime; cap 20b-scan-manual 1; back 2; fi
  back 2; ready >/dev/null
  log "TOUR $CFG: $(python3 $W20 a11yagg $OUT/a11y-$CFG.txt | head -1)"
}
part_tour() {
  anims 1
  warmup
  case $PART in
    tour-a)
      tour bg-light
      a cmd uimode night yes; sleep 3; tour bg-dark
      a cmd locale set-app-locales $PKG --locales en-US; sleep 3; tour en-dark
      a cmd uimode night no; sleep 3; tour en-light
      a cmd locale set-app-locales $PKG --locales bg-BG;;
    tour-b)
      if set_text_size '=Много голям|=Extra large'; then
        a settings put system font_scale 1.3; sleep 3; tour font130-xlarge
        a cmd locale set-app-locales $PKG --locales en-US; sleep 3; tour font130-xlarge-en
        a cmd locale set-app-locales $PKG --locales bg-BG; a settings put system font_scale 1.0; sleep 2; set_text_size '=Стандартен|=Standard'
      fi
      rot 1; tour landscape-light
      a cmd uimode night yes; sleep 3; tour landscape-dark; a cmd uimode night no; rot 0;;
    tour-c)
      a wm density 540; sleep 4; tour w320dp
      a wm size 2560x1600; a wm density 320; sleep 4; tour tablet-1280x800dp
      rot 1; tour tablet-portrait; rot 0
      a wm size reset; a wm density reset;;
  esac
  sec "T. Accessibility over all screens of this part"
  python3 $W20 a11yagg $OUT/a11y-*.txt | tee $OUT/a11y-agg.txt | head -60 | tee -a $SUM
}

# ---------------------------------------------------------------- край на всяка част: локалният дневник на грешките
diag_end() {
  sec "Z. The app's local diagnostics log (Settings › Информация › Диагностика) and app W/E lines in logcat"
  local f=/data/data/$PKG/no_backup/diagnostics/log.jsonl
  if [ "${ROOT:-0}" = 1 ]; then
    a "cat $f 2>/dev/null" > $OUT/diag-log.jsonl 2>/dev/null
    python3 $W20 diaglog $OUT/diag-log.jsonl | head -24 | tee -a $SUM
  fi
  launch >/dev/null 2>&1; sleep 4; dismiss
  if open_settings && settings_item '=Диагностика|=Diagnostics' >/dev/null; then
    sleep 2.5; dump; shot diag/diagnostics-end; cp $OUT/ui.xml $OUT/diag-screen.xml
    log "DIAG SCREEN: $(grep -oE 'text="(Записи: [0-9]+|Entries: [0-9]+|Няма записани грешки\.|No errors recorded[^"]*)"' $OUT/ui.xml | head -2 | tr '\n' ' ') $(python3 $W18 perfcard $OUT/ui.xml | cut -c1-400)"
    sw_up 600; sleep 1.5; dump; shot diag/diagnostics-end-2; log "DIAG SCREEN (scrolled): $(texts 30 | cut -c1-700)"
    back 1.5; back 1.5
  fi
  adb logcat -d -v threadtime > $OUT/logcat-we-src.txt 2>/dev/null
  cat $OUT/logcat-live.txt $OUT/logcat-we-src.txt 2>/dev/null | awk '!s[$0]++' > $OUT/logcat-we.txt
  python3 $W20 appwe $OUT/logcat-we.txt org.chyavorec.app org.chyavorec.app.dev | tee $OUT/app-we.txt | head -30 | tee -a $SUM
  rm -f $OUT/logcat-we-src.txt $OUT/logcat-we.txt
}

# ---------------------------------------------------------------- 1. ПЪРВО ПУСКАНЕ
part_first() {
  local i p
  anims 1
  part_onboard
  sec "F1. BrandIntro on the very first launch (fresh install, animations ON): screen recording"
  a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1
  timeout 180 adb install app.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  go_home 2
  rec_start brandintro 7; launch >/dev/null; rec_end; mkdir -p $OUT/shots/first; cp $OUT/rec/brandintro/f0{04,06,08,10,12,16,24,32}.png $OUT/shots/first/ 2>/dev/null
  python3 $W20 strip $OUT/shots/first/brandintro-strip.png all $OUT/rec/brandintro/f0{02,04,06,08,10,12,14,18,24}.png >/dev/null
  log "BRANDINTRO frame stats (8 fps; the edges = the shader 'жива шевица'): $(python3 $W20 frames $OUT/rec/brandintro/f0{02,04,06,08,10,12,16,24}.png | tr '\n' ' ' | cut -c1-600)"
  rec_clean
  dump; log "after the intro: $(texts 8 | cut -c1-200)"
  sec "F2. Onboarding → Home (guest): the шевица ribbon, carousel, feasts/events, „Нови постъпления“, the search bar"
  tap text '=Напред' >/dev/null; sleep 1.5; tap text '=Напред' >/dev/null; sleep 1.5
  tap text '=Продължи без вход' >/dev/null || tap text '=Продължи' >/dev/null || tap text '=Пропусни' >/dev/null
  t0=$(now_ms); for i in 01 04 08 14 20 26; do shot_t first/home-${i}s $((10#$i * 1000)); done
  dismiss; dump; ribbon_box
  [ -n "$RBOX" ] && log "RIBBON first run: change 1→4→8→14→20→26 s $(python3 $W19 seq "$RBOX" $OUT/shots/first/home-{01,04,08,14,20,26}s.png)"
  CFG=first; DENS=$(dens); dims
  cap 10-home-top 0
  for i in 1 2 3 4 5; do sw_up 450; cap 1$i-home-scroll-$i 1.5; done
  log "HOME sections seen while scrolling: $(cat $OUT/a11y/first/1*.xml 2>/dev/null | grep -oE 'text="(Нови постъпления|Предстоящи събития|Последни новини|Днес|Празници[^"]*|Имен ден[^"]*|Бързи връзки|Още новини|[^"]*празник[^"]*)"' | sort -u | tr '\n' ' ')"
  top_of; sleep 1; dump
  local cy; cy=$(python3 $W15 big $OUT/ui.xml $PKG 2>/dev/null | awk '{print int(($2+$4)/2)}')
  if [ -n "$cy" ] && [ "$cy" -gt 0 ]; then
    shot first/carousel-0; a input swipe $((W*85/100)) $cy $((W*15/100)) $cy 200; sleep 1.5; shot first/carousel-1
    log "CAROUSEL swipe at y=$cy: diff $(python3 $W19 rdiff $OUT/shots/first/carousel-0.png $OUT/shots/first/carousel-1.png "0 $((cy - 200)) $W $((cy + 200))")"
  else log "CAROUSEL: big card not found"; fi
  sec "F3. Second launch: no intro, no onboarding"
  a am force-stop $PKG; go_home 2; rec_start second 5; launch >/dev/null; rec_end; rec_clean
  dump; log "second launch: $(texts 6 | cut -c1-200)"
  sec "F4. First run offline with nothing cached (fresh install, airplane mode): all tabs"
  a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1; timeout 180 adb install app.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS; no_camera >/dev/null
  offline; launch >/dev/null; sleep 6; dismiss; ready; dims
  all_screens_offline first-offline
  online; sleep 5
  diag_end
}

# ---------------------------------------------------------------- 2. ДЕМО (devDebug)
demo_signin() {
  local p
  ready; tab my; sleep 3; tap text "$L_SIGNIN" >/dev/null || log "no 'Вход' in My: $(texts 10 | cut -c1-200)"
  sleep 3; dump; p=$(xy --all class EditText | sed -n 1p)
  if [ -n "$p" ]; then
    a input tap $p; sleep 0.8; a input text DEMO-0001; sleep 0.5
    dump; p=$(xy --all class EditText | sed -n 2p); a input tap $p; sleep 0.8; a input text demo; sleep 0.5; hide_ime
    tap text '=Вход' >/dev/null; sleep 6
  fi
  ready; tab my; sleep 3; dump
  log "MY after the demo sign-in: $(texts 12 | cut -c1-300)"
}
year_card_find() { local i; for i in 1 2 3 4 5 6; do dump; [ -n "$(xy text "$L_YEAR")" ] && return 0; sw_up 350; sleep 1.2; done; return 1; }
open_link_dev() { a am start -a org.chyavorec.app.action.OPEN_LINK -n $PKG/org.chyavorec.app.OpenLink --es deep_link "$1" >/dev/null 2>&1; sleep ${2:-4}; }
year_story_closed() { dump; ! has 'Слайд [0-9]+ от|Следващ слайд|Предишен слайд' && has 'text="(Моето|Начало)"'; }
year_timed() {  # $1 — етикет: докосване на картата → кадри 0.5 s → кога се вижда първият слайд
  local t1 i st="" got=-1
  year_card_find || { log "YEAR $1: no year card"; return 1; }
  rec_start year-$1 20
  mark y-$1; t1=$(now_ms); tap text "$L_YEAR" >/dev/null
  # успоредно: uiautomator dump на всеки ~1 s, докато се появи слайдът (описание на слайда / „Затвори“)
  for i in $(seq 1 30); do
    if dump && grep -qE 'content-desc="[^"]*(твоята година с книгите|Тази година взе|Затвори)' $OUT/ui.xml; then got=$(( $(now_ms) - t1 )); break; fi
    [ $(( $(now_ms) - t1 )) -gt 22000 ] && break
    sleep 0.4
  done
  rec_end >/dev/null 2>&1
  local fr=$OUT/rec/year-$1
  python3 $W20 frames $fr/f*.png > $OUT/year-frames-$1.txt 2>/dev/null
  python3 $W20 strip $OUT/shots/year/load-$1.png all $(ls $fr/f*.png 2>/dev/null | awk 'NR%8==1' | head -12) >/dev/null
  log "YEAR $1: slide visible to uiautomator after ${got} ms (-1 = not within 22 s); frame std every 1 s: $(awk 'NR%8==1 {sub(/std=/,"",$3); printf "%s ", $3}' $OUT/year-frames-$1.txt)"
  log "YEAR $1 logcat in the window (GC / catalogue / app): GC lines $(since y-$1 'GC freed|concurrent copying|mark compact' | wc -l), catalogue lines: $(since y-$1 'atalog|katalog|Searcher|lookup' | grep -v PERFCHECK | head -3 | cut -c19-170 | tr '\n' '~')"
  rec_clean
}
part_demo() {
  local p i s
  helper_install
  sec "Y0. devDebug (org.chyavorec.app.dev, demo data)"
  use_pkg org.chyavorec.app.dev
  timeout 180 adb install -r app-dev.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  log "installed: $(cat apk-dev-ref.txt 2>/dev/null); $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  anims 1
  warmup
  sec "Y1. Demo sign-in (DEMO-0001 / demo)"
  demo_signin
  CFG=demo; DENS=$(dens); dims; : > $OUT/a11y-demo.txt
  sec "Y2. Моето (signed in) and its screens"
  ready; tab my; cap 01-my 2; sw_up 500; cap 01b-my-2 1.5; sw_up 500; cap 01c-my-3 1.5; top_of
  open_link_dev my/loans; cap 02-loans 1; back 1.5
  open_link_dev my/history; cap 03-history 2; sw_up 500; cap 03b-history-2 1.5; back 1.5
  open_link_dev messages; cap 04-messages 2
  dump; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $((H/5)) $((H*80/100)) 120); [ -n "$p" ] && { a input tap $p; cap 04b-message 2.5; back 1.5; }
  back 1.5
  ready; tab my; sleep 2
  for s in 'Моето членство' 'Моят профил' 'Известия'; do
    for i in 1 2 3; do dump; [ -n "$(xy text "=$s")" ] && break; sw_up 400; sleep 1; done
    tap text "=$s" >/dev/null && { cap "05-$(echo $s | tr ' ' '-')" 2.5; back 1.5; }
  done
  open_link_dev my/want-to-read; cap 06-want 1.5; back 1.5
  sec "Y3. Hold ('Заяви книгата') on a book on the shelf"
  for p in $INV 1 2 3 5 8 13; do
    link "$SITE/elektronen-katalog?inv=$p" >/dev/null; sleep 4; dump
    for i in 1 2; do [ -n "$(xy text '=Заяви книгата')" ] && break; sw_up 400; sleep 1; dump; done
    [ -n "$(xy text '=Заяви книгата')" ] && break
    back 1.5
  done
  if [ -n "$(xy text '=Заяви книгата')" ]; then
    cap 07-book-hold-button 0; tap text '=Заяви книгата' >/dev/null; sleep 3; cap 07b-after-hold 0
    log "HOLD (inv $p): $(grep -oE 'text="[^"]*(Заявката|заявка|ПИН|влез)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
    back 1.5
  else log "HOLD: no 'Заяви книгата' on inv $INV/1/2/3/5/8/13 (canHold false?)"; fi
  sec "Y4. Library card with the foil: two different tilts → pixel diff (signed-in demo card)"
  sensors_info
  card_tilt demo
  sec "Y5. „Моята читателска година“: loading time (A: fresh process, catalogue not opened; B: after the catalogue tab; C: reopened)"
  a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 5; dismiss; ready; tab my; sleep 3
  year_timed A-fresh-process
  back 2; sleep 1; year_story_closed || back 2
  ready; tab catalog; waitfor "$RX_RESULTS" 60 && log "catalogue results after ${WAITED} ms"; ready; tab my; sleep 2; top_of
  year_timed B-after-catalogue
  back 2; sleep 1; year_story_closed || back 2
  ready; tab my; sleep 2; top_of
  year_timed C-reopened
  dump; cap 08-year-story-slide 0
  sec "Y6. Year story: closing with a swipe down (fast 350/200/120 ms, short flick, slow 1200 ms control)"
  local spec dur y1 y2 lbl
  for spec in "fast350:30:85:350" "fast200:30:85:200" "fast120:30:85:120" "flick:40:62:90" "slow1200:25:80:1200"; do
    IFS=: read lbl y1 y2 dur <<< "$spec"
    year_story_closed && { ready; tab my; sleep 1.5; top_of; year_card_find && tap text "$L_YEAR" >/dev/null; for i in $(seq 1 25); do sleep 1; dump && grep -qE 'content-desc="[^"]*(Затвори|Следващ слайд)' $OUT/ui.xml && break; done; sleep 1.5; }
    a input swipe $((W/2)) $((H*y1/100)) $((W/2)) $((H*y2/100)) $dur; sleep 2.5; shot year/swipe-$lbl
    log "YEAR swipe down $lbl (${y1}%→${y2}% in ${dur} ms): $(year_story_closed && echo closed || echo 'STILL OPEN')"
  done
  year_story_closed || back 2
  sec "Y7. Accessibility tree of the story (test service)"
  a11y_on; ready; tab my; sleep 1.5; top_of; year_card_find && tap text "$L_YEAR" >/dev/null; sleep 20; adump year-story; a11y_off; back 2
  sec "Y8. Diagnostics › Бързодействие (devDebug)"
  perf_card dev; back 1.5; back 1.5
  python3 $W20 a11yagg $OUT/a11y-demo.txt | tee $OUT/a11y-agg.txt | head -40 | tee -a $SUM
  diag_end
  use_pkg org.chyavorec.app
}

# ---------------------------------------------------------------- картата: наклон → фолиото
card_tilt() {  # $1 — етикет
  local B
  card_screen; dump
  card_shown || { card_add $CARD_NO >/dev/null 2>&1; card_screen; dump; }
  card_shown || { log "TILT $1: card not shown ($(texts 8 | cut -c1-160))"; return 1; }
  B=$(python3 $H_/find.py $OUT/ui.xml --bounds content-desc 'Баркод на карта|Library card barcode' 2>/dev/null)
  emu sensor set acceleration 0:9.81:0 >/dev/null; sleep 30
  emu sensor set acceleration 4.5:8.7:0 >/dev/null; sleep 2.5; shot tilt/$1-A1; sleep 1.5; shot tilt/$1-A2
  emu sensor set acceleration -4.5:8.7:0 >/dev/null; sleep 2.5; shot tilt/$1-B
  emu sensor set acceleration 0:7.5:6.3 >/dev/null; sleep 2.5; shot tilt/$1-C
  emu sensor set acceleration 0:9.81:0 >/dev/null; sleep 2.5; shot tilt/$1-F
  log "TILT $1 (card region 0 250 $W 1250; barcode at [$B]): $(python3 $W20 tilt "0 250 $W 1250" $OUT/shots/tilt/$1-{A1,A2,B,C,F}.png 2>&1)"
  python3 $W20 strip $OUT/shots/tilt/$1-strip.png "0 250 $W 1250" $OUT/shots/tilt/$1-{A1,A2,B,C,F}.png >/dev/null
  log "TILT $1 sensor status: $(emu sensor status | cut -c1-120); game rotation vector listeners: $(a dumpsys sensorservice 2>/dev/null | tr -d '\r' | grep -ciE "$PKG")"
  back 1.5
}

# ---------------------------------------------------------------- 3. ФУНКЦИИ
pull_refresh() {  # $1 — етикет, [$2 — начало в % от височината]: бавно дърпане 1500 ms, снимки по средата на жеста
  local x=$((W/2)) y0=$((H*${2:-22}/100)) y1=$((H*92/100)) f
  top_of; sleep 1; shot refresh/$1-0-before
  a "rm -f /sdcard/pr*.png"
  a "input swipe $x $y0 $x $y1 1500 & sleep 0.45; screencap -p /sdcard/pr1.png; sleep 0.15; screencap -p /sdcard/pr2.png; screencap -p /sdcard/pr3.png; wait; screencap -p /sdcard/pr4.png; sleep 0.5; screencap -p /sdcard/pr5.png; sleep 1.2; screencap -p /sdcard/pr6.png" >/dev/null 2>&1
  mkdir -p $OUT/shots/refresh
  for f in 1 2 3 4 5 6; do timeout 20 adb pull /sdcard/pr$f.png $OUT/shots/refresh/$1-$f.png >/dev/null 2>&1; done
  python3 $W20 strip $OUT/shots/refresh/$1-strip.png "0 0 $W $((H*55/100))" $OUT/shots/refresh/$1-{0-before,1,2,3,4,5,6}.png >/dev/null
  log "REFRESH $1: top-area diff vs before (mid-pull 1,2,3 | released 4 | +0.5 s 5 | +1.7 s 6): $(for f in 1 2 3 4 5 6; do printf '%s ' "$(python3 $W19 rdiff $OUT/shots/refresh/$1-0-before.png $OUT/shots/refresh/$1-$f.png "0 0 $W $((H*45/100))" 2>/dev/null)"; done)"
}
f_refresh_all() {
  sec "PR. Pull-to-refresh rosette: slow 1500 ms pull from the top, screencaps on the device mid-gesture"
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; pull_refresh news
  ready; tab home; sleep 3; pull_refresh home
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar; pull_refresh calendar
  ready; tab my; sleep 3; pull_refresh my
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30; then pull_refresh gallery; back 1.5; fi
  ready; tab catalog; sleep 3; pull_refresh catalog
  sec "PR2. The same pull on News, recorded (screenrecord, 8 fps)"
  link_pkg "$SITE/news" >/dev/null; sleep 4; top_of
  rec_start refresh-news 5; a input swipe $((W/2)) $((H*22/100)) $((W/2)) $((H*78/100)) 1500; rec_end "0 0 1080 1300"; rec_clean
}
f_catalog_ops() {
  local n0 n1
  sec "C1. Catalogue: search, filter 'Само налични', sort, shelf view"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; search_catalog 12 >/dev/null; n0=$(rcount); log "search '12': $n0 results"
  if tap content-desc "$L_FILTERS" >/dev/null; then sleep 2; dump; shot catalog/filters
    log "FILTERS sheet: $(texts 20 | cut -c1-400)"
    tap text '=Само налични' >/dev/null; sleep 1; dump; shot catalog/filters-available
    tap text 'Покажи|резултат' --last >/dev/null || back 1.5; sleep 3; dump; n1=$(rcount)
    log "FILTER 'Само налични': $n0 → $n1 results; chips: $(grep -oE 'text="[^"]*(налични|Филтри)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' ')"
    shot catalog/after-filter
  fi
  if tap content-desc "$L_SORT" >/dev/null; then sleep 1.5; dump; shot catalog/sort; log "SORT options: $(texts 12 | cut -c1-300)"
    tap text '=Година (нови първо)' >/dev/null; sleep 3; dump; shot catalog/sorted-year; log "after sort by year: $(texts 10 | cut -c1-260)"; fi
  if tap content-desc "$L_SHELF" >/dev/null; then sleep 3; shot catalog/shelf; gfx_reset; fling_v; gfx_save shelf-scroll; tap content-desc "$L_LIST" >/dev/null; sleep 1; fi
}
f_viewer_zoom() {
  sec "VZ. Gallery viewer: toolbar and zoom (double tap, pinch)"
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && open_photo 1; then
    sleep 2; shot viewer/0; dims
    mt_init || log "VIEWER: no multitouch device"
    dtap $((W/2)) $((H/2)) >/dev/null 2>&1
    sleep 1.5; shot viewer/1-doubletap; log "VIEWER double tap: diff $(python3 $W19 rdiff $OUT/shots/viewer/0.png $OUT/shots/viewer/1-doubletap.png "0 300 $W $((H-300))")"
    dtap $((W/2)) $((H/2)) >/dev/null 2>&1; sleep 1.5
    pinch $((W/2)) $((H/2)) 200 760 >/dev/null 2>&1; sleep 1.5; shot viewer/2-pinch; log "VIEWER pinch: diff $(python3 $W19 rdiff $OUT/shots/viewer/0.png $OUT/shots/viewer/2-pinch.png "0 300 $W $((H-300))")"
    back 1.5; back 1.5; back 1.5; back 1.5
  else log "VIEWER: not reached"; fi
}
f_docs_pdf() {
  sec "DOC. Documents: the list and opening a PDF"
  link "$SITE/load" >/dev/null; sleep 5; dump; shot docs/0
  log "DOCUMENTS: $(texts 14 | cut -c1-400)"
  pdf_try doc1 1 || true
  dump; has "package=\"$PKG\"" || launch >/dev/null
}
part_func() {
  local i
  helper_install
  anims 1
  warmup
  f_refresh_all
  f_catalog_ops
  f_bookopen
  f_surprise
  f_article
  f_gallery
  f_viewer_zoom
  f_event
  sec "EV2. Event: „Маршрут“"
  if open_event 1; then for i in 1 2 3; do dump; [ -n "$(xy text "$L_ROUTE")" ] && break; sw_up 400; sleep 1; done; route_tap ev1 || log "ROUTE: no 'Маршрут' on the event: $(texts 8 | cut -c1-200)"; back 2; fi
  f_docs_pdf
  sec "CT. Contacts: action buttons"
  contacts_row 40-standard
  sec "CARD. Self-entered card: foil with tilt"
  sensors_info; card_tilt release
  diag_end
}
part_func2() {
  helper_install
  anims 1
  sec "S. Scanner: manual entry (no camera)"
  warmup
  scanner; sleep 4; dump; shot scan/0; log "SCANNER: $(texts 8 | cut -c1-260)"
  if tap text "$L_MANUAL" >/dev/null; then sleep 2; a input text $INV; sleep 1.5; tap text '=Търси' --last >/dev/null; sleep 4; dump; shot scan/1-result
    log "SCAN manual $INV: $(has 'text="Отвори книгата"' && echo 'book sheet' || echo 'NO book sheet'); $(texts 10 | cut -c1-260)"
    tap text '=Отвори книгата' >/dev/null && { sleep 4; dump; log "SCAN → book: $(texts 3 | cut -c1-120)"; back 2; }
  fi
  back 2; ready
  sec "D. Diagnostics › Бързодействие after a warm-up"
  perf_card release; back 1.5; back 1.5
  sec "CL. Settings › Изчисти временните данни"
  clear_public_cache; restart; tab catalog; waitfor "$RX_RESULTS" 150 && log "catalogue after clearing: results after ${WAITED} ms"
  sec "U. Settings › Поверителност: unlock for the personal screens (part_unlock)"
  part_unlock
  diag_end
}

# ---------------------------------------------------------------- 4. ЗАВЪРТАНЕ И СМЪРТ НА ПРОЦЕСА
goto2() {  # $1 — екран
  case $1 in
    home|catalog|my|more) ready; tab $1; sleep 3; [ $1 = catalog ] && { waitfor "$RX_RESULTS" 30; search_catalog 12 >/dev/null; };;
    news) link_pkg "$SITE/news" >/dev/null; sleep 4; segstate | grep -q 'News\[checked=true' || segment news;;
    calendar) link_pkg "$EVENTS_URL" >/dev/null; sleep 4; cal_checked || segment calendar;;
    book) link "$BOOK_URL" >/dev/null; sleep 4;;
    want) open_want_list;;
    article) open_article >/dev/null;;
    event) open_event 1 >/dev/null;;
    gallery) open_gallery && waitfor "$RX_GALLERY_LOADED" 30;;
    album) open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && wait_cells 15;;
    viewer) open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && open_photo 1;;
    settings) open_settings && { sw_up 600; sleep 1; };;
    diagnostics) open_settings && settings_item '=Диагностика|=Diagnostics' >/dev/null;;
    card) card_open >/dev/null;;
    search) ready; tab home; sleep 2; tap text "$L_HOMESEARCH" >/dev/null && { sleep 2; a input text 12; sleep 3; hide_ime; };;
    surprise) ready; tab catalog; waitfor "$RX_RESULTS" 30; tap content-desc "$L_SURPRISE" >/dev/null; sleep 4;;
    contacts) link "$SITE/kontakti" >/dev/null; sleep 4;;
    documents) link "$SITE/load" >/dev/null; sleep 4;;
  esac
  sleep 1.5
}
leave() {  # обратно към разделите
  local i; for i in 1 2 3 4; do dump; has 'text="(Начало|Home)"' && has 'text="(Още|More)"' && ! has "$RX_BACK" && return 0; back 1.2; done; ready >/dev/null
}
robx() {  # $1 — екран: завъртане ×2 и смърт на процеса; сравнение на текстовете с тези преди това
  local s=$1 pid0 pid1 pid2
  goto2 $s; dump; cp $OUT/ui.xml $OUT/rb-$s-0.xml; shot robust/$s-0; pid0=$(pidof_app)
  rot 1; sleep 2.5; dump; cp $OUT/ui.xml $OUT/rb-$s-1.xml; shot robust/$s-1-land
  rot 0; sleep 2.5; dump; cp $OUT/ui.xml $OUT/rb-$s-2.xml; shot robust/$s-2-back; pid1=$(pidof_app)
  log "ROT $s: pid $pid0→$pid1 ($([ "$pid0" = "$pid1" ] && echo same || echo CHANGED)); landscape: $(python3 $W20 sig $OUT/rb-$s-0.xml $OUT/rb-$s-1.xml); back to portrait: $(python3 $W20 sig $OUT/rb-$s-0.xml $OUT/rb-$s-2.xml)"
  go_home 2.5; a am kill $PKG; sleep 2.5; pid2=$(pidof_app); [ -n "$pid2" ] && { sleep 4; a am kill $PKG; sleep 2; pid2=$(pidof_app); }
  launch >/dev/null; sleep 5; dump; cp $OUT/ui.xml $OUT/rb-$s-3.xml; shot robust/$s-3-restored
  log "PROCDEATH $s: killed $([ -z "$pid2" ] && echo yes || echo "NO ($pid2)"); new pid $(pidof_app); restored: $(python3 $W20 sig $OUT/rb-$s-0.xml $OUT/rb-$s-3.xml); front $(has "package=\"$PKG\"" && echo app || echo OTHER)"
  leave
}
part_robust() {
  local s
  anims 1
  warmup
  want_add "$BOOK_URL"; card_open >/dev/null; back 1.5
  sec "RB. Rotation (portrait → landscape → portrait) and process death (Home, am kill, launcher) on every tab and the deep screens"
  for s in home catalog my news more calendar book want article event gallery album viewer settings diagnostics card search surprise contacts documents; do robx $s; done
  sec "RB2. Process death, back from Recents (article, viewer)"
  pdeath book recents
  sec "RB3. Totals"
  log "ROT not fully kept (<90%): $(grep -E '^ROT ' $SUM | grep -vE 'portrait: kept (9[0-9]|100)%' | cut -d: -f1 | tr '\n' ' ')"
  log "PROCDEATH not fully restored (<90%): $(grep -E '^PROCDEATH ' $SUM | grep -vE 'restored: kept (9[0-9]|100)%' | cut -d: -f1 | tr '\n' ' ')"
  diag_end
}

# ---------------------------------------------------------------- 5. MONKEY
part_monkey2() {
  local seed
  warmup
  for seed in 1357 24680; do
    sec "K-$seed. Monkey 8000 events (seed $seed, throttle 80 ms, CAMERA revoked)"
    no_camera >/dev/null; restart
    mark monkey-$seed; local t0; t0=$(now_ms)
    timeout 2400 adb shell monkey -p $PKG --pct-syskeys 0 --pct-anyevent 0 --throttle 80 -s $seed -v 8000 > $OUT/monkey-$seed.txt 2>&1
    log "MONKEY $seed exit=$? duration=$(( ($(now_ms) - t0) / 1000 )) s; $(grep -E 'Events injected|Monkey finished|aborted' $OUT/monkey-$seed.txt | tr '\n' ' ')"
    log "MONKEY $seed markers: CRASH=$(grep -c 'CRASH' $OUT/monkey-$seed.txt) ANR=$(grep -c 'NOT RESPONDING\|ANR in' $OUT/monkey-$seed.txt) FATAL since start: $(since monkey-$seed 'FATAL EXCEPTION' | wc -l)"
    grep -nE -A20 'CRASH|NOT RESPONDING|ANR in' $OUT/monkey-$seed.txt | head -40
    alive "after monkey $seed"
    online; a settings put system font_scale 1.0; a cmd uimode night no; rot 0
    shot monkey-$seed-end; mem after-monkey-$seed
  done
  diag_end
}

# ---------------------------------------------------------------- 6. ОБНОВЯВАНЕ 1.2.32 → 1.2.33 С УИДЖЕТА
upd_flow() {  # $1 — етикет, $2 — очакваният versionCode
  local st t0 p
  launch >/dev/null; t0=$(now_ms)
  upd_dialog 'text="(Нова версия|New version)' && st=dialog || st=none
  shot update/$1-10-dialog; cp $OUT/ui.xml $OUT/dialog-$1.xml 2>/dev/null
  log "UPDATE $1 dialog: $st after $(( $(now_ms) - t0 )) ms: $(texts 12 | cut -c1-500)"
  [ $st = dialog ] || return 1
  [ -s $OUT/update.json ] && notes_match $1-dialog
  p=$(xy text '=Инсталирай|=Install'); [ -z "$p" ] && p=$(xy text '=Изтегли и инсталирай|=Download and install')
  [ -n "$p" ] && a input tap $p
  installer_confirm $2 $1 && log "UPDATE $1: installed $(ver)" || { log "UPDATE $1: NOT installed: $(ver)"; return 1; }
  sleep 3
  sec "U-$1b. After the update: the widget (app not reopened yet), then „Какво ново“"
  go_home 3; log "WIDGET after the update (before opening the app): $(wstate)"; shot update/$1-20-widget-after
  wcount $1-after >/dev/null; log "widget ids after: [$(wids)]"
  launch >/dev/null; sleep 3
  whatsnew_check $1-30-whats-new && { a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*40/100)) 400; sleep 1.5; shot update/$1-31-whats-new-scrolled; tap text '=Добре|=OK|=Разбрах|=Got it' >/dev/null || back 2; }
  sleep 2; dump; log "after 'Какво ново': $(texts 6 | cut -c1-200)"
  a am force-stop $PKG; launch >/dev/null; sleep 5; dump; log "relaunch: what's new again? $(has "$RX_WHATSNEW" && echo 'SHOWN AGAIN' || echo no)"
  go_home 3; log "WIDGET after opening the app: $(wstate)"; shot update/$1-40-widget-after-app
  K=$1; widget_clicks
}
part_update133() {
  local ok=no
  helper_install
  anims 1
  if [ -f rel-132.apk ] && [ -f rel-133.apk ] && [ "$SDK" -ge 30 ]; then
    sec "UA0. Released v1.2.32 (GitHub release APK, ARM, release key) → the real update.json → released v1.2.33"
    curl -sSL --max-time 30 "https://github.com/plam4o4o-source/ch-app/releases/latest/download/update.json" -o $OUT/update.json
    log "real manifest: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["size"], len(d["notes"].split(chr(10))), "note lines")' $OUT/update.json 2>&1)"
    if inst rel-132.apk rel-132 /dev/null; then
      log "abi: $(a dumpsys package $PKG | tr -d '\r' | grep -m1 -oE 'primaryCpuAbi=[^ ]+')"
      a appops set $PKG REQUEST_INSTALL_PACKAGES allow
      launch >/dev/null; sleep 6; dismiss; ready && ok=yes
      tab catalog; waitfor "$RX_RESULTS" 150; tab home; sleep 3
      log "v1.2.32 running: $ok ($(ver))"
    fi
    if [ $ok = yes ]; then
      sec "UA1. v1.2.32: the widget on the home screen"
      K=rel132; a am force-stop $PKG; widget_add rel132 && { go_home 3; log "WIDGET v1.2.32: $(wstate)"; shot update/rel-01-widget-132; wcount rel-before >/dev/null; log "widget ids before: [$(wids)]"; }
      sec "UA2. Update from the app's dialog → the system installer → v1.2.33"
      upd_flow rel 1500183 || ok=no
    fi
  fi
  if [ $ok != yes ]; then
    sec "UB0. Built v1.2.32 (tag, x86_64) with the staged update.json → the built v1.2.33 (a830b51, versionCode 1500183)"
    curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
    log "staged manifest: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["size"])' $OUT/update.json 2>&1)"
    inst app-base-upd.apk base-upd apk-base-upd-ref.txt || return
    a appops set $PKG REQUEST_INSTALL_PACKAGES allow
    launch >/dev/null; sleep 6; dismiss; ready; tab catalog; waitfor "$RX_RESULTS" 150; tab home; sleep 3
    K=b132; a am force-stop $PKG; widget_add b132 && { go_home 3; log "WIDGET built v1.2.32: $(wstate)"; shot update/b-01-widget-132; }
    upd_flow built 1500183
  fi
  diag_end
}

# ---------------------------------------------------------------- 7. УИДЖЕТ + ЕЗИК
part_widget2() {
  helper_install
  anims 1
  part_widget
  sec "WL. App language (in-app Settings and cmd) and system language (framework restart): widget and app"
  a am force-stop $PKG
  local ids; ids=$(wcount wl-start)
  log "widgets of ours before the language part: $ids"
  sys_locale en-US
  launch >/dev/null; sleep 5; dismiss; ready
  CFG=lang; DENS=$(dens); dims; : > $OUT/a11y-lang.txt
  cap 01-sysEN-appBG-home 1
  app_locale_ui English; ready; tab home; cap 02-sysEN-appEN-home 2; tab more; cap 03-sysEN-appEN-more 2
  go_home 3; wlabels 04-sysEN-appEN
  sys_locale bg-BG
  launch >/dev/null; sleep 5; ready; tab home; cap 05-sysBG-appEN-home 2
  go_home 3; wlabels 06-sysBG-appEN
  launch >/dev/null; sleep 3; ready; app_locale_ui 'Български'; ready; tab home; cap 07-sysBG-appBG-home 2
  go_home 3; wlabels 08-sysBG-appBG
  diag_end
}

# ---------------------------------------------------------------- 8. БЪРЗОДЕЙСТВИЕ
dexopt_state() { a dumpsys package dexopt 2>/dev/null | tr -d '\r' | grep -A6 "\[$PKG\]" | grep -oE '(arm64|arm|x86_64|x86): \[status=[^]]*\]' | head -2 | tr '\n' ' '; }
starts20() {  # $1 — етикет
  local i
  : > $OUT/start-$1-warm.txt
  for i in $(seq 1 20); do cold_drawn $1; done
  for i in $(seq 1 20); do a input keyevent KEYCODE_HOME; sleep 3; launch | tee -a $OUT/start-$1-warm.txt | tr '\n' ' '; echo; sleep 2; done
  python3 $H_/parse.py starts $OUT/drawn-$1.txt "cold $1" | tee -a $SUM
  drawn_sum $1
  python3 $H_/parse.py starts $OUT/start-$1-warm.txt "warm $1" | tee -a $SUM
}
part_perf() {
  local i JCY
  anims 1
  sec "P0. Package: $(cat apk-ref.txt); APK on the device $(a "stat -c %s \$(pm path $PKG | head -1 | cut -d: -f2)" 2>/dev/null | tr -d '\r') bytes; dexopt: $(dexopt_state)"
  log "baseline profile in the APK: $(unzip -l app.apk 2>/dev/null | grep -oE 'assets/dexopt/baseline\.prof[m]?' | tr '\n' ' ')"
  warmup
  for i in 1 2; do cold_drawn excluded; done
  sec "P1. 20 cold (with 'Fully drawn') + 20 warm starts, as installed (dexopt: $(dexopt_state))"
  starts20 installed
  sec "P2. After 'cmd package compile -m speed-profile -f' (as the idle background dexopt would): 20 + 20"
  log "compile: $(a cmd package compile -m speed-profile -f $PKG 2>&1 | tr -d '\r' | tail -1); dexopt now: $(dexopt_state)"
  for i in 1 2; do cold_drawn excluded2; done
  starts20 speedprofile
  sec "P3. Offline cold starts ×6 (cached content)"
  offline; for i in $(seq 1 6); do cold_drawn offline; done; drawn_sum offline; online; sleep 5
  sec "P4. gfxinfo per main scenario (animations ON, ${W}x${H})"
  restart; tab home; sleep 30
  jank_scn home-scroll rel
  dump; JCY=$(python3 $W15 big $OUT/ui.xml $PKG 2>/dev/null | awk '{print int(($2+$4)/2)}'); [ -n "$JCY" ] && jank_scn carousel rel
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; jank_scn news rel
  open_article && { sleep 2; jank_scn article rel; }
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar; jank_scn calendar rel
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 2; jank_scn catalog rel
  tap content-desc "$L_SHELF" >/dev/null && { sleep 3; jank_scn shelf rel; tap content-desc "$L_LIST" >/dev/null; sleep 1; }
  ready; tab my; sleep 3; jank_scn my rel
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30; then jank_scn gallery rel
    if open_album 1 && wait_cells 15; then jank_scn album-grid rel
      if open_photo 1; then sleep 2; gfx_reset; for i in $(seq 1 15); do sw_left 150; sleep 0.8; done; gfx_save rel-viewer; back 1.5; fi; back 1.5; fi; back 1.5; fi
  if open_settings; then jank_scn settings rel; back 1.5; fi
  ready; dims; dump; declare -A TXY; local t
  for t in home catalog my news more; do TXY[$t]=$(xy --tab "$(tablabel $t)" "$( [ $t = more ] && echo '=Home|=Начало' || echo '=More|=Още')"); done
  gfx_reset; for i in 1 2 3; do for t in catalog my news more home; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 1.2; done; done; gfx_save rel-tab-switch
  link "$SITE/elektronen-katalog" >/dev/null; waitfor "$RX_RESULTS" 60; sleep 2; dump
  local p; p=$(python3 $H_/find.py $OUT/ui.xml --first-clickable $(results_top) $((H*85/100)) 150)
  [ -n "$p" ] && { gfx_reset; for i in 1 2 3 4 5; do a input tap $p; sleep 2.5; back 2; done; gfx_save rel-book-open-x5; }
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 1
  gfx_reset; for i in 1 2 3; do tap content-desc "$L_SURPRISE" >/dev/null; sleep 4; tap content-desc "$L_CLOSE" >/dev/null || back 1.5; sleep 1.5; done; gfx_save rel-surprise-x3
  if card_open; then sleep 1; gfx_reset; sleep 10; gfx_save rel-card-idle-0-10s; gfx_reset; tilt_seq; gfx_save rel-card-tilt; back 1.5; fi
  sec "P4b. Totals"
  grep -E '^GFX ' $SUM | sort -k2 | tee $OUT/gfx-totals.txt
  sec "P5. Diagnostics › „Бързодействие“ (the app's own numbers)"
  perf_card filled; back 1.5; back 1.5
  sec "P6. Memory after the scenarios"
  mem after-scenarios
  diag_end
}

# ---------------------------------------------------------------- 9. CPU БЕЗ ДОКОСВАНЕ
idle_at() {  # $1 — етикет: 30 s след отварянето, после 15 s прозорец
  sleep 30; dump; shot idle/$1; cpu_win $1 15
}
part_idle() {
  anims 1
  warmup
  card_open >/dev/null; back 1.5
  sec "I. CPU and frames with no input: 30 s after opening each screen, then a 15 s window (should be ~0 frames, ~0% CPU)"
  a am force-stop $PKG; sleep 3; launch >/dev/null; sleep 2; dismiss; ready; tab home; idle_at home
  goto2 catalog; idle_at catalog
  tap content-desc "$L_SHELF" >/dev/null && { idle_at catalog-shelf; tap content-desc "$L_LIST" >/dev/null; sleep 1; }
  goto2 book; idle_at book; leave
  goto2 surprise; idle_at surprise-result; tap content-desc "$L_CLOSE" >/dev/null || back 1.5; leave
  goto2 search; idle_at home-search-expanded; leave
  goto2 my; idle_at my
  goto2 card; idle_at card; tap text "$L_CARDSHOW" >/dev/null && { idle_at card-full; back 1.5; }; leave
  goto2 want; idle_at want; leave
  goto2 news; idle_at news
  goto2 article; idle_at article; leave
  goto2 calendar; idle_at calendar
  goto2 event; idle_at event; leave
  goto2 more; idle_at more
  goto2 gallery; idle_at gallery; leave
  goto2 album; idle_at album; leave
  goto2 viewer; idle_at viewer; leave
  goto2 settings; idle_at settings; leave
  goto2 diagnostics; idle_at diagnostics; leave
  goto2 contacts; idle_at contacts; leave
  goto2 documents; idle_at documents; leave
  ready; tab home; sleep 1; go_home 3; cpu_win background 30
  sec "I2. Screens that kept drawing (frames > 0 in the 15 s window)"
  grep -E '^IDLE ' $SUM | grep -vE 'frames \+0;' | cut -c1-200 | tee -a $SUM
  diag_end
}

# ---------------------------------------------------------------- 10. ДОСТЪПНОСТ С ТЕСТОВАТА УСЛУГА
part_a11yt() {
  helper_install
  anims 1
  warmup
  card_open >/dev/null; back 1.5
  sec "AT. Test accessibility service with touch exploration (TalkBack stand-in): what a screen reader gets"
  a11y_on; touchx true
  ready; tab home; sleep 4; adump home
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 1; adump catalog
  tap content-desc "$L_SURPRISE" >/dev/null && { sleep 4; adump surprise; tap content-desc "$L_CLOSE" >/dev/null || back 1.5; sleep 1; }
  link "$BOOK_URL" >/dev/null; sleep 4; adump book
  tap content-desc "$L_SHARE" >/dev/null && { sleep 2; adump book-share-sheet; back 1.5; }
  back 1.5
  open_article && { adump article; sw_up 300; sleep 1.5; adump article-scrolled; tap content-desc "$L_READER" >/dev/null && { sleep 2; adump reader-sheet; back 1.5; }; back 1.5; }
  open_event 1 && { adump event; back 1.5; }
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30 && open_album 1 && open_photo 1; then sleep 2; adump viewer; back 1.5; back 1.5; back 1.5; fi
  card_open >/dev/null && { adump card; back 1.5; }
  open_settings && { adump settings; back 1.5; }
  touchx false; a11y_off
  sec "AT2. Unlabelled / tiny targets as the service sees them (all trees)"
  for f in $OUT/a11ytree-*.txt; do echo "== $(basename $f .txt)"; python3 $W16 tree $f | grep -E 'UNLABELLED|unlabelled|TREE|clickable' | head -6; done 2>/dev/null | tee -a $SUM | head -80
  diag_end
}

# ---------------------------------------------------------------- 11. ЖИВИ ИЗВЕСТИЯ (36 / 36.1)
part_live2() {
  part_live36
  log "SYSTEMUI flags (chips/promoted): $(a aflags list 2>/dev/null | grep -iE 'chip|promot|rich_ongoing|live_update' | tr -s ' ' | head -14 | cut -c1-120 | tr '\n' ';')"
  sec "L4. Live Update evidence: raw notification records during the download, the app's notification settings"
  local f
  for f in $(ls $OUT/live/notif-l*.txt 2>/dev/null | sort -V | sed -n '2p;5p;9p'); do echo "== $(basename $f)"; python3 $W20 notifraw $f $PKG | head -30; done | tee -a $SUM
  grep -hiE 'promot' $OUT/live/notif-l2.txt 2>/dev/null | sort | uniq -c | sort -rn | head -12 | cut -c1-220 | sed 's/^/  any promot line: /' | tee -a $SUM
  log "PERMISSION lines: $(a dumpsys package $PKG | tr -d '\r' | grep -iE 'PROMOTED' | tr -s ' ' | tr '\n' ';' | cut -c1-400)"
  log "appops: $(a appops get $PKG 2>/dev/null | tr -d '\r' | grep -iE 'promot|LIVE' | tr '\n' ';')"
  a am start -a android.settings.APP_NOTIFICATION_SETTINGS --es android.provider.extra.APP_PACKAGE $PKG >/dev/null 2>&1; sleep 3; dump; shot live/20-app-notification-settings
  log "APP NOTIFICATION SETTINGS: $(texts 30 | cut -c1-700)"
  sw_up 500; sleep 1.5; dump; shot live/21-app-notification-settings-2; log "  (scrolled): $(texts 30 | cut -c1-500)"
  back 1.5
}

# ---------------------------------------------------------------- 12. API 28
part_old28() {
  part_fb28
  sec "O. Tour on API $SDK (device language bg-BG via the helper)"
  CFG=api$SDK; tour api$SDK-light
  python3 $W20 a11yagg $OUT/a11y-*.txt | head -30 | tee -a $SUM
  sec "O2. Rotation and process death on API $SDK (book, catalogue)"
  robx book; robx catalog
  diag_end
}

part_w20() {
  case "$W20_PARTS" in *" $PART "*) ;; *) return 1;; esac
  case $PART in
    first) part_first;;
    tour-a|tour-b|tour-c) part_tour; diag_end;;
    demo) part_demo;;
    func) part_func;;
    func2) part_func2;;
    robust) part_robust;;
    offline) part_offline; diag_end;;
    monkey) part_monkey2;;
    update) part_update133;;
    widget) part_widget2;;
    perf) part_perf;;
    idle) part_idle;;
    mem) part_mem; diag_end;;
    a11yt) part_a11yt;;
    live36|live361|live361b) part_live2;;
    old28) part_old28;;
    upd2) part_upd2;;
    fix) part_fix;;
    demo2) part_demo2;;
    old28b) part_old28b;;
  esac
  return 0
}

# ================================================================= КРЪГ b: повторни и по-точни проверки
# Обновяване 1.2.32 → 1.2.33 без „По-късно“: уиджетът преди първото пускане, диалогът се приема.
upd_wait_dialog() {  # $1 — етикет; пропуска въведението и известията, НЕ натиска „По-късно“
  local i t0; t0=$(now_ms)
  for i in $(seq 1 60); do
    sleep 2; dump
    has 'text="(Нова версия|New version)' && { log "UPDATE $1 dialog after $(( $(now_ms) - t0 )) ms: $(texts 14 | cut -c1-500)"; return 0; }
    has 'text="(Пропусни|Skip)"' && tap text '=Пропусни|=Skip' >/dev/null
    has 'text="(Не сега|Not now)"' && tap text '=Не сега|=Not now' >/dev/null
  done
  log "UPDATE $1: no dialog in 120 s: $(texts 10 | cut -c1-300)"; return 1
}
part_upd2() {
  local p
  helper_install
  anims 1
  sec "UC0. Built v1.2.32 (tag 87ef20e, x86_64, versionCode 1495920) with update.json → built v1.2.33 (1500183)"
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  log "staged manifest: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["size"], len(d["notes"].split(chr(10))), "note lines")' $OUT/update.json 2>&1)"
  inst app-base-upd.apk base-upd apk-base-upd-ref.txt || return
  a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  sec "UC1. The widget on the home screen before the first launch of v1.2.32"
  K=c132; widget_add c132 && { go_home 3; log "WIDGET v1.2.32 (not launched yet): $(wstate)"; shot update/c-01-widget-132-before-launch; }
  sec "UC2. First launch of v1.2.32: onboarding skipped, the update dialog accepted"
  launch >/dev/null
  if upd_wait_dialog c; then
    shot update/c-10-dialog; cp $OUT/ui.xml $OUT/dialog-c.xml; notes_match c-dialog
    p=$(xy text '=Инсталирай|=Install'); [ -z "$p" ] && p=$(xy text '=Изтегли и инсталирай|=Download and install')
    [ -n "$p" ] && a input tap $p
    if installer_confirm 1500183 c; then
      log "UPDATE c: installed $(ver)"; sleep 3
      sec "UC3. After the update: widget before reopening, „Какво ново“, widget after"
      go_home 3; log "WIDGET after the update (before opening): $(wstate)"; shot update/c-20-widget-after
      launch >/dev/null; sleep 3
      whatsnew_check c-30-whats-new && { a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*40/100)) 400; sleep 1.5; shot update/c-31-whats-new-scrolled; dump; notes_match c-sheet-scrolled; tap text '=Добре|=OK|=Разбрах|=Got it' >/dev/null || back 2; }
      sleep 2; a am force-stop $PKG; launch >/dev/null; sleep 5; dump; log "relaunch: what's new again? $(has "$RX_WHATSNEW" && echo 'SHOWN AGAIN' || echo no)"
      go_home 3; log "WIDGET after opening the app: $(wstate)"; shot update/c-40-widget-after-app
      K=c133; widget_clicks
    else log "UPDATE c: NOT installed: $(ver)"; fi
  fi
  sec "UD. 'По-късно' on the update dialog: what the app offers afterwards (Settings › Обновления)"
  inst app-base-upd.apk base-upd2 apk-base-upd-ref.txt; a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  launch >/dev/null
  if upd_wait_dialog d; then tap text '=По-късно|=Later' >/dev/null; sleep 2
    a am force-stop $PKG; launch >/dev/null; sleep 8; dump; log "after 'По-късно' + relaunch: dialog $(has 'text="(Нова версия|New version)' && echo SHOWN || echo 'not shown')"
    if open_settings && settings_find 'Налична е версия|Провери за нова версия|Обновления'; then dump; shot update/d-settings; log "SETTINGS › Обновления: $(grep -oE 'text="[^"]*(версия|Обнов|автомат)[^"]*"' $OUT/ui.xml | head -5 | tr '\n' ' ')"
      tap text 'Налична е версия' >/dev/null || tap text 'Провери за нова версия' >/dev/null; sleep 4; dump; shot update/d-after-check
      log "after 'check now': dialog $(has 'text="(Нова версия|New version)' && echo SHOWN || echo 'not shown'); $(texts 8 | cut -c1-200)"; fi
  fi
  diag_end
}
eshot() {  # $1 — име: снимка от емулатора (дисплеят отвън — и на екраните с FLAG_SECURE)
  local d; d=$(realpath -m $OUT/shots/eshot); mkdir -p $d; rm -f $d/Screenshot*.png
  timeout 20 adb emu screenrecord screenshot $d >/dev/null 2>&1; sleep 1
  local f; f=$(ls -t $d/Screenshot*.png 2>/dev/null | head -1)
  [ -n "$f" ] && mv "$f" $d/$1.png && echo "$d/$1.png"
}
# Картата: сензорът и фолиото — пълният dumpsys sensorservice и наклон и с жироскопа
card_tilt2() {  # $1 — етикет
  local f=$OUT/sensors-$1.txt
  card_screen; dump; card_shown || { card_add $CARD_NO >/dev/null 2>&1; card_screen; dump; }
  sleep 3; a dumpsys sensorservice > $f 2>&1
  log "SENSORS on the card screen ($1): active connections mentioning the app: $(grep -c "$PKG" $f); lines: $(grep -E "$PKG|Game Rotation|game_rotation" $f | tr -s ' ' | head -6 | cut -c1-160 | tr '\n' '~')"
  local E=$OUT/shots/eshot
  eshot $1-0 >/dev/null; sleep 1.5; eshot $1-0b >/dev/null
  emu sensor set acceleration 4.5:8.7:0 >/dev/null; sleep 2.5; eshot $1-acc-A >/dev/null
  emu sensor set acceleration -4.5:8.7:0 >/dev/null; sleep 2.5; eshot $1-acc-B >/dev/null
  emu sensor set acceleration 0:9.81:0 >/dev/null
  emu sensor set gyroscope 0:0.9:0 >/dev/null; sleep 1.2; emu sensor set gyroscope 0:0:0 >/dev/null; sleep 1.5; eshot $1-gyro-A >/dev/null
  emu sensor set gyroscope 0:-1.8:0 >/dev/null; sleep 1.2; emu sensor set gyroscope 0:0:0 >/dev/null; sleep 1.5; eshot $1-gyro-B >/dev/null
  emu sensor set gyroscope 0.9:0:0 >/dev/null; sleep 1.2; emu sensor set gyroscope 0:0:0 >/dev/null; sleep 1.5; eshot $1-gyro-C >/dev/null
  log "ESHOT files: $(ls $E 2>/dev/null | tr '\n' ' '); size of $1-0: $(python3 $W19 png $E/$1-0.png 2>&1 | cut -c1-120)"
  mkdir -p $OUT/shots/tilt2; for f in 0 0b acc-A acc-B gyro-A gyro-B gyro-C; do [ -f $E/$1-$f.png ] && python3 -c "from PIL import Image; import sys; Image.open(sys.argv[1]).convert('RGB').resize(($W,$H)).save(sys.argv[2])" $E/$1-$f.png $OUT/shots/tilt2/$1-$f.png 2>/dev/null; done
  local R="0 250 $W 1250"
  log "TILT2 $1 noise 0→0b (same tilt, 1.5 s): $(python3 $W19 rdiff $OUT/shots/tilt2/$1-0.png $OUT/shots/tilt2/$1-0b.png "$R" 2>&1 | tail -1)"
  log "TILT2 $1: acc A→B $(python3 $W19 rdiff $OUT/shots/tilt2/$1-acc-A.png $OUT/shots/tilt2/$1-acc-B.png "$R"); 0→accA $(python3 $W19 rdiff $OUT/shots/tilt2/$1-0.png $OUT/shots/tilt2/$1-acc-A.png "$R"); gyro A→B $(python3 $W19 rdiff $OUT/shots/tilt2/$1-gyro-A.png $OUT/shots/tilt2/$1-gyro-B.png "$R"); gyro B→C $(python3 $W19 rdiff $OUT/shots/tilt2/$1-gyro-B.png $OUT/shots/tilt2/$1-gyro-C.png "$R"); 0→gyroA $(python3 $W19 rdiff $OUT/shots/tilt2/$1-0.png $OUT/shots/tilt2/$1-gyro-A.png "$R")"
  python3 $W20 strip $OUT/shots/tilt2/$1-strip.png "$R" $OUT/shots/tilt2/$1-{0,acc-A,acc-B,gyro-A,gyro-B,gyro-C}.png >/dev/null
  a dumpsys sensorservice > $OUT/sensors-$1-after.txt 2>&1
  log "SENSOR events for the app (recent): $(grep -iE "$PKG" -A3 $OUT/sensors-$1-after.txt | grep -iE 'rotation|events|rate' | head -4 | tr -s ' ' | cut -c1-160 | tr '\n' '~')"
  back 1.5
}
article_toolbar2() {
  local u
  sec "AR2. Article toolbar: the longest article, slow drags; screenshots of the bottom"
  ART_URL=$(python3 $H_/find_article.py long | cut -f1); log "longest article: $ART_URL"
  open_article || { log "AR2: article not opened"; return; }
  shot article2/0-open
  a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*40/100)) 700; sleep 2; dump; shot article2/1-drag-down
  log "AR2 after a slow 700 ms drag down (720 px): toolbar $(has 'content-desc="Режим за четене"' && echo VISIBLE || echo hidden)"
  a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*30/100)) 250; sleep 2; dump; shot article2/2-fling-down
  log "AR2 after a fling down: toolbar $(has 'content-desc="Режим за четене"' && echo VISIBLE || echo hidden)"
  a input swipe $((W/2)) $((H*40/100)) $((W/2)) $((H*55/100)) 500; sleep 2; dump; shot article2/3-drag-up
  log "AR2 after a drag up: toolbar $(has 'content-desc="Режим за четене"' && echo visible || echo HIDDEN)"
  back 2
}
year_frames() {  # devDebug: снимка на всяка ~1 s след докосването на картата на годината (до 20 s)
  local i t1
  year_card_find || { log "YEARF $1: no card"; return; }
  tap text "$L_YEAR" >/dev/null; t1=$(now_ms)
  local pid; pid=$(pidof_app)
  ( for k in 3 7 11; do sleep 4; a "debuggerd -j $pid" > $OUT/year-stack-$1-$k.txt 2>&1; done ) & local stpid=$!
  for i in $(seq 1 18); do shot year2/$1-$(printf %02d $i)s >/dev/null; while [ $(( $(now_ms) - t1 )) -lt $((i * 1000 + 1000)) ]; do sleep 0.1; done; done
  python3 $W20 strip $OUT/shots/year2/$1-strip.png all $OUT/shots/year2/$1-{01,02,03,04,05,06,08,10,12,14,16,18}s.png >/dev/null
  wait $stpid 2>/dev/null
  for k in 3 7 11; do log "YEAR STACK $1 +~${k}s: $(grep -E '^"(main|DefaultDispatcher[^"]*)"' -A14 $OUT/year-stack-$1-$k.txt 2>/dev/null | grep -E '^"|at org\.chyavorec|kotlinx\.serialization|json|Json|Reader|Search|Index|sqlite|SQLite|okhttp|Socket|sleep|park|delay' | grep -vE 'parking to wait|- waiting' | head -16 | cut -c1-140 | tr '\n' '~')"; done
  log "YEARF $1 frame stats per second: $(python3 $W20 frames $OUT/shots/year2/$1-*s.png | awk '{sub(/std=/,"",$3); printf "%s ", $3}')"
}
part_fix() {
  local i
  helper_install
  anims 1
  sec "FX0. BrandIntro on the very first launch: 12 s recording (round a stopped at ~5 s, on the splash)"
  a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1; timeout 180 adb install app.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS; go_home 2
  rec_start brandintro2 12; launch >/dev/null; rec_end >/dev/null
  python3 $W20 strip $OUT/shots/first/brandintro-12s-strip.png all $(ls $OUT/rec/brandintro2/f*.png 2>/dev/null | awk 'NR>=12 && NR%3==0' | head -16) >/dev/null
  log "BRANDINTRO2: $(ls $OUT/rec/brandintro2 2>/dev/null | wc -l) frames; motion: $(python3 $W19 motion all $OUT/rec/brandintro2/f*.png 2>/dev/null | cut -c1-500)"
  rec_clean
  dismiss; warmup
  sec "FX4. Pull-to-refresh rosette: slow 1500 ms pull starting inside the list (45% of the height)"
  link_pkg "$SITE/news" >/dev/null; sleep 5; segstate | grep -q 'News\[checked=true' || segment news; pull_refresh news2 45
  link_pkg "$EVENTS_URL" >/dev/null; sleep 5; cal_checked || segment calendar; pull_refresh calendar2 45
  ready; tab home; sleep 3; pull_refresh home2 45
  if open_gallery && waitfor "$RX_GALLERY_LOADED" 30; then pull_refresh gallery2 45; back 1.5; fi
  link_pkg "$SITE/news" >/dev/null; sleep 4; top_of
  rec_start refresh-news2 6; a input swipe $((W/2)) $((H*45/100)) $((W/2)) $((H*92/100)) 1500; sleep 3; rec_end "0 0 1080 1400"; rec_clean
  sec "FX1. Card foil and the sensor (self-entered card)"
  sensors_info; card_tilt2 release
  card_screen; tap text "$L_CARDSHOW" >/dev/null && { sleep 2; card_tilt2 release-full; }
  article_toolbar2
  sec "FX2. Catalogue filter 'Само налични' applied"
  ready; tab catalog; waitfor "$RX_RESULTS" 60; search_catalog 12 >/dev/null; log "before: $(rcount) results"
  if tap content-desc "$L_FILTERS" >/dev/null; then sleep 2; tap text '=Само налични' >/dev/null; sleep 1.5; dump; shot fix/filter-1
    log "FILTER sheet after the chip: $(texts 30 | cut -c1-500)"
    back 1.5; sleep 2; dump; shot fix/filter-2; log "after closing the sheet: $(rcount) results; $(grep -oE 'text="[^"]*(налични|Филтри|филт)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' ')"; fi
  sec "FX3. Shelf view vs list: gfxinfo of the same flings"
  ready; tab catalog; waitfor "$RX_RESULTS" 30; sleep 2; gfx_reset; fling_v; gfx_save list-flings
  tap content-desc "$L_SHELF" >/dev/null && { sleep 3; gfx_reset; fling_v; gfx_save shelf-flings; gfx_reset; sleep 8; gfx_save shelf-idle-8s; tap content-desc "$L_LIST" >/dev/null; }
  diag_end
}
part_demo2() {
  helper_install
  use_pkg org.chyavorec.app.dev
  timeout 180 adb install -r app-dev.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  anims 1
  warmup
  demo_signin
  sec "Y2b. Year story: what is on the screen during the first 18 s (screenshot each second)"
  a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 5; dismiss; ready; tab my; sleep 3
  year_frames fresh
  back 2; sleep 1; year_story_closed || back 2; ready; tab my; sleep 2; top_of
  year_frames again
  sec "Y6b. Short flick variants (the 22% / 90 ms flick stayed open in round a)"
  local spec dur y1 y2 lbl i
  for spec in "flick90:40:62:90" "flick150:40:62:150" "flick90b:40:62:90" "short300:40:62:300" "fast200:30:85:200"; do
    IFS=: read lbl y1 y2 dur <<< "$spec"
    year_story_closed && { ready; tab my; sleep 1.5; top_of; year_card_find && tap text "$L_YEAR" >/dev/null; for i in $(seq 1 25); do sleep 1; dump && grep -qE 'content-desc="[^"]*(Затвори|Следващ слайд)' $OUT/ui.xml && break; done; sleep 1.5; }
    a input swipe $((W/2)) $((H*y1/100)) $((W/2)) $((H*y2/100)) $dur; sleep 2.5; shot year2/swipe-$lbl
    log "YEAR swipe down $lbl (${y1}%→${y2}% = $((H*(y2-y1)/100)) px in ${dur} ms): $(year_story_closed && echo closed || echo 'STILL OPEN')"
  done
  year_story_closed || back 2
  sec "Y4b. Demo card: foil and the sensor"
  card_tilt2 demo
  sec "Y3b. Hold result"
  link "$SITE/elektronen-katalog?inv=$INV" >/dev/null; sleep 4; dump; for i in 1 2; do [ -n "$(xy text '=Заяви книгата')" ] && break; sw_up 400; sleep 1; dump; done
  if [ -n "$(xy text '=Заяви книгата')" ]; then tap text '=Заяви книгата' >/dev/null; for i in 1 2 3 4 5 6; do sleep 0.7; shot fix/hold-$i >/dev/null; done; dump; log "HOLD after the tap: $(texts 20 | cut -c1-500)"; fi
  diag_end
  use_pkg org.chyavorec.app
}
part_old28b() {
  helper_install; dev_locale bg-BG || true; a am force-stop $PKG
  anims 1
  warmup
  sec "O28. Event share as image on API $SDK (round a: no choice sheet)"
  local n
  for n in 1 2; do
    open_event $n || continue
    dump; log "event $n: share button $(xy content-desc '=Сподели')"; shot share28/event-$n-0
    tap content-desc '=Сподели' >/dev/null; sleep 3; dump; shot share28/event-$n-1; log "after tapping 'Сподели': $(texts 10 | cut -c1-300); fg $(fg)"
    has 'Сподели като картинка' && { share_image ev28-$n; break; }
    back 1.5; back 1.5
  done
  sec "O28b. Home on API $SDK: frames with no input after 30 s (no AGSL; carousel?)"
  restart; tab home; sleep 30; cpu_win home-30s 15
  diag_end
}

# ---------------------------------------------------------------- издаденият APK на x86_64 образа
relprobe() {  # издаденият APK (само ARM) на x86_64: инсталира ли се и тръгва ли (превод на ARM)? → app.apk
  case " live36 live361 live361b update old28 upd2 old28b " in *" $PART "*) log "RELEASE APK: not used in this part (built x86_64 app.apk)"; return;; esac
  [ -f rel-133.apk ] || { log "RELEASE APK: rel-133.apk missing — built x86_64 app.apk"; return; }
  [ "$SDK" -ge 30 ] || { log "RELEASE APK: API $SDK has no ARM translation — built x86_64 app.apk"; return; }
  log "abilist: $(a getprop ro.product.cpu.abilist | tr -d '\r'); native bridge: $(a getprop ro.dalvik.vm.native.bridge | tr -d '\r')"
  timeout 60 adb uninstall $PKG >/dev/null 2>&1
  local r pid ul
  r=$(timeout 180 adb install rel-133.apk 2>&1 | tr -d '\r' | tail -1)
  if ! echo "$r" | grep -q Success; then log "RELEASE APK install: $r → built x86_64 app.apk"; return; fi
  a cmd locale set-app-locales $PKG --locales bg-BG
  adb logcat -c; a am start -W -n $ACT >/dev/null 2>&1; sleep 10
  pid=$(a pidof $PKG | tr -d '\r'); ul=$(timeout 20 adb logcat -d 2>/dev/null | grep -cE 'UnsatisfiedLinkError|FATAL EXCEPTION')
  log "RELEASE APK probe: $r; $(a dumpsys package $PKG | tr -d '\r' | grep -m1 -oE 'primaryCpuAbi=[^ ]+'); pid '$pid'; FATAL/UnsatisfiedLink lines: $ul"
  a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1
  if [ -n "$pid" ] && [ "$ul" = 0 ]; then
    cp app.apk app-built.apk; cp rel-133.apk app.apk
    echo "1.2.33 RELEASED APK (GitHub release asset chitalishte-yavorets-1.2.33.apk, arm64-v8a/armeabi-v7a via ARM translation); versionCode 1500183" > apk-ref.txt
    log "RELEASE APK: USED for this part"
  else log "RELEASE APK: does not run here → built x86_64 app.apk"; fi
}
