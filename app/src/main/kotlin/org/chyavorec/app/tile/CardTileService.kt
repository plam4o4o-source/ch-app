package org.chyavorec.app.tile

import android.app.PendingIntent
import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import org.chyavorec.app.R
import org.chyavorec.app.ui.navigation.LaunchIntents
import java.io.File

/**
 * Плочката „Читателска карта“ в бързите настройки: отваря екрана на картата през
 * неекспортирания вход ([LaunchIntents.openLink]). На заключено устройство първо се иска
 * отключване — картата е лична, а екранът ѝ е FLAG_SECURE. Плочката не показва номер или име:
 * панелът се вижда и на заключен екран.
 */
open class CardTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        val tile = qsTile ?: return
        CardTile.render(this, tile)
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        if (isLocked) unlockAndRun { launch() } else launch()
    }

    /** Отваря екрана и прибира панела. Без карта екранът сам показва добавянето ѝ. */
    internal open fun launch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(CardTile.pendingIntent(this))
        } else {
            // До Android 13 — само с Intent (от Android 14 той е забранен за целевия API).
            @Suppress("DEPRECATION")
            startActivityAndCollapse(CardTile.openIntent(this))
        }
    }
}

object CardTile {
    const val LINK = "my/card"
    /** requestCode на PendingIntent-а — извън числата на известията ([org.chyavorec.app.notifications.NotificationIds]). */
    private const val REQUEST_CODE = 900
    /** Същите файлове като в AppContainer (запазената карта и запомнената сесия). */
    internal const val CARD_FILE = "secure/card.bin"
    internal const val SESSION_FILE = "secure/session.bin"

    /**
     * Активна — има запазена карта или запомнен вход. Само дали файловете съществуват — без
     * Keystore и без читателските данни. Вход без „запомни ме“ е само в паметта: плочката е
     * неактивна, но екранът пак показва картата.
     */
    fun active(context: Context): Boolean {
        val dir = context.noBackupFilesDir
        return File(dir, CARD_FILE).exists() || File(dir, SESSION_FILE).exists()
    }

    fun render(context: Context, tile: Tile) {
        val active = active(context)
        tile.state = if (active) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = context.getString(R.string.tile_card_label)
        tile.contentDescription = tile.label
        tile.icon = Icon.createWithResource(context, R.drawable.ic_tile_card)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = context.getString(if (active) R.string.tile_card_subtitle else R.string.tile_card_subtitle_add)
        }
    }

    fun openIntent(context: Context): Intent =
        LaunchIntents.openLink(context, LINK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

    /** Неизменим и изричен (към неекспортирания вход на приложението). */
    fun pendingIntent(context: Context): PendingIntent = PendingIntent.getActivity(
        context, REQUEST_CODE, openIntent(context), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    /**
     * Системният диалог „Добавяне на плочка“ (Android 13+). [onAnswer] — само при отговор на
     * потребителя (добавена, вече я има, отказ); при грешка (напр. друг диалог) — нищо.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestAdd(context: Context, onAnswer: () -> Unit) {
        val bar = context.getSystemService(StatusBarManager::class.java) ?: return
        runCatching {
            bar.requestAddTileService(
                ComponentName(context, CardTileService::class.java),
                context.getString(R.string.tile_card_label),
                Icon.createWithResource(context, R.drawable.ic_tile_card),
                ContextCompat.getMainExecutor(context),
            ) { result ->
                when (result) {
                    StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED,
                    StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED,
                    StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED,
                    -> onAnswer()
                }
            }
        }
    }
}
