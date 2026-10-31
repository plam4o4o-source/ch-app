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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.data.local.AppSettings
import org.chyavorec.app.messages.MessagePollWorker
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.ui.ChitalishteRoot
import org.chyavorec.app.ui.navigation.AppShortcuts
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
     * Прочита deep link-а от собственото известие или уиджет (extra), от пряк път на иконата
     * (`chyavorec://app/<връзка>`) или от адрес на сайта (App Links, `https://chyavorec.org/...`)
     * и го премахва от intent-а (еднократен). Чужди intent-и (MainActivity е exported) не
     * отварят произволна връзка или страница — виж [LaunchIntents.linkOf].
     */
    private fun consumeDeepLink(intent: Intent?) {
        if (intent == null) return
        val siteBaseUrl = (application as ChitalishteApp).container.config.siteBaseUrl
        val link = LaunchIntents.linkOf(intent, siteBaseUrl)
        deepLink.value = link
        // Отворено от пряк път — ползването му се отчита (извън главната нишка — IPC към системата).
        if (link != null && intent.action == LaunchIntents.ACTION_SHORTCUT) {
            lifecycleScope.launch(Dispatchers.IO) { AppShortcuts.launched(applicationContext, link) }
        }
        intent.removeExtra(Notifier.EXTRA_DEEP_LINK)
        intent.data = null
        // Обработеният intent се заменя с обикновен — повторно четене не отваря нищо.
        setIntent(LaunchIntents.consumed(intent))
    }

    companion object {
        /** Времето на отваряне се записва най-много веднъж на час (стига за 14-дневния срок). */
        private const val OPEN_RECORD_GAP_MS = 60 * 60 * 1000L
    }
}
