# -*- coding: utf-8 -*-
# „Какво ново“ за читателите от store/whats-new.md (виж обяснението в самия файл).
# Изпълнение (от release.yml): python3 tools/whats_new.py ВЕРСИЯ store/whats-new.md ИЗДАДЕНИ ИЗХОДНА_ПАПКА
#   ИЗДАДЕНИ — файл с tag-овете на съществуващите издания (по един на ред, напр. v1.2.25);
#              ако го няма (GitHub API не е отговорил) — разделът се ползва само при точно
#              съвпадение на версията, за да не излезе повторно стар текст.
# Пише ИЗХОДНА_ПАПКА/notes.bg.txt и notes.en.txt („• ред“ на ред). Не пада: при липсващ или
# сгрешен файл — общият текст.
#
# Точно определен раздел (от update-control.yml, действието „notes“ — и за по-стари издания):
#   python3 tools/whats_new.py --section X.Y.Z store/whats-new.md ИЗХОДНА_ПАПКА
# Разделът „## X.Y.Z“ се търси където и да е във файла; ако го няма — грешка (код 2), без файлове.
import os
import re
import sys

GENERIC = {"bg": "Подобрения и поправки.", "en": "Improvements and fixes."}
HEADER = re.compile(r"^##\s+v?(\d+)\.(\d+)\.(\d+)\s*$")
LANG = re.compile(r"^###\s+(bg|en)\s*$", re.IGNORECASE)


def parse_version(v):
    m = re.fullmatch(r"v?(\d+)\.(\d+)\.(\d+)", v.strip())
    return tuple(int(x) for x in m.groups()) if m else None


def sections(text):
    """Разделите отгоре надолу: (версия, {"bg": [...], "en": [...]})."""
    text = re.sub(r"<!--.*?-->", "", text, flags=re.S)
    version, lang, notes = None, None, {"bg": [], "en": []}
    for line in text.splitlines():
        h = HEADER.match(line)
        if h:
            if version is not None:
                yield version, notes
            version, lang, notes = tuple(int(x) for x in h.groups()), None, {"bg": [], "en": []}
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
    if version is not None:
        yield version, notes


def top_section(text):
    """(версия, {"bg": [...], "en": [...]}) на най-горния раздел или None."""
    return next(sections(text), None)


def section(version, text):
    """Раздел „## [version]“ където и да е във файла (първият такъв) или None."""
    v = parse_version(version)
    return next((s for s in sections(text) if v and s[0] == v), None)


def format_notes(notes):
    """{"bg": str, "en": str}: до 6 реда „• …“ на език; празен език — общият текст."""
    out = {}
    for lang in ("bg", "en"):
        lines = notes[lang][:6]
        out[lang] = "\n".join("• " + s for s in lines) if lines else GENERIC[lang]
    return out


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
    return format_notes(sec[1]), "file"


def write_notes(notes, out_dir):
    for lang, body in notes.items():
        open(os.path.join(out_dir, "notes.%s.txt" % lang), "w", encoding="utf-8").write(body + "\n")


def main_section(version, path, out_dir):
    sec = section(version, open(path, encoding="utf-8").read())
    if sec is None:
        print("В %s няма раздел „## %s“." % (path, version), file=sys.stderr)
        sys.exit(2)
    notes = format_notes(sec[1])
    write_notes(notes, out_dir)
    print("Какво ново (раздел %s):" % version)
    print(notes["bg"])
    print(notes["en"])


def main():
    if sys.argv[1:2] == ["--section"]:
        main_section(*sys.argv[2:5])
        return
    version, path, released_path, out_dir = sys.argv[1:5]
    try:
        text = open(path, encoding="utf-8").read()
    except OSError:
        text = ""
    released = None
    if os.path.isfile(released_path):
        released = {p for p in (parse_version(t) for t in open(released_path, encoding="utf-8").read().split()) if p}
    notes, source = choose(version, text, released)
    write_notes(notes, out_dir)
    print("Какво ново (%s):" % source)
    print(notes["bg"])
    print(notes["en"])


if __name__ == "__main__":
    main()
