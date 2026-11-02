package org.chyavorec.app.data.security

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_WEAK
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume

/**
 * Проверка със заключването на телефона (пръстов отпечатък, лице или ПИН/фигура/парола) за
 * личните екрани. Приложението получава само „успешно/не“ — биометричните данни остават в
 * системата. В тестовете се подменя през AppContainer.
 */
interface DeviceUnlock {
    /** Има ли заключване на екрана — без него проверка няма как да има. */
    fun available(): Boolean

    /** Показва системната проверка над [activity]; `true` — успешно отключено. */
    suspend fun authenticate(activity: Context, title: String, subtitle: String): Boolean
}

/**
 * Системният BiometricPrompt (без androidx.biometric — тя тегли appcompat, ~400 KB): от
 * Android 11 — `BIOMETRIC_WEAK or DEVICE_CREDENTIAL`; на Android 10 — с
 * `setDeviceCredentialAllowed`, ако има записан отпечатък/лице. Иначе (и на Android 8–9) —
 * системният екран за ПИН/фигура/парола ([KeyguardManager.createConfirmDeviceCredentialIntent]).
 */
class SystemDeviceUnlock(private val context: Context) : DeviceUnlock {
    private val keyguard get() = context.getSystemService(KeyguardManager::class.java)

    override fun available(): Boolean = keyguard?.isDeviceSecure == true

    override suspend fun authenticate(activity: Context, title: String, subtitle: String): Boolean {
        if (!available()) return false
        val host = activity.findActivity() as? ComponentActivity ?: return false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && usePrompt(host)) {
            // Веднага след връщане отпред (напр. след заключения екран) системата може да откаже
            // прозореца — „не е отпред“ (BIOMETRIC_ERROR_CANCELED на мига). Това не е отказ от
            // потребителя: опитва се пак след малко.
            repeat(PROMPT_TRIES) {
                val started = SystemClock.elapsedRealtime()
                prompt(host, title, subtitle)?.let { return it }
                if (SystemClock.elapsedRealtime() - started > FAST_FAIL_MS) return false
                delay(RETRY_DELAY_MS)
            }
            return false
        }
        return confirmCredential(host, title, subtitle)
    }

    private fun usePrompt(activity: Context): Boolean = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> true
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> runCatching {
            @Suppress("DEPRECATION")
            activity.getSystemService(BiometricManager::class.java)?.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        }.getOrDefault(false)
        else -> false
    }

    /** `true`/`false` — отключено/отказано; `null` — системата затвори прозореца сама (BIOMETRIC_ERROR_CANCELED). */
    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun prompt(activity: Activity, title: String, subtitle: String): Boolean? = suspendCancellableCoroutine { cont ->
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                if (cont.isActive) cont.resume(true)
            }

            // Отказ, „Назад“, твърде много опити… — без отключване. Единичен грешен опит
            // (onAuthenticationFailed) оставя прозореца отворен.
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (cont.isActive) cont.resume(if (errorCode == BiometricPrompt.BIOMETRIC_ERROR_CANCELED) null else false)
            }
        }
        val builder = BiometricPrompt.Builder(activity).setTitle(title).setSubtitle(subtitle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        } else {
            @Suppress("DEPRECATION")
            builder.setDeviceCredentialAllowed(true)
        }
        val cancel = CancellationSignal()
        runCatching {
            builder.build().authenticate(cancel, ContextCompat.getMainExecutor(activity), callback)
        }.onFailure {
            if (cont.isActive) cont.resume(false)
            return@suspendCancellableCoroutine
        }
        cont.invokeOnCancellation { runCatching { cancel.cancel() } }
    }

    private suspend fun confirmCredential(activity: ComponentActivity, title: String, subtitle: String): Boolean {
        @Suppress("DEPRECATION")
        val intent = keyguard?.createConfirmDeviceCredentialIntent(title, subtitle) ?: return false
        return suspendCancellableCoroutine { cont ->
            lateinit var launcher: ActivityResultLauncher<Intent>
            launcher = activity.activityResultRegistry.register(
                "device-unlock-" + requests.incrementAndGet(), ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                launcher.unregister()
                if (cont.isActive) cont.resume(result.resultCode == Activity.RESULT_OK)
            }
            cont.invokeOnCancellation { launcher.unregister() }
            runCatching { launcher.launch(intent) }.onFailure {
                launcher.unregister()
                if (cont.isActive) cont.resume(false)
            }
        }
    }

    private companion object {
        val requests = AtomicInteger()
        const val PROMPT_TRIES = 3
        /** Отказ от системата по-бързо от това — „още не е отпред“, опитва се пак. */
        const val FAST_FAIL_MS = 1_500L
        const val RETRY_DELAY_MS = 400L
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Отключването на личните екрани (Настройки › Поверителност) в паметта на процеса: важи,
 * докато приложението не остане във фонов режим над [graceMs] или екранът не угасне.
 * [now] — монотонно време (ms), независимо от часовника на телефона.
 */
class PersonalUnlock(private val now: () -> Long, private val graceMs: Long = GRACE_MS) {
    private val _unlocked = MutableStateFlow(false)
    val unlocked: StateFlow<Boolean> = _unlocked.asStateFlow()
    @Volatile private var backgroundAt = -1L

    fun unlock() {
        backgroundAt = -1
        _unlocked.value = true
    }

    fun lock() {
        _unlocked.value = false
    }

    fun onBackground() {
        backgroundAt = now()
    }

    fun onForeground() {
        val at = backgroundAt
        backgroundAt = -1
        if (at < 0) return
        val away = now() - at
        if (away > graceMs || away < 0) lock()
    }

    fun onScreenOff() = lock()

    companion object {
        /** Колко може да е във фонов режим, без да се иска ново отключване. */
        const val GRACE_MS = 60_000L
    }
}
