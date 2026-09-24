package com.d9tilov.android.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class DateTimeUtilTest {
    @Test
    fun `fiscal period starts on the last available day of the month`() {
        val cases =
            listOf(
                FiscalPeriodStartCase(LocalDate(2025, 2, 15), 31, LocalDate(2025, 1, 31)),
                FiscalPeriodStartCase(LocalDate(2025, 2, 28), 31, LocalDate(2025, 2, 28)),
                FiscalPeriodStartCase(LocalDate(2025, 2, 28), 30, LocalDate(2025, 2, 28)),
                FiscalPeriodStartCase(LocalDate(2025, 2, 28), 29, LocalDate(2025, 2, 28)),
                FiscalPeriodStartCase(LocalDate(2024, 2, 29), 31, LocalDate(2024, 2, 29)),
                FiscalPeriodStartCase(LocalDate(2025, 3, 1), 31, LocalDate(2025, 2, 28)),
                FiscalPeriodStartCase(LocalDate(2025, 4, 30), 31, LocalDate(2025, 4, 30)),
            )

        cases.forEach { case ->
            assertEquals(
                case.expectedStartDate,
                getStartDateOfFiscalPeriod(case.fiscalDay, case.currentDate).date,
            )
        }
    }

    @Test
    fun `fiscal period ends before the last available day of the next boundary`() {
        val cases =
            listOf(
                FiscalPeriodEndCase(
                    currentDate = LocalDateTime(2025, 2, 15, 0, 0),
                    fiscalDay = 31,
                    expectedEndDate = LocalDate(2025, 2, 27),
                ),
                FiscalPeriodEndCase(
                    currentDate = LocalDateTime(2025, 2, 28, 0, 0),
                    fiscalDay = 31,
                    expectedEndDate = LocalDate(2025, 3, 30),
                ),
                FiscalPeriodEndCase(
                    currentDate = LocalDateTime(2025, 2, 28, 0, 0),
                    fiscalDay = 30,
                    expectedEndDate = LocalDate(2025, 3, 29),
                ),
                FiscalPeriodEndCase(
                    currentDate = LocalDateTime(2025, 2, 28, 0, 0),
                    fiscalDay = 29,
                    expectedEndDate = LocalDate(2025, 3, 28),
                ),
                FiscalPeriodEndCase(
                    currentDate = LocalDateTime(2024, 2, 29, 0, 0),
                    fiscalDay = 31,
                    expectedEndDate = LocalDate(2024, 3, 30),
                ),
                FiscalPeriodEndCase(
                    currentDate = LocalDateTime(2025, 4, 30, 0, 0),
                    fiscalDay = 31,
                    expectedEndDate = LocalDate(2025, 5, 30),
                ),
            )

        cases.forEach { case ->
            assertEquals(
                case.expectedEndDate,
                case.currentDate.getEndDateOfFiscalPeriod(case.fiscalDay).date,
            )
        }
    }

    @Test
    fun `remaining days use the last available fiscal day of each month`() {
        val cases =
            listOf(
                RemainingDaysCase(LocalDateTime(2025, 2, 15, 0, 0), 31, 13),
                RemainingDaysCase(LocalDateTime(2025, 2, 28, 0, 0), 31, 31),
                RemainingDaysCase(LocalDateTime(2025, 2, 28, 0, 0), 30, 30),
                RemainingDaysCase(LocalDateTime(2025, 2, 28, 0, 0), 29, 29),
                RemainingDaysCase(LocalDateTime(2024, 2, 28, 0, 0), 31, 1),
                RemainingDaysCase(LocalDateTime(2024, 2, 29, 0, 0), 31, 31),
                RemainingDaysCase(LocalDateTime(2025, 4, 30, 0, 0), 31, 31),
            )

        cases.forEach { case ->
            assertEquals(
                case.expectedDays,
                case.currentDate.countDaysRemainingNextFiscalDate(case.fiscalDay),
            )
        }
    }
}

private data class FiscalPeriodStartCase(
    val currentDate: LocalDate,
    val fiscalDay: Int,
    val expectedStartDate: LocalDate,
)

private data class FiscalPeriodEndCase(
    val currentDate: LocalDateTime,
    val fiscalDay: Int,
    val expectedEndDate: LocalDate,
)

private data class RemainingDaysCase(
    val currentDate: LocalDateTime,
    val fiscalDay: Int,
    val expectedDays: Int,
)
