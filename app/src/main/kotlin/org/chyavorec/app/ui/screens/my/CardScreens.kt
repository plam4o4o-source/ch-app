package org.chyavorec.app.ui.screens.my

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.WindowManager
import android.content.Context
import android.provider.Settings
import android.view.Window
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.launch
import org.chyavorec.app.ui.components.rememberReducedMotion
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
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

/** Наклон на устройството (−1..1 по двете оси), нискочестотно филтриран; (0, 0) без сензор. */
private class Tilt {
    var x by mutableFloatStateOf(0f)
    var y by mutableFloatStateOf(0f)
}

/**
 * Чете вектора на завъртане (≈30 Hz) и го изглажда — за „живия“ отблясък на
 * картата. Не прави нищо без сензор или при намалено движение; спира при напускане.
 */
@Composable
private fun rememberTilt(): Tilt {
    val context = LocalContext.current
    val reduced = rememberReducedMotion()
    val tilt = remember { Tilt() }
    DisposableEffect(reduced) {
        if (reduced) return@DisposableEffect onDispose {}
        val manager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val sensor = manager?.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) ?: manager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (manager == null || sensor == null) return@DisposableEffect onDispose {}
        val rotation = FloatArray(9)
        val orientation = FloatArray(3)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                runCatching {
                    SensorManager.getRotationMatrixFromVector(rotation, event.values)
                    SensorManager.getOrientation(rotation, orientation)
                    // pitch (напред/назад) и roll (наляво/надясно) в радиани → ±1 при ≈ ±45°.
                    val ty = (orientation[1] / 0.8f).coerceIn(-1f, 1f)
                    val tx = (orientation[2] / 0.8f).coerceIn(-1f, 1f)
                    tilt.x += (tx - tilt.x) * 0.18f
                    tilt.y += (ty - tilt.y) * 0.18f
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        val registered = runCatching { manager.registerListener(listener, sensor, 33_000) }.getOrDefault(false)
        onDispose { if (registered) manager.unregisterListener(listener) }
    }
    return tilt
}

/** Диагонален отблясък (бяло 12 % → прозрачно), който се мести с наклона на телефона. */
private fun Modifier.tiltGloss(tilt: Tilt): Modifier = drawWithContent {
    drawContent()
    val w = size.width
    val h = size.height
    val tx = tilt.x
    val ty = tilt.y
    if (tx == 0f && ty == 0f) return@drawWithContent
    // Центърът на ивицата върви по диагонала според наклона; краищата остават прозрачни.
    val center = (0.5f + 0.35f * tx - 0.2f * ty).coerceIn(0.12f, 0.88f)
    drawRect(
        Brush.linearGradient(
            colorStops = arrayOf(
                0f to Color.Transparent,
                (center - 0.12f).coerceAtLeast(0f) to Color.Transparent,
                center to Color.White.copy(alpha = 0.12f),
                (center + 0.12f).coerceAtMost(1f) to Color.Transparent,
                1f to Color.Transparent,
            ),
            start = Offset(0f, h * 0.25f * ty),
            end = Offset(w, h + h * 0.25f * ty),
        ),
    )
}

/** Рамката на картата (размер ID-1, мастилено-бордо градиент, златен кант) — обща за двете страни. */
@Composable
private fun CardFrame(modifier: Modifier, large: Boolean, content: @Composable ColumnScope.() -> Unit) {
    val tilt = rememberTilt()
    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(1.586f) // стандартен размер ID-1 (85.6 × 54 мм)
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Brand.Ink, Color(0xFF3A1A18), Brand.Burgundy)))
            // „Жив“ отблясък по наклона на телефона; баркодът отдолу остава неподвижен.
            .tiltGloss(tilt),
    ) {
        Box(Modifier.fillMaxSize().padding(2.dp).border(1.dp, Brand.Gold.copy(alpha = 0.45f), RoundedCornerShape(18.dp)))
        // Картата е с фиксирани пропорции: при много едър системен шрифт текстът би
        // излязъл извън нея, затова мащабът на шрифта вътре е ограничен до 1.3×.
        val density = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = density.fontScale.coerceAtMost(1.3f))) {
            Column(Modifier.fillMaxSize().padding(if (large) 24.dp else 18.dp), content = content)
        }
    }
}

