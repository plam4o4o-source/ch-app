package org.chyavorec.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Brand.GoldDark,
    onPrimary = Color.White,
    primaryContainer = Brand.GoldLight,
    onPrimaryContainer = Brand.Ink,
    secondary = Brand.Burgundy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3DADD),
    onSecondaryContainer = Color(0xFF3D0B13),
    tertiary = Color(0xFF3F5E4A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD5E8D9),
    onTertiaryContainer = Color(0xFF10261A),
    background = Brand.Cream,
    onBackground = Brand.Ink,
    surface = Brand.Cream,
    onSurface = Brand.Ink,
    surfaceVariant = Brand.Parchment,
    onSurfaceVariant = Color(0xFF5A4D3D),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFCF9F4),
    surfaceContainer = Brand.Parchment,
    surfaceContainerHigh = Color(0xFFEFE8DC),
    surfaceContainerHighest = Color(0xFFE9E0D1),
    outline = Color(0xFFB8A988),
    outlineVariant = Color(0xFFE2D6BC),
    error = StatusColors.BadLight,
    onError = Color.White,
    inverseSurface = Brand.Ink,
    inverseOnSurface = Brand.Parchment,
    inversePrimary = Brand.Gold,
)

private val DarkColors = darkColorScheme(
    primary = Brand.Gold,
    onPrimary = Brand.Ink,
    primaryContainer = Color(0xFF4A3A12),
    onPrimaryContainer = Brand.GoldLight,
    secondary = Color(0xFFE6A8B1),
    onSecondary = Color(0xFF4A0F19),
    secondaryContainer = Color(0xFF5A1C26),
    onSecondaryContainer = Color(0xFFF7D9DE),
    tertiary = Color(0xFF9FCBAF),
    onTertiary = Color(0xFF0C2A18),
    tertiaryContainer = Color(0xFF26432F),
    onTertiaryContainer = Color(0xFFCBE9D4),
    background = Brand.Ink,
    onBackground = Brand.Parchment,
    surface = Brand.Ink,
    onSurface = Brand.Parchment,
    surfaceVariant = Brand.InkRaised,
    onSurfaceVariant = Color(0xFFD7CBB6),
    // На тъмен фон сенките не се виждат — дълбочината идва от тона: контейнерите са
    // малко по-светли и по-топли от фона (мастило → кафяво-златисто), за да не се сливат.
    surfaceContainerLowest = Color(0xFF120C05),
    surfaceContainerLow = Color(0xFF261B10),
    surfaceContainer = Color(0xFF2E2216),
    surfaceContainerHigh = Color(0xFF392B1D),
    surfaceContainerHighest = Color(0xFF443424),
    outline = Color(0xFF8C7C63),
    outlineVariant = Color(0xFF4A3C2B),
    error = StatusColors.BadDark,
    onError = Color(0xFF4A0008),
    inverseSurface = Brand.Parchment,
    inverseOnSurface = Brand.Ink,
    inversePrimary = Brand.GoldDark,
)

/**
 * Една скала за заоблянията в цялото приложение:
 * small 12 (миниатюри, етикети), medium 20 (карти), large 28 (големи снимки, лентата).
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Допълнителни цветове извън Material схемата. */
@Immutable
data class ExtendedColors(
    val ok: Color,
    val warn: Color,
    val bad: Color,
    val heroScrim: Color,
    val gold: Color,
    val isDark: Boolean,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(StatusColors.OkLight, StatusColors.WarnLight, StatusColors.BadLight, Brand.Ink, Brand.Gold, false)
}

enum class ThemeMode { SYSTEM, LIGHT, DARK }

@Composable
fun ChitalishteTheme(
    mode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val dark = when (mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val context = LocalContext.current
    val scheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        dark -> DarkColors
        else -> LightColors
    }
    val extended = if (dark) {
        ExtendedColors(StatusColors.OkDark, StatusColors.WarnDark, StatusColors.BadDark, Color.Black, Brand.Gold, true)
    } else {
        ExtendedColors(StatusColors.OkLight, StatusColors.WarnLight, StatusColors.BadLight, Brand.Ink, Brand.GoldDark, false)
    }
    CompositionLocalProvider(LocalExtendedColors provides extended) {
        MaterialTheme(colorScheme = scheme, typography = AppTypography, shapes = AppShapes, content = content)
    }
}
