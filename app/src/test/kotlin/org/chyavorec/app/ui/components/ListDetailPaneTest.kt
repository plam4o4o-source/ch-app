package org.chyavorec.app.ui.components

import android.app.Application
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.navigation.DETAIL_KEY
import org.chyavorec.app.ui.navigation.detailStack
import org.chyavorec.app.ui.navigation.openDetail
import org.chyavorec.app.ui.navigation.popDetail
import org.chyavorec.app.ui.navigation.pushDetail
import org.chyavorec.app.ui.navigation.showDetail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

/** Списък и детайл един до друг: „Назад“, тесен прозорец и редът за TalkBack. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "w1280dp-h800dp-land")
class ListDetailPaneTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private val state = SavedStateHandle()
    private val navigated = mutableListOf<String>()
    private var twoPane by mutableStateOf(true)

    /** ViewModel на детайла: кой маршрут го е създал и дали е изчистен. */
    class Probe(val route: String) : ViewModel() {
        var cleared = false
        override fun onCleared() { cleared = true }
    }

    /** Последният ViewModel на всеки маршрут (по реда на създаване). */
    private val probes = mutableListOf<Probe>()

    private fun probe(route: String) = probes.last { it.route == route }

    private fun start() {
        compose.setContent {
            ListDetailPane(
                state, { navigated += it },
                detail = { route, close ->
                    val vm = viewModel { Probe(route).also { probes += it } }
                    val pane = LocalDetailPane.current
                    Column {
                        Text("DETAIL $route ${vm.route}")
                        Button(onClick = close) { Text("CLOSE") }
                        // Връзка от самия детайл към друг детайл на панела (като „Настройки“ › „Поверителност“).
                        Button(onClick = { pane!!.fromDetail { state.openDetail("$route/next") } }) { Text("LINK") }
                    }
                },
                twoPane = twoPane,
            ) {
                Column { Text("LIST"); Button(onClick = { state[DETAIL_KEY] = "book/1" }) { Text("OPEN") } }
            }
        }
    }

    private fun detailShown() = compose.onAllNodes(hasText("DETAIL", substring = true)).fetchSemanticsNodes().isNotEmpty()

    @Test fun backClosesTheDetailFirst() {
        start()
        compose.onNodeWithText("OPEN").performClick()
        compose.waitForIdle()
        assertTrue(detailShown())
        compose.onNodeWithText("LIST").assertExists()
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.waitForIdle()
        assertFalse(detailShown())
        assertNull(state.get<String>(DETAIL_KEY))
        compose.onNodeWithText("LIST").assertExists()
        // Следващото „Назад“ е вече на навигацията (списъкът не го задържа).
        assertFalse(compose.runOnUiThread { compose.activity.onBackPressedDispatcher.hasEnabledCallbacks() })
    }

    /** Предсказуемо „назад“: докато жестът тече и при отказ детайлът остава; пуснат — затваря се. */
    @Test fun predictiveBackCancelKeepsTheDetail() {
        start()
        compose.onNodeWithText("OPEN").performClick()
        compose.waitForIdle()
        val back = compose.activity.onBackPressedDispatcher
        compose.runOnUiThread {
            back.dispatchOnBackStarted(BackEventCompat(0f, 400f, 0f, BackEventCompat.EDGE_LEFT))
            back.dispatchOnBackProgressed(BackEventCompat(200f, 400f, 0.5f, BackEventCompat.EDGE_LEFT))
        }
        compose.waitForIdle()
        assertTrue(detailShown())
        compose.runOnUiThread { back.dispatchOnBackCancelled() }
        compose.waitForIdle()
        assertTrue(detailShown())
        assertEquals("book/1", state.get<String>(DETAIL_KEY))
        // Целият жест — детайлът се затваря.
        compose.runOnUiThread {
            back.dispatchOnBackStarted(BackEventCompat(0f, 400f, 0f, BackEventCompat.EDGE_LEFT))
            back.dispatchOnBackProgressed(BackEventCompat(300f, 400f, 0.8f, BackEventCompat.EDGE_LEFT))
            back.onBackPressed()
        }
        compose.waitForIdle()
        assertFalse(detailShown())
        assertNull(state.get<String>(DETAIL_KEY))
    }

    @Test fun closeButtonInTheDetail() {
        start()
        compose.onNodeWithText("OPEN").performClick()
        compose.onNodeWithText("CLOSE").performClick()
        compose.waitForIdle()
        assertFalse(detailShown())
    }

    /** Завъртане/разделен екран: избраното продължава на цял екран над списъка. */
    @Test fun narrowWindowOpensTheSelectionFullScreen() {
        start()
        compose.onNodeWithText("OPEN").performClick()
        compose.waitForIdle()
        twoPane = false
        compose.waitForIdle()
        assertEquals(listOf("book/1"), navigated)
        assertNull(state.get<String>(DETAIL_KEY))
        assertFalse(detailShown())
    }

    /** Избор на тесен прозорец (напр. отпреди завъртането) — също на цял екран. */
    @Test fun selectionOnANarrowWindowIsNotShownBeside() {
        twoPane = false
        state[DETAIL_KEY] = "article/a"
        start()
        compose.waitForIdle()
        assertEquals(listOf("article/a"), navigated)
        assertFalse(detailShown())
    }

    /** TalkBack: две групи — списъкът (отляво, първи), после детайлът. */
    @Test fun talkBackReadsTheListFirst() {
        start()
        compose.onNodeWithText("OPEN").performClick()
        compose.waitForIdle()
        val group = SemanticsMatcher.expectValue(SemanticsProperties.IsTraversalGroup, true)
        val list = compose.onNode(group and hasAnyDescendantText("LIST")).fetchSemanticsNode()
        val detail = compose.onNode(group and hasAnyDescendantText("DETAIL")).fetchSemanticsNode()
        assertEquals(0f, list.config.getOrNull(SemanticsProperties.TraversalIndex))
        assertEquals(1f, detail.config.getOrNull(SemanticsProperties.TraversalIndex))
        assertTrue(list.boundsInRoot.right <= detail.boundsInRoot.left)
        compose.onNode(hasText("DETAIL", substring = true) and hasAnyAncestor(group)).assertExists()
    }

    /** Всеки показан детайл — със свои ViewModel-и: затворен или сменен, те се изчистват (B2). */
    @Test fun eachShownDetailHasItsOwnViewModels() {
        start()
        compose.runOnIdle { state.showDetail("quiz") }
        compose.waitForIdle()
        val first = probe("quiz")
        assertFalse(first.cleared)
        // Затворен — изчистен; отворен наново — нов (не вчерашният резултат).
        compose.runOnIdle { state.showDetail(null) }
        compose.waitForIdle()
        assertTrue(first.cleared)
        compose.runOnIdle { state.showDetail("quiz") }
        compose.waitForIdle()
        val second = probe("quiz")
        assertNotSame(first, second)
        // Смяна на раздела — и старият се изчиства (след анимацията).
        compose.runOnIdle { state.showDetail("timeline") }
        compose.waitForIdle()
        assertTrue(second.cleared)
        assertFalse(probe("timeline").cleared)
    }

    /** Връзка от детайла — над него; „Назад“ се връща към него със същия ViewModel, после затваря (B3). */
    @Test fun linkInsideThePaneStacksAndBackReturns() {
        start()
        compose.runOnIdle { state.showDetail("settings") }
        compose.waitForIdle()
        val settings = probe("settings")
        compose.onNodeWithText("LINK").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("DETAIL settings/next", substring = true).assertExists()
        assertEquals(listOf("settings"), state.detailStack())
        assertFalse(settings.cleared)
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.waitForIdle()
        compose.onNodeWithText("DETAIL settings settings").assertExists()
        assertSame(settings, probe("settings"))
        assertTrue(probe("settings/next").cleared)
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.waitForIdle()
        assertFalse(detailShown())
        assertTrue(settings.cleared)
    }

    /** Избор от списъка, докато в панела има стек — наново, без екраните под стария. */
    @Test fun choosingFromTheListReplacesTheStack() {
        start()
        compose.runOnIdle { state.showDetail("settings") }
        compose.waitForIdle()
        compose.onNodeWithText("LINK").performClick()
        compose.waitForIdle()
        compose.runOnIdle { state.openDetail("contacts") }
        compose.waitForIdle()
        assertEquals(emptyList(), state.detailStack())
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.waitForIdle()
        assertFalse(detailShown())
    }

    /** Стек в панела, а прозорецът стане тесен — на цял екран целият стек (Назад води по същия път). */
    @Test fun narrowWindowOpensTheWholePaneStack() {
        start()
        compose.runOnIdle { state.showDetail("settings") }
        compose.waitForIdle()
        compose.onNodeWithText("LINK").performClick()
        compose.waitForIdle()
        twoPane = false
        compose.waitForIdle()
        assertEquals(listOf("settings", "settings/next"), navigated)
        assertNull(state.get<String>(DETAIL_KEY))
        assertEquals(emptyList(), state.detailStack())
    }

    /** Стекът в панела не повтаря екран: връзка към екран отдолу се връща до него. */
    @Test fun paneStackNeverRepeatsAScreen() {
        state.showDetail("a")
        state.pushDetail("b")
        state.pushDetail("c")
        assertEquals(listOf("a", "b"), state.detailStack())
        state.pushDetail("c")
        assertEquals(listOf("a", "b"), state.detailStack())
        state.pushDetail("a")
        assertEquals(emptyList(), state.detailStack())
        assertEquals("a", state.get<String>(DETAIL_KEY))
        state.pushDetail("d")
        state.popDetail()
        assertEquals("a", state.get<String>(DETAIL_KEY))
        state.popDetail()
        assertNull(state.get<String>(DETAIL_KEY))
    }

    private fun hasAnyDescendantText(text: String) =
        androidx.compose.ui.test.hasAnyDescendant(hasText(text, substring = true))
}
