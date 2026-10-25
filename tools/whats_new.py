# -*- coding: utf-8 -*-
# „Какво ново“ за читателите от store/whats-new.md (виж обяснението в самия файл).
# Изпълнение (от release.yml): python3 tools/whats_new.py ВЕРСИЯ store/whats-new.md ИЗДАДЕНИ ИЗХОДНА_ПАПКА
#   ИЗДАДЕНИ — файл с tag-овете на съществуващите издания (по един на ред, напр. v1.2.25);
#              ако го няма (GitHub API не е отговорил) — разделът се ползва само при точно
#              съвпадение на версията, за да не излезе повторно стар текст.
# Пише ИЗХОДНА_ПАПКА/notes.bg.txt и notes.en.txt („• ред“ на ред). Не пада: при липсващ или
# сгрешен файл — общият текст.
import os
import re
import sys

GENERIC = {"bg": "Подобрения и поправки.", "en": "Improvements and fixes."}
HEADER = re.compile(r"^##\s+v?(\d+)\.(\d+)\.(\d+)\s*$")
LANG = re.compile(r"^###\s+(bg|en)\s*$", re.IGNORECASE)


def parse_version(v):
    m = re.fullmatch(r"v?(\d+)\.(\d+)\.(\d+)", v.strip())
    return tuple(int(x) for x in m.groups()) if m else None


def top_section(text):
    """(версия, {"bg": [...], "en": [...]}) на най-горния раздел или None."""
    text = re.sub(r"<!--.*?-->", "", text, flags=re.S)
    version, lang, notes = None, None, {"bg": [], "en": []}
    for line in text.splitlines():
        h = HEADER.match(line)
        if h:
            if version is not None:
                break
            version = tuple(int(x) for x in h.groups())
            continue
        if version is None:
            continue
        lg = LANG.match(line)
        if lg:
            lang = lg.group(1).lower()
            continue
        item = re.match(r"^\s*[-*•]\s+(.+?)\s*$", line)
        if lang and item:
            notes[lang].append(item.group(1))
    return (version, notes) if version else None


def choose(version, text, released):
    """Бележките за [version]: {"bg": str, "en": str} и откъде са ("file"/"generic")."""
    v = parse_version(version)
    sec = top_section(text) if text else None
    usable = False
    if v and sec:
        head = sec[0]
        if head == v:
            usable = True
        elif released is not None and head < v and head not in released:
            usable = True
    if not usable:
        return dict(GENERIC), "generic"
    out = {}
    for lang in ("bg", "en"):
        lines = sec[1][lang][:6]
        out[lang] = "\n".join("• " + s for s in lines) if lines else GENERIC[lang]
    return out, "file"


def main():
    version, path, released_path, out_dir = sys.argv[1:5]
    try:
        text = open(path, encoding="utf-8").read()
    except OSError:
        text = ""
    released = None
    if os.path.isfile(released_path):
        released = {p for p in (parse_version(t) for t in open(released_path, encoding="utf-8").read().split()) if p}
    notes, source = choose(version, text, released)
    for lang, body in notes.items():
        open(os.path.join(out_dir, "notes.%s.txt" % lang), "w", encoding="utf-8").write(body + "\n")
    print("Какво ново (%s):" % source)
    print(notes["bg"])
    print(notes["en"])


if __name__ == "__main__":
    main()
