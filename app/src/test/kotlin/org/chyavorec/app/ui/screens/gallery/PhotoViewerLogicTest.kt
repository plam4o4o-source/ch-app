package org.chyavorec.app.ui.screens.gallery

import org.chyavorec.app.ui.components.sitePhotoPlaceholderKey
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.data.site.SiteImageUrls
import org.chyavorec.domain.model.GalleryPhoto
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.robolectric.annotation.Config
import android.app.Application
import android.net.Uri
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Прегледът на цял екран: какво се зарежда, какво е заместител и какво се споделя. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class PhotoViewerLogicTest {
    private val photo = "https://chyavorec.org/pictures/sabor/1.jpg"

    @Test fun viewerLoadsTheOptimizedPhotoAtTwiceTheScreenThenTheOriginal() {
        val sources = ViewerSources.of(photo, "https://chyavorec.org/pictures/sabor/1-thumb.jpg", 2 * 1080)
        assertEquals(SiteImageUrls.optimized(photo, 2160), sources[0])
        assertEquals("https://chyavorec.org/_vercel/image?url=%2Fpictures%2Fsabor%2F1.jpg&w=1600&q=75", sources[0])
        assertEquals(photo, sources[1], "резервно — оригиналът")
        assertEquals("https://chyavorec.org/pictures/sabor/1-thumb.jpg", sources[2])
        // Снимка извън сайта — само самият адрес.
        assertEquals(listOf("https://example.org/a.jpg"), ViewerSources.of("https://example.org/a.jpg", "https://example.org/a.jpg", 2000))
    }

    @Test fun placeholderIsTheThumbnailAlreadyInMemory() {
        val thumbInGrid = SiteImageUrls.optimized(photo, 300)!!
        val cached = setOf(thumbInGrid)
        assertEquals(thumbInGrid, sitePhotoPlaceholderKey({ it in cached }, listOf(photo)))
        // Предпочита се най-голямата вече заредена ширина.
        val hero = SiteImageUrls.optimized(photo, 1000)!!
        assertEquals(hero, sitePhotoPlaceholderKey({ it in cached + hero }, listOf(photo)))
        // Външен адрес — по самия адрес; нищо в паметта — без заместител.
        assertEquals("https://example.org/a.jpg", sitePhotoPlaceholderKey({ it == "https://example.org/a.jpg" }, listOf("https://example.org/a.jpg")))
        assertNull(sitePhotoPlaceholderKey({ false }, listOf(photo)))
    }

    @Test fun articlePhotosShareTheArticleNotTheImage() {
        val article = "https://chyavorec.org/news/sabor"
        val photos = viewerPhotos(listOf(photo, "https://chyavorec.org/pictures/sabor/2.jpg"), listOf("Хорото на площада", ""), article)
        assertEquals(listOf("Хорото на площада", ""), photos.map { it.title })
        val share = PhotoShare.of(photos[1], "Събор в Яворец", "Галерия")
        assertEquals("Събор в Яворец", share.subject)
        assertEquals("Събор в Яворец\n$article", share.text)
        // Без статия (албум от галерията) — заглавието на снимката или албума и страницата му.
        val album = GalleryPhoto("1", "", "t", "https://chyavorec.org/pictures/a.jpg", "Празници", "https://chyavorec.org/fotodokumentalna-izlozhba")
        assertEquals(PhotoShare("Празници", "Празници\nhttps://chyavorec.org/fotodokumentalna-izlozhba"), PhotoShare.of(album, "", "Галерия"))
        // Нищо друго — само адресът на снимката, с общото заглавие като тема.
        val bare = GalleryPhoto("u0", "", photo, photo, "", "")
        assertEquals(PhotoShare("Галерия", photo), PhotoShare.of(bare, "", "Галерия"))
    }

    @Test fun viewerRouteCarriesAltTextPageAndTitle() {
        val route = Routes.viewerUrls(listOf(photo, "https://chyavorec.org/pictures/sabor/2.jpg"), 1, listOf("Хоро\nна площада", ""), "https://chyavorec.org/news/sabor", "Събор & хоро")
        val uri = Uri.parse("app://x/$route")
        assertEquals("Хоро на площада\n", uri.getQueryParameter("alt"))
        assertEquals("https://chyavorec.org/news/sabor", uri.getQueryParameter("page"))
        assertEquals("Събор & хоро", uri.getQueryParameter("title"))
        assertEquals("1", uri.getQueryParameter("index"))
        // Без описания — празен параметър (старите връзки към прегледа остават валидни).
        assertEquals("", Uri.parse("app://x/" + Routes.viewerUrls(listOf(photo), 0)).getQueryParameter("alt"))
    }
}
