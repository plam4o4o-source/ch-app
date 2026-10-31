package org.chyavorec.app.ui.screens.gallery

import android.view.accessibility.AccessibilityManager
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import org.chyavorec.app.ui.components.LeafRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.network.HttpException
import kotlinx.coroutines.CancellationException
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.ImmersiveSystemBars
import org.chyavorec.app.ui.theme.SystemBarIcons
import kotlin.math.abs
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.SiteImage
import org.chyavorec.app.ui.components.adaptiveCellWidth
import org.chyavorec.app.ui.components.windowWidth
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.site.GalleryViewModel
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.GalleryAlbum
import org.chyavorec.domain.model.GalleryPhoto
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalDensity
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Precision
import org.chyavorec.app.ui.components.sitePhotoPlaceholderKey
import org.chyavorec.app.ui.components.windowHeight
import org.chyavorec.data.site.SiteImageUrls

@Composable
fun GalleryScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel { GalleryViewModel(it.siteRepository, hasNetwork = it.connectivity::hasNetwork) }
    val state by vm.state.collectAsStateWithLifecycle()
    val reconnects = rememberReconnects()
    Scaffold(topBar = { BackTopBar(stringResource(R.string.gallery_title), onBack) }) { padding ->
        LeafRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(
                state = state, onRetry = { vm.refresh() }, isEmpty = { it.isEmpty() },
                skeleton = { SkeletonCards() },
                empty = { EmptyView(stringResource(R.string.gallery_empty), icon = Icons.Outlined.PhotoLibrary) },
                errorSubject = stringResource(R.string.subject_gallery),
            ) { albums ->
                // Ширината на клетка (без отстъпите на мрежата) — за размера на миниатюрите.
                val cell = adaptiveCellWidth(windowWidth() - 32.dp, minSize = 160.dp, spacing = 12.dp)
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }, contentType = "banner") { SyncBanner(state.fromCache, state.syncedAt, state.refreshError) }
                    items(albums, key = { it.name }, contentType = { "album" }) { album ->
                        Column(Modifier.clickable { navigate(Routes.album(album.name)) }.animateItem()) {
                            Box {
                                PhotoTile(album.cover?.thumbUrl, width = cell, contentDescription = null, reload = reconnects,
                                    modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(MaterialTheme.shapes.medium))
                                Text(
                                    pluralStringResource(R.plurals.photos_count, album.photos.size, album.photos.size),
                                    style = MaterialTheme.typography.labelMedium, color = Color.White,
                                    modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
                                        .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(50)).padding(horizontal = 8.dp, vertical = 3.dp),
                                )
                            }
                            Text(albumTitle(album.name), style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 6.dp))
                        }
                    }
                }
            }
        }
    }
}

/** Името на албума: снимките от новините — на езика на интерфейса; другите са заглавия на страници от сайта. */
@Composable
private fun albumTitle(name: String): String = if (name == GalleryAlbum.NEWS) stringResource(R.string.tab_news) else name

@Composable
fun AlbumScreen(name: String, onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel { GalleryViewModel(it.siteRepository, hasNetwork = it.connectivity::hasNetwork) }
    val state by vm.state.collectAsStateWithLifecycle()
    val reconnects = rememberReconnects()
    Scaffold(topBar = { BackTopBar(albumTitle(name), onBack) }) { padding ->
        StateContent(
            state = state.map { albums -> albums.firstOrNull { it.name == name }?.photos.orEmpty() },
            onRetry = { vm.refresh() }, isEmpty = { it.isEmpty() },
            skeleton = { SkeletonCards() }, empty = { EmptyView(stringResource(R.string.gallery_empty), icon = Icons.Outlined.PhotoLibrary) },
            modifier = Modifier.padding(padding),
        ) { photos ->
            val cell = adaptiveCellWidth(windowWidth() - 8.dp, minSize = 110.dp)
            LazyVerticalGrid(GridCells.Adaptive(110.dp), contentPadding = PaddingValues(4.dp)) {
                itemsIndexed(photos, key = { _, p -> p.id }, contentType = { _, _ -> "photo" }) { i, p ->
                    PhotoTile(
                        p.thumbUrl, width = cell, contentDescription = p.title.ifBlank { stringResource(R.string.photo_n, i + 1) }, reload = reconnects,
                        modifier = Modifier.aspectRatio(1f).padding(2.dp).clip(RoundedCornerShape(6.dp)).clickable { navigate(Routes.viewer(name, i)) },
                    )
                }
            }
        }
    }
}

