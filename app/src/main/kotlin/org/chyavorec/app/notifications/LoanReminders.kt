package org.chyavorec.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.R
import org.chyavorec.core.AppClock
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.LoanNotificationLog
import org.chyavorec.domain.model.LoanReminderKind
import org.chyavorec.domain.model.LoanReminderPlan
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * Локални напомняния за срок на заета книга (без мрежа): по една еднократна задача
 * на заемане и вид ([LoanReminderPlan]) — уникално име, REPLACE. Удължаване (нов срок)
 * пренасрочва същите задачи; върната книга просто не получава известие (проверка в
 * [LoanReminderWorker]). Планира се при всеки запис на заеманията в кеша.
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
                    // Без изискване за мрежа — работникът чете само кеша.
                    .setInitialDelay((at.toEpochMilli() - now.toEpochMilli()).coerceAtLeast(0), TimeUnit.MILLISECONDS)
                    .setInputData(workDataOf(KEY_LOAN to loan.loanId, KEY_DUE to dueOn.toString(), KEY_KIND to kind.name))
                    .addTag(TAG)
                    .build()
                wm.enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, request)
            }
        }
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
    }
}

class LoanReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val c = (applicationContext as? ChitalishteApp)?.container ?: return Result.success()
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
        if (c.authRepository.state.value !is AuthState.SignedIn) return Result.success()
        // Само кешът (без мрежа): книгата още е заета, срокът е същият, няма чакащо удължаване.
        val loan = c.libraryRepository.cachedLoans()?.data?.firstOrNull { it.loanId == loanId } ?: return Result.success()
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
        Notifier.show(applicationContext, Channel.LOANS, NotificationIds.dueSoon(loanId), title, text, "my/loans")
        c.settings.addLoanNotification(LoanNotificationLog.entry(loanId, dueOn, kind.name, today), today)
        return Result.success()
    }

    private companion object {
        val DAY_MONTH: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM")
    }
}
