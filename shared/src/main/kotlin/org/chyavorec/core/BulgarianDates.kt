package org.chyavorec.core

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Разпознаване и форматиране на дати на български. */
object BulgarianDates {
    val locale: Locale = Locale.forLanguageTag("bg-BG")

    private val months = listOf(
        "януари", "февруари", "март", "април", "май", "юни",
        "юли", "август", "септември", "октомври", "ноември", "декември",
    )
    private val monthPattern = months.joinToString("|")

    /** „12 октомври 2026“, „12 окт. 2026 г.“, „12 октомври“ */
    private val textual = Regex(
        "(?<!\\d)(\\d{1,2})\\s*(?:-|–|до\\s+\\d{1,2}\\s*)?\\s*($monthPattern|яну|фев|мар|апр|юни|юли|авг|сеп|септ|окт|ное|дек)\\.?\\s*(\\d{4})?",
        RegexOption.IGNORE_CASE,
    )

    /** „12.10.2026“, „12.10.2026 г.“, „12/10/2026“ */
    private val numeric = Regex("(?<!\\d)(\\d{1,2})[./](\\d{1,2})[./](\\d{4})(?!\\d)")

    /** „18:00 ч.“, „18.30 ч“, „от 18,00 часа“ */
    private val time = Regex("(?<![\\d.])([01]?\\d|2[0-3])[:.,]([0-5]\\d)\\s*(?:ч\\b|ч\\.|часа)", RegexOption.IGNORE_CASE)
    private val timeColon = Regex("(?<![\\d.:])([01]?\\d|2[0-3]):([0-5]\\d)(?![\\d])")

    fun monthIndex(token: String): Int? {
        val t = token.lowercase().trimEnd('.')
        if (t.isEmpty()) return null
        val idx = months.indexOfFirst { it == t || (t.length >= 3 && it.startsWith(t)) }
        return if (idx >= 0) idx + 1 else null
    }

    /**
     * Първата дата в текста. Ако годината липсва, се приема [referenceYear]
     * (обикновено годината на публикацията).
     */
    fun findDate(text: String, referenceYear: Int): LocalDate? {
        val candidates = mutableListOf<Pair<Int, LocalDate>>()
        numeric.findAll(text).forEach { m ->
            val (d, mo, y) = m.destructured
            runCatching { LocalDate.of(y.toInt(), mo.toInt(), d.toInt()) }.getOrNull()
                ?.let { candidates += m.range.first to it }
        }
        textual.findAll(text).forEach { m ->
            val day = m.groupValues[1].toInt()
            val month = monthIndex(m.groupValues[2]) ?: return@forEach
            val year = m.groupValues[3].toIntOrNull() ?: referenceYear
            runCatching { LocalDate.of(year, month, day) }.getOrNull()
                ?.let { candidates += m.range.first to it }
        }
        return candidates.minByOrNull { it.first }?.second
    }

    fun findTime(text: String): LocalTime? {
        val m = time.find(text) ?: timeColon.find(text) ?: return null
        return runCatching { LocalTime.of(m.groupValues[1].toInt(), m.groupValues[2].toInt()) }.getOrNull()
    }

    private val longFormat = DateTimeFormatter.ofPattern("d MMMM yyyy", locale)
    private val shortFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale)

    fun formatLong(date: LocalDate): String = "${date.dayOfMonth} ${months[date.monthValue - 1]} ${date.year}"
    fun formatShort(date: LocalDate): String = date.format(shortFormat)
    fun monthName(month: Int): String = months[month - 1]
}
