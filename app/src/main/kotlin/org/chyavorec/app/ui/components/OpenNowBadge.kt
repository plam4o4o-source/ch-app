package org.chyavorec.app.ui.components

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import kotlinx.coroutines.delay
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.domain.model.OpenStatus
import org.chyavorec.domain.model.OpeningHours
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * „Отворено до 16:30“ / „Затваря скоро — до 16:30“ / „Затворено · отваря утре в 08:00“ —
 * тих ред с цветна точка. Без работно време ([hours] `null` или [OpenStatus.Unknown]) — нищо.
 *
 * Не тиктака всеки кадър: надписът се преизчислява в следващия момент на промяна
 * ([OpeningHours.nextChange]) и само докато екранът е видим (при връщане — веднага).
 * TalkBack го чете като обикновен текст (точката е украса).
 */
@Composable
fun OpenNowBadge(hours: OpeningHours?, modifier: Modifier = Modifier) {
    if (hours == null) return
    val clock = LocalAppContainer.current.clock
    var now by remember(clock) { mutableStateOf(clock.now()) }
    val lifecycle by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val started = lifecycle.isAtLeast(Lifecycle.State.STARTED)
    LaunchedEffect(hours, clock, started) {
        if (!started) return@LaunchedEffect
        while (true) {
            val t = clock.now()
            now = t
            val next = hours.nextChange(t.atZone(OpeningHours.ZONE))
            // +1 s запас: да не се събуди милисекунда преди границата.
            delay(Duration.between(t, next).toMillis().coerceAtLeast(0L) + 1_000L)
        }
    }
    val zoned = now.atZone(OpeningHours.ZONE)
    val status = remember(hours, now) { hours.status(zoned) }
    if (status == OpenStatus.Unknown) return
    // LocalResources — при смяна на езика надписът се преизчислява.
    val res = LocalResources.current
    val today = zoned.toLocalDate()
    val text = remember(status, today, res) { openNowText(res, status, today) } ?: return
    val ext = LocalExtendedColors.current
    val dot = when (status) {
        is OpenStatus.Open -> ext.ok
        is OpenStatus.ClosingSoon -> ext.warn
        else -> MaterialTheme.colorScheme.outline
    }
    Row(modifier.semantics(mergeDescendants = true) {}, verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.size(6.dp).background(dot, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Работното време от запазените контакти (само кеша — без мрежа при старт) или вграденото;
 * `null`, докато се чете.
 */
@Composable
fun rememberOpeningHours(): OpeningHours? {
    val container = LocalAppContainer.current
    val hours by produceState<OpeningHours?>(null, container) { value = container.openingHours() }
    return hours
}

private val HOUR_MINUTE: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

/** Надписът за [status] спрямо [today] (българска дата); `null` при [OpenStatus.Unknown]. */
internal fun openNowText(res: Resources, status: OpenStatus, today: LocalDate): String? {
    fun hm(t: LocalTime) = t.format(HOUR_MINUTE)
    return when (status) {
        is OpenStatus.Open -> res.getString(R.string.open_now_until, hm(status.until))
        is OpenStatus.ClosingSoon -> res.getString(R.string.open_now_closing_soon, hm(status.until))
        is OpenStatus.Closed -> {
            val day = status.nextOpen.toLocalDate()
            val time = hm(status.nextOpen.toLocalTime())
            when (day) {
                today -> res.getString(R.string.open_now_opens_today, time)
                today.plusDays(1) -> res.getString(R.string.open_now_opens_tomorrow, time)
                else -> {
                    val on = res.getString(R.string.open_days_on).split(',').getOrElse(day.dayOfWeek.value - 1) { "" }
                    res.getString(R.string.open_now_opens_on, on, time)
                }
            }
        }
        OpenStatus.Unknown -> null
    }
}
