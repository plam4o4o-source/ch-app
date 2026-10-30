"""Търси възел в uiautomator dump и печата центъра му ("x y").

  find.py ui.xml ATTR VALUE [--last] [--miny N] [--maxy N]
      VALUE: '=точно' или подниз; няколко варианта с '|' (напр. '=Home|=Начало').
      --last: последното съвпадение; --miny/--maxy: само възли с център между тези y.
  find.py ui.xml --state ATTR VALUE [--miny N] [--maxy N]
      състоянието на първото съвпадение: "checked=… selected=… bounds=…" (или нищо).
  find.py ui.xml --tab VALUE REF
      етикетът VALUE в лентата с разделите: съвпадението на един ред (долна лента) или в една
      колона (страничната лента) с етикета REF (напр. '=More|=Още'); иначе последното съвпадение.
  find.py ui.xml --first-clickable MIN_Y MAX_Y [MIN_H]
      първият кликаем възел, чийто горен ръб е между MIN_Y и MAX_Y и е висок поне MIN_H px.
  find.py ui.xml --clickables MIN_Y MAX_Y [MIN_H]
      всички такива възли, по един център на ред.
  find.py ui.xml --texts
      всички текстове (text и content-desc), по един на ред — за сравнение между екрани.
  find.py ui.xml --all ATTR VALUE [--miny N] [--maxy N]
      центровете на всички съвпадения, по един на ред.
  find.py ui.xml --bounds ATTR VALUE
      границите на първото съвпадение: "left top right bottom".
"""
import re, sys, xml.etree.ElementTree as ET

path = sys.argv[1]
try:
    root = ET.parse(path).getroot()
except Exception:
    sys.exit(0)
args = sys.argv[2:]


def opt(name, default=None):
    if name in args:
        i = args.index(name)
        return int(args[i + 1])
    return default


def bounds(n):
    m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
    return tuple(map(int, m.groups())) if m else None


