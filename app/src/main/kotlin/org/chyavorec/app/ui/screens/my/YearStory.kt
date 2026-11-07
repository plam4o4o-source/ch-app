package org.chyavorec.app.ui.screens.my

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.ChLoadingIndicator
import org.chyavorec.app.ui.components.ShevitsaStrip
import org.chyavorec.app.ui.components.addShevitsaMotif
import org.chyavorec.app.ui.components.rememberIsTouchExplorationEnabled
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.share.CardEmblem
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareImageHost
import org.chyavorec.app.ui.share.ShareImageRequest
import org.chyavorec.app.ui.share.rememberShareImageState
import org.chyavorec.app.ui.share.sans
import org.chyavorec.app.ui.share.serif
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.ui.theme.SystemBarIcons
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.data.repository.AuthState

/** Колко трае един слайд при автоматично превъртане. */
internal const val SLIDE_MS = 5_500

/** Състоянието на историята: още се изчислява, готова или празна (няма книги през годината). */
sealed interface YearStoryState {
    data object Loading : YearStoryState
    data object Empty : YearStoryState
    data class Ready(val summary: ReadingYear.Summary) : YearStoryState
}

/** „Моята читателска година“ за [year] — от кешираната история (иначе веднъж от сървъра) и каталога в базата. */
class ReadingYearViewModel(private val year: Int, private val c: AppContainer) : ViewModel() {
    private val _state = MutableStateFlow<YearStoryState>(YearStoryState.Loading)
    val state: StateFlow<YearStoryState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            if (c.authRepository.state.value !is AuthState.SignedIn) {
                _state.value = YearStoryState.Empty
                return@launch
            }
            val history = c.libraryRepository.cachedHistory()?.data
                ?: (c.libraryRepository.history(force = false) as? Outcome.Success)?.value?.data
            if (history.isNullOrEmpty()) {
                _state.value = YearStoryState.Empty
                return@launch
            }
            val books = readingBooks(c, history)
            val summary = withContext(Dispatchers.Default) { ReadingYear.compute(history, books, year, c.clock.zone()) }
            _state.value = summary?.let { YearStoryState.Ready(it) } ?: YearStoryState.Empty
        }
    }
}

/** Екранът (маршрут, не диалог): тъмен, на цял екран, със светли икони в системните ленти. */
@Composable
fun YearStoryScreen(year: Int, onClose: () -> Unit) {
    val vm = appViewModel(key = "reading-year-$year") { ReadingYearViewModel(year, it) }
    val state by vm.state.collectAsStateWithLifecycle()
    SystemBarIcons(darkStatusBackground = true, darkNavigationBackground = true)
    Box(Modifier.fillMaxSize().background(Brand.Ink), contentAlignment = Alignment.Center) {
        when (val s = state) {
            YearStoryState.Loading -> ChLoadingIndicator(color = Brand.Gold)
            YearStoryState.Empty -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Text(stringResource(R.string.year_empty), style = serif(26.sp, Brand.Parchment))
                Spacer(Modifier.height(16.dp))
                Button(onClick = onClose) { Text(stringResource(R.string.action_close)) }
            }
            is YearStoryState.Ready -> YearStory(s.summary, onClose, siteUrl = LocalAppContainer.current.config.siteBaseUrl)
        }
    }
}

/**
 * Историята като в Instagram: лентички горе, които се пълнят (по ~5.5 s на слайд); докосване
 * вдясно — напред, вляво — назад; задържане — пауза; плъзгане надолу — затваряне. При
 * намалено движение — без автоматично превъртане и без ефекти; с TalkBack — паузирано,
 * всеки слайд е един възел с описание, а „Напред“/„Назад“ са бутони и действия на възела.
 */
