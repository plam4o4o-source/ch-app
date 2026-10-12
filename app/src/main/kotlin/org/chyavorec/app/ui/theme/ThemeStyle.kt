package org.chyavorec.app.ui.theme

/**
 * Палитрите на приложението (всяка — светла и тъмна):
 * „Яворец“ — злато и жито (по подразбиране, цветовете на сайта),
 * „Левски“ — тъмнозелено и сребро, „Шевица“ — червено, черно и слонова кост.
 *
 * [coverAnchors] — тоновете (ARGB), от които се избира фонът на генерираните
 * корици; всички са достатъчно тъмни за светъл текст отгоре.
 */
enum class ThemePalette(val coverAnchors: IntArray) {
    YAVORETS(
        intArrayOf(
            ColorMath.of(0xFF8B6914), ColorMath.of(0xFF6B1F2A), ColorMath.of(0xFF5A4D3D),
            ColorMath.of(0xFF3F5E4A), ColorMath.of(0xFF7A4E2E), ColorMath.of(0xFF8A4B3A),
        ),
    ),
    LEVSKI(
        intArrayOf(
            ColorMath.of(0xFF1E5B3A), ColorMath.of(0xFF2F5D62), ColorMath.of(0xFF4A5560),
            ColorMath.of(0xFF33473A), ColorMath.of(0xFF5C6670), ColorMath.of(0xFF3E5F4F),
        ),
    ),
    SHEVITSA(
        intArrayOf(
            ColorMath.of(0xFF9C1C24), ColorMath.of(0xFF2B2422), ColorMath.of(0xFF6B1F2A),
            ColorMath.of(0xFF7A2E1E), ColorMath.of(0xFF3A2A28), ColorMath.of(0xFF8C3A2E),
        ),
    ),
}

/**
 * Избраната палитра и „черно (OLED)“ за тъмната тема. Пази се в един ключ на
 * настройките като текст: `"LEVSKI"` или `"LEVSKI+oled"`.
 */
data class ThemeStyle(val palette: ThemePalette = ThemePalette.YAVORETS, val oledBlack: Boolean = false) {
    fun encode(): String = if (oledBlack) palette.name + OLED_SUFFIX else palette.name

    companion object {
        const val OLED_SUFFIX = "+oled"

        /** Непознато или липсващо → по подразбиране („Яворец“, без OLED). */
        fun decode(raw: String?): ThemeStyle {
            if (raw.isNullOrBlank()) return ThemeStyle()
            val oled = raw.endsWith(OLED_SUFFIX)
            val name = raw.removeSuffix(OLED_SUFFIX)
            val palette = ThemePalette.entries.firstOrNull { it.name == name } ?: ThemePalette.YAVORETS
            return ThemeStyle(palette, oled)
        }
    }
}
