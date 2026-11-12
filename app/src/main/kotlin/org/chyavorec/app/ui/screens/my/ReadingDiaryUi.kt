package org.chyavorec.app.ui.screens.my

import android.content.Context
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.toPath
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.data.local.DiaryBook
import org.chyavorec.app.data.local.ReadingDiaryEntity
import org.chyavorec.app.data.local.ReadingDiaryStore
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.components.AppBottomSheet
import org.chyavorec.app.ui.components.TabRosetteShapes
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.navigation.DeepLinks
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareImageRequest
import org.chyavorec.app.ui.share.ShareImageState
import org.chyavorec.app.util.Formatters
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem
import org.chyavorec.domain.model.ReadingDiary
import kotlin.math.roundToInt

/**
 * Една розетка (осморъката от шевиците, както индикаторът на долната лента): пълна — оценено,
 * контур — не. Само рисуване; пътят се строи веднъж. [stitch] — пунктирният бод по пълната.
 */
@Composable
fun RosetteMark(filled: Boolean, size: Dp, fill: Color, empty: Color, stitch: Color, modifier: Modifier = Modifier) {
    Spacer(
        modifier.size(size).drawWithCache {
            val path = TabRosetteShapes.rosette.toPath().asComposePath()
            val unit = this.size.minDimension / 2f * 0.92f
            val dash = PathEffect.dashPathEffect(floatArrayOf(0.12f, 0.08f))
            val outline = Stroke(1.6.dp.toPx() / unit)
            val bod = Stroke(1.dp.toPx() / (unit * 0.78f), pathEffect = dash)
            onDrawBehind {
                translate(this.size.width / 2f, this.size.height / 2f) {
                    scale(unit, unit, pivot = Offset.Zero) {
                        if (filled) {
                            drawPath(path, fill)
                            scale(0.78f, 0.78f, pivot = Offset.Zero) { drawPath(path, stitch, style = bod) }
                        } else {
                            drawPath(path, empty, style = outline)
                        }
                    }
                }
            }
        },
    )
}

/**
 * Оценка с 1–5 розетки. За екранен четец е ЕДИН регулируем елемент (като плъзгач: „Оценка на
 * книгата, 3 от 5“, плъзване нагоре/надолу променя), а не пет бутона; докосване върху розетка
 * дава оценката й. Мишените са по 48 dp. [onRate] `null` — само показване.
 */
@Composable
fun RosetteRating(rating: Int, onRate: ((Int) -> Unit)?, modifier: Modifier = Modifier, markSize: Dp = 30.dp) {
    val label = stringResource(R.string.diary_rating_cd)
    val state = if (rating in 1..ReadingDiary.MAX_RATING) stringResource(R.string.diary_rating_state, rating) else stringResource(R.string.diary_rating_none)
    val haptic = LocalHapticFeedback.current
    val reduced = rememberReducedMotion()
    val semantic = if (onRate != null) {
        Modifier.semantics {
            contentDescription = label
            stateDescription = state
            progressBarRangeInfo = ProgressBarRangeInfo(rating.toFloat(), 0f..ReadingDiary.MAX_RATING.toFloat(), ReadingDiary.MAX_RATING - 1)
            setProgress { target ->
                val v = target.roundToInt().coerceIn(1, ReadingDiary.MAX_RATING)
                if (v != rating) onRate(v)
                true
            }
        }
    } else {
        Modifier.clearAndSetSemantics { contentDescription = "$label: $state" }
    }
    val fill = MaterialTheme.colorScheme.primary
    val empty = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
    val stitch = MaterialTheme.colorScheme.onPrimary
    Row(modifier.then(semantic)) {
        for (i in 1..ReadingDiary.MAX_RATING) {
            val filled = i <= rating
            val pop by animateFloatAsState(
                if (filled) 1f else 0.88f,
                if (reduced) snap<Float>() else spring<Float>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
                label = "rosette-$i",
            )
            Box(
                Modifier.size(48.dp)
                    .then(
                        if (onRate != null) Modifier.pointerInput(i, rating) {
                            detectTapGestures {
                                haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                onRate(i)
                            }
                        } else Modifier,
                    )
                    .graphicsLayer { scaleX = pop; scaleY = pop },
                contentAlignment = Alignment.Center,
            ) {
                RosetteMark(filled, markSize, fill, empty, stitch)
            }
        }
    }
}

