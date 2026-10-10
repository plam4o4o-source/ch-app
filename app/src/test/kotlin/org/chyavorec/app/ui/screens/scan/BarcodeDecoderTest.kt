package org.chyavorec.app.ui.screens.scan

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Скенерът (ZXing): всеки поддържан формат се кодира, рисува се в „кадър от
 * камерата“ (яркостен канал, сив фон, шум, неточен мащаб, отстъп на редовете)
 * и се разчита през [BarcodeDecoder] — и изправен, и завъртян на 90°.
 */
class BarcodeDecoderTest {
    private val samples = listOf(
        BarcodeFormat.EAN_13 to "9780306406157", // ISBN 978-0-306-40615-7
        BarcodeFormat.EAN_8 to "96385074",
        BarcodeFormat.CODE_128 to "INV-000123",
        BarcodeFormat.CODE_39 to "12345",
        BarcodeFormat.QR_CODE to "https://chyavorec.org/katalog?inv=123",
    )

    @Test fun decodesEveryFormatUpright() {
        val decoder = BarcodeDecoder()
        for ((format, value) in samples) {
            val frame = Frame.render(encode(format, value), scale = 3.0, noise = 0)
            assertEquals(value, decoder.decode(frame.y, frame.width, frame.height), "$format изправен")
        }
    }

    @Test fun decodesWithNoiseOddScaleAndRowPadding() {
        val decoder = BarcodeDecoder()
        for ((format, value) in samples) {
            val frame = Frame.render(encode(format, value), scale = 2.6, noise = 24, rowPadding = 32)
            assertEquals(value, decoder.decode(frame.y, frame.width, frame.height, frame.rowStride), "$format с шум")
        }
    }

    /** Камерата дава „легнал“ кадър (rotationDegrees = 90): баркодът в буфера е вертикален. */
    @Test fun decodesRotatedFrames() {
        val decoder = BarcodeDecoder()
        for ((format, value) in samples) {
            val frame = Frame.render(encode(format, value), scale = 3.0, noise = 12).rotated()
            assertEquals(value, decoder.decode(frame.y, frame.width, frame.height, frame.rowStride, rotationDegrees = 90), "$format 90°")
            // И когато ориентацията е неизвестна/грешна — опитът със завъртане пак го намира.
            assertEquals(value, decoder.decode(frame.y, frame.width, frame.height, frame.rowStride, rotationDegrees = 0), "$format 90° (rot 0)")
        }
    }

    @Test fun emptyOrBrokenFramesGiveNull() {
        val decoder = BarcodeDecoder()
        val gray = ByteArray(320 * 240) { 0x80.toByte() }
        assertNull(decoder.decode(gray, 320, 240))
        val random = Random(7)
        val noise = ByteArray(320 * 240) { random.nextInt(256).toByte() }
        assertNull(decoder.decode(noise, 320, 240, rotationDegrees = 90))
        // Буфер, по-къс от обявения размер, и невалидни размери — без изключение.
        assertNull(decoder.decode(ByteArray(100), 320, 240))
        assertNull(decoder.decode(gray, 0, 240))
        assertNull(decoder.decode(gray, 320, 240, rowStride = 100))
    }

    /** Последният ред на равнината от камерата може да е без отстъп (по-къс от rowStride). */
    @Test fun acceptsShortLastRow() {
        val decoder = BarcodeDecoder()
        val frame = Frame.render(encode(BarcodeFormat.EAN_13, "9780306406157"), scale = 3.0, noise = 0, rowPadding = 16)
        val trimmed = frame.y.copyOf(frame.rowStride * (frame.height - 1) + frame.width)
        assertEquals("9780306406157", decoder.decode(trimmed, frame.width, frame.height, frame.rowStride))
    }

    private fun encode(format: BarcodeFormat, value: String): BitMatrix =
        MultiFormatWriter().encode(value, format, 0, 0, mapOf(EncodeHintType.MARGIN to 0))

    /** Яркостен канал: [width]×[height] пиксела, [rowStride] байта на ред. */
    private class Frame(val y: ByteArray, val width: Int, val height: Int, val rowStride: Int) {
        /** Завъртане на 90° обратно на часовниковата стрелка (както сензорът „вижда“ изправения кадър). */
        fun rotated(): Frame {
            val out = ByteArray(width * height)
            for (r in 0 until height) for (c in 0 until width) {
                // (c, r) → (r, width - 1 - c); новата ширина е height.
                out[(width - 1 - c) * height + r] = y[r * rowStride + c]
            }
            return Frame(out, height, width, height)
        }

        companion object {
            private const val WIDTH = 640
            private const val HEIGHT = 480
            private const val LIGHT = 205
            private const val DARK = 45

            /**
             * Рисува [matrix] в центъра на кадър 640×480 с [scale] пиксела на модул
             * (линейните кодове — с височина 100 px), тиха зона от 10 модула и
             * равномерен шум ±[noise].
             */
            fun render(matrix: BitMatrix, scale: Double, noise: Int, rowPadding: Int = 0): Frame {
                val stride = WIDTH + rowPadding
                val y = ByteArray(stride * HEIGHT)
                val random = Random(42)
                val linear = matrix.height == 1
                val codeW = (matrix.width * scale).toInt()
                val codeH = if (linear) 100 else (matrix.height * scale).toInt()
                require(codeW + 20 * scale < WIDTH && codeH + 20 * scale < HEIGHT) { "кодът не се събира в кадъра" }
                val left = (WIDTH - codeW) / 2
                val top = (HEIGHT - codeH) / 2
                for (r in 0 until HEIGHT) for (c in 0 until WIDTH) {
                    val inside = c in left until left + codeW && r in top until top + codeH
                    val dark = inside && run {
                        val mx = ((c - left) / scale).toInt().coerceAtMost(matrix.width - 1)
                        val my = if (linear) 0 else ((r - top) / scale).toInt().coerceAtMost(matrix.height - 1)
                        matrix.get(mx, my)
                    }
                    val base = if (dark) DARK else LIGHT
                    val v = if (noise > 0) base + random.nextInt(-noise, noise + 1) else base
                    y[r * stride + c] = v.coerceIn(0, 255).toByte()
                }
                return Frame(y, WIDTH, HEIGHT, stride)
            }
        }
    }
}
