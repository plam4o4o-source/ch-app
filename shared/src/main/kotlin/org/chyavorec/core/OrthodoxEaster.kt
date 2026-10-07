package org.chyavorec.core

import java.time.LocalDate

/** Православен Великден по григорианския календар (алгоритъм на Меус за юлианския календар + отместване). */
object OrthodoxEaster {
    /**
     * Датата на Великден за [year] като григорианска дата. Юлианската дата идва по
     * Меус; разликата между календарите е 13 дни за 1900–2099 (12 преди, 14 след).
     */
    fun of(year: Int): LocalDate {
        val a = year % 4
        val b = year % 7
        val c = year % 19
        val d = (19 * c + 15) % 30
        val e = (2 * a + 4 * b - d + 34) % 7
        val month = (d + e + 114) / 31
        val day = (d + e + 114) % 31 + 1
        val julian = LocalDate.of(year, month, day)
        val offset = when {
            year < 1900 -> 12L
            year < 2100 -> 13L
            else -> 14L
        }
        return julian.plusDays(offset)
    }
}
