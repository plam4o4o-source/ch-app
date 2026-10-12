package org.chyavorec.data

import org.chyavorec.data.site.SiteImageUrls
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SiteImageUrlsTest {

    private fun opt(path: String, w: Int) = "https://chyavorec.org/_vercel/image?url=$path&w=$w&q=75"

    @Test
    fun `site asset becomes optimized url with encoded path`() {
        assertEquals(
            opt("%2Fassets%2Fuploads%2Fx.jpg", 640),
            SiteImageUrls.optimized("https://chyavorec.org/assets/uploads/x.jpg", 600),
        )
        assertEquals(
            opt("%2Fpictures%2Fgallery%2F2024%2Fsnimka.webp", 256),
            SiteImageUrls.optimized("https://chyavorec.org/pictures/gallery/2024/snimka.webp", 200),
        )
    }

    @Test
    fun `width is the smallest allowed size not below the needed one, capped at 1600`() {
        val expected = mapOf(
            1 to 128, 128 to 128, 129 to 256, 256 to 256, 300 to 384, 384 to 384, 385 to 640,
            700 to 828, 828 to 828, 900 to 1080, 1080 to 1080, 1081 to 1600, 1600 to 1600, 4000 to 1600,
        )
        expected.forEach { (need, w) -> assertEquals("need $need", w, SiteImageUrls.widthFor(need)) }
        assertEquals(opt("%2Fassets%2Fa.png", 1600), SiteImageUrls.optimized("https://chyavorec.org/assets/a.png", 3000))
        assertEquals(listOf(128, 256, 384, 640, 828, 1080, 1600), SiteImageUrls.WIDTHS)
    }

    @Test
    fun `cyrillic, spaces and special characters are encoded once`() {
        // Вече кодиран адрес (както идва от сайта) — пътят се декодира и кодира точно веднъж.
        assertEquals(
            opt("%2Fassets%2Fuploads%2F%D1%81%D0%BD%D0%B8%D0%BC%D0%BA%D0%B0%201.jpg", 384),
            SiteImageUrls.optimized("https://chyavorec.org/assets/uploads/%D1%81%D0%BD%D0%B8%D0%BC%D0%BA%D0%B0%201.jpg", 384),
        )
        // Некодирана кирилица в адреса — същият резултат.
        assertEquals(
            opt("%2Fassets%2Fuploads%2F%D1%81%D0%BD%D0%B8%D0%BC%D0%BA%D0%B0.jpg", 128),
            SiteImageUrls.optimized("https://chyavorec.org/assets/uploads/снимка.jpg", 100),
        )
        assertEquals(
            opt("%2Fassets%2Fa%2Bb%26c.jpg", 128),
            SiteImageUrls.optimized("https://chyavorec.org/assets/a+b%26c.jpg", 64),
        )
    }

    @Test
    fun `www host and http scheme are accepted, result always uses the main https host`() {
        assertEquals(opt("%2Fassets%2Fx.jpg", 828), SiteImageUrls.optimized("https://www.chyavorec.org/assets/x.jpg", 800))
        assertEquals(opt("%2Fassets%2Fx.jpg", 828), SiteImageUrls.optimized("http://CHYAVOREC.org/assets/x.jpg", 800))
    }

    @Test
    fun `everything else is returned unchanged`() {
        val unchanged = listOf(
            "https://covers.openlibrary.org/b/isbn/9789540000000-M.jpg",
            "https://example.com/assets/x.jpg",
            "https://chyavorec.org.evil.com/assets/x.jpg",
            "https://chyavorec.org/images/logo.png",
            "https://chyavorec.org/assets/",
            "https://chyavorec.org/assetsx/y.jpg",
            "https://chyavorec.org/news/assets/x.jpg",
            "https://chyavorec.org/assets/x.jpg?v=2",
            "https://chyavorec.org/assets/icons/logo.svg",
            "https://chyavorec.org/assets/../secret.jpg",
            "https://chyavorec.org:8443/assets/x.jpg",
            "https://user@chyavorec.org/assets/x.jpg",
            "https://chyavorec.org/_vercel/image?url=%2Fassets%2Fx.jpg&w=640&q=75",
            "ftp://chyavorec.org/assets/x.jpg",
            "/assets/x.jpg",
            "not a url with spaces",
            "",
        )
        unchanged.forEach { assertEquals(it, it, SiteImageUrls.optimized(it, 400)) }
        assertNull(SiteImageUrls.optimized(null, 400))
        // Неизвестна ширина — оригиналът.
        assertEquals("https://chyavorec.org/assets/x.jpg", SiteImageUrls.optimized("https://chyavorec.org/assets/x.jpg", 0))
    }

    @Test
    fun `fragment is dropped, the optimized address does not carry it`() {
        assertEquals(opt("%2Fassets%2Fx.jpg", 128), SiteImageUrls.optimized("https://chyavorec.org/assets/x.jpg#top", 128))
    }
}
