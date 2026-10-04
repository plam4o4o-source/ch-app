package org.chyavorec.app.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.data.local.FilePayloadCache
import org.chyavorec.app.data.local.SecureSelfCardStore
import org.chyavorec.app.data.local.SecureSessionStore
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.SelfDeclaredCard
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SecureStorageTest {
    @get:Rule val tmp = TemporaryFolder()

    @Test fun encryptedCacheNeverWritesPlaintext() = runTest {
        val dir = tmp.newFolder("reader")
        val cache = FilePayloadCache(dir, XorTestCipher())
        cache.write("reader:loans", """[{"title":"Под игото"}]""")
        val raw = dir.listFiles()!!.single().readBytes().toString(Charsets.UTF_8)
        assertFalse(raw.contains("Под игото"), "съдържанието на диска трябва да е шифровано")
        assertEquals("""[{"title":"Под игото"}]""", cache.read("reader:loans")!!.text)
    }

    @Test fun corruptedCacheIsDiscarded() = runTest {
        val dir = tmp.newFolder("c")
        val cache = FilePayloadCache(dir, object : org.chyavorec.app.data.security.BytesCipher {
            override fun encrypt(plain: ByteArray) = plain
            override fun decrypt(data: ByteArray): ByteArray = throw IllegalStateException("bad key")
        })
        cache.write("k", "v")
        assertNull(cache.read("k"))
        assertTrue(dir.listFiles()!!.isEmpty())
    }

    @Test fun publicCacheRoundTripAndClear() = runTest {
        val cache = FilePayloadCache(tmp.newFolder("p"))
        cache.write("a", "1"); cache.write("b", "2")
        assertEquals("2", cache.read("b")?.text)
        cache.clear()
        assertNull(cache.read("a"))
    }

    @Test fun sessionNotPersistedWhenNotRemembered() = runTest {
        val file = File(tmp.root, "secure/session.bin")
        val store = SecureSessionStore(file, XorTestCipher())
        val s = AuthSession("token-123", "refresh", 99, "R1")
        store.save(s, persist = false)
        assertFalse(file.exists())
        assertFalse(store.isPersisted())
        assertEquals(s, store.load())
        store.save(s, persist = true)
        assertTrue(file.exists())
        assertTrue(store.isPersisted())
        assertFalse(file.readBytes().toString(Charsets.UTF_8).contains("token-123"))
        // Нов процес: четене от диска.
        assertEquals(s, SecureSessionStore(file, XorTestCipher()).load())
        store.clear()
        assertFalse(file.exists())
        assertNull(store.load())
    }

    @Test fun selfCardStore() = runTest {
        val store = SecureSelfCardStore(File(tmp.root, "card.bin"), XorTestCipher())
        assertNull(store.load())
        store.save(SelfDeclaredCard("R-42", "Иван"))
        assertEquals("R-42", store.load()?.cardNumber)
        store.clear()
        assertNull(store.load())
    }
}
