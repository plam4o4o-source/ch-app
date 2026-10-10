package org.chyavorec.app.update

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.chyavorec.app.AppConfig
import org.chyavorec.app.data.local.SettingsStore
import org.chyavorec.core.AppClock
import org.chyavorec.core.Outcome
import org.chyavorec.data.http.HttpFetcher
import org.chyavorec.data.update.Checksums
import org.chyavorec.data.update.UpdateChecker
import org.chyavorec.data.update.UpdateInfo
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.DigestOutputStream
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

/** Състояние на обновяването — показва се в диалога и в настройките. */
sealed interface UpdateState {
    data object Idle : UpdateState
    data object Checking : UpdateState
    data object UpToDate : UpdateState
    data class Available(val info: UpdateInfo) : UpdateState
    /** [progress] е от 0 до 1, или `null`, ако размерът не е известен. */
    data class Downloading(val info: UpdateInfo, val progress: Float?) : UpdateState
    data class Ready(val info: UpdateInfo) : UpdateState
    /** Потребителят трябва да разреши „Инсталиране на неизвестни приложения“ за това приложение. */
    data class NeedsPermission(val info: UpdateInfo) : UpdateState
    data class Installing(val info: UpdateInfo) : UpdateState
    data class Failed(val reason: UpdateFailure, val info: UpdateInfo?) : UpdateState
}

enum class UpdateFailure { CHECK, DOWNLOAD, CHECKSUM, SIGNATURE, INSTALL }

/**
 * Самообновяване на APK, инсталиран извън Google Play (от GitHub Releases).
 *
 * 1. `update.json` от последното release → по-висок versionCode ли е;
 * 2. изтегляне на APK файла в личната папка на приложението;
 * 3. проверка на SHA-256 от манифеста **и** че APK-то е за същия пакет,
 *    със същата версия и подписано със същия ключ като инсталираното;
 * 4. инсталиране през [PackageInstaller]. На Android 12+ системата може да
 *    обнови без въпрос, ако приложението само е инсталирало предишната версия;
 *    иначе показва стандартния системен диалог за потвърждение.
 *
 * В Google Play build-а ([AppConfig.selfUpdate] = false) нищо от това не се изпълнява.
 */
