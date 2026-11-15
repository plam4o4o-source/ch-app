package org.chyavorec.app.ui.screens.donate

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.FolkIconPlate
import org.chyavorec.app.ui.components.FolkIcons
import org.chyavorec.app.ui.components.FolkMotif
import org.chyavorec.app.ui.components.PageEndMark
import org.chyavorec.app.ui.components.ScreenGutter
import org.chyavorec.app.ui.components.SectionLabel
import java.io.File

/** Една избрана снимка: смаленото копие (за писмото) и миниатюрата (за екрана). */
class DonatePhoto(val file: File, val thumb: ImageBitmap)

/** Състоянието на формата — в ViewModel-а (пази се при завъртане и докато се избират снимки). */
@Stable
class DonateFormState {
    var who by mutableStateOf("")
    var time by mutableStateOf("")
    var place by mutableStateOf("")
    var contact by mutableStateOf("")
    var consent by mutableStateOf(false)
    val photos = mutableStateListOf<DonatePhoto>()
    /** Колко снимки още се смаляват. */
    var preparing by mutableIntStateOf(0)
    /** Колко не се прочетоха (не са снимки, изтекло право, твърде големи). */
    var failed by mutableIntStateOf(0)

    /** Колко още може да се добавят. */
    val room: Int get() = (DONATE_MAX_PHOTOS - photos.size - preparing).coerceAtLeast(0)

    fun form() = DonationForm(who, time, place, contact, consent)

    val canSend: Boolean get() = preparing == 0 && DonationMail.canSend(photos.size, form())
}

class DonateViewModel(private val app: Context, val to: String) : ViewModel() {
    val state = DonateFormState()

    init {
        // Старите копия (от предишни писма) — вън; после споделените от галерията, ако има.
        viewModelScope.launch(Dispatchers.IO) { runCatching { DonationPhotos.cleanup(app) } }
        add(DonateIntake.take())
    }

    /** Смалява избраните снимки една по една (извън главната нишка) — най-много до свободните места. */
    fun add(uris: List<Uri>) {
        val chosen = uris.distinct().take(state.room)
        if (chosen.isEmpty()) return
        state.failed = 0
        state.preparing += chosen.size
        viewModelScope.launch {
            for (uri in chosen) {
                val prepared = withContext(Dispatchers.IO) { DonationPhotos.prepare(app, uri) }
                state.preparing--
                if (prepared == null) state.failed++ else state.photos += DonatePhoto(prepared.file, prepared.thumb.asImageBitmap())
            }
        }
    }

    fun remove(photo: DonatePhoto) {
        state.photos.remove(photo)
        viewModelScope.launch(Dispatchers.IO) { photo.file.delete() }
    }
}

/**
 * „Дари спомен“: стари снимки за фотоархива. Изборът е през системния избор на снимки (без
 * разрешение за паметта), а писмото — през приложението за имейл на читателя.
 */
@Composable
fun DonateMemoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext
    val vm = appViewModel { DonateViewModel(app, it.fallbackContacts.emails.firstOrNull().orEmpty()) }
    val state = vm.state
    // Изборът на много снимки иска поне 2 места — за последното място е изборът на една.
    val many = remember(state.room) { ActivityResultContracts.PickMultipleVisualMedia(state.room.coerceAtLeast(2)) }
    val pickMany = rememberLauncherForActivityResult(many) { vm.add(it) }
    val pickOne = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> uri?.let { vm.add(listOf(it)) } }
    val request = PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
    DonateMemoryContent(
        state = state,
        onBack = onBack,
        onPick = { if (state.room >= 2) pickMany.launch(request) else if (state.room == 1) pickOne.launch(request) },
        onRemove = vm::remove,
        onSend = { send(context, vm.to, state) },
    )
}

