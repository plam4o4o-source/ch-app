package org.chyavorec.domain.repository

import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.SelfDeclaredCard

/** Запис в кеша: текст + кога е записан. */
data class CachedPayload(val text: String, val savedAtMillis: Long)

/**
 * Прост кеш „ключ → текст“. Реализациите в Android слоя са:
 * - файлов кеш за публичните данни (новини, каталог, страници);
 * - шифрован (Android Keystore) кеш за читателските данни.
 */
interface PayloadCache {
    suspend fun read(key: String): CachedPayload?
    suspend fun write(key: String, text: String)
    suspend fun remove(key: String)
    suspend fun clear()
}

/** Съхранение на сесията — в Android: шифровано с ключ от Android Keystore. */
interface SessionStore {
    suspend fun load(): AuthSession?
    /** [persist] = false → само в паметта („не ме помни“). */
    suspend fun save(session: AuthSession, persist: Boolean)
    /**
     * Дали текущата сесия е запазена трайно (т.е. при вход е избрано „запомни ме“).
     * Използва се при подновяване на токена, за да се запази изборът на потребителя.
     */
    suspend fun isPersisted(): Boolean
    suspend fun clear()
}

/** Ръчно въведената от читателя карта — също шифрована. */
interface SelfCardStore {
    suspend fun load(): SelfDeclaredCard?
    suspend fun save(card: SelfDeclaredCard)
    suspend fun clear()
}

/** Хранилище в паметта — за тестове и за сесии „без запомняне“. */
class InMemoryPayloadCache(private val clock: () -> Long = System::currentTimeMillis) : PayloadCache {
    private val map = java.util.concurrent.ConcurrentHashMap<String, CachedPayload>()
    override suspend fun read(key: String) = map[key]
    override suspend fun write(key: String, text: String) { map[key] = CachedPayload(text, clock()) }
    override suspend fun remove(key: String) { map.remove(key) }
    override suspend fun clear() = map.clear()
}
