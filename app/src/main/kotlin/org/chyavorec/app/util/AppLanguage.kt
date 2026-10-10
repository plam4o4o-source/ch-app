package org.chyavorec.app.util

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.Locale

/**
 * Езикът на приложението (избор в настройките), без AppCompat.
 *
 * - Android 13+: системният LocaleManager (езикът се вижда и в системните
 *   настройки на приложението благодарение на `android:localeConfig`); системата
 *   сама пресъздава активността.
 * - Android 8–12: изборът се пази в SharedPreferences; [wrap] (от
 *   `MainActivity.attachBaseContext`) прилага езика към контекста на активността,
 *   а [set] я пресъздава.
 *
 * Езиков таг "" означава „като системата“.
 */
object AppLanguage {
    private const val PREFS = "app_language"
    private const val KEY_TAG = "tag"

    /** Файлът, в който AppCompat пазеше избора (autoStoreLocales) — еднократно пренасяне. */
    private const val APPCOMPAT_FILE = "androidx.appcompat.app.AppCompatDelegate.application_locales_record_file"

    @Volatile private var overridden = false

    /** Текущият избран езиков таг ("" = системният език). */
    fun current(context: Context): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) systemTag(context) else storedTag(context)

    /** Сменя езика ("" = системният) и пресъздава активността, за да се приложи. */
    fun set(context: Context, tag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setSystemTag(context, tag)
        } else {
            // commit (синхронно): записът трябва да е готов преди attachBaseContext на новата активност.
            prefs(context).edit().putString(KEY_TAG, tag).commit()
            context.findActivity()?.recreate()
        }
    }

    /**
     * Контекст с избрания език за `attachBaseContext` (Android 8–12). Прилага се
     * само езикът (останалата конфигурация — тъмна тема, размер — следва системата).
     */
    fun wrap(base: Context): Context {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return base
        val tag = storedTag(base)
        if (tag.isEmpty()) {
            // Връщане към системния език след избор в същия процес.
            if (overridden) {
                LocaleList.setDefault(Resources.getSystem().configuration.locales)
                overridden = false
            }
            return base
        }
        val locales = LocaleList(Locale.forLanguageTag(tag))
        LocaleList.setDefault(locales)
        overridden = true
        val delta = Configuration().apply { setLocales(locales) }
        return base.createConfigurationContext(delta)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun systemTag(context: Context): String = try {
        context.getSystemService(LocaleManager::class.java)?.applicationLocales?.toLanguageTags().orEmpty()
    } catch (_: Exception) {
        ""
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setSystemTag(context: Context, tag: String) {
        try {
            context.getSystemService(LocaleManager::class.java)?.applicationLocales =
                if (tag.isEmpty()) LocaleList.getEmptyLocaleList() else LocaleList.forLanguageTags(tag)
        } catch (_: Exception) {
            // Без смяна на езика — по-добре от срив.
        }
    }

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private fun storedTag(context: Context): String = runCatching {
        val prefs = prefs(context)
        prefs.getString(KEY_TAG, null) ?: migrateFromAppCompat(context).also {
            prefs.edit().putString(KEY_TAG, it).apply()
        }
    }.getOrDefault("")

    /** Изборът, запазен от AppCompat (до версия без AppCompat), или "". */
    private fun migrateFromAppCompat(context: Context): String = runCatching {
        val file = context.getFileStreamPath(APPCOMPAT_FILE)
        if (!file.exists()) return@runCatching ""
        val xml = file.readText()
        Regex("application_locales=\"([^\"]*)\"").find(xml)?.groupValues?.get(1).orEmpty()
    }.getOrDefault("")

    private tailrec fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
