package org.chyavorec.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.ui.ChitalishteRoot
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.util.AppLanguage

class MainActivity : ComponentActivity() {

    private val deepLink = MutableStateFlow<String?>(null)
    private var ready by mutableStateOf(false)

    /** Избраният в настройките език (Android 8–12; на 13+ го прилага системата). */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLanguage.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Кратък splash: държим го само докато настройките се заредят (милисекунди).
        splash.setKeepOnScreenCondition { !ready }
        enableEdgeToEdge()
        // Само при истинско стартиране — не и при пресъздаване (напр. завъртане),
        // иначе същото известие би отваряло екрана отново и отново.
        if (savedInstanceState == null) consumeDeepLink(intent)
        val container = (application as ChitalishteApp).container

        setContent {
            val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = null)
            val s: AppSettings? = settings
            if (s != null) ready = true
            ChitalishteTheme(mode = s?.themeMode ?: org.chyavorec.app.ui.theme.ThemeMode.SYSTEM, dynamicColor = s?.dynamicColor ?: false) {
                if (s != null) {
                    ChitalishteRoot(container = container, settings = s, deepLink = deepLink)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        consumeDeepLink(intent)
    }

    /**
     * Прочита deep link-а от известието (extra) или от пряк път на иконата
     * (`chyavorec://app/<връзка>`) и го премахва от intent-а (еднократен).
     */
    private fun consumeDeepLink(intent: Intent?) {
        val data = intent?.data
        val fromShortcut = if (intent?.action == ACTION_SHORTCUT && data?.scheme == SHORTCUT_SCHEME) {
            data.path.orEmpty().trim('/').ifBlank { null }
        } else null
        deepLink.value = fromShortcut ?: intent?.getStringExtra(Notifier.EXTRA_DEEP_LINK)
        intent?.removeExtra(Notifier.EXTRA_DEEP_LINK)
        if (fromShortcut != null) intent?.data = null
    }

    companion object {
        /** Действие на статичните преки пътища (res/xml/shortcuts.xml); връзката е в `data`. */
        const val ACTION_SHORTCUT = "org.chyavorec.app.SHORTCUT"
        const val SHORTCUT_SCHEME = "chyavorec"
    }
}
