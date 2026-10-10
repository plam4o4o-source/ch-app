#!/usr/bin/env python3
"""Генерира иконата на приложението от логото (drawable-nodpi/logo_chitalishte.webp).

Адаптивна икона (Android 8+): платно 108dp; лаунчерът показва кръг/заоблен
квадрат от поне 72dp (66,7%), а гарантирано видимата „безопасна зона“ е кръг
с диаметър 66dp (61,1%). Цялата емблема (с надписа по ръба) трябва да е вътре
в безопасната зона — иначе маската реже буквите.

Иконите и splash логото се записват като WebP без загуби (по-малки от PNG;
Android 8+ ги поддържа с прозрачност).

Изпълнение от корена: python3 tools/generate_icons.py
"""
import math
from PIL import Image, ImageDraw, ImageFilter, ImageOps

LOGO = "app/src/main/res/drawable-nodpi/logo_chitalishte.webp"
RES = "app/src/main/res"
DENSITIES = {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}
SPLASH = 480     # px; splash иконата е 240dp платно, видимият кръг е 160dp

WHITE = (255, 255, 255, 255)
GOLD = (201, 168, 76, 255)        # brand_gold #C9A84C
GOLD_DARK = (139, 105, 20, 255)   # brand_gold_dark #8B6914
BURGUNDY = (107, 31, 42, 255)     # brand_burgundy #6B1F2A

# Пропорции спрямо платното 108dp.
DISC = 0.60      # белият диск (вътре в безопасната зона 61,1%)
RING = 0.012     # златният кант около диска
EMBLEM = 0.525   # диаметър на кръга, описан около емблемата (с буквите)


def load_logo():
    im = Image.open(LOGO).convert("RGBA")
    # По-ясни букви: тъмносивото става почти черно, светлото остава светло.
    r, g, b, a = im.split()
    lum = Image.merge("RGB", (r, g, b)).convert("L")
    lum = lum.point(lambda v: int(255 * ((v / 255) ** 1.35)))
    lum = ImageOps.autocontrast(lum, cutoff=0.5)
    rgb = Image.merge("RGB", (lum, lum, lum))
    return Image.merge("RGBA", (*rgb.split(), a))


def enclosing_circle(im, thr=40):
    """Център и радиус на най-малкия кръг около непрозрачните пиксели (груба оценка)."""
    a = im.split()[3].load()
    w, h = im.size
    pts = [(x, y) for y in range(0, h, 2) for x in range(0, w, 2) if a[x, y] > thr]
    bx = (min(p[0] for p in pts) + max(p[0] for p in pts)) / 2
    by = (min(p[1] for p in pts) + max(p[1] for p in pts)) / 2
    ext = sorted(pts, key=lambda p: -((p[0] - bx) ** 2 + (p[1] - by) ** 2))[:4000]
    best = None
    for dx in range(-40, 41, 2):
        for dy in range(-40, 41, 2):
            cx, cy = bx + dx, by + dy
            r = max(math.hypot(p[0] - cx, p[1] - cy) for p in ext)
            if best is None or r < best[0]:
                best = (r, cx, cy)
    return best


def place_emblem(logo, circle, canvas, diameter_frac):
    """Мащабира логото така, че описаният кръг да е diameter_frac от платното, центрирано."""
    r, cx, cy = circle
    scale = diameter_frac * canvas / (2 * r)
    w, h = logo.size
    big = logo.resize((round(w * scale), round(h * scale)), Image.LANCZOS)
    out = Image.new("RGBA", (canvas, canvas), (0, 0, 0, 0))
    out.alpha_composite(big, (round(canvas / 2 - cx * scale), round(canvas / 2 - cy * scale)))
    return out


def disc(canvas, frac, color, ss=4):
    s = canvas * ss
    im = Image.new("RGBA", (s, s), (0, 0, 0, 0))
    r = frac * s / 2
    ImageDraw.Draw(im).ellipse((s / 2 - r, s / 2 - r, s / 2 + r, s / 2 + r), fill=color)
    return im.resize((canvas, canvas), Image.LANCZOS)


def foreground(logo, circle, canvas):
    out = Image.new("RGBA", (canvas, canvas), (0, 0, 0, 0))
    # мека сянка под диска — медальонът „изпъква“ от фона
    shadow = disc(canvas, DISC + 2 * RING, (0, 0, 0, 90)).filter(ImageFilter.GaussianBlur(canvas * 0.012))
    out.alpha_composite(shadow, (0, round(canvas * 0.008)))
    out.alpha_composite(disc(canvas, DISC + 2 * RING, GOLD))
    out.alpha_composite(disc(canvas, DISC, WHITE))
    out.alpha_composite(place_emblem(logo, circle, canvas, EMBLEM))
    return out


