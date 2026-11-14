package org.chyavorec.baselineprofile

import android.graphics.Rect
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
/** Плочката „Галерия“ в „Още“. */
private val GALLERY = exactly("Галерия", "Gallery")
/** Броят снимки върху албума в галерията: „12 снимки“ / „12 photos“. */
private val PHOTOS_COUNT = Pattern.compile("^[0-9]+\\s+(снимк|photo).*", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)
private val MESSAGES = containing("Съобщения", "съобщени", "Messages", "message")
/** Заглавката над резултатите в каталога: „12 резултата · данни от InvLib към …“ / „12 results · InvLib data as of …“. */
private val CATALOG_RESULTS = Pattern.compile("^[1-9][0-9]*\\s+(резултат|result).*", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.DOTALL)
private val EDIT_TEXT: BySelector = By.clazz("android.widget.EditText")
/** Бързото действие „Читателска карта“ на „Начало“ и бутоните на екрана на картата. */
private val CARD = exactly("Читателска карта", "Library card")
private val CARD_SHOW = exactly("Покажи картата", "Show card")
private val SAVE = exactly("Запази", "Save")
private val CLOSE = exactly("Затвори", "Close")
/** Заровете в лентата на каталога (contentDescription) и „Още една“ под изтеглената карта. */
private val SURPRISE = exactly("Изненадай ме", "Surprise me")
private val SURPRISE_AGAIN = exactly("Още една", "Another one")
/** „Сподели“ в лентата на статията и „Сподели като картинка“ в листа с избор. */
private val SHARE = exactly("Сподели", "Share")
private val SHARE_IMAGE = exactly("Сподели като картинка", "Share as an image")
/** Плочките в „Още“ за вестника, лентата на времето, викторината и шевицата. */
private val NEWSPAPER = exactly("Старият вестник", "The old newspaper")
private val TIMELINE = exactly("Лента на времето", "Timeline")
private val QUIZ = exactly("Познай Яворец", "Know Yavorets")
private val EMBROIDERY = exactly("Извези шевица", "Embroider a pattern")
/** Страницата на статия от вестника (contentDescription на листа) и „Следваща страница“. */
private val PAPER_PAGE = containing("Щипни или докосни двукратно", "Pinch or double-tap to zoom")
private val NEXT_PAGE = exactly("Следваща страница", "Next page")
/** Платното на „Извези шевица“ (contentDescription). */
private val EMBROIDERY_CANVAS = containing("Платно за шевица", "Embroidery canvas")
/** Измислен номер за собствената карта (само на емулатора; Code 39 — главни букви и цифри). */
private const val PROFILE_CARD_NUMBER = "BP2026"

/** Броят бележки досега — път с нови бележки е стигнал само донякъде. */
private var notes = 0

private fun note(message: String) {
    notes++
    Log.w(TAG, message)
}

/**
 * Път, който не бива да проваля целия профил: грешка (напр. StaleObjectException, когато
 * екранът се смени под ръка) се отбелязва в logcat и генерирането продължава със следващия.
 *
 * Преди пътя приложението се връща на преден план, ако някой предишен е излязъл от него
 * (напр. едно „назад“ повече от раздел) — иначе всички следващи пътища пропадат тихо.
 * Резултатът на всеки път („journey …: ok / incomplete / failed“) е в logcat (CI го показва).
 */
fun MacrobenchmarkScope.optional(name: String, journey: MacrobenchmarkScope.() -> Unit) {
    val start = System.currentTimeMillis()
    val before = notes
    val result = try {
        ensureInApp(name)
        journey()
        if (notes == before) "ok" else "incomplete"
    } catch (e: Exception) {
        note("$name failed: $e")
        "failed"
    }
    val left = if (inApp()) "" else ", app left the foreground"
    Log.i(TAG, "journey $name: $result (${(System.currentTimeMillis() - start) / 1000} s$left)")
}

/** Приложението е на преден план (не началният екран на системата или друго приложение). */
private fun MacrobenchmarkScope.inApp(): Boolean = device.currentPackageName == packageName

/**
 * Ако приложението вече не е на преден план — пуска го отново (задачата му, ако още е жива)
 * и чака долната лента. Системен диалог за разрешение първо се затваря.
 */
