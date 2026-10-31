# -*- coding: utf-8 -*-
# Подготовка за Update control (виж update-control.yml): новият update.json и — при „notes“ —
# новото описание на изданието. Само пише файловете в работната папка; качва ги workflow-ът,
# и то едва след като и двата са готови (иначе грешка в описанието би оставила update.json
# сменен, а описанието — старо).
#
#   python3 update-control-prepare.py ДЕЙСТВИЕ UPDATE_JSON [BODY NOTES_DIR]
# ДЕЙСТВИЕ: pause | resume | notes. При notes: BODY — описанието на изданието (подменят се
# блоковете „Какво ново:“ и „What's new:“, както ги пише release.yml), NOTES_DIR —
# notes.bg.txt и notes.en.txt от tools/whats_new.py. Причината за pause — от $REASON.
# При грешка — изход с ненулев код, без да е записан нито един от файловете.
import datetime
import json
import os
import re
import sys

NOTES_BLOCKS = re.compile(r"(Какво ново:\n).*?(\n\nWhat's new:\n).*?(\n\n)", re.S)


def edit_manifest(data, action, reason="", notes=None):
    if not isinstance(data, dict) or "versionCode" not in data or "apkUrl" not in data:
        raise SystemExit("update.json не прилича на манифест на обновяване")
    if action == "pause":
        data["paused"] = True
        data["pausedAt"] = datetime.datetime.now(datetime.timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")
        if reason.strip():
            data["pausedReason"] = reason.strip()
        else:
            data.pop("pausedReason", None)
    elif action == "resume":
        for key in ("paused", "pausedAt", "pausedReason"):
            data.pop(key, None)
    elif action == "notes":
        data["notes"], data["notes_en"] = notes["bg"], notes["en"]
    else:
        raise SystemExit(f"Непознато действие: {action}")
    return data


def edit_body(body, notes):
    """Описанието с новите блокове „Какво ново“/„What's new“; без тях — грешка."""
    new, n = NOTES_BLOCKS.subn(lambda m: m.group(1) + notes["bg"] + m.group(2) + notes["en"] + m.group(3), body, count=1)
    if n != 1:
        raise SystemExit("Описанието на изданието няма блоковете „Какво ново:“/„What's new:“ — нищо не е променено.")
    return new


def read(path):
    return open(path, encoding="utf-8").read()


def main():
    action, manifest_path = sys.argv[1:3]
    notes = None
    if action == "notes":
        body_path, notes_dir = sys.argv[3:5]
        notes = {lang: read(os.path.join(notes_dir, f"notes.{lang}.txt")).strip() for lang in ("bg", "en")}
    data = edit_manifest(json.loads(read(manifest_path)), action, os.environ.get("REASON", ""), notes)
    body = edit_body(read(body_path), notes) if notes else None
    # Всичко е проверено — чак сега записът.
    with open(manifest_path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    if body is not None:
        with open(body_path, "w", encoding="utf-8") as f:
            f.write(body)


if __name__ == "__main__":
    main()
