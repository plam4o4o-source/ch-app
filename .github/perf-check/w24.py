# -*- coding: utf-8 -*-
"""Помощни команди за w24.sh (ВРЕМЕННО, debug/w24-check, резултати в emulator-results/v138).

  w24.py quizopts UI.xml H W   — отговорите във викторината: "A x y<TAB>текст" (по един на ред), "N x y" — „Напред“
  w24.py manifest APK OUT URL VC NAME [DMSPEC ...]
                               — update.json за локалния сървър; DMSPEC = minSdk:maxSdk:файл[:sha256] (sha — по желание,
                                 за нарочно грешна сума); maxSdk може да е празно
  w24.py frames DIR            — средната промяна между поредните кадри (има ли движение)
"""
import hashlib
import json
import os
import re
import sys
import xml.etree.ElementTree as ET


def bounds(n):
    m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
    return tuple(map(int, m.groups())) if m else None


def alltext(n):
    return ' '.join(((c.get('text') or '') + ' ' + (c.get('content-desc') or '')).strip() for c in n.iter('node')).strip()


def cmd_quizopts(path, H, W):
    try:
        root = ET.parse(path).getroot()
    except Exception:  # noqa: BLE001
        return
    H, W = int(H), int(W)
    nxt = None
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
            print(f"A {c[0]} {c[1]}\t{t}")
    if nxt:
        print('N', *nxt)


def cmd_quizres(path):
    """След отговор: RIGHT, WRONG<TAB>верният отговор или UNKNOWN."""
    try:
        root = ET.parse(path).getroot()
    except Exception:  # noqa: BLE001
        print('UNKNOWN')
        return
    texts = [(n.get('text') or '') for n in root.iter('node')] + [(n.get('content-desc') or '') for n in root.iter('node')]
    for t in texts:
        m = re.match(r'^(Верният отговор е|The correct answer is): (.*)$', t.strip())
        if m:
            print('WRONG\t' + m.group(2).strip())
            return
    if any(t.strip() in ('Вярно!', 'Correct!') for t in texts):
        print('RIGHT')
        return
    print('UNKNOWN')


def sha(path):
    h = hashlib.sha256()
    with open(path, 'rb') as f:
        for b in iter(lambda: f.read(1 << 20), b''):
            h.update(b)
    return h.hexdigest()


def cmd_manifest(apk, out, url, vc, name, *dms):
    d = {'versionCode': int(vc), 'versionName': name, 'apkUrl': url + '/' + os.path.basename(apk),
         'sha256': sha(apk), 'size': os.path.getsize(apk), 'minSdk': 26,
         'notes': '• Тестово обновяване (емулатор)', 'notes_en': '• Test update (emulator)'}
    dm = []
    for spec in dms:
        p = spec.split(':')
        mn, mx, f = int(p[0]), (int(p[1]) if p[1] else None), p[2]
        s = p[3] if len(p) > 3 and p[3] else sha(f)
        dm.append({'minSdk': mn, 'maxSdk': mx, 'url': url + '/' + os.path.basename(f), 'sha256': s, 'size': os.path.getsize(f)})
    if dm:
        d['dm'] = dm
    json.dump(d, open(out, 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
    print(f"manifest: vc {vc} {os.path.basename(apk)} {d['size']} B; dm: " + ', '.join(f"{e['minSdk']}-{e['maxSdk']} {os.path.basename(e['url'])} {e['size']} B sha {e['sha256'][:10]}" for e in dm))


def cmd_frames(d):
    try:
        from PIL import Image, ImageChops, ImageStat
    except Exception:  # noqa: BLE001
        print('frames: no PIL')
        return
    fs = sorted(f for f in os.listdir(d) if f.endswith('.png'))
    prev, diffs = None, []
    for f in fs:
        im = Image.open(os.path.join(d, f)).convert('L').resize((180, 400))
        if prev is not None:
            diffs.append(ImageStat.Stat(ImageChops.difference(im, prev)).mean[0])
        prev = im
    moving = sum(1 for x in diffs if x > 0.8)
    print(f"frames {len(fs)}; changed between consecutive frames: {moving}; max diff {max(diffs) if diffs else 0:.1f}")


if __name__ == '__main__':
    c, *a = sys.argv[1:]
    {'quizopts': cmd_quizopts, 'quizres': cmd_quizres, 'manifest': cmd_manifest, 'frames': cmd_frames}[c](*a)
