#!/usr/bin/env python3
"""Помощни изчисления за w26.sh (ВРЕМЕННО, debug/w26-check; резултати в emulator-results/v139).

  w26.py scrollable ui.xml          — L T R B на най-големия превъртаем възел
  w26.py years ui.xml               — first=… min=… max=…: годините (1800–2030) във видимите надписи
  w26.py cftime DIR FPS LT LB NY H  — конфети при 5 от 5: кадърът на отговора (първа промяна), първият кадър
                                      с преходни наситени точки извън лентата и колко ms са между тях;
                                      LT/LB — горе/долу на надписа на лентата (px), NY — y на „Напред“, H — височина
  w26.py jpeg OUT                   — тестова „стара снимка“ 1600×1200 (JPEG)
"""
import os
import re
import sys


def read(p):
    with open(p, encoding='utf-8', errors='replace') as f:
        return f.read()


def nodes(path):
    s = read(path)
    for m in re.finditer(r'<node [^>]*>', s):
        yield m.group(0)


def attr(n, k):
    m = re.search(r'\b' + k + r'="([^"]*)"', n)
    return m.group(1) if m else ''


def bounds(n):
    m = re.search(r'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"', n)
    return tuple(int(x) for x in m.groups()) if m else None


def cmd_scrollable(path):
    best = None
    for n in nodes(path):
        if attr(n, 'scrollable') != 'true':
            continue
        b = bounds(n)
        if not b:
            continue
        a = (b[2] - b[0]) * (b[3] - b[1])
        if best is None or a > best[0]:
            best = (a, b)
    if best:
        print(*best[1])


def cmd_years(path):
    ys = []
    for n in nodes(path):
        for k in ('text', 'content-desc'):
            for m in re.finditer(r'(?<!\d)(1[89]\d\d|20[0-3]\d)(?!\d)', attr(n, k)):
                ys.append(int(m.group(1)))
    if ys:
        print(f"first={ys[0]} min={min(ys)} max={max(ys)} n={len(ys)}")
    else:
        print("first=0 min=0 max=0 n=0")


def cmd_cftime(d, fps, lt, lb, ny, h):
    import colorsys
    from PIL import Image
    fps = float(fps)
    fs = sorted(f for f in os.listdir(d) if f.lower().endswith(('.jpg', '.png')) and f.startswith('f'))
    if len(fs) < 4:
        print('cftime: too few frames')
        return
    W, H = 216, 480
    ims = [Image.open(os.path.join(d, f)).convert('RGB').resize((W, H)) for f in fs]
    px = [im.load() for im in ims]
    first, last = px[0], px[-1]
    hh = float(h)
    # Лентата (надписът и платното до бутона) — там са бодът, затварянето и отблясъкът
    band_top = (float(lt) / hh - 0.01) if lt else 0.75
    band_bot = (float(ny) / hh - 0.02) if ny else 0.9
    by0, by1 = int(band_top * H), int(band_bot * H)
    resp = None
    counts, spans = [], []
    for i, q in enumerate(px):
        ch = 0
        n = 0
        ys = []
        for y in range(int(H * 0.04), H):
            for x in range(W):
                r, g, b = q[x, y]
                r0, g0, b0 = first[x, y]
                d0 = abs(r - r0) + abs(g - g0) + abs(b - b0)
                if by0 <= y <= by1:
                    continue  # (и за отговора: иглата в лентата може да се движи и преди него)
                if d0 >= 60:
                    ch += 1
                if d0 < 70:
                    continue
                r1, g1, b1 = last[x, y]
                if abs(r - r1) + abs(g - g1) + abs(b - b1) < 70:
                    continue  # остава и в края — не е конфети (отбелязаният отговор)
                _, s, v = colorsys.rgb_to_hsv(r / 255, g / 255, b / 255)
                if s > 0.35 and v > 0.3:
                    n += 1
                    ys.append(y)
        if resp is None and ch >= W * H * 0.002:
            resp = i
        counts.append(n)
        spans.append((min(ys), max(ys)) if ys else None)
    hot = [i for i, n in enumerate(counts) if n >= 8]
    print(f"cftime: {len(fs)} frames at {fps:g}/s; band {band_top * 100:.0f}-{band_bot * 100:.0f}% of the height; "
          f"transient saturated points outside the band per frame: {counts}")
    if resp is None:
        print("cftime: no frame changed (the tap was not seen)")
        return
    if not hot:
        print(f"cftime: tap response at frame #{resp + 1}; NO confetti-like transient points (>=8) after it")
        return
    h0 = next((i for i in hot if i >= resp), hot[0])
    peak = max(range(len(counts)), key=lambda i: counts[i])
    above = [spans[i] for i in hot[:4] if spans[i]]
    hi = min(s[0] for s in above) if above else None
    first_span = spans[h0]
    print(f"cftime: tap response at frame #{resp + 1}; first confetti frame #{h0 + 1} = {(h0 - resp) * 1000 / fps:.0f} ms after it "
          f"(±{1000 / fps:.0f} ms); peak #{peak + 1} ({counts[peak]} points, {(peak - resp) * 1000 / fps:.0f} ms); last #{hot[-1] + 1} "
          f"({(hot[-1] - resp) * 1000 / fps:.0f} ms); frames with confetti {len(hot)}")
    if first_span:
        print(f"cftime: first confetti frame spans {first_span[0] * 100 // H}-{first_span[1] * 100 // H}% of the height "
              f"(band top {band_top * 100:.0f}%); highest point in the first 4 confetti frames {hi * 100 // H if hi is not None else '-'}%; "
              f"{'starts ABOVE the band' if first_span[0] < by0 else 'starts at/below the band top'}")


def cmd_jpeg(out):
    from PIL import Image, ImageDraw
    im = Image.new('RGB', (1600, 1200), (196, 178, 140))
    d = ImageDraw.Draw(im)
    for i in range(0, 1600, 40):
        d.line([(i, 0), (i - 300, 1200)], fill=(170, 150, 112), width=6)
    d.rectangle([200, 250, 1400, 950], outline=(90, 60, 40), width=18)
    d.ellipse([560, 330, 1040, 810], fill=(120, 40, 40))
    d.polygon([(800, 380), (980, 570), (800, 760), (620, 570)], fill=(220, 190, 90))
    d.text((240, 1000), "w26 test photo 1958", fill=(40, 30, 20))
    im.save(out, 'JPEG', quality=88)
    print(out, os.path.getsize(out))


if __name__ == '__main__':
    c, args = sys.argv[1], sys.argv[2:]
    {'scrollable': cmd_scrollable, 'years': cmd_years, 'cftime': cmd_cftime, 'jpeg': cmd_jpeg}[c](*args)
