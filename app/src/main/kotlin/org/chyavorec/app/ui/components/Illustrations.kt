package org.chyavorec.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import org.chyavorec.app.ui.theme.Brand
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/*
 * Векторни анимации (Canvas) вместо Lottie: без външни файлове, с цветовете на марката.
 * При намалено движение показват статичен краен кадър.
 */

/** 0→1 за [halfPeriodMs] и обратно (нежен цикъл); при намалено движение — постоянно [staticValue]. */
@Composable
private fun loopProgress(halfPeriodMs: Int, staticValue: Float, reverse: Boolean = true): State<Float> {
    if (rememberReducedMotion()) return remember { mutableFloatStateOf(staticValue) }
    val transition = rememberInfiniteTransition(label = "illustration")
    return transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(halfPeriodMs, easing = if (reverse) FastOutSlowInEasing else LinearEasing),
            if (reverse) RepeatMode.Reverse else RepeatMode.Restart,
        ),
        label = "progress",
    )
}

/** Книга, чиято корица се отваря и страниците се разлистват (≈3 s цикъл). */
@Composable
fun OpeningBook(modifier: Modifier = Modifier) {
    val p by loopProgress(halfPeriodMs = 1500, staticValue = 1f)
    Canvas(modifier) {
        val bookW = size.width * 0.74f
        val bookH = size.height * 0.64f
        val top = (size.height - bookH) / 2f
        val spine = Offset(size.width / 2f, top + bookH / 2f)
        val half = bookW / 2f
        // Сянка под книгата.
        drawOval(Brand.Ink.copy(alpha = 0.10f), Offset(spine.x - bookW * 0.6f, top + bookH * 0.92f), Size(bookW * 1.2f, bookH * 0.2f))
        // Лист/корица „завъртян(а)“ около гръбчето: cos(θ) > 0 — вдясно, < 0 — обърнат(а) наляво.
        fun leaf(angle: Float, color: Color, inset: Float, radius: Float) {
            val sx = cos(angle)
            if (abs(sx) < 0.03f) return
            scale(scaleX = sx, scaleY = 1f, pivot = spine) {
                drawRoundRect(color, Offset(spine.x, top + inset), Size(half - inset, bookH - 2 * inset), CornerRadius(radius))
            }
        }
        val coverAngle = (PI * (p / 0.4f).coerceIn(0f, 1f)).toFloat()
        val pageAngles = List(3) { i -> (PI * ((p - 0.3f - 0.14f * i) / 0.42f).coerceIn(0f, 1f)).toFloat() }
        // Задна корица (дясно) и неподвижните страници под нея.
        leaf(0f, Brand.Burgundy, 0f, 10f)
        leaf(0f, Brand.Parchment, 5f, 5f)
        // Предна корица — щом е обърната, тя е под разлистените страници вляво.
        if (cos(coverAngle) < 0f) leaf(coverAngle, Brand.Burgundy, 0f, 10f)
        pageAngles.forEachIndexed { i, a -> if (cos(a) < 0f) leaf(a, if (i % 2 == 0) Brand.Cream else Brand.Parchment, 6f + i, 5f) }
        pageAngles.asReversed().forEachIndexed { i, a -> if (cos(a) >= 0f) leaf(a, if (i % 2 == 0) Brand.Parchment else Brand.Cream, 6f + i, 5f) }
        if (cos(coverAngle) >= 0f) {
            leaf(coverAngle, Brand.Burgundy, 0f, 10f)
            // Златен медальон върху затворената корица.
            scale(scaleX = cos(coverAngle), scaleY = 1f, pivot = spine) { drawCircle(Brand.Gold, bookH * 0.1f, Offset(spine.x + half / 2f, spine.y)) }
        }
        // Гръбче.
        drawRoundRect(Brand.Ink, Offset(spine.x - 3f, top - 2f), Size(6f, bookH + 4f), CornerRadius(3f))
    }
}

/** Две фигури в народен танц — хванати за ръце, леко се полюляват (цикъл ≈3 s). */
@Composable
fun DancingFigures(modifier: Modifier = Modifier) {
    val p by loopProgress(halfPeriodMs = 1500, staticValue = 0.5f)
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val ground = h * 0.9f
        val sway = sin((p * PI).toFloat()) * 2f - 1f // -1..1 (гладко напред-назад)
        val stroke = Stroke(width = h * 0.045f, cap = StrokeCap.Round)
        drawLine(Brand.Gold, Offset(w * 0.1f, ground), Offset(w * 0.9f, ground), strokeWidth = 3f, cap = StrokeCap.Round)
        val hands = Offset(w / 2f, ground - h * 0.52f + abs(sway) * h * 0.03f)
        /** Фигура с крака при [foot]; тялото е наклонено с [tilt] радиана; [dress] = пола вместо панталони. */
        fun figure(foot: Offset, tilt: Float, color: Color, dress: Boolean, outerDir: Float) {
            val bodyH = h * 0.55f
            fun up(d: Float) = Offset(foot.x + sin(tilt) * d, foot.y - cos(tilt) * d)
            val hip = up(bodyH * 0.45f)
            val shoulder = up(bodyH * 0.78f)
            val head = up(bodyH * 0.95f)
            // Крака: единият леко повдигнат според такта.
            val lift = abs(sway) * h * 0.06f
            drawLine(color, hip, Offset(foot.x - w * 0.05f, foot.y - lift), strokeWidth = stroke.width, cap = StrokeCap.Round)
            drawLine(color, hip, Offset(foot.x + w * 0.05f, foot.y), strokeWidth = stroke.width, cap = StrokeCap.Round)
            if (dress) {
                val skirt = Path().apply { moveTo(shoulder.x, shoulder.y - 4f); lineTo(hip.x - w * 0.09f, foot.y - lift / 2f); lineTo(hip.x + w * 0.09f, foot.y); close() }
                drawPath(skirt, color)
            } else {
                drawLine(color, hip, shoulder, strokeWidth = stroke.width * 1.6f, cap = StrokeCap.Round)
                drawLine(Brand.Gold, Offset(shoulder.x - w * 0.02f, shoulder.y + 2f), Offset(hip.x + w * 0.02f, hip.y), strokeWidth = 3f) // пояс
            }
            drawCircle(color, h * 0.07f, head)
            // Вътрешната ръка — към хванатите ръце; външната — вдигната нагоре (ръченица).
            drawLine(color, shoulder, hands, strokeWidth = stroke.width, cap = StrokeCap.Round)
            val raised = Offset(shoulder.x + outerDir * w * 0.12f, shoulder.y - h * 0.16f - sway * outerDir * h * 0.03f)
            drawLine(color, shoulder, raised, strokeWidth = stroke.width, cap = StrokeCap.Round)
        }
        figure(Offset(w * 0.34f, ground), tilt = 0.12f * sway, color = Brand.Ink, dress = false, outerDir = -1f)
        figure(Offset(w * 0.66f, ground), tilt = -0.12f * sway, color = Brand.Burgundy, dress = true, outerDir = 1f)
        drawCircle(Brand.Gold, h * 0.03f, hands)
    }
}
