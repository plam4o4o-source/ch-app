# -*- coding: utf-8 -*-
"""Помощни проверки за вълна 15 (ВРЕМЕННО, debug/wave15-check).

  w15.py rows UI REGEX [DENS]       — възлите с text/content-desc по REGEX: рамки, dp и колко реда заемат
  w15.py checkables UI              — възлите checkable=true: клас, етикет (с наследниците), състояние
  w15.py field UI INDEX             — текстът на поредното (от 0) поле EditText и password=
  w15.py widget UI PKG              — заглавието, събитията и сроковете в уиджета на PKG (по resource-id)
  w15.py targets UI PKG DENS        — кликаемите възли на уиджета на PKG: id, етикет, размер в dp
  w15.py diff A.png B.png [x0 y0 x1 y1] — средна абсолютна разлика (0..255) на сивото, по желание в изрез
  w15.py bbox A.png [thr]           — рамката на светлите пиксели (снимка на черен фон)
  w15.py big UI PKG                 — най-голямият кликаем възел на PKG в горната половина (въртележката)
  w15.py section UI HEADING_REGEX   — текстовете под заглавие до следващото заглавие (по y), с повторенията
  w15.py hsections UI               — заглавията на „Начало“ (heading-подобни текстове) и броят текстове под всяко
  w15.py notif FILE PKG             — известията на PKG от `dumpsys notification --noredact`
  w15.py prefs-write OUT key=s:val|b:true|l:123 ...  — нов файл Preferences DataStore (protobuf)
  w15.py prefs-dump FILE            — съдържанието на файл Preferences DataStore
"""
import re
import sys
import xml.etree.ElementTree as ET
from collections import Counter

BR = re.compile(r'\[(-?\d+),(-?\d+)\]\[(-?\d+),(-?\d+)\]')


def load(p):
    try:
        return ET.parse(p).getroot()
    except Exception as e:  # noqa: BLE001
        print(f"(unreadable dump: {e})")
        sys.exit(0)


def b(n):
    m = BR.match(n.get('bounds', ''))
    return tuple(map(int, m.groups())) if m else None


def lab(n):
    d = (n.get('content-desc') or '').strip() or (n.get('text') or '').strip()
    if d:
        return d
    parts = []
    for c in n.iter('node'):
        t = (c.get('content-desc') or '').strip() or (c.get('text') or '').strip()
        if t and t not in parts:
            parts.append(t)
    return ' '.join(parts)


def cmd_rows(ui, rx, dens='420'):
    dens = float(dens) / 160
    r = re.compile(rx)
    hits = []
    for n in load(ui).iter('node'):
        s = (n.get('content-desc') or '') + '|' + (n.get('text') or '')
        if r.search(s) and b(n):
            hits.append((b(n), (n.get('content-desc') or n.get('text') or '')[:40], n.get('clickable')))
    tops = sorted({h[0][1] for h in hits})
    for (x0, y0, x1, y1), t, c in hits:
        print(f"  '{t}' [{x0},{y0}][{x1},{y1}] {(x1 - x0) / dens:.0f}x{(y1 - y0) / dens:.0f}dp clickable={c}")
    print(f"ROWS: {len(hits)} matches in {len(tops)} row(s) (tops {tops})")


def cmd_checkables(ui):
    for n in load(ui).iter('node'):
        if n.get('checkable') == 'true':
            kids = sum(1 for c in n.iter('node') if c is not n and c.get('checkable') == 'true')
            print(f"CHECKABLE class={n.get('class')} label='{lab(n)[:60]}' checked={n.get('checked')} clickable={n.get('clickable')} "
                  f"bounds={n.get('bounds')} nested-checkables={kids}")


def cmd_field(ui, idx):
    f = [n for n in load(ui).iter('node') if n.get('class') == 'android.widget.EditText']
    i = int(idx)
    if i >= len(f):
        print(f"FIELD {i}: absent ({len(f)} fields)")
        return
    n = f[i]
    print(f"FIELD {i}: text='{n.get('text')}' password={n.get('password')} focused={n.get('focused')} hint='{n.get('hint', '')}'")


def cmd_targets(ui, pkg, dens):
    dens = float(dens) / 160
    root = load(ui)
    out = []
    for n in root.iter('node'):
        if n.get('package') != pkg or n.get('clickable') != 'true' or not b(n):
            continue
        x0, y0, x1, y1 = b(n)
        rid = (n.get('resource-id') or '').split('/')[-1]
        out.append(f"{rid or '?'}('{lab(n)[:24]}') {(x1 - x0) / dens:.0f}x{(y1 - y0) / dens:.0f}dp")
    small = [o for o in out if re.search(r' (\d+)x(\d+)dp', o) and min(map(int, re.search(r' (\d+)x(\d+)dp', o).groups())) < 48]
    print(f"WIDGET TARGETS: {len(out)} clickable, <48dp: {len(small)}; " + '; '.join(out))


