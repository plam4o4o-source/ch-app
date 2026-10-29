package org.chyavorec.app.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/** Наблюдава дали устройството има интернет (за офлайн лентата). */
class ConnectivityObserver(context: Context) {
    private val cm = context.getSystemService(ConnectivityManager::class.java)

    fun isOnline(): Boolean {
        val caps = cm?.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    /**
     * Дали изобщо има мрежа с интернет — и непотвърдена (блокирана проверка на системата,
     * VPN, частен DNS, първите секунди след свързване), на която заявките често минават.
     * За „без мрежа — не чакай заявката“; [isOnline] е по-строгото „потвърден интернет“.
     */
    fun hasNetwork(): Boolean =
        cm?.getNetworkCapabilities(cm.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

    val online: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { trySend(isOnline()) }
            override fun onLost(network: Network) { trySend(isOnline()) }
            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) { trySend(isOnline()) }
        }
        trySend(isOnline())
        val request = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
        runCatching { cm?.registerNetworkCallback(request, callback) }
        awaitClose { runCatching { cm?.unregisterNetworkCallback(callback) } }
    }.distinctUntilChanged()

    /** Дали активната мрежа е с отчитане на трафика (мобилни данни, точка за достъп). */
    fun isMetered(): Boolean {
        val caps = cm?.getNetworkCapabilities(cm.activeNetwork) ?: return cm?.isActiveNetworkMetered ?: false
        return meteredOf(caps)
    }

    /** [isMetered], опреснявано при всяка смяна на мрежата по подразбиране (Wi-Fi ↔ мобилни данни). */
    val metered: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) { trySend(meteredOf(caps)) }
            override fun onLost(network: Network) { trySend(isMetered()) }
        }
        trySend(isMetered())
        runCatching { cm?.registerDefaultNetworkCallback(callback) }
        awaitClose { runCatching { cm?.unregisterNetworkCallback(callback) } }
    }.distinctUntilChanged()

    private fun meteredOf(caps: NetworkCapabilities): Boolean = CoverNetworkPolicy.metered(
        notMetered = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED),
        temporarilyNotMetered = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED),
    )
}

/**
 * Кога кориците (от Open Library и сканираните на библиотеката) може да се теглят от
 * мрежата. Иначе се показват само вече кешираните (паметта/диска на Coil) — без нито
 * една заявка — а останалите книги остават с генерираната корица.
 */
object CoverNetworkPolicy {
    /** Мрежата се отчита, освен ако е без ограничение (и временно — напр. 5G с неограничен план, Android 11+). */
    fun metered(notMetered: Boolean, temporarilyNotMetered: Boolean): Boolean = !notMetered && !temporarilyNotMetered

    /** „Корици само по Wi-Fi“ спира тегленето само по мрежа с отчитане на трафика. */
    fun networkAllowed(wifiOnly: Boolean, metered: Boolean): Boolean = !(wifiOnly && metered)
}
