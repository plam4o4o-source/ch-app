package org.chyavorec.app.ui.screens.catalog

import org.chyavorec.domain.model.CatalogBook

/** Ширината на прозореца, плътността и размерът на шрифта — при тях височината на един ред е същата. */
internal data class ListLayoutKey(val width: Int, val density: Float, val fontScale: Float)

/**
 * Височините на видимите редове на каталога към момента, в който от списъка е отворена книга — за
 * първия кадър при връщане от нея ([CatalogScreen]). Тогава се строи само редът на тази книга (корицата
 * ѝ „прелита“ обратно към него), а другите видими редове са празни места със същата височина, така че
 * всеки ред (и корицата, към която се прелита) е на същото място; истинските редове са в следващия кадър.
 *
 * Височината важи само при същото подреждане ([ListLayoutKey]) и същите данни на книгата (напр. книга,
 * която междувременно е заета, получава етикет и става по-висока); иначе редът се строи веднага.
 * Само за главната нишка (от композицията и от докосването на ред).
 */
internal class ListReturnRows {
    /** Книгата, отворена последна от списъка (инв. №). */
    var opened: Long? = null
        private set
    private var layout: ListLayoutKey? = null
    private val rows = HashMap<Long, Pair<CatalogBook, Int>>()

    /** Отворена е книга [inv]; [visible] — видимите редове (книгата и височината на реда в px) при [layout]. */
    fun onOpen(inv: Long, layout: ListLayoutKey, visible: List<Pair<CatalogBook, Int>>) {
        if (layout != this.layout || rows.size > MAX_ROWS) {
            rows.clear()
            this.layout = layout
        }
        opened = inv
        for ((book, height) in visible) rows[book.inv] = book to height
    }

    /**
     * Височината (px) на празното място вместо реда на [book] в първия кадър при връщане от книга при
     * [layout]; null — редът се строи веднага (книгата, към която прелита корицата, непознат ред,
     * друго подреждане или променени данни).
     */
    fun standIn(book: CatalogBook, layout: ListLayoutKey): Int? {
        if (book.inv == opened || layout != this.layout) return null
        val (seen, height) = rows[book.inv] ?: return null
        return height.takeIf { seen == book && it > 0 }
    }

    private companion object {
        /** Над толкова запомнени реда (много отворени книги) списъкът се започва наново. */
        const val MAX_ROWS = 400
    }
}
