package org.chyavorec.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/** Генериране на баркодове с ZXing (само кодиране, без камера и без мрежа). */
object Barcodes {
    /**
     * InvLib печата читателските карти в Code 39 (виж barcode-labels.js) —
     * затова дигиталната карта ползва същия формат, за да я чете същият четец.
     */
    fun code39(value: String): BitMatrix? = runCatching {
        MultiFormatWriter().encode(value.uppercase(), BarcodeFormat.CODE_39, 0, 1, mapOf(EncodeHintType.MARGIN to 0))
    }.getOrNull()

    fun qr(value: String): BitMatrix? = runCatching {
        MultiFormatWriter().encode(
            value, BarcodeFormat.QR_CODE, 0, 0,
            mapOf(EncodeHintType.MARGIN to 1, EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M, EncodeHintType.CHARACTER_SET to "UTF-8"),
        )
    }.getOrNull()
}

/** Линеен баркод, изчертан векторно — остър при всяка плътност на екрана. */
@Composable
fun LinearBarcode(value: String, description: String, modifier: Modifier = Modifier, height: Dp = 72.dp, color: Color = Color.Black) {
    val matrix = remember(value) { Barcodes.code39(value) } ?: return
    Canvas(modifier.fillMaxWidth().height(height).semantics { contentDescription = description }) {
        val quiet = 10f
        val module = (size.width - 2 * quiet) / matrix.width
        for (x in 0 until matrix.width) {
            if (matrix.get(x, 0)) drawRect(color, Offset(quiet + x * module, 0f), Size(module + 0.5f, size.height))
        }
    }
}

@Composable
fun QrCodeView(value: String, description: String, modifier: Modifier = Modifier, color: Color = Color.Black) {
    val matrix = remember(value) { Barcodes.qr(value) } ?: return
    Canvas(modifier.semantics { contentDescription = description }) {
        val cell = minOf(size.width, size.height) / matrix.width
        for (y in 0 until matrix.height) for (x in 0 until matrix.width) {
            if (matrix.get(x, y)) drawRect(color, Offset(x * cell, y * cell), Size(cell + 0.5f, cell + 0.5f))
        }
    }
}
