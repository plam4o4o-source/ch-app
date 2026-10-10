package org.chyavorec.app.ui.components

import android.os.PowerManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.core.AppClock
import org.chyavorec.core.OrthodoxEaster
import java.time.LocalDate
import kotlinx.coroutines.delay
import java.time.Month
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/** Сезон за украсите на началния екран. */
enum class Season { NONE, CHRISTMAS, EASTER, HARVEST }

/**
 * Коледа: 15 декември – 7 януари (Ивановден); Великден: православният Великден ±7 дни;
 * жътва: септември (около Кръстовден). Иначе — без украса.
 */
fun currentSeason(today: LocalDate): Season {
    val m = today.month
    val d = today.dayOfMonth
    if ((m == Month.DECEMBER && d >= 15) || (m == Month.JANUARY && d <= 7)) return Season.CHRISTMAS
    val easter = OrthodoxEaster.of(today.year)
    if (!today.isBefore(easter.minusDays(7)) && !today.isAfter(easter.plusDays(7))) return Season.EASTER
    if (m == Month.SEPTEMBER) return Season.HARVEST
    return Season.NONE
}

fun currentSeason(clock: AppClock): Season = currentSeason(clock.today())

/** Текущият сезон, ако украсите са включени в настройките (иначе [Season.NONE]). */
@Composable
fun rememberSeason(): Season {
    val container = LocalAppContainer.current
    val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    val enabled = settings?.seasonal ?: false
    return remember(enabled) { if (enabled) currentSeason(container.clock) else Season.NONE }
}

private class Flake(val x: Float, val speed: Float, val radius: Float, val phase: Float, val sway: Float, val offset: Float)

/** ~30 кадъра в секунда: следващият кадър се иска ~25 ms след предишния (+ изчакване на vsync). */
private const val SNOW_FRAME_DELAY_MS = 25L
/** Снеговалежът е кратък поздрав, не постоянна анимация: ~20 s, после избледнява. */
private const val SNOW_RUN_NANOS = 20_000_000_000L
private const val SNOW_FADE_NANOS = 1_200_000_000L

/** Снеговалежът е изигран в този процес — при връщане към „Начало“ не започва наново. */
private object SnowSession { @Volatile var finished = false }

/**
 * Бавно падащи снежинки (най-много [count]) върху областта на заглавката. Рисуват се
 * с Canvas по време от кадъра — без състояние за всяка снежинка. Пестят батерията:
 * ~30 кадъра/s, спират (с избледняване) след ~20 s или щом [stopWhen] стане true
 * (напр. потребителят е превъртял); при намалено движение или режим за пестене
 * на батерията са неподвижни.
 */
@Composable
fun Snowfall(modifier: Modifier = Modifier, count: Int = 40, color: Color = Color.White, stopWhen: () -> Boolean = { false }) {
    val context = LocalContext.current
    val powerSave = remember { context.getSystemService(PowerManager::class.java)?.isPowerSaveMode == true }
    val still = rememberReducedMotion() || powerSave
    val flakes = remember {
        val r = Random(1922)
        List(count.coerceIn(1, 40)) {
            Flake(
                x = r.nextFloat(),
                speed = 0.025f + r.nextFloat() * 0.04f, // дял от височината в секунда
                radius = 1.5f + r.nextFloat() * 2.5f,
                phase = r.nextFloat() * (2f * PI.toFloat()),
                sway = 4f + r.nextFloat() * 8f,
                offset = r.nextFloat(),
            )
        }
    }
    var frame by remember { mutableLongStateOf(0L) }
    // 1 → 0 при спиране; при 0 нищо не се рисува.
    var fade by remember { mutableFloatStateOf(if (still || !SnowSession.finished) 1f else 0f) }
    val stop by rememberUpdatedState(stopWhen)
    LaunchedEffect(still) {
        if (still || SnowSession.finished) return@LaunchedEffect
        val start = withFrameNanos { it }
        var fadeStart = -1L
        while (true) {
            val t = withFrameNanos { it } - start
            if (fadeStart < 0 && (t >= SNOW_RUN_NANOS || stop())) fadeStart = t
            if (fadeStart >= 0) {
                fade = (1f - (t - fadeStart).toFloat() / SNOW_FADE_NANOS).coerceAtLeast(0f)
                if (fade == 0f) {
                    SnowSession.finished = true
                    break
                }
            }
            frame = t
            delay(SNOW_FRAME_DELAY_MS)
        }
    }
    Canvas(modifier.clearAndSetSemantics { }) {
        val alpha = if (still) 0.3f else 0.3f * fade
        if (alpha <= 0f) return@Canvas
        val t = frame / 1_000_000_000f
        val h = size.height
        val w = size.width
        val dp = density
        flakes.forEach { f ->
            val y = ((f.offset + t * f.speed) % 1f) * (h + 8 * dp) - 4 * dp
            val x = f.x * w + sin(t * 0.9f + f.phase) * f.sway * dp
            drawCircle(color.copy(alpha = alpha), radius = f.radius * dp, center = Offset(x, y))
        }
    }
}

/** Две писани яйца (великденска украса) — малък орнамент за заглавката. */
@Composable
fun EasterEggs(modifier: Modifier = Modifier) {
    Row(modifier.clearAndSetSemantics { }, verticalAlignment = Alignment.Bottom) {
        Canvas(Modifier.size(18.dp, 24.dp)) { drawEgg(Brand.Burgundy, Brand.Gold) }
        Spacer(Modifier.width(2.dp))
        Canvas(Modifier.size(18.dp, 24.dp)) { rotate(14f) { drawEgg(Color(0xFF3F5E4A), Brand.GoldLight) } }
    }
}

private fun DrawScope.drawEgg(base: Color, pattern: Color) {
    val w = size.width
    val h = size.height
    // Яйцето — овал, малко по-тесен отгоре.
    drawOval(Brush.verticalGradient(listOf(base.copy(alpha = 0.85f), base)), topLeft = Offset(w * 0.08f, 0f), size = Size(w * 0.84f, h))
    // Шевица: пояс с ромбчета през средата.
    val band = h * 0.5f
    drawLine(pattern, Offset(w * 0.12f, band), Offset(w * 0.88f, band), strokeWidth = 1.2f * density)
    val step = w * 0.22f
    var x = w * 0.2f
    while (x < w * 0.82f) {
        val r = w * 0.08f
        drawLine(pattern, Offset(x, band - r), Offset(x + r, band), strokeWidth = density)
        drawLine(pattern, Offset(x + r, band), Offset(x, band + r), strokeWidth = density)
        drawLine(pattern, Offset(x, band + r), Offset(x - r, band), strokeWidth = density)
        drawLine(pattern, Offset(x - r, band), Offset(x, band - r), strokeWidth = density)
        x += step
    }
    // отблясък
    drawOval(pattern.copy(alpha = 0.25f), topLeft = Offset(w * 0.2f, h * 0.1f), size = Size(w * 0.25f, h * 0.2f))
    drawOval(pattern.copy(alpha = 0.5f), topLeft = Offset(w * 0.08f, 0f), size = Size(w * 0.84f, h), style = Stroke(density * 0.8f))
}

/** Житни класове по долния ръб на карта (Кръстовден) — векторен орнамент, златен. */
@Composable
fun WheatEdge(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ornament_wheat),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.BottomCenter,
        colorFilter = ColorFilter.tint(Brand.Gold.copy(alpha = 0.55f)),
        modifier = modifier.fillMaxWidth().height(22.dp).clearAndSetSemantics { },
    )
}
