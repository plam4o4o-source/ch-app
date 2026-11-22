#!/usr/bin/env bash
# Gradle команда с още един опит — САМО ако е паднала при свалянето на зависимостите (Maven
# Central / Google Maven: 429, прекъсната връзка, изтекло време, временно „не е намерен“).
# Gradle сам опитва всяка заявка няколко пъти (виж gradle.properties), но след това изключва
# хранилището до края на build-а, а 404 не опитва пак — повторното пускане минава.
#
# Повтаря се само ако ВСЕКИ блок „* What went wrong:“ от отчета на Gradle е за зависимост/мрежа
# и нито един не е за компилация, тестове или lint. Без такъв блок (компилация, тест, OOM,
# спрян процес) — кодът на грешката веднага, без повторение.
#
# Употреба: bash .github/scripts/gradle-retry.sh ./gradlew задачи…
set -uo pipefail

# Подписите на мрежова грешка и на грешка в кода — за текста на блоковете (не за целия лог:
# тестовете печатат и свои „Connection reset“, а MockWebServer — и 429).
NETWORK='Could not resolve|could not resolve plugin artifact|Could not GET|Could not HEAD|status code 429|Too Many Requests|Read timed out|Connection reset'
CODE='There were failing tests|Compilation error|Compilation failed|Lint found|Timeout has been exceeded|No matching variant|Could not resolve project'
DELAY=${GRADLE_RETRY_DELAY:-60}

LOG=$(mktemp)
trap 'rm -f "$LOG"' EXIT

network_failure() {
  awk -v net="$NETWORK" -v code="$CODE" '
    function done_block() { if (block !~ net || block ~ code) bad++; inb = 0 }
    /^\* What went wrong:/ { if (inb) done_block(); inb = 1; n++; block = ""; next }
    inb && (/^\* (Try|Exception is):/ || /^BUILD FAILED/) { done_block(); next }
    inb { block = block "\n" $0 }
    END { if (inb) done_block(); exit !(n > 0 && bad == 0) }
  ' "$1"
}

# Само проверка на разпознаването върху готов лог (за тест на скрипта).
if [ "${1:-}" = "--classify" ]; then
  network_failure "$2" && echo retry || echo no-retry
  exit 0
fi

"$@" 2>&1 | tee "$LOG"
STATUS=${PIPESTATUS[0]}
[ "$STATUS" -eq 0 ] && exit 0
if ! network_failure "$LOG"; then
  exit "$STATUS"
fi
echo "::warning::Gradle падна при свалянето на зависимости (мрежа / хранилище) — нов опит след ${DELAY} s: $*"
sleep "$DELAY"
"$@"