/**
 * Защо снимката не се зареди: липсва на сайта (HTTP 404/410) — окончателно; всичко друго (без
 * връзка, изтекло време, 5xx, „само от кеша“ без мрежа) — временно: опитва се пак.
 */
internal enum class PhotoFailure {
    Missing, Transient;

    companion object {
        fun of(error: Throwable?): PhotoFailure =
            if (error is HttpException && (error.response.code == 404 || error.response.code == 410)) Missing else Transient
    }
}

/**
 * Миниатюра от сайта. Липсва на сайта (404/410) — „Снимката липсва“ с икона (не празна плочка,
 * която изглежда като „още се зарежда“). Друга грешка (напр. без връзка) — неутрална плочка с
 * „Опитай отново“, която опитва и сама, щом връзката се върне ([reload] се смени).
 */
@Composable
internal fun PhotoTile(url: String?, width: Dp, contentDescription: String?, modifier: Modifier = Modifier, reload: Int = 0) {
    var failure by remember(url) { mutableStateOf<PhotoFailure?>(null) }
    LaunchedEffect(reload) { if (failure == PhotoFailure.Transient) failure = null }
    when (failure) {
        PhotoFailure.Missing -> MissingPhoto(contentDescription, modifier)
        PhotoFailure.Transient -> UnloadedPhoto(contentDescription, modifier, onRetry = { failure = null })
        null -> SiteImage(url, width = width, contentDescription = contentDescription, modifier = modifier, onFailure = { failure = PhotoFailure.of(it) })
    }
}

/** Липсва на сайта: икона и „Снимката липсва“ — с името на снимката (и за екранния четец). */
@Composable
private fun MissingPhoto(title: String?, modifier: Modifier) {
    val label = stringResource(R.string.photo_unavailable)
    val spoken = if (title.isNullOrBlank()) label else stringResource(R.string.photo_unavailable_named, title)
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh).clearAndSetSemantics { contentDescription = spoken }.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Outlined.BrokenImage, contentDescription = null, tint = muted, modifier = Modifier.size(28.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = muted, textAlign = TextAlign.Center, maxLines = 2,
            overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp))
        if (!title.isNullOrBlank()) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = muted, textAlign = TextAlign.Center, maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
    }
}

/** Не се зареди по друга причина (най-често без връзка): неутрална плочка с „Опитай отново“. */
@Composable
private fun UnloadedPhoto(title: String?, modifier: Modifier, onRetry: () -> Unit) {
    val label = stringResource(R.string.photo_not_loaded)
    val spoken = if (title.isNullOrBlank()) label else stringResource(R.string.photo_not_loaded_named, title)
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconButton(onClick = onRetry) { Icon(Icons.Outlined.Refresh, stringResource(R.string.action_retry), tint = muted) }
        Text(label, style = MaterialTheme.typography.labelSmall, color = muted, textAlign = TextAlign.Center, maxLines = 2,
            overflow = TextOverflow.Ellipsis, modifier = Modifier.clearAndSetSemantics { contentDescription = spoken })
    }
}

/** Колко пъти се е върнала връзката, докато екранът е показан — неуспелите миниатюри опитват пак. */
@Composable
private fun rememberReconnects(): Int {
    val reconnected = LocalAppContainer.current.reconnected
    var count by remember { mutableIntStateOf(0) }
    LaunchedEffect(reconnected) { reconnected.collect { count++ } }
    return count
}