/**
 * Панелът „Читателски дневник“ за една книга: въпрос „Хареса ли ти?“ с розетки и „Не сега“ (докато
 * не е отговорено — [entry] без оценка), а след оценка — бележка на ред и „Препоръчай“. Не е
 * диалог: стои в страницата, може да се игнорира. Без състояние на базата — записът е в [DiaryActions].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiaryPanel(
    entry: ReadingDiaryEntity?,
    actions: DiaryActions,
    onRecommend: (ReadingDiaryEntity) -> Unit,
    modifier: Modifier = Modifier,
    /** Показва се въпросът „Не сега“ (в историята); на страницата на книгата — не. */
    dismissible: Boolean = true,
) {
    val rated = entry != null && entry.rating in 1..ReadingDiary.MAX_RATING
    Surface(
        modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(if (rated) R.string.diary_title else R.string.diary_question),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f).semantics { heading() },
                )
                if (!rated && dismissible) {
                    TextButton(onClick = actions.dismiss) { Text(stringResource(R.string.diary_not_now)) }
                }
            }
            RosetteRating(entry?.rating ?: 0, onRate = actions.rate)
            if (rated && entry != null) {
                DiaryNote(entry.note, actions.note)
                // При едър шрифт бутоните се пренасят на нов ред, а не се смачкват.
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.Center, itemVerticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = { onRecommend(entry) }, modifier = Modifier.heightIn(min = 48.dp)) {
                        Icon(Icons.Outlined.Share, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.diary_recommend))
                    }
                    TextButton(onClick = actions.remove) { Text(stringResource(R.string.diary_remove)) }
                }
                Text(
                    stringResource(R.string.diary_private), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
                )
            }
        }
    }
}

/** Бележката на ред: текст (докосване — промяна) или „Добави бележка“; при промяна — поле до [ReadingDiary.MAX_NOTE] знака. */
@Composable
private fun DiaryNote(note: String, onSave: (String) -> Unit) {
    var editing by rememberSaveable { mutableStateOf(false) }
    var draft by rememberSaveable(note, editing) { mutableStateOf(note) }
    if (editing) {
        val save = { onSave(draft); editing = false }
        OutlinedTextField(
            value = draft,
            onValueChange = { draft = it.replace('\n', ' ').take(ReadingDiary.MAX_NOTE) },
            label = { Text(stringResource(R.string.diary_note_label)) },
            supportingText = { Text(stringResource(R.string.diary_note_count, draft.length, ReadingDiary.MAX_NOTE)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { save() }),
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
        )
        Row {
            Spacer(Modifier.weight(1f))
            TextButton(onClick = save) { Text(stringResource(R.string.action_save)) }
        }
    } else if (note.isBlank()) {
        TextButton(onClick = { editing = true }) {
            Icon(Icons.Outlined.Edit, null, Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.diary_note_add))
        }
    } else {
        TextButton(onClick = { editing = true }, modifier = Modifier.fillMaxWidth()) {
            Text(
                note, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface, maxLines = 3, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** Действията на панела — подават се отвън (запис в [ReadingDiaryStore]; в тестовете — празни). */
class DiaryActions(
    val rate: (Int) -> Unit,
    val note: (String) -> Unit,
    val dismiss: () -> Unit,
    val remove: () -> Unit,
)

/** Действията за [book], записани в [store] от обхвата на екрана. */
@Composable
fun rememberDiaryActions(store: ReadingDiaryStore, book: DiaryBook): DiaryActions {
    val scope = rememberCoroutineScope()
    return remember(store, book) {
        DiaryActions(
            rate = { scope.launch { store.rate(book, it) } },
            note = { scope.launch { store.setNote(book, it) } },
            dismiss = { scope.launch { store.dismiss(book) } },
            remove = { scope.launch { store.remove(book) } },
        )
    }
}

/**
 * Листът „Препоръчай книгата“: бележката е лична и НЕ влиза в картинката, освен ако читателят
 * изрично включи „Включи бележката“.
 */
@Composable
fun RecommendSheet(entry: ReadingDiaryEntity, onDismiss: () -> Unit, onShare: (includeNote: Boolean) -> Unit) {
    var includeNote by rememberSaveable { mutableStateOf(false) }
    AppBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 24.dp)) {
            Text(stringResource(R.string.diary_share_title), style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
            Text(entry.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp), maxLines = 2, overflow = TextOverflow.Ellipsis)
            RosetteRating(entry.rating, onRate = null, markSize = 24.dp)
            if (entry.note.isNotBlank()) {
                Row(
                    Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        .toggleable(includeNote, role = Role.Checkbox) { includeNote = it },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(includeNote, onCheckedChange = null)
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.diary_share_include_note), style = MaterialTheme.typography.bodyLarge)
                }
                Text(
                    stringResource(R.string.diary_share_note_hint), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Button(onClick = { onShare(includeNote && entry.note.isNotBlank()) }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp).heightIn(min = 48.dp)) {
                Text(stringResource(R.string.share_as_image))
            }
        }
    }
}

