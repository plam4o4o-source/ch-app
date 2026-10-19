package org.chyavorec.app.notifications

import android.Manifest
import android.app.Notification
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
    /*
     * HIGH остава: всички известия за срокове вече са само през деня — напомнянията
     * в 10:00 (и наваксване до 20:00), просрочието от фоновата задача не и в 21:00–08:00
     * (LoanReminderPlan.isQuiet/deliverable), затова звукът не може да събуди нощем.
     */
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
    /** В известието: лично съдържание на влезлия читател (срок на заемане, лично съобщение). */
    private const val EXTRA_PERSONAL = "org.chyavorec.personal"

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
     * Показва известие. Текстът е кратък и НЕ съдържа лични данни извън заглавието на книгата
     * или личното съобщение — а те на заключен екран не се виждат: там личните известия
     * показват само общ текст ([lockScreenTitle]), независимо от системната настройка.
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
        /** Общият текст на заключен екран за лично известие; `null` — според канала (срок / лично съобщение). */
        lockScreenTitle: String? = null,
    ) {
        if (!canNotify(context)) return
        // Каналите се създават във фон веднъж за версия (ChitalishteApp) — при много ранно
        // известие или изтрит от системата канал може още да ги няма.
        context.getSystemService(NotificationManager::class.java)?.let { nm ->
            if (nm.getNotificationChannel(channel.id) == null) createChannels(context)
        }
        val personal = channel == Channel.LOANS || sensitive
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            deepLink?.let { putExtra(EXTRA_DEEP_LINK, it) }
        }
        val pending = PendingIntent.getActivity(
            context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        // Без отделна публична версия Android по подразбиране („Показване на цялото съдържание“)
        // показва и личното на заключен екран — VISIBILITY_PRIVATE само избира коя версия.
        val publicVersion = if (personal) {
            val generic = lockScreenTitle
                ?: context.getString(if (channel == Channel.LOANS) R.string.notif_public_loan else R.string.notif_public_message)
            NotificationCompat.Builder(context, channel.id)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(context, R.color.brand_gold_dark))
                .setContentTitle(generic)
                .build()
        } else null
        val notification = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, R.color.brand_gold_dark))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pending)
            .setAutoCancel(true)
            // Съобщенията: повторно показване със същото ID (напр. две проверки една след друга)
            // само подменя известието — без втори звук/вибрация.
            .setOnlyAlertOnce(channel == Channel.MESSAGES || channel == Channel.MESSAGES_IMPORTANT)
            // Заглавията на заети книги и личните съобщения — скрити на заключен екран (вместо тях — publicVersion).
            .setVisibility(if (personal) NotificationCompat.VISIBILITY_PRIVATE else NotificationCompat.VISIBILITY_PUBLIC)
            .apply { publicVersion?.let { setPublicVersion(it) } }
            // По този белег известията на читателя се махат при изход ([cancelPersonal]).
            .apply { if (personal) addExtras(android.os.Bundle().apply { putBoolean(EXTRA_PERSONAL, true) }) }
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        runCatching { NotificationManagerCompat.from(context).notify(id, notification) }
    }

    /**
     * При изход (или смяна на читателя): премахва показаните известия с лично съдържание —
     * за заемания (каналът LOANS) и личните съобщения от библиотеката. Общите (новини,
     * събития, съобщения от сайта, обновяване) остават.
     */
    fun cancelPersonal(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java) ?: return
        runCatching {
            nm.activeNotifications.filter { isPersonal(it.notification) }.forEach { nm.cancel(it.tag, it.id) }
        }
    }

    /** Личните съобщения отпреди белега [EXTRA_PERSONAL] се познават по скритото съдържание (VISIBILITY_PRIVATE). */
    internal fun isPersonal(n: Notification): Boolean =
        n.channelId == Channel.LOANS.id ||
            n.extras?.getBoolean(EXTRA_PERSONAL) == true ||
            (n.channelId in MESSAGE_CHANNELS && n.visibility == Notification.VISIBILITY_PRIVATE)

    private val MESSAGE_CHANNELS = setOf(Channel.MESSAGES.id, Channel.MESSAGES_IMPORTANT.id)
}
