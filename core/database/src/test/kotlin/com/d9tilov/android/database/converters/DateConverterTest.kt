package com.d9tilov.android.database.converters

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.TimeZone as JavaTimeZone

class DateConverterTest {
    private lateinit var originalTimeZone: JavaTimeZone

    @Before
    fun setUp() {
        originalTimeZone = JavaTimeZone.getDefault()
        JavaTimeZone.setDefault(JavaTimeZone.getTimeZone("GMT+03:00"))
    }

    @After
    fun tearDown() {
        JavaTimeZone.setDefault(originalTimeZone)
    }

    @Test
    fun `stores local date time as UTC epoch milliseconds`() {
        val localDateTime = LocalDateTime(2026, 9, 10, 12, 0)
        val expectedUtcMillis =
            localDateTime
                .toInstant(TimeZone.of("UTC+03:00"))
                .toEpochMilliseconds()

        assertEquals(expectedUtcMillis, DateConverter.fromOffsetDateTime(localDateTime))
    }

    @Test
    fun `reads UTC epoch milliseconds in current time zone`() {
        val expectedLocalDateTime = LocalDateTime(2026, 9, 10, 12, 0)
        val utcMillis =
            expectedLocalDateTime
                .toInstant(TimeZone.of("UTC+03:00"))
                .toEpochMilliseconds()

        assertEquals(expectedLocalDateTime, DateConverter.toOffsetDateTime(utcMillis))
    }
}
