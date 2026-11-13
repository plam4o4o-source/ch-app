package org.chyavorec.app.ui.screens.newspaper

import android.app.ActivityManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.LeafRefreshBox
import org.chyavorec.app.ui.components.MessageView
import org.chyavorec.app.ui.components.ScreenGutter
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchInOrnament
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.animateEntrance
import org.chyavorec.app.ui.components.rememberIsTouchExplorationEnabled
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.share.ContentShareCards
import org.chyavorec.app.ui.share.ShareActionButton
import org.chyavorec.app.ui.share.ShareImageHost
import org.chyavorec.app.ui.share.ShareImageRequest
import org.chyavorec.app.ui.share.rememberShareImageState
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.SiteDocument
import kotlin.math.min

/** Колко време свален файл остава в кеша — „Старият вестник“ се чете офлайн и след месеци. */
private const val FILE_MAX_AGE = Long.MAX_VALUE / 4

/**
 * „Старият вестник“: двадесетте статии от юбилейния вестник (1959) — като листове от стар вестник,
 * които се отварят в приложението (щипване за увеличение, офлайн след първото отваряне).
 */
@Composable
fun NewspaperScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel { NewspaperListViewModel(it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    // Голямото заглавие („шапката“) е в списъка; в лентата заглавието се показва, щом то излезе (или няма списък).
    val headerGone by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val listShown = state.data?.isNotEmpty() == true
    Scaffold(topBar = { BackTopBar(stringResource(R.string.newspaper_title), onBack, titleShown = !listShown || headerGone) }) { padding ->
        LeafRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(
                state = state,
                onRetry = { vm.refresh() },
                isEmpty = { it.isEmpty() },
                skeleton = { SkeletonList(withImage = false) },
                empty = { EmptyView(stringResource(R.string.newspaper_empty), icon = Icons.AutoMirrored.Outlined.Article) },
                errorSubject = stringResource(R.string.subject_documents),
            ) { docs ->
                NewspaperList(docs, state.fromCache, state.syncedAt, state.refreshError, listState = listState) { d ->
                    navigate(Routes.newspaperRead(d.url, d.title, d.category))
                }
            }
        }
    }
}

/** Заглавката („шапката“ на вестника) и картите-статии; без състояние — за тестовете със снимки. */
@Composable
fun NewspaperList(
    docs: List<SiteDocument>,
    fromCache: Boolean = false,
    syncedAt: java.time.Instant? = null,
    refreshError: org.chyavorec.core.AppError? = null,
    listState: LazyListState = rememberLazyListState(),
    onOpen: (SiteDocument) -> Unit,
) {
    LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 24.dp)) {
        item("masthead") { Masthead() }
        item("sync") { SyncBanner(fromCache, syncedAt, refreshError) }
        itemsIndexed(docs, key = { _, d -> d.id }) { i, d ->
            ArticleCard(d, onClick = { onOpen(d) }, modifier = Modifier.animateEntrance(i).padding(horizontal = ScreenGutter, vertical = 5.dp))
        }
    }
}

