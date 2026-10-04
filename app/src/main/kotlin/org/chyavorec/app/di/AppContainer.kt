package org.chyavorec.app.di

import android.content.Context
import android.os.Build
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.chyavorec.app.AppConfig
import org.chyavorec.app.data.local.AppDatabase
import org.chyavorec.app.data.local.FilePayloadCache
import org.chyavorec.app.data.local.SecureSelfCardStore
import org.chyavorec.app.data.local.SecureSessionStore
import org.chyavorec.app.data.local.SettingsStore
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.app.data.security.KeystoreBytesCipher
import org.chyavorec.app.network.ConnectivityObserver
import org.chyavorec.app.network.NetworkModule
import org.chyavorec.app.notifications.ReminderScheduler
import org.chyavorec.app.update.AppUpdater
import org.chyavorec.app.widget.ChitalishteWidget
import org.chyavorec.app.messages.MessageCenter
import org.chyavorec.core.AppClock
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
import org.chyavorec.data.repository.SelfCardRepository
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.data.site.ChyavorecSiteService
import org.chyavorec.domain.model.Contacts
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

    val okHttp by lazy { okHttpOverride ?: NetworkModule.okHttp(context) }
    private val userAgent = "ChitalishteYavorec-Android/${config.versionName} (Android ${Build.VERSION.RELEASE})"
    val http by lazy { HttpFetcher(okHttp, userAgent) }
    val connectivity by lazy { ConnectivityObserver(context) }

    val settings by lazy { SettingsStore(context) }
    val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "chitalishte.db").fallbackToDestructiveMigration(true).build()
    }

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
    val catalogService by lazy { GitHubCatalogService(http, config.catalogUrls) }

    val readerServices: ReaderServices by lazy {
        val demo = FlavorServices.demoReaderServices(config, clock)
        when {
            demo != null -> demo
            config.inflibConfigured -> {
                val remote = RemoteInvLibClient(http, config.inflibApiUrl, clock, Build.MODEL ?: "Android")
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
    val catalogRepository by lazy { CatalogRepository(catalogService, publicCache, clock) }
    val siteRepository by lazy { SiteRepository(siteService, publicCache, clock, newsRepository) }

    val authRepository by lazy {
        AuthRepository(readerServices.auth, SecureSessionStore(File(context.noBackupFilesDir, "secure/session.bin"), cipher), clock, readerCache)
    }
    val profileRepository by lazy { ProfileRepository(readerServices.reader, authRepository, readerCache, clock, readerServices.membership) }
    val libraryRepository by lazy { LibraryRepository(readerServices.reader, authRepository, readerCache, clock) }
    val membershipRepository by lazy { MembershipRepository(readerServices.membership, authRepository, readerCache, clock) }
    val selfCardRepository by lazy { SelfCardRepository(SecureSelfCardStore(File(context.noBackupFilesDir, "secure/card.bin"), cipher)) }

    val messagesRepository by lazy { MessagesRepository(siteService, publicCache, clock) }
    val messages by lazy { MessageCenter(messagesRepository, authRepository, selfCardRepository, settings) }

    val updater by lazy { AppUpdater(context, okHttp, http, config, settings, clock) }

    val reminders by lazy { ReminderScheduler(context, database.reminders(), clock) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * При ВСЕКИ преход от вход към изход — ръчен изход или принудителен (отказано
     * подновяване на сесията) — изчиства личните следи (история на търсенето,
     * известия за заемания). Извиква се веднъж от Application.
     */
    fun watchSignOut() {
        appScope.launch {
            var wasSignedIn = false
            authRepository.state.collect { s ->
                if (s is AuthState.SignedIn) {
                    if (!wasSignedIn) ChitalishteWidget.refresh(context)
                    wasSignedIn = true
                } else if (s is AuthState.SignedOut && wasSignedIn) {
                    wasSignedIn = false
                    runCatching { settings.clearPersonal() }
                    // Уиджетът не бива да показва заглавия на книги след изход.
                    ChitalishteWidget.refresh(context)
                }
            }
        }
    }

    /** „Изчисти кеша“ — само публичните данни и изображенията; сесията остава. */
    suspend fun clearPublicCache() {
        publicCache.clear()
        runCatching { okHttp.cache?.evictAll() }
        File(context.cacheDir, "image_cache").deleteRecursively()
    }
}
