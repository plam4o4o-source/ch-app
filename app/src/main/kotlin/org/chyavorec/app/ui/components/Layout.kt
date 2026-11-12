package org.chyavorec.app.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.sectionTitle
import org.chyavorec.app.util.Intents
import org.chyavorec.app.ui.theme.AppMotion

/** Едно поле отстрани за всички екрани (заглавки на раздели, ленти, карти). */
val ScreenGutter = 16.dp

/** Етикет над заглавие (както на сайта: малки главни букви + три кръстати бода в златно). */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    // Златото на темата — следва и палитрите „Левски“/„Шевица“.
    val gold = LocalExtendedColors.current.gold
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        StitchMark(gold)
        Spacer(Modifier.width(8.dp))
        Text(text.uppercase(), style = MaterialTheme.typography.labelSmall, color = gold)
    }
}

/** Трите бода пред етикета: колко от тях и как са „избродирани“ при напредък [progress] (0..1). */
internal object CrossStitches {
    const val COUNT = 3

    /** Дял (0..1) от [index]-тия бод, избродиран при общ напредък [progress] — един след друг. */
    fun fraction(progress: Float, index: Int): Float = (progress * COUNT - index).coerceIn(0f, 1f)

    /** Колко време бродира (ms) — веднъж, при първото показване. */
    const val DURATION_MS = 520
}

/**
 * Три кръстати бода (кръстат бод — основният в шевиците), които се „бродират“ веднъж — при първото
 * показване на етикета: всеки бод първо с една, после с другата диагонала. Флагът „показан“ е
 * rememberSaveable (както [animateEntrance]) — при връщане към екрана и завъртане е веднага готов.
 * При намалено движение — веднага готови. Напредъкът се чете само при рисуване.
 */
@Composable
internal fun StitchMark(color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    var shown by rememberSaveable { mutableStateOf(false) }
    val progress = remember { Animatable(if (reduced || shown) 1f else 0f) }
    LaunchedEffect(reduced) {
        if (reduced) progress.snapTo(1f)
        else if (progress.value < 1f) progress.animateTo(1f, tween(CrossStitches.DURATION_MS, easing = FastOutSlowInEasing))
        shown = true
    }
    androidx.compose.foundation.Canvas(modifier.size(20.dp, 8.dp).clearAndSetSemantics { }) {
        val p = progress.value
        val half = 2.5.dp.toPx()
        val pitch = 7.dp.toPx()
        val stroke = 1.4.dp.toPx()
        val cy = size.height / 2f
        for (i in 0 until CrossStitches.COUNT) {
            val f = CrossStitches.fraction(p, i)
            if (f <= 0f) continue
            val cx = half + i * pitch
            val a = (f * 2f).coerceAtMost(1f)
            val b = (f * 2f - 1f).coerceAtLeast(0f)
            // „\“ отгоре надолу, после „/“ — като при истински кръстат бод.
            drawLine(
                color, Offset(cx - half, cy - half), Offset(cx - half + 2 * half * a, cy - half + 2 * half * a),
                stroke, StrokeCap.Round,
            )
            if (b > 0f) {
                drawLine(
                    color, Offset(cx + half, cy - half), Offset(cx + half - 2 * half * b, cy - half + 2 * half * b),
                    stroke, StrokeCap.Round,
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    // Бутонът вдясно стои на базовата линия на заглавието, а не на долния ръб на реда.
    Row(modifier.fillMaxWidth().padding(start = ScreenGutter, end = ScreenGutter - 12.dp)) {
        Column(Modifier.weight(1f).alignBy(LastBaseline)) {
            if (label != null) {
                SectionLabel(label)
                Spacer(Modifier.height(4.dp))
            }
            // В тъмна тема „Яворец“ заглавията са по-топли — златисто-пергаментови (heading).
            Text(title, style = MaterialTheme.typography.sectionTitle, color = LocalExtendedColors.current.heading, modifier = Modifier.semantics { heading() })
        }
        if (actionLabel != null && onAction != null) {
            // Поне 48 dp за докосване и при едър шрифт (бутонът расте с текста, но не и минимумът).
            TextButton(onClick = onAction, modifier = Modifier.alignBy(LastBaseline).heightIn(min = 48.dp)) { Text(actionLabel) }
        }
    }
}

/**
 * Заглавката на вътрешен екран. [paneLabel] — какво TalkBack обявява при отварянето на екрана
 * (обикновено заглавието; детайлите без заглавие в лентата подават името на книгата/събитието).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    paneLabel: String = title,
    /**
     * false — заглавието е скрито (статията: появява се, щом заглавието в текста мине под лентата).
     * Преливането е само в слоя — без рекомпозиция на кадър; скритото не се чете от TalkBack.
     */
    titleShown: Boolean = true,
) {
    val reduced = rememberReducedMotion()
    val titleSpec: AnimationSpec<Float> = if (reduced) snap() else AppMotion.defaultEffects()
    val titleAlpha by animateFloatAsState(if (titleShown) 1f else 0f, titleSpec, label = "bar-title")
    TopAppBar(
        modifier = Modifier.screenPane(paneLabel).nightCloth(),
        title = {
            Text(
                title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = titleAlpha
                        translationY = (1f - titleAlpha) * 6.dp.toPx()
                    }
                    .then(if (titleShown) Modifier else Modifier.clearAndSetSemantics { }),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = stringResource(R.string.action_back))
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    )
}

/**
 * Заглавка на основните раздели: логото + името на раздела + търсене вдясно —
 * еднаква за „Новини“, „Моето“ и „Още“ (както заглавката на началния екран).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onSearch: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    /** По-ниска лента при малка височина на прозореца (телефон хоризонтално). */
    expandedHeight: androidx.compose.ui.unit.Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {},
) {
    // Свита лента (enterAlways): съдържанието ѝ остава в дървото, изрязано над екрана — TalkBack
    // би стигал до невидим бутон без име. Докато е скрита повече от наполовина, е извън семантиката.
    val hidden by remember(scrollBehavior) {
        derivedStateOf { (scrollBehavior?.state?.collapsedFraction ?: 0f) > 0.5f }
    }
    val hiddenSemantics = if (hidden) Modifier.clearAndSetSemantics { } else Modifier
    TopAppBar(
        modifier = Modifier.screenPane(title).nightCloth(),
        title = {
            Row(hiddenSemantics, verticalAlignment = Alignment.CenterVertically) {
                if (!LocalRailEmblemShown.current) {
                    Emblem(34.dp)
                    Spacer(Modifier.width(12.dp))
                }
                // Заглавието на основен раздел — над заглавките на разделите под него. При едър шрифт
                // по-дългото („Новини и събития“) се смалява до 3/4, вместо да се реже („Новини и съби…“);
                // многоточие — само ако и така не стига (както етикетите на долната лента).
                val style = MaterialTheme.typography.headlineSmall
                androidx.compose.foundation.text.BasicText(
                    title,
                    style = style.copy(
                        color = LocalExtendedColors.current.heading.takeIf { it.isSpecified } ?: androidx.compose.material3.LocalContentColor.current,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    autoSize = androidx.compose.foundation.text.TextAutoSize.StepBased(
                        minFontSize = style.fontSize * 0.75f, maxFontSize = style.fontSize,
                    ),
                    modifier = Modifier.semantics { heading() },
                )
            }
        },
        actions = {
            Row(hiddenSemantics, verticalAlignment = Alignment.CenterVertically) {
                actions()
                if (onSearch != null) {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Outlined.Search, contentDescription = stringResource(R.string.action_search))
                    }
                }
            }
        },
        expandedHeight = expandedHeight,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    )
}

/** Името на екрана за TalkBack (обявява се при поява на екрана); празно — нищо. */
private fun Modifier.screenPane(label: String): Modifier =
    if (label.isBlank()) this else semantics { paneTitle = label }

/** Тънката рамка на картите (outlineVariant, леко приглушена). */
@Composable
fun cardBorder(): BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))

