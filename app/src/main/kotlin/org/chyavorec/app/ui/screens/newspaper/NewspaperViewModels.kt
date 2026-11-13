package org.chyavorec.app.ui.screens.newspaper

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.FileDownloader
import org.chyavorec.data.repository.SiteRepository
import org.chyavorec.domain.model.DocumentKind
import org.chyavorec.domain.model.SiteDocument
import java.io.File

/** Списъкът със статиите от юбилейния вестник (PDF от „Документи и архив“ → „Публикации“). */
class NewspaperListViewModel(private val repo: SiteRepository) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<List<SiteDocument>>())
    val state: StateFlow<ScreenState<List<SiteDocument>>> = _state.asStateFlow()

    init { refresh(false) }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.startRefresh() }
            _state.update { s -> s.with(repo.documents(force).let { r -> if (r is Outcome.Success) Outcome.Success(r.value.map { list -> articles(list) }) else r }) }
        }
    }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() = refresh(false)

    companion object {
        /** Само публикациите (PDF-и) на нашия сайт — само те се четат във вътрешния четец. */
        fun articles(all: List<SiteDocument>): List<SiteDocument> =
            all.filter { it.kind == DocumentKind.PUBLICATION && it.url.substringBefore('?').endsWith(".pdf", ignoreCase = true) }
    }
}

/** Състоянието на четеца: изтегляне → готов за четене → или приятелска грешка. */
sealed interface ReaderState {
    data class Loading(val progress: Float?) : ReaderState
    data class Ready(val pageCount: Int) : ReaderState
    data class Failed(val reason: ReaderProblem) : ReaderState
}

/** Защо статията не се показва — за всяка причина екранът има свой текст и бутони. */
enum class ReaderProblem { NETWORK, SERVER, CORRUPT, PASSWORD, MEMORY }

/** Страница като картинка: [widthPx] е ширината, с която е нарисувана (за решаване дали да се прерисува по-едро). */
class RenderedPage(val index: Int, val image: ImageBitmap, val widthPx: Int, val heightPx: Int, internal val bitmap: Bitmap) {
    val aspect: Float get() = heightPx.toFloat() / widthPx
}

/**
 * Четецът на една статия: свалянето (и кешът — вече свален файл се чете офлайн), отварянето с
 * [PdfPages] и изчертаването на една страница наведнъж (≈ ширина на екрана, ограничена по пиксели).
 * Предишната картинка се освобождава малко след смяната — не докато още се рисува.
 */
