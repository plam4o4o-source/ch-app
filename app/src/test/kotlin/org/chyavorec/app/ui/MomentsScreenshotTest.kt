package org.chyavorec.app.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalAgslEnabled
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.components.LocalSeasonalDate
import org.chyavorec.app.ui.components.LocalSeasonalDecor
import org.chyavorec.app.ui.components.SectionLabel
import org.chyavorec.app.ui.components.rememberSeasonalThread
import org.chyavorec.app.ui.components.stitchedCorner
import org.chyavorec.app.ui.screens.events.EventLeaf
import org.chyavorec.app.ui.screens.my.CardData
import org.chyavorec.app.ui.screens.my.DueLeaf
import org.chyavorec.app.ui.screens.my.LibraryCard
import org.chyavorec.app.ui.screens.my.LoanCard
import org.chyavorec.app.ui.screens.my.LocalFixedCardTilt
import org.chyavorec.app.ui.share.ShareFixtures
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.chyavorec.app.ui.theme.ThemeMode
import org.chyavorec.domain.model.Event
import org.chyavorec.domain.model.Loan
import org.chyavorec.domain.model.LoanDueCalculator
import org.chyavorec.domain.model.MembershipStatus
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.time.LocalDate
import kotlin.test.assertTrue

/**
 * Вълна 23, „моменти“: сезонният конец на етикетите и плочките, „Нов лист“ при удължаване,
 * откъснатият отрязък на листа на събитието и „Твоята шевица“ върху читателската карта.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class MomentsScreenshotTest {
    @get:Rule val compose = createComposeRule()
    private val outDir = File("build/reports/screenshots").apply { mkdirs() }

    private fun shot(name: String) {
        compose.waitForIdle()
        val bmp = compose.onRoot().captureToImage().asAndroidBitmap()
        File(outDir, "$name.png").outputStream().use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Composable
    private fun Page(dark: Boolean = false, fontScale: Float = 1f, reduced: Boolean = true, content: @Composable () -> Unit) {
        ChitalishteTheme(mode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT) {
            val d = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(d.density, fontScale), LocalReducedMotion provides reduced) {
                Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp)) { content() }
            }
        }
    }

    // --- Сезонният конец ---

    private val seasons = listOf(
        "Обикновен ден" to LocalDate.of(2026, 11, 14),
        "Мартеница (1–9 март)" to LocalDate.of(2027, 3, 1),
        "Коледа" to LocalDate.of(2026, 12, 24),
        "Цветница и Великден" to LocalDate.of(2027, 4, 25),
    )

    private fun seasonal(dark: Boolean, name: String) {
        compose.setContent {
            Page(dark = dark) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    for ((label, date) in seasons) {
                        CompositionLocalProvider(LocalSeasonalDecor provides true, LocalSeasonalDate provides date) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    SectionLabel(label)
                                    Text(date.toString(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                // Натисната плочка: „затегнатият“ ъгъл в конеца на сезона.
                                Surface(
                                    shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surfaceContainerLow,
                                    modifier = Modifier.size(150.dp, 72.dp).stitchedCorner(rememberSeasonalThread()) { 1f },
                                ) {}
                            }
                        }
                    }
                    // Изключени „Сезонни украси“ — винаги златото.
                    CompositionLocalProvider(LocalSeasonalDecor provides false, LocalSeasonalDate provides LocalDate.of(2027, 3, 1)) {
                        SectionLabel("Без сезонни украси (1 март)")
                    }
                }
            }
        }
        shot(name)
    }

    @Test fun seasonalThreadLight() = seasonal(dark = false, "w23-seasonal-thread-light")

    @Test fun seasonalThreadDark() = seasonal(dark = true, "w23-seasonal-thread-dark")

    // --- „Нов лист“ при удължаване ---

    private val today = LocalDate.of(2026, 10, 17)
    private val renewed = Loan("1", 101, "Под игото", "Иван Вазов", borrowedOn = "2026-09-23", dueOn = "2026-11-04", renewals = 2, canRenew = true)
    private val other = Loan("3", 103, "Бай Ганьо", "Алеко Константинов", borrowedOn = "2026-10-10", dueOn = "2026-11-07", canRenew = true)

    private fun loans(leaf: DueLeaf, dark: Boolean = false, fontScale: Float = 1f, reduced: Boolean = true) {
        val calc = LoanDueCalculator()
        compose.setContent {
            Page(dark = dark, fontScale = fontScale, reduced = reduced) {
                Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LoanCard(renewed, calc, today, stale = false, serverRenews = true, renewing = false, onRenew = {}, onOpen = {}, leaf = leaf)
                    LoanCard(other, calc, today, stale = false, serverRenews = true, renewing = false, onRenew = {}, onOpen = {})
                }
            }
        }
    }

    private val leafPlayed = DueLeaf(oldDue = "2026-10-21", newDue = "2026-11-04", played = true)

    @Test fun renewedLeafLight() {
        loans(leafPlayed)
        // За екранния четец — новият и старият срок, а не две дати поотделно.
        compose.waitForIdle()
        val described = compose.onAllNodes(SemanticsMatcher("нов срок") { n ->
            n.config.getOrNull(SemanticsProperties.ContentDescription)?.any { it.startsWith("Нов срок: 04.11.2026") } == true
        }).fetchSemanticsNodes()
        assertTrue(described.isNotEmpty(), "редът със срока се чете като „Нов срок: … (беше …)“")
        shot("w23-loan-new-leaf-light")
    }

    @Test fun renewedLeafDark() {
        loans(leafPlayed, dark = true)
        shot("w23-loan-new-leaf-dark")
    }

    @Test fun renewedLeafLargeFont() {
        loans(leafPlayed, fontScale = 1.3f)
        shot("w23-loan-new-leaf-font130")
    }

    /** По средата на обръщането: старата дата се обръща нагоре и открива новата. */
    @Test fun renewedLeafMidFlip() {
        compose.mainClock.autoAdvance = false
        loans(leafPlayed.copy(played = false), reduced = false)
        compose.mainClock.advanceTimeBy(220L + 160L)
        shot("w23-loan-new-leaf-midflip")
    }

    // --- Откъснатият отрязък на листа ---

    private val concert = Event(
        "e1", "Коледен концерт на децата от школите", "2026-12-25", time = "18.30", place = "НЧ „Васил Левски – 1922“",
        category = "Концерт", sourceUrl = "https://chyavorec.org/events",
    )

    private fun torn(dark: Boolean, reduced: Boolean, name: String) {
        compose.setContent {
            Page(dark = dark, reduced = reduced) {
                EventLeaf(concert, LocalDate.of(2026, 12, 25), LocalDate.of(2026, 11, 12), torn = true)
            }
        }
        shot(name)
    }

    @Test fun tornStubLight() = torn(dark = false, reduced = false, "w23-event-leaf-torn-light")

    @Test fun tornStubDark() = torn(dark = true, reduced = false, "w23-event-leaf-torn-dark")

    /** Намалено движение: отрязъкът не се движи — отметка на него. */
    @Test fun tornStubReducedMotion() = torn(dark = false, reduced = true, "w23-event-leaf-torn-reduced")

    // --- „Твоята шевица“ върху картата ---

    private val card = CardData(holder = "Мария Петрова", number = "1024-5678", barcode = "10245678", status = MembershipStatus.ACTIVE, verified = true)

    private fun cardBand(name: String, fontScale: Float = 1f, dark: Boolean = false) {
        compose.setContent {
            Page(dark = dark, fontScale = fontScale) {
                CompositionLocalProvider(LocalAgslEnabled provides false, LocalFixedCardTilt provides Offset.Zero) {
                    Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        LibraryCard(card, band = ShareFixtures.year)
                        Spacer(Modifier.width(1.dp))
                    }
                }
            }
        }
        shot(name)
    }

    @Test fun cardBandLight() = cardBand("w23-card-band-light")

    @Test fun cardBandDark() = cardBand("w23-card-band-dark", dark = true)

    @Test fun cardBandLargeFont() = cardBand("w23-card-band-font130", fontScale = 1.3f)

    @Config(qualifiers = "bg-w360dp-h800dp-xhdpi")
    @Test fun cardBandNarrow() = cardBand("w23-card-band-narrow")
}
