package org.chyavorec.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.unit.dp
import org.chyavorec.app.R

/**
 * Индикаторът „зарежда се“ на приложението: меките форми на Material 3 Expressive
 * ([LoadingIndicator]) се превръщат една в друга и се въртят — вместо стандартния кръг.
 * Само за неопределено чакане на съдържание; дърпането за опресняване има житния клас
 * ([WheatRefreshBox]), а процентите (изтегляне) — линейна лента.
 *
 * При намалено движение — неподвижна „бисквитка“ със същия цвят. За екранните четци е
 * един елемент: неопределен прогрес + [contentDescription] (по подразбиране „Зареждане…“).
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChLoadingIndicator(
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.loading),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val a11y = Modifier.clearAndSetSemantics {
        progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        if (contentDescription != null) this.contentDescription = contentDescription
    }
    if (rememberReducedMotion()) {
        // Пропорциите на LoadingIndicator: фигура 38 dp в поле 48 dp.
        Box(modifier.then(a11y).size(48.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.fillMaxSize(38f / 48f).background(color, MaterialShapes.Cookie9Sided.toShape()))
        }
    } else {
        LoadingIndicator(modifier.then(a11y), color = color)
    }
}