def cmd_widget(ui, pkg):
    pre = pkg + ':id/'
    got = {}
    for n in load(ui).iter('node'):
        rid = n.get('resource-id') or ''
        if rid.startswith(pre):
            t = (n.get('text') or '').strip() or (n.get('content-desc') or '').strip()
            got.setdefault(rid[len(pre):], []).append(t)
    one = lambda k: ' / '.join(x for x in got.get(k, []) if x) or '-'
    ev = [k for k in ('widget_event_title', 'widget_event2_title') if k in got]
    due = [one(k) for k in ('widget_due_line', 'widget_group1_text', 'widget_group2_text', 'widget_group3_text', 'widget_countdown') if k in got]
    print(f"WIDGET VIEW: title='{one('widget_title')}' logo={'widget_logo' in got} events={len(ev)} "
          f"[{one('widget_event_date')}: {one('widget_event_title')}] [{one('widget_event2_date')}: {one('widget_event2_title')}] "
          f"no-events={'widget_no_events' in got} loans={'widget_loans' in got} due={due} buttons={[k for k in ('widget_scan', 'widget_catalog') if k in got]}")


def gray(p):
    from PIL import Image
    return Image.open(p).convert('L')


def cmd_diff(a, bb, *box):
    from PIL import ImageChops, ImageStat
    x, y = gray(a), gray(bb)
    if box:
        x0, y0, x1, y1 = map(int, box)
        x, y = x.crop((x0, y0, x1, y1)), y.crop((x0, y0, x1, y1))
    print(f"{ImageStat.Stat(ImageChops.difference(x, y)).mean[0]:.1f}")


def cmd_bbox(a, thr='40'):
    im = gray(a).point(lambda v: 255 if v > int(thr) else 0)
    bx = im.getbbox()
    w, h = im.size
    if not bx:
        print("BBOX none")
        return
    x0, y0, x1, y1 = bx
    print(f"BBOX [{x0},{y0}][{x1},{y1}] {x1 - x0}x{y1 - y0} of {w}x{h} ({(x1 - x0) * (y1 - y0) / (w * h) * 100:.0f}% of the screen)")


def cmd_hspan(a, y, thr='40'):
    im = gray(a)
    w, _ = im.size
    row = [im.getpixel((x, int(y))) for x in range(w)]
    xs = [x for x, v in enumerate(row) if v > int(thr)]
    print(f"HSPAN x {xs[0]}..{xs[-1]} (width {xs[-1] - xs[0] + 1} of {w})" if xs else "HSPAN none")


def cmd_big(ui, pkg):
    root = load(ui)
    hs = [b(n) for n in root.iter('node') if b(n)]
    H = max(h[3] for h in hs) if hs else 2400
    best = None
    for n in root.iter('node'):
        if n.get('package') != pkg or not b(n):
            continue
        x0, y0, x1, y1 = b(n)
        if n.get('clickable') == 'true' and y0 < H / 2 and (y1 - y0) > H / 8 and (x1 - x0) * (y1 - y0) < 0.6 * (x1 + 1) * H:
            a = (x1 - x0) * (y1 - y0)
            if not best or a > best[0]:
                best = (a, (x0, y0, x1, y1), lab(n)[:80])
    if best:
        print(f"{best[1][0]} {best[1][1]} {best[1][2]} {best[1][3]} | {best[2]}")


def texts_with_y(root):
    out = []
    for n in root.iter('node'):
        t = (n.get('text') or '').strip() or (n.get('content-desc') or '').strip()
        if t and b(n):
            out.append((b(n)[1], b(n)[0], t, n))
    return sorted(out, key=lambda v: (v[0], v[1]))


HEADINGS = r'^(Последни новини|Нови постъпления|Предстоящи събития|За теб|Още новини|Latest news|New arrivals|Upcoming events|For you|More news|Моите книги|Следващ срок)$'


def cmd_section(ui, rx):
    root = load(ui)
    items = texts_with_y(root)
    hy = [y for y, _, t, _ in items if re.search(rx, t)]
    if not hy:
        print("SECTION: heading not on screen")
        return
    y0 = hy[0]
    nxt = [y for y, _, t, _ in items if y > y0 + 10 and re.search(HEADINGS, t)]
    y1 = nxt[0] if nxt else 10 ** 9
    titles = [t for y, _, t, n in items if y0 + 10 < y < y1 and not re.search(rx, t) and n.get('class') == 'android.widget.TextView'
              and len(t) > 3 and not re.search(r'^(Покажи|Show|Виж)', t)]
    c = Counter(titles)
    print(f"SECTION y {y0}..{y1}: {len(titles)} texts, repeated: {[f'{k} x{v}' for k, v in c.items() if v > 1]}")
    print("TITLES: " + ' | '.join(titles))


