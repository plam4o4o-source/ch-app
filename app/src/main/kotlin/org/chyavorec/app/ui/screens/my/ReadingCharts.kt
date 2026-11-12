package org.chyavorec.app.ui.screens.my

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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
import org.chyavorec.app.ui.components.StitchChart
import org.chyavorec.app.ui.components.StitchCharts
import org.chyavorec.app.ui.components.darkTopHighlight
import org.chyavorec.app.ui.components.drawStitches
import org.chyavorec.app.ui.components.stitchBurgundy
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.UdcCoverColors
import org.chyavorec.app.util.Formatters
import org.chyavorec.app.util.UdcLabels
import org.chyavorec.core.Outcome
import org.chyavorec.data.catalog.ReadingStats
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.data.repository.AuthState
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem
import java.time.format.TextStyle as DateStyle
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * „Моето четене“ в „Моето“: обобщението на историята за графиките ([ReadingStats]).
 * Само за влязъл читател с история; изчислява се извън главната нишка — от кешираната
 * история (ако кеш няма — веднъж от сървъра, както в „Какво съм чел“) и каталога в базата.
 */
class ReadingStatsViewModel(private val c: AppContainer) : ViewModel() {
    private val _stats = MutableStateFlow<ReadingStats.Summary?>(null)
    /** `null` — няма какво да се покаже (не е влязъл, няма история или още се изчислява). */
    val stats: StateFlow<ReadingStats.Summary?> = _stats.asStateFlow()

    private val _years = MutableStateFlow<List<ReadingYear.Summary>>(emptyList())
    /** „Моята читателска година“ — предлаганите години ([ReadingYear.offeredYears]) с поне една книга. */
    val years: StateFlow<List<ReadingYear.Summary>> = _years.asStateFlow()

    /** Опитът да се изтегли история без кеш — веднъж на вход. */
    private var fetched = false

    init {
        viewModelScope.launch {
            if (c.authRepository.state.value is AuthState.Unknown) c.authRepository.restore()
            // Наново при вход/изход и при всяка нова история в кеша (от „Какво съм чел“, /v1/me/all).
            combine(c.authRepository.state, c.libraryRepository.historyVersion) { a, _ -> a }.collectLatest { a ->
                if (a !is AuthState.SignedIn) {
                    _stats.value = null
                    _years.value = emptyList()
                    fetched = false
                    return@collectLatest
                }
                // Извън главната нишка — и справките в каталога (десетки заявки към базата):
                // иначе всяко връщане от базата чака свободен кадър на главната нишка.
                withContext(Dispatchers.Default) {
                    val version = c.libraryRepository.historyVersion.value
                    val history = c.libraryRepository.cachedHistory()?.data ?: loadOnce()
                    if (history.isNullOrEmpty()) {
                        _stats.value = null
                        _years.value = emptyList()
                        return@withContext
                    }
                    val books = readingBooks(c, history)
                    val today = c.clock.today()
                    _stats.value = ReadingStats.compute(history, books, today, c.clock.zone())
                    _years.value = ReadingYear.offeredYears(today).mapNotNull { ReadingYear.compute(history, books, it, c.clock.zone()) }
                        .also { c.readingYears.put(version, it) }
                }
            }
        }
    }

    private suspend fun loadOnce(): List<HistoryItem>? {
        if (fetched) return null
        fetched = true
        return (c.libraryRepository.history(force = false) as? Outcome.Success)?.value?.data
    }
}

/**
 * Каталожните записи на книгите от историята — от каталога в базата (без мрежа); без него
 * разделите по УДК липсват (графиката по раздели и слайдът с раздела се скриват).
 */
