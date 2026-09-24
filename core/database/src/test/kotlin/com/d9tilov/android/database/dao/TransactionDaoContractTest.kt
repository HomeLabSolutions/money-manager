package com.d9tilov.android.database.dao

import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionDaoContractTest {
    @Test
    fun `period query delegates statistics and regular filters to SQL`() {
        val periodQuery =
            TransactionDao::class.java.declaredMethods.single {
                it.name == "getAllByTypeInPeriod"
            }

        assertEquals(6, periodQuery.parameterCount)
    }

    @Test
    fun `category period query delegates statistics filter to SQL`() {
        val categoryPeriodQuery =
            TransactionDao::class.java.declaredMethods.single {
                it.name == "getByCategoryIdInPeriod"
            }

        assertEquals(5, categoryPeriodQuery.parameterCount)
    }
}
