package org.chyavorec.app.ui.screens.home

import androidx.compose.ui.unit.dp
import org.chyavorec.app.ui.components.mayHaveRealCover
import org.chyavorec.domain.model.CatalogBook
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Разположението на „Начало“: въртележката, бързите действия и надписите под кориците. */
class HomeLayoutTest {
    @Test fun heroKeepsItsProportionInPortrait() {
        // Изправен телефон 411 × 891: 379 / 1.6.
        assertEquals(379.dp / 1.6f, heroMinHeight(379.dp, 891.dp))
        // Таблет: ширината на картата е до 720 dp.
        assertEquals(720.dp / 1.6f, heroMinHeight(808.dp, 1280.dp))
    }

    @Test fun heroIsAtMostAboutHalfTheWindowInLandscape() {
        // Хоризонтално 891 × 411: не 450 dp (целият екран), а под половината височина.
        val h = heroMinHeight(808.dp, 411.dp)
        assertTrue(h <= 411.dp * 0.5f, "$h")
        assertTrue(h >= 150.dp, "$h")
    }

    /**
     * Надписът на въртележката е на плоскост под снимката 16:9 (изправен телефон, таблет), а
     * хоризонтално — до нея: снимка + надпис един под друг биха заели целия екран.
     */
    @Test fun heroCaptionSitsUnderThePhotoInPortraitAndBesideItInLandscape() {
        // Изправен телефон 411 × 891 (голямата карта ~310 dp), и при „Много голям“ шрифт; телефон 360 × 640.
        assertFalse(heroSideBySide(310.dp, 891.dp))
        assertFalse(heroSideBySide(310.dp, 891.dp, fontScale = 1.3f))
        assertFalse(heroSideBySide(270.dp, 640.dp))
        // Таблет изправен 800 × 1280.
        assertFalse(heroSideBySide(640.dp, 1280.dp))
        // Хоризонтално: телефон 891 × 411 и таблет 1280 × 800 (съдържанието до 840 dp).
        assertTrue(heroSideBySide(640.dp, 411.dp))
        assertTrue(heroSideBySide(660.dp, 800.dp))
        // Мястото, докато се зарежда, е колкото картата: изправено — снимка 16:9 и надпис; хоризонтално — под половин прозорец.
        val portrait = heroSkeletonHeight(379.dp, 891.dp)
        assertTrue(portrait > 379.dp * 0.82f * 9f / 16f && portrait < 891.dp * 0.48f, "$portrait")
        assertTrue(heroSkeletonHeight(808.dp, 411.dp) <= 411.dp * 0.5f)
    }

    @Test fun quickActionsWrapToTwoColumnsBeforeWordsBreak() {
        // 411 dp без полетата (2 × 16): 4 в ред при обичаен и леко уголемен шрифт.
        assertEquals(4, quickActionsPerRow(379.dp, 1f))
        // Телефон 360 dp при нормален шрифт — пак 4 в ред.
        assertEquals(4, quickActionsPerRow(328.dp, 1f))
        assertEquals(4, quickActionsPerRow(379.dp, 1.1f))
        // „Много голям“ (×1.3): преди — 4 в ред и „Читателс/ка карта“.
        assertEquals(2, quickActionsPerRow(379.dp, 1.3f))
        assertEquals(2, quickActionsPerRow(379.dp, 2f))
        // Таблет: и при едър шрифт — 4.
        assertEquals(4, quickActionsPerRow(808.dp, 1.3f))
    }

    @Test fun captionSpaceIsReservedOnlyForBooksThatMayGetARealCover() {
        val plain = CatalogBook(1, "Вазов, Иван", "Под игото")
        val withIsbn = plain.copy(isbn = "978-954-09-0123-4")
        val scanned = plain.copy(coverUrl = "https://example.org/c.jpg")
        assertFalse(mayHaveRealCover(plain, onlineCovers = true))
        assertFalse(mayHaveRealCover(plain.copy(isbn = "123"), onlineCovers = true))
        assertTrue(mayHaveRealCover(withIsbn, onlineCovers = true))
        assertTrue(mayHaveRealCover(scanned, onlineCovers = true))
        // Личен екран / без онлайн корици — само генерирани.
        assertFalse(mayHaveRealCover(withIsbn, onlineCovers = false))
        assertFalse(mayHaveRealCover(scanned, onlineCovers = false))
    }
}
