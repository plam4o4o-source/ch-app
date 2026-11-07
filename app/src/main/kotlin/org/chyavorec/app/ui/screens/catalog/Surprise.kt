package org.chyavorec.app.ui.screens.catalog

import org.chyavorec.app.ui.components.HideAppChrome
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.addShevitsaMotif
import org.chyavorec.app.ui.components.shevitsaStripPath
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.app.util.udcSectionName
import org.chyavorec.data.catalog.CatalogSearcher
import org.chyavorec.data.catalog.SurprisePicker
import org.chyavorec.data.catalog.SurpriseTastes
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.CatalogBook
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

/** Скоро предложените заглавия — за цялата сесия (процеса), не само за екрана. */
internal object SurpriseSession {
    @Volatile var recent: List<String> = emptyList()
}

/**
 * „Изненадай ме“ — един опит: [round] се сменя при всеки („Още една“ пуска анимацията наново),
 * [pick] — предложението (`null` и [loading] = false — няма налична книга).
 */
data class SurpriseUi(
    val open: Boolean = false,
    val round: Int = 0,
    val loading: Boolean = false,
    val pick: CatalogBook? = null,
) {
    val empty: Boolean get() = open && !loading && pick == null
}

class SurpriseViewModel(
    /** Вкусът на читателя ([surpriseTastes]); тестовете подават свой. */
    private val tastes: suspend (CatalogSearcher) -> SurpriseTastes,
    private val random: Random = Random.Default,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _ui = MutableStateFlow(SurpriseUi())
    val ui: StateFlow<SurpriseUi> = _ui.asStateFlow()
    private var job: Job? = null

    /** Ново предложение от [searcher] (търсачката на каталога; `null` — каталогът още не е зареден). */
    fun roll(searcher: CatalogSearcher?) {
        if (job?.isActive == true) return
        val round = _ui.value.round + 1
        _ui.value = SurpriseUi(open = true, round = round, loading = true)
        job = viewModelScope.launch {
            val pick = if (searcher == null) null else withContext(dispatcher) {
                val t = try {
                    tastes(searcher)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    SurpriseTastes()
                }
                // Грешка на индекса (базата) — „няма предложение“, не срив.
                try {
                    SurprisePicker.pick(SurprisePicker.pool(searcher, t, random), t, SurpriseSession.recent, random)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }
            }
            pick?.let { SurpriseSession.recent = SurprisePicker.remember(SurpriseSession.recent, it) }
            _ui.update { if (it.round == round) it.copy(loading = false, pick = pick) else it }
        }
    }

    fun close() {
        job?.cancel()
        _ui.update { SurpriseUi(round = it.round) }
    }
}

/**
 * Вкусът — само от данни на телефона: „Искам да прочета“, историята на четене (ако е влязъл
 * читател, от кеша — без мрежа) и последните търсения. Нищо не излиза от телефона.
 */
suspend fun surpriseTastes(c: AppContainer, searcher: CatalogSearcher): SurpriseTastes {
    val wanted = c.database.wantToRead().all()
    val history = if (c.authRepository.state.value is AuthState.SignedIn) c.libraryRepository.cachedHistory()?.data.orEmpty() else emptyList()
    val searches = c.settings.recentSearches.first()
    val books = searcher.books((wanted.map { it.inv } + history.mapNotNull { it.inv }).distinct())
    return SurpriseTastes.of(
        wanted = wanted.mapNotNull { books[it.inv] },
        readBooks = history.mapNotNull { h -> h.inv?.let(books::get) },
        history = history,
        searches = searches,
    )
}

/**
 * Разклащане на телефона: поне [peaks] удара над [thresholdG] g за [windowMs] (напред-назад),
 * после пауза [cooldownMs]. Чиста логика — сензорът е в [ShakeEffect].
 */
class ShakeDetector(
    private val thresholdG: Float = 2.3f,
    private val windowMs: Long = 700,
    private val peaks: Int = 2,
    private val cooldownMs: Long = 1_500,
) {
    private var count = 0
    private var firstPeakAt = 0L
    private var lastPeakAt = Long.MIN_VALUE / 2
    private var lastShakeAt = Long.MIN_VALUE / 2

    /** Ускорението (m/s², с гравитацията) в момента [timeMs]; `true` — разклащане. */
    fun onSample(x: Float, y: Float, z: Float, timeMs: Long): Boolean {
        val g = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH
        if (g < thresholdG || timeMs - lastShakeAt < cooldownMs) return false
        // Един и същ удар (няколко отчитания подред) се брои веднъж.
        if (timeMs - lastPeakAt < MIN_PEAK_GAP_MS) return false
        lastPeakAt = timeMs
        if (count == 0 || timeMs - firstPeakAt > windowMs) {
            count = 1
            firstPeakAt = timeMs
            return false
        }
        if (++count < peaks) return false
        count = 0
        lastShakeAt = timeMs
        return true
    }

    private companion object { const val MIN_PEAK_GAP_MS = 120L }
}

