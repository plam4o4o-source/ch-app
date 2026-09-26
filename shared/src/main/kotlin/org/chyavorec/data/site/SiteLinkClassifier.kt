package org.chyavorec.data.site

import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SiteSection

/**
 * Разделите на chyavorec.org се разпознават по адреса (URL-ите на статичния
 * сайт във Vercel са стабилни и четими), а при непознат адрес — по заглавието.
 */
object SiteLinkClassifier {

    private val byPath: Map<String, SiteSection> = mapOf(
        "/about" to SiteSection.ABOUT,
        "/history" to SiteSection.HISTORY,
        "/library" to SiteSection.LIBRARY,
        "/elektronen-katalog" to SiteSection.CATALOG,
        "/events" to SiteSection.EVENTS,
        "/folklorna-grupa" to SiteSection.FOLKLORE,
        "/yavorski-hlapeta" to SiteSection.ENSEMBLE,
        "/tancov-klub" to SiteSection.DANCE,
        "/klubove-i-krzhoci" to SiteSection.CLUBS,
        "/proekti" to SiteSection.PROJECTS,
        "/kontakti" to SiteSection.CONTACTS,
        "/darenija" to SiteSection.DONATIONS,
        "/istoricheski-publikacii" to SiteSection.PUBLICATIONS,
        "/fotodokumentalna-izlozhba" to SiteSection.EXHIBITION,
        "/selo-yavorec" to SiteSection.VILLAGE,
        "/nature" to SiteSection.NATURE,
        "/policy" to SiteSection.PRIVACY,
        "/dostapnost" to SiteSection.ACCESSIBILITY,
        "/load" to SiteSection.DOCUMENTS,
        "/news" to SiteSection.NEWS,
    )

    /** Страниците, които винаги съществуват в сайта, но не са в търсещия индекс. */
    val staticPages: List<Pair<String, String>> = listOf(
        "Политика за поверителност" to "/policy",
        "Достъпност" to "/dostapnost",
        "Документи" to "/load",
        "Календар на събитията" to "/events",
    )

    fun classify(title: String, path: String): SiteSection {
        val p = path.substringBefore('#').substringBefore('?').trimEnd('/').lowercase().ifEmpty { "/" }
        byPath[p]?.let { return it }
        if (p.startsWith("/news/")) return SiteSection.NEWS
        val t = TextNormalizer.normalize(title)
        return when {
            "дигитал" in t -> SiteSection.DIGITAL_CLUB
            "поверителност" in t || "лични данни" in t -> SiteSection.PRIVACY
            "условия" in t -> SiteSection.TERMS
            "контакт" in t -> SiteSection.CONTACTS
            "каталог" in t -> SiteSection.CATALOG
            "библиотек" in t -> SiteSection.LIBRARY
            "събити" in t || "календар" in t -> SiteSection.EVENTS
            "фолклор" in t -> SiteSection.FOLKLORE
            "танц" in t -> SiteSection.DANCE
            "клуб" in t || "кръж" in t -> SiteSection.CLUBS
            "проект" in t -> SiteSection.PROJECTS
            "история" in t -> SiteSection.HISTORY
            "дарени" in t || "дарител" in t -> SiteSection.DONATIONS
            "природ" in t -> SiteSection.NATURE
            else -> SiteSection.OTHER
        }
    }

    fun link(title: String, url: String, baseUrl: String): SiteLink {
        val abs = if (url.startsWith("http")) url else baseUrl.trimEnd('/') + url
        val path = "/" + abs.substringAfter("://").substringAfter('/', "")
        return SiteLink(title.trim(), abs, classify(title, path))
    }
}
