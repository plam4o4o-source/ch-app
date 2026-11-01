package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.data.local.TextScale
import org.chyavorec.app.ui.components.AppAlertDialog
import org.chyavorec.app.ui.components.AppDropdownMenu
import org.chyavorec.app.ui.components.LimitFontScale
import org.chyavorec.app.ui.components.WhatsNewSheet
import org.chyavorec.app.ui.screens.settings.ChoiceDialog
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Диалозите, листовете и менютата са отделни прозорци — без обвивките (AppDialogs) текстът
 * в тях е със системния мащаб, не с „Размер на текста“. Снимките при 2× се записват до тези
 * на ScreenshotTest (app/build/reports/screenshots/).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class DialogFontScaleTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    /** „Много голям“ при системен мащаб 1× — както в ChitalishteRoot. */
    private val xlarge = appFontScale(1f, TextScale.XLARGE.factor)

    /** Мащабът от „Размер на текста“, подаден като в ChitalishteRoot (системният тук е 1×). */
    @Composable
    private fun AppScale(scale: Float, content: @Composable () -> Unit) {
        val d = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale = scale)) {
            ChitalishteTheme(mode = ThemeMode.LIGHT, content = content)
        }
    }

    /** Мащабът, с който е подреден текстът [text] (в който и да е прозорец). */
    private fun fontScaleOf(text: String): Float {
        val m = hasText(text)
        compose.waitUntil(10_000) { compose.onAllNodes(m, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty() }
        val node = compose.onAllNodes(m, useUnmergedTree = true).fetchSemanticsNodes().first()
        val get = node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action ?: throw AssertionError("„$text“ не е текст")
        return mutableListOf<TextLayoutResult>().also { get(it) }.first().layoutInput.density.fontScale
    }

    private fun shot(name: String) {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_500)
        compose.waitForIdle()
        val bmp = compose.captureScreen()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Самият AlertDialog губи мащаба — затова са обвивките. */
    @Test fun plainDialogUsesTheSystemScale() {
        compose.setContent {
            AppScale(xlarge) {
                AlertDialog(onDismissRequest = {}, confirmButton = {}, text = { Text("Обикновен диалог") })
            }
        }
        assertEquals(1f, fontScaleOf("Обикновен диалог"), 0.001f)
    }

    @Test fun alertDialogKeepsTheAppScale() {
        compose.setContent {
            AppScale(xlarge) {
                AppAlertDialog(
                    onDismissRequest = {},
                    title = { Text("Заглавие") },
                    text = { Text("Текст на диалога") },
                    confirmButton = { TextButton(onClick = {}) { Text("Добре") } },
                    dismissButton = { TextButton(onClick = {}) { Text("Отказ") } },
                )
            }
        }
        listOf("Заглавие", "Текст на диалога", "Добре", "Отказ").forEach { assertEquals(xlarge, fontScaleOf(it), 0.001f, it) }
    }

    /** Ограничението на мястото, откъдето е отворен (LimitFontScale), остава и в диалога. */
    @Test fun dialogKeepsTheCapWhereItIsOpened() {
        compose.setContent {
            AppScale(MAX_FONT_SCALE) {
                LimitFontScale(1.3f) {
                    AppAlertDialog(onDismissRequest = {}, confirmButton = {}, text = { Text("С ограничение") })
                }
            }
        }
        assertEquals(1.3f, fontScaleOf("С ограничение"), 0.001f)
    }

    @Test fun bottomSheetKeepsTheAppScale() {
        compose.setContent {
            AppScale(xlarge) { WhatsNewSheet(versionName = "1.2.30", notes = "• Нещо ново", onDismiss = {}) }
        }
        assertEquals(xlarge, fontScaleOf("• Нещо ново"), 0.001f)
        assertEquals(xlarge, fontScaleOf("Добре"), 0.001f)
    }

    @Test fun dropdownMenuKeepsTheAppScale() {
        compose.setContent {
            AppScale(xlarge) { WantToReadMenu() }
        }
        assertEquals(xlarge, fontScaleOf("Премахни от списъка"), 0.001f)
    }

    @Composable
    private fun WantToReadMenu() {
        Box {
            AppDropdownMenu(expanded = true, onDismissRequest = {}) {
                DropdownMenuItem(text = { Text("Кажи ми, когато е на рафта") }, leadingIcon = { Icon(Icons.Outlined.NotificationsNone, null) }, onClick = {})
                DropdownMenuItem(text = { Text("Премахни от списъка") }, leadingIcon = { Icon(Icons.Outlined.Delete, null) }, onClick = {})
            }
        }
    }

    /** Нов диалог, лист или меню — през обвивките (AppDialogs), иначе пак със системния мащаб. */
    @Test fun windowsGoThroughTheWrappers() {
        val direct = Regex("""^\s*(AlertDialog|BasicAlertDialog|Dialog|ModalBottomSheet|DropdownMenu|Popup)\(""", RegexOption.MULTILINE)
        val sources = File("src/main/kotlin").walk().filter { it.extension == "kt" && it.name != "AppDialogs.kt" }.toList()
        assertTrue(sources.size > 50, "няма изходен код в ${File("").absolutePath}")
        assertEquals(emptyList<String>(), sources.filter { direct.containsMatchIn(it.readText()) }.map { it.name })
    }

    // --- Снимки при най-едрия мащаб (2×) ---

    @Test fun choiceDialogLarge() {
        compose.setContent {
            AppScale(MAX_FONT_SCALE) {
                ChoiceDialog(
                    title = "Размер на текста",
                    note = "Умножава размера на шрифта от настройките на телефона. В статиите бутонът „Аа“ го променя допълнително.",
                    options = listOf(0 to "Малък", 1 to "Стандартен", 2 to "Голям", 3 to "Много голям"),
                    selected = 3, onSelect = {}, onDismiss = {},
                )
            }
        }
        assertEquals(MAX_FONT_SCALE, fontScaleOf("Много голям"), 0.001f)
        shot("86-dialogs-font2-choice")
    }

    @Test fun whatsNewSheetLarge() {
        compose.setContent {
            AppScale(MAX_FONT_SCALE) {
                WhatsNewSheet(
                    versionName = "1.2.30",
                    notes = "• Диалозите и листовете следват „Размер на текста“.\n• Джаджата сменя езика и без отваряне на приложението.",
                    onDismiss = {},
                )
            }
        }
        assertEquals(MAX_FONT_SCALE, fontScaleOf("Добре"), 0.001f)
        shot("87-dialogs-font2-whats-new")
    }

    @Test fun menuLarge() {
        compose.setContent {
            AppScale(MAX_FONT_SCALE) { WantToReadMenu() }
        }
        assertEquals(MAX_FONT_SCALE, fontScaleOf("Премахни от списъка"), 0.001f)
        shot("88-dialogs-font2-menu")
    }
}
