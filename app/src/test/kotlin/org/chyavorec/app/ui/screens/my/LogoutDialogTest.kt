package org.chyavorec.app.ui.screens.my

import android.app.Application
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Потвърждението на изхода: с бележки в дневника — отметка „Изтрий и бележките…“, сложена по
 * подразбиране; махната — дневникът остава. Без дневник отметка няма.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LogoutDialogTest {
    @get:Rule val compose = createComposeRule()
    private val app = ApplicationProvider.getApplicationContext<Application>()
    private val delete get() = app.getString(R.string.logout_delete_diary)
    private val logout get() = app.getString(R.string.action_logout)

    private fun show(hasDiary: Boolean): MutableList<Boolean> {
        val choices = mutableListOf<Boolean>()
        compose.setContent { ChitalishteTheme { LogoutDialog(hasDiary, onConfirm = { choices += it }, onDismiss = {}) } }
        return choices
    }

    @Test fun deleteIsTickedByDefault() {
        val choices = show(hasDiary = true)
        compose.onNodeWithText(delete).assertIsOn()
        compose.onNodeWithText(logout).performClick()
        assertEquals(listOf(false), choices, "по подразбиране дневникът се трие")
    }

    @Test fun untickingKeepsTheDiaryAndSaysSo() {
        val choices = show(hasDiary = true)
        compose.onNodeWithText(delete).performClick()
        compose.onNodeWithText(delete).assertIsOff()
        compose.onNodeWithText(app.getString(R.string.logout_diary_kept_note)).assertExists()
        compose.onNodeWithText(logout).performClick()
        assertEquals(listOf(true), choices)
    }

    @Test fun withoutDiaryEntriesThereIsNoCheckbox() {
        val choices = show(hasDiary = false)
        assertTrue(compose.onAllNodesWithText(delete).fetchSemanticsNodes().isEmpty())
        compose.onNodeWithText(logout).performClick()
        assertEquals(listOf(false), choices)
    }
}
