package org.chyavorec.app.ui.screens.events

import org.chyavorec.app.ui.components.sharedBoundsKey
import org.chyavorec.app.ui.components.appFilterChipColors
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.outlined.Sell
import org.chyavorec.app.ui.components.animateEntrance
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import org.chyavorec.app.ui.components.LeafRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.DancingFigures
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.PressableCard
import org.chyavorec.app.ui.components.SiteImage
import org.chyavorec.app.ui.components.windowWidth
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.LocalThemeStyle
import org.chyavorec.app.ui.theme.paletteDateBadge
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.ui.screens.settings.NotificationAccess
import org.chyavorec.app.ui.screens.settings.showNotificationsBlocked
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.chyavorec.app.util.Intents
import org.chyavorec.core.AppError
import org.chyavorec.core.BulgarianDates
import org.chyavorec.domain.model.Event
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
private fun DateBadge(date: LocalDate?, modifier: Modifier = Modifier) {
    // Плътна значка (бордо с пергамент, като в уиджета), а не розовият secondaryContainer.
    val (badge, onBadge) = paletteDateBadge(LocalThemeStyle.current.palette, LocalExtendedColors.current.isDark)
    // Минимален, а не фиксиран размер: при едър системен шрифт значката расте с текста, вместо да го реже.
    Column(
        modifier.widthIn(min = 58.dp).heightIn(min = 58.dp).clip(MaterialTheme.shapes.small)
            .background(badge).padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (date != null) {
            Text(date.dayOfMonth.toString(), style = MaterialTheme.typography.headlineSmall, color = onBadge, maxLines = 1)
            Text(Formatters.monthShort(LocalContext.current, date), style = MaterialTheme.typography.labelSmall, color = onBadge, maxLines = 1)
        } else {
            Icon(Icons.Outlined.Event, contentDescription = null, tint = onBadge)
        }
    }
}

