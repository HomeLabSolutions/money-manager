package com.d9tilov.android.currency.domain.contract

import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.DomainCurrency
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CurrencyInteractor {

    fun getCurrencies(): Flow<List<DomainCurrency>>
    fun getMainCurrencyFlow(): Flow<CurrencyMetaData>
    suspend fun getMainCurrency(): CurrencyMetaData
    suspend fun getCurrencyByCode(code: String): Currency
    suspend fun toTargetCurrency(
        amount: BigDecimal,
        sourceCurrencyCode: String,
        targetCurrencyCode: String
    ): BigDecimal

    suspend fun toUsd(amount: BigDecimal, currencyCode: String): BigDecimal
    suspend fun updateCurrencyRates(): Boolean
    suspend fun updateMainCurrency(code: String)
}
