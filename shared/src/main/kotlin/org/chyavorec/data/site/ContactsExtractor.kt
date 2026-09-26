package org.chyavorec.data.site

import org.chyavorec.domain.model.ContactPerson
import org.chyavorec.domain.model.Contacts
import org.jsoup.nodes.Document

/**
 * Чете страницата „Контакти“ на chyavorec.org: адрес (редовете след „Адрес“),
 * лицата за контакт, имейли/телефони (mailto:/tel: и текст), работното време
 * (ден и часове — на един или на два съседни реда) и Facebook.
 * Телефон се показва само ако е публикуван на сайта.
 */
class ContactsExtractor(private val baseUrl: String, private val fallback: Contacts) {

    private val phoneRegex = Regex("(?<![\\d])(?:\\+359|0)[\\s/-]?\\d{2,3}(?:[\\s/-]?\\d{2,3}){2,3}(?![\\d])")
    private val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    private val timeRange = Regex("\\d{1,2}[:.]\\d{2}\\s*(?:ч\\.?)?\\s*[–—-]\\s*\\d{1,2}[:.]\\d{2}")
    private val days = listOf("понеделник", "вторник", "сряда", "четвъртък", "петък", "събота", "неделя")
    private val roles = listOf("Председател", "Секретар", "Библиотекар", "Касиер", "Ръководител")
    private val labels = setOf("адрес", "имейл", "лица за контакт", "работно време", "телефон", "телефон:", "последвайте ни")

    fun extract(doc: Document, sourceUrl: String): Contacts {
        val container = HtmlContentExtractor(baseUrl).findContainer(doc)
        val lines = container.wholeText().lines().map { it.replace(' ', ' ').trim() }
            .filter { it.isNotEmpty() && it.any(Char::isLetterOrDigit) }
        val text = lines.joinToString("\n")

        val phones = (phoneRegex.findAll(text).map { it.value }.toList() +
            container.select("a[href^=tel:]").map { it.attr("href").removePrefix("tel:") })
            .map { it.trim().replace(Regex("[^\\d+ ]"), " ").replace(Regex("\\s+"), " ").trim() }
            .filter { it.count(Char::isDigit) >= 9 }
            .distinctBy { p -> p.filter(Char::isDigit).takeLast(9) }
        val emails = (container.select("a[href^=mailto:]").map { it.attr("href").removePrefix("mailto:").substringBefore('?') } +
            emailRegex.findAll(text).map { it.value }).map { it.trim().lowercase() }.distinct()

        val address = addressAfterLabel(lines) ?: lines.firstOrNull { l ->
            l.contains("Яворец") && (l.contains("пл.") || l.contains("ул.")) && l.length < 160
        }

        val hours = mutableListOf<String>()
        lines.forEachIndexed { i, l ->
            val low = l.lowercase()
            if (days.none { low.startsWith(it) }) return@forEachIndexed
            when {
                timeRange.containsMatchIn(l) || low.contains("почивен") -> hours += withColon(l)
                i + 1 < lines.size && (timeRange.containsMatchIn(lines[i + 1]) || lines[i + 1].lowercase().contains("почивен")) ->
                    hours += "${l.trimEnd(':')}: ${lines[i + 1]}"
                else -> Unit
            }
        }

        val persons = lines.mapIndexedNotNull { i, l ->
            val role = roles.firstOrNull { l.equals(it, ignoreCase = true) } ?: return@mapIndexedNotNull null
            val name = lines.getOrNull(i + 1)?.takeIf { it.lowercase().trimEnd(':') !in labels && it.split(' ').size in 2..4 }
            name?.let { ContactPerson(role, it) }
        }.distinct()

        val facebook = doc.select("a[href*=facebook.com]").firstOrNull()?.attr("href")
        if (address == null && emails.isEmpty() && phones.isEmpty()) return fallback
        return Contacts(
            organization = fallback.organization,
            address = address ?: fallback.address,
            persons = persons,
            phones = phones,
            emails = emails.ifEmpty { fallback.emails },
            website = fallback.website,
            facebook = facebook ?: fallback.facebook,
            workingHours = hours.distinct(),
            mapQuery = address?.let { "$it, ${fallback.organization}" } ?: fallback.mapQuery,
            fromSite = true,
            sourceUrl = sourceUrl,
        )
    }

    /** „Понеделник08:00 – 16:30“ → „Понеделник: 08:00 – 16:30“. */
    private fun withColon(line: String): String {
        val day = days.first { line.lowercase().startsWith(it) }
        val rest = line.substring(day.length).trimStart(':', ' ', '\u00A0')
        return line.substring(0, day.length) + ": " + rest
    }

    private fun addressAfterLabel(lines: List<String>): String? {
        val i = lines.indexOfFirst { it.equals("Адрес", ignoreCase = true) || it.equals("Адрес:", ignoreCase = true) }
        if (i < 0) return null
        val parts = lines.drop(i + 1).takeWhile { it.lowercase().trimEnd(':') !in labels && it.length < 80 }.take(3)
        return parts.joinToString(", ").ifBlank { null }
    }
}
