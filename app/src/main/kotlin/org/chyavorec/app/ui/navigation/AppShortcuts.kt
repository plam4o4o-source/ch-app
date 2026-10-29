package org.chyavorec.app.ui.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chyavorec.app.MainActivity
import org.chyavorec.app.R
import org.chyavorec.domain.model.CatalogBook

/**
 * Динамичните преки пътища (задържане на иконата): „Искам да прочета“ и последната книга
 * от публичния каталог. Водят през същия вход като статичните ([LaunchIntents.ACTION_SHORTCUT]
 * с `chyavorec://app/<връзка>`), а връзките им са в списъка [LaunchIntents.isShortcutLink].
 *
 * Книга от личен списък (заемания, история, „За теб“, „Искам да прочета“) НЕ става пряк път:
 * заглавието би издало избора на читателя и би останало след изход. Затова при изход няма
 * какво да се маха — каталогът е публичен.
 */
object AppShortcuts {
    const val ID_WANT_TO_READ = "want-to-read"
    const val ID_LAST_BOOK = "last-book"

    private const val SHORT_LABEL_MAX = 20
    private const val LONG_LABEL_MAX = 40

    /** Intent-ът на пряк път към [link] — изрично към MainActivity, както в shortcuts.xml. */
    fun intent(context: Context, link: String): Intent =
        Intent(LaunchIntents.ACTION_SHORTCUT, Uri.parse("${LaunchIntents.SHORTCUT_SCHEME}://app/$link"))
            .setComponent(ComponentName(context, MainActivity::class.java))

    /** Отворен е списъкът „Искам да прочета“: прекият път се публикува (обновява) и се отчита ползването му. */
    fun wantToReadOpened(context: Context) {
        val info = ShortcutInfoCompat.Builder(context, ID_WANT_TO_READ)
            .setShortLabel(context.getString(R.string.shortcut_want_to_read))
            .setLongLabel(context.getString(R.string.shortcut_want_to_read_long))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_want))
            .setIntent(intent(context, DeepLinks.WANT_TO_READ))
            .build()
        push(context, info)
    }

    /** Отворена е книга от публичния каталог (или търсенето) — тя става „последната книга“. */
    fun publicBookOpened(context: Context, book: CatalogBook) {
        val title = book.title.trim().ifEmpty { return }
        val long = if (book.author.isBlank()) title else "$title — ${book.author.trim()}"
        val info = ShortcutInfoCompat.Builder(context, ID_LAST_BOOK)
            .setShortLabel(ellipsize(title, SHORT_LABEL_MAX))
            .setLongLabel(ellipsize(long, LONG_LABEL_MAX))
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_book))
            .setIntent(intent(context, DeepLinks.catalogBook(book.inv)))
            .build()
        push(context, info)
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

    private fun push(context: Context, info: ShortcutInfoCompat) {
        // Launcher-ът може да откаже (лимит, работен профил) — прекият път не е задължителен.
        runCatching {
            ShortcutManagerCompat.pushDynamicShortcut(context, info)
            ShortcutManagerCompat.reportShortcutUsed(context, info.id)
        }
    }
}

/** На екрана „Искам да прочета“: пряк път към него (извън главната нишка — IPC към системата). */
@Composable
fun WantToReadShortcutEffect() {
    val context = LocalContext.current.applicationContext
    LaunchedEffect(Unit) { withContext(Dispatchers.IO) { AppShortcuts.wantToReadOpened(context) } }
}

/** На екрана на книга, отворена от публичния каталог: пряк път към нея (само при `personal = false`). */
@Composable
fun PublicBookShortcutEffect(book: CatalogBook?) {
    val context = LocalContext.current.applicationContext
    LaunchedEffect(book?.inv) { book?.let { withContext(Dispatchers.IO) { AppShortcuts.publicBookOpened(context, it) } } }
}
