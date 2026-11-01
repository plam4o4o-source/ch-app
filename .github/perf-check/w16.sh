# Частите на вълна 16 (ВРЕМЕННО, debug/wave16-check). Зарежда се от run.sh след w15.sh и
# ползва помощните му функции (a, dump, xy, tap, log, sec, shot, mark, since…).
W16=$H_/w16.py

# ---------------------------------------------------------------- общи
tapa() {  # докосване по текст, иначе по описание (content-desc)
  dump || return 1; local p; p=$(xy text "$1"); [ -z "$p" ] && p=$(xy content-desc "$1")
  [ -n "$p" ] || { echo "not found (text/desc): $1"; return 1; }; echo ">>> tap '$1' @ $p"; a input tap $p
}
# Клавиатурата се скрива с „Назад“ (само ако е показана): от вълна 16 Esc (111) е „Назад“ в приложението.
hide_ime() { a dumpsys input_method | tr -d '\r' | grep -q 'mInputShown=true' && { a input keyevent KEYCODE_BACK; sleep 1; }; return 0; }
perm_notif() { a dumpsys package $PKG | tr -d '\r' | grep -m1 -oE 'android.permission.POST_NOTIFICATIONS: granted=[a-z]+(, flags=\[[^]]*\])?'; }
helper_install() {
  [ -f helper.apk ] || { log "HELPER: helper.apk missing (helper job failed?)"; return 1; }
  timeout 120 adb install -r -g helper.apk 2>&1 | tail -1
  a pm grant org.pc.helper android.permission.CHANGE_CONFIGURATION 2>&1 | head -1
  a settings put global hidden_api_policy 1; a settings put global hidden_api_policy_p_apps 1; a settings put global hidden_api_policy_pre_p_apps 1
  a appops set org.pc.helper WRITE_SETTINGS allow
  log "HELPER installed: $(a pm list packages org.pc.helper | tr -d '\r')"
}
a11y_on() {  # тестовата услуга за достъпност (без „изследване чрез докосване“)
  mark m-a11y-on
  a settings put secure enabled_accessibility_services org.pc.helper/org.pc.helper.A11y
  a settings put secure accessibility_enabled 1
  local i; for i in 1 2 3 4 5 6 7 8 9 10; do sleep 1; since m-a11y-on 'PCA11Y.*connected' | grep -q . && break; done
  log "A11Y service: $(since m-a11y-on 'PCA11Y.*connected' | tail -1 | cut -c1-160)"
}
a11y_off() { a settings delete secure enabled_accessibility_services >/dev/null; a settings put secure accessibility_enabled 0; sleep 2; }
touchx() {  # $1 — true | false: режим „изследване чрез докосване“ (както с TalkBack)
  a am broadcast -a org.pc.helper.TOUCH --ez on $1 >/dev/null; sleep 2
  log "touch exploration $1: $(a dumpsys accessibility 2>/dev/null | tr -d '\r' | grep -m1 -oE 'touchExplorationEnabled=\w+|isTouchExplorationEnabled=\w+|mIsTouchExplorationEnabled=\w+')"
}
adump() {  # $1 — етикет: дървото от тестовата услуга → $OUT/a11ytree-$1.txt
  a am broadcast -a org.pc.helper.DUMP --es id t$1 >/dev/null; sleep 2.5
  grep -E "PCDUMP *: t$1 " $OUT/logcat-live.txt > $OUT/a11ytree-$1.txt 2>/dev/null
  log "TREE $1 ($(wc -l < $OUT/a11ytree-$1.txt) lines):"; python3 $W16 tree $OUT/a11ytree-$1.txt | tee -a $SUM
}
settings_find() {  # като settings_item, но без докосване
  local i
  for i in 1 2 3 4; do sw_down 200; sleep 0.5; done
  for i in 1 2 3 4 5 6 7 8 9 10; do dump; [ -n "$(xy text "$1")" ] && return 0; sw_up 300; sleep 1; done
  log "WARN: settings item $1 not found"; debug_ui settings-find; return 1
}
use_pkg() {  # $1 — пакетът на приложението за следващите стъпки
  PKG=$1; ACT=$PKG/org.chyavorec.app.MainActivity
}

