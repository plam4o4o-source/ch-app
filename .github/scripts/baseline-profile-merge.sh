#!/usr/bin/env bash
# Сливане на новия профил за бърз старт в main (виж baseline-profile.yml):
# Android CI на commit-а в baseline-profile/update → при зелен „checks“ main се придвижва
# напред до него (fast-forward). Ако main е мръднал — commit-ът се пренася върху новия main
# и проверката се повтаря веднъж. При какъвто и да е неуспех main не се пипа.
# Изисква: GH_TOKEN, GITHUB_REPOSITORY, текущият клон е baseline-profile/update.
# PUSH_TOKEN (тайната PROFILE_PUSH_TOKEN): токен на администратор с право за запис. С
# GITHUB_TOKEN защитата на main не пуска push-а (проверка, пусната с workflow_dispatch, не
# се зачита за задължителната „checks“) — тук проверките ги изисква самият скрипт. Без
# токена профилът остава в baseline-profile/update за ръчно сливане (без грешка).
set -euo pipefail

BRANCH=baseline-profile/update
API="repos/$GITHUB_REPOSITORY/actions"

# Пуска Android CI на текущия HEAD и чака задачата „checks“. 0 — зелена.
run_checks() {
  local sha run_id="" conclusion=""
  sha=$(git rev-parse HEAD)
  echo "Android CI за ${sha::7}…"
  gh api -X POST "$API/workflows/android.yml/dispatches" -f ref="$BRANCH" >/dev/null
  for _ in $(seq 1 30); do
    sleep 10
    run_id=$(gh api "$API/workflows/android.yml/runs?branch=$BRANCH&event=workflow_dispatch&head_sha=$sha&per_page=1" \
      --jq '.workflow_runs[0].id // empty')
    [ -n "$run_id" ] && break
  done
  if [ -z "$run_id" ]; then echo "::error::Пускането на Android CI не се появи."; return 1; fi
  echo "Run: https://github.com/$GITHUB_REPOSITORY/actions/runs/$run_id"
  # До 60 минути (проверките са ~20–25).
  for _ in $(seq 1 120); do
    conclusion=$(gh api "$API/runs/$run_id/jobs" \
      --jq '[.jobs[] | select(.name == "checks") | select(.status == "completed") | .conclusion][0] // empty')
    [ -n "$conclusion" ] && break
    sleep 30
  done
  echo "checks: ${conclusion:-не завърши}"
  [ "$conclusion" = "success" ]
}

for attempt in 1 2; do
  if ! run_checks; then
    echo "::error::Проверките на профила не минаха — main остава с предишния профил."
    exit 1
  fi
  git fetch -q origin main
  if git merge-base --is-ancestor origin/main HEAD; then
    if [ -z "${PUSH_TOKEN:-}" ]; then
      echo "::notice::Профилът е проверен, но няма тайна PROFILE_PUSH_TOKEN — чака в $BRANCH за ръчно сливане (fast-forward на main)."
      echo "- проверен; чака ръчно сливане от \`$BRANCH\` (няма PROFILE_PUSH_TOKEN)" >> "$GITHUB_STEP_SUMMARY"
      exit 0
    fi
    # Празната стойност нулира заглавката с GITHUB_TOKEN от actions/checkout.
    git -c http.https://github.com/.extraheader= \
      push "https://x-access-token:${PUSH_TOKEN}@github.com/${GITHUB_REPOSITORY}.git" "HEAD:main"
    echo "Профилът е в main ($(git rev-parse --short HEAD))."
    echo "- влязъл в \`main\`: $(git rev-parse --short HEAD)" >> "$GITHUB_STEP_SUMMARY"
    exit 0
  fi
  if [ "$attempt" = 2 ]; then break; fi
  echo "main е мръднал — пренасяне на профила върху новия main."
  git rebase -q origin/main
  git push -f origin "HEAD:$BRANCH"
done

echo "::error::main се промени и по време на втората проверка — профилът остава в $BRANCH (следващото пускане ще опита пак)."
exit 1
