package com.d9tilov.android.transaction.ui.model

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.places.domain.model.Place
import com.d9tilov.android.transaction.domain.model.Transaction
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionPlaceIndicatorTest {
    @Test
    fun `expense with saved name exposes location indicator on all lists`() {
        val transaction = Transaction.EMPTY.copy(place = Place(name = "Cafe"))
        assertTrue(transaction.toUiModel().hasPlace)
        assertTrue(transaction.toUiModel(isStatisticsScreen = true).hasPlace)
    }

    @Test
    fun `expense without a place has no location indicator`() {
        listOf(null, "", "  ").forEach { name ->
            assertFalse(
                Transaction.EMPTY
                    .copy(place = Place(name = name))
                    .toUiModel()
                    .hasPlace,
            )
        }
    }

    @Test
    fun `income with saved name exposes location indicator`() {
        assertTrue(
            Transaction.EMPTY
                .copy(type = TransactionType.INCOME, place = Place(name = "Cafe"))
                .toUiModel()
                .hasPlace,
        )
    }
}
