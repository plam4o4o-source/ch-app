package org.chyavorec.domain.timeline

import kotlinx.serialization.Serializable
import java.time.LocalDate

/**
 * Една „веха“ от историята на читалището (страницата „История“ на сайта, `.hist-tl-item`).
 * [label] е годината така, както я пише сайтът („1900“, „17 дек. 1922“, „1912–1918“, „След 1989“, „Днес“).
 */
@Serializable
data class TimelineEntry(
    val label: String,
    val title: String,
    val description: String = "",
    /** Годината (началната — при период); `null` — „Днес“ и всичко без година. */
    val year: Int? = null,
    /** Крайната година на период („1912–1918“). */
    val endYear: Int? = null,
    /** Ден и месец, ако сайтът ги пише („17 дек. 1922“). */
    val day: Int? = null,
    val month: Int? = null,
    /** „След 1989“ — приблизителна година. */
    val approximate: Boolean = false,
) {
    /** Точна година на събитието — единствена, без период и без „След“: годна за въпрос във викторината. */
    val exactYear: Int? get() = year.takeIf { endYear == null && !approximate }
}

/**
 * „На този ден“: вехата, чийто ден и месец съвпадат с днешните (17 декември — основаването, 17 март — първата копка).
 * Чисти функции — Home само пита [entryFor] и показва картата, ако има отговор.
 */
object OnThisDay {
    /**
     * Дните от годината, за които сайтът пази дата ([TimelineEntry.day]/[TimelineEntry.month]) към
     * момента на писането (17.12. и 17.03.). Само пази Home от четене на кеша в другите дни —
     * [entryFor] решава по данните; тестът със снимката на страницата следи двете да съвпадат.
     */
    private val KNOWN_DAYS = setOf(12 to 17, 3 to 17)

    /** Евтина проверка (без данни): има ли смисъл днес да се търси веха. */
    fun mayHaveEntry(date: LocalDate): Boolean = (date.monthValue to date.dayOfMonth) in KNOWN_DAYS

    /** Вехата за [date] от [entries] или `null`. Без дата в данните — няма карта. */
    fun entryFor(date: LocalDate, entries: List<TimelineEntry>): TimelineEntry? =
        entries.firstOrNull { it.day == date.dayOfMonth && it.month == date.monthValue && it.year != null }

    /** Преди колко години е станало ([entry] с година) спрямо [date]; `null` — без година или в бъдещето. */
    fun yearsAgo(entry: TimelineEntry, date: LocalDate): Int? =
        entry.year?.let { date.year - it }?.takeIf { it >= 0 }
}
