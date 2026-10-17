package com.d9tilov.android.transaction.ui.model

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.model.Transaction
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionPlaceIndicatorTest {
    @Test
    fun `expense with saved name exposes location indicator on all lists`() {
        val transaction = Transaction.EMPTY.copy(placeName = "Cafe")
        assertTrue(transaction.toUiModel().hasPlace)
        assertTrue(transaction.toUiModel(isStatisticsScreen = true).hasPlace)
    }

    @Test
    fun `expense without a place has no location indicator`() {
        listOf(null, "", "  ").forEach { name ->
            assertFalse(
                Transaction.EMPTY
                    .copy(placeName = name)
                    .toUiModel()
                    .hasPlace,
            )
        }
    }

    @Test
    fun `income never shows location indicator`() {
        assertFalse(
            Transaction.EMPTY
                .copy(type = TransactionType.INCOME, placeName = "Cafe")
                .toUiModel()
                .hasPlace,
        )
    }
}
