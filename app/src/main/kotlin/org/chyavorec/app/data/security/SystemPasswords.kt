package org.chyavorec.app.data.security

import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Номерът на картата и ПИН-ът от системния мениджър на пароли. ПИН-ът не влиза в логове (toString). */
class SavedLogin(val cardNumber: String, val pin: String) {
    override fun toString() = "SavedLogin(card=***, pin=***)"
}

/**
 * Системният мениджър на пароли (Credential Manager): запазване след вход и попълване с
 * едно докосване. Картата и ПИН-ът се пазят от него, а не от приложението. [activity] —
 * контекстът на екрана (системата показва своя прозорец над него).
 */
interface SystemPasswords {
    /** Предлага запазване; `true` — запазено. Отказ, липсващ мениджър или грешка — тихо, `false`. */
    suspend fun offerSave(activity: Context, login: SavedLogin): Boolean

    /** Запазеният вход за приложението или `null` (няма, отказ, липсващ мениджър). */
    suspend fun savedLogin(activity: Context): SavedLogin?
}

/**
 * Чрез androidx.credentials (под Android 14 — през Google Play услугите). Изборът на доставчик
 * (PackageManager, рефлексия) е извън главната нишка; самите заявки са асинхронни.
 */
class CredentialManagerPasswords(context: Context) : SystemPasswords {
    private val manager by lazy { CredentialManager.create(context.applicationContext) }

    override suspend fun offerSave(activity: Context, login: SavedLogin): Boolean = withContext(Dispatchers.Default) {
        try {
            manager.createCredential(activity, CreatePasswordRequest(id = login.cardNumber, password = login.pin))
            true
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Отказ („Не сега“), няма мениджър на пароли или друга грешка — нищо не се показва.
            false
        }
    }

    override suspend fun savedLogin(activity: Context): SavedLogin? = withContext(Dispatchers.Default) {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(GetPasswordOption())
            // Без запазен вход — без прозорец (NoCredentialException), а не празен списък.
            .setPreferImmediatelyAvailableCredentials(true)
            .build()
        try {
            (manager.getCredential(activity, request).credential as? PasswordCredential)
                ?.let { SavedLogin(it.id, it.password) }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Отказ, няма запазен вход или мениджър (CreateCredentialException/GetCredentialException
            // и др.) — входът е както досега, ръчно.
            null
        }
    }
}
