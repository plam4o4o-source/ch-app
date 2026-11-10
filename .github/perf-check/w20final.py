"""Помощни разчитания за крайната проверка на v1.2.34 (ВРЕМЕННО, debug/wave20-final).

  w20final.py gfxagg SUMMARY                 — GFX редовете по сценарий: новото срещу 1.2.33 (средно от кръговете)
  w20final.py shots TIMES PNG...             — кадрите след докосването: тъмен екран/слайд/зареждане и кога
  w20final.py strict LOGCAT PKG              — StrictMode / CloseGuard на процесите на пакета: видове и стекове
  w20final.py nodes TREE REGEX               — възлите от дървото на тестовата услуга, чийто текст/описание пасва
  w20final.py topbar TREE YMAX DENS          — фокусируемите/кликаемите възли в горните YMAX px и тези с празни граници
  w20final.py spines TREE DENS               — кликаемите възли на полицата: брой, повтарящи се етикети, най-малкият размер (dp)
  w20final.py checkstate UI LABEL            — превключвателят на реда с този текст (checked) в uiautomator dump
  w20final.py ys UI                          — какво от историята се вижда (слайд, грешка, зареждане) + скролируемо, отрязано
"""
import os, re, sys
from collections import Counter, defaultdict


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


def cmd_gfxagg(summary):
    rx = re.compile(r'^GFX (\S+)\s+frames=\s*(\d+)\s+janky=(\d+) \(([\d.]+)%\)\s+p50=(\d+)ms p90=(\d+)ms p95=(\d+)ms p99=(\d+)ms')
    data = defaultdict(lambda: defaultdict(list))
    for l in read(summary).splitlines():
        m = rx.match(l)
        if not m:
            continue
        lab = m.group(1)
        mm = re.match(r'(new|base)(\d+)-(.+)$', lab)
        if not mm:
            continue
        app, scen = mm.group(1), mm.group(3)
        data[scen][app].append(tuple(float(x) for x in m.groups()[1:]))
    print(f"{'scenario':<18}{'app':<6}{'n':>3}{'frames':>9}{'janky%':>8}{'p50':>6}{'p90':>6}{'p99':>6}")
    for scen in sorted(data):
        for app in ('new', 'base'):
            rs = data[scen].get(app, [])
            if not rs:
                print(f"{scen:<18}{app:<6}{0:>3}")
                continue
            av = [sum(r[i] for r in rs) / len(rs) for i in range(7)]
            print(f"{scen:<18}{app:<6}{len(rs):>3}{av[0]:>9.0f}{av[2]:>8.1f}{av[3]:>6.0f}{av[4]:>6.0f}{av[6]:>6.0f}")


def classify(p):
    from PIL import Image, ImageStat
    im = Image.open(p).convert('L')
    st = ImageStat.Stat(im)
    mean, std = st.mean[0], st.stddev[0]
    if mean < 90 and std >= 18:
        k = 'SLIDE'
    elif mean < 90:
        k = 'dark-blank/loader'
    else:
        k = 'other(light)'
    return k, mean, std


def cmd_shots(times, *ps):
    """TIMES: файл с „tap U“, „tapdone U“ и „i U“ (uptime в s, след всяка снимка)."""
    t = {}
    for l in read(times).split('\n'):
        f = l.split()
        if len(f) == 2:
            t[f[0]] = float(f[1])
    t0 = t.get('tapdone', t.get('tap'))
    prev = t0
    first = None
    out = []
    for p in sorted(ps, key=lambda s: int(re.search(r'(\d+)\.png$', s).group(1))):
        i = re.search(r'(\d+)\.png$', p).group(1)
        k, mean, std = classify(p)
        te = t.get(i)
        lo = (prev - t0) * 1000 if prev is not None and t0 is not None else -1
        hi = (te - t0) * 1000 if te is not None and t0 is not None else -1
        out.append(f"#{i} [{lo:.0f}..{hi:.0f} ms] {k} mean={mean:.0f} std={std:.0f}")
        if first is None and k == 'SLIDE':
            first = (lo, hi, i)
        prev = te
    print(' | '.join(out))
    if first:
        print(f"FIRST SLIDE in shot #{first[2]}: taken {first[0]:.0f}..{first[1]:.0f} ms after the tap returned "
              f"(input tap itself took {(t0 - t.get('tap', t0)) * 1000:.0f} ms)")
    else:
        print('FIRST SLIDE: not seen in the shots')


TS = re.compile(r'^(\d\d-\d\d \d\d:\d\d:\d\d\.\d+)\s+(\d+)\s+(\d+)\s+([VDIWEF])\s+(\S[^:]*?)\s*: ?(.*)$')


