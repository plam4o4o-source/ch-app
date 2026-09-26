package org.chyavorec.app.di

import org.chyavorec.app.AppConfig
import org.chyavorec.core.AppClock

/** Dev вариант: при USE_MOCK_DATA=true читателските екрани ползват демо услуги. */
object FlavorServices {
    fun demoReaderServices(config: AppConfig, clock: AppClock): ReaderServices? {
        if (!config.useMockData || config.isProduction) return null
        val demo = DemoInvLibServices(clock)
        return ReaderServices(demo, demo, demo, isDemo = true)
    }
}
