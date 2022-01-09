package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface BudgetInteractor {

    suspend fun create(budgetData: BudgetData)
    fun get(): Flow<BudgetData>
    suspend fun update(budgetData: BudgetData)
    suspend fun updateCurrency(sum: BigDecimal,
                       savedSum: BigDecimal,
                       currencyCode: String)
    suspend fun delete(budgetData: BudgetData)
}
