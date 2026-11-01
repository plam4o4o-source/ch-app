package org.chyavorec.app.ui.components

import android.app.Application
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.navigation.DETAIL_KEY
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Списък и детайл един до друг: „Назад“, тесен прозорец и редът за TalkBack. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "w1280dp-h800dp-land")
class ListDetailPaneTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private val state = SavedStateHandle()
    private val navigated = mutableListOf<String>()
    private var twoPane by mutableStateOf(true)

    private fun start() {
        compose.setContent {
            ListDetailPane(
                state, { navigated += it },
                detail = { route, close -> Column { Text("DETAIL $route"); Button(onClick = close) { Text("CLOSE") } } },
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

    private fun hasAnyDescendantText(text: String) =
        androidx.compose.ui.test.hasAnyDescendant(hasText(text, substring = true))
}
