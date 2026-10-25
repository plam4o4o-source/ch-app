package org.chyavorec.app.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.app.data.security.isUnreadableForever
import org.chyavorec.core.logFailure
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
class SecureSessionStore(
    private val file: File,
    private val cipher: BytesCipher,
    /** Изчакването преди втория опит при временна грешка (тестовете подават 0). */
    private val retryDelayMs: Long = 300,
) : SessionStore {
    @Volatile private var memory: AuthSession? = null
    /** Последното четене е спряло на временна грешка (файлът е запазен). */
    @Volatile private var transientFailure = false

    override suspend fun load(): AuthSession? {
        memory?.let { return it }
        return withContext(Dispatchers.IO) {
            if (!file.exists()) {
                transientFailure = false
                return@withContext null
            }
            // Нечетима завинаги сесия (сменен ключ, повреден файл) = изход; временна грешка на
            // Keystore — още един опит, после без сесия сега, но файлът остава (без изход от
            // профила — при следващото четене се опитва отново). Причината — в локалния дневник.
            var result = read()
            if (result.exceptionOrNull()?.let { !isUnreadableForever(it) } == true) {
                delay(retryDelayMs)
                result = read()
            }
            result.logFailure("auth.session-store")
            val error = result.exceptionOrNull()
            transientFailure = error != null && !isUnreadableForever(error)
            if (error != null && !transientFailure) file.delete()
            result.getOrNull()?.also { memory = it }
        }
    }

    private fun read(): Result<AuthSession> =
        runCatching { json.decodeFromString(AuthSession.serializer(), String(cipher.decrypt(file.readBytes()))) }

    override suspend fun unreadableNow(): Boolean = transientFailure && withContext(Dispatchers.IO) { file.exists() }

    override suspend fun save(session: AuthSession, persist: Boolean) {
        memory = session
        transientFailure = false
        withContext(Dispatchers.IO) {
            if (persist) {
                file.parentFile?.mkdirs()
                file.writeBytes(cipher.encrypt(json.encodeToString(AuthSession.serializer(), session).toByteArray()))
            } else file.delete()
        }
    }

    /** Файлът съществува само ако при вход е избрано „запомни ме“. */
    override suspend fun isPersisted(): Boolean = withContext(Dispatchers.IO) { file.exists() }

    override suspend fun clear() {
        memory = null
        transientFailure = false
        withContext(Dispatchers.IO) { file.delete() }
    }
}

class SecureSelfCardStore(private val file: File, private val cipher: BytesCipher) : SelfCardStore {
    override suspend fun load(): SelfDeclaredCard? = withContext(Dispatchers.IO) {
        if (!file.exists()) return@withContext null
        runCatching { json.decodeFromString(SelfDeclaredCard.serializer(), String(cipher.decrypt(file.readBytes()))) }
            .logFailure("card-store")
            .getOrElse { e -> if (isUnreadableForever(e)) file.delete(); null }
    }

    override suspend fun save(card: SelfDeclaredCard) = withContext(Dispatchers.IO) {
        file.parentFile?.mkdirs()
        file.writeBytes(cipher.encrypt(json.encodeToString(SelfDeclaredCard.serializer(), card).toByteArray()))
    }

    override suspend fun clear() = withContext(Dispatchers.IO) { file.delete(); Unit }
}
