# -*- coding: utf-8 -*-
"""Какво е пораснало в APK-а (ВРЕМЕННО, debug/wave16-check, v130b).

  sizediff.py NEW.apk BASE.apk NEW-mapping.txt BASE-mapping.txt AAPT2 [APKANALYZER]

- файловете в APK-а по групи (dex, res по типа и префикса на ресурса, arsc, …): компресиран размер;
- res: ресурсите (по имена от `aapt2 dump resources`), които ги има само в новия, групирани по префикс;
- resources.arsc: брой ресурси по тип и брой езикови конфигурации на низовете;
- dex: пакетите по apkanalyzer (размер, с mapping) и/или броят класове по пакет от mapping.txt.
"""
import collections
import re
import subprocess
import sys
import zipfile

new_apk, base_apk, new_map, base_map, aapt2 = sys.argv[1:6]
apkan = sys.argv[6] if len(sys.argv) > 6 else ''


def res_names(apk):
    """{път на файл в APK-а: 'тип/име'} и {тип: брой ресурси}, {езикови конфигурации на string}."""
    out = subprocess.run([aapt2, 'dump', 'resources', apk], capture_output=True, text=True).stdout
    files, types, locales = {}, collections.Counter(), set()
    cur = None
    for line in out.splitlines():
        m = re.match(r'\s+resource 0x[0-9a-f]+ ([\w.]+/[\w.$-]+)', line)
        if m:
            cur = m.group(1)
            types[cur.split('/')[0]] += 1
            continue
        f = re.search(r'\(file\) (res/\S+)', line)
        if f and cur:
            files[f.group(1)] = cur
        c = re.match(r'\s+\(([a-zA-Z-]+[^)]*)\)\s', line)
        if c and cur and cur.startswith('string/'):
            locales.add(c.group(1))
    return files, types, locales


def prefix(name):
    t, n = name.split('/', 1)
    p = n.split('_')[0] if '_' in n else n
    return f"{t}/{p}_*"


def zip_sizes(apk):
    return {i.filename: i.compress_size for i in zipfile.ZipFile(apk).infolist()}


nz, bz = zip_sizes(new_apk), zip_sizes(base_apk)
print(f"TOTAL compressed entries: new {sum(nz.values())} B, base {sum(bz.values())} B, delta {sum(nz.values()) - sum(bz.values()):+d} B")

nf, nt, nl = res_names(new_apk)
bf, bt, bl = res_names(base_apk)
print(f"ARSC resources by type (new/base/delta): " + ', '.join(
    f"{t} {nt[t]}/{bt[t]}/{nt[t] - bt[t]:+d}" for t in sorted(set(nt) | set(bt), key=lambda t: -(nt[t] - bt[t])) if nt[t] != bt[t]))
print(f"ARSC string configurations: new {len(nl)}, base {len(bl)}; only in new: {sorted(nl - bl)[:20]}")

# res/: по имената на ресурсите
nres = collections.Counter()
for path, size in nz.items():
    if path.startswith('res/'):
        nres[prefix(nf.get(path, 'unknown/' + path))] += size
bres = collections.Counter()
for path, size in bz.items():
    if path.startswith('res/'):
        bres[prefix(bf.get(path, 'unknown/' + path))] += size
diff = sorted(((nres[k] - bres[k], k) for k in set(nres) | set(bres)), reverse=True)
print("RES files, top growth by resource-name prefix (compressed B, new/base):")
for d, k in diff[:15]:
    if d > 0:
        print(f"  {d:+8d}  {k}  ({nres[k]}/{bres[k]})")
only_new = sorted({nf.get(p, p) for p in nz if p.startswith('res/')} - {bf.get(p, p) for p in bz if p.startswith('res/')})
print(f"RES resources with files only in new: {len(only_new)}; sample: {only_new[:25]}")


def classes(mapfile):
    c = collections.Counter()
    for line in open(mapfile, encoding='utf-8', errors='replace'):
        m = re.match(r'^([\w.$]+) -> [\w.$]+:$', line)
        if m:
            parts = m.group(1).split('.')
            c['.'.join(parts[:3]) if parts[0] in ('androidx', 'com', 'org', 'kotlinx', 'io') else '.'.join(parts[:2])] += 1
    return c


nc, bc = classes(new_map), classes(base_map)
print(f"DEX classes (mapping.txt): new {sum(nc.values())}, base {sum(bc.values())}; top growth by package:")
for d, k in sorted(((nc[k] - bc[k], k) for k in set(nc) | set(bc)), reverse=True)[:15]:
    if d:
        print(f"  {d:+6d}  {k}  ({nc[k]}/{bc[k]})")

if apkan:
    def pk(apk, mp):
        r = subprocess.run([apkan, 'dex', 'packages', '--defined-only', '--proguard-mappings', mp, apk], capture_output=True, text=True)
        sizes = {}
        for line in r.stdout.splitlines():
            f = line.split()
            # P d <defined methods> <referenced methods> <size> <name>
            if len(f) >= 6 and f[0] == 'P':
                sizes[f[5]] = int(f[4])
        if not sizes:
            print(f"apkanalyzer: no output ({r.stderr.strip()[:200]})")
        return sizes
    ns, bs = pk(new_apk, new_map), pk(base_apk, base_map)
    if ns and bs:
        def roll(s, depth):
            c = collections.Counter()
            for k, v in s.items():
                if k.count('.') + 1 == depth or (k.count('.') + 1 < depth and not any(x.startswith(k + '.') for x in s)):
                    c[k] += v
            return c
        for depth in (2, 3):
            n2, b2 = roll(ns, depth), roll(bs, depth)
            print(f"DEX size by package (depth {depth}, apkanalyzer, bytes), top growth:")
            for d, k in sorted(((n2[k] - b2[k], k) for k in set(n2) | set(b2)), reverse=True)[:12]:
                if d:
                    print(f"  {d:+8d}  {k}  ({n2[k]}/{b2[k]})")
