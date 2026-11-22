package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Бродираният медальон на празните състояния: декоративен (TalkBack чете само заглавието),
 * а на нисък прозорец празното състояние е по-ниско — само медальонът, без илюстрацията и лентата.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class EmbroideredMedallionTest {
    @get:Rule val compose = createComposeRule()

    @Test fun theCompactThresholdIsAbout480dp() {
        assertTrue(isCompactMessageHeight(411.dp), "хоризонтален телефон")
        assertTrue(isCompactMessageHeight(479.dp))
        assertFalse(isCompactMessageHeight(480.dp))
        assertFalse(isCompactMessageHeight(891.dp))
    }

    @Test fun theMedallionIsDecorative() {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                ChitalishteTheme(mode = ThemeMode.DARK) { EmptyView("Няма резултати", icon = Icons.Outlined.SearchOff) }
            }
        }
        compose.onNodeWithText("Няма резултати").assertExists()
        val described = compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentDescription), useUnmergedTree = true)
            .fetchSemanticsNodes()
        assertEquals(0, described.size, "медальонът и илюстрацията не се обявяват")
    }

    @Test fun aShortWindowGetsALowerEmptyState() {
        var compact by mutableStateOf(false)
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                ChitalishteTheme {
                    Box(Modifier.testTag("empty")) { EmptyView("Няма резултати", "Опитай с друга дума.", icon = Icons.Outlined.SearchOff, compact = compact) }
                }
            }
        }
        fun height() = compose.onNodeWithTag("empty").getUnclippedBoundsInRoot().height.value
        val full = height()
        compact = true
        compose.waitForIdle()
        val low = height()
        assertTrue(low < full - 80f, "компактното ($low dp) е поне 80 dp по-ниско от пълното ($full dp)")
    }
}
