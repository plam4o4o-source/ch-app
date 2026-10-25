package org.chyavorec.data

import kotlinx.coroutines.CompletableDeferred
import org.chyavorec.data.catalog.CatalogIndexColumns
import org.chyavorec.data.catalog.CatalogIndexHit
import org.chyavorec.data.catalog.CatalogIndexLostException
import org.chyavorec.data.catalog.CatalogIndexQuery
import org.chyavorec.data.catalog.CatalogIndexRow
import org.chyavorec.data.catalog.CatalogIndexState
import org.chyavorec.data.catalog.CatalogIndexStore
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogSort

/**
 * [CatalogIndexStore] в паметта, който изпълнява [CatalogIndexQuery] със семантиката на
 * SQL заявката в приложението (Room FTS4, unicode61): всяка FTS фраза съвпада, ако
 * термините ѝ (поредици от букви/цифри) стоят последователно в една колона, като
 * последният е префикс, когато фразата завършва с буква/цифра (`"дума*"`).
 */
class FakeFtsIndexStore : CatalogIndexStore {
    var state: CatalogIndexState? = null
    var rows: List<CatalogIndexRow> = emptyList()
    var replaces = 0
    val queries = mutableListOf<CatalogIndexQuery>()
    /** Справките за отделни книги (име на метода) — за проверка, че не минават през пълно търсене. */
    val lookups = mutableListOf<String>()
    var failing = false
    /** Ако е зададен — попълването чака отварянето му (новото съдържание се вижда едва след това, наведнъж). */
    var replaceGate: CompletableDeferred<Unit>? = null
    /** Ако е зададен — изчистването изпразва индекса и после чака отварянето му (като VACUUM след DELETE). */
    var clearGate: CompletableDeferred<Unit>? = null
    /** Съдържанието е изгубено (като пресъздадената празна база) — справките хвърлят, докато не се попълни. */
    var lost = false
        private set

    /** Като RoomCatalogIndexStore при повредена база: създадена наново, празна. */
    fun lose() {
        state = null
        rows = emptyList()
        lost = true
    }

    override suspend fun state(): CatalogIndexState? {
        if (failing) error("db")
        return state
    }

    override suspend fun replace(state: CatalogIndexState, rows: List<CatalogIndexRow>) {
        if (failing) error("db")
        replaceGate?.await()
        replaces++
        this.state = state
        this.rows = rows
        lost = false
    }

    override suspend fun clear() {
        if (failing) error("db")
        state = null
        rows = emptyList()
        lost = false
        clearGate?.await()
    }

    private fun read() {
        if (failing) error("db")
        if (lost) throw CatalogIndexLostException()
    }

    private fun lookup(name: String): List<CatalogIndexRow> {
        read()
        lookups += name
        return rows.sortedBy { it.ord }
    }

    override suspend fun book(inv: Long): CatalogBook? = lookup("book").lastOrNull { it.book.inv == inv }?.book

    override suspend fun books(invs: Collection<Long>): List<CatalogBook> = lookup("books").map { it.book }.filter { it.inv in invs }

    override suspend fun byTitleAndAuthor(title: String, author: String): List<CatalogBook> =
        lookup("byTitleAndAuthor").map { it.book }.filter { it.title == title && it.author == author }

    override suspend fun byAuthor(author: String): List<CatalogBook> = lookup("byAuthor").map { it.book }.filter { it.author == author }

    override suspend fun byIsbn(isbnDigits: Collection<String>): List<CatalogBook> =
        lookup("byIsbn").filter { it.isbnDigits in isbnDigits }.map { it.book }

    override suspend fun authors(): List<Pair<String, String>> =
        lookup("authors").filter { it.book.author.isNotEmpty() }.distinctBy { it.book.author }.map { it.book.author to it.author }

    override suspend fun recommendationCandidates(udcSections: Set<Int>, authorWords: Set<String>?): List<CatalogBook> =
        lookup("recommendationCandidates").filter { r ->
            r.book.available && (authorWords == null || r.udcSection in udcSections || authorWords.any { r.author.contains(it) })
        }.map { listRow(it).book }

    override suspend fun rows(query: CatalogIndexQuery): List<CatalogIndexRow> {
        read()
        queries += query
        // Без текст — само колоните за списъка (останалите празни), както в SQL заявката.
        val out = rows.sortedBy { it.ord }.filter { r -> matches(r, query) }.sortedWith(order(query.sort)).map { if (query.textless) listRow(it) else it }
        val from = query.offset.coerceAtMost(out.size)
        return query.limit?.let { out.subList(from, (from + it).coerceAtMost(out.size)) } ?: out.drop(from)
    }

