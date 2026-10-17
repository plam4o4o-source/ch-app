package org.chyavorec.domain.model

import org.chyavorec.core.Hashing
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/** Вид напомняне за срок на заета книга. */
enum class LoanReminderKind {
    /** [LoanReminderPlan.DAYS_BEFORE] дни преди срока. */
    BEFORE,
    /** В деня на срока. */
    DUE_DAY,
}

data class LoanReminder(val kind: LoanReminderKind, val at: Instant)

/**
 * Кога да се напомни за срок на заемане — чисти изчисления, без Android.
 *
 * - [DAYS_BEFORE] дни преди срока и в деня на срока, в [REMIND_AT] българско време;
 * - минал момент се пропуска; ако обаче денят на напомнянето е днес и вече е след
 *   [REMIND_AT], но преди [CATCH_UP_UNTIL] — известие [SOON] след сега;
 * - никога в тихите часове ([QUIET_FROM]–[QUIET_UNTIL]).
 */
object LoanReminderPlan {
    /** Сроковете в библиотеката се водят по българско време. */
    val ZONE: ZoneId = ZoneId.of("Europe/Sofia")
    const val DAYS_BEFORE = 2L
    val REMIND_AT: LocalTime = LocalTime.of(10, 0)
    val CATCH_UP_UNTIL: LocalTime = LocalTime.of(20, 0)
    val QUIET_FROM: LocalTime = LocalTime.of(21, 0)
    val QUIET_UNTIL: LocalTime = LocalTime.of(8, 0)
    val SOON: Duration = Duration.ofMinutes(1)

    /** Денят, в който се показва напомнянето от вида [kind]. */
    fun dayOf(kind: LoanReminderKind, dueOn: LocalDate): LocalDate = when (kind) {
        LoanReminderKind.BEFORE -> dueOn.minusDays(DAYS_BEFORE)
        LoanReminderKind.DUE_DAY -> dueOn
    }

    /** Всички напомняния, които още имат смисъл към [now] (най-много по едно от вид). */
    fun plan(dueOn: LocalDate, now: Instant, zone: ZoneId = ZONE): List<LoanReminder> =
        LoanReminderKind.entries.mapNotNull { kind -> at(kind, dueOn, now, zone)?.let { LoanReminder(kind, it) } }

    /** Моментът за [kind] или `null`, ако вече е късно (или би паднал в тихите часове). */
    fun at(kind: LoanReminderKind, dueOn: LocalDate, now: Instant, zone: ZoneId = ZONE): Instant? {
        val day = dayOf(kind, dueOn)
        val planned = day.atTime(REMIND_AT).atZone(zone).toInstant()
        val local = now.atZone(zone)
        val result = when {
            planned.isAfter(now) -> planned
            // Наваксване в същия ден (напр. книгата е заета/удължена след 10:00).
            local.toLocalDate() == day && local.toLocalTime() < CATCH_UP_UNTIL -> now.plus(SOON)
            else -> null
        }
        return result?.takeUnless { isQuiet(it, zone) }
    }

    /** 21:00–08:00 местно време — без известия. */
    fun isQuiet(instant: Instant, zone: ZoneId = ZONE): Boolean {
        val t = instant.atZone(zone).toLocalTime()
        return t >= QUIET_FROM || t < QUIET_UNTIL
    }

    /**
     * Проверка в момента на показване: WorkManager може да закъснее (Doze, рестарт),
     * затова известие само в деня на напомнянето и извън тихите часове.
     */
    fun deliverable(kind: LoanReminderKind, dueOn: LocalDate, now: Instant, zone: ZoneId = ZONE): Boolean =
        !isQuiet(now, zone) && now.atZone(zone).toLocalDate() == dayOf(kind, dueOn)
}

/**
 * Дневник на показаните известия за заемания — записи „loanId|срок|вид|ден на показване“.
 * Пази от повторно известие (напр. наваксването след рестарт) и от две известия
 * за една книга в един ден (напомняне + просрочие от фоновата синхронизация).
 */
object LoanNotificationLog {
    const val OVERDUE = "OVERDUE"
    private const val SEP = '|'
    /** По-стари записи се изтриват. */
    private const val KEEP_DAYS = 60L

    private data class Entry(val loanId: String, val dueOn: String, val kind: String, val sentOn: LocalDate)

