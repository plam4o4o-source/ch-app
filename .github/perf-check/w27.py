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


def _m(mp, t):
    return mp.get(t, t)


def clean(name, mp):
    name = re.sub(r'\s+', ' ', name or '').strip()
    if mp:
        name = re.sub(r'[A-Za-z_$][\w$]*(?:\.[\w$]+)+', lambda m: _m(mp, m.group(0)), name)
        # R8 repackages to the default package: the handler class and the callback of a looper message
        m = re.match(r'^msg Handler \(([^)]+)\) (\S+?)(:.*)?$', name)
        if m:
            cb = re.sub(r'[$@][^$@]*$', '', m.group(2)) if m.group(2) not in mp else m.group(2)
            name = f"msg Handler ({_m(mp, m.group(1))}) {_m(mp, m.group(2)) if m.group(2) in mp else _m(mp, cb) + m.group(2)[len(cb):]}{m.group(3) or ''}"
        name = re.sub(r'(Continuation at )([\w$.]+)', lambda m: m.group(1) + _m(mp, m.group(2)), name)
        name = name.replace('org.chyavorec.app.', '').replace('androidx.compose.', 'a.c.').replace('kotlinx.coroutines.', 'kx.')
    return name[:110]


# ---------------------------------------------------------------- slices: one tree over all tracks of the main thread
class S:
    __slots__ = ('ts', 'dur', 'name', 'kids', 'depth', 'parent', 'cpu')

    def __init__(self, ts, dur, name):
        self.ts, self.dur, self.name = ts, dur, name
        self.kids, self.depth, self.parent, self.cpu = [], 0, None, None

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


WAIT = re.compile(r'^(postAndWait|dequeueBuffer|eglSwapBuffers|waitForPresent|DrawFrame)')


class Cpu:
    """CPU time of the main thread in [a, b): Running intervals of thread_state (Perfetto); without states
    (text atrace) — the wall time minus the waits for the RenderThread (postAndWait, dequeueBuffer)."""

    def __init__(self, states):
        self.ok = bool(states)
        run = sorted((ts, ts + d) for ts, d, st, _ in (states or []) if st == 'Running' and d > 0)
        self.st = [r[0] for r in run]
        self.en = [r[1] for r in run]
        self.pre = [0]
        for x, y in run:
            self.pre.append(self.pre[-1] + (y - x))

    def __call__(self, a, b, node=None):
        if not self.ok:
            if node is None:
                return b - a
            return max(0, node.dur - sum(w.dur for w in walk(node) if w is not node and WAIT.match(w.name)))
        import bisect
        i = bisect.bisect_right(self.en, a)
        j = bisect.bisect_left(self.st, b)
        if j <= i:
            return 0
        tot = self.pre[j] - self.pre[i]
        tot -= max(0, a - self.st[i])
        tot -= max(0, self.en[j - 1] - b)
        return max(0, tot)


def walk(x):
    yield x
    for k in x.kids:
        yield from walk(k)


def cpu_of(x, cpu):
    if x.cpu is None:
        x.cpu = cpu(x.ts, x.end, x)
    return x.cpu


def tree_lines(root, mp, cpu, limit=36, maxdepth=10):
    thr = max(1.0e6, cpu_of(root, cpu) * 0.03)
    lines = []

    def go(x, d):
        if len(lines) >= limit or d > maxdepth:
            return
        if d > 0:
            lines.append(f"{'  ' * (d + 2)}cpu {cpu_of(x, cpu) / 1e6:6.1f} / wall {x.dur / 1e6:6.1f} ms  d{d}  {clean(x.name, mp)}")
        for k in sorted(x.kids, key=lambda k: k.ts):
            if cpu_of(k, cpu) >= thr:
                go(k, d + 1)
    go(root, 0)
    return lines


def self_top(root, mp, cpu, n=8):
    agg = defaultdict(float)
    for x in walk(root):
        own = cpu_of(x, cpu) - sum(cpu_of(k, cpu) for k in x.kids)
        agg[clean(x.name, mp)[:60]] += own / 1e6
    top = sorted(agg.items(), key=lambda kv: -kv[1])[:n]
    return ', '.join(f"{k} {v:.1f}" for k, v in top if v >= 0.5)


def is_frame(x):
    return x.name.startswith('Choreographer#doFrame')


def contains_frame(x):
    return any(is_frame(w) for w in walk(x))


def states_in(states, a, b):
    if not states:
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


