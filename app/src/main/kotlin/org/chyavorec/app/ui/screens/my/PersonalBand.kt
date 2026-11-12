package org.chyavorec.app.ui.screens.my

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import org.chyavorec.app.ui.components.DarkBurgundyThread
import org.chyavorec.app.ui.components.StitchChart
import org.chyavorec.app.ui.components.StitchMath
import org.chyavorec.app.ui.components.StitchedMotif
import org.chyavorec.app.ui.components.rememberReducedMotion
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.UdcCoverColors
import org.chyavorec.data.catalog.PersonalShevitsa
import org.chyavorec.data.catalog.ReadingYear

/*
 * „Твоята шевица“ в приложението: чистата схема от `shared` ([PersonalShevitsa]) се превежда на
 * [StitchChart] и се бродира със същия рисувач като останалите шевици. Лентата е винаги на тъмен
 * фон (слайдът и картата за споделяне са фирмени), затова конците са светлите им варианти.
 */

/** Схемата на лентата за годината като схема за бродиране. */
internal fun PersonalShevitsa.Chart.toStitchChart(): StitchChart {
    val cx = (columns - 1) / 2
    val cy = (rows - 1) / 2
    return StitchChart(
        columns, rows, x.copyOf(), y.copyOf(), thread.copyOf(),
        FloatArray(size) { StitchMath.angleOf(x[it] - cx, y[it] - cy) },
    )
}

/** Конците по раздели (индекс = цифрата по УДК), по-светли върху тъмния фон; липсващ раздел — приглушено злато. */
internal val SectionThreads: List<Color> = List(10) { s ->
    lerp(UdcCoverColors[s] ?: Brand.Muted, Color.White, 0.38f)
}

internal fun bandChart(s: ReadingYear.Summary): StitchChart = PersonalShevitsa.chart(s).toStitchChart()

internal fun bandStrip(s: ReadingYear.Summary): StitchChart = PersonalShevitsa.strip(s).toStitchChart()

/** Колко широка е лентата спрямо височината ѝ. */
internal const val BAND_ASPECT = PersonalShevitsa.COLUMNS.toFloat() / PersonalShevitsa.ROWS

/**
 * „Твоята шевица“: лентата се избродира от средата навън за [durationMs] и остава. [still] —
 * готова веднага (картата за споделяне, слайд, който не е текущ); при намалено движение — също.
 * Декоративна — описанието е на целия слайд.
 */
@Composable
internal fun PersonalBand(
    s: ReadingYear.Summary,
    modifier: Modifier = Modifier,
    still: Boolean = false,
    durationMs: Int = 2_500,
) {
    val reduced = rememberReducedMotion()
    val chart = remember(s) { bandChart(s) }
    var played by rememberSaveable { mutableStateOf(false) }
    val animate = !still && !reduced && !played
    val progress = remember { Animatable(if (animate) 0f else 1f) }
    if (animate) {
        LaunchedEffect(Unit) {
            progress.animateTo(1f, tween(durationMs, easing = LinearOutSlowInEasing))
            played = true
        }
    }
    StitchedMotif(
        chart,
        progress = { if (still || reduced) 1f else progress.value },
        modifier = modifier.fillMaxWidth().aspectRatio(BAND_ASPECT),
        gold = Brand.Gold,
        burgundy = DarkBurgundyThread,
        sections = SectionThreads,
    )
}

/** Тясната лента в долния край на картата за споделяне — винаги готова. */
@Composable
internal fun PersonalBandStrip(s: ReadingYear.Summary, modifier: Modifier = Modifier) {
    val chart = remember(s) { bandStrip(s) }
    StitchedMotif(
        chart, progress = { 1f },
        modifier = modifier.fillMaxWidth().aspectRatio(PersonalShevitsa.COLUMNS.toFloat() / 3f),
        gold = Brand.Gold, burgundy = DarkBurgundyThread, sections = SectionThreads,
    )
}