# ================================================================= 1. ОТКЛЮЧВАНЕ НА ЛИЧНИТЕ ЕКРАНИ
RX_PROMPT='Отключи личните екрани'
L_REQ='Искай отключване за личните екрани'
prompt_state() {  # системната проверка отгоре? (след dump)
  echo "prompt $(has "$RX_PROMPT" && echo SHOWN || echo absent); fg $(fg); $(python3 $W16 prompt $OUT/ui.xml | cut -c1-300)"
}
gate_state() {  # $1 — етикет: какво е на екрана
  sleep ${2:-3}; dump; shot unlock/$1
  log "GATE $1: prompt $(has "$RX_PROMPT" && echo SHOWN || echo absent); locked view $(has 'Личният екран е заключен' && echo shown || echo no); card $(card_shown && echo SHOWN || echo no); $(python3 $W16 tabsel $OUT/ui.xml); back arrow $(has "$RX_BACK" && echo yes || echo no); app in front $(has "package=\"$PKG\"" && echo yes || echo NO); fg $(fg); $(texts 6 | cut -c1-200)"
}
enter_pin() {
  local i
  for i in 1 2 3 4 5 6; do dump; has 'password="true"' && break; sleep 1; done
  log "PIN screen: $(python3 $W16 prompt $OUT/ui.xml | cut -c1-260)"
  a input text 1234; sleep 0.5; a input keyevent 66; sleep 3
}
open_privacy() { open_settings && settings_find "$L_REQ"; }
prompt_cancel() {  # „Назад“ на проверката (първото скрива клавиатурата на ПИН-а), докато изчезне
  local i
  for i in 1 2 3; do
    a input keyevent KEYCODE_BACK; sleep 2.5; dump
    log "cancel: Back #$i → prompt $(has "$RX_PROMPT" && echo still || echo gone); IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)')"
    has "$RX_PROMPT" || return 0
  done
}
my_card() {  # „Моето“ › „Читателска карта“
  ready; tab my; sleep 3; dump
  tap text '=Читателска карта' || tap text 'Читателска карта' || { log "MY: no 'Читателска карта' entry: $(texts 12 | cut -c1-200)"; card_screen; }
}
part_unlock() {
  warmup
  sec "P0. Package (GitHub build): requested permissions"
  a dumpsys package $PKG | tr -d '\r' > $OUT/dumpsys-package.txt
  local req p AAPT
  req=$(sed -n '/requested permissions:/,/install permissions:/p' $OUT/dumpsys-package.txt | grep -oE '[A-Za-z0-9_.]+\.permission\.[A-Z0-9_]+' | sort -u | tr '\n' ' ')
  log "requested permissions: $req"
  for p in USE_BIOMETRIC USE_FINGERPRINT CAMERA FOREGROUND_SERVICE FOREGROUND_SERVICE_DATA_SYNC; do log "PERM android.permission.$p: $(echo " $req " | grep -c " android.permission.$p ")"; done
  AAPT=$(ls -d ${ANDROID_HOME:-/usr/local/lib/android/sdk}/build-tools/*/ 2>/dev/null | sort -V | tail -1)aapt2
  log "APK uses-permission (aapt2): $($AAPT dump permissions app.apk 2>&1 | grep -oE "name='[^']+'" | tr '\n' ' ' | cut -c1-600)"
  log "APK SystemForegroundService: $($AAPT dump xmltree --file AndroidManifest.xml app.apk 2>/dev/null | grep -A4 'SystemForegroundService' | grep -oE 'foregroundServiceType[^ ]*( \(Raw[^)]*\))?' | head -1)"
  a locksettings clear --old 1234 >/dev/null 2>&1
  sec "P1. No screen lock: Settings › Поверителност — the switch is disabled with an explanation"
  log "device secure: $(a dumpsys trust 2>/dev/null | tr -d '\r' | grep -m1 -oE 'deviceSecure=\w+|isDeviceSecure=\w+') lock: $(a locksettings get-disabled 2>&1 | tr -d '\r' | head -1)"
  if open_privacy; then
    shot unlock/01-no-screen-lock; log "SWITCH (no lock): $(python3 $W16 switch $OUT/ui.xml "$L_REQ")"
    log "explanation 'Не може да се включи…': $(has 'Не може да се включи: телефонът няма заключване' && echo SHOWN || echo absent); group title 'Поверителност' $(has 'text="Поверителност"' && echo yes || echo NO)"
    tap text "$L_REQ"; sleep 3; dump; log "tap on the disabled switch: $(python3 $W16 switch $OUT/ui.xml "$L_REQ"); $(prompt_state | cut -c1-200)"
  fi
  sec "P2. A self-entered card (before the switch is on)"
  card_add TEST-12345; dump; log "card screen: card $(card_shown && echo shown || echo NO)"; back 2
  sec "P3. Device PIN 1234; the switch → the system prompt → PIN"
  log "set-pin: $(a locksettings set-pin 1234 2>&1 | tr -d '\r' | head -1)"
  if open_privacy; then
    shot unlock/10-with-screen-lock; log "SWITCH (PIN set): $(python3 $W16 switch $OUT/ui.xml "$L_REQ")"
    tap text "$L_REQ"; sleep 3; dump; shot unlock/11-system-prompt; cp $OUT/ui.xml $OUT/prompt-enable.xml
    log "SWITCH tapped → $(prompt_state)"
    enter_pin; dump; shot unlock/12-after-pin
    log "SWITCH after the PIN: $(python3 $W16 switch $OUT/ui.xml "$L_REQ"); fg $(fg)"
  fi
  sec "P4. New process: Моето › Читателска карта → prompt; Back (cancel) → Моето; PIN → the card"
  a am force-stop $PKG; launch >/dev/null; sleep 5; ready
  my_card; gate_state 20-card-prompt
  prompt_cancel; gate_state 21-after-cancel 1
  my_card; sleep 3; enter_pin; gate_state 22-after-pin 1
  sec "P5. Background 20 s (< 60 s): no prompt; background 70 s (> 60 s): prompt"
  go_home 2; sleep 20; launch >/dev/null; gate_state 30-after-20s-background
  go_home 2; sleep 70; launch >/dev/null; gate_state 31-after-70s-background
  has "$RX_PROMPT" && { enter_pin; gate_state 32-after-pin 1; }
  sec "P6. Screen off and on (keyguard with the PIN): prompt again"
  mark m-screenoff
  a input keyevent KEYCODE_SLEEP; sleep 4; a input keyevent KEYCODE_WAKEUP; sleep 2
  a wm dismiss-keyguard 2>/dev/null; sleep 2; dump; shot unlock/39-keyguard; log "keyguard: $(prompt_state | cut -c1-260)"
  if has "$RX_PROMPT"; then log "(the app's prompt is already on top of the keyguard)"
  elif has 'password="true"'; then a input text 1234; a input keyevent 66; sleep 3; fi
  gate_state 40-after-screen-off
  log "biometric/keyguard log after screen off: $(since m-screenoff 'BiometricService|AuthSession|BiometricPrompt|AuthController|onError|onAuthenticationError|ERROR_CANCELED|KeyguardViewMediator.*(show|hide)' | cut -c19-200 | head -14 | tr '\n' '~')"
  has "$RX_PROMPT" && { enter_pin; gate_state 41-after-pin 1; }
  sec "P7. Quick Settings tile → the card: the gate"
  qs_add; a am force-stop $PKG; go_home 2
  qs_tile gate
  if [ -n "$TP" ]; then a input tap $TP; else log "click-tile: $(a cmd statusbar click-tile $TILE 2>&1 | tr -d '\r' | head -1)"; fi
  gate_state 50-tile-tap 4
  prompt_cancel; a cmd statusbar collapse; gate_state 51-tile-after-cancel 1
  qs_tile gate2
  if [ -n "$TP" ]; then a input tap $TP; else a cmd statusbar click-tile $TILE >/dev/null 2>&1; fi
  sleep 4; dump; has "$RX_PROMPT" && enter_pin
  gate_state 52-tile-after-pin 1
  { fg | grep -q NotificationShade || has 'Display brightness'; } && { a cmd statusbar collapse; gate_state 53-tile-after-pin-shade-collapsed 2; }
  back 2
  sec "P8. Turning the switch off also asks for the PIN"
  if open_privacy; then tap text "$L_REQ"; sleep 3; dump; log "switch OFF tapped → $(prompt_state | cut -c1-200)"; has "$RX_PROMPT" && enter_pin; dump; log "SWITCH now: $(python3 $W16 switch $OUT/ui.xml "$L_REQ")"; fi
  log "clear PIN: $(a locksettings clear --old 1234 2>&1 | tr -d '\r' | head -1)"
}

# ================================================================= 2. ВХОД
catch_check() {  # $1 — етикет: имейлът стигна ли до PC catcher, с каква тема
  local i
  for i in 1 2 3 4; do
    dump
    if has 'text="PC catcher"'; then shot login/$1-resolver; log "EMAIL $1: chooser: $(texts 8 | cut -c1-200)"; tap text '=PC catcher'; sleep 1.5; dump; has 'Just once|Само веднъж' && tap text '=Just once|=Само веднъж'; sleep 2; continue; fi
    break
  done
  shot login/$1-intent; log "EMAIL $1: fg $(fg); catcher: $(since m-email-$1 'PCCATCH' | tail -1 | sed 's/.*PCCATCH *: //' | cut -c1-400)"
  log "EMAIL $1: activity starts: $(since m-email-$1 'ActivityTaskManager: START' | grep -oE 'act=[^ ]+ (dat=[^ ]+ )?.*cmp=[^ }]+' | cut -c1-200 | tr '\n' '~')"
}
login_state() {  # $1 — етикет
  dump; shot login/$1
  log "LOGIN $1: error '$(grep -oE 'text="(Грешен|Опитай отново след)[^"]*"' $OUT/ui.xml | head -1)' status '$(grep -oE 'text="(Остав|Твърде много)[^"]*"' $OUT/ui.xml | head -1)'; Вход button: $(python3 $H_/find.py $OUT/ui.xml --nodes text '=Вход' | grep -oE 'clickable="[a-z]+"' | head -1); fields: $(python3 $W15 field $OUT/ui.xml 0 | cut -c1-80) / $(python3 $W15 field $OUT/ui.xml 1 | cut -c1-80)"
}
login_try() {  # $1 — номер на опита: ПИН 1111 и „Вход“
  local p
  dump; p=$(xy --all class EditText | sed -n 2p)
  [ -n "$p" ] || { log "PIN field not found"; return 1; }
  a input tap $p; sleep 0.8; a input text 1111; sleep 0.5; hide_ime; sleep 0.5
  # Първото точно „Вход“ е бутонът (последното — заглавието в горната лента).
  tap text '=Вход' >/dev/null; sleep 4
  login_state 1$1-attempt-$1
}
forgot_sheet() {  # $1 — етикет
  tap text '=Забравен ПИН?' || { log "no 'Забравен ПИН?'"; return 1; }
  sleep 3; dump; shot login/$1-sheet; cp $OUT/ui.xml $OUT/forgot-$1.xml
  log "FORGOT $1 sheet: $(texts 10 | cut -c1-400)"
  log "FORGOT $1 email button: $(grep -oE 'text="Изпрати имейл[^"]*"' $OUT/ui.xml | head -1)"
  mark m-email-$1
  tap text 'Изпрати имейл' || { log "no email button"; return 1; }
  sleep 3; catch_check $1
}
part_login16() {
  helper_install
  sec "LG0. Demo build (devDebug, org.chyavorec.app.dev): demo sign-in — wrong PINs never reach the real server"
  use_pkg org.chyavorec.app.dev
  timeout 180 adb install -r app-dev.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG; a pm grant $PKG android.permission.POST_NOTIFICATIONS
  log "installed: $(cat apk-dev-ref.txt); $(a dumpsys package $PKG | grep -m2 -E 'versionName|versionCode' | tr -s ' ' | tr -d '\r' | tr '\n' ' ')"
  warmup
  ready; tab my; sleep 3; tap text '=Вход' || { log "no Вход in My: $(texts 10 | cut -c1-200)"; debug_ui login16; }
  sleep 4; dump; shot login/00-demo-login; log "LOGIN screen: $(texts 12 | cut -c1-400)"
  sec "LG1. Card number TEST-0042 (fake)"
  local p; p=$(xy --all class EditText | sed -n 1p)
  [ -n "$p" ] && { a input tap $p; sleep 0.8; a input text TEST-0042; sleep 0.5; }
  sec "LG1a. Esc (hardware keyboard) in the focused card field: the field keeps the text, the screen stays?"
  dump; log "before Esc: $(python3 $W15 field $OUT/ui.xml 0 | sed 's/password=[a-z]*//'); IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)')"
  a input keyevent KEYCODE_ESCAPE; sleep 2; dump; shot login/05-after-esc
  log "after Esc #1: login screen $(has 'text="Вход за читатели"' && echo STILL || echo 'GONE (navigated back)'); $(python3 $W15 field $OUT/ui.xml 0 | sed 's/password=[a-z]*//'); IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)'); $(python3 $W16 tabsel $OUT/ui.xml)"
  has 'text="Вход за читатели"' || { tap text '=Вход'; sleep 3; dump; p=$(xy --all class EditText | sed -n 1p); [ -n "$p" ] && { a input tap $p; sleep 0.8; a input text TEST-0042; sleep 0.5; }; }
  sec "LG1b. Two wrong attempts (fake card TEST-0042, PIN 1111)"
  login_try 1; login_try 2
  sec "LG2. Forgot PIN (card TEST-0042, PIN 9876 typed, not sent): the sheet; the email subject"
  p=$(xy --all class EditText | sed -n 2p); [ -n "$p" ] && { a input tap $p; sleep 0.8; a input text 9876; sleep 0.5; hide_ime; }
  forgot_sheet 20-demo
  log "EMAIL subject has the card number: $(since m-email-20-demo PCCATCH | grep -c 'TEST-0042'); has a PIN (9876/1111): $(since m-email-20-demo PCCATCH | grep -cE '9876|1111')"
  launch >/dev/null; sleep 3; dump; has 'Изпрати имейл' && back 2
  dump; has 'text="Вход за читатели"' || { ready; tab my; tap text '=Вход'; sleep 3; }
  sec "LG3. Attempts 3-5: the 5th locks sign-in; the countdown"
  login_try 3; login_try 4; login_try 5
  login_state 16-locked-again-3s
  sleep 5; login_state 17-locked-after-8s
  sec "LG4. 'Запомни ме' is a CheckBox"
  dump; python3 $W15 checkables $OUT/ui.xml | tee -a $SUM
  sec "LG5. Production build: the sign-in screen (no submit), 'Запомни ме', 'Забравен ПИН?' without a card number"
  use_pkg org.chyavorec.app
  a am force-stop org.chyavorec.app.dev
  launch >/dev/null; sleep 4; ready; tab my; sleep 3
  tap text '=Вход' && { sleep 4; dump; shot login/30-prod-login; log "PROD login: $(texts 12 | cut -c1-300)"; python3 $W15 checkables $OUT/ui.xml | sed 's/^/prod: /' | tee -a $SUM; forgot_sheet 31-prod; }
}

