#!/usr/bin/env bash
# Dex metadata (.dm) за самообновяването: профилът за бърз старт във вида, който ART чете при
# инсталиране — с него приложението е компилирано (speed-profile) още при обновяването, а не
# едва след първото стартиране (ProfileInstaller) или след часове (фоновата компилация).
#
# За всеки формат на ART — един .dm (zip с primary.prof и, ако profgen го направи, primary.profm):
#   v0_1_0_p — Android 9–11 (API 28–30), v0_1_5_s — Android 12+ (API 31+).
# Профилът е вграденият в APK-а (assets/dexopt/baseline.prof): AGP го е слял (генерираният +
# ръчните правила + тези на библиотеките) и пренаписал за обфускираните от R8 имена; profgen
# extractProfile го превежда за всеки формат (контролните суми на dex файловете са в него).
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
# Човешки четимият профил, слят от AGP и пренаписан от R8 — само за проверката по-долу
# (самите .dm се правят от профила, вграден в APK-а).
HRF=${HRF:-$(find app/build/intermediates -path "*${VARIANT}*" -path '*r8_art_profile*' -path '*minify*' -name 'baseline-prof.txt' 2>/dev/null | head -1)}

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
entries=()
for spec in "v0_1_0_p:28:30" "v0_1_5_s:31:"; do
  IFS=: read -r fmt min max <<< "$spec"
  d="$work/$fmt"; mkdir -p "$d"
  dm="$OUT/$NAME.$fmt.dm"
  rm -f "$dm"
  # profgen extractProfile: профилът от самия APK (assets/dexopt/baseline.prof + .profm, т.е.
  # точно това, което AGP е вградил), преведен във формата на ART и опакован като .dm.
  if ! "$PROFGEN" extractProfile --apk "$APK" --output-dex-metadata "$dm" --profile-format "$fmt" > "$d/log.txt" 2>&1 || [ ! -s "$dm" ]; then
    cat "$d/log.txt"
    warn "profgen extractProfile failed for $fmt"
  fi
  unzip -p "$dm" primary.prof > "$d/primary.prof" 2>/dev/null
  [ -s "$d/primary.prof" ] || warn "no primary.prof in $(basename "$dm")"
  echo "== $(basename "$dm"): $(stat -c %s "$dm") B"
  unzip -lv "$dm" | sed -n '1,8p'
  # Обратна проверка: двоичният профил се чете обратно спрямо APK-а (броят правила)…
  if "$PROFGEN" dumpProfile --profile "$d/primary.prof" --apk "$APK" --output "$d/dump.txt" > "$d/dump.log" 2>&1; then
    echo "   read back: $(wc -l < "$d/dump.txt") rules"
  else
    echo "   read back failed: $(head -c 300 "$d/dump.log")"
  fi
  # …и е същият като профила, направен наново от сливания профил (profgen bin).
  if [ -n "$HRF" ] && [ -s "$HRF" ] && "$PROFGEN" bin "$HRF" --apk "$APK" --output "$d/bin.prof" --profile-format "$fmt" > /dev/null 2>&1; then
    if cmp -s "$d/bin.prof" "$d/primary.prof"; then echo "   identical to profgen bin from ${HRF#app/build/intermediates/} ($(wc -l < "$HRF") lines)"
    else echo "   differs from profgen bin ($(stat -c %s "$d/bin.prof") B vs $(stat -c %s "$d/primary.prof") B)"; fi
  fi
  sha=$(sha256sum "$dm" | cut -d' ' -f1)
  entries+=("{\"format\":\"$fmt\",\"minSdk\":$min,\"maxSdk\":${max:-null},\"file\":\"$(basename "$dm")\",\"sha256\":\"$sha\",\"size\":$(stat -c %s "$dm")}")
done
(IFS=,; echo "[${entries[*]}]") | jq . > "$OUT/dm.json"
cat "$OUT/dm.json"
{
  echo "## Dex metadata (.dm)"
  echo
  jq -r '.[] | "- \(.file): \(.size) B (API \(.minSdk)–\(.maxSdk // "…"))"' "$OUT/dm.json"
} >> "$SUMMARY"
exit 0
