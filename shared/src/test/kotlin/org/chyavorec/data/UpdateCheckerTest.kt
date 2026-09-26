package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.update.Checksums
import org.chyavorec.data.update.UpdateChecker
import org.chyavorec.data.update.UpdateManifestParser
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UpdateCheckerTest {
    private val manifestUrl = "https://github.com/plam4o4o-source/ch-app/releases/latest/download/update.json"
    private val sha = "a".repeat(64)

    private fun manifest(code: Int = 5, apk: String = "https://github.com/plam4o4o-source/ch-app/releases/download/v1.1.0/app.apk", minSdk: Int = 26, hash: String = sha) = """
        {"versionCode": $code, "versionName": "1.1.0", "apkUrl": "$apk", "sha256": "$hash",
         "size": 12345, "minSdk": $minSdk, "notes": "Нова версия", "releaseUrl": "https://github.com/plam4o4o-source/ch-app/releases/tag/v1.1.0", "extra": 1}
    """.trimIndent()

    @Test fun parsesValidManifest() {
        val info = (UpdateManifestParser.parse(manifest(), manifestUrl) as Outcome.Success).value
        assertEquals(5, info.versionCode)
        assertEquals("1.1.0", info.versionName)
        assertEquals(12345L, info.sizeBytes)
        assertEquals(26, info.minSdk)
        assertEquals("Нова версия", info.notes)
    }

    @Test fun rejectsApkFromAnotherHost() {
        val r = UpdateManifestParser.parse(manifest(apk = "https://evil.example/app.apk"), manifestUrl)
        assertIs<Outcome.Failure>(r)
    }

    @Test fun rejectsPlainHttpApk() {
        assertFalse(UpdateManifestParser.trustedApkUrl("http://github.com/x.apk", manifestUrl))
        assertTrue(UpdateManifestParser.trustedApkUrl("https://github.com/x.apk", manifestUrl))
    }

    @Test fun rejectsBadChecksumAndMissingFields() {
        assertIs<Outcome.Failure>(UpdateManifestParser.parse(manifest(hash = "xyz"), manifestUrl))
        assertIs<Outcome.Failure>(UpdateManifestParser.parse("""{"versionName":"1"}""", manifestUrl))
        assertIs<Outcome.Failure>(UpdateManifestParser.parse("<html>", manifestUrl))
        assertIs<Outcome.Failure>(UpdateManifestParser.parse(manifest(code = 0), manifestUrl))
    }

    @Test fun uppercaseChecksumIsNormalised() {
        val info = (UpdateManifestParser.parse(manifest(hash = "AB".repeat(32)), manifestUrl) as Outcome.Success).value
        assertEquals("ab".repeat(32), info.sha256)
    }

    @Test fun newerOnlyWhenCodeHigherAndSdkSupported() {
        val info = (UpdateManifestParser.parse(manifest(code = 5, minSdk = 30), manifestUrl) as Outcome.Success).value
        assertTrue(UpdateChecker.isNewer(info, installedVersionCode = 4, sdkInt = 34))
        assertFalse(UpdateChecker.isNewer(info, installedVersionCode = 5, sdkInt = 34))
        assertFalse(UpdateChecker.isNewer(info, installedVersionCode = 6, sdkInt = 34))
        assertFalse(UpdateChecker.isNewer(info, installedVersionCode = 4, sdkInt = 29))
    }

    @Test fun checksum() {
        assertEquals(
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            Checksums.sha256("".byteInputStream()),
        )
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            Checksums.sha256("abc".byteInputStream()),
        )
    }

    private lateinit var server: MockWebServer
    private val http = HttpFetcher(OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(), "test")

    @BeforeTest fun start() { server = MockWebServer().apply { start() } }
    @AfterTest fun stop() = server.shutdown()

    @Test fun checkerReturnsUpdateFromServer() = runTest {
        val url = server.url("/releases/latest/download/update.json").toString()
        val apk = server.url("/releases/download/v1.1.0/app.apk").toString()
        server.enqueue(MockResponse().setBody(manifest(code = 7, apk = apk)))
        val info = (UpdateChecker(http, url).check(installedVersionCode = 3, sdkInt = 34) as Outcome.Success).value
        assertEquals(7, info?.versionCode)
        assertEquals("no-cache", server.takeRequest().getHeader("Cache-Control"))
    }

    @Test fun checkerUpToDate() = runTest {
        val url = server.url("/update.json").toString()
        server.enqueue(MockResponse().setBody(manifest(code = 3, apk = server.url("/a.apk").toString())))
        assertNull((UpdateChecker(http, url).check(3, 34) as Outcome.Success).value)
    }

    @Test fun checkerNoReleaseYet() = runTest {
        server.enqueue(MockResponse().setResponseCode(404))
        val r = UpdateChecker(http, server.url("/update.json").toString()).check(3, 34)
        assertEquals(AppError.NotFound, (r as Outcome.Failure).error)
    }

    @Test fun checkerDisabledWithoutUrl() = runTest {
        assertNull((UpdateChecker(http, "").check(3, 34) as Outcome.Success).value)
    }
}
