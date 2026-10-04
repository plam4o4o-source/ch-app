package org.chyavorec.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState

/** Какво показва уиджетът — изчислено веднъж в [ChitalishteWidget.provideGlance]. */
private data class WidgetState(
    val eventTitle: String?,
    val eventDate: String?,
    /** „Срок: <заглавие> — <дата>“ или null (няма вход / няма заемания). */
    val loanLine: String?,
)

/**
 * Уиджет „Читалище Яворец“ за началния екран: следващото събитие и (за влезли
 * читатели) най-близкият срок за връщане. Чете само кешираните данни на
 * приложението — мрежа се ползва единствено ако за събитията няма никакъв кеш.
 */
class ChitalishteWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = runCatching { load(context) }.getOrNull() ?: WidgetState(null, null, null)
        val labels = Labels(
            title = context.getString(R.string.widget_label),
            nextEvent = context.getString(R.string.widget_next_event),
            noEvents = context.getString(R.string.widget_no_events),
        )
        provideContent { WidgetContent(context, state, labels) }
    }

    private suspend fun load(context: Context): WidgetState {
        val app = context.applicationContext as? ChitalishteApp ?: return WidgetState(null, null, null)
        val c = app.container

        val events = runCatching {
            c.eventsRepository.cachedRolled()?.data
                ?: (c.eventsRepository.events(force = false) as? Outcome.Success)?.value?.data
        }.getOrNull().orEmpty()
        val next = c.eventsRepository.upcoming(events).firstOrNull()
        val eventDate = next?.let { e ->
            listOfNotNull(Formatters.date(context, e.date), e.time).joinToString(", ").ifBlank { null }
        }

        val loanLine = runCatching {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            if (c.authRepository.state.value !is AuthState.SignedIn) return@runCatching null
            val loan = c.libraryRepository.cachedLoans()?.data
                ?.filter { !it.dueOn.isNullOrBlank() }
                ?.minByOrNull { it.dueOn.orEmpty() }
                ?: return@runCatching null
            val date = Formatters.date(context, loan.dueOn) ?: loan.dueOn.orEmpty()
            context.getString(R.string.widget_due, loan.title, date)
        }.getOrNull()

        return WidgetState(next?.title, eventDate, loanLine)
    }

    companion object {
        /** Опреснява всички копия на уиджета; грешките се пренебрегват (уиджетът е допълнение). */
        suspend fun refresh(context: Context) {
            runCatching { ChitalishteWidget().updateAll(context) }
        }
    }
}

private data class Labels(val title: String, val nextEvent: String, val noEvents: String)

private val InkColor = Color(0xFF1A1208)
private val GoldDarkColor = Color(0xFF8B6914)
private val GoldColor = Color(0xFFC9A84C)
private val BurgundyColor = Color(0xFF6B1F2A)
private val MutedColor = Color(0xFF5A4D3D)

@Composable
private fun WidgetContent(context: Context, state: WidgetState, labels: Labels) {
    val openApp = Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_MAIN
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ImageProvider(R.drawable.widget_background))
            .cornerRadius(20.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(actionStartActivity(openApp)),
    ) {
        Text(
            labels.title,
            style = TextStyle(color = ColorProvider(GoldDarkColor), fontSize = 12.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
        )
        Spacer(GlanceModifier.height(4.dp))
        Spacer(GlanceModifier.fillMaxWidth().height(1.dp).background(GoldColor))
        Spacer(GlanceModifier.height(8.dp))

        if (state.eventTitle != null) {
            Text(
                labels.nextEvent,
                style = TextStyle(color = ColorProvider(MutedColor), fontSize = 11.sp),
                maxLines = 1,
            )
            Text(
                state.eventTitle,
                style = TextStyle(color = ColorProvider(InkColor), fontSize = 16.sp, fontWeight = FontWeight.Bold),
                maxLines = 2,
            )
            state.eventDate?.let {
                Text(it, style = TextStyle(color = ColorProvider(BurgundyColor), fontSize = 13.sp), maxLines = 1)
            }
        } else {
            Text(labels.noEvents, style = TextStyle(color = ColorProvider(MutedColor), fontSize = 14.sp), maxLines = 2)
        }

        state.loanLine?.let { line ->
            // Отделно действие: отваря направо „Моите книги“ (същият deep link като известията).
            val openLoans = Intent(context, MainActivity::class.java).apply {
                action = ACTION_OPEN_LOANS
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(Notifier.EXTRA_DEEP_LINK, "my/loans")
            }
            Spacer(GlanceModifier.height(8.dp))
            Text(
                line,
                modifier = GlanceModifier.fillMaxWidth().clickable(actionStartActivity(openLoans)),
                style = TextStyle(color = ColorProvider(BurgundyColor), fontSize = 13.sp, fontWeight = FontWeight.Medium),
                maxLines = 2,
            )
        }
    }
}

/** Различно действие от това на целия уиджет, за да не се слеят двата PendingIntent-а. */
private const val ACTION_OPEN_LOANS = "org.chyavorec.app.action.OPEN_LOANS"

class ChitalishteWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ChitalishteWidget()
}