def cmd_strict(logcat, pkg):
    lines = read(logcat).splitlines()
    pids = set()
    for l in lines:
        m = re.search(r'Start proc (\d+):' + re.escape(pkg) + r'(/|\s)', l)
        if m:
            pids.add(m.group(1))
        m = TS.match(l)
        if m and pkg in l and m.group(5) in ('ActivityManager', 'ActivityTaskManager'):
            mm = re.search(r'pid[= ](\d+)', l)
            if mm and 'proc' in l.lower():
                pass
    groups = defaultdict(list)
    order = []
    for l in lines:
        m = TS.match(l)
        if not m:
            continue
        ts, pid, tid, lvl, tag, msg = m.groups()
        if pid not in pids:
            continue
        if tag in ('StrictMode',) or (tag == 'System' and 'resource failed' in msg) or 'CloseGuard' in tag:
            k = (pid, tid, tag, ts)
            if k not in groups:
                order.append(k)
            groups[k].append(msg)
    print(f"STRICT pids of {pkg}: {sorted(pids, key=int)}; StrictMode/CloseGuard messages: {len(order)}")
    sig = Counter()
    ex = {}
    for k in order:
        body = groups[k]
        head = body[0]
        kind = re.search(r'(android\.os\.strictmode\.\w+|A resource [^.]*\.|Leaked[^ :]*)', ' '.join(body[:2]))
        kind = kind.group(1) if kind else head[:80]
        app = [b.strip() for b in body if 'org.chyavorec' in b]
        frames = [b.strip() for b in body if b.strip().startswith('at ')]
        where = app[0] if app else (frames[0] if frames else '-')
        # Първият кадър извън android/java/kotlin — най-често мястото на отварянето
        lib = [f for f in frames if not re.match(r'at (android\.|java\.|javax\.|jdk\.|dalvik\.|libcore\.|com\.android\.internal|sun\.)', f)]
        s = f"{kind} @ {where[:150]}" + ('' if app else f" (first non-platform: {(lib[0] if lib else '-')[:150]})")
        sig[s] += 1
        ex.setdefault(s, (k, body))
    for s, n in sig.most_common(12):
        print(f"  {n:4d}x {s}")
    print('STACKS (one example per kind):')
    for s, n in sig.most_common(8):
        k, body = ex[s]
        print(f"--- {n}x {k[3]} pid {k[0]} tid {k[1]} {k[2]}")
        for b in body[:45]:
            print('    ' + b.rstrip()[:200])


def tree_rows(f):
    rows = []
    for line in open(f, encoding='utf-8', errors='replace'):
        m = re.search(r'PCDUMP\s*:\s*\S+ (W\d+) d(\d+) (.*)$', line)
        if not m:
            continue
        kv = dict(re.findall(r'(\w+)=(.*?)(?= \w+=|$)', m.group(3)))
        kv['w'], kv['d'] = m.group(1), int(m.group(2))
        bb = re.match(r'\[?(-?\d+),(-?\d+)\]?\[(-?\d+),(-?\d+)\]', kv.get('b', '').replace(' - ', '][').replace('Rect(', '').replace(')', ''))
        if not bb:
            bb = re.findall(r'-?\d+', kv.get('b', ''))
            bb = [int(x) for x in bb[:4]] if len(bb) >= 4 else None
        else:
            bb = [int(x) for x in bb.groups()]
        kv['bb'] = bb
        rows.append(kv)
    return rows


def lab(r):
    return (r.get('text', '') + ' ' + r.get('desc', '')).strip()


def cmd_nodes(f, rx):
    n = 0
    for r in tree_rows(f):
        if re.search(rx, lab(r)) or re.search(rx, r.get('pane', '')):
            n += 1
            if n <= 8:
                print(f"NODE '{lab(r)[:90]}' cls={r.get('cls', '').split('.')[-1]} clk={r.get('clk')} focusable={r.get('focusable')} "
                      f"checkable={r.get('checkable')} checked={r.get('checked')} pane={r.get('pane')} b={r.get('b')}")
    print(f"NODES matching /{rx}/: {n}")


def cmd_topbar(f, ymax, dens):
    ymax, d = int(ymax), float(dens) / 160
    rows = tree_rows(f)
    app = [r for r in rows if r['w'] != 'W0']
    top, empty = [], []
    for r in app:
        bb = r['bb']
        if not bb:
            continue
        foc = r.get('focusable') == 'true' or r.get('clk') == 'true'
        if not foc:
            continue
        if bb[3] - bb[1] <= 1 or bb[2] - bb[0] <= 1:
            empty.append(r)
        elif bb[1] < ymax:
            top.append(r)
    print(f"TOPBAR focusable/clickable nodes with top < {ymax}px: {len(top)}: " + ' | '.join(f"'{lab(r)[:40]}' b={r.get('b')}" for r in top[:10]))
    print(f"TOPBAR focusable/clickable nodes with empty bounds (invisible but in the tree): {len(empty)}: " + ' | '.join(f"'{lab(r)[:40]}' b={r.get('b')}" for r in empty[:10]))


