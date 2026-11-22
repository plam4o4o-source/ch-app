package org.chyavorec.domain.model

import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/** Един период, в който библиотеката е отворена: [from] включително, [until] — не. */
data class OpenInterval(val from: LocalTime, val until: LocalTime)

/** Дали библиотеката е отворена в даден момент ([OpeningHours.status]). */
sealed interface OpenStatus {
    /** Отворено до [until] (повече от [OpeningHours.CLOSING_SOON] до затваряне). */
    data class Open(val until: LocalTime) : OpenStatus

    /** Затваря до [OpeningHours.CLOSING_SOON] — в [until]. */
    data class ClosingSoon(val until: LocalTime) : OpenStatus

    /** Затворено; отваря в [nextOpen] (българско време). */
    data class Closed(val nextOpen: LocalDateTime) : OpenStatus

    /** Не се знае (няма нито един работен ден в седмицата). */
    data object Unknown : OpenStatus
}

/**
 * Работното време по дни на седмицата — разчетено от свободния текст на страницата
 * „Контакти“ ([parse]), напр. „Понеделник – четвъртък: 08:00 – 16:30“. Дните без запис
 * са почивни. Всички моменти са по българско време ([ZONE]).
 */
data class OpeningHours(val week: Map<DayOfWeek, List<OpenInterval>>) {

    fun intervalsOn(day: DayOfWeek): List<OpenInterval> = week[day].orEmpty()

    fun isOpenOn(date: LocalDate): Boolean = intervalsOn(date.dayOfWeek).isNotEmpty()

    /** Състоянието към [now] (превежда се в [ZONE]). */
    fun status(now: ZonedDateTime): OpenStatus {
        val local = now.withZoneSameInstant(ZONE).toLocalDateTime()
        val t = local.toLocalTime()
        intervalsOn(local.dayOfWeek).firstOrNull { t >= it.from && t < it.until }?.let { open ->
            val left = Duration.between(t, open.until)
            return if (left <= CLOSING_SOON) OpenStatus.ClosingSoon(open.until) else OpenStatus.Open(open.until)
        }
        return nextOpening(local)?.let { OpenStatus.Closed(it) } ?: OpenStatus.Unknown
    }

    /** Първото отваряне след [after] (в рамките на седмица) или `null`. */
    fun nextOpening(after: LocalDateTime): LocalDateTime? {
        for (d in 0L..7L) {
            val date = after.toLocalDate().plusDays(d)
            intervalsOn(date.dayOfWeek).map { date.atTime(it.from) }.firstOrNull { it.isAfter(after) }?.let { return it }
        }
        return null
    }

    /** Последното затваряне в работен ден преди [date] (до седмица назад) или `null`. */
    fun lastClosingBefore(date: LocalDate): LocalDateTime? {
        for (d in 1L..7L) {
            val day = date.minusDays(d)
            intervalsOn(day.dayOfWeek).lastOrNull()?.let { return day.atTime(it.until) }
        }
        return null
    }

    /**
     * Следващият момент след [now], в който [status] може да се смени: отваряне, затваряне,
     * „затваря скоро“ или полунощ („утре“ става „днес“). До него надписът не се преизчислява.
     */
    fun nextChange(now: ZonedDateTime): Instant {
        val local = now.withZoneSameInstant(ZONE).toLocalDateTime()
        val today = local.toLocalDate()
        // Полунощ идва преди всички моменти от утрешния ден — стигат днешните.
        var next = today.plusDays(1).atStartOfDay()
        for (i in intervalsOn(today.dayOfWeek)) {
            for (t in listOf(i.from, i.until.minus(CLOSING_SOON), i.until)) {
                val at = today.atTime(t)
                if (at.isAfter(local) && at.isBefore(next)) next = at
            }
        }
        return next.atZone(ZONE).toInstant()
    }

