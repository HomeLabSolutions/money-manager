package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.firstOrNull

class BudgetInteractorImpl(
    private val budgetRepo: BudgetRepo,
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor
) : BudgetInteractor {

    override fun get() = budgetRepo.get()
    override suspend fun create() = budgetRepo.create(
        userInteractor.getCurrentCurrencyFlow().firstOrNull()?.code ?: DEFAULT_CURRENCY_CODE
    )

    override suspend fun update(budgetData: BudgetData) = budgetRepo.update(budgetData)
    override suspend fun updateBudgetWithCurrency(currency: String) {
        val budget = budgetRepo.get().firstOrNull()
        budget?.let {
            val newAmount =
                currencyInteractor.toTargetCurrency(it.sum, it.currencyCode, currency)
            val newSavedAmount = currencyInteractor.toTargetCurrency(
                it.saveSum,
                it.currencyCode,
                currency
            )
            budgetRepo.update(
                it.copy(
                    sum = newAmount,
                    saveSum = newSavedAmount,
                    currencyCode = currency
                )
            )
        }
    }

    override suspend fun delete(budgetData: BudgetData) = budgetRepo.delete(budgetData)
}
