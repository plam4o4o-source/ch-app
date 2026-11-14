package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.FolkIconPlate
import org.chyavorec.app.ui.components.FolkIcons
import org.chyavorec.app.ui.components.FolkMotif
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.app.ui.theme.ThemeStyle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Наборът икони „Бод“ наведнъж: всяка икона в 24 и 48 dp и върху плочката си (осморъка —
 * читалището, ромб — библиотеката и „моето“), в светла и тъмна тема. За визуален преглед
 * (клонът „screenshots“) — всяка икона трябва да се разпознава и в 24 dp.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h1250dp-xhdpi")
class FolkIconsScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Иконата, името й и плочката, на която стои в приложението. */
    private class Entry(val name: String, val icon: ImageVector, val motif: FolkMotif)

    private val R = FolkMotif.ROSETTE
    private val H = FolkMotif.RHOMBUS

    private fun entries() = listOf(
        Entry("Начало", FolkIcons.Home, R), Entry("Начало ●", FolkIcons.HomeSelected, R),
        Entry("Каталог", FolkIcons.Catalog, H), Entry("Каталог ●", FolkIcons.CatalogSelected, H),
        Entry("Моето", FolkIcons.My, H), Entry("Моето ●", FolkIcons.MySelected, H),
        Entry("Новини", FolkIcons.News, R), Entry("Новини ●", FolkIcons.NewsSelected, R),
        Entry("Още", FolkIcons.More, R), Entry("Още ●", FolkIcons.MoreSelected, R),
        Entry("Моите книги", FolkIcons.Books, H), Entry("Сканирай", FolkIcons.Scan, H),
        Entry("Карта", FolkIcons.Card, H), Entry("Искам да прочета", FolkIcons.Bookmark, H),
        Entry("Контакти", FolkIcons.Pin, R), Entry("Съобщения", FolkIcons.Messages, R),
        Entry("Събития", FolkIcons.Events, R), Entry("Дейности", FolkIcons.Activities, R),
        Entry("За читалището", FolkIcons.Chitalishte, R), Entry("Галерия", FolkIcons.Gallery, R),
        Entry("Документи", FolkIcons.Documents, R), Entry("Дигитален клуб", FolkIcons.Computer, R),
        Entry("Извези шевица", FolkIcons.Embroider, R), Entry("Старият вестник", FolkIcons.Newspaper, R),
        Entry("Лента на времето", FolkIcons.Timeline, R), Entry("Познай Яворец", FolkIcons.Quiz, R),
        Entry("Членство", FolkIcons.Membership, H), Entry("История", FolkIcons.History, H),
        Entry("Профил", FolkIcons.Profile, H), Entry("Известия", FolkIcons.Notifications, H),
    )

    @Composable
    private fun Cell(e: Entry) {
        val ink = MaterialTheme.colorScheme.onSurface
        Column(Modifier.width(94.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(e.icon, null, tint = ink, modifier = Modifier.size(24.dp))
                FolkIconPlate(e.icon, e.motif, size = 48.dp)
            }
            Text(
                e.name, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    private fun sheet(mode: ThemeMode, name: String) {
        val all = entries()
        compose.setContent {
            ChitalishteTheme(mode = mode, style = ThemeStyle()) {
                Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(8.dp)) {
                    // 24 dp и плочката (48 dp) — по четири в ред.
                    all.chunked(4).forEach { row ->
                        Row(Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) { row.forEach { Cell(it) } }
                    }
                    Spacer(Modifier.height(10.dp))
                    // 48 dp без плочка — за чертежа.
                    all.chunked(7).forEach { row ->
                        Row(Modifier.padding(vertical = 3.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { Icon(it.icon, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(48.dp)) }
                        }
                    }
                }
            }
        }
        shot(name)
    }

    @Test fun iconSetLight() = sheet(ThemeMode.LIGHT, "w23-bod-icons-light")

    @Test fun iconSetDark() = sheet(ThemeMode.DARK, "w23-bod-icons-dark")

    /** Всички икони са на решетка 24 × 24 и с име („Bod.…“) — едноцветни, без собствен цвят. */
    @Test fun iconsAreOnTheGrid() {
        entries().forEach { e ->
            assertEquals(24f, e.icon.viewportWidth, e.name)
            assertEquals(24f, e.icon.viewportHeight, e.name)
            assertEquals(24.dp, e.icon.defaultWidth, e.name)
            assertTrue(e.icon.name.startsWith("Bod."), e.name)
        }
        // Различни икони — без случайно повторена.
        assertEquals(entries().size, entries().map { it.icon.name }.toSet().size)
    }
}
