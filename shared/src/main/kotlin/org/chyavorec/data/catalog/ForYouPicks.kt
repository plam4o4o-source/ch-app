package org.chyavorec.data.catalog

import org.chyavorec.domain.model.CatalogBook

/**
 * Полицата „За теб“ на началния екран.
 *
 * - Влязъл читател с история: препоръките от [ReadingRecommender] (наличните първи,
 *   без прочетеното), допълнени от общия подбор, ако са малко.
 * - Всички останали: „Най-четени и нови“ ([popularAndNew]) — изчислява се веднъж за
 *   каталога заедно с обобщението за началния екран, не при всяко показване.
 *
 * Сигнал за „най-четени“ в публичния каталог няма (заеманията не се публикуват), затова
 * се ползва това, което библиотеката сама е решила: заглавия, купени в няколко
 * екземпляра. Новите са наличните постъпления след тези в реда „Нови книги“.
 */
object ForYouPicks {
    /** Най-много книги на полицата. */
    const val LIMIT = 12

    /** Под толкова лични препоръки полицата се допълва от общия подбор. */
    const val MIN_PERSONAL = 6

    /** Колко от най-новите постъпления изобщо се разглеждат (за разнообразие по раздели). */
    private const val NEW_WINDOW = 120

    /**
     * „Най-четени и нови“: редуват се заглавие с повече екземпляри и ново постъпление,
     * само налични, по едно от заглавие и от различни раздели на УДК (докато има такива).
     * [skip] — книгите, които вече са на началния екран (реда „Нови книги“).
     */
    fun popularAndNew(books: List<CatalogBook>, skip: Set<Long> = emptySet(), limit: Int = LIMIT): List<CatalogBook> {
        if (books.isEmpty() || limit <= 0) return emptyList()
        // Заглавие → всичките му екземпляри (еднакво заглавие + автор).
        val byTitle = LinkedHashMap<String, MutableList<CatalogBook>>()
        for (b in books) byTitle.getOrPut(ReadingRecommender.titleKey(b.title, b.author)) { ArrayList(2) } += b
        val popular = byTitle.values.asSequence()
            .filter { it.size >= 2 }
            .mapNotNull { copies -> copies.firstOrNull { it.available && it.inv !in skip }?.let { it to copies.size } }
            .sortedWith(compareByDescending<Pair<CatalogBook, Int>> { it.second }.thenByDescending { it.first.registeredOn })
            .map { it.first }
            .toList()
        val fresh = books.asSequence()
            .filter { it.available && it.registeredOn.isNotBlank() && it.inv !in skip }
            .sortedWith(compareByDescending<CatalogBook> { it.registeredOn }.thenByDescending { it.inv })
            .take(NEW_WINDOW)
            .toList()
        return interleave(varied(popular), varied(fresh), limit)
    }

    /**
     * Полицата за влязъл читател: [recommended] (наличните първи), без [exclude];
     * ако са по-малко от [MIN_PERSONAL] — допълва се от [fallback].
     */
    fun personal(recommended: List<CatalogBook>, fallback: List<CatalogBook>, exclude: Set<Long>, limit: Int = LIMIT): List<CatalogBook> {
        val seen = HashSet<String>()
        val out = ArrayList<CatalogBook>(limit)
        fun add(b: CatalogBook) {
            if (out.size >= limit || b.inv in exclude) return
            if (seen.add(ReadingRecommender.titleKey(b.title, b.author))) out += b
        }
        recommended.sortedBy { !it.available }.forEach(::add)
        if (out.size < MIN_PERSONAL) fallback.forEach(::add)
        return out
    }

    /**
     * Общият подбор без [exclude] (напр. вече прочетеното или показаното на екрана); ако
     * обобщението е от по-стара версия (без подбор) — книгите от рафтовете на библиотеката.
     */
    fun general(picks: List<CatalogBook>?, shelves: List<List<CatalogBook>>, exclude: Set<Long>, limit: Int = LIMIT): List<CatalogBook> {
        val source = picks ?: varied(shelves.flatten().filter { it.available })
        val seen = HashSet<String>()
        return source.asSequence()
            .filter { it.inv !in exclude && seen.add(ReadingRecommender.titleKey(it.title, it.author)) }
            .take(limit)
            .toList()
    }

    /**
     * Разбърква по раздели на УДК: по една книга от всеки раздел наред (в реда на първата
     * им поява), после втората и т.н.; редът вътре в раздела се запазва. По едно заглавие.
     */
    internal fun varied(books: List<CatalogBook>): List<CatalogBook> {
        val seen = HashSet<String>()
        val groups = LinkedHashMap<Int, ArrayDeque<CatalogBook>>()
        for (b in books) {
            if (!seen.add(ReadingRecommender.titleKey(b.title, b.author))) continue
            groups.getOrPut(b.udcSection ?: -1) { ArrayDeque() }.addLast(b)
        }
        val out = ArrayList<CatalogBook>(seen.size)
        while (groups.isNotEmpty()) {
            val iter = groups.entries.iterator()
            while (iter.hasNext()) {
                val q = iter.next().value
                out += q.removeFirst()
                if (q.isEmpty()) iter.remove()
            }
        }
        return out
    }

    /** Редува двата списъка (първо [a]), без повторение на заглавие или екземпляр. */
    private fun interleave(a: List<CatalogBook>, b: List<CatalogBook>, limit: Int): List<CatalogBook> {
        val seen = HashSet<String>()
        val out = ArrayList<CatalogBook>(limit)
        var i = 0
        var j = 0
        while (out.size < limit && (i < a.size || j < b.size)) {
            val next = when {
                i < a.size && (j >= b.size || out.size % 2 == 0) -> a[i++]
                else -> b[j++]
            }
            if (seen.add(ReadingRecommender.titleKey(next.title, next.author))) out += next
        }
        return out
    }
}
