package org.chyavorec.data

import org.chyavorec.data.site.AppMessagesParser
import org.chyavorec.domain.model.AppMessage
import org.chyavorec.domain.model.Inbox
import org.chyavorec.domain.model.MessageAudience
import org.chyavorec.domain.model.ReaderMessage
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Допълва AppMessagesTest и ReaderMessagesTest: граничните случаи на разчитането и общата кутия. */
class MessageInboxTest {

    @Test fun duplicateIdsKeepOneAndLongFieldsAreCut() {
        val text = """[
            {"id":"a","title":"${"Д".repeat(300)}","createdAt":"2026-10-01T08:00:00Z","audience":"MEMBERS"},
            {"id":"a","title":"Повторение","createdAt":"2026-10-02T08:00:00Z"},
            {"id":"  ","title":"Празно id","createdAt":"2026-10-02T08:00:00Z"},
            {"id":"b","title":"  ","createdAt":"2026-10-02T08:00:00Z"}
        ]"""
        val list = requireNotNull(AppMessagesParser.parse(text))
        assertEquals(listOf("a"), list.map { it.id })
        assertEquals(200, list.single().title.length)
        // Без текст — празно тяло; „MEMBERS“ — без значение на главните букви.
        assertEquals("", list.single().body)
        assertEquals(MessageAudience.MEMBERS, list.single().audience)
    }

    @Test fun brokenExpiryMeansNoExpiry() {
        val list = requireNotNull(AppMessagesParser.parse("""[{"id":"x","title":"Т","createdAt":"2026-10-01T08:00:00Z","expiresAt":"скоро"}]"""))
        assertNull(list.single().expiresOn)
        assertEquals(1, AppMessagesParser.visible(list, isMember = false, LocalDate.of(2030, 1, 1)).size)
    }

    @Test fun membersOnlyStayHiddenFromGuestsEvenWhenNewest() {
        val list = listOf(
            AppMessage("m", "Само за членове", "", MessageAudience.MEMBERS, createdAt = "2026-10-10T08:00:00Z"),
            AppMessage("a", "За всички", "", createdAt = "2026-10-01T08:00:00Z"),
        )
        val today = LocalDate.of(2026, 10, 17)
        assertEquals(listOf("a"), AppMessagesParser.visible(list, isMember = false, today).map { it.id })
        assertEquals(listOf("m", "a"), AppMessagesParser.visible(list, isMember = true, today).map { it.id })
    }

    @Test fun serverIdOnlyForPersonal() {
        assertEquals("7", Inbox.serverId("p:7"))
        assertNull(Inbox.serverId("p:"))
        assertNull(Inbox.serverId("msg-7"))
        assertEquals("p:7", Inbox.personalId("7"))
    }

    @Test fun mergeOrdersByDateAndPutsUndatedLast() {
        val site = listOf(
            AppMessage("s1", "Сайт", "", createdAt = "2026-10-05T08:00:00Z"),
            AppMessage("s2", "Без дата", "", createdAt = "не е дата"),
        )
        val personal = listOf(
            ReaderMessage("9", "Запазена книга", "Пристигна", "2026-10-06T08:00:00Z"),
            ReaderMessage("8", "", "По-старо", "2026-10-01T08:00:00Z", read = true),
        )
        val merged = Inbox.merge(site, personal)
        assertEquals(listOf("p:9", "s1", "p:8", "s2"), merged.map { it.id })
        assertTrue(merged.first().personal)
        assertEquals("Пристигна", merged.first().body)
        // Без лични — списъкът от сайта, както е.
        assertEquals(site, Inbox.merge(site, emptyList()))
    }
}
