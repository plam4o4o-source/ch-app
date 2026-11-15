package org.chyavorec.app.ui.theme

import android.app.Application
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Density
import org.chyavorec.app.ui.components.StitchKind
import org.chyavorec.app.ui.components.StitchSpec
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Народният речник ([FolkTokens], [StitchSpec]) и бюджетът на украсата: екраните (`ui/screens/`)
 * не рисуват свои пунктири, а заобляния и цветове с числа — само в изброените изключения.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class FolkTokensTest {
    @Test fun radiiMatchTheMaterialShapes() {
        val d = Density(1f)
        fun px(s: androidx.compose.ui.graphics.Shape) = (s as RoundedCornerShape).topStart.toPx(Size(200f, 200f), d)
        assertEquals(FolkTokens.RadiusSmall.value, px(AppShapes.small))
        assertEquals(FolkTokens.RadiusCard.value, px(AppShapes.medium))
        assertEquals(FolkTokens.RadiusLarge.value, px(AppShapes.large))
        assertTrue(FolkTokens.GOLD_BORDER_ALPHA in 0.3f..0.7f)
    }

    @Test fun stitchStrokesAreCachedPerScale() {
        val a = StitchSpec.Running.stroke(3.17f)
        assertSame(a, StitchSpec.Running.stroke(3.17f), "без алокация при същата плътност")
        assertNotSame(a, StitchSpec.Running.stroke(3.17f, scale = 2f))
        assertSame(a, StitchSpec.Running.stroke(3.17f), "кешът пази няколко мащаба")
        assertEquals(StitchSpec.Running.widthDp * 3.17f, a.width, 1e-4f)
        assertEquals(StrokeCap.Round, a.cap)
        assertEquals(StitchKind.RUNNING, StitchSpec.Running.kind)
        assertEquals(StitchKind.BASTING, StitchSpec.Basting.kind)
        // Тропосването е по-дълго и по-плътно от тичащия бод.
        assertTrue(StitchSpec.Basting.dashDp > StitchSpec.Running.dashDp)
        assertTrue(StitchSpec.Basting.widthDp > StitchSpec.Running.widthDp)
        assertEquals(0.26f, StitchSpec.Cross.STROKE)
    }

    /** Изключенията: файл → (сурови RoundedCornerShape(N.dp), сурови Color(0x…)) — само толкова, не повече. */
    private val budget = mapOf(
        // Дървеният рафт: цвят на дърво, тънки дъски.
        "catalog/CatalogShelf.kt" to (2 to 4),
        // Картите на „Изненадай ме“ — собствен размер на картата-играчка.
        "catalog/SurpriseCards.kt" to (1 to 0),
        // „Извези шевица“: конците и ленената кърпа са едни и същи във всяка тема (като на картата за споделяне).
        "embroidery/StitchDrawing.kt" to (0 to 10),
        // Листът от календара: хартия.
        "events/EventLeaf.kt" to (0 to 1),
        // Галерията: плочките и лентата над снимката на цял екран (върху черно).
        "gallery/GalleryScreens.kt" to (1 to 0),
        "gallery/ViewerToolbar.kt" to (2 to 0),
        // Снегът на зимния празник в светла тема.
        "home/HomeScreen.kt" to (0 to 1),
        // Читателската карта — физически предмет (фолио, пластмаса, бял баркод).
        "my/CardFoil.kt" to (0 to 7),
        "my/CardScreens.kt" to (4 to 2),
        // „Без раздел“ в диаграмите на дневника.
        "my/ReadingCharts.kt" to (0 to 1),
        // „Годината ми“ — историите имат собствена (винаги тъмна) палитра.
        "my/YearSlides.kt" to (3 to 9),
        "my/YearStory.kt" to (1 to 1),
        // „Старият вестник“: хартия, бюро и мастило.
        "newspaper/PaperSheet.kt" to (0 to 10),
        // Въвеждането: винаги тъмен фон.
        "onboarding/OnboardingScreen.kt" to (0 to 1),
        // Листът с резултата от сканирането (горни ъгли като на лист отдолу).
        "scan/ScanScreen.kt" to (1 to 0),
    )

    private val dash = Regex("""dashPathEffect\(""")
    private val radius = Regex("""RoundedCornerShape\([^)]*?\b\d+(\.\d+)?\.dp""")
    private val color = Regex("""Color\(0x""")

    @Test fun screensUseTheSharedTokens() {
        val root = File("src/main/kotlin")
        val screens = root.walk().filter { it.isFile && it.extension == "kt" && it.invariantSeparatorsPath.contains("/ui/screens/") }.toList()
        assertTrue(screens.size > 30, "няма екрани в ${root.absolutePath}")
        val problems = mutableListOf<String>()
        for (f in screens) {
            val key = f.invariantSeparatorsPath.substringAfter("/ui/screens/")
            val text = f.readText()
            val (maxRadius, maxColor) = budget[key] ?: (0 to 0)
            dash.findAll(text).count().let { if (it > 0) problems += "$key: $it× dashPathEffect( — ползвай StitchSpec.Running/Basting" }
            radius.findAll(text).count().let { if (it > maxRadius) problems += "$key: $it× RoundedCornerShape(N.dp) (позволени $maxRadius) — FolkTokens.Radius*/MaterialTheme.shapes" }
            color.findAll(text).count().let { if (it > maxColor) problems += "$key: $it× Color(0x…) (позволени $maxColor) — цветовете на темата/Brand" }
        }
        assertEquals(emptyList<String>(), problems)
    }

    /**
     * И общите компоненти (`ui/components/`, вкл. FolkShapes.kt — плочките и индикатора) не
     * рисуват свои пунктири: единственият източник е Stitching.kt ([StitchSpec]).
     */
    @Test fun componentsDrawNoRawDashes() {
        val root = File("src/main/kotlin")
        val components = root.walk().filter { it.isFile && it.extension == "kt" && it.invariantSeparatorsPath.contains("/ui/components/") }.toList()
        assertTrue(components.any { it.name == "FolkShapes.kt" }, "няма FolkShapes.kt в ${root.absolutePath}")
        val problems = components.filter { it.name != "Stitching.kt" }
            .mapNotNull { f -> dash.findAll(f.readText()).count().takeIf { it > 0 }?.let { "${f.name}: $it× dashPathEffect( — ползвай StitchSpec.Running/Basting" } }
        assertEquals(emptyList<String>(), problems)
    }

    @Test fun motionScaleIsOrdered() {
        val scale = listOf(AppMotion.StaggerMs, AppMotion.QuickMs, AppMotion.ShortMs, AppMotion.MediumMs, AppMotion.LongMs, AppMotion.StitchMs)
        assertEquals(scale.sorted(), scale)
        assertEquals(scale.size, scale.toSet().size)
    }

    /**
     * Изключенията от скалата на движението: файл (под `ui/`) → суровите числа в tween/durationMillis/
     * delayMillis и защо. Само цикли — еднократните движения са стъпки от [AppMotion] или изрази от тях.
     */
    private val motionAllowed: Map<String, Pair<Set<Int>, String>> = mapOf(
        "components/Skeleton.kt" to (setOf(1300) to "безкрайният цикъл на отблясъка на скелета, не еднократно движение"),
        "components/LeafRefresh.kt" to (setOf(1300) to "безкрайният цикъл на конеца при опресняване (в такт със скелета)"),
    )

    // Низовете се прескачат цели (иначе "image/*" би започнал „коментар“).
    private val comments = Regex(""""(?:\\.|[^"\\\n])*"|/\*[\s\S]*?\*/|//[^\n]*""")
    private val rawDuration = Regex("""\btween\s*(?:<[^>()]*>)?\s*\(\s*(?:durationMillis\s*=\s*)?(\d[\d_]*)|\b(?:durationMillis|delayMillis)\s*=\s*(\d[\d_]*)""")

    /**
     * Една скала на движението: в `ui/` (екраните, общите компоненти, навигацията) няма
     * `tween(<число>)`, `tween<T>(<число>)`, `durationMillis = <число>` или `delayMillis = <число>` —
     * само [AppMotion] (QuickMs/ShortMs/MediumMs/LongMs/StitchMs/StaggerMs) или изрази от тях.
     */
    @Test fun motionUsesTheDurationScale() {
        val root = File("src/main/kotlin")
        val files = root.walk().filter { it.isFile && it.extension == "kt" && it.invariantSeparatorsPath.contains("/ui/") }.toList()
        assertTrue(files.count { it.invariantSeparatorsPath.contains("/ui/screens/") } > 30, "няма екрани в ${root.absolutePath}")
        assertTrue(files.any { it.invariantSeparatorsPath.endsWith("/ui/components/Motion.kt") }, "няма ui/components/Motion.kt")
        val problems = mutableListOf<String>()
        val used = mutableMapOf<String, MutableSet<Int>>()
        for (f in files) {
            val key = f.invariantSeparatorsPath.substringAfter("/ui/")
            // Коментарите не се броят (редовете остават на мястото си).
            val code = comments.replace(f.readText()) { m -> if (m.value.startsWith('"')) m.value else m.value.filter { it == '\n' } }
            val allowed = motionAllowed[key]?.first.orEmpty()
            for (m in rawDuration.findAll(code)) {
                val n = (m.groupValues[1].ifEmpty { m.groupValues[2] }).replace("_", "").toInt()
                if (n in allowed) { used.getOrPut(key) { mutableSetOf() } += n; continue }
                val line = code.substring(0, m.range.first).count { it == '\n' } + 1
                problems += "ui/$key:$line: „${m.value.trim()}“ — ползвай AppMotion.QuickMs/ShortMs/MediumMs/LongMs/StitchMs/StaggerMs " +
                    "(или израз като LongMs + QuickMs); цикъл или нарочно време извън скалата — в motionAllowed с причина"
            }
        }
        // И обратното: изключение, което вече не е нужно, се маха (иначе остава вратичка).
        for ((key, v) in motionAllowed) {
            val stale = v.first - used[key].orEmpty()
            if (stale.isNotEmpty()) problems += "ui/$key: изключението $stale вече не се ползва — махни го от motionAllowed"
        }
        assertEquals(emptyList<String>(), problems)
    }
}
