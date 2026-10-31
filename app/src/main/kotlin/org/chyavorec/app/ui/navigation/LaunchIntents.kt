package org.chyavorec.app.ui.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import org.chyavorec.app.MainActivity
import org.chyavorec.app.notifications.Notifier
import org.chyavorec.core.Diagnostics

/**
 * Кога intent-ът, с който е отворен [org.chyavorec.app.MainActivity], носи *нова* връзка.
 *
 * На Android 8–11 „Назад“ от първия екран затваря activity-то, а задачата остава в
 * „Последни“. Отварянето ѝ оттам подава отново *първоначалния* intent на задачата (със
 * старото известие или адрес) с флага [Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY] —
 * той не е ново действие на потребителя и не бива да отваря екрана отново. Същото важи и
 * при пресъздаване (завъртане, спрян от системата процес): тогава intent-ът е вече
 * обработен, а неотработената връзка (ако има) идва от запазеното състояние.
 */
object LaunchIntents {
    /** Ключът в `savedInstanceState` за връзката, която още не е отворена. */
    const val KEY_PENDING_LINK = "pending_deep_link"

    /** [flags] — флаговете на intent-а; [restored] — activity-то е пресъздадено (има запазено състояние). */
    fun shouldHandle(flags: Int, restored: Boolean): Boolean =
        !restored && (flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0

    /**
     * Обикновен intent на мястото на обработения (за `setIntent`): без връзка, extra и
     * данни — повторно четене на `getIntent()` не намира нищо за отваряне.
     */
    fun consumed(intent: Intent): Intent =
        Intent(Intent.ACTION_MAIN).setComponent(intent.component)

    /**
     * Действието на собствените връзки (известия, уиджет) — само през неекспортирания вход
     * [OPEN_LINK_ALIAS] (activity-alias в AndroidManifest.xml). Друго приложение може да
     * стартира MainActivity (тя е exported) с какъвто и да е intent, но не и този вход.
     */
    const val ACTION_OPEN_LINK = "org.chyavorec.app.action.OPEN_LINK"

    /** Неекспортираният вход към MainActivity за собствените PendingIntent-и (името е от namespace, не от applicationId). */
    const val OPEN_LINK_ALIAS = "org.chyavorec.app.OpenLink"

    /** Действие на статичните преки пътища (res/xml/shortcuts.xml); връзката е в `data`. */
    const val ACTION_SHORTCUT = "org.chyavorec.app.SHORTCUT"
    const val SHORTCUT_SCHEME = "chyavorec"

    /**
     * Връзките на преките пътища — статичните (res/xml/shortcuts.xml) и „Искам да прочета“
     * (динамичен до 1.2.28 — закачените на началния екран копия още го отварят). Пряк път се
     * стартира от launcher-а (друго приложение), затова се приемат само тези, не произволна връзка.
     */
    val SHORTCUT_LINKS = setOf("catalog", "my/card", "scan", DeepLinks.WANT_TO_READ)

    /** Динамичният пряк път към последната книга от публичния каталог — само положителен инв. №. */
    private val SHORTCUT_BOOK = Regex("catalog/book/[1-9][0-9]{0,17}")

    fun isShortcutLink(link: String): Boolean = link in SHORTCUT_LINKS || SHORTCUT_BOOK.matches(link)

    /**
     * Известията до v1.2.25 включително са с `Intent(context, MainActivity)` — без действие и
     * без неекспортирания вход, само с extra „deep_link“. Вече показаните такива известия се
     * отварят, но само с връзки, които те са носили (заемите, съобщение, събитие, новина) —
     * чужд intent със същия вид не може да отвори произволна връзка (вход, обновяване).
     * Пътят може да се махне от около 1.2.30, когато такива известия вече няма да има.
     */
    fun isLegacyNotificationLink(link: String): Boolean = when {
        link == "my/loans" -> true
        link.startsWith("event/") || link.startsWith("article/") || link.startsWith("message/") ->
            link.substringAfter('/').isNotBlank()
        else -> false
    }

    /**
     * Intent към приложението, отварящ вътрешната връзка [link] (виж [DeepLinks]) — за
     * PendingIntent-ите на известията и уиджета. `data` е само за различни PendingIntent-и
     * (не се сливат); връзката се чете от extra-то.
     */
    fun openLink(context: Context, link: String?): Intent =
        Intent(ACTION_OPEN_LINK)
            .setComponent(ComponentName(context.packageName, OPEN_LINK_ALIAS))
            .apply {
                link?.let {
                    putExtra(Notifier.EXTRA_DEEP_LINK, it)
                    data = Uri.parse("$SHORTCUT_SCHEME://link/" + Uri.encode(it))
                }
            }

    /**
     * Връзката, която [intent] иска да отвори, или `null` (обикновено отваряне или чужд
     * intent, който се пренебрегва):
     * - собствена връзка (extra „deep_link“) — само с [ACTION_OPEN_LINK] през [OPEN_LINK_ALIAS];
     * - известие от стара версия (без действие, изрично към MainActivity) — само
     *   [isLegacyNotificationLink];
     * - пряк път — само връзка от [SHORTCUT_LINKS] или книга от каталога ([isShortcutLink]);
     * - адрес (ACTION_VIEW) — само адрес от сайта с native екран ([DeepLinks.forUrl]); всичко
     *   друго (чужд сайт, файл, непознат път) не се отваря през приложението.
     */
    fun linkOf(intent: Intent, siteBaseUrl: String): String? {
        val data = intent.data
        return when (intent.action) {
            ACTION_OPEN_LINK -> intent.getStringExtra(Notifier.EXTRA_DEEP_LINK)
                ?.takeIf { intent.component?.className == OPEN_LINK_ALIAS }
            ACTION_SHORTCUT -> data?.takeIf { it.scheme == SHORTCUT_SCHEME }?.path.orEmpty().trim('/').takeIf { isShortcutLink(it) }
            Intent.ACTION_VIEW -> data?.takeIf { it.scheme == "https" }?.toString()
                ?.takeIf { DeepLinks.forUrl(it, siteBaseUrl) != null }
            null -> intent.getStringExtra(Notifier.EXTRA_DEEP_LINK)
                ?.takeIf { intent.component?.className == MainActivity::class.java.name && isLegacyNotificationLink(it) }
                ?.also { Diagnostics.note("deeplink", "legacy link") }
            else -> null
        }
    }
}
