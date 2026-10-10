package org.chyavorec.core

import java.security.MessageDigest

/** Бързо hex кодиране и SHA-256 (без `String.format` за всеки байт). */
object Hashing {
    private val HEX = "0123456789abcdef".toCharArray()

    /** Hex на първите [count] байта (по подразбиране — всички). */
    fun hex(bytes: ByteArray, count: Int = bytes.size): String {
        val n = count.coerceIn(0, bytes.size)
        val out = CharArray(n * 2)
        for (i in 0 until n) {
            val v = bytes[i].toInt() and 0xff
            out[i * 2] = HEX[v ushr 4]
            out[i * 2 + 1] = HEX[v and 0x0f]
        }
        return String(out)
    }

    fun sha256(bytes: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256").digest(bytes)

    fun sha256Hex(bytes: ByteArray): String = hex(sha256(bytes))
}
