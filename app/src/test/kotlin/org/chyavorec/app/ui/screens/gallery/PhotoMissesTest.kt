package org.chyavorec.app.ui.screens.gallery

import org.chyavorec.domain.model.GalleryAlbum
import org.chyavorec.domain.model.GalleryPhoto
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Снимките, които сайтът няма (404/410): отпадат от албума и от броя му, празен албум изчезва,
 * а след деня (или в нов процес) снимката пак се пробва — ако сайтът я върне, тя се появява.
 */
class PhotoMissesTest {
    private var now = 1_000_000L
    private val misses = PhotoMissSet(ttlMs = PhotoMissSet.DAY_MS, now = { now })

    private fun photo(n: Int, album: String) = GalleryPhoto("$album-$n", "", "https://chyavorec.org/files/exh-$n-t.jpg", "https://chyavorec.org/files/exh-$n.jpg", album, "")
    private val exhibition = GalleryAlbum("Фотодокументална изложба", (1..4).map { photo(it, "Фотодокументална изложба") })
    private val feast = GalleryAlbum("Празници", listOf(photo(9, "Празници")))

    @Test fun missingPhotosLeaveTheAlbumAndItsCount() {
        misses.record(exhibition.photos[1].thumbUrl)
        misses.record(exhibition.photos[3].thumbUrl)
        val albums = listOf(exhibition, feast).withoutMissing(misses.active())
        assertEquals(listOf(1, 3).map { "Фотодокументална изложба-$it" }, albums[0].photos.map { it.id })
        assertEquals(2, albums[0].photos.size, "броят в албума е без липсващите")
        assertSame(feast, albums[1], "албум без пропуски остава същият")
    }

    @Test fun anAlbumWithoutPhotosIsHidden() {
        misses.record(feast.photos.single().thumbUrl)
        assertEquals(listOf(exhibition.name), listOf(exhibition, feast).withoutMissing(misses.active()).map { it.name })
    }

    @Test fun noMissesKeepsTheSameList() {
        val albums = listOf(exhibition, feast)
        assertSame(albums, albums.withoutMissing(misses.active()))
    }

    @Test fun aMissExpiresAfterADaySoARestoredPhotoComesBack() {
        val url = exhibition.photos[0].thumbUrl
        misses.record(url)
        now += PhotoMissSet.DAY_MS
        assertTrue(url in misses.active(), "до края на деня — още липсва")
        now += 1
        assertTrue(misses.active().isEmpty(), "след деня — пак се пробва")
        assertEquals(4, listOf(exhibition).withoutMissing(misses.active()).single().photos.size)
    }

    @Test fun aClockTurnedBackDoesNotExtendTheMiss() {
        misses.record(exhibition.photos[0].thumbUrl)
        now -= 1
        assertTrue(misses.active().isEmpty())
    }

    @Test fun repeatingAFreshMissDoesNotChangeTheState() {
        val url = exhibition.photos[0].thumbUrl
        misses.record(url)
        val before = misses.misses.value
        now += 1_000
        misses.record(url)
        assertSame(before, misses.misses.value, "без излишно прекомпозиране")
    }

    @Test fun expiredMissesArePrunedOnTheNextRecord() {
        misses.record(exhibition.photos[0].thumbUrl)
        now += PhotoMissSet.DAY_MS + 1
        misses.record(exhibition.photos[1].thumbUrl)
        assertEquals(setOf(exhibition.photos[1].thumbUrl), misses.misses.value.keys)
    }

    @Test fun blankAddressesAreIgnored() {
        misses.record(null)
        misses.record("")
        assertTrue(misses.misses.value.isEmpty())
    }
}
