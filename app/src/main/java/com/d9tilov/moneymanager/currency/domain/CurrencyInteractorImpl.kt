package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.math.BigDecimal
import java.util.Calendar

class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo,
    private val userInteractor: UserInteractor,
    private val domainMapper: CurrencyDomainMapper,
    private val budgetInteractor: BudgetInteractor,
) : CurrencyInteractor {

    override fun getCurrencies(): Flow<List<DomainCurrency>> {
        return flow { emit(userInteractor.getCurrentCurrency()) }
            .flatMapConcat { baseCurrency ->
                currencyRepo.getCurrencies()
                    .map { list ->
                        list.map { item ->
                            domainMapper.toDomain(
                                item,
                                baseCurrency == item.code
                            )
                        }
                    }
            }
    }

    override suspend fun getCurrentCurrencyCode(): String = userInteractor.getCurrentCurrency()

    override suspend fun getCurrencyByCode(code: String): Currency =
        currencyRepo.getCurrencyByCode(code)

    override suspend fun convertToMainCurrency(
        amount: BigDecimal,
        currencyCode: String
    ): BigDecimal {
        val mainCurrencyCode = getCurrentCurrencyCode()
        if (currencyCode == mainCurrencyCode) return amount
        val mainCurrency = getCurrencyByCode(mainCurrencyCode)
        val currentCurrency = getCurrencyByCode(currencyCode)
        val c = Calendar.getInstance()
        c.timeInMillis = mainCurrency.lastUpdateTime
        val mainAbsoluteAmount = mainCurrency.value
        val currentAbsoluteAmount = currentCurrency.value
        return amount.multiply(mainAbsoluteAmount.divideBy(currentAbsoluteAmount))
    }

    override suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal {
        val mainCurrencyCode = DataConstants.DEFAULT_CURRENCY_CODE
        val mainCurrency = getCurrencyByCode(mainCurrencyCode)
        val currentCurrency = getCurrencyByCode(currencyCode)
        val c = Calendar.getInstance()
        c.timeInMillis = mainCurrency.lastUpdateTime
        val currentAbsoluteAmount = currentCurrency.value
        return amount.divideBy(currentAbsoluteAmount)
    }

    override suspend fun updateCurrencyRates() {
        currencyRepo.updateCurrencies()
    }

    override suspend fun updateCurrency(currency: Currency) = currencyRepo.updateCurrency(currency)

    override suspend fun updateCurrentCurrency(currency: DomainCurrency) {
        currencyRepo.updateCurrentCurrency(domainMapper.toDataModel(currency))
        val user = userInteractor.getCurrentUser().first()
        userInteractor.updateUser(user.copy(currentCurrencyCode = currency.code))
        budgetInteractor.get()
            .catch { Timber.d("Can't update non-created budget") }
            .collect { budget ->
                val newAmount = convertToMainCurrency(budget.sum, budget.currencyCode)
                val newSavedAmount = convertToMainCurrency(budget.saveSum, budget.currencyCode)
                budgetInteractor.update(
                    budget.copy(
                        sum = newAmount,
                        saveSum = newSavedAmount,
                        currencyCode = currency.code
                    )
                )
            }
    }
}
