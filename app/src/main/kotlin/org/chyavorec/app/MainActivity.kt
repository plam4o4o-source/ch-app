package org.chyavorec.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.messages.MessagePollWorker
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.ui.ChitalishteRoot
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.EdgeToEdge
import org.chyavorec.app.ui.theme.SystemBarsAppearance
import org.chyavorec.app.util.AppLanguage
import org.chyavorec.core.logFailure
import org.chyavorec.domain.model.MessagePollPolicy

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
        // От край до край без оттеглените в Android 15 API (виж EdgeToEdge).
        EdgeToEdge.setUp(window)
        // Само при истинско стартиране — не и при пресъздаване (напр. завъртане) или при
        // отваряне от „Последни“ (Android 8–11 подава отново стария intent), иначе същото
        // известие би отваряло екрана отново и отново. Връзка, която не е била отворена
        // преди пресъздаването, идва от запазеното състояние.
        deepLink.value = savedInstanceState?.getString(LaunchIntents.KEY_PENDING_LINK)
        if (LaunchIntents.shouldHandle(intent?.flags ?: 0, restored = savedInstanceState != null)) consumeDeepLink(intent)
        else intent?.let { setIntent(LaunchIntents.consumed(it)) }
        val app = application as ChitalishteApp
        // Шрифтовете, мрежовият клиент и проверката на сесията — само при интерфейс, не при фонова задача.
        app.onUiStart()
        val container = app.container

        setContent {
            val settings by container.settings.settings.collectAsStateWithLifecycle(initialValue = null)
            val s: AppSettings? = settings
            // Темата се създава едва с настройките — първият кадър е с избраната палитра
            // (без преход от палитрата по подразбиране); дотогава е splash екранът.
            if (s != null) {
                ready = true
                ChitalishteTheme(mode = s.themeMode, dynamicColor = s.dynamicColor, style = s.themeStyle) {
                    SystemBarsAppearance {
                        ChitalishteRoot(container = container, settings = s, deepLink = deepLink)
                    }
                }
            }
        }
    }

    /**
     * Приложението е отворено: фоновата проверка за съобщения отново е на най-краткия
     * интервал, а 14-дневният срок без отваряне ([MessagePollPolicy]) започва отначало —
     * и следващата проверка се планира наново (или се отменя, ако известията са изключени
     * или забранени — напр. от системните настройки).
     */
    override fun onStart() {
        super.onStart()
        val container = (application as ChitalishteApp).container
        lifecycleScope.launch {
            runCatching {
                container.settings.markAppOpened(container.clock.now().toEpochMilli(), MessagePollPolicy.BASE_MILLIS, OPEN_RECORD_GAP_MS)
            }
            runCatching { MessagePollWorker.schedule(applicationContext, container) }.logFailure("startup.schedule-poll")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (LaunchIntents.shouldHandle(intent.flags, restored = false)) consumeDeepLink(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        deepLink.value?.let { outState.putString(LaunchIntents.KEY_PENDING_LINK, it) }
    }

    /**
     * Прочита deep link-а от известието (extra), от пряк път на иконата
     * (`chyavorec://app/<връзка>`) или от адрес на сайта (App Links, `https://chyavorec.org/...`)
     * и го премахва от intent-а (еднократен).
     */
    private fun consumeDeepLink(intent: Intent?) {
        val data = intent?.data
        val fromShortcut = if (intent?.action == ACTION_SHORTCUT && data?.scheme == SHORTCUT_SCHEME) {
            data.path.orEmpty().trim('/').ifBlank { null }
        } else null
        // Адресът минава през същото разпознаване като връзките в съдържанието (DeepLinks.forUrl).
        val fromWeb = if (intent?.action == Intent.ACTION_VIEW && data?.scheme == "https") data.toString() else null
        val link = fromShortcut ?: fromWeb
        deepLink.value = link ?: intent?.getStringExtra(Notifier.EXTRA_DEEP_LINK)
        intent?.removeExtra(Notifier.EXTRA_DEEP_LINK)
        if (link != null) intent?.data = null
        // Обработеният intent се заменя с обикновен — повторно четене не отваря нищо.
        if (intent != null) setIntent(LaunchIntents.consumed(intent))
    }

    companion object {
        /** Действие на статичните преки пътища (res/xml/shortcuts.xml); връзката е в `data`. */
        const val ACTION_SHORTCUT = "org.chyavorec.app.SHORTCUT"
        const val SHORTCUT_SCHEME = "chyavorec"
        /** Времето на отваряне се записва най-много веднъж на час (стига за 14-дневния срок). */
        private const val OPEN_RECORD_GAP_MS = 60 * 60 * 1000L
    }
}