    fun entry(loanId: String, dueOn: LocalDate, kind: String, sentOn: LocalDate): String =
        listOf(loanId, dueOn.toString(), kind, sentOn.toString()).joinToString(SEP.toString())

    /** loanId може да съдържа разделителя — останалите полета се четат отзад напред. */
    private fun parse(s: String): Entry? {
        val parts = s.split(SEP)
        if (parts.size < 4) return null
        val sentOn = runCatching { LocalDate.parse(parts.last()) }.getOrNull() ?: return null
        val n = parts.size
        return Entry(parts.subList(0, n - 3).joinToString(SEP.toString()), parts[n - 3], parts[n - 2], sentOn)
    }

    /** Това напомняне (за този срок) вече е показано. */
    fun wasSent(log: Set<String>, loanId: String, dueOn: LocalDate, kind: String): Boolean =
        log.any { s -> parse(s)?.let { it.loanId == loanId && it.dueOn == dueOn.toString() && it.kind == kind } == true }

    /** За тази книга днес вече има известие (от какъвто и да е вид). */
    fun sentOn(log: Set<String>, loanId: String, day: LocalDate): Boolean =
        log.any { s -> parse(s)?.let { it.loanId == loanId && it.sentOn == day } == true }

    /** [log] + [entry] без остарелите и повредените записи. */
    fun add(log: Set<String>, entry: String, today: LocalDate): Set<String> {
        val oldest = today.minusDays(KEEP_DAYS)
        return log.filterTo(mutableSetOf()) { s -> parse(s)?.sentOn?.isBefore(oldest) == false } + entry
    }
}

/**
 * Кога напомнянията за срок наистина трябва да се пренасрочат — чисти изчисления.
 *
 * Планът се помни като отпечатък по заемане: „loanId|хеш(срок, чакащо удължаване)“.
 * Пренасрочват се само новите и променените заемания, а изчезналите (върнати) се
 * отменят; при непроменен списък — нищо (без REPLACE, който би прекъснал вече
 * вървящо напомняне, и без работа при всеки старт на процеса).
 *
 * Запомненият план е:
 * - `null` — неизвестен (първо пускане на тази логика) → всички се планират отново;
 * - празен — нищо не е планирано (изход или изключени известия за срокове);
 *   при включване всички се планират отново.
 */
object LoanReminderSync {
    /** Сменя се при промяна на правилата за планиране ([LoanReminderPlan]) — тогава всичко се пренасрочва. */
    private const val FORMAT = 1
    private const val SEP = '|'

    /** Какво да се направи: да се (пре)планират [schedule] и да се отменят [cancel] (loanId). */
    data class Changes(val schedule: List<Loan>, val cancel: Set<String>) {
        val isEmpty: Boolean get() = schedule.isEmpty() && cancel.isEmpty()
    }

    /** Отпечатъкът на едно заемане — от него зависят моментите на напомнянията. */
    fun entry(loan: Loan): String {
        val plan = "$FORMAT$SEP${loan.dueOn.orEmpty()}$SEP${loan.renewPending}"
        return loan.loanId + SEP + Hashing.hex(Hashing.sha256(plan.toByteArray()), 6)
    }

    /** Отпечатъкът на целия план (записва се след успешно планиране). */
    fun entries(loans: List<Loan>): Set<String> = loans.distinctBy { it.loanId }.mapTo(linkedSetOf()) { entry(it) }

    /** loanId от запис (loanId може да съдържа разделителя — хешът е след последния). */
    private fun loanIdOf(entry: String): String? = entry.lastIndexOf(SEP).takeIf { it > 0 }?.let { entry.substring(0, it) }

    /** Разликата между запомнения план [previous] и текущите заемания [loans]. */
    fun diff(previous: Set<String>?, loans: List<Loan>): Changes {
        val unique = loans.distinctBy { it.loanId }
        if (previous == null) return Changes(unique, emptySet())
        val schedule = unique.filter { entry(it) !in previous }
        val current = unique.mapTo(hashSetOf()) { it.loanId }
        val cancel = previous.mapNotNullTo(linkedSetOf()) { loanIdOf(it) }.filterTo(linkedSetOf()) { it !in current }
        return Changes(schedule, cancel)
    }
}
