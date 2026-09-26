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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import org.chyavorec.app.ui.components.RemoteImage
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.navigation.Routes
import org.chyavorec.app.ui.screens.site.GalleryViewModel
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.GalleryPhoto

@Composable
fun GalleryScreen(onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel { GalleryViewModel(it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    Scaffold(topBar = { BackTopBar(stringResource(R.string.gallery_title), onBack) }) { padding ->
        PullToRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            StateContent(
                state = state, onRetry = { vm.refresh() }, isEmpty = { it.isEmpty() },
                skeleton = { SkeletonCards() },
                empty = { EmptyView(stringResource(R.string.gallery_empty), icon = Icons.Outlined.PhotoLibrary) },
                errorSubject = stringResource(R.string.gallery_title),
            ) { albums ->
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) { SyncBanner(state.fromCache, state.syncedAt, state.refreshError) }
                    items(albums, key = { it.name }) { album ->
                        Column(Modifier.clickable { navigate(Routes.album(album.name)) }.animateItem()) {
                            Box {
                                RemoteImage(album.cover?.thumbUrl, null, Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(18.dp)))
                                Text(
                                    pluralStringResource(R.plurals.photos_count, album.photos.size, album.photos.size),
                                    style = MaterialTheme.typography.labelMedium, color = Color.White,
                                    modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
                                        .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(50)).padding(horizontal = 8.dp, vertical = 3.dp),
                                )
                            }
                            Text(album.name, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumScreen(name: String, onBack: () -> Unit, navigate: (String) -> Unit) {
    val vm = appViewModel { GalleryViewModel(it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    Scaffold(topBar = { BackTopBar(name, onBack) }) { padding ->
        StateContent(
            state = state.map { albums -> albums.firstOrNull { it.name == name }?.photos.orEmpty() },
            onRetry = { vm.refresh() }, isEmpty = { it.isEmpty() },
            skeleton = { SkeletonCards() }, empty = { EmptyView(stringResource(R.string.gallery_empty)) },
            modifier = Modifier.padding(padding),
        ) { photos ->
            LazyVerticalGrid(GridCells.Adaptive(110.dp), contentPadding = PaddingValues(4.dp)) {
                itemsIndexed(photos, key = { _, p -> p.id }) { i, p ->
                    RemoteImage(
                        p.thumbUrl, p.title.ifBlank { stringResource(R.string.photo_n, i + 1) },
                        Modifier.aspectRatio(1f).padding(2.dp).clip(RoundedCornerShape(6.dp)).clickable { navigate(Routes.viewer(name, i)) },
                    )
                }
            }
        }
    }
}

/** Преглед на снимки на цял екран: swipe между снимките, pinch/двоен тап за увеличение, споделяне. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoViewerScreen(album: String?, index: Int, urls: List<String>, onClose: () -> Unit) {
    val vm = appViewModel { GalleryViewModel(it.siteRepository) }
    val state by vm.state.collectAsStateWithLifecycle()
    val photos: List<GalleryPhoto> = if (!album.isNullOrBlank()) {
        state.data?.firstOrNull { it.name == album }?.photos.orEmpty()
    } else urls.mapIndexed { i, u -> GalleryPhoto("u$i", "", u, u, "", u) }
    val context = LocalContext.current
    val resources = LocalResources.current
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (photos.isNotEmpty()) {
            val pager = rememberPagerState(initialPage = index.coerceIn(0, photos.lastIndex)) { photos.size }
            HorizontalPager(pager, modifier = Modifier.fillMaxSize(), key = { photos[it].id }) { page ->
                ZoomableImage(photos[page])
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
                IconButton(onClick = { Intents.share(context, current.title.ifBlank { resources.getString(R.string.gallery_title) }, current.pageUrl) }) {
                    Icon(Icons.Outlined.Share, stringResource(R.string.action_share), tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ZoomableImage(photo: GalleryPhoto) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val transform = rememberTransformableState { zoom, pan, _ ->
        scale = (scale * zoom).coerceIn(1f, 5f)
        offset = if (scale > 1f) offset + pan else Offset.Zero
    }
    RemoteImage(
        url = photo.fullUrl,
        fallbackUrl = photo.thumbUrl,
        contentDescription = photo.title.ifBlank { null },
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize().background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { if (scale > 1f) { scale = 1f; offset = Offset.Zero } else scale = 2.5f })
            }
            // Жестът за мащабиране е активен само при увеличение, за да не пречи на swipe.
            .transformable(transform, lockRotationOnZoomPan = true, canPan = { scale > 1f })
            .graphicsLayer { scaleX = scale; scaleY = scale; translationX = offset.x; translationY = offset.y },
    )
}
