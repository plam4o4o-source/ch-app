package org.chyavorec.app.ui.screens.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.chyavorec.domain.model.GalleryAlbum

/**
 * Снимките, за които сайтът е отговорил окончателно „няма я“ (HTTP 404/410, [PhotoFailure.Missing]) —
 * по адреса на миниатюрата. Такава снимка не се показва в мрежата на албума, не се брои в албума
 * и не се прелиства в прегледа; албум без нито една снимка изчезва от галерията.
 *
 * Помни се само в паметта на процеса и най-много [ttlMs] (ден): ако сайтът върне снимката,
 * тя се появява сама след рестарт на приложението или след деня — пропускът не е завинаги.
 * Грешка на връзката (без мрежа, timeout, 5xx) НЕ е пропуск — там плочката е с „Опитай отново“.
 */
internal class PhotoMissSet(
    private val ttlMs: Long = DAY_MS,
    private val now: () -> Long = System::currentTimeMillis,
) {
    private val _misses = MutableStateFlow<Map<String, Long>>(emptyMap())
    /** Адрес → кога сайтът е казал, че я няма (за наблюдение от композицията). */
    val misses: StateFlow<Map<String, Long>> = _misses.asStateFlow()

    /** Записва липсваща снимка; повторението (още валидно) не сменя нищо, остарелите се махат. */
    fun record(url: String?) {
        if (url.isNullOrBlank()) return
        val t = now()
        _misses.update { m ->
            if (m[url]?.let { fresh(it, t) } == true) m
            else m.filterValues { fresh(it, t) } + (url to t)
        }
    }

    /** Липсващите в момента (без изтеклите). */
    fun active(): Set<String> = activeIn(_misses.value)

    /** Липсващите от [snapshot] към този момент. */
    fun activeIn(snapshot: Map<String, Long>): Set<String> {
        val t = now()
        return snapshot.filterValues { fresh(it, t) }.keys
    }

    /** Забравя всичко (тестове). */
    fun clear() {
        _misses.value = emptyMap()
    }

    // Върнат назад часовник не удължава пропуска над деня.
    private fun fresh(at: Long, t: Long): Boolean = t - at in 0..ttlMs

    companion object {
        const val DAY_MS = 24L * 60 * 60 * 1000
    }
}

/** Общият за процеса списък (галерията, албумът и прегледът го споделят). */
internal val PhotoMisses = PhotoMissSet()

/**
 * Албумите без липсващите на сайта снимки ([missing] — адреси на миниатюри); албум, в който
 * не остава нищо, отпада. Без пропуски — същият списък (без копиране).
 */
internal fun List<GalleryAlbum>.withoutMissing(missing: Set<String>): List<GalleryAlbum> {
    if (missing.isEmpty()) return this
    return mapNotNull { album ->
        val photos = album.photos.filterNot { it.thumbUrl in missing }
        when {
            photos.isEmpty() -> null
            photos.size == album.photos.size -> album
            else -> album.copy(photos = photos)
        }
    }
}

/** Липсващите снимки за композицията — прекомпозира, щом плочка открие нова. */
@Composable
internal fun rememberMissingPhotos(): Set<String> {
    val misses by PhotoMisses.misses.collectAsStateWithLifecycle()
    return remember(misses) { PhotoMisses.activeIn(misses) }
}
