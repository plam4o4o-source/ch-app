package org.chyavorec.app.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp

/**
 * „Бод“ — собственият набор икони на приложението: като схема за кръстат бод, само от прави
 * бодове (дебелина [STITCH], квадратни краища) и малки ромбове върху решетка 24 × 24.
 * Едноцветни — оцветяват се като всяка икона (`Icon(tint = …)`), затова стоят еднакво в
 * светла и тъмна тема и в „тематичния“ едноцветен вид. Помощните действия (назад, сподели,
 * затвори, търсене, настройки) остават със стандартните икони.
 *
 * Всяка икона се строи при първото поискване и после се помни (както стандартните).
 * Целият набор (светло/тъмно, 24 и 48 dp, върху плочката) — в FolkIconsScreenshotTest.
 */
object FolkIcons {
    /** Дебелината на бода (в единици от решетката 24). */
    const val STITCH = 1.7f

    /** Начало. */
    val Home: ImageVector by lazy {
        bod("Home") {
            s(2.5f, 11.5f, 12f, 3.5f, 21.5f, 11.5f); s(5.5f, 9.5f, 5.5f, 20.5f, 18.5f, 20.5f, 18.5f, 9.5f); d(12f, 14.5f, 2.6f)
        }
    }

    /** Начало (избран раздел). */
    val HomeSelected: ImageVector by lazy {
        bod("HomeSelected") {
            s(2.5f, 11.5f, 12f, 3.5f, 21.5f, 11.5f)
            cut { p(4.7f, 10.2f, 12f, 4.6f, 19.3f, 10.2f, 19.3f, 21.3f, 4.7f, 21.3f); d(12f, 14.5f, 2.6f) }
        }
    }

    /** Каталог. */
    val Catalog: ImageVector by lazy {
        bod("Catalog") {
            c(12f, 6.5f, 3f, 4.5f, 3f, 18.5f, 12f, 20.5f); c(12f, 6.5f, 21f, 4.5f, 21f, 18.5f, 12f, 20.5f); s(5.5f, 9f, 9.5f, 10f)
            s(5.5f, 12.5f, 9.5f, 13.5f); d(16.5f, 12.5f, 2.2f)
        }
    }

    /** Каталог (избран раздел). */
    val CatalogSelected: ImageVector by lazy {
        bod("CatalogSelected") {
            cut { p(11.1f, 5.8f, 2.2f, 3.6f, 2.2f, 19.2f, 11.1f, 21.3f); p(4.8f, 8.3f, 9.8f, 9.5f, 9.8f, 10.9f, 4.8f, 9.7f); p(4.8f, 11.8f, 9.8f, 13f, 9.8f, 14.4f, 4.8f, 13.2f) }
            cut { p(12.9f, 5.8f, 21.8f, 3.6f, 21.8f, 19.2f, 12.9f, 21.3f); d(16.9f, 12.5f, 2.4f) }
        }
    }

    /** Моето. */
    val My: ImageVector by lazy {
        bod("My") {
            o(12f, 7.5f, 4.2f); c(4f, 21f, 4f, 18f, 8f, 14.5f, 16f, 14.5f, 20f, 18f, 20f, 21f)
        }
    }

    /** Моето (избран раздел). */
    val MySelected: ImageVector by lazy {
        bod("MySelected") {
            d(12f, 7.5f, 5f); f(3.2f, 21.8f, 3.2f, 17.6f, 7.7f, 13.7f, 16.3f, 13.7f, 20.8f, 17.6f, 20.8f, 21.8f)
        }
    }

    /** Новини. */
    val News: ImageVector by lazy {
        bod("News") {
            c(3.5f, 3.5f, 20.5f, 3.5f, 20.5f, 20.5f, 3.5f, 20.5f); s(6.5f, 7.5f, 17.5f, 7.5f); d(8.5f, 13f, 2.6f)
            s(13f, 11f, 17.5f, 11f); s(13f, 14.5f, 17.5f, 14.5f); s(6.5f, 17.5f, 17.5f, 17.5f)
        }
    }

