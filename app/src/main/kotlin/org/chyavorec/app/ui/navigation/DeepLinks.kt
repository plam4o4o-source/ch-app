package org.chyavorec.app.ui.navigation

import android.net.Uri
import org.chyavorec.core.ScanCodes
import org.chyavorec.core.ScanTarget
import org.chyavorec.core.TrustedLinks

/** Къде води една връзка — от известие, пряк път на иконата или адрес от сайта. */
sealed interface LinkTarget {
    /**
     * [tab] — разделът от долната лента под екрана (за да води „Назад“ към него, а не
     * извън приложението); [stack] — екраните над раздела (празно — самият раздел);
     * [segment] — сегментът вътре в раздела (напр. [Routes.SEGMENT_CALENDAR] в „Новини“).
     */
    data class Screen(val tab: String, val stack: List<String> = emptyList(), val segment: String? = null) : LinkTarget

    /** Диалогът за нова версия (без навигация). */
    data object UpdatePrompt : LinkTarget
}

/**
 * Вътрешните връзки на приложението. Известията носят в extra „deep_link“ (а преките
 * пътища — в `chyavorec://app/<връзка>`) низ от вида:
 * - `article/<id>` — новина по идентификатора ѝ от news.json (или по публичния ѝ адрес);
 * - `event/<id>` — събитие по идентификатора му от index.json;
 * - `message/<id>` — съобщенията, превъртени до това съобщение;
 * - `events`, `messages`, `catalog`, `scan`, `login`, `my/loans`, `my/card`, `my/history`, `update`;
 * - `https://chyavorec.org/...` — адрес от сайта (App Links), виж [forUrl].
 * Идентификаторите се кодират ([Uri.encode]), както в [Routes].
 */
object DeepLinks {
    const val EVENTS = "events"
    const val MESSAGES = "messages"
    const val UPDATE = "update"

    private const val ARTICLE_PREFIX = "article/"
    private const val EVENT_PREFIX = "event/"
    private const val MESSAGE_PREFIX = "message/"

    /** Страницата на електронния каталог на сайта (App Link към раздела „Каталог“). */
    private const val CATALOG_PATH = "/elektronen-katalog"

    fun article(id: String) = ARTICLE_PREFIX + Uri.encode(id)
    fun event(id: String) = EVENT_PREFIX + Uri.encode(id)
    fun message(id: String) = MESSAGE_PREFIX + Uri.encode(id)

    /**
     * Публичният адрес на книга — същият, който споделя каталогът на сайта
     * (`/elektronen-katalog#zapis=<инв. №>`). В браузър отваря книгата в сайта, а на
     * телефон с приложението — екрана на книгата (App Link, виж [forUrl]).
     */
    fun bookUrl(siteBaseUrl: String, inv: Long) = siteBaseUrl.trimEnd('/') + CATALOG_PATH + "#zapis=" + inv