/** Картата „Препоръчай“ и текстът към нея; бележката — само при [includeNote]. */
fun recommendRequest(context: Context, siteBaseUrl: String, entry: ReadingDiaryEntity, includeNote: Boolean): ShareImageRequest {
    val url = entry.inv?.takeIf { it > 0 }?.let { DeepLinks.bookUrl(siteBaseUrl, it) } ?: siteBaseUrl.trimEnd('/')
    val note = entry.note.takeIf { includeNote && it.isNotBlank() }
    val card = ShareCard.Recommend(CatalogBook(inv = entry.inv ?: 0L, author = entry.author, title = entry.title), entry.rating, note, url)
    val text = ReadingDiary.recommendation(entry.title, entry.rating, entry.note, includeNote, Formatters.isBulgarian(context)) + "\n" + url
    return ShareImageRequest(card, entry.title, text)
}

/**
 * Дневникът под ред от историята: оценена книга — панелът; върната до месец и още непитана — въпросът
 * (най-много три наведнъж, [prompt]); иначе нищо. Извън кликаемия ред — за да остане регулируем за TalkBack.
 */
@Composable
fun HistoryDiary(
    item: HistoryItem,
    entry: ReadingDiaryEntity?,
    prompt: Boolean,
    store: ReadingDiaryStore,
    onRecommend: (ReadingDiaryEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rated = entry != null && entry.rating > 0
    if (!rated && !prompt) return
    val actions = rememberDiaryActions(store, DiaryBook(item.title, item.author, item.inv, item.year))
    DiaryPanel(entry, actions, onRecommend, modifier.padding(start = 78.dp, end = 16.dp, bottom = 6.dp))
}

/**
 * Дневникът на страницата на книга, която читателят е прочел (върната, по историята в кеша). Влязъл
 * читател и върната книга — иначе нищо (гост не знае дали е чел; той вижда поканата в историята).
 */
@Composable
fun BookDiarySection(book: CatalogBook, share: ShareImageState, modifier: Modifier = Modifier) {
    val c = LocalAppContainer.current
    val auth by c.authRepository.state.collectAsStateWithLifecycle()
    if (auth !is AuthState.SignedIn) return
    val key = ReadingDiary.bookKey(book.title, book.author)
    val read by produceState<HistoryItem?>(null, book.inv, auth) {
        value = c.libraryRepository.cachedHistory()?.data
            ?.firstOrNull { (it.inv == book.inv || ReadingDiary.bookKey(it.title, it.author) == key) && it.dateIn != null }
    }
    val item = read ?: return
    val store = remember(c) { ReadingDiaryStore(c.database.readingDiary(), c.clock) }
    val diaryBook = DiaryBook(item.title, item.author, item.inv ?: book.inv, item.year)
    val entry by remember(diaryBook) { store.observeFor(diaryBook) }.collectAsStateWithLifecycle(null)
    val actions = rememberDiaryActions(store, diaryBook)
    val context = androidx.compose.ui.platform.LocalContext.current
    var sheet by remember { mutableStateOf<ReadingDiaryEntity?>(null) }
    DiaryPanel(entry, actions, onRecommend = { sheet = it }, modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp), dismissible = false)
    sheet?.let { e ->
        RecommendSheet(e, onDismiss = { sheet = null }) { include ->
            sheet = null
            share.share(recommendRequest(context, c.config.siteBaseUrl, e, include))
        }
    }
}
