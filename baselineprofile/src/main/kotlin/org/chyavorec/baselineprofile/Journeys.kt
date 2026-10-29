package org.chyavorec.baselineprofile

import android.util.Log
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import java.util.regex.Pattern

/*
 * Пътищата на потребителя за профила. Елементите се намират по видимия текст и
 * contentDescription (както ги вижда потребителят), на български ИЛИ английски —
 * приложението следва езика на системата (емулаторът в CI е en-US).
 *
 * Всяка стъпка е „толерантна“: ако елемент липсва (напр. няма мрежа или екранът е
 * променен), стъпката се пропуска с бележка в logcat, вместо целият профил да пропадне.
 * Профилът пак съдържа всичко, до което пътят е стигнал. Пътищата се пускат през
 * [optional] — и неочаквана грешка (напр. изчезнал елемент) спира само своя път.
 */

private const val TAG = "BaselineProfile"
private const val TIMEOUT = 10_000L
private const val CONTENT_TIMEOUT = 20_000L

/** Точно съвпадение с някой от текстовете (без значение от главни/малки букви). */
private fun exactly(vararg texts: String): Pattern = Pattern.compile(
    texts.joinToString("|", prefix = "^(", postfix = ")$") { Pattern.quote(it) },
    Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE,
)

/** Текстът съдържа някой от фрагментите. */
private fun containing(vararg parts: String): Pattern = Pattern.compile(
    parts.joinToString("|", prefix = "(?s).*(", postfix = ").*") { Pattern.quote(it) },
    Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE,
)

// Текстовете са от res/values (bg) и res/values-en (en).
private val SKIP = exactly("Пропусни", "Skip")
/** Обяснението за известията (Android 13+) при първо стартиране — „Не сега“. */
private val NOT_NOW = exactly("Не сега", "Not now")
/** Системният прозорец за разрешение (ако все пак се покаже) — „Не разрешавай“. */
private val DONT_ALLOW = exactly("Не разрешавам", "Не разрешавай", "Don’t allow", "Don't allow", "Deny")
private val HOME_SEARCH = containing("Търси книга", "Search books")
/** Заредено съдържание: каруселът с новини (contentDescription) или секция от списъка. */
private val HOME_CONTENT: List<BySelector> = listOf(
    By.desc(containing("Последни новини", "Latest news")),
    By.text(containing("Предстоящи събития", "Upcoming events", "Нови постъпления", "New arrivals", "Още новини", "More news")),
)
private val TAB_HOME = exactly("Начало", "Home")
private val TAB_CATALOG = exactly("Каталог", "Catalogue")
private val TAB_NEWS = exactly("Новини", "News")
private val TAB_MY = exactly("Моето", "My", "My library")
private val TAB_MORE = exactly("Още", "More")
private val BACK = exactly("Назад", "Back")
/** Сегментът „Календар“ в „Новини“ и бутоните за изгледа на събитията (contentDescription). */
private val SEGMENT_CALENDAR = exactly("Календар", "Calendar")
private val CALENDAR_VIEW = exactly("Изглед календар", "Calendar view")
private val LIST_VIEW = exactly("Изглед списък", "List view")
/** Отметката на страницата на книгата и плочката в „Моето“. */
private val WANT_TO_READ = exactly("Искам да прочета", "Want to read")
/** Плочката „Галерия“ в „Още“ — заглавието и описанието ѝ (то е само в „Още“). */
private val GALLERY = exactly("Галерия", "Gallery")
private val GALLERY_DESC = exactly("Снимки от сайта", "Photos from the website")
/** Броят снимки върху албума в галерията: „12 снимки“ / „12 photos“. */
private val PHOTOS_COUNT = Pattern.compile("^[0-9]+\\s+(снимк|photo).*", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)
private val MESSAGES = containing("Съобщения", "съобщени", "Messages", "message")
/** Заглавката над резултатите в каталога: „12 резултата · данни от InvLib към …“ / „12 results · InvLib data as of …“. */
private val CATALOG_RESULTS = Pattern.compile("^[1-9][0-9]*\\s+(резултат|result).*", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.DOTALL)
private val EDIT_TEXT: BySelector = By.clazz("android.widget.EditText")

private fun note(message: String) {
    Log.w(TAG, message)
}

/**
 * Път, който не бива да проваля целия профил: грешка (напр. StaleObjectException, когато
 * екранът се смени под ръка) се отбелязва в logcat и генерирането продължава със следващия.
 */