    /** Новини (избран раздел). */
    val NewsSelected: ImageVector by lazy {
        bod("NewsSelected") {
            cut { p(2.6f, 2.6f, 21.4f, 2.6f, 21.4f, 21.4f, 2.6f, 21.4f); p(5.8f, 6.7f, 18.2f, 6.7f, 18.2f, 8.3f, 5.8f, 8.3f); d(8.5f, 13f, 2.6f); p(12.3f, 10.2f, 18.2f, 10.2f, 18.2f, 11.8f, 12.3f, 11.8f); p(12.3f, 13.7f, 18.2f, 13.7f, 18.2f, 15.3f, 12.3f, 15.3f); p(5.8f, 16.7f, 18.2f, 16.7f, 18.2f, 18.3f, 5.8f, 18.3f) }
        }
    }

    /** Още. */
    val More: ImageVector by lazy {
        bod("More") {
            o(12f, 6f, 3.6f); o(18f, 12f, 3.6f); o(12f, 18f, 3.6f); o(6f, 12f, 3.6f)
        }
    }

    /** Още (избран раздел). */
    val MoreSelected: ImageVector by lazy {
        bod("MoreSelected") {
            d(12f, 6f, 4.4f); d(18f, 12f, 4.4f); d(12f, 18f, 4.4f); d(6f, 12f, 4.4f)
        }
    }

    /** Моите книги. */
    val Books: ImageVector by lazy {
        bod("Books") {
            c(3f, 16.5f, 21f, 16.5f, 21f, 21f, 3f, 21f); c(5f, 12f, 20f, 12f, 20f, 16.5f, 5f, 16.5f)
            c(4f, 7.5f, 18f, 7.5f, 18f, 12f, 4f, 12f); s(7f, 7.5f, 7f, 12f); s(17f, 12f, 17f, 16.5f); s(6f, 16.5f, 6f, 21f)
        }
    }

    /** Сканирай. */
    val Scan: ImageVector by lazy {
        bod("Scan") {
            s(3f, 8f, 3f, 3f, 8f, 3f); s(16f, 3f, 21f, 3f, 21f, 8f); s(21f, 16f, 21f, 21f, 16f, 21f); s(8f, 21f, 3f, 21f, 3f, 16f)
            s(7.5f, 7.5f, 7.5f, 16.5f); s(10.5f, 7.5f, 10.5f, 16.5f); s(13.5f, 7.5f, 13.5f, 16.5f); s(16.5f, 7.5f, 16.5f, 16.5f)
        }
    }

    /** Читателска карта. */
    val Card: ImageVector by lazy {
        bod("Card") {
            c(2.5f, 5f, 21.5f, 5f, 21.5f, 19f, 2.5f, 19f); d(7.5f, 12f, 2.8f); s(12.5f, 10f, 18.5f, 10f)
            s(12.5f, 14f, 16.5f, 14f)
        }
    }

    /** Искам да прочета. */
    val Bookmark: ImageVector by lazy {
        bod("Bookmark") {
            c(6f, 3f, 18f, 3f, 18f, 21f, 12f, 16f, 6f, 21f); d(12f, 9f, 2.4f)
        }
    }

    /** Контакти. */
    val Pin: ImageVector by lazy {
        bod("Pin") {
            c(12f, 21.5f, 5f, 10f, 12f, 2.5f, 19f, 10f); d(12f, 10f, 2.4f)
        }
    }

    /** Съобщения. */
    val Messages: ImageVector by lazy {
        bod("Messages") {
            c(2.5f, 5.5f, 21.5f, 5.5f, 21.5f, 18.5f, 2.5f, 18.5f); s(2.5f, 5.5f, 12f, 12.5f, 21.5f, 5.5f); d(12f, 13.5f, 2.3f)
        }
    }

    /** Събития. */
    val Events: ImageVector by lazy {
        bod("Events") {
            c(3f, 5f, 21f, 5f, 21f, 21f, 3f, 21f); s(3f, 9.5f, 21f, 9.5f); s(8f, 2.5f, 8f, 6.5f); s(16f, 2.5f, 16f, 6.5f)
            d(7.5f, 13.5f, 1.4f); d(12f, 13.5f, 1.4f); d(16.5f, 13.5f, 1.4f); d(7.5f, 17.5f, 1.4f); d(12f, 17.5f, 2f)
        }
    }

