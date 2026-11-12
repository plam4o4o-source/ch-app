package org.chyavorec.domain.model

import java.time.LocalDate

/**
 * Печатът „Върната“ — кога една книга наистина се празнува (чисти изчисления, без Android).
 *
 * Пази се само това, което не издава какво се чете: идентификаторите на заеманията.
 * Заглавието и корицата идват от историята на четенето ([HistoryItem]) в момента на показване.
 *
 * - [State.known] — заеманията, видени при последното пълно опресняване на „Моите книги“
 *   (`null` — още не са записвани: първото отваряне само запомня, не празнува);
 * - [State.pending] — изчезнали от заеманията, но още непотвърдени в историята (опашката);
 * - [State.done] — вече отпразнуваните (ограничено по брой и възраст, без повторение).
 *
 * Празнува се САМО когато (1) заемането е изчезнало от пресен успешен списък, (2) историята
 * казва, че е върнато ([HistoryItem.dateIn]) и (3) в списъка няма същата книга (удължаване или
 * ново заемане на същия екземпляр не е връщане). Неуспешно теглене на списъка или историята
 * не променя нищо — опашката чака следващото отваряне.
 */
object ReturnStamps {
    /** Най-много книги за едно отваряне — останалите чакат следващото. */
    const val MAX_PER_OPENING = 3
    private const val MAX_KNOWN = 60
    private const val MAX_PENDING = 30
    private const val MAX_DONE = 100
    /** Колко дни чака непотвърдено (забавена история) изчезнало заемане. */
    const val PENDING_DAYS = 21L
    /** По-старо връщане се пропуска мълчаливо — печат за книга от преди месец е странен. */
    const val RECENT_DAYS = 30L
    private const val DONE_KEEP_DAYS = 120L
    private const val SEP = '|'

    data class State(
        val known: Set<String>? = null,
        /** loanId → ден (epochDay), в който за пръв път е липсвало. */
        val pending: Map<String, Long> = emptyMap(),
        /** loanId → ден (epochDay) на празнуването. */
        val done: Map<String, Long> = emptyMap(),
    )

    data class Result(val state: State, val celebrate: List<HistoryItem>)

    /**
     * Една стъпка при пресен успешен списък [fresh] (`null` — теглене без успех: нищо не се променя)
     * и история [history] (`null` — недостъпна/неуспешна: само опашката расте).
     */
    fun step(state: State, fresh: List<Loan>?, history: List<HistoryItem>?, today: LocalDate): Result {
        if (fresh == null) return Result(state, emptyList())
        val day = today.toEpochDay()
        val freshIds = fresh.mapTo(linkedSetOf()) { it.loanId }

        // 1. Новоизчезналите влизат в опашката; върналите се в списъка (същото id) излизат от нея.
        val pending = linkedMapOf<String, Long>()
        for ((id, since) in state.pending) if (id !in freshIds && day - since <= PENDING_DAYS) pending[id] = since
        state.known?.forEach { id -> if (id !in freshIds && id !in state.done && id !in pending) pending[id] = day }
        while (pending.size > MAX_PENDING) pending.remove(pending.keys.first())
        val known = freshIds.take(MAX_KNOWN).toSet()

        // 2. Потвърждение от историята.
        val ready = mutableListOf<HistoryItem>()
        if (history != null) {
            val byId = history.distinctBy { it.loanId }.associateBy { it.loanId }
            val iterator = pending.keys.iterator()
            while (iterator.hasNext()) {
                val item = byId[iterator.next()] ?: continue
                val back = parse(item.dateIn) ?: continue
                // Същата книга още е заета (удължаване с ново id / ново заемане) — не е връщане.
                if (fresh.any { sameBook(it, item) }) { iterator.remove(); continue }
                // Старо връщане — без печат, но и без повече чакане.
                if (day - back.toEpochDay() > RECENT_DAYS) { iterator.remove(); continue }
                ready += item
            }
        }
        // Най-старото връщане първо; първите няколко се празнуват, останалите чакат следващото отваряне.
        val celebrate = ready.sortedWith(compareBy({ it.dateIn }, { it.loanId })).take(MAX_PER_OPENING)
        celebrate.forEach { pending.remove(it.loanId) }

        val done = linkedMapOf<String, Long>()
        for ((id, at) in state.done) if (day - at <= DONE_KEEP_DAYS) done[id] = at
        celebrate.forEach { done[it.loanId] = day }
        while (done.size > MAX_DONE) done.remove(done.entries.minByOrNull { it.value }!!.key)
        return Result(State(known, pending, done), celebrate)
    }

    /** Броят на върнатите през [year] книги (по една на заемане) — числото на одометъра. */
    fun returnedInYear(history: List<HistoryItem>, year: Int): Int =
        history.distinctBy { it.loanId }.count { parse(it.dateIn)?.year == year }

    private fun sameBook(loan: Loan, item: HistoryItem): Boolean {
        val inv = item.inv
        if (inv != null && inv != 0L && loan.inv != null) return loan.inv == inv
        return loan.title.isNotBlank() && loan.title == item.title && loan.author == item.author
    }

    private fun parse(date: String?): LocalDate? = date?.take(10)?.let { runCatching { LocalDate.parse(it) }.getOrNull() }

    // --- запис в настройките: „loanId|ден“ (loanId може да съдържа разделителя) ---

    fun encode(map: Map<String, Long>): Set<String> = map.mapTo(linkedSetOf()) { (id, day) -> "$id$SEP$day" }

    fun decode(entries: Set<String>?): Map<String, Long> {
        val out = linkedMapOf<String, Long>()
        for (s in entries.orEmpty()) {
            val cut = s.lastIndexOf(SEP)
            val day = s.substring(cut + 1).toLongOrNull()
            if (cut > 0 && day != null) out[s.substring(0, cut)] = day
        }
        return out
    }
}