/**
 * Преглед на снимки на цял екран: swipe между снимките, pinch/двоен тап за увеличение, споделяне.
 * Снимки по адрес (от статия/страница): [alts] — описанията им (в реда на [urls]), а
 * [pageUrl]/[pageTitle] — статията или страницата, която се споделя.
 */
@Composable
fun PhotoViewerScreen(
    album: String?,
    index: Int,
    urls: List<String>,
    onClose: () -> Unit,
    alts: List<String> = emptyList(),
    pageUrl: String = "",
    pageTitle: String = "",
) {
    // Отделни снимки по адрес (от статия/страница) не изискват зареждане на галерията.
    val vm = appViewModel { GalleryViewModel(it.siteRepository, load = !album.isNullOrBlank(), hasNetwork = it.connectivity::hasNetwork) }
    val state by vm.state.collectAsStateWithLifecycle()
    val photos: List<GalleryPhoto> = if (!album.isNullOrBlank()) {
        state.data?.firstOrNull { it.name == album }?.photos.orEmpty()
    } else remember(urls, alts, pageUrl) { viewerPhotos(urls, alts, pageUrl) }
    if (photos.isNotEmpty()) PhotoViewer(photos, index, onClose, pageTitle)
    else Box(Modifier.fillMaxSize().background(Color.Black))
}

/**
 * Самият преглед. Тап — скрива/показва лентата отгоре (затвори, брояч, заглавие, споделяне) и
 * системните ленти (потапящ режим). Плъзгане нагоре/надолу без увеличение — снимката следва
 * пръста, фонът избледнява, а след прага прегледът се затваря (иначе снимката се връща).
 * Жестът „назад“ смалява снимката (predictive back). С екранен четец контролите не се крият.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PhotoViewer(photos: List<GalleryPhoto>, index: Int, onClose: () -> Unit, pageTitle: String = "") {
    val context = LocalContext.current
    val resources = LocalResources.current
    val density = LocalDensity.current
    val reduced = rememberReducedMotion()
    var controls by rememberSaveable { mutableStateOf(true) }
    val touchExploration = rememberTouchExploration()
    val showControls = controls || touchExploration
    var zoomed by remember { mutableStateOf(false) }
    var height by remember { mutableIntStateOf(0) }
    // Изместването при плъзгане за затваряне и напредъкът на жеста „назад“ (0..1).
    var dragY by remember { mutableFloatStateOf(0f) }
    var backProgress by remember { mutableFloatStateOf(0f) }
    val dragFraction = if (height > 0) (abs(dragY) / height).coerceIn(0f, 1f) else 0f

    // Черен фон: светли икони в лентите, а при скрити контроли — и самите ленти скрити.
    SystemBarIcons(darkStatusBackground = true, darkNavigationBackground = true)
    ImmersiveSystemBars(hidden = !showControls)
    PredictiveBackHandler { events ->
        try {
            events.collect { backProgress = it.progress }
            onClose()
        } catch (e: CancellationException) {
            backProgress = 0f
            throw e
        }
    }

    Box(Modifier.fillMaxSize().onSizeChanged { height = it.height }.background(Color.Black.copy(alpha = 1f - dragFraction * 0.75f))) {
        val pager = rememberPagerState(initialPage = index.coerceIn(0, photos.lastIndex)) { photos.size }
        val threshold = height * DISMISS_FRACTION
        val fling = with(density) { DISMISS_VELOCITY.toPx() }
        HorizontalPager(
            pager, key = { photos[it].id }, beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
                // Само без увеличение — иначе вертикалното местене е на снимката.
                .draggable(
                    rememberDraggableState { dragY += it }, Orientation.Vertical, enabled = !zoomed,
                    onDragStopped = { velocity ->
                        when {
                            abs(dragY) > threshold || abs(velocity) > fling -> onClose()
                            reduced -> { dragY = 0f }
                            else -> animate(dragY, 0f, animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) { v, _ -> dragY = v }
                        }
                    },
                )
                .graphicsLayer {
                    translationY = dragY
                    val s = if (reduced) 1f else 1f - backProgress * 0.12f - dragFraction * 0.15f
                    scaleX = s; scaleY = s
                },
        ) { page ->
            // Увеличението се нулира, когато страницата излезе от фокус (смяна на снимка).
            val photo = photos[page]
            ZoomableImage(
                photo, photoDescription(photo.title, page, photos.size, pageTitle), active = pager.settledPage == page,
                onTap = { controls = !controls }, onZoomChange = { zoomed = it },
            )
        }
        val current = photos[pager.currentPage.coerceIn(0, photos.lastIndex)]
        AnimatedVisibility(
            showControls,
            enter = if (reduced) EnterTransition.None else fadeIn(),
            exit = if (reduced) ExitTransition.None else fadeOut(),
            modifier = Modifier.graphicsLayer { alpha = 1f - (dragFraction * 4f).coerceAtMost(1f) },
        ) {
            Row(
                Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)))
                    .statusBarsPadding().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, stringResource(R.string.action_close), tint = Color.White) }
                Text(
                    "${pager.currentPage + 1} / ${photos.size}" + if (current.title.isNotBlank()) "  ·  ${current.title}" else "",
                    color = Color.White, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                // Споделя се страницата (статията, албумът), а без нея — адресът на снимката.
                IconButton(onClick = {
                    val share = PhotoShare.of(current, pageTitle, resources.getString(R.string.gallery_title))
                    Intents.share(context, share.subject, share.text)
                }) {
                    Icon(Icons.Outlined.Share, stringResource(R.string.action_share), tint = Color.White)
                }
            }
        }
    }
}

/** Дали е включен екранен четец (изследване с докосване) — тогава контролите на прегледа не се крият. */
@Composable
private fun rememberTouchExploration(): Boolean {
    val context = LocalContext.current
    val am = remember(context) { context.getSystemService(AccessibilityManager::class.java) }
    var enabled by remember(am) { mutableStateOf(am?.isTouchExplorationEnabled == true) }
    DisposableEffect(am) {
        val listener = AccessibilityManager.TouchExplorationStateChangeListener { enabled = it }
        am?.addTouchExplorationStateChangeListener(listener)
        onDispose { am?.removeTouchExplorationStateChangeListener(listener) }
    }
    return enabled
}

