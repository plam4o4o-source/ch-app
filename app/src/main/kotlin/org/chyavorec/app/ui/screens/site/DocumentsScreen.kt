package org.chyavorec.app.ui.screens.site

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.util.Formatters
import org.chyavorec.domain.model.DocumentKind

/**
 * Документи на читалището (устав, отчети, декларации — /data/files.json) и
 * историческите публикации от юбилейния вестник (PDF). Файловете се отварят
 * в браузъра/PDF четеца на устройството.
 */
@Composable
fun DocumentsScreen(onBack: () -> Unit, openLink: (String) -> Unit) {
    val vm = appViewModel { DocumentsViewModel(it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    var tab by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(topBar = { BackTopBar(stringResource(R.string.documents_title), onBack) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            PrimaryTabRow(selectedTabIndex = tab) {
                Tab(tab == 0, onClick = { tab = 0 }, text = { Text(stringResource(R.string.documents_tab_docs)) },
                    icon = { Icon(Icons.Outlined.Description, null) })
                Tab(tab == 1, onClick = { tab = 1 }, text = { Text(stringResource(R.string.documents_tab_publications)) },
                    icon = { Icon(Icons.Outlined.HistoryEdu, null) })
            }
            PullToRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.fillMaxSize()) {
                val kind = if (tab == 0) DocumentKind.DOCUMENT else DocumentKind.PUBLICATION
                StateContent(
                    state = state.map { list -> list.filter { it.kind == kind } },
                    onRetry = { vm.refresh() },
                    isEmpty = { it.isEmpty() },
                    skeleton = { SkeletonList(withImage = false) },
                    empty = { EmptyView(stringResource(R.string.documents_empty), icon = Icons.Outlined.Description) },
                    errorSubject = stringResource(R.string.documents_title),
                ) { docs ->
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                        item { SyncBanner(state.fromCache, state.syncedAt, state.refreshError) }
                        itemsIndexed(docs, key = { _, d -> d.id + tab }) { i, d ->
                            ListItem(
                                overlineContent = listOfNotNull(d.category, Formatters.shortDate(LocalContext.current, d.date), d.size)
                                    .joinToString(" · ").ifBlank { null }?.let { { Text(it) } },
                                headlineContent = { Text(d.title, style = MaterialTheme.typography.titleMedium) },
                                supportingContent = d.description.takeIf { it.isNotBlank() }?.let {
                                    { Text(it, maxLines = 3, overflow = TextOverflow.Ellipsis) }
                                },
                                leadingContent = { Icon(Icons.Outlined.PictureAsPdf, null, tint = MaterialTheme.colorScheme.secondary) },
                                modifier = Modifier.clickable { openLink(d.url) }.animateEntrance(i),
                            )
                        }
                    }
                }
            }
        }
    }
}
