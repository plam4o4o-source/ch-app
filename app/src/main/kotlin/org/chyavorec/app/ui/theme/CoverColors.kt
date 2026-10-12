package org.chyavorec.app.ui.theme

import android.graphics.Bitmap
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * „Пощенска кутия“ за корицата на отворената книга: [BookCover][org.chyavorec.app.ui.components.BookCover]
 * пуска тук заредената снимка (само за книгата [inv]), а [CoverTintedTheme] вади цветовете ѝ.
 */
@Stable
class CoverColorSink(val inv: Long) {
    var bitmap by mutableStateOf<Bitmap?>(null)
        private set

    fun offer(bitmap: Bitmap) {
        if (this.bitmap !== bitmap) this.bitmap = bitmap
    }
}

/** Подава се само от екрана с детайлите на книга; навсякъде другаде е `null`. */
val LocalCoverColorSink = staticCompositionLocalOf<CoverColorSink?> { null }

/**
 * Темата на екрана с детайлите следва истинската корица на книгата [inv]: щом
 * Coil я зареди, Palette (извън главната нишка) вади живия/преобладаващия цвят,
 * от него се прави хармонизирана схема (виж [CoverPalette]) и цветовете плавно
 * преминават към нея. Без корица, при черно-бяла корица или ако контрастът на
 * текста не стига 4.5:1 — остават цветовете на приложението.
 */
@Composable
fun CoverTintedTheme(inv: Long, content: @Composable () -> Unit) {
    val sink = remember(inv) { CoverColorSink(inv) }
    val base = MaterialTheme.colorScheme
    val dark = LocalExtendedColors.current.isDark
    val bitmap = sink.bitmap
    var seed by remember(inv) { mutableStateOf<Int?>(null) }
    LaunchedEffect(bitmap) {
        val b = bitmap ?: return@LaunchedEffect
        seed = withContext(Dispatchers.Default) { runCatching { extractSeed(b) }.getOrNull() }
    }
    val target = remember(seed, base, dark) {
        val s = seed ?: return@remember base
        val tones = CoverPalette.derive(s, base.toSchemeBase(dark)) ?: return@remember base
        base.withCover(tones)
    }
    // Анимира се винаги (броят извиквания е постоянен); без цветове от корицата е
    // просто темата на приложението.
    val colors = animateColorScheme(target)
    CompositionLocalProvider(LocalCoverColorSink provides sink) {
        MaterialTheme(
            colorScheme = if (seed == null) base else colors,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content,
        )
    }
}

/** Цветът-семе от снимката (Palette смалява снимката сама); `null` за черно-бяла корица. */
private fun extractSeed(source: Bitmap): Int? {
    if (source.isRecycled) return null
    // Palette чете пикселите — хардуерните bitmap-и (от кеша на Coil) не позволяват това.
    val bitmap = if (source.config == Bitmap.Config.HARDWARE) source.copy(Bitmap.Config.ARGB_8888, false) ?: return null else source
    val palette = Palette.from(bitmap).maximumColorCount(16).generate()
    return CoverPalette.chooseSeed(
        vibrant = palette.vibrantSwatch?.rgb,
        dominant = palette.dominantSwatch?.rgb,
        muted = palette.mutedSwatch?.rgb,
    )
}

private fun ColorScheme.toSchemeBase(dark: Boolean) = SchemeBase(
    dark = dark,
    primary = primary.toArgb(),
    background = background.toArgb(),
    onBackground = onBackground.toArgb(),
    surface = surface.toArgb(),
    onSurface = onSurface.toArgb(),
    surfaceVariant = surfaceVariant.toArgb(),
    onSurfaceVariant = onSurfaceVariant.toArgb(),
    surfaceContainerLowest = surfaceContainerLowest.toArgb(),
    surfaceContainerLow = surfaceContainerLow.toArgb(),
    surfaceContainer = surfaceContainer.toArgb(),
    surfaceContainerHigh = surfaceContainerHigh.toArgb(),
    surfaceContainerHighest = surfaceContainerHighest.toArgb(),
    // Черният OLED фон остава черен.
    tintSurfaces = !(dark && background == Color.Black),
)

private fun ColorScheme.withCover(t: CoverTones) = copy(
    primary = Color(t.primary),
    onPrimary = Color(t.onPrimary),
    primaryContainer = Color(t.primaryContainer),
    onPrimaryContainer = Color(t.onPrimaryContainer),
    inversePrimary = Color(t.inversePrimary),
    secondary = Color(t.secondary),
    onSecondary = Color(t.onSecondary),
    secondaryContainer = Color(t.secondaryContainer),
    onSecondaryContainer = Color(t.onSecondaryContainer),
    surfaceTint = Color(t.primary),
    background = Color(t.background),
    surface = Color(t.surface),
    surfaceVariant = Color(t.surfaceVariant),
    surfaceContainerLowest = Color(t.surfaceContainerLowest),
    surfaceContainerLow = Color(t.surfaceContainerLow),
    surfaceContainer = Color(t.surfaceContainer),
    surfaceContainerHigh = Color(t.surfaceContainerHigh),
    surfaceContainerHighest = Color(t.surfaceContainerHighest),
)