/** Снимките по адрес: описанието е заглавието им, а страницата — статията, от която са. */
internal fun viewerPhotos(urls: List<String>, alts: List<String>, pageUrl: String): List<GalleryPhoto> =
    urls.mapIndexed { i, u -> GalleryPhoto("u$i", alts.getOrNull(i).orEmpty().trim(), u, u, "", pageUrl) }

/** Какво се споделя от прегледа: темата и текстът (заглавие + адрес на страницата/снимката). */
internal data class PhotoShare(val subject: String, val text: String) {
    companion object {
        fun of(photo: GalleryPhoto, pageTitle: String, fallbackTitle: String): PhotoShare {
            val subject = pageTitle.ifBlank { photo.title }.ifBlank { photo.album }.ifBlank { fallbackTitle }
            val link = photo.pageUrl.ifBlank { photo.fullUrl }
            return PhotoShare(subject, if (subject == fallbackTitle) link else subject + "\n" + link)
        }
    }
}

/** Описанието за екранния четец: alt текстът, иначе „Снимка N от M“ (и статията, ако е известна). */
@Composable
private fun photoDescription(alt: String, index: Int, count: Int, pageTitle: String): String {
    if (alt.isNotBlank()) return alt
    val n = stringResource(R.string.photo_n_of_m, index + 1, count)
    return if (pageTitle.isBlank()) n else stringResource(R.string.photo_in, n, pageTitle)
}

