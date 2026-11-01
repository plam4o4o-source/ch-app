package org.chyavorec.app.update

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.notifications.NotificationIds
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.core.Diagnostics
import org.chyavorec.data.update.UpdateInfo
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

/**
 * „Изтегли и инсталирай“ — изтеглянето като задача на WorkManager, за да продължи и след
 * излизане от приложението. Докато тече — известие с хода (процент) и бутон „Отказ“ (услуга
 * на преден план от вида dataSync). Накрая: приложението е отворено — инсталирането започва
 * веднага (както преди); иначе — известие „Готово — докосни, за да инсталираш“.
 *
 * Проверките (SHA-256, пакет, версия, подпис) и продължаването на прекъснато изтегляне са в
 * [AppUpdater.download]. Без разрешение за известия — ходът е само в диалога.
 */
class UpdateDownloadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val c = (applicationContext as? AppContainerOwner)?.appContainer ?: return Result.success()
        val updater = c.updater
        if (!updater.enabled) return Result.success()
        val notify = Notifier.canNotify(applicationContext)
        var info: UpdateInfo? = null
        val file = try {
            // Услугата на преден план — веднага, докато приложението още е отпред (току-що е натиснато
            // „Изтегли“): след „Отказ“ предишното изтегляне спира чак при следващото парче и новото
            // чака. Ако междувременно приложението отиде във фона, Android 12+ вече не пуска услугата,
            // а процесът без нея замръзва — изтеглянето не започва.
            val known = updater.currentInfo()
            if (notify && known != null) startForeground(known)
            // Версията се проверява наново: междувременно може да е спряна („paused“ в update.json) или
            // да има по-нова; нов процес (приложението е било затворено) още не я знае.
            val checked = updater.check(userInitiated = true) ?: return Result.success()
            info = checked
            if (notify && known?.versionCode != checked.versionCode) startForeground(checked)
            coroutineScope {
                val progress = if (notify) launch { showProgress(updater, checked) } else null
                try {
                    updater.download(checked)
                } finally {
                    withContext(NonCancellable) { progress?.cancelAndJoin() }
                }
            }
        } finally {
            NotificationManagerCompat.from(applicationContext).cancel(NotificationIds.UPDATE_PROGRESS)
        }
        val done = info ?: return Result.success()
        when {
            file != null -> if (updater.appInForeground()) {
                updater.install(background = false)
            } else {
                show(done, R.string.notif_update_ready, R.string.notif_update_downloaded_text)
            }
            // Неуспех — причината е в диалога; извън приложението — известие (отказът е без известие).
            updater.state.value is UpdateState.Failed && !updater.appInForeground() ->
                show(done, R.string.notif_update_failed, R.string.notif_update_failed_text)
        }
        return Result.success()
    }

    /**
     * Услуга на преден план (dataSync), за да не бъде спряно изтеглянето. От фона Android 12+
     * може да откаже — тогава известието е обикновено, а спряната задача продължава по-късно.
     */
    private suspend fun startForeground(info: UpdateInfo) {
        val n = progressNotification(applicationContext, info, if (info.sizeBytes != null) 0f else null)
        try {
            setForeground(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ForegroundInfo(NotificationIds.UPDATE_PROGRESS, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
                } else {
                    ForegroundInfo(NotificationIds.UPDATE_PROGRESS, n)
                },
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Diagnostics.note("update.foreground", e.javaClass.simpleName)
            post(n)
        }
    }

    /** Ходът от [AppUpdater.state] — при нов процент, но не по-често от [MIN_UPDATE] (системата ограничава честотата). */
    private suspend fun showProgress(updater: AppUpdater, info: UpdateInfo) {
        var lastPercent = -1
        var last = TimeSource.Monotonic.markNow()
        updater.state.collect { s ->
            if (s !is UpdateState.Downloading || s.info.versionCode != info.versionCode) return@collect
            val percent = s.progress?.let { (it * 100).toInt() } ?: -1
            if (percent == lastPercent || (lastPercent >= 0 && last.elapsedNow() < MIN_UPDATE)) return@collect
            lastPercent = percent
            last = TimeSource.Monotonic.markNow()
            post(progressNotification(applicationContext, info, s.progress))
        }
    }

    private fun post(n: Notification) {
        if (!Notifier.canNotify(applicationContext)) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        runCatching { NotificationManagerCompat.from(applicationContext).notify(NotificationIds.UPDATE_PROGRESS, n) }
    }

    private fun show(info: UpdateInfo, title: Int, text: Int) = Notifier.show(
        applicationContext, Channel.UPDATES, UpdateInstallReceiver.NOTIFICATION_ID,
        applicationContext.getString(title, info.versionName), applicationContext.getString(text), UpdateInstallReceiver.DEEP_LINK,
    )

    companion object {
        private const val NAME = "update-download"
        private val MIN_UPDATE = 500.milliseconds

        /** Второ натискане, докато тече — без второ изтегляне. Без условие за мрежа: без връзка грешката се вижда веднага. */
        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<UpdateDownloadWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(NAME, ExistingWorkPolicy.KEEP, request)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(NAME)
        }

        /** „Изтегляне на версия X — 45%“, с лента и „Отказ“; [progress] `null` — размерът не е известен. */
        internal fun progressNotification(context: Context, info: UpdateInfo, progress: Float?): Notification {
            // Каналите се създават във фон веднъж за версия — при много ранно известие може още да ги няма.
            context.getSystemService(NotificationManager::class.java)?.let { nm ->
                if (nm.getNotificationChannel(Channel.UPDATES.id) == null) Notifier.createChannels(context)
            }
            val percent = progress?.let { (it * 100).toInt().coerceIn(0, 100) }
            val title = if (percent != null) {
                context.getString(R.string.notif_update_downloading, info.versionName, percent)
            } else {
                context.getString(R.string.notif_update_downloading_unknown, info.versionName)
            }
            val open = PendingIntent.getActivity(
                context, NotificationIds.UPDATE_PROGRESS,
                LaunchIntents.openLink(context, UpdateInstallReceiver.DEEP_LINK).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            val cancel = PendingIntent.getBroadcast(
                context, NotificationIds.UPDATE_PROGRESS,
                Intent(context, UpdateInstallReceiver::class.java).setAction(UpdateInstallReceiver.ACTION_CANCEL_DOWNLOAD),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            return NotificationCompat.Builder(context, Channel.UPDATES.id)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(context, R.color.brand_gold_dark))
                .setContentTitle(title)
                .setProgress(100, percent ?: 0, percent == null)
                .setContentIntent(open)
                .addAction(R.drawable.ic_notification, context.getString(R.string.action_cancel), cancel)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSilent(true)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
        }
    }
}
