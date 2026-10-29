package org.chyavorec.app.ui.navigation

import android.app.Application
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.chyavorec.domain.model.CatalogBook
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Динамичните преки пътища: „Искам да прочета“ и последната книга от каталога (една, подменя се). */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class AppShortcutsTest {
    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val base = "https://chyavorec.org"

    private fun dynamic() = ShortcutManagerCompat.getDynamicShortcuts(context).associateBy { it.id }

    @Test fun wantToReadShortcutOpensTheList() {
        AppShortcuts.wantToReadOpened(context)
        val s = dynamic().getValue(AppShortcuts.ID_WANT_TO_READ)
        assertEquals(context.getString(R.string.shortcut_want_to_read), s.shortLabel.toString())
        assertEquals(context.getString(R.string.shortcut_want_to_read_long), s.longLabel.toString())
        val link = LaunchIntents.linkOf(s.intent, base)
        assertEquals(DeepLinks.WANT_TO_READ, link)
        assertEquals(LinkTarget.Screen(Routes.MY, listOf(Routes.WANT_TO_READ)), DeepLinks.target(link!!, base))
    }

    @Test fun lastPublicBookReplacesThePreviousOneWithATruncatedTitle() {
        AppShortcuts.publicBookOpened(context, CatalogBook(inv = 7, author = "Иван Вазов", title = "Под игото"))
        AppShortcuts.publicBookOpened(
            context,
            CatalogBook(inv = 624, author = "Димитър Талев", title = "Железният светилник: роман в четири части за Преспа"),
        )
        val books = ShortcutManagerCompat.getDynamicShortcuts(context).filter { it.id == AppShortcuts.ID_LAST_BOOK }
        val s = books.single()
        assertEquals(DeepLinks.catalogBook(624), LaunchIntents.linkOf(s.intent, base))
        assertEquals("Железният светилник…", s.shortLabel.toString())
        assertTrue(s.longLabel!!.length <= 40, s.longLabel.toString())
        // Книгата се отваря над каталога и не като лична.
        assertEquals(LinkTarget.Screen(Routes.CATALOG, listOf(Routes.book(624))), DeepLinks.target(DeepLinks.catalogBook(624), base))
    }

    @Test fun ellipsizeKeepsShortTextsAndCutsAtWords() {
        assertEquals("Под игото", AppShortcuts.ellipsize("  Под игото ", 20))
        assertEquals("Железният…", AppShortcuts.ellipsize("Железният светилник", 15))
        assertEquals("Абвгдежзийк…", AppShortcuts.ellipsize("Абвгдежзийклмнопрст", 12))
        assertEquals("Железният светилник…", AppShortcuts.ellipsize("Железният светилник: роман", 20))
    }
}
