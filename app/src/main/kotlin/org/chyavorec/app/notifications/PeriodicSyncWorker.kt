package org.chyavorec.app.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.R
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.messages.MessageWorker
import org.chyavorec.app.update.UpdateInstallReceiver
import org.chyavorec.app.widget.ChitalishteWidget
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.LoanDueCalculator
import java.util.concurrent.TimeUnit

/**
 * Единствената периодична фонова задача (на ~6 часа, само при интернет и
 * достатъчно батерия). Замества трите отделни задачи отпреди — съобщения (~3 ч),
 * синхронизация (~12 ч) и обновяване (~12 ч) — за да се буди устройството
 * веднъж вместо три пъти. Всяка стъпка има собствен интервал и условие и не
 * пита мрежата, ако резултатът не трябва на никого:
 *
 * - съобщения от сайта и лични (при вход) — при всяко пускане, ако известията
 *   за съобщения са включени и разрешени;
 * - нова новина → известие — на 12 часа, ако е включено и известията са разрешени;
 * - заемания със срок → известие — на 12 часа, ако е включено и разрешено,
 *   или ако има уиджет (той показва броя книги за връщане);
 * - събития — на 12 часа, само ако има уиджет (напомнянията за събития са
 *   локални аларми и не зависят от тази задача);
 * - проверка за нова версия — веднъж на 24 часа (само APK извън Google Play).
 *
 * Уиджетът се опреснява оттук само ако е добавен на началния екран.
 */
class PeriodicSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? ChitalishteApp ?: return Result.success()
        val c = app.container
        val settings = c.settings.current()
        val now = c.clock.now().toEpochMilli()
        val canNotify = Notifier.canNotify(applicationContext)
        val widget = ChitalishteWidget.hasInstances(applicationContext)

        // Съобщения — всеки път (общите от сайта; личните само при влязъл читател).
        if (settings.notifyMessages && canNotify) {
            step { MessageWorker.refreshAndNotify(applicationContext, c) }
        }

        if (settings.notifyNews && canNotify && due(c, STEP_NEWS, now, SYNC_INTERVAL_MS)) {
            step { if (syncNews(c, settings)) c.settings.setLastSyncStep(STEP_NEWS, now) }
        }

        val notifyLoans = settings.notifyLoans && canNotify
        if ((notifyLoans || widget) && due(c, STEP_LOANS, now, SYNC_INTERVAL_MS)) {
            step { if (syncLoans(c, notifyLoans)) c.settings.setLastSyncStep(STEP_LOANS, now) }
        }

        // Събитията трябват само на уиджета (кеш до 5 минути — без излишни заявки).
        if (widget && due(c, STEP_EVENTS, now, SYNC_INTERVAL_MS)) {
            step {
                if (c.eventsRepository.events(force = false) is Outcome.Success) c.settings.setLastSyncStep(STEP_EVENTS, now)
            }
        }

        if (c.updater.enabled && settings.autoUpdate && now - c.settings.lastUpdateCheck.first() >= UPDATE_INTERVAL_MS) {
            step { checkForUpdate(c) }
        }

        // Опресняване и при смяна на деня (следващото събитие), но само ако има уиджет.
        if (widget) ChitalishteWidget.refresh(applicationContext)
        c.settings.setLastBackgroundSync(now)
        return Result.success()
    }

    /** Грешка в една стъпка не спира останалите; прекратяването на задачата се зачита. */
    private inline fun step(block: () -> Unit) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Следващият опит е при следващото пускане.
        }
    }

    private suspend fun due(c: AppContainer, step: String, now: Long, interval: Long): Boolean {
        val last = c.settings.lastSyncStep(step)
        // Часовникът е върнат назад (last > now) → стъпката се смята за дължима.
        return last > now || now - last >= interval
    }

    /** Нова новина на сайта → известие (първия път само се запомня). @return true при свежи данни. */
    private suspend fun syncNews(c: AppContainer, settings: AppSettings): Boolean {
        val synced = (c.newsRepository.latest(force = true) as? Outcome.Success)?.value ?: return false
        if (synced.fromCache) return false
        val newest = synced.data.firstOrNull() ?: return true
        val last = settings.lastNewsId
        if (last != null && last != newest.id) {
            Notifier.show(
                applicationContext, Channel.NEWS, NotificationIds.NEWS,
                applicationContext.getString(R.string.notif_new_article), newest.title, "news/${newest.id.hashCode()}",
            )
        }
        c.settings.setLastNewsId(newest.id)
        return true
    }

    /**
     * Заеманията (само при вход и наличен API). При [notify] — известие за
     * наближаващ/изтекъл срок, веднъж на статус; иначе само за кеша на уиджета.
     * @return true, ако няма какво да се тегли (без вход) или данните са свежи.
     */
    private suspend fun syncLoans(c: AppContainer, notify: Boolean): Boolean {
        c.authRepository.restore()
        if (c.authRepository.state.value !is AuthState.SignedIn) return true
        val synced = (c.libraryRepository.loans(force = true) as? Outcome.Success)?.value ?: return false
        if (synced.fromCache) return false
        if (!notify) return true
        val calc = LoanDueCalculator()
        val today = c.clock.today()
        val already = c.settings.notifiedLoans()
        val current = mutableSetOf<String>()
        for (loan in synced.data) {
            // Заявено (непотвърдено) удължаване → без „наближава срок“, докато библиотеката не отговори.
            val status = calc.reminderStatus(loan, today) ?: continue
            if (status == DueStatus.PLENTY_OF_TIME) continue
            val key = "${loan.loanId}:$status"
            current += key
            if (key in already) continue
            val (titleRes, id) = if (status == DueStatus.OVERDUE) {
                R.string.notif_overdue to NotificationIds.overdue(loan.loanId)
            } else {
                R.string.notif_due_soon to NotificationIds.dueSoon(loan.loanId)
            }
            Notifier.show(
                applicationContext, Channel.LOANS, id,
                applicationContext.getString(titleRes), loan.title, "my/loans",
            )
        }
        c.settings.setNotifiedLoans(current)
        return true
    }

    /**
     * Автоматично обновяване: проверка → изтегляне (само по Wi-Fi/неограничена
     * мрежа) → тихо инсталиране, ако системата го позволява и приложението не се
     * използва в момента; иначе — едно известие за версията.
     */
    private suspend fun checkForUpdate(c: AppContainer) {
        val updater = c.updater
        val info = updater.check(userInitiated = false) ?: return
        val file = if (updater.isUnmetered()) updater.download(info) else null

        if (file != null && updater.canUpdateSilently() && !updater.appInForeground()) {
            if (updater.install(background = true)) return
        }
        if (c.settings.notifiedUpdateCode() != info.versionCode) {
            val title = if (file != null) R.string.notif_update_ready else R.string.notif_update_available
            Notifier.show(
                applicationContext, Channel.UPDATES, UpdateInstallReceiver.NOTIFICATION_ID,
                applicationContext.getString(title, info.versionName),
                applicationContext.getString(R.string.notif_update_text), UpdateInstallReceiver.DEEP_LINK,
            )
            c.settings.setNotifiedUpdateCode(info.versionCode)
        }
    }

    companion object {
        private const val NAME = "periodic-sync"
        /** Предишните отделни задачи — отменят се, за да не се будят паралелно с тази. */
        private val LEGACY_NAMES = listOf("app-messages", "background-sync", "app-update")

        private const val STEP_NEWS = "news"
        private const val STEP_LOANS = "loans"
        private const val STEP_EVENTS = "events"
        /** Малко под 12 ч, за да не се пропусне цял 6-часов цикъл заради отклонение в планирането. */
        private const val SYNC_INTERVAL_MS = 11 * 60 * 60 * 1000L
        private const val UPDATE_INTERVAL_MS = 23 * 60 * 60 * 1000L

        fun schedule(context: Context) {
            val wm = WorkManager.getInstance(context)
            LEGACY_NAMES.forEach { wm.cancelUniqueWork(it) }
            val request = PeriodicWorkRequestBuilder<PeriodicSyncWorker>(6, TimeUnit.HOURS, 2, TimeUnit.HOURS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build())
                .build()
            // UPDATE (а не KEEP): вече инсталираните копия също минават на новия период/ограничения,
            // без да се нулира графикът на задачата.
            wm.enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
        }
    }
}
