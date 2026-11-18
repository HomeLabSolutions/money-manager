package com.d9tilov.android.insights.data.impl.mapper

import com.d9tilov.android.insights.domain.model.Insight
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.util.TimeZone

class InsightMapperTest {
    private val originalTimeZone = TimeZone.getDefault()

    @After
    fun tearDown() {
        TimeZone.setDefault(originalTimeZone)
    }

    @Test
    fun `both occurrences of a repeated DST hour preserve their timestamps`() {
        val timestamps =
            listOf(
                Instant.parse("2026-11-01T08:30:00Z").toEpochMilli(),
                Instant.parse("2026-11-01T09:30:00Z").toEpochMilli(),
                Instant.parse("2026-10-25T00:30:00Z").toEpochMilli(),
                Instant.parse("2026-10-25T01:30:00Z").toEpochMilli(),
            )

        for (zone in listOf("America/Los_Angeles", "Europe/London", "Asia/Seoul")) {
            TimeZone.setDefault(TimeZone.getTimeZone(zone))
            for (timestamp in timestamps) {
                val insight = Insight(id = 1L, createdAtMillis = timestamp, text = "Insight")
                val stored = insight.toDbModel("client-id")

                assertEquals(timestamp, stored.createdDate)
                assertEquals(insight, stored.toDomainModel())
            }
        }
    }
}
