package org.chyavorec.app.ui.screens.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import org.chyavorec.app.ui.components.TabReselectEffect
import org.chyavorec.app.ui.components.BrandImage
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.ui.components.sharedElementKey
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.BookStatusPill
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.InfoRow
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.SyncStamp
import org.chyavorec.app.ui.components.errorMessage
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.home.BookRow
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.util.Intents
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.CatalogQuery
import org.chyavorec.domain.model.CatalogSort
import org.chyavorec.domain.model.SearchField
import org.chyavorec.domain.model.UdcSections

@Composable
fun fieldLabel(f: SearchField): String = stringResource(
    when (f) {
        SearchField.ALL -> R.string.field_all
        SearchField.TITLE -> R.string.field_title
        SearchField.AUTHOR -> R.string.field_author
        SearchField.ISBN -> R.string.field_isbn
        SearchField.KEYWORD -> R.string.field_keyword
        SearchField.INVENTORY -> R.string.field_inventory
    },
)

@Composable
fun sortLabel(s: CatalogSort): String = stringResource(
    when (s) {
        CatalogSort.RELEVANCE -> R.string.sort_relevance
        CatalogSort.TITLE -> R.string.sort_title
        CatalogSort.AUTHOR -> R.string.sort_author
        CatalogSort.YEAR_DESC -> R.string.sort_year_desc
        CatalogSort.YEAR_ASC -> R.string.sort_year_asc
        CatalogSort.NEWEST -> R.string.sort_newest
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navigate: (String) -> Unit) {
    val vm = appViewModel { CatalogViewModel(it.catalogRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var sortMenu by remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()
    val q = state.query
    TabReselectEffect(Routes.CATALOG) { listState.animateScrollToItem(0) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BrandImage(R.drawable.logo_catalog, null, Modifier.size(40.dp))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(stringResource(R.string.catalog_title), style = MaterialTheme.typography.titleLarge, maxLines = 1)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BrandImage(R.drawable.logo_invlib, null, Modifier.size(width = 20.dp, height = 13.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(stringResource(R.string.catalog_powered_by), style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            },
            actions = {
                Box {
                    IconButton(onClick = { sortMenu = true }) {
                        Icon(Icons.Outlined.SwapVert, contentDescription = stringResource(R.string.catalog_sort))
                    }
                    DropdownMenu(sortMenu, onDismissRequest = { sortMenu = false }) {
                        CatalogSort.entries.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(sortLabel(s) + if (q.sort == s) "  ✓" else "") },
                                onClick = { vm.setSort(s); sortMenu = false },
                            )
                        }
                    }
                }
                IconButton(onClick = { showFilters = true }) {
                    BadgedBox(badge = { if (q.hasFilters) Badge() }) {
                        Icon(Icons.Outlined.Tune, contentDescription = stringResource(R.string.catalog_filters))
                    }
                }
            },
        )
    }) { padding ->
        PullToRefreshBox(state.engine.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = q.text,
                    onValueChange = vm::setText,
                    placeholder = { Text(stringResource(R.string.catalog_search_hint, fieldLabel(q.field).lowercase())) },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    trailingIcon = {
                        if (q.text.isNotEmpty()) IconButton(onClick = { vm.setText("") }) {
                            Icon(Icons.Outlined.Clear, contentDescription = stringResource(R.string.action_clear))
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = if (q.field == SearchField.INVENTORY || q.field == SearchField.ISBN) KeyboardType.Number else KeyboardType.Text,
                    ),
                    keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                )
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(SearchField.entries) { f ->
                        FilterChip(
                            q.field == f,
                            onClick = {
                                if (q.field != f) haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                vm.setField(f)
                            },
                            label = { Text(fieldLabel(f)) },
                        )
                    }
                }
                AnimatedVisibility(state.suggestions.isNotEmpty() && q.text.length >= 2) {
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.suggestions) { s ->
                            TextButton(onClick = { vm.setText(s) }) { Text(s, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                        }
                    }
                }
                SyncBanner(state.engine.fromCache, state.engine.syncedAt, state.engine.refreshError)
                StateContent(
                    state = state.engine,
                    onRetry = { vm.refresh() },
                    isEmpty = { false },
                    skeleton = { SkeletonList() },
                    empty = {},
                    errorSubject = stringResource(R.string.subject_catalog),
                ) { engine ->
                    if (state.results.isEmpty() && !state.searching) {
                        EmptyView(stringResource(R.string.catalog_no_results), stringResource(R.string.catalog_no_results_hint), icon = Icons.Outlined.SearchOff)
                    } else {
                        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 24.dp)) {
                            item(contentType = "header") {
                                Text(
                                    pluralStringResource(R.plurals.catalog_count, state.totalResults, state.totalResults) + "  ·  " +
                                        stringResource(R.string.catalog_data_as_of, Formatters.shortDate(engine.snapshot.generatedOn) ?: "—"),
                                    style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                )
                            }
                            itemsIndexed(state.page, key = { _, b -> b.inv }, contentType = { _, _ -> "book" }) { i, b ->
                                BookListItem(b, onClick = { navigate(Routes.book(b.inv)) }, modifier = Modifier.animateItem().animateEntrance(i))
                            }
                            if (state.canLoadMore) {
                                item(contentType = "more") {
                                    OutlinedButton(onClick = vm::loadMore, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                        Text(stringResource(R.string.action_load_more))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFilters) {
        FiltersSheet(state, onDismiss = { showFilters = false }, onChange = vm::update, onClear = vm::clearFilters)
    }
}

@Composable
fun BookListItem(b: CatalogBook, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 10.dp)) {
        BookCover(b, width = 60.dp, modifier = Modifier.sharedElementKey("book-${b.inv}"))
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(b.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (b.author.isNotBlank()) {
                Text(b.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text(
                listOf(b.year, b.docType, b.department).filter { it.isNotBlank() }.joinToString(" · "),
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1,
            )
            Spacer(Modifier.height(6.dp))
            BookStatusPill(b.status)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FiltersSheet(state: CatalogUiState, onDismiss: () -> Unit, onChange: ((CatalogQuery) -> CatalogQuery) -> Unit, onClear: () -> Unit) {
    val facets = state.facets ?: return
    val q = state.query
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
        Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.catalog_filters), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f).semantics { heading() })
                TextButton(onClick = onClear, enabled = q.hasFilters) { Text(stringResource(R.string.action_clear_filters)) }
            }
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.filter_only_available), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Switch(q.onlyAvailable, onCheckedChange = { v -> onChange { it.copy(onlyAvailable = v) } })
            }
            FacetGroup(stringResource(R.string.filter_doc_type), facets.docTypes, q.docType) { v -> onChange { it.copy(docType = v) } }
            FacetGroup(stringResource(R.string.filter_genre), facets.udcSections.map { it.toString() }, q.udcSection?.toString(),
                label = { UdcSections.name(it.toInt()) }) { v -> onChange { it.copy(udcSection = v?.toInt()) } }
            FacetGroup(stringResource(R.string.filter_department), facets.departments, q.department) { v -> onChange { it.copy(department = v) } }
            FacetGroup(stringResource(R.string.filter_language), facets.languages, q.language) { v -> onChange { it.copy(language = v) } }
            val min = facets.minYear
            val max = facets.maxYear
            if (min != null && max != null && max > min) {
                Text(stringResource(R.string.filter_year), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                val from = (q.yearFrom ?: min).toFloat()
                val to = (q.yearTo ?: max).toFloat()
                Text("${from.toInt()} – ${to.toInt()}", style = MaterialTheme.typography.bodyMedium)
                RangeSlider(
                    value = from..to,
                    valueRange = min.toFloat()..max.toFloat(),
                    onValueChange = { r ->
                        onChange {
                            it.copy(
                                yearFrom = r.start.toInt().takeIf { y -> y > min },
                                yearTo = r.endInclusive.toInt().takeIf { y -> y < max },
                            )
                        }
                    },
                )
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(pluralStringResource(R.plurals.catalog_show_results, state.totalResults, state.totalResults))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FacetGroup(title: String, values: List<String>, selected: String?, label: (String) -> String = { it }, onSelect: (String?) -> Unit) {
    if (values.isEmpty()) return
    val haptic = LocalHapticFeedback.current
    Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp, bottom = 6.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        values.take(24).forEach { v ->
            FilterChip(
                selected == v,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onSelect(if (selected == v) null else v)
                },
                label = { Text(label(v)) },
            )
        }
    }
}

@Composable
fun BookScreen(inv: Long, onBack: () -> Unit, navigate: (String) -> Unit) {
    val container = LocalAppContainer.current
    val vm = appViewModel(key = "book-$inv") { BookViewModel(inv, it) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val hold by vm.holdResult.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val resources = LocalResources.current
    val b = ui.book

    Scaffold(topBar = {
        BackTopBar("", onBack, actions = {
            if (b != null) {
                IconButton(onClick = {
                    Intents.share(
                        context, b.title,
                        listOf(b.title, b.author, resources.getString(R.string.share_book_footer, container.config.siteBaseUrl + "/elektronen-katalog"))
                            .filter { it.isNotBlank() }.joinToString("\n"),
                    )
                }) { Icon(Icons.Outlined.Share, contentDescription = stringResource(R.string.action_share)) }
            }
        })
    }) { padding ->
        if (b == null) {
            if (ui.loaded) ErrorView(AppError.NotFound, onRetry = null, modifier = Modifier.padding(padding))
            return@Scaffold
        }
        Column(Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
            SyncBanner(ui.fromCache, ui.syncedAt, null)
            Row(Modifier.padding(20.dp), verticalAlignment = Alignment.Top) {
                BookCover(b, width = 124.dp, modifier = Modifier.sharedElementKey("book-${b.inv}"))
                Spacer(Modifier.width(18.dp))
                Column(Modifier.weight(1f)) {
                    Text(b.title, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
                    if (b.subtitle.isNotBlank()) Text(b.subtitle, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (b.author.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text(b.author, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
                    }
                    Spacer(Modifier.height(12.dp))
                    BookStatusPill(ui.liveStatus ?: b.status)
                    Text(
                        stringResource(R.string.book_status_as_of, Formatters.shortDate(ui.generatedOn) ?: "—"),
                        style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
            if (ui.canHold && b.available) {
                Button(onClick = { vm.placeHold() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    Icon(Icons.Outlined.BookmarkAdd, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.book_hold))
                }
            }
            if (b.annotation.isNotBlank()) {
                Text(stringResource(R.string.book_description), style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 20.dp, top = 16.dp).semantics { heading() })
                Text(b.annotation, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
            }
            Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Text(stringResource(R.string.book_details), style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp).semantics { heading() })
                val authors = b.authors
                if (authors.size > 1) InfoRow(stringResource(R.string.book_other_authors), authors.drop(1).joinToString("; "))
                InfoRow(stringResource(R.string.book_publisher), listOf(b.publisher, b.city).filter { it.isNotBlank() }.joinToString(", "))
                InfoRow(stringResource(R.string.book_year), b.year)
                InfoRow(stringResource(R.string.book_isbn), b.isbn)
                InfoRow(stringResource(R.string.book_language), b.language)
                InfoRow(stringResource(R.string.book_doc_type), b.docType)
                InfoRow(stringResource(R.string.book_genre), b.udcSection?.let { UdcSections.name(it) + " (УДК " + b.udc + ")" })
                InfoRow(stringResource(R.string.book_keywords), b.keywords)
                InfoRow(stringResource(R.string.book_call_number), b.callNumber)
                InfoRow(stringResource(R.string.book_inventory), b.inv.toString())
                InfoRow(stringResource(R.string.book_department), b.department)
                InfoRow(stringResource(R.string.book_library), stringResource(R.string.org_library_name))
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text(stringResource(R.string.book_availability_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                SyncStamp(ui.syncedAt, Modifier.padding(top = 4.dp))
            }
            if (ui.copies.isNotEmpty()) {
                Text(
                    pluralStringResource(R.plurals.book_copies, ui.copies.size + 1, ui.copies.size + 1) + ": " +
                        pluralStringResource(R.plurals.book_available_copies, (ui.copies + b).count { it.available }, (ui.copies + b).count { it.available }),
                    style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
            if (ui.sameAuthor.isNotEmpty()) {
                Row(Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Outlined.LibraryBooks, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.book_same_author), style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
                }
                BookRow(ui.sameAuthor) { navigate(Routes.book(it.inv)) }
            }
        }
    }

    hold?.let { result ->
        AlertDialog(
            onDismissRequest = { vm.holdResult.value = null },
            confirmButton = { TextButton(onClick = { vm.holdResult.value = null }) { Text(stringResource(R.string.action_ok)) } },
            title = { Text(stringResource(if (result is Outcome.Success) R.string.hold_ok_title else R.string.hold_error_title)) },
            text = {
                Text(
                    when (result) {
                        is Outcome.Success -> stringResource(R.string.hold_ok_text)
                        is Outcome.Failure -> if (result.error == AppError.Unauthorized) stringResource(R.string.hold_login_needed) else errorMessage(result.error)
                    },
                )
            },
        )
    }
}
