package org.chyavorec.core

import java.net.URI
import java.net.URISyntaxException

/**
 * Единственото място, където се решава дали един адрес е „наш“ — т.е. дали приложението
 * може да го покаже като страница на читалището (native екран, страница от сайта).
 *
 * Всичко, което не минава проверката, се отваря във външния браузър (там адресът се
 * вижда), никога в нашия интерфейс. Проверката е върху разчетения адрес ([URI]), а не
 * върху низа: `endsWith`/`contains` пропускат `evilchyavorec.org`, а
 * `https://chyavorec.org:443@evil.com/` (потребител и парола преди `@`) всъщност е evil.com.
 *
 * Правила:
 * - само `https` (`http` — единствено за локалните тестове, когато и базовият адрес е `http`
 *   на същия хост и порт); `javascript:`, `intent:`, `content:`, `file:` и т.н. — никога;
 * - точен хост: [SITE_HOSTS] (плюс хоста на базовия адрес от конфигурацията) — без
 *   представки и наставки;
 * - без потребител/парола (`@`), без `\`, интервали и управляващи знаци, без кодиран
 *   (`%2e`) или не-ASCII хост; главните букви в схемата и хоста са без значение;
 * - стандартният порт (или същият като на базовия адрес).
 */
object TrustedLinks {
    /** Каноничният домейн на сайта (App Links в AndroidManifest.xml са само за него). */
    const val SITE_HOST = "chyavorec.org"

    /** Домейните на сайта на читалището — точно тези, без поддомейни. */
    val SITE_HOSTS: Set<String> = setOf(SITE_HOST, "www.$SITE_HOST")

    private const val MAX_LENGTH = 4096
    private val HOST_NAME = Regex("^[a-z0-9]([a-z0-9-]*[a-z0-9])?(\\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$")
    private val IPV6 = Regex("^\\[[0-9a-f:.]+]$")

    /** Разчетен адрес: схемата и хостът са с малки букви; [port] = -1 — подразбиращият се. */
    data class Link(
        val scheme: String,
        val host: String,
        val port: Int,
        /** Декодираният път (`/news/…`), за разпознаване на раздела; празен — началото. */
        val path: String,
        /** Пътят, както е в адреса (кодиран) — за сглобяване на адрес наново. */
        val rawPath: String,
        val rawQuery: String?,
        val rawFragment: String?,
    )

    /**
     * Строго разчитане на `http(s)` адрес; `null` — друга схема или съмнителен адрес
     * (потребител в адреса, `\`, интервали, кодиран/не-ASCII хост, грешен синтаксис).
     */
    fun parse(url: String?): Link? {
        val s = url?.trim() ?: return null
        if (s.isEmpty() || s.length > MAX_LENGTH) return null
        // Браузърите (и OkHttp) четат `\` като `/`, а java.net.URI — не: различно разчитане = отказ.
        if (s.any { it == '\\' || it.isWhitespace() || it.isISOControl() }) return null
        val uri = try {
            URI(s)
        } catch (_: URISyntaxException) {
            return null
        }
        val scheme = uri.scheme?.lowercase() ?: return null
        if (scheme != "https" && scheme != "http") return null
        if (uri.isOpaque) return null
        val authority = uri.rawAuthority ?: return null
        if (uri.rawUserInfo != null || '@' in authority) return null
        // Хост с % или не-ASCII знаци java.net.URI не разпознава като хост (host = null).
        val host = uri.host?.lowercase() ?: return null
        if (!HOST_NAME.matches(host) && !IPV6.matches(host)) return null
        // Изричният стандартен порт (`:443`) е същият адрес като без порт.
        val port = uri.port.takeIf { it != defaultPort(scheme) } ?: -1
        return Link(scheme, host, port, uri.path.orEmpty(), uri.rawPath.orEmpty(), uri.rawQuery, uri.rawFragment)
    }

    /** Хостът на адреса (малки букви) или `null` за неразчетен/неподходящ адрес. */
    fun hostOf(url: String?): String? = parse(url)?.host

    /**
     * Адресът е от сайта на читалището: `https` на [SITE_HOSTS] или на хоста на
     * [siteBaseUrl] (с или без `www.`). Върнатият [Link] е разчетеният адрес.
     */
    fun siteLink(url: String?, siteBaseUrl: String): Link? {
        val link = parse(url) ?: return null
        val base = parse(siteBaseUrl)
        val baseHost = base?.host
        val hosts = buildSet {
            addAll(SITE_HOSTS)
            if (baseHost != null) {
                add(baseHost)
                add(if (baseHost.startsWith("www.")) baseHost.removePrefix("www.") else "www.$baseHost")
            }
        }
        if (link.host !in hosts) return null
        val sameOrigin = base != null && link.host == base.host && link.port == base.port
        return when (link.scheme) {
            "https" -> link.takeIf { it.port == -1 || (sameOrigin && base?.scheme == "https") }
            // http — само при http базов адрес (MockWebServer в тестовете), на същия хост и порт.
            else -> link.takeIf { sameOrigin && base?.scheme == "http" }
        }
    }

    fun isSiteUrl(url: String?, siteBaseUrl: String): Boolean = siteLink(url, siteBaseUrl) != null

    /**
     * Каноничният адрес на страница от сайта за зареждане: произходът (схема, хост, порт)
     * е винаги този на [siteBaseUrl], а от [url] се взимат само пътят и заявката.
     * `null` — [url] не е от сайта.
     */
    fun canonicalSiteUrl(url: String?, siteBaseUrl: String): String? {
        val link = siteLink(url, siteBaseUrl) ?: return null
        val origin = siteBaseUrl.trim().trimEnd('/')
        val path = link.rawPath.ifEmpty { "/" }
        return origin + path + (link.rawQuery?.let { "?$it" }.orEmpty())
    }

    private fun defaultPort(scheme: String) = if (scheme == "https") 443 else 80
}
