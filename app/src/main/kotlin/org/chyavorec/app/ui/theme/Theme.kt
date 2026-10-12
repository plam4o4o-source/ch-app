package org.chyavorec.app.ui.theme

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.view.accessibility.AccessibilityManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import org.chyavorec.app.ui.components.rememberReducedMotion

/* ---- „Яворец“ — злато и жито (цветовете на сайта; по подразбиране) ---- */

private val YavoretsLight = lightColorScheme(
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

private val YavoretsDark = darkColorScheme(
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

/* ---- „Левски“ — тъмнозелено и сребро ---- */

private val LevskiLight = lightColorScheme(
    primary = Color(0xFF1E5B3A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCDE8D6),
    onPrimaryContainer = Color(0xFF062814),
    secondary = Color(0xFF56616B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDDE3E8),
    onSecondaryContainer = Color(0xFF161D23),
    tertiary = Color(0xFF7A5C10),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF2E2B8),
    onTertiaryContainer = Color(0xFF2A1E00),
    background = Color(0xFFF6F8F6),
    onBackground = Color(0xFF121A15),
    surface = Color(0xFFF6F8F6),
    onSurface = Color(0xFF121A15),
    surfaceVariant = Color(0xFFE8EDEA),
    onSurfaceVariant = Color(0xFF414B45),
    surfaceTint = Color(0xFF1E5B3A),
    surfaceBright = Color(0xFFF6F8F6),
    surfaceDim = Color(0xFFD6DCD8),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF1F4F2),
    surfaceContainer = Color(0xFFEBEFEC),
    surfaceContainerHigh = Color(0xFFE4E9E6),
    surfaceContainerHighest = Color(0xFFDDE3DF),
    outline = Color(0xFF8A958E),
    outlineVariant = Color(0xFFC9D1CC),
    error = StatusColors.BadLight,
    onError = Color.White,
    inverseSurface = Color(0xFF2A322D),
    inverseOnSurface = Color(0xFFEEF2EF),
    inversePrimary = Color(0xFF9ED3B2),
)

private val LevskiDark = darkColorScheme(
    primary = Color(0xFF9ED3B2),
    onPrimary = Color(0xFF003920),
    primaryContainer = Color(0xFF1B4D33),
    onPrimaryContainer = Color(0xFFC9EBD5),
    secondary = Color(0xFFC3CCD4),
    onSecondary = Color(0xFF2A3138),
    secondaryContainer = Color(0xFF414A52),
    onSecondaryContainer = Color(0xFFDDE3E9),
    tertiary = Color(0xFFE3C46E),
    onTertiary = Color(0xFF3B2F00),
    tertiaryContainer = Color(0xFF554500),
    onTertiaryContainer = Color(0xFFF7E0A0),
    background = Color(0xFF0F1511),
    onBackground = Color(0xFFDEE4DF),
    surface = Color(0xFF0F1511),
    onSurface = Color(0xFFDEE4DF),
    surfaceVariant = Color(0xFF1F2823),
    onSurfaceVariant = Color(0xFFBFC9C2),
    surfaceTint = Color(0xFF9ED3B2),
    surfaceBright = Color(0xFF353B37),
    surfaceDim = Color(0xFF0F1511),
    surfaceContainerLowest = Color(0xFF0A0F0C),
    surfaceContainerLow = Color(0xFF171E1A),
    surfaceContainer = Color(0xFF1B231E),
    surfaceContainerHigh = Color(0xFF252E28),
    surfaceContainerHighest = Color(0xFF303933),
    outline = Color(0xFF89938C),
    outlineVariant = Color(0xFF3F4943),
    error = StatusColors.BadDark,
    onError = Color(0xFF4A0008),
    inverseSurface = Color(0xFFDEE4DF),
    inverseOnSurface = Color(0xFF2C322E),
    inversePrimary = Color(0xFF1E5B3A),
)

/* ---- „Шевица“ — червено, черно и слонова кост ---- */