fun MacrobenchmarkScope.optional(name: String, journey: MacrobenchmarkScope.() -> Unit) {
    try {
        journey()
    } catch (e: Exception) {
        note("$name failed: $e")
    }
}

/** Повтаря [find] до [timeout] ms, докато върне нещо (изчезнал елемент — пак опит). */
private fun <T : Any> poll(timeout: Long, find: () -> T?): T? {
    val end = System.currentTimeMillis() + timeout
    while (true) {
        runCatching(find).getOrNull()?.let { return it }
        if (System.currentTimeMillis() >= end) return null
        Thread.sleep(250)
    }
}

private fun MacrobenchmarkScope.waitFor(selector: BySelector, timeout: Long = TIMEOUT): UiObject2? =
    device.wait(Until.findObject(selector), timeout)

/** Изчаква някой от [selectors]; true, ако се е появил до [timeout] ms. */
private fun MacrobenchmarkScope.waitForAny(selectors: List<BySelector>, timeout: Long): Boolean {
    val end = System.currentTimeMillis() + timeout
    while (System.currentTimeMillis() < end) {
        if (selectors.any { device.hasObject(it) }) return true
        Thread.sleep(250)
    }
    return selectors.any { device.hasObject(it) }
}

/** Плъзгане по екрана (превърта списъци, без да зависи от конкретен елемент). */
private fun MacrobenchmarkScope.swipeUp() {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h * 3 / 4, w / 2, h / 4, 20)
    device.waitForIdle()
}

private fun MacrobenchmarkScope.swipeDown() {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h / 4, w / 2, h * 3 / 4, 20)
    device.waitForIdle()
}

/** Бързо плъзгане с пръст нагоре (fling): списъкът продължава по инерция надолу. */
private fun MacrobenchmarkScope.flingUp() {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h * 4 / 5, w / 2, h / 5, 4)
    Thread.sleep(800)
    device.waitForIdle()
}

private fun MacrobenchmarkScope.flingDown() {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h / 5, w / 2, h * 4 / 5, 4)
    Thread.sleep(800)
    device.waitForIdle()
}

/** Търси [selector] на екрана, а ако го няма — превърта надолу до [swipes] пъти. */
private fun MacrobenchmarkScope.findScrolling(selector: BySelector, swipes: Int = 3): UiObject2? {
    repeat(swipes) {
        device.findObject(selector)?.let { return it }
        swipeUp()
    }
    return device.findObject(selector)
}

/** Долният край на заглавната лента („назад“); без нея — горната десета от екрана. */
private fun MacrobenchmarkScope.belowTopBar(): Int =
    device.findObject(By.desc(BACK))?.visibleBounds?.bottom ?: (device.displayHeight / 10)

/**
 * Най-големият докосваем елемент в съдържанието: под [belowY] и над долната лента (напр.
 * първата новина, събитие, албум). Чиповете и иконите са по-малки от картите и редовете.
 */
private fun MacrobenchmarkScope.largestClickableBelow(belowY: Int): UiObject2? {
    val navTop = device.findObjects(By.text(TAB_HOME)).maxByOrNull { it.visibleBounds.centerY() }?.visibleBounds?.top
        ?: device.displayHeight
    return device.findObjects(By.clickable(true))
        .filter { val b = it.visibleBounds; b.top >= belowY && b.bottom <= navTop && !b.isEmpty }
        .maxByOrNull { it.visibleBounds.width() * it.visibleBounds.height() }
}

/** Самият докосваем елемент на текст (етикетът на чип/плочка може да е вътрешен възел). */
private fun UiObject2.clickableAncestor(): UiObject2 {
    var node: UiObject2? = this
    repeat(4) {
        val n = node ?: return this
        if (n.isClickable || n.isCheckable) return n
        node = n.parent
    }
    return this
}

/** Връщане назад, докато се появи [target] (първото „назад“ може само да скрие клавиатурата). */
private fun MacrobenchmarkScope.backUntil(target: BySelector, maxPresses: Int = 3) {
    repeat(maxPresses) {
        if (device.hasObject(target)) return
        device.pressBack()
        device.waitForIdle()
        if (device.wait(Until.hasObject(target), 2_000) == true) return
    }
    note("backUntil: target not reached after $maxPresses presses")
}

/**
 * Затваря диалозите, които може да покрият екрана: обяснението за известията на
 * приложението и системния прозорец за разрешение. Без грешка, ако няма такива.
 */
