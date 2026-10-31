package org.chyavorec.app.notifications

import android.Manifest
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.provider.CalendarContract
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.NewsArticle
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Известията с лично съдържание: махат се при изход и са общи на заключен екран. Известията за
 * заемания: категория „напомняне“, обща група с обобщение при 2+, бутон „Удължи“.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class NotifierTest {
    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val nm get() = context.getSystemService(NotificationManager::class.java)

    @Before fun setUp() {
        shadowOf(context).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        Notifier.createChannels(context)
    }

    @Test fun cancelPersonalLeavesPublicNotifications() {
        Notifier.show(context, Channel.LOANS, NotificationIds.dueSoon("L1"), "Под игото", "Срок утре", "my/loans")
        Notifier.show(context, Channel.MESSAGES, NotificationIds.message("p:7"), "Лично", "Запазена книга", null, sensitive = true)
        Notifier.show(context, Channel.MESSAGES, NotificationIds.message("m1"), "Съобщение", "За всички", null)
        Notifier.show(context, Channel.NEWS, NotificationIds.NEWS, "Новина", "Текст", null)
        // Скрито на заключен екран, но не е на читателя от сесията — остава след изход.
        Notifier.show(context, Channel.LIBRARY, NotificationIds.wantToRead(624), "На рафта", "Белият дома", null, sensitive = true, readerData = false)
        assertEquals(5, nm.activeNotifications.size)
        assertEquals(Notification.VISIBILITY_PRIVATE, nm.activeNotifications.single { it.id == NotificationIds.wantToRead(624) }.notification.visibility)

        Notifier.cancelPersonal(context)

        assertEquals(
            setOf(NotificationIds.message("m1"), NotificationIds.NEWS, NotificationIds.wantToRead(624)),
            nm.activeNotifications.map { it.id }.toSet(),
        )
    }

    /**
     * Личните известия имат обща публична версия за заключения екран — без заглавието на
     * книгата и без текста на съобщението (системата я показва, когато скрива чувствителното съдържание).
     */
    @Test fun personalNotificationsHaveGenericLockScreenVersion() {
        Notifier.show(context, Channel.LOANS, NotificationIds.dueSoon("L1"), "Наближава срок за връщане", "Под игото — до 21.10", "my/loans")
        Notifier.show(
            context, Channel.LOANS, NotificationIds.overdue("L2"), "Просрочена книга", "Тютюн", "my/loans",
            lockScreenTitle = context.getString(R.string.notif_public_overdue),
        )
        Notifier.show(context, Channel.MESSAGES_IMPORTANT, NotificationIds.message("p:7"), "Запазена книга", "Книгата те чака", null, sensitive = true)
        Notifier.show(context, Channel.NEWS, NotificationIds.NEWS, "Новина", "Текст", null)
        val byId = nm.activeNotifications.associate { it.id to it.notification }

        fun publicOf(id: Int): Notification {
            val n = byId.getValue(id)
            assertEquals(Notification.VISIBILITY_PRIVATE, n.visibility)
            return assertNotNull(n.publicVersion, "липсва публична версия за $id")
        }
        val loan = publicOf(NotificationIds.dueSoon("L1"))
        assertEquals(context.getString(R.string.notif_public_loan), loan.extras.getCharSequence(Notification.EXTRA_TITLE).toString())
        val overdue = publicOf(NotificationIds.overdue("L2"))
        assertEquals(context.getString(R.string.notif_public_overdue), overdue.extras.getCharSequence(Notification.EXTRA_TITLE).toString())
        val message = publicOf(NotificationIds.message("p:7"))
        assertEquals(context.getString(R.string.notif_public_message), message.extras.getCharSequence(Notification.EXTRA_TITLE).toString())
        // Нищо лично в публичните версии.
        listOf(loan, overdue, message).forEach { p ->
            val all = listOf(Notification.EXTRA_TITLE, Notification.EXTRA_TEXT, Notification.EXTRA_BIG_TEXT, Notification.EXTRA_SUB_TEXT)
                .joinToString(" ") { p.extras.getCharSequence(it)?.toString().orEmpty() }
            listOf("Под игото", "Тютюн", "Запазена книга", "Книгата те чака").forEach { assertFalse(it in all, "„$it“ се вижда на заключен екран") }
        }
        // Общите известия са публични и без отделна версия.
        val news = byId.getValue(NotificationIds.NEWS)
        assertEquals(Notification.VISIBILITY_PUBLIC, news.visibility)
        assertNull(news.publicVersion)
    }

    @Test fun loanNotificationsAreGroupedRemindersWithSummary() {
        Notifier.show(context, Channel.LOANS, NotificationIds.dueSoon("L1"), "Наближава срок за връщане", "Под игото", "my/loans")
        Notifier.show(context, Channel.NEWS, NotificationIds.NEWS, "Новина", "Текст", null)
        // Едно известие — без обобщение.
        assertNull(nm.activeNotifications.firstOrNull { it.id == NotificationIds.LOANS_SUMMARY })
        val first = nm.activeNotifications.single { it.id == NotificationIds.dueSoon("L1") }.notification
        assertEquals(Notification.CATEGORY_REMINDER, first.category)
        assertEquals(Notifier.LOANS_GROUP, first.group)
        assertNull(nm.activeNotifications.single { it.id == NotificationIds.NEWS }.notification.group)

        Notifier.show(context, Channel.LOANS, NotificationIds.overdue("L2"), "Просрочена книга", "Тютюн", "my/loans")
        val summary = nm.activeNotifications.single { it.id == NotificationIds.LOANS_SUMMARY }.notification
        assertEquals(Notifier.LOANS_GROUP, summary.group)
        assertTrue(summary.flags and Notification.FLAG_GROUP_SUMMARY != 0)
        assertEquals(Channel.LOANS.id, summary.channelId)
        // Обобщението е общо — без заглавия на книги — и се маха при изход.
        val text = listOf(Notification.EXTRA_TITLE, Notification.EXTRA_TEXT).joinToString(" ") { summary.extras.getCharSequence(it)?.toString().orEmpty() }
        assertFalse("Под игото" in text || "Тютюн" in text)
        Notifier.cancelPersonal(context)
        assertEquals(listOf(NotificationIds.NEWS), nm.activeNotifications.map { it.id })
    }

    @Test fun renewActionIsImmutableBroadcastToPrivateReceiver() {
        val loan = Loan("L1", 7, "Под игото", "Иван Вазов", dueOn = "2026-10-20", canRenew = true)
        val id = NotificationIds.dueSoon(loan.loanId)
        Notifier.show(
            context, Channel.LOANS, id, "Наближава срок за връщане", "Под игото", "my/loans",
            actions = listOf(RenewActionReceiver.action(context, loan, id, "R1")),
        )
        val action = nm.activeNotifications.single { it.id == id }.notification.actions.single()
        assertEquals(context.getString(R.string.notif_action_renew), action.title.toString())
        val pending = shadowOf(action.actionIntent)
        assertTrue(pending.isBroadcastIntent)
        assertTrue(pending.isImmutable)
        assertEquals(id, pending.requestCode)
        val intent = pending.savedIntent
        assertEquals(ComponentName(context, RenewActionReceiver::class.java), intent.component)
        assertEquals(RenewActionReceiver.ACTION_RENEW, intent.action)
        assertEquals("L1", intent.getStringExtra(RenewActionReceiver.EXTRA_LOAN))
        assertEquals(id, intent.getIntExtra(RenewActionReceiver.EXTRA_NOTIFICATION, 0))
        // Заявката — само от името на читателя на известието.
        assertEquals("R1", intent.getStringExtra(RenewActionReceiver.EXTRA_READER))
        // Друга книга — друг requestCode (бутоните не се препокриват).
        val other = RenewActionReceiver.action(context, loan.copy(loanId = "L2"), NotificationIds.dueSoon("L2"), "R1")
        assertNotEquals(id, shadowOf(other.actionIntent).requestCode)
        // Приемникът не е достъпен за други приложения.
        val info = context.packageManager.getReceiverInfo(ComponentName(context, RenewActionReceiver::class.java), 0)
        assertFalse(info.exported)
    }

    @Test fun statusCategoryAndSilentOverride() {
        Notifier.show(
            context, Channel.LOANS, NotificationIds.renew("L1"), "Срокът е удължен", "Под игото", "my/loans",
            silent = true, category = NotificationCompat.CATEGORY_STATUS,
        )
        val n = nm.activeNotifications.single { it.id == NotificationIds.renew("L1") }.notification
        assertEquals(Notification.CATEGORY_STATUS, n.category)
        assertTrue(n.actions.isNullOrEmpty())
    }

    /**
     * Каналите са в три групи в системните настройки; ID-тата им са старите (настройките остават).
     * Android подрежда групите по ID: „Моята библиотека“, „Читалището“, „Приложението“.
     */
    @Test fun channelsAreGroupedWithUnchangedIds() {
        val selfUpdate = org.chyavorec.app.BuildConfig.SELF_UPDATE
        val expected = mapOf(
            "loans" to "1_my_library", "library" to "1_my_library",
            "news" to "2_centre", "events" to "2_centre",
            "messages" to "2_centre", "messages_important" to "2_centre",
        ) + (if (selfUpdate) mapOf("updates" to "3_app") else emptyMap())
        assertEquals(expected, nm.notificationChannels.associate { it.id to it.group })
        val names = nm.notificationChannelGroups.sortedBy { it.id }.map { it.name.toString() }
        assertEquals(
            listOf(R.string.channel_group_my_library, R.string.channel_group_centre).map { context.getString(it) } +
                (if (selfUpdate) listOf(context.getString(R.string.channel_group_app)) else emptyList()),
            names,
        )
        // Без обновяване от приложението (Play) — без празна група „Приложението“.
        assertEquals(selfUpdate, nm.notificationChannelGroups.any { it.id == "3_app" })
        assertEquals(context.getString(R.string.channel_library), nm.getNotificationChannel("library").name.toString())
    }

    private fun template(n: Notification) = n.extras.getString(Notification.EXTRA_TEMPLATE)
    private fun hasPicture(n: Notification) =
        n.extras.containsKey(Notification.EXTRA_PICTURE) || n.extras.containsKey(Notification.EXTRA_PICTURE_ICON)

    /** Новина/събитие със снимка — BigPictureStyle с малката снимка и текста; без снимка — BigTextStyle. */
    @Test fun generalNotificationWithImageIsBigPicture() {
        val image = Bitmap.createBitmap(64, 36, Bitmap.Config.ARGB_8888)
        Notifier.show(context, Channel.NEWS, NotificationIds.NEWS, "Нова публикация", "Изложба", "article/n1", image = image)
        Notifier.show(context, Channel.EVENTS, NotificationIds.event("e1"), "Напомняне за събитие", "Концерт", "event/e1")
        val news = nm.activeNotifications.single { it.id == NotificationIds.NEWS }.notification
        assertEquals(Notification.BigPictureStyle::class.java.name, template(news))
        assertTrue(hasPicture(news))
        assertNotNull(news.getLargeIcon())
        assertEquals("Изложба", news.extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT).toString())
        val event = nm.activeNotifications.single { it.id == NotificationIds.event("e1") }.notification
        assertEquals(Notification.BigTextStyle::class.java.name, template(event))
        assertFalse(hasPicture(event))
    }

    /** Заемания и лични съобщения — никога снимка (дори подадена по погрешка): остават само с текст. */
    @Test fun personalNotificationsNeverShowImages() {
        val image = Bitmap.createBitmap(64, 36, Bitmap.Config.ARGB_8888)
        Notifier.show(context, Channel.LOANS, NotificationIds.dueSoon("L1"), "Наближава срок", "Под игото", "my/loans", image = image)
        Notifier.show(context, Channel.MESSAGES, NotificationIds.message("p:1"), "Лично", "Текст", null, sensitive = true, image = image)
        Notifier.show(context, Channel.LIBRARY, NotificationIds.wantToRead(7), "На рафта", "Тютюн", null, sensitive = true, readerData = false, image = image)
        listOf(NotificationIds.dueSoon("L1"), NotificationIds.message("p:1"), NotificationIds.wantToRead(7)).forEach { id ->
            val n = nm.activeNotifications.single { it.id == id }.notification
            assertEquals(Notification.BigTextStyle::class.java.name, template(n), "известие $id")
            assertFalse(hasPicture(n))
            assertNull(n.getLargeIcon())
        }
    }

    private fun registerActivity(intent: Intent, pkg: String) {
        shadowOf(context.packageManager).addResolveInfoForIntent(
            intent,
            // isDefault — заявката е с MATCH_DEFAULT_ONLY (както при startActivity).
            ResolveInfo().apply { isDefault = true; activityInfo = ActivityInfo().apply { packageName = pkg; name = "$pkg.Main" } },
        )
    }

    /**
     * „Добави в календара“ и „Маршрут“ отварят направо чуждото activity (без трамплин през
     * BroadcastReceiver), с неизменим PendingIntent; без място — без „Маршрут“.
     */
    @Test fun eventActionsOpenCalendarAndMapDirectly() {
        registerActivity(Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI), "com.example.calendar")
        registerActivity(Intents.mapIntent("Голям салон, с. Яворец"), "com.example.maps")
        val event = Event("e1", "Концерт", "2026-10-20", time = "18:00", place = "Голям салон", sourceUrl = "https://chyavorec.org/events")
        val id = NotificationIds.event(event.id)
        val actions = RichNotifications.eventActions(context, event, ZoneId.of("Europe/Sofia"), id)
        Notifier.show(context, Channel.EVENTS, id, "Напомняне за събитие", event.title, "event/e1", actions = actions)
        val shown = nm.activeNotifications.single { it.id == id }.notification.actions
        assertEquals(
            listOf(context.getString(R.string.event_add_calendar), context.getString(R.string.notif_action_route)),
            shown.map { it.title.toString() },
        )
        val calendar = shadowOf(shown[0].actionIntent)
        assertTrue(calendar.isActivityIntent)
        assertTrue(calendar.isImmutable)
        assertEquals(Intent.ACTION_INSERT, calendar.savedIntent.action)
        assertEquals("Концерт", calendar.savedIntent.getStringExtra(CalendarContract.Events.TITLE))
        val map = shadowOf(shown[1].actionIntent)
        assertTrue(map.isActivityIntent)
        assertTrue(map.isImmutable)
        assertEquals("geo", map.savedIntent.data?.scheme)
        assertTrue("Голям салон" in map.savedIntent.data.toString().let(android.net.Uri::decode))
        // Без място — само календарът; без дата — нищо.
        assertEquals(1, RichNotifications.eventActions(context, event.copy(place = null), ZoneId.of("Europe/Sofia"), id).size)
        assertTrue(RichNotifications.eventActions(context, event.copy(date = null, place = null), ZoneId.of("Europe/Sofia"), id).isEmpty())
    }

    /** Няма приложение за календар/карта — няма и бутон (иначе докосването не би направило нищо). */
    @Test fun noHandlingAppMeansNoButton() {
        val event = Event("e2", "Сбирка", "2026-10-20", place = "Читалището", sourceUrl = "https://chyavorec.org/events")
        assertTrue(RichNotifications.eventActions(context, event, ZoneId.of("Europe/Sofia"), NotificationIds.event("e2")).isEmpty())
    }

    /** „Сподели“ на новина — системният избор със заглавието и адреса, неизменим. */
    @Test fun newsShareActionIsImmutableChooser() {
        val article = NewsArticle("n1", "Изложба на шевици", "https://chyavorec.org/news/izlozhba")
        val action = assertNotNull(RichNotifications.shareAction(context, article, NotificationIds.NEWS))
        assertEquals(context.getString(R.string.action_share), action.title.toString())
        val pending = shadowOf(action.actionIntent)
        assertTrue(pending.isActivityIntent)
        assertTrue(pending.isImmutable)
        assertEquals(Intent.ACTION_CHOOSER, pending.savedIntent.action)
        @Suppress("DEPRECATION")
        val send = pending.savedIntent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)!!
        assertEquals("Изложба на шевици\nhttps://chyavorec.org/news/izlozhba", send.getStringExtra(Intent.EXTRA_TEXT))
    }
}

/** Каналите от по-стара версия — без [NotifierTest.setUp], който вече ги е създал в новите групи. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class NotifierLegacyGroupsTest {
    private val context get() = ApplicationProvider.getApplicationContext<Application>()

    /**
     * Канал от по-стара версия, вече в старата група: Android не би го преместил, затова остава в нея
     * (с новото ѝ име), вместо да стане невидим в настройките под изтрита група.
     */
    @Test fun channelInLegacyGroupStaysThere() {
        val nm = context.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannelGroup(NotificationChannelGroup("group_my_library", "Стара"))
        nm.createNotificationChannel(NotificationChannel("loans", "Стар", NotificationManager.IMPORTANCE_HIGH).apply { group = "group_my_library" })
        Notifier.createChannels(context)
        assertEquals("group_my_library", nm.getNotificationChannel("loans").group)
        val groups = nm.notificationChannelGroups.associate { it.id to it.name.toString() }
        assertEquals(context.getString(R.string.channel_group_my_library), groups["group_my_library"])
        assertEquals("2_centre", nm.getNotificationChannel("events").group)
    }
}
