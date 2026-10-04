package org.chyavorec.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.MembershipStatus

/** Статус с цветна точка + текст (цветът никога не е единственият носител на смисъл). */
@Composable
fun StatusPill(text: String, color: Color, modifier: Modifier = Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.14f)).padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.size(8.dp).background(color, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Composable
fun BookStatusPill(status: BookStatus, modifier: Modifier = Modifier) {
    val ext = LocalExtendedColors.current
    val (text, color) = when (status) {
        BookStatus.AVAILABLE -> stringResource(R.string.status_available) to ext.ok
        BookStatus.ON_LOAN -> stringResource(R.string.status_on_loan) to ext.warn
        BookStatus.NOT_ON_SHELF -> stringResource(R.string.status_not_on_shelf) to ext.warn
        BookStatus.UNAVAILABLE -> stringResource(R.string.status_unavailable) to ext.bad
    }
    StatusPill(text, color, modifier)
}

@Composable
fun dueColor(status: DueStatus): Color {
    val ext = LocalExtendedColors.current
    return when (status) {
        DueStatus.PLENTY_OF_TIME -> ext.ok
        DueStatus.DUE_SOON -> ext.warn
        DueStatus.OVERDUE -> ext.bad
    }
}

@Composable
fun DuePill(status: DueStatus, daysLeft: Long, modifier: Modifier = Modifier) {
    val text = when (status) {
        DueStatus.OVERDUE -> pluralStringResource(R.plurals.due_overdue_days, (-daysLeft).toInt(), (-daysLeft).toInt())
        else -> if (daysLeft == 0L) stringResource(R.string.due_today)
        else pluralStringResource(R.plurals.due_days_left, daysLeft.toInt(), daysLeft.toInt())
    }
    StatusPill(text, dueColor(status), modifier)
}

@Composable
fun MembershipPill(status: MembershipStatus, modifier: Modifier = Modifier) {
    val ext = LocalExtendedColors.current
    val (text, color) = when (status) {
        MembershipStatus.ACTIVE -> stringResource(R.string.membership_active) to ext.ok
        MembershipStatus.EXPIRED -> stringResource(R.string.membership_expired) to ext.bad
        MembershipStatus.SUSPENDED -> stringResource(R.string.membership_suspended) to ext.warn
        MembershipStatus.UNKNOWN -> stringResource(R.string.membership_unknown) to MaterialTheme.colorScheme.onSurfaceVariant
    }
    StatusPill(text, color, modifier)
}
