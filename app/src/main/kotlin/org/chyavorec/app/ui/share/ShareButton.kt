package org.chyavorec.app.ui.share

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.components.AppBottomSheet
import org.chyavorec.app.ui.theme.Brand

/**
 * Бутонът „Сподели“ в лентата на събитие, книга и новина: лист с избор — „Сподели като
 * картинка“ (карта с QR код, [ShareImageHost]) или връзката като текст ([onShareLink],
 * досегашното споделяне). Докато картинката се рисува, вместо иконата се върти индикатор.
 */
@Composable
fun ShareActionButton(onShareLink: () -> Unit, image: () -> ShareImageRequest?) {
    val state = rememberShareImageState()
    var sheet by remember { mutableStateOf<ShareImageRequest?>(null) }
    Box(contentAlignment = Alignment.Center) {
        IconButton(
            onClick = {
                val request = image()
                if (request == null) onShareLink() else sheet = request
            },
            enabled = !state.busy,
        ) {
            if (state.busy) {
                val busy = stringResource(R.string.share_image_busy)
                CircularProgressIndicator(Modifier.size(22.dp).semantics { contentDescription = busy }, strokeWidth = 2.dp)
            } else {
                Icon(Icons.Outlined.Share, contentDescription = stringResource(R.string.action_share))
            }
        }
        ShareImageHost(state)
    }
    sheet?.let { request ->
        ShareChoiceSheet(
            request,
            onDismiss = { sheet = null },
            onImage = { sheet = null; state.share(request) },
            onLink = { sheet = null; onShareLink() },
        )
    }
}

@Composable
private fun ShareChoiceSheet(request: ShareImageRequest, onDismiss: () -> Unit, onImage: () -> Unit, onLink: () -> Unit) {
    AppBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(
                stringResource(R.string.action_share), style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp).semantics { heading() },
            )
            Spacer(Modifier.height(8.dp))
            val colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            ListItem(
                headlineContent = { Text(stringResource(R.string.share_as_image)) },
                supportingContent = { Text(stringResource(R.string.share_as_image_desc)) },
                leadingContent = { CardThumbnail(request.card) },
                colors = colors,
                modifier = Modifier.clickable(onClick = onImage).padding(horizontal = 8.dp),
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.share_as_link)) },
                supportingContent = { Text(stringResource(R.string.share_as_link_desc)) },
                leadingContent = {
                    Box(Modifier.size(width = 54.dp, height = 54.dp), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Link, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = colors,
                modifier = Modifier.clickable(onClick = onLink).padding(horizontal = 8.dp),
            )
        }
    }
}

/** Умалена картата (без снимката) — какво ще се сподели; само за окото. */
@Composable
private fun CardThumbnail(card: ShareCard) {
    val width = 54.dp
    val height = width * (card.format.heightPx.toFloat() / card.format.widthPx)
    val screen = LocalDensity.current
    val shape = RoundedCornerShape(6.dp)
    Box(
        Modifier.size(width, height).clip(shape).border(1.dp, Brand.Gold.copy(alpha = 0.6f), shape).clearAndSetSemantics { },
        contentAlignment = Alignment.Center,
    ) {
        // Цялата карта (360 картови dp) в [width]: по-малка плътност, без мащаб на шрифта.
        val scaled = Density(screen.density * width.value / card.format.widthDp.value, 1f)
        CompositionLocalProvider(LocalDensity provides scaled) {
            Box(Modifier.requiredSize(card.format.widthDp, card.format.heightDp)) { ShareCardContent(card, photo = null) }
        }
    }
}
