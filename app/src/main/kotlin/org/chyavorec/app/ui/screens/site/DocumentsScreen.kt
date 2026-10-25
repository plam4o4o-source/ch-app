package org.chyavorec.app.ui.screens.site

import androidx.compose.foundation.clickable
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import org.chyavorec.app.util.Intents
import org.chyavorec.data.http.FileDownloader
import java.io.File
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
import org.chyavorec.app.ui.components.LeafRefreshBox
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
    val context = LocalContext.current
    val vm = appViewModel { DocumentsViewModel(it.siteRepository, FileDownloader(it.okHttp, File(context.cacheDir, "documents"))) }
    val state by vm.state.collectAsStateWithLifecycle()
    val download by vm.download.collectAsStateWithLifecycle()
    // Свален PDF — с приложение за PDF; без такова или при неуспешно сваляне — в браузъра (Custom Tab).
    LaunchedEffect(vm) {
        vm.results.collect { r ->
            val opened = r is DocumentsViewModel.PdfResult.Ready && Intents.openPdf(context, r.file)
            if (!opened) {
                if (r is DocumentsViewModel.PdfResult.Failed) Toast.makeText(context, R.string.pdf_download_failed, Toast.LENGTH_SHORT).show()
                openLink(r.url)
            }
        }
    }
    var tab by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(topBar = { BackTopBar(stringResource(R.string.documents_title), onBack) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            PrimaryTabRow(selectedTabIndex = tab) {
                Tab(tab == 0, onClick = { tab = 0 }, text = { Text(stringResource(R.string.documents_tab_docs)) },
                    icon = { Icon(Icons.Outlined.Description, null) })
                Tab(tab == 1, onClick = { tab = 1 }, text = { Text(stringResource(R.string.documents_tab_publications)) },
                    icon = { Icon(Icons.Outlined.HistoryEdu, null) })
            }
            LeafRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.fillMaxSize()) {
                val kind = if (tab == 0) DocumentKind.DOCUMENT else DocumentKind.PUBLICATION
                StateContent(
                    state = state.map { list -> list.filter { it.kind == kind } },
                    onRetry = { vm.refresh() },
                    isEmpty = { it.isEmpty() },
                    skeleton = { SkeletonList(withImage = false) },
                    empty = { EmptyView(stringResource(R.string.documents_empty), icon = Icons.Outlined.Description) },
                    errorSubject = stringResource(R.string.subject_documents),
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
                                trailingContent = if (DocumentsViewModel.isPdf(d.url)) {
                                    { PdfAppButton(download?.takeIf { it.url == d.url }, onOpen = { vm.openInPdfApp(d.url) }, onCancel = vm::cancelDownload) }
                                } else null,
                                modifier = Modifier.clickable { openLink(d.url) }.animateEntrance(i),
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * „Отвори с приложение за PDF“: сваля файла и го подава на приложение на телефона (докосване на
 * реда — в браузъра, както досега). Докато се сваля — напредъкът; докосване го отказва.
 */
@Composable
private fun PdfAppButton(download: DocumentsViewModel.PdfDownload?, onOpen: () -> Unit, onCancel: () -> Unit) {
    if (download == null) {
        IconButton(onClick = onOpen) { Icon(Icons.Outlined.FileOpen, stringResource(R.string.pdf_open_with_app)) }
        return
    }
    val label = stringResource(R.string.pdf_downloading)
    IconButton(onClick = onCancel, modifier = Modifier.semantics { stateDescription = label }) {
        Box(contentAlignment = Alignment.Center) {
            val p = download.progress
            if (p == null) CircularProgressIndicator(Modifier.size(28.dp), strokeWidth = 3.dp)
            else CircularProgressIndicator(progress = { p }, modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
            Icon(Icons.Outlined.Close, stringResource(R.string.pdf_cancel_download), Modifier.size(16.dp))
        }
    }
}

