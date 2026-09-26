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
)
