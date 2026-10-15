package org.chyavorec.data.catalog

import org.chyavorec.core.TextNormalizer
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
        val p = profile(history, byInv) ?: return emptyList()
        val scored = ArrayList<Pair<CatalogBook, Int>>()
        val seenTitles = HashSet<String>()
        for (b in books) {
            if (!b.available || b.inv in p.readInv) continue
            val tk = titleKey(b.title, b.author)
            if (tk in p.readTitles) continue
            var score = 0
            udcPrefix(b.udc, 3)?.let { pre -> p.udc3[pre]?.let { score += 3 * it } }
            udcPrefix(b.udc, 2)?.let { pre -> p.udc2[pre]?.let { score += 1 * it } }
            for (a in b.authors) authorKey(a)?.let { k -> p.authors[k]?.let { score += 5 * it } }
            if (score <= 0) continue
            if (!seenTitles.add(tk)) continue // един екземпляр от заглавие
            scored += b to score
        }
        return scored.sortedWith(compareByDescending<Pair<CatalogBook, Int>> { it.second }.thenByDescending { it.first.registeredOn }.thenBy { it.first.title })
            .take(limit).map { it.first }
    }

    /**
     * Предварителен подбор в базата: само наличните книги от тези раздели на УДК или с
     * някоя от тези думи в нормализирания автор могат да получат точки в [recommend]
     * (надмножество — точкуването и подреждането остават същите).
     */
    class Prefilter(
        val udcSections: Set<Int>,
        /** Най-дългата дума на всеки автор от прочетеното, нормализирана; `null` — без подбор по автор (всички налични). */
        val authorWords: Set<String>?,
    )

    /** `null` — няма какво да се препоръча (празна история или нищо за точкуване). */
    fun prefilter(history: List<HistoryItem>, byInv: (Long) -> CatalogBook?): Prefilter? {
        if (history.isEmpty()) return null
        val p = profile(history, byInv) ?: return null
        // Префиксът по УДК започва с първата цифра на индекса — тя е и разделът (udc_section).
        val sections = p.udc2.keys.mapTo(HashSet()) { it[0].digitToInt() }
        // Ключът на автора е от думи (букви/цифри); всяка от тях е подниз и на нормализирания автор.
        val words = p.authors.keys.mapTo(HashSet()) { key -> TextNormalizer.normalize(key.split(' ').maxBy { it.length }) }
        val usable = words.none { it.isEmpty() } && words.size <= MAX_AUTHOR_WORDS
        return Prefilter(sections, if (usable) words else null)
    }

    /** Какво се търси: прочетените книги и тежестите на префиксите на УДК и авторите. */
    private class Profile(
        val readInv: Set<Long>,
        val readTitles: Set<String>,
        val udc3: Map<String, Int>,
        val udc2: Map<String, Int>,
        val authors: Map<String, Int>,
    )

    private fun profile(history: List<HistoryItem>, byInv: (Long) -> CatalogBook?): Profile? {
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
        if (udc3.isEmpty() && udc2.isEmpty() && authors.isEmpty()) return null
        return Profile(readInv, readTitles, udc3, udc2, authors)
    }

    /** Повече думи — без подбор по автор (параметрите на една SQL заявка са ограничени). */
    private const val MAX_AUTHOR_WORDS = 500

    /** Първите [n] знака от УДК индекса (само цифри/точки), напр. „821.1“ → „821“. */
    internal fun udcPrefix(udc: String, n: Int): String? {
        val digits = udc.trim().takeWhile { it.isDigit() || it == '.' }.filter { it.isDigit() }
        return digits.takeIf { it.length >= n }?.substring(0, n)
    }

    /** Всичко освен букви и цифри (компилира се веднъж, не при всеки запис). */
    private val nonAlnum = Regex("[^\\p{L}\\p{N}]+")

    /** Ключ на автора (малки букви, без препинателни знаци); `null` — твърде кратък или „и др.“. */
    internal fun authorKey(a: String): String? =
        a.lowercase().replace(nonAlnum, " ").trim().takeIf { it.length >= 3 && it != "и др" }

    /** Ключ на заглавието (заглавие + автор, без препинателни знаци) — екземплярите на една книга са с еднакъв ключ. */
    internal fun titleKey(title: String, author: String): String =
        (title + "|" + author).lowercase().replace(nonAlnum, " ").trim()
}
