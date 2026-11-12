package org.chyavorec.app.ui.share

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.chyavorec.app.R
import org.chyavorec.app.ui.screens.embroidery.StitchPicture
import org.chyavorec.app.ui.theme.Brand
import org.chyavorec.app.ui.theme.ShevitsaMotif

/** Картичката със собствената шевица (4:5): везаната кърпа в златна рамка, под етикет и бележка. */
@Composable
internal fun EmbroideryShareCard(card: ShareCard.Embroidery) {
    BrandCardFrame(card.format, card.url, motif = ShevitsaMotif.ROSETTE) {
        Spacer(Modifier.weight(0.3f))
        CardLabel(stringResource(R.string.embroidery_card_label), modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(8.dp))
        val shape = RoundedCornerShape(10.dp)
        Box(
            Modifier.align(Alignment.CenterHorizontally).size(206.dp).border(1.5.dp, Brand.Gold, shape).padding(5.dp),
        ) {
            StitchPicture(
                card.pattern, Modifier.size(194.dp),
                description = stringResource(R.string.embroidery_card_cd, card.pattern.stitchCount),
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            stringResource(R.string.embroidery_card_note), style = sans(12.sp, Brand.GoldLight), textAlign = TextAlign.Center,
            maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.weight(0.7f))
    }
}
