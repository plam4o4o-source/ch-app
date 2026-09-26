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
    UPDATES("updates", R.string.channel_updates, R.string.channel_updates_desc, NotificationManager.IMPORTANCE_DEFAULT),
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
    fun show(context: Context, channel: Channel, id: Int, title: String, text: String, deepLink: String?) {
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
            .setVisibility(if (channel == Channel.LOANS) NotificationCompat.VISIBILITY_PRIVATE else NotificationCompat.VISIBILITY_PUBLIC)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        runCatching { NotificationManagerCompat.from(context).notify(id, notification) }
    }
}
