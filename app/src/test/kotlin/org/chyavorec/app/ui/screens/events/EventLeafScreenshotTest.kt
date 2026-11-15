package org.chyavorec.app.ui.screens.events

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.assertNoMidWordBreak
import org.chyavorec.app.ui.components.LocalNavAnimatedScope
import org.chyavorec.app.ui.components.LocalReducedMotion
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
    private fun Page(dark: Boolean = false, fontScale: Float = 1f, reduced: Boolean = false, content: @Composable () -> Unit) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale), LocalReducedMotion provides reduced) {
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

    // --- Откъснатият отрязък (след връщане от календара) ---

    /** Листът и заглавието под него — както в подробностите (12 dp между тях). */
    @Composable
    private fun LeafAndTitle(torn: Boolean) = Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        EventLeaf(concert, LocalDate.parse("2026-12-25"), today, torn = torn)
        Text(concert.title, style = MaterialTheme.typography.headlineLarge)
    }

    /**
     * Отрязъкът се откъсва на пружина и запазва място под себе си (+12 dp), за да не легне върху
     * заглавието; думите са „Отворено в календара“ — не се знае дали там е запазено.
     */
    @Test fun tornStubKeepsClearOfTheTitleAndSaysOpened() {
        val torn = mutableStateOf(false)
        compose.setContent { Page { LeafAndTitle(torn.value) } }
        val leaf = compose.onNode(hasContentDescription("18:30", substring = true))
        val before = leaf.getUnclippedBoundsInRoot().let { it.bottom - it.top }
        torn.value = true
        compose.waitForIdle()
        val after = leaf.getUnclippedBoundsInRoot().let { it.bottom - it.top }
        assertEquals((before + 12.dp).value, after.value, 0.6f)
        compose.onNode(hasContentDescription("Отворено в календара", substring = true)).assertExists()
        compose.onNode(hasContentDescription("Добавено", substring = true)).assertDoesNotExist()
        shot("w24-event-leaf-stub-torn")
    }

    @Test fun tornStubDark() {
        compose.setContent { Page(dark = true) { LeafAndTitle(torn = true) } }
        shot("w24-event-leaf-stub-torn-dark")
    }

    @Config(qualifiers = "en-w411dp-h891dp-xhdpi")
    @Test fun tornStubSaysOpenedInEnglish() {
        compose.setContent { Page { LeafAndTitle(torn = true) } }
        compose.onNode(hasContentDescription("Opened in calendar", substring = true)).assertExists()
    }

    // --- „Падането“ на листа при отваряне на екрана ---

    /** Листът в преход на екран (като в навигацията): [visible] е състоянието на прехода. */
    private fun turnPage(visible: MutableTransitionState<Boolean>, reduced: Boolean, probe: (State<Float>?) -> Unit) {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            Page(reduced = reduced) {
                AnimatedVisibility(visibleState = visible, enter = EnterTransition.None, exit = ExitTransition.None) {
                    CompositionLocalProvider(LocalNavAnimatedScope provides this) {
                        probe(rememberLeafTurn())
                        LeafAndTitle(torn = false)
                    }
                }
            }
        }
    }

    /** Отваряне: от −75° (обърнат нагоре) към 0° на пружина; „назад“ — обратно нагоре. */
    @Test fun leafTurnsDownWhenTheScreenOpensAndBackOnBack() {
        // Екранът влиза: преходът тръгва от PreEnter към Visible още при първата композиция.
        val visible = MutableTransitionState(false).apply { targetState = true }
        var turn: State<Float>? = null
        turnPage(visible, reduced = false) { turn = it }
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeBy(80)
        val mid = assertNotNull(turn).value
        assertTrue(mid > 0.05f && mid < 0.95f, "по средата на падането: $mid")
        assertTrue(leafTurnDegrees(mid) < -3f && leafTurnDegrees(mid) > LEAF_TURN_DEG)
        shot("w24-event-leaf-turn-mid")
        compose.mainClock.advanceTimeBy(1500)
        assertEquals(1f, turn!!.value, 1e-3f)
        assertEquals(0f, leafTurnDegrees(turn!!.value), 0.1f)
        shot("w24-event-leaf-turn-end")
        // „Назад“: листът се обръща обратно нагоре, докато екранът излиза.
        visible.targetState = false
        Snapshot.sendApplyNotifications()
        compose.mainClock.advanceTimeByFrame()
        compose.mainClock.advanceTimeBy(100)
        val back = turn!!.value
        assertTrue(back < 0.95f, "при „назад“ листът се връща нагоре: $back")
    }

    /** Намалено движение: без завъртане — само кратка проява при отваряне. */
    @Test fun reducedMotionOnlyFadesTheLeafIn() {
        val visible = MutableTransitionState(false).apply { targetState = true }
        var turn: State<Float>? = null
        turnPage(visible, reduced = true) { turn = it }
        compose.mainClock.advanceTimeByFrame()
        assertTrue(assertNotNull(turn).value < 1f, "в началото листът е прозрачен")
        compose.mainClock.advanceTimeBy(1000)
        assertEquals(1f, turn!!.value, 1e-3f)
    }

    /** Екранът вече е видим (завъртане, връщане към него) — листът е на мястото си, без повторение. */
    private fun inPlaceWhenAlreadyVisible(reduced: Boolean) {
        var turn: State<Float>? = null
        turnPage(MutableTransitionState(true), reduced = reduced) { turn = it }
        compose.mainClock.advanceTimeByFrame()
        assertEquals(1f, assertNotNull(turn).value, 1e-3f)
    }

    @Test fun leafIsInPlaceWhenTheScreenIsAlreadyVisible() = inPlaceWhenAlreadyVisible(reduced = false)

    @Test fun leafIsInPlaceWhenAlreadyVisibleReducedMotion() = inPlaceWhenAlreadyVisible(reduced = true)
}
