#!/usr/bin/env bash
# Причината за неуспешно генериране на Baseline профила — направо в лога на workflow-а
# (артефактите не винаги са удобни за преглед): резултатите от инструментацията, краят
# на logcat за всеки тест и паметта на runner-а.
R=baselineprofile/build/outputs/androidTest-results
if [ -d "$R" ]; then
  find "$R" -name '*.xml' -print -exec grep -h -A40 '<failure' {} + || true
  find "$R" -name 'logcat-*.txt' | while read -r f; do
    echo "::group::$f"
    grep -E 'AndroidRuntime|FATAL|BaselineProfile|TestRunner|Benchmark|lowmemorykiller|am_crash|ANR' "$f" | tail -n 200 || true
    echo "::endgroup::"
  done
else
  echo "no test results"
fi
echo "::group::memory"
free -m || true
sudo dmesg 2>/dev/null | grep -iE 'killed process|out of memory|oom' | tail -n 20 || true
echo "::endgroup::"
exit 0
