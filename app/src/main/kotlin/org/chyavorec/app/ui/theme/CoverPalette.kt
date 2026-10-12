package org.chyavorec.app.ui.theme

import org.chyavorec.app.ui.theme.ColorMath.WHITE
import org.chyavorec.app.ui.theme.ColorMath.contrast
import org.chyavorec.app.ui.theme.ColorMath.fromHsl
import org.chyavorec.app.ui.theme.ColorMath.mix
import org.chyavorec.app.ui.theme.ColorMath.readable
import org.chyavorec.app.ui.theme.ColorMath.searchLightness
import org.chyavorec.app.ui.theme.ColorMath.toHsl

/**
 * Ролите от темата на приложението, върху които се нанасят цветовете от корицата
 * (ARGB). [tintSurfaces] = false пази фоновете непроменени (напр. черно за OLED).
 */
data class SchemeBase(
    val dark: Boolean,
    val primary: Int,
    val background: Int,
    val onBackground: Int,
    val surface: Int,
    val onSurface: Int,
    val surfaceVariant: Int,
    val onSurfaceVariant: Int,
    val surfaceContainerLowest: Int,
    val surfaceContainerLow: Int,
    val surfaceContainer: Int,
    val surfaceContainerHigh: Int,
    val surfaceContainerHighest: Int,
    val tintSurfaces: Boolean = true,
)

/** Цветовете на екрана с детайлите, получени от корицата (ARGB). */
data class CoverTones(
    val primary: Int,
    val onPrimary: Int,
    val primaryContainer: Int,
    val onPrimaryContainer: Int,
    val inversePrimary: Int,
    val secondary: Int,
    val onSecondary: Int,
    val secondaryContainer: Int,
    val onSecondaryContainer: Int,
    val background: Int,
    val surface: Int,
    val surfaceVariant: Int,
    val surfaceContainerLowest: Int,
    val surfaceContainerLow: Int,
    val surfaceContainer: Int,
    val surfaceContainerHigh: Int,
    val surfaceContainerHighest: Int,
)

/**
 * Цветове от корицата → хармонизирана схема. Семето е живият (vibrant) или
 * преобладаващият цвят; тонът му се приближава към тона на темата, а светлотата
 * на всяка роля се подбира така, че текстът да има контраст поне 4.5:1. Ако това
 * не е възможно — `null` и екранът остава с цветовете на приложението.
 */
object CoverPalette {
    /** Под тази наситеност корицата е „сива“ — цветовете ѝ не носят нищо. */
    private const val MIN_SEED_SATURATION = 0.18f

    /**
     * Семето: първият достатъчно наситен и не почти черен/бял цвят измежду живия,
     * преобладаващия и приглушения; `null` за черно-бяла корица.
     */
    fun chooseSeed(vibrant: Int?, dominant: Int?, muted: Int? = null): Int? =
        listOfNotNull(vibrant, dominant, muted).firstOrNull { c ->
            val hsl = toHsl(c)
            hsl[1] >= MIN_SEED_SATURATION && hsl[2] in 0.08f..0.94f
        }