private val ShevitsaLight = lightColorScheme(
    primary = Color(0xFF9C1C24),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD7),
    onPrimaryContainer = Color(0xFF410006),
    secondary = Color(0xFF2B2422),
    onSecondary = Color(0xFFFFF8F0),
    secondaryContainer = Color(0xFFE9E1D8),
    onSecondaryContainer = Color(0xFF1E1814),
    tertiary = Color(0xFF7A5A12),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF6E1B0),
    onTertiaryContainer = Color(0xFF271900),
    background = Color(0xFFFFFBF3),
    onBackground = Color(0xFF1C1716),
    surface = Color(0xFFFFFBF3),
    onSurface = Color(0xFF1C1716),
    surfaceVariant = Color(0xFFF3EBE0),
    onSurfaceVariant = Color(0xFF54463F),
    surfaceTint = Color(0xFF9C1C24),
    surfaceBright = Color(0xFFFFFBF3),
    surfaceDim = Color(0xFFE2D8CC),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFCF6EC),
    surfaceContainer = Color(0xFFF6EFE4),
    surfaceContainerHigh = Color(0xFFF0E8DC),
    surfaceContainerHighest = Color(0xFFEAE1D4),
    outline = Color(0xFFA08C80),
    outlineVariant = Color(0xFFE0D3C6),
    error = Color(0xFF8C1D18),
    onError = Color.White,
    inverseSurface = Color(0xFF1C1716),
    inverseOnSurface = Color(0xFFF7EFE6),
    inversePrimary = Color(0xFFFFB3AD),
)

private val ShevitsaDark = darkColorScheme(
    primary = Color(0xFFFFB3AD),
    onPrimary = Color(0xFF68000F),
    primaryContainer = Color(0xFF7F1019),
    onPrimaryContainer = Color(0xFFFFDAD7),
    secondary = Color(0xFFE9E1D8),
    onSecondary = Color(0xFF2B2422),
    secondaryContainer = Color(0xFF3A3230),
    onSecondaryContainer = Color(0xFFF2EAE1),
    tertiary = Color(0xFFE8C374),
    onTertiary = Color(0xFF402D00),
    tertiaryContainer = Color(0xFF5C4300),
    onTertiaryContainer = Color(0xFFFFDF9E),
    background = Color(0xFF120D0C),
    onBackground = Color(0xFFEFE6E1),
    surface = Color(0xFF120D0C),
    onSurface = Color(0xFFEFE6E1),
    surfaceVariant = Color(0xFF2A2120),
    onSurfaceVariant = Color(0xFFD8C8C2),
    surfaceTint = Color(0xFFFFB3AD),
    surfaceBright = Color(0xFF3A3130),
    surfaceDim = Color(0xFF120D0C),
    surfaceContainerLowest = Color(0xFF0B0707),
    surfaceContainerLow = Color(0xFF1B1514),
    surfaceContainer = Color(0xFF211918),
    surfaceContainerHigh = Color(0xFF2C2322),
    surfaceContainerHighest = Color(0xFF372D2C),
    outline = Color(0xFFA08C86),
    outlineVariant = Color(0xFF4E4340),
    error = StatusColors.BadDark,
    onError = Color(0xFF4A0008),
    inverseSurface = Color(0xFFEFE6E1),
    inverseOnSurface = Color(0xFF2C2322),
    inversePrimary = Color(0xFF9C1C24),
)

/** Схемата на палитрата (без OLED и висок контраст) — и за мострите в настройките. */
fun paletteScheme(palette: ThemePalette, dark: Boolean): ColorScheme = when (palette) {
    ThemePalette.YAVORETS -> if (dark) YavoretsDark else YavoretsLight
    ThemePalette.LEVSKI -> if (dark) LevskiDark else LevskiLight
    ThemePalette.SHEVITSA -> if (dark) ShevitsaDark else ShevitsaLight
}

/** Акцентът на палитрата (етикети, икони, точки): злато, сребро или червено — проверен за 4.5:1 върху фона. */
fun paletteAccent(palette: ThemePalette, dark: Boolean): Color = when (palette) {
    ThemePalette.YAVORETS -> if (dark) Brand.Gold else Brand.GoldDark
    ThemePalette.LEVSKI -> if (dark) Color(0xFFC3CCD4) else Color(0xFF56616B)
    ThemePalette.SHEVITSA -> if (dark) Color(0xFFFFB3AD) else Color(0xFF9C1C24)
}

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

/** Избраната палитра (за генерираните корици и мострите в настройките). */
val LocalThemeStyle = staticCompositionLocalOf { ThemeStyle() }

enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** Продължителност на прехода между две схеми (смяна на палитра/тема, цветове от корица). */
private const val SCHEME_ANIMATION_MS = 450

