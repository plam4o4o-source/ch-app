package org.chyavorec.app.ui.screens.scan

import com.google.zxing.BarcodeFormat
import com.google.zxing.Binarizer
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.ReaderException
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.common.HybridBinarizer

/**
 * Разчитане на баркод от яркостния (Y) канал на кадър от камерата със ZXing —
 * изцяло на устройството, без вградени native библиотеки и модели.
 *
 * Поддържани формати: EAN-13 (ISBN), EAN-8, Code 128, Code 39 (инвентарни
 * етикети) и QR. Опити по ред: ориентацията, в която потребителят вижда кадъра
 * (според `rotationDegrees`), после завъртяна на 90° (линеен баркод, държан
 * вертикално — там само линейните формати: QR не зависи от ориентацията); за
 * всяка — HybridBinarizer, после GlobalHistogramBinarizer.
 *
 * Не е безопасен за ползване от няколко нишки едновременно (анализаторът е на
 * една нишка); буферите се преизползват между кадрите.
 */
class BarcodeDecoder {
    private val reader = MultiFormatReader().apply { setHints(hints(FORMATS)) }
    private val linearReader = MultiFormatReader().apply { setHints(hints(FORMATS - BarcodeFormat.QR_CODE)) }
    private var rotated = ByteArray(0)

    /**
     * @param y яркостният канал (ред по ред, [rowStride] байта на ред; последният
     *   ред може да е по-къс — само [width] байта).
     * @param rotationDegrees завъртането на кадъра спрямо екрана (0/90/180/270).
     * @return разчетеният текст или null (няма баркод / нечетим кадър).
     */
    fun decode(y: ByteArray, width: Int, height: Int, rowStride: Int = width, rotationDegrees: Int = 0): String? {
        if (width <= 0 || height <= 0 || rowStride < width) return null
        if (y.size.toLong() < rowStride.toLong() * (height - 1) + width) return null
        return runCatching {
            val upright = PlanarYUVLuminanceSource(y, rowStride, height, 0, 0, width, height, false)
            // Кадрите на камерата обикновено са „легнали“ (90°): тогава първо завъртаният.
            val sideways = rotationDegrees % 180 != 0
            decodeSource(reader, if (sideways) rotate(y, width, height, rowStride) else upright)
                ?: decodeSource(linearReader, if (sideways) upright else rotate(y, width, height, rowStride))
        }.getOrNull()
    }

    private fun decodeSource(reader: MultiFormatReader, source: LuminanceSource): String? =
        decodeWith(reader, HybridBinarizer(source)) ?: decodeWith(reader, GlobalHistogramBinarizer(source))

    private fun decodeWith(reader: MultiFormatReader, binarizer: Binarizer): String? = try {
        reader.decodeWithState(BinaryBitmap(binarizer)).text?.takeIf { it.isNotBlank() }
    } catch (_: ReaderException) {
        null
    } finally {
        reader.reset()
    }

    /** Завърта Y канала на 90° по часовниковата стрелка в преизползван буфер (без отстъп на редовете). */
    private fun rotate(y: ByteArray, width: Int, height: Int, rowStride: Int): LuminanceSource {
        val size = width * height
        if (rotated.size < size) rotated = ByteArray(size)
        val out = rotated
        for (row in 0 until height) {
            val src = row * rowStride
            val col = height - 1 - row
            for (x in 0 until width) out[x * height + col] = y[src + x]
        }
        return PlanarYUVLuminanceSource(out, height, width, 0, 0, height, width, false)
    }

    companion object {
        val FORMATS: List<BarcodeFormat> = listOf(
            BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.CODE_128,
            BarcodeFormat.CODE_39, BarcodeFormat.QR_CODE,
        )

        private fun hints(formats: List<BarcodeFormat>): Map<DecodeHintType, Any> = mapOf(
            DecodeHintType.POSSIBLE_FORMATS to formats,
            DecodeHintType.TRY_HARDER to true,
        )
    }
}
