package org.chyavorec.app.ui.screens.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.notifications.ReminderScheduler
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.toState
import org.chyavorec.core.AppClock
import org.chyavorec.data.repository.EventsRepository
import org.chyavorec.domain.model.Event
import java.time.LocalDate
import java.time.YearMonth

data class EventsUiState(
    val state: ScreenState<List<Event>> = ScreenState(),
    val categories: List<String> = emptyList(),
    val category: String? = null,
    val calendarMode: Boolean = false,
    val month: YearMonth,
    val selectedDate: LocalDate? = null,
    val visible: List<Event> = emptyList(),
    val daysWithEvents: Set<LocalDate> = emptySet(),
    val reminderIds: Set<String> = emptySet(),
    val today: LocalDate,
)

class EventsViewModel(
    private val repo: EventsRepository,
    private val reminders: ReminderScheduler,
    private val clock: AppClock,
) : ViewModel() {

    private val load = MutableStateFlow(ScreenState<List<Event>>())
    private val category = MutableStateFlow<String?>(null)
    private val calendarMode = MutableStateFlow(false)
    private val month = MutableStateFlow(YearMonth.from(clock.today()))
    private val selected = MutableStateFlow<LocalDate?>(null)

    private val controls = combine(category, calendarMode, month, selected) { r, c, m, s -> Controls(r, c, m, s) }
    private data class Controls(val category: String?, val calendar: Boolean, val month: YearMonth, val selected: LocalDate?)

    val ui: StateFlow<EventsUiState> = combine(load, controls, reminders.reminderIds) { s, c, rem ->
        val today = clock.today()
        val all = s.data.orEmpty()
        val filtered = all.filter { c.category == null || it.category == c.category }
        val dated = filtered.mapNotNull { e -> e.date?.let { runCatching { LocalDate.parse(it) }.getOrNull() }?.let { it to e } }
        val visible = if (c.calendar) {
            dated.filter { (d, _) -> YearMonth.from(d) == c.month && (c.selected == null || d == c.selected) }.map { it.second }
        } else {
            // Годишният календар винаги е „предстоящ“: датите вече са следващите настъпвания.
            dated.filter { !it.first.isBefore(today) }.sortedBy { it.first }.map { it.second }
        }
        EventsUiState(
            state = s, categories = all.mapNotNull { it.category }.groupingBy { it }.eachCount().entries.sortedByDescending { it.value }.map { it.key },
            category = c.category, calendarMode = c.calendar, month = c.month, selectedDate = c.selected,
            visible = visible, daysWithEvents = dated.map { it.first }.toSet(), reminderIds = rem.toSet(), today = today,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EventsUiState(month = YearMonth.from(clock.today()), today = clock.today()))

    init {
        viewModelScope.launch {
            repo.cachedRolled()?.let { c -> load.value = c.toState().copy(loading = true) }
            refresh(false)
        }
    }

    fun refresh(force: Boolean = true) = viewModelScope.launch {
        load.update { it.startRefresh() }
        load.update { it.with(repo.events(force)) }
    }

    fun setCategory(c: String?) { category.value = if (category.value == c) null else c }
    fun setCalendarMode(on: Boolean) { calendarMode.value = on; selected.value = null }
    fun shiftMonth(delta: Long) { month.update { it.plusMonths(delta) }; selected.value = null }
    fun select(date: LocalDate?) { selected.value = if (selected.value == date) null else date }
}

class EventDetailViewModel(
    private val id: String,
    private val repo: EventsRepository,
    private val reminders: ReminderScheduler,
) : ViewModel() {
    val event = MutableStateFlow<Event?>(null)
    val loaded = MutableStateFlow(false)
    val hasReminder: StateFlow<Boolean> = combine(reminders.reminderIds, event) { ids, _ -> id in ids }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    /** null — няма резултат; true/false — напомнянето е включено/изключено. */
    val reminderResult = MutableStateFlow<Boolean?>(null)

    init {
        viewModelScope.launch {
            event.value = repo.find(id)
            loaded.value = true
        }
    }

    fun canRemind(e: Event): Boolean = reminders.remindAt(e) != null

    fun toggleReminder() = viewModelScope.launch {
        val e = event.value ?: return@launch
        if (hasReminder.value) {
            reminders.cancel(e.id)
            reminderResult.value = false
        } else {
            reminderResult.value = reminders.schedule(e)
        }
    }
}
