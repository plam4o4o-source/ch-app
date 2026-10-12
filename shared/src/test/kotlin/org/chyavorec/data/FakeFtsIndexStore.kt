package org.chyavorec.data

import org.chyavorec.data.catalog.CatalogIndexColumns
import org.chyavorec.data.catalog.CatalogIndexQuery
import org.chyavorec.data.catalog.CatalogIndexRow
import org.chyavorec.data.catalog.CatalogIndexState
import org.chyavorec.data.catalog.CatalogIndexStore

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
    var failing = false

    override suspend fun state(): CatalogIndexState? {
        if (failing) error("db")
        return state
    }

    override suspend fun replace(state: CatalogIndexState, rows: List<CatalogIndexRow>) {
        if (failing) error("db")
        replaces++
        this.state = state
        this.rows = rows
    }

    override suspend fun rows(query: CatalogIndexQuery): List<CatalogIndexRow> {
        if (failing) error("db")
        queries += query
        val out = rows.sortedBy { it.ord }.filter { r -> matches(r, query) }
        val from = query.offset.coerceAtMost(out.size)
        return query.limit?.let { out.subList(from, (from + it).coerceAtMost(out.size)) } ?: out.drop(from)
    }

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
