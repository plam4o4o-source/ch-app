"""TEMPORARY (debug/w27-cat, results in emulator-results/v142perf): main-thread CPU per frame for one measured
interaction, and the A/B table over variants (build + launch flags).

  w28.py meas TRACE PKG NAME CSV   one Perfetto trace: CPU (Running) of every doFrame of the app's main thread from
                                   the first input on (or from the trace start, when the action has no input event:
                                   typing through the test IME); the worst frame, frames over 16/25/30 ms, the entry
                                   frame (first doFrame >= 8 ms) and the next one, catalogue rows composed per frame
                                   ("w28 row" sections of the experimental build). Exit code 3: worst > 25 ms.
  w28.py agg CSV                   names <kind>@<variant>@r<round>[-<i>] → table per kind and variant
"""
import csv as csvmod
import os
import re
import statistics as stx
import sys
from collections import defaultdict

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from w27 import S, Cpu, is_frame, q  # noqa: E402


def cmd_meas(trace, pkg, name, csvp):
    from perfetto.trace_processor import TraceProcessor
    tp = TraceProcessor(trace=trace)
    try:
        procs = q(tp, f"select upid, pid, name from process where name = '{pkg}'")
        if not procs:
            print(f"M {name}: app process {pkg} not in the trace")
            return False
        upid, pid = procs[-1].upid, procs[-1].pid
        utid = q(tp, f"select utid from thread where upid = {upid} and tid = {pid}")[0].utid
        b = q(tp, "select start_ts, end_ts from trace_bounds")[0]
        rows = q(tp, f"select s.ts, s.dur, s.name from slice s join thread_track t on s.track_id = t.id where t.utid = {utid}")
        st = q(tp, f"select ts, dur, state, blocked_function from thread_state where utid = {utid}")
        cpu = Cpu([(r.ts, r.dur if r.dur >= 0 else 0, r.state, r.blocked_function) for r in st])
        sl = [S(r.ts, r.dur if r.dur >= 0 else b.end_ts - r.ts, r.name or '') for r in rows]
        inputs = [x for x in sl if re.search(r'ACTION_DOWN|ACTION_UP|KeyEvent|deliverInputEvent|dispatchInputEvent', x.name)]
        t_in = min((x.ts for x in inputs), default=b.start_ts)
        frames = sorted((x for x in sl if is_frame(x) and x.ts >= t_in - 2e6), key=lambda x: x.ts)
        rowsl = sorted(x.ts for x in sl if x.name == 'w28 row')
        import bisect
        fc, fr = [], []
        for x in frames:
            fc.append(cpu(x.ts, x.end) / 1e6)
            fr.append(bisect.bisect_left(rowsl, x.end) - bisect.bisect_left(rowsl, x.ts))
        k0 = next((i for i, c in enumerate(fc) if c >= 8), None)
        worst = max(fc, default=0)
        kw = fc.index(worst) if fc else 0
        f1 = fc[k0] if k0 is not None else 0
        f2 = fc[k0 + 1] if k0 is not None and k0 + 1 < len(fc) else 0
        n16 = sum(1 for c in fc if c > 16)
        n25 = sum(1 for c in fc if c > 25)
        n30 = sum(1 for c in fc if c > 30)
        work = [c for c in fc if c >= 3]
        seq = fc[k0:k0 + 4] if k0 is not None else []
        rw = fr[kw] if fr else 0
        print(f"M {name}: worst {worst:.1f} ms CPU (rows composed in it {rw}); frames >16 {n16}, >25 {n25}, >30 {n30} of {len(fc)} "
              f"({len(work)} with >= 3 ms); entry frame {f1:.1f} + next {f2:.1f} (from the entry: {', '.join(f'{c:.1f}' for c in seq)}); "
              f"rows composed per frame max {max(fr, default=0)}{'' if inputs else ' [no input slice: from the trace start]'}")
        head = not os.path.exists(csvp)
        with open(csvp, 'a', encoding='utf-8') as fo:
            w = csvmod.writer(fo)
            if head:
                w.writerow(['name', 'worst', 'f1', 'f2', 'n16', 'n25', 'n30', 'nframes', 'nwork', 'rows_worst', 'rows_max', 'cpu_all'])
            w.writerow([name, f"{worst:.1f}", f"{f1:.1f}", f"{f2:.1f}", n16, n25, n30, len(fc), len(work), rw, max(fr, default=0), f"{sum(fc):.1f}"])
        return worst > 25
    finally:
        tp.close()


def cmd_agg(csvp):
    g = defaultdict(lambda: defaultdict(list))
    order = []
    for r in csvmod.DictReader(open(csvp, encoding='utf-8')):
        p = r['name'].split('@')
        if len(p) < 3:
            continue
        kind, var = p[0], p[1]
        if kind not in order:
            order.append(kind)
        g[kind][var].append(r)
    md = lambda v: stx.median(v) if v else 0
    print("kind | variant | n | worst frame CPU ms: median / mean / max | entry frame median | next frame median | frames >25 ms (total) | frames >30 ms (total) | rows composed in the worst frame (median) | CPU of all frames, median")
    print("---|---|---|---|---|---|---|---|---|---")
    for kind in order:
        vs = sorted(g[kind], key=lambda v: (v != 'base', v != 'x', v))
        for v in vs:
            rs = g[kind][v]
            w = [float(r['worst']) for r in rs]
            f1 = [float(r['f1']) for r in rs]
            f2 = [float(r['f2']) for r in rs]
            print(f"{kind} | {v} | {len(rs)} | {md(w):.1f} / {sum(w) / len(w):.1f} / {max(w):.1f} | {md(f1):.1f} | {md(f2):.1f} | "
                  f"{sum(int(r['n25']) for r in rs)} | {sum(int(r['n30']) for r in rs)} | {md([int(r['rows_worst']) for r in rs]):.0f} | "
                  f"{md([float(r['cpu_all']) for r in rs]):.0f}")


if __name__ == '__main__':
    c, a = sys.argv[1], sys.argv[2:]
    if c == 'meas':
        sys.exit(3 if cmd_meas(*a) else 0)
    elif c == 'agg':
        cmd_agg(*a)
