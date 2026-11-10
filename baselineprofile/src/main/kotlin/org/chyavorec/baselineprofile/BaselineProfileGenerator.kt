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

    // По-малко повторения от подразбиращите се (15 / 3): два поредни профила се различават с
    // 3–6% (~45 000 правила), а пътищата на емулатора траеха 30 min.
    private val maxIterations = 5
    private val stableIterations = 2

    /** Студен старт → начален екран със съдържание. Влиза и в Startup профила. */
    @Test
    fun startup() = rule.collect(
        packageName = targetPackage(),
        maxIterations = maxIterations,
        stableIterations = stableIterations,
        includeInStartupProfile = true,
    ) {
        coldStartToHome()
    }

    /**
     * Основните пътища на потребителя (без старта — той е в [startup]). Всеки е [optional]:
     * неуспешен път не проваля профила — остава всичко, до което другите са стигнали.
     */
    @Test
    fun criticalUserJourneys() = rule.collect(
        packageName = targetPackage(),
        maxIterations = maxIterations,
        stableIterations = stableIterations,
    ) {
        coldStartToHome()
        optional("home scroll") { homeScrollJourney() }
        optional("catalog") { catalogJourney() }
        optional("global search") { globalSearchJourney() }
        optional("messages") { messagesJourney() }
        optional("news") { newsJourney() }
        optional("want to read") { wantToReadJourney() }
        optional("my") { myGuestJourney() }
        optional("more") { moreJourney() }
        optional("gallery") { galleryJourney() }
        optional("card") { cardJourney() }
        optional("surprise") { surpriseJourney() }
        optional("share") { shareJourney() }
        // Историята „Моята читателска година“ иска история на четене (вход в профил), а prod
        // няма демо вход — тя, неподвижните шевици до API 32 и фолиото на картата без шейдър
        // са с ръчни правила в app/src/main/baselineProfiles/hand-written-rules.txt.
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
