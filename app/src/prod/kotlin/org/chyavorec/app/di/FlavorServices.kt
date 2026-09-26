package org.chyavorec.app.di

import org.chyavorec.app.AppConfig
import org.chyavorec.core.AppClock

/**
 * Production вариант: демо данни НЕ съществуват. Дори при грешно зададен
 * USE_MOCK_DATA тук няма какво да бъде върнато — изборът е компилационен.
 */
object FlavorServices {
    @Suppress("UNUSED_PARAMETER")
    fun demoReaderServices(config: AppConfig, clock: AppClock): ReaderServices? = null
}
