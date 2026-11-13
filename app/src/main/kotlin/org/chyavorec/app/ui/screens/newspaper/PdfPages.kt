package org.chyavorec.app.ui.screens.newspaper

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

/** Защо PDF не се чете — екранът показва приятелско съобщение и „Опитай отново“ / „Отвори във външно приложение“. */
enum class PdfProblem { MISSING, CORRUPT, PASSWORD, MEMORY }

class PdfReadException(val problem: PdfProblem, cause: Throwable? = null) : Exception(problem.name, cause)

/** Размерът на страницата в картинка: чиста функция — тества се без PdfRenderer. */
object PdfRenderPlan {
    /** Най-голямата страна на картинка (GPU текстурите ~4096). */
    const val MAX_SIDE = 4096

    /**
     * Размерът (ширина, височина) за страница [pageWidth]×[pageHeight] (точки): ширина [targetWidth], но
     * не над [MAX_SIDE] по страна и не над [maxPixels] общо. Много висока или широка страница се смалява,
     * а не се реже. `null` — страница без размер.
     */
    fun size(pageWidth: Int, pageHeight: Int, targetWidth: Int, maxPixels: Int): Pair<Int, Int>? {
        if (pageWidth <= 0 || pageHeight <= 0 || targetWidth <= 0 || maxPixels <= 0) return null
        val aspect = pageHeight.toDouble() / pageWidth
        var w = min(targetWidth, MAX_SIDE).toDouble()
        var h = w * aspect
        if (h > MAX_SIDE) { h = MAX_SIDE.toDouble(); w = h / aspect }
        if (w * h > maxPixels) {
            val k = sqrt(maxPixels / (w * h))
            w *= k; h *= k
        }
        return max(1, w.roundToInt()) to max(1, h.roundToInt())
    }
}

/** Страниците на отворен PDF; една страница наведнъж (PdfRenderer не е безопасен за нишки). */
interface PdfPages : Closeable {
    val pageCount: Int

    /** Страницата като картинка с ширина ~[targetWidthPx] (в границите на [maxPixels]); блокиращо — на IO. */
    suspend fun render(index: Int, targetWidthPx: Int, maxPixels: Int): Bitmap
}

/**
 * Платформеният [PdfRenderer] (API 21+, без зависимост). Повреден, защитен с парола или липсващ файл,
 * недостиг на памет и срутване на изчертаването стават [PdfReadException] — никога срутване на приложението.
 */
class PlatformPdfPages private constructor(
    private val descriptor: ParcelFileDescriptor,
    private val renderer: PdfRenderer,
    private val io: CoroutineDispatcher,
) : PdfPages {
    private val lock = Any()
    @Volatile private var closed = false

    override val pageCount: Int = renderer.pageCount

    override suspend fun render(index: Int, targetWidthPx: Int, maxPixels: Int): Bitmap {
        var produced: Bitmap? = null
        try {
            return withContext(io) {
                val bitmap = try {
                    renderOnce(index, targetWidthPx, maxPixels)
                } catch (_: OutOfMemoryError) {
                    // Втори опит — със 70 % от ширината (половината пиксели); после — приятелска грешка.
                    try {
                        renderOnce(index, targetWidthPx * 7 / 10, maxPixels / 2)
                    } catch (e: OutOfMemoryError) {
                        throw PdfReadException(PdfProblem.MEMORY, e)
                    }
                }
                produced = bitmap
                bitmap
            }
        } catch (e: CancellationException) {
            // Отменено, след като картинката вече е готова: withContext я хвърля — освобождава се тук.
            produced?.let { if (!it.isRecycled) it.recycle() }
            throw e
        }
    }

    private fun renderOnce(index: Int, width: Int, maxPixels: Int): Bitmap = synchronized(lock) {
        if (closed) throw PdfReadException(PdfProblem.MISSING)
        if (index !in 0 until pageCount) throw PdfReadException(PdfProblem.CORRUPT)
        val page = try {
            renderer.openPage(index)
        } catch (e: Exception) {
            throw PdfReadException(PdfProblem.CORRUPT, e)
        }
        try {
            val (w, h) = PdfRenderPlan.size(page.width, page.height, width, maxPixels) ?: throw PdfReadException(PdfProblem.CORRUPT)
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            try {
                // Страницата не рисува фон — без бяло изгрев „прозрачно“ става черно в платното.
                bitmap.eraseColor(Color.WHITE)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmap
            } catch (e: Throwable) {
                bitmap.recycle()
                throw e
            }
        } catch (e: PdfReadException) {
            throw e
        } catch (e: OutOfMemoryError) {
            throw e
        } catch (e: Exception) {
            throw PdfReadException(PdfProblem.CORRUPT, e)
        } finally {
            runCatching { page.close() }
        }
    }

    override fun close() {
        if (closed) return
        closed = true
        // Затваря се след текущото изчертаване (ключалката), но не на главната нишка.
        CoroutineScope(io).launch {
            synchronized(lock) {
                runCatching { renderer.close() }
                runCatching { descriptor.close() }
            }
        }
    }

    companion object {
        /** Отваря [file]; блокиращо — извън главната нишка. */
        suspend fun open(file: File, io: CoroutineDispatcher = Dispatchers.IO): PlatformPdfPages = withContext(io) {
            val descriptor = try {
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            } catch (e: IOException) {
                throw PdfReadException(PdfProblem.MISSING, e)
            }
            try {
                val renderer = PdfRenderer(descriptor)
                if (renderer.pageCount <= 0) {
                    renderer.close()
                    throw PdfReadException(PdfProblem.CORRUPT)
                }
                PlatformPdfPages(descriptor, renderer, io)
            } catch (e: PdfReadException) {
                runCatching { descriptor.close() }
                throw e
            } catch (e: SecurityException) {
                runCatching { descriptor.close() }
                throw PdfReadException(PdfProblem.PASSWORD, e)
            } catch (e: Throwable) {
                // IOException („не е PDF“), IllegalArgumentException, пропуснат native код — файлът не се чете.
                runCatching { descriptor.close() }
                if (e is OutOfMemoryError) throw PdfReadException(PdfProblem.MEMORY, e)
                if (e is Exception || e is LinkageError) throw PdfReadException(PdfProblem.CORRUPT, e)
                throw e
            }
        }
    }
}
