package org.chyavorec.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.chyavorec.data.site.SiteImageAccept
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SiteImageAcceptTest {

    /** Изпраща заявка през интерсептора и връща заглавката `Accept`, стигнала до „мрежата“. */
    private fun sentAccept(url: String, sdkInt: Int, accept: String? = null): String? {
        var seen: Request? = null
        // Последният интерсептор връща отговор без мрежа — само записва заявката.
        val client = OkHttpClient.Builder()
            .addInterceptor(SiteImageAccept.Interceptor(sdkInt))
            .addInterceptor(Interceptor { chain ->
                seen = chain.request()
                Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(ByteArray(0).toResponseBody()).build()
            })
            .build()
        val request = Request.Builder().url(url).apply { accept?.let { header("Accept", it) } }.build()
        client.newCall(request).execute().close()
        return seen!!.header("Accept")
    }

    private val optimized = "https://chyavorec.org/_vercel/image?url=%2Fassets%2Fx.jpg&w=640&q=75"

    @Test
    fun `optimizer requests advertise avif from API 34 and webp below`() {
        assertEquals("image/avif,image/webp,image/*", sentAccept(optimized, 34))
        assertEquals("image/avif,image/webp,image/*", sentAccept(optimized, 36))
        assertEquals("image/webp,image/*", sentAccept(optimized, 33))
        assertEquals("image/webp,image/*", sentAccept(optimized, 31))
        assertEquals("image/webp,image/*", sentAccept(optimized, 26))
    }

    @Test
    fun `original site images and www host get the header too`() {
        assertEquals("image/webp,image/*", sentAccept("https://www.chyavorec.org/pictures/a.jpg", 30))
        assertEquals("image/webp,image/*", sentAccept("https://CHYAVOREC.org/assets/a.png", 30))
    }

    @Test
    fun `other hosts are left untouched`() {
        assertNull(sentAccept("https://covers.openlibrary.org/b/isbn/9789540000000-M.jpg", 36))
        assertEquals("*/*", sentAccept("https://covers.openlibrary.org/b/id/1-M.jpg", 36, accept = "*/*"))
        assertNull(sentAccept("https://chyavorec.org.evil.example/a.jpg", 36))
    }
}
