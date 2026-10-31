package org.chyavorec.app.ui.screens.my

import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.chyavorec.app.R
import org.chyavorec.app.data.local.SettingsStore
import org.chyavorec.app.tile.CardTile
import org.chyavorec.app.ui.LocalAppContainer
import org.chyavorec.app.ui.theme.LocalExtendedColors

/**
 * Еднократното „Добави в бързите настройки“ под картата (Android 13+, системният диалог за
 * плочката [org.chyavorec.app.tile.CardTileService]). Не се предлага отново след отговор в
 * диалога или „×“. На по-стар Android плочката се добавя ръчно от редакцията на панела.
 */
@Composable
fun CardTilePrompt(
    modifier: Modifier = Modifier,
    settings: SettingsStore = LocalAppContainer.current.settings,
    /** Показва системния диалог; вика [onAnswer] при отговор (подменя се в тестовете). */
    request: (Context, onAnswer: () -> Unit) -> Unit = { context, onAnswer ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) CardTile.requestAdd(context, onAnswer)
    },
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    // Докато настройката не е прочетена — скрито (без мигване за вече отговорилите).
    val done by settings.cardTilePromptDone.collectAsStateWithLifecycle(initialValue = true)
    if (done) return
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val finish: () -> Unit = { scope.launch { settings.setCardTilePromptDone() } }
    Surface(
        onClick = { request(context, finish) },
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(R.drawable.ic_tile_card), contentDescription = null, tint = LocalExtendedColors.current.gold, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(
                stringResource(R.string.card_tile_add), style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f).padding(vertical = 12.dp),
            )
            IconButton(onClick = finish) {
                Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.card_tile_dismiss))
            }
        }
    }
}