/** „Шапката“: заглавие в Cormorant между двойни златни линии, годината и бродирана лента. */
@Composable
private fun Masthead() {
    val gold = LocalExtendedColors.current.gold
    Column(Modifier.fillMaxWidth().padding(horizontal = ScreenGutter, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        DoubleRule(gold)
        Text(
            stringResource(R.string.newspaper_title), style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 10.dp).semantics { heading() },
        )
        Text(
            stringResource(R.string.newspaper_subtitle), style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
        )
        StitchInOrnament(StitchCharts.band(4), Modifier.size(width = 132.dp, height = 28.dp))
        Text(
            stringResource(R.string.newspaper_intro), style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

/** Двойна тънка линия като над заглавието на стар вестник. */
@Composable
private fun DoubleRule(color: androidx.compose.ui.graphics.Color) {
    androidx.compose.foundation.Canvas(Modifier.fillMaxWidth().height(6.dp).decorative()) {
        drawLine(color, androidx.compose.ui.geometry.Offset(0f, 1.dp.toPx()), androidx.compose.ui.geometry.Offset(size.width, 1.dp.toPx()), strokeWidth = 1.5.dp.toPx())
        drawLine(color.copy(alpha = 0.6f), androidx.compose.ui.geometry.Offset(0f, 5.dp.toPx()), androidx.compose.ui.geometry.Offset(size.width, 5.dp.toPx()), strokeWidth = 0.8.dp.toPx())
    }
}

private fun Modifier.decorative(): Modifier = this.clearAndSetSemantics { }

@Composable
private fun ArticleCard(d: SiteDocument, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val dark = LocalExtendedColors.current.isDark
    val paper = if (dark) PaperColors.SheetDark else PaperColors.Sheet
    val ink = if (dark) androidx.compose.ui.graphics.Color(0xFFEDDEBD) else androidx.compose.ui.graphics.Color(0xFF2A1E12)
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
    val open = stringResource(R.string.newspaper_open)
    Surface(
        color = paper, contentColor = ink, shape = shape, shadowElevation = 1.dp,
        border = BorderStroke(1.dp, LocalExtendedColors.current.gold.copy(alpha = 0.55f)),
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp).clip(shape)
            .clickable(onClickLabel = open, onClick = onClick),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            d.category?.takeIf { it.isNotBlank() }?.let {
                Text(it.uppercase(), style = MaterialTheme.typography.labelSmall, color = if (dark) LocalExtendedColors.current.gold else org.chyavorec.app.ui.theme.Brand.GoldText)
            }
            Text(d.title, style = MaterialTheme.typography.titleLarge.copy(fontFamily = Cormorant, fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.titleLarge.fontSize * 1.12f))
            if (d.description.isNotBlank()) {
                Text(d.description, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis, color = ink.copy(alpha = 0.85f))
            }
        }
    }
}

/** Четецът: листът на екрана с щипване/влачене, страници, споделяне и „Отвори във външно приложение“. */
@Composable
fun NewspaperReaderScreen(url: String, title: String, subtitle: String, onBack: () -> Unit, openLink: (String) -> Unit) {
    val context = LocalContext.current
    val vm = appViewModel(key = "newspaper:$url") { c ->
        NewspaperReaderViewModel(url, title, fetch = { progress -> c.newspaper.download(url, title, FILE_MAX_AGE, progress) })
    }
    val state by vm.state.collectAsStateWithLifecycle()
    val page by vm.page.collectAsStateWithLifecycle()
    val pageFailed by vm.pageFailed.collectAsStateWithLifecycle()
    var index by rememberSaveable { mutableIntStateOf(0) }
    val share = rememberShareImageState()
    val screenReader = rememberIsTouchExplorationEnabled()
    val lowRam = remember { context.getSystemService(ActivityManager::class.java)?.isLowRamDevice == true }

    val openExternal: () -> Unit = {
        val f = vm.file
        if (f == null || !Intents.openPdf(context, f)) openLink(url)
    }
    val shareText = remember(title, url) { "$title\n$url" }

    Scaffold(
        topBar = {
            BackTopBar(title, onBack, actions = {
                IconButton(onClick = openExternal) { Icon(Icons.AutoMirrored.Outlined.OpenInNew, stringResource(R.string.newspaper_open_external)) }
                ShareActionButton(
                    onShareLink = { Intents.share(context, title, shareText) },
                    image = { ShareImageRequest(ContentShareCards.newspaper(title, subtitle, url), title, shareText) },
                    state = share,
                )
            })
        },
    ) { padding ->
        ShareImageHost(share)
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (val s = state) {
                is ReaderState.Loading -> LoadingSheet(s.progress)
                is ReaderState.Failed -> ProblemSheet(s.reason, onRetry = vm::retry, onOpenExternal = { openLink(url) })
                is ReaderState.Ready -> {
                    val count = s.pageCount
                    val safeIndex = index.coerceIn(0, count - 1)
                    BoxWithConstraints(Modifier.fillMaxSize()) {
                        val widthPx = constraints.maxWidth
                        val base = (widthPx * NewspaperReaderViewModel.BASE_FACTOR).toInt()
                        LaunchedEffect(safeIndex, base) { vm.render(safeIndex, base) }
                        Column(Modifier.fillMaxSize()) {
                            Box(Modifier.weight(1f).fillMaxWidth()) {
                                ZoomablePaper(
                                    page = page?.takeIf { it.index == safeIndex },
                                    pageKey = safeIndex,
                                    description = pageDescription(title, safeIndex, count),
                                    onZoomSettled = { z ->
                                        // Увеличено над ~1.4× — по-едра картинка (със запас), но в границите на паметта.
                                        if (z > 1.4f) {
                                            val boosted = min(NewspaperReaderViewModel.MAX_BOOST_WIDTH, (widthPx * z * 1.25f).toInt())
                                            vm.render(safeIndex, if (lowRam) min(boosted, 2400) else boosted, boost = !lowRam)
                                        }
                                    },
                                )
                                if (pageFailed) PageFailed(onRetry = vm::retryPage, modifier = Modifier.align(Alignment.Center))
                            }
                            if (count > 1) PageBar(safeIndex, count, onPrev = { index = safeIndex - 1 }, onNext = { index = safeIndex + 1 })
                            if (screenReader) ExternalButton(openExternal)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingSheet(progress: Float?) {
    val dark = LocalExtendedColors.current.isDark
    Box(Modifier.fillMaxSize().background(if (dark) PaperColors.DeskDark else PaperColors.Desk), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            StitchInOrnament(StitchCharts.rosette(6), Modifier.size(72.dp))
            Spacer(Modifier.size(16.dp))
            Text(stringResource(R.string.newspaper_loading), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.size(12.dp))
            val modifier = Modifier.width(180.dp)
            if (progress == null) LinearProgressIndicator(modifier) else LinearProgressIndicator(progress = { progress }, modifier = modifier)
        }
    }
}

@Composable
private fun ProblemSheet(reason: ReaderProblem, onRetry: () -> Unit, onOpenExternal: () -> Unit) {
    val dark = LocalExtendedColors.current.isDark
    val (icon, message) = when (reason) {
        ReaderProblem.NETWORK -> Icons.Outlined.CloudOff to R.string.newspaper_error_network
        ReaderProblem.SERVER -> Icons.Outlined.ErrorOutline to R.string.newspaper_error_server
        ReaderProblem.CORRUPT -> Icons.Outlined.BrokenImage to R.string.newspaper_error_corrupt
        ReaderProblem.PASSWORD -> Icons.Outlined.BrokenImage to R.string.newspaper_error_password
        ReaderProblem.MEMORY -> Icons.Outlined.BrokenImage to R.string.newspaper_error_memory
    }
    Column(
        Modifier.fillMaxSize().background(if (dark) PaperColors.DeskDark else PaperColors.Desk),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
    ) {
        MessageView(icon, stringResource(R.string.newspaper_error_title), stringResource(message), actionLabel = stringResource(R.string.action_retry), onAction = onRetry)
        ExternalButton(onOpenExternal)
    }
}

@Composable
private fun PageFailed(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh, shape = MaterialTheme.shapes.medium, modifier = modifier.padding(24.dp), shadowElevation = 3.dp) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(stringResource(R.string.newspaper_page_failed), style = MaterialTheme.typography.bodyMedium)
            OutlinedButton(onClick = onRetry, modifier = Modifier.heightIn(min = 48.dp)) { Text(stringResource(R.string.action_retry)) }
        }
    }
}

@Composable
private fun ExternalButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.padding(12.dp).heightIn(min = 48.dp)) {
        Icon(Icons.AutoMirrored.Outlined.OpenInNew, null, Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.newspaper_open_external))
    }
}

/** „‹ 1 / 2 ›“ — страниците на статия с повече от една страница. */
@Composable
fun PageBar(index: Int, count: Int, onPrev: () -> Unit, onNext: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = onPrev, enabled = index > 0) { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, stringResource(R.string.newspaper_prev_page)) }
            Text(
                stringResource(R.string.newspaper_page_of, index + 1, count),
                style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp),
            )
            IconButton(onClick = onNext, enabled = index < count - 1) { Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, stringResource(R.string.newspaper_next_page)) }
        }
    }
}
