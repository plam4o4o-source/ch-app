package org.chyavorec.app.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.chyavorec.app.R

/**
 * Имената на разделите по УДК на езика на интерфейса. Разделите са от приложението
 * (по първата цифра на УДК), не от каталога — затова са в ресурсите, а не в
 * [org.chyavorec.domain.model.UdcSections] (той остава за данните и търсенето).
 */
object UdcLabels {
    @StringRes
    fun res(section: Int?): Int = when (section) {
        0 -> R.string.udc_0
        1 -> R.string.udc_1
        2 -> R.string.udc_2
        3 -> R.string.udc_3
        5 -> R.string.udc_5
        6 -> R.string.udc_6
        7 -> R.string.udc_7
        8 -> R.string.udc_8
        9 -> R.string.udc_9
        else -> R.string.udc_none
    }
}

@Composable
fun udcSectionName(section: Int?): String = stringResource(UdcLabels.res(section))
