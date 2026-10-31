package org.chyavorec.app.ui.screens.my

import android.app.Application
import android.os.Looper
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.R
import org.chyavorec.app.data.local.SettingsStore
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * „Добави в бързите настройки“ под картата (Android 13+): докосването показва системния
 * диалог, а след отговора или „×“ предложението изчезва и не се показва отново.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class CardTilePromptTest {
    @get:Rule val compose = createComposeRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val settings by lazy { SettingsStore(app) }
    private var requests = 0
    private val label get() = app.getString(R.string.card_tile_add)

    private fun show(done: Boolean, answer: Boolean = true) {
        runBlocking { settings.setCardTilePromptDone(done) }
        compose.setContent {
            ChitalishteTheme {
                CardTilePrompt(settings = settings, request = { _, onAnswer -> requests++; if (answer) onAnswer() })
            }
        }
    }

    private fun waitGone() = compose.waitUntil(5_000) {
        shadowOf(Looper.getMainLooper()).idle()
        compose.onAllNodesWithText(label).fetchSemanticsNodes().isEmpty()
    }

    private fun waitShown() = compose.waitUntil(5_000) {
        shadowOf(Looper.getMainLooper()).idle()
        compose.onAllNodesWithText(label).fetchSemanticsNodes().isNotEmpty()
    }

    @Test fun answeringTheSystemDialogHidesThePromptForGood() {
        show(done = false)
        waitShown()
        compose.onNodeWithText(label).performClick()
        assertEquals(1, requests)
        waitGone()
        assertTrue(runBlocking { settings.cardTilePromptDone.first() })
    }

    @Test fun dismissHidesWithoutAsking() {
        show(done = false)
        waitShown()
        compose.onNodeWithContentDescription(app.getString(R.string.card_tile_dismiss)).performClick()
        waitGone()
        assertEquals(0, requests)
        assertTrue(runBlocking { settings.cardTilePromptDone.first() })
    }

    @Test fun errorFromTheDialogKeepsThePrompt() {
        // Диалогът не е показан (напр. друг вече е отворен) — предложението остава.
        show(done = false, answer = false)
        waitShown()
        compose.onNodeWithText(label).performClick()
        compose.waitForIdle()
        assertEquals(1, requests)
        compose.onNodeWithText(label).assertExists()
    }

    @Test fun alreadyAnsweredShowsNothing() {
        show(done = true)
        compose.waitForIdle()
        shadowOf(Looper.getMainLooper()).idle()
        assertTrue(compose.onAllNodesWithText(label).fetchSemanticsNodes().isEmpty())
    }
}
