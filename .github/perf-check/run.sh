#!/usr/bin/env bash
# Проверка на оптимизирания release на емулатор: размер, студен старт, основните
# екрани, скенерът (ZXing) и сривове в logcat.
set -u
PKG=org.chyavorec.app
OUT=out; mkdir -p $OUT
dump() {
  rm -f $OUT/ui.xml; timeout 10 adb shell rm -f /sdcard/ui.xml
  timeout 25 adb shell uiautomator dump /sdcard/ui.xml >/dev/null 2>&1
  timeout 15 adb pull /sdcard/ui.xml $OUT/ui.xml >/dev/null 2>&1
  [ -s $OUT/ui.xml ] || { echo "(ui dump timed out)"; return 1; }
  echo "--- screen: $(grep -o 'text="[^"]*"\|content-desc="[^"]*"' $OUT/ui.xml | grep -v '=""' | head -25 | tr '\n' ' ')"
}
tap() { dump || return 1; xy=$(python3 .github/perf-check/find.py $OUT/ui.xml "$1" "$2"); [ -n "$xy" ] || { echo "not found: $1=$2"; return 1; }; echo ">>> tap $1=$2"; timeout 10 adb shell input tap $xy; }
shot() { timeout 20 adb shell screencap -p /sdcard/s.png; timeout 20 adb pull /sdcard/s.png "$OUT/$1.png" >/dev/null 2>&1; }

echo "APK size: $(stat -c %s app.apk) bytes"
unzip -l app.apk | grep -E "lib/|\.so$" | head; unzip -l app.apk | tail -1
adb install -r app.apk || { echo "RESULT: install failed"; exit 1; }
adb shell pm grant org.chyavorec.app android.permission.POST_NOTIFICATIONS || true
adb logcat -c
(adb logcat -v time > $OUT/logcat-live.txt 2>&1 &)
for i in 1 2 3; do
  timeout 10 adb shell am force-stop $PKG; sleep 2
  timeout 60 adb shell am start -W -n $PKG/.MainActivity | grep -E "TotalTime|WaitTime"
done
sleep 8; shot 01-start
tap text "=Skip"; sleep 5; shot 02-home
waitres() {  # чака текст „results“ до $1 секунди; печата колко е чакал
  local t0=$(date +%s)
  for i in $(seq 1 $(( $1 / 3 ))); do dump >/dev/null 2>&1; grep -q 'results' $OUT/ui.xml && { echo "=== results after $(( $(date +%s) - t0 )) s"; dump; return 0; }; sleep 3; done
  echo "=== NO results after $1 s"; dump; return 1
}
tap text "=Catalogue" || tap content-desc "Catalogue"; waitres 90; shot 03-catalog
# книга от списъка
dump && xy=$(python3 .github/perf-check/find.py $OUT/ui.xml content-desc "вятъра") && [ -n "$xy" ] && { echo ">>> open book"; timeout 10 adb shell input tap $xy; sleep 5; shot 04-book; dump; timeout 10 adb shell input keyevent KEYCODE_BACK; sleep 3; dump; }
# търсене (FTS): поле → „12“
tap text "Search by" ; sleep 2; timeout 10 adb shell input text "12"; sleep 4; shot 05-search; dump
xy=$(python3 .github/perf-check/find.py $OUT/ui.xml content-desc "вятъра"); [ -n "$xy" ] && { echo ">>> open book from search"; timeout 10 adb shell input tap $xy; sleep 5; dump; shot 05b-book; timeout 10 adb shell input keyevent KEYCODE_BACK; sleep 3; }
timeout 10 adb shell input keyevent KEYCODE_BACK; sleep 2; dump
tap text "=Home"; sleep 3
# студен старт с готов индекс: колко бързо идват резултатите
timeout 10 adb shell am force-stop $PKG; sleep 2; timeout 60 adb shell am start -W -n $PKG/.MainActivity | grep -E "TotalTime"; sleep 4
tap text "=Catalogue" || tap content-desc "Catalogue"; waitres 60
tap text "=Home"; sleep 3
tap text "=My"; sleep 4; shot 06-my
tap text "=More"; sleep 3; shot 07-more
tap text "Settings" || tap text "Настройки"; sleep 4; dump; shot 08-settings
timeout 20 adb shell dumpsys meminfo $PKG | grep -E "TOTAL PSS|TOTAL:|Java Heap|Native Heap" | head -5
timeout 30 adb logcat -d > $OUT/logcat.txt || cp $OUT/logcat-live.txt $OUT/logcat.txt
echo "===== emulator alive? ====="; timeout 10 adb get-state || echo "EMULATOR GONE"
echo "===== tail of live logcat ====="; tail -60 $OUT/logcat-live.txt | cut -c1-220
echo "===== FATAL ====="; grep -n -A40 "FATAL EXCEPTION" $OUT/logcat-live.txt | head -80 || true
echo "===== StrictMode/ANR ====="; grep -nE "ANR in|Application Not Responding" $OUT/logcat.txt | head
FAIL=0
grep -q "FATAL EXCEPTION" $OUT/logcat-live.txt && FAIL=1
timeout 10 adb shell pidof $PKG >/dev/null || { echo "process not running"; }
[ $FAIL = 0 ] && echo "RESULT: OK" || echo "RESULT: crash"
exit $FAIL
