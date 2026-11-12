"""Разчитания за проверката на шейдърите в 1.2.35 (ВРЕМЕННО, debug/w21-shader).

  w21.py fs FILE LABEL      — `dumpsys gfxinfo <pkg> framestats`: кадрите (IntendedVsync → FrameCompleted, ms),
                              най-бавният (с UI/RenderThread частите и флага) и първите 5
  w21.py fsagg SUMMARY      — от редовете „FS …“: по издание и вид на отварянето — най-бавните кадри, първите 5
  w21.py ab NEW BASE        — студеният старт: TotalTime и „Fully drawn“ на двете издания (медиани, разлика,
                              Mann–Whitney U с нормално приближение)
"""
import math, re, statistics, sys
from collections import defaultdict


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


def frames(path):
    out = []
    for block in re.findall(r'---PROFILEDATA---\n(.*?)---PROFILEDATA---', read(path), re.S):
        lines = [l.strip().rstrip(',') for l in block.strip().splitlines() if l.strip()]
        if not lines or not lines[0].startswith('Flags'):
            continue
        head = lines[0].split(',')
        ix = {k: i for i, k in enumerate(head)}
        for l in lines[1:]:
            v = l.split(',')
            try:
                g = lambda k: int(v[ix[k]]) if k in ix and ix[k] < len(v) else 0
                iv, done = g('IntendedVsync'), g('FrameCompleted')
                if iv <= 0 or done <= 0 or done < iv:
                    continue
                out.append({
                    'iv': iv, 'flags': g('Flags'),
                    'total': (done - iv) / 1e6,
                    'ui': (g('SyncQueued') - iv) / 1e6 if g('SyncQueued') else float('nan'),
                    'rt': (done - g('SyncStart')) / 1e6 if g('SyncStart') else float('nan'),
                    'issue': (g('SwapBuffers') - g('IssueDrawCommandsStart')) / 1e6 if g('SwapBuffers') and g('IssueDrawCommandsStart') else float('nan'),
                    'draw': (g('SyncQueued') - g('DrawStart')) / 1e6 if g('SyncQueued') and g('DrawStart') else float('nan'),
                })
            except (ValueError, IndexError):
                continue
    out.sort(key=lambda f: f['iv'])
    return out


def cmd_fs(path, label):
    fr = frames(path)
    if not fr:
        print(f"FS {label}: no frames")
        return
    s = max(fr, key=lambda f: f['total'])
    i = fr.index(s) + 1
    first = ', '.join(f"{f['total']:.0f}" for f in fr[:5])
    span = (fr[-1]['iv'] - fr[0]['iv']) / 1e6
    over = lambda ms: sum(1 for f in fr if f['total'] > ms)
    full = ' (120 = buffer full, the earliest frames may be lost)' if len(fr) >= 120 else ''
    print(f"FS {label}: n={len(fr)}{full} slowest={s['total']:.1f}ms (frame #{i}; ui {s['ui']:.1f} rt {s['rt']:.1f} issue {s['issue']:.1f} flags {s['flags']})"
          f" first5=[{first}] >32ms={over(32)} >64ms={over(64)} >100ms={over(100)} span={span:.0f}ms")


