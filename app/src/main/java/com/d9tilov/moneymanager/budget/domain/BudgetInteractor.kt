package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetInteractor {

    suspend fun create(budgetData: BudgetData)
    fun get(): Flow<BudgetData>
    suspend fun update(budgetData: BudgetData)
    suspend fun updateBudgetWithCurrency(currencyCode: String)
    suspend fun delete(budgetData: BudgetData)
}
