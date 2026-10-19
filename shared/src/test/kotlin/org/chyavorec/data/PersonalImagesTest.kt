package org.chyavorec.data

import kotlinx.coroutines.test.runTest
import org.chyavorec.core.FixedClock
import org.chyavorec.data.invlib.UnavailableInvLibServices
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.PersonalImageTrackingCache
import org.chyavorec.data.repository.PersonalImages
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.repository.SessionStore
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * При изход се махат само личните изображения (корици на заетите книги, снимката в
 * профила) — не целият кеш на изображенията.
 */
class PersonalImagesTest {
    private val loans = """[{"loanId":"L1","inv":7,"title":"Под игото","author":"Вазов","coverUrl":"https://lib.example/covers/7.jpg"},""" +
        """{"loanId":"L2","inv":8,"title":"Бай Ганьо","author":"Алеко","coverUrl":null}]"""
    private val profile = """{"readerId":"R1","cardNumber":"0042","fullName":"Иван","photoUrl":"https://lib.example/p/R1.jpg"}"""

    private class NoSession : SessionStore {
        override suspend fun load(): AuthSession? = null
        override suspend fun save(session: AuthSession, persist: Boolean) = Unit
        override suspend fun isPersisted() = false
        override suspend fun clear() = Unit
    }

    @Test fun findsCoverAndPhotoUrlsAtAnyDepth() {
        assertEquals(setOf("https://lib.example/covers/7.jpg"), PersonalImages.urlsIn(loans))
        assertEquals(setOf("https://lib.example/p/R1.jpg"), PersonalImages.urlsIn("""{"data":$profile,"syncedAt":1}"""))
        // Не-JSON, без адреси или с адрес, който не е http(s) — нищо.
        assertTrue(PersonalImages.urlsIn("не е json coverUrl").isEmpty())
        assertTrue(PersonalImages.urlsIn("""{"title":"Без корица"}""").isEmpty())
        assertTrue(PersonalImages.urlsIn("""{"coverUrl":"file:///etc/passwd"}""").isEmpty())
    }

    @Test fun clearEvictsEveryPersonalImageEverSeenButNothingElse() = runTest {
        val evicted = mutableListOf<Set<String>>()
        val base = InMemoryPayloadCache()
        val cache = PersonalImageTrackingCache(base) { evicted += it }
        cache.write("r1/reader:loans", loans)
        cache.write("r1/reader:profile", profile)
        // Книгата е върната — новият списък е без нея, но корицата ѝ още е в кеша на изображенията.
        cache.write("r1/reader:loans", "[]")
        assertEquals("[]", cache.read("r1/reader:loans")?.text)
        cache.clear()
        assertEquals(listOf(setOf("https://lib.example/covers/7.jpg", "https://lib.example/p/R1.jpg")), evicted)
        assertNull(base.read("r1/reader:profile"))
        // Следващото изчистване (нищо ново) не маха нищо повече.
        cache.clear()
        assertEquals(1, evicted.size)
    }

    @Test fun rememberedAcrossRestartAndClearedOnStartWithoutSession() = runTest {
        val disk = InMemoryPayloadCache()
        PersonalImageTrackingCache(disk) { }.write("r1/reader:loans", loans)
        // Нов процес („не ме помни“): сесия няма, restore() трие читателския кеш.
        val evicted = mutableSetOf<String>()
        val cache = PersonalImageTrackingCache(disk) { evicted += it }
        val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
        AuthRepository(UnavailableInvLibServices(), NoSession(), clock, cache).restore()
        assertEquals(setOf("https://lib.example/covers/7.jpg"), evicted)
        assertNull(disk.read("r1/reader:loans"))
    }

    @Test fun dataWrittenBeforeTheListExistedIsRememberedWhenRead() = runTest {
        val disk = InMemoryPayloadCache()
        // Записано от по-стара версия (без списъка с адреси).
        disk.write("r1/reader:profile", profile)
        val evicted = mutableSetOf<String>()
        val cache = PersonalImageTrackingCache(disk) { evicted += it }
        assertEquals(profile, cache.read("r1/reader:profile")?.text)
        cache.clear()
        assertEquals(setOf("https://lib.example/p/R1.jpg"), evicted)
    }

    @Test fun repeatedWritesOfKnownUrlsDoNotRewriteTheList() = runTest {
        val base = InMemoryPayloadCache()
        val cache = PersonalImageTrackingCache(base) { }
        cache.write("r1/reader:loans", loans)
        val writes = base.writes
        cache.write("r1/reader:loans", loans)
        assertEquals(writes + 1, base.writes, "само самият запис, без списъка с адреси")
    }
}
