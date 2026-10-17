package org.chyavorec.app.ui.screens.scan

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import org.junit.Test
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Изрязването на кадъра до прозореца на визьора (за всяко завъртане на сензора) и
 * ограничаването на честотата на разчитане.
 */
class ScanGeometryTest {
    @Test fun windowMatchesTheOverlay() {
        val w = scanWindow(1080f, 2000f)
        assertEquals(842.4f, w.width, 0.01f)
        assertEquals(842.4f * 0.62f, w.height, 0.01f)
        assertEquals((1080f - 842.4f) / 2f, w.left, 0.01f)
        assertEquals((2000f - w.height) / 2f - 120f, w.top, 0.01f)
        // Нисък изглед (хоризонтално): височината е най-много половината.
        assertEquals(400f, scanWindow(2000f, 800f).height, 0.01f)
    }

    @Test fun cropWithoutRotationOrScaling() {
        // Изгледът е точно колкото кадъра: прозорец 780×400 при (110, 152) + 10 % от всяка страна.
        assertEquals(CropRect(32, 112, 936, 480), cropForWindow(1000, 800, 0, 1000, 800))
    }

    @Test fun invalidSizesGiveNoCrop() {
        assertNull(cropForWindow(0, 480, 90, 1080, 1920))
        assertNull(cropForWindow(640, 480, 90, 0, 0))
        assertNull(cropForWindow(640, 480, 45, 1080, 1920))
    }

    /**
     * За произволни размери и всяко завъртане: ъглите на изрязаната област, пренесени
     * обратно в изгледа (независимо изчисление: буфер → изправен кадър → FILL_CENTER),
     * съвпадат с разширения прозорец с точност до един пиксел на буфера.
     */
    @Test fun cropMapsBackOntoTheWindowForEveryRotation() {
        val random = Random(3)
        repeat(200) {
            val rotation = 90 * random.nextInt(4)
            val fw = random.nextInt(320, 1921)
            val fh = random.nextInt(240, 1081)
            val vw = random.nextInt(300, 2401)
            val vh = random.nextInt(300, 2401)
            val crop = assertNotNull(cropForWindow(fw, fh, rotation, vw, vh), "$fw×$fh $rotation° → $vw×$vh")
            val window = scanWindow(vw.toFloat(), vh.toFloat())
            val mx = window.width * 0.1f
            val my = window.height * 0.1f
            val corners = listOf(
                toView(crop.left.toFloat(), crop.top.toFloat(), fw, fh, rotation, vw, vh),
                toView((crop.left + crop.width).toFloat(), (crop.top + crop.height).toFloat(), fw, fh, rotation, vw, vh),
            )
            val left = corners.minOf { it.first }
            val right = corners.maxOf { it.first }
            val top = corners.minOf { it.second }
            val bottom = corners.maxOf { it.second }
            val sideways = rotation % 180 != 0
            val scale = max(vw.toFloat() / (if (sideways) fh else fw), vh.toFloat() / (if (sideways) fw else fh))
            val tolerance = scale + 0.01f
            // Областта никога не е по-тясна от прозореца (закръгляне навън) и не е много по-голяма.
            fun near(expected: Float, actual: Float, outward: Float) {
                assertTrue(abs(expected - actual) <= tolerance && (actual - expected) * outward >= -0.01f, "$expected / $actual ($fw×$fh $rotation° $vw×$vh)")
            }
            near(window.left - mx, left, -1f)
            near(window.left + window.width + mx, right, 1f)
            near(window.top - my, top, -1f)
            near(window.top + window.height + my, bottom, 1f)
        }
    }

