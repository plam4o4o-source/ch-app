package org.chyavorec.app.ui.screens.my

import org.chyavorec.app.ui.components.AppCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.clickable
import androidx.compose.ui.semantics.stateDescription
import org.chyavorec.domain.model.DueStatus
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import org.chyavorec.app.ui.theme.LocalExtendedColors
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import org.chyavorec.app.ui.components.LeafRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalResources
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.DemoBanner
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.ChLoadingIndicator
import org.chyavorec.app.ui.components.DuePill
import org.chyavorec.app.ui.components.EmptyView
import org.chyavorec.app.ui.components.OpeningBook
import org.chyavorec.app.ui.components.ErrorView
import org.chyavorec.app.ui.components.InfoRow
import org.chyavorec.app.ui.components.MembershipPill
import org.chyavorec.app.ui.components.QrCodeView
import org.chyavorec.app.ui.components.PersonalCovers
import org.chyavorec.app.ui.components.RemoteImage
import org.chyavorec.app.ui.components.SecureScreen
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.StateContent
import org.chyavorec.app.ui.components.SyncBanner
import org.chyavorec.app.ui.components.SyncStamp
import org.chyavorec.app.ui.components.errorMessage
import org.chyavorec.app.util.Formatters
import org.chyavorec.core.AppError
import org.chyavorec.core.Outcome
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.MembershipEvaluator

/** Когато няма вход — обяснение и бутон, вместо празни/фиктивни данни. */
@Composable
internal fun NeedsLogin(error: AppError, onLogin: () -> Unit) {
    if (error == AppError.Unauthorized) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            ErrorView(error, onRetry = null)
            Button(onClick = onLogin) {
                Icon(Icons.AutoMirrored.Outlined.Login, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.action_login))
            }
        }
    } else {
        ErrorView(error, onRetry = null)
    }
}

@Composable
fun LoansScreen(onBack: () -> Unit, onLogin: () -> Unit, onOpenBook: ((Long) -> Unit)? = null) {
    // Заетите книги са лични — не в „Скорошни“ и в снимки на екрана.
    SecureScreen()
    // …и без корици от интернет (адресът на корицата би издал коя книга е заета).
    PersonalCovers { LoansContent(onBack, onLogin, onOpenBook) }
}

