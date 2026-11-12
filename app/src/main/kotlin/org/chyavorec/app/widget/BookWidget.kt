package org.chyavorec.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.edit
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.theme.ThemePalette
import org.chyavorec.data.catalog.BookOfTheDay
import org.chyavorec.domain.model.CatalogBook
import java.time.LocalDate
import kotlin.math.roundToInt

/** Книгата на деня в уиджета: само каквото се показва и каквото отваря книгата — публични данни от каталога. */
internal data class BookPick(
    val inv: Long,
    val title: String,
    val author: String,
    val udcSection: Int? = null,
    val docType: String = "",
)

internal fun CatalogBook.toPick() = BookPick(inv, title, author, udcSection, docType)

/**
 * Какво показва уиджетът „Книга на деня“. [book] `null` — каталогът още не е в кеша на
 * телефона (приятелска бележка, докосване — към приложението).
 */
internal data class BookWidgetState(
    val book: BookPick?,
    /** „Цветове от тапета“ са включени (Android 12+). */
    val dynamic: Boolean = false,
    /** Палитрата на приложението — от нея е цветът на генерираната корица. */
    val palette: ThemePalette = ThemePalette.YAVORETS,
)

/** Примерната книга на прегледа в избора на уиджети. */
internal fun previewBookState(context: Context) =
    BookWidgetState(BookPick(1, context.getString(R.string.widget_book_preview_title), context.getString(R.string.widget_book_preview_author), udcSection = 8))

private const val PREFS = "widget_book"
private const val KEY_DAY = "day"
private const val KEY_INV = "inv"

/**
 * Книгата на деня [today] от кеша на каталога (без мрежа). Избраната за деня се помни (ден + инв. №):
 * наличността се сменя със синхронизациите и без това изборът би се променял през деня. `null` —
 * няма каталог в кеша или е празен.
 */
internal suspend fun loadBookOfTheDay(c: AppContainer, prefs: SharedPreferences, today: LocalDate): CatalogBook? {
    val searcher = c.catalogRepository.lookupSearcher(network = false)?.data ?: return null
    val day = today.toString()
    if (prefs.getString(KEY_DAY, null) == day) {
        val inv = prefs.getLong(KEY_INV, 0L)
        if (inv > 0) searcher.book(inv)?.takeIf { it.title.isNotBlank() }?.let { return it }
    }
    val book = BookOfTheDay.pick(searcher, today) ?: return null
    prefs.edit {
        putString(KEY_DAY, day)
        putLong(KEY_INV, book.inv)
    }
    return book
}

internal fun bookPrefs(context: Context): SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

/** RemoteViews на уиджета „Книга на деня“ (layout/widget_book). Един размер — от 3×2 нагоре. */
internal object BookWidgetViews {
    /** Корицата в оформлението е 62×88 dp — битмапът е с тази ширина в пиксели на екрана. */
    private const val COVER_DP = 62f

    fun build(context: Context, state: BookWidgetState): RemoteViews {
        val v = RemoteViews(context.packageName, R.layout.widget_book)
        val dynamic = state.dynamic
        with(WidgetViews) {
            v.setInt(android.R.id.background, "setBackgroundResource", if (dynamic) R.drawable.widget_dyn_background else R.drawable.widget_background)
            v.tone(context, R.id.widget_title, WidgetViews.Tone.TITLE, dynamic)
            v.tone(context, R.id.widget_book_title, WidgetViews.Tone.TEXT, dynamic)
            v.tone(context, R.id.widget_book_author, WidgetViews.Tone.MUTED, dynamic)
            v.tone(context, R.id.widget_book_empty, WidgetViews.Tone.MUTED, dynamic)
        }
        v.setViewVisibility(R.id.widget_title, View.VISIBLE)
        v.setTextViewText(R.id.widget_title, context.getString(R.string.widget_book_label))
        val book = state.book
        if (book == null) {
            v.setViewVisibility(R.id.widget_book_cover, View.GONE)
            v.setViewVisibility(R.id.widget_book_title, View.GONE)
            v.setViewVisibility(R.id.widget_book_author, View.GONE)
            v.setViewVisibility(R.id.widget_book_empty, View.VISIBLE)
            v.setTextViewText(R.id.widget_book_empty, context.getString(R.string.widget_book_empty))
            // Още няма каталог — към каталога в приложението (там се зарежда).
            v.setOnClickPendingIntent(android.R.id.background, WidgetViews.link(context, "catalog"))
            return v
        }
        val density = context.resources.displayMetrics.density
        val dark = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        v.setViewVisibility(R.id.widget_book_cover, View.VISIBLE)
        v.setImageViewBitmap(R.id.widget_book_cover, BookWidgetCover.render(context, book, (COVER_DP * density).roundToInt(), state.palette, dark))
        v.setViewVisibility(R.id.widget_book_title, View.VISIBLE)
        v.setTextViewText(R.id.widget_book_title, book.title)
        // Оскъден запис — без автор: редът се скрива, а не остава празен.
        v.setViewVisibility(R.id.widget_book_author, if (book.author.isBlank()) View.GONE else View.VISIBLE)
        v.setTextViewText(R.id.widget_book_author, book.author)
        v.setViewVisibility(R.id.widget_book_empty, View.GONE)
        // Докосването — книгата в каталога (като динамичния пряк път).
        v.setOnClickPendingIntent(android.R.id.background, WidgetViews.link(context, DeepLinks.catalogBook(book.inv)))
        return v
    }
}

/**
 * Уиджет „Книга на деня“ — всеки ден друга книга от каталога. Опреснява се от общата фонова
 * работа на уиджетите ([WidgetUpdater]); смяната на деня се хваща от синхронизацията (~6 ч).
 */
class BookOfTheDayWidgetReceiver : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        inWidgetScope { WidgetUpdater.updateBooks(context, appWidgetIds, fallback = true) }
    }
}
