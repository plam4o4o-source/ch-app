package org.chyavorec.app.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.chyavorec.app.R
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.appViewModel
import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.BackTopBar
import org.chyavorec.app.ui.components.ChLoadingIndicator
import org.chyavorec.app.ui.components.MessageView
import org.chyavorec.app.ui.components.ScreenGutter
import org.chyavorec.app.ui.components.SiteImage
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.StitchInOrnament
import org.chyavorec.app.ui.components.StitchSpec
import org.chyavorec.app.ui.components.StitchedMotif
import org.chyavorec.app.ui.components.drawStitches
import org.chyavorec.app.ui.components.stitchBurgundy
import org.chyavorec.app.ui.share.ContentShareCards
import org.chyavorec.app.ui.share.ShareImageHost
import org.chyavorec.app.ui.share.ShareImageRequest
import org.chyavorec.app.ui.share.rememberShareImageState
import org.chyavorec.app.ui.theme.FolkTokens
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.domain.quiz.QuizKind
import org.chyavorec.domain.quiz.QuizQuestion

/** „Познай Яворец“ — пет въпроса на ден (от историята на читалището, каталога и снимките от изложбата). */
@Composable
fun QuizScreen(onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val vm = appViewModel {
        QuizViewModel(
            AppQuizSources(it.timelineRepository, it.catalogRepository, it.siteRepository, it.quizQuestionsRepository),
            it.quizStore, it.clock,
        )
    }
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val resources = LocalResources.current
    val share = rememberShareImageState()
    Scaffold(topBar = { BackTopBar(stringResource(R.string.quiz_title), onBack) }) { padding ->
        ShareImageHost(share)
        Box(Modifier.padding(padding).fillMaxSize()) {
            QuizContent(
                state, onChoose = vm::choose, onNext = vm::next, onRetry = vm::load,
                onShare = { f ->
                    val text = resources.getString(R.string.quiz_share_text, f.results.count { it }, f.results.size, container.config.siteBaseUrl)
                    share.share(
                        ShareImageRequest(
                            ContentShareCards.quiz(Formatters.date(context, f.quiz.date).orEmpty(), f.results, f.streak, container.config.siteBaseUrl),
                            resources.getString(R.string.quiz_title), text,
                        ),
                    )
                },
                onBack = onBack,
            )
        }
    }
}

/** Съдържанието по състояние; без ViewModel — за тестовете със снимки. */
@Composable
fun QuizContent(
    state: QuizUi,
    onChoose: (Int) -> Unit,
    onNext: () -> Unit,
    onRetry: () -> Unit,
    onShare: (QuizUi.Finished) -> Unit,
    onBack: () -> Unit,
) {
    when (state) {
        QuizUi.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { ChLoadingIndicator() }
        QuizUi.Unavailable -> MessageView(
            Icons.Outlined.Quiz, stringResource(R.string.quiz_unavailable_title), stringResource(R.string.quiz_unavailable),
            actionLabel = stringResource(R.string.action_retry), onAction = onRetry, modifier = Modifier.fillMaxSize(),
        )
        is QuizUi.Playing -> QuestionContent(state, onChoose, onNext)
        is QuizUi.Finished -> ResultContent(state, onShare = { onShare(state) }, onBack = onBack)
    }
}

/** Широк екран (таблет, телефон хоризонтално): въпросът вляво, отговорът и лентата — вдясно. */
private val WideQuiz = 600.dp
private val QuizMaxWidth = 1040.dp
private val BandMaxWidth = 360.dp
private val BandCompactWidth = 260.dp
private val CompactQuiz = 620.dp

