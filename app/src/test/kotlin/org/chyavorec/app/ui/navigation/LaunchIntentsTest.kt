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
}
