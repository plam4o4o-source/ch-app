package org.chyavorec.app.ui.components

import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/** Чистата логика зад анимациите: цифрите на брояча-„одометър“ и сезоните (частиците — в HolidayCalendarTest). */
class MotionLogicTest {

    @Test fun digitsAreMostSignificantFirst() {
        assertContentEquals(intArrayOf(0), Odometer.digits(0))
        assertContentEquals(intArrayOf(7), Odometer.digits(7))
        assertContentEquals(intArrayOf(1, 9, 2, 2), Odometer.digits(1922))
        assertContentEquals(intArrayOf(1, 0, 0), Odometer.digits(100))
        // Знакът се показва отделно — цифрите са на абсолютната стойност (и за Int.MIN_VALUE).
        assertContentEquals(intArrayOf(4, 2), Odometer.digits(-42))
        assertContentEquals(intArrayOf(2, 1, 4, 7, 4, 8, 3, 6, 4, 8), Odometer.digits(Int.MIN_VALUE))
    }

    @Test fun rollsForwardWhenGrowingAndBackwardWhenShrinking() {
        // 7 → 2 при растящо число: 8, 9, 0, 1, 2 (напред, през нулата).
        assertEquals(12f, Odometer.rollTarget(7f, 2, up = true))
        // 7 → 2 при намаляващо: назад.
        assertEquals(2f, Odometer.rollTarget(7f, 2, up = false))
        // 2 → 7 при намаляващо: 1, 0, 9, 8, 7 (назад, през нулата).
        assertEquals(-3f, Odometer.rollTarget(2f, 7, up = false))
        // Същата цифра — без завъртане; позиция след няколко обиколки се взима по модул 10.
        assertEquals(17f, Odometer.rollTarget(17f, 7, up = true))
        assertEquals(20f, Odometer.rollTarget(19f, 0, up = true))
        // Прекъсната анимация (дробна позиция) — от най-близката цифра.
        assertEquals(15f, Odometer.rollTarget(13.4f, 5, up = true))
    }

    @Test fun introSpinsLowerDigitsMore() {
        assertEquals(22f, Odometer.introTarget(2, place = 0)) // единици: 2 обиколки
        assertEquals(12f, Odometer.introTarget(2, place = 1)) // десетици: 1 обиколка
        assertEquals(9f, Odometer.introTarget(9, place = 2))
        assertEquals(1f, Odometer.introTarget(1, place = 3))
    }

    @Test fun ornamentSeasonsUnchanged() {
        assertEquals(Season.CHRISTMAS, currentSeason(LocalDate.of(2026, 12, 15)))
        assertEquals(Season.HARVEST, currentSeason(LocalDate.of(2026, 9, 14)))
        assertEquals(Season.NONE, currentSeason(LocalDate.of(2026, 10, 20)))
    }
}
