"""Помощни разчитания за проверката на вълна 19 (ВРЕМЕННО, debug/wave19-check).

  w19.py ribbon UI                   — правоъгълникът на лентата „жива шевица“ под заглавката на „Начало“
  w19.py rstat PNG BOX               — средно, разсейване и дял „конци“ в BOX (x0 y0 x1 y1)
  w19.py rdiff A B BOX               — средна абсолютна разлика (0..255) в BOX
  w19.py seq BOX PNG...              — разликите между поредни снимки в BOX
  w19.py frames GFX                  — „Total frames rendered“ от dumpsys gfxinfo
  w19.py cpu T0 T1 SECS              — CPU на процеса и нишките му между два снимка на /proc/PID/task/*/stat
  w19.py png FILE                    — подпис и размери на PNG
  w19.py sheet OUT CROP PNG...       — контактен лист (CROP: x0,y0,x1,y1 или all) от кадрите
  w19.py motion CROP PNG...          — движение по кадри: разлика с предишния и с първия кадър
  w19.py confetti CROP BASE PNG...   — наситени цветни точки, които ги няма в BASE (конфети)
  w19.py slides LOG                  — смените на слайда в историята (събитията на PC a11y)
  w19.py notif FILE PKG              — живите известия: ProgressStyle, молба за повишаване, чип, флагове
"""
import glob, os, re, sys


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


def box_of(s):
    if s in ('all', '', None):
        return None
    return tuple(int(v) for v in re.split(r'[ ,]+', s.strip()))


def img(p, crop=None, gray=True):
    from PIL import Image
    im = Image.open(p).convert('L' if gray else 'RGB')
    if crop:
        im = im.crop(crop)
    return im


def cmd_ribbon(ui):
    """Между долния ръб на заглавката („с. Яворец“) и горния на лентата за търсене."""
    import xml.etree.ElementTree as ET
    try:
        root = ET.parse(ui).getroot()
    except Exception:
        return
    def b(n):
        m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
        return tuple(map(int, m.groups())) if m else None
    top, bottom, width = None, None, 0
    for n in root.iter('node'):
        t, d = n.get('text', ''), n.get('content-desc', '')
        bb = b(n)
        if not bb:
            continue
        width = max(width, bb[2])
        if t in ('с. Яворец', 'Yavorets village', 'Yavorets') and top is None:
            top = bb[3]
        if bottom is None and (d in ('Лента за търсене', 'Search bar') or t.startswith('Търси книга') or t.startswith('Search books')):
            bottom = bb[1]
    if top is None or bottom is None or bottom - top < 20:
        return
    # без 4 px по краищата (антиалиасинг на съседите)
    print(f"0 {top + 4} {width} {bottom - 4}")


def cmd_rstat(p, box):
    from PIL import ImageStat
    im = img(p, box_of(box))
    st = ImageStat.Stat(im)
    px = list(im.getdata())
    bg = max(set(px[::7]), key=px[::7].count) if px else 0
    ink = sum(1 for v in px if abs(v - bg) > 24) / max(1, len(px))
    print(f"mean={st.mean[0]:.1f} std={st.stddev[0]:.1f} thread-px={ink * 100:.1f}%")


def diff(a, b):
    from PIL import ImageChops, ImageStat
    return ImageStat.Stat(ImageChops.difference(a, b)).mean[0]


def cmd_rdiff(a, b, box):
    c = box_of(box)
    print(f"{diff(img(a, c), img(b, c)):.2f}")


def cmd_seq(box, *ps):
    c = box_of(box)
    ims = [img(p, c) for p in ps if os.path.exists(p)]
    print(' '.join(f"{diff(x, y):.2f}" for x, y in zip(ims, ims[1:])))


def cmd_frames(p):
    m = re.search(r'Total frames rendered:\s*(\d+)', read(p))
    print(m.group(1) if m else -1)


def tasks(p):
    out = {}
    for l in read(p).splitlines():
        if ')' not in l:
            continue
        pid = l.split(' ', 1)[0]
        name = l[l.find('(') + 1:l.rfind(')')]
        f = l[l.rfind(')') + 2:].split()
        try:
            out[pid] = (name, int(f[11]) + int(f[12]))
        except Exception:
            pass
    return out


def cmd_cpu(t0, t1, secs):
    a, b = tasks(t0), tasks(t1)
    secs = float(secs)
    rows = []
    for k, (name, v) in b.items():
        d = v - a[k][1] if k in a else v  # нишка, родена в прозореца — всичките ѝ тикове са в него
        rows.append((d, name))
    tot = sum(d for d, _ in rows)
    rows.sort(reverse=True)
    top = ', '.join(f"{n}={d / secs:.1f}%" for d, n in rows[:5] if d > 0)
    print(f"cpu={tot / secs:.1f}% (ticks {tot} in {secs:.0f} s; 100 ticks = 1 cpu-s) threads: {top or 'none'}")


def cmd_png(p):
    try:
        h = open(p, 'rb').read(32)
        from PIL import Image
        im = Image.open(p)
        print(f"PNG-signature={'yes' if h[:8] == bytes.fromhex('89504e470d0a1a0a') else 'NO'} size={os.path.getsize(p)} B dims={im.size[0]}x{im.size[1]} mode={im.mode}")
    except Exception as e:  # noqa: BLE001
        print(f"PNG read failed: {e}")


