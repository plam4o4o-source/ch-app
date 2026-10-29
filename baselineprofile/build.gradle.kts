/*
 * Генератор на Baseline и Startup профила на приложението (Macrobenchmark,
 * BaselineProfileRule). Пуска основните пътища на потребителя на емулатор/устройство
 * и записва кои класове и методи се ползват, за да се компилират AOT при инсталиране.
 *
 * Генериране (нужно е свързано устройство/емулатор с API 33+ или rooted API 28+):
 *     ./gradlew :app:generateBaselineProfile
 * В :app е `mergeIntoMain = true`, затова задачата е общата `generateBaselineProfile`
 * (плъгинът не създава `generateProdReleaseBaselineProfile` при сливане в main).
 * Резултатът е в app/src/main/generated/baselineProfiles/{baseline-prof,startup-prof}.txt.
 * В CI: .github/workflows/baseline-profile.yml (ръчно, всяка седмица и преди издание).
 *
 * AGP 9: вграденият Kotlin компилира src/main/kotlin (без плъгина kotlin.android).
 */
plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "org.chyavorec.baselineprofile"
    compileSdk = 37

    defaultConfig {
        // BaselineProfileRule: API 33+ без root (или 28+ с root).
        minSdk = 28
        targetSdk = 37
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Само prod: профилът се генерира от prodNonMinifiedRelease на :app
    // (същото измерение „env“ като в :app, за да съвпаднат вариантите).
    flavorDimensions += "env"
    productFlavors {
        create("prod") { dimension = "env" }
    }

    targetProjectPath = ":app"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

baselineProfile {
    // Свързаното устройство (в CI — емулаторът на reactivecircus/android-emulator-runner).
    useConnectedDevices = true
}

dependencies {
    implementation(libs.androidx.test.ext.junit)
    // androidx.test:runner (AndroidJUnitRunner) идва транзитивно от espresso-core.
    implementation(libs.androidx.test.espresso)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
