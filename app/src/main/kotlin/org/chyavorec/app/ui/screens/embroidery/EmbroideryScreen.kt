package org.chyavorec.app.ui.screens.embroidery

import androidx.compose.ui.unit.sp
import org.chyavorec.app.ui.theme.FolkTokens
import org.chyavorec.app.ui.components.PillToggle
import org.chyavorec.app.ui.components.appTonalButtonColors
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.AppBottomSheet
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.ConnectedButtonGroup
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.components.rememberIsTouchExplorationEnabled
import org.chyavorec.app.ui.share.ShareCard
import org.chyavorec.app.ui.share.ShareImageHost
import org.chyavorec.app.ui.share.ShareImageRequest
import org.chyavorec.app.ui.share.rememberShareImageState
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.data.embroidery.EmbroideryStore
import org.chyavorec.domain.embroidery.StitchColor
import org.chyavorec.domain.embroidery.StitchGeometry
import org.chyavorec.domain.embroidery.StitchPattern
import org.chyavorec.domain.embroidery.Symmetry
import java.io.File

/** Платното и запазените шевици; черновата се пише след всяка завършена промяна (последователно). */
class EmbroideryViewModel(private val store: EmbroideryStore) : ViewModel() {
    val editor = EmbroideryEditor()
    var saved by mutableStateOf<List<EmbroideryStore.Saved>>(emptyList())
        private set
    private val drafts = MutableSharedFlow<StitchPattern>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        editor.onSettled = { drafts.tryEmit(editor.board.snapshot()) }
        viewModelScope.launch(Dispatchers.IO) { drafts.collect { store.saveDraft(it) } }
        viewModelScope.launch {
            val (draft, list) = withContext(Dispatchers.IO) { store.loadDraft() to store.list() }
            // Черновата се връща само ако междувременно не е нарисувано нищо ново.
            if (draft != null && !editor.board.canUndo && editor.stitchCount == 0) editor.restore(draft)
            saved = list
        }
    }

    enum class SaveResult { SAVED, EMPTY, FULL }

    suspend fun save(): SaveResult {
        val pattern = editor.board.snapshot()
        if (pattern.isEmpty) return SaveResult.EMPTY
        val added = withContext(Dispatchers.IO) { store.add(pattern, System.currentTimeMillis())?.also { saved = store.list() } }
        return if (added == null) SaveResult.FULL else SaveResult.SAVED
    }

    suspend fun delete(id: String) {
        withContext(Dispatchers.IO) { store.delete(id); saved = store.list() }
    }
}

/** „Извези шевица“ — екранът под „Още“. */
@Composable
fun EmbroideryScreen(onBack: () -> Unit) {
    val app = LocalContext.current.applicationContext
    val vm = appViewModel { EmbroideryViewModel(EmbroideryStore(File(app.filesDir, "shevitsa"))) }
    val siteUrl = LocalAppContainer.current.config.siteBaseUrl
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val share = rememberShareImageState()
    ShareImageHost(share)
    var mine by remember { mutableStateOf(false) }
    val savedMsg = stringResource(R.string.embroidery_saved)
    val emptyMsg = stringResource(R.string.embroidery_save_empty)
    val fullMsg = stringResource(R.string.embroidery_save_full, EmbroideryStore.MAX_SAVED)
    val shareEmpty = stringResource(R.string.embroidery_share_empty)
    val deletedMsg = stringResource(R.string.embroidery_deleted)
    val subject = stringResource(R.string.embroidery_card_label)
    EmbroideryScaffold(
        editor = vm.editor,
        snackbar = snackbar,
        onBack = onBack,
        onSave = {
            scope.launch {
                snackbar.showSnackbar(
                    when (vm.save()) {
                        EmbroideryViewModel.SaveResult.SAVED -> savedMsg
                        EmbroideryViewModel.SaveResult.EMPTY -> emptyMsg
                        EmbroideryViewModel.SaveResult.FULL -> fullMsg
                    },
                )
            }
        },
        onOpenMine = { mine = true },
        onShare = {
            val pattern = vm.editor.board.snapshot()
            if (pattern.isEmpty) {
                scope.launch { snackbar.showSnackbar(shareEmpty) }
            } else {
                share.share(ShareImageRequest(ShareCard.Embroidery(pattern, siteUrl), subject, "$subject\n$siteUrl"))
            }
        },
    )
    if (mine) {
        SavedPatternsSheet(
            vm.saved,
            onDismiss = { mine = false },
            onOpen = { vm.editor.load(it.pattern); mine = false },
            onDelete = { scope.launch { vm.delete(it.id); snackbar.showSnackbar(deletedMsg) } },
        )
    }
}

