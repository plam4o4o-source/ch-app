package org.chyavorec.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Генерира Baseline профила (и Startup профила за R8 dex layout) на приложението.
 *
 * Пуска се от Gradle: `./gradlew :app:generateBaselineProfile` (виж
 * baselineprofile/build.gradle.kts). Профилите се записват в
 * app/src/main/generated/baselineProfiles/.
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    /** Студен старт → начален екран със съдържание. Влиза и в Startup профила. */
    @Test
    fun startup() = rule.collect(
        packageName = targetPackage(),
        includeInStartupProfile = true,
    ) {
        coldStartToHome()
    }

    /** Основните пътища на потребителя (без старта — той е в [startup]). */
    @Test
    fun criticalUserJourneys() = rule.collect(packageName = targetPackage()) {
        coldStartToHome()
        catalogJourney()
        globalSearchJourney()
        messagesJourney()
        myGuestJourney()
        moreJourney()
    }

    /**
     * applicationId на целевото приложение: Baseline Profile плъгинът го подава като
     * аргумент на инструментацията; по подразбиране — prod (org.chyavorec.app).
     */
    private fun targetPackage(): String =
        InstrumentationRegistry.getArguments().getString("androidx.benchmark.targetPackageName")
            ?.takeIf { it.isNotBlank() }
            ?: "org.chyavorec.app"
}
