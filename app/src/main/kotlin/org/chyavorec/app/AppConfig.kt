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
    /** update.json на последното GitHub Release (празно = без самообновяване). */
    val updateManifestUrl: String = "",
    /** true само в prodRelease (APK извън Google Play); false в Play и debug build-овете. */
    val selfUpdate: Boolean = false,
    /** debug | release | play */
    val buildType: String = "debug",
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
            updateManifestUrl = BuildConfig.UPDATE_MANIFEST_URL.trim().takeIf { it.startsWith("https://") }.orEmpty(),
            selfUpdate = BuildConfig.SELF_UPDATE,
            buildType = BuildConfig.BUILD_TYPE,
        )
    }
}
