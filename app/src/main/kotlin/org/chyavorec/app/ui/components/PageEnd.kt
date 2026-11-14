package org.chyavorec.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ShevitsaMotif

/**
 * Тих знак за край на страницата („Настройки“, „Документи“, „Контакти“, „Диагностика“, входът):
 * къса лента шевица ([ShevitsaStrip]) в приглушено злато, в средата. Това е единствената лента
 * на тези екрани (бюджетът на украсата). Неподвижна — пътят се строи веднъж, в покой не струва нищо.
 */
@Composable
fun PageEndMark(modifier: Modifier = Modifier) {
    val gold = LocalExtendedColors.current.gold
    Box(modifier.fillMaxWidth().padding(top = 20.dp, bottom = 28.dp), contentAlignment = Alignment.Center) {
        ShevitsaStrip(ShevitsaMotif.RHOMBUS, gold.copy(alpha = PAGE_END_ALPHA), Modifier.width(112.dp).height(10.dp))
    }
}

/** Колко е приглушено златото на знака — да се забелязва, без да привлича поглед. */
private const val PAGE_END_ALPHA = 0.5f
