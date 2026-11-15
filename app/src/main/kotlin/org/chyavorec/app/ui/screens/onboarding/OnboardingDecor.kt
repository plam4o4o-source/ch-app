package org.chyavorec.app.ui.screens.onboarding

import org.chyavorec.app.ui.components.StitchSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Event
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.Emblem
import org.chyavorec.app.ui.components.FolkIconPlate
import org.chyavorec.app.ui.components.FolkIcons
import org.chyavorec.app.ui.components.FolkMotif
import org.chyavorec.app.ui.components.LivingShevitsa
import org.chyavorec.app.ui.components.ShevitsaPalettes
import org.chyavorec.app.ui.components.WeaveBudget
import org.chyavorec.app.ui.components.drawGoldRing
import org.chyavorec.app.ui.screens.my.SampleCardPreview
import org.chyavorec.app.ui.theme.Brand
import kotlin.math.max

/**
 * Печатът в тъкана рамка — същият замисъл като във въведението на логото ([BrandIntro]):
 * „жива шевица“ с празен кръг в средата, златен пръстен и печатът вътре. Шевицата започва
 * от неподвижната плочка (първият кадър), снопът светлина минава веднъж ([budget]) и тя застива.
 */
@Composable
internal fun SealMedallion(size: Dp, budget: WeaveBudget, modifier: Modifier = Modifier) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        LivingShevitsa(
            ShevitsaPalettes.card.copy(threadAlpha = 0.55f), Modifier.matchParentSize().clip(RoundedCornerShape(size * 0.14f)),
            cell = 3.dp, clearCenter = 0.6f, budget = budget,
        )
        Canvas(Modifier.size(size * 0.7f).clearAndSetSemantics { }) { drawGoldRing(1f) }
        Emblem(size * 0.58f)
    }
}

/**
 * Плочката с икона във въведението — същата [FolkIconPlate] като в приложението, в злато върху
 * тъмния фон. Възможностите на приложението са с иконите от набора „Бод“ ([folkFeature]):
 * новините и събитията — осморъка (читалището), каталогът и картата — ромб (библиотеката).
 */
@Composable
internal fun KnotPlate(icon: ImageVector, size: Dp, modifier: Modifier = Modifier) {
    val (folk, motif) = folkFeature(icon)
    FolkIconPlate(
        folk, motif, modifier.clearAndSetSemantics { }, size = size,
        container = Brand.Gold.copy(alpha = 0.3f), content = Brand.GoldLight,
    )
}

/** Стандартната икона на възможност → иконата „Бод“ и формата на плочката (другите остават). */
private fun folkFeature(icon: ImageVector): Pair<ImageVector, FolkMotif> = when (icon) {
    Icons.Outlined.Event -> FolkIcons.Events to FolkMotif.ROSETTE
    Icons.AutoMirrored.Outlined.MenuBook -> FolkIcons.Catalog to FolkMotif.RHOMBUS
    Icons.Outlined.Badge -> FolkIcons.Card to FolkMotif.RHOMBUS
    else -> icon to FolkMotif.ROSETTE
}

/**
 * Страниците като нишка между [count] възела: бродирана нишка (прекъсната) със златна част до
 * изминатото, възлите са ромбове — минатите и текущият са златни (текущият е по-голям), бъдещите — контур.
 * [position] (страница + дял от плъзгането) се чете само при рисуване — следва пръста без прекомпозиция.
 */
@Composable
internal fun ThreadDots(count: Int, position: () -> Float, modifier: Modifier = Modifier, current: Int = 0) {
    if (count < 2) return
    val label = stringResource(R.string.onb_page_of, current + 1, count)
    val dim = Brand.Parchment.copy(alpha = 0.4f)
    Canvas(
        modifier.size(width = 52.dp * (count - 1) + 36.dp, height = 28.dp).semantics { contentDescription = label },
    ) {
        val gap = 52.dp.toPx()
        val x0 = 18.dp.toPx()
        val y = size.height / 2f
        val stroke = 2.dp.toPx()
        val pos = position().coerceIn(0f, (count - 1).toFloat())
        val bod = StitchSpec.Basting.stroke(density)
        // Нишката: бродирана (тропосване) отдолу, плътно злато до изминатото.
        drawLine(dim, Offset(x0, y), Offset(x0 + gap * (count - 1), y), strokeWidth = bod.width, pathEffect = bod.pathEffect, cap = StrokeCap.Round)
        if (pos > 0f) drawLine(Brand.Gold, Offset(x0, y), Offset(x0 + gap * pos, y), strokeWidth = stroke, cap = StrokeCap.Round)
        for (i in 0 until count) {
            val cx = x0 + gap * i
            // Колко „близо“ до възела е погледът: 1 в самия него, 0 на цяла страница разстояние.
            val near = (1f - kotlin.math.abs(pos - i)).coerceIn(0f, 1f)
            val r = (6.dp.toPx() + 4.dp.toPx() * near)
            if (i <= pos + 0.001f || near > 0f) diamond(cx, y, r, Brand.Gold, fill = true) else diamond(cx, y, r, dim, fill = false, stroke = stroke)
            if (near > 0.5f) diamond(cx, y, r * 0.38f, Brand.Ink, fill = true)
        }
    }
}

private fun DrawScope.diamond(cx: Float, cy: Float, r: Float, color: Color, fill: Boolean, stroke: Float = 0f) {
    val p = Path().apply {
        moveTo(cx, cy - r); lineTo(cx + r, cy); lineTo(cx, cy + r); lineTo(cx - r, cy); close()
    }
    if (fill) drawPath(p, color) else drawPath(p, color, style = Stroke(width = max(stroke, 1f)))
}

/**
 * Примерната читателска карта на въведението — същата като на „Моето“ и входа ([SampleCardPreview]),
 * върху тъмния фон; сензорът тръгва с появата (страницата кани да се наклони телефонът).
 */
@Composable
internal fun SampleCard(large: Boolean, modifier: Modifier = Modifier) {
    SampleCardPreview(
        modifier, sharedKey = null, live = true, maxWidth = if (large) 300.dp else 340.dp,
        hint = stringResource(R.string.onb_sample_hint), onDark = true,
    )
}

/** Тънка лента шевица най-горе — първото, което се вижда: фирменият знак още от първия екран. */
@Composable
internal fun OnboardingRibbon(modifier: Modifier = Modifier) {
    org.chyavorec.app.ui.components.ShevitsaStrip(
        org.chyavorec.app.ui.theme.ShevitsaMotif.RHOMBUS, Brand.Gold.copy(alpha = 0.55f), modifier.fillMaxWidth().height(14.dp),
    )
}
