package org.chyavorec.core

/**
 * Оставащо време за изчакване (напр. временно заключен вход), закръглено така,
 * както човек би го казал: под минута — в секунди; до час — в минути (до най-близката,
 * защото броячът на заключването закръгля секундите нагоре); от час нагоре — в часове,
 * с „около“, ако не е точно.
 */
data class WaitDuration(val amount: Int, val unit: Unit, val approximate: Boolean = false) {
    enum class Unit { SECONDS, MINUTES, HOURS }

    companion object {
        fun of(seconds: Long): WaitDuration {
            val s = seconds.coerceAtLeast(0)
            return when {
                s < 60 -> WaitDuration(s.toInt(), Unit.SECONDS)
                s < 3600 -> WaitDuration(((s + 30) / 60).toInt().coerceIn(1, 59), Unit.MINUTES)
                else -> {
                    val hours = ((s + 1800) / 3600).coerceIn(1, Int.MAX_VALUE.toLong()).toInt()
                    WaitDuration(hours, Unit.HOURS, approximate = s % 3600 != 0L)
                }
            }
        }
    }
}
