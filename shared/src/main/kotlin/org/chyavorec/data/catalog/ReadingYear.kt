package org.chyavorec.data.catalog

import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId

/**
 * „Моята читателска година“: обобщение на една календарна година от историята на четенето —
 * за историята на цял екран (като „Wrapped“). Изцяло на телефона, от същите данни като
 * [ReadingStats]: датата е тази на заемане (ако липсва — на връщане), по българско време.
 */
object ReadingYear {

    /** Читателският тип — само от това, което данните наистина показват. */
    enum class Archetype {
        /** Много книги (средно поне две на месец). */
        BOOKWORM,

        /** Верен на един автор — поне 3 книги и поне 30 % от годината. */
        LOYAL,

        /** Обиколил е поне 4 раздела на библиотеката. */
        EXPLORER,

        /** Чете постоянно — поне 6 месеца подред. */
        STEADY,

        /** Всички останали — внимателен, любопитен читател. */
        CURIOUS,
    }

    data class Summary(
        val year: Int,
        /** Книгите, взети през годината. */
        val books: Int,
        /** Точно 12 бройки — януари първи. */
        val months: List<Int>,
        /** Най-честият автор (поне 2 книги), иначе `null`. */
        val favouriteAuthor: String?,
        val favouriteAuthorCount: Int,
        /** Колко различни автори. */
        val authors: Int,
        /** Познатите раздели по УДК (без „без раздел“), най-четените първи. */
        val sections: List<ReadingStats.SectionCount>,
        /** Най-дългата поредица от месеци с поне една книга (в рамките на годината). */
        val longestStreak: Int,
        val archetype: Archetype,
    ) {
        /** Месецът с най-много книги (при равенство — по-късният); `null` — няма книги. */
        val peakMonth: Month? get() {
            val top = months.max()
            if (top <= 0) return null
            return Month.of(months.lastIndexOf(top) + 1)
        }

        val peakCount: Int get() = months.max()

        /** Месеците с поне една книга. */
        val activeMonths: Int get() = months.count { it > 0 }

        /** Най-четеният раздел (`null` — каталогът не познава нито една книга). */
        val topSection: ReadingStats.SectionCount? get() = sections.firstOrNull()

        /** Индексите (0–11) на месеците в най-дългата поредица (първата, ако са няколко). */
        val streakMonths: IntRange get() {
            var run = 0
            for (i in months.indices) {
                run = if (months[i] > 0) run + 1 else 0
                if (run == longestStreak && run > 0) return (i - run + 1)..i
            }
            return IntRange.EMPTY
        }
    }

    /**
     * Годината [year] от [history]; [books] — каталожният запис по `loanId`
     * ([ReadingStats.resolveBooks]). `null` — през тази година няма взети книги.
     */
    fun compute(history: List<HistoryItem>, books: Map<String, CatalogBook>, year: Int, zone: ZoneId = ReadingStats.SOFIA): Summary? {
        val items = history.distinctBy { it.loanId }.filter { ReadingStats.monthOf(it.dateOut ?: it.dateIn, zone)?.year == year }
        if (items.isEmpty()) return null
        val months = IntArray(12)
        for (h in items) ReadingStats.monthOf(h.dateOut ?: h.dateIn, zone)?.let { months[it.monthValue - 1]++ }

        val bySection = HashMap<Int, Int>()
        for (h in items) books[h.loanId]?.udcSection?.let { bySection[it] = (bySection[it] ?: 0) + 1 }
        val sections = bySection.map { (s, n) -> ReadingStats.SectionCount(s, n) }
            .sortedWith(compareByDescending<ReadingStats.SectionCount> { it.count }.thenBy { it.section })

        val authorKeys = HashSet<String>()
        for (h in items) {
            val names = books[h.loanId]?.authors?.takeIf { it.isNotEmpty() } ?: listOf(h.author)
            names.mapNotNullTo(authorKeys) { ReadingRecommender.authorKey(it) }
        }
        val streak = ReadingStats.longestStreak((1..12).filter { months[it - 1] > 0 }.map { YearMonth.of(year, it) })
        val (author, authorCount) = ReadingStats.favouriteAuthor(items, books)
        val favourite = author?.takeIf { authorCount >= 2 }
        val n = items.size
        return Summary(
            year = year,
            books = n,
            months = months.toList(),
            favouriteAuthor = favourite,
            favouriteAuthorCount = if (favourite != null) authorCount else 0,
            authors = authorKeys.size,
            sections = sections,
            longestStreak = streak,
            archetype = archetype(n, if (favourite != null) authorCount else 0, sections.size, streak),
        )
    }

    /** Типът по реда на „силата“ си: много книги, верност, разнообразие, постоянство. */
    internal fun archetype(books: Int, favouriteAuthorCount: Int, sections: Int, streak: Int): Archetype = when {
        books >= 24 -> Archetype.BOOKWORM
        favouriteAuthorCount >= 3 && favouriteAuthorCount * 10 >= books * 3 -> Archetype.LOYAL
        sections >= 4 -> Archetype.EXPLORER
        streak >= 6 -> Archetype.STEADY
        else -> Archetype.CURIOUS
    }

    /** Годините, които се предлагат на [today]: текущата, а през януари — и миналата (първа). */
    fun offeredYears(today: LocalDate): List<Int> =
        if (today.month == Month.JANUARY) listOf(today.year - 1, today.year) else listOf(today.year)

    /** Декември и януари — времето за равносметка: картата в „Моето“ е голяма. */
    fun isSeason(today: LocalDate): Boolean = today.month == Month.DECEMBER || today.month == Month.JANUARY
}
