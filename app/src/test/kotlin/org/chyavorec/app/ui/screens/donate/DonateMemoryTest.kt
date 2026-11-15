package org.chyavorec.app.ui.screens.donate

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.IntentCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.MainActivity
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.navigation.LinkTarget
import org.chyavorec.app.ui.navigation.Routes
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.ByteArrayOutputStream
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * „Дари спомен“: писмото (получател, тема, текст, прикачени снимки с право за четене, до 10,
 * само със съгласие), приемането на споделени снимки от галерията и смаляването в кеша.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg")
class DonateMemoryTest {
    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val to = "chitalishte_yavorec@abv.bg"

    private fun uris(n: Int) = (1..n).map { Uri.parse("content://${context.packageName}.files/donate/spomen-$it.jpg") }

    private val filled = DonationForm(
        who = "Баба Мара и дядо Иван", time = "около 1960 г.", place = "пред читалището",
        contact = "Мария Петрова, 0888 123 456", consent = true,
    )

    @Test fun mailGoesToTheCentreWithTheFieldsAndThePhotos() {
        val photos = uris(3)
        val send = assertNotNull(DonationMail.sendIntent(context, to, filled, photos))
        assertEquals(Intent.ACTION_SEND_MULTIPLE, send.action)
        assertEquals("image/jpeg", send.type)
        assertEquals(listOf(to), send.getStringArrayExtra(Intent.EXTRA_EMAIL)?.toList())
        assertEquals("Дари спомен — Баба Мара и дядо Иван", send.getStringExtra(Intent.EXTRA_SUBJECT))
        val body = assertNotNull(send.getStringExtra(Intent.EXTRA_TEXT))
        assertContains(body, "Изпращам 3 стари снимки за фотоархива на читалището.")
        assertContains(body, "Кой е на снимката: Баба Мара и дядо Иван")
        assertContains(body, "Кога (приблизително): около 1960 г.")
        assertContains(body, "Къде: пред читалището")
        assertContains(body, "Изпраща: Мария Петрова, 0888 123 456")
        assertContains(body, "Съгласен/съгласна съм читалището да покаже снимките в архива и на сайта си.")
        // Снимките — като EXTRA_STREAM и в ClipData, с временно право за четене.
        assertEquals<List<Uri>?>(photos, IntentCompat.getParcelableArrayListExtra(send, Intent.EXTRA_STREAM, Uri::class.java))
        val clip = assertNotNull(send.clipData)
        assertEquals(photos, (0 until clip.itemCount).map { clip.getItemAt(it).uri })
        assertTrue(send.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0)
    }

    @Test fun emptyFieldsAreLeftOutAndTheSubjectCountsThePhotos() {
        val form = DonationForm(consent = true)
        val send = assertNotNull(DonationMail.sendIntent(context, to, form, uris(1)))
        assertEquals("Дари спомен — 1 снимка", send.getStringExtra(Intent.EXTRA_SUBJECT))
        val body = send.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
        assertFalse(body.contains("Кой е на снимката"), body)
        assertFalse(body.contains("Изпраща"), body)
        assertContains(body, "Изпращам 1 стара снимка")
        // Без „кой“ — мястото; дългото се съкращава.
        val long = DonationForm(place = "Нагоре по реката, при старата воденица на дядо Стоян, където са сенокосите", consent = true)
        val subject = DonationMail.subject(context, long, 2)
        assertTrue(subject.startsWith("Дари спомен — Нагоре по реката") && subject.endsWith("…"), subject)
    }

    @Test fun consentIsRequired() {
        val form = filled.copy(consent = false)
        assertFalse(DonationMail.canSend(3, form))
        assertNull(DonationMail.sendIntent(context, to, form, uris(3)))
    }

