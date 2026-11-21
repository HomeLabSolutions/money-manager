package com.d9tilov.android.currency.domain.contract

import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepo {
    fun getCurrencies(): Flow<List<CurrencyRate>>

    fun getMainCurrency(): Flow<CurrencyMetaData>

    suspend fun getCurrencyByCode(code: String): CurrencyRate

    suspend fun updateCurrencies()

    suspend fun updateMainCurrency(code: String)

    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean
}
