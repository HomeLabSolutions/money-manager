package com.d9tilov.android.currency.domain.contract

import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.CurrencyRate
import com.d9tilov.android.currency.domain.model.DomainCurrency
import kotlinx.coroutines.flow.Flow

interface CurrencyInteractor {
    fun getCurrencies(): Flow<List<DomainCurrency>>

    fun getMainCurrencyFlow(): Flow<CurrencyMetaData>

    suspend fun getMainCurrency(): CurrencyMetaData

    suspend fun getCurrencyByCode(code: String): CurrencyRate

    suspend fun toTargetCurrency(
        amount: Decimal,
        sourceCurrencyCode: String,
        targetCurrencyCode: String,
    ): Decimal

    suspend fun toUsd(
        amount: Decimal,
        currencyCode: String,
    ): Decimal

    suspend fun updateCurrencyRates(): Result<Boolean>

    suspend fun updateMainCurrency(code: String)
}
