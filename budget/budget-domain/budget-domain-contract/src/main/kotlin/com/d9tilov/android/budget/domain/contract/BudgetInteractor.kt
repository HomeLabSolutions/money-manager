package com.d9tilov.android.budget.domain.contract

import com.d9tilov.android.budget.domain.model.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetInteractor {

    fun get(): Flow<BudgetData>
    suspend fun create()
    suspend fun update(budgetData: BudgetData)
    suspend fun updateBudgetWithCurrency(currency: String)
    suspend fun delete(budgetData: BudgetData)
}
