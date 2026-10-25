#!/usr/bin/env python3
"""Снимките за Google Play: рамкирани екрани с надпис (store/screenshots/*.png).

Взима суровите снимки от CI (PlayScreenshotTest → play-*.png в клона „screenshots“)
и ги подрежда в 1080×1920 (9:16, телефон): горе — лента с кратък надпис на
български (Cormorant Garamond от app/src/main/res/font, в цвета на пергамента върху
бордо, със златна чертичка), под нея — екранът със заоблени ъгли, тънка златна
рамка и мека сянка; долният му край „излиза“ от кадъра. PNG без алфа канал (Play
отхвърля прозрачност).

Изпълнение от корена (нужен е Pillow: pip install pillow):
    git fetch origin screenshots
    python3 tools/play_screenshots.py                  # от origin/screenshots
    python3 tools/play_screenshots.py --ref <commit>   # от друг commit на клона
    python3 tools/play_screenshots.py --src DIR        # от папка с play-*.png
Снимките се правят наново след всяка промяна по външния вид (CI → клонът „screenshots“
→ този скрипт); проверете последния commit на клона да е с вашия sha.
"""
import argparse
import io
import os
import subprocess
import sys

from PIL import Image, ImageDraw, ImageFilter, ImageFont

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
FONT_DIR = os.path.join(ROOT, "app", "src", "main", "res", "font")

W, H = 1080, 1920
BURGUNDY = (0x6B, 0x1F, 0x2A)
BURGUNDY_DEEP = (0x45, 0x11, 0x19)
GOLD = (0xC9, 0xA8, 0x4C)
PARCHMENT = (0xF5, 0xF0, 0xE8)

# (сурова снимка от CI, файл в store/screenshots, надпис — 4–6 думи)
SHOTS = [
    ("play-01-home-light.png", "01-home-light.png", "Читалището винаги в джоба ти"),
    ("play-02-home-dark.png", "02-home-dark.png", "Удобно и в тъмна тема"),
    ("play-03-catalog.png", "03-catalog.png", "Целият каталог на библиотеката тук"),
    ("play-80-design-book.png", "04-book.png", "Виж дали книгата е на рафта"),
    ("play-81-design-events.png", "05-events.png", "Не пропускай нито едно събитие"),
    ("play-04-my.png", "06-my.png", "Твоите книги и срокове за връщане"),
    ("play-82-design-card.png", "07-card.png", "Читателската карта винаги с теб"),
    ("play-05-more.png", "08-more.png", "Новини, клубове, история и още"),
]

BAND = 400          # височина на лентата с надписа
SHOT_W = 880        # ширина на екрана в кадъра
SHOT_TOP = 430      # горен ръб на екрана
RADIUS = 46
BORDER = 3


def font(name, size):
    return ImageFont.truetype(os.path.join(FONT_DIR, name), size)


def load(name, ref, src):
    """Суровата снимка — от папка или от commit на клона „screenshots“."""
    if src:
        return Image.open(os.path.join(src, name)).convert("RGB")
    data = subprocess.run(["git", "-C", ROOT, "show", f"{ref}:{name}"], check=True, capture_output=True).stdout
    return Image.open(io.BytesIO(data)).convert("RGB")


def wrap(draw, text, fnt, max_w):
    """Пренася надписа на редове, по-тесни от max_w (без пренасяне на думи); два реда — балансирани."""
    words = text.split()
    if draw.textlength(text, font=fnt) <= max_w:
        return [text]
    # Два реда с възможно най-близка дължина (не „дълъг ред + една дума“).
    splits = [(" ".join(words[:i]), " ".join(words[i:])) for i in range(1, len(words))]
    fitting = [s for s in splits if max(draw.textlength(p, font=fnt) for p in s) <= max_w]
    if fitting:
        return list(min(fitting, key=lambda s: max(draw.textlength(p, font=fnt) for p in s)))
    lines, line = [], ""
    for word in words:
        trial = f"{line} {word}".strip()
        if draw.textlength(trial, font=fnt) <= max_w or not line:
            line = trial
        else:
            lines.append(line)
            line = word
    if line:
        lines.append(line)
    return lines


def background():
    """Бордо с лек преход към по-тъмно надолу."""
    img = Image.new("RGB", (W, H), BURGUNDY)
    px = ImageDraw.Draw(img)
    for y in range(H):
        t = y / (H - 1)
        c = tuple(round(a + (b - a) * t * 0.9) for a, b in zip(BURGUNDY, BURGUNDY_DEEP))
        px.line([(0, y), (W, y)], fill=c)
    return img


def caption(img, text):
    draw = ImageDraw.Draw(img)
    size = 84
    while True:
        fnt = font("cormorant_garamond_semibold.ttf", size)
        lines = wrap(draw, text, fnt, W - 160)
        if len(lines) <= 2 or size <= 56:
            break
        size -= 4
    line_h = round(size * 1.12)
    block = line_h * len(lines)
    # Златната чертичка над надписа (както етикетите на сайта и в приложението).
    top = (BAND - block) // 2 + 24
    draw.rectangle([W // 2 - 48, top - 46, W // 2 + 48, top - 43], fill=GOLD)
    for i, line in enumerate(lines):
        w = draw.textlength(line, font=fnt)
        draw.text(((W - w) / 2, top + i * line_h), line, font=fnt, fill=PARCHMENT)


def frame(img, shot):
    # Суровите снимки на CI са 9:16; по-високите се режат отгоре надолу до 9:16.
    sw, sh = shot.size
    if sh / sw > 16 / 9 + 0.01:
        shot = shot.crop((0, 0, sw, round(sw * 16 / 9)))
    scale = SHOT_W / shot.width
    shot = shot.resize((SHOT_W, round(shot.height * scale)), Image.LANCZOS)
    x = (W - SHOT_W) // 2
    y = SHOT_TOP
    box = (x - BORDER, y - BORDER, x + SHOT_W + BORDER, y + shot.height + BORDER)
    # Мека сянка под екрана.
    shadow = Image.new("L", (W, H), 0)
    ImageDraw.Draw(shadow).rounded_rectangle((box[0], box[1] + 18, box[2], box[3] + 18), RADIUS + BORDER, fill=150)
    shadow = shadow.filter(ImageFilter.GaussianBlur(28))
    img.paste((20, 6, 9), (0, 0), shadow)
    # Тънка златна рамка и екранът със заоблени ъгли.
    ImageDraw.Draw(img).rounded_rectangle(box, RADIUS + BORDER, fill=GOLD)
    mask = Image.new("L", shot.size, 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, shot.width - 1, shot.height - 1), RADIUS, fill=255)
    img.paste(shot, (x, y), mask)


def compose(shot, text):
    img = background()
    caption(img, text)
    frame(img, shot)
    return img


def main():
    ap = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    ap.add_argument("--ref", default="origin/screenshots", help="commit/клон със суровите снимки")
    ap.add_argument("--src", help="папка със суровите снимки (вместо --ref)")
    ap.add_argument("--out", default=os.path.join(ROOT, "store", "screenshots"))
    args = ap.parse_args()
    os.makedirs(args.out, exist_ok=True)
    missing = []
    for src_name, out_name, text in SHOTS:
        try:
            shot = load(src_name, args.ref, args.src)
        except (subprocess.CalledProcessError, FileNotFoundError):
            missing.append(src_name)
            continue
        out = os.path.join(args.out, out_name)
        compose(shot, text).save(out, optimize=True)
        print(f"{out_name}  ←  {src_name}  „{text}“")
    if missing:
        print("липсват:", ", ".join(missing), file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
