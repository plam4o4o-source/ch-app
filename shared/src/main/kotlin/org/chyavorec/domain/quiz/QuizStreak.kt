package org.chyavorec.domain.quiz

import kotlinx.serialization.Serializable
import java.time.LocalDate

/**
 * Редицата от дни с довършена викторина — пази се само на телефона (две числа и една дата).
 * Чисти функции: [record] при довършване, [current] за показване.
 */
@Serializable
data class QuizStreak(
    /** Последният довършен ден (ISO) или `null`. */
    val lastDay: String? = null,
    /** Дължината на редицата към [lastDay]. */
    val length: Int = 0,
    /** Най-дългата редица. */
    val best: Int = 0,
    /** Колко викторини са довършени общо (ограничено — [MAX]). */
    val played: Int = 0,
) {
    /** Дължината за показване днес: редица, прекъсната преди вчера, е 0. */
    fun current(today: LocalDate): Int {
        val last = lastDay?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: return 0
        return if (!last.isBefore(today.minusDays(1)) && !last.isAfter(today)) length else 0
    }

    /** Довършена викторина на [day]: същия ден — без промяна; следващия — +1; иначе — отначало. */
    fun record(day: LocalDate): QuizStreak {
        val last = lastDay?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        if (last == day) return this
        val next = if (last != null && last.plusDays(1) == day) (length + 1).coerceAtMost(MAX) else 1
        return QuizStreak(day.toString(), next, maxOf(best, next).coerceAtMost(MAX), (played + 1).coerceAtMost(MAX))
    }

    companion object {
        /** Горна граница на всички числа — записът остава малък и безопасен при повреден файл. */
        const val MAX = 9_999
    }
}
