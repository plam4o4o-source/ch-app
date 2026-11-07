package org.chyavorec.app.ui.screens.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R
import org.chyavorec.app.ui.theme.Brand

/** Мастилен фон и златен кант — като рамката на снимка в албум. */
private val ToolbarFill = Brand.Ink.copy(alpha = 0.88f)
private val ToolbarEdge = Brand.Gold.copy(alpha = 0.5f)

/**
 * Плаващата лента на прегледа на снимки (по Material 3 Expressive „floating toolbar“):
 * хапче долу в средата с действията, които прегледът има — „Сподели“, „Отвори в сайта“ и
 * „За снимката“ (описанието ѝ на цял текст над лентата; само ако снимката има описание).
 * Скрива се заедно с горната лента при тап по снимката.
 */
@Composable
internal fun ViewerToolbar(
    caption: String,
    info: Boolean,
    onInfo: (Boolean) -> Unit,
    onShare: () -> Unit,
    onOpenSite: (() -> Unit)?,
    reduced: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AnimatedVisibility(
            info && caption.isNotBlank(),
            enter = if (reduced) EnterTransition.None else fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = if (reduced) ExitTransition.None else fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
        ) {
            Text(
                caption,
                color = Brand.Cream,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 8,
                modifier = Modifier.widthIn(max = 560.dp)
                    .background(ToolbarFill, RoundedCornerShape(20.dp))
                    .border(1.dp, ToolbarEdge, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
        Row(
            Modifier.shadow(10.dp, CircleShape)
                .background(ToolbarFill, CircleShape)
                .border(1.dp, ToolbarEdge, CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val tint = Brand.Cream
            IconButton(onClick = onShare) { Icon(Icons.Outlined.Share, stringResource(R.string.action_share), tint = tint) }
            if (onOpenSite != null) {
                IconButton(onClick = onOpenSite) { Icon(Icons.AutoMirrored.Outlined.OpenInNew, stringResource(R.string.action_open_site), tint = tint) }
            }
            if (caption.isNotBlank()) {
                // Включено — златен кръг с мастилена икона.
                IconToggleButton(
                    checked = info, onCheckedChange = onInfo,
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        contentColor = tint, checkedContainerColor = Brand.Gold, checkedContentColor = Brand.Ink,
                        containerColor = Color.Transparent,
                    ),
                ) { Icon(Icons.Outlined.Info, stringResource(R.string.photo_info)) }
            }
        }
    }
}
