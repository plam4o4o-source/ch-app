package org.chyavorec.app.network

import android.content.Context
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkModule {
    /**
     * Един споделен клиент: само съвременен TLS, разумни timeouts, HTTP кеш за
     * изображения и статично съдържание. Никакво логване на тела/заглавки
     * (токените и личните данни не попадат в logcat).
     */
    fun okHttp(context: Context): OkHttpClient = OkHttpClient.Builder()
        .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.RESTRICTED_TLS))
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .callTimeout(45, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .cache(Cache(File(context.cacheDir, "http"), 30L * 1024 * 1024))
        .build()
}