# ================================================================= 3. ОБНОВЯВАНЕ С ИЗВЕСТИЕ ЗА ХОДА
notes_match() {  # $1 — етикет (бележките на 1.2.30 от update.json)
  python3 - $OUT/ui.xml $OUT/update.json "$1" <<'EOF' | tee -a $SUM
import json, sys, html
ui = html.unescape(open(sys.argv[1], encoding='utf-8', errors='replace').read())
notes = json.load(open(sys.argv[2], encoding='utf-8'))['notes'].split('\n')
hit = [l for l in notes if l.strip()[:28] in ui]
print(f"NOTES {sys.argv[3]}: {len(hit)}/{len(notes)} lines of the real 1.2.30 notes visible" + ('' if len(hit) == len(notes) else f"; missing: {[l[:40] for l in notes if l not in hit]}"))
EOF
}
throttle_on() {
  local r
  r=$(a "iptables -I INPUT 1 -p tcp --sport 443 -m hashlimit --hashlimit-above 64kb/s --hashlimit-burst 64kb --hashlimit-mode srcip --hashlimit-name pcdl -j DROP" 2>&1 | tr -d '\r')
  if [ -n "$r" ]; then
    log "hashlimit rule failed ($r) — packet limit instead"
    a "iptables -I INPUT 1 -p tcp --sport 443 -m limit --limit 40/second --limit-burst 40 -j ACCEPT"
    a "iptables -I INPUT 2 -p tcp --sport 443 -j DROP"
  fi
  a "ip6tables -I INPUT 1 -p tcp --sport 443 -m hashlimit --hashlimit-above 64kb/s --hashlimit-burst 64kb --hashlimit-mode srcip --hashlimit-name pcdl6 -j DROP" >/dev/null 2>&1
  log "THROTTLE on: $(a iptables -S INPUT 2>&1 | tr -d '\r' | head -3 | tr '\n' ' ')"
}
throttle_off() {
  local i
  for i in 1 2 3; do a "iptables -S INPUT" | tr -d '\r' | grep -q -- '--sport 443' || break; a "iptables -D INPUT 1"; done
  a "ip6tables -D INPUT 1" >/dev/null 2>&1
  log "THROTTLE off: $(a iptables -S INPUT 2>&1 | tr -d '\r' | grep -c -- '--sport 443') rules left"
}
upd_files() { a "ls -l /data/data/$PKG/no_backup/updates/ 2>&1" | tr -d '\r' | grep -v '^total' | tr -s ' ' | cut -d' ' -f5- | tr '\n' ';'; }
upd_notifs() { a dumpsys notification --noredact 2>/dev/null | tr -d '\r' > $OUT/notif-$1.txt; python3 $W16 notifp $OUT/notif-$1.txt $PKG; }
upd_dialog() {  # $1 — регулярен израз, до 90 s; пропуска въведението и молбата за известия
  local i
  for i in $(seq 1 45); do
    sleep 2; dump
    has "$1" && return 0
    has 'text="(Пропусни|Skip)"' && tap text '=Пропусни|=Skip'
    has 'text="(Не сега|Not now)"' && tap text '=Не сега|=Not now'
  done
  return 1
}
installer_confirm() {  # системният инсталатор → „Update“, докато версията стане $1
  local i
  for i in $(seq 1 60); do
    sleep 2; dump
    if has 'package="com.(google.)?android.packageinstaller"'; then
      shot update/$2-system-installer; log "INSTALLER: $(texts 6 | cut -c1-200)"
      tap text '=Update|=Обнови|=Install|=Инсталирай|=OK' && sleep 3
    fi
    [ "$(a dumpsys package $PKG | grep -m1 -oE 'versionCode=[0-9]+')" = "versionCode=$1" ] && return 0
  done
  return 1
}
part_update16() {
  local i st t0 p
  sec "U0. Staged manifest (test v1.2.30 APK + the real 1.2.30 notes: tools/whats_new.py 1.2.30 store/whats-new.md <released> <out>)"
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  log "staged manifest: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["sha256"][:12], d["size"], len(d["notes"].split(chr(10))), "note lines")' $OUT/update.json 2>&1)"
  log "notes (bg): $(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["notes"].replace(chr(10), " / "))' $OUT/update.json 2>&1 | cut -c1-700)"
  # ---------------- A: v1.2.29 → v1.2.30 (както читателите; Wi-Fi — v1.2.29 го изтегля сам)
  sec "UA1. v1.2.29 (tag) with the staged update.json: the dialog"
  inst app-base-upd.apk base-upd apk-base-upd-ref.txt || return
  log "install-unknown-apps: $(a appops set $PKG REQUEST_INSTALL_PACKAGES allow 2>&1; a appops get $PKG REQUEST_INSTALL_PACKAGES | tr -d '\r')"
  launch >/dev/null; t0=$(now_ms)
  upd_dialog 'text="(Нова версия|New version)' && st=dialog || st=none
  shot update/10-v129-dialog; cp $OUT/ui.xml $OUT/dialog-a.xml 2>/dev/null
  log "UPDATE dialog (v1.2.29): $st after $(( $(now_ms) - t0 )) ms: $(texts 12 | cut -c1-500)"
  if [ $st = dialog ]; then
    notes_match a-dialog
    p=$(xy text '=Инсталирай|=Install'); [ -z "$p" ] && p=$(xy text '=Изтегли и инсталирай|=Download and install')
    [ -n "$p" ] && { log "UPDATE (v1.2.29): tap $(python3 $H_/find.py $OUT/ui.xml --texts | grep -m1 -E '^(Инсталирай|Изтегли и инсталирай)$')"; a input tap $p; }
    installer_confirm $NEW_VC 11 && log "UPDATE A: installed $(ver)" || log "UPDATE A: NOT installed: $(ver)"
    sleep 3; dump; log "after install: fg $(fg); $(texts 6 | cut -c1-160)"
    sec "UA2. 'Какво ново' after the update: the 1.2.30 notes"
    has "$RX_WHATSNEW" || { launch >/dev/null; sleep 3; }
    if whatsnew_check 12-whats-new-1.2.30; then
      a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*40/100)) 400; sleep 1.5; dump; shot update/13-whats-new-scrolled; notes_match a-sheet-scrolled
      tap text '=Добре|=OK|=Разбрах|=Got it' || back 2
    fi
  fi
  # ---------------- B: новият код (1.2.30-pre) → тестовото 1.2.30, само мобилни данни, забавено
  sec "UB0. The v1.2.30 code as 1.2.30-pre (versionCode $VC_BASE) → the test v1.2.30; mobile data only (metered), throttled"
  inst app-new-upd.apk new-upd apk-new-upd-ref.txt || return
  a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  a svc wifi disable; a svc data enable; sleep 6
  log "network: $(a dumpsys connectivity 2>/dev/null | tr -d '\r' | grep -m2 -oE 'Active default network: [0-9]+|NetworkAgentInfo\{[^}]*(MOBILE|WIFI|CELLULAR)[^ ]*' | tr '\n' ' ' | cut -c1-200); metered: $(a dumpsys netpolicy 2>/dev/null | tr -d '\r' | grep -m1 -oE 'meteredness=\w+|Metered ifaces: \{[^}]*\}')"
  throttle_on
  launch >/dev/null; t0=$(now_ms)
  upd_dialog '(Изтегли и инсталирай|Инсталирай)' && st=dialog || st=none
  shot update/20-dialog; cp $OUT/ui.xml $OUT/dialog-b.xml 2>/dev/null
  log "UPDATE dialog (1.2.30-pre): $st after $(( $(now_ms) - t0 )) ms: $(texts 12 | cut -c1-500)"
  [ $st = dialog ] || { log "UPDATE B: no dialog — progress notification not tested"; throttle_off; a svc wifi enable; return; }
  notes_match b-dialog
  sec "UB1. 'Изтегли и инсталирай': progress and 'Отказ' in the dialog"
  mark m-dl-1
  tap text '=Изтегли и инсталирай' || log "no 'Изтегли и инсталирай' (already downloaded?): $(texts 8 | cut -c1-200)"
  t0=$(now_ms)
  for i in 1 2 3; do
    sleep 1.5; dump; [ $i = 2 ] && shot update/21-dialog-downloading
    log "DIALOG +$(( $(now_ms) - t0 )) ms: progress bars $(grep -c 'class="android.widget.ProgressBar"' $OUT/ui.xml) [$(grep -oE 'class="android.widget.ProgressBar"[^>]*' $OUT/ui.xml | grep -oE '(text|content-desc)="[^"]*"' | head -2 | tr '\n' ' ')]; 'Отказ' $(has 'text="Отказ"' && echo SHOWN || echo absent); $(grep -oE 'text="[^"]*([0-9]+ ?%|MB|КБ|МБ|KB)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  done
  sec "UB2. Home during the download: the progress notification; the shade"
  go_home 1.5
  for i in 1 2 3; do log "NOTIF +$(( $(now_ms) - t0 )) ms: $(upd_notifs b2-$i | head -2 | tr '\n' ' ' | cut -c1-400)"; sleep 2; done
  log "partial file: $(upd_files)"
  a cmd statusbar expand-notifications; sleep 2.5; dump; shot update/22-shade-downloading; cp $OUT/ui.xml $OUT/shade-downloading.xml
  log "SHADE: $(grep -oE 'text="[^"]*(Изтегляне на версия|Отказ)[^"]*"' $OUT/ui.xml | head -3 | tr '\n' ' '); progress bar in the shade: $(grep -c 'android:id/progress' $OUT/ui.xml)"
  sec "UB3. 'Отказ' from the notification: the download stops, the partial file is deleted"
  if tap text '=Отказ'; then
    sleep 4; log "after 'Отказ' in the shade: $(upd_notifs b3 | tr '\n' ' ' | cut -c1-300); files: [$(upd_files)]"
    sleep 6; log "after 10 s: files: [$(upd_files)]; $(upd_notifs b3b | tail -1)"
  else
    log "no 'Отказ' in the shade (download already done?): $(texts 12 | cut -c1-300)"
  fi
  a cmd statusbar collapse; sleep 1
  sec "UB4. Restart the download, Home at once: it finishes in the background → 'готова за инсталиране' → tap → installer"
  launch >/dev/null; sleep 3; dump
  if ! has 'text="Изтегли и инсталирай"'; then
    log "dialog after relaunch: $(texts 8 | cut -c1-200) — asking from Settings"
    if open_settings && settings_find 'Налична е версия|Провери за нова версия'; then tap text 'Налична е версия' || tap text 'Провери за нова версия'; sleep 4; fi
    upd_dialog 'text="Изтегли и инсталирай"'
  fi
  shot update/30-dialog-again
  mark m-dl-2
  tap text '=Изтегли и инсталирай' && t0=$(now_ms)
  for i in 1 2 3 4; do
    sleep 2; dump; [ $i = 2 ] && shot update/30b-dialog-after-second-tap
    log "DIALOG after the 2nd tap +$(( $(now_ms) - t0 )) ms: progress $(grep -c 'class="android.widget.ProgressBar"' $OUT/ui.xml) $(grep -oE 'text="[0-9]+ ?%"' $OUT/ui.xml | head -1); 'Отказ' $(has 'text="Отказ"' && echo yes || echo no); 'Изтегли и инсталирай' $(has 'text="Изтегли и инсталирай"' && echo yes || echo no); notif: $(upd_notifs b4t-$i | head -1 | cut -c1-120)"
  done
  log "jobs of the app after the 2nd tap: $(a dumpsys jobscheduler 2>/dev/null | tr -d '\r' | grep -E "JOB #.*$PKG" | cut -c1-140 | head -4 | tr '\n' '~'); files: [$(upd_files)]"
  log "logcat since the 2nd tap: $(since m-dl-2 "$PKG|WM-|WorkManager|SystemForeground|update" | grep -vE 'Choreographer|OpenGLRenderer|uiautomator' | cut -c19-220 | head -10 | tr '\n' '~')"
  a dumpsys jobscheduler 2>/dev/null | tr -d '\r' > $OUT/jobscheduler-after-2nd-tap.txt
  log "app jobs (state): $(grep -A30 -E "JOB #u0a[0-9]+/[0-9]+: [0-9a-f]+ $PKG/" $OUT/jobscheduler-after-2nd-tap.txt | grep -E "JOB #|Required constraints|Unsatisfied|Ready|Pending|Running|Enqueue time|Minimum latency|Satisfied" | cut -c1-120 | head -24 | tr -s ' ' | tr '\n' '~')"
  log "app process: '$(a pidof $PKG | tr -d '\r')'; state shown: $(grep -oE 'text="(Проверка[^"]*|Изтегляне[^"]*)"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  if ! upd_notifs b4-check | grep -q 'Изтегляне на версия'; then
    sec "UB4b. Still no download after the 2nd tap: the app force-stopped and opened again, 'Изтегли и инсталирай' once more"
    a am force-stop $PKG; sleep 2; launch >/dev/null; upd_dialog 'text="Изтегли и инсталирай"'; shot update/30c-dialog-new-process
    mark m-dl-3; tap text '=Изтегли и инсталирай' && t0=$(now_ms)
    for i in 1 2 3; do sleep 3; dump; log "new process +$(( $(now_ms) - t0 )) ms: $(grep -oE 'text="[0-9]+ ?%"' $OUT/ui.xml | head -1) 'Отказ' $(has 'text="Отказ"' && echo yes || echo no); notif: $(upd_notifs b4n-$i | head -1 | cut -c1-120)"; done
  fi
  go_home 1
  st=none
  for i in $(seq 1 60); do
    upd_notifs b4 > $OUT/b4.txt
    [ $((i % 4)) = 1 ] && log "NOTIF +$(( $(now_ms) - t0 )) ms: $(head -1 $OUT/b4.txt | cut -c1-250)"
    grep -q 'готова за инсталиране' $OUT/b4.txt && { st=ready; break; }
    sleep 4
  done
  log "DOWNLOAD in the background: $st after $(( $(now_ms) - t0 )) ms; $(grep -m1 'готова' $OUT/b4.txt | cut -c1-250)"
  throttle_off
  a cmd statusbar expand-notifications; sleep 2.5; dump; shot update/31-shade-ready
  if tap text 'готова за инсталиране'; then
    sleep 4; dump; shot update/32-after-tap; log "tap on 'готова за инсталиране' → fg $(fg); $(texts 6 | cut -c1-200)"
    has 'text="Инсталирай"' && tap text '=Инсталирай'
    installer_confirm $NEW_VC 33 && log "UPDATE B: installed $(ver)" || log "UPDATE B: NOT installed: $(ver)"
    sleep 3; has "$RX_WHATSNEW" || { launch >/dev/null; sleep 3; }
    whatsnew_check 34-whats-new-after-b
  else log "no 'готова за инсталиране' notification in the shade: $(texts 10 | cut -c1-300)"; fi
  a cmd statusbar collapse
  a svc wifi enable
}