    /** Дейности. */
    val Activities: ImageVector by lazy {
        bod("Activities") {
            d(5f, 5.2f, 2.2f); d(12f, 5.2f, 2.2f); d(19f, 5.2f, 2.2f)
            s(1.5f, 13.5f, 5f, 10f, 8.5f, 13.5f, 12f, 10f, 15.5f, 13.5f, 19f, 10f, 22.5f, 13.5f); s(5f, 8.5f, 5f, 15.5f)
            s(12f, 8.5f, 12f, 15.5f); s(19f, 8.5f, 19f, 15.5f); s(2.8f, 21f, 5f, 15.5f, 7.2f, 21f)
            s(9.8f, 21f, 12f, 15.5f, 14.2f, 21f); s(16.8f, 21f, 19f, 15.5f, 21.2f, 21f)
        }
    }

    /** За читалището. */
    val Chitalishte: ImageVector by lazy {
        bod("Chitalishte") {
            c(2.5f, 8.5f, 12f, 3f, 21.5f, 8.5f); s(6f, 11f, 6f, 18f); s(10f, 11f, 10f, 18f); s(14f, 11f, 14f, 18f)
            s(18f, 11f, 18f, 18f); s(2.5f, 20.5f, 21.5f, 20.5f)
        }
    }

    /** Галерия. */
    val Gallery: ImageVector by lazy {
        bod("Gallery") {
            c(3f, 4f, 21f, 4f, 21f, 20f, 3f, 20f); s(3f, 17f, 8.5f, 11f, 12.5f, 15f, 15f, 12.5f, 21f, 18.5f); d(15.5f, 8.5f, 2f)
        }
    }

    /** Документи. */
    val Documents: ImageVector by lazy {
        bod("Documents") {
            c(5f, 2.5f, 14.5f, 2.5f, 19.5f, 7.5f, 19.5f, 21.5f, 5f, 21.5f); s(14.5f, 2.5f, 14.5f, 7.5f, 19.5f, 7.5f)
            s(8f, 12f, 16.5f, 12f); s(8f, 15f, 16.5f, 15f); s(8f, 18f, 13.5f, 18f)
        }
    }

    /** Извези шевица. */
    val Embroider: ImageVector by lazy {
        bod("Embroider") {
            cut { p(21.8f, 2.2f, 21.6f, 5f, 11.2f, 13.8f, 19f, 2.4f); p(20.6f, 3.5f, 20.6f, 4.3f, 18.6f, 6.3f, 18.6f, 5.5f) }
            s(3.5f, 16.5f, 6.5f, 19.5f); s(6.5f, 16.5f, 3.5f, 19.5f); s(9.5f, 16.5f, 12.5f, 19.5f); s(12.5f, 16.5f, 9.5f, 19.5f)
            s(3.5f, 10.5f, 6.5f, 13.5f); s(6.5f, 10.5f, 3.5f, 13.5f)
        }
    }

    /** Старият вестник. */
    val Newspaper: ImageVector by lazy {
        bod("Newspaper") {
            c(3.5f, 3f, 20.5f, 3f, 20.5f, 6.5f, 3.5f, 6.5f); c(3.5f, 17.5f, 20.5f, 17.5f, 20.5f, 21f, 3.5f, 21f)
            s(5.5f, 6.5f, 5.5f, 17.5f); s(18.5f, 6.5f, 18.5f, 17.5f); s(8.5f, 10f, 15.5f, 10f); s(8.5f, 14f, 15.5f, 14f)
        }
    }

    /** Лента на времето. */
    val Timeline: ImageVector by lazy {
        bod("Timeline") {
            s(6f, 2.5f, 6f, 21.5f); d(6f, 6f, 2.6f); d(6f, 12f, 2.6f); d(6f, 18f, 2.6f); s(10.5f, 6f, 20.5f, 6f)
            s(10.5f, 12f, 17.5f, 12f); s(10.5f, 18f, 19.5f, 18f)
        }
    }

    /** Познай Яворец. */
    val Quiz: ImageVector by lazy {
        bod("Quiz") {
            s(6.5f, 8.5f, 9.5f, 3.5f, 14.5f, 3.5f, 17.5f, 8.5f, 12f, 13f, 12f, 15.5f); d(12f, 19.5f, 2f)
        }
    }

    /** Дигитален клуб. */
    val Computer: ImageVector by lazy {
        bod("Computer") {
            c(2.5f, 4f, 21.5f, 4f, 21.5f, 16f, 2.5f, 16f); s(12f, 16f, 12f, 20.5f); s(8f, 20.5f, 16f, 20.5f); d(12f, 10f, 2.6f)
        }
    }

