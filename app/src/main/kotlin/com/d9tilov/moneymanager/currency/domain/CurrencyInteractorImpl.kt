package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.divideBy
import com.d9tilov.moneymanager.core.util.removeScale
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

class CurrencyInteractorImpl(
    private val userInteractor: UserInteractor,
    private val currencyRepo: CurrencyRepo,
    private val domainMapper: CurrencyDomainMapper
) : CurrencyInteractor {

    override fun getCurrencies(): Flow<List<DomainCurrency>> {
        return userInteractor.getCurrentCurrencyFlow()
            .flatMapMerge { baseCurrency ->
                currencyRepo.getCurrencies()
                    .map { list ->
                        list.map { item ->
                            domainMapper.toDomain(item, baseCurrency.code == item.code)
                        }
                    }
            }
    }

    override suspend fun getCurrencyByCode(code: String): Currency =
        currencyRepo.getCurrencyByCode(code)

    override suspend fun toTargetCurrency(
        amount: BigDecimal,
        sourceCurrencyCode: String,
        targetCurrencyCode: String
    ): BigDecimal {
        if (sourceCurrencyCode == targetCurrencyCode) return amount
        val targetCurrency = getCurrencyByCode(targetCurrencyCode)
        val sourceCurrency = getCurrencyByCode(sourceCurrencyCode)
        val mainAbsoluteAmount = targetCurrency.value
        val currentAbsoluteAmount = sourceCurrency.value
        return amount.multiply(mainAbsoluteAmount.divideBy(currentAbsoluteAmount)).removeScale
    }

    override suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal {
        val mainCurrencyCode = DataConstants.DEFAULT_CURRENCY_CODE
        if (currencyCode == mainCurrencyCode) return amount
        val currentCurrency = getCurrencyByCode(currencyCode)
        val currentAbsoluteAmount = currentCurrency.value
        return amount.divideBy(currentAbsoluteAmount)
    }

    override suspend fun updateCurrencyRates(): Boolean {
        return try {
            currencyRepo.updateCurrencies()
            true
        } catch (ex: Exception) {
            false
        }
    }
}
