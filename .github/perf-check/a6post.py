"""След частта: смалява изхода, преди да отиде в emulator-results (ВРЕМЕННО, debug/a6-full).

  a6post.py OUT_DIR
- снимките (PNG) → JPEG: обиколките (tour/, robust/, rotation/, procdeath/, debug/) на 50 %, останалите на 60 %;
  контактните листове и споделените картинки (share/) остават както са;
- logcat: остава само logcat-all.txt.gz (live/dump са негови части); a11y/, cpu/, hi/*.txt и *.xml → raw.tgz.
"""
import gzip, os, shutil, sys, tarfile

out = sys.argv[1]
try:
    from PIL import Image
except Exception:  # noqa: BLE001
    Image = None

n = saved = 0
if Image is not None:
    for root, _, files in os.walk(out):
        rel = os.path.relpath(root, out).replace(os.sep, '/')
        if rel.startswith('share') or rel.startswith('sheets'):
            continue
        small = any(k in rel for k in ('tour', 'robust', 'rotation', 'procdeath', 'debug', 'a11y'))
        for f in files:
            if not f.lower().endswith('.png'):
                continue
            p = os.path.join(root, f)
            try:
                im = Image.open(p).convert('RGB')
                sc = 0.5 if small else 0.6
                if max(im.size) * sc < 700:
                    sc = 1.0
                if sc < 1.0:
                    im = im.resize((max(1, int(im.width * sc)), max(1, int(im.height * sc))))
                q = p[:-4] + '.jpg'
                im.save(q, quality=72, optimize=True)
                saved += os.path.getsize(p) - os.path.getsize(q)
                os.remove(p); n += 1
            except Exception as e:  # noqa: BLE001
                print('skip', p, e)
print(f"images converted: {n}, saved {saved // 1024} KB")

la = os.path.join(out, 'logcat-all.txt')
if os.path.exists(la):
    with open(la, 'rb') as f, gzip.open(la + '.gz', 'wb', 6) as g:
        shutil.copyfileobj(f, g)
    os.remove(la)
for f in ('logcat-live.txt', 'logcat-dump.txt'):
    p = os.path.join(out, f)
    if os.path.exists(p):
        os.remove(p)

raw = []
for d in ('a11y', 'cpu', 'debug'):
    p = os.path.join(out, d)
    if os.path.isdir(p):
        for root, _, files in os.walk(p):
            raw += [os.path.join(root, f) for f in files if not f.endswith('.jpg')]
for f in os.listdir(out):
    p = os.path.join(out, f)
    if os.path.isfile(p) and (f.endswith('.xml') or f.startswith('sens-') or f.startswith('appwidget-') or f.startswith('notif-') or f.startswith('a11ytree-')):
        raw.append(p)
hi = os.path.join(out, 'hi')
if os.path.isdir(hi):
    raw += [os.path.join(hi, f) for f in os.listdir(hi)]
if raw:
    with tarfile.open(os.path.join(out, 'raw.tgz'), 'w:gz') as t:
        for p in raw:
            t.add(p, arcname=os.path.relpath(p, out))
    for p in raw:
        os.remove(p)
    for d in ('a11y', 'cpu', 'hi'):
        p = os.path.join(out, d)
        if os.path.isdir(p):
            for root, dirs, files in os.walk(p, topdown=False):
                if not os.listdir(root):
                    os.rmdir(root)
print(f"raw files packed: {len(raw)}")
