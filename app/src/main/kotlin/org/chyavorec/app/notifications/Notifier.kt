package org.chyavorec.app.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.app.R

/**
 * Групи на каналите в системните настройки. Android ги подрежда по ID, затова „1_“…„3_“:
 * „Моята библиотека“, „Читалището“, „Приложението“. ID-тата на каналите не се променят.
 *
 * Android сменя групата само на канал без група, а изтриването на група изтрива и каналите ѝ
 * (възстановен, каналът пак сочи старата група и изчезва от настройките). Затова канал от по-стара
 * версия остава в старата си група ([legacyId]) — с новото ѝ име; новият ред е за новите инсталации.
 */
enum class ChannelGroup(val id: String, val legacyId: String, val nameRes: Int) {
    MY_LIBRARY("1_my_library", "group_my_library", R.string.channel_group_my_library),
    CENTRE("2_centre", "group_centre", R.string.channel_group_centre),
    APP("3_app", "group_app", R.string.channel_group_app),
}

/** Канали за известия — потребителят ги управлява и от системните настройки. */
enum class Channel(val id: String, val nameRes: Int, val descRes: Int, val importance: Int, val group: ChannelGroup) {
    /*
     * HIGH остава: всички известия за срокове вече са само през деня — напомнянията
     * в 10:00 (и наваксване до 20:00), просрочието от фоновата задача не и в 21:00–08:00
     * (LoanReminderPlan.isQuiet/deliverable), затова звукът не може да събуди нощем.
     */
    LOANS("loans", R.string.channel_loans, R.string.channel_loans_desc, NotificationManager.IMPORTANCE_HIGH, ChannelGroup.MY_LIBRARY),
    EVENTS("events", R.string.channel_events, R.string.channel_events_desc, NotificationManager.IMPORTANCE_DEFAULT, ChannelGroup.CENTRE),
    NEWS("news", R.string.channel_news, R.string.channel_news_desc, NotificationManager.IMPORTANCE_LOW, ChannelGroup.CENTRE),
    LIBRARY("library", R.string.channel_library, R.string.channel_library_desc, NotificationManager.IMPORTANCE_DEFAULT, ChannelGroup.MY_LIBRARY),
    MESSAGES("messages", R.string.channel_messages, R.string.channel_messages_desc, NotificationManager.IMPORTANCE_DEFAULT, ChannelGroup.CENTRE),
    MESSAGES_IMPORTANT("messages_important", R.string.channel_messages_important, R.string.channel_messages_important_desc, NotificationManager.IMPORTANCE_HIGH, ChannelGroup.CENTRE),
    UPDATES("updates", R.string.channel_updates, R.string.channel_updates_desc, NotificationManager.IMPORTANCE_DEFAULT, ChannelGroup.APP),
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
    private const val RENEW_BASE = 50_000
    private const val WANT_TO_READ_BASE = 60_000
    private const val MASK = 0x1FFF
    /** Обобщението на групата известия за заемания ([Notifier.LOANS_GROUP]). */
    const val LOANS_SUMMARY = 600
    /** Краткото известие на бързата задача за удължаване (Android 8–11, [RenewActionWorker]). */
    const val RENEW_PROGRESS = 601

    fun dueSoon(loanId: String): Int = DUE_SOON_BASE + (loanId.hashCode() and MASK)
    fun overdue(loanId: String): Int = OVERDUE_BASE + (loanId.hashCode() and MASK)
    fun event(eventId: String): Int = EVENT_BASE + (eventId.hashCode() and MASK)
    fun message(messageId: String): Int = MESSAGE_BASE + (messageId.hashCode() and MASK)
    /** Отговорът на библиотеката на заявка за удължаване. */
    fun renew(loanId: String): Int = RENEW_BASE + (loanId.hashCode() and MASK)
    /** „Вече е на рафта“ — книга от „Искам да прочета“. */
    fun wantToRead(inv: Long): Int = WANT_TO_READ_BASE + (inv.hashCode() and MASK)
}

object Notifier {
    const val EXTRA_DEEP_LINK = "deep_link"
    /** Групата на известията за заемания — при 2 и повече се показват под общо обобщение. */
    const val LOANS_GROUP = "org.chyavorec.loans"
    /** В известието: данни на влезлия читател (срок на заемане, лично съобщение) — махат се при изход. */
    private const val EXTRA_PERSONAL = "org.chyavorec.personal"

