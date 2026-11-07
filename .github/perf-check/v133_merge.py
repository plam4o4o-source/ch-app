"""Вълна 19 (ВРЕМЕННО): събира v133/ от кръговете a, b, c (и върха на клона) в един индекс.

  v133_merge.py TIP SHA_C SHA_B SHA_A   (в git хранилище с индекс = TIP)

Папките на по-новия кръг остават под името си; по-старите версии на същата папка — като
„<кръг>-<име>“ (напр. a-fb28). Пише записи за `git update-index --index-info` на stdout.
"""
import subprocess, sys

tip, rounds = sys.argv[1], list(zip(['c', 'b', 'a'], sys.argv[2:5]))


def folders(sha):
    out = subprocess.run(['git', 'ls-tree', '--name-only', sha, 'v133/'], capture_output=True, text=True).stdout.split()
    return {p.split('/', 1)[1] for p in out if '/' in p} - {'index.md', 'runs.txt', 'size', 'sizediff', 'update'}


taken = folders(tip)
for label, sha in rounds:
    for f in sorted(folders(sha)):
        dest = f if f not in taken else f'{label}-{f}'
        if dest in taken:
            continue
        taken.add(dest)
        ls = subprocess.run(['git', 'ls-tree', '-r', sha, f'v133/{f}'], capture_output=True, text=True).stdout
        for line in ls.splitlines():
            meta, path = line.split('\t', 1)
            print(f"{meta}\tv133/{dest}/{path[len(f'v133/{f}/'):]}")
        print(f"merged {sha[:7]} v133/{f} -> v133/{dest}", file=sys.stderr)