@Composable
fun YearStory(summary: ReadingYear.Summary, onClose: () -> Unit, siteUrl: String, initialSlide: Int = 0) {
    val slides = remember(summary) { yearSlides(summary) }
    var index by rememberSaveable { mutableStateOf(initialSlide.coerceIn(0, slides.lastIndex)) }
    val reduced = rememberReducedMotion()
    val talkBack = rememberIsTouchExplorationEnabled()
    val auto = !reduced && !talkBack
    var held by remember { mutableStateOf(false) }
    val resumed by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val share = rememberShareImageState()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Таймерът и влизането са на слайд — нов слайд, нови стойности.
    // „Назад“ на първия слайд го пуска отначало ([restart]).
    var restart by remember { mutableIntStateOf(0) }
    val timer = remember(index, restart) { Animatable(0f) }
    val appear = remember(index) { Animatable(if (reduced) 1f else 0f) }
    LaunchedEffect(index, reduced) {
        if (reduced) appear.snapTo(1f) else appear.animateTo(1f, tween(1_800, easing = LinearEasing))
    }
    val paused = held || share.busy || !resumed.isAtLeast(Lifecycle.State.RESUMED)
    LaunchedEffect(index, restart, auto, paused) {
        if (!auto || paused) return@LaunchedEffect
        timer.animateTo(1f, tween(((1f - timer.value) * SLIDE_MS).toInt().coerceAtLeast(1), easing = LinearEasing))
        if (index < slides.lastIndex) index++
    }
    fun next() {
        if (index < slides.lastIndex) index++ else onClose()
    }
    fun previous() {
        if (index > 0) index-- else restart++
    }

    // Плъзгане надолу — затваря (историята се смалява след пръста).
    var dragY by remember { mutableFloatStateOf(0f) }
    val closeAt = with(LocalDensity.current) { 140.dp.toPx() }
    val kind = slides[index]
    val description = slideDescription(summary, kind)
    val stateText = stringResource(R.string.year_slide_state, index + 1, slides.size)
    val nextLabel = stringResource(R.string.year_next)
    val previousLabel = stringResource(R.string.year_previous)
    val shareSubject = stringResource(R.string.year_title_with, summary.year)
    val shareText = stringResource(R.string.year_share_text, summary.year, siteUrl)

    Box(
        Modifier.fillMaxSize()
            .graphicsLayer {
                val f = (dragY / size.height).coerceIn(0f, 1f)
                translationY = dragY
                scaleX = 1f - 0.12f * f
                scaleY = scaleX
                if (dragY > 0f) {
                    shape = RoundedCornerShape(28.dp)
                    clip = true
                }
            }
            .draggable(
                rememberDraggableState { d -> dragY = (dragY + d).coerceAtLeast(0f) },
                Orientation.Vertical,
                onDragStarted = { held = true },
                onDragStopped = { velocity ->
                    held = false
                    if (dragY > closeAt || velocity > 2_000f) {
                        onClose()
                    } else {
                        scope.launch { animate(dragY, 0f) { v, _ -> dragY = v } }
                    }
                },
            )
            .pointerInput(slides.size) {
                detectTapGestures(
                    onPress = {
                        held = true
                        tryAwaitRelease()
                        held = false
                    },
                    onLongPress = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
                    onTap = { o -> if (o.x < size.width * 0.3f) previous() else next() },
                )
            },
    ) {
        AnimatedContent(
            targetState = index,
            transitionSpec = { if (reduced) fadeIn(tween(0)) togetherWith fadeOut(tween(0)) else fadeIn(tween(260)) togetherWith fadeOut(tween(200)) },
            label = "year-slide",
        ) { i ->
            val k = slides[i]
            val look = remember(k, summary) { lookOf(k, summary) }
            val current = i == index
            Box(Modifier.fillMaxSize()) {
                // Мотивът във фона бавно се завърта с таймера (само докато слайдът тече).
                SlideBackdrop(look, drift = { if (current) timer.value else 1f })
                SlideBody(
                    summary, k,
                    appear = { if (current) appear.value else 1f },
                    modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(StoryContentPadding)
                        .clearAndSetSemantics {
                            contentDescription = description
                            stateDescription = stateText
                            liveRegion = LiveRegionMode.Polite
                            customActions = listOf(
                                CustomAccessibilityAction(nextLabel) { next(); true },
                                CustomAccessibilityAction(previousLabel) { previous(); true },
                            )
                        },
                )
            }
        }

        // Горе: лентичките на слайдовете, емблемата и „Затвори“.
        Column(Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 12.dp, vertical = 8.dp)) {
            StoryProgress(slides.size, index, progress = { if (auto) timer.value else 1f })
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CardEmblem(32.dp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.year_title), style = sans(13.sp, Brand.Parchment, FontWeight.SemiBold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(summary.year.toString(), style = sans(12.sp, Brand.GoldLight), maxLines = 1)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.action_close), tint = Brand.Parchment)
                }
            }
        }

        // Долу: „Сподели“ (картинка на текущия слайд), а с TalkBack — и „Назад“/„Напред“.
        Row(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (talkBack) {
                IconButton(onClick = ::previous, enabled = index > 0) {
                    Icon(Icons.Outlined.ChevronLeft, contentDescription = previousLabel, tint = Brand.Parchment)
                }
                Spacer(Modifier.width(12.dp))
            }
            Button(
                onClick = { share.share(ShareImageRequest(ShareCard.Year(summary, kind, siteUrl), shareSubject, shareText)) },
                enabled = !share.busy,
                colors = ButtonDefaults.buttonColors(containerColor = Brand.Gold, contentColor = Brand.Ink),
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                if (share.busy) {
                    CircularProgressIndicator(Modifier.size(18.dp), color = Brand.Ink, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.Share, null, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.action_share))
            }
            if (talkBack) {
                Spacer(Modifier.width(12.dp))
                IconButton(onClick = ::next) {
                    Icon(Icons.Outlined.ChevronRight, contentDescription = nextLabel, tint = Brand.Parchment)
                }
            }
        }
        ShareImageHost(share)
    }
}

