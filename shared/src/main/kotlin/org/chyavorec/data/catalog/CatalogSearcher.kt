package org.chyavorec.data.catalog

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogFacets
import org.chyavorec.domain.model.CatalogQuery

/** Търсенето на екрана „Каталог“ — през пълнотекстовия индекс или индекса в паметта. */
interface CatalogSearcher {
    /** Датата, на която InvLib е генерирал каталога (ISO). */
    val generatedOn: String
    val facets: CatalogFacets
    suspend fun search(query: CatalogQuery): List<CatalogBook>
    /** Предложения за автоматично допълване: автори и заглавия. */
    suspend fun suggestions(prefix: String, limit: Int = 8): List<String>
}

/** Търсене в индекса в паметта ([CatalogSearchEngine]). */
class EngineCatalogSearcher(val engine: CatalogSearchEngine) : CatalogSearcher {
    override val generatedOn: String get() = engine.snapshot.generatedOn
    override val facets: CatalogFacets get() = engine.facets
    override suspend fun search(query: CatalogQuery): List<CatalogBook> = engine.search(query)
    override suspend fun suggestions(prefix: String, limit: Int): List<String> = engine.suggestions(prefix, limit)
}

/**
 * Търсене през пълнотекстовия индекс ([CatalogIndexStore], Room FTS4): индексът дава
 * кандидатите (всяка дума — като начало на дума, плюс филтрите), а точното съвпадение,
 * точките и подреждането са същите като в [CatalogSearchEngine] ([CatalogMatcher]).
 * Ако FTS не намери нищо (напр. дума, търсена по средата), се търси като подниз
 * навсякъде — както в индекса в паметта.
 *
 * Целият каталог не се държи в паметта. При грешка на базата се ползва [fallback]
 * (индексът в паметта).
 */
class FtsCatalogSearcher(
    private val store: CatalogIndexStore,
    val state: CatalogIndexState,
    private val fallback: suspend () -> CatalogSearchEngine?,
    private val work: CoroutineDispatcher = Dispatchers.Default,
) : CatalogSearcher {
    override val generatedOn: String get() = state.generatedOn
    override val facets: CatalogFacets get() = state.facets

    override suspend fun search(query: CatalogQuery): List<CatalogBook> {
        val tokens = TextNormalizer.tokens(query.text)
        val digits = TextNormalizer.digits(query.text)
        return guarded({ it.search(query) }) {
            val plan = CatalogIndexQueries.search(query, tokens, digits) ?: return@guarded emptyList()
            val found = rank(store.rows(plan), query, tokens, digits)
            if (found.isNotEmpty() || plan.matchTokens.isEmpty()) return@guarded found
            val scan = CatalogIndexQueries.scan(query, tokens) ?: return@guarded found
            rank(store.rows(scan), query, tokens, digits)
        }
    }

    override suspend fun suggestions(prefix: String, limit: Int): List<String> {
        val tokens = TextNormalizer.tokens(prefix)
        if (tokens.isEmpty()) return emptyList()
        return guarded({ it.suggestions(prefix, limit) }) {
            val out = LinkedHashSet<String>()
            var offset = 0
            while (out.size < limit) {
                val q = CatalogIndexQueries.suggestions(tokens, offset, SUGGESTION_PAGE) ?: break
                val rows = store.rows(q)
                for (r in rows) if (!CatalogSearchEngine.addSuggestions(out, r.book, r.title, r.author, tokens, limit)) break
                if (rows.size < SUGGESTION_PAGE) break
                offset += SUGGESTION_PAGE
            }
            out.toList()
        }
    }

    private suspend fun rank(rows: List<CatalogIndexRow>, q: CatalogQuery, tokens: List<String>, digits: String): List<CatalogBook> =
        withContext(work) {
            val hits = ArrayList<CatalogMatcher.Hit>()
            for (r in rows) {
                if (!CatalogMatcher.passesFilters(r.book, r.year, q)) continue
                val score = if (tokens.isEmpty()) 0
                else CatalogMatcher.score(r.title, r.author, r.keywords, r.rest, r.isbnDigits, r.book.inv, q.field, tokens, digits) ?: continue
                hits += CatalogMatcher.Hit(r.book, score, r.titleRank, r.authorRank, r.year)
            }
            CatalogMatcher.sort(hits, q.sort, hasText = tokens.isNotEmpty())
            hits.map { it.book }
        }

    private suspend fun <T> guarded(onEngine: (CatalogSearchEngine) -> T, block: suspend () -> T): T =
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val engine = fallback() ?: throw e
            withContext(work) { onEngine(engine) }
        }

    private companion object {
        const val SUGGESTION_PAGE = 200
    }
}
