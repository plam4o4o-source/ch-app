package org.chyavorec.app.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.AppError
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanReminderPlan
import org.chyavorec.domain.model.RenewOutcomes

/**
 * Отговорът на библиотеката на заявка за удължаване → известие („удължено до…“ / „отказано: …“).
 * Вика се при всеки запис на заеманията в кеша (AppContainer — екраните и фоновото опресняване)
 * и пряко от фоновата задача. Чисто сравнение — [RenewOutcomes]; тук са състоянието и известията.
 */
object RenewOutcomeNotifier {
    /** Една проверка наведнъж — екранът и фоновата задача не показват един отговор два пъти. */
    private val lock = Mutex()

    /**
     * Сравнява [loans] със запомнените чакащи удължавания и показва новите отговори — само при
     * включени и разрешени известия за срокове (иначе само обновява състоянието). Нощем
     * (без отворено приложение) отговорите изчакват.
     * @return false, ако има отложен отговор (тихите часове).
     */
    suspend fun check(context: Context, c: AppContainer, loans: List<Loan>): Boolean = lock.withLock {
        val settings = c.settings
        val enabled = settings.current().notifyLoans && Notifier.canNotify(context)
        val watch = settings.renewWatch()
        val notified = settings.renewNotified()
        val now = RenewOutcomes.diff(watch, notified, loans)
        val defer = enabled && now.outcomes.isNotEmpty() && LoanReminderPlan.isQuiet(c.clock.now()) && !inForeground()
        val result = if (defer) RenewOutcomes.diff(watch, notified, loans, defer = true) else now
        if (enabled) result.outcomes.forEach { show(context, it) }
        settings.setRenewState(result.watch, result.notified)
        !defer
    }

    private fun inForeground(): Boolean =
        runCatching { ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) }.getOrDefault(false)

    private fun show(context: Context, outcome: RenewOutcomes.Outcome) {
        val loan = outcome.loan
        val (title, text) = when (outcome.kind) {
            RenewOutcomes.Kind.APPROVED -> context.getString(R.string.notif_renew_done_title) to
                context.getString(R.string.notif_renew_done, loan.title, Formatters.shortDate(context, loan.dueOn) ?: "—")
            RenewOutcomes.Kind.REJECTED -> context.getString(R.string.notif_renew_rejected_title) to
                (
                    loan.renewResult?.reason?.takeIf { it.isNotBlank() }?.let { context.getString(R.string.notif_renew_rejected, loan.title, it) }
                        ?: context.getString(R.string.notif_renew_rejected_noreason, loan.title)
                    )
        }
        if (outcome.kind == RenewOutcomes.Kind.APPROVED) {
            // Напомнянията за стария срок вече не важат.
            NotificationManagerCompat.from(context).apply {
                cancel(NotificationIds.dueSoon(loan.loanId))
                cancel(NotificationIds.overdue(loan.loanId))
            }
        }
        Notifier.show(
            context, Channel.LOANS, NotificationIds.renew(loan.loanId), title, text, "my/loans",
            lockScreenTitle = context.getString(R.string.notif_public_renew),
            category = NotificationCompat.CATEGORY_STATUS,
        )
    }
}

/**
 * Бутонът „Удължи“ в известията за срок (наближава, днес, просрочена). Приемникът не е
 * изнесен (само от собствения PendingIntent): веднага сменя известието на „изпраща се…“ и
 * пуска [RenewActionWorker], който праща заявката и с нея подменя известието.
 */
class RenewActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_RENEW) return
        val loanId = intent.getStringExtra(EXTRA_LOAN) ?: return
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION, 0).takeIf { it != 0 } ?: return
        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        Notifier.show(
            context, Channel.LOANS, notificationId, context.getString(R.string.notif_renew_sending), title, "my/loans",
            lockScreenTitle = context.getString(R.string.notif_public_renew), silent = true, category = NotificationCompat.CATEGORY_STATUS,
        )
        RenewActionWorker.enqueue(context, loanId, notificationId, title)
    }

    companion object {
        const val ACTION_RENEW = "org.chyavorec.app.action.RENEW_LOAN"
        const val EXTRA_LOAN = "loan"
        const val EXTRA_NOTIFICATION = "notification"
        const val EXTRA_TITLE = "title"

        /**
         * „Удължи“ за известието [notificationId] — само ако сървърът приема удължаване и
         * заемането може да се удължи (както бутонът в „Моите книги“). Иначе — без бутони.
         */
        suspend fun actionsFor(context: Context, c: AppContainer, loan: Loan, notificationId: Int): List<NotificationCompat.Action> {
            if (!loan.canRenew || loan.renewPending) return emptyList()
            val supported = try {
                c.authRepository.capabilities().renew
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Diagnostics.record("renew.action.caps", e)
                false
            }
            return if (supported) listOf(action(context, loan, notificationId)) else emptyList()
        }

        internal fun action(context: Context, loan: Loan, notificationId: Int): NotificationCompat.Action {
            val intent = Intent(context, RenewActionReceiver::class.java).apply {
                action = ACTION_RENEW
                putExtra(EXTRA_LOAN, loan.loanId)
                putExtra(EXTRA_NOTIFICATION, notificationId)
                putExtra(EXTRA_TITLE, loan.title)
            }
            // requestCode = ID-то на известието: уникално за заемането (и вида) — бутоните не се препокриват.
            val pending = PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            return NotificationCompat.Action.Builder(R.drawable.ic_notification, context.getString(R.string.notif_action_renew), pending).build()
        }
    }
}

