package org.chyavorec.app.ui.screens.catalog

import android.os.Build
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.HideAppChrome
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.util.udcSectionName
import org.chyavorec.domain.model.CatalogBook
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.random.Random

/**
 * Как се показва „Изненадай ме“: [FULL] — тестето с 3D обръщане и сенки на слоя; [LITE] — същото
 * разбъркване, но без перспектива и без сенки на слоя (Android 8–11: HWUI там преизчислява сянката
 * и изрязването при всеки кадър под 3D матрица — паметта растеше до 1,5 GB); [STILL] — без
 * разбъркване, само плавна поява на резултата (намалено движение, екранен четец).
 */
enum class SurpriseMotion { FULL, LITE, STILL }

/** Най-старият Android, на който разбъркването е проверено на емулатор (паметта остава равна). */
internal const val MIN_SHUFFLE_SDK = Build.VERSION_CODES.O

/** Изборът на [SurpriseMotion] за устройството ([sdk]) и настройките на достъпността. */
fun surpriseMotion(sdk: Int, reducedMotion: Boolean, screenReader: Boolean): SurpriseMotion = when {
    reducedMotion || screenReader || sdk < MIN_SHUFFLE_SDK -> SurpriseMotion.STILL
    sdk < Build.VERSION_CODES.S -> SurpriseMotion.LITE
    else -> SurpriseMotion.FULL
}

/** Картите в тестето и разположението им. */
private const val CARDS = 5
private const val HERO = CARDS / 2
private val CardWidth = 112.dp
private val CardHeight = CardWidth * 1.42f
private const val FAN_STEP = 46f
private const val FAN_DROP = 9f
private const val FAN_TILT = 9f

/** Запазеното място на резултата под тестето (един ред заглавие, автор, бутони). */
private val DetailsMinHeight = 200.dp

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
 * „Към книгата“. При [SurpriseMotion.STILL] или вече показан резултат ([SurpriseUi.revealed]) —
 * само плавна поява. Жестът „назад“ смалява и избледнява сцената; пуснат — затваря, отказан — връща.
 */
