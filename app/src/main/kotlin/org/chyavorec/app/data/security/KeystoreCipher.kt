package org.chyavorec.app.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * AES-256-GCM с ключ, генериран и пазен в Android Keystore. Ключът не може да
 * бъде изваден от устройството; шифрованите данни са безполезни другаде.
 * Формат: [1 байт дължина на IV][IV][шифротекст+tag].
 */
class KeystoreCipher(private val alias: String = "chyavorec_secure_store_v1") {

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    private fun key(): SecretKey {
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
        val ivLen = data[0].toInt()
        require(ivLen in 12..16 && data.size > 1 + ivLen) { "corrupt" }
        val iv = data.copyOfRange(1, 1 + ivLen)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, iv))
        return cipher.doFinal(data, 1 + ivLen, data.size - 1 - ivLen)
    }

    /** При logout/изтриване на данни — ключът се унищожава. */
    fun destroyKey() {
        runCatching { keyStore.deleteEntry(alias) }
    }

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
    }
}

/** Абстракция за тестове (Robolectric няма Android Keystore). */
interface BytesCipher {
    fun encrypt(plain: ByteArray): ByteArray
    fun decrypt(data: ByteArray): ByteArray
}

class KeystoreBytesCipher(private val cipher: KeystoreCipher = KeystoreCipher()) : BytesCipher {
    override fun encrypt(plain: ByteArray) = cipher.encrypt(plain)
    override fun decrypt(data: ByteArray) = cipher.decrypt(data)
}
