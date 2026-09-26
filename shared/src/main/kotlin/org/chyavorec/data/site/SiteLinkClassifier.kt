package org.chyavorec.data.site

import org.chyavorec.core.TextNormalizer
import org.chyavorec.core.Urls
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SiteSection
import org.jsoup.nodes.Document

/**
 * Открива страниците на сайта от навигацията на началната страница и ги
 * разпознава по заглавие/адрес. Така нова страница в менюто на сайта (напр.
 * „Дигитален клуб“) се появява в приложението без нова версия.
 */
object SiteLinkClassifier {

    /** Адресите, познати към момента на анализа (fallback, ако откриването не успее). */
    val knownPages: List<Pair<String, SiteSection>> = listOf(
        "Събития" to SiteSection.EVENTS,
        "Библиотека" to SiteSection.LIBRARY,
        "Фолклорна група" to SiteSection.FOLKLORE,
        "Народни танци" to SiteSection.DANCE,
        "Клубове" to SiteSection.CLUBS,
        "Проекти" to SiteSection.PROJECTS,
        "История" to SiteSection.HISTORY,
        "Контакти" to SiteSection.CONTACTS,
        "Електронен каталог" to SiteSection.CATALOG,
    )
    val knownPaths: Map<SiteSection, String> = mapOf(
        SiteSection.EVENTS to "/index/events/0-58",
        SiteSection.LIBRARY to "/index/library/0-59",
        SiteSection.FOLKLORE to "/index/folklorna_grupa/0-60",
        SiteSection.DANCE to "/index/tancov_klub/0-61",
        SiteSection.CLUBS to "/index/klubove_i_krzhoci/0-62",
        SiteSection.PROJECTS to "/index/proekti/0-63",
        SiteSection.HISTORY to "/index/history/0-64",
        SiteSection.CONTACTS to "/index/kontakti/0-65",
        SiteSection.CATALOG to "/index/elektronen_katalog/0-70",
    )

    fun fallbackLinks(baseUrl: String): List<SiteLink> = knownPages.mapNotNull { (title, section) ->
        knownPaths[section]?.let { SiteLink(title, baseUrl.trimEnd('/') + it, section) }
    }

    private val contentPath = Regex("^/(index/[^/?#]+/\\d+-\\d+|news/?|photo/?|load/?|publ/?|blog/?)", RegexOption.IGNORE_CASE)

    fun discover(doc: Document, baseUrl: String): List<SiteLink> {
        val out = LinkedHashMap<String, SiteLink>()
        for (a in doc.select("a[href]")) {
            val url = Urls.absolutize(a.attr("href"), baseUrl) ?: continue
            if (!Urls.sameSite(url, baseUrl)) continue
            val path = url.substringAfter("://").substringAfter('/', "").let { "/$it" }.substringBefore('#')
            if (!contentPath.containsMatchIn(path)) continue
            // Системни страници на uCoz (вход, регистрация, търсене) не са съдържание.
            if (path.startsWith("/index/0-") || path.contains("/index/1") && path.length < 10) continue
            val title = a.text().trim().trimEnd('→', '›', '»').trim()
            if (title.length < 3 || title.length > 60) continue
            val key = url.substringBefore('#').trimEnd('/')
            if (key in out) continue
            out[key] = SiteLink(title, url.substringBefore('#'), classify(title, path))
        }
        return out.values.toList()
    }

    fun classify(title: String, path: String): SiteSection {
        val t = TextNormalizer.normalize(title)
        val p = path.lowercase()
        return when {
            "поверителност" in t || "лични данни" in t || "gdpr" in t || "privacy" in p -> SiteSection.PRIVACY
            "условия" in t -> SiteSection.TERMS
            "дигитал" in t -> SiteSection.DIGITAL_CLUB
            "контакт" in t || "свържете" in t || "kontakt" in p -> SiteSection.CONTACTS
            "каталог" in t || "katalog" in p -> SiteSection.CATALOG
            "библиотек" in t || "library" in p -> SiteSection.LIBRARY
            "събити" in t || "календар" in t || "афиш" in t || "events" in p -> SiteSection.EVENTS
            "фолклор" in t || "folklor" in p -> SiteSection.FOLKLORE
            "танц" in t || "tanc" in p -> SiteSection.DANCE
            "клуб" in t || "кръж" in t || "klub" in p -> SiteSection.CLUBS
            "ансамбъл" in t -> SiteSection.FOLKLORE
            "проект" in t || "proekt" in p -> SiteSection.PROJECTS
            "история" in t || "history" in p -> SiteSection.HISTORY
            "за нас" in t || "за читалището" in t || "about" in p -> SiteSection.ABOUT
            "дарени" in t || "дари" in t || "дарител" in t -> SiteSection.DONATIONS
            "публикаци" in t -> SiteSection.PUBLICATIONS
            "природ" in t || "яворец" in t || "село" in t -> SiteSection.VILLAGE
            "галерия" in t || "снимки" in t || "фото" in t || p.startsWith("/photo") -> SiteSection.GALLERY
            "новини" in t || p.startsWith("/news") -> SiteSection.NEWS
            "документ" in t || "файл" in t || p.startsWith("/load") -> SiteSection.DOCUMENTS
            else -> SiteSection.OTHER
        }
    }
}
