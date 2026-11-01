package org.chyavorec.app.ui.components

import android.app.Application
import android.content.Intent
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.domain.model.Contacts
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Листът „Забравен ПИН“ / „Попитай библиотеката“: телефонът и имейлът са бутони с номера и
 * адреса; без известни контакти — къде да ги потърси.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xxhdpi")
class LibraryContactSheetTest {
    @get:Rule val compose = createComposeRule()
    private val app get() = ApplicationProvider.getApplicationContext<Application>()

    private fun contacts(phones: List<String>, emails: List<String>) = Contacts(
        organization = "Читалище", address = "", phones = phones, emails = emails, website = "", facebook = null,
        workingHours = emptyList(), mapQuery = "", fromSite = true, sourceUrl = null,
    )

    private fun shown(text: String) = compose.onAllNodes(hasText(text, substring = true)).fetchSemanticsNodes().isNotEmpty()

    private fun show(c: Contacts?) {
        compose.setContent {
            ChitalishteTheme {
                LibraryContactSheet(title = "Забравен ПИН", text = "Обяснение", contacts = c, onDismiss = {}, emailSubject = "Тема")
            }
        }
        compose.waitUntil(10_000) { shown("Обяснение") }
    }

    @Test fun phoneAndEmailAreButtons() {
        show(contacts(listOf("066 123 456"), listOf("lib@example.org")))
        val call = app.getString(R.string.contacts_call) + " · 066 123 456"
        val email = app.getString(R.string.contacts_email) + " · lib@example.org"
        compose.onNode(hasText(call) and hasClickAction()).performClick()
        val dial = assertNotNull(shadowOf(app).nextStartedActivity)
        assertEquals(Intent.ACTION_DIAL, dial.action)
        assertEquals("tel:066123456", dial.dataString)
        compose.onNode(hasText(email) and hasClickAction()).performClick()
        val mail = assertNotNull(shadowOf(app).nextStartedActivity)
        assertEquals("Тема", mail.getStringExtra(Intent.EXTRA_SUBJECT))
        assertFalse(shown(app.getString(R.string.login_forgot_no_contacts)))
    }

    @Test fun withoutContactsSaysWhereToFindThem() {
        show(contacts(emptyList(), emptyList()))
        assertTrue(shown(app.getString(R.string.login_forgot_no_contacts)))
        assertFalse(shown(app.getString(R.string.contacts_call)))
        assertFalse(shown(app.getString(R.string.contacts_email)))
    }
}
