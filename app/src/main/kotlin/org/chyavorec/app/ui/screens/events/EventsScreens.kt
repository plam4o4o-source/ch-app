package org.chyavorec.app.ui.screens.events

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
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
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.PressableCard
import org.chyavorec.app.ui.components.RemoteImage
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.util.Intents
import org.chyavorec.core.AppError
import org.chyavorec.core.BulgarianDates
import org.chyavorec.domain.model.Event
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
private fun DateBadge(date: LocalDate?, modifier: Modifier = Modifier) {
    Column(
        modifier.size(58.dp).clip(MaterialTheme.shapes.small).background(MaterialTheme.colorScheme.secondaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (date != null) {
            Text(date.dayOfMonth.toString(), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(BulgarianDates.monthName(date.monthValue).take(3).uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
        } else {
            Icon(Icons.Outlined.Event, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
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
                Text(e.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel { EventsViewModel(it.eventsRepository, it.reminders, it.clock) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current
    Scaffold(topBar = {
        BackTopBar(stringResource(R.string.events_title), onBack, actions = {
            IconButton(onClick = { vm.setCalendarMode(!ui.calendarMode) }) {
                Icon(
                    if (ui.calendarMode) Icons.AutoMirrored.Outlined.ViewList else Icons.Outlined.CalendarMonth,
                    contentDescription = stringResource(if (ui.calendarMode) R.string.events_list_view else R.string.events_calendar_view),
                )
            }
        })
    }) { padding ->
        PullToRefreshBox(ui.state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            Column {
                SyncBanner(ui.state.fromCache, ui.state.syncedAt, ui.state.refreshError)
                AnimatedContent(ui.calendarMode, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "mode") { calendar ->
                    if (calendar) {
                        MonthCalendar(ui, onPrev = { vm.shiftMonth(-1) }, onNext = { vm.shiftMonth(1) }, onSelect = vm::select)
                    } else {
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(ui.category == null, onClick = { haptic.performHapticFeedback(HapticFeedbackType.SegmentTick); vm.setCategory(null) }, label = { Text(stringResource(R.string.filter_all)) })
                            }
                            items(ui.categories) { c ->
                                FilterChip(ui.category == c, onClick = { haptic.performHapticFeedback(HapticFeedbackType.SegmentTick); vm.setCategory(c) }, label = { Text(c) })
                            }
                        }
                    }
                }
                StateContent(
                    state = ui.state.map { ui.visible },
                    onRetry = { vm.refresh() },
                    isEmpty = { it.isEmpty() },
                    skeleton = { SkeletonList(withImage = false) },
                    empty = { EmptyView(stringResource(R.string.events_empty), stringResource(R.string.events_empty_hint), icon = Icons.Outlined.EventAvailable) },
                    errorSubject = stringResource(R.string.subject_events),
                ) { list ->
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

@Composable
private fun MonthCalendar(ui: EventsUiState, onPrev: () -> Unit, onNext: () -> Unit, onSelect: (LocalDate) -> Unit) {
    val month = ui.month
    val context = LocalContext.current
    val monthTitle = if (Formatters.isBulgarian(context)) BulgarianDates.monthName(month.monthValue)
    else month.month.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, java.util.Locale.ENGLISH)
    Column(Modifier.padding(horizontal = 12.dp)) {
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
                    Box(Modifier.weight(1f).heightIn(min = 48.dp).aspectRatio(1f).then(cellModifier), contentAlignment = Alignment.Center) {
                        if (date != null) {
                            Box(
                                Modifier.fillMaxSize().padding(2.dp).clip(CircleShape)
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

@Composable
fun EventDetailScreen(id: String, onBack: () -> Unit, openLink: (String) -> Unit) {
    val vm = appViewModel(key = "event-$id") { EventDetailViewModel(id, it.eventsRepository, it.reminders) }
    val event by vm.event.collectAsStateWithLifecycle()
    val loaded by vm.loaded.collectAsStateWithLifecycle()
    val hasReminder by vm.hasReminder.collectAsStateWithLifecycle()
    val result by vm.reminderResult.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val zone = LocalAppContainer.current.clock.zone()
    val snackbar = remember { SnackbarHostState() }
    val msgOn = stringResource(R.string.reminder_set)
    val msgOff = stringResource(R.string.reminder_removed)
    val msgPast = stringResource(R.string.reminder_past)
    LaunchedEffect(result) {
        when (result) {
            true -> snackbar.showSnackbar(if (hasReminder) msgOn else msgOn)
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
            if (loaded) ErrorView(AppError.NotFound, onRetry = null, modifier = Modifier.padding(padding))
            return@Scaffold
        }
        Column(Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
            e.imageUrl?.let {
                RemoteImage(it, null, Modifier.fillMaxWidth().aspectRatio(16f / 9f).padding(horizontal = 16.dp).clip(MaterialTheme.shapes.large))
            }
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(e.title, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.semantics { heading() })
                DetailLine(Icons.Outlined.Event, Formatters.date(context, e.date) ?: stringResource(R.string.event_no_date))
                if (e.recurring) {
                    Text(stringResource(R.string.event_recurring), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                e.category?.let { DetailLine(Icons.Outlined.Sell, it) }
                e.time?.let { DetailLine(Icons.Outlined.AccessTime, it) }
                e.place?.let { DetailLine(Icons.Outlined.Place, it) }
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
                    OutlinedButton(onClick = { permission.request { vm.toggleReminder() } }, modifier = Modifier.fillMaxWidth()) {
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

@Composable
private fun DetailLine(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = LocalExtendedColors.current.gold, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}