/**
 * Снимка с pinch-to-zoom и местене; двоен тап — 2× / обратно; [active] = false нулира увеличението.
 *
 * Зарежда се оптимизираният вариант от сайта в около 2× размера на екрана (рязка и при 2×
 * увеличение); при по-голямо увеличение — оригиналът, но до 4× размера на екрана и без
 * запис в кеша в паметта. Докато идва, се вижда вече заредената миниатюра от мрежата/статията
 * (заместител от кеша в паметта), а не черен екран. Не се зареди нито един адрес — съобщение:
 * „липсва на сайта“ (404/410) или с „Опитай отново“ (без връзка и др.); неуспешен оригинал —
 * остава оптимизираният вариант. [onTap] — единичен тап; [onZoomChange] — дали е увеличена
 * (само докато е [active]).
 */
@Composable
internal fun ZoomableImage(
    photo: GalleryPhoto,
    description: String,
    active: Boolean,
    onTap: () -> Unit = {},
    onZoomChange: (Boolean) -> Unit = {},
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var bounds by remember { mutableStateOf(IntSize.Zero) }
    // Увеличено над 2× размера на зареждането — оригиналът; остава до нулирането.
    var original by remember { mutableStateOf(false) }
    var originalFailed by remember { mutableStateOf(false) }
    // Местенето не извежда снимката извън екрана.
    fun clamp(o: Offset, s: Float): Offset {
        val maxX = (bounds.width * (s - 1f) / 2f).coerceAtLeast(0f)
        val maxY = (bounds.height * (s - 1f) / 2f).coerceAtLeast(0f)
        return Offset(o.x.coerceIn(-maxX, maxX), o.y.coerceIn(-maxY, maxY))
    }
    val transform = rememberTransformableState { zoom, pan, _ ->
        scale = (scale * zoom).coerceIn(1f, 5f)
        offset = if (scale > 1f) clamp(offset + pan, scale) else Offset.Zero
        if (scale > ORIGINAL_ABOVE && !originalFailed) original = true
    }
    LaunchedEffect(active) {
        if (!active) { scale = 1f; offset = Offset.Zero; original = false }
    }
    val zoomed = scale > 1f
    LaunchedEffect(zoomed, active) { if (active) onZoomChange(zoomed) }
    val singleTap by rememberUpdatedState(onTap)
    val context = LocalContext.current
    val density = LocalDensity.current
    val screenW = with(density) { windowWidth().roundToPx() }
    val screenH = with(density) { windowHeight().roundToPx() }
    // Вече заредената миниатюра (мрежата на албума, статията) — заместител, докато идва голямата.
    val placeholder = remember(photo.thumbUrl, photo.fullUrl) {
        val memory = SingletonImageLoader.get(context).memoryCache
        sitePhotoPlaceholderKey({ key -> memory?.get(MemoryCache.Key(key)) != null }, listOf(photo.thumbUrl, photo.fullUrl))
    }
    val sources = remember(photo.fullUrl, photo.thumbUrl, screenW) { ViewerSources.of(photo.fullUrl, photo.thumbUrl, screenW * 2) }
    var attempt by remember(sources) { mutableIntStateOf(0) }
    // Всички адреси са без успех (и защо); [retry] — нов опит отначало (нова заявка, не от паметта на изгледа).
    var failure by remember(sources) { mutableStateOf<PhotoFailure?>(null) }
    var retry by remember(sources) { mutableIntStateOf(0) }
    val failed = failure
    if (failed != null) {
        PhotoLoadError(
            missing = failed == PhotoFailure.Missing,
            onRetry = { attempt = 0; originalFailed = false; failure = null; retry++ },
            modifier = Modifier.pointerInput(Unit) { detectTapGestures(onTap = { singleTap() }) },
        )
        return
    }
    val sized = sources.getOrElse(attempt) { photo.fullUrl }
    // Двата размера имат свои ключове в паметта: голямата снимка е заместител на оригинала.
    val request = remember(sized, original, placeholder, screenW, screenH, retry) {
        val data = if (original) photo.fullUrl else sized
        ImageRequest.Builder(context)
            .data(data)
            .memoryCacheKey(data + if (original) ORIGINAL_KEY else VIEWER_KEY)
            .apply {
                // Повторният опит — от мрежата, не от диска.
                if (retry > 0) diskCachePolicy(CachePolicy.WRITE_ONLY)
                if (original) {
                    // До 4× екрана (увеличението е до 5×) и без запис в паметта: голям bitmap
                    // само за този преглед не изтласква миниатюрите и кориците от кеша.
                    size(screenW * ORIGINAL_SCREENS, screenH * ORIGINAL_SCREENS)
                    precision(Precision.INEXACT)
                    memoryCachePolicy(CachePolicy.READ_ONLY)
                    placeholderMemoryCacheKey(sized + VIEWER_KEY)
                } else {
                    size(screenW * 2, screenH * 2)
                    precision(Precision.INEXACT)
                    placeholder?.let { placeholderMemoryCacheKey(it) }
                }
            }
            .build()
    }
    key(retry) {
        AsyncImage(
            model = request,
            contentDescription = description,
            contentScale = ContentScale.Fit,
            onError = {
                when {
                    original -> { original = false; originalFailed = true }
                    attempt < sources.lastIndex -> attempt++
                    else -> failure = PhotoFailure.of(it.result.throwable)
                }
            },
            // Без собствен фон: при плъзгане за затваряне избледнява фонът на прегледа.
            modifier = Modifier.fillMaxSize()
                .onSizeChanged { bounds = it }
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { singleTap() }, onDoubleTap = { tap ->
                        if (scale > 1f) {
                            scale = 1f; offset = Offset.Zero
                        } else {
                            scale = 2f
                            // Увеличава се около докоснатото място.
                            val center = Offset(bounds.width / 2f, bounds.height / 2f)
                            offset = clamp((center - tap) * (scale - 1f), scale)
                        }
                    })
                }
                // Жестът за мащабиране е активен само при увеличение, за да не пречи на swipe.
                .transformable(transform, lockRotationOnZoomPan = true, canPan = { scale > 1f })
                .graphicsLayer { scaleX = scale; scaleY = scale; translationX = offset.x; translationY = offset.y },
        )
    }
}

