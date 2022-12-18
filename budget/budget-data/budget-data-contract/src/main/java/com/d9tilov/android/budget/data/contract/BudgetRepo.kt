package com.d9tilov.android.budget.data.contract

import com.d9tilov.android.budget.data.model.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetRepo {

    fun get(): Flow<BudgetData>
    suspend fun create(currencyCode: String)
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
