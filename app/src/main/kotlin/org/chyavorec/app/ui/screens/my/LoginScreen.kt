package org.chyavorec.app.ui.screens.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import kotlinx.coroutines.delay
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.waitText
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.Contacts
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import org.chyavorec.app.ui.components.ChLoadingIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.DemoBanner
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.components.errorMessage
import org.chyavorec.app.ui.components.notAvailableMessage
import org.chyavorec.core.AppError
import org.chyavorec.core.Feature

@Composable
fun LoginScreen(onBack: () -> Unit, onLoggedIn: () -> Unit, onAddCard: () -> Unit) {
    val vm = appViewModel(key = "login") { LoginViewModel(it) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    val caps by vm.capabilities.collectAsStateWithLifecycle()
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(ui.success) {
        if (ui.success) {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            onLoggedIn()
        }
    }
    LaunchedEffect(Unit) { vm.refreshCapabilities() }
    // Временно заключен вход: обратно броене всяка секунда; при нула бутонът се отключва.
    val clock = LocalAppContainer.current.clock
    var lockLeft by remember { mutableLongStateOf(0L) }
    LaunchedEffect(ui.lockedUntilMillis) {
        while (true) {
            val left = ((ui.lockedUntilMillis - clock.now().toEpochMilli()) + 999) / 1000
            lockLeft = left.coerceAtLeast(0)
            if (left <= 0) {
                vm.lockEnded()
                break
            }
            delay(1_000)
        }
    }
    val locked = lockLeft > 0
    var showForgot by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { BackTopBar(stringResource(R.string.login_title), onBack) }) { padding ->
        Column(
            Modifier.padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(24.dp).widthIn(max = 560.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            DemoBanner(vm.isDemo)
            Emblem(84.dp)
            Text(stringResource(R.string.login_heading), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.semantics { heading() })
            val c = caps
            when {
                c == null -> ChLoadingIndicator()
                !c.login -> {
                    // Няма онлайн вход в InvLib — НЕ показваме фиктивна форма.
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.medium) {
                        Row(Modifier.padding(16.dp)) {
                            Icon(Icons.Outlined.Info, null)
                            Spacer(Modifier.size(12.dp))
                            Text(notAvailableMessage(Feature.LOGIN), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Button(onClick = onAddCard, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.Badge, null); Spacer(Modifier.size(8.dp)); Text(stringResource(R.string.card_add_manual))
                    }
                }
                else -> {
                    if (vm.isDemo) Text(stringResource(R.string.login_demo_hint), style = MaterialTheme.typography.bodySmall)
                    OutlinedTextField(
                        value = ui.cardNumber, onValueChange = vm::setCard,
                        label = { Text(stringResource(R.string.login_card)) },
                        leadingIcon = { Icon(Icons.Outlined.Badge, null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Ascii, autoCorrectEnabled = false),
                        modifier = Modifier.fillMaxWidth().semantics { contentType = ContentType.Username },
                    )
                    val err = ui.error
                    val errText = when {
                        locked -> stringResource(R.string.login_locked, waitText(lockLeft))
                        err == null -> null
                        err == AppError.Unauthorized -> stringResource(R.string.login_wrong)
                        else -> errorMessage(err)
                    }
                    OutlinedTextField(
                        value = ui.password, onValueChange = vm::setPassword,
                        label = { Text(stringResource(R.string.login_password)) },
                        leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = stringResource(if (showPassword) R.string.login_hide_password else R.string.login_show_password),
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        isError = errText != null,
                        supportingText = errText?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.NumberPassword, autoCorrectEnabled = false),
                        keyboardActions = KeyboardActions(onDone = { vm.submit() }),
                        modifier = Modifier.fillMaxWidth().semantics {
                            contentType = ContentType.Password
                            if (errText != null) error(errText)
                        },
                    )
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(ui.remember, onCheckedChange = vm::setRemember)
                        Text(stringResource(R.string.login_remember), style = MaterialTheme.typography.bodyMedium)
                    }
                    // След 3 грешни опита — колко остават до временното заключване.
                    ui.attemptsLeft?.takeIf { !locked }?.let { n ->
                        Text(
                            pluralStringResource(R.plurals.login_attempts_left, n, n),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
                        )
                    }
                    Button(
                        onClick = vm::submit,
                        enabled = !ui.submitting && !locked && ui.cardNumber.isNotBlank() && ui.password.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        if (ui.submitting) ChLoadingIndicator(Modifier.size(28.dp))
                        else Text(stringResource(R.string.action_login))
                    }
                    // „Забравен ПИН?“ винаги се вижда: възстановяване от сървъра, ако го поддържа,
                    // иначе — как се взима нов ПИН от библиотеката (с бутони за обаждане/имейл).
                    if (c.passwordReset) {
                        TextButton(onClick = vm::forgotPassword, enabled = ui.cardNumber.isNotBlank(), modifier = Modifier.heightIn(min = 48.dp)) {
                            Text(stringResource(R.string.login_forgot))
                        }
                        if (ui.resetSent) Text(stringResource(R.string.login_reset_sent), style = MaterialTheme.typography.bodySmall)
                    } else {
                        TextButton(onClick = { vm.loadContacts(); showForgot = true }, modifier = Modifier.heightIn(min = 48.dp)) {
                            Text(stringResource(R.string.login_forgot))
                        }
                    }
                    Text(stringResource(R.string.login_privacy_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
    if (showForgot) {
        val contacts by vm.contacts.collectAsStateWithLifecycle()
        ForgotPinSheet(contacts, onDismiss = { showForgot = false })
    }
}

/**
 * „Забравен ПИН?“, когато сървърът не издава нов ПИН: обяснение и бутони за обаждане
 * и имейл до библиотеката. Без известен телефон/имейл — къде да ги потърси.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForgotPinSheet(contacts: Contacts?, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val subject = stringResource(R.string.login_forgot_title)
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(stringResource(R.string.login_forgot_title), style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
            Text(notAvailableMessage(Feature.PASSWORD_RESET), style = MaterialTheme.typography.bodyLarge)
            when {
                contacts == null -> ChLoadingIndicator(Modifier.align(Alignment.CenterHorizontally))
                contacts.phones.isEmpty() && contacts.emails.isEmpty() -> Text(
                    stringResource(R.string.login_forgot_no_contacts),
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                else -> {
                    contacts.phones.firstOrNull()?.let { phone ->
                        Button(onClick = { Intents.dial(context, phone) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                            Icon(Icons.Outlined.Call, null); Spacer(Modifier.size(8.dp)); Text(stringResource(R.string.contacts_call) + " · " + phone)
                        }
                    }
                    contacts.emails.firstOrNull()?.let { email ->
                        OutlinedButton(
                            onClick = { Intents.email(context, email, subject = subject) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        ) {
                            Icon(Icons.Outlined.Email, null); Spacer(Modifier.size(8.dp)); Text(stringResource(R.string.contacts_email))
                        }
                    }
                }
            }
        }
    }
}
