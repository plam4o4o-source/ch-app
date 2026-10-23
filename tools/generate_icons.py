#!/usr/bin/env python3
"""Генерира иконата за Google Play (store/graphics/play-icon-512.png) от логото
(drawable-nodpi/logo_chitalishte.webp): цялата емблема с надписа върху бордо.

Иконата в приложението НЕ се генерира оттук: в лаунчера емблемата с надписа в 48 dp се
превръща в бял диск с нечетими букви, затова адаптивната икона е векторна и опростена —
пръстен и отворена книга (drawable/ic_launcher_foreground.xml, ic_launcher_monochrome.xml,
фон ic_launcher_background.xml). Същият знак е и splash иконата (drawable/splash_icon.xml,
анимирана в drawable-v31).

Изпълнение от корена: python3 tools/generate_icons.py
"""
import math
from PIL import Image, ImageDraw, ImageFilter, ImageOps

LOGO = "app/src/main/res/drawable-nodpi/logo_chitalishte.webp"

WHITE = (255, 255, 255, 255)
GOLD = (201, 168, 76, 255)        # brand_gold #C9A84C
BURGUNDY = (107, 31, 42, 255)     # brand_burgundy #6B1F2A


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


def background(canvas):
    """Бордо с лек радиален ореол (за Play иконата; в приложението фонът е vector)."""
    s = canvas
    im = Image.new("RGBA", (s, s), BURGUNDY)
    glow = Image.new("L", (s, s), 0)
    ImageDraw.Draw(glow).ellipse((s * 0.1, s * 0.05, s * 0.9, s * 0.85), fill=110)
    glow = glow.filter(ImageFilter.GaussianBlur(s * 0.12))
    im = Image.composite(Image.new("RGBA", (s, s), (150, 52, 64, 255)), im, glow)
    return im


def main():
    logo = load_logo()
    circle = enclosing_circle(logo)

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
    print("emblem circle (logo px):", [round(v) for v in circle])


if __name__ == "__main__":
    main()
