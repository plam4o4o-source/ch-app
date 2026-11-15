package org.chyavorec.app.ui.screens.my

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTouchInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.chyavorec.app.ui.components.LocalAgslEnabled
import org.chyavorec.app.ui.components.LocalReducedMotion
import org.chyavorec.app.ui.theme.ChitalishteTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSensor
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Сензорът на наклона на примерната карта (без вход): не се включва с появата ѝ, а чак при
 * докосване; докато се пише във формата за вход — изобщо. Истинската карта — както досега.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class, qualifiers = "bg-w411dp-h891dp-xhdpi")
class SampleCardSensorTest {
    @get:Rule val compose = createComposeRule()

    private val sensors = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Before fun addSensors() {
        shadowOf(sensors).addSensor(ShadowSensor.newInstance(Sensor.TYPE_GAME_ROTATION_VECTOR))
        shadowOf(sensors).addSensor(ShadowSensor.newInstance(Sensor.TYPE_ACCELEROMETER))
    }

    private fun show(content: @Composable () -> Unit) {
        compose.setContent {
            ChitalishteTheme {
                CompositionLocalProvider(LocalReducedMotion provides false, LocalAgslEnabled provides false) { content() }
            }
        }
        compose.waitForIdle()
    }

    private val listeners get() = shadowOf(sensors).listeners.size

    private fun touchCard() {
        compose.onNodeWithContentDescription("Примерна читателска карта с име, номер и баркод").performTouchInput { down(center); up() }
        compose.waitForIdle()
    }

    @Test fun sampleCardWakesTheSensorOnlyOnTouch() {
        show { SampleCardPreview(sharedKey = null) }
        assertEquals(0, listeners, "сензорът е включен без докосване")
        touchCard()
        assertEquals(1, listeners, "докосването не включи сензора")
    }

    @Test fun sampleCardKeepsStillWhileTyping() {
        show { SampleCardPreview(sharedKey = null, tiltPaused = true) }
        touchCard()
        assertEquals(0, listeners)
    }

    /** Въведението кани да се наклони телефонът — там сензорът тръгва с появата, както на истинската карта. */
    @Test fun liveSampleCardStartsTheSensorOnAppear() {
        show { SampleCardPreview(sharedKey = null, live = true) }
        assertTrue(listeners > 0)
    }

    @Test fun signedInCardStartsTheSensorOnAppear() {
        show { LibraryCard(sampleCardData()) }
        assertTrue(listeners > 0)
    }
}
