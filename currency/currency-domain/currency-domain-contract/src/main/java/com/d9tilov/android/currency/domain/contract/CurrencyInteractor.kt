package com.d9tilov.android.currency.domain.contract

import com.d9tilov.android.currency.data.model.Currency
import com.d9tilov.android.currency.data.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.DomainCurrency
import java.math.BigDecimal
import kotlinx.coroutines.flow.Flow

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
