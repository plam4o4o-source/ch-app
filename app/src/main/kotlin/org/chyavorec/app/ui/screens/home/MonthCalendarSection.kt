package org.chyavorec.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.domain.model.DayStatus
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.MonthCalendar
import org.chyavorec.domain.model.MonthCalendarItem
import java.time.YearMonth
import java.time.format.TextStyle

/** „Календар — октомври 2026“ (името на месеца според езика на приложението). */
@Composable
fun monthTitle(month: YearMonth): String {
    val locale = LocalConfiguration.current.locales[0]
    val name = month.month.getDisplayName(TextStyle.FULL_STANDALONE, locale).replaceFirstChar { it.titlecase(locale) }
    return stringResource(R.string.home_month_title, name, month.year)
}

/**
 * Датите от годишния календар на сайта (chyavorec.org/events) за текущия месец:
 * минали — приглушени, днес — отличен, предстоящи — нормално.
 */
@Composable
fun MonthCalendarCard(month: MonthCalendar, onOpen: (Event) -> Unit, modifier: Modifier = Modifier) {
    OutlinedCard(modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        if (month.items.isEmpty()) {
            Text(
                stringResource(R.string.home_month_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp),
            )
        }
        month.items.forEachIndexed { i, item ->
            if (i > 0) HorizontalDivider(Modifier.padding(start = 72.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            DateRow(item, onOpen, Modifier.animateEntrance(i))
        }
        if (month.upcomingCount == 0) {
            month.next?.let { next ->
                HorizontalDivider()
                val locale = LocalConfiguration.current.locales[0]
                val date = next.date?.let { runCatching { java.time.LocalDate.parse(it) }.getOrNull() }
                val when_ = date?.let { "${it.dayOfMonth} ${it.month.getDisplayName(TextStyle.FULL, locale)}" }.orEmpty()
                Text(
                    stringResource(R.string.home_month_next, next.title, when_),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onOpen(next) }.fillMaxWidth().padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun DateRow(item: MonthCalendarItem, onOpen: (Event) -> Unit, modifier: Modifier = Modifier) {
    val today = item.status == DayStatus.TODAY
    val past = item.status == DayStatus.PAST
    val colors = MaterialTheme.colorScheme
    val statusLabel = when (item.status) {
        DayStatus.TODAY -> stringResource(R.string.home_month_today)
        DayStatus.PAST -> stringResource(R.string.home_month_past)
        DayStatus.UPCOMING -> null
    }
    Row(
        modifier
            .fillMaxWidth()
            .then(if (today) Modifier.background(colors.primaryContainer.copy(alpha = 0.55f)) else Modifier)
            .clickable { onOpen(item.event) }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .alpha(if (past) 0.55f else 1f)
            .semantics(mergeDescendants = true) {
                contentDescription = listOfNotNull(item.day.toString(), item.event.title, item.event.category, statusLabel).joinToString(", ")
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            Modifier.size(42.dp).clip(RoundedCornerShape(10.dp))
                .background(if (today) colors.primary else colors.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                item.day.toString().padStart(2, '0'),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (today) colors.onPrimary else colors.onSecondaryContainer,
            )
        }
        Column(Modifier.weight(1f)) {
            Text(item.event.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            val sub = listOfNotNull(item.event.category, statusLabel).joinToString(" · ")
            if (sub.isNotBlank()) {
                Text(
                    sub,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (today) colors.primary else colors.onSurfaceVariant,
                )
            }
        }
    }
}
