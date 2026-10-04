package org.chyavorec.app.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.R
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.LoanDueCalculator
import java.util.concurrent.TimeUnit

/**
 * Фонова синхронизация (веднъж на 12 часа, само при интернет):
 * - нова новина на сайта → известие (ако е включено);
 * - заемане с наближаващ/изтекъл срок → известие (само при вход и наличен API).
 * Сроковете се изчисляват от реалните дати, всяко известие се показва веднъж на статус.
 */
class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? ChitalishteApp ?: return Result.success()
        val c = app.container
        val settings = c.settings.current()

        if (settings.notifyNews) {
            (c.newsRepository.latest(force = true) as? Outcome.Success)?.value?.let { synced ->
                val newest = synced.data.firstOrNull()
                if (newest != null && !synced.fromCache) {
                    val last = settings.lastNewsId
                    if (last != null && last != newest.id) {
                        Notifier.show(
                            applicationContext, Channel.NEWS, NEWS_ID,
                            applicationContext.getString(R.string.notif_new_article), newest.title, "news/${newest.id.hashCode()}",
                        )
                    }
                    c.settings.setLastNewsId(newest.id)
                }
            }
        }

        if (settings.notifyLoans) {
            c.authRepository.restore()
            if (c.authRepository.state.value is AuthState.SignedIn) {
                (c.libraryRepository.loans(force = true) as? Outcome.Success)?.value?.takeIf { !it.fromCache }?.let { synced ->
                    val calc = LoanDueCalculator()
                    val today = c.clock.today()
                    val already = c.settings.notifiedLoans()
                    val current = mutableSetOf<String>()
                    for (loan in synced.data) {
                        val status = calc.status(loan, today) ?: continue
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
                }
            }
        }
        c.settings.setLastBackgroundSync(c.clock.now().toEpochMilli())
        return Result.success()
    }

    companion object {
        private const val NAME = "background-sync"
        private const val NEWS_ID = NotificationIds.NEWS

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<SyncWorker>(12, TimeUnit.HOURS, 2, TimeUnit.HOURS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build())
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.KEEP, request)
        }
    }
}
