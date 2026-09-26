package org.chyavorec.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UrlsTest {
    private val base = "https://chyavorec.org"
    @Test fun relative() = assertEquals("https://chyavorec.org/news/", Urls.absolutize("/news/", base))
    @Test fun protocolRelative() = assertEquals("https://x.org/a.jpg", Urls.absolutize("//x.org/a.jpg", base))
    @Test fun upgradesHttp() = assertEquals("https://chyavorec.org/a", Urls.absolutize("http://chyavorec.org/a", base))
    @Test fun rejectsJavascript() = assertNull(Urls.absolutize("javascript:alert(1)", base))
    @Test fun sameSiteIgnoresWww() = assertTrue(Urls.sameSite("https://www.chyavorec.org/x", base))
}
