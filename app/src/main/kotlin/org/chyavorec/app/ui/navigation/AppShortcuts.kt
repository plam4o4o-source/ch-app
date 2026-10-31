package org.chyavorec.app.ui.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.domain.model.CatalogBook

/**
 * Динамичният пряк път (задържане на иконата) — последната книга от публичния каталог. Води
 * през същия вход като статичните ([LaunchIntents.ACTION_SHORTCUT] с `chyavorec://app/<връзка>`),
 * а връзката му е в списъка [LaunchIntents.isShortcutLink]. Статичните са три (res/xml/shortcuts.xml)
 * — общо 4, колкото показват launcher-ите.
 *
 * Книга от личен списък (заемания, история, „За теб“, „Искам да прочета“) НЕ става пряк път:
 * заглавието би издало избора на читателя. И публичната е следа от разглеждането (като историята
 * на търсенето) — маха се при изход и при „Изчисти временните данни“ ([removeLastBook]).
 */
object AppShortcuts {
    /** Статичните (shortcuts.xml). */
    const val ID_SCAN = "scan"
    const val ID_CARD = "card"
    const val ID_CATALOG = "catalog"
    /** Динамичният „Искам да прочета“ до 1.2.28 — вече не се публикува ([removeLegacy]). */
    const val ID_WANT_TO_READ = "want-to-read"
    const val ID_LAST_BOOK = "last-book"

    private const val SHORT_LABEL_MAX = 20
    private const val LONG_LABEL_MAX = 40

    /** Коя книга е публикувана последно — повторно отваряне на същата не вика системата отново. */
    private const val PREFS = "app_shortcuts"
    private const val KEY_LAST_BOOK = "last_book"

    /** Intent-ът на пряк път към [link] — изрично към MainActivity, както в shortcuts.xml. */
    fun intent(context: Context, link: String): Intent =
        Intent(LaunchIntents.ACTION_SHORTCUT, Uri.parse("${LaunchIntents.SHORTCUT_SCHEME}://app/$link"))
            .setComponent(ComponentName(context, MainActivity::class.java))

    /**
     * Отворена е книга от публичния каталог (или търсенето) — тя става „последната книга“.
     * @return false, ако същата книга вече е публикувана (без нова заявка към системата).
     */
    fun publicBookOpened(context: Context, book: CatalogBook): Boolean {
        val title = book.title.trim().ifEmpty { return false }
        val long = if (book.author.isBlank()) title else "$title — ${book.author.trim()}"
        val shortLabel = ellipsize(title, SHORT_LABEL_MAX)
        val longLabel = ellipsize(long, LONG_LABEL_MAX)
        val signature = "${book.inv}|$shortLabel|$longLabel"
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getString(KEY_LAST_BOOK, null) == signature) return false
        val info = ShortcutInfoCompat.Builder(context, ID_LAST_BOOK)
            .setShortLabel(shortLabel)
            .setLongLabel(longLabel)
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_book))
            .setIntent(intent(context, DeepLinks.catalogBook(book.inv)))
            .build()
        // Launcher-ът може да откаже (лимит, работен профил) — прекият път не е задължителен.
        // pushDynamicShortcut сам отчита ползването.
        val pushed = runCatching { ShortcutManagerCompat.pushDynamicShortcut(context, info) }.getOrDefault(false)
        if (pushed) prefs.edit { putString(KEY_LAST_BOOK, signature) }
        return pushed
    }

    /** Маха „последната книга“ (изход, „Изчисти временните данни“). Закачената на началния екран остава — избор на потребителя. */
    fun removeLastBook(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit { remove(KEY_LAST_BOOK) }
        runCatching { ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(ID_LAST_BOOK)) }
    }

    /** Еднократно след обновяване: динамичният „Искам да прочета“ от по-старите версии. */
    fun removeLegacy(context: Context) {
        runCatching { ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(ID_WANT_TO_READ)) }
    }

    /** ID-то на прекия път с връзка [link] (за отчитане на ползването) или `null`. */
    internal fun idOf(link: String): String? = when (link) {
        "scan" -> ID_SCAN
        "my/card" -> ID_CARD
        "catalog" -> ID_CATALOG
        DeepLinks.WANT_TO_READ -> ID_WANT_TO_READ
        else -> ID_LAST_BOOK.takeIf { link.startsWith("catalog/book/") && LaunchIntents.isShortcutLink(link) }
    }

    /** Приложението е отворено от пряк път с [link] — launcher-ът го нарежда по-напред. */
    fun launched(context: Context, link: String) {
        val id = idOf(link) ?: return
        runCatching { ShortcutManagerCompat.reportShortcutUsed(context, id) }
    }

    /** Съкращава [text] до [max] знака с „…“ (по думи, ако може). */
    internal fun ellipsize(text: String, max: Int): String {
        val t = text.trim()
        if (t.length <= max) return t
        val cut = t.take(max - 1)
        // Следващият знак не е буква — отрязаното завършва с цяла дума.
        val whole = if (!t[max - 1].isLetterOrDigit()) cut else cut.substringBeforeLast(' ', "")
        val word = whole.trimEnd(' ', ',', '.', ':', ';', '—', '-')
        return (if (word.length >= max / 2) word else cut.trimEnd()) + "…"
    }
}

/** На екрана на книга, отворена от публичния каталог: пряк път към нея (само при `personal = false`). */
@Composable
fun PublicBookShortcutEffect(book: CatalogBook?) {
    val context = LocalContext.current.applicationContext
    LaunchedEffect(book?.inv) { book?.let { withContext(Dispatchers.IO) { AppShortcuts.publicBookOpened(context, it) } } }
}
