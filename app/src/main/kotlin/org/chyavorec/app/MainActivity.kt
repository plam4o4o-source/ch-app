package org.chyavorec.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {

    private val deepLink = MutableStateFlow<String?>(null)
    private var ready by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Кратък splash: държим го само докато настройките се заредят (милисекунди).
        splash.setKeepOnScreenCondition { !ready }
        enableEdgeToEdge()
        deepLink.value = intent?.getStringExtra(Notifier.EXTRA_DEEP_LINK)
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
        deepLink.value = intent.getStringExtra(Notifier.EXTRA_DEEP_LINK)
    }
}
