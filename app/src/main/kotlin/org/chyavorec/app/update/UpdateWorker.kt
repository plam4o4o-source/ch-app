package org.chyavorec.app.update

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
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.notifications.Notifier
import java.util.concurrent.TimeUnit

/**
 * Автоматично обновяване във фона (на ~12 часа, при интернет и достатъчно батерия):
 * проверка → изтегляне (само по Wi-Fi/неограничена мрежа) → тихо инсталиране,
 * ако системата го позволява и приложението не се използва в момента;
 * иначе — едно известие за версията.
 */
class UpdateWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? ChitalishteApp ?: return Result.success()
        val c = app.container
        val updater = c.updater
        if (!updater.enabled || !c.settings.current().autoUpdate) return Result.success()

        val info = updater.check(userInitiated = false) ?: return Result.success()
        val file = if (updater.isUnmetered()) updater.download(info) else null

        if (file != null && updater.canUpdateSilently() && !updater.appInForeground()) {
            if (updater.install(background = true)) return Result.success()
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
        return Result.success()
    }

    companion object {
        private const val NAME = "app-update"

        fun schedule(context: Context, enabled: Boolean) {
            val wm = WorkManager.getInstance(context)
            if (!enabled) {
                wm.cancelUniqueWork(NAME)
                return
            }
            val request = PeriodicWorkRequestBuilder<UpdateWorker>(12, TimeUnit.HOURS, 3, TimeUnit.HOURS)
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build())
                .build()
            wm.enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.KEEP, request)
        }
    }
}
