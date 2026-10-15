package org.chyavorec.data.catalog

import kotlinx.coroutines.CancellationException
import org.chyavorec.core.TextNormalizer
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.SearchField
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneId

/**
 * „Моето четене“: обобщение на историята на четенето за графиките в „Моето“ — изцяло на
 * телефона, от кешираната история и публичния каталог (нищо не се изпраща).
 *
 * - Книги по месеци за последните 12 месеца (по българско време; датата е тази на заемане,
 *   а ако липсва — на връщане).
 * - Раздели по УДК — от каталожния запис на всяка книга ([resolveBooks]).
 * - Общо за годината, любим автор и най-дългата поредица от месеци с поне една книга.
 */
object ReadingStats {
    /** Сроковете и датите в библиотеката са по българско време. */
    val SOFIA: ZoneId = ZoneId.of("Europe/Sofia")

    /** Колко месеца показва графиката (с текущия). */
    const val MONTHS = 12

    data class MonthCount(val month: YearMonth, val count: Int)

    /** Раздел по УДК (0–9); `null` — книгата не е в каталога или е без УДК. */
    data class SectionCount(val section: Int?, val count: Int)

    data class Summary(
        /** Точно [MONTHS] месеца — най-старият първи, последният е текущият. */
        val months: List<MonthCount>,
        /** Най-четените раздели първи; без раздел — накрая. */
        val sections: List<SectionCount>,
        /** Книги, взети през текущата година. */
        val thisYear: Int,
        /** Най-често срещаният автор (`null` — няма достатъчно данни). */
        val favouriteAuthor: String?,
        val favouriteAuthorCount: Int,
        /** Най-дългата поредица от последователни месеци с поне една книга. */
        val longestStreak: Int,
        /** Всички записи в историята. */
        val total: Int,
    ) {
        /** Книгите за последните [MONTHS] месеца. */
        val lastMonthsTotal: Int get() = months.sumOf { it.count }

        /** Месецът с най-много книги (при равенство — по-скорошният); `null` — няма книги. */
        val peakMonth: MonthCount? get() = months.filter { it.count > 0 }.maxWithOrNull(compareBy<MonthCount> { it.count }.thenBy { it.month })
    }

    /**
     * Обобщението на [history] към [today]; [books] — каталожният запис по `loanId`
     * ([resolveBooks]). `null` — празна история (тогава картата не се показва).
     */
    fun compute(history: List<HistoryItem>, books: Map<String, CatalogBook>, today: LocalDate, zone: ZoneId = SOFIA): Summary? {
        val items = history.distinctBy { it.loanId }
        if (items.isEmpty()) return null
        val current = YearMonth.from(today)
        val first = current.minusMonths((MONTHS - 1).toLong())
        val perMonth = HashMap<YearMonth, Int>()
        for (h in items) monthOf(h.dateOut ?: h.dateIn, zone)?.let { perMonth[it] = (perMonth[it] ?: 0) + 1 }
        val months = (0 until MONTHS).map { i -> first.plusMonths(i.toLong()).let { m -> MonthCount(m, perMonth[m] ?: 0) } }
        val thisYear = perMonth.entries.sumOf { (m, n) -> if (m.year == today.year && m <= current) n else 0 }

        val bySection = HashMap<Int?, Int>()
        for (h in items) {
            val s = books[h.loanId]?.udcSection
            bySection[s] = (bySection[s] ?: 0) + 1
        }
        val sections = bySection.map { (s, n) -> SectionCount(s, n) }
            .sortedWith(compareBy<SectionCount> { it.section == null }.thenByDescending { it.count }.thenBy { it.section })

        val (author, authorCount) = favouriteAuthor(items, books)
        return Summary(
            months = months,
            sections = sections,
            thisYear = thisYear,
            favouriteAuthor = author,
            favouriteAuthorCount = authorCount,
            longestStreak = longestStreak(perMonth.keys),
            total = items.size,
        )
    }

