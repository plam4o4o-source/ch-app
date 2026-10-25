package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/** Новина от модула „Новини на сайта“ на chyavorec.org (RSS). */
@Serializable
data class NewsArticle(
    /** Стабилен идентификатор: guid от RSS или адресът на статията. */
    val id: String,
    val title: String,
    val url: String,
    /** Епоха в милисекунди (UTC); null ако сайтът не е подал дата. */
    val publishedAtMillis: Long? = null,
    val summary: String = "",
    val imageUrl: String? = null,
    val category: String? = null,
    val author: String? = null,
    /** Пълното съдържание, ако RSS го подава (content:encoded). */
    val contentBlocks: List<ContentBlock> = emptyList(),
)

/** Пълна статия — заредена от самата страница на новината. */
@Serializable
data class ArticleDetail(
    val article: NewsArticle,
    val blocks: List<ContentBlock>,
    val gallery: List<String>,
    /**
     * Описанието (alt/figcaption) на снимките от [gallery] по адрес — за прегледа на цял
     * екран и екранния четец; липсващите нямат описание. Празно в кеша от по-стари версии.
     */
    val galleryAlts: Map<String, String> = emptyMap(),
) {
    /** Описанията в реда на [gallery] (празно — без описание). */
    val galleryAltList: List<String> get() = gallery.map { galleryAlts[it].orEmpty() }

    companion object {
        /** Описанията на снимките от съдържанието (първото непразно за всеки адрес). */
        fun altsOf(blocks: List<ContentBlock>): Map<String, String> = buildMap {
            for (b in blocks) if (b is ContentBlock.Image) {
                val alt = b.caption?.trim()?.replace(Regex("\\s+"), " ").orEmpty()
                if (alt.isNotEmpty() && b.url !in this) put(b.url, alt)
            }
        }
    }
}

/**
 * Ключът, по който се разпознава една и съща новина, независимо как е отворена: от
 * списъка/„Начало“/търсенето/известие идва идентификаторът ѝ, а от адрес на сайта (App
 * Link) — публичният ѝ адрес. Адресите се сравняват нормализирани: без схема, `www.`,
 * заявка, котва и крайна наклонена черта, с малки букви в домейна.
 */
object ArticleKeys {
    private val URL = Regex("^(?:https?:)?//(?:www\\.)?([^/?#]+)([^?#]*)", RegexOption.IGNORE_CASE)

    fun normalize(key: String): String {
        val k = key.trim()
        val m = URL.find(k) ?: return k.trimEnd('/')
        return m.groupValues[1].lowercase() + m.groupValues[2].trimEnd('/')
    }

    /** Новината с [id] и [url] е тази, към която води [key] (идентификатор или адрес). */
    fun matches(id: String, url: String, key: String): Boolean {
        if (key.isBlank()) return false
        if (id == key) return true
        val k = normalize(key)
        return normalize(id) == k || (url.isNotBlank() && normalize(url) == k)
    }
}
