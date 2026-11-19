"""TEMPORARY (debug/w27-cat): which work on the main thread makes the slow frames of the catalogue tab.

  w27.py fs BEFORE AFTER NAME CSV JSON     framestats of one interaction (frames new since BEFORE): counts,
                                           frames over 32/80 ms (UI thread and total), every frame over 40 ms
  w27.py tp TRACE PKG NAME JSON MAPPING CSV   Perfetto trace (TraceProcessor): main-thread doFrame slices, the
                                           slow frames as a slice tree with thread states, long non-frame work
  w27.py at TRACE PID NAME JSON MAPPING CSV   the same from a text atrace (API 26: no Perfetto)
  w27.py agg CSV                           one table over all interactions
  w27.py warm                              downloads trace_processor_shell (once per job)
Exit code 3 from tp/at: the trace has a main-thread slice over 32 ms (worth keeping).
"""
import csv as csvmod
import json
import math
import os
import re
import sys
from collections import defaultdict

LONG = 40.0  # ms: a frame/slice printed in detail


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


# ---------------------------------------------------------------- framestats (as w25.py)
COLS = ('Flags', 'IntendedVsync', 'Vsync', 'HandleInputStart', 'AnimationStart', 'PerformTraversalsStart', 'DrawStart',
        'SyncQueued', 'SyncStart', 'IssueDrawCommandsStart', 'SwapBuffers', 'FrameCompleted', 'GpuCompleted')


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
            key = r['IntendedVsync']
            # two windows (sheet/popup + activity) on one vsync: keep the slower
            if key in out and out[key]['FrameCompleted'] >= r['FrameCompleted']:
                continue
            out[key] = r
    return out


def ms(a, b):
    return (b - a) / 1e6 if a and b and b >= a else float('nan')


def split(r):
    return {
        'total': ms(r['IntendedVsync'], r['FrameCompleted']),
        'delay': ms(r['IntendedVsync'], r['HandleInputStart']),
        'input': ms(r['HandleInputStart'], r['AnimationStart']),
        'anim': ms(r['AnimationStart'], r['PerformTraversalsStart']),
        'layout': ms(r['PerformTraversalsStart'], r['DrawStart']),
        'draw': ms(r['DrawStart'], r['SyncQueued']),
        'sync': ms(r['SyncStart'], r['IssueDrawCommandsStart']),
        'rt': ms(r['SyncStart'], r['FrameCompleted']),
        # as JankStats frameDurationUiNanos (UNKNOWN_DELAY + input + animation + layout + draw + SYNC):
        # vsync → the RenderThread starts issuing draw commands (the UI thread waits for the sync)
        'ui': ms(r['IntendedVsync'], r['IssueDrawCommandsStart'] or r['SyncQueued']),
    }


def nz(x):
    return 0.0 if x != x else x


def cmd_fs(before, after, name, csv, js):
    old = fs_rows(before)
    new = fs_rows(after)
    rows = [new[k] for k in sorted(new) if k not in old]
    t0 = rows[0]['IntendedVsync'] if rows else 0
    sp = [(r, split(r)) for r in rows]
    ui = [nz(s['ui']) for _, s in sp]
    tot = [nz(s['total']) for _, s in sp]
    n = len(sp)
    line = (f"FS {name}: frames={n}{' BUFFER-FULL(120)' if len(new) >= 119 else ''} | UI thread as JankStats (vsync→sync done): >32ms {sum(u > 32 for u in ui)}, "
            f">80ms {sum(u > 80 for u in ui)}, max {max(ui) if ui else 0:.0f} | total: >32ms {sum(t > 32 for t in tot)}, "
            f">80ms {sum(t > 80 for t in tot)}, max {max(tot) if tot else 0:.0f}")
    print(line)
    long = []
    for r, s in sp:
        if nz(s['ui']) > LONG or nz(s['total']) > LONG:
            long.append({'vsync': r['IntendedVsync'], 'done': r['FrameCompleted'], 'syncq': r['SyncQueued'], 'flags': r['Flags'],
                         **{k: round(nz(v), 1) for k, v in s.items()}})
    long.sort(key=lambda f: -f['ui'])
    for f in long[:8]:
        print(f"  FRAME ui={f['ui']:.0f} total={f['total']:.0f} ms at +{(f['vsync'] - t0) / 1e6:.0f} ms flags={f['flags']} "
              f"[delay {f['delay']:.1f} input {f['input']:.1f} anim {f['anim']:.1f} layout {f['layout']:.1f} draw {f['draw']:.1f} | rt {f['rt']:.1f}]")
    json.dump(long, open(js, 'w'))
    head = not os.path.exists(csv)
    with open(csv, 'a', encoding='utf-8') as f:
        if head:
            f.write('name,frames,ui32,ui80,uimax,tot32,tot80,totmax\n')
        f.write(f"{name},{n},{sum(u > 32 for u in ui)},{sum(u > 80 for u in ui)},{max(ui) if ui else 0:.1f},"
                f"{sum(t > 32 for t in tot)},{sum(t > 80 for t in tot)},{max(tot) if tot else 0:.1f}\n")


