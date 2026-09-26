package org.chyavorec.app.update

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import androidx.core.content.IntentCompat
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.R
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.notifications.Notifier

/**
 * Резултатът от [PackageInstaller] за самообновяването. Не е експортиран —
 * само системата (от името на това приложение) може да го извика.
 */
class UpdateInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as? ChitalishteApp ?: return
        val updater = app.container.updater
        when (val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                val background = intent.getBooleanExtra(EXTRA_BACKGROUND, false)
                val confirm = IntentCompat.getParcelableExtra(intent, Intent.EXTRA_INTENT, Intent::class.java)
                if (background || confirm == null) {
                    // От фона не отваряме системен диалог — сесията се отказва и
                    // потребителят получава известие; инсталирането продължава при докосване.
                    val sessionId = intent.getIntExtra(PackageInstaller.EXTRA_SESSION_ID, -1)
                    if (sessionId >= 0) runCatching { context.packageManager.packageInstaller.abandonSession(sessionId) }
                    updater.onNeedsConfirmation()
                    updater.currentInfo()?.let { info ->
                        Notifier.show(
                            context, Channel.UPDATES, NOTIFICATION_ID,
                            context.getString(R.string.notif_update_ready, info.versionName),
                            context.getString(R.string.notif_update_text), DEEP_LINK,
                        )
                    }
                } else {
                    runCatching { context.startActivity(confirm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
                        .onFailure { updater.onInstallResult(PackageInstaller.STATUS_FAILURE) }
                }
            }
            else -> updater.onInstallResult(status)
        }
    }

    companion object {
        const val EXTRA_BACKGROUND = "org.chyavorec.app.update.BACKGROUND"
        const val NOTIFICATION_ID = 700
        const val DEEP_LINK = "update"
    }
}
