package org.chyavorec.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.Brand

/**
 * Кратко анимирано въведение след системния splash (≈1,2 s, само при студен старт):
 * логото „изплува“, около него се изписва златен пръстен, после името на
 * читалището. При изключени анимации се пропуска изцяло.
 */
@Composable
fun BrandIntro(onFinished: () -> Unit) {
    val reduced = rememberReducedMotion()
    val logo = remember { Animatable(0f) }
    val ring = remember { Animatable(0f) }
    val text = remember { Animatable(0f) }
    val exit = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        if (reduced) { onFinished(); return@LaunchedEffect }
        coroutineScope {
            val a = async { logo.animateTo(1f, tween(520, easing = FastOutSlowInEasing)) }
            val b = async { delay(180); ring.animateTo(1f, tween(700, easing = LinearOutSlowInEasing)) }
            val c = async { delay(380); text.animateTo(1f, tween(460, easing = FastOutSlowInEasing)) }
            a.await(); b.await(); c.await()
        }
        delay(220)
        exit.animateTo(0f, tween(280))
        onFinished()
    }
    Box(
        Modifier.fillMaxSize().graphicsLayer { alpha = exit.value }
            .background(Brush.verticalGradient(listOf(Brand.Cream, androidx.compose.ui.graphics.Color.White, Brand.Parchment))),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(Modifier.size(196.dp)) {
                    drawArc(
                        brush = Brush.sweepGradient(listOf(Brand.GoldLight, Brand.Gold, Brand.GoldDark, Brand.Gold, Brand.GoldLight)),
                        startAngle = -90f,
                        sweepAngle = 360f * ring.value,
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                    )
                }
                Emblem(
                    168.dp,
                    Modifier.graphicsLayer {
                        val s = 0.72f + 0.28f * logo.value
                        scaleX = s; scaleY = s; alpha = logo.value
                        rotationZ = (1f - logo.value) * -12f
                    },
                )
            }
            Spacer(Modifier.height(28.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer { alpha = text.value; translationY = (1f - text.value) * 18.dp.toPx() },
            ) {
                Text(stringResource(R.string.app_title), style = MaterialTheme.typography.displaySmall, color = Brand.Ink, textAlign = TextAlign.Center)
                Text(stringResource(R.string.app_subtitle), style = MaterialTheme.typography.titleMedium, color = Brand.Burgundy, textAlign = TextAlign.Center)
                Spacer(Modifier.height(20.dp))
                // Малка отваряща се книга под името (векторна анимация, без външни файлове).
                OpeningBook(Modifier.size(96.dp, 64.dp))
            }
        }
    }
}
