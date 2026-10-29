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
