package org.chyavorec.app.ui.share

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.AppBottomSheet
import org.chyavorec.app.ui.components.limitedInput
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaMotif
import org.chyavorec.domain.occasion.GreetingCards
import org.chyavorec.domain.occasion.Occasion
import org.chyavorec.domain.occasion.OccasionOrnament

/**
 * Малкият редактор на честитката: лист с преглед на картата, поле за личен ред и „Сподели“.
 * Картата се рисува на телефона през [share] (същият път като другите карти); листът се
 * затваря, щом споделянето приключи. [share] е на нивото на екрана (не в листа), за да не се
 * прекъсне рисуването, ако листът се затвори.
 */
@Composable
fun GreetingComposerSheet(
    occasion: Occasion,
    ornament: OccasionOrnament,
    motif: ShevitsaMotif,
    url: String,
    share: ShareImageState,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val (title, wish) = remember(occasion, context) { GreetingTexts.texts(context, occasion) } ?: return
    val line = rememberTextFieldState()
    var started by remember { mutableStateOf(false) }
    val busy = share.busy
    // Споделянето започна и приключи (картата е отворена в „Сподели“ или не се е получило) — листът се затваря.
    LaunchedEffect(busy) { if (started && !busy) onDismiss() }
    AppBottomSheet(onDismissRequest = onDismiss) {
        GreetingComposerBody(
            title = title, wish = wish, occasionId = occasion.id, ornament = ornament, motif = motif, url = url, line = line, busy = busy,
            onShare = { card ->
                started = true
                val text = listOf(card.title, card.line, card.wish, url).filter { it.isNotBlank() }.joinToString("\n")
                share.share(ShareImageRequest(card, card.title, text))
            },
        )
    }
}

/** Съдържанието на редактора — без листа и без споделянето (за тестовете и снимките). */
@Composable
fun GreetingComposerBody(
    title: String,
    wish: String,
    occasionId: String,
    ornament: OccasionOrnament,
    motif: ShevitsaMotif,
    url: String,
    line: TextFieldState,
    busy: Boolean,
    onShare: (ShareCard.Greeting) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clean = GreetingCards.sanitizeLine(line.text.toString())
    val card = remember(occasionId, title, wish, clean, ornament, motif, url) {
        ShareCard.Greeting(occasionId, title, wish, clean, ornament, motif, url)
    }
    // Празните знаци (поставен нов ред) стават интервал; непозволените (емоджита, символи) не се приемат.
    val input = remember { limitedInput(GreetingCards.MAX_LINE, map = { if (it.isWhitespace()) ' ' else it }, keep = GreetingCards::isAllowed) }
    Column(
        modifier.fillMaxWidth().verticalScroll(rememberScrollState()).imePadding().padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            stringResource(R.string.greeting_composer_title), style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth().semantics { heading() },
        )
        Text(
            stringResource(R.string.greeting_composer_text), style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(),
        )
        GreetingPreview(card, previewWidth = 168.dp)
        OutlinedTextField(
            state = line,
            inputTransformation = input,
            label = { Text(stringResource(R.string.greeting_line_label)) },
            placeholder = { Text(stringResource(R.string.greeting_line_placeholder)) },
            supportingText = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.greeting_line_support, GreetingCards.MAX_LINE), Modifier.weight(1f))
                    Text("${line.text.length}/${GreetingCards.MAX_LINE}")
                }
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = { onShare(card) }, enabled = !busy, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            if (busy) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Icon(Icons.Outlined.Share, null, Modifier.size(18.dp))
            }
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.greeting_share))
        }
    }
}

/** Умалената карта — какво ще се сподели. За екранния четец — текстът ѝ. */
@Composable
private fun GreetingPreview(card: ShareCard.Greeting, previewWidth: androidx.compose.ui.unit.Dp) {
    val height = previewWidth * (card.format.heightPx.toFloat() / card.format.widthPx)
    val screen = LocalDensity.current
    val shape = RoundedCornerShape(12.dp)
    val description = stringResource(R.string.greeting_preview_cd, card.title, listOf(card.line, card.wish).filter { it.isNotBlank() }.joinToString(". "))
    Box(
        Modifier.size(previewWidth, height).clip(shape).border(1.dp, Brand.Gold.copy(alpha = 0.6f), shape).clearAndSetSemantics { contentDescription = description },
    ) {
        // Цялата карта (360 картови dp) в [previewWidth]: по-малка плътност, без мащаб на шрифта.
        val scaled = Density(screen.density * previewWidth.value / card.format.widthDp.value, 1f)
        CompositionLocalProvider(LocalDensity provides scaled) {
            Box(Modifier.requiredSize(card.format.widthDp, card.format.heightDp)) { ShareCardContent(card, photo = null) }
        }
    }
}
