package org.chyavorec.app.ui.screens.site

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import org.chyavorec.app.MainDispatcherRule
import org.chyavorec.app.ui.RefreshOnReconnect
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
import org.chyavorec.domain.model.SiteSection
import org.chyavorec.domain.repository.InMemoryPayloadCache
import org.chyavorec.domain.service.SiteContentService
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Екраните от сайта (галерия, контакти, документи, навигация, страница, „За читалището“),
 * отворени без връзка и без кеш, се зареждат сами, щом връзката се върне — както началният
 * екран, без „Опитай отново“. Заредените вече не се теглят наново при следващо връщане.
 */
class SiteReconnectTest {
    @get:Rule val main = MainDispatcherRule()
    private val clock = FixedClock(Instant.parse("2026-09-26T10:00:00Z"))
    private val created = mutableListOf<ViewModel>()

    @After fun clear() = created.forEach { it.viewModelScope.cancel() }

    private class FakeSite : SiteContentService {
        @Volatile var online = false
        val calls = ConcurrentHashMap<String, Int>()

        private fun <T> net(name: String, value: () -> T): Outcome<T> {
            calls.merge(name, 1, Int::plus)
            return if (online) Outcome.Success(value()) else Outcome.Failure(AppError.Network)
        }

        override suspend fun discoverLinks(): Outcome<List<SiteLink>> =
            net("links") { listOf(SiteLink("За нас", ABOUT_URL, SiteSection.ABOUT)) }
        override suspend fun fetchPage(url: String): Outcome<SitePage> = net("page") { SitePage(url, "За нас", emptyList(), emptyList()) }
        override suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts> = net("contacts") {
            Contacts(
                organization = "НЧ „Васил Левски – 1922“", address = null, phones = emptyList(), emails = emptyList(),
                website = "https://example.org", facebook = null, workingHours = emptyList(), mapQuery = "Яворец",
                fromSite = true, sourceUrl = null,
            )
        }
        override suspend fun fetchGallery(news: List<NewsArticle>?): Outcome<List<GalleryPhoto>> =
            net("gallery") { listOf(GalleryPhoto("1", "Сцена", "https://example.org/t.jpg", "https://example.org/f.jpg", "Празници", "https://example.org/g")) }
        override suspend fun fetchDocuments(): Outcome<List<SiteDocument>> =
            net("documents") { listOf(SiteDocument("1", "Устав", "https://example.org/ustav.pdf")) }
        override suspend fun fetchSearchIndex(): Outcome<List<SiteSearchDoc>> = net("index") { emptyList() }
        override suspend fun fetchFeast(date: String): Outcome<DailyFeast> = net("feast") { DailyFeast(date, "-") }
        override suspend fun fetchMessages(): Outcome<List<AppMessage>> = net("messages") { emptyList() }
    }

    /** Хранилището пише в кеша на Dispatchers.Default — изчакваме в реално време. */
    private fun waitUntil(what: String, condition: () -> Boolean) {
        val end = System.currentTimeMillis() + 10_000
        while (!condition()) {
            if (System.currentTimeMillis() > end) throw AssertionError("не дочакахме: $what")
            Thread.sleep(10)
        }
    }

    @Test fun siteScreensLoadByThemselvesWhenTheConnectionReturns() {
        val site = FakeSite()
        val repo = SiteRepository(site, InMemoryPayloadCache(), clock)
        val reconnected = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

        val gallery = GalleryViewModel(repo)
        val contacts = ContactsViewModel(repo)
        val documents = DocumentsViewModel(repo)
        val links = LinksViewModel(repo)
        val page = PageViewModel(ABOUT_URL, repo)
        val about = AboutChitalishteViewModel(repo)
        val all = listOf(gallery, contacts, documents, links, page, about)
        created += all
        all.forEach { watchReconnect(it, reconnected) }

        // Без връзка и без кеш: всеки екран е с мрежова грешка и иска опресняване при връзка.
        waitUntil("грешките без връзка") {
            listOf(gallery.state.value, contacts.state.value, documents.state.value, links.state.value, page.state.value)
                .all { it.error == AppError.Network } && about.error.value == AppError.Network
        }
        all.forEach { vm ->
            vm as RefreshOnReconnect
            assertTrue(vm.reconnectState.wantsReconnectRefresh(), "${vm::class.simpleName} иска опресняване")
        }

        site.online = true
        assertTrue(reconnected.tryEmit(Unit))
        waitUntil("данните след връщане на връзката") {
            gallery.state.value.data != null && contacts.state.value.data != null && documents.state.value.data != null &&
                links.state.value.data != null && page.state.value.data != null && about.link.value != null
        }
        assertEquals("Празници", gallery.state.value.data!!.single().name)
        assertEquals(ABOUT_URL, about.link.value?.url)
        assertEquals(null, about.error.value)
        all.forEach { vm ->
            vm as RefreshOnReconnect
            assertTrue(!vm.reconnectState.wantsReconnectRefresh(), "${vm::class.simpleName} е зареден")
        }

        // Следващо връщане на връзката: заредените екрани не теглят нищо наново.
        val before = HashMap(site.calls)
        assertTrue(reconnected.tryEmit(Unit))
        Thread.sleep(200)
        assertEquals<Map<String, Int>>(before, HashMap(site.calls))
    }

    /** Сайтът без страница „За нас“/„История“ — „няма я“, и връщането на връзката не я търси. */
    @Test fun aboutWithoutSuchPageIsNotRetried() {
        val site = FakeSite().apply { online = true }
        val repo = object : SiteContentService by site {
            override suspend fun discoverLinks(): Outcome<List<SiteLink>> = Outcome.Success(emptyList())
        }
        val about = AboutChitalishteViewModel(SiteRepository(repo, InMemoryPayloadCache(), clock))
        created += about
        waitUntil("„няма я“") { about.error.value != null }
        assertEquals(AppError.NotFound, about.error.value)
        assertTrue(!about.reconnectState.wantsReconnectRefresh())
        assertNotNull(about.error.value)
    }

    private companion object {
        const val ABOUT_URL = "https://example.org/za-nas"
    }
}
