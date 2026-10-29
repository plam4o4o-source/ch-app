package org.chyavorec.app.ui.screens.settings

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

/** Диалозите за избор в „Настройки“: редове поне 48 dp, радиобутони в една група („1 от 4“). */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class ChoiceDialogTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private val options = listOf(1 to "Малък", 2 to "Обичаен", 3 to "Голям", 4 to "Много голям")
    private val inGroup = hasAnyAncestor(SemanticsMatcher.keyIsDefined(SemanticsProperties.SelectableGroup))
    private val radio = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton)

    @Test fun rowsAreTallEnoughAndGrouped() {
        var picked: Int? = null
        compose.setContent {
            ChoiceDialog(title = "Размер на текста", options = options, selected = 2, onSelect = { picked = it }, onDismiss = {}, note = "Бележка")
        }
        compose.waitForIdle()
        compose.onAllNodes(isSelectable() and radio and inGroup).assertCountEquals(options.size)
        options.forEach { (_, label) ->
            compose.onNode(hasText(label) and isSelectable()).assertHeightIsAtLeast(48.dp)
        }
        compose.onNode(hasText("Обичаен") and isSelectable()).assertIsSelected()
        compose.onNode(hasText("Голям") and isSelectable()).assertIsNotSelected()
        // Бележката не е част от избора.
        compose.onAllNodes(hasText("Бележка") and isSelectable()).assertCountEquals(0)

        compose.onNode(hasText("Голям") and isSelectable()).performClick()
        compose.waitForIdle()
        assertEquals(3, picked)
    }
}
