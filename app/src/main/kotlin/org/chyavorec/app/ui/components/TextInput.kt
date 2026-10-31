package org.chyavorec.app.ui.components

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

/**
 * Ограничено въвеждане в поле с TextFieldState: всеки знак минава през [map], остават само
 * тези, за които [keep] е `true`, и най-много [max] — излишното отпада (и при поставяне), а
 * курсорът остава след въведеното. [onEdit] — след всяка промяна от потребителя (промените
 * от кода, напр. `setTextAndPlaceCursorAtEnd`, не минават оттук).
 */
fun limitedInput(
    max: Int,
    map: (Char) -> Char = { it },
    keep: (Char) -> Boolean = { true },
    onEdit: () -> Unit = {},
): InputTransformation = object : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val text = asCharSequence().toString()
        val cursorAt = selection.start
        val clean = StringBuilder()
        var cursor = 0
        text.forEachIndexed { i, ch ->
            val m = map(ch)
            if (keep(m) && clean.length < max) {
                clean.append(m)
                if (i < cursorAt) cursor = clean.length
            }
        }
        if (clean.toString() != text) {
            replace(0, length, clean)
            placeCursorBeforeCharAt(cursor)
        }
        onEdit()
    }
}