private fun MacrobenchmarkScope.dismissDialogsIfShown(timeout: Long = 0) {
    if (timeout > 0) device.wait(Until.hasObject(By.text(NOT_NOW)), timeout)
    for (selector in listOf(By.text(NOT_NOW), By.text(DONT_ALLOW))) {
        val button = device.findObject(selector) ?: continue
        button.click()
        device.wait(Until.gone(selector), TIMEOUT)
        device.waitForIdle()
    }
}

/**
 * Докосва раздел от долната навигация. Етикетът може да се среща и другаде на екрана —
 * избира се най-долният (лентата е в дъното).
 */
private fun MacrobenchmarkScope.openTab(label: Pattern): Boolean {
    dismissDialogsIfShown()
    if (device.wait(Until.hasObject(By.text(label)), TIMEOUT) != true) {
        note("tab not found: $label")
        return false
    }
    val tab = device.findObjects(By.text(label)).maxByOrNull { it.visibleBounds.centerY() } ?: return false
    tab.click()
    device.waitForIdle()
    return true
}

/** Студен старт → (пропускане на въведението, ако се покаже) → начален екран със съдържание. */
fun MacrobenchmarkScope.coldStartToHome() {
    device.pressHome()
    startActivityAndWait()
    skipOnboardingIfShown()
    // Обяснението за известията се показва веднъж, след въведението.
    dismissDialogsIfShown(timeout = 3_000)
    if (waitFor(By.text(HOME_SEARCH), CONTENT_TIMEOUT) == null) {
        note("home screen not shown")
        return
    }
    // Съдържанието идва от мрежата/кеша — изчакваме първите секции (без грешка, ако ги няма).
    if (!waitForAny(HOME_CONTENT, CONTENT_TIMEOUT)) note("home content not loaded")
    device.waitForIdle()
}

/** Първо стартиране: въведението (5 екрана) — „Пропусни“, както би направил потребителят. */
private fun MacrobenchmarkScope.skipOnboardingIfShown() {
    // Началният екран или въведението — което се покаже първо.
    val first = By.text(Pattern.compile("${SKIP.pattern()}|${HOME_SEARCH.pattern()}", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE))
    device.wait(Until.hasObject(first), CONTENT_TIMEOUT)
    val skip = device.findObject(By.text(SKIP)) ?: return
    device.waitForIdle()
    skip.click()
    device.wait(Until.gone(By.text(SKIP)), TIMEOUT)
}

/** Каталог: списък → търсене (буква по буква) → книга → назад. */
fun MacrobenchmarkScope.catalogJourney() {
    if (!openTab(TAB_CATALOG)) return
    val field = waitFor(EDIT_TEXT)
    if (field == null) {
        note("catalog search field not found")
        return
    }
    // Каталогът (katalog.json) се зарежда от мрежата при първо отваряне.
    if (waitFor(By.text(CATALOG_RESULTS), CONTENT_TIMEOUT) == null) {
        note("catalog not loaded")
        return
    }
    swipeUp()
    swipeDown()

    // Търсене, както се пише — всяка буква минава през търсачката.
    val query = "Вазов"
    for (i in 1..query.length) {
        (device.findObject(EDIT_TEXT) ?: break).setText(query.substring(0, i))
        Thread.sleep(250)
    }
    if (waitFor(By.text(CATALOG_RESULTS), 5_000) == null) {
        // Няма такъв автор в каталога — отваряме първата книга от целия списък.
        device.findObject(EDIT_TEXT)?.setText("")
        waitFor(By.text(CATALOG_RESULTS), TIMEOUT)
    }
    device.waitForIdle()

    // Първата книга: най-горният докосваем ред под заглавката с броя резултати.
    val header = device.findObject(By.text(CATALOG_RESULTS))
    val book = header?.let { h ->
        device.findObjects(By.clickable(true))
            .filter { it.visibleBounds.top >= h.visibleBounds.bottom }
            .minByOrNull { it.visibleBounds.top }
    }
    if (book == null) {
        note("no book to open")
        return
    }
    book.click()
    device.wait(Until.gone(By.text(CATALOG_RESULTS)), TIMEOUT)
    device.waitForIdle()
    Thread.sleep(1_000)
    // В „Искам да прочета“ — списъкът в „Моето“ да не е празен (отметката остава и за
    // следващите повторения: вече отбелязана книга не се маха).
    optional("want to read on book page") { markWantToRead() }
    swipeUp()
    swipeDown()
    // Към каталога: полето за търсене е над списъка и винаги се вижда.
    backUntil(EDIT_TEXT)
}

