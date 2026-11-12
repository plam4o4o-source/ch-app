"""Разчитания за пълната проверка на издаденото v1.2.35 (ВРЕМЕННО, debug/a6-full).

  a6.py fstl FILE U0 U1        — `dumpsys gfxinfo <pkg> framestats`: кадрите с IntendedVsync между U0 и U1
                                 (секунди от /proc/uptime) — кога са (s от началото), на групи
  a6.py changes OUT P1 P2 …    — какво се мени между поредните снимки: правоъгълниците на разликите (px);
                                 OUT — контактен лист с първите промени (изрезки преди/след)
  a6.py sheets OUTDIR N P1 …   — контактни листове: по N снимки на лист (намалени), с името под всяка
  a6.py hist FILE              — хистограмата на gfxinfo: най-бавният кадър и броят над 50/100/200/500 ms
"""
import os, re, sys


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


def cmd_fstl(path, u0, u1):
    u0, u1 = float(u0), float(u1)
    fr = []
    for block in re.findall(r'---PROFILEDATA---\n(.*?)---PROFILEDATA---', read(path), re.S):
        lines = [l.strip().rstrip(',') for l in block.strip().splitlines() if l.strip()]
        if not lines or not lines[0].startswith('Flags'):
            continue
        ix = {k: i for i, k in enumerate(lines[0].split(','))}
        for l in lines[1:]:
            v = l.split(',')
            try:
                iv = int(v[ix['IntendedVsync']]); done = int(v[ix['FrameCompleted']])
            except (ValueError, IndexError, KeyError):
                continue
            if iv > 0:
                fr.append((iv / 1e9, (done - iv) / 1e6 if done > iv else 0.0))
    fr = sorted(set(fr))
    win = [(t - u0, d) for t, d in fr if u0 <= t <= u1]
    if not win:
        print(f"FSTL: 0 frames in the window ({len(fr)} in the buffer; window {u1 - u0:.1f} s)")
        return
    groups = [[win[0]]]
    for a, b in zip(win, win[1:]):
        if b[0] - a[0] > 0.25:
            groups.append([])
        groups[-1].append(b)
    desc = '; '.join(f"+{g[0][0]:.2f}s×{len(g)}({(g[-1][0] - g[0][0]) * 1000:.0f}ms)" for g in groups[:14])
    starts = [g[0][0] for g in groups]
    gaps = ' '.join(f"{b - a:.2f}" for a, b in zip(starts, starts[1:]))[:160]
    print(f"FSTL: {len(win)} frames in the window, {len(groups)} group(s) [start×frames(length)]: {desc}"
          f"{' …' if len(groups) > 14 else ''}; gaps between group starts (s): {gaps or '-'}; slowest {max(d for _, d in win):.0f} ms")


