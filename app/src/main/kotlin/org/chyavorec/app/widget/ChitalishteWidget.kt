package org.chyavorec.app.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import org.chyavorec.app.ChitalishteApp
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.app.data.local.resetOnCorruption
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.Outcome
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import java.time.LocalDate
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.glance.color.ColorProvider as DayNightColor

/**
 * Заемания с един и същ срок. Заглавия на книги НЕ се пазят и не се показват —
 * началният екран се вижда от всеки; само брой, дата и колко остава.
 */
private data class DueGroup(
    /** „15.10“ / „Oct 15“; празно — заемания без срок. */
    val date: String,
    /** „утре“, „след 3 дни“, „просрочена“ … */
    val countdown: String,
    val books: Int,
    /** null — без срок. */
    val status: DueStatus?,
)

/** Какво показва уиджетът — презарежда се при всяко [ChitalishteWidget.refresh]. */
private data class WidgetState(
    val eventTitle: String?,
    val eventDate: String?,
    /** Брой заети книги; 0 — няма вход или няма заемания. */
    val loansTotal: Int,
    /** Сроковете, най-близкият пръв (заеманията без срок — накрая). */
    val groups: List<DueGroup>,
)

private val EmptyWidgetState = WidgetState(null, null, 0, emptyList())

/**
 * Версия на данните в Glance състоянието на всяко копие. [ChitalishteWidget.refresh]
 * я сменя, а съдържанието я ползва като ключ за презареждане — така и докато
 * сесията на уиджета е жива (когато update само прекомпозира) се четат нови данни,
 * напр. след изход от профила.
 */
private val DataVersionKey = longPreferencesKey("data_version")

/**
 * Като PreferencesGlanceStateDefinition (същият файл), но повреден файл се заменя с празен,
 * вместо уиджетът да гърми при всяко опресняване. Ползва се навсякъде (и в
 * [updateAppWidgetState]) — Glance пази по едно хранилище за файл.
 */
private object WidgetStateDefinition : GlanceStateDefinition<Preferences> {
    override fun getLocation(context: Context, fileKey: String): File = context.preferencesDataStoreFile(fileKey)

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(corruptionHandler = resetOnCorruption("widget")) { getLocation(context, fileKey) }
}

/**
 * Размери (SizeMode.Responsive): малък 2×2 — един главен ред (следващ срок или събитие);
 * широк 4×2 — срокове + събитие + бутони; широк и висок — до 3 реда срокове.
 * [LocalSize] в съдържанието е избраният от тези размери, не действителният.
 */
private val SmallSize = DpSize(110.dp, 100.dp)
private val WideSize = DpSize(250.dp, 100.dp)
private val WideTallSize = DpSize(250.dp, 150.dp)

/**
 * Уиджет „Читалище Яворец“ за началния екран. За влезли читатели — сроковете за
 * връщане (брой книги, дата, „утре“/„след 3 дни“/„просрочена“; без заглавия), иначе —
 * следващото събитие. Широкият вариант показва и двете, с бутони „Сканирай“ и „Каталог“.
 * Чете само кешираните данни на приложението — мрежа се ползва единствено ако за
 * събитията няма кеш. Цветове: динамичните на системата (Android 12+), иначе фирмените.
 */
class ChitalishteWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = WidgetStateDefinition
    override val sizeMode: SizeMode = SizeMode.Responsive(setOf(SmallSize, WideSize, WideTallSize))

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val labels = Labels(
            title = context.getString(R.string.widget_label),
            nextEvent = context.getString(R.string.widget_next_event),
            noEvents = context.getString(R.string.widget_no_events),
            nextDue = context.getString(R.string.widget_next_due),
            scan = context.getString(R.string.shortcut_scan),
            catalog = context.getString(R.string.shortcut_catalog),
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
            GlanceTheme {
                val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicWidgetColors() else BrandWidgetColors
                val size = LocalSize.current
                if (size.width >= WideSize.width) {
                    WideContent(context, state, labels, colors, maxGroups = if (size.height >= WideTallSize.height) 3 else 2)
                } else {
                    SmallContent(context, state, labels, colors)
                }
            }
        }
    }

    private suspend fun load(context: Context): WidgetState {
        val app = context.applicationContext as? ChitalishteApp ?: return EmptyWidgetState
        val c = app.container

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

        return WidgetState(next?.title, eventDate, loans.size, dueGroups(context, loans, c.clock.today()))
    }

    companion object {
        /** Има ли поне едно копие на уиджета на началния екран (иначе фоновата задача не тегли данни за него). */
        suspend fun hasInstances(context: Context): Boolean = runCatching {
            GlanceAppWidgetManager(context).getGlanceIds(ChitalishteWidget::class.java).isNotEmpty()
        }.getOrDefault(false)

        /** Опреснява всички копия на уиджета; грешките се пренебрегват (уиджетът е допълнение). */
        suspend fun refresh(context: Context) {
            runCatching {
                val widget = ChitalishteWidget()
                val version = System.currentTimeMillis()
                GlanceAppWidgetManager(context).getGlanceIds(ChitalishteWidget::class.java).forEach { glanceId ->
                    runCatching {
                        updateAppWidgetState(context, WidgetStateDefinition, glanceId) { prefs ->
                            prefs.toMutablePreferences().apply { this[DataVersionKey] = version }
                        }
                        widget.update(context, glanceId)
                    }
                }
            }
        }
    }
}

/** Групира заеманията по срок (най-близкият пръв); без заглавия — само брой и дата. */
private fun dueGroups(context: Context, loans: List<Loan>, today: LocalDate): List<DueGroup> {
    if (loans.isEmpty()) return emptyList()
    val calc = LoanDueCalculator()
    val byDate = loans.mapNotNull { LoanDueCalculator.parse(it.dueOn) }.groupingBy { it }.eachCount().toSortedMap()
    val dated = byDate.map { (date, books) ->
        DueGroup(dayMonth(context, date), countdown(context, calc.daysLeft(date, today), books), books, calc.status(date, today))
    }
    val undated = loans.size - byDate.values.sum()
    return if (undated > 0) dated + DueGroup("", context.getString(R.string.widget_no_due_date), undated, null) else dated
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

/** „2 книги · 15.10“ (или само броя за заемания без срок). */
private fun groupLine(context: Context, g: DueGroup): String =
    if (g.date.isEmpty()) books(context, g.books) else books(context, g.books) + " · " + g.date

private data class Labels(
    val title: String,
    val nextEvent: String,
    val noEvents: String,
    val nextDue: String,
    val scan: String,
    val catalog: String,
)

/** Цветовете на уиджета — от GlanceTheme (динамични) или фирмените (ден/нощ). */
private class WidgetColors(
    /** null — фонът е drawable/widget_background (фирмен, със заоблени ъгли и под Android 12). */
    val background: ColorProvider?,
    val title: ColorProvider,
    val text: ColorProvider,
    val muted: ColorProvider,
    val eventDate: ColorProvider,
    val buttonBg: ColorProvider,
    val buttonText: ColorProvider,
    val tonalBg: ColorProvider,
    val tonalText: ColorProvider,
    val overdueBg: ColorProvider,
    val overdueText: ColorProvider,
    val soonBg: ColorProvider,
    val soonText: ColorProvider,
    val okBg: ColorProvider,
    val okText: ColorProvider,
)

/** Динамичните цветове на системата (Material You) — Android 12+. */
@Composable
private fun dynamicWidgetColors(): WidgetColors {
    val c = GlanceTheme.colors
    return WidgetColors(
        background = c.background,
        title = c.primary,
        text = c.onBackground,
        muted = c.onSurfaceVariant,
        eventDate = c.secondary,
        buttonBg = c.primary,
        buttonText = c.onPrimary,
        tonalBg = c.secondaryContainer,
        tonalText = c.onSecondaryContainer,
        overdueBg = c.errorContainer,
        overdueText = c.onErrorContainer,
        soonBg = c.tertiaryContainer,
        soonText = c.onTertiaryContainer,
        okBg = c.primaryContainer,
        okText = c.onPrimaryContainer,
    )
}

/**
 * Фирмената палитра (като темата на приложението) — под Android 12. Фонът и
 * прегледът в избора на уиджети ползват същите цветове от res/values/widget_colors.xml
 * (и values-night).
 */
private val BrandWidgetColors = WidgetColors(
    background = null,
    title = DayNightColor(day = Color(0xFF8B6914), night = Color(0xFFC9A84C)),
    text = DayNightColor(day = Color(0xFF1A1208), night = Color(0xFFF5F0E8)),
    muted = DayNightColor(day = Color(0xFF5A4D3D), night = Color(0xFFD7CBB6)),
    eventDate = DayNightColor(day = Color(0xFF6B1F2A), night = Color(0xFFE6A8B1)),
    buttonBg = DayNightColor(day = Color(0xFF8B6914), night = Color(0xFFC9A84C)),
    buttonText = DayNightColor(day = Color(0xFFFFFFFF), night = Color(0xFF1A1208)),
    tonalBg = DayNightColor(day = Color(0xFFE8D5A3), night = Color(0xFF4A3A12)),
    tonalText = DayNightColor(day = Color(0xFF1A1208), night = Color(0xFFF5F0E8)),
    overdueBg = DayNightColor(day = Color(0xFFF9DEDC), night = Color(0xFF5C1A1F)),
    overdueText = DayNightColor(day = Color(0xFFA3262F), night = Color(0xFFFF9C9C)),
    soonBg = DayNightColor(day = Color(0xFFF6E3C2), night = Color(0xFF4A3A12)),
    soonText = DayNightColor(day = Color(0xFF9A5B00), night = Color(0xFFF2C06B)),
    okBg = DayNightColor(day = Color(0xFFD5E8D9), night = Color(0xFF26432F)),
    okText = DayNightColor(day = Color(0xFF2F6B4A), night = Color(0xFF8FD1A8)),
)

private fun WidgetColors.chipBg(status: DueStatus?): ColorProvider = when (status) {
    DueStatus.OVERDUE -> overdueBg
    DueStatus.DUE_SOON -> soonBg
    else -> okBg
}

private fun WidgetColors.chipText(status: DueStatus?): ColorProvider = when (status) {
    DueStatus.OVERDUE -> overdueText
    DueStatus.DUE_SOON -> soonText
    else -> okText
}

/** Отваря приложението на дадена връзка — същият път като преките пътища (`chyavorec://app/<връзка>`). */
private fun linkAction(context: Context, link: String): Action =
    actionStartActivity(
        Intent(context, MainActivity::class.java).apply {
            action = MainActivity.ACTION_SHORTCUT
            // Различният адрес прави и PendingIntent-ите различни (не се сливат).
            data = Uri.parse("${MainActivity.SHORTCUT_SCHEME}://app/$link")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        },
    )

private fun openAppAction(context: Context): Action =
    actionStartActivity(
        Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        },
    )

