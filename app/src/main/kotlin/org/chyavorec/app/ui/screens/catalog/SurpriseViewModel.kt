package org.chyavorec.app.ui.screens.catalog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.di.AppContainer
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.data.catalog.SurprisePicker
import org.chyavorec.data.catalog.SurpriseTastes
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.CatalogBook
import kotlin.random.Random

/** Скоро предложените заглавия — за цялата сесия (процеса), не само за екрана. */
internal object SurpriseSession {
    @Volatile var recent: List<String> = emptyList()
}

/**
 * „Изненадай ме“ — един опит: [round] се сменя при всеки („Още една“ пуска анимацията наново),
 * [pick] — предложението (`null` и [loading] = false — няма налична книга). [revealed] —
 * резултатът вече е бил показан (след завъртане или спиране на процеса): без ново разбъркване.
 */
data class SurpriseUi(
    val open: Boolean = false,
    val round: Int = 0,
    val loading: Boolean = false,
    val pick: CatalogBook? = null,
    val revealed: Boolean = false,
) {
    val empty: Boolean get() = open && !loading && pick == null
}

class SurpriseViewModel(
    /** Вкусът на читателя ([surpriseTastes]); тестовете подават свой. */
    private val tastes: suspend (CatalogSearcher) -> SurpriseTastes,
    private val random: Random = Random.Default,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    /** Отворената изненада и предложената книга (инв. №) — оцеляват след спиране на процеса. */
    private val saved: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {
    /** Предложението отпреди спирането на процеса — чака каталога ([restore]). */
    private var pendingInv: Long? = saved.get<Long>(KEY_INV)?.takeIf { saved.get<Boolean>(KEY_OPEN) == true }

    private val _ui = MutableStateFlow(
        if (pendingInv != null) SurpriseUi(open = true, round = saved.get<Int>(KEY_ROUND) ?: 1, loading = true, revealed = true)
        else SurpriseUi(),
    )
    val ui: StateFlow<SurpriseUi> = _ui.asStateFlow()
    private var job: Job? = null

    init {
        // Спряно по време на избора (без предложение) — не се възстановява.
        if (pendingInv == null) forget()
    }

    /** Ново предложение от [searcher] (търсачката на каталога; `null` — каталогът още не е зареден). */
    fun roll(searcher: CatalogSearcher?) {
        if (job?.isActive == true && pendingInv == null) return
        job?.cancel()
        pendingInv = null
        val round = _ui.value.round + 1
        _ui.value = SurpriseUi(open = true, round = round, loading = true)
        saved[KEY_OPEN] = true
        saved[KEY_ROUND] = round
        saved.remove<Long>(KEY_INV)
        job = viewModelScope.launch {
            val pick = if (searcher == null) null else withContext(dispatcher) {
                val t = try {
                    tastes(searcher)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    SurpriseTastes()
                }
                // Грешка на индекса (базата) — „няма предложение“, не срив.
                try {
                    SurprisePicker.pick(SurprisePicker.pool(searcher, t, random), t, SurpriseSession.recent, random)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }
            }
            pick?.let { SurpriseSession.recent = SurprisePicker.remember(SurpriseSession.recent, it) }
            _ui.update { if (it.round == round) it.copy(loading = false, pick = pick) else it }
            if (_ui.value.round == round && pick != null) saved[KEY_INV] = pick.inv
        }
    }

    /**
     * След спиране на процеса: същата книга по инвентарния номер (без ново теглене), щом
     * каталогът е зареден. Няма я вече или не е на рафта — изненадата се затваря.
     */
    fun restore(searcher: CatalogSearcher?) {
        val inv = pendingInv ?: return
        if (searcher == null || job?.isActive == true) return
        val round = _ui.value.round
        job = viewModelScope.launch {
            val book = withContext(dispatcher) {
                try {
                    searcher.book(inv)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }
            }
            pendingInv = null
            if (book == null || !book.available) {
                close()
            } else {
                _ui.update { if (it.round == round && it.open) it.copy(loading = false, pick = book) else it }
            }
        }
    }

    /** Сцената показа резултата на опит [round] — при ново показване (завъртане) без разбъркване. */
    fun markRevealed(round: Int) {
        _ui.update { if (it.round == round && it.open && !it.loading) it.copy(revealed = true) else it }
    }

    fun close() {
        job?.cancel()
        pendingInv = null
        _ui.update { SurpriseUi(round = it.round) }
        forget()
    }

    private fun forget() {
        saved[KEY_OPEN] = false
        saved.remove<Long>(KEY_INV)
    }

    private companion object {
        const val KEY_OPEN = "surprise.open"
        const val KEY_INV = "surprise.inv"
        const val KEY_ROUND = "surprise.round"
    }
}

/**
 * Вкусът — само от данни на телефона: „Искам да прочета“, историята на четене (ако е влязъл
 * читател, от кеша — без мрежа) и последните търсения. Нищо не излиза от телефона.
 */
suspend fun surpriseTastes(c: AppContainer, searcher: CatalogSearcher): SurpriseTastes {
    val wanted = c.database.wantToRead().all()
    val history = if (c.authRepository.state.value is AuthState.SignedIn) c.libraryRepository.cachedHistory()?.data.orEmpty() else emptyList()
    val searches = c.settings.recentSearches.first()
    val books = searcher.books((wanted.map { it.inv } + history.mapNotNull { it.inv }).distinct())
    return SurpriseTastes.of(
        wanted = wanted.mapNotNull { books[it.inv] },
        readBooks = history.mapNotNull { h -> h.inv?.let(books::get) },
        history = history,
        searches = searches,
    )
}
