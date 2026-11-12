package org.chyavorec.domain.model

import org.chyavorec.data.catalog.ReadingRecommender
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Запис в „Читателския дневник“: лична оценка (1–5 розетки) и по желание бележка на ред за
 * прочетена книга. Само на телефона. [rating] 0 — читателят е пропуснал въпроса („Не сега“): повече
 * не се пита за тази книга, но оценка няма. Самата книга е по [bookKey] (заглавие + автор, без
 * препинателни знаци — екземплярите на едно заглавие споделят оценка); [inv] е последният видян
 * инвентарен номер (за връзка със страницата на книгата).
 */
data class DiaryEntry(
    val bookKey: String,
    val title: String,
    val author: String,
    val inv: Long?,
    val rating: Int,
    val note: String,
    /** Годината на заемане на книгата (от историята) — за „любимата книга на годината“. */
    val readYear: Int?,
    val ratedAtMillis: Long,
) {
    val rated: Boolean get() = rating in 1..ReadingDiary.MAX_RATING
}

/** Чистите правила на дневника: ограничения, тежести за препоръките, любима книга и текст за споделяне. */
object ReadingDiary {
    const val MAX_RATING = 5

    /** Бележката е най-много толкова знака (един ред). */
    const val MAX_NOTE = 140

    /** Оценка от [LIKED] нагоре е „хареса ми“, до [DISLIKED] — „не ми хареса“. */
    const val LIKED = 4
    const val DISLIKED = 2

    fun bookKey(title: String, author: String): String = ReadingRecommender.titleKey(title, author)

    fun clampRating(rating: Int): Int = rating.coerceIn(0, MAX_RATING)

    /** Един ред, без излишни интервали, до [MAX_NOTE] знака. */
    fun cleanNote(raw: String): String = raw.replace(Regex("\\s+"), " ").trim().take(MAX_NOTE)

    /** Оценките на книгите (ключ → 1–5); пропуснатите (0) не са оценка. */
    fun ratings(entries: Collection<DiaryEntry>): Map<String, Int> =
        entries.filter { it.rated }.associate { it.bookKey to it.rating }

    /**
     * Тежестта на една прочетена книга в препоръките ([ReadingRecommender]): неоценена — 1 (както
     * досега); 5 → 3, 4 → 2, 3 → 1, 2 → 0 (без принос), 1 → −1 (разделът и авторът се отдалечават).
     * Малко и детерминирано — оценките само изместват, не обръщат подбора.
     */
    fun weight(rating: Int?): Int = when (rating) {
        null -> 1
        5 -> 3
        4 -> 2
        3 -> 1
        2 -> 0
        1 -> -1
        else -> 1
    }

    /** Колко дни след връщането „Хареса ли ти?“ се предлага в историята (по-старите — само от страницата на книгата). */
    const val PROMPT_DAYS = 30L

    /** Най-много книги наведнъж с въпрос „Хареса ли ти?“ в историята — не повече, за да не досажда. */
    const val MAX_PROMPTS = 3

    /**
     * Записите от [history] (по `loanId`), при които в историята се показва въпросът „Хареса ли ти?“:
     * върната книга до [PROMPT_DAYS] дни назад, за която още НЕ е питано ([entries] няма запис — нито
     * оценка, нито „Не сега“), най-много [MAX_PROMPTS], най-скоро върнатите първи. Една книга
     * (ключ) — един въпрос, колкото и пъти да е заемана.
     */
    fun promptLoanIds(history: List<HistoryItem>, entries: Collection<DiaryEntry>, today: LocalDate): Set<String> {
        val asked = entries.mapTo(HashSet()) { it.bookKey }
        val seen = HashSet<String>()
        return history.asSequence()
            .mapNotNull { h -> returnedOn(h)?.let { h to it } }
            .filter { (_, d) -> !d.isAfter(today) && !d.isBefore(today.minusDays(PROMPT_DAYS)) }
            .sortedWith(compareByDescending<Pair<HistoryItem, LocalDate>> { it.second }.thenBy { it.first.loanId })
            .filter { (h, _) -> bookKey(h.title, h.author).let { it !in asked && seen.add(it) } }
            .take(MAX_PROMPTS)
            .mapTo(LinkedHashSet()) { it.first.loanId }
    }

    private fun returnedOn(h: HistoryItem): LocalDate? =
        h.dateIn?.takeIf { it.length >= 10 }?.let { runCatching { LocalDate.parse(it.take(10)) }.getOrNull() }

    /**
     * Любимата книга на [year] (за „Моята читателска година“): с най-висока оценка, не под [LIKED];
     * при равенство — по-скоро оценената, после по ключа (детерминирано). `null` — няма такава.
     */
    fun favorite(entries: Collection<DiaryEntry>, year: Int, zone: ZoneId = ReadingZone): DiaryEntry? =
        entries.filter { it.rating >= LIKED && yearOf(it, zone) == year }
            .maxWithOrNull(compareBy<DiaryEntry> { it.rating }.thenBy { it.ratedAtMillis }.thenBy { it.bookKey })

    private fun yearOf(e: DiaryEntry, zone: ZoneId): Int = e.readYear ?: Instant.ofEpochMilli(e.ratedAtMillis).atZone(zone).year

    private val ReadingZone: ZoneId = ZoneId.of("Europe/Sofia")

    /**
     * Текстът към картата „Препоръчай“: „<заглавие> — препоръчва читател на НЧ „Васил Левски – 1922““,
     * с оценката. Бележката се добавя САМО при [includeNote] (читателят я е включил) и ако има такава.
     */
    fun recommendation(title: String, rating: Int, note: String, includeNote: Boolean, bg: Boolean): String {
        val head = if (bg) "$title — препоръчва читател на НЧ „Васил Левски – 1922“"
        else "$title — recommended by a reader of the “Vasil Levski – 1922” Community Centre"
        val score = if (rating in 1..MAX_RATING) (if (bg) "Оценка: $rating от $MAX_RATING" else "Rating: $rating of $MAX_RATING") else null
        val quote = note.trim().takeIf { includeNote && it.isNotEmpty() }?.let { if (bg) "„$it“" else "“$it”" }
        return listOfNotNull(head, score, quote).joinToString("\n")
    }
}
