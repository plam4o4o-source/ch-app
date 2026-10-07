package org.chyavorec.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
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

/** Какво показва уиджетът — презарежда се при всяко [ChitalishteWidget.refresh]. */
private data class WidgetState(
    val eventTitle: String?,
    val eventDate: String?,
    /**
     * „N книги за връщане · най-близък срок <дата>“ или null (няма вход / няма
     * заемания). Заглавия на книги НЕ се показват — началният екран се вижда от всеки.
     */
    val loanLine: String?,
)

private val EmptyWidgetState = WidgetState(null, null, null)

/**
 * Версия на данните в Glance състоянието на всяко копие. [ChitalishteWidget.refresh]
 * я сменя, а съдържанието я ползва като ключ за презареждане — така и докато
 * сесията на уиджета е жива (когато update само прекомпозира) се четат нови данни,
 * напр. след изход от профила.
 */
private val DataVersionKey = longPreferencesKey("data_version")

/**
 * „Прелистване“ на уиджета: Glance няма анимации, затова в ниския размер се
 * показва само един ред, който при всяко опресняване се редува — събитие ↔ книги
 * за връщане. Флагът се обръща в [ChitalishteWidget.refresh].
 */
private val AlternateKey = booleanPreferencesKey("alternate")

/** Нисък (един ред) и висок (двата реда) размер — изборът е по действителната височина. */
private val SmallSize = DpSize(180.dp, 80.dp)
private val LargeSize = DpSize(180.dp, 130.dp)

/**
 * Уиджет „Читалище Яворец“ за началния екран: следващото събитие и (за влезли
 * читатели) колко книги са за връщане и най-близкият срок. Чете само кешираните
 * данни на приложението — мрежа се ползва единствено ако за събитията няма кеш.
 */
class ChitalishteWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    override val sizeMode: SizeMode = SizeMode.Responsive(setOf(SmallSize, LargeSize))

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val labels = Labels(
            title = context.getString(R.string.widget_label),
            nextEvent = context.getString(R.string.widget_next_event),
            noEvents = context.getString(R.string.widget_no_events),
        )
        // Първото зареждане — преди съдържанието, за да не се покаже празен уиджет.
        val initial = runCatching { load(context) }.getOrNull() ?: EmptyWidgetState
        provideContent {
            val version = currentState(DataVersionKey) ?: 0L
            // Версията, с която е заредено [initial] — за нея не се чете повторно.
            val loadedVersion = remember { version }
            val state by produceState(initial, version) {
                // При нова версия (refresh) — прочитане наново; при грешка остава предишното.
                if (version != loadedVersion) runCatching { load(context) }.getOrNull()?.let { value = it }
            }
            val alternate = currentState(AlternateKey) ?: false
            val tall = LocalSize.current.height >= LargeSize.height
            WidgetContent(context, state, labels, showEvent = tall || !alternate || state.loanLine == null, showLoans = tall || alternate)
        }
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
            val loans = c.libraryRepository.cachedLoans()?.data.orEmpty()
            if (loans.isEmpty()) return@runCatching null
            val nearest = loans.mapNotNull { it.dueOn?.takeIf(String::isNotBlank) }.minOrNull()
            val date = nearest?.let { Formatters.date(context, it) ?: it } ?: "—"
            context.resources.getQuantityString(R.plurals.widget_loans_due, loans.size, loans.size, date)
        }.getOrNull()

        return WidgetState(next?.title, eventDate, loanLine)
    }

    companion object {
        /** Опреснява всички копия на уиджета; грешките се пренебрегват (уиджетът е допълнение). */
        suspend fun refresh(context: Context) {
            runCatching {
                val widget = ChitalishteWidget()
                val version = System.currentTimeMillis()
                GlanceAppWidgetManager(context).getGlanceIds(ChitalishteWidget::class.java).forEach { glanceId ->
                    runCatching {
                        updateAppWidgetState(context, glanceId) { prefs ->
                            prefs[DataVersionKey] = version
                            prefs[AlternateKey] = !(prefs[AlternateKey] ?: false)
                        }
                        widget.update(context, glanceId)
                    }
                }
            }
        }
    }
}

private data class Labels(val title: String, val nextEvent: String, val noEvents: String)

private val InkColor = Color(0xFF1A1208)
private val GoldDarkColor = Color(0xFF8B6914)
private val GoldColor = Color(0xFFC9A84C)
private val BurgundyColor = Color(0xFF6B1F2A)
private val MutedColor = Color(0xFF5A4D3D)

/**
 * [showEvent]/[showLoans] — кои редове се виждат: във високия размер и двата, в
 * ниския — един, редуващ се при всяко опресняване (виж [AlternateKey]).
 */
@Composable
private fun WidgetContent(context: Context, state: WidgetState, labels: Labels, showEvent: Boolean = true, showLoans: Boolean = true) {
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

        if (showEvent && state.eventTitle != null) {
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
        } else if (showEvent) {
            Text(labels.noEvents, style = TextStyle(color = ColorProvider(MutedColor), fontSize = 14.sp), maxLines = 2)
        }

        state.loanLine?.takeIf { showLoans }?.let { line ->
            // Отделно действие: отваря направо „Моите книги“ (същият deep link като известията).
            val openLoans = Intent(context, MainActivity::class.java).apply {
                action = ACTION_OPEN_LOANS
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(Notifier.EXTRA_DEEP_LINK, "my/loans")
            }
            if (showEvent) Spacer(GlanceModifier.height(8.dp))
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