def cmd_spines(f, dens):
    d = float(dens) / 160
    rows = [r for r in tree_rows(f) if r['w'] != 'W0' and r.get('clk') == 'true' and r['bb']]
    # Гръбчетата: тесни високи кликаеми възли (по-високи, отколкото широки)
    sp = [r for r in rows if (r['bb'][3] - r['bb'][1]) > 1.3 * (r['bb'][2] - r['bb'][0])]
    labs = Counter(lab(r) for r in sp)
    dup = [(k, v) for k, v in labs.items() if v > 1]
    if sp:
        mw = min((r['bb'][2] - r['bb'][0]) / d for r in sp)
        mh = min((r['bb'][3] - r['bb'][1]) / d for r in sp)
    else:
        mw = mh = 0
    print(f"SPINES clickable tall nodes: {len(sp)}; min width {mw:.0f} dp, min height {mh:.0f} dp; "
          f"<48dp wide: {sum(1 for r in sp if (r['bb'][2] - r['bb'][0]) / d < 47.5)}; unlabelled: {sum(1 for r in sp if not lab(r))}; "
          f"duplicate labels: {len(dup)} {[(k[:50], v) for k, v in dup[:4]]}")
    print('SPINES examples: ' + ' | '.join(f"'{lab(r)[:60]}' {r.get('b')}" for r in sp[:5]))


def ui_nodes(ui):
    import xml.etree.ElementTree as ET
    try:
        root = ET.parse(ui).getroot()
    except Exception:
        return None
    return root


def bnds(n):
    m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
    return [int(x) for x in m.groups()] if m else None


def cmd_checkstate(ui, label):
    root = ui_nodes(ui)
    if root is None:
        print('CHECKSTATE: no dump')
        return
    parent = {c: p for p in root.iter() for c in p}
    hit = None
    for n in root.iter('node'):
        if label in (n.get('text') or '') or label in (n.get('content-desc') or ''):
            hit = n
            break
    if hit is None:
        print(f"CHECKSTATE '{label}': label not found")
        return
    cur = hit
    for _ in range(6):
        if cur.get('checkable') == 'true':
            print(f"CHECKSTATE '{label}': checked={cur.get('checked')} (node itself/ancestor, clickable={cur.get('clickable')})")
            return
        for c in cur.iter('node'):
            if c.get('checkable') == 'true':
                print(f"CHECKSTATE '{label}': checked={c.get('checked')} (descendant {c.get('class')})")
                return
        cur = parent.get(cur)
        if cur is None:
            break
    # Съседен превключвател на същия ред
    hb = bnds(hit)
    for n in root.iter('node'):
        b = bnds(n)
        if n.get('checkable') == 'true' and b and hb and b[1] <= (hb[1] + hb[3]) / 2 <= b[3] + 40:
            print(f"CHECKSTATE '{label}': checked={n.get('checked')} (switch on the same row {n.get('bounds')})")
            return
    print(f"CHECKSTATE '{label}': no checkable node near the label")


def cmd_ys(ui, h='2400'):
    root = ui_nodes(ui)
    if root is None:
        print('YS: no dump')
        return
    h = int(h)
    texts, scroll, clipped = [], 0, []
    for n in root.iter('node'):
        t = (n.get('text') or n.get('content-desc') or '').strip()
        b = bnds(n)
        if n.get('scrollable') == 'true':
            scroll += 1
        if t:
            texts.append(t)
            if b and (b[3] > h or b[1] < 0):
                clipped.append(f"{t[:30]} {n.get('bounds')}")
    ell = [t for t in texts if t.endswith('…')]
    state = re.search(r'Слайд \d+ от \d+', ' '.join(texts))
    print(f"YS state={'%s' % state.group(0) if state else '-'} error={'YES' if any(re.search('Няма връзка|не се зареди|Опитай отново', t) for t in texts) else 'no'} "
          f"scrollable={scroll} ellipsized={len(ell)} beyond-screen={len(clipped)} {clipped[:3]} | " + ' · '.join(t[:40] for t in texts[:8]))


if __name__ == '__main__':
    c, args = sys.argv[1], sys.argv[2:]
    try:
        globals()['cmd_' + c](*args)
    except Exception as e:  # noqa: BLE001
        print(f"w20final.py {c}: error {type(e).__name__}: {e}")
