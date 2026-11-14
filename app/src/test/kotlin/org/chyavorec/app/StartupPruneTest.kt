package org.chyavorec.app

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.OkHttpClient
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import java.time.Instant
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Стартът на интерфейса ([ChitalishteApp.onUiStart] → [pruneDownloadsOnStart]): прекъснатите
 * сваляния (`.part`) на документите и на „Стария вестник“ се трият веднага, а готовите статии
 * на вестника (пазят се година — офлайн четене) остават.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class StartupPruneTest {
    private val app get() = ApplicationProvider.getApplicationContext<Application>()

    private fun file(dir: String, path: String, ageDays: Int): File {
        val f = File(app.cacheDir, "$dir/$path")
        f.parentFile!!.mkdirs()
        f.writeBytes(ByteArray(64))
        f.setLastModified(System.currentTimeMillis() - ageDays * 24L * 60 * 60 * 1000)
        return f
    }

    @Test fun startupDropsPartialDownloadsButKeepsTheNewspaperForOfflineReading() {
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = true, versionName = "test", versionCode = 1,
        )
        val container = AppContainer(app, config, FixedClock(Instant.parse("2026-11-14T10:00:00Z")), XorTestCipher(), OkHttpClient())

        val articlePart = file("vestnik", "0123456789abcdef01234567/statia-3.pdf.part", ageDays = 0)
        val article = file("vestnik", "89abcdef0123456701234567/statia-1.pdf", ageDays = 40)
        val documentPart = file("documents", "fedcba9876543210fedcba98/ustav.pdf.part", ageDays = 0)
        val oldDocument = file("documents", "00112233445566778899aabb/otchet.pdf", ageDays = 10)
        val document = file("documents", "aabbccddeeff001122334455/pravilnik.pdf", ageDays = 1)

        pruneDownloadsOnStart(container)

        assertFalse(articlePart.exists(), "недосвалената статия на вестника — вън")
        assertFalse(articlePart.parentFile!!.exists(), "и празната ѝ папка")
        assertTrue(article.exists(), "готовата статия остава (вестникът се пази година)")
        assertFalse(documentPart.exists(), "недосваленият документ — вън")
        assertFalse(oldDocument.exists(), "документ по-стар от седмица — вън")
        assertTrue(document.exists())
    }
}