def cmd_hsections(ui):
    root = load(ui)
    items = texts_with_y(root)
    # Без лентата с разделите долу (последните ~260 px)
    bottom = max((b(n)[3] for n in root.iter('node') if b(n)), default=0)
    items = [i for i in items if i[0] < bottom - 260]
    hs = [(y, t) for y, _, t, _ in items if re.search(HEADINGS, t)]
    out = []
    for i, (y, t) in enumerate(hs):
        y1 = hs[i + 1][0] if i + 1 < len(hs) else 10 ** 9
        k = sum(1 for yy, _, tt, _ in items if y + 10 < yy < y1)
        out.append(f"{t}@{y}:{k}")
    print("HSECTIONS " + ' '.join(out))


def cmd_notif(f, pkg):
    t = open(f, encoding='utf-8', errors='replace').read()
    recs = re.split(r'\n\s*NotificationRecord\(', t)
    n = 0
    for r in recs[1:]:
        head = r.split('\n', 1)[0]
        if f'pkg={pkg}' not in head:
            continue
        n += 1
        g = lambda k: (re.search(rf'{k}=([^\n]*)', r) or [None, '-'])[1].strip()[:110]
        acts = re.findall(r'\[\d+\] "([^"]*)" -> PendingIntent', r)
        nid = (re.search(r'id=(-?\d+)', head) or [0, '?'])[1]
        chan = (re.search(r'channel=([^ ]+)', r) or [0, '?'])[1]
        print(f"NOTIF id={nid} channel={chan} "
              f"title={g('android.title')} text={g('android.text')} template={g('android.template')} "
              f"picture={'yes ' + g('android.picture') if 'android.picture=' in r else 'NO'} "
              f"largeIcon={'yes' if 'android.largeIcon=' in r else 'no'} actions={len(acts)} {acts}")
    print(f"NOTIF total for {pkg}: {n}")


# ---- Preferences DataStore (androidx.datastore.preferences.PreferencesProto) ----
def varint(v):
    out = bytearray()
    v &= (1 << 64) - 1
    while True:
        c = v & 0x7f
        v >>= 7
        if v:
            out.append(c | 0x80)
        else:
            out.append(c)
            return bytes(out)


def ld(field, data):
    return varint((field << 3) | 2) + varint(len(data)) + data


def value(kind, raw):
    if kind == 's':
        return ld(5, raw.encode('utf-8'))
    if kind == 'b':
        return varint(1 << 3 | 0) + varint(1 if raw.lower() == 'true' else 0)
    if kind == 'l':
        return varint(4 << 3 | 0) + varint(int(raw))
    if kind == 'i':
        return varint(3 << 3 | 0) + varint(int(raw))
    raise SystemExit(f"unknown kind {kind}")


def cmd_prefs_write(out, *kvs):
    data = b''
    for kv in kvs:
        k, v = kv.split('=', 1)
        kind, raw = v.split(':', 1)
        entry = ld(1, k.encode('utf-8')) + ld(2, value(kind, raw))
        data += ld(1, entry)
    open(out, 'wb').write(data)
    print(f"prefs written: {len(data)} bytes, {len(kvs)} keys")


def rd_varint(buf, i):
    s, v = 0, 0
    while True:
        c = buf[i]
        i += 1
        v |= (c & 0x7f) << s
        s += 7
        if c < 0x80:
            return v, i


def fields(buf):
    i = 0
    while i < len(buf):
        tag, i = rd_varint(buf, i)
        f, wt = tag >> 3, tag & 7
        if wt == 0:
            v, i = rd_varint(buf, i)
        elif wt == 2:
            ln, i = rd_varint(buf, i)
            v, i = buf[i:i + ln], i + ln
        elif wt == 5:
            v, i = buf[i:i + 4], i + 4
        elif wt == 1:
            v, i = buf[i:i + 8], i + 8
        else:
            raise ValueError(wt)
        yield f, wt, v


def cmd_prefs_dump(f):
    buf = open(f, 'rb').read()
    for fld, _, entry in fields(buf):
        if fld != 1:
            continue
        k, val = None, None
        for f2, _, v in fields(entry):
            if f2 == 1:
                k = v.decode('utf-8', 'replace')
            elif f2 == 2:
                for f3, wt, vv in fields(v):
                    val = vv.decode('utf-8', 'replace') if wt == 2 and f3 == 5 else vv
        print(f"PREF {k} = {val}")


if __name__ == '__main__':
    c, a = sys.argv[1], sys.argv[2:]
    {'widget': cmd_widget, 'rows': cmd_rows, 'checkables': cmd_checkables, 'field': cmd_field, 'targets': cmd_targets, 'diff': cmd_diff,
     'bbox': cmd_bbox, 'hspan': cmd_hspan, 'big': cmd_big, 'section': cmd_section, 'hsections': cmd_hsections, 'notif': cmd_notif,
     'prefs-write': cmd_prefs_write, 'prefs-dump': cmd_prefs_dump}[c](*a)