def cmd_sheet(out, crop, *ps):
    from PIL import Image
    ps = [p for p in ps if os.path.exists(p)]
    if not ps:
        print('sheet: no frames')
        return
    c = box_of(crop)
    ims = [img(p, c, gray=False) for p in ps]
    if len(ims) > 16:
        step = len(ims) / 16
        ims = [ims[int(i * step)] for i in range(16)]
    w = 260
    ims = [im.resize((w, max(1, int(im.size[1] * w / im.size[0])))) for im in ims]
    cols = 4 if ims[0].size[1] > w else 2
    rows = (len(ims) + cols - 1) // cols
    h = max(im.size[1] for im in ims)
    sheet = Image.new('RGB', (cols * (w + 6), rows * (h + 6)), (40, 40, 40))
    for i, im in enumerate(ims):
        sheet.paste(im, ((i % cols) * (w + 6), (i // cols) * (h + 6)))
    sheet.save(out, optimize=True)
    print(f"sheet {os.path.basename(out)}: {len(ims)} frames")


def cmd_motion(crop, *ps):
    c = box_of(crop)
    ps = [p for p in ps if os.path.exists(p)]
    ims = [img(p, c).resize((180, 400)) for p in ps]
    if not ims:
        print('motion: no frames')
        return
    prev = [diff(x, y) for x, y in zip(ims, ims[1:])]
    first = [diff(ims[0], y) for y in ims[1:]]
    busy = sum(1 for v in prev if v > 1.0)
    print(f"frames={len(ims)} moving-frames(>1.0)={busy} max-step={max(prev or [0]):.1f} max-vs-first={max(first or [0]):.1f} steps=[{' '.join(f'{v:.1f}' for v in prev)}]")


def cmd_confetti(crop, base, *ps):
    import colorsys
    c = box_of(crop)
    b = img(base, c, gray=False).resize((270, 600))
    bp = b.load()
    best = 0
    per = []
    for p in ps:
        if not os.path.exists(p):
            continue
        im = img(p, c, gray=False).resize((270, 600))
        q = im.load()
        n = 0
        for y in range(0, 600, 2):
            for x in range(0, 270, 2):
                r, g, bl = q[x, y]
                r0, g0, b0 = bp[x, y]
                if abs(r - r0) + abs(g - g0) + abs(bl - b0) < 90:
                    continue
                _, s, v = colorsys.rgb_to_hsv(r / 255, g / 255, bl / 255)
                if s > 0.45 and v > 0.35:
                    n += 1
        per.append(n)
        best = max(best, n)
    print(f"confetti-like new saturated points per frame (max {best}): {per}")


def cmd_slides(log):
    """PC a11y събития от приложението: смяната на content-desc на слайда (с часа на logcat)."""
    last, t0, out = None, None, []
    for l in read(log).splitlines():
        if 'PCLIVE' not in l or 'desc=' not in l:
            continue
        m = re.search(r'^\S+ (\d+):(\d+):(\d+)\.(\d+)', l)
        d = re.search(r'desc=(.*?) state=(.*)$', l)
        if not m or not d:
            continue
        if 'Слайд' not in d.group(2) and 'Slide' not in d.group(2):
            continue
        desc = d.group(2).strip() + ' | ' + d.group(1)
        t = int(m.group(1)) * 3600 + int(m.group(2)) * 60 + int(m.group(3)) + int(m.group(4)) / 1000
        if desc != last:
            t0 = t0 if t0 is not None else t
            out.append((t - t0, desc[:70]))
            last = desc
    for t, d in out[:24]:
        print(f"  +{t:5.1f} s  {d}")
    gaps = [b[0] - a[0] for a, b in zip(out, out[1:])]
    print(f"SLIDE changes: {len(out)}; gaps (s): {' '.join(f'{g:.1f}' for g in gaps)}")


def cmd_notif(f, pkg):
    t = read(f)
    recs = re.split(r'\n\s*NotificationRecord\(', t)
    n = 0
    for r in recs[1:]:
        head = r.split('\n', 1)[0]
        if f'pkg={pkg}' not in head:
            continue
        n += 1
        g = lambda k: (re.search(rf'\b{re.escape(k)}=([^\n]*)', r) or [None, '-'])[1].strip()[:90]
        flags = (re.search(r'flags=(0x[0-9a-fA-F]+)', r) or [None, '?'])[1]
        promo_flag = '?'
        try:
            promo_flag = 'yes' if int(flags, 16) & 0x40000 else 'no'
        except Exception:
            pass
        nid = (re.search(r'id=(-?\d+)', head) or [0, '?'])[1]
        style = (re.search(r'android\.template=([^\n]*)', r) or [None, '-'])[1].strip()
        print(f"LIVE id={nid} title={g('android.title')} text={g('android.text')} template={style} "
              f"progress={g('android.progress')} requestPromotedOngoing={g('android.requestPromotedOngoing')} "
              f"shortCriticalText={g('android.shortCriticalText')} chronometer={g('android.showChronometer')} "
              f"flags={flags} FLAG_PROMOTED_ONGOING={promo_flag} promotable={'isPromotable' in r or 'promotable' in r.lower()} "
              f"ongoing={'FLAG_ONGOING_EVENT' in r or 'ongoing=true' in r}")
        for l in [l for l in r.splitlines() if 'flags=' in l or 'Notification(' in l][:3]:
            print('   raw: ' + l.strip()[:240])
        for k in ('promot', 'Promot', 'ProgressStyle', 'criticalText', 'CriticalText'):
            for l in r.splitlines():
                if k in l:
                    print('   ' + l.strip()[:200])
                    break
    print(f"LIVE total for {pkg}: {n}")


if __name__ == '__main__':
    c, args = sys.argv[1], sys.argv[2:]
    globals()['cmd_' + c](*args)
