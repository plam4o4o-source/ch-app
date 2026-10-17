package org.chyavorec.app.ui.screens.my

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.DuePill
import org.chyavorec.app.ui.components.rememberToday
import org.chyavorec.app.util.Formatters
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.DueStatus
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanSummary

/**
 * Кешираните заемания на влезлия читател — САМО от шифрования кеш, без мрежа
 * (за „Начало“ и плочката „Моите книги“). Чете се наново при вход/изход и при
 * всяко връщане към екрана (напр. след „Моите книги“ или фоновото опресняване).
 * `null` — не е влязъл или още няма кеширани заемания.
 */
class LoanSummaryViewModel(private val c: AppContainer) : ViewModel() {
    private val _loans = MutableStateFlow<List<Loan>?>(null)
    val loans: StateFlow<List<Loan>?> = _loans.asStateFlow()

    init {
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            c.authRepository.state.collect { reload() }
        }
    }

    fun reload() {
        viewModelScope.launch {
            val signedIn = c.authRepository.state.value is AuthState.SignedIn
            _loans.value = if (signedIn) runCatching { c.libraryRepository.cachedLoans()?.data }.getOrNull() else null
        }
    }
}

/** Кешираните заемания на влезлия читател (`null` — няма вход/кеш). */
@Composable
fun rememberCachedLoans(): List<Loan>? {
    val vm = appViewModel(key = "loan-summary") { LoanSummaryViewModel(it) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { vm.reload() }
    val loans by vm.loans.collectAsStateWithLifecycle()
    return loans
}

/** Обобщението за днес (`null` — няма вход или заети книги: картата не се показва). */
@Composable
fun rememberLoanSummary(): LoanSummary? {
    val loans = rememberCachedLoans()
    val today = rememberToday()
    return remember(loans, today) { LoanSummary.of(loans, today) }
}

/**
 * Кратко описание за плочката „Моите книги“: следващият срок или колко книги са
 * просрочени. `null` — няма данни (тогава плочката показва общото описание).
 */
@Composable
fun loanSummaryLine(summary: LoanSummary?, loans: List<Loan>?): String? {
    val context = LocalContext.current
    return when (summary) {
        is LoanSummary.Overdue -> pluralStringResource(R.plurals.home_loans_overdue, summary.count, summary.count)
        is LoanSummary.NextDue -> stringResource(R.string.my_books_next_due, Formatters.dayMonth(context, summary.dueOn))
        is LoanSummary.NoDueDate -> pluralStringResource(R.plurals.home_loans_total, summary.total, summary.total)
        null -> if (loans != null && loans.isEmpty()) stringResource(R.string.loans_empty) else null
    }
}

/**
 * Картата „Следващ срок / Просрочено“ на „Начало“: една книга и срокът ѝ или броят
 * просрочени. Цялата карта е бутон към „Моите книги“.
 */
@Composable
fun LoanDueCard(summary: LoanSummary, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val overdue = summary is LoanSummary.Overdue
    val container = if (overdue) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceContainerHigh
    val onContainer = if (overdue) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
    // Върху errorContainer етикетът и знакът са в onErrorContainer: „error“ в тъмната тема е твърде блед там.
    val accent = if (overdue) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.primary
    val shape = MaterialTheme.shapes.medium
    val openLabel = stringResource(R.string.home_loans_open)
    Surface(
        color = container,
        contentColor = onContainer,
        shape = shape,
        modifier = modifier.fillMaxWidth().heightIn(min = 48.dp).clip(shape)
            .clickable(onClickLabel = openLabel, role = Role.Button, onClick = onClick)
            // Етикет, текст и срок се четат като едно цяло.
            .semantics(mergeDescendants = true) {},
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (overdue) Icons.Outlined.ErrorOutline else Icons.Outlined.CollectionsBookmark,
                contentDescription = null, tint = accent,
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    stringResource(if (overdue) R.string.home_loans_label_overdue else R.string.home_loans_label_next),
                    style = MaterialTheme.typography.labelMedium, color = accent,
                )
                when (summary) {
                    is LoanSummary.Overdue -> {
                        Text(
                            pluralStringResource(R.plurals.home_loans_overdue, summary.count, summary.count),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        // При една книга — коя е и с колко дни; при няколко — само броят (подробно в „Моите книги“).
                        if (summary.count == 1) {
                            Text(summary.first.title, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            // Върху червения фон на картата — в цвета на текста ѝ (BadDark там е под 4.5:1).
                            DuePill(DueStatus.OVERDUE, -summary.daysOverdue, Modifier.padding(top = 4.dp), color = accent)
                        }
                    }
                    is LoanSummary.NextDue -> {
                        Text(
                            stringResource(R.string.home_loans_return_by, Formatters.dayMonth(context, summary.dueOn), summary.loan.title),
                            style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis,
                        )
                        Row(Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            DuePill(summary.status, summary.daysLeft)
                            if (summary.total > 1) {
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    pluralStringResource(R.plurals.home_loans_total, summary.total, summary.total),
                                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                    is LoanSummary.NoDueDate -> Text(
                        pluralStringResource(R.plurals.home_loans_total, summary.total, summary.total),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
