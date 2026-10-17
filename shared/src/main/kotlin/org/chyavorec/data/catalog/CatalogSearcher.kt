package org.chyavorec.data.catalog

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogFacets
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.HistoryItem

/**
 * Началото на резултатите по заявка: [books] е префикс на всички резултати с поне
 * `min(limit, total)` книги (търсачката може да даде и всички наведнъж), [total] — броят на всички.
 */
data class CatalogResults(val books: List<CatalogBook>, val total: Int)

/**
 * Търсенето в каталога (екранът „Каталог“, глобалното търсене) и справките за отделни
 * книги (книга, скенер, история) — през пълнотекстовия индекс или индекса в паметта.
 */
interface CatalogSearcher {
    /** Датата, на която InvLib е генерирал каталога (ISO). */
    val generatedOn: String
    val facets: CatalogFacets

    /**
     * Книгите по заявката. Без текст за търсене пълнотекстовият индекс не чете анотацията
     * и ключовите думи (не се показват в списъка) — за тях се ползва [book].
     */
    suspend fun search(query: CatalogQuery): List<CatalogBook>

    /**
     * Първите [limit] резултата и броят на всички — за списъка на страници. По
     * подразбиране (и с текст за търсене, където точките изискват всички кандидати) —
     * целият резултат наведнъж.
     */
    suspend fun searchFirst(query: CatalogQuery, limit: Int): CatalogResults =
        search(query).let { CatalogResults(it, it.size) }

    /** Следващата страница: [limit] резултата от [offset] нататък (в същия ред като [searchFirst]). */
    suspend fun searchMore(query: CatalogQuery, offset: Int, limit: Int): List<CatalogBook> =
        search(query).drop(offset).take(limit)

    /** Предложения за автоматично допълване: автори и заглавия. */
    suspend fun suggestions(prefix: String, limit: Int = 8): List<String>

    /** Книгата с инвентарния номер. */
    suspend fun book(inv: Long): CatalogBook?
    /** Книгите с тези инвентарни номера (липсващите се пропускат). */
    suspend fun books(invs: Collection<Long>): Map<Long, CatalogBook>
    /** Другите екземпляри от същото заглавие (еднакво заглавие + автор). */
    suspend fun copiesOf(book: CatalogBook): List<CatalogBook>
    /** Други заглавия от същия автор (по един екземпляр), без [excludeInv]. */
    suspend fun byAuthor(author: String, excludeInv: Long, limit: Int = 10): List<CatalogBook>
    /** Книга по ISBN (двете форми ISBN-10/13); наличните екземпляри са с предимство. */
    suspend fun findByIsbn(raw: String?): CatalogBook?
    /** Всички екземпляри с този ISBN. */
    suspend fun allByIsbn(raw: String?): List<CatalogBook>
    /** Различните автори, които съдържат всички [tokens] (нормализирани), в реда на каталога. */
    suspend fun authors(tokens: List<String>, limit: Int = 8): List<String>
    /** „Подобни книги“ по историята на четенето ([ReadingRecommender]). */
    suspend fun recommend(history: List<HistoryItem>, limit: Int = 12): List<CatalogBook>
}

/** Търсене в индекса в паметта ([CatalogSearchEngine]). */
class EngineCatalogSearcher(val engine: CatalogSearchEngine) : CatalogSearcher {
    override val generatedOn: String get() = engine.snapshot.generatedOn
    override val facets: CatalogFacets get() = engine.facets
    override suspend fun search(query: CatalogQuery): List<CatalogBook> = engine.search(query)
    override suspend fun suggestions(prefix: String, limit: Int): List<String> = engine.suggestions(prefix, limit)
    override suspend fun book(inv: Long): CatalogBook? = engine.book(inv)
    override suspend fun books(invs: Collection<Long>): Map<Long, CatalogBook> = engine.books(invs)
    override suspend fun copiesOf(book: CatalogBook): List<CatalogBook> = engine.copiesOf(book)
    override suspend fun byAuthor(author: String, excludeInv: Long, limit: Int): List<CatalogBook> = engine.byAuthor(author, excludeInv, limit)
    override suspend fun findByIsbn(raw: String?): CatalogBook? = engine.findByIsbn(raw)
    override suspend fun allByIsbn(raw: String?): List<CatalogBook> = engine.allByIsbn(raw)
    override suspend fun authors(tokens: List<String>, limit: Int): List<String> = engine.authors(tokens, limit)
    override suspend fun recommend(history: List<HistoryItem>, limit: Int): List<CatalogBook> =
        ReadingRecommender.recommend(engine, history, limit)
}

