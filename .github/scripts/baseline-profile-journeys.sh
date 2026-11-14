#!/usr/bin/env bash
# Резултатът на всеки път от Journeys.kt („journey <име>: ok / incomplete / failed (… s)“ в
# logcat) — в лога и като таблица в $GITHUB_STEP_SUMMARY: колко пъти е минал, колко пъти е
# стигнал само донякъде (бележка — напр. екранът не се е отворил) и колко пъти е пропаднал.
# Непълен път е предупреждение (::warning::), не грешка: профилът пак съдържа всичко, до което
# е стигнал, а ключовите екрани проверява baseline-profile-check.sh.
set -uo pipefail

SUMMARY=${GITHUB_STEP_SUMMARY:-/dev/null}
# Пътищата съдържат интервали („test(AVD) - 13“) — масив, не низ.
mapfile -t LOGS < <(find baselineprofile/build/outputs -name 'logcat-*.txt' 2>/dev/null | sort)
if [ "${#LOGS[@]}" -eq 0 ]; then
  echo "no logcat files"
  exit 0
fi

for f in "${LOGS[@]}"; do
  echo "::group::$f"
  grep -E ' BaselineProfile *:' "$f" | sed -E 's/^.* BaselineProfile *: //' || true
  echo "::endgroup::"
done

# Всички повторения (и двата опита, ако генерирането е пускано втори път) — заедно.
grep -hE 'BaselineProfile *: journey .*: (ok|incomplete|failed) \(' "${LOGS[@]}" \
  | sed -E 's/^.*BaselineProfile *: journey (.*): (ok|incomplete|failed) \(.*$/\1\t\2/' \
  | awk -F '\t' '
      { n[$1, $2]++; if (!($1 in order)) { order[$1] = ++k; names[k] = $1 } }
      END {
        print "## Пътища (Journeys.kt)"
        print ""
        print "| Път | ok | непълен | грешка |"
        print "|---|---:|---:|---:|"
        for (i = 1; i <= k; i++) {
          j = names[i]
          printf "| %s | %d | %d | %d |\n", j, n[j, "ok"], n[j, "incomplete"], n[j, "failed"]
          bad = n[j, "incomplete"] + n[j, "failed"]
          if (bad > 0) printf "::warning::journey %s: %d of %d incomplete/failed\n", j, bad, bad + n[j, "ok"] > "/dev/stderr"
        }
      }' | tee -a "$SUMMARY"

# Бележките и предупрежденията (защо път е непълен) — уникалните, с броя им.
NOTES=$(grep -hE ' W BaselineProfile *:' "${LOGS[@]}" | sed -E 's/^.* BaselineProfile *: //' | sort | uniq -c | sort -rn | head -n 30)
if [ -n "$NOTES" ]; then
  { echo; echo "Бележки:"; echo '```'; echo "$NOTES"; echo '```'; } | tee -a "$SUMMARY"
fi
exit 0
