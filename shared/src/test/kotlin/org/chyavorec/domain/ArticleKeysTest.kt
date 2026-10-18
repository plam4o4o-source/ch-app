package org.chyavorec.domain

import kotlinx.coroutines.test.runTest
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.ArticleKeys
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.NewsService
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Една и съща новина — по идентификатор или по адрес в различен запис. */
class ArticleKeysTest {
    private val url = "https://chyavorec.org/news/2026-10-01-sabor"

    @Test fun urlVariantsNormalizeToTheSameKey() {
        val k = ArticleKeys.normalize(url)
        listOf(
            "$url/", "http://chyavorec.org/news/2026-10-01-sabor", "https://www.chyavorec.org/news/2026-10-01-sabor",
            "https://CHYAVOREC.org/news/2026-10-01-sabor?utm_source=fb", "$url#comments", " $url ",
        ).forEach { assertEquals(k, ArticleKeys.normalize(it), it) }
        // Пътят не губи главните букви (на сайта е значим).
        assertFalse(ArticleKeys.normalize("https://chyavorec.org/news/A") == ArticleKeys.normalize("https://chyavorec.org/news/a"))
    }

    @Test fun matchesByIdOrByUrl() {
        assertTrue(ArticleKeys.matches("guid-7", url, "guid-7"))
        assertTrue(ArticleKeys.matches("guid-7", url, "http://www.chyavorec.org/news/2026-10-01-sabor/"))
        assertTrue(ArticleKeys.matches(url, url, "$url/"))
        assertFalse(ArticleKeys.matches("guid-7", url, "guid-8"))
        assertFalse(ArticleKeys.matches("guid-7", url, "https://chyavorec.org/news/other"))
        assertFalse(ArticleKeys.matches("guid-7", "", ""))
    }

    @Test fun repositoryFindsTheArticleByAnyUrlVariant() = runTest {
        val service = object : NewsService {
            override suspend fun fetchLatest() = Outcome.Success(listOf(NewsArticle("guid-7", "Събор", url)))
            override suspend fun fetchArticle(article: NewsArticle) = Outcome.Success(ArticleDetail(article, emptyList(), emptyList()))
        }
        val repo = NewsRepository(service, InMemoryPayloadCache(), FixedClock(Instant.parse("2026-10-01T10:00:00Z")))
        repo.latest(false)
        assertEquals("guid-7", repo.find("guid-7")?.id)
        assertEquals("guid-7", repo.find("http://www.chyavorec.org/news/2026-10-01-sabor/")?.id)
        assertEquals(null, repo.find("https://chyavorec.org/news/other"))
    }
}
