package org.chyavorec.app.ui.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
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

@OptIn(FlowPreview::class)
class NewsListViewModel(
    private val repo: NewsRepository,
    private val favorites: FavoritesDao,
    private val clock: AppClock,
    /** Филтрирането върви извън главната нишка (тестовете подават свой диспечер). */
    private val work: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val load = MutableStateFlow(ScreenState<List<NewsArticle>>())
    private val filter = MutableStateFlow(NewsFilter())

    /** Нормализираният текст (заглавие + резюме) на новините — веднъж на списък, не при всеки клавиш. */
    private var normalized: Pair<List<NewsArticle>, List<String>>? = null

    private fun haystacks(list: List<NewsArticle>): List<String> {
        normalized?.let { (l, h) -> if (l === list) return h }
        return list.map { TextNormalizer.normalize(it.title + " " + it.summary) }.also { normalized = list to it }
    }

    // Списъкът се филтрира извън главната нишка, 150 ms след последния клавиш (категориите —
    // веднага). Самият филтър (текстът в полето за търсене) влиза в състоянието веднага.
    private val appliedFilter = filter.debounce { if (it.query.isBlank()) 0L else QUERY_DEBOUNCE_MS }

    private class Filtered(val visible: List<NewsArticle>, val favoriteIds: Set<String>)

    private val filtered = combine(load, appliedFilter, favorites.observeAll()) { s, applied, favs ->
        val source: List<NewsArticle>
        val texts: List<String>?
        if (applied.favoritesOnly) {
            // Любимите работят и офлайн — пазят се в Room.
            source = favs.map { NewsArticle(it.id, it.title, it.url, it.publishedAtMillis, it.summary, it.imageUrl, it.category) }
            texts = null
        } else {
            source = s.data.orEmpty()
            texts = haystacks(source)
        }
        val tokens = TextNormalizer.tokens(applied.query)
        Filtered(
            visible = source.filterIndexed { i, a ->
                (applied.category == null || a.category == applied.category) &&
                    (tokens.isEmpty() || (texts?.get(i) ?: TextNormalizer.normalize(a.title + " " + a.summary)).let { t -> tokens.all { t.contains(it) } })
            },
            favoriteIds = favs.mapTo(HashSet()) { it.id },
        )
    }.flowOn(work)

    val ui: StateFlow<NewsUiState> = combine(load, filter, filtered) { s, f, r ->
        NewsUiState(
            state = s,
            filter = f,
            categories = s.data.orEmpty().mapNotNull { it.category }.distinct(),
            favoriteIds = r.favoriteIds,
            visible = r.visible,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NewsUiState())

    private companion object {
        const val QUERY_DEBOUNCE_MS = 150L
    }

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
