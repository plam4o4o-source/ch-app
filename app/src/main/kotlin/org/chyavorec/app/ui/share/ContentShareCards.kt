package org.chyavorec.app.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.DarkBurgundyThread
import org.chyavorec.app.ui.screens.quiz.QuizBandCanvas
import org.chyavorec.app.ui.screens.quiz.QuizBandCharts
import org.chyavorec.app.ui.screens.quiz.QuizBandColors
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaMotif

/*
 * Картите за споделяне на „Старият вестник“ и „Познай Яворец“ — в същата рамка като другите
 * (BrandCardFrame: двойна златна линия, шевица по ъглите, QR кодът долу).
 */

/** Статия от юбилейния вестник: заглавие, рубриката ѝ и QR към PDF-а. */
@Immutable
data class NewspaperShareCard(val title: String, val label: String?, override val url: String) : ShareCard {
    override val format get() = ShareFormat.FEED
    override val photoUrl: String? get() = null
    override val fileName get() = "newspaper"
}

/** Резултатът от викторината: [results] — верен/грешен по въпрос; [streak] — дни поред (0 — не се показва). */
@Immutable
data class QuizShareCard(val dateLine: String, val results: List<Boolean>, val streak: Int, override val url: String) : ShareCard {
    override val format get() = ShareFormat.STORY
    override val photoUrl: String? get() = null
    override val fileName get() = "quiz"
    val score: Int get() = results.count { it }
}

object ContentShareCards {
    fun newspaper(title: String, label: String?, url: String) = NewspaperShareCard(title, label?.takeIf { it.isNotBlank() }, url)

    fun quiz(dateLine: String, results: List<Boolean>, streak: Int, url: String) = QuizShareCard(dateLine, results, streak, url)
}

private val InkOnPaper = Color(0xFF2A1E12)

/** Лентата на картата: винаги върху тъмната рамка. */
private val ShareBandColors = QuizBandColors(
    gold = Brand.Gold, burgundy = DarkBurgundyThread, holes = Brand.Parchment.copy(alpha = 0.45f), current = Brand.Gold,
    cloth = Color.White.copy(alpha = 0.05f), hem = Brand.Gold.copy(alpha = 0.5f),
)
private val SheetTone = Color(0xFFF4ECD8)

@Composable
internal fun NewspaperShareCardContent(card: NewspaperShareCard) {
    BrandCardFrame(card.format, card.url, motif = ShevitsaMotif.RHOMBUS) {
        Spacer(Modifier.weight(0.25f))
        // Лист от стар вестник: пергамент, шапка, заглавие и редове „текст“.
        Column(
            Modifier.fillMaxWidth().background(SheetTone, RoundedCornerShape(4.dp)).border(1.dp, Brand.Gold, RoundedCornerShape(4.dp))
                .padding(horizontal = 22.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.fillMaxWidth().height(1.5.dp).background(InkOnPaper))
            Spacer(Modifier.height(4.dp))
            Box(Modifier.fillMaxWidth().height(0.7.dp).background(InkOnPaper.copy(alpha = 0.6f)))
            Spacer(Modifier.height(12.dp))
            Text(stringResource(R.string.share_card_newspaper).uppercase(), style = sans(11.sp, Brand.Burgundy, FontWeight.Bold, spacing = 0.2f), maxLines = 1)
            Spacer(Modifier.height(10.dp))
            Text(
                card.title, style = serif(titleSize(card.title, 34f), InkOnPaper), maxLines = 4, overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
            card.label?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = sans(12.sp, Brand.GoldDark, FontWeight.SemiBold), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.height(16.dp))
            // Редове „текст“ — нарисувани, без съдържание.
            listOf(1f, 0.94f, 1f, 0.88f, 0.97f, 0.7f).forEach { w ->
                Box(Modifier.fillMaxWidth(w).height(3.dp).background(InkOnPaper.copy(alpha = 0.22f), RoundedCornerShape(2.dp)))
                Spacer(Modifier.height(7.dp))
            }
        }
        Spacer(Modifier.weight(0.15f))
        Text(
            stringResource(R.string.share_card_newspaper_cta), style = serif(18.sp, Brand.Parchment, FontWeight.Normal), textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
        )
    }
}

@Composable
internal fun QuizShareCardContent(card: QuizShareCard) {
    BrandCardFrame(card.format, card.url, motif = ShevitsaMotif.ROSETTE) {
        Spacer(Modifier.weight(0.5f))
        CardLabel(stringResource(R.string.share_card_quiz), modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(6.dp))
        Text(card.dateLine, style = sans(13.sp, Brand.GoldLight), modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(26.dp))
        Text(
            "${card.score}/${card.results.size}", style = serif(120.sp, Brand.Gold, FontWeight.SemiBold, lineHeight = 1f),
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
        )
        Text(
            stringResource(R.string.share_card_quiz_score, card.score, card.results.size), style = serif(22.sp, Brand.Parchment, FontWeight.Normal),
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
        )
        if (card.streak > 1) {
            Spacer(Modifier.height(20.dp))
            Text(
                pluralStringResource(R.plurals.share_card_quiz_streak, card.streak, card.streak), style = sans(15.sp, Brand.GoldLight, FontWeight.SemiBold),
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(Modifier.weight(1f))
        // Долу — избродираната лента от викторината (същата като на екрана): мотив за всеки верен отговор.
        QuizBandCanvas(
            card.results, card.results.size, current = null, colors = ShareBandColors,
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.86f).aspectRatio(QuizBandCharts.aspect(card.results.size)),
        )
        Spacer(Modifier.height(18.dp))
        Text(
            stringResource(R.string.share_card_quiz_cta), style = serif(20.sp, Brand.Parchment, FontWeight.Normal), textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
        )
    }
}
