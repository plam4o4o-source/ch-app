#!/usr/bin/env bash
# Размерът на APK-а от GitHub (prodRelease) и разликата спрямо последното издание — в лога и в
# $GITHUB_STEP_SUMMARY. Размерът на изданието е полето „size“ от update.json на releases/latest
# (малък файл; самият APK не се тегли), а ако го няма — размерът на .apk файла в изданието.
# Само за сведение: никога не проваля build-а; над +LIMIT_KB — предупреждение (::warning::).
#
# Вход: пътят до APK-а; GH_TOKEN и GITHUB_REPOSITORY (за API-то на GitHub).
set -uo pipefail

APK=${1:-}
LIMIT_KB=${LIMIT_KB:-150}
SUMMARY=${GITHUB_STEP_SUMMARY:-/dev/null}

if [ -z "$APK" ] || [ ! -f "$APK" ]; then
  echo "::notice::APK size: no APK ($APK)"
  exit 0
fi
size=$(stat -c %s "$APK")
kb() { awk -v b="$1" 'BEGIN { printf "%.1f", b / 1024 }'; }
mb() { awk -v b="$1" 'BEGIN { printf "%.2f", b / 1048576 }'; }

base="" tag=""
if release=$(gh api "repos/$GITHUB_REPOSITORY/releases/latest" 2>/dev/null); then
  tag=$(jq -r '.tag_name // empty' <<< "$release")
  manifest=$(jq -r '.assets[]? | select(.name == "update.json") | .url' <<< "$release" | head -1)
  if [ -n "$manifest" ]; then
    base=$(gh api -H "Accept: application/octet-stream" "$manifest" 2>/dev/null | jq -r '.size // empty' 2>/dev/null)
  fi
  # Без update.json (или без „size“ в него) — размерът на самия файл от изданието.
  if ! [[ "$base" =~ ^[0-9]+$ ]]; then
    base=$(jq -r '[.assets[]? | select(.name | endswith(".apk")) | .size] | first // empty' <<< "$release")
  fi
fi

line="APK (prodRelease): $size B = $(mb "$size") MB"
if [[ "$base" =~ ^[0-9]+$ ]] && [ "$base" -gt 0 ]; then
  delta=$((size - base))
  sign=$([ "$delta" -ge 0 ] && echo "+" || echo "−")
  abs=${delta#-}
  line="$line; $tag: $base B = $(mb "$base") MB; разлика $sign$(kb "$abs") KB"
  if [ "$delta" -gt $((LIMIT_KB * 1024)) ]; then
    echo "::warning::APK size grew by $(kb "$delta") KB vs $tag (over +$LIMIT_KB KB)"
  fi
else
  line="$line; последното издание: няма данни"
fi
echo "$line"
{ echo "## Размер на APK"; echo; echo "- $line"; } >> "$SUMMARY"
exit 0
