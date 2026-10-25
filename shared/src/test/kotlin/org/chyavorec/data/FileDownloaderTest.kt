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

/** Свалянето на PDF за приложение на телефона: напредък, повторно ползване, без половин файл. */
class FileDownloaderTest {
    private lateinit var server: MockWebServer
    private val dir = Files.createTempDirectory("docs").toFile()
    private var now = 1_000_000L
    private val client = OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build()
    private val downloader = FileDownloader(client, dir, now = { now }, io = Dispatchers.IO)

    @BeforeTest fun start() { server = MockWebServer().apply { start() } }
    @AfterTest fun stop() { server.shutdown(); dir.deleteRecursively() }

    private val pdf = ByteArray(300_000) { (it % 251).toByte() }

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
        server.enqueue(MockResponse().setBody("first"))
        server.enqueue(MockResponse().setBody("second"))
        val file = (downloader.download(url) as Outcome.Success).value
        file.setLastModified(now)
        now += 2 * 24 * 60 * 60 * 1000L
        assertEquals("second", (downloader.download(url) as Outcome.Success).value.readText())
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
}
