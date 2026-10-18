package org.chyavorec.app.di

import android.content.Context
import android.os.Build
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import coil3.SingletonImageLoader
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.chyavorec.app.AppConfig
import org.chyavorec.app.data.local.AppDatabase
import org.chyavorec.app.data.local.CatalogDatabase
import org.chyavorec.app.data.local.DataStoreThrottleStore
import org.chyavorec.app.data.local.FilePayloadCache
import org.chyavorec.app.data.local.RoomCatalogIndexStore
import org.chyavorec.app.data.local.SecureSelfCardStore
import org.chyavorec.app.data.local.SecureSessionStore
import org.chyavorec.app.data.local.SettingsStore
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.app.data.security.KeystoreBytesCipher
import org.chyavorec.app.network.ConnectivityObserver
import org.chyavorec.app.network.NetworkModule
import org.chyavorec.app.notifications.LoanReminderScheduler
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.notifications.ReminderScheduler
import org.chyavorec.app.update.AppUpdater
import org.chyavorec.app.widget.ChitalishteWidget
import org.chyavorec.app.messages.MessageCenter
import org.chyavorec.core.AppClock
import org.chyavorec.core.Diagnostics
import org.chyavorec.core.logFailure
import org.chyavorec.core.reconnections
import org.chyavorec.core.SystemClock
import org.chyavorec.data.catalog.GitHubCatalogService
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.invlib.RemoteInvLibClient
import org.chyavorec.data.invlib.UnavailableInvLibServices
import org.chyavorec.data.repository.AuthRepository
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.CatalogRepository
import org.chyavorec.data.repository.EventsRepository
import org.chyavorec.data.repository.LibraryRepository
import org.chyavorec.data.repository.MembershipRepository
import org.chyavorec.data.repository.MessagesRepository
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.data.repository.ProfileRepository
import org.chyavorec.data.repository.ReaderDataRepository
import org.chyavorec.data.repository.ReaderMessagesRepository
import org.chyavorec.data.repository.SelfCardRepository
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.data.site.ChyavorecSiteService
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanReminderSync
import org.chyavorec.domain.service.AuthenticationService
import org.chyavorec.domain.service.MembershipService
import org.chyavorec.domain.service.ReaderService
import java.io.File

/** Трите читателски услуги — от една и съща реализация. */
data class ReaderServices(
    val auth: AuthenticationService,
    val reader: ReaderService,
    val membership: MembershipService,
    /** true само в dev с демо данни — UI показва ясна лента „ДЕМО ДАННИ“. */
    val isDemo: Boolean,
)

/**
 * Ръчно DI: един обект с всички зависимости, създаден от Application.
 * [cipherOverride] позволява тестовете да подменят Android Keystore.
 */
