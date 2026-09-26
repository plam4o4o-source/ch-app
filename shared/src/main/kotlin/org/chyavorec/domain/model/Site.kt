package org.chyavorec.domain.model

import kotlinx.serialization.Serializable

/** Връзка от навигацията на сайта (открита автоматично). */
@Serializable
data class SiteLink(val title: String, val url: String, val kind: SiteSection)

/** Разделите на сайта, които приложението разпознава. */
@Serializable
enum class SiteSection {
    ABOUT, HISTORY, LIBRARY, CATALOG, EVENTS, FOLKLORE, DANCE, CLUBS, PROJECTS,
    DIGITAL_CLUB, CONTACTS, GALLERY, NEWS, DOCUMENTS, DONATIONS, PUBLICATIONS,
    VILLAGE, PRIVACY, TERMS, OTHER,
}

@Serializable
data class SitePage(
    val url: String,
    val title: String,
    val blocks: List<ContentBlock>,
    val images: List<String>,
)

@Serializable
data class Contacts(
    val organization: String,
    val address: String?,
    val phones: List<String>,
    val emails: List<String>,
    val website: String,
    val facebook: String?,
    val workingHours: List<String>,
    val mapQuery: String,
    /** true, когато данните са прочетени от страницата „Контакти“ на сайта. */
    val fromSite: Boolean,
    val sourceUrl: String?,
)

@Serializable
data class GalleryPhoto(
    val id: String,
    val title: String,
    val thumbUrl: String,
    val fullUrl: String,
    val album: String,
    val pageUrl: String,
    val publishedAtMillis: Long? = null,
)

data class GalleryAlbum(val name: String, val photos: List<GalleryPhoto>) {
    val cover: GalleryPhoto? get() = photos.firstOrNull()
}