/**
 * Акселерометърът — само докато екранът е отпред (RESUMED) и [enabled]; при пауза, смяна на
 * екрана или изход се спира веднага. Без сензор (емулатор, таблет) — нищо.
 */
@Composable
fun ShakeEffect(enabled: Boolean, onShake: () -> Unit) {
    if (!enabled) return
    val context = LocalContext.current
    val current by rememberUpdatedState(onShake)
    LifecycleResumeEffect(context) {
        val sm = context.getSystemService(SensorManager::class.java)
        val sensor = sm?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val detector = ShakeDetector()
        val listener = object : SensorEventListener {
            override fun onSensorChanged(e: SensorEvent) {
                if (detector.onSample(e.values[0], e.values[1], e.values[2], e.timestamp / 1_000_000L)) current()
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        if (sm != null && sensor != null) sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onPauseOrDispose { sm?.unregisterListener(listener) }
    }
}

/** Картите в тестето и разположението им. */
private const val CARDS = 5
private const val HERO = CARDS / 2
private val CardWidth = 112.dp
private val CardHeight = CardWidth * 1.42f

/** Едно карта: отместване (dp), завъртане и прозрачност. */
private class CardAnim(alpha: Float) {
    val x = Animatable(0f)
    val y = Animatable(0f)
    val rotation = Animatable(0f)
    val alpha = Animatable(alpha)
}

/**
 * „Изненадай ме“ върху целия екран: тесте карти с гръб-шевица се разперва и разбърква
 * (пружини, ~1 s, тик при всяко разместване), после средната се обръща и излиза напред —
 * корицата на предложението (потвърждение с вибрация), под нея заглавието, „Още една“ и
 * „Към книгата“. При намалено движение или екранен четец — само плавна поява на резултата.
 */
@Composable
fun SurpriseOverlay(
    ui: SurpriseUi,
    reduced: Boolean,
    onOpen: (CatalogBook) -> Unit,
    onAgain: () -> Unit,
    onClose: () -> Unit,
) {
    BackHandler(onBack = onClose)
    // Сцената е на цял екран — без долната лента на приложението под нея.
    HideAppChrome()
    val haptic = LocalHapticFeedback.current
    val cards = remember(ui.round) { List(CARDS) { CardAnim(if (reduced) 0f else 1f) } }
    val flip = remember(ui.round) { Animatable(if (reduced) 0f else 180f) }
    val zoom = remember(ui.round) { Animatable(if (reduced) 1.25f else 1f) }
    var revealed by remember(ui.round) { mutableStateOf(false) }
    val latest by rememberUpdatedState(ui)
    LaunchedEffect(ui.round) {
        if (reduced) {
            // Без разбъркване — само плавна поява на корицата и текста.
            snapshotFlow { latest.loading }.first { !it }
            revealed = true
            if (latest.pick != null) cards[HERO].alpha.animateTo(1f, tween(220))
            return@LaunchedEffect
        }
        val fanSpring = spring<Float>(dampingRatio = 0.68f, stiffness = Spring.StiffnessMediumLow)
        val swapSpring = spring<Float>(dampingRatio = 0.75f, stiffness = Spring.StiffnessMedium)
        fun moveTo(card: CardAnim, slot: Int, s: androidx.compose.animation.core.AnimationSpec<Float>) {
            val off = slot - HERO
            launch { card.x.animateTo(off * FAN_STEP, s) }
            launch { card.y.animateTo(abs(off) * FAN_DROP, s) }
            launch { card.rotation.animateTo(off * FAN_TILT, s) }
        }
        // 1. Тестето се разперва ветрило.
        var slots = IntArray(CARDS) { it }
        cards.forEachIndexed { i, c -> moveTo(c, slots[i], fanSpring) }
        delay(260)
        // 2. Три размествания — по две карти си сменят местата (пружината продължава с инерцията).
        val random = Random(ui.round)
        repeat(3) {
            val a = random.nextInt(CARDS)
            val b = (a + 1 + random.nextInt(CARDS - 1)) % CARDS
            slots = slots.copyOf().also { s -> val t = s[a]; s[a] = s[b]; s[b] = t }
            haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
            moveTo(cards[a], slots[a], swapSpring)
            moveTo(cards[b], slots[b], swapSpring)
            // Повдигане на разместваните — „прескачат“ една през друга.
            launch { cards[a].y.animateTo(-18f, swapSpring); cards[a].y.animateTo(abs(slots[a] - HERO) * FAN_DROP, swapSpring) }
            delay(160)
        }
        // 3. Събират се в купчина; средната (предложението) — отгоре.
        cards.forEach { c -> moveTo(c, HERO, fanSpring) }
        delay(200)
        snapshotFlow { latest.loading }.first { !it }
        // 4. Останалите изчезват, предложението се обръща и излиза напред.
        cards.forEachIndexed { i, c -> if (i != HERO) launch { c.alpha.animateTo(0f, tween(180)) } }
        if (latest.pick != null) {
            launch { zoom.animateTo(1.25f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow)) }
            launch { cards[HERO].y.animateTo(-6f, fanSpring) }
            flip.animateTo(90f, tween(150))
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            flip.animateTo(0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium))
        }
        revealed = true
    }

    val status = when {
        ui.pick != null && revealed -> stringResource(R.string.surprise_announce, ui.pick.title, ui.pick.author.ifBlank { "—" })
        ui.empty && revealed -> stringResource(R.string.surprise_empty)
        else -> stringResource(R.string.surprise_shuffling)
    }
    // Тъмната сцена се появява за ~0.16 s; зад тестето — топъл отблясък (бордо и злато).
    val appear = remember { Animatable(0f) }
    LaunchedEffect(Unit) { appear.animateTo(1f, tween(160)) }
    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = appear.value }
            .drawWithCache {
                val glow = Brush.radialGradient(
                    0f to Brand.Burgundy.copy(alpha = 0.55f),
                    0.45f to Brand.Burgundy.copy(alpha = 0.18f),
                    1f to Brand.Ink.copy(alpha = 0f),
                    center = Offset(size.width / 2f, size.height * 0.22f),
                    radius = size.width * 0.85f,
                )
                onDrawBehind {
                    drawRect(Brand.Ink)
                    drawRect(glow)
                }
            }
            // Докосване извън картата затваря; списъкът отдолу не получава докосванията. Без семантика —
            // с TalkBack се затваря с бутона „Затвори“ или „Назад“.
            .pointerInput(Unit) { detectTapGestures { onClose() } }
            .testTag("surprise"),
    ) {
        Column(
            Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(40.dp))
            // Тестето: 5 карти една върху друга (средната — последна, най-отгоре).
            Box(
                Modifier
                    .size(CardWidth * 2.6f, CardHeight * 1.35f)
                    // Докосване на корицата — към книгата (за TalkBack е бутонът „Към книгата“).
                    .pointerInput(ui.round) { detectTapGestures { if (revealed) latest.pick?.let(onOpen) } },
                contentAlignment = Alignment.Center,
            ) {
                val order = (0 until CARDS).filter { it != HERO } + HERO
                order.forEach { i ->
                    val c = cards[i]
                    val hero = i == HERO
                    Box(
                        Modifier
                            .size(CardWidth, CardHeight)
                            .graphicsLayer {
                                translationX = c.x.value * density
                                translationY = c.y.value * density
                                rotationZ = c.rotation.value
                                alpha = c.alpha.value
                                if (hero) {
                                    rotationY = flip.value
                                    cameraDistance = 14f * density
                                    scaleX = zoom.value
                                    scaleY = zoom.value
                                }
                                shadowElevation = 10f * density
                                shape = CardShape
                                clip = true
                            }
                            .clearAndSetSemantics { },
                    ) {
                        val pick = ui.pick
                        if (hero && pick != null && (flip.value < 90f) && (!reduced || revealed)) {
                            BookCover(pick, width = CardWidth, showStatusDot = false, decorative = true)
                        } else if (!reduced) {
                            CardBack(Modifier.fillMaxSize())
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            // TalkBack: възелът е тук от началото — смяната („Предложение: …“) се казва веднага.
            Box(Modifier.size(1.dp).semantics { liveRegion = LiveRegionMode.Assertive; contentDescription = status })
            AnimatedVisibility(
                visible = revealed,
                enter = if (reduced) fadeIn(tween(220)) else fadeIn(tween(220)) + slideInVertically(tween(260)) { it / 6 },
            ) {
                val pick = ui.pick
                if (pick != null) {
                    SurpriseDetails(pick, onOpen = { onOpen(pick) }, onAgain = onAgain)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(R.string.surprise_empty), color = Brand.Cream, textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).systemBarsPadding().padding(8.dp)) {
            Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.action_close), tint = Brand.Cream)
        }
    }
}

private const val FAN_STEP = 46f
private const val FAN_DROP = 9f
private const val FAN_TILT = 9f
private val CardShape = RoundedCornerShape(10.dp)

/** Заглавието, авторът, разделът и бутоните под обърнатата карта. */
@Composable
private fun SurpriseDetails(book: CatalogBook, onOpen: () -> Unit, onAgain: () -> Unit) {
    Column(
        Modifier.widthIn(max = 420.dp).fillMaxWidth()
            // Докосванията тук не затварят изненадата.
            .pointerInput(Unit) { detectTapGestures { } },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(Modifier.clearAndSetSemantics { }, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(R.string.surprise_label), color = Brand.Gold,
                style = MaterialTheme.typography.labelLarge, letterSpacing = 1.5.sp,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                book.title, color = Brand.Cream, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center,
                maxLines = 3, overflow = TextOverflow.Ellipsis,
            )
            if (book.author.isNotBlank()) {
                Text(book.author, color = Brand.GoldLight, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, maxLines = 2)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                listOfNotNull(book.udcSection?.let { udcSectionName(it) }, stringResource(R.string.status_available).lowercase()).joinToString("  ·  "),
                color = Brand.Cream.copy(alpha = 0.75f), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onAgain,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Brand.GoldLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, Brand.Gold.copy(alpha = 0.7f)),
                modifier = Modifier.weight(1f, fill = false),
            ) {
                Icon(Icons.Outlined.Casino, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.surprise_again), maxLines = 1)
            }
            Button(
                onClick = onOpen,
                colors = ButtonDefaults.buttonColors(containerColor = Brand.Gold, contentColor = Brand.Ink),
                modifier = Modifier.weight(1f, fill = false),
            ) {
                Text(stringResource(R.string.surprise_open), fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
        }
    }
}

