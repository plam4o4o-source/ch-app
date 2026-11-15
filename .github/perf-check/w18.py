"""Помощни разчитания за проверката на вълна 18 (ВРЕМЕННО, debug/wave18-check).

  w18.py memtable DIR LABEL         — c00/c10/c20/c30 (raw и след trim): PSS, Java, Native, Graphics, GL/EGL mtrack…
  w18.py memrow FILE LABEL          — един ред от dumpsys meminfo (същите полета)
  w18.py fds FILE                   — `ls -l /proc/PID/fd`: колко дескриптора по вид на целта
  w18.py gfxmem FILE LABEL          — от dumpsys gfxinfo: GPU паметта, текстурите, слоевете, конвейерът
  w18.py drawn FILE                 — „Fully drawn …: +1s234ms“ → ms (всички редове във файла)
  w18.py zoomfix ORIG ZOOMED CX CY LABEL — увеличение s и неподвижната точка f (zoomed(x) = orig(f + (x-f)/s))
  w18.py shift A B LABEL            — изместване (phase correlation) от A към B, в пиксели
  w18.py diff A B                   — средна абсолютна разлика (0..255) в средната част на екрана
  w18.py perfcard UI                — текстовете на картата „Бързодействие“ (uiautomator dump)
"""
import os, re, sys, statistics


def meminfo(text):
    def first(p):
        m = re.search(p, text, re.M)
        return int(m.group(1)) if m else None
    row = lambda name: first(r'^\s*' + name + r'\s+(\d+)')
    d = {
        'PSS': first(r'TOTAL PSS:\s*(\d+)') or first(r'^\s*TOTAL\s+(\d+)'),
        'Java': first(r'Java Heap:\s*(\d+)'),
        'Native': first(r'Native Heap:\s*(\d+)'),
        'Graphics': first(r'Graphics:\s*(\d+)'),
        'GL': row('GL mtrack'),
        'EGL': row('EGL mtrack'),
        'GfxDev': row('Gfx dev'),
        'OtherDev': row('Other dev'),
        'Unknown': row('Unknown'),
        'PrivOther': first(r'Private Other:\s*(\d+)'),
        'RSS': first(r'TOTAL RSS:\s*(\d+)'),
    }
    return d


KEYS = ['PSS', 'Java', 'Native', 'Graphics', 'GL', 'EGL', 'GfxDev', 'OtherDev', 'Unknown', 'PrivOther', 'RSS']


def fmt(d):
    return '  '.join(f"{k}={(d[k] / 1024):.1f}" if d[k] is not None else f"{k}=?" for k in KEYS)


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


def cmd_memtable(dirn, label):
    print(f"MEMTABLE {label} (MB; Graphics = GL mtrack + EGL mtrack + Gfx dev in the App Summary)")
    for c in ('00', '10', '20', '30'):
        for kind, f in (('raw', f'mem-c{c}-raw.txt'), ('trim', f'mem-c{c}.txt')):
            p = os.path.join(dirn, f)
            if os.path.exists(p):
                print(f"  c{c} {kind:<4} {fmt(meminfo(read(p)))}")


def cmd_memrow(path, label):
    print(f"MEMROW {label:<22} {fmt(meminfo(read(path)))}")


def cmd_fds(path):
    from collections import Counter
    cnt = Counter()
    for l in read(path).splitlines():
        m = re.search(r'-> (.*)$', l)
        if not m:
            continue
        t = m.group(1).strip()
        t = re.sub(r'^(socket|pipe):\[\d+\]', r'\1', t)
        t = re.sub(r'^anon_inode:\[?([^\]]+)\]?', r'anon_inode:\1', t)
        if t.startswith('/data/'):
            t = '/data/' + '/'.join(t.split('/')[2:5]) + '/…'
        t = re.sub(r'\d{3,}', 'N', t)
        cnt[t] += 1
    tot = sum(cnt.values())
    print(f"FDS total={tot}: " + '; '.join(f"{k}×{v}" for k, v in cnt.most_common(14)))


def cmd_gfxmem(path, label):
    t = read(path)
    out = []
    for name, pat in (('pipeline', r'Pipeline=([^\n]+)'), ('GPU total', r'Total GPU memory usage:\s*\n?\s*([^\n]+)'),
                      ('layers', r'Layers Total\s+([^\n]+)'), ('textures', r'Texture:\s*([^\n]+)'),
                      ('buffers', r'([^\n]*[Gg]raphic ?[Bb]uffer[^\n]*)'), ('frames', r'Total frames rendered:\s*(\d+)')):
        m = re.findall(pat, t)
        if m:
            out.append(f"{name}: " + ' | '.join(x.strip() for x in m[:3]))
    print(f"GFXMEM {label}: " + ('; '.join(out) if out else '(nothing)'))


