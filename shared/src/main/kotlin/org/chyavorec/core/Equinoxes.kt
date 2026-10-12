package org.chyavorec.core

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.PI
import kotlin.math.cos

/**
 * Равноденствия и слънцестоения (астрономическото начало на сезоните) по Меус,
 * „Astronomical Algorithms“, гл. 27 — точност около минута за 1000–3000 г.
 * Датата е по софийско време: есенното равноденствие 2026 е в 00:05 UTC, т.е.
 * 23 септември в България.
 */
object Equinoxes {
    enum class Kind { MARCH, JUNE, SEPTEMBER, DECEMBER }

    private val sofia: ZoneId = ZoneId.of("Europe/Sofia")

    /** Моментът на събитието (JDE се приема за UT — ΔT е около минута). */
    fun instant(year: Int, kind: Kind): Instant {
        val jde = jde(year, kind)
        val millis = Math.round((jde - 2440587.5) * 86_400_000.0)
        return Instant.ofEpochMilli(millis)
    }

    /** Датата в България. */
    fun date(year: Int, kind: Kind): LocalDate = instant(year, kind).atZone(sofia).toLocalDate()

    private fun jde(year: Int, kind: Kind): Double {
        val y = (year - 2000) / 1000.0
        val jde0 = when (kind) {
            Kind.MARCH -> 2451623.80984 + 365242.37404 * y + 0.05169 * y * y - 0.00411 * y * y * y - 0.00057 * y * y * y * y
            Kind.JUNE -> 2451716.56767 + 365241.62603 * y + 0.00325 * y * y + 0.00888 * y * y * y - 0.00030 * y * y * y * y
            Kind.SEPTEMBER -> 2451810.21715 + 365242.01767 * y - 0.11575 * y * y + 0.00337 * y * y * y + 0.00078 * y * y * y * y
            Kind.DECEMBER -> 2451900.05952 + 365242.74049 * y - 0.06223 * y * y - 0.00823 * y * y * y + 0.00032 * y * y * y * y
        }
        val t = (jde0 - 2451545.0) / 36525.0
        val w = rad(35999.373 * t - 2.47)
        val dl = 1 + 0.0334 * cos(w) + 0.0007 * cos(2 * w)
        var s = 0.0
        for (i in A.indices) s += A[i] * cos(rad(B[i] + C[i] * t))
        return jde0 + 0.00001 * s / dl
    }

    private fun rad(deg: Double) = deg * PI / 180.0

    // Периодичните членове (табл. 27.C).
    private val A = intArrayOf(485, 203, 199, 182, 156, 136, 77, 74, 70, 58, 52, 50, 45, 44, 29, 18, 17, 16, 14, 12, 12, 12, 9, 8)
    private val B = doubleArrayOf(
        324.96, 337.23, 342.08, 27.85, 73.14, 171.52, 222.54, 296.72, 243.58, 119.81, 297.17, 21.02,
        247.54, 325.15, 60.93, 155.12, 288.79, 198.04, 199.76, 95.39, 287.11, 320.81, 227.73, 15.45,
    )
    private val C = doubleArrayOf(
        1934.136, 32964.467, 20.186, 445267.112, 45036.886, 22518.443, 65928.934, 3034.906, 9037.513, 33718.147, 150.678, 2281.226,
        29929.562, 31555.956, 4443.417, 67555.328, 4562.452, 62894.029, 31436.921, 14577.848, 31931.756, 34777.259, 1222.114, 16859.074,
    )
}