    fun createChannels(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java) ?: return
        val channels = Channel.entries.filter { it != Channel.UPDATES || org.chyavorec.app.BuildConfig.SELF_UPDATE }
        // Каналът от по-стара версия остава в групата, в която е (виж [ChannelGroup]).
        val groupIds = channels.associateWith { c ->
            nm.getNotificationChannel(c.id)?.group?.takeIf { it == c.group.legacyId } ?: c.group.id
        }
        // Групите — преди каналите и в реда на показване; празна група (без обновяване в Play) не се създава.
        nm.createNotificationChannelGroups(
            ChannelGroup.entries.flatMap { g -> channels.filter { it.group == g }.map { groupIds.getValue(it) }.distinct().map { it to g } }
                .map { (id, g) -> NotificationChannelGroup(id, context.getString(g.nameRes)) },
        )
        channels.forEach { c ->
            nm.createNotificationChannel(
                NotificationChannel(c.id, context.getString(c.nameRes), c.importance).apply {
                    description = context.getString(c.descRes)
                    group = groupIds.getValue(c)
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
     * или личното съобщение. За скритите известия има публична версия с общ текст
     * ([lockScreenTitle]), но Android я показва на заключен екран само при системната настройка
     * „скриване на чувствителното съдържание“; при „цялото съдържание“ се вижда и заглавието.
     */
    fun show(
        context: Context,
        channel: Channel,
        id: Int,
        title: String,
        text: String,
        deepLink: String?,
        /** Лично съдържание (лично съобщение, списъкът „Искам да прочета“) — скрито на заключен екран (VISIBILITY_PRIVATE). */
        sensitive: Boolean = false,
        /** Данни на влезлия читател — махат се при изход ([cancelPersonal]); списъкът „Искам да прочета“ остава. */
        readerData: Boolean = sensitive,
        /** Общият текст на заключен екран за лично известие; `null` — според канала (срок / лично съобщение). */
        lockScreenTitle: String? = null,
        /** Бутони в известието (напр. „Удължи“ — [RenewActionReceiver]). */
        actions: List<NotificationCompat.Action> = emptyList(),
        /** Без звук и вибрация — напр. резултатът, който подменя вече показано известие. */
        silent: Boolean = false,
        /** Категорията за системата; известията за заемания по подразбиране са напомняния. */
        category: String? = if (channel == Channel.LOANS) NotificationCompat.CATEGORY_REMINDER else null,
    ) {
        if (!canNotify(context)) return
        // Каналите се създават във фон веднъж за версия (ChitalishteApp) — при много ранно
        // известие или изтрит от системата канал може още да ги няма.
        context.getSystemService(NotificationManager::class.java)?.let { nm ->
            if (nm.getNotificationChannel(channel.id) == null) createChannels(context)
        }
        val hidden = channel == Channel.LOANS || sensitive
        val personal = channel == Channel.LOANS || readerData
        // Връзката — само през собствения (неекспортиран) вход; виж LaunchIntents.linkOf.
        val intent = LaunchIntents.openLink(context, deepLink).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pending = PendingIntent.getActivity(
            context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        // VISIBILITY_PRIVATE не скрива нищо само по себе си: при „скриване на чувствителното
        // съдържание“ Android показва тази версия (без нея — само името на приложението), а при
        // „цялото съдържание“ (често по подразбиране) — пълното известие. Приложението не може да го наложи.
        val publicVersion = if (hidden) {
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
            // Заглавията на книги и личните съобщения — чувствително съдържание (вместо тях — publicVersion).
            .setVisibility(if (hidden) NotificationCompat.VISIBILITY_PRIVATE else NotificationCompat.VISIBILITY_PUBLIC)
            .apply { publicVersion?.let { setPublicVersion(it) } }
            // По този белег известията на читателя се махат при изход ([cancelPersonal]).
            .apply { if (personal) addExtras(android.os.Bundle().apply { putBoolean(EXTRA_PERSONAL, true) }) }
            .apply { category?.let { setCategory(it) } }
            .apply { actions.forEach { addAction(it) } }
            .apply { if (silent) setSilent(true) }
            // Няколко книги за връщане — една група с обобщение, а не отделни редове в лентата.
            .apply { if (channel == Channel.LOANS) setGroup(LOANS_GROUP) }
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        runCatching { NotificationManagerCompat.from(context).notify(id, notification) }
        if (channel == Channel.LOANS) runCatching { updateLoansSummary(context) }
    }

    /**
     * Обобщението на групата [LOANS_GROUP] — при 2 и повече показани известия за заемания.
     * Само общ текст (без заглавия) и без собствен звук: алармират самите известия. Не се
     * маха при по-малко: отмяната на обобщение маха и известията под него.
     */
    private fun updateLoansSummary(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java) ?: return
        val children = nm.activeNotifications.count { it.id != NotificationIds.LOANS_SUMMARY && it.notification.group == LOANS_GROUP }
        if (children < 2) return
        val intent = LaunchIntents.openLink(context, "my/loans").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pending = PendingIntent.getActivity(
            context, NotificationIds.LOANS_SUMMARY, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val summary = NotificationCompat.Builder(context, Channel.LOANS.id)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, R.color.brand_gold_dark))
            .setContentTitle(context.getString(R.string.notif_loans_summary))
            .setContentIntent(pending)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setGroup(LOANS_GROUP)
            .setGroupSummary(true)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addExtras(android.os.Bundle().apply { putBoolean(EXTRA_PERSONAL, true) })
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        NotificationManagerCompat.from(context).notify(NotificationIds.LOANS_SUMMARY, summary)
    }

    /**
     * При изход (или смяна на читателя): премахва показаните известия с данни на читателя —
     * за заемания (каналът LOANS) и личните съобщения от библиотеката. Общите (новини,
     * събития, съобщения от сайта, обновяване) и „вече е на рафта“ (списъкът „Искам да
     * прочета“ е на устройството и остава след изход) остават.
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