    /** Целта на връзката [link] или `null` — непозната връзка (пренебрегва се, без срив). */
    fun target(link: String, siteBaseUrl: String): LinkTarget? {
        if (link.startsWith("https://", ignoreCase = true) || link.startsWith("http://", ignoreCase = true)) {
            return forUrl(link, siteBaseUrl)
        }
        return when {
            link == "login" -> LinkTarget.Screen(Routes.MY, listOf(Routes.LOGIN))
            link == "my/loans" -> LinkTarget.Screen(Routes.MY, listOf(Routes.LOANS))
            link == "my/card" -> LinkTarget.Screen(Routes.MY, listOf(Routes.CARD))
            link == "my/history" -> LinkTarget.Screen(Routes.MY, listOf(Routes.HISTORY))
            link == "catalog" -> LinkTarget.Screen(Routes.CATALOG)
            link == "scan" -> LinkTarget.Screen(Routes.CATALOG, listOf(Routes.SCAN))
            link == EVENTS -> eventsTab()
            link == MESSAGES -> LinkTarget.Screen(Routes.HOME, listOf(Routes.MESSAGES))
            link == UPDATE -> LinkTarget.UpdatePrompt
            link.startsWith(ARTICLE_PREFIX) -> idOf(link, ARTICLE_PREFIX)
                ?.let { LinkTarget.Screen(Routes.NEWS, listOf(Routes.article(it))) }
                ?: LinkTarget.Screen(Routes.NEWS)
            // Под събитието — „Новини и събития“ на сегмента „Календар“.
            link.startsWith(EVENT_PREFIX) -> idOf(link, EVENT_PREFIX)
                ?.let { eventsTab(listOf(Routes.event(it))) }
                ?: eventsTab()
            link.startsWith(MESSAGE_PREFIX) -> idOf(link, MESSAGE_PREFIX)
                ?.let { LinkTarget.Screen(Routes.HOME, listOf(Routes.message(it))) }
                ?: LinkTarget.Screen(Routes.HOME, listOf(Routes.MESSAGES))
            // Известия от по-стари версии („news/<хеш>“) — разделът „Новини“.
            link == "news" || link.startsWith("news/") -> LinkTarget.Screen(Routes.NEWS)
            else -> null
        }
    }

    /** Събитията — разделът „Новини и събития“ със сегмента „Календар“ (и [stack] над него). */
    fun eventsTab(stack: List<String> = emptyList()) = LinkTarget.Screen(Routes.NEWS, stack, Routes.SEGMENT_CALENDAR)

    private fun idOf(link: String, prefix: String): String? =
        Uri.decode(link.removePrefix(prefix)).takeIf { it.isNotBlank() }

    /**
     * Адрес от сайта → native екран; `null` — файл, външен или непознат адрес (отваря се в
     * браузъра). Пътищата, заявени като App Links в AndroidManifest.xml, трябва всички да
     * водят до екран тук (проверява го DeepLinksTest).
     */
    fun forUrl(url: String, siteBaseUrl: String): LinkTarget.Screen? {
        // Само строго разчетен https адрес от сайта (TrustedLinks) — иначе браузърът.
        val link = TrustedLinks.siteLink(url, siteBaseUrl) ?: return null
        val path = link.path.trimEnd('/').ifEmpty { "/" }
        if (path.substringAfterLast('/').contains('.')) return null
        return when {
            path == "/" -> LinkTarget.Screen(Routes.HOME)
            // Връзка към книга (`#zapis=`/`?inv=`) — книгата над каталога; иначе самият каталог.
            path == CATALOG_PATH -> (ScanCodes.resolve(url, siteBaseUrl) as? ScanTarget.Inventory)
                ?.let { LinkTarget.Screen(Routes.CATALOG, listOf(Routes.book(it.inv))) }
                ?: LinkTarget.Screen(Routes.CATALOG)
            path == "/news" -> LinkTarget.Screen(Routes.NEWS)
            // По публичния адрес без заявка/котва (така е и в кеша на новините).
            path.startsWith("/news/") -> LinkTarget.Screen(Routes.NEWS, listOf(Routes.article(siteBaseUrl.trimEnd('/') + path)))
            path == "/events" -> eventsTab()
            path == "/kontakti" -> LinkTarget.Screen(Routes.HOME, listOf(Routes.CONTACTS))
            path == "/load" || path == "/istoricheski-publikacii" -> LinkTarget.Screen(Routes.MORE, listOf(Routes.DOCUMENTS))
            path == "/policy" -> LinkTarget.Screen(Routes.MORE, listOf(Routes.PRIVACY))
            // Страницата се зарежда от адреса на сайта (произходът е от конфигурацията, не от връзката).
            path.count { it == '/' } == 1 -> TrustedLinks.canonicalSiteUrl(url, siteBaseUrl)
                ?.let { LinkTarget.Screen(Routes.MORE, listOf(Routes.page(it, ""))) }
            else -> null
        }
    }
}
