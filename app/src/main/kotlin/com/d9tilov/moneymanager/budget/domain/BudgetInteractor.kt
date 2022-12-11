package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.android.core.model.Interactor
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetInteractor : Interactor {

    fun get(): Flow<BudgetData>
    suspend fun create()
    suspend fun update(budgetData: BudgetData)
    suspend fun updateBudgetWithCurrency(currency: String)
    suspend fun delete(budgetData: BudgetData)
}
