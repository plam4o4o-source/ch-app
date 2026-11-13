package org.chyavorec.app.ui

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.text.TextLayoutResult

/**
 * Текстът [label] не е отрязан (без „…“) и не е счупен насред дума: всеки пренос на ред е
 * на интервал, не вътре в дума (при сричкопренасяне би бил с тире — за етикети то не е позволено).
 * Проверява всички текстове с този надпис (и в немергнатото дърво).
 */
fun ComposeTestRule.assertNoMidWordBreak(label: String, maxLines: Int = Int.MAX_VALUE) {
    waitForIdle()
    val layouts = onAllNodes(hasText(label), useUnmergedTree = true).fetchSemanticsNodes().mapNotNull { n ->
        n.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.let { get ->
            mutableListOf<TextLayoutResult>().also { get(it) }.firstOrNull()
        }
    }
    if (layouts.isEmpty()) throw AssertionError("няма текст „$label“")
    layouts.forEach { l ->
        val text = l.layoutInput.text.text
        // hasVisualOverflow тук не ставa: при центриран/обгърнат текст дава фалшиво „true“ (ширина на абзаца ≠ на възела).
        if (l.multiParagraph.didExceedMaxLines || (0 until l.lineCount).any { l.isLineEllipsized(it) }) throw AssertionError(
            "„$label“ се реже: exceeded=${l.multiParagraph.didExceedMaxLines} ellipsized=${(0 until l.lineCount).map { l.isLineEllipsized(it) }} " +
                "lines=${l.lineCount} size=${l.size} maxWidth=${l.layoutInput.constraints.maxWidth} maxHeight=${l.layoutInput.constraints.maxHeight} " +
                "density=${l.layoutInput.density.fontScale} text=\"$text\"",
        )
        if (l.lineCount > maxLines) throw AssertionError("„$label“ е на ${l.lineCount} реда (най-много $maxLines)")
        for (i in 0 until l.lineCount - 1) {
            val end = l.getLineEnd(i)
            val atSpace = end in 1..text.length && (text[end - 1].isWhitespace() || (end < text.length && text[end].isWhitespace()))
            if (!atSpace) throw AssertionError("„$label“ е счупен насред дума: „${text.substring(l.getLineStart(i), end)}“ | „${text.substring(end)}“")
        }
    }
}
