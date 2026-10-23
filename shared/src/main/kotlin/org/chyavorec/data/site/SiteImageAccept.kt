package org.chyavorec.data.site

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Заглавката `Accept` за снимките от chyavorec.org.
 *
 * Оптимизаторът на Vercel (`/_vercel/image`, vercel.json: `formats: avif, webp`) избира
 * формата по `Accept` на клиента (`Vary: Accept`) — без `image/webp`/`image/avif` връща
 * JPEG. Coil изпраща само заглавките на заявката, т.е. никакъв `Accept`.
 *
 * AVIF се иска само от [AVIF_MIN_SDK] нагоре: Android 12–13 декодират AVIF през пътя за
 * HEIF (хардуерен/доставчиков AV1 декодер, без алфа канал), Android 14 — през libavif в
 * Skia, еднакво на всички устройства. WebP се декодира навсякъде (minSdk 26).
 */
object SiteImageAccept {

    const val AVIF_MIN_SDK: Int = 34

    private val HOSTS = setOf("chyavorec.org", "www.chyavorec.org")

    fun header(sdkInt: Int): String =
        if (sdkInt >= AVIF_MIN_SDK) "image/avif,image/webp,image/*" else "image/webp,image/*"

    /** Само заявки към сайта; другите (Open Library, корици от чужди адреси) — без промяна. */
    fun appliesTo(host: String): Boolean = host.lowercase() in HOSTS

    /** Interceptor за OkHttp клиента на изображенията (Coil). */
    class Interceptor(sdkInt: Int) : okhttp3.Interceptor {
        private val accept = header(sdkInt)

        override fun intercept(chain: okhttp3.Interceptor.Chain): Response {
            val request = chain.request()
            if (!appliesTo(request.url.host)) return chain.proceed(request)
            return chain.proceed(request.newBuilder().header("Accept", accept).build())
        }
    }
}