/** Лентата, платното и управлението — без модела на екрана (за тестовете и снимките). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmbroideryScaffold(
    editor: EmbroideryEditor,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onOpenMine: () -> Unit,
    onShare: () -> Unit,
) {
    // Екранен четец: платното не се рисува с докосване, затова курсорът е включен от начало.
    val exploring = rememberIsTouchExplorationEnabled()
    LaunchedEffect(exploring) { if (exploring) editor.cursorMode = true }
    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.embroidery_title), onBack, actions = {
                IconButton(onClick = editor::undo, enabled = editor.canUndo) {
                    Icon(Icons.AutoMirrored.Outlined.Undo, contentDescription = stringResource(R.string.embroidery_undo))
                }
                IconButton(onClick = editor::redo, enabled = editor.canRedo) {
                    Icon(Icons.AutoMirrored.Outlined.Redo, contentDescription = stringResource(R.string.embroidery_redo))
                }
                IconButton(onClick = onShare) {
                    Icon(Icons.Outlined.Share, contentDescription = stringResource(R.string.embroidery_share))
                }
            })
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        BoxWithConstraints(Modifier.padding(padding).fillMaxSize()) {
            val wide = maxWidth >= 720.dp
            if (wide) {
                Row(Modifier.fillMaxSize().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Box(Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        StitchEditorCanvas(editor, Modifier.widthIn(max = 520.dp).fillMaxWidth())
                    }
                    Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        EmbroideryControls(editor, onSave, onOpenMine, onShare)
                    }
                }
            } else {
                Column(
                    Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    StitchEditorCanvas(editor, Modifier.widthIn(max = 520.dp).fillMaxWidth())
                    EmbroideryControls(editor, onSave, onOpenMine, onShare)
                }
            }
        }
    }
}

/**
 * Платното: една рисувана повърхност (кърпа, мрежа, бодове, курсор) от масива на модела.
 * Докосване/влачене слага бод (върху бод със същия цвят — маха); за клавиатура — стрелки и
 * интервал; за екранен четец — режимът с курсор ([EmbroideryControls]).
 */
