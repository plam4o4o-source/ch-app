# Частите на вълна 15 (ВРЕМЕННО, debug/wave15-check). Зарежда се от run.sh преди „main“ и
# ползва неговите помощни функции (a, dump, xy, tap, log, sec, shot…).
W15=$H_/w15.py
dens() { a wm density | tail -1 | grep -oE '[0-9]+$'; }
mark() { a log -t PERFCHECK "'$1'"; sleep 0.5; }
# Редовете от logcat след маркера $1, които съвпадат с $2
since() { sed -n "/PERFCHECK.*$1/,\$p" $OUT/logcat-live.txt 2>/dev/null | grep -iE "$2" | grep -v PERFCHECK; }
gms_info() {
  echo "GMS $(a dumpsys package com.google.android.gms 2>/dev/null | grep -m1 versionName | tr -d '\r ')" \
    "; Play Store $(a pm list packages 2>/dev/null | grep -c 'com.android.vending')" \
    "; Google accounts $(a dumpsys account 2>/dev/null | tr -d '\r' | grep -cE 'Account \{name=.*type=com.google')"
}

# ================================================================= СКЕНЕР (Google code scanner)
RX_MANUAL_PANEL='text="(ISBN или инвентарен номер|ISBN or inventory number)"'
scan_state() {
  local f k=other
  f=$(fg); dump
  if echo "$f" | grep -qiE 'com.google.android.gms|codescanner|mlkit' || has 'package="com.google.android.gms"'; then k=GOOGLE-SCANNER-UI
  elif has 'text="Скенерът не е достъпен"'; then k=APP-UNAVAILABLE
  elif has 'text="Сканирай баркода на книгата"'; then k=APP-INTRO
  elif ! has "package=\"$PKG\""; then k="OTHER-WINDOW"
  fi
  echo "$k; fg $f; manual panel $(has "$RX_MANUAL_PANEL" && echo OPEN || echo closed); explanation $(has 'идва с Google Play услугите' && echo SHOWN || echo absent); download/install text $(grep -qiE '(изтегл|download|install|инсталир)' $OUT/ui.xml && echo yes || echo no); 'Сканирай баркод' $(has 'text="Сканирай баркод"' && echo yes || echo no); 'Опитай отново' $(has 'text="Опитай отново"' && echo yes || echo no); 'Въведи номера на ръка' $(has 'text="Въведи номера на ръка"' && echo yes || echo no); texts: $(texts 10 | cut -c1-300)"
}
scan_watch() {  # $1 — етикет: състоянието ~2, 5, 10, 20 s след отварянето
  local t prev=0
  for t in 2 5 10 20; do sleep $((t - prev)); prev=$t; shot scan/$1-${t}s; log "SCAN $1 +${t}s: $(scan_state)"; done
}
scan_back() {  # „Назад“, докато сме в скенера на Google или в панела „на ръка“ (до 3 пъти)
  local i
  for i in 1 2 3; do
    dump
    if has 'package="com.google.android.gms"' || has "$RX_MANUAL_PANEL" || ! has "package=\"$PKG\""; then back 2.5; log "SCAN Back #$i → $(scan_state)"; else break; fi
  done
}
scan_manual() {  # $1 — етикет
  tap text '=Въведи номера на ръка' || { log "SCAN: no 'Въведи номера на ръка' button: $(texts 8 | cut -c1-200)"; debug_ui scan-manual-$1; return 1; }
  sleep 2; dump; log "SCAN manual panel: open $(has "$RX_MANUAL_PANEL" && echo yes || echo NO); explanation $(has 'идва с Google Play услугите' && echo SHOWN || echo absent)"
  a input text $INV; sleep 1.5; tap text '=Търси' --last; sleep 4; dump; shot scan/$1-manual-result
  log "SCAN manual $INV: $(has 'text="Отвори книгата"' && echo 'BOOK SHEET with Отвори книгата' || echo 'NO book sheet'); $(texts 10 | cut -c1-260)"
  if tap text '=Отвори книгата'; then sleep 4; dump; shot scan/$1-book; log "SCAN → book: $(has "$RX_BACK" && echo opened || echo 'NOT opened'); $(texts 3 | cut -c1-120)"; back 2; fi
}
part_scan() {
  warmup
  sec "S0. Package: permissions (no CAMERA); Google Play services on the image"
  a dumpsys package $PKG | tr -d '\r' > $OUT/dumpsys-package.txt
  log "requested permissions: $(sed -n '/requested permissions:/,/install permissions:/p' $OUT/dumpsys-package.txt | grep -oE '[A-Za-z0-9_.]+\.permission\.[A-Z0-9_]+' | sort -u | tr '\n' ' ')"
  log "CAMERA in dumpsys package: $(grep -c 'android.permission.CAMERA' $OUT/dumpsys-package.txt) line(s)"
  local AAPT; AAPT=$(ls -d ${ANDROID_HOME:-/usr/local/lib/android/sdk}/build-tools/*/ 2>/dev/null | sort -V | tail -1)aapt2
  log "APK uses-permission (aapt2): $($AAPT dump permissions app.apk 2>&1 | grep -oE "name='[^']+'" | tr '\n' ' ' | cut -c1-500)"
  log "$(gms_info)"
  log "ML Kit meta-data in the manifest: $($AAPT dump xmltree --file AndroidManifest.xml app.apk 2>/dev/null | grep -A2 'mlkit.vision.DEPENDENCIES' | grep -oE '"barcode_ui"|Raw: "[^"]*"' | head -1)"
  sec "S1. Scan from Home (API $SDK, camera: $(a getprop ro.kernel.qemu.camera_back 2>/dev/null | tr -d '\r')$(a dumpsys media.camera 2>/dev/null | grep -m1 -oE 'Number of camera devices: [0-9]+'))"
  restart; tab home; sleep 3; dims; dump
  mark m-scan-home
  tap content-desc '=Сканирай баркод|=Scan barcode' || tap text '=Сканирай|=Scan' || { log "SCAN: no scan entry on Home: $(texts 12 | cut -c1-200)"; debug_ui scan-home; }
  scan_watch 10-home
  sec "S2. Back / cancel"
  scan_back; shot scan/20-after-back; log "SCAN after Back/cancel: $(scan_state)"
  sec "S3. Manual entry $INV"
  scan_manual 30
  sec "S4. Scan from the launcher shortcut (scan), ~60 s later (the module may have downloaded)"
  sleep 40; a am force-stop $PKG; mark m-scan-shortcut; scanner
  scan_watch 40-shortcut
  scan_back; shot scan/45-shortcut-after-back; log "SCAN (shortcut) after Back: $(scan_state)"
  sec "S5. The button on the screen again"
  if tap text '=Сканирай баркод|=Опитай отново'; then scan_watch 50-again; scan_back; fi
  grep -iE 'ModuleInstall|codescanner|barcode|mlkit|vision|Chimera|GmsBarcode|optional.?module|dynamite' $OUT/logcat-live.txt | grep -v PERFCHECK > $OUT/logcat-scanner.txt
  log "logcat scanner/module lines: $(wc -l < $OUT/logcat-scanner.txt); first: $(head -12 $OUT/logcat-scanner.txt | cut -c1-200 | tr '\n' '~')"
  log "ML Kit registrar errors in the app process: $(grep -c 'Could not instantiate com.google.mlkit' $OUT/logcat-live.txt) line(s): $(grep -m2 -oE 'NoSuchMethodException: com.google.mlkit[^ ]*' $OUT/logcat-live.txt | sort -u | tr '\n' ' ')"
  log "Play services module/scanner calls after the first scan: $(since m-scan-home 'ModuleInstall|barcode\.ui|codescanner|GmsBarcode|mlkit\.barcode' | wc -l) line(s): $(since m-scan-home 'ModuleInstall|barcode\.ui|codescanner|GmsBarcode|mlkit\.barcode' | head -6 | cut -c1-200 | tr '\n' '~')"
  log "activities started since the first scan: $(since m-scan-home 'ActivityTaskManager: START' | grep -oE 'cmp=[^ }]+' | sort | uniq -c | tr -s ' ' | tr '\n' ' ')"
  case $PART in
    scan33) part_misc;;
    scan34) f_login15;;
  esac
}

# ================================================================= ПРЕКИ ПЪТИЩА И „КОНТАКТИ“
shortcuts_list() {  # $1 — етикет: преките пътища на приложението (dumpsys shortcut)
  a dumpsys shortcut 2>/dev/null | tr -d '\r' > $OUT/shortcut-$1.txt
  awk -v p="Package: $PKG" 'index($0, p) {f=1; print; next} f && /(Package|Launcher): / {f=0} f' $OUT/shortcut-$1.txt > $OUT/shortcut-$1-app.txt
  grep -oE 'ShortcutInfo \{id=[^,]+|shortLabel=[^,]+|flags=0x[0-9a-f]+ \[[^]]*\]' $OUT/shortcut-$1-app.txt | sed 's/ShortcutInfo {/ |/' | tr '\n' ' '
}
launcher_popup() {  # $1 — етикет: задържане на иконата в списъка с приложения
  local i p
  go_home 2; dims
  a input swipe $((W/2)) $((H*85/100)) $((W/2)) $((H*20/100)) 300; sleep 2.5
  for i in 1 2 3 4 5 6 7 8; do dump; p=$(xy text '=НЧ Яворец|=Yavorets'); [ -n "$p" ] && break; sw_up 500; sleep 1.2; done
  [ -n "$p" ] || { log "LAUNCHER $1: app icon not found in All apps"; debug_ui launcher-$1; go_home 2; return 1; }
  lpress ${p% *} ${p#* } 1300; sleep 2; dump; shot shortcuts/$1-popup; cp $OUT/ui.xml $OUT/launcher-$1.xml
  log "LAUNCHER long-press $1: $(python3 $H_/find.py $OUT/ui.xml --texts | grep -vE '^(НЧ Яворец|App info|Информация за приложението|Pause app|Widgets|Install|Split screen)$' | head -12 | tr '\n' '|' | cut -c1-300)"
  log "LAUNCHER $1: Events $(grep -qE 'text="(Събития|Events)"' $OUT/ui.xml && echo PRESENT || echo absent)"
  back 1.5; go_home 2
}
contacts_row() {  # $1 — етикет: бутоните за действие на „Контакти“
  local i
  link "$SITE/kontakti"; sleep 5; dims
  for i in 1 2 3 4 5; do dump; has 'content-desc="Обади се на читалището"' && break; sw_up 400; sleep 1.5; done
  shot contacts/$1; cp $OUT/ui.xml $OUT/contacts-$1.xml
  python3 $W15 rows $OUT/ui.xml '^(Обади се на читалището|Изпрати имейл на читалището|Покажи читалището на картата|Отвори сайта на читалището)' $(dens) > $OUT/contacts-$1.txt
  log "CONTACTS $1: $(tail -1 $OUT/contacts-$1.txt); $(head -4 $OUT/contacts-$1.txt | tr -s ' ' | tr '\n' ' ' | cut -c1-400)"
  back 2
}
part_misc() {
  sec "SC1. Shortcuts before a book is opened"
  log "SHORTCUTS start: $(shortcuts_list 0)"
  sec "SC2. A book from the public catalogue → 'last book' shortcut; launcher long-press"
  link "$BOOK_URL"; sleep 6; dump; log "book: $(texts 3 | cut -c1-120)"; back 2; sleep 2
  log "SHORTCUTS after the book: $(shortcuts_list 1)"
  launcher_popup 10-after-book
  sec "SC3. Settings → 'Изчисти временните данни' → the last-book shortcut is removed"
  launch >/dev/null; sleep 3
  if open_settings && settings_item '=Изчисти временните данни|=Clear temporary data'; then
    sleep 2; dump; shot shortcuts/20-clear-dialog; log "clear dialog: $(texts 6 | cut -c1-200)"
    tap text '=Изчисти|=Clear' --last; sleep 4; dump; log "after clearing: $(texts 4 | cut -c1-160)"
  fi
  log "SHORTCUTS after clearing: $(shortcuts_list 2)"
  launcher_popup 30-after-clear
  sec "CT. Contacts: short action buttons in one row; stacked at a large font"
  launch >/dev/null; sleep 3; ready
  contacts_row 40-standard
  if set_text_size '=Extra large|=Много голям'; then
    a settings put system font_scale 1.3; sleep 3; restart
    contacts_row 41-font130-xlarge
    a settings put system font_scale 1.0; sleep 2; set_text_size '=Standard|=Стандартен'
  fi
}

# ================================================================= ВХОД
f_login15() {
  local t p d t0
  sec "LG1. Sign-in on a fresh install (password-manager flag unset): no credential query, no crash (API $SDK)"
  log "$(gms_info); credential providers: $(a cmd package query-services --brief -a android.service.credentials.CredentialProviderService 2>&1 | tr -d '\r' | grep -E '/' | tr '\n' ' ' | cut -c1-200)"
  ready; tab my; sleep 3; dims; dump
  mark m-login-open
  tap text '=Вход|=Sign in' || { log "LOGIN: no 'Вход' button: $(texts 10 | cut -c1-200)"; debug_ui login; return; }
  for t in 2 6; do sleep $t; dump; log "LOGIN +${t}s: fg $(fg); $(texts 10 | cut -c1-260)"; done
  cp $OUT/ui.xml $OUT/login-open.xml
  log "LOGIN credential traffic after open: [$(since m-login-open 'CredentialManager|GetCredential|credman|PasswordAndPasskey|CredentialProvider|identity\.credentials|auth\.api\.credentials' | head -6 | cut -c1-220 | tr '\n' '~')]"
  log "LOGIN activities started after open: [$(since m-login-open 'ActivityTaskManager: START' | grep -oE 'cmp=[^ }]+' | sort | uniq -c | tr -s ' ' | tr '\n' ' ')]"
  sec "LG2. Card number field"
  p=$(xy --all class EditText | sed -n 1p)
  if [ -n "$p" ]; then a input tap $p; sleep 1; a input text 'ab-123'; sleep 1.5; dump; log "LOGIN card field after typing 'ab-123': $(python3 $W15 field $OUT/ui.xml 0)"; fi
  sec "LG3. PIN (OutlinedSecureTextField): the last digit shows briefly; the eye toggle"
  dump; p=$(xy --all class EditText | sed -n 2p)
  if [ -n "$p" ]; then
    a input tap $p; sleep 1
    for d in 1 2 3 4; do
      a input text $d; t0=$(now_ms); dump; log "PIN after '$d' +$(( $(now_ms) - t0 )) ms: $(python3 $W15 field $OUT/ui.xml 1)"
      sleep 2.5; dump; log "PIN after '$d' +$(( $(now_ms) - t0 )) ms: $(python3 $W15 field $OUT/ui.xml 1)"
    done
    if tap content-desc '=Покажи ПИН-а|=Show PIN'; then sleep 1.5; dump; log "PIN eye ON: $(python3 $W15 field $OUT/ui.xml 1); toggle now: $(grep -oE 'content-desc="(Скрий ПИН-а|Покажи ПИН-а)"' $OUT/ui.xml)"; fi
    if tap content-desc '=Скрий ПИН-а|=Hide PIN'; then sleep 1.5; dump; log "PIN eye OFF: $(python3 $W15 field $OUT/ui.xml 1); toggle now: $(grep -oE 'content-desc="(Скрий ПИН-а|Покажи ПИН-а)"' $OUT/ui.xml)"; fi
  else log "PIN field not found"; debug_ui pin; fi
  sec "LG4. 'Запомни ме' row: one labelled checkbox"
  hide_ime; dump; cp $OUT/ui.xml $OUT/login-remember-before.xml
  python3 $W15 checkables $OUT/ui.xml | sed 's/^/before: /' | tee -a $SUM
  log "'Запомни ме' text nodes: $(grep -c 'Запомни ме на това устройство' $OUT/ui.xml)"
  if tap text 'Запомни ме на това устройство'; then sleep 1.5; dump; python3 $W15 checkables $OUT/ui.xml | sed 's/^/after tap on the text: /' | tee -a $SUM; fi
  python3 $H_/a11y.py $OUT/ui.xml $(dens) $W $H login-15 | tee -a $SUM
  back 2; back 2; dump; log "LOGIN after Back: $(texts 4 | cut -c1-120); pid '$(a pidof $PKG | tr -d '\r')'"
  tap text '=Вход|=Sign in' && { mark m-login-open2; sleep 5; dump; log "LOGIN second open: fg $(fg); credential traffic: [$(since m-login-open2 'CredentialManager|GetCredential|credman|PasswordAndPasskey' | head -3 | cut -c1-200 | tr '\n' '~')]"; back 2; }
}
part_login() { warmup; f_login15; }

# ================================================================= ПЛОЧКА В БЪРЗИТЕ НАСТРОЙКИ
TILE=$PKG/.tile.CardTileService
RX_TILE='Читателска карта|Library card'
qs_tile() {  # $1 — етикет: отваря бързите настройки и намира плочката; TP — центърът ѝ (без подчерупка!)
  local i
  a cmd statusbar expand-settings; sleep 3; dims; TP=""
  # Панелът помни последната страница — първо назад до първата (плъзгане надясно).
  for i in 1 2 3 4; do a input swipe $((W*15/100)) $((H*30/100)) $((W*85/100)) $((H*30/100)) 300; sleep 1; done
  for i in 1 2 3 4; do
    dump; cp $OUT/ui.xml $OUT/qs-$1-page$i.xml
    TP=$(xy content-desc "$RX_TILE"); [ -z "$TP" ] && TP=$(xy text "$RX_TILE")
    [ -n "$TP" ] && break
    a input swipe $((W*85/100)) $((H*30/100)) $((W*15/100)) $((H*30/100)) 300; sleep 1.5
  done
  log "QS $1: tile @ ${TP:-NOT FOUND} (page $i): $(python3 $H_/find.py $OUT/ui.xml --toggle "$RX_TILE" | head -2 | cut -c1-600 | tr '\n' ' ') texts: $(grep -oE '(text|content-desc)="(Читателска карта|Library card|Добави картата|Покажи на гишето|Add card|Show at the desk)[^"]*"' $OUT/ui.xml | tr '\n' ' ')"
  log "QS $1: ours in sysui_qs_tiles: $(a settings get secure sysui_qs_tiles | tr -d '\r' | grep -c 'chyavorec')"
}
qs_add() {  # плочката в бързите настройки: cmd statusbar add-tile, иначе в sysui_qs_tiles
  log "sysui_qs_tiles before: $(a settings get secure sysui_qs_tiles | tr -d '\r')"
  log "add-tile $TILE: '$(a cmd statusbar add-tile $TILE 2>&1 | tr -d '\r' | head -2 | tr '\n' ' ')'"; sleep 3
  if ! a settings get secure sysui_qs_tiles | grep -q chyavorec; then
    log "add-tile did not change sysui_qs_tiles — adding custom(...) to the setting (fallback)"
    local v; v="custom($TILE),$(a settings get secure sysui_qs_tiles | tr -d '\r')"
    a "settings put secure sysui_qs_tiles '$v'"; sleep 3
  fi
  log "sysui_qs_tiles after: $(a settings get secure sysui_qs_tiles | tr -d '\r')"
}
qs_remove() {
  log "remove-tile: '$(a cmd statusbar remove-tile $TILE 2>&1 | tr -d '\r' | head -1)'; ours in sysui_qs_tiles: $(a settings get secure sysui_qs_tiles | grep -c chyavorec)"
  if a settings get secure sysui_qs_tiles | grep -q chyavorec; then
    local v; v=$(a settings get secure sysui_qs_tiles | tr -d '\r' | sed -E 's/custom\([^)]*chyavorec[^)]*\),?//; s/,$//')
    a "settings put secure sysui_qs_tiles '$v'"; sleep 2
    log "removed via the setting: ours now $(a settings get secure sysui_qs_tiles | grep -c chyavorec)"
  fi
}
card_screen() { a am start -a org.chyavorec.app.SHORTCUT -d chyavorec://app/my/card -n $ACT >/dev/null 2>&1; sleep 4; }
card_add() {  # $1 — номер на картата
  local p
  card_screen; dump
  p=$(xy --all class EditText | sed -n 1p)
  [ -n "$p" ] || { log "CARD: no card-number field: $(texts 8 | cut -c1-200)"; debug_ui card-add; return 1; }
  a input tap $p; sleep 1; a input text "$1"; sleep 1; hide_ime; tap text '=Запази|=Save'; sleep 3; dump
  log "CARD added '$1': $(texts 10 | cut -c1-260)"
}
card_shown() { has 'TEST-?12345|ЧИТАТЕЛСКА КАРТА|Баркод на карта'; }
part_tile() {
  warmup
  sec "T0. The tile service"
  log "QS_TILE services: $(a cmd package query-services --brief -a android.service.quicksettings.action.QS_TILE 2>&1 | tr -d '\r' | grep -i chyavorec | tr '\n' ' ')"
  sec "T1. No card: added with 'cmd statusbar add-tile' → inactive"
  qs_add
  qs_tile no-card; shot tile/10-qs-no-card
  sec "T2. Tap without a card (unlocked)"
  if [ -n "$TP" ]; then a input tap $TP; else log "tile not on screen — 'cmd statusbar click-tile' instead: '$(a cmd statusbar click-tile $TILE 2>&1 | tr -d '\r' | head -1)'"; fi
  sleep 4; dump; shot tile/11-after-tap-no-card; log "after tap (no card): fg $(fg); app $(has "package=\"$PKG\"" && echo IN FRONT || echo no); $(texts 8 | cut -c1-240)"
  back 2; a cmd statusbar collapse
  sec "T3. Card-screen prompt 'Добави в бързите настройки' (API $SDK; the tile removed first)"
  qs_remove
  card_add TEST-12345
  dump; shot tile/19-card-screen; log "CARD screen: card $(card_shown && echo shown || echo NO); prompt $(has 'text="Добави в бързите настройки"' && echo SHOWN || echo absent); dismiss X $(has 'content-desc="Не предлагай отново"' && echo yes || echo no)"
  if tap text '=Добави в бързите настройки'; then
    sleep 3; dump; shot tile/20-system-add-dialog; cp $OUT/ui.xml $OUT/tile-add-dialog.xml; log "system dialog: fg $(fg); $(texts 8 | cut -c1-300)"
    tap text '=Add tile|=Добавяне на панела|=Добавяне' || tap text 'Add tile'; sleep 3
    dump; log "after 'Add tile': prompt $(has 'text="Добави в бързите настройки"' && echo 'STILL SHOWN' || echo gone); fg $(fg)"; shot tile/21-after-add
  fi
  sec "T4. With a card: active"
  log "after the prompt: ours in sysui_qs_tiles $(a settings get secure sysui_qs_tiles | grep -c chyavorec)"
  a settings get secure sysui_qs_tiles | grep -q chyavorec || { log "the tile is not in Quick Settings after the prompt — added for the next steps"; qs_add; }
  go_home 2   # картата е FLAG_SECURE — без нея отдолу снимката на панела не е черна
  qs_tile with-card; shot tile/30-qs-with-card
  sec "T5. Tap (unlocked) → the card screen"
  if [ -n "$TP" ]; then a input tap $TP; else log "tile not on screen — click-tile: '$(a cmd statusbar click-tile $TILE 2>&1 | tr -d '\r' | head -1)'"; fi
  sleep 4; dump; shot tile/31-after-tap; log "after tap (card): fg $(fg); card screen $(card_shown && echo SHOWN || echo NO); $(texts 8 | cut -c1-240)"
  back 2; go_home 2
  sec "T6. Locked with a PIN: tap the tile → the unlock prompt first"
  log "set-pin: $(a locksettings set-pin 1234 2>&1 | tr -d '\r' | head -1)"
  a input keyevent KEYCODE_POWER; sleep 3; a input keyevent KEYCODE_WAKEUP; sleep 3
  dump; shot tile/40-lockscreen; log "lock screen: $(a dumpsys window 2>/dev/null | tr -d '\r' | grep -m2 -oE '(mKeyguardShowing|isKeyguardShowing|KeyguardShowing|mShowingLockscreen)=\w+' | tr '\n' ' '); $(texts 6 | cut -c1-200)"
  qs_tile locked; shot tile/41-qs-locked
  if true; then
    if [ -n "$TP" ]; then a input tap $TP; else log "tile not on screen — click-tile: '$(a cmd statusbar click-tile $TILE 2>&1 | tr -d '\r' | head -1)'"; fi
    sleep 3; dump; shot tile/42-after-tap-locked; cp $OUT/ui.xml $OUT/tile-locked-tap.xml
    log "after tap while LOCKED: fg $(fg); PIN entry $(grep -qiE 'PIN' $OUT/ui.xml && echo SHOWN || echo absent); app in front $(has "package=\"$PKG\"" && echo YES || echo no); $(texts 8 | cut -c1-240)"
    a input text 1234; a input keyevent 66; sleep 5; dump; shot tile/43-after-unlock
    log "after entering the PIN: fg $(fg); card screen $(card_shown && echo SHOWN || echo NO); $(texts 6 | cut -c1-200)"
  fi
  log "clear PIN: $(a locksettings clear --old 1234 2>&1 | tr -d '\r' | head -1)"
}

# ================================================================= ИЗВЕСТИЯ
app_jobs() { a dumpsys jobscheduler 2>/dev/null | tr -d '\r' | grep -oE "JOB #u[0-9]+a[0-9]+/[0-9]+: [0-9a-f]+ $PKG/" | sed -E 's|^JOB #[^/]+/([0-9]+):.*|\1|' | sort -un | tr '\n' ' '; }
run_jobs() { local j; for j in $(app_jobs); do log "jobscheduler run -f $j: $(a cmd jobscheduler run -f $PKG $j 2>&1 | tr -d '\r' | head -1)"; done; }
notifs() { a dumpsys notification --noredact 2>/dev/null | tr -d '\r' > $OUT/notif-$1.txt; python3 $W15 notif $OUT/notif-$1.txt $PKG; }
shade_expand() {  # $1 — регулярен израз за текст в нашето известие, $2 — етикет
  local p y e
  a cmd statusbar expand-notifications; sleep 3; dims; dump; shot notif/$2-0-shade
  p=$(xy text "$1"); [ -n "$p" ] || { log "SHADE $2: '$1' not in the shade: $(texts 14 | cut -c1-300)"; return 1; }
  y=${p#* }
  if ! grep -qiE 'text="(Маршрут|Добави в календара|Сподели)"' $OUT/ui.xml; then
    e=$(python3 $H_/find.py $OUT/ui.xml --all resource-id 'expand_button' | awk -v y=$y '{d=$2-y; if (d<0) d=-d; if (b=="" || d<bd) {b=$0; bd=d}} END{print b}')
    if [ -n "$e" ]; then a input tap $e; else a input swipe ${p% *} $y ${p% *} $((y + 600)) 400; fi
    sleep 2.5; dump
  fi
  shot notif/$2-1-expanded; cp $OUT/ui.xml $OUT/shade-$2.xml
  log "SHADE $2 expanded: buttons [$(grep -oiE 'text="(Маршрут|Добави в календара|Сподели)"' $OUT/ui.xml | tr '\n' ' ')]; big picture view $(has 'resource-id="android:id/big_picture"' && echo yes || echo no); $(texts 16 | cut -c1-320)"
}
notif_action() {  # $1 — надпис на бутона, $2 — етикет
  dump; tap text "=$1" || { log "ACTION '$1': no such button in the shade"; return 1; }
  sleep 5; dump; shot notif/$2; log "ACTION '$1' → fg $(fg); $(texts 6 | cut -c1-200)"
  go_home 2
}
event_reminder_set() {
  local i j p ok=no
  link_pkg "$EVENTS_URL"; sleep 5; cal_checked || segment calendar
  dump; has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък'; sleep 2; }
  shot notif/30-events
  for i in 1 2 3 4; do
    dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $((H/4)) $((H*85/100)) 150 | sed -n ${i}p)
    [ -n "$p" ] || break
    a input tap $p; sleep 4; dump; log "EVENT candidate $i: $(texts 6 | cut -c1-200)"
    for j in 1 2 3; do [ -n "$(xy text '=Напомни ми')" ] && break; sw_up 400; sleep 1; dump; done
    if tap text '=Напомни ми'; then
      sleep 1.5; dump; shot notif/31-reminder-set-$i; ok=yes
      log "REMINDER set on: $(texts 2 | cut -c1-120); snackbar: $(grep -oE 'text="Ще получиш напомняне[^"]*"' $OUT/ui.xml | head -1); bell now: $(has 'Премахни напомнянето' && echo on || echo '?'); image on the event screen: $(grep -cE 'class="android.widget.ImageView"' $OUT/ui.xml)"
    fi
    back 2
  done
  log "REMINDER: $ok"
}
work_rewind() {  # PeriodicSyncWorker: last_enqueue_time − 7 h (с flex 2 из 6 ч първото пускане е ~4 ч след добавянето)
  local D uid f
  D=/data/data/$PKG/no_backup; a ls $D/androidx.work.workdb >/dev/null 2>&1 || D=/data/data/$PKG/databases
  a am force-stop $PKG; sleep 2
  log "WorkManager db: $(a ls -l $D 2>&1 | tr -d '\r' | grep -E 'androidx.work' | tr -s ' ' | tr '\n' ' ')"
  rm -f $OUT/androidx.work.workdb*
  for f in androidx.work.workdb androidx.work.workdb-wal androidx.work.workdb-shm; do timeout 20 adb pull $D/$f $OUT/$f >/dev/null 2>&1; done
  python3 - $OUT/androidx.work.workdb <<'EOF' | tee -a $SUM
import sqlite3, sys
c = sqlite3.connect(sys.argv[1])
try:
    for r in c.execute("select id, worker_class_name, state, period_count, last_enqueue_time, interval_duration, flex_duration from workspec"):
        print('WORKSPEC', r[1].split('.')[-1], 'state', r[2], 'periods', r[3], 'last_enqueue', r[4], 'interval', r[5], 'flex', r[6])
    n = c.execute("update workspec set last_enqueue_time = last_enqueue_time - 7*3600*1000 where worker_class_name like '%PeriodicSyncWorker'").rowcount
    c.commit()
    print('WORK REWIND: PeriodicSyncWorker rows moved 7 h back:', n)
except Exception as e:
    print('WORK REWIND failed:', e, [r[0] for r in c.execute("pragma table_info(workspec)")])
c.execute('pragma wal_checkpoint(TRUNCATE)'); c.execute('pragma journal_mode=DELETE'); c.close()
EOF
  uid=$(a stat -c %u /data/data/$PKG | tr -d '\r')
  a rm -f $D/androidx.work.workdb-wal $D/androidx.work.workdb-shm
  timeout 30 adb push $OUT/androidx.work.workdb $D/androidx.work.workdb >/dev/null 2>&1
  a chown $uid:$uid $D/androidx.work.workdb; a chmod 600 $D/androidx.work.workdb; a restorecon $D/androidx.work.workdb
}
part_notif() {
  local uid
  sec "N0. Handlers on the image; settings seeded before the first launch (last_news_id = a stale id)"
  log "calendar (INSERT event) handlers: [$(a cmd package query-activities --brief -a android.intent.action.INSERT -t vnd.android.cursor.dir/event 2>&1 | tr -d '\r' | grep '/' | tr '\n' ' ' | cut -c1-200)]"
  log "geo handlers: [$(a cmd package query-activities --brief -a android.intent.action.VIEW -d 'geo:0,0?q=Yavorets' 2>&1 | tr -d '\r' | grep '/' | tr '\n' ' ' | cut -c1-200)]"
  python3 $W15 prefs-write $OUT/settings.preferences_pb last_news_id=s:perfcheck-stale-id | tee -a $SUM
  uid=$(a stat -c %u /data/data/$PKG | tr -d '\r')
  a mkdir -p /data/data/$PKG/files/datastore
  timeout 30 adb push $OUT/settings.preferences_pb /data/data/$PKG/files/datastore/settings.preferences_pb >/dev/null 2>&1
  a chown -R $uid:$uid /data/data/$PKG/files; a chmod 700 /data/data/$PKG/files /data/data/$PKG/files/datastore
  a chmod 600 /data/data/$PKG/files/datastore/settings.preferences_pb; a restorecon -R /data/data/$PKG/files
  log "seeded: $(a ls -lZ /data/data/$PKG/files/datastore 2>&1 | tr -d '\r' | tail -1)"
  warmup
  sec "N1. New-article notification (PeriodicSyncWorker; the stored newest id is stale)"
  log "app jobs: $(app_jobs)"; sleep 20; notifs n1-natural | tee -a $SUM
  if ! grep -q 'Нова публикация' $OUT/notif-n1-natural.txt; then run_jobs; sleep 30; notifs n1-forced | tee -a $SUM; fi
  if ! grep -q 'Нова публикация' $OUT/notif-n1-forced.txt 2>/dev/null && ! grep -q 'Нова публикация' $OUT/notif-n1-natural.txt; then
    log "(the periodic work has a flex window: its first run is ~4 h after it is enqueued — moving it back in the WorkManager db)"
    work_rewind
    launch >/dev/null; sleep 8; dismiss; log "app jobs after the rewind: $(app_jobs)"; run_jobs; sleep 35; notifs n1-rewound | tee -a $SUM
  fi
  timeout 20 adb pull /data/data/$PKG/files/datastore/settings.preferences_pb $OUT/settings-after.pb >/dev/null 2>&1
  log "settings after the sync: $(python3 $W15 prefs-dump $OUT/settings-after.pb 2>&1 | grep -E 'last_news_id|bg_step|notify_news|last_background' | tr '\n' ' ')"
  sec "N2. Shade: the new-article notification expanded"
  if shade_expand 'Нова публикация' 20-news; then notif_action 'Сподели' 21-news-share; back 1.5; fi
  a cmd statusbar collapse; sleep 1
  sec "N3. Event reminder: 'Напомни ми' on an upcoming event, then the work forced"
  launch >/dev/null; sleep 3; ready
  event_reminder_set
  log "app jobs: $(app_jobs)"; run_jobs; sleep 25; notifs n3 | tee -a $SUM
  sec "N4. Shade: the event reminder expanded; its buttons"
  if shade_expand 'Напомняне за събитие' 40-event; then
    notif_action 'Маршрут' 41-route
    shade_expand 'Напомняне за събитие' 42-event-again && notif_action 'Добави в календара' 43-calendar
  fi
  a cmd statusbar collapse; sleep 1
  notifs n-end > /dev/null
}

# ================================================================= ГАЛЕРИЯ
gcount() { echo "not-loaded=$(grep -cE 'content-desc="[^"]*([Сс]нимката не се зареди|Не се зареди)"' $OUT/ui.xml) missing=$(grep -cE 'content-desc="[^"]*([Сс]нимката липсва)"' $OUT/ui.xml) retry-icons=$(grep -c 'content-desc="Опитай отново"' $OUT/ui.xml) cells=$(photo_cells | wc -l)"; }
vstate() {  # $1 — етикет: състоянието на прегледа
  dump; shot gallery/$1
  log "VIEWER $1: counter '$(grep -oE 'text="[0-9]+ / [0-9]+[^"]*"' $OUT/ui.xml | head -1)'; close button $(has 'content-desc="Затвори"' && echo shown || echo HIDDEN); album grid $([ $(photo_cells | wc -l) -gt 0 ] && echo VISIBLE || echo no); bars: $(a dumpsys window windows 2>/dev/null | tr -d '\r' | grep -A60 "$PKG/" | grep -m1 -oiE '(mRequestedVisibilities|requestedVisibleTypes|mRequestedVisibleTypes)=[^ ]*' ); $(python3 $W15 bbox $OUT/shots/gallery/$1.png 2>&1)"
}
part_gallery() {
  local i p t prev cx cy y
  if [ "$SDK" -ge 34 ]; then
    sec "G0. API $SDK: predictive back animations on (developer option) before the framework (re)starts"
    a settings put global enable_back_animation 1; a setprop persist.wm.debug.predictive_back 1
    a stop; sleep 3; a start
    for i in $(seq 1 60); do sleep 3; [ "$(a getprop init.svc.bootanim | tr -d '\r')" = stopped ] && a pm path android >/dev/null 2>&1 && break; done
    sleep 25; a input keyevent KEYCODE_WAKEUP; a wm dismiss-keyguard 2>/dev/null; sleep 2; go_home 3
    log "enable_back_animation=$(a settings get global enable_back_animation | tr -d '\r') persist.wm.debug.predictive_back=$(a getprop persist.wm.debug.predictive_back | tr -d '\r')"
  fi
  warmup
  sec "G1. Album 1 online (the album data is cached); then the image caches are emptied"
  restart; open_gallery && waitfor "$RX_GALLERY_LOADED" 40; open_album 1; wait_cells 20; sleep 6; dims; dump; shot gallery/01-album-online
  log "ALBUM online: $(gcount); 404 tiles: $(grep -oE 'content-desc="[^"]*— снимката липсва"' $OUT/ui.xml | head -3 | tr '\n' ' ')"
  back 1.5; back 1.5
  a am force-stop $PKG; sleep 1
  log "app cache before: $(a du -sk /data/data/$PKG/cache/image_cache /data/data/$PKG/cache/http 2>&1 | tr -d '\r' | tr '\n' ' ')"
  a rm -rf /data/data/$PKG/cache/image_cache /data/data/$PKG/cache/http
  sec "G2. Offline: thumbnails show 'Не се зареди' (not 'Снимката липсва')"
  offline; launch >/dev/null; sleep 5; ready; open_gallery; sleep 4; dump; log "gallery offline: $(texts 6 | cut -c1-200)"
  open_album 1; sleep 6; dump; shot gallery/02-album-offline; cp $OUT/ui.xml $OUT/album-offline.xml
  log "ALBUM offline: $(gcount); sample: $(grep -oE 'content-desc="[^"]*(не се зареди|липсва)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' ')"
  sw_up 500; sleep 3; dump; shot gallery/03-album-offline-scrolled; log "ALBUM offline scrolled: $(gcount)"
  sec "G3. Reconnect without touching: the thumbnails recover"
  online; prev=0
  for t in 5 15 30; do sleep $((t - prev)); prev=$t; dump; log "ALBUM +${t}s after reconnect: $(gcount)"; done
  shot gallery/04-album-recovered
  for i in 1 2 3 4; do sw_down 300; sleep 0.5; done; sleep 4; dump; shot gallery/05-album-top-recovered; log "ALBUM top after reconnect: $(gcount)"
  sec "G4. A real 404 photo: '<title> — снимката липсва'; the viewer on it"
  for i in 1 2 3 4 5; do dump; has '— снимката липсва"' && break; sw_up 400; sleep 3; done
  log "404 tiles: $(grep -oE 'content-desc="[^"]*— снимката липсва"' $OUT/ui.xml | head -4 | tr '\n' ' '); not-loaded here: $(grep -oE 'content-desc="[^"]*не се зареди"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  p=$(xy content-desc '— снимката липсва')
  if [ -n "$p" ]; then
    shot gallery/10-404-tile; a input tap $p; sleep 5; dump; shot gallery/11-viewer-404
    log "VIEWER on a 404 photo: $(viewer_page); 'Снимката липсва на сайта' $(has 'Снимката липсва на сайта' && echo SHOWN || echo absent); retry $([ -n "$(xy text '=Опитай отново')" ] && echo SHOWN || echo absent); $(texts 6 | cut -c1-200)"
    back 1.5
  else log "G4: no 404 tile found"; debug_ui g404; fi
  for i in 1 2 3 4 5 6; do sw_down 300; sleep 0.5; done
  sec "G5. Viewer: a tap hides and shows the bars (immersive)"
  open_photo 1; sleep 3; vstate 20-viewer-start
  a input tap $((W/2)) $((H/2)); sleep 2; vstate 21-after-tap1
  a input tap $((W/2)) $((H/2)); sleep 2; vstate 22-after-tap2
  sec "G6. Vertical drag: a short one returns, a long one dismisses"
  mdrag $((W/2)) $((H*45/100)) $((W/2)) $((H*52/100)) 0.1; sleep 2; vstate 30-after-short-drag
  a input swipe $((W/2)) $((H*35/100)) $((W/2)) $((H*85/100)) 220; sleep 2.5; vstate 31-after-long-drag
  sec "G7. Zoom still works: double tap 2×, pinch, pan; no dismiss while zoomed; reset"
  [ $(photo_cells | wc -l) -gt 0 ] && { open_photo 1; sleep 3; }
  dims; cx=$((W/2)); cy=$((H/2))
  if mt_init; then
    shot gallery/40-1x
    dtap $cx $cy; sleep 2.5; shot gallery/41-2x-doubletap
    pinch $cx $cy 300 900 16; sleep 3; shot gallery/42-pinched
    log "ZOOM: diff 1x→2x $(python3 $W15 diff $OUT/shots/gallery/40-1x.png $OUT/shots/gallery/41-2x-doubletap.png), 2x→pinched $(python3 $W15 diff $OUT/shots/gallery/41-2x-doubletap.png $OUT/shots/gallery/42-pinched.png) (≈0 = no change)"
    a input swipe $((W/2)) $((H*35/100)) $((W/2)) $((H*75/100)) 300; sleep 2; vstate 43-vertical-drag-while-zoomed
    sw_left 300; sleep 1.5; log "swipe while zoomed: $(viewer_page) (same photo = panned)"
    dtap $cx $cy; sleep 2.5; shot gallery/44-reset; log "ZOOM reset: diff 1x vs after reset $(python3 $W15 diff $OUT/shots/gallery/40-1x.png $OUT/shots/gallery/44-reset.png)"
  else log "ZOOM: no multi-touch device"; fi
  sec "G8. Back gesture (gesture navigation; API $SDK): the photo scales during the gesture"
  nav_gestures
  [ "$SDK" -ge 34 ] && log "predictive back animation setting: $(a settings put global enable_back_animation 1; a settings get global enable_back_animation | tr -d '\r')"
  [ $(photo_cells | wc -l) -gt 0 ] && { open_photo 1; sleep 3; }
  dims; y=$((H/2)); vstate 60-before-gesture
  a input motionevent DOWN 2 $y; a input motionevent MOVE 60 $y; a input motionevent MOVE 200 $y; a input motionevent MOVE $((W*30/100)) $y
  sleep 0.6; shot gallery/61-gesture-30pct
  a input motionevent MOVE $((W*50/100)) $y; sleep 0.6; shot gallery/62-gesture-50pct
  a input motionevent MOVE $((W*70/100)) $y; sleep 0.6; shot gallery/62b-gesture-70pct
  for f in 60-before-gesture 61-gesture-30pct 62-gesture-50pct 62b-gesture-70pct; do log "PHOTO EDGES $f (row y=$((H*62/100))): $(python3 $W15 hspan $OUT/shots/gallery/$f.png $((H*62/100)))"; done
  a input motionevent MOVE 80 $y; a input motionevent MOVE 10 $y; a input motionevent UP 10 $y; sleep 2; vstate 63-after-cancelled-gesture
  bgesture gallery/64-full-gesture-midway; log "full gesture midway: $(python3 $W15 bbox $OUT/shots/gallery/64-full-gesture-midway.png)"; vstate 65-after-full-gesture
  nav_buttons
}

# ================================================================= НАЧАЛО
hm_loading() {
  local p i
  sec "HS0. Search right after a fresh install (catalogue index not built yet): loading indicator"
  inst app.apk new apk-ref.txt || return
  launch >/dev/null; sleep 4; dismiss; ready; tab home; sleep 1; dims; dump
  p=$(xy text 'Търси книга|Search books'); [ -n "$p" ] || { log "HS0: no search field"; return; }
  a input tap $p; sleep 1; a input text 12
  for i in 1 2 3 4 5 6; do
    dump; [ $i = 1 ] && shot home/s-00-first-search
    log "HS0 dump $i: loading indicator $(has 'content-desc="Зареждане…"' && echo SHOWN || echo absent); results $(has '12 -те вятъра|text="Книги"' && echo yes || echo no); empty-state $(has 'Нищо не открихме' && echo SHOWN || echo no)"
  done
  back 1.5; back 1.5
}
hm_newarr() {  # $1 — етикет: заглавията в „Нови постъпления“
  local i p y
  restart; tab home; sleep 4; dims
  for i in 1 2 3 4 5 6 7 8; do
    dump; p=$(xy text '=Нови постъпления|=New arrivals')
    [ -n "$p" ] && [ ${p#* } -lt $((H*55/100)) ] && break
    a input swipe $((W/2)) $((H*70/100)) $((W/2)) $((H*40/100)) 500; sleep 1.5
  done
  [ -n "$p" ] || { log "NEW ARRIVALS $1: section not found"; debug_ui newarr-$1; return; }
  y=${p#* }; shot home/$1-new-arrivals-0; cp $OUT/ui.xml $OUT/newarr-$1-0.xml
  log "NEW ARRIVALS $1 [0]: $(python3 $W15 section $OUT/ui.xml '^(Нови постъпления|New arrivals)$' | tr '\n' ' ' | cut -c1-900)"
  for i in 1 2 3 4; do
    a input swipe $((W*85/100)) $((y + 300)) $((W*15/100)) $((y + 300)) 400; sleep 1.5; dump; cp $OUT/ui.xml $OUT/newarr-$1-$i.xml
    log "NEW ARRIVALS $1 [$i]: $(python3 $W15 section $OUT/ui.xml '^(Нови постъпления|New arrivals)$' | tr '\n' ' ' | cut -c1-900)"
  done
  shot home/$1-new-arrivals-end
}
hm_events() {  # $1 — етикет: кога се пълни „Предстоящи събития“ при студен старт (висок екран)
  local i t0 got s
  a wm size 1080x4800; sleep 3
  for i in 1 2 3; do
    a am force-stop $PKG; sleep 2
    a am start -n $ACT -a android.intent.action.MAIN -c android.intent.category.LAUNCHER >/dev/null 2>&1; t0=$(now_ms); got=-1; s=""
    while [ $(( $(now_ms) - t0 )) -lt 15000 ]; do
      dump; s=$(python3 $W15 hsections $OUT/ui.xml)
      echo "$s" | grep -qE 'Предстоящи събития@[0-9]+:([3-9]|[1-9][0-9])' && { got=$(( $(now_ms) - t0 )); break; }
      sleep 0.2
    done
    log "EVENTS $1 cold #$i: events section filled after ${got} ms (uiautomator polling, ~1 s resolution); $s"
    shot home/$1-events-$i
  done
  a wm size reset; sleep 3; dims
}
hm_carousel() {
  local b x0 y0 x1 y1 cy S=$OUT/shots/home
  sec "HC1. Hero carousel: auto-advance (animations on), swipe"
  anims 1; restart; tab home; sleep 1; dims; dump
  b=$(python3 $W15 big $OUT/ui.xml $PKG); log "carousel card: $b"
  read x0 y0 x1 y1 _ <<< "$b"
  if [ -z "${y1:-}" ]; then log "carousel not found"; debug_ui carousel; anims 0; return; fi
  shot home/c-00; sleep 6.5; shot home/c-01; sleep 6.5; shot home/c-02
  log "CAROUSEL auto-advance: diff 0→6.5 s $(python3 $W15 diff $S/c-00.png $S/c-01.png 0 $y0 $W $y1), 6.5→13 s $(python3 $W15 diff $S/c-01.png $S/c-02.png 0 $y0 $W $y1) (≈0 = no change)"
  cy=$(( (y0 + y1) / 2 ))
  a input swipe $((W*85/100)) $cy $((W*15/100)) $cy 300; sleep 2; shot home/c-03-swiped-left
  dump; log "CAROUSEL swipe left: diff $(python3 $W15 diff $S/c-02.png $S/c-03-swiped-left.png 0 $y0 $W $y1); focal: $(python3 $W15 big $OUT/ui.xml $PKG | cut -d'|' -f2 | cut -c1-80)"
  a input swipe $((W*15/100)) $cy $((W*85/100)) $cy 300; sleep 2; shot home/c-04-swiped-right
  log "CAROUSEL swipe right: diff $(python3 $W15 diff $S/c-03-swiped-left.png $S/c-04-swiped-right.png 0 $y0 $W $y1)"
  sleep 45; shot home/c-05-after-60s; sleep 7; shot home/c-06-after-67s
  log "CAROUSEL after ~60 s (the ~30 s auto-advance budget): diff $(python3 $W15 diff $S/c-05-after-60s.png $S/c-06-after-67s.png 0 $y0 $W $y1)"
  sec "HC2. Carousel: dark, large font, landscape"
  a cmd uimode night yes; sleep 3; restart; tab home; sleep 3; shot home/c-10-dark; a cmd uimode night no; sleep 2
  if set_text_size '=Extra large|=Много голям'; then
    a settings put system font_scale 1.3; sleep 2; restart; tab home; sleep 3; shot home/c-11-font130-xlarge; dump
    log "CAROUSEL font 1.3 + Много голям: $(python3 $W15 big $OUT/ui.xml $PKG | cut -c1-160)"
    a settings put system font_scale 1.0; sleep 2; set_text_size '=Standard|=Стандартен'
  fi
  restart; tab home; rot 1; sleep 3; shot home/c-12-landscape; dump; log "CAROUSEL landscape ${W}x${H}: $(python3 $W15 big $OUT/ui.xml $PKG | cut -c1-160)"; rot 0
  anims 0
}
sb_open_result() {
  local p
  dump; p=$(xy text '12 -те вятъра' --miny $((H/6))); [ -z "$p" ] && p=$(xy --first-clickable $((H/4)) $((H*4/5)) 100)
  [ -n "$p" ] || { log "SEARCH: no result row"; debug_ui sb-result; return 1; }
  a input tap $p; sleep 4; dump; log "result opened: $(texts 3 | cut -c1-120)"
}
hm_search() {
  local p
  sec "HS1. Open a result and come back: the results without the keyboard"
  sb_expand || return; hide_ime; dump; shot home/s-01-results
  if sb_open_result; then
    back 3; dump; shot home/s-02-back-from-result
    log "SEARCH back from the result: $(sbstate); results visible $(has '12 -те вятъра' && echo yes || echo NO)"
  fi
  sec "HS2. One Back collapses; the collapsed bar shows the placeholder"
  back 2; shot home/s-03-after-one-back; log "SEARCH after ONE Back: $(sbstate)"
  sec "HS3. A result, then another tab, then Home: the bar stays collapsed"
  if sb_expand; then hide_ime
    if sb_open_result; then
      log "on the result: tab bar $(has 'text="(Каталог|Catalogue)"' && echo visible || echo hidden)"
      if has 'text="(Каталог|Catalogue)"'; then tab catalog; else link "$SITE/elektronen-katalog"; fi
      sleep 3; dump; log "on the other tab: $(texts 4 | cut -c1-120)"; tab home; sleep 3; shot home/s-04-home-tab-again
      dump; log "Home tab again: $(texts 4 | cut -c1-120) (the book stays on top of the Home stack)"
      if has 'text="12 -те вятъра"' && has "$RX_BACK"; then back 3; fi
      shot home/s-05-back-from-book-after-tab-switch
      log "SEARCH after another tab and Back from the book to Home (should be collapsed): $(sbstate)"
    fi
  fi
  sec "HS4. 'Покажи всички (N)'"
  if sb_expand; then hide_ime; dump
    log "show-all buttons: $(grep -oE 'text="(Покажи всички|Show all) \([0-9]+\)"' $OUT/ui.xml | tr '\n' ' ')"
    p=$(xy text 'Покажи всички (|Show all (')
    if [ -n "$p" ]; then a input tap $p; sleep 3; dump; shot home/s-06-show-all
      log "after 'Покажи всички': chip Книги $(xy --state text '=Книги'); show-all left $(grep -c 'Покажи всички (' $OUT/ui.xml); $(texts 12 | cut -c1-300)"
    else log "no 'Покажи всички' on the results"; fi
    back 2; back 2
  fi
}
part_home() {
  sec "HM-base. v1.2.28: 'Нови постъпления' and the events section"
  inst app-base.apk base apk-base-ref.txt && { launch >/dev/null; sleep 6; ready; dims; tab catalog; waitfor "$RX_RESULTS" 150; tab home; sleep 3; hm_newarr base; hm_events base; }
  sec "HM-new. v1.2.29: fresh install, the first search, 'Нови постъпления' and the events section"
  hm_loading
  tab catalog; waitfor "$RX_RESULTS" 150 && log "new catalogue results after ${WAITED} ms"; tab home; sleep 3
  hm_newarr new; hm_events new
  hm_carousel
  hm_search
}
