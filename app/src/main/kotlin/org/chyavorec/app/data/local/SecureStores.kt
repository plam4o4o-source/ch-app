package org.chyavorec.app.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.domain.model.AuthSession
import org.chyavorec.domain.model.SelfDeclaredCard
import org.chyavorec.domain.repository.SelfCardStore
import org.chyavorec.domain.repository.SessionStore
import java.io.File

private val json = Json { ignoreUnknownKeys = true }

/**
 * Сесията (токени) — шифрована с ключ от Android Keystore в частната папка на
 * приложението. Паролата НЕ се пази никъде. При „не ме помни“ сесията живее
 * само в паметта до затваряне на приложението.
 */
class SecureSessionStore(private val file: File, private val cipher: BytesCipher) : SessionStore {
    @Volatile private var memory: AuthSession? = null

    override suspend fun load(): AuthSession? {
        memory?.let { return it }
        return withContext(Dispatchers.IO) {
            if (!file.exists()) return@withContext null
            runCatching { json.decodeFromString(AuthSession.serializer(), String(cipher.decrypt(file.readBytes()))) }
                .getOrElse { file.delete(); null }
                ?.also { memory = it }
        }
    }

    override suspend fun save(session: AuthSession, persist: Boolean) {
        memory = session
        withContext(Dispatchers.IO) {
            if (persist) {
                file.parentFile?.mkdirs()
                file.writeBytes(cipher.encrypt(json.encodeToString(AuthSession.serializer(), session).toByteArray()))
            } else file.delete()
        }
    }

    override suspend fun clear() {
        memory = null
        withContext(Dispatchers.IO) { file.delete() }
    }
}

class SecureSelfCardStore(private val file: File, private val cipher: BytesCipher) : SelfCardStore {
    override suspend fun load(): SelfDeclaredCard? = withContext(Dispatchers.IO) {
        if (!file.exists()) return@withContext null
        runCatching { json.decodeFromString(SelfDeclaredCard.serializer(), String(cipher.decrypt(file.readBytes()))) }
            .getOrElse { file.delete(); null }
    }

    override suspend fun save(card: SelfDeclaredCard) = withContext(Dispatchers.IO) {
        file.parentFile?.mkdirs()
        file.writeBytes(cipher.encrypt(json.encodeToString(SelfDeclaredCard.serializer(), card).toByteArray()))
    }

    override suspend fun clear() = withContext(Dispatchers.IO) { file.delete(); Unit }
}
