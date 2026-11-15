"""Проверка от хоста (не от емулатора): снимките на страница от сайта и вариантите им от
оптимизатора на Vercel, както ги иска приложението (SiteImageUrls: w от 128…1600, q=75).

  probe_images.py PAGE_PATH [N] [WIDTHS]  -> по ред на снимка: статус, време, размер
"""
import re, sys, time, urllib.parse, urllib.request

SITE = "https://chyavorec.org"
path = sys.argv[1]
n = int(sys.argv[2]) if len(sys.argv) > 2 else 30
widths = [int(w) for w in (sys.argv[3] if len(sys.argv) > 3 else "384,1600").split(",")]


def get(url, limit=None):
    t0 = time.time()
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "okhttp/5 (emulator check)"})
        with urllib.request.urlopen(req, timeout=40) as r:
            body = r.read() if limit is None else r.read(limit)
            return r.status, time.time() - t0, len(body), r.headers.get("Content-Type", ""), body
    except urllib.error.HTTPError as e:
        return e.code, time.time() - t0, 0, "", b""
    except Exception as e:  # noqa: BLE001
        return type(e).__name__, time.time() - t0, 0, "", b""


st, dt, size, _, html = get(SITE + path)
print(f"PAGE {path}: {st} {dt:.2f}s {size} bytes")
html = html.decode("utf-8", "replace")
imgs = re.findall(r"<img\b[^>]*>", html)
print(f"img tags: {len(imgs)}")


def attr(tag, name):
    m = re.search(name + r'\s*=\s*"([^"]*)"', tag)
    return m.group(1) if m else ""


def optimized(url, w):
    u = urllib.parse.urlparse(urllib.parse.urljoin(SITE + "/", url))
    if u.netloc not in ("chyavorec.org", "www.chyavorec.org") or u.query or not u.path.startswith(("/assets/", "/pictures/")):
        return None
    if u.path.lower().endswith(".svg"):
        return None
    p = urllib.parse.quote(urllib.parse.unquote(u.path), safe="")
    return f"{SITE}/_vercel/image?url={p}&w={w}&q=75"


fails = 0
for i, tag in enumerate(imgs[:n], 1):
    src = attr(tag, "data-src") or attr(tag, "src")
    srcset = attr(tag, "srcset") or attr(tag, "data-srcset")
    thumb = src
    if srcset:
        cands = []
        for part in srcset.split(","):
            bits = part.strip().split()
            if bits:
                wd = int(bits[1][:-1]) if len(bits) > 1 and bits[1].endswith("w") and bits[1][:-1].isdigit() else 10 ** 6
                cands.append((wd, bits[0]))
        if cands:
            thumb = min(cands)[1]
    src_abs = urllib.parse.urljoin(SITE + "/", src)
    s0 = get(src_abs)
    line = f"#{i:02d} {attr(tag, 'alt')[:40]!r} original {s0[0]} {s0[1]:.2f}s {s0[2] // 1024}KB {s0[3]}"
    for w in widths:
        base = thumb if w <= 640 else src
        o = optimized(base, w)
        if o is None:
            line += f" | w{w}: not optimizable"
            continue
        r = get(o)
        if r[0] != 200:
            fails += 1
        line += f" | w{w} {r[0]} {r[1]:.2f}s {r[2] // 1024}KB {r[3]}"
    print(line)
    print(f"     src={src_abs[:150]}  thumb={thumb[:120]}")
print(f"PROBE {path}: {min(n, len(imgs))} images, optimizer non-200: {fails}")
