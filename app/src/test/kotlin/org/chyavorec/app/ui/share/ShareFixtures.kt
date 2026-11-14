package org.chyavorec.app.ui.share

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.chyavorec.data.catalog.ReadingYear
import org.chyavorec.domain.model.CatalogBook
import org.chyavorec.domain.model.HistoryItem
import java.time.LocalDate

/**
 * Примерни данни за картите и „Моята читателска година“ — същата година като демо входа
 * (DemoInvLibServices в dev): ~30 книги назад от 7 ноември 2026, с любим автор, раздели и поредица.
 */
object ShareFixtures {
    val today: LocalDate = LocalDate.of(2026, 11, 7)

    private val books = listOf(
        3 to ("Немили-недраги" to "Вазов, Иван"), 9 to ("Чичовци" to "Вазов, Иван"), 16 to ("Железният светилник" to "Талев, Димитър"),
        24 to ("Бай Ганьо" to "Константинов, Алеко"), 33 to ("Тютюн" to "Димов, Димитър"), 41 to ("История славянобългарска" to "Хилендарски, Паисий"),
        47 to ("Време разделно" to "Дончев, Антон"), 52 to ("Епопея на забравените" to "Вазов, Иван"), 58 to ("Преспанските камбани" to "Талев, Димитър"),
        66 to ("Кратка история на времето" to "Хокинг, Стивън"), 79 to ("Гераците" to "Елин Пелин"), 88 to ("Старопланински легенди" to "Йовков, Йордан"),
        101 to ("Записки по българските въстания" to "Захари Стоянов"), 109 to ("Илинден" to "Талев, Димитър"), 118 to ("Песента на колелетата" to "Йовков, Йордан"),
        126 to ("Под игото" to "Вазов, Иван"), 139 to ("Малкият принц" to "Сент-Екзюпери, Антоан дьо"), 151 to ("Осъдени души" to "Димов, Димитър"),
        163 to ("До Чикаго и назад" to "Константинов, Алеко"), 171 to ("Нова земя" to "Вазов, Иван"), 186 to ("Българските народни носии" to "Веселинова, Мария"),
        204 to ("Сто години самота" to "Маркес, Габриел Гарсия"), 219 to ("Бащи и деца" to "Тургенев, Иван"), 233 to ("Чичовци" to "Вазов, Иван"),
        251 to ("Хайдушки копнежи" to "Яворов, Пейо"), 268 to ("Нощем с белите коне" to "Вежинов, Павел"),
        283 to ("Престъпление и наказание" to "Достоевски, Фьодор"), 301 to ("Гласовете ви чувам" to "Яворов, Пейо"),
    )

    val history: List<HistoryItem> = books.mapIndexed { i, (days, b) ->
        val out = today.minusDays(days.toLong())
        HistoryItem("demo-y$i", null, b.first, b.second, out.toString(), minOf(out.plusDays(14L + i % 9), today).toString())
    }

    /** Разделите по УДК — както би ги дал каталогът (литература, история, природни науки, изкуство). */
    private val catalog: Map<String, CatalogBook> = history.associate { h ->
        val udc = when (h.title) {
            "История славянобългарска", "Записки по българските въстания" -> "94(497.2)"
            "Кратка история на времето" -> "52"
            "Българските народни носии" -> "391"
            else -> "821.163.2"
        }
        h.loanId to CatalogBook(inv = 1, author = h.author, title = h.title, udc = udc)
    }

    val year: ReadingYear.Summary = requireNotNull(ReadingYear.compute(history, catalog, today.year))

    /** Същата година с най-харесана книга от дневника (5 розетки). */
    val yearWithFavourite: ReadingYear.Summary = year.copy(favourite = ReadingYear.Favourite("Под игото", "Вазов, Иван", 5))

    val book = CatalogBook(inv = 1234, author = "Талев, Димитър", title = "Железният светилник", publisher = "Български писател", year = "1952", udc = "821.163.2-31")

    /** „Снимка“ без мрежа: залез над хълмове (градиенти) — за картите със снимка. */
    fun photo(width: Int = 1080, height: Int = 760): ImageBitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        val sky = Paint().apply { shader = LinearGradient(0f, 0f, 0f, height.toFloat(), 0xFF35507A.toInt(), 0xFFE8A86A.toInt(), Shader.TileMode.CLAMP) }
        c.drawRect(0f, 0f, width.toFloat(), height.toFloat(), sky)
        c.drawCircle(width * 0.7f, height * 0.55f, height * 0.12f, Paint().apply { color = 0xFFFFE2A8.toInt() })
        fun hill(y: Float, amp: Float, color: Long) {
            val p = Path().apply {
                moveTo(0f, height.toFloat()); lineTo(0f, y)
                var x = 0f
                while (x <= width) { lineTo(x, y - amp * kotlin.math.sin(x / width * 6.0).toFloat()); x += 20f }
                lineTo(width.toFloat(), height.toFloat()); close()
            }
            c.drawPath(p, Paint().apply { this.color = color.toInt(); isAntiAlias = true })
        }
        hill(height * 0.68f, height * 0.06f, 0xFF4A5D3A)
        hill(height * 0.8f, height * 0.05f, 0xFF2F3E26)
        return bmp.asImageBitmap()
    }
}
