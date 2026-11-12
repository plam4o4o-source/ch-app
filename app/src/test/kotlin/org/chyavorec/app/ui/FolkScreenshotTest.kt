package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.AppCard
import org.chyavorec.app.ui.components.AppTopBar
import org.chyavorec.app.ui.components.BookCover
import org.chyavorec.app.ui.components.ClothWeave
import org.chyavorec.app.ui.components.FolkLoaderFrame
import org.chyavorec.app.ui.components.FolkMotif
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.SectionHeader
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.components.SkeletonCards
import org.chyavorec.app.ui.components.SkeletonList
import org.chyavorec.app.ui.components.TileGrid
import org.chyavorec.app.ui.components.TileItem
import org.chyavorec.app.ui.components.goldSheen
import org.chyavorec.app.ui.components.stitchedCorner
import org.chyavorec.app.ui.theme.LocalExtendedColors
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.app.ui.theme.ThemeStyle
import org.chyavorec.domain.model.CatalogBook
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Народният вид отвъд „Начало“ (вълна 21): етикет с три бода, плочки с народни форми и
 * пунктиран ъгъл, индикатор „зарежда се“, канавата на skeleton-а, ленената подвързия на
 * корицата, златният отблясък на заглавието и „нощната черга“ — светла, тъмна и OLED тема.
 * За визуален преглед (клонът „screenshots“); не сравнява пиксели.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class FolkScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    private enum class Look(val suffix: String, val mode: ThemeMode, val oled: Boolean) {
        LIGHT("light", ThemeMode.LIGHT, false),
        DARK("dark", ThemeMode.DARK, false),
        OLED("oled", ThemeMode.DARK, true),
    }

    @Composable
    private fun Themed(look: Look, content: @Composable () -> Unit) {
        ChitalishteTheme(mode = look.mode, style = ThemeStyle(oledBlack = look.oled)) {
            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) { content() }
        }
    }

    @Composable
    private fun Label(text: String) = Text(text, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

    private fun tiles() = listOf(
        TileItem(Icons.Outlined.Campaign, "Съобщения", "Съобщения и покани от читалището") {},
        TileItem(Icons.Outlined.Event, "Събития", "Календар на читалището") {},
        TileItem(Icons.Outlined.Photo, "Галерия", "Снимки от сайта", motif = FolkMotif.ELBETITSA) {},
        TileItem(Icons.Outlined.Folder, "Документи", "Устав, отчети", motif = FolkMotif.ELBETITSA) {},
    )

    /** Етикет с бодове, заглавка на раздел, плочки (осморъка / ромб / елбетица), ъгълът в покой и „затегнат“, карта. */
    private fun sections(look: Look) {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Themed(look) {
                    SectionHeader("Читалището", label = "Читалището", modifier = Modifier.padding(top = 12.dp, bottom = 8.dp))
                    TileGrid(tiles())
                    SectionLabel("Моето", Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp))
                    TileGrid(
                        listOf(
                            TileItem(Icons.Outlined.Badge, "Моята карта", "Номер и баркод") {},
                            TileItem(Icons.Outlined.AutoStories, "История", "Прочетено", motif = FolkMotif.ELBETITSA) {},
                        ),
                        motif = FolkMotif.RHOMBUS,
                    )
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf(0f, 0.5f, 1f).forEach { t ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    Modifier.size(96.dp, 64.dp).background(MaterialTheme.colorScheme.surfaceContainerLow)
                                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                        .stitchedCorner(LocalExtendedColors.current.gold) { t },
                                )
                                Label("ъгъл $t")
                            }
                        }
                    }
                    AppCard(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text("Голяма карта", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
        shot("w21-folk-sections-${look.suffix}")
    }

    @Test fun sectionsLight() = sections(Look.LIGHT)

    @Test fun sectionsDark() = sections(Look.DARK)

    @Test fun sectionsOled() = sections(Look.OLED)

    /** Бродирането на трите бода: по средата (кадър от ~40%) и готово. */
    @Test fun stitchMarkEmbroidering() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false) {
                Themed(Look.LIGHT) {
                    SectionLabel("Читалището", Modifier.padding(16.dp))
                }
            }
        }
        compose.mainClock.advanceTimeBy(210)
        shot("w21-folk-stitch-mark-mid")
        compose.mainClock.advanceTimeBy(600)
        shot("w21-folk-stitch-mark-done")
    }

    /** Индикаторът: кръг → осморъка → ромб → елбетица, по кадри (фаза 0 .. 3.7). */
    private fun loader(look: Look) {
        compose.setContent {
            Themed(look) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0f, 0.35f, 0.7f, 1f, 1.35f, 1.7f, 2f, 2.35f, 2.7f, 3f, 3.35f, 3.7f).forEach { p ->
                        FolkLoaderFrame(p, MaterialTheme.colorScheme.primary, Modifier.size(28.dp))
                    }
                }
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf(0f, 1f, 2f, 3f).forEach { p ->
                        FolkLoaderFrame(p, MaterialTheme.colorScheme.primary, Modifier.size(72.dp))
                    }
                }
            }
        }
        shot("w21-folk-loader-${look.suffix}")
    }

    @Test fun loaderLight() = loader(Look.LIGHT)

    @Test fun loaderDark() = loader(Look.DARK)

    @Test fun loaderOled() = loader(Look.OLED)

    /** „Канава“: неподвижен skeleton (намалено движение) във всяка тема. */
    private fun skeleton(look: Look, reduced: Boolean = true) {
        compose.mainClock.autoAdvance = reduced
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides reduced) {
                Themed(look) {
                    SkeletonList(rows = 3)
                    SkeletonCards()
                }
            }
        }
        if (!reduced) compose.mainClock.advanceTimeBy(450)
        shot("w21-folk-skeleton-${look.suffix}${if (reduced) "" else "-moving"}")
    }

    @Test fun skeletonLight() = skeleton(Look.LIGHT)

    @Test fun skeletonDark() = skeleton(Look.DARK)

    @Test fun skeletonOled() = skeleton(Look.OLED)

    @Test fun skeletonLightMoving() = skeleton(Look.LIGHT, reduced = false)

    @Test fun skeletonDarkMoving() = skeleton(Look.DARK, reduced = false)

    /** Корици с ленена подвързия (малки и голяма) + плочките на тъканта, увеличени. */
    private fun covers(look: Look) {
        val books = listOf(
            CatalogBook(1, "Вазов, Иван", "Под игото", udc = "821", available = true),
            CatalogBook(2, "Йовков, Йордан", "Старопланински легенди", udc = "821"),
            CatalogBook(3, "Колев, Симеон", "Белият детството", udc = "3"),
            CatalogBook(4, "Дейвид Аттънбъро", "Планетата Земя", udc = "5"),
            CatalogBook(5, "Радичков, Йордан", "Суматоха", udc = "9", available = true),
        )
        compose.setContent {
            Themed(look) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    books.forEach { BookCover(it, width = 64.dp, showStatusDot = false) }
                }
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Bottom) {
                    BookCover(books[0], width = 124.dp)
                    BookCover(books[2], width = 124.dp)
                    Column {
                        Image(ClothWeave.linen, null, Modifier.size(96.dp).background(Color(0xFF8A4B3A)), filterQuality = FilterQuality.None)
                        Label("лен ×1.5")
                    }
                }
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Image(ClothWeave.night, null, Modifier.size(96.dp).background(Color(0xFF1A1208)), filterQuality = FilterQuality.None)
                    Label("нощен плат ×3")
                }
            }
        }
        shot("w21-folk-covers-${look.suffix}")
    }

    @Test fun coversLight() = covers(Look.LIGHT)

    @Test fun coversDark() = covers(Look.DARK)

    @Test fun coversOled() = covers(Look.OLED)

    /** Заглавие на книга: отблясъкът по средата на движението (~45%) и след него. */
    @Test fun goldSheenTitle() {
        compose.mainClock.autoAdvance = false
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides false, org.chyavorec.app.ui.components.LocalAgslEnabled provides false) {
                Column {
                    Themed(Look.LIGHT) {
                        Text("Под игото", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(20.dp).goldSheen(1L))
                    }
                    Themed(Look.DARK) {
                        Text("Под игото", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(20.dp).goldSheen(2L))
                    }
                }
            }
        }
        compose.mainClock.advanceTimeBy(120 + 230)
        shot("w21-folk-sheen-mid")
    }

    /** „Нощна черга“: горна лента и карти в светла, тъмна и OLED (в OLED и светла — без тъкан). */
    @OptIn(ExperimentalMaterial3Api::class)
    private fun night(look: Look) {
        compose.setContent {
            CompositionLocalProvider(LocalReducedMotion provides true) {
                Themed(look) {
                    AppTopBar("Още", onSearch = {})
                    SectionHeader("Читалището", label = "Приложението", modifier = Modifier.padding(top = 12.dp))
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        repeat(2) {
                            AppCard(Modifier.fillMaxWidth().height(120.dp)) {
                                Text("Голяма карта", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
        shot("w21-folk-night-${look.suffix}")
    }

    @Test fun nightLight() = night(Look.LIGHT)

    @Test fun nightDark() = night(Look.DARK)

    @Test fun nightOled() = night(Look.OLED)
}
