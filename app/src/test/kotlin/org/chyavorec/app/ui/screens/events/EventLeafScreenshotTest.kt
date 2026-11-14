package org.chyavorec.app.ui.screens.events

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.assertNoMidWordBreak
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.Event
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * Листът на събитието (празник и час на листа, празен ден), кръглите действия и бродираният
 * ромб на календара — за преглед в клона „screenshots“ (телефон, тъмна тема, таблет, голям шрифт).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class EventLeafScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }
    private val today = LocalDate.of(2026, 11, 12)

    private val concert = Event(
        "e1", "Коледен концерт на децата от школите", "2026-12-25", time = "18.30", place = "НЧ „Васил Левски – 1922“",
        category = "Концерт", sourceUrl = "https://chyavorec.org/events",
    )
    private val plain = Event("e2", "Световен ден за борба срещу СПИН", "2026-12-01", category = "Читалище", sourceUrl = "https://chyavorec.org/events")

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Page(dark: Boolean = false, fontScale: Float = 1f, content: @Composable () -> Unit) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale)) {
                Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp), contentAlignment = Alignment.TopCenter) {
                    content()
                }
            }
        }
    }

    @Composable
    private fun Actions() = EventActions(
        showCalendar = true, onCalendar = {}, showReminder = true, hasReminder = false, onReminder = {},
        showRoute = true, onRoute = {}, onSource = {},
    )

    @Composable
    private fun LeafAndActions(e: Event, date: String) = Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        EventLeaf(e, LocalDate.parse(date), today)
        Actions()
    }

    @Test fun leafPhoneLight() {
        compose.setContent { Page { LeafAndActions(concert, "2026-12-25") } }
        shot("w21-event-leaf-1-phone")
    }

    @Test fun leafPhoneDark() {
        compose.setContent { Page(dark = true) { LeafAndActions(concert, "2026-12-25") } }
        shot("w21-event-leaf-2-dark")
    }

    /** Без празник и без час: листът е по-нисък, отрязъкът — само „след N дни“. */
    @Test fun leafWithoutFeastOrTime() {
        compose.setContent { Page { LeafAndActions(plain, "2026-12-01") } }
        shot("w21-event-leaf-3-plain")
    }

    @Config(qualifiers = "bg-w1280dp-h800dp-land-xhdpi")
    @Test fun leafTablet() {
        compose.setContent {
            Page {
                Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                    Column(Modifier.width(340.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        EventLeaf(concert, LocalDate.parse("2026-12-25"), today)
                        Actions()
                    }
                    Column(Modifier.width(420.dp)) { EventLeaf(plain, LocalDate.parse("2026-12-01"), today) }
                }
            }
        }
        shot("w21-event-leaf-4-tablet")
    }

    @Test fun leafLargeFont() {
        compose.setContent { Page(fontScale = 2f) { Column { LeafAndActions(concert, "2026-12-25") } } }
        shot("w21-event-leaf-5-large-font")
    }

    @Test fun leafFont13() {
        compose.setContent { Page(fontScale = 1.3f) { Column { LeafAndActions(concert, "2026-12-25") } } }
        shot("w22-event-leaf-font13")
    }

    /** Етикетите на действията не се късат насред дума при 1.0, 1.3 и 2× (светла и тъмна тема). */
    @Test fun actionLabelsNeverBreakMidWord() {
        val scale = androidx.compose.runtime.mutableStateOf(1f)
        val dark = androidx.compose.runtime.mutableStateOf(false)
        compose.setContent { Page(dark = dark.value, fontScale = scale.value) { Actions() } }
        for (d in listOf(false, true)) for (s in listOf(1f, 1.15f, 1.3f, 1.5f, 2f)) {
            dark.value = d
            scale.value = s
            compose.waitForIdle()
            for (label in listOf("Напомни ми", "Маршрут", "Виж публикацията")) {
                compose.assertNoMidWordBreak(label)
                compose.onNode(hasText(label) and hasClickAction()).assertIsDisplayed().assertHeightIsAtLeast(48.dp).assertWidthIsAtLeast(48.dp)
            }
        }
        scale.value = 2f
        dark.value = false
        shot("w22-event-actions-font2-wide")
    }

    /** 320 dp: „Ноември 2026“ се събира цял, а разделът слиза под него, ако не стига място. */
    @Config(qualifiers = "bg-w320dp-h640dp-xhdpi")
    @Test fun leafNarrow() {
        compose.setContent { Page { EventLeaf(concert.copy(date = "2026-11-28"), LocalDate.parse("2026-11-28"), today) } }
        shot("c23-12-event-leaf-320")
    }

    @Config(qualifiers = "bg-w320dp-h640dp-xhdpi")
    @Test fun leafNarrowLargeFont() {
        compose.setContent { Page(fontScale = 1.3f) { EventLeaf(concert.copy(date = "2026-09-28", category = "Литературно четене"), LocalDate.parse("2026-09-28"), today) } }
        shot("c23-13-event-leaf-320-font13")
    }

    /** Минало и днешно събитие: „преди N дни“ и „днес“ в отрязъка. */
    @Test fun leafPastAndToday() {
        compose.setContent {
            Page {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    EventLeaf(plain, LocalDate.parse("2026-11-09"), today)
                    EventLeaf(plain.copy(id = "e3"), today, today)
                }
            }
        }
        shot("w21-event-leaf-6-past-today")
    }

    /** Всяко действие се чете с етикет, е поне 48 dp и върши работата си. */
    @Test fun actionsAreLabelledBigEnoughAndWork() {
        val clicked = mutableListOf<String>()
        compose.setContent {
            Page {
                EventActions(
                    showCalendar = true, onCalendar = { clicked += "calendar" }, showReminder = true, hasReminder = false,
                    onReminder = { clicked += "reminder" }, showRoute = true, onRoute = { clicked += "route" }, onSource = { clicked += "source" },
                )
            }
        }
        for (label in listOf("Добави в календара", "Напомни ми", "Маршрут", "Виж публикацията")) {
            compose.onNode(hasText(label) and hasClickAction()).assertIsDisplayed().assertHeightIsAtLeast(48.dp).assertWidthIsAtLeast(48.dp).performClick()
        }
        assertEquals(listOf("calendar", "reminder", "route", "source"), clicked)
    }
}