private fun MacrobenchmarkScope.ensureInApp(name: String) {
    if (inApp()) return
    dismissDialogsIfShown()
    if (inApp()) return
    note("$name: app not in foreground (${device.currentPackageName}) — relaunching")
    try {
        startActivityAndWait()
    } catch (e: Exception) {
        note("relaunch failed: $e — via the launcher intent")
        device.pressHome()
        device.executeShellCommand("monkey -p $packageName -c android.intent.category.LAUNCHER 1")
    }
    dismissDialogsIfShown(timeout = 2_000)
    if (waitFor(tabSelector(TAB_HOME), CONTENT_TIMEOUT) == null) note("$name: bottom bar not shown after relaunch")
    device.waitForIdle()
}

/** Текст от самото приложение (не от системната лента или клавиатурата). */
private fun MacrobenchmarkScope.tabSelector(label: Pattern): BySelector = By.text(label).pkg(packageName)

/**
 * На основен раздел: долната лента се вижда и няма стрелка „назад“ (вътрешен екран или
 * разгънатото търсене на „Начало“). Тогава „назад“ вече би затворило приложението.
 */
private fun MacrobenchmarkScope.onTab(): Boolean =
    inApp() && device.hasObject(tabSelector(TAB_HOME)) && !device.hasObject(By.desc(BACK).pkg(packageName))

/** Клавиатурата е отворена (тогава първото „назад“ само я скрива). */
private fun MacrobenchmarkScope.keyboardShown(): Boolean =
    runCatching { device.executeShellCommand("dumpsys input_method").contains("mInputShown=true") }.getOrDefault(false)

/**
 * „Назад“, докато се стигне основен раздел ([onTab]) — и нито веднъж повече: на раздел
 * „назад“ затваря приложението. Не разчита на конкретен текст на екрана (напр. подсказката
 * в полето за търсене, която изчезва, ако полето пази последното търсене).
 */
private fun MacrobenchmarkScope.backToTab(maxPresses: Int = 4) {
    repeat(maxPresses) {
        if (onTab()) {
            if (keyboardShown()) { device.pressBack(); device.waitForIdle() }
            return
        }
        if (!inApp()) return note("backToTab: app left the foreground")
        device.pressBack()
        device.waitForIdle()
        poll(2_000) { onTab().takeIf { it } }
    }
    if (!onTab()) note("backToTab: no tab after $maxPresses presses")
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

/**
 * Плъзгане по екрана (превърта списъци, без да зависи от конкретен елемент). После се изчаква
 * списъкът да спре ([settle]): докосване на още движещ се списък само го спира — Compose не
 * праща щракване на елемента под пръста, т.е. екранът не се отваря (така пропадаше пътят до
 * „Искам да прочета“ от 1.2.36 нататък — плочката вече е под първия екран на „Моето“).
 */
private fun MacrobenchmarkScope.swipeUp(settle: Boolean = true) {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h * 3 / 4, w / 2, h / 4, 20)
    device.waitForIdle()
    if (settle) settle()
}

private fun MacrobenchmarkScope.swipeDown() {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h / 4, w / 2, h * 3 / 4, 20)
    device.waitForIdle()
    settle()
}

/** Бързо плъзгане с пръст нагоре (fling): списъкът продължава по инерция надолу. */
private fun MacrobenchmarkScope.flingUp() {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h * 4 / 5, w / 2, h / 5, 4)
    Thread.sleep(800)
    device.waitForIdle()
    settle()
}

private fun MacrobenchmarkScope.flingDown() {
    val w = device.displayWidth
    val h = device.displayHeight
    device.swipe(w / 2, h / 5, w / 2, h * 4 / 5, 4)
    Thread.sleep(800)
    device.waitForIdle()
    settle()
}

/** Местата на докосваемите елементи на приложението — за [settle]. */
private fun MacrobenchmarkScope.clickableBounds(): List<Rect>? =
    runCatching { device.findObjects(By.clickable(true).pkg(packageName)).map { it.visibleBounds } }.getOrNull()

/** Изчаква превъртането (инерцията) да спре: две поредни снимки на елементите са еднакви. */
private fun MacrobenchmarkScope.settle(maxMs: Long = 3_000) {
    val end = System.currentTimeMillis() + maxMs
    var last = clickableBounds()
    while (System.currentTimeMillis() < end) {
        Thread.sleep(200)
        val now = clickableBounds()
        if (now != null && now == last) return
        last = now
    }
}

