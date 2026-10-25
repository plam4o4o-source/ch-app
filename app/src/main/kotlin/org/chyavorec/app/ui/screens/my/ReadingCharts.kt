package org.chyavorec.app.ui.screens.my

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chyavorec.app.R
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.components.darkTopHighlight
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.UdcCoverColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.util.UdcLabels
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.ReadingStats
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem
import java.time.format.TextStyle
import kotlin.math.max

/**
 * „Моето четене“ в „Моето“: обобщението на историята за графиките ([ReadingStats]).
 * Само за влязъл читател с история; изчислява се извън главната нишка — от кешираната
 * история (ако кеш няма — веднъж от сървъра, както в „Какво съм чел“) и каталога в базата.
 */
class ReadingStatsViewModel(private val c: AppContainer) : ViewModel() {
    private val _stats = MutableStateFlow<ReadingStats.Summary?>(null)
    /** `null` — няма какво да се покаже (не е влязъл, няма история или още се изчислява). */
    val stats: StateFlow<ReadingStats.Summary?> = _stats.asStateFlow()

    /** Опитът да се изтегли история без кеш — веднъж на вход. */
    private var fetched = false

    init {
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            // Наново при вход/изход и при всяка нова история в кеша (от „Какво съм чел“, /v1/me/all).
            combine(c.authRepository.state, c.libraryRepository.historyVersion) { a, _ -> a }.collectLatest { a ->
                if (a !is AuthState.SignedIn) {
                    _stats.value = null
                    fetched = false
                    return@collectLatest
                }
                val history = c.libraryRepository.cachedHistory()?.data ?: loadOnce()
                _stats.value = history?.takeIf { it.isNotEmpty() }?.let { compute(it) }
            }
        }
    }

    private suspend fun loadOnce(): List<HistoryItem>? {
        if (fetched) return null
        fetched = true
        return (c.libraryRepository.history(force = false) as? Outcome.Success)?.value?.data
    }

    private suspend fun compute(history: List<HistoryItem>): ReadingStats.Summary? {
        // Разделите — от каталога в базата (без мрежа); без него графиката по раздели се скрива.
        val books: Map<String, CatalogBook> = try {
            c.catalogRepository.lookupSearcher(network = false)?.data?.let { ReadingStats.resolveBooks(it, history) }.orEmpty()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            emptyMap()
        }
        return withContext(Dispatchers.Default) { ReadingStats.compute(history, books, c.clock.today(), c.clock.zone()) }
    }
}

/** Парче от пръстена по раздели: име, брой, цвят. */
@Immutable
private data class Slice(val label: String, val count: Int, val color: Color)

/** Сив за книгите без раздел и за „Други“. */
private val NoSectionColor = Color(0xFF8C8273)

/** Най-много раздели в легендата — останалите се събират в „Други“. */
private const val LEGEND_MAX = 5

/**
 * Картата „Моето четене“: книги по месеци (стълбчета), раздели по УДК (пръстен с легенда)
 * и три малки числа. Стълбчетата и пръстенът „израстват“ при първото показване (без
 * анимация при намалено движение). Всяка графика има описание за четеца на екрана.
 */
