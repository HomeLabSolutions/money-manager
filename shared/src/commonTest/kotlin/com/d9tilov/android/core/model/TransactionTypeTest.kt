package com.d9tilov.android.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TransactionTypeTest {
    @Test
    fun persistedValuesRetainTheirMeaning() {
        assertEquals(TransactionType.INCOME, 0.toType())
        assertEquals(TransactionType.EXPENSE, 1.toType())
        assertEquals("income", 0.toType().name)
        assertEquals("expense", 1.toType().name)
    }

    @Test
    fun transactionTypesRoundTripThroughStorage() {
        listOf(TransactionType.INCOME, TransactionType.EXPENSE).forEach { type ->
            assertEquals(type, type.value.toType())
        }
    }

    @Test
    fun incomeClassificationMatchesTransactionType() {
        assertTrue(TransactionType.INCOME.isIncome())
        assertFalse(TransactionType.EXPENSE.isIncome())
    }

    @Test
    fun unknownStoredValuesAreRejected() {
        listOf(-1, 2, Int.MIN_VALUE, Int.MAX_VALUE).forEach { value ->
            val exception = assertFailsWith<IllegalArgumentException> { value.toType() }
            assertEquals("Wrong transaction type: $value", exception.message)
        }
    }
}
