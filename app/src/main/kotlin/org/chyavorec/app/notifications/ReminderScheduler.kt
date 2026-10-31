package org.chyavorec.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import org.chyavorec.app.R
import org.chyavorec.app.data.local.EventReminderEntity
import org.chyavorec.app.data.local.RemindersDao
import org.chyavorec.app.data.local.dbWrite
import org.chyavorec.app.data.local.orOnDbError
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.core.AppClock
import org.chyavorec.domain.model.Event
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Локални напомняния за събития (без сървър): известие ден преди събитието в
 * 18:00, или 2 часа преди началото, ако събитието е днес/утре и часът е известен.
 */
class ReminderScheduler(
    private val context: Context,
    private val dao: RemindersDao,
    private val clock: AppClock,
) {
    val reminderIds: Flow<List<String>> = dao.observeIds().orOnDbError(emptyList())

    fun remindAt(event: Event): Long? {
        val date = event.date?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: return null
        val time = event.time?.let { runCatching { LocalTime.parse(it) }.getOrNull() }
        val zone = clock.zone()
        val candidates = buildList {
            add(date.minusDays(1).atTime(18, 0).atZone(zone).toInstant().toEpochMilli())
            if (time != null) add(date.atTime(time).minusHours(2).atZone(zone).toInstant().toEpochMilli())
        }
        val now = clock.now().toEpochMilli()
        return candidates.filter { it > now }.minOrNull()
    }

    /** @return false, ако събитието вече е минало и напомняне е невъзможно. */
    suspend fun schedule(event: Event): Boolean {
        val at = remindAt(event) ?: return false
        val delay = at - clock.now().toEpochMilli()
        val request = OneTimeWorkRequestBuilder<EventReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(KEY_TITLE to event.title, KEY_ID to event.id))
            .addTag(tag(event.id))
            .build()
        WorkManager.getInstance(context).enqueue(request)
        // Известието е планирано и без записа — той само отбелязва звънчето на събитието.
        dbWrite { dao.upsert(EventReminderEntity(event.id, event.title, at)) }
        return true
    }

    suspend fun cancel(eventId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag(eventId))
        dbWrite { dao.delete(eventId) }
    }

    private fun tag(id: String) = "event-reminder:" + id.hashCode()

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_ID = "id"
    }
}

class EventReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val title = inputData.getString(ReminderScheduler.KEY_TITLE) ?: return Result.success()
        val id = inputData.getString(ReminderScheduler.KEY_ID).orEmpty()
        val notificationId = NotificationIds.event(id)
        // Събитието от кеша (без мрежа) — за снимката и бутоните; без него — само текст.
        val c = (applicationContext as? AppContainerOwner)?.appContainer
        val event = if (c != null && id.isNotBlank() && Notifier.canNotify(applicationContext)) {
            try {
                c.eventsRepository.find(id)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                null
            }
        } else null
        // Докосването отваря самото събитие (под него — списъкът със събития).
        Notifier.show(
            applicationContext, Channel.EVENTS, notificationId,
            applicationContext.getString(R.string.notif_event_reminder), title,
            if (id.isNotBlank()) DeepLinks.event(id) else DeepLinks.EVENTS,
            actions = if (c != null && event != null) RichNotifications.eventActions(applicationContext, event, c.clock.zone(), notificationId) else emptyList(),
            image = if (c != null && event != null) RichNotifications.image(applicationContext, c, event.imageUrl) else null,
        )
        return Result.success()
    }
}
