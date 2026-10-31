package org.chyavorec.app.tile

import android.Manifest
import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.chyavorec.app.R
import org.chyavorec.app.XorTestCipher
import org.chyavorec.app.di.AppContainer
import org.chyavorec.app.ui.navigation.LaunchIntents
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Тестовата плочка: вместо да стартира екрана, само записва кога е поискано отварянето. */
class RecordingCardTileService : CardTileService() {
    val launches = mutableListOf<Boolean>()
    override fun launch() { launches += isLocked }
}

/**
 * Плочката „Читателска карта“: активна само със запазена карта или запомнен вход, без лични
 * данни; докосването на заключено устройство първо иска отключване; отваря картата през
 * неекспортирания вход с неизменим PendingIntent; свързва я само системата.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class CardTileServiceTest {
    private val app get() = ApplicationProvider.getApplicationContext<Application>()

    @After fun cleanUp() {
        File(app.noBackupFilesDir, CardTile.CARD_FILE).delete()
        File(app.noBackupFilesDir, CardTile.SESSION_FILE).delete()
    }

    private fun listen(): Tile {
        val service = Robolectric.buildService(CardTileService::class.java).create().get()
        service.onStartListening()
        return service.qsTile
    }

    @Test fun inactiveWithoutCardActiveWithSavedCardAndNoPersonalData() = runBlocking<Unit> {
        val empty = listen()
        assertEquals(Tile.STATE_INACTIVE, empty.state)
        assertEquals(app.getString(R.string.tile_card_label), empty.label.toString())
        assertEquals(app.getString(R.string.tile_card_subtitle_add), empty.subtitle.toString())

        // Картата се записва както от екрана — през хранилището на приложението (същият файл).
        val container = AppContainer(app, cipherOverride = XorTestCipher())
        assertTrue(container.selfCardRepository.save("1024-5678", "Мария Петрова"))
        val active = listen()
        assertEquals(Tile.STATE_ACTIVE, active.state)
        assertEquals(app.getString(R.string.tile_card_subtitle), active.subtitle.toString())
        // Панелът се вижда и на заключен екран — нито номер, нито име.
        val shown = listOf(active.label, active.subtitle, active.contentDescription).joinToString(" ")
        assertFalse("1024" in shown || "Мария" in shown, shown)

        container.selfCardRepository.clear()
        assertEquals(Tile.STATE_INACTIVE, listen().state)
    }

    @Test fun rememberedSignInMakesTheTileActive() {
        File(app.noBackupFilesDir, CardTile.SESSION_FILE).apply { parentFile?.mkdirs() }.writeBytes(byteArrayOf(1))
        assertEquals(Tile.STATE_ACTIVE, listen().state)
    }

    @Test fun tapOnLockedDeviceUnlocksFirst() {
        val service = Robolectric.buildService(RecordingCardTileService::class.java).create().get()
        shadowOf(service).setLocked(true)
        service.onClick()
        // Екранът се отваря едва след отключването (unlockAndRun).
        assertEquals(listOf(false), service.launches)

        service.onClick()
        assertEquals(listOf(false, false), service.launches)
    }

    @Test fun opensTheCardThroughThePrivateEntry() {
        val pending = shadowOf(CardTile.pendingIntent(app))
        assertTrue(pending.isActivityIntent)
        assertTrue(pending.isImmutable)
        val intent = pending.savedIntent
        assertEquals(ComponentName(app.packageName, LaunchIntents.OPEN_LINK_ALIAS), intent.component)
        assertEquals("my/card", LaunchIntents.linkOf(intent, "https://chyavorec.org"))
        assertTrue(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
    }

    @Test fun onlyTheSystemCanBindTheTile() {
        val component = ComponentName(app, CardTileService::class.java)
        val info = app.packageManager.getServiceInfo(component, 0)
        assertEquals(Manifest.permission.BIND_QUICK_SETTINGS_TILE, info.permission)
        val found = app.packageManager.queryIntentServices(Intent(TileService.ACTION_QS_TILE).setPackage(app.packageName), 0)
        assertTrue(found.any { it.serviceInfo.name == CardTileService::class.java.name })
    }
}
