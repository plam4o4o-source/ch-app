package org.chyavorec.app

import android.app.Application
import androidx.compose.ui.text.font.FontListFontFamily
import androidx.compose.ui.text.font.ResourceFont
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okio.Path.Companion.toOkioPath
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.diagnostics.AppDiagnostics
import org.chyavorec.core.logFailure
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.messages.MessagePollWorker
import org.chyavorec.app.notifications.PeriodicSyncWorker
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.Raleway

class ChitalishteApp : Application(), SingletonImageLoader.Factory, Configuration.Provider, AppContainerOwner {

    lateinit var container: AppContainer
        private set

    override val appContainer: AppContainer get() = container

    /** Фонови задачи на ниво процес (напр. планиране на WorkManager извън главната нишка). */
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** OkHttp без HTTP кеш за изображенията — Coil има собствен дисков кеш (иначе всяка снимка се пази двойно). */
    private val imageHttp by lazy { container.okHttp.newBuilder().cache(null).build() }

    override fun onCreate() {
        super.onCreate()
        // Локалният дневник на грешките (само на устройството) — и за сривовете.
        AppDiagnostics.install(this)
        container = AppContainer(this)
        // Преди наблюдателите — те също стигат до мрежовия клиент, но по-късно.
        appScope.launch { warmUp() }
        container.watchSignOut()
        container.watchLoanReminders()
        // Каналите за известия и инициализацията на WorkManager (база данни) не бива да бавят първия кадър.
        appScope.launch { setUpBackground() }
    }

    /**
     * Подготовка за първия кадър извън главната нишка (паралелно със създаването на
     * Activity). Нищо не се променя — само се изпълнява по-рано и на друга нишка:
     * - OkHttp клиентът (`by lazy` в AppContainer): изграждането му зарежда TLS
     *   (доверените сертификати на системата, SSLSocketFactory) — иначе това става на
     *   главната нишка при създаването на ViewModel-а на началния екран;
     * - шрифтовете (Cormorant, Raleway): Compose ги зарежда блокиращо при първото
     *   измерване на текст през Resources.getFont — системата пази заредения Typeface в
     *   свой кеш, така че първият кадър го взема наготово, без да разчита файла.
     */
    private fun warmUp() {
        runCatching { container.http }
        for (family in listOf(Cormorant, Raleway)) {
            (family as? FontListFontFamily)?.fonts?.forEach { font ->
                if (font is ResourceFont) runCatching { resources.getFont(font.resId) }
            }
        }
    }

    /**
     * Процесът се стартира и при всяко събуждане на фонова задача — затова пълната настройка
     * (канали, отмяна на старите задачи, UPDATE на периодичните) е само веднъж за версия и
     * език (имената на каналите са преведени). Иначе — само KEEP: задачите се добавят
     * отново единствено ако WorkManager ги е загубил. Notifier.show сам създава липсващ канал.
     */
    private suspend fun setUpBackground() {
        val stamp = "${BuildConfig.VERSION_CODE}:${resources.configuration.locales[0].toLanguageTag()}"
        val fresh = runCatching { container.settings.backgroundSetup() != stamp }.getOrDefault(true)
        if (fresh) runCatching { Notifier.createChannels(this) }
        // Една обща периодична задача (съобщения, новини, заемания, уиджет, обновяване).
        val sync = runCatching { PeriodicSyncWorker.schedule(this, update = fresh) }.logFailure("startup.schedule-sync")
        // Съобщенията от читалището — по-често (адаптивно 1–6 ч.), само статичният файл от сайта.
        val poll = runCatching { MessagePollWorker.schedule(this, update = fresh) }.logFailure("startup.schedule-poll")
        if (fresh && sync.isSuccess && poll.isSuccess) runCatching { container.settings.setBackgroundSetup(stamp) }
    }

    /**
     * Приложението е скрито (или системата иска памет във фонов режим): освобождаваме
     * индекса за търсене в каталога — при следващо търсене се зарежда отново от диска.
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN && ::container.isInitialized) {
            container.catalogRepository.trimMemory()
        }
    }

    /** Изображенията се кешират на диска — работят и офлайн (Coil 3 не зачита Cache-Control по подразбиране). */
    override fun newImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
        .components { add(OkHttpNetworkFetcherFactory(callFactory = { imageHttp })) }
        .memoryCache { MemoryCache.Builder().maxSizePercent(context, 0.2).build() }
        .diskCache { DiskCache.Builder().directory(cacheDir.resolve("image_cache").toOkioPath()).maxSizeBytes(80L * 1024 * 1024).build() }
        .crossfade(true)
        .build()

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setMinimumLoggingLevel(android.util.Log.ERROR).build()
}