@Composable
internal fun StitchEditorCanvas(editor: EmbroideryEditor, modifier: Modifier = Modifier) {
    val haptic = LocalHapticFeedback.current
    val tick = rememberUpdatedState { haptic.performHapticFeedback(HapticFeedbackType.SegmentTick) }
    val paths = remember { StitchPaths() }
    val n = editor.board.size
    val gold = LocalExtendedColors.current.gold
    val description = stringResource(R.string.embroidery_canvas_cd, n, n, editor.stitchCount)
    val shape = FolkTokens.SmallShape
    Box(
        modifier.aspectRatio(1f).clip(shape).border(2.dp, gold, shape)
            .semantics { contentDescription = description }
            .focusable()
            .onPreviewKeyEvent { e ->
                if (e.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                // Клавиатура: стрелките местят курсора, интервал/Enter слага или маха бод.
                when (e.key) {
                    Key.DirectionLeft -> editor.moveCursor(-1, 0)
                    Key.DirectionRight -> editor.moveCursor(1, 0)
                    Key.DirectionUp -> editor.moveCursor(0, -1)
                    Key.DirectionDown -> editor.moveCursor(0, 1)
                    Key.Spacebar, Key.Enter, Key.NumPadEnter -> if (editor.placeAtCursor()) tick.value()
                    else -> return@onPreviewKeyEvent false
                }
                editor.cursorMode = true
                true
            }
            .pointerInput(editor) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val cell = size.width.toFloat() / n
                    fun cellOf(o: Offset): Pair<Int, Int>? {
                        if (o.x < 0f || o.y < 0f || o.x >= size.width || o.y >= size.height) return null
                        return (o.x / cell).toInt().coerceIn(0, n - 1) to (o.y / cell).toInt().coerceIn(0, n - 1)
                    }
                    val first = cellOf(down.position) ?: return@awaitEachGesture
                    down.consume()
                    var last = first
                    var lastTick = 0L
                    fun feel(changed: Boolean) {
                        if (!changed) return
                        val now = System.nanoTime()
                        // Не по-често от ~30 ms — по бързо влачене вибрациите се сливат.
                        if (now - lastTick > 30_000_000L) { lastTick = now; tick.value() }
                    }
                    feel(editor.beginStroke(first.first, first.second))
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!change.pressed) { change.consume(); break }
                        val at = cellOf(change.position)
                        if (at != null && at != last) {
                            for ((x, y) in StitchGeometry.line(last.first, last.second, at.first, at.second).drop(1)) feel(editor.paintAt(x, y))
                            last = at
                        }
                        change.consume()
                    }
                    editor.endStroke()
                }
            },
    ) {
        Canvas(Modifier.fillMaxSize()) {
            // Версията се чете тук — променя се само рисуването, не композицията.
            editor.drawVersion
            val cell = size.width / n
            drawFabric(n, Offset.Zero, cell)
            if (editor.symmetry != Symmetry.NONE) {
                val axis = gold.copy(alpha = 0.85f)
                val w = (cell * 0.07f).coerceAtLeast(1.5f)
                val mid = size.width / 2f
                drawLine(axis, Offset(mid, 0f), Offset(mid, size.height), w)
                if (editor.symmetry == Symmetry.QUAD) drawLine(axis, Offset(0f, mid), Offset(size.width, mid), w)
            }
            drawStitches(paths, n, Offset.Zero, cell) { x, y -> editor.board[x, y] }
            if (editor.cursorMode) {
                val topLeft = Offset(editor.cursorX * cell, editor.cursorY * cell)
                drawRoundRect(Color.White, topLeft, Size(cell, cell), CornerRadius(cell * 0.2f), style = Stroke(cell * 0.2f))
                drawRoundRect(Brand.Ink, topLeft, Size(cell, cell), CornerRadius(cell * 0.2f), style = Stroke(cell * 0.09f))
            }
        }
    }
}