def _load(p, scale=4):
    from PIL import Image
    im = Image.open(p).convert('RGB')
    return im, im.resize((im.width // scale, im.height // scale)).convert('L')


def _bbox(a, b, thr=12):
    from PIL import ImageChops
    d = ImageChops.difference(a, b).point(lambda v: 255 if v > thr else 0)
    return d.getbbox()


def cmd_changes(out, *ps):
    from PIL import Image
    ps = [p for p in ps if os.path.exists(p)]
    if len(ps) < 2:
        print('changes: fewer than 2 shots')
        return
    sc = 4
    prev_full, prev = _load(ps[0], sc)
    res, crops = [], []
    for p in ps[1:]:
        full, small = _load(p, sc)
        if small.size == prev.size:
            # лентата на състоянието (часовникът) не се брои
            top = int(prev.height * 0.045)
            bb = _bbox(prev.crop((0, top, prev.width, prev.height)), small.crop((0, top, small.width, small.height)))
            if bb:
                x0, y0, x1, y1 = bb[0] * sc, (bb[1] + top) * sc, bb[2] * sc, (bb[3] + top) * sc
                res.append(f"{os.path.basename(p).rsplit('.', 1)[0][-2:]}:[{x0},{y0}][{x1},{y1}]")
                if len(crops) < 4:
                    m = 40
                    box = (max(0, x0 - m), max(0, y0 - m), min(full.width, x1 + m), min(full.height, y1 + m))
                    crops.append((prev_full.crop(box), full.crop(box)))
            else:
                res.append(f"{os.path.basename(p).rsplit('.', 1)[0][-2:]}:same")
        prev_full, prev = full, small
    changed = [r for r in res if not r.endswith(':same')]
    print(f"CHANGES: {len(changed)} of {len(res)} consecutive pairs differ: {' '.join(res)[:900]}")
    if crops:
        tiles = []
        for a, b in crops:
            w = min(520, a.width)
            h = max(1, int(a.height * w / a.width))
            if h > 700:
                h = 700; w = max(1, int(a.width * h / a.height))
            t = Image.new('RGB', (w * 2 + 6, h), (255, 0, 255))
            t.paste(a.resize((w, h)), (0, 0)); t.paste(b.resize((w, h)), (w + 6, 0))
            tiles.append(t)
        W = max(t.width for t in tiles); H = sum(t.height for t in tiles) + 6 * (len(tiles) - 1)
        sheet = Image.new('RGB', (W, H), (40, 40, 40))
        y = 0
        for t in tiles:
            sheet.paste(t, (0, y)); y += t.height + 6
        os.makedirs(os.path.dirname(out) or '.', exist_ok=True)
        sheet.save(out, optimize=True)


def cmd_sheets(outdir, n, *ps):
    from PIL import Image, ImageDraw
    n = int(n)
    ps = [p for p in ps if os.path.exists(p)]
    os.makedirs(outdir, exist_ok=True)
    made = 0
    for k in range(0, len(ps), n):
        chunk = ps[k:k + n]
        tiles = []
        for p in chunk:
            try:
                im = Image.open(p).convert('RGB')
            except Exception:  # noqa: BLE001
                continue
            land = im.width > im.height
            tw = 620 if land else 360
            th = max(1, int(im.height * tw / im.width))
            im = im.resize((tw, th))
            t = Image.new('RGB', (tw, th + 22), (255, 255, 255))
            t.paste(im, (0, 0))
            ImageDraw.Draw(t).text((4, th + 5), os.path.basename(p).rsplit('.', 1)[0][:56], fill=(0, 0, 0))
            tiles.append(t)
        if not tiles:
            continue
        cols = min(len(tiles), 3 if tiles[0].width > 400 else 5)
        rows = (len(tiles) + cols - 1) // cols
        tw, th = max(t.width for t in tiles), max(t.height for t in tiles)
        sheet = Image.new('RGB', (cols * (tw + 8), rows * (th + 8)), (90, 90, 90))
        for i, t in enumerate(tiles):
            sheet.paste(t, ((i % cols) * (tw + 8), (i // cols) * (th + 8)))
        sheet.save(os.path.join(outdir, f"sheet-{k // n + 1:02d}.jpg"), quality=72, optimize=True)
        made += 1
    print(f"SHEETS {outdir}: {made} sheet(s) of {len(ps)} shots")


def cmd_hist(path):
    t = read(path)
    m = re.search(r'HISTOGRAM:\s*(.*)', t)
    if not m:
        print('HIST: no histogram')
        return
    b = [(int(x), int(c)) for x, c in re.findall(r'(\d+)ms=(\d+)', m.group(1))]
    nz = [x for x, c in b if c]
    over = lambda ms: sum(c for x, c in b if x >= ms)
    tot = sum(c for _, c in b)
    print(f"HIST: frames={tot} slowest-bucket={max(nz) if nz else 0}ms ≥50ms={over(50)} ≥100ms={over(100)} ≥200ms={over(200)} ≥500ms={over(500)}")


def cmd_crashes(logcat, out):
    """Пълните стекове на FATAL EXCEPTION и блоковете „ANR in“ от logcat (-v threadtime)."""
    lines = read(logcat).splitlines()
    blocks, i = [], 0
    while i < len(lines):
        l = lines[i]
        if 'FATAL EXCEPTION' in l or re.search(r'ANR in |Fatal signal \d+', l):
            m = re.match(r'^\S+\s+\S+\s+(\d+)\s+(\d+)\s+\w\s+([^:]+):', l)
            key = (m.group(1), m.group(3).strip()) if m else None
            blk = [l]
            j = i + 1
            while j < len(lines) and len(blk) < 120:
                m2 = re.match(r'^\S+\s+\S+\s+(\d+)\s+(\d+)\s+\w\s+([^:]+):', lines[j])
                if not m2 or key is None or (m2.group(1), m2.group(3).strip()) != key:
                    break
                blk.append(lines[j]); j += 1
            blocks.append(blk); i = j
        else:
            i += 1
    with open(out, 'w', encoding='utf-8') as f:
        for b in blocks:
            f.write('\n'.join(x[:400] for x in b) + '\n\n')
    kinds = {}
    for b in blocks:
        k = 'FATAL' if 'FATAL EXCEPTION' in b[0] else ('ANR' if 'ANR in' in b[0] else 'SIGNAL')
        kinds[k] = kinds.get(k, 0) + 1
    print(f"CRASHES: {len(blocks)} block(s) {kinds or ''} → {os.path.basename(out)}")


if __name__ == '__main__':
    c, args = sys.argv[1], sys.argv[2:]
    try:
        globals()['cmd_' + c](*args)
    except Exception as e:  # noqa: BLE001
        print(f"a6.py {c}: error {type(e).__name__}: {e}")
