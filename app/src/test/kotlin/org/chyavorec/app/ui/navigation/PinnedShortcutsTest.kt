package org.chyavorec.app.ui.navigation

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.Event
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Закачените преки пътища: какво се строи, как се чете обратно и какво никога не се отваря от чужд intent. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class PinnedShortcutsTest {
    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val base = "https://chyavorec.org"

    private fun pin(link: String) = Intent(LaunchIntents.ACTION_PIN, Uri.parse("chyavorec://app/$link")).setClassName(context, "org.chyavorec.app.MainActivity")

    @Test fun everySectionRoundTripsThroughTheLauncherIntent() {
        for (section in PinSection.entries) {
            val info = PinnedShortcuts.section(context, section)
            assertEquals(section.link, LaunchIntents.linkOf(info.intent, base), section.name)
            assertNotNull(DeepLinks.target(section.link, base), section.name)
        }
    }

    @Test fun sectionsOpenTheRightScreens() {
        assertEquals(LinkTarget.Screen(Routes.MY, listOf(Routes.WANT_TO_READ)), DeepLinks.target(PinSection.MY_BOOKS.link, base))
        assertEquals(DeepLinks.eventsTab(), DeepLinks.target(PinSection.EVENTS.link, base))
        assertEquals(DeepLinks.newsTab(), DeepLinks.target(PinSection.NEWS.link, base))
        assertEquals(LinkTarget.Screen(Routes.MORE, listOf(Routes.EMBROIDERY)), DeepLinks.target(PinSection.EMBROIDERY.link, base))
    }

    /** Картата се отваря през екрана на картата, който е зад заключването на личните екрани (UnlockGate в графа). */
    @Test fun readerCardOpensTheCardScreenAndLabelHasNoPersonalData() {
        assertEquals(LinkTarget.Screen(Routes.MY, listOf(Routes.CARD)), DeepLinks.target(PinSection.CARD.link, base))
        val info = PinnedShortcuts.section(context, PinSection.CARD)
        assertEquals("Читателска карта", info.shortLabel.toString())
        assertEquals("Читателска карта", info.longLabel.toString())
    }

    @Test fun bookShortcutOpensTheBookAboveTheCatalogWithTruncatedLabels() {
        val book = CatalogBook(inv = 399666, author = "Димитър Талев", title = "Железният светилник: роман в четири части за Преспа")
        val info = assertNotNull(PinnedShortcuts.book(context, book))
        assertEquals("pin-book-399666", info.id)
        assertEquals(DeepLinks.catalogBook(399666), LaunchIntents.linkOf(info.intent, base))
        assertEquals(LinkTarget.Screen(Routes.CATALOG, listOf(Routes.book(399666))), DeepLinks.target(LaunchIntents.linkOf(info.intent, base)!!, base))
        assertEquals("Железният светилник…", info.shortLabel.toString())
        assertTrue(info.longLabel!!.length <= 40)
        assertNull(PinnedShortcuts.book(context, CatalogBook(inv = 5, author = "x", title = "  ")))
    }

    @Test fun bookCoverIconIsSmallAndStableForTheSameBook() {
        val book = CatalogBook(inv = 7, author = "Иван Вазов", title = "Под игото")
        val bmp = PinnedShortcuts.coverIcon(context, book)
        assertEquals(PinnedShortcuts.ICON_PX, bmp.width)
        assertEquals(PinnedShortcuts.ICON_PX, bmp.height)
        assertTrue(bmp.byteCount <= 200_000, "растерът е ограничен: ${bmp.byteCount}")
        // Заглавие без буква или цифра не сгъва.
        PinnedShortcuts.coverIcon(context, CatalogBook(inv = 8, author = "", title = "…"))
    }

    @Test fun eventShortcutKeepsTheEncodedIdAndOpensTheEvent() {
        for (id in listOf("e1", "praznik-na-selo-2026", "a b/c?d=e&f#g", "събор 2026")) {
            val info = assertNotNull(PinnedShortcuts.event(context, Event(id = id, title = "Празник на селото и събор на народното творчество", date = "2026-09-26", sourceUrl = base)))
            val link = assertNotNull(LaunchIntents.linkOf(info.intent, base), id)
            assertEquals(DeepLinks.event(id), link)
            assertEquals(DeepLinks.eventsTab(listOf(Routes.event(id))), DeepLinks.target(link, base), id)
            assertTrue(info.shortLabel.length <= 20)
        }
    }

    @Test fun pinnedIntentsAcceptOnlyOwnLinks() {
        for (ok in listOf("events", "news", "catalog", "my/card", "my/want-to-read", "embroidery", "catalog/book/624", "event/abc%20d")) {
            assertEquals(ok, LaunchIntents.linkOf(pin(ok), base), ok)
        }
        for (bad in listOf(
            "login", "update", "my/loans", "my/history", "messages", "scan", "page?url=https://evil.example", "https://evil.example/", "",
            "catalog/book/0", "catalog/book/x", "catalog/book/12/x", "event/", "event/a/b", "event/a b", "book/624", "embroidery/x", "../card",
        )) assertNull(LaunchIntents.linkOf(pin(bad), base), bad)
        // Без схемата chyavorec и без хоста app — нищо.
        assertNull(LaunchIntents.linkOf(Intent(LaunchIntents.ACTION_PIN, Uri.parse("https://app/events")), base))
        assertNull(LaunchIntents.linkOf(Intent(LaunchIntents.ACTION_PIN, Uri.parse("chyavorec://evil/events")), base))
    }

    /** Закачените връзки не разширяват списъка на статичните преки пътища (там „events“ и „my/loans“ остават затворени). */
    @Test fun staticShortcutListIsUntouched() {
        val shortcut = Intent(LaunchIntents.ACTION_SHORTCUT, Uri.parse("chyavorec://app/events")).setClassName(context, "org.chyavorec.app.MainActivity")
        assertNull(LaunchIntents.linkOf(shortcut, base))
        assertEquals(setOf("catalog", "my/card", "scan", DeepLinks.WANT_TO_READ), LaunchIntents.SHORTCUT_LINKS)
    }

    @Test fun requestWithoutAShortcutDoesNothing() {
        assertEquals(false, PinnedShortcuts.request(context, null))
    }
}
