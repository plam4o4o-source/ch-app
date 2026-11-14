package org.chyavorec.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import android.graphics.Matrix
import org.chyavorec.app.ui.theme.LocalExtendedColors
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R

/**
 * Позицията на отблясъка, обща за всички skeleton елементи на един екран
 * ([ShimmerHost]) — една безкрайна анимация вместо по една на всяко правоъгълниче.
 */
private val LocalShimmerX = staticCompositionLocalOf<State<Float>?> { null }

@Composable
private fun rememberShimmerX(): State<Float> = rememberInfiniteTransition(label = "shimmer").animateFloat(
    initialValue = -600f, targetValue = 1400f,
    animationSpec = infiniteRepeatable(tween(1300, easing = LinearEasing), RepeatMode.Restart), label = "x",
)

/** Обща анимация за всички [shimmer] вътре в [content]; при намалено движение — без анимация. */
@Composable
private fun ShimmerHost(content: @Composable () -> Unit) {
    if (rememberReducedMotion()) {
        content()
    } else {
        CompositionLocalProvider(LocalShimmerX provides rememberShimmerX(), content = content)
    }
}

/**
 * „Канава“ на skeleton-а: бледа шевица (плочката на [ShevitsaStill]) — като платно за бродиране,
 * върху което се чака. Плочката е по една на тема за процеса; в покой не струва нищо по кадри.
 */
internal object SkeletonCanvas {
    /** Бодът (dp) и периодът на шарката: малка плочка (~22 dp) — евтина за първото рисуване. */
    const val CELL_DP = 2.2f
    const val PERIOD = 10

    /** Колко плътна е канавата върху фона на skeleton-а. */
    const val ALPHA = 0.06f

    fun colors(dark: Boolean): ShevitsaColors = ShevitsaPalettes.ribbon(dark)

    fun brush(cellPx: Float, dark: Boolean): ShaderBrush {
        val side = PERIOD * cellPx
        val tile = ShevitsaStill.tile(WeaveSpec(side, side, cellPx, PERIOD.toFloat(), false, 1f, 0f, colors(dark)))
        val shader = ImageShader(tile.image, TileMode.Repeated, TileMode.Repeated)
        shader.setLocalMatrix(Matrix().apply { setScale(tile.width / tile.image.width, tile.height / tile.image.height) })
        return ShaderBrush(shader)
    }
}

/**
 * Shimmer ефект за skeleton зареждане: бледа канава (шевица при ~6%) под един общ отблясък.
 * [stitch] — един златен „бод“ през средата, който върви с отблясъка (за голямата плоскост на
 * екрана, не за всеки ред). Уважава системната настройка „Премахване на анимациите“ — тогава
 * е статичен. Позицията се чете само при рисуване (без рекомпозиция на всеки кадър); вътре в
 * [SkeletonList]/[SkeletonCards] всички елементи ползват една обща анимация.
 */
fun Modifier.shimmer(stitch: Boolean = false): Modifier = composed {
    val base = MaterialTheme.colorScheme.surfaceContainerHigh
    val highlight = MaterialTheme.colorScheme.surfaceContainerLowest
    val ext = LocalExtendedColors.current
    val reduced = rememberReducedMotion()
    val x = if (reduced) null else (LocalShimmerX.current ?: rememberShimmerX())
    // Краищата на отблясъка са прозрачни в същия цвят — канавата отдолу се вижда извън него.
    val colors = remember(highlight) { listOf(highlight.copy(alpha = 0f), highlight, highlight.copy(alpha = 0f)) }
    val gold = ext.gold
    val goldColors = remember(gold) { listOf(gold.copy(alpha = 0f), gold.copy(alpha = 0.8f), gold.copy(alpha = 0f)) }
    drawWithCache {
        val canvas = SkeletonCanvas.brush(SkeletonCanvas.CELL_DP.dp.toPx(), ext.isDark)
        // Бодът през средата — тропосване ([StitchSpec.Basting]), както подгъвът.
        val bod = StitchSpec.Basting.stroke(density)
        val thread = bod.width
        val dash = bod.pathEffect
        onDrawBehind {
            drawRect(base)
            drawRect(canvas, alpha = SkeletonCanvas.ALPHA)
            if (x != null) {
                val start = x.value
                drawRect(Brush.linearGradient(colors, start = Offset(start, 0f), end = Offset(start + 500f, 300f)))
            }
            if (stitch) {
                val y = size.height / 2f
                val from = Offset(0f, y)
                val to = Offset(size.width, y)
                if (x != null) {
                    val start = x.value
                    drawLine(Brush.horizontalGradient(goldColors, startX = start, endX = start + 500f), from, to, thread, StrokeCap.Round, dash)
                } else {
                    drawLine(gold, from, to, thread, StrokeCap.Round, dash, alpha = 0.45f)
                }
            }
        }
    }
}

@Composable
fun SkeletonBox(modifier: Modifier = Modifier, height: Dp = 16.dp, width: Dp? = null) {
    Box(
        modifier.then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
            .height(height).clip(RoundedCornerShape(8.dp)).shimmer(),
    )
}

@Composable
fun SkeletonList(rows: Int = 6, withImage: Boolean = true, modifier: Modifier = Modifier) = ShimmerHost {
    val desc = stringResource(R.string.loading)
    Column(
        modifier.fillMaxWidth().padding(16.dp).semantics { contentDescription = desc },
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        repeat(rows) { row ->
            Row {
                if (withImage) {
                    // Един златен бод на екрана — само върху първата корица.
                    Box(Modifier.size(72.dp, 96.dp).clip(MaterialTheme.shapes.small).shimmer(stitch = row == 0))
                    Spacer(Modifier.width(14.dp))
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SkeletonBox(height = 18.dp)
                    SkeletonBox(height = 14.dp, width = 160.dp)
                    SkeletonBox(height = 12.dp, width = 110.dp)
                }
            }
        }
    }
}

@Composable
fun SkeletonCards(modifier: Modifier = Modifier) = ShimmerHost {
    val desc = stringResource(R.string.loading)
    Column(modifier.fillMaxWidth().padding(16.dp).semantics { contentDescription = desc }, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(Modifier.fillMaxWidth().height(220.dp).clip(MaterialTheme.shapes.large).shimmer(stitch = true))
        SkeletonBox(height = 22.dp, width = 200.dp)
        SkeletonBox(height = 14.dp)
        SkeletonBox(height = 14.dp)
        SkeletonBox(height = 14.dp, width = 240.dp)
    }
}
