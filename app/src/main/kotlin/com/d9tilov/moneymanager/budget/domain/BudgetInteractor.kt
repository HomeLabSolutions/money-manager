package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetInteractor {

    fun get(): Flow<BudgetData>
    suspend fun create(budgetData: BudgetData)
    suspend fun update(budgetData: BudgetData)
    suspend fun updateBudgetWithCurrency(currencyCode: String)
    suspend fun delete(budgetData: BudgetData)
}