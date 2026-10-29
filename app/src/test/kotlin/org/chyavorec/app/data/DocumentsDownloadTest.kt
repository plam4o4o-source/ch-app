package org.chyavorec.app.data

import android.app.Application
import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.util.Intents
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * PDF-ите от „Документи и архив“: без общия timeout на клиента (50 MB по бавна мрежа), само
 * от сайта, а приложението за PDF вижда четимото име на файла (не хеша в кеша).
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class DocumentsDownloadTest {
    private val app = ApplicationProvider.getApplicationContext<Application>()

    private fun container(): AppContainer {
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val shared = OkHttpClient.Builder().callTimeout(45, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build()
        return AppContainer(app, config, FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), shared)
    }

    @Test fun downloadsHaveNoCallTimeoutAndComeOnlyFromTheSite(): Unit = runBlocking {
        val c = container()
        assertEquals(0, c.documentsHttp.callTimeoutMillis, "цялото тяло може да трае повече от 45 s")
        assertEquals(60_000, c.documentsHttp.readTimeoutMillis)
        assertEquals(45_000, c.okHttp.callTimeoutMillis, "общият клиент — непроменен")
        // Чужд адрес — без заявка.
        val r = assertIs<Outcome.Failure>(c.documents.download("https://example.org/ustav.pdf"))
        assertEquals(AppError.Unexpected("host"), r.error)
    }

    @Test fun pdfAppSeesTheReadableFileName() {
        val c = container()
        val file = c.documents.fileFor("https://chyavorec.org/files/3bf6.pdf", "Устав на читалището")
        file.parentFile!!.mkdirs()
        file.writeText("%PDF-1.4")
        val uri = FileProvider.getUriForFile(app, Intents.fileProviderAuthority(app), file)
        assertEquals("Устав на читалището.pdf", uri.lastPathSegment)
        assertEquals("documents", uri.pathSegments.first(), "под cache-path documents/ (res/xml/file_paths.xml)")
        c.documents.clear()
    }
}
