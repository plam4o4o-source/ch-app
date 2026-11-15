"""Помощници на частите за v1.2.36 (ВРЕМЕННО, debug/w22-check).

  w22.py db FILE                 — user_version, таблици и брой редове на базата (с -wal/-shm до нея)
  w22.py files BEFORE AFTER      — сравнение на два списъка „път размер“ (какво е изчезнало / ново / променено)
  w22.py quizans UI.xml H W      — центровете на отговорите във викторината (по един на ред) и на „Напред“
  w22.py blank PNG [x0 y0 x1 y1] — средно и разсейване на яркостта в правоъгълник (празна ли е страницата)
  w22.py stitches UI.xml         — броят бодове от описанието на платното
  w22.py shotdiff A.png B.png    — средна разлика на яркостта между две снимки
  w22.py stats FILE...           — медиана/min/max на числата (по едно на ред)
"""
import os
import re
import shutil
import sqlite3
import sys
import tempfile
import xml.etree.ElementTree as ET


def cmd_db(path):
    if not os.path.exists(path) or os.path.getsize(path) == 0:
        print(f"DB {os.path.basename(path)}: missing or empty")
        return
    d = tempfile.mkdtemp()
    base = os.path.basename(path)
    for suf in ('', '-wal', '-shm'):
        if os.path.exists(path + suf):
            shutil.copy(path + suf, os.path.join(d, base + suf))
    try:
        con = sqlite3.connect(os.path.join(d, base))
        uv = con.execute('PRAGMA user_version').fetchone()[0]
        tabs = [r[0] for r in con.execute("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%' ORDER BY name")]
        counts = []
        for t in tabs:
            try:
                counts.append(f"{t}={con.execute(f'SELECT COUNT(*) FROM `{t}`').fetchone()[0]}")
            except Exception as e:  # noqa: BLE001
                counts.append(f"{t}=ERR({e})")
        extra = ''
        if 'want_to_read' in tabs:
            rows = con.execute('SELECT inv, title FROM want_to_read ORDER BY inv').fetchall()
            extra = ' want_to_read: ' + '; '.join(f"{i}:{t[:24]}" for i, t in rows[:5])
        ident = ''
        if 'room_master_table' in tabs:
            r = con.execute('SELECT identity_hash FROM room_master_table').fetchone()
            ident = f" identity_hash={r[0] if r else '-'}"
        print(f"DB {base}: user_version={uv}{ident}; tables: {' '.join(counts)};{extra}")
    except Exception as e:  # noqa: BLE001
        print(f"DB {base}: cannot open: {e}")
    finally:
        shutil.rmtree(d, ignore_errors=True)


def cmd_files(a, b):
    def load(p):
        d = {}
        for l in open(p, encoding='utf-8', errors='replace'):
            m = re.match(r'(\S+)\s+(\d+)', l.strip())
            if m:
                d[m.group(1)] = int(m.group(2))
        return d
    A, B = load(a), load(b)
    gone = sorted(set(A) - set(B))
    new = sorted(set(B) - set(A))
    chg = sorted(k for k in A if k in B and A[k] != B[k])
    print(f"FILES before {len(A)}, after {len(B)}; removed {len(gone)}: {' '.join(gone[:12])}; new {len(new)}: {' '.join(new[:12])}; size changed {len(chg)}: {' '.join(chg[:12])}")


def bounds(n):
    m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
    return tuple(map(int, m.groups())) if m else None


def alltext(n):
    return ' '.join(((c.get('text') or '') + ' ' + (c.get('content-desc') or '')).strip() for c in n.iter('node')).strip()


def cmd_quizans(path, H, W):
    root = ET.parse(path).getroot()
    H, W = int(H), int(W)
    answers, nxt = [], None
    for n in root.iter('node'):
        b = bounds(n)
        if not b or n.get('clickable') != 'true':
            continue
        w, h = b[2] - b[0], b[3] - b[1]
        if w < W * 0.55 or h < 70 or b[1] < H * 0.12:
            continue
        t = alltext(n)
        c = ((b[0] + b[2]) // 2, (b[1] + b[3]) // 2)
        if re.search(r'Напред|Виж резултата|Next|See the result|Сподели|Share', t):
            nxt = nxt or c
        elif h < H * 0.3:
            answers.append(c)
    for c in answers:
        print('A', *c)
    if nxt:
        print('N', *nxt)


def cmd_blank(png, *box):
    from PIL import Image, ImageStat
    im = Image.open(png).convert('L')
    if len(box) == 4:
        im = im.crop(tuple(map(int, box)))
    else:
        w, h = im.size
        im = im.crop((0, int(h * 0.12), w, int(h * 0.9)))
    s = ImageStat.Stat(im)
    print(f"mean={s.mean[0]:.0f} std={s.stddev[0]:.1f}")


def cmd_stitches(path):
    t = open(path, encoding='utf-8', errors='replace').read()
    m = re.search(r'(Платно за шевица|Embroidery canvas)[^"]*?(\d+)\s*(бода|бод|stitches|stitch)', t)
    print(m.group(2) if m else 'NA')


def cmd_shotdiff(a, b):
    from PIL import Image, ImageChops, ImageStat
    A = Image.open(a).convert('L')
    B = Image.open(b).convert('L')
    if A.size != B.size:
        B = B.resize(A.size)
    print(f"{ImageStat.Stat(ImageChops.difference(A, B)).mean[0]:.2f}")


def cmd_stats(*files):
    v = []
    for f in files:
        for l in open(f, encoding='utf-8', errors='replace'):
            try:
                v.append(float(l.strip()))
            except ValueError:
                pass
    if not v:
        print('n=0')
        return
    v.sort()
    print(f"n={len(v)} median={v[len(v) // 2]:.0f} min={v[0]:.0f} max={v[-1]:.0f}")


if __name__ == '__main__':
    c = sys.argv[1]
    globals()['cmd_' + c](*sys.argv[2:])
