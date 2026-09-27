package org.chyavorec.domain.model

import java.time.LocalDate
import java.time.YearMonth

/** Кога е датата спрямо днес — за оцветяването в календара на месеца. */
enum class DayStatus { PAST, TODAY, UPCOMING }

data class MonthCalendarItem(val event: Event, val day: Int, val status: DayStatus)

/**
 * Датите от годишния календар на сайта (chyavorec.org/events) за един месец.
 * [next] е първата дата след месеца — показва се, когато всички в месеца са минали.
 */
data class MonthCalendar(
    val month: YearMonth,
    val items: List<MonthCalendarItem>,
    val next: Event?,
) {
    val upcomingCount: Int get() = items.count { it.status != DayStatus.PAST }

    companion object {
        /**
         * Събитията на [today].month (по ден и месец — годишният календар се повтаря
         * всяка година, затова и вече минали дни от месеца остават в списъка).
         */
        fun of(events: List<Event>, today: LocalDate): MonthCalendar {
            val dated = events.mapNotNull { e -> e.date?.let { d -> runCatching { LocalDate.parse(d) }.getOrNull() }?.let { e to it } }
            val items = dated
                .filter { (_, d) -> d.monthValue == today.monthValue }
                .map { (e, d) ->
                    val status = when {
                        d.dayOfMonth < today.dayOfMonth -> DayStatus.PAST
                        d.dayOfMonth == today.dayOfMonth -> DayStatus.TODAY
                        else -> DayStatus.UPCOMING
                    }
                    MonthCalendarItem(e, d.dayOfMonth, status)
                }
                .sortedWith(compareBy({ it.day }, { it.event.title }))
            val next = dated
                .filter { (_, d) -> d.isAfter(today) && d.monthValue != today.monthValue }
                .minByOrNull { (_, d) -> d }?.first
            return MonthCalendar(YearMonth.from(today), items, next)
        }
    }
}