private fun send(context: Context, to: String, state: DonateFormState) {
    val uris = runCatching { state.photos.map { DonationPhotos.uri(context, it.file) } }.getOrNull() ?: return
    val intent = DonationMail.sendIntent(context, to, state.form(), uris) ?: return
    try {
        context.startActivity(DonationMail.chooser(context, intent))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, R.string.error_no_app, Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun GroupTitle(text: String) = SectionLabel(
    text, Modifier.padding(start = ScreenGutter, end = ScreenGutter, top = 22.dp, bottom = 10.dp).semantics { heading() },
)

@Composable
private fun Group(title: String, content: @Composable ColumnScope.() -> Unit) {
    GroupTitle(title)
    AppCard(Modifier.fillMaxWidth().padding(horizontal = ScreenGutter)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

/** Самият екран (без ViewModel — и за снимките на екрана в тестовете). */
@Composable
fun DonateMemoryContent(
    state: DonateFormState,
    onBack: () -> Unit,
    onPick: () -> Unit,
    onRemove: (DonatePhoto) -> Unit,
    onSend: () -> Unit,
) {
    Scaffold(topBar = { BackTopBar(stringResource(R.string.donate_title), onBack) }) { padding ->
        Column(
            Modifier.padding(padding).consumeWindowInsets(padding).imePadding().fillMaxSize()
                .verticalScroll(rememberScrollState()).widthIn(max = 720.dp),
        ) {
            // Кратък топъл увод с плочката на читалището (осморъка) — единственият народен акцент.
            Row(Modifier.padding(start = ScreenGutter, end = ScreenGutter, top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                FolkIconPlate(FolkIcons.Profile, FolkMotif.ROSETTE, Modifier.clearAndSetSemantics { }, size = 52.dp)
                Spacer(Modifier.width(14.dp))
                Text(stringResource(R.string.donate_intro), style = MaterialTheme.typography.bodyLarge)
            }

            Group(stringResource(R.string.donate_photos_label)) { Photos(state, onPick, onRemove) }

            Group(stringResource(R.string.donate_about_label)) {
                Field(state.who, { state.who = it }, stringResource(R.string.donate_who), singleLine = false)
                Field(state.time, { state.time = it }, stringResource(R.string.donate_when))
                Field(state.place, { state.place = it }, stringResource(R.string.donate_where))
            }

            Group(stringResource(R.string.donate_contact_label)) {
                Field(state.contact, { state.contact = it }, stringResource(R.string.donate_contact), last = true)
            }

            Spacer(Modifier.height(12.dp))
            Consent(state.consent) { state.consent = it }

            Button(
                onClick = onSend, enabled = state.canSend,
                modifier = Modifier.fillMaxWidth().padding(horizontal = ScreenGutter).padding(top = 8.dp).height(52.dp),
            ) {
                Icon(Icons.AutoMirrored.Outlined.Send, null, Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text(stringResource(R.string.action_send))
            }
            // Защо бутонът е неактивен — или какво ще стане при натискане.
            val note = when {
                state.photos.isEmpty() && state.preparing == 0 -> stringResource(R.string.donate_need_photos)
                !state.consent -> stringResource(R.string.donate_need_consent)
                else -> stringResource(R.string.donate_send_note)
            }
            Text(
                note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = ScreenGutter + 4.dp, vertical = 10.dp).semantics { liveRegion = LiveRegionMode.Polite },
            )
            PageEndMark()
        }
    }
}

@Composable
private fun Field(value: String, onChange: (String) -> Unit, label: String, singleLine: Boolean = true, last: Boolean = false) {
    OutlinedTextField(
        value = value, onValueChange = { onChange(it.take(FIELD_MAX)) },
        label = { Text(label) },
        singleLine = singleLine, maxLines = if (singleLine) 1 else 4,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = if (last) ImeAction.Done else ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
    )
}

/** Целият ред е едно поле за отметка (TalkBack чете текста; докосване и по текста). */
@Composable
private fun Consent(checked: Boolean, onChange: (Boolean) -> Unit) {
    val label = stringResource(R.string.donate_consent)
    AppCard(Modifier.fillMaxWidth().padding(horizontal = ScreenGutter), accent = checked) {
        Row(
            Modifier.fillMaxWidth()
                .toggleable(value = checked, role = Role.Checkbox, onValueChange = onChange)
                .clearAndSetSemantics { text = AnnotatedString(label) }
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = checked, onCheckedChange = null, modifier = Modifier.padding(8.dp))
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Photos(state: DonateFormState, onPick: () -> Unit, onRemove: (DonatePhoto) -> Unit) {
    val empty = state.photos.isEmpty() && state.preparing == 0
    if (empty) {
        Text(stringResource(R.string.donate_photos_empty), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        FilledTonalButton(onClick = onPick) {
            Icon(Icons.Outlined.AddPhotoAlternate, null, Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.donate_pick))
        }
    } else {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            state.photos.forEachIndexed { i, photo -> Thumb(photo, i + 1) { onRemove(photo) } }
            repeat(state.preparing) { Preparing() }
            if (state.room > 0) AddTile(onPick)
        }
        Text(
            stringResource(R.string.donate_photos_count, state.photos.size + state.preparing, DONATE_MAX_PHOTOS),
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    if (state.failed > 0) {
        Text(
            pluralStringResource(R.plurals.donate_failed, state.failed, state.failed),
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        )
    }
}

private val ThumbSize = 92.dp

@Composable
private fun Thumb(photo: DonatePhoto, n: Int, onRemove: () -> Unit) {
    val shape = MaterialTheme.shapes.small
    Box(Modifier.size(ThumbSize)) {
        Image(
            photo.thumb, contentDescription = stringResource(R.string.photo_n, n), contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().clip(shape).border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape),
        )
        // Кръгче „×“ в ъгъла; самият бутон е с пълния размер за докосване.
        IconButton(onClick = onRemove, modifier = Modifier.align(Alignment.TopEnd)) {
            Box(Modifier.size(26.dp).background(Color.Black.copy(alpha = 0.55f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.Close, stringResource(R.string.donate_remove_photo, n), tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun Preparing() {
    val label = stringResource(R.string.donate_preparing)
    Box(
        Modifier.size(ThumbSize).clip(MaterialTheme.shapes.small).background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
    }
}

/** Плочка „Добави още“ след миниатюрите. */
@Composable
private fun AddTile(onPick: () -> Unit) {
    val shape = MaterialTheme.shapes.small
    val label = stringResource(R.string.donate_pick_more)
    Column(
        Modifier.size(ThumbSize).clip(shape).border(1.dp, MaterialTheme.colorScheme.outline, shape)
            .clickable(onClickLabel = label, role = Role.Button, onClick = onPick)
            .clearAndSetSemantics { contentDescription = label },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Outlined.AddPhotoAlternate, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
    }
}

/**
 * Връзката от фотодокументалната изложба (албумът в галерията и страницата от сайта):
 * „Имаш стари снимки? Дари спомен“ — тиха, като текстов бутон.
 */
@Composable
fun DonateMemoryLink(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(onClick = onClick, modifier = modifier) {
        Icon(Icons.Outlined.AddPhotoAlternate, null, Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.donate_exhibit_link))
    }
}

/** Най-много толкова знака в поле — писмото остава писмо. */
private const val FIELD_MAX = 500
