package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.FolkIconPlate
import org.chyavorec.app.ui.components.FolkListPlate
import org.chyavorec.app.ui.components.FolkPlateBitmaps
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
        Entry("Размер на текста", FolkIcons.TextSize, H),
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

    /**
     * Еднакво „тегло“ на набора: всяка икона, нарисувана в 24 dp (черно върху бяло), е с 15–32 %
     * мастило (преди — „Познай Яворец“ 10 %, „Старият вестник“ 36 %), а избраните (●, плътни) —
     * по-тежки от своя контур, но до 55 %. И всяка оставя поле от 1 единица (1/24) до ръба — нищо
     * не опира в края (преди — „Дейности“ 0.2…23.8, „За читалището“ — остриетата на фронтона).
     */
    @Test fun iconsHaveEvenInkAndAOneUnitMargin() {
        val all = entries()
        compose.setContent {
            Column(Modifier.background(Color.White)) {
                all.chunked(8).forEach { row ->
                    Row {
                        row.forEach { e ->
                            Box(Modifier.size(24.dp).background(Color.White).testTag(e.name)) {
                                Icon(e.icon, null, tint = Color.Black, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
        compose.waitForIdle()
        val problems = mutableListOf<String>()
        val ink = HashMap<String, Double>()
        all.forEach { e ->
            val bmp = compose.onNodeWithTag(e.name, useUnmergedTree = true).captureToImage().asAndroidBitmap()
            val w = bmp.width
            val h = bmp.height
            val margin = Math.round(w / 24f)
            var sum = 0.0
            var edge = 0.0
            for (y in 0 until h) for (x in 0 until w) {
                val v = 1.0 - AndroidColor.red(bmp.getPixel(x, y)) / 255.0
                sum += v
                if (x < margin || y < margin || x >= w - margin || y >= h - margin) edge = maxOf(edge, v)
            }
            val coverage = sum / (w * h)
            ink[e.name] = coverage
            val band = if ("●" in e.name) 0.25..0.55 else 0.15..0.32
            if (coverage !in band) problems += "${e.name}: ${"%.1f".format(coverage * 100)} % мастило (а трябва ${band.start * 100}–${band.endInclusive * 100} %)"
            if (edge > 0.1) problems += "${e.name}: опира в ръба (${"%.2f".format(edge)})"
        }
        all.filter { "●" in it.name }.forEach { sel ->
            val plain = sel.name.removeSuffix(" ●")
            if (ink.getValue(sel.name) <= ink.getValue(plain)) problems += "${sel.name}: не е по-плътна от „$plain“"
        }
        assertTrue(problems.isEmpty(), problems.joinToString("\n"))
    }

    /**
     * Плочките в тъмна тема: плътни и с тънък ръб в цвета на иконата — осморъката (читалището) и
     * ромбът (библиотеката) се различават и върху мастило (преди тъмнозлатното се сливаше, 1.68:1).
     * Редовете: 36 dp (списъци — търсене, съобщения), 48 dp (плочките на „Още“), 56 dp („Начало“),
     * важното съобщение (в цвета на грешката); отдолу същото в светла тема.
     */
    @Test fun platesInDarkAndLight() {
        compose.setContent {
            Column {
                for (mode in listOf(ThemeMode.DARK, ThemeMode.LIGHT)) {
                    ChitalishteTheme(mode = mode, style = ThemeStyle()) {
                        Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                FolkListPlate(FolkIcons.Messages, R)
                                FolkListPlate(FolkIcons.Profile, H)
                                FolkListPlate(FolkIcons.Books, H)
                                FolkListPlate(FolkIcons.Events, R)
                                FolkListPlate(
                                    FolkIcons.Messages, R,
                                    container = MaterialTheme.colorScheme.errorContainer, content = MaterialTheme.colorScheme.onErrorContainer,
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                FolkIconPlate(FolkIcons.Chitalishte, R, size = 48.dp)
                                FolkIconPlate(FolkIcons.Card, H, size = 48.dp)
                                FolkIconPlate(FolkIcons.Pin, R)
                                FolkIconPlate(FolkIcons.Bookmark, H)
                                FolkIconPlate(FolkIcons.Scan, H)
                            }
                        }
                    }
                }
            }
        }
        shot("w24-plates-dark-light")
    }

    /** Еднаквите плочки в списък са една и съща готова картинка: 12 плочки от 2 вида — най-много 2 нови в кеша. */
    @Test fun platesShareOneBitmapPerKind() {
        val second = mutableStateOf(false)
        compose.setContent {
            ChitalishteTheme(mode = ThemeMode.DARK, style = ThemeStyle()) {
                Column {
                    if (!second.value) repeat(6) { Row { FolkListPlate(FolkIcons.News, R); FolkListPlate(FolkIcons.Books, H) } }
                    else repeat(10) { Row { FolkListPlate(FolkIcons.Gallery, R); FolkListPlate(FolkIcons.Card, H) } }
                }
            }
        }
        compose.waitForIdle()
        val before = FolkPlateBitmaps.count
        assertTrue(before >= 2)
        second.value = true
        compose.waitForIdle()
        // Същите форми, размер и цветове (иконата е отделно) — нищо ново.
        assertEquals(before, FolkPlateBitmaps.count)
    }

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