class AppUpdater(
    private val context: Context,
    okHttp: OkHttpClient,
    http: HttpFetcher,
    private val config: AppConfig,
    private val settings: SettingsStore,
    private val clock: AppClock,
) {
    val enabled: Boolean = config.selfUpdate && config.updateManifestUrl.isNotBlank()
    val installedVersionName: String get() = config.versionName

    private val checker = UpdateChecker(http, config.updateManifestUrl)
    /**
     * Изтеглянето може да трае повече от общия timeout на споделения клиент.
     * Без HTTP кеш: APK-то (десетки MB) иначе би изместило всичко друго от него.
     */
    private val downloadClient by lazy {
        okHttp.newBuilder().cache(null).callTimeout(0, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build()
    }
    private val dir get() = File(context.noBackupFilesDir, "updates")
    /** Грешка в обновяването никога не бива да срине приложението. */
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> })
    private val mutex = Mutex()

    private val _state = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val state: StateFlow<UpdateState> = _state.asStateFlow()

    /** Молба към UI да покаже диалога (напр. след докосване на известие). */
    private val _prompt = MutableStateFlow(false)
    val prompt: StateFlow<Boolean> = _prompt.asStateFlow()

    /** Проверката, пусната от UI (диалог/настройки) — за да не тръгнат две едновременно. */
    @Volatile private var uiCheck: Job? = null

    /**
     * Показва диалога. Ако още няма данни за версията (напр. докоснато известие в
     * нов процес, а проверката при стартиране е пропусната, защото е правена
     * скоро) — те се зареждат, за да има какво да покаже диалогът.
     */
    fun showPrompt() {
        if (!enabled) return
        _prompt.value = true
        if (_state.value == UpdateState.Idle && uiCheck?.isActive != true) {
            uiCheck = scope.launch { check(userInitiated = true) }
        }
    }
    fun hidePrompt() { _prompt.value = false }

    /**
     * При стартиране на приложението: тиха проверка (най-много веднъж на
     * [LAUNCH_CHECK_INTERVAL_MS], заедно с фоновите); по Wi-Fi новата версия се
     * изтегля веднага. Диалогът се показва, освен ако потребителят е отложил
     * същата версия („По-късно“ — за [SNOOZE_MS]). Ръчната проверка ([checkNow])
     * не е ограничена.
     */
    fun checkOnLaunch() {
        if (!enabled) return
        scope.launch {
            cleanup()
            if (!settings.current().autoUpdate) return@launch
            val sinceLast = clock.now().toEpochMilli() - settings.lastUpdateCheck.first()
            if (sinceLast in 0L until LAUNCH_CHECK_INTERVAL_MS) return@launch
            val info = check(userInitiated = false) ?: return@launch
            if (isUnmetered()) download(info)
            val s = _state.value
            if ((s is UpdateState.Available || s is UpdateState.Ready) &&
                !settings.updateSnoozed(info.versionCode, clock.now().toEpochMilli())
            ) {
                _prompt.value = true
            }
        }
    }

    /** „По-късно“ в диалога. */
    fun snooze() {
        _prompt.value = false
        val info = currentInfo() ?: return
        scope.launch { settings.snoozeUpdate(info.versionCode, clock.now().toEpochMilli() + SNOOZE_MS) }
    }

    /** „Провери за нова версия“ от настройките. */
    fun checkNow() {
        if (!enabled) return
        uiCheck = scope.launch {
            val info = check(userInitiated = true)
            if (info != null) _prompt.value = true
        }
    }

    fun startDownloadAndInstall() {
        val info = currentInfo() ?: return
        scope.launch {
            if (download(info) != null) install(background = false)
        }
    }

    fun startInstall() {
        scope.launch { install(background = false) }
    }

    /** След връщане от системните настройки за „неизвестни приложения“. */
    fun onResume() {
        val s = _state.value
        if (s is UpdateState.NeedsPermission && canRequestInstalls()) startInstall()
    }

    /**
     * @return новата версия или `null` (няма нова, грешка или изключено).
     * При [userInitiated] = false грешките не се показват.
     */
    suspend fun check(userInitiated: Boolean): UpdateInfo? {
        if (!enabled) return null
        val busy = _state.value
        if (busy is UpdateState.Downloading || busy is UpdateState.Installing) return (busy as? UpdateState.Downloading)?.info
        _state.value = UpdateState.Checking
        val result = checker.check(config.versionCode, Build.VERSION.SDK_INT)
        settings.setLastUpdateCheck(clock.now().toEpochMilli())
        return when (result) {
            is Outcome.Failure -> {
                _state.value = if (userInitiated) UpdateState.Failed(UpdateFailure.CHECK, null) else UpdateState.Idle
                null
            }
            is Outcome.Success -> {
                val info = result.value
                if (info == null) {
                    _state.value = UpdateState.UpToDate
                    cleanup()
                } else {
                    _state.value = if (verifiedFile(info) != null) UpdateState.Ready(info) else UpdateState.Available(info)
                }
                info
            }
        }
    }

    /**
     * Изтегля и проверява APK файла. Повторно извикване за същата версия ползва вече
     * изтегления файл; прекъснато изтегляне продължава оттам, докъдето е стигнало
     * (HTTP Range), а контролната сума се проверява за целия файл.
     */
    suspend fun download(info: UpdateInfo): File? = mutex.withLock {
        withContext(Dispatchers.IO) {
            verifiedFile(info)?.let { _state.value = UpdateState.Ready(info); return@withContext it }
            _state.value = UpdateState.Downloading(info, if (info.sizeBytes != null) 0f else null)
            dir.mkdirs()
            val part = File(dir, "update-${info.versionCode}.apk.part")
            // Валидаторът (ETag/Last-Modified) на частично изтегления файл — за If-Range.
            val validator = File(dir, "update-${info.versionCode}.apk.part.tag")
            val discard = {
                part.delete()
                validator.delete()
            }
            val target = apkFile(info)
            val digest = try {
                fetch(info, part, validator)
            } catch (e: Exception) {
                // Прекъсване (задачата е спряна) или грешка в мрежата: наличната част остава за продължаване.
                if (e is CancellationException) throw e
                if (e is TooLargeException) discard()
                _state.value = UpdateState.Failed(UpdateFailure.DOWNLOAD, info)
                return@withContext null
            }
            validator.delete()
            val sha = digest.digest().joinToString("") { "%02x".format(it) }
            if (sha != info.sha256) {
                discard()
                _state.value = UpdateState.Failed(UpdateFailure.CHECKSUM, info)
                return@withContext null
            }
            when (verifyApk(part, info)) {
                ApkCheck.OK -> Unit
                ApkCheck.WRONG_SIGNATURE -> {
                    discard()
                    _state.value = UpdateState.Failed(UpdateFailure.SIGNATURE, info)
                    return@withContext null
                }
                ApkCheck.INVALID -> {
                    discard()
                    _state.value = UpdateState.Failed(UpdateFailure.CHECKSUM, info)
                    return@withContext null
                }
            }
            target.delete()
            if (!part.renameTo(target)) {
                discard()
                _state.value = UpdateState.Failed(UpdateFailure.DOWNLOAD, info)
                return@withContext null
            }
            // Сумата току-що е сметната — файлът не се хешира повторно (виж [sha256Of]).
            verified = VerifiedApk(target.absolutePath, target.length(), target.lastModified(), sha)
            _state.value = UpdateState.Ready(info)
            target
        }
    }

    /** Изтегля [part] докрай и връща SHA-256 на целия файл (заедно с вече наличната част). */
    private fun fetch(info: UpdateInfo, part: File, validator: File): MessageDigest {
        fetchOnce(info, part, validator)?.let { return it }
        // Наличната част не пасва на файла на сървъра (416) — отначало.
        part.delete()
        validator.delete()
        return fetchOnce(info, part, validator) ?: throw IOException("range not satisfiable")
    }

    /** @return сумата или `null`, ако сървърът отхвърли продължаването (416). */
    private fun fetchOnce(info: UpdateInfo, part: File, validator: File): MessageDigest? {
        val digest = MessageDigest.getInstance("SHA-256")
        var existing = if (part.isFile) part.length() else 0L
        val expected = info.sizeBytes
        if (existing > MAX_APK_BYTES || (expected != null && existing > expected)) {
            part.delete()
            validator.delete()
            existing = 0L
        }
        // Файлът е изтеглен целият, но не е бил проверен (напр. процесът е спрян) — без заявка.
        if (expected != null && existing > 0L && existing == expected) {
            digestFile(part, digest)
            return digest
        }
        val builder = Request.Builder().url(info.apkUrl).header("User-Agent", "ChitalishteYavorec-Android/${config.versionName}")
        if (existing > 0L) {
            builder.header("Range", "bytes=$existing-")
            // If-Range: ако файлът на сървъра е сменен, идва целият (200) вместо чуждо парче.
            runCatching { validator.readText().trim() }.getOrNull()?.takeIf { it.isNotEmpty() }?.let { builder.header("If-Range", it) }
        }
        downloadClient.newCall(builder.build()).execute().use { response ->
            if (response.code == 416 && existing > 0L) return null
            if (!response.isSuccessful) throw IOException("HTTP ${response.code}")
            val body = response.body
            val resume = existing > 0L && response.code == 206
            if (resume) {
                // „bytes <начало>-<край>/<общо>“ — парчето трябва да започва точно след наличното.
                val range = response.header("Content-Range").orEmpty()
                if (!range.startsWith("bytes $existing-")) throw IOException("bad Content-Range")
                digestFile(part, digest)
            } else {
                if (response.code == 206) throw IOException("unexpected partial content")
                // 200: сървърът праща целия файл (не поддържа Range или файлът е сменен) — отначало.
                existing = 0L
                val tag = response.header("ETag")?.takeUnless { it.startsWith("W/") } ?: response.header("Last-Modified")
                if (tag.isNullOrBlank()) validator.delete() else runCatching { validator.writeText(tag) }
            }
            val total = body.contentLength().takeIf { it > 0 }?.let { it + existing } ?: expected
            if (total != null && total > MAX_APK_BYTES) throw TooLargeException()
            body.byteStream().use { input ->
                DigestOutputStream(FileOutputStream(part, resume), digest).use { out ->
                    val buf = ByteArray(64 * 1024)
                    var read = existing
                    var lastReported = -1
                    while (true) {
                        val n = input.read(buf)
                        if (n < 0) break
                        out.write(buf, 0, n)
                        read += n
                        if (read > MAX_APK_BYTES) throw TooLargeException()
                        if (total != null) {
                            val pct = (read * 100 / total).toInt()
                            if (pct != lastReported) {
                                lastReported = pct
                                _state.value = UpdateState.Downloading(info, (read.toFloat() / total).coerceIn(0f, 1f))
                            }
                        }
                    }
                }
            }
        }
        return digest
    }

    private fun digestFile(file: File, digest: MessageDigest) {
        file.inputStream().use { input ->
            val buf = ByteArray(64 * 1024)
            while (true) {
                val n = input.read(buf)
                if (n < 0) break
                digest.update(buf, 0, n)
            }
        }
    }

    private class TooLargeException : IOException("too large")

    /**
     * Инсталиране на изтеглената версия. При [background] = true (фонова задача)
     * се допуска само тихо обновяване; ако системата поиска потвърждение,
     * вместо диалог се показва известие.
     */
    suspend fun install(background: Boolean): Boolean = withContext(Dispatchers.IO) {
        val info = currentInfo() ?: return@withContext false
        val file = verifiedFile(info) ?: run {
            _state.value = UpdateState.Available(info)
            return@withContext false
        }
        if (!canRequestInstalls()) {
            _state.value = UpdateState.NeedsPermission(info)
            return@withContext false
        }
        _state.value = UpdateState.Installing(info)
        try {
            val installer = context.packageManager.packageInstaller
            val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL).apply {
                setAppPackageName(context.packageName)
                setSize(file.length())
                setInstallReason(PackageManager.INSTALL_REASON_USER)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED)
                }
            }
            val sessionId = installer.createSession(params)
            installer.openSession(sessionId).use { session ->
                session.openWrite("base.apk", 0, file.length()).use { out ->
                    file.inputStream().use { it.copyTo(out) }
                    session.fsync(out)
                }
                val intent = Intent(context, UpdateInstallReceiver::class.java)
                    .setPackage(context.packageName)
                    .putExtra(UpdateInstallReceiver.EXTRA_BACKGROUND, background)
                // Системата добавя статуса като extra, затова PendingIntent-ът трябва да е mutable (изричен Intent).
                val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)
                val pending = PendingIntent.getBroadcast(context, sessionId, intent, flags)
                session.commit(pending.intentSender)
            }
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            _state.value = UpdateState.Failed(UpdateFailure.INSTALL, info)
            false
        }
    }

    /** Отговор от системата след инсталиране (виж [UpdateInstallReceiver]). */
    fun onInstallResult(status: Int) {
        val info = currentInfo() ?: return
        _state.value = when (status) {
            PackageInstaller.STATUS_SUCCESS -> UpdateState.Idle
            // Потребителят отказа в системния диалог — не е грешка, файлът остава готов.
            PackageInstaller.STATUS_FAILURE_ABORTED -> UpdateState.Ready(info)
            // Различен подпис от инсталираното приложение.
            PackageInstaller.STATUS_FAILURE_CONFLICT, PackageInstaller.STATUS_FAILURE_INCOMPATIBLE -> UpdateState.Failed(UpdateFailure.SIGNATURE, info)
            else -> UpdateState.Failed(UpdateFailure.INSTALL, info)
        }
    }

    /** Фоновата задача остави инсталирането за по-късно (нужно е потвърждение). */
    fun onNeedsConfirmation() {
        currentInfo()?.let { _state.value = UpdateState.Ready(it) }
    }

    fun dismissError() {
        if (_state.value is UpdateState.Failed || _state.value is UpdateState.UpToDate) _state.value = UpdateState.Idle
    }

    fun currentInfo(): UpdateInfo? = when (val s = _state.value) {
        is UpdateState.Available -> s.info
        is UpdateState.Downloading -> s.info
        is UpdateState.Ready -> s.info
        is UpdateState.NeedsPermission -> s.info
        is UpdateState.Installing -> s.info
        is UpdateState.Failed -> s.info
        else -> null
    }

    fun canRequestInstalls(): Boolean = context.packageManager.canRequestPackageInstalls()

    /** Android 12+ обновява без въпрос само ако това приложение е инсталирало текущата версия. */
    fun canUpdateSilently(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return false
        val installer = runCatching { context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName }.getOrNull()
        return installer == context.packageName && canRequestInstalls()
    }

    suspend fun appInForeground(): Boolean = withContext(Dispatchers.Main) {
        ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }

    fun isUnmetered(): Boolean {
        val cm = context.getSystemService(ConnectivityManager::class.java) ?: return false
        return !cm.isActiveNetworkMetered
    }

    private fun apkFile(info: UpdateInfo) = File(dir, "update-${info.versionCode}.apk")

    private fun verifiedFile(info: UpdateInfo): File? {
        val f = apkFile(info)
        if (!f.isFile) return null
        return if (sha256Of(f) == info.sha256) f else { f.delete(); null }
    }

    /** Проверената сума на изтегления файл — за същия път, размер и време на промяна не се смята наново. */
    private data class VerifiedApk(val path: String, val length: Long, val modified: Long, val sha256: String)
    @Volatile private var verified: VerifiedApk? = null

    /** SHA-256 на файла; APK-то (десетки MB) се хешира веднъж, а не при всяка проверка/инсталиране. */
    private fun sha256Of(f: File): String? {
        val length = f.length()
        val modified = f.lastModified()
        verified?.let { v -> if (v.path == f.absolutePath && v.length == length && v.modified == modified) return v.sha256 }
        val sha = runCatching { Checksums.sha256(f) }.getOrNull() ?: return null
        verified = VerifiedApk(f.absolutePath, length, modified, sha)
        return sha
    }

    /** Изтрива изтеглени файлове за вече инсталирани (или по-стари) версии. */
    private fun cleanup() {
        dir.listFiles()?.forEach { f ->
            val code = f.name.removePrefix("update-").substringBefore('.').toIntOrNull()
            if (code == null || code <= config.versionCode) f.delete()
        }
    }

    private enum class ApkCheck { OK, WRONG_SIGNATURE, INVALID }

    private fun verifyApk(file: File, info: UpdateInfo): ApkCheck {
        val pm = context.packageManager
        val archive = packageInfo { flags -> pm.getPackageArchiveInfo(file.absolutePath, flags) } ?: return ApkCheck.INVALID
        if (archive.packageName != context.packageName) return ApkCheck.INVALID
        if (PackageInfoCompat.getLongVersionCode(archive) != info.versionCode.toLong()) return ApkCheck.INVALID
        val installed = packageInfo { flags -> pm.getPackageInfo(context.packageName, flags) } ?: return ApkCheck.OK
        val a = certificates(archive)
        val b = certificates(installed)
        // Ако системата не върне подписите, последната дума е на инсталатора (той също сравнява ключа).
        if (a.isEmpty() || b.isEmpty()) return ApkCheck.OK
        return if (a.intersect(b).isNotEmpty()) ApkCheck.OK else ApkCheck.WRONG_SIGNATURE
    }

    @Suppress("DEPRECATION")
    private fun packageInfo(get: (Int) -> PackageInfo?): PackageInfo? = runCatching {
        get(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PackageManager.GET_SIGNING_CERTIFICATES else PackageManager.GET_SIGNATURES)
    }.getOrNull()

    @Suppress("DEPRECATION")
    private fun certificates(info: PackageInfo): Set<String> {
        val sigs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val s = info.signingInfo ?: return emptySet()
            if (s.hasMultipleSigners()) s.apkContentsSigners.toList() else s.signingCertificateHistory.orEmpty().toList()
        } else {
            info.signatures.orEmpty().toList()
        }
        return sigs.map { Checksums.sha256(it.toByteArray().inputStream()) }.toSet()
    }

    companion object {
        private const val SNOOZE_MS = 24 * 60 * 60 * 1000L
        /** Автоматичната проверка при стартиране — най-много веднъж на 12 часа. */
        private const val LAUNCH_CHECK_INTERVAL_MS = 12 * 60 * 60 * 1000L
        private const val MAX_APK_BYTES = 200L * 1024 * 1024
    }
}
