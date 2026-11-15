"""„Начало“: y на всеки раздел по кадри от screenrecord (на хоста).

  homey_sections.py FRAMES_DIR FINAL_UI_XML SCALE FPS OFFSET_S LABEL

FRAMES_DIR — f0001.png … (ffmpeg -vf fps=FPS); FINAL_UI_XML — uiautomator dump след
успокояване (координати на екрана); SCALE — кадър/екран; OFFSET_S — секунди от началото на
записа до `am start`. Разделите се вземат от последния dump: полето за търсене, бързите
действия, „Предстоящи събития“, „Нови постъпления“, „За теб“, всяка „Витрина“, „Още новини“
и броячите. Шаблонът е изрезката на заглавието от последния кадър; във всеки кадър се търси по
вертикала (в същите колони) най-доброто съвпадение. Съвпадение се приема само ако разликата е
малка И изрезката има текст (контраст като на шаблона) — иначе празен фон би „съвпаднал“.
Печата y по време и за всеки раздел: първа поява, крайна позиция, колко пъти и с колко е
скачал след първата си поява.
"""
import os, re, sys, xml.etree.ElementTree as ET
import numpy as np
from PIL import Image

d, xml, scale, fps, off, label = sys.argv[1], sys.argv[2], float(sys.argv[3]), float(sys.argv[4]), float(sys.argv[5]), sys.argv[6]
frames = sorted(f for f in os.listdir(d) if re.match(r'f\d+\.png$', f))
if not frames:
    print(f"HOMEY {label}: no frames"); sys.exit(0)

FIXED = [
    ('SEARCH', lambda t: t.startswith(('Търси книга', 'Search books'))),
    ('ACTIONS', lambda t: t in ('Сканирай', 'Scan', 'Читателска карта', 'Library card', 'Моите книги', 'My books')),
    ('EVENTS', lambda t: t in ('Предстоящи събития', 'Upcoming events')),
    ('NEW', lambda t: t in ('Нови постъпления', 'New arrivals')),
    ('FORYOU', lambda t: t in ('За теб', 'For you')),
    ('NEWS', lambda t: t in ('Още новини', 'More news')),
    ('STATS', lambda t: t in ('години история', 'years of history')),
]
try:
    nodes = [n for n in ET.parse(xml).getroot().iter('node')]
except Exception as e:  # noqa: BLE001
    print(f"HOMEY {label}: final dump unreadable ({e})"); sys.exit(0)


def box(n):
    m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
    return tuple(int(int(v) * scale) for v in m.groups()) if m else None


pos = {}
texts = [(n, (n.get('text') or '').strip()) for n in nodes]
for key, ok in FIXED:
    for n, t in texts:
        if t and ok(t):
            pos[key] = box(n); break
shelf = 0
for i, (n, t) in enumerate(texts):
    if t.lower() in ('витрина', 'shelf'):
        for m, t2 in texts[i + 1:i + 4]:
            if t2:
                shelf += 1; pos[f'SHELF{shelf}:{t2[:18]}'] = box(m); break

imgs = [np.asarray(Image.open(os.path.join(d, f)).convert('L'), dtype=np.float32) for f in frames]
last = imgs[-1]
H, W = last.shape
tpl = {}
for k, b in pos.items():
    if not b:
        continue
    l, t, r, bt = b
    l2, r2 = max(0, l - 2), min(W, r + 2)
    t2, b2 = max(0, t - 1), min(H, bt + 1)
    if b2 - t2 > 3 and r2 - l2 > 6:
        crop = last[t2:b2, l2:r2]
        tpl[k] = (l2, r2, crop, t2, float(crop.std()))
TH = 12.0
rows = []
for i, im in enumerate(imgs):
    ys = {}
    for k, (l2, r2, tp, t0, sd) in tpl.items():
        h = tp.shape[0]
        col = im[:, l2:r2]
        # все прозорци по вертикала: средна абсолютна разлика и контраст
        win = np.lib.stride_tricks.sliding_window_view(col, (h, r2 - l2))[:, 0]
        diff = np.abs(win - tp).mean(axis=(1, 2))
        std = win.std(axis=(1, 2))
        diff[std < 0.5 * sd] = 1e9
        y = int(diff.argmin())
        ys[k] = y if diff[y] < TH else None
    rows.append((i / fps - off, ys))
print(f"HOMEY {label}: {len(frames)} frames @ {fps:g} fps; sections found in the final dump: {len(tpl)}; final y (frame px, x{scale:g} of screen): "
      + ' '.join(f"{k}={v[3]}" for k, v in tpl.items()))
line = []
for t, ys in rows:
    if t < -0.05:
        continue
    line.append(f"{t:.1f}s " + ','.join(f"{k.split(':')[0]}={ys[k] if ys[k] is not None else '-'}" for k in tpl))
print(f"HOMEY {label} y by time after am start: " + ' | '.join(line))
summ, worst = [], 0
for k in tpl:
    seen = [(t, ys[k]) for t, ys in rows if ys[k] is not None and t >= -0.05]
    if not seen:
        summ.append(f"{k}: never matched"); continue
    vals = [y for _, y in seen]
    jumps = [(round(seen[j + 1][0], 1), b - a) for j, (a, b) in enumerate(zip(vals, vals[1:])) if abs(a - b) > 2]
    worst = max(worst, max((abs(j[1]) for j in jumps), default=0))
    summ.append(f"{k}: first {seen[0][0]:.1f}s y={vals[0]}, final {vals[-1]}, jumps={len(jumps)}"
                + (f" {[(t, f'{dy:+d}') for t, dy in jumps][:5]}" if jumps else ''))
print(f"HOMEY {label} summary (frame px; x{1/scale:g} on screen): " + '; '.join(summ))
print(f"HOMEY {label} LARGEST JUMP after a section first appeared: {worst} frame px = {int(worst / scale)} screen px")
