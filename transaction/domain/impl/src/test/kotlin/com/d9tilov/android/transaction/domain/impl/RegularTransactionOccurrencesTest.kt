package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.core.model.ExecutionPeriod
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class RegularTransactionOccurrencesTest {
    @Test
    fun `daily includes both interval boundaries`() {
        val occurrences =
            occurrencesBetween(
                executionPeriod = ExecutionPeriod.EveryDay(),
                activeFrom = LocalDate(2024, 1, 1),
                from = LocalDate(2024, 1, 1),
                to = LocalDate(2024, 1, 30),
            )

        assertEquals(30, occurrences.size)
        assertEquals(LocalDate(2024, 1, 1), occurrences.first())
        assertEquals(LocalDate(2024, 1, 30), occurrences.last())
    }

    @Test
    fun `weekly includes occurrence on last day`() {
        val occurrences =
            occurrencesBetween(
                executionPeriod = ExecutionPeriod.EveryWeek(dayOfWeek = 0),
                activeFrom = LocalDate(2024, 1, 1),
                from = LocalDate(2024, 1, 2),
                to = LocalDate(2024, 1, 8),
            )

        assertEquals(listOf(LocalDate(2024, 1, 8)), occurrences)
    }

    @Test
    fun `created date excludes earlier weekly occurrences`() {
        val occurrences =
            occurrencesBetween(
                executionPeriod = ExecutionPeriod.EveryWeek(dayOfWeek = 0),
                activeFrom = LocalDate(2024, 1, 10),
                from = LocalDate(2024, 1, 1),
                to = LocalDate(2024, 1, 31),
            )

        assertEquals(
            listOf(LocalDate(2024, 1, 15), LocalDate(2024, 1, 22), LocalDate(2024, 1, 29)),
            occurrences,
        )
    }

    @Test
    fun `monthly day 31 uses last day of shorter month`() {
        val executionPeriod = ExecutionPeriod.EveryMonth(dayOfMonth = 31)

        assertEquals(
            listOf(LocalDate(2024, 2, 29)),
            occurrencesBetween(
                executionPeriod = executionPeriod,
                activeFrom = LocalDate(2024, 2, 1),
                from = LocalDate(2024, 2, 1),
                to = LocalDate(2024, 2, 29),
            ),
        )
        assertEquals(
            listOf(LocalDate(2023, 2, 28)),
            occurrencesBetween(
                executionPeriod = executionPeriod,
                activeFrom = LocalDate(2023, 2, 1),
                from = LocalDate(2023, 2, 1),
                to = LocalDate(2023, 2, 28),
            ),
        )
    }
}
