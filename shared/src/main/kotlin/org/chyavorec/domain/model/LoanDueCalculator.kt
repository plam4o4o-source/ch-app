package org.chyavorec.domain.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Изчислява индикатора на срока за връщане само от реалните дати.
 * [dueSoonDays] — колко дни преди срока заемането става „наближава“.
 */
class LoanDueCalculator(private val dueSoonDays: Int = 3) {

    fun status(dueOn: LocalDate, today: LocalDate): DueStatus {
        val days = daysLeft(dueOn, today)
        return when {
            days < 0 -> DueStatus.OVERDUE
            days <= dueSoonDays -> DueStatus.DUE_SOON
            else -> DueStatus.PLENTY_OF_TIME
        }
    }

    fun status(loan: Loan, today: LocalDate): DueStatus? =
        parse(loan.dueOn)?.let { status(it, today) }

    /**
     * Статус за напомняне (известие): като [status], но `null` при заявено и още
     * непотвърдено удължаване — срокът вероятно ще се промени, не бива да се
     * досажда с „наближава срок“, докато библиотеката не отговори.
     */
    fun reminderStatus(loan: Loan, today: LocalDate): DueStatus? =
        if (loan.renewPending) null else status(loan, today)

    /** Отрицателно число = дни просрочие. */
    fun daysLeft(dueOn: LocalDate, today: LocalDate): Long = ChronoUnit.DAYS.between(today, dueOn)

    /** Дял от срока, който вече е изминал (0..1) — за прогрес индикатора. */
    fun elapsedFraction(borrowedOn: LocalDate, dueOn: LocalDate, today: LocalDate): Float {
        val total = ChronoUnit.DAYS.between(borrowedOn, dueOn).coerceAtLeast(1)
        val elapsed = ChronoUnit.DAYS.between(borrowedOn, today)
        return (elapsed.toFloat() / total).coerceIn(0f, 1f)
    }

    companion object {
        fun parse(iso: String?): LocalDate? = iso?.let { runCatching { LocalDate.parse(it.take(10)) }.getOrNull() }
    }
}

/** Статус на членството по реалната дата на валидност. */
object MembershipEvaluator {
    fun effectiveStatus(membership: Membership, today: LocalDate): MembershipStatus {
        if (membership.status == MembershipStatus.SUSPENDED) return MembershipStatus.SUSPENDED
        val until = LoanDueCalculator.parse(membership.validUntil) ?: return membership.status
        return if (until.isBefore(today)) MembershipStatus.EXPIRED else MembershipStatus.ACTIVE
    }
}
