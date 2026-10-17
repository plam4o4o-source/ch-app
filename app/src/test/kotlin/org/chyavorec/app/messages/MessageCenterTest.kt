package org.chyavorec.app.messages

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.chyavorec.app.TestBackend
import org.chyavorec.app.di.AppContainer
import org.chyavorec.core.FixedClock
import org.chyavorec.core.Outcome
import org.chyavorec.core.Synced
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.Inbox
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Съобщенията в приложението ([MessageCenter]): кой вижда „само за членове“ (карта или
 * вход), броят непрочетени и „прочетено“, личните съобщения само при вход — и
 * „прочетено“ за тях до сървъра, с повторен опит при неуспех.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class MessageCenterTest {
    private val app get() = ApplicationProvider.getApplicationContext<Application>()
    private val backend = TestBackend()
    private val clock = FixedClock(Instant.parse("2026-10-17T10:00:00Z"))
    /** Уникални за теста — прочетените (DataStore) се споделят между тестовете в процеса. */
    private val run = UUID.randomUUID().toString().take(8)
    private val forAll = "all-$run"
    private val forMembers = "members-$run"
    private lateinit var c: AppContainer

    @Before fun setUp() {
        backend.start()
        TestBackend.allowNotificationsAndNetwork(app)
        backend.reader()
        backend.json(
            "/data/app-messages.json",
            """[
              {"id":"$forAll","title":"Ново работно време","body":"От понеделник","audience":"all","createdAt":"2026-10-15T08:00:00Z"},
              {"id":"$forMembers","title":"Общо събрание","body":"Само за членове","audience":"members","createdAt":"2026-10-16T08:00:00Z"}
            ]""",
        )
        backend.json("${TestBackend.API}/v1/me/messages", """{"items":[]}""")
        c = backend.container(app, clock)
    }

    @After fun tearDown() = backend.shutdown()

    private fun ids(r: Outcome<Synced<List<AppMessage>>>) = (r as Outcome.Success).value.data.map { it.id }

    private fun signIn() = runBlocking {
        assertTrue(c.authRepository.login("0042", "1234".toCharArray(), remember = false) is Outcome.Success)
    }

    @Test fun membersOnlyNeedCardOrSignIn() = runBlocking {
        assertFalse(c.messages.isMember())
        assertEquals(listOf(forAll), ids(c.messages.refresh(force = true)))
        // Въведена читателска карта — проверката е на телефона, нищо не се праща към сайта.
        assertTrue(c.selfCardRepository.save("A-0042", ""))
        assertTrue(c.messages.isMember())
        assertEquals(listOf(forMembers, forAll), ids(c.messages.refresh(force = false)))
        assertEquals(1, backend.count("/data/app-messages.json"))
        c.selfCardRepository.clear()
        assertEquals(listOf(forAll), ids(c.messages.refresh(force = false)))
        // Вход — също член.
        signIn()
        assertTrue(c.messages.isMember())
    }

    @Test fun unreadCountFollowsReadState() = runBlocking {
        c.selfCardRepository.save("A-0042", "")
        c.messages.refresh(force = true)
        assertEquals(2, c.messages.unreadCount.first())
        c.messages.markRead(listOf(forMembers))
        assertEquals(1, c.messages.unreadCount.first())
        assertTrue(forMembers in c.messages.readIds.first())
        c.messages.markRead(listOf(forAll, forMembers))
        assertEquals(0, c.messages.unreadCount.first())
        // Съобщенията от сайта нямат „прочетено“ на сървъра.
        assertEquals(0, backend.requests.count { it.method == "POST" })
    }

    @Test fun personalMessagesOnlyWhenSignedInAndReadIsSent() = runBlocking {
        val pm = "pm-$run"
        backend.json(
            "${TestBackend.API}/v1/me/messages",
            """{"items":[{"id":"$pm","title":"Запазена книга","text":"Книгата те чака","at":"2026-10-17T09:00:00Z","read":false}]}""",
        )
        backend.json("${TestBackend.API}/v1/me/messages/$pm/read", "", code = 202)
        // Без вход — само съобщенията от сайта, без заявки към моста за лични.
        c.messages.refresh(force = true)
        assertEquals(0, backend.count("${TestBackend.API}/v1/me/messages"))
        signIn()
        val list = ids(c.messages.refresh(force = true))
        assertEquals(Inbox.personalId(pm), list.first())
        assertEquals(3, c.messages.unreadCount.first())

        c.messages.markRead(listOf(Inbox.personalId(pm)))
        assertEquals(1, backend.count("${TestBackend.API}/v1/me/messages/$pm/read"))
        assertTrue(c.settings.pendingPersonalReads().isEmpty())
        assertEquals(2, c.messages.unreadCount.first())

        // Изход — личните изчезват веднага, и от броя.
        c.authRepository.logout()
        val after = ids(c.messages.refresh(force = false))
        assertTrue(after.none { it.startsWith(Inbox.PERSONAL_PREFIX) })
        assertTrue(c.messages.messages.value.none { it.personal })
    }

    @Test fun failedReadIsRetriedOnNextRefresh() = runBlocking {
        val pm = "pm-$run"
        backend.json(
            "${TestBackend.API}/v1/me/messages",
            """{"items":[{"id":"$pm","text":"Напомняне","at":"2026-10-17T09:00:00Z","read":false}]}""",
        )
        var fail = true
        backend.routes["${TestBackend.API}/v1/me/messages/$pm/read"] = {
            if (fail) MockResponse().setResponseCode(503) else MockResponse().setResponseCode(202)
        }
        signIn()
        c.messages.refresh(force = true)
        c.messages.markRead(listOf(Inbox.personalId(pm)))
        // Прочетено на телефона веднага; до сървъра — при следващото опресняване.
        assertTrue(Inbox.personalId(pm) in c.messages.readIds.first())
        assertEquals(setOf(pm), c.settings.pendingPersonalReads())
        fail = false
        c.messages.refresh(force = true)
        assertEquals(2, backend.count("${TestBackend.API}/v1/me/messages/$pm/read"))
        assertTrue(c.settings.pendingPersonalReads().isEmpty())
    }

    @Test fun readOnAnotherDeviceCountsAsRead() = runBlocking {
        val pm = "pm-$run"
        backend.json(
            "${TestBackend.API}/v1/me/messages",
            """{"items":[{"id":"$pm","text":"Вече видяно","at":"2026-10-17T09:00:00Z","read":true}]}""",
        )
        signIn()
        c.messages.refresh(force = true)
        assertTrue(Inbox.personalId(pm) in c.messages.readIds.first())
        // Двете от сайта (вход = член) остават непрочетени.
        assertEquals(2, c.messages.unreadCount.first())
    }
}
