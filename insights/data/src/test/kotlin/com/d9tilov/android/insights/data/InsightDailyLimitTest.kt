package com.d9tilov.android.insights.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class InsightDailyLimitTest {
    @Test
    fun `day starts at midnight UTC`() {
        val start = Instant.parse("2026-09-29T00:00:00Z").toEpochMilli()

        assertEquals(start, utcDayStartMillis(start))
        assertEquals(start, utcDayStartMillis(Instant.parse("2026-09-29T23:59:59Z").toEpochMilli()))
        assertEquals(start + 86_400_000L, utcDayStartMillis(start + 86_400_000L))
    }
}
