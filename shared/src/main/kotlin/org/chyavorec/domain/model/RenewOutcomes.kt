package org.chyavorec.domain.model

/**
 * Отговорът на библиотеката на заявка за удължаване — чисти изчисления, без Android.
 *
 * Удължаването е асинхронно: приложението праща заявка (`renewPending`), а InvLib я
 * изпълнява или отказва при следващата си синхронизация. За известието се помнят
 * чакащите заемания („наблюдавани“) като записи „loanId|срок при заявката“. Когато
 * наблюдавано заемане вече не чака:
 * - `renewResult` = отказано → [Kind.REJECTED];
 * - срокът е друг (с или без `renewResult` = done) → [Kind.APPROVED];
 * - done, но срокът още е старият (снимката на InvLib още не е дошла) → чака се новият срок;
 * - иначе (заявката е изтекла, книгата е върната) → забравя се без известие.
 *
 * Наблюдаваните = `null` — неизвестни (първо пускане, нов вход): само се запомнят чакащите,
 * без известия за стари отговори. Показаните ключове ([Outcome.key]) пазят от двойно известие
 * (напр. фоновата задача и екранът едновременно); пазят се само за заеманията в списъка.
 */
object RenewOutcomes {
    private const val SEP = '|'

    enum class Kind { APPROVED, REJECTED }

    /** Известие за [loan]; [key] — за еднократността. */
    data class Outcome(val loan: Loan, val kind: Kind, val key: String)

    /** Какво да се покаже и новото състояние за запис. */
    data class Result(val outcomes: List<Outcome>, val watch: Set<String>, val notified: Set<String>)

    fun watchEntry(loanId: String, dueOn: String?): String = loanId + SEP + dueOn.orEmpty()

    /** loanId може да съдържа разделителя — срокът е след последния. */
    private fun parse(entry: String): Pair<String, String>? {
        val i = entry.lastIndexOf(SEP)
        if (i <= 0) return null
        return entry.substring(0, i) to entry.substring(i + 1)
    }

    /**
     * @param watch запомнените чакащи заемания (`null` — неизвестни).
     * @param notified вече показаните ключове.
     * @param defer true — известията се отлагат (тихите часове): отговорите остават наблюдавани
     *   и ще излязат при следващата проверка.
     */
    fun diff(watch: Set<String>?, notified: Set<String>, loans: List<Loan>, defer: Boolean = false): Result {
        val previous = watch.orEmpty().mapNotNull(::parse).toMap()
        val unique = loans.distinctBy { it.loanId }
        val nextWatch = linkedSetOf<String>()
        val outcomes = mutableListOf<Outcome>()
        for (loan in unique) {
            val dueAtRequest = previous[loan.loanId]
            if (loan.renewPending) {
                // Срокът при заявката се пази — по него се познава удълженият.
                nextWatch += watchEntry(loan.loanId, dueAtRequest ?: loan.dueOn)
                continue
            }
            if (watch == null || dueAtRequest == null) continue
            val result = loan.renewResult
            val newDue = loan.dueOn?.takeIf { it.isNotBlank() && it != dueAtRequest }
            val outcome = when {
                result?.isRejected == true -> Outcome(loan, Kind.REJECTED, key(loan.loanId, RenewResult.STATUS_REJECTED, result.at))
                newDue != null -> Outcome(loan, Kind.APPROVED, key(loan.loanId, RenewResult.STATUS_DONE, newDue))
                result?.isDone == true -> {
                    // Изпълнено, но новият срок още не е в данните — следващия път.
                    nextWatch += watchEntry(loan.loanId, dueAtRequest)
                    null
                }
                else -> null
            } ?: continue
            when {
                defer -> nextWatch += watchEntry(loan.loanId, dueAtRequest)
                outcome.key !in notified -> outcomes += outcome
            }
        }
        val ids = unique.map { it.loanId }
        val kept = notified.filterTo(linkedSetOf()) { k -> ids.any { k.startsWith(it + SEP) } }
        return Result(outcomes, nextWatch, kept + outcomes.map { it.key })
    }

    private fun key(loanId: String, status: String, detail: String?): String = listOf(loanId, status, detail.orEmpty()).joinToString(SEP.toString())
}
