"""Помощни разчитания за пълната проверка на изданието v1.2.33 (ВРЕМЕННО, debug/full-test-133).

  w20.py brief UI                 — брой текстове, отрязани с „…“, първите няколко текста
  w20.py a11yagg FILE...          — обобщение на a11y.py по всички екрани: уникалните проблеми и къде са
  w20.py sig A B                  — колко от текстовете на A са и в B (възстановяване след завъртане/смърт)
  w20.py notifraw FILE PKG        — суровите редове на известията на пакета (флагове, promot…, ongoing)
  w20.py appwe LOGCAT PKG...      — редовете W/E на процесите на приложението, групирани по таг и текст
  w20.py frames PNG...            — средно/разсейване по кадри (зареждане → съдържание)
  w20.py diaglog FILE             — локалният дневник на грешките (no_backup/diagnostics/log.jsonl)
  w20.py tilt BOX A1 A2 B C F     — фолиото: шум при един и същ наклон срещу разлика при друг наклон
  w20.py strip OUT CROP PNG...    — снимките една до друга (изрязани), за преглед
"""
import json, os, re, sys
from collections import Counter, defaultdict


def read(p):
    return open(p, encoding='utf-8', errors='replace').read()


def nodes(ui):
    import xml.etree.ElementTree as ET
    try:
        return list(ET.parse(ui).getroot().iter('node'))
    except Exception:
        return []


def cmd_brief(ui):
    ts = []
    for n in nodes(ui):
        for a in ('text', 'content-desc'):
            v = (n.get(a) or '').strip()
            if v:
                ts.append(v)
    ell = [t for t in ts if t.endswith('…') and len(t) > 3]
    print(f"texts={len(ts)} ellipsized={len(ell)}" + (f" [{'; '.join(e[:30] for e in ell[:3])}]" if ell else '')
          + " | " + ' · '.join(t[:28] for t in ts[:6]))


def cmd_a11yagg(*files):
    per = defaultdict(lambda: defaultdict(set))  # kind -> item -> screens
    screens, failed = 0, []
    totals = Counter()
    for f in files:
        cur = None
        for l in read(f).splitlines():
            m = re.match(r'A11Y (\S+): (.*)$', l)
            if m:
                cur = m.group(1)
                if 'dump failed' in m.group(2) or 'unreadable' in m.group(2):
                    failed.append(cur)
                    continue
                screens += 1
                for k, v in re.findall(r'(MISSING|DOUBLE|SAME-LABEL|SMALL\(<48dp\))=(\d+)', m.group(2)):
                    totals[k] += int(v)
                continue
            m = re.match(r'\s+(MISSING|DOUBLE|SAME-LABEL|SMALL): (.*)$', l)
            if m and cur:
                kind, rest = m.groups()
                rest = re.sub(r' \(\+\d+\)$', '', rest)
                for it in rest.split('; '):
                    key = it
                    if kind == 'SMALL':
                        key = re.sub(r" \d+x\d+dp$", '', it)
                    per[kind][key.strip()].add(cur.split('/', 1)[-1])
    print(f"A11Y AGG: {screens} screens analysed, {len(failed)} dumps failed; totals " + ' '.join(f"{k}={v}" for k, v in totals.items()))
    if failed:
        print(f"   dump failed: {', '.join(failed[:20])}")
    for kind in ('MISSING', 'SMALL', 'SAME-LABEL', 'DOUBLE'):
        items = sorted(per[kind].items(), key=lambda kv: -len(kv[1]))
        print(f"== {kind}: {len(items)} distinct")
        for k, scr in items[:25]:
            s = sorted(scr)
            print(f"   {k[:70]}  [{len(s)} screens: {', '.join(s[:6])}{' …' if len(s) > 6 else ''}]")


def texts_of(ui):
    out = set()
    for n in nodes(ui):
        for a in ('text', 'content-desc'):
            v = (n.get(a) or '').strip()
            if v and not re.fullmatch(r'[\d:., %]+', v):
                out.add(v)
    return out


def cmd_sig(a, b):
    ta, tb = texts_of(a), texts_of(b)
    if not ta:
        print('before: no texts')
        return
    kept = len(ta & tb) / len(ta)
    lost = sorted(ta - tb, key=len)[:4]
    print(f"kept {kept * 100:.0f}% of {len(ta)} texts" + (f"; lost e.g. {[l[:30] for l in lost]}" if kept < 0.999 else ''))


