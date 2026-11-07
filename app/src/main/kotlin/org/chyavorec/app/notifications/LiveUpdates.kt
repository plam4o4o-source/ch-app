package org.chyavorec.app.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CancellationException
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.domain.model.Event
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * „Живи известия“ (Android 16+): системата показва известието най-отгоре в панела, на заключения
 * екран и като чип в лентата на състоянието (процент, обратно броене). Под Android 16 — обичайните
 * известия, без промяна. Потребителят може да ги изключи (Настройки › Известия › приложението).
 */
object LiveUpdates {
    /**
     * Ключът на `Notification.Builder.setRequestPromotedOngoing` (Android 16 QPR2) — методът само
     * го записва в extras. Така молбата работи и на 36.0 (там системата я пренебрегва) без
     * проверка на второстепенната версия.
     */
    internal const val EXTRA_REQUEST_PROMOTED_ONGOING = "android.requestPromotedOngoing"
    /** Белег на живото известие за събитие — опресняването не го показва отново, ако е махнато. */
    internal const val EXTRA_LIVE_EVENT = "org.chyavorec.liveEvent"

    fun supported(sdk: Int = Build.VERSION.SDK_INT): Boolean = sdk >= Build.VERSION_CODES.BAKLAVA

    /** Моли за „живо“ показване (ProgressStyle, без цветен фон и собствени изгледи — условията на системата). */
    internal fun requestPromotion(builder: Notification.Builder, extras: Bundle = Bundle()): Notification.Builder =
        builder.addExtras(extras.apply { putBoolean(EXTRA_REQUEST_PROMOTED_ONGOING, true) })

    /** Показва ли се още живото известие за събитие [id] (не е махнато от потребителя или от системата). */
    internal fun isEventShowing(context: Context, id: Int): Boolean = runCatching {
        context.getSystemService(NotificationManager::class.java)?.activeNotifications.orEmpty()
            .any { it.id == id && it.notification.extras.getBoolean(EXTRA_LIVE_EVENT) }
    }.getOrDefault(false)
}

/**
 * Прозорецът на живото известие за събитие: от [LEAD_MS] преди началото до началото (+ [GRACE_MS],
 * после системата го маха сама — setTimeoutAfter). Обратното броене е хронометър (без опресняване
 * всяка минута); лентата на хода се опреснява само в [REFRESH_BEFORE_MS].
 */
object EventLiveWindow {
    const val LEAD_MS = 60 * 60_000L
    const val GRACE_MS = 5 * 60_000L
    /** Минутите в лентата: последните [FINAL_MINUTES] са в бордо. */
    const val MINUTES = 60
    const val FINAL_MINUTES = 15
    /** Опресняване на лентата — 30 и 10 минути преди началото (две събуждания за целия час). */
    val REFRESH_BEFORE_MS = listOf(30 * 60_000L, 10 * 60_000L)

    /** Началото на събитието (дата и час в [zone]); без час — `null` (живо известие няма). */
    fun startMillis(event: Event, zone: ZoneId): Long? {
        val date = event.date?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: return null
        val time = event.time?.let { runCatching { LocalTime.parse(it) }.getOrNull() } ?: return null
        return date.atTime(time).atZone(zone).toInstant().toEpochMilli()
    }

    /** След колко време да тръгне задачата (0 — вече сме в прозореца); `null` — събитието е започнало. */
    fun delayUntilShow(now: Long, start: Long): Long? = if (now >= start) null else (start - LEAD_MS - now).coerceAtLeast(0)

    /** Минутите от началото на прозореца (0..[MINUTES]) — ходът в ProgressStyle. */
    fun elapsedMinutes(now: Long, start: Long): Int = ((now - (start - LEAD_MS)) / 60_000L).toInt().coerceIn(0, MINUTES)

    /** setTimeoutAfter: известието изчезва [GRACE_MS] след началото. */
    fun timeoutAfter(now: Long, start: Long): Long = (start + GRACE_MS - now).coerceAtLeast(0)

    /** Следващото опресняване (поне минута напред); `null` — няма повече. */
    fun nextRefresh(now: Long, start: Long): Long? = REFRESH_BEFORE_MS.map { start - it }.firstOrNull { it > now + 60_000L }
}

/** Живото известие „Събитието започва скоро“ (само Android 16+; напомнянето отпреди остава както е). */
object EventLiveUpdates {
    private const val UNIQUE = "event-live:"

    internal const val KEY_ID = "id"
    internal const val KEY_TITLE = "title"
    internal const val KEY_PLACE = "place"
    internal const val KEY_START = "start"
    internal const val KEY_REFRESH = "refresh"

    fun uniqueName(eventId: String) = UNIQUE + eventId.hashCode()

