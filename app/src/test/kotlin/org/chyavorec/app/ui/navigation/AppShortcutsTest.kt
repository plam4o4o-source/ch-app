package org.chyavorec.app.ui.navigation

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.chyavorec.domain.model.CatalogBook
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.xmlpull.v1.XmlPullParser
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Преките пътища: три статични („Сканирай“ първи) и един динамичен — последната книга от каталога. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class AppShortcutsTest {
    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val base = "https://chyavorec.org"
    private val ns = "http://schemas.android.com/apk/res/android"

    private fun dynamic() = ShortcutManagerCompat.getDynamicShortcuts(context).associateBy { it.id }

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

    /** Същата книга отново (връщане към екрана, завъртане) — без нова заявка към системата. */
    @Test fun sameBookIsNotPushedAgain() {
        val book = CatalogBook(inv = 7, author = "Иван Вазов", title = "Под игото")
        assertTrue(AppShortcuts.publicBookOpened(context, book))
        assertFalse(AppShortcuts.publicBookOpened(context, book))
        assertTrue(AppShortcuts.publicBookOpened(context, CatalogBook(inv = 8, author = "", title = "Тютюн")))
        assertTrue(AppShortcuts.publicBookOpened(context, book))
        // След махането (изход, изчистване) същата книга се публикува отново.
        AppShortcuts.removeLastBook(context)
        assertTrue(AppShortcuts.publicBookOpened(context, book))
    }

    @Test fun removeLastBookDropsTheShortcut() {
        AppShortcuts.publicBookOpened(context, CatalogBook(inv = 7, author = "Иван Вазов", title = "Под игото"))
        assertTrue(AppShortcuts.ID_LAST_BOOK in dynamic())
        AppShortcuts.removeLastBook(context)
        assertTrue(AppShortcuts.ID_LAST_BOOK !in dynamic())
    }

    /** Динамичният „Искам да прочета“ от по-старите версии се маха след обновяването. */
    @Test fun legacyWantToReadShortcutIsRemoved() {
        val legacy = ShortcutInfoCompat.Builder(context, AppShortcuts.ID_WANT_TO_READ)
            .setShortLabel("Искам да прочета")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_book))
            .setIntent(AppShortcuts.intent(context, DeepLinks.WANT_TO_READ))
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, legacy)
        AppShortcuts.publicBookOpened(context, CatalogBook(inv = 7, author = "Иван Вазов", title = "Под игото"))
        AppShortcuts.removeLegacy(context)
        assertEquals(setOf(AppShortcuts.ID_LAST_BOOK), dynamic().keys)
    }

    /** Статичните: „Сканирай“, картата, каталогът — с динамичния общо 4 (колкото показват launcher-ите). */
    @Test fun staticShortcutsAreScanCardCatalog() {
        val parser = context.resources.getXml(R.xml.shortcuts)
        val ids = mutableListOf<String>()
        val links = mutableListOf<String?>()
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "shortcut" -> ids += parser.getAttributeValue(ns, "shortcutId")
                "intent" -> {
                    val intent = Intent(parser.getAttributeValue(ns, "action"), Uri.parse(parser.getAttributeValue(ns, "data")))
                        .setClassName(context, parser.getAttributeValue(ns, "targetClass"))
                    links += LaunchIntents.linkOf(intent, base)
                }
            }
        }
        assertEquals(listOf(AppShortcuts.ID_SCAN, AppShortcuts.ID_CARD, AppShortcuts.ID_CATALOG), ids)
        assertEquals(listOf("scan", "my/card", "catalog"), links)
        assertTrue(ids.size + 1 <= 4)
    }

    /** Отваряне от пряк път се отчита с ID-то му; друга връзка — не. */
    @Test fun launchedLinksMapToShortcutIds() {
        assertEquals(AppShortcuts.ID_SCAN, AppShortcuts.idOf("scan"))
        assertEquals(AppShortcuts.ID_CARD, AppShortcuts.idOf("my/card"))
        assertEquals(AppShortcuts.ID_CATALOG, AppShortcuts.idOf("catalog"))
        assertEquals(AppShortcuts.ID_LAST_BOOK, AppShortcuts.idOf(DeepLinks.catalogBook(624)))
        assertEquals(AppShortcuts.ID_WANT_TO_READ, AppShortcuts.idOf(DeepLinks.WANT_TO_READ))
        for (link in listOf("events", "catalog/book/0", "catalog/book/x", "my/loans", "")) assertNull(AppShortcuts.idOf(link), link)
    }

    @Test fun ellipsizeKeepsShortTextsAndCutsAtWords() {
        assertEquals("Под игото", AppShortcuts.ellipsize("  Под игото ", 20))
        assertEquals("Железният…", AppShortcuts.ellipsize("Железният светилник", 15))
        assertEquals("Абвгдежзийк…", AppShortcuts.ellipsize("Абвгдежзийклмнопрст", 12))
        assertEquals("Железният светилник…", AppShortcuts.ellipsize("Железният светилник: роман", 20))
    }
}