class NewspaperReaderViewModel(
    private val url: String,
    private val title: String,
    /** Сваля/намира файла; `progress` — 0..1 или `null`. Вече свален файл се връща без мрежа. */
    private val fetch: suspend (progress: (Float?) -> Unit) -> Outcome<File>,
    private val open: suspend (File) -> PdfPages = { PlatformPdfPages.open(it) },
    /** Изтрива повредения файл, за да се свали наново. */
    private val discard: (File) -> Unit = { it.delete() },
) : ViewModel() {
    private val _state = MutableStateFlow<ReaderState>(ReaderState.Loading(null))
    val state: StateFlow<ReaderState> = _state.asStateFlow()
    private val _page = MutableStateFlow<RenderedPage?>(null)
    val page: StateFlow<RenderedPage?> = _page.asStateFlow()
    /** Страницата не се нарисува (памет, повреда в средата на файла) — четецът остава, страницата има „Опитай пак“. */
    private val _pageFailed = MutableStateFlow(false)
    val pageFailed: StateFlow<Boolean> = _pageFailed.asStateFlow()

    /** Свалият файл — за „Отвори във външно приложение“ (`null`, докато не е свален). */
    @Volatile var file: File? = null
        private set
    private var pages: PdfPages? = null
    private var loadJob: Job? = null
    private var renderJob: Job? = null
    private var lastRequest: Triple<Int, Int, Boolean>? = null
    /** Последната нарисувана заявка (страница, поискана ширина) и тази, която тече. */
    private var served: Pair<Int, Int>? = null
    private var inFlight: Pair<Int, Int>? = null
    /** Сменените картинки, чакащи освобождаване (само главната нишка) — [onCleared] ги освобождава всичките. */
    private val retiring = HashSet<Bitmap>()

    init { load() }

    fun retry() {
        if (_state.value is ReaderState.Loading) return
        load()
    }

    private fun load() {
        loadJob?.cancel()
        _state.value = ReaderState.Loading(null)
        _pageFailed.value = false
        loadJob = viewModelScope.launch {
            try {
                var redownloaded = false
                while (true) {
                    val r = fetch { p -> _state.value = ReaderState.Loading(p) }
                    if (r is Outcome.Failure) {
                        _state.value = ReaderState.Failed(problemOf(r.error))
                        return@launch
                    }
                    val f = (r as Outcome.Success).value
                    file = f
                    try {
                        val p = open(f)
                        closePages()
                        pages = p
                        served = null
                        _state.value = ReaderState.Ready(p.pageCount)
                        lastRequest?.let { (i, w, boost) -> render(i, w, boost, p) }
                        return@launch
                    } catch (e: PdfReadException) {
                        // Повреден кеширан файл — веднъж се изтегля наново; после — приятелска грешка.
                        if (e.problem == PdfProblem.CORRUPT && !redownloaded) {
                            redownloaded = true
                            discard(f)
                            continue
                        }
                        _state.value = ReaderState.Failed(
                            when (e.problem) {
                                PdfProblem.PASSWORD -> ReaderProblem.PASSWORD
                                PdfProblem.MEMORY -> ReaderProblem.MEMORY
                                else -> ReaderProblem.CORRUPT
                            },
                        )
                        return@launch
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _state.value = ReaderState.Failed(ReaderProblem.CORRUPT)
            }
        }
    }

    /**
     * Нарисува страницата [index] (0-based) с ширина ~[widthPx]; [boost] — по-едро (при увеличение).
     * Същата заявка не се повтаря; нова отменя неприключилата.
     */
    fun render(index: Int, widthPx: Int, boost: Boolean = false) {
        lastRequest = Triple(index, widthPx, boost)
        val p = pages ?: return
        render(index, widthPx, boost, p)
    }

    private fun render(index: Int, widthPx: Int, boost: Boolean, p: PdfPages) {
        // Вече имаме същата страница поне толкова едро (поисканата ширина, не постигнатата — тя може да е ограничена по пиксели).
        val served = served
        if (!_pageFailed.value && served != null && served.first == index && served.second >= widthPx * 0.95f) return
        if (inFlight?.first == index && (inFlight?.second ?: 0) >= widthPx * 0.95f && renderJob?.isActive == true) return
        renderJob?.cancel()
        inFlight = index to widthPx
        renderJob = viewModelScope.launch {
            try {
                val maxPixels = if (boost) BOOST_PIXELS else BASE_PIXELS
                val bitmap = p.render(index.coerceIn(0, p.pageCount - 1), widthPx, maxPixels)
                // Заявката е отменена, докато е рисувала — картинката никому не трябва (15–40 MB за GC).
                if (!isActive) {
                    bitmap.recycle()
                    return@launch
                }
                val next = RenderedPage(index, bitmap.asImageBitmap(), bitmap.width, bitmap.height, bitmap)
                val old = _page.value
                _page.value = next
                this@NewspaperReaderViewModel.served = index to widthPx
                _pageFailed.value = false
                // Старата картинка още може да е в кадъра, който се рисува — освобождава се малко по-късно.
                if (old != null) retire(old.bitmap)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                // PdfReadException и всичко друго: страницата получава „Опитай пак“, четецът остава.
                _pageFailed.value = true
            } finally {
                if (inFlight?.first == index) inFlight = null
            }
        }
    }

    /**
     * Освобождава [bitmap] след [RECYCLE_DELAY_MS]. Закъснението е във [viewModelScope], не в заявката за
     * рисуване: следващата заявка (бързо прелистване, прерисуване по-едро) го отменя и картинката остава за GC.
     */
    private fun retire(bitmap: Bitmap) {
        retiring += bitmap
        viewModelScope.launch {
            delay(RECYCLE_DELAY_MS)
            release(bitmap)
        }
    }

    private fun release(bitmap: Bitmap) {
        retiring -= bitmap
        if (!bitmap.isRecycled) bitmap.recycle()
    }

    /** „Опитай пак“ на страницата, която не се е нарисувала. */
    fun retryPage() {
        val (i, w, boost) = lastRequest ?: return
        _pageFailed.value = false
        lastRequest = null
        served = null
        render(i, w, boost)
    }

    private fun problemOf(e: AppError): ReaderProblem = when (e) {
        is AppError.Server, AppError.NotFound -> ReaderProblem.SERVER
        is AppError.Parse -> ReaderProblem.CORRUPT
        else -> ReaderProblem.NETWORK
    }

    private fun closePages() {
        pages?.close()
        pages = null
    }

    override fun onCleared() {
        loadJob?.cancel()
        renderJob?.cancel()
        closePages()
        _page.value?.bitmap?.let { if (!it.isRecycled) it.recycle() }
        retiring.toList().forEach(::release)
    }

    companion object {
        /** Пиксели за една страница: нормално ~6 MP (24 MB), при увеличение ~10 MP. */
        const val BASE_PIXELS = 6_000_000
        const val BOOST_PIXELS = 10_000_000
        const val RECYCLE_DELAY_MS = 600L
        /** Колко пъти ширината на екрана е нормалната картинка (запас за малко увеличение). */
        const val BASE_FACTOR = 1.5f
        /** Най-голямата ширина на картинка при увеличение. */
        const val MAX_BOOST_WIDTH = 3600
    }
}