def center(b):
    return ((b[0] + b[2]) // 2, (b[1] + b[3]) // 2)


def fmt(b):
    x, y = center(b)
    return f"{x} {y}"


def matches(attr, value):
    alts = [(v.startswith('='), v.lstrip('=').lower()) for v in value.split('|')]
    lo, hi = opt('--miny', -1), opt('--maxy', 10 ** 9)
    out = []
    for n in root.iter('node'):
        got = (n.get(attr) or '').lower()
        if any((got == w) if ex else (w and w in got) for ex, w in alts):
            b = bounds(n)
            if b and b[2] > b[0] and b[3] > b[1] and lo <= center(b)[1] <= hi:
                out.append((n, b))
    return out


mode = args[0]
parent_of = {c: p for p in root.iter() for c in p}


def widget_roots(pkg):
    """Корените на уиджетите на пакета: най-горните възли с resource-id от пакета (или
    android:id/background над тях — коренът на RemoteViews)."""
    pre = pkg + ':id/'
    tops = []
    for n in root.iter('node'):
        if (n.get('resource-id') or '').startswith(pre):
            m, top = n, n
            while m is not None:
                rid = m.get('resource-id') or ''
                if rid.startswith(pre) or rid == 'android:id/background':
                    top = m
                m = parent_of.get(m)
            if all(top is not t for t in tops):
                tops.append(top)
    return tops


if mode == '--widget-texts':
    # find.py ui.xml --widget-texts PKG — текстовете в уиджетите на пакета (по реда в дървото)
    seen = []
    for r in widget_roots(args[1]):
        for n in r.iter('node'):
            for a in ('text', 'content-desc'):
                v = (n.get(a) or '').strip()
                if v and v not in seen:
                    seen.append(v)
    print('\n'.join(seen))
    sys.exit(0)
if mode == '--widget-bounds':
    # find.py ui.xml --widget-bounds PKG — "left top right bottom" на първия уиджет на пакета
    rs = widget_roots(args[1])
    if rs:
        b = bounds(rs[0])
        if b:
            print(*b)
    sys.exit(0)
if mode == '--nodes':
    # find.py ui.xml --nodes ATTR VALUE — атрибутите на всяко съвпадение (за достъпността)
    for n, b in matches(args[1], args[2]):
        print(' '.join(f'{a}="{n.get(a)}"' for a in ('class', 'text', 'content-desc', 'checkable', 'checked', 'clickable', 'selected', 'bounds')))
    sys.exit(0)
if mode == '--dupes':
    # find.py ui.xml --dupes — content-desc, който повтаря видим текст на същия екран
    # (TalkBack чете заглавието два пъти); брой и стойностите.
    texts = {(n.get('text') or '').strip() for n in root.iter('node')} - {''}
    d = [(n.get('content-desc') or '').strip() for n in root.iter('node')]
    d = [v for v in d if v and v in texts]
    print(f"{len(d)} {' | '.join(d[:12])}")
    sys.exit(0)
if mode == '--radio':
    # find.py ui.xml --radio DENSITY — височината (px и dp) на редовете с роля RadioButton
    dens = float(args[1]) / 160
    for n in root.iter('node'):
        if 'RadioButton' in (n.get('class') or ''):
            b = bounds(n)
            if b:
                lab = (n.get('text') or n.get('content-desc') or '').strip()
                if not lab:
                    lab = ' '.join((c.get('text') or '').strip() for c in n.iter('node') if c is not n and c.get('text'))
                print(f"{b[3] - b[1]}px={((b[3] - b[1]) / dens):.0f}dp clickable={n.get('clickable')} '{lab[:30]}'")
    sys.exit(0)
if mode == '--cells':
    # find.py ui.xml --cells MIN_Y MAX_Y MIN_SIZE — кликаеми почти квадратни клетки (снимките в албум),
    # горният ръб между MIN_Y и MAX_Y, страна поне MIN_SIZE px; по един център на ред.
    lo, hi, min_s = int(args[1]), int(args[2]), int(args[3])
    for n in root.iter('node'):
        b = bounds(n)
        if n.get('clickable') != 'true' or not b or not (lo <= b[1] <= hi):
            continue
        w, h = b[2] - b[0], b[3] - b[1]
        if w >= min_s and h >= min_s and abs(w - h) <= 0.25 * w:
            print(fmt(b))
    sys.exit(0)
if mode in ('--first-clickable', '--clickables'):
    lo, hi = int(args[1]), int(args[2])
    min_h = int(args[3]) if len(args) > 3 else 80
    for n in root.iter('node'):
        b = bounds(n)
        if n.get('clickable') == 'true' and b and lo <= b[1] <= hi and b[3] - b[1] >= min_h and b[2] - b[0] > 200:
            print(fmt(b))
            if mode == '--first-clickable':
                break
elif mode == '--texts':
    for n in root.iter('node'):
        for a in ('text', 'content-desc'):
            v = (n.get(a) or '').strip()
            if v:
                print(v)
elif mode == '--state':
    # Състоянието е на кликаемия възел (сегмент, chip), а текстът често е в дете — търси се нагоре.
    parent = {c: p for p in root.iter() for c in p}
    hits = matches(args[1], args[2])
    if hits:
        n, b = hits[0]
        m = n
        while m is not None and m.get('clickable') != 'true' and m.get('checkable') != 'true':
            m = parent.get(m)
        m = m if m is not None else n
        print(f"checked={m.get('checked')} selected={m.get('selected')} bounds={m.get('bounds')}")
elif mode == '--all':
    for n, b in matches(args[1], args[2]):
        print(fmt(b))
elif mode == '--bounds':
    hits = matches(args[1], args[2])
    if hits:
        print(*hits[0][1])
elif mode == '--tab':
    hits = matches('text', args[1])
    refs = matches('text', args[2])
    pick = None
    if hits and refs:
        rx, ry = center(refs[-1][1])
        for n, b in hits:
            x, y = center(b)
            if abs(y - ry) <= 20 or abs(x - rx) <= 20:
                pick = b
    if pick is None and hits:
        pick = hits[-1][1]
    if pick:
        print(fmt(pick))
else:
    hits = matches(args[0], args[1])
    if hits:
        print(fmt(hits[-1][1] if '--last' in args else hits[0][1]))