def monochrome(logo, circle, canvas):
    """Тематична икона (Android 13+): системата оцветява алфа канала."""
    r, g, b, a = logo.split()
    lum = Image.merge("RGB", (r, g, b)).convert("L")
    # тъмните букви/контури — плътни; сивият диск — лек; бялото — прозрачно
    ink = lum.point(lambda v: max(0, min(255, int((215 - v) * 255 / 170))))
    alpha = Image.eval(Image.merge("LA", (ink, a)).split()[0], lambda v: v)
    alpha = Image.composite(alpha, Image.new("L", logo.size, 0), a)
    mask_logo = Image.merge("RGBA", (Image.new("L", logo.size, 255),) * 3 + (alpha,))
    return place_emblem(mask_logo, circle, canvas, EMBLEM + 0.03)


def background(canvas):
    """Бордо с лек радиален ореол (за Play иконата; в приложението фонът е vector)."""
    s = canvas
    im = Image.new("RGBA", (s, s), BURGUNDY)
    glow = Image.new("L", (s, s), 0)
    ImageDraw.Draw(glow).ellipse((s * 0.1, s * 0.05, s * 0.9, s * 0.85), fill=110)
    glow = glow.filter(ImageFilter.GaussianBlur(s * 0.12))
    im = Image.composite(Image.new("RGBA", (s, s), (150, 52, 64, 255)), im, glow)
    return im


def save_webp(im, path):
    """WebP без загуби (точни пиксели, вкл. прозрачност), максимално компресиран."""
    im.save(path, "WEBP", lossless=True, quality=100, method=6)


def main():
    logo = load_logo()
    circle = enclosing_circle(logo)
    for name, k in DENSITIES.items():
        c = round(108 * k)
        save_webp(foreground(logo, circle, c), f"{RES}/mipmap-{name}/ic_launcher_foreground.webp")
        save_webp(monochrome(logo, circle, c), f"{RES}/mipmap-{name}/ic_launcher_monochrome.webp")

    # Splash (Android 12+): 240dp платно, видим кръг 160dp (66,7%); отдолу е белият iconBackground.
    splash = Image.new("RGBA", (SPLASH, SPLASH), (0, 0, 0, 0))
    splash.alpha_composite(place_emblem(logo, circle, SPLASH, 0.58))
    save_webp(splash, f"{RES}/drawable-nodpi/splash_logo.webp")

    # Google Play: 512×512, Play сам заобля ъглите (без кръгла маска).
    play = background(512)
    fg = Image.new("RGBA", (512, 512), (0, 0, 0, 0))
    shadow = disc(512, 0.80, (0, 0, 0, 100)).filter(ImageFilter.GaussianBlur(8))
    fg.alpha_composite(shadow, (0, 5))
    fg.alpha_composite(disc(512, 0.80, GOLD))
    fg.alpha_composite(disc(512, 0.77, WHITE))
    fg.alpha_composite(place_emblem(logo, circle, 512, 0.69))
    play.alpha_composite(fg)
    play.convert("RGB").save("store/graphics/play-icon-512.png", optimize=True)

    # Преглед: как изглежда през кръгла и „squircle“ маска.
    prev = Image.new("RGBA", (3 * 240 + 40, 280), (120, 140, 40, 255))
    fgp = foreground(logo, circle, 324)
    bgp = background(324)
    bgp.alpha_composite(fgp)
    full = bgp.crop((54, 54, 270, 270)).resize((216, 216), Image.LANCZOS)  # 72dp видима област
    circ = Image.new("L", (216, 216), 0); ImageDraw.Draw(circ).ellipse((0, 0, 215, 215), fill=255)
    sq = Image.new("L", (216, 216), 0); ImageDraw.Draw(sq).rounded_rectangle((0, 0, 215, 215), radius=70, fill=255)
    mono = Image.new("RGBA", (324, 324), (220, 230, 200, 255))
    mono.alpha_composite(Image.merge("RGBA", (Image.new("L", (324, 324), 40),) * 3 + (monochrome(logo, circle, 324).split()[3],)))
    mono = mono.crop((54, 54, 270, 270))
    for i, (img, m) in enumerate([(full, circ), (full, sq), (mono, circ)]):
        prev.paste(img, (20 + i * 240, 32), m)
    prev.save("/tmp/icon-preview.png")
    print("emblem circle (logo px):", [round(v) for v in circle])


if __name__ == "__main__":
    main()
