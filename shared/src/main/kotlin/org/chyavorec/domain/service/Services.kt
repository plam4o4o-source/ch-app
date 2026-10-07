package org.chyavorec.domain.service

import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.CatalogSnapshot
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.DailyFeast
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.GalleryPhoto
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.Membership
import org.chyavorec.domain.model.NewsArticle
import org.chyavorec.domain.model.ReaderProfile
import org.chyavorec.domain.model.ServiceCapabilities
import org.chyavorec.domain.model.SiteDocument
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSearchDoc

/*
 * Интерфейси към външните източници на данни. Всяка реализация е заменяема:
 * ако утре сайтът получи JSON API или InvLib — онлайн сървър, се сменя само
 * реализацията, не и репозиториите/UI.
 */

/** Публичният каталог на библиотеката (InvLib → katalog.json). */
interface CatalogService {
    /** Връща суровия JSON текст (за кеширане) и разчетения каталог. */
    suspend fun fetchCatalog(): Outcome<Pair<String, CatalogSnapshot>>

    /**
     * Като [fetchCatalog], но ако сваленото съдържание има SHA-256 [knownHash]
     * (т.е. е същото като кешираното), не се разчита повторно и се връща `null`.
     */
    suspend fun fetchCatalogIfChanged(knownHash: String?): Outcome<Pair<String, CatalogSnapshot>?> = fetchCatalog()
}

/** Новини от chyavorec.org (/data/news.json). */
interface NewsService {
    suspend fun fetchLatest(): Outcome<List<NewsArticle>>
    suspend fun fetchArticle(article: NewsArticle): Outcome<ArticleDetail>
}

/** Събития от календара на chyavorec.org. */
interface EventsService {
    suspend fun fetchEvents(): Outcome<List<Event>>
}

/** Страници, навигация, контакти и галерия на chyavorec.org. */
interface SiteContentService {
    suspend fun discoverLinks(): Outcome<List<SiteLink>>
    suspend fun fetchPage(url: String): Outcome<SitePage>
    suspend fun fetchContacts(links: List<SiteLink>): Outcome<Contacts>
    /** [news] — вече свалените новини (за албума „Новини“); `null` = свали ги сам. */
    suspend fun fetchGallery(news: List<NewsArticle>? = null): Outcome<List<GalleryPhoto>>
    suspend fun fetchDocuments(): Outcome<List<SiteDocument>>
    suspend fun fetchSearchIndex(): Outcome<List<SiteSearchDoc>>
    /** [date] във формат Г-М-Д без водещи нули (както очаква сайтът). */
    suspend fun fetchFeast(date: String): Outcome<DailyFeast>
    /** Съобщенията до приложението (`/data/app-messages.json`); няма файл = няма съобщения. */
    suspend fun fetchMessages(): Outcome<List<org.chyavorec.domain.model.AppMessage>>
}

/** Вход в читателския профил (InvLib — бъдещ онлайн API). */
interface AuthenticationService {
    suspend fun capabilities(): Outcome<ServiceCapabilities>
    suspend fun login(cardNumber: String, password: CharArray): Outcome<AuthSession>
    suspend fun refresh(session: AuthSession): Outcome<AuthSession>
    suspend fun logout(session: AuthSession): Outcome<Unit>
    suspend fun requestPasswordReset(cardNumber: String): Outcome<Unit>
}

/** Читателски данни (InvLib — бъдещ онлайн API). */
interface ReaderService {
    suspend fun profile(session: AuthSession): Outcome<ReaderProfile>
    suspend fun loans(session: AuthSession): Outcome<List<Loan>>
    suspend fun placeHold(session: AuthSession, inv: Long): Outcome<Unit>
    suspend fun renew(session: AuthSession, loanId: String): Outcome<Loan>
    suspend fun requestAccountDeletion(session: AuthSession): Outcome<Unit>
    /** Актуален статус на конкретен екземпляр (по-точен от katalog.json). */
    suspend fun availability(inv: Long): Outcome<BookStatus>
    /** История на четенето (най-новите първо, до 200) — само ако сървърът я поддържа. */
    suspend fun history(session: AuthSession): Outcome<List<HistoryItem>>
}

/** Членство/регистрация (InvLib — бъдещ онлайн API). */
interface MembershipService {
    suspend fun membership(session: AuthSession): Outcome<Membership>
}
