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
import org.chyavorec.app.R
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.messages.MessageWorker
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.update.UpdateInstallReceiver
import org.chyavorec.app.widget.WidgetUpdater
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.LoanNotificationLog
import org.chyavorec.domain.model.LoanReminderPlan
import org.chyavorec.domain.model.MessagePollPolicy
import java.util.concurrent.TimeUnit

/**
 * Единствената периодична фонова задача (на ~6 часа, само при интернет и
 * достатъчно батерия). Замества трите отделни задачи отпреди — съобщения (~3 ч),
 * синхронизация (~12 ч) и обновяване (~12 ч) — за да се буди устройството
 * веднъж вместо три пъти. Всяка стъпка има собствен интервал и условие и не
 * пита мрежата, ако резултатът не трябва на никого:
 *
 * - съобщения от сайта и лични (при вход) — при всяко пускане, ако известията
 *   за съобщения са включени и разрешени; тези от сайта — само ако приложението е
 *   отваряно през последните 14 дни ([MessagePollPolicy.isDormant]);
 * - нова новина → известие — на 12 часа, ако е включено и известията са разрешени;
 * - заемания → известие за просрочие и за отговора на заявка за удължаване — на 12 часа, ако е включено и разрешено,
 *   или ако има уиджет (той показва броя книги за връщане); напомнянията за
 *   наближаващ срок са локални ([LoanReminderScheduler]) и не зависят от мрежата;
 * - „Искам да прочета“ с „Кажи ми, когато е на рафта“ → известие — при всяко пускане, само ако има
 *   такава книга и известията „Отново на рафта“ са включени; каталогът се пита условно (ETag, 304 без тяло)
 *   и не по-често от веднъж на 30 минути ([WantToReadAlerts.refreshAndCheck]); не и ако приложението „спи“;
 * - събития — на 12 часа, само ако има уиджет (напомнянията за събития са
 *   локални аларми и не зависят от тази задача);
 * - проверка за нова версия — веднъж на 24 часа (само APK извън Google Play).
 *
 * Уиджетът се опреснява оттук само ако е добавен на началния екран.
 */
class PeriodicSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val c = (applicationContext as? AppContainerOwner)?.appContainer ?: return Result.success()
        val settings = c.settings.current()
        val now = c.clock.now().toEpochMilli()
        val canNotify = Notifier.canNotify(applicationContext)
        val widget = WidgetUpdater.hasInstances(applicationContext)

        // Съобщения — всеки път (общите от сайта — и в MessagePollWorker, адаптивно 1–6 ч.; личните само
        // при влязъл читател). Без force: току-що свалените (до 10 мин.) съобщения от сайта не се теглят отново.
        // Приложението не е отваряно 14 дни — файлът от сайта не се пита (както в MessagePollWorker и
        // според политиката за поверителност); личните съобщения на влезлия читател — да.
        if (settings.notifyMessages && canNotify) {
            val dormant = MessagePollPolicy.isDormant(now, c.settings.messagePoll().lastOpen)
            step("sync.messages") { MessageWorker.refreshAndNotify(applicationContext, c, force = false, includeSite = !dormant) }
        }

        if (settings.notifyNews && canNotify && due(c, STEP_NEWS, now, SYNC_INTERVAL_MS)) {
            step("sync.news") { if (syncNews(c, settings)) c.settings.setLastSyncStep(STEP_NEWS, now) }
        }

        val notifyLoans = settings.notifyLoans && canNotify
        if ((notifyLoans || widget) && due(c, STEP_LOANS, now, SYNC_INTERVAL_MS)) {
            step("sync.loans") { if (syncLoans(c, notifyLoans)) c.settings.setLastSyncStep(STEP_LOANS, now) }
        }

        // „Кажи ми, когато е на рафта“ — само ако има такава книга, известията „Отново на рафта“ са включени и
        // приложението не „спи“ (14 дни неотваряно — както съобщенията от сайта). Каталогът — условна заявка (ETag).
        if (settings.notifyLibrary && canNotify && !MessagePollPolicy.isDormant(now, c.settings.messagePoll().lastOpen)) {
            step("sync.want-to-read") { WantToReadAlerts.refreshAndCheck(applicationContext, c) }
        }

        // Събитията трябват само на уиджета (кеш до 5 минути — без излишни заявки).
        if (widget && due(c, STEP_EVENTS, now, SYNC_INTERVAL_MS)) {
            step("sync.events") {
                if (c.eventsRepository.events(force = false) is Outcome.Success) c.settings.setLastSyncStep(STEP_EVENTS, now)
            }
        }

        if (c.updater.enabled && settings.autoUpdate && now - c.settings.lastUpdateCheck.first() >= UPDATE_INTERVAL_MS) {
            step("sync.update") { checkForUpdate(c) }
        }

        // Опресняване и при смяна на деня (следващото събитие), но само ако има уиджет.
        if (widget) WidgetUpdater.refresh(applicationContext)
        c.settings.setLastBackgroundSync(now)
        return Result.success()
    }

    /**
     * Грешка в една стъпка не спира останалите; прекратяването на задачата се зачита.
     * Грешката остава в локалния дневник под етикета [tag].
     */
    private inline fun step(tag: String, block: () -> Unit) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Следващият опит е при следващото пускане.
            Diagnostics.record(tag, e)
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
            // Със снимката на новината (ако се свали навреме) и „Сподели“; докосването я отваря.
            Notifier.show(
                applicationContext, Channel.NEWS, NotificationIds.NEWS,
                applicationContext.getString(R.string.notif_new_article), newest.title, DeepLinks.article(newest.id),
                actions = listOfNotNull(RichNotifications.shareAction(applicationContext, newest, NotificationIds.NEWS)),
                image = RichNotifications.image(applicationContext, c, newest.imageUrl),
            )
        }
        c.settings.setLastNewsId(newest.id)
        return true
    }

    /**
     * Заеманията (само при вход и наличен API). Записът в кеша сам пренасрочва
     * локалните напомняния за срок ([LoanReminderScheduler]). При [notify] — известие
     * за ПРОСРОЧЕНА книга, веднъж на заемане: само от свежи данни (кешът може да не знае,
     * че книгата е върната), само през деня и не в ден, в който за нея вече е имало известие.
     * „Наближава срок“ вече идва от напомнянията (2 дни преди и в деня на срока).
     * @return true, ако няма какво да се тегли (без вход) или данните са свежи и всичко е показано;
     *   false и при временно нечетима сесия.
     */
    private suspend fun syncLoans(c: AppContainer, notify: Boolean): Boolean {
        c.authRepository.restore()
        when (c.authRepository.state.value) {
            is AuthState.SignedIn -> Unit
            // Сесията временно не се чете (Keystore) — не е изход: стъпката остава дължима (следващото пускане).
            AuthState.Unavailable -> return false
            else -> return true
        }
        // Без force: ако стъпката „съобщения“ преди секунди е минала през `/v1/me/all`, заеманията
        // вече са свежи в кеша (до 3 мин.) — без втора заявка. Иначе — от мрежата, както досега.
        val synced = (c.libraryRepository.loans(force = false) as? Outcome.Success)?.value ?: return false
        if (synced.fromCache) return false
        // Отговор на заявка за удължаване („удължено до…“ / „отказано“) — нощем изчаква.
        val renewOnTime = RenewOutcomeNotifier.check(applicationContext, c, synced.data)
        if (!notify) return true
        val calc = LoanDueCalculator()
        val now = c.clock.now()
        val today = c.clock.today()
        val already = c.settings.notifiedLoans()
        val current = mutableSetOf<String>()
        var deferred = false
        for (loan in synced.data) {
            if (calc.reminderStatus(loan, today) != DueStatus.OVERDUE) continue
            val dueOn = LoanDueCalculator.parse(loan.dueOn) ?: continue
            val key = "${loan.loanId}:${DueStatus.OVERDUE}"
            if (key in already) {
                current += key
                continue
            }
            // Нощем — не; стъпката остава дължима и се опитва при следващото пускане.
            if (LoanReminderPlan.isQuiet(now)) {
                deferred = true
                continue
            }
            // Днес вече е имало известие за тази книга → утре.
            if (LoanNotificationLog.sentOn(c.settings.loanNotificationLog(), loan.loanId, today)) continue
            current += key
            val id = NotificationIds.overdue(loan.loanId)
            Notifier.show(
                applicationContext, Channel.LOANS, id,
                applicationContext.getString(R.string.notif_overdue), loan.title, "my/loans",
                lockScreenTitle = applicationContext.getString(R.string.notif_public_overdue),
                actions = RenewActionReceiver.actionsFor(applicationContext, c, loan, id),
            )
            c.settings.addLoanNotification(LoanNotificationLog.entry(loan.loanId, dueOn, LoanNotificationLog.OVERDUE, today), today)
        }
        c.settings.setNotifiedLoans(current)
        return !deferred && renewOnTime
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
        /** Предишните отделни задачи — отменят се (веднъж за версия), за да не се будят паралелно с тази. */
        private val LEGACY_NAMES = listOf("app-messages", "background-sync", "app-update")

        private const val STEP_NEWS = "news"
        private const val STEP_LOANS = "loans"
        private const val STEP_EVENTS = "events"
        /** Малко под 12 ч, за да не се пропусне цял 6-часов цикъл заради отклонение в планирането. */
        private const val SYNC_INTERVAL_MS = 11 * 60 * 60 * 1000L
        private const val UPDATE_INTERVAL_MS = 23 * 60 * 60 * 1000L

        /**
         * [update] = true — само веднъж за версия (вж. ChitalishteApp): отмяна на старите задачи и
         * UPDATE, за да минат и вече инсталираните копия на новия период/ограничения, без да се
         * нулира графикът. Иначе — KEEP: задачата се добавя само ако WorkManager я няма.
         */
        fun schedule(context: Context, update: Boolean) {
            val wm = WorkManager.getInstance(context)
            if (update) LEGACY_NAMES.forEach { wm.cancelUniqueWork(it) }
            val request = PeriodicWorkRequestBuilder<PeriodicSyncWorker>(6, TimeUnit.HOURS, 2, TimeUnit.HOURS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build())
                .build()
            val policy = if (update) ExistingPeriodicWorkPolicy.UPDATE else ExistingPeriodicWorkPolicy.KEEP
            wm.enqueueUniquePeriodicWork(NAME, policy, request)
        }
    }
}
