package org.chyavorec.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.util.Intents
import org.chyavorec.domain.model.Contacts

/**
 * Връзка с библиотеката: заглавие, обяснение и бутони за обаждане и имейл (първият
 * телефон/имейл от контактите на сайта или резервните). Без известен телефон/имейл —
 * къде да ги потърси. Ползва се от „Забравен ПИН?“ и „Попитай библиотеката“ при книга.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryContactSheet(
    title: String,
    text: String,
    contacts: Contacts?,
    onDismiss: () -> Unit,
    emailSubject: String = title,
    emailBody: String? = null,
) {
    val context = LocalContext.current
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
            Text(text, style = MaterialTheme.typography.bodyLarge)
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
                            onClick = { Intents.email(context, email, subject = emailSubject, body = emailBody) },
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
