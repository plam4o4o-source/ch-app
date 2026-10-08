import re, sys, xml.etree.ElementTree as ET
path, attr, value = sys.argv[1], sys.argv[2], sys.argv[3]
try:
    root = ET.parse(path).getroot()
except Exception:
    sys.exit(0)
for n in root.iter('node'):
    if value.lower() in (n.get(attr) or '').lower():
        m = re.match(r'\[(\d+),(\d+)\]\[(\d+),(\d+)\]', n.get('bounds', ''))
        if m:
            x1, y1, x2, y2 = map(int, m.groups())
            print((x1 + x2) // 2, (y1 + y2) // 2)
            break