def path_of(x, mp, cpu):
    out = ''
    k = x
    while k.kids and len(out) < 160:
        k = max(k.kids, key=lambda c: cpu_of(c, cpu))
        if cpu_of(k, cpu) < 1e6:
            break
        out += f" > {clean(k.name, mp)[:45]} {cpu_of(k, cpu) / 1e6:.0f}"
    return out


def analyse(name, slices, states, others, render, t0, frames_fs, off, mp, csvp, mode):
    """slices — S of the main thread (all tracks), states — (ts, dur, state, blocked_fn) or None,
    others — {thread: [S]} other threads of the app, render — RenderThread DrawFrame durations (ms).
    Everything is ranked by the main thread's own CPU time: on the emulator (software GPU) the UI thread spends
    most of each frame waiting for the RenderThread (postAndWait), which says nothing about a phone."""
    cpu = Cpu(states)
    nest(slices)
    frames = [x for x in slices if is_frame(x)]
    tops = [x for x in slices if x.parent is None]
    fc = [cpu_of(x, cpu) / 1e6 for x in frames]
    fw = [x.dur / 1e6 for x in frames]
    # Work outside frames: top-level slices without a doFrame inside (hook: every looper message) and, from
    # thread states, CPU bursts of the main thread outside any top-level slice (the released build has no
    # message slices: coroutine resumptions on Dispatchers.Main are invisible otherwise).
    nonframe = [x for x in tops if not contains_frame(x)]
    bursts = []
    if cpu.ok:
        tops_iv = sorted((x.ts, x.end) for x in tops)
        cur = None
        for a, b in zip(cpu.st, cpu.en):
            # clip out the parts inside top-level slices
            segs = [(a, b)]
            for x0, x1 in tops_iv:
                if x1 <= a or x0 >= b:
                    continue
                segs = [p for s_ in segs for p in ((s_[0], min(s_[1], x0)), (max(s_[0], x1), s_[1])) if p[1] > p[0]]
            for s0, s1 in segs:
                if cur and s0 - cur[1] < 3e6:
                    cur = [cur[0], s1, cur[2] + (s1 - s0)]
                else:
                    if cur:
                        bursts.append(cur)
                    cur = [s0, s1, s1 - s0]
        if cur:
            bursts.append(cur)
    nf_cpu = [cpu_of(x, cpu) / 1e6 for x in nonframe] + [b[2] / 1e6 for b in bursts]
    print(f"TR {name} ({mode}): doFrame n={len(frames)}; main-thread CPU per frame: >16ms {sum(c > 16 for c in fc)}, >32ms {sum(c > 32 for c in fc)}, "
          f">80ms {sum(c > 80 for c in fc)}, max {max(fc) if fc else 0:.1f} (wall max {max(fw) if fw else 0:.0f}, mostly waiting for the "
          f"RenderThread: DrawFrame max {max(render) if render else 0:.0f}); outside frames: max {max(nf_cpu) if nf_cpu else 0:.1f} ms CPU"
          f"{'' if cpu.ok else ' [no thread states: CPU = wall minus RenderThread waits]'}")
    items = [(cpu_of(x, cpu), 'FRAME', x) for x in frames] + [(cpu_of(x, cpu), 'MSG', x) for x in nonframe]
    items += [(b[2], 'BURST', b) for b in bursts]
    items.sort(key=lambda t: -t[0])
    keep = bool(items) and items[0][0] > 25e6
    for c, kind, x in items[:5]:
        if c < 16e6:
            break
        if kind == 'BURST':
            a, b, _ = x
            ov = sorted({clean(s_.name, mp)[:60] for s_ in slices if s_.end > a and s_.ts < b})[:8]
            print(f"  BURST {c / 1e6:.1f} ms CPU in {(b - a) / 1e6:.1f} ms at +{(a - t0) / 1e6:.0f} ms: main thread busy OUTSIDE any slice "
                  f"(e.g. a coroutine resumed on Dispatchers.Main; the hook build names it){'; slices around: ' + ', '.join(ov) if ov else ''}")
            continue
        chain = []
        par = x
        while par.parent is not None:
            par = par.parent
            chain.append(clean(par.name, mp)[:70])
        print(f"  {kind} cpu {c / 1e6:.1f} ms (wall {x.dur / 1e6:.1f}) at +{(x.ts - t0) / 1e6:.0f} ms | {clean(x.name, mp)[:100]}"
              + (f" | inside: {' < '.join(chain)}" if chain else ''))
        st = states_in(states, x.ts, x.end)
        if st:
            print(f"      main thread states (ms): {st}")
        for l in tree_lines(x, mp, cpu):
            print(l)
        print(f"      self CPU by slice (ms): {self_top(x, mp, cpu)}")
        ot = []
        for th, ss in others.items():
            for o in ss:
                lo, hi = max(o.ts, x.ts), min(o.end, x.end)
                if hi - lo > 4e6 and re.search(r'GC|garbage|Lock contention|monitor contention|JIT|Compil|Verif|binder|Inflat|SQLite|sqlite|Room|dex|Class', o.name, re.I):
                    ot.append(f"{th}: {clean(o.name, mp)[:60]} {o.dur / 1e6:.1f}")
        if ot:
            print(f"      other app threads meanwhile: {'; '.join(ot[:6])}")
    # the slow framestats frames: how much of [vsync, sync] the main thread really ran
    if frames_fs and cpu.ok:
        rows = []
        for f in frames_fs:
            a, b = f['vsync'] + off, f.get('syncq', f.get('done', f['vsync'])) + off
            rows.append((cpu(a, b), f))
        rows.sort(key=lambda t: -t[0])
        print("  FS frames over 40 ms (ui) → main-thread CPU in [vsync, sync]: " +
              ', '.join(f"ui {f['ui']:.0f}/cpu {c / 1e6:.0f}" for c, f in rows[:8]))
    comp = defaultdict(lambda: [0, 0.0])
    for x in slices:
        if re.search(r'\(\w+\.kt:\d+\)', x.name):
            c_ = comp[clean(x.name, mp)[:80]]
            c_[0] += 1
            c_[1] += cpu_of(x, cpu) / 1e6
    if comp:
        top = sorted(comp.items(), key=lambda kv: -kv[1][1])[:8]
        print(f"  composables (count, CPU ms incl. children): {'; '.join(f'{k} ×{v[0]} {v[1]:.0f}' for k, v in top)}")
    if csvp:
        head = not os.path.exists(csvp)
        wf = max(frames, key=lambda x: cpu_of(x, cpu), default=None)
        wm = max(nonframe, key=lambda x: cpu_of(x, cpu), default=None)
        wb = max(bursts, key=lambda b: b[2], default=None)
        nfdesc = ''
        if wm is not None and (wb is None or cpu_of(wm, cpu) >= wb[2]):
            nfdesc = f"{clean(wm.name, mp)[:60]} {cpu_of(wm, cpu) / 1e6:.0f}{path_of(wm, mp, cpu)[:80]}"
        elif wb is not None:
            nfdesc = f"unnamed main-thread burst {wb[2] / 1e6:.0f}"
        with open(csvp, 'a', encoding='utf-8') as fo:
            w = csvmod.writer(fo)
            if head:
                w.writerow(['name', 'doframes', 'cpu16', 'cpu32', 'cpumax', 'worst_path', 'nonframe'])
            w.writerow([name, len(frames), sum(c > 16 for c in fc), sum(c > 32 for c in fc), f"{max(fc) if fc else 0:.1f}",
                        path_of(wf, mp, cpu) if wf is not None else '', nfdesc])
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
    fs, tr = {}, {}
    if os.path.exists(fscsv):
        for r in csvmod.DictReader(open(fscsv, encoding='utf-8')):
            fs[r['name']] = r
    if os.path.exists(trcsv):
        for r in csvmod.DictReader(open(trcsv, encoding='utf-8')):
            tr[r['name']] = r
    names = list(fs) + [n for n in tr if n not in fs]
    print("interaction | frames | ui>32 | ui max | doFrames | main CPU/frame >16 | >32 | max CPU in a frame | path in that frame (CPU ms) | worst work outside frames (CPU ms)")
    print("---|---|---|---|---|---|---|---|---|---")
    for n in names:
        a, b = fs.get(n, {}), tr.get(n, {})
        print(f"{n} | {a.get('frames', '')} | {a.get('ui32', '')} | {a.get('uimax', '')} | {b.get('doframes', '')} | {b.get('cpu16', '')} | "
              f"{b.get('cpu32', '')} | {b.get('cpumax', '')} | {b.get('worst_path', '')[:170]} | {b.get('nonframe', '')[:150]}")
    bad = sorted(((float(b.get('cpumax') or 0), n) for n, b in tr.items()), reverse=True)
    print("highest main-thread CPU in one frame: " + ', '.join(f"{n} {v:.0f} ms" for v, n in bad[:14]))


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