def cmd_notifraw(f, pkg):
    t = read(f)
    recs = re.split(r'\n\s*NotificationRecord\(', t)
    for r in recs[1:]:
        head = r.split('\n', 1)[0]
        if f'pkg={pkg}' not in head:
            continue
        print('REC ' + head.strip()[:200])
        for l in r.splitlines()[1:]:
            s = l.strip()
            if re.search(r'flags=|promot|Promot|ongoing|Ongoing|importance|mChannel|chip|shortCritical|template|isColorized|group', s):
                print('   ' + s[:220])
    prefs = [l.strip() for l in t.splitlines() if re.search(r'romot', l) and pkg in l]
    for l in prefs[:6]:
        print('PREF ' + l[:240])


def cmd_appwe(path, *pkgs):
    text = read(path)
    pids = set()
    for p in pkgs:
        pids |= set(re.findall(r'Start proc (\d+):' + re.escape(p) + r'(?:[/ :]|$)', text, re.M))
    groups = Counter()
    first = {}
    for l in text.splitlines():
        m = re.match(r'^\S+\s+(\S+)\s+(\d+)\s+\d+\s+([WEF])\s+([^:]+?)\s*:\s?(.*)$', l)
        if not m or m.group(2) not in pids:
            continue
        lvl, tag, msg = m.group(3), m.group(4).strip(), m.group(5)
        norm = re.sub(r'0x[0-9a-f]+|\d+', '#', msg)[:110]
        k = f"{lvl}/{tag}: {norm}"
        groups[k] += 1
        first.setdefault(k, l[:60])
    print(f"APP W/E lines (pids {sorted(pids, key=int)}): {sum(groups.values())} in {len(groups)} groups")
    for k, v in groups.most_common(40):
        print(f"   {v:5d}  {k}")


def cmd_frames(*ps):
    from PIL import Image, ImageStat
    for p in ps:
        im = Image.open(p).convert('L')
        st = ImageStat.Stat(im)
        print(f"{os.path.basename(p)} mean={st.mean[0]:.1f} std={st.stddev[0]:.1f}")


def cmd_diaglog(f):
    if not os.path.exists(f) or os.path.getsize(f) == 0:
        print('DIAGLOG: empty (no entries)')
        return
    es = []
    for l in read(f).splitlines():
        try:
            es.append(json.loads(l))
        except Exception:
            pass
    c = Counter()
    for e in es:
        tag = e.get('tag') or e.get('t') or '?'
        msg = (e.get('message') or e.get('m') or e.get('error') or e.get('type') or '')
        if isinstance(msg, list):
            msg = msg[0] if msg else ''
        c[f"{tag}: {str(msg)[:120]}"] += 1
    print(f"DIAGLOG: {len(es)} entries, {len(c)} distinct")
    for k, v in c.most_common(20):
        print(f"   {v:3d}  {k}")
    if es:
        print('   keys of an entry: ' + ', '.join(sorted(es[-1].keys())))


def cmd_tilt(box, a1, a2, b, c, f):
    from PIL import Image, ImageChops, ImageStat
    bx = tuple(int(v) for v in re.split(r'[ ,]+', box.strip()))
    def im(p):
        return Image.open(p).convert('RGB').crop(bx)
    def d(x, y):
        return sum(ImageStat.Stat(ImageChops.difference(im(x), im(y))).mean) / 3
    noise = d(a1, a2)
    ab, bc, af = d(a2, b), d(b, c), d(a1, f)
    print(f"noise (same tilt, 1.5 s apart)={noise:.2f}; tilt A→B={ab:.2f}; B→C={bc:.2f}; A→flat={af:.2f}; "
          f"verdict: {'FOIL CHANGES WITH TILT' if ab > max(1.0, 3 * noise) else 'no clear change'}")


def cmd_strip(out, crop, *ps):
    from PIL import Image
    ps = [p for p in ps if os.path.exists(p)]
    if not ps:
        return
    c = None if crop == 'all' else tuple(int(v) for v in re.split(r'[ ,]+', crop.strip()))
    ims = [Image.open(p).convert('RGB') for p in ps]
    if c:
        ims = [i.crop(c) for i in ims]
    h = max(i.height for i in ims)
    sc = min(1.0, 900 / h)
    ims = [i.resize((max(1, int(i.width * sc)), max(1, int(i.height * sc)))) for i in ims]
    W = sum(i.width for i in ims) + 6 * (len(ims) - 1)
    sheet = Image.new('RGB', (W, max(i.height for i in ims)), (255, 0, 255))
    x = 0
    for i in ims:
        sheet.paste(i, (x, 0))
        x += i.width + 6
    os.makedirs(os.path.dirname(out) or '.', exist_ok=True)
    sheet.save(out)
    print(out)


if __name__ == '__main__':
    c, args = sys.argv[1], sys.argv[2:]
    try:
        globals()['cmd_' + c](*args)
    except Exception as e:  # noqa: BLE001
        print(f"w20.py {c}: error {type(e).__name__}: {e}")
