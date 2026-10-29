package org.chyavorec.app.notifications

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withTimeoutOrNull
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.LoanNotificationLog
import org.chyavorec.domain.model.LoanReminderKind
import org.chyavorec.domain.model.LoanReminderPlan
import org.chyavorec.domain.model.LoanReminderSync
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * Локални напомняния за срок на заета книга: по една еднократна задача на заемане и
 * вид ([LoanReminderPlan]) — уникално име, REPLACE. Удължаване (нов срок) пренасрочва
 * същите задачи; върнатата книга се отменя, а работникът проверява отново преди
 * известие. Пренасрочват се само променените заемания ([LoanReminderSync], [applyChanges]).
 */
class LoanReminderScheduler(private val context: Context, private val clock: AppClock) {

    fun schedule(loans: List<Loan>) {
        val wm = WorkManager.getInstance(context)
        val now = clock.now()
        for (loan in loans) {
            val dueOn = LoanDueCalculator.parse(loan.dueOn)
            for (kind in LoanReminderKind.entries) {
                val name = uniqueName(loan.loanId, kind)
                val at = dueOn?.let { LoanReminderPlan.at(kind, it, now) }
                if (dueOn == null || at == null) {
                    // Няма какво да се напомня (минал момент/без срок) — без остатък от стария срок.
                    wm.cancelUniqueWork(name)
                    continue
                }
                val request = OneTimeWorkRequestBuilder<LoanReminderWorker>()
                    // Без изискване за мрежа — без интернет работникът чете кеша.
                    .setInitialDelay((at.toEpochMilli() - now.toEpochMilli()).coerceAtLeast(0), TimeUnit.MILLISECONDS)
                    // Повторен опит (временно нечетима сесия) — след 5 мин, после двойно по-дълго.
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5, TimeUnit.MINUTES)
                    .setInputData(workDataOf(KEY_LOAN to loan.loanId, KEY_DUE to dueOn.toString(), KEY_KIND to kind.name))
                    .addTag(TAG)
                    .build()
                wm.enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, request)
            }
        }
    }

    /** Отменя напомнянията за [loanIds] (напр. върнати книги). */
    fun cancel(loanIds: Collection<String>) {
        if (loanIds.isEmpty()) return
        val wm = WorkManager.getInstance(context)
        for (id in loanIds) for (kind in LoanReminderKind.entries) wm.cancelUniqueWork(uniqueName(id, kind))
    }

    /** Прилага разликата от [LoanReminderSync.diff] — непроменените заемания не се пипат. */
    fun applyChanges(changes: LoanReminderSync.Changes) {
        cancel(changes.cancel)
        if (changes.schedule.isNotEmpty()) schedule(changes.schedule)
    }

    /** При изход и при изключени известия за срокове. */
    fun cancelAll() {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
    }

    companion object {
        const val TAG = "loan-due"
        const val KEY_LOAN = "loan"
        const val KEY_DUE = "due"
        const val KEY_KIND = "kind"

        fun uniqueName(loanId: String, kind: LoanReminderKind) = "$TAG:$loanId:${kind.name}"

        /**
         * Колко [LoanReminderWorker] вървят в момента. Докато има такъв, пренасрочването чака
         * (AppContainer.watchLoanReminders): REPLACE на вървящата задача би я прекъснал —
         * пропуснато или (с наваксването „след минута“) повторено известие.
         */
        private val running = MutableStateFlow(0)

        internal fun workerStarted() = running.update { it + 1 }
        internal fun workerFinished() = running.update { (it - 1).coerceAtLeast(0) }

        /** Изчаква да приключат вървящите напомняния. */
        suspend fun awaitIdle() {
            running.first { it == 0 }
        }
    }
}

/**
 * Показва напомнянето за срок. Никога не пренасрочва (нито пряко, нито чрез
 * AppContainer.watchLoanReminders — то чака края на работника, вж. [LoanReminderScheduler.awaitIdle]).
 */
class LoanReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        LoanReminderScheduler.workerStarted()
        try {
            return remind()
        } catch (e: Exception) {
            // Грешката остава в локалния дневник; WorkManager я отчита както досега.
            Diagnostics.record("worker.loan-reminder", e)
            throw e
        } finally {
            LoanReminderScheduler.workerFinished()
        }
    }

    private suspend fun remind(): Result {
        val c = (applicationContext as? AppContainerOwner)?.appContainer ?: return Result.success()
        val loanId = inputData.getString(LoanReminderScheduler.KEY_LOAN) ?: return Result.success()
        val dueOn = inputData.getString(LoanReminderScheduler.KEY_DUE)?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
            ?: return Result.success()
        val kind = inputData.getString(LoanReminderScheduler.KEY_KIND)
            ?.let { k -> LoanReminderKind.entries.firstOrNull { it.name == k } } ?: return Result.success()

        if (!c.settings.current().notifyLoans || !Notifier.canNotify(applicationContext)) return Result.success()
        // Закъснение (Doze/рестарт) до вечерта или до друг ден → без известие.
        val now = c.clock.now()
        if (!LoanReminderPlan.deliverable(kind, dueOn, now)) return Result.success()
        c.authRepository.restore()
        when (c.authRepository.state.value) {
            is AuthState.SignedIn -> Unit
            // Сесията временно не се чете (Keystore) — не е изход: нов опит по-късно (докато е в своя
            // ден). „Успех“ би изгубил напомнянето — планът го води за насрочено и не го пренасрочва.
            AuthState.Unavailable -> return if (runAttemptCount < MAX_UNAVAILABLE_RETRIES) Result.retry() else Result.success()
            else -> return Result.success()
        }
        // Книгата още е заета, срокът е същият, няма чакащо удължаване — по възможно най-пресните данни.
        val loan = currentLoans(c)?.firstOrNull { it.loanId == loanId } ?: return Result.success()
        if (LoanDueCalculator.parse(loan.dueOn) != dueOn || loan.renewPending) return Result.success()

        val today = c.clock.today()
        val log = c.settings.loanNotificationLog()
        if (LoanNotificationLog.wasSent(log, loanId, dueOn, kind.name) || LoanNotificationLog.sentOn(log, loanId, today)) {
            return Result.success()
        }
        val (title, text) = when (kind) {
            LoanReminderKind.BEFORE -> applicationContext.getString(R.string.notif_due_soon) to
                applicationContext.getString(R.string.notif_due_on, loan.title, dueOn.format(DAY_MONTH))
            LoanReminderKind.DUE_DAY -> applicationContext.getString(R.string.notif_due_today) to loan.title
        }
        // Едно ID за двете напомняния: „днес“ заменя „наближава“ в лентата.
        val id = NotificationIds.dueSoon(loanId)
        Notifier.show(
            applicationContext, Channel.LOANS, id, title, text, "my/loans",
            actions = RenewActionReceiver.actionsFor(applicationContext, c, loan, id),
        )
        c.settings.addLoanNotification(LoanNotificationLog.entry(loanId, dueOn, kind.name, today), today)
        return Result.success()
    }

    /**
     * Заеманията за проверката. Кешът се опреснява от общата задача на ~11 ч. — книгата
     * може вече да е върната. Затова при интернет и кеш, по-стар от [FRESH_ENOUGH_MS],
     * първо кратка заявка (до [FETCH_TIMEOUT_MS]); при неуспех — кешът, както досега.
     */
    private suspend fun currentLoans(c: AppContainer): List<Loan>? {
        val cached = c.libraryRepository.cachedLoans()
        val age = cached?.let { c.clock.now().toEpochMilli() - it.syncedAt.toEpochMilli() }
        val stale = age == null || age !in 0 until FRESH_ENOUGH_MS
        if (stale && c.connectivity.isOnline()) {
            val fresh = try {
                withTimeoutOrNull(FETCH_TIMEOUT_MS) { c.libraryRepository.loans(force = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Diagnostics.record("worker.loan-reminder.fetch", e)
                null
            }
            (fresh as? Outcome.Success)?.value?.let { return it.data }
            // Изход/отнет достъп → без известие.
            if ((fresh as? Outcome.Failure)?.error is AppError.Unauthorized) return null
        }
        return cached?.data
    }

    internal companion object {
        val DAY_MONTH: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM")
        const val FRESH_ENOUGH_MS = 30 * 60 * 1000L
        const val FETCH_TIMEOUT_MS = 15_000L
        /** Опитите при временно нечетима сесия: през 5, 10, 20, 40, 80 мин ([LoanReminderScheduler.schedule]). */
        const val MAX_UNAVAILABLE_RETRIES = 5
    }
}
