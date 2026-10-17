package org.chyavorec.app.ui.screens.settings

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import org.chyavorec.app.R
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.notifications.Notifier

/** Отговорът при искане на разрешение за известия. */
enum class NotificationAccess {
    /** Известията ще се показват. */
    GRANTED,

    /** Отказ сега — системният прозорец може да се покаже пак при следващото включване. */
    DENIED,

    /**
     * Изключени за приложението, а системният прозорец вече не се показва (траен отказ,
     * Android под 13 или изключени ръчно) — остават само настройките на приложението.
     */
    BLOCKED,
}

/** Иска разрешение за известия (Android 13+) едва когато потребителят включи известие. */
class NotificationPermissionRequester internal constructor(private val launch: ((NotificationAccess) -> Unit) -> Unit) {
    /** [onResult] — дали известията вече ще се показват; без разрешение нищо не се включва. */
    fun requestWithResult(onResult: (NotificationAccess) -> Unit) = launch(onResult)
}

@Composable
fun rememberNotificationPermission(): NotificationPermissionRequester {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val pending = remember { arrayOfNulls<(NotificationAccess) -> Unit>(1) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        val access = when {
            Notifier.canNotify(context) -> NotificationAccess.GRANTED
            // Разрешено, но изключени в системата — прозорецът не помага.
            granted -> NotificationAccess.BLOCKED
            // След първия отказ системата пита пак; след втория („не питай повече“) — вече не.
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                activity?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) == true -> NotificationAccess.DENIED
            else -> NotificationAccess.BLOCKED
        }
        pending[0]?.invoke(access)
        pending[0] = null
    }
    return remember {
        NotificationPermissionRequester { cb ->
            when {
                Notifier.canNotify(context) -> cb(NotificationAccess.GRANTED)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPostPermission(context) -> {
                    pending[0] = cb
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                // Разрешението е налице (или не е нужно), но известията са изключени ръчно.
                else -> cb(NotificationAccess.BLOCKED)
            }
        }
    }
}

private fun hasPostPermission(context: Context): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED

/** Дали известията се показват: за приложението изобщо и кои от [channels] са изключени в системата. */
@Immutable
data class NotificationStatus(val appBlocked: Boolean, val blockedChannels: List<Channel>) {
    val anyBlocked: Boolean get() = appBlocked || blockedChannels.isNotEmpty()

    /** Дали известие от [channel] би се показало. */
    fun allows(channel: Channel): Boolean = !appBlocked && channel !in blockedChannels
}

fun notificationStatus(context: Context, channels: Collection<Channel>): NotificationStatus {
    val nm = NotificationManagerCompat.from(context)
    return NotificationStatus(
        appBlocked = !Notifier.canNotify(context),
        blockedChannels = channels.filter { nm.getNotificationChannelCompat(it.id)?.importance == NotificationManagerCompat.IMPORTANCE_NONE },
    )
}

/** Състоянието на известията — проверява се пак при всяко връщане към екрана (напр. от настройките). */
@Composable
fun rememberNotificationStatus(channels: List<Channel>): NotificationStatus {
    val context = LocalContext.current
    var status by remember(channels) { mutableStateOf(notificationStatus(context, channels)) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { status = notificationStatus(context, channels) }
    return status
}

/**
 * Системните настройки за известия на приложението (или на един канал); при липсващ
 * екран (някои производители) — общите данни за приложението.
 */
fun openNotificationSettings(context: Context, channel: Channel? = null) {
    val intent = if (channel != null) {
        Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            .putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
    } else {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    try {
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: ActivityNotFoundException) {
        runCatching {
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
        }
    }
}

/** Настройките за това, което пречи: един изключен канал — направо него, иначе — приложението. */
fun openNotificationSettings(context: Context, status: NotificationStatus) =
    openNotificationSettings(context, status.blockedChannels.singleOrNull()?.takeIf { !status.appBlocked })

/**
 * Съобщение „известията няма да се показват“ с бутон към настройките (при траен отказ или
 * изключени в системата; при обикновен отказ системата ще пита пак — без бутона).
 */
suspend fun SnackbarHostState.showNotificationsBlocked(context: Context, access: NotificationAccess, message: String, channel: Channel? = null) {
    val action = if (access == NotificationAccess.BLOCKED) context.getString(R.string.notifications_open_settings) else null
    val r = showSnackbar(message, actionLabel = action, withDismissAction = action == null, duration = SnackbarDuration.Long)
    if (r == SnackbarResult.ActionPerformed) openNotificationSettings(context, channel)
}

/** Лента над превключвателите: известията са спрени на ниво система — и как да се пуснат. */
@Composable
fun NotificationsBlockedBanner(status: NotificationStatus, modifier: Modifier = Modifier) {
    if (!status.anyBlocked) return
    val context = LocalContext.current
    val text = if (status.appBlocked) {
        stringResource(R.string.notifications_blocked_app)
    } else {
        stringResource(R.string.notifications_blocked_channels, status.blockedChannels.joinToString(", ") { context.getString(it.nameRes) })
    }
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Row(Modifier.padding(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Outlined.NotificationsOff, contentDescription = null, modifier = Modifier.padding(top = 2.dp))
            Column(Modifier.weight(1f)) {
                Text(text, style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = { openNotificationSettings(context, status) }, modifier = Modifier.align(Alignment.End)) {
                    Text(stringResource(R.string.notifications_open_settings))
                }
            }
        }
    }
}

/** Кратко обяснение преди системния прозорец при първото стартиране; системният — само при „Да“. */
@Composable
fun NotificationRationaleDialog(onYes: () -> Unit, onLater: () -> Unit) {
    AlertDialog(
        onDismissRequest = onLater,
        icon = { Icon(Icons.Outlined.NotificationsActive, contentDescription = null) },
        title = { Text(stringResource(R.string.notifications_rationale_title)) },
        text = { Text(stringResource(R.string.notifications_rationale_text)) },
        confirmButton = { TextButton(onClick = onYes) { Text(stringResource(R.string.notifications_rationale_yes)) } },
        dismissButton = { TextButton(onClick = onLater) { Text(stringResource(R.string.notifications_rationale_later)) } },
    )
}