    /**
     * Цял път: изправена сцена (каквато я вижда потребителят) с EAN-13 в прозореца и QR
     * извън него, „заснета“ от сензор с даденото завъртане. В изрязаната област се
     * разчита само кодът в прозореца; кодът извън него — само в целия кадър.
     */
    @Test fun decodesOnlyWhatIsInsideTheWindow() {
        val decoder = BarcodeDecoder()
        val isbn = "9780306406157"
        val qr = "https://chyavorec.org/katalog?inv=123"
        for (rotation in listOf(0, 90, 180, 270)) {
            val sideways = rotation % 180 != 0
            // Телефонът изправен — сензорът е „легнал“; хоризонтално — съвпадат.
            val (fw, fh) = 640 to 480
            val (vw, vh) = if (sideways) 1080 to 1920 else 1920 to 1080
            val uw = if (sideways) fh else fw
            val uh = if (sideways) fw else fh
            val scale = max(vw.toFloat() / uw, vh.toFloat() / uh)
            val window = scanWindow(vw.toFloat(), vh.toFloat())
            // Центърът на прозореца в изправения кадър.
            val cx = ((window.left + window.width / 2f) - (vw - uw * scale) / 2f) / scale
            val cy = ((window.top + window.height / 2f) - (vh - uh * scale) / 2f) / scale

            val withBoth = Scene(uw, uh)
            withBoth.draw(encode(BarcodeFormat.EAN_13, isbn), cx.toInt(), cy.toInt(), module = 2, barHeight = 60)
            withBoth.draw(encode(BarcodeFormat.QR_CODE, qr), uw / 2, uh - 40, module = 2, barHeight = 0)
            val crop = assertNotNull(cropForWindow(fw, fh, rotation, vw, vh))
            val frame = withBoth.toSensor(rotation)
            assertEquals(frame.size, fw * fh)
            assertEquals(isbn, decoder.decode(frame, fw, fh, fw, rotation, crop), "EAN-13 в прозореца, $rotation°")

            val qrOnly = Scene(uw, uh)
            qrOnly.draw(encode(BarcodeFormat.QR_CODE, qr), uw / 2, uh - 40, module = 2, barHeight = 0)
            val qrFrame = qrOnly.toSensor(rotation)
            assertNull(decoder.decode(qrFrame, fw, fh, fw, rotation, crop), "QR извън прозореца, $rotation°")
            assertEquals(qr, decoder.decode(qrFrame, fw, fh, fw, rotation), "QR в целия кадър, $rotation°")
        }
    }

    @Test fun throttleSpacesDecodes() {
        val t = DecodeThrottle(intervalMs = 150, minGapMs = 50)
        assertTrue(t.tryStart(0))
        assertFalse(t.tryStart(10), "докато тече опит")
        t.finish(20)
        assertFalse(t.tryStart(100))
        assertTrue(t.tryStart(150))
        // Бавен опит (400 ms): следващият — чак 50 ms след края му.
        t.finish(550)
        assertFalse(t.tryStart(560))
        assertTrue(t.tryStart(600))
    }

    @Test fun throttleCapsTheRateAt30Fps() {
        val t = DecodeThrottle(intervalMs = 150, minGapMs = 50)
        var decodes = 0
        var now = 0L
        while (now < 3_000) {
            if (t.tryStart(now)) {
                decodes++
                t.finish(now + 12)
            }
            now += 33 // кадър на ~30 fps
        }
        // ~6 опита в секунда вместо 30.
        assertTrue(decodes in 15..21, "decodes = $decodes")
    }

    /** Буфер → изправен кадър (завъртане по часовниковата стрелка) → изглед (FILL_CENTER). */
    private fun toView(bx: Float, by: Float, fw: Int, fh: Int, rotation: Int, vw: Int, vh: Int): Pair<Float, Float> {
        var x = bx
        var y = by
        var w = fw.toFloat()
        var h = fh.toFloat()
        repeat(rotation / 90) {
            val nx = h - y
            y = x
            x = nx
            val t = w; w = h; h = t
        }
        val scale = max(vw / w, vh / h)
        return (x * scale + (vw - w * scale) / 2f) to (y * scale + (vh - h * scale) / 2f)
    }

    private fun encode(format: BarcodeFormat, value: String): BitMatrix =
        MultiFormatWriter().encode(value, format, 0, 0, mapOf(EncodeHintType.MARGIN to 0))

    /** Изправена сцена: светъл фон, кодове с [module] пиксела на модул. */
    private class Scene(val width: Int, val height: Int) {
        val y = ByteArray(width * height) { LIGHT.toByte() }

        /** Рисува [matrix] с център ([cx], [cy]); линейните — с височина [barHeight]. */
        fun draw(matrix: BitMatrix, cx: Int, cy: Int, module: Int, barHeight: Int) {
            val linear = matrix.height == 1
            val w = matrix.width * module
            val h = if (linear) barHeight else matrix.height * module
            val left = cx - w / 2
            val top = cy - h / 2
            require(left >= 0 && top >= 0 && left + w <= width && top + h <= height) { "кодът не се събира" }
            for (r in 0 until h) for (c in 0 until w) {
                if (matrix.get(c / module, if (linear) 0 else r / module)) y[(top + r) * width + left + c] = DARK.toByte()
            }
        }

        /**
         * Буферът, който сензорът дава: изправената сцена, завъртяна обратно на
         * часовниковата стрелка на [rotation]° (буферът, завъртян по часовниковата — сцената).
         */
        fun toSensor(rotation: Int): ByteArray {
            var data = y
            var w = width
            var h = height
            repeat(rotation / 90) {
                val out = ByteArray(w * h)
                for (r in 0 until h) for (c in 0 until w) out[(w - 1 - c) * h + r] = data[r * w + c]
                data = out
                val t = w; w = h; h = t
            }
            return data
        }

        companion object {
            const val LIGHT = 205
            const val DARK = 45
        }
    }
}
