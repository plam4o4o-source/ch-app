package org.chyavorec.app.ui.navigation

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.MainActivity
import org.chyavorec.app.notifications.Notifier
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Старото известие/адрес не се отваря отново при връщане от „Последни“ или пресъздаване. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class LaunchIntentsTest {

    @Test fun freshLaunchIsHandled() {
        assertTrue(LaunchIntents.shouldHandle(0, restored = false))
        // Флаговете на известието (NEW_TASK | CLEAR_TOP) не пречат.
        assertTrue(LaunchIntents.shouldHandle(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP, restored = false))
    }

    @Test fun relaunchFromRecentsIsIgnored() {
        // Android 8–11: задачата от „Последни“ получава отново първоначалния intent с този флаг.
        val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
        assertFalse(LaunchIntents.shouldHandle(flags, restored = false))
        assertFalse(LaunchIntents.shouldHandle(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY, restored = false))
    }

    @Test fun recreationIsIgnored() {
        // Завъртане / спрян процес — intent-ът е вече обработен.
        assertFalse(LaunchIntents.shouldHandle(0, restored = true))
        assertFalse(LaunchIntents.shouldHandle(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY, restored = true))
    }

    @Test fun recentsRelaunchOfARealNotificationIntentIsIgnored() {
        // Истински intent като от Notifier, после подаден отново от „Последни“.
        val fromNotification = Intent(Intent.ACTION_VIEW)
            .setComponent(ComponentName("org.chyavorec.app", MainActivity::class.java.name))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra(Notifier.EXTRA_DEEP_LINK, DeepLinks.article("42"))
        assertTrue(LaunchIntents.shouldHandle(fromNotification.flags, restored = false))
        val fromRecents = Intent(fromNotification).addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
        assertFalse(LaunchIntents.shouldHandle(fromRecents.flags, restored = false))
    }

    @Test fun consumedIntentCarriesNothingToOpen() {
        val component = ComponentName("org.chyavorec.app", MainActivity::class.java.name)
        val original = Intent(Intent.ACTION_VIEW, Uri.parse("https://chyavorec.org/news/x"))
            .setComponent(component)
            .putExtra(Notifier.EXTRA_DEEP_LINK, "events")
        val plain = LaunchIntents.consumed(original)
        assertEquals(Intent.ACTION_MAIN, plain.action)
        assertEquals(component, plain.component)
        assertNull(plain.data)
        assertNull(plain.getStringExtra(Notifier.EXTRA_DEEP_LINK))
        // Оригиналът остава непокътнат (новият intent е отделен обект).
        assertEquals("events", original.getStringExtra(Notifier.EXTRA_DEEP_LINK))
    }

    private val base = "https://chyavorec.org"
    private val context get() = androidx.test.core.app.ApplicationProvider.getApplicationContext<Application>()
    private val main = ComponentName("org.chyavorec.app", MainActivity::class.java.name)

    /** Собствените известия и уиджетът — през неекспортирания вход, с връзката в extra. */
    @Test fun ownNotificationLinksAreHonoured() {
        val own = LaunchIntents.openLink(context, DeepLinks.article("42"))
        assertEquals(LaunchIntents.OPEN_LINK_ALIAS, own.component?.className)
        assertEquals(LaunchIntents.ACTION_OPEN_LINK, own.action)
        assertEquals(DeepLinks.article("42"), LaunchIntents.linkOf(own, base))
        assertEquals("my/loans", LaunchIntents.linkOf(LaunchIntents.openLink(context, "my/loans"), base))
        // Различните връзки дават различни PendingIntent-и (filterEquals гледа и data).
        assertFalse(own.filterEquals(LaunchIntents.openLink(context, "events")))
        assertNull(LaunchIntents.linkOf(LaunchIntents.openLink(context, null), base))
    }

    /** MainActivity е exported: чужд intent с extra „deep_link“ не отваря вход, обновяване или чужд адрес. */
    @Test fun deepLinkExtraFromOtherAppsIsIgnored() {
        for (action in listOf(null, Intent.ACTION_MAIN, Intent.ACTION_VIEW, LaunchIntents.ACTION_OPEN_LINK, LaunchIntents.ACTION_SHORTCUT)) {
            for (link in listOf("my/card", "login", "update", "https://evil.example/phish")) {
                val spoofed = Intent().setAction(action).setComponent(main).putExtra(Notifier.EXTRA_DEEP_LINK, link)
                assertNull(LaunchIntents.linkOf(spoofed, base), "$action / $link")
            }
        }
    }

    /**
     * Известие, показано от v1.2.25 преди обновяването: `Intent(context, MainActivity)` без
     * действие, с extra „deep_link“ — отваря се, но само с връзките, които такива известия носят
     * (съобщенията са `message/<id>`; `messages` и `book/…` v1.2.25 не е изпращала).
     */
    @Test fun legacyNotificationIntentsOpenOnlyAllowListedLinks() {
        fun legacy(link: String) = Intent(context, MainActivity::class.java).putExtra(Notifier.EXTRA_DEEP_LINK, link)
        for (link in listOf("my/loans", DeepLinks.event("event-03-03"), DeepLinks.article("45-ти събор / Тимок"), DeepLinks.message("m-17"), DeepLinks.message("p:42"))) {
            assertEquals(link, LaunchIntents.linkOf(legacy(link), base), link)
            assertTrue(DeepLinks.target(link, base) is LinkTarget.Screen, link)
        }
        for (link in listOf("my/card", "login", "update", "catalog", "scan", "events", "event/", "article/ ", "message/", "message/ ", "messages", DeepLinks.book(399666), "$base/about", "https://evil.example/phish", ""))
            assertNull(LaunchIntents.linkOf(legacy(link), base), link)
        // Без изричния компонент MainActivity — нищо (и неекспортираният вход иска своето действие).
        assertNull(LaunchIntents.linkOf(Intent().putExtra(Notifier.EXTRA_DEEP_LINK, "my/loans"), base))
        assertNull(LaunchIntents.linkOf(Intent().setComponent(ComponentName("org.chyavorec.app", "org.chyavorec.app.Other")).putExtra(Notifier.EXTRA_DEEP_LINK, "my/loans"), base))
        assertNull(LaunchIntents.linkOf(Intent().setComponent(ComponentName("org.chyavorec.app", LaunchIntents.OPEN_LINK_ALIAS)).putExtra(Notifier.EXTRA_DEEP_LINK, "my/loans"), base))
    }

    @Test fun shortcutsOpenOnlyTheirOwnLinks() {
        fun shortcut(link: String) = Intent(LaunchIntents.ACTION_SHORTCUT, Uri.parse("chyavorec://app/$link")).setComponent(main)
        for (link in LaunchIntents.SHORTCUT_LINKS) assertEquals(link, LaunchIntents.linkOf(shortcut(link), base))
        for (link in listOf("login", "update", "my/loans", "page?url=https://evil.example", "")) assertNull(LaunchIntents.linkOf(shortcut(link), base), link)
        assertNull(LaunchIntents.linkOf(Intent(LaunchIntents.ACTION_SHORTCUT, Uri.parse("https://app/catalog")).setComponent(main), base))
    }

    /** Динамичните преки пътища: „Искам да прочета“ и книга от каталога — само с положителен инв. №, нищо друго. */
    @Test fun dynamicShortcutLinksAreStrict() {
        fun shortcut(link: String) = Intent(LaunchIntents.ACTION_SHORTCUT, Uri.parse("chyavorec://app/$link")).setComponent(main)
        for (link in listOf(DeepLinks.WANT_TO_READ, DeepLinks.catalogBook(624), DeepLinks.catalogBook(399666)))
            assertEquals(link, LaunchIntents.linkOf(shortcut(link), base), link)
        for (link in listOf(
            "catalog/book/", "catalog/book/0", "catalog/book/-1", "catalog/book/012", "catalog/book/12x", "catalog/book/1 2",
            "catalog/book/12/x", "catalog/book/99999999999999999999", "book/624", "my/want", "my/want-to-read/x", "catalog/books/1",
        )) assertNull(LaunchIntents.linkOf(shortcut(link), base), link)
        // Intent-ът, който приложението публикува, минава през същия вход.
        assertEquals(DeepLinks.catalogBook(7), LaunchIntents.linkOf(AppShortcuts.intent(context, DeepLinks.catalogBook(7)), base))
        assertEquals(DeepLinks.WANT_TO_READ, LaunchIntents.linkOf(AppShortcuts.intent(context, DeepLinks.WANT_TO_READ), base))
    }

    /** Адрес отвън — само от сайта и само с native екран; чужд адрес не се отваря в Custom Tab с цветовете на приложението. */
    @Test fun externalViewIntentsAcceptOnlyRecognisedSiteLinks() {
        fun view(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).setComponent(main)
        for (url in listOf("$base/news/x", "$base/events", "$base/elektronen-katalog#zapis=12", "$base/about"))
            assertEquals(url, LaunchIntents.linkOf(view(url), base), url)
        for (url in listOf("https://evil.example/", "https://chyavorec.org@evil.com/about", "$base/files/ustav.pdf", "$base/api/x/y", "http://chyavorec.org/news", "chyavorec://app/login"))
            assertNull(LaunchIntents.linkOf(view(url), base), url)
    }
}