/** Лентичките горе: минали — пълни, текущата — според [progress], следващите — празни. */
@Composable
private fun StoryProgress(count: Int, index: Int, progress: () -> Float) {
    Canvas(Modifier.fillMaxWidth().height(3.dp).clearAndSetSemantics { }) {
        val gap = 4.dp.toPx()
        val w = (size.width - gap * (count - 1)) / count
        val r = CornerRadius(size.height / 2f)
        for (i in 0 until count) {
            val x = i * (w + gap)
            drawRoundRect(Color.White.copy(alpha = 0.28f), Offset(x, 0f), Size(w, size.height), r)
            val fill = when {
                i < index -> 1f
                i == index -> progress().coerceIn(0f, 1f)
                else -> 0f
            }
            if (fill > 0f) drawRoundRect(Brand.Gold, Offset(x, 0f), Size(w * fill, size.height), r)
        }
    }
}

/**
 * Картата „Моята читателска година“ в „Моето“: през декември и януари — голяма, тъмна, със
 * златната година и шевица; през останалото време — скромен ред.
 */
@Composable
fun YearStoryCard(summary: ReadingYear.Summary, prominent: Boolean, onOpen: () -> Unit, modifier: Modifier = Modifier) {
    val title = stringResource(R.string.year_title_with, summary.year)
    if (prominent) {
        val shape = MaterialTheme.shapes.large
        Surface(
            onClick = onOpen,
            shape = shape,
            color = Brand.Ink,
            contentColor = Brand.Parchment,
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp).border(1.dp, Brand.Gold.copy(alpha = 0.55f), shape),
        ) {
            Box(
                Modifier.drawWithCache {
                    val bg = Brush.linearGradient(listOf(Brand.Burgundy, Color(0xFF3A1A18), Brand.Ink), Offset.Zero, Offset(size.width, size.height))
                    val side = size.height * 1.3f
                    val motif = Path().apply {
                        fillType = PathFillType.EvenOdd
                        addShevitsaMotif(ShevitsaMotif.ROSETTE, size.width - side * 0.62f, -side * 0.15f, side)
                    }
                    onDrawBehind {
                        drawRect(bg)
                        drawPath(motif, Brand.Gold.copy(alpha = 0.10f))
                    }
                },
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(summary.year.toString(), style = serif(56.sp, Brand.Gold, lineHeight = 1f))
                    Text(stringResource(R.string.year_title), style = serif(26.sp, Brand.Parchment))
                    Spacer(Modifier.height(6.dp))
                    Text(stringResource(R.string.year_card_text), style = sans(14.sp, Brand.GoldLight))
                    Spacer(Modifier.height(14.dp))
                    ShevitsaStrip(ShevitsaMotif.ROSETTE, Brand.Gold.copy(alpha = 0.7f), Modifier.fillMaxWidth().height(8.dp))
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            pluralStringResource(R.plurals.history_year_count, summary.books, summary.books),
                            style = sans(14.sp, Brand.Parchment, FontWeight.SemiBold), modifier = Modifier.weight(1f),
                        )
                        Surface(shape = RoundedCornerShape(50), color = Brand.Gold, contentColor = Brand.Ink) {
                            Row(Modifier.heightIn(min = 40.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.year_card_open), style = MaterialTheme.typography.labelLarge)
                                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    } else {
        Surface(
            onClick = onOpen,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Row(Modifier.heightIn(min = 72.dp).padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(48.dp).clip(MaterialTheme.shapes.small).background(Brush.linearGradient(listOf(Brand.Burgundy, Brand.Ink))),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(summary.year.toString(), style = serif(17.sp, Brand.Gold))
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleSmall)
                    Text(
                        pluralStringResource(R.plurals.year_card_books, summary.books, summary.books),
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
