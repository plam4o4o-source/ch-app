package org.chyavorec.app.ui.theme

import androidx.compose.ui.graphics.Color

/** Палитрата на chyavorec.org (дизайн системата на сайта). */
object Brand {
    val Ink = Color(0xFF1A1208)
    val Gold = Color(0xFFC9A84C)
    val GoldDark = Color(0xFF8B6914)
    val GoldLight = Color(0xFFE8D5A3)
    val Burgundy = Color(0xFF6B1F2A)
    val Cream = Color(0xFFFAF7F2)
    val Parchment = Color(0xFFF5F0E8)
    val Muted = Color(0xFF8A7E6E)
    val InkSoft = Color(0xFF2A1E12)
    val InkRaised = Color(0xFF33261A)
}

/** Семантични цветове за статуси — проверени за контраст WCAG AA върху фоновете. */
object StatusColors {
    val OkLight = Color(0xFF2F6B4A)
    val OkDark = Color(0xFF8FD1A8)
    val WarnLight = Color(0xFF9A5B00)
    val WarnDark = Color(0xFFF2C06B)
    val BadLight = Color(0xFFA3262F)
    val BadDark = Color(0xFFFF9C9C)
}

/** Цветове на корицата по раздел на УДК (от page-katalog.html, проверени по WCAG AA). */
val UdcCoverColors: Map<Int, Color> = mapOf(
    0 to Color(0xFF4F6D8A),
    1 to Color(0xFF6A5A8C),
    2 to Color(0xFF7A4E6E),
    3 to Color(0xFF6B7A3A),
    5 to Color(0xFF2F7A72),
    6 to Color(0xFFA06A1E),
    7 to Color(0xFF8A4B3A),
    8 to Color(0xFF6B1F2A),
    9 to Color(0xFF3F5E4A),
)
