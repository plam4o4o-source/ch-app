package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * TalkBack: етикетът на раздел е едно заглавие (текстът и `heading()` на един възел), а
 * докосваемите карти и плочки казват „бутон“ — със и без собствен етикет на действието.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg")
class CardSemanticsTest {
    @get:Rule val compose = createComposeRule()
    private val isButton = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)

    private fun show(content: @Composable () -> Unit) = compose.setContent { ChitalishteTheme(mode = ThemeMode.LIGHT) { content() } }

    @Test fun sectionLabelIsOneHeadingWithItsText() {
        show { SectionLabel("Общи", Modifier.semantics { heading() }) }
        // Етикетът е с главни букви; заглавието и текстът — на един и същ възел.
        compose.onAllNodes(isHeading()).assertCountEquals(1)
        compose.onNode(isHeading() and hasText("ОБЩИ")).assertExists()
    }

    @Test fun sectionLabelWithoutHeadingIsStillOneNode() {
        show { SectionLabel("Библиотека") }
        compose.onAllNodes(isHeading()).assertCountEquals(0)
        compose.onNodeWithText("БИБЛИОТЕКА").assertExists()
    }

    @Test fun clickableCardIsAButton() {
        show { AppCard(onClick = {}) { Text("Карта") } }
        compose.onNode(hasClickAction()).assert(isButton).assert(hasText("Карта"))
    }

    @Test fun clickableCardWithActionLabelIsAButton() {
        show { AppCard(onClick = {}, onClickLabel = "Отвори") { Text("Карта") } }
        compose.onNode(hasClickAction()).assert(isButton)
    }

    @Test fun plainCardHasNoRole() {
        show { AppCard { Text("Карта") } }
        compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.Role)).assertCountEquals(0)
    }

    @Test fun tilesAreButtons() {
        show {
            TileGrid(
                listOf(
                    TileItem(Icons.Outlined.Info, "За нас", "История", onClick = {}),
                    TileItem(Icons.Outlined.Info, "Контакти", null, onClick = {}),
                ),
            )
        }
        compose.onAllNodes(hasClickAction()).assertCountEquals(2)
        compose.onAllNodes(hasClickAction() and isButton).assertCountEquals(2)
        compose.onNode(hasClickAction() and hasText("За нас")).assert(isButton)
    }
}
