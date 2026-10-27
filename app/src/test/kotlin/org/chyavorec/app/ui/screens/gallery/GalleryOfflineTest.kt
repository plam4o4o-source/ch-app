package org.chyavorec.app.ui.screens.gallery

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.ui.screens.site.GalleryViewModel
import org.chyavorec.app.ui.wantsReconnectRefresh
import org.chyavorec.app.ui.watchReconnect
import org.chyavorec.core.AppError
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
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
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Галерията без връзка и без кеш: веднага грешка с „Опитай отново“ (не скелет, докато
 * заявката „виси“ до timeout-а), а щом връзката се върне — зарежда се сама.
 */
class GalleryOfflineTest {
    @get:Rule val main = MainDispatcherRule()
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private var vm: GalleryViewModel? = null

    @After fun clear() { vm?.viewModelScope?.cancel() }

    /** Без връзка заявката не връща нищо (като истинската мрежа до изтичане на timeout-а). */
    private class HangingSite : SiteContentService {
        @Volatile var online = false
        val galleryCalls = AtomicInteger()

        private suspend fun <T> net(value: () -> T): Outcome<T> = if (online) Outcome.Success(value()) else awaitCancellation()

        override suspend fun discoverLinks(): Outcome<List<SiteLink>> = net { emptyList() }
        override suspend fun fetchPage(url: String): Outcome<SitePage> = net { SitePage(url, "", emptyList(), emptyList()) }
        override suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts> = Outcome.Failure(AppError.Network)
        override suspend fun fetchGallery(news: List<NewsArticle>?): Outcome<List<GalleryPhoto>> {
            galleryCalls.incrementAndGet()
            return net { listOf(GalleryPhoto("1", "Сцена", "https://example.org/t.jpg", "https://example.org/f.jpg", "Празници", "https://example.org/g")) }
        }
        override suspend fun fetchDocuments(): Outcome<List<SiteDocument>> = net { emptyList() }
        override suspend fun fetchSearchIndex(): Outcome<List<SiteSearchDoc>> = net { emptyList() }
        override suspend fun fetchFeast(date: String): Outcome<DailyFeast> = net { DailyFeast(date, "-") }
        override suspend fun fetchMessages(): Outcome<List<AppMessage>> = net { emptyList() }
    }

    private fun waitUntil(what: String, condition: () -> Boolean) {
        val end = System.currentTimeMillis() + 10_000
        while (!condition()) {
            if (System.currentTimeMillis() > end) throw AssertionError("не дочакахме: $what")
            Thread.sleep(10)
        }
    }

    @Test fun offlineWithEmptyCacheShowsErrorAndLoadsWhenTheConnectionReturns() {
        val site = HangingSite()
        val reconnected = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
        val gallery = GalleryViewModel(SiteRepository(site, InMemoryPayloadCache(), clock), online = { site.online }).also { vm = it }
        watchReconnect(gallery, reconnected)

        waitUntil("грешката без връзка") { gallery.state.value.error != null }
        val offline = gallery.state.value
        assertEquals(AppError.Network, offline.error)
        assertTrue(!offline.showSkeleton, "без връзка — не скелет")
        assertEquals(0, site.galleryCalls.get(), "без връзка мрежата не се пита")
        assertTrue(offline.wantsReconnectRefresh())

        site.online = true
        assertTrue(reconnected.tryEmit(Unit))
        waitUntil("албумите след връщане на връзката") { gallery.state.value.data != null }
        assertEquals("Празници", gallery.state.value.data!!.single().name)
        assertEquals(null, gallery.state.value.error)
    }
}