# ================================================================= 4. СЪБИТИЯ: МЯСТО И „МАРШРУТ“
open_event_list() {
  link_pkg "$EVENTS_URL"; sleep 5; cal_checked || segment calendar
  dump; has 'content-desc="(List view|Изглед списък)"' && { tap content-desc '=List view|=Изглед списък'; sleep 2; }
}
route_tap() {  # $1 — етикет: „Маршрут“ на екрана и намерението geo:
  mark m-route-$1
  tap text '=Маршрут' || return 1
  sleep 5; dump; shot events/$1-maps
  log "ROUTE $1: fg $(fg); geo intent: $(since m-route-$1 'START u0' | grep -oE 'act=android.intent.action.VIEW dat=geo:[^ ]*( [a-z]+=[^ ]*)*' | head -1 | cut -c1-200); $(since m-route-$1 'START u0' | grep -oE 'cmp=[^ }]+' | head -2 | tr '\n' ' ')"
}
part_events16() {
  local i j p ok=no
  sec "E0. Site data (host): events with a place; texts with emoji"
  python3 $W16 site $OUT/site 2>&1 | tee -a $SUM
  warmup
  sec "E1. Events list: the cards show the time and the description, not the place"
  open_event_list; shot events/01-list; cp $OUT/ui.xml $OUT/events-list.xml
  python3 $W16 cards $OUT/ui.xml $OUT/site/places.txt | tee -a $SUM
  sw_up 500; sleep 2; dump; shot events/02-list-scrolled; python3 $W16 cards $OUT/ui.xml $OUT/site/places.txt | tail -3 | tee -a $SUM
  sec "E2. An event with a place: the address line and 'Маршрут' → a geo: intent"
  open_event_list
  for i in 1 2 3 4 5 6 7 8; do
    dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $((H/5)) $((H*85/100)) 150 | sed -n ${i}p)
    [ -n "$p" ] || { sw_up 500; sleep 2; dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $((H/5)) $((H*85/100)) 150 | sed -n 1p); }
    [ -n "$p" ] || break
    a input tap $p; sleep 4; dump
    for j in 1 2 3; do [ -n "$(xy text '=Маршрут')" ] && break; sw_up 400; sleep 1; dump; done
    if [ -n "$(xy text '=Маршрут')" ]; then
      shot events/10-detail-with-place; cp $OUT/ui.xml $OUT/event-detail.xml
      log "EVENT with a place: $(texts 12 | cut -c1-500)"
      log "EVENT place line: $(python3 $H_/find.py $OUT/ui.xml --texts | grep -iE 'Яворец|читалище|площад|пл\.|ул\.|салон|зала|църква|училище' | head -3 | tr '\n' '|')"
      route_tap 11; launch >/dev/null; sleep 3
      dump; [ -n "$(xy text '=Напомни ми')" ] || { for j in 1 2; do sw_up 400; sleep 1; dump; done; }
      if tap text '=Напомни ми'; then sleep 1.5; dump; log "REMINDER set on this event: $(grep -oE 'text="Ще получиш напомняне[^"]*"' $OUT/ui.xml | head -1)"; ok=yes; fi
      break
    fi
    log "EVENT candidate $i without 'Маршрут': $(texts 4 | cut -c1-160)"
    back 2
  done
  [ $ok = yes ] || { sec "E2b. Reminder on another event"; event_reminder_set; }
  sec "E3. The event reminder notification: 'Маршрут'"
  log "app jobs: $(app_jobs)"; run_jobs; sleep 25; notifs e3 | tee -a $SUM
  if shade_expand 'Напомняне за събитие' 20-reminder; then
    mark m-route-notif
    notif_action 'Маршрут' 21-reminder-route
    log "ROUTE from the notification: geo intent: $(since m-route-notif 'START u0' | grep -oE 'dat=geo:[^ ]*' | head -1 | cut -c1-200)"
  fi
  a cmd statusbar collapse
  sec "E4. Texts with emoji (API $SDK)"
  emoji_items
}
emoji_items() {
  local n=0 kind id url q
  [ -s $OUT/site/emoji.txt ] || python3 $W16 site $OUT/site > /dev/null 2>&1
  local title w
  while IFS='|' read -r kind id url title <&3; do
    n=$((n+1)); [ $n -gt 6 ] && break
    q=$(python3 -c 'import sys,urllib.parse; print(urllib.parse.quote(sys.argv[1], safe=""))' "$id")
    case $kind in
      event) a am start -a org.chyavorec.app.SHORTCUT -d "chyavorec://app/event/$q" -n $ACT >/dev/null 2>&1 < /dev/null;;
      news)
        if [ -n "$url" ] && [ "$url" != None ]; then link "$SITE$url" < /dev/null
        else  # без адрес (news.json): търсене в „Новини“ по латинска дума от заглавието
          w=$(echo "$title" | grep -oE '[A-Za-z]{2,}( [A-Za-z]{2,})?' | head -1)
          [ -n "$w" ] || { log "EMOJI $n: '$title' — no URL and no Latin word to search for"; continue; }
          link_pkg "$SITE/news" < /dev/null; sleep 4; segstate | grep -q 'News\[checked=true' || segment news
          tap text 'Търси в новините|Search news' < /dev/null; sleep 1; a input text "${w// /%s}" < /dev/null; sleep 3; hide_ime < /dev/null
          dump; p=$(python3 $H_/find.py $OUT/ui.xml --clickables $((H/4)) $((H*85/100)) 150 | head -1)
          [ -n "$p" ] && a input tap $p < /dev/null
        fi;;
      *) case $url in http*) link "$url" < /dev/null;; /*) link "$SITE$url" < /dev/null;; *) continue;; esac;;
    esac
    sleep 5; dump; shot emoji/$n-$kind
    log "EMOJI $n ($kind $id '$title'): $(python3 $H_/find.py $OUT/ui.xml --texts | python3 -c 'import re,sys; print(" | ".join(l.strip()[:120] for l in sys.stdin if re.search("[\U0001F000-\U0001FAFF\u2600-\u27BF]", l))[:400])')"
    back 2 < /dev/null
  done 3< $OUT/site/emoji.txt
  [ $n = 0 ] && log "EMOJI: no site text with emoji found"
  link_pkg "$EVENTS_URL"; sleep 5; shot emoji/9-events-list
}
part_emoji() {
  sec "M0. Site data (host): texts with emoji"
  python3 $W16 site $OUT/site 2>&1 | tee -a $SUM
  log "GMS on the image: $(a dumpsys package com.google.android.gms 2>/dev/null | tr -d '\r' | grep -m1 -oE 'versionName=[^ ]+'); emoji font provider (androidx.content.action.LOAD_EMOJI_FONT): $(a dumpsys package com.google.android.gms 2>/dev/null | tr -d '\r' | grep -c 'LOAD_EMOJI_FONT') match(es); app EmojiCompatInitializer in the APK: $($(ls -d ${ANDROID_HOME:-/usr/local/lib/android/sdk}/build-tools/*/ | sort -V | tail -1)aapt2 dump xmltree --file AndroidManifest.xml app.apk 2>/dev/null | grep -c 'EmojiCompatInitializer')"
  warmup
  sec "M1. Texts with emoji on API $SDK"
  emoji_items
  sec "M1b. The bread article again after 30 s (EmojiCompat loads its font asynchronously)"
  sleep 30; link "$SITE/news/svetoven-den-na-hlyaba"; sleep 6; shot emoji/1b-news-after-30s
  a am force-stop $PKG; sleep 2; launch >/dev/null; sleep 15; link "$SITE/news/svetoven-den-na-hlyaba"; sleep 6; shot emoji/1c-news-second-process
  log "logcat EmojiCompat/fonts: $(grep -iE 'EmojiCompat|emoji2|FontsContract|FontProvider|FontRequest' $OUT/logcat-live.txt | cut -c19-200 | head -6 | tr '\n' '~')"
  sec "M2. Cold starts (10), API $SDK"
  : > $OUT/start-cold.txt
  for i in $(seq 1 10); do a am force-stop $PKG; sleep 3; launch | tee -a $OUT/start-cold.txt | tr '\n' ' '; echo; sleep 3; done
  python3 $H_/parse.py starts $OUT/start-cold.txt "cold API $SDK" | tee -a $SUM
}

