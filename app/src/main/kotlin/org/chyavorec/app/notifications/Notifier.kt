package org.chyavorec.app.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R

/** Канали за известия — потребителят ги управлява и от системните настройки. */
enum class Channel(val id: String, val nameRes: Int, val descRes: Int, val importance: Int) {
    LOANS("loans", R.string.channel_loans, R.string.channel_loans_desc, NotificationManager.IMPORTANCE_HIGH),
    EVENTS("events", R.string.channel_events, R.string.channel_events_desc, NotificationManager.IMPORTANCE_DEFAULT),
    NEWS("news", R.string.channel_news, R.string.channel_news_desc, NotificationManager.IMPORTANCE_LOW),
    LIBRARY("library", R.string.channel_library, R.string.channel_library_desc, NotificationManager.IMPORTANCE_DEFAULT),
    MESSAGES("messages", R.string.channel_messages, R.string.channel_messages_desc, NotificationManager.IMPORTANCE_DEFAULT),
    MESSAGES_IMPORTANT("messages_important", R.string.channel_messages_important, R.string.channel_messages_important_desc, NotificationManager.IMPORTANCE_HIGH),
    UPDATES("updates", R.string.channel_updates, R.string.channel_updates_desc, NotificationManager.IMPORTANCE_DEFAULT),
}

/**
 * ID-та на известията. Всеки вид има собствен, непрепокриващ се диапазон
 * (база + 13 бита от хеша), затова известие за срок не може да замени
 * известие за събитие или съобщение. ID-то служи и за requestCode на
 * PendingIntent-а, така че и той е уникален за вида.
 */
object NotificationIds {
    const val NEWS = 500
    const val UPDATE = 700
    private const val DUE_SOON_BASE = 10_000
    private const val OVERDUE_BASE = 20_000
    private const val EVENT_BASE = 30_000
    private const val MESSAGE_BASE = 40_000
    private const val MASK = 0x1FFF

    fun dueSoon(loanId: String): Int = DUE_SOON_BASE + (loanId.hashCode() and MASK)
    fun overdue(loanId: String): Int = OVERDUE_BASE + (loanId.hashCode() and MASK)
    fun event(eventId: String): Int = EVENT_BASE + (eventId.hashCode() and MASK)
    fun message(messageId: String): Int = MESSAGE_BASE + (messageId.hashCode() and MASK)
}

object Notifier {
    const val EXTRA_DEEP_LINK = "deep_link"

    fun createChannels(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java) ?: return
        Channel.entries.filter { it != Channel.UPDATES || org.chyavorec.app.BuildConfig.SELF_UPDATE }.forEach { c ->
            nm.createNotificationChannel(
                NotificationChannel(c.id, context.getString(c.nameRes), c.importance).apply {
                    description = context.getString(c.descRes)
                },
            )
        }
    }

    fun canNotify(context: Context): Boolean =
        (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) &&
            NotificationManagerCompat.from(context).areNotificationsEnabled()

    /**
     * Показва известие. Текстът е кратък и НЕ съдържа лични данни извън
     * заглавието на книгата (видимо само на заключения екран според системните настройки).
     */
    fun show(
        context: Context,
        channel: Channel,
        id: Int,
        title: String,
        text: String,
        deepLink: String?,
        /** Лично съдържание (напр. лично съобщение от библиотеката) — скрито на заключен екран. */
        sensitive: Boolean = false,
    ) {
        if (!canNotify(context)) return
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            deepLink?.let { putExtra(EXTRA_DEEP_LINK, it) }
        }
        val pending = PendingIntent.getActivity(
            context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, R.color.brand_gold_dark))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pending)
            .setAutoCancel(true)
            // Заглавията на заети книги са лична информация — скрити на заключен екран.
            .setVisibility(if (channel == Channel.LOANS || sensitive) NotificationCompat.VISIBILITY_PRIVATE else NotificationCompat.VISIBILITY_PUBLIC)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        runCatching { NotificationManagerCompat.from(context).notify(id, notification) }
    }
}
