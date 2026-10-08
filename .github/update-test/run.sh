#!/usr/bin/env bash
# Самообновяване: инсталира публикуваната v1.2.10, отваря я и натиска бутоните
# за обновяване като човек; накрая проверява инсталираната версия и logcat.
set -u
PKG=org.chyavorec.app
OUT=out; mkdir -p $OUT
dump() {
  rm -f $OUT/ui.xml; timeout 10 adb shell rm -f /sdcard/ui.xml
  timeout 25 adb shell uiautomator dump /sdcard/ui.xml >/dev/null 2>&1
  timeout 15 adb pull /sdcard/ui.xml $OUT/ui.xml >/dev/null 2>&1
  [ -s $OUT/ui.xml ] || { echo "(ui dump timed out)"; return 1; }
  echo "--- screen: $(grep -o 'text="[^"]*"\|content-desc="[^"]*"' $OUT/ui.xml | grep -v '=""' | head -30 | tr '\n' ' ')"
}
tapif() {  # $1 атрибут, $2 стойност — натиска, ако е на последния dump
  [ -s $OUT/ui.xml ] || return 1
  xy=$(python3 .github/update-test/find.py $OUT/ui.xml "$1" "$2")
  [ -n "$xy" ] || return 1
  echo ">>> tap $1=$2 at $xy"; timeout 10 adb shell input tap $xy; return 0
}
shot() { timeout 20 adb shell screencap -p /sdcard/s.png; timeout 20 adb pull /sdcard/s.png "$OUT/$1.png" >/dev/null 2>&1; }
ver() { timeout 15 adb shell dumpsys package $PKG | grep -m2 -E "versionName|versionCode"; }

adb install -r old.apk
adb shell pm grant org.chyavorec.app android.permission.POST_NOTIFICATIONS || true
ver
adb logcat -c
adb shell am start -n $PKG/.MainActivity
sleep 15
dump; tapif text "Skip"; sleep 4
for i in $(seq 1 20); do
  dump || { sleep 5; continue; }
  shot "step-$i"
  if tapif text "=Open settings" || tapif text "=Allow from this source" || tapif text "=Download and install" \
     || tapif text "=Install" || tapif text "=Update" || tapif text "=Open"; then
    if grep -q 'Allow from this source' $OUT/ui.xml; then sleep 2; timeout 10 adb shell input keyevent KEYCODE_BACK; fi
    sleep 6; continue
  fi
  # приложението е рестартирано след обновяване или е затворено
  if grep -q "version 1.2.11\|1.2.11" $OUT/ui.xml; then :; fi
  if ! timeout 10 adb shell pidof $PKG >/dev/null; then echo "process not running (step $i)"; fi
  sleep 6
done
echo "===== installed version ====="
ver
timeout 30 adb logcat -d > $OUT/logcat.txt
echo "===== notifications ====="
timeout 20 adb shell dumpsys notification --noredact > $OUT/notif.txt; grep -n "chyavorec" $OUT/notif.txt | head -10; grep -E "android.title=|android.text=" $OUT/notif.txt | head -20
echo "===== installer / update log ====="
grep -nE "PackageInstaller|PackageManager|INSTALL_FAILED|AppUpdater|update-|chyavorec.*(Exception|Error)|FATAL" $OUT/logcat.txt | grep -v "dexopt" | head -80
grep -n -A40 "FATAL EXCEPTION" $OUT/logcat.txt | head -80
true
