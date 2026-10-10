package org.chyavorec.app.network

import android.content.Context
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkModule {
    /**
     * Един споделен клиент: само съвременен TLS, разумни timeouts, HTTP кеш за
     * изображения и статично съдържание. Никакво логване на тела/заглавки
     * (токените и личните данни не попадат в logcat).
     *
     * [userAgent] (име и версия на приложението) се слага на всяка заявка — и на
     * производните клиенти (изображения, изтегляне на обновления), — освен ако
     * заявката сама не е задала свой.
     */
    fun okHttp(context: Context, userAgent: String): OkHttpClient = OkHttpClient.Builder()
        .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.RESTRICTED_TLS))
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .callTimeout(45, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .cache(Cache(File(context.cacheDir, "http"), 30L * 1024 * 1024))
        .addNetworkInterceptor(userAgentInterceptor(userAgent))
        .build()

    /**
     * Мрежов interceptor: тук OkHttp вече е сложил своя „okhttp/x.y“, ако заявката
     * няма User-Agent — той се заменя с [userAgent]; изрично зададеният се запазва.
     */
    private fun userAgentInterceptor(userAgent: String) = Interceptor { chain ->
        val request = chain.request()
        val current = request.header(USER_AGENT)
        if (current == null || current.startsWith("okhttp/")) {
            chain.proceed(request.newBuilder().header(USER_AGENT, userAgent).build())
        } else {
            chain.proceed(request)
        }
    }

    private const val USER_AGENT = "User-Agent"
}
