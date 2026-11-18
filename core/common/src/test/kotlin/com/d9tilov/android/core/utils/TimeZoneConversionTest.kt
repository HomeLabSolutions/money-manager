package com.d9tilov.android.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.util.TimeZone as JavaTimeZone

class TimeZoneConversionTest {
    private val originalTimeZone = JavaTimeZone.getDefault()

    @After
    fun tearDown() {
        JavaTimeZone.setDefault(originalTimeZone)
    }

    @Test
    fun `default conversions use the system time zone in both directions`() {
        val localNoon = LocalDateTime(2026, 11, 18, 12, 0)
        val cases =
            mapOf(
                "Asia/Seoul" to "2026-11-18T03:00:00Z",
                "America/Los_Angeles" to "2026-11-18T20:00:00Z",
                "Europe/London" to "2026-11-18T12:00:00Z",
            )

        for ((zone, expectedInstant) in cases) {
            JavaTimeZone.setDefault(JavaTimeZone.getTimeZone(zone))
            val expectedMillis = Instant.parse(expectedInstant).toEpochMilli()

            assertEquals(expectedMillis, localNoon.toMillis())
            assertEquals(localNoon, expectedMillis.toLocalDateTime())
            assertEquals(localNoon.date, expectedMillis.toLocalDate())
        }
    }

    @Test
    fun `UTC calendar dates keep their day regardless of system zone or DST`() {
        val dates =
            listOf(
                LocalDate(2024, 2, 29),
                LocalDate(2026, 3, 8),
                LocalDate(2026, 11, 1),
                LocalDate(2026, 11, 18),
            )
        for (zone in listOf("Asia/Seoul", "America/Los_Angeles", "Europe/London")) {
            JavaTimeZone.setDefault(JavaTimeZone.getTimeZone(zone))
            for (date in dates) {
                val utcMidnight = Instant.parse("${date}T00:00:00Z").toEpochMilli()

                assertEquals(utcMidnight, date.getStartOfDay().toMillis(TimeZone.UTC))
                assertEquals(date, utcMidnight.toLocalDate(TimeZone.UTC))
                assertEquals(date.getStartOfDay(), utcMidnight.toLocalDateTime(TimeZone.UTC))
            }
        }
    }

    @Test
    fun `local day boundaries account for daylight saving transitions`() {
        JavaTimeZone.setDefault(JavaTimeZone.getTimeZone("America/Los_Angeles"))

        assertEquals(
            23 * 3_600_000L,
            LocalDateTime(2026, 3, 9, 0, 0).toMillis() - LocalDateTime(2026, 3, 8, 0, 0).toMillis(),
        )
        assertEquals(
            25 * 3_600_000L,
            LocalDateTime(2026, 11, 2, 0, 0).toMillis() - LocalDateTime(2026, 11, 1, 0, 0).toMillis(),
        )
    }
}