    override suspend fun hits(query: CatalogIndexQuery): List<CatalogIndexHit> {
        read()
        queries += query
        return rows.sortedBy { it.ord }.filter { r -> matches(r, query) }.sortedWith(order(query.sort)).map { it.toHit() }
    }

    override suspend fun booksAt(ords: List<Int>): List<CatalogBook> {
        val byOrd = lookup("booksAt").associateBy { it.ord }
        return ords.mapNotNull { byOrd[it]?.book }
    }

    override suspend fun count(query: CatalogIndexQuery): Int {
        read()
        queries += query
        return rows.count { r -> matches(r, query) }
    }

    /** Като ORDER BY в [CatalogIndexQuery.sql] (сортирането е стабилно — при равенство редът в каталога). */
    private fun order(sort: CatalogSort?): Comparator<CatalogIndexRow> = when (sort) {
        null -> compareBy { 0 }
        CatalogSort.RELEVANCE, CatalogSort.TITLE -> compareBy { it.titleRank }
        CatalogSort.AUTHOR -> compareBy<CatalogIndexRow> { it.authorRank }.thenBy { it.titleRank }
        CatalogSort.YEAR_DESC -> compareBy<CatalogIndexRow> { it.year == null }.thenByDescending { it.year }
        CatalogSort.YEAR_ASC -> compareBy<CatalogIndexRow> { it.year == null }.thenBy { it.year }
        CatalogSort.NEWEST -> compareByDescending<CatalogIndexRow> { it.book.registeredOn }.thenByDescending { it.book.inv }
    }

    private fun listRow(r: CatalogIndexRow) = r.copy(
        book = r.book.copy(keywords = "", annotation = ""),
        title = "", author = "", keywords = "", annotation = "", publisher = "", docType = "", callNumber = "", invText = "", isbn = "", isbnDigits = "",
    )

    private fun matches(r: CatalogIndexRow, q: CatalogIndexQuery): Boolean {
        val b = r.book
        if (q.matchTokens.isNotEmpty()) {
            val columns = q.matchColumn?.let { listOf(it) } ?: CatalogIndexColumns.FTS
            val docs = columns.map { terms(column(r, it)) }
            for (t in q.matchTokens) if (docs.none { phraseIn(t, it) }) return false
        }
        if (q.contains.isNotEmpty()) {
            val hay = q.containsColumns.joinToString(" ") { column(r, it) }
            if (q.contains.any { !hay.contains(it) }) return false
        }
        q.isbnDigits?.let { if (!r.isbnDigits.contains(it)) return false }
        q.invDigits?.let { if (r.invText != it.trimStart('0') && !r.invText.startsWith(it)) return false }
        if (q.onlyAvailable && !b.available) return false
        q.docType?.let { if (b.docType != it) return false }
        q.department?.let { if (b.department != it) return false }
        q.language?.let { if (b.language != it) return false }
        q.udcSection?.let { if (r.udcSection != it) return false }
        q.yearFrom?.let { if (r.year == null || r.year!! < it) return false }
        q.yearTo?.let { if (r.year == null || r.year!! > it) return false }
        return true
    }

    private fun column(r: CatalogIndexRow, name: String): String = when (name) {
        CatalogIndexColumns.TITLE -> r.title
        CatalogIndexColumns.AUTHOR -> r.author
        CatalogIndexColumns.KEYWORDS -> r.keywords
        CatalogIndexColumns.ANNOTATION -> r.annotation
        CatalogIndexColumns.PUBLISHER -> r.publisher
        CatalogIndexColumns.DOC_TYPE -> r.docType
        CatalogIndexColumns.CALL_NUMBER -> r.callNumber
        CatalogIndexColumns.INV -> r.invText
        CatalogIndexColumns.ISBN -> r.isbn
        else -> error("column $name")
    }

    companion object {
        /** Термините като при unicode61: поредици от букви/цифри, с малки букви. */
        fun terms(text: String): List<String> =
            Regex("[\\p{L}\\p{N}]+").findAll(text.lowercase()).map { it.value }.toList()

        fun phraseIn(phrase: String, doc: List<String>): Boolean {
            val ts = terms(phrase)
            if (ts.isEmpty()) return false
            val prefixLast = phrase.last().isLetterOrDigit()
            for (start in 0..doc.size - ts.size) {
                var ok = true
                for (i in ts.indices) {
                    val d = doc[start + i]
                    val last = i == ts.lastIndex
                    if (!(if (last && prefixLast) d.startsWith(ts[i]) else d == ts[i])) { ok = false; break }
                }
                if (ok) return true
            }
            return false
        }
    }
}
