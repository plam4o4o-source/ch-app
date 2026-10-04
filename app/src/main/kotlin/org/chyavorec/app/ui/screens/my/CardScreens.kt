package org.chyavorec.app.ui.screens.my

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.DemoBanner
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.components.LinearBarcode
import org.chyavorec.app.ui.components.MembershipPill
import org.chyavorec.app.ui.components.QrCodeView
import org.chyavorec.app.ui.components.SyncStamp
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.Cormorant
import org.chyavorec.data.repository.AuthState
import org.chyavorec.data.repository.SelfCardRepository
import org.chyavorec.domain.model.MembershipStatus

/** Данните, показвани върху картата. [verified] = идват от библиотечната система. */
data class CardData(
    val holder: String,
    val number: String,
    val barcode: String,
    val status: MembershipStatus?,
    val verified: Boolean,
)

@Composable
private fun rememberCardData(vm: AccountViewModel): CardData? {
    val auth by vm.authState.collectAsStateWithLifecycle()
    val profile by vm.profile.collectAsStateWithLifecycle()
    val self by vm.selfCard.collectAsStateWithLifecycle()
    val p = profile.data
    return when {
        auth is AuthState.SignedIn && p != null -> CardData(
            holder = p.fullName, number = p.cardNumber,
            barcode = p.membership?.barcodePayload ?: p.cardNumber,
            status = p.membership?.status, verified = true,
        )
        self != null -> CardData(self!!.holderName, self!!.cardNumber, self!!.cardNumber, null, verified = false)
        else -> null
    }
}

