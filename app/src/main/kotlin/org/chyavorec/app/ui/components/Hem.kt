package org.chyavorec.app.ui.components

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.Brand

/*
 * Подгъвът на жеста „назад“: докато екранът се смалява под пръста, по ръба му тече златен
 * ред от бодове (като подгъв на плат), който се „разпаря“ с прогреса на жеста. Рисува се
 * само при излизане с жест (Android 14+: само там има прогрес), само докато ъглите са
 * заоблени; пътят, ефектът и четката се строят веднъж за размера (без алокации на кадър),
 * а в покой модификаторът не прави нищо.
 */

/** Правилата на подгъва — чисти изчисления (тестват се без Compose). */
internal object HemSpec {
    /** Колко навътре от ръба е редът, и радиусът на ъглите му (ъгълът на екрана е 28 dp). */
    const val INSET_DP = 6f
    const val CORNER_DP = 22f

    /** Колко от реда е още зашит при прогрес [p] на заобляне (0..1): от цял до почти нищо. */
    fun stitched(p: Float): Float = (1f - 0.92f * p).coerceIn(0.08f, 1f)

    /** Появата: пълна непрозрачност след първите ~12 % от жеста, без „щракване“ в първия кадър. */
    fun alpha(p: Float): Float = (p * 8f).coerceIn(0f, 1f) * 0.92f
}

/** Помни дали излизането е с жест: след като е видян прогрес, подгъвът остава до края на излизането. */
@Stable
internal class HemState {
    var gesture = false
}

private class HemGeometry(
    val path: Path,
    val measure: PathMeasure,
    val length: Float,
    val segment: Path,
    val stroke: Stroke,
)

private fun Density.hemGeometry(width: Float, height: Float): HemGeometry {
    val inset = HemSpec.INSET_DP.dp.toPx()
    val path = Path().apply {
        addRoundRect(RoundRect(inset, inset, (width - inset).coerceAtLeast(inset), (height - inset).coerceAtLeast(inset), CornerRadius(HemSpec.CORNER_DP.dp.toPx())))
    }
    val measure = PathMeasure().also { it.setPath(path, false) }
    // Подгъвът е тропосване — общият бод за подгъви и перфорации.
    val stroke = StitchSpec.Basting.stroke(density)
    return HemGeometry(path, measure, measure.length, Path(), stroke)
}

/**
 * Златният ред по ръба. [progress] — заоблеността на ъглите (0..1; 0 — в покой, нищо не се рисува);
 * [leaving] — екранът излиза; [seeking] — излизането следва пръста (жест; преходът е спрян, не тече). Всичко се чете при
 * рисуване, затова нищо не прекомпозира екрана.
 */
internal fun Modifier.stitchedHem(
    state: HemState,
    progress: () -> Float,
    leaving: () -> Boolean,
    seeking: () -> Boolean,
): Modifier = drawWithCache {
    // Строи се чак при първата нужда — в покой не е алокирано нищо.
    val hem = lazy(LazyThreadSafetyMode.NONE) { hemGeometry(size.width, size.height) }
    onDrawWithContent {
        drawContent()
        val p = progress()
        if (p <= 0.001f) {
            state.gesture = false
            return@onDrawWithContent
        }
        val out = leaving()
        if (!out) state.gesture = false else if (seeking()) state.gesture = true
        if (!state.gesture || !out) return@onDrawWithContent
        val g = hem.value
        g.segment.rewind()
        g.measure.getSegment(0f, g.length * HemSpec.stitched(p), g.segment, true)
        drawPath(g.segment, Brand.Gold, alpha = HemSpec.alpha(p), style = g.stroke)
    }
}
