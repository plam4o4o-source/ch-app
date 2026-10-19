package org.chyavorec.app.ui.screens.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import org.chyavorec.app.ui.components.WheatRefreshBox
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.messages.MessageInbox
import org.chyavorec.app.ui.RefreshOnReconnect
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.ScreenState
import org.chyavorec.app.ui.components.SecureScreen
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.util.Formatters
import org.chyavorec.data.site.AppMessagesParser
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.MessageAudience
import org.chyavorec.domain.model.MessagePriority

class MessagesViewModel(private val center: MessageInbox) : ViewModel(), RefreshOnReconnect {
    private val _state = MutableStateFlow(ScreenState<List<AppMessage>>())
    val state: StateFlow<ScreenState<List<AppMessage>>> = _state.asStateFlow()

    /** Непрочетените към момента на зареждане — остават отбелязани „Ново“, докато екранът е отворен. */
    private val _fresh = MutableStateFlow<Set<String>>(emptySet())
    val fresh: StateFlow<Set<String>> = _fresh.asStateFlow()

    private val _member = MutableStateFlow(true)
    val member: StateFlow<Boolean> = _member.asStateFlow()

    /**
     * Екранът е пред очите на читателя (RESUMED). Опресняване при върната връзка идва и
     * за екрана, оставен под друг в стека или при приложение във фона — тогава новите
     * съобщения НЕ са прочетени (иначе значката и известията биха изчезнали, без да са видени).
     */
    private var visible = false

    init { refresh(false) }

    override val reconnectState: ScreenState<*> get() = _state.value
    override fun refreshOnReconnect() { refresh(false) }

    fun refresh(force: Boolean = true) = viewModelScope.launch {
        _state.update { it.startRefresh() }
        _member.value = center.isMember()
        val r = center.refresh(force)
        _state.update { it.with(r) }
        val list = _state.value.data.orEmpty()
        val read = center.readIds.first()
        _fresh.update { it + list.map { m -> m.id }.filter { id -> id !in read } }
        // Заредените, докато екранът се вижда, са прочетени; иначе — едва при показването му.
        if (visible) markShownRead()
    }

    /** От екрана: при ON_RESUME — true, при ON_PAUSE / излизане — false. */
    fun setVisible(value: Boolean) {
        visible = value
        if (value) markShownRead()
    }

    /** Показаните са прочетени: локално веднага, личните — и към библиотеката (повторен опит при неуспех). */
    private fun markShownRead() {
        val ids = _state.value.data.orEmpty().map { it.id }
        if (ids.isEmpty()) return
        viewModelScope.launch { center.markRead(ids) }
    }
}

/** [focusId] — съобщението, до което се превърта списъкът (от известие за него). */
@Composable
fun MessagesScreen(onBack: () -> Unit, openLink: (String) -> Unit, focusId: String? = null) {
    val vm = appViewModel { MessagesViewModel(it.messages) }
    val state by vm.state.collectAsStateWithLifecycle()
    val fresh by vm.fresh.collectAsStateWithLifecycle()
    val member by vm.member.collectAsStateWithLifecycle()
    // Лични съобщения от библиотеката — не в „Скорошни“ и в снимки на екрана (общите от сайта — може).
    if (state.data.orEmpty().any { it.personal }) SecureScreen()
    // Прочетени — само докато екранът е на преден план (не под друг екран и не във фона).
    LifecycleResumeEffect(vm) {
        vm.setVisible(true)
        onPauseOrDispose { vm.setVisible(false) }
    }
    Scaffold(topBar = { BackTopBar(stringResource(R.string.messages_title), onBack) }) { padding ->
        WheatRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(
                state = state,
                onRetry = { vm.refresh() },
                isEmpty = { it.isEmpty() },
                skeleton = { SkeletonList(withImage = false) },
                empty = {
                    EmptyView(
                        stringResource(R.string.messages_empty),
                        if (!member) stringResource(R.string.messages_members_hint) else null,
                        icon = Icons.Outlined.Campaign,
                    )
                },
                errorSubject = stringResource(R.string.messages_title),
            ) { list ->
                val listState = rememberLazyListState()
                var focused by rememberSaveable { mutableStateOf(false) }
                LaunchedEffect(list, focusId) {
                    if (focused || focusId == null) return@LaunchedEffect
                    val index = list.indexOfFirst { it.id == focusId }
                    // +1 — първият елемент е лентата за синхронизацията.
                    if (index >= 0) {
                        listState.animateScrollToItem(index + 1)
                        focused = true
                    }
                }
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item { SyncBanner(state.fromCache, state.syncedAt, state.refreshError) }
                    itemsIndexed(list, key = { _, m -> m.id }) { i, m ->
                        MessageCard(m, isNew = m.id in fresh, openLink = openLink, modifier = Modifier.animateEntrance(i))
                    }
                    if (!member) item {
                        Text(
                            stringResource(R.string.messages_members_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageCard(m: AppMessage, isNew: Boolean, openLink: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val high = m.priority == MessagePriority.HIGH
    val personalFallback = stringResource(R.string.messages_personal_title)
    val title = if (m.personal && m.title.isBlank()) personalFallback else m.title
    ElevatedCard(modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    when {
                        high -> Icons.Outlined.PriorityHigh
                        m.personal -> Icons.Outlined.Person
                        else -> Icons.Outlined.Campaign
                    },
                    null,
                    tint = if (high) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                )
                Text(
                    if (m.createdAt.isBlank()) "" else Formatters.dateTime(context, AppMessagesParser.createdAt(m)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                if (isNew) Badge { Text(stringResource(R.string.messages_new)) }
            }
            Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.semantics { heading() })
            if (m.body.isNotBlank()) SelectionContainer { Text(m.body, style = MaterialTheme.typography.bodyMedium) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (m.personal) {
                    // „Лично“ — съобщение само до този читател (от библиотеката, не от сайта).
                    AssistChip(
                        onClick = {}, enabled = false,
                        label = { Text(stringResource(R.string.messages_personal)) },
                        leadingIcon = { Icon(Icons.Outlined.Person, null, Modifier.padding(0.dp)) },
                        colors = AssistChipDefaults.assistChipColors(
                            disabledLabelColor = MaterialTheme.colorScheme.primary,
                            disabledLeadingIconContentColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
                if (m.audience == MessageAudience.MEMBERS) {
                    AssistChip(
                        onClick = {}, enabled = false,
                        label = { Text(stringResource(R.string.messages_members_only)) },
                        leadingIcon = { Icon(Icons.Outlined.Groups, null, Modifier.padding(0.dp)) },
                        colors = AssistChipDefaults.assistChipColors(
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLeadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
                m.expiresOn?.let {
                    Text(
                        stringResource(R.string.messages_valid_until, Formatters.shortDate(LocalContext.current, it) ?: it),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            m.url?.let { url ->
                TextButton(onClick = { openLink(url) }) {
                    Icon(Icons.AutoMirrored.Outlined.OpenInNew, null)
                    Text(stringResource(R.string.messages_open_link), Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
