#!/usr/bin/env python3
"""Подмножества на шрифтовете в app/src/main/res/font (по-малък APK) + проверка на покритието.

Оставя: латиница (ASCII + Latin-1), цялата кирилица, общата пунктуация
(„“ — – … ‘’ • ‰ …), знаците за валута (€ …), № ™ и стрелките ← →. Махат се
Latin Extended-A, стилистичните алтернативи (оставят се стандартните OpenType
функции — kern, liga, locl за българските форми и т.н.) и TrueType hinting-ът
(Android не го ползва при съвременните плътности на екрана).

Проверка: всички знаци от strings.xml (двата езика), от низовете в Kotlin кода
и типичните знаци от сайта/каталога, които оригиналният шрифт е покривал,
трябва да са в подмножеството. Иначе — грешка (код 1).

Изпълнение от корена (нужен е fontTools: pip install fonttools):
    python3 tools/subset_fonts.py          # подмножества + проверка
    python3 tools/subset_fonts.py --check  # само проверка
Повторното изпълнение е безопасно (подмножество на подмножество е същото).
"""
import glob
import re
import sys
import xml.etree.ElementTree as ET

from fontTools import subset
from fontTools.ttLib import TTFont

FONT_DIR = "app/src/main/res/font"
STRINGS = ["app/src/main/res/values/strings.xml", "app/src/main/res/values-en/strings.xml"]
KOTLIN = ["app/src/main/kotlin", "app/src/prod/kotlin", "app/src/dev/kotlin", "shared/src/main/kotlin"]

UNICODES = (
    list(range(0x0020, 0x007F))      # Basic Latin
    + list(range(0x00A0, 0x0100))    # Latin-1 Supplement (« » € няма, но © ° · × и т.н.)
    + list(range(0x0100, 0x0180))    # Latin Extended-A (Č, Ł, Ő … — имена на чужди автори в каталога)
    + list(range(0x0400, 0x0500))    # Cyrillic
    + list(range(0x2000, 0x2070))    # General Punctuation („ “ — – … ’ • ‰ ′ ″)
    + list(range(0x20A0, 0x20D0))    # Currency Symbols (€)
    + [0x2116, 0x2122, 0x2190, 0x2192]  # № ™ ← →
)

# Знаци, типични за съдържанието на сайта и каталога (освен тези в strings.xml).
TYPICAL = "„“”‘’‚«»—–‑…•·€№%‰°×©®™←→" + "абвгдежзийклмнопрстуфхцчшщъьюяѝАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЬЮЯЍ" \
    + "ёЁіІїЇєЄґҐўЎјЈљЉњЊћЋџЏђЂѓЃќЌѕЅ" + "äöüßÄÖÜéèêëàâçîïôûùÿñáíóúÁÉÍÓÚ"


def needed_chars():
    chars = set(TYPICAL)
    for path in STRINGS:
        for el in ET.parse(path).getroot().iter():
            for t in (el.text, el.tail):
                if t:
                    chars.update(t)
    # Литерали в Kotlin кода (видим текст като „…“ или „ · “ се сглобява и там).
    lit = re.compile(r'"((?:[^"\\\n]|\\.)*)"')
    for root in KOTLIN:
        for path in glob.glob(f"{root}/**/*.kt", recursive=True):
            with open(path, encoding="utf-8") as f:
                for m in lit.finditer(f.read()):
                    chars.update(m.group(1))
    return {c for c in chars if ord(c) >= 0x20 and not c.isspace() or c == " "}


def subset_font(path):
    opts = subset.Options()
    opts.hinting = False
    opts.name_IDs = ["*"]
    opts.name_languages = ["*"]
    opts.notdef_outline = True
    font = TTFont(path)
    s = subset.Subsetter(opts)
    s.populate(unicodes=UNICODES)
    s.subset(font)
    font.save(path)


def main():
    check_only = "--check" in sys.argv
    fonts = sorted(glob.glob(f"{FONT_DIR}/*.ttf"))
    chars = needed_chars()
    allowed = set(UNICODES)
    ok = True
    for path in fonts:
        before = set(TTFont(path).getBestCmap())
        if not check_only:
            subset_font(path)
        after = set(TTFont(path).getBestCmap())
        # Знак, който шрифтът е имал и е нужен, но вече липсва → грешка.
        lost = sorted(c for c in chars if ord(c) in before and ord(c) not in after)
        # Нужен знак извън обхвата на подмножеството (ще се ползва системният шрифт).
        outside = sorted(c for c in chars if ord(c) not in allowed and ord(c) not in after)
        name = path.rsplit("/", 1)[-1]
        print(f"{name}: {len(after)} знака" + (f"; ИЗГУБЕНИ: {''.join(lost)}" if lost else ""))
        if outside and path == fonts[0]:
            print("  извън подмножеството (системен шрифт): " + " ".join(f"{c} U+{ord(c):04X}" for c in outside))
        if lost:
            ok = False
    missing_basic = [c for c in "„“—–…€№«»’" if ord(c) not in allowed]
    if missing_basic:
        print("Липсват основни знаци в UNICODES:", missing_basic)
        ok = False
    print("OK" if ok else "ГРЕШКА: подмножеството не покрива нужните знаци")
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