    companion object {
        /** Работното време на библиотеката е по българско време. */
        val ZONE: ZoneId = ZoneId.of("Europe/Sofia")

        /** Колко преди затваряне е „затваря скоро“. */
        val CLOSING_SOON: Duration = Duration.ofMinutes(30)

        /** Часовете от сайта; ако не се разчетат — вградените ([fallback]). */
        fun resolve(site: List<String>, fallback: List<String>): OpeningHours? = parse(site) ?: parse(fallback)

        /**
         * Разчита редове като „Понеделник – четвъртък: 08:00 – 16:30“, „Пон.–Пет.: 8:00-12:00 и
         * 13:00-17:00“, „Събота и неделя: почивни дни“, „Делнични дни: 9:00 – 17:00“ (и английските
         * им съответствия — вградения текст на английски). Непознатите редове се пропускат.
         * Ако нито един ден не получи часове — `null`.
         */
        fun parse(lines: List<String>): OpeningHours? {
            // За всеки ден — часовете от най-конкретния ред (с най-малко дни): „Петък: 8–16“ надделява
            // над „Делнични дни: 8–17“; редове с еднакъв обхват се сливат (8–12 и 13–17 на два реда).
            val best = mutableMapOf<DayOfWeek, Pair<Int, List<OpenInterval>>>()
            for (raw in lines) {
                val line = normalize(raw)
                val firstDigit = line.indexOfFirst { it.isDigit() }
                val days = days(if (firstDigit < 0) line else line.substring(0, firstDigit))
                if (days.isEmpty()) continue
                val intervals = if (firstDigit < 0) emptyList() else intervals(line.substring(firstDigit))
                val closed = closedWords.any { line.contains(it) }
                if (intervals.isEmpty() && !closed) continue
                for (d in days) {
                    val prev = best[d]
                    best[d] = when {
                        prev == null || days.size < prev.first -> days.size to intervals
                        days.size == prev.first -> days.size to prev.second + intervals
                        else -> prev
                    }
                }
            }
            val week = best.mapValues { merge(it.value.second) }.filterValues { it.isNotEmpty() }
            return if (week.isEmpty()) null else OpeningHours(week)
        }

        // „почивка“ (обедна) не е почивен ден.
        private val closedWords = listOf("почивн", "почивен", "затворен", "не работи", "closed", "day off", "days off")

        private fun normalize(s: String): String = s.lowercase()
            .replace('\u00A0', ' ')
            .replace(Regex("[\u2010-\u2015\u2212]"), "-")

        private val timeRange = Regex("(\\d{1,2})[:.](\\d{2})\\s*(?:ч\\.?|h)?\\s*(?:-|до|to)\\s*(\\d{1,2})[:.](\\d{2})")

        private fun intervals(s: String): List<OpenInterval> = timeRange.findAll(s).mapNotNull { m ->
            val (h1, m1, h2, m2) = m.destructured
            val from = time(h1.toInt(), m1.toInt()) ?: return@mapNotNull null
            val until = time(h2.toInt(), m2.toInt()) ?: return@mapNotNull null
            OpenInterval(from, until).takeIf { from < until }
        }.toList()

        private fun time(h: Int, m: Int): LocalTime? = when {
            m !in 0..59 -> null
            h in 0..23 -> LocalTime.of(h, m)
            // „до 24:00“ — до края на деня.
            h == 24 && m == 0 -> LocalTime.MAX
            else -> null
        }

        /** Сливане на застъпващите се и долепените периоди, по ред. */
        private fun merge(list: List<OpenInterval>): List<OpenInterval> {
            val out = mutableListOf<OpenInterval>()
            for (i in list.sortedBy { it.from }) {
                val last = out.lastOrNull()
                if (last != null && i.from <= last.until) out[out.lastIndex] = OpenInterval(last.from, maxOf(last.until, i.until))
                else out += i
            }
            return out
        }

        private val workdays = DayOfWeek.entries.take(5)
        private val token = Regex("\\p{L}+|-")
        private val rangeWords = setOf("-", "до", "to", "through", "till", "until")

        /** Дните в частта на реда преди часовете: диапазони, изброявания и „делнични дни“. */
        private fun days(part: String): Set<DayOfWeek> {
            val out = linkedSetOf<DayOfWeek>()
            if (part.contains("всеки ден") || part.contains("every day")) out += DayOfWeek.entries
            var last: DayOfWeek? = null
            var range = false
            for (t in token.findAll(part).map { it.value }) {
                if (t in rangeWords) {
                    range = last != null
                    continue
                }
                if (t.startsWith("делнич") || t.startsWith("weekday") || t == "ежедневно" || t == "daily") {
                    out += if (t == "ежедневно" || t == "daily") DayOfWeek.entries else workdays
                    last = null
                    range = false
                    continue
                }
                val d = dayOf(t) ?: continue
                val from = last
                if (range && from != null) {
                    var x: DayOfWeek = from
                    while (x != d) { out += x; x = x.plus(1) }
                }
                out += d
                last = d
                range = false
            }
            return out
        }

        private fun dayOf(w: String): DayOfWeek? = when {
            w == "пн" || w == "пон" || w.startsWith("понед") || w == "mon" || w.startsWith("monday") -> DayOfWeek.MONDAY
            w == "вт" || w.startsWith("вторн") || w == "tue" || w == "tues" || w.startsWith("tuesday") -> DayOfWeek.TUESDAY
            w == "ср" || w.startsWith("сряд") || w == "wed" || w.startsWith("wednesday") -> DayOfWeek.WEDNESDAY
            w == "чт" || w == "чет" || w.startsWith("четвър") || w == "thu" || w == "thur" || w == "thurs" || w.startsWith("thursday") -> DayOfWeek.THURSDAY
            w == "пт" || w == "пет" || w.startsWith("петък") || w == "fri" || w.startsWith("friday") -> DayOfWeek.FRIDAY
            w == "сб" || w == "съб" || w.startsWith("събот") || w == "sat" || w.startsWith("saturday") -> DayOfWeek.SATURDAY
            w == "нд" || w == "нед" || w.startsWith("недел") || w == "sun" || w.startsWith("sunday") -> DayOfWeek.SUNDAY
            else -> null
        }
    }
}
