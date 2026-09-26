package org.chyavorec.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R

/** Cormorant Garamond — заглавия (както на сайта). */
val Cormorant = FontFamily(
    Font(R.font.cormorant_garamond_light, FontWeight.Light),
    Font(R.font.cormorant_garamond_regular, FontWeight.Normal),
    Font(R.font.cormorant_garamond_semibold, FontWeight.SemiBold),
    Font(R.font.cormorant_garamond_italic, FontWeight.Normal, FontStyle.Italic),
)

/** Raleway — основен текст и етикети. */
val Raleway = FontFamily(
    Font(R.font.raleway_light, FontWeight.Light),
    Font(R.font.raleway_regular, FontWeight.Normal),
    Font(R.font.raleway_semibold, FontWeight.SemiBold),
    Font(R.font.raleway_bold, FontWeight.Bold),
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.Light, fontSize = 52.sp, lineHeight = 56.sp),
    displayMedium = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.Light, fontSize = 42.sp, lineHeight = 46.sp),
    displaySmall = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.Normal, fontSize = 34.sp, lineHeight = 38.sp),
    headlineLarge = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.Normal, fontSize = 31.sp, lineHeight = 36.sp),
    headlineMedium = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.SemiBold, fontSize = 27.sp, lineHeight = 32.sp),
    headlineSmall = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.SemiBold, fontSize = 23.sp, lineHeight = 28.sp),
    titleLarge = TextStyle(fontFamily = Cormorant, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 25.sp),
    bodyMedium = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 17.sp),
    labelLarge = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.02.em),
    labelMedium = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.04.em),
    labelSmall = TextStyle(fontFamily = Raleway, fontWeight = FontWeight.Bold, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.14.em),
)
