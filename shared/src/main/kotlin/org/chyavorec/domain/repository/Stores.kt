package org.chyavorec.domain.repository

import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.SelfDeclaredCard

/** Запис в кеша: текст + кога е записан. */
data class CachedPayload(val text: String, val savedAtMillis: Long)

/** Запис в кеша като сурови байтове (UTF-8) — за големи стойности като katalog.json. */
class CachedBytes(val bytes: ByteArray, val savedAtMillis: Long)

/**
 * Прост кеш „ключ → текст“. Реализациите в Android слоя са:
 * - файлов кеш за публичните данни (новини, каталог, страници);
 * - шифрован (Android Keystore) кеш за читателските данни.
 *
 * Методите с реализация по подразбиране са оптимизации — една реализация трябва
 * да предостави само четирите основни.
 */
interface PayloadCache {
    suspend fun read(key: String): CachedPayload?
    suspend fun write(key: String, text: String)
    suspend fun remove(key: String)
    suspend fun clear()

    /** Като [read], но без декодиране до текст (големите файлове не се държат двойно в паметта). */
    suspend fun readBytes(key: String): CachedBytes? =
        read(key)?.let { CachedBytes(it.text.toByteArray(Charsets.UTF_8), it.savedAtMillis) }

    /** Като [write], но директно от байтове (UTF-8). */
    suspend fun writeBytes(key: String, bytes: ByteArray) = write(key, String(bytes, Charsets.UTF_8))

    /**
     * Отбелязва записа като току-що запазен, без да го презаписва (съдържанието е
     * същото). `false`, ако ключът липсва.
     */
    suspend fun touch(key: String): Boolean {
        val p = read(key) ?: return false
        write(key, p.text)
        return true
    }

    /**
     * Брояч на промените по ключа: расте при всеки [write]/[remove]/[touch] на ключа и
     * при [clear]. По него слоевете в паметта (CachedResource) разбират, че копието им е
     * остаряло. `null` = реализацията не го поддържа (тогава паметта не се ползва).
     */
    fun version(key: String): Long? = null
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

/**
 * Броячи на промените за [PayloadCache.version]: монотонни в рамките на процеса;
 * [clear] прави всички ключове „променени“.
 */
class PayloadVersions {
    private val seq = java.util.concurrent.atomic.AtomicLong()
    private val perKey = java.util.concurrent.ConcurrentHashMap<String, Long>()
    @Volatile private var clearedAt = 0L

    fun bump(key: String) { perKey[key] = seq.incrementAndGet() }
    fun clear() { clearedAt = seq.incrementAndGet(); perKey.clear() }
    fun of(key: String): Long = maxOf(perKey[key] ?: 0L, clearedAt)
}

/** Хранилище в паметта — за тестове и за сесии „без запомняне“. */
class InMemoryPayloadCache(private val clock: () -> Long = System::currentTimeMillis) : PayloadCache {
    private val map = java.util.concurrent.ConcurrentHashMap<String, CachedPayload>()
    private val versions = PayloadVersions()
    /** Брой записи (за тестове: дали непромененото съдържание се презаписва). */
    @Volatile var writes = 0
        private set
    override suspend fun read(key: String) = map[key]
    override suspend fun write(key: String, text: String) { map[key] = CachedPayload(text, clock()); writes++; versions.bump(key) }
    override suspend fun remove(key: String) { map.remove(key); versions.bump(key) }
    override suspend fun clear() { map.clear(); versions.clear() }
    override suspend fun touch(key: String): Boolean {
        val p = map[key] ?: return false
        map[key] = p.copy(savedAtMillis = clock())
        versions.bump(key)
        return true
    }
    override fun version(key: String): Long = versions.of(key)
}
