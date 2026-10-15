package org.chyavorec.app.ui.components

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.core.AppClock
import org.chyavorec.core.OrthodoxEaster
import java.time.Duration
import java.time.LocalDate
import java.time.Month

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

/**
 * Дали украсите (празници, сезони) са включени — подава се от корена от вече прочетените
 * настройки, за да е вярно още в първия кадър (без „скок“, когато украсата се появи).
 * `null` = не е подаден (preview, екран извън корена) → настройките се четат на място.
 */
val LocalSeasonalDecor = staticCompositionLocalOf<Boolean?> { null }

/** Включени ли са украсите в настройките (вж. [LocalSeasonalDecor]). */
@Composable
fun rememberSeasonalEnabled(): Boolean {
    val provided = LocalSeasonalDecor.current
    if (provided != null) return provided
    val container = LocalAppContainer.current
    val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = null)
    return settings?.seasonal ?: false
}

/**
 * Днешната дата по часовника на приложението. Опреснява се при всяко връщане към
 * приложението (ON_RESUME) и в полунощ, ако екранът остане отворен — украсата и
 * поздравът не остават от вчера.
 */
@Composable
fun rememberToday(): LocalDate {
    val clock = LocalAppContainer.current.clock
    var today by remember(clock) { mutableStateOf(clock.today()) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { today = clock.today() }
    LaunchedEffect(clock, today) {
        // До следващата полунощ в часовата зона на приложението (+1 s запас). Сменен
        // часовник/зона междувременно се хваща при следващото ON_RESUME.
        val midnight = today.plusDays(1).atStartOfDay(clock.zone()).toInstant()
        delay(Duration.between(clock.now(), midnight).toMillis().coerceAtLeast(0L) + 1_000L)
        today = clock.today()
    }
    return today
}

/** Текущият сезон, ако украсите са включени в настройките (иначе [Season.NONE]). */
@Composable
fun rememberSeason(today: LocalDate = rememberToday()): Season {
    val enabled = rememberSeasonalEnabled()
    return remember(enabled, today) { if (enabled) currentSeason(today) else Season.NONE }
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
