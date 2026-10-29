#!/usr/bin/env bash
# Свеж профил за бърз старт преди издание (виж auto-release.yml, задачата „profile“).
#
# Сегашният профил е генериран от commit-а в съобщението „Профил за бърз старт: генериран на
# емулатор от <sha>“ (виж baseline-profile.yml). Ако кодът на приложението (app/src/main,
# shared/src/main — без самия профил) не е променян след него, изданието е с този профил.
# Иначе Baseline profile се пуска от main и изданието чака новия профил да влезе в main (до
# WAIT_MINUTES), после се издава новият връх на main (commit-ът с профила е проверен от
# baseline-profile-merge.sh). Неуспех, „профилът не се е променил“ или изтекло време —
# издание с предишния профил (предупреждение): емулаторът никога не спира издание.
# Ако междувременно в main влезе друг commit — без издание тук: Android CI на новия commit
# пуска Auto release отново и то издава най-новото (и проверява профила наново).
#
# Вход: REF (commit-ът от плана, == origin/main), GH_TOKEN, GITHUB_REPOSITORY.
# Изход ($GITHUB_OUTPUT): release=true|false, ref=<commit за издаване>.
set -euo pipefail

WAIT_MINUTES=${WAIT_MINUTES:-120}
PROFILE_DIR=app/src/main/generated/baselineProfiles
NOT_PROFILE=":(exclude)$PROFILE_DIR"
CODE_PATHS="app/src/main shared/src/main"
WORKFLOW="repos/$GITHUB_REPOSITORY/actions/workflows/baseline-profile.yml"

done_with() {
  echo "release=$1" >> "$GITHUB_OUTPUT"
  [ -n "${2:-}" ] && echo "ref=$2" >> "$GITHUB_OUTPUT"
  exit 0
}

# Commit-ът, от който е генериран профилът в [rev] (празно — няма профил).
profile_source() {
  local subject sha
  subject=$(git log -1 --format=%s "$1" -- "$PROFILE_DIR")
  sha=$(sed -n 's/.*генериран на емулатор от \([0-9a-f]\{7,40\}\).*/\1/p' <<< "$subject")
  if [ -n "$sha" ] && git rev-parse -q --verify "$sha^{commit}" >/dev/null; then
    git rev-parse "$sha^{commit}"
  else
    # Без такова съобщение (напр. ръчен профил) — самият commit с профила.
    git log -1 --format=%H "$1" -- "$PROFILE_DIR"
  fi
}

# 0 — профилът в [rev] е от код, който до [rev] не е променян.
fresh() {
  local src
  src=$(profile_source "$1")
  [ -n "$src" ] && git diff --quiet "$src" "$1" -- $CODE_PATHS "$NOT_PROFILE"
}

# 0 — [rev] е [REF] или след него има само профил.
only_profile_after_ref() {
  git merge-base --is-ancestor "$REF" "$1" && git diff --quiet "$REF" "$1" -- . "$NOT_PROFILE"
}

if fresh "$REF"; then
  echo "Профилът е от $(git rev-parse --short "$(profile_source "$REF")"); кодът след това не е променян — издание с него."
  done_with true "$REF"
fi
echo "Кодът е променен след commit-а на профила — нов профил преди изданието."

START=$(date -u +%Y-%m-%dT%H:%M:%SZ)
if ! gh api -X POST "$WORKFLOW/dispatches" -f ref=main >/dev/null; then
  echo "::warning::Baseline profile не можа да се пусне — издание с предишния профил."
  done_with true "$REF"
fi

SEEN=false
for minute in $(seq 1 "$WAIT_MINUTES"); do
  sleep 60
  git fetch -q origin main
  HEAD=$(git rev-parse origin/main)
  if ! only_profile_after_ref "$HEAD"; then
    echo "В main влезе нов commit ($(git rev-parse --short "$HEAD")) — изданието ще е от следващото пускане на Auto release."
    done_with false
  fi
  if [ "$HEAD" != "$REF" ] && fresh "$HEAD"; then
    echo "Новият профил е в main ($(git rev-parse --short "$HEAD")) — издание от него."
    echo "- профил за бърз старт: свеж, издание от \`$(git rev-parse --short "$HEAD")\`" >> "$GITHUB_STEP_SUMMARY"
    done_with true "$HEAD"
  fi
  # Пусканията на Baseline profile от main след START (и нашето): още вървят ли?
  RUNS=$(gh api "$WORKFLOW/runs?branch=main&created=%3E%3D$START&per_page=20" \
    --jq '[.workflow_runs[] | .status] | "\(length) \(map(select(. != "completed")) | length)"' || echo "0 0")
  read -r TOTAL ACTIVE <<< "$RUNS"
  [ "$TOTAL" -gt 0 ] && SEEN=true
  if [ "$SEEN" = true ] && [ "$ACTIVE" -eq 0 ]; then
    echo "Baseline profile завърши, без нов профил в main (непроменен, неуспешен или чака ръчно сливане)."
    break
  fi
  if [ "$SEEN" = false ] && [ "$minute" -ge 5 ]; then
    echo "Пускането на Baseline profile не се появи."
    break
  fi
done

# Без свеж профил: върхът на main (REF или REF + по-стар профил, вече проверен).
git fetch -q origin main
HEAD=$(git rev-parse origin/main)
if ! only_profile_after_ref "$HEAD"; then
  echo "В main влезе нов commit — изданието ще е от следващото пускане на Auto release."
  done_with false
fi
echo "::warning::Изданието е с предишния профил за бърз старт (новият не влезе в main навреме — виж Actions → Baseline profile)."
echo "- профил за бърз старт: предишният (новият не влезе в main навреме)" >> "$GITHUB_STEP_SUMMARY"
done_with true "$HEAD"