# ================================================================= 5. ДИАЛОЗИТЕ И „РАЗМЕР НА ТЕКСТА“
dlg() {  # $1 — етикет: dump, снимка, размери
  sleep ${2:-2}; dump; shot dialogs/$1; cp $OUT/ui.xml $OUT/dlg-$1.xml
  log "DIALOG $1: $(python3 $W16 texth $OUT/ui.xml $DENS 8 | cut -c1-600)"
}
seed_whatsnew() {  # „Какво ново“ за 1.2.30 при следващото пускане (записът, който оставя обновяването)
  local D=/data/data/$PKG/files/datastore uid
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  python3 $W16 whatsnew-json $OUT/update.json $OUT/whatsnew-record.json | tee -a $SUM
  a am force-stop $PKG; sleep 1
  rm -f $OUT/settings.pb; timeout 20 adb pull $D/settings.preferences_pb $OUT/settings.pb >/dev/null 2>&1
  python3 $W16 prefs-add $OUT/settings.pb pending_whats_new=sf:$OUT/whatsnew-record.json | tee -a $SUM
  uid=$(a stat -c %u /data/data/$PKG | tr -d '\r')
  timeout 20 adb push $OUT/settings.pb $D/settings.preferences_pb >/dev/null 2>&1
  a chown $uid:$uid $D/settings.preferences_pb; a chmod 600 $D/settings.preferences_pb; a restorecon $D/settings.preferences_pb
}
dialogs_pass() {  # $1 — етикет на мащаба
  local s=$1
  sec "D-$s. Dialogs at '$s' (font_scale $(a settings get system font_scale | tr -d '\r'))"
  open_settings && settings_find '=Размер на текста' && { dump; cp $OUT/ui.xml $OUT/dlg-$s-00-settings-screen.xml; shot dialogs/$s-00-settings-screen; tap text '=Размер на текста'; dlg $s-01-text-size; back 1.5; }
  settings_find '=Изчисти временните данни' && { tap text '=Изчисти временните данни'; dlg $s-02-clear-cache; tap text '=Отказ' || back 1.5; }
  ready; tab catalog; waitfor "$RX_RESULTS" 60; dump; cp $OUT/ui.xml $OUT/dlg-$s-03a-catalog-screen.xml
  tapa '=Подреждане' || tapa 'Подреждане' && { dlg $s-03-sort-menu; back 1.5; }
  tapa '=Филтри' || tapa 'Филтри' && { dlg $s-04-filters 2.5; a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*35/100)) 400; sleep 1.5; dump; shot dialogs/$s-04b-filters-swiped; log "filters after a swipe: $(python3 $W16 texth $OUT/ui.xml $DENS 4 | cut -c1-200)"; back 1.5; }
  seed_whatsnew; launch >/dev/null; sleep 2
  local i; for i in 1 2 3 4 5 6 7 8; do dump; has "$RX_WHATSNEW" && break; sleep 1.5; done
  dlg $s-05-whats-new 0.5
  a input swipe $((W/2)) $((H*80/100)) $((W/2)) $((H*35/100)) 400; sleep 1.5; shot dialogs/$s-05b-whats-new-swiped
  tap text '=Добре|=OK|=Разбрах|=Got it' || back 2
}
part_dialogs() {
  DENS=$(dens)
  warmup
  dialogs_pass std
  set_text_size '=Много голям' && dialogs_pass xl
  sec "D-xl130. 'Много голям' + system font 1.3 (the stress case)"
  a settings put system font_scale 1.3; sleep 2; restart
  dialogs_pass xl130
  a settings put system font_scale 1.0
  sec "D-ratio. Dialog text height ×(Много голям / Стандартен) next to the screen text"
  local k
  for k in 00-settings-screen 01-text-size 02-clear-cache 03a-catalog-screen 03-sort-menu 04-filters 05-whats-new; do
    log "RATIO $k xl/std: $(python3 $W16 ratio $OUT/dlg-std-$k.xml $OUT/dlg-xl-$k.xml 2>&1 | cut -c1-300)"
  done
  set_text_size '=Стандартен'
}

# ================================================================= 6. ЕЗИКЪТ НА УИДЖЕТА
wlang() {  # $1 — етикет
  wlabels $1 >/dev/null
  local t; t=$(python3 $H_/find.py $OUT/ui.xml --widget-texts $PKG | tr '\n' ' ')
  log "WIDGET-LANG $1: $(python3 $W16 lang $t) | $(echo "$t" | cut -c1-200)"
}
wwatch() {  # $1 — етикет: уиджетът след 2, 5, 10, 20, 40 s (без отваряне на приложението)
  local t prev=0
  for t in 2 5 10 20 40; do sleep $((t - prev)); prev=$t; go_home 0.5; wlang $1-${t}s; done
  log "app process after: '$(a pidof $PKG | tr -d '\r')'"
}
dev_locale() {  # $1 — bg-BG | en-US: езикът на устройството без рестарт (помощното приложение)
  local r
  r=$(a am broadcast -n org.pc.helper/.Cmd -a org.pc.helper.LOCALE --es tags $1 2>&1 | tr -d '\r' | grep -oE 'data="[^"]*"')
  log "DEVICE locale -> $1: $r; config now: $(a am get-config 2>/dev/null | tr -d '\r' | grep -m1 -oE '^config: [^ ]+')"
  echo "$r" | grep -q 'LOCALE ok'
}
part_widgetlang16() {
  K=lang
  helper_install
  warmup
  sec "WL1. The widget (system en-US, app bg-BG)"
  log "system: $(a am get-config 2>/dev/null | tr -d '\r' | grep -m1 -oE '^config: [^ ]+'); app: $(a cmd locale get-app-locales $PKG | tr -d '\r' | grep -oE '\[[^]]*\]')"
  widget_add lang || { log "WIDGET: NOT added — language checks skipped"; return; }
  go_home 3; wlang 01-appBG
  dump; log "$(python3 $W16 widgetcol $OUT/ui.xml $PKG $(dens))"; log "$(python3 $W15 targets $OUT/ui.xml $PKG $(dens))"
  sec "WL2. App language → English from the system per-app setting (cmd locale), app not opened (process killed, not force-stopped)"
  go_home 2; a am kill $PKG; sleep 2; log "app process before: '$(a pidof $PKG | tr -d '\r')'"
  mark m-wl2; a cmd locale set-app-locales $PKG --locales en-US
  wwatch 10-appEN
  log "receiver/broadcast lines: $(since m-wl2 'LOCALE_CHANGED|chyavorec' | head -4 | cut -c1-160 | tr '\n' '~')"
  sec "WL3. App language → Български (cmd locale), app not opened"
  a am kill $PKG; sleep 1; a cmd locale set-app-locales $PKG --locales bg-BG
  wwatch 20-appBG
  sec "WL4. Device language (app follows the system): en-US → bg-BG → en-US, without a reboot"
  a "cmd locale set-app-locales $PKG --locales ''"; sleep 3
  log "app locales cleared: $(a cmd locale get-app-locales $PKG | tr -d '\r')"
  go_home 2; sleep 8; wlang 30-follow-system-en
  a am kill $PKG; sleep 1
  if dev_locale bg-BG; then
    wwatch 31-deviceBG
    a am kill $PKG; sleep 1; dev_locale en-US; wwatch 32-deviceEN
  else
    log "(helper could not change the device language — framework restart instead)"
    sys_locale bg-BG; wwatch 31-deviceBG-restart
    sys_locale en-US; wwatch 32-deviceEN-restart
  fi
  a cmd locale set-app-locales $PKG --locales bg-BG
  a cmd uimode night yes; sleep 4; go_home 3; shot widget/lang-40-dark; a cmd uimode night no
}

