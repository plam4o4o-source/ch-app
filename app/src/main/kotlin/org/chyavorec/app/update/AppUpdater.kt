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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    /** Изтеглянето може да трае повече от общия timeout на споделения клиент. */
    private val downloadClient by lazy {
        okHttp.newBuilder().callTimeout(0, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build()
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
    fun showPrompt() { if (enabled) _prompt.value = true }
    fun hidePrompt() { _prompt.value = false }

    /**
     * При стартиране на приложението: тиха проверка; по Wi-Fi новата версия се
     * изтегля веднага. Диалогът се показва, освен ако потребителят е отложил
     * същата версия („По-късно“ — за [SNOOZE_MS]).
     */
    fun checkOnLaunch() {
        if (!enabled) return
        scope.launch {
            cleanup()
            if (!settings.current().autoUpdate) return@launch
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
        scope.launch {
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

    /** Изтегля и проверява APK файла. Повторно извикване за същата версия ползва вече изтегления файл. */
    suspend fun download(info: UpdateInfo): File? = mutex.withLock {
        withContext(Dispatchers.IO) {
            verifiedFile(info)?.let { _state.value = UpdateState.Ready(info); return@withContext it }
            _state.value = UpdateState.Downloading(info, if (info.sizeBytes != null) 0f else null)
            dir.mkdirs()
            val part = File(dir, "update-${info.versionCode}.apk.part")
            val target = apkFile(info)
            val digest = MessageDigest.getInstance("SHA-256")
            try {
                val request = Request.Builder().url(info.apkUrl).header("User-Agent", "ChitalishteYavorec-Android/${config.versionName}").build()
                downloadClient.newCall(request).execute().use { response ->
                    val body = response.body
                    if (!response.isSuccessful || body == null) throw IOException("HTTP ${response.code}")
                    val total = body.contentLength().takeIf { it > 0 } ?: info.sizeBytes
                    if (total != null && total > MAX_APK_BYTES) throw IOException("too large")
                    body.byteStream().use { input ->
                        DigestOutputStream(part.outputStream(), digest).use { out ->
                            val buf = ByteArray(64 * 1024)
                            var read = 0L
                            var lastReported = -1
                            while (true) {
                                val n = input.read(buf)
                                if (n < 0) break
                                out.write(buf, 0, n)
                                read += n
                                if (read > MAX_APK_BYTES) throw IOException("too large")
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
            } catch (e: Exception) {
                if (e is CancellationException) { part.delete(); throw e }
                part.delete()
                _state.value = UpdateState.Failed(UpdateFailure.DOWNLOAD, info)
                return@withContext null
            }
            val sha = digest.digest().joinToString("") { "%02x".format(it) }
            if (sha != info.sha256) {
                part.delete()
                _state.value = UpdateState.Failed(UpdateFailure.CHECKSUM, info)
                return@withContext null
            }
            when (verifyApk(part, info)) {
                ApkCheck.OK -> Unit
                ApkCheck.WRONG_SIGNATURE -> {
                    part.delete()
                    _state.value = UpdateState.Failed(UpdateFailure.SIGNATURE, info)
                    return@withContext null
                }
                ApkCheck.INVALID -> {
                    part.delete()
                    _state.value = UpdateState.Failed(UpdateFailure.CHECKSUM, info)
                    return@withContext null
                }
            }
            target.delete()
            if (!part.renameTo(target)) {
                part.delete()
                _state.value = UpdateState.Failed(UpdateFailure.DOWNLOAD, info)
                return@withContext null
            }
            _state.value = UpdateState.Ready(info)
            target
        }
    }

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
        return if (runCatching { Checksums.sha256(f) }.getOrNull() == info.sha256) f else { f.delete(); null }
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
        private const val MAX_APK_BYTES = 200L * 1024 * 1024
    }
}
