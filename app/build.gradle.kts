import java.util.Properties

// AGP 9: вграденият Kotlin (без плъгина org.jetbrains.kotlin.android).
plugins {
    alias(libs.plugins.android.application)
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
// Мостът за онлайн достъп на читатели (сайтът на читалището, Vercel). Приложението
// е само за библиотеката в Яворец — кодът „yavorec“ е фиксиран тук.
val inflibApiUrl = config("INFLIB_API_URL", "https://chyavorec.org/api/invlib/yavorec")
// Автоматично обновяване извън Google Play: update.json към последното GitHub Release.
val updateManifestUrl = config(
    "UPDATE_MANIFEST_URL",
    "https://github.com/plam4o4o-source/ch-app/releases/latest/download/update.json",
)
// ABI-та на release/play build-овете (виж buildTypes); debug пази всички — за емулаторите.
val releaseAbis = listOf("arm64-v8a", "armeabi-v7a", "x86_64") // ВРЕМЕННО само за проверката на емулатор

android {
    namespace = "org.chyavorec.app"
    compileSdk = 37

    defaultConfig {
        applicationId = "org.chyavorec.app"
        minSdk = 26
        targetSdk = 37
        versionCode = config("VERSION_CODE", "1").toInt()
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SITE_BASE_URL", siteBaseUrl.quoted())
        buildConfigField("String", "CATALOG_URLS", catalogUrls.quoted())
        buildConfigField("String", "INFLIB_API_URL", inflibApiUrl.quoted())
        buildConfigField("String", "UPDATE_MANIFEST_URL", updateManifestUrl.quoted())
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
        }
        create("prod") {
            dimension = "env"
            buildConfigField("String", "APP_ENV", "\"production\"")
            // Production НИКОГА не използва демо данни: флагът е твърдо false,
            // а демо реализациите не съществуват в prod source set-а (src/dev).
            buildConfigField("boolean", "USE_MOCK_DATA", "false")
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
            // Само ARM (реалните телефони): без x86/x86_64 native библиотеки в APK-а.
            // Debug пази всички ABI — за емулаторите.
            ndk { abiFilters += releaseAbis }
        }
        // Същото като release, но за Google Play: без самообновяване и без
        // разрешението REQUEST_INSTALL_PACKAGES (правилата на Play го забраняват —
        // там обновяването е от самия Google Play). Виж src/play/AndroidManifest.xml.
        create("play") {
            initWith(getByName("release"))
            matchingFallbacks += "release"
            // initWith копира и ndk.abiFilters; повторено изрично за яснота (множество — без дубликати).
            ndk { abiFilters += releaseAbis }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        isCoreLibraryDesugaringEnabled = false
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += setOf(
            "/META-INF/{AL2.0,LGPL2.1}",
            "META-INF/versions/9/previous-compilation-data.bin",
            // Метаданни, ненужни по време на работа (kotlin-reflect не се ползва;
            // META-INF/services/** и *.kotlin_module остават).
            "kotlin/**",
            "DebugProbesKt.bin",
            "META-INF/*.version",
            "META-INF/androidx/**",
            "/*.properties",
        )
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = false
        // Само езиците на приложението: махат се преводите на библиотеките (Material,
        // CameraX…) за десетки други езици — по-малък APK/AAB.
        localeFilters += listOf("bg", "en")
    }

    // Езикът се сменя и в самото приложение (Android 8–12: util/AppLanguage) —
    // и двата езика трябва да са в базовия APK, не в отделни езикови части на AAB.
    bundle {
        language {
            enableSplit = false
        }
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

androidComponents {
    // Play build има смисъл само за production.
    beforeVariants { v ->
        if (v.buildType == "play" && v.productFlavors.any { it.second == "dev" }) v.enable = false
    }
    // Самообновяване от GitHub Releases: само в prodRelease (APK за директно инсталиране).
    onVariants { v ->
        val selfUpdate = v.buildType == "release" && v.flavorName == "prod"
        v.buildConfigFields?.put(
            "SELF_UPDATE",
            com.android.build.api.variant.BuildConfigField("boolean", selfUpdate.toString(), "Обновяване от GitHub Releases"),
        )
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        // Стабилните по поведение, но още маркирани като експериментални Compose API
        // (TopAppBar scroll behavior, PullToRefreshBox, FlowRow, Pager).
        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.foundation.ExperimentalFoundationApi",
            "androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "androidx.compose.animation.ExperimentalSharedTransitionApi",
        )
    }
}

// Схемата на Room се пази в Git (за проверка на миграциите); плъгинът разделя
// изхода по вариант, така че dev/prod не пишат едновременно в един файл.
room {
    schemaDirectory("$projectDir/schemas")
}

tasks.withType<Test>().configureEach {
    // Снимките от ScreenshotTest са изход на теста — връщат се и от кеша на Gradle.
    outputs.dir(layout.buildDirectory.dir("reports/screenshots"))
    // Robolectric (SDK 36) на JDK 25 чете jdk.internal.access чрез reflection.
    jvmArgs("--add-exports=java.base/jdk.internal.access=ALL-UNNAMED", "--add-opens=java.base/jdk.internal.access=ALL-UNNAMED")
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStackTraces = true
        showStandardStreams = true
    }
}

dependencies {
    implementation("org.chyavorec:shared")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.browser)
    // Уиджет за началния екран.
    implementation(libs.androidx.glance.appwidget)
    // Инсталира src/main/baseline-prof.txt при инсталиране от извън Google Play (по-бърз старт).
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.animation)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // ZXing: генериране на баркодове (карта) и разчитане при сканиране.
    implementation(libs.zxing.core)
    // Скенер на баркодове (ISBN / инвентарен номер) — камера + разпознаване (ZXing) само на устройството.
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(platform(libs.compose.bom))
    testImplementation(libs.compose.ui.test.junit4)
    testImplementation(libs.androidx.work.testing)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.okhttp.tls)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
}
