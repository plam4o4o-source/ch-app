package org.chyavorec.data.update

/**
 * Подписите на един APK, както ги връща PackageManager — като SHA-256 на сертификатите.
 *
 * - Android 9+ (`GET_SIGNING_CERTIFICATES`): [current] = `apkContentsSigners` (текущите
 *   подписващи), [history] = `signingCertificateHistory` (веригата на смяна на ключа,
 *   включително текущия) — празна при няколко подписващи;
 * - до Android 8.1 (`GET_SIGNATURES`): [current] = `signatures`, без история.
 */
data class ApkSigners(
    val current: Set<String>,
    val history: Set<String> = emptySet(),
    val multipleSigners: Boolean = current.size > 1,
)

/**
 * Дали изтегленото обновление е подписано със същия ключ като инсталираното приложение.
 * Решението е тук (без Android), за да се тества; AppUpdater само събира подписите.
 *
 * Отказва при всяко съмнение („fail closed“): неразчетени подписи (`null`), празен
 * списък или изключение при четенето — по-добре без обновяване, отколкото с чужд APK.
 */
object SigningCheck {
    enum class Result {
        /** Същият ключ (или законна смяна на ключа) — може да се инсталира. */
        MATCH,

        /** Различен ключ — не е нашето приложение. */
        MISMATCH,

        /** Подписите не можаха да се разчетат — не се инсталира. */
        UNREADABLE,
    }

    /**
     * [archive] — подписите на изтегления APK, [installed] — на инсталираното приложение;
     * `null` — PackageManager не ги е върнал (или е хвърлил изключение).
     *
     * - няколко подписващи (от която и да е страна) → наборите трябва да съвпадат изцяло
     *   (така проверява и Android — смяна на ключа там не е възможна);
     * - един подписващ → същият сертификат, или новият APK е с нов ключ, чиято история
     *   ([ApkSigners.history]) съдържа инсталирания ключ (смяна на ключа, APK Signature
     *   Scheme v3). Обратното — връщане към стар ключ — не се приема.
     */
    fun decide(archive: ApkSigners?, installed: ApkSigners?): Result {
        if (archive == null || installed == null) return Result.UNREADABLE
        if (archive.current.isEmpty() || installed.current.isEmpty()) return Result.UNREADABLE
        if (archive.current.any { it.isBlank() } || installed.current.any { it.isBlank() }) return Result.UNREADABLE
        if (archive.multipleSigners || installed.multipleSigners) {
            return if (archive.current == installed.current) Result.MATCH else Result.MISMATCH
        }
        if (archive.current == installed.current) return Result.MATCH
        // Ротиран ключ: инсталираният (текущ) сертификат е в историята на новия APK.
        return if (installed.current.all { it in archive.history }) Result.MATCH else Result.MISMATCH
    }
}
