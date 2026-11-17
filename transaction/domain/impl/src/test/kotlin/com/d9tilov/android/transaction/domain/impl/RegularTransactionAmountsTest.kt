package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class RegularTransactionAmountsTest {
    private val annualTransaction =
        RegularTransaction.EMPTY.copy(
            sum = BigDecimal("3650"),
            createdDate = LocalDate(2023, 1, 1).getStartOfDay(),
            executionPeriod = ExecutionPeriod.EveryYear(month = 12, dayOfMonth = 1),
        )

    @Test
    fun `annual amount accrues even when payment falls outside fiscal month`() {
        assertAmount("310", annualTransaction, LocalDate(2023, 7, 15), LocalDate(2023, 8, 14))
    }

    @Test
    fun `annual amount is spread across 366 days in leap year`() {
        val transaction = annualTransaction.copy(sum = BigDecimal("3660"))
        assertAmount("290", transaction, LocalDate(2024, 2, 1), LocalDate(2024, 2, 29))
        assertAmount("10", transaction, LocalDate(2024, 2, 29), LocalDate(2024, 2, 29))
    }

    @Test
    fun `full calendar year preserves annual amount without rounding loss`() {
        val transaction = annualTransaction.copy(sum = BigDecimal("100"))
        assertAmount("100", transaction, LocalDate(2023, 1, 1), LocalDate(2023, 12, 31))
        assertAmount("100", transaction, LocalDate(2024, 1, 1), LocalDate(2024, 12, 31))
    }

    @Test
    fun `fiscal period spanning years uses each years daily share`() {
        val transaction = annualTransaction.copy(sum = BigDecimal("133590"))
        assertAmount("731", transaction, LocalDate(2023, 12, 31), LocalDate(2024, 1, 1))
    }

    @Test
    fun `annual allocation starts on creation date`() {
        val transaction = annualTransaction.copy(createdDate = LocalDate(2023, 7, 20).getStartOfDay())
        assertAmount("120", transaction, LocalDate(2023, 7, 1), LocalDate(2023, 7, 31))
        assertAmount("0", transaction, LocalDate(2023, 6, 1), LocalDate(2023, 6, 30))
    }

    @Test
    fun `weekly and monthly schedules still count whole payments`() {
        val weekly = annualTransaction.copy(executionPeriod = ExecutionPeriod.EveryWeek(dayOfWeek = 0))
        val monthly = annualTransaction.copy(executionPeriod = ExecutionPeriod.EveryMonth(dayOfMonth = 15))
        assertAmount("14600", weekly, LocalDate(2023, 2, 1), LocalDate(2023, 2, 28))
        assertAmount("3650", monthly, LocalDate(2023, 2, 1), LocalDate(2023, 2, 28))
    }

    private fun assertAmount(
        expected: String,
        transaction: RegularTransaction,
        from: LocalDate,
        to: LocalDate,
    ) {
        assertEquals(0, BigDecimal(expected).compareTo(transaction.amountInPeriod(from, to, today = from)))
    }
}
