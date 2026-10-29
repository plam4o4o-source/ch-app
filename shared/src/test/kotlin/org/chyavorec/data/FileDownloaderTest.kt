package org.chyavorec.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Buffer
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.FileDownloader
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Свалянето на PDF за приложение на телефона: напредък, повторно ползване, без половин файл,
 * само PDF до границата, само от позволен адрес (и след пренасочване), четимо име, малка папка.
 */
class FileDownloaderTest {
    private lateinit var server: MockWebServer
    private val dir = Files.createTempDirectory("docs").toFile()
    private var now = 1_000_000L
    private val client = OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build()
    private val downloader = FileDownloader(client, dir, now = { now }, io = Dispatchers.IO, maxBytes = 1_000_000)

    @BeforeTest fun start() { server = MockWebServer().apply { start() } }
    @AfterTest fun stop() { server.shutdown(); dir.deleteRecursively() }

    private val pdf = "%PDF-1.7\n".toByteArray() + ByteArray(300_000) { (it % 251).toByte() }

    @Test fun downloadsWithProgressAndReusesTheFreshFile() = runBlocking {
        server.enqueue(MockResponse().setBody(Buffer().write(pdf)).setHeader("Content-Type", "application/pdf"))
        val url = server.url("/files/ustav.pdf").toString()
        val seen = mutableListOf<Float?>()
        val file = assertIs<Outcome.Success<java.io.File>>(downloader.download(url) { seen += it }).value
        assertContentEquals(pdf, file.readBytes())
        assertEquals(0f, seen.first())
        assertEquals(1f, seen.last())
        assertTrue(seen.filterNotNull().zipWithNext().all { (a, b) -> b >= a }, "напредъкът само расте")
        assertTrue(dir.listFiles()!!.none { it.name.endsWith(".part") })
        // Свален преди малко — без нова заявка.
        file.setLastModified(now)
        assertEquals(file, (downloader.download(url) as Outcome.Success).value)
        assertEquals(1, server.requestCount)
        assertEquals("no-store", server.takeRequest().getHeader("Cache-Control"))
    }

    @Test fun staleFileIsDownloadedAgain() = runBlocking {
        val url = server.url("/a.pdf").toString()
        server.enqueue(MockResponse().setBody("%PDF-1.4 first"))
        server.enqueue(MockResponse().setBody("%PDF-1.4 second"))
        val file = (downloader.download(url) as Outcome.Success).value
        file.setLastModified(now)
        now += 2 * 24 * 60 * 60 * 1000L
        assertEquals("%PDF-1.4 second", (downloader.download(url) as Outcome.Success).value.readText())
    }

