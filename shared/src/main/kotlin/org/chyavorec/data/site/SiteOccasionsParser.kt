package org.chyavorec.data.site

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.chyavorec.domain.occasion.CustomGreeting
import org.chyavorec.domain.occasion.HolidayCalendar
import org.chyavorec.domain.occasion.Occasion
import org.chyavorec.domain.occasion.OccasionEffect
import org.chyavorec.domain.occasion.OccasionLink
import org.chyavorec.domain.occasion.OccasionMood
import org.chyavorec.domain.occasion.OccasionOrnament
import org.chyavorec.domain.occasion.SiteOccasion
import java.time.LocalDate
import java.time.MonthDay

/**
 * Местен празник от `/data/app-occasions.json` (празникът на селото, годишнина на
 * читалището…). Пази се в кеша в този суров вид; [toSiteOccasion] го превръща в повод.
 */
@Serializable
data class SiteOccasionEntry(
    val id: String,
    /** `ММ-ДД` (всяка година) или `ГГГГ-ММ-ДД` (само тази година). */
    val date: String,
    /** Последният ден (включително), в същия формат; без него — само [date]. */
    val until: String? = null,
    val title: String,
    val text: String = "",
    val titleEn: String? = null,
    val textEn: String? = null,
    val ornament: String? = null,
    val effect: String? = null,
    val quiet: Boolean = false,
    /** Търсене в каталога при натискане на поздрава. */
    val search: String? = null,
)

/**
 * Разчита `/data/app-occasions.json` — `{"occasions": [ … ]}` или направо масив.
 * Невалидните записи се пропускат, не провалят целия файл.
 */
object SiteOccasionsParser {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun parse(text: String): List<SiteOccasionEntry>? {
        val root = runCatching { json.parseToJsonElement(text) }.getOrNull() ?: return null
        val array = when (root) {
            is JsonArray -> root
            is JsonObject -> root["occasions"] as? JsonArray ?: JsonArray(emptyList())
            else -> return null
        }
        return array.mapNotNull { (it as? JsonObject)?.let(::item) }.distinctBy { it.id }
    }

    private fun item(o: JsonObject): SiteOccasionEntry? {
        fun str(key: String) = runCatching { o[key]?.jsonPrimitive?.content }.getOrNull()?.trim()?.takeIf { it.isNotEmpty() }
        val id = str("id")?.take(60) ?: return null
        val date = str("date") ?: return null
        val until = str("until")
        val entry = SiteOccasionEntry(
            id = id,
            date = date,
            until = until,
            title = str("title")?.take(120) ?: return null,
            text = str("text")?.take(300).orEmpty(),
            titleEn = (str("title_en") ?: str("titleEn"))?.take(120),
            textEn = (str("text_en") ?: str("textEn"))?.take(300),
            ornament = str("ornament"),
            effect = str("effect"),
            quiet = runCatching { o["quiet"]?.jsonPrimitive?.booleanOrNull }.getOrNull() ?: false,
            search = str("search")?.take(80),
        )
        return entry.takeIf { toSiteOccasion(it) != null }
    }

    /** Поводът за календара или null, ако датите са невалидни. */
    fun toSiteOccasion(e: SiteOccasionEntry): SiteOccasion? {
        val (from, fromYear) = parseDate(e.date) ?: return null
        val (to, toYear) = e.until?.let { parseDate(it) ?: return null } ?: (from to fromYear)
        // Еднократен период е в една година или минава през Нова година.
        if (fromYear != null && toYear != null && toYear != fromYear && !(toYear == fromYear + 1 && to.isBefore(from))) return null
        // „2026-12-30“ до „2026-01-02“ — краят е преди началото в същата година.
        if (fromYear != null && toYear == fromYear && to.isBefore(from)) return null
        if ((fromYear == null) != (toYear == null)) return null
        val quiet = e.quiet
        val occasion = Occasion(
            id = "site_" + e.id,
            priority = if (quiet) HolidayCalendar.MEMORIAL - 1 else HolidayCalendar.SITE,
            mood = if (quiet) OccasionMood.QUIET else OccasionMood.FESTIVE,
            effect = if (quiet) OccasionEffect.NONE else enumOrNone(e.effect, OccasionEffect.NONE),
            ornament = enumOrNone(e.ornament, OccasionOrnament.NONE),
            link = e.search?.let { OccasionLink(text = it) },
            custom = CustomGreeting(e.title, e.text, e.titleEn, e.textEn),
        )
        return SiteOccasion(from, to, fromYear, occasion)
    }

    private fun parseDate(s: String): Pair<MonthDay, Int?>? = runCatching {
        if (s.length == 10) LocalDate.parse(s).let { MonthDay.from(it) to it.year }
        else MonthDay.parse("--$s") to null
    }.getOrNull()

    private inline fun <reified T : Enum<T>> enumOrNone(name: String?, none: T): T =
        name?.let { n -> enumValues<T>().firstOrNull { it.name.equals(n.replace('-', '_'), ignoreCase = true) } } ?: none
}
