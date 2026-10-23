package org.chyavorec.baselineprofile

import android.util.Log
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
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
 * Профилът пак съдържа всичко, до което пътят е стигнал.
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
private val TAB_MY = exactly("Моето", "My")
private val TAB_MORE = exactly("Още", "More")
private val MESSAGES = containing("Съобщения", "съобщени", "Messages", "message")
/** Заглавката над резултатите в каталога: „12 резултата · данни от InvLib към …“ / „12 results · InvLib data as of …“. */
private val CATALOG_RESULTS = Pattern.compile("^[1-9][0-9]*\\s+(резултат|result).*", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.DOTALL)
private val EDIT_TEXT: BySelector = By.clazz("android.widget.EditText")

private fun note(message: String) {
    Log.w(TAG, message)
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
    swipeUp()
    swipeDown()
    // Към каталога: полето за търсене е над списъка и винаги се вижда.
    backUntil(EDIT_TEXT)
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
