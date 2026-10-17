package org.chyavorec.app.di

/**
 * Приложението, което държи [AppContainer] (ChitalishteApp). Фоновите задачи взимат
 * зависимостите оттук — тестовете (WorkManager + Robolectric) подават собствено
 * тестово приложение с контейнер към MockWebServer.
 */
interface AppContainerOwner {
    val appContainer: AppContainer
}
