package org.chyavorec.app.network

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** „Корици само по Wi-Fi“: кога кориците може да се теглят от мрежата. */
class CoverNetworkPolicyTest {

    @Test fun meteredUnlessUnmeteredOrTemporarilyUnmetered() {
        assertTrue(CoverNetworkPolicy.metered(notMetered = false, temporarilyNotMetered = false)) // мобилни данни
        assertFalse(CoverNetworkPolicy.metered(notMetered = true, temporarilyNotMetered = false)) // Wi-Fi
        assertFalse(CoverNetworkPolicy.metered(notMetered = false, temporarilyNotMetered = true)) // 5G с неограничен план
    }

    @Test fun wifiOnlyBlocksNetworkOnlyOnMeteredNetworks() {
        assertTrue(CoverNetworkPolicy.networkAllowed(wifiOnly = false, metered = true)) // по подразбиране — както досега
        assertTrue(CoverNetworkPolicy.networkAllowed(wifiOnly = false, metered = false))
        assertTrue(CoverNetworkPolicy.networkAllowed(wifiOnly = true, metered = false))
        assertFalse(CoverNetworkPolicy.networkAllowed(wifiOnly = true, metered = true))
    }
}