/** Всичко под платното: курсор (при нужда), цветове, бод/гума, огледало, действия, мотиви, подсказка. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmbroideryControls(editor: EmbroideryEditor, onSave: () -> Unit, onOpenMine: () -> Unit, onShare: () -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (editor.cursorMode) CursorPad(editor)
        ThreadPicker(editor)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ConnectedButtonGroup(listOf(false, true), editor.erase, { editor.erase = it }, Modifier.fillMaxWidth()) { erase, _ ->
                Text(stringResource(if (erase) R.string.embroidery_tool_erase else R.string.embroidery_tool_stitch))
            }
            Text(
                stringResource(R.string.embroidery_mirror), style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp),
            )
            ConnectedButtonGroup(Symmetry.entries, editor.symmetry, editor::chooseSymmetry, Modifier.fillMaxWidth()) { s, _ ->
                // Кратък надпис (при едър шрифт „Четворно“ се чупи по букви); пълното име — за екранния четец.
                val name = stringResource(
                    when (s) {
                        Symmetry.NONE -> R.string.embroidery_mirror_none
                        Symmetry.MIRROR -> R.string.embroidery_mirror_two
                        Symmetry.QUAD -> R.string.embroidery_mirror_four
                    },
                )
                Text(
                    when (s) { Symmetry.NONE -> name; Symmetry.MIRROR -> "×2"; Symmetry.QUAD -> "×4" },
                    modifier = Modifier.semantics { contentDescription = name },
                )
            }
        }
        // Четирите действия — пълни редове (всяко хапче дели остатъка от реда), без „стъпала“; при
        // тесен екран/едър шрифт надписът не се къса насред дума — хапчето минава на нов ред.
        FlowRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Превключвател-хапче (като другите бутони тук), за TalkBack — избираем като чип.
            PillToggle(
                selected = editor.cursorMode, onClick = { editor.cursorMode = !editor.cursorMode },
                label = { Text(stringResource(R.string.embroidery_cursor)) },
                leadingIcon = { Icon(Icons.Outlined.TouchApp, null, Modifier.size(18.dp)) },
                modifier = Modifier.weight(1f),
            )
            ActionButton(Icons.Outlined.Save, R.string.embroidery_save, onSave, Modifier.weight(1f))
            ActionButton(Icons.Outlined.FolderOpen, R.string.embroidery_mine, onOpenMine, Modifier.weight(1f))
            ActionButton(Icons.Outlined.DeleteOutline, R.string.embroidery_clear, editor::clear, Modifier.weight(1f))
        }
        TemplatePicker(editor)
        Text(
            stringResource(R.string.embroidery_hint), style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ActionButton(icon: ImageVector, label: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(onClick = onClick, modifier = modifier.heightIn(min = 48.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
        Icon(icon, null, Modifier.size(18.dp))
        Spacer(Modifier.size(8.dp))
        Text(stringResource(label))
    }
}

/** Петте конеца: кръгове с пръстен за избрания; всеки е радиобутон с името на цвета. */
@Composable
private fun ThreadPicker(editor: EmbroideryEditor) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(stringResource(R.string.embroidery_colours), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(Modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (c in StitchColor.entries) {
                val selected = editor.colour == c.code
                val name = stringResource(StitchPalette.nameRes(c))
                Box(
                    Modifier.size(52.dp)
                        .selectable(selected, role = Role.RadioButton, onClick = { editor.colour = c.code; editor.erase = false })
                        .semantics { contentDescription = name },
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier.size(if (selected) 44.dp else 36.dp)
                            .border(if (selected) 3.dp else 1.dp, if (selected) LocalExtendedColors.current.gold else MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            .padding(if (selected) 5.dp else 2.dp)
                            .clip(CircleShape)
                            .background(StitchPalette.thread(c))
                            .border(1.dp, StitchPalette.edge(c.code), CircleShape),
                    )
                }
            }
        }
    }
}

/** Мотивите на приложението като начало: малка картина и име; докосването ги слага на платното (връща се с „Върни“). */
@Composable
private fun TemplatePicker(editor: EmbroideryEditor) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionLabel(stringResource(R.string.embroidery_templates))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            for (t in EmbroideryTemplate.entries) {
                val name = stringResource(t.nameRes)
                val cd = stringResource(R.string.embroidery_tpl_cd, name)
                val pattern = remember(t) { t.pattern() }
                Column(
                    Modifier.clip(FolkTokens.SmallShape)
                        .clickable(onClickLabel = cd) { editor.load(pattern) }
                        .semantics { contentDescription = cd }
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    StitchPicture(pattern, Modifier.size(80.dp))
                    TemplateName(name)
                }
            }
        }
    }
}

/**
 * Името на мотива под картинката: пренася се само между думите. Една дума („Осморъка“) е на
 * един ред и при нужда се смалява до 0.75×, вместо да се къса („Осморъ/ка“); повече думи — до
 * два реда („Дърво на / живота“).
 */
