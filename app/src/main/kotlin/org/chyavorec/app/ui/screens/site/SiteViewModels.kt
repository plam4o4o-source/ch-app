package org.chyavorec.app.ui.screens.site

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.core.TrustedLinks
import org.chyavorec.data.http.FileDownloader
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.GalleryAlbum
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSection

class LinksViewModel(private val repo: SiteRepository) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<List<SiteLink>>())
    val state: StateFlow<ScreenState<List<SiteLink>>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.links(force)) }
    }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() { refresh(false) }
}

class PageViewModel(private val url: String, private val repo: SiteRepository) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<SitePage>())
    val state: StateFlow<ScreenState<SitePage>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.page(url, force)) }
    }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() { refresh(false) }
}

/**
 * „За читалището“: страницата „За нас“ (или „История“) от сайта. Без връзка и без кеш на
 * навигацията — мрежовата грешка (с „Опитай отново“ и сама при връщане на връзката);
 * сайтът без такава страница — „няма я“ ([AppError.NotFound]), което мрежата не поправя.
 */
class AboutChitalishteViewModel(private val repo: SiteRepository) : ViewModel(), RefreshOnReconnect {
    val link = MutableStateFlow<SiteLink?>(null)
    val error = MutableStateFlow<AppError?>(null)
    private val loading = MutableStateFlow(true)
    init { load() }

    fun retry() {
        if (loading.value || link.value != null) return
        load()
    }

    override val reconnectState: ScreenState<*>
        get() = ScreenState(data = link.value, loading = loading.value, error = error.value)

    override fun refreshOnReconnect() = retry()

    private fun load() {
        loading.value = true
        error.value = null
        viewModelScope.launch {
            val r = repo.links(false)
            val links = (r as? Outcome.Success)?.value?.data.orEmpty()
            val l = links.firstOrNull { it.kind == SiteSection.ABOUT } ?: links.firstOrNull { it.kind == SiteSection.HISTORY }
            link.value = l
            error.value = when {
                l != null -> null
                r is Outcome.Failure -> r.error
                else -> AppError.NotFound
            }
            loading.value = false
        }
    }
}

/**
 * Контактите: от сайта, без връзка — последно заредените (с „Последна синхронизация“), а
 * без кеш (напр. първо пускане без интернет) — вградените [fallback] вместо грешка.
 */
class ContactsViewModel(private val repo: SiteRepository, private val fallback: Contacts) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<Contacts>())
    val state: StateFlow<ScreenState<Contacts>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        val r = repo.contacts(force)
        _state.update { s ->
            // refreshError — за да се опита пак при връщане на връзката (wantsReconnectRefresh).
            if (r is Outcome.Failure && s.data?.fromSite != true) ScreenState(data = fallback, loading = false, refreshError = r.error)
            else s.with(r)
        }
    }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() { refresh(false) }
}

/**
 * Албумите на галерията. Хранилището слива едновременните заявки и връща една и съща
 * (вече групирана) стойност на всички екрани. [load] = false — екранът не ползва албумите
 * (напр. преглед на отделни снимки по адрес) и галерията не се зарежда.
 *
 * Без връзка ([online] = false) не се чака мрежата (заявките висят до изтичане на timeout-а,
 * а дотогава — скелет): веднага кешът или грешка с „Опитай отново“; при връщане на
 * връзката екранът се опреснява сам ([RefreshOnReconnect]).
 */
class GalleryViewModel(
    private val repo: SiteRepository,
    load: Boolean = true,
    private val online: () -> Boolean = { true },
) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<List<GalleryAlbum>>())
    val state: StateFlow<ScreenState<List<GalleryAlbum>>> = _state.asStateFlow()
    init { if (load) refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        val result = if (online()) repo.gallery(force)
        else repo.cachedGallery()?.let { Outcome.Success(it) } ?: Outcome.Failure(AppError.Network)
        _state.update { it.with(result) }
    }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() { refresh(false) }
}

class DocumentsViewModel(
    private val repo: SiteRepository,
    /** Свалянето на PDF за приложение на телефона (`null` — само отваряне в браузъра). */
    private val downloader: FileDownloader? = null,
    /** Базовият адрес на сайта — свалят се само документи от него. */
    private val siteBaseUrl: String = "https://" + TrustedLinks.SITE_HOST,
) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<List<org.chyavorec.domain.model.SiteDocument>>())
    val state: StateFlow<ScreenState<List<org.chyavorec.domain.model.SiteDocument>>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.documents(force)) }
    }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() { refresh(false) }

    /** Документът, който се сваля за приложение за PDF, и напредъкът (`null` — неизвестен размер). */
    data class PdfDownload(val url: String, val progress: Float?)

    /** Резултатът от свалянето: файлът за отваряне — или неуспех (тогава адресът се отваря в браузъра). */
    sealed interface PdfResult {
        val url: String
        data class Ready(override val url: String, val file: java.io.File) : PdfResult
        data class Failed(override val url: String) : PdfResult
    }

    private val _download = MutableStateFlow<PdfDownload?>(null)
    val download: StateFlow<PdfDownload?> = _download.asStateFlow()
    private val _results = kotlinx.coroutines.flow.MutableSharedFlow<PdfResult>(extraBufferCapacity = 1)
    val results: kotlinx.coroutines.flow.SharedFlow<PdfResult> = _results
    private var job: kotlinx.coroutines.Job? = null

    /** „Отвори с приложение за PDF“: сваля файла в кеша (само един наведнъж), после — за отваряне. */
    fun openInPdfApp(url: String) {
        val d = downloader
        if (d == null || !canDownload(url)) {
            _results.tryEmit(PdfResult.Failed(url))
            return
        }
        if (job?.isActive == true) return
        _download.value = PdfDownload(url, null)
        job = viewModelScope.launch {
            try {
                val r = d.download(url) { p -> _download.value = PdfDownload(url, p) }
                _results.emit(if (r is Outcome.Success) PdfResult.Ready(url, r.value) else PdfResult.Failed(url))
            } finally {
                _download.value = null
            }
        }
    }

    /** Отказ по време на свалянето — нищо не остава в кеша. */
    fun cancelDownload() { job?.cancel() }

    /** Бутонът „Отвори с приложение за PDF“ — само за PDF от сайта ([isPdf]). */
    fun canDownload(url: String): Boolean = isPdf(url, siteBaseUrl)

    companion object {
        /** PDF от сайта на читалището (по разширението в адреса); чужд адрес — само в браузъра. */
        fun isPdf(url: String, siteBaseUrl: String): Boolean =
            TrustedLinks.isSiteUrl(url, siteBaseUrl) && url.substringBefore('#').substringBefore('?').endsWith(".pdf", ignoreCase = true)
    }
}