/** Дигитална читателска карта — оформена като истинска библиотечна карта (лице с баркод). */
@Composable
fun LibraryCard(data: CardData, modifier: Modifier = Modifier, large: Boolean = false) {
    CardFrame(modifier, large) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Emblem(if (large) 44.dp else 34.dp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.card_library_line), color = Brand.GoldLight, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(stringResource(R.string.org_short), color = Brand.Parchment, fontFamily = Cormorant, fontSize = if (large) 20.sp else 17.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        Spacer(Modifier.weight(1f))
        Text(data.holder.ifBlank { "—" }, color = Brand.Parchment, fontFamily = Cormorant, fontSize = if (large) 28.sp else 22.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(R.string.reader_number, data.number),
                color = Brand.GoldLight, style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f),
                maxLines = 1, overflow = TextOverflow.Ellipsis,
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

/**
 * Гърбът на картата: номерът едро (за диктуване или ръчно въвеждане на гишето),
 * името на читателя и библиотеката.
 */
@Composable
fun LibraryCardBack(data: CardData, modifier: Modifier = Modifier, large: Boolean = false) {
    CardFrame(modifier, large) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Emblem(if (large) 32.dp else 26.dp)
            Spacer(Modifier.width(10.dp))
            Text(stringResource(R.string.card_library_line), color = Brand.GoldLight, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Spacer(Modifier.weight(1f))
        Text(stringResource(R.string.profile_reader_number), color = Brand.GoldLight, style = MaterialTheme.typography.labelMedium)
        // Дългите номера (до 32 знака) се смаляват, за да останат на един ред.
        val numberSize = when {
            data.number.length > 18 -> if (large) 22.sp else 18.sp
            data.number.length > 11 -> if (large) 30.sp else 24.sp
            else -> if (large) 44.sp else 34.sp
        }
        Text(
            data.number, color = Brand.Parchment, fontFamily = FontFamily.Monospace, fontSize = numberSize,
            letterSpacing = 2.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(6.dp))
        Text(data.holder.ifBlank { "—" }, color = Brand.Parchment, fontFamily = Cormorant, fontSize = if (large) 24.sp else 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.weight(1f))
        Text(
            stringResource(R.string.org_library_name), color = Brand.Parchment.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis,
        )
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
 * Карта на цял екран за сканиране на гишето: яркостта плавно се вдига до
 * максимум, екранът не заспива, баркод + QR. Картата „влиза“ с 3D обръщане, а
 * докосване я обръща към гърба (номерът едро). При намалено движение — без анимации.
 */
@Composable
fun CardFullscreen(onClose: () -> Unit) {
    val vm = accountViewModel()
    val data = rememberCardData(vm)
    val context = LocalContext.current
    val reduced = rememberReducedMotion()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val window = remember(context) { (context as? Activity)?.window }

    // Яркост: от текущата към максимална за ~300 ms вместо рязък скок.
    val brightness = remember { Animatable(initialBrightness(context, window)) }
    DisposableEffect(window) {
        val old = window?.attributes?.screenBrightness
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.let {
                it.attributes = it.attributes.apply { screenBrightness = old ?: WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE }
                it.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
    LaunchedEffect(window) {
        val w = window ?: return@LaunchedEffect
        launch {
            snapshotFlow { brightness.value }.collect { v ->
                w.attributes = w.attributes.apply { screenBrightness = v }
            }
        }
        if (reduced) {
            brightness.snapTo(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL)
        } else {
            brightness.animateTo(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL, tween(300, easing = LinearEasing))
        }
    }

    // Влизане (90° → 0°) веднъж; обръщане 0° ↔ 180° при докосване. Оцеляват при завъртане.
    var entered by rememberSaveable { mutableStateOf(false) }
    var showBack by rememberSaveable { mutableStateOf(false) }
    val entry = remember { Animatable(if (entered || reduced) 0f else 90f) }
    val flip = remember { Animatable(if (showBack) 180f else 0f) }
    val backVisible by remember { derivedStateOf { entry.value + flip.value > 90f } }
    val hasCard = data != null
    LaunchedEffect(hasCard) {
        if (!hasCard || entered) return@LaunchedEffect
        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
        if (reduced) entry.snapTo(0f) else entry.animateTo(0f, tween(350, easing = FastOutSlowInEasing))
        entered = true
    }
    val flipLabel = stringResource(R.string.card_flip)
    val flipInteraction = remember { MutableInteractionSource() }

    Box(Modifier.fillMaxSize().background(Color.White)) {
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(top = 8.dp, end = 12.dp)) {
            Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.action_close), tint = Color.Black)
        }
        if (data == null) return@Box
        BoxWithConstraints(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            val landscape = maxWidth > maxHeight
            val content: @Composable () -> Unit = {
                Box(
                    Modifier
                        .widthIn(max = 520.dp)
                        .graphicsLayer {
                            rotationY = entry.value + flip.value
                            cameraDistance = 12f * density
                        }
                        .clickable(
                            interactionSource = flipInteraction,
                            indication = null,
                            onClickLabel = flipLabel,
                            role = Role.Button,
                        ) {
                            showBack = !showBack
                            haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            val target = if (showBack) 180f else 0f
                            scope.launch {
                                if (reduced) flip.snapTo(target) else flip.animateTo(target, tween(450, easing = FastOutSlowInEasing))
                            }
                        },
                ) {
                    if (backVisible) {
                        // Гърбът е завъртян още 180°, за да не се чете огледално.
                        LibraryCardBack(data, Modifier.graphicsLayer { rotationY = 180f }, large = true)
                    } else {
                        LibraryCard(data, large = true)
                    }
                }
            }
            if (landscape) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Box(Modifier.weight(1.4f)) { content() }
                    QrCodeView(data.barcode, stringResource(R.string.card_qr_desc), Modifier.weight(0.6f).aspectRatio(1f))
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(28.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        content()
                        Text(stringResource(R.string.card_flip_hint), style = MaterialTheme.typography.bodySmall, color = Color(0xFF5A4D3D))
                    }
                    QrCodeView(data.barcode, stringResource(R.string.card_qr_desc), Modifier.size(200.dp))
                    Text(data.number, fontFamily = FontFamily.Monospace, fontSize = 22.sp, color = Color.Black)
                }
            }
        }
    }
}

/** Текущата яркост (0..1): тази на прозореца, ако е зададена, иначе системната. */
private fun initialBrightness(context: Context, window: Window?): Float {
    val own = window?.attributes?.screenBrightness ?: -1f
    if (own >= 0f) return own.coerceIn(0f, 1f)
    val system = runCatching { Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS) }.getOrNull()
    return ((system ?: 128) / 255f).coerceIn(0.05f, 1f)
}