/**
 * Гърбът на картата: бордо, тънка златна рамка, ромбът-шевица в средата и ленти с кривулица
 * горе и долу. Пътищата — веднъж за размера (drawWithCache).
 */
@Composable
private fun CardBack(modifier: Modifier) {
    Spacer(
        modifier.drawWithCache {
            val w = size.width
            val h = size.height
            val inset = 7.dp.toPx()
            val band = 14.dp.toPx()
            val strip = shevitsaStripPath(ShevitsaMotif.ZIGZAG, w - 2 * inset - 8.dp.toPx(), band)
            val center = Path().apply {
                fillType = PathFillType.EvenOdd
                val s = w * 0.46f
                addShevitsaMotif(ShevitsaMotif.RHOMBUS, (w - s) / 2f, (h - s) / 2f, s)
            }
            val corners = Path().apply {
                fillType = PathFillType.EvenOdd
                val s = w * 0.12f
                addShevitsaMotif(ShevitsaMotif.ROSETTE, (w - s) / 2f, h / 2f - w * 0.23f - s * 1.3f, s)
                addShevitsaMotif(ShevitsaMotif.ROSETTE, (w - s) / 2f, h / 2f + w * 0.23f + s * 0.3f, s)
            }
            val line = Stroke(width = 1.2.dp.toPx())
            val radius = CornerRadius(6.dp.toPx())
            onDrawBehind {
                drawRect(Brand.Burgundy)
                drawRoundRect(Brand.Gold.copy(alpha = 0.8f), Offset(inset, inset), Size(w - 2 * inset, h - 2 * inset), radius, style = line)
                translate(inset + 4.dp.toPx(), inset + 5.dp.toPx()) { drawPath(strip, Brand.Gold.copy(alpha = 0.85f)) }
                translate(inset + 4.dp.toPx(), h - inset - 5.dp.toPx() - band) { drawPath(strip, Brand.Gold.copy(alpha = 0.85f)) }
                drawPath(center, Brand.Gold)
                drawPath(corners, Brand.GoldLight.copy(alpha = 0.7f))
            }
        },
    )
}

/** Бутонът в горната лента на каталога. */
@Composable
fun SurpriseButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Outlined.Casino, contentDescription = stringResource(R.string.surprise_action))
    }
}