    /**
     * Месецът на ISO дата или момент: „2026-03-31“ е март; „2026-03-31T22:30:00Z“ е вече
     * април в София. Неразчетена дата — `null`.
     */
    fun monthOf(iso: String?, zone: ZoneId = SOFIA): YearMonth? {
        val s = iso?.trim().orEmpty()
        if (s.length < 7) return null
        return runCatching { YearMonth.from(LocalDate.parse(s)) }.getOrNull()
            ?: runCatching { YearMonth.from(OffsetDateTime.parse(s).atZoneSameInstant(zone)) }.getOrNull()
            ?: runCatching { YearMonth.from(Instant.parse(s).atZone(zone)) }.getOrNull()
            ?: runCatching { YearMonth.from(LocalDateTime.parse(s)) }.getOrNull()
            ?: runCatching { YearMonth.parse(s.take(7)) }.getOrNull()
    }

    /** Най-дългата поредица от последователни месеци (без празен месец помежду). */
    internal fun longestStreak(months: Collection<YearMonth>): Int {
        var best = 0
        var run = 0
        var prev: YearMonth? = null
        for (m in months.toSortedSet()) {
            run = if (prev != null && prev.plusMonths(1) == m) run + 1 else 1
            if (run > best) best = run
            prev = m
        }
        return best
    }

    /** Най-честият автор (от каталога, иначе от историята); при равенство — по-скоро четеният. */
    private fun favouriteAuthor(items: List<HistoryItem>, books: Map<String, CatalogBook>): Pair<String?, Int> {
        class Tally(val name: String, var count: Int, var last: String)
        val tally = LinkedHashMap<String, Tally>()
        for (h in items) {
            val names = books[h.loanId]?.authors?.takeIf { it.isNotEmpty() } ?: listOf(h.author)
            val date = h.dateOut ?: h.dateIn ?: ""
            for (n in names) {
                val key = ReadingRecommender.authorKey(n) ?: continue
                val t = tally.getOrPut(key) { Tally(n.trim(), 0, date) }
                t.count++
                if (date > t.last) t.last = date
            }
        }
        val top = tally.values.maxWithOrNull(compareBy<Tally> { it.count }.thenBy { it.last }) ?: return null to 0
        return top.name to top.count
    }

    /**
     * Каталожният запис на всяка книга от историята (по `loanId`): по инвентарен номер,
     * а без такъв (или ако екземплярът вече не е в каталога) — по заглавие и автор.
     * Търсенията по заглавие са най-много [maxTitleLookups] (различни заглавия).
     */
    suspend fun resolveBooks(searcher: CatalogSearcher, history: List<HistoryItem>, maxTitleLookups: Int = 40): Map<String, CatalogBook> {
        val byInv = searcher.books(history.mapNotNull { it.inv }.distinct())
        val out = HashMap<String, CatalogBook>(history.size)
        val byTitle = HashMap<String, CatalogBook?>()
        for (h in history) {
            val direct = h.inv?.let(byInv::get)
            if (direct != null) {
                out[h.loanId] = direct
                continue
            }
            if (h.title.isBlank()) continue
            val key = ReadingRecommender.titleKey(h.title, h.author)
            val found = if (key in byTitle) {
                byTitle[key]
            } else {
                if (byTitle.size >= maxTitleLookups) continue
                lookupByTitle(searcher, h).also { byTitle[key] = it }
            }
            if (found != null) out[h.loanId] = found
        }
        return out
    }

    /** Същото заглавие и автор; ако няма — същото заглавие (авторът — ако е известен — също съвпада). */
    private suspend fun lookupByTitle(searcher: CatalogSearcher, h: HistoryItem): CatalogBook? = try {
        searcher.copiesOf(CatalogBook(inv = -1, author = h.author, title = h.title)).firstOrNull()
            ?: run {
                val title = TextNormalizer.normalize(h.title)
                val author = ReadingRecommender.authorKey(h.author)
                searcher.search(CatalogQuery(text = h.title, field = SearchField.TITLE)).firstOrNull { b ->
                    TextNormalizer.normalize(b.title) == title &&
                        (author == null || b.authors.any { ReadingRecommender.authorKey(it) == author })
                }
            }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        null
    }
}
