package org.chyavorec.app.ui.navigation

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Връзките от известия / преки пътища и адресите от сайта → екран (с раздела отдолу). */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class DeepLinksTest {
    private val base = "https://chyavorec.org"

    private fun target(link: String) = DeepLinks.target(link, base)

    // --- Връзки от известия и преки пътища ---

    @Test fun articleNotificationOpensTheArticleOverNews() {
        val id = "45-ти събор / Тимок"
        val link = DeepLinks.article(id)
        assertTrue(link.startsWith("article/"))
        assertEquals(LinkTarget.Screen(Routes.NEWS, listOf(Routes.article(id))), target(link))
    }

    @Test fun eventNotificationOpensTheEventOverEventsList() {
        val id = "event-03-03"
        assertEquals(
            LinkTarget.Screen(Routes.HOME, listOf(Routes.EVENTS, Routes.event(id))),
            target(DeepLinks.event(id)),
        )
    }

    @Test fun messageNotificationOpensMessagesAtThatMessage() {
        val id = "personal:42"
        assertEquals(LinkTarget.Screen(Routes.HOME, listOf(Routes.message(id))), target(DeepLinks.message(id)))
        assertEquals(LinkTarget.Screen(Routes.HOME, listOf(Routes.MESSAGES)), target(DeepLinks.MESSAGES))
    }

    @Test fun emptyIdsFallBackToTheList() {
        assertEquals(LinkTarget.Screen(Routes.NEWS), target("article/"))
        assertEquals(LinkTarget.Screen(Routes.HOME, listOf(Routes.EVENTS)), target("event/"))
        assertEquals(LinkTarget.Screen(Routes.HOME, listOf(Routes.MESSAGES)), target("message/"))
    }

    @Test fun legacyAndShortcutLinks() {
        // Известия от по-стари версии.
        assertEquals(LinkTarget.Screen(Routes.NEWS), target("news/12345"))
        assertEquals(LinkTarget.Screen(Routes.HOME, listOf(Routes.EVENTS)), target("events"))
        assertEquals(LinkTarget.Screen(Routes.MY, listOf(Routes.LOANS)), target("my/loans"))
        assertEquals(LinkTarget.Screen(Routes.MY, listOf(Routes.CARD)), target("my/card"))
        assertEquals(LinkTarget.Screen(Routes.MY, listOf(Routes.HISTORY)), target("my/history"))
        assertEquals(LinkTarget.Screen(Routes.MY, listOf(Routes.LOGIN)), target("login"))
        assertEquals(LinkTarget.Screen(Routes.CATALOG), target("catalog"))
        assertEquals(LinkTarget.Screen(Routes.CATALOG, listOf(Routes.SCAN)), target("scan"))
        assertEquals(LinkTarget.UpdatePrompt, target("update"))
        assertNull(target("nonsense"))
        assertNull(target(""))
    }

    @Test fun everyTargetHasATabUnderneath() {
        val links = listOf("article/x", "event/x", "message/x", "events", "messages", "my/loans", "scan", "login", "news")
        val tabs = TopTab.entries.map { it.route }.toSet()
        links.forEach { l ->
            val t = target(l) as LinkTarget.Screen
            assertTrue(t.tab in tabs, "$l → ${t.tab}")
        }
    }

    // --- Адреси от сайта (App Links и връзки в съдържанието) ---

    @Test fun siteUrlsMapToNativeScreens() {
        assertEquals(LinkTarget.Screen(Routes.HOME), DeepLinks.forUrl("$base/", base))
        assertEquals(LinkTarget.Screen(Routes.HOME), DeepLinks.forUrl(base, base))
        assertEquals(LinkTarget.Screen(Routes.CATALOG), DeepLinks.forUrl("$base/elektronen-katalog", base))
        assertEquals(LinkTarget.Screen(Routes.NEWS), DeepLinks.forUrl("$base/news/", base))
        assertEquals(LinkTarget.Screen(Routes.HOME, listOf(Routes.EVENTS)), DeepLinks.forUrl("$base/events", base))
        assertEquals(LinkTarget.Screen(Routes.HOME, listOf(Routes.CONTACTS)), DeepLinks.forUrl("$base/kontakti", base))
        assertEquals(LinkTarget.Screen(Routes.MORE, listOf(Routes.DOCUMENTS)), DeepLinks.forUrl("$base/load", base))
        assertEquals(LinkTarget.Screen(Routes.MORE, listOf(Routes.DOCUMENTS)), DeepLinks.forUrl("$base/istoricheski-publikacii", base))
        assertEquals(LinkTarget.Screen(Routes.MORE, listOf(Routes.PRIVACY)), DeepLinks.forUrl("$base/policy", base))
        assertEquals(LinkTarget.Screen(Routes.MORE, listOf(Routes.page("$base/history", ""))), DeepLinks.forUrl("$base/history", base))
    }

    @Test fun bookUrlOpensTheBookOverTheCatalog() {
        val url = DeepLinks.bookUrl("$base/", 399666)
        // Същият адрес като споделената от каталога на сайта връзка.
        assertEquals("$base/elektronen-katalog#zapis=399666", url)
        val book = LinkTarget.Screen(Routes.CATALOG, listOf(Routes.book(399666)))
        assertEquals(book, DeepLinks.forUrl(url, base))
        assertEquals(book, target(url))
        assertEquals(book, DeepLinks.forUrl("$base/elektronen-katalog?inv=399666", base))
        // Търсене (без книга) — каталогът.
        assertEquals(LinkTarget.Screen(Routes.CATALOG), DeepLinks.forUrl("$base/elektronen-katalog#q=вазов", base))
    }

    @Test fun articleUrlIsNormalizedToThePublicAddress() {
        val expected = LinkTarget.Screen(Routes.NEWS, listOf(Routes.article("$base/news/s-horo-i-pesen-vav-voditsa")))
        assertEquals(expected, DeepLinks.forUrl("$base/news/s-horo-i-pesen-vav-voditsa", base))
        // Заявка, котва, наклонена черта накрая и www не променят новината.
        assertEquals(expected, DeepLinks.forUrl("$base/news/s-horo-i-pesen-vav-voditsa/?utm_source=fb#top", base))
        assertEquals(expected, DeepLinks.forUrl("https://www.chyavorec.org/news/s-horo-i-pesen-vav-voditsa", base))
    }

    @Test fun filesForeignAndDeepPathsOpenInTheBrowser() {
        assertNull(DeepLinks.forUrl("$base/files/ustav.pdf", base))
        assertNull(DeepLinks.forUrl("$base/admin.html", base))
        assertNull(DeepLinks.forUrl("$base/api/calendar/x", base))
        assertNull(DeepLinks.forUrl("https://www.facebook.com/nchvasillevski1922/", base))
        assertNull(DeepLinks.forUrl("https://example.org/news/x", base))
    }

    /**
     * Друго приложение може да изпрати изричен intent с какъвто и да е адрес (или extra
     * „deep_link“) — чуждата страница не бива да се покаже като страница на читалището.
     */
    @Test fun spoofedSiteUrlsAreNotOpenedAsOurPages() {
        listOf(
            "https://chyavorec.org:443@evil.com/about",
            "https://chyavorec.org@evil.com/about",
            "https://evil.com\\@chyavorec.org/about",
            "https://evilchyavorec.org/about",
            "https://chyavorec.org.evil.com/about",
            "https://chyavorec%2Eorg/about",
            "http://chyavorec.org/about",
            "HTTP://chyavorec.org/about",
            "https://chyavorec.org:8443/about",
            "javascript:alert(1)//https://chyavorec.org/about",
            "intent://chyavorec.org/about#Intent;scheme=https;end",
            "content://chyavorec.org/about",
            "file:///chyavorec.org/about",
        ).forEach { url ->
            assertNull(DeepLinks.forUrl(url, base), url)
            assertNull(target(url), url)
        }
    }

    @Test fun sitePageIsLoadedFromTheConfiguredOrigin() {
        // www, главни букви и котва не сменят адреса, от който се зарежда страницата.
        val page = LinkTarget.Screen(Routes.MORE, listOf(Routes.page("$base/history", "")))
        assertEquals(page, DeepLinks.forUrl("https://WWW.chyavorec.org/history#top", base))
        assertEquals(page, DeepLinks.forUrl("HTTPS://chyavorec.org:443/history", base))
    }

    @Test fun webLinksGoThroughTheUrlMapping() {
        assertEquals(DeepLinks.forUrl("$base/events", base), target("$base/events"))
        assertNull(target("$base/files/ustav.pdf"))
    }

    /**
     * Всеки път, заявен като App Link в AndroidManifest.xml, трябва да води до екран —
     * иначе адресът би се отворил в приложението, без то да може да го покаже.
     */
    @Test fun everyAppLinkPathInTheManifestIsRoutable() {
        val doc = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
            .newDocumentBuilder().parse(File("src/main/AndroidManifest.xml"))
        val ns = "http://schemas.android.com/apk/res/android"
        val filters = doc.getElementsByTagName("intent-filter")
        val paths = mutableListOf<String>()
        val prefixes = mutableListOf<String>()
        val hosts = mutableSetOf<String>()
        for (i in 0 until filters.length) {
            val f = filters.item(i) as org.w3c.dom.Element
            if (f.getAttributeNS(ns, "autoVerify") != "true") continue
            val data = f.getElementsByTagName("data")
            for (j in 0 until data.length) {
                val d = data.item(j) as org.w3c.dom.Element
                d.getAttributeNS(ns, "host").takeIf { it.isNotEmpty() }?.let { hosts += it }
                d.getAttributeNS(ns, "path").takeIf { it.isNotEmpty() }?.let { paths += it }
                d.getAttributeNS(ns, "pathPrefix").takeIf { it.isNotEmpty() }?.let { prefixes += it }
                assertEquals("", d.getAttributeNS(ns, "pathPattern"), "само точни пътища и представки")
            }
        }
        assertEquals(setOf("chyavorec.org"), hosts)
        assertTrue(paths.size >= 10, "пътища: $paths")
        paths.forEach { p -> assertTrue(DeepLinks.forUrl("https://chyavorec.org$p", base) != null, "App Link $p няма екран") }
        prefixes.forEach { p -> assertTrue(DeepLinks.forUrl("https://chyavorec.org${p}primer", base) != null, "App Link $p* няма екран") }
    }
}