class AppContainer(
    private val context: Context,
    val config: AppConfig = AppConfig.fromBuildConfig(),
    val clock: AppClock = SystemClock,
    cipherOverride: BytesCipher? = null,
    /** Само за тестове (MockWebServer по HTTP); production ползва [NetworkModule.okHttp]. */
    okHttpOverride: okhttp3.OkHttpClient? = null,
) {
    private val cipher: BytesCipher by lazy { cipherOverride ?: KeystoreBytesCipher() }

    private val userAgent = "ChitalishteYavorec-Android/${config.versionName} (Android ${Build.VERSION.RELEASE})"
    val okHttp by lazy { okHttpOverride ?: NetworkModule.okHttp(context, userAgent) }
    val http by lazy { HttpFetcher(okHttp, userAgent) }
    /**
     * Читателският API (мостът към InvLib) — собствен клиент без HTTP кеш: личните отговори
     * (профил, заемания, история, съобщения) не се пазят нешифровани в cacheDir/http.
     * Връзките и настройките се споделят с [okHttp].
     */
    private val readerHttp by lazy { HttpFetcher(okHttp.newBuilder().cache(null).build(), userAgent) }
    val connectivity by lazy { ConnectivityObserver(context) }

    val settings by lazy { SettingsStore(context) }
    /** Потребителските данни (любими, напомняния) — с миграции, никога с изтриване ([AppDatabase.create]). */
    val database: AppDatabase by lazy { AppDatabase.create(context) }
    /**
     * Пълнотекстовият индекс на каталога (FTS4) — отделна база-кеш, която се попълва
     * отново от katalog.json; отваря се едва при първото търсене в каталога. Ако не се
     * чете (повреден файл), индексът я създава наново ([RoomCatalogIndexStore]).
     */
    val catalogDatabase: CatalogDatabase by lazy { CatalogDatabase.create(context) }

    /** Публичен кеш (новини, каталог, страници) — в noBackup папката. */
    val publicCache by lazy { FilePayloadCache(File(context.noBackupFilesDir, "cache/public")) }
    /** Шифрован кеш за читателските данни. */
    val readerCache by lazy { FilePayloadCache(File(context.noBackupFilesDir, "cache/reader"), cipher) }

    /** Показва се само ако страницата „Контакти“ не се зареди (данни от сайта към 09.2026). */
    val fallbackContacts = Contacts(
        organization = "НЧ „Васил Левски – 1922“",
        address = "пл. 9-ти Септември 3, с. Яворец, общ. Габрово, ПК 5334",
        phones = emptyList(),
        emails = listOf("chitalishte_yavorec@abv.bg"),
        website = config.siteBaseUrl,
        facebook = "https://www.facebook.com/nchvasillevski1922/",
        workingHours = emptyList(),
        mapQuery = "пл. 9-ти Септември 3, 5334 Яворец, Габрово",
        fromSite = false,
        sourceUrl = null,
    )

    val siteService by lazy { ChyavorecSiteService(http, config.siteBaseUrl, clock, fallbackContacts) }
    val catalogService by lazy { GitHubCatalogService(http, config.catalogUrls, legacyUrls = config.catalogLegacyUrls) }

    val readerServices: ReaderServices by lazy {
        val demo = FlavorServices.demoReaderServices(config, clock)
        when {
            demo != null -> demo
            config.inflibConfigured -> {
                // Възможностите (не са лични) се помнят 24 ч в публичния кеш.
                val remote = RemoteInvLibClient(readerHttp, config.inflibApiUrl, clock, Build.MODEL ?: "Android", publicCache)
                ReaderServices(remote, remote, remote, isDemo = false)
            }
            else -> {
                val none = UnavailableInvLibServices()
                ReaderServices(none, none, none, isDemo = false)
            }
        }
    }

    val newsRepository by lazy { NewsRepository(siteService, publicCache, clock) }
    val eventsRepository by lazy { EventsRepository(siteService, publicCache, clock) }
    val catalogRepository by lazy { CatalogRepository(catalogService, publicCache, clock, index = RoomCatalogIndexStore(recreate = { CatalogDatabase.recreate(context) }) { catalogDatabase }) }
    val siteRepository by lazy { SiteRepository(siteService, publicCache, clock, newsRepository) }

    val authRepository by lazy {
        AuthRepository(
            readerServices.auth, SecureSessionStore(File(context.noBackupFilesDir, "secure/session.bin"), cipher), clock, readerCache,
            // Защитата от налучкване на ПИН-а оцелява при рестарт (не се нулира при изход).
            throttleStore = DataStoreThrottleStore(context),
        )
    }
    /**
     * Едно общо хранилище за читателските данни (един кеш на вид, `/v1/me/all` при наличие,
     * профилът на ниво приложение) — всички читателски репозиторита го споделят.
     */
    val readerDataRepository by lazy {
        ReaderDataRepository(readerServices.reader, authRepository, readerCache, clock, readerServices.membership) { loans ->
            storedLoans.value = loans
        }
    }
    val profileRepository by lazy {
        ProfileRepository(readerServices.reader, authRepository, readerCache, clock, readerServices.membership, readerDataRepository)
    }
    val libraryRepository by lazy { LibraryRepository(readerServices.reader, authRepository, readerCache, clock, readerDataRepository) }
    val membershipRepository by lazy {
        MembershipRepository(readerServices.membership, authRepository, readerCache, clock, readerDataRepository)
    }
    val selfCardRepository by lazy { SelfCardRepository(SecureSelfCardStore(File(context.noBackupFilesDir, "secure/card.bin"), cipher)) }

    val messagesRepository by lazy { MessagesRepository(siteService, publicCache, clock) }
    /** Лични съобщения от библиотеката — в шифрования читателски кеш (изтрива се при изход). */
    val readerMessagesRepository by lazy {
        ReaderMessagesRepository(readerServices.reader, authRepository, readerCache, clock, readerDataRepository)
    }
    val messages by lazy {
        MessageCenter(messagesRepository, authRepository, selfCardRepository, settings, readerMessagesRepository, clock)
    }

    val updater by lazy { AppUpdater(context, okHttp, http, config, settings, clock) }

    val reminders by lazy { ReminderScheduler(context, database.reminders(), clock) }
    val loanReminders by lazy { LoanReminderScheduler(context, clock) }

    /** Последно записаните в кеша заемания (`null` = още няма запис в този процес). */
    private val storedLoans = MutableStateFlow<List<Loan>?>(null)

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * „Връзката се върна“ — по едно събитие при преход офлайн → онлайн, стабилен 2 s
     * ([reconnections]). Екраните с кеширани данни или грешка се опресняват сами (вж.
     * org.chyavorec.app.ui.watchReconnect). Мрежата се наблюдава само докато има слушател.
     */
    val reconnected: SharedFlow<Unit> by lazy {
        connectivity.online.reconnections().shareIn(appScope, SharingStarted.WhileSubscribed(), replay = 0)
    }

    /**
     * При ВСЕКИ преход от вход към изход — ръчен изход или принудителен (отказано
     * подновяване на сесията) — изчиства личните следи ([clearReaderTraces]). Също при старт
     * без сесия, ако предишният процес е имал вход („не ме помни“ и спрян процес — следващият
     * на същия телефон не бива да види нищо), и при вход на друг читател без изход между двата.
     * Извиква се веднъж от Application.
     */
    fun watchSignOut() {
        appScope.launch { runCatching { evictLegacyReaderHttpCache() }.logFailure("signout.http-cache") }
        appScope.launch {
            var wasSignedIn = false
            var previous: AuthState? = null
            authRepository.state.collect { s ->
                val before = previous
                previous = s
                if (s is AuthState.SignedIn) {
                    if (before is AuthState.SignedIn && before.readerId != s.readerId) clearReaderTraces()
                    // Само при истински вход (SignedOut → SignedIn). Възстановената сесия при
                    // студен старт (Unknown → SignedIn) не променя показаното в уиджета.
                    if (before is AuthState.SignedOut) ChitalishteWidget.refresh(context)
                    wasSignedIn = true
                    runCatching { settings.setReaderTraces(true) }
                } else if (s is AuthState.SignedOut) {
                    val traces = wasSignedIn || runCatching { settings.readerTraces() }.getOrDefault(false)
                    wasSignedIn = false
                    if (traces) clearReaderTraces()
                }
            }
        }
    }

    /**
     * Личните следи на излезлия читател: съобщенията в паметта, историята на търсенето и
     * следите от известията (DataStore), напомнянията за срок, показаните лични известия,
     * изображенията (снимка в профила, корици на заети книги) и отговорите на моста в HTTP
     * кеша. Шифрованият читателски кеш и сесията се трият в [AuthRepository].
     */
    private suspend fun clearReaderTraces() {
        // Личните съобщения изчезват веднага и от паметта (кешът им е в readerCache).
        runCatching { messages.clearPersonal() }.logFailure("signout.messages")
        runCatching { settings.clearPersonal() }.logFailure("signout.settings")
        // Напомнянията за срок на предишния читател — отменени, списъкът му — забравен.
        storedLoans.value = null
        runCatching { loanReminders.cancelAll() }.logFailure("signout.reminders")
        // Вече показаните известия за заемания и лични съобщения.
        runCatching { Notifier.cancelPersonal(context) }.logFailure("signout.notifications")
        clearImageCaches()
        runCatching { evictReaderApiFromHttpCache() }.logFailure("signout.http-cache")
        // Локалният дневник на грешките не съдържа лични данни, но се изчиства и той — за всеки случай.
        Diagnostics.clear()
        runCatching { settings.setReaderTraces(false) }
        // Уиджетът не бива да показва заглавия на книги след изход.
        runCatching { ChitalishteWidget.refresh(context) }
    }

    /**
     * Еднократно: отговорите на моста, кеширани от по-стари версии (преди собствения клиент без
     * кеш и `no-store`), се изтриват от HTTP кеша на [okHttp].
     */
    private suspend fun evictLegacyReaderHttpCache() {
        if (settings.readerHttpCacheEvicted()) return
        evictReaderApiFromHttpCache()
        settings.setReaderHttpCacheEvicted()
    }

    /** Изтрива от HTTP кеша на [okHttp] всички записи за адреса на моста (InvLib API). */
    private suspend fun evictReaderApiFromHttpCache() = withContext(Dispatchers.IO) {
        val host = config.inflibApiUrl.toHttpUrlOrNull()?.host ?: return@withContext
        val cache = okHttp.cache ?: return@withContext
        val urls = cache.urls()
        while (urls.hasNext()) {
            if (urls.next().toHttpUrlOrNull()?.host == host) urls.remove()
        }
    }

    /** Кешът на изображенията (Coil) — паметта и дискът. През Coil, а не с изтриване на папката: дисковият кеш е отворен. */
    private suspend fun clearImageCaches() = withContext(Dispatchers.IO) {
        runCatching {
            val images = SingletonImageLoader.get(context)
            images.memoryCache?.clear()
            images.diskCache?.clear()
        }
        Unit
    }

    /**
     * Напомнянията за срок на заемане — едно място, едно след друго (без надпревара
     * между стар и нов списък): при всеки запис на заеманията в кеша (екраните и фоновата
     * задача), при старт (от кеша) и при включване на известията — пренасрочване;
     * при изход или изключени известия за срокове — отмяна на всички.
     *
     * Пренасрочва се САМО разликата спрямо запомнения план ([LoanReminderSync]): при
     * непроменени заемания (обикновеният старт на процеса, вкл. всяко фоново събуждане)
     * нищо не се пипа. Докато върви напомняне (LoanReminderWorker) — изчакване: неговото
     * restore()/опресняване не бива да подменя (REPLACE) самото него.
     * Извиква се веднъж от Application.
     */
    fun watchLoanReminders() {
        appScope.launch {
            val signedIn = authRepository.state.map {
                when (it) {
                    is AuthState.SignedIn -> true
                    AuthState.SignedOut -> false
                    AuthState.Unknown -> null
                }
            }.distinctUntilChanged()
            val enabled = settings.settings.map { it.notifyLoans }.distinctUntilChanged()
            // collectLatest: по-нов списък отменя още чакащото прилагане на стария.
            combine(storedLoans, enabled, signedIn) { loans, on, inside -> Triple(loans, on, inside) }.collectLatest { (loans, on, inside) ->
                try {
                    syncLoanReminders(loans, on, inside)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // Следващата промяна (или старт) опитва отново — планът не е записан.
                    Diagnostics.record("loans.reminders", e)
                }
            }
        }
    }

    private suspend fun syncLoanReminders(loans: List<Loan>?, on: Boolean, inside: Boolean?) {
        when {
            // Сесията още не е проверена — нищо не се променя.
            inside == null -> Unit
            !on || !inside -> {
                // Празен план = нищо не е планирано; отмяна само ако може да има нещо.
                if (settings.loanReminderPlan()?.isEmpty() != true) {
                    loanReminders.cancelAll()
                    settings.setLoanReminderPlan(emptySet())
                }
            }
            else -> {
                val list = loans ?: libraryRepository.cachedLoans()?.data ?: return
                LoanReminderScheduler.awaitIdle()
                val previous = settings.loanReminderPlan()
                val changes = LoanReminderSync.diff(previous, list)
                if (changes.isEmpty && previous != null) return
                loanReminders.applyChanges(changes)
                settings.setLoanReminderPlan(LoanReminderSync.entries(list))
            }
        }
    }

    /**
     * Опреснява уиджета във фон (на ниво приложение, не на екрана) — напр. след
     * успешно зареждане на заеманията. Грешките се пренебрегват.
     */
    fun refreshWidget() {
        appScope.launch { runCatching { ChitalishteWidget.refresh(context) } }
    }

    /**
     * „Изчисти кеша“ — само публичните данни: новините, събитията, страниците, каталогът
     * (katalog.json с хеша/ETag-а, обобщението и „За теб“, индексът в базата), HTTP кешът
     * и изображенията (и кориците). Сесията, картата, любимите, напомнянията, историята и
     * настройките остават.
     */
    suspend fun clearPublicCache() = withContext(Dispatchers.IO) {
        // Първо каталогът: спира попълването на индекса и забравя състоянието в паметта.
        runCatching { catalogRepository.clear() }.logFailure("cache.catalog")
        publicCache.clear()
        runCatching { okHttp.cache?.evictAll() }
        clearImageCaches()
    }
}