    @Test fun failuresLeaveNoFile() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404))
        val url = server.url("/missing.pdf").toString()
        assertEquals(AppError.NotFound, assertIs<Outcome.Failure>(downloader.download(url)).error)
        server.enqueue(MockResponse().setBody(Buffer().write(pdf)).setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY))
        assertEquals(AppError.Network, assertIs<Outcome.Failure>(downloader.download(url)).error)
        assertTrue(dir.listFiles().orEmpty().isEmpty(), "нито файл, нито .part")
    }

    @Test fun cancelledDownloadLeavesNoPartialFile() = runBlocking {
        server.enqueue(MockResponse().setBody(Buffer().write(pdf)).throttleBody(16 * 1024, 50, TimeUnit.MILLISECONDS))
        val url = server.url("/slow.pdf").toString()
        val job = async { downloader.download(url) { p -> if (p != null && p > 0.1f) throw CancellationException("назад") } }
        assertFailsWith<CancellationException> { job.await() }
        assertTrue(dir.listFiles().orEmpty().isEmpty())
    }

    /** Страница за вход в Wi-Fi мрежа (или друго не-PDF) не се пази като документ за 24 ч. */
    @Test fun nonPdfResponsesAreRejected() = runBlocking {
        val url = server.url("/ustav.pdf").toString()
        server.enqueue(MockResponse().setBody("<html>Вход в мрежата</html>").setHeader("Content-Type", "text/html"))
        assertEquals(AppError.Parse("not a pdf"), assertIs<Outcome.Failure>(downloader.download(url)).error)
        // Без подсказващ Content-Type — по съдържанието.
        server.enqueue(MockResponse().setBody("<html>Вход в мрежата</html>").setHeader("Content-Type", "application/octet-stream"))
        assertEquals(AppError.Parse("not a pdf"), assertIs<Outcome.Failure>(downloader.download(url)).error)
        assertTrue(dir.listFiles().orEmpty().isEmpty(), "нито файл, нито .part")
        // Вече запазен не-PDF (от предишна версия) не се връща — тегли се наново.
        downloader.fileFor(url).apply { parentFile.mkdirs(); writeText("<html>стар</html>"); setLastModified(now) }
        server.enqueue(MockResponse().setBody(Buffer().write(pdf)))
        assertContentEquals(pdf, assertIs<Outcome.Success<java.io.File>>(downloader.download(url)).value.readBytes())
        assertEquals(3, server.requestCount)
    }

    @Test fun oversizedFilesAreAbortedByHeaderAndByBytesRead() = runBlocking {
        val big = "%PDF-1.7\n".toByteArray() + ByteArray(1_200_000)
        val url = server.url("/big.pdf").toString()
        server.enqueue(MockResponse().setBody(Buffer().write(big)))
        assertEquals(AppError.Parse("too large"), assertIs<Outcome.Failure>(downloader.download(url)).error)
        // Без Content-Length (chunked) — по прочетеното.
        server.enqueue(MockResponse().setChunkedBody(Buffer().write(big), 64 * 1024))
        val seen = mutableListOf<Float?>()
        assertEquals(AppError.Parse("too large"), assertIs<Outcome.Failure>(downloader.download(url) { seen += it }).error)
        assertEquals(listOf<Float?>(null), seen)
        assertTrue(dir.listFiles().orEmpty().isEmpty(), "нито файл, нито .part")
    }

    @Test fun pruneDropsOldFilesAndKeepsTheFolderSmall() = runBlocking {
        // Истинското време: току-сваленият файл получава часа на системата.
        now = System.currentTimeMillis()
        dir.mkdirs()
        val day = 24 * 60 * 60 * 1000L
        fun file(name: String, size: Int, ageDays: Int) = java.io.File(dir, name).apply {
            writeBytes(ByteArray(size)); setLastModified(now - ageDays * day)
        }
        val old = file("old.pdf", 10, 8)
        val oldest = file("a.pdf", 400_000, 3)
        val older = file("b.pdf", 400_000, 2)
        val recent = file("c.pdf", 400_000, 1)
        val part = file("d.pdf.part", 400_000, 0)
        downloader.prune()
        // Над седмица — изтрит; над 1 MB общо — най-старият; текущото сваляне (.part) — не.
        assertEquals(listOf(older, recent, part).map { it.name }.sorted(), dir.listFiles()!!.map { it.name }.sorted())
        assertTrue(!old.exists() && !oldest.exists())

        // След сваляне: новият файл остава, по-старите отстъпват.
        server.enqueue(MockResponse().setBody(Buffer().write(pdf)))
        val fresh = assertIs<Outcome.Success<java.io.File>>(downloader.download(server.url("/new.pdf").toString())).value
        assertTrue(fresh.exists() && !older.exists() && recent.exists())

        downloader.clear()
        assertTrue(!dir.exists())
    }

    /** Документът е в папка по адреса, а името на файла е четимо (него вижда четецът на PDF). */
    @Test fun fileNameIsReadableAndSafe() = runBlocking {
        val url = server.url("/files/Otchet%202024.PDF").toString()
        assertEquals("Otchet 2024.pdf", downloader.fileFor(url).name, "от адреса")
        assertEquals("Устав 2024 проект.pdf", downloader.fileFor(url, "Устав / 2024: проект?").name)
        assertEquals("hidden.pdf", downloader.fileFor(url, "..hidden").name, "без водеща точка")
        assertEquals("document.pdf", downloader.fileFor(server.url("/").toString(), " ").name)
        assertTrue(downloader.fileFor(url, "а".repeat(300)).name.length <= 84)
        // Една и съща папка за адреса — каквото и да е името.
        assertEquals(downloader.fileFor(url).parentFile, downloader.fileFor(url, "Отчет").parentFile)
        assertEquals(dir, downloader.fileFor(url).parentFile.parentFile)

        server.enqueue(MockResponse().setBody("%PDF-1.4 първи"))
        val first = assertIs<Outcome.Success<java.io.File>>(downloader.download(url, "Отчет за 2024 г.")).value
        assertEquals("Отчет за 2024 г.pdf", first.name, "без точка в края на името")
        // Сменено заглавие — нов файл в същата папка, старият — вън.
        server.enqueue(MockResponse().setBody("%PDF-1.4 втори"))
        val second = assertIs<Outcome.Success<java.io.File>>(downloader.download(url, "Годишен отчет")).value
        assertEquals("Годишен отчет.pdf", second.name)
        assertEquals(listOf(second), second.parentFile.listFiles()!!.toList())
    }

    @Test fun redirectToAForeignHostIsRejected() = runBlocking {
        val foreign = MockWebServer().apply { start() }
        try {
            val strict = FileDownloader(client, dir, now = { now }, io = Dispatchers.IO, allowedUrl = { it.port == server.port })
            foreign.enqueue(MockResponse().setBody(Buffer().write(pdf)))
            server.enqueue(MockResponse().setResponseCode(302).setHeader("Location", foreign.url("/evil.pdf")))
            val url = server.url("/ustav.pdf").toString()
            assertEquals(AppError.Unexpected("redirect"), assertIs<Outcome.Failure>(strict.download(url)).error)
            assertTrue(dir.listFiles().orEmpty().isEmpty(), "нито файл, нито .part")
            // Чужд адрес още преди заявката — без мрежа.
            assertEquals(AppError.Unexpected("host"), assertIs<Outcome.Failure>(strict.download(foreign.url("/a.pdf").toString())).error)
            assertEquals(1, foreign.requestCount)
            // Пренасочване в рамките на позволения хост — свалено.
            server.enqueue(MockResponse().setResponseCode(302).setHeader("Location", "/files/ustav.pdf"))
            server.enqueue(MockResponse().setBody(Buffer().write(pdf)))
            assertContentEquals(pdf, assertIs<Outcome.Success<java.io.File>>(strict.download(url)).value.readBytes())
        } finally {
            foreign.shutdown()
        }
    }

    /** Прекъснато сваляне (спрян процес) оставя `.part` — при стартиране се трие веднага. */
    @Test fun startupPruneDropsEveryPartialFile() = runBlocking {
        now = System.currentTimeMillis()
        val part = java.io.File(dir, "0123456789abcdef01234567/Устав.pdf.part").apply { parentFile.mkdirs(); writeBytes(ByteArray(10)); setLastModified(now) }
        val done = java.io.File(dir, "fedcba9876543210fedcba98/Отчет.pdf").apply { parentFile.mkdirs(); writeBytes(ByteArray(10)); setLastModified(now) }
        downloader.prune()
        assertTrue(part.exists(), "обикновено — само по възраст (може да тече сваляне)")
        downloader.prune(dropPartial = true)
        assertTrue(!part.exists() && !part.parentFile.exists(), "файлът и празната му папка")
        assertTrue(done.exists())
        // Остарелият документ — и папката му.
        done.setLastModified(now - 8 * 24 * 60 * 60 * 1000L)
        downloader.prune()
        assertTrue(!done.parentFile.exists())
    }
}
