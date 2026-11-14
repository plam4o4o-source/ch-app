#!/usr/bin/env python3
"""Лентата шевица на уиджета: една верига от ромбове на кръстат бод (повтаряща се плочка).

Ромбът е 5×5 бода (контур |dx|+|dy| = 2, в средата — едно зрънце), съседните се допират с
върховете: период 4 бода. Бодът е 1 dp — по 2/3/4 px при xhdpi/xxhdpi/xxxhdpi (по-дребен бод
на истински размер е кафяво петно). Плочката е 4×5 dp; в widget_background.xml лентата е 5 dp.
Пише (светла и тъмна тема):
  app/src/main/res/drawable-{xhdpi,xxhdpi,xxxhdpi}/widget_ribbon.png
  app/src/main/res/drawable-night-{xhdpi,xxhdpi,xxxhdpi}/widget_ribbon.png
Пускане: python3 tools/gen_widget_ribbon.py
"""
import os
import struct
import zlib

PERIOD = 4   # бода по дължина (ромбовете се допират с върховете)
ROWS = 5     # бода на височина
DENSITIES = {"xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}  # px на бод (1 dp)

EMPTY, OUTLINE, CENTER = 0, 1, 2


def kind(cx, cy):
    """Бодът в клетка (cx, cy): cx 0..PERIOD-1, cy 0..ROWS-1; ромбът е с център (2, 2)."""
    dx = abs(cx - 2)
    dx = min(dx, PERIOD - dx)  # върхът на съседния ромб (cx = 0) е общ
    dy = abs(cy - 2)
    if dx + dy == 2:
        return OUTLINE
    if dx == 0 and dy == 0:
        return CENTER
    return EMPTY


def coverage(px, py, s):
    """Колко от пиксела (px, py) в бод от s×s px покрива бодът: квадрат с леко заоблени ъгли.
    Ъглите остават почти плътни — съседните по диагонал бодове се допират и контурът е линия."""
    n = 8
    r = 0.22 * s  # радиус на заоблянето
    hit = 0
    for i in range(n):
        for j in range(n):
            x = px + (i + 0.5) / n
            y = py + (j + 0.5) / n
            # Разстояние до най-близкия ъгъл, ако точката е в ъгловото квадратче.
            qx = min(x, s - x)
            qy = min(y, s - y)
            if qx < r and qy < r:
                if (r - qx) ** 2 + (r - qy) ** 2 > r * r:
                    continue
            hit += 1
    return hit / (n * n)


def render(colors, s):
    w, h = PERIOD * s, ROWS * s
    rows = []
    for y in range(h):
        row = []
        cy, py = divmod(y, s)
        for x in range(w):
            cx, px = divmod(x, s)
            k = kind(cx, cy)
            if k == EMPTY:
                row.append((0, 0, 0, 0))
                continue
            a = coverage(px, py, s)
            # Лек релеф на конеца: горният ляв край — малко по-светъл, долният десен — по-тъмен.
            lit = 1.0 + 0.08 * (((s - 1) / 2 - (px + py) / 2) / max(s - 1, 1))
            r, g, b = (min(255, max(0, round(c * lit))) for c in colors[k])
            row.append((r, g, b, round(a * 255)))
        rows.append(row)
    return rows


def png(path, rows):
    h = len(rows)
    w = len(rows[0])
    raw = b"".join(b"\x00" + b"".join(bytes(px) for px in row) for row in rows)

    def chunk(tag, data):
        c = struct.pack(">I", len(data)) + tag + data
        return c + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    data = b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0))
    data += chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b"")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(data)


def hexrgb(v):
    return ((v >> 16) & 255, (v >> 8) & 255, v & 255)


if __name__ == "__main__":
    root = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "app", "src", "main", "res")
    # {вид: цвят}: светла — бордо с злато (като канта), тъмна — злато с розово бордо.
    light = {OUTLINE: hexrgb(0x6B1F2A), CENTER: hexrgb(0xC9A84C)}
    dark = {OUTLINE: hexrgb(0xC9A84C), CENTER: hexrgb(0xC0566A)}
    for name, s in DENSITIES.items():
        png(os.path.join(root, f"drawable-{name}", "widget_ribbon.png"), render(light, s))
        png(os.path.join(root, f"drawable-night-{name}", "widget_ribbon.png"), render(dark, s))
        print("ribbon", name, PERIOD * s, "x", ROWS * s)
