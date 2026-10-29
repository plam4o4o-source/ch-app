package org.chyavorec.app.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.UnrecoverableKeyException
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * AES-256-GCM с ключ, генериран и пазен в Android Keystore. Ключът не може да
 * бъде изваден от устройството; шифрованите данни са безполезни другаде.
 * Формат: [1 байт дължина на IV][IV][шифротекст+tag].
 *
 * Ключът НЕ се унищожава при изход: с него е шифрована и ръчно въведената карта
 * (secure/card.bin), която остава след изход от профила. Изтрива се заедно с данните
 * на приложението (при деинсталиране или „Изчисти данните“ от системата).
 */
class KeystoreCipher(private val alias: String = "chyavorec_secure_store_v1") {

    /**
     * Keystore-ът се отваря едва при първото шифроване/дешифроване — създаването на обекта
     * (напр. с хранилищата на сесията при фоново събуждане) не стига до системната услуга.
     */
    private val keyStore: KeyStore by lazy { KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) } }

    /** Ключът се взима от Keystore веднъж (всяко четене е IPC към системната услуга). */
    @Volatile private var cachedKey: SecretKey? = null

    private fun key(): SecretKey {
        cachedKey?.let { return it }
        return synchronized(this) { cachedKey ?: loadOrCreateKey().also { cachedKey = it } }
    }

    private fun loadOrCreateKey(): SecretKey {
        (keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        generator.init(
            KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setRandomizedEncryptionRequired(true)
                .build(),
        )
        return generator.generateKey()
    }

    fun encrypt(plain: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key())
        val iv = cipher.iv
        val out = cipher.doFinal(plain)
        return byteArrayOf(iv.size.toByte()) + iv + out
    }

    fun decrypt(data: ByteArray): ByteArray {
        require(data.isNotEmpty()) { "corrupt" }
        val ivLen = data[0].toInt()
        // AndroidKeyStore дава (и приема) само 12-байтов IV; друга дължина или шифротекст, по-къс
        // от tag-а, е повреден файл — нечетим завинаги ([isUnreadableForever]), а не временна грешка.
        require(ivLen == GCM_IV_BYTES && data.size >= 1 + ivLen + GCM_TAG_BYTES) { "corrupt" }
        val iv = data.copyOfRange(1, 1 + ivLen)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(GCM_TAG_BYTES * 8, iv))
        return cipher.doFinal(data, 1 + ivLen, data.size - 1 - ivLen)
    }

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_IV_BYTES = 12
        const val GCM_TAG_BYTES = 16
    }
}

/**
 * Шифрованите данни никога няма да се прочетат: грешен или сменен ключ (AEAD tag), ключ,
 * обезсилен или изгубен от Keystore, повреден файл (вкл. IV, различен от 12 байта) или JSON
 * (IllegalArgumentException, вкл. SerializationException). Всичко друго (напр. временно
 * недостъпна услуга на Keystore, ProviderException, KeyStoreException, а и обвитите от нея
 * InvalidKeyException/IllegalBlockSizeException) може да мине при следващия опит — файлът
 * остава; сесия, нечетима няколко поредни пускания на интерфейса (и поне 3 дни), се изтрива
 * (AuthRepository.MAX_UNREADABLE_LAUNCHES, MIN_UNREADABLE_SPAN_MS).
 */
fun isUnreadableForever(e: Throwable): Boolean =
    e is AEADBadTagException || e is KeyPermanentlyInvalidatedException || e is UnrecoverableKeyException ||
        e is IllegalArgumentException

/** Абстракция за тестове (Robolectric няма Android Keystore). */
interface BytesCipher {
    fun encrypt(plain: ByteArray): ByteArray
    fun decrypt(data: ByteArray): ByteArray
}

class KeystoreBytesCipher(private val cipher: KeystoreCipher = KeystoreCipher()) : BytesCipher {
    override fun encrypt(plain: ByteArray) = cipher.encrypt(plain)
    override fun decrypt(data: ByteArray) = cipher.decrypt(data)
}
