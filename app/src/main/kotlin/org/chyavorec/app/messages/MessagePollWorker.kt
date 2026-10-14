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
import org.chyavorec.app.notifications.Notifier
import java.util.concurrent.TimeUnit

/**
 * Съобщенията от читалището (важни и обикновени) — проверка на ~30 минути, за да
 * излиза известие скоро след публикуването им от админ панела. Пита само статичния
 * `/data/app-messages.json` (CDN кеш, условна заявка) — без моста и без Blob
 * операции; личните съобщения остават в общата фонова задача.
 */
class MessagePollWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? ChitalishteApp ?: return Result.success()
        val c = app.container
        if (!c.settings.current().notifyMessages || !Notifier.canNotify(applicationContext)) return Result.success()
        runCatching { MessageWorker.refreshAndNotify(applicationContext, c, includePersonal = false) }
        return Result.success()
    }

    companion object {
        private const val NAME = "message-poll"

        fun schedule(context: Context) {
            // 30 мин. с 10 мин. отклонение: системата групира будностите (Doze) — без излишна батерия.
            val request = PeriodicWorkRequestBuilder<MessagePollWorker>(30, TimeUnit.MINUTES, 10, TimeUnit.MINUTES)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
        }
    }
}
