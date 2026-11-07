package org.chyavorec.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath

/**
 * Формите на индикатора на избрания раздел, в единици = радиуса на розетката (18 dp):
 * кръг (началото — симетрично „разцъфване“) и осморъка — осемлъчата звезда от шевиците.
 */
internal object TabRosetteShapes {
    /** Радиусът на розетката (dp): 36 dp — колкото да не стига етикета под иконата. */
    const val UNIT_DP = 18f

    /** Кръг с 8 върха — всеки лъч на розетката „израства“ от свой връх. */
    val circle: RoundedPolygon = RoundedPolygon.circle(numVertices = 8, radius = 0.86f)

    /** Осморъка: 8 заоблени лъча, вътрешният радиус е голям — мека „розетка“, не бодлива звезда. */
    val rosette: RoundedPolygon = RoundedPolygon.star(
        numVerticesPerRadius = 8, radius = 1f, innerRadius = 0.8f,
        rounding = CornerRounding(0.2f, smoothing = 0.5f), innerRounding = CornerRounding(0.1f),
    )

    val morph = Morph(circle, rosette)
}

/**
 * Индикаторът на избрания раздел (долната лента и страничната): при избор кръгът се
 * „разцъфва“ в осморъка с леко завъртане и пружина, а вътре се появява пунктирен „бод“ —
 * като шевица. При отказ от избора — обратно към кръга и изчезва.
 * При намалено движение — направо крайната форма. Само рисуване: прогресът се чете във
 * фазата на рисуване, без рекомпозиция по кадри; пътят се строи наново само при промяна.
 */
@Composable
fun Modifier.tabRosette(selected: Boolean, reduced: Boolean, fill: Color, stitch: Color): Modifier {
    val progress = remember { Animatable(if (selected) 1f else 0f) }
    LaunchedEffect(selected, reduced) {
        val target = if (selected) 1f else 0f
        if (reduced) progress.snapTo(target)
        else progress.animateTo(target, if (selected) spring<Float>(dampingRatio = 0.55f, stiffness = 420f) else spring<Float>(stiffness = 900f))
    }
    val cache = remember { RosetteCache() }
    return drawBehind {
        val p = progress.value
        if (p <= 0.001f) return@drawBehind
        val m = p.coerceIn(0f, 1f)
        val path = cache.pathAt(m)
        val unit = TabRosetteShapes.UNIT_DP.dp.toPx()
        val alpha = (p * 3f).coerceIn(0f, 1f)
        // Растеж с лек „отскок“ (пружината минава над 1) и четвърт лъч завъртане.
        val grow = 0.55f + 0.45f * p
        translate(size.width / 2f, size.height / 2f) {
            rotate((1f - m) * -22.5f, pivot = Offset.Zero) {
                scale(unit * grow, unit * grow, pivot = Offset.Zero) {
                    drawPath(path, fill, alpha = alpha)
                    // Пунктирният бод по контура — едва в края на разцъфването.
                    val s = ((m - 0.6f) / 0.4f).coerceIn(0f, 1f)
                    if (s > 0f) {
                        scale(0.84f, 0.84f, pivot = Offset.Zero) {
                            drawPath(path, stitch, alpha = s * alpha, style = cache.stitchStroke(1.dp.toPx() / (unit * 0.84f)))
                        }
                    }
                }
            }
        }
    }
}

/** Пътят на междинната форма — един и същ обект, строи се наново само при нов прогрес. */
private class RosetteCache {
    private val platformPath = android.graphics.Path()
    private val path = platformPath.asComposePath()
    private var last = -1f
    private var strokeWidth = -1f
    private var stroke: Stroke? = null

    fun pathAt(progress: Float): androidx.compose.ui.graphics.Path {
        if (progress != last) {
            TabRosetteShapes.morph.toPath(progress, platformPath)
            last = progress
        }
        return path
    }

    /** Пунктир в единиците на формата (0.12 ≈ 2 dp бод), създава се веднъж за дебелината. */
    fun stitchStroke(width: Float): Stroke {
        val s = stroke
        if (s != null && width == strokeWidth) return s
        strokeWidth = width
        return Stroke(width, pathEffect = PathEffect.dashPathEffect(floatArrayOf(0.12f, 0.08f))).also { stroke = it }
    }
}