@Composable
private fun LoansContent(onBack: () -> Unit, onLogin: () -> Unit, onOpenBook: ((Long) -> Unit)?) {
    val vm = appViewModel(key = "loans") { LoansViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val canRenew by vm.canRenew.collectAsStateWithLifecycle()
    val renewing by vm.renewing.collectAsStateWithLifecycle()
    val inCatalog by vm.inCatalog.collectAsStateWithLifecycle()
    val renew by vm.renewResult.collectAsStateWithLifecycle()
    val today = LocalAppContainer.current.clock.today()
    val calc = LoanDueCalculator()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val resources = LocalResources.current
    // Успешното удължаване — кратко съобщение и потвърждение с вибрация, без диалог за затваряне.
    LaunchedEffect(renew) {
        val r = renew as? Outcome.Success ?: return@LaunchedEffect
        vm.renewResult.value = null
        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
        val msg = if (r.value.renewPending) resources.getString(R.string.renew_requested)
        else resources.getString(R.string.renew_ok, Formatters.shortDate(context, r.value.dueOn) ?: "—")
        scope.launch {
            snackbar.currentSnackbarData?.dismiss()
            // Заявката (удължава се при следващото обновяване) е по-дълъг текст — показва се по-дълго.
            snackbar.showSnackbar(msg, duration = if (r.value.renewPending) SnackbarDuration.Long else SnackbarDuration.Short)
        }
    }

    Scaffold(topBar = { BackTopBar(stringResource(R.string.my_books), onBack) }, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        LeafRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            Column {
                DemoBanner(vm.isDemo)
                val err = state.error
                if (state.data == null && err != null && (err == AppError.Unauthorized || err is AppError.NotAvailable)) {
                    NeedsLogin(err, onLogin)
                    return@Column
                }
                SyncBanner(state.fromCache, state.syncedAt, state.refreshError)
                if (!state.fromCache && state.data != null) SyncStamp(state.syncedAt)
                StateContent(
                    state = state,
                    onRetry = { vm.refresh() },
                    isEmpty = { it.isEmpty() },
                    skeleton = { SkeletonList() },
                    empty = {
                        EmptyView(
                            stringResource(R.string.loans_empty), stringResource(R.string.loans_empty_hint),
                            icon = Icons.Outlined.CollectionsBookmark, animation = { OpeningBook(Modifier.fillMaxSize()) },
                        )
                    },
                    errorSubject = stringResource(R.string.subject_loans),
                ) { loans ->
                    // Без краен срок — най-отдолу.
                    val sorted = loans.sortedWith(compareBy(nullsLast<String>()) { it.dueOn })
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(sorted, key = { it.loanId }) { loan ->
                            // Към страницата на книгата — само ако я има в каталога (иначе „не е намерена“).
                            val open = loan.inv?.takeIf { it in inCatalog && onOpenBook != null }?.let { inv -> { onOpenBook?.invoke(inv); Unit } }
                            LoanCard(
                                loan, calc, today, stale = state.fromCache,
                                serverRenews = canRenew,
                                renewing = loan.loanId in renewing,
                                onRenew = { vm.renew(loan) },
                                onOpen = open,
                            )
                        }
                    }
                }
            }
        }
    }
    // Отказът (напр. книгата е заявена от друг читател) се чете спокойно — в диалог.
    (renew as? Outcome.Failure)?.let { r ->
        AlertDialog(
            onDismissRequest = { vm.renewResult.value = null },
            confirmButton = { TextButton(onClick = { vm.renewResult.value = null }) { Text(stringResource(R.string.action_ok)) } },
            text = { Text(errorMessage(r.error)) },
        )
    }
}

/**
 * Една заета книга. [serverRenews] — дали сървърът изобщо приема удължаване; ако тази книга
 * не може да се удължи, картата казва защо, вместо просто да няма бутон. [onOpen] (`null` —
 * книгата я няма в каталога) отваря страницата на книгата.
 */
