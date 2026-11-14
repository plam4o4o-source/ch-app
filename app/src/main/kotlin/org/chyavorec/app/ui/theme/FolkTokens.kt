package org.chyavorec.app.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.components.StitchSpec

/**
 * Народният речник на приложението — малко на брой мерки, от които се строят всички украси.
 *
 * **Бюджет на украсата** (правилото, което пазим): на един екран — една лента (бродирана
 * шевица/бордюр) и един знаков елемент (розетка, печат, медалия); картите получават само рамка
 * — без собствени шевици, пунктири и текстури отгоре. Пунктирите и „X“-овете идват само от
 * [StitchSpec]; заобляния — само [RadiusSmall]/[RadiusCard]/[RadiusLarge] (или
 * `MaterialTheme.shapes`), златната рамка — само [goldBorder]. `ScreenTokensTest` следи
 * `ui/screens/` за сурови `dashPathEffect(`, `RoundedCornerShape(N.dp)` и `Color(0x…)`.
 */
object FolkTokens {
    /** Единствената прозрачност на златната рамка (карти-акцент, медалии, рамки на листове). */
    const val GOLD_BORDER_ALPHA = 0.5f

    /** Дебелината на рамката на карта. */
    val BorderWidth = 1.dp

    /** Малко заобляне: миниатюри, етикети, редове в листове, платното. Равно на `shapes.small`. */
    val RadiusSmall = 12.dp

    /** Заоблянето на картите ([org.chyavorec.app.ui.components.AppCard]). Равно на `shapes.medium`. */
    val RadiusCard = 20.dp

    /** Голямо заобляне: големи снимки, листове отдолу. Равно на `shapes.large`. */
    val RadiusLarge = 28.dp

    /**
     * Документирано изключение: хартията на „Старият вестник“ — почти прав ъгъл (4 dp), защото е
     * лист, а не карта.
     */
    val PaperRadius = 4.dp

    val SmallShape = RoundedCornerShape(RadiusSmall)
    val PaperShape = RoundedCornerShape(PaperRadius)

    /** Трите бода — [StitchSpec.Running], [StitchSpec.Basting], [StitchSpec.Cross]. */
    val Stitch = StitchSpec

    /** Златната рамка на карта-акцент: злато на темата при [GOLD_BORDER_ALPHA]. */
    @Composable
    fun goldBorder(): BorderStroke = BorderStroke(BorderWidth, LocalExtendedColors.current.gold.copy(alpha = GOLD_BORDER_ALPHA))
}
