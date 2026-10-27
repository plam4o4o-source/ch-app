package org.chyavorec.app.ui.screens.site

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.FileDownloader
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSearchDoc
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.SiteContentService
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.nio.file.Files
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** „Отвори с приложение за PDF“: сваляне в кеша, после файлът — или браузърът при неуспех. */
class DocumentsPdfTest {
    @get:Rule val main = MainDispatcherRule()
    private val server = MockWebServer().apply { start() }
    private val dir = Files.createTempDirectory("documents").toFile()

    @After fun stop() { server.shutdown(); dir.deleteRecursively() }

    private object NoSite : SiteContentService {
        private fun <T> none(): Outcome<T> = Outcome.Failure(AppError.Network)
        override suspend fun discoverLinks(): Outcome<List<SiteLink>> = none()
        override suspend fun fetchPage(url: String): Outcome<SitePage> = none()
        override suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts> = none()
        override suspend fun fetchGallery(news: List<NewsArticle>?): Outcome<List<GalleryPhoto>> = none()
        override suspend fun fetchDocuments(): Outcome<List<SiteDocument>> = none()
        override suspend fun fetchSearchIndex(): Outcome<List<SiteSearchDoc>> = none()
        override suspend fun fetchFeast(date: String): Outcome<DailyFeast> = none()
        override suspend fun fetchMessages(): Outcome<List<AppMessage>> = none()
    }

    private fun vm(downloader: FileDownloader?) =
        DocumentsViewModel(SiteRepository(NoSite, InMemoryPayloadCache(), FixedClock(Instant.parse("2026-09-26T10:00:00Z"))), downloader)

    @Test fun onlyHttpsPdfLinksFromTheSiteCanBeDownloaded() {
        val site = "https://chyavorec.org"
        assertTrue(DocumentsViewModel.isPdf("https://chyavorec.org/files/ustav.pdf", site))
        assertTrue(DocumentsViewModel.isPdf("https://www.chyavorec.org/files/OTCHET.PDF?v=2#page=3", site))
        assertFalse(DocumentsViewModel.isPdf("http://chyavorec.org/files/ustav.pdf", site))
        assertFalse(DocumentsViewModel.isPdf("https://chyavorec.org/files/ustav.docx", site))
        assertFalse(DocumentsViewModel.isPdf("https://chyavorec.org/load", site))
        // Чужд или подвеждащ хост — само в браузъра.
        assertFalse(DocumentsViewModel.isPdf("https://example.org/ustav.pdf", site))
        assertFalse(DocumentsViewModel.isPdf("https://evilchyavorec.org/ustav.pdf", site))
        assertFalse(DocumentsViewModel.isPdf("https://chyavorec.org@evil.com/ustav.pdf", site))
        assertFalse(vm(null).also { it.viewModelScope.cancel() }.canDownload("https://example.org/ustav.pdf"))
    }

    @Test fun downloadedPdfIsHandedOverAndFailureFallsBackToTheBrowser(): Unit = runBlocking {
        // MockWebServer е по HTTP — свалянето се проверява направо през FileDownloader, а изгледът — с HTTPS адрес.
        server.enqueue(MockResponse().setBody("%PDF-1.4 тест"))
        val downloader = FileDownloader(OkHttpClient(), dir)
        val file = assertIs<Outcome.Success<java.io.File>>(downloader.download(server.url("/ustav.pdf").toString())).value
        assertEquals("%PDF-1.4 тест", file.readText())

        // Адрес от сайта, но недостъпен (без DNS) — неуспех: отваря се в браузъра (Custom Tab), както досега.
        val offline = OkHttpClient.Builder().dns { throw java.net.UnknownHostException(it) }.build()
        val model = vm(FileDownloader(offline, dir))
        try {
            val url = "https://chyavorec.org/files/ustav.pdf"
            val result = async(start = CoroutineStart.UNDISPATCHED) { withTimeout(10_000) { model.results.first() } }
            model.openInPdfApp(url)
            val r = result.await()
            assertIs<DocumentsViewModel.PdfResult.Failed>(r)
            assertEquals(url, r.url)
            withTimeout(5_000) { model.download.first { it == null } }
            assertNull(model.download.value)
        } finally {
            model.viewModelScope.cancel()
        }
    }

    @Test fun withoutDownloaderTheDocumentOpensInTheBrowser(): Unit = runBlocking {
        val model = vm(null)
        try {
            val result = async(start = CoroutineStart.UNDISPATCHED) { withTimeout(5_000) { model.results.first() } }
            model.openInPdfApp("https://chyavorec.org/files/ustav.pdf")
            assertIs<DocumentsViewModel.PdfResult.Failed>(result.await())
        } finally {
            model.viewModelScope.cancel()
        }
    }
}
