package org.chyavorec.app.ui.screens.scan

import com.google.zxing.BarcodeFormat
import com.google.zxing.Binarizer
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.ReaderException
import com.google.zxing.Result
import com.google.zxing.common.BitArray
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.oned.Code128Reader
import com.google.zxing.oned.Code39Reader
import com.google.zxing.oned.MultiFormatUPCEANReader
import com.google.zxing.oned.OneDReader
import com.google.zxing.qrcode.QRCodeReader

/**
 * Разчитане на баркод от яркостния (Y) канал на кадър от камерата със ZXing —
 * изцяло на устройството, без вградени native библиотеки и модели.
 *
 * Поддържани формати: EAN-13 (ISBN; UPC-A се чете като EAN-13 с водеща 0), EAN-8,
 * Code 128, Code 39 (инвентарни етикети) и QR. Опити по ред: ориентацията, в която
 * потребителят вижда кадъра (според `rotationDegrees`), после завъртяна на 90°
 * (линеен баркод, държан вертикално — там само линейните формати: QR не зависи от
 * ориентацията); за всяка — HybridBinarizer, после GlobalHistogramBinarizer.
 *
 * Без TRY_HARDER: кадърът е изрязан до прозореца на визьора (кодът е в него), а
 * линейните кодове се търсят в средната част на прозореца — така един опит е
 * многократно по-евтин, а следващият кадър идва след миг.
 *
 * Четците са изброени поотделно (не MultiFormatReader): така R8 маха от APK-а
 * останалите формати на ZXing (PDF417, Aztec, Data Matrix, RSS…).
 *
 * Не е безопасен за ползване от няколко нишки едновременно (анализаторът е на
 * една нишка); буферите се преизползват между кадрите.
 */
class BarcodeDecoder {
    private val linear = LinearReader(mapOf(DecodeHintType.POSSIBLE_FORMATS to LINEAR_FORMATS))
    private val qr = QRCodeReader()
    private val readers: Array<Reader> = arrayOf(linear, qr)
    private val linearOnly: Array<Reader> = arrayOf(linear)
    private var rotated = ByteArray(0)

    /**
     * @param y яркостният канал (ред по ред, [rowStride] байта на ред; последният
     *   ред може да е по-къс — само [width] байта).
     * @param rotationDegrees завъртането на кадъра спрямо екрана (0/90/180/270).
     * @param crop частта от кадъра, в която се търси (прозорецът на визьора); null — целият кадър.
     * @return разчетеният текст или null (няма баркод / нечетим кадър).
     */
    fun decode(
        y: ByteArray, width: Int, height: Int, rowStride: Int = width, rotationDegrees: Int = 0,
        crop: CropRect? = null,
    ): String? {
        if (width <= 0 || height <= 0 || rowStride < width) return null
        val area = crop ?: CropRect(0, 0, width, height)
        if (area.left < 0 || area.top < 0 || area.width <= 0 || area.height <= 0) return null
        if (area.left + area.width > width || area.top + area.height > height) return null
        if (y.size.toLong() < rowStride.toLong() * (area.top + area.height - 1) + area.left + area.width) return null
        return runCatching {
            // Буферът се подава с височина до долния край на областта: последният ред може да е по-къс.
            val upright = PlanarYUVLuminanceSource(
                y, rowStride, area.top + area.height, area.left, area.top, area.width, area.height, false,
            )
            // Кадрите на камерата обикновено са „легнали“ (90°): тогава първо завъртаният.
            val sideways = rotationDegrees % 180 != 0
            decodeSource(readers, if (sideways) rotate(y, rowStride, area) else upright)
                ?: decodeSource(linearOnly, if (sideways) upright else rotate(y, rowStride, area))
        }.getOrNull()
    }

    private fun decodeSource(readers: Array<Reader>, source: LuminanceSource): String? =
        decodeWith(readers, HybridBinarizer(source)) ?: decodeWith(readers, GlobalHistogramBinarizer(source))

    private fun decodeWith(readers: Array<Reader>, binarizer: Binarizer): String? {
        val bitmap = BinaryBitmap(binarizer)
        for (reader in readers) {
            try {
                return reader.decode(bitmap, HINTS).text?.takeIf { it.isNotBlank() } ?: continue
            } catch (_: ReaderException) {
                // Следващият формат.
            } finally {
                reader.reset()
            }
        }
        return null
    }

    /** Завърта областта на 90° по часовниковата стрелка в преизползван буфер (без отстъп на редовете). */
    private fun rotate(y: ByteArray, rowStride: Int, area: CropRect): LuminanceSource {
        val width = area.width
        val height = area.height
        val size = width * height
        if (rotated.size < size) rotated = ByteArray(size)
        val out = rotated
        for (row in 0 until height) {
            val src = (area.top + row) * rowStride + area.left
            val col = height - 1 - row
            for (x in 0 until width) out[x * height + col] = y[src + x]
        }
        return PlanarYUVLuminanceSource(out, height, width, 0, 0, height, width, false)
    }

    /**
     * Линейните формати с едно обхождане на редовете (както MultiFormatOneDReader,
     * но само с нужните четци).
     */
    private class LinearReader(hints: Map<DecodeHintType, Any>) : OneDReader() {
        private val readers = arrayOf(MultiFormatUPCEANReader(hints), Code128Reader(), Code39Reader())

        override fun decodeRow(rowNumber: Int, row: BitArray, hints: Map<DecodeHintType, *>?): Result {
            for (reader in readers) {
                try {
                    return reader.decodeRow(rowNumber, row, hints)
                } catch (_: ReaderException) {
                    // Следващият формат.
                }
            }
            throw NotFoundException.getNotFoundInstance()
        }

        override fun reset() {
            for (reader in readers) reader.reset()
        }
    }

    companion object {
        private val LINEAR_FORMATS: List<BarcodeFormat> = listOf(
            BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.CODE_128, BarcodeFormat.CODE_39,
        )
        val FORMATS: List<BarcodeFormat> = LINEAR_FORMATS + BarcodeFormat.QR_CODE

        private val HINTS: Map<DecodeHintType, Any> = mapOf(DecodeHintType.POSSIBLE_FORMATS to FORMATS)
    }
}
