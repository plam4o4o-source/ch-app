package org.chyavorec.app.notifications

import android.Manifest
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** Известията с лично съдържание: махат се при изход и са общи на заключен екран. */
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
        assertEquals(4, nm.activeNotifications.size)

        Notifier.cancelPersonal(context)

        assertEquals(
            setOf(NotificationIds.message("m1"), NotificationIds.NEWS),
            nm.activeNotifications.map { it.id }.toSet(),
        )
    }

    /**
     * Личните известия имат обща публична версия за заключения екран — без заглавието на
     * книгата и без текста на съобщението (иначе Android по подразбиране показва всичко).
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
}
