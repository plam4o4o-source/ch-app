"""Разчитане на изхода от dumpsys / logcat за проверката на емулатор.

  parse.py gfx FILE LABEL        -> един ред: кадри, janky %, перцентили
  parse.py mem FILE LABEL        -> един ред: TOTAL PSS / Java Heap / Native Heap (KB)
  parse.py starts FILE LABEL     -> min / median / max на TotalTime от `am start -W`
  parse.py logcat FILE PKG       -> редовете на приложението: FATAL, ANR, StrictMode, Skipped frames
  parse.py block FILE START STOP -> блокът от START до следващия STOP (напр. AppSettings в dumpsys notification)
"""
import re, statistics, sys

cmd, path = sys.argv[1], sys.argv[2]
text = open(path, encoding='utf-8', errors='replace').read()


def first(pattern, default='?'):
    m = re.search(pattern, text, re.M)
    return m.group(1) if m else default


if cmd == 'gfx':
    label = sys.argv[3]
    frames = first(r'Total frames rendered:\s*(\d+)')
    janky = first(r'Janky frames:\s*\d+\s*\(([\d.]+)%\)')
    jn = first(r'Janky frames:\s*(\d+)')
    p = [first(rf'^\s*{q}th percentile:\s*(\d+)ms') for q in (50, 90, 95, 99)]
    slow_ui = first(r'Number Slow UI thread:\s*(\d+)')
    slow_draw = first(r'Number Slow issue draw commands:\s*(\d+)')
    hi_input = first(r'Number High input latency:\s*(\d+)')
    print(f"GFX {label:<12} frames={frames:>5}  janky={jn} ({janky}%)  p50={p[0]}ms p90={p[1]}ms p95={p[2]}ms p99={p[3]}ms"
          f"  slowUI={slow_ui} slowDraw={slow_draw} highInput={hi_input}")
elif cmd == 'mem':
    label = sys.argv[3]
    pss = first(r'TOTAL PSS:\s*(\d+)')
    if pss == '?':
        pss = first(r'^\s*TOTAL\s+(\d+)')
    java = first(r'Java Heap:\s*(\d+)')
    native = first(r'Native Heap:\s*(\d+)')
    graphics = first(r'Graphics:\s*(\d+)')
    views = first(r'Views:\s*(\d+)')
    acts = first(r'Activities:\s*(\d+)')
    print(f"MEM {label:<16} PSS={pss}KB  Java={java}KB  Native={native}KB  Graphics={graphics}KB  Views={views} Activities={acts}")
elif cmd == 'starts':
    label = sys.argv[3]
    vals = [int(v) for v in re.findall(r'TotalTime:\s*(\d+)', text)]
    states = re.findall(r'LaunchState:\s*(\w+)', text)
    if vals:
        print(f"START {label:<18} n={len(vals)} min={min(vals)}ms median={statistics.median(vals):.0f}ms max={max(vals)}ms"
              f"  all={vals}  states={sorted(set(states))}")
    else:
        print(f"START {label:<18} no TotalTime values")
elif cmd == 'logcat':
    pkg = sys.argv[3]
    pids = set(re.findall(r'Start proc (\d+):' + re.escape(pkg) + r'[/ :]', text))
    pids |= set(re.findall(r'Start proc (\d+):' + re.escape(pkg) + r'$', text, re.M))
    print(f"app pids seen: {sorted(pids, key=int)}")
    lines = text.splitlines()
    def pid_of(line):
        m = re.match(r'^\S+\s+\S+\s+(\d+)\s+\d+\s', line)  # -v threadtime
        return m.group(1) if m else None
    cats = {
        'FATAL': re.compile(r'FATAL EXCEPTION|AndroidRuntime: Process: ' + re.escape(pkg)),
        'ANR': re.compile(r'ANR in ' + re.escape(pkg) + r'|Application Not Responding.*' + re.escape(pkg)),
        'StrictMode': re.compile(r'StrictMode'),
        'Skipped': re.compile(r'Skipped \d+ frames'),
        'Davey': re.compile(r'Davey! duration=\d+ms'),
    }
    for name, rx in cats.items():
        hits = []
        for i, l in enumerate(lines):
            if not rx.search(l):
                continue
            if name in ('StrictMode', 'Skipped', 'Davey') and pid_of(l) not in pids:
                continue
            if name == 'FATAL' and pid_of(l) not in pids and pkg not in l:
                # FATAL от друг процес: показваме само ако следващите редове споменават пакета
                if not any(pkg in x for x in lines[i:i + 3]):
                    continue
            hits.append(i)
        print(f"== {name}: {len(hits)}")
        if name == 'Skipped':
            nums = [int(re.search(r'Skipped (\d+) frames', lines[i]).group(1)) for i in hits]
            if nums:
                print(f"   skipped-frames events={len(nums)} max={max(nums)} total={sum(nums)}")
        if name == 'Davey':
            nums = [int(re.search(r'duration=(\d+)ms', lines[i]).group(1)) for i in hits]
            if nums:
                print(f"   davey events={len(nums)} max={max(nums)}ms median={statistics.median(nums):.0f}ms")
        shown = 0
        for i in hits:
            ctx = lines[i:i + 25] if name == 'FATAL' else lines[i:i + 1]
            for l in ctx:
                print('   ' + l[:240])
            shown += 1
            if shown >= (3 if name == 'FATAL' else 12):
                break
    # Хронология: маркерите на секциите и смъртта/убиването на процеса (без нашите force-stop)
    print("== timeline (PERFCHECK markers, process deaths/kills not caused by force-stop)")
    rx = re.compile(r'PERFCHECK|Process ' + re.escape(pkg) + r' \(pid \d+\) has died|Killing \d+:' + re.escape(pkg) + r'|lowmemorykiller.*' + re.escape(pkg))
    for i, l in enumerate(lines):
        if rx.search(l) and 'Force stopping' not in l and 'due to from pid' not in l:
            print('   ' + l[:220])
elif cmd == 'block':
    start, stop = sys.argv[3], sys.argv[4]
    lines = text.splitlines()
    on = False
    for l in lines:
        if start in l:
            on = True
        elif on and stop in l:
            on = False
        if on:
            print(l[:300])
