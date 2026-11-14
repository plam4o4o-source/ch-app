# -*- coding: utf-8 -*-
"""Helpers for w25.sh (TEMPORARY, debug/w25-check, results in emulator-results/v138b).

  w25.py fs BEFORE AFTER SCEN VER REP CSV   - `dumpsys gfxinfo <pkg> framestats` after a scripted gesture: the frames
                                              that are not in BEFORE; one line with median/p90/p99 and the split;
                                              one CSV row per frame is appended to CSV
  w25.py fsagg CSV [MD]                     - per scenario and version: per-repetition medians, spread, rank test
  w25.py mem FILE VER CYCLE CSV             - `dumpsys meminfo <pkg>`: the App Summary buckets and object counts
  w25.py memagg CSV                         - table per version, growth and slope over the cycles
  w25.py confetti DIR [L T R B]             - frames (jpg/png) of a recording: confetti-like points per frame
                                              outside the rectangle L T R B (in 0..1 of the frame)
  w25.py panes UI.xml W                     - texts of a uiautomator dump split into the left and right pane
  w25.py loginbtn UI.xml W H                - the centre of the sign-in BUTTON (clickable, wide, below the top bar)
  w25.py w25log LOGCAT MARK                 - W25 log lines after the PERFCHECK marker MARK, times relative to it
  w25.py slidedesc UI.xml                   - the longest content-desc on the screen (the year-story slide)
  w25.py clipped UI.xml W H                 - nodes whose bounds are cut by the screen edge or overlap each other
"""
import math
import os
import re
import statistics
import sys
import xml.etree.ElementTree as ET
from collections import defaultdict


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


# ---------------------------------------------------------------- framestats
COLS = ('Flags', 'IntendedVsync', 'Vsync', 'HandleInputStart', 'AnimationStart', 'PerformTraversalsStart', 'DrawStart',
        'SyncQueued', 'SyncStart', 'IssueDrawCommandsStart', 'SwapBuffers', 'FrameCompleted', 'GpuCompleted',
        'DequeueBufferDuration', 'QueueBufferDuration')


def fs_rows(path):
    out = {}
    try:
        text = read(path)
    except OSError:
        return out
    for block in re.findall(r'---PROFILEDATA---\n(.*?)---PROFILEDATA---', text, re.S):
        lines = [l.strip().rstrip(',') for l in block.strip().splitlines() if l.strip()]
        if not lines or not lines[0].startswith('Flags'):
            continue
        ix = {k: i for i, k in enumerate(lines[0].split(','))}
        for l in lines[1:]:
            v = l.split(',')
            try:
                r = {k: (int(v[ix[k]]) if k in ix and ix[k] < len(v) else 0) for k in COLS}
            except ValueError:
                continue
            if r['IntendedVsync'] <= 0 or r['FrameCompleted'] <= 0 or r['FrameCompleted'] < r['IntendedVsync']:
                continue
            out[r['IntendedVsync']] = r
    return out


def ms(a, b):
    return (b - a) / 1e6 if a and b and b >= a else float('nan')


def split(r):
    """The parts of one frame, ms. ui = the UI thread (input, animation, measure/layout, draw = recording the
    display lists); rt = the RenderThread (sync, issue = executing the draw commands, swap)."""
    return {
        'total': ms(r['IntendedVsync'], r['FrameCompleted']),
        'start': ms(r['IntendedVsync'], r['HandleInputStart']),
        'input': ms(r['HandleInputStart'], r['AnimationStart']),
        'anim': ms(r['AnimationStart'], r['PerformTraversalsStart']),
        'layout': ms(r['PerformTraversalsStart'], r['DrawStart']),
        'draw': ms(r['DrawStart'], r['SyncQueued']),
        'queue': ms(r['SyncQueued'], r['SyncStart']),
        'sync': ms(r['SyncStart'], r['IssueDrawCommandsStart']),
        'issue': ms(r['IssueDrawCommandsStart'], r['SwapBuffers']),
        'swap': ms(r['SwapBuffers'], r['FrameCompleted']),
        'ui': ms(r['HandleInputStart'], r['SyncQueued']),
        'rt': ms(r['SyncStart'], r['FrameCompleted']),
    }


