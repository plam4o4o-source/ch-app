package org.chyavorec.app.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.os.Bundle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainerOwner
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Заемания с един и същ срок. Заглавия на книги НЕ се пазят и не се показват —
 * началният екран се вижда от всеки; само брой, дата и колко остава.
 */
internal data class DueGroup(
    /** „утре“, „след 3 дни“, „просрочена“, „без срок“ … */
    val countdown: String,
    /** „2 книги · 15.10“ (или само броя за заемания без срок) — малкият уиджет. */
    val line: String,
    /** До етикета в широкия уиджет: само датата за една книга, иначе [line]. */
    val short: String,
    /** null — без срок. */
    val status: DueStatus?,
)

/** Какво показва уиджетът — чете се наново при всяко опресняване ([WidgetUpdater]). */
internal data class WidgetState(
    val eventTitle: String?,
    val eventDate: String?,
    /** „2 книги за връщане“; null — няма вход или няма заемания. */
    val loansTotal: String?,
    /** Сроковете, най-близкият пръв (заеманията без срок — накрая). */
    val groups: List<DueGroup>,
) {
    val hasLoans: Boolean get() = loansTotal != null && groups.isNotEmpty()
}

internal val EmptyWidgetState = WidgetState(null, null, null, emptyList())

/**
 * Данните за уиджета: за влезли читатели — сроковете за връщане (брой книги, дата,
 * „утре“/„след 3 дни“/„просрочена“; без заглавия), и следващото събитие. Чете само
 * кешираните данни на приложението — мрежа се ползва единствено ако за събитията няма кеш.
 */
internal suspend fun loadWidgetState(context: Context): WidgetState {
    val c = (context.applicationContext as? AppContainerOwner)?.appContainer ?: return EmptyWidgetState

    val events = runCatching {
        c.eventsRepository.cachedRolled()?.data
            ?: (c.eventsRepository.events(force = false) as? Outcome.Success)?.value?.data
    }.getOrNull().orEmpty()
    val next = c.eventsRepository.upcoming(events).firstOrNull()
    val eventDate = next?.let { e ->
        listOfNotNull(Formatters.date(context, e.date), e.time).joinToString(", ").ifBlank { null }
    }

    val loans = runCatching {
        if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
        if (c.authRepository.state.value !is AuthState.SignedIn) return@runCatching emptyList<Loan>()
        c.libraryRepository.cachedLoans()?.data.orEmpty()
    }.getOrNull().orEmpty()

    val total = loans.size.takeIf { it > 0 }
        ?.let { context.resources.getQuantityString(R.plurals.widget_books_to_return, it, it) }
    return WidgetState(next?.title, eventDate, total, dueGroups(context, loans, c.clock.today()))
}

/** Примерните данни на прегледа в избора на уиджети — като layout/widget_preview. */
internal fun previewWidgetState(context: Context): WidgetState {
    val date = context.getString(R.string.widget_preview_date)
    return WidgetState(
        eventTitle = context.getString(R.string.widget_preview_event),
        eventDate = context.getString(R.string.widget_preview_event_date),
        loansTotal = context.getString(R.string.widget_preview_books),
        groups = listOf(DueGroup(context.getString(R.string.widget_preview_due), date, date, DueStatus.DUE_SOON)),
    )
}

/** Групира заеманията по срок (най-близкият пръв); без заглавия — само брой и дата. */
internal fun dueGroups(context: Context, loans: List<Loan>, today: LocalDate): List<DueGroup> {
    if (loans.isEmpty()) return emptyList()
    val calc = LoanDueCalculator()
    val byDate = loans.mapNotNull { LoanDueCalculator.parse(it.dueOn) }.groupingBy { it }.eachCount().toSortedMap()
    val dated = byDate.map { (date, books) ->
        val day = dayMonth(context, date)
        val line = books(context, books) + " · " + day
        DueGroup(countdown(context, calc.daysLeft(date, today), books), line, if (books > 1) line else day, calc.status(date, today))
    }
    val undated = loans.size - byDate.values.sum()
    if (undated == 0) return dated
    val line = books(context, undated)
    return dated + DueGroup(context.getString(R.string.widget_no_due_date), line, line, null)
}

/** „днес“, „утре“, „след N дни“, „просрочена/просрочени“ (по броя книги). */
private fun countdown(context: Context, days: Long, books: Int): String = when {
    days < 0 -> context.resources.getQuantityString(R.plurals.widget_due_overdue, books)
    days == 0L -> context.getString(R.string.widget_due_today)
    days == 1L -> context.getString(R.string.widget_due_tomorrow)
    else -> context.resources.getQuantityString(R.plurals.widget_due_in_days, days.toInt(), days.toInt())
}

/** Кратка дата без година — мястото в уиджета е малко: „15.10“ / „Oct 15“. */
private fun dayMonth(context: Context, d: LocalDate): String =
    if (Formatters.isBulgarian(context)) "%02d.%02d".format(Locale.ROOT, d.dayOfMonth, d.monthValue)
    else d.format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH))

private fun books(context: Context, n: Int): String =
    context.resources.getQuantityString(R.plurals.widget_books_count, n, n)

/**
 * Уиджет „Читалище Яворец“ за началния екран — обикновен AppWidgetProvider с RemoteViews
 * (оформленията layout/widget_*, [WidgetViews]). Името на класа е същото като на предишния
 * (Glance) приемник — вече добавените на началния екран копия продължават да работят.
 */
class ChitalishteWidgetReceiver : AppWidgetProvider() {

    /** Ново копие, обновяване на приложението, рестарт — данните се четат и се показват. */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        inBackground { WidgetUpdater.update(context, appWidgetIds, fallback = true) }
    }

    /** Под Android 12 оформлението се избира по новия размер (от 12 — системата, от картата с размери). */
    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            inBackground { WidgetUpdater.update(context, intArrayOf(appWidgetId), fallback = true) }
        }
    }

    /** Извън главната нишка; приемникът живее, докато [block] свърши (goAsync — до ~10 с). */
    private fun inBackground(block: suspend () -> Unit) {
        // Извън истинско съобщение (напр. в тестовете) — null.
        val pending: PendingResult? = goAsync()
        WidgetUpdater.scope.launch {
            try {
                block()
            } finally {
                pending?.finish()
            }
        }
    }
}
