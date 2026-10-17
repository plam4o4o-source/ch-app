package org.chyavorec.app.ui.screens.scan

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.lerp
import kotlinx.coroutines.delay
import org.chyavorec.app.ui.components.rememberReducedMotion
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.theme.Brand

/** Какво показва визьорът: търсене, намерена книга или „няма я“. */
internal enum class ViewfinderMode { SCANNING, HIT, MISS }

/**
 * Затъмнен фон с прозорец със заоблени ъгли и златни „скоби“ по ъглите.
 *
 * Докато се търси, скобите леко „дишат“. При разчетен код (ZXing не дава удобно
 * положението му, а потребителят държи кода в центъра) скобите се „захващат“ —
 * свиват се към центъра на прозореца, от него излиза златна вълна и се изписва
 * отметка; при „няма я“ скобите се разклащат. Всички стойности се четат само
 * при рисуване. При намалено движение — без дишане и разклащане, а захващането
 * е мигновено.
 */
@Composable
internal fun ViewfinderOverlay(mode: ViewfinderMode, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    val lock = remember { Animatable(0f) } // 0 — свободни скоби, 1 — захванати
    val pulse = remember { Animatable(0f) } // вълна 0 → 1
    val check = remember { Animatable(0f) } // изписване на отметката
    val shake = remember { Animatable(0f) } // хоризонтално отместване (dp)
    LaunchedEffect(mode, reduced) {
        when (mode) {
            ViewfinderMode.SCANNING -> {
                check.snapTo(0f); pulse.snapTo(0f); shake.snapTo(0f)
                if (reduced) lock.snapTo(0f) else lock.animateTo(0f, tween(250))
            }
            ViewfinderMode.HIT -> {
                if (reduced) {
                    lock.snapTo(1f); check.snapTo(1f)
                } else {
                    launch { lock.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow)) }
                    launch { pulse.snapTo(0f); pulse.animateTo(1f, tween(650, easing = FastOutSlowInEasing)) }
                    delay(120)
                    check.animateTo(1f, tween(320, easing = FastOutSlowInEasing))
                }
            }
            ViewfinderMode.MISS -> {
                if (!reduced) {
                    shake.animateTo(0f, keyframes {
                        durationMillis = 420
                        -10f at 60
                        9f at 140
                        -6f at 220
                        4f at 300
                        0f at 420
                    })
                }
            }
        }
    }
    val breathing = if (mode == ViewfinderMode.SCANNING && !reduced) {
        rememberInfiniteTransition(label = "viewfinder").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "breath",
        )
    } else {
        null
    }

    Canvas(modifier) {
        // Същият прозорец, до който анализаторът изрязва кадъра (scanWindow).
        val area = scanWindow(size.width, size.height)
        val w = area.width
        val h = area.height
        val left = area.left
        val top = area.top
        val radius = 22.dp.toPx()
        val window = Path().apply { addRoundRect(RoundRect(left, top, left + w, top + h, CornerRadius(radius))) }
        clipPath(window, ClipOp.Difference) { drawRect(Color.Black.copy(alpha = 0.55f)) }
        drawRoundRect(
            Brand.Gold.copy(alpha = 0.35f), Offset(left, top), Size(w, h), CornerRadius(radius),
            style = Stroke(1.5.dp.toPx()),
        )

        // Скобите: дишане (±2 %), захващане (до 62 % от прозореца, към квадрат около центъра), разклащане.
        val l = lock.value
        val breath = breathing?.value ?: 0f
        val cx = size.width / 2f + shake.value.dp.toPx()
        val cy = top + h / 2f
        val target = minOf(w, h) * 0.62f
        val bw = (w + (target - w) * l) * (1f - 0.02f * breath)
        val bh = (h + (target - h) * l) * (1f - 0.02f * breath)
        val bl = cx - bw / 2f
        val bt = cy - bh / 2f
        val armR = radius * (1f - 0.3f * l)
        val len = 28.dp.toPx()
        val stroke = Stroke((4f + 1.5f * l).dp.toPx(), cap = StrokeCap.Round)
        val armColor = lerp(Brand.Gold, Brand.GoldLight, l.coerceIn(0f, 1f))
        val corner = Path()
        fun arm(x0: Float, y0: Float, dx: Float, dy: Float) {
            corner.reset()
            corner.moveTo(x0 + dx * len, y0)
            corner.lineTo(x0 + dx * armR, y0)
            corner.quadraticTo(x0, y0, x0, y0 + dy * armR)
            corner.lineTo(x0, y0 + dy * len)
            drawPath(corner, armColor, style = stroke)
        }
        arm(bl, bt, 1f, 1f)
        arm(bl + bw, bt, -1f, 1f)
        arm(bl, bt + bh, 1f, -1f)
        arm(bl + bw, bt + bh, -1f, -1f)

        // Вълна от захванатия код.
        val p = pulse.value
        if (p > 0f && p < 1f) {
            val grow = 1f + 0.5f * p
            drawRoundRect(
                Brand.GoldLight.copy(alpha = 0.6f * (1f - p)),
                topLeft = Offset(cx - bw * grow / 2f, cy - bh * grow / 2f),
                size = Size(bw * grow, bh * grow),
                cornerRadius = CornerRadius(armR * grow),
                style = Stroke(3.dp.toPx()),
            )
        }

        // Отметка в златен кръг: първо кръгът, после двете рамена на отметката.
        val c = check.value
        if (c > 0f) {
            val r = 26.dp.toPx()
            drawCircle(Brand.Gold.copy(alpha = 0.92f * c.coerceAtMost(1f)), radius = r * (0.6f + 0.4f * c), center = Offset(cx, cy))
            val a = Offset(cx - r * 0.42f, cy + r * 0.02f)
            val b = Offset(cx - r * 0.1f, cy + r * 0.34f)
            val e = Offset(cx + r * 0.45f, cy - r * 0.3f)
            val first = (c / 0.4f).coerceIn(0f, 1f)
            val second = ((c - 0.4f) / 0.6f).coerceIn(0f, 1f)
            val tickStroke = 4.dp.toPx()
            drawLine(Brand.Ink, a, androidx.compose.ui.geometry.lerp(a, b, first), strokeWidth = tickStroke, cap = StrokeCap.Round)
            if (second > 0f) drawLine(Brand.Ink, b, androidx.compose.ui.geometry.lerp(b, e, second), strokeWidth = tickStroke, cap = StrokeCap.Round)
        }
    }
}