    fun derive(seed: Int, base: SchemeBase): CoverTones? {
        val seedHsl = toHsl(seed)
        val hue = ColorMath.harmonizeHue(seedHsl[0], toHsl(base.primary)[0])
        val sat = seedHsl[1].coerceIn(0.28f, 0.70f)
        val min = ColorMath.MIN_TEXT_CONTRAST

        // Фоновете — само лек оттенък от корицата (5–7 %), за да остане текстът четим.
        val tint = fromHsl(hue, sat, 0.5f)
        val t = if (!base.tintSurfaces) 0f else if (base.dark) 0.07f else 0.05f
        fun tinted(c: Int, k: Float = t) = if (k == 0f) c else mix(c, tint, k)
        val background = tinted(base.background)
        val surface = tinted(base.surface)
        val containers = intArrayOf(
            tinted(base.surfaceContainerLowest), tinted(base.surfaceContainerLow), tinted(base.surfaceContainer),
            tinted(base.surfaceContainerHigh), tinted(base.surfaceContainerHighest),
        )
        // Основният и вторичният цвят са и текст (бутони, връзки, автор) — четими върху всеки фон.
        val surfaces = intArrayOf(background, surface) + containers
        fun readableOnSurfaces(c: Int) = surfaces.all { readable(c, it) }

        val tones = if (!base.dark) {
            val primary = searchLightness(hue, sat, 0.42f, 0.10f) { readable(WHITE, it) && readableOnSurfaces(it) } ?: return null
            val primaryContainer = fromHsl(hue, sat * 0.7f, 0.88f)
            val onPrimaryContainer = searchLightness(hue, sat, 0.16f, 0.02f) { readable(it, primaryContainer) } ?: return null
            val secondary = searchLightness(hue, sat * 0.45f, 0.40f, 0.10f) { readable(WHITE, it) && readableOnSurfaces(it) } ?: return null
            val secondaryContainer = fromHsl(hue, sat * 0.35f, 0.90f)
            val onSecondaryContainer = searchLightness(hue, sat * 0.45f, 0.16f, 0.02f) { readable(it, secondaryContainer) } ?: return null
            Roles(
                primary, WHITE, primaryContainer, onPrimaryContainer, fromHsl(hue, sat, 0.80f),
                secondary, WHITE, secondaryContainer, onSecondaryContainer,
            )
        } else {
            val primary = searchLightness(hue, sat, 0.70f, 0.92f) { readableOnSurfaces(it) } ?: return null
            val onPrimary = searchLightness(hue, sat, 0.14f, 0.02f) { readable(it, primary) } ?: return null
            val primaryContainer = fromHsl(hue, sat * 0.6f, 0.24f)
            val onPrimaryContainer = searchLightness(hue, sat * 0.6f, 0.88f, 0.98f) { readable(it, primaryContainer) } ?: return null
            val secondary = searchLightness(hue, sat * 0.35f, 0.74f, 0.94f) { readableOnSurfaces(it) } ?: return null
            val onSecondary = searchLightness(hue, sat * 0.35f, 0.14f, 0.02f) { readable(it, secondary) } ?: return null
            val secondaryContainer = fromHsl(hue, sat * 0.3f, 0.22f)
            val onSecondaryContainer = searchLightness(hue, sat * 0.3f, 0.88f, 0.98f) { readable(it, secondaryContainer) } ?: return null
            Roles(
                primary, onPrimary, primaryContainer, onPrimaryContainer, fromHsl(hue, sat, 0.35f),
                secondary, onSecondary, secondaryContainer, onSecondaryContainer,
            )
        }

        val result = CoverTones(
            primary = tones.primary,
            onPrimary = tones.onPrimary,
            primaryContainer = tones.primaryContainer,
            onPrimaryContainer = tones.onPrimaryContainer,
            inversePrimary = tones.inversePrimary,
            secondary = tones.secondary,
            onSecondary = tones.onSecondary,
            secondaryContainer = tones.secondaryContainer,
            onSecondaryContainer = tones.onSecondaryContainer,
            background = background,
            surface = surface,
            surfaceVariant = tinted(base.surfaceVariant, t * 1.6f),
            surfaceContainerLowest = containers[0],
            surfaceContainerLow = containers[1],
            surfaceContainer = containers[2],
            surfaceContainerHigh = containers[3],
            surfaceContainerHighest = containers[4],
        )
        return result.takeIf { passesContrast(it, base, min) }
    }

    /**
     * Последна проверка на двойките текст/фон, които екранът ползва; при провал
     * схемата се отхвърля изцяло (по-добре цветовете на приложението, отколкото нечетим текст).
     */
    fun passesContrast(t: CoverTones, base: SchemeBase, min: Double = ColorMath.MIN_TEXT_CONTRAST): Boolean {
        val surfaces = listOf(
            t.background, t.surface, t.surfaceContainerLowest, t.surfaceContainerLow,
            t.surfaceContainer, t.surfaceContainerHigh, t.surfaceContainerHighest,
        )
        val pairs = buildList {
            add(t.onPrimary to t.primary)
            add(t.onPrimaryContainer to t.primaryContainer)
            add(t.onSecondary to t.secondary)
            add(t.onSecondaryContainer to t.secondaryContainer)
            surfaces.forEach {
                add(t.primary to it)
                add(t.secondary to it)
            }
            add(base.onBackground to t.background)
            surfaces.forEach { add(base.onSurface to it) }
            add(base.onSurfaceVariant to t.surface)
            add(base.onSurfaceVariant to t.background)
            add(base.onSurfaceVariant to t.surfaceVariant)
        }
        return pairs.all { (fg, bg) -> contrast(fg, bg) >= min }
    }

    private class Roles(
        val primary: Int,
        val onPrimary: Int,
        val primaryContainer: Int,
        val onPrimaryContainer: Int,
        val inversePrimary: Int,
        val secondary: Int,
        val onSecondary: Int,
        val secondaryContainer: Int,
        val onSecondaryContainer: Int,
    )
}
