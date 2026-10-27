package org.chyavorec.app.ui.components

import androidx.compose.animation.core.AnimationSpec
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.onLongClick
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

/** Етикет над заглавие (както на сайта: малки главни букви + златна линия). */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    // Златото на темата — следва и палитрите „Левски“/„Шевица“.
    val gold = LocalExtendedColors.current.gold
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.foundation.Canvas(Modifier.width(18.dp).height(1.dp)) {
            drawRect(color = gold)
        }
        Spacer(Modifier.width(8.dp))
        Text(text.uppercase(), style = MaterialTheme.typography.labelSmall, color = gold)
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
            Text(title, style = MaterialTheme.typography.sectionTitle, modifier = Modifier.semantics { heading() })
        }
        if (actionLabel != null && onAction != null) {
            TextButton(onClick = onAction, modifier = Modifier.alignBy(LastBaseline)) { Text(actionLabel) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleLarge) },
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
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Emblem(34.dp)
                Spacer(Modifier.width(12.dp))
                // Заглавието на основен раздел — над заглавките на разделите под него.
                Text(
                    title, style = MaterialTheme.typography.headlineSmall, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                )
            }
        },
        actions = {
            actions()
            if (onSearch != null) {
                IconButton(onClick = onSearch) {
                    Icon(Icons.Outlined.Search, contentDescription = stringResource(R.string.action_search))
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
    val m = modifier.darkTopHighlight(shape)
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
    val scale by animateFloatAsState(if (pressed && !reduced) 0.97f else 1f, pressSpec, label = "press")
    AppCard(modifier = modifier.scale(scale), onClick = onClick, interactionSource = interaction) { content() }
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
        Modifier
            .pointerInput(value) { detectTapGestures(onLongPress = { doCopy() }) }
            .semantics { onLongClick(copyLabel) { doCopy(); true } }
    } else Modifier
    Row(modifier.fillMaxWidth().then(copy).padding(vertical = 7.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.42f))
        Text(value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.58f))
    }
}
