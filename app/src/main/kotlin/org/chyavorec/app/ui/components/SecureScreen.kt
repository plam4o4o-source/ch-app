package org.chyavorec.app.ui.components

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import java.util.WeakHashMap

/**
 * Докато е в композицията, екранът не попада в „Скорошни“, в снимки и записи на екрана:
 * FLAG_SECURE на прозореца, а на Android 13+ — и без снимка за списъка със задачи.
 * При излизане всичко се връща. Ползва се за читателската карта (баркодът е като парола
 * за библиотеката).
 */
@Composable
fun SecureScreen() {
    val activity = LocalActivity.current
    DisposableEffect(activity) {
        if (activity == null) return@DisposableEffect onDispose { }
        SecureWindows.acquire(activity)
        onDispose { SecureWindows.release(activity) }
    }
}

/**
 * Брои защитените екрани на всяка активност: при прехода „карта → цял екран“ (и обратно)
 * двата са на екрана за миг и излизащият не бива да свали флага на влизащия.
 * Само от главната нишка (ефектите на Compose).
 */
private object SecureWindows {
    private val holders = WeakHashMap<Activity, Int>()

    fun acquire(activity: Activity) {
        val n = holders[activity] ?: 0
        holders[activity] = n + 1
        if (n == 0) setSecure(activity, true)
    }

    fun release(activity: Activity) {
        val n = (holders[activity] ?: return) - 1
        if (n > 0) {
            holders[activity] = n
            return
        }
        holders.remove(activity)
        setSecure(activity, false)
    }

    private fun setSecure(activity: Activity, secure: Boolean) {
        val window = activity.window ?: return
        if (secure) window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) else window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Системна услуга — в тестовата среда може да липсва; флагът по-горе стига и сам.
            runCatching { activity.setRecentsScreenshotEnabled(!secure) }
        }
    }
}
