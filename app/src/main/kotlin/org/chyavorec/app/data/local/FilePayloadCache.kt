package org.chyavorec.app.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.domain.repository.CachedPayload
import org.chyavorec.domain.repository.PayloadCache
import java.io.File
import java.security.MessageDigest

/**
 * Файлов кеш: всеки ключ → отделен файл. Записът е атомарен (временен файл +
 * преименуване), така че прекъсване не оставя счупен кеш. Подходящ и за
 * големи стойности като katalog.json (SQLite CursorWindow е ограничен до 2 MB).
 *
 * С [cipher] съдържанието се шифрова — използва се за читателските данни.
 */
class FilePayloadCache(
    private val dir: File,
    private val cipher: BytesCipher? = null,
) : PayloadCache {

    private fun fileFor(key: String): File {
        val digest = MessageDigest.getInstance("SHA-256").digest(key.toByteArray())
        return File(dir, digest.joinToString("") { "%02x".format(it) } + if (cipher != null) ".bin" else ".json")
    }

    override suspend fun read(key: String): CachedPayload? = withContext(Dispatchers.IO) {
        val f = fileFor(key)
        if (!f.exists()) return@withContext null
        runCatching {
            val bytes = f.readBytes()
            val plain = cipher?.decrypt(bytes) ?: bytes
            CachedPayload(String(plain, Charsets.UTF_8), f.lastModified())
        }.getOrElse {
            // Повреден или нечетим (напр. след смяна на ключа) — изтриваме го.
            f.delete()
            null
        }
    }

    override suspend fun write(key: String, text: String) = withContext(Dispatchers.IO) {
        dir.mkdirs()
        val target = fileFor(key)
        val tmp = File(dir, target.name + ".tmp")
        val bytes = text.toByteArray(Charsets.UTF_8)
        tmp.writeBytes(cipher?.encrypt(bytes) ?: bytes)
        if (!tmp.renameTo(target)) {
            target.delete()
            tmp.renameTo(target)
        }
        Unit
    }

    override suspend fun remove(key: String) = withContext(Dispatchers.IO) { fileFor(key).delete(); Unit }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        dir.listFiles()?.forEach { it.delete() }
        Unit
    }

    suspend fun sizeBytes(): Long = withContext(Dispatchers.IO) { dir.listFiles()?.sumOf { it.length() } ?: 0L }
}
