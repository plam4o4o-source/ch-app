package org.chyavorec.app

import android.app.Application
import android.os.StrictMode
import androidx.compose.ui.text.font.FontListFontFamily
import androidx.compose.ui.text.font.FontStyle
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.Path.Companion.toOkioPath
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.diagnostics.AppDiagnostics
import org.chyavorec.core.logFailure
import org.chyavorec.data.site.SiteImageAccept
import org.chyavorec.app.notifications.LegacyWork
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.messages.MessagePollWorker
import org.chyavorec.app.notifications.PeriodicSyncWorker
import org.chyavorec.app.ui.navigation.AppShortcuts
import org.chyavorec.app.ui.share.ShareFiles
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.Raleway
import org.chyavorec.app.widget.WidgetUpdater
import java.util.concurrent.atomic.AtomicBoolean

class ChitalishteApp : Application(), SingletonImageLoader.Factory, Configuration.Provider, AppContainerOwner {

    lateinit var container: AppContainer
        private set

    override val appContainer: AppContainer get() = container

    /** Фонови задачи на ниво процес (напр. планиране на WorkManager извън главната нишка). */
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * OkHttp без HTTP кеш за изображенията — Coil има собствен дисков кеш (иначе всяка снимка се пази двойно).
     * Към chyavorec.org — `Accept` с WebP/AVIF, за да не връща оптимизаторът на Vercel JPEG.
     */
    private val imageHttp by lazy {
        container.okHttp.newBuilder().cache(null)
            .addInterceptor(SiteImageAccept.Interceptor(android.os.Build.VERSION.SDK_INT))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) detectLeaks()
        // Локалният дневник на грешките (само на устройството) — и за сривовете.
        AppDiagnostics.install(this)
        container = AppContainer(this)
        // Наблюдателите на входа/изхода тръгват едва със създаването на AuthRepository — фоново
        // събуждане, което не стига до читателските данни, не пипа Keystore-а.
        container.watchReader()
        // Каналите за известия и инициализацията на WorkManager (база данни) не бива да бавят първия кадър.
        appScope.launch { setUpBackground() }
    }

    /**
     * Само в debug: незатворен ресурс (курсор, поток, отговор на OkHttp, Surface…) или
     * неотписан приемник — в logcat със стека на мястото, където е отворен (тагът StrictMode).
     * В release е изрязано (BuildConfig.DEBUG е константа).
     */
    private fun detectLeaks() {
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .build(),
        )
    }

    private val uiStarted = AtomicBoolean(false)

    private companion object {
        /** Изчакване на KEEP планирането на проверката за съобщения, когато е стартиран интерфейс. */
        const val KEEP_POLL_DELAY_MS = 3_000L
    }

    /**
     * Стартира интерфейсът (MainActivity.onCreate), а не само фонова задача — веднъж за процес:
     * - подготовка за първия кадър ([warmUp]) — във фоновите събуждания не трябва;
     * - наблюдението на каталога за „Искам да прочета“ ([org.chyavorec.app.notifications.WantToReadAlerts.watch]);
     * - проверка на сесията: при старт без сесия („не ме помни“ и спрян процес) следите на
     *   предишния читател се изчистват веднага (AppContainer.watchReader), а не едва при първия
     *   екран, който стигне до читателските данни; временно нечетима сесия (фоново събуждане
     *   преди това) се чете отново;
     * - уиджетът ([WidgetUpdater.onUiStart]).
     */
    fun onUiStart() {
        if (!uiStarted.compareAndSet(false, true)) return
        // Отключването на личните екрани изтича във фонов режим и при угаснал екран.
        container.watchPersonalUnlock()
        appScope.launch { warmUp() }
        // Свалените PDF-и: по-старите от седмица, над 50 MB общо и прекъснатите (.part) — вън.
        // Вестникът пази готовите статии година (офлайн четене), но недосвалените .part не бива да стоят толкова.
        appScope.launch(Dispatchers.IO) { pruneDownloadsOnStart(container) }
        // Картинките за споделяне (и личните слайдове от „Моята читателска година“) — по-старите от час.
        appScope.launch(Dispatchers.IO) { runCatching { ShareFiles.prune(this@ChitalishteApp) }.logFailure("startup.share") }
        // Само тук нечетимата сесия се брои към изхода — фоновите събуждания не броят.
        appScope.launch { runCatching { container.authRepository.restore(countLaunch = true) }.logFailure("startup.restore") }
        // „Кажи ми, когато е на рафта“ — при всеки нов каталог, приет на преден план (фоновият — PeriodicSyncWorker).
        appScope.launch { org.chyavorec.app.notifications.WantToReadAlerts.watch(this@ChitalishteApp, container) }
        // Уиджетът: генерираният преглед в избора (Android 15+) и опресняване на копията — във фон.
        WidgetUpdater.onUiStart(this)
    }

    /**
     * Подготовка за първия кадър извън главната нишка (паралелно със създаването на
     * Activity). Нищо не се променя — само се изпълнява по-рано и на друга нишка. Три
     * независими задачи, по реда на нуждата от тях:
     * - шрифтовете (Cormorant, Raleway): Compose ги зарежда блокиращо при първото
     *   измерване на текст през Resources.getFont — системата пази заредения Typeface в
     *   свой кеш, така че първият кадър го взема наготово. Първи и сами: ако главната нишка
     *   стигне до текста преди тях, зарежда ги сама. Без курсива (само за цитати и статии);
     * - OkHttp клиентът и кешираните данни на „Начало“ ([warmData]);
     * - украсата на първите кадри ([warmDecor]) — шейдърът на шевицата, формите на раздела,
     *   плочката на лентата. Шейдърът на фолиото се превежда едва след първия кадър (ShaderWarmup).
     */
    private fun warmUp() {
        appScope.launch { warmFonts() }
        appScope.launch { warmData() }
        appScope.launch { warmDecor() }
    }

    private fun warmFonts() {
        for (family in listOf(Cormorant, Raleway)) {
            (family as? FontListFontFamily)?.fonts?.forEach { font ->
                if (font is ResourceFont && font.style != FontStyle.Italic) runCatching { resources.getFont(font.resId) }
            }
        }
    }

    /**
     * Мрежовият клиент (`by lazy` в AppContainer): изграждането му зарежда TLS (доверените
     * сертификати на системата, SSLSocketFactory) — иначе това става на главната нишка при
     * създаването на ViewModel-а на началния екран. После — кешираното, което ще чете
     * „Начало“ (новини с пълните текстове, събития, обобщение на каталога): хранилищата и
     * зависимостите им се строят тук, а `cached()` помни резултата в паметта — ViewModel-ът го
     * взема без диск и без декодиране.
     */
    private suspend fun warmData() {
        runCatching { container.http }
        coroutineScope {
            launch { runCatching { container.newsRepository.cached() }.logFailure("startup.warm-news") }
            launch { runCatching { container.eventsRepository.cachedRolled() }.logFailure("startup.warm-events") }
            launch { runCatching { container.catalogRepository.homeSummary() }.logFailure("startup.warm-catalog") }
        }
    }

    private suspend fun warmDecor() {
        // Шейдърът на шевицата (втората секунда на „Начало“) се превежда в нишката на пула, без да чакаме.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            runCatching { org.chyavorec.app.ui.components.ShaderPool.prepare(org.chyavorec.app.ui.components.ShaderPool.WEAVE) }
        }
        // Формите на индикатора на раздела (graphics-shapes) са в първия кадър на лентата.
        runCatching { org.chyavorec.app.ui.components.TabRosetteShapes.morph }
        // Плочката на везаната лента на „Начало“ — иначе се рисува на главната нишка в първия кадър.
        runCatching {
            val dark = when (container.settings.current().themeMode) {
                org.chyavorec.app.ui.theme.ThemeMode.LIGHT -> false
                org.chyavorec.app.ui.theme.ThemeMode.DARK -> true
                org.chyavorec.app.ui.theme.ThemeMode.SYSTEM ->
                    (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                        android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
            org.chyavorec.app.ui.screens.home.prewarmHomeBand(resources.displayMetrics.density, dark)
        }.logFailure("startup.warm-band")
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
        // Съобщенията от читалището — по-често (адаптивно 1–6 ч.), само статичният файл от сайта:
        // еднократна задача за следващата дължима проверка (вж. MessagePollWorker). Старата
        // периодична (на всеки час) се отменя; KEEP — само ако веригата е изгубена.
        if (fresh) runCatching { MessagePollWorker.cancelLegacy(this) }.logFailure("startup.cancel-poll")
        // При старт на интерфейс MainActivity.onStart планира проверката сама (REPLACE) — KEEP не е
        // спешен и не се състезава с първия кадър за WorkManager и настройките.
        if (!fresh && uiStarted.get()) delay(KEEP_POLL_DELAY_MS)
        val poll = runCatching { MessagePollWorker.schedule(this, container, keep = true) }.logFailure("startup.schedule-poll")
        if (fresh) {
            // Остатъците от по-старите версии: задачите на уиджета с Glance и динамичният „Искам да прочета“.
            withContext(Dispatchers.IO) {
                runCatching { LegacyWork.cancelGlance(this@ChitalishteApp) }.logFailure("startup.legacy-work")
                runCatching { AppShortcuts.removeLegacy(this@ChitalishteApp) }.logFailure("startup.legacy-shortcut")
            }
        }
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

    /**
     * Нова конфигурация на живия процес — и сменен език от системните настройки (без
     * пресъздаване на процеса): уиджетът — на новия, без да чака отваряне на приложението.
     */
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        WidgetUpdater.refreshIfLanguageChanged(this)
    }

    /** Изображенията се кешират на диска — работят и офлайн (Coil 3 не зачита Cache-Control по подразбиране). */
    override fun newImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
        .components { add(OkHttpNetworkFetcherFactory(callFactory = { imageHttp })) }
        .memoryCache { MemoryCache.Builder().maxSizePercent(context, 0.15).build() }
        .diskCache { DiskCache.Builder().directory(cacheDir.resolve("image_cache").toOkioPath()).maxSizeBytes(80L * 1024 * 1024).build() }
        .crossfade(true)
        .build()

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setMinimumLoggingLevel(android.util.Log.ERROR).build()
}

/**
 * При старт на интерфейса (сваляне още не тече): документите и вестникът — без прекъснатите
 * `.part` и без остарелите по своя срок (документите — седмица, вестникът — година). Блокиращо.
 */
internal fun pruneDownloadsOnStart(container: AppContainer) {
    runCatching { container.documents.prune(dropPartial = true) }.logFailure("startup.documents")
    runCatching { container.newspaper.prune(dropPartial = true) }.logFailure("startup.newspaper")
}
