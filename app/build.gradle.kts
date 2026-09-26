import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

/*
 * Конфигурация (виж .env.example). Приоритет:
 * 1) променлива на средата, 2) -P свойство на Gradle, 3) файл .env в корена, 4) стойност по подразбиране.
 * Тайни (пароли за подписване, бъдещи API ключове) НЕ се пазят в Git.
 */
val dotEnv = Properties().apply {
    val f = rootProject.file(".env")
    if (f.exists()) f.readLines(Charsets.UTF_8)
        .map { it.trim() }
        .filter { it.isNotEmpty() && !it.startsWith("#") && it.contains('=') }
        .forEach { line -> setProperty(line.substringBefore('=').trim(), line.substringAfter('=').trim().trim('"')) }
}

fun config(name: String, default: String = ""): String =
    System.getenv(name)?.takeIf { it.isNotBlank() }
        ?: (project.findProperty(name) as String?)?.takeIf { it.isNotBlank() }
        ?: dotEnv.getProperty(name)?.takeIf { it.isNotBlank() }
        ?: default

fun String.quoted() = "\"" + replace("\\", "\\\\").replace("\"", "\\\"") + "\""

val siteBaseUrl = config("API_BASE_URL", "https://chyavorec.org")
val catalogUrls = config(
    "CATALOG_URLS",
    "https://raw.githubusercontent.com/plam4o4o-source/yavorec-katalog/main/katalog.json|" +
        "https://cdn.jsdelivr.net/gh/plam4o4o-source/yavorec-katalog@main/katalog.json",
)
val inflibApiUrl = config("INFLIB_API_URL", "")

android {
    namespace = "org.chyavorec.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.chyavorec.app"
        minSdk = 26
        targetSdk = 36
        versionCode = config("VERSION_CODE", "1").toInt()
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "SITE_BASE_URL", siteBaseUrl.quoted())
        buildConfigField("String", "CATALOG_URLS", catalogUrls.quoted())
        buildConfigField("String", "INFLIB_API_URL", inflibApiUrl.quoted())
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "APP_ENV", config("APP_ENV", "development").quoted())
            // В dev по подразбиране читателските екрани работят с ясно маркирани демо данни.
            buildConfigField("boolean", "USE_MOCK_DATA", config("USE_MOCK_DATA", "true").toBoolean().toString())
            resValue("string", "app_name", "Читалище Яворец (dev)")
        }
        create("prod") {
            dimension = "env"
            buildConfigField("String", "APP_ENV", "\"production\"")
            // Production НИКОГА не използва демо данни: флагът е твърдо false,
            // а демо реализациите не съществуват в prod source set-а (src/dev).
            buildConfigField("boolean", "USE_MOCK_DATA", "false")
            resValue("string", "app_name", "Читалище Яворец")
        }
    }

    signingConfigs {
        create("release") {
            val storeFilePath = config("SIGNING_STORE_FILE")
            if (storeFilePath.isNotEmpty()) {
                storeFile = file(storeFilePath)
                storePassword = config("SIGNING_STORE_PASSWORD")
                keyAlias = config("SIGNING_KEY_ALIAS")
                keyPassword = config("SIGNING_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (config("SIGNING_STORE_FILE").isNotEmpty()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = false
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += setOf("/META-INF/{AL2.0,LGPL2.1}", "META-INF/versions/9/previous-compilation-data.bin")
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = false
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = true
        warningsAsErrors = false
        // Проверките за „по-нова версия“ зависят от мрежата и не са грешки в кода.
        disable += setOf("GradleDependency", "NewerVersionAvailable", "AndroidGradlePluginVersion", "OldTargetApi")
        xmlReport = true
        htmlReport = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

// Схемата на Room се пази в Git (за проверка на миграциите); плъгинът разделя
// изхода по вариант, така че dev/prod не пишат едновременно в един файл.
room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation("org.chyavorec:shared")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.animation)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.coil.compose)
    implementation(libs.zxing.core)

    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(platform(libs.compose.bom))
    testImplementation(libs.compose.ui.test.junit4)
    testImplementation(libs.androidx.work.testing)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
}
