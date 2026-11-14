#!/usr/bin/env bash
# Dex metadata (.dm) за самообновяването: профилът за бърз старт във вида, който ART чете при
# инсталиране — с него приложението е компилирано (speed-profile) още при обновяването, а не
# едва след първото стартиране (ProfileInstaller) или след часове (фоновата компилация).
#
# За всеки формат на ART — един .dm (zip с primary.prof и, ако profgen го направи, primary.profm):
#   v0_1_0_p — Android 9–11 (API 28–30), v0_1_5_s — Android 12+ (API 31+).
# Профилът е човешки четимият профил, който AGP е слял (генерираният + ръчните правила + тези
# на библиотеките) и R8 е пренаписал за обфускираните имена в APK-а; profgen го превежда в
# двоичен вид спрямо dex файловете на самия APK (контролните им суми са в профила).
#
# Употреба: dex-metadata.sh <apk> <изходна папка> <име>
#   → <папка>/<име>.<формат>.dm и <папка>/dm.json ([{"format","minSdk","maxSdk","file","sha256","size"}])
# Никога не проваля стъпката: без profgen или при грешка — ::warning:: и без .dm (изданието и
# обновяването вървят и без тях). Изходът — в лога и в $GITHUB_STEP_SUMMARY.
set -uo pipefail

APK=${1:?apk}
OUT=${2:?out dir}
NAME=${3:?name}
SUMMARY=${GITHUB_STEP_SUMMARY:-/dev/null}
VARIANT=${VARIANT:-prodRelease}
mkdir -p "$OUT"
OUT=$(cd "$OUT" && pwd)
echo "[]" > "$OUT/dm.json"

warn() { echo "::warning::dex metadata: $*"; echo "- .dm: $*" >> "$SUMMARY"; exit 0; }

[ -f "$APK" ] || warn "no APK ($APK)"
PROFGEN=$(ls "${ANDROID_HOME:-/nonexistent}"/cmdline-tools/*/bin/profgen 2>/dev/null | sort -V | tail -1)
[ -n "$PROFGEN" ] && [ -x "$PROFGEN" ] || warn "profgen not found in the Android SDK cmdline-tools"
echo "profgen: $PROFGEN"
echo "::group::profgen help"; "$PROFGEN" --help 2>&1 | head -40; "$PROFGEN" bin --help 2>&1 | head -60; "$PROFGEN" extractProfile --help 2>&1 | head -30; echo "::endgroup::"

# Сливаният и пренаписан от R8 профил (входът на AGP за assets/dexopt/baseline.prof).
HRF=${HRF:-}
if [ -z "$HRF" ]; then
  echo "Candidate human-readable profiles:"
  find app/build/intermediates -path "*${VARIANT}*" -name '*.txt' \( -path '*art_profile*' -o -path '*baseline*' \) -printf '%s %p\n' 2>/dev/null | sort -k2 || true
  HRF=$(find app/build/intermediates -path "*${VARIANT}*" -path '*r8_art_profile*' -path '*minify*' -name 'baseline-prof.txt' 2>/dev/null | head -1)
fi
[ -n "$HRF" ] && [ -s "$HRF" ] || warn "no merged R8-rewritten profile for $VARIANT"
echo "profile: $HRF ($(wc -l < "$HRF") lines)"

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
entries=()
for spec in "v0_1_0_p:28:30" "v0_1_5_s:31:"; do
  IFS=: read -r fmt min max <<< "$spec"
  d="$work/$fmt"; mkdir -p "$d"
  if ! "$PROFGEN" bin "$HRF" --apk "$APK" --output "$d/primary.prof" --output-meta "$d/primary.profm" --profile-format "$fmt" > "$d/log.txt" 2>&1; then
    cat "$d/log.txt"
    warn "profgen failed for $fmt"
  fi
  [ -s "$d/primary.prof" ] || warn "empty primary.prof for $fmt"
  dm="$OUT/$NAME.$fmt.dm"
  rm -f "$dm"
  # Без компресия на вече компресирания профил — и без пътища/времена (възпроизводим zip).
  (cd "$d" && touch -d '2000-01-01 00:00:00' primary.prof* && zip -q -X -0 "$dm" primary.prof $( [ -s primary.profm ] && echo primary.profm ))
  [ -s "$dm" ] || warn "zip failed for $fmt"
  echo "== $(basename "$dm"): $(stat -c %s "$dm") B"
  unzip -lv "$dm" | sed -n '1,6p'
  # Обратна проверка: двоичният профил се чете обратно спрямо APK-а (броят правила).
  if "$PROFGEN" dumpProfile --profile "$d/primary.prof" --apk "$APK" --output "$d/dump.txt" > "$d/dump.log" 2>&1; then
    echo "   read back: $(wc -l < "$d/dump.txt") rules"
  else
    echo "   read back failed: $(head -c 300 "$d/dump.log")"
  fi
  # Формат P е същият, в който AGP вгражда профила в APK-а (assets/dexopt/baseline.prof).
  if [ "$fmt" = v0_1_0_p ] && unzip -p "$APK" assets/dexopt/baseline.prof > "$d/embedded.prof" 2>/dev/null && [ -s "$d/embedded.prof" ]; then
    if cmp -s "$d/embedded.prof" "$d/primary.prof"; then echo "   identical to assets/dexopt/baseline.prof in the APK"
    else echo "   differs from assets/dexopt/baseline.prof ($(stat -c %s "$d/embedded.prof") B)"; fi
  fi
  sha=$(sha256sum "$dm" | cut -d' ' -f1)
  entries+=("{\"format\":\"$fmt\",\"minSdk\":$min,\"maxSdk\":${max:-null},\"file\":\"$(basename "$dm")\",\"sha256\":\"$sha\",\"size\":$(stat -c %s "$dm")}")
done
(IFS=,; echo "[${entries[*]}]") | jq . > "$OUT/dm.json"
cat "$OUT/dm.json"
{
  echo "## Dex metadata (.dm)"
  echo
  echo "- профил: \`${HRF#app/build/intermediates/}\` ($(wc -l < "$HRF") реда)"
  jq -r '.[] | "- \(.file): \(.size) B (API \(.minSdk)–\(.maxSdk // "…"))"' "$OUT/dm.json"
} >> "$SUMMARY"
exit 0
