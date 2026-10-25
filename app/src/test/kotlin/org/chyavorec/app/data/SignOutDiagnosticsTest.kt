package org.chyavorec.app.data

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.DiagnosticLog
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.FixedClock
import org.chyavorec.core.InMemoryDiagnosticStore
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import kotlin.test.assertTrue

/**
 * Изходът (и принудителният — отказано подновяване на сесията) изчиства личните следи, но
 * не и локалния дневник на грешките: в него е и записът, който обяснява защо е станал изходът.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SignOutDiagnosticsTest {
    @After fun uninstall() = Diagnostics.install(null)

    @Test fun signOutKeepsTheSanitisedDiagnosticsLog(): Unit = runBlocking {
        val log = DiagnosticLog(InMemoryDiagnosticStore())
        Diagnostics.install(log)
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val c = AppContainer(
            ApplicationProvider.getApplicationContext(), config,
            FixedClock(Instant.parse("2026-09-26T10:00:00Z")), XorTestCipher(), OkHttpClient(),
        )
        // Предишният процес е имал вход — следите му се изчистват при изхода.
        c.settings.setReaderTraces(true)
        Diagnostics.note("auth.refresh", "подновяването на сесията е отказано")
        c.authRepository.signOutLocally()
        // Изчистването на следите е минало, когато флагът е свален.
        withTimeout(5_000) { while (c.settings.readerTraces()) delay(20) }
        assertTrue(log.entries().any { it.tag == "auth.refresh" }, "причината за изхода остава в дневника")
    }
}
