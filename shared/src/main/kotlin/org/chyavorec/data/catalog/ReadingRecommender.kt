package org.chyavorec.data.catalog

import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem

/**
 * „Подобни книги“ — препоръки само на устройството, от публичния каталог и
 * историята на четенето. Нищо не се изпраща навън: точкува се по общ префикс
 * на УДК (първите 2–3 знака на индекса) и по същия автор. Вече четените
 * заглавия и неналичните екземпляри се пропускат.
 */
object ReadingRecommender {

    fun recommend(engine: CatalogSearchEngine, history: List<HistoryItem>, limit: Int = 12): List<CatalogBook> =
        recommend(engine.snapshot.books, history, engine::book, limit)

    fun recommend(
        books: List<CatalogBook>,
        history: List<HistoryItem>,
        byInv: (Long) -> CatalogBook?,
        limit: Int = 12,
    ): List<CatalogBook> {
        if (history.isEmpty() || books.isEmpty()) return emptyList()
        val readInv = history.mapNotNull { it.inv }.toSet()
        val readBooks = readInv.mapNotNull(byInv)
        val readTitles = (readBooks.map { titleKey(it.title, it.author) } + history.map { titleKey(it.title, it.author) }).toSet()
        // Тежест на всеки префикс/автор = колко пъти се среща в прочетеното.
        val udc3 = HashMap<String, Int>()
        val udc2 = HashMap<String, Int>()
        for (b in readBooks) {
            udcPrefix(b.udc, 3)?.let { udc3[it] = (udc3[it] ?: 0) + 1 }
            udcPrefix(b.udc, 2)?.let { udc2[it] = (udc2[it] ?: 0) + 1 }
        }
        val authors = HashMap<String, Int>()
        for (a in readBooks.flatMap { it.authors } + history.map { it.author }) {
            val k = authorKey(a) ?: continue
            authors[k] = (authors[k] ?: 0) + 1
        }
        if (udc3.isEmpty() && udc2.isEmpty() && authors.isEmpty()) return emptyList()

        val scored = ArrayList<Pair<CatalogBook, Int>>()
        val seenTitles = HashSet<String>()
        for (b in books) {
            if (!b.available || b.inv in readInv) continue
            val tk = titleKey(b.title, b.author)
            if (tk in readTitles) continue
            var score = 0
            udcPrefix(b.udc, 3)?.let { p -> udc3[p]?.let { score += 3 * it } }
            udcPrefix(b.udc, 2)?.let { p -> udc2[p]?.let { score += 1 * it } }
            for (a in b.authors) authorKey(a)?.let { k -> authors[k]?.let { score += 5 * it } }
            if (score <= 0) continue
            if (!seenTitles.add(tk)) continue // един екземпляр от заглавие
            scored += b to score
        }
        return scored.sortedWith(compareByDescending<Pair<CatalogBook, Int>> { it.second }.thenByDescending { it.first.registeredOn }.thenBy { it.first.title })
            .take(limit).map { it.first }
    }

    /** Първите [n] знака от УДК индекса (само цифри/точки), напр. „821.1“ → „821“. */
    internal fun udcPrefix(udc: String, n: Int): String? {
        val digits = udc.trim().takeWhile { it.isDigit() || it == '.' }.filter { it.isDigit() }
        return digits.takeIf { it.length >= n }?.substring(0, n)
    }

    private fun authorKey(a: String): String? =
        a.lowercase().replace(Regex("[^\\p{L}\\p{N}]+"), " ").trim().takeIf { it.length >= 3 && it != "и др" }

    private fun titleKey(title: String, author: String): String =
        (title + "|" + author).lowercase().replace(Regex("[^\\p{L}\\p{N}]+"), " ").trim()
}
