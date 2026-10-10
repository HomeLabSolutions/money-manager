package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.insights.data.contract.model.DailyTotalDto
import com.d9tilov.android.insights.domain.model.InsightTransaction
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

        val traces = DefaultInsightsAggregator().aggregateTransactions(rows)

        assertEquals(setOf("Cafe", "Food"), traces.keys)
        assertEquals(
            listOf(
                DailyTotalDto("2026-09-26", "expense", "USD", "200"),
                DailyTotalDto("2026-09-27", "expense", "EUR", "20"),
                DailyTotalDto("2026-09-27", "expense", "USD", "400"),
            ),
            traces.getValue("Cafe"),
        )
        assertEquals(1, traces.getValue("Food").size)
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