    @Test fun oneToTenPhotos() {
        assertNull(DonationMail.sendIntent(context, to, filled, emptyList()), "без снимки")
        assertNotNull(DonationMail.sendIntent(context, to, filled, uris(DONATE_MAX_PHOTOS)))
        assertNull(DonationMail.sendIntent(context, to, filled, uris(DONATE_MAX_PHOTOS + 1)), "над 10")
        val state = DonateFormState().apply { consent = true }
        assertFalse(state.canSend, "без снимки бутонът е неактивен")
        assertEquals(DONATE_MAX_PHOTOS, state.room)
    }

    @Test fun chooserKeepsTheGrantPrefersEmailAppsAndLeavesOutTheApp() {
        val send = assertNotNull(DonationMail.sendIntent(context, to, filled, uris(2)))
        val plain = DonationMail.chooser(context, Intent(send))
        assertEquals(Intent.ACTION_CHOOSER, plain.action)
        assertTrue(plain.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0)
        val target = assertNotNull(IntentCompat.getParcelableExtra(plain, Intent.EXTRA_INTENT, Intent::class.java))
        assertEquals(Intent.ACTION_SEND_MULTIPLE, target.action)
        assertNull(target.selector, "без приложение за имейл — всички, които приемат снимки")
        @Suppress("DEPRECATION")
        val excluded = plain.getParcelableArrayExtra(Intent.EXTRA_EXCLUDE_COMPONENTS)?.toList()
        assertEquals(listOf(ComponentName(context, MainActivity::class.java)), excluded)

        // С приложение за имейл — изборът е само между тях (selector mailto:).
        val mailto = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        val info = ResolveInfo().apply {
            activityInfo = ActivityInfo().apply { packageName = "org.example.mail"; name = "org.example.mail.Compose" }
        }
        shadowOf(context.packageManager).addResolveInfoForIntent(mailto, info)
        val chooser = DonationMail.chooser(context, Intent(send))
        val toMail = assertNotNull(IntentCompat.getParcelableExtra(chooser, Intent.EXTRA_INTENT, Intent::class.java))
        assertEquals("mailto", toMail.selector?.data?.scheme)
        assertEquals(Intent.ACTION_SENDTO, toMail.selector?.action)
    }

    /* ---- „Сподели → Читалище Яворец“ ---- */

    @Test fun sharedPhotosFromAnotherAppOpenTheScreen() {
        val photo = Uri.parse("content://media/external/images/media/42")
        val share = Intent(Intent.ACTION_SEND).setType("image/jpeg").putExtra(Intent.EXTRA_STREAM, photo)
        assertEquals(DeepLinks.DONATE, DonateIntake.receive(context, share))
        assertEquals(listOf(photo), DonateIntake.take())
        assertEquals(emptyList<Uri>(), DonateIntake.take(), "веднъж")
        assertEquals(LinkTarget.Screen(Routes.MORE, listOf(Routes.DONATE)), DeepLinks.target(DeepLinks.DONATE, "https://chyavorec.org"))
    }

