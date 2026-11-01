package org.chyavorec.app.ui.navigation

import android.content.Context
import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.KeyboardShortcutInfo
import org.chyavorec.app.R

/** Команда от физическа клавиатура (таблет, Chromebook). */
sealed interface KeyCommand {
    /** Ctrl+F или „/“ — търсенето на текущия раздел. */
    data object Search : KeyCommand

    /** Ctrl+1…5 — разделът с този индекс (от 0) в [TopTab]. */
    data class Tab(val index: Int) : KeyCommand

    /** Esc — като „Назад“. */
    data object Back : KeyCommand
}

/**
 * Клавишните комбинации на приложението. Обработват се едва ако екранът не е обработил
 * клавиша сам — „/“ в поле за текст си остава знак, а Esc първо изважда фокуса от полето.
 */
object KeyboardShortcuts {
    private val tabKeys = listOf(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5)

    /** Командата за натиснатия клавиш или `null` (само при натискане, без автоповторение). */
    fun command(event: KeyEvent): KeyCommand? {
        if (event.action != KeyEvent.ACTION_DOWN || event.repeatCount != 0) return null
        val code = event.keyCode
        return when {
            event.hasModifiers(KeyEvent.META_CTRL_ON) -> when (code) {
                KeyEvent.KEYCODE_F -> KeyCommand.Search
                in tabKeys -> KeyCommand.Tab(tabKeys.indexOf(code))
                else -> null
            }
            !event.hasNoModifiers() -> null
            code == KeyEvent.KEYCODE_SLASH || code == KeyEvent.KEYCODE_NUMPAD_DIVIDE -> KeyCommand.Search
            code == KeyEvent.KEYCODE_ESCAPE -> KeyCommand.Back
            else -> null
        }
    }

    /**
     * Активността: [dispatched] — обичайната обработка (екранът); необработен клавиш с команда
     * отива в [onCommand] и се счита за обработен.
     */
    fun dispatch(event: KeyEvent, dispatched: (KeyEvent) -> Boolean, onCommand: (KeyCommand) -> Unit): Boolean {
        if (dispatched(event)) return true
        val command = command(event) ?: return false
        onCommand(command)
        return true
    }

    /** За системния списък с комбинации (Meta+/). */
    fun group(context: Context): KeyboardShortcutGroup {
        val search = context.getString(R.string.action_search)
        val items = listOf(
            KeyboardShortcutInfo(search, KeyEvent.KEYCODE_F, KeyEvent.META_CTRL_ON),
            KeyboardShortcutInfo(search, '/', 0),
        ) + TopTab.entries.mapIndexed { i, tab ->
            KeyboardShortcutInfo(context.getString(tab.labelRes), tabKeys[i], KeyEvent.META_CTRL_ON)
        } + KeyboardShortcutInfo(context.getString(R.string.action_back), KeyEvent.KEYCODE_ESCAPE, 0)
        return KeyboardShortcutGroup(context.getString(R.string.app_title), items)
    }
}
