package org.chyavorec.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.StatusColors
import org.chyavorec.domain.model.BookStatus
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.MembershipStatus

/**
 * Плътността на фона на етикета за статус (от цвета му). Цветовете в StatusColors са
 * проверени за 4.5:1 върху този фон над всеки контейнер (SmallTextContrastTest).
 */
const val STATUS_PILL_ALPHA = 0.12f

/** Статус с цветна точка + текст (цветът никога не е единственият носител на смисъл). */
@Composable
fun StatusPill(text: String, color: Color, modifier: Modifier = Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = STATUS_PILL_ALPHA)).padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.size(8.dp).background(color, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

/**
 * Статусите върху винаги тъмна повърхност (читателската карта, заглавката в „Моето“)
 * и в светла тема: светлите им варианти — тъмнозеленото „Активно“ там е нечетимо.
 */
@Composable
fun OnDarkSurfaceStatuses(content: @Composable () -> Unit) = CompositionLocalProvider(
    LocalExtendedColors provides LocalExtendedColors.current.copy(ok = StatusColors.OkDark, warn = StatusColors.WarnDark, bad = StatusColors.BadDark),
    content = content,
)

/** Наличността с думи — за етикета и за екранния четец (корицата, редовете). */
@Composable
fun bookStatusText(status: BookStatus): String = stringResource(
    when (status) {
        BookStatus.AVAILABLE -> R.string.status_available
        BookStatus.ON_LOAN -> R.string.status_on_loan
        BookStatus.NOT_ON_SHELF -> R.string.status_not_on_shelf
        BookStatus.UNAVAILABLE -> R.string.status_unavailable
    },
)

@Composable
fun BookStatusPill(status: BookStatus, modifier: Modifier = Modifier) {
    val ext = LocalExtendedColors.current
    val color = when (status) {
        BookStatus.AVAILABLE -> ext.ok
        BookStatus.ON_LOAN, BookStatus.NOT_ON_SHELF -> ext.warn
        BookStatus.UNAVAILABLE -> ext.bad
    }
    StatusPill(bookStatusText(status), color, modifier)
}

/**
 * Наличността на екземпляра: етикетът му и — ако той не е на рафта, а други от заглавието
 * са — отделен зелен ред „Още N на рафта“ (страницата на книгата и скенерът).
 */
@Composable
fun CopyStatus(status: BookStatus, othersOnShelf: Int, modifier: Modifier = Modifier) {
    Column(modifier) {
        BookStatusPill(status)
        if (status != BookStatus.AVAILABLE && othersOnShelf > 0) {
            val ok = LocalExtendedColors.current.ok
            Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.size(6.dp).background(ok, CircleShape))
                Spacer(Modifier.width(6.dp))
                Text(pluralStringResource(R.plurals.book_more_on_shelf, othersOnShelf, othersOnShelf), style = MaterialTheme.typography.labelMedium, color = ok)
            }
        }
    }
}

/**
 * „налична“ в ред от списък с книги — като в каталога: зелена точка и малки букви, без
 * фон (етикетът [BookStatusPill] остава за „не е на рафта“ и другите, които искат внимание).
 */
@Composable
fun AvailableMark(modifier: Modifier = Modifier) {
    val ok = LocalExtendedColors.current.ok
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.size(6.dp).background(ok, CircleShape))
        Spacer(Modifier.width(5.dp))
        Text(stringResource(R.string.status_available).lowercase(), style = MaterialTheme.typography.bodySmall, color = ok, maxLines = 1)
    }
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
fun DuePill(status: DueStatus, daysLeft: Long, modifier: Modifier = Modifier, color: Color = dueColor(status)) {
    val text = when (status) {
        DueStatus.OVERDUE -> pluralStringResource(R.plurals.due_overdue_days, (-daysLeft).toInt(), (-daysLeft).toInt())
        else -> if (daysLeft == 0L) stringResource(R.string.due_today)
        else pluralStringResource(R.plurals.due_days_left, daysLeft.toInt(), daysLeft.toInt())
    }
    StatusPill(text, color, modifier)
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
