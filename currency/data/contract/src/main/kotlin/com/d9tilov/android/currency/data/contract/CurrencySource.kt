package com.d9tilov.android.currency.data.contract

import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface CurrencySource {
    fun getMainCurrency(): Flow<CurrencyMetaData>

    suspend fun saveCurrencies(currencies: List<CurrencyRate>)

    suspend fun getCurrencyByCode(code: String): CurrencyRate

    suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean

    suspend fun update(currency: CurrencyRate)

    suspend fun updateMainCurrency(code: String)

    fun getCurrencies(): Flow<List<CurrencyRate>>
}
