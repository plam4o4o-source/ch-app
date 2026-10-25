package org.chyavorec.data.catalog

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
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
    override suspend fun search(query: CatalogQuery): List<CatalogBook> {
        val context = currentCoroutineContext()
        return engine.search(query) { context.ensureActive() }
    }
    override suspend fun suggestions(prefix: String, limit: Int): List<String> = engine.suggestions(prefix, limit)
    override suspend fun book(inv: Long): CatalogBook? = engine.book(inv)
    override suspend fun books(invs: Collection<Long>): Map<Long, CatalogBook> = engine.books(invs)
    override suspend fun copiesOf(book: CatalogBook): List<CatalogBook> = engine.copiesOf(book)
    override suspend fun byAuthor(author: String, excludeInv: Long, limit: Int): List<CatalogBook> = engine.byAuthor(author, excludeInv, limit)
    override suspend fun findByIsbn(raw: String?): CatalogBook? = engine.findByIsbn(raw)
    override suspend fun allByIsbn(raw: String?): List<CatalogBook> = engine.allByIsbn(raw)
    override suspend fun authors(tokens: List<String>, limit: Int): List<String> = engine.authors(tokens, limit)
    override suspend fun recommend(history: List<HistoryItem>, limit: Int): List<CatalogBook> {
        val context = currentCoroutineContext()
        return ReadingRecommender.recommend(engine.snapshot.books, history, engine::book, limit) { context.ensureActive() }
    }
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
 * (индексът в паметта); ако индексът е изгубен ([CatalogIndexLostException] — базата е
 * създадена наново, празна), преди това се вика [onIndexLost], за да се попълни отново.
 *
 * Докато индексът се попълва за нов каталог, търсачката отговаря от стария (транзакцията
 * още не е приключила). Книга или ISBN, които ги няма в него (напр. току-що добавена
 * книга в скенера), изчакват новия индекс ([awaitNewer]) и се търсят още веднъж.
 */
