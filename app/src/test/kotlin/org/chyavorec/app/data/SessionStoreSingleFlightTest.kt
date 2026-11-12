package org.chyavorec.app.data

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.SecureSessionStore
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.domain.model.AuthSession
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Старт на процеса: сесията се иска едновременно от няколко места. Файлът и Keystore-ът се
 * четат веднъж (и при временна грешка — един общ повторен опит), а не по веднъж на повикване.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class SessionStoreSingleFlightTest {
    /** Брои четенията; докато [broken] — Keystore „не отговаря“ (временна грешка). */
    private class CountingCipher : BytesCipher {
        val decrypts = AtomicInteger()
        @Volatile var broken = false
        private val xor = XorTestCipher()
        override fun encrypt(plain: ByteArray) = xor.encrypt(plain)
        override fun decrypt(data: ByteArray): ByteArray {
            decrypts.incrementAndGet()
            if (broken) throw java.security.ProviderException("keystore busy")
            return xor.decrypt(data)
        }
    }

    private val session = AuthSession("a", "r", 4_000_000_000_000L, "R1")

    private fun file(name: String): File {
        val context = ApplicationProvider.getApplicationContext<Application>()
        return File(context.noBackupFilesDir, "secure/$name.bin").also { it.delete() }
    }

    /** Записана сесия във файл; хранилище с броящ шифър, което още не я е прочело. */
    private fun storeWithFile(name: String, cipher: CountingCipher, retryDelayMs: Long = 0): SecureSessionStore {
        val f = file(name)
        runBlocking { SecureSessionStore(f, XorTestCipher()).save(session, persist = true) }
        return SecureSessionStore(f, cipher, retryDelayMs)
    }

    @Test fun concurrentLoadsReadTheFileOnce() = runBlocking {
        val cipher = CountingCipher()
        val store = storeWithFile("single-ok", cipher)
        val results = (1..8).map { async(Dispatchers.Default) { store.load() } }.awaitAll()
        assertTrue(results.all { it == session }, "всички получават сесията: $results")
        assertEquals(1, cipher.decrypts.get(), "един достъп до Keystore, не осем")
    }

    @Test fun concurrentLoadsOnTransientErrorShareOneRetry() = runBlocking {
        val cipher = CountingCipher().apply { broken = true }
        // Изчакването е достатъчно дълго, за да се наредят всички повиквания зад първото.
        val store = storeWithFile("single-flaky", cipher, retryDelayMs = 400)
        val started = System.currentTimeMillis()
        val results = (1..6).map { async(Dispatchers.Default) { store.load() } }.awaitAll()
        val took = System.currentTimeMillis() - started
        assertTrue(results.all { it == null }, "временно нечетима — без сесия сега")
        assertEquals(2, cipher.decrypts.get(), "опит + един общ повторен опит (не по два на повикване)")
        assertTrue(took < 6 * 400, "изчакванията не се трупат едно след друго: $took ms")
    }

    @Test fun laterLoadAfterTheFailureReadsAgain() = runBlocking {
        val cipher = CountingCipher().apply { broken = true }
        val store = storeWithFile("single-later", cipher)
        assertNull(store.load())
        assertEquals(2, cipher.decrypts.get())
        // Keystore-ът отговаря: последователното повикване чете наново (не взема стария резултат).
        cipher.broken = false
        assertEquals(session, store.load())
        assertEquals(3, cipher.decrypts.get())
        // След успех — от паметта, без четене.
        assertEquals(session, store.load())
        assertEquals(3, cipher.decrypts.get())
    }

    @Test fun clearedSessionIsNotServedToAWaitingCaller() = runBlocking {
        val cipher = CountingCipher()
        val store = storeWithFile("single-clear", cipher)
        assertNotNull(store.load())
        store.clear()
        assertNull(store.load(), "след изход четенето не връща старата сесия")
    }
}