def drawn_ms(s):
    m = re.search(r'\+(?:(\d+)s)?(\d+)ms', s)
    return (int(m.group(1) or 0) * 1000 + int(m.group(2))) if m else None


def cmd_drawn(path):
    vals = [drawn_ms(l) for l in read(path).splitlines() if 'Fully drawn' in l]
    vals = [v for v in vals if v is not None]
    if vals:
        print(f"n={len(vals)} median={statistics.median(vals):.0f}ms min={min(vals)} max={max(vals)} all={vals}")
    else:
        print("n=0 (no 'Fully drawn' line)")


def gray(p, scale=2):
    from PIL import Image
    import numpy as np
    im = Image.open(p).convert('L')
    im = im.resize((im.width // scale, im.height // scale))
    return np.asarray(im, dtype=np.float32)


def cmd_zoomfix(orig, zoomed, cx, cy, label):
    import numpy as np
    import cv2
    S = 2
    A, B = gray(orig, S), gray(zoomed, S)
    h, w = B.shape
    # Средната част на увеличения кадър (без лентата отгоре и навигацията).
    y0, y1 = int(h * 0.20), int(h * 0.82)
    x0, x1 = int(w * 0.04), int(w * 0.96)
    roi = B[y0:y1, x0:x1]
    if roi.std() < 3:
        print(f"ZOOMFIX {label}: zoomed frame is flat (std {roi.std():.1f}) — no estimate"); return
    best = None
    for s in np.arange(1.15, 4.6, 0.025):
        tw, th = int(roi.shape[1] / s), int(roi.shape[0] / s)
        if tw < 40 or th < 40 or tw >= w or th >= h:
            continue
        T = cv2.resize(roi, (tw, th), interpolation=cv2.INTER_AREA)
        r = cv2.matchTemplate(A, T, cv2.TM_CCOEFF_NORMED)
        _, mv, _, ml = cv2.minMaxLoc(r)
        if best is None or mv > best[0]:
            best = (mv, s, ml)
    if best is None:
        print(f"ZOOMFIX {label}: no estimate"); return
    mv, s, (lx, ly) = best
    # orig(loc + (x - roi0)/s) = zoomed(x): неподвижната точка f = (loc - roi0/s) / (1 - 1/s)
    fx = (lx - x0 / s) / (1 - 1 / s) * S
    fy = (ly - y0 / s) / (1 - 1 / s) * S
    W, H = w * S, h * S
    cx, cy = float(cx), float(cy)
    dF = ((fx - cx) ** 2 + (fy - cy) ** 2) ** 0.5
    dC = ((fx - W / 2) ** 2 + (fy - H / 2) ** 2) ** 0.5
    verdict = 'AROUND THE FINGERS' if dF < dC * 0.5 and dF < 120 else ('AROUND THE CENTRE' if dC < dF * 0.5 and dC < 120 else 'unclear')
    print(f"ZOOMFIX {label}: scale≈{s:.2f} fixed point≈({fx:.0f},{fy:.0f}) fingers=({cx:.0f},{cy:.0f}) "
          f"|f-fingers|={dF:.0f}px |f-centre({W // 2},{H // 2})|={dC:.0f}px match={mv:.3f} → {verdict}")


def cmd_shift(a, b, label):
    import numpy as np
    import cv2
    A, B = gray(a, 1), gray(b, 1)
    h, w = A.shape
    y0, y1 = int(h * 0.2), int(h * 0.82)
    (dx, dy), resp = cv2.phaseCorrelate(A[y0:y1], B[y0:y1])
    print(f"SHIFT {label}: dx={dx:+.0f}px dy={dy:+.0f}px (response {resp:.2f})")


def cmd_diff(a, b):
    import numpy as np
    A, B = gray(a, 2), gray(b, 2)
    h = A.shape[0]
    print(f"{np.abs(A[int(h * .2):int(h * .82)] - B[int(h * .2):int(h * .82)]).mean():.1f}")


def cmd_perfcard(ui):
    import xml.etree.ElementTree as ET
    texts = []
    for n in ET.parse(ui).iter('node'):
        t = n.get('text') or ''
        if t:
            texts.append(t)
    on = False
    out = []
    for t in texts:
        if t == 'Бързодействие' or t == 'Performance':
            on = True
        if on:
            out.append(t)
        if on and len(out) > 22:
            break
    print(' | '.join(out) if out else '(card not on screen) ' + ' | '.join(texts[:12]))


if __name__ == '__main__':
    c, args = sys.argv[1], sys.argv[2:]
    {'memtable': cmd_memtable, 'memrow': cmd_memrow, 'fds': cmd_fds, 'gfxmem': cmd_gfxmem, 'drawn': cmd_drawn,
     'zoomfix': cmd_zoomfix, 'shift': cmd_shift, 'diff': cmd_diff, 'perfcard': cmd_perfcard}[c](*args)