@Composable
fun EventCard(e: Event, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val date = e.date?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
    PressableCard(onClick = onClick, modifier = modifier) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            DateBadge(date)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(e.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.sharedBoundsKey("event-title-${e.id}"))
                val meta = listOfNotNull(e.time, e.place).joinToString(" · ").ifBlank { e.description }
                if (meta.isNotBlank()) {
                    Text(meta, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                e.category?.let {
                    Text(it.uppercase(), style = MaterialTheme.typography.labelSmall, color = LocalExtendedColors.current.gold, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

/** ViewModel на събитията — същият за сегмента „Календар“ и за стария самостоятелен екран. */
@Composable
fun eventsViewModel(): EventsViewModel = appViewModel { EventsViewModel(it.eventsRepository, it.reminders, it.clock) }

/** Действията над събитията: абонамент за календара и изглед календар/списък. */
@Composable
fun EventsActions(vm: EventsViewModel) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    var subscribe by rememberSaveable { mutableStateOf(false) }
    IconButton(onClick = { subscribe = true }) {
        Icon(Icons.Outlined.EventRepeat, contentDescription = stringResource(R.string.ics_subscribe))
    }
    IconButton(onClick = { vm.setCalendarMode(!ui.calendarMode) }) {
        Icon(
            if (ui.calendarMode) Icons.AutoMirrored.Outlined.ViewList else Icons.Outlined.CalendarMonth,
            contentDescription = stringResource(if (ui.calendarMode) R.string.events_list_view else R.string.events_calendar_view),
        )
    }
    if (subscribe) CalendarSubscribeSheet(onDismiss = { subscribe = false })
}

/**
 * Абонамент за календара на сайта (/events.ics): `webcal://` го отваря в приложението за
 * календар, което поддържа абонаменти. Google Календар на Android не може да се абонира
 * сам — затова „Копирай връзката“ и кратко обяснение как се добавя от уеб версията.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarSubscribeSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val url = Intents.eventsFeedUrl(LocalAppContainer.current.config.siteBaseUrl)
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(stringResource(R.string.ics_subscribe), style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
            Text(stringResource(R.string.ics_sheet_text), style = MaterialTheme.typography.bodyMedium)
            Button(onClick = { Intents.subscribeCalendar(context, url); onDismiss() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.EventRepeat, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.ics_subscribe_action))
            }
            Text(stringResource(R.string.ics_google_hint), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = { Intents.copy(context, context.getString(R.string.ics_link_label), url) }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.ContentCopy, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.ics_copy_link))
            }
            Text(
                url, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Старият самостоятелен екран „Събития“ — само за запазени стекове от по-стари версии;
 * събитията вече са сегментът „Календар“ на раздела „Новини и събития“.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = eventsViewModel()
    Scaffold(topBar = {
        BackTopBar(stringResource(R.string.events_title), onBack, actions = { EventsActions(vm) })
    }) { padding ->
        EventsContent(vm, navigate, Modifier.padding(padding))
    }
}

/** Събитията — списък или месечен календар; [header] стои над тях (сегментите на раздела). */
@Composable
fun EventsContent(vm: EventsViewModel, navigate: (String) -> Unit, modifier: Modifier = Modifier, header: @Composable () -> Unit = {}) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current
    LeafRefreshBox(ui.state.refreshing, onRefresh = { vm.refresh() }, modifier = modifier.fillMaxSize()) {
        Column {
            header()
            SyncBanner(ui.state.fromCache, ui.state.syncedAt, ui.state.refreshError)
            AnimatedContent(ui.calendarMode, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "mode") { calendar ->
                if (calendar) {
                    CalendarLayout(ui, vm, navigate)
                } else {
                    Column {
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(ui.category == null, onClick = { haptic.performHapticFeedback(HapticFeedbackType.SegmentTick); vm.setCategory(null) }, label = { Text(stringResource(R.string.filter_all)) }, colors = appFilterChipColors())
                            }
                            items(ui.categories) { c ->
                                FilterChip(ui.category == c, onClick = { haptic.performHapticFeedback(HapticFeedbackType.SegmentTick); vm.setCategory(c) }, label = { Text(c) }, colors = appFilterChipColors())
                            }
                        }
                        EventsState(ui, vm) { list ->
                            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                itemsIndexed(list, key = { _, e -> e.id }) { i, e ->
                                    EventCard(e, onClick = { navigate(Routes.event(e.id)) }, modifier = Modifier.fillMaxWidth().animateItem().animateEntrance(i))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Зареждане, грешка и празен списък; [emptyIsContent] — празният месец е съдържание (календарът остава). */
@Composable
private fun EventsState(ui: EventsUiState, vm: EventsViewModel, emptyIsContent: Boolean = false, content: @Composable (List<Event>) -> Unit) =
    StateContent(
        state = ui.state.map { ui.visible },
        onRetry = { vm.refresh() },
        isEmpty = { !emptyIsContent && it.isEmpty() },
        skeleton = { SkeletonList(withImage = false) },
        empty = {
            EmptyView(
                stringResource(R.string.events_empty), stringResource(R.string.events_empty_hint),
                icon = Icons.Outlined.EventAvailable, animation = { DancingFigures(Modifier.fillMaxSize()) },
            )
        },
        errorSubject = stringResource(R.string.subject_events),
        content = content,
    )

/** Най-широкият месечен календар — по-широк дава клетки, които не се събират на височина. */
private val CalendarMaxWidth = 480.dp

/**
 * Месечният календар и проявите под него. Преди календарът стоеше в непревъртаща се колона
 * с квадратни клетки по цялата ширина — хоризонтално шестте реда бяха ~660 dp, последните
 * седмици се рязаха, а списъкът за деня оставаше с нулева височина. Сега:
 * - телефон: календарът (до [CalendarMaxWidth]) и списъкът са в една превъртаща се колона;
 * - широк прозорец (от 840 dp): календарът отляво, списъкът отдясно — всеки се превърта сам.
 */
@Composable
private fun CalendarLayout(ui: EventsUiState, vm: EventsViewModel, navigate: (String) -> Unit) {
    val onPrev = { vm.shiftMonth(-1) }
    val onNext = { vm.shiftMonth(1) }
    if (windowWidth() >= 840.dp) {
        Row(Modifier.fillMaxSize()) {
            Column(Modifier.width(400.dp).fillMaxHeight().verticalScroll(rememberScrollState()).padding(bottom = 16.dp)) {
                MonthCalendar(ui, onPrev, onNext, vm::select)
            }
            Box(Modifier.weight(1f).fillMaxHeight()) {
                EventsState(ui, vm, emptyIsContent = true) { list ->
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (list.isEmpty()) item("empty") { MonthEmpty() }
                        itemsIndexed(list, key = { _, e -> e.id }) { i, e ->
                            EventCard(e, onClick = { navigate(Routes.event(e.id)) }, modifier = Modifier.fillMaxWidth().animateItem().animateEntrance(i))
                        }
                    }
                }
            }
        }
    } else {
        EventsState(ui, vm, emptyIsContent = true) { list ->
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item("calendar") {
                    MonthCalendar(ui, onPrev, onNext, vm::select, Modifier.widthIn(max = CalendarMaxWidth).fillMaxWidth())
                }
                if (list.isEmpty()) item("empty") { MonthEmpty() }
                itemsIndexed(list, key = { _, e -> e.id }) { i, e ->
                    EventCard(
                        e, onClick = { navigate(Routes.event(e.id)) },
                        modifier = Modifier.padding(horizontal = 16.dp).widthIn(max = 720.dp).fillMaxWidth().animateItem().animateEntrance(i),
                    )
                }
            }
        }
    }
}

/** Месецът е без прояви — кратък ред под календара (той остава, за да се смени месецът). */
@Composable
private fun MonthEmpty() {
    Text(
        stringResource(R.string.calendar_month_empty), style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun MonthCalendar(ui: EventsUiState, onPrev: () -> Unit, onNext: () -> Unit, onSelect: (LocalDate) -> Unit, modifier: Modifier = Modifier) {
    val month = ui.month
    val context = LocalContext.current
    val monthTitle = if (Formatters.isBulgarian(context)) BulgarianDates.monthName(month.monthValue)
    else month.month.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, java.util.Locale.ENGLISH)
    BoxWithConstraints(modifier.padding(horizontal = 12.dp)) {
        // Клетката е поне 48 dp (зоната за докосване), но не по-висока от 56 dp: на широк екран
        // редовете не растат с ширината. Кръгът е по-малкото от двете измерения.
        val cellHeight = (maxWidth / 7).coerceIn(48.dp, 56.dp)
        val circle = minOf(maxWidth / 7, cellHeight) - 4.dp
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onPrev) { Icon(Icons.Outlined.ChevronLeft, contentDescription = stringResource(R.string.calendar_prev)) }
                Text(
                    monthTitle.replaceFirstChar { it.uppercase() } + " " + month.year,
                    style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.weight(1f).semantics { heading() },
                )
                IconButton(onClick = onNext) { Icon(Icons.Outlined.ChevronRight, contentDescription = stringResource(R.string.calendar_next)) }
            }
            val dayNames = stringResource(R.string.calendar_weekdays).split(',')
            Row(Modifier.fillMaxWidth()) {
                dayNames.forEach { d ->
                    Text(d, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                }
            }
            val first = month.atDay(1)
            val offset = (first.dayOfWeek.value - DayOfWeek.MONDAY.value)
            val cells = offset + month.lengthOfMonth()
            val rows = (cells + 6) / 7
            val eventLabel = stringResource(R.string.calendar_has_events)
            val todayLabel = stringResource(R.string.calendar_today)
            val selectLabel = stringResource(R.string.calendar_show_day)
            for (r in 0 until rows) {
                Row(Modifier.fillMaxWidth()) {
                    for (c in 0 until 7) {
                        val dayNum = r * 7 + c - offset + 1
                        val inMonth = dayNum in 1..month.lengthOfMonth()
                        val date = if (inMonth) month.atDay(dayNum) else null
                        val has = date != null && date in ui.daysWithEvents
                        val isSel = date != null && date == ui.selectedDate
                        val isToday = date != null && date == ui.today
                        // Клетката (цялата колона, поне 48dp висока) е зоната за докосване;
                        // кръгът вътре е само визуален.
                        val cellModifier = if (date != null) {
                            Modifier.clip(CircleShape)
                                .clickable(enabled = has, onClickLabel = selectLabel) { onSelect(date) }
                                .semantics {
                                    selected = isSel
                                    contentDescription = listOfNotNull(
                                        Formatters.date(context, date),
                                        todayLabel.takeIf { isToday },
                                        eventLabel.takeIf { has },
                                    ).joinToString(", ")
                                }
                        } else Modifier
                        Box(Modifier.weight(1f).height(cellHeight).then(cellModifier), contentAlignment = Alignment.Center) {
                            if (date != null) {
                                Box(
                                    Modifier.size(circle).clip(CircleShape)
                                        .background(if (isSel) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent)
                                        .then(if (isToday && !isSel) Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape) else Modifier),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        dayNum.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    )
                                    if (has) {
                                        Box(Modifier.align(Alignment.BottomCenter).padding(bottom = 5.dp).size(5.dp)
                                            .background(if (isSel) MaterialTheme.colorScheme.onPrimary else LocalExtendedColors.current.gold, CircleShape))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventDetailScreen(id: String, onBack: () -> Unit, openLink: (String) -> Unit) {
    val vm = appViewModel(key = "event-$id") { EventDetailViewModel(id, it.eventsRepository, it.reminders) }
    val event by vm.event.collectAsStateWithLifecycle()
    val loaded by vm.loaded.collectAsStateWithLifecycle()
    val loadError by vm.error.collectAsStateWithLifecycle()
    val hasReminder by vm.hasReminder.collectAsStateWithLifecycle()
    val result by vm.reminderResult.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val zone = LocalAppContainer.current.clock.zone()
    val snackbar = remember { SnackbarHostState() }
    val msgOn = stringResource(R.string.reminder_set)
    val msgOff = stringResource(R.string.reminder_removed)
    val msgPast = stringResource(R.string.reminder_past)
    val msgBlocked = stringResource(R.string.reminder_blocked)
    val scope = rememberCoroutineScope()
    val resources = androidx.compose.ui.platform.LocalResources.current
    LaunchedEffect(result) {
        when (result) {
            // Кога точно ще дойде напомнянето.
            true -> snackbar.showSnackbar(
                vm.event.value?.let { vm.remindAt(it) }
                    ?.let { resources.getString(R.string.reminder_set_at, Formatters.dateTime(context, java.time.Instant.ofEpochMilli(it))) }
                    ?: msgOn,
            )
            false -> snackbar.showSnackbar(if (vm.event.value?.let { vm.canRemind(it) } == false) msgPast else msgOff)
            null -> Unit
        }
        vm.reminderResult.value = null
    }
    val permission = org.chyavorec.app.ui.screens.settings.rememberNotificationPermission()

    Scaffold(
        topBar = {
            BackTopBar("", onBack, actions = {
                event?.let { e ->
                    IconButton(onClick = { Intents.share(context, e.title, listOfNotNull(e.title, Formatters.date(context, e.date), e.time, e.place, e.sourceUrl).joinToString("\n")) }) {
                        Icon(Icons.Outlined.Share, contentDescription = stringResource(R.string.action_share))
                    }
                }
            })
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        val e = event
        if (e == null) {
            val error = loadError
            when {
                // „Няма го“ — само когато списъкът е зареден и събитието наистина липсва.
                loaded -> ErrorView(AppError.NotFound, onRetry = null, modifier = Modifier.padding(padding))
                // Без данни (без връзка, празен кеш) — „Опитай отново“; при връзка се опитва сам.
                error != null -> ErrorView(error, onRetry = { vm.retry() }, modifier = Modifier.padding(padding), subject = stringResource(R.string.subject_events))
                else -> SkeletonCards(Modifier.padding(padding))
            }
            return@Scaffold
        }
        Column(Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
            e.imageUrl?.let {
                SiteImage(it, width = windowWidth() - 32.dp, contentDescription = null, modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f).padding(horizontal = 16.dp).clip(MaterialTheme.shapes.large))
            }
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(e.title, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.sharedBoundsKey("event-title-${e.id}").semantics { heading() })
                DetailLine(Icons.Outlined.Event, Formatters.date(context, e.date) ?: stringResource(R.string.event_no_date))
                if (e.recurring) {
                    Text(stringResource(R.string.event_recurring), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                e.category?.let { DetailLine(Icons.Outlined.Sell, it) }
                e.time?.let { DetailLine(Icons.Outlined.AccessTime, it) }
                // Мястото отваря картата (в селото — „<място>, с. Яворец“).
                e.place?.let { place ->
                    DetailLine(
                        Icons.Outlined.Place, place,
                        onClickLabel = stringResource(R.string.event_open_map),
                        trailing = Icons.Outlined.Map,
                    ) { Intents.map(context, "$place, с. Яворец") }
                }
                e.organizer?.let { DetailLine(Icons.Outlined.Groups, it) }
                if (e.description.isNotBlank()) {
                    Text(e.description, style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(Modifier.height(4.dp))
                if (e.date != null) {
                    Button(onClick = { Intents.addToCalendar(context, e, zone) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.CalendarMonth, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.event_add_calendar))
                    }
                }
                if (vm.canRemind(e) || hasReminder) {
                    OutlinedButton(onClick = {
                        if (hasReminder) vm.toggleReminder()
                        // Без разрешение (или с изключени известия за събития) напомнянето не би се показало —
                        // не се задава, а се казва защо (с бутон към настройките).
                        else permission.requestWithResult { access ->
                            val events = org.chyavorec.app.notifications.Channel.EVENTS
                            val status = org.chyavorec.app.ui.screens.settings.notificationStatus(context, listOf(events))
                            if (access == NotificationAccess.GRANTED && status.allows(events)) vm.toggleReminder()
                            else scope.launch {
                                snackbar.showNotificationsBlocked(
                                    context,
                                    if (access == NotificationAccess.GRANTED) NotificationAccess.BLOCKED else access,
                                    msgBlocked,
                                    events.takeIf { !status.appBlocked },
                                )
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Icon(if (hasReminder) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsNone, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(if (hasReminder) R.string.event_remove_reminder else R.string.event_remind))
                    }
                }
                OutlinedButton(onClick = { openLink(e.sourceUrl) }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.AutoMirrored.Outlined.OpenInNew, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.event_source))
                }
            }
        }
    }
}

/** Ред с икона; с [onClick] — докосваем (поне 48 dp), с [trailing] икона и етикет за екранния четец. */
@Composable
private fun DetailLine(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClickLabel: String? = null,
    trailing: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    val clickable = if (onClick != null) {
        Modifier.fillMaxWidth().heightIn(min = 48.dp).clip(MaterialTheme.shapes.small)
            .clickable(onClickLabel = onClickLabel, role = Role.Button, onClick = onClick)
    } else Modifier
    Row(clickable, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = LocalExtendedColors.current.gold, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(
            text, style = MaterialTheme.typography.titleMedium,
            color = if (onClick != null) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Unspecified,
            modifier = Modifier.weight(1f, fill = false),
        )
        if (trailing != null) {
            Spacer(Modifier.width(8.dp))
            Icon(trailing, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
    }
}