/**
 * Заявката за удължаване от бутона в известието — и при неработещо приложение: сесията се
 * възстановява от хранилището. Резултатът подменя известието (без звук).
 */
class RenewActionWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val c = (applicationContext as? AppContainerOwner)?.appContainer ?: return Result.success()
        val loanId = inputData.getString(KEY_LOAN) ?: return Result.success()
        val notificationId = inputData.getInt(KEY_NOTIFICATION, 0).takeIf { it != 0 } ?: return Result.success()
        val title = inputData.getString(KEY_TITLE).orEmpty()
        return try {
            renew(c, loanId, notificationId, title)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Diagnostics.record("worker.renew-action", e)
            showFailure(notificationId, title, AppError.Unexpected(e.javaClass.simpleName))
            Result.success()
        }
    }

    private suspend fun renew(c: AppContainer, loanId: String, notificationId: Int, title: String): Result {
        c.authRepository.restore()
        when (c.authRepository.state.value) {
            is AuthState.SignedIn -> Unit
            // Сесията временно не се чете (Keystore) — не е изход: още няколко опита, после
            // „опитай пак по-късно“, а не „влез отново“.
            AuthState.Unavailable -> {
                if (runAttemptCount < MAX_RETRIES) return Result.retry()
                showFailure(notificationId, title, applicationContext.getString(R.string.notif_renew_session_unavailable))
                return Result.success()
            }
            else -> {
                showFailure(notificationId, title, AppError.Unauthorized)
                return Result.success()
            }
        }
        when (val r = c.libraryRepository.renew(loanId)) {
            is Outcome.Success -> {
                val loan = r.value
                val name = loan.title.ifBlank { title }
                val (head, text) = if (loan.renewPending) {
                    applicationContext.getString(R.string.notif_renew_sent) to applicationContext.getString(R.string.notif_renew_sent_text, name)
                } else {
                    applicationContext.getString(R.string.notif_renew_done_title) to
                        applicationContext.getString(R.string.notif_renew_done, name, Formatters.shortDate(applicationContext, loan.dueOn) ?: "—")
                }
                result(notificationId, head, text)
                // Чакащото удължаване се запомня веднага — отговорът на библиотеката ще дойде като известие.
                c.libraryRepository.cachedLoans()?.data?.let { RenewOutcomeNotifier.check(applicationContext, c, it) }
            }
            is Outcome.Failure -> {
                // Мрежата падна по средата — още няколко опита (WorkManager чака връзка).
                if (r.error == AppError.Network && runAttemptCount < MAX_RETRIES) return Result.retry()
                showFailure(notificationId, title, r.error)
            }
        }
        return Result.success()
    }

    private fun showFailure(notificationId: Int, title: String, error: AppError) = showFailure(notificationId, title, errorText(error))

    private fun showFailure(notificationId: Int, title: String, reason: String) {
        result(
            notificationId, applicationContext.getString(R.string.notif_renew_failed),
            if (title.isBlank()) reason else applicationContext.getString(R.string.notif_renew_failed_text, title, reason),
        )
    }

    private fun result(notificationId: Int, title: String, text: String) {
        Notifier.show(
            applicationContext, Channel.LOANS, notificationId, title, text, "my/loans",
            lockScreenTitle = applicationContext.getString(R.string.notif_public_renew), silent = true,
            category = NotificationCompat.CATEGORY_STATUS,
        )
    }

    /** Причината с думи (като в „Моите книги“, без Compose). */
    private fun errorText(error: AppError): String = applicationContext.getString(
        when (error) {
            AppError.Network -> R.string.error_network
            AppError.Unauthorized -> R.string.error_unauthorized
            AppError.NotFound -> R.string.error_not_found
            is AppError.NotAvailable -> R.string.na_renew
            is AppError.Conflict -> when (error.code) {
                AppError.Conflict.PENDING -> R.string.renew_conflict_pending
                AppError.Conflict.NOT_ALLOWED -> R.string.renew_conflict_not_allowed
                else -> R.string.renew_conflict_other
            }
            // Твърде много заявки, грешка на сървъра — „опитай пак по-късно“.
            else -> R.string.renew_conflict_other
        },
    )

    /** Android 8–11 пуска бързата задача като услуга на преден план — с това кратко известие. */
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val n = NotificationCompat.Builder(applicationContext, Channel.LOANS.id)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(applicationContext, R.color.brand_gold_dark))
            .setContentTitle(applicationContext.getString(R.string.notif_renew_sending))
            .setSilent(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
        return ForegroundInfo(NotificationIds.RENEW_PROGRESS, n)
    }

    companion object {
        const val KEY_LOAN = "loan"
        const val KEY_NOTIFICATION = "notification"
        const val KEY_TITLE = "title"
        private const val MAX_RETRIES = 3

        fun uniqueName(loanId: String) = "renew-action:$loanId"

        fun enqueue(context: Context, loanId: String, notificationId: Int, title: String) {
            val request = OneTimeWorkRequestBuilder<RenewActionWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setInputData(workDataOf(KEY_LOAN to loanId, KEY_NOTIFICATION to notificationId, KEY_TITLE to title))
                .build()
            // Второ натискане, докато първото чака/тече — без втора заявка.
            WorkManager.getInstance(context).enqueueUniqueWork(uniqueName(loanId), ExistingWorkPolicy.KEEP, request)
        }
    }
}
