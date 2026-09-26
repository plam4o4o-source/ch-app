package org.chyavorec.core

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/** Абстракция на часовника — позволява детерминирани тестове на сроковете. */
interface AppClock {
    fun now(): Instant
    fun zone(): ZoneId
    fun today(): LocalDate = now().atZone(zone()).toLocalDate()
}

object SystemClock : AppClock {
    /** Сроковете в библиотеката се водят по българско време. */
    private val sofia: ZoneId = ZoneId.of("Europe/Sofia")
    override fun now(): Instant = Instant.now()
    override fun zone(): ZoneId = sofia
}

class FixedClock(private var instant: Instant, private val zoneId: ZoneId = ZoneId.of("Europe/Sofia")) : AppClock {
    override fun now(): Instant = instant
    override fun zone(): ZoneId = zoneId
    fun advanceSeconds(seconds: Long) { instant = instant.plusSeconds(seconds) }
}