    /** Членство. */
    val Membership: ImageVector by lazy {
        bod("Membership") {
            o(12f, 9f, 6.3f); d(12f, 9f, 2.4f); s(9f, 14f, 7.5f, 21.5f, 10f, 19.8f, 11.6f, 21.5f)
            s(15f, 14f, 16.5f, 21.5f, 14f, 19.8f, 12.4f, 21.5f)
        }
    }

    /** История. */
    val History: ImageVector by lazy {
        bod("History") {
            s(5f, 3f, 19f, 3f); s(5f, 21f, 19f, 21f); c(6.5f, 3f, 17.5f, 3f, 12f, 12f); c(12f, 12f, 17.5f, 21f, 6.5f, 21f)
            f(12f, 15.5f, 15f, 20.2f, 9f, 20.2f)
        }
    }

    /** Профил. */
    val Profile: ImageVector by lazy {
        bod("Profile") {
            c(3f, 3f, 21f, 3f, 21f, 21f, 3f, 21f); o(12f, 9.5f, 3.2f)
            s(6.5f, 21f, 6.5f, 18.5f, 9.5f, 15.5f, 14.5f, 15.5f, 17.5f, 18.5f, 17.5f, 21f)
        }
    }

    /** Известия. */
    val Notifications: ImageVector by lazy {
        bod("Notifications") {
            c(5.5f, 17.5f, 7.5f, 9f, 10f, 6f, 14f, 6f, 16.5f, 9f, 18.5f, 17.5f); s(3.5f, 17.5f, 20.5f, 17.5f); s(12f, 3f, 12f, 6f)
            d(12f, 20.5f, 1.8f)
        }
    }
}

/**
 * Чертожът на една икона: [s] — отворена линия от бодове, [c] — затворена, [o] — ромб-контур,
 * [f] — плътен многоъгълник, [d] — плътен ромб (център и „радиус“ до върха),
 * [cut] — плътна форма с изрязани в нея дупки (четно-нечетно запълване).
 */
private class BodScope {
    val stitches = PathBuilder()
    val fills = PathBuilder()
    val cuts = ArrayList<PathBuilder>(2)

    fun s(vararg p: Float) = stitches.poly(p, closed = false)
    fun c(vararg p: Float) = stitches.poly(p, closed = true)
    fun o(x: Float, y: Float, r: Float) = stitches.diamond(x, y, r)
    fun f(vararg p: Float) = fills.poly(p, closed = true)
    fun d(x: Float, y: Float, r: Float) = fills.diamond(x, y, r)
    fun cut(block: Cut.() -> Unit) {
        cuts += Cut().apply(block).path
    }

    class Cut {
        val path = PathBuilder()
        fun p(vararg p: Float) = path.poly(p, closed = true)
        fun d(x: Float, y: Float, r: Float) = path.diamond(x, y, r)
    }
}

private fun PathBuilder.poly(p: FloatArray, closed: Boolean) {
    moveTo(p[0], p[1])
    var i = 2
    while (i < p.size) {
        lineTo(p[i], p[i + 1])
        i += 2
    }
    if (closed) close()
}

private fun PathBuilder.diamond(x: Float, y: Float, r: Float) {
    moveTo(x, y - r); lineTo(x + r, y); lineTo(x, y + r); lineTo(x - r, y); close()
}

private val Ink = SolidColor(Color.Black)

private inline fun bod(name: String, block: BodScope.() -> Unit): ImageVector {
    val scope = BodScope().apply(block)
    return ImageVector.Builder(
        name = "Bod.$name", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f,
    ).apply {
        val stitches = scope.stitches.nodes
        if (stitches.isNotEmpty()) {
            addPath(
                stitches, fill = null, stroke = Ink, strokeLineWidth = FolkIcons.STITCH,
                strokeLineCap = StrokeCap.Square, strokeLineJoin = StrokeJoin.Miter, strokeLineMiter = 4f,
            )
        }
        val fills = scope.fills.nodes
        if (fills.isNotEmpty()) addPath(fills, fill = Ink)
        scope.cuts.forEach { addPath(it.nodes, pathFillType = PathFillType.EvenOdd, fill = Ink) }
    }.build()
}
