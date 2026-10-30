"""От хоста: новина от сайта с курсив и кавички („…“) — за снимката на шрифтовете.

  find_article.py  -> "URL<TAB>заглавие<TAB>брой <em>/<i>/<blockquote><TAB>брой „"
Адресът е публичният /news/<slug> от rss.xml (по заглавие); без съвпадение — празен ред.
"""
import html, json, re, sys, urllib.request

SITE = "https://chyavorec.org"


def get(path):
    req = urllib.request.Request(SITE + path, headers={"User-Agent": "okhttp/5 (emulator check)"})
    with urllib.request.urlopen(req, timeout=40) as r:
        return r.read().decode("utf-8", "replace")


try:
    news = json.loads(get("/data/news.json"))
    rss = get("/rss.xml")
except Exception as e:  # noqa: BLE001
    print(f"\t(fetch failed: {e})\t0\t0")
    sys.exit(0)
items = news if isinstance(news, list) else (news.get("items") or news.get("news") or [])
links = {}
for m in re.finditer(r"<item>.*?<title>(.*?)</title>.*?<link>(.*?)</link>", rss, re.S):
    t = html.unescape(re.sub(r"<!\[CDATA\[|\]\]>", "", m.group(1))).strip()
    links[t] = m.group(2).strip()
best = None
for it in items:
    body = it.get("content") or ""
    ital = len(re.findall(r"<(em|i|blockquote)\b", body))
    quotes = body.count("„")
    title = (it.get("title") or "").strip()
    url = links.get(title, "")
    if not url:
        continue
    score = (ital > 0, ital + quotes)
    if best is None or score > best[0]:
        best = (score, url, title, ital, quotes)
if best:
    print(f"{best[1]}\t{best[2]}\t{best[3]}\t{best[4]}")
else:
    print("\t(no article matched rss)\t0\t0")
