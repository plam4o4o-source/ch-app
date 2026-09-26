package org.chyavorec.app.ui.screens.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.chyavorec.app.notifications.Notifier

/** Иска разрешение за известия (Android 13+) едва когато потребителят включи известие. */
class NotificationPermissionRequester internal constructor(private val launch: ((Boolean) -> Unit) -> Unit) {
    fun requestWithResult(onResult: (Boolean) -> Unit) = launch(onResult)
    /** Изпълнява [onGranted] независимо от отговора — известието просто няма да се покаже без разрешение. */
    fun request(onGranted: () -> Unit) = launch { onGranted() }
}

@Composable
fun rememberNotificationPermission(): NotificationPermissionRequester {
    val context = LocalContext.current
    val pending = remember { arrayOfNulls<(Boolean) -> Unit>(1) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        pending[0]?.invoke(granted)
        pending[0] = null
    }
    return remember {
        NotificationPermissionRequester { cb ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !Notifier.canNotify(context)) {
                pending[0] = cb
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else cb(true)
        }
    }
}