private fun MacrobenchmarkScope.markWantToRead() {
    val label = findScrolling(By.text(WANT_TO_READ), swipes = 2) ?: return note("want-to-read chip not found")
    val chip = label.clickableAncestor()
    if (chip.isChecked || chip.isSelected) return
    chip.click()
    device.waitForIdle()
    Thread.sleep(500)
}

/** „Начало“: fling до долу и обратно — всички секции (редовете с книги, събития, новини). */
fun MacrobenchmarkScope.homeScrollJourney() {
    if (!device.hasObject(By.text(HOME_SEARCH)) && !openTab(TAB_HOME)) return
    repeat(3) { flingUp() }
    repeat(3) { flingDown() }
}

/** Глобално търсене от началния екран. */
fun MacrobenchmarkScope.globalSearchJourney() {
    if (!openTab(TAB_HOME)) return
    val pill = waitFor(By.text(HOME_SEARCH))
    if (pill == null) {
        note("home search not found")
        return
    }
    pill.click()
    val field = waitFor(EDIT_TEXT)
    if (field == null) {
        note("search field not found")
    } else {
        val query = "Ботев"
        for (i in 1..query.length) {
            (device.findObject(EDIT_TEXT) ?: break).setText(query.substring(0, i))
            Thread.sleep(250)
        }
        device.waitForIdle()
        Thread.sleep(2_000)
    }
    backUntil(By.text(HOME_SEARCH))
}

/** Съобщенията от читалището (звънчето в заглавката на началния екран). */
fun MacrobenchmarkScope.messagesJourney() {
    if (!device.hasObject(By.text(HOME_SEARCH)) && !openTab(TAB_HOME)) return
    val bell = waitFor(By.desc(MESSAGES))
    if (bell == null) {
        note("messages button not found")
        return
    }
    bell.click()
    device.waitForIdle()
    Thread.sleep(1_500)
    backUntil(By.text(HOME_SEARCH))
}

/**
 * „Новини“: превъртане → статия → назад → търсене (буква по буква) → „Календар“ (списък,
 * изглед календар, събитие) → обратно към „Новини“ (следващото повторение започва оттам).
 */
fun MacrobenchmarkScope.newsJourney() {
    if (!openTab(TAB_NEWS)) return
    if (waitFor(EDIT_TEXT) == null) {
        note("news search field not found")
        return
    }
    // Новините идват от сайта или от кеша — изчакваме първата.
    val first = poll(CONTENT_TIMEOUT) { device.findObject(EDIT_TEXT)?.let { largestClickableBelow(it.visibleBounds.bottom) } }
    if (first == null) {
        note("news not loaded")
    } else {
        swipeUp()
        swipeDown()
        optional("news article") { openNewsArticle() }
    }
    optional("news search") {
        val query = "Яворец"
        for (i in 1..query.length) {
            (device.findObject(EDIT_TEXT) ?: break).setText(query.substring(0, i))
            Thread.sleep(250)
        }
        device.waitForIdle()
        Thread.sleep(1_000)
        device.findObject(EDIT_TEXT)?.setText("")
        device.waitForIdle()
    }
    optional("calendar") { calendarSegment() }
    // Сегментът „Новини“ — горният от двата текста (другият е разделът в долната лента).
    device.findObjects(By.text(TAB_NEWS)).minByOrNull { it.visibleBounds.top }?.click()
    device.waitForIdle()
}

private fun MacrobenchmarkScope.openNewsArticle() {
    val field = device.findObject(EDIT_TEXT) ?: return
    val item = largestClickableBelow(field.visibleBounds.bottom) ?: return note("no news to open")
    item.click()
    if (device.wait(Until.gone(EDIT_TEXT), TIMEOUT) != true) return note("article not opened")
    // Текстът на статията се зарежда от сайта.
    Thread.sleep(2_000)
    swipeUp()
    swipeUp()
    swipeDown()
    backUntil(EDIT_TEXT)
}