internal suspend fun readingBooks(c: AppContainer, history: List<HistoryItem>): Map<String, CatalogBook> = try {
    c.catalogRepository.lookupSearcher(network = false)?.data?.let { ReadingStats.resolveBooks(it, history) }.orEmpty()
} catch (e: CancellationException) {
    throw e
} catch (_: Exception) {
    emptyMap()
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

/**
 * Месеците за последните 12 месеца като колони от кръстат бод — по един бод за книга; рекордният
 * месец е в златен конец, текущият — с удебелена буква; броят — над колоната.
 */
@Composable
private fun MonthBars(stats: ReadingStats.Summary, progress: () -> Float) {
    val locale = Formatters.uiLocale(LocalContext.current)
    val months = stats.months
    val peak = stats.peakMonth
    val description = if (peak == null) {
        stringResource(R.string.reading_months_none_cd)
    } else {
        val name = peak.month.month.getDisplayName(DateStyle.FULL_STANDALONE, locale)
        pluralStringResource(R.plurals.reading_months_cd, stats.lastMonthsTotal, stats.lastMonthsTotal, name)
    }
    val initials = remember(months, locale) {
        months.map { it.month.month.getDisplayName(DateStyle.NARROW_STANDALONE, locale).uppercase(locale) }
    }
    val extended = LocalExtendedColors.current
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val labelStyle = MaterialTheme.typography.labelSmall.copy(color = labelColor)
    val currentLabelStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
    val counts = remember(months) { months.map { it.count } }
    Box(Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = description }) {
        EmbroideredMonths(
            counts = counts,
            labels = initials,
            progress = progress,
            thread = stitchBurgundy(),
            peak = extended.gold,
            baseline = MaterialTheme.colorScheme.outlineVariant,
            valueStyle = labelStyle.copy(fontWeight = FontWeight.Bold),
            labelStyle = { i -> if (i == initials.lastIndex) currentLabelStyle else labelStyle },
        )
    }
}

/** Отсечките в пояса по раздели. */
internal const val BELT_SEGMENTS = 40

/** Схемата на месечните колони от кръстат бод — чисти функции (тестват се без рисуване). */
internal object ChartStitches {
    /** Над толкова реда клетката се смалява, та колоната да се побере в същата височина. */
    const val FULL_ROWS = 9

    /** Най-малко редове — за да не е плоска графиката при малки числа. */
    private const val MIN_ROWS = 6

    /** Редовете: най-високата колона и един ред над нея — за числото. */
    fun rows(max: Int): Int = maxOf(MIN_ROWS, max + 1)

    /** Колони: бод през пролука — месец i е в колона 2i+1. */
    fun columns(months: Int): Int = 2 * months + 1

    /** Клетката се смалява, когато редовете са над [FULL_ROWS]; 1 — пълна. */
    fun cellScale(rows: Int): Float = if (rows <= FULL_ROWS) 1f else FULL_ROWS.toFloat() / rows

    /**
     * Колко от [BELT_SEGMENTS] отсечки на пояса са за всеки раздел (по [counts]): пропорционално,
     * но поне една за непразен раздел; сборът винаги е [BELT_SEGMENTS] (ако разделите са не повече от тях).
     */
    fun beltSegments(counts: List<Int>): IntArray {
        val sum = counts.sumOf { it.coerceAtLeast(0) }.toDouble()
        if (counts.isEmpty() || sum <= 0.0) return IntArray(counts.size)
        val out = IntArray(counts.size) { i ->
            if (counts[i] <= 0) 0 else maxOf(1, (counts[i] / sum * BELT_SEGMENTS).roundToInt())
        }
        // Изравняване до точния сбор: излишъкът се взема от най-голямата отсечка, липсата се дава на нея.
        var diff = BELT_SEGMENTS - out.sum()
        while (diff != 0) {
            val big = out.indices.maxByOrNull { out[it] } ?: break
            if (diff < 0 && out[big] <= 1) break
            out[big] += if (diff > 0) 1 else -1
            diff += if (diff > 0) -1 else 1
        }
        return out
    }

    /** Рекордният месец (при равенство — по-късният); -1 — няма книги. */
    fun peakIndex(counts: List<Int>): Int {
        val top = counts.maxOrNull() ?: return -1
        return if (top <= 0) -1 else counts.lastIndexOf(top)
    }