    /** Планира задачата за T−60 мин (неточно, през WorkManager). `false` — няма час или е минало. */
    fun schedule(context: Context, event: Event, zone: ZoneId, now: Long, tag: String): Boolean {
        if (!LiveUpdates.supported()) return false
        val start = EventLiveWindow.startMillis(event, zone) ?: return false
        val delay = EventLiveWindow.delayUntilShow(now, start) ?: return false
        val request = OneTimeWorkRequestBuilder<EventLiveUpdateWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(KEY_ID to event.id, KEY_TITLE to event.title, KEY_PLACE to event.place, KEY_START to start))
            .addTag(tag)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(uniqueName(event.id), ExistingWorkPolicy.REPLACE, request)
        return true
    }

    /** Маха живото известие (напомнянето е отменено); обикновеното напомняне със същото ID остава. */
    fun cancel(context: Context, eventId: String) {
        val id = NotificationIds.event(eventId)
        if (LiveUpdates.isEventShowing(context, id)) runCatching { NotificationManagerCompat.from(context).cancel(id) }
    }

    /**
     * „Вечер на поезията“ / „Читалището — начало в 18:30“: ProgressStyle с изминалото от часа
     * преди началото (злато, последните 15 мин — бордо), чип с обратното броене до началото
     * (хронометър) и „Маршрут“, ако е известно мястото.
     */
    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    internal fun notification(
        context: Context, eventId: String, title: String, place: String?, start: Long, now: Long, zone: ZoneId,
    ): Notification {
        context.getSystemService(NotificationManager::class.java)?.let { nm ->
            if (nm.getNotificationChannel(Channel.EVENTS.id) == null) Notifier.createChannels(context)
        }
        val id = NotificationIds.event(eventId)
        val gold = ContextCompat.getColor(context, R.color.brand_gold)
        val burgundy = ContextCompat.getColor(context, R.color.brand_burgundy)
        val style = Notification.ProgressStyle()
            .setProgressSegments(
                listOf(
                    Notification.ProgressStyle.Segment(EventLiveWindow.MINUTES - EventLiveWindow.FINAL_MINUTES).setColor(gold),
                    Notification.ProgressStyle.Segment(EventLiveWindow.FINAL_MINUTES).setColor(burgundy),
                ),
            )
            // Половината от часа — точка в лентата.
            .setProgressPoints(listOf(Notification.ProgressStyle.Point(EventLiveWindow.MINUTES / 2).setColor(burgundy)))
            .setProgress(EventLiveWindow.elapsedMinutes(now, start))
        val at = Instant.ofEpochMilli(start).atZone(zone).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        val text = place?.takeIf { it.isNotBlank() }
            ?.let { context.getString(R.string.notif_event_live_text_place, it, at) }
            ?: context.getString(R.string.notif_event_live_text, at)
        val open = PendingIntent.getActivity(
            context, id,
            LaunchIntents.openLink(context, DeepLinks.event(eventId)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        return LiveUpdates.requestPromotion(
            Notification.Builder(context, Channel.EVENTS.id),
            Bundle().apply { putBoolean(LiveUpdates.EXTRA_LIVE_EVENT, true) },
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, R.color.brand_gold_dark))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(style)
            // Чипът в лентата на състоянието: обратно броене до началото, без опресняване.
            .setWhen(start)
            .setShowWhen(true)
            .setUsesChronometer(true)
            .setChronometerCountDown(true)
            .setTimeoutAfter(EventLiveWindow.timeoutAfter(now, start))
            .setContentIntent(open)
            .apply { RichNotifications.routeAction(context, place, id)?.let { addAction(it) } }
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            // Напомнянето отпреди вече е звъннало — живото известие е тихо.
            .setSilent(true)
            .setCategory(Notification.CATEGORY_EVENT)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .build()
    }
}

/**
 * Живото известие за събитие с напомняне: тръгва час преди началото, опреснява лентата два пъти
 * ([EventLiveWindow.nextRefresh]) и не се показва отново, ако потребителят го е махнал. Само с
 * включени известия за събития (настройката и каналът — системата не показва известие в изключен канал).
 */
class EventLiveUpdateWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (!LiveUpdates.supported()) return Result.success()
        val id = inputData.getString(EventLiveUpdates.KEY_ID)?.takeIf { it.isNotBlank() } ?: return Result.success()
        val title = inputData.getString(EventLiveUpdates.KEY_TITLE) ?: return Result.success()
        val start = inputData.getLong(EventLiveUpdates.KEY_START, 0L)
        val c = (applicationContext as? AppContainerOwner)?.appContainer ?: return Result.success()
        val now = c.clock.now().toEpochMilli()
        if (now >= start) return Result.success()
        val notificationId = NotificationIds.event(id)
        // Опресняване на вече махнато известие — нищо (и без следващи опреснявания).
        if (inputData.getBoolean(EventLiveUpdates.KEY_REFRESH, false) && !LiveUpdates.isEventShowing(applicationContext, notificationId)) {
            return Result.success()
        }
        val enabled = try {
            c.settings.current().notifyEvents
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            true
        }
        if (!enabled || !Notifier.canNotify(applicationContext)) return Result.success()
        val n = EventLiveUpdates.notification(
            applicationContext, id, title, inputData.getString(EventLiveUpdates.KEY_PLACE), start, now, c.clock.zone(),
        )
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.success()
        }
        runCatching { NotificationManagerCompat.from(applicationContext).notify(notificationId, n) }
        EventLiveWindow.nextRefresh(now, start)?.let { at ->
            val next = OneTimeWorkRequestBuilder<EventLiveUpdateWorker>()
                .setInitialDelay(at - now, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf(
                    EventLiveUpdates.KEY_ID to id, EventLiveUpdates.KEY_TITLE to title,
                    EventLiveUpdates.KEY_PLACE to inputData.getString(EventLiveUpdates.KEY_PLACE),
                    EventLiveUpdates.KEY_START to start, EventLiveUpdates.KEY_REFRESH to true,
                ))
                .apply { this@EventLiveUpdateWorker.tags.forEach { t -> addTag(t) } }
                .build()
            // След текущата (APPEND): REPLACE би отменил самата нея.
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(EventLiveUpdates.uniqueName(id), ExistingWorkPolicy.APPEND_OR_REPLACE, next)
        }
        return Result.success()
    }
}
