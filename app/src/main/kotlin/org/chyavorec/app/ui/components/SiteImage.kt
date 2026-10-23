package org.chyavorec.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import org.chyavorec.data.site.SiteImageUrls

/**
 * Снимка от chyavorec.org (новини, галерия, събития, страници) в размера, в който се
 * показва: [width] е ширината на мястото в dp (за мрежи — ширината на клетката).
 * Зарежда оптимизирания от Vercel вариант ([SiteImageUrls]); ако той върне грешка —
 * еднократно оригинала (чрез `fallbackUrl` на [RemoteImage]). Адреси извън сайта се
 * зареждат както досега.
 */
@Composable
fun SiteImage(
    url: String?,
    width: Dp,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    onFailure: (() -> Unit)? = null,
) {
    val widthPx = with(LocalDensity.current) { width.roundToPx() }
    val optimized = remember(url, widthPx) { SiteImageUrls.optimized(url, widthPx) }
    RemoteImage(
        url = optimized,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        // Само ако адресът е сменен — иначе грешката отива направо в onFailure.
        fallbackUrl = url.takeIf { it != optimized },
        containerColor = containerColor,
        onFailure = onFailure,
    )
}

/** Ширината на прозореца в dp — за снимки на цяла ширина (без отстъпите). */
@Suppress("ConfigurationScreenWidthHeight")
@Composable
fun windowWidth(): Dp {
    val px = LocalWindowInfo.current.containerSize.width
    // Преди първото измерване размерът може да е 0 — тогава от конфигурацията,
    // за да не се изтегли веднъж оригиналът и после пак оптимизираният вариант.
    return if (px > 0) with(LocalDensity.current) { px.toDp() } else LocalConfiguration.current.screenWidthDp.dp
}

/** Височината на прозореца в dp (преди първото измерване — от конфигурацията, както [windowWidth]). */
@Suppress("ConfigurationScreenWidthHeight")
@Composable
fun windowHeight(): Dp {
    val px = LocalWindowInfo.current.containerSize.height
    return if (px > 0) with(LocalDensity.current) { px.toDp() } else LocalConfiguration.current.screenHeightDp.dp
}

/**
 * Ширина на клетка в `GridCells.Adaptive(minSize)` при налична ширина [available]
 * (без отстъпите на мрежата) и разстояние [spacing] между клетките — същата сметка
 * като в Compose: брой = max(1, (available + spacing) / (minSize + spacing)).
 */
fun adaptiveCellWidth(available: Dp, minSize: Dp, spacing: Dp = 0.dp): Dp {
    val count = ((available + spacing) / (minSize + spacing)).toInt().coerceAtLeast(1)
    return ((available - spacing * (count - 1)) / count).coerceAtLeast(1.dp)
}
