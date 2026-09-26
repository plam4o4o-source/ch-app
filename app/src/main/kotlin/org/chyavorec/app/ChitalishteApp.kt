package org.chyavorec.app

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.notifications.SyncWorker

class ChitalishteApp : Application(), ImageLoaderFactory, Configuration.Provider {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        Notifier.createChannels(this)
        SyncWorker.schedule(this)
    }

    /** Изображенията се кешират на диска — работят и офлайн. */
    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .okHttpClient { container.okHttp }
        .memoryCache { MemoryCache.Builder(this).maxSizePercent(0.2).build() }
        .diskCache { DiskCache.Builder().directory(cacheDir.resolve("image_cache")).maxSizeBytes(80L * 1024 * 1024).build() }
        .respectCacheHeaders(false)
        .crossfade(true)
        .build()

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setMinimumLoggingLevel(android.util.Log.ERROR).build()
}
