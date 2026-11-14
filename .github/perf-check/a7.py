"""Помощни за проверката на издаденото v1.2.36 (ВРЕМЕННО, debug/a7-check).

  a7.py pick OUT_DIR N FRAME...   — от кадрите на запис избира N последователни около началото на движението
                                    (първият кадър, който се различава от предишния), копира ги като 01.png…
  a7.py same A B                  — средна разлика между две снимки (0–255), за „промени ли се екранът“
"""
import os, shutil, sys


def load(p, w=108):
    from PIL import Image
    im = Image.open(p).convert('L')
    return im.resize((w, max(1, im.height * w // im.width)))


def diff(a, b):
    import numpy as np
    x = np.asarray(a, dtype='int16'); y = np.asarray(b, dtype='int16')
    if x.shape != y.shape:
        return 255.0
    return float(abs(x - y).mean())


def pick(out, n, frames):
    os.makedirs(out, exist_ok=True)
    if not frames:
        print('no frames'); return
    small = [load(f) for f in frames]
    d = [0.0] + [diff(small[i - 1], small[i]) for i in range(1, len(small))]
    start = next((i for i, v in enumerate(d) if v > 1.2), None)
    if start is None:
        start = max(range(len(d)), key=lambda i: d[i])
    a = max(0, start - 1); b = min(len(frames), a + n); a = max(0, b - n)
    for k, i in enumerate(range(a, b), 1):
        shutil.copy(frames[i], os.path.join(out, '%02d.png' % k))
    moving = [i for i, v in enumerate(d) if v > 1.2]
    span = (moving[-1] - moving[0] + 1) if moving else 0
    print('frames=%d motion-start=#%d moving-frames=%d (span %d) kept #%d..#%d; diffs: %s' % (
        len(frames), start, len(moving), span, a, b - 1, ' '.join('%.0f' % v for v in d[a:b])))


if __name__ == '__main__':
    if sys.argv[1] == 'pick':
        pick(sys.argv[2], int(sys.argv[3]), sys.argv[4:])
    elif sys.argv[1] == 'same':
        print('%.2f' % diff(load(sys.argv[2], 216), load(sys.argv[3], 216)))