    /**
     * Схемата: по един бод за книга, отдолу нагоре, месец след месец отляво надясно.
     * Рекордният месец е със златен конец ([StitchCharts.GOLD]), останалите — бордо.
     */
    fun chart(counts: List<Int>): StitchChart {
        val n = counts.size
        val rows = rows(counts.maxOrNull()?.coerceAtLeast(0) ?: 0)
        val peak = peakIndex(counts)
        val col = ArrayList<Int>()
        val row = ArrayList<Int>()
        val thread = ArrayList<Byte>()
        counts.forEachIndexed { i, c ->
            for (k in 0 until c.coerceAtLeast(0).coerceAtMost(rows - 1)) {
                col += 2 * i + 1
                row += rows - 1 - k
                thread += if (i == peak) StitchCharts.GOLD else StitchCharts.BURGUNDY
            }
        }
        return StitchChart(
            columns(n), rows, col.toIntArray(), row.toIntArray(), thread.toByteArray(),
            FloatArray(col.size),
        )
    }
}

/**
 * Колони от кръстат бод със стойности и етикети на месеците. Клетката е по ширината (най-много
 * [maxCell]) и се смалява при колони над [ChartStitches.FULL_ROWS] бода; етикетите са точно под
 * колоните. [valueStyle] `null` — без числа над колоните. Рисуването е само при [progress].
 */
@Composable
internal fun EmbroideredMonths(
    counts: List<Int>,
    labels: List<String>,
    progress: () -> Float,
    thread: Color,
    peak: Color,
    baseline: Color,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle? = null,
    labelStyle: (Int) -> TextStyle,
    maxCell: Dp = 16.dp,
    labelColor: Color = Color.Unspecified,
) {
    val chart = remember(counts) { ChartStitches.chart(counts) }
    val measurer = rememberTextMeasurer()
    val total = chart.size
    // Накъде е свършила всяка колона (брой бодове до нея) — за появата на числата.
    val ends = remember(counts) { counts.runningFold(0) { acc, c -> acc + c.coerceAtLeast(0).coerceAtMost(chart.rows - 1) }.drop(1) }
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val cell = minOf(maxWidth / chart.columns, maxCell) * ChartStitches.cellScale(chart.rows)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(Modifier.padding(top = 6.dp).size(cell * chart.columns, cell * chart.rows)) {
                val p = progress()
                drawLine(baseline, Offset(0f, size.height - 0.5f), Offset(size.width, size.height - 0.5f), strokeWidth = 1.dp.toPx())
                drawStitches(chart, p, gold = peak, burgundy = thread)
                if (valueStyle != null) {
                    val c = size.width / chart.columns
                    counts.forEachIndexed { i, n ->
                        if (n <= 0) return@forEachIndexed
                        val shown = (p * total - ends[i] + 1f).coerceIn(0f, 1f)
                        if (shown <= 0f) return@forEachIndexed
                        val text = measurer.measure(n.toString(), valueStyle)
                        val rows = (n.coerceAtMost(chart.rows - 1))
                        drawText(
                            text,
                            topLeft = Offset((2 * i + 1.5f) * c - text.size.width / 2f, size.height - rows * c - text.size.height - 1.dp.toPx()),
                            alpha = shown,
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(Modifier.width(cell * (chart.columns - 1))) {
                labels.forEachIndexed { i, s ->
                    Text(
                        s, style = labelStyle(i), textAlign = TextAlign.Center, maxLines = 1,
                        color = labelColor,
                        modifier = Modifier.weight(1f),
                    )
                }
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
    val segments = remember(slices) { ChartStitches.beltSegments(slices.map { it.count }) }
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
                // Тъкан пояс: пръстенът е от кратки отсечки (по една „нишка“), цветът им — разделът;
                // нечетните отсечки са по-тънки и по-светли — като преплетени нишки.
                val sweep = 360f / BELT_SEGMENTS
                val shown = BELT_SEGMENTS * progress()
                val gap = 1.4f
                var j = 0
                for ((si, s) in slices.withIndex()) {
                    repeat(segments[si]) {
                        val visible = (shown - j).coerceIn(0f, 1f)
                        if (visible > 0f) {
                            val over = j % 2 == 0
                            drawArc(
                                if (over) s.color else lerp(s.color, Color.White, 0.22f),
                                -90f + j * sweep + gap / 2f, (sweep - gap) * visible, useCenter = false,
                                topLeft = topLeft, size = arcSize,
                                style = Stroke(if (over) stroke else stroke * 0.72f),
                            )
                        }
                        j++
                    }
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
