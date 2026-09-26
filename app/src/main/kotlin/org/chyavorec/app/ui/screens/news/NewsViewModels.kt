package org.chyavorec.app.ui.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.data.local.FavoriteNewsEntity
import org.chyavorec.app.data.local.FavoritesDao
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.toState
import org.chyavorec.core.AppClock
import org.chyavorec.core.AppError
import org.chyavorec.core.TextNormalizer
import org.chyavorec.data.repository.NewsRepository
import org.chyavorec.domain.model.ArticleDetail
import org.chyavorec.domain.model.NewsArticle

data class NewsFilter(val query: String = "", val category: String? = null, val favoritesOnly: Boolean = false)

data class NewsUiState(
    val state: ScreenState<List<NewsArticle>> = ScreenState(),
    val filter: NewsFilter = NewsFilter(),
    val categories: List<String> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val visible: List<NewsArticle> = emptyList(),
)

class NewsListViewModel(
    private val repo: NewsRepository,
    private val favorites: FavoritesDao,
    private val clock: AppClock,
) : ViewModel() {

    private val load = MutableStateFlow(ScreenState<List<NewsArticle>>())
    private val filter = MutableStateFlow(NewsFilter())

    val ui: StateFlow<NewsUiState> = combine(load, filter, favorites.observeAll()) { s, f, favs ->
        val favIds = favs.map { it.id }.toSet()
        val source: List<NewsArticle> = if (f.favoritesOnly) {
            // Любимите работят и офлайн — пазят се в Room.
            favs.map { NewsArticle(it.id, it.title, it.url, it.publishedAtMillis, it.summary, it.imageUrl, it.category) }
        } else s.data.orEmpty()
        val tokens = TextNormalizer.tokens(f.query)
        NewsUiState(
            state = s,
            filter = f,
            categories = s.data.orEmpty().mapNotNull { it.category }.distinct(),
            favoriteIds = favIds,
            visible = source.filter { a ->
                (f.category == null || a.category == f.category) &&
                    (tokens.isEmpty() || TextNormalizer.normalize(a.title + " " + a.summary).let { t -> tokens.all { t.contains(it) } })
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NewsUiState())

    init {
        viewModelScope.launch {
            repo.cached()?.let { c -> load.value = c.toState().copy(loading = true) }
            refresh(false)
        }
    }

    fun refresh(force: Boolean = true) = viewModelScope.launch {
        load.update { it.startRefresh() }
        val r = repo.latest(force)
        load.update { it.with(r) }
    }

    fun setQuery(q: String) = filter.update { it.copy(query = q) }
    fun setCategory(c: String?) = filter.update { it.copy(category = c) }
    fun setFavoritesOnly(on: Boolean) = filter.update { it.copy(favoritesOnly = on) }

    fun toggleFavorite(a: NewsArticle, isFavorite: Boolean) = viewModelScope.launch {
        if (isFavorite) favorites.delete(a.id)
        else favorites.upsert(
            FavoriteNewsEntity(a.id, a.title, a.url, a.imageUrl, a.summary, a.category, a.publishedAtMillis, clock.now().toEpochMilli()),
        )
    }
}

class ArticleViewModel(
    private val id: String,
    private val repo: NewsRepository,
    private val favorites: FavoritesDao,
    private val clock: AppClock,
) : ViewModel() {
    private val _state = MutableStateFlow(ScreenState<ArticleDetail>())
    val state: StateFlow<ScreenState<ArticleDetail>> = _state.asStateFlow()
    val isFavorite: StateFlow<Boolean> = favorites.observeIds().map { ids -> id in ids }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private var article: NewsArticle? = null

    init { load(false) }

    fun load(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        val a = article ?: repo.find(id) ?: favoriteFallback()
        if (a == null) {
            _state.value = ScreenState(loading = false, error = AppError.NotFound)
            return@launch
        }
        article = a
        // Докато зарежда пълния текст — показваме заглавието и резюмето от RSS.
        if (_state.value.data == null) {
            _state.value = ScreenState(ArticleDetail(a, a.contentBlocks, listOfNotNull(a.imageUrl)), loading = true)
        }
        _state.update { it.with(repo.article(a, force)).let { s -> if (s.data == null) s.copy(data = it.data) else s } }
    }

    private suspend fun favoriteFallback(): NewsArticle? =
        favorites.observeAll().first().firstOrNull { it.id == id }
            ?.let { NewsArticle(it.id, it.title, it.url, it.publishedAtMillis, it.summary, it.imageUrl, it.category) }

    fun toggleFavorite() = viewModelScope.launch {
        val a = article ?: return@launch
        if (isFavorite.value) favorites.delete(a.id)
        else favorites.upsert(FavoriteNewsEntity(a.id, a.title, a.url, a.imageUrl, a.summary, a.category, a.publishedAtMillis, clock.now().toEpochMilli()))
    }
}
