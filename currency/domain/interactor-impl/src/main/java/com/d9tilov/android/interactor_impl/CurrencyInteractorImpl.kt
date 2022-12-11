package com.d9tilov.android.interactor_impl

import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.utils.divideBy
import com.d9tilov.android.core.utils.removeScale
import com.d9tilov.android.interactor.CurrencyInteractor
import com.d9tilov.android.interactor.model.DomainCurrency
import com.d9tilov.android.interactor_impl.mapper.CurrencyDomainMapper
import com.d9tilov.android.repo.CurrencyRepo
import com.d9tilov.android.repo.model.Currency
import com.d9tilov.android.repo.model.CurrencyMetaData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

class CurrencyInteractorImpl(
    private val currencyRepo: CurrencyRepo,
    private val domainMapper: CurrencyDomainMapper
) : CurrencyInteractor {

    override fun getCurrencies(): Flow<List<DomainCurrency>> {
        return currencyRepo.getMainCurrency()
            .flatMapMerge { baseCurrency ->
                currencyRepo.getCurrencies()
                    .map { list ->
                        list.map { item ->
                            domainMapper.toDomain(item, baseCurrency.code == item.code)
                        }
                    }
            }
    }

    override fun getMainCurrencyFlow(): Flow<CurrencyMetaData> = currencyRepo.getMainCurrency()

    override suspend fun getMainCurrency(): CurrencyMetaData =
        currencyRepo.getMainCurrency().first()

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

    override suspend fun updateMainCurrency(code: String) = currencyRepo.updateMainCurrency(code)
}
