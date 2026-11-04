package org.chyavorec.app.ui.components

import android.app.Application
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.request.allowHardware
import coil3.request.bitmapConfig
import org.chyavorec.app.ui.screens.gallery.viewerRequest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

/**
 * Миниатюрите в списъците (корици, снимки на новини, плочки от албуми) са в RGB_565 —
 * наполовина памет; прегледът на цял екран и голямата корица остават в пълен цвят.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class ThumbnailRequestTest {
    private val context = ApplicationProvider.getApplicationContext<Application>()
    private val photo = "https://chyavorec.org/_vercel/image?url=%2Fpictures%2Fsabor%2F1.jpg&w=384&q=75"
    private val cover = "https://covers.openlibrary.org/b/isbn/9789540000000-M.jpg?default=false"

    @Test fun listImagesAreRgb565() {
        val thumb = remoteImageRequest(context, photo, network = true, urlMemoryKey = true, thumbnail = true)
        assertEquals(Bitmap.Config.RGB_565, thumb.bitmapConfig)
        // Ключът в паметта остава самият адрес — заместителят в прегледа го намира.
        assertEquals(photo, thumb.memoryCacheKey)
        assertEquals(Bitmap.Config.RGB_565, coverRequest(context, cover, network = true, large = false, wantsBitmap = false).bitmapConfig)
    }

    @Test fun viewerLargeCoverAndOtherImagesKeepFullColour() {
        val viewer = viewerRequest(context, photo, photo, original = false, placeholder = null, screenW = 1080, screenH = 2400, retry = false)
        assertNotEquals(Bitmap.Config.RGB_565, viewer.bitmapConfig)
        val original = viewerRequest(context, "https://chyavorec.org/pictures/sabor/1.jpg", photo, original = true, placeholder = null, screenW = 1080, screenH = 2400, retry = false)
        assertNotEquals(Bitmap.Config.RGB_565, original.bitmapConfig)
        // Голямата корица в детайлите и снимка, която не е миниатюра (напр. профилната).
        assertNotEquals(Bitmap.Config.RGB_565, coverRequest(context, cover, network = true, large = true, wantsBitmap = false).bitmapConfig)
        assertNotEquals(Bitmap.Config.RGB_565, remoteImageRequest(context, photo, network = true, urlMemoryKey = false, thumbnail = false).bitmapConfig)
    }

    @Test fun paletteCoverIsASoftwareFullColourBitmap() {
        val palette = coverRequest(context, cover, network = true, large = true, wantsBitmap = true)
        assertFalse(palette.allowHardware)
        assertNotEquals(Bitmap.Config.RGB_565, palette.bitmapConfig)
    }
}
