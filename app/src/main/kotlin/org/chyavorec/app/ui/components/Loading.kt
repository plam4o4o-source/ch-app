package org.chyavorec.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Индикаторът „зарежда се“ на приложението — в духа на Material 3 Expressive
 * LoadingIndicator: мека форма („бисквитка“, петоъгълник, хапче, слънце…), която се
 * превръща в следващата и се завърта. Вместо стандартния кръг; само за неопределено
 * чакане на съдържание — дърпането за опресняване има житния клас ([WheatRefreshBox]),
 * а процентите (изтегляне) — линейна лента.
 *
 * При намалено движение — неподвижна „бисквитка“. За екранните четци е един елемент:
 * неопределен прогрес + [contentDescription] (по подразбиране „Зареждане…“).
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
    val path = remember { Path() }
    if (rememberReducedMotion()) {
        Canvas(a11y) { drawMorph(path, MorphShapes.all[0], MorphShapes.all[0], 0f, 0f, color) }
        return
    }
    val count = MorphShapes.all.size
    val phase = rememberInfiniteTransition(label = "loading").animateFloat(
        initialValue = 0f, targetValue = count.toFloat(),
        animationSpec = infiniteRepeatable(tween(count * STEP_MS, easing = LinearEasing)),
        label = "phase",
    )
    Canvas(a11y) {
        val ph = phase.value // четене само във фазата на рисуване
        val i = min(ph.toInt(), count - 1)
        val f = ph - i
        // Превръщането заема първите 75% от стъпката, после формата „почива“.
        val m = FastOutSlowInEasing.transform(min(f / 0.75f, 1f))
        // Въртенето ускорява по време на превръщането; след пълен цикъл е кратно на 360°.
        val rotation = (i + 0.6f * m + 0.4f * f) * STEP_DEG
        drawMorph(path, MorphShapes.all[i], MorphShapes.all[(i + 1) % count], m, rotation, color)
    }
}

private const val STEP_MS = 700
private const val STEP_DEG = 360f * 3f / 7f

/** Формите като радиус по ъгъла (0..1, POINTS стойности) — изчисляват се веднъж. */
private object MorphShapes {
    const val POINTS = 120
    val cosT = FloatArray(POINTS) { cos(2.0 * PI * it / POINTS).toFloat() }
    val sinT = FloatArray(POINTS) { sin(2.0 * PI * it / POINTS).toFloat() }

    /** Кръг с [n] меки издатини с дълбочина [depth]. */
    private fun lobes(n: Int, depth: Float) =
        FloatArray(POINTS) { (1f + depth * cos(2.0 * PI * n * it / POINTS).toFloat()) / (1f + depth) }

    /** Елипса (хапче) с полуоси 1 и [b]. */
    private fun oval(b: Float) =
        FloatArray(POINTS) { b / sqrt(b * b * cosT[it] * cosT[it] + sinT[it] * sinT[it]) }

    // Бисквитка 9, петоъгълник, хапче, слънце, бисквитка 4, триъгълник, звезда 12.
    val all = arrayOf(lobes(9, 0.08f), lobes(5, 0.1f), oval(0.64f), lobes(8, 0.045f), lobes(4, 0.11f), lobes(3, 0.13f), lobes(12, 0.06f))
}

/** Рисува междинната форма между [from] и [to] (дял [m]), завъртяна на [rotationDeg]. Без заделяне на памет. */
private fun DrawScope.drawMorph(path: Path, from: FloatArray, to: FloatArray, m: Float, rotationDeg: Float, color: Color) {
    // Пропорциите на Material: фигура 38 dp в поле 48 dp.
    val radius = min(size.width, size.height) / 2f * (38f / 48f)
    val rad = rotationDeg * (PI.toFloat() / 180f)
    val cr = cos(rad)
    val sr = sin(rad)
    val cx = size.width / 2f
    val cy = size.height / 2f
    path.rewind()
    for (k in 0 until MorphShapes.POINTS) {
        val r = radius * (from[k] + (to[k] - from[k]) * m)
        val x = cx + r * (MorphShapes.cosT[k] * cr - MorphShapes.sinT[k] * sr)
        val y = cy + r * (MorphShapes.sinT[k] * cr + MorphShapes.cosT[k] * sr)
        if (k == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color)
}