/**
 * Търсене през пълнотекстовия индекс ([CatalogIndexStore], Room FTS4): индексът дава
 * кандидатите (всяка дума — като начало на дума, плюс филтрите), а точното съвпадение,
 * точките и подреждането са същите като в [CatalogSearchEngine] ([CatalogMatcher]).
 * Ако FTS не намери нищо (напр. дума, търсена по средата), се търси като подниз
 * навсякъде — както в индекса в паметта.
 *
 * Справките за отделни книги са прости заявки по индексирани колони ([CatalogIndexStore]),
 * със същата семантика като в [CatalogSearchEngine].
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

    /**
     * Без текст за търсене (празното търсене при отваряне на „Каталог“) — само страницата
     * и броят от базата (подредени там), не целият каталог; с текст — както [search].
     */
    override suspend fun searchFirst(query: CatalogQuery, limit: Int): CatalogResults {
        if (TextNormalizer.tokens(query.text).isNotEmpty()) return search(query).let { CatalogResults(it, it.size) }
        return guarded({ e -> e.search(query).let { CatalogResults(it.take(limit), it.size) } }) {
            val plan = CatalogIndexQueries.page(query, offset = 0, limit = limit)
            CatalogResults(store.rows(plan).map { it.book }, store.count(plan))
        }
    }

    override suspend fun searchMore(query: CatalogQuery, offset: Int, limit: Int): List<CatalogBook> {
        if (TextNormalizer.tokens(query.text).isNotEmpty()) return search(query).drop(offset).take(limit)
        return guarded({ it.search(query).drop(offset).take(limit) }) {
            store.rows(CatalogIndexQueries.page(query, offset, limit)).map { it.book }
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

    override suspend fun book(inv: Long): CatalogBook? = guarded({ it.book(inv) }) { store.book(inv) }

    override suspend fun books(invs: Collection<Long>): Map<Long, CatalogBook> {
        if (invs.isEmpty()) return emptyMap()
        return guarded({ it.books(invs) }) { store.books(invs.toSet()).associateBy { it.inv } }
    }

    override suspend fun copiesOf(book: CatalogBook): List<CatalogBook> =
        guarded({ it.copiesOf(book) }) { store.byTitleAndAuthor(book.title, book.author).filter { it.inv != book.inv } }

    override suspend fun byAuthor(author: String, excludeInv: Long, limit: Int): List<CatalogBook> {
        if (author.isBlank()) return emptyList()
        return guarded({ it.byAuthor(author, excludeInv, limit) }) { CatalogSearchEngine.sameAuthor(store.byAuthor(author), excludeInv, limit) }
    }

    override suspend fun findByIsbn(raw: String?): CatalogBook? = CatalogSearchEngine.preferAvailable(allByIsbn(raw))

    override suspend fun allByIsbn(raw: String?): List<CatalogBook> {
        val wanted = CatalogSearchEngine.isbnKeys(raw)
        if (wanted.isEmpty()) return emptyList()
        return guarded({ it.allByIsbn(raw) }) { store.byIsbn(wanted) }
    }

    /** Различните автори — четат се веднъж (малко данни), после се филтрират в паметта. */
    @Volatile private var authorIndex: List<Pair<String, String>>? = null

    override suspend fun authors(tokens: List<String>, limit: Int): List<String> {
        if (tokens.isEmpty()) return emptyList()
        return guarded({ it.authors(tokens, limit) }) {
            val all = authorIndex ?: store.authors().filter { it.first.isNotBlank() }.also { authorIndex = it }
            withContext(work) { CatalogSearchEngine.matchingAuthors(all, tokens, limit) }
        }
    }

    override suspend fun recommend(history: List<HistoryItem>, limit: Int): List<CatalogBook> {
        if (history.isEmpty()) return emptyList()
        return guarded({ ReadingRecommender.recommend(it, history, limit) }) {
            val read = books(history.mapNotNull { it.inv })
            val pre = ReadingRecommender.prefilter(history, read::get) ?: return@guarded emptyList()
            // Само наличните книги, които изобщо могат да получат точки — не целият каталог.
            val candidates = store.recommendationCandidates(pre.udcSections, pre.authorWords)
            withContext(work) { ReadingRecommender.recommend(candidates, history, read::get, limit) }
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
            // Базата на индекса не отговаря — търсене в паметта; причината — в локалния дневник.
            Diagnostics.record("catalog.search", e)
            val engine = fallback() ?: throw e
            withContext(work) { onEngine(engine) }
        }

    private companion object {
        const val SUGGESTION_PAGE = 200
    }
}