@Composable
fun SurpriseOverlay(
    ui: SurpriseUi,
    motion: SurpriseMotion,
    onOpen: (CatalogBook) -> Unit,
    onAgain: () -> Unit,
    onClose: () -> Unit,
    onRevealed: (round: Int) -> Unit = {},
) {
    val reducedMotion = rememberReducedMotion()
    val scope = rememberCoroutineScope()
    // Напредъкът на жеста „назад“ (0..1).
    val back = remember { Animatable(0f) }
    PredictiveBackHandler { events ->
        try {
            events.collect { back.snapTo(it.progress) }
            onClose()
        } catch (e: CancellationException) {
            scope.launch { back.animateTo(0f, spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)) }
            throw e
        }
    }
    // Сцената е на цял екран — без долната лента на приложението под нея.
    HideAppChrome()
    val haptic = LocalHapticFeedback.current
    // Без разбъркване: намалено движение / екранен четец, или резултатът вече е бил показан.
    val still = remember(ui.round) { motion == SurpriseMotion.STILL || ui.revealed }
    val lite = motion == SurpriseMotion.LITE
    val cards = remember(ui.round) { List(CARDS) { CardAnim(if (still) 0f else 1f) } }
    val flip = remember(ui.round) { Animatable(if (still) 0f else 180f) }
    val zoom = remember(ui.round) { Animatable(if (still) 1.25f else 1f) }
    // Лицевата страна се вижда под 90° — чете се при смяна, не при всеки кадър.
    val front by remember(ui.round) { derivedStateOf { flip.value < 90f } }
    var revealed by remember(ui.round) { mutableStateOf(false) }
    val latest by rememberUpdatedState(ui)
    LaunchedEffect(ui.round) {
        if (still) {
            // Без разбъркване — само плавна поява на корицата и текста.
            snapshotFlow { latest.loading }.first { !it }
            revealed = true
            onRevealed(ui.round)
            if (latest.pick != null) cards[HERO].alpha.animateTo(1f, tween(220))
            return@LaunchedEffect
        }
        val fanSpring = spring<Float>(dampingRatio = 0.68f, stiffness = Spring.StiffnessMediumLow)
        val swapSpring = spring<Float>(dampingRatio = 0.75f, stiffness = Spring.StiffnessMedium)
        fun moveTo(card: CardAnim, slot: Int, s: AnimationSpec<Float>) {
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
        onRevealed(ui.round)
    }

    val status = when {
        ui.pick != null && revealed -> stringResource(R.string.surprise_announce, ui.pick.title, ui.pick.author.ifBlank { "—" })
        ui.empty && revealed -> stringResource(R.string.surprise_empty)
        else -> stringResource(R.string.surprise_shuffling)
    }
    // Заглавието на „прозореца“ за TalkBack: сцената, после резултатът.
    val pane = stringResource(if (revealed && ui.pick != null) R.string.surprise_label else R.string.surprise_action)
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
                    center = Offset(if (size.width >= size.height * 1.2f) size.width * 0.32f else size.width / 2f, size.height * 0.4f),
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
            .semantics { paneTitle = pane }
            .testTag("surprise"),
    ) {
        // Сцената е по средата на екрана: тестето и под него резултатът — като група, центрирана
        // вертикално (телефон и таблет, изправено) или тестето отляво и резултатът отдясно (легнало).
        // Мястото на резултата е запазено още при разбъркването — тестето не „скача“ при появата му.
        BoxWithConstraints(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Жестът „назад“: сцената се смалява (до 0,9) и избледнява; при намалено движение — само избледнява.
                    val p = back.value
                    alpha = 1f - 0.5f * p
                    if (!reducedMotion) {
                        scaleX = 1f - 0.1f * p
                        scaleY = 1f - 0.1f * p
                    }
                }
                .systemBarsPadding(),
        ) {
            val sideBySide = maxWidth >= 700.dp && maxWidth > maxHeight
            val viewport = maxHeight
            val deck: @Composable () -> Unit = {
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
                                    if (lite) {
                                        // Без перспектива: обръщането — свиване по ширина (cos на ъгъла).
                                        scaleX = zoom.value * abs(cos(flip.value * PI.toFloat() / 180f))
                                    } else {
                                        rotationY = flip.value
                                        cameraDistance = 14f * density
                                        scaleX = zoom.value
                                    }
                                    scaleY = zoom.value
                                }
                                if (!lite) {
                                    shadowElevation = 10f * density
                                    shape = CardShape
                                    clip = true
                                }
                            }
                            // Леко: нарисувана сянка зад картата и изрязване в отделен слой без трансформации.
                            .then(if (lite) Modifier.cardShadow().clip(CardShape) else Modifier)
                            .clearAndSetSemantics { },
                    ) {
                        val pick = ui.pick
                        if (hero && pick != null && front && (!still || revealed)) {
                            BookCover(pick, width = CardWidth, showStatusDot = false, decorative = true)
                        } else if (!still) {
                            CardBack(Modifier.fillMaxSize())
                        }
                    }
                }
            }
            }
            val details: @Composable () -> Unit = {
                // Запазено място: резултатът (заглавие, автор, бутони) расте надолу, без да мести тестето.
                Box(Modifier.heightIn(min = DetailsMinHeight), contentAlignment = Alignment.TopCenter) {
            // TalkBack: възелът е тук от началото — смяната („Предложение: …“) се казва веднага.
            Box(Modifier.size(1.dp).semantics { liveRegion = LiveRegionMode.Assertive; contentDescription = status })
            AnimatedVisibility(
                visible = revealed,
                enter = if (still) fadeIn(tween(220)) else fadeIn(tween(220)) + slideInVertically(tween(260)) { it / 6 },
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
            }
            if (sideBySide) {
                Row(
                    Modifier.fillMaxSize().padding(start = 32.dp, end = 32.dp, bottom = 40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                ) {
                    deck()
                    Box(Modifier.weight(1f, fill = false).widthIn(max = 420.dp).verticalScroll(rememberScrollState())) { details() }
                }
            } else {
                // Под тестето — празно място за лентата „няма връзка“ (долу), та групата да е малко над средата.
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .heightIn(min = viewport)
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 56.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    deck()
                    Spacer(Modifier.height(20.dp))
                    details()
                }
            }
        }
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).systemBarsPadding().padding(8.dp)) {
            Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.action_close), tint = Brand.Cream)
        }
    }
}

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

/** Бутонът в горната лента на каталога. */
@Composable
fun SurpriseButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Outlined.Casino, contentDescription = stringResource(R.string.surprise_action))
    }
}