@Composable
fun ChitalishteTheme(
    mode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    style: ThemeStyle = ThemeStyle(),
    content: @Composable () -> Unit,
) {
    val dark = when (mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val context = LocalContext.current
    val dynamic = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val highContrast = observeHighContrast()
    val scheme: ColorScheme = remember(dark, dynamic, style, highContrast, context) {
        var s = when {
            dynamic -> if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            else -> paletteScheme(style.palette, dark)
        }
        if (dark && style.oledBlack) s = s.toOledBlack()
        if (highContrast) s = s.toHighContrast(dark)
        s
    }
    val accent = paletteAccent(style.palette, dark)
    val extended = if (dark) {
        ExtendedColors(StatusColors.OkDark, StatusColors.WarnDark, StatusColors.BadDark, Color.Black, accent, true)
    } else {
        ExtendedColors(StatusColors.OkLight, StatusColors.WarnLight, StatusColors.BadLight, Brand.Ink, accent, false)
    }
    val colors = animateColorScheme(scheme)
    CompositionLocalProvider(LocalExtendedColors provides extended, LocalThemeStyle provides style) {
        MaterialTheme(colorScheme = colors, typography = AppTypography, shapes = AppShapes, content = content)
    }
}

/**
 * Всеки цвят от [target] се анимира поотделно (при намалено движение — веднага).
 * Първата стойност е самата схема — при стартиране няма преход. Щом всички цветове
 * стигнат целта, се връща самият [target] (същата инстанция) — темата не се
 * „сменя“ при всяка рекомпозиция и дървото под нея не се преначертава излишно.
 */
@Composable
fun animateColorScheme(target: ColorScheme): ColorScheme {
    val reduced = rememberReducedMotion()
    val spec = remember<AnimationSpec<Color>>(reduced) { if (reduced) snap() else tween(SCHEME_ANIMATION_MS, easing = FastOutSlowInEasing) }
    val primary = animated(target.primary, spec, "primary")
    val onPrimary = animated(target.onPrimary, spec, "onPrimary")
    val primaryContainer = animated(target.primaryContainer, spec, "primaryContainer")
    val onPrimaryContainer = animated(target.onPrimaryContainer, spec, "onPrimaryContainer")
    val inversePrimary = animated(target.inversePrimary, spec, "inversePrimary")
    val secondary = animated(target.secondary, spec, "secondary")
    val onSecondary = animated(target.onSecondary, spec, "onSecondary")
    val secondaryContainer = animated(target.secondaryContainer, spec, "secondaryContainer")
    val onSecondaryContainer = animated(target.onSecondaryContainer, spec, "onSecondaryContainer")
    val tertiary = animated(target.tertiary, spec, "tertiary")
    val onTertiary = animated(target.onTertiary, spec, "onTertiary")
    val tertiaryContainer = animated(target.tertiaryContainer, spec, "tertiaryContainer")
    val onTertiaryContainer = animated(target.onTertiaryContainer, spec, "onTertiaryContainer")
    val background = animated(target.background, spec, "background")
    val onBackground = animated(target.onBackground, spec, "onBackground")
    val surface = animated(target.surface, spec, "surface")
    val onSurface = animated(target.onSurface, spec, "onSurface")
    val surfaceVariant = animated(target.surfaceVariant, spec, "surfaceVariant")
    val onSurfaceVariant = animated(target.onSurfaceVariant, spec, "onSurfaceVariant")
    val surfaceTint = animated(target.surfaceTint, spec, "surfaceTint")
    val inverseSurface = animated(target.inverseSurface, spec, "inverseSurface")
    val inverseOnSurface = animated(target.inverseOnSurface, spec, "inverseOnSurface")
    val error = animated(target.error, spec, "error")
    val onError = animated(target.onError, spec, "onError")
    val errorContainer = animated(target.errorContainer, spec, "errorContainer")
    val onErrorContainer = animated(target.onErrorContainer, spec, "onErrorContainer")
    val outline = animated(target.outline, spec, "outline")
    val outlineVariant = animated(target.outlineVariant, spec, "outlineVariant")
    val scrim = animated(target.scrim, spec, "scrim")
    val surfaceBright = animated(target.surfaceBright, spec, "surfaceBright")
    val surfaceDim = animated(target.surfaceDim, spec, "surfaceDim")
    val surfaceContainer = animated(target.surfaceContainer, spec, "surfaceContainer")
    val surfaceContainerHigh = animated(target.surfaceContainerHigh, spec, "surfaceContainerHigh")
    val surfaceContainerHighest = animated(target.surfaceContainerHighest, spec, "surfaceContainerHighest")
    val surfaceContainerLow = animated(target.surfaceContainerLow, spec, "surfaceContainerLow")
    val surfaceContainerLowest = animated(target.surfaceContainerLowest, spec, "surfaceContainerLowest")
    val settled = primary == target.primary &&
        onPrimary == target.onPrimary &&
        primaryContainer == target.primaryContainer &&
        onPrimaryContainer == target.onPrimaryContainer &&
        inversePrimary == target.inversePrimary &&
        secondary == target.secondary &&
        onSecondary == target.onSecondary &&
        secondaryContainer == target.secondaryContainer &&
        onSecondaryContainer == target.onSecondaryContainer &&
        tertiary == target.tertiary &&
        onTertiary == target.onTertiary &&
        tertiaryContainer == target.tertiaryContainer &&
        onTertiaryContainer == target.onTertiaryContainer &&
        background == target.background &&
        onBackground == target.onBackground &&
        surface == target.surface &&
        onSurface == target.onSurface &&
        surfaceVariant == target.surfaceVariant &&
        onSurfaceVariant == target.onSurfaceVariant &&
        surfaceTint == target.surfaceTint &&
        inverseSurface == target.inverseSurface &&
        inverseOnSurface == target.inverseOnSurface &&
        error == target.error &&
        onError == target.onError &&
        errorContainer == target.errorContainer &&
        onErrorContainer == target.onErrorContainer &&
        outline == target.outline &&
        outlineVariant == target.outlineVariant &&
        scrim == target.scrim &&
        surfaceBright == target.surfaceBright &&
        surfaceDim == target.surfaceDim &&
        surfaceContainer == target.surfaceContainer &&
        surfaceContainerHigh == target.surfaceContainerHigh &&
        surfaceContainerHighest == target.surfaceContainerHighest &&
        surfaceContainerLow == target.surfaceContainerLow &&
        surfaceContainerLowest == target.surfaceContainerLowest
    if (settled) return target
    return target.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceDim = surfaceDim,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainerLowest = surfaceContainerLowest,
    )
}

