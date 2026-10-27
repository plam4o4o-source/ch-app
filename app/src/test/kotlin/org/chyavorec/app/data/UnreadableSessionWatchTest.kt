package org.chyavorec.app.data

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import org.chyavorec.app.AppConfig
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.SecureSessionStore
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.AuthSession
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Временно нечетима сесия (Keystore) не е изход: наблюдателите на входа не изчистват личните
 * следи и напомнянията, а когато Keystore-ът отговори — същият читател е вътре, без нов вход.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class UnreadableSessionWatchTest {
    /** Keystore „не отговаря“, докато [broken]; иначе — тестовото шифроване. */
    private class FlakyCipher : BytesCipher {
        @Volatile var broken = true
        private val xor = XorTestCipher()
        override fun encrypt(plain: ByteArray) = xor.encrypt(plain)
        override fun decrypt(data: ByteArray): ByteArray {
            if (broken) throw java.security.ProviderException("keystore busy")
            return xor.decrypt(data)
        }
    }

    @Test fun watchersKeepTracesWhileSessionIsUnreadable(): Unit = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
        val sessionFile = File(context.noBackupFilesDir, "secure/session.bin")
        SecureSessionStore(sessionFile, XorTestCipher())
            .save(AuthSession("a", "r", clock.now().toEpochMilli() + 3_600_000, "R1"), persist = true)
        val cipher = FlakyCipher()
        val config = AppConfig(
            siteBaseUrl = "https://chyavorec.org", catalogUrls = emptyList(), inflibApiUrl = "",
            environment = "test", useMockData = false, versionName = "test", versionCode = 1,
        )
        val c = AppContainer(context, config, clock, cipher, OkHttpClient())
        // Следите от предишния процес с вход.
        c.settings.setReaderTraces(true)
        c.settings.addRecentSearch("Под игото")
        c.settings.setNotifiedLoans(setOf("L1"))
        c.settings.setLoanReminderPlan(setOf("L1"))
        c.watchReader()

        c.authRepository.restore()
        assertEquals(AuthState.Unavailable, c.authRepository.state.value)
        delay(500) // наблюдателите са видели състоянието
        assertTrue(c.settings.readerTraces())
        assertEquals(listOf("Под игото"), c.settings.recentSearches.first())
        assertEquals(setOf("L1"), c.settings.notifiedLoans())
        assertEquals(setOf("L1"), c.settings.loanReminderPlan(), "напомнянията не са отменени")
        assertTrue(sessionFile.exists())

        // Keystore-ът отговаря: следващият опит е вход със същата сесия, нищо не е изтрито.
        cipher.broken = false
        c.authRepository.restore()
        assertEquals(AuthState.SignedIn("R1"), c.authRepository.state.value)
        delay(300)
        assertEquals(setOf("L1"), c.settings.notifiedLoans())
        assertEquals(listOf("Под игото"), c.settings.recentSearches.first())

        // Наблюдателят е бил жив през цялото време: истински изход изчиства.
        c.authRepository.signOutLocally()
        withTimeout(5_000) { while (c.settings.readerTraces()) delay(20) }
        assertTrue(c.settings.notifiedLoans().isEmpty())
    }
}
