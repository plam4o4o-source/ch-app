package org.chyavorec.app.data.scan

import android.content.Context
import com.google.android.gms.common.api.OptionalModuleApi
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** Резултат от едно отваряне на скенера. */
sealed interface ScanOutcome {
    /** Разчетеният текст на кода. */
    data class Code(val raw: String) : ScanOutcome
    /** Потребителят е затворил скенера („Назад“). */
    data object Cancelled : ScanOutcome
    /** Няма Google Play услуги или модулът на скенера още не е изтеглен. */
    data object Unavailable : ScanOutcome
}

/**
 * Скенерът на баркодове. [activity] — контекстът на екрана (скенерът се показва над него).
 * В тестовете се подменя през AppContainer — без Google Play услуги.
 */
interface BarcodeScanner {
    suspend fun scan(activity: Context): ScanOutcome
}

/**
 * Скенерът на Google (Google Play услуги, code scanner): своят прозорец с камерата, без
 * разрешение за камера в приложението — то получава само разчетения код. Кадрите се
 * обработват от Play услугите на телефона.
 *
 * Форматите — както досега: EAN-13 (ISBN), EAN-8, UPC-A, Code 128 и Code 39 (инвентарни
 * етикети) и QR (адрес от каталога).
 */
class PlayServicesBarcodeScanner : BarcodeScanner {
    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_QR_CODE,
        )
        .enableAutoZoom()
        .build()

    override suspend fun scan(activity: Context): ScanOutcome {
        val scanner = try {
            GmsBarcodeScanning.getClient(activity, options)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            return ScanOutcome.Unavailable
        }
        return when (val r = scanner.startScan().awaitOutcome()) {
            is TaskResult.Success -> ScanOutcome.Code(r.value.rawValue ?: r.value.displayValue.orEmpty())
            TaskResult.Cancelled -> ScanOutcome.Cancelled
            is TaskResult.Failure -> {
                val code = (r.error as? MlKitException)?.errorCode
                when (code) {
                    MlKitException.CODE_SCANNER_CANCELLED -> ScanOutcome.Cancelled
                    else -> {
                        // Модулът на скенера може още да не е изтеглен — молба към Play услугите
                        // да го изтеглят сега (следващият опит ще мине); иначе няма Play услуги.
                        requestModule(activity, scanner)
                        ScanOutcome.Unavailable
                    }
                }
            }
        }
    }

    private suspend fun requestModule(activity: Context, scanner: OptionalModuleApi) {
        try {
            val client = ModuleInstall.getClient(activity)
            val available = client.areModulesAvailable(scanner).awaitOutcome()
            if (available is TaskResult.Success && !available.value.areModulesAvailable()) {
                client.installModules(ModuleInstallRequest.newBuilder().addApi(scanner).build())
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Без Play услуги — само ръчното въвеждане.
        }
    }
}

private sealed interface TaskResult<out T> {
    data class Success<T>(val value: T) : TaskResult<T>
    data object Cancelled : TaskResult<Nothing>
    data class Failure(val error: Exception) : TaskResult<Nothing>
}

/** Изчакване на Task от Play услугите (без допълнителна библиотека за корутини). */
private suspend fun <T> Task<T>.awaitOutcome(): TaskResult<T> = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { t ->
        cont.resume(
            when {
                t.isCanceled -> TaskResult.Cancelled
                t.isSuccessful -> TaskResult.Success(t.result)
                else -> TaskResult.Failure(t.exception ?: IllegalStateException("task failed"))
            },
        )
    }
}
