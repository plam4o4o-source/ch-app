"""Скокове на „Начало“ по кадри от screenrecord (на хоста).

  homey_frames.py FRAMES_DIR FINAL_UI_XML SCALE FPS OFFSET_S LABEL
FRAMES_DIR — f0001.png … (ffmpeg -vf fps=FPS,scale=…); FINAL_UI_XML — uiautomator dump след
успокояване (координати на екрана); SCALE — кадър/екран (напр. 0.5); OFFSET_S — секунди от
началото на записа до `am start`. За всеки заглавен ред („Нови постъпления“, „За теб“,
„Предстоящи събития“) шаблонът е от последния кадър; във всеки кадър се търси най-доброто
съвпадение по вертикала (средна абсолютна разлика в сивото). Печата y по кадри и колко се е
местил всеки ред след първата си поява.
"""
import os, re, sys, xml.etree.ElementTree as ET
from PIL import Image, ImageChops, ImageStat

d, xml, scale, fps, off, label = sys.argv[1], sys.argv[2], float(sys.argv[3]), float(sys.argv[4]), float(sys.argv[5]), sys.argv[6]
frames = sorted(f for f in os.listdir(d) if re.match(r'f\d+\.png$', f))
if not frames:
    print(f"HOMEY-FRAMES {label}: no frames"); sys.exit(0)
anchors = {'NEW': ('Нови постъпления', 'New arrivals'), 'FORYOU': ('За теб', 'For you'), 'EVENTS': ('Предстоящи събития', 'Upcoming events')}
pos = {}
try:
    root = ET.parse(xml).getroot()
    for k, names in anchors.items():
        for n in root.iter('node'):
            if (n.get('text') or '') in names:
                m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
                pos[k] = tuple(int(int(v) * scale) for v in m.groups()); break
except Exception as e:  # noqa: BLE001
    print(f"HOMEY-FRAMES {label}: final dump unreadable ({e})"); sys.exit(0)
imgs = [Image.open(os.path.join(d, f)).convert('L') for f in frames]
last = imgs[-1]
W, H = last.size
tpl = {}
for k, (l, t, r, b) in pos.items():
    l2, r2 = max(0, l - 4), min(W, max(r + 4, l + 120))
    t2, b2 = max(0, t - 2), min(H, b + 2)
    if b2 - t2 > 4:
        tpl[k] = (l2, r2, last.crop((l2, t2, r2, b2)), t2)
TH = 14.0
rows = []
for i, im in enumerate(imgs):
    ys = {}
    for k, (l2, r2, tp, t0) in tpl.items():
        h = tp.size[1]; best = (1e9, None)
        for y in range(0, H - h, 1):
            s = ImageStat.Stat(ImageChops.difference(im.crop((l2, y, r2, y + h)), tp)).mean[0]
            if s < best[0]:
                best = (s, y)
        ys[k] = best[1] if best[0] < TH else None
    rows.append((i / fps - off, ys))
print(f"HOMEY-FRAMES {label}: {len(frames)} frames @ {fps:g} fps; final y (frame px, x{scale:g} of screen): " + ' '.join(f"{k}={v[3]}" for k, v in tpl.items()))
line = []
for t, ys in rows:
    if t < -0.05:
        continue
    line.append(f"{t:.1f}s " + ','.join(f"{k[0]}{ys[k] if ys[k] is not None else '-'}" for k in tpl))
print(f"HOMEY-FRAMES {label} y by time after am start: " + ' | '.join(line))
summ = []
for k in tpl:
    seen = [(t, ys[k]) for t, ys in rows if ys[k] is not None and t >= -0.05]
    if not seen:
        summ.append(f"{k}: never matched"); continue
    vals = [y for _, y in seen]
    moves = sum(1 for a, b in zip(vals, vals[1:]) if abs(a - b) > 2)
    summ.append(f"{k}: first at {seen[0][0]:.1f}s y={vals[0]}, final y={vals[-1]}, range={max(vals) - min(vals)}px (x{1/scale:g} on screen), jumps={moves}")
print(f"HOMEY-FRAMES {label} summary: " + '; '.join(summ))
