package org.chyavorec.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
 * Shimmer ефект за skeleton зареждане. Уважава системната настройка
 * „Премахване на анимациите“ — тогава е статичен. Позицията се чете само при
 * рисуване (без рекомпозиция на всеки кадър); вътре в [SkeletonList]/[SkeletonCards]
 * всички елементи ползват една обща анимация.
 */
fun Modifier.shimmer(): Modifier = composed {
    val base = MaterialTheme.colorScheme.surfaceContainerHigh
    val highlight = MaterialTheme.colorScheme.surfaceContainerLowest
    if (rememberReducedMotion()) return@composed background(base)
    val x = LocalShimmerX.current ?: rememberShimmerX()
    val colors = remember(base, highlight) { listOf(base, highlight, base) }
    drawBehind {
        val start = x.value
        drawRect(Brush.linearGradient(colors, start = Offset(start, 0f), end = Offset(start + 500f, 300f)))
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
        repeat(rows) {
            Row {
                if (withImage) {
                    Box(Modifier.size(72.dp, 96.dp).clip(MaterialTheme.shapes.small).shimmer())
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
        Box(Modifier.fillMaxWidth().height(220.dp).clip(MaterialTheme.shapes.large).shimmer())
        SkeletonBox(height = 22.dp, width = 200.dp)
        SkeletonBox(height = 14.dp)
        SkeletonBox(height = 14.dp)
        SkeletonBox(height = 14.dp, width = 240.dp)
    }
}
