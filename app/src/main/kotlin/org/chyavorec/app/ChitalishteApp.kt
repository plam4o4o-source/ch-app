package org.chyavorec.app

import android.app.Application
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
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.notifications.PeriodicSyncWorker

class ChitalishteApp : Application(), SingletonImageLoader.Factory, Configuration.Provider {

    lateinit var container: AppContainer
        private set

    /** Фонови задачи на ниво процес (напр. планиране на WorkManager извън главната нишка). */
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** OkHttp без HTTP кеш за изображенията — Coil има собствен дисков кеш (иначе всяка снимка се пази двойно). */
    private val imageHttp by lazy { container.okHttp.newBuilder().cache(null).build() }

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        container.watchSignOut()
        // Каналите за известия и инициализацията на WorkManager (база данни) не бива да бавят първия кадър.
        appScope.launch {
            runCatching { Notifier.createChannels(this@ChitalishteApp) }
            // Една обща периодична задача (съобщения, новини, заемания, уиджет, обновяване).
            runCatching { PeriodicSyncWorker.schedule(this@ChitalishteApp) }
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