PARTS = ('total', 'ui', 'rt', 'start', 'input', 'anim', 'layout', 'draw', 'queue', 'sync', 'issue', 'swap')


def pct(vals, q):
    v = sorted(x for x in vals if x == x)
    if not v:
        return float('nan')
    k = (len(v) - 1) * q / 100.0
    lo, hi = int(math.floor(k)), int(math.ceil(k))
    return v[lo] + (v[hi] - v[lo]) * (k - lo)


def cmd_fs(before, after, scen, ver, rep, csv):
    old = fs_rows(before)
    new = fs_rows(after)
    rows = [new[k] for k in sorted(new) if k not in old]
    n_all = len(rows)
    flagged = [r for r in rows if r['Flags'] != 0]
    rows = [r for r in rows if r['Flags'] == 0]
    sp = [split(r) for r in rows]
    if not sp:
        print(f"FS {scen} {ver} rep{rep}: no new frames (after={len(new)}, before={len(old)})")
        return
    head = not os.path.exists(csv)
    with open(csv, 'a', encoding='utf-8') as f:
        if head:
            f.write('scen,ver,rep,' + ','.join(PARTS) + '\n')
        for s in sp:
            f.write(f"{scen},{ver},{rep}," + ','.join(f"{s[k]:.2f}" for k in PARTS) + '\n')
    med = {k: pct([s[k] for s in sp], 50) for k in PARTS}
    full = ' BUFFER-FULL' if len(new) >= 120 and n_all >= 118 else ''
    print(f"FS {scen} {ver} rep{rep}: n={len(sp)} (+{len(flagged)} flagged){full} total p50={med['total']:.0f} p90={pct([s['total'] for s in sp], 90):.0f} "
          f"p99={pct([s['total'] for s in sp], 99):.0f} max={max(s['total'] for s in sp):.0f} ms | medians: ui={med['ui']:.1f} "
          f"(input {med['input']:.1f} anim {med['anim']:.1f} layout {med['layout']:.1f} draw {med['draw']:.1f}) rt={med['rt']:.1f} "
          f"(sync {med['sync']:.1f} issue {med['issue']:.1f} swap {med['swap']:.1f}) start-delay={med['start']:.1f} queue={med['queue']:.1f}")


def mwu_exact(a, b):
    """Two-sided Mann-Whitney U. Exact for small samples without ties, else the normal approximation."""
    n1, n2 = len(a), len(b)
    if not n1 or not n2:
        return float('nan'), float('nan')
    u = sum((1.0 if x > y else 0.5 if x == y else 0.0) for x in a for y in b)
    ties = len(set(a) | set(b)) < n1 + n2
    if n1 + n2 <= 20 and not ties:
        # the distribution of U by dynamic programming
        from functools import lru_cache

        @lru_cache(None)
        def cnt(i, j, k):
            if k < 0:
                return 0
            if i == 0 or j == 0:
                return 1 if k == 0 else 0
            return cnt(i - 1, j, k - j) + cnt(i, j - 1, k)
        tot = math.comb(n1 + n2, n1)
        um = min(u, n1 * n2 - u)
        p = 2 * sum(cnt(n1, n2, k) for k in range(0, int(um) + 1)) / tot
        return u, min(1.0, p)
    mu, sd = n1 * n2 / 2, math.sqrt(n1 * n2 * (n1 + n2 + 1) / 12)
    z = (u - mu) / sd if sd else 0
    return u, math.erfc(abs(z) / math.sqrt(2))


def load_csv(csv):
    data = defaultdict(lambda: defaultdict(list))  # (scen, ver) -> rep -> [dict]
    lines = read(csv).splitlines()
    head = lines[0].split(',')
    for l in lines[1:]:
        v = l.split(',')
        if len(v) != len(head):
            continue
        d = dict(zip(head, v))
        try:
            data[(d['scen'], d['ver'])][d['rep']].append({k: float(d[k]) for k in PARTS})
        except ValueError:
            continue
    return data


