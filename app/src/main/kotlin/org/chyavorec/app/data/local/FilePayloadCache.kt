package org.chyavorec.app.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.core.Hashing
import org.chyavorec.domain.repository.CachedBytes
import org.chyavorec.domain.repository.CachedPayload
import org.chyavorec.domain.repository.PayloadCache
import org.chyavorec.domain.repository.PayloadVersions
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Файлов кеш: всеки ключ → отделен файл. Записът е атомарен (временен файл +
 * преименуване), така че прекъсване не оставя счупен кеш. Подходящ и за
 * големи стойности като katalog.json (SQLite CursorWindow е ограничен до 2 MB).
 *
 * С [cipher] съдържанието се шифрова — използва се за читателските данни.
 * Пътят на файла за ключ се смята веднъж (SHA-256), а [version] позволява на слоевете
 * в паметта да разберат кога записът е променен или изтрит.
 */
class FilePayloadCache(
    private val dir: File,
    private val cipher: BytesCipher? = null,
) : PayloadCache {

    private val files = ConcurrentHashMap<String, File>()
    private val versions = PayloadVersions()
    private val ext = if (cipher != null) ".bin" else ".json"

    private fun fileFor(key: String): File = files.getOrPut(key) {
        File(dir, Hashing.sha256Hex(key.toByteArray()) + ext)
    }

    override suspend fun read(key: String): CachedPayload? =
        readBytes(key)?.let { CachedPayload(String(it.bytes, Charsets.UTF_8), it.savedAtMillis) }

    override suspend fun readBytes(key: String): CachedBytes? = withContext(Dispatchers.IO) {
        val f = fileFor(key)
        if (!f.exists()) return@withContext null
        runCatching {
            val savedAt = f.lastModified()
            val bytes = f.readBytes()
            CachedBytes(cipher?.decrypt(bytes) ?: bytes, savedAt)
        }.getOrElse {
            // Повреден или нечетим (напр. след смяна на ключа) — изтриваме го.
            f.delete()
            versions.bump(key)
            null
        }
    }

    override suspend fun write(key: String, text: String) = writeBytes(key, text.toByteArray(Charsets.UTF_8))

    override suspend fun writeBytes(key: String, bytes: ByteArray) = withContext(Dispatchers.IO) {
        dir.mkdirs()
        val target = fileFor(key)
        val tmp = File(dir, target.name + ".tmp")
        tmp.writeBytes(cipher?.encrypt(bytes) ?: bytes)
        if (!tmp.renameTo(target)) {
            target.delete()
            tmp.renameTo(target)
        }
        versions.bump(key)
    }

    /** Непроменено съдържание: само новото време на файла (без повторен запис/шифроване). */
    override suspend fun touch(key: String): Boolean {
        val touched = withContext(Dispatchers.IO) {
            val f = fileFor(key)
            f.exists() && f.setLastModified(System.currentTimeMillis())
        }
        if (touched) {
            versions.bump(key)
            return true
        }
        // Файловата система не позволява смяна на времето — презаписваме съдържанието.
        val current = readBytes(key) ?: return false
        writeBytes(key, current.bytes)
        return true
    }

    override fun version(key: String): Long = versions.of(key)

    override suspend fun remove(key: String) = withContext(Dispatchers.IO) {
        fileFor(key).delete()
        versions.bump(key)
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        dir.listFiles()?.forEach { it.delete() }
        versions.clear()
    }

    suspend fun sizeBytes(): Long = withContext(Dispatchers.IO) { dir.listFiles()?.sumOf { it.length() } ?: 0L }
}