private fun MacrobenchmarkScope.calendarSegment() {
    val segment = device.findObjects(By.text(SEGMENT_CALENDAR)).minByOrNull { it.visibleBounds.top }
        ?: return note("calendar segment not found")
    segment.click()
    // Полето за търсене е само в сегмента „Новини“.
    if (device.wait(Until.gone(EDIT_TEXT), TIMEOUT) != true) return note("calendar not shown")
    Thread.sleep(1_500)
    // Изгледът се помни — ако е останал „календар“, първо обратно към списъка.
    device.findObject(By.desc(LIST_VIEW))?.let { it.click(); device.wait(Until.hasObject(By.desc(CALENDAR_VIEW)), TIMEOUT) }
    swipeUp()
    swipeDown()
    // Изглед календар (месецът) и обратно.
    device.findObject(By.desc(CALENDAR_VIEW))?.let { toggle ->
        toggle.click()
        if (device.wait(Until.hasObject(By.desc(LIST_VIEW)), TIMEOUT) == true) {
            Thread.sleep(1_000)
            device.findObject(By.desc(LIST_VIEW))?.click()
            device.wait(Until.hasObject(By.desc(CALENDAR_VIEW)), TIMEOUT)
        }
        device.waitForIdle()
    }
    // Първото събитие от списъка.
    val top = device.findObjects(By.text(SEGMENT_CALENDAR)).minByOrNull { it.visibleBounds.top }?.visibleBounds?.bottom ?: return
    val event = largestClickableBelow(top) ?: return note("no event to open")
    event.click()
    if (device.wait(Until.gone(By.text(SEGMENT_CALENDAR)), TIMEOUT) != true) return note("event not opened")
    Thread.sleep(1_000)
    swipeUp()
    swipeDown()
    backUntil(By.text(SEGMENT_CALENDAR))
}

/** „Моето“ → „Искам да прочета“ (с книгата, отбелязана в каталога) → назад. */
fun MacrobenchmarkScope.wantToReadJourney() {
    if (!openTab(TAB_MY)) return
    val tile = findScrolling(By.text(WANT_TO_READ)) ?: return note("want-to-read tile not found")
    tile.click()
    device.waitForIdle()
    Thread.sleep(1_500)
    swipeUp()
    swipeDown()
    // Едно „назад“ — към „Моето“ (на екрана няма поле, т.е. и клавиатура).
    device.pressBack()
    device.waitForIdle()
}

/** „Моето“ без вход (гост). */
fun MacrobenchmarkScope.myGuestJourney() {
    if (!openTab(TAB_MY)) return
    Thread.sleep(1_000)
    swipeUp()
    swipeDown()
}

/** „Още“. */
fun MacrobenchmarkScope.moreJourney() {
    if (!openTab(TAB_MORE)) return
    Thread.sleep(1_000)
    swipeUp()
    swipeDown()
}

/** „Още“ → „Галерия“ → албум → снимка (и следващата) → назад толкова пъти, колкото екрана са отворени. */
fun MacrobenchmarkScope.galleryJourney() {
    if (!openTab(TAB_MORE)) return
    val tile = findScrolling(By.text(GALLERY)) ?: return note("gallery tile not found")
    tile.click()
    if (device.wait(Until.gone(By.text(GALLERY_DESC)), TIMEOUT) != true) return note("gallery not opened")
    var depth = 1
    try {
        // Албумите идват от сайта — изчакваме броя снимки върху първия.
        if (device.wait(Until.hasObject(By.text(PHOTOS_COUNT)), CONTENT_TIMEOUT) != true) return note("gallery not loaded")
        swipeUp()
        swipeDown()
        val album = largestClickableBelow(belowTopBar()) ?: return note("no album to open")
        album.click()
        if (device.wait(Until.gone(By.text(PHOTOS_COUNT)), TIMEOUT) != true) return note("album not opened")
        depth++
        // Първата снимка — горе вляво.
        val photo = poll(CONTENT_TIMEOUT) {
            val top = belowTopBar()
            device.findObjects(By.clickable(true))
                .filter { it.visibleBounds.top >= top && !it.visibleBounds.isEmpty }
                .minWithOrNull(compareBy<UiObject2>({ it.visibleBounds.top }, { it.visibleBounds.left }))
        } ?: return note("no photo to open")
        Thread.sleep(1_000)
        photo.click()
        device.waitForIdle()
        depth++
        Thread.sleep(1_500)
        // Следващата снимка (плъзгане настрани).
        device.findObject(By.scrollable(true))?.fling(Direction.RIGHT)
        Thread.sleep(1_000)
    } finally {
        repeat(depth) {
            device.pressBack()
            device.waitForIdle()
            Thread.sleep(300)
        }
    }
}