@Composable
fun ReadingStatsCard(stats: ReadingStats.Summary, onOpenHistory: () -> Unit, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    // Анимацията — само веднъж за екрана (не при всяко връщане/превъртане).
    var played by rememberSaveable { mutableStateOf(false) }
    val progress = remember { Animatable(if (reduced || played) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (progress.value < 1f) progress.animateTo(1f, tween(durationMillis = 1_100, easing = FastOutSlowInEasing))
        played = true
    }
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp).darkTopHighlight(MaterialTheme.shapes.large),
    ) {
        Column(Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.reading_months_label),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onOpenHistory) { Text(stringResource(R.string.reading_open_history)) }
            }
            Column(Modifier.padding(end = 8.dp)) {
                MonthBars(stats, progress = { progress.value })
                val sections = rememberSlices(stats)
                if (sections != null) {
                    Spacer(Modifier.height(18.dp))
                    Text(stringResource(R.string.reading_sections_label), style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(10.dp))
                    SectionRing(sections, stats.total, progress = { progress.value })
                }
                Spacer(Modifier.height(16.dp))
                StatTiles(stats)
                Spacer(Modifier.height(10.dp))
                Text(
                    stringResource(R.string.reading_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Стълбчета за последните 12 месеца; текущият месец е в златно, броят — над стълбчето. */
@Composable
private fun MonthBars(stats: ReadingStats.Summary, progress: () -> Float) {
    val locale = Formatters.uiLocale(LocalContext.current)
    val months = stats.months
    val peak = stats.peakMonth
    val description = if (peak == null) {
        stringResource(R.string.reading_months_none_cd)
    } else {
        val name = peak.month.month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
        pluralStringResource(R.plurals.reading_months_cd, stats.lastMonthsTotal, stats.lastMonthsTotal, name)
    }
    val initials = remember(months, locale) {
        months.map { it.month.month.getDisplayName(TextStyle.NARROW_STANDALONE, locale).uppercase(locale) }
    }
    val extended = LocalExtendedColors.current
    val barColor = MaterialTheme.colorScheme.primary.copy(alpha = if (extended.isDark) 0.55f else 0.42f)
    val currentColor = extended.gold
    val baseline = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val labelStyle = MaterialTheme.typography.labelSmall.copy(color = labelColor)
    val currentLabelStyle = labelStyle.copy(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
    val measurer = rememberTextMeasurer()
    val top = max(1, months.maxOf { it.count })
    Column(Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = description }) {
        Canvas(Modifier.fillMaxWidth().height(112.dp).padding(top = 4.dp)) {
            val n = months.size
            val slot = size.width / n
            val barWidth = slot * 0.56f
            val labelSpace = 18.dp.toPx()
            val chartHeight = size.height - labelSpace
            val radius = CornerRadius(barWidth / 2.6f)
            val p = progress()
            // Лека основна линия — празните месеци също се виждат.
            drawLine(baseline, Offset(0f, size.height - 0.5f), Offset(size.width, size.height - 0.5f), strokeWidth = 1.dp.toPx())
            months.forEachIndexed { i, m ->
                // Стълбчетата израстват едно след друго (отляво надясно).
                val local = ((p * 1.6f) - i * (0.6f / n)).coerceIn(0f, 1f)
                val current = i == n - 1
                val h = if (m.count == 0) 0f else max(4.dp.toPx(), chartHeight * m.count / top) * local
                val x = i * slot + (slot - barWidth) / 2f
                if (h > 0f) {
                    // Заоблени са само горните ъгли: долните остават под основата (изрязани).
                    clipRect(bottom = size.height) {
                        drawRoundRect(
                            color = if (current) currentColor else barColor,
                            topLeft = Offset(x, size.height - h),
                            size = Size(barWidth, h + radius.x),
                            cornerRadius = radius,
                        )
                    }
                }
                if (m.count > 0 && local > 0.85f) {
                    val text = measurer.measure(m.count.toString(), if (current) currentLabelStyle else labelStyle)
                    drawText(
                        text,
                        topLeft = Offset(x + barWidth / 2f - text.size.width / 2f, size.height - h - text.size.height - 2.dp.toPx()),
                        alpha = ((local - 0.85f) / 0.15f).coerceIn(0f, 1f),
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth()) {
            initials.forEachIndexed { i, s ->
                val current = i == initials.lastIndex
                Text(
                    s,
                    style = if (current) MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.labelSmall,
                    color = if (current) MaterialTheme.colorScheme.onSurface else labelColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

/** Разделите за пръстена (най-много [LEGEND_MAX] реда); `null` — няма нито един познат раздел. */
@Composable
private fun rememberSlices(stats: ReadingStats.Summary): List<Slice>? {
    val dark = LocalExtendedColors.current.isDark
    val other = stringResource(R.string.reading_sections_other)
    val res = LocalResources.current
    return remember(stats.sections, dark, other, res) {
        if (stats.sections.none { it.section != null }) return@remember null
        // В тъмна тема цветовете на кориците са малко изсветлени, за да се четат върху тъмния фон.
        fun tone(c: Color) = if (dark) lerp(c, Color.White, 0.22f) else c
        val all = stats.sections.map { s ->
            Slice(res.getString(UdcLabels.res(s.section)), s.count, tone(s.section?.let { UdcCoverColors[it] } ?: NoSectionColor))
        }
        if (all.size <= LEGEND_MAX) all
        else all.take(LEGEND_MAX - 1) + Slice(other, all.drop(LEGEND_MAX - 1).sumOf { it.count }, tone(NoSectionColor))
    }
}

/** Пръстен по раздели (с броя в средата) и легенда до него. */
@Composable
private fun SectionRing(slices: List<Slice>, total: Int, progress: () -> Float) {
    val summary = slices.joinToString(", ") { "${it.label} – ${it.count}" }
    val description = stringResource(R.string.reading_sections_cd, summary)
    val track = MaterialTheme.colorScheme.surfaceContainerHighest
    Row(
        Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(108.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(108.dp)) {
                val stroke = 16.dp.toPx()
                val inset = stroke / 2f
                val arcSize = Size(size.width - stroke, size.height - stroke)
                val topLeft = Offset(inset, inset)
                drawArc(track, 0f, 360f, useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(stroke))
                val sum = slices.sumOf { it.count }.toFloat().coerceAtLeast(1f)
                // Малка пролука между парчетата (без нея при едно-единствено парче).
                val gap = if (slices.size > 1) 2.5f else 0f
                val sweepAll = 360f * progress()
                var start = -90f
                for (s in slices) {
                    val sweep = 360f * s.count / sum
                    val visible = (minOf(start + sweep, -90f + sweepAll) - start - gap).coerceAtLeast(0f)
                    if (visible > 0f) {
                        drawArc(s.color, start + gap / 2f, visible, useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(stroke))
                    }
                    start += sweep
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(total.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                // Малки букви — labelMedium (разредката на labelSmall е само за ГЛАВНИ).
                Text(stringResource(R.string.reading_total), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            slices.forEach { s ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(s.color, CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        s.label, style = MaterialTheme.typography.bodySmall,
                        maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(s.count.toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/** Три малки числа: тази година, любим автор, най-дълга поредица от месеци. */
@Composable
private fun StatTiles(stats: ReadingStats.Summary) {
    // Еднаква височина на трите плочки (по най-високата).
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatTile(stats.thisYear.toString(), stringResource(R.string.reading_this_year), Modifier.weight(1f))
        StatTile(
            stats.favouriteAuthor?.let(::shortAuthor) ?: "—",
            stringResource(R.string.reading_favourite_author),
            Modifier.weight(1.3f),
            small = true,
        )
        StatTile(
            pluralStringResource(R.plurals.reading_streak_months, stats.longestStreak, stats.longestStreak),
            stringResource(R.string.reading_streak),
            Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatTile(value: String, label: String, modifier: Modifier = Modifier, small: Boolean = false) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxHeight().semantics(mergeDescendants = true) {},
    ) {
        Column(Modifier.heightIn(min = 68.dp).padding(horizontal = 10.dp, vertical = 10.dp), verticalArrangement = Arrangement.Center) {
            // Стойностите — все в Cormorant (числата и името на автора); името е по-дребно.
            val valueStyle = MaterialTheme.typography.titleLarge
            Text(
                value,
                style = if (small) valueStyle.copy(fontSize = 18.sp, lineHeight = 21.sp) else valueStyle,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
        }
    }
}

/** „Вазов, Иван“ → „Иван Вазов“ (както се казва на глас); друго — както е. */
internal fun shortAuthor(author: String): String {
    val parts = author.split(',').map { it.trim() }
    return if (parts.size == 2 && parts.all { it.isNotEmpty() }) "${parts[1]} ${parts[0]}" else author.trim()
}