@Composable
private fun animated(color: Color, spec: AnimationSpec<Color>, label: String): Color =
    animateColorAsState(color, spec, label = label).value

/** Тъмна схема с чисто черен фон (OLED): фоновете стават черни, контейнерите — почти черни. */
private fun ColorScheme.toOledBlack(): ColorScheme = copy(
    background = Color.Black,
    surface = Color.Black,
    surfaceDim = Color.Black,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = lerp(surfaceContainerLow, Color.Black, 0.6f),
    surfaceContainer = lerp(surfaceContainer, Color.Black, 0.5f),
    surfaceContainerHigh = lerp(surfaceContainerHigh, Color.Black, 0.4f),
    surfaceContainerHighest = lerp(surfaceContainerHighest, Color.Black, 0.3f),
    surfaceVariant = lerp(surfaceVariant, Color.Black, 0.4f),
    surfaceBright = lerp(surfaceBright, Color.Black, 0.3f),
)

/**
 * Висок контраст (системната настройка): второстепенният текст става като основния,
 * очертанията — по-плътни, акцентите — по-близо до текста.
 */
private fun ColorScheme.toHighContrast(dark: Boolean): ColorScheme {
    val ink = onSurface
    return copy(
        onSurfaceVariant = ink,
        onBackground = ink,
        outline = lerp(outline, ink, 0.5f),
        outlineVariant = lerp(outlineVariant, ink, 0.35f),
        primary = lerp(primary, ink, if (dark) 0.25f else 0.3f),
        secondary = lerp(secondary, ink, if (dark) 0.25f else 0.3f),
    )
}

/**
 * Дали потребителят е поискал висок контраст: „Текст с висок контраст“ (публичен API
 * от Android 16 — чете се с reflection, за да не зависи компилацията от точното SDK)
 * или системното ниво на контраста от Android 14 (UiModeManager.getContrast ≥ 0.5,
 * т.е. „средно“ или „високо“). Опреснява се при връщане към приложението.
 */
@Composable
private fun observeHighContrast(): Boolean {
    val context = LocalContext.current
    var on by remember { mutableStateOf(readHighContrast(context)) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { on = readHighContrast(context) }
    return on
}

private fun readHighContrast(context: Context): Boolean = runCatching {
    if (Build.VERSION.SDK_INT >= 36) {
        val am = context.getSystemService(AccessibilityManager::class.java)
        val text = runCatching {
            AccessibilityManager::class.java.getMethod("isHighContrastTextEnabled").invoke(am) as? Boolean
        }.getOrNull()
        if (text == true) return@runCatching true
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        val ui = context.getSystemService(UiModeManager::class.java)
        if (ui != null && ui.contrast >= 0.5f) return@runCatching true
    }
    false
}.getOrDefault(false)
