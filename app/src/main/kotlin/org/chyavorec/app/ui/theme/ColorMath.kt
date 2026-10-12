package org.chyavorec.app.ui.theme

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

/*
 * Чиста цветова математика върху ARGB като Int — без Android и Compose, за да се
 * тества на обикновена JVM. Цветовете се приемат за непрозрачни (алфата се игнорира
 * при сметките и резултатите са с алфа 0xFF).
 */
object ColorMath {
    /** WCAG AA за нормален текст. */
    const val MIN_TEXT_CONTRAST = 4.5

    const val WHITE: Int = -0x1 // 0xFFFFFFFF
    const val BLACK: Int = -0x1000000 // 0xFF000000

    fun red(c: Int): Int = (c shr 16) and 0xFF
    fun green(c: Int): Int = (c shr 8) and 0xFF
    fun blue(c: Int): Int = c and 0xFF

    fun rgb(r: Int, g: Int, b: Int): Int =
        (0xFF shl 24) or (r.coerceIn(0, 255) shl 16) or (g.coerceIn(0, 255) shl 8) or b.coerceIn(0, 255)

    /** Непрозрачен ARGB от 0xAARRGGBB, записан като Long (както `Color(0xFF…)`). */
    fun of(argb: Long): Int = (argb and 0xFFFFFFFFL).toInt() or (0xFF shl 24)

    private fun linear(channel: Int): Double {
        val s = channel / 255.0
        return if (s <= 0.04045) s / 12.92 else ((s + 0.055) / 1.055).pow(2.4)
    }

    /** Относителна яркост по WCAG 2.x (0 = черно, 1 = бяло). */
    fun luminance(c: Int): Double = 0.2126 * linear(red(c)) + 0.7152 * linear(green(c)) + 0.0722 * linear(blue(c))

    /** Контраст по WCAG 2.x — от 1:1 до 21:1. */
    fun contrast(a: Int, b: Int): Double {
        val la = luminance(a)
        val lb = luminance(b)
        return (max(la, lb) + 0.05) / (min(la, lb) + 0.05)
    }

    fun readable(foreground: Int, background: Int, min: Double = MIN_TEXT_CONTRAST): Boolean = contrast(foreground, background) >= min

    /** HSL: [0] тон 0–360, [1] наситеност 0–1, [2] светлота 0–1. */
    fun toHsl(c: Int): FloatArray {
        val r = red(c) / 255f
        val g = green(c) / 255f
        val b = blue(c) / 255f
        val mx = max(r, max(g, b))
        val mn = min(r, min(g, b))
        val l = (mx + mn) / 2f
        val d = mx - mn
        if (d == 0f) return floatArrayOf(0f, 0f, l)
        val s = d / (1f - abs(2f * l - 1f))
        val h = when (mx) {
            r -> 60f * (((g - b) / d) % 6f)
            g -> 60f * ((b - r) / d + 2f)
            else -> 60f * ((r - g) / d + 4f)
        }
        return floatArrayOf((h + 360f) % 360f, s.coerceIn(0f, 1f), l)
    }

    fun fromHsl(h: Float, s: Float, l: Float): Int {
        val hh = ((h % 360f) + 360f) % 360f
        val ss = s.coerceIn(0f, 1f)
        val ll = l.coerceIn(0f, 1f)
        val c = (1f - abs(2f * ll - 1f)) * ss
        val x = c * (1f - abs((hh / 60f) % 2f - 1f))
        val m = ll - c / 2f
        val (r, g, b) = when {
            hh < 60f -> Triple(c, x, 0f)
            hh < 120f -> Triple(x, c, 0f)
            hh < 180f -> Triple(0f, c, x)
            hh < 240f -> Triple(0f, x, c)
            hh < 300f -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }
        return rgb(((r + m) * 255f).roundToInt(), ((g + m) * 255f).roundToInt(), ((b + m) * 255f).roundToInt())
    }

    /** Линейно смесване в sRGB: t = 0 → [a], t = 1 → [b]. */
    fun mix(a: Int, b: Int, t: Float): Int {
        val k = t.coerceIn(0f, 1f)
        fun ch(x: Int, y: Int) = (x + (y - x) * k).roundToInt()
        return rgb(ch(red(a), red(b)), ch(green(a), green(b)), ch(blue(a), blue(b)))
    }

    /** Най-късата посока между два тона (-180..180]. */
    fun hueDelta(from: Float, to: Float): Float {
        var d = (to - from) % 360f
        if (d > 180f) d -= 360f
        if (d <= -180f) d += 360f
        return d
    }

    /**
     * „Хармонизиране“ (като в Material): тонът се завърта към тона на темата с
     * половината от разликата, но най-много с [maxShift] градуса — цветът на
     * корицата остава разпознаваем, а екранът не се „кара“ с останалото приложение.
     */
    fun harmonizeHue(hue: Float, toward: Float, maxShift: Float = 15f): Float {
        val d = hueDelta(hue, toward)
        val shift = (d / 2f).coerceIn(-maxShift, maxShift)
        return ((hue + shift) % 360f + 360f) % 360f
    }

    /**
     * Първият цвят с тон [h] и наситеност [s], чиято светлота (от [from] към [to],
     * на стъпки по 0.02) изпълнява [ok]; `null`, ако няма такъв.
     */
    fun searchLightness(h: Float, s: Float, from: Float, to: Float, ok: (Int) -> Boolean): Int? {
        val step = if (to >= from) 0.02f else -0.02f
        var l = from
        var i = 0
        while (i <= 60) {
            val done = if (step > 0) l > to + 1e-4f else l < to - 1e-4f
            if (done) break
            val c = fromHsl(h, s, l)
            if (ok(c)) return c
            l += step
            i++
        }
        return null
    }
}
