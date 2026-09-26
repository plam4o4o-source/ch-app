package org.chyavorec.app.ui.screens.site

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.domain.model.Contacts
import org.chyavorec.domain.model.GalleryAlbum
import org.chyavorec.domain.model.SiteLink
import org.chyavorec.domain.model.SitePage
import org.chyavorec.domain.model.SiteSection

class LinksViewModel(private val repo: SiteRepository) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<List<SiteLink>>())
    val state: StateFlow<ScreenState<List<SiteLink>>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.links(force)) }
    }
}

class PageViewModel(private val url: String, private val repo: SiteRepository) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<SitePage>())
    val state: StateFlow<ScreenState<SitePage>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.page(url, force)) }
    }
}

/** „За читалището“: страницата „За нас“ (или „История“) от сайта. */
class AboutChitalishteViewModel(private val repo: SiteRepository) : ViewModel() {
    val link = MutableStateFlow<SiteLink?>(null)
    val failed = MutableStateFlow(false)
    init {
        viewModelScope.launch {
            val links = (repo.links(false) as? Outcome.Success)?.value?.data.orEmpty()
            val l = links.firstOrNull { it.kind == SiteSection.ABOUT } ?: links.firstOrNull { it.kind == SiteSection.HISTORY }
            link.value = l
            failed.value = l == null
        }
    }
}

class ContactsViewModel(private val repo: SiteRepository) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<Contacts>())
    val state: StateFlow<ScreenState<Contacts>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.contacts(force)) }
    }
}

class GalleryViewModel(private val repo: SiteRepository) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<List<GalleryAlbum>>())
    val state: StateFlow<ScreenState<List<GalleryAlbum>>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.gallery(force)) }
    }
}

class DocumentsViewModel(private val repo: SiteRepository) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<List<org.chyavorec.domain.model.SiteDocument>>())
    val state: StateFlow<ScreenState<List<org.chyavorec.domain.model.SiteDocument>>> = _state.asStateFlow()
    init { refresh(false) }
    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _state.update { it.with(repo.documents(force)) }
    }
}
