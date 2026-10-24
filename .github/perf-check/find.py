"""Търси възел в uiautomator dump и печата центъра му ("x y").

  find.py ui.xml ATTR VALUE [--last]
      VALUE: '=точно' или подниз; няколко варианта с '|' (напр. '=Home|=Начало').
      --last: последното съвпадение (напр. етикетът в долната лента, а не бутон със същия текст).
  find.py ui.xml --first-clickable MIN_Y MAX_Y [MIN_H]
      първият кликаем възел, чийто горен ръб е между MIN_Y и MAX_Y и е висок поне MIN_H px.
"""
import re, sys, xml.etree.ElementTree as ET

path = sys.argv[1]
try:
    root = ET.parse(path).getroot()
except Exception:
    sys.exit(0)


def bounds(n):
    m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
    return tuple(map(int, m.groups())) if m else None


def center(b):
    return f"{(b[0] + b[2]) // 2} {(b[1] + b[3]) // 2}"


if sys.argv[2] == '--first-clickable':
    lo, hi = int(sys.argv[3]), int(sys.argv[4])
    min_h = int(sys.argv[5]) if len(sys.argv) > 5 else 80
    for n in root.iter('node'):
        b = bounds(n)
        if n.get('clickable') == 'true' and b and lo <= b[1] <= hi and b[3] - b[1] >= min_h and b[2] - b[0] > 200:
            print(center(b))
            break
    sys.exit(0)

attr, value = sys.argv[2], sys.argv[3]
last = '--last' in sys.argv[4:]
alts = []
for v in value.split('|'):
    alts.append((v.startswith('='), v.lstrip('=').lower()))
hit = None
for n in root.iter('node'):
    got = (n.get(attr) or '').lower()
    if any((got == w) if ex else (w and w in got) for ex, w in alts):
        b = bounds(n)
        if b and b[2] > b[0] and b[3] > b[1]:
            hit = b
            if not last:
                break
if hit:
    print(center(hit))