/**
 * Единственият вид карта в приложението: запълнена (surfaceContainerLow) с тънка рамка
 * и без сянка — сянка има само въртележката на „Начало“. С [onClick] картата е бутон.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = MaterialTheme.shapes.medium
    val color = MaterialTheme.colorScheme.surfaceContainerLow
    val m = modifier.darkTopHighlight(shape).nightCloth(shape)
    if (onClick != null) {
        Surface(onClick = onClick, shape = shape, color = color, border = cardBorder(), interactionSource = interactionSource, modifier = m) {
            Column(content = content)
        }
    } else {
        Surface(shape = shape, color = color, border = cardBorder(), modifier = m) { Column(content = content) }
    }
}

/** Карта ([AppCard]) с леко „натискане“ (micro-interaction) при докосване. */
@Composable
fun PressableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val reduced = rememberReducedMotion()
    val pressSpec: AnimationSpec<Float> = if (reduced) snap() else AppMotion.fastSpatial()
    val scale = animateFloatAsState(if (pressed && !reduced) 0.97f else 1f, pressSpec, label = "press")
    // Мащабът се чете само в graphicsLayer — пружината не прекомпозира картата на всеки кадър.
    AppCard(
        modifier = modifier.graphicsLayer { scaleX = scale.value; scaleY = scale.value },
        onClick = onClick, interactionSource = interaction,
    ) { content() }
}

/**
 * Ограничава мащаба на системния шрифт до [max] за елементи с фиксирана височина по
 * спецификация (етикетите на долната лента) — над него текстът би се рязал.
 */
@Composable
fun LimitFontScale(max: Float, content: @Composable () -> Unit) {
    val d = LocalDensity.current
    if (d.fontScale <= max) content()
    else CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = max), content = content)
}

/** Ред „етикет — стойност“. [copyable] — дълго натискане копира стойността (сигнатура, инв. №). */
@Composable
fun InfoRow(label: String, value: String?, modifier: Modifier = Modifier, copyable: Boolean = false) {
    if (value.isNullOrBlank()) return
    val copy = if (copyable) {
        val context = LocalContext.current
        val haptic = LocalHapticFeedback.current
        val copyLabel = stringResource(R.string.action_copy)
        val doCopy = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            Intents.copy(context, label, value)
        }
        // Целият ред, поне 48 dp висок — удобен за задържане; текстът е центриран по височина.
        Modifier
            .pointerInput(value) { detectTapGestures(onLongPress = { doCopy() }) }
            .semantics { onLongClick(copyLabel) { doCopy(); true } }
            .heightIn(min = 48.dp)
            .wrapContentHeight(Alignment.CenterVertically)
    } else Modifier
    Row(modifier.fillMaxWidth().then(copy).padding(vertical = 7.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.42f))
        Text(value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.58f))
    }
}

/** Скрити долна/странична лента на приложението (сцена на цял екран над раздела) — от корена. */
val LocalAppChromeHidden = compositionLocalOf<MutableState<Boolean>?> { null }

/** Страничната лента (таблет) вече показва емблемата — заглавката на раздела я пропуска (да няма две печата един до друг). */
val LocalRailEmblemShown = compositionLocalOf { false }

/** Докато е в композицията, лентите на приложението са скрити (напр. „Изненадай ме“). */
@Composable
fun HideAppChrome() {
    val hidden = LocalAppChromeHidden.current ?: return
    DisposableEffect(hidden) {
        hidden.value = true
        onDispose { hidden.value = false }
    }
}