/**
 * Търси [selector] на екрана, а ако го няма — превърта надолу до [swipes] пъти. Намереният след
 * превъртане елемент се взима наново, след като списъкът спре (в logcat — с колко се е мръднал
 * дотогава: толкова би сбъркало докосване веднага след намирането).
 */
private fun MacrobenchmarkScope.findScrolling(selector: BySelector, swipes: Int = 3): UiObject2? {
    device.findObject(selector)?.let { return it }
    repeat(swipes) { i ->
        swipeUp(settle = false)
        val moving = device.findObject(selector) ?: return@repeat
        val before = runCatching { moving.visibleBounds }.getOrNull()
        settle()
        val found = device.findObject(selector) ?: return@repeat
        Log.i(TAG, "found after ${i + 1} swipe(s); moved ${before?.let { kotlin.math.abs(it.top - found.visibleBounds.top) } ?: -1} px while the list settled")
        return found
    }
    settle()
    return device.findObject(selector)
}

/** Отворен е вътрешен екран (със стрелка „назад“ в заглавката) — не основен раздел. */
private fun MacrobenchmarkScope.innerScreenShown(): Boolean = inApp() && device.hasObject(By.desc(BACK).pkg(packageName))

/**
 * Докосва елемента от [find] (самия докосваем възел) и изчаква [opened]. Ако след [timeout] ms
 * екранът не се е сменил — още един опит (наново намерен елемент, след като всичко спре).
 * Неуспех е бележка, т.е. пътят е „incomplete“ в CI, не тихо „ok“.
 */