def cmd_fsagg(csv, md=None):
    data = load_csv(csv)
    scens = sorted({s for s, _ in data})
    vers = sorted({v for _, v in data})
    out = []
    out.append("| scenario | version | reps | frames/rep | median of rep medians, ms (min..max) | p90 (median of reps) | p99 | UI thread | RenderThread | layout | draw(record) | sync | issue | swap |")
    out.append("|---|---|---|---|---|---|---|---|---|---|---|---|---|---|")
    permed = {}
    for s in scens:
        for v in vers:
            reps = data.get((s, v))
            if not reps:
                continue
            keys = sorted(reps, key=lambda r: int(re.sub(r'\D', '', r) or 0))
            m = {k: [pct([f[k] for f in reps[r]], 50) for r in keys] for k in PARTS}
            p90 = [pct([f['total'] for f in reps[r]], 90) for r in keys]
            p99 = [pct([f['total'] for f in reps[r]], 99) for r in keys]
            permed[(s, v)] = m
            g = lambda xs: statistics.median(xs) if xs else float('nan')
            out.append(f"| {s} | {v} | {len(keys)} | {g([len(reps[r]) for r in keys]):.0f} | **{g(m['total']):.1f}** ({min(m['total']):.1f}..{max(m['total']):.1f}) | {g(p90):.0f} | {g(p99):.0f} | "
                       f"{g(m['ui']):.1f} | {g(m['rt']):.1f} | {g(m['layout']):.1f} | {g(m['draw']):.1f} | {g(m['sync']):.1f} | {g(m['issue']):.1f} | {g(m['swap']):.1f} |")
    out.append("")
    out.append("Per-repetition medians of the total frame time (ms) and the rank test against the first listed version:")
    out.append("")
    for s in scens:
        ref = None
        for v in vers:
            if (s, v) not in permed:
                continue
            t = permed[(s, v)]['total']
            line = f"- {s} / {v}: {[round(x, 1) for x in t]}"
            if ref is None:
                ref = (v, permed[(s, v)])
            else:
                rv, rm = ref
                u, p = mwu_exact(t, rm['total'])
                sep = 'ranges do NOT overlap' if (min(t) > max(rm['total']) or max(t) < min(rm['total'])) else 'ranges overlap'
                d = statistics.median(t) - statistics.median(rm['total'])
                du = statistics.median(permed[(s, v)]['ui']) - statistics.median(rm['ui'])
                dr = statistics.median(permed[(s, v)]['rt']) - statistics.median(rm['rt'])
                di = statistics.median(permed[(s, v)]['issue']) - statistics.median(rm['issue'])
                line += f" vs {rv}: median diff {d:+.1f} ms (UI {du:+.1f}, RT {dr:+.1f}, issue {di:+.1f}); Mann-Whitney U={u:.1f}, p={p:.3f}; {sep}"
            out.append(line)
    text = '\n'.join(out)
    print(text)
    if md:
        open(md, 'w', encoding='utf-8').write(text + '\n')


# ---------------------------------------------------------------- meminfo
MEM_KEYS = ('Java Heap', 'Native Heap', 'Code', 'Stack', 'Graphics', 'Private Other', 'System', 'TOTAL PSS')
OBJ_KEYS = ('Views', 'ViewRootImpl', 'AppContexts', 'Activities', 'Assets', 'AssetManagers', 'Local Binders', 'Proxy Binders',
            'Parcel memory', 'Death Recipients', 'WebViews')


def cmd_mem(path, ver, cycle, csv):
    t = read(path)
    vals = {}
    for k in MEM_KEYS:
        m = re.search(r'^\s*' + re.escape(k) + r':\s*(\d+)', t, re.M)
        vals[k] = int(m.group(1)) if m else -1
    m = re.search(r'TOTAL RSS:\s*(\d+)', t)
    vals['TOTAL RSS'] = int(m.group(1)) if m else -1
    for k in OBJ_KEYS:
        m = re.search(r'(?<![A-Za-z])' + re.escape(k) + r':\s*(\d+)', t)
        vals[k] = int(m.group(1)) if m else -1
    for name in ('Dalvik Heap', 'Native Heap'):
        # the table row: Pss Total, Private Dirty, Private Clean, SwapPss, Rss, Heap Size, Heap Alloc, Heap Free
        m = re.search(r'^\s*' + name + r'\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)', t, re.M)
        vals[name + ' alloc'] = int(m.group(7)) if m else -1
    for name in ('GL mtrack', 'EGL mtrack', 'Gfx dev', 'Ashmem', '.art mmap', 'Other mmap', 'Unknown'):
        m = re.search(r'^\s*' + re.escape(name) + r'\s+(\d+)', t, re.M)
        vals[name] = int(m.group(1)) if m else -1
    keys = list(vals)
    head = not os.path.exists(csv)
    with open(csv, 'a', encoding='utf-8') as f:
        if head:
            f.write('ver,cycle,' + ','.join(keys) + '\n')
        f.write(f"{ver},{cycle}," + ','.join(str(vals[k]) for k in keys) + '\n')
    print(f"MEM {ver} c{cycle}: PSS={vals['TOTAL PSS']} Java={vals['Java Heap']} Native={vals['Native Heap']} Graphics={vals['Graphics']} Code={vals['Code']} "
          f"PrivOther={vals['Private Other']} System={vals['System']} | DalvikAlloc={vals['Dalvik Heap alloc']} NativeAlloc={vals['Native Heap alloc']} | "
          f"Views={vals['Views']} ViewRootImpl={vals['ViewRootImpl']} Activities={vals['Activities']} AppContexts={vals['AppContexts']} RSS={vals['TOTAL RSS']} (KB)")