/** Дигитална читателска карта — оформена като истинска библиотечна карта. */
@Composable
fun LibraryCard(data: CardData, modifier: Modifier = Modifier, large: Boolean = false) {
    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(1.586f) // стандартен размер ID-1 (85.6 × 54 мм)
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Brand.Ink, Color(0xFF3A1A18), Brand.Burgundy))),
    ) {
        Box(Modifier.fillMaxSize().padding(2.dp).border(1.dp, Brand.Gold.copy(alpha = 0.45f), RoundedCornerShape(18.dp)))
        Column(Modifier.fillMaxSize().padding(if (large) 24.dp else 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Emblem(if (large) 44.dp else 34.dp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.card_library_line), color = Brand.GoldLight, style = MaterialTheme.typography.labelSmall)
                    Text(stringResource(R.string.org_short), color = Brand.Parchment, fontFamily = Cormorant, fontSize = if (large) 20.sp else 17.sp)
                }
            }
            Spacer(Modifier.weight(1f))
            Text(data.holder.ifBlank { "—" }, color = Brand.Parchment, fontFamily = Cormorant, fontSize = if (large) 28.sp else 22.sp, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.reader_number, data.number),
                    color = Brand.GoldLight, style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f),
                )
                data.status?.let { MembershipPill(it) }
            }
            Spacer(Modifier.height(8.dp))
            Surface(color = Color.White, shape = RoundedCornerShape(8.dp)) {
                LinearBarcode(
                    data.barcode,
                    description = stringResource(R.string.card_barcode_desc, data.number),
                    height = if (large) 70.dp else 44.dp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
fun CardScreen(onBack: () -> Unit, onFullscreen: () -> Unit, onLogin: () -> Unit) {
    val vm = accountViewModel()
    val data = rememberCardData(vm)
    val auth by vm.authState.collectAsStateWithLifecycle()
    val caps by vm.capabilities.collectAsStateWithLifecycle()
    val profile by vm.profile.collectAsStateWithLifecycle()
    var editing by rememberSaveable { mutableStateOf(false) }
    var confirmRemove by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Scaffold(topBar = { BackTopBar(stringResource(R.string.my_card), onBack) }) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(20.dp).widthIn(max = 640.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DemoBanner(vm.isDemo && data?.verified == true)
            if (data == null || editing) {
                SelfCardForm(
                    initialNumber = data?.takeIf { !it.verified }?.number.orEmpty(),
                    initialName = data?.takeIf { !it.verified }?.holder.orEmpty(),
                    onSave = { n, name, done ->
                        vm.saveSelfCard(n, name) { ok ->
                            done(ok)
                            if (ok) {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                editing = false
                            }
                        }
                    },
                    onCancel = if (data != null) ({ editing = false }) else null,
                )
                if (caps?.login == true && auth !is AuthState.SignedIn) {
                    OutlinedButton(onClick = onLogin, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.card_login_instead)) }
                }
            } else {
                LibraryCard(data, Modifier.clickable(onClick = onFullscreen))
                Button(onClick = onFullscreen, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.Fullscreen, null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.card_show))
                }
                if (data.verified) {
                    SyncStamp(profile.syncedAt)
                } else {
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.medium) {
                        Row(Modifier.padding(14.dp)) {
                            Icon(Icons.Outlined.Info, null)
                            Spacer(Modifier.width(10.dp))
                            Text(stringResource(R.string.card_self_note), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { editing = true }, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Outlined.Edit, null); Spacer(Modifier.width(6.dp)); Text(stringResource(R.string.action_edit))
                        }
                        OutlinedButton(onClick = { confirmRemove = true }, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Outlined.Delete, null); Spacer(Modifier.width(6.dp)); Text(stringResource(R.string.action_remove))
                        }
                    }
                }
                Text(stringResource(R.string.card_usage_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    if (confirmRemove) {
        AlertDialog(
            onDismissRequest = { confirmRemove = false },
            title = { Text(stringResource(R.string.card_remove_title)) },
            confirmButton = { TextButton(onClick = { confirmRemove = false; vm.removeSelfCard() }) { Text(stringResource(R.string.action_remove)) } },
            dismissButton = { TextButton(onClick = { confirmRemove = false }) { Text(stringResource(R.string.action_cancel)) } },
        )
    }
}

@Composable
private fun SelfCardForm(
    initialNumber: String,
    initialName: String,
    onSave: (String, String, (Boolean) -> Unit) -> Unit,
    onCancel: (() -> Unit)?,
) {
    var number by rememberSaveable { mutableStateOf(initialNumber) }
    var name by rememberSaveable { mutableStateOf(initialName) }
    var invalid by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.card_add_title), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics { heading() })
        Text(stringResource(R.string.card_add_text), style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = number,
            onValueChange = { v -> number = v.uppercase().filter { it in SelfCardRepository.CODE39_CHARS }.take(32); invalid = false },
            label = { Text(stringResource(R.string.card_number_label)) },
            supportingText = { Text(stringResource(if (invalid) R.string.card_number_invalid else R.string.card_number_hint)) },
            isError = invalid,
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, keyboardType = KeyboardType.Ascii, autoCorrectEnabled = false),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = name, onValueChange = { name = it.take(80) },
            label = { Text(stringResource(R.string.card_name_label)) }, singleLine = true, modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (onCancel != null) OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.action_cancel)) }
            Button(onClick = { onSave(number, name) { ok -> invalid = !ok } }, enabled = number.isNotBlank(), modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.action_save))
            }
        }
    }
}

/**
 * Карта на цял екран за сканиране на гишето: максимална яркост, екранът не
 * заспива, баркод + QR.
 */
@Composable
fun CardFullscreen(onClose: () -> Unit) {
    val vm = accountViewModel()
    val data = rememberCardData(vm)
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        val old = window?.attributes?.screenBrightness
        window?.let {
            it.attributes = it.attributes.apply { screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL }
            it.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.let {
                it.attributes = it.attributes.apply { screenBrightness = old ?: WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE }
                it.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
    Box(Modifier.fillMaxSize().background(Color.White)) {
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(top = 8.dp, end = 12.dp)) {
            Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.action_close), tint = Color.Black)
        }
        if (data == null) return@Box
        BoxWithConstraints(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            val landscape = maxWidth > maxHeight
            val content: @Composable () -> Unit = {
                LibraryCard(data, Modifier.widthIn(max = 520.dp), large = true)
            }
            if (landscape) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Box(Modifier.weight(1.4f)) { content() }
                    QrCodeView(data.barcode, stringResource(R.string.card_qr_desc), Modifier.weight(0.6f).aspectRatio(1f))
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(28.dp)) {
                    content()
                    QrCodeView(data.barcode, stringResource(R.string.card_qr_desc), Modifier.size(200.dp))
                    Text(data.number, fontFamily = FontFamily.Monospace, fontSize = 22.sp, color = Color.Black)
                }
            }
        }
    }
}
