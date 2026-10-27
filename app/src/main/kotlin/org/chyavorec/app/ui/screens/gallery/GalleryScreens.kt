package org.chyavorec.app.ui.screens.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import org.chyavorec.app.ui.components.LeafRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
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
    val vm = appViewModel { GalleryViewModel(it.siteRepository, online = it.connectivity::isOnline) }
    val state by vm.state.collectAsStateWithLifecycle()
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
                                SiteImage(album.cover?.thumbUrl, width = cell, contentDescription = null, modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(MaterialTheme.shapes.medium))
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
    val vm = appViewModel { GalleryViewModel(it.siteRepository, online = it.connectivity::isOnline) }
    val state by vm.state.collectAsStateWithLifecycle()
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
                    SiteImage(
                        p.thumbUrl, width = cell, contentDescription = p.title.ifBlank { stringResource(R.string.photo_n, i + 1) },
                        modifier = Modifier.aspectRatio(1f).padding(2.dp).clip(RoundedCornerShape(6.dp)).clickable { navigate(Routes.viewer(name, i)) },
                    )
                }
            }
        }
    }
}

/**
 * Преглед на снимки на цял екран: swipe между снимките, pinch/двоен тап за увеличение, споделяне.
 * Снимки по адрес (от статия/страница): [alts] — описанията им (в реда на [urls]), а
 * [pageUrl]/[pageTitle] — статията или страницата, която се споделя.
 */
@OptIn(ExperimentalFoundationApi::class)
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
    val vm = appViewModel { GalleryViewModel(it.siteRepository, load = !album.isNullOrBlank(), online = it.connectivity::isOnline) }
    val state by vm.state.collectAsStateWithLifecycle()
    val photos: List<GalleryPhoto> = if (!album.isNullOrBlank()) {
        state.data?.firstOrNull { it.name == album }?.photos.orEmpty()
    } else remember(urls, alts, pageUrl) { viewerPhotos(urls, alts, pageUrl) }
    val context = LocalContext.current
    val resources = LocalResources.current
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (photos.isNotEmpty()) {
            val pager = rememberPagerState(initialPage = index.coerceIn(0, photos.lastIndex)) { photos.size }
            HorizontalPager(pager, modifier = Modifier.fillMaxSize(), key = { photos[it].id }, beyondViewportPageCount = 1) { page ->
                // Увеличението се нулира, когато страницата излезе от фокус (смяна на снимка).
                val photo = photos[page]
                ZoomableImage(photo, photoDescription(photo.title, page, photos.size, pageTitle), active = pager.settledPage == page)
            }
            val current = photos[pager.currentPage.coerceIn(0, photos.lastIndex)]
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
 * (заместител от кеша в паметта), а не черен екран.
 */
@Composable
private fun ZoomableImage(photo: GalleryPhoto, description: String, active: Boolean) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var bounds by remember { mutableStateOf(IntSize.Zero) }
    // Увеличено над 2× размера на зареждането — оригиналът; остава до нулирането.
    var original by remember { mutableStateOf(false) }
    // Местенето не извежда снимката извън екрана.
    fun clamp(o: Offset, s: Float): Offset {
        val maxX = (bounds.width * (s - 1f) / 2f).coerceAtLeast(0f)
        val maxY = (bounds.height * (s - 1f) / 2f).coerceAtLeast(0f)
        return Offset(o.x.coerceIn(-maxX, maxX), o.y.coerceIn(-maxY, maxY))
    }
    val transform = rememberTransformableState { zoom, pan, _ ->
        scale = (scale * zoom).coerceIn(1f, 5f)
        offset = if (scale > 1f) clamp(offset + pan, scale) else Offset.Zero
        if (scale > ORIGINAL_ABOVE) original = true
    }
    LaunchedEffect(active) {
        if (!active) { scale = 1f; offset = Offset.Zero; original = false }
    }
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
    val sized = sources.getOrElse(attempt) { photo.fullUrl }
    // Двата размера имат свои ключове в паметта: голямата снимка е заместител на оригинала.
    val request = remember(sized, original, placeholder, screenW, screenH) {
        val data = if (original) photo.fullUrl else sized
        ImageRequest.Builder(context)
            .data(data)
            .memoryCacheKey(data + if (original) ORIGINAL_KEY else VIEWER_KEY)
            .apply {
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
    AsyncImage(
        model = request,
        contentDescription = description,
        contentScale = ContentScale.Fit,
        onError = { if (!original && attempt < sources.lastIndex) attempt++ },
        modifier = Modifier.fillMaxSize().background(Color.Black)
            .onSizeChanged { bounds = it }
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { tap ->
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

/** Над това увеличение се зарежда оригиналът (до 2× зареждането е рязко). */
private const val ORIGINAL_ABOVE = 2.05f
/** Оригиналът — най-много толкова пъти размера на екрана. */
private const val ORIGINAL_SCREENS = 4
private const val VIEWER_KEY = "#viewer"
private const val ORIGINAL_KEY = "#original"

/**
 * Адресите за прегледа по ред на опитване: оптимизираният от сайта вариант в ширина
 * [widthPx] (≈ 2× екрана), после оригиналът, накрая миниатюрата (ако и той не се зареди).
 */
internal object ViewerSources {
    fun of(fullUrl: String, thumbUrl: String, widthPx: Int): List<String> =
        listOfNotNull(SiteImageUrls.optimized(fullUrl, widthPx), fullUrl, thumbUrl.takeIf { it.isNotBlank() }).distinct()
}