# ================================================================= 7. TALKBACK / ДОСТЪПНОСТ, 10. СЪОБЩЕНИЯ
part_a11y16() {
  local t S=$OUT/shots/a11y16 b x0 y0 x1 y1 cy
  helper_install
  log "TalkBack on the image: $(a pm list packages 2>/dev/null | grep -ciE 'talkback|marvin') package(s) — the test accessibility service stands in for it"
  warmup
  DENS=$(dens); dims
  sec "B. Home top bar: Messages (megaphone) and the unread badge"
  restart; tab home; sleep 4; dump; shot a11y16/01-home-top; cp $OUT/ui.xml $OUT/home.xml
  log "MESSAGES button: $(grep -oE 'content-desc="Съобщения[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' '); bell/notification icons: $(grep -oE 'content-desc="[^"]*(Известия|звън)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  log "carousel card (uiautomator): $(python3 $W15 big $OUT/ui.xml $PKG | cut -c1-200)"
  sec "B2. Unread messages: the stored read list emptied (as if none were read) → the badge and the label"
  local D=/data/data/$PKG/files/datastore uid
  a am force-stop $PKG; sleep 1; rm -f $OUT/settings.pb
  timeout 20 adb pull $D/settings.preferences_pb $OUT/settings.pb >/dev/null 2>&1
  python3 $W16 prefs-add $OUT/settings.pb read_messages=e: | tee -a $SUM
  uid=$(a stat -c %u /data/data/$PKG | tr -d '\r')
  timeout 20 adb push $OUT/settings.pb $D/settings.preferences_pb >/dev/null 2>&1
  a chown $uid:$uid $D/settings.preferences_pb; a chmod 600 $D/settings.preferences_pb; a restorecon $D/settings.preferences_pb
  launch >/dev/null; sleep 6; ready; tab home; sleep 3; dump; shot a11y16/02-home-unread
  log "MESSAGES button (unread): $(grep -oE 'content-desc="Съобщения[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' '); badge texts near it: $(grep -oE 'text="[0-9]+\+?"' $OUT/ui.xml | head -3 | tr '\n' ' ')"
  tap content-desc 'Съобщения' && { sleep 4; dump; shot a11y16/03-messages; log "Messages screen: $(texts 8 | cut -c1-260)"; back 2; }
  b=$(python3 $W15 big $OUT/ui.xml $PKG); read x0 y0 x1 y1 _ <<< "$b"
  ready; tab home; sleep 2; dump
  declare -A TXY; local ref
  for t in home catalog my news more; do ref='=More|=Още'; [ $t = more ] && ref='=Home|=Начало'; TXY[$t]=$(xy --tab "$(tablabel $t)" "$ref"); done
  sec "T1. Test accessibility service on (log mode): pane titles on the tabs and detail screens"
  a11y_on
  mark m-pane
  for t in catalog my news more home; do [ -n "${TXY[$t]}" ] && a input tap ${TXY[$t]}; sleep 3; done
  link "$BOOK_URL" >/dev/null; sleep 4; back 2
  a input tap ${TXY[news]}; sleep 3
  a am broadcast -a org.pc.helper.DUMP --es id tnews >/dev/null; sleep 2
  p=$(grep -E 'PCDUMP *: tnews ' $OUT/logcat-live.txt | grep 'clk=true' | grep -oE 'b=\[[0-9]+,[0-9]+\]\[[0-9]+,[0-9]+\]' | python3 -c '
import re, sys
for l in sys.stdin:
    x0, y0, x1, y1 = map(int, re.findall(r"\d+", l))
    if y0 > 600 and x1 - x0 > 500 and y1 - y0 > 150:
        print((x0 + x1) // 2, (y0 + y1) // 2); break')
  [ -n "$p" ] && { a input tap $p; sleep 4; back 2; }
  a input tap ${TXY[more]}; sleep 3
  a am broadcast -a org.pc.helper.CLICK --es text Настройки >/dev/null; sleep 3; back 2
  since m-pane 'PCA11Y' | grep -E 'TYPE_WINDOW_STATE_CHANGED|pane=[^ ]|TYPE_ANNOUNCEMENT' | grep -v 'pane= *$' > $OUT/pane-events.txt
  since m-pane 'PCA11Y' > $OUT/a11y-events-all.txt
  log "PANE events ($(wc -l < $OUT/pane-events.txt)): $(grep -oE 'pkg=org.chyavorec.app[^ ]* cls=[^ ]* text=\[[^]]*\]|pane=[^ ].*$' $OUT/pane-events.txt | sed 's/pkg=org.chyavorec.app //' | awk '!s[$0]++' | head -16 | cut -c1-90 | tr '\n' '|')"
  sec "T2. Home as a screen reader sees it: the carousel"
  a input tap ${TXY[home]}; sleep 3
  adump home1
  grep -E 'Следваща новина|Предишна новина|Последни новини' $OUT/a11ytree-home1.txt | head -6 | cut -c1-300 | sed 's/^.*PCDUMP *: //' | tee -a $SUM
  grep -E 'desc=Съобщения' $OUT/a11ytree-home1.txt | head -2 | sed 's/^.*PCDUMP *: //' | cut -c1-260 | tee -a $SUM
  mark m-act; a "am broadcast -a org.pc.helper.ACT --es label 'Следваща новина'" >/dev/null; sleep 3
  log "custom action: $(since m-act 'PCA11Y.*ACT' | tail -1 | cut -c1-200)"
  adump home2
  log "current card before/after 'Следваща новина': $(grep -m1 -E 'acts=.*новина' $OUT/a11ytree-home1.txt | grep -oE 'desc=[^=]* pane' | cut -c1-80) → $(grep -m1 -E 'acts=.*новина' $OUT/a11ytree-home2.txt | grep -oE 'desc=[^=]* pane' | cut -c1-80)"
  sec "T3. Touch exploration (as with TalkBack): the carousel does not auto-advance; control without it"
  anims 1; touchx true
  a am force-stop $PKG; sleep 1; a am start -n $ACT -a android.intent.action.MAIN -c android.intent.category.LAUNCHER >/dev/null 2>&1; sleep 6
  shot a11y16/10-tx-0s; sleep 6.5; shot a11y16/11-tx-6s; sleep 6.5; shot a11y16/12-tx-13s
  if [ -n "${y1:-}" ]; then log "CAROUSEL with touch exploration: diff 0→6.5 s $(python3 $W15 diff $S/10-tx-0s.png $S/11-tx-6s.png 0 $y0 $W $y1), 6.5→13 s $(python3 $W15 diff $S/11-tx-6s.png $S/12-tx-13s.png 0 $y0 $W $y1) (≈0 = no auto-advance)"; fi
  a dumpsys accessibility 2>/dev/null | tr -d '\r' > $OUT/dumpsys-accessibility.txt
  log "dumpsys accessibility: $(grep -oE '(touchExplorationEnabled|isTouchExplorationEnabled|mIsTouchExplorationEnabled|Bound services|Enabled services)[=:][^}]{0,80}' $OUT/dumpsys-accessibility.txt | head -4 | tr '\n' ' ')"
  touchx false
  a am force-stop $PKG; sleep 1; launch >/dev/null; sleep 6
  shot a11y16/13-ctl-0s; sleep 6.5; shot a11y16/14-ctl-6s; sleep 6.5; shot a11y16/15-ctl-13s
  [ -n "${y1:-}" ] && log "CAROUSEL control (no touch exploration): diff 0→6.5 s $(python3 $W15 diff $S/13-ctl-0s.png $S/14-ctl-6s.png 0 $y0 $W $y1), 6.5→13 s $(python3 $W15 diff $S/14-ctl-6s.png $S/15-ctl-13s.png 0 $y0 $W $y1)"
  a11y_off
  local TB; TB=$(a pm list packages 2>/dev/null | tr -d '\r' | grep -iE 'talkback|marvin' | sed 's/package://' | head -1)
  sec "T3b. Real TalkBack ($TB): the carousel does not auto-advance"
  if [ -n "$TB" ]; then
    log "TalkBack services: $(a cmd package query-services --brief -a android.accessibilityservice.AccessibilityService 2>&1 | tr -d '\r' | grep -i "$TB" | tr '\n' ' ')"
    a settings put secure enabled_accessibility_services $TB/com.google.android.marvin.talkback.TalkBackService
    a settings put secure accessibility_enabled 1; sleep 6
    log "TalkBack on: $(a dumpsys accessibility 2>/dev/null | tr -d '\r' | grep -m1 -oE 'touchExplorationEnabled=\w+'); bound: $(a dumpsys accessibility 2>/dev/null | tr -d '\r' | grep -m1 -oE 'Bound services:\{[^}]{0,80}')"
    a am force-stop $PKG; sleep 1; a am start -n $ACT -a android.intent.action.MAIN -c android.intent.category.LAUNCHER >/dev/null 2>&1; sleep 8
    shot a11y16/16-talkback-0s; sleep 6.5; shot a11y16/17-talkback-6s; sleep 6.5; shot a11y16/18-talkback-13s
    [ -n "${y1:-}" ] && log "CAROUSEL with real TalkBack: diff 0→6.5 s $(python3 $W15 diff $S/16-talkback-0s.png $S/17-talkback-6s.png 0 $y0 $W $y1), 6.5→13 s $(python3 $W15 diff $S/17-talkback-6s.png $S/18-talkback-13s.png 0 $y0 $W $y1) (TalkBack's focus box may differ)"
    a settings delete secure enabled_accessibility_services; a settings put secure accessibility_enabled 0; sleep 3
  else log "no TalkBack package on the image"; fi
  sec "T4. Reduced motion (animator_duration_scale 0): the offline bar and the sync banner"
  local mode
  for mode in 0 1; do
    anims $mode; restart; tab home; sleep 4
    rec_toggle reduced$mode-offline offline
    for i in 1 2 3 4 5 6 7 8; do dump; has 'Няма интернет връзка' && break; sleep 1.5; done
    shot a11y16/2$mode-offline; log "anims $mode offline: app in front $(has "package=\"$PKG\"" && echo yes || echo NO); offline bar $(has 'Няма интернет връзка' && echo shown || echo NO); $(texts 6 | cut -c1-160)"
    tab news; sleep 3; dump; shot a11y16/2$mode-news-offline; log "anims $mode news offline: sync banner $(has 'Показваме последно наличните|Последно обновяване|синхрон|кеш' && echo shown || echo '?'); $(texts 8 | cut -c1-200)"
    tab home; sleep 2
    rec_toggle reduced$mode-online online; dump; log "anims $mode back online: offline bar $(has 'Няма интернет връзка' && echo STILL || echo gone)"
  done
  anims 0
  sec "T5. System font 1.3: the carousel title (up to 3 lines)"
  a settings put system font_scale 1.3; sleep 2; restart; tab home; sleep 4; dump; shot a11y16/30-font130-home
  python3 $W16 carousel $OUT/ui.xml $PKG $DENS 1.3 | tee -a $SUM
  a settings put system font_scale 1.0; sleep 2; restart; tab home; sleep 3; dump
  python3 $W16 carousel $OUT/ui.xml $PKG $DENS 1.0 | tail -3 | tee -a $SUM
}
rec_toggle() {  # $1 — етикет, $2 — offline | online: запис 6 s около превключването, кадрите — 10 fps
  local d=$OUT/rec/$1 rp
  mkdir -p $d; a "rm -f /sdcard/rt.mp4"
  T=30 a screenrecord --size 540x1200 --time-limit 12 /sdcard/rt.mp4 & rp=$!
  sleep 1.5; $2; wait $rp
  timeout 60 adb pull /sdcard/rt.mp4 $d/rec.mp4 >/dev/null 2>&1
  if [ -s $d/rec.mp4 ] && command -v ffmpeg >/dev/null; then
    ffmpeg -loglevel error -i $d/rec.mp4 -vf fps=10 $d/f%03d.png
    log "REC $1: $(python3 $W16 frames $d 120 2400)"
    rm -f $d/f*.png
  else log "REC $1: no recording"; fi
}

# ================================================================= 8. ТАБЛЕТ
two_pane() { dump; echo "list $(has "$RX_RESULTS|text=\"Новини\"|Искам да прочета" && echo yes || echo no); detail back/title $(has "$RX_BACK" && echo yes || echo no); $(python3 $W16 panes $OUT/ui.xml $W | cut -c1-260)"; }
tb_first() {  # първият висок ред на списъка (новини)
  dump; python3 $H_/find.py $OUT/ui.xml --clickables $((H/4)) $((H*85/100)) 150 | head -1
}
tb_book() {  # каталогът → „12“ → „12 -те вятъра“
  ready; tab catalog; waitfor "$RX_RESULTS" 60; sleep 1
  dump; grep -q 'text="12"' $OUT/ui.xml || search_catalog 12
  # Първото съвпадение е подсказката (chip) над резултатите — редът е последното.
  tap text '12 -те вятъра' --last || { log "TABLET: no '12 -те вятъра' row: $(texts 10 | cut -c1-200)"; debug_ui tb-row; return 1; }
  sleep 4
}
keyc() {  # клавишна комбинация (API 33+: input keycombination)
  local r; r=$(a input keycombination "$@" 2>&1 | tr -d '\r' | head -1); [ -n "$r" ] && log "keycombination $*: $r"; sleep 2
}
part_tablet() {
  local p i
  warmup
  want_add "$BOOK_URL"; want_add "$SITE/elektronen-katalog?inv=1"
  sec "TB1. Tablet: wm size 2560x1600, density 320 (1280x800 dp) — Catalogue → the book beside the list"
  a wm size 2560x1600; a wm density 320; sleep 4; restart; dims
  log "window ${W}x${H} px = $((W/2))x$((H/2)) dp; tab labels: $(for t in home catalog my news more; do [ -n "$(xy text "$(tablabel $t)")" ] && printf '%s ' $t; done)"
  shot tablet/00-home
  tb_book && { shot tablet/01-catalog-book; log "CATALOGUE + book: $(two_pane)"; }
  sec "TB2. Back closes the detail first"
  back 2; shot tablet/02-after-back; log "after Back: $(two_pane)"
  sec "TB3. News → the article beside the list"
  ready; tab news; sleep 3; segstate | grep -q 'News\[checked=true' || segment news
  p=$(tb_first); [ -n "$p" ] && { a input tap $p; sleep 4; shot tablet/03-news-article; log "NEWS + article: $(two_pane)"; back 2; log "after Back: $(two_pane)"; }
  sec "TB4. Моето › Искам да прочета → the book beside the list"
  open_want_list && { tap text '12 -те вятъра' && { sleep 4; shot tablet/04-want-book; log "WANT + book: $(two_pane)"; back 2; log "after Back: $(two_pane)"; }; }
  sec "TB5. Rotation with a book open (density 320: portrait is 800 dp wide)"
  tb_book; log "before rotation: $(two_pane)"
  rot 1; sleep 3; shot tablet/05-rotated; log "rotated (${W}x${H} px = $((W/2)) dp wide): $(two_pane)"
  rot 0; sleep 3; shot tablet/06-rotated-back; log "rotated back: $(two_pane)"
  sec "TB6. Rotation at density 240 (1706x1066 dp; portrait 1066 dp wide — two panes both ways)"
  a wm density 240; sleep 4; restart; dims
  tb_book; log "density 240 landscape: $(two_pane)"; shot tablet/07-d240-land
  rot 1; sleep 3; shot tablet/08-d240-portrait; log "density 240 portrait (selection kept?): $(two_pane)"
  rot 0; sleep 3; log "density 240 back: $(two_pane)"
  sec "TB7. Narrow the window (phone size) with the book open → the detail goes full screen"
  a wm size 1080x2400; a wm density 420; sleep 5; dims; shot tablet/09-narrowed; log "narrowed: $(two_pane)"
  back 2; log "Back on the narrowed window: $(two_pane)"
  sec "TB8. Keyboard (tablet size): Ctrl+1..5, Ctrl+F, '/', Esc"
  a wm size 2560x1600; a wm density 320; sleep 4; restart; dims
  for i in 2 3 4 5 1; do keyc KEYCODE_CTRL_LEFT KEYCODE_$i; dump; log "Ctrl+$i → $(python3 $W16 tabsel $OUT/ui.xml)"; done
  keyc KEYCODE_CTRL_LEFT KEYCODE_F; dump; shot tablet/10-ctrl-f-home; log "Ctrl+F on Home: $(sbstate)"
  a input keyevent KEYCODE_ESCAPE; sleep 2; log "Esc: $(sbstate)"
  a input keyevent KEYCODE_ESCAPE; sleep 2; log "Esc again: $(sbstate)"
  keyc KEYCODE_CTRL_LEFT KEYCODE_2; a input keyevent KEYCODE_SLASH; sleep 2; dump; shot tablet/11-slash-catalog
  log "'/' on Catalogue: focused field $(python3 $H_/find.py $OUT/ui.xml --nodes class EditText | grep -c 'focused' ) IME $(a dumpsys input_method | tr -d '\r' | grep -m1 -oE 'mInputShown=(true|false)'); $(grep -oE 'class="android.widget.EditText"[^>]*focused="(true|false)"' $OUT/ui.xml | grep -oE 'focused="[a-z]+"' | head -1)"
  a input keyevent KEYCODE_ESCAPE; sleep 1.5; a input keyevent KEYCODE_ESCAPE; sleep 1.5
  tb_book; log "book open: $(two_pane)"; a input keyevent KEYCODE_ESCAPE; sleep 2; log "Esc on the book: $(two_pane)"
  keyc KEYCODE_CTRL_LEFT KEYCODE_4; keyc KEYCODE_CTRL_LEFT KEYCODE_F; dump; shot tablet/12-ctrl-f-news; log "Ctrl+F on News: fg $(fg); $(texts 6 | cut -c1-160)"
  a input keyevent KEYCODE_ESCAPE; sleep 2
  sec "TB9. Phone in landscape: unchanged (single pane)"
  a wm size reset; a wm density reset; sleep 3; restart; rot 1; sleep 3; dims
  tb_book; shot tablet/13-phone-landscape-book; log "phone landscape ${W}x${H}: $(two_pane)"
  back 2; rot 0
}

# ================================================================= 9. ВЪВЕДЕНИЕТО
onb_page() {  # $1 — етикет
  sleep ${2:-2}; dump; shot onboard/$1; cp $OUT/ui.xml $OUT/onb-$1.xml
  log "ONB $1: skip $(has 'text="Пропусни"' && echo yes || echo NO); $(texts 16 | cut -c1-520)"
}
onb_fresh() {
  a am force-stop $PKG; timeout 60 adb uninstall $PKG >/dev/null 2>&1
  timeout 180 adb install app.apk 2>&1 | tail -1
  a cmd locale set-app-locales $PKG --locales bg-BG
  log "fresh install; POST_NOTIFICATIONS: $(perm_notif)"
  launch >/dev/null; sleep 5
}
main_asks() {  # $1 — етикет: пита ли главният екран за известия?
  sleep 8; dump; shot onboard/$1
  log "MAIN $1: system permission dialog $(has 'package="com.(google.)?android.permissioncontroller"' && echo SHOWN || echo no); in-app ask $(has 'text="(Не сега|Разреши известията|Включи известията)"' && echo SHOWN || echo no); $(perm_notif); $(texts 6 | cut -c1-160)"
}
part_onboard() {
  local h0 h1
  DENS=$(dens)
  sec "OB1. Fresh install, no notification permission: page 1 (welcome, three feature rows)"
  onb_fresh; onb_page 01-page1 2
  log "page 1 features: news $(has 'text="Новини и събития"' && echo yes || echo NO), catalogue $(has 'text="Каталог и „Искам да прочета“"' && echo yes || echo NO), card $(has 'text="Читателска карта"' && echo yes || echo NO)"
  sec "OB2. Page 2: text-size chips (apply at once), 'Разреши известията' → the system prompt (API $SDK)"
  tap text '=Напред'; onb_page 02-page2
  h0=$(python3 $W16 nodeh $OUT/ui.xml "Размерът на текста и известията" $DENS)
  log "chips: $(for c in Малък Стандартен Голям 'Много голям'; do printf '%s[%s] ' "$c" "$(xy --state text "=$c" | grep -oE 'checked=[a-z]+|selected=[a-z]+' | tr '\n' ',')"; done)"
  tap text '=Много голям'; onb_page 03-page2-xlarge 1.5
  h1=$(python3 $W16 nodeh $OUT/ui.xml "Размерът на текста и известията" $DENS)
  log "TEXT SIZE applied at once: title height $h0 dp → $h1 dp; chips: $(for c in Стандартен 'Много голям'; do printf '%s[%s] ' "$c" "$(xy --state text "=$c" | grep -oE 'checked=[a-z]+|selected=[a-z]+' | tr '\n' ',')"; done)"
  tap text '=Стандартен'; sleep 1.5
  dump; for i in 1 2 3; do [ -n "$(xy text '=Разреши известията')" ] && break; sw_up 400; sleep 1; dump; done
  tap text '=Разреши известията'; sleep 3; dump; shot onboard/04-system-prompt
  log "after 'Разреши известията': system dialog $(has 'package="com.(google.)?android.permissioncontroller"' && echo SHOWN || echo absent); $(texts 6 | cut -c1-200)"
  tap text '=Allow|=Разрешаване|=Разреши' --last; sleep 2.5; onb_page 05-page2-after-allow 0.5
  log "status: $(grep -oE 'text="(Известията са включени|Засега без известия)[^"]*"' $OUT/ui.xml | head -1); $(perm_notif)"
  sec "OB3. Page 3: sign in / add a card; then the main screen does not ask again"
  tap text '=Напред'; onb_page 06-page3
  tap text '=Продължи без вход' || tap text '=Продължи' || tap text '=Пропусни'
  main_asks 07-main-after-allow
  a am force-stop $PKG; launch >/dev/null; main_asks 08-main-restart
  sec "OB4. Deny in the onboarding: the main screen does not ask again"
  onb_fresh; tap text '=Напред'; sleep 2; dump; for i in 1 2 3; do [ -n "$(xy text '=Разреши известията')" ] && break; sw_up 400; sleep 1; dump; done
  tap text '=Разреши известията'; sleep 3; dump; shot onboard/10-system-prompt
  tap text 't allow' || tap text '=Не разрешавам'; sleep 2.5; onb_page 11-page2-after-deny 0.5
  log "status: $(grep -oE 'text="(Известията са включени|Засега без известия)[^"]*"' $OUT/ui.xml | head -1); $(perm_notif)"
  tap text '=Напред'; onb_page 12-page3
  tap text '=Продължи без вход' || tap text '=Продължи' || tap text '=Пропусни'
  main_asks 13-main-after-deny
  a am force-stop $PKG; launch >/dev/null; main_asks 14-main-after-deny-restart
  sec "OB5. 'Пропусни' on page 1 (the permission not answered)"
  onb_fresh; tap text '=Пропусни'; main_asks 20-main-after-skip
}

# ================================================================= КРЪГ b: повторното изтегляне след „Отказ“ (255fdc6)
upd_wait_bg() {  # $1 — етикет: известието за хода и „готова за инсталиране“, до ~5 min от t0
  local i st=none first=""
  for i in $(seq 1 70); do
    upd_notifs $1 > $OUT/$1-n.txt
    if [ -z "$first" ] && grep -q 'Изтегляне на версия' $OUT/$1-n.txt; then first=$(( $(now_ms) - t0 )); log "$1: progress notification at +${first} ms: $(head -1 $OUT/$1-n.txt | cut -c1-200)"; fi
    grep -q 'готова за инсталиране' $OUT/$1-n.txt && { st=ready; break; }
    [ $((i % 6)) = 0 ] && log "$1 +$(( $(now_ms) - t0 )) ms: $(head -1 $OUT/$1-n.txt | cut -c1-160); files [$(upd_files)]"
    sleep 4
  done
  log "$1 RESULT: $st after $(( $(now_ms) - t0 )) ms (first progress notification at +${first:-never} ms); app process '$(a pidof $PKG | tr -d '\r')'; files [$(upd_files)]"
  [ $st = ready ]
}
upd_fresh_dialog() {  # изтеглените файлове изтрити, приложението наново → диалогът с „Изтегли и инсталирай“
  # Данните на приложението се изчистват: проверката при пускане е веднъж на няколко часа, а
  # изтегленият файл иначе стига направо до „Инсталирай“.
  a am force-stop $PKG; a pm clear $PKG >/dev/null; a "rm -rf /data/data/$PKG/no_backup/updates"
  a pm grant $PKG android.permission.POST_NOTIFICATIONS; a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  a cmd locale set-app-locales $PKG --locales bg-BG
  launch >/dev/null
  upd_dialog 'text="Изтегли и инсталирай"' || { log "no dialog with 'Изтегли и инсталирай': $(texts 8 | cut -c1-200)"; return 1; }
}
upd_cancel_cycle() {  # $1 — етикет: изтегляне ~6 s, Home, „Отказ“ от известието, приложението наново
  tap text '=Изтегли и инсталирай' || return 1
  sleep 6; go_home 1
  a cmd statusbar expand-notifications; sleep 2.5; dump; shot update/$1-shade-downloading
  log "$1 shade: $(grep -oE 'text="[^"]*(Изтегляне на версия|Отказ)[^"]*"' $OUT/ui.xml | head -2 | tr '\n' ' ')"
  tap text '=Отказ' || log "$1: no 'Отказ' in the shade"
  sleep 4; log "$1 after 'Отказ': $(upd_notifs $1-c | tail -1); files [$(upd_files)]"
  a cmd statusbar collapse; sleep 1
  launch >/dev/null; upd_dialog 'text="Изтегли и инсталирай"' || log "$1: no 'Изтегли и инсталирай' after relaunch: $(texts 8 | cut -c1-200)"
  shot update/$1-dialog-again
}
part_upd2() {
  sec "R0. The v1.2.30 code as 1.2.30-pre (versionCode $VC_BASE, 255fdc6) → the test v1.2.30; mobile data only, throttled"
  curl -sS --max-time 30 "$STAGE_URL/update.json" -o $OUT/update.json
  log "staged: $(python3 -c 'import json,sys; d=json.load(open(sys.argv[1])); print(d["versionCode"], d["versionName"], d["size"])' $OUT/update.json 2>&1)"
  inst app-new-upd.apk new-upd apk-new-upd-ref.txt || return
  a appops set $PKG REQUEST_INSTALL_PACKAGES allow
  a svc wifi disable; a svc data enable; sleep 6
  throttle_on
  launch >/dev/null; upd_dialog 'text="Изтегли и инсталирай"' || { log "no update dialog: $(texts 10 | cut -c1-200)"; return; }
  sec "R1. Plain download: 'Изтегли и инсталирай', Home at +1 s → finishes in the background"
  tap text '=Изтегли и инсталирай' && { t0=$(now_ms); sleep 1; go_home 1; }
  upd_wait_bg r1-plain
  sec "R2. Download → 'Отказ' from the notification → 'Изтегли и инсталирай' again, Home at +1 s"
  upd_fresh_dialog && upd_cancel_cycle r2 && {
    mark m-r2; tap text '=Изтегли и инсталирай' && { t0=$(now_ms); sleep 1; go_home 1; }
    upd_wait_bg r2-home-1s
    log "r2 logcat (app/FGS): $(since m-r2 "ForegroundService|startForeground|FGS|SystemForegroundService|$PKG" | grep -vE 'Choreographer|OpenGLRenderer' | cut -c19-200 | head -6 | tr '\n' '~')"
  }
  sec "R3. The same, Home at +17 s"
  upd_fresh_dialog && upd_cancel_cycle r3 && {
    mark m-r3; tap text '=Изтегли и инсталирай' && t0=$(now_ms)
    for i in 1 2 3 4; do sleep 4; dump; log "r3 in the app +$(( $(now_ms) - t0 )) ms: $(grep -oE 'text="([0-9]+ ?%|Изтегляне на новата версия…|Проверка[^"]*)"' $OUT/ui.xml | head -2 | tr '\n' ' ')'Отказ' $(has 'text="Отказ"' && echo yes || echo no); notif: $(upd_notifs r3-in-$i | head -1 | cut -c1-120)"; done
    while [ $(( $(now_ms) - t0 )) -lt 17000 ]; do sleep 0.5; done
    go_home 1; shot update/r3-home-at-17s
    upd_wait_bg r3-home-17s
  }
  sec "R4. 'готова за инсталиране' → installer → v1.2.30 and 'Какво ново'"
  throttle_off
  a cmd statusbar expand-notifications; sleep 2.5; dump; shot update/r4-shade-ready
  if tap text 'готова за инсталиране'; then
    sleep 4; dump; has 'text="Инсталирай"' && tap text '=Инсталирай'
    installer_confirm $NEW_VC r4 && log "UPDATE: installed $(ver)" || log "UPDATE: NOT installed: $(ver)"
    sleep 3; has "$RX_WHATSNEW" || { launch >/dev/null; sleep 3; }
    whatsnew_check r4-whats-new
  else log "no 'готова за инсталиране' in the shade: $(texts 10 | cut -c1-200)"; fi
  a cmd statusbar collapse; a svc wifi enable
}

# ================================================================= main (вълна 16)
part_w16() {
  case $PART in
    unlock33|unlock34) part_unlock;;
    login) part_login16;;
    update) part_update16;;
    upd33|upd34) part_upd2;;
    events) part_events16;;
    emoji26|emoji28|emoji33) part_emoji;;
    dialogs) part_dialogs;;
    widgetlang) part_widgetlang16;;
    a11y16) part_a11y16;;
    tablet) part_tablet;;
    onboard) part_onboard;;
    *) return 1;;
  esac
  return 0
}