/**
 * Снимката в прегледа не се зареди: ясно съобщение вместо черен екран — „липсва на сайта“
 * ([missing], без смисъл от нов опит) или общото с „Опитай отново“.
 */
@Composable
private fun PhotoLoadError(missing: Boolean, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Outlined.BrokenImage, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(48.dp))
        Text(
            stringResource(if (missing) R.string.photo_missing_on_site else R.string.photo_load_failed),
            color = Color.White, style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center, modifier = Modifier.padding(top = 12.dp),
        )
        if (!missing) {
            OutlinedButton(
                onClick = onRetry, modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            ) { Text(stringResource(R.string.action_retry)) }
        }
    }
}

/** Над това увеличение се зарежда оригиналът (до 2× зареждането е рязко). */
private const val ORIGINAL_ABOVE = 2.05f
/** Оригиналът — най-много толкова пъти размера на екрана. */
private const val ORIGINAL_SCREENS = 4
private const val VIEWER_KEY = "#viewer"
private const val ORIGINAL_KEY = "#original"
/** Плъзгане за затваряне: след тази част от височината — или по-бързо от [DISMISS_VELOCITY]. */
private const val DISMISS_FRACTION = 0.18f
private val DISMISS_VELOCITY = 1500.dp

/**
 * Адресите за прегледа по ред на опитване: оптимизираният от сайта вариант в ширина
 * [widthPx] (≈ 2× екрана), после оригиналът, накрая миниатюрата (ако и той не се зареди).
 */
internal object ViewerSources {
    fun of(fullUrl: String, thumbUrl: String, widthPx: Int): List<String> =
        listOfNotNull(SiteImageUrls.optimized(fullUrl, widthPx), fullUrl, thumbUrl.takeIf { it.isNotBlank() }).distinct()
}
