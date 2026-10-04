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
import okio.Path.Companion.toOkioPath
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.messages.MessageWorker
import org.chyavorec.app.notifications.SyncWorker
import org.chyavorec.app.update.UpdateWorker

class ChitalishteApp : Application(), SingletonImageLoader.Factory, Configuration.Provider {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        container.watchSignOut()
        Notifier.createChannels(this)
        SyncWorker.schedule(this)
        MessageWorker.schedule(this)
        UpdateWorker.schedule(this, container.updater.enabled)
    }

    /** Изображенията се кешират на диска — работят и офлайн (Coil 3 не зачита Cache-Control по подразбиране). */
    override fun newImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
        .components { add(OkHttpNetworkFetcherFactory(callFactory = { container.okHttp })) }
        .memoryCache { MemoryCache.Builder().maxSizePercent(context, 0.2).build() }
        .diskCache { DiskCache.Builder().directory(cacheDir.resolve("image_cache").toOkioPath()).maxSizeBytes(80L * 1024 * 1024).build() }
        .crossfade(true)
        .build()

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setMinimumLoggingLevel(android.util.Log.ERROR).build()
}
