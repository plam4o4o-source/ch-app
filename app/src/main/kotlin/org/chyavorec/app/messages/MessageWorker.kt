package org.chyavorec.app.messages

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.notifications.NotificationIds
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.core.Outcome
import org.chyavorec.data.site.AppMessagesParser
import org.chyavorec.domain.model.MessagePriority
import java.util.concurrent.TimeUnit

/**
 * Проверка за нови съобщения от читалището (на ~1 час, при интернет).
 * Push сървър няма (без Firebase/проследяване), затова приложението само
 * пита сайта. Всяко съобщение дава известие най-много веднъж; при първото
 * пускане старите съобщения не се показват като известия.
 */
class MessageWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? ChitalishteApp ?: return Result.success()
        val c = app.container
        if (!c.settings.current().notifyMessages) return Result.success()
        val result = c.messages.refresh(force = true)
        val list = (result as? Outcome.Success)?.value?.takeIf { !it.fromCache }?.data ?: return Result.success()

        val notified = c.settings.notifiedMessageIds()
        val read = c.settings.readMessageIdsNow()
        val firstRun = !c.settings.messagesInitialized()
        val cutoff = c.clock.now().minusSeconds(RECENT_SECONDS)
        list.filter { it.id !in notified && it.id !in read }
            .filter { !firstRun && AppMessagesParser.createdAt(it).isAfter(cutoff) }
            .take(MAX_PER_RUN)
            .forEach { m ->
                Notifier.show(
                    applicationContext,
                    if (m.priority == MessagePriority.HIGH) Channel.MESSAGES_IMPORTANT else Channel.MESSAGES,
                    NotificationIds.message(m.id),
                    m.title, m.body.ifBlank { m.title }, DEEP_LINK,
                )
            }
        c.settings.setNotifiedMessages(list.map { it.id }.toSet())
        c.settings.setMessagesInitialized()
        return Result.success()
    }

    companion object {
        private const val NAME = "app-messages"
        const val DEEP_LINK = "messages"
        private const val MAX_PER_RUN = 3
        /** Известие само за съобщения от последните 7 дни. */
        private const val RECENT_SECONDS = 7 * 24 * 60 * 60L

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<MessageWorker>(1, TimeUnit.HOURS, 20, TimeUnit.MINUTES)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.KEEP, request)
        }
    }
}
