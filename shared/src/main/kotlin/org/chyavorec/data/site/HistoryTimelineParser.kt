package org.chyavorec.data.site

import org.chyavorec.core.BulgarianDates
import org.chyavorec.domain.timeline.TimelineEntry
import org.jsoup.Jsoup

/**
 * Разчита хронологията от страницата „История“ на сайта: `.hist-tl-item` с `.hist-tl-year`,
 * `.hist-tl-event` и `.hist-tl-desc`. Сменени класове → празен списък (екранът тогава показва
 * обикновената страница), а не изключение.
 */
object HistoryTimelineParser {
    private val range = Regex("(\\d{4})\\s*[–—-]\\s*(\\d{4})")
    private val dated = Regex("(\\d{1,2})\\s+([\\p{L}]+)\\.?\\s+(\\d{4})")
    private val yearOnly = Regex("(?<!\\d)(\\d{4})(?!\\d)")

    fun parse(html: String, baseUrl: String = ""): List<TimelineEntry> {
        val doc = Jsoup.parse(html, baseUrl)
        return doc.select(".hist-tl-item").mapNotNull { item ->
            val label = item.selectFirst(".hist-tl-year")?.text()?.trim().orEmpty()
            val title = item.selectFirst(".hist-tl-event")?.text()?.trim().orEmpty()
            if (label.isEmpty() || title.isEmpty()) return@mapNotNull null
            val description = item.selectFirst(".hist-tl-desc")?.text()?.trim().orEmpty()
            parseLabel(label, title, description)
        }
    }

    internal fun parseLabel(label: String, title: String, description: String): TimelineEntry {
        range.find(label)?.let { m ->
            val (a, b) = m.destructured
            return TimelineEntry(label, title, description, year = a.toInt(), endYear = b.toInt())
        }
        dated.find(label)?.let { m ->
            val month = BulgarianDates.monthIndex(m.groupValues[2])
            val day = m.groupValues[1].toIntOrNull()?.takeIf { it in 1..31 }
            if (month != null && day != null) {
                return TimelineEntry(label, title, description, year = m.groupValues[3].toInt(), day = day, month = month)
            }
        }
        val year = yearOnly.find(label)?.groupValues?.get(1)?.toInt()
        // „След 1989“ — всяка дума пред годината прави датата приблизителна.
        val approximate = year != null && label.substringBefore(year.toString()).isNotBlank()
        return TimelineEntry(label, title, description, year = year, approximate = approximate)
    }
}
