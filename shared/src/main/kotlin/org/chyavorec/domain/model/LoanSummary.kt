package org.chyavorec.domain.model

import java.time.LocalDate

/**
 * Кратко обобщение на заеманията — за картата „Следващ срок / Просрочено“ на „Начало“
 * и за плочката „Моите книги“. Изчислява се само от кешираните заемания (без мрежа).
 */
sealed interface LoanSummary {
    /** Всички заети книги (за „общо N“). */
    val total: Int

    /** Поне една книга е просрочена; [first] — най-отдавна просрочената. */
    data class Overdue(val count: Int, val first: Loan, val daysOverdue: Long, override val total: Int) : LoanSummary

    /** Нищо не е просрочено; [loan] е книгата с най-близкия срок. */
    data class NextDue(
        val loan: Loan,
        val dueOn: LocalDate,
        val daysLeft: Long,
        val status: DueStatus,
        override val total: Int,
    ) : LoanSummary

    /** Има заети книги, но сървърът не е върнал срок за нито една. */
    data class NoDueDate(override val total: Int) : LoanSummary

    companion object {
        /** `null` — няма заети книги (тогава картата не се показва). */
        fun of(loans: List<Loan>?, today: LocalDate, calc: LoanDueCalculator = LoanDueCalculator()): LoanSummary? {
            if (loans.isNullOrEmpty()) return null
            // При равни срокове — по заглавие, за да не „скача“ показаната книга.
            val dated = loans.mapNotNull { l -> LoanDueCalculator.parse(l.dueOn)?.let { l to it } }
                .sortedWith(compareBy<Pair<Loan, LocalDate>> { it.second }.thenBy { it.first.title })
            val overdue = dated.filter { calc.daysLeft(it.second, today) < 0 }
            if (overdue.isNotEmpty()) {
                val (loan, due) = overdue.first()
                return Overdue(overdue.size, loan, -calc.daysLeft(due, today), loans.size)
            }
            val (loan, due) = dated.firstOrNull() ?: return NoDueDate(loans.size)
            return NextDue(loan, due, calc.daysLeft(due, today), calc.status(due, today), loans.size)
        }
    }
}
