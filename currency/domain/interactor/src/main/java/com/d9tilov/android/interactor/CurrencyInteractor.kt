package com.d9tilov.android.interactor

import com.d9tilov.android.interactor.model.DomainCurrency
import java.math.BigDecimal
import kotlinx.coroutines.flow.Flow

interface CurrencyInteractor {

    fun getCurrencies(): Flow<List<DomainCurrency>>
    fun getMainCurrencyFlow(): Flow<com.d9tilov.android.repo.model.CurrencyMetaData>
    suspend fun getMainCurrency(): com.d9tilov.android.repo.model.CurrencyMetaData
    suspend fun getCurrencyByCode(code: String): com.d9tilov.android.repo.model.Currency
    suspend fun toTargetCurrency(
        amount: BigDecimal,
        sourceCurrencyCode: String,
        targetCurrencyCode: String
    ): BigDecimal

    suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal
    suspend fun updateCurrencyRates(): Boolean
    suspend fun updateMainCurrency(code: String)
}
