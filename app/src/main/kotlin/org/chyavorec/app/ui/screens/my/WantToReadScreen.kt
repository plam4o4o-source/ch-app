package org.chyavorec.app.ui.screens.my

import android.content.res.Resources
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.data.local.WantToReadEntity
import org.chyavorec.app.data.local.dbWrite
import org.chyavorec.app.data.local.orOnDbError
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.notifications.Channel
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.AvailableMark
import org.chyavorec.app.ui.components.BookStatusPill
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.PersonalCovers
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.components.sharedElementKey
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.settings.NotificationAccess
import org.chyavorec.app.ui.screens.settings.notificationStatus
import org.chyavorec.app.ui.screens.settings.rememberNotificationPermission
import org.chyavorec.app.ui.screens.settings.showNotificationsBlocked
import org.chyavorec.app.ui.theme.AppMotion
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.Synced
import org.chyavorec.core.logFailure
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.CatalogBook

/** Книга от списъка и записът ѝ в текущия каталог (`null` — няма я в него или каталогът още не е прочетен). */
@Immutable
data class WantToReadRow(val saved: WantToReadEntity, val book: CatalogBook?) {
    /** За корицата и при липса в каталога — от запазеното заглавие и автор. */
    val shown: CatalogBook get() = book ?: CatalogBook(saved.inv, saved.author, saved.title, available = saved.lastKnownAvailable)
}

data class WantToReadUi(
    val loaded: Boolean = false,
    val rows: List<WantToReadRow> = emptyList(),
    /** Датата на каталога, по който е наличността; `null` — каталогът още не е прочетен (наличността е последно известната). */
    val generatedOn: String? = null,
)

/**
 * „Искам да прочета“ — списъкът е само на устройството ([org.chyavorec.app.data.local.WantToReadDao]);
 * наличността е от текущия каталог (през индекса в базата, без да се разчита целият каталог).
 * При отваряне каталогът се опреснява (условна заявка, на 30 мин. — без мрежа) — новият
 * каталог сам дава известията „вече е на рафта“ ([org.chyavorec.app.notifications.WantToReadAlerts]).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WantToReadViewModel(private val c: AppContainer) : ViewModel() {
    private val dao get() = c.database.wantToRead()
    private val searcher = MutableStateFlow<Synced<CatalogSearcher>?>(null)

    val ui: StateFlow<WantToReadUi> = combine(dao.observeAll().orOnDbError(emptyList()), searcher) { saved, s -> saved to s }
        .mapLatest { (saved, s) ->
            val books = s?.data?.let { cat -> runCatching { cat.books(saved.map { it.inv }) }.getOrNull() }.orEmpty()
            WantToReadUi(loaded = true, rows = saved.map { WantToReadRow(it, books[it.inv]) }, generatedOn = s?.data?.generatedOn)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WantToReadUi())

    init {
        viewModelScope.launch {
            runCatching { c.catalogRepository.lookupSearcher(network = true) }.logFailure("want-to-read.lookup").getOrNull()
                ?.let { if (searcher.value == null) searcher.value = it }
            runCatching { c.catalogRepository.refreshHomeSummary(force = false) }.logFailure("want-to-read.refresh")
        }
        // Нов каталог (опресняването по-горе или друг екран) — наличността наново.
        viewModelScope.launch { c.catalogRepository.searcherUpdates.collect { searcher.value = it } }
    }

    fun remove(item: WantToReadEntity) = viewModelScope.launch { dbWrite { dao.delete(item.inv) } }

    /** „Отмени“ — записът се връща точно какъвто беше (датата на добавяне, „Кажи ми“). */
    fun restore(item: WantToReadEntity) = viewModelScope.launch { dbWrite { dao.upsert(item) } }

    fun setNotify(item: WantToReadEntity, on: Boolean) = viewModelScope.launch {
        // Новият преход се брои от показаната сега наличност.
        val available = ui.value.rows.firstOrNull { it.saved.inv == item.inv }?.book?.available ?: item.lastKnownAvailable
        dbWrite { dao.upsert(item.copy(notifyWhenAvailable = on, lastKnownAvailable = available)) }
    }

    fun clear() = viewModelScope.launch { dbWrite { dao.clear() } }
}

