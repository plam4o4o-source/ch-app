#!/usr/bin/env python3
"""Лентата шевица на уиджета (6 dp, повтаряща се плочка) — същата шарка и бод като
ShevitsaStill.render (LivingShevitsa.kt), нарисувана като плочка на лента: период 4 бода,
9 реда по 2 px при xxhdpi (18 px = 6 dp). Пише:
  app/src/main/res/drawable-xxhdpi/widget_ribbon.png        (светла тема)
  app/src/main/res/drawable-night-xxhdpi/widget_ribbon.png  (тъмна тема)
Пускане: python3 tools/gen_widget_ribbon.py
"""
import math
import os
import struct
import zlib

CELL = 2.0
PERIOD = 4
ROWS = PERIOD + 5          # ShevitsaPattern.ribbonRows
COLS = 2 * PERIOD          # ShevitsaStill.tileCells(ribbon)
W = int(round(COLS * CELL))
H = int(round(ROWS * CELL))
ORIGIN_Y = int(math.floor(ROWS * 0.5))

EMPTY, RED, INK, GOLD = 0, 1, 2, 3


def kind(cx, cy, p):
    r = p * 0.5
    u, v = cx + cy, cx - cy
    mu, mv = u % p, v % p
    du, dv = min(mu, p - mu), min(mv, p - mv)
    node = max(du, dv) < 1.5
    ay = abs(cy)
    if ay > r + 0.5:
        if ay < r + 1.5:
            return GOLD if node else EMPTY
        if ay < r + 2.5:
            return RED
        return EMPTY
    if node:
        return INK if du + dv < 0.5 else GOLD
    if du < 0.5 or dv < 0.5:
        return RED
    i, j = math.floor(u / p), math.floor(v / p)
    if abs(i - j) > 0.5:
        return EMPTY
    lu, lv = mu - r, mv - r
    manh = max(abs(lu), abs(lv))
    sel = i % 2
    s = r
    if sel < 0.5:
        if manh < 0.5:
            return GOLD
        if manh < 1.5:
            return INK
        ax, by = abs(lu + lv) * 0.5, abs(lu - lv) * 0.5
        along = (ax + by) * 0.5 - s * 0.375
        across = abs(ax - by) * 0.5
        if abs(along) / (s * 0.275) + across / (s * 0.1) <= 1:
            return GOLD
        ma, mi = max(ax, by), min(ax, by)
        if abs(ma - s * 0.5) / (s * 0.4) + mi / (s * 0.2) <= 1:
            return RED
        return EMPTY
    b = math.floor(s * 0.75 + 0.01)
    if abs(manh - b) < 0.1:
        return RED
    if abs(manh - math.floor(b * 0.5)) < 0.1:
        return GOLD
    if manh < 0.1:
        return INK
    return EMPTY


def smooth(e0, e1, x):
    t = min(max((x - e0) / (e1 - e0), 0.0), 1.0)
    return t * t * (3 - 2 * t)


def render(threads):
    aa = 1.0 / CELL
    tw2 = 0.21
    sx, sy = COLS * CELL / W, ROWS * CELL / H
    rows = []
    for y in range(H):
        gy = (y + 0.5) * sy / CELL
        cy_raw = math.floor(gy)
        qy = gy - cy_raw
        cy = cy_raw - ORIGIN_Y
        row = []
        for x in range(W):
            gx = (x + 0.5) * sx / CELL
            cxi = min(max(int(math.floor(gx)), 0), COLS - 1)
            qx = gx - math.floor(gx)
            k = kind(cxi, cy, PERIOD)
            cov, lit = 0.0, 1.0
            if k != EMPTY:
                dA = abs(qx - qy) * 0.7071
                tA = (qx + qy) * 0.5
                dB = abs(qx + qy - 1) * 0.7071
                tB = (qx - qy) * 0.5 + 0.5
                endA = smooth(0.03, 0.1, tA) * (1 - smooth(0.9, 0.97, tA))
                endB = smooth(0.03, 0.1, tB) * (1 - smooth(0.9, 0.97, tB))
                covA = (1 - smooth(tw2 - aa, tw2 + aa, dA)) * endA
                covB = (1 - smooth(tw2 - aa, tw2 + aa, dB)) * endB
                nA, nB = dA / tw2, dB / tw2
                shA = math.sqrt(min(max(1 - nA * nA, 0), 1))
                shB = math.sqrt(min(max(1 - nB * nB, 0), 1))
                twA = 0.9 + 0.1 * math.sin((tA * 7 + dA * 9) * 6.2832)
                twB = 0.9 + 0.1 * math.sin((tB * 7 + dB * 9) * 6.2832)
                shd = 1 - 0.35 * (1 - smooth(tw2, tw2 + 0.12, dB)) * endB
                litA = (0.5 + 0.62 * shA) * twA * shd
                litB = (0.5 + 0.62 * shB) * twB
                lit = litB * covB + litA * (1 - covB)
                cov = min(max(covA + covB - covA * covB, 0), 1)
            if cov <= 0.001:
                row.append((0, 0, 0, 0))
                continue
            t = threads[k - 1]
            row.append(tuple(min(max(int(round(c * lit)), 0), 255) for c in t) + (int(round(cov * 255)),))
        rows.append(row)
    return rows


def png(path, rows):
    raw = b"".join(b"\x00" + b"".join(bytes(px) for px in row) for row in rows)

    def chunk(tag, data):
        c = struct.pack(">I", len(data)) + tag + data
        return c + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    data = b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", W, H, 8, 6, 0, 0, 0))
    data += chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b"")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(data)


def hexrgb(h):
    return ((h >> 16) & 255, (h >> 8) & 255, h & 255)


if __name__ == "__main__":
    root = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "app", "src", "main", "res")
    # (червено, мастило, злато) — ShevitsaPalettes.ribbon(светла/тъмна) без канавата
    light = [hexrgb(0x6B1F2A), hexrgb(0x1A1208), hexrgb(0xC9A84C)]
    dark = [hexrgb(0xC0566A), hexrgb(0xE8D5A3), hexrgb(0xC9A84C)]
    png(os.path.join(root, "drawable-xxhdpi", "widget_ribbon.png"), render(light))
    png(os.path.join(root, "drawable-night-xxhdpi", "widget_ribbon.png"), render(dark))
    print("ribbon", W, "x", H)
