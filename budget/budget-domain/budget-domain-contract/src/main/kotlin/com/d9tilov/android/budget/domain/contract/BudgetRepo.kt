package com.d9tilov.android.budget.domain.contract

import com.d9tilov.android.budget.domain.model.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetRepo {
    fun get(): Flow<BudgetData>

    suspend fun create(currencyCode: String)

    suspend fun update(budgetData: BudgetData)

    suspend fun delete(budgetData: BudgetData)
}
