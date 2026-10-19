package org.chyavorec.app.messages

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.logFailure
import org.chyavorec.domain.model.MessagePollPolicy
import java.util.concurrent.TimeUnit

/**
 * Съобщенията от читалището (важни и обикновени) — адаптивна проверка ([MessagePollPolicy]):
 * непроменен файл удвоява интервала (1 → 6 ч.), ново съобщение или отваряне на приложението
 * го връща на 1 ч. Задачата е ЕДНОКРАТНА и е планирана точно за следващата дължима проверка
 * (само с интернет и достатъчно батерия); в края на всяка проверка се планира следващата.
 * Така процесът не се стартира на всеки час само за да установи, че не е време.
 *
 * Не се планира изобщо, ако известията за съобщения са изключени или забранени, или ако
 * приложението не е отваряно 14 дни — до следващото отваряне (MainActivity.onStart), включване
 * на настройката или даване на разрешение. Пита само статичния `/data/app-messages.json`
 * (CDN кеш, условна заявка) — без моста и без Blob операции; личните съобщения остават
 * в общата фонова задача.
 */
class MessagePollWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val c = (applicationContext as? AppContainerOwner)?.appContainer ?: return Result.success()
        val ok = try {
            poll(c)
        } catch (e: CancellationException) {
            // Спряна задача (напр. изгубена мрежа) — WorkManager я пуска отново сам.
            throw e
        } catch (e: Exception) {
            Diagnostics.record("worker.poll", e)
            false
        }
        // Следващата проверка: след неуспех (грешка/офлайн) — след най-краткия интервал, не веднага.
        runCatching { schedule(applicationContext, c, fromWorker = true, minDelayMillis = if (ok) 0 else MessagePollPolicy.BASE_MILLIS) }
            .logFailure("worker.poll-schedule")
        return Result.success()
    }

    /** @return false — проверката е била дължима, но не е успяла (нов опит по-късно). */
    private suspend fun poll(c: AppContainer): Boolean {
        if (!enabled(applicationContext, c)) return true
        val now = c.clock.now().toEpochMilli()
        val state = c.settings.messagePoll()
        if (state.lastOpen == 0L) c.settings.setLastAppOpenIfMissing(now)
        // Дотук — само четене на настройките: без мрежа, без Keystore, без разчитане.
        if (MessagePollPolicy.isDormant(now, state.lastOpen)) return true
        if (!MessagePollPolicy.isDue(now, state.lastPoll, state.interval)) return true
        // Без force: ако приложението/общата задача току-що са свалили файла (до 10 мин.) — без нова заявка.
        val list = MessageWorker.refreshAndNotify(applicationContext, c, includePersonal = false, force = false)
            // Без свежи данни (грешка/офлайн) — нищо не се записва.
            ?: return false
        val signature = MessagePollPolicy.signature(list)
        // Интервалът — от записаното в момента: ако междувременно приложението е отворено, отварянето печели.
        c.settings.recordMessagePoll(now, signature) { stored ->
            // Първата проверка (без отпечатък) не удължава интервала.
            val changed = stored.signature == null || stored.signature != signature
            MessagePollPolicy.intervalAfterPoll(state.interval, state.lastOpen, stored.interval, stored.lastOpen, changed)
        }
        return true
    }

    companion object {
        internal const val NAME = "message-poll-next"
        /** Предишната периодична задача (на всеки час) — отменя се веднъж за версия. */
        private const val LEGACY_NAME = "message-poll"

        /** Известията за съобщения са включени и разрешени. */
        private suspend fun enabled(context: Context, c: AppContainer): Boolean =
            c.settings.current().notifyMessages && Notifier.canNotify(context)

        /**
         * Планира следващата проверка за момента, в който стане дължима — или я отменя, ако
         * известията за съобщения са изключени/забранени или приложението „спи“.
         *
         * - от приложението (отваряне, настройката, разрешението) — REPLACE: новият момент
         *   (напр. след отваряне интервалът е отново 1 ч.) замества стария;
         * - [fromWorker] — от самата задача в края ѝ: APPEND_OR_REPLACE (REPLACE би прекъснал
         *   нея самата; следващата тръгва след приключването ѝ, закъснението се брои оттогава).
         *   Без смисъл от проверка — просто не се планира нищо (веригата свършва тук);
         * - [keep] — при старт на процеса: само ако WorkManager я няма (напр. изгубена верига).
         */
        suspend fun schedule(
            context: Context,
            c: AppContainer,
            fromWorker: Boolean = false,
            keep: Boolean = false,
            minDelayMillis: Long = 0,
        ) = lock.withLock {
            // Под заключване: две почти едновременни планирания (напр. разрешение и настройката)
            // не се разминават — по-късното чете вече записаното и неговото решение остава.
            val wm = WorkManager.getInstance(context)
            val on = enabled(context, c)
            val now = c.clock.now().toEpochMilli()
            val state = c.settings.messagePoll()
            val delay = MessagePollPolicy.nextRunDelay(on, now, state.lastOpen, state.lastPoll, state.interval, minDelayMillis)
            if (delay == null) {
                if (!fromWorker) wm.cancelUniqueWork(NAME)
                return@withLock
            }
            val request = OneTimeWorkRequestBuilder<MessagePollWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build())
                .build()
            val policy = when {
                fromWorker -> ExistingWorkPolicy.APPEND_OR_REPLACE
                keep -> ExistingWorkPolicy.KEEP
                else -> ExistingWorkPolicy.REPLACE
            }
            wm.enqueueUniqueWork(NAME, policy, request)
            Unit
        }

        private val lock = Mutex()

        /** Веднъж за версия: старата периодична задача (будеше процеса на всеки час) — отменена. */
        fun cancelLegacy(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(LEGACY_NAME)
        }
    }
}