private fun MacrobenchmarkScope.tapAndWait(
    what: String,
    timeout: Long = TIMEOUT,
    opened: () -> Boolean = { innerScreenShown() },
    find: () -> UiObject2?,
): Boolean {
    repeat(2) { attempt ->
        settle()
        val target = runCatching(find).getOrNull()?.clickableAncestor()
        if (target == null) {
            note("$what: not found")
            return false
        }
        target.click()
        if (poll(timeout) { opened().takeIf { it } } != null) {
            if (attempt > 0) Log.i(TAG, "$what: opened on the second tap")
            device.waitForIdle()
            return true
        }
        Log.w(TAG, "$what: tap ${attempt + 1} did not open the screen")
    }
    note("$what: not opened")
    return false
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

/**
 * Връщане назад, докато се появи [target] (първото „назад“ може само да скрие клавиатурата).
 * Спира и на основен раздел — следващото „назад“ би затворило приложението.
 */
private fun MacrobenchmarkScope.backUntil(target: BySelector, maxPresses: Int = 3) {
    repeat(maxPresses) {
        if (device.hasObject(target)) return
        if (onTab() && !keyboardShown()) return note("backUntil: on a tab, target not shown")
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
    // Клавиатурата покрива долната лента — докосването би паднало върху нея.
    if (keyboardShown()) {
        device.pressBack()
        device.waitForIdle()
    }
    if (device.wait(Until.hasObject(By.text(label)), TIMEOUT) != true) {
        note("tab not found: $label")
        return false
    }
    val tab = device.findObjects(By.text(label)).maxByOrNull { it.visibleBounds.centerY() } ?: return false
    tab.click()
    device.waitForIdle()
    // Повторно докосване на отворения раздел превърта съдържанието му догоре (анимирано) —
    // елемент, намерен по време на това превъртане, после не е там.
    settle()
    return true
}

/** Студен старт → (пропускане на въведението, ако се покаже) → начален екран със съдържание. */
fun MacrobenchmarkScope.coldStartToHome() {
    device.pressHome()
    startActivityAndWait()
    skipOnboardingIfShown()
    // Обяснението за известията се показва веднъж, след въведението.
    dismissDialogsIfShown(timeout = 3_000)
    // Долната лента, не подсказката в полето за търсене (тя липсва, ако полето пази текст).
    if (waitFor(tabSelector(TAB_HOME), CONTENT_TIMEOUT) == null) {
        note("home screen not shown")
        return
    }
    // Съдържанието идва от мрежата/кеша — изчакваме първите секции (без грешка, ако ги няма).
    if (!waitForAny(HOME_CONTENT, CONTENT_TIMEOUT)) note("home content not loaded")
    device.waitForIdle()
}

/** Първо стартиране: въведението (3 екрана) — „Пропусни“ горе вдясно, както би направил потребителят. */
private fun MacrobenchmarkScope.skipOnboardingIfShown() {
    // Началният екран (долната лента) или въведението — което се покаже първо.
    val first = By.text(Pattern.compile("${SKIP.pattern()}|${TAB_HOME.pattern()}", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE))
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
    // Подсказката в полето; ако полето пази предишно търсене — самото поле.
    val pill = waitFor(By.text(HOME_SEARCH)) ?: device.findObject(EDIT_TEXT)
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
    // Обратно към „Начало“ по долната лента: полето може да пази „Ботев“ вместо подсказката.
    backToTab()
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
    backToTab()
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
    largestClickableBelow(field.visibleBounds.bottom) ?: return note("no news to open")
    if (!tapAndWait("article") { device.findObject(EDIT_TEXT)?.let { largestClickableBelow(it.visibleBounds.bottom) } }) return
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
    // Първото събитие от списъка → екранът му (листът от календара, EventLeaf).
    val firstEvent = {
        device.findObjects(By.text(SEGMENT_CALENDAR)).minByOrNull { it.visibleBounds.top }?.visibleBounds?.bottom
            ?.let { largestClickableBelow(it) }
    }
    firstEvent() ?: return note("no event to open")
    if (!tapAndWait("event", find = firstEvent)) return backUntil(By.text(SEGMENT_CALENDAR))
    Thread.sleep(1_500)
    swipeUp()
    swipeDown()
    backUntil(By.text(SEGMENT_CALENDAR))
}

/** „Моето“ → „Искам да прочета“ (с книгата, отбелязана в каталога) → назад. */
fun MacrobenchmarkScope.wantToReadJourney() {
    if (!openTab(TAB_MY)) return
    // В „Моето“ има повече редове (печат, дневник, шевица) — плочката е по-надолу.
    if (findScrolling(By.text(WANT_TO_READ), swipes = 7) == null) return note("want-to-read tile not found")
    try {
        if (!tapAndWait("want-to-read screen") { device.findObject(By.text(WANT_TO_READ)) }) return
        Thread.sleep(1_500)
        swipeUp()
        swipeDown()
    } finally {
        // Към „Моето“.
        backToTab()
    }
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

/** „Още“ → „Галерия“ → албум → снимка (и следващата) → назад до „Още“. */
fun MacrobenchmarkScope.galleryJourney() {
    if (!openTab(TAB_MORE)) return
    findScrolling(By.text(GALLERY)) ?: return note("gallery tile not found")
    try {
        if (!tapAndWait("gallery") { device.findObject(By.text(GALLERY)) }) return
        // Албумите идват от сайта — изчакваме броя снимки върху първия.
        if (device.wait(Until.hasObject(By.text(PHOTOS_COUNT)), CONTENT_TIMEOUT) != true) return note("gallery not loaded")
        swipeUp()
        swipeDown()
        val album = largestClickableBelow(belowTopBar()) ?: return note("no album to open")
        album.click()
        if (device.wait(Until.gone(By.text(PHOTOS_COUNT)), TIMEOUT) != true) return note("album not opened")
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
        Thread.sleep(1_500)
        // Следващата снимка (плъзгане настрани).
        device.findObject(By.scrollable(true))?.fling(Direction.RIGHT)
        Thread.sleep(1_000)
    } finally {
        backToTab()
    }
}

/** Плочка в „Още“ → нейният екран; false (с бележка), ако не се е отворил. */
private fun MacrobenchmarkScope.openMoreTile(label: Pattern, what: String): Boolean {
    if (!openTab(TAB_MORE)) return false
    // Новите плочки са в долната част на мрежата.
    findScrolling(By.text(label), swipes = 5) ?: run {
        note("$what tile not found")
        return false
    }
    return tapAndWait(what) { device.findObject(By.text(label)) }
}

/** Най-горната широка (карта, ред) докосваема част на съдържанието под [belowY]. */
private fun MacrobenchmarkScope.topWideClickableBelow(belowY: Int): UiObject2? {
    val navTop = device.findObjects(By.text(TAB_HOME)).maxByOrNull { it.visibleBounds.centerY() }?.visibleBounds?.top
        ?: device.displayHeight
    return device.findObjects(By.clickable(true).pkg(packageName))
        .filter { val b = it.visibleBounds; b.top >= belowY && b.bottom <= navTop && b.width() >= device.displayWidth * 2 / 3 }
        .minByOrNull { it.visibleBounds.top }
}

/**
 * „Още“ → „Старият вестник“ (списъкът със статии от сайта) → първата статия (PDF-ът се тегли
 * и рисува; следващата страница, ако има) → назад до „Още“.
 */
fun MacrobenchmarkScope.newspaperJourney() {
    try {
        if (!openMoreTile(NEWSPAPER, "newspaper")) return
        // Статиите идват от сайта; „шапката“ на вестника може да заема целия първи екран.
        val firstArticle = { topWideClickableBelow(belowTopBar()) }
        if (poll(CONTENT_TIMEOUT) { firstArticle() } == null) {
            swipeUp()
            if (firstArticle() == null) return note("newspaper not loaded")
        }
        swipeUp()
        swipeDown()
        if (poll(TIMEOUT) { firstArticle() } == null) swipeUp()
        // Списъкът винаги показва „Старият вестник“ (в „шапката“ или в заглавката), четецът — заглавието на статията.
        val readerShown = { !device.hasObject(By.text(NEWSPAPER).pkg(packageName)) }
        if (!tapAndWait("newspaper article", opened = readerShown, find = firstArticle)) return
        // Изтеглянето и първата страница.
        if (waitFor(By.desc(PAPER_PAGE).pkg(packageName), CONTENT_TIMEOUT) == null) return note("newspaper page not shown")
        Thread.sleep(1_500)
        device.findObject(By.desc(NEXT_PAGE).pkg(packageName))?.let {
            it.click()
            device.waitForIdle()
            Thread.sleep(1_500)
        }
    } finally {
        backToTab()
    }
}

/** „Още“ → „Лента на времето“ → до долу и обратно → назад. */
fun MacrobenchmarkScope.timelineJourney() {
    try {
        if (!openMoreTile(TIMELINE, "timeline")) return
        // Вехите идват от страницата „История“ на сайта (или от кеша).
        Thread.sleep(2_000)
        repeat(2) { flingUp() }
        repeat(2) { flingDown() }
    } finally {
        backToTab()
    }
}

/** „Още“ → „Познай Яворец“ — само отваряне (без отговори: серията и резултатът остават непокътнати). */
fun MacrobenchmarkScope.quizJourney() {
    try {
        if (!openMoreTile(QUIZ, "quiz")) return
        // Въпросите се съставят от каталога, събитията и снимките (или „Днес няма викторина“).
        Thread.sleep(2_500)
        swipeUp()
        swipeDown()
    } finally {
        backToTab()
    }
}

/** „Още“ → „Извези шевица“ → един бод (следващото повторение го маха) → назад. */
fun MacrobenchmarkScope.embroideryJourney() {
    try {
        if (!openMoreTile(EMBROIDERY, "embroidery")) return
        val canvas = waitFor(By.desc(EMBROIDERY_CANVAS).pkg(packageName)) ?: return note("embroidery canvas not found")
        Thread.sleep(500)
        canvas.click()
        device.waitForIdle()
        Thread.sleep(1_000)
    } finally {
        backToTab()
    }
}

/**
 * Читателската карта без вход: „Начало“ → „Читателска карта“ → (първия път — ръчно въведена
 * карта, както би я добавил читател без профил; остава и за следващите повторения) → „Покажи
 * картата“ → цял екран → обръщане и обратно → затваряне → към „Начало“.
 */
fun MacrobenchmarkScope.cardJourney() {
    if (!openTab(TAB_HOME)) return
    val label = findScrolling(By.text(CARD).pkg(packageName), swipes = 2)
        ?: run { repeat(2) { swipeDown() }; device.findObject(By.text(CARD).pkg(packageName)) }
        ?: return note("card quick action not found")
    label.clickableAncestor().click()
    // Картата вече е добавена („Покажи картата“) или формата за нея (полето за номера).
    try {
        val ready = poll(TIMEOUT) { device.findObject(By.text(CARD_SHOW)) ?: device.findObject(EDIT_TEXT) }
            ?: return note("card screen not shown")
        if (!device.hasObject(By.text(CARD_SHOW))) {
            // Първото поле — номерът (второто, името, е по желание).
            (device.findObjects(EDIT_TEXT).minByOrNull { it.visibleBounds.top } ?: ready).setText(PROFILE_CARD_NUMBER)
            device.waitForIdle()
            // Клавиатурата може да покрива „Запази“.
            if (keyboardShown()) { device.pressBack(); device.waitForIdle() }
            val save = device.findObject(By.text(SAVE).pkg(packageName)) ?: return note("card save button not found")
            save.clickableAncestor().click()
            if (waitFor(By.text(CARD_SHOW)) == null) return note("self card not saved")
        }
        Thread.sleep(1_000)
        swipeUp()
        swipeDown()
        if (!tapAndWait("fullscreen card", opened = { device.hasObject(By.desc(CLOSE).pkg(packageName)) }) { device.findObject(By.text(CARD_SHOW)) }) return
        // Влизането (3D обръщане) и яркостта.
        Thread.sleep(1_500)
        // Обръщане към гърба и обратно. Самата карта е най-големият докосваем елемент (освен нея
        // е само „Затвори“).
        repeat(2) {
            val card = device.findObjects(By.clickable(true).pkg(packageName))
                .filter { !it.visibleBounds.isEmpty }
                .maxByOrNull { it.visibleBounds.width() * it.visibleBounds.height() }
                ?: return note("card not found on the fullscreen screen")
            card.click()
            Thread.sleep(1_200)
        }
        device.findObject(By.desc(CLOSE).pkg(packageName))?.click() ?: device.pressBack()
        device.wait(Until.hasObject(By.text(CARD_SHOW)), TIMEOUT)
        device.waitForIdle()
    } finally {
        backToTab()
    }
}

/**
 * „Изненадай ме“ (заровете в каталога): разбъркване → обърнатата карта → „Още една“ →
 * затваряне. Каталогът трябва да е зареден (иначе — само „няма предложение“).
 */
fun MacrobenchmarkScope.surpriseJourney() {
    if (!openTab(TAB_CATALOG)) return
    if (waitFor(By.text(CATALOG_RESULTS), CONTENT_TIMEOUT) == null) return note("catalog not loaded for surprise")
    // Клавиатурата (от търсенето) покрива част от екрана.
    if (keyboardShown()) { device.pressBack(); device.waitForIdle() }
    val dice = waitFor(By.desc(SURPRISE).pkg(packageName)) ?: return note("surprise button not found")
    dice.click()
    // Разбъркването трае около 2–3 s, после картата се обръща.
    if (waitFor(By.text(SURPRISE_AGAIN), CONTENT_TIMEOUT) == null) {
        note("surprise result not shown")
    } else {
        Thread.sleep(1_000)
        device.findObject(By.text(SURPRISE_AGAIN))?.clickableAncestor()?.click()
        device.waitForIdle()
        Thread.sleep(3_500)
    }
    device.findObject(By.desc(CLOSE).pkg(packageName))?.click() ?: device.pressBack()
    device.wait(Until.gone(By.text(SURPRISE_AGAIN)), TIMEOUT)
    device.waitForIdle()
    backToTab()
}

/**
 * Споделяне на новина: статия → „Сподели“ → листът с избор (умалената карта) → „Сподели
 * като картинка“ (рисуването на картата с QR кода) → системният избор на приложение се
 * затваря с „назад“, без да се изпраща нищо → към „Новини“.
 */
fun MacrobenchmarkScope.shareJourney() {
    if (!openTab(TAB_NEWS)) return
    val first = poll(CONTENT_TIMEOUT) { device.findObject(EDIT_TEXT)?.let { largestClickableBelow(it.visibleBounds.bottom) } }
        ?: return note("news not loaded for share")
    first.click()
    if (device.wait(Until.gone(EDIT_TEXT), TIMEOUT) != true) return note("article not opened for share")
    try {
        Thread.sleep(1_500)
        val share = waitFor(By.desc(SHARE).pkg(packageName)) ?: return note("share button not found")
        share.click()
        if (waitFor(By.text(SHARE_IMAGE), 5_000) == null) {
            // Без картинка — направо системният избор (споделяне на връзката).
            note("share sheet not shown")
        } else {
            Thread.sleep(1_000)
            device.findObject(By.text(SHARE_IMAGE))?.clickableAncestor()?.click()
            // Картинката се рисува, после се отваря системният избор (друго приложение).
            poll(CONTENT_TIMEOUT) { (!inApp()).takeIf { it } } ?: note("share chooser not shown")
        }
        Thread.sleep(1_500)
        // Затваряне на системния избор — нищо не се изпраща.
        repeat(3) {
            if (inApp()) return@repeat
            device.pressBack()
            device.waitForIdle()
            poll(2_000) { inApp().takeIf { it } }
        }
        if (!inApp()) note("share chooser not closed")
    } finally {
        if (inApp()) backUntil(EDIT_TEXT)
    }
}