# ---------------------------------------------------------------- deobfuscation (R8 mapping)
_MAP = None


def mapping(path):
    global _MAP
    if _MAP is not None:
        return _MAP
    _MAP = {}
    if path and os.path.exists(path):
        rx = re.compile(r'^(\S+) -> (\S+):$')
        for l in open(path, encoding='utf-8', errors='replace'):
            if l[0] in ' #':
                continue
            m = rx.match(l.rstrip('\n'))
            if m and m.group(1) != m.group(2):
                _MAP[m.group(2)] = m.group(1)
    return _MAP


def clean(name, mp):
    name = re.sub(r'\s+', ' ', name or '').strip()
    if mp:
        def sub(m):
            t = m.group(0)
            return mp.get(t, t)
        name = re.sub(r'[A-Za-z_$][\w$]*(?:\.[\w$]+)+', sub, name)
        name = name.replace('org.chyavorec.app.', '').replace('androidx.compose.', 'a.c.').replace('kotlinx.coroutines.', 'kx.')
    return name[:110]


# ---------------------------------------------------------------- slices: one tree over all tracks of the main thread
class S:
    __slots__ = ('ts', 'dur', 'name', 'kids', 'depth', 'parent')

    def __init__(self, ts, dur, name):
        self.ts, self.dur, self.name = ts, dur, name
        self.kids, self.depth, self.parent = [], 0, None

    @property
    def end(self):
        return self.ts + self.dur


def nest(slices):
    out = sorted(slices, key=lambda x: (x.ts, -x.dur))
    stack = []
    for x in out:
        while stack and stack[-1].end <= x.ts:
            stack.pop()
        if stack:
            x.parent = stack[-1]
            x.depth = stack[-1].depth + 1
            stack[-1].kids.append(x)
        stack.append(x)
    return out


def self_ms(x):
    return (x.dur - sum(k.dur for k in x.kids)) / 1e6


def tree_lines(root, mp, limit=34, maxdepth=9):
    thr = max(1.5e6, root.dur * 0.025)
    lines = []

    def walk(x, d):
        if len(lines) >= limit or d > maxdepth:
            return
        if d > 0:
            lines.append(f"{'  ' * (d + 2)}{x.dur / 1e6:7.1f} ms  d{d}  {clean(x.name, mp)}")
        for k in sorted(x.kids, key=lambda k: k.ts):
            if k.dur >= thr:
                walk(k, d + 1)
    walk(root, 0)
    return lines


def self_top(root, mp, n=8):
    agg = defaultdict(float)

    def walk(x):
        agg[clean(x.name, mp)[:60]] += self_ms(x)
        for k in x.kids:
            walk(k)
    for k in root.kids:
        walk(k)
    own = self_ms(root)
    top = sorted(agg.items(), key=lambda kv: -kv[1])[:n]
    return ', '.join(f"{k} {v:.1f}" for k, v in top if v >= 0.5) + f" | in the frame slice itself {own:.1f}"


def is_frame(x):
    return x.name.startswith('Choreographer#doFrame')


def contains_frame(x):
    return is_frame(x) or any(contains_frame(k) for k in x.kids)


def states_in(states, a, b):
    if states is None:
        return ''
    agg = defaultdict(float)
    blocked = defaultdict(float)
    for ts, dur, st, fn in states:
        lo, hi = max(ts, a), min(ts + dur, b)
        if hi > lo:
            agg[st] += (hi - lo) / 1e6
            if fn and st in ('D', 'DK'):
                blocked[fn] += (hi - lo) / 1e6
    s = ' '.join(f"{k} {v:.1f}" for k, v in sorted(agg.items(), key=lambda kv: -kv[1]) if v >= 0.1)
    if blocked:
        s += ' (blocked in ' + ', '.join(f"{k} {v:.1f}" for k, v in blocked.items()) + ')'
    return s


