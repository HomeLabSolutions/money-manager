package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetInteractor : Interactor {

    fun get(): Flow<BudgetData>
    suspend fun create()
    suspend fun update(budgetData: BudgetData)
    suspend fun updateBudgetWithCurrency(currencyCode: String)
    suspend fun delete(budgetData: BudgetData)
}
