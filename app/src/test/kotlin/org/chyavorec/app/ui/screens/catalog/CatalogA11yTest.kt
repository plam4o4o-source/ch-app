package org.chyavorec.app.ui.screens.catalog

import android.app.Application
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.click
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.domain.model.CatalogBook
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Достъпност в каталога: „Само налични“ е един превключвател с име (и текстът го превключва);
 * гръбчетата на полицата са поне 48 dp за TalkBack и докосване, а екземплярите на едно заглавие
 * се различават („екземпляр 2 от 3“).
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class CatalogA11yTest {
    @get:Rule val compose = createComposeRule()

    private val isSwitch = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch)
    private val isButton = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)

    @Test fun onlyAvailableIsOneLabelledSwitch() {
        var on by mutableStateOf(false)
        compose.setContent { ChitalishteTheme { OnlyAvailableRow(on) { on = it } } }
        // Един възел: превключвател с името си (не „превключвател, изключен“ без име).
        assertEquals(1, compose.onAllNodes(isSwitch).fetchSemanticsNodes().size)
        compose.onNode(isSwitch).assert(hasText("Само налични")).assert(hasClickAction()).assertIsOff().assertHeightIsAtLeast(48.dp)
        assertEquals(1, compose.onAllNodes(hasClickAction()).fetchSemanticsNodes().size, "самият превключвател не е отделна цел")
        // Докосване на текста превключва.
        compose.onNodeWithText("Само налични", useUnmergedTree = true).performClick()
        assertTrue(on)
        compose.onNode(isSwitch).assertIsOn()
        compose.onNodeWithText("Само налични", useUnmergedTree = true).performClick()
        assertFalse(on)
    }

    @Test fun shelfSpinesAreWideEnoughAndDistinct() {
        val opened = mutableListOf<Long>()
        val books = listOf(
            CatalogBook(1, "Николай Симеонов", "Белият дом"),
            CatalogBook(2, "Николай Симеонов", "Белият дом"),
            CatalogBook(3, "Николай Симеонов", "Белият дом"),
            CatalogBook(4, "Иван Вазов", "Чичовци"),
        )
        compose.setContent {
            ChitalishteTheme {
                CatalogShelfView(books, rememberLazyListState(), header = {}, canLoadMore = false, onLoadMore = {}, onOpen = { opened += it.inv })
            }
        }
        val spines = compose.onAllNodes(isButton and hasClickAction()).fetchSemanticsNodes()
        assertEquals(4, spines.size)
        val labels = spines.map { it.config[SemanticsProperties.ContentDescription].single() }
        assertEquals(labels.size, labels.toSet().size, "еднакви етикети: $labels")
        assertTrue("Белият дом, Николай Симеонов, екземпляр 2 от 3" in labels, "$labels")
        assertTrue("Чичовци, Иван Вазов" in labels, "$labels")
        val min = with(compose.density) { 48.dp.toPx() } - 0.5f
        spines.forEach { assertTrue(it.boundsInRoot.width >= min, "${it.boundsInRoot.width} px: ${it.config}") }
        // TalkBack: двойно докосване отваря точно този екземпляр.
        compose.onNode(isButton and SemanticsMatcher.expectValue(SemanticsProperties.ContentDescription, listOf("Белият дом, Николай Симеонов, екземпляр 2 от 3"))).performClick()
        assertEquals(listOf(2L), opened)
        // Докосване близо до десния ръб на първото гръбче (в зоната, която застъпва второто) — първото.
        val first = spines.first { it.config[SemanticsProperties.ContentDescription].single().endsWith("екземпляр 1 от 3") }.boundsInRoot
        val x = first.center.x + with(compose.density) { 12.dp.toPx() }
        compose.onRoot().performTouchInput { click(Offset(x, first.center.y)) }
        assertEquals(listOf(2L, 1L), opened)
    }
}