@Composable
fun WantToReadScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel(key = "want-to-read") { WantToReadViewModel(it) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val context = LocalContext.current
    val requestNotify = rememberNotifyRequest(snackbar)
    var confirmClear by rememberSaveable { mutableStateOf(false) }

    val remove: (WantToReadEntity) -> Unit = { item ->
        vm.remove(item)
        scope.launch { if (snackbar.showUndo(resources.getString(R.string.want_removed, item.title), resources)) vm.restore(item) }
    }
    val haptic = LocalHapticFeedback.current
    val toggleNotify: (WantToReadEntity) -> Unit = { item ->
        if (item.notifyWhenAvailable) {
            haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
            vm.setNotify(item, false)
            scope.launch { if (snackbar.showUndo(resources.getString(R.string.want_notify_unset), resources)) vm.restore(item) }
        } else {
            requestNotify { haptic.performHapticFeedback(HapticFeedbackType.ToggleOn); vm.setNotify(item, true) }
        }
    }

    // Личен списък: корици само генерирани (заявка по ISBN би издала какво искаш да четеш).
    PersonalCovers {
        Scaffold(
            topBar = {
                BackTopBar(stringResource(R.string.want_title), onBack, actions = {
                    if (ui.rows.isNotEmpty()) {
                        IconButton(onClick = { confirmClear = true }) {
                            Icon(Icons.Outlined.DeleteSweep, contentDescription = stringResource(R.string.want_clear))
                        }
                    }
                })
            },
            snackbarHost = { SnackbarHost(snackbar) },
        ) { padding ->
            when {
                !ui.loaded -> SkeletonList(rows = 3, modifier = Modifier.padding(padding))
                ui.rows.isEmpty() -> EmptyView(
                    stringResource(R.string.want_empty), stringResource(R.string.want_empty_hint),
                    modifier = Modifier.padding(padding), icon = Icons.Outlined.BookmarkBorder,
                )
                else -> LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(bottom = 32.dp)) {
                    item("header") {
                        Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                            // Наличността е по каталога (не „в момента“) — с датата му.
                            ui.generatedOn?.let { date ->
                                Text(
                                    stringResource(R.string.book_status_as_of, Formatters.shortDate(context, date) ?: "—"),
                                    style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                stringResource(R.string.want_hint_swipe),
                                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    items(ui.rows, key = { it.saved.inv }) { row ->
                        Column(Modifier.animateItem()) {
                            SwipeToRemove(onRemove = { remove(row.saved) }) {
                                WantToReadItem(
                                    row, catalogRead = ui.generatedOn != null,
                                    onOpen = { navigate(Routes.book(row.saved.inv, personal = true)) },
                                    onToggleNotify = { toggleNotify(row.saved) },
                                    onRemove = { remove(row.saved) },
                                )
                            }
                            HorizontalDivider(Modifier.padding(start = 92.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }

    if (confirmClear) {
        val cleared = stringResource(R.string.want_cleared)
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text(stringResource(R.string.want_clear)) },
            text = { Text(stringResource(R.string.want_clear_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    confirmClear = false
                    vm.clear()
                    scope.launch { snackbar.showSnackbar(cleared) }
                }) { Text(stringResource(R.string.action_clear)) }
            },
            dismissButton = { TextButton(onClick = { confirmClear = false }) { Text(stringResource(R.string.action_cancel)) } },
        )
    }
}

/** Плъзгане настрани (в двете посоки) маха реда; отдолу — червен фон с кошче. */
@Composable
private fun SwipeToRemove(onRemove: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val state = rememberSwipeToDismissBoxState()
    val remove by rememberUpdatedState(onRemove)
    val haptic = LocalHapticFeedback.current
    // Прагът е преминат (пуснат тук, редът ще се махне) — усеща се под пръста.
    LaunchedEffect(state.targetValue) {
        if (state.targetValue != SwipeToDismissBoxValue.Settled) haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
    }
    // Редът се маха, щом плъзгането стигне докрай (със „Отмени“ се връща нов ред).
    LaunchedEffect(state.currentValue) {
        if (state.currentValue != SwipeToDismissBoxValue.Settled) remove()
    }
    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        backgroundContent = {
            Box(
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer).padding(horizontal = 24.dp),
                contentAlignment = if (state.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd,
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        },
    ) { content() }
}

@Composable
private fun WantToReadItem(
    row: WantToReadRow,
    catalogRead: Boolean,
    onOpen: () -> Unit,
    onToggleNotify: () -> Unit,
    onRemove: () -> Unit,
) {
    val saved = row.saved
    val reduced = rememberReducedMotion()
    var menu by remember { mutableStateOf(false) }
    val removeLabel = stringResource(R.string.want_remove)
    val notifyLabel = stringResource(if (saved.notifyWhenAvailable) R.string.want_notify_stop else R.string.want_notify)
    val status = when {
        row.book != null -> row.book.status
        // Каталогът е прочетен, а книгата я няма в него (отписана).
        catalogRead -> null
        // Каталогът още не е прочетен — последно известната наличност.
        else -> if (saved.lastKnownAvailable) BookStatus.AVAILABLE else BookStatus.NOT_ON_SHELF
    }
    // „Кажи ми“ — само за книга, която не е на рафта (като на страницата ѝ); включеното
    // известие може да се изключи и когато вече е там.
    val canNotify = status != BookStatus.AVAILABLE || saved.notifyWhenAvailable
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onOpen)
            .heightIn(min = 72.dp)
            .padding(start = 20.dp, end = 4.dp, top = 10.dp, bottom = 10.dp)
            // Без жест: махането и „Кажи ми“ — и като действия за екранния четец.
            .semantics {
                customActions = listOfNotNull(
                    CustomAccessibilityAction(removeLabel) { onRemove(); true },
                    if (canNotify) CustomAccessibilityAction(notifyLabel) { onToggleNotify(); true } else null,
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BookCover(row.shown, width = 56.dp, showStatusDot = false, modifier = Modifier.sharedElementKey("book-${saved.inv}"), decorative = true)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(saved.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (saved.author.isNotBlank()) {
                Text(
                    saved.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
            }
            Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AnimatedContent(status, transitionSpec = { stateChange(reduced) }, label = "want-status") { st ->
                    when (st) {
                        null -> Text(stringResource(R.string.want_missing), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        // Като в реда на каталога: точка и „налична“ с малки букви; етикет — само за другите.
                        BookStatus.AVAILABLE -> AvailableMark()
                        else -> BookStatusPill(st)
                    }
                }
                if (saved.notifyWhenAvailable) {
                    Icon(
                        Icons.Outlined.NotificationsActive, contentDescription = stringResource(R.string.want_notify_on),
                        tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
        Box {
            IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, contentDescription = stringResource(R.string.want_more)) }
            DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                if (canNotify) DropdownMenuItem(
                    text = { Text(notifyLabel) },
                    leadingIcon = { Icon(if (saved.notifyWhenAvailable) Icons.Outlined.NotificationsOff else Icons.Outlined.NotificationsNone, null) },
                    onClick = { menu = false; onToggleNotify() },
                )
                DropdownMenuItem(
                    text = { Text(removeLabel) },
                    leadingIcon = { Icon(Icons.Outlined.Delete, null) },
                    onClick = { menu = false; onRemove() },
                )
            }
        }
    }
}

/**
 * „Кажи ми“: иска разрешение за известия едва сега; ако известията (или каналът „Библиотека“)
 * са спрени — не се включва, а снекбарът казва защо (с бутон към настройките). Изключените в
 * приложението известия „Библиотека“ се казват със снекбар с „Включи“.
 */
@Composable
fun rememberNotifyRequest(snackbar: SnackbarHostState): (onAllowed: () -> Unit) -> Unit {
    val context = LocalContext.current
    val c = LocalAppContainer.current
    val resources = LocalResources.current
    val permission = rememberNotificationPermission()
    val scope = rememberCoroutineScope()
    return remember(permission, snackbar) {
        { onAllowed: () -> Unit ->
            permission.requestWithResult { access ->
                val library = Channel.LIBRARY
                val status = notificationStatus(context, listOf(library))
                if (access == NotificationAccess.GRANTED && status.allows(library)) {
                    onAllowed()
                    scope.launch {
                        if (c.settings.current().notifyLibrary) {
                            snackbar.showSnackbar(resources.getString(R.string.want_notify_set))
                        } else {
                            val r = snackbar.showSnackbar(
                                resources.getString(R.string.want_library_off),
                                actionLabel = resources.getString(R.string.want_library_turn_on),
                                duration = SnackbarDuration.Long,
                            )
                            if (r == SnackbarResult.ActionPerformed) c.settings.setNotifyLibrary(true)
                        }
                    }
                } else {
                    scope.launch {
                        snackbar.showNotificationsBlocked(
                            context,
                            if (access == NotificationAccess.GRANTED) NotificationAccess.BLOCKED else access,
                            resources.getString(R.string.want_notify_blocked),
                            library.takeIf { !status.appBlocked },
                        )
                    }
                }
            }
        }
    }
}

/** Състоянието на една книга в „Искам да прочета“ — за страницата на книгата. */
class BookWantToReadViewModel(private val inv: Long, private val c: AppContainer) : ViewModel() {
    private val dao get() = c.database.wantToRead()
    val saved: StateFlow<WantToReadEntity?> = dao.observe(inv).orOnDbError(null)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private fun entry(book: CatalogBook, notify: Boolean, addedAt: Long) =
        WantToReadEntity(book.inv, book.title, book.author, addedAt, notify, book.available)

    /** Добавя/маха книгата; връща записа преди промяната (за „Отмени“), `null` — току-що добавена. */
    fun toggle(book: CatalogBook, onDone: (removed: WantToReadEntity?) -> Unit) = viewModelScope.launch {
        val current = runCatching { dao.get(inv) }.getOrNull()
        if (current != null) {
            if (dbWrite { dao.delete(inv) }) onDone(current)
        } else if (dbWrite { dao.upsert(entry(book, notify = false, c.clock.now().toEpochMilli())) }) {
            onDone(null)
        }
    }

    fun restore(item: WantToReadEntity) = viewModelScope.launch { dbWrite { dao.upsert(item) } }

    /** „Кажи ми“ — добавя книгата в списъка (ако я няма); преходът се брои от показаната сега наличност. */
    fun setNotify(book: CatalogBook, on: Boolean) = viewModelScope.launch {
        val current = runCatching { dao.get(inv) }.getOrNull()
        val base = current ?: entry(book, notify = on, c.clock.now().toEpochMilli())
        dbWrite { dao.upsert(base.copy(title = book.title, author = book.author, notifyWhenAvailable = on, lastKnownAvailable = book.available)) }
    }
}

@Composable
private fun bookWantToReadViewModel(inv: Long): BookWantToReadViewModel = appViewModel(key = "want-$inv") { BookWantToReadViewModel(inv, it) }

/** Отметката „Искам да прочета“ на страницата на книгата (запълнена — в списъка е). */
@Composable
fun WantToReadToggle(book: CatalogBook, snackbar: SnackbarHostState, modifier: Modifier = Modifier) {
    val vm = bookWantToReadViewModel(book.inv)
    val saved by vm.saved.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val haptic = LocalHapticFeedback.current
    val reduced = rememberReducedMotion()
    FilterChip(
        selected = saved != null,
        onClick = {
            haptic.performHapticFeedback(if (saved != null) HapticFeedbackType.ToggleOff else HapticFeedbackType.ToggleOn)
            vm.toggle(book) { removed ->
                scope.launch {
                    snackbar.currentSnackbarData?.dismiss()
                    if (removed == null) {
                        snackbar.showSnackbar(resources.getString(R.string.want_added))
                    } else if (snackbar.showUndo(resources.getString(R.string.want_removed, removed.title), resources)) {
                        vm.restore(removed)
                    }
                }
            }
        },
        label = {
            // На един ред: до корицата на тесен телефон (360 dp) „Искам да прочета“ иначе се пренася.
            val style = LocalTextStyle.current
            val max = if (style.fontSize.isSpecified) style.fontSize else 14.sp
            BasicText(
                stringResource(R.string.want_title), style = style.copy(color = LocalContentColor.current),
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                autoSize = TextAutoSize.StepBased(minFontSize = max * 0.8f, maxFontSize = max, stepSize = 0.25.sp),
            )
        },
        leadingIcon = {
            AnimatedContent(saved != null, transitionSpec = { stateChange(reduced) }, label = "want-chip") { inList ->
                Icon(
                    if (inList) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder, contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                )
            }
        },
        modifier = modifier.heightIn(min = 48.dp),
    )
}

/**
 * „Кажи ми, когато е на рафта“ — за книга, която не е на рафта (до „Попитай библиотеката“).
 * Превключвател (за TalkBack — „включено/изключено“): изключен — с контур, включен — тонален,
 * с „Ще ти кажем…“; изключването може да се отмени от снекбара. Името за екранния четец е
 * едно и също („Кажи ми…“) — състоянието казва Switch, не смененият надпис.
 * [onlyWhileOn] — само ако известието вече е включено (друг екземпляр е на рафта, но
 * включеното известие трябва да може да се изключи и оттук); след изключване остава до излизане.
 */
@Composable
fun NotifyWhenAvailableButton(
    book: CatalogBook,
    snackbar: SnackbarHostState,
    modifier: Modifier = Modifier,
    onlyWhileOn: Boolean = false,
) {
    val vm = bookWantToReadViewModel(book.inv)
    val saved by vm.saved.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val haptic = LocalHapticFeedback.current
    val requestNotify = rememberNotifyRequest(snackbar)
    val on = saved?.notifyWhenAvailable == true
    var wasOn by rememberSaveable(book.inv) { mutableStateOf(false) }
    LaunchedEffect(on) { if (on) wasOn = true }
    if (onlyWhileOn && !on && !wasOn) return
    val reduced = rememberReducedMotion()
    val colors = MaterialTheme.colorScheme
    val colorSpec: AnimationSpec<Color> = if (reduced) snap() else AppMotion.defaultEffects()
    // Цветовете се четат само при рисуване — преходът не прекомпозира бутона.
    val container = animateColorAsState(if (on) colors.secondaryContainer else colors.secondaryContainer.copy(alpha = 0f), colorSpec, label = "notify-bg")
    val content = animateColorAsState(if (on) colors.onSecondaryContainer else colors.primary, colorSpec, label = "notify-fg")
    val border = animateColorAsState(if (on) colors.outline.copy(alpha = 0f) else colors.outline, colorSpec, label = "notify-border")
    val label = stringResource(R.string.want_notify)
    Row(
        modifier
            .heightIn(min = 48.dp)
            .clip(CircleShape)
            .drawBehind {
                drawRect(container.value)
                // Контур 1 dp по ръба (като border с CircleShape).
                val w = 1.dp.toPx()
                drawRoundRect(
                    border.value, topLeft = Offset(w / 2, w / 2), size = Size(size.width - w, size.height - w),
                    cornerRadius = CornerRadius((size.minDimension - w) / 2), style = Stroke(w),
                )
            }
            .toggleable(value = on, role = Role.Switch) { turnOn ->
                if (turnOn) {
                    requestNotify { haptic.performHapticFeedback(HapticFeedbackType.ToggleOn); vm.setNotify(book, true) }
                } else {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    val before = saved
                    vm.setNotify(book, false)
                    scope.launch {
                        if (snackbar.showUndo(resources.getString(R.string.want_notify_unset), resources) && before != null) vm.restore(before)
                    }
                }
            }
            .semantics { contentDescription = label }
            .padding(ButtonDefaults.ContentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        val textStyle = MaterialTheme.typography.labelLarge
        AnimatedContent(on, transitionSpec = { stateChange(reduced) }, label = "notify") { isOn ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = rememberVectorPainter(if (isOn) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsNone)
                Spacer(Modifier.size(ButtonDefaults.IconSize).drawBehind { with(icon) { draw(size, colorFilter = ColorFilter.tint(content.value)) } })
                Spacer(Modifier.width(8.dp))
                // Видимият надпис се сменя; екранният четец казва [label] и състоянието.
                BasicText(
                    stringResource(if (isOn) R.string.want_notify_on else R.string.want_notify),
                    Modifier.clearAndSetSemantics { }, style = textStyle, color = { content.value },
                )
            }
        }
    }
}

/** Снекбар с „Отмени“ (както при махане от списъка); `true` — потребителят отмени. */
private suspend fun SnackbarHostState.showUndo(message: String, resources: Resources): Boolean {
    currentSnackbarData?.dismiss()
    return showSnackbar(message, actionLabel = resources.getString(R.string.want_undo), duration = SnackbarDuration.Long) == SnackbarResult.ActionPerformed
}

/** Смяна на състояние (етикет, превключвател): кратко преливане; при намалено движение — веднага. */
private fun stateChange(reduced: Boolean): ContentTransform =
    if (reduced) EnterTransition.None togetherWith ExitTransition.None
    else fadeIn(AppMotion.defaultEffects()) togetherWith fadeOut(AppMotion.fastEffects())

/** Описанието на плочката в „Моето“: колко книги има в списъка. */
@Composable
fun wantToReadTileSubtitle(): String {
    val c = LocalAppContainer.current
    val count by remember(c) { c.database.wantToRead().observeCount().orOnDbError(0) }.collectAsStateWithLifecycle(0)
    return if (count > 0) pluralStringResource(R.plurals.my_want_count, count, count) else stringResource(R.string.my_want_desc)
}
