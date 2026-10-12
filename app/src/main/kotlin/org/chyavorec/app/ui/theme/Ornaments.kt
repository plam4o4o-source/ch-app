package org.chyavorec.app.ui.theme

import org.chyavorec.core.OrthodoxEaster
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit

/** Мотиви от българската шевица (рисуват се като векторни пътища — виж ui/components/Decor.kt). */
enum class ShevitsaMotif {
    /** Ромб с ромб и точка в средата — най-старият „пазещ“ знак. */
    RHOMBUS,

    /** Осмолъчна розетка („звезда“) — Коледа, литература, изкуство. */
    ROSETTE,

    /** Дърво на живота — Великден, будителите, знанието. */
    TREE_OF_LIFE,

    /** Кривулица (зигзаг) — вода, път, Гергьовден. */
    ZIGZAG,
}

/** Празниците, за които шевицата се сменя. */
enum class BgHoliday { CHRISTMAS, EASTER, LIBERATION_DAY, GERGYOVDEN, BUDITELI }

/**
 * Кой мотив да се рисува на дадена дата: първо празниците (с кратък прозорец около
 * тях), после сезонът. Детерминирано — една и съща дата дава един и същ мотив.
 */
object ShevitsaCalendar {
    /** Празникът, в чийто прозорец е [date]; празниците с фиксирана дата са с предимство пред Великден. */
    fun holiday(date: LocalDate): BgHoliday? {
        val m = date.month
        val d = date.dayOfMonth
        // 3 март (и Баба Марта преди него): 1–3 март.
        if (m == Month.MARCH && d in 1..3) return BgHoliday.LIBERATION_DAY
        // Гергьовден — 6 май (5–7 май).
        if (m == Month.MAY && d in 5..7) return BgHoliday.GERGYOVDEN
        // Ден на народните будители — 1 ноември (30 октомври – 1 ноември).
        if ((m == Month.OCTOBER && d >= 30) || (m == Month.NOVEMBER && d == 1)) return BgHoliday.BUDITELI
        // Великден — седмица преди и след (православен).
        val easter = OrthodoxEaster.of(date.year)
        if (kotlin.math.abs(ChronoUnit.DAYS.between(easter, date)) <= 7) return BgHoliday.EASTER
        // Коледа — от 20 декември до Ивановден (7 януари).
        if ((m == Month.DECEMBER && d >= 20) || (m == Month.JANUARY && d <= 7)) return BgHoliday.CHRISTMAS
        return null
    }

    fun motifFor(date: LocalDate): ShevitsaMotif = when (holiday(date)) {
        BgHoliday.CHRISTMAS -> ShevitsaMotif.ROSETTE
        BgHoliday.EASTER -> ShevitsaMotif.TREE_OF_LIFE
        BgHoliday.LIBERATION_DAY -> ShevitsaMotif.RHOMBUS
        BgHoliday.GERGYOVDEN -> ShevitsaMotif.ZIGZAG
        BgHoliday.BUDITELI -> ShevitsaMotif.TREE_OF_LIFE
        null -> when (date.month) {
            Month.DECEMBER, Month.JANUARY, Month.FEBRUARY -> ShevitsaMotif.ROSETTE
            Month.MARCH, Month.APRIL, Month.MAY -> ShevitsaMotif.TREE_OF_LIFE
            Month.JUNE, Month.JULY, Month.AUGUST -> ShevitsaMotif.ZIGZAG
            else -> ShevitsaMotif.RHOMBUS
        }
    }
}

/** Генерирана („типографска“) корица за книга без истинска корица. */
object GeneratedCover {
    /** Цветът на текста върху корицата — слонова кост. */
    const val INK: Int = -0x40d // 0xFFFFFBF3

    /**
     * Най-силното изсветляване, което корицата слага върху фона (горният край на
     * градиента + „хартиената“ светлина) — контрастът се проверява и спрямо него.
     */
    const val LIGHTEST_OVERLAY = 0.18f

    /** FNV-1a (32 бита) — стабилен между версии и устройства (за разлика от реда в HashMap). */
    fun stableHash(text: String): Int {
        var h = 0x811C9DC5.toInt()
        for (ch in text) {
            h = h xor ch.code
            h *= 0x01000193
        }
        return h
    }

    /**
     * Фонът на корицата: тон от [anchors] (палитрата на темата) според автора, с
     * малко отклонение на тона и светлотата; текстът ([INK]) има контраст ≥ 4.5:1
     * и в най-светлата точка на корицата ([LIGHTEST_OVERLAY]). В тъмна тема е малко по-тъмен.
     */
    fun backgroundArgb(author: String, anchors: IntArray, dark: Boolean): Int {
        require(anchors.isNotEmpty())
        val h = stableHash(author.trim().lowercase())
        val anchor = anchors[(h ushr 1) % anchors.size]
        val hsl = ColorMath.toHsl(anchor)
        val dl = ((h ushr 8) and 0xFF) / 255f // 0..1
        val dh = (((h ushr 16) and 0xFF) / 255f - 0.5f) * 16f // ±8°
        var l = (hsl[2] * (0.85f + 0.3f * dl) * (if (dark) 0.88f else 1f)).coerceIn(0.12f, 0.40f)
        val hue = hsl[0] + dh
        var c = ColorMath.fromHsl(hue, hsl[1], l)
        while (!readableOn(c) && l > 0.04f) {
            l -= 0.02f
            c = ColorMath.fromHsl(hue, hsl[1], l)
        }
        return c
    }

    /** Текстът [INK] се чете и в най-светлата точка на фона [background]. */
    fun readableOn(background: Int): Boolean =
        ColorMath.readable(INK, ColorMath.mix(background, ColorMath.WHITE, LIGHTEST_OVERLAY))

    /**
     * Мотивът на лентата според раздела по УДК и вида документ: литература и
     * изкуство — розетка; история и география — ромб; природни и приложни науки —
     * кривулица; общи, философия, религия, обществени — дърво на живота.
     * Нетекстовите документи (видео, аудио, карти…) — кривулица.
     */
    fun motifFor(udcSection: Int?, docType: String): ShevitsaMotif {
        val type = docType.lowercase()
        if (type.isNotBlank() && !type.startsWith("книг") && !type.startsWith("book") &&
            listOf("видео", "аудио", "звук", "карт", "ноти", "video", "audio", "map").any { it in type }
        ) {
            return ShevitsaMotif.ZIGZAG
        }
        return when (udcSection) {
            7, 8 -> ShevitsaMotif.ROSETTE
            9 -> ShevitsaMotif.RHOMBUS
            5, 6 -> ShevitsaMotif.ZIGZAG
            0, 1, 2, 3 -> ShevitsaMotif.TREE_OF_LIFE
            else -> ShevitsaMotif.RHOMBUS
        }
    }
}
