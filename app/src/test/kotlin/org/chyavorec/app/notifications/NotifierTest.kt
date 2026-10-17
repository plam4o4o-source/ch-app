package org.chyavorec.app.notifications

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

/** При изход се махат само известията с лично съдържание (заемания, лични съобщения). */
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
}