@Composable
private fun TemplateName(name: String) {
    val words = remember(name) { name.trim().split(Regex("\\s+")).size }
    val style = MaterialTheme.typography.labelMedium
    androidx.compose.foundation.text.BasicText(
        name,
        style = style.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = androidx.compose.ui.text.style.TextAlign.Center),
        maxLines = minOf(words, 2),
        autoSize = androidx.compose.foundation.text.TextAutoSize.StepBased(minFontSize = style.fontSize * 0.75f, maxFontSize = style.fontSize, stepSize = 0.25.sp),
        modifier = Modifier.padding(top = 4.dp).widthIn(max = 104.dp),
    )
}

/**
 * Режимът с курсор — запасният начин да се извезва без пръст по платното (екранен четец,
 * клавиатура, по-голяма точност): четири стрелки и „Постави бод“; позицията се обявява.
 */
@Composable
private fun CursorPad(editor: EmbroideryEditor) {
    val cell = editor.cursorCell()
    val status = stringResource(
        R.string.embroidery_cursor_pos, editor.cursorY + 1, editor.cursorX + 1,
        if (cell == 0) stringResource(R.string.embroidery_cell_empty)
        else stringResource(R.string.embroidery_cell_stitch, stringResource(StitchPalette.nameRes(StitchColor.of(cell)!!))),
    )
    val removes = cell == editor.colour || editor.erase
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            status, style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite; heading() },
        )
        PadButton(Icons.Outlined.KeyboardArrowUp, R.string.embroidery_up) { editor.moveCursor(0, -1) }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            PadButton(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, R.string.embroidery_left) { editor.moveCursor(-1, 0) }
            FilledTonalButton(onClick = { editor.placeAtCursor() }, modifier = Modifier.heightIn(min = 56.dp), colors = appTonalButtonColors()) {
                Text(stringResource(if (removes && cell != 0) R.string.embroidery_remove else R.string.embroidery_place))
            }
            PadButton(Icons.AutoMirrored.Outlined.KeyboardArrowRight, R.string.embroidery_right) { editor.moveCursor(1, 0) }
        }
        PadButton(Icons.Outlined.KeyboardArrowDown, R.string.embroidery_down) { editor.moveCursor(0, 1) }
    }
}

@Composable
private fun PadButton(icon: ImageVector, label: Int, onClick: () -> Unit) {
    FilledTonalButton(onClick = onClick, modifier = Modifier.size(56.dp), contentPadding = PaddingValues(0.dp), colors = appTonalButtonColors()) {
        Icon(icon, contentDescription = stringResource(label), modifier = Modifier.size(28.dp))
    }
}

/** „Моите шевици“: до [EmbroideryStore.MAX_SAVED] запазени — отваряне и изтриване. */
@Composable
private fun SavedPatternsSheet(
    saved: List<EmbroideryStore.Saved>,
    onDismiss: () -> Unit,
    onOpen: (EmbroideryStore.Saved) -> Unit,
    onDelete: (EmbroideryStore.Saved) -> Unit,
) {
    val context = LocalContext.current
    AppBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
            Text(
                stringResource(R.string.embroidery_mine), style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp).semantics { heading() },
            )
            Spacer(Modifier.height(8.dp))
            if (saved.isEmpty()) {
                Text(
                    stringResource(R.string.embroidery_mine_empty), style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                )
            }
            for (item in saved) {
                val date = Formatters.millisDate(context, item.savedAtMs) ?: ""
                val open = stringResource(R.string.embroidery_open_item, date)
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        Modifier.weight(1f).heightIn(min = 64.dp).clip(FolkTokens.SmallShape).clickable(onClickLabel = open) { onOpen(item) }
                            .padding(8.dp).semantics(mergeDescendants = true) { contentDescription = open },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        StitchPicture(item.pattern, Modifier.size(56.dp))
                        Column {
                            Text(date, style = MaterialTheme.typography.titleMedium)
                            Text(
                                stringResource(R.string.embroidery_stitches, item.pattern.stitchCount), style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    IconButton(onClick = { onDelete(item) }) {
                        Icon(Icons.Outlined.DeleteOutline, contentDescription = stringResource(R.string.embroidery_delete))
                    }
                }
            }
        }
    }
}
