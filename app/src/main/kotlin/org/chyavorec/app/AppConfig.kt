package org.chyavorec.app

/**
 * Конфигурация от BuildConfig (виж .env.example и app/build.gradle.kts).
 * Тук няма тайни — само публични адреси.
 */
data class AppConfig(
    val siteBaseUrl: String,
    val catalogUrls: List<String>,
    /** Празно = InvLib няма онлайн API → читателските функции са „недостъпни“. */
    val inflibApiUrl: String,
    val environment: String,
    val useMockData: Boolean,
    val versionName: String,
    val versionCode: Int,
) {
    /** Приема се само HTTPS адрес — HTTP би изложил токените. */
    val inflibConfigured: Boolean get() = inflibApiUrl.startsWith("https://")
    val isProduction: Boolean get() = environment == "production"

    companion object {
        fun fromBuildConfig() = AppConfig(
            siteBaseUrl = BuildConfig.SITE_BASE_URL.trimEnd('/'),
            catalogUrls = BuildConfig.CATALOG_URLS.split('|').map { it.trim() }.filter { it.startsWith("https://") },
            inflibApiUrl = BuildConfig.INFLIB_API_URL.trim(),
            environment = BuildConfig.APP_ENV,
            useMockData = BuildConfig.USE_MOCK_DATA,
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
        )
    }
}