def analyse(name, slices, states, others, render, t0, frames_fs, off, mp, csvp, mode):
    """slices — S of the main thread (all tracks), states — (ts, dur, state, blocked_fn) or None,
    others — {thread: [S]} other threads of the app (GC, JIT, binder…), render — RenderThread DrawFrame durations (ms)."""
    nest(slices)
    frames = [x for x in slices if is_frame(x)]
    fd = [x.dur / 1e6 for x in frames]
    worst = max(frames, key=lambda x: x.dur, default=None)
    tops = [x for x in slices if x.parent is None]
    busy = sum(x.dur for x in tops) / 1e6
    print(f"TR {name} ({mode}): doFrame n={len(frames)}, >32ms {sum(d > 32 for d in fd)}, >80ms {sum(d > 80 for d in fd)}, "
          f"max {max(fd) if fd else 0:.1f} ms; main-thread top-level slices {busy:.0f} ms in total; "
          f"RenderThread DrawFrame max {max(render) if render else 0:.1f} ms (>40ms {sum(r > 40 for r in render)})")
    shown = set()
    keep = any(x.dur > 32e6 for x in tops)

    def frame_block(x, label):
        par = x
        chain = []
        while par.parent is not None:
            par = par.parent
            chain.append(clean(par.name, mp)[:70])
        print(f"  {label} {x.dur / 1e6:.1f} ms at +{(x.ts - t0) / 1e6:.0f} ms | {clean(x.name, mp)[:60]}"
              + (f" | inside: {' < '.join(chain)}" if chain else ''))
        st = states_in(states, x.ts, x.end)
        if st:
            print(f"      main thread states (ms): {st}")
        for l in tree_lines(x, mp):
            print(l)
        print(f"      self time by slice (ms): {self_top(x, mp)}")
        ot = []
        for th, ss in others.items():
            for o in ss:
                lo, hi = max(o.ts, x.ts), min(o.end, x.end)
                if hi - lo > 4e6 and re.search(r'GC|garbage|Lock contention|monitor contention|JIT|Compil|Verif|binder|Inflat|SQLite|sqlite|Room|dex|Class', o.name, re.I):
                    ot.append(f"{th}: {clean(o.name, mp)[:60]} {o.dur / 1e6:.1f}")
        if ot:
            print(f"      other threads meanwhile: {'; '.join(ot[:6])}")

    # 1) the slowest doFrames
    for x in sorted([f for f in frames if f.dur > LONG * 1e6], key=lambda f: -f.dur)[:4]:
        shown.add(id(x))
        frame_block(x, 'DOFRAME')
    # 2) the slow frames of framestats → main-thread work between vsync and the end of the UI part
    if frames_fs:
        for f in sorted(frames_fs, key=lambda f: -f['ui'])[:4]:
            if f['ui'] <= LONG:
                continue
            a, b = f['vsync'] + off, f['syncq'] + off
            ov = [x for x in tops if x.end > a and x.ts < b and x.dur > 2e6]
            df = [x for x in frames if x.end > a and x.ts < b + 5e6]
            desc = ', '.join(f"{clean(x.name, mp)[:70]} {x.dur / 1e6:.1f} (from +{(x.ts - a) / 1e6:.0f})" for x in ov[:6])
            print(f"  FS-FRAME ui={f['ui']:.0f} (delay before doFrame {f['delay']:.1f}) at trace +{(a - t0) / 1e6:.0f} ms: "
                  f"top-level main-thread slices in [vsync, sync]: {desc or 'none'}; doFrame(s) there: "
                  f"{', '.join(f'{d.dur / 1e6:.1f}' for d in df) or 'none'}")
            st = states_in(states, a, b)
            if st:
                print(f"      main thread states in [vsync, sync] (ms): {st}")
            for x in ov:
                if id(x) not in shown and x.dur > 8e6 and not contains_frame(x):
                    shown.add(id(x))
                    frame_block(x, 'BEFORE-FRAME')
    # 3) long main-thread work that is not a frame
    long_other = []
    for x in tops:
        if x.dur > 30e6 and id(x) not in shown:
            if contains_frame(x):
                inner = [k for k in x.kids if is_frame(k)]
                extra = x.dur - sum(k.dur for k in inner)
                if extra > 15e6:
                    long_other.append((x, f'message around a doFrame, {extra / 1e6:.1f} ms outside it'))
            else:
                long_other.append((x, 'NOT a frame'))
    for x, why in sorted(long_other, key=lambda t: -t[0].dur)[:4]:
        shown.add(id(x))
        frame_block(x, f'LONG-MAIN ({why})')
    # 4) the composables / sections by total time in the window (if composition tracing worked)
    comp = defaultdict(lambda: [0, 0.0])
    for x in slices:
        if '.kt:' in x.name or re.search(r'\(\w+\.kt', x.name):
            c = comp[clean(x.name, mp)[:80]]
            c[0] += 1
            c[1] += x.dur / 1e6
    if comp:
        top = sorted(comp.items(), key=lambda kv: -kv[1][1])[:6]
        print(f"  composables (count, ms incl. children): {'; '.join(f'{k} ×{v[0]} {v[1]:.0f}' for k, v in top)}")
    if csvp:
        head = not os.path.exists(csvp)
        top2 = ''
        if worst is not None:
            k = sorted(worst.kids, key=lambda k: -k.dur)[:1]
            while k and k[0].kids and len(top2) < 120:
                top2 += clean(k[0].name, mp)[:40] + f" {k[0].dur / 1e6:.0f} > "
                k = sorted(k[0].kids, key=lambda c: -c.dur)[:1]
            if k:
                top2 += clean(k[0].name, mp)[:40] + f" {k[0].dur / 1e6:.0f}"
        lo = max((x for x in long_other if x[1] == 'NOT a frame'), key=lambda t: t[0].dur, default=None)
        with open(csvp, 'a', encoding='utf-8') as fo:
            w = csvmod.writer(fo)
            if head:
                w.writerow(['name', 'doframes', 'df32', 'df80', 'dfmax', 'worst_path', 'long_nonframe'])
            w.writerow([name, len(frames), sum(d > 32 for d in fd), sum(d > 80 for d in fd), f"{max(fd) if fd else 0:.1f}", top2,
                        f"{clean(lo[0].name, mp)[:50]} {lo[0].dur / 1e6:.0f}" if lo else ''])
    return keep


