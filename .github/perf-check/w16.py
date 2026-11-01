# -*- coding: utf-8 -*-
"""Помощни проверки за вълна 16 (ВРЕМЕННО, debug/wave16-check).

  w16.py site OUTDIR                 — index.json и news.json от сайта: събитията (с мястото) и текстовете с емоджи
  w16.py switch UI LABEL             — редът с превключвателя: enabled/checked/клас и текстовете в него
  w16.py prompt UI                   — системната проверка (BiometricPrompt / екранът за ПИН): пакет, текстове, поле
  w16.py texth UI DENS [MAXN]        — текстовите възли: височина в dp (за размера на шрифта), scrollable възли
  w16.py ratio A.xml B.xml           — височините на еднаквите текстове в B спрямо A (медиана, мин., макс.)
  w16.py nodeh UI TEXT DENS          — височината (dp) на първия възел с този текст (подниз)
  w16.py notifp FILE PKG             — известията на PKG: заглавие, текст, ход (progress), бутони
  w16.py prefs-add FILE key=s:val …  — добавя ключове към съществуващ файл Preferences DataStore
  w16.py whatsnew-json UPDATE.json   — записът „Какво ново“ (pending_whats_new) от update.json
  w16.py frames DIR Y0 Y1            — кадрите от screenrecord: кои се различават от предишния (в изрез y0..y1)
  w16.py lang TEXT…                  — дял на кирилицата в текстовете на уиджета
  w16.py tabsel UI                   — избраният раздел (selected=true) в лентата/релсата
  w16.py panes UI W                  — колко текста има вляво и вдясно (списък и детайл един до друг?)
  w16.py tree FILE [CAROUSEL_LABEL]  — дървото от тестовата услуга (PCDUMP): безименни бутони, действия, заглавия на панели
  w16.py cards UI PLACES.txt         — картите в списъка със събития: текстовете им и дали съдържат мястото
  w16.py carousel UI PKG DENS SCALE  — заглавието в голямата карта на въртележката: височина и брой редове
  w16.py widgetcol UI PKG DENS       — колоната със събитията в уиджета: размер в dp
"""
import html
import json
import os
import re
import statistics
import sys
import xml.etree.ElementTree as ET

BR = re.compile(r'\[(-?\d+),(-?\d+)\]\[(-?\d+),(-?\d+)\]')
EMOJI = re.compile('[\U0001F000-\U0001FAFF☀-➿⏩-⏿⭐⭕⤴⤵〰〽️]')


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


def parents(root):
    return {c: p for p in root.iter() for c in p}