class FtsCatalogSearcher(
    private val store: CatalogIndexStore,
    val state: CatalogIndexState,
    private val fallback: suspend () -> CatalogSearchEngine?,
    private val work: CoroutineDispatcher = Dispatchers.Default,
    private val onIndexLost: suspend () -> Unit = {},
    /** Изчаква попълването на по-нов индекс, ако тече; `true` — записан е (справката се повтаря). */
    private val awaitNewer: suspend () -> Boolean = { false },
) : CatalogSearcher {
    override val generatedOn: String get() = state.generatedOn
    override val facets: CatalogFacets get() = state.facets

    override suspend fun search(query: CatalogQuery): List<CatalogBook> {
        // Без текст — само филтрите, подредени в базата (като [CatalogMatcher.sort] без точки).
        if (TextNormalizer.tokens(query.text).isEmpty()) return guarded({ it.search(query) }) {
            store.rows(CatalogIndexQueries.search(query, emptyList(), "")!!.copy(sort = query.sort)).map { it.book }
        }
        return guarded({ it.search(query) }) { store.booksAt(ranked(query).asList()) }
    }

    /**
     * Без текст за търсене (празното търсене при отваряне на „Каталог“) — само страницата
     * и броят от базата (подредени там), не целият каталог. С текст — точкуват се само
     * леките записи ([CatalogIndexHit]), а книгите се четат само за страницата.
     */
    override suspend fun searchFirst(query: CatalogQuery, limit: Int): CatalogResults {
        if (TextNormalizer.tokens(query.text).isNotEmpty()) {
            return guarded({ e -> e.search(query).let { CatalogResults(it.take(limit), it.size) } }) {
                val ords = ranked(query)
                CatalogResults(store.booksAt(ords.asList().take(limit)), ords.size)
            }
        }
        return guarded({ e -> e.search(query).let { CatalogResults(it.take(limit), it.size) } }) {
            val plan = CatalogIndexQueries.page(query, offset = 0, limit = limit)
            CatalogResults(store.rows(plan).map { it.book }, store.count(plan))
        }
    }

    override suspend fun searchMore(query: CatalogQuery, offset: Int, limit: Int): List<CatalogBook> {
        if (TextNormalizer.tokens(query.text).isNotEmpty()) {
            return guarded({ it.search(query).drop(offset).take(limit) }) {
                val ords = ranked(query, reuse = true).asList()
                store.booksAt(ords.subList(offset.coerceAtMost(ords.size), (offset + limit).coerceAtMost(ords.size)))
            }
        }
        return guarded({ it.search(query).drop(offset).take(limit) }) {
            store.rows(CatalogIndexQueries.page(query, offset, limit)).map { it.book }
        }
    }

    /**
     * Последната заявка с текст и подредените ѝ резултати (местата в каталога): „Още“
     * чете само следващата страница книги, без да точкува кандидатите наново.
     */
    @Volatile private var lastRanked: Pair<CatalogQuery, IntArray>? = null

    /**
     * Резултатите с текст, подредени (местата в каталога): кандидатите от FTS (или резервното
     * търсене като подниз), точкувани по лекия им изглед. [reuse] — „Още“ за същата заявка
     * взима вече подредените (новото търсене винаги чете индекса наново — може да е попълнен междувременно).
     */
    private suspend fun ranked(query: CatalogQuery, reuse: Boolean = false): IntArray {
        if (reuse) lastRanked?.let { (q, ords) -> if (q == query) return ords }
        val tokens = TextNormalizer.tokens(query.text)
        val digits = TextNormalizer.digits(query.text)
        val plan = CatalogIndexQueries.search(query, tokens, digits) ?: return IntArray(0)
        var found = rank(store.hits(plan), query, tokens, digits)
        if (found.isEmpty() && plan.matchTokens.isNotEmpty()) {
            CatalogIndexQueries.scan(query, tokens)?.let { found = rank(store.hits(it), query, tokens, digits) }
        }
        lastRanked = query to found
        return found
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

    override suspend fun book(inv: Long): CatalogBook? =
        guarded({ it.book(inv) }) { store.book(inv) ?: if (awaitNewer()) store.book(inv) else null }

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
        return guarded({ it.allByIsbn(raw) }) { store.byIsbn(wanted).ifEmpty { if (awaitNewer()) store.byIsbn(wanted) else emptyList() } }
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
            // Ако разделите на УДК и без това обхващат повечето книги — всички налични, без условия.
            val broad = ReadingRecommender.coversMost(pre.udcSections, state.availableBySection, state.facets.udcSections)
            val candidates = if (broad) store.recommendationCandidates(emptySet(), null)
            else store.recommendationCandidates(pre.udcSections, pre.authorWords)
            withContext(work) {
                val context = currentCoroutineContext()
                ReadingRecommender.recommend(candidates, history, read::get, limit) { context.ensureActive() }
            }
        }
    }

    /**
     * Точното съвпадение, точките и подреждането (като в [CatalogSearchEngine]); филтрите
     * вече са в заявката. Прекъсва се (отмяна), щом заявката остарее — на всеки
     * [CatalogSearchEngine.CHECK_EVERY] записа.
     */
    private suspend fun rank(rows: List<CatalogIndexHit>, q: CatalogQuery, tokens: List<String>, digits: String): IntArray =
        withContext(work) {
            val context = currentCoroutineContext()
            val hits = ArrayList<CatalogMatcher.Hit>()
            for ((i, r) in rows.withIndex()) {
                if (i % CatalogSearchEngine.CHECK_EVERY == 0) context.ensureActive()
                val score = CatalogMatcher.score(r.title, r.author, r.keywords, r.rest, r.isbnDigits, r.inv, q.field, tokens, digits) ?: continue
                hits += CatalogMatcher.Hit(r.ord, score, r.titleRank, r.authorRank, r.year, r.registeredOn, r.inv)
            }
            context.ensureActive()
            CatalogMatcher.sort(hits, q.sort, hasText = true)
            IntArray(hits.size) { hits[it].ord }
        }

    private suspend fun <T> guarded(onEngine: (CatalogSearchEngine) -> T, block: suspend () -> T): T =
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Базата на индекса не отговаря — търсене в паметта; причината — в локалния дневник.
            Diagnostics.record("catalog.search", e)
            if (e is CatalogIndexLostException) onIndexLost()
            val engine = fallback() ?: throw e
            withContext(work) { onEngine(engine) }
        }

    private companion object {
        const val SUGGESTION_PAGE = 200
    }
}
