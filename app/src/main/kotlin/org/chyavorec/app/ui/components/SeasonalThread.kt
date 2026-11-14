package org.chyavorec.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.BgHoliday
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ShevitsaCalendar
import java.time.LocalDate
import java.time.Month

/*
 * „Сезонният конец“: бодовете пред етикетите на разделите (SectionLabel) и пунктираният ъгъл на
 * натисната плочка следват празниците от [ShevitsaCalendar], както рамките, кориците и картите
 * за споделяне. Малка, спокойна палитра; рисува се веднъж (без анимация на цвета) и само при
 * включени „Сезонни украси“ — иначе е обичайното злато на темата.
 */

/** Конците през годината. */
enum class SeasonalThread {
    /** Обичайното злато на темата. */
    GOLD,

    /** 1–9 март: червено и бяло, усукани като мартеница. */
    MARTENITSA,

    /** Коледа (20 декември – 7 януари): злато със звезден бод. */
    CHRISTMAS_STAR,

    /** Цветница, Великден и Гергьовден: зелен конец. */
    SPRING_GREEN,
    ;

    companion object {
        /** Конецът за [date]; мартеницата е с предимство (3 март е и в нейните дни). */
        fun forDate(date: LocalDate): SeasonalThread {
            if (date.month == Month.MARCH && date.dayOfMonth in 1..9) return MARTENITSA
            return when (ShevitsaCalendar.holiday(date)) {
                BgHoliday.CHRISTMAS -> CHRISTMAS_STAR
                // Великденският прозорец (±7 дни) обхваща и Цветница (седмица преди Великден).
                BgHoliday.EASTER, BgHoliday.GERGYOVDEN -> SPRING_GREEN
                else -> GOLD
            }
        }
    }
}

/**
 * Цветовете на конеца: [primary] — основният; [second] — вторият конец на усукания (мартеница),
 * с кант [secondEdge] (бялото на светъл фон иначе не се вижда); [star] — звезден бод (кръст + „+“).
 */
@Immutable
data class ThreadColors(val primary: Color, val second: Color? = null, val secondEdge: Color? = null, val star: Boolean = false)

/** Цветовете на [thread] в светла/тъмна тема; [gold] — златото на темата. */
fun threadColors(thread: SeasonalThread, gold: Color, dark: Boolean): ThreadColors = when (thread) {
    SeasonalThread.GOLD -> ThreadColors(gold)
    SeasonalThread.MARTENITSA -> if (dark) ThreadColors(MartenitsaRedDark, MartenitsaWhite)
    else ThreadColors(MartenitsaRed, MartenitsaWhite, secondEdge = MartenitsaRed.copy(alpha = 0.55f))
    SeasonalThread.CHRISTMAS_STAR -> ThreadColors(gold, star = true)
    SeasonalThread.SPRING_GREEN -> ThreadColors(if (dark) SpringGreenDark else SpringGreen)
}

// Червеното на мартеницата (в тъмна тема — по-светло, за контраст) и бялото — слонова кост.
private val MartenitsaRed = Color(0xFFB3261E)
private val MartenitsaRedDark = Color(0xFFE0605A)
private val MartenitsaWhite = Color(0xFFFFFBF3)

// Зеленото на Цветница: приглушено, като на писаните яйца ([EasterEggs]).
private val SpringGreen = Color(0xFF4A7A52)
private val SpringGreenDark = Color(0xFF9CC5A1)

/**
 * Датата на сезонния конец в тестовете и прегледите (`null` — днешната по часовника на приложението).
 */
val LocalSeasonalDate = staticCompositionLocalOf<LocalDate?> { null }

/**
 * Днешният конец, ако украсите са включени. Настройката идва от корена ([LocalSeasonalDecor]);
 * без нея (преглед, тест без приложение) — обичайното злато, без да се пипа контейнерът.
 */
@Composable
fun rememberSeasonalThread(): ThreadColors {
    val ext = LocalExtendedColors.current
    val enabled = LocalSeasonalDecor.current == true
    val date = if (enabled) LocalSeasonalDate.current ?: LocalAppContainer.current.clock.today() else null
    return remember(date, ext.gold, ext.isDark) {
        threadColors(date?.let { SeasonalThread.forDate(it) } ?: SeasonalThread.GOLD, ext.gold, ext.isDark)
    }
}
