package org.chyavorec.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Наш ли е този адрес“ — точният хост на разчетения адрес, а не проверка на низа. */
class TrustedLinksTest {
    private val base = "https://chyavorec.org"

    private fun site(url: String, b: String = base) = TrustedLinks.isSiteUrl(url, b)

    @Test fun acceptsTheSiteWithAndWithoutWww() {
        assertTrue(site("https://chyavorec.org"))
        assertTrue(site("https://chyavorec.org/"))
        assertTrue(site("https://chyavorec.org/news/s-horo-i-pesen?utm_source=fb#top"))
        assertTrue(site("https://www.chyavorec.org/history"))
        assertTrue(site("https://chyavorec.org/elektronen-katalog#zapis=156"))
        assertTrue(site("https://chyavorec.org/elektronen-katalog#q=вазов"))
        assertTrue(site("https://chyavorec.org/%D0%BD%D0%BE%D0%B2%D0%B8%D0%BD%D0%B8"))
        // Изричният стандартен порт е същият адрес.
        assertTrue(site("https://chyavorec.org:443/about"))
        // Главните букви в схемата и хоста не променят адреса (браузърите ги свеждат до малки).
        assertTrue(site("HTTPS://ChYaVoReC.ORG/about"))
        assertTrue(site("  https://chyavorec.org/about  "))
    }

    @Test fun rejectsLookAlikeHosts() {
        listOf(
            "https://evilchyavorec.org/",
            "https://chyavorec.org.evil.com/",
            "https://evil.com/chyavorec.org",
            "https://evil.com/?u=https://chyavorec.org",
            "https://evil.com#chyavorec.org",
            "https://sub.chyavorec.org/",
            "https://www.www.chyavorec.org/",
            "https://chyavorec.org./",
            "https://chyavorec.orgg/",
            "https://chyavorec.com/",
            // Кирилско „у“ вместо латинско „y“ (хомограф).
            "https://chуavorec.org/",
            "https://xn--chavorec-0og.org/",
        ).forEach { assertFalse(site(it), it) }
    }

    @Test fun rejectsUserInfoTricks() {
        listOf(
            "https://chyavorec.org@evil.com/",
            "https://chyavorec.org:443@evil.com/about",
            "https://user:pass@chyavorec.org/",
            "https://chyavorec.org%40evil.com/",
            "https://evil.com%2F@chyavorec.org/",
            "https://evil.com\\@chyavorec.org/about",
            "https://chyavorec.org\\@evil.com/",
        ).forEach { assertFalse(site(it), it) }
    }

    @Test fun rejectsBackslashesEncodingAndWhitespace() {
        listOf(
            "https://evil.com\\.chyavorec.org/",
            "https:\\\\chyavorec.org/about",
            "https://chyavorec%2Eorg/",
            "https://%63hyavorec.org/",
            "https://chyavorec.org/a b",
            "https://chyavorec.org/\nhttps://evil.com",
            "https://chyavorec.org\t.evil.com/",
            "https://chyavorec.org/%zz",
            "https://[::1]/",
        ).forEach { assertFalse(site(it), it) }
    }

    @Test fun rejectsOtherSchemes() {
        listOf(
            "http://chyavorec.org/about",
            "javascript:alert(1)//https://chyavorec.org",
            "JavaScript://chyavorec.org/%0Aalert(1)",
            "intent://chyavorec.org/#Intent;scheme=https;package=com.evil;end",
            "content://chyavorec.org/about",
            "file://chyavorec.org/etc/passwd",
            "file:///data/data/org.chyavorec.app/",
            "ftp://chyavorec.org/",
            "data:text/html,<h1>chyavorec.org</h1>",
            "chyavorec://app/login",
            "//chyavorec.org/about",
            "/about",
            "chyavorec.org/about",
            "https:chyavorec.org/about",
            "https:/chyavorec.org/about",
            "",
            "   ",
        ).forEach { assertFalse(site(it), it) }
        assertFalse(TrustedLinks.isSiteUrl(null, base))
    }

    @Test fun rejectsOtherPorts() {
        assertFalse(site("https://chyavorec.org:8443/about"))
        assertFalse(site("https://chyavorec.org:80/about"))
    }

    @Test fun httpOnlyForALocalHttpBaseOnTheSameHostAndPort() {
        val local = "http://127.0.0.1:8080"
        assertTrue(site("http://127.0.0.1:8080/about", local))
        assertFalse(site("http://127.0.0.1:9090/about", local))
        assertFalse(site("http://evil.com/about", local))
        // Каноничният сайт си остава доверен и при друг базов адрес — но само по https.
        assertTrue(site("https://chyavorec.org/about", local))
        assertFalse(site("http://chyavorec.org/about", local))
    }

    @Test fun configuredBaseHostIsTrustedExactly() {
        val staging = "https://staging.chyavorec.org"
        assertTrue(site("https://staging.chyavorec.org/about", staging))
        assertTrue(site("https://www.staging.chyavorec.org/about", staging))
        assertFalse(site("https://evilstaging.chyavorec.org/about", staging))
        assertFalse(site("https://staging.chyavorec.org/about", base))
    }

    @Test fun parseNormalizesAndExposesThePath() {
        val l = TrustedLinks.parse("HTTPS://WWW.Chyavorec.org:443/News/%D0%B0?x=1#y")!!
        assertEquals("https", l.scheme)
        assertEquals("www.chyavorec.org", l.host)
        assertEquals(-1, l.port)
        assertEquals("/News/а", l.path)
        assertEquals("/News/%D0%B0", l.rawPath)
        assertEquals("x=1", l.rawQuery)
        assertEquals("y", l.rawFragment)
        assertNull(TrustedLinks.parse("mailto:chitalishte_yavorec@abv.bg"))
        assertNull(TrustedLinks.parse("tel:+359888"))
    }

    @Test fun canonicalUrlKeepsOnlyPathAndQueryOnTheConfiguredOrigin() {
        assertEquals("https://chyavorec.org/history", TrustedLinks.canonicalSiteUrl("https://www.chyavorec.org/history#top", base))
        assertEquals("https://chyavorec.org/about?x=1", TrustedLinks.canonicalSiteUrl("HTTPS://CHYAVOREC.ORG:443/about?x=1", base))
        assertEquals("https://chyavorec.org/", TrustedLinks.canonicalSiteUrl("https://chyavorec.org", "$base/"))
        assertNull(TrustedLinks.canonicalSiteUrl("https://chyavorec.org:443@evil.com/about", base))
        assertNull(TrustedLinks.canonicalSiteUrl("https://evil.com/about", base))
    }

    @Test fun urlsHelpersUseTheStrictCheck() {
        assertTrue(Urls.sameSite("https://www.chyavorec.org/x", base))
        assertFalse(Urls.sameSite("https://chyavorec.org:443@evil.com/x", base))
        assertFalse(Urls.sameSite("https://evilchyavorec.org/x", base))
        assertEquals("evil.com", TrustedLinks.hostOf("https://evil.com#@chyavorec.org"))
    }
}