# ---------------------------------------------------------------- Perfetto (TraceProcessor)
def q(tp, sql):
    return list(tp.query(sql))


def cmd_tp(trace, pkg, name, js, mp_path, csvp):
    from perfetto.trace_processor import TraceProcessor
    frames_fs = json.load(open(js)) if os.path.exists(js) else []
    mp = mapping(mp_path)
    tp = TraceProcessor(trace=trace)
    try:
        procs = q(tp, f"select upid, pid, name from process where name = '{pkg}' or name like '{pkg}:%' or name like '%chyavorec%'")
        main = [p for p in procs if p.name == pkg] or procs
        if not main:
            print(f"TR {name}: app process not in the trace ({len(q(tp, 'select upid from process'))} processes)")
            return False
        upid, pid = main[-1].upid, main[-1].pid
        th = q(tp, f"select utid, tid, name from thread where upid = {upid}")
        mt = [t for t in th if t.tid == pid]
        if not mt:
            print(f"TR {name}: no main thread for pid {pid}")
            return False
        utid = mt[0].utid
        b = q(tp, "select start_ts, end_ts from trace_bounds")[0]
        t0 = b.start_ts
        rows = q(tp, f"select s.ts, s.dur, s.name from slice s join thread_track t on s.track_id = t.id where t.utid = {utid}")
        slices = [S(r.ts, r.dur if r.dur >= 0 else b.end_ts - r.ts, r.name or '') for r in rows]
        try:
            st = q(tp, f"select ts, dur, state, blocked_function from thread_state where utid = {utid}")
            states = [(r.ts, r.dur if r.dur >= 0 else 0, r.state, r.blocked_function) for r in st]
        except Exception:
            states = None
        others = {}
        render = []
        for t in th:
            if t.tid == pid:
                continue
            rs = q(tp, f"select s.ts, s.dur, s.name from slice s join thread_track tt on s.track_id = tt.id where tt.utid = {t.utid} and s.dur > 2000000")
            if t.name == 'RenderThread':
                render = [r.dur / 1e6 for r in rs if (r.name or '').startswith('DrawFrame')]
            if rs:
                others[t.name or str(t.tid)] = [S(r.ts, r.dur, r.name or '') for r in rs]
        off = 0
        try:
            cs = q(tp, "select ts, clock_value from clock_snapshot where clock_name = 'MONOTONIC' or clock_id = 3")
            if cs:
                d = sorted(r.ts - r.clock_value for r in cs)
                off = d[len(d) // 2]
        except Exception:
            pass
        names = len(set(s.name for s in slices))
        print(f"   (trace {(b.end_ts - b.start_ts) / 1e9:.1f} s, pid {pid}, {len(slices)} main-thread slices / {names} names, "
              f"thread states {'yes' if states else 'no'}, MONOTONIC→trace offset {off / 1e6:.0f} ms)")
        return analyse(name, slices, states, others, render, t0, frames_fs, off, mp, csvp, 'perfetto')
    finally:
        tp.close()


# ---------------------------------------------------------------- text atrace (API 26)
RX_AT = re.compile(r'^\s*(.+?)-(\d+)\s+(?:\(\s*[\d-]+\)\s+)?\[\d+\]\s+\S+\s+([\d.]+): tracing_mark_write: ([BE])(.*)$')


def cmd_at(trace, pid, name, js, mp_path, csvp):
    frames_fs = json.load(open(js)) if os.path.exists(js) else []
    mp = mapping(mp_path)
    pid = str(pid)
    stacks = defaultdict(list)
    done = defaultdict(list)
    comm = {}
    t0 = t1 = None
    try:
        f = open(trace, encoding='utf-8', errors='replace')
    except OSError:
        print(f"TR {name}: no atrace file")
        return False
    for l in f:
        m = RX_AT.match(l)
        if not m:
            continue
        c, tid, ts, kind, rest = m.groups()
        ts = int(round(float(ts) * 1e9))
        t0 = ts if t0 is None else t0
        t1 = ts
        if kind == 'B':
            p = rest.split('|', 2)
            if len(p) < 3 or p[1] != pid:
                continue
            comm[tid] = c.strip()
            stacks[tid].append(S(ts, 0, p[2]))
        elif stacks[tid]:
            x = stacks[tid].pop()
            x.dur = ts - x.ts
            done[tid].append(x)
    if t0 is None:
        print(f"TR {name}: no slices in the atrace")
        return False
    slices = done.get(pid, [])
    others = {comm.get(t, t): [x for x in v if x.dur > 2e6] for t, v in done.items() if t != pid}
    render = []
    for t, v in done.items():
        if comm.get(t, '').startswith('RenderThread'):
            render += [x.dur / 1e6 for x in v if x.name.startswith('DrawFrame')]
    print(f"   (atrace {(t1 - t0) / 1e9:.1f} s, pid {pid}, {len(slices)} main-thread slices, no thread states; offset assumed 0)")
    return analyse(name, slices, None, others, render, t0, frames_fs, 0, mp, csvp, 'atrace')


# ---------------------------------------------------------------- the table
def cmd_agg(fscsv, trcsv):
    fs = {}
    if os.path.exists(fscsv):
        for r in csvmod.DictReader(open(fscsv, encoding='utf-8')):
            fs[r['name']] = r
    tr = {}
    if os.path.exists(trcsv):
        for r in csvmod.DictReader(open(trcsv, encoding='utf-8')):
            tr[r['name']] = r
    names = list(fs) + [n for n in tr if n not in fs]
    print("interaction | frames | UI>32 | UI>80 | UI max | total max | doFrame max (trace) | path in the worst doFrame | long non-frame main-thread work")
    print("---|---|---|---|---|---|---|---|---")
    for n in names:
        a, b = fs.get(n, {}), tr.get(n, {})
        print(f"{n} | {a.get('frames', '')} | {a.get('ui32', '')} | {a.get('ui80', '')} | {a.get('uimax', '')} | {a.get('totmax', '')} | "
              f"{b.get('dfmax', '')} | {b.get('worst_path', '')[:150]} | {b.get('long_nonframe', '')}")
    bad = [(float(a.get('uimax') or 0), n) for n, a in fs.items()]
    bad.sort(reverse=True)
    print("slowest UI-thread frames: " + ', '.join(f"{n} {v:.0f} ms" for v, n in bad[:12]))


def cmd_warm():
    from perfetto.trace_processor.platform import PlatformDelegate
    print('trace_processor_shell:', PlatformDelegate().get_shell_path(None))


if __name__ == '__main__':
    c, a = sys.argv[1], sys.argv[2:]
    if c == 'fs':
        cmd_fs(*a)
    elif c == 'tp':
        sys.exit(3 if cmd_tp(*a) else 0)
    elif c == 'at':
        sys.exit(3 if cmd_at(*a) else 0)
    elif c == 'agg':
        cmd_agg(*a)
    elif c == 'warm':
        cmd_warm()
