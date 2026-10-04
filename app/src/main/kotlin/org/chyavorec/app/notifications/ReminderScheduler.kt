package org.chyavorec.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import org.chyavorec.app.R
import org.chyavorec.app.data.local.EventReminderEntity
import org.chyavorec.app.data.local.RemindersDao
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
    val reminderIds: Flow<List<String>> = dao.observeIds()

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
        dao.upsert(EventReminderEntity(event.id, event.title, at))
        return true
    }

    suspend fun cancel(eventId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag(eventId))
        dao.delete(eventId)
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
        Notifier.show(
            applicationContext, Channel.EVENTS, NotificationIds.event(id),
            applicationContext.getString(R.string.notif_event_reminder), title, "events",
        )
        return Result.success()
    }
}
