#!/usr/bin/env python3
"""Подмножества на шрифтовете в app/src/main/res/font (по-малък APK) + проверка на покритието.

Оставя: латиница (ASCII, Latin-1 и Latin Extended-A — имена на чужди автори в
каталога), основната кирилица U+0400–045F (българските, руските, украинските,
сръбските и македонските букви, вкл. Ѝ ѝ) и Ґ ґ, общата пунктуация
(„“ — – … ‘’ • ‰ › …), знаците за валута (€ …), № ™ и стрелките ← →. Махат се
историческата и разширената кирилица (U+0460–04FF без Ґ ґ — сайтът и каталогът не
ги ползват; ~55 KB по-малък APK и ~135 KB по-малко памет за шрифтовете),
стилистичните алтернативи (оставят се стандартните OpenType функции — kern, liga,
locl за българските форми и т.н.) и TrueType hinting-ът (Android не го ползва при
съвременните плътности на екрана).

Цифри: и двата шрифта са със старинни (old-style) цифри по подразбиране — „1“
изглежда като „I“ („1 ДЕК“ → „I ДЕК“, „104“ → „IO4“). Затова `lnum` е „замразен“
(както pyftfeatfreeze): cmap сочи право към изравнените (lining) цифри и знаци
(№ $ € £ ¥ в Cormorant) — цифрите са изравнени навсякъде, без
fontFeatureSettings. Функциите lnum/onum/pnum/tnum остават (Cormorant има
tnum — таблични изравнени цифри; Raleway няма tnum/onum).

Raleway е с „Reserved Font Name“ (OFL, т. 3): променена версия (подмножество +
замразени цифри) не може да носи името „Raleway“, затова вътрешното име на
семейството е „Yavorec Sans“ (авторските права и лицензът остават в name
таблицата; текстът на лиценза — docs/fonts/). Cormorant Garamond няма RFN.

Проверка: всички знаци от strings.xml (двата езика), от низовете в Kotlin кода
и типичните знаци от сайта/каталога, които оригиналният шрифт е покривал,
трябва да са в подмножеството, всяка буква от тях — в шрифта, а цифрите —
изравнени. Иначе — грешка (код 1). В CI (android.yml, „checks“) върви --check.

Изпълнение от корена (нужен е fontTools: pip install fonttools):
    python3 tools/subset_fonts.py          # подмножества + проверка
    python3 tools/subset_fonts.py --check  # само проверка
    python3 tools/subset_fonts.py --source DIR  # наново от пълните шрифтове в DIR
Повторното изпълнение е безопасно (подмножество на подмножество е същото).

Изходни шрифтове (за --source, със същите имена на файловете): пълните статични
инстанции на Cormorant Garamond 4.001 и Raleway 4.026 (google/fonts, OFL) от
първото им добавяне в хранилището, commit 7c86440 (`show 7c86440:<път>`).
"""
import glob
import re
import shutil
import sys
import xml.etree.ElementTree as ET

from fontTools import subset
from fontTools.pens.boundsPen import BoundsPen
from fontTools.ttLib import TTFont

FONT_DIR = "app/src/main/res/font"
STRINGS = ["app/src/main/res/values/strings.xml", "app/src/main/res/values-en/strings.xml"]
KOTLIN = ["app/src/main/kotlin", "app/src/prod/kotlin", "app/src/dev/kotlin", "shared/src/main/kotlin"]

UNICODES = (
    list(range(0x0020, 0x007F))      # Basic Latin
    + list(range(0x00A0, 0x0100))    # Latin-1 Supplement (« » € няма, но © ° · × и т.н.)
    + list(range(0x0100, 0x0180))    # Latin Extended-A (Č, Ł, Ő … — имена на чужди автори в каталога)
    + list(range(0x0400, 0x0460))    # Cyrillic (основната; без историческата и разширената)
    + [0x0490, 0x0491]               # Ґ ґ
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


# Цифрови функции, които подмножеството запазва (освен стандартните на fontTools).
NUMERAL_FEATURES = ["lnum", "onum", "pnum", "tnum"]
# Шрифтове с Reserved Font Name → вътрешно име на променената версия.
RFN_RENAME = {"Raleway": "Yavorec Sans"}


def lining_map(font):
    """Замените на `lnum` (старинна → изравнена цифра) от GSUB."""
    mapping = {}
    if "GSUB" not in font:
        return mapping
    gsub = font["GSUB"].table
    for fr in gsub.FeatureList.FeatureRecord:
        if fr.FeatureTag != "lnum":
            continue
        for li in fr.Feature.LookupListIndex:
            for st in gsub.LookupList.Lookup[li].SubTable:
                st = getattr(st, "ExtSubTable", st)
                mapping.update(getattr(st, "mapping", None) or {})
    return mapping


def freeze_lining_figures(font):
    """cmap → изравнени цифри (замразен lnum). Повторно — без промяна."""
    mapping = lining_map(font)
    for table in font["cmap"].tables:
        if table.isUnicode():
            for u, g in list(table.cmap.items()):
                if g in mapping:
                    table.cmap[u] = mapping[g]


def rename_rfn(font):
    """Маха Reserved Font Name от имената на променената версия."""
    for rec in font["name"].names:
        if rec.nameID not in (1, 3, 4, 6, 16, 21, 25):
            continue
        text = rec.toUnicode()
        for old, new in RFN_RENAME.items():
            text = text.replace(old, new.replace(" ", "") if rec.nameID in (3, 6, 25) else new)
        rec.string = text


def has_lining_figures(font):
    """Изравнени цифри: „1“ е висока колкото главна буква (старинната е до x-височината)."""
    gs = font.getGlyphSet()
    pen = BoundsPen(gs)
    gs[font.getBestCmap()[ord("1")]].draw(pen)
    return pen.bounds[3] >= 0.9 * font["OS/2"].sCapHeight


def subset_font(path):
    opts = subset.Options()
    opts.hinting = False
    opts.name_IDs = ["*"]
    opts.name_languages = ["*"]
    opts.notdef_outline = True
    opts.layout_features = opts.layout_features + NUMERAL_FEATURES
    font = TTFont(path)
    freeze_lining_figures(font)
    rename_rfn(font)
    s = subset.Subsetter(opts)
    s.populate(unicodes=UNICODES)
    s.subset(font)
    font.save(path)


def main():
    check_only = "--check" in sys.argv
    fonts = sorted(glob.glob(f"{FONT_DIR}/*.ttf"))
    if "--source" in sys.argv and not check_only:
        src = sys.argv[sys.argv.index("--source") + 1]
        for path in fonts:
            shutil.copyfile(f"{src}/{path.rsplit('/', 1)[-1]}", path)
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
        # Буква без глиф (и при --check): системният шрифт по средата на дума — грешка.
        # Символите (✓ − ≈ …) могат да са от системния шрифт.
        letters = sorted(c for c in chars if c.isalpha() and ord(c) not in after)
        if letters:
            print(f"  БУКВИ БЕЗ ГЛИФ: {' '.join(f'{c} U+{ord(c):04X}' for c in letters)}")
        if lost or letters:
            ok = False
        if not has_lining_figures(TTFont(path)):
            print("  старинни цифри по подразбиране („1“ като „I“) — lnum не е замразен")
            ok = False
    missing_basic = [c for c in "„“—–…€№«»’" if ord(c) not in allowed]
    if missing_basic:
        print("Липсват основни знаци в UNICODES:", missing_basic)
        ok = False
    print("OK" if ok else "ГРЕШКА: липсващи знаци или старинни цифри (виж по-горе)")
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