def cmd_fsagg(path):
    rx = re.compile(r'^FS (new|base)-(\d+)-(.+?)-(cardscreen|full): n=(\d+).*?slowest=([\d.]+)ms \(frame #(\d+); ui ([\d.na]+) rt ([\d.na]+) issue ([\d.na]+).*?first5=\[([^\]]*)\]')
    groups = defaultdict(list)
    for l in read(path).splitlines():
        m = rx.match(l)
        if not m:
            continue
        k, blk, kind, scr, n, slow, idx, ui, rt, iss, first = m.groups()
        mode = 'wait5s' if re.match(r'\d+-wait$', kind) else kind
        f5 = [float(x) for x in first.split(',') if x.strip()]
        groups[(scr, mode, k)].append((float(slow), int(idx), f5, f"{k}-{blk}-{kind}", float(rt) if rt != 'nan' else float('nan')))
    if not groups:
        print('FSAGG: no FS lines')
        return
    for (scr, mode, k) in sorted(groups, key=lambda t: (t[0] != 'full', t[1], t[2] != 'new')):
        g = groups[(scr, mode, k)]
        sl = sorted(x[0] for x in g)
        ff = [x[2][0] for x in g if x[2]]
        f25 = [v for x in g for v in x[2][1:5]]
        print(f"FSAGG {scr:<10} {mode:<11} {k:<4} opens={len(g):>2}  slowest frame: median {statistics.median(sl):6.1f} ms, mean {statistics.mean(sl):6.1f}, "
              f"min {sl[0]:6.1f}, max {sl[-1]:6.1f}  | frame 1 median {statistics.median(ff) if ff else float('nan'):6.1f} ms | frames 2-5 median {statistics.median(f25) if f25 else float('nan'):5.1f} ms"
              f"  | slowest per open: {[round(x[0]) for x in g]} at frame # {[x[1] for x in g]}")
    print("FSAGG first-5 frames per open (ms):")
    for (scr, mode, k) in sorted(groups, key=lambda t: (t[0] != 'full', t[1], t[2] != 'new')):
        if scr != 'full':
            continue
        for x in groups[(scr, mode, k)]:
            print(f"   {scr} {x[3]:<22} first5={[round(v) for v in x[2]]} slowest={x[0]:.0f} (#{x[1]}, rt {x[4]:.0f})")


def mwu(a, b):
    """Mann–Whitney U (двустранно, нормално приближение с поправка за равни стойности)."""
    allv = sorted((v, 0) for v in a) + sorted((v, 1) for v in b)
    allv.sort(key=lambda t: t[0])
    ranks = [0.0] * len(allv)
    i = 0
    while i < len(allv):
        j = i
        while j + 1 < len(allv) and allv[j + 1][0] == allv[i][0]:
            j += 1
        r = (i + j) / 2 + 1
        for k in range(i, j + 1):
            ranks[k] = r
        i = j + 1
    ra = sum(r for r, (_, g) in zip(ranks, allv) if g == 0)
    n1, n2 = len(a), len(b)
    u = ra - n1 * (n1 + 1) / 2
    mu, sd = n1 * n2 / 2, math.sqrt(n1 * n2 * (n1 + n2 + 1) / 12)
    z = (u - mu) / sd if sd else 0
    p = math.erfc(abs(z) / math.sqrt(2))
    return u, p


def parse_drawn(p):
    tt, fd = [], []
    for l in read(p).splitlines():
        m = re.search(r'TotalTime:\s*(\d+)', l)
        if m:
            tt.append(int(m.group(1)))
        m = re.search(r'Fully drawn \S+ \+(?:(\d+)s)?(\d+)ms', l)
        if m:
            fd.append(int(m.group(1) or 0) * 1000 + int(m.group(2)))
    return tt, fd


def cmd_ab(new, base):
    tn, fn = parse_drawn(new)
    tb, fb = parse_drawn(base)
    for name, a, b in (('TotalTime', tn, tb), ('Fully drawn', fn, fb)):
        if not a or not b:
            print(f"AB {name}: no data (new n={len(a)}, base n={len(b)})")
            continue
        ma, mb = statistics.median(a), statistics.median(b)
        u, p = mwu(a, b)
        print(f"AB {name:<11}: 1.2.35 n={len(a)} median {ma:.0f} ms (mean {statistics.mean(a):.0f}) | 1.2.34 n={len(b)} median {mb:.0f} ms (mean {statistics.mean(b):.0f})"
              f" | diff of medians {ma - mb:+.0f} ms ({(ma - mb) / mb * 100:+.1f}%) | Mann-Whitney p={p:.2f}")


if __name__ == '__main__':
    c = sys.argv[1]
    {'fs': cmd_fs, 'fsagg': cmd_fsagg, 'ab': cmd_ab}[c](*sys.argv[2:])
