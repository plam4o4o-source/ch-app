package org.chyavorec.app.ui.components

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrElse
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.screens.catalog.BookListItem
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.CatalogBook
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Екранният четец чете заглавието и наличността веднъж: корицата сама („За теб“) ги казва,
 * а до видимо заглавие/етикет е само украса. Наличността на екземпляра — етикет и отделен ред.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg")
class BookCoverSemanticsTest {
    @get:Rule val compose = createComposeRule()
    private val book = CatalogBook(1, "Вазов, Иван", "Под игото", available = false)

    private fun show(content: @Composable () -> Unit) = compose.setContent { ChitalishteTheme(mode = ThemeMode.LIGHT) { content() } }

    @Test fun standaloneCoverSaysTitleAndStatus() {
        show { BookCover(book) }
        compose.onNodeWithContentDescription("Под игото")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Не е на рафта"))
    }

    @Test fun decorativeCoverIsNotRead() {
        show { BookCover(book, decorative = true) }
        compose.onAllNodesWithContentDescription("Под игото").assertCountEquals(0)
        compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.StateDescription)).assertCountEquals(0)
    }

    @Test fun catalogRowReadsTheTitleOnce() {
        show { BookListItem(book, onClick = {}) }
        val row = compose.onNode(hasClickAction()).fetchSemanticsNode().config
        val texts = row.getOrElse(SemanticsProperties.Text) { emptyList() }.map { it.text }
        assertEquals(1, texts.count { it == "Под игото" }, "$texts")
        assertTrue(row.getOrElse(SemanticsProperties.ContentDescription) { emptyList() }.none { it == "Под игото" })
        // Етикетът се вижда — наличността не се казва и като състояние на корицата.
        assertEquals(1, texts.count { it == "Не е на рафта" }, "$texts")
        assertTrue(row.getOrElse(SemanticsProperties.StateDescription) { "" }.isEmpty())
    }

    @Test fun copyOutWithOthersOnShelfHasASeparateLine() {
        show { CopyStatus(BookStatus.NOT_ON_SHELF, othersOnShelf = 5) }
        compose.onNodeWithText("Не е на рафта").assertExists()
        compose.onNodeWithText("Още 5 на рафта").assertExists()
    }

    @Test fun copyStatusWithoutOthersIsOnlyTheLabel() {
        show { CopyStatus(BookStatus.ON_LOAN, othersOnShelf = 0) }
        compose.onNodeWithText("Заета").assertExists()
        compose.onAllNodesWithText("на рафта", substring = true).assertCountEquals(0)
    }
}
