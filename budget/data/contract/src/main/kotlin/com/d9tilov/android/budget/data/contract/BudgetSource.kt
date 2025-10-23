package com.d9tilov.android.budget.data.contract

import com.d9tilov.android.budget.domain.model.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetSource {
    fun get(): Flow<BudgetData?>

    suspend fun createIfNeeded(currencyCode: String): BudgetData

    suspend fun update(budgetData: BudgetData)

    suspend fun delete(budgetData: BudgetData)
}