def slope(xs, ys):
    n = len(xs)
    if n < 2:
        return float('nan')
    mx, my = sum(xs) / n, sum(ys) / n
    den = sum((x - mx) ** 2 for x in xs)
    return sum((x - mx) * (y - my) for x, y in zip(xs, ys)) / den if den else float('nan')


def cmd_memagg(csv):
    lines = read(csv).splitlines()
    head = lines[0].split(',')
    rows = defaultdict(list)
    for l in lines[1:]:
        v = l.split(',')
        if len(v) != len(head):
            continue
        d = dict(zip(head, v))
        rows[d['ver']].append(d)
    show = ('TOTAL PSS', 'Java Heap', 'Native Heap', 'Graphics', 'Code', 'Private Other', 'System', 'Dalvik Heap alloc', 'Native Heap alloc', 'Views', 'ViewRootImpl', 'Activities', 'AppContexts')
    for ver in rows:
        rs = sorted(rows[ver], key=lambda d: int(d['cycle']))
        print(f"\n{ver}: after trim + GC, KB (cycle: " + ' '.join(d['cycle'] for d in rs) + ")")
        print("| bucket | " + ' | '.join('c' + d['cycle'] for d in rs) + " | c0→last | last third slope, KB/cycle | whole slope |")
        print("|---|" + '---|' * (len(rs) + 3))
        for k in show:
            ys = [int(d.get(k, -1)) for d in rs]
            xs = [int(d['cycle']) for d in rs]
            t = max(2, len(xs) // 3 + 1)
            print(f"| {k} | " + ' | '.join(str(y) for y in ys) + f" | {ys[-1] - ys[0]:+d} | {slope(xs[-t:], ys[-t:]):+.1f} | {slope(xs, ys):+.1f} |")


# ---------------------------------------------------------------- confetti in recorded frames
def cmd_confetti(d, *box):
    import colorsys
    from PIL import Image
    fs = sorted(f for f in os.listdir(d) if f.lower().endswith(('.jpg', '.png')))
    if not fs:
        print('confetti: no frames')
        return
    box = [b for b in box if b != '']
    bx = [float(x) for x in box] if len(box) == 4 else None
    W, H = 216, 480
    ims = [Image.open(os.path.join(d, f)).convert('RGB').resize((W, H)) for f in fs]
    base = ims[0].load()
    per = []
    where = []
    for im in ims:
        q = im.load()
        n = 0
        ys = []
        for y in range(int(H * 0.04), H):
            for x in range(W):
                if bx and bx[0] * W <= x <= bx[2] * W and bx[1] * H <= y <= bx[3] * H:
                    continue
                r, g, b = q[x, y]
                r0, g0, b0 = base[x, y]
                if abs(r - r0) + abs(g - g0) + abs(b - b0) < 70:
                    continue
                _, s, v = colorsys.rgb_to_hsv(r / 255, g / 255, b / 255)
                if s > 0.35 and v > 0.3:
                    n += 1
                    ys.append(y)
        per.append(n)
        where.append(f"{min(ys) * 100 // H}-{max(ys) * 100 // H}%" if ys else '-')
    hot = [i for i, n in enumerate(per) if n >= 6]
    # the frame with the largest excess over the level of the last frames (what stays changed after the animation)
    rest = statistics.median(per[-5:]) if len(per) >= 5 else 0
    peak = max(range(len(per)), key=lambda i: per[i] - rest)
    try:
        open(os.path.join(d, 'peak.txt'), 'w').write(str(peak + 1 if per[peak] - rest >= 6 else 0))
    except OSError:
        pass
    print(f"confetti-like new saturated points outside the band, per frame (level at the end {rest:.0f}; peak #{peak + 1} = {per[peak]}; frames with >=6: {len(hot)}, first #{hot[0] + 1 if hot else 0}, last #{hot[-1] + 1 if hot else 0}): {per}")
    print("vertical span of those points: " + ' '.join(where[i] for i in hot[:12]))


def cmd_motion(d):
    """Mean absolute difference between consecutive frames (0..255) - when something moves on the screen."""
    from PIL import Image, ImageChops, ImageStat
    fs = sorted(f for f in os.listdir(d) if f.lower().endswith(('.jpg', '.png')))
    prev, diffs = None, []
    for f in fs:
        im = Image.open(os.path.join(d, f)).convert('L').resize((216, 480))
        if prev is not None:
            diffs.append(ImageStat.Stat(ImageChops.difference(im, prev)).mean[0])
        prev = im
    mv = [i + 2 for i, x in enumerate(diffs) if x > 0.35]
    print(f"frames {len(fs)}; frames that differ from the previous one (>0.35): {len(mv)}, first #{mv[0] if mv else 0}, last #{mv[-1] if mv else 0}; diffs: " + ' '.join(f"{x:.1f}" for x in diffs))


# ---------------------------------------------------------------- uiautomator
def bounds(n):
    m = re.match(r'\[(-?\d+),(-?\d+)\]\[(-?\d+),(-?\d+)\]', n.get('bounds', ''))
    return tuple(map(int, m.groups())) if m else None


def parse(path):
    try:
        return ET.parse(path).getroot()
    except Exception:  # noqa: BLE001
        return None


def cmd_panes(path, W):
    root = parse(path)
    if root is None:
        print('panes: no dump')
        return
    W = int(W)
    left, right, edges = [], [], []
    for n in root.iter('node'):
        t = (n.get('text') or n.get('content-desc') or '').strip()
        b = bounds(n)
        if not t or not b or b[2] <= b[0]:
            continue
        if b[3] > 0 and (b[2] - b[0]) < W * 0.9:
            edges.append(b[0]); edges.append(b[2])
        (left if (b[0] + b[2]) / 2 < W * 0.42 else right).append(t[:40])
    # the vertical line that most node edges share between 25% and 60% of the width = the border of the panes
    cand = defaultdict(int)
    for e in edges:
        if W * 0.2 < e < W * 0.65:
            cand[e // 8 * 8] += 1
    border = max(cand, key=cand.get) if cand else 0
    print(f"PANES: left {len(left)} texts, right {len(right)} texts; likely pane border x={border} ({border * 100 // W}% of {W})")
    print("  LEFT: " + ' | '.join(left[:22]))
    print("  RIGHT: " + ' | '.join(right[:22]))


def cmd_loginbtn(path, W, H):
    root = parse(path)
    if root is None:
        return
    W, H = int(W), int(H)
    parent = {c: p for p in root.iter() for c in p}
    best = None
    for n in root.iter('node'):
        if (n.get('text') or '').strip() not in ('Вход', 'Sign in'):
            continue
        m = n
        while m is not None and m.get('clickable') != 'true':
            m = parent.get(m)
        if m is None:
            continue
        b = bounds(m)
        if not b or (b[2] - b[0]) < W * 0.45 or b[1] < H * 0.14 or b[3] > H * 0.97:
            continue
        best = (b, m.get('enabled'), m.get('class'))
    if best:
        b = best[0]
        print(f"{(b[0] + b[2]) // 2} {(b[1] + b[3]) // 2} enabled={best[1]} class={best[2]} bounds={b}")


def cmd_w25log(logcat, mark):
    lines = read(logcat).splitlines()
    t0 = None
    out = []

    def ts(l):
        m = re.match(r'^\d\d-\d\d (\d\d):(\d\d):(\d\d)\.(\d\d\d)', l)
        return int(m.group(1)) * 3600000 + int(m.group(2)) * 60000 + int(m.group(3)) * 1000 + int(m.group(4)) if m else None
    for l in lines:
        if t0 is None:
            if re.search(r'PERFCHECK\s*:\s*' + re.escape(mark) + r'\s*$', l):
                t0 = ts(l)
            continue
        if 'PERFCHECK' in l:
            break
        if re.search(r'\bW25\s*:', l) or 'Davey!' in l or re.search(r'Skipped \d+ frames', l):
            t = ts(l)
            msg = re.sub(r'^\S+ \S+\s+\d+\s+\d+ \w ', '', l)
            if 'Davey!' in msg:
                msg = re.sub(r';.*', '', msg)
            out.append(f"+{(t - t0) if t is not None and t0 is not None else -1:>6} ms  {msg[:230]}")
    print('\n'.join(out) if out else f"(no W25 lines after the marker {mark})")


def cmd_slidedesc(path):
    root = parse(path)
    if root is None:
        print('(dump failed)')
        return
    best = ''
    for n in root.iter('node'):
        d = (n.get('content-desc') or '').strip()
        if d in ('Затвори', 'Сподели', 'Следващ слайд', 'Предишен слайд', 'Close', 'Share') or len(d) < 12:
            continue
        if len(d) > len(best):
            best = d
    pos = ''
    for n in root.iter('node'):
        for a in ('text', 'content-desc', 'stateDescription'):
            v = n.get(a) or ''
            m = re.search(r'(Слайд|Slide) (\d+) (от|of) (\d+)', v)
            if m:
                pos = m.group(0)
    print((pos + ': ' if pos else '') + (best or '(no slide description)'))


def cmd_clipped(path, W, H):
    """Texts cut by the right/bottom edge of the screen or of their parent, and clickable nodes that overlap."""
    root = parse(path)
    if root is None:
        print('clipped: no dump')
        return
    W, H = int(W), int(H)
    parent = {c: p for p in root.iter() for c in p}
    cut, tiny = [], []
    for n in root.iter('node'):
        t = (n.get('text') or '').strip()
        b = bounds(n)
        if not t or not b:
            continue
        w, h = b[2] - b[0], b[3] - b[1]
        if w <= 0 or h <= 0:
            continue
        p = parent.get(n)
        pb = bounds(p) if p is not None and p.tag == 'node' else None
        if b[2] >= W - 1 and w > 40:
            cut.append(f"'{t[:24]}' touches the right edge {b}")
        elif pb and (b[2] > pb[2] + 1 or b[0] < pb[0] - 1):
            cut.append(f"'{t[:24]}' wider than its parent {b} in {pb}")
        if h < 24 and len(t) > 2:
            tiny.append(f"'{t[:24]}' only {h}px high")
    cl = [(bounds(n), (n.get('text') or n.get('content-desc') or '')[:20]) for n in root.iter('node') if n.get('clickable') == 'true' and bounds(n)]
    over = []
    for i in range(len(cl)):
        for j in range(i + 1, len(cl)):
            a, b = cl[i][0], cl[j][0]
            ix, iy = min(a[2], b[2]) - max(a[0], b[0]), min(a[3], b[3]) - max(a[1], b[1])
            if ix > 8 and iy > 8:
                aa, ab = (a[2] - a[0]) * (a[3] - a[1]), (b[2] - b[0]) * (b[3] - b[1])
                if ix * iy < 0.9 * min(aa, ab):  # not simply nested
                    over.append(f"{cl[i][1] or a} x {cl[j][1] or b} ({ix}x{iy}px)")
    print(f"CLIP: cut {len(cut)}, squeezed {len(tiny)}, overlapping clickables {len(over)}" + (': ' + '; '.join((cut + tiny + over)[:8]) if cut or tiny or over else ''))


if __name__ == '__main__':
    c, *a = sys.argv[1:]
    {'fs': cmd_fs, 'fsagg': cmd_fsagg, 'mem': cmd_mem, 'memagg': cmd_memagg, 'confetti': cmd_confetti, 'panes': cmd_panes,
     'loginbtn': cmd_loginbtn, 'motion': cmd_motion, 'w25log': cmd_w25log, 'slidedesc': cmd_slidedesc, 'clipped': cmd_clipped}[c](*a)
