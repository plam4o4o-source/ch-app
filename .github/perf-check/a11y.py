"""Достъпност от uiautomator dump (само възлите на приложението).

  a11y.py ui.xml DENSITY SCREEN_W SCREEN_H LABEL

- MISSING: кликаем/отметваем възел без text и content-desc в себе си и в наследниците (TalkBack
  казва само „бутон“). Пропускат се контейнерите, по-големи от 90% от екрана.
- DOUBLE: възел с content-desc, който повтаря текст на свой наследник (TalkBack го чете два пъти),
  и кликаем възел, чийто етикет съдържа един и същ текст два пъти.
- SAME-LABEL: различни кликаеми цели с еднакъв етикет на един екран (неясно накъде водят).
- SMALL: кликаема цел под 48 dp по ширина или височина (без отрязаните от ръба на екрана).
"""
import re, sys, xml.etree.ElementTree as ET
from collections import Counter

path, dens, sw, sh, label = sys.argv[1], float(sys.argv[2]) / 160, int(sys.argv[3]), int(sys.argv[4]), sys.argv[5]
PKG = 'org.chyavorec.app'
try:
    root = ET.parse(path).getroot()
except Exception as e:  # noqa: BLE001
    print(f"A11Y {label}: dump unreadable ({e})"); sys.exit(0)


def b(n):
    m = re.match(r'\[(-?\d+),(-?\d+)\]\[(-?\d+),(-?\d+)\]', n.get('bounds', ''))
    return tuple(map(int, m.groups())) if m else None


def own(n):
    return [(n.get(a) or '').strip() for a in ('content-desc', 'text') if (n.get(a) or '').strip()]


def eff(n):
    """Етикетът, който TalkBack би прочел: content-desc, иначе текстовете на наследниците."""
    d = (n.get('content-desc') or '').strip()
    if d:
        return d
    parts = []
    for c in n.iter('node'):
        t = (c.get('content-desc') or '').strip() or (c.get('text') or '').strip()
        if t and t not in parts:
            parts.append(t)
    return ' '.join(parts)


nodes = [n for n in root.iter('node') if n.get('package') == PKG]
act = [n for n in nodes if (n.get('clickable') == 'true' or n.get('checkable') == 'true' or n.get('long-clickable') == 'true')
       and n.get('enabled') != 'false' and b(n) and b(n)[2] > b(n)[0] and b(n)[3] > b(n)[1]]
missing, small, double = [], [], []
for n in act:
    x0, y0, x1, y1 = b(n)
    w, h = x1 - x0, y1 - y0
    if w * h > 0.9 * sw * sh:
        continue
    lab = eff(n)
    cls = (n.get('class') or '').split('.')[-1]
    if not lab:
        missing.append(f"{cls}@[{x0},{y0}][{x1},{y1}]")
    edge = y0 <= 0 or y1 >= sh or x0 <= 0 or x1 >= sw
    wd, hd = w / dens, h / dens
    if (wd < 47.5 or hd < 47.5) and not edge:
        small.append(f"'{lab[:30]}' {wd:.0f}x{hd:.0f}dp")
for n in nodes:
    d = (n.get('content-desc') or '').strip()
    if d:
        kids = {(c.get('text') or '').strip() for c in n.iter('node') if c is not n}
        if d in kids:
            double.append(f"'{d[:40]}' (desc + child text)")
for n in act:
    texts = [(c.get('text') or '').strip() for c in n.iter('node') if (c.get('text') or '').strip()]
    rep = [t for t, k in Counter(texts).items() if k > 1 and len(t) > 2]
    if rep:
        double.append(f"'{rep[0][:40]}' x{Counter(texts)[rep[0]]} inside one target")
labs = Counter(eff(n) for n in act if eff(n))
same = [f"'{k[:30]}' x{v}" for k, v in labs.items() if v > 1]
print(f"A11Y {label}: targets={len(act)} MISSING={len(missing)} DOUBLE={len(double)} SAME-LABEL={len(same)} SMALL(<48dp)={len(small)}")
for name, lst in (('MISSING', missing), ('DOUBLE', double), ('SAME-LABEL', same), ('SMALL', small)):
    if lst:
        print(f"   {name}: " + '; '.join(lst[:10]) + (f" (+{len(lst) - 10})" if len(lst) > 10 else ''))
