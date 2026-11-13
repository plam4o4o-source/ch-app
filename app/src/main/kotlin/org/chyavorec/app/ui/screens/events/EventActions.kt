package org.chyavorec.app.ui.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors

/**
 * Действията на събитието: едно основно (добави в календара) и три кръгли с етикет под тях —
 * напомняне, маршрут, публикацията. Споделянето е в горната лента. Всяко е поне 48 dp.
 */
@Composable
internal fun EventActions(
    showCalendar: Boolean,
    onCalendar: () -> Unit,
    showReminder: Boolean,
    hasReminder: Boolean,
    onReminder: () -> Unit,
    showRoute: Boolean,
    onRoute: () -> Unit,
    onSource: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        if (showCalendar) {
            Button(onClick = onCalendar, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                Icon(Icons.Outlined.CalendarMonth, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.event_add_calendar))
            }
        }
        // При едър шрифт три колони не побират думи като „публикацията“ — етикетите са отделни
        // редове на цялата ширина (бутонът отляво, текстът до него).
        val wide = LocalDensity.current.fontScale >= WIDE_ACTIONS_FONT_SCALE
        val actions: @Composable () -> Unit = {
            if (showReminder) {
                RoundAction(
                    if (hasReminder) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsNone,
                    stringResource(if (hasReminder) R.string.event_remove_reminder else R.string.event_remind),
                    onReminder, active = hasReminder, wide = wide,
                )
            }
            if (showRoute) RoundAction(Icons.Outlined.Directions, stringResource(R.string.notif_action_route), onRoute, wide = wide)
            RoundAction(Icons.AutoMirrored.Outlined.OpenInNew, stringResource(R.string.event_source), onSource, wide = wide)
        }
        if (wide) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) { actions() }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Top) { actions() }
        }
    }
}

/** От този мащаб на шрифта действията са редове на цялата ширина, не колони. */
internal const val WIDE_ACTIONS_FONT_SCALE = 1.3f

/**
 * Кръгъл бутон (56 dp) със златен кант и етикет под него (при [wide] — вдясно от него);
 * целият стълб е докосваем и се чете като бутон.
 */
@Composable
private fun RoundAction(icon: ImageVector, label: String, onClick: () -> Unit, active: Boolean = false, wide: Boolean = false) {
    val gold = LocalExtendedColors.current.gold
    if (wide) {
        Row(
            Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium)
                .clickable(role = Role.Button, onClick = onClick).padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionCircle(icon, active)
            Spacer(Modifier.width(14.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        }
        return
    }
    // Ширината расте с шрифта (112 dp при 1.0), иначе „Напомни ми“ се къса насред думата.
    val maxWidth = (112 * LocalDensity.current.fontScale.coerceIn(1f, 2f)).dp
    Column(
        Modifier.widthIn(min = 72.dp, max = maxWidth).clip(MaterialTheme.shapes.medium)
            .clickable(role = Role.Button, onClick = onClick).padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ActionCircle(icon, active)
        Spacer(Modifier.height(6.dp))
        Text(
            label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center, maxLines = 3,
        )
    }
}

@Composable
private fun ActionCircle(icon: ImageVector, active: Boolean) {
    val gold = LocalExtendedColors.current.gold
    Box(
        Modifier.size(56.dp).clip(CircleShape)
            .background(if (active) gold else MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(1.dp, gold, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = if (active) Brand.Ink else MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
    }
}
