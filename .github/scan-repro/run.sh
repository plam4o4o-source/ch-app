#!/usr/bin/env bash
# Възпроизвеждане на срив при отваряне на скенера: инсталира APK-то на емулатор,
# минава въведението като човек, натиска иконата за сканиране и записва logcat.
set -u
PKG=org.chyavorec.app
OUT=out; mkdir -p $OUT
tap() {  # $1 = атрибут (text|content-desc), $2 = стойност (подниз)
  rm -f $OUT/ui.xml; adb shell rm -f /sdcard/ui.xml
  timeout 25 adb shell uiautomator dump /sdcard/ui.xml >/dev/null 2>&1
  timeout 15 adb pull /sdcard/ui.xml $OUT/ui.xml >/dev/null 2>&1
  [ -s $OUT/ui.xml ] || { echo "ui dump timed out (screen never idle)"; return 1; }
  xy=$(python3 .github/scan-repro/find.py $OUT/ui.xml "$1" "$2")
  echo "--- screen texts:"; grep -o 'text="[^"]*"\|content-desc="[^"]*"' $OUT/ui.xml | grep -v '=""' | head -25 | tr '\n' ' '; echo
  if [ -n "$xy" ]; then echo "tap $1=$2 at $xy"; adb shell input tap $xy; return 0; fi
  echo "not found: $1=$2"; return 1
}
shot() { timeout 20 adb shell screencap -p /sdcard/s.png; timeout 20 adb pull /sdcard/s.png "$OUT/$1.png" >/dev/null 2>&1; }

adb install -r app.apk
adb logcat -c
adb shell am start -n $PKG/.MainActivity
sleep 12; shot 01-start
tap text "Skip" || tap text "Пропусни"; sleep 3
tap text "Continue as guest" || tap text "Продължи без вход"; sleep 6; shot 02-home
tap content-desc "Scan barcode" || tap content-desc "Сканирай баркод"; sleep 6; shot 03-after-scan-tap
timeout 20 adb logcat -d > $OUT/logcat-after-tap.txt
# Системният диалог за камерата (ако се появи)
tap text "While using the app" || tap text "Only this time" || tap text "Allow" || true
sleep 12; shot 04-after-permission
timeout 30 adb logcat -d > $OUT/logcat.txt
echo "===== FATAL ====="
grep -n -A60 "FATAL EXCEPTION" $OUT/logcat.txt || echo "NO FATAL EXCEPTION"
echo "===== process ====="
timeout 10 adb shell pidof $PKG || echo "process not running"
grep -nE "AndroidRuntime|ScanScreen|CameraX|MlKit|mlkit|camera" $OUT/logcat.txt | grep -iE " E |FATAL|Exception" | head -60
FAIL=0
if grep -q "NoSuchMethodException.*Registrar" $OUT/logcat.txt; then echo "RESULT: ML Kit registrars still stripped"; FAIL=1; fi
if grep -q "FATAL EXCEPTION" $OUT/logcat.txt; then echo "RESULT: crash"; FAIL=1; fi
timeout 20 adb shell dumpsys activity activities | grep -m3 "mResumedActivity\|topResumedActivity" || true
[ $FAIL = 0 ] && echo "RESULT: OK — no crash, ML Kit registrars present"
exit $FAIL
