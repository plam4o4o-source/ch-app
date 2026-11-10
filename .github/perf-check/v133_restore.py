"""Merge v133/ of several emulator-results commits into one tree (payloads for the GitHub Git Data API).

  restore.py OUTDIR TIP SHA_D SHA_C SHA_B SHA_A   (run inside the repo; newest first after TIP)

Writes OUTDIR/v133-tree.json (POST git/trees), OUTDIR/index.md and prints a summary.
Folder precedence: TIP > D > C > B > A; an older version of an existing folder is kept as '<round>-<name>'.
"""
import json, os, subprocess, sys

out, tip = sys.argv[1], sys.argv[2]
rounds = list(zip(['d', 'c', 'b', 'a'], sys.argv[3:7]))
SKIP = {'index.md', 'runs.txt'}


def git(*a):
    return subprocess.run(['git', *a], capture_output=True, text=True, check=True).stdout


def entries(sha):
    res = {}
    for l in git('ls-tree', sha, 'v133/').splitlines():
        meta, path = l.split('\t', 1)
        mode, typ, obj = meta.split()
        res[path.split('/', 1)[1]] = (mode, typ, obj)
    return res


merged = {}       # name -> (mode, typ, obj, src_sha, src_name, round)
for name, e in entries(tip).items():
    merged[name] = (*e, tip, name, 'tip')
for label, sha in rounds:
    for name, e in entries(sha).items():
        if name in SKIP or name == 'update':
            continue
        if name in ('size', 'sizediff') and name in merged:
            continue
        if any(v[2] == e[2] for v in merged.values()):
            continue  # the same tree already merged
        dest = name if name not in merged else f'{label}-{name}'
        if dest in merged:
            continue
        merged[dest] = (*e, sha, name, label)

# runs.txt: all lines, once
runs = []
for sha in [s for _, s in reversed(rounds)] + [tip]:
    try:
        for l in git('show', f'{sha}:v133/runs.txt').splitlines():
            if l not in runs:
                runs.append(l)
    except subprocess.CalledProcessError:
        pass

# index.md
idx = ['# Wave 19 emulator check: test v1.2.33 (6d3a324) vs v1.2.32 (87ef20e)', '',
       'Rounds a–e of debug/wave19-check (runs in runs.txt), merged. API 33 google_apis x86_64, -gpu swangle_indirect, '
       'unless the part name/status says otherwise (fb28/surprise28*: API 28, surprise31: API 31, live36: API 36, live361: API 36.1). '
       'A folder prefixed a-/b-/c- is the older round of a part re-run later. Previous release check: [../v132/index.md](../v132/index.md).',
       '', '| part | round | status |', '|---|---|---|']
dirs = sorted(n for n, v in merged.items() if v[1] == 'tree' and n not in ('size', 'sizediff', 'update'))
for n in dirs:
    mode, typ, obj, sha, src, label = merged[n]
    try:
        st = git('show', f'{sha}:v133/{src}/job-status.txt').strip()
    except subprocess.CalledProcessError:
        st = '-'
    try:
        done = 'COMPLETE' if '## COMPLETE' in git('show', f'{sha}:v133/{src}/summary.txt') else 'incomplete'
    except subprocess.CalledProcessError:
        done = 'no summary'
    idx.append(f'| {n} | {label} ({sha[:7]}) | {done}; {st} |')
idx += ['', '```']
for f in ('size/size-new.txt', 'size/size-base.txt'):
    try:
        idx.append(git('show', f'{tip}:v133/{f}').rstrip())
    except subprocess.CalledProcessError:
        pass
idx += ['```', '']
for n in ['sizediff'] + dirs:
    if n not in merged:
        continue
    mode, typ, obj, sha, src, label = merged[n]
    try:
        s = git('show', f'{sha}:v133/{src}/summary.txt')
    except subprocess.CalledProcessError:
        continue
    idx += [f'## {n}', '', '```', s.rstrip(), '```', '']
idx += ['## Files', '']
for n in sorted(merged):
    mode, typ, obj, sha, src, label = merged[n]
    if typ != 'tree':
        continue
    for l in git('ls-tree', '-r', '--name-only', sha, f'v133/{src}/').splitlines():
        rel = n + l[len(f'v133/{src}'):]
        idx.append(f'- [{rel}]({rel})')
os.makedirs(out, exist_ok=True)
open(os.path.join(out, 'index.md'), 'w', encoding='utf-8').write('\n'.join(idx) + '\n')
open(os.path.join(out, 'runs.txt'), 'w', encoding='utf-8').write('\n'.join(runs) + '\n')
tree = [{'path': n, 'mode': v[0], 'type': v[1], 'sha': v[2]} for n, v in sorted(merged.items()) if n not in SKIP]
tree.append({'path': 'index.md', 'mode': '100644', 'type': 'blob', 'content': '\n'.join(idx) + '\n'})
tree.append({'path': 'runs.txt', 'mode': '100644', 'type': 'blob', 'content': '\n'.join(runs) + '\n'})
json.dump({'tree': tree}, open(os.path.join(out, 'v133-tree.json'), 'w', encoding='utf-8'), ensure_ascii=False)
for n, v in sorted(merged.items()):
    print(f'{n:<16} {v[5]:<4} {v[3][:7]} {v[4]}')
print(f'index.md {len(idx)} lines; runs.txt {len(runs)} lines; tree entries {len(tree)}')
