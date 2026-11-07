package org.chyavorec.app.ui.theme

import org.chyavorec.app.ui.theme.ShevitsaPattern.EMPTY
import org.chyavorec.app.ui.theme.ShevitsaPattern.GOLD
import org.chyavorec.app.ui.theme.ShevitsaPattern.INK
import org.chyavorec.app.ui.theme.ShevitsaPattern.RED
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * „Lint“ за AGSL текста (Robolectric не го изпълнява): униформите съвпадат с тези,
 * които кодът задава, входната точка е правилна, няма конструкции, които AGSL не
 * приема. И шарката ([ShevitsaPattern] — копието на шейдъра): симетрия и мотиви.
 */
class ShevitsaShadersTest {
    private val uniform = Regex("""^\s*(?:layout\(color\)\s+)?uniform\s+(\w+)\s+(\w+)\s*;""", RegexOption.MULTILINE)

    private fun uniforms(src: String) = uniform.findAll(src).map { it.groupValues[2] }.toList()

    private fun checkSource(src: String, expected: List<String>) {
        assertEquals(expected.sorted(), uniforms(src).sorted())
        assertTrue(src.contains("half4 main(float2 "))
        assertEquals(src.count { it == '{' }, src.count { it == '}' })
        assertEquals(src.count { it == '(' }, src.count { it == ')' })
        // AGSL (SkSL, ES2): без цели числа с %, без масиви, цикли и производни.
        assertFalse(src.contains('%'))
        for (bad in listOf(Regex("""\bint\b"""), Regex("""\bfor\s*\("""), Regex("""\bwhile\b"""), Regex("""\bdFd[xy]\b"""), Regex("""\bfwidth\b"""), Regex("""\[\s*\d"""))) {
            assertFalse("$bad", bad.containsMatchIn(src))
        }
        // Всяка униформа се използва (неизползвана може да бъде премахната и задаването ѝ да хвърли).
        for (name in expected) {
            assertTrue(name, Regex("""\b$name\b""").findAll(src).count() >= 2)
        }
        // Цветовете идват като layout(color) half4 — правилното цветово пространство.
        Regex("""uniform\s+half4\s+(\w+)""").findAll(src).forEach { m ->
            assertTrue(m.value, src.contains("layout(color) uniform half4 ${m.groupValues[1]}"))
        }
    }

    @Test fun weaveSource() = checkSource(ShevitsaShaders.WEAVE, ShevitsaShaders.WEAVE_UNIFORMS)

    @Test fun foilSource() = checkSource(ShevitsaShaders.FOIL, ShevitsaShaders.FOIL_UNIFORMS)

    @Test fun weaveDecimalLiterals() {
        // В SkSL `1.0` и `1` са различни типове; числата в шейдъра са с десетична точка.
        val code = ShevitsaShaders.WEAVE.lines().filterNot { it.trim().startsWith("//") }.joinToString("\n")
        val bare = Regex("""(?<![\w.])\d+(?![\w.])""").findAll(code).map { it.value }.toList()
        // Изключение: цифрите в имената на типовете (float2, half4…) не се хващат от \w-границата.
        assertTrue("цели числа: $bare", bare.isEmpty())
    }

    @Test fun latticeNodeIsInkWithGoldArms() {
        assertEquals(INK, ShevitsaPattern.kind(0f, 0f, 18f, ribbon = false))
        for ((x, y) in listOf(1f to 0f, -1f to 0f, 0f to 1f, 0f to -1f)) {
            assertEquals(GOLD, ShevitsaPattern.kind(x, y, 18f, ribbon = false))
        }
        // Нишките на мрежата — по диагоналите.
        assertEquals(RED, ShevitsaPattern.kind(4f, 4f, 18f, ribbon = false))
        assertEquals(RED, ShevitsaPattern.kind(-5f, 5f, 18f, ribbon = false))
    }

    @Test fun diamondsAlternateStarsAndRhombi() {
        // Ромбът с център (9, 0): осморъка — злато в средата, мастило около него, червени лъчи.
        assertEquals(GOLD, ShevitsaPattern.kind(9f, 0f, 18f, ribbon = false))
        assertEquals(INK, ShevitsaPattern.kind(10f, 0f, 18f, ribbon = false))
        assertEquals(RED, ShevitsaPattern.kind(13f, 0f, 18f, ribbon = false))
        // Съседният ред ромбове (център (0, 9)): „ромб с ромб и точка“ — точка от мастило.
        assertEquals(INK, ShevitsaPattern.kind(0f, 9f, 18f, ribbon = false))
        assertEquals(RED, ShevitsaPattern.kind(4f, 9f, 18f, ribbon = false))
    }

    @Test fun fieldIsPointSymmetric() {
        for (x in -40..40) for (y in -40..40) {
            val k = ShevitsaPattern.kind(x.toFloat(), y.toFloat(), 18f, ribbon = false)
            assertEquals("($x, $y)", k, ShevitsaPattern.kind(-x.toFloat(), -y.toFloat(), 18f, ribbon = false))
            assertEquals("($x, $y)", k, ShevitsaPattern.kind(-x.toFloat(), y.toFloat(), 18f, ribbon = false))
        }
    }

    @Test fun ribbonHasBordersAndNothingOutside() {
        val period = 10
        assertEquals(15, ShevitsaPattern.ribbonRows(period))
        for (x in -30..30) {
            assertEquals(RED, ShevitsaPattern.kind(x.toFloat(), 7f, period.toFloat(), ribbon = true))
            assertEquals(RED, ShevitsaPattern.kind(x.toFloat(), -7f, period.toFloat(), ribbon = true))
            assertEquals(EMPTY, ShevitsaPattern.kind(x.toFloat(), 8f, period.toFloat(), ribbon = true))
            assertEquals(EMPTY, ShevitsaPattern.kind(x.toFloat(), -9f, period.toFloat(), ribbon = true))
        }
        // Веригата: осморъка и ромб се редуват по средата на лентата.
        val centres = (0..3).map { ShevitsaPattern.kind(5f + 10f * it, 0f, period.toFloat(), ribbon = true) }
        assertEquals(listOf(GOLD, INK, GOLD, INK), centres)
    }

    @Test fun everyMotifUsesAllThreeColours() {
        val kinds = (-20..20).flatMap { x -> (-20..20).map { y -> ShevitsaPattern.kind(x.toFloat(), y.toFloat(), 18f, ribbon = false) } }.toSet()
        assertEquals(setOf(EMPTY, RED, INK, GOLD), kinds)
    }
}
