package org.chyavorec.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.chyavorec.app.data.local.FavoriteNewsEntity
import org.chyavorec.app.data.local.FavoritesDao
import org.chyavorec.app.data.scan.BarcodeScanner
import org.chyavorec.app.data.scan.ScanOutcome
import org.chyavorec.app.data.security.BytesCipher
import org.chyavorec.app.data.security.SavedLogin
import org.chyavorec.app.data.security.SystemPasswords
import org.junit.rules.TestWatcher
import org.junit.runner.Description

object TestResources {
    fun text(name: String): String = requireNotNull(javaClass.classLoader.getResource(name)).readText()
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(dispatcher)
    override fun finished(description: Description) = Dispatchers.resetMain()
}

/** Тестов „шифър“ (XOR) — Robolectric няма Android Keystore. НЕ е за production. */
class XorTestCipher : BytesCipher {
    override fun encrypt(plain: ByteArray) = ByteArray(plain.size) { (plain[it].toInt() xor 0x5A).toByte() }
    override fun decrypt(data: ByteArray) = encrypt(data)
}

/**
 * Мениджър на пароли без системния прозорец: помни предложенията за запазване (и ги
 * „запазва“ — [saves]) и връща [saved].
 */
class FakeSystemPasswords(private val saved: SavedLogin? = null, private val saves: Boolean = true) : SystemPasswords {
    val offered = java.util.concurrent.CopyOnWriteArrayList<SavedLogin>()
    @Volatile var asked = 0
        private set
    override suspend fun offerSave(activity: android.content.Context, login: SavedLogin): Boolean { offered += login; return saves }
    override suspend fun savedLogin(activity: android.content.Context): SavedLogin? { asked++; return saved }
}

/** Скенер на баркодове без Google Play услуги: връща поред [outcomes] (последният — за всички следващи). */
class FakeBarcodeScanner(vararg outcomes: ScanOutcome) : BarcodeScanner {
    private val queue = java.util.concurrent.ConcurrentLinkedQueue(outcomes.toList())
    private val last: ScanOutcome = outcomes.lastOrNull() ?: ScanOutcome.Cancelled
    @Volatile var calls = 0
        private set
    override suspend fun scan(activity: android.content.Context): ScanOutcome {
        calls++
        return queue.poll() ?: last
    }
}

class InMemoryFavoritesDao : FavoritesDao {
    val items = MutableStateFlow<List<FavoriteNewsEntity>>(emptyList())
    override fun observeAll(): Flow<List<FavoriteNewsEntity>> = items
    override fun observeIds(): Flow<List<String>> = items.map { l -> l.map { it.id } }
    override suspend fun upsert(item: FavoriteNewsEntity) { items.value = items.value.filterNot { it.id == item.id } + item }
    override suspend fun delete(id: String) { items.value = items.value.filterNot { it.id == id } }
    override suspend fun clear() { items.value = emptyList() }
}