    @Test fun onlyForeignContentUrisAndAtMostTen() {
        val own = context.packageName
        val foreign = (1..12).map { Uri.parse("content://com.google.android.apps.photos.contentprovider/0/1/media/$it") }
        val list = ArrayList(
            listOf(
                Uri.parse("file:///data/data/$own/shared_prefs/settings.xml"),
                Uri.parse("content://$own.files/documents/abc/ustav.pdf"),
                foreign.first(),
            ) + foreign,
        )
        val share = Intent(Intent.ACTION_SEND_MULTIPLE).setType("image/*").putParcelableArrayListExtra(Intent.EXTRA_STREAM, list)
        assertEquals(DeepLinks.DONATE, DonateIntake.receive(context, share))
        val taken = DonateIntake.take()
        assertEquals(foreign.take(DONATE_MAX_PHOTOS), taken, "без файлове, без собствените ни, без повторения, до 10")
        // Не снимки — нищо не чака; друго действие — не е за този екран.
        val text = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_STREAM, foreign.first())
        assertEquals(emptyList<Uri>(), DonateIntake.sharedImages(own, text))
        assertNull(DonateIntake.receive(context, Intent(Intent.ACTION_VIEW, Uri.parse("https://chyavorec.org/"))))
    }

    @Test fun theManifestAcceptsSharedImagesAndTheProviderServesTheCopies() {
        val ns = "http://schemas.android.com/apk/res/android"
        val doc = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
            .newDocumentBuilder().parse(File("src/main/AndroidManifest.xml"))
        val filters = doc.getElementsByTagName("intent-filter")
        val share = (0 until filters.length).map { filters.item(it) as org.w3c.dom.Element }.firstOrNull { f ->
            val actions = f.getElementsByTagName("action").let { a -> (0 until a.length).map { (a.item(it) as org.w3c.dom.Element).getAttributeNS(ns, "name") } }
            Intent.ACTION_SEND in actions && Intent.ACTION_SEND_MULTIPLE in actions
        }
        val filter = assertNotNull(share, "MainActivity приема „Сподели“")
        assertEquals(".MainActivity", (filter.parentNode as org.w3c.dom.Element).getAttributeNS(ns, "name"))
        assertEquals("image/*", (filter.getElementsByTagName("data").item(0) as org.w3c.dom.Element).getAttributeNS(ns, "mimeType"))
        assertContains(File("src/main/res/xml/file_paths.xml").readText(), """<cache-path name="donate" path="donate/" />""")
    }

    /* ---- Смаляването ---- */

    private fun jpeg(width: Int, height: Int): ByteArray {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(bmp).apply {
            drawColor(Color.rgb(196, 170, 130))
            drawCircle(width / 2f, height / 2f, minOf(width, height) / 3f, Paint().apply { color = Color.rgb(90, 70, 50) })
        }
        return ByteArrayOutputStream().also { bmp.compress(Bitmap.CompressFormat.JPEG, 90, it) }.toByteArray()
    }

    @Test fun bigPhotosAreScaledToTheLongSideAndSmallOnesStay() {
        val big = assertNotNull(DonationPhotos.prepare(context, jpeg(4000, 3000), 1_000L))
        assertEquals(DonationPhotos.dir(context), big.file.parentFile)
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(big.file.path, bounds)
        assertEquals(DonationPhotos.MAX_SIDE to 1920, bounds.outWidth to bounds.outHeight)
        assertEquals("image/jpeg", bounds.outMimeType)
        assertTrue(maxOf(big.thumb.width, big.thumb.height) <= DonationPhotos.THUMB_SIDE)

        val small = assertNotNull(DonationPhotos.prepare(context, jpeg(800, 600), 2_000L))
        BitmapFactory.decodeFile(small.file.path, bounds)
        assertEquals(800 to 600, bounds.outWidth to bounds.outHeight)

        assertNull(DonationPhotos.prepare(context, "не е снимка".toByteArray(), 3_000L))
        assertEquals(2560 to 1440, DonationPhotos.targetSize(5120, 2880))
        assertEquals(1080 to 2560, DonationPhotos.targetSize(1350, 3200))
    }

    @Test fun copiesOlderThanADayAreCleared() {
        val dir = DonationPhotos.dir(context).apply { mkdirs() }
        val now = 10L * DonationPhotos.MAX_AGE_MS
        val old = File(dir, "old.jpg").apply { writeText("x"); setLastModified(now - DonationPhotos.MAX_AGE_MS - 1_000) }
        val fresh = File(dir, "fresh.jpg").apply { writeText("x"); setLastModified(now - 60_000L) }
        DonationPhotos.cleanup(context, now)
        assertFalse(old.exists())
        assertTrue(fresh.exists(), "писмото може още да е чернова")
        DonationPhotos.clear(context)
        assertFalse(dir.exists())
    }

    @Test fun theExhibitionLeadsHere() {
        assertTrue(DonateEntry.isExhibitionPage("https://chyavorec.org/fotodokumentalna-izlozhba"))
        assertTrue(DonateEntry.isExhibitionPage("https://chyavorec.org/fotodokumentalna-izlozhba/"))
        assertFalse(DonateEntry.isExhibitionPage("https://chyavorec.org/history"))
    }
}
