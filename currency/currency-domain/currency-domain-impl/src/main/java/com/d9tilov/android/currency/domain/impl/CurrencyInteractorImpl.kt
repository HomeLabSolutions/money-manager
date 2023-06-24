package com.d9tilov.android.currency.domain.impl

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.utils.divideBy
import com.d9tilov.android.core.utils.removeScale
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.contract.mapper.toDomain
import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.DomainCurrency
import java.math.BigDecimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class CurrencyInteractorImpl(private val currencyRepo: CurrencyRepo) : CurrencyInteractor {

    override fun getCurrencies(): Flow<List<DomainCurrency>> {
        return currencyRepo.getMainCurrency()
            .flatMapMerge { baseCurrency ->
                currencyRepo.getCurrencies()
                    .map { list -> list.map { item -> item.toDomain(baseCurrency.code == item.code) } }
            }
    }

    override fun getMainCurrencyFlow(): Flow<CurrencyMetaData> = currencyRepo.getMainCurrency()

    override suspend fun getMainCurrency(): CurrencyMetaData =
        currencyRepo.getMainCurrency().firstOrNull() ?: CurrencyMetaData.EMPTY

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
        val mainCurrencyCode = DEFAULT_CURRENCY_CODE
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

    override suspend fun updateMainCurrency(code: String) = currencyRepo.updateMainCurrency(code)
}
