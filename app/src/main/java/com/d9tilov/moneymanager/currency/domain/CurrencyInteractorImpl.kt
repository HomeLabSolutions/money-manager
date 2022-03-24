package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.util.Calendar

class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo,
    private val domainMapper: CurrencyDomainMapper
) : CurrencyInteractor {

    override fun getCurrencies(): Flow<List<DomainCurrency>> {
        return flow { emit(currencyRepo.getCurrentCurrency()) }
            .flatMapConcat { baseCurrency ->
                currencyRepo.getCurrencies()
                    .map { list ->
                        list.map { item ->
                            domainMapper.toDomain(item, baseCurrency.code == item.code)
                        }
                    }
            }
    }

    override fun getCurrentCurrency(): CurrencyMetaData = currencyRepo.getCurrentCurrency()

    override suspend fun getCurrencyByCode(code: String): Currency =
        currencyRepo.getCurrencyByCode(code)

    override suspend fun toMainCurrency(
        amount: BigDecimal,
        currencyCode: String
    ): BigDecimal {
        val mainCurrencyCode = getCurrentCurrency()
        if (currencyCode == mainCurrencyCode.code) return amount
        val mainCurrency = getCurrencyByCode(mainCurrencyCode.code)
        val currentCurrency = getCurrencyByCode(currencyCode)
        val c = Calendar.getInstance()
        c.timeInMillis = mainCurrency.lastUpdateTime
        val mainAbsoluteAmount = mainCurrency.value
        val currentAbsoluteAmount = currentCurrency.value
        return amount.multiply(mainAbsoluteAmount.divideBy(currentAbsoluteAmount))
    }

    override suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal {
        val mainCurrencyCode = DataConstants.DEFAULT_CURRENCY_CODE
        if (currencyCode == mainCurrencyCode) return amount
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
    }
}