@Composable
private fun QuestionContent(s: QuizUi.Playing, onChoose: (Int) -> Unit, onNext: () -> Unit) {
    val q = s.quiz.questions[s.index]
    val haptic = LocalHapticFeedback.current
    val correct = s.chosen == q.correctIndex
    LaunchedEffect(s.index, s.chosen) {
        if (s.chosen != null) haptic.performHapticFeedback(if (correct) HapticFeedbackType.Confirm else HapticFeedbackType.Reject)
    }
    // Всеки въпрос започва отгоре.
    val scroll = key(s.index) { rememberScrollState() }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        // Нисък екран (телефон с едър шрифт): по-малка лента без надпис, за да остане място за въпроса.
        val compact = maxHeight < CompactQuiz
        val feedbackMax = maxHeight * if (compact) 0.22f else 0.3f
        if (maxWidth >= WideQuiz) {
            Row(Modifier.align(Alignment.TopCenter).widthIn(max = QuizMaxWidth).fillMaxSize()) {
                Column(
                    Modifier.weight(1.15f).fillMaxHeight().verticalScroll(scroll).padding(start = ScreenGutter, end = 12.dp, top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) { QuestionPart(s, onChoose) }
                Column(
                    Modifier.weight(1f).fillMaxHeight().padding(start = 12.dp, end = ScreenGutter, top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Progress(s.results, s.index, s.quiz.questions.size, s.chosen != null)
                    // Бележката е над лентата и се превърта сама, ако е дълга — бутонът не мърда.
                    Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.BottomStart) {
                        if (s.chosen != null) key(q.id) { Feedback(q, correct, Modifier.verticalScroll(rememberScrollState())) }
                    }
                    BandAndNext(s, onNext, compact)
                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(scroll).padding(horizontal = ScreenGutter, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Progress(s.results, s.index, s.quiz.questions.size, s.chosen != null)
                    QuestionPart(s, onChoose)
                }
                // Долната част е неподвижна: отговорът, лентата и „Напред“ винаги на едно място.
                Column(
                    Modifier.fillMaxWidth().padding(start = ScreenGutter, end = ScreenGutter, top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (s.chosen != null) key(q.id) { Feedback(q, correct, Modifier.heightIn(max = feedbackMax).verticalScroll(rememberScrollState())) }
                    BandAndNext(s, onNext, compact)
                }
            }
        }
    }
}

/** Номерът, снимката, въпросът и отговорите. */
@Composable
private fun QuestionPart(s: QuizUi.Playing, onChoose: (Int) -> Unit) {
    val q = s.quiz.questions[s.index]
    Text(
        stringResource(R.string.quiz_question_of, s.index + 1, s.quiz.questions.size), style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    q.imageUrl?.let { url ->
        SiteImage(
            url, 640.dp, stringResource(R.string.quiz_photo_desc),
            Modifier.fillMaxWidth().aspectRatio(16f / 10f).clip(MaterialTheme.shapes.medium), ContentScale.Crop,
        )
    }
    Text(questionText(q), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics { heading() })
    q.options.forEachIndexed { i, text ->
        AnswerButton(text, i, q.correctIndex, s.chosen, onClick = { onChoose(i) })
    }
}

/** „Избродирай отговорите“ и „Напред“ (неактивен, докато няма отговор — за да не „изскача“). */
@Composable
private fun BandAndNext(s: QuizUi.Playing, onNext: () -> Unit, compact: Boolean) {
    val total = s.quiz.questions.size
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AnswersBand(s.results, total, current = if (s.chosen == null) s.index else null, compact = compact)
        Button(onClick = onNext, enabled = s.chosen != null, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
            Text(stringResource(if (s.index + 1 >= total) R.string.quiz_see_result else R.string.quiz_next))
        }
    }
}

/** Лентата с надписа над нея; при всички верни — „Шевицата е цяла!“. */
@Composable
private fun AnswersBand(results: List<Boolean>, total: Int, current: Int?, modifier: Modifier = Modifier, compact: Boolean = false) {
    val closed = QuizBandCharts.closed(results, total)
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (!compact) {
            Text(
                stringResource(if (closed) R.string.quiz_band_done else R.string.quiz_band_title), style = MaterialTheme.typography.labelMedium,
                color = if (closed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
            )
        }
        QuizBand(results, total, current, quizBandColors(), Modifier.widthIn(max = if (compact) BandCompactWidth else BandMaxWidth).fillMaxWidth().aspectRatio(QuizBandCharts.aspect(total)))
    }
}

/** Цветовете на лентата в темата на екрана. */
@Composable
private fun quizBandColors(): QuizBandColors {
    val ext = LocalExtendedColors.current
    val scheme = MaterialTheme.colorScheme
    val burgundy = stitchBurgundy()
    return remember(ext, scheme, burgundy) {
        QuizBandColors(
            gold = ext.gold, burgundy = burgundy, holes = scheme.onSurfaceVariant.copy(alpha = 0.5f), current = ext.gold,
            cloth = scheme.surfaceContainer, hem = ext.gold.copy(alpha = FolkTokens.GOLD_BORDER_ALPHA),
        )
    }
}

@Composable
private fun questionText(q: QuizQuestion): String = when (q.kind) {
    QuizKind.TIMELINE_YEAR -> stringResource(R.string.quiz_q_year, q.subject)
    QuizKind.TIMELINE_EVENT -> stringResource(R.string.quiz_q_event, q.subject)
    QuizKind.CATALOG_AUTHOR -> stringResource(R.string.quiz_q_author, q.subject)
    QuizKind.CATALOG_TITLE -> stringResource(R.string.quiz_q_title, q.subject)
    QuizKind.PHOTO -> stringResource(R.string.quiz_q_photo)
    QuizKind.CUSTOM -> q.subject
}

/** Петте въпроса като бродирани знаци: верен — избродирана розетка, грешен — „разпорен“ кръг, текущият — с рамка. */
@Composable
private fun Progress(results: List<Boolean>, index: Int, total: Int, answered: Boolean) {
    val gold = LocalExtendedColors.current.gold
    val desc = stringResource(R.string.quiz_progress_desc, results.count { it }, results.size, total)
    Row(Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = desc }, horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(total) { i ->
            val done = i < results.size && (i < index || answered)
            Box(
                Modifier.size(34.dp).then(if (!done) Modifier.border(if (i == index) 2.dp else 1.dp, gold.copy(alpha = if (i == index) 1f else FolkTokens.GOLD_BORDER_ALPHA), CircleShape) else Modifier),
                contentAlignment = Alignment.Center,
            ) {
                if (done && results[i]) StitchedMotif(StitchCharts.rosette(5), progress = { 1f }, modifier = Modifier.size(32.dp))
                else if (done) UnpickedMark(Modifier.size(32.dp))
            }
        }
    }
}

/** Колко от розетката остава на „разпорения“ знак — цели бодове (без игла насред бод). */
internal fun unpickedProgress(count: Int, keep: Float = 0.45f): Float = if (count <= 0) 0f else (count * keep).toInt().toFloat() / count

/**
 * Грешният отговор: розетка, бродирана до половината и разпорена — тичащ бод в кръг и
 * останалите бодове в цвета на грешката. Същият размер като верния знак.
 */
@Composable
private fun UnpickedMark(modifier: Modifier = Modifier) {
    val bad = LocalExtendedColors.current.bad
    val chart = remember { StitchCharts.rosette(5) }
    val progress = remember(chart) { unpickedProgress(chart.size) }
    Spacer(
        modifier.clearAndSetSemantics { }.drawBehind {
            val ring = StitchSpec.Running.stroke(density)
            drawCircle(bad, radius = size.minDimension / 2f - ring.width, alpha = 0.55f, style = ring)
            inset(size.minDimension * 0.1f) { drawStitches(chart, progress, bad, bad.copy(alpha = 0.7f)) }
        },
    )
}

@Composable
private fun AnswerButton(text: String, index: Int, correctIndex: Int, chosen: Int?, onClick: () -> Unit) {
    val ext = LocalExtendedColors.current
    val revealed = chosen != null
    val isCorrect = revealed && index == correctIndex
    val isWrongChoice = revealed && index == chosen && index != correctIndex
    // Обща карта ([AppCard]): преди отговора — със златна рамка; след него верният и грешният
    // избор са в цвета на състоянието, а останалите — обикновени карти.
    val accent = when {
        isCorrect -> ext.ok
        isWrongChoice -> ext.bad
        else -> Color.Unspecified
    }
    val container = when {
        isCorrect -> ext.ok.copy(alpha = 0.14f)
        isWrongChoice -> ext.bad.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }
    val state = when {
        isCorrect -> stringResource(R.string.quiz_state_correct)
        isWrongChoice -> stringResource(R.string.quiz_state_wrong)
        else -> null
    }
    AppCard(
        Modifier.fillMaxWidth().semantics { if (state != null) stateDescription = state },
        onClick = onClick, enabled = !revealed, accent = !revealed, accentColor = accent, color = container,
    ) {
        // Поне 60 dp, текстът — центриран по височина.
        Row(Modifier.heightIn(min = 60.dp).padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            if (isCorrect) Icon(Icons.Outlined.CheckCircle, null, tint = ext.ok, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

/** „Вярно!“ / „Не точно“ с бродирания знак и бележката; обявява се от екранния четец. */
@Composable
private fun Feedback(q: QuizQuestion, correct: Boolean, modifier: Modifier = Modifier) {
    val ext = LocalExtendedColors.current
    Column(modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite }, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (correct) StitchInOrnament(StitchCharts.rosette(6), Modifier.size(40.dp))
            else StitchInOrnament(StitchCharts.rhombus(6), Modifier.size(40.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    stringResource(if (correct) R.string.quiz_right else R.string.quiz_wrong), style = MaterialTheme.typography.titleLarge,
                    color = if (correct) ext.ok else MaterialTheme.colorScheme.onSurface,
                )
                if (!correct) Text(stringResource(R.string.quiz_correct_was, q.correct), style = MaterialTheme.typography.bodyMedium)
            }
        }
        q.note?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable
private fun ResultContent(f: QuizUi.Finished, onShare: () -> Unit, onBack: () -> Unit) {
    val score = f.results.count { it }
    val total = f.results.size
    val headline = when {
        score == total -> R.string.quiz_result_perfect
        score * 2 >= total -> R.string.quiz_result_good
        else -> R.string.quiz_result_try
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val viewport = maxHeight
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
            // Картата е по средата на свободното място, бутоните — долу (без празнина под тях).
            Column(
                Modifier.heightIn(min = viewport).widthIn(max = ResultMaxWidth).fillMaxWidth().padding(horizontal = ScreenGutter, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(Modifier.height(0.dp))
                AppCard(Modifier.fillMaxWidth().padding(vertical = 16.dp), accent = true) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(stringResource(headline), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.semantics { heading() })
                        Text("$score/$total", style = MaterialTheme.typography.displayLarge, color = LocalExtendedColors.current.gold)
                        Text(stringResource(R.string.share_card_quiz_score, score, total), style = MaterialTheme.typography.bodyLarge)
                        AnswersBand(f.results, total, current = null, modifier = Modifier.padding(vertical = 6.dp))
                        if (f.streak > 0) {
                            Text(pluralStringResource(R.plurals.quiz_streak, f.streak, f.streak), style = MaterialTheme.typography.titleMedium)
                            if (f.best > f.streak) Text(stringResource(R.string.quiz_best, f.best), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            stringResource(R.string.quiz_come_back), style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
                        )
                    }
                }
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onShare, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                        Icon(Icons.Outlined.Share, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.quiz_share))
                    }
                    // „Затвори“, не второ „Назад“ — TalkBack не чете две еднакви действия (стрелката горе е „Назад“).
                    // Рамката е в основния цвят: в тъмна тема сивата почти не се вижда.
                    OutlinedButton(
                        onClick = onBack, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    ) { Text(stringResource(R.string.action_close)) }
                }
            }
        }
    }
}

private val ResultMaxWidth = 560.dp
