package com.d9tilov.android.insights.data

import com.d9tilov.android.core.model.TransactionType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class CategoryTraceTest {
    @Test
    fun `groups transactions into date-sorted daily category totals`() {
        val rows =
            listOf(
                row(27, "Cafe", "USD", "100"),
                row(26, "Cafe", "USD", "200"),
                row(27, "Cafe", "USD", "300"),
                row(27, "Cafe", "EUR", "20"),
                row(27, "Food", "USD", "50"),
            )

        val traces = aggregateTransactions(rows)

        assertEquals(setOf("Cafe", "Food"), traces.keys)
        assertEquals(
            listOf(
                DailyTotal(LocalDate(2026, 9, 26), TransactionType.EXPENSE, "USD", BigDecimal("200")),
                DailyTotal(LocalDate(2026, 9, 27), TransactionType.EXPENSE, "EUR", BigDecimal("20")),
                DailyTotal(LocalDate(2026, 9, 27), TransactionType.EXPENSE, "USD", BigDecimal("400")),
            ),
            traces.getValue("Cafe").days,
        )
        assertEquals(1, traces.getValue("Food").days.size)
        assertEquals(
            listOf("2026-09-26", "2026-09-27", "2026-09-27"),
            traces.toPayload().getValue("Cafe").map { it["date"] },
        )
        assertEquals("expense", traces.toPayload().getValue("Cafe").first()["type"])
    }

    private fun row(
        day: Int,
        category: String,
        currency: String,
        amount: String,
    ) = InsightTransaction(
        type = TransactionType.EXPENSE,
        date = LocalDateTime(2026, 9, day, 12, 0),
        currency = currency,
        amount = BigDecimal(amount),
        category = category,
    )
}