# ---------------------------------------------------------------- сайтът
def cmd_site(out):
    import urllib.request
    os.makedirs(out, exist_ok=True)

    def get(path):
        req = urllib.request.Request('https://chyavorec.org' + path, headers={'User-Agent': 'perf-check'})
        return urllib.request.urlopen(req, timeout=40).read().decode('utf-8')

    events, emoji = [], []
    try:
        idx = json.loads(get('/javora/index.json'))
        for d in idx.get('docs', []):
            txt = ' '.join(str(d.get(k) or '') for k in ('title', 'excerpt', 'place'))
            if d.get('type') == 'event':
                events.append({k: d.get(k) for k in ('id', 'title', 'place', 'day', 'month', 'monthNum', 'excerpt', 'url')})
            if EMOJI.search(txt):
                emoji.append({'kind': d.get('type'), 'id': d.get('id'), 'url': d.get('url'), 'title': d.get('title'),
                              'emoji': ''.join(sorted(set(EMOJI.findall(txt))))})
    except Exception as e:  # noqa: BLE001
        print(f"SITE index.json failed: {e}")
    try:
        news = json.loads(get('/data/news.json'))
        items = news if isinstance(news, list) else news.get('items') or news.get('news') or news.get('posts') or []
        for a in items:
            if not isinstance(a, dict):
                continue
            txt = ' '.join(str(a.get(k) or '') for k in ('title', 'excerpt', 'summary', 'content', 'html', 'text', 'body'))
            if EMOJI.search(txt):
                emoji.append({'kind': 'news', 'id': a.get('id') or a.get('slug'), 'url': a.get('url') or a.get('link'),
                              'title': a.get('title'), 'emoji': ''.join(sorted(set(EMOJI.findall(txt))))})
        print(f"SITE news.json: {len(items)} items; keys of the first: {sorted(items[0].keys()) if items and isinstance(items[0], dict) else '-'}")
    except Exception as e:  # noqa: BLE001
        print(f"SITE news.json failed: {e}")
    json.dump(events, open(os.path.join(out, 'events.json'), 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
    json.dump(emoji, open(os.path.join(out, 'emoji.json'), 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
    with open(os.path.join(out, 'places.txt'), 'w', encoding='utf-8') as f:
        for e in events:
            if (e.get('place') or '').strip():
                f.write(e['place'].strip() + '\n')
    withp = [e for e in events if (e.get('place') or '').strip()]
    print(f"SITE events: {len(events)}, with a place: {len(withp)}; places: {sorted({e['place'].strip() for e in withp})[:8]}")
    for e in emoji[:12]:
        print(f"EMOJI {e['kind']} id={e['id']} url={e['url']} {e['emoji']} '{(e['title'] or '')[:60]}'")
    # Редовете за скрипта: вид|id|url
    with open(os.path.join(out, 'emoji.txt'), 'w', encoding='utf-8') as f:
        for e in emoji:
            t = re.sub(r'[|\n]', ' ', e['title'] or '')
            f.write(f"{e['kind']}|{e['id'] or ''}|{e['url'] or ''}|{t}\n")


# ---------------------------------------------------------------- превключвател, проверка
def cmd_switch(ui, label):
    root = load(ui)
    par = parents(root)
    for n in root.iter('node'):
        if label.lower() in ((n.get('text') or '') + (n.get('content-desc') or '')).lower():
            m = n
            while m is not None and m.get('checkable') != 'true' and m.get('clickable') != 'true':
                m = par.get(m)
            m = m if m is not None else n
            texts = [t for t in ((c.get('text') or '').strip() for c in m.iter('node')) if t]
            print(f"SWITCH class={m.get('class')} checkable={m.get('checkable')} checked={m.get('checked')} enabled={m.get('enabled')} "
                  f"clickable={m.get('clickable')} bounds={m.get('bounds')} texts={texts}")
            return
    print(f"SWITCH '{label}': not on screen")


def cmd_prompt(ui):
    root = load(ui)
    pk = sorted({n.get('package') for n in root.iter('node') if n.get('package')})
    texts = [t for t in ((n.get('text') or n.get('content-desc') or '').strip() for n in root.iter('node')) if t]
    pw = [n for n in root.iter('node') if n.get('password') == 'true']
    print(f"PROMPT packages={pk} password-fields={len(pw)} texts={texts[:12]}")


# ---------------------------------------------------------------- размери на текста
def text_nodes(root):
    out = []
    for n in root.iter('node'):
        t = (n.get('text') or '').strip()
        if t and b(n):
            x0, y0, x1, y1 = b(n)
            if y1 > y0 and x1 > x0:
                out.append((t, y1 - y0, n))
    return out


def cmd_texth(ui, dens, maxn='14'):
    root = load(ui)
    d = float(dens) / 160
    tn = text_nodes(root)
    scr = [n for n in root.iter('node') if n.get('scrollable') == 'true']
    pk = sorted({n.get('package') for n in root.iter('node') if n.get('package')})
    print(f"TEXTH packages={pk} scrollable={len(scr)} {[n.get('bounds') for n in scr][:3]} texts={len(tn)}: "
          + '; '.join(f"'{t[:28]}' {h / d:.0f}dp" for t, h, _ in tn[:int(maxn)]))


def cmd_ratio(a, bb):
    ta = {t: h for t, h, _ in text_nodes(load(a))}
    tb = {t: h for t, h, _ in text_nodes(load(bb))}
    common = [t for t in ta if t in tb and ta[t] > 0]
    if not common:
        print("RATIO: no common texts")
        return
    r = sorted(tb[t] / ta[t] for t in common)
    print(f"RATIO {len(common)} common texts: median {statistics.median(r):.2f} min {r[0]:.2f} max {r[-1]:.2f}; "
          + '; '.join(f"'{t[:20]}' {tb[t] / ta[t]:.2f}" for t in common[:8]))


def cmd_nodeh(ui, text, dens):
    d = float(dens) / 160
    for t, h, n in text_nodes(load(ui)):
        if text.lower() in t.lower():
            print(f"{h / d:.1f}")
            return
    print("-")


# ---------------------------------------------------------------- известия
def cmd_notifp(f, pkg):
    t = open(f, encoding='utf-8', errors='replace').read()
    recs = re.split(r'\n\s*NotificationRecord\(', t)
    n = 0
    for r in recs[1:]:
        head = r.split('\n', 1)[0]
        if f'pkg={pkg}' not in head:
            continue
        n += 1
        g = lambda k: (re.search(rf'\b{k}=([^\n]*)', r) or [None, '-'])[1].strip()[:110]
        acts = re.findall(r'\[\d+\] "([^"]*)" -> PendingIntent', r)
        nid = (re.search(r'id=(-?\d+)', head) or [0, '?'])[1]
        print(f"NOTIF id={nid} title={g('android.title')} text={g('android.text')} progress={g('android.progress')}/"
              f"{g('android.progressMax')} indeterminate={g('android.progressIndeterminate')} ongoing={'FLAG_ONGOING_EVENT' in r or 'ongoing=true' in r} "
              f"fgs={'FLAG_FOREGROUND_SERVICE' in r} actions={acts}")
    print(f"NOTIF total for {pkg}: {n}")


# ---------------------------------------------------------------- Preferences DataStore
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
    if kind == 'e':  # празно множество от низове (string_set)
        return ld(6, b'')
    raise SystemExit(f"unknown kind {kind}")


def cmd_prefs_add(f, *kvs):
    data = open(f, 'rb').read() if os.path.exists(f) else b''
    for kv in kvs:
        k, v = kv.split('=', 1)
        kind, raw = v.split(':', 1)
        if kind == 'sf':  # стойността — от файл
            kind, raw = 's', open(raw, encoding='utf-8').read()
        data += ld(1, ld(1, k.encode('utf-8')) + ld(2, value(kind, raw)))
    open(f, 'wb').write(data)
    print(f"prefs: {len(data)} bytes after adding {[kv.split('=', 1)[0] for kv in kvs]}")


def cmd_whatsnew_json(upd, out):
    d = json.load(open(upd, encoding='utf-8'))
    w = {'versionCode': d['versionCode'], 'versionName': d['versionName'], 'notes': d['notes'], 'notesEn': d.get('notes_en', '')}
    open(out, 'w', encoding='utf-8').write(json.dumps(w, ensure_ascii=False))
    print(f"whats-new record: {d['versionName']} ({d['versionCode']}), {len(d['notes'].splitlines())} lines")


# ---------------------------------------------------------------- кадри (намалено движение)
def cmd_frames(dr, y0, y1):
    from PIL import Image, ImageChops, ImageStat
    fs = sorted(x for x in os.listdir(dr) if x.endswith('.png'))
    prev, changed, diffs = None, [], []
    for i, f in enumerate(fs):
        im = Image.open(os.path.join(dr, f)).convert('L')
        w, h = im.size
        im = im.crop((0, int(int(y0) * h / 2400), w, int(int(y1) * h / 2400)))
        if prev is not None:
            dv = ImageStat.Stat(ImageChops.difference(prev, im)).mean[0]
            diffs.append(dv)
            if dv > 0.8:
                changed.append(i)
        prev = im
    runs, cur = [], []
    for i in changed:
        if cur and i != cur[-1] + 1:
            runs.append(cur)
            cur = []
        cur.append(i)
    if cur:
        runs.append(cur)
    print(f"FRAMES {len(fs)} at 10 fps: changed frames {changed}; longest run of consecutive changing frames "
          f"{max((len(r) for r in runs), default=0)} (1-2 = a jump, more = an animation); max diff {max(diffs, default=0):.1f}")


def cmd_lang(*texts):
    s = ' '.join(texts)
    cyr = len(re.findall('[А-Яа-яЁё]', s))
    lat = len(re.findall('[A-Za-z]', s))
    print(f"LANG cyrillic={cyr} latin={lat} -> {'BG' if cyr > lat else 'EN' if lat > cyr else '?'}")


# ---------------------------------------------------------------- таблет
TABS = ['Начало', 'Каталог', 'Моето', 'Новини', 'Още', 'Home', 'Catalogue', 'My library', 'News', 'More']


def cmd_tabsel(ui):
    root = load(ui)
    par = parents(root)
    sel = []
    for n in root.iter('node'):
        if (n.get('text') or '') in TABS:
            m = n
            while m is not None and m.get('selected') != 'true' and m.get('clickable') != 'true':
                m = par.get(m)
            if m is not None and m.get('selected') == 'true':
                sel.append(n.get('text'))
    print(f"TAB selected: {sel or 'none'}")


def cmd_panes(ui, w):
    w = int(w)
    root = load(ui)
    left, right = [], []
    for t, h, n in text_nodes(root):
        x0, y0, x1, y1 = b(n)
        cx = (x0 + x1) / 2
        (left if cx < w * 0.42 else right).append(t)
    print(f"PANES left={len(left)} right={len(right)}; left: {[t[:24] for t in left[:6]]}; right: {[t[:24] for t in right[:6]]}")


# ---------------------------------------------------------------- дървото от тестовата услуга
def cmd_tree(f, carousel='Последни новини'):
    rows = []
    for line in open(f, encoding='utf-8', errors='replace'):
        m = re.search(r'PCDUMP\s*:\s*\S+ (W\d+) d(\d+) (.*)$', line)
        if not m:
            continue
        rest = m.group(3)
        kv = dict(re.findall(r'(\w+)=(.*?)(?= \w+=|$)', rest))
        kv['w'], kv['d'] = m.group(1), int(m.group(2))
        rows.append(kv)
    wins = [l.strip()[-160:] for l in open(f, encoding='utf-8', errors='replace') if re.search(r'PCDUMP\s*:\s*\S+ W\d+ type=', l)]
    print(f"TREE nodes={len(rows)} windows: {wins[:4]}")
    unl, acts, panes, heads, clk = [], [], [], [], 0
    for i, r in enumerate(rows):
        own = (r.get('text', '') + r.get('desc', '')).strip()
        if r.get('pane'):
            panes.append(r['pane'])
        if r.get('heading') == 'true':
            heads.append(own[:30])
        if r.get('acts'):
            acts.append(f"'{own[:40]}' [{r['acts'].strip(';')}]")
        if r.get('clk') == 'true' and r.get('enabled') == 'true':
            clk += 1
            sub = own
            for rr in rows[i + 1:]:
                if rr['w'] != r['w'] or rr['d'] <= r['d']:
                    break
                sub += rr.get('text', '') + rr.get('desc', '')
            if not sub.strip():
                unl.append(f"{r.get('cls', '').split('.')[-1]} {r.get('b', '')}")
    print(f"TREE clickable={clk} UNLABELLED clickable={len(unl)} {unl[:8]}")
    print(f"TREE pane titles: {panes}; headings: {heads[:12]}")
    print(f"TREE nodes with labelled (custom) actions: {len(acts)}: " + ' | '.join(acts[:8]))
    car = [r for r in rows if carousel in (r.get('desc', '') + r.get('text', ''))]
    nxt = [r for r in rows if 'Следваща новина' in r.get('acts', '') or 'Предишна новина' in r.get('acts', '')]
    print(f"TREE carousel nodes '{carousel}': {len(car)}; nodes with Следваща/Предишна новина: {len(nxt)}: "
          + ' | '.join(f"'{(r.get('desc', '') + r.get('text', ''))[:60]}' clk={r.get('clk')} b={r.get('b')} acts=[{r.get('acts', '').strip(';')}]" for r in nxt[:4]))


def cmd_cards(ui, places_file):
    places = [p.strip() for p in open(places_file, encoding='utf-8') if p.strip()] if os.path.exists(places_file) else []
    root = load(ui)
    hits = 0
    for n in root.iter('node'):
        if n.get('clickable') != 'true' or not b(n):
            continue
        x0, y0, x1, y1 = b(n)
        if x1 - x0 < 400 or y1 - y0 < 120:
            continue
        texts = [t for t in ((c.get('text') or '').strip() for c in n.iter('node')) if t]
        if not texts:
            continue
        inplace = [p for p in places if any(p in t for t in texts)]
        hits += 1
        if hits <= 6:
            print(f"CARD {texts[:4]} place-in-card={inplace or 'no'}")
    print(f"CARDS {hits} (known places: {len(places)})")


def cmd_carousel(ui, pkg, dens, scale):
    root = load(ui)
    d = float(dens) / 160
    best = None
    hs = [b(n) for n in root.iter('node') if b(n)]
    H = max(h[3] for h in hs) if hs else 2400
    for n in root.iter('node'):
        if n.get('package') != pkg or n.get('clickable') != 'true' or not b(n):
            continue
        x0, y0, x1, y1 = b(n)
        if y0 < H / 2 and (y1 - y0) > H / 8:
            a = (x1 - x0) * (y1 - y0)
            if not best or a > best[0]:
                best = (a, n)
    if not best:
        print("CAROUSEL card not found")
        return
    n = best[1]
    line = 36 * float(scale) * d
    for c in n.iter('node'):
        t = (c.get('text') or '').strip()
        if t and b(c):
            h = b(c)[3] - b(c)[1]
            print(f"CAROUSEL text '{t[:50]}' h={h / d:.0f}dp ≈ {h / line:.1f} lines of a 36sp×{scale} headline")
    print(f"CAROUSEL card {n.get('bounds')} {(b(n)[3] - b(n)[1]) / d:.0f}dp tall")


def cmd_widgetcol(ui, pkg, dens):
    d = float(dens) / 160
    for n in load(ui).iter('node'):
        if (n.get('resource-id') or '').endswith(':id/widget_event_column') or (n.get('resource-id') or '') == f'{pkg}:id/widget_event_column':
            x0, y0, x1, y1 = b(n)
            print(f"WIDGET event column {(x1 - x0) / d:.0f}x{(y1 - y0) / d:.0f}dp clickable={n.get('clickable')} label='{lab(n)[:80]}'")
            return
    print("WIDGET event column: not in the dump")


if __name__ == '__main__':
    c, a = sys.argv[1], sys.argv[2:]
    {'site': cmd_site, 'switch': cmd_switch, 'prompt': cmd_prompt, 'texth': cmd_texth, 'ratio': cmd_ratio, 'nodeh': cmd_nodeh,
     'notifp': cmd_notifp, 'prefs-add': cmd_prefs_add, 'whatsnew-json': cmd_whatsnew_json, 'frames': cmd_frames, 'lang': cmd_lang,
     'tabsel': cmd_tabsel, 'panes': cmd_panes, 'tree': cmd_tree, 'cards': cmd_cards, 'carousel': cmd_carousel,
     'widgetcol': cmd_widgetcol}[c](*a)
