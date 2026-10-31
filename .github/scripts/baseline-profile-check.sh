#!/usr/bin/env bash
# Проверка на новия профил за бърз старт преди commit (виж baseline-profile.yml).
#
# Профилът се приема само ако пътищата в Journeys.kt са стигнали до ключовите екрани:
#   • всеки ключов екран има поне едно правило (0 значи, че пътят не е стигнал дотам —
#     напр. приложението е излязло на заден план и следващите пътища са пропаднали тихо);
#   • общият брой правила не е паднал с повече от MAX_DROP_PERCENT спрямо профила в Git.
# При неуспех стъпката пропада: commit няма, main остава с предишния профил, а изданието
# (auto-release.yml → release-profile.sh) излиза с него и с предупреждение.
#
# Вход: новият профил в app/src/main/generated/baselineProfiles/ (работното копие), старият —
# от HEAD (commit-ът, от който е генериран). Броят правила отива и в $GITHUB_STEP_SUMMARY.
set -euo pipefail

D=app/src/main/generated/baselineProfiles
NEW="$D/baseline-prof.txt"
MAX_DROP_PERCENT=${MAX_DROP_PERCENT:-15}
SUMMARY=${GITHUB_STEP_SUMMARY:-/dev/null}

# Ключовите екрани: име и шаблон (grep -E) за правилата им. Composable функциите са в
# класовете <Файл>Kt, а ламбдите им — <Файл>Kt$<функция>… или <Файл>Kt;-><функция>$lambda….
KEYS=(
  "HomeScreen|HomeScreenKt"
  "CatalogScreens|CatalogScreensKt"
  "MessagesScreen|MessagesScreenKt"
  "NewsListScreen|NewsScreensKt(;->|\\$)NewsListScreen"
  "EventsContent|EventsScreensKt(;->|\\$)EventsContent"
  "WantToReadScreen|WantToReadScreenKt(;->|\\$)WantToReadScreen"
  "MyHubScreen|MyHubScreenKt"
  "MoreScreen|MoreScreenKt"
  "GalleryScreens|GalleryScreensKt"
)
# Само за сведение (не спират профила).
INFO=(
  "ArticleScreen|NewsScreensKt(;->|\\$)ArticleScreen"
  "EventCard|EventsScreensKt(;->|\\$)EventCard"
  "WantToReadItem|WantToReadScreenKt(;->|\\$)WantToReadItem"
  "SearchScreen|SearchScreenKt"
)

if [ ! -s "$NEW" ]; then
  echo "::error::baseline-prof.txt was not generated"
  exit 1
fi
OLD=$(mktemp)
trap 'rm -f "$OLD"' EXIT
git show "HEAD:$NEW" > "$OLD" 2>/dev/null || : > "$OLD"

count() { grep -cE "$1" "$2" || true; }

new_total=$(wc -l < "$NEW")
old_total=$(wc -l < "$OLD")
errors=()
{
  echo "## Baseline profile"
  echo "- baseline-prof.txt: $new_total rules (в Git: $old_total)"
  [ -f "$D/startup-prof.txt" ] && echo "- startup-prof.txt: $(wc -l < "$D/startup-prof.txt") rules"
  echo
  echo "| Екран | Правила | В Git |"
  echo "|---|---:|---:|"
  for entry in "${KEYS[@]}" "${INFO[@]}"; do
    name=${entry%%|*}
    pattern=${entry#*|}
    echo "| \`$name\` | $(count "$pattern" "$NEW") | $(count "$pattern" "$OLD") |"
  done
} | tee -a "$SUMMARY"

for entry in "${KEYS[@]}"; do
  name=${entry%%|*}
  if [ "$(count "${entry#*|}" "$NEW")" -eq 0 ]; then
    errors+=("няма правила за $name (пътят в Journeys.kt не е стигнал дотам)")
  fi
done
if [ "$old_total" -gt 0 ] && [ $((new_total * 100)) -lt $((old_total * (100 - MAX_DROP_PERCENT))) ]; then
  errors+=("правилата са намалели с над $MAX_DROP_PERCENT%: $old_total → $new_total")
fi

if [ "${#errors[@]}" -gt 0 ]; then
  for e in "${errors[@]}"; do
    echo "::error::Профилът е отхвърлен: $e"
    echo "- **отхвърлен:** $e" >> "$SUMMARY"
  done
  exit 1
fi
echo "- проверката мина: всички ключови екрани са в профила" | tee -a "$SUMMARY"
