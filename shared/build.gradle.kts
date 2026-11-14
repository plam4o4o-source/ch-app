plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "org.chyavorec"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
    // Тестовете: runCurrent, advanceUntilIdle, StandardTestDispatcher… от kotlinx-coroutines-test са
    // още експериментални — без предупреждение за всяко извикване.
    target.compilations.named("test") {
        compileTaskProvider.configure { compilerOptions.optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi") }
    }
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.json)
    api(libs.okhttp)
    implementation(libs.jsoup)
    // Jsoup 1.22+ анотира API-то си с JSpecify (nullability) — нужни са при компилиране.
    compileOnly(libs.jspecify)

    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}

tasks.test {
    useJUnit()
    // Класовете — в до 2 JVM (MockWebServer е на случаен порт).
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceIn(1, 2)
    // Имена на файлове на кирилица (FileDownloaderTest): в среда без UTF-8 локал (LANG/LC_ALL празни —
    // sun.jnu.encoding=ANSI_X3.4-1968) JVM ги записва като „?“. На Linux тестовете вървят с C.UTF-8;
    // където средата вече е UTF-8 (CI, macOS), нищо не се променя.
    val jnu = System.getProperty("sun.jnu.encoding").orEmpty()
    if (System.getProperty("os.name").orEmpty().startsWith("Linux") && !jnu.equals("UTF-8", ignoreCase = true)) {
        environment("LC_ALL", "C.UTF-8")
    }
}