/** Фонът и рамката на целия уиджет; докосване извън бутоните отваря приложението. */
private fun rootModifier(context: Context, colors: WidgetColors): GlanceModifier {
    val base = GlanceModifier.fillMaxSize().appWidgetBackground()
    val withBackground = colors.background?.let { base.background(it) }
        ?: base.background(ImageProvider(R.drawable.widget_background))
    return withBackground
        .cornerRadius(20.dp)
        .padding(horizontal = 14.dp, vertical = 10.dp)
        .clickable(openAppAction(context))
}

@Composable
private fun Header(title: String, colors: WidgetColors, modifier: GlanceModifier = GlanceModifier) {
    Text(
        title,
        modifier = modifier,
        style = TextStyle(color = colors.title, fontSize = 12.sp, fontWeight = FontWeight.Bold),
        maxLines = 1,
    )
}

/** Малкият вариант (2×2): следващият срок, а без заемания — следващото събитие. */
@Composable
private fun SmallContent(context: Context, state: WidgetState, labels: Labels, colors: WidgetColors) {
    Column(modifier = rootModifier(context, colors)) {
        Header(labels.title, colors)
        Spacer(GlanceModifier.height(6.dp))
        val nearest = state.groups.firstOrNull()
        if (state.loansTotal > 0 && nearest != null) {
            Column(GlanceModifier.fillMaxWidth().clickable(linkAction(context, "my/loans"))) {
                Text(labels.nextDue, style = TextStyle(color = colors.muted, fontSize = 11.sp), maxLines = 1)
                Text(
                    nearest.countdown,
                    style = TextStyle(color = colors.chipText(nearest.status), fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    maxLines = 1,
                )
                Text(groupLine(context, nearest), style = TextStyle(color = colors.text, fontSize = 12.sp), maxLines = 1)
                if (state.groups.size > 1) {
                    Text(
                        context.resources.getQuantityString(R.plurals.widget_books_to_return, state.loansTotal, state.loansTotal),
                        style = TextStyle(color = colors.muted, fontSize = 11.sp),
                        maxLines = 1,
                    )
                }
            }
        } else {
            EventBlock(context, state, labels, colors, titleSize = 15)
        }
    }
}

/** Широкият вариант (4×2): бутони, до [maxGroups] срока и следващото събитие. */
@Composable
private fun WideContent(context: Context, state: WidgetState, labels: Labels, colors: WidgetColors, maxGroups: Int) {
    Column(modifier = rootModifier(context, colors)) {
        Row(GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Header(labels.title, colors, GlanceModifier.defaultWeight())
            ActionChip(labels.scan, colors.buttonBg, colors.buttonText, linkAction(context, "scan"))
            Spacer(GlanceModifier.width(6.dp))
            ActionChip(labels.catalog, colors.tonalBg, colors.tonalText, linkAction(context, "catalog"))
        }
        Spacer(GlanceModifier.height(8.dp))
        Row(GlanceModifier.fillMaxWidth()) {
            if (state.loansTotal > 0 && state.groups.isNotEmpty()) {
                Column(GlanceModifier.defaultWeight().clickable(linkAction(context, "my/loans"))) {
                    Text(
                        context.resources.getQuantityString(R.plurals.widget_books_to_return, state.loansTotal, state.loansTotal),
                        style = TextStyle(color = colors.muted, fontSize = 11.sp),
                        maxLines = 1,
                    )
                    state.groups.take(maxGroups).forEach { g ->
                        Spacer(GlanceModifier.height(4.dp))
                        Row(GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            DueChip(g, colors)
                            Spacer(GlanceModifier.width(6.dp))
                            Text(
                                if (g.books > 1 || g.date.isEmpty()) groupLine(context, g) else g.date,
                                style = TextStyle(color = colors.text, fontSize = 12.sp),
                                maxLines = 1,
                            )
                        }
                    }
                }
                Spacer(GlanceModifier.width(12.dp))
            }
            Column(GlanceModifier.defaultWeight()) {
                EventBlock(context, state, labels, colors, titleSize = 14)
            }
        }
    }
}

/** Следващото събитие (докосване — към „Събития“) или „Няма предстоящи събития“. */
@Composable
private fun EventBlock(context: Context, state: WidgetState, labels: Labels, colors: WidgetColors, titleSize: Int) {
    val title = state.eventTitle
    if (title == null) {
        Text(labels.noEvents, style = TextStyle(color = colors.muted, fontSize = 13.sp), maxLines = 2)
        return
    }
    Column(GlanceModifier.fillMaxWidth().clickable(linkAction(context, "events"))) {
        Text(labels.nextEvent, style = TextStyle(color = colors.muted, fontSize = 11.sp), maxLines = 1)
        Text(
            title,
            style = TextStyle(color = colors.text, fontSize = titleSize.sp, fontWeight = FontWeight.Bold),
            maxLines = 2,
        )
        state.eventDate?.let {
            Text(it, style = TextStyle(color = colors.eventDate, fontSize = 12.sp), maxLines = 1)
        }
    }
}

/** Етикет „след 3 дни“ в цвета на срока (просрочен / наближава / има време). */
@Composable
private fun DueChip(g: DueGroup, colors: WidgetColors) {
    Box(
        GlanceModifier.background(colors.chipBg(g.status)).cornerRadius(10.dp).padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            g.countdown,
            style = TextStyle(color = colors.chipText(g.status), fontSize = 12.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
        )
    }
}

/** Малък бутон („Сканирай“, „Каталог“) — Box с текст, по-компактен от Glance Button. */
@Composable
private fun ActionChip(label: String, background: ColorProvider, content: ColorProvider, action: Action) {
    Box(
        GlanceModifier.background(background).cornerRadius(14.dp).padding(horizontal = 10.dp, vertical = 4.dp).clickable(action),
    ) {
        Text(label, style = TextStyle(color = content, fontSize = 12.sp, fontWeight = FontWeight.Bold), maxLines = 1)
    }
}

class ChitalishteWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ChitalishteWidget()
}
