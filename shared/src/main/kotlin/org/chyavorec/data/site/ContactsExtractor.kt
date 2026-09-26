package org.chyavorec.data.site

import org.chyavorec.domain.model.Contacts
import org.jsoup.nodes.Document

/**
 * Чете контактите от страницата „Контакти“ на сайта: tel:/mailto: връзки,
 * телефони и имейли в текста, адрес (редът със „с. Яворец“) и работно време
 * (редове с интервал от часове).
 */
class ContactsExtractor(private val baseUrl: String, private val fallback: Contacts) {

    private val phoneRegex = Regex("(?<![\\d])(?:\\+359|0)[\\s/-]?\\d{2,3}(?:[\\s/-]?\\d{2,3}){2,3}(?![\\d])")
    private val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    private val hoursRegex = Regex("\\d{1,2}[:.]\\d{2}\\s*(?:ч\\.?)?\\s*[–—-]\\s*\\d{1,2}[:.]\\d{2}")
    private val dayWords = listOf("пон", "вт", "ср", "чет", "пет", "съб", "нед", "всеки ден", "делнич", "работно време")

    fun extract(doc: Document, sourceUrl: String): Contacts {
        val container = HtmlContentExtractor(baseUrl).findContainer(doc)
        val lines = container.wholeText().lines().map { it.replace(' ', ' ').trim() }.filter { it.isNotEmpty() }
        val text = lines.joinToString("\n")

        // Номерът от текста е в „човешки“ формат — предпочитаме него пред tel: връзката;
        // един и същ номер (+359… и 0…) се показва веднъж (по последните 9 цифри).
        val phones = (phoneRegex.findAll(text).map { it.value }.toList() +
            container.select("a[href^=tel:]").map { it.attr("href").removePrefix("tel:") })
            .map { normalizePhone(it) }.filter { it.count(Char::isDigit) >= 9 }
            .distinctBy { p -> p.filter(Char::isDigit).takeLast(9) }
        val emails = (container.select("a[href^=mailto:]").map { it.attr("href").removePrefix("mailto:").substringBefore('?') } +
            emailRegex.findAll(text).map { it.value })
            .map { it.trim().lowercase() }.distinct()
        val address = lines.firstOrNull { l ->
            (l.contains("Яворец") || l.contains("ул.") || l.contains("пл.")) && l.length in 8..160 &&
                !l.contains('@') && phoneRegex.find(l) == null
        }?.removePrefix("Адрес:")?.trim()
        val hours = lines.filter { l ->
            hoursRegex.containsMatchIn(l) && dayWords.any { l.lowercase().contains(it) }
        }.map { it.removePrefix("Работно време:").trim() }.distinct().take(8)
        val facebook = doc.select("a[href*=facebook.com]").firstOrNull()?.attr("href")

        val foundSomething = phones.isNotEmpty() || emails.isNotEmpty() || address != null
        if (!foundSomething) return fallback
        return Contacts(
            organization = fallback.organization,
            address = address ?: fallback.address,
            phones = phones.ifEmpty { fallback.phones },
            emails = emails.ifEmpty { fallback.emails },
            website = fallback.website,
            facebook = facebook ?: fallback.facebook,
            workingHours = hours,
            mapQuery = fallback.mapQuery,
            fromSite = true,
            sourceUrl = sourceUrl,
        )
    }

    private fun normalizePhone(raw: String): String = raw.trim().replace(Regex("[^\\d+ ]"), " ").replace(Regex("\\s+"), " ").trim()
}
