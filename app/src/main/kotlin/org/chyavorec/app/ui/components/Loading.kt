package org.chyavorec.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R

/**
 * Индикаторът „зарежда се“ на приложението — народна форма, която се превръща в следваща и
 * се върти: кръг → осморъка → ромб → елбетица ([FolkLoaderShapes]), а около нея бавно се
 * върти пунктиран „бод“. Вместо стандартния кръг; само за неопределено чакане на
 * съдържание — дърпането за опресняване има яворовия лист ([LeafRefreshBox]), а процентите
 * (изтегляне) — линейна лента.
 *
 * При намалено движение — неподвижна осморъка с пунктирен бод. За екранните четци е един
 * елемент: неопределен прогрес + [contentDescription] (по подразбиране „Зареждане…“).
 */
@Composable
fun ChLoadingIndicator(
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.loading),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val a11y = modifier.clearAndSetSemantics {
        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        if (contentDescription != null) this.contentDescription = contentDescription
    }.size(48.dp)
    val cache = remember { FolkLoaderCache() }
    if (rememberReducedMotion()) {
        // Осморъката в покой (втората форма).
        Canvas(a11y) { drawFolkLoader(cache, 1f, color, spin = false) }
        return
    }
    val count = FolkLoaderShapes.count
    val phase = rememberInfiniteTransition(label = "loading").animateFloat(
        initialValue = 0f, targetValue = count.toFloat(),
        animationSpec = infiniteRepeatable(tween(count * STEP_MS, easing = LinearEasing)),
        label = "phase",
    )
    // Фазата се чете само във фазата на рисуване — без рекомпозиция по кадри.
    Canvas(a11y) { drawFolkLoader(cache, phase.value, color) }
}

private const val STEP_MS = 700
