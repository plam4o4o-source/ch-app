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
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Top) {
            if (showReminder) {
                RoundAction(
                    if (hasReminder) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsNone,
                    stringResource(if (hasReminder) R.string.event_remove_reminder else R.string.event_remind),
                    onReminder, active = hasReminder,
                )
            }
            if (showRoute) RoundAction(Icons.Outlined.Directions, stringResource(R.string.notif_action_route), onRoute)
            RoundAction(Icons.AutoMirrored.Outlined.OpenInNew, stringResource(R.string.event_source), onSource)
        }
    }
}

/** Кръгъл бутон (56 dp) със златен кант и етикет под него; целият стълб е докосваем и се чете като бутон. */
@Composable
private fun RoundAction(icon: ImageVector, label: String, onClick: () -> Unit, active: Boolean = false) {
    val gold = LocalExtendedColors.current.gold
    Column(
        Modifier.widthIn(min = 72.dp, max = 112.dp).clip(MaterialTheme.shapes.medium)
            .clickable(role = Role.Button, onClick = onClick).padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(56.dp).clip(CircleShape)
                .background(if (active) gold else MaterialTheme.colorScheme.surfaceContainerHigh)
                .border(1.dp, gold, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = if (active) Brand.Ink else MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(
            label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center, maxLines = 3,
        )
    }
}
