package org.chyavorec.domain.occasion

import org.chyavorec.core.Equinoxes
import org.chyavorec.core.OrthodoxEaster
import org.chyavorec.domain.occasion.OccasionEffect as E
import org.chyavorec.domain.occasion.OccasionOrnament as O
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay

/**
 * Кой празник, сезон или обичай се пада на дадена дата и как се украсява денят.
 *
 * Важност (по-високото печели): паметни дни 100 → Великден 90 → 24 май 85 →
 * официални празници 80 → празници от сайта 75 → дни на книгата 60 → народни
 * празници 50 → начало на сезон 40 → периоди без поздрав (мартеничка, яйца,
 * сняг, листа, цветчета) 10–21.
 */
object HolidayCalendar {
    const val MEMORIAL = 100
    const val EASTER = 90
    const val CULTURE = 85
    const val OFFICIAL = 80
    const val SITE = 75
    const val BOOKS = 60
    const val FOLK = 50
    const val SEASON = 40

    /** Всички поводи за [date] — най-важният първи. [site] са празниците от сайта. */
    fun occasionsOn(date: LocalDate, site: List<SiteOccasion> = emptyList()): List<Occasion> {
        val list = ArrayList<Occasion>()
        fixed(date, list)
        easterCycle(date, list)
        seasons(date, list)
        periods(date, list)
        site.filter { it.matches(date) }.mapTo(list) { it.occasion }
        return list.sortedByDescending { it.priority }
    }

    /**
     * Украсата за деня: ефектът, орнаментът и поздравът идват всеки от най-важния
     * повод, който ги има. Паметен ден (или тих повод от сайта) е единствената
     * украса — без анимации и без весели орнаменти от по-маловажните поводи.
     */
    fun decorFor(date: LocalDate, site: List<SiteOccasion> = emptyList()): DayDecor {
        val all = occasionsOn(date, site)
        val top = all.firstOrNull() ?: return DayDecor.NONE
        if (top.mood == OccasionMood.QUIET) {
            return DayDecor(E.NONE, top.ornament, top.takeIf { it.greeting }, quiet = true)
        }
        return DayDecor(
            effect = all.firstOrNull { it.effect != E.NONE }?.effect ?: E.NONE,
            ornament = all.firstOrNull { it.ornament != O.NONE }?.ornament ?: O.NONE,
            greeting = all.firstOrNull { it.greeting },
        )
    }

    private fun fixed(date: LocalDate, out: MutableList<Occasion>) {
        val d = date.dayOfMonth
        fun add(o: Occasion) { out += o }
        when (date.month) {
            Month.JANUARY -> when (d) {
                1 -> {
                    add(Occasion("new_year", OFFICIAL, effect = E.FIREWORKS))
                    // Сурвакането е част от новогодишния поздрав — тук е само сурвачката.
                    add(Occasion("survaki", FOLK, ornament = O.SURVACHKA, greeting = false))
                }
                6 -> add(Occasion("jordan", FOLK, ornament = O.CROSS_WATER))
                21 -> add(Occasion("babinden", FOLK))
            }
            Month.FEBRUARY -> when (d) {
                14 -> add(Occasion("trifon", FOLK, ornament = O.VINE))
                19 -> add(Occasion("levski_memorial", MEMORIAL, OccasionMood.QUIET, ornament = O.MEMORIAL_WREATH, link = OccasionLink(text = "Левски")))
            }
            Month.MARCH -> when (d) {
                1 -> add(Occasion("baba_marta", FOLK, ornament = O.MARTENITSA))
                3 -> add(Occasion("liberation", OFFICIAL, ornament = O.TRICOLOR, link = OccasionLink(text = "Освобождение")))
            }
            Month.APRIL -> when (d) {
                2 -> add(Occasion("children_book", BOOKS, link = OccasionLink(departmentHint = "детск")))
                23 -> add(Occasion("book_day", BOOKS, effect = E.PAGES, link = OccasionLink(newest = true)))
            }
            Month.MAY -> when (d) {
                1 -> add(Occasion("labour", OFFICIAL))
                6 -> add(Occasion("gergyovden", OFFICIAL, ornament = O.GREEN_WREATH))
                24 -> add(Occasion("may24", CULTURE, effect = E.LETTERS, ornament = O.LAUREL_WREATH, link = OccasionLink(text = "Кирил Методий")))
            }
            Month.JUNE -> when (d) {
                1 -> add(Occasion("children_day", BOOKS, effect = E.BALLOONS, link = OccasionLink(departmentHint = "детск")))
                2 -> add(Occasion("botev", MEMORIAL, OccasionMood.QUIET, ornament = O.MEMORIAL_WREATH, link = OccasionLink(text = "Ботев")))
                24 -> add(Occasion("enyovden", FOLK, effect = E.DEW, ornament = O.HERB_WREATH))
            }
            Month.JULY -> when (d) {
                18 -> add(Occasion("levski_birth", FOLK + 5, ornament = O.LAUREL_WREATH, link = OccasionLink(text = "Левски")))
                20 -> add(Occasion("ilinden", FOLK))
            }
            Month.AUGUST -> if (d == 15) add(Occasion("assumption", FOLK))
            Month.SEPTEMBER -> when (d) {
                6 -> add(Occasion("unification", OFFICIAL, ornament = O.TRICOLOR))
                22 -> add(Occasion("independence", OFFICIAL, ornament = O.TRICOLOR))
            }
            Month.OCTOBER -> if (d == 26) add(Occasion("dimitrovden", FOLK))
            Month.NOVEMBER -> if (d == 1) add(Occasion("enlighteners", CULTURE - 1, ornament = O.BOOK_FLAME))
            Month.DECEMBER -> when (d) {
                6 -> add(Occasion("nikulden", FOLK, ornament = O.FISH))
                20 -> add(Occasion("ignazhden", FOLK))
                24 -> add(Occasion("christmas_eve", OFFICIAL, ornament = O.RITUAL_BREAD))
                25, 26 -> add(Occasion("christmas", OFFICIAL, ornament = O.CHRISTMAS_STAR))
            }
            else -> Unit
        }
    }

