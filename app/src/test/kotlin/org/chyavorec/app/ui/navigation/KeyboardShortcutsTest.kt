package org.chyavorec.app.ui.navigation

import android.app.Application
import android.view.KeyEvent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Клавишните комбинации (таблет, Chromebook): кой клавиш каква команда е и кога не е. */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg")
class KeyboardShortcutsTest {
    private fun down(code: Int, meta: Int = 0, repeat: Int = 0) = KeyEvent(0L, 0L, KeyEvent.ACTION_DOWN, code, repeat, meta)
    private val ctrl = KeyEvent.META_CTRL_ON or KeyEvent.META_CTRL_LEFT_ON

    @Test fun searchKeys() {
        assertEquals(KeyCommand.Search, KeyboardShortcuts.command(down(KeyEvent.KEYCODE_F, ctrl)))
        assertEquals(KeyCommand.Search, KeyboardShortcuts.command(down(KeyEvent.KEYCODE_SLASH)))
        assertEquals(KeyCommand.Search, KeyboardShortcuts.command(down(KeyEvent.KEYCODE_NUMPAD_DIVIDE)))
        // Само „F“ (буква в полето), Shift+/ („?“) и Ctrl+Shift+F — не.
        assertNull(KeyboardShortcuts.command(down(KeyEvent.KEYCODE_F)))
        assertNull(KeyboardShortcuts.command(down(KeyEvent.KEYCODE_SLASH, KeyEvent.META_SHIFT_ON or KeyEvent.META_SHIFT_LEFT_ON)))
        assertNull(KeyboardShortcuts.command(down(KeyEvent.KEYCODE_F, ctrl or KeyEvent.META_SHIFT_ON or KeyEvent.META_SHIFT_LEFT_ON)))
    }

    @Test fun ctrlDigitsSwitchTheFiveTabs() {
        val codes = listOf(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5)
        codes.forEachIndexed { i, code -> assertEquals(KeyCommand.Tab(i), KeyboardShortcuts.command(down(code, ctrl))) }
        assertEquals(TopTab.entries.size, codes.size)
        assertNull(KeyboardShortcuts.command(down(KeyEvent.KEYCODE_6, ctrl)))
        // Цифра без Ctrl се пише (напр. инвентарен номер).
        assertNull(KeyboardShortcuts.command(down(KeyEvent.KEYCODE_2)))
    }

    @Test fun escapeIsBack() {
        assertEquals(KeyCommand.Back, KeyboardShortcuts.command(down(KeyEvent.KEYCODE_ESCAPE)))
    }

    @Test fun onlyTheFirstPress() {
        assertNull(KeyboardShortcuts.command(KeyEvent(0L, 0L, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ESCAPE, 0, 0)))
        assertNull(KeyboardShortcuts.command(down(KeyEvent.KEYCODE_F, ctrl, repeat = 1)))
    }

    /** Клавиш, обработен от екрана (напр. „/“ в поле за текст), не е команда. */
    @Test fun dispatchLeavesHandledKeysAlone() {
        val got = mutableListOf<KeyCommand>()
        assertTrue(KeyboardShortcuts.dispatch(down(KeyEvent.KEYCODE_SLASH), { true }) { got += it })
        assertEquals(emptyList<KeyCommand>(), got)
        assertTrue(KeyboardShortcuts.dispatch(down(KeyEvent.KEYCODE_SLASH), { false }) { got += it })
        assertEquals(listOf<KeyCommand>(KeyCommand.Search), got)
        // Необработен клавиш без команда остава необработен (системата да реши).
        assertFalse(KeyboardShortcuts.dispatch(down(KeyEvent.KEYCODE_A), { false }) { got += it })
        assertEquals(1, got.size)
    }

    /** Системният списък (Meta+/): търсенето два пъти, петте раздела и „Назад“. */
    @Test fun shortcutsHelperGroup() {
        val group = KeyboardShortcuts.group(ApplicationProvider.getApplicationContext())
        assertEquals("Читалище Яворец", group.label)
        val items = group.items
        assertEquals(8, items.size)
        assertEquals(listOf("Търсене", "Търсене", "Начало", "Каталог", "Моето", "Новини", "Още", "Назад"), items.map { it.label.toString() })
        assertEquals(KeyEvent.KEYCODE_F, items[0].keycode)
        assertEquals(KeyEvent.META_CTRL_ON, items[0].modifiers)
        assertEquals('/', items[1].baseCharacter)
        assertEquals(KeyEvent.KEYCODE_5, items[6].keycode)
        assertEquals(KeyEvent.KEYCODE_ESCAPE, items[7].keycode)
    }
}
