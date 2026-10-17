package org.chyavorec.app.messages

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CancellationException
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.domain.model.MessagePollPolicy
import java.util.concurrent.TimeUnit

/**
 * Съобщенията от читалището (важни и обикновени) — адаптивна проверка ([MessagePollPolicy]):
 * задачата се буди на ~1 ч. (само с интернет и достатъчно батерия), но пита сайта само ако
 * е „дължимо“ — непроменен файл удвоява интервала до 6 ч., ново съобщение или отваряне
 * на приложението го връща на 1 ч. Ако приложението не е отваряно 14 дни — нищо не се
 * проверява, докато не бъде отворено. Пита само статичния `/data/app-messages.json`
 * (CDN кеш, условна заявка) — без моста и без Blob операции; личните съобщения остават
 * в общата фонова задача.
 */
class MessagePollWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? ChitalishteApp ?: return Result.success()
        val c = app.container
        if (!c.settings.current().notifyMessages || !Notifier.canNotify(applicationContext)) return Result.success()
        val now = c.clock.now().toEpochMilli()
        val state = c.settings.messagePoll()
        if (state.lastOpen == 0L) c.settings.setLastAppOpenIfMissing(now)
        // Дотук — само четене на настройките: без мрежа, без Keystore, без разчитане.
        if (MessagePollPolicy.isDormant(now, state.lastOpen)) return Result.success()
        if (!MessagePollPolicy.isDue(now, state.lastPoll, state.interval)) return Result.success()
        try {
            // Без force: ако приложението/общата задача току-що са свалили файла (до 10 мин.) — без нова заявка.
            val list = MessageWorker.refreshAndNotify(applicationContext, c, includePersonal = false, force = false)
            if (list != null) {
                val signature = MessagePollPolicy.signature(list)
                // Първата проверка (без отпечатък) не удължава интервала.
                val changed = state.signature == null || state.signature != signature
                c.settings.setMessagePoll(now, MessagePollPolicy.next(state.interval, changed), signature)
            }
            // Без свежи данни (грешка/офлайн) — нищо не се записва: нов опит при следващото събуждане.
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Следващият опит е при следващото събуждане.
        }
        return Result.success()
    }

    companion object {
        private const val NAME = "message-poll"

        /**
         * [update] = true (нова версия) — UPDATE: инсталираните копия минават на новия
         * период/ограничения; иначе KEEP — задачата се добавя само ако WorkManager я няма.
         */
        fun schedule(context: Context, update: Boolean) {
            // 60 мин. с 15 мин. отклонение: системата групира будностите (Doze) — без излишна батерия.
            val request = PeriodicWorkRequestBuilder<MessagePollWorker>(60, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build(),
                )
                .build()
            val policy = if (update) ExistingPeriodicWorkPolicy.UPDATE else ExistingPeriodicWorkPolicy.KEEP
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(NAME, policy, request)
        }
    }
}