    private fun easterCycle(date: LocalDate, out: MutableList<Occasion>) {
        val easter = OrthodoxEaster.of(date.year)
        when (java.time.temporal.ChronoUnit.DAYS.between(easter, date)) {
            -49L -> out += Occasion("forgiveness", FOLK)
            -8L -> out += Occasion("lazarovden", FOLK, ornament = O.FLOWER_WREATH)
            -7L -> out += Occasion("tsvetnitsa", FOLK, ornament = O.WILLOW)
            -2L -> out += Occasion("good_friday", EASTER + 5, OccasionMood.QUIET)
            0L, 1L -> out += Occasion("easter", EASTER, ornament = O.EASTER_EGGS)
            39L -> out += Occasion("ascension", FOLK)
        }
    }

    private fun seasons(date: LocalDate, out: MutableList<Occasion>) {
        val y = date.year
        when (date) {
            Equinoxes.date(y, Equinoxes.Kind.MARCH) -> out += Occasion("spring", SEASON, effect = E.PETALS_BUTTERFLIES)
            Equinoxes.date(y, Equinoxes.Kind.JUNE) -> out += Occasion("summer", SEASON, ornament = O.SUN)
            Equinoxes.date(y, Equinoxes.Kind.SEPTEMBER) -> out += Occasion("autumn", SEASON, effect = E.LEAVES)
            Equinoxes.date(y, Equinoxes.Kind.DECEMBER) -> out += Occasion("winter", SEASON)
        }
    }

    /** Периоди без поздрав — само фон: мартеничка, яйца, сняг, листа, цветчета. */
    private fun periods(date: LocalDate, out: MutableList<Occasion>) {
        val m = date.month
        val d = date.dayOfMonth
        if (m == Month.MARCH) out += Occasion("martenitsa", 20, ornament = O.MARTENITSA, greeting = false)
        val easter = OrthodoxEaster.of(date.year)
        if (!date.isBefore(easter.minusDays(7)) && !date.isAfter(easter.plusDays(7))) {
            out += Occasion("easter_eggs", 21, ornament = O.EASTER_EGGS, greeting = false)
        }
        val sky = when {
            (m == Month.DECEMBER && d >= 20) || (m == Month.JANUARY && d <= 7) -> E.SNOW_STARS
            m == Month.DECEMBER && d >= 15 -> E.SNOW
            (m == Month.OCTOBER && d >= 15) || m == Month.NOVEMBER -> E.LEAVES
            m == Month.MAY && d <= 10 -> E.PETALS
            else -> E.NONE
        }
        if (sky != E.NONE) out += Occasion("sky", 10, effect = sky, greeting = false)
    }
}

/**
 * Празник, добавен от сайта: всяка година от [from] до [to] (включително; може да
 * минава през Нова година) или само в една година, ако е дадена [year].
 */
data class SiteOccasion(val from: MonthDay, val to: MonthDay, val year: Int?, val occasion: Occasion) {
    fun matches(date: LocalDate): Boolean {
        val md = MonthDay.from(date)
        val inRange = if (!to.isBefore(from)) !md.isBefore(from) && !md.isAfter(to) else !md.isBefore(from) || !md.isAfter(to)
        if (!inRange) return false
        if (year == null) return true
        if (!to.isBefore(from)) return date.year == year
        // Период през Нова година: декемврийската част е в [year], януарската — в следващата.
        // Иначе 1 януари на [year] би съвпаднал, макар периодът да започва чак в края ѝ.
        return if (!md.isBefore(from)) date.year == year else date.year == year + 1
    }
}
