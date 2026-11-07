package org.chyavorec.app.ui.share

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.chyavorec.app.ui.components.Barcodes
import org.chyavorec.app.ui.screens.my.YearSlideKind
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Картинките за споделяне: размерът, QR кодът към адреса, файлът в кеша и намерението „Сподели“. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg")
class ShareImagesTest {
    @get:Rule val compose = createComposeRule()
    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private val eventUrl = "https://chyavorec.org/events/praznik-na-selo-2026"

    private fun decodeQr(bitmap: Bitmap): String {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val source = RGBLuminanceSource(bitmap.width, bitmap.height, pixels)
        return QRCodeReader().decode(BinaryBitmap(HybridBinarizer(source)), mapOf(DecodeHintType.TRY_HARDER to true)).text
    }

    @Test fun qrEncodesTheUrl() {
        val url = "https://chyavorec.org/elektronen-katalog#zapis=1234"
        val matrix = assertNotNull(Barcodes.qr(url))
        val scale = 8
        val bmp = Bitmap.createBitmap(matrix.width * scale, matrix.height * scale, Bitmap.Config.ARGB_8888)
        for (y in 0 until bmp.height) for (x in 0 until bmp.width) {
            bmp.setPixel(x, y, if (matrix.get(x / scale, y / scale)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
        assertEquals(url, decodeQr(bmp))
    }

    /** Рисува [card] през [ShareImageHost] (извън екрана) и връща готовата картинка. */
    private fun render(card: ShareCard): Bitmap {
        var result: Bitmap? = null
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.LIGHT) {
                val state = rememberShareImageState()
                LaunchedEffect(Unit) { state.share(ShareImageRequest(card, "тема", "текст")) }
                ShareImageHost(state) { bitmap, _ -> result = bitmap }
            }
        }
        compose.waitUntil(15_000) { result != null }
        return result!!
    }

    private fun save(bitmap: Bitmap, name: String) =
        File(outDir, "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

    /** Долният десен ъгъл на картата — там е QR кодът (останалата картина не пречи на търсенето). */
    private fun qrCorner(bitmap: Bitmap): Bitmap =
        Bitmap.createBitmap(bitmap, bitmap.width / 2, bitmap.height * 3 / 4, bitmap.width / 2, bitmap.height / 4)

    @Test fun eventCardIsAStoryWithTheQrToTheEvent() {
        val card = ShareCard.Event("Празник на селото", "събота, 26 септември 2026 · 18:00", "26", "септември", "Читалищен салон", null, eventUrl)
        val bitmap = render(card)
        save(bitmap, "share-render-event")
        assertEquals(1080, bitmap.width)
        assertEquals(1920, bitmap.height)
        // Не е празна: горе е тъмният фон, а QR кодът долу се чете.
        assertFalse(bitmap.getPixel(540, 40) == 0, "картата е прозрачна")
        assertEquals(eventUrl, decodeQr(qrCorner(bitmap)))
    }

    @Test fun newsCardIsAFeedPost() {
        val url = "https://chyavorec.org/news/45-ti-sabor"
        val bitmap = render(ShareCard.News("45-ти събор на народното творчество", "26 септември 2026", "Кратко описание.", null, url))
        assertEquals(1080, bitmap.width)
        assertEquals(1350, bitmap.height)
        assertEquals(url, decodeQr(qrCorner(bitmap)))
    }

    @Test fun yearSlideIsAStory() {
        val bitmap = render(ShareCard.Year(ShareFixtures.year, YearSlideKind.BOOKS, "https://chyavorec.org"))
        save(bitmap, "share-render-year")
        assertEquals(1080 to 1920, bitmap.width to bitmap.height)
    }

    @Test fun shareIntentCarriesThePngTheLinkAndTheReadGrant() {
        val bmp = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        val file = ShareFiles.write(context, bmp, "event")
        assertTrue(file.exists())
        assertEquals(File(context.cacheDir, "share"), file.parentFile)
        val uri = ShareFiles.uri(context, file)
        assertEquals("content", uri.scheme)
        assertEquals(context.packageName + ".files", uri.authority)
        val text = "Празник на селото\n$eventUrl"
        val send = ShareFiles.sendIntent(uri, "Празник на селото", text)
        assertEquals(Intent.ACTION_SEND, send.action)
        assertEquals("image/png", send.type)
        @Suppress("DEPRECATION")
        assertEquals(uri, send.getParcelableExtra<Uri>(Intent.EXTRA_STREAM))
        assertEquals(text, send.getStringExtra(Intent.EXTRA_TEXT))
        assertTrue(send.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0)
        assertEquals(uri, send.clipData?.getItemAt(0)?.uri)
        val chooser = ShareFiles.chooser(context, uri, "Празник на селото", text)
        assertEquals(Intent.ACTION_CHOOSER, chooser.action)
        @Suppress("DEPRECATION")
        assertEquals("image/png", chooser.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)?.type)
    }

    @Test fun oldImagesAreCleanedUp() {
        val dir = ShareFiles.dir(context).apply { mkdirs() }
        val now = 10L * ShareFiles.MAX_AGE_MS
        val old = File(dir, "old.png").apply { writeText("x"); setLastModified(now - ShareFiles.MAX_AGE_MS - 1_000) }
        val fresh = (0 until ShareFiles.KEEP + 2).map { i -> File(dir, "f$i.png").apply { writeText("x"); setLastModified(now - 60_000L * (i + 1)) } }
        ShareFiles.cleanup(dir, now)
        assertFalse(old.exists(), "по-стара от ден")
        // Остават най-новите (място и за новата картинка).
        assertEquals(fresh.take(ShareFiles.KEEP - 1).map { it.name }.toSet(), dir.listFiles()!!.map { it.name }.toSet())
    }
}