@Composable
internal fun LoanCard(
    loan: Loan,
    calc: LoanDueCalculator,
    today: java.time.LocalDate,
    stale: Boolean,
    serverRenews: Boolean,
    renewing: Boolean,
    onRenew: () -> Unit,
    onOpen: (() -> Unit)?,
) {
    val context = LocalContext.current
    val due = LoanDueCalculator.parse(loan.dueOn)
    val status = due?.let { calc.status(it, today) }
    val overdue = status == DueStatus.OVERDUE
    val openLabel = stringResource(R.string.loan_open_book)
    AppCard(Modifier.fillMaxWidth()) {
        // Цялата карта води към книгата (ако я има в каталога); бутонът за удължаване е отделна цел.
        val open = if (onOpen != null) Modifier.clickable(onClickLabel = openLabel, role = Role.Button, onClick = onOpen) else Modifier
        Row(open.fillMaxWidth().padding(14.dp)) {
            // Същата „издателска“ корица като в каталога (на този личен екран — само генерирана, вж. PersonalCovers).
            val cover = remember(loan.loanId, loan.title, loan.author, loan.coverUrl) {
                CatalogBook(inv = loan.inv ?: 0L, author = loan.author, title = loan.title, coverUrl = loan.coverUrl.orEmpty())
            }
            BookCover(cover, width = 64.dp, showStatusDot = false)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(loan.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (loan.author.isNotBlank()) {
                    Text(loan.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    stringResource(R.string.loan_borrowed, Formatters.shortDate(context, loan.borrowedOn) ?: "—"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                // Срокът е най-важното на картата: по-едър и удебелен; просрочен — в цвета за грешка.
                val dueText = Formatters.shortDate(context, loan.dueOn) ?: "—"
                Text(
                    if (overdue) stringResource(R.string.loan_due_overdue, dueText) else stringResource(R.string.loan_due, dueText),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (overdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                )
                // Срокът с думи и колко остава — без отделна лента за изминалото време (същото трети път).
                if (status != null) DuePill(status, calc.daysLeft(due, today), Modifier.padding(top = 4.dp))
                if (loan.renewals > 0) {
                    Text(
                        pluralStringResource(R.plurals.loan_renewed_times, loan.renewals, loan.renewals),
                        style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (stale) Text(stringResource(R.string.loan_stale_note), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
                RenewState(loan)
                RenewAction(loan, serverRenews = serverRenews, stale = stale, renewing = renewing, onRenew = onRenew)
            }
        }
    }
}

/**
 * Бутонът „Удължи срока“ (с индикатор, докато заявката тече) или — ако тази книга
 * не може да се удължи онлайн — обяснение защо. Чакащо/отказано удължаване вече е
 * показано от [RenewState]; при остарели данни бутон няма (може вече да е върната).
 */
@Composable
private fun RenewAction(loan: Loan, serverRenews: Boolean, stale: Boolean, renewing: Boolean, onRenew: () -> Unit) {
    when {
        loan.renewPending || stale -> Unit
        serverRenews && loan.canRenew -> {
            val busy = stringResource(R.string.loan_renewing)
            TextButton(
                onClick = onRenew,
                enabled = !renewing,
                contentPadding = PaddingValues(horizontal = 0.dp),
                modifier = Modifier.heightIn(min = 48.dp).semantics { if (renewing) stateDescription = busy },
            ) {
                if (renewing) {
                    // Индикаторът на приложението; за екранните четци „изпраща се“ е в stateDescription на бутона.
                    ChLoadingIndicator(Modifier.size(20.dp), contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                }
                Text(stringResource(R.string.loan_renew))
            }
        }
        // Отказът от библиотеката вече казва причината — не я повтаряме.
        loan.renewResult?.isRejected == true -> Unit
        else -> Text(
            stringResource(R.string.loan_cannot_renew),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

/** Чакащо удължаване или последният отговор на библиотеката (до 7 дни). */
@Composable
private fun RenewState(loan: Loan) {
    val result = loan.renewResult
    when {
        loan.renewPending -> Surface(
            shape = RoundedCornerShape(50),
            color = Color.Transparent,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(top = 6.dp),
        ) {
            Text(
                stringResource(R.string.loan_renew_pending),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
        result?.status == "rejected" -> Text(
            result.reason?.takeIf { it.isNotBlank() }?.let { stringResource(R.string.loan_renew_rejected, it) }
                ?: stringResource(R.string.loan_renew_rejected_noreason),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 6.dp),
        )
        result?.status == "done" -> Text(
            stringResource(R.string.loan_renew_done),
            style = MaterialTheme.typography.labelMedium,
            color = LocalExtendedColors.current.ok,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}

@Composable
fun MembershipScreen(onBack: () -> Unit, onLogin: () -> Unit) {
    // QR кодът на членството е като картата — не в „Скорошни“ и в снимки на екрана.
    SecureScreen()
    val vm = appViewModel(key = "membership") { MembershipViewModel(it) }
    val state by vm.state.collectAsStateWithLifecycle()
    val today = LocalAppContainer.current.clock.today()
    val context = androidx.compose.ui.platform.LocalContext.current

    Scaffold(topBar = { BackTopBar(stringResource(R.string.my_membership), onBack) }) { padding ->
        LeafRefreshBox(state.refreshing, onRefresh = { vm.refresh() }, modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                DemoBanner(vm.isDemo)
                val err = state.error
                if (state.data == null && err != null && (err == AppError.Unauthorized || err is AppError.NotAvailable)) {
                    NeedsLogin(err, onLogin)
                    return@Column
                }
                SyncBanner(state.fromCache, state.syncedAt, state.refreshError)
                StateContent(
                    state = state, onRetry = { vm.refresh() }, isEmpty = { false },
                    skeleton = { SkeletonList(rows = 3, withImage = false) }, empty = {},
                    errorSubject = stringResource(R.string.subject_membership),
                ) { m ->
                    Column(Modifier.padding(20.dp).widthIn(max = 640.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(m.holderName, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
                        // Статусът се изчислява и от реалната дата на валидност.
                        MembershipPill(MembershipEvaluator.effectiveStatus(m, today))
                        Spacer(Modifier.height(8.dp))
                        InfoRow(stringResource(R.string.membership_number), m.memberNumber)
                        InfoRow(stringResource(R.string.membership_since), Formatters.date(context, m.since))
                        InfoRow(stringResource(R.string.membership_valid_until), Formatters.date(context, m.validUntil))
                        m.barcodePayload?.let {
                            Spacer(Modifier.height(12.dp))
                            QrCodeView(it, stringResource(R.string.card_qr_desc), Modifier.size(180.dp).align(Alignment.CenterHorizontally))
                        }
                        if (state.fromCache) Text(stringResource(R.string.loan_stale_note), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
                        else SyncStamp(state.syncedAt)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    // Име, читателски номер, имейл — не в „Скорошни“ и в снимки на екрана.
    SecureScreen()
    val vm = accountViewModel()
    val profile by vm.profile.collectAsStateWithLifecycle()
    val caps by vm.capabilities.collectAsStateWithLifecycle()
    val deletion by vm.deletion.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    var confirmDelete by remember { mutableStateOf(false) }

    Scaffold(topBar = { BackTopBar(stringResource(R.string.my_profile), onBack) }) { padding ->
        StateContent(
            state = profile, onRetry = { vm.loadProfile() }, isEmpty = { false },
            skeleton = { SkeletonList(rows = 3, withImage = false) }, empty = {},
            modifier = Modifier.padding(padding),
        ) { p ->
            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp).widthIn(max = 640.dp)) {
                DemoBanner(vm.isDemo)
                if (p.photoUrl != null) {
                    RemoteImage(p.photoUrl, stringResource(R.string.profile_photo), Modifier.size(96.dp).clip(RoundedCornerShape(50)))
                    Spacer(Modifier.height(12.dp))
                }
                Text(p.fullName.ifBlank { stringResource(R.string.my_profile) }, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
                Spacer(Modifier.height(8.dp))
                InfoRow(stringResource(R.string.profile_reader_number), p.cardNumber)
                InfoRow(stringResource(R.string.profile_category), p.category)
                InfoRow(stringResource(R.string.profile_email), p.email)
                InfoRow(stringResource(R.string.profile_registered), Formatters.date(context, p.registeredOn))
                p.membership?.let { MembershipPill(it.status, Modifier.padding(vertical = 8.dp)) }
                SyncStamp(profile.syncedAt)
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.profile_edit_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))
                if (caps?.accountDeletion == true) {
                    OutlinedButton(onClick = { confirmDelete = true }) { Text(stringResource(R.string.profile_delete_request)) }
                } else {
                    // Сървърът не поддържа искане от приложението → публичната страница за изтриване.
                    OutlinedButton(onClick = { org.chyavorec.app.util.Intents.openUrl(context, org.chyavorec.app.util.Intents.ACCOUNT_DELETION_URL) }) {
                        Text(stringResource(R.string.account_delete_web))
                    }
                }
            }
        }
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text(stringResource(R.string.profile_delete_request)) },
            text = { Text(stringResource(R.string.profile_delete_text)) },
            confirmButton = { TextButton(onClick = { confirmDelete = false; vm.requestDeletion() }) { Text(stringResource(R.string.action_send)) } },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text(stringResource(R.string.action_cancel)) } },
        )
    }
    deletion?.let { r ->
        AlertDialog(
            onDismissRequest = { vm.deletion.value = null },
            confirmButton = { TextButton(onClick = { vm.deletion.value = null }) { Text(stringResource(R.string.action_ok)) } },
            text = { Text(if (r is Outcome.Success) stringResource(R.string.profile_delete_sent) else errorMessage((r as Outcome.Failure).error)) },
        )
    }
}
