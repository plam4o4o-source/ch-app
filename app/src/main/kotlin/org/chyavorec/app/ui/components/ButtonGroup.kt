package org.chyavorec.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.AppMotion

/**
 * Свързана група бутони (по Material 3 Expressive „connected button group“) вместо
 * сегментите: отделни „хапчета“ на 2 dp едно от друго — външните ъгли са напълно заоблени,
 * вътрешните — малки; избраният става изцяло кръгъл и плътен — в основния цвят (злато) и в
 * двете теми, като плътните бутони, а натиснатият „стяга“ вътрешните си ъгли. Един избор ([selected]) — за екранния
 * четец всеки е радиобутон в обща група. При намалено движение формите и цветът се сменят
 * без анимация.
 */
@Composable
fun <T> ConnectedButtonGroup(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemModifier: RowScope.(T) -> Modifier = { Modifier.weight(1f) },
    content: @Composable (option: T, selected: Boolean) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    Row(modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        options.forEachIndexed { i, option ->
            val isSelected = option == selected
            ConnectedButton(
                selected = isSelected,
                leading = i == 0,
                trailing = i == options.lastIndex,
                onClick = {
                    if (!isSelected) {
                        haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                        onSelect(option)
                    }
                },
                modifier = itemModifier(option),
            ) { content(option, isSelected) }
        }
    }
}

/** Пълното заобляне (височината е поне 48 dp) и вътрешните ъгли — в покой и натиснат. */
private val Full = 24.dp
private val Inner = 8.dp
private val InnerPressed = 4.dp

@Composable
private fun ConnectedButton(
    selected: Boolean,
    leading: Boolean,
    trailing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    val reduced = rememberReducedMotion()
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val spec: AnimationSpec<Dp> = if (reduced) snap() else AppMotion.fastSpatial()
    val inner = when {
        selected -> Full
        pressed -> InnerPressed
        else -> Inner
    }
    val start by animateDpAsState(if (leading) Full else inner, spec, label = "group-start")
    val end by animateDpAsState(if (trailing) Full else inner, spec, label = "group-end")
    val shape = RoundedCornerShape(topStart = start, bottomStart = start, topEnd = end, bottomEnd = end)
    val scheme = MaterialTheme.colorScheme
    val colorSpec: AnimationSpec<Color> = if (reduced) snap() else AppMotion.defaultEffects()
    // Един цвят на избора в двете теми (primary/onPrimary): ≥ 4.5:1 за текста и ≥ 3:1 спрямо фона.
    val container by animateColorAsState(if (selected) scheme.primary else scheme.surfaceContainerHigh, colorSpec, label = "group-container")
    val contentColor = if (selected) scheme.onPrimary else scheme.onSurface
    Box(
        modifier
            .heightIn(min = 48.dp)
            .clip(shape)
            .background(container)
            .selectable(selected, interactionSource = interaction, indication = ripple(), role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, LocalTextStyle provides MaterialTheme.typography.labelLarge) {
            content()
        }
    }
}

/**
 * Тоналните бутони на приложението: златният контейнер (primaryContainer) с мастилен текст —
 * като избраните филтри ([appFilterChipColors]), не бордо върху пергамент.
 */
@Composable
fun appTonalButtonColors(): ButtonColors = ButtonDefaults.filledTonalButtonColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
)

/**
 * Бутон-превключвател с форма на хапче (като другите бутони, не чип): изключен — с контур,
 * включен — тонален ([appTonalButtonColors]). За екранния четец е като FilterChip — избираем
 * (Role.Checkbox, „избрано“), с [label] като име. Поне 48 dp висок; цветът се сменя без
 * анимация при намалено движение.
 */
@Composable
fun PillToggle(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val reduced = rememberReducedMotion()
    val scheme = MaterialTheme.colorScheme
    val colorSpec: AnimationSpec<Color> = if (reduced) snap() else AppMotion.defaultEffects()
    val container by animateColorAsState(if (selected) scheme.primaryContainer else scheme.primaryContainer.copy(alpha = 0f), colorSpec, label = "pill-bg")
    val border by animateColorAsState(if (selected) scheme.outline.copy(alpha = 0f) else scheme.outline, colorSpec, label = "pill-border")
    val content = if (selected) scheme.onPrimaryContainer else scheme.primary
    Row(
        modifier
            .heightIn(min = 48.dp)
            .clip(CircleShape)
            .background(container)
            .border(1.dp, border, CircleShape)
            .selectable(selected, role = Role.Checkbox, onClick = onClick)
            .padding(start = if (leadingIcon != null) 12.dp else 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides content, LocalTextStyle provides MaterialTheme.typography.labelLarge) {
            if (leadingIcon != null) {
                Box(Modifier.size(18.dp), contentAlignment = Alignment.Center) { leadingIcon() }
                Spacer(Modifier.width(8.dp))
            }
            label()
        }
    }
}
